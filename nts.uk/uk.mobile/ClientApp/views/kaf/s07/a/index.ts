import { _, Vue } from '@app/provider';
import { component, Prop, Watch } from '@app/core/component';
import { KDL002Component } from '../../../kdl/002';
import { Kdl001Component } from '../../../kdl/001';
import { KafS00DComponent } from '../../../kaf/s00/d';
import {
    KafS00AComponent,
    KafS00BComponent,
    KafS00CComponent
} from 'views/kaf/s00';
import { KafS00ShrComponent, AppType } from 'views/kaf/s00/shr';
// import { AppWorkChange } from '../../../cmm/s45/components/app2/index';
@component({
    name: 'kafs07a',
    route: '/kaf/s07/a',
    style: require('./style.scss'),
    template: require('./index.vue'),
    resource: require('./resources.json'),
    validations: {
        valueWorkHours1: {
            timeRange: false,
            required: true
        },
        valueWorkHours2: {
            timeRange: false,
            required: false
        }
    },
    constraints: [],
    components: {
        'kafs00-a': KafS00AComponent,
        'kafs00-b': KafS00BComponent,
        'kafs00-c': KafS00CComponent,
        'worktype': KDL002Component,
        'kafs00d': KafS00DComponent,
        'worktime': Kdl001Component,
    },

})
export class KafS07AComponent extends KafS00ShrComponent {
    // to edit
    @Prop({ default: null })
    public params?: any;
    public title: string = 'KafS07A';

    public model: Model = new Model();

    public mode: Boolean = true;

    public valueWorkHours1: { start: number, end: number } = null;

    public valueWorkHours2: { start: number, end: number } = null;

    public isValidateAll: Boolean = true;

    // handle visible of view

    public isCondition1: boolean = false;

    public isCondition2: boolean = false;

    public isCondition3: boolean = false;

    public isCondition4: boolean = false;

    // data is fetched service
    public data: any = 'data';

    public kaf000_A_Params: any = null;

    public kaf000_B_Params: any = null;

    public kaf000_C_Params: any = null;

    public user: any;
    public application: any = {
        version: 1,
        // appID: '939a963d-2923-4387-a067-4ca9ee8808zz',
        prePostAtr: 1,
        // employeeID: '',
        appType: 2,
        appDate: this.$dt(new Date(), 'YYYY/MM/DD'),
        enteredPerson: '1',
        inputDate: this.$dt(new Date(), 'YYYY/MM/DD HH:mm:ss'),
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
        },
        // opStampRequestMode: 1,
        // opReversionReason: '1',
        // opAppStartDate: '2020/08/07',
        // opAppEndDate: '2020/08/08',
        // opAppReason: 'jdjadja',
        // opAppStandardReasonCD: 1


    };
    public appWorkChangeDto: any = {};
    public appDispInfoStartupOutput: any = {};

    public created() {
        const self = this;
        if (self.params) {
            console.log(self.params);
            self.mode = false;
            this.data = self.params;
        }
        self.fetchStart();

    }



    get application1() {
        const self = this;

        return {
            prePostAtr: self.kaf000_B_Params.output.prePostAtr
        };
    }

    public mounted() {
        let self = this;

    }

    public fetchStart() {
        const self = this;
        self.$mask('show');
        self.$auth.user.then((usr: any) => {
            self.user = usr;
        }).then(() => {
            return self.loadCommonSetting(AppType.WORK_CHANGE_APPLICATION);
        }).then((loadData: any) => {
            if (loadData) {
                let appWorkChangeDispInfo = {
                    appDispInfoStartupOutput: self.appDispInfoStartupOutput
                } as any;
                let appWorkChangeOutputDto = {
                    appWorkChangeDispInfo
                } as any;
                let param = self.mode ? 
                {
                    mode: self.mode,
                    companyId: self.user.companyId,
                    employeeId: self.user.employeeId,
                    listDates: [],
                    appWorkChangeOutputDto,
                    appWorkChangeDto: self.mode ? null : self.data.appWorkChange
                } : 
                {
                    mode: self.mode,
                    companyId: self.user.companyId,
                    employeeId: self.user.employeeId,
                    listDates: [],
                    appWorkChangeOutputCmd: self.data,
                    appWorkChangeDto: self.mode ? null : self.data.appWorkChange
                };

                return self.$http.post('at', API.startS07, param);
            }
            if (self.appDispInfoStartupOutput) {
                if (self.appDispInfoStartupOutput.appDispInfoWithDateOutput.opErrorFlag != 0) {
                    let param = self.mode ? 
                {
                    mode: self.mode,
                    companyId: self.user.companyId,
                    employeeId: self.user.employeeId,
                    listDates: [],
                    appWorkChangeOutputDto: null,
                    appWorkChangeDto: self.mode ? null : self.data.appWorkChange
                } : 
                {
                    mode: self.mode,
                    companyId: self.user.companyId,
                    employeeId: self.user.employeeId,
                    listDates: [],
                    appWorkChangeOutputCmd: self.data,
                    appWorkChangeDto: self.mode ? null : self.data.appWorkChange
                };

                    return self.$http.post('at', API.startS07, param);
                }
            }

        }).then((res: any) => {
            if (!res) {
                return;
            }
            self.data = res.data;
            self.createParamA();
            self.createParamB();
            self.createParamC();
            // let appWorkChange = res.data.appWorkChange;
            self.bindStart();
            self.$mask('hide');
        }).catch((err: any) => {
            self.handleErrorMessage(err).then((res: any) => {
                if (err.messageId == 'Msg_43') {
                    self.$goto('ccg008a');
                }
            });
        });
    }


    // bind params to components
    public createParamA() {
        const self = this;
        let appDispInfoWithDateOutput = self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoWithDateOutput;
        let appDispInfoNoDateOutput = self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput;
        self.kaf000_A_Params = {
            companyID: self.user.companyId,
            employeeID: self.user.employeeId,
            // 申請表示情報．申請表示情報(基準日関係あり)．社員所属雇用履歴を取得．雇用コード
            employmentCD: appDispInfoWithDateOutput.empHistImport.employmentCode,
            // 申請表示情報．申請表示情報(基準日関係あり)．申請承認機能設定．申請利用設定
            applicationUseSetting: appDispInfoWithDateOutput.approvalFunctionSet.appUseSetLst[0],
            // 申請表示情報．申請表示情報(基準日関係なし)．申請設定．受付制限設定
            receptionRestrictionSetting: appDispInfoNoDateOutput.applicationSetting.receptionRestrictionSetting[0],
            // opOvertimeAppAtr: null
        };
    }
    public createParamB() {
        const self = this;
        self.kaf000_B_Params = null;
        let paramb = {
            input: {
                // mode: 0,

                // appDisplaySetting: {
                //     prePostDisplayAtr: 1,
                //     manualSendMailAtr: 0
                // },
                // newModeContent: {
                //     appTypeSetting: {
                //         appType: 2,
                //         sendMailWhenRegister: false,
                //         sendMailWhenApproval: false,
                //         displayInitialSegment: 1,
                //         canClassificationChange: true
                //     },
                //     useMultiDaySwitch: true,
                //     initSelectMultiDay: true
                // }
                mode: self.mode ? 0 : 1,
                appDisplaySetting: self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput.applicationSetting.appDisplaySetting,
                newModeContent: {
                    // 申請表示情報．申請表示情報(基準日関係なし)．申請設定．申請表示設定																	
                    appTypeSetting: self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput.applicationSetting.appTypeSetting,
                    useMultiDaySwitch: true,
                    initSelectMultiDay: false
                },
                detailModeContent: null


            },
            output: {
                prePostAtr: 0,
                startDate: null,
                endDate: null
            }
        };
        if (!self.mode) {
            paramb.input.detailModeContent = {
                prePostAtr: self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application.prePostAtr,
                startDate: self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application.opAppStartDate,
                endDate: self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application.opAppEndDate,
                employeeName: _.isEmpty(self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput.employeeInfoLst) ? 'empty' : self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput.employeeInfoLst[0].bussinessName
            };
        }
        self.kaf000_B_Params = paramb;
        if (self.mode) {
            self.$watch('kaf000_B_Params.output.startDate', (newV, oldV) => {
                console.log('changedate' + oldV + '--' + newV);
                let startDate = _.clone(self.kaf000_B_Params.output.startDate);
                let endDate = _.clone(self.kaf000_B_Params.output.endDate);
                if (_.isNull(startDate)) {

                    return;
                }
                let listDate = [];
                if (!self.kaf000_B_Params.input.newModeContent.initSelectMultiDay) {
                    listDate.push(self.$dt(newV, 'YYYY/MM/DD'));
                }

                if (!_.isNull(endDate)) {
                    let isCheckDate = startDate.getTime() <= endDate.getTime();
                    if (self.kaf000_B_Params.input.newModeContent.initSelectMultiDay && isCheckDate) {
                        while (startDate.getTime() <= endDate.getTime()) {
                            listDate.push(self.$dt(startDate, 'YYYY/MM/DD'));
                            startDate.setDate(startDate.getDate() + 1);
                        }
                    }

                }
                self.changeDate(listDate);
            });

            self.$watch('kaf000_B_Params.output.endDate', (newV, oldV) => {
                if (!self.kaf000_B_Params.input.newModeContent.initSelectMultiDay) {

                    return;
                }
                let startDate = _.clone(self.kaf000_B_Params.output.startDate);
                let endDate = _.clone(self.kaf000_B_Params.output.endDate);
                if (_.isNull(endDate)) {

                    return;
                }
                let listDate = [];
                if (!_.isNull(startDate)) {
                    let isCheckDate = startDate.getTime() <= endDate.getTime();
                    if (self.kaf000_B_Params.input.newModeContent.initSelectMultiDay && isCheckDate) {
                        while (startDate.getTime() <= endDate.getTime()) {
                            listDate.push(self.$dt(startDate, 'YYYY/MM/DD'));
                            startDate.setDate(startDate.getDate() + 1);
                        }
                    }
                }

                self.changeDate(listDate);
            });
            self.$watch('kaf000_B_Params.input.newModeContent.initSelectMultiDay', (newV, oldV) => {
                console.log(newV + ':' + oldV);
            });

        }


    }
    public createParamC() {
        const self = this;
        // KAFS00_C_起動情報
        let appDispInfoNoDateOutput = self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput;
        self.kaf000_C_Params = {
            input: {
                // 定型理由の表示
                // 申請表示情報．申請表示情報(基準日関係なし)．定型理由の表示区分
                displayFixedReason: appDispInfoNoDateOutput.displayStandardReason,
                // 申請理由の表示
                // 申請表示情報．申請表示情報(基準日関係なし)．申請理由の表示区分
                displayAppReason: appDispInfoNoDateOutput.displayAppReason,
                // 定型理由一覧
                // 申請表示情報．申請表示情報(基準日関係なし)．定型理由項目一覧
                reasonTypeItemLst: appDispInfoNoDateOutput.reasonTypeItemLst,
                // 申請制限設定
                // 申請表示情報．申請表示情報(基準日関係なし)．申請設定．申請制限設定
                appLimitSetting: appDispInfoNoDateOutput.applicationSetting.appLimitSetting,
                // 選択中の定型理由
                // empty
                // opAppStandardReasonCD: this.mode ? 1 : this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application.opAppReason,
                // 入力中の申請理由
                // empty
                // opAppReason: this.mode ? 'Empty' : this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application.opAppStandardReasonCD
            },
            output: {
                // 定型理由
                opAppStandardReasonCD: self.mode ? '' : self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application.opAppStandardReasonCD,
                // 申請理由
                opAppReason: self.mode ? '' : self.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application.opAppReason
            }
        };
    }


    public bindStart() {
        const self = this;
        let appWorkChangeDispInfo = self.data.appWorkChangeDispInfo;
        self.bindVisibleView(appWorkChangeDispInfo);
        self.bindCommon(appWorkChangeDispInfo);
        self.bindValueWorkHours(self.data);
        self.bindWork(self.data);
        self.bindDirectBounce(self.data);
    }

    public bindDirectBounce(params: any) {
        const self = this;
        if (!self.mode) {
            self.model.straight = params.appWorkChange.straightGo == 1 ? 1 : 2;
            self.model.bounce = params.appWorkChange.straightBack == 1 ? 1 : 2;
        }
    }
    public bindWork(params: any) {
        const self = this;
        self.model.workType.code = self.mode ? params.appWorkChangeDispInfo.workTypeCD : (params.appWorkChange ? (params.appWorkChange.opWorkTypeCD ? params.appWorkChange.opWorkTypeCD : null) : null);
        let isExist = _.find(params.appWorkChangeDispInfo.workTypeLst, (item: any) => item.workTypeCode == self.model.workType.code);
        self.model.workType.name = isExist ? isExist.name : self.$i18n('KAFS07_10');

        self.model.workTime.code = self.mode ? params.appWorkChangeDispInfo.workTimeCD : (params.appWorkChange ? (params.appWorkChange.opWorkTimeCD ? params.appWorkChange.opWorkTimeCD : null) : null);
        isExist = _.find(params.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoWithDateOutput.opWorkTimeLst, (item: any) => item.worktimeCode == self.model.workTime.code);
        self.model.workTime.name = isExist ? isExist.workTimeDisplayName.workTimeName : self.$i18n('KAFS07_10');
        self.bindWorkTime(params.appWorkChangeDispInfo);
    }
    public bindWorkTime(params: any) {
        const self = this;
        if (params.predetemineTimeSetting) {
            let startTime = _.find(params.predetemineTimeSetting.prescribedTimezoneSetting.lstTimezone, (item: any) => item.workNo == 1).start;
            let endTime = _.find(params.predetemineTimeSetting.prescribedTimezoneSetting.lstTimezone, (item: any) => item.workNo == 1).end;
            self.model.workTime.time = self.$dt.timedr(startTime) + '~' + self.$dt.timedr(endTime);
        }
    }
    public bindValueWorkHours(params: any) {
        // *4
        // if (!this.isCondition4)
        let time1;
        let time2;
        if (params.appWorkChangeDispInfo.predetemineTimeSetting) {
            time1 = _.find(params.appWorkChangeDispInfo.predetemineTimeSetting.prescribedTimezoneSetting.lstTimezone, (item: any) => item.workNo == 1);
            time2 = _.find(params.appWorkChangeDispInfo.predetemineTimeSetting.prescribedTimezoneSetting.lstTimezone, (item: any) => item.workNo == 2);
        }
        if (!this.mode) {
            let appWorkChange = params.appWorkChange;
            if (appWorkChange) {
                time1 = _.find(appWorkChange.timeZoneWithWorkNoLst, (item: any) => item.workNo == 1);
                time2 = _.find(appWorkChange.timeZoneWithWorkNoLst, (item: any) => item.workNo == 2);
            }
            this.bindWorkHours(time1, time2);

            return;

        }
        if (!this.isCondition4) {
            this.bindWorkHours(time1, time2);
        }


    }
    public bindWorkHours(time1: any, time2: any) {
        if (this.isCondition1) {
            if (!this.valueWorkHours1 && time1) {
                this.valueWorkHours1 = {
                    start: 0,
                    end: 0
                };
            }
            if (time1) {

                if (!this.mode) {
                    this.valueWorkHours1.start = time1.timeZone.startTime;
                    this.valueWorkHours1.end = time1.timeZone.endTime;
                } else {
                    this.valueWorkHours1.start = time1.start;
                    this.valueWorkHours1.end = time1.end;
                }
            }

        } else {
            this.$updateValidator('valueWorkHours1', {
                timeRange: false,
                required: false
            });
        }
        if (this.isCondition2) {
            if (!this.valueWorkHours2 && time2) {
                this.valueWorkHours2 = {
                    start: 0,
                    end: 0
                };
            }
            if (time2) {
                if (!this.mode) {
                    this.valueWorkHours2.start = time2.timeZone.startTime;
                    this.valueWorkHours2.end = time2.timeZone.endTime;
                } else {
                    this.valueWorkHours2.start = time2.start;
                    this.valueWorkHours2.end = time2.end;
                }
            }

        } else {
            this.$updateValidator('valueWorkHours2', {
                timeRange: false,
                required: false
            });
        }
        if (!this.isCondition3) {
            this.$updateValidator('valueWorkHours2', {
                timeRange: false,
                required: false
            });
            this.$updateValidator('valueWorkHours1', {
                timeRange: false,
                required: false
            });
        }
    }

    public bindCommon(params: any) {
        // bind appDispInfoStartupOutput to common component
        this.appDispInfoStartupOutput.appDispInfoNoDateOutput = params.appDispInfoStartupOutput.appDispInfoNoDateOutput;
        this.appDispInfoStartupOutput.appDispInfoWithDateOutput = params.appDispInfoStartupOutput.appDispInfoWithDateOutput;
        this.appDispInfoStartupOutput.appDetailScreenInfo = params.appDispInfoStartupOutput.appDetailScreenInfo;
    }
    public bindAppWorkChangeRegister() {
        this.appWorkChangeDto.straightGo = this.model.straight == 2 ? 0 : 1;
        this.appWorkChangeDto.straightBack = this.model.bounce == 2 ? 0 : 1;
        this.appWorkChangeDto.opWorkTypeCD = this.model.workType.code;
        this.appWorkChangeDto.opWorkTimeCD = this.model.workTime.code;
        if (this.isCondition3) {
            this.appWorkChangeDto.timeZoneWithWorkNoLst = [];
            let a = null;
            let b = null;
            if (this.isCondition1) {
                a = {
                    workNo: 1,
                    timeZone: {
                        startTime: this.valueWorkHours1.start,
                        endTime: this.valueWorkHours1.end
                    }
                };
                this.appWorkChangeDto.timeZoneWithWorkNoLst.push(a);
            }
            if (this.valueWorkHours2) {

                if (this.isCondition2 && !(_.isNull(this.valueWorkHours2.start) && _.isNull(this.valueWorkHours2.end))) {
                    b = {
                        workNo: 2,
                        timeZone: {
                            startTime: this.valueWorkHours2 ? this.valueWorkHours2.start : null,
                            endTime: this.valueWorkHours2 ? this.valueWorkHours2.end : null
                        }
                    };
                    this.appWorkChangeDto.timeZoneWithWorkNoLst.push(b);
                }
            }
        } else {
            this.appWorkChangeDto.timeZoneWithWorkNoLst = null;
        }
        if (!this.mode && !this.isCondition3) {

            this.appWorkChangeDto.timeZoneWithWorkNoLst = [];
            let a = null;
            let b = null;
            if (this.isCondition1 && this.valueWorkHours1.start && this.valueWorkHours1.end) {
                a = {
                    workNo: 1,
                    timeZone: {
                        startTime: this.valueWorkHours1.start,
                        endTime: this.valueWorkHours1.end
                    }
                };
                this.appWorkChangeDto.timeZoneWithWorkNoLst.push(a);
            }
            if (this.isCondition2 && this.valueWorkHours2.start && this.valueWorkHours2.end) {
                b = {
                    workNo: 2,
                    timeZone: {
                        startTime: this.valueWorkHours2.start,
                        endTime: this.valueWorkHours2.end
                    }
                };
                this.appWorkChangeDto.timeZoneWithWorkNoLst.push(b);
            }
        }
        if (!this.mode) {
            this.application = this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDetailScreenInfo.application;
        }
        if (this.mode) {
            this.application.employeeID = this.user.employeeId;
        }

        if (this.kaf000_B_Params) {
            if (this.mode) {
                this.application.appDate = this.$dt.date(this.kaf000_B_Params.output.startDate, 'YYYY/MM/DD');
                this.application.opAppStartDate = this.$dt.date(this.kaf000_B_Params.output.startDate, 'YYYY/MM/DD');
                if (this.kaf000_B_Params.input.newModeContent.initSelectMultiDay) {
                    this.application.opAppEndDate = this.$dt.date(this.kaf000_B_Params.output.endDate, 'YYYY/MM/DD');
                } else {
                    this.application.opAppEndDate = this.$dt.date(this.kaf000_B_Params.output.startDate, 'YYYY/MM/DD');
                }
            }

            this.application.prePostAtr = this.kaf000_B_Params.output.prePostAtr;

        }

        if (this.kaf000_C_Params.output) {
            this.application.opAppStandardReasonCD = this.kaf000_C_Params.output.opAppStandardReasonCD;
            this.application.opAppReason = this.kaf000_C_Params.output.opAppReason;
        }
        this.application.enteredPerson = this.user.employeeId;


    }

    public changeDate(dates: any) {
        const self = this;
        self.$mask('show');
        self.data.appWorkChangeDispInfo.workTypeCD = self.model.workType.code;
        self.data.appWorkChangeDispInfo.workTimeCD = self.model.workTime.code;
        let params = {
            companyId: self.user.companyId,
            listDates: dates,
            appWorkChangeDispInfo: self.data.appWorkChangeDispInfo
        };
        self.$http.post('at', API.updateAppWorkChange, params)
            .then((res: any) => {
                self.$mask('hide');
                self.data.appWorkChangeDispInfo = res.data;
                self.bindStart();
                let useDivision = self.appDispInfoStartupOutput.appDispInfoWithDateOutput.approvalFunctionSet.appUseSetLst[0].useDivision,
                recordDate = self.appDispInfoStartupOutput.appDispInfoNoDateOutput.applicationSetting.recordDate,
                opErrorFlag = self.appDispInfoStartupOutput.appDispInfoWithDateOutput.opErrorFlag,
                msgID = '';
                if (useDivision == 0) {
                    self.$modal.error('Msg_323').then(() => {
                        if (recordDate == 0) {
                            self.$goto('ccg008a');   
                        }
                    });
                    if (recordDate == 0) {
                        return false;
                    }

                    return true;
                }
            
                if (_.isNull(opErrorFlag)) {
                    return true;    
                }
                switch (opErrorFlag) {
                    case 1:
                        msgID = 'Msg_324';
                        break;
                    case 2: 
                        msgID = 'Msg_238';
                        break;
                    case 3:
                        msgID = 'Msg_237';
                        break;
                    default: 
                        break;
                }
                if (_.isEmpty(msgID)) { 
                    return true;
                }
                self.$modal.error({ messageId: msgID }).then(() => {
                    if (recordDate == 0) {
                        self.$goto('ccg008a');    
                    }    
                });

                return false;
                
            })
            .catch((res: any) => {
                self.handleErrorMessage(res).then((msgId: any) => {
                    if (res.messageId == 'Msg_426') {
                        self.$goto('ccg008a');
                    }
                });
            }); 

    }
    public registerData(res: any) {
        const self = this;
        this.$mask('show');
        this.$http.post('at', API.registerAppWorkChange, {
            mode: this.mode,
            companyId: this.user.companyId,
            applicationDto: this.application,
            appWorkChangeDto: this.appWorkChangeDto,
            holidayDates: res.data.holidayDateLst,
            isMail: this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoNoDateOutput.mailServerSet,
            appDispInfoStartupDto: this.data.appWorkChangeDispInfo.appDispInfoStartupOutput
        }).then((res: any) => {
            this.$mask('hide');
            // KAFS00_D_申請登録後画面に移動する
            this.$modal('kafs00d', { mode: this.mode ? ScreenMode.NEW : ScreenMode.DETAIL, appID: res.data.appID });
        }).catch((res: any) => {
            this.$mask('hide');
            self.handleErrorMessage(res);
        });
    }
    public handleConfirmMessage(listMes: any, res: any) {

        if (!_.isEmpty(listMes)) {
            let item = listMes.shift();
            this.$modal.confirm({ messageId: item.messageId }).then((value) => {
                this.$mask('hide');
                if (value == 'yes') {
                    if (_.isEmpty(listMes)) {
                        this.registerData(res);
                    } else {
                        this.handleConfirmMessage(listMes, res);
                    }

                }
            });
        }
    }
    public register() {
        const vm = this;
        let validAll: boolean = true;
        for (let child of vm.$children) {
            child.$validate();
            if (!child.$valid) {
                validAll = false;
            }
        }
        vm.isValidateAll = validAll;
        console.log(validAll);
        console.log(vm.application);

        // check validation 
        this.$validate();
        if (!this.$valid || !validAll) {
            window.scrollTo(500, 0);

            return;
        }
        if (this.$valid && validAll) {
            this.$mask('show');
        }
        this.bindAppWorkChangeRegister();
        console.log(this.appWorkChangeDto);


        // check before registering application
        this.$http.post('at', API.checkBeforRegister, {
            mode: this.mode,
            companyId: this.user.companyId,
            applicationDto: this.application,
            appWorkChangeDto: this.appWorkChangeDto,
            // 申請表示情報．申請表示情報(基準日関係あり)．承認ルートエラー情報
            isError: this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoWithDateOutput.opErrorFlag,
            appDispInfoStartupDto: this.appDispInfoStartupOutput
        }).then((res: any) => {
            this.$mask('hide');
            // confirmMsgLst
            // holidayDateLst
            let isConfirm = true;
            if (!_.isEmpty(res)) {
                // display list confirm message
                if (!_.isEmpty(res.data.confirmMsgLst)) {
                    let listTemp = _.clone(res.data.confirmMsgLst);
                    this.handleConfirmMessage(listTemp, res);

                } else {
                    this.registerData(res);
                }


            }



        }).catch((res: any) => {
            this.handleErrorMessage(res);

        });

    }

    // visible/ invisible
    // A2_1


    // 「勤務変更申請の表示情報．勤務変更申請の反映.出退勤を反映するか」がする
    public isDisplay1(params: any) {
        return params.reflectWorkChangeAppDto.whetherReflectAttendance == 1;

        // return true;
    }
    // ※1 = ○　AND　「勤務変更申請の表示情報．申請表示情報．申請表示情報(基準日関係なし)．複数回勤務の管理」= true
    public isDisplay2(params: any) {
        return params.reflectWorkChangeAppDto.whetherReflectAttendance == 1 && params.appDispInfoStartupOutput.appDispInfoNoDateOutput.managementMultipleWorkCycles;
        // return false;

    }
    // A4_3  「勤務変更申請の表示情報．就業時間帯の必須区分」が「必須」または「任意」
    public isDisplay3(params: any) {
        return params.setupType == 1 || params.setupType == 0;
    }

    // 「勤務変更申請の表示情報．勤務変更申請設定．勤務時間の初期表示」が「空白」 => clear data ,true
    // 「勤務変更申請の表示情報．勤務変更申請設定．勤務時間の初期表示」が「定時」=> transfer from data ,false
    public isDisplay4(params: any) {
        // return true;
        return params.appWorkChangeSet.initDisplayWorktimeAtr == 1;

    }
    // // Display error message
    // // UI処理【1】
    // public isDisplay5() {
    //     return true;

    // }
    // handle message dialog

    public handleErrorMessage(res: any) {
        const self = this;
        self.$mask('hide');
        if (res.messageId) {
            return self.$modal.error({ messageId: res.messageId, messageParams: res.parameterIds });
        } else {
            
            if (_.isArray(res.errors)) {
                return self.$modal.error({ messageId: res.errors[0].messageId, messageParams: res.parameterIds});
            } else {
                return self.$modal.error({ messageId: res.errors.messageId, messageParams: res.parameterIds });
            }
        }
    }

    // bind visible of view 
    public bindVisibleView(params: any) {
        let appWorkChangeDispInfo = params;
        this.isCondition1 = this.isDisplay1(appWorkChangeDispInfo);
        this.isCondition2 = this.isDisplay2(appWorkChangeDispInfo);
        this.isCondition3 = this.isDisplay3(appWorkChangeDispInfo);
        this.isCondition4 = this.isDisplay4(appWorkChangeDispInfo);

    }
    public openKDL002(name: string) {
        const self = this;
        console.log(_.map(this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoWithDateOutput.opWorkTimeLst, (item: any) => item.worktimeCode));
        if (name == 'worktype') {
            this.$modal(
                'worktype',
                {
                    seledtedWkTypeCDs: _.map(_.uniqBy(this.data.appWorkChangeDispInfo.workTypeLst, (e: any) => e.workTypeCode), (item: any) => item.workTypeCode),
                    selectedWorkTypeCD: this.model.workType.code,
                    seledtedWkTimeCDs: _.map(this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoWithDateOutput.opWorkTimeLst, (item: any) => item.worktimeCode),
                    selectedWorkTimeCD: this.model.workTime.code,
                    isSelectWorkTime: 1,
                }
            ).then((f: any) => {
                if (f) {
                    // check worktime
                    let appWorkChangeSet = self.data.appWorkChangeDispInfo.appWorkChangeSet;
                    let param = {
                        companyId: self.user.companyId,
                        workType: f.selectedWorkType.workTypeCode,
                        workTime: f.selectedWorkTime ? (f.selectedWorkTime.code ? f.selectedWorkTime.code : null) : null,
                        appWorkChangeSetDto: appWorkChangeSet
                    };
                    self.$mask('show');
                    self.$http.post('at', API.checkWorkTime, param)
                        .then((res: any) => {
                            self.$mask('hide');
                            self.data.appWorkChangeDispInfo.setupType = res.data.setupType;
                            self.data.appWorkChangeDispInfo.predetemineTimeSetting = res.data.opPredetemineTimeSetting;
                            self.bindVisibleView(self.data.appWorkChangeDispInfo);
                            this.model.workType.code = f.selectedWorkType.workTypeCode;
                            this.model.workType.name = f.selectedWorkType.name;
                            if (f.selectedWorkTime) {
                                this.model.workTime.code = f.selectedWorkTime.code;
                                this.model.workTime.name = f.selectedWorkTime.name;
                                this.model.workTime.time = f.selectedWorkTime.workTime1;
                            }
                        })
                        .catch((res: any) => {
                            self.handleErrorMessage(res);
                        });
                    
                }
            }).catch((res: any) => {
                self.handleErrorMessage(res);
            });
        } else {
            this.$modal(
                'worktime',
                {
                    isAddNone: 1,
                    seledtedWkTimeCDs: _.map(this.data.appWorkChangeDispInfo.appDispInfoStartupOutput.appDispInfoWithDateOutput.opWorkTimeLst, (item: any) => item.worktimeCode),
                    selectedWorkTimeCD: this.model.workTime.code,
                    isSelectWorkTime: 1
                }
            ).then((f: any) => {
                if (!f) {

                    return;
                    // this.model.workTime.code = f.selectedWorkTime.code;
                    // this.model.workTime.name = f.selectedWorkTime.name;
                    // this.model.workTime.time = f.selectedWorkTime.workTime1;
                }
                let appWorkChangeSet = self.data.appWorkChangeDispInfo.appWorkChangeSet;
                let param = {
                    companyId: self.user.companyId,
                    workType: this.model.workType.code,
                    workTime: f.selectedWorkTime ? (f.selectedWorkTime.code ? f.selectedWorkTime.code : null) : null,
                    appWorkChangeSetDto: appWorkChangeSet
                };
                self.$http.post('at', API.checkWorkTime, param)
                        .then((res: any) => {
                            self.$mask('hide');
                            self.data.appWorkChangeDispInfo.setupType = res.data.setupType;
                            self.data.appWorkChangeDispInfo.predetemineTimeSetting = res.data.opPredetemineTimeSetting;
                            self.bindVisibleView(self.data.appWorkChangeDispInfo);
                            // this.model.workType.code = f.selectedWorkType.workTypeCode;
                            // this.model.workType.name = f.selectedWorkType.name;
                            if (!(f.selectedWorkTime.code == '' && res.data.setupType == 0)) {
                                this.model.workTime.code = f.selectedWorkTime.code;
                                this.model.workTime.name = f.selectedWorkTime.name;
                                this.model.workTime.time = f.selectedWorkTime.workTime1;
                            }
                        })
                        .catch((res: any) => {
                            self.handleErrorMessage(res);
                        });

            }).catch((res: any) => {
                self.handleErrorMessage(res);
            });
        }




    }

}
export class Work {
    public code: String = '';
    public name: String = '';
    constructor() {

    }

}
export class WorkTime extends Work {
    public time: String = '項目移送';
    constructor() {
        super();
    }
}

export class Model {

    public workType: Work = new Work();

    public workTime: WorkTime = new WorkTime();

    public straight: number = 2;

    public bounce: number = 2;

    constructor() {

    }
}

// 画面モード
export enum ScreenMode {
    // 新規モード
    NEW = 0,
    // 詳細モード
    DETAIL = 1
}

const API = {
    startS07: 'at/request/application/workchange/mobile/startMobile',
    checkBeforRegister: 'at/request/application/workchange/mobile/checkBeforeRegister_New',
    registerAppWorkChange: 'at/request/application/workchange/mobile/addWorkChange_New',
    updateAppWorkChange: 'at/request/application/workchange/mobile/changeDateKAFS07',
    checkWorkTime: 'at/request/application/workchange/mobile/checkWorkTime'
};
