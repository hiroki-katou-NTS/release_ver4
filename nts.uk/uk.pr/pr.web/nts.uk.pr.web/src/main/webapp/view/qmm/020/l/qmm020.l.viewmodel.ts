module qmm020.l.viewmodel {
    import option = nts.uk.ui.option;
    export class ScreenModel {
        selectLayoutAtr: KnockoutObservable<string>;
        itemList: KnockoutObservableArray<ItemModel>;
        isEnable: KnockoutObservable<boolean>
        //Switch マスタ
        roundingRulesMaster: KnockoutObservableArray<any>;
        //Switch 個人
        roundingRulesEmployee: KnockoutObservableArray<any>;
        selectedRuleCodeMaster: any;
        selectedRuleCodeEmployee: any;
        selectedValue : KnockoutObservable<boolean>;
        //Group ListRadio
        itemListRadio: KnockoutObservableArray<any>;
        selectedId: KnockoutObservable<number>;
        enable: KnockoutObservable<boolean>
        
        constructor() {
            var self = this;
            self.selectLayoutAtr = ko.observable("3");
            self.itemList = ko.observableArray([]);
            self.isEnable = ko.observable(true);
            
            //self.selectedValue = ko.observable(2);
            //Switch　マスタ
            self.roundingRulesMaster = ko.observableArray([
                { code: '1', name: '利用する' },
                { code: '2', name: '利用しない' }
            ]);
            self.selectedRuleCodeMaster = ko.observable(1);
            //Switch 個人
            self.roundingRulesEmployee = ko.observableArray([
                { code: '1', name: '利用する' },
                { code: '2', name: '利用しない' }
            ]);
            self.selectedRuleCodeEmployee = ko.observable(1);
            //Get value when click to switch Master
            self.selectedRuleCodeMaster.subscribe(function(codeChange) {
                //alert(self.selectedRuleCodeMaster());
                //self.selectedRuleCodeMaster = ko.observable(0);
            });
            //Group List radio 
            self.itemListRadio = ko.observableArray([
                new BoxModel(1, '雇用'),
                new BoxModel(2, '部門'),
                new BoxModel(3, '分類'),
                new BoxModel(4, '職位'),
                new BoxModel(5, '給与分類')
            ]);
            self.selectedId = ko.observable(1);
            self.enable = ko.observable(true);

        }
         
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();   
            
            
             dfd.resolve();
            // Return.
            return dfd.promise();    
        }
        
        startDialog() : any{
            var self = this;     
        }
        
       //Close dialog
       closeDialog(): any{
            nts.uk.ui.windows.close();   
       }
       //Setting Button 
        settingScreen() {
            var self = this;
            var settingValue = self.selectedRuleCodeMaster() +"~"+ self.selectedId() +"~" + self.selectedRuleCodeEmployee();
            nts.uk.ui.windows.setShared('arrSettingVal', settingValue);
            nts.uk.ui.windows.close();
        }
    }
    
    //Item Group Radio 
    class BoxModel {
        id: number;
        name: string;
        constructor(id, name){
            var self = this;
            self.id = id;
            self.name = name;
        }
    }
     /**
     * Class Item model.
     */
    export class ItemModel {
        stt: string;
        printType: string;
        paperType: string;
        direction: string;
        numberPeople: string;
        numberDisplayItems: string;
        reference: string;
        
        
        constructor(stt: string,printType: string, paperType: string, direction: string, numberPeople: string, numberDisplayItems: string, reference: string) {
            this.stt = stt;
            this.printType = printType;
            this.paperType = paperType;
            this.direction = direction;
            this.numberPeople = numberPeople;
            this.numberDisplayItems = numberDisplayItems;
            this.reference = reference;
        }
    }
    
}