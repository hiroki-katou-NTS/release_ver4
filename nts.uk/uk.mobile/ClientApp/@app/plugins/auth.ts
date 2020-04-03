import { obj, auth as authentication, storage } from '@app/utils';
import { Vue, VueConstructor } from '@app/provider';

/**
 * Authentication plugin for UK Mobile
 * Extend once object $auth to all view model
 * Method: login, logout
 * Properity: user, token
 */
const auth = {
    install: (vue: VueConstructor<Vue>) => {
        // clear cookie method
        let clearAuthentication = () => {
            storage.session.removeItem('user');
            storage.session.removeItem('csrf');
        };

        vue.mixin({
            beforeCreate() {
                let self = this;

                obj.extend(self, {
                    $auth: {
                        login: (data: any) => {
                            let lapi: string = 'ctx/sys/gateway/login/submit/mobile';

                            return self.$http
                                .post(lapi, data)
                                .then((resp: { data: any }) => {
                                    if (data.saveInfo) {
                                        storage.local.setItem('remember', {
                                            companyCode: data.companyCode,
                                            employeeCode: data.employeeCode
                                        });
                                    } else {
                                        storage.local.removeItem('remember');
                                    }

                                    return self.$http.get('/ctx/sys/gateway/login/mobile/token')
                                        .then((v: { data: string | any }) => {
                                            if (typeof v.data !== 'string') {
                                                clearAuthentication();
                                            } else {
                                                storage.session.setItem('csrf', v.data);
                                            }

                                            return typeof v.data === 'string';
                                        })
                                        .then((v: boolean) => {
                                            if (v) {
                                                self.$http.post('/view-context/user')
                                                    .then((resp: { data: any }) => {
                                                        storage.session.setItem('user', resp.data);
                                                    });
                                            }
                                        })
                                        .then(() => resp.data);
                                });
                        },
                        logout: () => {
                            let lapi: string = 'sys/portal/webmenu/logout';

                            return Promise
                                .resolve()
                                // check login?
                                .then(() => authentication.valid)
                                // call api clear cookie cache (server)
                                .then((v) => v && self.$http.post(lapi))
                                // clear cookie cache (client)
                                .then(clearAuthentication)
                                // go to login page
                                .then(() => self.$goto.login());
                        }
                    }
                });

                // get token of current user logined
                Object.defineProperty(self.$auth, 'token', {
                    get() {
                        return new Promise((resolve) => {
                            let token = authentication.token;

                            if (!token) {
                                resolve(null);

                                if (!location.href.match(/(\/documents)|(\/ccg\/007\/)/)) {
                                    self.$goto.login();
                                }
                            } else {
                                resolve(token);
                            }
                        });
                    }
                });

                // get user info
                Object.defineProperty(self.$auth, 'user', {
                    get() {
                        return new Promise((resolve) => {
                            let user = authentication.user;

                            if (!user) {
                                resolve(null);


                                if (!location.href.match(/(\/documents)|(\/ccg\/007\/)/)) {
                                    self.$goto.login();
                                }
                            } else {
                                resolve(user);
                            }
                        });
                    }
                });

                // get constract info
                Object.defineProperty(self.$auth, 'contract', {
                    get() {
                        return Promise.resolve(authentication.contract);
                    }
                });
            }
        });
    }
};

Vue.use(auth);