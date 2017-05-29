module nts.uk.pr.view.kmf001.d {
    
    import UpperLimitSettingFindDto = service.model.UpperLimitSettingFindDto;
    import RetentionYearlyFindDto = service.model.RetentionYearlyFindDto;
    import RetentionYearlyDto = service.model.RetentionYearlyDto;
    import UpperLimitSettingDto = service.model.UpperLimitSettingDto;
    import EmploymentSettingDto = service.model.EmploymentSettingDto;
    import EmploymentSettingFindDto = service.model.EmploymentSettingFindDto;
    
    export module viewmodel {
        export class ScreenModel {
            
            retentionYearsAmount: KnockoutObservable<number>;
            maxDaysCumulation: KnockoutObservable<number>;
            textEditorOption: KnockoutObservable<any>;
            yearsAmountByEmp: KnockoutObservable<number>;
            maxDaysCumulationByEmp: KnockoutObservable<number>;
            isManaged: KnockoutObservable<boolean>;
            
            employmentList: KnockoutObservableArray<ItemModel>;
            columnsSetting: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
            selectedCode: KnockoutObservable<string>;
            managementOption: KnockoutObservableArray<ManagementModel>;
            selectedManagement: KnockoutObservable<number>;
            hasEmpoyment: KnockoutObservable<boolean>;

            // Dirty checker
            dirtyChecker: nts.uk.ui.DirtyChecker;

            constructor() {
                var self = this;
                self.retentionYearsAmount = ko.observable(null);
                self.maxDaysCumulation = ko.observable(null);
                self.yearsAmountByEmp = ko.observable(null);
                self.maxDaysCumulationByEmp = ko.observable(null);
                self.textEditorOption = ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    width: "50px",
                    textmode: "text",
                    placeholder: "Not Empty",
                    textalign: "left"
                }));
                self.employmentList = ko.observableArray<ItemModel>([]);
                for (let i = 1; i < 9; i++) {
                    self.employmentList.push(new ItemModel('0' + i, '基本給', i % 3 === 0));
                }
                self.columnsSetting = ko.observableArray([
                    { headerText: 'コード', key: 'code', width: 100 },
                    { headerText: '名称', key: 'name', width: 150 },
                    { headerText: '設定済', key: 'alreadySet', width: 150 }
                ]);
                self.selectedCode = ko.observable('');
                self.managementOption = ko.observableArray<ManagementModel>([
                    new ManagementModel(1, '管理する'),
                    new ManagementModel(0, '管理しな')
                ]);
                self.selectedManagement = ko.observable(1);
                self.hasEmpoyment = ko.computed(function() {
                    return self.employmentList().length > 0;
                }, self);
                self.isManaged = ko.computed(function() {
                    return self.selectedManagement() == 1;
                }, self);
                
                self.selectedCode.subscribe(function(data: string){
                    service.findByEmployment(data).done(function(data1: EmploymentSettingFindDto){
                    if(data1 == null) {
                        self.yearsAmountByEmp(0);
                        self.maxDaysCumulationByEmp(0);
                        self.selectedManagement(0);
                    }
                    else {
                       self.bindEmploymentSettingData(data1);
                    }
                    });
                });
            }
            
            public startPage(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                service.findRetentionYearly().done(function(data: RetentionYearlyFindDto) {
                    if(data == null) {
                        self.retentionYearsAmount(1);
                        self.maxDaysCumulation(40);
                    }
                    else {
                       self.initializeWholeCompanyData(data);
                    }
                    dfd.resolve();
                })
                return dfd.promise();
            }
            
            public bindEmploymentSettingData(data: EmploymentSettingFindDto): void {
                var self = this;
                self.yearsAmountByEmp(data.upperLimitSetting.retentionYearsAmount);
                self.maxDaysCumulationByEmp(data.upperLimitSetting.maxDaysCumulation);
                self.selectedManagement(data.managementCategory);
            }
            
            initializeWholeCompanyData(data: RetentionYearlyFindDto): void {
                var self = this;
                self.retentionYearsAmount(data.upperLimitSetting.retentionYearsAmount);
                self.maxDaysCumulation(data.upperLimitSetting.maxDaysCumulation);
            }
            
            public backToHistorySelection() {
                
            }
            
            public collectWholeCompanyData(): RetentionYearlyDto {
                var self = this;
                var dto: RetentionYearlyDto = new RetentionYearlyDto();
                var upperDto: UpperLimitSettingDto = new  UpperLimitSettingDto();
                upperDto.retentionYearsAmount = self.retentionYearsAmount();
                upperDto.maxDaysCumulation = self.maxDaysCumulation();
                dto.upperLimitSettingDto = upperDto;
                return dto;
            }
            
            public registerWholeCompany(): void {
                var self = this;
                /*
                // Validate.
                $('.nts-input').ntsEditor('validate');
                if ($('.nts-input').ntsError('hasError')) {
                    return;
                }
                */
                service.saveRetentionYearly(self.collectWholeCompanyData()).done(function() {
                    nts.uk.ui.dialog.alert('登録しました。');
                })
                    .fail((res) => {
                        nts.uk.ui.dialog.alert(res.message);
                    });
            }
            
            public collectDataByEmployment(): EmploymentSettingDto {
                var self = this;
                var dto: EmploymentSettingDto = new EmploymentSettingDto();
                var upperLimitDto: UpperLimitSettingDto = new  UpperLimitSettingDto();
                upperLimitDto.retentionYearsAmount = self.yearsAmountByEmp();
                upperLimitDto.maxDaysCumulation = self.maxDaysCumulationByEmp();
                dto.upperLimitSetting = upperLimitDto;
                dto.employmentCode = self.selectedCode();
                dto.managementCategory = self.selectedManagement();
                return dto;
            }
            
            
            public registerByEmployment(): void {
                var self = this;
                service.saveByEmployment(self.collectDataByEmployment()).done(function() {
                    nts.uk.ui.dialog.alert('登録しました。');
                })
                    .fail((res) => {
                        nts.uk.ui.dialog.alert(res.message);
                    });
            }
            
        }
        
        class ItemModel {

            code: string;
            name: string;
            alreadySet: boolean;
            constructor(code: string, name: string, alreadySet: boolean) {
                this.code = code;
                this.name = name;
                this.alreadySet = alreadySet;
            }
        }
        
        class ManagementModel {
            code: number;
            name: string;
            constructor(code: number, name: string) {
                this.code = code;
                this.name = name;
            }
        }
    }
}