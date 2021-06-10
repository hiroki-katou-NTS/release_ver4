module nts.uk.at.view.ksu001.a.viewmodel {
    import alert = nts.uk.ui.dialog.alert;
    import alertError = nts.uk.ui.dialog.alertError;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;
    import Ccg001ReturnedData = nts.uk.com.view.ccg.share.ccg.service.model.Ccg001ReturnedData;
    import blockUI = nts.uk.ui.block;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import formatById = nts.uk.time.format.byId;
    import openDialog = nts.uk.ui.windows.sub.modal;
    import getText = nts.uk.resource.getText;


    /**
     * load screen O->Q->A
     * reference file a.start.ts
     */
    export class ScreenModel {
        employeeIdLogin: string = null;
        //tree-grid
        itemsTree: KnockoutObservableArray<Node>;
        selectedCodeTree: KnockoutObservableArray<Node>;
        singleSelectedCodeTree: KnockoutObservable<Node>;

        empItems: KnockoutObservableArray<PersonModel> = ko.observableArray([]);
        dataSource: KnockoutObservableArray<BasicSchedule> = ko.observableArray([]);
        ccgcomponent: GroupOption = ko.observable();
        selectedCode: KnockoutObservableArray<any> = ko.observableArray([]);
        showinfoSelectedEmployee: KnockoutObservable<boolean> = ko.observable(true);

        //Pop-up
        items: KnockoutObservableArray<ItemModel> = ko.observableArray([]);
        columns: KnockoutObservableArray<NtsGridListColumn> = ko.observableArray([
            { headerText: getText("KSU001_19"), key: 'code', width: 50 },
            { headerText: getText("KSU001_20"), key: 'name', width: 150 },
            { headerText: 'コード', key: 'id', width: 50, hidden: true },
        ]);
        currentCodeList: KnockoutObservableArray<any> = ko.observableArray([]);

        backgroundColorList: KnockoutObservableArray<ItemModel> = ko.observableArray([
            new ItemModel('001', '就業時間帯'),
            new ItemModel('002', '通常')
        ]);
        selectedBackgroundColor: KnockoutObservable<string> = ko.observable('001');
        itemList: KnockoutObservableArray<ItemModel> = ko.observableArray([
            new ItemModel('基本給1', '基本給'),
            new ItemModel('基本給2', '役職手当'),
            new ItemModel('基本給3', '基本給')
        ]);
        selectedCode1: KnockoutObservable<string> = ko.observable('0003');
        roundingRules: KnockoutObservableArray<any> = ko.observableArray([
            new BoxModel(1, getText("KSU001_89")),
            new BoxModel(2, getText("KSU001_90")),
        ]);
        selectedDisplayLastWeek: any = ko.observable(1);
        selectedDisplayBank: any = ko.observable(1);
        selectedDisplayVertical: any = ko.observable(1);
        selectedCompareMonth: any = ko.observable(1);
        itemList1: KnockoutObservableArray<any> = ko.observableArray([
            new BoxModel(1, '画面サイズ'),
            new BoxModel(2, '高さを指定'),
        ]);
        selectedTypeHeightExTable: KnockoutObservable<number> = ko.observable(1);
        isEnableInputHeight: KnockoutObservable<boolean> = ko.observable(false);
        isEnableCompareMonth: KnockoutObservable<boolean> = ko.observable(true);

        itemList2: KnockoutObservableArray<any> = ko.observableArray([
            new BoxModel(1, getText("KSU001_339")),
            new BoxModel(2, getText("KSU001_340")),
            new BoxModel(3, getText("KSU001_341"))
        ]);
        selectedIds: KnockoutObservableArray<number> = ko.observableArray([1, 2]);
        popupVal: KnockoutObservable<string> = ko.observable('');
        selectedDate: KnockoutObservable<string> = ko.observable('1993/23/12');

        //Date time
        currentDate: Date = new Date();
        dtPrev: KnockoutObservable<Date> = ko.observable(null);
        dtAft: KnockoutObservable<Date> = ko.observable(null);
        dateTimePrev: KnockoutObservable<string>;
        dateTimeAfter: KnockoutObservable<string>;

        //Switch
        timePeriod: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: '抽出' },
            { code: 2, name: '２８日' },
            { code: 3, name: '末日' }]);
        selectedTimePeriod: KnockoutObservable<number> = ko.observable(1);

        modeDisplay: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: '略名' },
            { code: 2, name: '時刻' }
//            { code: 3, name: '記号' }
        ]);
        selectedModeDisplay: KnockoutObservable<number> = ko.observable(1);

        modeDisplayObject: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: '予定' },
            { code: 2, name: '実績' }]);
        selectedModeDisplayObject: KnockoutObservable<number> = ko.observable(1);

        arrDay: Time[] = [];
        listSid: KnockoutObservableArray<string> = ko.observableArray([]);
        lengthListSid: any;
        workplaceId: any = null;
        workPlaceNameDisplay: KnockoutObservable<string> = ko.observable('');
        dataWScheduleState: KnockoutObservableArray<WorkScheduleState> = ko.observableArray([]);
        listStateWorkTypeCode: KnockoutObservableArray<any> = ko.observableArray([]);
        listCheckNeededOfWorkTime: KnockoutObservableArray<any> = ko.observableArray([]);
        dataWkpSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataComSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataPublicHoliday: KnockoutObservableArray<any> = ko.observableArray([]);
        dataWorkEmpCombine: KnockoutObservableArray<any> = ko.observableArray([]);
        dataScheduleDisplayControl: KnockoutObservable<any> = ko.observableArray([]);
        isInsuranceStatus: boolean = false;
        listColorOfHeader: KnockoutObservableArray<ksu001.common.viewmodel.CellColor> = ko.observableArray([]);
        flag: boolean = true;
        isClickChangeDisplayMode: boolean = false;
        stopRequest: KnockoutObservable<boolean> = ko.observable(true);
        arrLockCellInit: KnockoutObservableArray<Cell> = ko.observableArray([]);
        // 表示形式 ＝ 日付別(固定) = 0
        displayFormat: KnockoutObservable<number> = ko.observable(0);
        hasEmployee: KnockoutObservable<boolean> = ko.observable(false);


        constructor() {
            let self = this;
            //Tree grid
            self.itemsTree = ko.observableArray([]);
            self.selectedCodeTree = ko.observableArray([]);
            self.singleSelectedCodeTree = ko.observable(null);

            //Date time
            self.dateTimeAfter = ko.observable(moment(self.dtAft()).format('YYYY/MM/DD'));
            self.dateTimePrev = ko.observable(moment(self.dtPrev()).format('YYYY/MM/DD'));

            self.dtPrev.subscribe((newValue) => {
                self.dateTimePrev(moment(self.dtPrev()).format('YYYY/MM/DD'));
            });
            self.dtAft.subscribe((newValue) => {
                self.dateTimeAfter(moment(self.dtAft()).format('YYYY/MM/DD'));
            });

            //Pop-up
            for (let i = 1; i <= 12; i++) {
                self.items.push(new ItemModel('00' + i, '基本給' + i, '00' + i));
            }

            self.selectedTypeHeightExTable.subscribe(function(newValue) {
                $('#input-heightExtable').ntsError('clear');
                if (newValue == 1) {
                    self.isEnableInputHeight(false);
                } else {
                    self.isEnableInputHeight(true);
                    setTimeout(() =>{
                        $('#input-heightExtable').focus();                        
                    }, 1);
                }
            });

            self.selectedDisplayVertical.subscribe(function(newValue) {
                if (newValue == 1) {
                    self.isEnableCompareMonth(true);
                } else {
                    self.isEnableCompareMonth(false);
                }
            });

            self.selectedModeDisplay.subscribe(function(newValue) {
                nts.uk.ui.errors.clearAll();
                __viewContext.viewModel.viewO.time1('');
                __viewContext.viewModel.viewO.time2('');
                
                self.stopRequest(false);
                // close screen O1 when change mode
                let currentScreen = __viewContext.viewModel.viewO.currentScreen;
                if (currentScreen) {
                    currentScreen.close();
                }
                
                let detailContentDeco: any[] = [];

                if (newValue == 1) {
                    $('#contain-view').show();
                    $('#contain-view').removeClass('h-90');
                    $('#group-bt').show();
                    $('#oViewModel').show();
                    $('#qViewModel').hide();
                    self.setColorForCell(detailContentDeco).done(() => {
                        $("#extable").exTable("mode", "shortName", "stick", { y: 210 }, [{
                            name: "BodyCellStyle",
                            decorator: detailContentDeco
                        }]);
                        self.stopRequest(true);
                    });
                    $("#extable").exTable("stickMode", "single");
                    $("#combo-box1").focus();
                    // get data to stickData
                    $("#extable").exTable("stickData", __viewContext.viewModel.viewO.nameWorkTimeType());
                } else if (newValue == 2) {
                    $('#contain-view').hide();
                    self.setColorForCell(detailContentDeco).done(() => {
                        $("#extable").exTable("mode", "time", "edit", { y: 150 }, [{
                            name: "BodyCellStyle",
                            decorator: detailContentDeco
                        }]);
                        self.stopRequest(true);
                    });
                } else {
                    $('#contain-view').show();
                    $('#contain-view').addClass('h-90');
                    $('#oViewModel').hide();
                    $('#qViewModel').show();
                    $('#group-bt').show();
                    self.setColorForCell(detailContentDeco).done(() => {
                        $("#extable").exTable("mode", "symbol", "stick", { y: 245 }, [{
                            name: "BodyCellStyle",
                            decorator: detailContentDeco
                        }]);
                        self.stopRequest(true);
                    });
                    $("#extable").exTable("stickMode", "multi");
                    $("#tab-panel").focus();
                    // get data to stickData
                    // if buttonTable not selected, set stickData is null
                    if ((__viewContext.viewModel.viewQ.selectedTab() == 'company' && $("#test1").ntsButtonTable("getSelectedCells")[0] == undefined)
                        || (__viewContext.viewModel.viewQ.selectedTab() == 'workplace' && $("#test2").ntsButtonTable("getSelectedCells")[0] == undefined)) {
                        $("#extable").exTable("stickData", null);
                    } else if (__viewContext.viewModel.viewQ.selectedTab() == 'company') {
                        let dataToStick = _.map($("#test1").ntsButtonTable("getSelectedCells")[0].data.data, 'data');
                        $("#extable").exTable("stickData", dataToStick);
                    } else if (__viewContext.viewModel.viewQ.selectedTab() == 'workplace') {
                        let dataToStick = _.map($("#test2").ntsButtonTable("getSelectedCells")[0].data.data, 'data');
                        $("#extable").exTable("stickData", dataToStick);
                    }
                    // jump initScreenQ only once
                    if (self.flag) {
                        //select first link button
                        __viewContext.viewModel.viewQ.initScreenQ();
                        self.flag = false;
                    }
                }
            });
            // TODO - comment do chua lam phan du lieu thuc te
//            self.selectedModeDisplayObject.subscribe((newValue) => {
//                if (self.listSid().length > 0) {
//                    if (newValue == 2) {
//                        // actual data display mode 
//                        // (in phase 2 not done, so the actual data = intended data)
//                        // if actual data is null, display intended data
//                        self.setDatasource().done(function() {
//                            self.updateExTable();
//                        });
//                    } else {
//                        // intended data display mode 
//                        self.setDatasource().done(function() {
//                            self.updateExTable();
//                        });
//                    }
//                }
//            });

            self.selectedBackgroundColor.subscribe((newValue) => {
                self.updateExTable();
            });

            //display for A3_2
            self.lengthListSid = ko.pureComputed(() => {
                let sizeListSid: number = self.listSid().length;
                if(sizeListSid > 0){
                    self.hasEmployee(true);
                }
                return getText('KSU001_54', [sizeListSid.toString()]);
            });

            self.stopRequest.subscribe(function(value) {
                if (!value) {
                    nts.uk.ui.block.grayout();
                } else {
                    nts.uk.ui.block.clear();
                }
            });

            /**
             * close popup
             */
            $(".close-popup").click(function() {
                $('#popup-area8').css('display', 'none');
            });
        }

        /**
         * get data for screen O, Q , A before bind
         */
        startKSU001(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            // get data for screen O 
            // initScreen: get data workType-workTime for 2 combo-box of screen O
            // and startDate-endDate of screen A
            __viewContext.viewModel.viewO.initScreen().done(() => {
                self.getDataScheduleDisplayControl(); 
                self.getDataComPattern();
                self.dtPrev(moment.utc(__viewContext.viewModel.viewO.startDateScreenA, 'YYYY/MM/DD'));
                self.dtAft(moment.utc(__viewContext.viewModel.viewO.endDateScreenA, 'YYYY/MM/DD'));
                self.employeeIdLogin = __viewContext.viewModel.viewO.employeeIdLogin;
                // get state of list workTypeCode
                // get data for screen A
                let lstWorkTypeCode = [];
                _.map(__viewContext.viewModel.viewO.listWorkType(), (workType: nts.uk.at.view.ksu001.common.viewmodel.WorkType) => {
                    lstWorkTypeCode.push(workType.workTypeCode);
                });
                // get data for dialog C
                // self.initShiftCondition();
                // init and get data for screen A
                // checkNeededOfWorkTimeSetting(): get list state of workTypeCode relate to need of workTime
                self.listStateWorkTypeCode(__viewContext.viewModel.viewO.checkStateWorkTypeCode);
                self.listCheckNeededOfWorkTime(__viewContext.viewModel.viewO.checkNeededOfWorkTimeSetting);
                self.dataWorkEmpCombine(__viewContext.viewModel.viewO.workEmpCombines);

                self.initExTable();
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * CCG001 return listEmployee
         * When listEmployee changed, call function updateExtable to refresh data of exTable
         */
        searchEmployee(dataEmployee: EmployeeSearchDto[]) {
            let self = this;
            self.stopRequest(false);

            self.empItems.removeAll();
            _.forEach(dataEmployee, function(item: EmployeeSearchDto) {
                self.empItems.push(new PersonModel({
                    empId: item.employeeId,
                    empCd: item.employeeCode,
                    empName: item.employeeName,
                    workplaceId: item.workplaceId,
                    wokplaceCd: item.workplaceCode,
                    workplaceName: item.workplaceName,
                }));
            });
            self.workplaceId = self.empItems()[0].workplaceId;
            // get data for listSid
            self.listSid([]);
            let arrSid: string[] = [];
            _.each(self.empItems(), (x) => {
                arrSid.push(x.empId);
            });
            self.listSid(arrSid);
            //getDataOfWorkPlace(): get workPlaceName to display A3-1
            //getDataWkpPattern() : get data WorkPattern for screen Q
            $.when(self.getDataWkpPattern(), self.getDataOfWorkPlace()).done(() => {
                if (__viewContext.viewModel.viewQ.selectedTab() === 'workplace') {
                    __viewContext.viewModel.viewQ.initScreenQ();
                }

                if (self.selectedModeDisplayObject() == 1) {
                    // intended data display mode 
                    self.setDatasource().done(function() {
                        self.updateExTable();
                    });
                } else {
                    // actual data display mode 
                    // in phare 2, set actual data = intended data
                    self.setDatasource().done(function() {
                        self.updateExTable();
                    });
                }
            }).fail(() => { self.stopRequest(true); });
        }

        initCCG001(): JQueryPromise<any>  {
            let self = this, dfd = $.Deferred();
            // Component option
            self.ccgcomponent = {
                maxPeriodRange: 'oneMonth',
                /** Common properties */
                systemType: 2, // システム区分
                showEmployeeSelection: false, // 検索タイプ
                showQuickSearchTab: false, // クイック検索
                showAdvancedSearchTab: true, // 詳細検索
                showBaseDate: false, // 基準日利用
                showClosure: false, // 就業締め日利用
                showAllClosure: false, // 全締め表示
                showPeriod: true, // 対象期間利用
                periodFormatYM: false, // 対象期間精度

                /** Required parameter */
                periodStartDate: self.dateTimePrev, // 対象期間開始日
                periodEndDate: self.dateTimeAfter, // 対象期間終了日
                inService: true, // 在職区分
                leaveOfAbsence: false, // 休職区分
                closed: false, // 休業区分
                retirement: false, // 退職区分

                /** Quick search tab options */
                showAllReferableEmployee: true, // 参照可能な社員すべて
                showOnlyMe: false, // 自分だけ
                showSameWorkplace: true, // 同じ職場の社員
                showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

                /** Advanced search properties */
                showEmployment: true, // 雇用条件
                showWorkplace: true, // 職場条件
                showClassification: true, // 分類条件
                showJobTitle: true, // 職位条件
                showWorktype: true, // 勤種条件
                isMutipleCheck: true, // 選択モード
                showOnStart: true,

                /** Return data */
                returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                    self.searchEmployee(data.listEmployee);
                    // set startDate-endDate
                    let isAllowUpdateExTable = false;
                    if (moment(self.dtPrev()).format('YYYYMMDD') !== moment(data.periodStart).format('YYYYMMDD')) {
                        self.dtPrev(new Date(data.periodStart));
                        isAllowUpdateExTable = true;
                    }

                    if (moment(self.dtAft()).format('YYYYMMDD') !== moment(data.periodEnd).format('YYYYMMDD')) {
                        self.dtAft(new Date(data.periodEnd));
                        isAllowUpdateExTable = true;
                    }

                    if (isAllowUpdateExTable) {
                        self.updateDetailAndHorzSum();
                    }
                }
            }
            // Start component
            $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent).done(() => {
                dfd.resolve();
            });
            
            return dfd.promise();
        }

        /**
         * Create exTable
         */
        initExTable(): void {
            let self = this,
                timeRanges = [],
                //Get dates in time period
                currentDay = new Date(self.dtPrev().toString());

            // create data for columns
            let leftmostDs = [],
                middleDs = [],
                middleContentDeco = [],
                detailHeaderDeco = [],
                detailContentDeco = [],
                detailHeaderDs = [],
                detailContentDs = [],
                objDetailHeaderDs = {},
                detailColumns = [],
                horzSumHeaderDs = [],
                horzSumContentDs = [],
                leftHorzContentDs = [],
                vertSumContentDs = [];

            while (currentDay <= self.dtAft()) {
                self.arrDay.push(new Time(currentDay));
                let time = new Time(currentDay);
                objDetailHeaderDs['_' + time.yearMonthDay] = '';
                detailColumns.push({
                    key: "_" + time.yearMonthDay, width: "50px", handlerType: "input", dataType: "duration/duration", primitiveValue: "TimeWithDayAttr", visible: true
                });

                currentDay.setDate(currentDay.getDate() + 1);
            }

            //create dataSource for detailHeader
            detailHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));
            detailHeaderDs.push(objDetailHeaderDs);

            //define the detailColumns
            horzSumHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));

            //create leftMost Header and Content
            let leftmostColumns = [{
                headerText: getText("KSU001_56"), key: "empName", width: "160px"
            }];

            let leftmostHeader = {
                columns: leftmostColumns,
                rowHeight: "60px",
                width: "160px"
            };

            let leftmostContent = {
                columns: leftmostColumns,
                dataSource: leftmostDs,
                primaryKey: "empId"
            };

            //create Middle Header and Content
            let middleColumns = [
                { headerText: getText("KSU001_57"), key: "team", width: "50px" },
                { headerText: getText("KSU001_58"), key: "rank", width: "50px" },
                { headerText: getText("KSU001_59"), key: "qualification", width: "50px" },
                { headerText: getText("KSU001_60"), key: "employmentName", width: "100px" },
                { headerText: getText("KSU001_61"), key: "workplaceName", width: "150px" },
                { headerText: getText("KSU001_62"), key: "classificationName", width: "100px" },
                { headerText: getText("KSU001_63"), key: "positionName", width: "100px" },
            ];

            let middleHeader = {
                columns: middleColumns,
                width: "100px",
                features: [{
                    name: "HeaderRowHeight",
                    rows: { 0: "60px" }
                }]
            };

            let middleContent = {
                columns: middleColumns,
                dataSource: middleDs,
                primaryKey: "empId",
                features: [{
                    name: "BodyCellStyle",
                    decorator: middleContentDeco
                }]
            };
            //create Detail Content
            let detailContent = {
                columns: detailColumns,
                dataSource: detailContentDs,
                primaryKey: "empId",
                features: [{
                    name: "BodyCellStyle",
                    decorator: detailContentDeco
                }, {
                        name: "TimeRange",
                        ranges: timeRanges
                    }],
                view: function(mode, obj) {
                    switch (mode) {
                        case "shortName":
                            return ["workTypeName", "workTimeName"];
                        case "symbol":
                            return ["symbolName"];
                        case "time":
                            return ["startTime", "endTime"];
                    }
                },
                fields: ["workTypeCode", "workTimeCode", "startTime", "endTime"],
                upperInput: "startTime",
                lowerInput: "endTime",
                banEmptyInput: ["time"]
            };

            //create VerticalSum Header and Content
            let vertSumColumns = [
                {
                    headerText: "公休日数",
                    group: [
                        { headerText: "可能数", key: "noCan", width: "100px" },
                        { headerText: "取得数", key: "noGet", width: "100px" }
                    ]
                }
            ];

            let vertSumHeader = {
                columns: vertSumColumns,
                width: "200px",
                features: [{
                    name: "HeaderRowHeight",
                    rows: { 0: "35px", 1: "25px" }
                }]
            };

            let vertSumContent = {
                columns: vertSumColumns,
                dataSource: vertSumContentDs,
                primaryKey: "empId"
            };

            //create LeftHorzSum Header and Content
            let leftHorzColumns = [
                { headerText: "項目名", key: "itemName", width: "160px" },
                { headerText: "合計", key: "sum", width: "600px" }
            ];

            let leftHorzSumHeader = {
                columns: leftHorzColumns,
                rowHeight: "40px"
            };

            let leftHorzSumContent = {
                columns: leftHorzColumns,
                dataSource: leftHorzContentDs,
                primaryKey: "itemId"
            };
            // create HorizontalSum Content
            let horizontalSumContent = {
                columns: detailColumns,
                dataSource: horzSumContentDs,
                primaryKey: "itemId"
            };

            $.when(self.setColorForCellHeaderDetailAndHoz(detailHeaderDeco)).done(() => {
                // create Detail Header
                let detailHeader = {
                    columns: detailColumns,
                    dataSource: detailHeaderDs,
                    width: "700px",
                    features: [{
                        name: "HeaderRowHeight",
                        rows: { 0: "40px", 1: "20px" }
                    }, {
                            name: "HeaderCellStyle",
                            decorator: detailHeaderDeco
                        }, {
                            name: "ColumnResizes"
                        }, {
                            name: "HeaderPopups",
                            menu: {
                                rows: [0],
                                items: [
                                    { id: "日付別", text: getText("KSU001_325"), selectHandler: function(id) { alert('Open KSU003'); } },
                                    { id: "シフト別", text: getText("KSU001_326"), selectHandler: function(id) { alert('Open KSC003'); } }
                                ]
                            },
                            //                            popup: {
                            //                                rows: [1],
                            //                                provider: function(columnKey) {
                            //                                    self.selectedDate(moment(columnKey, '_YYYYMMDD').format('YYYY/MM/DD'));
                            //                                    return $("#popup-area8");
                            //                                }
                            //                            }

                        }]
                };

                //create HorizontalSum Header
                let horizontalSumHeader = {
                    columns: detailColumns,
                    dataSource: horzSumHeaderDs,
                    rowHeight: "40px",
                    features: [{
                        name: "HeaderRowHeight",
                        rows: { 0: "40px" }
                    }, {
                            name: "HeaderCellStyle",
                            decorator: detailHeaderDeco
                        }]
                };

                new nts.uk.ui.exTable.ExTable($("#extable"), {
                    headerHeight: "60px", bodyRowHeight: "30px", bodyHeight: "0px",
                    horizontalSumHeaderHeight: "40px", horizontalSumBodyHeight: "75px",
                    horizontalSumBodyRowHeight: "20px",
                    areaResize: true,
                    bodyHeightMode: "dynamic",
                    windowXOccupation: 134,
                    windowYOccupation: 210,
                    manipulatorId: self.employeeIdLogin,
                    manipulatorKey: "empId",
                    updateMode: "stick",
                    pasteOverWrite: true,
                    stickOverWrite: true,
                    viewMode: "shortName",
                    determination: {
                        rows: [0, 1],
                        columns: ["empName"]
                    },
                })
                    .LeftmostHeader(leftmostHeader).LeftmostContent(leftmostContent)
                    //                    .MiddleHeader(middleHeader).MiddleContent(middleContent)
                    .DetailHeader(detailHeader).DetailContent(detailContent)
                    //                    .VerticalSumHeader(vertSumHeader).VerticalSumContent(vertSumContent)
                    //                    .LeftHorzSumHeader(leftHorzSumHeader).LeftHorzSumContent(leftHorzSumContent)
                    //                    .HorizontalSumHeader(horizontalSumHeader).HorizontalSumContent(horizontalSumContent)    
                    .create();

                // set stick single
                $("#extable").exTable("stickData", __viewContext.viewModel.viewO.nameWorkTimeType());
                $("#extable").exTable("stickMode", "single");
                
                // fix bug #117021
                if ($("#extable").width() == 0)
                    $("#extable").width(1);

                /**
                 * update text for row 2 of detailHeader
                 */
                $("#popup-set").click(function() {
                    $("#extable").exTable("popupValue", self.popupVal());
                });
            });
        }

        /**
         *  update extable
         *  create new dataSource for some part of exTable
         *  set color for extable
         */
        updateExTable(): void {
            let self = this;
            // save scroll's position
            $("#extable").exTable("saveScroll");
            self.stopRequest(false);

            let newLeftMostDs = [], newMiddleDs = [], newDetailContentDs = [], newDetailHeaderDs = [], newObjDetailHeaderDs = [], newVertSumContentDs = [], newLeftHorzContentDs = [];

            _.each(self.listSid(), (x) => {
                //newLeftMost dataSource
                let empItem: PersonModel = _.find(self.empItems(), ['empId', x]);
                newLeftMostDs.push({ empId: x, empName: empItem.empCd + ' ' + empItem.empName });
                //newMiddle dataSource
                newMiddleDs.push({ empId: x, team: "1", rank: "A", qualification: "★", employmentName: "アルバイト", workplaceName: "東京本社", classificationName: "分類", positionName: "一般" });
                //newDetail dataSource
                let dsOfSid: any = _.filter(self.dataSource(), ['employeeId', x]);
                newDetailContentDs.push(new ExItem(x, dsOfSid, __viewContext.viewModel.viewO.listWorkType(), __viewContext.viewModel.viewO.listWorkTime(), false, self.arrDay));
                //newVertSumContent dataSource
                newVertSumContentDs.push({ empId: x, noCan: 6, noGet: 6 });
            });

            //create new detailHeaderDs
            newDetailHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));
            for (let i = 0; i < self.arrDay.length; i++) {
                newObjDetailHeaderDs['_' + self.arrDay[i].yearMonthDay] = '';
            }
            newDetailHeaderDs.push(newObjDetailHeaderDs);

            //newLeftHorzSContent dataSource
            for (let i = 0; i < 5; i++) {
                newLeftHorzContentDs.push({ itemId: i.toString(), itemName: "8:00 ~ 9:00", sum: "23.5" });
            }

            //get new horzSumContentDs
            let horzSumContentDs = [];
            for (let i = 0; i < 5; i++) {
                let obj = {};
                obj["itemId"] = i.toString();
                obj["empId"] = "";
                for (let j = 0; j < self.arrDay.length; j++) {
                    obj['_' + self.arrDay[j].yearMonthDay] = "10";
                }
                horzSumContentDs.push(obj);
            }

            let newDetailColumns = [];
            //define the new detailColumns
            _.each(self.arrDay, (x: Time) => {
                newDetailColumns.push({
                    key: "_" + x.yearMonthDay, width: "50px", handlerType: "input", dataType: "duration/duration", primitiveValue: "TimeWithDayAttr", visible: true
                });
            });

            let updateMiddleContent = {
                dataSource: newMiddleDs,
            };

            let leftmostContentDeco = [], detailHeaderDeco = [], detailContentDeco = [];
            let leftmostColumns = [{
                key: "empName", headerText: "", width: "160px", icon: { for: "body", class: "icon-leftmost", width: "25px" },
                css: { whiteSpace: "pre" }, control: "link", handler: function(rData, rowIdx, key) { }
            }];

            self.setColor(detailHeaderDeco, detailContentDeco).done(() => {
                let updateLeftmostContent = {
                    columns: leftmostColumns,
                    dataSource: newLeftMostDs,
                    features: [{
                        name: "BodyCellStyle",
                        decorator: leftmostContentDeco
                    }]
                };

                let updateDetailHeader = {
                    columns: newDetailColumns,
                    dataSource: newDetailHeaderDs,
                    features: [{
                        name: "HeaderRowHeight",
                        rows: { 0: "40px", 1: "20px" }
                    }, {
                            name: "HeaderCellStyle",
                            decorator: detailHeaderDeco
                        }, {
                            name: "HeaderPopups",
                            menu: {
                                rows: [0],
                                items: [
                                    { id: "日付別", text: getText("KSU001_325"), selectHandler: function(id) { alert('Open KSU003'); } },
                                    { id: "シフト別", text: getText("KSU001_326"), selectHandler: function(id) { alert('Open KSC003'); } }
                                ]
                            },
                            //                            popup: {
                            //                                rows: [1],
                            //                                provider: function(columnKey) {
                            //                                    self.selectedDate(moment(columnKey, '_YYYYMMDD').format('YYYY/MM/DD'));
                            //                                    return $("#popup-area8");
                            //                                }
                            //                            }
                        }]
                };

                let updateDetailContent = {
                    columns: newDetailColumns,
                    dataSource: newDetailContentDs,
                    features: [{
                        name: "BodyCellStyle",
                        decorator: detailContentDeco
                    }]
                };

                let updateHorzSumHeader = {
                    columns: newDetailColumns,
                    dataSource: newDetailHeaderDs,
                    features: [{
                        name: "HeaderCellStyle",
                        decorator: detailHeaderDeco
                    }]
                };

                let updateHorzSumContent = {
                    columns: newDetailColumns,
                    dataSource: horzSumContentDs
                };

                let updateVertSumContent = {
                    dataSource: newVertSumContentDs,
                };

                let updateLeftHorzSumContent = {
                    dataSource: newLeftHorzContentDs
                };

                $("#extable").exTable("updateTable", "leftmost", {}, updateLeftmostContent);
                //                $("#extable").exTable("updateTable", "middle", {}, updateMiddleContent);
                //                $("#extable").exTable("updateTable", "verticalSummaries", {}, updateVertSumContent);
                //                $("#extable").exTable("updateTable", "leftHorizontalSummaries", {}, updateLeftHorzSumContent);
                $("#extable").exTable("updateTable", "detail", updateDetailHeader, updateDetailContent);
                //                $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, updateHorzSumContent);

                $("#extable").on("extablecellupdated", function() { });
                $("#extable").on("extablerowupdated", function() { });

                // scroll Back
                setTimeout(function() {
                    $("#extable").exTable("scrollBack", 2);
                }, 1000);

                //set lock cell
                _.forEach(self.dataSource(), (x) => {
                    if (x.confirmedAtr == 1) {
                        $("#extable").exTable("lockCell", x.employeeId, "_" + moment(x.date, 'YYYY/MM/DD').format('YYYYMMDD'));
                    }
                });
                // get cell is locked
                self.arrLockCellInit($("#extable").exTable("lockCells"));

                // validate when stick data in cell
                $("#extable").exTable("stickValidate", function(rowIdx, key, data) {
                    if (__viewContext.viewModel.viewO.selectedWorkTimeCode() == '据え置き') {
                        let dataS: BasicSchedule =
                            _.find(self.dataSource(), { 'date': moment(key, '_YYYYMMDD').format('YYYY/MM/DD'), 'employeeId': self.listSid()[rowIdx] });
                        let wTimeCode: string = dataS ? dataS.workTimeCode : null;
                        let wTime: any =
                            wTimeCode ? _.find(__viewContext.viewModel.viewO.listWorkTime(), { 'workTimeCode': wTimeCode }) : null;
                        let wTimeName: string = wTime ? wTime.abName : null;
                        $("#extable").exTable("stickData", {
                            workTypeCode: data.workTypeCode,
                            workTypeName: data.workTypeName,
                            workTimeCode: wTimeCode,
                            workTimeName: wTimeName,
                            startTime: dataS ? dataS.scheduleStartClock : '',
                            endTime: dataS ? dataS.scheduleEndClock : ''
                        });
                        return true;
                    }

                    let workTypeCd: any = _.find(self.listCheckNeededOfWorkTime(), ['workTypeCode', data.workTypeCode]);
                    // if workTypeCode is not required( state = 2) worktime is needless
                    if (workTypeCd && workTypeCd.state == 2 && !_.isEmpty(data.workTimeCode)) {
                        return function() {
                            alertError({ messageId: 'Msg_434' });
                        };
                    }
                    // if workTypeCode is required( state = 0) worktime is need
                    if (workTypeCd && workTypeCd.state == 0 && _.isEmpty(data.workTimeCode)) {
                        return function() {
                            alertError({ messageId: 'Msg_435' });
                        };
                    }

                    return true;
                });
                
                // set color for cell
                $("#extable").exTable("stickStyler", function(rowIdx, key, innerIdx, data) {
                    let stateWorkTypeCode = _.find(self.listStateWorkTypeCode(), { 'workTypeCode': data.workTypeCode }).state;
                    if (stateWorkTypeCode == 3) return { textColor: "#0000ff" }; // color-attendance
                    else if (stateWorkTypeCode == 0) return { textColor: "#ff0000" };// color-schedule-sunday
                    else return { textColor: "#FF7F27" };// color-half-day-work
                });
            }).always(() => {
                self.stopRequest(true);
            });
        }

        /**
         * update new data of header and content of detail and horizSum
         */
        updateDetailAndHorzSum(): void {
            let self = this;
            // save scroll's position
            $("#extable").exTable("saveScroll");
            //Get dates in time period
            let currentDay = new Date(self.dtPrev().toString());
            self.arrDay = [];
            let newDetailColumns = [], newObjDetailHeaderDs = [], newDetailHeaderDs = [], newDetailContentDs = [];
            while (moment(currentDay).format('YYYY-MM-DD') <= moment(self.dtAft()).format('YYYY-MM-DD')) {
                self.arrDay.push(new Time(currentDay));
                let time = new Time(currentDay);
                //define the new detailColumns
                newDetailColumns.push({
                    key: "_" + time.yearMonthDay, width: "50px", handlerType: "input", dataType: "duration/duration", primitiveValue: "TimeWithDayAttr", visible: true
                });
                //create new detailHeaderDs
                newObjDetailHeaderDs['_' + time.yearMonthDay] = '';

                currentDay.setDate(currentDay.getDate() + 1);
            }

            //create new detailHeaderDs
            newDetailHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));
            newDetailHeaderDs.push(newObjDetailHeaderDs);

            //get new horzSumContentDs
            let horzSumContentDs = [];
            for (let i = 0; i < 5; i++) {
                let obj = {};
                obj["itemId"] = i.toString();
                obj["empId"] = "";
                for (let j = 0; j < self.arrDay.length; j++) {
                    obj['_' + self.arrDay[j].yearMonthDay] = "10";
                }
                horzSumContentDs.push(obj);
            }

            let detailHeaderDeco = [], detailContentDeco = [];

            //if haven't data in extable, only update header detail and header horizontal
            if (self.empItems().length == 0) {
                $.when(self.setColorForCellHeaderDetailAndHoz(detailHeaderDeco)).done(() => {
                    let updateDetailHeader = {
                        columns: newDetailColumns,
                        dataSource: newDetailHeaderDs,
                        features: [{
                            name: "HeaderRowHeight",
                            rows: { 0: "40px", 1: "20px" }
                        }, {
                                name: "HeaderCellStyle",
                                decorator: detailHeaderDeco
                            }, {
                                name: "HeaderPopups",
                                menu: {
                                    rows: [0],
                                    items: [
                                        { id: "日付別", text: getText("KSU001_325"), selectHandler: function(id) { alert('Open KSU003'); } },
                                        { id: "シフト別", text: getText("KSU001_326"), selectHandler: function(id) { alert('Open KSC003'); } }
                                    ]
                                },
                                //                                popup: {
                                //                                    rows: [1],
                                //                                    provider: function(columnKey) {
                                //                                        self.selectedDate(moment(columnKey, '_YYYYMMDD').format('YYYY/MM/DD'));
                                //                                        return $("#popup-area8");
                                //                                    }
                                //                                }

                            }]
                    };

                    let updateHorzSumHeader = {
                        columns: newDetailColumns,
                        dataSource: newDetailHeaderDs,
                        features: [{
                            name: "HeaderCellStyle",
                            decorator: detailHeaderDeco
                        }]
                    };

                    $("#extable").exTable("updateTable", "detail", updateDetailHeader, {});
                    //                    $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, {});

                    setTimeout(function() { $("#extable").exTable("scrollBack", 2); }, 1000);

                    //set lock cell
                    _.forEach(self.dataSource(), (x) => {
                        if (x.confirmedAtr == 1) {
                            $("#extable").exTable("lockCell", x.employeeId, "_" + moment(x.date, 'YYYY/MM/DD').format('YYYYMMDD'));
                        }
                    });
                    // get cell is locked
                    self.arrLockCellInit($("#extable").exTable("lockCells"));

                    self.stopRequest(true);
                });
            } else if (self.selectedModeDisplayObject() == 1) {
                self.setDatasource().done(() => {
                    self.setColor(detailHeaderDeco, detailContentDeco).done(() => {
                        let updateDetailHeader = {
                            columns: newDetailColumns,
                            dataSource: newDetailHeaderDs,
                            features: [{
                                name: "HeaderRowHeight",
                                rows: { 0: "40px", 1: "20px" }
                            }, {
                                    name: "HeaderCellStyle",
                                    decorator: detailHeaderDeco
                                }, {
                                    name: "HeaderPopups",
                                    menu: {
                                        rows: [0],
                                        items: [
                                            { id: "日付別", text: getText("KSU001_325"), selectHandler: function(id) { alert('Open KSU003'); } },
                                            { id: "シフト別", text: getText("KSU001_326"), selectHandler: function(id) { alert('Open KSC003'); } }
                                        ]
                                    },
                                    //                                    popup: {
                                    //                                        rows: [1],
                                    //                                        provider: function(columnKey) {
                                    //                                            self.selectedDate(moment(columnKey, '_YYYYMMDD').format('YYYY/MM/DD'));
                                    //                                            return $("#popup-area8");
                                    //                                        }
                                    //                                    }

                                }]
                        };

                        //intended data display mode 
                        //dataSource of detail
                        _.each(self.listSid(), (x) => {
                            let dsOfSid: any = _.filter(self.dataSource(), ['employeeId', x]);
                            newDetailContentDs.push(new ExItem(x, dsOfSid, __viewContext.viewModel.viewO.listWorkType(),
                                __viewContext.viewModel.viewO.listWorkTime(), false, self.arrDay));
                        });

                        let updateDetailContent = {
                            columns: newDetailColumns,
                            dataSource: newDetailContentDs,
                            features: [{
                                name: "BodyCellStyle",
                                decorator: detailContentDeco
                            }]
                        };

                        let updateHorzSumHeader = {
                            columns: newDetailColumns,
                            dataSource: newDetailHeaderDs,
                            features: [{
                                name: "HeaderCellStyle",
                                decorator: detailHeaderDeco
                            }]
                        };

                        let updateHorzSumContent = {
                            columns: newDetailColumns,
                            dataSource: horzSumContentDs
                        };
                        
                        $("#extable").exTable("updateTable", "detail", updateDetailHeader, updateDetailContent);
                        //                        $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, updateHorzSumContent);

                        setTimeout(function() { $("#extable").exTable("scrollBack", 2); }, 1000);

                        //set lock cell
                        _.forEach(self.dataSource(), (x) => {
                            if (x.confirmedAtr == 1) {
                                $("#extable").exTable("lockCell", x.employeeId, "_" + moment(x.date, 'YYYY/MM/DD').format('YYYYMMDD'));
                            }
                        });
                        // get cell is locked
                        self.arrLockCellInit($("#extable").exTable("lockCells"));
                    });
                }).always(() => {
                    self.stopRequest(true);
                });
            } else if (self.selectedModeDisplayObject() == 2) {
                // in phare 2, set dataSource of actual data = dataSource of intended data
                self.setDatasource().done(() => {
                    self.setColor(detailHeaderDeco, detailContentDeco).done(() => {
                        let updateDetailHeader = {
                            columns: newDetailColumns,
                            dataSource: newDetailHeaderDs,
                            features: [{
                                name: "HeaderRowHeight",
                                rows: { 0: "40px", 1: "20px" }
                            }, {
                                    name: "HeaderCellStyle",
                                    decorator: detailHeaderDeco
                                }, {
                                    name: "HeaderPopups",
                                    menu: {
                                        rows: [0],
                                        items: [
                                            { id: "日付別", text: getText("KSU001_325"), selectHandler: function(id) { alert('Open KSU003'); } },
                                            { id: "シフト別", text: getText("KSU001_326"), selectHandler: function(id) { alert('Open KSC003'); } }
                                        ]
                                    },
                                    //                                    popup: {
                                    //                                        rows: [1],
                                    //                                        provider: function(columnKey) {
                                    //                                            self.selectedDate(moment(columnKey, '_YYYYMMDD').format('YYYY/MM/DD'));
                                    //                                            return $("#popup-area8");
                                    //                                        }
                                    //                                    }
                                }]
                        };

                        // actual data display mode , if hasn't actual data, display intended data
                        _.each(self.listSid(), (x) => {
                            let dsOfSid: any = _.filter(self.dataSource(), ['employeeId', x]);
                            newDetailContentDs.push(new ExItem(x, dsOfSid, __viewContext.viewModel.viewO.listWorkType(),
                                __viewContext.viewModel.viewO.listWorkTime(), false, self.arrDay));
                        });

                        let updateDetailContent = {
                            columns: newDetailColumns,
                            dataSource: newDetailContentDs,
                            features: [{
                                name: "BodyCellStyle",
                                decorator: detailContentDeco
                            }]
                        };

                        let updateHorzSumHeader = {
                            columns: newDetailColumns,
                            dataSource: newDetailHeaderDs,
                            features: [{
                                name: "HeaderCellStyle",
                                decorator: detailHeaderDeco
                            }]
                        };

                        let updateHorzSumContent = {
                            columns: newDetailColumns,
                            dataSource: horzSumContentDs
                        };
                        
                        $("#extable").exTable("updateTable", "detail", updateDetailHeader, updateDetailContent);
                        //                        $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, updateHorzSumContent);

                        setTimeout(function() { $("#extable").exTable("scrollBack", 2); }, 1000);

                        //set lock cell
                        _.forEach(self.dataSource(), (x) => {
                            if (x.confirmedAtr == 1) {
                                $("#extable").exTable("lockCell", x.employeeId, "_" + moment(x.date, 'YYYY/MM/DD').format('YYYYMMDD'));
                            }
                        });
                        // get cell is locked
                        self.arrLockCellInit($("#extable").exTable("lockCells"));
                    });
                }).always(() => {
                    self.stopRequest(true);
                });
            } else {
                self.stopRequest(true);
            }
        }

        /**
         * Shift condition A2_4
         */
        initShiftCondition(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            service.buildTreeShiftCondition().done(function(itemsTree) {
                self.itemsTree(itemsTree);
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Get data of Basic Schedule = listDataShortName + listDataTimeZone + dataWScheState
         */
        getDataBasicSchedule(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred(),
                obj = {
                    employeeId: self.listSid(),
                    startDate: self.dtPrev(),
                    endDate: self.dtAft()
                };

            service.getDataBasicSchedule(obj).done(function(datas) {
                let arrTmp: any = [];
                self.dataSource([]);
                _.each(datas, data =>{
                    arrTmp.push(new BasicSchedule({
                            date: data.date,
                            employeeId: data.employeeId,
                            workTimeCode: data.workTimeCode,
                            workTypeCode: data.workTypeCode,
                            confirmedAtr: data.confirmedAtr,
                            isIntendedData: true, // = true la du lieu du dinh
                            scheduleCnt: data.scheduleCnt,
                            scheduleStartClock: data.scheduleStartClock,
                            scheduleEndClock: data.scheduleEndClock,
                            bounceAtr: data.bounceAtr
                        }));
                });
                
                self.dataSource(arrTmp);
                
//                //set dataSource for mode shortName
//                self.dataSource([]);
//                _.each(data.listDataShortName, (itemData: BasicSchedule) => {
//                    let itemDataSource: BasicSchedule = _.find(self.dataSource(), { 'employeeId': itemData.employeeId, 'date': itemData.date });
//                    if (itemDataSource) {
//                        itemDataSource.workTimeCode = itemData.workTimeCode;
//                        itemDataSource.workTypeCode = itemData.workTypeCode;
//                        itemDataSource.confirmedAtr = itemData.confirmedAtr;
//                        itemDataSource.isIntendedData = true
//                    } else {
//                        self.dataSource.push(new BasicSchedule({
//                            date: itemData.date,
//                            employeeId: itemData.employeeId,
//                            workTimeCode: itemData.workTimeCode,
//                            workTypeCode: itemData.workTypeCode,
//                            confirmedAtr: itemData.confirmedAtr,
//                            isIntendedData: true
//                        }));
//                    }
//                });
//
//                //set dataSource for mode timeZone
//                _.each(self.dataSource(), (itemDataSource: BasicSchedule) => {
//                    let itemDataTimeZone: any = _.find(data.listDataTimeZone, { 'employeeId': itemDataSource.employeeId, 'date': itemDataSource.date });
//                    if (itemDataTimeZone) {
//                        itemDataSource.scheduleStartClock = itemDataTimeZone.scheduleStartClock;
//                        itemDataSource.scheduleEndClock = itemDataTimeZone.scheduleEndClock;
//                    } else {
//                        itemDataSource.scheduleStartClock = null;
//                        itemDataSource.scheduleEndClock = null;
//                    }
//                });

                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data to display symbol for dataSource(exTable)
         */
        setDataToDisplaySymbol(dataS): void {
            let self = this;
            if (self.dataScheduleDisplayControl() && +self.dataScheduleDisplayControl().symbolAtr == 1) {
                _.each(dataS, (x) => {
                    let workEmpCombine = _.find(self.dataWorkEmpCombine(), { 'workTypeCode': x.workTypeCode, 'workTimeCode': x.workTimeCode });
                    if (workEmpCombine) {
                        x.symbolName = workEmpCombine.symbolName;
                    } else {
                        self.handleSetSymbolForCell(x);
                    }
                });
            } else {
                _.each(dataS, (item) => {
                    self.handleSetSymbolForCell(item);
                });
            }
        }

        handleSetSymbolForCell(item: any): void {
            let self = this;
            let symbolName: string = '';
            if (_.isEmpty(item.workTimeCode)) {
                let workTypeItem: any = _.find(__viewContext.viewModel.viewO.listWorkType(), { 'workTypeCode': item.workTypeCode });
                symbolName = workTypeItem ? workTypeItem.symbolicName : '';
            } else {
                let workTimeItem: any = _.find(__viewContext.viewModel.viewO.listWorkTime(), { 'workTimeCode': item.workTimeCode });
                symbolName = workTimeItem ? workTimeItem.symbolName : '';
            }
            //state = 0 || 3 : rest || work all day
            //state = 1 || 2 : work in morning || work in afternoon
            let stateWorkTypeCode = _.find(self.listStateWorkTypeCode(), { 'workTypeCode': item.workTypeCode });
            if (stateWorkTypeCode) {
                let state = stateWorkTypeCode.state;
                if (state == 1 && self.dataScheduleDisplayControl() && +self.dataScheduleDisplayControl().symbolHalfDayAtr == 1) {
                    item.symbolName = symbolName + self.dataScheduleDisplayControl().symbolHalfDayName;
                } else if (state == 2 && self.dataScheduleDisplayControl() && +self.dataScheduleDisplayControl().symbolHalfDayAtr == 1) {
                    item.symbolName = self.dataScheduleDisplayControl().symbolHalfDayName + symbolName;
                } else {
                    //                    if (state == 0 || state == 3) {
                    item.symbolName = symbolName;
                }
            }
        }

        /**
        * datasource = dataBasicSchedule + dataToDisplaySymbol
        */
        setDatasource(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            $.when(self.getDataBasicSchedule()).done(function() {
                // set data hien thi o mode symbol
                self.setDataToDisplaySymbol(self.dataSource())
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Get data WorkScheduleState
         * with itemId = 1~4 (set on server)
         */
        getDataWorkScheduleState(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    employeeId: self.listSid(),
                    startDate: self.dtPrev(),
                    endDate: self.dtAft(),
                };
            service.getDataWorkScheduleState(obj).done(function(data) {
                self.dataWScheduleState(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data WkpSpecificDate, ComSpecificDate, PublicHoliday
         */
        getDataSpecDateAndHoliday(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    workplaceId: self.empItems()[0] ? self.empItems()[0].workplaceId : null,
                    startDate: self.dtPrev(),
                    endDate: self.dtAft()
                };
            service.getDataSpecDateAndHoliday(obj).done(function(data) {
                self.dataWkpSpecificDate(data.listWkpSpecificDate);
                self.dataComSpecificDate(data.listComSpecificDate);
                self.dataPublicHoliday(data.listPublicHoliday);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data WorkEmpCombine
         */
        getDataWorkEmpCombine(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred(), lstWorkTypeCode: any[] = [], lstWorkTimeCode: any[] = [], obj: any = null;
            _.each(__viewContext.viewModel.viewO.listWorkType(), (item) => {
                lstWorkTypeCode.push(item.workTypeCode);
            });
            _.each(__viewContext.viewModel.viewO.listWorkTime(), (item) => {
                lstWorkTimeCode.push(item.workTimeCode);
            });

            obj = {
                lstWorkTypeCode: lstWorkTypeCode,
                lstWorkTimeCode: lstWorkTimeCode
            }
            service.getDataWorkEmpCombine(obj).done(function(data) {
                self.dataWorkEmpCombine(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data of Schedule Display Control
         */
        getDataScheduleDisplayControl(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            service.getDataScheduleDisplayControl().done(function(data) {
                self.dataScheduleDisplayControl(data);
                //TO-DO
                //                if (!!data && data.personInforAtr == 7) {
                //                    self.isInsuranceStatus = true;
                //                }
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
        * next a month
        */
        nextMonth(): void {
            let self = this;
            self.stopRequest(false);
            if (self.selectedTimePeriod() == 1) {
                // Recalculate the time period
                let dtMoment = moment(self.dtAft());
                dtMoment.add(1, 'days');
                self.dtPrev(dtMoment.toDate());
                dtMoment = dtMoment.add(1, 'months');
                dtMoment.subtract(1, 'days');
                self.dtAft(dtMoment.toDate());
                self.dataSource([]);
                self.updateDetailAndHorzSum();
            } else {
                self.stopRequest(true);
            }
        }

        /**
        * come back a month
        */
        backMonth(): void {
            let self = this;
            self.stopRequest(false);
            if (self.selectedTimePeriod() == 1) {
                //Recalculate the time period
                let dtMoment = moment(self.dtPrev());
                dtMoment.subtract(1, 'days');
                self.dtAft(dtMoment.toDate());
                if (dtMoment.date() === dtMoment.daysInMonth()) {
                    dtMoment = dtMoment.subtract(1, 'months');
                    dtMoment.endOf('months');
                } else {
                    dtMoment = dtMoment.subtract(1, 'months');
                }
                dtMoment.add(1, 'days');
                self.dtPrev(new Date(dtMoment.format('YYYY/MM/DD')));
                self.dataSource([]);
                self.updateDetailAndHorzSum();
            } else {
                self.stopRequest(true);
            }
        }

        /**
        * Save data
        */
        saveData(): void {
            let self = this;
            setTimeout(function() {
                let arrObj: any[] = [],
                    arrCell: Cell[] = $("#extable").exTable("updatedCells"),
                    arrTmp: Cell[] = _.clone(arrCell),
                    arrLockCellAfterSave: Cell[] = $("#extable").exTable("lockCells"),
                    newArrCell = [];
                
                // compare 2 array lockCell init and after
                if (arrCell.length == 0 && _.isEqual(self.arrLockCellInit(), arrLockCellAfterSave)) {
                    return;
                }
                
                self.stopRequest(false);
                                    
                let arrNewCellIsLocked: any[] = _.differenceWith(arrLockCellAfterSave, self.arrLockCellInit(), _.isEqual),
                    arrNewCellIsUnlocked: any[] = _.differenceWith(self.arrLockCellInit(), arrLockCellAfterSave, _.isEqual);

                // neu o mode time thi can merge cac object giong nhau vao thanh 1
                if (self.selectedModeDisplay() == 2) {
                    _.each(arrTmp, (item) => {
                        let arrFilter = _.filter(arrTmp, { 'rowIndex': item.rowIndex, 'columnKey': item.columnKey });
                        if (arrFilter.length > 1) {
                            let sTime: any = '', eTime: any = '';
                            _.each(arrFilter, (data) => {
                                if (data.innerIdx == 0) {
                                    sTime = data.value.startTime;
                                } 
                                if (data.innerIdx == 1) {
                                    eTime = data.value.endTime;
                                }
                                _.remove(arrCell, data);
                            });
                            // set innerIdx = -1: do sua startTime va endTime (=> cell) trong mode Time
                            arrCell.push(new Cell({
                                rowIndex: item.rowIndex,
                                columnKey: item.columnKey,
                                value: new ksu001.common.viewmodel.ExCell ({
                                    workTypeCode: item.value.workTypeCode,
                                    workTypeName: item.value.workTypeName,
                                    workTimeCode: item.value.workTimeCode,
                                    workTimeName: item.value.workTimeName,
                                    symbolName: item.value.symbolName,
                                    startTime: sTime,
                                    endTime: eTime,
                                }),
                                innerIdx: -1,    
                            }));
                        }
                    });
                }
                
                // distinct arrCell- do khi thay doi 1 cell co 2 row thi a Manh ban ra 2 cell vs innerIdx khac nhau
                _.each(arrCell, cell => {
                    if (!_.find(newArrCell, { 'rowIndex': cell.rowIndex, 'columnKey': cell.columnKey })) {
                        newArrCell.push(cell);
                    };
                });
                arrCell = newArrCell;
                
                arrNewCellIsUnlocked = _.differenceBy(arrNewCellIsUnlocked, arrCell, ['rowIndex', 'columnKey']);
                arrCell.push.apply(arrCell, arrNewCellIsUnlocked);
                arrCell = _.differenceBy(arrCell, arrNewCellIsLocked, ['rowIndex', 'columnKey']);

                for (let i = 0; i < arrCell.length; i += 1) {
                    let cell: any = arrCell[i], valueCell = cell.value, sid = self.listSid()[Number(cell.rowIndex)];
                    
                     // slice string '_YYYYMMDD' to 'YYYYMMDD'
                    let date: string = moment.utc(cell.columnKey.slice(1, cell.columnKey.length), 'YYYYMMDD').toISOString(),
                        basicSchedule: any = _.find(self.dataSource(), {'employeeId': sid, 'date': moment(date).format('YYYY/MM/DD') }),
                        bounceAtr: any = 1, //set static bounceAtr =  1
                        confirmedAtr: any =  0,
                        workScheduleTimeZone: any = (self.selectedModeDisplay() != 1 && valueCell.workTimeCode != null) ? [{
                            scheduleCnt: 1,
                            scheduleStartClock: (typeof valueCell.startTime === 'number') ? valueCell.startTime
                                : (valueCell.startTime ? nts.uk.time.minutesBased.clock.dayattr.parseString(valueCell.startTime).asMinutes : null),
                            scheduleEndClock: (typeof valueCell.endTime === 'number') ? valueCell.endTime
                                : (valueCell.endTime ? nts.uk.time.minutesBased.clock.dayattr.parseString(valueCell.endTime).asMinutes : null),
                            bounceAtr: bounceAtr
                        }] : null;
                        
                    arrObj.push({
                        date: date,
                        employeeId: sid,
                        workTimeCode: valueCell.workTimeCode,
                        workTypeCode: valueCell.workTypeCode,
                        confirmedAtr: confirmedAtr,
                        workScheduleTimeZoneSaveCommands: workScheduleTimeZone
                    });
                }
                
                for (let i = 0; i < arrNewCellIsLocked.length; i += 1) {
                    let newCellIsLocked: any = arrNewCellIsLocked[i], 
                        valueNewCellIsLocked = newCellIsLocked.value,
                        sid = self.listSid()[newCellIsLocked.rowIndex];
                    if(!valueNewCellIsLocked.workTypeCode) { 
                        $("#extable").exTable("unlockCell", sid, newCellIsLocked.columnKey);
                        continue;
                    }
                    // slice string '_YYYYMMDD' to 'YYYYMMDD'
                    let date: string = moment.utc(newCellIsLocked.columnKey.slice(1, newCellIsLocked.columnKey.length), 'YYYYMMDD').toISOString(),
                        basicSchedule: any = _.find(self.dataSource(), {'employeeId': sid, 'date': moment(date).format('YYYY/MM/DD') }),
                        bounceAtr: any = 1, //set static bounceAtr =  1
                        confirmedAtr: any =  1,
                        workScheduleTimeZone: any = (valueNewCellIsLocked.workTimeCode != null && self.selectedModeDisplay() != 1) ? [{
                            scheduleCnt: 1,
                            scheduleStartClock: (typeof valueNewCellIsLocked.startTime === 'number') ? valueNewCellIsLocked.startTime
                                : (valueNewCellIsLocked.startTime ? nts.uk.time.minutesBased.clock.dayattr.parseString(valueNewCellIsLocked.startTime).asMinutes : null),
                            scheduleEndClock: (typeof valueNewCellIsLocked.endTime === 'number') ? valueNewCellIsLocked.endTime
                                : (valueNewCellIsLocked.endTime ? nts.uk.time.minutesBased.clock.dayattr.parseString(valueNewCellIsLocked.endTime).asMinutes : null),
                            bounceAtr: bounceAtr
                        }] : null;
                        
                    arrObj.push({
                        date: date,
                        employeeId: self.listSid()[Number(newCellIsLocked.rowIndex)],
                        workTimeCode: valueNewCellIsLocked.workTimeCode,
                        workTypeCode: valueNewCellIsLocked.workTypeCode,
                        confirmedAtr: confirmedAtr,
                        workScheduleTimeZoneSaveCommands: workScheduleTimeZone
                    });
                }
                
                if(arrObj.length <= 0){
                    self.stopRequest(true);
                    return;
                }
                
                let dataRegisterBasicSchedule: any = {
                    modeDisplay: self.selectedModeDisplay(),
                    listRegisterBasicSchedule: arrObj
                };
                
                service.registerData(dataRegisterBasicSchedule).done(function(error: any) {
                    //get data and update extable
                    self.setDatasource().done(function() {
                        self.updateExTable();
                        if (error.length != 0) {
                            self.addListError(error);
                        }
                    });
                }).fail(function(error: any) {
                    alertError({ messageId: error.messageId });
                    self.setDatasource().done(function() {
                        self.updateExTable();
                    });
                }).always(() => {
                    self.stopRequest(true);
                });
            }, 1);
        }

        /**
         * Set color for cell = set text color + set background color
         */
        setColorForCell(detailContentDeco: any): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            
            /* set color for text in cell - 明細セル文字色の判断処理 */
            if (self.selectedModeDisplayObject() == 2) {
                // isIntendedData': false => this is actual data
                let arrActualData: any[] = _.filter(self.dataSource(), { 'isIntendedData': false });
                if (arrActualData.length > 0) {
                    _.each(arrActualData, (item: BasicSchedule) => {
                        detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(new Date(item.date)).format('YYYYMMDD'), item.employeeId, "color-schedule-performance", 0));
                        detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(new Date(item.date)).format('YYYYMMDD'), item.employeeId, "color-schedule-performance", 1));
                    });
                }
            }
            
            // lstData: list object in dataSource. It has workTypeCode, which exist in master data WORKTYPE
            let lstData: BasicSchedule[] = [];
            _.each(__viewContext.viewModel.viewO.listWorkType(), (item) => {
                let obj = _.filter(self.dataSource(), { 'workTypeCode': item.workTypeCode });
                if (obj) {
                    lstData.push.apply(lstData, obj);
                }
            });
            
            if (self.selectedModeDisplay() == 2) {
                // when mode is time(時刻) dont set color for text in cell
                _.each(lstData, (item) => {
                    detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, '', 0));
                    detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, '', 1));
                });
            } else {
                // when mode is shortName(略名) and mode symbol(記号)
                let lstWorkTypeCode = [],
                    lstIntendedData = _.filter(self.dataSource(), { 'isIntendedData': true });
                if (lstIntendedData.length > 0) {
                    _.map(lstIntendedData, (x) => {
                        if (!_.includes(lstWorkTypeCode, x.workTypeCode)) {
                            lstWorkTypeCode.push(x.workTypeCode);
                        }
                    });
                }

                _.each(lstData, (item) => {
                    let stateWorkTypeCode = _.find(self.listStateWorkTypeCode(), { 'workTypeCode': item.workTypeCode });
                    if (stateWorkTypeCode) {
                        let state = stateWorkTypeCode.state;
                        if (state == 3) {
                            //state == 3 is work-day
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, "color-attendance", 0));
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, "color-attendance", 1));
                        } else if (state == 0) {
                            //state == 0 is holiday-day
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, "color-holiday", 0));
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, "color-holiday", 1));
                        } else {
                            //state == 1 || 2 is work-half-day
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, "color-half-day-work", 0));
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor("_" + moment(item.date, 'YYYY/MM/DD', true).format('YYYYMMDD'), item.employeeId, "color-half-day-work", 1));
                        }
                    }
                });
            }
            
            /* set background color for cell*/
            
            //            if (self.selectedBackgroundColor() === '001') {
            // Return value：就業時間帯 -> query table WorkTime to get color code
            // TO-DO
            //                dfd.resolve();
            //            } else {
            // TO-DO
            // 日単位でチェック handler will return state 　非表示　or 確定　or 応援者　or 修正不可
            
            // get data from WorkScheduleState
            self.getDataWorkScheduleState().done(() => {
                let data = [],
                    innerIdx: number = 1;
                if (self.selectedModeDisplay() === 3) {
                    // refer スケジュール明細セル　背景色制御 in document
                    //scheduleItemId = 1: it is id of WorkType
                    //scheduleItemId = 2: it is id of WorkTime
                    data = _.reduce(self.dataWScheduleState(), (result, item) => {
                        if (item.scheduleItemId == 1 || item.scheduleItemId == 2) {
                            // ham find ben duoi luon chi tim duoc toi da 1 gia tri
                            let scheState: any = _.find(result, { 'employeeId': item.employeeId, 'date': item.date });
                            if (scheState == undefined) {
                                // neu chua co data cho employeeId va date tuong ung thi insert
                                result.push(item);
                            } else if (scheState.scheduleEditState > item.scheduleEditState) {
                                // neu co data cho employeeId va date tuong ung nhung state khong uu tien thi remove roi insert
                                _.remove(result, { 'employeeId': item.employeeId, 'date': item.date });
                                result.push(item);
                            }
                        }
                        return result;
                    }, []);
                } else {
                    // Return value of ScheduleEditState of WorkScheduleState
                    if (self.selectedModeDisplay() === 2) {
                        //scheduleItemId = 3: it is id of StartTime
                        //scheduleItemId = 4: it is id of EndTime
                        data = _.reduce(self.dataWScheduleState(), (result, item) => {
                            if (item.scheduleItemId == 3 || item.scheduleItemId == 4) {
                                result.push(item);
                            }
                            return result;
                        }, []);
                    } else {
                        //scheduleItemId = 1: it is id of WorkType
                        //scheduleItemId = 2: it is id of WorkTime
                        data = _.reduce(self.dataWScheduleState(), (result, item) => {
                            if (item.scheduleItemId == 1 || item.scheduleItemId == 2) {
                                result.push(item);
                            }
                            return result;
                        }, []);
                    }

                }
                // set background color    
                _.each(data, (item) => {
                    let columnKey: string = "_" + moment(new Date(item.date)).format('YYYYMMDD');
                    // neu la mode symbol thi luon set innerIdx = 0 de mau background luon hien thi dung
                    // do mau background luon hien thi mau theo innerIdx = 0
                    if (self.selectedModeDisplay() === 3) {
                        innerIdx = 0;
                    } else {
                        if (item.scheduleItemId == 1 || item.scheduleItemId == 3) {
                            innerIdx = 0;
                        } else {
                            innerIdx = 1;
                        }
                    }
                    
                    if (item.scheduleEditState == 1) {
                        //手修正(本人) = bg-daily-alter-self
                        let cell = _.find(detailContentDeco, { 'columnKey': columnKey, 'rowId': item.employeeId, 'innerIdx': innerIdx });
                        if (_.isNil(cell)){
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor(columnKey, item.employeeId, "bg-daily-alter-self", innerIdx));
                        } else{
                            cell.clazz = 'bg-daily-alter-self' + (cell.clazz == '' ? '' : ' ') + cell.clazz;
                        } 
                    } else if (item.scheduleEditState == 2) {
                        //手修正(他人) = bg-daily-alter-other
                        let cell = _.find(detailContentDeco, { 'columnKey': columnKey, 'rowId': item.employeeId, 'innerIdx': innerIdx });
                        if (_.isNil(cell)){
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor(columnKey, item.employeeId, "bg-daily-alter-other", innerIdx));
                        } else {
                            cell.clazz = 'bg-daily-alter-other' + (cell.clazz == '' ? '' : ' ') + cell.clazz;
                        } 
                    } else if (item.scheduleEditState == 3) {
                        //申請反映 = bg-daily-reflect-application
                        let cell = _.find(detailContentDeco, { 'columnKey': columnKey, 'rowId': item.employeeId, 'innerIdx': innerIdx });
                        if (_.isNil(cell)){
                            detailContentDeco.push(new ksu001.common.viewmodel.CellColor(columnKey, item.employeeId, "bg-daily-reflect-application", innerIdx));    
                        } else {
                            cell.clazz = 'bg-daily-reflect-application' + (cell.clazz == '' ? '' : ' ') + cell.clazz;
                        } 
                    }
                });
                
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Set color for cell header: 日付セル背景色文字色制御
         * 
         */
        setColorForCellHeaderDetailAndHoz(detailHeaderDeco: any): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();

            if (self.isClickChangeDisplayMode) {
                self.isClickChangeDisplayMode = false;
                _.map(self.listColorOfHeader(), (item) => {
                    detailHeaderDeco.push(item);
                });
                dfd.resolve();
                return;
            }
            // getDataSpecDateAndHoliday always query to server
            // because date is changed when click nextMonth or backMonth
            $.when(self.getDataSpecDateAndHoliday()).done(() => {
                _.each(self.arrDay, (date) => {
                    let ymd = date.yearMonthDay;
                    let dateFormat = moment(date.yearMonthDay).format('YYYY/MM/DD');
                    if (_.includes(self.dataWkpSpecificDate(), dateFormat) || _.includes(self.dataComSpecificDate(), dateFormat)) {
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 0, "bg-schedule-specific-date"));
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 1, "bg-schedule-specific-date"));
                    } else if (_.includes(self.dataPublicHoliday(), dateFormat)) {
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 0, "bg-schedule-sunday color-schedule-sunday"));
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 1, "bg-schedule-sunday color-schedule-sunday"));
                    } else if (date.weekDay === '土') {
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 0, "bg-schedule-saturday color-schedule-saturday"));
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 1, "bg-schedule-saturday color-schedule-saturday"));
                    } else if (date.weekDay === '日') {
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 0, "bg-schedule-sunday color-schedule-sunday"));
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 1, "bg-schedule-sunday color-schedule-sunday"));
                    } else {
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 0, "bg-weekdays color-weekdays"));
                        detailHeaderDeco.push(new ksu001.common.viewmodel.CellColor("_" + ymd, 1, "bg-weekdays color-weekdays"));
                    }
                });

                // set class bg-schedule-that-day for currentDay
                if (self.dateTimePrev() <= moment().format('YYYY/MM/DD') && moment().format('YYYY/MM/DD') <= self.dateTimeAfter()) {
                    let arrCellColorFilter = _.filter(detailHeaderDeco, ['columnKey', "_" + moment().format('YYYYMMDD')]);
                    _.map(arrCellColorFilter, (cellColor: any) => {
                        cellColor.clazz = cellColor.clazz.replace(/bg-\w*/g, 'bg-schedule-that-day');
                    });
                }

                self.listColorOfHeader(detailHeaderDeco);
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Set color for cell of leftmost : 個人名セルの背景色の判断処理
         */
        setColorForLeftmostContent(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            //            $.when(self.getDataScheduleDisplayControl()).done(() => {
            if (self.isInsuranceStatus) {
                //TO-DO    
            }
            dfd.resolve();
            //            });
            return dfd.promise();
        }

        /**
         * Set color for cell : 明細セル背景色の判断処理
         */
        setColor(detailHeaderDeco: any, detailContentDeco: any): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            $.when(self.setColorForCellHeaderDetailAndHoz(detailHeaderDeco),
                self.setColorForCell(detailContentDeco), self.setColorForLeftmostContent()).done(() => {
                    dfd.resolve();
                });
            return dfd.promise();
        }

        /**
         * Set error
         */
        addListError(errorsRequest: Array<string>) {
            let errors = [];
            _.forEach(errorsRequest, function(err) {
                let errSplits = err.split(",");
                if (errSplits.length == 1) {
                    errors.push({ message: nts.uk.resource.getMessage(err), messageId: err, supplements: {} });
                } else {
                    errors.push({
                        message: nts.uk.resource.getMessage(errSplits[0], [getText(errSplits[1]), getText(errSplits[2])]),
                        messageId: errSplits[0],
                        supplements: {}
                    });
                }
            });

            nts.uk.ui.dialog.bundledErrors({ errors: errors });
        }

        /**
         * paste data on cell
         */
        pasteData(): void {
            let self = this;
            $("#extable").exTable("updateMode", "stick");
             
            if (self.selectedModeDisplay() == 1) {
                // set sticker single
                $("#extable").exTable("stickData", __viewContext.viewModel.viewO.nameWorkTimeType());
                $("#extable").exTable("stickMode", "single");
            } else if (self.selectedModeDisplay() == 3) {
                $("#extable").exTable("stickMode", "multi");
            }
        }

        /**
         * copy data on cell
         */
        copyData(): void {
            $("#extable").exTable("updateMode", "copyPaste");
        }

        /**
         * undo data on cell
         */
        undoData(): void {
            $("#extable").exTable("stickUndo");
        }

        /**
         * get data form COM_PATTERN (for screen Q)
         */
        getDataComPattern(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            service.getDataComPattern().done((data) => {
                __viewContext.viewModel.viewQ.listComPattern(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });

            return dfd.promise();
        }

        /**
         * get data form WKP_PATTERN (for screen Q)
         */
        getDataWkpPattern(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            let obj: string = self.empItems()[0] ? self.empItems()[0].workplaceId : '';
            service.getDataWkpPattern(obj).done((data) => {
                __viewContext.viewModel.viewQ.listWkpPattern(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });

            return dfd.promise();
        }

        /**
         * get data of workplace by pk
         */
        getDataOfWorkPlace(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            let data = {
                workplaceId: self.workplaceId,
                baseDate: moment().toISOString()
            }
            service.getWorkPlaceById(data).done((wkp) => {
                self.workPlaceNameDisplay(_.isNil(wkp) ? null : wkp.wkpDisplayName);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });

            return dfd.promise();
        }

        /**
         * open dialog C
         */
        openDialogC(): void {
            let self = this;
            $('#popup-area3').ntsPopup('hide');
            nts.uk.ui.windows.setShared('selectionCondition', self.selectedCodeTree());
            nts.uk.ui.windows.setShared('startDate', self.dateTimePrev());
            nts.uk.ui.windows.setShared('endDate', self.dateTimeAfter());
            nts.uk.ui.windows.setShared("listEmployee", self.empItems());
            nts.uk.ui.windows.sub.modal("/view/ksu/001/c/index.xhtml");
        }

        /**
         * open dialog D
         */
        openDialogD(): void {
            let self = this;
            setShared('dataForScreenD', {
                dataSource: self.dataSource(),
                empItems: self.empItems(),
                startDate: moment(self.dtPrev()).format('YYYY/MM/DD'),
                endDate: moment(self.dtAft()).format('YYYY/MM/DD'),
                // in phare 2, permissionHandCorrection allow false
                permissionHandCorrection: false,
                listColorOfHeader: self.listColorOfHeader()
            });
            
            nts.uk.ui.windows.sub.modal("/view/ksu/001/d/index.xhtml").onClosed(() => {
                if (getShared("dataFromScreenD") && !getShared("dataFromScreenD").clickCloseDialog) {
                    self.dataSource([]);
                    self.updateDetailAndHorzSum();
//                    $.when(self.setDatasource()).done(() => {
//                        self.updateExTable();
//                    });
                }
            });
        }

        /**
         * open dialog L
         */
        openDialogL(): void {
            let self = this;
            $('#popup-area5').ntsPopup('hide');
            //hiện giờ truyền sang workplaceId va tất cả emmployee . Sau này sửa truyền list employee theo workplace id
            setShared("dataForScreenL", {
                workplaceId: self.empItems()[0] ? self.empItems()[0].workplaceId : null,
                empItems: self.empItems()
            });
            nts.uk.ui.windows.sub.modal("/view/ksu/001/l/index.xhtml");
        }

        /**
         * open dialog N
         */
        openDialogN(): void {
            let self = this;
            $('#popup-area5').ntsPopup('hide');
            nts.uk.ui.windows.setShared("listEmployee", self.empItems());
            nts.uk.ui.windows.sub.modal("/view/ksu/001/n/index.xhtml");
        }

        /**
         * go to screen KML004
         */
        gotoKml004(): void {
            nts.uk.ui.windows.sub.modal("/view/kml/004/a/index.xhtml");
        }

        /**
        * go to screen KML002
        */
        gotoKml002(): void {
            nts.uk.request.jump("/view/kml/002/h/index.xhtml");
        }
        
         /**
         * Open dialog CDL027
         */
        openCDL027(): void {
            let self = this,
                period = {
                    startDate : self.dateTimePrev(),
                    endDate : self.dateTimeAfter()  
                },
                param = {
                    pgid: __viewContext.program.programId,
                    functionId: 1,
                    listEmployeeId: self.listSid(),
                    period : period,
                    displayFormat: self.displayFormat() 
                };
            setShared("CDL027Params", param);
            openDialog('com',"/view/cdl/027/a/index.xhtml");
        }
        
        compareArrByRowIndexAndColumnKey(a: any, b: any): any {
            return a.rowIndex == b.rowIndex && a.comlumnKey == b.comlumnKey;
        }

    }

    class Node {
        code: string;
        name: string;
        nodeText: string;
        custom: string;
        childs: Array<Node>;
        constructor(code: string, name: string, childs: Array<Node>) {
            var self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;
            self.custom = 'Random' + new Date().getTime();
        }
    }

    class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }

    interface ICell {
        rowIndex: string,
        columnKey: string,
        value: ksu001.common.viewmodel.ExCell,
        innerIdx: number
    }

    class Cell {
        rowIndex: string;
        columnKey: string;
        value: ksu001.common.viewmodel.ExCell;
        innerIdx: number;

        constructor(params: ICell) {
            this.rowIndex = params.rowIndex;
            this.columnKey = params.columnKey;
            this.value = params.value;
            this.innerIdx = params.innerIdx;
        }
    }

    interface IPersonModel {
        empId: string,
        empCd: string,
        empName: string,
        workplaceId: string,
        wokplaceCd: string,
        workplaceName: string,
        baseDate?: number
    }

    class PersonModel {
        empId: string;
        empCd: string;
        empName: string;
        workplaceId: string;
        wokplaceCd: string;
        workplaceName: string;
        baseDate: number;

        constructor(param: IPersonModel) {
            this.empId = param.empId;
            this.empCd = param.empCd;
            this.empName = param.empName;
            this.workplaceId = param.workplaceId;
            this.wokplaceCd = param.wokplaceCd;
            this.workplaceName = param.workplaceName;
            this.baseDate = param.baseDate;
        }
    }

    interface IBasicSchedule {
        date: string,
        employeeId: string,
        workTimeCode?: string,
        workTypeCode?: string,
        confirmedAtr?: number,
        isIntendedData?: boolean,
        scheduleCnt?: number,
        scheduleStartClock?: number,
        scheduleEndClock?: number,
        bounceAtr?: number,
        symbolName?: string
    }

    class BasicSchedule {
        date: string;
        employeeId: string;
        workTimeCode: string;
        workTypeCode: string;
        confirmedAtr: number;
        isIntendedData: boolean;
        scheduleCnt: number;
        scheduleStartClock: number;
        scheduleEndClock: number;
        bounceAtr: number;
        symbolName: string;

        constructor(params: IBasicSchedule) {
            this.date = params.date;
            this.employeeId = params.employeeId;
            this.workTimeCode = params.workTimeCode;
            this.workTypeCode = params.workTypeCode;
            this.confirmedAtr = params.confirmedAtr;
            this.isIntendedData = params.isIntendedData;
            this.scheduleCnt = params.scheduleCnt;
            this.scheduleStartClock = params.scheduleStartClock;
            this.scheduleEndClock = params.scheduleEndClock;
            this.bounceAtr = params.bounceAtr;
            this.symbolName = params.symbolName;
        }
    }

    class WorkScheduleState {
        date: Date;
        employeeId: string;
        scheduleItemId: number;
        scheduleEditState: number;

        constructor(date: Date, employeeId: string, scheduleItemId: number, scheduleEditState: number) {
            this.date = date;
            this.employeeId = employeeId;
            this.scheduleItemId = scheduleItemId;
            this.scheduleEditState = scheduleEditState;
        }
    }

    class TimeModel {
        dateTimePrev: string;
        dateTimeAfter: string;
        text: string;
        constructor(dateTimePrev: string, dateTimeAfter: string, text: string) {
            this.dateTimePrev = dateTimePrev;
            this.dateTimePrev = dateTimeAfter;
            this.text = dateTimePrev + dateTimeAfter;
        }
    }

    class ItemModel {
        code: string;
        name: string;
        description: string;
        constructor(code: string, name: string, description?: string) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
    }

    class TimeRange {
        columnKey: any;
        rowId: any;
        innerIdx: any;
        max: string;
        min: string;
        constructor(columnKey: any, rowId: any, max: string, min: string, innerIdx?: any) {
            this.columnKey = columnKey;
            this.rowId = rowId;
            this.innerIdx = innerIdx;
            this.max = max;
            this.min = min;
        }
    }

    class Time {
        year: string;
        month: string;
        day: string;
        weekDay: string;
        yearMonthDay: string;

        constructor(ymd: Date) {
            this.year = moment(ymd).format('YYYY');
            this.month = moment(ymd).format('M');
            this.day = moment(ymd).format('D');
            this.weekDay = moment(ymd).format('dd');
            this.yearMonthDay = this.year + moment(ymd).format('MM') + moment(ymd).format('DD');
        }
    }

    class ExItem {
        empId: string;
        empName: string;

        constructor(empId: string, dsOfSid: BasicSchedule[], listWorkType: ksu001.common.viewmodel.WorkType[], listWorkTime: ksu001.common.viewmodel.WorkTime[], manual: boolean, arrDay: Time[]) {
            this.empId = empId;
            this.empName = empId;
            // create detailHeader (ex: 4/1 | 4/2)
            if (manual) {
                for (let i = 0; i < arrDay.length; i++) {
                    if (+arrDay[i].day == 1) {
                        this['_' + arrDay[i].yearMonthDay] = arrDay[i].month + '/' + arrDay[i].day + "<br/>" + arrDay[i].weekDay;
                    } else {
                        this['_' + arrDay[i].yearMonthDay] = arrDay[i].day + "<br/>" + arrDay[i].weekDay;
                    }
                }
                return;
            }
            //create detailContent 
            for (let i = 0; i < arrDay.length; i++) {
                let obj: BasicSchedule = _.find(dsOfSid, (x) => {
                    return moment(new Date(x.date)).format('D') == arrDay[i].day;
                });
                if (obj) {
                    //get code and name of workType and workTime
                    let workTypeCode = null, workTypeName = null, workTimeCode = null, workTimeName = null;
                    let workType = _.find(listWorkType, ['workTypeCode', obj.workTypeCode]);
                    if (workType) {
                        workTypeCode = obj.workTypeCode;
                        workTypeName = workType.abbreviationName;
                    } else {
                        workTypeCode = null;
                        workTypeName = obj.workTypeCode != null ? getText('KSU001_103', [obj.workTypeCode]) : null;
                    }

                    let workTime = _.find(listWorkTime, ['workTimeCode', obj.workTimeCode]);
                    if (workTime) {
                        workTimeCode = obj.workTimeCode;
                        workTimeName = workTime.abName;
                    } else {
                        workTimeCode = null;
                        workTimeName = obj.workTimeCode != null ? getText('KSU001_103', [obj.workTimeCode]) : null;
                    }

                    this['_' + arrDay[i].yearMonthDay] = new ksu001.common.viewmodel.ExCell({
                        workTypeCode: workTypeCode,
                        workTypeName: workTypeName,
                        workTimeCode: workTimeCode,
                        workTimeName: workTimeName,
                        symbolName: obj.symbolName,
                        startTime: obj.scheduleStartClock ? formatById("Clock_Short_HM", obj.scheduleStartClock) : '',
                        endTime: obj.scheduleStartClock ? formatById("Clock_Short_HM", obj.scheduleEndClock) : ''
                    });
                } else {
                    this['_' + arrDay[i].yearMonthDay] = new ksu001.common.viewmodel.ExCell({
                        workTypeCode: null,
                        workTypeName: null,
                        workTimeCode: null,
                        workTimeName: null,
                        symbolName: '',
                        //startTime/endTime để là '' chứ không phải null
                        //do màn hình ở mode Time, click cell mà k thay đổi rồi click vào cell khác
                        //khi đó cell bị click trả ra giá trị là ''
                        //nếu để startTime/endTime null thì cell đó sẽ nhận thấy là đã thay đổi giá trị
                        //điều đó là không đúng
                        startTime: '',
                        endTime: ''
                    });
                }
            }
        }
    }
}