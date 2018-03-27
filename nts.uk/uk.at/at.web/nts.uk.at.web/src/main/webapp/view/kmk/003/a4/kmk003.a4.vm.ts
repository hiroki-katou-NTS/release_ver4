module a4 {
    import MainSettingModel = nts.uk.at.view.kmk003.a.viewmodel.MainSettingModel;
    import EnumWorkForm = nts.uk.at.view.kmk003.a.viewmodel.EnumWorkForm;
    import SettingMethod = nts.uk.at.view.kmk003.a.viewmodel.SettingMethod;
    import TabMode = nts.uk.at.view.kmk003.a.viewmodel.TabMode;
    import WorkTimeSettingEnumDto = nts.uk.at.view.kmk003.a.service.model.worktimeset.WorkTimeSettingEnumDto;
    import PrioritySettingModel = nts.uk.at.view.kmk003.a.viewmodel.common.PrioritySettingModel;
    class ScreenModel {

        selectedTab: KnockoutObservable<string>;
        
        // Screen mode
        isDetailMode: KnockoutObservable<boolean>;
        
        priorityOptions: KnockoutObservableArray<Item>;
        priorityGoWork: KnockoutObservable<number>;
        priorityLeaveWork: KnockoutObservable<number>;

        stampComboBoxOptions: KnockoutObservableArray<Item>;
        stampGoWork: KnockoutObservable<number>;
        stampLeaveWork: KnockoutObservable<number>;

        stampRoundingOptions: KnockoutObservableArray<Item>;
        stampRoundingGoWork: KnockoutObservable<number>;
        stampRoundingLeaveWork: KnockoutObservable<number>;


        leastWorkTime: KnockoutObservable<number>;
        morningEndTime: KnockoutObservable<number>;
        afternoonStartTime: KnockoutObservable<number>;

        oneDay: KnockoutObservable<number>;
        morning: KnockoutObservable<number>;
        afternoon: KnockoutObservable<number>;
        
        mainSettingModel: MainSettingModel;
        /**
        * Constructor.
        */
        constructor(selectedTab: KnockoutObservable<string>, tabMode: any,enumSetting: WorkTimeSettingEnumDto,mainSettingModel: MainSettingModel) {
            let self = this;
            self.selectedTab = selectedTab;
            
            // Subscribe Detail/Simple mode 
            self.isDetailMode = ko.observable(null);
            tabMode.subscribe((value: any) => {
                value == TabMode.DETAIL ? self.isDetailMode(true) : self.isDetailMode(false);
            });
            tabMode() == TabMode.DETAIL ? self.isDetailMode(true) : self.isDetailMode(false);
            
            //main model
            self.mainSettingModel = mainSettingModel;
            
            self.priorityOptions = ko.observableArray([
                new Item(0, nts.uk.resource.getText("KMK003_69")),
                new Item(1, nts.uk.resource.getText("KMK003_70"))
            ]);

            self.priorityGoWork = ko.observable(0);
            self.priorityLeaveWork = ko.observable(0);

            let roundTimes:Item[] = [];
            enumSetting.roundingTime.forEach(function(item, index) {
                roundTimes.push(new Item(index, item.localizedName));
            });
            
            self.stampComboBoxOptions = ko.observableArray(roundTimes);

            self.stampGoWork = ko.observable(0);
            self.stampLeaveWork = ko.observable(0);

            self.stampRoundingOptions = ko.observableArray([
                new Item(0, nts.uk.resource.getText("KMK003_72")),
                new Item(1, nts.uk.resource.getText("KMK003_73"))
            ]);

            self.stampRoundingGoWork = ko.observable(0);
            self.stampRoundingLeaveWork = ko.observable(0);

            self.leastWorkTime = ko.observable(0);
            self.morningEndTime = ko.observable(0);
            self.afternoonStartTime = ko.observable(0);

            self.oneDay = ko.observable(0);
            self.morning = ko.observable(0);
            self.afternoon = ko.observable(0);
            self.bindToScreen();
        }
        
        public bindToScreen() {
            let self = this;
            let workForm = self.mainSettingModel.workTimeSetting.workTimeDivision.workTimeDailyAtr();

            let stamp = self.mainSettingModel.commonSetting.stampSet;
            self.priorityGoWork = stamp.prioritySets[0].priorityAtr() == EnumStampPiorityAtr.GOING_WORK ? stamp.prioritySets[0].stampAtr : stamp.prioritySets[1].stampAtr;
            self.priorityLeaveWork = stamp.prioritySets[0].priorityAtr() == EnumStampPiorityAtr.LEAVE_WORK ? stamp.prioritySets[0].stampAtr : stamp.prioritySets[1].stampAtr;
            //                
            self.stampRoundingGoWork = stamp.roundingSets[0].section() == EnumStampPiorityAtr.GOING_WORK ? stamp.roundingSets[0].roundingSet.roundingTimeUnit : stamp.roundingSets[1].roundingSet.roundingTimeUnit;
            self.stampRoundingLeaveWork = stamp.roundingSets[0].section() == EnumStampPiorityAtr.LEAVE_WORK ? stamp.roundingSets[0].roundingSet.roundingTimeUnit : stamp.roundingSets[1].roundingSet.roundingTimeUnit;
            
            self.stampGoWork = stamp.roundingSets[0].section() == EnumStampPiorityAtr.GOING_WORK ? stamp.roundingSets[0].roundingSet.fontRearSection : stamp.roundingSets[1].roundingSet.fontRearSection;
            self.stampLeaveWork = stamp.roundingSets[0].section() == EnumStampPiorityAtr.LEAVE_WORK ? stamp.roundingSets[0].roundingSet.fontRearSection : stamp.roundingSets[1].roundingSet.fontRearSection;
            //check mode screen
        }

    }
    export class Item {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    export enum EnumStampPiorityAtr {
        GOING_WORK,
        LEAVE_WORK
    }
    
    class KMK003A4BindingHandler implements KnockoutBindingHandler {
        /**
         * Constructor.
         */
        constructor() {
        }

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            var webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["at"] + '/')
                .mergeRelativePath('/view/kmk/003/a4/index.xhtml').serialize();

            //get data
            let input = valueAccessor();
            let tabMode = input.tabMode;
            let enumSetting = input.enum;
            let mainSettingModel = input.mainSettingModel;

            var screenModel = new ScreenModel(input.selectedTab, tabMode, enumSetting, mainSettingModel);
            $(element).load(webserviceLocator, function() {
                ko.cleanNode($(element)[0]);
                ko.applyBindingsToDescendants(screenModel, $(element)[0]);
            });
            
//             $.fn.getData = function() {
//                return {
//                    name: screenModel.leastWorkTime()
//                };
//            }
        }

        private getData() {
            let self = this;
            //            service.findWorkTimeSetByCode()    
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
        }

    }
    ko.bindingHandlers['ntsKMK003A4'] = new KMK003A4BindingHandler();

}
