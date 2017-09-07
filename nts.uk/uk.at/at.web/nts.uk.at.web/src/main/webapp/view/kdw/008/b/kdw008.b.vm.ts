module nts.uk.at.view.kdw008.b {
    export module viewmodel {
        export class ScreenModel {

            newMode: KnockoutObservable<boolean>;
            isUpdate: boolean;

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
            currentCodeListSwapMonthly: KnockoutObservableArray<any>;
            businessTypeFormatMonthlyValue: KnockoutObservableArray<AttendanceItemModel>;

            //swap list tab 2
            currentCodeListSwap2: KnockoutObservableArray<any>;
            businessTypeFormatDailyValue: KnockoutObservableArray<AttendanceItemModel>;
            columns2: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            selectedSheetName: KnockoutObservable<string>;

            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;

            constructor() {
                var self = this;
                self.newMode = ko.observable(false);
                self.isUpdate = true;

                self.currentBusinessTypeCode = ko.observable('');
                self.currentBusinessTypeName = ko.observable('');

                self.selectedSheetName = ko.observable('');

                self.businessTypeList = ko.observableArray([]);
                self.currentBusinessType = ko.observable(new BusinessTypeDetailModel(null));

                self.columns1 = ko.observableArray([
                    { headerText: 'コード', key: 'businessTypeCode', width: 100 },
                    { headerText: '勤務種別名称', key: 'businessTypeName', width: 150, formatter: _.escape }
                ]);
                this.selectedCode = ko.observable();

                self.columns3 = ko.observableArray([
                    { headerText: 'コード', key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: 'ID', key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: '名称', key: 'attendanceItemName', width: 150 }
                ]);
                self.columns2 = ko.observableArray([
                    { headerText: 'コード', key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: 'ID', key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: '名称', key: 'attendanceItemName', width: 150 }
                ]);

                //swap list 1
                self.monthlyDetailList = ko.observableArray([]);
                self.businessTypeFormatMonthlyValue = ko.observableArray([]);
                var monthlySwapList = [];
                self.currentCodeListSwapMonthly = ko.observableArray(monthlySwapList);
                this.currentCodeListSwapMonthly.subscribe(function(value) {
                    console.log(value);
                });
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: '月次項目', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: '日次項目', content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) }
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
                    self.getDetail(self.selectedCode());
                });

                //swaplist 2
                var x = [];
                this.currentCodeListSwap2 = ko.observableArray(x);
                this.currentCodeListSwap2.subscribe(function(value) {
                    console.log(value);
                });
                self.businessTypeFormatDailyValue = ko.observableArray([]);

                self.selectedCode.subscribe(newValue => {
                    if (nts.uk.text.isNullOrEmpty(newValue)) return;
                    let empSelect = _.find(self.businessTypeList(), bus => {
                        return bus.businessTypeCode == newValue;
                    });
                    if (empSelect) {
                        self.currentBusinessTypeCode(empSelect.businessTypeCode);
                        self.currentBusinessTypeName(empSelect.businessTypeName);
                    }
                    self.getDetail(self.currentBusinessTypeCode());
                });

            }

            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.businessTypeList([]);
                new service.Service().getBusinessType().done(function(data: Array<IBusinessType>) {
                    if (data && data.length > 0) {
                        self.businessTypeList(_.map(data, item => { return new BusinessTypeModel(item) }));
                        self.currentBusinessTypeCode(self.businessTypeList()[0].businessTypeCode);
                        self.currentBusinessTypeName(self.businessTypeList()[0].businessTypeName);
                        self.selectedCode(self.businessTypeList()[0].businessTypeCode);
                        self.getDetail(self.businessTypeList()[0].businessTypeCode);
                    } else {
                        self.setNewMode();
                    }
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
            }

            getDetail(businessTypeCode: string) {
                let self = this,
                    dfd = $.Deferred();
                new service.Service().getDailyPerformance(businessTypeCode, self.selectedSheetNo()).done(function(data: IBusinessTypeDetail) {
                    if (data) {
                        self.businessTypeFormatMonthlyValue([]);
                        self.businessTypeFormatDailyValue([]);
                        self.currentBusinessType(new BusinessTypeDetailModel(data));
                        self.currentBusinessType().attendanceItemDtos.valueHasMutated();
                        // show data tab 1
                        data.businessTypeFormatMonthlyDtos = _.sortBy(data.businessTypeFormatMonthlyDtos, ["order"]);
                        if (data.businessTypeFormatMonthlyDtos) {
                            var attendanceItemModelMonthly = _.map(data.businessTypeFormatMonthlyDtos, item => {
                                var obj = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber
                                };
                                return new AttendanceItemModel(obj);
                            });
                            self.businessTypeFormatMonthlyValue(attendanceItemModelMonthly);

                        } else self.businessTypeFormatMonthlyValue([]);
                        //show data tab 2
                        //self.selectedSheetNo(data.businessTypeFormatDailyDto.sheetNo);
                        if(data.businessTypeFormatDailyDto == null){
                           self.selectedSheetName("");
                        } else self.selectedSheetName(data.businessTypeFormatDailyDto.sheetName);
                        self.currentBusinessType().attendanceItemDtos.valueHasMutated();
                        if (data.businessTypeFormatDailyDto != null && data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos) {
                            data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos = _.sortBy(data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos, ["order"]);
                            var attendanceItemModelDaily = _.map(data.businessTypeFormatDailyDto.businessTypeFormatDetailDtos, item => {
                                var daily = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber
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

            addOrUpdateClick() {
                let self = this;
                if (!self.isUpdate) {
                    self.updateData();
                    return;
                }
                self.register();
            }

            register() {
                let self = this;
                //add or update Monthly
                var businessTypeFormatDetailDtos = _.map(self.businessTypeFormatMonthlyValue(), item => {
                    var indexOfItem = _.findIndex(self.businessTypeFormatMonthlyValue(), { attendanceItemId: item.attendanceItemId });
                    var obj = {
                        attendanceItemId: item.attendanceItemId,
                        dislayNumber: item.attendanceItemDisplayNumber,
                        attendanceItemName: item.attendanceItemName,
                        order: indexOfItem,
                        columnWidth: 0
                    };
                    return new BusinessTypeFormatDetailModel(obj);
                })
                var addOrUpdateBusinessFormatMonthly = new AddBusinessFormatMonthly(self.currentBusinessTypeCode(), businessTypeFormatDetailDtos);
                if (self.currentBusinessType().businessTypeFormatMonthlyDtos().length > 0) {
                    new service.Service().updateMonthlyDetail(addOrUpdateBusinessFormatMonthly);
                } else {
                    new service.Service().addMonthlyDetail(addOrUpdateBusinessFormatMonthly);
                }

                //add or update Daily
                var businessTypeFormatDetailDailyDto = _.map(self.businessTypeFormatDailyValue(), item => {
                    var indexOfDaily = _.findIndex(self.businessTypeFormatDailyValue(), { attendanceItemId: item.attendanceItemId });
                    var monthly = {
                        attendanceItemId: item.attendanceItemId,
                        dislayNumber: item.attendanceItemDisplayNumber,
                        attendanceItemName: item.attendanceItemName,
                        order: indexOfDaily,
                        columnWidth: 0
                    };
                    return new BusinessTypeFormatDetailModel(monthly);
                });
                var addOrUpdateBusinessFormatDaily = new AddBusinessFormatDaily(self.currentBusinessTypeCode(), self.selectedSheetNo(), self.selectedSheetName(), businessTypeFormatDetailDailyDto);
                if (self.currentBusinessType().businessTypeFormatDailyDto().businessTypeFormatDetailDtos.length > 0) {
                    new service.Service().updateDailyDetail(addOrUpdateBusinessFormatDaily);
                } else {
                    new service.Service().addDailyDetail(addOrUpdateBusinessFormatDaily);
                }
                nts.uk.ui.dialog.alert({ messageId: "Msg_15" });
                self.getDetail(self.currentBusinessTypeCode());
            }
            
            dialog(){
               nts.uk.ui.windows.sub.modal("../c/index.xhtml"); 
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
                this.sheetName = sheetName || "";
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
            constructor(data: IAttendanceItem) {
                if (!data) return;
                this.attendanceItemId = data.attendanceItemId || 0;
                this.attendanceItemName = data.attendanceItemName || "";
                this.attendanceItemDisplayNumber = data.attendanceItemDisplayNumber || 0;
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
                this.sheetName = sheetName || "";
                this.businessTypeFormatDetailDtos = businessTypeFormatDetailDtos || [];
            }
        }

        export class BusinessTypeDetailModel {
            attendanceItemDtos: KnockoutObservableArray<AttendanceItemModel> = ko.observableArray([]);
            businessTypeFormatDailyDto: KnockoutObservable<BusinessTypeFormatDailyModel> = ko.observable(new BusinessTypeFormatDailyModel(null));
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
