module kdl024.a.viewmodel {
    export class ScreenModel {
        contraint: Array<string>;
        // Mode
        isNew: KnockoutObservable<boolean>;
        // BudgetList
        listBudget: KnockoutObservableArray<BudgetItemDto>;
        selectedBudgetCode: KnockoutObservable<string>;
        columns: KnockoutObservableArray<NtsGridListColumn>;
        // Details Item
        currentItem: KnockoutObservable<BudgetItem>;
        // Switch Rouding Rules
        roundingRules: KnockoutObservableArray<any>;
        // Combobox Section Type
        itemListCbb: KnockoutObservableArray<ItemModelCbb>;

        constructor() {
            var self = this;
            // Mode
            self.isNew = ko.observable(false);
            // Budget list
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KDL024_5'), key: 'externalBudgetCode', width: 40 },
                { headerText: nts.uk.resource.getText('KDL024_6'), key: 'externalBudgetName', width: 150 }
            ]);
            self.listBudget = ko.observableArray([]);
            self.selectedBudgetCode = ko.observable('');
            self.selectedBudgetCode.subscribe((newCode) => {
                if (nts.uk.ui._viewModel !== undefined)
                    $('.nts-checkvalue').ntsError('clear');
                self.findBudgetByCode(newCode);
            });
            // Details
            self.currentItem = ko.observable(null)
            // Switch button 
            self.roundingRules = ko.observableArray([
                { unitId: 0, unitName: nts.uk.resource.getText('KDL024_10') },
                { unitId: 1, unitName: nts.uk.resource.getText('KDL024_11') }
            ]);
            // Combobox Section Type
            self.itemListCbb = ko.observableArray([
                new ItemModelCbb(0, nts.uk.resource.getText('Enum_Attribute_Section_Time')),
                new ItemModelCbb(1, nts.uk.resource.getText('Enum_Attribute_Section_PeopleNum')),
                new ItemModelCbb(2, nts.uk.resource.getText('Enum_Attribute_Section_Money')),
                new ItemModelCbb(3, nts.uk.resource.getText('Enum_Attribute_Section_Numeric')),
                new ItemModelCbb(4, nts.uk.resource.getText('Enum_Attribute_Section_Price'))
            ]);
        }

        /** Start page */
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            // Get list budget   
            service.getListExternalBudget().done(function(lstBudget: IBudgetItem) {
                if (lstBudget.length > 0) {
                    self.isNew(false);
                    self.listBudget(lstBudget);
                    self.findItemByIndex(0);
                }
                else {
                    self.isNew(true);
                    self.newMode();
                }
                dfd.resolve();
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.message);
                dfd.reject();
            });
            return dfd.promise();
        }

        //Register Data
        register() {
            var self = this;
            //Mode INSERT
            if (self.isNew()) {
                //Format Code to Formular "000" 
                let currentCode: string = self.currentItem().externalBudgetCode();
                self.currentItem().externalBudgetCode(self.padZero(currentCode));
                //insert process
                service.insertExternalBudget(self.currentItem()).done(function() {
                    nts.uk.ui.dialog.alert(nts.uk.resource.getMessage('Msg_15'))；
                        // Add new item
                        self.listBudget.push({
                            externalBudgetCode: self.currentItem().externalBudgetCode(),
                            externalBudgetName: self.currentItem().externalBudgetName(),
                            budgetAtr: self.currentItem().budgetAtr(),
                            unitAtr: self.currentItem().unitAtr()
                        });
                        //Re-sort
                        self.listBudget(_.orderBy(self.listBudget(), ['externalBudgetCode'], ['asc']));
                        self.selectedBudgetCode(self.currentItem().externalBudgetCode());
                        self.isNew(false);
                    }).fail(function(res) {
                        $('#inpCode').ntsError('set',res);
                    }); 
            }
            //Mode UPDATE
            else {
                service.updateExternalBudget(self.currentItem()).done(function() {
                    nts.uk.ui.dialog.alert(nts.uk.resource.getMessage('Msg_15'))；
                    //update list source
                    let updateItem = _.find(self.listBudget(),['externalBudgetCode',self.selectedBudgetCode()]);
                    if(updateItem !== undefined){
                        self.listBudget.remove(updateItem);
                        updateItem.externalBudgetName = self.currentItem().externalBudgetName();
                        updateItem.budgetAtr = self.currentItem().budgetAtr();
                        updateItem.unitAtr = self.currentItem().unitAtr();
                        self.listBudget.push(updateItem);
                    }
                    self.listBudget(_.orderBy(self.listBudget(), ['externalBudgetCode'], ['asc']));
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                    self.start();
                });
            }
        }
        
        /** Add new Button click */
        addNew() {
            var self = this;
            self.isNew(true);
            self.newMode();
        }
        
        /** Close dialog */
        close(){
            nts.uk.ui.windows.close();
        }
        
        /** Delete selected Item */
        del() {
            var self = this;
            nts.uk.ui.dialog.confirm(nts.uk.resource.getMessage('Msg_18')).ifYes(function(){
                //削除後処理 
                service.deleteExternalBudget(self.currentItem()).done(function() {
                   nts.uk.ui.dialog.alert(nts.uk.resource.getMessage('Msg_16')).then(function(){
                        var deletedIndex = _.findIndex(self.listBudget(), ['externalBudgetCode',self.selectedBudgetCode()]);
                        var deletedItem = _.find(self.listBudget(), ['externalBudgetCode',self.selectedBudgetCode()]);
                        self.listBudget.remove(deletedItem);
                        if(self.listBudget().length === 0) {
                            self.newMode();
                        }
                        else {
                            self.findItemByIndex(_.min([deletedIndex, self.listBudget().length - 1]));
                        }
                   })；
                   
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                    self.start();
                });
            });
            
        }
        
        /** Create mode */
        private newMode(): void {
            var self = this;
            if (nts.uk.ui._viewModel !== undefined)
                $('.nts-checkvalue').ntsError('clear');
            //current Code, 何にも、項目選択している。
            self.selectedBudgetCode(null);
            self.currentItem(new BudgetItem("", "", 0, 0));
            //Enable Code Input
            $('#inpCode').focus();
        }
        
        /** Find selected item by code */
        private findBudgetByCode(externalBudgetCode: string): void {
            var self = this;
            var currentItem = _.find(self.listBudget(), function(item) { return item.externalBudgetCode == externalBudgetCode; });
            if (currentItem !== undefined) {
                self.isNew(false);
                self.currentItem(new BudgetItem(currentItem.externalBudgetCode, currentItem.externalBudgetName, currentItem.budgetAtr, currentItem.unitAtr));
            }
            else {
                self.isNew(true);
                self.currentItem(new BudgetItem("", "", 0, 0));
            }
        }
        
        /** Select item by index */
        private findItemByIndex(index: number): void {
            var self = this;
            var currentItem = _.nth(self.listBudget(), index);
            if (currentItem !== undefined)
                self.selectedBudgetCode(currentItem.externalBudgetCode);
            else
                self.selectedBudgetCode(null);
        }
        
        /** Add zero number for Code */
        private padZero(code:string){
            let format :string ="000";
            let length : number = code.length;
            return format.substr(0,3-length) + code;
        }
        
    }
    
    interface BudgetItemDto {
        externalBudgetCode: string;
        externalBudgetName: string;
        budgetAtr: number;
        unitAtr: number;
    }

    /** Budget Observable Class */
    class BudgetItem {
        externalBudgetCode: KnockoutObservable<string>;
        externalBudgetName: KnockoutObservable<string>;
        budgetAtr: KnockoutObservable<number>;
        unitAtr: KnockoutObservable<number>;
        constructor(externalBudgetCode: string, externalBudgetName: string, budgetAtr: number, unitAtr: number) {
            var self = this;
            self.externalBudgetCode = ko.observable(externalBudgetCode);
            self.externalBudgetName = ko.observable(externalBudgetName);
            self.budgetAtr = ko.observable(budgetAtr);
            self.unitAtr = ko.observable(unitAtr);
        }
    }
    
    /**item Combo Box */
    class ItemModelCbb {
        codeCbb: string;
        nameCbb: string;
        constructor(codeCbb: string, nameCbb: string) {
            var self = this;
            self.codeCbb = codeCbb;
            self.nameCbb = nameCbb;
        }
    }

}