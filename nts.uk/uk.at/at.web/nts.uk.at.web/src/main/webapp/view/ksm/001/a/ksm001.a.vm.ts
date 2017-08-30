module nts.uk.at.view.ksm001.a {

    import EstimateTimeDto = service.model.EstimateTimeDto;
    import EstimatePriceDto = service.model.EstimatePriceDto;
    import EstimateDaysDto = service.model.EstimateDaysDto;
    import EstablishmentTimeDto = service.model.EstablishmentTimeDto;
    import EstablishmentPriceDto = service.model.EstablishmentPriceDto;
    import EstablishmentDaysDto = service.model.EstablishmentDaysDto;
    import CompanyEstablishmentDto = service.model.CompanyEstablishmentDto;
    import EmploymentEstablishmentDto = service.model.EmploymentEstablishmentDto;
    import PersonalEstablishmentDto = service.model.PersonalEstablishmentDto;
    import UsageSettingDto = service.model.UsageSettingDto;

    export module viewmodel {

        export class ScreenModel {
            usageSettingModel: UsageSettingModel;
            lstTargetYear: KnockoutObservableArray<any>;
            isCompanySelected: KnockoutObservable<boolean>;
            isEmploymentSelected: KnockoutObservable<boolean>;
            isPersonSelected: KnockoutObservable<boolean>;
            isLoading: KnockoutObservable<boolean>;
            companyEstablishmentModel: EstablishmentModel;
            employmentEstablishmentModel: EstablishmentModel;
            personalEstablishmentModel: EstablishmentModel;
            tabs: KnockoutObservableArray<NtsTabPanelModel>;
           
            employmentTabs: KnockoutObservableArray<NtsTabPanelModel>;
            
            selEmploymentTab: KnockoutObservable<string>;
            
            
            lstEmploymentComponentOption: any;
            selectedEmploymentCode: KnockoutObservable<string>;
            
            ccgcomponentPerson: GroupOption;

            // Options
            baseDate: KnockoutObservable<Date>;
            selectedEmployee: KnockoutObservableArray<EmployeeSearchDto>;

            lstPersonComponentOption: any;
            selectedEmployeeCode: KnockoutObservable<string>;
            employeeName: KnockoutObservable<string>;
            employmentName: KnockoutObservable<string>;
            alreadySettingEmployment: KnockoutObservableArray<UnitAlreadySettingModel>;
            isShowNoSelectRow: KnockoutObservable<boolean>;
            employeeList: KnockoutObservableArray<UnitModel>;

            employmentList: KnockoutObservableArray<UnitModel>;
            
            alreadySettingPersonal: KnockoutObservableArray<UnitAlreadySettingModel>;

            constructor() {
                var self = this;
                self.usageSettingModel = new UsageSettingModel();
                this.tabs = ko.observableArray([
                    { id: 'tab-1', title: nts.uk.resource.getText("KSM001_23"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: nts.uk.resource.getText("KSM001_24"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-3', title: nts.uk.resource.getText("KSM001_25"), content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true) }
                ]);

                self.baseDate = ko.observable(new Date());
                self.selectedEmployee = ko.observableArray([]);
                self.lstTargetYear = ko.observableArray([]);
                self.companyEstablishmentModel = new EstablishmentModel();
                self.employmentEstablishmentModel = new EstablishmentModel();
                self.personalEstablishmentModel = new EstablishmentModel();
                self.isCompanySelected = ko.observable(false);
                self.isEmploymentSelected = ko.observable(false);
                self.isPersonSelected = ko.observable(false);
                self.isLoading = ko.observable(false);
                self.selectedEmploymentCode = ko.observable('');
                self.alreadySettingEmployment = ko.observableArray([]);
                self.alreadySettingPersonal = ko.observableArray([]);
                self.selectedEmployeeCode = ko.observable('');
                
                self.lstEmploymentComponentOption = {
                    isShowAlreadySet: true,
                    isMultiSelect: false,
                    listType: ListType.EMPLOYMENT,
                    selectType: SelectType.SELECT_FIRST_ITEM,
                    selectedCode: self.selectedEmploymentCode,
                    isDialog: false,
                    isShowNoSelectRow: false,
                    alreadySettingList: self.alreadySettingEmployment,
                    maxRows: 12
                };
                
                self.ccgcomponentPerson = {
                    baseDate: self.baseDate,
                    //Show/hide options
                    isQuickSearchTab: true,
                    isAdvancedSearchTab: true,
                    isAllReferableEmployee: true,
                    isOnlyMe: true,
                    isEmployeeOfWorkplace: true,
                    isEmployeeWorkplaceFollow: true,
                    isMutipleCheck: true,
                    isSelectAllEmployee: true,
                    /**
                    * @param dataList: list employee returned from component.
                    * Define how to use this list employee by yourself in the function's body.
                    */
                    onSearchAllClicked: function(dataList: EmployeeSearchDto[]) {
                        self.selectedEmployee(dataList);
                        self.applyKCP005ContentSearch(dataList);
                    },
                    onSearchOnlyClicked: function(data: EmployeeSearchDto) {
                        var dataEmployee: EmployeeSearchDto[] = [];
                        dataEmployee.push(data);
                        self.selectedEmployee(dataEmployee);
                        self.applyKCP005ContentSearch(dataEmployee);
                    },
                    onSearchOfWorkplaceClicked: function(dataList: EmployeeSearchDto[]) {
                        self.selectedEmployee(dataList);
                        self.applyKCP005ContentSearch(dataList);
                    },
                    onSearchWorkplaceChildClicked: function(dataList: EmployeeSearchDto[]) {
                        self.selectedEmployee(dataList);
                        self.applyKCP005ContentSearch(dataList);
                    },
                    onApplyEmployee: function(dataEmployee: EmployeeSearchDto[]) {
                        self.selectedEmployee(dataEmployee);
                        self.applyKCP005ContentSearch(dataEmployee);
                    }

                }
                self.employmentName = ko.observable('');
                self.employmentList = ko.observableArray([]);
                self.employmentList.subscribe(function() {
                    self.employmentName(self.findEmploymentByCode(self.selectedEmploymentCode()).name);
                });
                
                self.employmentTabs = ko.observableArray([
                    { id: 'emp-tab-1', title: nts.uk.resource.getText("KSM001_23"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'emp-tab-2', title: nts.uk.resource.getText("KSM001_24"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'emp-tab-3', title: nts.uk.resource.getText("KSM001_25"), content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true) }
                ]);
                self.selEmploymentTab = ko.observable('emp-tab-1');

                // Selected year subscription.
                self.companyEstablishmentModel.selectedYear.subscribe(year => {
                    self.loadCompanyEstablishment(year, false);
                });
                self.employmentEstablishmentModel.selectedYear.subscribe(year => {
                    self.updateEmploymentEstimateSetting(year);
                    self.loadEmploymentEstablishment(year, self.selectedEmploymentCode(), false);
                });
                self.personalEstablishmentModel.selectedYear.subscribe(year => {
                    self.updatePersonalEstimateSetting(year);
                    self.loadCompanyEstablishment(year, false);
                    self.updatePersonalEstimateSetting(year);
                });
                
                self.selectedEmployeeCode.valueHasMutated();
                self.selectedEmployeeCode.subscribe(function(employeeCode) {
                    if (!employeeCode) {
                        self.personalEstablishmentModel.disableInput();
                        self.personalEstablishmentModel.enableDelete(false);
                    }else {
                        var employment: UnitModel = self.findByCodeEmployee(employeeCode);
                        if (employment) {
                            self.employeeName(employment.name);
                        }
                        self.personalEstablishmentModel.enableInput();
                        self.loadPersonalEstablishment(self.personalEstablishmentModel.selectedYear(), self.selectedEmployeeCode(), false);
                        
                    }
                });

                self.selectedEmploymentCode.subscribe(function(employmentCode) {
                    if (!employmentCode) {
                        self.employmentEstablishmentModel.disableInput();
                    }
                    if (employmentCode) {
                        var employment: UnitModel = self.findEmploymentByCode(employmentCode);
                        if (employment) {
                            self.employmentName(employment.name);
                        }
                        self.employmentEstablishmentModel.enableInput();

                        let currentYear = self.employmentEstablishmentModel.selectedYear() ?
                            self.employmentEstablishmentModel.selectedYear() : moment().year();

                        self.loadEmploymentEstablishment(currentYear, employmentCode, false);
                    }
                });
                
            }
            
            /**
             * call service load UsageSettingModel
             */
            private loadUsageSettingModel(): void {
                var self = this;
                service.findCompanySettingEstimate().done(function(data) {
                    self.usageSettingModel.updateData(data);
                });
            }
            /**
            * start page data 
            */
            public startPage(): JQueryPromise<any> {
                nts.uk.ui.block.invisible();
                var self = this;
                var dfd = $.Deferred();
                self.setSelectableYears();
                self.loadUsageSettingModel();
                self.onSelectCompany().done(function(){
                    dfd.resolve(self);    
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
                return dfd.promise();
            }

            private setSelectableYears(): void {
                let self = this;
                let currentYear = moment();

                // Get 2 years before, 2 years after.
                let arr = [];
                arr.push(currentYear.subtract(2, 'years').year());
                arr.push(currentYear.add(1, 'years').year());
                arr.push(currentYear.add(1, 'years').year());
                arr.push(currentYear.add(1, 'years').year());
                arr.push(currentYear.add(1, 'years').year());

                // Map to model
                let mapped = arr.map(i => {
                    return { year: i }
                });

                self.lstTargetYear(mapped);
            }
            
            public visibleTabpanel() {
                var self = this;
                
            }
            
           /**
            * load company establishment
            */
            private loadCompanyEstablishment(targetYear: number, isLoading: boolean): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                service.findCompanyEstablishment(targetYear).done(function(data) {
                    self.companyEstablishmentModel.estimateTimeModel.updateData(data.estimateTime);
                    self.companyEstablishmentModel.estimatePriceModel.updateData(data.estimatePrice);
                    self.companyEstablishmentModel.estimateDaysModel.updateData(data.estimateNumberOfDay);
                    if (isLoading) {
                        self.isLoading(false);
                    }
                    self.initNextTabFeature();
                    dfd.resolve();
                });
                return dfd.promise();
            }
            /**
             * on click tab panel company action event
             */
            public onSelectCompany(): JQueryPromise<void> {
                $('.nts-input').ntsError('clear');
                var self = this;
                nts.uk.ui.block.invisible();
                var dfd = $.Deferred<void>();
                self.isEmploymentSelected(false);
                self.isPersonSelected(false);
                self.isCompanySelected(true);
                self.isLoading(true);

                let currentYear = self.companyEstablishmentModel.selectedYear() ?
                    self.companyEstablishmentModel.selectedYear() : moment().year();

                self.loadCompanyEstablishment(currentYear, true).done(function() {
                    dfd.resolve();
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
                return dfd.promise();
            }
           
            /**
             * find all employment setting to view
             */
            private updateEmploymentEstimateSetting(targetYear: number){
                var self = this;
                service.findAllEmploymentSetting(targetYear).done(function(data) {
                    var employmentSettings: UnitAlreadySettingModel[] = [];
                    for (var employment of data) {
                        var employmentSetting: UnitAlreadySettingModel = { code: employment.employmentCode, isAlreadySetting: true };
                        employmentSettings.push(employmentSetting);
                    }
                    self.alreadySettingEmployment(employmentSettings);
                });
            }
            
            /**
             * check setting employment setting
             */
            private checkEmploymentSetting(employmentCode: string){
                var self = this;
                for(var employmentSetting of self.alreadySettingEmployment()){
                    if(employmentSetting.code === employmentCode && employmentSetting.isAlreadySetting){
                        return true;    
                    }    
                }    
                
                return false;
            }
            
             
             /**
            * load company establishment
            */
            private loadEmploymentEstablishment(targetYear: number, employmentCode: string, isLoading: boolean): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                service.findEmploymentEstablishment(targetYear,employmentCode).done(function(data) {
                    self.employmentEstablishmentModel.estimateTimeModel.updateData(data.estimateTime);
                    self.employmentEstablishmentModel.estimatePriceModel.updateData(data.estimatePrice);
                    self.employmentEstablishmentModel.estimateDaysModel.updateData(data.estimateNumberOfDay);
                    if (isLoading) {
                        self.isLoading(false);
                    }
                    self.initNextTabFeature();
                    nts.uk.ui.block.clear();
                    dfd.resolve();
                });
                return dfd.promise();
            }
             /**
             * on click tab panel employment action event
             */
            public onSelectEmployment(): void {
                $('.nts-input').ntsError('clear');
                nts.uk.ui.block.invisible();
                var self = this;
                self.isCompanySelected(false);
                self.isPersonSelected(false);
                self.isEmploymentSelected(true);
                self.isLoading(true);

                $('#employmentSetting').ntsListComponent(self.lstEmploymentComponentOption).done(function() {
                    self.employmentList($('#employmentSetting').getDataList());
                    self.loadEmploymentEstablishment(self.employmentEstablishmentModel.selectedYear(),self.selectedEmploymentCode(), true);
                    self.updateEmploymentEstimateSetting(self.employmentEstablishmentModel.selectedYear());
                });
                
            }
            /**
             * call service find all personal setting => view UI
             */
            private updatePersonalEstimateSetting(targetYear: number){
                var self = this;
                service.findAllPersonalSetting(targetYear).done(function(data) {
                    var personalSettings: UnitAlreadySettingModel[] = [];
                    for (var personal of data) {
                        var employmentSetting: UnitAlreadySettingModel = { code: self.findEmployeeCodeById(personal.employeeId), isAlreadySetting: true };
                        personalSettings.push(employmentSetting);
                    }
                    self.alreadySettingPersonal(personalSettings);
                    self.updateEnableDeletePersonal(self.selectedEmployeeCode());
                });
            }
            
            /**
             * 
             */
            private updateEnableDeletePersonal(employeeCode: string) {
                var self = this;
                self.personalEstablishmentModel.enableDelete(self.checkSettingPersonal(employeeCode));
            }
              
             /**
            * load personal establishment
            */
            private loadPersonalEstablishment(targetYear: number, employeeCode: string, isLoading: boolean): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                var self = this;
                var employeeId = self.findEmployeeIdByCode(employeeCode);
                if (employeeId) {
                    service.findPersonalEstablishment(targetYear, employeeId).done(function(data) {
                        self.personalEstablishmentModel.estimateTimeModel.updateData(data.estimateTime);
                        self.personalEstablishmentModel.estimatePriceModel.updateData(data.estimatePrice);
                        self.personalEstablishmentModel.estimateDaysModel.updateData(data.estimateNumberOfDay);
                        if (isLoading) {
                            self.isLoading(false);
                        }
                        self.updateEnableDeletePersonal(employeeCode);
                        self.initNextTabFeature();
                        nts.uk.ui.block.clear();
                        dfd.resolve();
                    });
                }
                else {
                    nts.uk.ui.block.clear();
                    dfd.resolve();
                }
                return dfd.promise();
            }
            /**
             * on click tab panel employment action event
             */
            public onSelectPerson(): void {
                $('.nts-input').ntsError('clear');
                var self = this;
                nts.uk.ui.block.invisible();
                self.employeeName = ko.observable('');
                self.isCompanySelected(false);
                self.isEmploymentSelected(false);
                self.isPersonSelected(true);
                self.isLoading(true);
                $('#ccgcomponent').ntsGroupComponent(self.ccgcomponentPerson).done(function() {
                    self.alreadySettingEmployment = ko.observableArray([]);
                    self.isShowNoSelectRow = ko.observable(false);
                    self.employeeList = ko.observableArray<UnitModel>([]);
                    self.applyKCP005ContentSearch([]);
                    self.viewDefaultPersonal();
                    $('#employeeSearch').ntsListComponent(self.lstPersonComponentOption).done(function() {

                        self.updatePersonalEstimateSetting(self.personalEstablishmentModel.selectedYear());
                        self.loadPersonalEstablishment(self.personalEstablishmentModel.selectedYear(), self.selectedEmployeeCode(), true);
                    });
                });
                
                
            }
            /**
             * Default personal data
             */
            private viewDefaultPersonal(): void {
                var self = this;
                var employeeDefault: string ="01";
                service.findPersonalEstablishment(moment().year(), employeeDefault).done(function(data) {
                    self.personalEstablishmentModel.estimateTimeModel.updateData(data.estimateTime);
                    self.personalEstablishmentModel.estimatePriceModel.updateData(data.estimatePrice);
                    self.personalEstablishmentModel.estimateDaysModel.updateData(data.estimateNumberOfDay);
                    self.isLoading(false);
                });
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
             * Find employment by code.
             */
            public findEmploymentByCode(employmentCode: string): UnitModel {
                var employment: UnitModel;
                var self = this;
                employment =_.find(self.employmentList(), item => item.code === employmentCode);
                return employment;
            }
            
            
            /**
             * find employee id in selected
             */
            public findEmployeeIdByCode(employeeCode: string): string{
                var self = this;
                var employeeId = '';
                for (var employee of self.selectedEmployee()) {
                    if(employee.employeeCode === employeeCode){
                        employeeId = employee.employeeId;
                    }
                }
                return employeeId;
            }
            /**
             * find employee code in selected
             */
            public findEmployeeCodeById(employeeId: string): string{
                var self = this;
                var employeeCode = '';
                for (var employee of self.selectedEmployee()) {
                    if(employee.employeeId === employeeId){
                        employeeCode = employee.employeeCode;
                    }
                }
                return employeeCode;
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
                
                if (dataList.length > 0) {
                    self.selectedEmployeeCode(dataList[0].employeeCode);
                }

                self.updatePersonalEstimateSetting(self.personalEstablishmentModel.selectedYear());
                self.lstPersonComponentOption = {
                    isShowAlreadySet: true,
                    isMultiSelect: false,
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.SELECT_FIRST_ITEM,
                    selectedCode: self.selectedEmployeeCode,
                    isDialog: false,
                    isShowNoSelectRow: false,
                    alreadySettingList: self.alreadySettingPersonal,
                    isShowWorkPlaceName: true,
                    isShowSelectAllButton: false,
                    maxWidth: 350,
                    maxRows: 12
                };

            }
            
            /**
           * get all employee id by search data CCG out put 
           */
            public getAllEmployeeIdBySearch(): string[] {
                var self = this;
                var employeeIds: string[] = [];
                for (var employeeSelect of self.employeeList()) {
                    employeeIds.push(employeeSelect.code);
                }
                return employeeIds;
            }
            
            /**
             * call service find all by employee id
             */
            public findAllByEmployeeIds(employeeIds: string[]): JQueryPromise<UnitAlreadySettingModel[]> {
                var dfd = $.Deferred();
                var dataRes: UnitAlreadySettingModel[] = [];
                dfd.resolve(dataRes);
                return dfd.promise();
            }
            
           /**
            * function on click saveCompanyEstablishment action
            */
            public saveCompanyEstablishment(): void {
                if( $('.nts-input').ntsError('hasError')) {
                    return;
                };
                nts.uk.ui.block.invisible();
                var self = this;    
                var dto: CompanyEstablishmentDto = {
                    estimateTime: self.companyEstablishmentModel.estimateTimeModel.toDto(),
                    estimatePrice: self.companyEstablishmentModel.estimatePriceModel.toDto(),
                    estimateNumberOfDay: self.companyEstablishmentModel.estimateDaysModel.toDto()
                    
                };
                service.saveCompanyEstablishment(self.companyEstablishmentModel.selectedYear(), dto).done(function(){
                    // show message 15
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                        // reload pa    
                        self.loadCompanyEstablishment(self.companyEstablishmentModel.selectedYear(), false);
                    });
                }).fail(function(error){
                    nts.uk.ui.dialog.alertError(error);    
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }
            
           /**
            * function on click deleteCompanyEstablishment action
            */
            public deleteCompanyEstablishment(): void {
                nts.uk.ui.block.invisible();
                var self = this;
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                    service.deleteCompanyEstablishment(self.selectedTargetYear()).done(function() {
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                            // reload page
                            self.loadCompanyEstablishment(self.selectedTargetYear(), false);
                        });
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alertError(error);
                    }).always(() => {
                        nts.uk.ui.block.clear();
                    });
                });
            }
            
           /**
            * function on click saveEmploymentEstablishment action
            */
            public saveEmploymentEstablishment(): void {
                if( $('.nts-input').ntsError('hasError')) {
                    return;
                };
                nts.uk.ui.block.invisible();
                var self = this;    
                var dto: EmploymentEstablishmentDto = {
                    estimateTime: self.employmentEstablishmentModel.estimateTimeModel.toDto(),
                    estimatePrice: self.employmentEstablishmentModel.estimatePriceModel.toDto(),
                    estimateNumberOfDay: self.employmentEstablishmentModel.estimateDaysModel.toDto(),
                    employmentCode: self.selectedEmploymentCode()
                };

                service.saveEmploymentEstablishment(self.employmentEstablishmentModel.selectedYear(), dto).done(function() {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                        self.updateEmploymentEstimateSetting(self.employmentEstablishmentModel.selectedYear());
                    });
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }
            
           /**
            * function on click deleteEmploymentEstablishment action
            */
            public deleteEmploymentEstablishment(): void {
                let self = this;
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(() => {
                    nts.uk.ui.block.invisible();
                    service.deleteEmploymentEstablishment(
                        self.employmentEstablishmentModel.selectedYear(),
                        self.selectedEmploymentCode()).done(function() {
                            nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                                self.updateEmploymentEstimateSetting(self.employmentEstablishmentModel.selectedYear());
                            });
                        }).always(() => {
                            nts.uk.ui.block.clear();
                        });
                });
            }
            /**
             * function check employee setting
             */
            private checkSettingPersonal(employeeCode: string): boolean {
                var self = this;
                for(var employee of self.alreadySettingPersonal()){
                    if(employee.code == employeeCode && employee.isAlreadySetting){
                        return true;    
                    }    
                }
                return false;
            }
           /**
            * function on click savePersonalEstablishment action
            */
            public savePersonalEstablishment(): void {
                if( $('.nts-input').ntsError('hasError')) {
                    return;
                };
                nts.uk.ui.block.invisible();
                var self = this;    
                var dto: PersonalEstablishmentDto = {
                    estimateTime: self.personalEstablishmentModel.estimateTimeModel.toDto(),
                    estimatePrice: self.personalEstablishmentModel.estimatePriceModel.toDto(),
                    estimateNumberOfDay: self.personalEstablishmentModel.estimateDaysModel.toDto(),
                    employeeId: self.findEmployeeIdByCode(self.selectedEmployeeCode())
                };
                service.savePersonalEstablishment(self.personalEstablishmentModel.selectedYear(), dto).done(function(){
                    // show message 15
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                        // reload page    
                        self.updatePersonalEstimateSetting(self.personalEstablishmentModel.selectedYear());
                        self.loadPersonalEstablishment(self.personalEstablishmentModel.selectedYear(),self.selectedEmployeeCode(), false);
                    });
                }).fail(function(error){
                   nts.uk.ui.dialog.alertError(error);     
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }
            
           /**
            * function on click deletePersonalEstablishment action
            */
            public deletePersonalEstablishment(): void {
                nts.uk.ui.block.invisible();
                var self = this;    
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                    service.deletePersonalEstablishment(self.personalEstablishmentModel.selectedYear(),self.findEmployeeIdByCode(self.selectedEmployeeCode())).done(function() {
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(function() {
                            // reload page
                            
                            self.loadPersonalEstablishment(self.personalEstablishmentModel.selectedYear(), self.selectedEmployeeCode(), false);
                            self.updatePersonalEstimateSetting(self.personalEstablishmentModel.selectedYear());
                        });
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alertError(error);
                    }).always(() => {
                        nts.uk.ui.block.clear();
                    });
                });
            }
            
            /**
             * open dialog UsageSettingModel (view model E)
             */
            private openDialogUsageSettingModel(): void {
                var self = this;
                nts.uk.ui.windows.sub.modal("/view/ksm/001/e/index.xhtml").onClosed(function() {
                    self.loadUsageSettingModel();
                });
            }
            
            /**
             * open dialog CommonSetting (view model F)
             */
            private openDialogCommonSetting(): void {
                var self = this;
                nts.uk.ui.windows.sub.modal("/view/ksm/001/f/index.xhtml").onClosed(function() {
                    
                });
            }
            
            /**
             * set next tab index
             */
             public initNextTabFeature() {
                let self = this;
                // Auto next tab when press tab key.
                $("[tabindex='75']").on('keydown', function(e) {
                    console.log('75');
                    if (e.which == 9) {
                        self.companyEstablishmentModel.selectedTab('tab-2');
                        self.employmentEstablishmentModel.selectedTab('tab-2');
                        self.personalEstablishmentModel.selectedTab('tab-2');
                    }
                });

                $("[tabindex='141']").on('keydown', function(e) {
                    console.log('141');
                    if (e.which == 9) {
                        self.companyEstablishmentModel.selectedTab('tab-3');
                        self.employmentEstablishmentModel.selectedTab('tab-3');
                        self.personalEstablishmentModel.selectedTab('tab-3');
                    }
                });
                $("[tabindex='9']").on('keydown', function(e) {
                    if (e.which == 9 && !$(e.target).parents("[tabindex='9']")[0]) {
                        self.companyEstablishmentModel.selectedTab('tab-1');
                        self.employmentEstablishmentModel.selectedTab('tab-1');
                        self.personalEstablishmentModel.selectedTab('tab-1');
                    }
                });
            }

        }

        export class EstimateTimeModel {
            month: KnockoutObservable<number>;
            time1st: KnockoutObservable<number>;
            time2nd: KnockoutObservable<number>;
            time3rd: KnockoutObservable<number>;
            time4th: KnockoutObservable<number>;
            time5th: KnockoutObservable<number>;

            constructor() {
                this.month = ko.observable(1);
                this.time1st = ko.observable(0);
                this.time2nd = ko.observable(0);
                this.time3rd = ko.observable(0);
                this.time4th = ko.observable(0);
                this.time5th = ko.observable(0);
            }

            updateData(dto: EstimateTimeDto) {
                this.month(dto.month);
                this.time1st(dto.time1st);
                this.time2nd(dto.time2nd);
                this.time3rd(dto.time3rd);
                this.time4th(dto.time4th);
                this.time5th(dto.time5th);
            }
            
            toDto(): EstimateTimeDto{
                var dto: EstimateTimeDto = {
                    month: this.month(),
                    time1st: this.time1st(),
                    time2nd: this.time2nd(),
                    time3rd: this.time3rd(),
                    time4th: this.time4th(),
                    time5th: this.time5th()
                }
                return dto; 
            }
        }
        export class EstimatePriceModel {
            month: KnockoutObservable<number>;
            price1st: KnockoutObservable<number>;
            price2nd: KnockoutObservable<number>;
            price3rd: KnockoutObservable<number>;
            price4th: KnockoutObservable<number>;
            price5th: KnockoutObservable<number>;

            constructor() {
                this.month = ko.observable(1);
                this.price1st = ko.observable(0);
                this.price2nd = ko.observable(0);
                this.price3rd = ko.observable(0);
                this.price4th = ko.observable(0);
                this.price5th = ko.observable(0);
            }

            updateData(dto: EstimatePriceDto) {
                this.month(dto.month);
                this.price1st(dto.price1st);
                this.price2nd(dto.price2nd);
                this.price3rd(dto.price3rd);
                this.price4th(dto.price4th);
                this.price5th(dto.price5th);
            }
            
            toDto(): EstimatePriceDto{
                var dto: EstimatePriceDto = {
                    month: this.month(),
                    price1st: this.price1st(),
                    price2nd: this.price2nd(),
                    price3rd: this.price3rd(),
                    price4th: this.price4th(),
                    price5th: this.price5th()
                }
                return dto; 
            }
        }
        export class EstimateDaysModel {
            month: KnockoutObservable<number>;
            days1st: KnockoutObservable<number>;
            days2nd: KnockoutObservable<number>;
            days3rd: KnockoutObservable<number>;
            days4th: KnockoutObservable<number>;
            days5th: KnockoutObservable<number>;

            constructor() {
                this.month = ko.observable(1);
                this.days1st = ko.observable(0);
                this.days2nd = ko.observable(0);
                this.days3rd = ko.observable(0);
                this.days4th = ko.observable(0);
                this.days5th = ko.observable(0);
            }

            updateData(dto: EstimateDaysDto) {
                this.month(dto.month);
                this.days1st(dto.days1st);
                this.days2nd(dto.days2nd);
                this.days3rd(dto.days3rd);
                this.days4th(dto.days4th);
                this.days5th(dto.days5th);
            }
            
            toDto(): EstimateDaysDto{
                var dto: EstimateDaysDto = {
                    month: this.month(),
                    days1st: this.days1st(),
                    days2nd: this.days2nd(),
                    days3rd: this.days3rd(),
                    days4th: this.days4th(),
                    days5th: this.days5th()
                }
                return dto; 
            }
        }
        
        export class EstablishmentTimeModel{
            monthlyEstimates: EstimateTimeModel[];
            yearlyEstimate: EstimateTimeModel;
            
            constructor(){
                this.monthlyEstimates = [];
                this.yearlyEstimate = new EstimateTimeModel();    
            }
            
            updateData(dto: EstablishmentTimeDto) {
                if (this.monthlyEstimates.length == 0) {
                    this.monthlyEstimates = [];
                    for (var item of dto.monthlyEstimates) {
                        var model: EstimateTimeModel = new EstimateTimeModel();
                        model.updateData(item);
                        this.monthlyEstimates.push(model);
                    }
                } else {
                    for (var itemDto of dto.monthlyEstimates) {
                        for(var model of this.monthlyEstimates){
                            if (itemDto.month == model.month()) {
                                model.updateData(itemDto);
                            }
                        }
                    }
                }
                this.yearlyEstimate.updateData(dto.yearlyEstimate);
            }
            
            toDto(): EstablishmentTimeDto{
                var monthlyEstimateTime: EstimateTimeDto[] = [];
                for (var item of this.monthlyEstimates) {
                    monthlyEstimateTime.push(item.toDto());
                }
                var dto: EstablishmentTimeDto = {
                    monthlyEstimates: monthlyEstimateTime,
                    yearlyEstimate: this.yearlyEstimate.toDto()
                };
                return dto;
            }
        }
        export class EstablishmentPriceModel{
            monthlyEstimates: EstimatePriceModel[];
            yearlyEstimate: EstimatePriceModel;
            
            constructor(){
                this.monthlyEstimates = [];
                this.yearlyEstimate = new EstimatePriceModel();    
            }
            
            updateData(dto: EstablishmentPriceDto) {
                if (this.monthlyEstimates.length == 0) {
                    this.monthlyEstimates = [];
                    for (var item of dto.monthlyEstimates) {
                        var model: EstimatePriceModel = new EstimatePriceModel();
                        model.updateData(item);
                        this.monthlyEstimates.push(model);
                    }
                } else {
                    for(var itemDto of dto.monthlyEstimates){
                        for(var model of this.monthlyEstimates){
                            if(model.month() == itemDto.month){
                                model.updateData(itemDto);    
                            }    
                        }    
                    }
                }
                this.yearlyEstimate.updateData(dto.yearlyEstimate);
            }
            
            toDto(): EstablishmentPriceDto{
                var monthlyEstimatePrice: EstimatePriceDto[] = [];
                for (var item of this.monthlyEstimates) {
                    monthlyEstimatePrice.push(item.toDto());
                }
                var dto: EstablishmentPriceDto = {
                    monthlyEstimates: monthlyEstimatePrice,
                    yearlyEstimate: this.yearlyEstimate.toDto()
                };
                return dto;
            }
        }
        
        export class EstablishmentDaysModel{
            monthlyEstimates: EstimateDaysModel[];
            yearlyEstimate: EstimateDaysModel;
            
            constructor(){
                this.monthlyEstimates = [];
                this.yearlyEstimate = new EstimateDaysModel();    
            }
            
            updateData(dto: EstablishmentDaysDto) {
                if (this.monthlyEstimates.length == 0) {
                    this.monthlyEstimates = [];
                    for (var item of dto.monthlyEstimates) {
                        var model: EstimateDaysModel = new EstimateDaysModel();
                        model.updateData(item);
                        this.monthlyEstimates.push(model);
                    }
                }else {
                    for(var itemDto of dto.monthlyEstimates){
                        for(var model of this.monthlyEstimates){
                            if(model.month() == itemDto.month){
                                model.updateData(itemDto);    
                            }    
                        }    
                    }    
                }
                this.yearlyEstimate.updateData(dto.yearlyEstimate);
            }
            
            toDto(): EstablishmentDaysDto{
                var monthlyEstimateDays: EstimateDaysDto[] = [];
                for (var item of this.monthlyEstimates) {
                    monthlyEstimateDays.push(item.toDto());
                }
                var dto: EstablishmentDaysDto = {
                    monthlyEstimates: monthlyEstimateDays,
                    yearlyEstimate: this.yearlyEstimate.toDto()
                };
                return dto;
            }
        }
        
        export class EstablishmentModel {
            estimateTimeModel: EstablishmentTimeModel;
            estimatePriceModel: EstablishmentPriceModel;
            estimateDaysModel: EstablishmentDaysModel;
            selectedTab: KnockoutObservable<string>;
            isEditable: KnockoutObservable<boolean>;
            isEnableDelete: KnockoutObservable<boolean>;
            selectedYear: KnockoutObservable<number>;
            constructor() {
                this.estimateTimeModel = new EstablishmentTimeModel();
                this.estimatePriceModel = new EstablishmentPriceModel();
                this.estimateDaysModel = new EstablishmentDaysModel();
                this.selectedTab = ko.observable('tab-1');
                this.isEditable = ko.observable(true);
                this.isEnableDelete = ko.observable(true);
                this.selectedYear = ko.observable(moment().year());
            }

            enableDelete(isDelete: boolean){
                this.isEnableDelete(isDelete);
            }
            enableInput(): void {
                this.isEditable(true);
            }
            disableInput(): void {
                this.isEditable(false);
            }
        }
        
        
        export class UsageSettingModel {
            settingEmployment: KnockoutObservable<boolean>;
            settingPersonal: KnockoutObservable<boolean>;
            constructor() {
                this.settingEmployment = ko.observable(true);
                this.settingPersonal = ko.observable(true);
            }
            updateData(dto: UsageSettingDto){
                this.settingEmployment(dto.employmentSetting);    
                this.settingPersonal(dto.personalSetting);    
            }
            
            toDto(): UsageSettingDto {
                var dto: UsageSettingDto = {
                    employmentSetting: this.settingEmployment(),
                    personalSetting: this.settingPersonal()
                };
                return dto;
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
    }
}