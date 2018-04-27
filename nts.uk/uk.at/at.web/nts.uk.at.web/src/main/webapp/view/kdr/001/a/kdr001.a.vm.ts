module nts.uk.at.view.kdr001.a.viewmodel {

    import ScheduleBatchCorrectSettingSave = service.model.ScheduleBatchCorrectSettingSave;
    import Ccg001ReturnedData = nts.uk.com.view.ccg.share.ccg.service.model.Ccg001ReturnedData;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;
    import getText = nts.uk.resource.getText;

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
        isSelectAllEmployee: KnockoutObservable<boolean>;
        periodStartDate: KnockoutObservable<moment.Moment>;
        periodEndDate: KnockoutObservable<moment.Moment>;
        baseDate: KnockoutObservable<moment.Moment>;
        lstSearchEmployee: KnockoutObservableArray<EmployeeSearchDto>;
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

        selectedImplementAtrCode: KnockoutObservable<number>;
        resetWorkingHours: KnockoutObservable<boolean>;
        resetDirectLineBounce: KnockoutObservable<boolean>;
        resetMasterInfo: KnockoutObservable<boolean>;
        resetTimeChildCare: KnockoutObservable<boolean>;
        resetAbsentHolidayBusines: KnockoutObservable<boolean>;
        resetTimeAssignment: KnockoutObservable<boolean>;

        periodDate: KnockoutObservable<any>;
        copyStartDate: KnockoutObservable<Date>;
        startDateString: KnockoutObservable<string>;
        endDateString: KnockoutObservable<string>;

        lstLabelInfomation: KnockoutObservableArray<string>;
        //            infoCreateMethod: KnockoutObservable<string>;
        infoPeriodDate: KnockoutObservable<string>;
        lengthEmployeeSelected: KnockoutObservable<string>;

        // Employee tab
        lstPersonComponentOption: any;
        selectedEmployeeCode: KnockoutObservableArray<string>;
        employeeName: KnockoutObservable<string>;
        employeeList: KnockoutObservableArray<UnitModel>;
        alreadySettingPersonal: KnockoutObservableArray<UnitAlreadySettingModel>;
        ccgcomponentPerson: GroupOption;

        // date
        date: KnockoutObservable<string>;

        //combo-box
        lstHolidayRemaining: KnockoutObservableArray<HolidayRemainingModel> = ko.observableArray([]) ;
        itemSelected: KnockoutObservableArray<ItemModel>;
        selectedCode: KnockoutObservable<string> = ko.observable('0');
        holidayRemainingSelectedCd: KnockoutObservable<string> = ko.observable('');
        
        permissionOfEmploymentForm : KnockoutObservable<PermissionOfEmploymentFormModel> 
                                        = ko.observable(new PermissionOfEmploymentFormModel(
                                            '', '', 1, true));
        constructor() {
            var self = this;

            self.date = ko.observable(moment());
            self.systemTypes = ko.observableArray([
                { name: 'システム管理者', value: 1 }, // PERSONAL_INFORMATION
                { name: '就業', value: 2 } // EMPLOYMENT
            ]);
            self.lstSearchEmployee = ko.observableArray([]);

            // initial ccg options
            self.setDefaultCcg001Option();

            

            self.periodFormatYM.subscribe(item => {
                if (item) {
                    self.showClosure(true);
                }
            });

            self.startDateString = ko.observable(moment());
            self.endDateString = ko.observable(moment());
            self.selectedEmployeeCode = ko.observableArray([]);
            self.alreadySettingPersonal = ko.observableArray([]);
            self.periodDate = ko.observable({
                startDate: self.startDateString(),
                endDate: self.endDateString()
            });

            self.resetWorkingHours = ko.observable(false);
            self.resetDirectLineBounce = ko.observable(false);
            self.resetMasterInfo = ko.observable(false);
            self.resetTimeChildCare = ko.observable(false);
            self.resetAbsentHolidayBusines = ko.observable(false);
            self.resetTimeAssignment = ko.observable(false);
            self.copyStartDate = ko.observable(new Date());
            
            self.startDateString.subscribe(function(value) {
                self.periodDate().startDate = value;
                self.periodDate.valueHasMutated();
                self.periodStartDate(moment(value));
                self.periodStartDate.valueHasMutated();
            });

            self.endDateString.subscribe(function(value) {
                self.periodDate().endDate = value;
                self.periodDate.valueHasMutated();
                self.periodEndDate(moment(value));
                self.periodEndDate.valueHasMutated();
            });

            //combo-box2
            self.itemSelected = ko.observableArray([
                new ItemModel('0', getText("Enum_BreakSelection_NONE")),
                new ItemModel('1', getText("Enum_BreakSelection_WORKPLACE")),
                new ItemModel('2', getText("Enum_BreakSelection_INDIVIDUAL"))
            ]);
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
            self.isSelectAllEmployee = ko.observable(false);
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
            self.retirement = ko.observable(true); // 退職区分
            self.systemType = ko.observable(2);
            self.showClosure = ko.observable(true); // 就業締め日利用
            self.showBaseDate = ko.observable(false); // 基準日利用
            self.showAllClosure = ko.observable(true); // 全締め表示
            self.showPeriod = ko.observable(true); // 対象期間利用
            self.periodFormatYM = ko.observable(true); // 対象期間精度
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
                showWorkplace: self.showWorkplace(), // 職場条件
                showClassification: self.showClassification(), // 分類条件
                showJobTitle: self.showJobTitle(), // 職位条件
                showWorktype: self.showWorktype(), // 勤種条件
                isMutipleCheck: self.isMutipleCheck(), // 選択モード

                /** Return data */
                returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                    self.lstSearchEmployee(data.listEmployee);
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
            let user: any = __viewContext.user;
            $.when(service.findAll(),
                    service.getDate(),
                    service.getPermissionOfEmploymentForm(),
                    nts.uk.characteristics.restore("UserSpecific_" + user.employeeId)
                    ).done((
                            holidayRemainings: Array<any>,
                            dateData: IGetDate,
                            permission: any,
                            userSpecific
                            ) => {
                    self.loadAllHolidayRemaining(holidayRemainings);
                    
                    let startDate = moment(dateData ? dateData.startDate || moment() : moment());
                    let endDate = moment(dateData ? dateData.endDate || moment() : moment());
                    //画面項目「A3_4：終了年月」にパラメータ「当月+１月」をセットする    
                    let nextMonth = moment(endDate).add(1, 'M');
                    endDate = nextMonth.format("YYYY/MM");

                    //画面項目「A3_2：開始年月」にパラメータ「当月」－1年した値をセットする
                    let preYear = moment(startDate).add(-1, 'Y');
                    startDate = preYear.format("YYYY/MM");
                    self.startDateString(moment.utc(startDate).format("YYYY/MM"));
                    self.endDateString(moment.utc(endDate).format("YYYY/MM"));
                        
                    self.permissionOfEmploymentForm(new PermissionOfEmploymentFormModel(
                            permission.companyId,
                            permission.roleId,
                            permission.functionNo,
                            permission.availability));
                    if (userSpecific) {
                        self.holidayRemainingSelectedCd(userSpecific.outputItemSettingCode);
                        self.selectedCode(userSpecific.pageBreakAtr);
                    }
                    // Init component.
                    self.reloadCcg001();
               }).fail(function(res) {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() { nts.uk.ui.block.clear(); });
            }).always(() => {
                nts.uk.ui.block.clear();
                dfd.resolve(self);
            });
            return dfd.promise();
        }
        
        /**
         * load and set item selected
         */
        loadAllHolidayRemaining(data: Array<HolidayRemainingModel>) {
            let self = this;
            if (data && data.length > 0) {
                data = _.sortBy(data, ['cd']);
                self.lstHolidayRemaining(data);
            }
            // no data
            else {
                self.lstHolidayRemaining([]);
                self.holidayRemainingSelectedCd('');
            }
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
         * function export excel button
         */
        private exportButton() {
            let self = this;
            $('.nts-input').trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return;
            }
            nts.uk.ui.block.invisible();
            
            let startMonth = moment.utc(self.startDateString());
            let endMonth = moment.utc(self.endDateString());
            let totalMonths = (parseInt(endMonth.format("YYYY"))*12 + parseInt(endMonth.format("MM")))
                             - (parseInt(startMonth.format("YYYY"))*12 + parseInt(startMonth.format("MM")));
            if (totalMonths < 0){
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_1217' });
                nts.uk.ui.block.clear();  
                return;
            }
            if (totalMonths > 12){
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_1173' });
                nts.uk.ui.block.clear();  
                return;
            }
            // get and build selected employee
            let lstSelectedEployee : Array<EmployeeSearchDto> = [];
            var index = -1;
            for (var i = 0; i < self.selectedEmployeeCode().length; i++) {
                index = _.findIndex(self.lstSearchEmployee(), function(x)
                { return x.employeeCode === self.selectedEmployeeCode()[i] });
                if (index > -1) {
                    lstSelectedEployee.push(self.lstSearchEmployee()[index]);
                }
            }
            if (!lstSelectedEployee || lstSelectedEployee.length === 0){
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_884' });
                nts.uk.ui.block.clear();  
                return;
            }
            
            let user: any = __viewContext.user;
            let userSpecificInformation = new UserSpecificInformation(
                user.employeeId,
                user.companyId,
                self.holidayRemainingSelectedCd(),
                self.selectedCode()
            );
            nts.uk.characteristics.save("UserSpecific_" + user.employeeId, userSpecificInformation);

            let holidayRemainingOutputCondition = new HolidayRemainingOutputCondition(
                startMonth.format("YYYY/MM"),
                endMonth.format("YYYY/MM"),
                self.holidayRemainingSelectedCd(),
                self.selectedCode()
            );

            let data = new ReportInfor(holidayRemainingOutputCondition, lstSelectedEployee);
            service.saveAsExcel(data).done(() => {
                nts.uk.ui.block.clear();
            }).fail(function(res: any) {
                nts.uk.ui.dialog.alertError(res.messageId);
                nts.uk.ui.block.clear();
            });
        }
        
        /**
         * Open dialog B
         */
        openKDR001b() {
            let self = this;
            nts.uk.ui.block.invisible();
            setShared('KDR001Params', self.holidayRemainingSelectedCd());
            modal("/view/kdr/001/b/index.xhtml").onClosed(function() {
                self.holidayRemainingSelectedCd(getShared('KDR001B2A_cd'));
                service.findAll().done(function(data: Array<any>) {
                    self.loadAllHolidayRemaining(data);
                    nts.uk.ui.block.clear();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() { nts.uk.ui.block.clear(); });
                    nts.uk.ui.block.clear();    
                });
            });
        }
    }

    export class HolidayRemainingModel {
        cd: string;
        name: string;

        constructor(code: string, name: string) {
            this.cd = code;
            this.name = name;
        }
    }

    export class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    
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
    // スケジュール一括修正設定
    export class ScheduleBatchCorrectSetting {
        // 勤務種類
        worktypeCode: string;

        // 社員ID
        employeeId: string;

        // 終了日
        endDate: string;

        // 開始日
        startDate: string;

        // 就業時間帯
        worktimeCode: string;

        constructor() {
            var self = this;
            self.worktypeCode = '';
            self.employeeId = '';
            self.endDate = '';
            self.startDate = '';
            self.worktimeCode = '';
        }
    }
    
    export interface IGetDate{
        startDate : string;
        endDate : string;
    }
    
    export class GetDate{
        startDate : string;
        endDate : string;
        constructor(startDate : string, endDate : string ){
            this.startDate = startDate;
            this.endDate = endDate;
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

    export interface EmployeeSearchDto {
        employeeId: string;

        employeeCode: string;

        employeeName: string;

        workplaceCode: string;

        workplaceId: string;

        workplaceName: string;
    }

    export interface GroupOption {
        baseDate?: KnockoutObservable<Date>;

        periodStartDate?: KnockoutObservable<Date>;

        periodEndDate?: KnockoutObservable<Date>;

        inService: boolean;

        leaveOfAbsence: boolean;

        closed: boolean;

        retirement: boolean;

        // クイック検索タブ
        isQuickSearchTab: boolean;
        // 参照可能な社員すべて
        isAllReferableEmployee: boolean;
        //自分だけ
        isOnlyMe: boolean;
        //おなじ部門の社員
        isEmployeeOfWorkplace: boolean;
        //おなじ＋配下部門の社員
        isEmployeeWorkplaceFollow: boolean;


        // 詳細検索タブ
        isAdvancedSearchTab: boolean;
        //複数選択 
        isMutipleCheck: boolean;

        //社員指定タイプ or 全社員タイプ
        isSelectAllEmployee: boolean;

        onSearchAllClicked: (data: EmployeeSearchDto[]) => void;

        onSearchOnlyClicked: (data: EmployeeSearchDto) => void;

        onSearchOfWorkplaceClicked: (data: EmployeeSearchDto[]) => void;

        onSearchWorkplaceChildClicked: (data: EmployeeSearchDto[]) => void;

        onApplyEmployee: (data: EmployeeSearchDto[]) => void;
    }
    
    export class ReportInfor {
        holidayRemainingOutputCondition: any;
        lstEmpIds: any[];
        constructor(holidayRemainingOutputCondition: any, lstEmpIds: any[]) {
            this.holidayRemainingOutputCondition = holidayRemainingOutputCondition;
            this.lstEmpIds = lstEmpIds;
        }
    }

    export class HolidayRemainingOutputCondition {
        startMonth: string;
        endMonth: string;
        outputItemSettingCode: string;
        pageBreak: string;

        constructor(startMonth: string, endMonth: string, outputItemSettingCode: string, pageBreak: string) {
            this.startMonth = startMonth;
            this.endMonth = endMonth;
            this.outputItemSettingCode = outputItemSettingCode;
            this.pageBreak = pageBreak;
        }
    }

    export class UserSpecificInformation {
        userId: string;
        companyId: string;
        outputItemSettingCode: string;
        pageBreakAtr: string;
        constructor(userId: string, companyId: string, outputItemSettingCode: string, pageBreakAtr: string) {
            this.userId = userId;
            this.companyId = companyId;
            this.outputItemSettingCode = outputItemSettingCode;
            this.pageBreakAtr = pageBreakAtr;
        }
    }
}