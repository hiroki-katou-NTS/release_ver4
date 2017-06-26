__viewContext.ready(function() {
    var products = [
        { "ProductID": 1, "Name": "Adjustable Race", "ProductNumber": "AR-5381" },
        { "ProductID": 2, "Name": "Bearing Ball", "ProductNumber": "BA-8327" },
        { "ProductID": 3, "Name": "BB Ball Bearing", "ProductNumber": "BE-2349" },
        { "ProductID": 4, "Name": "Headset Ball Bearings", "ProductNumber": "BE-2908" },
        { "ProductID": 5, "Name": "Blade", "ProductNumber": "BL-2036" },
        { "ProductID": 6, "Name": "LL Crankarm", "ProductNumber": "CA-5965" },
        { "ProductID": 7, "Name": "ML Crankarm", "ProductNumber": "CA-6738" },
        { "ProductID": 8, "Name": "HL Crankarm", "ProductNumber": "CA-7457" },
        { "ProductID": 9, "Name": "Chainring Bolts", "ProductNumber": "CB-2903" },
        { "ProductID": 10, "Name": "Adustable Race", "ProductNumber": "AR-5383" },
        { "ProductID": 11, "Name": "Adjusable Race", "ProductNumber": "AR-5389" },
        { "ProductID": 12, "Name": "djustable Race", "ProductNumber": "AR-5387" }
    ];

    class ScreenModel {
        htmlString: KnockoutObservable<string>;
        dataSource: any;

        constructor() {
            var self = this;
            self.dataSource = products;
            self.htmlString = ko.observable("<script>alert('You have been attacked!')</script>");

            // Grid settings
            var columns = [
                { headerText: "Product ID", key: "ProductID", dataType: "number" },
                { headerText: "Product Name", key: "Name", dataType: "string" },
                { headerText: "Product Number", key: "ProductNumber", dataType: "string" }
            ];
            var xssColumns = [
                { headerText: "Product ID", key: "ProductID", dataType: "number" },
                { headerText: "Product Name", key: "Name", dataType: "string", formatter: _.escape },
                { headerText: "Product Number", key: "ProductNumber", dataType: "string", formatter: _.escape }
            ];
            var features = [
                { name: "Selection" },
                { name: 'Sorting', type: 'local' },
                { name: 'RowSelectors', enableCheckBoxes: true, enableRowNumbering: true },
                { name: "Updating", editMode: "dialog", enableAddRow: false, columnSettings: [{ columnKey: "ProductID", readOnly: true }] }
            ];
            // Init Grid
            $("#grid").igGrid({
                columns: columns,
                features: features,
                height: "350px",
                primaryKey: "ProductID",
                dataSource: self.dataSource
            });
            $("#grid-xss").igGrid({
                columns: xssColumns,
                features: features,
                height: "350px",
                primaryKey: "ProductID",
                dataSource: self.dataSource
            });
        }

        XSSattack() {
            var self = this;
            $("#display-message").html(self.htmlString());
        }

        PreventXSSattack() {
            var self = this;
            $("#display-message").html(_.escape(self.htmlString()));
        }
    }

    this.bind(new ScreenModel());

});