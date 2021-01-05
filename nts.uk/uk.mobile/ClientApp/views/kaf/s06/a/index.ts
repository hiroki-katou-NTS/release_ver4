import { _, Vue } from '@app/provider';
import { component, Prop } from '@app/core/component';
import { KafS00AComponent, KafS00BComponent, KafS00CComponent } from 'views/kaf/s00';
import { KafS00ShrComponent, AppType, Application, InitParam } from 'views/kaf/s00/shr';
import { StartMobileParam } from '../a/define.interface';

@component({
    name: 'kafs06a',
    route: '/kaf/s06/a',
    style: require('./style.scss'),
    template: require('./index.vue'),
    resource: require('./resources.json'),
    validations: {},
    constraints: [],
    components: {
        'kafs00-a': KafS00AComponent,
        'kafs00-b': KafS00BComponent,
        'kafs00-c': KafS00CComponent
    }
})
export class KafS06AComponent extends KafS00ShrComponent {
    public text1: string = null;
    public isValidateAll: boolean = true;
    public user: any = null;
    public modeNew: boolean = true;
    public application: Application = null;
    public workHours1: {start: number, end: number} = null;
    public workHours2: {start: number, end: number} = null;

    public selectedValue = null;
    public time: number = 0;
    public dropdownList = [
        {
            code: null,
            text: 'Select--'
        },
        {
            code: '01',
            text: 'abcd'
        }
    ];
    @Prop() 
    public readonly params: InitParam;

    public created() {
        const vm = this;
        if (vm.params) {
            vm.modeNew = false;
            vm.appDispInfoStartupOutput = vm.params.appDispInfoStartupOutput;
        }
        if (vm.modeNew) {
            vm.application = vm.createApplicationInsert(AppType.ABSENCE_APPLICATION);
        } else {
            vm.application = vm.params.appDispInfoStartupOutput.appDetailScreenInfo.application;
        }
        vm.$auth.user.then((user: any) => {
            vm.user = user;
        }).then(() => {
            if (vm.modeNew) {
                return vm.loadCommonSetting(AppType.ABSENCE_APPLICATION);
            }
            
            return true;
        }).then((loadData: any) => {
            if (loadData) {
                vm.updateKaf000_A_Params(vm.user);
                vm.updateKaf000_B_Params(vm.modeNew);
                vm.updateKaf000_C_Params(vm.modeNew);
                let command = {

                } as StartMobileParam;
                command.mode = vm.modeNew ? MODE_NEW : MODE_UPDATE;
                command.companyId = vm.user.companyId;
                command.employeeIdOp = vm.user.employeeId;
                command.datesOp = [];
                command.appDispInfoStartupOutput = vm.appDispInfoStartupOutput;
                if (vm.modeNew) {
                    return vm.$http.post('at', API.start, command);  
                }
                
                return true;
            }
        }).then((result: any) => {
            if (result) {
                if (vm.modeNew) {
                    
                } else {

                }   
            }
        }).catch((error: any) => {
            vm.handleErrorCustom(error).then((result) => {
                if (result) {
                    vm.handleErrorCommon(error);
                }
            });
        }).then(() => vm.$mask('hide'));
    }

    public kaf000BChangeDate(objectDate) {
        const vm = this;
        if (objectDate.startDate) {
            if (vm.modeNew) {
                vm.application.appDate = vm.$dt.date(objectDate.startDate, 'YYYY/MM/DD');
                vm.application.opAppStartDate = vm.$dt.date(objectDate.startDate, 'YYYY/MM/DD');
                vm.application.opAppEndDate = vm.$dt.date(objectDate.endDate, 'YYYY/MM/DD');
                // console.log('changeDateCustom');
            }
        }
    }
    
    public kaf000BChangePrePost(prePostAtr) {
        const vm = this;
        vm.application.prePostAtr = prePostAtr;
    }

    public kaf000CChangeReasonCD(opAppStandardReasonCD) {
        const vm = this;
        vm.application.opAppStandardReasonCD = opAppStandardReasonCD;
    }

    public kaf000CChangeAppReason(opAppReason) {
        const vm = this;
        vm.application.opAppReason = opAppReason;
    }

    public customValidate(viewModel: any) {
        const vm = this;
        let validAllChild = true;
        for (let child of viewModel.$children) {
            let validChild = true;
            if (child.$children) {
                validChild = vm.customValidate(child); 
            }
            child.$validate();
            if (!child.$valid || !validChild) {
                validAllChild = false;
            }
        }

        return validAllChild;
    }

    public register() {
        const vm = this;
        vm.isValidateAll = vm.customValidate(vm);
        vm.$validate();
        if (!vm.$valid || !vm.isValidateAll) {

            return;
        }
        vm.$mask('show');
        vm.$http.post('at', API.checkBeforeRegisterSample, ['Msg_260', 'Msg_261'])
        .then((result: any) => {
            if (result) {
                // xử lý confirmMsg
                return vm.handleConfirmMessage(result.data);
            }
        }).then((result: any) => {
            if (result) {
                // đăng kí 
                return vm.$http.post('at', API.registerSample, ['Msg_15']).then(() => {
                    return vm.$modal.info({ messageId: 'Msg_15'}).then(() => {
                        return true;
                    });	
                });
            }
        }).then((result: any) => {
            if (result) {
                // gửi mail sau khi đăng kí
                // return vm.$ajax('at', API.sendMailAfterRegisterSample);
                return true;
            }
        }).catch((failData) => {
            // xử lý lỗi nghiệp vụ riêng
            vm.handleErrorCustom(failData).then((result: any) => {
                if (result) {
                    // xử lý lỗi nghiệp vụ chung
                    vm.handleErrorCommon(failData);
                }
            });
        }).then(() => {
            vm.$mask('hide');    	
        });
    }
    
    public handleErrorCustom(failData: any): any {
        const vm = this;

        return new Promise((resolve) => {
            if (failData.messageId == 'Msg_26') {
                vm.$modal.error({ messageId: failData.messageId, messageParams: failData.parameterIds })
                .then(() => {
                    vm.$goto('ccg008a');
                });

                return resolve(false);		
            }

            return resolve(true);
        });
    }

    public handleConfirmMessage(listMes: any): any {
        const vm = this;

        return new Promise((resolve) => {
            if (_.isEmpty(listMes)) {
                return resolve(true);
            }
            let msg = listMes[0];
    
            return vm.$modal.confirm({ messageId: msg.msgID, messageParams: msg.paramLst })
            .then((value) => {
                if (value === 'yes') {
                    return vm.handleConfirmMessage(_.drop(listMes)).then((result) => {
                        if (result) {
                            return resolve(true);    
                        }

                        return resolve(false);
                    });
                }
                
                return resolve(false);
            });
        });
    }


    
}
const API = {
    start: 'at/request/application/appforleave/mobile/start',
    checkBeforeRegisterSample: 'at/request/application/checkBeforeSample',
    registerSample: 'at/request/application/changeDataSample',
    sendMailAfterRegisterSample: ''
};
const MODE_NEW = 0;
const MODE_UPDATE = 1;