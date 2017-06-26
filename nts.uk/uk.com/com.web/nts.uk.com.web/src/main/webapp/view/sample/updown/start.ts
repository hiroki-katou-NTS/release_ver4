__viewContext.ready(function() {

    class ScreenModel {
        itemsSwap: KnockoutObservableArray<ItemModel>;
        items1: KnockoutObservableArray<Node>;
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCodeListSwap: KnockoutObservableArray<any>;
        test: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservableArray<any>;
        testSingle: KnockoutObservable<any>;
        columns2: KnockoutObservableArray<any>;

        constructor() {

            this.itemsSwap = ko.observableArray([]);
            
            var array = [];
            for (var i = 0; i < 10000; i++) {
                array.push(new ItemModel("test" + i, '基本給', "description"));
            }
            this.itemsSwap(array);

            this.columns = ko.observableArray([
                { headerText: 'コード', key: 'code', width: 100 },
                { headerText: '名称', key: 'name', width: 150 }
            ]);
            this.items1 = ko.observableArray([]);
            for(let i = 1; i <= 2; i++) {
                let level1 = new Node('0000' + i, 'サービス部' + i, []);
                for(let j = 1; j <= 2; j++) {
                    let ij = i + "" + j;
                    let level2 = new Node('0000' + ij, 'サービス部' + ij, []);
                    level1.childs.push(level2);
                    for(let k = 1; k <= 2; k++) {
                        let  ijk = ij + "" + k;
                        let level3 = new Node('0000' + ijk, 'サービス部' + ijk, []);
                        level2.childs.push(level3);
                        for(let l = 1; l <= 2; l++) {
                            let  ijkl = ijk + "" + l;
                            let level4 = new Node('0000' + ijkl, 'サービス部' + ijkl, []);
                            level3.childs.push(level4);
                            for(let n = 1; n <= 2; n++) {
                                let  ijkln = ijkl + "" + n;
                                let level5 = new Node('0000' + ijkln, 'サービス部' + ijkln, []);
                                level4.childs.push(level5);
                            }
                        }
                    }   
                }
                this.items1.push(level1);
            }
            this.columns2 = ko.observableArray([{ headerText: "Item Code", width: "250px", key: 'code', dataType: "string", hidden: false },
            { headerText: "Item Text", key: 'nodeText', width: "200px", dataType: "string" }]);
            this.selectedCode = ko.observableArray([]);
            this.currentCodeListSwap = ko.observableArray([]);
            this.test = ko.observableArray([]);
            this.testSingle = ko.observable(null);
        }
        
        remove(){
            this.itemsSwap.shift();            
        }
        
    }
    
    class ItemModel {
        code: string;
        name: string;
        description: string;
        constructor(code: string, name: string, description: string) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
    }

    class Node {
        code: string;
        name: string;
        nodeText: string;
        custom: string;
        childs: Array<Node>;
        constructor(code: string, name: string, childs: Array<Node>) {
            var self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;
            self.custom = 'Random' + new Date().getTime();
        }
    }

    this.bind(new ScreenModel());

});
