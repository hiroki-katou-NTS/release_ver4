__viewContext.ready(function () {
    class ScreenModel {
        value: KnockoutObservable<string>;
        enable: KnockoutObservable<boolean>;
        
        constructor() {
            var self = this;
            
            self.value = ko.observable('');
            self.enable = ko.observable(true);
        }
    }
    
    this.bind(new ScreenModel());
    
});