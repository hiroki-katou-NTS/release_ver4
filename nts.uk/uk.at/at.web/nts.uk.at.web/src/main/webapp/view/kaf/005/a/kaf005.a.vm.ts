module nts.uk.at.view.kaf005.a.viewmodel {
    import common = nts.uk.at.view.kaf005.share.common;
    import service = nts.uk.at.view.kaf005.shr.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import setShared = nts.uk.ui.windows.setShared;
    import util = nts.uk.util;
    import text = nts.uk.resource.getText;
    import showError = nts.uk.at.view.kaf000.shr.model.CommonProcess.showError;

    export class ScreenModel {
        
        screenModeNew: KnockoutObservable<boolean> = ko.observable(true);
        
        DATE_FORMAT: string = "YYYY/MM/DD";
        //kaf000
        kaf000_a: kaf000.a.viewmodel.ScreenModel;
        
        checkBoxValue: KnockoutObservable<boolean> = ko.observable(false);
        enableSendMail: KnockoutObservable<boolean> = ko.observable(false);
        //申請者
        employeeName: KnockoutObservable<string> = ko.observable("");
        typeSiftVisible: KnockoutObservable<boolean> = ko.observable(true);
        prePostSelected: KnockoutObservable<number> = ko.observable(0);
        // 申請日付
        appDate: KnockoutObservable<string> = ko.observable('');
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
        //申請理由が必須
        requiredReason: KnockoutObservable<boolean> = ko.observable(false);
        multilContent: KnockoutObservable<string> = ko.observable('');
        //comboBox 定型理由
        reasonCombo2: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
        selectedReason2: KnockoutObservable<string> = ko.observable('');
        //MultilineEditor
        requiredReason2: KnockoutObservable<boolean> = ko.observable(false);
        multilContent2: KnockoutObservable<string> = ko.observable('');
        
        employeeID: KnockoutObservable<string> = ko.observable('');
        employeeIDs: KnockoutObservableArray<string> = ko.observableArray([]);
        employeeList :KnockoutObservableArray<common.EmployeeOT> = ko.observableArray([]);
        selectedEmplCodes: KnockoutObservable<string> = ko.observable(null);
        employeeFlag: KnockoutObservable<boolean> = ko.observable(false);
        totalEmployee: KnockoutObservable<string> = ko.observable(null);
        heightOvertimeHours: KnockoutObservable<number> = ko.observable(null);
        
        overtimeAtr: KnockoutObservable<number> = ko.observable(null);
        //休出時間
        restTime: KnockoutObservableArray<common.OverTimeInput> = ko.observableArray([]);
        //残業時間
        overtimeHours: KnockoutObservableArray<common.OvertimeCaculation> = ko.observableArray([]);
        
        overtimeHoursOld: Array<common.OvertimeCaculation> = [];
        //menu-bar 
        prePostDisp: KnockoutObservable<boolean> = ko.observable(true);
        prePostEnable: KnockoutObservable<boolean> = ko.observable(true);

        displayBonusTime: KnockoutObservable<boolean> = ko.observable(false);
        displayCaculationTime: KnockoutObservable<boolean> = ko.observable(false);
        displayPrePostFlg: KnockoutObservable<boolean> = ko.observable(false);
        displayRestTime: KnockoutObservable<boolean> = ko.observable(false);
        restTimeDisFlg: KnockoutObservable<boolean> = ko.observable(false); // RequestAppDetailSetting 
        typicalReasonDisplayFlg: KnockoutObservable<boolean> = ko.observable(false);
        displayAppReasonContentFlg: KnockoutObservable<boolean> = ko.observable(false);
        displayDivergenceReasonForm: KnockoutObservable<boolean> = ko.observable(false);
        displayDivergenceReasonInput: KnockoutObservable<boolean> = ko.observable(false);
        
        // 参照
        referencePanelFlg: KnockoutObservable<boolean> = ko.observable(false);
        preAppPanelFlg: KnockoutObservable<boolean> = ko.observable(false);
        allPreAppPanelFlg: KnockoutObservable<boolean> = ko.observable(false);
        isRightContent: KnockoutObservable<boolean> = ko.observable(false);
        performanceDisplayAtr: KnockoutObservable<boolean> = ko.observable(false);
        preDisplayAtr: KnockoutObservable<boolean> = ko.observable(false);
        workTypeChangeFlg: KnockoutObservable<boolean> = ko.observable(false);

        overtimeWork: KnockoutObservableArray<common.OvertimeWork> = ko.observableArray([
            new common.OvertimeWork("",0,0,0,0,"",""),
            new common.OvertimeWork("",0,0,0,0,"",""),    
        ]);
        indicationOvertimeFlg: KnockoutObservable<boolean> = ko.observable(true);
        

        // preAppOvertime
        appDatePre: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));
        workTypeCodePre:  KnockoutObservable<string> = ko.observable("");
        workTypeNamePre:  KnockoutObservable<string> = ko.observable("");
        siftCodePre:  KnockoutObservable<string> = ko.observable("");
        siftNamePre:  KnockoutObservable<string> = ko.observable("");
        //TIME LINE 1
        workClockFrom1To1Pre: KnockoutObservable<string> = ko.observable(null);
        overtimeHoursPre: KnockoutObservableArray<common.AppOvertimePre> = ko.observableArray([]);
        overTimeShiftNightPre: KnockoutObservable<string> = ko.observable(null);
        flexExessTimePre: KnockoutObservable<string> = ko.observable(null);
        
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
        calculateFlag: KnockoutObservable<number> = ko.observable(1);
        uiType: KnockoutObservable<number> = ko.observable(0);
        preWorkContent: common.WorkContent;
        targetDate: any = moment(new Date()).format(this.DATE_FORMAT); 
        //画面モード(表示/編集)
        editable: KnockoutObservable<boolean> = ko.observable(true);
        enableOvertimeInput: KnockoutObservable<boolean> = ko.observable(false);
        isSpr: boolean = false;
        appOvertimeNightFlg: KnockoutObservable<boolean> = ko.observable(true);
        flexFLag: KnockoutObservable<boolean> = ko.observable(true);
        performanceExcessAtr: KnockoutObservable<number> = ko.observable(0);
        preExcessDisplaySetting: KnockoutObservable<number> = ko.observable(0);
        callServiceChangePrePost: boolean = true;
        opAppBefore: any = null;
        beforeAppStatus: boolean = true;
        actualStatus: number = 0;
        actualLst: any = [];
        resultLst: any = [];
        tmpOverTime: any;
        overtimeSettingDataDto: any;
        forceYearConfirm: boolean = false;
        forcePreApp: boolean = false;
        forceActual: boolean = false;
        forceOvertimeDetail: boolean = false;
        forceInconsistency: boolean = false;
        appOvertimeDetail: any = null;
        isNotAgentMode: KnockoutObservable<boolean> = ko.observable(true);
        constructor(transferData :any) {
            let self = this;
            if(transferData != null){
                self.timeStart1(transferData.startTime);
                self.timeEnd1(transferData.endTime);
                self.appDate(transferData.appDate);
                self.multilContent(transferData.applicationReason);
                self.employeeIDs(transferData.employeeIDs);
                self.employeeID(transferData.employeeID); 
                self.uiType(transferData.uiType); 
                if(!nts.uk.util.isNullOrUndefined(transferData.appDate)){
                    self.isSpr = true;
                    self.targetDate = transferData.appDate;        
                }
                if(_.isEmpty(transferData.employeeIDs)) {
                    self.isNotAgentMode(true);    
                } else {
                    if(transferData.employeeIDs.length > 1) {
                        self.isNotAgentMode(false);         
                    } else {
                        self.isNotAgentMode(true);     
                    }    
                }
            }
                    
            //KAF000_A
            self.kaf000_a = new kaf000.a.viewmodel.ScreenModel();
            //startPage 005a AFTER start 000_A
            self.startPage().done(function() {
                self.kaf000_a.start(self.employeeID(), 1, 0, self.targetDate, self.overtimeAtr()).done(function() {
                    self.tmpOverTime = $("#fixed-overtime-hour-table").clone(true);
                    for(let i = self.overtimeHours().length - 1; i > 0; i--){
                        self.tmpOverTime.children('tbody').children('tr').eq(i).remove();
                    }
                    $("#fixed-overtime-hour-table").remove();
                    self.timeTableEdit(self.prePostSelected());
                    ko.cleanNode(document.getElementById('fixed-overtime-hour-table'));
                    ko.applyBindings(self, document.getElementById('fixed-overtime-hour-table'));
                                   
                    $("#fixed-table").ntsFixedTable({ height: 120 });
                    $("#fixed-overtime-hour-table").ntsFixedTable({ height: self.heightOvertimeHours() });
                    $("#fixed-table-indicate").ntsFixedTable({ height: 120 });
                    $('.nts-fixed-table.cf').first().find('.nts-fixed-body-container.ui-iggrid').css('border-left','1px solid #CCC')
                })
            })

        }
        /**
         * 
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            let url = $(location).attr('search');
            let urlParam :string = url == "" ? "0" : url.split("=")[1].trim();
            nts.uk.ui.block.invisible();
            service.getOvertimeByUI({
                url: urlParam == '' ? 0 : urlParam,
                appDate: nts.uk.util.isNullOrEmpty(self.appDate()) ? null : moment(self.appDate()).format(self.DATE_FORMAT),
                uiType: self.uiType(),
                timeStart1: self.timeStart1(),
                timeEnd1: self.timeEnd1(),
                reasonContent: self.multilContent(),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                employeeIDs: self.employeeIDs()
            }).done((data) => {
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
                // $("#inputdate").focus();
                 // findByChangeAppDate
                self.appDate.subscribe(function(value){
                    var dfd = $.Deferred();
                    if(!nts.uk.util.isNullOrEmpty(value)){
                        nts.uk.ui.errors.clearAll();
                        $("#inputdate").trigger("validate");
                        if (nts.uk.ui.errors.hasError()) {
                            return;
                        }
                        nts.uk.ui.block.invisible();
                        service.findByChangeAppDate({
                            appDate: moment(value).format(self.DATE_FORMAT),
                            prePostAtr: self.prePostSelected(),
                            siftCD: self.siftCD(),
                            overtimeHours: _.map(ko.toJS(self.overtimeHours()), item => {return self.initCalculateData(item);}),
                            workTypeCode: self.workTypeCd(),
                            startTimeRests: nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(), x=>{return x.startTime()}),
                            endTimeRests:nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(), x=>{return x.endTime()}),
                            overtimeAtr: self.overtimeAtr(),
                            changeEmployee: nts.uk.util.isNullOrEmpty(self.employeeList()) ? null : self.employeeList()[0].id
                        }).done((data) =>{
                            self.resetForceAction();
                            self.findBychangeAppDateData(data);
                            self.kaf000_a.getAppDataDate(0, moment(value).format(self.DATE_FORMAT), false,nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(), self.overtimeAtr());
                            self.convertAppOvertimeReferDto(data);
                            nts.uk.ui.block.clear(); 
                            dfd.resolve(data);
                        }).fail((res) =>{
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
                        });
                        //Check calculate times
                        if(!nts.uk.util.isNullOrEmpty(self.preWorkContent)){
                            if(self.preWorkContent.applicationDate != value){
                                //→エラーＭＳＧ
                                self.calculateFlag(1);
                            }
                        }
                        if (self.prePostSelected() == 0) {
                            self.callServiceChangePrePost = true;
                        }
                    }
                    return dfd.promise();
                    });
                self.prePostSelected.subscribe(function(value) {
                    $('#kaf005-pre-post-select').ntsError('clear');
                    self.clearErrorA6_8();
                    
                    $("#fixed-overtime-hour-table").parents('div').eq(2).remove();
                    self.timeTableEdit(value);
                    ko.cleanNode(document.getElementById('fixed-overtime-hour-table'));
                    ko.applyBindings(self, document.getElementById('fixed-overtime-hour-table'));
                    $("#fixed-overtime-hour-table").ntsFixedTable({ height: self.heightOvertimeHours() });

                    nts.uk.ui.errors.clearAll();
                    $("#inputdate").trigger("validate");
                    if (nts.uk.ui.errors.hasError()) {
                        if (value == 0) {
                            self.isRightContent(false);
                        }
                        if (value == 1 && self.performanceDisplayAtr()) {
                            self.isRightContent(true);
                            self.referencePanelFlg(true);
                        }
                        if (value == 1 && self.preDisplayAtr()) {
                            self.isRightContent(true);
                            self.allPreAppPanelFlg(true);
                        }
                        return;
                    }
                    
                    if (value == 0) {
                        self.isRightContent(false);
                    } else {
                        self.isRightContent(self.allPreAppPanelFlg || self.referencePanelFlg);
                    }
                    service.checkConvertPrePost({
                        prePostAtr: value,
                        appDate: nts.uk.util.isNullOrEmpty(self.appDate()) ? null : moment(self.appDate()).format(self.DATE_FORMAT),
                        siftCD: self.siftCD(),
                        overtimeHours: _.map(ko.toJS(self.overtimeHours()), item => { return self.initCalculateData(item); }),
                        workTypeCode: self.workTypeCd(),
                        startTimeRests: nts.uk.util.isNullOrEmpty(self.restTime()) ? [] : _.map(self.restTime(), x => { return x.startTime() }),
                        endTimeRests: nts.uk.util.isNullOrEmpty(self.restTime()) ? [] : _.map(self.restTime(), x => { return x.endTime() }),
                        startTime: nts.uk.util.isNullOrEmpty(self.timeStart1()) ? null : self.timeStart1(),
                        endTime: nts.uk.util.isNullOrEmpty(self.timeEnd1()) ? null : self.timeEnd1(),
                        overtimeSettingDataDto: self.overtimeSettingDataDto,
                        opAppBefore: self.opAppBefore,
                        beforeAppStatus: self.beforeAppStatus,
                        actualStatus: self.actualStatus,
                        actualLst: self.actualLst
                    }).done((data) => {
                        self.resetForceAction();
                        self.opAppBefore = data.opAppBefore;
                        self.preAppOvertimeDto = data.preAppOvertimeDto;
                        self.beforeAppStatus = data.beforeAppStatus;
                        self.actualStatus = data.actualStatus;
                        self.actualLst = data.actualLst;
                        self.resultLst = data.resultLst;
                        self.fillColor(data);
                        self.convertpreAppOvertimeDto(data);
                        self.convertAppOvertimeReferDto(data);
                        self.referencePanelFlg(data.referencePanelFlg);
                        self.allPreAppPanelFlg(data.allPreAppPanelFlg);
                        self.preAppPanelFlg(data.preAppPanelFlg);
                        self.isRightContent(data.allPreAppPanelFlg || data.referencePanelFlg);
                        $("#fixed-overtime-hour-table").parents('div').eq(2).remove();
                        self.timeTableEdit(value);
                        ko.cleanNode(document.getElementById('fixed-overtime-hour-table'));
                        ko.applyBindings(self, document.getElementById('fixed-overtime-hour-table'));
                        $("#fixed-overtime-hour-table").ntsFixedTable({ height: self.heightOvertimeHours() });
                    }).fail((res) => {
                        dfd.reject(res);
                    });
                });
                
                //Check work content Changed
                self.checkWorkContentChanged();
                
                dfd.resolve(data);
                nts.uk.ui.block.clear();
            }).fail((res) => {
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
            });
            return dfd.promise();
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
        
        isShowReason(){
            let self =this;
            if(self.screenModeNew()){
                    return self.displayAppReasonContentFlg();
                }else{
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
            self.preExcessDisplaySetting(data.preExcessDisplaySetting);
            self.performanceExcessAtr(data.performanceExcessAtr);
            self.appOvertimeNightFlg(data.appOvertimeNightFlg == 1 ? true : false);
            self.flexFLag(data.flexFLag);
            self.requiredReason(data.requireAppReasonFlg);
            self.enableOvertimeInput(data.enableOvertimeInput);
            self.checkBoxValue(!data.manualSendMailAtr);
            self.enableSendMail(!data.sendMailWhenRegisterFlg);
            self.displayPrePostFlg(data.displayPrePostFlg ? true : false);
            self.prePostSelected(data.application.prePostAtr);
            self.displayCaculationTime(data.displayCaculationTime);
            self.typicalReasonDisplayFlg(data.typicalReasonDisplayFlg);
            self.displayAppReasonContentFlg(data.displayAppReasonContentFlg);
            self.displayDivergenceReasonForm(data.displayDivergenceReasonForm);
            self.displayDivergenceReasonInput(data.displayDivergenceReasonInput);
            self.displayBonusTime(data.displayBonusTime);
            self.restTimeDisFlg(data.displayRestTime);
            self.employeeName(data.employeeName);
            self.employeeID(data.employeeID);
            if (data.siftType != null) {
                self.siftCD(data.siftType.siftCode);
                self.siftName(self.getName(data.siftType.siftCode, data.siftType.siftName));
            }
            if (data.workType != null) {
                self.workTypeCd(data.workType.workTypeCode);
                self.workTypeName(self.getName(data.workType.workTypeCode,data.workType.workTypeName));
            }
            self.workTypecodes(data.workTypes);
            self.workTimecodes(data.siftTypes);
            self.timeStart1(data.workClockFrom1 == null ? null : data.workClockFrom1);
            self.timeEnd1(data.workClockTo1 == null ? null : data.workClockTo1);
            if(data.applicationReasonDtos != null && data.applicationReasonDtos.length > 0){
                let lstReasonCombo = _.map(data.applicationReasonDtos, o => { return new common.ComboReason(o.reasonID, o.reasonTemp); });
                self.reasonCombo(lstReasonCombo);
                let reasonID = _.find(data.applicationReasonDtos, o => { return o.defaultFlg == 1 }).reasonID;
                self.selectedReason(reasonID);
                
                self.multilContent(data.application.applicationReason);
            } 
            
            if(data.divergenceReasonDtos != null && data.divergenceReasonDtos.length > 0){
                self.reasonCombo2(_.map(data.divergenceReasonDtos, o => { return new common.ComboReason(o.divergenceReasonID, o.reasonTemp); }));
                let defaultID = _.find(data.divergenceReasonDtos, o => { return o.divergenceReasonIdDefault == 1 })
                let reasonID = "";
                if(!nts.uk.util.isNullOrUndefined(defaultID)){
                    reasonID = defaultID;         
                }
                self.selectedReason2(reasonID);
                self.multilContent2(data.divergenceReasonContent); 
            }
            
            self.referencePanelFlg(data.referencePanelFlg);
            self.preAppPanelFlg(data.preAppPanelFlg);
            self.prePostEnable(data.prePostCanChangeFlg);
            self.allPreAppPanelFlg(data.allPreAppPanelFlg);
            self.indicationOvertimeFlg(data.extratimeDisplayFlag);
            if(nts.uk.util.isNullOrUndefined(data.agreementTimeDto)){
                self.indicationOvertimeFlg(false);    
            } else {
                common.Process.setOvertimeWork(data.agreementTimeDto, self);    
            }
            self.isRightContent(data.allPreAppPanelFlg || data.referencePanelFlg);
            self.preDisplayAtr(data.preDisplayAtr);
            self.performanceDisplayAtr(data.performanceDisplayAtr);
            self.workTypeChangeFlg(data.workTypeChangeFlg);
            self.opAppBefore = data.opAppBefore;
            self.beforeAppStatus = data.beforeAppStatus;
            self.actualStatus = data.actualStatus;
            self.actualLst = data.actualLst;
            // list employeeID
            if(!nts.uk.util.isNullOrEmpty(data.employees)){
                self.employeeFlag(true);
                for(let i= 0; i < data.employees.length; i++){
                    self.employeeList.push(new common.EmployeeOT(data.employees[i].employeeIDs,data.employees[i].employeeName));
                }
                let total = data.employees.length;
                self.totalEmployee(nts.uk.resource.getText("KAF005_184",total.toString()));
            }
            // 休憩時間
            self.setTimeZones(data.timezones);
            // 残業時間
            if (!data.resultCaculationTimeFlg) {
                if (data.overTimeInputs != null) {
                    for (let i = 0; i < data.overTimeInputs.length; i++) {
                        if (data.overTimeInputs[i].attendanceID == 1) {
                            self.overtimeHours.push(new common.OvertimeCaculation("", "", data.overTimeInputs[i].attendanceID, "", data.overTimeInputs[i].frameNo, 0, data.overTimeInputs[i].frameName, null, null, null, "#[KAF005_55]", "", ""));
                        }
                    }
                }
                //
                if (data.appOvertimeNightFlg == 1) {
                    self.overtimeHours.push(new common.OvertimeCaculation("", "", 1, "", 11, 0, nts.uk.resource.getText("KAF005_63"), null, null, null, "#[KAF005_64]", "", ""));
                }
                if (data.flexFLag) {
                    self.overtimeHours.push(new common.OvertimeCaculation("", "", 1, "", 12, 0, nts.uk.resource.getText("KAF005_65"), null, null, null, "#[KAF005_66]", "", ""));
                }
            }else{
                let dataOverTime = _.filter(data.caculationTimes, {'attendanceID': 1});
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
                //勤務内容を変更後に計算ボタン押下。計算フラグ=0にする。 
                if(!self.isEmptyOverTimeInput(ko.toJS(self.overtimeHours()))){
                    self.calculateFlag(0);
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
                 //Check work content Changed
                self.checkWorkContentChanged(); 
            }
            
            // preAppOvertime
            self.convertpreAppOvertimeDto(data);
            self.convertAppOvertimeReferDto(data);
            
            self.overtimeAtr(data.overtimeAtr);
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
        
        getStartTime(data) {
            return data ? data.start : null;
        }

        getEndTime(data) {
            return data ? data.end : null;
        }

        checkRequiredOvertimeHours() {
            let self = this;
            _.each(self.overtimeHours(), function(item) {
                item.applicationTime.subscribe(function(value) {
                    self.clearErrorA6_8();
                    if (!self.hasAppTimeOvertimeHours()) {
                        self.setErrorA6_8();
                    }
                })
            })
        }

        hasAppTimeOvertimeHours() {
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

        setErrorA6_8() {
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

        clearErrorA6_8() {
            $('.overtimeHoursCheck').ntsError('clear');
        }

        //登録処理
        registerClick() {
            let self = this;
            $('#kaf005-pre-post-select').ntsError('check');
            $("#inputdate").trigger("validate");
            if(self.displayCaculationTime()){
                $("#inpStartTime1").trigger("validate");
                $("#inpEndTime1").trigger("validate");
                if(!self.validate()){return;}
            }
            if(self.enableOvertimeInput()){
                if (!self.hasAppTimeOvertimeHours()) {
                    self.setErrorA6_8();
                }
            }
            //return if has error
            if (nts.uk.ui.errors.hasError()){return;}              
            //block screen
            nts.uk.ui.block.invisible();
            
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
            let overtime = {
                applicationDate: new Date(self.appDate()),
                prePostAtr: self.prePostSelected(),
                applicantSID: self.employeeID(),
                workTypeCode: self.workTypeCd(),
                siftTypeCode: self.siftCD(),
                workClockFrom1: self.timeStart1(),
                workClockTo1: self.timeEnd1(),
                overtimeHours: ko.toJS(self.overtimeHours()),
                restTime: ko.toJS(self.restTime()),
                overTimeShiftNight: overTimeShiftNightTmp == null ? null : overTimeShiftNightTmp,
                flexExessTime: flexExessTimeTmp == null ? null : flexExessTimeTmp,
                sendMail: self.checkBoxValue(),
                overtimeAtr: self.overtimeAtr(),
                calculateFlag: self.calculateFlag(),
                checkOver1Year: true,
                checkAppDate:false,
                opAppBefore: self.opAppBefore,
                beforeAppStatus: self.beforeAppStatus,
                actualStatus: self.actualStatus,
                actualLst: self.actualLst,
                overtimeSettingDataDto: self.overtimeSettingDataDto
            };
            let newOvertimes = ko.toJSON(self.overtimeHours());
            let oldOvertimes = ko.toJSON(self.overtimeHoursOld);
            if(newOvertimes.localeCompare(oldOvertimes)) {
                self.resetForceAction();    
            }  
            
            //登録前エラーチェック
            self.beforeRegisterColorConfirm(overtime);
        }
        
        //登録処理を実行
        registerData(overtime) {
            var self = this;
            self.forceInconsistency = true;
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
            overtime.applicationReason = textAreaReason;
            overtime.divergenceReasonContent = comboDivergenceReason;
            overtime.appReasonID = comboBoxReason;
            overtime.divergenceReasonArea = areaDivergenceReason;
            overtime.appOvertimeDetail = self.appOvertimeDetail;
            service.createOvertime(overtime).done((data) => {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    if(self.isSpr){
                        let cache = JSON.parse(window.sessionStorage.getItem("nts.uk.request.STORAGE_KEY_TRANSFER_DATA"));    
                        cache.applicationReason = self.multilContent();
                        window.sessionStorage.setItem("nts.uk.request.STORAGE_KEY_TRANSFER_DATA", JSON.stringify(cache));        
                    }
                    if(data.autoSendMail){
                        appcommon.CommonProcess.displayMailResult(data);  
                    } else {
                        if(self.checkBoxValue()){
                            appcommon.CommonProcess.openDialogKDL030(data.appID);  
                        } else {
                            location.reload();
                        }   
                    }
                });
            }).fail((res) => {
                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                .then(function() { nts.uk.ui.block.clear(); });
            });
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
                if(!nts.uk.util.isNullOrEmpty(startTime)){
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
                self.resultLst = data.resultLst;
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
            let resultLst = _.isEmpty(calcData.resultLst) ? self.resultLst : calcData.resultLst;
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
        
        getReasonName(reasonCombo: common.ComboReason, reasonId: string): string{  
            let self = this;
           let selectedReason = _.find(reasonCombo, item => {return item.reasonId == reasonId} );
           if(!nts.uk.util.isNullOrUndefined(selectedReason)){
              return selectedReason.reasonName; 
           }
           return "";
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
                            appDate: nts.uk.util.isNullOrEmpty(self.appDate()) ? null : moment(self.appDate()).format(self.DATE_FORMAT),
                            siftCD: self.siftCD(),
                            prePostAtr: self.prePostSelected(),
                            overtimeHours: _.map(ko.toJS(self.overtimeHours()), item => {return self.initCalculateData(item);}),
                            workTypeCode: self.workTypeCd(),
                            startTimeRests: nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(),x=>{return x.startTime()}),
                            endTimeRests:nts.uk.util.isNullOrEmpty(self.restTime())? [] : _.map(self.restTime(),x=>{return x.endTime()}),
                            restTimeDisFlg: self.restTimeDisFlg(),
                            overtimeSettingDataDto: self.overtimeSettingDataDto
                        }
                    ).done(data => {
                        $("#inpStartTime1").ntsError("clear"); 
                        $("#inpEndTime1").ntsError("clear");
                        self.timeStart1(data.startTime1 == null ? null : data.startTime1);
                        self.timeEnd1(data.endTime1 == null ? null : data.endTime1);
                        self.convertAppOvertimeReferDto(data);
                        // 休憩時間
                        self.setTimeZones(data.timezones);
                       
                    });
                }
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
        
        findBychangeAppDateData(data: any) {
            var self = this;
            let overtimeDto = data;
            if(data.displayCaculationTime) {
                if(_.isEmpty(data.siftTypes)) {
                    dialog.alertError({ messageId: "Msg_1568" })
                    .then(function() { 
                        nts.uk.ui.block.clear();
                    });               
                } 
            }
            self.prePostSelected(overtimeDto.application.prePostAtr);
            self.displayPrePostFlg(data.displayPrePostFlg ? true : false);
            self.displayCaculationTime(overtimeDto.displayCaculationTime);
            self.restTimeDisFlg(self.restTimeDisFlg());
            self.employeeName(overtimeDto.employeeName);
            if (overtimeDto.siftType != null) {
                self.siftCD(overtimeDto.siftType.siftCode);
                self.siftName(self.getName(overtimeDto.siftType.siftCode,overtimeDto.siftType.siftName));
            }
            if (overtimeDto.workType != null) {
                self.workTypeCd(overtimeDto.workType.workTypeCode);
                self.workTypeName(overtimeDto.workType.workTypeName|| text("KAL003_120"));
            }
            $("#inpStartTime1").ntsError("clear"); 
            $("#inpEndTime1").ntsError("clear");
            self.timeStart1(data.workClockFrom1);
            self.timeEnd1(data.workClockTo1);
            if(overtimeDto.applicationReasonDtos != null){
                self.reasonCombo(_.map(overtimeDto.applicationReasonDtos, o => { return new common.ComboReason(o.reasonID, o.reasonTemp); }));
                self.selectedReason(_.find(overtimeDto.applicationReasonDtos, o => { return o.defaultFlg == 1 }).reasonID);
                self.multilContent(overtimeDto.application.applicationReason);
            }
            
            if(overtimeDto.divergenceReasonDtos != null){
                self.reasonCombo2(_.map(overtimeDto.divergenceReasonDtos, o => { return new common.ComboReason(o.divergenceReasonID, o.reasonTemp); }));
                let defaultID = _.find(overtimeDto.divergenceReasonDtos, o => { return o.divergenceReasonIdDefault == 1 })
                let reasonID = "";
                if(!nts.uk.util.isNullOrUndefined(defaultID)){
                    reasonID = defaultID;         
                }
                self.selectedReason2(reasonID);
                self.multilContent2(overtimeDto.divergenceReasonContent); 
            }
            
            // preAppOvertime
            self.convertpreAppOvertimeDto(overtimeDto);
            self.referencePanelFlg(data.referencePanelFlg);
            self.preAppPanelFlg(data.preAppPanelFlg);
            self.allPreAppPanelFlg(data.allPreAppPanelFlg);
            self.isRightContent(data.allPreAppPanelFlg || data.referencePanelFlg);
            self.opAppBefore = data.opAppBefore;
            self.beforeAppStatus = data.beforeAppStatus;
            self.actualStatus = data.actualStatus;
            self.actualLst = data.actualLst;
           
             // 残業時間
            if (overtimeDto.overTimeInputs != null) {
                for (let i = 0; i < overtimeDto.overTimeInputs.length; i++) {
                    //1: 残業時間
                    if (overtimeDto.overTimeInputs[i].attendanceID == 1) {
                        self.overtimeHours.push(new common.OvertimeCaculation("", "", overtimeDto.overTimeInputs[i].attendanceID, "", overtimeDto.overTimeInputs[i].frameNo,0, overtimeDto.overTimeInputs[i].frameName, null, null, null,"#[KAF005_55]","",""));
                    }
                }
            }
            
            // 休憩時間
            self.setTimeZones(data.timezones);
           
        }
        
        convertpreAppOvertimeDto(data :any){
            let self = this;
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
            self.overtimeHoursPre.removeAll();
            if(data.preAppOvertimeDto.overTimeInputsPre != null){
                for (let i = 0; i < data.preAppOvertimeDto.overTimeInputsPre.length; i++) {
                    if(data.preAppOvertimeDto.overTimeInputsPre[i].applicationTime != -1){
                        if(data.preAppOvertimeDto.overTimeInputsPre[i].frameNo != 11 && data.preAppOvertimeDto.overTimeInputsPre[i].frameNo != 12){
                            let preAppTime = data.preAppOvertimeDto.overTimeInputsPre[i].applicationTime == null ? self.convertIntToTime(0) : self.convertIntToTime(data.preAppOvertimeDto.overTimeInputsPre[i].applicationTime);
                            self.overtimeHoursPre.push(new common.AppOvertimePre("", "", 
                            data.preAppOvertimeDto.overTimeInputsPre[i].attendanceID,
                            "", data.preAppOvertimeDto.overTimeInputsPre[i].frameNo,
                            0, data.preAppOvertimeDto.overTimeInputsPre[i].frameName +" : ",
                            data.preAppOvertimeDto.overTimeInputsPre[i].startTime == null ? self.convertIntToTime(0) : self.convertIntToTime(data.preAppOvertimeDto.overTimeInputsPre[i].startTime),
                            data.preAppOvertimeDto.overTimeInputsPre[i].endTime,
                            preAppTime,null));
                        }
                    }else{
                        continue;    
                    } 
                }
            }
            self.overTimeShiftNightPre(data.preAppOvertimeDto.overTimeShiftNightPre == null ? self.convertIntToTime(0) : self.convertIntToTime(data.preAppOvertimeDto.overTimeShiftNightPre));
            self.flexExessTimePre(data.preAppOvertimeDto.overTimeShiftNightPre == null ? self.convertIntToTime(0) : self.convertIntToTime(data.preAppOvertimeDto.flexExessTimePre));
            }
        }
        
        convertAppOvertimeReferDto(data :any){
            let self = this;
            if(data.appOvertimeReference != null && !nts.uk.util.isNullOrEmpty(self.appDate())) {
                self.appDateReference(data.appOvertimeReference.appDateRefer);
                if(data.appOvertimeReference.workTypeRefer != null){
                    self.workTypeCodeReference(data.appOvertimeReference.workTypeRefer.workTypeCode);
                    self.workTypeNameReference(self.getName(data.appOvertimeReference.workTypeRefer.workTypeCode, data.appOvertimeReference.workTypeRefer.workTypeName));
                } else {
                    self.workTypeCodeReference("");
                    self.workTypeNameReference("");        
                }
                if(data.appOvertimeReference.siftTypeRefer != null){
                    self.siftCodeReference(data.appOvertimeReference.siftTypeRefer.siftCode);
                    self.siftNameReference(self.getName(data.appOvertimeReference.siftTypeRefer.siftCode, data.appOvertimeReference.siftTypeRefer.siftName));
                } else {
                    self.siftCodeReference("");
                    self.siftNameReference("");
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
        
        convertIntToTime(data : any) : string{
            if(nts.uk.util.isNullOrEmpty(data)) {
                return null;    
            }
            return nts.uk.time.format.byId("Time_Short_HM", data);
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
        private changeOvertimeHours(data){
            let self = this;
            for (let i = 0; i < data.length; i++) {
                //残業時間

                if (data[i].frameNo != 11 && data[i].frameNo != 12) {
                    self.overtimeHours.push(new common.OvertimeCaculation("", "",
                        data[i].attendanceID,
                        "",
                        data[i].frameNo,
                        0,
                        data[i].frameName,
                        data[i].applicationTime,
                        self.convertIntToTime(data[i].preAppTime),
                        self.convertIntToTime(data[i].caculationTime), "#[KAF005_55]", "", ""));

                } else if (data[i].frameNo == 11) {
                    self.overtimeHours.push(new common.OvertimeCaculation("", "",
                        data[i].attendanceID,
                        "",
                        data[i].frameNo,
                        0,
                        nts.uk.resource.getText("KAF005_63"),
                        data[i].applicationTime,
                        self.convertIntToTime(data[i].preAppTime),
                        self.convertIntToTime(data[i].caculationTime), "#[KAF005_64]", "", ""));
                } else if (data[i].frameNo == 12) {
                    self.overtimeHours.push(new common.OvertimeCaculation("", "",
                        data[i].attendanceID,
                        "",
                        data[i].frameNo,
                        0,
                        nts.uk.resource.getText("KAF005_65"),
                        data[i].applicationTime,
                        self.convertIntToTime(data[i].preAppTime),
                        self.convertIntToTime(data[i].caculationTime), "#[KAF005_66]", "", ""));
                }

            }
        }
        
        beforeRegisterColorConfirm(overtime: any){
            let self = this;
            if(self.forceYearConfirm && self.forcePreApp && self.forceActual) {
                self.beforeRegisterProcess(overtime);   
                return;  
            }
            service.beforeRegisterColorConfirm(overtime).done((data2) => {
                overtime.checkOver1Year = false;
                self.contentBefRegColorConfirmDone(overtime, data2);
            }).fail((res) => {
                if (res.messageId == "Msg_1518") {//confirm
                    if(self.forceYearConfirm) {
                        overtime.checkOver1Year = false;
                        service.beforeRegisterColorConfirm(overtime).done((data3) => {
                            self.contentBefRegColorConfirmDone(overtime, data3);
                        }).fail((res) => {
                            dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                                .then(function() { nts.uk.ui.block.clear(); });
                        });    
                    } else {
                        dialog.confirm({ messageId: res.messageId }).ifYes(() => {
                            overtime.checkOver1Year = false;
                            service.beforeRegisterColorConfirm(overtime).done((data3) => {
                                self.contentBefRegColorConfirmDone(overtime, data3);
                            }).fail((res) => {
                                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                                    .then(function() { nts.uk.ui.block.clear(); });
                            });
                        }).ifNo(() => {
                            nts.uk.ui.block.clear();
                        });
                    }
                } else {
                    dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                        .then(function() { nts.uk.ui.block.clear(); });
                }
            });
        }
        
        contentBefRegColorConfirmDone(overtime, data2) {
            let self = this;
            self.forceYearConfirm = true;
            if(nts.uk.util.isNullOrUndefined(data2.preActualColorResult)){
                self.beforeRegisterProcess(overtime);    
            } else {
                self.overtimeHoursOld = ko.toJS(self.overtimeHours());
                self.resultLst = data2.resultLst;
                self.fillColor(data2.preActualColorResult);
                self.checkPreApp(overtime, data2);        
            }
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
                self.beforeRegisterProcess(overtime);
                return;    
            }
            let beforeAppStatus = data.preActualColorResult.beforeAppStatus;
            let actualStatus = data.preActualColorResult.actualStatus;
            let resultLst = data.preActualColorResult.resultLst;
            if(self.performanceExcessAtr()==0){
                self.beforeRegisterProcess(overtime);
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
                        self.beforeRegisterProcess(overtime);  
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
                    self.beforeRegisterProcess(overtime);
                }).ifNo(() => {
                    nts.uk.ui.block.clear();
                }); 
                return;
            } else {
                self.beforeRegisterProcess(overtime);    
            }
        }
        
        beforeRegisterProcess(overtime: any){
            let self = this;
            self.forcePreApp = true;
            self.forceActual = true;
            if(self.forceOvertimeDetail) {
                self.confirmInconsistency(overtime);
                return;       
            }
            service.checkBeforeRegister(overtime).done((data) => {
                self.forceOvertimeDetail = true;    
                self.appOvertimeDetail = data.appOvertimeDetail;
                self.confirmInconsistency(overtime);
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
        
        confirmInconsistency(overtime: any){
            let self = this;
            if(self.forceInconsistency) {
                self.registerData(overtime);
                return;       
            }
            service.confirmInconsistency(overtime).done((data1) => { 
                if (!nts.uk.util.isNullOrEmpty(data1)) {
                    dialog.confirm({ messageId: data1[0], messageParams: [data1[1],data1[2]] }).ifYes(() => {
                        //登録処理を実行
                        self.registerData(overtime);
                    }).ifNo(() => {
                        //終了状態：処理をキャンセル
                        nts.uk.ui.block.clear();
                        return;
                    });
                } else {
                    //登録処理を実行
                    self.registerData(overtime);
                }   
            }).fail((res) => {
                dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                .then(function() { nts.uk.ui.block.clear(); });           
            });    
        }
        
        resetForceAction() {
            let self = this;
            self.forceYearConfirm = false;
            self.forcePreApp = false;
            self.forceActual = false;
            self.forceOvertimeDetail = false;
            self.forceInconsistency = false;        
        }
    }

}


