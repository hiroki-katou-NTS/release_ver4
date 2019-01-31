module nts.uk.at.view.kmf022.l.viewmodel {
    import setShared =  nts.uk.ui.windows.setShared;
    import clear = nts.uk.ui.block.clear;
    import isNullOrEmpty = nts.uk.text.isNullOrEmpty;
    export class ScreenModel {
        //Screen mode
        screenMode: KnockoutObservable<ScreenMode> =  ko.observable(ScreenMode.INSERT);
        dateFormat: string = 'YYYY/MM/DD';
        listComponentOption: any;
        selectedCode: KnockoutObservable<string> = ko.observable('');
        alreadySettingList: KnockoutObservableArray<any>;
        employmentName: KnockoutObservable<string> = ko.observable('');
        workTypeList: Array<any>;
        appSetData: KnockoutObservable<PreBeforeAppSetData> = ko.observable(new PreBeforeAppSetData(''));
        alreadySettingData: Array<any>;
        codeStart: string = '';
        listWTShareKDL002: KnockoutObservableArray<any> = ko.observableArray([]);
        allowRegister: KnockoutObservableArray<boolean> = ko.observable(true);
        //previewData: any = null;
        //previewCode: string = "";
        //saveNotify:KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            let self = this;
            self.alreadySettingList = ko.observableArray([]);
            self.listComponentOption = {
                isShowAlreadySet: true,
                isMultiSelect: false,
                listType: ListType.EMPLOYMENT,
                selectType: SelectType.SELECT_BY_SELECTED_CODE,
                selectedCode: self.selectedCode,
                isDialog: false,
                isShowNoSelectRow: false,
                alreadySettingList: self.alreadySettingList,
                maxRows: 12
            };
            
            //Employment code change listener
            self.selectedCode.subscribe(value =>{
                nts.uk.ui.errors.clearAll();
                if(!isNullOrEmpty(value) && value != "undefined" && value != undefined){
                    self.allowRegister(true);
                    //Get employment name  
                    let employmentList: Array<UnitModel> = $('#empt-list-setting').getDataList();  
                    let selectedEmp = _.find(employmentList, { 'code': value });
                    if(!nts.uk.util.isNullOrUndefined(selectedEmp)){
                        self.employmentName(selectedEmp.name);
                    }else{
                        self.employmentName('');
                    }
                    //Get data setting            
                    self.changeEmploymentCode(value); 
                }else{
                    self.allowRegister(false);
                    self.employmentName('');
                    self.changeEmploymentCode(value);
                }
            });
        }
        start(): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            var self = this;
            var dfd = $.Deferred();  
            $('#empt-list-setting').ntsListComponent(self.listComponentOption).done(function () {  
                //Find work type list:
                service.findAllWorktype().done(data => {
                    self.workTypeList = data;
                    //Load data setting
                    self.reloadData().done(data => {
                        self.selectedCode(self.codeStart);
                        dfd.resolve();
                        clear();
                    }).fail((res) => {
                        dfd.reject();
                        clear();
                    });                    
                }).fail((res) => {
                    dfd.reject();
                    clear();
                });
            }).fail((res) => {
                dfd.reject();
                nts.uk.ui.dialog.alertError({messageId: res.messageId}).then(function(){ 
                    nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");  
                });
                clear();
            });   
            return dfd.promise();
        }
        reloadData(): JQueryPromise<any>{
            let self = this;
            var dfd = $.Deferred();
            service.findEmploymentSetByCid().done(data => {
                clear();
                //Find already setting list
                if(_.size(data)){
                    //Get Employment List.
                    let employmentList: Array<UnitModel> = $('#empt-list-setting').getDataList();
                    self.codeStart = employmentList[0].code;
                        let alreadyLst: Array<UnitModel> = _.filter(employmentList, 
                                            function(emp) {
                                                let foundEmployment = _.find(data, function(item:any) { return item.employmentCode === emp.code; });
                                                 return !nts.uk.util.isNullOrUndefined(foundEmployment); 
                                            });
                        self.alreadySettingList(_.map(alreadyLst, item => {
                                                    let alreadyList: UnitAlreadySettingModel = {code: item.code, isAlreadySetting: true};
                                                    return alreadyList;}));
                    //Store for preview process
                    self.alreadySettingData = data;
                    self.updateWorkTypeName();
                    dfd.resolve();
                }
            }).fail((res) => {
                dfd.reject();
                clear();
            });
            return dfd.promise();
        }
        updateWorkTypeName(){
            let self = this;
            //Update work type name
//            if(nts.uk.util.isNullOrUndefined(self.alreadySettingData) || nts.uk.util.isNullOrUndefined(self.workTypeList)){
//                return;
//            }
            self.alreadySettingData = _.map(self.alreadySettingData, function(i:any) {
                i.lstWorkType = _.map(i.lstWorkType, function(wkType:any) {
                    let foundEmployment = _.find(self.workTypeList, function(item:any) { return wkType.workTypeCode === item.workTypeCode; });
                    if(!nts.uk.util.isNullOrUndefined(foundEmployment)){
                        wkType.workTypeName = foundEmployment.name
                    } else {
                        wkType.workTypeName = wkType.workTypeCode + "マスタ未登録";
                    }
                    return wkType;
                    });
                   return i; 
                });
        }
        /**
         * Load & binding data
         */
        changeEmploymentCode(empCode: string){                         
            let self = this;
//            if(empCode === self.previewCode) return;
//            if(self.checkSaveChanged()){
//                 nts.uk.ui.dialog.confirm({ messageId: 'Msg_19' })
//                 .ifNo(function() { //Back to preview data
//                    self.selectedCode(self.previewCode);
//                    return;
//                });   
//             } 
            //return if no selected employment code
            if(nts.uk.util.isNullOrEmpty(empCode)){
                self.appSetData(new PreBeforeAppSetData(empCode));
                return;
            } 
            nts.uk.ui.block.invisible();
            self.screenMode(ScreenMode.INSERT);
            self.appSetData(new PreBeforeAppSetData(empCode));
            //Find employment code
            let data = _.filter(self.alreadySettingData, function(item:any) { return item.employmentCode === empCode; });
            
            if(data!= null && data.length > 0){
                //残業申請
                let overTimeData = _.filter(data, function(item:any) { return item.appType === ApplicationType.OVER_TIME_APPLICATION; });
                if(overTimeData!= null && overTimeData.length > 0){ 
                    self.initDataSetting(self.appSetData().overTimeSet(), overTimeData[0]);
                }
                 //休暇申請
                let absenceData = _.filter(data, function(item:any) { return item.appType === ApplicationType.ABSENCE_APPLICATION; });
                if(absenceData!= null && absenceData.length > 0){
                   self.initDataSettingWithListCode(self.appSetData().absenceSet(), absenceData);
                }             
                //2:勤務変更申請
                let workChangeData = _.filter(data, function(item:any) { return item.appType === ApplicationType.WORK_CHANGE_APPLICATION; });
                if(workChangeData!= null && workChangeData.length > 0){
                    self.initDataSetting(self.appSetData().workChangeSet(), workChangeData[0]);
                }
                //3:出張申請
                let businessTripData = _.filter(data, function(item:any) { return item.appType === ApplicationType.BUSINESS_TRIP_APPLICATION; });
                if(businessTripData!= null && businessTripData.length > 0){
                     self.initDataSetting(self.appSetData().businessTripSet(), businessTripData[0]);
                }
                //4:直行直帰申請
                let goReturndirectData = _.filter(data, function(item:any) { return item.appType === ApplicationType.GO_RETURN_DIRECTLY_APPLICATION; });
                if(goReturndirectData!= null && goReturndirectData.length > 0){
                    self.initDataSetting(self.appSetData().goReturndirectSet(), goReturndirectData[0]);
                }
                //6:休出時間申請
                let breakTimeData = _.filter(data, function(item:any) { return item.appType === ApplicationType.BREAK_TIME_APPLICATION; });
                if(breakTimeData!= null && breakTimeData.length > 0){
                    self.initDataSetting(self.appSetData().breakTimeSet(), breakTimeData[0]);
                }
                //7:打刻申請(打刻申請)
                let stampData = _.filter(data, function(item:any) { return item.appType === ApplicationType.STAMP_APPLICATION; });
                if(stampData!= null && stampData.length > 0){
                    self.initDataSetting(self.appSetData().stampSet(), stampData[0]);
                }
                //8:時間年休申請
                let annualHolidayData = _.filter(data, function(item:any) { return item.appType === ApplicationType.ANNUAL_HOLIDAY_APPLICATION; });
                if(annualHolidayData!= null && annualHolidayData.length > 0){
                    self.initDataSetting(self.appSetData().annualHolidaySet(), annualHolidayData[0]);
                }
                //9:遅刻早退取消申請
                let earlyLeaveData = _.filter(data, function(item:any) { return item.appType === ApplicationType.EARLY_LEAVE_CANCEL_APPLICATION; });
                if(earlyLeaveData!= null && earlyLeaveData.length > 0){
                    self.initDataSetting(self.appSetData().earlyLeaveSet(), earlyLeaveData[0]);
                }
                //10:振休振出申請        
                let complementLeaveData = _.filter(data, function(item:any) { return item.appType === ApplicationType.COMPLEMENT_LEAVE_APPLICATION; });
                if(complementLeaveData!= null && complementLeaveData.length > 0){
                    self.initDataSettingWithListCode(self.appSetData().complementLeaveSet(), complementLeaveData);
                }
                //11:打刻申請（NR形式）               
                let stampNRData = _.filter(data, function(item:any) { return item.appType === ApplicationType.STAMP_NR_APPLICATION; });
                if(stampNRData!= null && stampNRData.length > 0){
                    self.initDataSetting(self.appSetData().stampNRSet(), stampNRData[0]);
                }
                //14:３６協定時間申請                              
                let application36Data = _.filter(data, function(item:any) { return item.appType === ApplicationType.APPLICATION_36; });
                if(application36Data!= null && application36Data.length > 0){
                    self.initDataSetting(self.appSetData().application36Set(), application36Data[0]);
                }       
                self.screenMode(ScreenMode.UPDATE);
                //self.previewData = ko.mapping.toJS(self.appSetData());
                //self.previewCode = empCode;
            }
            clear();
        }
        /**
         * 登録処理
         */
        registerEmploymentSet(parent:any){
            nts.uk.ui.errors.clearAll();
            nts.uk.ui.block.grayout();
            let self = parent;      
            var dfd = $.Deferred(); 
            let code = self.selectedCode();
            let commands = [];
            if(!self.allowRegister()){
                clear();
                return;    
            }
            var overTimeSet = ko.mapping.toJS(self.appSetData().overTimeSet());
            // nếu chọn L6 mà không chọn L37 về thì báo error, nếu ko chọn L6 thì reset L37 về []
            if(overTimeSet.displayFlag === true && overTimeSet.displayWorkTypes === ""){
                clear();
                $('#l37').ntsError('set', {messageId:"Msg_1377", messageParams:['残業申請']});
            }
            if(!overTimeSet.displayFlag){overTimeSet.lstWorkType = [];}
            commands.push(overTimeSet);
             _.forEach(self.appSetData().absenceSet(), function(item: any) {
                 let noKo = ko.mapping.toJS(item);
                 let error = noKo.displayFlag === true && noKo.holidayTypeUseFlg == false && (!_.size(noKo.lstWorkType) || noKo.lstWorkType[0].workTypeCode == "") ? true : false;
                 if(error == true && noKo.optionName === '【年次有休】'){
                     clear();
                     $('.lagre-input-code:eq(1)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【年次有休】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【年次有休】') {
                     $('.lagre-input-code:eq(1)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                     clear();
                 }
                 else if(error == true && noKo.optionName === '【代休】'){
                     clear();
                     $('.lagre-input-code:eq(2)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【代休】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【代休】') {
                         $('.lagre-input-code:eq(2)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                         clear();
                     }
                 else if(error == true && noKo.optionName === '【欠勤】'){
                     clear();
                     $('.lagre-input-code:eq(3)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【欠勤】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【欠勤】') {
                         $('.lagre-input-code:eq(3)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                         clear();
                     }
                 else if(error == true && noKo.optionName === '【特別休暇】'){
                     clear();
                     $('.lagre-input-code:eq(4)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【特別休暇】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【特別休暇】') {
                         $('.lagre-input-code:eq(4)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                         clear();
                     }
                 else if(error == true && noKo.optionName === '【積立年休】'){
                     clear();
                     $('.lagre-input-code:eq(5)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【積立年休】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【積立年休】') {
                     $('.lagre-input-code:eq(5)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                     clear();
                 }
                 else if(error == true && noKo.optionName === '【休日】'){
                     clear();
                     $('.lagre-input-code:eq(6)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【休日】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【休日】') {
                     $('.lagre-input-code:eq(6)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                     clear();
                 }
                 else if(error == true && noKo.optionName === '【時間消化】'){
                     clear();
                     $('.lagre-input-code:eq(7)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【時間消化】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【時間消化】') {
                     $('.lagre-input-code:eq(7)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                     clear();
                 }
                 else if(error == true && noKo.optionName === '【振休】'){
                     clear();
                     $('.lagre-input-code:eq(8)').ntsError('set', {messageId:"Msg_1378", messageParams:['休暇申請', '【振休】']});
                 }
                 else if (!noKo.displayFlag && noKo.optionName === '【振休】') {
                     $('.lagre-input-code:eq(8)').ntsError('clear');
                     noKo.holidayTypeUseFlg = false;
                         noKo.lstWorkType = [];
                     clear();
                 }
                commands.push(noKo);     
            });  
            let wSet = ko.mapping.toJS(self.appSetData().workChangeSet());
            commands.push(wSet);
            if(wSet.displayFlag === true && wSet.displayWorkTypes === ""){
                clear();
                $('#l46').ntsError('set', {messageId:"Msg_1377", messageParams:['勤務変更申請']});
            }
            else if(wSet.displayFlag === false){
                wSet.lstWorkType = [];
            }
            commands.push(ko.mapping.toJS(self.appSetData().businessTripSet()));
            
            let returnDirect = ko.mapping.toJS(self.appSetData().goReturndirectSet());
            commands.push(returnDirect);
            if(returnDirect.displayFlag === true && returnDirect.displayWorkTypes === ""){
                clear();
                $('#l48').ntsError('set', {messageId:"Msg_1377", messageParams:['直行直帰申請']});
            }
            else if(returnDirect.displayFlag === false){
                returnDirect.lstWorkType = [];
            }
            
            let breakTime = ko.mapping.toJS(self.appSetData().breakTimeSet());
            commands.push(breakTime);
            if(breakTime.displayFlag === true && breakTime.displayWorkTypes === ""){
                clear();
                $('#l49').ntsError('set', {messageId:"Msg_1377", messageParams:['休出時間申請']});
            }
            else if(breakTime.displayFlag === false){
                breakTime.lstWorkType = [];
            }            
            
            commands.push(ko.mapping.toJS(self.appSetData().stampSet()));
            commands.push(ko.mapping.toJS(self.appSetData().annualHolidaySet()));
            commands.push(ko.mapping.toJS(self.appSetData().earlyLeaveSet()));
            _.forEach(self.appSetData().complementLeaveSet(), function(item: any) { 
                let leave = ko.mapping.toJS(item);
                commands.push(leave);
                if(leave.displayFlag === true && leave.optionName === '【振出】' && (!_.size(leave.lstWorkType) || leave.lstWorkType[0].workTypeCode == "")){
                    clear();
                    $('.lagre-input-code:eq(12)').ntsError('set', {messageId:"Msg_1378", messageParams:['振休振出申請', '【振出】']});
                 }
                else if (leave.displayFlag === false && leave.optionName === '【振出】') {
                    leave.lstWorkType = [];
                }  
                if(leave.displayFlag === true && leave.optionName === '【振休】' && (!_.size(leave.lstWorkType) || leave.lstWorkType[0].workTypeCode == "")){
                    clear();
                    $('.lagre-input-code:eq(13)').ntsError('set', {messageId:"Msg_1378", messageParams:['振休振出申請', '【振休】']});
                 }
                else if (leave.displayFlag === false && leave.optionName === '【振休】') {
                    leave.lstWorkType = [];
                }
            });            
            commands.push(ko.mapping.toJS(self.appSetData().stampNRSet()));
            commands.push(ko.mapping.toJS(self.appSetData().application36Set()));
            if (nts.uk.ui.errors.hasError() === false) {
                if (self.screenMode() === ScreenMode.INSERT) {
                    service.addEmploymentSet(commands).done(() => {
                        //マスタリストを更新。マスタ設定済みとする 
                        //let alreadyList: UnitAlreadySettingModel = {code: self.selectedCode(), isAlreadySetting: true};
                        //self.alreadySettingList.push(alreadyList);
                        //self.alreadySettingList.valueHasMutated();
                        self.screenMode(ScreenMode.UPDATE);
                        //情報メッセージ（Msg_15）を表示する
                        clear();
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                            //Load data setting
                            self.start().done(() => {
                                self.selectedCode("");
                                self.selectedCode(code);
                            });
                        });
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                            clear();
                        });
                    });
                } else {
                    service.updateEmploymentSet(commands).done(() => {
                        //情報メッセージ（Msg_15）を表示する
                        clear();
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                            //Load data setting
                            self.start().done(() => {
                                self.selectedCode("");
                                self.selectedCode(code);
                            });
                        });
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                            clear();
                        });
                    });
                }
            }
        }
        /**
         * 申請の前準備の削除処理  
         */
        deleteEmploymentSet(parent:any){
            let self = parent;
            ////画面を更新モードにする  
            if (self.screenMode() === ScreenMode.UPDATE) {
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                    nts.uk.ui.block.invisible();
                    let command = ko.mapping.toJS(self.appSetData().overTimeSet());
                    service.deleteEmploymentSet(command).done(() => {
                        clear();
                        nts.uk.ui.dialog.info({ messageId: 'Msg_16' }).then(function() {
                            //Remove already setting data
                            self.alreadySettingList.remove( function (item) { return item.code === self.selectedCode(); } );
                            self.alreadySettingList.valueHasMutated();
                            //Remove model data
                            self.appSetData(new PreBeforeAppSetData(self.selectedCode()));
                            //Remove DB data
                            _.remove(self.alreadySettingData, function(currentData : any) {
                                        return currentData.employmentCode === self.selectedCode();
                                    });
                            //Change screen mode
                            self.screenMode(ScreenMode.INSERT);
                        });
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                            clear();
                        });
                    }); 
                }).ifNo(function() {
                    clear();
                });
            } else {
                clear();
            }
        }
        /**
         * 申請の前準備を複写する
         */
        copyEmploymentSet(parent:any){
            nts.uk.ui.block.invisible();
            let self = parent;
            if(self.screenMode() === ScreenMode.UPDATE){
                let listSetting = [];
                listSetting = _.map(self.alreadySettingList(), function(item: any) {return item.code;});
                //CDL023：複写ダイアログを起動する
                let param = {
                    code: self.selectedCode(),
                    name: self.employmentName(),
                    targetType: 1,// 雇用
                    itemListSetting: listSetting,
                    baseDate: moment(new Date()).format(self.dateFormat)
                };
                setShared("CDL023Input", param);
                nts.uk.ui.windows.sub.modal("com", "/view/cdl/023/a/index.xhtml").onClosed(() => {
                    nts.uk.ui.block.invisible();
                    let data = nts.uk.ui.windows.getShared("CDL023Output");
                    if (!nts.uk.util.isNullOrUndefined(data)){
                        //check overide mode
                        let isOveride: boolean = false;
                        let alreadyLst: Array<any> = _.filter(data, 
                                                function(emp) {
                                                    let foundEmployment = _.find(self.alreadySettingList(), function(item:any) { return item.code === emp; });
                                                     return !nts.uk.util.isNullOrUndefined(foundEmployment); 
                                                });
                        //Overide mode
                        if(!nts.uk.util.isNullOrUndefined(alreadyLst) && alreadyLst.length > 0){ isOveride = true;}
                        let command = {
                            targetEmploymentCodes:data,
                            overide: isOveride,
                            employmentCode: self.selectedCode()};
                        service.copyEmploymentSet(command).done(() => {
                            //情報メッセージ（Msg_15）を表示する
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                                //複写の場合は、複写先の数がわからないので、画面の初期表示処理を実行する
                                self.reloadData(); 
                                clear();
                            });
                        }).fail(function(res: any) {
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                                clear();
                            });
                        }); 
                    }
                    clear();
                });
            }else{
                clear();
            }
        }
        // get work type if apptype = 0
        findOtKaf022(): JQueryPromise<any>{
            let self = this;
            var dfd = $.Deferred();
            service.findOtKaf022().done(data => {
                //Find already setting list
                if(_.size(data)){
                    self.listWTShareKDL002(data);
                    dfd.resolve();
                }
            }).fail((res) => {
                dfd.reject();
                clear();
            });
            return dfd.promise();
        }
        // get work type if apptype = 1
        findAbsenceKaf022(absenceKAF022: any): JQueryPromise<any>{
            let self = this;
            var dfd = $.Deferred();
            service.findAbsenceKaf022(absenceKAF022).done(data => {
                //Find already setting list
                if(_.size(data)){
                    self.listWTShareKDL002(data);
                    dfd.resolve();
                }else{
                    self.listWTShareKDL002([]);
                    dfd.resolve();
                }
            }).fail((res) => {
                dfd.reject();
                clear();
            });
            return dfd.promise();
        }
        // get work type if app type = 2
        findWkChangeKaf022(): JQueryPromise<any>{
            let self = this;
            var dfd = $.Deferred();
            service.findWkChangeKaf022().done(data => {
                //Find already setting list
                if(_.size(data)){
                    self.listWTShareKDL002(data);
                    dfd.resolve();
                }
            }).fail((res) => {
                dfd.reject();
                clear();
            });
            return dfd.promise();
        }
        
        // get work type if app type = 4
        findBounceKaf022(): JQueryPromise<any>{
            let self = this;
            var dfd = $.Deferred();
            let haplfDay = [11, 7, 2, 0, 4, 5, 6, 9];
            service.findBounceKaf022(haplfDay).done(data => {
                //Find already setting list
                if(_.size(data)){
                    self.listWTShareKDL002(data);
                    dfd.resolve();
                }
            }).fail((res) => {
                dfd.reject();
                clear();
            });
            return dfd.promise();
        }
        
        // get work type if app type = 6
        findHdTimeKaf022(): JQueryPromise<any>{
            let self = this;
            var dfd = $.Deferred();
            service.findHdTimeKaf022().done(data => {
                //Find already setting list
                if(_.size(data)){
                    self.listWTShareKDL002(data);
                    dfd.resolve();
                }
            }).fail((res) => {
                dfd.reject();
                clear();
            });
            return dfd.promise();
        }
        
        // get work type if app type = 10
        findHdShipKaf022(hdShip): JQueryPromise<any>{
            let self = this;
            var dfd = $.Deferred();
            service.findHdShipKaf022(hdShip).done(data => {
                //Find already setting list
                if(_.size(data)){
                    self.listWTShareKDL002(data);
                    dfd.resolve();
                }
            }).fail((res) => {
                dfd.reject();
                clear();
            });
            return dfd.promise();
        }
        
        /**  
         * KDL002-勤務種類選択（ダイアログ）を起動する
         */
        openKDL002Dialog(itemSet: DataSetting) {
            nts.uk.ui.block.grayout();
            let self = this;
            var dfd = $.Deferred();
            nts.uk.ui.errors.clearAll();
            let workTypeCodes = [];
            let selectedWorkTypes = [];
//            workTypeCodes = _.map(self.workTypeList, function(item: any) { return item.workTypeCode; });
            selectedWorkTypes = _.map(itemSet.lstWorkType(), function(item: any) { return item.workTypeCode; });
            setShared('KDL002_Multiple', true);
            setShared('KDL002_SelectedItemId', selectedWorkTypes);
            if(itemSet.appType == 0){
                self.findOtKaf022().done(() => {
                    workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                    setShared('KDL002_AllItemObj', workTypeCodes);  
                    dfd.resolve();   
                });
            }
            if(itemSet.appType == 1){
                if(itemSet.optionName() == "【年次有休】"){
                    let absenceKAF022 = {
                        oneDayAtr: 2,
                        morningAtr: 2,
                        afternoonAtr: 2,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
                else if(itemSet.optionName() == "【代休】"){
                    let absenceKAF022 = {
                        oneDayAtr: 6,
                        morningAtr: 6,
                        afternoonAtr: 6,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    })
                }
                else if(itemSet.optionName() == "【欠勤】"){
                    let absenceKAF022 = {
                        oneDayAtr: 5,
                        morningAtr: 5,
                        afternoonAtr: 5,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    }).fail(() => {
                        return;    
                    });
                }
                else if(itemSet.optionName() == "【特別休暇】"){
                    let absenceKAF022 = {
                        oneDayAtr: 4,
                        morningAtr: 4,
                        afternoonAtr: 4,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = [];
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
                else if(itemSet.optionName() == "【積立年休】"){
                    let absenceKAF022 = {
                        oneDayAtr: 3,
                        morningAtr: 3,
                        afternoonAtr: 3,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = [];
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
                else if(itemSet.optionName() == "【休日】"){
                    let absenceKAF022 = {
                        oneDayAtr: 1,
                        morningAtr: 1,
                        afternoonAtr: 1,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = [];
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
                else if(itemSet.optionName() == "【時間消化】"){
                    let absenceKAF022 = {
                        oneDayAtr: 9,
                        morningAtr: 9,
                        afternoonAtr: 9,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = [];
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
                else if(itemSet.optionName() == "【振休】"){
                    let absenceKAF022 = {
                        oneDayAtr: 8,
                        morningAtr: 8,
                        afternoonAtr: 8,
                    }
                    self.findAbsenceKaf022(absenceKAF022).done(() => {
                        workTypeCodes = [];
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
            }
            if(itemSet.appType == 2){
                self.findWkChangeKaf022().done(() => {
                    workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                    setShared('KDL002_AllItemObj', workTypeCodes);  
                    dfd.resolve();   
                });
            }
            if(itemSet.appType == 4){
                self.findBounceKaf022().done(() => {
                    workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                    setShared('KDL002_AllItemObj', workTypeCodes);  
                    dfd.resolve();   
                });
            }
            if(itemSet.appType == 6){
                self.findHdTimeKaf022().done(() => {
                    workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                    setShared('KDL002_AllItemObj', workTypeCodes);  
                    dfd.resolve();   
                });
            }
            
            if(itemSet.appType == 10){
                if(itemSet.optionName() === "【振出】"){
                    let hdShip = {
                        oneDayAtr: 7, 
                        morningAtr2: 7, 
                        afternoon2: [1, 2, 3, 4, 5, 6, 8, 9], 
                        morningAtr3: [1, 2, 3, 4, 5, 6, 8, 9],
                        afternoon3: 7,
                        morningAtr4: 7, 
                        afternoon4: [0], 
                        morningAtr5: [0], 
                        afternoon5: 7,
                    }
                    self.findHdShipKaf022(hdShip).done(() => {
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
                if(itemSet.optionName() === "【振休】"){
                    let hdShip = {
                        oneDayAtr: 8, 
                        morningAtr2: 8, 
                        afternoon2: [0], 
                        morningAtr3: [0],
                        afternoon3: 8,
                        morningAtr4: 8, 
                        afternoon4: [1, 2, 3, 4, 5, 6, 8, 9], 
                        morningAtr5: [1, 2, 3, 4, 5, 6, 8, 9], 
                        afternoon5: 8,
                    }
                    self.findHdShipKaf022(hdShip).done(() => {
                        workTypeCodes = _.map(self.listWTShareKDL002(), function(item: any) { return item.workTypeCode; });
                        setShared('KDL002_AllItemObj', workTypeCodes);  
                        dfd.resolve(); 
                    });
                }
            }

            nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml').onClosed(function(): any {                
                let data = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                let newSelectedCodes = [];
                if(data != null && data.length > 0){
                    _.forEach(data, function(item: any) {
                        let newSelectedCode = {companyID: itemSet.companyId, employmentCode: itemSet.employmentCode, appType: itemSet.appType, holidayOrPauseType: itemSet.holidayOrPauseType, workTypeCode: item.code, workTypeName: item.name};
                        newSelectedCodes.push(newSelectedCode);
                    });
                    itemSet.lstWorkType(newSelectedCodes);
                }
                clear();  
            });
        }
//        checkSaveChanged() : boolean{
//            let self = this;
//            if(nts.uk.util.isNullOrUndefined(self.previewData)) return false;
//            if(self.previewData.overTimeSet.holidayTypeUseFlg != self.appSetData().overTimeSet().holidayTypeUseFlg()) return true;
//            if(self.previewData.overTimeSet.displayWorkTypes != self.appSetData().overTimeSet().displayWorkTypes()) return true;
//            return false;
//        }
//        listenerSaveNotify(){
//            let self = this;
//            self.appSetData().overTimeSet().holidayTypeUseFlg.subscribe((value) =>{
//                if(self.previewData.overTimeSet.holidayTypeUseFlg != value) self.saveNotify(true);
//            });
//            self.appSetData().overTimeSet().displayWorkTypes.subscribe((value) =>{
//                if(self.previewData.overTimeSet.displayWorkTypes != value) self.saveNotify(true);
//            });
//        }
        initDataSetting(itemSet, data){
            itemSet.companyId = data.companyID;
            itemSet.employmentCode = data.employmentCode;
            itemSet.appType = data.appType;
            itemSet.holidayOrPauseType = data.holidayOrPauseType;
            itemSet.displayFlag(data.displayFlag);
            itemSet.holidayTypeUseFlg(data.holidayTypeUseFlg);
            itemSet.lstWorkType(data.lstWorkType);
        }        
        initDataSettingWithListCode(itemSet, datas:any[]){
            let self = this;
            _.forEach(datas, function(item) {
               let index:number;
                index = _.findIndex(itemSet, function(o:any) { return o.holidayOrPauseType == item.holidayOrPauseType; });
                if(index >= 0){
                    itemSet[index].employmentCode = item.employmentCode;
                    itemSet[index].appType = item.appType;
                    itemSet[index].holidayOrPauseType = item.holidayOrPauseType;
                    itemSet[index].displayFlag(item.displayFlag);
                    itemSet[index].holidayTypeUseFlg(item.holidayTypeUseFlg);
                    itemSet[index].lstWorkType(item.lstWorkType);
                }
            });            
        }
        
    }
    export class ListType {
        static EMPLOYMENT = 1;
        static Classification = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }

    export interface UnitModel {
        code: string;
        name?: string;
        workplaceName?: string;
        isAlreadySetting?: boolean;
    }

    export class SelectType {
        static SELECT_BY_SELECTED_CODE = 1;
        static SELECT_ALL = 2;
        static SELECT_FIRST_ITEM = 3;
        static NO_SELECT = 4;
    }

    export interface UnitAlreadySettingModel {
        code: string;
        isAlreadySetting: boolean;
    }
    
    export class ScreenMode {
        //新規モード
        static INSERT = 0;
        //更新モード
        static UPDATE = 1;
    }
    
    export class PreBeforeAppSetData{
        //0:残業申請
        overTimeSet: KnockoutObservable<DataSetting>;
        //1:休暇申請
        absenceSet: KnockoutObservableArray<DataSetting>;
        //2:勤務変更申請
        workChangeSet: KnockoutObservable<DataSetting>;
        //3:出張申請
        businessTripSet: KnockoutObservable<DataSetting>;
        //4:直行直帰申請
        goReturndirectSet: KnockoutObservable<DataSetting>;
        //6:休出時間申請
        breakTimeSet: KnockoutObservable<DataSetting>;
        //7:打刻申請(打刻申請)
        stampSet: KnockoutObservable<DataSetting>;
        //8:時間年休申請
        annualHolidaySet: KnockoutObservable<DataSetting>;
        //9:遅刻早退取消申請
        earlyLeaveSet: KnockoutObservable<DataSetting>;
        //10:振休振出申請
        complementLeaveSet: KnockoutObservableArray<DataSetting>;
        //11:打刻申請（NR形式）
        stampNRSet: KnockoutObservable<DataSetting>;
        //14:３６協定時間申請
        application36Set: KnockoutObservable<DataSetting>;
        constructor(employmentCode: string){
            let self = this;
            this.overTimeSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.OVER_TIME_APPLICATION, 9)); 
            this.absenceSet = self.initDefaultAbsenceSet(employmentCode); 
            this.workChangeSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.WORK_CHANGE_APPLICATION, 9)); 
            this.businessTripSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.BUSINESS_TRIP_APPLICATION, 9)); 
            this.goReturndirectSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.GO_RETURN_DIRECTLY_APPLICATION, 9)); 
            this.breakTimeSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.BREAK_TIME_APPLICATION, 9)); 
            this.stampSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.STAMP_APPLICATION, 9)); 
            this.annualHolidaySet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.ANNUAL_HOLIDAY_APPLICATION, 9)); 
            this.earlyLeaveSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.EARLY_LEAVE_CANCEL_APPLICATION, 9)); 
            this.complementLeaveSet = self.initDefaultComplementLeaveSet(employmentCode); 
            this.stampNRSet = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.STAMP_NR_APPLICATION, 9)); 
            this.application36Set = ko.observable(self.initDefauleDataSetting(employmentCode, ApplicationType.APPLICATION_36, 9)); 
        }        
        initDefauleDataSetting(employmentCode: string, appType: number, holidayOrPauseType: number) : DataSetting{
            let workTypeData = {
                    appType: appType,
                    companyID: '',
                    employmentCode: employmentCode,
                    holidayOrPauseType: holidayOrPauseType,
                    workTypeCode: '',
                };
            return new DataSetting('', employmentCode, appType, holidayOrPauseType, false, null, [workTypeData]);
        }
        initDefaultAbsenceSet(employmentCode: string): KnockoutObservableArray<DataSetting>{
            let self = this,
            absenceSet: KnockoutObservableArray<DataSetting> = ko.observableArray([]);
            for (let i = 0; i < 8; i++) {
                let resId: number = 47 + i;
                let dataSetting = self.initDefauleDataSetting(employmentCode, ApplicationType.ABSENCE_APPLICATION, i);
                if (dataSetting.displayFlag) {
                   dataSetting.holidayTypeUseFlg(false);
                }
                dataSetting.optionName(nts.uk.resource.getText('KAF022_'+ resId));
                absenceSet.push(dataSetting);
            }            
            return absenceSet;
        }
        initDefaultComplementLeaveSet(employmentCode: string): KnockoutObservableArray<DataSetting>{
            let self = this,
            complementLeaveSet: KnockoutObservableArray<DataSetting> = ko.observableArray([]);
            let resId:Array<string> = ["KAF022_279", "KAF022_54"];
            for (let i = 0; i < 2; i++) {
                
                let dataSetting = self.initDefauleDataSetting(employmentCode, ApplicationType.COMPLEMENT_LEAVE_APPLICATION, (resId.length - (i + 1)));
                dataSetting.optionName(nts.uk.resource.getText(resId[i]));
                complementLeaveSet.push(dataSetting);
            }            
            return complementLeaveSet;
        }
    }
    export class DataSetting{
        companyId: string;
        employmentCode: string;
        appType: number;
        holidayOrPauseType: number;
        displayFlag: KnockoutObservable<boolean> = ko.observable(false);
        holidayTypeUseFlg: KnockoutObservable<boolean> = ko.observable(null);
        lstWorkType: KnockoutObservableArray<any> = ko.observableArray();
        displayWorkTypes: KnockoutObservable<string> = ko.observable('');
        optionName: KnockoutObservable<string> = ko.observable('');
        enableButton: KnockoutObservable<boolean>;
        constructor(companyId: string, employmentCode: string, appType: number,holidayOrPauseType: number, displayFlag: boolean, holidayTypeUseFlg: boolean, lstWorkType: Array<any>){
            let self = this;
            this.enableButton = ko.observable((displayFlag == true && holidayTypeUseFlg == false) ? true : false);
            this.companyId = companyId;
            this.employmentCode = employmentCode;
            this.appType = appType;
            this.holidayOrPauseType = holidayOrPauseType;
            this.displayFlag(displayFlag);
            this.holidayTypeUseFlg(holidayTypeUseFlg);   
            this.lstWorkType(lstWorkType);
            this.lstWorkType.extend({ rateLimit: 50 });
            this.displayWorkTypes = ko.observable(lstWorkType.map(function(elem){
                                                        return elem.workTypeName;
                                                    }).join(" + "));
            this.lstWorkType.subscribe(value =>{
                    this.displayWorkTypes(_.map(this.lstWorkType(), item =>{return item.workTypeName;}).join(" + "));
            });
            this.holidayTypeUseFlg.subscribe(value => {
                if(value == false && this.displayFlag() == true){
                    this.enableButton(true);    
                }else{
                    this.enableButton(false);
                }
            });
            this.displayFlag.subscribe(obj => {
                this.holidayTypeUseFlg.valueHasMutated();    
            });
        }        
    }
     export interface IWorkTypeModal {
        workTypeCode: string;
        name: string;
        memo: string;
    }    
    export enum ApplicationType {
        /** 残業申請*/
        OVER_TIME_APPLICATION = 0,
        /** 休暇申請*/
        ABSENCE_APPLICATION = 1,
        /** 勤務変更申請*/
        WORK_CHANGE_APPLICATION = 2,
        /** 出張申請*/
        BUSINESS_TRIP_APPLICATION = 3,
        /** 直行直帰申請*/
        GO_RETURN_DIRECTLY_APPLICATION = 4,
        /** 休出時間申請*/
        BREAK_TIME_APPLICATION = 6,
        /** 打刻申請*/
        STAMP_APPLICATION = 7,
        /** 時間年休申請*/
        ANNUAL_HOLIDAY_APPLICATION = 8,
        /** 遅刻早退取消申請*/
        EARLY_LEAVE_CANCEL_APPLICATION = 9,
        /** 振休振出申請*/
        COMPLEMENT_LEAVE_APPLICATION = 10,
        /** 打刻申請（NR形式）*/
        STAMP_NR_APPLICATION = 11,
        /** 連続出張申請*/
        LONG_BUSINESS_TRIP_APPLICATION = 12,
        /** 出張申請オフィスヘルパー*/
        BUSINESS_TRIP_APPLICATION_OFFICE_HELPER = 13,
        /** ３６協定時間申請*/
        APPLICATION_36 = 14
    }

}