__viewContext.ready(function () {
    
    class ScreenModel {
        items: KnockoutObservableArray<ItemModel>;
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;
        constructor() {
            
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
            this.currentCodeList.subscribe(function (newValue) {
//                alert(newValue);    
            });
        }
        
        addItem() {
            this.items.push(new ItemModel('999', '基本給', "description 1", "other1"));
        }
        
        updateItem() {
//            this.items()[0].name = "tests";
//            this.items.valueHasMutated();
            this.currentCodeList([]);
        }
        
        removeItem() {
            this.items.shift();
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