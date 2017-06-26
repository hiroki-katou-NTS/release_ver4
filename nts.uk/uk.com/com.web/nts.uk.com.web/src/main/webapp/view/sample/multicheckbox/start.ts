__viewContext.ready(function () {
    class ScreenModel {
        itemList: KnockoutObservableArray<any>;
        selectedValues: KnockoutObservableArray<any>;
        defaultValue: KnockoutObservable<string>;
        selectedIds: KnockoutObservableArray<number>;
        enable: KnockoutObservable<boolean>;
        count: any = 10;
        value: KnockoutObservable<number>;
        
        constructor() {
            var self = this;
            self.itemList = ko.observableArray([]);
            for (let i = 1; i < 10; i++) {
                self.itemList.push(new BoxModel(i, 'box ' + i));
            }
            self.selectedValues = ko.observableArray([
                new BoxModel(1, 'box 1'), 
                new BoxModel(3, 'box 3')
            ]);
            self.selectedIds = ko.observableArray([1,2]);
            self.enable = ko.observable(false); 
            self.value = ko.observable(0);
            self.defaultValue = ko.observable('');
        }
        
        addBoxes() {
            var self = this;
            self.itemList.push(new BoxModel(self.count, 'box ' + self.count));
            self.count++;
        }
        
        removeBoxes() {
            var self = this;
            self.itemList.pop();
        }
        
        setDefault() {
            var self = this;
            nts.uk.util.value.reset($("#check-box"), self.defaultValue() !== '' ? _.map(self.defaultValue().split(","), (a) => parseInt(a)) : undefined);
        }
        
        enableCheckBox() {
            var self = this;
            if(self.value() < self.itemList().length - 1){
                self.itemList()[self.value()].enable(true);
            }
        }
    }
    
    class BoxModel {
        id: number;
        name: string;
        enable: KnockoutObservable<boolean>;
        constructor(id, name){
            var self = this;
            self.id = id;
            self.name = name;
            self.enable = ko.observable(id % 3 === 0);
        }
    }
    
    this.bind(new ScreenModel());
    
});