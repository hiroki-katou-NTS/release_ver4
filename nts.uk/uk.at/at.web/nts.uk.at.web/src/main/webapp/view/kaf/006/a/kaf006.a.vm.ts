module nts.uk.at.view.kaf006.a.viewmodel {
    import common = nts.uk.at.view.kaf006.share.common;
    import service = nts.uk.at.view.kaf006.shr.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import setShared = nts.uk.ui.windows.setShared;
    import modal = nts.uk.ui.windows.sub.modal;
    import getText = nts.uk.resource.getText;
    export class ScreenModel {
        DATE_FORMAT: string = "YYYY/MM/DD";
        //kaf000
        kaf000_a: kaf000.a.viewmodel.ScreenModel;
        checkBoxValue: KnockoutObservable<boolean> = ko.observable(false);
        enableSendMail: KnockoutObservable<boolean> = ko.observable(false);
        mailFlag: KnockoutObservable<boolean> = ko.observable(true);
        screenModeNew: KnockoutObservable<boolean> = ko.observable(true);
        displayEndDateFlg: KnockoutObservable<boolean> = ko.observable(false);
        enableDisplayEndDate: KnockoutObservable<boolean> = ko.observable(true);
        //current Data
        //        curentGoBackDirect: KnockoutObservable<common.GoBackDirectData>;
        //申請者
        employeeName: KnockoutObservable<string> = ko.observable("");
        employeeList :KnockoutObservableArray<common.EmployeeOT> = ko.observableArray([]);
        selectedEmplCodes: KnockoutObservable<string> = ko.observable(null);
        employeeFlag: KnockoutObservable<boolean> = ko.observable(false);
        totalEmployee: KnockoutObservable<string> = ko.observable(null);
        //Pre-POST
        prePostSelected: KnockoutObservable<number> = ko.observable(3);
        workState: KnockoutObservable<boolean> = ko.observable(true);
        typeSiftVisible: KnockoutObservable<boolean> = ko.observable(true);
        // 申請日付
        startAppDate: KnockoutObservable<string> = ko.observable('');
        // 申請日付
        endAppDate: KnockoutObservable<string> = ko.observable('');
        dateValue: KnockoutObservable<any> = ko.observable({ startDate: '', endDate: '' });
        appDate: KnockoutObservable<string> = ko.observable('');
        selectedAllDayHalfDayValue: KnockoutObservable<number> = ko.observable(0);
        holidayTypes: KnockoutObservableArray<common.HolidayType> = ko.observableArray([]);
        holidayTypeCode: KnockoutObservable<number> = ko.observable(0);
        typeOfDutys: KnockoutObservableArray<common.TypeOfDuty> = ko.observableArray([]);
        selectedTypeOfDuty: KnockoutObservable<any> = ko.observable(null);
        displayHalfDayValue: KnockoutObservable<boolean> = ko.observable(false);
        changeWorkHourValue: KnockoutObservable<boolean> = ko.observable(false);
        changeWorkHourValueFlg: KnockoutObservable<boolean> = ko.observable(false);
        //        displayChangeWorkHour:  KnockoutObservable<boolean> = ko.observable(false);
        displayStartFlg: KnockoutObservable<boolean> = ko.observable(false);
        contentFlg: KnockoutObservable<boolean> = ko.observable(true);
        eblTimeStart1: KnockoutObservable<boolean> = ko.observable(false);
        eblTimeEnd1: KnockoutObservable<boolean> = ko.observable(false);
        workTimeCodes: KnockoutObservableArray<string> = ko.observableArray([]);
        workTypecodes: KnockoutObservableArray<string> = ko.observableArray([]);
        displayWorkTimeName: KnockoutObservable<string> = ko.observable(null);
        //TIME LINE 1
        timeStart1: KnockoutObservable<number> = ko.observable(null);
        timeEnd1: KnockoutObservable<number> = ko.observable(null);
        //TIME LINE 2
        timeStart2: KnockoutObservable<number> = ko.observable(null);
        timeEnd2: KnockoutObservable<number> = ko.observable(null);
        //勤務種類
        workTimeCode: KnockoutObservable<string> = ko.observable('');
        workTimeName: KnockoutObservable<string> = ko.observable('');
        //comboBox 定型理由
        reasonCombo: KnockoutObservableArray<common.ComboReason> = ko.observableArray([]);
        selectedReason: KnockoutObservable<string> = ko.observable('');
        //MultilineEditor
        requiredReason: KnockoutObservable<boolean> = ko.observable(false);
        multilContent: KnockoutObservable<string> = ko.observable('');

        //Approval 
        approvalSource: Array<common.AppApprovalPhase> = [];
        employeeID: KnockoutObservable<string> = ko.observable('');
        //menu-bar 
        prePostDisp: KnockoutObservable<boolean> = ko.observable(true);
        prePostEnable: KnockoutObservable<boolean> = ko.observable(true);
        useMulti: KnockoutObservable<boolean> = ko.observable(true);

        displayPrePostFlg: KnockoutObservable<boolean> = ko.observable(true);

        typicalReasonDisplayFlg: KnockoutObservable<boolean> = ko.observable(true);
        displayAppReasonContentFlg: KnockoutObservable<boolean> = ko.observable(true);
        // enable
        enbAllDayHalfDayFlg: KnockoutObservable<boolean> = ko.observable(true);
        enbWorkType: KnockoutObservable<boolean> = ko.observable(true);
        enbHalfDayFlg: KnockoutObservable<boolean> = ko.observable(true);
        enbChangeWorkHourFlg: KnockoutObservable<boolean> = ko.observable(true);
        enbbtnWorkTime: KnockoutObservable<boolean> = ko.observable(true);
        enbReasonCombo: KnockoutObservable<boolean> = ko.observable(true);
        enbContentReason: KnockoutObservable<boolean> = ko.observable(true);
        employeeIDs: KnockoutObservableArray<string> = ko.observableArray([]);
        targetDate: any = moment(new Date()).format(this.DATE_FORMAT);
        //ver15
        selectedRelation: KnockoutObservable<any> = ko.observable('');
        relationCombo: KnockoutObservableArray<any> = ko.observableArray([]);
        relaReason: KnockoutObservable<any> = ko.observable('');
        mournerDis: KnockoutObservable<boolean> = ko.observable(false);
        isCheck: KnockoutObservable<boolean> = ko.observable(false);
        fix: KnockoutObservable<boolean> = ko.observable(false);
        maxDayDis: KnockoutObservable<boolean> = ko.observable(false);
        maxDayline1: KnockoutObservable<string> = ko.observable('');
        maxDayline2: KnockoutObservable<string> = ko.observable('');
        requiredRela: KnockoutObservable<boolean> = ko.observable(true);
        //上限日数
        maxDay: KnockoutObservable<number> = ko.observable(0);
        //喪主加算日数
        dayOfRela: KnockoutObservable<number> = ko.observable(0);
        relaEnable: KnockoutObservable<boolean> = ko.observable(true);
        relaMourner: KnockoutObservable<boolean> = ko.observable(true);
        relaRelaReason: KnockoutObservable<boolean> = ko.observable(true);
        displayReasonLst: Array<common.DisplayReason> = []; 
        //No.65 + No.376
        pridigCheck: KnockoutObservable<boolean> = ko.observable(true);
        numberSubHd: KnockoutObservable<number> = ko.observable(0);
        numberSubVaca: KnockoutObservable<number> = ko.observable(0);
        settingNo65: KnockoutObservable<common.SettingNo65> = ko.observable(null);
        yearRemain: KnockoutObservable<string> = ko.observable('0日');//年休残数
        subHdRemain: KnockoutObservable<string> = ko.observable('0日');//代休残数
        subVacaRemain: KnockoutObservable<string> = ko.observable('0日');//振休残数
        stockRemain: KnockoutObservable<string> = ko.observable('0日');//ストック休暇残数
        numberRemain: KnockoutObservableArray<any> = ko.observableArray([]);
        yearDis: KnockoutObservable<boolean> = ko.observable(false);
        subHdDis: KnockoutObservable<boolean> = ko.observable(false);
        subVacaDis: KnockoutObservable<boolean> = ko.observable(false);
        stockDis: KnockoutObservable<boolean> = ko.observable(false);
        //ver20
        disAll: KnockoutObservable<boolean> = ko.observable(false);
        //ver21
        relaResonDis: KnockoutObservable<boolean> = ko.observable(true);
        hdTypeDis: KnockoutObservable<boolean> = ko.observable(false);
        dataMax: KnockoutObservable<boolean> = ko.observable(false);
        dayDispSet: KnockoutObservable<boolean> = ko.observable(false);
        constructor(transferData :any) {

            let self = this;
            $(document).ajaxStart(function() {
                nts.uk.ui.block.invisible();
            }).ajaxStop(function() {
                nts.uk.ui.block.clear();
            });
            if(transferData != null){
                self.appDate(transferData.appDate);
                self.employeeIDs(transferData.employeeIDs);
                self.employeeID(transferData.employeeID); 
                if(!nts.uk.util.isNullOrUndefined(transferData.appDate)){
                    self.targetDate = transferData.appDate;        
                }
            }
            //KAF000_A
            self.kaf000_a = new kaf000.a.viewmodel.ScreenModel();
            //startPage 006a AFTER start 000_A
            self.startPage().done(function() {
                self.kaf000_a.start(self.employeeID(), 1, 1, self.targetDate).done(function() {
                    self.approvalSource = self.kaf000_a.approvalList;

                })
            })
            self.selectedRelation.subscribe(function(codeChange){
                if(codeChange === undefined || codeChange == null || codeChange.length == 0){
                    return;
                }
                $('#relaReason').ntsError('clear');
                service.changeRelaCD(self.selectedTypeOfDuty(), codeChange).done(function(data){
                    //上限日数表示エリア(vùng hiển thị số ngày tối đa)
                    let line1 = getText('KAF006_44');
                    let maxDay = 0;
                    if(self.mournerDis() && self.isCheck()){//・ 画面上喪主チェックボックス(A10_3)が表示される　AND チェックあり ⇒ 上限日数　＋　喪主加算日数
                        maxDay = data.maxDayObj == null ? 0 :  data.maxDayObj.maxDay + data.maxDayObj.dayOfRela;
                    }else{//・その以外 ⇒ 上限日数
                        maxDay = data.maxDayObj == null ? 0 : data.maxDayObj.maxDay;
                    }
                    if(data.maxDayObj != null){
                        self.maxDay(data.maxDayObj.maxDay);
                        self.dayOfRela(data.maxDayObj.dayOfRela);
                        self.dataMax(true);  
                    }else{
                        self.dataMax(false);    
                    }
                    let line2 = getText('KAF006_46',[maxDay]);
                    
                    self.maxDayline1(line1);
                    self.maxDayline2(line2);
                    //ver21
                    let relaS = self.findRelaSelected(codeChange);
                    self.relaResonDis(relaS == undefined ? false : relaS.threeParentOrLess);
                });
            });
            self.isCheck.subscribe(function(checkChange){
                if(self.mournerDis()){
                    //上限日数表示エリア(vùng hiển thị số ngày tối đa)
                    let line1 = getText('KAF006_44');
                    let maxDay = 0;
                    if(self.mournerDis() && self.isCheck()){//・ 画面上喪主チェックボックス(A10_3)が表示される　AND チェックあり ⇒ 上限日数　＋　喪主加算日数
                        maxDay = self.maxDay() + self.dayOfRela();
                    }else{//・その以外 ⇒ 上限日数
                        maxDay = self.maxDay();
                    }
                    let line2 = getText('KAF006_46',[maxDay]);
                    
                    self.maxDayline1(line1);
                    self.maxDayline2(line2);
                }
            });
        }
        findRelaSelected(relaCD: string): any{
            let self = this;
            return _.find(self.relationCombo(), function(rela){
                return rela.relationCd == relaCD;
            });
        }
        /**
         * 
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            nts.uk.ui.block.invisible();
            service.getAppForLeaveStart({
                appDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                employeeID: null,
                employeeIDs: nts.uk.util.isNullOrEmpty(self.employeeIDs()) ? null : self.employeeIDs()
            }).done((data) => {
                $("#inputdate").focus();
                //No.65
                if(data.setingNo65 != null){
                    self.settingNo65(new common.SettingNo65(data.setingNo65.hdType,data.setingNo65.screenMode, data.setingNo65.pridigCheck,
                        data.setingNo65.subVacaManage, data.setingNo65.subVacaTypeUseFlg, data.setingNo65.subHdManage, data.setingNo65.subHdTypeUseFlg));
                }
                //No.376
                if(data.numberRemain != null){
                    if(data.numberRemain.yearRemain != null){//年休残数
                        self.yearRemain(data.numberRemain.yearRemain + '日');
                        self.yearDis(true);
                    }
                    if(data.numberRemain.subHdRemain != null){//代休残数
                        self.subHdRemain(data.numberRemain.subHdRemain + '日');
                        self.numberSubHd(data.numberRemain.subHdRemain);
                        self.subHdDis(true);
                    }
                    if(data.numberRemain.subVacaRemain != null){//振休残数
                        self.subVacaRemain(data.numberRemain.subVacaRemain + '日');
                        self.numberSubVaca(data.numberRemain.subVacaRemain);
                        self.subVacaDis(true);
                    }
                    if(data.numberRemain.stockRemain != null){//ストック休暇残数
                        self.stockRemain(data.numberRemain.stockRemain + '日');
                        self.stockDis(true);
                    }
                }
                if(self.yearDis() || self.subHdDis() || self.subVacaDis() || self.stockDis()){
                    self.disAll(true);
                }
                self.initData(data);
                //ver16
                self.prePostEnable(data.prPostChange);
                self.holidayTypeCode.subscribe(function(value) {
                    let currentDisplay = _.find(self.displayReasonLst, (o) => o.typeLeave==value);
                    if(nts.uk.util.isNullOrUndefined(currentDisplay)){
                        self.typicalReasonDisplayFlg(false);
                        self.displayAppReasonContentFlg(false);        
                    } else {
                        self.typicalReasonDisplayFlg(currentDisplay.displayFixedReason);
                        self.displayAppReasonContentFlg(currentDisplay.displayAppReason);     
                    }
                    self.checkDisplayEndDate(self.displayEndDateFlg());
                    if (self.checkStartDate()) {
                        return;
                    }
                    if (!nts.uk.util.isNullOrEmpty(self.selectedAllDayHalfDayValue())) {
                        var dfd = $.Deferred();
                        service.getAllAppForLeave({
                            startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                            endAppDate: nts.uk.util.isNullOrEmpty(self.endAppDate()) ? null : moment(self.endAppDate()).format(self.DATE_FORMAT),
                            employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                            displayHalfDayValue: self.displayHalfDayValue(),
                            holidayType: value,
                            alldayHalfDay: self.selectedAllDayHalfDayValue(),
                            relationCD: self.selectedRelation()
                        }).done((data) => {
                            //hoatt 2018.08.09
//                            self.changeForSpecHd(data);
                            self.displayStartFlg(true);
                            self.changeWorkHourValueFlg(data.changeWorkHourFlg);
                            if (nts.uk.util.isNullOrEmpty(data.workTypes)) {
                                self.typeOfDutys([]);
                                self.workTypecodes([]);
                                self.selectedTypeOfDuty(null);
                            } else {
                                let a = [];
                                self.workTypecodes.removeAll();
                                for (let i = 0; i < data.workTypes.length; i++) {
                                    a.push(new common.TypeOfDuty(data.workTypes[i].workTypeCode, data.workTypes[i].displayName));
                                    self.workTypecodes.push(data.workTypes[i].workTypeCode);
                                }
                                self.typeOfDutys(a);
                                let contain = _.find(a, (o) => { return o.typeOfDutyID == self.selectedTypeOfDuty(); });
                                if (nts.uk.util.isNullOrUndefined(contain)){
                                    self.selectedTypeOfDuty('');
                                }
                                
                            }
                            $("#workTypes").find("input:first").focus();
                            dfd.resolve(data);
                        }).fail((res) => {
                            dfd.reject(res);
                        });
                        return dfd.promise();
                    }
                });
                self.selectedAllDayHalfDayValue.subscribe(function(value) {
                    if (value == 0) {
                        self.enbHalfDayFlg(true);
                    } else {
                        self.enbHalfDayFlg(false);
                    }
                });


                //find by change AllDayHalfDay
                self.selectedAllDayHalfDayValue.subscribe((value) => {
                    self.findChangeAllDayHalfDay(value);
                });
                // find change value A5_3
                self.displayHalfDayValue.subscribe((value) => {
                    self.findChangeDisplayHalfDay(value);
                });
                self.selectedTypeOfDuty.subscribe((value) => {
                    if(nts.uk.util.isNullOrUndefined(value)||nts.uk.util.isNullOrEmpty(value)){
                        self.changeWorkHourValueFlg(false);
                        self.maxDayDis(false); 
                        self.hdTypeDis(false);
                    } else {
                        self.findChangeWorkType(value);
                        if(self.holidayTypeCode() == 3){
                            self.hdTypeDis(true);
                        }else{
                            self.hdTypeDis(false);
                        }
                    }
                });
                self.displayWorkTimeName.subscribe((value) => {
                    self.changeDisplayWorkime();
                });
                // find changeDate
                self.appDate.subscribe(function(value) {
                    if(value == null || value == '' || value ==  undefined){
                        return;
                    }
                    self.findChangeAppDate(value);
                });
                self.displayEndDateFlg.subscribe((value) => {
                    nts.uk.ui.errors.clearAll();
                    if (value) {
                        $('.ntsStartDatePicker').focus();
                        self.dateValue({ startDate: self.appDate(), endDate: "" });
                        self.dateValue.subscribe(function() {
                            if ($("#daterangepicker").find(".ntsDateRangeComponent").ntsError("hasError")) {
                                return;
                            }
                            if(self.dateValue().startDate != '' && self.dateValue().endDate != ''){
                                self.findChangeAppDate(self.dateValue().startDate);
                            }
                            
                        })
                    } else {
                        self.appDate(self.dateValue().startDate);
                        self.endAppDate('');
                        $("#inputdate").focus();
                    }
                    if(self.relaReason() != ''){
                        $("#relaReason").trigger("validate");
                    }
                });
                self.changeWorkHourValue.subscribe((value) =>{
                    self.changeDisplayWorkime();
                });
                nts.uk.ui.block.clear();
                dfd.resolve(data);
            }).fail((res) => {
                if (res.messageId == 'Msg_426') {
                    dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.ui.block.clear();
                    });
                } else if (res.messageId == 'Msg_473') {
                    dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.ui.block.clear();
                    });
                } else {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
                        nts.uk.ui.block.clear();
                    });
                }
                dfd.reject(res);
            });
            return dfd.promise();

        }
        changeForSpecHd(data: any){
            let self = this;
             //hoatt 2018.08.09
            //relationship list
            self.relationCombo([]);
            let lstRela = [];
            _.each(data.lstRela, function(rela){
                lstRela.push({relationCd: rela.relationCD, relationName: rela.relationName, 
                        maxDate: rela.maxDate, threeParentOrLess: rela.threeParentOrLess});
            });
            self.relationCombo(lstRela);
            let fix = false;
            if(data.specHdForEventFlag){
                fix = data.maxNumberDayType == 2 ? true : false;
            }
            if(!fix && self.relaReason() != ''){
                $('#relaReason').ntsError('clear');
            }
            $("#relaCD-combo").ntsError('clear');
            self.fix(fix);
            if(!fix){
                self.requiredRela(false);
            }else{
                self.requiredRela(true);
            }
            self.maxDayDis(data.specHdForEventFlag);
            if(data.specHdForEventFlag && data.maxNumberDayType == 2 && data.makeInvitation == 1){
                self.mournerDis(true);
            }else{
               self.mournerDis(false);
            }
            if(self.holidayTypeCode() == 3){
                //上限日数表示エリア(vùng hiển thị số ngày tối đa)
                let line1 = getText('KAF006_44');
                let maxDay = 0;
                if(self.mournerDis() && self.isCheck()){//・ 画面上喪主チェックボックス(A10_3)が表示される　AND チェックあり ⇒ 上限日数　＋　喪主加算日数
                    maxDay =data.maxDayObj == null ? 0 :  data.maxDayObj.maxDay + data.maxDayObj.dayOfRela;
                }else{//・その以外 ⇒ 上限日数
                    maxDay = data.maxDayObj == null ? 0 : data.maxDayObj.maxDay;
                }
                if(data.maxDayObj != null){
                    self.maxDay(data.maxDayObj.maxDay);
                    self.dayOfRela(data.maxDayObj.dayOfRela);
                    self.dataMax(true);  
                }else{
                    self.dataMax(false);    
                }
                let line2 = getText('KAF006_46',[maxDay]);
                
                self.maxDayline1(line1);
                self.maxDayline2(line2);
            }
        }
        // change by appDate
        findChangeAppDate(data: any) {
            let self = this;
            self.checkDisplayEndDate(self.displayEndDateFlg());
            if (self.checkStartDate()) {
                return;
            }
            let dfd = $.Deferred();
            service.findByChangeAppDate({
                startAppDate: nts.uk.util.isNullOrEmpty(data) ? null : moment(data).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                displayHalfDayValue: self.displayHalfDayValue(),
                holidayType: self.holidayTypeCode(),
                prePostAtr: self.prePostSelected(),
                workTypeCode: self.selectedTypeOfDuty(),
                alldayHalfDay: self.selectedAllDayHalfDayValue()
            }).done((result) => {
                if (!nts.uk.util.isNullOrEmpty(result.workTypes)) {
                    let a = [];
                    self.workTypecodes.removeAll();
                    for (let i = 0; i < result.workTypes.length; i++) {
                        a.push(new common.TypeOfDuty(result.workTypes[i].workTypeCode, result.workTypes[i].displayName));
                        self.workTypecodes.push(result.workTypes[i].workTypeCode);
                    }
                    self.typeOfDutys(a);
                    let contain = _.find(a, (o) => { return o.typeOfDutyID == self.selectedTypeOfDuty(); });
                    if (nts.uk.util.isNullOrUndefined(contain)){
                        self.selectedTypeOfDuty('');
                    }
                }
                self.prePostSelected(result.application.prePostAtr);
                self.displayPrePostFlg(result.prePostFlg);
                if (!nts.uk.util.isNullOrEmpty(self.startAppDate())) {
                    self.kaf000_a.getAppDataDate(1, moment(self.startAppDate()).format(self.DATE_FORMAT), false,nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID());
                }
                //ver13 hoatt - 2018.07.31
                self.convertListHolidayType(result.holidayAppTypeName, result.checkDis);
                dfd.resolve(result);
            }).fail((res) => {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        // change by switch button AllDayHalfDay(A3_12)
        findChangeAllDayHalfDay(value: any) {
            let self = this;
            self.checkDisplayEndDate(self.displayEndDateFlg());
            if (self.checkStartDate()) {
                return;
            }
            let dfd = $.Deferred();
            service.findChangeAllDayHalfDay({
                startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                endAppDate: nts.uk.util.isNullOrEmpty(self.endAppDate()) ? null : moment(self.endAppDate()).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                displayHalfDayValue: self.displayHalfDayValue(),
                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                alldayHalfDay: value
            }).done((result) => {
                self.changeWorkHourValueFlg(result.changeWorkHourFlg);
                if (nts.uk.util.isNullOrEmpty(result.workTypes)) {
                    self.typeOfDutys([]);
                    self.workTypecodes([]);
                    self.selectedTypeOfDuty(null);
                    self.fix(false);
                    self.mournerDis(false);
                    self.maxDayDis(false);
                    self.isCheck(false);
                    self.relaReason('');
                } else {
                    let a = [];
                    self.workTypecodes.removeAll();
                    for (let i = 0; i < result.workTypes.length; i++) {
                        a.push(new common.TypeOfDuty(result.workTypes[i].workTypeCode, result.workTypes[i].displayName));
                        self.workTypecodes.push(result.workTypes[i].workTypeCode);
                    }
                    self.typeOfDutys(a);
                    let contain = _.find(a, (o) => { return o.typeOfDutyID == self.selectedTypeOfDuty(); });
                    if (nts.uk.util.isNullOrUndefined(contain)){
                        self.selectedTypeOfDuty('');
                    }
                }
                if (!nts.uk.util.isNullOrEmpty(result.workTimeCodes)) {
                    self.workTimeCodes.removeAll();
                    self.workTimeCodes(result.workTimeCodes);
                }
                dfd.resolve(result);
            }).fail((res) => {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        // change by switch button DisplayHalfDay(A5_3)
        findChangeDisplayHalfDay(value: any) {
            let self = this;
            self.checkDisplayEndDate(self.displayEndDateFlg());
            if (self.checkStartDate()) {
                return;
            }
            let dfd = $.Deferred();
            service.getChangeDisplayHalfDay({
                startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                endAppDate: nts.uk.util.isNullOrEmpty(self.endAppDate()) ? null : moment(self.endAppDate()).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                displayHalfDayValue: self.displayHalfDayValue(),
                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                workTypeCode: self.selectedTypeOfDuty(),
                alldayHalfDay: self.selectedAllDayHalfDayValue()
            }).done((result) => {
                self.changeWorkHourValueFlg(result.changeWorkHourFlg);
                if (nts.uk.util.isNullOrEmpty(result.workTypes)) {
                    self.typeOfDutys([]);
                    self.workTypecodes([]);
                    self.selectedTypeOfDuty(null);
                    self.fix(false);
                    self.mournerDis(false);
                    self.maxDayDis(false);
                    self.isCheck(false);
                    self.relaReason('');
                } else {
                    let a = [];
                    self.workTypecodes.removeAll();
                    for (let i = 0; i < result.workTypes.length; i++) {
                        a.push(new common.TypeOfDuty(result.workTypes[i].workTypeCode, result.workTypes[i].displayName));
                        self.workTypecodes.push(result.workTypes[i].workTypeCode);
                    }
                    self.typeOfDutys(a);
                    let contain = _.find(a, (o) => { return o.typeOfDutyID == self.selectedTypeOfDuty(); });
                    if (nts.uk.util.isNullOrUndefined(contain)){
                        self.selectedTypeOfDuty('');
                    }
                }
                if (!nts.uk.util.isNullOrEmpty(result.workTimeCodes)) {
                    self.workTimeCodes.removeAll();
                    self.workTimeCodes(result.workTimeCodes);
                }
                dfd.resolve(result);
            }).fail((res) => {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        /**
         * when change by workType A5_2
         */
        findChangeWorkType(value: any) {
            let self = this;
            self.checkDisplayEndDate(self.displayEndDateFlg());
            if (self.checkStartDate()) {
                return;
            }
            let dfd = $.Deferred();
            service.getChangeWorkType({
                startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                workTypeCode: self.selectedTypeOfDuty(),
                workTimeCode: self.workTimeCode()
            }).done((result) => {
                //hoatt 2018.08.09
                self.isCheck(false);
                self.changeForSpecHd(result);
                self.changeWorkHourValueFlg(result.changeWorkHourFlg);
                if (result.startTime1 != null) {
                    self.timeStart1(result.startTime1);
                }
                if (result.endTime1 != null) {
                    self.timeEnd1(result.endTime1);
                }
                dfd.resolve(result);
            }).fail((res) => {
                dfd.reject(res);
            });
            return dfd.promise();
        }
        initData(data: any) {
            let self = this;
            _.forEach(data.displayReasonDtoLst, (o) => {
                self.displayReasonLst.push(new common.DisplayReason(o.typeOfLeaveApp, o.displayFixedReason==1?true:false, o.displayAppReason==1?true:false));     
            });
            self.checkBoxValue(data.manualSendMailFlg);
            self.enableSendMail(!data.sendMailWhenRegisterFlg);
            self.employeeName(data.employeeName);
            self.employeeID(data.employeeID);
            self.prePostSelected(data.application.prePostAtr);
            self.convertListHolidayType(data.holidayAppTypeName, data.checkDis);
            self.holidayTypeCode(null);
            self.displayPrePostFlg(data.prePostFlg);
            self.displayWorkTimeName(nts.uk.resource.getText("KAF006_21"));
            self.mailFlag(data.mailFlg);
            self.requiredReason(data.appReasonRequire);
            if (data.applicationReasonDtos != null && data.applicationReasonDtos.length > 0) {
                let lstReasonCombo = _.map(data.applicationReasonDtos, o => { return new common.ComboReason(o.reasonID, o.reasonTemp); });
                self.reasonCombo(lstReasonCombo);
                let reasonID = _.find(data.applicationReasonDtos, o => { return o.defaultFlg == 1 }).reasonID;
                self.selectedReason(reasonID);

                self.multilContent(data.application.applicationReason);
            }
            if(!nts.uk.util.isNullOrEmpty(data.employees)){
                self.employeeFlag(true);
                for(let i= 0; i < data.employees.length; i++){
                    self.employeeList.push(new common.EmployeeOT(data.employees[i].employeeIDs,data.employees[i].employeeName));
                }
                let total = data.employees.length;
                self.totalEmployee(nts.uk.resource.getText("KAF006_65",total.toString()));
            }
            self.dayDispSet(data.dayDispSet==1?true:false);
        }
        /**
         * when click button A1_1 - 登録
         */
        registerClick() {
            let self = this;
            self.checkDisplayEndDate(self.displayEndDateFlg());
            if (self.displayEndDateFlg()) {
               $('#daterangepicker').find(".nts-input").trigger('validate');
            } else {
                $("#inputdate").trigger("validate");
            }
            $("#switch_prePost").trigger("validate");
            $("#relaReason").trigger("validate");
            if(self.holidayTypeCode() == 3 && self.fix()){
                $("#relaCD-combo").trigger("validate");
            }
            $("#hdType").trigger('validate');
            $("#workTypes").trigger('validate');
            if (!self.validate()) { return; }
            if (nts.uk.ui.errors.hasError()) { return; }
            nts.uk.ui.block.invisible();
            if(self.holidayTypeCode() != 0){
                self.registerApp();
            }else{
                let setNo65 = {
                                    pridigCheck: self.settingNo65() == null ? 0 : self.settingNo65().pridigCheck,
                                    subVacaManage: self.settingNo65() == null ? false : self.settingNo65().subVacaManage,
                                    subVacaTypeUseFlg: self.settingNo65() == null ? false : self.settingNo65().subVacaTypeUseFlg,
                                    subHdManage: self.settingNo65() == null ? false : self.settingNo65().subHdManage,
                                    subHdTypeUseFlg: self.settingNo65() == null ? false : self.settingNo65().subHdTypeUseFlg,
                }
                let paramCheck = {
                                    setNo65: setNo65,
                                    numberSubHd: self.numberSubHd(),//代休残数
                                    numberSubVaca: self.numberSubVaca()//振休残数
                                };
                //アルゴリズム「代休振休優先消化チェック」を実行する (Thực hiện thuật toán [check sử dụng độ ưu tiên nghi bù])
                service.checkRegister(paramCheck).done(()=>{
                    self.registerApp();
                }).fail((res)=>{
                    if (res.messageId == 'Msg_1392' || res.messageId == 'Msg_1394') {//エラーメッセージがある場合(t/h có error message)
                        dialog.alertError({ messageId: res.messageId }).then(function() {
                            nts.uk.ui.block.clear();
                            return;
                        });
                    } 
                    //確認メッセージがある場合(t/h có confirm message)
                    //確認メッセージを表示する (Hiển thị confirm message)
                    if(res.messageId == 'Msg_1393' || res.messageId == 'Msg_1395'){
                        dialog.confirm({ messageId: res.messageId }).ifYes(() => {
                            self.registerApp();
                        }).ifCancel(() => {
                            nts.uk.ui.block.clear();
                        });
                    }
                });
            }
        }
//        checkBfRegister(){
//            let self = this;
//             if(self.selectedTypeOfDuty() == null){
//                 $("#workTypes").trigger('validate');
//                return;
//            }
//            let comboBoxReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
//            let textAreaReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true); 
//            let paramCheck = {  appType: 1,
//                                prePostAtr: self.prePostSelected(),
//                                startDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
//                                endDate: nts.uk.util.isNullOrEmpty(self.endAppDate()) ? moment(self.startAppDate()).format(self.DATE_FORMAT) : moment(self.endAppDate()).format(self.DATE_FORMAT),
//                                employeeID: self.employeeID(),
//                                appReasonID: comboBoxReason,
//                                applicationReason: textAreaReason,
//                                overTimeAtr:0}
//            nts.uk.at.view.kaf000.a.service.checkBfRegister(paramCheck).done(()=>{
//                self.registerApp();
//            }).fail((res)=>{
//                if(res.messageId == "Msg_1518"){//confirm
//                    dialog.confirm({messageId: res.messageId}).ifYes(() => {
//                            self.registerApp();
//                        }).ifCancel(() => {
//                            nts.uk.ui.block.clear();
//                        });
//                }else{
//                    dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
//                        .then(function() {
//                            nts.uk.ui.block.clear();
//                        });
//                }
//            });
//        }
        registerApp(){
            let self = this;
            let comboBoxReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
            let textAreaReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), true); 
            let appReason: string;
            if (!appcommon.CommonProcess.checklenghtReason(comboBoxReason+":"+textAreaReason, "#appReason")) {
                return;
            }
            if (!self.changeWorkHourValueFlg()) {
                self.changeWorkHourValue(false);
                self.timeStart1(null);
                self.timeEnd1(null);
                self.timeStart2(null);
                self.timeEnd2(null);
                self.workTimeCode(null);
            }
            let specHd = null;
            if(self.holidayTypeCode() == 3 && self.fix()){
                specHd = {  relationCD: self.selectedRelation(),
                            mournerCheck: self.isCheck(),
                            relaReason: self.relaReason()
                        }
            }
            let paramInsert = {
                prePostAtr: self.prePostSelected(),
                startDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                endDate: nts.uk.util.isNullOrEmpty(self.endAppDate()) ? moment(self.startAppDate()).format(self.DATE_FORMAT) : moment(self.endAppDate()).format(self.DATE_FORMAT),
                employeeID: self.employeeID(),
                appReasonID: comboBoxReason,
                applicationReason: textAreaReason,
                holidayAppType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                workTypeCode: self.selectedTypeOfDuty(),
                workTimeCode: nts.uk.util.isNullOrEmpty(self.workTimeCode()) ? null : self.workTimeCode(),
                halfDayFlg: self.displayHalfDayValue(),
                changeWorkHour: self.changeWorkHourValue(),
                allDayHalfDayLeaveAtr: self.selectedAllDayHalfDayValue(),
                startTime1: self.timeStart1(),
                endTime1: self.timeEnd1(),
                startTime2: self.timeStart2(),
                endTime2: self.timeEnd2(),
                displayEndDateFlg: self.displayEndDateFlg(),
                specHd: specHd,
                checkOver1Year: true,
                checkContradiction: false
                
                
            };
            service.createAbsence(paramInsert).done((data) => {
                self.sendMail(data);
            }).fail((res) => {
                if(res.messageId == "Msg_1518"){//confirm
                    dialog.confirm({messageId: res.messageId}).ifYes(() => {
                        paramInsert.checkOver1Year = false;
                             service.createAbsence(paramInsert).done((data) => {
                                self.sendMail(data);
                            }).fail((res1) => {
								if (res1.messageId == "Msg_1520" || res1.messageId == "Msg_1522") {
			                        dialog.confirm({ messageId: res1.messageId, messageParams: res1.parameterIds }).ifYes(() => {
			                            paramInsert.checkContradiction = true;
			                            service.createAbsence(paramInsert).done((data) => {
			                                self.sendMail(data);
			                            }).fail((res2) => {
			                                dialog.alertError({ messageId: res2.messageId, messageParams: res2.parameterIds })
			                                    .then(function() { nts.uk.ui.block.clear(); });
			                            });
			                        }).ifNo(() => {
			                            nts.uk.ui.block.clear();
			                        });
			                    } else {
			                        dialog.alertError({ messageId: res1.messageId, messageParams: res1.parameterIds })
			                            .then(function() { nts.uk.ui.block.clear(); });
			                    }
                            });
                        }).ifNo(() => {
                            nts.uk.ui.block.clear();
                        });
                    
                }else{
                    if (res.messageId == "Msg_1520" || res.messageId == "Msg_1522") {
                        dialog.confirm({ messageId: res.messageId, messageParams: res.parameterIds }).ifYes(() => {
                            paramInsert.checkContradiction = true;
                            service.createAbsence(paramInsert).done((data) => {
                                self.sendMail(data);
                            }).fail((res1) => {
                                dialog.alertError({ messageId: res1.messageId, messageParams: res1.parameterIds })
                                    .then(function() { nts.uk.ui.block.clear(); });
                            });
                        }).ifNo(() => {
                            nts.uk.ui.block.clear();
                        });
                    } else {
                        dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                            .then(function() { nts.uk.ui.block.clear(); });
                    }
                }
            });    
        }
        sendMail(data){
            let self = this;
            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
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
        }
        getReason(inputReasonID: string, inputReasonList: Array<common.ComboReason>, detailReason: string): string {
            let appReason = '';
            let inputReason: string = '';
            if (!nts.uk.util.isNullOrEmpty(inputReasonID)) {
                inputReason = _.find(inputReasonList, o => { return o.reasonId == inputReasonID; }).reasonName;
            }
            if (!nts.uk.util.isNullOrEmpty(inputReason) && !nts.uk.util.isNullOrEmpty(detailReason)) {
                appReason = inputReason + ":" + detailReason;
            } else if (!nts.uk.util.isNullOrEmpty(inputReason) && nts.uk.util.isNullOrEmpty(detailReason)) {
                appReason = inputReason;
            } else if (nts.uk.util.isNullOrEmpty(inputReason) && !nts.uk.util.isNullOrEmpty(detailReason)) {
                appReason = detailReason;
            }
            return appReason;
        }
        btnSelectWorkTimeZone() {
            let self = this;
            self.getListWorkTime().done(() => {
                nts.uk.ui.windows.setShared('parentCodes', {
                    workTypeCodes: self.workTypecodes(),
                    selectedWorkTypeCode: self.selectedTypeOfDuty(),
                    workTimeCodes: self.workTimeCodes(),
                    selectedWorkTimeCode: self.workTimeCode()
                }, true);

                nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                    //view all code of selected item 
                    var childData = nts.uk.ui.windows.getShared('childData');
                    if (childData) {
                        self.selectedTypeOfDuty(childData.selectedWorkTypeCode);
                        self.workTimeCode(childData.selectedWorkTimeCode);
                        self.workTimeName(childData.selectedWorkTimeName);
                        self.displayWorkTimeName(childData.selectedWorkTimeCode + "　" + childData.selectedWorkTimeName);
                        service.getWorkingHours(
                            {
                                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                                workTypeCode: self.selectedTypeOfDuty(),
                                workTimeCode: self.workTimeCode()
                            }
                        ).done(data => {
                            if(nts.uk.util.isNullOrEmpty(data)){
                                self.timeStart1(childData.first.start);    
                                self.timeEnd1(childData.first.end);
                            } else {
                                if(nts.uk.util.isNullOrUndefined(data[0])){
                                    self.timeStart1(childData.first.start);    
                                    self.timeEnd1(childData.first.end);    
                                } else {
                                    self.timeStart1(data[0].startTime == null ? childData.first.start : data[0].startTime);
                                    self.timeEnd1(data[0].endTime == null ? childData.first.end : data[0].endTime);        
                                }
                            }
                        }).fail(() => {
                            self.timeStart1(childData.first.start);    
                            self.timeEnd1(childData.first.end);
                        });
                    }
                });
            });
        }
        changeDisplayWorkime() {
            let self = this;
            self.eblTimeStart1(self.changeWorkHourValue() && (self.displayWorkTimeName() != nts.uk.resource.getText('KAF006_21')));
            self.eblTimeEnd1(self.changeWorkHourValue() && (self.displayWorkTimeName() != nts.uk.resource.getText('KAF006_21')));
        }
        getListWorkTime() {
            let self = this;
            let dfd = $.Deferred();
            service.getListWorkTime({
                startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
            }).done((value) => {
                self.workTimeCodes(value);
                dfd.resolve(value);
            }).fail((res) => {
                dfd.reject(res);
            })
            return dfd.promise();
        }
        convertListHolidayType(data: any, checkDis: any) {
            let self = this;
            let lstHdName = [];
            self.holidayTypes([]);
            for (let i = 0; i < data.length; i++) {
                //ver new
                if(self.checkDisplay(checkDis, data[i].holidayAppTypeCode)){
                    lstHdName.push(new common.HolidayType(data[i].holidayAppTypeCode, data[i].holidayAppTypeName));
                }
            }
            self.holidayTypes(lstHdName);
        }
        checkDisplay(checkDis: any, hdType: any): boolean{
            if(checkDis == null){
                return true;
            }
            if(hdType == 0){
                return checkDis.yearManage;
            }
            if(hdType == 1){
                return checkDis.subHdManage;
            }
            if(hdType == 7){
                return checkDis.subVacaManage;
            }
            if(hdType == 4){
                return checkDis.retentionManage;
            }
            return true;
        }
        checkStartDate(): boolean {
            let self = this;
            if (!nts.uk.util.isNullOrEmpty(self.startAppDate())) {
                if (!self.displayEndDateFlg()) {
                    nts.uk.ui.errors.clearAll();
                    $('#inputdate').trigger("validate");
                    if (nts.uk.ui.errors.hasError()) { return true; }
                } else {
                    nts.uk.ui.errors.clearAll();
                    $('.ntsStartDatePicker').trigger("validate");
                    if (nts.uk.ui.errors.hasError()) { return true; }
                }
            }
            if(self.relaReason() != ''){
                $("#relaReason").trigger("validate");
            }
            return false;
        }
        private checkDisplayEndDate(data) {
            let self = this;
            if (data) {
                self.startAppDate(self.dateValue().startDate);
                self.endAppDate(self.dateValue().endDate);
            } else {
                self.startAppDate(self.appDate());
            }
        }
        validate(): boolean {
            let self = this;
            //勤務時間
            if (!nts.uk.util.isNullOrEmpty(self.timeStart1())) {
                if (!self.validateTime(self.timeStart1(), self.timeEnd1(), '#inpStartTime1')) {
                    return false;
                };
            }
            //            if ( !nts.uk.util.isNullOrEmpty(self.timeStart2()) && self.timeStart2() != "") {
            //                if ( !self.validateTime( self.timeStart2(), self.timeEnd2(), '#inpStartTime2' ) ) {
            //                    return false;
            //                };
            //            }   
            return true;
        }
        //Validate input time
        validateTime(startTime: number, endTime: number, elementId: string): boolean {
            if (startTime >= endTime) {
                dialog.alertError({ messageId: "Msg_307" })
                $(elementId).focus();
                return false;
            }
            return true;
        }

        /**
         * Jump to CMM018 Screen
         */
        openCMM018() {
            let self = this;
            nts.uk.request.jump("com", "/view/cmm/018/a/index.xhtml", { screen: 'Application', employeeId: self.employeeID });
        }
        
        //ver10
        /**
         * when click button A1_7: 年休参照ボタン
         */
        openKDL020(){
            let self = this;
            let lstid = [];
            _.each(self.employeeList(), function(emp){
                lstid.push(emp.id);
            });
            setShared('KDL020A_PARAM', { baseDate: moment(new Date()).toDate(), 
                                            employeeIds: lstid.length > 0 ? lstid : [self.employeeID()] } );
            modal('/view/kdl/020/a/index.xhtml')
        }
        /**
         * when click button A1_8: 積休参照ボタン
         */
        openKDL029(){
            let self = this;
            let lstid = [];
            _.each(self.employeeList(), function(emp){
                lstid.push(emp.id);
            });
            let param = {employeeIds: lstid.length > 0 ? lstid : [self.employeeID()],
                        baseDate: moment(new Date()).format("YYYY/MM/DD")}
            setShared('KDL029_PARAM', param);
            modal("/view/kdl/029/a/index.xhtml");
        }
        /**
         * when click button A1_3: 代休参照ボタン
         */
        openKDL005(){
            let self = this;
            let lstid = [];
            _.each(self.employeeList(), function(emp){
                lstid.push(emp.id);
            });
            let data = {employeeIds: lstid.length > 0 ? lstid : [self.employeeID()],
                        baseDate: moment(new Date()).format("YYYYMMDD")}
            setShared('KDL005_DATA', data);
            if(data.employeeIds.length > 1) {
                modal("/view/kdl/005/a/multi.xhtml");
            } else {
                modal("/view/kdl/005/a/single.xhtml");
            }
        }
    }

}

