module qmm012.i.viewmodel {
    export class ScreenModel {

        //textediter

        //Checkbox
        Checked_NoDisplay: KnockoutObservable<boolean> = ko.observable(false);
        Checked_ErrorUpper: KnockoutObservable<boolean> = ko.observable(false);
        Checked_AlarmUpper: KnockoutObservable<boolean> = ko.observable(false);
        Checked_ErrorLower: KnockoutObservable<boolean> = ko.observable(false);
        Checked_AlarmLower: KnockoutObservable<boolean> = ko.observable(false);

        columns: KnockoutObservableArray<any>;
        gridListCurrentCode: KnockoutObservable<string> = ko.observable('');
        //Switch
        roundingRules_ZeroDisplayIndicator: KnockoutObservableArray<any>;
        noDisplay_Enable: KnockoutObservable<boolean> = ko.observable(false);
        //currencyeditor
        currencyeditor_I_Inp_ErrorUpper: any;
        currencyeditor_I_Inp_AlarmUpper: any;
        currencyeditor_I_Inp_ErrorLower: any;
        currencyeditor_I_Inp_AlarmLower: any;
        //search box 
        filteredData: any;
        CurrentItemMaster: KnockoutObservable<qmm012.b.service.model.ItemMaster> = ko.observable(null);
        ItemBDList: KnockoutObservableArray<service.model.ItemBD> = ko.observableArray([]);
        CurrentCategoryAtrName: KnockoutObservable<string> = ko.observable('');
        CurrentItemBD: KnockoutObservable<service.model.ItemBD> = ko.observable(null);
        dirtyItemBD: KnockoutObservable<service.model.ItemBD> = ko.observable(null);
        CurrentItemBreakdownCode: KnockoutObservable<string> = ko.observable('');
        CurrentItemBreakdownName: KnockoutObservable<string> = ko.observable('');
        CurrentItemBreakdownAbName: KnockoutObservable<string> = ko.observable('');
        CurrentUniteCode: KnockoutObservable<string> = ko.observable('');
        CurrentZeroDispSet: KnockoutObservable<number> = ko.observable(1);
        CurrentItemDispAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeHigh: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeHigh: KnockoutObservable<number> = ko.observable(0);

        enable_I_Inp_Code: KnockoutObservable<boolean> = ko.observable(false);
        I_Btn_DeleteButton_enable: KnockoutObservable<boolean> = ko.observable(true);
        currentItemCode: KnockoutObservable<string> = ko.observable('');
        dirty: nts.uk.ui.DirtyChecker;
        oldGridListCurrentCode: KnockoutObservable<string> = ko.observable('');
        ItemCategoryName: KnockoutObservable<string> = ko.observable('');
        constructor() {
            var self = this;
            //start Switch Data
            self.roundingRules_ZeroDisplayIndicator = ko.observableArray([
                { code: 1, name: 'ゼロを表示する' },
                { code: 0, name: 'ゼロを表示しない' }
            ]);
            //endSwitch Data
            //currencyeditor
            //005
            self.currencyeditor_I_Inp_ErrorUpper = {
                value: self.CurrentErrRangeHigh,
                constraint: 'ErrRangeHigh',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_ErrorUpper
            };
            //006
            self.currencyeditor_I_Inp_AlarmUpper = {
                value: self.CurrentAlRangeHigh,
                constraint: 'AlRangeHigh',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_AlarmUpper
            };
            //007
            self.currencyeditor_I_Inp_ErrorLower = {
                value: self.CurrentErrRangeLow,
                constraint: 'ErrRangeLow',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_ErrorLower
            };
            //008
            self.currencyeditor_I_Inp_AlarmLower = {
                value: self.CurrentAlRangeLow,
                constraint: 'AlRangeLow',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable: self.Checked_AlarmLower
            };
            //end currencyeditor
            // start search box 
            self.filteredData = ko.observableArray(nts.uk.util.flatArray(self.ItemBDList(), "childs"));
            // end search box 
            self.columns = ko.observableArray([
                { headerText: 'ード', prop: 'itemBreakdownCode', width: 100 },
                { headerText: '名', prop: 'itemBreakdownName', width: 150 }
            ]);

            self.gridListCurrentCode.subscribe(function(newValue) {
                if (self.oldGridListCurrentCode() != newValue) {
                    var item = _.find(self.ItemBDList(), function(ItemBD: service.model.ItemBD) {
                        return ItemBD.itemBreakdownCode == newValue;
                    });
                    self.activeDirty(
                        function() { self.CurrentItemBD(item ? item : new service.model.ItemBD()); },
                        function() { self.CurrentItemBD(item ? item : new service.model.ItemBD()); },
                        function() { self.gridListCurrentCode(self.oldGridListCurrentCode()); });
                }
            });
            self.CurrentItemBD.subscribe(function(itemBD: service.model.ItemBD) {
                self.clearAllValidateError();
                self.CurrentItemBreakdownCode(itemBD ? itemBD.itemBreakdownCode : '');
                self.CurrentItemBreakdownName(itemBD ? itemBD.itemBreakdownName : '');
                self.CurrentItemBreakdownAbName(itemBD ? itemBD.itemBreakdownAbName : '');
                self.CurrentUniteCode(itemBD ? itemBD.uniteCode : '');
                self.CurrentZeroDispSet(itemBD ? itemBD.zeroDispSet : 1);
                self.Checked_NoDisplay(itemBD ? itemBD.itemDispAtr == 1 ? false : true : false);
                self.CurrentItemDispAtr(itemBD ? itemBD.itemDispAtr : 0);
                self.Checked_ErrorLower(itemBD ? itemBD.errRangeLowAtr == 1 ? true : false : false);
                self.CurrentErrRangeLow(itemBD ? itemBD.errRangeLow : 0);
                self.Checked_ErrorUpper(itemBD ? itemBD.errRangeHighAtr == 1 ? true : false : false);
                self.CurrentErrRangeHigh(itemBD ? itemBD.errRangeHigh : 0);
                self.Checked_AlarmLower(itemBD ? itemBD.alRangeLowAtr == 1 ? true : false : false);
                self.CurrentAlRangeLow(itemBD ? itemBD.alRangeLow : 0);
                self.Checked_AlarmUpper(itemBD ? itemBD.alRangeHighAtr == 1 ? true : false : false);
                self.CurrentAlRangeHigh(itemBD ? itemBD.alRangeHigh : 0);
                if (itemBD ? itemBD.itemCode != '' : false) {
                    //if item not undefined it mean active update mode
                    self.enable_I_Inp_Code(false);
                }
                self.dirtyItemBD(self.getCurrentItemBD());
                if (self.dirty)
                    self.dirty.reset();
                self.oldGridListCurrentCode(self.gridListCurrentCode());
            });
            self.enable_I_Inp_Code.subscribe(function(newValue) {
                if (newValue) {
                    //it mean new mode 
                    self.I_Btn_DeleteButton_enable(false);
                }
                else {
                    //it mean update mode
                    self.I_Btn_DeleteButton_enable(true);
                    $('#I_Inp_Code').ntsError('clear');
                }

            })
            self.CurrentItemBreakdownCode.subscribe(function(newValue) {
                //validate item for not duplicate on client
                if (self.enable_I_Inp_Code()) {
                    var item = _.find(self.ItemBDList(), function(ItemBD: service.model.ItemBD) {
                        return ItemBD.itemBreakdownCode == newValue;
                    });
                    if (item)
                        $('#I_Inp_Code').ntsError('set', '入力したコードは既に存在しています');
                }
            });
            self.CurrentZeroDispSet.subscribe(function(newValue) {
                if (newValue == 0) {
                    self.noDisplay_Enable(true);
                } else {
                    self.noDisplay_Enable(false);
                }
            });

            self.loadItemBDs();
        }
        clearAllValidateError() {
            $('.save-error').ntsError('clear');
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
            self.ItemCategoryName(self.genCategoryName(self.CurrentItemMaster().categoryAtr));
        }
        genCategoryName(categoryAtr) {
            let CategoryName = '';
            switch (categoryAtr) {
                case 0:
                    CategoryName = "支給内訳項目";
                    break;
                case 1:
                    CategoryName = "控除内訳項目";
                    break;

            }
            return CategoryName;
        }
        reloadAndSetSelectedCode(itemCode?) {
            let self = this;
            //reload list
            service.findAllItemBD(self.CurrentItemMaster()).done(function(ItemBDs: Array<service.model.ItemBD>) {
                self.ItemBDList(ItemBDs);
                //set selected 
                if (self.ItemBDList().length) {
                    if (itemCode == undefined)
                        //if param itemCode == undefined => select first item in grid list
                        self.gridListCurrentCode(self.ItemBDList()[0].itemBreakdownCode);
                    else {
                        //else set itemCode 
                        var item = _.find(self.ItemBDList(), function(ItemBD: service.model.ItemBD) {
                            return ItemBD.itemBreakdownCode == itemCode;
                        });
                        self.CurrentItemBD(item);
                        self.oldGridListCurrentCode(itemCode);
                        self.gridListCurrentCode(itemCode);
                    }
                    self.dirtyItemBD(self.getCurrentItemBD());
                    self.dirty = new nts.uk.ui.DirtyChecker(self.dirtyItemBD);
                } else {
                    //if no item,set new mode
                    //  nts.uk.ui.dialog.alert("対象データがありません。");
                    self.setNewMode();
                }

            });
        }
        getCurrentItemBD() {
            //get item customer has input on form 
            let self = this;
            return new service.model.ItemBD(
                self.CurrentItemMaster().itemCode,
                self.CurrentItemBreakdownCode(),
                self.CurrentItemBreakdownName(),
                self.CurrentItemBreakdownAbName(),
                self.CurrentUniteCode(),
                self.CurrentZeroDispSet(),
                self.Checked_NoDisplay() == true ? 0 : 1,
                self.Checked_ErrorLower() == true ? 1 : 0,
                self.CurrentErrRangeLow(),
                self.Checked_ErrorUpper() == true ? 1 : 0,
                self.CurrentErrRangeHigh(),
                self.Checked_AlarmLower() == true ? 1 : 0,
                self.CurrentAlRangeLow(),
                self.Checked_AlarmUpper() == true ? 1 : 0,
                self.CurrentAlRangeHigh()
            );
        }
        validateItemBD() {
            $('#I_Inp_Code').ntsEditor('validate');
            $('#I_Inp_Name').ntsEditor('validate');
            $('#I_Inp_AbbreviatedName').ntsEditor('validate');
            if ($('.nts-editor').ntsError("hasError")) {
                return true;
            }
            return false;
        }
        saveItem() {
            let self = this;
            //if I_Inp_Code is enable is mean add new mode
            if (!self.validateItemBD()) {
                let itemBD = self.getCurrentItemBD();
                if (self.enable_I_Inp_Code())
                    self.addItemBD(itemBD);
                else
                    self.updateItemBD(itemBD);
            }

        }
        deleteItem() {
            let self = this;
            let itemBD = self.CurrentItemBD();
            if (itemBD) {
                nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                    let itemCode;
                    let index = self.ItemBDList().indexOf(self.CurrentItemBD());
                    //set selected code after remove item
                    if (self.ItemBDList().length > 1) {
                        if (index < self.ItemBDList().length - 1)
                            itemCode = self.ItemBDList()[index + 1].itemBreakdownCode;
                        else
                            itemCode = self.ItemBDList()[index - 1].itemBreakdownCode;
                    } else
                        itemCode = '';
                    //remove item 
                    itemBD.itemCode = self.CurrentItemMaster().itemCode;
                    service.deleteItemBD(itemBD, self.CurrentItemMaster()).done(function(any) {
                        // set selected code
                        self.reloadAndSetSelectedCode(itemCode);
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alert(res.value);
                    });

                })
            }
        }
        activeDirty(MainFunction, YesFunction?, NoFunction?) {
            let self = this;
            self.dirtyItemBD(self.getCurrentItemBD());
            //   if (self.dirty ? !self.dirty.isDirty() : true) {
            MainFunction();
            //   } else {
            //     nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。  ").ifYes(function() {
            //reset data on form when not save 
            //        self.gridListCurrentCode(self.gridListCurrentCode());
            //         if (YesFunction)
            //          YesFunction();
            //    }).ifNo(function() {
            //        if (NoFunction)
            //             NoFunction();
            //     })
            //    }
        }
        addItemBD(itemBD) {
            let self = this;
            //get itemBD on form
            service.addItemBD(itemBD, self.CurrentItemMaster()).done(function(any) {
                // set selected code
                self.CurrentItemBD(itemBD);
                self.reloadAndSetSelectedCode(itemBD.itemBreakdownCode);
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.value);
            });
        }
        updateItemBD(itemBD) {
            let self = this;
            let itemCode = itemBD.itemBreakdownCode;
            //update item 
            service.updateItemBD(itemBD, self.CurrentItemMaster()).done(function(any) {
                // set selected code
                self.reloadAndSetSelectedCode(itemCode);
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res.value);
            });
        }
        closeDialog() {
            let self = this;
            self.activeDirty(function() {
                nts.uk.ui.windows.setShared('itemBDs', self.ItemBDList())
                nts.uk.ui.windows.close();
            }, function() {
                nts.uk.ui.windows.setShared('itemBDs', self.ItemBDList())
                nts.uk.ui.windows.close();
            });

        }
        setNewMode() {
            let self = this;
            self.enable_I_Inp_Code(true);
            self.clearForm();
        }
        clearForm() {
            let self = this;
            self.clearAllValidateError();
            self.CurrentItemBreakdownCode('');
            self.CurrentItemBreakdownName('');
            self.CurrentItemBreakdownAbName('');
            self.CurrentUniteCode('');
            self.CurrentZeroDispSet(1);
            self.Checked_NoDisplay(false);
            self.CurrentItemDispAtr(0);
            self.Checked_ErrorLower(false);
            self.CurrentErrRangeLow(0);
            self.Checked_ErrorUpper(false);
            self.CurrentErrRangeHigh(0);
            self.Checked_AlarmLower(false);
            self.CurrentAlRangeLow(0);
            self.Checked_AlarmUpper(false);
            self.CurrentAlRangeHigh(0);
            self.dirtyItemBD(self.getCurrentItemBD());
            if (self.dirty)
                self.dirty.reset();
            self.oldGridListCurrentCode('');
            self.gridListCurrentCode('');
        }
        addNewItem() {
            let self = this;
            self.activeDirty(function() { self.setNewMode(); }, function() { self.setNewMode(); });
        }
    }

}