module nts.uk.at.view.kmk004.shr.worktime.setting {
    export module viewmodel {
        import UsageUnitSettingService = nts.uk.at.view.kmk004.e.service;
        
        export class ScreenModel {
            
            tabs: KnockoutObservableArray<NtsTabPanelModel>;
            baseDate: KnockoutObservable<Date>;
            
            usageUnitSetting: UsageUnitSetting;
            companyWTSetting: CompanyWTSetting;
            // Start month.
            startMonth: KnockoutObservable<number>;
            
            isNewMode: KnockoutObservable<boolean>;
            isLoading: KnockoutObservable<boolean>;
            
            aggrSelectionItemList: KnockoutObservableArray<any>;// FlexAggregateMethod フレックス集計方法
            selectedAggrSelection: KnockoutObservable<number>;
            
            worktimeSetting: WorktimeSetting;
            
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
                self.baseDate = ko.observable(new Date());
                
                self.worktimeSetting = new WorktimeSetting();
                
                // Data model.
                self.usageUnitSetting = new UsageUnitSetting();
                self.companyWTSetting = new CompanyWTSetting();
                
                // Update
                self.aggrSelectionItemList = ko.observableArray([
                    { id: 0, name: nts.uk.resource.getText("KMK004_51")},
                    { id: 1, name: nts.uk.resource.getText("KMK004_52")}
                ]);
                self.selectedAggrSelection = ko.observable(1);
                
                //==================UPDATE==================
                
            }
            
            /**
             * Initialize
             */
            public initialize(): JQueryPromise<void> {
                nts.uk.ui.block.invisible();
                let self = this;
                let dfd = $.Deferred<void>();
                
                self.loadUsageUnitSetting().done(() => {
                    viewmodel.getStartMonth().done((month) => {
                        self.startMonth = ko.observable(month);
                        
                        self.isLoading(true);
                        self.loadCompanySettingNewest().done(() => {
                            // Update flag.
                            self.isLoading(false);
                            $('#companyYearPicker').focus();
                            
                            dfd.resolve();
                        });
                    }).always(() => {
                        nts.uk.ui.block.clear();
                    });
                }).fail(() => {
                    nts.uk.ui.block.clear();
                });
                return dfd.promise();
            }
            
            public saveCompanySettingNewest(): void {
                let self = this;
                // Validate
                if (self.hasError()) {
                    return;
                }
                
                let saveCommand: WorktimeSettingDtoSaveCommand = new WorktimeSettingDtoSaveCommand();
                saveCommand.updateData(self.worktimeSetting);
                service.saveCompanySetting(ko.toJS(saveCommand)).done(() => {
                    self.isNewMode(false);
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                }).fail(error => {
                    nts.uk.ui.dialog.alertError(error);
                });
            }
            
            /**
             * LOAD WORKTIMESETTING (NEWEST)
             */
            public loadCompanySettingNewest(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
//                if (self.isCompanySelected()) {
                    // Find CompanySetting
                    service.findCompanySetting(self.worktimeSetting.normalSetting().year()).done(function(data: WorktimeSettingDto) {
                        // Clear Errors
                        self.clearError();
                        // update mode.
                        // Check condition: ドメインモデル「会社別通常勤務労働時間」を取得する
                        if (data.statWorkTimeSetDto && data.statWorkTimeSetDto.regularLaborTime) {
                            self.isNewMode(false);
                            // Update Full Data
                            self.worktimeSetting.updateFullData(data);
                            self.worktimeSetting.updateYear(data.statWorkTimeSetDto.year);
                        }
                        else {
                            // new mode.
                            self.isNewMode(true);
                            let newSetting = new WorktimeSettingDto();
                            // Reserve selected year.
                            newSetting.updateYear(self.worktimeSetting.normalSetting().year());
                            // Update Full Data
                            self.worktimeSetting.updateFullData(ko.toJS(newSetting));
                        }
                        // Sort month.
                        self.worktimeSetting.sortMonth(self.startMonth());
                        dfd.resolve();
                    });
//                }
                
                return dfd.promise();
            }
            
            /**
             * Remove company setting.
             */
            public removeCompanySetting(): void {
                let self = this;
                if ($('#companyYearPicker').ntsError('hasError')) {
                    return;
                }
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                    let selectedYear = self.companyWTSetting.year();
                    let command = { year: selectedYear }
                    service.removeCompanySetting(command).done(() => {
                        // Reserve current year.
                        let newSetting = new CompanyWTSetting();
                        newSetting.year(selectedYear);
                        self.companyWTSetting.updateData(ko.toJS(newSetting));
                        // Sort month.
                        self.companyWTSetting.sortMonth(self.startMonth());
                        self.isNewMode(true);
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                    }).fail(error => {
                        nts.uk.ui.dialog.alertError(error);
                    }).always(() => {
                        self.clearError();
                    });
                }).ifNo(function() {
                    nts.uk.ui.block.clear();
                    return;
                })
            }
            
            /**
             * Load usage unit setting.
             */
            public loadUsageUnitSetting(): JQueryPromise<any> {
                let self = this;
                let dfd = $.Deferred<any>();
                UsageUnitSettingService.findUsageUnitSetting().done(function(res: UsageUnitSettingService.model.UsageUnitSettingDto) {
                    self.usageUnitSetting.employee(res.employee);
                    self.usageUnitSetting.employment(res.employment);
                    self.usageUnitSetting.workplace(res.workPlace);
                    dfd.resolve();
                });
                return dfd.promise();
            }
            
            /**
             * Set start month.
             */
            private setStartMonth(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
                service.getStartMonth().done(res => {
                    if (res.startMonth) {
                        self.startMonth = ko.observable(res.startMonth);
                    } else {
                        // Default startMonth..
                        self.startMonth = ko.observable(1);
                    }
                    dfd.resolve();
                });
                return dfd.promise();
            }
            
            /**
             * Check validate all input.
             */
            private hasError(): boolean {
                return $('.nts-editor').ntsError('hasError');
            }
            
            /**
             * Clear all errors.
             */
            private clearError(): void {
                let self = this;
                if (nts.uk.ui._viewModel) {
                    // Reset year if has error.
                    if ($('#companyYearPicker').ntsError('hasError')) {
                        self.companyWTSetting.year(new Date().getFullYear());
                    }
                    // Clear error inputs
                    $('.nts-input').ntsError('clear');
                }
            }
            
            /**
             * Go to screen E (usage unit setting).
             */
            public gotoE(): void {
                let self = this;
                nts.uk.ui.windows.sub.modal("/view/kmk/004/e/index.xhtml").onClosed(() => {
                    self.loadUsageUnitSetting();
                    $('#companyYearPicker').focus();
                });
            }
            
            
        } // --- end ScreenModel
        
        export function getStartMonth(): JQueryPromise<number> {
            let self = this;
            let dfd = $.Deferred<number>();
            service.getStartMonth().done(res => {
                let month = 1;
                if (res.startMonth) {
                    month = res.startMonth;
                }
                dfd.resolve(month);
            });
            return dfd.promise();
        }
        
        export class UsageUnitSetting {
            employee: KnockoutObservable<boolean>;
            employment: KnockoutObservable<boolean>;
            workplace: KnockoutObservable<boolean>;

            constructor() {
                let self = this;
                self.employee = ko.observable(true);
                self.employment = ko.observable(true);
                self.workplace = ko.observable(true);
            }
        }
        
        export class CompanyWTSetting {
            deformationLaborSetting: DeformationLaborSetting;
            flexSetting: FlexSetting;
            normalSetting: NormalSetting;
            year: KnockoutObservable<number>;
            selectedTab: KnockoutObservable<string>;
            // Update
    
            constructor() {
                let self = this;
                self.selectedTab = ko.observable('tab-1');
                self.year = ko.observable(new Date().getFullYear());
                self.deformationLaborSetting = new DeformationLaborSetting();
                self.flexSetting = new FlexSetting();
                self.normalSetting = new NormalSetting();
            }
    
            public updateData(dto: any): void {
                let self = this;
                self.year(dto.year);
                self.normalSetting.updateData(dto.normalSetting);
                self.deformationLaborSetting.updateData(dto.deformationLaborSetting);
                self.flexSetting.updateData(dto.flexSetting);
            }
            public sortMonth(startMonth: number): void {
                let self = this;
                self.normalSetting.statutorySetting.sortMonth(startMonth);
                self.deformationLaborSetting.statutorySetting.sortMonth(startMonth);
                self.flexSetting.sortMonth(startMonth);
            }
        }
        /**
             * Company Worktime Setting (Tab 1)
             */
        export class WorktimeSetting {
            // Selected Tab
            selectedTab: KnockoutObservable<string>;
            // 会社別通常勤務労働時間
            normalWorktime: KnockoutObservable<NormalWorktime>;
            // 会社別変形労働労働時間
            deformLaborWorktime: KnockoutObservable<NormalWorktime>;
    
            // 会社別通常勤務月間労働時間
            normalSetting: KnockoutObservable<WorktimeNormalDeformSetting>;
            // 会社別フレックス勤務月間労働時間
            //            flexSetting: KnockoutObservable<WorktimeFlexSetting>;
            flexSetting: KnockoutObservable<WorktimeFlexSetting1>;
            // 会社別変形労働月間労働時間
            deformLaborSetting: KnockoutObservable<WorktimeNormalDeformSetting>;
    
            // Details
            // 通常勤務労働会社別月別実績集計設定
            normalAggrSetting: KnockoutObservable<NormalWorktimeAggrSetting>;
            // 変形労働会社別月別実績集計設定
            deformAggrSetting: KnockoutObservable<DeformWorktimeAggrSetting>;
            // フレックス会社別月別実績集計設定
            flexAggrSetting: KnockoutObservable<FlexWorktimeAggrSetting>;
    
            constructor() {
                let self = this;
                self.selectedTab = ko.observable('tab-1');
                self.normalWorktime = ko.observable(new NormalWorktime());
                self.deformLaborWorktime = ko.observable(new NormalWorktime());
    
                self.normalSetting = ko.observable(new WorktimeNormalDeformSetting());
                self.flexSetting = ko.observable(new WorktimeFlexSetting1());
                self.deformLaborSetting = ko.observable(new WorktimeNormalDeformSetting());
    
                self.normalAggrSetting = ko.observable(new NormalWorktimeAggrSetting());
                self.deformAggrSetting = ko.observable(new DeformWorktimeAggrSetting());
                self.flexAggrSetting = ko.observable(new FlexWorktimeAggrSetting());
    
            }
    
            public sortMonth(startMonth: number): void {
                let self = this;
                self.normalSetting().sortMonth(startMonth);
                self.flexSetting().sortMonth(startMonth);
                self.deformLaborSetting().sortMonth(startMonth);
            }
            //WorktimeSettingDto
            public updateDataDependOnYear(dto: StatutoryWorktimeSettingDto): void {
                let self = this;
                self.normalWorktime().updateData(dto.regularLaborTime);
                self.deformLaborWorktime().updateData(dto.transLaborTime);
    
                self.normalSetting().updateData(dto);
                self.flexSetting().updateData(dto);
                self.deformLaborSetting().updateData(dto);
            }
    
            public updateDetailData(dto: MonthlyCalSettingDto): void {
                let self = this;
                // Update Detail Data
                self.normalAggrSetting().updateData(dto.regAggrSetting);
                self.deformAggrSetting().updateData(dto.deforAggrSetting);
                self.flexAggrSetting().updateData(dto.flexAggrSetting);
            }
            // Update Full Data: 8 Models
            public updateFullData(dto: WorktimeSettingDto): void {
                let self = this;
                self.updateDataDependOnYear(dto.statWorkTimeSetDto);
                self.updateDetailData(dto.monthCalSetDto);
            }
    
            public updateYear(year: number): void {
                let self = this;
                self.normalSetting().year(year);
                self.flexSetting().year(year);
                self.deformLaborSetting().year(year);
            }
    
            public gotoF(): void {
                let self = this;
                let params: NormalSetParams = new NormalSetParams();
                // F1_2
                params.startWeek = self.normalWorktime().startWeek();
                // F2_3
                params.isIncludeExtraAggr = self.normalAggrSetting().aggregateOutsideTimeSet.includeExtra();
                // F2_8
                params.isIncludeLegalAggr = self.normalAggrSetting().aggregateOutsideTimeSet.includeLegal();
                // F2_12
                params.isIncludeHolidayAggr = self.normalAggrSetting().aggregateOutsideTimeSet.includeHoliday();
    
                // F2_16
                params.isIncludeExtraExcessOutside = self.normalAggrSetting().excessOutsideTimeSet.includeExtra();
                // F2_21
                params.isIncludeLegalExcessOutside = self.normalAggrSetting().excessOutsideTimeSet.includeLegal();
                // F2_25
                params.isIncludeHolidayExcessOutside = self.normalAggrSetting().excessOutsideTimeSet.includeHoliday();
    
                nts.uk.ui.windows.setShared('NORMAL_SET_PARAM', params, true);
    
                nts.uk.ui.windows.sub.modal("/view/kmk/004/f/index.xhtml").onClosed(() => {
                    $('#companyYearPicker').focus();
    
                    // Get params
                    var normalSetOutput: NormalSetParams = nts.uk.ui.windows.getShared('NORMAL_SET_OUTPUT');
                    // If normalSetOutput is undefined
                    if (!normalSetOutput) {
                        return;
                    } else {
                        self.normalWorktime().startWeek(normalSetOutput.startWeek);
                        self.normalAggrSetting().aggregateOutsideTimeSet.includeExtra(normalSetOutput.isIncludeExtraAggr);
                        self.normalAggrSetting().aggregateOutsideTimeSet.includeLegal(normalSetOutput.isIncludeLegalAggr);
                        self.normalAggrSetting().aggregateOutsideTimeSet.includeHoliday(normalSetOutput.isIncludeHolidayAggr);
    
                        // F2_16
                        self.normalAggrSetting().excessOutsideTimeSet.includeExtra(normalSetOutput.isIncludeExtraExcessOutside);
                        // F2_21
                        self.normalAggrSetting().excessOutsideTimeSet.includeLegal(normalSetOutput.isIncludeLegalExcessOutside);
                        // F2_25
                        self.normalAggrSetting().excessOutsideTimeSet.includeHoliday(normalSetOutput.isIncludeHolidayExcessOutside);
                    }
                });
            }
    
            public gotoG(): void {
                let self = this;
                let params: FlexSetParams = new FlexSetParams();
                params.isIncludeOverTime = self.flexAggrSetting().includeOT(); // G1_2
                params.shortageSetting = self.flexAggrSetting().shortageSetting();// G2_2
    
                nts.uk.ui.windows.setShared('FLEX_SET_PARAM', params, true);
    
                nts.uk.ui.windows.sub.modal("/view/kmk/004/g/index.xhtml").onClosed(() => {
                    $('#companyYearPicker').focus();
    
                    // get params from dialog 
                    var flexSetOutput: FlexSetParams = nts.uk.ui.windows.getShared('FLEX_SET_OUTPUT');
                    // If FLEXSetOutput is undefined
                    if (!flexSetOutput) {
                        return;
                    } else {
                        self.flexAggrSetting().includeOT(flexSetOutput.isIncludeOverTime);
                        self.flexAggrSetting().shortageSetting(flexSetOutput.shortageSetting);
                    }
                });
            }
    
    
            public gotoH(): void {
    
                let self = this;
                let params: DeformSetParams = new DeformSetParams();
    
                params.strMonth = self.deformAggrSetting().startMonth();
                params.period = self.deformAggrSetting().period();
                params.repeatCls = self.deformAggrSetting().repeatCls();
                // H1_1
                params.startWeek = self.deformLaborWorktime().startWeek();
                // // H3_3
                params.isIncludeExtraAggr = self.deformAggrSetting().aggregateOutsideTimeSet.includeExtra();
                // H3_8
                params.isIncludeLegalAggr = self.deformAggrSetting().aggregateOutsideTimeSet.includeLegal();
                // H3_12
                params.isIncludeHolidayAggr = self.deformAggrSetting().aggregateOutsideTimeSet.includeHoliday();
    
                // H3_16
                params.isIncludeExtraExcessOutside = self.deformAggrSetting().excessOutsideTimeSet.includeExtra();
                // H3_21
                params.isIncludeLegalExcessOutside = self.deformAggrSetting().excessOutsideTimeSet.includeLegal();
                // H3_25
                params.isIncludeHolidayExcessOutside = self.deformAggrSetting().excessOutsideTimeSet.includeHoliday();
    
                nts.uk.ui.windows.setShared('DEFORM_SET_PARAM', params, true);
    
                nts.uk.ui.windows.sub.modal("/view/kmk/004/h/index.xhtml").onClosed(() => {
                    $('#companyYearPicker').focus();
    
                    // Get params
                    var deformSetOutput: DeformSetParams = nts.uk.ui.windows.getShared('DEFORM_SET_OUTPUT');
                    // If deformSetOutput is undefined
                    if (!deformSetOutput) {
                        return;
                    } else {
                        self.deformAggrSetting().startMonth(deformSetOutput.strMonth);
                        self.deformAggrSetting().period(deformSetOutput.period);
                        self.deformAggrSetting().repeatCls(deformSetOutput.repeatCls);
                        //
                        self.deformLaborWorktime().startWeek(deformSetOutput.startWeek);
                        self.deformAggrSetting().aggregateOutsideTimeSet.includeExtra(deformSetOutput.isIncludeExtraAggr);
                        self.deformAggrSetting().aggregateOutsideTimeSet.includeLegal(deformSetOutput.isIncludeLegalAggr);
                        self.deformAggrSetting().aggregateOutsideTimeSet.includeHoliday(deformSetOutput.isIncludeHolidayAggr);
    
                        self.deformAggrSetting().excessOutsideTimeSet.includeExtra(deformSetOutput.isIncludeExtraExcessOutside);
                        self.deformAggrSetting().excessOutsideTimeSet.includeLegal(deformSetOutput.isIncludeLegalExcessOutside);
                        self.deformAggrSetting().excessOutsideTimeSet.includeHoliday(deformSetOutput.isIncludeHolidayExcessOutside);
                    }
                });
            }
        }
    
        export class DeformationLaborSetting {
            statutorySetting: WorkingTimeSetting;
            weekStart: KnockoutObservable<number>;
    
            constructor() {
                let self = this;
                self.statutorySetting = new WorkingTimeSetting();
                self.weekStart = ko.observable(0);
            }
    
            public updateData(dto: any): void {
                let self = this;
                self.weekStart(dto.weekStart);
                self.statutorySetting.updateData(dto.statutorySetting);
            }
        }
    
        export class FlexSetting {
            flexDaily: FlexDaily;
            flexMonthly: KnockoutObservableArray<FlexMonth>;
    
            constructor() {
                let self = this;
                self.flexDaily = new FlexDaily();
                self.flexMonthly = ko.observableArray<FlexMonth>([]);
                for (let i = 1; i < 13; i++) {
                    let flm = new FlexMonth();
                    flm.speName(nts.uk.resource.getText("KMK004_21", [i]));
                    flm.staName(nts.uk.resource.getText("KMK004_22", [i]));
                    flm.month(i);
                    flm.statutoryTime(0);
                    flm.specifiedTime(0);
                    self.flexMonthly.push(flm);
                }
            }
            public updateData(dto: any): void {
                let self = this;
                self.flexDaily.updateData(dto.flexDaily);
                self.flexMonthly().forEach(i => {
                    let updatedData = dto.flexMonthly.filter(j => i.month() == j.month)[0];
                    i.updateData(updatedData.statutoryTime, updatedData.specifiedTime);
                });
            }
            public sortMonth(startMonth: number): void {
                let self = this;
                let sortedList: Array<any> = new Array<any>();
                for (let i = 0; i < 12; i++) {
                    if (startMonth > 12) {
                        // reset month.
                        startMonth = 1;
                    }
                    let value = self.flexMonthly().filter(m => startMonth == m.month())[0];
                    sortedList.push(value);
                    startMonth++;
                }
                self.flexMonthly(sortedList);
            }
        }
    
        export class FlexDaily {
            statutoryTime: KnockoutObservable<number>;
            specifiedTime: KnockoutObservable<number>;
            constructor() {
                let self = this;
                self.statutoryTime = ko.observable(0);
                self.specifiedTime = ko.observable(0);
            }
            public updateData(dto: any): void {
                let self = this;
                self.statutoryTime(dto.statutoryTime);
                self.specifiedTime(dto.specifiedTime);
            }
        }
    
        export class FlexMonth {
            month: KnockoutObservable<number>;
            speName: KnockoutObservable<string>;
            staName: KnockoutObservable<string>;
            statutoryTime: KnockoutObservable<number>;
            specifiedTime: KnockoutObservable<number>;
            constructor() {
                let self = this;
                self.month = ko.observable(0);
                self.speName = ko.observable('');
                self.staName = ko.observable('');
                self.statutoryTime = ko.observable(0);
                self.specifiedTime = ko.observable(0);
            }
            public updateData(statutoryTime: number, specifiedTime: number): void {
                let self = this;
                self.statutoryTime(statutoryTime);
                self.specifiedTime(specifiedTime);
            }
        }
    
        export class NormalSetting {
            statutorySetting: WorkingTimeSetting;
            weekStart: KnockoutObservable<number>;
    
            constructor() {
                let self = this;
                self.statutorySetting = new WorkingTimeSetting();
                self.weekStart = ko.observable(0);
            }
    
            public updateData(dto: any): void {
                let self = this;
                self.weekStart(dto.weekStart);
                self.statutorySetting.updateData(dto.statutorySetting);
            }
        }
    
        export class WorkingTimeSetting {
            daily: KnockoutObservable<number>;
            monthly: KnockoutObservableArray<Monthly>;
            weekly: KnockoutObservable<number>;
    
            constructor() {
                let self = this;
                self.daily = ko.observable(0);
                self.weekly = ko.observable(0);
                self.monthly = ko.observableArray<Monthly>([]);
                for (let i = 1; i < 13; i++) {
                    let m = new Monthly();
                    m.month(i);
                    m.normal(nts.uk.resource.getText("KMK004_14", [i]));
                    m.deformed(nts.uk.resource.getText("KMK004_26", [i]));
                    self.monthly.push(m);
                }
            }
            public updateData(dto: any): void {
                let self = this;
                self.daily(dto.daily);
                self.weekly(dto.weekly);
                self.monthly().forEach(i => {
                    let updatedData = dto.monthly.filter(j => i.month() == j.month)[0];
                    i.updateData(updatedData.time);
                });
            }
            public sortMonth(startMonth: number): void {
                let self = this;
                let sortedList: Array<any> = new Array<any>();
                for (let i = 0; i < 12; i++) {
                    if (startMonth > 12) {
                        // reset month.
                        startMonth = 1;
                    }
                    let value = self.monthly().filter(m => startMonth == m.month())[0];
                    sortedList.push(value);
                    startMonth++;
                }
                self.monthly(sortedList);
            }
        }
    
        export class Monthly {
            month: KnockoutObservable<number>;
            time: KnockoutObservable<number>;
            normal: KnockoutObservable<string>;
            deformed: KnockoutObservable<string>;
    
            constructor() {
                let self = this;
                self.time = ko.observable(0);
                self.month = ko.observable(0);
                self.normal = ko.observable('');
                self.deformed = ko.observable('');
            }
    
            public updateData(time: number): void {
                let self = this;
                self.time(time);
            }
        }
    
        export class WorktimeSettingDto {
            // Cuc 5 cais
            statWorkTimeSetDto: StatutoryWorktimeSettingDto;
            // Cuc 3 cai
            monthCalSetDto: MonthlyCalSettingDto;
    
            constructor() {
                let self = this;
                self.statWorkTimeSetDto = new StatutoryWorktimeSettingDto();
                self.monthCalSetDto = new MonthlyCalSettingDto();
            }
    
            public updateYear(year: number): void {
                let self = this;
                self.statWorkTimeSetDto.year = year;
            }
    
            public updateData(model: WorktimeSetting): void {
                let self = this;
                self.statWorkTimeSetDto.updateData(model);
                self.monthCalSetDto.updateData(model);
            }
        }
    
        export class WorktimeSettingDtoSaveCommand {
    
            /** The save com stat work time set command. */
            saveStatCommand: StatutoryWorktimeSettingDto;;
    
            /** The save com flex command. */
            saveMonthCommand: MonthlyCalSettingDto;
    
            constructor() {
                let self = this;
                self.saveStatCommand = new StatutoryWorktimeSettingDto();
                self.saveMonthCommand = new MonthlyCalSettingDto();
            }
    
            public updateYear(year: number): void {
                let self = this;
                self.saveStatCommand.year = year;
            }
    
            public updateData(model: WorktimeSetting): void {
                let self = this;
                self.saveStatCommand.updateData(model);
                self.saveMonthCommand.updateData(model);
            }
        }
    
    
        export class StatutoryWorktimeSettingDto {
            year: number;
            // 会社別通常勤務労働時間
            regularLaborTime: NormalWorktimeDto;
            // 会社別変形労働労働時間
            transLaborTime: NormalWorktimeDto;
    
            // 会社別通常勤務月間労働時間
            normalSetting: WorktimeNormalDeformSettingDto;
            // 会社別フレックス勤務月間労働時間
            //            flexSetting: KnockoutObservable<WorktimeFlexSetting>;
            flexSetting: WorktimeFlexSetting1Dto;
            // 会社別変形労働月間労働時間
            deforLaborSetting: WorktimeNormalDeformSettingDto;
    
            constructor() {
                let self = this;
                self.regularLaborTime = new NormalWorktimeDto();
                self.transLaborTime = new NormalWorktimeDto();
    
                self.normalSetting = new WorktimeNormalDeformSettingDto();
                self.flexSetting = new WorktimeFlexSetting1Dto();
                self.deforLaborSetting = new WorktimeNormalDeformSettingDto();
            }
    
            // WorktimeNormalDeformSetting
            public updateData(model: WorktimeSetting): void {
                let self = this;
                self.year = model.normalSetting().year();
                self.regularLaborTime.updateData(model.normalWorktime());
                self.transLaborTime.updateData(model.deformLaborWorktime());
                self.normalSetting.updateData(model.normalSetting());
                self.flexSetting.updateData(model.flexSetting());
                self.deforLaborSetting.updateData(model.deformLaborSetting());
            }
        }
    
        export class MonthlyCalSettingDto {
            // Details
            // 通常勤務労働会社別月別実績集計設定
            regAggrSetting: NormalWorktimeAggrSettingDto;
            // 変形労働会社別月別実績集計設定
            deforAggrSetting: DeformWorktimeAggrSettingDto;
            // フレックス会社別月別実績集計設定
            flexAggrSetting: FlexWorktimeAggrSettingDto;
    
            constructor() {
                let self = this;
                self.regAggrSetting = new NormalWorktimeAggrSettingDto();
                self.deforAggrSetting = new DeformWorktimeAggrSettingDto();
                self.flexAggrSetting = new FlexWorktimeAggrSettingDto();
            }
    
            public updateData(model: WorktimeSetting): void {
                let self = this;
                self.regAggrSetting.updateData(model.normalAggrSetting());
                self.flexAggrSetting.updateData(model.flexAggrSetting());
                self.deforAggrSetting.updateData(model.deformAggrSetting());
            }
        }
    
        export class NormalWorktimeDto {
            // 会社別通常勤務労働時間
            dailyTime: DailyUnitDto;
            weeklyTime: WeeklyUnitDto;
            //            startWeek: KnockoutObservable<nu        
            constructor() {
                let self = this;
                self.dailyTime = new DailyUnitDto();
                self.weeklyTime = new WeeklyUnitDto();
                //                self.startWeek = ko.observable(StartWeek.SUNDAY);
            }
    
            public updateData(model: NormalWorktime): void {
                let self = this;
                self.dailyTime.dailyTime = model.dailyTime();
                self.weeklyTime.time = model.weeklyTime();
                self.weeklyTime.start = model.startWeek();
            }
        }
    
        export class DailyUnitDto {
            dailyTime: number;
            constructor() {
                let self = this;
                self.dailyTime = 0;
            }
        }
    
        export class WeeklyUnitDto {
            time: number;
            start: number;
            constructor() {
                let self = this;
                self.time = 0;
                self.start = 0;
            }
        }
    
        /**
         * 会社別通常勤務月間労働時間
         * 会社別変形労働月間労働時間
         */
        export class WorktimeNormalDeformSettingDto {
            //            year: KnockoutObservable<number>;
            // 法定時間: 月単位
            statutorySetting: MonthlyUnitDto[];
    
            constructor() {
                let self = this;
                //                self.year = ko.observable(new Date().getFullYear());
                self.statutorySetting = [];
                for (let i = 1; i < 13; i++) {
                    let m = new MonthlyUnitDto();
                    m.month = i;
                    m.monthlyTime = 0;
                    self.statutorySetting.push(m);
                }
            }
    
            public updateData(model: WorktimeNormalDeformSetting): void {
                let self = this;
                self.statutorySetting.forEach(i => {
                    let updatedData: MonthlyTime = model.statutorySetting().filter(j => i.month == j.month())[0];
                    i.updateData(updatedData);
                });
            }
    
            //            public sortMonth(startMonth: number): void {
            //                let self = this;
            //                let sortedList: Array<any> = new Array<any>();
            //                let flexSortedList: Array<any> = new Array<any>();
            //                for (let i = 0; i < 12; i++) {
            //                    if (startMonth > 12) {
            //                        // reset month.
            //                        startMonth = 1;
            //                    }
            //                    let value = self.statutorySetting().filter(m => startMonth == m.month())[0];
            //                    sortedList.push(value);
            //                    startMonth++;
            //                }
            //                self.statutorySetting(sortedList);
            //            }
        }
    
        /**
         * MonthlyTime 月単位
         */
        export class MonthlyUnitDto {
            month: number;
            monthlyTime: number;
    
            constructor() {
                let self = this;
                self.month = new Date().getMonth();
                self.monthlyTime = 0;
            }
    
            public updateData(model: MonthlyTime): void {
                let self = this;
                self.month = model.month();
                self.monthlyTime = model.time();
            }
        }
    
    
        /**
         * 会社別フレックス勤務月間労働時間
         */
        export class WorktimeFlexSetting1Dto {
            // 法定時間: 月単位
            statutorySetting: MonthlyUnitDto[];
            // 法定時間: 月単位
            specifiedSetting: MonthlyUnitDto[];
    
            constructor() {
                let self = this;
                //                self.year = ko.observable(new Date().getFullYear());
                self.statutorySetting = [];
                self.specifiedSetting = [];
                for (let i = 1; i < 13; i++) {
                    let m = new MonthlyUnitDto();
                    m.month = i;
                    m.monthlyTime = 0;
                    self.statutorySetting.push(m);
                    self.specifiedSetting.push(m);
                }
            }
    
            public updateData(model: WorktimeFlexSetting1): void {
                let self = this;
                self.statutorySetting.forEach(i => {
                    let flexData: FlexMonthlyTime = model.flexSettingDetail().filter(j => i.month == j.month())[0];
                    let updatedData: MonthlyTime = new MonthlyTime();
                    updatedData.month(flexData.month());
                    updatedData.time(flexData.statutoryTime());
    
                    i.updateData(updatedData);
                });
    
                self.specifiedSetting.forEach(i => {
                    let flexData: FlexMonthlyTime = model.flexSettingDetail().filter(j => i.month == j.month())[0];
                    let updatedData: MonthlyTime = new MonthlyTime();
                    updatedData.month(flexData.month());
                    updatedData.time(flexData.specifiedTime());
                    i.updateData(updatedData);
                });
            }
            //            public sortMonth(startMonth: number): void {
            //                let self = this;
            //                let sortedList: Array<any> = new Array<any>();
            //                let flexSortedList: Array<any> = new Array<any>();
            //                for (let i = 0; i < 12; i++) {
            //                    if (startMonth > 12) {
            //                        // reset month.
            //                        startMonth = 1;
            //                    }
            //                    let flexValue = self.flexSettingDetail().filter(m => startMonth == m.month())[0];
            //                    flexSortedList.push(flexValue);
            //                    startMonth++;
            //                }
            //                self.flexSettingDetail(flexSortedList);
            //            }
        }
    
        //        export class FlexMonthlyTimeDto {
        //            month: KnockoutObservable<number>;
        //            statutoryTime: KnockoutObservable<number>;
        ////            specifiedMonth: KnockoutObservable<number>;
        //            specifiedTime: KnockoutObservable<number>;
        //            constructor() {
        //                let self = this;
        //                self.month = ko.observable(new Date().getMonth());
        //                self.statutoryTime = ko.observable(0);
        //                self.specifiedTime = ko.observ
        //            }
        //        }
    
        /**
         * 通常勤務の法定内集計設定
         */
        export class NormalWorktimeAggrSettingDto {
            // 時間外超過設定: 割増集計方法
            excessOutsideTimeSet: ExcessOutsideTimeSetDto;
            // 集計時間設定: 割増集計方法
            aggregateTimeSet: ExcessOutsideTimeSetDto;
    
            constructor() {
                let self = this;
                self.excessOutsideTimeSet = new ExcessOutsideTimeSetDto();
                self.aggregateTimeSet = new ExcessOutsideTimeSetDto();
            }
            // NormalWorktimeAggrSetting
            public updateData(model: NormalWorktimeAggrSetting): void {
                let self = this;
                self.excessOutsideTimeSet.updateData(model.excessOutsideTimeSet);
                self.aggregateTimeSet.updateData(model.aggregateOutsideTimeSet);
            }
        }
    
        /**
         * 割増集計方法
         */
        export class ExcessOutsideTimeSetDto {
            legalOverTimeWork: boolean;
            legalHoliday: boolean;
            surchargeWeekMonth: boolean;
            constructor() {
                let self = this;
                self.legalOverTimeWork = false;
                self.legalHoliday = false;
                self.surchargeWeekMonth = false;
            }
    
            // ExcessOutsideTimeSet
            public updateData(model: ExcessOutsideTimeSet): void {
                let self = this;
                self.legalOverTimeWork = model.includeLegal();
                self.legalHoliday = model.includeHoliday();
                self.surchargeWeekMonth = model.includeExtra();
            }
        }
    
        /**
         * 変形労働時間勤務の法定内集計設定
         */
        export class DeformWorktimeAggrSettingDto {
            // 通常勤務労働会社別月別実績集計設定
            excessOutsideTimeSet: ExcessOutsideTimeSetDto;
            aggregateTimeSet: ExcessOutsideTimeSetDto;
    
            isOtTransCriteria: boolean;
            settlementPeriod: DeforLaborSettlementPeriodDto;
    
            constructor() {
                let self = this;
                self.excessOutsideTimeSet = new ExcessOutsideTimeSetDto();
                self.aggregateTimeSet = new ExcessOutsideTimeSetDto();
    
                self.isOtTransCriteria = false;
                self.settlementPeriod = new DeforLaborSettlementPeriodDto();
            }
    
            public updateData(model: DeformWorktimeAggrSetting): void {
                let self = this;
                self.excessOutsideTimeSet.updateData(model.excessOutsideTimeSet);
                self.aggregateTimeSet.updateData(model.aggregateOutsideTimeSet);
                self.isOtTransCriteria = model.isDeformedOT();
                self.settlementPeriod.updateData(model);
    
            }
        }
    
        export class DeforLaborSettlementPeriodDto {
            /** The start month. */
            startMonth: number;
    
            /** The period. */
            period: number;
    
            /** The repeat atr. */
            repeatAtr: boolean;
    
            constructor() {
                let self = this;
                self.period = 1
                self.repeatAtr = false;
                self.startMonth = new Date().getMonth();
            }
    
            public updateData(model: DeformWorktimeAggrSetting): void {
                let self = this;
                self.startMonth = model.startMonth();
                self.period = model.period();
                self.repeatAtr = model.repeatCls();
            }
        }
    
        /**
         * フレックス時間勤務の月の集計設定
         */
        export class FlexWorktimeAggrSettingDto {
            // 不足設定: フレックス不足設定// G2_2
            insufficSet: number;
            // 残業時間を含める: するしない区分//// G1_2
            includeOverTime: boolean;
            // 法定内集計設定: 法定内フレックス時間集計
            legalAggrSet: number;
            // 集計方法: フレックス集計方法//
            aggrMethod: number;
    
            constructor() {
                let self = this;
                self.insufficSet = ShortageSetting.CURRENT_MONTH_INTEGRATION;
                self.includeOverTime = false;
                self.legalAggrSet = AggrregateSetting.MANAGED_AS_FLEX_TIME;
                self.aggrMethod = FlexAggregateMethod.PRINCIPLE;
            }
            // 
            public updateData(model: FlexWorktimeAggrSetting): void {
                let self = this;
                self.insufficSet = model.shortageSetting();
                self.includeOverTime = model.includeOT();
                self.legalAggrSet = model.legalAggrSet();
                self.aggrMethod = model.aggregateMethod();
            }
        }
    
        /**
         * 集計設定
         */
        export class AggrregateSetting {
            static MANAGED_AS_FLEX_TIME = 1;
            static MANAGE_BREAKDOWN = 0;
        }
    
        /**
         * フレックス不足時の繰越設定
         */
        export class ShortageSetting {
            static CURRENT_MONTH_INTEGRATION = 0;
            static NEXT_MONTH_CARRY_FORWARD = 1;
        }
    
        /**
         * フレックス集計方法
         */
        export class FlexAggregateMethod {
            static PRINCIPLE = 0;
            static FOR_CONVINENCE = 1;
        }
    
        /**
         * 週開始
         */
        export class StartWeek {
            static MONDAY = 0;
            static TUESDAY = 1;
            static WEDNESDAY = 2;
            static THURSDAY = 3;
            static FRIDAY = 4;
            static SATURDAY = 5;
            static SUNDAY = 6;
            static CLOSURE_STR_DATE = 7;
        }
    
        /**
             * 割増集計方法
             */
        export class ExcessOutsideTimeSet {
            includeLegal: KnockoutObservable<boolean>;
            includeHoliday: KnockoutObservable<boolean>;
            includeExtra: KnockoutObservable<boolean>;
            constructor() {
                let self = this;
                self.includeLegal = ko.observable(false);
                self.includeHoliday = ko.observable(false);
                self.includeExtra = ko.observable(false);
            }
    
            public updateData(dto: ExcessOutsideTimeSetDto): void {
                let self = this;
                self.includeLegal(dto.legalOverTimeWork);
                self.includeHoliday(dto.legalHoliday);
                self.includeExtra(dto.surchargeWeekMonth);
            }
        }
        /**
         * MonthlyTime 月単位
         */
        export class MonthlyTime {
            month: KnockoutObservable<number>;
            time: KnockoutObservable<number>;
    
            constructor() {
                let self = this;
                self.month = ko.observable(new Date().getMonth());
                self.time = ko.observable(0);
            }
            public updateData(dto: MonthlyUnitDto): void {
                let self = this;
                self.month(dto.month);
                self.time(dto.monthlyTime);
            }
        }
    
        export class FlexMonthlyTime {
            month: KnockoutObservable<number>;
            statutoryTime: KnockoutObservable<number>;
            //            specifiedMonth: KnockoutObservable<number>;
            specifiedTime: KnockoutObservable<number>;
            constructor() {
                let self = this;
                self.month = ko.observable(new Date().getMonth());
                self.statutoryTime = ko.observable(0);
                self.specifiedTime = ko.observable(0);
            }
    
            //            public updateData(dto: ): void {
            //                let self = this;
            //            }
        }
    
        export class NormalWorktime {
            // 会社別通常勤務労働時間
            dailyTime: KnockoutObservable<number>;
            weeklyTime: KnockoutObservable<number>;
            startWeek: KnockoutObservable<number>;
    
            constructor() {
                let self = this;
                self.dailyTime = ko.observable(0);
                self.weeklyTime = ko.observable(0);
                self.startWeek = ko.observable(StartWeek.SUNDAY);
            }
    
            public updateData(dto: NormalWorktimeDto): void {
                let self = this;
                self.dailyTime(dto.dailyTime.dailyTime);
                self.weeklyTime(dto.weeklyTime.time);
                self.startWeek(dto.weeklyTime.start);
            }
        }
    
        /**
         * 会社別通常勤務月間労働時間
         * 会社別変形労働月間労働時間
         */
        export class WorktimeNormalDeformSetting {
            year: KnockoutObservable<number>;
            // 法定時間: 月単位
            statutorySetting: KnockoutObservableArray<MonthlyTime>;
    
            constructor() {
                let self = this;
                self.year = ko.observable(new Date().getFullYear());
                self.statutorySetting = ko.observableArray([]);
                for (let i = 1; i < 13; i++) {
                    let m = new MonthlyTime();
                    m.month(i);
                    m.time(0);
                    self.statutorySetting.push(m);
                }
            }
    
            public updateData(dto: StatutoryWorktimeSettingDto): void {
                let self = this;
                self.year(dto.year);
                self.statutorySetting().forEach(i => {
                    let updatedData: MonthlyUnitDto = dto.normalSetting.statutorySetting.filter(j => i.month() == j.month)[0];
                    i.updateData(updatedData);
                });
            }
    
            public sortMonth(startMonth: number): void {
                let self = this;
                let sortedList: Array<any> = new Array<any>();
                let flexSortedList: Array<any> = new Array<any>();
                for (let i = 0; i < 12; i++) {
                    if (startMonth > 12) {
                        // reset month.
                        startMonth = 1;
                    }
                    let value = self.statutorySetting().filter(m => startMonth == m.month())[0];
                    sortedList.push(value);
                    startMonth++;
                }
                self.statutorySetting(sortedList);
            }
        }
        /**
         * 会社別フレックス勤務月間労働時間
         */
        export class WorktimeFlexSetting {
            year: KnockoutObservable<number>;
            // 法定時間: 月単位
            statutorySetting: KnockoutObservableArray<MonthlyTime>;
            // 所定時間: 月単位
            specifiedSetting: KnockoutObservableArray<MonthlyTime>;
    
            constructor() {
                let self = this;
                self.year = ko.observable(new Date().getFullYear());
                self.statutorySetting = ko.observableArray([]);
                self.specifiedSetting = ko.observableArray([]);
                for (let i = 1; i < 13; i++) {
                    let m = new MonthlyTime();
                    m.month(i);
                    m.time(0);
                    self.statutorySetting.push(m);
                    self.statutorySetting.push(m);
                }
            }
    
            public sortMonth(startMonth: number): void {
                let self = this;
                let statutorySortedList: Array<any> = new Array<any>();
                let specifiedSortedList: Array<any> = new Array<any>();
                for (let i = 0; i < 12; i++) {
                    if (startMonth > 12) {
                        // reset month.
                        startMonth = 1;
                    }
                    let statutoryValue = self.statutorySetting().filter(m => startMonth == m.month())[0];
                    let specifiedValue = self.specifiedSetting().filter(m => startMonth == m.month())[0];
                    statutorySortedList.push(statutoryValue);
                    specifiedSortedList.push(specifiedValue);
                    startMonth++;
                }
                self.statutorySetting(statutorySortedList);
                self.specifiedSetting(specifiedSortedList);
            }
        }
    
        /**
         * 会社別フレックス勤務月間労働時間
         */
        export class WorktimeFlexSetting1 {
            year: KnockoutObservable<number>;
            //            // 法定時間: 月単位
            //            statutorySetting: KnockoutObservableArray<MonthlyTime>;
            //            // 所定時間: 月単位
            //            specifiedSetting: Knockourray<MonthlyTime>;
                
            flexSettingDetail: KnockoutObservableArray<FlexMonthlyTime>;
    
            constructor() {
                let self = this;
                self.year = ko.observable(new Date().getFullYear());
                self.flexSettingDetail = ko.observableArray([]);
                for (let i = 1; i < 13; i++) {
                    let mFlex = new FlexMonthlyTime();
                    mFlex.month(i);
                    mFlex.statutoryTime(0);
                    mFlex.specifiedTime(0);
                    self.flexSettingDetail.push(mFlex);
                }
            }
    
            public updateData(dto: StatutoryWorktimeSettingDto): void {
                let self = this;
                self.year(dto.year);
                self.flexSettingDetail().forEach(i => {
                    // Stutory
                    let stutoryData: MonthlyUnitDto = dto.flexSetting.statutorySetting.filter(j => i.month() == j.month)[0];//WorktimeFlexSetting1Dto
                    // Specified
                    let specifiedData: MonthlyUnitDto = dto.flexSetting.specifiedSetting.filter(j => i.month() == j.month)[0];
    
                    i.month(stutoryData.month);
                    i.statutoryTime(stutoryData.monthlyTime);
                    i.specifiedTime(specifiedData.monthlyTime);
    
                });
            }
    
            public sortMonth(startMonth: number): void {
                let self = this;
                let sortedList: Array<any> = new Array<any>();
                let flexSortedList: Array<any> = new Array<any>();
                for (let i = 0; i < 12; i++) {
                    if (startMonth > 12) {
                        // reset month.
                        startMonth = 1;
                    }
                    let flexValue = self.flexSettingDetail().filter(m => startMonth == m.month())[0];
                    flexSortedList.push(flexValue);
                    startMonth++;
                }
                self.flexSettingDetail(flexSortedList);
            }
        }
    
        /**
         * 通常勤務の法定内集計設定
         */
        export class NormalWorktimeAggrSetting {
            // 時間外超過設定: 割増集計方法
            excessOutsideTimeSet: ExcessOutsideTimeSet;
            // 集計時間設定: 割増集計方法
            aggregateOutsideTimeSet: ExcessOutsideTimeSet;
    
            constructor() {
                let self = this;
                self.excessOutsideTimeSet = new ExcessOutsideTimeSet();
                self.aggregateOutsideTimeSet = new ExcessOutsideTimeSet();
            }
            // MonthlyCalSettingDto
            public updateData(dto: NormalWorktimeAggrSettingDto): void {
                let self = this;
                self.excessOutsideTimeSet.updateData(dto.excessOutsideTimeSet);
                self.aggregateOutsideTimeSet.updateData(dto.aggregateTimeSet);
            }
        }
    
        /**
         * 変形労働時間勤務の法定内集計設定
         */
        export class DeformWorktimeAggrSetting {
            // 通常勤務労働会社別月別実績集計設定
            excessOutsideTimeSet: ExcessOutsideTimeSet;
            aggregateOutsideTimeSet: ExcessOutsideTimeSet;
    
            isDeformedOT: KnockoutObservable<boolean>;
            period: KnockoutObservable<number>;
            repeatCls: KnockoutObservable<boolean>;
            startMonth: KnockoutObservable<number>;
    
            constructor() {
                let self = this;
                self.excessOutsideTimeSet = new ExcessOutsideTimeSet();
                self.aggregateOutsideTimeSet = new ExcessOutsideTimeSet();
    
                self.isDeformedOT = ko.observable(false);
                self.period = ko.observable(1);
                self.repeatCls = ko.observable(false);
                self.startMonth = ko.observable(new Date().getMonth());
            }
    
            public updateData(dto: DeformWorktimeAggrSettingDto): void {
                let self = this;
                self.excessOutsideTimeSet.updateData(dto.excessOutsideTimeSet);
                self.aggregateOutsideTimeSet.updateData(dto.aggregateTimeSet);
                self.isDeformedOT(dto.isOtTransCriteria);
                self.startMonth(dto.settlementPeriod.startMonth);
                self.period(dto.settlementPeriod.period);
                self.repeatCls(dto.settlementPeriod.repeatAtr);
            }
        }
    
        /**
         * フレックス時間勤務の月の集計設定
         */
        export class FlexWorktimeAggrSetting {
            // 不足設定: フレックス不足設定// G2_2
            shortageSetting: KnockoutObservable<number>;
            // 残業時間を含める: するしない区分//// G1_2
            includeOT: KnockoutObservable<boolean>;
            // 法定内集計設定: 法定内フレックス時間集計
            legalAggrSet: KnockoutObservable<number>;
            // 集計方法: フレックス集計方法//
            aggregateMethod: KnockoutObservable<number>;
    
            constructor() {
                let self = this;
                self.shortageSetting = ko.observable(ShortageSetting.CURRENT_MONTH_INTEGRATION);
                self.includeOT = ko.observable(false);
                self.legalAggrSet = ko.observable(AggrregateSetting.MANAGED_AS_FLEX_TIME);
                self.aggregateMethod = ko.observable(FlexAggregateMethod.PRINCIPLE);
            }
    
            public updateData(dto: FlexWorktimeAggrSettingDto): void {
                let self = this;
                self.shortageSetting(dto.insufficSet);
                self.includeOT(dto.includeOverTime);
                self.legalAggrSet(dto.legalAggrSet);
                self.aggregateMethod(dto.aggrMethod);
            }
        }
    
        /**
             * Normal Setting Params Model (Screen F)
             */
        export class NormalSetParams {
            startWeek: number;
            isIncludeExtraAggr: boolean;
            isIncludeLegalAggr: boolean;
            isIncludeHolidayAggr: boolean;
            isIncludeExtraExcessOutside: boolean;
            isIncludeLegalExcessOutside: boolean;
            isIncludeHolidayExcessOutside: boolean;
    
            constructor() {
                let self = this;
                self.startWeek = 0;
                self.isIncludeExtraAggr = false;
                self.isIncludeLegalAggr = false;
                self.isIncludeHolidayAggr = false;
                self.isIncludeExtraExcessOutside = false;
                self.isIncludeLegalExcessOutside = false;
                self.isIncludeHolidayExcessOutside = false;
            }
        }
    
        /**
         * Flex Setting Params Model
         */
        export class FlexSetParams {
            isIncludeOverTime: boolean;
            shortageSetting: number;
    
            constructor() {
                let self = this;
                self.isIncludeOverTime = false;
                self.shortageSetting = 1;
            }
        }
    
        /**
         * Deformed Labor Setting Params Model
         */
        export class DeformSetParams {
            strMonth: number;
            period: number;
            repeatCls: boolean;
            startWeek: number;
            isIncludeExtraAggr: boolean;
            isIncludeLegalAggr: boolean;
            isIncludeHolidayAggr: boolean;
            isIncludeExtraExcessOutside: boolean;
            isIncludeLegalExcessOutside: boolean;
            isIncludeHolidayExcessOutside: boolean;
    
            constructor() {
                let self = this;
                self.strMonth = moment(new Date()).toDate().getMonth();
                self.period = 1;
                self.repeatCls = false;
                self.startWeek = 0;
                self.isIncludeExtraAggr = false;
                self.isIncludeLegalAggr = false;
                self.isIncludeHolidayAggr = false;
                self.isIncludeExtraExcessOutside = false;
                self.isIncludeLegalExcessOutside = false;
                self.isIncludeHolidayExcessOutside = false;
            }
        }
    }
}