module qmm018.b.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<shr.viewmodelbase.ItemModel>;
        currentCodeListSwap: KnockoutObservableArray<shr.viewmodelbase.ItemModel>; // Item Selected
        oldCurrentCodeListSwap: KnockoutObservableArray<shr.viewmodelbase.ItemModel>; // Item selected form A screen, n = 0: ItemSalary, n = 2: ItemAttend
        constructor() {
            var self = this;
            self.items = ko.observableArray([]);
            self.currentCodeListSwap = ko.observableArray([]);
            self.oldCurrentCodeListSwap = ko.observableArray([]);
        }
        
        /**
         * get init data
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            qmm018.b.service.itemSelect(nts.uk.ui.windows.getShared('categoryAtr')).done(function(data) { // get item master list
                if(!data.length) {
                    $("#label-span").ntsError('set', shr.viewmodelbase.Error.ER010);    
                }
                else {
                    data.forEach(function(dataItem){
                        self.items().push(new shr.viewmodelbase.ItemModel(dataItem.itemCode,dataItem.itemAbName));
                    });
                    self.currentCodeListSwap.subscribe(function(value){
                        if(!value.length) $("#label-span").ntsError('set', shr.viewmodelbase.Error.ER007);
                        else $("#label-span").ntsError('clear');    
                    });
                }
                dfd.resolve();
                self.currentCodeListSwap(nts.uk.ui.windows.getShared('selectedItemList'));
                self.oldCurrentCodeListSwap(nts.uk.ui.windows.getShared('selectedItemList'));
            }).fail(function(res) {
            });
            return dfd.promise();
        }
        
        /**
         * send changed data to A screen
         */
        submitData(): void {
            // return new data
            var self = this;
            nts.uk.ui.windows.setShared('selectedItemList', self.currentCodeListSwap());
            nts.uk.ui.windows.close();
        }
        
        /**
         * back to A screen without change
         */
        closeWindow(): void {
            // return old data
            var self = this;
            nts.uk.ui.windows.setShared('selectedItemList', self.oldCurrentCodeListSwap());
            nts.uk.ui.windows.close();
        }
    }
}