module qmm012.h.viewmodel {
    export class ScreenModel {
        //textediter

        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        isEditable: KnockoutObservable<boolean>;
        //gridlist
        gridListItems: KnockoutObservableArray<GridItemModel>;
        columns: KnockoutObservableArray<any>;
        gridListCurrentCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;
        //Switch
        Roundingrules_ValidityPeriod: KnockoutObservableArray<any>;
        Roundingrules_CycleSetting: KnockoutObservableArray<any>;
        enable: KnockoutObservable<boolean> = ko.observable(true);
        CurrentItemMaster: KnockoutObservable<qmm012.b.service.model.ItemMaster> = ko.observable(null);
        CurrentCategoryAtrName: KnockoutObservable<string> = ko.observable('');
        CurrentItemPeriod: KnockoutObservable<service.model.ItemPeriod> = ko.observable(null);
        CurrentCodeAndNameText: KnockoutObservable<string> = ko.observable('');
        CurrentPeriodAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentStrY: KnockoutObservable<number> = ko.observable(1900);
        CurrentEndY: KnockoutObservable<number> = ko.observable(1900);
        CurrentCycleAtr: KnockoutObservable<number> = ko.observable(0);
        H_Sel_January: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_February: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_InMarch: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_April: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_May: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_June: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_July: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_August: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_September: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_October: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_November: KnockoutObservable<boolean> = ko.observable(false);
        H_Sel_December: KnockoutObservable<boolean> = ko.observable(false);
        CycleSetting: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            var self = this;
            //set Switch Data
            self.Roundingrules_ValidityPeriod = ko.observableArray([
                { code: 1, name: '設定する' },
                { code: 0, name: '設定しない' }
            ]);

            self.Roundingrules_CycleSetting = ko.observableArray([
                { code: 1, name: 'する' },
                { code: 0, name: 'しない' }
            ]);

            self.CurrentItemPeriod.subscribe(function(ItemPeriod: service.model.ItemPeriod) {
                self.CurrentPeriodAtr(ItemPeriod ? ItemPeriod.periodAtr : 0);
                self.CurrentStrY(ItemPeriod ? ItemPeriod.strY : 1900);
                self.CurrentEndY(ItemPeriod ? ItemPeriod.endY : 1900);
                self.CurrentCycleAtr(ItemPeriod ? ItemPeriod.cycleAtr : 0);
                self.H_Sel_January(ItemPeriod ? ItemPeriod.cycle01Atr == 1 ? true : false : false);
                self.H_Sel_February(ItemPeriod ? ItemPeriod.cycle02Atr == 1 ? true : false : false);
                self.H_Sel_InMarch(ItemPeriod ? ItemPeriod.cycle03Atr == 1 ? true : false : false);
                self.H_Sel_April(ItemPeriod ? ItemPeriod.cycle04Atr == 1 ? true : false : false);
                self.H_Sel_May(ItemPeriod ? ItemPeriod.cycle05Atr == 1 ? true : false : false);
                self.H_Sel_June(ItemPeriod ? ItemPeriod.cycle06Atr == 1 ? true : false : false);
                self.H_Sel_July(ItemPeriod ? ItemPeriod.cycle07Atr == 1 ? true : false : false);
                self.H_Sel_August(ItemPeriod ? ItemPeriod.cycle08Atr == 1 ? true : false : false);
                self.H_Sel_September(ItemPeriod ? ItemPeriod.cycle09Atr == 1 ? true : false : false);
                self.H_Sel_October(ItemPeriod ? ItemPeriod.cycle10Atr == 1 ? true : false : false);
                self.H_Sel_November(ItemPeriod ? ItemPeriod.cycle11Atr == 1 ? true : false : false);
                self.H_Sel_December(ItemPeriod ? ItemPeriod.cycle12Atr == 1 ? true : false : false);

            });
            self.CurrentCycleAtr.subscribe(function(newValue) {
                if (newValue == 1)
                    self.CycleSetting(true);
                else
                    self.CycleSetting(false);
            });
            self.LoadItemPeriod();
        }
        LoadItemPeriod() {
            //this dialog only load data in session from parrent call it
            let self = this;
            let itemMaster: qmm012.b.service.model.ItemMaster = nts.uk.ui.windows.getShared('itemMaster');
            if (itemMaster != undefined) {
                self.CurrentItemMaster(itemMaster);
                self.CurrentCategoryAtrName(itemMaster.categoryAtrName);
                self.CurrentCodeAndNameText(itemMaster.itemCode + "  " + itemMaster.itemName);
            }
            if (nts.uk.ui.windows.getShared('itemPeriod'))
                self.CurrentItemPeriod(nts.uk.ui.windows.getShared('itemPeriod'));
        }
        getCurrentItemPeriod() {
            //return  ItemPeriod customer has input to form
            let self = this;
            return new service.model.ItemPeriod(
                self.CurrentItemMaster().itemCode,
                self.CurrentPeriodAtr(),
                self.CurrentStrY(),
                self.CurrentEndY(),
                self.CurrentCycleAtr(),
                self.H_Sel_January() == true ? 1 : 0,
                self.H_Sel_February() == true ? 1 : 0,
                self.H_Sel_InMarch() == true ? 1 : 0,
                self.H_Sel_April() == true ? 1 : 0,
                self.H_Sel_May() == true ? 1 : 0,
                self.H_Sel_June() == true ? 1 : 0,
                self.H_Sel_July() == true ? 1 : 0,
                self.H_Sel_August() == true ? 1 : 0,
                self.H_Sel_September() == true ? 1 : 0,
                self.H_Sel_October() == true ? 1 : 0,
                self.H_Sel_November() == true ? 1 : 0,
                self.H_Sel_December() == true ? 1 : 0
            );
        }
        validateForm() {
            let self = this;
            let returnResult = true;
            if (self.CurrentStrY() > self.CurrentEndY()) {
                nts.uk.ui.dialog.alert("範囲の指定が正しくありません。");
                return false;
            }
            if (self.CurrentCycleAtr() == 1 && !self.H_Sel_January() && !self.H_Sel_December()) {
                nts.uk.ui.dialog.alert("1月か12月が選択されていません。");
                return false;
            }
            return returnResult;
        }
        clearError() {
            $('#H_Inp_StartYear').ntsError('clear');
            $('#H_Inp_EndYear').ntsError('clear');
        }
        SubmitDialog() {
            let self = this;
            let itemPeriodOld = self.CurrentItemPeriod();
            let itemPeriod = self.getCurrentItemPeriod();
            if (self.validateForm()) {
                if (itemPeriodOld) {
                    //it mean this item has been created before
                    service.updateItemPeriod(itemPeriod, self.CurrentItemMaster()).done(function(res: any) {
                        nts.uk.ui.windows.setShared('itemPeriod', itemPeriod);
                        nts.uk.ui.windows.close();
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.alert(res.value);
                    });
                } else {
                    service.addItemPeriod(itemPeriod, self.CurrentItemMaster()).done(function(res: any) {
                        nts.uk.ui.windows.setShared('itemPeriod', itemPeriod);
                        nts.uk.ui.windows.close();
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.alert(res.value);
                    });
                }
            }
        }

        CloseDialog() {
            nts.uk.ui.windows.close();
        }
    }
    class GridItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }

    }
    class ComboboxItemModel {
        code: string;
        name: string;
        label: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }


}