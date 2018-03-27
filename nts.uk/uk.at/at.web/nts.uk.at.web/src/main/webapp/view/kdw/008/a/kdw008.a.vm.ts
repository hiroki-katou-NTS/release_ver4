module nts.uk.at.view.kdw008.a {
    export module viewmodel {
        export class ScreenModel {

            newMode: KnockoutObservable<boolean>;
            isUpdate: KnockoutObservable<boolean>;
            isRemove: KnockoutObservable<boolean>;
            showCode: KnockoutObservable<boolean>;

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

            //swap list tab 2
            monthlyDetailList: KnockoutObservableArray<DailyAttendanceAuthorityDetailDto>;
            columns3: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            currentCodeListSwapMonthly: KnockoutObservableArray<any>;
            authorityFormatMonthlyValue: KnockoutObservableArray<AttendanceItemModel>;
            monthlyDataSource: KnockoutObservableArray<AttendanceItemModel>;

            //swap list tab 1
            currentCodeListSwap2: KnockoutObservableArray<any>;
            authorityFormatDailyValue: KnockoutObservableArray<AttendanceItemModel>;
            columns2: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            selectedSheetName: KnockoutObservable<string>;
            dailyDataSource: KnockoutObservableArray<AttendanceItemModel>;

            tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
            selectedTab: KnockoutObservable<string>;

            constructor() {
                var self = this;
                self.newMode = ko.observable(false);
                self.isUpdate = ko.observable(true);
                self.isRemove = ko.observable(true);
                self.showCode = ko.observable(false);

                self.checked = ko.observable(false);

                self.currentDailyFormatCode = ko.observable('');
                self.currentDailyFormatName = ko.observable('');

                self.selectedSheetName = ko.observable('');

                self.businessTypeList = ko.observableArray([]);
                self.currentBusinessType = ko.observable(new AuthorityDetailModel(null));

                self.columns1 = ko.observableArray([
                    { headerText: 'コード', key: 'dailyPerformanceFormatCode', width: 90 },
                    { headerText: '名称', key: 'dailyPerformanceFormatName', width: 120, formatter: _.escape }
                ]);
                this.selectedCode = ko.observable();

                self.columns3 = ko.observableArray([
                    { headerText: 'コード', key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: 'ID', key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: '名称', key: 'attendanceItemName', width: 100 }
                ]);
                self.columns2 = ko.observableArray([
                    { headerText: 'コード', key: 'attendanceItemDisplayNumber', width: 70 },
                    { headerText: 'ID', key: 'attendanceItemId', hidden: true, width: 100 },
                    { headerText: '名称', key: 'attendanceItemName', width: 100 }
                ]);

                //swap list 2
                self.monthlyDetailList = ko.observableArray([]);
                self.authorityFormatMonthlyValue = ko.observableArray([]);
                self.monthlyDataSource = ko.observableArray([]);
                var monthlySwapList = [];
                self.currentCodeListSwapMonthly = ko.observableArray(monthlySwapList);
                this.currentCodeListSwapMonthly.subscribe(function(value) {
                    console.log(value);
                });
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: '日次項目', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2a', title: 'tab2a', content: '.tab-content-2a', enable: ko.observable(true), visible: ko.observable(true) },
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
                    nts.uk.ui.errors.clearAll();
                    self.getDetail(self.selectedCode());
                });

                //swaplist 1
                var x = [];
                this.currentCodeListSwap2 = ko.observableArray(x);
                this.currentCodeListSwap2.subscribe(function(value) {
                    console.log(value);
                });
                self.authorityFormatDailyValue = ko.observableArray([]);
                self.dailyDataSource = ko.observableArray([]);

                self.selectedCode.subscribe(newValue => {
                    self.isUpdate(true);
                    self.showCode(false);
                    if (nts.uk.text.isNullOrEmpty(newValue)) return;
                    let empSelect = _.find(self.businessTypeList(), bus => {
                        return bus.dailyPerformanceFormatCode == newValue;
                    });
                    if (empSelect) {
                        self.currentDailyFormatCode(empSelect.dailyPerformanceFormatCode);
                        self.currentDailyFormatName(empSelect.dailyPerformanceFormatName);
                    }
                    self.isRemove(true);
                    nts.uk.ui.errors.clearAll();
                    self.getDetail(self.currentDailyFormatCode(), 1);
                    //                    self.selectedTab('tab-1');
                });

            }

            startPage(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                nts.uk.ui.block.grayout();
                self.businessTypeList([]);
                new service.Service().getBusinessType().done(function(data: Array<IDailyPerformanceFormatType>) {
                    if (data && data.length > 0) {
                        self.businessTypeList(_.map(data, item => { return new BusinessTypeModel(item) }));
                        self.currentDailyFormatCode(self.businessTypeList()[0].dailyPerformanceFormatCode);
                        self.currentDailyFormatName(self.businessTypeList()[0].dailyPerformanceFormatName);
                        self.selectedCode(self.businessTypeList()[0].dailyPerformanceFormatCode);
                        self.getDetail(self.businessTypeList()[0].dailyPerformanceFormatCode);
                        nts.uk.ui.block.clear();
                        dfd.resolve();
                    } else {
                        nts.uk.ui.dialog.alert({ messageId: "Msg_242" });
                        nts.uk.ui.block.clear();
                        dfd.resolve();
                        self.setNewMode();
                    }
                });

                return dfd.promise();
            }

            setNewMode() {
                let self = this;
                self.newMode(true);
                self.currentDailyFormatCode(null);
                self.currentDailyFormatName('');
                self.selectedCode(null);
                self.getDetail(self.selectedCode()).done(() => {
                    _.defer(() => { $("#currentCode").focus(); });
                });
                self.checked(false);
                self.showCode(true);
                self.isUpdate(false);
                self.isRemove(false);
                nts.uk.ui.errors.clearAll();
            }

            getDetail(dailyPerformanceFormatCode: string, selectedSheetNo?: number) {
                let self = this,
                    dfd = $.Deferred();
                if (selectedSheetNo) {
                    self.selectedSheetNo(selectedSheetNo);
                }
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
                        var dailyDataSource = _.map(self.currentBusinessType().attendanceItemDtos(), item => {
                            var obj = {
                                attendanceItemId: item.attendanceItemId,
                                attendanceItemName: item.attendanceItemName,
                                attendanceItemDisplayNumber: item.attendanceItemDisplayNumber,
                                columnWidth: item.columnWidth
                            }
                            return new AttendanceItemModel(obj);
                        })
                        self.dailyDataSource(_.clone(dailyDataSource));
                        self.dailyDataSource.valueHasMutated();
                        data.dailyAttendanceAuthorityMonthlyDto = _.sortBy(data.dailyAttendanceAuthorityMonthlyDto, ["order"]);
                        if (data.dailyAttendanceAuthorityMonthlyDto) {
                            var attendanceItemModelMonthly = _.map(data.dailyAttendanceAuthorityMonthlyDto, item => {
                                var obj = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber,
                                    columnWidth: item.columnWidth
                                };
                                return new AttendanceItemModel(obj);
                            });
                            self.authorityFormatMonthlyValue(attendanceItemModelMonthly);

                        } else self.authorityFormatMonthlyValue([]);
                        //show data tab 1
                        //self.selectedSheetNo(data.businessTypeFormatDailyDto.sheetNo);
                        self.selectedSheetName(data.dailyAttendanceAuthorityDailyDto.sheetName);
                        self.currentBusinessType().attendanceItemDtos.valueHasMutated();
                        self.monthlyDataSource(_.clone(dailyDataSource));
                        self.monthlyDataSource.valueHasMutated();
                        if (data.dailyAttendanceAuthorityDailyDto != null && data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos) {
                            data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos = _.sortBy(data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos, ["order"]);
                            var attendanceItemModelDaily = _.map(data.dailyAttendanceAuthorityDailyDto.dailyAttendanceAuthorityDetailDtos, item => {
                                var daily = {
                                    attendanceItemId: item.attendanceItemId,
                                    attendanceItemName: item.attendanceItemName,
                                    attendanceItemDisplayNumber: item.dislayNumber,
                                    columnWidth: item.columnWidth
                                }
                                return new AttendanceItemModel(daily);
                            });
                            self.authorityFormatDailyValue(attendanceItemModelDaily);
                        } else {
                            self.authorityFormatDailyValue([]);
                        }
                        if (!self.newMode()) {
                            $("#currentName").focus();
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
                        new service.Service().removeAuthorityDailyFormat(removeAuthorityDto).done(function() {
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                            $("#currentName").focus();
                        }).fail(function(error) {
                        });;
                        self.reloadData(removeAuthorityDto.dailyPerformanceFormatCode, true);
                    });
            }

            addOrUpdateClick() {
                let self = this;
                $("#currentName").trigger("validate");
                if (!nts.uk.ui.errors.hasError()) {
                    self.register();
                }
            }

            register() {
                let self = this;
                $(".need-check").trigger("validate");
                if (!nts.uk.ui.errors.hasError()) {
                    //add or update Monthly
                    var authorityFormatDetailDtos = _.map(self.authorityFormatMonthlyValue(), item => {
                        var indexOfItem = _.findIndex(self.authorityFormatMonthlyValue(), { attendanceItemId: item.attendanceItemId });
                        var obj = {
                            attendanceItemId: item.attendanceItemId,
                            dislayNumber: item.attendanceItemDisplayNumber,
                            attendanceItemName: item.attendanceItemName,
                            order: indexOfItem,
                            columnWidth: item.columnWidth ? item.columnWidth : null
                        };
                        return new DailyAttendanceAuthorityDetailDto(obj);
                    })
                    var addOrUpdateBusinessFormatMonthly = new AddAuthorityFormatMonthly(self.currentDailyFormatCode(), authorityFormatDetailDtos);

                    //add or update Daily
                    var businessTypeFormatDetailDailyDto = _.map(self.authorityFormatDailyValue(), item => {
                        var indexOfDaily = _.findIndex(self.authorityFormatDailyValue(), { attendanceItemId: item.attendanceItemId });
                        var monthly = {
                            attendanceItemId: item.attendanceItemId,
                            dislayNumber: item.attendanceItemDisplayNumber,
                            attendanceItemName: item.attendanceItemName,
                            order: indexOfDaily,
                            columnWidth: item.columnWidth ? item.columnWidth : null
                        };
                        return new DailyAttendanceAuthorityDetailDto(monthly);
                    });

                    if (self.checked() == true) {
                        var addOrUpdateBusinessFormatDaily = new AddAuthorityFormatDaily(self.currentDailyFormatCode(), self.currentDailyFormatName(), self.selectedSheetNo(), self.selectedSheetName(), businessTypeFormatDetailDailyDto, 1);
                    } else {
                        var addOrUpdateBusinessFormatDaily = new AddAuthorityFormatDaily(self.currentDailyFormatCode(), self.currentDailyFormatName(), self.selectedSheetNo(), self.selectedSheetName(), businessTypeFormatDetailDailyDto, 0);
                    }

                    var addOrUpdateDailyFormat = new AddOrUpdateDailyFormat(addOrUpdateBusinessFormatMonthly, addOrUpdateBusinessFormatDaily);
                    nts.uk.ui.block.invisible();
                    if (self.isUpdate() == true) {
                        new service.Service().updateDailyDetail(addOrUpdateDailyFormat).done(function() {
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(()=>{
                               self.reloadData(self.currentDailyFormatCode()); 
                            });
                            $("#currentName").focus();
                        }).always(function() {
                            nts.uk.ui.block.clear();
                        }).fail(function(error) {
                            nts.uk.ui.dialog.alertError(error.message);
                        });
                    } else {
                        new service.Service().addDailyDetail(addOrUpdateDailyFormat).done(function() {
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(()=>{
                               self.reloadData(self.currentDailyFormatCode()); 
                            });
                            $("#currentName").focus();
                        }).always(function() {
                            nts.uk.ui.block.clear();
                        }).fail(function(error) {
                            //                        nts.uk.ui.dialog.alertError(error.message);
                            $('#currentCode').ntsError('set', error);
                        });
                    }
                    //self.getDetail(self.currentDailyFormatCode());
                }
            }

            reloadData(dailyPerformanceFormatCode: string, isRemove?: boolean) {
                let self = this,
                    dfd = $.Deferred();
                let oldSelectIndex = _.findIndex(self.businessTypeList(), item => { return item.dailyPerformanceFormatCode == dailyPerformanceFormatCode; });
                self.businessTypeList([]);
                new service.Service().getBusinessType().done(function(data: Array<IDailyPerformanceFormatType>) {
                    if (data && data.length > 0) {
                        data = _.orderBy(data, ["dailyPerformanceFormatCode"], ['asc']);
                        self.businessTypeList(_.map(data, item => { return new BusinessTypeModel(item) }));
                        self.currentDailyFormatCode(dailyPerformanceFormatCode);
                        //                        self.currentDailyFormatName(self.businessTypeList()[0].dailyPerformanceFormatName);
                        //                        self.selectedCode(dailyPerformanceFormatCode);
                        self.selectedSheetNo(1);
                        self.getDetail(dailyPerformanceFormatCode);
                        if (isRemove && isRemove == true) {
                            self.selectedCode(self.getNewSelectRemove(oldSelectIndex));
                        } else {
                            self.selectedCode(dailyPerformanceFormatCode);
                        }
                    } else {
                        self.setNewMode();
                    }
                    dfd.resolve();
                });

                return dfd.promise();
            }

            getNewSelectRemove(oldSelectIndex: number): String {
                let self = this;
                let dataLength = self.businessTypeList().length;
                if (dataLength == 1 || oldSelectIndex > dataLength) {
                    return self.businessTypeList()[0].dailyPerformanceFormatCode;
                }
                if (oldSelectIndex <= dataLength - 1) {
                    return self.businessTypeList()[oldSelectIndex].dailyPerformanceFormatCode;
                }
                if (oldSelectIndex == dataLength) {
                    return self.businessTypeList()[oldSelectIndex - 1].dailyPerformanceFormatCode;
                }
                return null;
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

        export class AddOrUpdateDailyFormat {
            authorityMonthlyCommand: KnockoutObservable<AddAuthorityFormatMonthly>;
            authorityDailyCommand: KnockoutObservable<AddAuthorityFormatDaily>;
            constructor(addOrUpdateAuthorityFormatMonthly: KnockoutObservable<AddAuthorityFormatMonthly>, addOrUpdateAuthorityFormatDaily: KnockoutObservable<AddAuthorityFormatDaily>) {
                let self = this;
                self.authorityMonthlyCommand = addOrUpdateAuthorityFormatMonthly;
                self.authorityDailyCommand = addOrUpdateAuthorityFormatDaily;
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
                this.sheetName = sheetName || null;
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
            columnWidth: number;
            constructor(data: IAttendanceItem) {
                if (!data) return;
                this.attendanceItemId = data.attendanceItemId || 0;
                this.attendanceItemName = data.attendanceItemName || "";
                this.attendanceItemDisplayNumber = data.attendanceItemDisplayNumber || 0;
                this.columnWidth = data.columnWidth;
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
                this.sheetName = sheetName || null;
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
            columnWidth: number;
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
