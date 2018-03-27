module a14 {
    
    import WorkTimeDailyAtr = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeDailyAtr;
    import WorkTimeMethodSet = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeMethodSet
    import WorkTimeSettingEnumDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeSettingEnumDto;
    import EnumConstantDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.EnumConstantDto;
    
    import WorkTimezoneShortTimeWorkSetModel = nts.uk.at.view.kmk003.a.viewmodel.common.WorkTimezoneShortTimeWorkSetModel;
    
    import MainSettingModel = nts.uk.at.view.kmk003.a.viewmodel.MainSettingModel;
    import TabMode = nts.uk.at.view.kmk003.a.viewmodel.TabMode;
    
     /**
     * Screen Model - Tab 14
     * 就業時間帯の共通設定 -> 短時間勤務設定
     * WorkTimeCommonSet -> ShortTimeWorkSetOfWorkTime
     */
    class ScreenModel {

        selectedTab: KnockoutObservable<string>;
        
        // Screen mode
        isDetailMode: KnockoutObservable<boolean>;
        
        // Screen data model
        model: MainSettingModel;
        settingEnum: WorkTimeSettingEnumDto;
        
        // Detail mode - Data
        childCareWorkUse: KnockoutObservable<boolean>;
        nursingTimeWorkUse: KnockoutObservable<boolean>;
        
        lstChildCareEnum: KnockoutObservableArray<any>;
        lstNursingTimeEnum: KnockoutObservableArray<any>;   
        
        // Simple mode - Data  
    
        /**
        * Constructor.
        */
        constructor(selectedTab: KnockoutObservable<string>, screenMode: any, model: MainSettingModel, settingEnum: WorkTimeSettingEnumDto) {
            let _self = this;
            _self.selectedTab = selectedTab;
            
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
            _self.lstChildCareEnum = ko.observableArray([
                {value: false, localizedName: nts.uk.resource.getText("KMK003_116")},
                {value: true, localizedName: nts.uk.resource.getText("KMK003_117")}
            ]);          
            _self.lstNursingTimeEnum = ko.observableArray([
                {value: false, localizedName: nts.uk.resource.getText("KMK003_196")},
                {value: true, localizedName: nts.uk.resource.getText("KMK003_197")}
            ]);
            
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
            _self.childCareWorkUse = _self.model.commonSetting.shortTimeWorkSet.childCareWorkUse;
            _self.nursingTimeWorkUse = _self.model.commonSetting.shortTimeWorkSet.nursTimezoneWorkUse;
        }         
        
    }
    
    /**
     * KMK003A14BindingHandler
     */
    class KMK003A14BindingHandler implements KnockoutBindingHandler {
        
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any,
            bindingContext: KnockoutBindingContext): void {
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any,
            bindingContext: KnockoutBindingContext): void {
            var webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                .mergeRelativePath('/view/kmk/003/a14/index.xhtml').serialize();
            //get data
            let input = valueAccessor();
            let screenMode = input.screenMode;
            let model = input.model;
            let settingEnum = input.enum;

            let screenModel = new ScreenModel(input.selectedTab, screenMode, model, settingEnum);
            $(element).load(webserviceLocator, function() {
                ko.cleanNode($(element)[0]);
                ko.applyBindingsToDescendants(screenModel, $(element)[0]);
                screenModel.startTab(screenMode);
            });
        }
    }
    ko.bindingHandlers['ntsKMK003A14'] = new KMK003A14BindingHandler();
}
