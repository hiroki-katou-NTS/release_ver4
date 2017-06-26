module qmm019.h.viewmodel {
    import option = nts.uk.ui.option;

    export class ScreenModel {
        itemList: KnockoutObservableArray<ItemWage>;
        personalWages: KnockoutObservableArray<service.model.PersonalWageNameDto>;
        selectedCode: KnockoutObservable<any>;
        selectedName: KnockoutObservable<any>;       
        categoryAtr : KnockoutObservable<number>;
        constructor() {
            var self = this;
            //self.listBox = new ListBox();
            self.itemList = ko.observableArray([]);   
            self.personalWages = ko.observableArray([]);
            self.selectedCode = ko.observable(null);
        }

      
         buildItemList(): any{
           var self = this;
//            self.itemList.removeAll();
            _.forEach(self.personalWages(), function(personalWage){
                var companyCode = personalWage.companyCode;
                if(companyCode.length == 1){
                    companyCode = "0" + personalWage.companyCode;
                }
                self.itemList.push(new ItemWage(personalWage.personalWageCode, personalWage.personalWageName));
            });          
          
        }
        
         start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();

            var categoryAtr = nts.uk.ui.windows.getShared('categoryAtr');
            service.getPersonalWageNames(categoryAtr).done(function(data: any){
                self.personalWages(data);
                self.buildItemList();
                dfd.resolve();
            }).fail(function(res) {
                alert(res);
            });
             
            // Return.
            return dfd.promise();    
        }

        chooseItem() {
            var self = this;
            var item = _.find(self.itemList(), function(item) {return item.wageCode === self.selectedCode()});
            nts.uk.ui.windows.setShared('selectedName', item.wageName);
            nts.uk.ui.windows.setShared('selectedCode', item.wageCode);
            nts.uk.ui.windows.close();
        }

        closeDialog() {
            nts.uk.ui.windows.close();
        }

    }
    export class ItemWage{
        wageCode: string;
        wageName: string;
        
        constructor(wageCode: string, wageName: string){
            this.wageCode = wageCode;
            this.wageName = wageName;    
        }
    }

}
