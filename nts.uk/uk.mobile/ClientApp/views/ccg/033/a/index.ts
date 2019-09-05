import { Vue, _ } from '@app/provider';
import { component, Prop } from '@app/core/component';
import { CmmS45CComponent } from 'views/cmm/s45/c/index';
import { CmmS45DComponent } from 'views/cmm/s45/d/index';

@component({
    name: 'ccg033a',
    route: '/ccg/033/a',
    style: require('./style.scss'),
    template: require('./index.vue'),
    resource: require('./resources.json'),
    validations: {},
    constraints: [],
    components: {
        'cmms45c': CmmS45CComponent,
        'cmms45d': CmmS45DComponent
    }
})
export class Ccg033AComponent extends Vue {
    public created() {
        let self = this;
        let appId = self.$route.query.appId;
        let programID = self.$route.query.programID;
        if (programID == 'cmm045') {
            self.$goto('cmms45b');
        } else if (programID == 'kaf005') {
            let userAtr = self.$route.query.userAtr;
            if (userAtr == '1') {//利用者 = 承認者
                self.$modal('cmms45d', { 'listAppMeta': [appId], 'currentApp': appId });
            } else {
                self.$modal('cmms45c', { 'listAppMeta': [appId], 'currentApp': appId });
            }
        }
    }
}

const servicePath = {
    judgmentuser: 'at/request/application/approval/judgmentuser'
};