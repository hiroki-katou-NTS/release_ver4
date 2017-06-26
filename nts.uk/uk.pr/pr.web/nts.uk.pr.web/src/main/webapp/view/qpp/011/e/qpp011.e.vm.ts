// TreeGrid Node
module qpp011.e {
    export class ScreenModel {
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;
        enable: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.enable = ko.observable(true);
            self.roundingRules = ko.observableArray([
                { code: '1', name: '付加する' },
                { code: '2', name: '付加しない' }
            ]);
            self.selectedRuleCode = ko.observable(1);
        }
        submitDialog() {
            nts.uk.ui.windows.close();
        }
        closeDialog() {
            nts.uk.ui.windows.close();
        }
    }

};
