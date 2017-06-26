__viewContext.ready(function () {
    class ScreenModel {
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;
        enable: KnockoutObservable<boolean>;
        defaultValue: KnockoutObservable<string>;
        constructor() {
            var self = this;
            self.enable = ko.observable(true);
            self.roundingRules = ko.observableArray([
                { code: '1', name: '四捨五入' },
                { code: '2', name: '切り上げ' },
                { code: '3', name: '切り捨て' }
            ]);
            self.selectedRuleCode = ko.observable();
            self.defaultValue = ko.observable();
        }
        
        setDefault() {
            var self = this;
            nts.uk.util.value.reset($("#switch-buttons"), self.defaultValue() !== '' ? self.defaultValue() : undefined);
        }
    }
    
    this.bind(new ScreenModel());
    
});