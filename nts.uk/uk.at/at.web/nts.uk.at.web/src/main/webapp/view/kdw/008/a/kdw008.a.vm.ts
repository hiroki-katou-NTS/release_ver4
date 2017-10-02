module nts.uk.at.view.kdw008.a {
    export module viewmodel {
        export class ScreenModel {

            newMode: KnockoutObservable<boolean>;
            isUpdate: boolean;

            checked: KnockoutObservable<boolean>;

            currentDailyFormatCode: KnockoutObservable<string>;
            currentDailyFormatName: KnockoutObservable<string>;

            currentBusinessType: KnockoutObservable<AuthorityDetailModel>;

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
            monthlyDetailList: KnockoutObservableArray<DailyAttendanceAuthorityDetailDto>;
            columns3: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            currentCodeListSwapMonthly: KnockoutObservableArray<any>;
            authorityFormatMonthlyValue: KnockoutObservableArray<AttendanceItemModel>;

            //swap list tab 2
            currentCodeListSwap2: KnockoutObservableArray<any>;
            authorityFormatDailyValue: KnockoutObservableArray<AttendanceItemModel>;
            columns2: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            selectedSheetName: KnockoutObservable<string>;

            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;

            constructor() {
                var self = this;
                self.newMode = ko.observable(false);
                self.isUpdate = true;

                self.checked = ko.observable(false);

                self.currentDailyFormatCode = ko.observable('');
                self.currentDailyFormatName = ko.observable('');

                self.selectedSheetName = ko.observable('');

                self.businessTypeList = ko.observableArray([]);
                self.currentBusinessType = ko.observable(new AuthorityDetailModel(null));

                self.columns1 = ko.observableArray([
                    { headerText: 'コード', key: 'dailyPerformanceFormatCode', width: 100 },
                    { headerText: '勤務種別名称', key: 'dailyPerformanceFormatName', width: 150, formatter: _.escape }
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
                self.authorityFormatMonthlyValue = ko.observableArray([]);
                var monthlySwapList = [];
                self.currentCodeListSwapMonthly = ko.observableArray(monthlySwapList);
                this.currentCodeListSwapMonthly.subscribe(function(value) {
                    console.log(value);
                });
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: '日次項目', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: '月次項目', content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) }
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
                self.authorityFormatDailyValue = ko.observableArray([]);

                self.selectedCode.subscribe(newValue => {
                    if (nts.uk.text.isNullOrEmpty(newValue)) return;
                    let empSelect = _.find(self.businessTypeList(), bus => {
                        return bus.dailyPerformanceFormatCode == newValue;
                    });
                    if (empSelect) {
                        self.currentDailyFormatCode(empSelect.dailyPerformanceFormatCode);
                        self.currentDailyFormatName(empSelect.dailyPerformanceFormatName);
                    }
                    self.getDetail(self.currentDailyFormatCode());
                });

            }

            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                self.businessTypeList([]);
                new service.Service().getBusinessType().done(function(data: Array<IDailyPerformanceFormatType>) {
                    if (data && data.length > 0) {
                        self.businessTypeList(_.map(data, item => { return new BusinessTypeModel(item) }));
                        self.currentDailyFormatCode(self.businessTypeList()[0].dailyPerformanceFormatCode);
                        self.currentDailyFormatName(self.businessTypeList()[0].dailyPerformanceFormatName);
                        self.selectedCode(self.businessTypeList()[0].dailyPerformanceFormatCode);
                        self.getDetail(self.businessTypeList()[0].dailyPerformanceFormatCode);
                    } else {
                        self.setNewMode();
                    }
                    dfd.resolve();
                });

                return dfd.promise();
            }

            setNewMode() {
                let self = this;
                self.newMode(true);
                self.isUpdate = false;
                self.currentDailyFormatCode(null);
                self.currentDailyFormatName('');
                self.selectedCode(null);
                self.getDetail(self.selectedCode());
                self.checked(false);
            }

            getDetail(dailyPerformanceFormatCode: string) {
                let self = this,
                    dfd = $.Deferred();
                new service.Service().getDailyPerformance(dailyPerformanceFormatCode, self.selectedSheetNo()).done(function(data: IDailyPerformanceFormatTypeDetail) {
                    if (data) {
                        if (data.isDefaultInitial == 1) {
                            self.checked(true);
                        } else {
                            self.checked(false);
                        }
                        self.authorityFormatMonthlyValue([]);
                        self.authorityFormatDailyValue([]);
                        self.currentBusinessType(new AuthorityDetailModel(data));
                        self.currentBusinessType().attendanceItemDtos.valueHasMutated();
                        // show data tab 2
                        data.dailyAttendanceAuthorityMonthlyDto = _.sortBy(data.dailyAttendanceAuthorityMonthlyDto, ["order"]);
                        if (data.dailyAttendanceAuthorityMonthlyDto) {
                            var attendanceItemModelMonthly = _.map(data.dailyAttendanceAuthorityMonthlyDto, item => {
                                var obj = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber
                                };
                                return new AttendanceItemModel(obj);
                            });
                            self.authorityFormatMonthlyValue(attendanceItemModelMonthly);

                        } else self.authorityFormatMonthlyValue([]);
                        //show data tab 1
                        //self.selectedSheetNo(data.businessTypeFormatDailyDto.sheetNo);
                        if (data.dailyAttendanceAuthorityDailyDto == null) {
                            self.selectedSheetName("");
                        } else self.selectedSheetName(data.dailyAttendanceAuthorityDailyDto.sheetName);
                        self.currentBusinessType().attendanceItemDtos.valueHasMutated();
                        if (data.dailyAttendanceAuthorityDailyDto != null && data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos) {
                            data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos = _.sortBy(data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos, ["order"]);
                            var attendanceItemModelDaily = _.map(data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos, item => {
                                var daily = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber
                                }
                                return new AttendanceItemModel(daily);
                            });
                            self.authorityFormatDailyValue(attendanceItemModelDaily);
                        } else {
                            self.authorityFormatDailyValue([]);
                        }

                    } else {
                        self.currentBusinessType([]);
                    }
                    dfd.resolve();
                }).fail(error => {

                });
                return dfd.promise();

            }

            remove() {
                let self = this;
                let isDefaultInitial = 0;
                if (self.checked() == true) {
                    let isDefaultInitial = 1;
                }
                var removeAuthorityDto = {
                    dailyPerformanceFormatCode: self.currentDailyFormatCode(),
                    isDefaultInitial: isDefaultInitial
                }
                nts.uk.ui.dialog.confirm(nts.uk.resource.getMessage("Msg_18", []))
                    .ifYes(() => {
                        new service.Service().removeAuthorityDailyFormat(removeAuthorityDto);
                        //                        self.getDetail(self.businessTypeList()[1].dailyPerformanceFormatCode);
                        self.reloadData();
                    });
            }

            addOrUpdateClick() {
                let self = this;
                //                if (!self.isUpdate) {
                //                    self.updateData();
                //                    return;
                //                }
                self.register();
            }

            register() {
                let self = this;
                //add or update Monthly
                var authorityFormatDetailDtos = _.map(self.authorityFormatMonthlyValue(), item => {
                    var indexOfItem = _.findIndex(self.authorityFormatMonthlyValue(), { attendanceItemId: item.attendanceItemId });
                    var obj = {
                        attendanceItemId: item.attendanceItemId,
                        dislayNumber: item.attendanceItemDisplayNumber,
                        attendanceItemName: item.attendanceItemName,
                        order: indexOfItem,
                        columnWidth: 0
                    };
                    return new DailyAttendanceAuthorityDetailDto(obj);
                })
                var addOrUpdateBusinessFormatMonthly = new AddAuthorityFormatMonthly(self.currentDailyFormatCode(), authorityFormatDetailDtos);
                if (self.currentBusinessType().dailyAttendanceAuthorityMonthlyDto().length > 0) {
                    new service.Service().updateMonthlyDetail(addOrUpdateBusinessFormatMonthly);
                } else {
                    new service.Service().addMonthlyDetail(addOrUpdateBusinessFormatMonthly);
                }

                //add or update Daily
                var businessTypeFormatDetailDailyDto = _.map(self.authorityFormatDailyValue(), item => {
                    var indexOfDaily = _.findIndex(self.authorityFormatDailyValue(), { attendanceItemId: item.attendanceItemId });
                    var monthly = {
                        attendanceItemId: item.attendanceItemId,
                        dislayNumber: item.attendanceItemDisplayNumber,
                        attendanceItemName: item.attendanceItemName,
                        order: indexOfDaily,
                        columnWidth: 0
                    };
                    return new DailyAttendanceAuthorityDetailDto(monthly);
                });

                if (self.checked() == true) {
                    var addOrUpdateBusinessFormatDaily = new AddAuthorityFormatDaily(self.currentDailyFormatCode(), self.currentDailyFormatName(), self.selectedSheetNo(), self.selectedSheetName(), businessTypeFormatDetailDailyDto, 1);
                } else {
                    var addOrUpdateBusinessFormatDaily = new AddAuthorityFormatDaily(self.currentDailyFormatCode(), self.currentDailyFormatName(), self.selectedSheetNo(), self.selectedSheetName(), businessTypeFormatDetailDailyDto, 0);
                }

                if (self.currentBusinessType().dailyAttendanceAuthorityDailyDto() != null && self.currentBusinessType().dailyAttendanceAuthorityDailyDto().dailyAttendanceAuthorityDetailDtos.length > 0) {
                    new service.Service().updateDailyDetail(addOrUpdateBusinessFormatDaily);
                } else {
                    new service.Service().addDailyDetail(addOrUpdateBusinessFormatDaily);
                }
                nts.uk.ui.dialog.alert({ messageId: "Msg_15" });
                //self.getDetail(self.currentDailyFormatCode());
                self.reloadData();
            }

            reloadData() {
                let self = this,
                    dfd = $.Deferred();
                self.businessTypeList([]);
                new service.Service().getBusinessType().done(function(data: Array<IDailyPerformanceFormatType>) {
                    if (data && data.length > 0) {
                        self.businessTypeList(_.map(data, item => { return new BusinessTypeModel(item) }));
                        self.currentDailyFormatCode(self.businessTypeList()[0].dailyPerformanceFormatCode);
                        self.currentDailyFormatName(self.businessTypeList()[0].dailyPerformanceFormatName);
                        self.selectedCode(self.businessTypeList()[0].dailyPerformanceFormatCode);
                        self.getDetail(self.businessTypeList()[0].dailyPerformanceFormatCode);
                    } else {
                        self.setNewMode();
                    }
                    dfd.resolve();
                });

                return dfd.promise();
            }

            dialog() {
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

        export class AddAuthorityFormatMonthly {
            dailyPerformanceFormatCode: string;
            dailyAttendanceAuthorityDetailDtos: Array<DailyAttendanceAuthorityDetailDto>;
            constructor(dailyPerformanceFormatCode: string, dailyAttendanceAuthorityDetailDtos: Array<DailyAttendanceAuthorityDetailDto>) {
                let self = this;
                self.dailyPerformanceFormatCode = dailyPerformanceFormatCode || "";
                self.dailyAttendanceAuthorityDetailDtos = dailyAttendanceAuthorityDetailDtos || [];
            }
        }

        export class AddAuthorityFormatDaily {
            dailyPerformanceFormatCode: string;
            dailyPerformanceFormatName: string;
            sheetNo: number;
            sheetName: string;
            dailyAttendanceAuthorityDetailDtos: Array<DailyAttendanceAuthorityDetailDto>;
            isDefaultInitial: number;
            constructor(dailyPerformanceFormatCode: string, dailyPerformanceFormatName: string, sheetNo: number, sheetName: string, dailyAttendanceAuthorityDetailDtos: Array<DailyAttendanceAuthorityDetailDto>, isDefaultInitial: number) {
                this.dailyPerformanceFormatCode = dailyPerformanceFormatCode || "";
                this.dailyPerformanceFormatName = dailyPerformanceFormatName || "";
                this.sheetNo = sheetNo || 0;
                this.sheetName = sheetName || "";
                this.dailyAttendanceAuthorityDetailDtos = dailyAttendanceAuthorityDetailDtos || [];
                this.isDefaultInitial = isDefaultInitial;
            }
        }

        export class BusinessTypeModel {
            dailyPerformanceFormatCode: string;
            dailyPerformanceFormatName: string;
            constructor(data: IDailyPerformanceFormatType) {
                let self = this;
                if (!data) return;
                self.dailyPerformanceFormatCode = data.dailyPerformanceFormatCode || "";
                self.dailyPerformanceFormatName = data.dailyPerformanceFormatName || "";
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

        export class DailyAttendanceAuthorityDetailDto {
            attendanceItemId: number = 0;
            dislayNumber: number = 0;
            attendanceItemName: string = '';
            order: number = 0;
            columnWidth: number = 0;
            constructor(data: IAuthorityFormatDetail) {
                if (!data) return;
                this.attendanceItemId = data.attendanceItemId || 0;
                this.dislayNumber = data.dislayNumber || 0;
                this.attendanceItemName = data.attendanceItemName || '';
                this.order = data.order || 0;
                this.columnWidth = data.columnWidth || 0;
            }
        }

        export class DailyAttendanceAuthorityDailyDto {
            sheetNo: number;
            sheetName: string;
            dailyAttendanceAuthorityDetailDtos: Array<DailyAttendanceAuthorityDetailDto>;
            constructor(sheetNo: number, sheetName: string, dailyAttendanceAuthorityDetailDtos: Array<DailyAttendanceAuthorityDetailDto>) {
                this.sheetNo = sheetNo || 0;
                this.sheetName = sheetName || "";
                this.dailyAttendanceAuthorityDetailDtos = dailyAttendanceAuthorityDetailDtos || [];
            }
        }

        export class AuthorityDetailModel {
            attendanceItemDtos: KnockoutObservableArray<AttendanceItemModel> = ko.observableArray([]);
            dailyAttendanceAuthorityDailyDto: KnockoutObservable<DailyAttendanceAuthorityDailyDto> = ko.observable(new DailyAttendanceAuthorityDailyDto(null, null, null));
            dailyAttendanceAuthorityMonthlyDto: KnockoutObservableArray<DailyAttendanceAuthorityDetailDto> = ko.observableArray([]);
            isDefaultInitial: number = 0;
            constructor(data: IDailyPerformanceFormatTypeDetail) {
                if (!data) return;
                this.attendanceItemDtos(data.attendanceItemDtos ? _.map(data.attendanceItemDtos, item => { return new AttendanceItemModel(item) }) : []);
                this.dailyAttendanceAuthorityDailyDto(data.dailyAttendanceAuthorityDailyDto ? new DailyAttendanceAuthorityDailyDto(data.dailyAttendanceAuthorityDailyDto.sheetNo, data.dailyAttendanceAuthorityDailyDto.sheetName, data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos) : null);
                this.dailyAttendanceAuthorityMonthlyDto(data.dailyAttendanceAuthorityMonthlyDto ? _.map(data.dailyAttendanceAuthorityMonthlyDto, item => { return new DailyAttendanceAuthorityDetailDto(item) }) : []);
                this.isDefaultInitial = data.isDefaultInitial;
            }
        }

        export interface IDailyPerformanceFormatTypeDetail {
            attendanceItemDtos: Array<IAttendanceItem>;
            dailyAttendanceAuthorityDailyDto: IAuthorityFormatDaily;
            dailyAttendanceAuthorityMonthlyDto: Array<IAuthorityFormatDetail>;
            isDefaultInitial: number;
        }

        export interface IDailyPerformanceFormatType {
            dailyPerformanceFormatCode: string;
            dailyPerformanceFormatName: string;
        }
        export interface IAttendanceItem {
            attendanceItemId: number;
            attendanceItemName: string;
            attendanceItemDisplayNumber: number;
        }
        export interface IAuthorityFormatDetail {
            attendanceItemId: number;
            dislayNumber: number;
            attendanceItemName: string;
            order: number;
            columnWidth: number;
        }
        export interface IAuthorityFormatDaily {
            sheetNo: number;
            sheetName: string;
            dailyAttendanceAuthorityDetailDtos: Array<IAuthorityFormatDetail>;
        }

    }
}
