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
            screenModeNew: KnockoutObservable<boolean> = ko.observable(false);
            
            manualSendMailAtr: KnockoutObservable<boolean> = ko.observable(false);
            //申請者
            employeeName: KnockoutObservable<string> = ko.observable("");
            //Pre-POST
            prePostSelected: KnockoutObservable<number> = ko.observable(0);
            typeSiftVisible: KnockoutObservable<boolean> = ko.observable(true);
            // 申請日付
            appDate: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));
            //TIME LINE 1
            timeStart1: KnockoutObservable<number> = ko.observable(null);
            timeEnd1: KnockoutObservable<number> = ko.observable(null);
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
            
            displayCaculationTime: KnockoutObservable<boolean> = ko.observable(true);
            displayPrePostFlg: KnockoutObservable<boolean> = ko.observable(true); 
            displayBonusTime: KnockoutObservable<boolean> = ko.observable(false);
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
            overtimeHoursReference: KnockoutObservableArray<common.AppOvertimePre> = ko.observableArray([]);
            overTimeShiftNightRefer: KnockoutObservable<string> = ko.observable(null);
            flexExessTimeRefer: KnockoutObservable<string> = ko.observable(null);
            //　初期起動時、計算フラグ=1とする。
            calculateFlag: KnockoutObservable<number> = ko.observable(0);
            version: number = 0;
            preWorkContent: common.WorkContent;
            allPreAppPanelFlg: KnockoutObservable<boolean> = ko.observable(false);
            overtimeAtr: KnockoutObservable<number> = ko.observable(null);
            heightOvertimeHours: KnockoutObservable<number> = ko.observable(null);
            //画面モード(表示/編集)
            editable: KnockoutObservable<boolean> = ko.observable( true );
            enableOvertimeInput: KnockoutObservable<boolean> = ko.observable(false);
            appCur: any = null;
            appOvertimeNightFlg: KnockoutObservable<boolean> = ko.observable(true);
            flexFLag: KnockoutObservable<boolean> = ko.observable(true);
            performanceExcessAtr: KnockoutObservable<number> = ko.observable(0);
            preExcessDisplaySetting: KnockoutObservable<number> = ko.observable(0);
            opAppBefore: any = null;
            beforeAppStatus: boolean = true;
            actualStatus: number = 0;
            actualLst: any = [];
            overtimeSettingDataDto: any = null;
            forcePreApp: boolean = false;
            forceActual: boolean = false;
            forceOvertimeDetail: boolean = false;
            appOvertimeDetail: any = null;
            tmpOverTime: any;
            isNotAgentMode: KnockoutObservable<boolean> = ko.observable(true);
            constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata, rebind?: boolean) {
                super(listAppMetadata, currentApp);
                var self = this;
                self.appCur = currentApp;
                self.startPage(self.appID()).done(function(){
                    if(nts.uk.util.isNullOrUndefined(rebind)){
                        self.tmpOverTime = $("#fixed-overtime-hour-table").clone(true);
                        $("#fixed-overtime-hour-table").remove();
                        self.timeTableEdit(self.prePostSelected());
                        $("#fixed-overtime-hour-table").ntsFixedTable({ height: self.heightOvertimeHours() });
                        $("#fixed-table-indicate").ntsFixedTable({ height: 120 });
                        $("#fixed-table").ntsFixedTable({ height: 120 });
                        $('.nts-fixed-table.cf').first().find('.nts-fixed-body-container.ui-iggrid').css('border-left','1px solid #CCC');
                    } else {                       
                        if (rebind == true) {
                            self.tmpOverTime = $("#fixed-overtime-hour-table").clone(true);
                            for(let i = self.overtimeHours().length - 1; i > 0; i--){
                                self.tmpOverTime.children('tbody').children('tr').eq(i).remove();
                            }
                            $("#fixed-overtime-hour-table").remove();
                            self.timeTableEdit(self.prePostSelected());
                            ko.cleanNode(document.getElementById('fixed-overtime-hour-table'));
                            ko.applyBindings(self, document.getElementById('fixed-overtime-hour-table'));
                            $("#fixed-overtime-hour-table").ntsFixedTable({ height: self.heightOvertimeHours() - 23 });
                            $("#fixed-table-indicate").ntsFixedTable({ height: 96 });
                            $("#fixed-table").ntsFixedTable({ height: 96 });
                            $('.nts-fixed-table.cf').first().find('.nts-fixed-body-container.ui-iggrid').css('border-left','1px solid #CCC');  
                        }    
                    }
                });
            }
            
            timeTableEdit(value) {
                var self = this;
                if (value == 1) {
                    self.tmpOverTime.children('colgroup').children().eq(2).attr('width',110);
                    self.tmpOverTime.children('colgroup').children().eq(3).attr('width',110);

                } else if (value == 0) {
                    self.tmpOverTime.children('colgroup').children().eq(2).attr('width',0);
                    self.tmpOverTime.children('colgroup').children().eq(3).attr('width',0);
                }
                $("#overtime-container").append(self.tmpOverTime.clone(true));
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
                    self.resetForceAction();
                    self.initData(data);
                    self.timeStart1.subscribe(() => {
                        $("#inpStartTime1").trigger("validate");
                        if (nts.uk.ui.errors.hasError()){
                            return;
                        }
                        service.getByChangeTime({
                            workTypeCD: self.workTypeCd(),
                            workTimeCD: self.siftCD(),
                            startTime: self.timeStart1(),
                            endTime: self.timeEnd1()
                        }).done((timeRs) => {
                            self.setTimeZones(timeRs);    
                            self.calculateFlag(1);
                        });    
                    });
                    self.timeEnd1.subscribe(() => {
                        $("#inpEndTime1").trigger("validate");
                        if (nts.uk.ui.errors.hasError()){
                            return;
                        }
                        service.getByChangeTime({
                            workTypeCD: self.workTypeCd(),
                            workTimeCD: self.siftCD(),
                            startTime: self.timeStart1(),
                            endTime: self.timeEnd1()
                        }).done((timeRs) => {
                            self.setTimeZones(timeRs); 
                            self.calculateFlag(1);     
                        });    
                    });
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
                        nts.uk.ui.dialog.alertError({messageId : res.messageId}).then(function(){
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
                if(data.displayCaculationTime) {
                    if(_.isEmpty(data.siftTypes)) {
                        dialog.alertError({ messageId: "Msg_1568" })
                        .then(function() { 
                            nts.uk.ui.block.clear();
                        });               
                    } 
                }
                self.opAppBefore = data.opAppBefore;
                self.beforeAppStatus = data.beforeAppStatus;
                self.actualStatus = data.actualStatus;
                self.actualLst = data.actualLst;
                self.preExcessDisplaySetting(data.preExcessDisplaySetting);
                self.performanceExcessAtr(data.performanceExcessAtr);
                self.appOvertimeNightFlg(data.appOvertimeNightFlg == 1 ? true : false);
                self.flexFLag(data.flexFLag);
                self.requiredReason(data.requireAppReasonFlg);
                self.version = data.application.version;
                self.enableOvertimeInput(data.enableOvertimeInput);
                self.manualSendMailAtr(data.manualSendMailAtr);
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
                self.restTime.removeAll();
                self.overtimeHours.removeAll();
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
                    if(item.frameNo == 11){
                        if (data.appOvertimeNightFlg == 1) {
                            self.overtimeHours.push(self.createOvertimeInputInit(data.caculationTimes, item));
                        }
                    } else if (item.frameNo == 12) {
                        if (data.flexFLag) {
                            self.overtimeHours.push(self.createOvertimeInputInit(data.caculationTimes, item));
                        }
                    } else {
                        self.overtimeHours.push(self.createOvertimeInputInit(data.caculationTimes, item));
                    }
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
                self.overtimeSettingDataDto = data.overtimeSettingDataDto;
            }
            
            createOvertimeInputInit(calcLstInit, item){
                let self = this;
                let calcValue = _.find(calcLstInit, value => {
                    return item.attendanceID == value.attendanceID && item.frameNo == value.frameNo;     
                });
                let frameName = item.frameName;
                if(item.frameNo==11){
                    frameName = nts.uk.resource.getText("KAF005_63");    
                }
                if(item.frameNo==12){
                    frameName = nts.uk.resource.getText("KAF005_65");     
                }
                let color = '';
                if(nts.uk.util.isNullOrUndefined(calcValue.applicationTime)){
                    if(self.editable()&& self.enableOvertimeInput()){
                        color = 'transparent';
                    } else {
                        color = '#ebebe4';
                    }  
                } else {
                    color = self.getColorInit(item.attendanceID, item.frameNo, calcValue.errorCode, calcValue.preAppExceedState, calcValue.actualExceedState);            
                }
                return new common.OvertimeCaculation(
                        item.companyID, 
                        item.appID, 
                        item.attendanceID, 
                        "", 
                        item.frameNo, 
                        item.timeItemTypeAtr, 
                        frameName, 
                        calcValue.applicationTime, 
                        _.isEmpty(calcValue.preAppTime) ? null : self.convertIntToTime(parseInt(calcValue.preAppTime)), 
                        _.isEmpty(calcValue.caculationTime) ? null : self.convertIntToTime(parseInt(calcValue.caculationTime)),
                        "",
                        "", 
                        color);        
            }
            
            getColorInit(attendanceId, frameNo,errorCode, beforeAppStatus, actualStatus){
                let self = this;
                if((self.prePostSelected() == 1) && (self.performanceExcessAtr() == 2) &&(errorCode == 4||actualStatus==true)){
                    return '#FD4D4D';
                }
                if((self.prePostSelected() == 1) && (self.performanceExcessAtr() == 1) &&(errorCode == 3||actualStatus==true)){
                    return '#F6F636';
                }
                if((self.prePostSelected() == 1) && (self.preExcessDisplaySetting()==1) &&(errorCode == 2||beforeAppStatus==true)){
                    return '#FFC0CB';
                }
                if(errorCode == 1){
                    return '#F69164';
                }
                if(self.editable()&& self.enableOvertimeInput()){
                    return 'transparent';    
                } else {
                    return '#ebebe4';
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
                let times = [];
                if (timeZones) {
                    for (let i = 1; i < 11; i++) {
                        times.push(new common.OverTimeInput("", "", 0, "", i, 0, i, self.getStartTime(timeZones[i - 1]), self.getEndTime(timeZones[i - 1]), null, ""));
                    }
                }
                self.restTime(times);
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
                        .ntsError('set', { messageId: 'MsgB_1', messageParams: [self.getValueOfNameId(item.nameID())] });
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
                    appApprovalPhaseCmds: self.approvalList,
                    workTypeCode: self.workTypeCd(),
                    siftTypeCode: self.siftCD(),
                    workClockFrom1: self.timeStart1(),
                    workClockTo1: self.timeEnd1(),
                    overtimeHours: ko.mapping.toJS(_.map(self.overtimeHours(), item => self.convertOvertimeCaculationToOverTimeInput(item))),
                    restTime: ko.mapping.toJS(self.restTime()),
                    overTimeShiftNight: overTimeShiftNightTmp == null ? null : overTimeShiftNightTmp,
                    flexExessTime: flexExessTimeTmp == null ? null : flexExessTimeTmp,
                    overtimeAtr: self.overtimeAtr(),
                    sendMail: self.manualSendMailAtr(),
                    calculateFlag: self.calculateFlag(),
                    user: self.user,
                    reflectPerState: self.reflectPerState,
                    opAppBefore: self.opAppBefore,
                    beforeAppStatus: self.beforeAppStatus,
                    actualStatus: self.actualStatus,
                    actualLst: self.actualLst,
                    overtimeSettingDataDto: self.overtimeSettingDataDto
                }
                let newOvertimes = ko.toJSON(self.overtimeHours());
                let oldOvertimes = ko.toJSON(self.overtimeHoursOld);
                if(newOvertimes.localeCompare(oldOvertimes)) {
                    self.resetForceAction();    
                }  
                self.beforeUpdateColorConfirm(command);
                
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
                let comboBoxReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
                let textAreaReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true);
                if(_.isEmpty(comboBoxReason)) {
                    if(!appcommon.CommonProcess.checklenghtReason(textAreaReason,"#appReason")){
                        return;
                    }          
                } else {
                    if(!appcommon.CommonProcess.checklenghtReason(comboBoxReason+":"+textAreaReason,"#appReason")){
                        return;
                    }    
                }
                let comboDivergenceReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason2(), self.reasonCombo2(), self.displayDivergenceReasonForm());
                let areaDivergenceReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent2(), self.displayDivergenceReasonInput(), true);
                if(_.isEmpty(comboDivergenceReason)) {
                    if(!appcommon.CommonProcess.checklenghtReason(areaDivergenceReason,"#divergenceReason")){
                        return;
                    }          
                } else {
                    if(!appcommon.CommonProcess.checklenghtReason(comboDivergenceReason+":"+areaDivergenceReason,"#divergenceReason")){
                        return;
                    } 
                }
                command.applicationReason = textAreaReason;
                command.divergenceReasonContent = comboDivergenceReason;
                command.appReasonID = comboBoxReason;
                command.divergenceReasonArea = areaDivergenceReason;
                command.appOvertimeDetail = self.appOvertimeDetail;
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
                                endTimeRests: nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(), x=>{return x.endTime()}),
                                restTimeDisFlg: self.restTimeDisFlg(),
                                overtimeSettingDataDto: self.overtimeSettingDataDto
                            }
                        ).done(data => {
                            $("#inpStartTime1").ntsError("clear"); 
                            $("#inpEndTime1").ntsError("clear");
                            self.timeStart1(data.startTime1 == null ? null : data.startTime1);
                            self.timeEnd1(data.endTime1 == null ? null : data.endTime1);
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
                if(nts.uk.util.isNullOrEmpty(self.appDate())){
                    dialog.alertError({messageId : "Msg_959"});
                    return;    
                }
                $(".overtimeHoursCheck").ntsError('clear');
                $("#inpStartTime1").trigger("validate");
                $("#inpEndTime1").trigger("validate");
                if (nts.uk.ui.errors.hasError()){
                    return;
                }
                if(!self.validateTime(self.timeStart1(), self.timeEnd1(), '#inpStartTime1')){
                    return;    
                }
                nts.uk.ui.block.invisible();    
                let overtimeInputLst = [];
                _.forEach(ko.toJS(self.overtimeHours()), (item) => {
                    overtimeInputLst.push(self.initCalculateData(item));        
                });
                //計算をクリック
                // self.calculatorColorConfirm(param);
                let param1 = {
                    employeeID: self.employeeID(),
                    appDate: moment(self.appDate()).format("YYYY/MM/DD"),
                    prePostAtr: self.prePostSelected(),
                    workTypeCD: self.workTypeCd(),
                    workTimeCD: self.siftCD(),
                    overtimeInputLst: overtimeInputLst,
                    startTime: nts.uk.util.isNullOrEmpty(self.timeStart1()) ? null : self.timeStart1(),
                    endTime: nts.uk.util.isNullOrEmpty(self.timeEnd1()) ? null : self.timeEnd1(),
                    startTimeRests: nts.uk.util.isNullOrEmpty(self.restTime()) ? [] : _.map(self.restTime(),x=>{return x.startTime()}),
                    endTimeRests: nts.uk.util.isNullOrEmpty(self.restTime()) ? [] : _.map(self.restTime(),x=>{return x.endTime()}),
                    opAppBefore: self.opAppBefore,
                    beforeAppStatus: self.beforeAppStatus,
                    actualStatus: self.actualStatus,
                    actualLst: self.actualLst,
                    overtimeSettingDataDto: self.overtimeSettingDataDto       
                }
                //setting work content
                self.preWorkContent = {
                    applicationDate: self.appDate(),
                    workType: self.workTypeCd(),
                    siftType: self.siftCD(),
                    workClockFrom1: self.timeStart1(),
                    workClockTo1: self.timeEnd1(),
                    overtimeHours:  ko.toJS(self.overtimeHours())
                }
                service.getCalculateValue(param1).done((data: any) => {
                    self.resetForceAction(); 
                    self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                    self.fillColor(data);
                    nts.uk.ui.block.clear();
                    if(!self.isEmptyOverTimeInput(ko.toJS(self.overtimeHours()))){
                        self.calculateFlag(0);
                    }
                    self.checkWorkContentChanged();
                });
            }
            
            fillColor(calcData){
                let self = this;
                let beforeAppStatus = calcData.beforeAppStatus;
                let actualStatus = calcData.actualStatus;
                let resultLst = calcData.resultLst;
                _.forEach(self.overtimeHours(), overtimeHour => {
                    let calcOT = _.find(resultLst, item => {
                        return item.attendanceID == 1 &&
                            item.frameNo == overtimeHour.frameNo();    
                    });          
                    if(!nts.uk.util.isNullOrUndefined(calcOT)){
                        overtimeHour.applicationTime(calcOT.appTime);
                        overtimeHour.preAppTime(nts.uk.util.isNullOrUndefined(calcOT.preAppTime) ? null : nts.uk.time.format.byId("Time_Short_HM", calcOT.preAppTime));
                        overtimeHour.caculationTime(nts.uk.util.isNullOrUndefined(calcOT.actualTime) ? null : nts.uk.time.format.byId("Time_Short_HM", calcOT.actualTime));
                        if(nts.uk.util.isNullOrUndefined(overtimeHour.applicationTime())){
                            if(self.editable()&& self.enableOvertimeInput()){
                                $('td#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', 'transparent');
                                $('input#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', 'transparent');
                                overtimeHour.color('transparent');
                                return; 
                            } else {
                                $('td#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', '#ebebe4');
                                $('input#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', '#ebebe4');
                                overtimeHour.color('#ebebe4');
                                return; 
                            }  
                        }
                        let oldValue = _.find(self.overtimeHoursOld, item => {
                            return item.attendanceID == 1 &&
                                item.frameNo == overtimeHour.frameNo();    
                        });  
                        let calcChange = false;
                        if((nts.uk.util.isNullOrUndefined(oldValue)) ||
                            (nts.uk.util.isNullOrUndefined(oldValue.applicationTime)) || 
                            (ko.toJSON(oldValue).localeCompare(ko.toJSON(overtimeHour))!=0)){
                            calcChange = true;
                        }
                        let newColor = self.changeColor(1, overtimeHour.frameNo(), self.getErrorCode(calcOT.calcError, calcOT.preAppError, calcOT.actualError), beforeAppStatus, actualStatus, calcChange);
                        if(!nts.uk.util.isNullOrUndefined(newColor)){
                            overtimeHour.color(newColor);
                        }
                    } else {
                        if(nts.uk.util.isNullOrUndefined(overtimeHour.applicationTime())){
                            if(self.editable()&& self.enableOvertimeInput()){
                                $('td#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', 'transparent');
                                $('input#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', 'transparent');
                                overtimeHour.color('transparent');
                                return; 
                            } else {
                                $('td#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', '#ebebe4');
                                $('input#overtimeHoursCheck_'+overtimeHour.attendanceID()+'_'+overtimeHour.frameNo()).css('background', '#ebebe4');
                                overtimeHour.color('#ebebe4');
                                return; 
                            }  
                        }
                        let newColor = self.changeColor(1, overtimeHour.frameNo(), self.getErrorCode(0, 0, 0), beforeAppStatus, actualStatus, false);
                        if(!nts.uk.util.isNullOrUndefined(newColor)){
                            overtimeHour.color(newColor);
                        }      
                    }
                });         
            }
            
            convertAppOvertimeReferDto(data :any){
                let self = this;
                if(data.appOvertimeReference != null && !nts.uk.util.isNullOrEmpty(self.appDate())) {
                    self.appDateReference(data.appOvertimeReference.appDateRefer);
                    if(data.appOvertimeReference.workTypeRefer != null){
                        self.workTypeCodeReference(data.appOvertimeReference.workTypeRefer.workTypeCode);
                        self.workTypeNameReference(self.getName(data.appOvertimeReference.workTypeRefer.workTypeCode, data.appOvertimeReference.workTypeRefer.workTypeName));
                    }
                    if(data.appOvertimeReference.siftTypeRefer != null){
                        self.siftCodeReference(data.appOvertimeReference.siftTypeRefer.siftCode);
                        self.siftNameReference(self.getName(data.appOvertimeReference.siftTypeRefer.siftCode, data.appOvertimeReference.siftTypeRefer.siftName));
                    }
                    self.workClockFrom1To1Reference(data.appOvertimeReference.workClockFromTo1Refer);
                    self.overtimeHoursReference.removeAll();
                    if(data.appOvertimeReference.overTimeInputsRefer != null){
                        for (let i = 0; i < data.appOvertimeReference.overTimeInputsRefer.length; i++) {
                            if(data.appOvertimeReference.overTimeInputsRefer[i].frameNo != 11 && data.appOvertimeReference.overTimeInputsRefer[i].frameNo != 12){
                                self.overtimeHoursReference.push(new common.AppOvertimePre("", "", 
                                data.appOvertimeReference.overTimeInputsRefer[i].attendanceID,
                                "", data.appOvertimeReference.overTimeInputsRefer[i].frameNo,
                                0, data.appOvertimeReference.overTimeInputsRefer[i].frameName +" : ",
                                null,
                                null,
                                data.appOvertimeReference.overTimeInputsRefer[i].applicationTime == null ? self.convertIntToTime(0) : self.convertIntToTime(data.appOvertimeReference.overTimeInputsRefer[i].applicationTime),
                                null));
                            }
                        }
                    }
                     self.overTimeShiftNightRefer(data.appOvertimeReference.overTimeShiftNightRefer == null ? self.convertIntToTime(0) : self.convertIntToTime(data.appOvertimeReference.overTimeShiftNightRefer));
                     self.flexExessTimeRefer(data.appOvertimeReference.flexExessTimeRefer == null ? self.convertIntToTime(0) : self.convertIntToTime(data.appOvertimeReference.flexExessTimeRefer));
                } else {
                    self.workTypeCodeReference("");
                    self.workTypeNameReference("");    
                    self.siftCodeReference("");
                    self.siftNameReference("");
                    self.workClockFrom1To1Reference("");
                    self.overtimeHoursReference.removeAll();
                    for (let index in self.overtimeHours()) {
                        let overtimeHour = self.overtimeHours()[index];
                        if(overtimeHour.frameNo() != 11 && overtimeHour.frameNo() != 12){
                            self.overtimeHoursReference.push(new common.AppOvertimePre("", "", 
                            overtimeHour.attendanceID(),
                            "", overtimeHour.frameNo(),
                            0, overtimeHour.frameName() +" : ",
                            null, null, null, null));
                        }
                    }
                }
            }
            
            convertIntToTime(data : number) : string{
                if(nts.uk.util.isNullOrEmpty(data)) {
                    return null;    
                }
                return nts.uk.time.format.byId("Time_Short_HM", data);
            }
            
            getErrorCode(calcError, preAppError, actualError){
                if(actualError > preAppError) {
                    if(actualError > calcError) {
                        return actualError;
                    } else {
                        return calcError;
                    }
                } else {
                    if(preAppError > calcError) {
                        return preAppError;
                    } else {
                        return calcError;
                    }
                }
            }
            
            changeColor(attendanceId, frameNo,errorCode, beforeAppStatus, actualStatus, calcChange){
                let self = this;
                if((self.prePostSelected() == 1) && (self.performanceExcessAtr() == 2) &&(errorCode == 4||actualStatus==3)){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FD4D4D');
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FD4D4D');
                    return '#FD4D4D';
                }
                if((self.prePostSelected() == 1) && (self.performanceExcessAtr() == 1) &&(errorCode == 3||actualStatus==3)){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F6F636');
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F6F636');
                    return '#F6F636';
                }
                if((self.prePostSelected() == 1) && (self.preExcessDisplaySetting()==1) &&(errorCode == 2||beforeAppStatus==true)){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FFC0CB');
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#FFC0CB');
                    return '#FFC0CB';
                }
                if(errorCode == 1 && calcChange){
                    $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F69164');
                    $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#F69164');
                    return '#F69164';
                }
                if(self.editable()&& self.enableOvertimeInput()){
                    if(calcChange){
                        $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', 'transparent');
                        $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', 'transparent');
                        return 'transparent'; 
                    }
                } else {
                    if(calcChange){
                        $('td#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#ebebe4');
                        $('input#overtimeHoursCheck_'+attendanceId+'_'+frameNo).css('background', '#ebebe4');
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
            }
            
            beforeUpdateColorConfirm(overtime: any){
                let self = this;
                if(self.forcePreApp && self.forceActual) {
                    self.beforeUpdateProcess(overtime);   
                    return;  
                }
                service.beforeRegisterColorConfirm(overtime).done((data2) => { 
                    if(nts.uk.util.isNullOrUndefined(data2.preActualColorResult)){
                        self.beforeUpdateProcess(overtime);    
                    } else {
                        self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                        self.fillColor(data2.preActualColorResult);
                        self.checkPreApp(overtime, data2);        
                    }
                }).fail((res) => {
                    dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                    .then(function() { nts.uk.ui.block.clear(); });           
                });         
            }
            
            checkPreApp(overtime, data) {
                let self = this;
                if(self.forcePreApp) {
                    self.checkActual(overtime, data);
                    return;    
                }
                let beforeAppStatus = data.preActualColorResult.beforeAppStatus;
                let actualStatus = data.preActualColorResult.actualStatus;
                let resultLst = data.preActualColorResult.resultLst;
                if(self.preExcessDisplaySetting()==0){
                    self.checkActual(overtime, data);
                    return;    
                }
                if(beforeAppStatus){
                    dialog.confirm({ messageId: "Msg_1508" }).ifYes(() => {
                        self.forcePreApp = true;
                        self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                        self.checkActual(overtime, data);   
                    }).ifNo(() => {
                        nts.uk.ui.block.clear();
                    });       
                    return; 
                }
                let preAppErrorFrames = resultLst.filter(f => 
                    f.attendanceID == 1 && 
                    self.getErrorCode(f.calcError, f.preAppError, f.actualError, beforeAppStatus, actualStatus) == 2);
                if(nts.uk.util.isNullOrEmpty(preAppErrorFrames)){
                    self.checkActual(overtime, data);         
                } else {
                    let framesError = '';
                    preAppErrorFrames.forEach((v, k)=>{
                        let currentFrame = _.find(self.overtimeHours(), ot => ot.attendanceID()==v.attendanceID && ot.frameNo()==v.frameNo);
                        if(!nts.uk.util.isNullOrUndefined(currentFrame)){
                            framesError+=currentFrame.frameName();
                            if(k<(preAppErrorFrames.length-1)){
                                framesError+=",";    
                            }    
                        }    
                    }); 
                    dialog.confirm({ messageId: "Msg_424", messageParams: [ self.employeeName(), framesError ] }).ifYes(() => {
                        self.forcePreApp = true;
                        self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                        self.checkActual(overtime, data);
                    }).ifNo(() => {
                        nts.uk.ui.block.clear();
                    }); 
                }
            }
        
            checkActual(overtime, data) {
                let self = this;
                if(self.forceActual) {
                    self.beforeUpdateProcess(overtime);
                    return;    
                }
                let beforeAppStatus = data.preActualColorResult.beforeAppStatus;
                let actualStatus = data.preActualColorResult.actualStatus;
                let resultLst = data.preActualColorResult.resultLst;
                if(self.performanceExcessAtr()==0){
                    self.beforeUpdateProcess(overtime);
                    return;        
                } 
                if(actualStatus==3){
                    if(self.performanceExcessAtr() == 2){
                        dialog.alertError({ messageId: "Msg_1565", messageParams: [ self.employeeName(), moment(self.appDate()).format(self.DATE_FORMAT), "登録できません。" ] })
                        .then(function() { 
                            nts.uk.ui.block.clear();
                        });    
                        return;
                    } else {
                        dialog.confirm({ messageId: "Msg_1565", messageParams: [ self.employeeName(), moment(self.appDate()).format(self.DATE_FORMAT), "登録してもよろしいですか？" ] }).ifYes(() => {
                            self.forceActual = true;
                            self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                            self.beforeUpdateProcess(overtime);  
                        }).ifNo(() => {
                            nts.uk.ui.block.clear();
                        }); 
                        return;        
                    }   
                }
                let actualErrorFrames = resultLst.filter(f => 
                    f.attendanceID == 1 && 
                    self.getErrorCode(f.calcError, f.preAppError, f.actualError, beforeAppStatus, actualStatus) == 4);
                let actualAlarmFrames = resultLst.filter(f => 
                    f.attendanceID == 1 && 
                    self.getErrorCode(f.calcError, f.preAppError, f.actualError, beforeAppStatus, actualStatus) == 3);
                if(!nts.uk.util.isNullOrEmpty(actualErrorFrames)){
                    let framesError = '';
                    actualErrorFrames.forEach((v, k)=>{
                        let currentError = _.find(self.overtimeHours(), ot => ot.attendanceID()==v.attendanceID&&ot.frameNo()==v.frameNo);
                        if(!nts.uk.util.isNullOrUndefined(currentError)){
                            framesError+=currentError.frameName();
                            if(k<(actualErrorFrames.length-1)){
                                framesError+=",";    
                            }    
                        }    
                    });  
                    dialog.alertError({ messageId: "Msg_423", messageParams: [ self.employeeName(), framesError, "登録できません。" ] })
                    .then(function() { 
                        nts.uk.ui.block.clear();
                    }); 
                    return;
                } else if(!nts.uk.util.isNullOrEmpty(actualAlarmFrames)){
                    let framesAlarm = '';
                    actualAlarmFrames.forEach((v, k)=>{
                        let currentAlarm = _.find(self.overtimeHours(), ot => ot.attendanceID()==v.attendanceID&&ot.frameNo()==v.frameNo);
                        if(!nts.uk.util.isNullOrUndefined(currentAlarm)){
                            framesAlarm+=currentAlarm.frameName();
                            if(k<(actualAlarmFrames.length-1)){
                                framesAlarm+=",";    
                            }    
                        }    
                    }); 
                    dialog.confirm({ messageId: "Msg_423", messageParams: [ self.employeeName(), framesAlarm, "登録してもよろしいですか？" ] }).ifYes(() => {
                        self.forceActual = true;
                        self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                        self.beforeUpdateProcess(overtime);
                    }).ifNo(() => {
                        nts.uk.ui.block.clear();
                    }); 
                    return;
                } else {
                    self.beforeUpdateProcess(overtime);    
                }
            }
            
            beforeUpdateProcess(overtime: any){
                let self = this;
                self.forcePreApp = true;
                self.forceActual = true;
                if(self.forceOvertimeDetail) {
                    self.updateOvertime(overtime);
                    return;       
                }
                service.checkBeforeUpdate(overtime).done((data) => {    
                    self.forceOvertimeDetail = true;    
                    self.appOvertimeDetail = data.appOvertimeDetail;
                    self.updateOvertime(overtime);
                }).fail((res) => {
                    if(nts.uk.util.isNullOrEmpty(res.errors)){
                        dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                        .then(function() { nts.uk.ui.block.clear(); });       
                    } else {
                        let errors = res.errors;
                        for(let i = 0; i < errors.length; i++){
                            let error = errors[i];
                            if(error.messageId=="Msg_1538"){
                                error.parameterIds = [
                                    nts.uk.time.formatYearMonth(parseInt(error.parameterIds[4])), 
                                    nts.uk.time.formatYearMonth(parseInt(error.parameterIds[5])),
                                    nts.uk.time.format.byId("Clock_Short_HM", parseInt(error.parameterIds[6])), 
                                    nts.uk.time.format.byId("Clock_Short_HM", parseInt(error.parameterIds[7]))
                                ];     
                            } else {
                                error.parameterIds = [
                                    nts.uk.time.format.byId("Clock_Short_HM", parseInt(error.parameterIds[4])), 
                                    nts.uk.time.format.byId("Clock_Short_HM", parseInt(error.parameterIds[5]))
                                ];     
                            }
                            error.message = nts.uk.resource.getMessage(error.messageId, error.parameterIds);
                        }
                        nts.uk.ui.dialog.bundledErrors({ errors: errors })    
                        .then(function() { nts.uk.ui.block.clear(); });          
                    }           
                });        
            }
            
            resetForceAction() {
                let self = this;
                self.forcePreApp = false;
                self.forceActual = false;
                self.forceOvertimeDetail = false;    
            }
        }
    }
}