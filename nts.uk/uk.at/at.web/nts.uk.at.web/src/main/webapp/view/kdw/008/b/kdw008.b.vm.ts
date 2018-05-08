module nts.uk.at.view.kdw008.b {
    import getText = nts.uk.resource.getText;
    export module viewmodel {
        export class ScreenModel {

            newMode: KnockoutObservable<boolean>;
            isUpdate: boolean;
            hasdata: boolean;
            listMonthlyAttdItemFullData: KnockoutObservableArray<any>

            currentBusinessTypeCode: KnockoutObservable<string>;
            currentBusinessTypeName: KnockoutObservable<string>;

            currentBusinessType: KnockoutObservable<BusinessTypeDetailModel>;

            // list businessType
            businessTypeList: KnockoutObservableArray<BusinessTypeModel>;
            columns1: KnockoutObservableArray<NtsGridListColumn>;
            selectedCode: KnockoutObservable<any>;
            //combobox select sheetNo
            sheetNoList: KnockoutObservableArray<SheetNoModel>;
            itemNameCbb2: KnockoutObservable<string>;
            currentCodeCbb2: KnockoutObservable<number>;
            selectedSheetNo: KnockoutObservable<number>;

            //swap list tab 1
            monthlyDetailList: KnockoutObservableArray<BusinessTypeFormatDetailModel>;
            columns3: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            columns4: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            currentCodeListSwapMonthly: KnockoutObservableArray<any>;
            businessTypeFormatMonthlyValue: KnockoutObservableArray<AttendanceItemModel>;
            monthlyDataSource: KnockoutObservableArray<AttendanceItemModel>;

            //swap list tab 2
            currentCodeListSwap2: KnockoutObservableArray<any>;
            businessTypeFormatDailyValue: KnockoutObservableArray<AttendanceItemModel>;
            columns2: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            selectedSheetName: KnockoutObservable<string>;
            dailyDataSource: KnockoutObservableArray<AttendanceItemModel>;

            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;

            //is daily
            isDaily: boolean;

            //monthly
            listMonthlyAttdItem: KnockoutObservableArray<any>;
            valuesMonthly: KnockoutObservableArray<any>;

            // monthly tab 3
            valuesMonth: KnockoutObservableArray<any>;


            constructor(dataShare: any) {
                var self = this;
                //check daily
                self.isDaily = dataShare.ShareObject;
                self.isDaily = false;

                //monthly
                self.listMonthlyAttdItem = ko.observableArray([]);
                self.listMonthlyAttdItemFullData = ko.observableArray([]);
                self.valuesMonthly = ko.observableArray([]);

                // monthly tab 3
                self.valuesMonth = ko.observableArray([]);

                self.newMode = ko.observable(false);
                self.isUpdate = true;
                self.hasdata = true;

                self.currentBusinessTypeCode = ko.observable('');
                self.currentBusinessTypeName = ko.observable('');

                self.selectedSheetName = ko.observable(null);

                self.businessTypeList = ko.observableArray([]);
                self.currentBusinessType = ko.observable(new BusinessTypeDetailModel(null));

                self.columns1 = ko.observableArray([
                    { headerText: 'コード', key: 'businessTypeCode', width: 100 },
                    { headerText: '勤務種別名称', key: 'businessTypeName', width: 150, formatter: _.escape }
                ]);
                this.selectedCode = ko.observable();

                self.columns4 = ko.observableArray([
                    { headerText: getText('KDW008_7'), key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: getText('KDW008_7'), key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: getText('KDW008_8'), key: 'attendanceItemName', width: 150 }
                ]);
                self.columns3 = ko.observableArray([
                    { headerText: getText('KDW008_7'), key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: getText('KDW008_7'), key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: getText('KDW008_8'), key: 'attendanceItemName', width: 150 }
                ]);
                self.columns2 = ko.observableArray([
                    { headerText: getText('KDW008_7'), key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: getText('KDW008_7'), key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: getText('KDW008_8'), key: 'attendanceItemName', width: 150 }
                ]);

                self.columns5 = ko.observableArray([
                    { headerText: getText('KDW008_7'), key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: getText('KDW008_7'), key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: getText('KDW008_8'), key: 'attendanceItemName', width: 150 }
                ]);

                //swap list 1
                self.monthlyDetailList = ko.observableArray([]);
                self.businessTypeFormatMonthlyValue = ko.observableArray([]);
                self.monthlyDataSource = ko.observableArray([]);

                var monthlySwapList = [];
                self.currentCodeListSwapMonthly = ko.observableArray(monthlySwapList);
                this.currentCodeListSwapMonthly.subscribe(function(value) {
                    console.log(value);
                });
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: '月次項目', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(self.isDaily) },
                    { id: 'tab-2', title: '日次項目', content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(self.isDaily) },
                    { id: 'tab-3', title: '月次項目', content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(!self.isDaily) },
                ]);
                self.selectedTab = ko.observable('tab-1');

                //combobox select sheetNo tab2
                self.sheetNoList = ko.observableArray([
                    new SheetNoModel('1', '1'),
                    new SheetNoModel('2', '2'),
                    new SheetNoModel('3', '3'),
                    new SheetNoModel('4', '4'),
                    new SheetNoModel('5', '5'),
                    new SheetNoModel('6', '6'),
                    new SheetNoModel('7', '7'),
                    new SheetNoModel('8', '8'),
                    new SheetNoModel('9', '9'),
                    new SheetNoModel('10', '10')
                ]);
                self.selectedSheetNo = ko.observable(1);
                self.selectedSheetNo.subscribe((value) => {
                    if (self.isDaily) {
                        nts.uk.ui.errors.clearAll();
                        self.getDetail(self.selectedCode());
                    } else {
                        self.valuesMonthly([]);
                        self.listMonthlyAttdItem(_.cloneDeep(self.listMonthlyAttdItemFullData()));
                        self.getMonthRight(self.selectedCode(), value);
                    }
                });

                //swaplist 2
                var x = [];
                this.currentCodeListSwap2 = ko.observableArray(x);
                this.currentCodeListSwap2.subscribe(function(value) {
                    console.log(value);
                });
                self.businessTypeFormatDailyValue = ko.observableArray([]);
                self.dailyDataSource = ko.observableArray([]);

                self.selectedCode.subscribe(newValue => {
                    if (nts.uk.text.isNullOrEmpty(newValue)) return;
                    let empSelect = _.find(self.businessTypeList(), bus => {
                        return bus.businessTypeCode == newValue;
                    });

                    if (empSelect) {
                        self.currentBusinessTypeCode(empSelect.businessTypeCode);
                        self.currentBusinessTypeName(empSelect.businessTypeName);
                    }
                    nts.uk.ui.errors.clearAll();
                    if (self.isDaily) {
                        self.getDetail(self.currentBusinessTypeCode());
                    } else {
                        self.getMonthRight(newValue, self.selectedSheetNo());
                    }

                });

            }

            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();

                let dfdGetBusinessType = self.getBusinessType();
                let dfdGetListMonthlyAttdItem = self.getListMonthlyAttdItem();
                $.when(dfdGetBusinessType, dfdGetListMonthlyAttdItem).done(function(dfdGetBusinessTypeData, dfdGetListMonthlyAttdItemData) {
                    self.getMonthRight(self.selectedCode(), self.selectedSheetNo());
                    dfd.resolve();
                });
                dfd.resolve();
                return dfd.promise();
            }

            getListMonthlyAttdItem() {
                let self = this;
                let dfd = $.Deferred();
                new service.Service().getListMonthlyAttdItem().done(function(data) {
                    self.listMonthlyAttdItem(data);
                    self.listMonthlyAttdItemFullData(_.cloneDeep(data));

                    dfd.resolve();
                });
                return dfd.promise();
            }


            getBusinessType() {
                let self = this;
                let dfd = $.Deferred();
                nts.uk.ui.block.grayout();
                self.businessTypeList([]);
                new service.Service().getBusinessType().done(function(data: Array<IBusinessType>) {
                    if (data && data.length > 0) {
                        self.businessTypeList(_.map(data, item => { return new BusinessTypeModel(item) }));
                        self.currentBusinessTypeCode(self.businessTypeList()[0].businessTypeCode);
                        self.currentBusinessTypeName(self.businessTypeList()[0].businessTypeName);
                        self.selectedCode(self.businessTypeList()[0].businessTypeCode);
                        self.getDetail(self.businessTypeList()[0].businessTypeCode);
                        nts.uk.ui.block.clear();
                    } else {
                        nts.uk.ui.dialog.alert({ messageId: "Msg_242" });
                        nts.uk.ui.block.clear();
                        self.setNewMode();
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert({ messageId: "Msg_242" });
                    self.hasdata(false);
                    dfd.resolve();
                });

                return dfd.promise();
            }

            setNewMode() {
                let self = this;
                self.businessTypeList([]);
                self.newMode(true);
                self.isUpdate = false;
                self.currentBusinessType(new BusinessTypeDetailModel(null));
                self.currentBusinessTypeCode(null);
                self.currentBusinessTypeName('');
                self.selectedCode(null);
                nts.uk.ui.errors.clearAll();
            }

            getDetail(businessTypeCode: string) {
                let self = this,
                    dfd = $.Deferred();
                new service.Service().getDailyPerformance(businessTypeCode, self.selectedSheetNo()).done(function(data: IBusinessTypeDetail) {
                    if (data) {
                        self.valuesMonthly([]);
                        self.businessTypeFormatDailyValue([]);
                        self.currentBusinessType(new BusinessTypeDetailModel(data));
                        self.currentBusinessType().attendanceItemDtos.valueHasMutated();
                        var dailyDataSource = _.map(self.currentBusinessType().attendanceItemDtos(), item => {
                            var obj = {
                                attendanceItemId: item.attendanceItemId,
                                attendanceItemName: item.attendanceItemName,
                                attendanceItemDisplayNumber: item.attendanceItemDisplayNumber,
                                columnWidth: item.columnWidth
                            }
                            return new AttendanceItemModel(obj);
                        })

                        // show data tab 1
                        self.monthlyDataSource(_.clone(dailyDataSource));
                        self.monthlyDataSource.valueHasMutated();
                        data.businessTypeFormatMonthlyDtos = _.sortBy(data.businessTypeFormatMonthlyDtos, ["order"]);
                        if (data.businessTypeFormatMonthlyDtos) {
                            var attendanceItemModelMonthly = _.map(data.businessTypeFormatMonthlyDtos, item => {
                                var obj = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber,
                                    columnWidth: item.columnWidth
                                };
                                return new AttendanceItemModel(obj);
                            });
                            self.valuesMonthly(attendanceItemModelMonthly);

                        } else self.valuesMonthly([]);
                        //show data tab 2
                        //self.selectedSheetNo(data.businessTypeFormatDailyDto.sheetNo);
                        self.selectedSheetName(data.businessTypeFormatDailyDto.sheetName);
                        self.currentBusinessType().attendanceItemDtos.valueHasMutated();
                        self.dailyDataSource(_.clone(dailyDataSource));
                        self.dailyDataSource.valueHasMutated();
                        if (data.businessTypeFormatDailyDto != null && data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos) {
                            data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos = _.sortBy(data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos, ["order"]);
                            var attendanceItemModelDaily = _.map(data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos, item => {
                                var daily = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber,
                                    columnWidth: item.columnWidth
                                }
                                return new AttendanceItemModel(daily);
                            });
                            self.businessTypeFormatDailyValue(attendanceItemModelDaily);
                        } else {
                            self.businessTypeFormatDailyValue([]);
                        }

                    } else {
                        self.currentBusinessType([]);
                    }
                    dfd.resolve();
                }).fail(error => {

                });
                return dfd.promise();

            }

            getMonthRight(code: string, no: number) {
                let self = this;
                dfd = $.Deferred();
                self.valuesMonth([]);
                self.listMonthlyAttdItem(_.cloneDeep(self.listMonthlyAttdItemFullData()));
                new service.Service().getListMonthRight(code).done(function(data) {
                    if (data) {
                        let temp = data.displayItem.listSheetCorrectedMonthly;
                        for (let i = 0; i < temp.length; i++) {
                            if (temp[i].sheetNo == no) {
                                self.valuesMonth(temp[i].listDisplayTimeItem);
                            }
                        }
                        if (self.valuesMonth()) {
                            var attendanceItemModelMonth = _.map(self.valuesMonth(), item => {
                                var obj = {
                                    attendanceItemId: item.itemDaily,
                                    attendanceItemName: _.filter(self.listMonthlyAttdItemFullData(), function(o) { return item.itemDaily == o.attendanceItemId; })[0].attendanceItemName,
                                    attendanceItemDisplayNumber: item.displayOrder,
                                    columnWidth: item.columnWidthTable
                                };
                                return new AttendanceItemModel(obj);
                            });
                            self.valuesMonth(attendanceItemModelMonth);

                        } else self.valuesMonth([]);
                    } else {
                        self.valuesMonth([]);
                        self.listMonthlyAttdItem(_.cloneDeep(self.listMonthlyAttdItemFullData()));

                    }
                    dfd.resolve();
                });
                dfd.resolve();
            }

            addOrUpdateClick() {
                let self = this;
                if (self.isDaily) {
                    if (!self.isUpdate) {
                        self.updateData();
                        return;
                    }
                    self.register();
                } else {
                    self.registerMonthly();
                }
            }

            registerMonthly() {
                let self = this; $(".need-check").trigger("validate");
                if (!nts.uk.ui.errors.hasError()) {
                    let listDisplayTimeItem = [];
                    for (let i = 0; i < self.valuesMonth().length; i++) {
                        let obj = new DisplayTimeItem(
                            self.valuesMonth()[i].attendanceItemDisplayNumber,
                            self.valuesMonth()[i].attendanceItemId,
                            null
                        );
                        listDisplayTimeItem.push(obj);
                    }


                    let listSheetMonthly = [new SheetCorrectedMonthly(
                        self.selectedSheetNo(),
                        self.selectedSheetName(),
                        listDisplayTimeItem
                    )];

                    let temp = new MonthlyRecordWorkType("", self.currentBusinessTypeCode(),
                        new MonthlyActualResults(listSheetMonthly)
                    );
                    nts.uk.ui.block.invisible();
                    new service.Service().updateMonthly(temp).done(function() {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                            nts.uk.ui.block.clear();
                            self.getMonthRight(self.currentBusinessTypeCode(), 1);
                            self.selectedCode(self.currentBusinessTypeCode());
                            self.selectedCode.valueHasMutated();
                        });
                        $("#currentName").focus();
                    }).always(function() {
                    }).fail(function(error) {
                        nts.uk.ui.block.clear();
                        $('#currentCode').ntsError('set', error);
                    });
                }
            }

            register() {
                let self = this; $(".need-check").trigger("validate");
                if (!nts.uk.ui.errors.hasError()) {
                    //add or update Monthly
                    var businessTypeFormatDetailDtosAdd = _.map(self.valuesMonthly(), item => {
                        var indexOfItem = _.findIndex(self.valuesMonthly(), { attendanceItemId: item.attendanceItemId });
                        var monthlyAdd = {
                            attendanceItemId: item.attendanceItemId,
                            dislayNumber: item.attendanceItemDisplayNumber,
                            attendanceItemName: item.attendanceItemName,
                            order: indexOfItem,
                            columnWidth: item.columnWidth ? item.columnWidth : null
                        };
                        return new BusinessTypeFormatDetailModel(monthlyAdd);
                    })
                    var addOrUpdateBusinessFormatMonthly = new AddBusinessFormatMonthly(self.currentBusinessTypeCode(), businessTypeFormatDetailDtosAdd);

                    //add or update Daily
                    var businessTypeFormatDetailDailyDto = _.map(self.businessTypeFormatDailyValue(), item => {
                        var indexOfDaily = _.findIndex(self.businessTypeFormatDailyValue(), { attendanceItemId: item.attendanceItemId });
                        var dailyAdd = {
                            attendanceItemId: item.attendanceItemId,
                            dislayNumber: item.attendanceItemDisplayNumber,
                            attendanceItemName: item.attendanceItemName,
                            order: indexOfDaily,
                            columnWidth: item.columnWidth ? item.columnWidth : null
                        };
                        return new BusinessTypeFormatDetailModel(dailyAdd);
                    });
                    var addOrUpdateBusinessFormatDaily = new AddBusinessFormatDaily(self.currentBusinessTypeCode(), self.selectedSheetNo(), self.selectedSheetName(), businessTypeFormatDetailDailyDto);

                    var addOrUpdateBusFormat = new AddOrUpdateBusFormat(addOrUpdateBusinessFormatMonthly, addOrUpdateBusinessFormatDaily);

                    new service.Service().addDailyDetail(addOrUpdateBusFormat).done(function() {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                            self.reloadData(self.currentBusinessTypeCode());
                        });
                        nts.uk.ui.block.clear();
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alertError({ messageId: 'Msg_920' });
                    });
                }
            }

            reloadData(currentBusinessTypeCode: string) {
                let self = this,
                    dfd = $.Deferred();
                let oldSelectIndex = _.findIndex(self.businessTypeList(), item => { return item.currentBusinessTypeCode == currentBusinessTypeCode; });
                self.businessTypeList([]);
                new service.Service().getBusinessType().done(function(data: Array<IBusinessType>) {
                    if (data && data.length > 0) {
                        data = _.orderBy(data, ["businessTypeCode"], ['asc']);
                        self.businessTypeList(_.map(data, item => { return new BusinessTypeModel(item) }));
                        self.currentBusinessTypeCode(currentBusinessTypeCode);
                        //                        self.currentDailyFormatName(self.businessTypeList()[0].dailyPerformanceFormatName);
                        //                        self.selectedCode(dailyPerformanceFormatCode);
                        self.selectedSheetNo(1);
                        self.getDetail(currentBusinessTypeCode);
                    } else {
                        self.setNewMode();
                    }
                    dfd.resolve();
                });

                return dfd.promise();
            }

            dialog() {
                let self = this;
                nts.uk.ui.windows.setShared("openC", self.isDaily);
                nts.uk.ui.windows.sub.modal("../c/index.xhtml");
            }

        }

        //monthly

        export class MonthlyRecordWorkType {
            companyID: string;
            businessTypeCode: string;
            displayItem: MonthlyActualResults;
            constructor(companyID: string,
                businessTypeCode: string,
                displayItem: MonthlyActualResultsboolean) {
                this.companyID = companyID;
                this.businessTypeCode = businessTypeCode;
                this.displayItem = displayItem;

            }
        }

        export class MonthlyActualResults {
            listSheetCorrectedMonthly: Array<SheetCorrectedMonthly>;
            constructor(listSheetCorrectedMonthly: Array<SheetCorrectedMonthly>) {
                this.listSheetCorrectedMonthly = listSheetCorrectedMonthly;
            }
        }

        export class SheetCorrectedMonthly {
            sheetNo: number;
            sheetName: string;
            listDisplayTimeItem: Array<DisplayTimeItem>;
            constructor(sheetNo: number,
                sheetName: string,
                listDisplayTimeItem: Array<DisplayTimeItem>) {
                this.sheetNo = sheetNo;
                this.sheetName = sheetName;
                this.listDisplayTimeItem = listDisplayTimeItem;
            }
        }

        export class DisplayTimeItem {
            displayOrder: number;
            itemDaily: number;
            columnWidthTable: Number;
            constructor(
                displayOrder: number,
                itemDaily: number,
                columnWidthTable: Number) {
                this.displayOrder = displayOrder;
                this.itemDaily = itemDaily;
                this.columnWidthTable = (columnWidthTable == null ? null : columnWidthTable);
            }
        }


        export class SheetNoModel {
            sheetNoId: string;
            sheetNoName: string;
            constructor(sheetNoId: string, sheetNoName: string) {
                this.sheetNoId = sheetNoId;
                this.sheetNoName = sheetNoName;
            }
        }

        export class AddOrUpdateBusFormat {
            busTypeMonthlyCommand: KnockoutObservable<AddBusinessFormatMonthly>;
            busTypeDailyCommand: KnockoutObservable<AddBusinessFormatDaily>;
            constructor(addOrUpdateAuthorityFormatMonthly: KnockoutObservable<AddBusinessFormatMonthly>, addOrUpdateAuthorityFormatDaily: KnockoutObservable<AddBusinessFormatDaily>) {
                let self = this;
                self.busTypeMonthlyCommand = addOrUpdateAuthorityFormatMonthly;
                self.busTypeDailyCommand = addOrUpdateAuthorityFormatDaily;
            }
        }

        export class AddBusinessFormatMonthly {
            businesstypeCode: string;
            businessTypeFormatDetailDtos: Array<BusinessTypeFormatDetailModel>;
            constructor(businessTypeCode: string, businessTypeFormatDetailDtos: Array<BusinessTypeFormatDetailModel>) {
                let self = this;
                self.businesstypeCode = businessTypeCode || "";
                self.businessTypeFormatDetailDtos = businessTypeFormatDetailDtos || [];
            }
        }

        export class AddBusinessFormatDaily {
            businesstypeCode: string;
            sheetNo: number;
            sheetName: string;
            businessTypeFormatDetailDtos: Array<BusinessTypeFormatDetailModel>;
            constructor(businesstypeCode: string, sheetNo: number, sheetName: string, businessTypeFormatDetailDtos: Array<BusinessTypeFormatDetailModel>) {
                this.businesstypeCode = businesstypeCode || "";
                this.sheetNo = sheetNo || 0;
                this.sheetName = sheetName || null;
                this.businessTypeFormatDetailDtos = businessTypeFormatDetailDtos || [];
            }
        }

        export class BusinessTypeModel {
            businessTypeCode: string;
            businessTypeName: string;
            constructor(data: IBusinessType) {
                let self = this;
                if (!data) return;
                self.businessTypeCode = data.businessTypeCode || "";
                self.businessTypeName = data.businessTypeName || "";
            }
        }

        export class AttendanceItemModel {
            attendanceItemId: number = 0;
            attendanceItemName: string = "";
            attendanceItemDisplayNumber: number = 0;
            columnWidth: number;
            constructor(data: IAttendanceItem) {
                if (!data) return;
                this.attendanceItemId = data.attendanceItemId || 0;
                this.attendanceItemName = data.attendanceItemName || "";
                this.attendanceItemDisplayNumber = data.attendanceItemDisplayNumber || 0;
                this.columnWidth = data.columnWidth;
            }
        }

        export class BusinessTypeFormatDetailModel {
            attendanceItemId: number = 0;
            dislayNumber: number = 0;
            attendanceItemName: string = '';
            order: number = 0;
            columnWidth: number = 0;
            constructor(data: IBusinessTypeFormatDetail) {
                if (!data) return;
                this.attendanceItemId = data.attendanceItemId || 0;
                this.dislayNumber = data.dislayNumber || 0;
                this.attendanceItemName = data.attendanceItemName || '';
                this.order = data.order || 0;
                this.columnWidth = data.columnWidth || 0;
            }
        }

        export class BusinessTypeFormatDailyModel {
            sheetNo: number;
            sheetName: string;
            businessTypeFormatDetailDtos: Array<BusinessTypeFormatDetailModel>;
            constructor(sheetNo: number, sheetName: string, businessTypeFormatDetailDtos: Array<BusinessTypeFormatDetailModel>) {
                this.sheetNo = sheetNo || 0;
                this.sheetName = sheetName || null;
                this.businessTypeFormatDetailDtos = businessTypeFormatDetailDtos || [];
            }
        }

        export class BusinessTypeDetailModel {
            attendanceItemDtos: KnockoutObservableArray<AttendanceItemModel> = ko.observableArray([]);
            businessTypeFormatDailyDto: KnockoutObservable<BusinessTypeFormatDailyModel> = ko.observable(new BusinessTypeFormatDailyModel(1, null, null));
            businessTypeFormatMonthlyDtos: KnockoutObservableArray<BusinessTypeFormatDetailModel> = ko.observableArray([]);
            constructor(data: IBusinessTypeDetail) {
                if (!data) return;
                this.attendanceItemDtos(data.attendanceItemDtos ? _.map(data.attendanceItemDtos, item => { return new AttendanceItemModel(item) }) : []);
                this.businessTypeFormatDailyDto(data.businessTypeFormatDailyDto ? new BusinessTypeFormatDailyModel(data.businessTypeFormatDailyDto.sheetNo, data.businessTypeFormatDailyDto.sheetName, data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos) : null);
                this.businessTypeFormatMonthlyDtos(data.businessTypeFormatMonthlyDtos ? _.map(data.businessTypeFormatMonthlyDtos, item => { return new BusinessTypeFormatDetailModel(item) }) : []);
            }
        }

        export interface IBusinessTypeDetail {
            attendanceItemDtos: Array<IAttendanceItem>;
            businessTypeFormatDailyDto: IBusinessTypeFormatDaily;
            businessTypeFormatMonthlyDtos: Array<IBusinessTypeFormatDetail>;
        }

        export interface IBusinessType {
            businessTypeCode: string;
            businessTypeName: string;
        }
        export interface IAttendanceItem {
            attendanceItemId: number;
            attendanceItemName: string;
            attendanceItemDisplayNumber: number;
            columnWidth: number;
        }
        export interface IBusinessTypeFormatDetail {
            attendanceItemId: number;
            dislayNumber: number;
            attendanceItemName: string;
            order: number;
            columnWidth: number;
        }
        export interface IBusinessTypeFormatDaily {
            sheetNo: number;
            sheetName: string;
            businessTypeFormatDetailDtos: Array<IBusinessTypeFormatDetail>;
        }

    }
}
