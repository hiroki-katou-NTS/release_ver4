module nts.uk.at.view.kaf006.b{
    import common = nts.uk.at.view.kaf006.share.common;
    import service = nts.uk.at.view.kaf006.shr.service;
    import dialog = nts.uk.ui.dialog;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import getText = nts.uk.resource.getText;
    export module viewmodel {
        export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
        DATE_FORMAT: string = "YYYY/MM/DD";
        //kaf000
        kaf000_a: kaf000.a.viewmodel.ScreenModel;
        manualSendMailAtr: KnockoutObservable<boolean> = ko.observable(true);
        screenModeNew: KnockoutObservable<boolean> = ko.observable(false);
        displayEndDateFlg : KnockoutObservable<boolean> = ko.observable(false);
        enableDisplayEndDate: KnockoutObservable<boolean> = ko.observable(false);
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
        appDate: KnockoutObservable<string> = ko.observable(moment().format(this.DATE_FORMAT));
        selectedAllDayHalfDayValue: KnockoutObservable<number> = ko.observable(0);
        holidayTypes: KnockoutObservableArray<common.HolidayType> = ko.observableArray([]);
        holidayTypeCode: KnockoutObservable<number> = ko.observable(0);
        typeOfDutys: KnockoutObservableArray<common.TypeOfDuty> = ko.observableArray([]);
        selectedTypeOfDuty:  KnockoutObservable<number> = ko.observable(null);
        displayHalfDayValue: KnockoutObservable<boolean> = ko.observable(false);
        changeWorkHourValue: KnockoutObservable<boolean> = ko.observable(false);
        changeWorkHourValueFlg: KnockoutObservable<boolean> = ko.observable(true);
//        displayChangeWorkHour:  KnockoutObservable<boolean> = ko.observable(false);
        displayStartFlg: KnockoutObservable<boolean> = ko.observable(true);
        contentFlg: KnockoutObservable<boolean> = ko.observable(true);
        eblTimeStart1:  KnockoutObservable<boolean> = ko.observable(false);
        eblTimeEnd1:  KnockoutObservable<boolean> = ko.observable(false);
        workTimeCodes: KnockoutObservableArray<string> = ko.observableArray([]);
        workTypecodes: KnockoutObservableArray<string> = ko.observableArray([]);
        displayWorkTimeName:  KnockoutObservable<string> = ko.observable(null);
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
        requiredReason : KnockoutObservable<boolean> = ko.observable(false);
        multilContent: KnockoutObservable<string> = ko.observable('');
  
        //Approval 
        approvalSource: Array<common.AppApprovalPhase> = [];
        employeeID : KnockoutObservable<string> = ko.observable('');
        //menu-bar 
        enableSendMail :KnockoutObservable<boolean> = ko.observable(true); 
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
        enbContentReason:  KnockoutObservable<boolean> = ko.observable(true);
        version: number = 0;
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
        //No.376
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
        displayTypicalReason: KnockoutObservable<boolean> = ko.observable(false);
        enableTypicalReason: KnockoutObservable<boolean> = ko.observable(false);
        displayReason: KnockoutObservable<boolean> = ko.observable(false);
        enableReason: KnockoutObservable<boolean> = ko.observable(false);
        displayReasonLst: Array<common.DisplayReason> = []; 
        //ver21
        relaResonDis: KnockoutObservable<boolean> = ko.observable(true);
        hdTypeDis: KnockoutObservable<boolean> = ko.observable(false);
        dataMax: KnockoutObservable<boolean> = ko.observable(false);
        appCur: any = null;
        constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata) {
            super(listAppMetadata, currentApp);
            let self = this;
            self.appCur = currentApp;
              self.startPage(self.appID()).done(function(){

                });   
            self.selectedRelation.subscribe(function(codeChange){
                if(codeChange === undefined || codeChange == null || codeChange.length == 0){
                    return;
                }
                service.changeRelaCD(self.selectedTypeOfDuty(), codeChange).done(function(data){
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
                    //ver21
                    let relaS = self.findRelaSelected(codeChange);
                    self.relaResonDis(relaS == undefined ? false : relaS.threeParentOrLess);
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
        startPage(appID: string): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            let self = this;
            let dfd = $.Deferred();
            service.findByAppID(appID).done((data) => {
                //No.376
                if(data.numberRemain != null){
                    if(data.numberRemain.yearRemain != null){//年休残数
                        self.yearRemain(data.numberRemain.yearRemain + '日');
                        self.yearDis(true);
                    }
                    if(data.numberRemain.subHdRemain != null){//代休残数
                        self.subHdRemain(data.numberRemain.subHdRemain + '日');
                        self.subHdDis(true);
                    }
                    if(data.numberRemain.subVacaRemain != null){//振休残数
                        self.subVacaRemain(data.numberRemain.subVacaRemain + '日');
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
                //find by change AllDayHalfDay
                self.selectedAllDayHalfDayValue.subscribe((value) => {
                    self.getChangeAllDayHalfDayForDetail(value);
                });
                // find change value A5_3
                self.displayHalfDayValue.subscribe((value) => {
                    self.findChangeDisplayHalfDay(value);
                });
                // change workType
                self.selectedTypeOfDuty.subscribe((value) => {
                    self.findChangeWorkType(value);
                    if(self.holidayTypeCode() == 3){
                        self.hdTypeDis(true);
                    }else{
                        self.hdTypeDis(false);
                    }
                });
                self.displayWorkTimeName.subscribe((value) => {
                    self.changeDisplayWorkime();
                });
                self.changeWorkHourValue.subscribe((value) => {
                    self.changeDisplayWorkime();
                    self.enbbtnWorkTime(value);
                });
                //hoatt 2018.08.09
                self.changeForSpecHd(data);
                dfd.resolve(); 
            })
            .fail(function(res) {
                if (res.messageId == 'Msg_426') {
                    dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.ui.block.clear();
                    });
                } else if (res.messageId == 'Msg_473') {
                    dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.ui.block.clear();
                    });
                } else {
                    nts.uk.ui.dialog.alertError(res.message).then(function() {
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
            nts.uk.ui.errors.clearAll();
            if(self.holidayTypeCode() == 3){
                if(data.specHdDto != null){
                    self.relaReason(data.specHdDto.relationshipReason);
                    self.isCheck(data.specHdDto.mournerFlag);
                    self.selectedRelation(data.specHdDto.relationshipCD);
                    if(!fix && self.relaReason() != ''){
                        $("#relaReason").ntsError('clear');
                    }
                }else{//data db k co
                    if(!fix){//th an clear rela reason
                        self.relaReason('');
                    }
                    $("#relaReason").ntsError('clear');
                    if(self.relaReason() != ''){
                        $("#relaReason").trigger("validate");
                    }
                }
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
        // change by switch button AllDayHalfDay(A3_12)
        getChangeAllDayHalfDayForDetail(value: any){
            let self = this;
            let dfd = $.Deferred();
            service.getChangeAllDayHalfDayForDetail({
                startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                endAppDate: nts.uk.util.isNullOrEmpty(self.endAppDate()) ? null : moment(self.endAppDate()).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                displayHalfDayValue: self.displayHalfDayValue(),
                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                alldayHalfDay: value
            }).done((result) =>{
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
                }else{
                    let a = [];
                    self.workTypecodes.removeAll();
                    for (let i = 0; i < result.workTypes.length; i++) {
                        a.push(new common.TypeOfDuty(result.workTypes[i].workTypeCode, result.workTypes[i].displayName));
                        self.workTypecodes.push(result.workTypes[i].workTypeCode);
                    }
                    self.typeOfDutys(a);
                    if (nts.uk.util.isNullOrEmpty(self.selectedTypeOfDuty)) {
                        self.selectedTypeOfDuty(result.workTypeCode);
                    }
                }
                if(!nts.uk.util.isNullOrEmpty(result.workTimeCodes)){
                    self.workTimeCodes.removeAll();
                    self.workTimeCodes(result.workTimeCodes);
                }
                 dfd.resolve(result);
            }).fail((res) =>{
                dfd.reject(res);
            });
             return dfd.promise();
        }
        // change by switch button DisplayHalfDay(A5_3)
        findChangeDisplayHalfDay(value: any){
            let self = this;
            let dfd = $.Deferred();
            service.getChangeDisplayHalfDay({
                startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                endAppDate: nts.uk.util.isNullOrEmpty(self.endAppDate()) ? null : moment(self.endAppDate()).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                displayHalfDayValue: self.displayHalfDayValue(),
                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                workTypeCode: self.selectedTypeOfDuty(),
                alldayHalfDay: self.selectedAllDayHalfDayValue()
            }).done((result) =>{
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
                }else{
                    let a = [];
                    self.workTypecodes.removeAll();
                    for (let i = 0; i < result.workTypes.length; i++) {
                        a.push(new common.TypeOfDuty(result.workTypes[i].workTypeCode, result.workTypes[i].displayName));
                        self.workTypecodes.push(result.workTypes[i].workTypeCode);
                    }
                    self.typeOfDutys(a);
                    if (nts.uk.util.isNullOrEmpty(self.selectedTypeOfDuty)) {
                        self.selectedTypeOfDuty(result.workTypeCode);
                    }
                }
                if(!nts.uk.util.isNullOrEmpty(result.workTimeCodes)){
                    self.workTimeCodes.removeAll();
                    self.workTimeCodes(result.workTimeCodes);
                }
                 dfd.resolve(result);
            }).fail((res) =>{
                dfd.reject(res);
            });
             return dfd.promise();
        }
        // change by workType
        findChangeWorkType(value: any){
            let self = this;
            let dfd = $.Deferred();
            if(self.selectedTypeOfDuty() == null || self.selectedTypeOfDuty() == undefined || self.selectedTypeOfDuty().length == 0){
                return;
            }
            service.getChangeWorkType({
                startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
                employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),
                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                workTypeCode: self.selectedTypeOfDuty(),
                workTimeCode: self.workTimeCode()
            }).done((result) =>{
                //hoatt 2018.08.09
                self.changeForSpecHd(result);
                self.changeWorkHourValueFlg(result.changeWorkHourFlg);
                if(result.startTime1 != null){
                    self.timeStart1(result.startTime1);    
                }
                if(result.endTime1 != null){
                    self.timeEnd1(result.endTime1);    
                }
                dfd.resolve(result);
            }).fail((res) =>{
                dfd.reject(res);
            });
             return dfd.promise();
        }
        initData(data: any){
            let self = this;
            _.forEach(data.displayReasonDtoLst, (o) => {
                self.displayReasonLst.push(new common.DisplayReason(o.typeOfLeaveApp, o.displayFixedReason==1?true:false, o.displayAppReason==1?true:false));     
            });
            self.version = data.application.version;
            self.manualSendMailAtr(data.manualSendMailFlg);
            self.employeeName(data.employeeName);
            self.employeeID(data.employeeID);
            self.prePostSelected(data.application.prePostAtr);
            self.convertListHolidayType(data.holidayAppTypeName);
            self.holidayTypeCode(data.holidayAppType);
            let currentDisplay = _.find(self.displayReasonLst, (o) => o.typeLeave==self.holidayTypeCode());
            if(nts.uk.util.isNullOrUndefined(currentDisplay)){
                self.typicalReasonDisplayFlg(false);
                self.displayAppReasonContentFlg(false);
            } else {
                self.typicalReasonDisplayFlg(currentDisplay.displayFixedReason);
                self.displayAppReasonContentFlg(self.typicalReasonDisplayFlg()||
                    (currentDisplay.displayAppReason)); 
                self.enbReasonCombo(currentDisplay.displayFixedReason);
                self.enbContentReason(currentDisplay.displayAppReason);
            }
            self.displayPrePostFlg(data.prePostFlg);
            self.requiredReason(data.appReasonRequire);
            self.workTimeCode(data.workTimeCode);
            self.displayWorkTimeName(nts.uk.util.isNullOrEmpty(data.workTimeCode) ? nts.uk.resource.getText('KAF006_21') : data.workTimeCode +"　"+ data.workTimeName);
            if(data.applicationReasonDtos != null && data.applicationReasonDtos.length > 0){
                let lstReasonCombo = _.map(data.applicationReasonDtos, o => { return new common.ComboReason(o.reasonID, o.reasonTemp); });
                self.reasonCombo(lstReasonCombo);
                let reasonID = data.applicationReasonDtos[0].reasonID;
                self.selectedReason(reasonID);
                
                self.multilContent(data.application.applicationReason);
            }
            self.workTimeCodes(data.workTimeCodes);
            if (!nts.uk.util.isNullOrEmpty(data.workTypes)) {
                for (let i = 0; i < data.workTypes.length; i++) {
                    self.typeOfDutys.push(new common.TypeOfDuty(data.workTypes[i].workTypeCode, data.workTypes[i].displayName));
                    self.workTypecodes.push(data.workTypes[i].workTypeCode);
                }
                self.selectedTypeOfDuty(data.workTypeCode);
            }
            self.changeWorkHourValueFlg(data.displayWorkChangeFlg);
            self.changeWorkHourValue(data.changeWorkHourFlg);
            self.selectedAllDayHalfDayValue(data.allDayHalfDayLeaveAtr);
            self.displayHalfDayValue(data.halfDayFlg);
            self.startAppDate(moment(data.application.applicationDate ).format(self.DATE_FORMAT));
            self.endAppDate(data.application.endDate);
            if(self.endAppDate() === self.startAppDate()){
                self.appDate(moment(data.application.applicationDate ).format(self.DATE_FORMAT));
            }else{
                let appDateAll = moment(data.application.applicationDate ).format(self.DATE_FORMAT) +"　"+ nts.uk.resource.getText('KAF005_38')　+"　"+  moment(data.application.endDate).format(self.DATE_FORMAT);
                self.appDate(appDateAll);
            }
            self.timeStart1(data.startTime1 == null ? null : data.startTime1);
            self.timeEnd1(data.endTime1 == null ? null : data.endTime1);
            if(data.holidayAppType == 3){
                self.hdTypeDis(true);
            }
            //rela specHdDto
            if(data.specHdDto != null && data.specHdDto !== undefined){
                self.relaRelaReason(data.specHdDto.relationshipReason);
                self.selectedRelation(data.specHdDto.relationshipCD);
            }
            if(data.initMode == 0){
                // display Mode
                self.enbAllDayHalfDayFlg(false);
                self.enbWorkType(false);
                self.enbHalfDayFlg(false);
                self.enbChangeWorkHourFlg(false);
                self.enbbtnWorkTime(false);
                self.eblTimeStart1(false);
                self.eblTimeEnd1(false);
                self.enbReasonCombo(false);
                self.enbContentReason(false);
                self.relaEnable(false);
                self.relaMourner(false);
                self.relaRelaReason(false);
            }else if(data.initMode == 1){
                self.relaEnable(true);
                self.relaMourner(true);
                self.relaRelaReason(true);
                // edit Mode
                self.enbAllDayHalfDayFlg(true);
                self.enbWorkType(true);
                self.enbHalfDayFlg(true);
                self.enbChangeWorkHourFlg(true);
                
                if(data.changeWorkHourFlg && !nts.uk.util.isNullOrEmpty(data.workTimeCode)){
                     self.eblTimeStart1(true);
                     self.eblTimeEnd1(true);
                    self.enbbtnWorkTime(true);
                }else{
                    self.eblTimeStart1(false);
                     self.eblTimeEnd1(false);
                    self.enbbtnWorkTime(false);
                }
            }
        }
         update(): JQueryPromise<any> {
             let self = this;
             $("#workTypes").trigger('validate');
             $("#relaReason").trigger("validate");
             if(self.holidayTypeCode() == 3 && self.fix()){
                $("#relaCD-combo").trigger("validate");
            }
             if (nts.uk.ui.errors.hasError()){return;} 
             nts.uk.ui.block.invisible();
             let comboBoxReason: string = appcommon.CommonProcess.getComboBoxReason(self.selectedReason(), self.reasonCombo(), self.typicalReasonDisplayFlg());
             let textAreaReason: string = appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), self.enbContentReason());
             let appReason: string;
             if (!appcommon.CommonProcess.checklenghtReason(comboBoxReason+":"+textAreaReason, "#appReason")) {
                 return;
             }
             let specHd = null;
            if(self.holidayTypeCode() == 3 && self.fix()){
                specHd = {  relationCD: self.selectedRelation(),
                            mournerCheck: self.isCheck(),
                            relaReason: self.relaReason()
                        }
            }
             let paramInsert = {
                version: self.version,
                appID: self.appID(),
                prePostAtr: self.prePostSelected(),
                startDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : self.startAppDate(),
                endDate:  nts.uk.util.isNullOrEmpty(self.endAppDate()) ? self.startAppDate() : self.endAppDate(),
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
                specHd: specHd
             };
             service.updateAbsence(paramInsert).done((data) =>{
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    if(data.autoSendMail){
                        appcommon.CommonProcess.displayMailResult(data);   
                    } else {
                        self.reBinding(self.listAppMeta, self.appCur, false);
                    }
                });
             }).fail((res) =>{
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
            return appcommon.CommonProcess.getTextAreaReason(self.multilContent(), self.displayAppReasonContentFlg(), self.enbContentReason());   
        }   
            
        resfreshReason(appReason: string){
            var self = this;
            self.selectedReason('');    
            self.multilContent(appReason);
        }
            
        getReason(inputReasonID: string, inputReasonList: Array<common.ComboReason>, detailReason: string): string{
            let appReason = '';
            let inputReason: string = '';
            if(!nts.uk.util.isNullOrEmpty(inputReasonID)){
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
        btnSelectWorkTimeZone(){
            let self = this;
            self.getListWorkTime().done(() =>{
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
//                        self.selectedWorkTypeCode(childData.selectedWorkTypeCode);
//                        self.workTypeName(childData.selectedWorkTypeName);
                        self.selectedTypeOfDuty(childData.selectedWorkTypeCode);
                        self.workTimeCode(childData.selectedWorkTimeCode);
                        self.workTimeName(childData.selectedWorkTimeName);
                        self.displayWorkTimeName(childData.selectedWorkTimeCode +"　"+childData.selectedWorkTimeName);
                        service.getWorkingHours(
                            {
                                holidayType: nts.uk.util.isNullOrEmpty(self.holidayTypeCode()) ? null : self.holidayTypeCode(),
                                workTypeCode: self.selectedTypeOfDuty(),
                                workTimeCode: self.workTimeCode()
                            }
                        ).done(data => {
                            self.timeStart1(data.startTime1 == null ? null : data.startTime1);
                            self.timeEnd1(data.endTime1 == null ? null : data.endTime1);
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
        getListWorkTime(){
            let self = this;
            let dfd = $.Deferred();
            service.getListWorkTime({
               startAppDate: nts.uk.util.isNullOrEmpty(self.startAppDate()) ? null : moment(self.startAppDate()).format(self.DATE_FORMAT),
               employeeID: nts.uk.util.isNullOrEmpty(self.employeeID()) ? null : self.employeeID(),    
            }).done((value) =>{
                self.workTimeCodes(value);
                dfd.resolve(value);
            }).fail((res) =>{
                dfd.reject(res);
            })
            return dfd.promise();
        }
        convertListHolidayType(data: any) {
            let self = this;
            for (let i = 0; i < data.length; i++) {
                self.holidayTypes.push(new common.HolidayType(data[i].holidayAppTypeCode, data[i].holidayAppTypeName));
            }
        }
        
        /**
         * Jump to CMM018 Screen
         */
        openCMM018(){
            let self = this;
            nts.uk.request.jump("com", "/view/cmm/018/a/index.xhtml", {screen: 'Application', employeeId: self.employeeID});  
        }
    }
    }    
}

