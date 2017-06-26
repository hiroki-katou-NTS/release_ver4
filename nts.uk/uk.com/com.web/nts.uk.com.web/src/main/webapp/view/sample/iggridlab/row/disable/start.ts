__viewContext.ready(function() {
    class ScreenModel {
        items: KnockoutObservableArray<any>;
        first: any;
        
        constructor() {
            this.items = ko.observableArray(testdata.createHogeArray(100));
            this.first = this.items()[0];
        }
        
        activeRowChanging(evt, ui) {
            if (ui.row !== null && ui.row.id === '2'){
                return false;
            }
        }
        
        rowSelectionChanging(evt, ui) {
            if (ui.row !== null && ui.row.id === '2') {
                return false;
            }
        }
    }
    
    this.bind(new ScreenModel());
});

