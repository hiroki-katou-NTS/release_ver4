__viewContext.ready(function () {
    class ScreenModel {
        dataSource: any;
        dataSource2: any;      
        singleSelectedCode: any;
        singleSelectedCode2: any;
        selectedCodes: any;
        selectedCodes2: any;
        headers: any;
        items: KnockoutObservableArray<ItemModel>;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;
        
        constructor() {
            var self = this;
            self.dataSource = ko.observableArray([new Node('0001', 'Hanoi Vietnam', []),
                new Node('0003', 'Bangkok Thailand', []),
                new Node('0004', 'Tokyo Japan', []),
                new Node('0005', 'Jakarta Indonesia', []), 
                new Node('0002', 'Seoul Korea', []),
                new Node('0006', 'Paris France', []),
                new Node('0007', 'United States', [new Node('0008', 'Washington US', [new Node('0008-1', 'Wasford US', []),new Node('0008-2', 'Newmece US', [])]),new Node('0009', 'Newyork US', [])]),                             
                new Node('0010', 'Beijing China', []),
                new Node('0011', 'London United Kingdom', []),
                new Node('0012', '', [])]);
            self.dataSource2 = ko.observableArray([new Node('0001', 'Hanoi Vietnam', []),
                new Node('0003', 'Bangkok Thailand', []),
                new Node('0004', 'Tokyo Japan', []),
                new Node('0005', 'Jakarta Indonesia', []), 
                new Node('0002', 'Seoul Korea', []),
                new Node('0006', 'Paris France', []),
                new Node('0007', 'United States', [new Node('0008', 'Washington US', []),new Node('0009', 'Newyork US', [])]),                             
                new Node('0010', 'Beijing China', []),
                new Node('0011', 'London United Kingdom', []),
                new Node('0012', '', [])]);            
            self.singleSelectedCode = ko.observable(null);
            self.singleSelectedCode2 = ko.observable(null);
            self.selectedCodes = ko.observableArray([]);
            self.selectedCodes2 = ko.observableArray([]);            
            self.headers = ko.observableArray(["Item Value Header","Item Text Header"]);
            
            this.items = ko.observableArray([]);
            var str = ['a0', 'b0', 'c0', 'd0'];
            for(var j = 0; j < 4; j++) {
                for(var i = 1; i < 51; i++) {    
                    var code = i < 10 ? str[j] + '0' + i : str[j] + i;         
                    this.items.push(new ItemModel(code,code,code,code));
                } 
            }
            this.columns = ko.observableArray([
                { headerText: 'コード', prop: 'code', width: 100 },
                { headerText: '名称', prop: 'name', width: 230 },
                { headerText: '説明', prop: 'description', width: 150 },
                { headerText: '説明1', prop: 'other1', width: 150 },
                { headerText: '説明2', prop: 'other2', width: 150 }
            ]);
            this.currentCode = ko.observable();
            this.currentCodeList = ko.observableArray([]);
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
    class ItemModel {
        code: string;
        name: string;
        description: string;
        other1: string;
        other2: string;
        constructor(code: string, name: string, description: string, other1?: string, other2?: string) {
            this.code = code;
            this.name = name;
            this.description = description;
            this.other1 = other1;
            this.other2 = other2 || other1;         
        }
    } 
    this.bind(new ScreenModel());
    
});