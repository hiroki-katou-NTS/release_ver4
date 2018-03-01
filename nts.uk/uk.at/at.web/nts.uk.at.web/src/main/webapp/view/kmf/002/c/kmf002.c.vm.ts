module nts.uk.at.view.kmf002.c {

    import service = nts.uk.at.view.kmf002.c.service;
    import blockUI = nts.uk.ui.block;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    export module viewmodel {
    export class ScreenModel {

        commonTableMonthDaySet: KnockoutObservable<nts.uk.at.view.kmf002.viewmodel.CommonTableMonthDaySet>;

        /* start declare variable CCG001 */
//        ccgcomponent: GroupOption;
//        showinfoSelectedEmployee: KnockoutObservable<boolean>;
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
        // Options
//        baseDate: KnockoutObservable<Date>;
        /* end declare variable CCG001 */
        
        /* start declare variable KCP005 */
        listComponentOption: any;
        selectedCode: KnockoutObservable<string>;
        multiSelectedCode: KnockoutObservableArray<string[]>;
        isShowAlreadySet: KnockoutObservable<boolean>;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel[]>;
        isDialog: KnockoutObservable<boolean>;
        isShowNoSelectRow: KnockoutObservable<boolean>;
        isMultiSelect: KnockoutObservable<boolean>;
        isShowWorkPlaceName: KnockoutObservable<boolean>;
        isShowSelectAllButton: KnockoutObservable<boolean>;
        employeeList: KnockoutObservableArray<UnitModel>;
        /* end declare variable KCP005 */
            
        enableSave: KnockoutObservable<boolean>;
        enableDelete: KnockoutObservable<boolean>;
        
        mapEmployeeCode: Map<string, string>;
        mapEmployeeID: Map<string, string>; 

            
        constructor() {
            let _self = this;
            
            _self.mapEmployeeCode = new Map<string, string>();
            _self.mapEmployeeID = new Map<string, string>();
            
            _self.enableSave = ko.observable(true);
            _self.enableDelete= ko.observable(true);
            _self.commonTableMonthDaySet = ko.observable(new nts.uk.at.view.kmf002.viewmodel.CommonTableMonthDaySet());
            
            /* start declare variable CCG001 */
            _self.selectedEmployee = ko.observableArray([]);
                
            /*
                Init variable CCG001
            */
            _self.isQuickSearchTab = ko.observable(true);
            _self.isAdvancedSearchTab = ko.observable(true);
            _self.isAllReferableEmployee = ko.observable(true);
            _self.isOnlyMe = ko.observable(true);
            _self.isEmployeeOfWorkplace = ko.observable(true);
            _self.isEmployeeWorkplaceFollow = ko.observable(true);
            _self.isMutipleCheck = ko.observable(true);
            _self.isSelectAllEmployee = ko.observable(true);
            _self.baseDate = ko.observable(moment());
            _self.periodStartDate = ko.observable(moment());
            _self.periodEndDate = ko.observable(moment());
            _self.showEmployment = ko.observable(true); // 雇用条件
            _self.showWorkplace = ko.observable(true); // 職場条件
            _self.showClassification = ko.observable(true); // 分類条件
            _self.showJobTitle = ko.observable(true); // 職位条件
            _self.showWorktype = ko.observable(true); // 勤種条件
            _self.inService = ko.observable(true); // 在職区分
            _self.leaveOfAbsence = ko.observable(true); // 休職区分
            _self.closed = ko.observable(true); // 休業区分
            _self.retirement = ko.observable(true); // 退職区分
            _self.systemType = ko.observable(1);
            _self.showClosure = ko.observable(true); // 就業締め日利用
            _self.showBaseDate = ko.observable(true); // 基準日利用
            _self.showAllClosure = ko.observable(true); // 全締め表示
            _self.showPeriod = ko.observable(true); // 対象期間利用
            _self.periodFormatYM = ko.observable(false); // 対象期間精度
                
            // Component option
            _self.ccgcomponent = {
                /** Common properties */
                systemType: _self.systemType(), // システム区分
                showEmployeeSelection: _self.isSelectAllEmployee(), // 検索タイプ
                showQuickSearchTab: _self.isQuickSearchTab(), // クイック検索
                showAdvancedSearchTab: _self.isAdvancedSearchTab(), // 詳細検索
                showBaseDate: _self.showBaseDate(), // 基準日利用
                showClosure: _self.showClosure(), // 就業締め日利用
                showAllClosure: _self.showAllClosure(), // 全締め表示
                showPeriod: _self.showPeriod(), // 対象期間利用
                periodFormatYM: _self.periodFormatYM(), // 対象期間精度

                /** Required parameter */
                baseDate: moment(_self.baseDate()).format("YYYY-MM-DD"), // 基準日
                periodStartDate: moment(_self.periodStartDate()).format("YYYY-MM-DD"), // 対象期間開始日
                periodEndDate: moment(_self.periodEndDate()).format("YYYY-MM-DD"), // 対象期間終了日
                inService: _self.inService(), // 在職区分
                leaveOfAbsence: _self.leaveOfAbsence(), // 休職区分
                closed: _self.closed(), // 休業区分
                retirement: _self.retirement(), // 退職区分

                /** Quick search tab options */
                showAllReferableEmployee: _self.isAllReferableEmployee(), // 参照可能な社員すべて
                showOnlyMe: _self.isOnlyMe(), // 自分だけ
                showSameWorkplace: _self.isEmployeeOfWorkplace(), // 同じ職場の社員
                showSameWorkplaceAndChild: _self.isEmployeeWorkplaceFollow(), // 同じ職場とその配下の社員

                /** Advanced search properties */
                showEmployment: _self.showEmployment(), // 雇用条件
                showWorkplace: _self.showWorkplace(), // 職場条件
                showClassification: _self.showClassification(), // 分類条件
                showJobTitle: _self.showJobTitle(), // 職位条件
                showWorktype: _self.showWorktype(), // 勤種条件
                isMutipleCheck: _self.isMutipleCheck(), // 選択モード

                /** Return data */
                returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                    _self.selectedEmployee(data.listEmployee);
                    _self.employeeList.removeAll();
                    _.forEach(data.listEmployee, function(value: any) {
                        _self.mapEmployeeCode.set(value.employeeCode, value.employeeId);
                        _self.mapEmployeeID.set(value.employeeId, value.employeeCode);
                        _self.employeeList.push({ code: value.employeeCode, name: value.employeeName, workplaceName: value.workplaceName});  
                    });
                    _self.findAllEmployeeRegister();
                    _self.initKCP004();
                }
            
            }
            /* end declare variable CCG001 */
                

            /* start declare variable KCP005 */
            _self.baseDate = ko.observable(new Date());
            _self.selectedCode = ko.observable("");
            _self.multiSelectedCode = ko.observableArray([]);
            _self.isShowAlreadySet = ko.observable(true);
            _self.alreadySettingList = ko.observableArray([]);
            _self.isDialog = ko.observable(false);
            _self.isShowNoSelectRow = ko.observable(false);
            _self.isMultiSelect = ko.observable(false);
            _self.isShowWorkPlaceName = ko.observable(true);
            _self.isShowSelectAllButton = ko.observable(false);
            _self.employeeList = ko.observableArray<UnitModel>();
            _self.listComponentOption = {
                isShowAlreadySet: _self.isShowAlreadySet(),
                isMultiSelect: _self.isMultiSelect(),
                listType: ListType.EMPLOYEE,
                employeeInputList: _self.employeeList,
                selectType: SelectType.SELECT_FIRST_ITEM,
                selectedCode: _self.selectedCode,
                isDialog: _self.isDialog(),
                isShowNoSelectRow: _self.isShowNoSelectRow(),
                alreadySettingList: _self.alreadySettingList,
                isShowWorkPlaceName: _self.isShowWorkPlaceName(),
                isShowSelectAllButton: _self.isShowSelectAllButton(),
                maxRows: 26
            };
            /* end declare variable KCP005 */
                
            /* include table */
            _self.commonTableMonthDaySet().visibleInfoSelect(true);
            _self.commonTableMonthDaySet().infoSelect1(nts.uk.resource.getText("Com_Person"));
//            _self.commonTableMonthDaySet.infoSelect2(_self.employeeList()[0].code);
//            _self.commonTableMonthDaySet.infoSelect3(_self.employeeList()[0].name);
                
            _self.selectedCode.subscribe(function(newValue: any) {   
                if (_.isUndefined(_self.selectedCode()) || _.isEmpty(_self.selectedCode()) || _.isNull(newValue)) {
                    _self.enableDelete(false);
                    _self.enableSave(false);
                    _self.commonTableMonthDaySet().infoSelect2('');
                    _self.commonTableMonthDaySet().infoSelect3('');
                    _self.setDefaultMonthDay();
                } else {
                     _.forEach(_self.employeeList(), function(value: any) {
                        if (value.code == newValue) {
                            _self.commonTableMonthDaySet().infoSelect2(newValue);
                            _self.commonTableMonthDaySet().infoSelect3(value.name);
                        }
                    });
                    _self.enableDelete(true);
                    _self.enableSave(true);
                    _self.getDataFromService();
                }
            });
                
            _self.commonTableMonthDaySet().fiscalYear.subscribe(function(newValue) {
                // change year
                if (!nts.uk.ui.errors.hasError()) {
                    _self.getDataFromService(); 
                    _self.findAllEmployeeRegister();
                }
            });
            }
            
            private setDefaultMonthDay(): void {
                let _self = this;
                for (let i=0; i<_self.commonTableMonthDaySet().arrMonth().length; i++) {
                    _self.commonTableMonthDaySet().arrMonth()[i].day(0); 
                }     
            }
        
            private initKCP004(): void {
                let _self = this;
                $('#component-items-list').ntsListComponent(_self.listComponentOption).done(function(data: any) {            
                });
            }
            
            private addDataToKCP004(dataEmployee: any): void {
                let _self = this;
                _self.employeeList.removeAll();
                _.forEach(dataEmployee, function(value: any) {
                    _self.employeeList.push({ code: value.employeeId, name: value.employeeName, workplaceName: value.workplaceName});  
                });
            }

        /**
         * init default data when start page
         */
        public start_page(): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            let _self = this;
            _self.getDataFromService();
             $.when($('#ccgcomponent').ntsGroupComponent(_self.ccgcomponent), 
                    $('#component-items-list').ntsListComponent(_self.listComponentOption)).done(function(data: any) {    
                dfd.resolve();    
            });
            return dfd.promise();
        }
            
        private save(): void {
            let _self = this;
            if (!nts.uk.ui.errors.hasError()) {
                let id = _self.mapEmployeeCode.get(_self.selectedCode());
                service.save(_self.commonTableMonthDaySet().fiscalYear(), _self.commonTableMonthDaySet().arrMonth(), id).done((data) => {
                    _self.getDataFromService();
                    _self.alreadySettingList.push({code: _self.selectedCode(), isAlreadySetting: true});
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                });    
            } 
        }
        
        private remove(): void {
            let _self = this;
             nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                service.remove(_self.commonTableMonthDaySet().fiscalYear(), _self.selectedCode()).done((data) => {
                    _self.getDataFromService();
                    _self.alreadySettingList.remove(function(s) { return s.code == _self.selectedCode() });
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                });   
            }).ifNo(() => {
            }).ifCancel(() => {
            }).then(() => {
            });
        }
            
        private findAllEmployeeRegister(): JQueryPromise<any> {
            var dfd = $.Deferred<void>();
            let _self = this;
            $.when(service.findAllEmployeeRegister(_self.commonTableMonthDaySet().fiscalYear())).done(function(data: any) {
                _self.alreadySettingList.removeAll();
                _.forEach(data, function(id) {
                    _self.alreadySettingList.push({code: _self.mapEmployeeID.get(id), isAlreadySetting: true});
                })
                dfd.resolve();
            });
            
            return dfd.promise();
        }
            
        private getDataFromService(): void {
            let _self = this;
            if (!_.isNull(_self.selectedCode()) && !_.isEmpty(_self.selectedCode())) {
                $.when(service.find(_self.commonTableMonthDaySet().fiscalYear(), _self.selectedCode()), 
                        service.findFirstMonth()
        //                ,_self.findAllEmployeeRegister()
                        ).done(function(data: any, data2: any) {
                    if (typeof data === "undefined") {
                        /** 
                         *   create value null for prepare create new 
                        **/
                        _.forEach(_self.commonTableMonthDaySet().arrMonth(), function(value: any) {
                            value.day(0);
                        });
                        _self.enableDelete(false);
                    } else {
                        if (_.isEmpty(data2)) {
                            data2.startMonth = 0;
                        }
                        _self.commonTableMonthDaySet().arrMonth.removeAll();
                        for (let i=data2.startMonth-1; i<12; i++) {
                            _self.commonTableMonthDaySet().arrMonth.push({'month': ko.observable(data.publicHolidayMonthSettings[i].month), 
                                                                          'day': ko.observable(data.publicHolidayMonthSettings[i].inLegalHoliday), 
                                                                          'enable': ko.observable(true)});    
                        }
                        for (let i=0; i<data2.startMonth-1; i++) {
                            _self.commonTableMonthDaySet().arrMonth.push({'month': ko.observable(data.publicHolidayMonthSettings[i].month), 
                                                                          'day': ko.observable(data.publicHolidayMonthSettings[i].inLegalHoliday), 
                                                                          'enable': ko.observable(true)});    
                        }
                        _self.enableDelete(true);
                    }  
                });    
            }
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
        
        export class EmployeeSearchDto {
            employeeId: string;
    
            employeeCode: string;
    
            employeeName: string;
    
            workplaceName: string;
        }
            
        export interface Ccg001ReturnedData {
            baseDate: string; // 基準日
            closureId?: number; // 締めID
            periodStart: string; // 対象期間（開始)
            periodEnd: string; // 対象期間（終了）
            listEmployee: Array<EmployeeSearchDto>; // 検索結果
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
    }
}