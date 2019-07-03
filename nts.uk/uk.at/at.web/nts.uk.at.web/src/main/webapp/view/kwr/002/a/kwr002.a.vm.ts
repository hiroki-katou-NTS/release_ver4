module nts.uk.com.view.kwr002.a {
    import blockUI = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import getMsg = nts.uk.resource.getMessage;
    import isNullOrUndefined = nts.uk.util.isNullOrUndefined;

    export module viewModel {
        export class ScreenModel {
            ccgcomponent: GroupOption;
            exportDto: KnockoutObservable<ExportDto>;
            enableSave: KnockoutObservable<boolean>;
            baseDate: KnockoutObservable<Date>;
            periodStartDate: KnockoutObservable<moment.Moment>;
            employeeList: KnockoutObservableArray<UnitModel>;
            listEmployee: KnockoutObservableArray<Employee>;
            //            alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
            //            listComponentOption: any;
            //            optionalColumnDatasource: KnockoutObservableArray<any>;

            //            enChange: KnockoutObservable<boolean>;

            columns: KnockoutObservableArray<any>;
            currentCode: KnockoutObservable<any>;
            currentCodeList: KnockoutObservableArray<string>;

            enable: KnockoutObservable<boolean>;
            dateValue: KnockoutObservable<any>;
            startDateString: KnockoutObservable<string>;
            endDateString: KnockoutObservable<string>;

            attendanceRecordList: KnockoutObservableArray<AttendanceRecordExportSettingDto>;
            selectedCode: KnockoutObservable<string>;

            selectedEmployee: KnockoutObservableArray<any>;
            employeeName: KnockoutObservable<string>;

            permission: KnockoutObservable<boolean>;

            lstPersonComponentOption: ComponentOption;

            selectedEmployeeCode: KnockoutObservableArray<string>;
            alreadySettingPersonal: KnockoutObservableArray<UnitAlreadySettingModel>;

            comboboxName: string;

            closureId: KnockoutObservable<number>;
            
            constructor() {
                let self = this;
                self.comboboxName = nts.uk.resource.getText("KWR002_19");
                //let currentDate = self.getCurrentDay(new Date());
                self.enable = ko.observable(true);
                self.permission = ko.observable(true);
                self.startDateString = ko.observable("");
                self.endDateString = ko.observable("");
                self.dateValue = ko.observable("");
                self.exportDto = ko.observable<ExportDto>();
                self.listEmployee = ko.observableArray<Employee>([]);
                self.startDateString.subscribe(function(value) {
                    self.dateValue().startDate = value;
                    self.dateValue.valueHasMutated();
                });

                self.endDateString.subscribe(function(value) {
                    self.dateValue().endDate = value;
                    self.dateValue.valueHasMutated();
                });

                self.attendanceRecordList = ko.observableArray([]);

                self.selectedCode = ko.observable(null);

                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText("KWR002_13"), key: 'id', width: 140, hidden: true },
                    { headerText: nts.uk.resource.getText("KWR002_13"), key: 'code', width: 140 },
                    { headerText: nts.uk.resource.getText("KWR002_14"), key: 'name', width: 200 },
                    { headerText: nts.uk.resource.getText("KWR002_15"), key: 'workplaceName', width: 150 }
                ]);

                self.currentCode = ko.observable();
                self.currentCodeList = ko.observableArray([]);
                self.selectedEmployee = ko.observableArray([]);

                self.baseDate = ko.observable(new Date());
                self.periodStartDate = ko.observable(moment());
                self.employeeList = ko.observableArray<UnitModel>([]);
                //                self.alreadySettingList = ko.observableArray([]);
                self.applyKCP005ContentSearch([]);
                //                self.optionalColumnDatasource = ko.observableArray([]);

                //                self.enableSystemChange = ko.observable(false);

                self.enableSave = ko.observable(true);

                self.employeeName = ko.observable('');

                self.selectedEmployeeCode = ko.observableArray([]);
                self.alreadySettingPersonal = ko.observableArray([]);

                self.ccgcomponent = {

                    tabindex: -1,
                    /** Common properties */
                    systemType: 1, // システム区分
                    showEmployeeSelection: false, // 検索タイプ
                    showQuickSearchTab: true, // クイック検索
                    showAdvancedSearchTab: true, // 詳細検索
                    showBaseDate: false, // 基準日利用
                    showClosure: true, // 就業締め日利用
                    showAllClosure: true, // 全締め表示
                    showPeriod: true, // 対象期間利用
                    periodFormatYM: true, // 対象期間精度

                    /** Required parameter */
                    baseDate: self.baseDate().toISOString(), // 基準日
//                    periodStartDate: self.dateValue().startDate, // 対象期間開始日
//                    periodEndDate: self.dateValue().endDate, // 対象期間終了日
                    dateRangePickerValue: self.dateValue,
                    inService: true, // 在職区分
                    leaveOfAbsence: true, // 休職区分
                    closed: true, // 休業区分
                    retirement: true, // 退職区分

                    /** Quick search tab options */
                    showAllReferableEmployee: true, // 参照可能な社員すべて
                    showOnlyMe: true, // 自分だけ
                    showSameWorkplace: true, // 同じ職場の社員
                    showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

                    /** Advanced search properties */
                    showEmployment: true, // 雇用条件
                    showWorkplace: true, // 職場条件
                    showClassification: true, // 分類条件
                    showJobTitle: true, // 職位条件
                    showWorktype: false, // 勤種条件
                    isMutipleCheck: true, // 選択モード                   

                    /** Return data */
                    returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                        //画面項目「A7_3」を更新する
                        self.dateValue().startDate = moment(data.periodStart).format("YYYY/MM/DD");
                        self.dateValue().endDate = moment(data.periodEnd).format("YYYY/MM/DD");
                        self.dateValue.valueHasMutated();
                        //                        self.selectedEmployee(data.listEmployee);
                        self.applyKCP005ContentSearch(data.listEmployee);
                        self.listEmployee(data.listEmployee);
                        self.closureId(data.closureId); 
                    }
                }

                // self.currentCodeList.subscribe(function (employeeId: any) {
                //     let employee: any;
                //     self.selectedEmployee.removeAll();
                //     for (let i of employeeId) {
                //         employee = self.findByIdEmployee(i);
                //         self.selectedEmployee.push(employee);
                //     }
                // });
                
                self.closureId = ko.observable(0);
            }

            public start_page(): JQueryPromise<any> {

                blockUI.invisible();
                let self = this;
                let dfd = $.Deferred();

                // Start component
                $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent).done(() => {
                    self.employeeList = ko.observableArray<UnitModel>([]);
                    self.applyKCP005ContentSearch([]);
                    // Load employee list component
                    $('#employeeSearch').ntsListComponent(self.lstPersonComponentOption).done(function() {
                        $(".icon.icon-searchbox").remove();
                        $(".caret-right.caret-background.bg-green").removeClass();

                    }).then(() => {
                        setTimeout(() => {
                            $(".nts-searchbbox-wrapper").hide();
                        }, 200);

                    });
                });
                service.getPermission().done((result) => {
                    if (result == true) {
                        self.permission(false);
                    }
                });
                service.getAllAttendanceRecExpSet().done(function(listAttendance: Array<AttendanceRecordExportSettingDto>) {
                    if (listAttendance === undefined || listAttendance.length == 0) {
                        self.attendanceRecordList();
                        self.enableSave(false);
                        // $('#print').attr("disabled", "disabled")
                        // $('#exportExcel').attr("disabled", "disabled")
                    } else {
                        self.enableSave(true);
                        var sortArray = _.orderBy(listAttendance, [e => Number(e.code)], ['asc']);
                        _.map(sortArray, (item) => {
                            item.code = _.padStart(item.code, 2, '0');
                        });
                        self.attendanceRecordList(sortArray);
                        self.selectedCode(sortArray[0].code);
                    }

                    dfd.resolve();
                });
                service.getClosureMonth().done(function(dto) {
                    const startMonth = dto.currentMonth;
                    const endMonth = dto.currentMonth;
                    const parsedStart = startMonth.slice(0, 4) + '/' + startMonth.slice(4);
                    const parsedEnd = endMonth.slice(0, 4) + '/' + endMonth.slice(4);
                    self.dateValue({
                        startDate : parsedStart,
                        endDate : parsedEnd
                    })
                    self.dateValue.valueHasMutated();
                });

                blockUI.clear();
                return dfd.promise();
            }

            public getCurrentDay(date: Date) {
                let month = date.getMonth() + 1;
                let year = date.getFullYear();
                return year + "/" + month;
            }

            /**
             * apply ccg001 search data to kcp005
             */
            public applyKCP005ContentSearch(dataList: Employee[]): void {
                let self = this;
                self.employeeList([]);
                let employeeSearchs: UnitModel[] = [];
                for (let employeeSearch of dataList) {
                    let employee: UnitModel = {
                        id: employeeSearch.employeeId,
                        code: employeeSearch.employeeCode,
                        name: employeeSearch.employeeName,
                        workplaceName: employeeSearch.workplaceName
                    };
                    employeeSearchs.push(employee);
                }
                self.employeeList(employeeSearchs);
                self.lstPersonComponentOption = {
                    tabindex: 3,
                    isShowAlreadySet: false,
                    isMultiSelect: true,
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.SELECT_ALL,
                    selectedCode: self.selectedEmployeeCode,
                    isDialog: false,
                    isShowNoSelectRow: false,
                    alreadySettingList: self.alreadySettingPersonal,
                    isShowWorkPlaceName: true,
                    isShowSelectAllButton: false,
                    isSelectAllAfterReload: true,
                    maxWidth: 480,
                    maxRows: 12
                };
            }


            /**
             * find employee code in selected
             */
            public findEmployeeCodeById(employeeId: string): string {
                let self = this;
                let employeeCode = '';
                for (let employee of self.selectedEmployee()) {
                    if (employee.employeeId === employeeId) {
                        employeeCode = employee.employeeCode;
                    }
                }
                return employeeCode;
            }

            /**
             * get all employee id by search data CCG out put
             */
            // public getAllEmployeeIdBySearch(): string[] {
            //     let self = this;
            //     let employeeIds: string[] = [];
            //     for (let employeeSelect of self.employeeList()) {
            //         employeeIds.push(self.findEmployeeIdByCode(employeeSelect.code));
            //     }
            //     return employeeIds;
            // }

            /**
             * find employee id in selected
             */
            public findEmployeeByCode(employeeCode: string): Employee {
                let self = this;
                for (let employee of self.listEmployee()) {
                    if (employee.employeeCode === employeeCode) {
                        return employee;
                    }
                }
            }

            /**
             * update selected employee kcp005 => detail
             */
            public findByCodeEmployee(employeeCode: string): UnitModel {
                let employee: UnitModel;
                let self = this;
                for (let employeeSelect of self.employeeList()) {
                    if (employeeSelect.code === employeeCode) {
                        employee = employeeSelect;
                        break;
                    }
                }
                return employee;
            }


            /**
             * update selected employee kcp005 => detail
             */
            public findByIdEmployee(employeeId: string): Employee {
                let employee: Employee;
                let self = this;
                for (let employeeSelect of self.listEmployee()) {
                    if (employeeSelect.employeeId === employeeId) {
                        employee = employeeSelect;
                        break;
                    }
                }
                return employee;
            }

            /**
             * convert string to date
             */
            private toDate(strDate: string): Date {
                return moment(strDate, 'YYYY/MM/DD').toDate();
            }

            public print() {
                // mode = 1 for export file excel
                let self = this;
                console.log(self.currentCodeList());
                if (self.selectedEmployeeCode().length <= 0) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_1310" });
                    return;
                }
                nts.uk.ui.block.grayout();
                self.exportDto(new ExportDto(self.findEmployeeIdsByCodes(self.selectedEmployeeCode()), self.toDate(self.dateValue().startDate), self.toDate(self.dateValue().endDate), self.selectedCode(), 1, self.closureId()));
                service.exportService(self.exportDto()).done((response) => {
                    if (response.taskDatas.length > 0) {
                        nts.uk.ui.dialog.error({ messageId: "Msg_1269", messageParams: [response.taskDatas[0].valueAsString] });
                    }
                    nts.uk.ui.block.clear();
                }).fail((res: any) => {
                    nts.uk.ui.block.clear();
                    nts.uk.ui.dialog.error(res);
                    /*if (res.message !== "" && res.businessException) {
                        nts.uk.ui.dialog.caution(res).then(() => {
                            nts.uk.ui.dialog.error({ messageId: 'Msg_37' });
                        });
                    } else {
                        nts.uk.ui.dialog.error({ messageId: "Msg_37" });

                    }*/

                });
            }

            public exportExcel() {
                // mode = 2 for export file excel
                let self = this;

                if (self.selectedEmployeeCode().length <= 0) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_1310" });
                    return;
                }
                nts.uk.ui.block.grayout();
                self.exportDto(new ExportDto(self.findEmployeeIdsByCodes(self.selectedEmployeeCode()), self.toDate(self.dateValue().startDate), self.toDate(self.dateValue().endDate), self.selectedCode(), 2, self.closureId()));self.exportDto(new ExportDto(self.findEmployeeIdsByCodes(self.selectedEmployeeCode()), self.toDate(self.dateValue().startDate), self.toDate(self.dateValue().endDate), self.selectedCode(), 2));
                service.exportService(self.exportDto()).done((response) => {
                    if (response.taskDatas.length > 0) {
                        nts.uk.ui.dialog.error({ messageId: "Msg_1269", messageParams: [response.taskDatas[0].valueAsString] });
                    }
                    nts.uk.ui.block.clear();
                }).fail((res: any) => {
                    nts.uk.ui.block.clear();
                    nts.uk.ui.dialog.error(res);
                    /*if (res.message !== "" && res.businessException) {
                        nts.uk.ui.dialog.caution(res).then(() => {
                            nts.uk.ui.dialog.error({ messageId: 'Msg_37' });
                        });


                    } else {
                        nts.uk.ui.dialog.error({ messageId: "Msg_37" });

                    }*/


                });
            }


            private findEmployeeIdsByCodes(employeeCodes: string[]): Employee[] {
                let self = this;
                let employeeIds: string[] = [];
                let employee: Employee[] = [];
                for (let employeeCode of employeeCodes) {
                    let emp = self.findEmployeeByCode(employeeCode);
                    if (isNullOrUndefined(emp)) continue;
                    employee.push(emp);
                }
                return employee;
            }

            /**
             * showMessageError
             */
            public showMessageError(res: any) {
                let dfd = $.Deferred<any>();

                // check error business exception
                if (!res.businessException) {
                    return;
                }

                nts.uk.ui.dialog.bundledErrors(res);
                // show error message
                //                if (Array.isArray(res.errors)) {
                //                    nts.uk.ui.dialog.bundledErrors(res);
                //                } else {
                //                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                //                }
            }

            public openBDialog(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                blockUI.invisible();
                nts.uk.ui.windows.sub.modal("/view/kwr/002/b/index.xhtml").onClosed(function() {

                    service.getAllAttendanceRecExpSet().done(function(listAttendance: Array<AttendanceRecordExportSettingDto>) {
                        if (listAttendance === undefined || listAttendance.length == 0) {
                            self.attendanceRecordList(null);
                            self.selectedCode(null);
                            self.enableSave(false)
                            // $('#print').attr("disabled", "disabled")
                            // $('#exportExcel').attr("disabled", "disabled")
                        } else {
                            let ARESCode = getShared("currentARESCode");
                            var sortArray = _.orderBy(listAttendance, [e => Number(e.code)], ['asc']);
                            _.map(sortArray, (item) => {
                                item.code = _.padStart(item.code, 2, '0');
                            });
                            self.attendanceRecordList(sortArray);
                            if (ARESCode === undefined) {
                                self.selectedCode(sortArray[0].code);
                            }
                            else {
                                self.selectedCode(ARESCode);
                            }
                            self.enableSave(true)
                        }
                        // console.log(self.attendanceRecordList());

                        dfd.resolve();

                    });
                });
                nts.uk.ui.block.clear();

                return dfd.promise();
            }
        }

        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        export class SelectType {
            static SELECT_BY_SELECTED_CODE = 1;
            static SELECT_ALL = 2;
            static SELECT_FIRST_ITEM = 3;
            static NO_SELECT = 4;
        }

        export class AttendanceRecordExportSettingDto {
            code: string;
            name: string;

            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }

        export interface GroupOption {
            /** Common properties */
            showEmployeeSelection: boolean; // 検索タイプ
            systemType: number; // システム区分
            showQuickSearchTab: boolean; // クイック検索
            showAdvancedSearchTab: boolean; // 詳細検索
            showBaseDate: boolean; // 基準日利用
            showClosure: boolean; // 就業締め日利用
            showAllClosure: boolean; // 全締め表示
            showPeriod: boolean; // 対象期間利用
            periodFormatYM: boolean; // 対象期間精度

            /** Required parameter */
            baseDate?: string; // 基準日
            periodStartDate?: string; // 対象期間開始日
            periodEndDate?: string; // 対象期間終了日
            inService: boolean; // 在職区分
            leaveOfAbsence: boolean; // 休職区分
            closed: boolean; // 休業区分
            retirement: boolean; // 退職区分

            /** Quick search tab options */
            showAllReferableEmployee: boolean; // 参照可能な社員すべて
            showOnlyMe: boolean; // 自分だけ
            showSameWorkplace: boolean; // 同じ職場の社員
            showSameWorkplaceAndChild: boolean; // 同じ職場とその配下の社員

            /** Advanced search properties */
            showEmployment: boolean; // 雇用条件
            showWorkplace: boolean; // 職場条件
            showClassification: boolean; // 分類条件
            showJobTitle: boolean; // 職位条件
            showWorktype: boolean; // 勤種条件
            isMutipleCheck: boolean; // 選択モード
            // showDepartment: boolean; // 部門条件 not covered
            // showDelivery: boolean; not covered

            /** Data returned */
            returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
        }

        export interface Ccg001ReturnedData {
            baseDate: string; // 基準日
            closureId?: number; // 締めID
            periodStart: string; // 対象期間（開始)
            periodEnd: string; // 対象期間（終了）
            listEmployee: Array<Employee>; // 検索結果
        }

        export class Employee {
            employeeId: string;
            employeeCode: string;
            employeeName: string;
            workplaceName: string;

            constructor(employeeId: string, employeeCode: string, employeeName: string, workplaceName: string) {
                this.employeeId = employeeId;
                this.employeeCode = employeeCode;
                this.employeeName = employeeName;
                this.workplaceName = workplaceName;
            }
        }

        export class ExportDto {
            employeeList: Array<Employee>;
            startDate: Date;
            endDate: Date;
            layout: string;
            mode: number;
            closureId: number;

            constructor(employeeList: Array<Employee>, startDate: Date, endDate: Date, layout: string, mode: number, closureId: number) {
                this.employeeList = employeeList;
                this.startDate = startDate;
                this.endDate = endDate;
                this.layout = layout;
                this.mode = mode;
                this.closureId = closureId;
            }
        }
    }
}