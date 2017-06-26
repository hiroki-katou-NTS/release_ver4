module qmm012.d.viewmodel {
    export class ScreenModel {
        isEditable: KnockoutObservable<boolean>;
        isEnable: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;
        //001
        D_Sel_DeductionItem: KnockoutObservableArray<ComboboxItemModel>;
        //Checkbox
        //D_003
        Checked_NoDisplayNames: KnockoutObservable<boolean> = ko.observable(true);
        //D_003
        Checked_ErrorUpper: KnockoutObservable<boolean> = ko.observable(false);
        //D_003
        Checked_AlarmHigh: KnockoutObservable<boolean> = ko.observable(false);
        //D_003
        Checked_ErrorLower: KnockoutObservable<boolean> = ko.observable(false);
        //D_003
        Checked_AlarmLower: KnockoutObservable<boolean> = ko.observable(false);
        //Switch
        currencyeditor_ErrorUpper: any;
        currencyeditor_AlarmUpper: any;
        currencyeditor_ErrorLower: any;
        currencyeditor_AlarmLower: any;
        //D_002
        roundingRules_ZeroDisplayIndicator: KnockoutObservableArray<any>;

        CurrentItemMaster: KnockoutObservable<qmm012.b.service.model.ItemMaster> = ko.observable(null);
        CurrentItemDeduct: KnockoutObservable<service.model.ItemDeduct> = ko.observable(null);
        CurrentAlRangeHigh: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentDeductAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeHigh: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentMemo: KnockoutObservable<String> = ko.observable("");
        CurrentItemDisplayAtr: KnockoutObservable<number> = ko.observable(1);
        CurrentZeroDisplaySet: KnockoutObservable<number> = ko.observable(1);
        currentItemPeriod: KnockoutObservable<qmm012.h.service.model.ItemPeriod> = ko.observable(null);
        D_Lbl_SettingClassification_Text: KnockoutObservable<string> = ko.observable('設定なし');
        currentItemBDs: KnockoutObservableArray<qmm012.i.service.model.ItemBD> = ko.observableArray([]);
        D_Lbl_BreakdownItem_Text: KnockoutObservable<string> = ko.observable("設定なし");
        D_Btn_PeriodSetting_enable: KnockoutObservable<boolean> = ko.observable(true);
        D_Btn_BreakdownSetting_enable: KnockoutObservable<boolean> = ko.observable(true);
        noDisplayNames_Enable: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            var self = this;
            self.isEditable = ko.observable(true);
            self.isEnable = ko.observable(true);
            self.enable = ko.observable(true);
            self.D_Sel_DeductionItem = ko.observableArray([
                new ComboboxItemModel(0, '任意控除項目'),
                new ComboboxItemModel(1, '社会保険項目'),
                new ComboboxItemModel(2, '所得税項目'),
                new ComboboxItemModel(3, '住民税項目')
            ]);
            //end combobox data
            //D_002
            self.roundingRules_ZeroDisplayIndicator = ko.observableArray([
                { code: 1, name: 'ゼロを表示する' },
                { code: 0, name: 'ゼロを表示しない' }
            ]);
            //currencyeditor
            //001
            self.currencyeditor_ErrorUpper = {
                value: self.CurrentErrRangeHigh,
                constraint: 'ErrRangeHigh',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_ErrorUpper
            };
            //002
            self.currencyeditor_AlarmUpper = {
                value: self.CurrentAlRangeHigh,
                constraint: 'AlRangeHigh',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_AlarmHigh
            };
            //003
            self.currencyeditor_ErrorLower = {
                value: self.CurrentErrRangeLow,
                constraint: 'ErrRangeLow',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_ErrorLower

            };
            //004
            self.currencyeditor_AlarmLower = {
                value: self.CurrentAlRangeLow,
                constraint: 'AlRangeLow',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_AlarmLower
            };


            self.CurrentItemDeduct.subscribe(function(ItemDeduct: service.model.ItemDeduct) {
                self.CurrentAlRangeHigh(ItemDeduct ? ItemDeduct.alRangeHigh : 0);
                self.CurrentAlRangeLow(ItemDeduct ? ItemDeduct.alRangeLow : 0);
                self.CurrentDeductAtr(ItemDeduct ? ItemDeduct.deductAtr : 0);
                self.CurrentErrRangeHigh(ItemDeduct ? ItemDeduct.errRangeHigh : 0);
                self.CurrentErrRangeLow(ItemDeduct ? ItemDeduct.errRangeLow : 0);
                self.CurrentMemo(ItemDeduct ? ItemDeduct.memo : "");
                self.Checked_ErrorUpper(ItemDeduct ? ItemDeduct.errRangeHighAtr == 0 ? false : true : false);
                self.Checked_AlarmHigh(ItemDeduct ? ItemDeduct.alRangeHighAtr == 0 ? false : true : false);
                self.Checked_ErrorLower(ItemDeduct ? ItemDeduct.errRangeLowAtr == 0 ? false : true : false);
                self.Checked_AlarmLower(ItemDeduct ? ItemDeduct.alRangeLowAtr == 0 ? false : true : false);
            });

            self.Checked_NoDisplayNames.subscribe(function(NewValue) {
                self.CurrentItemDisplayAtr(NewValue ? 0 : 1);
            });
            self.currentItemPeriod.subscribe(function(newValue) {
                self.D_Lbl_SettingClassification_Text(newValue ? newValue.periodAtr == 1 ? '設定あり' : '設定なし' : '設定なし');
            });
            self.currentItemBDs.subscribe(function(newValue) {
                self.D_Lbl_BreakdownItem_Text(newValue.length ? '設定あり' : '設定なし');
            });
            self.CurrentZeroDisplaySet.subscribe(function(newValue) {
                if (newValue == 0) {
                    self.noDisplayNames_Enable(true);
                } else {
                    self.noDisplayNames_Enable(false);
                }
            });
        }

        loadData(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred<any>();
            self.CurrentItemMaster(itemMaster);
            if (itemMaster.itemCode) {
                self.loadItemDeduct(itemMaster).done(function() {
                    self.loadItemPeriod(itemMaster).done(function() {
                        self.loadItemBDs(itemMaster).done(function() {
                            dfd.resolve();
                        });
                    });
                });
            } else {
                self.clearContent();
                dfd.resolve();
            }
            self.Checked_NoDisplayNames(itemMaster ? itemMaster.itemDisplayAtr == 0 ? true : false : false);
            self.CurrentZeroDisplaySet(itemMaster ? itemMaster.zeroDisplaySet : 1);
            return dfd.promise();
        }
        clearContent() {
            let self = this;
            self.CurrentAlRangeHigh(0);
            self.CurrentAlRangeLow(0);
            self.CurrentDeductAtr(0);
            self.CurrentZeroDisplaySet(1);
            self.CurrentErrRangeHigh(0);
            self.CurrentErrRangeLow(0);
            self.CurrentMemo("");
            self.Checked_ErrorUpper(false);
            self.Checked_AlarmHigh(false);
            self.Checked_ErrorLower(false);
            self.Checked_AlarmLower(false);
            self.D_Lbl_SettingClassification_Text('設定なし');
            self.D_Lbl_BreakdownItem_Text('設定なし');
        }
        loadItemDeduct(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred<any>();
            service.findItemDeduct(itemMaster.itemCode).done(function(ItemDeduct: service.model.ItemDeduct) {
                self.CurrentItemDeduct(ItemDeduct);
                dfd.resolve(ItemDeduct);
            }).fail(function(res) {
                // Alert message
                nts.uk.ui.dialog.alert(res);
            });
            return dfd.promise();
        }
        loadItemPeriod(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred<any>();
            qmm012.h.service.findItemPeriod(itemMaster).done(function(ItemPeriod: qmm012.h.service.model.ItemPeriod) {
                self.currentItemPeriod(ItemPeriod);
                dfd.resolve(ItemPeriod);
            }).fail(function(res) {
                // Alert message
                nts.uk.ui.dialog.alert(res);
            });
            return dfd.promise();
        }
        loadItemBDs(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred<any>();
            qmm012.i.service.findAllItemBD(itemMaster).done(function(ItemBDs: Array<qmm012.i.service.model.ItemBD>) {
                self.currentItemBDs(ItemBDs);
                dfd.resolve(ItemBDs);
            }).fail(function(res) {
                // Alert message
                nts.uk.ui.dialog.alert(res);
            });
            return dfd.promise();
        }
        openHDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('itemMaster', self.CurrentItemMaster());
            nts.uk.ui.windows.setShared('itemPeriod', self.currentItemPeriod());
            nts.uk.ui.windows.sub.modal('../h/index.xhtml', { height: 570, width: 735, dialogClass: "no-close", title: "項目名の登録" }).onClosed(function(): any {
                self.currentItemPeriod(nts.uk.ui.windows.getShared('itemPeriod'));
            });
        }

        openIDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('itemMaster', self.CurrentItemMaster());
            nts.uk.ui.windows.setShared('itemBDs', self.currentItemBDs());
            nts.uk.ui.windows.sub.modal('../i/index.xhtml', { height: 620, width: 1060, dialogClass: "no-close", title: "項目名の登録" }).onClosed(function(): any {
                self.currentItemBDs(nts.uk.ui.windows.getShared('itemBDs'));
            });
        }
        GetCurrentItemDeduct() {
            let self = this;
            let ItemDeduct = new service.model.ItemDeduct(
                self.CurrentDeductAtr(),
                self.Checked_ErrorLower() ? 1 : 0,
                self.CurrentErrRangeLow(),
                self.Checked_ErrorUpper() ? 1 : 0,
                self.CurrentErrRangeHigh(),
                self.Checked_AlarmLower() ? 1 : 0,
                self.CurrentAlRangeLow(),
                self.Checked_AlarmHigh() ? 1 : 0,
                self.CurrentAlRangeHigh(),
                self.CurrentMemo());
            return ItemDeduct;
        }

    }

    class ComboboxItemModel {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }
}