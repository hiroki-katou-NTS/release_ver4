module nts.uk.at.view.kaf009_ref.a.viewmodel {
    import Application = nts.uk.at.view.kaf000_ref.shr.viewmodel.Application;
    import CommonProcess = nts.uk.at.view.kaf000_ref.shr.viewmodel.CommonProcess;
    import Model = nts.uk.at.view.kaf009_ref.shr.viewmodel.Model;
    import AppType = nts.uk.at.view.kaf000_ref.shr.viewmodel.model.AppType;
    import Kaf000AViewModel = nts.uk.at.view.kaf000_ref.a.viewmodel.Kaf000AViewModel;

    @bean()
    class Kaf009AViewModel extends Kaf000AViewModel {
        
		appType: KnockoutObservable<number> = ko.observable(AppType.GO_RETURN_DIRECTLY_APPLICATION);
        application: KnockoutObservable<Application>;
        applicationTest: any = {
                employeeID: this.$user.employeeId,
                appType: '4',
                appDate: moment(new Date()).format('YYYY/MM/DD'),
                enteredPerson: this.$user.employeeId,
                inputDate: moment(new Date()).format('YYYY/MM/DD HH:mm:ss'),
                reflectionStatus: {
                    listReflectionStatusOfDay: [{
                        actualReflectStatus: 1,
                        scheReflectStatus: 1,
                        targetDate: '2020/01/07',
                        opUpdateStatusAppReflect: {
                            opActualReflectDateTime: '2020/01/07 20:11:11',
                            opScheReflectDateTime: '2020/01/07 20:11:11',
                            opReasonActualCantReflect: 1,
                            opReasonScheCantReflect: 0

                        },
                        opUpdateStatusAppCancel: {
                            opActualReflectDateTime: '2020/01/07 20:11:11',
                            opScheReflectDateTime: '2020/01/07 20:11:11',
                            opReasonActualCantReflect: 1,
                            opReasonScheCantReflect: 0
                        }
                    }]
                }
                


            };
        model: Model;
        dataFetch: KnockoutObservable<ModelDto> = ko.observable(null);
        mode: string = 'edit';

        created(params: any) {
            const vm = this;
            vm.application = ko.observable(new Application(vm.appType()));
            vm.model = new Model(true, true, true, '', '', '', '');            
            vm.$blockui("show");
            vm.loadData([], [], vm.appType())
            .then((loadDataFlag: any) => {
                vm.appDispInfoStartupOutput.subscribe(value => {
                    console.log(value);
                    if (value) {
                        vm.changeDate();
                    }
                });
                if(loadDataFlag) {
                    let ApplicantEmployeeID: null,
                        ApplicantList: null,
                        appDispInfoStartupOutput = ko.toJS(vm.appDispInfoStartupOutput),
                        command = { ApplicantEmployeeID, ApplicantList, appDispInfoStartupOutput };
                        
                    return vm.$ajax(API.startNew, command);
                }
            }).then((res: any) => {
                if(res) {
                    vm.dataFetch({
                        workType: ko.observable(res.workType),
                        workTime: ko.observable(res.workTime),
                        appDispInfoStartup: ko.observable(res.appDispInfoStartup),
                        goBackReflect: ko.observable(res.goBackReflect),
                        lstWorkType: ko.observable(res.lstWorkType),
                        goBackApplication: ko.observable(res.goBackApplication),
                        isChangeDate: false
                    });     
                }
            }).fail((failData: any) => {
                let param;
                if (failData.message) {
                    param = {message: failData.message, messageParams: failData.parameterIds};
                } else {
                    param = {messageId: failData.messageId, messageParams: failData.parameterIds}
                }
                vm.$dialog.error(param); 
                
            }).always(() => vm.$blockui("hide"));
            
            
            

        }

        mounted() {
            const vm = this;
        }
        
       
        
        public handleConfirmMessage(listMes: any, res: any) {
            let vm = this;
            if (!_.isEmpty(listMes)) {
                let item = listMes.shift();
                vm.$dialog.confirm({ messageId: item.msgID }).then((value) => {
                    if (value == 'yes') {
                        if (_.isEmpty(listMes)) {
                            vm.registerData(res);
                        } else {
                            vm.handleConfirmMessage(listMes, res);
                        }

                    }
                });
            }
        }
        
        registerData(goBackApp) {
            let vm = this;
            let paramsRegister = {
                    companyId: vm.$user.companyId,
                    applicationDto: vm.applicationTest,
                    goBackDirectlyDto: goBackApp,
                    inforGoBackCommonDirectDto: ko.toJS(vm.dataFetch),
                    mode : vm.mode == 'edit'
            }
            
            return vm.$ajax( API.register, paramsRegister )
                .done( resRegister => {
                    console.log( resRegister );
                    this.$dialog.info( { messageId: "Msg_15" } ).then(() => {
                        // bussiness logic after error show
                        location.reload();
                    } );
                })
        }

        register() {
            const vm = this;
            let application = ko.toJS(vm.application);
            vm.applicationTest.appID = application.appID;
            vm.applicationTest.appDate = application.appDate;
            vm.applicationTest.appType = application.appType;
            vm.applicationTest.prePostAtr = application.prePostAtr;
            vm.applicationTest.opAppStartDate = application.opAppStartDate;
            vm.applicationTest.opAppEndDate = application.opAppEndDate;
            vm.applicationTest.opAppReason = application.opAppReason;
            vm.applicationTest.opAppStandardReasonCD = application.opAppStandardReasonCD;    
            vm.applicationTest.opReversionReason = application.opReversionReason;
            
            vm.$blockui( "show" );
            vm.$validate('.nts-input', '#kaf000-a-component3-prePost', '#kaf000-a-component5-comboReason')
                .then( isValid => {
                    if ( isValid ) {
                        return true;
                    }
                } )
                .then( result => {
                    if ( result ) {
                        let model = ko.toJS( vm.model );
                        let goBackApp = new GoBackApplication(
                            model.checkbox1 ? 1 : 0,
                            model.checkbox2 ? 1 : 0,
                        );
                        // is change can be null
                        if (!_.isNull(model.checkbox3)) {
                            goBackApp.isChangedWork = model.checkbox3 ? 1 : 0;
                            let dw = new DataWork( model.workTypeCode );
                            if ( model.workTimeCode ) {
                                dw.workTime = model.workTimeCode
                            }
                            goBackApp.dataWork = dw;
                        }
                        console.log( goBackApp );

                        let param = {
                            companyId: this.$user.companyId,
                            agentAtr: true,
                            applicationDto: vm.applicationTest,
                            goBackDirectlyDto: goBackApp,
                            inforGoBackCommonDirectDto: ko.toJS( vm.dataFetch ),
                            mode: true
                        };
                        vm.$ajax( API.checkRegister, param )
                            .done( res => {
                                console.log( res );

                                if ( _.isEmpty( res ) ) {
                                    return vm.registerData( goBackApp );
                                } else {
                                    let listTemp = _.clone( res );
                                    vm.handleConfirmMessage( listTemp, goBackApp );

                                }
                            } )
                            .fail( err => {
                                let param;
                                if (err.message && err.messageId) {
                                    param = {messageId: err.messageId, messageParams: err.parameterIds};
                                } else {
                                    
                                    if (err.message) {
                                        param = {message: err.message, messageParams: err.parameterIds};
                                    } else {
                                        param = {messageId: err.messageId, messageParams: err.parameterIds};
                                    }
                                }
                                vm.$dialog.error(param);
                            } )
                            .always(() => vm.$blockui( "hide" ) );
                    }
                } ).always(() => vm.$blockui( "hide" ) );

            



        }
        
        changeDate() {
            const vm = this;
            if(!_.isNull(ko.toJS(vm.dataFetch))) {
                vm.dataFetch().isChangeDate = true;                
            }
            let dataClone = _.clone(vm.dataFetch());
            if (!_.isNull(dataClone)) {
                vm.dataFetch(dataClone); 
                return;
            }
            vm.$blockui( "show" );
            let ApplicantEmployeeID: null,
            ApplicantList: null,
            appDispInfoStartupOutput = ko.toJS(vm.appDispInfoStartupOutput),
            command = { ApplicantEmployeeID, ApplicantList, appDispInfoStartupOutput };
            vm.$ajax(API.startNew, command)
                .done(res => {
                    if (res) {
                        vm.dataFetch({
                            workType: ko.observable(res.workType),
                            workTime: ko.observable(res.workTime),
                            appDispInfoStartup: ko.observable(res.appDispInfoStartup),
                            goBackReflect: ko.observable(res.goBackReflect),
                            lstWorkType: ko.observable(res.lstWorkType),
                            goBackApplication: ko.observable(res.goBackApplication),
                            isChangeDate: false
                        });
                    }
                })
                .fail(res => {
                    
                    let param;
                    if (res.message) {
                        param = {message: res.message, messageParams: res.parameterIds};
                    } else {
                        param = {messageId: res.messageId, messageParams: res.parameterIds}
                    }
                    vm.$dialog.error(param);
                })
                .always(() => vm.$blockui( "hide" ));
            
        }

        

    }
    export class GoBackApplication {
        straightDistinction: number;
        straightLine: number;
        isChangedWork?: number;
        dataWork?: DataWork;
        constructor(straightDistinction: number, straightLine: number, isChangedWork?: number, dataWork?: DataWork) {
            this.straightDistinction = straightDistinction;
            this.straightLine = straightLine;
            this.isChangedWork = isChangedWork;
            this.dataWork = dataWork;
        }
    }
   
   
    export class DataWork {
        workType: string;
        workTime?: string;
        constructor(workType: string, workTime?: string) {
            this.workType = workType;
            this.workTime = workTime;
        }
    }
    export class GoBackReflect {
        companyId: string;
        reflectApplication: number;
    }
    export class ModelDto {

        workType: KnockoutObservable<any>;

        workTime: KnockoutObservable<any>;

        appDispInfoStartup: KnockoutObservable<any>;

        goBackReflect: KnockoutObservable<GoBackReflect> = ko.observable( null );

        lstWorkType: KnockoutObservable<any>;

        goBackApplication: KnockoutObservable<any>;
    
        isChangeDate: boolean = false;
    }

    const API = {
        startNew: "at/request/application/gobackdirectly/getGoBackCommonSettingNew",
        checkRegister: "at/request/application/gobackdirectly/checkBeforeRegisterNew",
        register: "at/request/application/gobackdirectly/registerNewKAF009",
        changeDate: "at/request/application/gobackdirectly/getAppDataByDate"
    }

    export class ApplicationStatus {
        //        反映しない
        public static DO_NOT_REFLECT: number = 0;
        //        反映する
        public static DO_REFLECT: number = 1;
        //      申請時に決める(初期値：反映しない)
        public static DO_NOT_REFLECT_1: number = 2;
        //        申請時に決める(初期値：反映する)
        public static DO_REFLECT_1: number = 3;
    }
    export class ParamBeforeRegister {
        companyId: string;
        agentAtr: boolean;
        applicationDto: any;
        goBackDirectlyDto: any;
        inforGoBackCommonDirectDto: any;

    }

}