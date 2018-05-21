module nts.uk.at.view.kwr008.a {

    import Ccg001ReturnedData = nts.uk.com.view.ccg.share.ccg.service.model.Ccg001ReturnedData;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;
    import share = nts.uk.at.view.kwr008.share.model;

    export module viewmodel {
        export class ScreenModel {

            ccgcomponent: GroupOption;
            systemTypes: KnockoutObservableArray<any>;

            // Options
            isQuickSearchTab: KnockoutObservable<boolean>;
            isAdvancedSearchTab: KnockoutObservable<boolean>;
            isAllReferableEmployee: KnockoutObservable<boolean>;
            isOnlyMe: KnockoutObservable<boolean>;
            isEmployeeOfWorkplace: KnockoutObservable<boolean>;
            isEmployeeWorkplaceFollow: KnockoutObservable<boolean>;
            isMutipleCheck: KnockoutObservable<boolean>;
            showDepartment: KnockoutObservable<boolean>; // 部門条件
            isSelectAllEmployee: KnockoutObservable<boolean>;
            periodStartDate: KnockoutObservable<moment.Moment>;
            periodEndDate: KnockoutObservable<moment.Moment>;
            baseDate: KnockoutObservable<moment.Moment>;
            selectedEmployee: KnockoutObservableArray<EmployeeSearchDto>;
            showEmployment: KnockoutObservable<boolean>; // 雇用条件
            showWorkplace: KnockoutObservable<boolean>; // 職場条件
            showClassification: KnockoutObservable<boolean>; // 分類条件
            showJobTitle: KnockoutObservable<boolean>; // 職位条件
            showWorktype: KnockoutObservable<boolean>; // 勤種条件
            inService: KnockoutObservable<boolean>; // 在職区分
            leaveOfAbsence: KnockoutObservable<boolean>; // 休職区分
            closed: KnockoutObservable<boolean>; // 休業区分
            retirement: KnockoutObservable<boolean>; // 退職区分
            systemType: KnockoutObservable<number>;
            showClosure: KnockoutObservable<boolean>; // 就業締め日利用
            showBaseDate: KnockoutObservable<boolean>; // 基準日利用
            showAllClosure: KnockoutObservable<boolean>; // 全締め表示
            showPeriod: KnockoutObservable<boolean>; // 対象期間利用
            periodFormatYM: KnockoutObservable<boolean>; // 対象期間精度

            user: any = __viewContext.user;
            // Employee tab
            lstPersonComponentOption: any;
            selectedEmployeeCode: KnockoutObservableArray<string>;
            employeeName: KnockoutObservable<string>;
            employeeList: KnockoutObservableArray<UnitModel> = ko.observableArray([]);
            alreadySettingPersonal: KnockoutObservableArray<UnitAlreadySettingModel>;
            ccgcomponentPerson: GroupOption;

            permissionOfEmploymentForm : KnockoutObservable<model.PermissionOfEmploymentFormModel>
                = ko.observable(new model.PermissionOfEmploymentFormModel('', '', 4, false));
            // date
            date: KnockoutObservable<string>;
            maxDaysCumulationByEmp: KnockoutObservable<number>;
            periodDate: KnockoutObservable<any>;
            //A3
            dateValue: KnockoutObservable<any> = ko.observable({ startDate: '', endDate: '' });
            startDateString: KnockoutObservable<string> = ko.observable('');
            endDateString: KnockoutObservable<string> = ko.observable('');
            //A4
            outputItem: KnockoutObservableArray<share.ItemModel> = ko.observableArray([]);
            selectedOutputItem: KnockoutObservable<string> = ko.observable(null);

            //A6 
            breakPage: KnockoutObservableArray<share.EnumConstantDto> = ko.observableArray([]);
            selectedBreakPage: KnockoutObservable<number> = ko.observable(null);

            constructor() {
                var self = this;

                // dump
                self.selectedEmployee = ko.observableArray([]);

                // initial ccg options
                self.setDefaultCcg001Option();

                // Init component.
                self.reloadCcg001();

                self.startDateString.subscribe(function(value){
                    self.dateValue().startDate = value;
                    self.dateValue.valueHasMutated();
                });

                self.endDateString.subscribe(function(value){
                    self.dateValue().endDate = value;
                    self.dateValue.valueHasMutated();
                });

                self.periodFormatYM.subscribe(item => {
                    if (item) {
                        self.showClosure(true);
                    }
                });

                self.selectedEmployeeCode = ko.observableArray([]);
                self.alreadySettingPersonal = ko.observableArray([]);
                self.maxDaysCumulationByEmp = ko.observable(0);
            }

            getOutItemSettingCode() {
                var self = this;
                self.outputItem([]);
                service.getOutItemSettingCode().done((dataArr: Array<share.OutputSettingCodeDto>) => {
                    _.forEach(dataArr, data => {
                        self.outputItem.push(new share.ItemModel(data.cd, data.name));
                    });
                });
            }
            checkInput(): boolean {
                var self = this;
                return self.dateValue().startDate && self.dateValue().endDate && self.selectedOutputItem();
            }

            exportReport() {
                var self = this;
                if (self.validate()) return;
                nts.uk.ui.block.invisible();
                //対象期間をチェックする
                if (moment(self.dateValue().startDate, 'YYYY/MM').add(12, 'M').toDate() <=
                    moment(self.dateValue().endDate, 'YYYYMM').toDate()) {
                    nts.uk.ui.dialog.alertError({messageId: 'Msg_883'});
                    nts.uk.ui.block.clear();
                    return;
                }
                //出力対象の社員をチェックする
                if (!self.selectedEmployeeCode().length) {
                    nts.uk.ui.dialog.alertError({messageId: 'Msg_884'});
                    nts.uk.ui.block.clear();
                    return;
                }
                var data = new model.EmployeeDto();
                data.startYearMonth   = self.dateValue().startDate;
                data.endYearMonth     = self.dateValue().endDate;
                data.setItemsOutputCd = self.selectedOutputItem();
                data.breakPage        = self.selectedBreakPage().toString();
                data.employees = [];
                for (var employeeCode of self.selectedEmployeeCode()) {
                    data.employees.push(self.findByCodeEmployee(employeeCode));
                }
                //ユーザ固有情報「年間勤務表（36チェックリスト）」を更新する
                self.saveOutputConditionAnnualWorkSchedule(new model.OutputConditionAnnualWorkScheduleChar(self.selectedOutputItem(), self.selectedBreakPage()));

                nts.uk.request.exportFile('at/function/annualworkschedule/export', data).done(() => {
                    
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }

            openKWR008B() {
                let self = this;
                nts.uk.ui.block.invisible();
                let param = {
                    selectedCd: self.selectedOutputItem()
                }
                nts.uk.ui.windows.setShared("KWR008_B_Param", param);
                nts.uk.ui.windows.sub.modal("at", "/view/kwr/008/b/index.xhtml").onClosed(() => {
                    //reload A4_2
                    self.getOutItemSettingCode();
                    let resultData = nts.uk.ui.windows.getShared("KWR008_B_Result");
                    if (!resultData) {
                        self.selectedOutputItem(null);
                        nts.uk.ui.block.clear();
                        return;
                    } else {
                        self.selectedOutputItem(resultData.selectedCd);
                    }
                });
            }

            /**
             * Set default ccg001 options
             */
            public setDefaultCcg001Option(): void {
                let self = this;
                self.isQuickSearchTab = ko.observable(true);
                self.isAdvancedSearchTab = ko.observable(true);
                self.isAllReferableEmployee = ko.observable(true);
                self.isOnlyMe = ko.observable(true);
                self.isEmployeeOfWorkplace = ko.observable(true);
                self.isEmployeeWorkplaceFollow = ko.observable(true);
                self.isMutipleCheck = ko.observable(true);
                self.showDepartment = ko.observable(false); // 部門条件
                self.isSelectAllEmployee = ko.observable(true); //社員リスト
                self.baseDate = ko.observable(moment());
                self.periodStartDate = ko.observable(moment());
                self.periodEndDate = ko.observable(moment());
                self.showEmployment = ko.observable(true); // 雇用条件
                self.showWorkplace = ko.observable(true); // 職場条件
                self.showClassification = ko.observable(true); // 分類条件
                self.showJobTitle = ko.observable(true); // 職位条件
                self.showWorktype = ko.observable(true); // 勤種条件
                self.inService = ko.observable(true); // 在職区分
                self.leaveOfAbsence = ko.observable(true); // 休職区分
                self.closed = ko.observable(true); // 休業区分
                self.retirement = ko.observable(false); // 退職区分
                self.systemType = ko.observable(2); //ok -
                self.showClosure = ko.observable(false); // 就業締め日利用
                self.showBaseDate = ko.observable(true); // 基準日利用
                self.showAllClosure = ko.observable(false); // 全締め表示
                self.showPeriod = ko.observable(false); // 対象期間利用
                self.periodFormatYM = ko.observable(false); // 対象期間精度
            }

            /**
             * Reload component CCG001
             */
            public reloadCcg001(): void {
                var self = this;
                var periodStartDate, periodEndDate: string;
                if (self.showBaseDate()) {
                    periodStartDate = moment(self.periodStartDate()).format("YYYY-MM-DD");
                    periodEndDate = moment(self.periodEndDate()).format("YYYY-MM-DD");
                } else {
                    periodStartDate = moment(self.periodStartDate()).format("YYYY-MM");
                    periodEndDate = moment(self.periodEndDate()).format("YYYY-MM"); // 対象期間終了日
                }

                if (!self.showBaseDate() && !self.showClosure() && !self.showPeriod()) {
                    nts.uk.ui.dialog.alertError("Base Date or Closure or Period must be shown!");
                    return;
                }
                self.ccgcomponent = {
                    /** Common properties */
                    systemType: self.systemType(), // システム区分
                    showEmployeeSelection: self.isSelectAllEmployee(), // 検索タイプ
                    showQuickSearchTab: self.isQuickSearchTab(), // クイック検索
                    showAdvancedSearchTab: self.isAdvancedSearchTab(), // 詳細検索
                    showBaseDate: self.showBaseDate(), // 基準日利用
                    showClosure: self.showClosure(), // 就業締め日利用
                    showAllClosure: self.showAllClosure(), // 全締め表示
                    showPeriod: self.showPeriod(), // 対象期間利用
                    periodFormatYM: self.periodFormatYM(), // 対象期間精度

                    /** Required parameter */
                    baseDate: moment(self.baseDate()).format("YYYY-MM-DD"), // 基準日
                    periodStartDate: periodStartDate, // 対象期間開始日
                    periodEndDate: periodEndDate, // 対象期間終了日
                    inService: self.inService(), // 在職区分
                    leaveOfAbsence: self.leaveOfAbsence(), // 休職区分
                    closed: self.closed(), // 休業区分
                    retirement: self.retirement(), // 退職区分

                    /** Quick search tab options */
                    showAllReferableEmployee: self.isAllReferableEmployee(), // 参照可能な社員すべて
                    showOnlyMe: self.isOnlyMe(), // 自分だけ
                    showSameWorkplace: self.isEmployeeOfWorkplace(), // 同じ職場の社員
                    showSameWorkplaceAndChild: self.isEmployeeWorkplaceFollow(), // 同じ職場とその配下の社員

                    /** Advanced search properties */
                    showEmployment: self.showEmployment(), // 雇用条件
                    showDepartment: self.showDepartment(), // 部門条件 not covered
                    showWorkplace: self.showWorkplace(), // 職場条件
                    showClassification: self.showClassification(), // 分類条件
                    showJobTitle: self.showJobTitle(), // 職位条件
                    showWorktype: self.showWorktype(), // 勤種条件
                    isMutipleCheck: self.isMutipleCheck(), // 選択モード

                    /** Return data */
                    returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                        self.selectedEmployee(data.listEmployee);
                        self.applyKCP005ContentSearch(data.listEmployee);
                    }
                }
                //$('#ccgcomponent').ntsGroupComponent(self.ccgcomponent);
            }

            /**
           * start page data 
           */
            public startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();

                var getPermissionOfEmploymentForm = service.getPermissionOfEmploymentForm().done((permission: any) => {
                    self.permissionOfEmploymentForm(new model.PermissionOfEmploymentFormModel(
                            permission.companyId,
                            permission.roleId,
                            permission.functionNo,
                            permission.availability));
                });
                //A3
                var getPeriod = service.getPeriod().done((data) => {
                    if (data) {
                        self.startDateString(data.startYearMonth);
                        self.endDateString(data.endYearMonth);
                    }
                });

                //A4
                self.getOutItemSettingCode();
                // A6
                var restoreOutputConditionAnnualWorkSchedule
                    = self.restoreOutputConditionAnnualWorkSchedule()
                    .done((data: model.OutputConditionAnnualWorkScheduleChar) => {
                        if (data) {
                            self.selectedOutputItem(data.setItemsOutputCd);
                            self.selectedBreakPage(data.breakPage);
                        } else if (self.outputItem().length) {
                            self.selectedOutputItem(self.outputItem()[0].code);
                        }
                        if (!self.outputItem().length) {
                            self.selectedOutputItem(null);
                        }
                    });

                var getPageBreakSelection = service.getPageBreakSelection().done((enumRes)=>{
                    self.breakPage(enumRes);
                }).fail((enumError)=>{
                    console.log(`fail : ${enumError}`);
                });

                $.when(getPermissionOfEmploymentForm, getPeriod,
                       restoreOutputConditionAnnualWorkSchedule,
                       getPageBreakSelection).done(() => {
                            dfd.resolve(self);
                       })
                return dfd.promise();
            }
            public validate(): boolean {
                $('#period').trigger('validate');
                $('#outputItem').trigger('validate');
                return nts.uk.ui.errors.hasError();
            }
            /**
            * apply ccg001 search data to kcp005
            */
            public applyKCP005ContentSearch(dataList: EmployeeSearchDto[]): void {
                var self = this;
                self.employeeList([]);
                var employeeSearchs: UnitModel[] = [];
                for (var employeeSearch of dataList) {
                    var employee: UnitModel = {
                        code: employeeSearch.employeeCode,
                        name: employeeSearch.employeeName,
                        workplaceName: employeeSearch.workplaceName
                    };
                    employeeSearchs.push(employee);
                }
                self.employeeList(employeeSearchs);
                self.lstPersonComponentOption = {
                    isShowAlreadySet: false,
                    isMultiSelect: true,
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.SELECT_BY_SELECTED_CODE,
                    selectedCode: self.selectedEmployeeCode,
                    isDialog: false,
                    isShowNoSelectRow: false,
                    alreadySettingList: self.alreadySettingPersonal,
                    isShowWorkPlaceName: true,
                    isShowSelectAllButton: true,
                    maxWidth: 550,
                    maxRows: 15
                };

            }

            /**
             * update selected employee kcp005 => detail
             */
            public findByCodeEmployee(employeeCode: string): UnitModel {
                var employee: UnitModel;
                var self = this;
                for (var employeeSelect of self.employeeList()) {
                    if (employeeSelect.code === employeeCode) {
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

            /**
             * save to client service PersonalSchedule by employeeId
            */
            private restoreOutputConditionAnnualWorkSchedule(): JQueryPromise<model.OutputConditionAnnualWorkScheduleChar> {
                var self = this;
                return nts.uk.characteristics.restore("OutputConditionAnnualWorkScheduleChar_" + self.user.employeeId);
            }
            /**
             * save to client service PersonalSchedule by employeeId
            */
            private saveOutputConditionAnnualWorkSchedule(data: model.OutputConditionAnnualWorkScheduleChar): void {
                var self = this;
                nts.uk.characteristics.save("OutputConditionAnnualWorkScheduleChar_" + self.user.employeeId, data);
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

        /** model */
        export module model {
            /**
             * Permission Of Employment Form model
             */
            export class PermissionOfEmploymentFormModel {
                companyId : string;
                roleId : string;
                functionNo : number;
                availability : KnockoutObservable<boolean> = ko.observable(false);
                constructor(companyId : string, roleId : string, functionNo : number, availability : boolean) {
                    let self = this;
                    self.companyId = companyId || '';
                    self.roleId = roleId || '';
                    self.functionNo = functionNo || 0;
                    self.availability(availability || false);
                }
            }

            export interface PeriodDto {
                startYearMonth: Date;
                endYearMonth: Date;
            }

            export class OutputConditionAnnualWorkScheduleChar {
                /** A4_2 定型選択 */
                setItemsOutputCd: string;
                /** A6_2 改頁選択 */
                breakPage: number;
                constructor(setItemsOutputCd: string, breakPage: number) {
                    this.setItemsOutputCd = setItemsOutputCd;
                    this.breakPage = breakPage;
                }
            }

            export class EmployeeDto {
                employees: Array<UnitModel>;
                startYearMonth: string;
                endYearMonth: string;
                setItemsOutputCd: string;
                breakPage: string;
                constructor() {}
            }
        }
    }
}