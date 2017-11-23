module ksu001.a.viewmodel {
    import alert = nts.uk.ui.dialog.alert;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;
    import blockUI = nts.uk.ui.block;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
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
            { headerText: nts.uk.resource.getText("KSU001_19"), key: 'code', width: 50 },
            { headerText: nts.uk.resource.getText("KSU001_20"), key: 'name', width: 150 },
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
            { code: '1', name: nts.uk.resource.getText("KSU001_89") },
            { code: '2', name: nts.uk.resource.getText("KSU001_90") }
        ]);
        selectedRuleCode: any = ko.observable(1);
        itemList1: KnockoutObservableArray<any> = ko.observableArray([
            new BoxModel(1, '画面サイズ'),
            new BoxModel(2, '高さを指定'),
        ]);
        selectedId: KnockoutObservable<number> = ko.observable(1);

        itemList2: KnockoutObservableArray<any> = ko.observableArray([
            new BoxModel(1, nts.uk.resource.getText("KSU001_339")),
            new BoxModel(2, nts.uk.resource.getText("KSU001_340")),
            new BoxModel(3, nts.uk.resource.getText("KSU001_341"))
        ]);
        selectedIds: KnockoutObservableArray<number> = ko.observableArray([1, 2]);
        popupVal: KnockoutObservable<string> = ko.observable('');
        selectedDate: KnockoutObservable<string> = ko.observable('');

        //Date time
        currentDate: Date = new Date();
        dtPrev: KnockoutObservable<Date> = ko.observable(new Date('2017/11/01'));
        dtAft: KnockoutObservable<Date> = ko.observable(new Date('2017/11/30'));
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
            { code: 2, name: '時刻' },
            { code: 3, name: '記号' }]);
        selectedModeDisplay: KnockoutObservable<number> = ko.observable(1);

        modeDisplayObject: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: '予定' },
            { code: 2, name: '実績' }]);
        selectedModeDisplayObject: KnockoutObservable<number> = ko.observable(1);

        arrDay: Time[] = [];
        listSid: KnockoutObservableArray<string> = ko.observableArray([]);
        lengthListSid: any;
        workPlaceNameDisplay: KnockoutObservable<string> = ko.observable('');
        dataWScheduleState: KnockoutObservableArray<WorkScheduleState> = ko.observableArray([]);
        listStateWorkTypeCode: KnockoutObservableArray<any> = ko.observableArray([]);
        dataWkpSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataComSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataPublicHoliday: KnockoutObservableArray<any> = ko.observableArray([]);
        isInsuranceStatus: boolean = false;

        constructor() {
            let self = this;
            //Tree grid
            self.itemsTree = ko.observableArray([]);
            self.selectedCodeTree = ko.observableArray([]);
            self.singleSelectedCodeTree = ko.observable(null);

            //Date time
            self.dateTimeAfter = ko.observable(moment(self.dtAft()).format('YYYY/MM/DD'));
            self.dateTimePrev = ko.observable(moment(self.dtPrev()).format('YYYY/MM/DD'));

            self.dtPrev.subscribe(() => {
                self.dateTimePrev(moment(self.dtPrev()).format('YYYY/MM/DD'));
            });
            self.dtAft.subscribe(() => {
                self.dateTimeAfter(moment(self.dtAft()).format('YYYY/MM/DD'));
            });

            //Pop-up
            for (let i = 1; i <= 12; i++) {
                self.items.push(new ItemModel('00' + i, '基本給' + i, '00' + i));
            }

            self.selectedModeDisplay.subscribe(function(newValue) {
                let currentScreen = __viewContext.viewModel.viewO.currentScreen;
                if (currentScreen) {
                    currentScreen.close();
                }

                if (newValue == 1) {
                    $('#contain-view').show();
                    $('#group-bt').show();
                    $('#oViewModel').show();
                    $('#qViewModel').hide();
                    $("#extable").exTable("updateMode", "none");
                    $("#extable").exTable("viewMode", "shortName");
                } else if (newValue == 2) {
                    $('#contain-view').hide();
                    $("#extable").exTable("updateMode", "edit");
                    $("#extable").exTable("viewMode", "time");
                } else {
                    $('#contain-view').show();
                    $('#oViewModel').hide();
                    $('#qViewModel').show();
                    $('#group-bt').show();
                    $("#extable").exTable("viewMode", "symbol");
                    $("#extable").exTable("updateMode", "none");
                }
                self.updateExTable();
            });

            self.selectedModeDisplayObject.subscribe((newValue) => {
                if (newValue == 2) {
                    // actual data display mode (in phase 2 not done, so the actual data is set to null)
                    // if actual data is null, display intended data
                    self.dataSource([]);
                    self.updateExTable();
                } else {
                    // intended data display mode 
                    // get data basicSchedule
                    self.getDataBasicSchedule().done(function() {
                        self.updateExTable();
                    });
                }
            });

            self.selectedBackgroundColor.subscribe((newValue) => {
                self.updateExTable();
            });

            //display for A3_2
            self.lengthListSid = ko.pureComputed(() => {
                return nts.uk.resource.getText('KSU001_54', [self.listSid().length.toString()]);
            });

            //start
            self.initCCG001();
            self.initExTable();
            self.initShiftCondition();
        }

        /**
         *shift condition  A2_4
         */
        initShiftCondition(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            service.getShiftCondition().done(function(listShiftCondition) {
                service.getShiftConditionCategory().done(function(listShiftCategory) {
                    _.forEach(listShiftCategory, function(shiftCate) {
                        let level1 = new Node(shiftCate.categoryNo, shiftCate.categoryName, []);
                        _.forEach(listShiftCondition, function(shiftCon) {
                            if (shiftCate.categoryNo == shiftCon.categoryNo) {
                                let level2 = new Node(shiftCon.conditionNo, shiftCon.conditionName, []);
                                level1.childs.push(level2);
                            }
                        });
                        self.itemsTree.push(level1);
                    });
                });
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Get data of Basic Schedule
         */
        getDataBasicSchedule(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    employeeId: self.listSid(),
                    startDate: self.dtPrev(),
                    endDate: self.dtAft()
                };

            service.getDataBasicSchedule(obj).done(function(data: BasicSchedule[]) {
                self.dataSource([]);
                _.each(data, (x: BasicSchedule) => {
                    self.dataSource.push(new BasicSchedule({
                        date: x.date,
                        employeeId: x.employeeId,
                        workTimeCode: x.workTimeCode,
                        workTypeCode: x.workTypeCode,
                        confirmedAtr: x.confirmedAtr,
                        workDayAtr: x.workDayAtr,
                        isIntendedData: true
                    }));
                });
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data of Schedule Display Control
         */
        checkIsInsuranceStatus(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            service.getDataScheduleDisplayControl().done((data) => {
                if (!!data && data.personInforAtr == 7) {
                    self.isInsuranceStatus = true;
                }
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Check State of list WorkTypeCode
         */
        checkStateWorkTypeCode(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                lstWorkTypeCode = [],
                lstIntendedData = _.filter(self.dataSource(), { 'isIntendedData': true });
            if (lstIntendedData.length > 0) {
                _.map(lstIntendedData, (x) => {
                    if (!_.includes(lstWorkTypeCode, x.workTypeCode)) {
                        lstWorkTypeCode.push(x.workTypeCode);
                    }
                });
            }
            service.checkStateWorkTypeCode(lstWorkTypeCode).done((data) => {
                self.listStateWorkTypeCode(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data WorkScheduleState
         */
        getDataWorkScheduleState(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    sId: self.listSid(),
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
         * Get data WkpSpecificDate
         */
        getDataWkpSpecificDate(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    workplaceId: self.empItems()[0].workplaceId,
                    startDate: +moment(self.dtPrev()).format('YYYYMMDD'),
                    endDate: +moment(self.dtAft()).format('YYYYMMDD'),
                };
            service.getDataWkpSpecificDate(obj).done(function(data) {
                self.dataWkpSpecificDate(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data WkpSpecificDate
         */
        getDataComSpecificDate(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    startDate: +moment(self.dtPrev()).format('YYYYMMDD'),
                    endDate: +moment(self.dtAft()).format('YYYYMMDD'),
                };
            service.getDataComSpecificDate(obj).done(function(data) {
                self.dataComSpecificDate(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Get data Public Holiday
         */
        getDataPublicHoliday(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    startDate: +moment(self.dtPrev()).format('YYYYMMDD'),
                    endDate: +moment(self.dtAft()).format('YYYYMMDD'),
                };
            service.getDataPublicHoliday(obj).done(function(data) {
                self.dataPublicHoliday(data);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }


        /**
         * CCG001 return listEmployee
         * When listEmployee changed, call function updateExtable() to refresh data of exTable
         */
        searchEmployee(dataEmployee: EmployeeSearchDto[]) {
            let self = this;
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
            //get workPlaceName to display A3-1
            self.workPlaceNameDisplay(self.empItems()[0].workplaceName);

            self.listSid([]);
            let arrSid: string[] = [];
            _.each(self.empItems(), (x) => {
                arrSid.push(x.empId);
            });
            self.listSid(arrSid);
            if (self.selectedModeDisplayObject() == 1) {
                //intended data display mode 
                //get data basicSchedule
                self.getDataBasicSchedule().done(function() {
                    self.updateExTable();
                });
            } else {
                //actual data display mode 
                self.updateExTable();
            }
        }

        /**
         * init CCG001
         */
        initCCG001() {
            let self = this;
            self.ccgcomponent = {
                baseDate: ko.observable(new Date()),
                // Show/hide options 
                isQuickSearchTab: true,
                isAdvancedSearchTab: true,
                isAllReferableEmployee: true,
                isOnlyMe: true,
                isEmployeeOfWorkplace: true,
                isEmployeeWorkplaceFollow: true,
                isMutipleCheck: true,
                isSelectAllEmployee: true,

                /**
                * @param dataList: list employee returned from component.
                * Define how to use this list employee by yourself in the function's body.
                */
                onSearchAllClicked: function(dataList: EmployeeSearchDto[]) {
                    self.searchEmployee(dataList);

                },
                onSearchOnlyClicked: function(data: EmployeeSearchDto) {
                    self.showinfoSelectedEmployee(true);
                    var dataEmployee: EmployeeSearchDto[] = [];
                    dataEmployee.push(data);
                    self.searchEmployee(dataEmployee);
                },
                onSearchOfWorkplaceClicked: function(dataList: EmployeeSearchDto[]) {
                    self.searchEmployee(dataList);
                },
                onSearchWorkplaceChildClicked: function(dataList: EmployeeSearchDto[]) {
                    self.searchEmployee(dataList);
                },
                onApplyEmployee: function(dataEmployee: EmployeeSearchDto[]) {
                    self.searchEmployee(dataEmployee);
                }
            }
            $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent);
        }

        /**
         * Create exTable
         */
        initExTable(): void {
            let self = this,
                timeRanges = [],
                //Get dates in time period
                currentDay = new Date(self.dtPrev().toString());

            while (currentDay <= self.dtAft()) {
                self.arrDay.push(new Time(currentDay));
                currentDay.setDate(currentDay.getDate() + 1);
            }

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

            //            self.setColor(detailHeaderDeco, detailContentDeco);

            //create dataSource for detailHeader
            detailHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));
            for (let i = 0; i < self.arrDay.length; i++) {
                objDetailHeaderDs['_' + self.arrDay[i].yearMonthDay] = '';
            }
            detailHeaderDs.push(objDetailHeaderDs);

            //define the detailColumns
            _.each(self.arrDay, (x: Time) => {
                detailColumns.push({
                    key: "_" + x.yearMonthDay, width: "50px", headerText: "", handlerType: "input", dataType: "time/time", visible: true
                });
            });

            horzSumHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));

            //create leftMost Header and Content
            let leftmostColumns = [{
                headerText: nts.uk.resource.getText("KSU001_56"), key: "empName", width: "160px", icon: "ui-icon ui-icon-contact",
                iconWidth: "35px", control: "link", handler: function(rData, rowIdx, key) { }
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
                { headerText: nts.uk.resource.getText("KSU001_57"), key: "team", width: "50px" },
                { headerText: nts.uk.resource.getText("KSU001_58"), key: "rank", width: "50px" },
                { headerText: nts.uk.resource.getText("KSU001_59"), key: "qualification", width: "50px" },
                { headerText: nts.uk.resource.getText("KSU001_60"), key: "employmentName", width: "100px" },
                { headerText: nts.uk.resource.getText("KSU001_61"), key: "workplaceName", width: "150px" },
                { headerText: nts.uk.resource.getText("KSU001_62"), key: "classificationName", width: "100px" },
                { headerText: nts.uk.resource.getText("KSU001_63"), key: "positionName", width: "100px" },
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

            //create Detail Header and Content
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
                                { id: "日付別", text: nts.uk.resource.getText("KSU001_325"), selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-calendar" },
                                { id: "シフト別", text: nts.uk.resource.getText("KSU001_326"), selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-star" }
                            ]
                        },
                        popup: {
                            rows: [1],
                            provider: function() { return $("#popup-area8"); }
                        }
                    }]
            };

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
                            return [obj.workTypeName, obj.workTimeName];
                        case "symbol":
                            return obj.symbol;
                        case "time":
                            return [obj.startTime, obj.endTime];
                    }
                },
                upperInput: "startTime",
                lowerInput: "endTime"
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

            //create HorizontalSum Header and Content
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

            let horizontalSumContent = {
                columns: detailColumns,
                dataSource: horzSumContentDs,
                primaryKey: "itemId"
            };

            new nts.uk.ui.exTable.ExTable($("#extable"), {
                headerHeight: "60px", bodyRowHeight: "30px", bodyHeight: "0px",
                horizontalSumHeaderHeight: "40px", horizontalSumBodyHeight: "75px",
                horizontalSumBodyRowHeight: "20px",
                areaResize: true,
                bodyHeightMode: "dynamic",
                windowXOccupation: 40,
                windowYOccupation: 215,
                updateMode: "none",
                pasteOverWrite: true,
                stickOverWrite: true,
                viewMode: "shortName",
                determination: {
                    rows: [0, 1],
                    columns: ["empName"]
                },
            })
                .LeftmostHeader(leftmostHeader).LeftmostContent(leftmostContent)
                .MiddleHeader(middleHeader).MiddleContent(middleContent)
                .DetailHeader(detailHeader).DetailContent(detailContent)
                .VerticalSumHeader(vertSumHeader).VerticalSumContent(vertSumContent)
                .LeftHorzSumHeader(leftHorzSumHeader).LeftHorzSumContent(leftHorzSumContent)
                .HorizontalSumHeader(horizontalSumHeader).HorizontalSumContent(horizontalSumContent).create();

            /**
             * update text for row 2 of detailHeader
             */
            $("#popup-set").click(function() {
                $("#extable").exTable("popupValue", self.popupVal());
            });

            /**
             * close popup
             */
            $(".close-popup").click(function() {
                $('#popup-area8').css('display', 'none');
            });

        }

        /**
         *  update extable 
         */
        updateExTable(): void {
            let self = this;

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
                    key: "_" + x.yearMonthDay, width: "50px", headerText: "", handlerType: "input", dataType: "time/time", visible: true
                });
            });

            let updateMiddleContent = {
                dataSource: newMiddleDs,
            };

            let leftmostContentDeco = [], detailHeaderDeco = [], detailContentDeco = [];

            self.setColor(detailHeaderDeco, detailContentDeco).done(() => {

                let updateLeftmostContent = {
                    dataSource: newLeftMostDs,
                    features: [{
                        name: "BodyCellStyle",
                        decorator: leftmostContentDeco
                    }]
                }
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
                                    { id: "日付別", text: "日付別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-calendar" },
                                    { id: "シフト別", text: "シフト別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-star" }
                                ]
                            },
                            popup: {
                                rows: [1],
                                provider: function() { return $("#popup-area4"); }
                            }
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
                $("#extable").exTable("updateTable", "middle", {}, updateMiddleContent);
                $("#extable").exTable("updateTable", "verticalSummaries", {}, updateVertSumContent);
                $("#extable").exTable("updateTable", "leftHorizontalSummaries", {}, updateLeftHorzSumContent);
                $("#extable").exTable("updateTable", "detail", updateDetailHeader, updateDetailContent);
                $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, updateHorzSumContent);

                $("#extable").on("extablecellupdated", function() { });
                $("#extable").on("extablerowupdated", function() { });
            });
        }

        /**
         * update new data of header and content of detail and horizSum
         */
        updateDetailAndHorzSum(): void {
            let self = this;
            //Get dates in time period
            let currentDay = new Date(self.dtPrev().toString());
            self.arrDay = [];
            let newDetailColumns = [], newObjDetailHeaderDs = [], newDetailHeaderDs = [], newDetailContentDs = [];
            while (currentDay <= self.dtAft()) {
                self.arrDay.push(new Time(currentDay));
                currentDay.setDate(currentDay.getDate() + 1);
            }

            //define the new detailColumns
            _.each(self.arrDay, (x: Time) => {
                newDetailColumns.push({
                    key: "_" + x.yearMonthDay, width: "50px", headerText: "", visible: true
                });
            });

            //create new detailHeaderDs
            newDetailHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));
            for (let i = 0; i < self.arrDay.length; i++) {
                newObjDetailHeaderDs['_' + self.arrDay[i].yearMonthDay] = '';
            }
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
                self.setColorForCellHeaderDetailAndHoz(detailHeaderDeco);

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
                                    { id: "日付別", text: "日付別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-calendar" },
                                    { id: "シフト別", text: "シフト別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-star" }
                                ]
                            },
                            popup: {
                                rows: [1],
                                provider: function() { return $("#popup-area8"); }
                            }
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
                $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, {});
            } else if (self.selectedModeDisplayObject() == 1) {
                self.getDataBasicSchedule().done(() => {
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
                                            { id: "日付別", text: "日付別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-calendar" },
                                            { id: "シフト別", text: "シフト別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-star" }
                                        ]
                                    },
                                    popup: {
                                        rows: [1],
                                        provider: function() { return $("#popup-area8"); }
                                    }
                                }]
                        };

                        //intended data display mode 
                        //dataSour of detail
                        _.each(self.listSid(), (x) => {
                            let dsOfSid: any = _.filter(self.dataSource(), ['employeeId', x]);
                            newDetailContentDs.push(new ExItem(x, dsOfSid, __viewContext.viewModel.viewO.listWorkType(), __viewContext.viewModel.viewO.listWorkTime(), false, self.arrDay));
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
                        $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, updateHorzSumContent);
                    });
                });
            } else if (self.selectedModeDisplayObject() == 2) {
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
                                        { id: "日付別", text: "日付別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-calendar" },
                                        { id: "シフト別", text: "シフト別", selectHandler: function(id) { alert(id); }, icon: "ui-icon ui-icon-star" }
                                    ]
                                },
                                popup: {
                                    rows: [1],
                                    provider: function() { return $("#popup-area8"); }
                                }
                            }]
                    };

                    //actual data display mode , if hasn't actual data, display intended data
                    newDetailContentDs.push(new ExItem(null, [], __viewContext.viewModel.viewO.listWorkType(), __viewContext.viewModel.viewO.listWorkTime(), false, self.arrDay));
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
                    $("#extable").exTable("updateTable", "horizontalSummaries", updateHorzSumHeader, updateHorzSumContent);
                });
            }
        }

        /**
        * next a month
        */
        nextMonth(): void {
            let self = this;
            if (self.selectedTimePeriod() == 1) {
                //Recalculate the time period
                let dtMoment = moment(self.dtAft());
                dtMoment.add(1, 'days');
                self.dtPrev(dtMoment.toDate());
                dtMoment = dtMoment.add(1, 'months');
                dtMoment.subtract(1, 'days');
                self.dtAft(dtMoment.toDate());
                self.updateDetailAndHorzSum();
            }
        }

        /**
        * come back a month
        */
        backMonth(): void {
            let self = this;
            if (self.selectedTimePeriod() == 1) {
                //Recalculate the time period
                let dtMoment = moment(self.dtPrev());
                dtMoment.subtract(1, 'days');
                self.dtAft(dtMoment.toDate());
                dtMoment = dtMoment.subtract(1, 'months');
                dtMoment.add(1, 'days');
                self.dtPrev(dtMoment.toDate());
                self.updateDetailAndHorzSum();
            }
        }

        /**
        * Save data
        */
        saveData(): void {
            let self = this;
            let arrObj: BasicSchedule[] = [],
                arrCell: Cell[] = $("#extable").exTable("updatedCells"),
                lengthArrCell = arrCell.length;
            if (lengthArrCell == 0) {
                return;
            }
            for (let i = 0; i < lengthArrCell; i += 1) {
                arrObj.push(new BasicSchedule({
                    // slice string '_YYYYMMDD' to 'YYYYMMDD'
                    date: moment.utc(arrCell[i].columnKey.slice(1, arrCell[i].columnKey.length), 'YYYYMMDD').toISOString(),
                    employeeId: self.listSid()[Number(arrCell[i].rowIndex)],
                    workTimeCode: arrCell[i].value.workTimeCode,
                    workTypeCode: arrCell[i].value.workTypeCode,
                    //set static
                    confirmedAtr: 0,
                    workDayAtr: 0
                }));
            }

            service.registerData(arrObj).done(function(error: any) {
                if (error.length != 0) {
                    self.addListError(error);
                } else {
                    nts.uk.ui.dialog.alert(nts.uk.resource.getMessage("Msg_15"));
                }
                //get data and update extable
                self.getDataBasicSchedule().done(function() {
                    self.updateExTable();
                });

            }).fail(function(error: any) {
                nts.uk.ui.dialog.alertError(error.message);
            });
        }

        /**
         * Set color for text in cell : 明細セル文字色の判断処理
         */
        setColorForText(detailHeaderDeco: any, detailContentDeco: any): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            //Set color for text in cell 
            if (self.selectedModeDisplayObject() == 2) {
                let arrActualData: [] = _.filter(self.dataSource(), { 'isIntendedData': false });
                if (arrActualData.length > 0) {
                    _.each(arrActualData, (item: BasicSchedule) => {
                        detailContentDeco.push(new CellColor("_" + moment(new Date(item.date)).format('YYYYMMDD'), item.employeeId, "color-schedule-performance"));
                    });
                }
            }

            if (self.selectedModeDisplay() == 1 || self.selectedModeDisplay() == 3) {
                self.checkStateWorkTypeCode().done(function() {
                    //lstData: list object in dataSource. It has workTypeCode, which exist in master data WORKTYPE
                    let lstData: BasicSchedule[] = [];
                    _.each(__viewContext.viewModel.viewO.listWorkType(), (item) => {
                        let obj = _.filter(self.dataSource(), { 'workTypeCode': item.workTypeCode });
                        if (obj) {
                            lstData.push.apply(lstData, obj);
                        }
                    });

                    _.each(lstData, (item) => {
                        let stateWorkTypeCode = _.find(self.listStateWorkTypeCode(), { 'workTypeCode': item.workTypeCode });
                        if (stateWorkTypeCode) {
                            let state = stateWorkTypeCode.state;
                            if (state == 3) {
                                //state == 3 is work-day
                                detailContentDeco.push(new CellColor("_" + moment(item.date).format('YYYYMMDD'), item.employeeId, "color-attendance"));
                            } else if (state == 0) {
                                //state == 0 is holiday-day
                                detailContentDeco.push(new CellColor("_" + moment(item.date).format('YYYYMMDD'), item.employeeId, "color-holiday"));
                            } else {
                                //state == 1 || 2 is work-half-day
                                detailContentDeco.push(new CellColor("_" + moment(item.date).format('YYYYMMDD'), item.employeeId, "color-half-day-work"));
                            }
                        }
                    });
                    dfd.resolve();
                });
            } else {
                dfd.resolve();
            }

            return dfd.promise();
        }

        /**
         * Set color for cell
         */
        setColorForCell(detailHeaderDeco: any, detailContentDeco: any): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            if (self.selectedBackgroundColor() === '001') {
                // Return value：就業時間帯 -> query table WorkTime to get color cod
                //TO-DO
                dfd.resolve();
            } else {
                //get data from WorkScheduleState
                self.getDataWorkScheduleState().done(() => {
                    if (self.selectedModeDisplay() === 3) {
                        _.each(self.listSid(), (sId) => {
                            let endDate = new Date(self.dtAft()), startDate = new Date(self.dtPrev());
                            for (let currDate = startDate; startDate <= endDate; startDate.setDate(startDate.getDate() + 1)) {
                                let cDate1 = moment(currDate).format('YYYY/MM/DD'), cDate2 = moment(currDate).format('YYYYMMDD');
                                let arr: any[] = _.filter(self.dataWScheduleState(), { 'employeeId': sId, 'date': cDate1, 'scheduleEditState': 0 });
                                if (arr.length == 3) {
                                    let scheduleEditStateItem: number = _.find(self.dataWScheduleState(), (item) => {
                                        return item.employeeId == sId && item.date == cDate1 && item.scheduleEditState != 0;
                                    }).scheduleEditState;

                                    if (scheduleEditStateItem == 2) {
                                        detailContentDeco.push(new CellColor("_" + cDate2, sId, "bg-daily-alter-other"));
                                    } else if (scheduleEditStateItem == 1) {
                                        detailContentDeco.push(new CellColor("_" + cDate2, sId, "bg-daily-alter-self"));
                                    } else if (scheduleEditStateItem == 3) {
                                        detailContentDeco.push(new CellColor("_" + cDate2, sId, "bg-daily-reflect-application"));
                                    }
                                }
                                if (arr.length <= 2) {
                                    if (_.find(self.dataWScheduleState(), { 'employeeId': sId, 'date': cDate1, 'scheduleEditState': 2 })) {
                                        detailContentDeco.push(new CellColor("_" + cDate2, sId, "bg-daily-alter-other"));
                                    } else if (_.find(self.dataWScheduleState(), { 'employeeId': sId, 'date': cDate1, 'scheduleEditState': 1 })) {
                                        detailContentDeco.push(new CellColor("_" + cDate2, sId, "bg-daily-alter-self"));
                                    } else if (_.find(self.dataWScheduleState(), { 'employeeId': sId, 'date': cDate1, 'scheduleEditState': 3 })) {
                                        detailContentDeco.push(new CellColor("_" + cDate2, sId, "bg-daily-reflect-application"));
                                    }
                                }
                            }
                        });
                    } else {
                        //Return value of ScheduleEditState of WorkScheduleState
                        let data = [];
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

                        _.each(data, (item) => {
                            if (item.scheduleEditState == 1) {
                                //手修正(本人) = bg-daily-alter-self
                                detailContentDeco.push(new CellColor("_" + moment(new Date(item.date)).format('YYYYMMDD'), item.employeeId, "bg-daily-alter-self"));
                            } else if (item.scheduleEditState == 2) {
                                //手修正(他人) = bg-daily-alter-other
                                detailContentDeco.push(new CellColor("_" + moment(new Date(item.date)).format('YYYYMMDD'), item.employeeId, "bg-daily-alter-other"));
                            } else if (item.scheduleEditState == 3) {
                                //申請反映 = bg-daily-reflect-application
                                detailContentDeco.push(new CellColor("_" + moment(new Date(item.date)).format('YYYYMMDD'), item.employeeId, "bg-daily-reflect-application"));
                            }
                        });
                    }
                    dfd.resolve();
                });
            }
            return dfd.promise();
        }

        /**
         * Set color for cell header: 日付セル背景色文字色制御
         * 
         */
        setColorForCellHeaderDetailAndHoz(detailHeaderDeco: any): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            if (self.empItems().length != 0) {
                if (moment().isBetween(self.dtPrev(), self.dtAft())) {
                    detailHeaderDeco.push(new CellColor("_" + moment().format('YYYYMMDD'), 0, "bg-schedule-that-day "));
                    detailHeaderDeco.push(new CellColor("_" + moment().format('YYYYMMDD'), 1, "bg-schedule-that-day"));
                }
                $.when(self.getDataWkpSpecificDate(), self.getDataComSpecificDate(), self.getDataPublicHoliday()).done(() => {
                    _.each(self.arrDay, (date) => {
                        let dateFormat = +date.yearMonthDay;
                        if (self.dataWkpSpecificDate().includes(dateFormat) || self.dataComSpecificDate().includes(dateFormat)) {
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 0, "bg-schedule-specific-date "));
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 1, "bg-schedule-specific-date"));
                        } else if (self.dataPublicHoliday().includes(dateFormat)) {
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 0, "bg-schedule-sunday color-schedule-sunday"));
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 1, "bg-schedule-sunday color-schedule-sunday"));
                        } else if (date.weekDay === '土') {
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 0, "bg-schedule-saturday color-schedule-saturday"));
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 1, "bg-schedule-saturday color-schedule-saturday"));
                        } else if (date.weekDay === '日') {
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 0, "bg-schedule-sunday color-schedule-sunday"));
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 1, "bg-schedule-sunday color-schedule-sunday"));
                        } else {
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 0, "bg-weekdays color-weekdays"));
                            detailHeaderDeco.push(new CellColor("_" + dateFormat, 1, "bg-weekdays color-weekdays"));
                        }
                    });
                    dfd.resolve();
                });
            } else {
                dfd.resolve();
            }
            return dfd.promise();
        }

        /**
         * Set color for cell of leftmost : 個人名セルの背景色の判断処理
         */
        setColorForLeftmostContent(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            $.when(self.checkIsInsuranceStatus()).done(() => {
                if (self.isInsuranceStatus) {
                    //TO-DO    
                }
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Set color for cell : 明細セル背景色の判断処理
         */
        setColor(detailHeaderDeco: any, detailContentDeco: any): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            $.when(self.setColorForCellHeaderDetailAndHoz(detailHeaderDeco), self.setColorForText(detailHeaderDeco, detailContentDeco), self.setColorForCell(detailHeaderDeco, detailContentDeco), self.setColorForLeftmostContent()).done(() => {
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Set error
         */
        addListError(errorsRequest: Array<string>) {
            var messages = {};
            _.forEach(errorsRequest, function(err) {
                messages[err] = nts.uk.resource.getMessage(err);
            });

            var errorVm = {
                messageId: errorsRequest,
                messages: messages
            };

            nts.uk.ui.dialog.bundledErrors(errorVm);
        }

        /**
         * paste data on cell
         */
        pasteData(): void {
            let self = this;
            if (self.selectedModeDisplay() == 1) {
                $("#extable").exTable("updateMode", "stick");
                $("#extable").exTable("stickMode", "single");
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
                startDate: self.dtPrev(),
                endDate: self.dtAft()
            });
            nts.uk.ui.windows.sub.modal("/view/ksu/001/d/index.xhtml");
        }

        /**
         * open dialog L
         */
        openDialogL(): void {
            let self = this;
            $('#popup-area5').ntsPopup('hide');
            //hiện giờ fix cứng workplaceId và truyền sang tất cả emmployee . Sau này sửa truyền list employee theo workplace id
            nts.uk.ui.windows.setShared("workPlaceId", "000000000000000000000000000000000002");
            nts.uk.ui.windows.setShared("listEmployee", self.empItems());
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
            nts.uk.request.jump("/view/kml/002/a/index.xhtml");
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
        value: ExCell,
        innerIdx: number
    }

    class Cell {
        rowIndex: string;
        columnKey: string;
        value: ExCell;
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
        workTimeCode: string,
        workTypeCode: string,
        confirmedAtr: number,
        workDayAtr: number,
        isIntendedData?: boolean
    }

    class BasicSchedule {
        date: string;
        employeeId: string;
        workTimeCode: string;
        workTypeCode: string;
        confirmedAtr: number;
        workDayAtr: number;
        isIntendedData: boolean;

        constructor(params: IBasicSchedule) {
            this.date = params.date;
            this.employeeId = params.employeeId;
            this.workTimeCode = params.workTimeCode;
            this.workTypeCode = params.workTypeCode;
            this.confirmedAtr = params.confirmedAtr;
            this.workDayAtr = params.workDayAtr;
            this.isIntendedData = params.isIntendedData;
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

    class CellColor {
        columnKey: any;
        rowId: any;
        innerIdx: any;
        clazz: any;
        constructor(columnKey: any, rowId: any, clazz: any, innerIdx?: any) {
            this.columnKey = columnKey;
            this.rowId = rowId;
            this.innerIdx = innerIdx;
            this.clazz = clazz;
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

    interface IWorkType {
        workTypeCode: string,
        sortOrder: number,
        symbolicName: string,
        name: string,
        abbreviationName: string,
        memo: string,
        displayAtr: number
    }

    class WorkType {
        workTypeCode: string;
        sortOrder: number;
        symbolicName: string;
        name: string;
        abbreviationName: string;
        memo: string;
        displayAtr: number;

        constructor(params: IWorkType) {
            this.workTypeCode = params.workTypeCode;
            this.sortOrder = params.sortOrder;
            this.symbolicName = params.symbolicName;
            this.name = params.name;
            this.abbreviationName = params.abbreviationName;
            this.memo = params.memo;
            this.displayAtr = params.displayAtr;
        }
    }

    interface IWorkTime {
        siftCd: string,
        name: string,
        abName: string,
        dailyWorkAtr: number,
        methodAtr: number,
        displayAtr: number,
        note: string
    }

    class WorkTime {
        siftCd: string;
        name: string;
        abName: string;
        dailyWorkAtr: number;
        methodAtr: number;
        displayAtr: number;
        note: string;

        constructor(params: IWorkTime) {
            this.siftCd = params.siftCd;
            this.name = params.name;
            this.abName = params.abName;
            this.dailyWorkAtr = params.dailyWorkAtr;
            this.methodAtr = params.methodAtr;
            this.displayAtr = params.displayAtr;
            this.note = params.note;
        }
    }

    interface IExCell {
        workTypeCode: string,
        workTypeName: string,
        workTimeCode: string,
        workTimeName: string,
        symbol: string,
        startTime: any,
        endTime: any
    }

    class ExCell {
        workTypeCode: string;
        workTypeName: string;
        workTimeCode: string;
        workTimeName: string;
        symbol: string;
        startTime: any;
        endTime: any;
        constructor(params: IExCell) {
            this.workTypeCode = params.workTypeCode;
            this.workTypeName = params.workTypeName;
            this.workTimeCode = params.workTimeCode;
            this.workTimeName = params.workTimeName;
            let symbolArr: string[] = ["通", "◯", "✕"];
            this.symbol = params.symbol || symbolArr[Math.floor((Math.random() * 3))];
            this.startTime = params.startTime || "8:30";
            this.endTime = params.endTime || "17:30";
        }
    }

    class ExItem {
        empId: string;
        empName: string;

        constructor(empId: string, dsOfSid: BasicSchedule[], listWorkType: WorkType[], listWorkTime: WorkTime[], manual: boolean, arrDay: Time[]) {
            this.empId = empId;
            this.empName = empId;
            // create detailHeader (ex: 4/1 | 4/2)
            if (manual) {
                for (let i = 0; i < arrDay.length; i++) {
                    this['_' + arrDay[i].yearMonthDay] = arrDay[i].month + '/' + arrDay[i].day + "<br/>" + arrDay[i].weekDay;
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
                        workTypeCode = '';
                        workTypeName = '';
                    }

                    let workTime = _.find(listWorkTime, ['siftCd', obj.workTimeCode]);
                    if (workTime) {
                        workTimeCode = obj.workTimeCode;
                        workTimeName = workTime.abName;
                    } else {
                        workTimeCode = '';
                        workTimeName = '';
                    }

                    this['_' + arrDay[i].yearMonthDay] = new ExCell({
                        workTypeCode: workTypeCode,
                        workTypeName: workTypeName,
                        workTimeCode: workTimeCode,
                        workTimeName: workTimeName,
                        symbol: null,
                        startTime: null,
                        endTime: null
                    });
                } else {
                    this['_' + arrDay[i].yearMonthDay] = new ExCell({
                        workTypeCode: '',
                        workTypeName: '',
                        workTimeCode: '',
                        workTimeName: '',
                        symbol: null,
                        startTime: null,
                        endTime: null
                    });
                }
            }
        }
    }
}