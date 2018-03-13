module nts.uk.pr.view.kmf001.d {
    
    import UpperLimitSettingFindDto = service.model.UpperLimitSettingFindDto;
    import RetentionYearlyFindDto = service.model.RetentionYearlyFindDto;
    import RetentionYearlyDto = service.model.RetentionYearlyDto;
    import UpperLimitSettingDto = service.model.UpperLimitSettingDto;
    import EmploymentSettingDto = service.model.EmploymentSettingDto;
    import EmploymentSettingFindDto = service.model.EmploymentSettingFindDto;
    
    
    export module viewmodel {
        export class ScreenModel {
            selectedItem: KnockoutObservable<string>;
            listComponentOption: KnockoutObservable<any>;
            alreadySettingList: KnockoutObservableArray<any>;
            enableRegister: KnockoutObservable<boolean>;
            
            retentionYearsAmount: KnockoutObservable<number>;
            maxDaysCumulation: KnockoutObservable<number>;
            
            yearsAmountByEmp: KnockoutObservable<number>;
            maxDaysCumulationByEmp: KnockoutObservable<number>;
            isManaged: KnockoutObservable<boolean>;
            annualManage: KnockoutObservable<number>;
            
            employmentList: KnockoutObservableArray<ItemModel>;
            managementOption: KnockoutObservableArray<ManagementModel>;
            selectedManagement: KnockoutObservable<number>;
            selectedComManagement: KnockoutObservable<number>;
            hasSelectedEmp: KnockoutObservable<boolean>;
            leaveAsWorkDays: KnockoutObservable<boolean>;
            leaveAsWorkDaysOpt: KnockoutObservableArray<LeaveAsWorkDaysModel>;
            isShowEmployment: KnockoutObservable<boolean>;
            
            selectedName: KnockoutObservable<string>;

            employmentVisible: KnockoutObservable<boolean>;
            
            // Dirty checker
            dirtyChecker: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                this.selectedItem = ko.observable(null);
                
                self.selectedName = ko.observable(null);
                self.alreadySettingList = ko.observableArray([]);

                self.employmentList = ko.observableArray<ItemModel>([]);
                self.isShowEmployment = ko.observable(false);
                
                // Initialize properties of Component
                this.listComponentOption = {
                    isShowAlreadySet: true, // is show already setting column.
                    isMultiSelect: false, // is multiselect.
                    listType: ListType.EMPLOYMENT,
                    selectedCode: self.selectedItem,
                    isDialog: false,
                    alreadySettingList: self.alreadySettingList
                };
                
                self.retentionYearsAmount = ko.observable(null);
                self.maxDaysCumulation = ko.observable(null);
                self.yearsAmountByEmp = ko.observable(null);
                self.maxDaysCumulationByEmp = ko.observable(null);
                
                self.managementOption = ko.observableArray<ManagementModel>([
                    new ManagementModel(1, '管理する'),
                    new ManagementModel(0, '管理しない')
                ]);
                self.selectedManagement = ko.observable(1);
                self.selectedComManagement = ko.observable(0);
                self.hasSelectedEmp = ko.observable(false);
                
                self.employmentVisible = ko.observable(self.selectedComManagement() == 1);
                
                self.isManaged = ko.computed(function() {
                    return self.selectedManagement() == 1;
                }, self);
                
                self.annualManage = ko.observable(1);
                self.isManaged = ko.computed(function() {
                    return self.annualManage() == 1;
                }, self);
                self.leaveAsWorkDaysOpt = ko.observableArray<LeaveAsWorkDaysModel>([
                    new LeaveAsWorkDaysModel(true, '管理する'),
                    new LeaveAsWorkDaysModel(false, '管理しない')
                ]);
                self.leaveAsWorkDays = ko.observable(null);
                self.enableRegister = ko.computed(function() {
                    return self.isManaged() && self.hasSelectedEmp();
                }, self);
            }
            
            // Start Page
            public startPage(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                self.findIsManaged().done(function() {
                    self.findRetentionYearly();
                    dfd.resolve();
                })
                .fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
                return dfd.promise();
            }
            
            // Find annualManaged to able/disable controls
            private findIsManaged(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred();
                service.findIsManaged().done(function(data: any) {
                    if (data != undefined) {
                        self.annualManage(data.annualManage);
                    } else {
                        self.annualManage(0);
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });
                return dfd.promise();
            }
            
            // Find Yearly Retention
            private findRetentionYearly(): void {
                var self = this;
                service.findRetentionYearly().done(function(data: RetentionYearlyFindDto) {
                    if (data == null) {
                        self.retentionYearsAmount(null);
                        self.maxDaysCumulation(null);
                        self.leaveAsWorkDays(false);
                        self.selectedComManagement(0);
                    }
                    else {
                        self.initializeWholeCompanyData(data);
                    }
                    $('#switch-btn-anagement').focus();
                    self.employmentVisible(self.selectedComManagement() == 1);
                });
            }
            
            // Bind EmploymentSetting Data
            private bindEmploymentSettingData(data: EmploymentSettingFindDto): void {
                var self = this;
                self.clearEmptErrors();
                if (data == undefined) {
                    self.yearsAmountByEmp(null);
                    self.maxDaysCumulationByEmp(null);
                    self.selectedManagement(0);
                }
                else {
                    // Set EmploymentSetting Data
                    self.yearsAmountByEmp(data.upperLimitSetting.retentionYearsAmount);
                    self.maxDaysCumulationByEmp(data.upperLimitSetting.maxDaysCumulation);
                    self.selectedManagement(data.managementCategory);
                }
            }
            
            // Initialize wholeCompany Data
            initializeWholeCompanyData(data: RetentionYearlyFindDto): void {
                var self = this;
                self.retentionYearsAmount(data.upperLimitSetting.retentionYearsAmount);
                self.maxDaysCumulation(data.upperLimitSetting.maxDaysCumulation);
                self.leaveAsWorkDays(data.leaveAsWorkDays);
                self.selectedComManagement(data.managementCategory);
            }
            
            // Collect wholeCompany Data
            private collectWholeCompanyData(): RetentionYearlyDto {
                var self = this;
                var dto: RetentionYearlyDto = new RetentionYearlyDto();
                var upperDto: UpperLimitSettingDto = new UpperLimitSettingDto();
                upperDto.retentionYearsAmount = self.retentionYearsAmount();
                upperDto.maxDaysCumulation = self.maxDaysCumulation();
                dto.upperLimitSettingDto = upperDto;
                dto.leaveAsWorkDays = self.leaveAsWorkDays();
                dto.managementCategory = self.selectedComManagement();
                return dto;
            }
            
            // Switch To Employment Tab
            private switchToEmploymentTab(): void {
                var self = this;
                // Clear Errors
                self.clearErrors();
                self.isShowEmployment(true);
                // Already Setting List
                service.findAllByEmployment().done(function(data: any) {
                    for (var i = 0; i < data.length; i++) {
                        self.alreadySettingList.push({ "code": data[i].employmentCode, "isAlreadySetting": true });
                    }
                });
                // Selected Item subscribe
                self.selectedItem.subscribe(function(data: string) {
                    if (data) {
                        // Find EmploymentSetting By employment
                        service.findByEmployment(data).done(function(data1: EmploymentSettingFindDto) {
                            self.bindEmploymentSettingData(data1);
                        });
                        
                        // Set displayed Employee name
                        let employmentList: Array<UnitModel> = $('#left-content').getDataList();  
                        let selectedEmp = _.find(employmentList, { 'code': data });
                        self.selectedName(':   ' + selectedEmp.name);
                        
                        self.hasSelectedEmp(true);
                    }
                    else {
                        // Set displayed Employee name to empty
                        self.selectedName('');
                        self.hasSelectedEmp(false);
                    }
                    
                });

                // Load Component
                $('#left-content').ntsListComponent(self.listComponentOption).done(function() {
                    // Set Focus on Switch Button
                    $('#switch-btn').focus();
                    
                    // Get Data List
                    if (($('#left-content').getDataList() == undefined) || ($('#left-content').getDataList().length <= 0)) {
                        //                        self.hasSelectedEmp(false);
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_146" });
                    }
                    else {
                        // Get Employment List after Load Component
                        self.employmentList($('#left-content').getDataList());
                        // Set Selected Item
                        self.selectedItem(self.employmentList()[0].code);
                        //                        self.hasSelectedEmp(true);
                    }
                });
            }
            
            // Switch To Company Tab
            private switchToCompanyTab(): void {
                var self = this;
                // Clear Errors
                self.clearEmptErrors();
                self.isShowEmployment(false);
                self.findRetentionYearly();
            }
            
            // Method: Register By Whole Company
            private registerWholeCompany(): void {
                var self = this;
                
                // Clear errors
                self.clearErrors();
                
                // Validate. 
                $('#year-amount-company').ntsEditor('validate');
                $('#max-days-company').ntsEditor('validate');
                
                if ($('.nts-input').ntsError('hasError')) {
                    return;
                }
                // Register
                service.saveRetentionYearly(self.collectWholeCompanyData()).done(function() {
                    self.employmentVisible(self.selectedComManagement() == 1);
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                }).fail((res) => {
                    nts.uk.ui.dialog.alertError(res.message);
                });
            }
            
            // Clear Errors Company Tab
            private clearErrors(): void {
                var self = this;
                // Clear errors
                $('#year-amount-company').ntsError('clear');
                $('#max-days-company').ntsError('clear');
            }
            
            // Clear Errors Employment Tab
            private clearEmptErrors(): void {
                var self = this;
                $('#year-amount-emp').ntsError('clear');
                $('#max-days-emp').ntsError('clear');
            }
            
            // Collect Data By Employment
            private collectDataByEmployment(): EmploymentSettingDto {
                var self = this;
                var dto: EmploymentSettingDto = new EmploymentSettingDto();
                var upperLimitDto: UpperLimitSettingDto = new UpperLimitSettingDto();
                upperLimitDto.retentionYearsAmount = self.yearsAmountByEmp();
                upperLimitDto.maxDaysCumulation = self.maxDaysCumulationByEmp();
                dto.upperLimitSetting = upperLimitDto;
                dto.employmentCode = self.selectedItem();
                dto.managementCategory = self.selectedManagement();
                return dto;
            }
            
            private deleteByEmployment(): void {
                var self = this;
                service.deleteByEmployment(self.selectedItem());
                nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                
                // Remove item from setting list (un-tick)
                self.alreadySettingList.remove(function(item){ return item.code == self.selectedItem()});
                
                // Reload current setting
                service.findByEmployment(self.selectedItem()).done(function(data1: EmploymentSettingFindDto) {
                    self.bindEmploymentSettingData(data1);
                });
            }
            
            // Method register By Employment
            private registerByEmployment(): void {
                var self = this;
                // Clear errors
                self.clearEmptErrors();

                // Validate. 
                $('#year-amount-emp').ntsEditor('validate');
                $('#max-days-emp').ntsEditor('validate');
                
                if ($('.nts-input').ntsError('hasError')) {
                    return;
                }
                
                // Register
                service.saveByEmployment(self.collectDataByEmployment()).done(function() {
                    self.alreadySettingList.push({ "code": self.selectedItem(), "isAlreadySetting": true });
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                })
                    .fail((res) => {
                        nts.uk.ui.dialog.alertError(res.message);
                    });
            }

        }
        
        // Class ListType
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        // UnitModel
        export interface UnitModel {
            code: string;
            name?: string;
            workplaceName?: string;
            isAlreadySetting?: boolean;
        }

        // Class ItemModel
        class ItemModel {
            code: string;
            name: string;
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        // Class ManagementModel
        class ManagementModel {
            code: number;
            name: string;
            constructor(code: number, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        
        // Class LeaveAsWorkDaysModel
        class LeaveAsWorkDaysModel {
            value: boolean;
            name: string;
            constructor(value: boolean, name: string) {
                this.value = value;
                this.name = name;
            }
        }
    }
}