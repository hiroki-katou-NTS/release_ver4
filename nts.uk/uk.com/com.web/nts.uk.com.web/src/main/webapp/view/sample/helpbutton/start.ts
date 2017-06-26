__viewContext.ready(function () {
    class ScreenModel {
        enable: KnockoutObservable<boolean>;
        
        constructor() {
            var self = this;
            self.enable = ko.observable(true);
        }
    }
    
    this.bind(new ScreenModel());
    
});