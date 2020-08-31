module nts.uk.at.view.kaf000_ref.a.component2.viewmodel {

    @component({
        name: 'kaf000-a-component2',
        template: '/nts.uk.at.web/view/kaf_ref/000/a/component2/index.html'
    })
    class Kaf000AComponent2ViewModel extends ko.ViewModel {
		appType: KnockoutObservable<number> = null;
        appDispInfoStartupOutput: any;
        employeeName: KnockoutObservable<string>;
        created(params: any) {
            const vm = this;
			vm.appType = params.appType;
            vm.appDispInfoStartupOutput = params.appDispInfoStartupOutput;
            vm.employeeName = ko.observable("employeeName");
            
            vm.appDispInfoStartupOutput.subscribe(value => {
                vm.employeeName(value.appDispInfoNoDateOutput.employeeInfoLst[0].bussinessName);   
                params.application().employeeIDLst(_.map(value.appDispInfoNoDateOutput.employeeInfoLst, o => o.sid));              
            });
        }
    
        mounted() {
            const vm = this;
        }
    }
}