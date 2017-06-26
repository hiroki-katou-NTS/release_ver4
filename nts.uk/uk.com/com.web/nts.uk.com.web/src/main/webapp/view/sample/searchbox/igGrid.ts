var multiple = true;
var products = [
    { "ProductID": 1, "Name": "Adjustable Race", "ProductNumber": "AR-5381" },
    { "ProductID": 2, "Name": "Bearing Ball", "ProductNumber": "BA-8327" },
    { "ProductID": 3, "Name": "BB Ball Bearing", "ProductNumber": "BE-2349" },
    { "ProductID": 4, "Name": "Headset Ball Bearings", "ProductNumber": "BE-2908" },
    { "ProductID": 316, "Name": "Blade", "ProductNumber": "BL-2036" },
    { "ProductID": 317, "Name": "LL Crankarm", "ProductNumber": "CA-5965" },
    { "ProductID": 318, "Name": "ML Crankarm", "ProductNumber": "CA-6738" },
    { "ProductID": 319, "Name": "HL Crankarm", "ProductNumber": "CA-7457" },
    { "ProductID": 320, "Name": "Chainring Bolts", "ProductNumber": "CB-2903" },
    { "ProductID": 5, "Name": "Adustable Race", "ProductNumber": "AR-5383" },
    { "ProductID": 6, "Name": "Adjusable Race", "ProductNumber": "AR-5389" },
    { "ProductID": 7, "Name": "djustable Race", "ProductNumber": "AR-5387" }
];
__viewContext.ready(function () {
    class ScreenModel {
        dataSource: any;
        selectedList: any;
        constructor() {
            var self = this;
            self.dataSource = products;
            self.selectedList = ko.observableArray([]);
            var features = [];
            features.push({
                name: 'Selection',
                mode: 'row',
                multipleSelection: multiple,
                activation: false,
                rowSelectionChanged: this.selectionChanged.bind(this)
            });
            features.push({ name: 'Sorting', type: 'local' });
            features.push({ name: 'RowSelectors', enableCheckBoxes: multiple, enableRowNumbering: true });
            $("#grid").igGrid({
               columns: [
                   { headerText: "Product ID", key: "ProductID", dataType: "number" },
                   { headerText: "Product Name", key: "Name", dataType: "string" },
                   { headerText: "Product Number", key: "ProductNumber", dataType: "string" }
               ],
               features: [{
                    name: 'Selection',
                    mode: 'row',
                    multipleSelection: true,
                    activation: false,
                    rowSelectionChanged: this.selectionChanged.bind(this)
                },
                    { name: 'Sorting', type: 'local' },
                    { name: 'RowSelectors', enableCheckBoxes: true, enableRowNumbering: true }
               ],
               virtualization: true,
               virtualizationMode: 'continuous',
               width: "500px",
               height: "240px",
               primaryKey: "ProductID",
               dataSource: self.dataSource
           });
           $("#grid").closest('.ui-iggrid').addClass('nts-gridlist');
           $("#grid").setupSearchScroll("igGrid", true);
        }
        selectionChanged (evt, ui) {
            //console.log(evt.type);
            var selectedRows = ui.selectedRows;
            var arr = [];
            for(var i = 0; i < selectedRows.length; i++) {
                arr.push(""+selectedRows[i].id);
            }
            this.selectedList(arr);
        };              
    }
    this.bind(new ScreenModel());
    
});
