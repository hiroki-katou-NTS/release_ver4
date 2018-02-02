module nts.uk.at.view.kal003.b.viewmodel{
    import block = nts.uk.ui.block;
    import errors = nts.uk.ui.errors;
    import dialog = nts.uk.ui.dialog;
    import windows = nts.uk.ui.windows;
    import resource = nts.uk.resource;
    import sharemodel = nts.uk.at.view.kal003.share.model;
    import shareutils = nts.uk.at.view.kal003.share.kal003utils;

    export class ScreenModel {
        workRecordExtractingCondition: KnockoutObservable<sharemodel.WorkRecordExtractingCondition>;
        // list item check
        listTypeCheckWorkRecords    : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        listSingleValueCompareTypes : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        listRangeCompareTypes       : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        listCompareTypes            : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        itemListTargetServiceType_BA1_2         : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        itemListTargetSelectionRange_BA1_5         : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        listAllWorkType         : Array<string> = ([]);
        listAllAttdItem         : Array<string> = ([]);
        listAllWorkingTime  : Array<string> = ([]);

        displayWorkTypeSelections_BA1_4         : KnockoutObservable<string> = ko.observable('');
        displayAttendanceItemSelections_BA2_3   : KnockoutObservable<string> = ko.observable('');
        displayWorkingTimeSelections_BA5_3  : KnockoutObservable<string> = ko.observable('');
               
        private setting : sharemodel.WorkRecordExtractingCondition;
        swANDOR_B5_3 : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        swANDOR_B6_3 : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        swANDOR_B7_2 : KnockoutObservableArray<model.EnumModel> = ko.observableArray([]);
        enableComparisonMaxValue : KnockoutObservable<boolean> = ko.observable(false);
        comparisonRange : KnockoutObservable<model.ComparisonValueRange>;

        constructor() {
            let self = this;
            let option = windows.getShared('inputKal003b');
            self.setting = $.extend({}, shareutils.getDefaultWorkRecordExtractingCondition(0), option);
            
            let workRecordExtractingCond = shareutils.convertTransferDataToWorkRecordExtractingCondition(self.setting);
            self.workRecordExtractingCondition = ko.observable(workRecordExtractingCond);
            // setting comparison value range

            self.comparisonRange = ko.observable(self.initComparisonValueRange());
                
            // change select item check
            self.workRecordExtractingCondition().checkItem.subscribe((itemCheck) => {
                errors.clearAll();
                if ((itemCheck && itemCheck != undefined) || itemCheck === 0) {
                    self.initialScreen();
                }
            });
            self.comparisonRange().comparisonOperator.subscribe((operN) => {
                self.settingEnableComparisonMaxValueField();
            });
        }

        //initial screen
        start(): JQueryPromise<any> {
            
            let self = this,
                dfd = $.Deferred();
            errors.clearAll();
            self.getAllEnums().done(function() {
                //initial screen - in case update
                self.initialScreen().done(() => {
                    dfd.resolve();
                }).always(() => {
                    dfd.reject();
                });
           }).always(() => {
               dfd.reject();
           });
            return dfd.promise();
        }

        /**
         * setting Enable/Disable Comparison of Max Value Field
         */
        private settingEnableComparisonMaxValueField() {
            let self = this;
            self.enableComparisonMaxValue(
                self.workRecordExtractingCondition().errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon()[0].compareOperator() > 5);
        }

        private initComparisonValueRange() : model.ComparisonValueRange {
            let self = this;
            let erAlAtdItemCondition = self.workRecordExtractingCondition().errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon()[0];

            let comparisonValueRange;
            
            if (self.workRecordExtractingCondition().checkItem() == enItemCheck.Time          //時間
                || self.workRecordExtractingCondition().checkItem() == enItemCheck.Times      //回数
                || self.workRecordExtractingCondition().checkItem() == enItemCheck.AmountOfMoney //金額
                || self.workRecordExtractingCondition().checkItem() == enItemCheck.TimeOfDate    //時刻の場合
                || self.workRecordExtractingCondition().checkItem() == enItemCheck.CountinuousTime   //連続時間
                ){
                if (erAlAtdItemCondition.compareOperator() > 5
                    || erAlAtdItemCondition.conditionType() == ConditionType.FIXED_VALUE
                    ) {
                    comparisonValueRange = new model.ComparisonValueRange(
                        self.workRecordExtractingCondition().checkItem
                        , erAlAtdItemCondition.compareOperator
                        , erAlAtdItemCondition .compareStartValue()
                        , erAlAtdItemCondition.compareEndValue());
                } else {
                    comparisonValueRange = new model.ComparisonValueRange(
                        self.workRecordExtractingCondition().checkItem
                        , erAlAtdItemCondition.compareOperator
                        , erAlAtdItemCondition.singleAtdItem()
                        , erAlAtdItemCondition.singleAtdItem());
                }
            } else {
                comparisonValueRange = new model.ComparisonValueRange(
                        self.workRecordExtractingCondition().checkItem
                        , erAlAtdItemCondition.compareOperator
                        , 0
                        , 0);
            }
            return comparisonValueRange;
        }
        /**
         * initial screen
         */
        private initialScreen() : JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            self.initialDaily().done(() => {
                self.settingEnableComparisonMaxValueField();
                dfd.resolve();
            });
            return dfd.promise();
        }
        
        // ===========common begin ===================
        private getAllEnums() : JQueryPromise<any> {
            let self = this,
            dfd = $.Deferred();

            $.when(service.getEnumSingleValueCompareTypse(),
                    service.getEnumRangeCompareType(),
                    service.getEnumTypeCheckWorkRecord(),
                    service.getEnumTargetSelectionRange(),
                    service.getEnumTargetServiceType(),
                    service.getEnumLogicalOperator()).done((
                            listSingleValueCompareTypse : Array<model.EnumModel>,
                            lstRangeCompareType : Array<model.EnumModel>,
                            listTypeCheckWorkRecord : Array<model.EnumModel>,
                            listTargetSelectionRange : Array<model.EnumModel>,
                            listTargetServiceType : Array<model.EnumModel>,
                            listLogicalOperator : Array<model.EnumModel>) => {
                    self.listSingleValueCompareTypes(self.getLocalizedNameForEnum(listSingleValueCompareTypse));
                    self.listRangeCompareTypes(self.getLocalizedNameForEnum(lstRangeCompareType));
                    self.listTypeCheckWorkRecords(self.getLocalizedNameForEnum(listTypeCheckWorkRecord));
                    let listTargetRangeWithName = self.getLocalizedNameForEnum(listTargetSelectionRange);
                    self.itemListTargetSelectionRange_BA1_5(listTargetRangeWithName);
                    self.itemListTargetServiceType_BA1_2(self.getLocalizedNameForEnum(listTargetServiceType));
                    self.buildListCompareTypes();
                    let listANDOR = self.getLocalizedNameForEnum(listLogicalOperator)
                  //ENUM 論理演算子
                    self.swANDOR_B5_3 = ko.observableArray(listANDOR);
                    //ENUM 論理演算子
                    self.swANDOR_B6_3 = ko.observableArray(listANDOR);
                  //ENUM 論理演算子
                    self.swANDOR_B7_2 = ko.observableArray(listANDOR);
                    dfd.resolve();
               
            }).always(() => {
                dfd.resolve();
            });
            return dfd.promise();
        }
        
        /**
         * build List of Compare Types
         */
        private buildListCompareTypes() {
            let self = this;
            var listCompareTypes = self.listSingleValueCompareTypes().concat(self.listRangeCompareTypes());
            self.listCompareTypes(listCompareTypes);
        }
        
        /**
         * get Localize name by Enum
         * @param listEnum
         */
        private getLocalizedNameForEnum(listEnum : Array<model.EnumModel>) : Array<model.EnumModel> {
            if (listEnum) {
                for (var i = 0; i < listEnum.length; i++) {
                    if (listEnum[i].localizedName) {
                        listEnum[i].localizedName = resource.getText(listEnum[i].localizedName);
                    }
                }
                return listEnum;
            }
            return [];
        }

        /**
         * Initial Group Condition
         * @param listGroupCondition
         */
        private initGroupCondition(listGroupCondition : Array<sharemodel.ErAlAtdItemCondition>) : Array<sharemodel.ErAlAtdItemCondition> {
            let listCondition : Array<sharemodel.ErAlAtdItemCondition> = [];
            let maxRow = 3;
            if (listGroupCondition && listGroupCondition != undefined) {
                for(var i = 0; i < listGroupCondition.length && i < maxRow; i++) {
                    listGroupCondition[i].targetNO(i);
                    listCondition.push(listGroupCondition[i]);
                }
            }
            if (listCondition.length < maxRow) {
                for(var i = listCondition.length; i < maxRow; i++) {
                    listCondition.push(shareutils.getDefaultCondition(i-1));
                }
            }
            return listCondition;
        }
 
        /**
         * Initial Compound Group Condition
         */
        private initCompoundGroupCondition() {
            let self = this,
                errorAlarmCondition = self.workRecordExtractingCondition().errorAlarmCondition();
            let compoundCondition = errorAlarmCondition.atdItemCondition();
            if (!compoundCondition || compoundCondition == undefined) {
                compoundCondition = shareutils.getDefaultAttendanceItemCondition();
                errorAlarmCondition.atdItemCondition(compoundCondition);
            }
            let listGr1 = self.initGroupCondition(compoundCondition.group1().lstErAlAtdItemCon());
            compoundCondition.group1().lstErAlAtdItemCon(listGr1);
            let listGr2 = self.initGroupCondition(compoundCondition.group2().lstErAlAtdItemCon());
            compoundCondition.group2().lstErAlAtdItemCon(listGr2);
        }
     // ============build enum for combobox BA2-5: end ==============
        // ===========common end =====================
        //==========Daily section Begin====================
        /**
         * Initial Daily
         */
        private initialDaily() : JQueryPromise<any> {
            let self = this,
            dfd = $.Deferred();
            switch (self.workRecordExtractingCondition().checkItem()) {
                case enItemCheck.Time:          //時間
                case enItemCheck.Times:         //回数
                case enItemCheck.AmountOfMoney: //金額
                case enItemCheck.TimeOfDate:    //時刻の場合
                    self.initialDailyItemChkItemComparison();
                    dfd.resolve();
                    break;
                case enItemCheck.CountinuousTime:   //連続時間
                    self.initialDailyItemChkCountinuousTime();
                    dfd.resolve();
                    break;
                case enItemCheck.CountinuousWork:   //連続時間帯
                    self.initialDailyItemChkCountinuousWork();
                    dfd.resolve();
                    break;
                case enItemCheck.CountinuousTimeZone: //連続勤務
                    self.initialDailyItemChkCountinuousTimeZone();
                    dfd.resolve();
                    break;
                case enItemCheck.CompoundCondition: //複合条件
                    self.initCompoundGroupCondition();
                    dfd.resolve();
                    break;
                default:
                    
                    dfd.resolve();
                    break;
            }
            return dfd.promise();
        }
        
        /**
         * initial in case check item : Time, Times, Amount of money, Time of day
         */
        private initialDailyItemChkItemComparison() {
            let self = this,
            workRecordExtractingCondition = self.workRecordExtractingCondition();
            //ドメインモデル「日次の勤怠項目」を取得する - Acquire domain model "DailyAttendanceItem"
            service.getDailyItemChkItemComparison(workRecordExtractingCondition.checkItem()).done((itemAttendances : Array<any>) => {
                self.listAllAttdItem = self.getListAttendanceIdFromDtos(itemAttendances);
                
                // build name of Attendance Item
                let currentAtdItemCondition = workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon()[0];
                self.fillTextDisplayTarget(currentAtdItemCondition);
                /*
               let listAttendanceItemSelectedCode = self.getListAttendanceItemCode();//勤怠項目の加算減算式
                self.generateNameCorrespondingToAttendanceItem(listAttendanceItemSelectedCode).done((names) => {
                            self.displayAttendanceItemSelections_BA2_3(names);
                });
                */
                // initial default data of ErAlAtdItemCon
                //self.initialDataOfErAlAtdItemCon();
                //ドメインモデル「勤務種類」を取得する - Acquire domain model "WorkType"
                self.initialWorkTypes();
            });
        }
        
        /**
         * Initial in case Daily Item Check Continuous Time
         */
        private initialDailyItemChkCountinuousTime() {
            let self = this;
            //ドメインモデル「日次の勤怠項目」を取得する - Acquire domain model "DailyAttendanceItem"
            service.getAttendCoutinousTime().done((itemAttendances) => {
                self.listAllAttdItem = self.getListAttendanceIdFromDtos(itemAttendances);

                // build name of Attendance Item
                let currentAtdItemCondition = self.workRecordExtractingCondition().errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon()[0];
                self.fillTextDisplayTarget(currentAtdItemCondition);
                
                /*let listWorkTimeItemSelectedCode = self.getListAttendanceItemCode();//勤怠項目の加算減算式
                self.generateNameCorrespondingToAttendanceItem(listWorkTimeItemSelectedCode).done((names) => {
                    self.displayAttendanceItemSelections_BA2_3(names);
                });
                */
                // initial default data of ErAlAtdItemCon
                //self.initialDataOfErAlAtdItemCon();
                //ドメインモデル「勤務種類」を取得する - Acquire domain model "WorkType"
                self.initialWorkTypes();
            });
        }
        
        /**
         * Initial in case Daily Item Check Continuous Work
         */
        private initialDailyItemChkCountinuousWork() {
            let self = this;
            //ドメインモデル「勤務種類」を取得する - Acquire domain model "WorkType"
            self.initialWorkTypes();
        }
        
        /**
         * Initial in case Daily Item Check Continuous Time zone
         */
        private initialDailyItemChkCountinuousTimeZone() {
            let self = this;
            //ドメインモデル「就業時間帯の設定」を取得する - Acquire domain model "WorkTimeSetting"
            service.getAttendCoutinousTimeZone().done((settingTimeZones) => {
                self.listAllWorkingTime = self.getListWorkTimeCdFromDtos(settingTimeZones);
                //get name
                let listItems = self.workRecordExtractingCondition().errorAlarmCondition().workTimeCondition().planLstWorkTime();
                
                self.generateNameCorrespondingToAttendanceItem(listItems).done((data) => {
                    self.displayWorkingTimeSelections_BA5_3(data);
                });
                //self.initialWorkTimeCodesFromDtos(settingTimeZones);
              //ドメインモデル「勤務種類」を取得する - Acquire domain model "WorkType"
                self.initialWorkTypes();
            });
        }
        
        private getListAttendanceItemCode() : Array<any> {
            let self = this,
                workRecordExtractingCondition = self.workRecordExtractingCondition();
            let lstErAlAtdItemCon = workRecordExtractingCondition.errorAlarmCondition()
                .atdItemCondition().group1().lstErAlAtdItemCon();
            let listAttendanceItemSelectedCode = lstErAlAtdItemCon[0].countableAddAtdItems() || [];//勤怠項目の加算減算式
            return listAttendanceItemSelectedCode;
        }
        private setListAttendanceItemCode(listWorkTimeItemSelectedCode : Array<any>) {
            let self = this,
                workRecordExtractingCondition = self.workRecordExtractingCondition();
            workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1()
                .lstErAlAtdItemCon()[0].countableAddAtdItems(listWorkTimeItemSelectedCode || []);//勤怠項目の加算減算式
        }
        /**
         * アルゴリズム「勤怠項目に対応する名称を生成する」を実行する - Execute algorithm "Generate name corresponding to attendance item"
         * @param List<itemAttendanceId>
         */
        private generateNameCorrespondingToAttendanceItem(listAttendanceItemCode : Array<any>) : JQueryPromise<any> {
            let self = this,
            dfd = $.Deferred();
            if (listAttendanceItemCode && listAttendanceItemCode.length > 0) {
                service.getAttendNameByIds(listAttendanceItemCode).done((dailyAttendanceItemNames) => {
                    if (dailyAttendanceItemNames && dailyAttendanceItemNames.length > 0) {
                        var attendanceName : string = '';
                        for(var i = 0; i < dailyAttendanceItemNames.length; i++) {
                            if (attendanceName) {
                                attendanceName = attendanceName + "," + dailyAttendanceItemNames[i].attendanceItemName;
                            } else {
                                attendanceName = dailyAttendanceItemNames[i].attendanceItemName;
                            }
                        }
                        dfd.resolve(attendanceName);
                    } else {
                        dfd.resolve('');
                    }
                }).always(() => {
                    dfd.resolve('');
                });
            } else {
                dfd.resolve('');
            }
            return dfd.promise();
        }

        /**
         * Build list of Attendance Item Name from List<AttdItemDto>
         * @param listAttendanceItemCode
         */
        private buildItemName(listItem : Array<any>) : string {
            let self = this, retNames : string = '';
            if (listItem) {
                for(var i = 0; i < listItem.length; i++) {
                    if (retNames) {
                        retNames = retNames + "," + listItem[i].name;
                    } else {
                        retNames = listItem[i].name;
                    }
                }
            }
            return retNames;
        }
        
        /**
         * //ドメインモデル「勤務種類」を取得する - Acquire domain model "WorkType"
         * 
         */
        private initialWorkTypes() : void {
            let self =this,
                workTypeCondition = self.workRecordExtractingCondition().errorAlarmCondition().workTypeCondition();
            self.listAllWorkType = [];
            // get all Work type
            service.getAttendCoutinousWork().done((workTypes) => {
                if (workTypes && workTypes != undefined) {
                    for(var i = 0; i < workTypes.length; i++) {
                        self.listAllWorkType.push(workTypes[i].workTypeCode);
                    }
                }
            });
            // get Name of selected work type.
            let wkTypeSelected = workTypeCondition.planLstWorkType();
            if (wkTypeSelected && wkTypeSelected.length > 0) {
                service.findWorkTypeByCodes(wkTypeSelected).done((listWrkTypes) => {
                    let names : string = self.buildItemName(listWrkTypes);
                    self.displayWorkTypeSelections_BA1_4(names);
                });
            } else {
                self.displayWorkTypeSelections_BA1_4("");
            }
        }
        
        //initial default data of ErAlAtdItemCon
        private initialDataOfErAlAtdItemCon() {
            let self = this, workRecordExtractingCondition = self.workRecordExtractingCondition();
            if (!(workRecordExtractingCondition.checkItem() == enItemCheck.Time
                || workRecordExtractingCondition.checkItem() == enItemCheck.Time
                || workRecordExtractingCondition.checkItem() == enItemCheck.CountinuousTime
                || workRecordExtractingCondition.checkItem() == enItemCheck.Times
                || workRecordExtractingCondition.checkItem() == enItemCheck.AmountOfMoney
                || workRecordExtractingCondition.checkItem() == enItemCheck.TimeOfDate)) {
                return;
            }
            let conditionAtr = 0;
            let conditionType = ConditionType.ATTENDANCE_ITEM;
            let lstErAlAtdItemCon1 = workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon();
            
            switch (workRecordExtractingCondition.checkItem()) {
                case enItemCheck.Time:          //時間
                case enItemCheck.CountinuousTime:   //連続時間
                    conditionAtr = 1;
                    conditionType = ConditionType.FIXED_VALUE;
                    break;
                case enItemCheck.Times:         //回数
                    conditionAtr = 0;
                    conditionType = ConditionType.FIXED_VALUE;
                    break;
                case enItemCheck.AmountOfMoney: //金額
                    conditionAtr = 3;
                    conditionType = ConditionType.FIXED_VALUE;
                    break;
                case enItemCheck.TimeOfDate:    //時刻の場合
                    conditionAtr = 2;
                    conditionType = ConditionType.FIXED_VALUE;
                    break;
                default:
                    if (lstErAlAtdItemCon1 && lstErAlAtdItemCon1.length > 0) {
                        conditionAtr = lstErAlAtdItemCon1[0].conditionAtr();
                    }
                    break;
            }

            for(var i=0; i< lstErAlAtdItemCon1.length; i++) {
                lstErAlAtdItemCon1[i].conditionAtr(conditionAtr);
                lstErAlAtdItemCon1[i].conditionType(conditionType); //1: 勤怠項目 - AttendanceItem, 0: fix
            }
            let lstErAlAtdItemCon2 = workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group2().lstErAlAtdItemCon();
            for(var i=0; i< lstErAlAtdItemCon2.length; i++) {
                lstErAlAtdItemCon2[i].conditionAtr(conditionAtr);
                lstErAlAtdItemCon2[i].conditionType(conditionType); //1: 勤怠項目 - AttendanceItem, 0: fix
            }
        }
        /**
         * Get list of attendance id from list Dtos
         * @param itemAttendances
         */
        private getListAttendanceIdFromDtos(itemAttendances : Array<any>) : Array<string> {
            let listAllAttdItemCode : Array<string> = [];
            if (itemAttendances && itemAttendances != undefined) {
                for(var i = 0; i < itemAttendances.length; i++) {
                    listAllAttdItemCode.push(itemAttendances[i].attendanceItemId);
                }
            }
            return listAllAttdItemCode;
        }
        
        /**
         * Get list of work time id from list Dtos
         * @param itemAttendances
         */
        private getListWorkTimeCdFromDtos(workTimes : Array<any>) : Array<string> {
            let listWorkTimesCode : Array<string> = [];
            if (workTimes && workTimes != undefined) {
                for(var i = 0; i < workTimes.length; i++) {
                    listWorkTimesCode.push(workTimes[i].worktimeCode);
                }
            }
            return listWorkTimesCode;
        }
          //==========Daily session End====================

        /**
         * Get list code from list codes that return from selected dialog
         * @param listKdl002Model
         */
        private getListCode(listKdl002Model : Array<model.ItemModelKdl002>) : Array<string>{
            let retListCode : Array<string> = [];
            if (listKdl002Model == null || listKdl002Model == undefined) {
                return retListCode;
            }
            for(var i = 0; i < listKdl002Model.length; i++) {
                retListCode.push(listKdl002Model[i].code);
            }
            return retListCode;
        }
        /**
         * open dialog for select working type
         */
        btnSettingBA1_3_click () {
            let self = this,
                workTypeCondition = self.workRecordExtractingCondition().errorAlarmCondition().workTypeCondition();

            block.invisible();
            let lstSelectedCode = workTypeCondition.planLstWorkType();
            windows.setShared("KDL002_Multiple", true);
            //all possible items
            windows.setShared("KDL002_AllItemObj", self.listAllWorkType);
            //selected items
            windows.setShared("KDL002_SelectedItemId", lstSelectedCode);
            windows.sub.modal("/view/kdl/002/a/index.xhtml", 
                    { title: "乖離時間の登録＞対象項目", dialogClass: "no-close"}).onClosed(function(): any {
              //get data from share window
                let listItems : Array<any> = windows.getShared("KDL002_SelectedNewItem");
                if (listItems != null && listItems != undefined) {
                    let listCodes : Array<string> = self.getListCode(listItems);
                    workTypeCondition.planLstWorkType(listCodes);
                    // get name
                    let names : string = self.buildItemName(listItems);
                    self.displayWorkTypeSelections_BA1_4(names);
                    
                }
                block.clear();
            });
        }

        /**
         * open dialog for select working time zone (KDL002)
         */
        btnSettingBA5_2_click() {
            let self = this,
                workTimeCondition = self.workRecordExtractingCondition().errorAlarmCondition().workTimeCondition();

            block.invisible();
            let lstSelectedCode = workTimeCondition.planLstWorkTime();
            windows.setShared("kml001multiSelectMode", true);
            //all possible items
            windows.setShared("kml001selectAbleCodeList", self.listAllWorkingTime);
            //selected items
            windows.setShared("kml001selectedCodeList", lstSelectedCode);
            windows.sub.modal("/view/kdl/001/a/index.xhtml", 
                    { title: "割増項目の設定", dialogClass: "no-close"}).onClosed(function(): any {
              //get data from share window
                let listItems : Array<any> = windows.getShared("kml001selectedCodeList");
                if (listItems != null && listItems != undefined) {
                    workTimeCondition.planLstWorkTime(listItems);
                    //get name
                    self.generateNameCorrespondingToAttendanceItem(listItems).done((data) => {
                        self.displayWorkingTimeSelections_BA5_3(data);
                    });
                }
                block.clear();
            });
        }

        /**
         * open dialog for select working time zone
         */
        /*
        btnSettingBA2_2_click() {
            let self = this;

            block.invisible();
            let lstSelectedCode = self.getListAttendanceItemCode();
            windows.setShared("Multiple", true);
            //all possible items
            windows.setShared("AllAttendanceObj", self.listAllAttdItem);
            //selected items
            windows.setShared("SelectedAttendanceId", lstSelectedCode);
            windows.sub.modal('/view/kdl/021/a/index.xhtml',
                    {dialogClass: 'no-close'}).onClosed(function(): any {
              //get data from share window
                let listItems = windows.getShared('selectedChildAttendace');
                if (listItems != null && listItems != undefined) {
                    self.setListAttendanceItemCode(listItems);
                    // get name
                    self.generateNameCorrespondingToAttendanceItem(listItems).done((data) => {
                        self.displayAttendanceItemSelections_BA2_3(data);
                    });
                    
                   let group1 = self.workRecordExtractingCondition().errorAlarmCondition().atdItemCondition().group1();
                    let listErAlAtdItemCondition = group1.lstErAlAtdItemCon();
                    let erAlAtdItemCondition = listErAlAtdItemCondition[0];
                    self.comparisonRange().comparisonOperator(erAlAtdItemCondition.compareOperator());
                    self.comparisonRange().minValue(erAlAtdItemCondition.compareStartValue());
                    self.comparisonRange().maxValue(erAlAtdItemCondition.compareEndValue());
                }
                block.clear();
            });
        }
        */
        //openSelectAtdItemDialogTarget() {
        btnSettingBA2_2_click() {
            let self = this;
            let currentAtdItemCondition = self.workRecordExtractingCondition().errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon()[0];
            self.getListItemByAtr(currentAtdItemCondition.conditionAtr()).done((lstItem) => {
                let lstItemCode = lstItem.map((item) => { return item.attendanceItemId; });
                if (currentAtdItemCondition.conditionAtr() === 2) {
                    //Open dialog KDL021
                    nts.uk.ui.windows.setShared('Multiple', false);
                    nts.uk.ui.windows.setShared('AllAttendanceObj', lstItemCode);
                    nts.uk.ui.windows.setShared('SelectedAttendanceId', [currentAtdItemCondition.uncountableAtdItem()]);
                    nts.uk.ui.windows.sub.modal("at", "/view/kdl/021/a/index.xhtml").onClosed(() => {
                        let output = nts.uk.ui.windows.getShared("selectedChildAttendace");
                        if (output) {
                            currentAtdItemCondition.uncountableAtdItem(parseInt(output));
                            self.fillTextDisplayTarget(currentAtdItemCondition);
                        }
                    });
                } else {
                    //Open dialog KDW007C
                    let param = {
                        lstAllItems: lstItemCode,
                        lstAddItems: currentAtdItemCondition.countableAddAtdItems(),
                        lstSubItems: currentAtdItemCondition.countableSubAtdItems()
                    };
                    nts.uk.ui.windows.setShared("KDW007Params", param);
                    nts.uk.ui.windows.sub.modal("at", "/view/kdw/007/c/index.xhtml").onClosed(() => {
                        let output = nts.uk.ui.windows.getShared("KDW007CResults");
                        if (output) {
                            currentAtdItemCondition.countableAddAtdItems(output.lstAddItems.map((item) => { return parseInt(item); }));
                            currentAtdItemCondition.countableSubAtdItems(output.lstSubItems.map((item) => { return parseInt(item); }));
                            self.fillTextDisplayTarget(currentAtdItemCondition);
                        }
                    });
                }
            });
        }
        
        getListItemByAtr(conditionAtr) {
            let self = this;
            if (conditionAtr === 0) {
                //With type 回数 - Times
                return service.getAttendanceItemByAtr(2);
            } else if (conditionAtr === 1) {
                //With type 時間 - Time
                return service.getAttendanceItemByAtr(5);
            } else if (conditionAtr === 2) {
                //With type 時刻 - TimeWithDay
                return service.getAttendanceItemByAtr(6);
            } else if (conditionAtr === 3) {
                //With type 金額 - AmountMoney
                return service.getAttendanceItemByAtr(3);
            }
        }
        
        fillTextDisplayTarget(currentAtdItemCondition) {
            let self = this;
            self.displayAttendanceItemSelections_BA2_3("");
            if (currentAtdItemCondition.conditionAtr() === 2) {
                if (currentAtdItemCondition.uncountableAtdItem()) {
                    service.getAttendanceItemByCodes([currentAtdItemCondition.uncountableAtdItem()]).done((lstItems) => {
                        if (lstItems && lstItems.length > 0) {
                            self.displayAttendanceItemSelections_BA2_3(lstItems[0].attendanceItemName);
                            $("#display-target-item").trigger("validate");
                        }
                    });
                }
            } else {
                if (currentAtdItemCondition.countableAddAtdItems().length > 0) {
                    service.getAttendanceItemByCodes(currentAtdItemCondition.countableAddAtdItems()).done((lstItems) => {
                        if (lstItems && lstItems.length > 0) {
                            for (let i = 0; i < lstItems.length; i++) {
                                let operator = (i === (lstItems.length - 1)) ? "" : " + ";
                                self.displayAttendanceItemSelections_BA2_3(self.displayAttendanceItemSelections_BA2_3() + lstItems[i].attendanceItemName + operator);
                            }
                            $("#display-target-item").trigger("validate");
                        }
                    }).then(() => {
                        if (currentAtdItemCondition.countableSubAtdItems().length > 0) {
                            service.getAttendanceItemByCodes(currentAtdItemCondition.countableSubAtdItems()).done((lstItems) => {
                                if (lstItems && lstItems.length > 0) {
                                    for (let i = 0; i < lstItems.length; i++) {
                                        let operator = (i === (lstItems.length - 1)) ? "" : " - ";
                                        let beforeOperator = (i === 0) ? " - " : "";
                                        self.displayAttendanceItemSelections_BA2_3(self.displayAttendanceItemSelections_BA2_3() + beforeOperator + lstItems[i].attendanceItemName + operator);
                                    }
                                    $("#display-target-item").trigger("validate");
                                }
                            })
                        }
                    });
                } else if (currentAtdItemCondition.countableSubAtdItems().length > 0) {
                    service.getAttendanceItemByCodes(currentAtdItemCondition.countableSubAtdItems()).done((lstItems) => {
                        if (lstItems && lstItems.length > 0) {
                            for (let i = 0; i < lstItems.length; i++) {
                                let operator = (i === (lstItems.length - 1)) ? "" : " - ";
                                let beforeOperator = (i === 0) ? " - " : "";
                                self.displayAttendanceItemSelections_BA2_3(self.displayAttendanceItemSelections_BA2_3() + beforeOperator + lstItems[i].attendanceItemName + operator);
                            }
                            $("#display-target-item").trigger("validate");
                        }
                    })
                }

            }
        }
        
        /**
         * close dialog B and return result
         */
        btnDecision() {
            let self = this,
                workRecordExtractingCondition = self.workRecordExtractingCondition();
            $('.nts-input').trigger("validate");
            if (errors.hasError() === true) {
                return;
             }                
            let isOk : boolean = true;
            if (workRecordExtractingCondition.checkItem() == enItemCheck.Time
                || workRecordExtractingCondition.checkItem() == enItemCheck.Times
                || workRecordExtractingCondition.checkItem() == enItemCheck.AmountOfMoney
                || workRecordExtractingCondition.checkItem() == enItemCheck.TimeOfDate
                || workRecordExtractingCondition.checkItem() == enItemCheck.CountinuousTime
            ) {
                 // validate comparison range
                let group1 = workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1();
                let listErAlAtdItemCondition = group1.lstErAlAtdItemCon();
                let erAlAtdItemCondition = listErAlAtdItemCondition[0];                
                if (self.comparisonRange().checkValidOfRange(
                    workRecordExtractingCondition.checkItem()
                    
                    , 1)) {
                    erAlAtdItemCondition.compareOperator(self.comparisonRange().comparisonOperator());
                    erAlAtdItemCondition.compareStartValue(self.comparisonRange().minValue());
                    erAlAtdItemCondition.compareEndValue(self.comparisonRange().maxValue());
                    erAlAtdItemCondition.singleAtdItem(self.comparisonRange().minValue());
                    //clear 
                    //listErAlAtdItemCondition = listErAlAtdItemCondition.splice(0, 1);
                   // workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon(listErAlAtdItemCondition);
                    //workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group2().lstErAlAtdItemCon([]);
                    
                } else {
                    isOk = false;
                }
            } else if (workRecordExtractingCondition.checkItem() == enItemCheck.CountinuousWork
                 || workRecordExtractingCondition.checkItem() == enItemCheck.CountinuousTimeZone) {
               // workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group1().lstErAlAtdItemCon([]);
                //workRecordExtractingCondition.errorAlarmCondition().atdItemCondition().group2().lstErAlAtdItemCon([]);
            }
            if (isOk) {
                let retData = ko.toJS(workRecordExtractingCondition);
                retData = shareutils.convertArrayOfWorkRecordExtractingConditionToJS(retData, workRecordExtractingCondition);
                windows.setShared('outputKal003b', retData);
                windows.close();
           }
        }
        /**
         * close dialog B and return result
         */
        closeDialog() {
            windows.setShared('outputKal003b', undefined);
            windows.close();
        }
    }

    /**
     * The enum of ROLE TYPE 
     */
    export enum enCategory {
        Daily   = 0,
        Weekly  = 1,
        Monthly = 2

    }
    export enum enItemCheck {
        Time                = 0, // 時間
        Times               = 1, // 回数
        AmountOfMoney       = 2, // 金額
        TimeOfDate          = 3, // 時刻
        CountinuousTime     = 4, // 連続時間
        CountinuousWork     = 5, // 連続勤務
        CountinuousTimeZone = 6, // 連続時間帯
        CompoundCondition   = 7// 複合条件
    }
    
    export enum ConditionType {

        /* 固定値 */
        FIXED_VALUE = 0, //, "Enum_ConditionType_FixedValue"),
        /* 勤怠項目 */
        ATTENDANCE_ITEM = 1 //, "Enum_ConditionType_AttendanceItem");
    }
    
    module model {
        export class ItemModelKdl002 {
            code: string;
            name: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        export interface IEnumModel {
            value : number;
            fieldName: string;
            localizedName: string;
        }
        
        export class EnumModel {
            value : KnockoutObservable<number>;
            fieldName: string;
            localizedName: string;
            constructor(param: IEnumModel) {
                let self = this;
                self.value =  ko.observable(param.value || -1);
                self.fieldName = param.fieldName || '';
                self.localizedName = param.localizedName || '';
            }
        }

        /**
         * ComparisonValueRange
         */
        export class ComparisonValueRange {
            minTimeValue: KnockoutObservable<number> =  ko.observable(0);
            maxTimeValue: KnockoutObservable<number> =  ko.observable(0);
            
            minTimesValue: KnockoutObservable<number> =  ko.observable(0);
            maxTimesValue: KnockoutObservable<number> =  ko.observable(0);
            
            minAmountOfMoneyValue: KnockoutObservable<number> =  ko.observable(0);
            maxAmountOfMoneyValue: KnockoutObservable<number> =  ko.observable(0);
            
            minTimeWithinDayValue: KnockoutObservable<number> =  ko.observable(0);
            maxTimeWithinDayValue: KnockoutObservable<number> =  ko.observable(0);
            minValue : KnockoutObservable<number> =  ko.observable(0);
            maxValue : KnockoutObservable<number> =  ko.observable(0);
            
            checkItem : KnockoutObservable<number> =  ko.observable(0);
            comparisonOperator : KnockoutObservable<number> =  ko.observable(0);
            
            isChecking : boolean = false;
            constructor(checkItem : KnockoutObservable<number>, comOper : KnockoutObservable<number>, minVal : number, maxVal: number) {
                let self = this;
                minVal = self.convertToNumber(minVal);
                maxVal = self.convertToNumber(maxVal);
                self.minValue(minVal || 0);
                self.maxValue(maxVal || 0);
                self.checkItem = checkItem;
                self.comparisonOperator = comOper;
                //時間 - 0: check time
                //連続時間 - 4:  check time
                self.minTimeValue(minVal);
                self.maxTimeValue(maxVal);
                //回数 - 1: check times
                self.minTimesValue(minVal);
                self.maxTimesValue(maxVal);
                //金額 - 2: check amount of money
                self.minAmountOfMoneyValue(minVal || 0);
                self.maxAmountOfMoneyValue(maxVal || 0);
                //時刻の場合 - 3: time within day
                self.minTimeWithinDayValue( minVal || 0);
                self.maxTimeWithinDayValue(maxVal || 0);

                //時間 - 0: check time
               //連続時間 - 4:  check time
               self.minTimeValue.subscribe((value) => {
                   self.settingMinValue(value);
                });
                self.maxTimeValue.subscribe((value) => {
                    self.settingMaxValue(value);
                });

                //回数 - 1: check times
                self.minTimesValue.subscribe((value) => {
                    self.settingMinValue(value);
                });
                self.maxTimesValue.subscribe((value) => {
                    self.settingMaxValue(value);
                });

                //金額 - 2: check amount of money
                self.minAmountOfMoneyValue.subscribe((value) => {
                    self.settingMinValue(value);
                });
                self.maxAmountOfMoneyValue.subscribe((value) => {
                    self.settingMaxValue(value);
                });
                
                //時刻の場合 - 3: time within day
                self.minTimeWithinDayValue.subscribe((value) => {
                    self.settingMinValue(value);
                });
                self.maxTimeWithinDayValue.subscribe((value) => {
                    self.settingMaxValue(value);
                });
            }
            
            private settingMinValue(val) {
                let self = this;
               if (self.minValue() == val) {
                   return;
               }
               self.minValue(val);
               self.checkValidOfRange(self.checkItem(), 0); //min
            }
            private settingMaxValue(val) {
                let self = this;
                   if (self.maxValue() == val) {
                       return;
                   }
                   self.maxValue(val);
                   self.checkValidOfRange(self.checkItem(), 1); //max
            }

            private convertToNumber(value : string | number) : number {
                if (typeof value === "string") {
                    return parseInt(value);
                } else {
                    return value; // We know its a number 
                }
            }
             /**
             * valid range of comparison 
             */
            checkValidOfRange(checkItem: number, textBoxFocus : number) : boolean {
                let self = this;
                let isValid : boolean = true;

                if (self.comparisonOperator() > 5) {
                    let mnValue : number = undefined;
                    let mxValue : number = undefined;
                    switch (checkItem) {
                        case enItemCheck.Time:          //時間 - 0: check time
                        case enItemCheck.CountinuousTime:   //連続時間 - 4:  check time
                            mnValue = self.minTimeValue();
                            mxValue = self.maxTimeValue();
                            break;
                        case enItemCheck.Times:         //回数 - 1: check times
                            mnValue = self.minTimesValue();
                            mxValue = self.maxTimesValue();
                            break;
                        case enItemCheck.AmountOfMoney: //金額 - 2: check amount of money
                            mnValue = self.minAmountOfMoneyValue();
                            mxValue = self.maxAmountOfMoneyValue();
                            break;
                        case enItemCheck.TimeOfDate:    //時刻の場合 - 3: time within day
                            mnValue = self.minTimeWithinDayValue();
                            mxValue = self.maxTimeWithinDayValue();
                            break
                        default:
                            break;
                    }
                    
                    if (mnValue != undefined && mxValue != undefined) {
                        isValid = self.compareValid(self.comparisonOperator(), mnValue, mxValue);
                    }
                }
                if (!isValid) {
                    dialog.info({ messageId: "Msg_927" });
                    
                    if(textBoxFocus === 1) { //max
                        $('KAL003_65').ntsError('set', {messageId:"Msg_927"});
                        $('KAL003_65').focus();
                    } else {
                        $('KAL003_64').ntsError('set', {messageId:"Msg_927"});
                        $('KAL003_64').focus();
                    }
                    
                }
                return isValid;
            }
            /**
             * execute check valid of range
             */
            private compareValid(comOper : number, minValue : number, maxValue: number): boolean {
                 switch (comOper) {
                    case 6: // 範囲の間（境界値を含まない）（＜＞）
                    case 8: // 範囲の外（境界値を含まない）（＞＜）
                        return minValue < maxValue;
                    case 7: // 範囲の間（境界値を含む）（≦≧）
                    case 9: // 範囲の外（境界値を含む）（≧≦）
                        return minValue <= maxValue;
                    default:
                        break;
                }
                return true;
            }
        }
     }
}