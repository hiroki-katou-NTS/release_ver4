module nts.uk.at.view.kaf005.b {
    import common = nts.uk.at.view.kaf005.share.common;
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import service = nts.uk.at.view.kaf005.shr.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import util = nts.uk.util;
    import text = nts.uk.resource.getText;

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
            //Pre-POST
            prePostSelected: KnockoutObservable<number> = ko.observable(0);
            workState: KnockoutObservable<boolean> = ko.observable(true);;
            typeSiftVisible: KnockoutObservable<boolean> = ko.observable(true);
            // 申請日付
            appDate: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));
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
            employeeList: KnockoutObservableArray<common.EmployeeOT> = ko.observableArray([]);
            selectedEmplCodes: KnockoutObservable<string> = ko.observable(null);
            employeeFlag: KnockoutObservable<boolean> = ko.observable(false);
            totalEmployee: KnockoutObservable<string> = ko.observable(null);
            //休出時間
            restTime: KnockoutObservableArray<common.OverTimeInput> = ko.observableArray([]);
            //残業時間
            overtimeHours: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            overtimeHoursOld: Array<common.OvertimeCaculation> = [];
            //休憩時間
            breakTimes: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            //加給時間
            bonusTimes: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
            bonusTimesOld: Array<common.OvertimeCaculation> = [];
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
            workTypeChangeFlg: KnockoutObservable<boolean> = ko.observable(false);
            
            // AppOvertimeReference
            appDateReference: KnockoutObservable<string> = ko.observable("");
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
            allPreAppPanelFlg: KnockoutObservable<boolean> = ko.observable(false);
            overtimeAtr: KnockoutObservable<number> = ko.observable(null);
            heightOvertimeHours: KnockoutObservable<number> = ko.observable(null);
            //画面モード(表示/編集)
            editable: KnockoutObservable<boolean> = ko.observable( true );
            enableOvertimeInput: KnockoutObservable<boolean> = ko.observable(false);
            appCur: any = null;
            constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata, rebind?: boolean) {
                super(listAppMetadata, currentApp);
                var self = this;
                self.appCur = currentApp;
                self.startPage(self.appID()).done(function(){
                    if(nts.uk.util.isNullOrUndefined(rebind)){
                        $("#fixed-overtime-hour-table").ntsFixedTable({ height: self.heightOvertimeHours() });
                        $("#fixed-break_time-table").ntsFixedTable({ height: 120 });
                        $("#fixed-bonus_time-table").ntsFixedTable({ height: 120 });
                        $("#fixed-table-indicate").ntsFixedTable({ height: 120 });
                        $("#fixed-table").ntsFixedTable({ height: 120 });
                        $("#fixed-overtime-hour-table-pre").ntsFixedTable({ height: self.heightOvertimeHours() });
                        $("#fixed-bonus_time-table-pre").ntsFixedTable({ height: 120 });
                        $('.nts-fixed-table.cf').first().find('.nts-fixed-body-container.ui-iggrid').css('border-left','1px solid #CCC');
                    } else {
                        if(rebind==true){
                            $("#fixed-overtime-hour-table").ntsFixedTable({ height: self.heightOvertimeHours() - 23 });
                            $("#fixed-break_time-table").ntsFixedTable({ height: 96 });
                            $("#fixed-bonus_time-table").ntsFixedTable({ height: 96 });
                            $("#fixed-table-indicate").ntsFixedTable({ height: 96 });
                            $("#fixed-table").ntsFixedTable({ height: 96 });
                            $("#fixed-overtime-hour-table-pre").ntsFixedTable({ height: self.heightOvertimeHours() - 23 });
                            $("#fixed-bonus_time-table-pre").ntsFixedTable({ height: 96 });
                            $('.nts-fixed-table.cf').first().find('.nts-fixed-body-container.ui-iggrid').css('border-left','1px solid #CCC');        
                        }    
                    }
                });
            }
            
            startPage(appID: string): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                var dfd = $.Deferred();
                service.findByAppID(appID).done((data) => {
                    //write log
                    let paramLog = {programId: 'KAF000',
                                    screenId: 'B', 
                                    queryString: 'apptype=0_'+data.overtimeAtr};
                    nts.uk.at.view.kaf000.b.service.writeLog(paramLog);
                    //get pg-name
                    let namePath = nts.uk.text.format("sys/portal/standardmenu/findPgName/{0}/{1}/{2}", 'KAF005', 'B', 'overworkatr='+data.overtimeAtr);
                    nts.uk.request.ajax("com", namePath).done((value) => {
                        if(!nts.uk.util.isNullOrEmpty(value)){
                            $("#pg-name").text(value);
                        }else{
                            $("#pg-name").text('');
                        }
                    });
                    self.initData(data);
                    self.checkRequiredOvertimeHours();
                    //Check work content Changed
                    self.checkWorkContentChanged();
                    nts.uk.ui.block.clear();
                    dfd.resolve(); 
                }).fail(function(res) {
                    if(res.messageId == 'Msg_426'){
                       dialog.alertError({messageId : res.messageId}).then(function(){
                            nts.uk.ui.block.clear();
                    });
                    }else{ 
                        nts.uk.ui.dialog.alertError(res.message).then(function(){
                            nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
                            nts.uk.ui.block.clear();
                        });
                    }
                    dfd.reject(res);  
                });
                return dfd.promise();
            }
            
            isShowReason() {
                let self = this;
                if (self.screenModeNew()) {
                    return self.displayAppReasonContentFlg();
                } else {
                    return self.displayAppReasonContentFlg() || self.typicalReasonDisplayFlg();
                }
            }
            
            getName(code, name) {
                let result = "";
                if (code) {
                    result = name || text("KAL003_120");
                }
                return result;
            }

            
            initData(data: any) {
                var self = this;
                self.version = data.application.version;
                self.enableOvertimeInput(data.enableOvertimeInput);
                self.manualSendMailAtr(data.manualSendMailAtr);
                self.prePostSelected(data.application.prePostAtr);
                self.displayCaculationTime(data.displayCaculationTime);
                self.typicalReasonDisplayFlg(data.typicalReasonDisplayFlg);
                self.displayAppReasonContentFlg(data.displayAppReasonContentFlg);
                self.requiredReason(data.displayAppReasonContentFlg);
                self.displayDivergenceReasonForm(data.displayDivergenceReasonForm);
                self.displayDivergenceReasonInput(data.displayDivergenceReasonInput);
                self.displayBonusTime(data.displayBonusTime);
                self.restTimeDisFlg(data.displayRestTime);
                self.appDate(data.application.applicationDate);
                self.employeeName(data.employeeName);
                self.employeeID(data.application.applicantSID);
                if (data.siftType != null) {
                    self.siftCD(data.siftType.siftCode);
                    self.siftName(self.getName(data.siftType.siftCode,data.siftType.siftName));
                }
                if (data.workType != null) {
                    self.workTypeCd(data.workType.workTypeCode);
                    self.workTypeName(self.getName(data.workType.workTypeCode, data.workType.workTypeName));
                }
                
                self.workTypecodes(data.workTypes);
                self.workTimecodes(data.siftTypes);
                
                self.timeStart1(data.workClockFrom1 == null ? null : data.workClockFrom1);
                self.timeEnd1(data.workClockTo1 == null ? null : data.workClockTo1);
                self.timeStart2(data.workClockFrom2 == null ? null : data.workClockFrom2);
                self.timeEnd2(data.workClockTo2 == null ? null : data.workClockTo2);
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
                self.overtimeAtr(data.overtimeAtr);
//                if (data.overtimeAtr == 0) {
//                    self.heightOvertimeHours(56);
//                } else if (data.overtimeAtr == 1) {
//                    self.heightOvertimeHours(180);
//                } else {
//                    self.heightOvertimeHours(216);
//                }
                self.instructInforFlag(data.displayOvertimeInstructInforFlg);
                self.instructInfor(data.overtimeInstructInformation);
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
                self.workTypeChangeFlg(data.workTypeChangeFlg);
                // preAppOvertime
                if(data.preAppOvertimeDto != null){
                    self.appDatePre(data.preAppOvertimeDto.appDatePre);
                    if(data.preAppOvertimeDto.workTypePre != null){
                        self.workTypeCodePre(data.preAppOvertimeDto.workTypePre.workTypeCode);
                        self.workTypeNamePre(data.preAppOvertimeDto.workTypePre.workTypeName);
                    }
                    if(data.preAppOvertimeDto.siftTypePre != null){
                        self.siftCodePre(data.preAppOvertimeDto.siftTypePre.siftCode);
                        self.siftNamePre(data.preAppOvertimeDto.siftTypePre.siftName);
                    }
                    self.workClockFrom1To1Pre(data.preAppOvertimeDto.workClockFromTo1Pre);
                    self.workClockFrom2To2Pre(data.preAppOvertimeDto.workClockFromTo2Pre);
                    if (nts.uk.util.isNullOrEmpty(self.workClockFrom2To2Pre())) {
                        self.displayWorkClockFrom2To2Pre(false);
                    }
                    if(data.preAppOvertimeDto.overTimeInputsPre != null){
                        for (let i = 0; i < data.preAppOvertimeDto.overTimeInputsPre.length; i++) {
                            if(data.preAppOvertimeDto.overTimeInputsPre[i].frameNo != 11 && data.preAppOvertimeDto.overTimeInputsPre[i].frameNo != 12){
                                self.overtimeHoursPre.push(new common.OverTimeInput("",
                                 "",
                                 data.preAppOvertimeDto.overTimeInputsPre[i].attendanceID,
                                 "",
                                 data.preAppOvertimeDto.overTimeInputsPre[i].frameNo,
                                 0,
                                 data.preAppOvertimeDto.overTimeInputsPre[i].frameName == null ? null : (data.preAppOvertimeDto.overTimeInputsPre[i].frameName + " : "),
                                 data.preAppOvertimeDto.overTimeInputsPre[i].startTime, data.preAppOvertimeDto.overTimeInputsPre[i].endTime,
                                 data.preAppOvertimeDto.overTimeInputsPre[i].applicationTime ==-1 ? null :self.convertIntToTime(data.preAppOvertimeDto.overTimeInputsPre[i].applicationTime),
                                 null));
                            }
                        }
                    }
                    self.overTimeShiftNightPre(data.preAppOvertimeDto.overTimeShiftNightPre == -1 ? null : self.convertIntToTime(data.preAppOvertimeDto.overTimeShiftNightPre));
                    self.flexExessTimePre(data.preAppOvertimeDto.flexExessTimePre == -1 ? null : self.convertIntToTime(data.preAppOvertimeDto.flexExessTimePre));
                }
                self.convertAppOvertimeReferDto(data);
                let dataRestTime = _.filter(data.overTimeInputs, {'attendanceID': 0});
                let dataOverTime = _.filter(data.overTimeInputs, {'attendanceID': 1});
                let dataBreakTime = _.filter(data.overTimeInputs, {'attendanceID': 2});
                let dataBonusTime = _.filter(data.overTimeInputs, {'attendanceID': 3});
                self.datatest = dataOverTime;
                self.restTime.removeAll();
                self.overtimeHours.removeAll();
                self.breakTimes.removeAll();
                self.bonusTimes.removeAll();
                if(nts.uk.util.isNullOrEmpty(dataRestTime)){
                    for (let i = 0; i < 11; i++) {
                        self.restTime.push(new common.OverTimeInput("", "", 0, "", i,0, i.toString(), null, null, null,""));
                    }    
                } else {
                    _.forEach(dataRestTime, (item) => { 
                        
                            self.restTime.push(new common.OverTimeInput(
                                item.companyID, 
                                item.appID, 
                                item.attendanceID, 
                                "", 
                                item.frameNo, 
                                item.timeItemTypeAtr, 
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
                            self.overtimeHours.push(new common.OvertimeCaculation(
                                item.companyID,
                                item.appID,
                                item.attendanceID,
                                "",
                                item.frameNo,
                                item.timeItemTypeAtr,
                                nts.uk.resource.getText("KAF005_63"),
                                item.applicationTime,
                                null,
                                null, "#[KAF005_64]", "", color));
                        }
                    } else if (item.frameNo == 12) {
                        if (data.flexFLag) {
                            self.overtimeHours.push(new common.OvertimeCaculation(
                                item.companyID,
                                item.appID,
                                item.attendanceID,
                                "",
                                item.frameNo,
                                item.timeItemTypeAtr,
                                nts.uk.resource.getText("KAF005_65"),
                                item.applicationTime,
                                null,
                                null, "#[KAF005_66]", "", color));
                        }
                    } else {
                        self.overtimeHours.push(new common.OvertimeCaculation(
                            item.companyID,
                            item.appID,
                            item.attendanceID,
                            "",
                            item.frameNo,
                            item.timeItemTypeAtr,
                            item.frameName,
                            item.applicationTime,
                            null,
                            null, "#[KAF005_55]", "", color));
                    }
                   
                }); 
                _.forEach(dataBreakTime, (item :any) => { 
                    self.breakTimes.push(new common.OvertimeCaculation(
                        item.companyID, 
                        item.appID, 
                        item.attendanceID, 
                        "", 
                        item.frameNo, 
                        item.timeItemTypeAtr, 
                        item.frameName, 
                        item.applicationTime, 
                        null, 
                        null, "","",""));
                }); 
                _.forEach(dataBonusTime, (item) => { 
                    self.bonusTimes.push(new common.OvertimeCaculation(
                        item.companyID, 
                        item.appID, 
                        item.attendanceID, 
                        "", 
                        item.frameNo, 
                        item.timeItemTypeAtr, 
                        item.frameName, 
                        item.applicationTime, 
                        null, 
                        null, "","",""));
                }); 
                
                switch(self.overtimeHours().length){
                    case 1: 
                        self.heightOvertimeHours(56);
                        break;         
                    case 2:
                        self.heightOvertimeHours(88);
                        break;     
                    case 3: 
                        self.heightOvertimeHours(120);    
                        break;
                    case 4:
                        self.heightOvertimeHours(152);    
                        break;
                    case 5:
                        self.heightOvertimeHours(184);    
                        break;    
                    default: break;
                }
            }

            checkRequiredOvertimeHours() {
                let self = this;
                _.each(self.overtimeHours(), function(item) {
                    item.applicationTime.subscribe(function(value) {
                        self.clearErrorB6_8();
                        if (!self.hasAppTimeOvertimeHours()) {
                            self.setErrorB6_8();
                        }
                    })
                })
            }
            
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
            
            hasAppTimeOvertimeHours(){
                let self = this,
                    hasData = false;
                _.each(self.overtimeHours(), function(item: common.OvertimeCaculation) {
                    let timeValidator = new nts.uk.ui.validation.TimeValidator(self.getValueOfNameId(item.nameID()), "OvertimeAppPrimitiveTime", { required: false, valueType: "Clock", inputFormat: "hh:mm", outputFormat: "time", mode: "time" });
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
                let self = this;
                _.each(self.overtimeHours(), function(item) {
                    $('input#overtimeHoursCheck_' + item.attendanceID() + '_' + item.frameNo())
                        .ntsError('set', { messageId: 'FND_E_REQ_INPUT', messageParams: [self.getValueOfNameId(item.nameID())] });
                })
            }

            getValueOfNameId(nameId) {
                let name = "";
                for (let i = 0; i < nameId.length; i++) {
                    let c = nameId.charAt(i);
                    if (c === '#' || c === '[' || c === ']') {
                        continue;
                    }
                    name += c;
                }
                return nts.uk.resource.getText(name);
            }
    
            clearErrorB6_8() {
                $('.overtimeHoursCheck').ntsError('clear');
            }
            
            update(): JQueryPromise<any> {                
                let self = this,
                appReason: string,
                divergenceReason: string;
                if (self.displayCaculationTime()) {
                    $("#inpStartTime1").trigger("validate");
                    $("#inpEndTime1").trigger("validate");
                    if (!self.validate()) { return; }
                }
                if(self.enableOvertimeInput()){
                    if (!self.hasAppTimeOvertimeHours()) {
                        self.setErrorB6_8();
                    }
                }
                //return if has error
                if (nts.uk.ui.errors.hasError()){return;}   
                nts.uk.ui.block.invisible();
                appReason = self.getReason(
                    self.typicalReasonDisplayFlg(),
                    self.selectedReason(),
                    self.reasonCombo(),
                    self.displayAppReasonContentFlg(),
                    self.multilContent()
                );
                
                let comboBoxReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
                let textAreaReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true);
                
                if(!appcommon.CommonProcess.checklenghtReason(comboBoxReason+":"+textAreaReason,"#appReason")){
                    return;
                }
                
                let comboDivergenceReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason2(), self.reasonCombo2(), self.displayDivergenceReasonForm());
                let areaDivergenceReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent2(), self.displayDivergenceReasonInput(), true);
                
                if(!appcommon.CommonProcess.checklenghtReason(comboDivergenceReason+":"+areaDivergenceReason,"#divergenceReason")){
                    return;
                }
                
                let overTimeShiftNightTmp: number = null;
                let flexExessTimeTmp: number = null;
                for (let i = 0; i < self.overtimeHours().length; i++) {
                    if(self.overtimeHours()[i].frameNo() == 11){
                        overTimeShiftNightTmp = self.overtimeHours()[i].applicationTime();                    
                    }
                    if(self.overtimeHours()[i].frameNo() == 12){
                        flexExessTimeTmp = self.overtimeHours()[i].applicationTime();  
                    }
                }
                let command = {
                    version: self.version,
                    appID: self.appID(),
                    applicationDate: new Date(self.appDate()),
                    prePostAtr: self.prePostSelected(),
                    applicantSID: self.employeeID(),
                    applicationReason: textAreaReason,
                    appApprovalPhaseCmds: self.approvalList,
                    workTypeCode: self.workTypeCd(),
                    siftTypeCode: self.siftCD(),
                    workClockFrom1: self.timeStart1(),
                    workClockTo1: self.timeEnd1(),
                    workClockFrom2: self.timeStart2(),
                    workClockTo2: self.timeEnd2(),
                    breakTimes: ko.mapping.toJS(_.map(self.breakTimes(), item => self.convertOvertimeCaculationToOverTimeInput(item))),
                    overtimeHours: ko.mapping.toJS(_.map(self.overtimeHours(), item => self.convertOvertimeCaculationToOverTimeInput(item))),
                    restTime: ko.mapping.toJS(self.restTime()),
                    bonusTimes: ko.mapping.toJS(_.map(self.bonusTimes(), item => self.convertOvertimeCaculationToOverTimeInput(item))),
                    overTimeShiftNight: overTimeShiftNightTmp == null ? null : overTimeShiftNightTmp,
                    flexExessTime: flexExessTimeTmp == null ? null : flexExessTimeTmp,
                    overtimeAtr: self.overtimeAtr(),
                    divergenceReasonContent: comboDivergenceReason,
                    sendMail: self.manualSendMailAtr(),
                    calculateFlag: self.calculateFlag(),
                    appReasonID: comboBoxReason,
                    divergenceReasonArea: areaDivergenceReason,
                    user: self.user,
                    reflectPerState: self.reflectPerState
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
                            for (let i = 0; i < self.overtimeHours().length; i++) {
                                self.changeColor( self.overtimeHours()[i].attendanceID(), self.overtimeHours()[i].frameNo(),data.errorCode);
                                if(self.overtimeHours().length == 1){
                                    frameName = self.overtimeHours()[i].frameName();
                                }else{
                                    if(i == 0){
                                        frameName = self.overtimeHours()[0].frameName();
                                    }else{
                                        frameName += "、"+ self.overtimeHours()[i].frameName();
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
                let self = this;
                service.updateOvertime(command)
                .done((data) => {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                        if(data.autoSendMail){
                            appcommon.CommonProcess.displayMailResult(data);  
                        } else {
                            self.reBinding(self.listAppMeta, self.appCur, false);
                        }
                    });   
                })
                .fail(function(res) { 
                    if(res.optimisticLock == true){
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function(){
                            self.reBinding(self.listAppMeta, self.appCur, false);
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
                    param.timeItemTypeAtr(),
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
                if (!nts.uk.util.isNullOrEmpty(self.appDate())) {
                    nts.uk.ui.errors.clearAll();
                    $("#inputdate").trigger("validate");
                    if (nts.uk.ui.errors.hasError()) {
                        return;
                    }
                }
                nts.uk.ui.windows.setShared('parentCodes', {
                    workTypeCodes: self.workTypecodes(),
                    selectedWorkTypeCode: self.workTypeCd(),
                    workTimeCodes: self.workTimecodes(),
                    selectedWorkTimeCode: self.siftCD(),
                    showNone: false
                }, true);
    
                nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                    //view all code of selected item 
                    var childData = nts.uk.ui.windows.getShared('childData');
                    if (childData) {
                        self.workTypeCd(childData.selectedWorkTypeCode);
                        self.workTypeName(childData.selectedWorkTypeName);
                        self.siftCD(childData.selectedWorkTimeCode);
                        self.siftName(childData.selectedWorkTimeName);
                        service.getRecordWork(
                            {
                                employeeID: self.employeeID(), 
                                appDate: moment(self.appDate()).format("YYYY/MM/DD"),
                                siftCD: self.siftCD(),
                                prePostAtr: self.prePostSelected(),
                                overtimeHours:  _.map(ko.toJS(self.overtimeHours()), item => {return self.initCalculateData(item);}),
                                workTypeCode: self.workTypeCd(),
                                startTimeRests: nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(), x=>{return x.startTime()}),
                                endTimeRests: nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(), x=>{return x.endTime()})
                            }
                        ).done(data => {
                            $("#inpStartTime1").ntsError("clear"); 
                            $("#inpEndTime1").ntsError("clear");
                            self.timeStart1(data.startTime1 == null ? null : data.startTime1);
                            self.timeEnd1(data.endTime1 == null ? null : data.endTime1);
                            self.timeStart2(data.startTime2 == null ? null : data.startTime2);
                            self.timeEnd2(data.endTime2 == null ? null : data.endTime2); 
                            self.convertAppOvertimeReferDto(data);   
                            self.setTimeZones(data.timezones);
                        });
                    }
                })
            }
            /**
             * Jump to CMM018 Screen
             */
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
//                if ( !nts.uk.util.isNullOrEmpty(self.timeStart2()) && self.timeStart2() != "") {
//                    if ( !self.validateTime( self.timeStart2(), self.timeEnd2(), '#inpStartTime2' ) ) {
//                        return false;
//                    };
//                }
                //休憩時間
                for (let i = 0; i < self.restTime().length; i++) {
                    let startTime = self.restTime()[i].startTime();
                    let endTime = self.restTime()[i].endTime();
                    let attendanceId = self.restTime()[i].attendanceID();
                    let frameNo = self.restTime()[i].frameNo();
                    if(!nts.uk.util.isNullOrEmpty(startTime) && startTime != ""){
                        if(!self.validateTime(startTime, endTime, 'input#restTimeStart_'+attendanceId+'_'+frameNo)){
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
                if(startTime < 300){
                    dialog.alertError({messageId:"Msg_307"})
                     $(elementId).focus();
                    return false;    
                }
                if(endTime > 1740){
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
                    $(".overtimeHoursCheck").ntsError('clear');
                    $("#inpStartTime1").trigger("validate");
                    $("#inpEndTime1").trigger("validate");
                    
                    //return if has error
                    if (nts.uk.ui.errors.hasError()) { return; }
                    if (!self.validateTime(self.timeStart1(), self.timeEnd1(), '#inpStartTime1')) {
                        return;
                    }
//                    if (!nts.uk.util.isNullOrEmpty(self.timeStart2())) {
//                        if (!self.validateTime(self.timeStart2(), self.timeEnd2(), '#inpStartTime2')) {
//                            return;
//                        };
//                    }
                    nts.uk.ui.block.invisible();
                    let param : any ={
                        overtimeHours: _.map(ko.toJS(self.overtimeHours()), item => {return self.initCalculateData(item);}),
                        bonusTimes: _.map(ko.toJS(self.bonusTimes()), item => {return self.initCalculateData(item);}),
                        prePostAtr : self.prePostSelected(),
                        appDate : moment(self.appDate()).format(self.DATE_FORMAT),
                        siftCD: self.siftCD(),
                        workTypeCode: self.workTypeCd(),
                        startTimeRests: nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(), x=>{return x.startTime()}),
                        endTimeRests: nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(), x=>{return x.endTime()}),
                        startTime: nts.uk.util.isNullOrEmpty(self.timeStart1()) ? null : self.timeStart1(),
                        endTime: nts.uk.util.isNullOrEmpty(self.timeEnd1()) ? null : self.timeEnd1()
                    }
                    //setting work content
                     //setting work content
                    self.preWorkContent = {
                        applicationDate: self.appDate(),
                        workType: self.workTypeCd(),
                        siftType: self.siftCD(),
                        workClockFrom1: self.timeStart1(),
                        workClockTo1: self.timeEnd1(),
                        workClockFrom2: self.timeStart2(),
                        workClockTo2: self.timeEnd2(),
                        overtimeHours: ko.toJS(self.overtimeHours())
                    }
                    service.getCaculationResult(param).done(function(data){
                        self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                        self.bonusTimesOld = ko.toJS(self.bonusTimes());  
                        if(self.prePostSelected()==0){
                            self.bonusTimes.removeAll();
                            _.forEach(data, item => {
                                if(item.attendanceID == 1) {
                                    let changeItem = _.find(self.overtimeHours(), otItem => {
                                        return otItem.attendanceID() == 1 &&
                                            otItem.frameNo() == item.frameNo;    
                                    }); 
                                    if(changeItem) {
                                        changeItem.applicationTime(item.applicationTime);
                                        self.changeColorCalc(item);
                                    }
                                } else if(item.attendanceID == 3) {
                                    self.bonusTimes.push(new common.OvertimeCaculation("", "", item.attendanceID,
                                        "", item.frameNo,
                                        item.timeItemTypeAtr ,
                                        item.frameName, item.applicationTime,
                                        self.convertIntToTime(item.preAppTime), null,"","",""));
                                }       
                            });         
                        } else {   
                            self.overtimeHours.removeAll();
                            self.bonusTimes.removeAll();
                            if(data != null){
                                for(let i =0; i < data.length; i++){
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
                        }
                        //勤務内容を変更後に計算ボタン押下。計算フラグ=0にする。 
                        if(!self.isEmptyOverTimeInput(ko.toJS(self.overtimeHours()))){
                            self.calculateFlag(0);
                        }
                        //Check work content Changed
                        self.checkWorkContentChanged();
                        nts.uk.ui.block.clear();
                         dfd.resolve(data);
                    }).fail(function(res){
                        nts.uk.ui.block.clear();
                        dfd.reject(res);
                    });
                    return dfd.promise();
                }
            convertAppOvertimeReferDto(data :any){
                let self = this;
                if(data.appOvertimeReference != null){
                    self.appDateReference(data.appOvertimeReference.appDateRefer);
                    if(data.appOvertimeReference.workTypeRefer != null){
                        self.workTypeCodeReference(data.appOvertimeReference.workTypeRefer.workTypeCode);
                        self.workTypeNameReference(self.getName(data.appOvertimeReference.workTypeRefer.workTypeCode, data.appOvertimeReference.workTypeRefer.workTypeName));
                    } else {
                        self.workTypeCodeReference("");
                        self.workTypeNameReference("");
                    }
                    if (data.appOvertimeReference.siftTypeRefer != null) {
                        self.siftCodeReference(data.appOvertimeReference.siftTypeRefer.siftCode);
                        self.siftNameReference(self.getName(data.appOvertimeReference.siftTypeRefer.siftCode, data.appOvertimeReference.siftTypeRefer.siftName));
                    } else {
                        self.siftCodeReference("");
                        self.siftNameReference("");
                    }
                    self.workClockFrom1To1Reference(data.appOvertimeReference.workClockFromTo1Refer);
                    self.workClockFrom2To2Reference(data.appOvertimeReference.workClockFromTo2Refer);
                    if(nts.uk.util.isNullOrEmpty(self.workClockFrom2To2Reference())){
                        self.displayWorkClockFrom2To2Reference(false);
                    }
                    self.overtimeHoursReference.removeAll();
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
                                data.appOvertimeReference.overTimeInputsRefer[i].caculationTime == -1 ? null : self.convertIntToTime(data.appOvertimeReference.overTimeInputsRefer[i].caculationTime) ,null));
                                }
                        }
                    }
                     self.overTimeShiftNightRefer(data.appOvertimeReference.overTimeShiftNightRefer == -1 ? null : self.convertIntToTime(data.appOvertimeReference.overTimeShiftNightRefer));
                     self.flexExessTimeRefer(data.appOvertimeReference.flexExessTimeRefer == -1? null : self.convertIntToTime(data.appOvertimeReference.flexExessTimeRefer));
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
    
            changeColorCalc(calcItem){
                let self = this;
                let oldValue = _.find(self.overtimeHoursOld, item => {
                    return item.attendanceID == 1 &&
                        item.frameNo == calcItem.frameNo;    
                });  
                let calcChange = false;
                if((nts.uk.util.isNullOrUndefined(oldValue)) || 
                    (nts.uk.util.isNullOrUndefined(oldValue.applicationTime)) || 
                    (ko.toJSON(oldValue.applicationTime).localeCompare(ko.toJSON(calcItem.applicationTime))!=0)){
                    calcChange = true;
                }     
                let compareValue = 0;
                let errorCode = 0;
                if(!nts.uk.util.isNullOrUndefined(oldValue)) {
                    if(!nts.uk.util.isNullOrUndefined(oldValue.applicationTime)) {
                        compareValue = oldValue.applicationTime;         
                    }    
                }   
                if(ko.toJSON(compareValue).localeCompare(ko.toJSON(calcItem.applicationTime))!=0){
                    errorCode = 1;        
                }
                if(errorCode == 1 && calcChange){
                    $('td#overtimeHoursCheck_'+calcItem.attendanceID+'_'+calcItem.frameNo).css('background', '#F69164');
                    $('input#overtimeHoursCheck_'+calcItem.attendanceID+'_'+calcItem.frameNo).css('background', '#F69164');
                    return '#F69164';
                }
                if(self.editable()&& self.enableOvertimeInput()){
                    if(calcChange){
                        $('td#overtimeHoursCheck_'+calcItem.attendanceID+'_'+calcItem.frameNo).css('background', 'none');
                        $('input#overtimeHoursCheck_'+calcItem.attendanceID+'_'+calcItem.frameNo).css('background', 'none');
                        return 'none'; 
                    }
                } else {
                    if(calcChange){
                        $('td#overtimeHoursCheck_'+calcItem.attendanceID+'_'+calcItem.frameNo).css('background', '#ebebe4');
                        $('input#overtimeHoursCheck_'+calcItem.attendanceID+'_'+calcItem.frameNo).css('background', '#ebebe4');
                        return '#ebebe4';
                    }
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
//                for (let i = 0; i < self.overtimeHours().length; i++) {
//                    self.overtimeHours()[i].applicationTime.subscribe(value => {
//                        if (!nts.uk.util.isNullOrEmpty(self.preWorkContent)) {
//                            if (self.preWorkContent.overtimeHours[i].applicationTime != value) {
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