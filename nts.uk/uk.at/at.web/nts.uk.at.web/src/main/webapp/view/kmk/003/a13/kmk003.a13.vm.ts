module a13 {
    
    import WorkTimeDailyAtr = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeDailyAtr;
    import WorkTimeMethodSet = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeMethodSet
    import WorkTimeSettingEnumDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeSettingEnumDto;
    import EnumConstantDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.EnumConstantDto;
    
    import TimeRoundingSettingModel = nts.uk.at.view.kmk003.a.viewmodel.common.TimeRoundingSettingModel;
    
    import MainSettingModel = nts.uk.at.view.kmk003.a.viewmodel.MainSettingModel;
    import TabMode = nts.uk.at.view.kmk003.a.viewmodel.TabMode;
    
    /**
     * Screen Model - Tab 13
     * 就業時間帯の共通設定 -> 臨時設定
     * WorkTimeCommonSet -> TemporaryWorkTimeSet
     */
    class ScreenModel {
        
        isNewMode: KnockoutObservable<boolean>;
        
        // Screen mode
        isDetailMode: KnockoutObservable<boolean>;
        
        // Screen data model
        model: MainSettingModel;
        settingEnum: WorkTimeSettingEnumDto;
        
        // Detail mode - Data
        temporaryWorkTimeSettingRoundingTime: KnockoutObservable<number>;
        temporaryWorkTimeSettingRounding: KnockoutObservable<number>;
           
        listRoundingTimeValue: KnockoutObservableArray<EnumConstantDto>;
        listRoundingValue: KnockoutObservableArray<EnumConstantDto>;
        
        // Simple mode - Data  
        
        /**
         * Constructor
         */
        constructor(isNewMode: KnockoutObservable<boolean>, screenMode: any, model: MainSettingModel, settingEnum: WorkTimeSettingEnumDto) {
            let _self = this;
            _self.isNewMode = isNewMode;
            _self.isNewMode.subscribe((v) => {
                // Set default value for switch button
                if (v) {
                    if (!nts.uk.util.isNullOrUndefined(_self.temporaryWorkTimeSettingRounding)) _self.temporaryWorkTimeSettingRounding(0);
                }    
            });
            
            // Check exist
            if (nts.uk.util.isNullOrUndefined(model) || nts.uk.util.isNullOrUndefined(settingEnum)) {
                // Stop rendering page
                return;    
            }
            
            // Binding data
            _self.model = model; 
            _self.settingEnum = settingEnum;
            _self.bindingData();
            
            // Init all data                                            
            _self.listRoundingTimeValue = ko.observableArray(_self.settingEnum.roundingTime);
            _self.listRoundingValue = ko.observableArray(_self.settingEnum.roundingSimple);      
            
            // Detail mode and simple mode is same
            _self.isDetailMode = ko.observable(null);
            _self.isDetailMode.subscribe(newValue => {
                // Nothing to do
            });                                                            
            // Subscribe Detail/Simple mode 
            screenMode.subscribe((value: any) => {
                value == TabMode.DETAIL ? _self.isDetailMode(true) : _self.isDetailMode(false);
            });
        }
                        
        /**
         * Start tab
         */
        public startTab(screenMode: any): void {
            let _self = this;
            screenMode() == TabMode.DETAIL ? _self.isDetailMode(true) : _self.isDetailMode(false);
        }
        
        /**
         * Binding data
         */
        private bindingData() {
            let _self = this;           
            _self.temporaryWorkTimeSettingRoundingTime = _self.model.commonSetting.extraordTimeSet.timeRoundingSet.roundingTime;
            _self.temporaryWorkTimeSettingRounding = _self.model.commonSetting.extraordTimeSet.timeRoundingSet.rounding;
        }                    
    }
    
    /**
     * Knockout Binding Handler - Tab 13
     */
    class KMK003A13BindingHandler implements KnockoutBindingHandler {
        
        /**
         * Constructor
         */
        constructor() {}

        /**
         * Init
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {}

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {          
            let webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                .mergeRelativePath('/view/kmk/003/a13/index.xhtml').serialize();
            // Get data
            let input = valueAccessor();
            let screenMode = input.screenMode;
            let model = input.model;
            let settingEnum = input.enum;

            let screenModel = new ScreenModel(input.isNewMode, screenMode, model, settingEnum);
            $(element).load(webserviceLocator, () => {
                ko.cleanNode($(element)[0]);
                ko.applyBindingsToDescendants(screenModel, $(element)[0]);
                screenModel.startTab(screenMode);
            });
        }
    }
    
    ko.bindingHandlers['ntsKMK003A13'] = new KMK003A13BindingHandler();
}