__viewContext.ready(function () {
    class ScreenModel {
        checked: KnockoutObservable<boolean>;
        readonly: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;
        
        constructor() {
            var self = this;
            self.checked = ko.observable(true);
            self.enable = ko.observable(true);
            self.readonly = ko.observable(false);
        }
    }
    
    this.bind(new ScreenModel());
    
});