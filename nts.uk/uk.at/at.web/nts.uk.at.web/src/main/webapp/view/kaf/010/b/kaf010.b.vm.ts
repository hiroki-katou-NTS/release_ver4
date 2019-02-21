module nts.uk.at.view.kaf010.b {
    import common = nts.uk.at.view.kaf010.share.common;
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import service = nts.uk.at.view.kaf010.shr.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import util = nts.uk.util;

    export module viewmodel {
        export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
            DATE_FORMAT: string = "YYYY/MM/DD";
            datatest : any;
            screenModeNew: KnockoutObservable<boolean> = ko.observable(false);
            //current Data
            //        curentGoBackDirect: KnockoutObservable<common.GoBackDirectData>;
            //manualSendMailAtr
            manualSendMailAtr: KnockoutObservable<boolean> = ko.observable(false);
            displayBreakTimeFlg: KnockoutObservable<boolean> = ko.observable(false);
            //申請者
            employeeName: KnockoutObservable<string> = ko.observable("");
            employeeList: KnockoutObservableArray<common.EmployeeOT> = ko.observableArray([]);
            selectedEmplCodes: KnockoutObservable<string> = ko.observable(null);
            employeeFlag: KnockoutObservable<boolean> = ko.observable(false);
            totalEmployee: KnockoutObservable<string> = ko.observable(null);
            //Pre-POST
            prePostSelected: KnockoutObservable<number> = ko.observable(0);
            backSelected1: KnockoutObservable<number> = ko.observable(0);
            goSelected1 : KnockoutObservable<number> = ko.observable(0);
            backSelected2 : KnockoutObservable<number> = ko.observable(0);
            goSelected2 : KnockoutObservable<number> = ko.observable(0);
            goSelected1Value: KnockoutObservable<string> = ko.observable("");
            backSelected1Value: KnockoutObservable<string> = ko.observable("");
            workState: KnockoutObservable<boolean> = ko.observable(true);;
            typeSiftVisible: KnockoutObservable<boolean> = ko.observable(true);
            // 申請日付
            appDate: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));
            enbAppDate: KnockoutObservable<boolean> = ko.observable(true);
            //TIME LINE 1
            timeStart1: KnockoutObservable<number> = ko.observable(null);
            timeEnd1: KnockoutObservable<number> = ko.observable(null);
            //TIME LINE 2
            timeStart2: KnockoutObservable<number> = ko.observable(null);
            timeEnd2: KnockoutObservable<number> = ko.observable(null);
            //勤務種類
            workTypeCd: KnockoutObservable<string> = ko.observable('');
            workTypeName: KnockoutObservable<string> = ko.observable('');
            //勤務種類
            siftCD: KnockoutObservable<string> = ko.observable('');
            siftName: KnockoutObservable<string> = ko.observable('');
            workTypecodes: KnockoutObservableArray<string> = ko.observableArray([]);
            workTimecodes: KnockoutObservableArray<string> = ko.observableArray([]);
            //comboBox 定型理由
            reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
            selectedReason: KnockoutObservable<string> = ko.observable('');
            //MultilineEditor
            requiredReason: KnockoutObservable<boolean> = ko.observable(false);
            multilContent: KnockoutObservable<string> = ko.observable('');
            //comboBox 定型理由
            reasonCombo2: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
            selectedReason2: KnockoutObservable<string> = ko.observable('');
            //MultilineEditor
            requiredReason2: KnockoutObservable<boolean> = ko.observable(false);
            multilContent2: KnockoutObservable<string> = ko.observable('');
            //Approval 
            approvalSource: Array<common.AppApprovalPhase> = [];
            employeeID: KnockoutObservable<string> = ko.observable('');
            //休出時間
            restTime: KnockoutObservableArray<common.OverTimeInput> = ko.observableArray([]);
            //残業時間
            overtimeHours: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            //休憩時間
            breakTimes: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            //加給時間
            bonusTimes: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            //menu-bar 
            enableSendMail: KnockoutObservable<boolean> = ko.observable(true);
            prePostDisp: KnockoutObservable<boolean> = ko.observable(true);
            prePostEnable: KnockoutObservable<boolean> = ko.observable(true);
            useMulti: KnockoutObservable<boolean> = ko.observable(true);
    
            displayBonusTime: KnockoutObservable<boolean> = ko.observable(false);
            displayCaculationTime: KnockoutObservable<boolean> = ko.observable(true);
            displayPrePostFlg: KnockoutObservable<boolean> = ko.observable(true);
            displayRestTime: KnockoutObservable<boolean> = ko.observable(false);
            restTimeDisFlg: KnockoutObservable<boolean> = ko.observable(false); // RequestAppDetailSetting 
            typicalReasonDisplayFlg: KnockoutObservable<boolean> = ko.observable(false);
            displayAppReasonContentFlg: KnockoutObservable<boolean> = ko.observable(false);
            displayDivergenceReasonForm: KnockoutObservable<boolean> = ko.observable(false);
            displayDivergenceReasonInput: KnockoutObservable<boolean> = ko.observable(false);
    
            // 参照
            referencePanelFlg: KnockoutObservable<boolean> = ko.observable(false);
            preAppPanelFlg: KnockoutObservable<boolean> = ko.observable(false);
            isRightContent: KnockoutObservable<boolean> = ko.observable(false);
            
            instructInforFlag: KnockoutObservable <boolean> = ko.observable(true);
            instructInfor : KnockoutObservable <string> = ko.observable('');
    
            overtimeWork: KnockoutObservableArray<common.overtimeWork> = ko.observableArray([]);
            indicationOvertimeFlg: KnockoutObservable<boolean> = ko.observable(true);
            
            // preAppOvertime
            appDatePre: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));
            workTypeCodePre:  KnockoutObservable<string> = ko.observable("");
            workTypeNamePre:  KnockoutObservable<string> = ko.observable("");
            siftCodePre:  KnockoutObservable<string> = ko.observable("");
            siftNamePre:  KnockoutObservable<string> = ko.observable("");
            //TIME LINE 1
            workClockFrom1To1Pre: KnockoutObservable<string> = ko.observable(null);
            //TIME LINE 2
            workClockFrom2To2Pre: KnockoutObservable<string> = ko.observable(null);
            displayWorkClockFrom2To2Pre: KnockoutObservable <boolean> = ko.observable(true);
            overtimeHoursPre: KnockoutObservableArray<common.OverTimeInput> = ko.observableArray([]);
            overTimeShiftNightPre: KnockoutObservable<number> = ko.observable(null);
            flexExessTimePre: KnockoutObservable<number> = ko.observable(null);
            
            // AppOvertimeReference
            appDateReference: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));
            workTypeCodeReference:  KnockoutObservable<string> = ko.observable("");
            workTypeNameReference:  KnockoutObservable<string> = ko.observable("");
            siftCodeReference:  KnockoutObservable<string> = ko.observable("");
            siftNameReference:  KnockoutObservable<string> = ko.observable("");
            //TIME LINE 1
            workClockFrom1To1Reference: KnockoutObservable<string> = ko.observable(null);
            //TIME LINE 2
            workClockFrom2To2Reference: KnockoutObservable<string> = ko.observable(null);
            displayWorkClockFrom2To2Reference: KnockoutObservable <boolean> = ko.observable(true);
            overtimeHoursReference: KnockoutObservableArray<common.AppOvertimePre> = ko.observableArray([]);
            overTimeShiftNightRefer: KnockoutObservable<string> = ko.observable(null);
            flexExessTimeRefer: KnockoutObservable<string> = ko.observable(null);
            //　初期起動時、計算フラグ=1とする。
            calculateFlag: KnockoutObservable<number> = ko.observable(1);
            version: number = 0;
            preWorkContent: common.WorkContent;
            inputDate: KnockoutObservable<string> = ko.observable('');
            allPreAppPanelFlg: KnockoutObservable<boolean> = ko.observable(false);
            //画面モード(表示/編集)
            editable: KnockoutObservable<boolean> = ko.observable(true);
            enableOvertimeInput: KnockoutObservable<boolean> = ko.observable(false);
            constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata) {
                super(listAppMetadata, currentApp);
                var self = this;
                    $("#fixed-table-holiday").ntsFixedTable({ height: 120 });
                    $("#fixed-break_time-table-holiday").ntsFixedTable({ height: 119 });
                    $("#fixed-break_time-table-holiday-pre").ntsFixedTable({ height: 119 });
                    $("#fixed-overtime-hour-table-holiday").ntsFixedTable({ height: 216 });
                    $("#fixed-bonus_time-table-holiday").ntsFixedTable({ height: 120 });
                    $("#fixed-table-indicate-holiday").ntsFixedTable({ height: 120 });
                    $('.nts-fixed-table.cf').first().find('.nts-fixed-body-container.ui-iggrid').css('border-left','1px solid #CCC');
                self.startPage(self.appID()).done(function(){
                   
                });
            }
            
            startPage(appID: string): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                service.findByAppID(appID).done((data) => { 
                    self.initData(data);
                    self.checkRequiredBreakTimes();
                    //Check work content Changed
                    self.checkWorkContentChanged();
                    dfd.resolve(); 
                })
                .fail(function(res) {
                    if(res.messageId == 'Msg_426'){
                       dialog.alertError({messageId : res.messageId}).then(function(){
                            nts.uk.ui.block.clear();
                           appcommon.CommonProcess.callCMM045();
                    });
                    }else if(res.messageId == 'Msg_423'){
                        dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                            .then(function() {
                                nts.uk.ui.block.clear();
                                appcommon.CommonProcess.callCMM045();
                            });
                    }else{ 
                        nts.uk.ui.dialog.alertError(res.message).then(function(){
                            appcommon.CommonProcess.callCMM045();
                            nts.uk.ui.block.clear();
                        });
                    }
                    dfd.reject(res);  
                });
                return dfd.promise();
            }
            
            isShowReason(){
            let self =this;
            if(self.screenModeNew()){
                    return self.displayAppReasonContentFlg();
                }else{
                    return self.typicalReasonDisplayFlg() || self.displayAppReasonContentFlg();
            }
        }
            
            initData(data: any) {
                var self = this;
                self.version = data.application.version;
                self.enableOvertimeInput(data.enableOvertimeInput);
                self.manualSendMailAtr(data.manualSendMailAtr);
                self.displayPrePostFlg(data.displayPrePostFlg ? true : false);
                self.prePostSelected(data.application.prePostAtr);
                self.displayCaculationTime(data.displayCaculationTime);
                self.typicalReasonDisplayFlg(data.typicalReasonDisplayFlg);
                self.displayAppReasonContentFlg(data.displayAppReasonContentFlg);
                self.displayDivergenceReasonForm(data.displayDivergenceReasonForm);
                self.displayDivergenceReasonInput(data.displayDivergenceReasonInput);
                self.displayBonusTime(data.displayBonusTime);
                self.restTimeDisFlg(data.displayRestTime);
                self.appDate(data.application.applicationDate);
                self.employeeName(data.employeeName);
                self.employeeID(data.application.applicantSID);
                self.inputDate(data.application.inputDate);
                if (data.workTime != null) {
                    self.siftCD(data.workTime.siftCode);
                    self.siftName(data.workTime.siftName);
                }
                if (data.workType != null) {
                    self.workTypeCd(data.workType.workTypeCode);
                    self.workTypeName(data.workType.workTypeName);
                }
                
                self.workTypecodes(data.workTypes);
                self.workTimecodes(data.workTimes);
                
                self.timeStart1(data.workClockStart1);
                self.timeEnd1(data.workClockEnd1);
                self.timeStart2(data.workClockStart2);
                self.timeEnd2(data.workClockEnd2);
                self.goSelected1(data.goAtr1);
                self.goSelected2(data.goAtr2);
                self.backSelected1(data.backAtr1);
                self.backSelected2(data.backAtr2);
                self.goSelected1Value(data.goAtr1 == 0 ? nts.uk.resource.getText("KAF009_17") : nts.uk.resource.getText("KAF009_16"));
                self.backSelected1Value(data.backAtr1 == 0 ? nts.uk.resource.getText("KAF009_19") : nts.uk.resource.getText("KAF009_18"));
                if(data.applicationReasonDtos != null && data.applicationReasonDtos.length > 0){
                    let reasonID = data.applicationReasonDtos[0].reasonID;
                    self.selectedReason(reasonID);
                    let lstReasonCombo = _.map(data.applicationReasonDtos, o => { return new common.ComboReason(o.reasonID, o.reasonTemp); });
                    self.reasonCombo(lstReasonCombo);
                }
                self.multilContent(data.application.applicationReason);
                if(data.divergenceReasonDtos != null && data.divergenceReasonDtos.length > 0){
                    self.reasonCombo2(_.map(data.divergenceReasonDtos, o => { return new common.ComboReason(o.divergenceReasonID, o.reasonTemp); }));
                    let reasonID = data.divergenceReasonDtos[0].divergenceReasonID;
                    self.selectedReason2(reasonID);
                }
                self.multilContent2(data.divergenceReasonContent);
                self.instructInforFlag(data.displayHolidayInstructInforFlg);
                self.instructInfor(data.holidayInstructInformation);
                self.referencePanelFlg(data.referencePanelFlg);
                self.preAppPanelFlg(data.preAppPanelFlg);
                self.allPreAppPanelFlg(data.allPreAppPanelFlg);
                self.indicationOvertimeFlg(data.extratimeDisplayFlag);
                if(nts.uk.util.isNullOrUndefined(data.appOvertimeDetailDto)){
                    self.indicationOvertimeFlg(false);    
                } else {
                    common.Process.setOvertimeWorkDetail(data.appOvertimeDetailDto, self, data.appOvertimeDetailStatus);    
                }
                self.isRightContent(data.allPreAppPanelFlg || data.referencePanelFlg);
//                // preAppOvertime
                if (data.preAppHolidayWorkDto != null) {
                self.appDatePre(data.preAppHolidayWorkDto.appDate);
                if (data.preAppHolidayWorkDto.workType != null) {
                    self.workTypeCodePre(data.preAppHolidayWorkDto.workType.workTypeCode);
                    self.workTypeNamePre(data.preAppHolidayWorkDto.workType.workTypeName);
                }
                if (data.preAppHolidayWorkDto.siftTypePre != null) {
                    self.siftCodePre(data.preAppHolidayWorkDto.workTime.siftCode);
                    self.siftNamePre(data.preAppHolidayWorkDto.workTime.siftName);
                }
                if (!nts.uk.util.isNullOrEmpty(data.preAppHolidayWorkDto.workClockStart1) || !nts.uk.util.isNullOrEmpty(data.preAppHolidayWorkDto.workClockEnd1)) {
                    self.workClockFrom1To1Pre(self.convertIntToTime(data.preAppHolidayWorkDto.workClockStart1) + " " + nts.uk.resource.getText("KAF005_126") + " " + self.convertIntToTime(data.preAppHolidayWorkDto.workClockEnd1));
                }
                if (!nts.uk.util.isNullOrEmpty(data.preAppHolidayWorkDto.workClockStart2) || !nts.uk.util.isNullOrEmpty(data.preAppHolidayWorkDto.workClockEnd2)) {
                    self.workClockFrom2To2Pre(self.convertIntToTime(data.preAppHolidayWorkDto.workClockStart2) + " " + nts.uk.resource.getText("KAF005_126") + " " + self.convertIntToTime(data.preAppHolidayWorkDto.workClockEnd2));
                }
                if (self.workClockFrom2To2Pre() == null) {
                    self.displayWorkClockFrom2To2Pre(false);
                }
                self.overtimeHoursPre.removeAll();
                if (data.preAppHolidayWorkDto.holidayWorkInputs != null) {
                    for (let i = 0; i < data.preAppHolidayWorkDto.holidayWorkInputs.length; i++) {
                        if (data.preAppHolidayWorkDto.holidayWorkInputs[i].applicationTime != -1) {
                            self.overtimeHoursPre.push(new common.AppOvertimePre("", "",
                                data.preAppHolidayWorkDto.holidayWorkInputs[i].attendanceID,
                                "", data.preAppHolidayWorkDto.holidayWorkInputs[i].frameNo,
                                0, data.preAppHolidayWorkDto.holidayWorkInputs[i].frameName + " : ",
                                data.preAppHolidayWorkDto.holidayWorkInputs[i].startTime,
                                data.preAppHolidayWorkDto.holidayWorkInputs[i].endTime,
                                self.convertIntToTime(data.preAppHolidayWorkDto.holidayWorkInputs[i].applicationTime), null));

                        } else {
                            continue;
                        }

                    }
                }
            }
                
                let dataRestTime = _.filter(data.holidayWorkInputDtos, {'attendanceType': 0});
                let dataOverTime = _.filter(data.holidayWorkInputDtos, {'attendanceType': 1});
                let dataBreakTime = _.filter(data.holidayWorkInputDtos, {'attendanceType': 2});
                let dataBonusTime = _.filter(data.holidayWorkInputDtos, {'attendanceType': 3});
                self.datatest = dataOverTime;
                self.restTime.removeAll();
                self.overtimeHours.removeAll();
                self.breakTimes.removeAll();
                self.bonusTimes.removeAll();
                if(nts.uk.util.isNullOrEmpty(dataRestTime)){
                    for (let i = 0; i < 11; i++) {
                        self.restTime.push(new common.OverTimeInput("", "", 0, "", i,0, i.toString(), null, null, null,"", ""));
                    }    
                } else {
                    _.forEach(dataRestTime, (item) => { 
                        
                            self.restTime.push(new common.OverTimeInput(
                                item.companyID, 
                                item.appID, 
                                item.attendanceType, 
                                "", 
                                item.frameNo, 
                                "", 
                                item.frameName, 
                                item.startTime, 
                                item.endTime, 
                                item.applicationTime, ""));
                    });
                };
                _.forEach(dataOverTime, (item : any) => { 
                    let color: string = "";
                    if (item.errorCode == 1) {
                        color = '#FD4D4D';
                    }
                    if (item.errorCode == 2) {
                        color = '#F6F636';
                    }
                    if (item.errorCode == 3) {
                        color = '#F69164';
                    }
                    if(item.frameNo == 11){
                            if (data.appOvertimeNightFlg == 1) {
                                if(item.errorCode)
                                self.overtimeHours.push(new common.OvertimeCaculation(
                                    item.companyID, 
                                    item.appID, 
                                    item.attendanceType, 
                                    "", 
                                    item.frameNo, 
                                    "", 
                                    nts.uk.resource.getText("KAF005_63"), 
                                    item.applicationTime, 
                                    null, 
                                    null,"#[KAF005_64]","",color));
                             }
                    }else if(item.frameNo == 12){
                                self.overtimeHours.push(new common.OvertimeCaculation(
                                    item.companyID, 
                                    item.appID, 
                                    item.attendanceType, 
                                    "", 
                                    item.frameNo, 
                                    "", 
                                    nts.uk.resource.getText("KAF005_65"), 
                                    item.applicationTime, 
                                    null, 
                                    null,"#[KAF005_66]","",color));
                     }else{
                                self.overtimeHours.push(new common.OvertimeCaculation(
                                    item.companyID, 
                                    item.appID, 
                                    item.attendanceType, 
                                    "", 
                                    item.frameNo, 
                                    "", 
                                    item.frameName, 
                                    item.applicationTime, 
                                    null, 
                                    null, "#[KAF005_55]","",color));
                    }
                   
                }); 
                _.forEach(dataBreakTime, (item :any) => {
                    let color: string = "";
                    if (item.errorCode == 1) {
                        color = '#FD4D4D';
                    }
                    if (item.errorCode == 2) {
                        color = '#F6F636';
                    }
                    if (item.errorCode == 3) {
                        color = '#F69164';
                    } 
                    self.breakTimes.push(new common.OvertimeCaculation(
                        item.companyID, 
                        item.appID, 
                        item.attendanceType, 
                        "", 
                        item.frameNo, 
                        "", 
                        item.frameName, 
                        item.applicationTime, 
                        null, 
                        null, "","",color));
                }); 
                _.forEach(dataBonusTime, (item) => { 
                    self.bonusTimes.push(new common.OvertimeCaculation(
                        item.companyID, 
                        item.appID, 
                        item.attendanceType, 
                        "", 
                        item.frameNo, 
                        "", 
                        item.frameName, 
                        item.applicationTime, 
                        null, 
                        null, "","",""));
                }); 
            }

            checkRequiredBreakTimes() {
                let self = this;
                _.each(self.breakTimes(), function(item) {
                    item.applicationTime.subscribe(function(value) {
                        self.clearErrorB6_8();
                        if (!self.hasAppTimeBreakTimes()) {
                            self.setErrorB6_8();
                        }
                    })
                })
            }

            hasAppTimeBreakTimes() {
                let self = this,
                    hasData = false;
                _.each(self.breakTimes(), function(item: common.OvertimeCaculation) {
                    let timeValidator = new nts.uk.ui.validation.TimeValidator(nts.uk.resource.getText("KAF010_56"), "OvertimeAppPrimitiveTime", { required: false, valueType: "Clock", inputFormat: "hh:mm", outputFormat: "time", mode: "time" });
                    if (!util.isNullOrEmpty(item.applicationTime())) {
                        hasData = true;
                    }
                    if (item.applicationTime() == null) return;

                    let control = $('input#overtimeHoursCheck_' + item.attendanceID() + '_' + item.frameNo());
                    let check = timeValidator.validate(control.val());
                    if (!check.isValid) {
                        control.ntsError('set', { messageId: check.errorCode, message: check.errorMessage });
                    }
                })
                return hasData;
            }

            setErrorB6_8() {
                $('.breakTimesCheck').ntsError('set', { messageId: 'FND_E_REQ_INPUT', messageParams: [nts.uk.resource.getText("KAF010_56")] });
            }

            clearErrorB6_8() {
                $('.breakTimesCheck').ntsError('clear');
            }

            update(): JQueryPromise<any> {                
                let self = this,
                appReason: string,
                divergenceReason: string;
                if(self.displayCaculationTime()){
                    if(!appcommon.CommonProcess.checkWorkTypeWorkTime(self.workTypeCd(), self.siftCD(), "kaf010-workType-workTime-div")){
                        return;    
                    }
                }
                if (self.displayCaculationTime()) {
                    $("#inpStartTime1").trigger("validate");
                    $("#inpEndTime1").trigger("validate");
                    if (!self.validate()) { return; }
                }
                if(self.enableOvertimeInput()){
                    if (!self.hasAppTimeBreakTimes()) {
                        self.setErrorB6_8();
                    }      
                }
                //return if has error
                if (nts.uk.ui.errors.hasError()){return;}   
                nts.uk.ui.block.invisible();
                /*
                appReason = self.getReason(
                    self.typicalReasonDisplayFlg(),
                    self.selectedReason(),
                    self.reasonCombo(),
                    self.displayAppReasonContentFlg(),
                    self.multilContent()
                );
                let appReasonError = !appcommon.CommonProcess.checkAppReason(true, self.typicalReasonDisplayFlg(), self.displayAppReasonContentFlg(), appReason);
                if(appReasonError){
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_115' }).then(function(){nts.uk.ui.block.clear();});    
                    return;    
                }
                if(!appcommon.CommonProcess.checklenghtReason(appReason,"#appReason")){
                    return;
                }
                */
                
                let comboBoxReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
                let textAreaReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true);
                
                if(!appcommon.CommonProcess.checklenghtReason(comboBoxReason+":"+textAreaReason,"#appReason")){
                    return;
                }
                divergenceReason = self.getReason(
                    self.displayDivergenceReasonForm(),
                    self.selectedReason2(),
                    self.reasonCombo2(),
                    self.displayDivergenceReasonInput(),
                    self.multilContent2()
                );
                if(!appcommon.CommonProcess.checklenghtReason(divergenceReason,"#divergenceReason")){
                    return;
                }
                let overTimeShiftNightTmp: number = 0;
                let flexExessTimeTmp: number = 0;
                for (let i = 0; i < self.overtimeHours().length; i++) {
                    if(self.overtimeHours()[i].frameNo() == 11){
                        overTimeShiftNightTmp = self.overtimeHours()[i].applicationTime;                    
                    }else if(self.overtimeHours()[i].frameNo() == 12){
                        flexExessTimeTmp = self.overtimeHours()[i].applicationTime;  
                    }
                }
                let command = {
                    version: self.version,
                    appID: self.appID(),
                    applicationDate: new Date(self.appDate()),
                    prePostAtr: self.prePostSelected(),
                    applicantSID: self.employeeID(),
                    applicationReason: textAreaReason,
                    workTypeCode: self.workTypeCd(),
                    siftTypeCode: self.siftCD(),
                    workClockStart1: self.timeStart1(),
                    workClockEnd1: self.timeEnd1(),
                    workClockStart2: self.timeStart2(),
                    workClockEnd2: self.timeEnd2(),
                    goAtr1: self.goSelected1(),
                    backAtr1: self.backSelected1(),
                    goAtr2: self.goSelected2(),
                    backAtr2: self.backSelected2(),
                    bonusTimes: ko.toJS(self.bonusTimes()),
                    overtimeHours: ko.toJS(self.overtimeHours()),
                    breakTimes: ko.toJS(self.breakTimes()),
                    restTime: ko.toJS(self.restTime()),
                    holidayWorkShiftNight: ko.toJS(overTimeShiftNightTmp == null ? -1 : overTimeShiftNightTmp),
                    divergenceReasonContent: divergenceReason,
                    sendMail: self.manualSendMailAtr(),
                    calculateFlag: self.calculateFlag(),
                    appReasonID: comboBoxReason
                }
                
                service.checkBeforeUpdate(command).done((data) => {                
                    if (data.errorCode == 0) {
                        command.appOvertimeDetail = data.appOvertimeDetail;
                        if (data.confirm) {
                            //メッセージNO：829
                            dialog.confirm({ messageId: "Msg_829" }).ifYes(() => {
                                //登録処理を実行
                                self.updateOvertime(command);
                            }).ifNo(() => {
                                //終了状態：処理をキャンセル
                                nts.uk.ui.block.clear();
                                return;
                            });
                        } else {
                            //登録処理を実行
                            self.updateOvertime(command);
                        }
                    } else if (data.errorCode == 1){
                        self.calculateFlag(1);
                        if(data.frameNo == -1){
                            let frameName = "";
                            //Setting color for item error
                            for (let i = 0; i < self.breakTimes().length; i++) {
                                self.changeColor( self.breakTimes()[i].attendanceID(), self.breakTimes()[i].frameNo(),data.errorCode);
                                if(self.breakTimes().length == 1){
                                    frameName = self.breakTimes()[i].frameName();
                                }else{
                                    if(i == 0){
                                        frameName = self.breakTimes()[0].frameName();
                                    }else{
                                        frameName += "、"+ self.breakTimes()[i].frameName();
                                    }
                                }
                            }
                            dialog.alertError({messageId:"Msg_424", messageParams: [self.employeeName(),frameName]}) .then(function() { nts.uk.ui.block.clear(); }); 
                        }else{
                          //Change background color
                            self.changeColor( data.attendanceId, data.frameNo,data.errorCode);
                            dialog.alertError({messageId:"Msg_424", messageParams: [self.employeeName(),$('#overtimeHoursHeader_'+data.attendanceId+'_'+data.frameNo).text()]}) .then(function() { nts.uk.ui.block.clear(); }); 
                        }
                    }
                }).fail((res) => {
                    dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                    .then(function() { nts.uk.ui.block.clear(); });
                });
            }
            
            getBoxReason(){
                var self = this;
                return appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
            
            }
        
            getAreaReason(){
                var self = this;
                return appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true);   
            }
            
            resfreshReason(appReason: string){
                var self = this;
                self.selectedReason('');  
                self.multilContent(appReason);  
            }
            
            updateOvertime(command: any){
                service.updateOvertime(command)
                .done((data) => {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                        if(data.autoSendMail){
                            appcommon.CommonProcess.displayMailResult(data); 
                        } else {
                            location.reload();
                        }
                    });    
                })
                .fail(function(res) { 
                    if(res.optimisticLock == true){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function(){
                            location.reload();
                        });    
                    } else {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();}); 
                    }
                });           
            }
            
            convertOvertimeCaculationToOverTimeInput(param: common.OverTimeInput): common.OverTimeInput{
                return new common.OverTimeInput(
                    param.companyID(),
                    param.appID(),
                    param.attendanceID(),
                    param.attendanceName(),
                    param.frameNo(),
                    "",
                    param.frameName(),
                    null,
                    null,
                    param.applicationTime(),
                    param.nameID(), ""    
                );        
            } 
            
            /**
             * KDL003
             */
            openDialogKdl003() {
                let self = this;
                nts.uk.ui.windows.setShared('parentCodes', {
                   workTypeCodes: self.workTypecodes(),
                selectedWorkTypeCode: self.workTypeCd(),
                workTimeCodes: self.workTimecodes(),
                selectedWorkTimeCode: self.siftCD()
                }, true);
    
                nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                    $("#kaf010-workType-workTime-div").ntsError('clear');
                    $("#kaf010-workType-workTime-div").css("border","none");
                    //view all code of selected item 
                    var childData = nts.uk.ui.windows.getShared('childData');
                    if (childData) {
                        $("#inpStartTime1").ntsError("clear"); 
                        $("#inpEndTime1").ntsError("clear");
                        self.workTypeCd(childData.selectedWorkTypeCode);
                        self.workTypeName(childData.selectedWorkTypeName);
                        self.siftCD(childData.selectedWorkTimeCode);
                        self.siftName(childData.selectedWorkTimeName);
                        self.timeStart1(childData.first.start);
                        self.timeEnd1(childData.first.end);
                        self.timeStart2(childData.second.start);
                        self.timeEnd2(childData.second.end);
                        let param = { workTypeCD: childData.selectedWorkTypeCode, workTimeCD: childData.selectedWorkTimeCode }
                        service.getBreakTimes(param).done((data) => {
                            self.setTimeZones(data);
                        });
                        //                    service.getRecordWork(
                        //                        {
                        //                            employeeID: self.employeeID(), 
                        //                            appDate: nts.uk.util.isNullOrEmpty(self.appDate()) ? null : moment(self.appDate()).format(self.DATE_FORMAT),
                        //                            siftCD: self.siftCD(),
                        //                            prePostAtr: self.prePostSelected(),
                        //                            overtimeHours: ko.toJS(self.breakTimes())
                        //                        }
                        //                    ).done(data => {
                        //                        self.timeStart1(data.startTime1 == null ? null : data.startTime1);
                        //                        self.timeEnd1(data.endTime1 == null ? null : data.endTime1);
                        //                        self.timeStart2(data.startTime2 == null ? null : data.startTime2);
                        //                        self.timeEnd2(data.endTime2 == null ? null : data.endTime2);
                        //                        self.convertAppOvertimeReferDto(data);
                        //                    });
                    }
                })
            }
            /**
             * Jump to CMM018 Screen
             */
            setTimeZones(timeZones) {
                let self = this;
                if (timeZones) {
                    let times = [];
                    for (let i = 1; i < 11; i++) {
                        times.push(new common.OverTimeInput("", "", 0, "", i, 0, i, self.getStartTime(timeZones[i - 1]), self.getEndTime(timeZones[i - 1]), null, ""));
                    }
                    self.restTime(times);
                }
            }

            getStartTime(data) {
                return data ? data.start : null;
            }

            getEndTime(data) {
                return data ? data.end : null;
            }
            
            openCMM018() {
                let self = this;
                nts.uk.request.jump("com", "/view/cmm/018/a/index.xhtml", { screen: 'Application', employeeId: self.employeeID() });
            }
            validate(): boolean{
                let self = this;            
                //勤務時間
                if(!self.validateTime(self.timeStart1(), self.timeEnd1(), '#inpStartTime1')){
                    return false;
                };
                if ( !nts.uk.util.isNullOrEmpty(self.timeStart2()) && self.timeStart2() != "") {
                    if ( !self.validateTime( self.timeStart2(), self.timeEnd2(), '#inpStartTime2' ) ) {
                        return false;
                    };
                }
                //休憩時間
                for (let i = 0; i < self.restTime().length; i++) {
                    let startTime = self.restTime()[i].startTime();
                    let endTime = self.restTime()[i].endTime();
                    let attendanceId = self.restTime()[i].attendanceID();
                    let frameNo = self.restTime()[i].frameNo();
                    if (i != 9) {
                        let startTimeAdd = self.restTime()[i + 1].startTime();
                        let endTimeAdd = self.restTime()[i + 1].endTime();
                        let attendanceIdAdd = self.restTime()[i + 1].attendanceID();
                        let frameNoAdd = self.restTime()[i + 1].frameNo();
                    }
                    if (nts.uk.util.isNullOrEmpty(startTime) && !nts.uk.util.isNullOrEmpty(endTime)) {
                        dialog.alertError({ messageId: "Msg_307" })
                        $('input#restTimeStart_' + attendanceId + '_' + frameNo).focus();
                        return false;
                    }
                    if (!nts.uk.util.isNullOrEmpty(startTime) && startTime != "") {
                        if (!self.validateTime(startTime, endTime, 'input#restTimeEnd_' + attendanceId + '_' + frameNo)) {
                            return false;
                        };
                    }
                    if (!nts.uk.util.isNullOrEmpty(startTimeAdd)) {
                        if (endTime == null) {
                                dialog.alertError({ messageId: "Msg_307" })
                                $('input#restTimeEnd_' + attendanceId + '_' + frameNo).focus();
                                return false;
                        }
                        if (!self.validateTime(endTime, startTimeAdd, 'input#restTimeStart_' + attendanceIdAdd + '_' + frameNoAdd)) {
                                return false;
                        };
                    }
                }
                return true;            
            }
            //Validate input time
            validateTime(startTime: number, endTime: number, elementId: string): boolean{            
                if(startTime >= endTime){
                    dialog.alertError({messageId:"Msg_307"})
                     $(elementId).focus();
                    return false;
                }
                return true;
            }
            CaculationTime(){
                    let self = this;
                    let dfd = $.Deferred();
                
                    if (nts.uk.util.isNullOrEmpty(self.appDate())) {
                        dialog.alertError({ messageId: "Msg_959" });
                        return;
                    }
                    $(".breakTimesCheck").ntsError('clear');
                    $("#inpStartTime1").trigger("validate");
                    $("#inpEndTime1").trigger("validate");
                    //return if has error
                    if (nts.uk.ui.errors.hasError()) { return; }
                    if (!self.validateTime(self.timeStart1(), self.timeEnd1(), '#inpStartTime1')) {
                        return;
                    }
                    if (!nts.uk.util.isNullOrEmpty(self.timeStart2())) {
                        if (!self.validateTime(self.timeStart2(), self.timeEnd2(), '#inpStartTime2')) {
                            return;
                        };
                    }
                    let param : any ={
                        breakTimes: _.map(ko.toJS(self.breakTimes()), item => {return self.initCalculateData(item);}),
                        //bonusTimes: _.map(ko.toJS(self.bonusTimes()), item => {return self.initCalculateData(item);}),
                        prePostAtr : self.prePostSelected(),
                        appDate : nts.uk.util.isNullOrEmpty(self.appDate()) ? null : moment(self.appDate()).format(self.DATE_FORMAT),
                        siftCD: self.siftCD(),
                        workTypeCode: self.workTypeCd(),
                        inputDate: self.inputDate(),
                        startTimeRests: nts.uk.util.isNullOrEmpty(self.restTime()) ? [] : _.map(self.restTime(), function (x) { return x.startTime(); }),
                        endTimeRests: nts.uk.util.isNullOrEmpty(self.restTime()) ? [] : _.map(self.restTime(), function (x) { return x.endTime(); }),
                        startTime: nts.uk.util.isNullOrEmpty(self.timeStart1()) ? null : self.timeStart1(),
                        endTime: nts.uk.util.isNullOrEmpty(self.timeEnd1()) ? null : self.timeEnd1(),
                        employeeID: self.employeeID()
                    }
                    //setting work content
                    self.preWorkContent = {
                            applicationDate: self.appDate(),
                            workType: self.workTypeCd(),
                            siftType: self.siftCD(),
                            workClockFrom1: self.timeStart1(),
                            workClockTo1: self.timeEnd1(),
                            workClockFrom2: self.timeStart2(),
                            workClockTo2: self.timeEnd2(),
                            breakTimes:  ko.toJS(self.breakTimes())
                        }
                    
                    nts.uk.ui.block.invisible();
                    service.getCaculationResult(param).done(function(data){
                           
                       self.breakTimes.removeAll();
                       self.overtimeHours.removeAll();
                       self.bonusTimes.removeAll();
                         if(data != null){
                         for(let i =0; i < data.length; i++){
                             if(data[i].attendanceID == 2){
                               self.breakTimes.push(new common.OvertimeCaculation("", "",
                                    data[i].attendanceID,
                                    "", 
                                    data[i].frameNo,
                                    0, 
                                    data[i].frameName,
                                    data[i].applicationTime,
                                    self.convertIntToTime(data[i].preAppTime),
                                    self.convertIntToTime(data[i].caculationTime),"#[KAF005_55]","",""));
                               self.changeColor(2,data[i].frameNo,data[i].errorCode);//
                           }
                             //残業時間
                             if (data[i].attendanceID == 1) {
                                 let color: string = "";
                                 if (data[i].errorCode == 1) {
                                     color = '#FD4D4D';
                                 }
                                 if (data[i].errorCode == 2) {
                                     color = '#F6F636';
                                 }
                                 if (data[i].errorCode == 3) {
                                     color = '#F69164';
                                 }
                                    
                                   if(data[i].frameNo != 11 && data[i].frameNo != 12){
                                       self.overtimeHours.push(new common.OvertimeCaculation("", "",
                                         data[i].attendanceID,
                                         "", 
                                         data[i].frameNo,
                                         0, 
                                         data[i].frameName,
                                         data[i].applicationTime,
                                         self.convertIntToTime(data[i].preAppTime),
                                         self.convertIntToTime(data[i].caculationTime),"#[KAF005_55]","",color));
                                   }else if(data[i].frameNo == 11){
                                       self.overtimeHours.push(new common.OvertimeCaculation("", "",
                                         data[i].attendanceID,
                                         "", 
                                         data[i].frameNo,
                                         0, 
                                         nts.uk.resource.getText("KAF005_63"),
                                         data[i].applicationTime,
                                         self.convertIntToTime(data[i].preAppTime),
                                         self.convertIntToTime(data[i].caculationTime),"#[KAF005_64]","",color));
                                   }else if(data[i].frameNo == 12){
                                        self.overtimeHours.push(new common.OvertimeCaculation("", "",
                                          data[i].attendanceID,
                                          "", 
                                          data[i].frameNo,
                                          0, 
                                          nts.uk.resource.getText("KAF005_65"),
                                          data[i].applicationTime,
                                          self.convertIntToTime(data[i].preAppTime),
                                          self.convertIntToTime(data[i].caculationTime),"#[KAF005_66]","",color));
                                   }
                                   self.changeColor(1,data[i].frameNo,data[i].errorCode);
                               }
                             //加給時間
                             else if(data[i].attendanceID == 3){
                               self.bonusTimes.push(new common.OvertimeCaculation("", "", data[i].attendanceID,
                                    "", data[i].frameNo,
                                    data[i].timeItemTypeAtr ,
                                    data[i].frameName, data[i].applicationTime,
                                    self.convertIntToTime(data[i].preAppTime), null,"","#F69164"));
                           }   
                         }   
                        }
                        //勤務内容を変更後に計算ボタン押下。計算フラグ=0にする。 
                        if(!self.isEmptyOverTimeInput(ko.toJS(self.breakTimes()))){
                            self.calculateFlag(0);
                        }
                        //Check work content Changed
                         self.checkWorkContentChanged();
                         nts.uk.ui.block.clear();
                         dfd.resolve(data);
                    }).fail(function(res){
                        nts.uk.ui.block.invisible();
                        dfd.reject(res);
                    });
                    return dfd.promise();
                }
                convertAppOvertimeReferDto(data :any){
                let self = this;
                if(data.appOvertimeReference != null){
                self.appDateReference(data.appOvertimeReference.appDateRefer);
                if(data.appOvertimeReference.workTypePre != null){
                    self.workTypeCodeReference(data.appOvertimeReference.workTypeRefer.workTypeCode);
                    self.workTypeNameReference(data.appOvertimeReference.workTypeRefer.workTypeName);
                }
                if(data.appOvertimeReference.siftTypePre != null){
                    self.siftCodeReference(data.appOvertimeReference.siftTypeRefer.siftCode);
                    self.siftNameReference(data.appOvertimeReference.siftTypeRefer.siftName);
                }
                if(data.appOvertimeReference.workClockFrom1Refer != null || data.appOvertimeReference.workClockTo1Refer!= null){
                     self.workClockFrom1To1Reference(self.convertIntToTime(data.appOvertimeReference.workClockFrom1Refer) + " "+ nts.uk.resource.getText("KAF005_126") +" "+self.convertIntToTime(data.appOvertimeReference.workClockTo1Refer));
                }
                if(data.appOvertimeReference.workClockFrom2Refer != null || data.appOvertimeReference.workClockTo2Refer != null){
                    self.workClockFrom2To2Reference(self.convertIntToTime(data.appOvertimeReference.workClockFrom2Refer) +" "+ nts.uk.resource.getText("KAF005_126") +" "+ self.convertIntToTime(data.appOvertimeReference.workClockTo2Refer));
                }
                if(self.workClockFrom2To2Reference () == null){
                    self.displayWorkClockFrom2To2Reference(false);
                }
                self.overtimeHoursPre.removeAll();
                if(data.appOvertimeReference.overTimeInputsRefer != null){
                    for (let i = 0; i < data.appOvertimeReference.overTimeInputsRefer.length; i++) {
                        self.changeColor( 1 , data.appOvertimeReference.overTimeInputsRefer[i].frameNo,data.appOvertimeReference.overTimeInputsRefer[i].errorCode);
                            if(data.appOvertimeReference.overTimeInputsRefer[i].frameNo != 11 && data.appOvertimeReference.overTimeInputsRefer[i].frameNo != 12){
                                self.overtimeHoursReference.push(new common.AppOvertimePre("", "", 
                            data.appOvertimeReference.overTimeInputsRefer[i].attendanceID,
                            "", data.appOvertimeReference.overTimeInputsRefer[i].frameNo,
                            0, data.appOvertimeReference.overTimeInputsRefer[i].frameName +" : ",
                            data.appOvertimeReference.overTimeInputsRefer[i].applicationTime,
                            data.appOvertimeReference.overTimeInputsRefer[i].preAppTime,
                            self.convertIntToTime(data.appOvertimeReference.overTimeInputsRefer[i].caculationTime) ,null));
                            }
                    }
                }
                 self.overTimeShiftNightRefer(self.convertIntToTime(data.appOvertimeReference.overTimeShiftNightRefer));
                 self.flexExessTimeRefer(self.convertIntToTime(data.appOvertimeReference.flexExessTimeRefer));
                }
            }
            convertIntToTime(data : number) : string{
                let hourMinute : string = "";
                if(nts.uk.util.isNullOrEmpty(data)){
                    return null;
                }else if (data == 0) {
                    hourMinute = "0:00";
                }else if(data != null){
                    let hour = Math.floor(Math.abs(data)/60);
                    let minutes = Math.floor(Math.abs(data)%60);
                    hourMinute =  hour + ":"+ (minutes < 10 ? ("0" + minutes) : minutes);
                }
                return hourMinute;
            }
            
            changeColor(attendanceId, frameNo,errorCode){
                if(errorCode == 1){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FD4D4D')
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FD4D4D')
                }
                if(errorCode == 2){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F6F636')
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F6F636')
                }
                 if(errorCode == 3){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F69164')
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F69164')
                }
           
            }
            
            getReason(inputReasonDisp: boolean, inputReasonID: string, inputReasonList: Array<common.ComboReason>, detailReasonDisp: boolean, detailReason: string): string{
                let appReason = '';
                let inputReason: string = '';
                if(!nts.uk.util.isNullOrEmpty(inputReasonID)){
                    inputReason = _.find(inputReasonList, o => { return o.reasonId == inputReasonID; }).reasonName;    
                }    
                if(inputReasonDisp==true&&detailReasonDisp==true){
                    if(!nts.uk.util.isNullOrEmpty(inputReason)&&!nts.uk.util.isNullOrEmpty(detailReason)){
                        appReason = inputReason + ":" + detailReason;
                    } else if(!nts.uk.util.isNullOrEmpty(inputReason)&&nts.uk.util.isNullOrEmpty(detailReason)){
                        appReason = inputReason; 
                    } else if(nts.uk.util.isNullOrEmpty(inputReason)&&!nts.uk.util.isNullOrEmpty(detailReason)){
                        appReason = detailReason;             
                    }                
                } else if(inputReasonDisp==true&&detailReasonDisp==false){
                    appReason = inputReason;                 
                } else if(inputReasonDisp==false&&detailReasonDisp==true){
                    appReason = detailReason;     
                } 
                return appReason;
            }
            private isEmptyOverTimeInput(OverTimeInputs: Array<any>): boolean {
                return _.isEmpty(_.filter(OverTimeInputs, x => !nts.uk.util.isNullOrEmpty(x.applicationTime)));
            }
            private initCalculateData(item: any): any{
                return data ={companyID: item.companyID,
                                appID: item.appID,
                                attendanceID: item.attendanceID,
                                attendanceName: item.attendanceName,
                                frameNo: item.frameNo,
                                timeItemTypeAtr: item.timeItemTypeAtr,
                                frameName: item.frameName,
                                applicationTime: item.applicationTime,
                                preAppTime: null,
                                caculationTime: null,
                                nameID: item.nameID, 
                                itemName:item.itemName};
            }
            private checkWorkContentChanged(){
                let self = this;
                //Check calculate times
                //勤務種類         
                self.workTypeCd.subscribe(value => {
                    if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
                        if (self.preWorkContent.workType != value) {
                            //→エラーＭＳＧ
                            self.calculateFlag(1);
                        }
                    }
                });
                //就業時間帯      
                self.siftCD.subscribe(value => {
                    if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
                        if (self.preWorkContent.siftType != value) {
                            //→エラーＭＳＧ
                            self.calculateFlag(1);
                        }
                    }
                });
                //勤務時間
                self.timeStart1.subscribe(value => {
                    if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
                        if (self.preWorkContent.workClockFrom1 != value) {
                            //→エラーＭＳＧ
                            self.calculateFlag(1);
                        }
                    }
                });
                self.timeEnd1.subscribe(value => {
                    if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
                        if (self.preWorkContent.workClockTo1 != value) {
                            //→エラーＭＳＧ
                            self.calculateFlag(1);
                        }
                    }
                });
                self.timeStart2.subscribe(value => {
                    if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
                        if (self.preWorkContent.workClockFrom2 != value) {
                            //→エラーＭＳＧ
                            self.calculateFlag(1);
                        }
                    }
                });
                self.timeEnd2.subscribe(value => {
                    if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
                        if (self.preWorkContent.workClockTo2 != value) {
                            //→エラーＭＳＧ
                            self.calculateFlag(1);
                        }
                    }
                });
//                //休憩時間
//                for (let i = 0; i < self.breakTimes().length; i++) {
//                    self.breakTimes()[i].applicationTime.subscribe(value => {
//                        if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
//                            if (self.preWorkContent.breakTimes[i].applicationTime != value) {
//                                //→エラーＭＳＧ
//                                self.calculateFlag(1);
//                            }
//                        }
//                    });
//                }
            }
        }
    }
}