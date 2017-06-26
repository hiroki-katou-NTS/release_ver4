var qmm012;
(function (qmm012) {
    var d;
    (function (d) {
        var viewmodel;
        (function (viewmodel) {
            class ScreenModel {
                constructor() {
                    //Checkbox
                    //D_003
                    this.checked_D_003 = ko.observable(true);
                    //D_003
                    this.checked_D_004 = ko.observable(false);
                    //D_003
                    this.checked_D_005 = ko.observable(false);
                    //D_003
                    this.checked_D_006 = ko.observable(false);
                    //D_003
                    this.checked_D_007 = ko.observable(false);
                    this.CurrentItemMaster = ko.observable(null);
                    this.CurrentItemDeduct = ko.observable(null);
                    this.CurrentAlRangeHigh = ko.observable(0);
                    this.CurrentAlRangeLow = ko.observable(0);
                    this.CurrentDeductAtr = ko.observable(0);
                    this.CurrentErrRangeHigh = ko.observable(0);
                    this.CurrentErrRangeLow = ko.observable(0);
                    this.CurrentMemo = ko.observable("");
                    this.CurrentItemDisplayAtr = ko.observable(1);
                    this.CurrentZeroDisplaySet = ko.observable(1);
                    this.currentItemPeriod = ko.observable(null);
                    this.D_LBL_011_Text = ko.observable('設定なし');
                    this.currentItemBDs = ko.observableArray([]);
                    this.D_LBL_012_Text = ko.observable("設定なし");
                    this.D_BTN_001_enable = ko.observable(true);
                    this.D_BTN_002_enable = ko.observable(true);
                    var self = this;
                    self.isEditable = ko.observable(true);
                    self.isEnable = ko.observable(true);
                    self.enable = ko.observable(true);
                    self.ComboBoxItemList_D_001 = ko.observableArray([
                        new ComboboxItemModel(0, '任意控除項目'),
                        new ComboboxItemModel(1, '社会保険項目'),
                        new ComboboxItemModel(2, '所得税項目'),
                        new ComboboxItemModel(3, '住民税項目')
                    ]);
                    //end combobox data
                    //D_002
                    self.roundingRules_D_002 = ko.observableArray([
                        { code: 1, name: 'ゼロを表示する' },
                        { code: 0, name: 'ゼロを表示しない' }
                    ]);
                    //currencyeditor
                    //001
                    self.currencyeditor_D_001 = {
                        value: self.CurrentErrRangeHigh,
                        constraint: 'ErrRangeHigh',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        })),
                        required: ko.observable(false),
                        enable: ko.observable(true),
                        readonly: ko.observable(false)
                    };
                    //002
                    self.currencyeditor_D_002 = {
                        value: self.CurrentAlRangeHigh,
                        constraint: 'AlRangeHigh',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        })),
                        required: ko.observable(false),
                        enable: ko.observable(true),
                        readonly: ko.observable(false)
                    };
                    //003
                    self.currencyeditor_D_003 = {
                        value: self.CurrentErrRangeLow,
                        constraint: 'ErrRangeLow',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        })),
                        required: ko.observable(false),
                        enable: ko.observable(true),
                        readonly: ko.observable(false)
                    };
                    //004
                    self.currencyeditor_D_004 = {
                        value: self.CurrentAlRangeLow,
                        constraint: 'AlRangeLow',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        })),
                        required: ko.observable(false),
                        enable: ko.observable(true),
                        readonly: ko.observable(false)
                    };
                    self.CurrentItemMaster.subscribe(function (ItemMaster) {
                        if (ItemMaster) {
                            d.service.findItemDeduct(ItemMaster.itemCode).done(function (ItemDeduct) {
                                self.CurrentItemDeduct(ItemDeduct);
                            }).fail(function (res) {
                                // Alert message
                                alert(res);
                            });
                        }
                        else {
                            self.CurrentItemDeduct(null);
                        }
                        self.loadItemPeriod();
                        self.loadItemBDs();
                        self.checked_D_003(ItemMaster ? ItemMaster.itemDisplayAtr == 0 ? true : false : false);
                        self.CurrentZeroDisplaySet(ItemMaster ? ItemMaster.zeroDisplaySet : 1);
                    });
                    self.CurrentItemDeduct.subscribe(function (ItemDeduct) {
                        self.CurrentAlRangeHigh(ItemDeduct ? ItemDeduct.alRangeHigh : 0);
                        self.CurrentAlRangeLow(ItemDeduct ? ItemDeduct.alRangeLow : 0);
                        self.CurrentDeductAtr(ItemDeduct ? ItemDeduct.deductAtr : 0);
                        self.CurrentErrRangeHigh(ItemDeduct ? ItemDeduct.errRangeHigh : 0);
                        self.CurrentErrRangeLow(ItemDeduct ? ItemDeduct.errRangeLow : 0);
                        self.CurrentMemo(ItemDeduct ? ItemDeduct.memo : "");
                        self.checked_D_004(ItemDeduct ? ItemDeduct.errRangeHighAtr == 0 ? false : true : false);
                        self.checked_D_005(ItemDeduct ? ItemDeduct.alRangeHighAtr == 0 ? false : true : false);
                        self.checked_D_006(ItemDeduct ? ItemDeduct.errRangeLowAtr == 0 ? false : true : false);
                        self.checked_D_007(ItemDeduct ? ItemDeduct.alRangeLowAtr == 0 ? false : true : false);
                    });
                    self.checked_D_003.subscribe(function (NewValue) {
                        self.CurrentItemDisplayAtr(NewValue ? 0 : 1);
                    });
                    self.currentItemPeriod.subscribe(function (newValue) {
                        self.D_LBL_011_Text(newValue ? newValue.periodAtr == 1 ? '設定あり' : '設定なし' : '設定なし');
                    });
                    self.currentItemBDs.subscribe(function (newValue) {
                        self.D_LBL_012_Text(newValue.length ? '設定あり' : '設定なし');
                    });
                }
                loadItemPeriod() {
                    let self = this;
                    //Load Screen H  Data
                    if (self.CurrentItemMaster()) {
                        qmm012.h.service.findItemPeriod(self.CurrentItemMaster()).done(function (ItemPeriod) {
                            self.currentItemPeriod(ItemPeriod);
                        }).fail(function (res) {
                            // Alert message
                            alert(res);
                        });
                    }
                    else
                        self.currentItemPeriod(undefined);
                }
                loadItemBDs() {
                    let self = this;
                    if (self.CurrentItemMaster()) {
                        qmm012.i.service.findAllItemBD(self.CurrentItemMaster()).done(function (ItemBDs) {
                            self.currentItemBDs(ItemBDs);
                        }).fail(function (res) {
                            // Alert message
                            alert(res);
                        });
                    }
                    else
                        self.currentItemBDs([]);
                }
                openHDialog() {
                    let self = this;
                    nts.uk.ui.windows.setShared('itemMaster', self.CurrentItemMaster());
                    nts.uk.ui.windows.setShared('itemPeriod', self.currentItemPeriod());
                    nts.uk.ui.windows.sub.modal('../h/index.xhtml', { height: 570, width: 735, dialogClass: "no-close" }).onClosed(function () {
                        self.currentItemPeriod(nts.uk.ui.windows.getShared('itemPeriod'));
                    });
                }
                openIDialog() {
                    let self = this;
                    nts.uk.ui.windows.setShared('itemMaster', self.CurrentItemMaster());
                    nts.uk.ui.windows.setShared('itemBDs', self.currentItemBDs());
                    nts.uk.ui.windows.sub.modal('../i/index.xhtml', { height: 620, width: 1060, dialogClass: "no-close" }).onClosed(function () {
                        self.currentItemBDs(nts.uk.ui.windows.getShared('itemBDs'));
                    });
                }
                GetCurrentItemDeduct() {
                    let self = this;
                    let ItemDeduct = new d.service.model.ItemDeduct(self.CurrentDeductAtr(), self.checked_D_006() ? 1 : 0, self.CurrentErrRangeLow(), self.checked_D_004() ? 1 : 0, self.CurrentErrRangeHigh(), self.checked_D_007() ? 1 : 0, self.CurrentAlRangeLow(), self.checked_D_005() ? 1 : 0, self.CurrentAlRangeHigh(), self.CurrentMemo());
                    return ItemDeduct;
                }
            }
            viewmodel.ScreenModel = ScreenModel;
            class ComboboxItemModel {
                constructor(code, name) {
                    this.code = code;
                    this.name = name;
                }
            }
        })(viewmodel = d.viewmodel || (d.viewmodel = {}));
    })(d = qmm012.d || (qmm012.d = {}));
})(qmm012 || (qmm012 = {}));
