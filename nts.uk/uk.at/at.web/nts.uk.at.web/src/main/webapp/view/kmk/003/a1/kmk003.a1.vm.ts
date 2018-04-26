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

        linkedWithDialogF: KnockoutObservable<boolean>;
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
            
            self.linkedWithDialogF = ko.observable(self.checkLinked(self.collectDialog()));
            self.predseting.predTime.predTime.oneDay.subscribe((v) => {
                let preCheck = self.linkedWithDialogF();
                self.linkedWithDialogF(self.checkLinked(self.collectDialog()));
                if (self.linkedWithDialogF() || preCheck == true) {
                    self.predseting.predTime.addTime.oneDay(v);
                }
                self.linkedWithDialogF(self.checkLinked(self.collectDialog()));
            });
            self.predseting.predTime.predTime.morning.subscribe((v) => {
                let preCheck = self.linkedWithDialogF();
                self.linkedWithDialogF(self.checkLinked(self.collectDialog()));
                if (self.linkedWithDialogF() || preCheck == true) {
                    self.predseting.predTime.addTime.morning(v);
                }
                self.linkedWithDialogF(self.checkLinked(self.collectDialog()));
            });
            self.predseting.predTime.predTime.afternoon.subscribe((v) => {
                let preCheck = self.linkedWithDialogF();
                self.linkedWithDialogF(self.checkLinked(self.collectDialog()));
                if (self.linkedWithDialogF() || preCheck == true) {
                    self.predseting.predTime.addTime.afternoon(v);
                }
                self.linkedWithDialogF(self.checkLinked(self.collectDialog()));
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
                                                   
            // Subscribe event update dialog J interlock for A7_4, A7_6, A7_12, A7_13, A7_14
            self.predseting.startDateClock.subscribe(() => { self.mainSettingModel.updateStampValue(); self.mainSettingModel.updateInterlockDialogJ(); });
            self.predseting.rangeTimeDay.subscribe(() => { self.mainSettingModel.updateStampValue(); self.mainSettingModel.updateInterlockDialogJ(); });
            self.timeZoneModelOne.end.subscribe(() => { self.mainSettingModel.updateStampValue(); self.mainSettingModel.updateInterlockDialogJ(); });                
            self.timeZoneModelTwo.start.subscribe(() => { self.mainSettingModel.updateStampValue(); self.mainSettingModel.updateInterlockDialogJ(); });
            self.timeZoneModelTwo.useAtr.subscribe(() => { self.mainSettingModel.updateStampValue(); self.mainSettingModel.updateInterlockDialogJ(); });
        }

        public collectData(oldData: any) {
            let self = this;
            oldData.startDateClock = self.dayStartTime();
        }

        public openDetailSetting() {
            let self = this;
            //open dialog F
            let dialogDataObject = self.collectDialog();
            self.linkedWithDialogF(self.checkLinked(dialogDataObject));
            
            //send data sang dialog
            nts.uk.ui.windows.setShared('KMK003_DIALOG_F_INPUT_DATA', dialogDataObject);
            nts.uk.ui.windows.sub.modal("/view/kmk/003/f/index.xhtml").onClosed(() => {
                let returnObject = nts.uk.ui.windows.getShared('KMK003_DIALOG_F_OUTPUT_DATA');
                self.predseting.predTime.addTime.oneDay(returnObject.oneDayDialog);
                self.predseting.predTime.addTime.morning(returnObject.morningDialog);
                self.predseting.predTime.addTime.afternoon(returnObject.afternoonDialog);
                self.linkedWithDialogF(self.checkLinked(returnObject));
//                if (self.linkedWithDialogF()) {
//                    //bind from parent to dialog model
//                    self.predseting.predTime.addTime.oneDay(self.predseting.predTime.predTime.oneDay());
//                    self.predseting.predTime.addTime.morning(self.predseting.predTime.predTime.morning());
//                    self.predseting.predTime.addTime.afternoon(self.predseting.predTime.predTime.afternoon());
//                }
//                else {
//                    self.predseting.predTime.addTime.oneDay(returnObject.oneDayDialog);
//                    self.predseting.predTime.addTime.morning(returnObject.morningDialog);
//                    self.predseting.predTime.addTime.afternoon(returnObject.afternoonDialog);
//                }
            });
        }
        
        private collectDialog() {
            let self = this;
            return {
                oneDayDialog: self.predseting.predTime.addTime.oneDay(),
                morningDialog: self.predseting.predTime.addTime.morning(),
                afternoonDialog: self.predseting.predTime.addTime.afternoon()
            };
        }
        
        private checkLinked(dialogDataObject: any): boolean {
            let self = this;
            return (dialogDataObject.oneDayDialog == self.predseting.predTime.predTime.oneDay()) && (dialogDataObject.morningDialog == self.predseting.predTime.predTime.morning()) && (dialogDataObject.afternoonDialog == self.predseting.predTime.predTime.afternoon());
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
