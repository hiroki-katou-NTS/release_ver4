module a1 {
    
    import WorkTimeSettingEnumDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeSettingEnumDto;
    
    import PredetemineTimeSettingModel = nts.uk.at.view.kmk003.a.viewmodel.predset.PredetemineTimeSettingModel;
    import TimezoneModel = nts.uk.at.view.kmk003.a.viewmodel.predset.TimezoneModel;
    import EmTimezoneChangeExtentModel = nts.uk.at.view.kmk003.a.viewmodel.difftimeset.EmTimezoneChangeExtentModel;
    import CoreTimeSettingModel = nts.uk.at.view.kmk003.a.viewmodel.flexset.CoreTimeSettingModel
    import MainSettingModel = nts.uk.at.view.kmk003.a.viewmodel.MainSettingModel;
    
    class ScreenModel {
        
        selectedTab: KnockoutObservable<string>;

        dayStartTime: KnockoutObservable<number>;
        dayStartTimeOption: KnockoutObservable<any>;

        oneDayRangeTimeOption: KnockoutObservable<any>;

        beforeUpdateWorkTimeOption: KnockoutObservable<any>;

        afterUpdateWorkTimeOption: KnockoutObservable<any>;

        // flag
        isDiffTimeMode: KnockoutObservable<boolean>;
        isDetailMode: KnockoutObservable<boolean>;
        isTimezoneTwoEnabled: KnockoutObservable<boolean>;
        isFlexMode: KnockoutObservable<boolean>;
        useHalfDay: KnockoutObservable<boolean>;

        mainSettingModel: MainSettingModel;
        predseting: PredetemineTimeSettingModel;
        changeExtent: EmTimezoneChangeExtentModel;
        timeZoneModelOne: TimezoneModel;
        timeZoneModelTwo: TimezoneModel;
        coreTimeSettingModel: CoreTimeSettingModel;
        settingEnum: WorkTimeSettingEnumDto;
        SBcoreTimezone: Array<any>;

        /**
        * Constructor.
        */
        constructor(input: any) {
            let self = this;
            self.selectedTab = input.selectedTab;
            
            self.loadDataFromMainScreen(input);
            self.isTimezoneTwoEnabled = ko.computed(() => {
                return !self.isFlexMode() && !self.isDiffTimeMode();
            });

            self.SBcoreTimezone = [
                { value: 1, localizedName: nts.uk.resource.getText("KMK003_158") }, // used
                { value: 0, localizedName: nts.uk.resource.getText("KMK003_159") } // not used
            ];

            //day start Time
            self.dayStartTime = ko.observable(0);
            self.dayStartTimeOption = ko.observable(new nts.uk.ui.option.TimeEditorOption({
                width: "50"
            }));

            //one day range Time
            self.oneDayRangeTimeOption = ko.observable(new nts.uk.ui.option.TimeEditorOption({
                width: "50",
            }));


            self.beforeUpdateWorkTimeOption = ko.observable(new nts.uk.ui.option.TimeEditorOption({
                width: "50"
            }));
            self.afterUpdateWorkTimeOption = ko.observable(new nts.uk.ui.option.TimeEditorOption({
                width: "50"
            }));

            self.isDiffTimeMode.subscribe(function(isDifftime: boolean){
                if(isDifftime && !(self.changeExtent)){
                    self.changeExtent = self.mainSettingModel.diffWorkSetting.changeExtent;
                }
            });
        }

        /**
         * Load data from main screen
         */
        private loadDataFromMainScreen(data: any): void {
            let self = this;
            let settingEnum: WorkTimeSettingEnumDto = data.enum;
            self.isDetailMode = data.isDetailMode;
            self.mainSettingModel = data.mainSettingModel;
            self.predseting = self.mainSettingModel.predetemineTimeSetting;
            self.timeZoneModelOne = self.mainSettingModel.predetemineTimeSetting.prescribedTimezoneSetting.shiftOne
            self.timeZoneModelTwo = self.mainSettingModel.predetemineTimeSetting.prescribedTimezoneSetting.shiftTwo;
            self.coreTimeSettingModel = self.mainSettingModel.flexWorkSetting.coreTimeSetting;
            self.isFlexMode = self.mainSettingModel.workTimeSetting.isFlex;
            self.isDiffTimeMode = self.mainSettingModel.workTimeSetting.isDiffTime;
            self.settingEnum = settingEnum;
            self.useHalfDay = data.useHalfDay;
        }

        public collectData(oldData: any) {
            let self = this;
            oldData.startDateClock = self.dayStartTime();
        }

    }
    export class Item {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    class KMK003A1BindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        private getData() {
            let self = this;
            // service.findWorkTimeSetByCode()
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any): void {
            var webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                .mergeRelativePath('/view/kmk/003/a1/index.xhtml').serialize();

            let screenModel = new ScreenModel(valueAccessor());
            $(element).load(webserviceLocator, function() {
                ko.cleanNode($(element)[0]);
                ko.applyBindingsToDescendants(screenModel, $(element)[0]);
            });
        }

    }
    ko.bindingHandlers['ntsKMK003A1'] = new KMK003A1BindingHandler();

}
