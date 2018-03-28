module nts.uk.at.view.kmk004.b {
    export module viewmodel {
        import WorktimeSettingVM = nts.uk.at.view.kmk004.shr.worktime.setting.viewmodel;
        
        export class ScreenModel {
            
            tabs: KnockoutObservableArray<NtsTabPanelModel>;
            
            worktimeSetting: WorktimeSettingVM.ScreenModel;
            
            isNewMode: KnockoutObservable<boolean>;
            isLoading: KnockoutObservable<boolean>;
            
            startMonth: KnockoutObservable<number>;
            
            displayEmployeeCode: KnockoutObservable<string>;
            displayEmployeeName: KnockoutObservable<string>;
            
            // Employee tab
            lstPersonComponentOption: any;
            selectedEmployeeCode: KnockoutObservableArray<string>;
            employeeName: KnockoutObservable<string>;
            employeeList: KnockoutObservableArray<UnitModel>;
            alreadySettingPersonal: KnockoutObservableArray<any>;
            ccgcomponentPerson: GroupOption;
            
            ccgcomponent: GroupOption;
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
            
            constructor() {
                let self = this;
                self.isNewMode = ko.observable(true);
                self.isLoading = ko.observable(true);
                
                // Datasource.
                self.tabs = ko.observableArray([
                    { id: 'tab-1', title: nts.uk.resource.getText("KMK004_3"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: nts.uk.resource.getText("KMK004_4"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-3', title: nts.uk.resource.getText("KMK004_5"), content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true) }
                ]);
                
                self.worktimeSetting = new WorktimeSettingVM.ScreenModel();
                self.selectedEmployee = ko.observableArray([]);
                self.selectedEmployeeCode = ko.observableArray([]);
                self.alreadySettingPersonal = ko.observableArray([]);
                
                self.displayEmployeeCode = ko.observable('');
                self.displayEmployeeName = ko.observable('');
                
                // initial ccg options
                self.setDefaultCcg001Option();
                
                // Init component.
                self.reloadCcg001();
                
                self.selectedEmployeeCode.subscribe(function(code){
                    if (code != null && code.length > 0) {
                        let empt = _.find(self.selectedEmployee(), item => item.employeeCode == code);
                        if (empt) {
                            self.displayEmployeeCode(code);
                            self.displayEmployeeName(empt.employeeName);
                            self.isNewMode( !(_.find(self.alreadySettingPersonal(), item => item.code == code ) != null) );
                            return;
                        }
                    }
                    self.displayEmployeeCode('');
                    self.displayEmployeeName('');
                    self.isNewMode(true);
                });
            }
                
            public startPage(): JQueryPromise<void> {
                nts.uk.ui.block.invisible();
                let self = this;
                let dfd = $.Deferred<void>();
                
                self.isLoading(true);

                // Load component.
                self.worktimeSetting.initialize().done(() => {
                    WorktimeSettingVM.getStartMonth().done((month) => {
                        self.startMonth = ko.observable(month);
                        
                        dfd.resolve();
                    }).fail(() => {
                        nts.uk.ui.block.clear();
                    });
                }).fail(() => {
                    nts.uk.ui.block.clear();
                });
                return dfd.promise();
            }
            
            public postBindingProcess(): void {
                let self = this;
                
                $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent).done(function() {
                    self.employeeList = ko.observableArray<UnitModel>([]);
                    self.applyKCP005ContentSearch([]);
                    // Load employee list component
                    $('#list-employee').ntsListComponent(self.lstPersonComponentOption).done(function() {
                        if(self.employeeList().length <= 0){
                            $('#ccg001-btn-search-drawer').trigger('click');  
                        }
                        
                        self.isLoading(false);
                        
                        ko.applyBindingsToNode($('#lblEmployeeCode')[0], { text: self.displayEmployeeCode });
                        ko.applyBindingsToNode($('#lblEmployeeName')[0], { text: self.displayEmployeeName });
                    });
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
                self.isSelectAllEmployee = ko.observable(true);
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
                self.systemType = ko.observable(1);
                self.showClosure = ko.observable(true); // 就業締め日利用
                self.showBaseDate = ko.observable(true); // 基準日利用
                self.showAllClosure = ko.observable(true); // 全締め表示
                self.showPeriod = ko.observable(true); // 対象期間利用
                self.periodFormatYM = ko.observable(false); // 対象期間精度
            }
            
            /**
             * Reload component CCG001
             */
            public reloadCcg001(): void {
                var self = this;
                var periodStartDate, periodEndDate : string;
                if (self.showBaseDate()){
                    periodStartDate = moment(self.periodStartDate()).format("YYYY-MM-DD");
                    periodEndDate = moment(self.periodEndDate()).format("YYYY-MM-DD");
                } else {
                    periodStartDate = moment(self.periodStartDate()).format("YYYY-MM");
                    periodEndDate = moment(self.periodEndDate()).format("YYYY-MM"); // 対象期間終了日
                }
                
                if (!self.showBaseDate() && !self.showClosure() && !self.showPeriod()){
                    nts.uk.ui.dialog.alertError("Base Date or Closure or Period must be shown!" );
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
                        self.selectedEmployee(data.listEmployee);
                        
                        service.findAlreadySetting().done((settingData) => {
                            let listCode = _.map(self.selectedEmployee(), item => {
                                if ( _.find(settingData, it => { return it.employeeId == item.employeeId; } )) return item.employeeCode;
                                return null; 
                            });
                            listCode = _.filter(listCode, item => { return item != null });
                            let listSelected = [];
                            
                            _.each(listCode, item => {
                                listSelected.push( { code: item, isAlreadySetting: true } );
                            });
                            
                            self.alreadySettingPersonal(listSelected);
                            self.applyKCP005ContentSearch(self.selectedEmployee());
                        });
                    }
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
                    isShowAlreadySet: true,
                    isMultiSelect: false,
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
            
            public saveEmployeeSetting(): void {
                
            }
            
            public removeEmployeeSetting(): void {
                
            }
        } // --- end ScreenModel
        
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
    }
}