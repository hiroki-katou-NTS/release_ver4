var qmm012;
(function (qmm012) {
    var i;
    (function (i) {
        var viewmodel;
        (function (viewmodel) {
            class ScreenModel {
                constructor() {
                    //textediter
                    //Checkbox
                    this.checked_002 = ko.observable(false);
                    this.checked_003 = ko.observable(false);
                    this.checked_004 = ko.observable(false);
                    this.checked_005 = ko.observable(false);
                    this.checked_006 = ko.observable(false);
                    this.gridListCurrentCode = ko.observable('');
                    this.selectedRuleCode_001 = ko.observable(1);
                    this.CurrentItemMaster = ko.observable(null);
                    this.ItemBDList = ko.observableArray([]);
                    this.CurrentCategoryAtrName = ko.observable('');
                    this.CurrentItemBD = ko.observable(null);
                    this.CurrentItemBreakdownCode = ko.observable('');
                    this.CurrentItemBreakdownName = ko.observable('');
                    this.CurrentItemBreakdownAbName = ko.observable('');
                    this.CurrentUniteCode = ko.observable('');
                    this.CurrentZeroDispSet = ko.observable(1);
                    this.CurrentItemDispAtr = ko.observable(0);
                    this.CurrentErrRangeLow = ko.observable(0);
                    this.CurrentErrRangeHigh = ko.observable(0);
                    this.CurrentAlRangeLow = ko.observable(0);
                    this.CurrentAlRangeHigh = ko.observable(0);
                    this.enable_I_INP_002 = ko.observable(false);
                    this.I_BTN_003_enable = ko.observable(true);
                    this.currentItemCode = ko.observable('');
                    var self = this;
                    //start Switch Data
                    self.enable = ko.observable(true);
                    self.roundingRules_001 = ko.observableArray([
                        { code: 1, name: 'ゼロを表示する' },
                        { code: 0, name: 'ゼロを表示しない' }
                    ]);
                    //endSwitch Data
                    //currencyeditor
                    //005
                    self.currencyeditor_I_INP_005 = {
                        value: self.CurrentErrRangeHigh,
                        constraint: 'ErrRangeHigh',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    //006
                    self.currencyeditor_I_INP_006 = {
                        value: self.CurrentAlRangeHigh,
                        constraint: 'AlRangeHigh',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    //007
                    self.currencyeditor_I_INP_007 = {
                        value: self.CurrentErrRangeLow,
                        constraint: 'ErrRangeLow',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    //008
                    self.currencyeditor_I_INP_008 = {
                        value: self.CurrentAlRangeLow,
                        constraint: 'AlRangeLow',
                        option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                            grouplength: 3,
                            currencyformat: "JPY",
                            currencyposition: 'right'
                        }))
                    };
                    //end currencyeditor
                    // start search box 
                    self.filteredData = ko.observableArray(nts.uk.util.flatArray(self.ItemBDList(), "childs"));
                    // end search box 
                    self.columns = ko.observableArray([
                        { headerText: 'ード', prop: 'itemBreakdownCode', width: 100 },
                        { headerText: '名', prop: 'itemBreakdownName', width: 150 }
                    ]);
                    self.gridListCurrentCode.subscribe(function (newValue) {
                        var item = _.find(self.ItemBDList(), function (ItemBD) {
                            return ItemBD.itemBreakdownCode == newValue;
                        });
                        self.CurrentItemBD(item);
                    });
                    self.CurrentItemBD.subscribe(function (ItemBD) {
                        self.CurrentItemBreakdownCode(ItemBD ? ItemBD.itemBreakdownCode : '');
                        self.CurrentItemBreakdownName(ItemBD ? ItemBD.itemBreakdownName : '');
                        self.CurrentItemBreakdownAbName(ItemBD ? ItemBD.itemBreakdownAbName : '');
                        self.CurrentUniteCode(ItemBD ? ItemBD.uniteCode : '');
                        self.CurrentZeroDispSet(ItemBD ? ItemBD.zeroDispSet : 1);
                        self.checked_002(ItemBD ? ItemBD.itemDispAtr == 1 ? false : true : false);
                        self.CurrentItemDispAtr(ItemBD ? ItemBD.itemDispAtr : 0);
                        self.checked_005(ItemBD ? ItemBD.errRangeLowAtr == 1 ? true : false : false);
                        self.CurrentErrRangeLow(ItemBD ? ItemBD.errRangeLow : 0);
                        self.checked_003(ItemBD ? ItemBD.errRangeHighAtr == 1 ? true : false : false);
                        self.CurrentErrRangeHigh(ItemBD ? ItemBD.errRangeHigh : 0);
                        self.checked_006(ItemBD ? ItemBD.alRangeLowAtr == 1 ? true : false : false);
                        self.CurrentAlRangeLow(ItemBD ? ItemBD.alRangeLow : 0);
                        self.checked_004(ItemBD ? ItemBD.alRangeHighAtr == 1 ? true : false : false);
                        self.CurrentAlRangeHigh(ItemBD ? ItemBD.alRangeHigh : 0);
                        if (ItemBD != undefined) {
                            //if item not undefined it mean active update mode
                            self.enable_I_INP_002(false);
                        }
                    });
                    self.enable_I_INP_002.subscribe(function (newValue) {
                        if (newValue) {
                            //it mean new mode 
                            self.I_BTN_003_enable(false);
                            self.gridListCurrentCode('');
                        }
                        else {
                            //it mean update mode
                            self.I_BTN_003_enable(true);
                            $('#I_INP_002').ntsError('clear');
                        }
                    });
                    self.CurrentItemBreakdownCode.subscribe(function (newValue) {
                        //validate item for not duplicate on client
                        if (self.enable_I_INP_002()) {
                            var item = _.find(self.ItemBDList(), function (ItemBD) {
                                return ItemBD.itemBreakdownCode == newValue;
                            });
                            if (item)
                                $('#I_INP_002').ntsError('set', 'えらーです');
                            else
                                $('#I_INP_002').ntsError('clear');
                        }
                    });
                    self.loadItemBDs();
                }
                loadItemBDs() {
                    let self = this;
                    self.CurrentItemMaster(nts.uk.ui.windows.getShared('itemMaster'));
                    let itemMaster = self.CurrentItemMaster();
                    if (itemMaster != undefined) {
                        self.reloadAndSetSelectedCode();
                        self.CurrentCategoryAtrName(self.CurrentItemMaster().categoryAtrName);
                        self.currentItemCode(itemMaster.itemCode);
                    }
                }
                reloadAndSetSelectedCode(itemCode) {
                    let self = this;
                    //reload list
                    i.service.findAllItemBD(self.CurrentItemMaster()).done(function (ItemBDs) {
                        self.ItemBDList(ItemBDs);
                        //set selected 
                        if (self.ItemBDList().length)
                            if (itemCode == undefined)
                                //if param itemCode == undefined => select first item in grid list
                                self.gridListCurrentCode(self.ItemBDList()[0].itemBreakdownCode);
                            else
                                //else set itemCode 
                                self.gridListCurrentCode(itemCode);
                    });
                }
                getCurrentItemBD() {
                    //get item customer has input on form 
                    let self = this;
                    return new i.service.model.ItemBD(self.CurrentItemMaster().itemCode, self.CurrentItemBreakdownCode(), self.CurrentItemBreakdownName(), self.CurrentItemBreakdownAbName(), self.CurrentUniteCode(), self.CurrentZeroDispSet(), self.checked_002() == true ? 0 : 1, self.checked_005() == true ? 1 : 0, self.CurrentErrRangeLow(), self.checked_003() == true ? 1 : 0, self.CurrentErrRangeHigh(), self.checked_006() == true ? 1 : 0, self.CurrentAlRangeLow(), self.checked_004() == true ? 1 : 0, self.CurrentAlRangeHigh());
                }
                saveItem() {
                    let self = this;
                    //if I_INP_002 is enable is mean add new mode => add new item
                    if (self.enable_I_INP_002())
                        self.addItemBD();
                    else
                        //else is update
                        self.updateItemBD();
                }
                deleteItem() {
                    let self = this;
                    let itemBD = self.CurrentItemBD();
                    if (itemBD) {
                        //show dialog
                        nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function () {
                            let itemCode;
                            let index = self.ItemBDList().indexOf(self.CurrentItemBD());
                            //set selected code after remove item
                            if (self.ItemBDList().length > 1) {
                                if (index < self.ItemBDList().length - 1)
                                    itemCode = self.ItemBDList()[index + 1].itemBreakdownCode;
                                else
                                    itemCode = self.ItemBDList()[index - 1].itemBreakdownCode;
                            }
                            else
                                itemCode = '';
                            //remove item 
                            itemBD.itemCode = self.CurrentItemMaster().itemCode;
                            i.service.deleteItemBD(itemBD, self.CurrentItemMaster()).done(function (any) {
                                // set selected code
                                self.reloadAndSetSelectedCode(itemCode);
                            }).fail(function (res) {
                                alert(res.value);
                            });
                        });
                    }
                }
                addItemBD() {
                    let self = this;
                    //get itemBD on form
                    let itemBD = self.getCurrentItemBD();
                    i.service.addItemBD(itemBD, self.CurrentItemMaster()).done(function (any) {
                        // set selected code
                        self.reloadAndSetSelectedCode(itemBD.itemBreakdownCode);
                    }).fail(function (res) {
                        alert(res.value);
                    });
                }
                updateItemBD() {
                    let self = this;
                    let itemBD = self.getCurrentItemBD();
                    let itemCode = itemBD.itemBreakdownCode;
                    //update item 
                    i.service.updateItemBD(itemBD, self.CurrentItemMaster()).done(function (any) {
                        // set selected code
                        self.reloadAndSetSelectedCode(itemBD.itemBreakdownCode);
                    }).fail(function (res) {
                        alert(res.value);
                    });
                }
                closeDialog() {
                    let self = this;
                    nts.uk.ui.windows.setShared('itemBDs', self.ItemBDList());
                    nts.uk.ui.windows.close();
                }
                addNewItem() {
                    let self = this;
                    self.enable_I_INP_002(true);
                }
            }
            viewmodel.ScreenModel = ScreenModel;
        })(viewmodel = i.viewmodel || (i.viewmodel = {}));
    })(i = qmm012.i || (qmm012.i = {}));
})(qmm012 || (qmm012 = {}));
