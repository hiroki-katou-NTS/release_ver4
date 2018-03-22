module nts.uk.com.view.cmf001.l.viewmodel {
    import model = nts.uk.com.view.cmf001.share.model;
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        compareItems: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getCompareTypes());
        acceptScreenConditionSetting: KnockoutObservable<model.AcceptScreenConditionSetting> = ko.observable(new model.AcceptScreenConditionSetting(null, 0, null, null, null, null, null, null, null, null, null, null));
        selectedDataType: number;
        selectComparisonCondition: KnockoutObservable<number> = ko.observable(0);
        inputMode: boolean = true;
        valueSelected: KnockoutObservable<number>;
        requiredCond1: KnockoutObservable<boolean> = ko.observable(false);
        requiredCond2: KnockoutObservable<boolean> = ko.observable(false);
        enabledCond1: KnockoutObservable<boolean> = ko.observable(false);
        enabledCond2: KnockoutObservable<boolean> = ko.observable(false);
        receiptItemName : KnockoutObservable<string> = ko.observable('');
        numberConditionValue1: KnockoutObservable<number> = ko.observable(null);
        numberConditionValue2: KnockoutObservable<number> = ko.observable(null);
        characterConditionValue1: KnockoutObservable<string> = ko.observable(null);
        characterConditionValue2:  KnockoutObservable<string> = ko.observable(null);
        dateConditionValue1: KnockoutObservable<string> = ko.observable(null);
        dateConditionValue2: KnockoutObservable<string> = ko.observable(null);
        timeConditionValue2: KnockoutObservable<number> = ko.observable(null);
        timeConditionValue1: KnockoutObservable<number> = ko.observable(null);
        timeMomentConditionValue2: KnockoutObservable<number> = ko.observable(null);
        timeMomentConditionValue1: KnockoutObservable<number> = ko.observable(null);
        
        constructor() {
            var self = this;
            let params = getShared("CMF001lParams");
            let inputMode = params.inputMode;
            self.selectedDataType = params.dataType;
            self.inputMode = inputMode;
            self.selectComparisonCondition.subscribe(function(selectedValue) {
                if (selectedValue == 0) {
                    self.enabledCond1(false);
                    self.enabledCond2(false);
                    self.requiredCond1(false);
                    self.requiredCond2(false);
                    nts.uk.ui.errors.clearAll();
                } else if (selectedValue == 1 || selectedValue == 2 || selectedValue == 3 || selectedValue == 4 || selectedValue == 9 || selectedValue == 10) {
                    self.enabledCond1(true);
                    self.enabledCond2(false);
                    self.requiredCond1(true);
                    self.requiredCond2(false);
                } else if (selectedValue == 5 || selectedValue == 6 || selectedValue == 7 || selectedValue == 8) {
                    self.enabledCond1(true);
                    self.enabledCond2(true);
                    self.requiredCond1(true);
                    self.requiredCond2(true);
                }
            });
            if (params.condition) {
                let condition = params.condition;
                self.receiptItemName(condition.receiptItemName);
                self.selectComparisonCondition(condition.selectComparisonCondition);
                self.numberConditionValue1(condition.numberConditionValue1);
                self.numberConditionValue2(condition.numberConditionValue2);
                self.characterConditionValue1(condition.characterConditionValue1);
                self.characterConditionValue2(condition.characterConditionValue2);
                self.dateConditionValue1(condition.dateConditionValue1);
                self.dateConditionValue2(condition.dateConditionValue2);
                self.timeConditionValue1(condition.timeConditionValue1);
                self.timeConditionValue2(condition.timeConditionValue2);
                self.timeMomentConditionValue1(condition.timeMomentConditionValue1);
                self.timeMomentConditionValue2(condition.timeMomentConditionValue2);
                self.selectComparisonCondition.valueHasMutated();
            }
        }

 
        /**
        * Close dialog.
        */
        cancelSetting(): void {
            setShared('CMF001lCancel', true);
            nts.uk.ui.windows.close();
        }

        saveSetting() {
            var self = this;
            let value = self.selectedDataType;
            switch (value) {
                case model.ITEM_TYPE.NUMERIC:
                    $(".numberCondition").trigger("validate");
                    break;
                case model.ITEM_TYPE.CHARACTER:
                    $(".characterCondition").trigger("validate");
                    break;
                case model.ITEM_TYPE.DATE:
                    $(".dateCondition").trigger("validate");
                    break;
                case model.ITEM_TYPE.TIME:
                    $(".timeCondition").trigger("validate");
                    break;
                case model.ITEM_TYPE.INS_TIME:
                    $(".timeMomentCondition").trigger("validate");
                    break;
            }
            if (!nts.uk.ui.errors.hasError()) {
                self.acceptScreenConditionSetting().receiptItemName(self.receiptItemName());
                self.acceptScreenConditionSetting().numberConditionValue1(self.numberConditionValue1());
                self.acceptScreenConditionSetting().numberConditionValue2(self.numberConditionValue2());
                self.acceptScreenConditionSetting().characterConditionValue1(self.characterConditionValue1());
                self.acceptScreenConditionSetting().characterConditionValue2(self.characterConditionValue2());
                self.acceptScreenConditionSetting().dateConditionValue1(moment.utc(self.dateConditionValue1(), "YYYY/MM/DD").toISOString());
                self.acceptScreenConditionSetting().dateConditionValue2(moment.utc(self.dateConditionValue2(), "YYYY/MM/DD").toISOString());
                self.acceptScreenConditionSetting().timeConditionValue1(self.timeConditionValue1());
                self.acceptScreenConditionSetting().timeConditionValue2(self.timeConditionValue2());
                self.acceptScreenConditionSetting().timeMomentConditionValue1(self.timeMomentConditionValue1());
                self.acceptScreenConditionSetting().timeMomentConditionValue2(self.timeMomentConditionValue2());
                self.acceptScreenConditionSetting().selectComparisonCondition(self.selectComparisonCondition());
                setShared('CMF001lOutput', ko.toJS(self.acceptScreenConditionSetting), true);
                nts.uk.ui.windows.close();
            }

        }
    }
}