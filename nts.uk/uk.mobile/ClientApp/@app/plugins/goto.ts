import { Vue, VueConstructor } from '@app/provider';
import { obj } from '@app/utils';

const goto = {
    install(vue: VueConstructor<Vue>) {
        vue.mixin({
            beforeMount() {
                let self: Vue = this;

                if (self && self.$router) {
                    obj.extend(self.$router, {
                        goto(location: { name: string; params: { [key: string]: any } }) {
                            return new Promise((resolve, reject) => {
                                (self.$router as any).push({
                                    name: location.name,
                                    params: {
                                        params: location.params
                                    }
                                }, resolve, reject);
                            });
                        }
                    });

                    obj.extend(self.$goto, {
                        home(params?: any) {
                            return self.$goto('ccg008a', params);
                        },
                        login(params?: any) {
                            return self.$goto('ccg007b', params);
                        },
                        password: {
                            change(params?: any) {
                                return self.$goto('ccg007c', params);
                            },
                            forget(params?: any) {
                                return self.$goto('ccg007d', params);
                            },
                            reset(params?: any) {
                                return self.$goto('ccg007e', params);
                            },
                            mail(params?: any) {
                                return self.$goto('ccg007f', params);
                            }
                        }
                    });

                    obj.extend(self.$router.goto, {
                        home(params?: any) {
                            return self.$goto.home(params);
                        },
                        login(params?: any) {
                            return self.$goto.login(params);
                        },
                        password: {
                            change(params?: any) {
                                return self.$goto.password.change(params);
                            },
                            forget(params?: any) {
                                return self.$goto.password.forget(params);
                            },
                            reset(params?: any) {
                                return self.$goto.password.reset(params);
                            },
                            mail(params?: any) {
                                return self.$goto.password.mail(params);
                            }
                        }
                    });
                }
            }
        });

        vue.prototype.$goto = function (nameOrLocation: { name: string; params: { [key: string]: any; }; }, params?: { [key: string]: any; }) {
            let self: Vue = this;

            return new Promise((resolve, reject) => {
                if (typeof nameOrLocation !== 'string') {
                    self.$router.push({
                        name: nameOrLocation.name,
                        params: {
                            params: nameOrLocation.params
                        } as any
                    }, resolve, reject);
                } else {
                    self.$router.push({
                        name: nameOrLocation,
                        params: { params } as any
                    }, resolve, reject);
                }
            });
        };
    }
};

Vue.use(goto);