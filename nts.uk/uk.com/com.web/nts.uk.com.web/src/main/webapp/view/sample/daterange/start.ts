__viewContext.ready(function () {
    class ScreenModel {
        enable: KnockoutObservable<boolean>;
        required: KnockoutObservable<boolean>;
        dateValue: KnockoutObservable<any>;
        startDateString: KnockoutObservable<string>;
        endDateString: KnockoutObservable<string>;
        
        constructor() {
            var self = this;
            self.enable = ko.observable(true);
            self.required = ko.observable(true);
            
            self.startDateString = ko.observable("");
            self.endDateString = ko.observable("");
            self.dateValue = ko.observable({});
            
            self.startDateString.subscribe(function(value){
                self.dateValue().startDate = value;
                self.dateValue.valueHasMutated();        
            });
            
            self.endDateString.subscribe(function(value){
                self.dateValue().endDate = value;   
                self.dateValue.valueHasMutated();      
            });
        }
    }
    
    this.bind(new ScreenModel());
    
});