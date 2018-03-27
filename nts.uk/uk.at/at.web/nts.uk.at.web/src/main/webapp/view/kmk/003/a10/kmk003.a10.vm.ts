module a10 {
    
    import WorkTimeDailyAtr = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeDailyAtr;
    import WorkTimeMethodSet = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeMethodSet
    import WorkTimeSettingEnumDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeSettingEnumDto;
    import EnumConstantDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.EnumConstantDto;
    
    import MainSettingModel = nts.uk.at.view.kmk003.a.viewmodel.MainSettingModel;
    import TabMode = nts.uk.at.view.kmk003.a.viewmodel.TabMode;
    
    /**
     * Screen Model - Tab 10
     * 就業時間帯の共通設定 -> 加給設定
     * WorkTimeCommonSet -> BonusPaySettingCode
     */
    class ScreenModel {
        
        selectedTab: KnockoutObservable<string>;
        
        // Screen mode
        isDetailMode: KnockoutObservable<boolean>;
        
        // Screen data model
        model: MainSettingModel;
        settingEnum: WorkTimeSettingEnumDto;
        
        // Detail mode - Data
        bonusPaySettingCode: KnockoutObservable<string>;
        bonusPaySettingName: KnockoutObservable<string>;
        
        lstBonusPaysetting: KnockoutObservableArray<any>;
        // Simple mode - Data  
        
        /**
         * Constructor
         */
        constructor(selectedTab: KnockoutObservable<string>, screenMode: any, model: MainSettingModel, settingEnum: WorkTimeSettingEnumDto, lstPaySetting:any) {
            let _self = this;          
            _self.selectedTab = selectedTab;
            
            _self.lstBonusPaysetting = ko.observableArray(lstPaySetting);
            // Check exist
            if (nts.uk.util.isNullOrUndefined(model) || nts.uk.util.isNullOrUndefined(settingEnum)) {
                // Stop rendering page
                return;    
            }
            
            // Binding data
            _self.model = model; 
            _self.settingEnum = settingEnum;
            _self.bindingData();
                        
            _self.bonusPaySettingName = ko.observable("");                                 
            
            //subscribe not run here=> add control to bind name to screen
            _self.bindingNameByCode(_self.bonusPaySettingCode());
            
             // Detail mode and simple mode is same
            _self.isDetailMode = ko.observable(null);
            _self.isDetailMode.subscribe(newValue => {
                // Nothing to do
            });                                                            
            // Subscribe Detail/Simple mode 
            screenMode.subscribe((value: any) => {
                value == TabMode.DETAIL ? _self.isDetailMode(true) : _self.isDetailMode(false);
            });
            _self.bonusPaySettingCode.subscribe((v)=>{
                _self.bindingNameByCode(v);
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
            _self.bonusPaySettingCode = _self.model.commonSetting.raisingSalarySet;
        }             
        
        private bindingNameByCode(code: string) {
            let _self = this;
            if (!nts.uk.util.isNullOrUndefined(code) && (code != "") && (typeof code == 'string')) {
                //filter to get name by code
                let itemPaySetting: any = _.filter(_self.lstBonusPaysetting(), item => item.code == code);
                _self.bonusPaySettingName(itemPaySetting[0].name);
            }
            else {
                _self.bonusPaySettingName("");
            }
        }
        /**
         * UI handler: open referral dialog
         */
        public openReferralDialog(): void {
            let _self = this;
            let param: any = {
                isMulti: false,
                selecteds: [
                    _self.bonusPaySettingCode()
                ]
            }
            nts.uk.ui.windows.setShared('KDL007_PARAM', param, true);
            nts.uk.ui.windows.sub.modal('/view/kdl/007/a/index.xhtml').onClosed(() => {
                let listResult = nts.uk.ui.windows.getShared('KDL007_VALUES');
                if (listResult && listResult.selecteds && !nts.uk.util.isNullOrEmpty(listResult.selecteds[0]) && (typeof listResult.selecteds[0] == 'string')) {
                    _self.bonusPaySettingCode(listResult.selecteds[0]);
//                    _self.bonusPaySettingName(listResult.selecteds[0]);
                } else {
                    _self.bonusPaySettingCode(null);
                    _self.bonusPaySettingName(null);
                }
            });
        }
    }
    
    /**
     * Knockout Binding Handler - Tab 10
     */
    class KMK003A10BindingHandler implements KnockoutBindingHandler {
        
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
                .mergeRelativePath('/view/kmk/003/a10/index.xhtml').serialize();
            // Get data
            let input = valueAccessor();
            let screenMode = input.screenMode;
            let model = input.model;
            let settingEnum = input.enum;

            nts.uk.at.view.kmk003.a10.service.findAllBonusPaySetting().done(function(lstPaySetting:any) {
                let screenModel = new ScreenModel(input.selectedTab, screenMode, model, settingEnum,lstPaySetting);

                $(element).load(webserviceLocator, () => {
                    ko.cleanNode($(element)[0]);
                    ko.applyBindingsToDescendants(screenModel, $(element)[0]);
                    screenModel.startTab(screenMode);
                });

            });
        }
    }
    
    ko.bindingHandlers['ntsKMK003A10'] = new KMK003A10BindingHandler();
}