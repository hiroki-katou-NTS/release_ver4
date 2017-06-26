__viewContext.ready(function () {
    class ScreenModel {
        enable: KnockoutObservable<boolean>;
        selectedValue: KnockoutObservable<any>;
        selectedValue2: KnockoutObservable<any>;
        selectedValue3: KnockoutObservable<any>;
        items: KnockoutObservableArray<any>;
        option: KnockoutObservable<any>;
        
        constructor() {
            var self = this;
            self.enable = ko.observable(true);
            self.items = ko.observableArray([
                {value: 1, text: 'One'},
                {value: 2, text: 'Two'},
                {value: 3, text: 'Three'}
            ]);
            self.selectedValue = ko.observable(1);
            self.selectedValue2 = ko.observable(false);
            self.selectedValue3 = ko.observable(false);
            
            self.option = ko.observable({value: 1, text: 'Radio button'});
        }
    }
    
    this.bind(new ScreenModel());
    
});