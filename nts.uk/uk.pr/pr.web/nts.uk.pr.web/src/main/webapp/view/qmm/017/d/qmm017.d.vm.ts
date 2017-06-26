module nts.qmm017 {
    export class ListBox {
        itemList: KnockoutObservableArray<any>;
        itemName: KnockoutObservable<string>;
        currentCode: KnockoutObservable<number>
        selectedCode: KnockoutObservable<any>;
        isEnable: KnockoutObservable<boolean>;

        constructor(data) {
            var self = this;
            self.itemList = ko.observableArray(data);
            self.itemName = ko.observable('');
            self.currentCode = ko.observable(3);
            self.selectedCode = ko.observable('');
            self.isEnable = ko.observable(true);
        }
    }
    
    export class DScreen {
        listBoxItemType: KnockoutObservable<ListBox>;
        listBoxItems: KnockoutObservable<ListBox>;

        constructor() {
            var self = this;
            var dList001 = [
                { code: '1', name: '支給項目（支給＠）' },
                { code: '2', name: '控除項目（控除＠）' },
                { code: '3', name: '勤怠項目（勤怠＠）' }
            ];
            self.listBoxItemType = ko.observable(new ListBox(dList001));
            self.listBoxItems = ko.observable(new ListBox([]));
            self.listBoxItemType().selectedCode.subscribe(function(codeChange){
                if(codeChange === '1') {
                    service.getListItemMaster(0).done(function(lstItem: Array<model.ItemMasterDto>){
                        self.listBoxItems().itemList([]);
                        _.forEach(lstItem, function(item){
                            self.listBoxItems().itemList.push({code: item.itemCode, name: item.itemName});
                        });
                    });
                } else if(codeChange === '2') {
                    service.getListItemMaster(1).done(function(lstItem: Array<model.ItemMasterDto>){
                        self.listBoxItems().itemList([]);
                        _.forEach(lstItem, function(item){
                            self.listBoxItems().itemList.push({code: item.itemCode, name: item.itemName});
                        });
                    });
                } else if(codeChange === '3') {
                    service.getListItemMaster(2).done(function(lstItem: Array<model.ItemMasterDto>){
                        self.listBoxItems().itemList([]);
                        _.forEach(lstItem, function(item){
                            self.listBoxItems().itemList.push({code: item.itemCode, name: item.itemName});
                        });
                    });
                } 
            });
        }
    }
}