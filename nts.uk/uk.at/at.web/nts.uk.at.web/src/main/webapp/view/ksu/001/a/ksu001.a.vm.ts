module ksu001.a.viewmodel {
    import alert = nts.uk.ui.dialog.alert;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;
    import blockUI = nts.uk.ui.block;

    export class ScreenModel {

        empItems: KnockoutObservableArray<PersonModel> = ko.observableArray([]);
        dataSource: KnockoutObservableArray<BasicSchedule> = ko.observableArray([]);
        ccgcomponent: GroupOption = ko.observable();
        selectedCode: KnockoutObservableArray<any> = ko.observableArray([]);
        showinfoSelectedEmployee: KnockoutObservable<boolean> = ko.observable(true);

        //Grid list A2_4 (pop-up)
        items: KnockoutObservableArray<ItemModel> = ko.observableArray([]);
        columns: KnockoutObservableArray<NtsGridListColumn> = ko.observableArray([
            { headerText: nts.uk.resource.getText("KSU001_19"), key: 'code', width: 50 },
            { headerText: nts.uk.resource.getText("KSU001_20"), key: 'name', width: 150 },
            { headerText: 'コード', key: 'id', width: 50, hidden: true },
        ]);
        currentCodeList: KnockoutObservableArray<any> = ko.observableArray([]);

        //Date time
        dtPrev: KnockoutObservable<Date> = ko.observable(new Date('2017/01/01'));
        dtAft: KnockoutObservable<Date> = ko.observable(new Date('2017/01/31'));
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
        selectedModeDisplay: KnockoutObservable<number> = ko.observable(undefined);

        modeDisplayObject: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: '予定' },
            { code: 2, name: '実績' }]);
        selectedModeDisplayObject: KnockoutObservable<number> = ko.observable(1);

        arrDay: Time[] = [];
        listSid: KnockoutObservableArray<string> = ko.observableArray([]);
        lengthListSid: any;
        workPlaceNameDisplay: KnockoutObservable<string> = ko.observable('');

        constructor() {
            let self = this;

            //Date time
            self.dateTimeAfter = ko.observable(moment(self.dtAft()).format('YYYY/MM/DD'));
            self.dateTimePrev = ko.observable(moment(self.dtPrev()).format('YYYY/MM/DD'));

            self.dtPrev.subscribe(() => {
                self.dateTimePrev(moment(self.dtPrev()).format('YYYY/MM/DD'));
            });
            self.dtAft.subscribe(() => {
                self.dateTimeAfter(moment(self.dtAft()).format('YYYY/MM/DD'));
            });

            //Grid list for pop-up
            for (let i = 1; i <= 12; i++) {
                self.items.push(new ItemModel('00' + i, '基本給' + i, '00' + i));
            }

            self.selectedModeDisplay.subscribe(function(newValue) {
                if (newValue == 1) {
                    $('#oViewModel').addClass('oViewModelDisplay');
                } else {
                    $('#oViewModel').removeClass('oViewModelDisplay');
                }
            });

            //display for A3_2
            self.lengthListSid = ko.pureComputed(() => {
                return nts.uk.resource.getText('KSU001_54', [self.listSid().length.toString()]);
            });

            //start
            self.selectedModeDisplay(1);
            self.initCCG001();
            self.initExTable();
        }

        /**
         * Get data Basic_Schedule
         */
        getDataBasicSchedule(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    sId: self.listSid(),
                    startDate: self.dtPrev(),
                    endDate: self.dtAft()
                };
            service.getDataBasicSchedule(obj).done(function(data: BasicSchedule[]) {
                if (data) {
                    self.dataSource(data);
                }
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
            //get data basicSchedule
            self.getDataBasicSchedule().done(function() {
                self.updateExTable();
            });
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

            self.setColorForDetail(detailHeaderDeco, detailContentDeco);

            //create dataSource for detailHeader
            detailHeaderDs.push(new ExItem(undefined, null, null, null, true, self.arrDay));
            for (let i = 0; i < self.arrDay.length; i++) {
                objDetailHeaderDs['_' + self.arrDay[i].yearMonthDay] = '';
            }
            detailHeaderDs.push(objDetailHeaderDs);

            //define the detailColumns
            _.each(self.arrDay, (x: Time) => {
                detailColumns.push({
                    key: "_" + x.yearMonthDay, width: "100px", headerText: "", visible: true
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
                rowHeight: "75px",
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
                    rows: { 0: "75px" }
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
                rowHeight: "30px",
                width: "700px",
                features: [{
                    name: "HeaderRowHeight",
                    rows: { 0: "50px", 1: "25px" }
                }, {
                        name: "HeaderCellStyle",
                        decorator: detailHeaderDeco
                    }, {
                        name: "ColumnResizes"
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
                    rows: { 0: "30px", 1: "45px" }
                }]
            };

            let vertSumContent = {
                columns: vertSumColumns,
                dataSource: vertSumContentDs,
                primaryKey: "empId"
            };

            //create LeftHorzSum Header and Content
            let leftHorzColumns = [
                { headerText: "項目名", key: "itemName", width: "200px" },
                { headerText: "合計", key: "sum", width: "100px" }
            ];

            let leftHorzSumHeader = {
                columns: leftHorzColumns,
                rowHeight: "75px"
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
                rowHeight: "75px",
                features: [{
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
                headerHeight: "75px", bodyRowHeight: "50px", bodyHeight: "200px",
                horizontalSumHeaderHeight: "75px", horizontalSumBodyHeight: "100px",
                horizontalSumBodyRowHeight: "20px",
                areaResize: true,
                bodyHeightMode: "dynamic",
                windowXOccupation: 80,
                windowYOccupation: 150,
                updateMode: "stick",
                pasteOverWrite: true,
                stickOverWrite: true,
                viewMode: "shortName",
            })
                .LeftmostHeader(leftmostHeader).LeftmostContent(leftmostContent)
                .MiddleHeader(middleHeader).MiddleContent(middleContent)
                .DetailHeader(detailHeader).DetailContent(detailContent)
                .VerticalSumHeader(vertSumHeader).VerticalSumContent(vertSumContent)
                .LeftHorzSumHeader(leftHorzSumHeader).LeftHorzSumContent(leftHorzSumContent)
                .HorizontalSumHeader(horizontalSumHeader).HorizontalSumContent(horizontalSumContent).create();

            //set mode of exTable is stickMode single
            $("#extable").exTable("stickMode", "single");

            /**
             * next a month
             */
            $("#nextMonth").click(function() {
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
            });

            /**
             * come back a month
             */
            $("#prevMonth").click(function() {
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
            });

            /**
             * Save data
             */
            $("#saveData").click(function() {
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
                        employeeId: self.listSid()[arrCell[i].rowIndex],
                        workTimeCode: arrCell[i].value.workTimeCode,
                        workTypeCode: arrCell[i].value.workTypeCode
                    }));
                }
                //Msg_436
                arrObj.push(new BasicSchedule({
                    date: "2017-01-09T00:00:00.000Z",
                    workTypeCode: "231",
                    workTimeCode: "001",
                    employeeId: "00000000-0000-0000-0000-000000000001"
                }));
                //Msg_468
                arrObj.push(new BasicSchedule({
                    date: "2017-01-10T00:00:00.000Z",
                    workTypeCode: "007",
                    workTimeCode: "001",
                    employeeId: "00000000-0000-0000-0000-000000000001"
                }));
                //workTimeCode == NULL or empty
                arrObj.push(new BasicSchedule({
                    date: "2017-01-11T00:00:00.000Z",
                    workTypeCode: "001",
                    workTimeCode: null,
                    employeeId: "00000000-0000-0000-0000-000000000001"
                }));
                //Msg_437
                arrObj.push(new BasicSchedule({
                    date: "2017-01-12T00:00:00.000Z",
                    workTypeCode: "001",
                    workTimeCode: "231",
                    employeeId: "00000000-0000-0000-0000-000000000001"
                }));
                //Msg_469
                arrObj.push(new BasicSchedule({
                    date: "2017-01-12T00:00:00.000Z",
                    workTypeCode: "001",
                    workTimeCode: "010",
                    employeeId: "00000000-0000-0000-0000-000000000001"
                }));

                //Msg_435
                arrObj.push(new BasicSchedule({
                    date: "2017-01-12T00:00:00.000Z",
                    workTypeCode: "001",
                    workTimeCode: "000",
                    employeeId: "00000000-0000-0000-0000-000000000001"
                }));

                //Msg_434
                arrObj.push(new BasicSchedule({
                    date: "2017-01-12T00:00:00.000Z",
                    workTypeCode: "002",
                    workTimeCode: "001",
                    employeeId: "00000000-0000-0000-0000-000000000001"
                }));

                service.registerData(arrObj).done(function(error: any) {
                    if (error) {
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
            });
        }

        /**
         *  update extable 
         */
        updateExTable(): void {
            let self = this;
            //Paste data into cell (set-sticker-single)
            $("#extable").exTable("stickData", __viewContext.viewModel.viewO.nameWorkTimeType());

            let newLeftMostDs = [], newMiddleDs = [], newDetailContentDs = [], newVertSumContentDs = [], newLeftHorzContentDs = [];

            _.each(self.listSid(), (x) => {
                //newLeftMost dataSource
                let empItem: PersonModel = _.find(self.empItems(), ['empId', x]);
                newLeftMostDs.push({ empId: x, empName: empItem.empCd + ' ' + empItem.empName });
                //newMiddle dataSource
                newMiddleDs.push({ empId: x, team: "1", rank: "A", qualification: "★", employmentName: "アルバイト", workplaceName: "東京本社", classificationName: "分類", positionName: "一般" });
                //newDetail dataSource
                let dsOfSid: any = _.filter(self.dataSource(), ['sid', x]);
                newDetailContentDs.push(new ExItem(x, dsOfSid, __viewContext.viewModel.viewO.listWorkType(), __viewContext.viewModel.viewO.listWorkTime(), false, self.arrDay));
                //newVertSumContent dataSource
                newVertSumContentDs.push({ empId: x, noCan: 6, noGet: 6 });
            });
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
                    key: "_" + x.yearMonthDay, width: "100px", headerText: "", visible: true
                });
            });

            let updateLeftmostContent = {
                dataSource: newLeftMostDs,
            };

            let updateMiddleContent = {
                dataSource: newMiddleDs,
            };

            let detailHeaderDeco = [], detailContentDeco = [];
            //Set color for detail
            self.setColorForDetail(detailHeaderDeco, detailContentDeco);
            let updateDetailContent = {
                columns: newDetailColumns,
                dataSource: newDetailContentDs,
                features: [{
                    name: "BodyCellStyle",
                    decorator: detailContentDeco
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
            $("#extable").exTable("updateTable", "detail", {}, updateDetailContent);
            $("#extable").exTable("updateTable", "horizontalSummaries", {}, updateHorzSumContent);
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
                    key: "_" + x.yearMonthDay, width: "100px", headerText: "", visible: true
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
            //Set color for detail
            self.setColorForDetail(detailHeaderDeco, detailContentDeco);

            let updateDetailHeader = {
                columns: newDetailColumns,
                dataSource: newDetailHeaderDs,
                features: [{
                    name: "HeaderCellStyle",
                    decorator: detailHeaderDeco
                }]
            };

            //if haven't data in extable, only update header detail and header horizontal
            if (self.empItems().length == 0) {
                $("#extable").exTable("updateTable", "detail", updateDetailHeader, {});
                $("#extable").exTable("updateTable", "horizontalSummaries", updateDetailHeader, {});
            } else {
                self.getDataBasicSchedule().done(() => {
                    //dataSour of detail
                    _.each(self.listSid(), (x) => {
                        let dsOfSid: any = _.filter(self.dataSource(), ['sid', x]);
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

                    let updateHorzSumContent = {
                        columns: newDetailColumns,
                        dataSource: horzSumContentDs
                    };

                    $("#extable").exTable("updateTable", "detail", updateDetailHeader, updateDetailContent);
                    $("#extable").exTable("updateTable", "horizontalSummaries", updateDetailHeader, updateHorzSumContent);
                });
            }
        }

        /**
         * Set color for detailHeader
         */
        setColorForDetail(detailHeaderDeco: any, detailContentDeco: any): void {
            let self = this;

            for (let i = 0; i < self.arrDay.length; i++) {
                if (self.arrDay[i].weekDay == '土') {
                    detailHeaderDeco.push(new CellColor("_" + self.arrDay[i].yearMonthDay, 0, "color-blue text-color-blue text-align-center"));
                    detailHeaderDeco.push(new CellColor("_" + self.arrDay[i].yearMonthDay, 1, "color-blue"));
                } else if (self.arrDay[i].weekDay == '日') {
                    detailHeaderDeco.push(new CellColor("_" + self.arrDay[i].yearMonthDay, 0, "color-pink text-color-red text-align-center"));
                    detailHeaderDeco.push(new CellColor("_" + self.arrDay[i].yearMonthDay, 1, "color-pink"));
                } else {
                    detailHeaderDeco.push(new CellColor("_" + self.arrDay[i].yearMonthDay, 0, "text-align-center"));
                }
                //Set color for detailContent
                _.each(self.listSid(), (empId) => {
                    if (self.arrDay[i].weekDay == '土' || self.arrDay[i].weekDay == '日') {
                        detailContentDeco.push(new CellColor("_" + self.arrDay[i].yearMonthDay, empId, "text-color-red"));
                    } else {
                        detailContentDeco.push(new CellColor("_" + self.arrDay[i].yearMonthDay, empId, "text-color-blue"));
                    }
                });
            }
        }
        
        /**
         * Set error
         */
        addListError(errorsRequest: Array<string>) {
            var messages = _.map(errorsRequest, function(err){
                return {
                    err: nts.uk.resource.getMessage(err)   
                };    
            });
            
            var errorVm = {
                messageId:  errorsRequest,
                messages: messages
            };
            
            nts.uk.ui.dialog.bundledErrors(errorVm);
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
        workTypeCode: string
    }

    class BasicSchedule {
        date: string;
        employeeId: string;
        workTimeCode: string;
        workTypeCode: string;

        constructor(params: IBasicSchedule) {
            this.date = params.date;
            this.employeeId = params.employeeId;
            this.workTimeCode = params.workTimeCode;
            this.workTypeCode = params.workTypeCode;
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
        constructor(code: string, name: string, description: string) {
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
        ymd: Date;
        year: string;
        month: string;
        day: string;
        weekDay: string;
        yearMonthDay: string;

        constructor(ymd: Date) {
            this.ymd = ymd;
            this.year = moment(this.ymd).format('YYYY');
            this.month = moment(this.ymd).format('M');
            this.day = moment(this.ymd).format('D');
            this.weekDay = moment(this.ymd).format('dd');
            this.yearMonthDay = this.year + moment(this.ymd).format('MM') + moment(this.ymd).format('DD');
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
            this.symbol = params.symbol;
            this.startTime = params.startTime;
            this.endTime = params.endTime;
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
                //holiday
                if (arrDay[i].weekDay == '日' || arrDay[i].weekDay == '土') {
                    this['_' + arrDay[i].yearMonthDay] = new ExCell({
                        endTime: null,
                        startTime: null,
                        symbol: null,
                        workTimeCode: "",
                        workTimeName: "",
                        workTypeCode: "",
                        workTypeName: "休日",
                    });
                } else if (obj) {
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