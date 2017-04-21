var qmm012;
(function (qmm012) {
    var b;
    (function (b) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel(screenModel) {
                    this.enable = ko.observable(true);
                    this.B_Selected_ItemClassification = ko.observable(1);
                    //gridlist
                    this.GridlistItems = ko.observableArray([]);
                    this.GridlistCurrentCode = ko.observable('');
                    this.GridlistCurrentItem = ko.observable(null);
                    this.GridCurrentItemAbName = ko.observable('');
                    this.GridCurrentItemName = ko.observable('');
                    this.GridCurrentDisplaySet = ko.observable(false);
                    this.GridCurrentUniteCode = ko.observable('');
                    this.GridCurrentCategoryAtr = ko.observable(0);
                    this.GridCurrentCategoryAtrName = ko.observable('');
                    this.GridCurrentCodeAndName = ko.observable('');
                    this.B_Inp_Code_text = ko.observable('');
                    this.categoryAtr = -1;
                    //Checkbox
                    //B_002
                    this.Checked_AlsoDisplayAbolition = ko.observable(false);
                    this.enable_B_Inp_Code = ko.observable(false);
                    this.B_Btn_DeleteButton_enable = ko.observable(true);
                    this.dirtyItemMaster = ko.observable(null);
                    this.dirtyOldValue = new DirtyValue(1, '', false);
                    var self = this;
                    self.screenModel = screenModel;
                    //set combobox data
                    //001
                    self.B_Sel_ItemClassification = ko.observableArray([
                        new ComboboxItemModel(1, '全件'),
                        new ComboboxItemModel(2, '支給項目'),
                        new ComboboxItemModel(3, '控除項目'),
                        new ComboboxItemModel(4, '勤怠項目'),
                        new ComboboxItemModel(5, '記事項目'),
                        new ComboboxItemModel(6, 'その他項目')
                    ]);
                    self.B_Selected_ItemClassification.subscribe(function (newValue) {
                        if (self.dirtyOldValue.selItemClassification != newValue) {
                            self.dirtyItemMaster(self.getCurrentItemMaster());
                            var categoryAtr_1;
                            switch (newValue) {
                                case 1:
                                    //select  all  
                                    categoryAtr_1 = -1;
                                    break;
                                case 2:
                                    // 支給
                                    categoryAtr_1 = 0;
                                    break;
                                case 3:
                                    // 控除
                                    categoryAtr_1 = 1;
                                    break;
                                case 4:
                                    // 勤怠
                                    categoryAtr_1 = 2;
                                    break;
                                case 5:
                                    //記事
                                    categoryAtr_1 = 3;
                                    break;
                                case 6:
                                    //その他
                                    categoryAtr_1 = 9;
                                    break;
                            }
                            self.activeDirty(function () {
                                self.dirtyOldValue.selItemClassification = newValue;
                                self.categoryAtr = categoryAtr_1;
                                //then load gridlist
                                self.loadGridList();
                            }, function () {
                                self.dirtyOldValue.selItemClassification = newValue;
                                self.categoryAtr = categoryAtr_1;
                                self.GridlistCurrentItem(self.GridlistCurrentItem());
                                //then load gridlist
                                self.loadGridList();
                            }, function () {
                                self.B_Selected_ItemClassification(self.dirtyOldValue.selItemClassification);
                            });
                        }
                    });
                    self.Checked_AlsoDisplayAbolition.subscribe(function (newValue) {
                        if (self.dirtyOldValue.selAlsoDisplayAbolition != newValue) {
                            self.activeDirty(function () {
                                self.dirtyOldValue.selAlsoDisplayAbolition = newValue;
                                self.loadGridList();
                            }, function () {
                                self.dirtyOldValue.selAlsoDisplayAbolition = newValue;
                                //then load gridlist
                                self.loadGridList();
                            }, function () {
                                self.Checked_AlsoDisplayAbolition(self.dirtyOldValue.selAlsoDisplayAbolition);
                            });
                        }
                    });
                    // set gridlist data
                    //gridlist column
                    self.GridColumns = ko.observableArray([
                        { headerText: '項目区分', prop: 'categoryAtrName', width: 80 },
                        { headerText: 'コード', prop: 'itemCode', width: 50 },
                        { headerText: '名称', prop: 'itemName', width: 150 },
                        { headerText: '廃止', prop: 'displaySet', width: 20, formatter: makeIcon }
                    ]);
                    function makeIcon(val) {
                        if (val == 1)
                            //it  mean この項目名を廃止する , bind X icon
                            return "<div class = 'NoIcon' > </div>";
                        return "";
                    }
                    self.GridlistCurrentCode.subscribe(function (newValue) {
                        if (newValue != self.dirtyOldValue.lstCode) {
                            var item = _.find(self.GridlistItems(), function (ItemModel) {
                                return ItemModel.itemCode == newValue;
                            });
                            self.activeDirty(function () {
                                self.GridlistCurrentItem(item);
                            }, function () {
                                self.GridlistCurrentItem(item);
                            }, function () {
                                self.GridlistCurrentCode(self.dirtyOldValue.lstCode);
                            });
                        }
                    });
                    self.GridlistCurrentItem.subscribe(function (itemModel) {
                        self.clearAllValidateError();
                        self.GridCurrentItemName(itemModel ? itemModel.itemName : '');
                        self.GridCurrentUniteCode(itemModel ? itemModel.uniteCode : '');
                        //set text for B_Inp_Code
                        self.B_Inp_Code_text(itemModel ? itemModel.itemCode : '');
                        self.GridCurrentCodeAndName(itemModel ? itemModel.itemCode + ' ' + itemModel.itemName : '');
                        self.GridCurrentDisplaySet(itemModel ? itemModel.displaySet == 1 ? true : false : false);
                        self.GridCurrentItemAbName(itemModel ? itemModel.itemAbName : '');
                        self.GridCurrentCategoryAtrName(itemModel ? itemModel.categoryAtrName : '');
                        //when itemModel != undefined , need disable INP_002
                        if (itemModel ? itemModel.itemCode != '' : false) {
                            self.enable_B_Inp_Code(false);
                            self.GridCurrentCategoryAtr(itemModel.categoryAtr);
                        }
                        self.ChangeGroup(self.GridCurrentCategoryAtr()).done(function () {
                            self.dirtyItemMaster(self.getCurrentItemMaster());
                            if (self.dirty)
                                self.dirty.reset();
                            self.dirtyOldValue.lstCode = self.GridlistCurrentCode();
                        });
                    });
                    //first load , need call loadGridList
                    self.loadGridList();
                    self.enable_B_Inp_Code.subscribe(function (newValue) {
                        if (!newValue) {
                            //it mean update item mode
                            self.setUpdateItemMode();
                        }
                    });
                    //set text editer data
                    //INP_002
                    self.texteditor_B_Inp_Code = {
                        value: self.B_Inp_Code_text,
                        option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                            textmode: "text",
                            placeholder: "",
                            textalign: "left"
                        })),
                        enable: self.enable_B_Inp_Code
                    };
                    //INP_003
                    self.texteditor_B_Inp_Name = {
                        value: self.GridCurrentItemName,
                        option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                            textmode: "text",
                            placeholder: "",
                            textalign: "left"
                        }))
                    };
                    //INP_004
                    self.texteditor_B_Inp_AbbreviatedName = {
                        value: self.GridCurrentItemAbName,
                        option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                            textmode: "text",
                            placeholder: "",
                            textalign: "left"
                        }))
                    };
                    self.B_Inp_Code_text.subscribe(function (newValue) {
                        if (self.enable_B_Inp_Code()) {
                            var item = _.find(self.GridlistItems(), function (ItemModel) {
                                return ItemModel.itemCode == newValue;
                            });
                            if (item) {
                                $('#B_Inp_Code').ntsError('set', '入力したコードは既に存在しています。');
                            }
                        }
                    });
                }
                ScreenModel.prototype.ChangeGroup = function (newValue) {
                    var dfd = $.Deferred();
                    var self = this;
                    $('#screenC').hide();
                    $('#screenD').hide();
                    $('#screenE').hide();
                    $('#screenF').hide();
                    switch (newValue) {
                        case 0:
                            //支給
                            $('#screenC').show();
                            self.screenModel.screenModelC.loadData(self.GridlistCurrentItem()).done(function () {
                                dfd.resolve("done");
                            });
                            break;
                        case 1:
                            //控除
                            $('#screenD').show();
                            self.screenModel.screenModelD.loadData(self.GridlistCurrentItem()).done(function () {
                                dfd.resolve("done");
                            });
                            break;
                        case 2:
                            //勤怠
                            $('#screenE').show();
                            self.screenModel.screenModelE.loadData(self.GridlistCurrentItem()).done(function () {
                                dfd.resolve("done");
                            });
                            break;
                        case 3:
                            //記事
                            $('#screenF').show();
                            self.screenModel.screenModelF.loadData(self.GridlistCurrentItem()).done(function () {
                                dfd.resolve("done");
                            });
                            break;
                        case 9:
                            dfd.resolve("done");
                            break;
                    }
                    return dfd.promise();
                    //why don't have その他 ? because it show nothing
                };
                ScreenModel.prototype.clearAllValidateError = function () {
                    $('.save-error').ntsError('clear');
                };
                ScreenModel.prototype.reload = function () {
                    var self = this;
                    location.reload(true);
                };
                ScreenModel.prototype.setNewItemMode = function () {
                    var self = this;
                    self.dirtyOldValue.lstCode = '';
                    self.GridlistCurrentCode('');
                    self.clearContent();
                    //disable delete button
                    self.B_Btn_DeleteButton_enable(false);
                    //disable 有効期間設定 button
                    self.screenModel.screenModelC.C_Btn_PeriodSetting_enable(false);
                    self.screenModel.screenModelC.C_Btn_BreakdownSetting_enable(false);
                    self.screenModel.screenModelD.D_Btn_PeriodSetting_enable(false);
                    self.screenModel.screenModelD.D_Btn_BreakdownSetting_enable(false);
                };
                ScreenModel.prototype.clearContent = function () {
                    var self = this;
                    self.clearAllValidateError();
                    self.GridCurrentItemName('');
                    self.GridCurrentUniteCode('');
                    //set text for B_Inp_Code
                    self.B_Inp_Code_text('');
                    self.GridCurrentCodeAndName('');
                    self.GridCurrentDisplaySet(false);
                    self.GridCurrentItemAbName('');
                    self.GridCurrentCategoryAtrName('');
                    //when itemModel != undefined , need disable INP_002
                    self.enable_B_Inp_Code(true);
                    self.GridlistCurrentItem(self.getCurrentItemMaster());
                };
                ScreenModel.prototype.setUpdateItemMode = function () {
                    var self = this;
                    //if from new mode change to update mode , need clear all ntsError 
                    $('#B_Inp_Code').ntsError('clear');
                    //enable delete button
                    self.B_Btn_DeleteButton_enable(true);
                    //enable 有効期間設定 button
                    self.screenModel.screenModelC.C_Btn_PeriodSetting_enable(true);
                    self.screenModel.screenModelC.C_Btn_BreakdownSetting_enable(true);
                    self.screenModel.screenModelD.D_Btn_PeriodSetting_enable(true);
                    self.screenModel.screenModelD.D_Btn_BreakdownSetting_enable(true);
                };
                ScreenModel.prototype.getCurrentItemMaster = function () {
                    //get item master The user entered on the form 
                    var self = this;
                    //this is item master Constructor
                    var itemMaster = new b.service.model.ItemMaster(self.B_Inp_Code_text(), self.GridCurrentItemName(), self.GridCurrentCategoryAtr(), self.GridCurrentCategoryAtrName(), self.GridCurrentItemAbName(), self.GridlistCurrentItem() ? self.GridlistCurrentItem().itemAbNameO : "", self.GridlistCurrentItem() ? self.GridlistCurrentItem().itemAbNameE : "", self.GridCurrentDisplaySet() == true ? 1 : 0, self.GridCurrentUniteCode(), self.getCurrentZeroDisplaySet(), self.getCurrentItemDisplayAtr(), 1);
                    //set sub item constructor
                    itemMaster.itemSalary = self.screenModel.screenModelC.GetCurrentItemSalary();
                    itemMaster.itemDeduct = self.screenModel.screenModelD.GetCurrentItemDeduct();
                    itemMaster.itemAttend = self.screenModel.screenModelE.getCurrentItemAttend();
                    return itemMaster;
                };
                ScreenModel.prototype.validateItemMaster = function () {
                    var self = this;
                    $("#B_Inp_Code").ntsEditor("validate");
                    $("#B_Inp_Name").ntsEditor("validate");
                    $("#B_Inp_AbbreviatedName").ntsEditor("validate");
                    $("#C_Inp_LimitAmount").ntsEditor("validate");
                    if ($('.nts-editor').ntsError("hasError")) {
                        return true;
                    }
                    return false;
                };
                ScreenModel.prototype.loadGridList = function (ItemCode) {
                    var self = this;
                    var categoryAtr = self.categoryAtr;
                    //load dispSet 
                    //if 0  mean
                    // no only view この項目名を廃止する 
                    //else view all
                    var dispSet = self.Checked_AlsoDisplayAbolition() ? -1 : 0;
                    //call service load findAllItemMaster
                    b.service.findAllItemMaster(categoryAtr, dispSet).done(function (MasterItems) {
                        self.GridlistItems(MasterItems);
                        //set selected first item in list
                        if (self.dirty)
                            self.dirty.reset();
                        if (self.GridlistItems().length > 0) {
                            // if not itemcode parameter
                            if (!ItemCode) {
                                //set GridlistCurrentCode selected first item in gridlist
                                self.GridlistCurrentItem(self.GridlistItems()[0]);
                                self.dirtyOldValue.lstCode = self.GridlistItems()[0].itemCode;
                                self.GridlistCurrentCode(self.GridlistItems()[0].itemCode);
                            }
                            else {
                                //set  selected == param itemcode
                                var item = _.find(self.GridlistItems(), function (ItemModel) {
                                    return ItemModel.itemCode == ItemCode;
                                });
                                if (item) {
                                    self.GridlistCurrentItem(item);
                                    self.dirtyOldValue.lstCode = ItemCode;
                                    self.GridlistCurrentCode(ItemCode);
                                }
                                else {
                                    self.GridlistCurrentItem(self.GridlistItems()[0]);
                                    self.dirtyOldValue.lstCode = self.GridlistItems()[0].itemCode;
                                    self.GridlistCurrentCode(self.GridlistItems()[0].itemCode);
                                }
                            }
                            self.dirtyItemMaster(self.getCurrentItemMaster());
                            self.dirty = new nts.uk.ui.DirtyChecker(self.dirtyItemMaster);
                        }
                        else {
                            //if no item, show message set new mode
                            nts.uk.ui.dialog.alert("対象データがありません。");
                            self.setNewItemMode();
                        }
                    }).fail(function (res) {
                        nts.uk.ui.dialog.alert(res);
                    });
                };
                ScreenModel.prototype.deleteItem = function () {
                    var self = this;
                    var ItemMaster = self.getCurrentItemMaster();
                    var index = self.GridlistItems.indexOf(self.GridlistCurrentItem());
                    //if has item selected
                    if (index >= 0) {
                        //show dialog
                        nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function () {
                            //if yes call service delete item
                            b.service.deleteItemMaster(ItemMaster).done(function (any) {
                                //reload grid and set select code after delete item success
                                var selectItemCode;
                                //if after delete gridlist length >0
                                if (self.GridlistItems().length - 1 > 1) {
                                    if (index < self.GridlistItems().length - 1)
                                        //if not last selected item , set selected same position
                                        selectItemCode = self.GridlistItems()[index + 1].itemCode;
                                    else
                                        //else selected item Before it
                                        selectItemCode = self.GridlistItems()[index - 1].itemCode;
                                }
                                else
                                    //length < 0 no select any thing
                                    selectItemCode = '';
                                //reload gruid list
                                self.loadGridList(selectItemCode);
                            }).fail(function (res) {
                                nts.uk.ui.dialog.alert(res);
                            });
                        });
                    }
                };
                ScreenModel.prototype.getCurrentZeroDisplaySet = function () {
                    var Result;
                    var self = this;
                    var CurrentGroup = self.GridCurrentCategoryAtr();
                    switch (CurrentGroup) {
                        case 0:
                            //支給
                            Result = self.screenModel.screenModelC.CurrentZeroDisplaySet();
                            break;
                        case 1:
                            //控除
                            Result = self.screenModel.screenModelD.CurrentZeroDisplaySet();
                            break;
                        case 2:
                            //勤怠
                            Result = self.screenModel.screenModelE.CurrentZeroDisplaySet();
                            break;
                        //記事
                        case 3:
                            Result = self.screenModel.screenModelF.CurrentZeroDisplaySet();
                            break;
                    }
                    return Result;
                };
                ScreenModel.prototype.getCurrentItemDisplayAtr = function () {
                    //like getCurrentZeroDisplaySet
                    var Result;
                    var self = this;
                    var CurrentGroup = self.GridCurrentCategoryAtr();
                    switch (CurrentGroup) {
                        case 0:
                            Result = self.screenModel.screenModelC.CurrentItemDisplayAtr();
                            break;
                        case 1:
                            Result = self.screenModel.screenModelD.CurrentItemDisplayAtr();
                            break;
                        case 2:
                            Result = self.screenModel.screenModelE.CurrentItemDisplayAtr();
                            break;
                        case 3:
                            Result = self.screenModel.screenModelF.CurrentItemDisplayAtr();
                            break;
                    }
                    return Result;
                };
                ScreenModel.prototype.openADialog = function () {
                    var self = this;
                    nts.uk.ui.windows.sub.modal('../a/index.xhtml', { height: 480, width: 630, dialogClass: "no-close" }).onClosed(function () {
                        if (nts.uk.ui.windows.getShared('groupCode') != undefined) {
                            //get group from session
                            var groupCode = Number(nts.uk.ui.windows.getShared('groupCode'));
                            //set layout for new.
                            self.GridCurrentCategoryAtr(groupCode);
                            self.setNewItemMode();
                        }
                    });
                };
                ScreenModel.prototype.copyButtonClick = function () {
                    var self = this;
                    self.activeDirty(function () { });
                };
                ScreenModel.prototype.exportExcelButtonClick = function () {
                    var self = this;
                    self.activeDirty(function () { });
                };
                ScreenModel.prototype.submitData = function () {
                    var self = this;
                    if (!self.validateItemMaster()) {
                        var ItemMaster = self.getCurrentItemMaster();
                        //if self.enable_B_Inp_Code == true is mean New mode
                        if (self.enable_B_Inp_Code()) {
                            self.addNewItemMaster(ItemMaster);
                        }
                        else {
                            //else update mode
                            self.updateItemMaster(ItemMaster);
                        }
                    }
                };
                ScreenModel.prototype.addNewItemMaster = function (ItemMaster) {
                    var self = this;
                    //call add service
                    b.service.addItemMaster(ItemMaster).done(function (any) {
                        //after add , reload grid list
                        self.loadGridList(ItemMaster.itemCode);
                    }).fail(function (res) {
                        nts.uk.ui.dialog.alert(res);
                    });
                };
                ScreenModel.prototype.updateItemMaster = function (ItemMaster) {
                    var self = this;
                    //call update service
                    b.service.updateItemMaster(ItemMaster).done(function (any) {
                        //after add , reload grid list
                        self.loadGridList(ItemMaster.itemCode);
                    }).fail(function (res) {
                        nts.uk.ui.dialog.alert(res);
                    });
                };
                ScreenModel.prototype.registerPrintingNameButtonClick = function () {
                    var self = this;
                    self.activeDirty(function () { self.openJDialog(); }, function () { self.openJDialog(); });
                };
                ScreenModel.prototype.addNewButtonClick = function () {
                    var self = this;
                    //set value client has input on form 
                    self.activeDirty(function () { self.openADialog(); }, function () { self.openADialog(); });
                };
                ScreenModel.prototype.openJDialog = function () {
                    var self = this;
                    nts.uk.ui.windows.sub.modal('../j/index.xhtml', { height: 700, width: 970, dialogClass: "no-close" }).onClosed(function () {
                        self.loadGridList(self.GridlistCurrentCode());
                    });
                };
                ScreenModel.prototype.activeDirty = function (MainFunction, YesFunction, NoFunction) {
                    var self = this;
                    self.dirtyItemMaster(self.getCurrentItemMaster());
                    if (self.dirty ? !self.dirty.isDirty() : true) {
                        MainFunction();
                    }
                    else {
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。  ").ifYes(function () {
                            //reset data on form when not save 
                            self.GridlistCurrentItem(self.GridlistCurrentItem());
                            if (YesFunction)
                                YesFunction();
                        }).ifNo(function () {
                            if (NoFunction)
                                NoFunction();
                        });
                    }
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var DirtyValue = (function () {
                function DirtyValue(selItemClassification, lstCode, selAlsoDisplayAbolition) {
                    this.selItemClassification = selItemClassification;
                    this.lstCode = lstCode;
                    this.selAlsoDisplayAbolition = selAlsoDisplayAbolition;
                }
                return DirtyValue;
            }());
            var ComboboxItemModel = (function () {
                function ComboboxItemModel(code, name) {
                    this.code = code;
                    this.name = name;
                }
                return ComboboxItemModel;
            }());
        })(viewmodel = b.viewmodel || (b.viewmodel = {}));
    })(b = qmm012.b || (qmm012.b = {}));
})(qmm012 || (qmm012 = {}));
