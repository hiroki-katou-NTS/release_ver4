module nts.uk.at.kha003.d {

    const API = {
        //TODO api path
    };

    @bean()
    export class ViewModel extends ko.ViewModel {
        c21Params: KnockoutObservable<any>;
        c31Params: KnockoutObservable<any>;
        c41Params: KnockoutObservable<any>;
        c51Params: KnockoutObservable<any>;
        constructor() {
            super();
            const vm = this;
            vm.c21Params = ko.observable();
            vm.c31Params = ko.observable();
            vm.c41Params = ko.observable();
            vm.c51Params = ko.observable();

        }

        created() {
            const vm = this;
            _.extend(window, {vm});
            vm.$window.storage('kha003AShareData').done((data: any) => {
                console.log('in side kha003 D:'+data)
                vm.c21Params(data.c21);
                vm.c31Params(data.c31);
                vm.c41Params(data.c41);
                vm.c51Params(data.c51);
            })
        }

        mounted() {
            const vm = this;
        }
    }
}


