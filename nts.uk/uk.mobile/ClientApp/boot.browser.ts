import '@app/index';
import '@views/index';

import { router } from '@app/core/router';
import { Vue, Vuex, VueRouter } from '@app/provider';
import { tojs, bstrp, ajax, resources, i18n, mask, modal, dialog, picker, validate, Language, LanguageBar } from '@app/plugins';

import { obj, browser } from '@app/utils';
import { SideMenuBar, NavMenuBar } from '@app/components';

// use ajax request
Vue.use(ajax, 'webapi');

Vue.use(tojs);
Vue.use(bstrp);
Vue.use(modal);
Vue.use(dialog);

Vue.use(i18n);
Vue.use(mask);
Vue.use(picker);

Vue.use(validate);

Vue.use(Vuex);
Vue.use(VueRouter);

Vue.config.silent = true;
Vue.config.devtools = true;
Vue.config.productionTip = false;

new Vue({
    router,
    components: {
        'nav-bar': NavMenuBar,
        'side-bar': SideMenuBar,
        'language-bar': LanguageBar
    },
    el: document.querySelector('body>#app_uk_mobile'),
    computed: {
        pgName: {
            get() {
                return Language.pgName || 'app_name';
            }
        }
    },
    beforeCreate() {
        const self = this,
            rapi = '/i18n/resources/mobile/get';

        browser.private
            .then((prid: boolean) => {
                if (browser.ios && prid) {
                    self.$modal.warn('Msg_1533');
                }
            });

        self.$http.get(rapi)
            .then((resp: { data: any }) => {
                obj.merge(resources,
                    {
                        jp: resp.data
                    });
            })
            .then(() => {
                Language.refresh();
            });
    }
});