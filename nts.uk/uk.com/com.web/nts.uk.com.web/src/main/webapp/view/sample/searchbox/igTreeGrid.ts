__viewContext.ready(function () {
    class ScreenModel {
        dataSource: any;
        selectedValue: any;
        selectedValues: any;
        constructor() {
            var self = this;
            var data = [new Node('0001', 'Hanoi Vietnam', []),
                new Node('0003', 'Bangkok Thailand', []),
                new Node('0004', 'Tokyo Japan', []),
                new Node('0005', 'Jakarta Indonesia', []), 
                new Node('0002', 'Seoul Korea', []),
                new Node('0006', 'Paris France', []),
                new Node('0007', 'United States', [new Node('0008', 'Washington US', [new Node('0008-1', 'Wasford US', []),new Node('0008-2', 'Newmece US', [])]),new Node('0009', 'Newyork US', [])]),                             
                new Node('0010', 'Beijing China', []),
                new Node('0011', 'London United Kingdom', []),
                new Node('0012', '', [])];
            //primaryKey for treegrid: "code"
            var primaryKey = "code";
            //enable checkbox if you wish a multimode
            var multiple = true;
            //childField
            //columns for treegrid
            var columns = [{ headerText: "Item Code", width: "250px", key: 'code', dataType: "string", hidden: false },
            { headerText: "Item Text", key: 'nodeText', width: "200px", dataType: "string" }];
            //dataSource that can be apply to SearchBox Binding
            self.dataSource = data;
            //selectedValue(s) : depend on desire mode 'multiple' or 'single'. but array good for both case
            self.selectedValue = ko.observableArray([]);
            var $treegrid = $("#treegrid");
            self.selectedValue.subscribe(function (newValue) {
                var selectedRows = _.map($treegrid.igTreeGridSelection("selectedRows"), function(row) {
                    return row.id;
                });
                if(!_.isEqual(selectedRows, newValue)) {
                    $treegrid.igTreeGridSelection("clearSelection");
                    newValue.forEach(function (id) {
                        $treegrid.igTreeGridSelection("selectRowById" , id);
                    });
                }
            });
            
            var treeGridId = "treegrid";
            $treegrid.igTreeGrid({
                width: 700,
                height: 250,
                dataSource: data,
                primaryKey: primaryKey,
                columns: columns,
               
                initialExpandDepth: 10,
                features: [
                    {
                        name: "Selection",
                        multipleSelection: true,
                        activation: true,
                        rowSelectionChanged: function(evt: any, ui: any) {
                            var selectedRows: Array<any> = ui.selectedRows;
                            self.selectedValue(_.map(selectedRows, function(row) {
                                return row.id;
                            }));                        
                        }
                    },
                    {
                        name: "RowSelectors",
                        enableCheckBoxes: multiple,
                        checkBoxMode: "biState"
                    }]
            });
            $treegrid.closest('.ui-igtreegrid').addClass('nts-treegridview');           
            $treegrid.setupSearchScroll("igTreeGrid");
        }               
    }
    class Node {
        code: string;
        name: string;
        nodeText: string;
        childs: any;
        constructor(code: string, name: string, childs: Array<Node>) {
            var self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;         
        }
    }  
    this.bind(new ScreenModel());   
});

