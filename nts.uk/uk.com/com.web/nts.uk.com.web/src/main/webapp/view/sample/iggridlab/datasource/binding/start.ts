__viewContext.ready(function() {
    class ScreenModel {
        items: KnockoutObservableArray<any>;
        first: any;
        
        constructor() {
            this.items = ko.observableArray(testdata.createHogeArray(100));
            this.first = this.items()[0];
        }
    }
    
    this.bind(new ScreenModel());
});

