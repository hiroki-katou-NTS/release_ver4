var qmm012;
(function (qmm012) {
    var e;
    (function (e) {
        var viewmodel;
        (function (viewmodel) {
            class ScreenModel {
                constructor() {
                    //E_004
                    this.checked_E_004 = ko.observable(false);
                    //E_005
                    this.checked_E_005 = ko.observable(false);
                    //E_006
                    this.checked_E_006 = ko.observable(false);
                    //E_007
                    this.checked_E_007 = ko.observable(false);
                    //E_008
                    this.checked_E_008 = ko.observable(false);
                    this.CurrentItemMaster = ko.observable(null);
                    this.CurrentItemAttend = ko.observable(null);
                    this.CurrentAvePayAtr = ko.observable(0);
                    this.CurrentItemAtr = ko.observable(0);
                    this.CurrentErrRangeLow = ko.observable(0);
                    this.CurrentErrRangeHigh = ko.observable(0);
                    this.CurrentAlRangeLow = ko.observable(0);
                    this.CurrentAlRangeHigh = ko.observable(0);
                    this.CurrentWorkDaysScopeAtr = ko.observable(1);
                    this.CurrentMemo = ko.observable("");
                    this.CurrentZeroDisplaySet = ko.observable(1);
                    this.CurrentItemDisplayAtr = ko.observable(1);
                    let self = this;
                    //E_001 To 003
                    //E_001To003
                    self.roundingRules_E_001 = ko.observableArray([
                        { code: 0, name: '時間' },
                        { code: 1, name: '回数' }
                    ]);
                    self.roundingRules_E_002 = ko.observableArray([
                        { code: 1, name: '対象' },
                        { code: 0, name: '対象外' }
                    ]);
                    self.roundingRules_E_003 = ko.observableArray([
                        { code: 1, name: 'ゼロを表示する' },
                        { code: 0, name: 'ゼロを表示しない' }
                    ]);
                    self.enable = ko.observable(false);
                    //E_001
                    self.currencyeditor_E_001 = {
                        value: self.CurrentErrRangeHigh,
                        constraint: 'ErrRangeHigh',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    //E_002
                    self.currencyeditor_E_002 = {
                        value: self.CurrentAlRangeHigh,
                        constraint: 'AlRangeHigh',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    //E_003
                    self.currencyeditor_E_003 = {
                        value: self.CurrentErrRangeLow,
                        constraint: 'ErrRangeLow',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    //E_004
                    self.currencyeditor_E_004 = {
                        value: self.CurrentAlRangeLow,
                        constraint: 'AlRangeLow',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    self.CurrentItemMaster.subscribe(function (ItemMaster) {
                        if (ItemMaster) {
                            e.service.findItemAttend(ItemMaster.itemCode).done(function (ItemAttend) {
                                self.CurrentItemAttend(ItemAttend);
                            }).fail(function (res) {
                                // Alert message
                                alert(res);
                            });
                        }
                        else {
                            self.CurrentItemAttend(null);
                        }
                        self.CurrentZeroDisplaySet(ItemMaster ? ItemMaster.zeroDisplaySet : 1);
                        self.checked_E_004(ItemMaster ? ItemMaster.itemDisplayAtr == 0 ? true : false : false);
                    });
                    self.CurrentItemAttend.subscribe(function (ItemAttend) {
                        self.CurrentAvePayAtr(ItemAttend ? ItemAttend.avePayAtr : 0);
                        self.CurrentItemAtr(ItemAttend ? ItemAttend.itemAtr : 0);
                        self.CurrentErrRangeLow(ItemAttend ? ItemAttend.errRangeLow : 0);
                        self.CurrentErrRangeHigh(ItemAttend ? ItemAttend.errRangeHigh : 0);
                        self.CurrentAlRangeLow(ItemAttend ? ItemAttend.alRangeLow : 0);
                        self.CurrentAlRangeHigh(ItemAttend ? ItemAttend.alRangeHigh : 0);
                        self.CurrentWorkDaysScopeAtr(ItemAttend ? ItemAttend.workDaysScopeAtr : 1);
                        self.CurrentMemo(ItemAttend ? ItemAttend.memo : "");
                        self.checked_E_005(ItemAttend ? ItemAttend.errRangeHighAtr == 0 ? false : true : false);
                        self.checked_E_006(ItemAttend ? ItemAttend.errRangeLowAtr == 0 ? false : true : false);
                        self.checked_E_007(ItemAttend ? ItemAttend.alRangeHighAtr == 0 ? false : true : false);
                        self.checked_E_008(ItemAttend ? ItemAttend.alRangeLowAtr == 0 ? false : true : false);
                    });
                    self.checked_E_004.subscribe(function (NewValue) {
                        self.CurrentItemDisplayAtr(NewValue ? 0 : 1);
                    });
                }
                getCurrentItemAttend() {
                    //return Item Attend customer input on form
                    let self = this;
                    let itemAttend = new e.service.model.ItemAttend(self.CurrentAvePayAtr(), self.CurrentItemAtr(), self.checked_E_006() ? 1 : 0, self.CurrentErrRangeLow(), self.checked_E_005() ? 1 : 0, self.CurrentErrRangeHigh(), self.checked_E_008() ? 1 : 0, self.CurrentAlRangeLow(), self.checked_E_007() ? 1 : 0, self.CurrentAlRangeHigh(), self.CurrentWorkDaysScopeAtr(), self.CurrentMemo());
                    return itemAttend;
                }
            }
            viewmodel.ScreenModel = ScreenModel;
        })(viewmodel = e.viewmodel || (e.viewmodel = {}));
    })(e = qmm012.e || (qmm012.e = {}));
})(qmm012 || (qmm012 = {}));
