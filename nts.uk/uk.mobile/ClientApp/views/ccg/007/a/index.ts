import { Vue } from '@app/provider';
import { component, Prop } from '@app/core/component';
import { storage, auth } from '@app/utils';
import { NavMenu, SideMenu } from '@app/services';

@component({
    route: '/ccg/007/a',
    style: require('./style.scss'),
    template: require('./index.vue'),
    validations: {
        model: {
            contractCode: {
                required: false
            },
            password: {
                required: false
            }
        }
    },
    name: 'ccg007a'
})
export class Ccg007AComponent extends Vue {

    @Prop({ default: () => ({}) })
    public params!: any;

    public model = {
        contractCode: '',
        password: ''
    };

    public mounted() {
        const vm = this;
        vm.$nextTick(() => {
            vm.$parent.$el.querySelector('.container-fluid').classList.add('uk-bg-process');
        });
    }

    public authContract() {
        const vm = this;
        vm.$mask('show', { message: true });
        vm.$validate();
        if (!vm.$valid) {
            vm.$mask('hide');
            return;
        }
        vm.$http.post(servicePath.submitcontract, vm.model).then((response) => {
            vm.$mask('hide');
            storage.local.setItem('contract', { code: vm.model.contractCode, password: vm.model.password });
            vm.$router.go(-1);
        }).catch((error) => {
            vm.$mask('hide');
            vm.$modal.error({ messageId: error.messageId });
        });
    }

    public beforeDestroy() {
        const vm = this;
        vm.$parent.$el.querySelector('.container-fluid').classList.remove('uk-bg-process');
    }

}

const servicePath = {
    submitcontract: 'ctx/sys/gateway/login/submitcontract'
};