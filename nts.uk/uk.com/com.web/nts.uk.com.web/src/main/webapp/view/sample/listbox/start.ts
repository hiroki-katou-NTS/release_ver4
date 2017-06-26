__viewContext.ready(function () {
    class ScreenModel {
        itemList: KnockoutObservableArray<ItemModel>;
        itemName: KnockoutObservable<string>;
        currentCode: KnockoutObservable<number>
        selectedCode: KnockoutObservable<number>;
        selectedCodes: KnockoutObservableArray<number>;
        isEnable: KnockoutObservable<boolean>; 
        isMulti: KnockoutObservable<boolean>;
        isMulti2: KnockoutObservable<boolean>;
        isValidate: KnockoutObservable<boolean>;
        
        constructor() {
            var self = this;
            var temp = [];
            for(var i = 0; i < 100; i++){
                temp.push(new ItemModel((i + 1), '基本給', "description " + (i + 1)));
            }
            self.itemList = ko.observableArray(temp);
            self.itemName = ko.observable('');
            self.currentCode = ko.observable(3);
            self.selectedCode = ko.observable(temp[2]);
            self.selectedCode.subscribe(function (value){
                console.log(value);        
            });
            self.selectedCodes = ko.observableArray([]);
            self.selectedCodes.subscribe(function (value){
                console.log(value);        
            });
            self.isEnable = ko.observable(true);
            self.isMulti = ko.observable(true);
            self.isMulti2 = ko.observable(true);
            self.isValidate = ko.observable(true);
        }

        addOptions() {
            var self = this;
            var newCode = self.currentCode() + 1;
            var itemCode = newCode;
            self.itemList.push(new ItemModel(itemCode, self.itemName(), ""));
            self.currentCode(newCode);
        }
        
        deselectAll() {
            $('#list-box').ntsListBox('deselectAll');
        }
        
        selectAll() {
            $('#list-box').ntsListBox('selectAll');
        }
        
        /**
         * Clear options.
         */
        clearOptions() {
            var self = this;
            self.itemList([]);
        }
        
        /**
         * Remove item by code;
         */
        remove() {
            var self = this;
            
            // Remove by code.
            var selected: ItemModel = self.itemList().filter(item => item.code === self.selectedCode().code)[0];
            self.itemList.remove(selected);
            
            // Remove by codes
            var selecteds: ItemModel[] = self.itemList().filter(item => self.selectedCodes().indexOf(item) != -1);
            self.itemList.removeAll(selecteds);
        }
    }
    
    class ItemModel {
        code: number;
        name: string;
        description: string;
        
        constructor(code: number, name: string, description: string) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
    }

    
    this.bind(new ScreenModel());
    
});