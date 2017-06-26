__viewContext.ready(function() {
    class ScreenModel {
        items: KnockoutObservableArray<any>;
        first: any;
        
        constructor() {
            this.items = ko.observableArray();
            this.first = ko.observable({});
        }
        
        rebind() {
            // dispose binding
            ko.cleanNode($("#grid")[0]);
            
            // update viewmodel datasource
            let newArray = testdata.createRandomHogeArray(100);
            this.first(newArray[0]);
            this.items.removeAll();
            this.items(newArray);
            
            // prepare igGrid options
            let gridOptions = {
                primaryKey: 'code',
                dataSource: this.items,
                autoCommit: true,
                width: '400px',
                height: '300px',
                autoGenerateColumns: false,
                features: [
                    {
                        name: 'Updating',
                        editMode: 'cell',
                        columnSettings: [
                            { columnKey: 'code', readOnly: true }
                        ]
                    }
                ],
                columns: [
                    {headerText: 'Code', key: 'code', dataType: 'string', width: 100},
                    {headerText: 'Name', key: 'name', dataType: 'string', width: 200},
                ]
            };
            
            // apply bindings
            ko.applyBindingsToNode($('#grid')[0], { igGrid: gridOptions }, this);
        }
    }
    
    this.bind(new ScreenModel());
    
    
    
});

