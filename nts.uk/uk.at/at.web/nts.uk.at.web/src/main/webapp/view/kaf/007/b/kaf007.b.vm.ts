module nts.uk.at.view.kaf007_ref.c.viewmodel {
    //import Kaf000BViewModel = nts.uk.at.view.kaf000.b.viewmodel.Kaf000BViewModel;
    import Application = nts.uk.at.view.kaf000.shr.viewmodel.Application;
    import AppWorkChange = nts.uk.at.view.kaf007_ref.shr.viewmodel.AppWorkChange; 
	import PrintContentOfEachAppDto = nts.uk.at.view.kaf000.shr.viewmodel.PrintContentOfEachAppDto;
	import AppType = nts.uk.at.view.kaf000.shr.viewmodel.model.AppType;
    
    @component({
        name: 'kaf007-b',
        template: '/nts.uk.at.web/view/kaf/007/b/index.html'
    })
    class Kaf007CViewModel extends ko.ViewModel {
        
		appType: KnockoutObservable<number> = ko.observable(AppType.WORK_CHANGE_APPLICATION);
        appDispInfoStartupOutput: any;
        application: KnockoutObservable<Application>;
        appWorkChange: KnockoutObservable<AppWorkChange>;
        approvalReason: KnockoutObservable<string>;
        
        created(
            params: { 
				appType: any,
            	application: any,
				printContentOfEachAppDto: PrintContentOfEachAppDto,
            	approvalReason: any,
                appDispInfoStartupOutput: any, 
                eventUpdate: (evt: () => void ) => void
            }
        ) {
            const vm = this;
            vm.appDispInfoStartupOutput = params.appDispInfoStartupOutput;
            vm.application = params.application,
			vm.appType = params.appType,
            vm.appWorkChange = ko.observable(new AppWorkChange("001", "001", 100, 200));
            vm.approvalReason = params.approvalReason;
			
			params.printContentOfEachAppDto.opPrintContentOfWorkChange = {
				appWorkChangeDispInfo: null,
				appWorkChange: null
			};
            
            // gui event con ra viewmodel cha
            // nhớ dùng bind(vm) để ngữ cảnh lúc thực thi
            // luôn là component
            params.eventUpdate(vm.update.bind(vm));
        }
    
        mounted() {
            const vm = this;
        }
        
        // event update cần gọi lại ở button của view cha
        update() {
            const vm = this;
            vm.$blockui("show");
            return vm.$ajax(API.updateworkchange, {
                workChange: ko.toJS(vm.appWorkChange()),
                application: ko.toJS(vm.application()),
                appDispInfoStartupOutput: ko.toJS(vm.appDispInfoStartupOutput())
            }).done((successData: any) => {
                vm.$dialog.info({ messageId: "Msg_15" }).then(() => {
                    vm.$blockui("hide");
                });
            }).fail((failData: any) => {
                vm.$blockui("hide");    
            });  
        }
        
        dispose() {
            const vm = this;
            
        }
    }
    
    const API = {
        getWorkchangeByAppID_PC: "at/request/application/workchange/getWorkchangeByAppID_PC",
        updateworkchange: "at/request/application/workchange/updateworkchange"
    }
}