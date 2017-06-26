var qmm012;
(function (qmm012) {
    var b;
    (function (b) {
        var viewmodel;
        (function (viewmodel) {
            class ScreenModel {
                constructor(screenModel) {
                    this.enable = ko.observable(true);
                    this.selectedCode_B_001 = ko.observable(1);
                    //gridlist
                    this.GridlistItems_B_001 = ko.observableArray([]);
                    this.GridlistCurrentCode_B_001 = ko.observable('');
                    this.GridlistCurrentItem_B_001 = ko.observable(null);
                    this.GridCurrentItemAbName_B_001 = ko.observable('');
                    this.GridCurrentItemName_B_001 = ko.observable('');
                    this.GridCurrentDisplaySet_B_001 = ko.observable(false);
                    this.GridCurrentUniteCode_B_001 = ko.observable('');
                    this.GridCurrentCategoryAtr_B_001 = ko.observable(0);
                    this.GridCurrentCategoryAtrName_B_001 = ko.observable('');
                    this.GridCurrentCodeAndName_B_001 = ko.observable('');
                    this.B_INP_002_text = ko.observable('');
                    this.categoryAtr = -1;
                    //Checkbox
                    //B_002
                    this.checked_B_002 = ko.observable(false);
                    this.enable_B_INP_002 = ko.observable(false);
                    this.B_BTN_004_enable = ko.observable(true);
                    let self = this;
                    self.screenModel = screenModel;
                    //set combobox data
                    //001
                    self.ComboBoxItemList_B_001 = ko.observableArray([
                        new ComboboxItemModel(1, '全件'),
                        new ComboboxItemModel(2, '支給項目'),
                        new ComboboxItemModel(3, '控除項目'),
                        new ComboboxItemModel(4, '勤怠項目'),
                        new ComboboxItemModel(5, '記事項目'),
                        new ComboboxItemModel(6, 'その他項目')
                    ]);
                    self.selectedCode_B_001.subscribe(function (newValue) {
                        let categoryAtr;
                        switch (newValue) {
                            case 1:
                                //select  all  
                                categoryAtr = -1;
                                break;
                            case 2:
                                // 支給
                                categoryAtr = 0;
                                break;
                            case 3:
                                // 控除
                                categoryAtr = 1;
                                break;
                            case 4:
                                // 勤怠
                                categoryAtr = 2;
                                break;
                            case 5:
                                //記事
                                categoryAtr = 3;
                                break;
                            case 6:
                                //その他
                                categoryAtr = 9;
                                break;
                        }
                        self.categoryAtr = categoryAtr;
                        //then load gridlist
                        self.LoadGridList();
                    });
                    self.checked_B_002.subscribe(function () {
                        self.LoadGridList();
                    });
                    // set gridlist data
                    //gridlist column
                    self.GridColumns_B_001 = ko.observableArray([
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
                    self.GridlistCurrentCode_B_001.subscribe(function (newValue) {
                        //get ItemModel by itemCode has selected
                        var item = _.find(self.GridlistItems_B_001(), function (ItemModel) {
                            return ItemModel.itemCode == newValue;
                        });
                        //set it to GridlistCurrentItem_B_001 for trigger subscribe
                        self.GridlistCurrentItem_B_001(item);
                        //set text for B_INP_002
                        self.B_INP_002_text(newValue);
                    });
                    self.GridlistCurrentItem_B_001.subscribe(function (itemModel) {
                        self.GridCurrentItemName_B_001(itemModel ? itemModel.itemName : '');
                        self.GridCurrentUniteCode_B_001(itemModel ? itemModel.uniteCode : '');
                        self.GridCurrentCategoryAtr_B_001(itemModel ? itemModel.categoryAtr : 0);
                        // If set same group, GridCurrentCategoryAtr_B_001 will not trigger subscribe => can't call ChangeGroup function for load that item data so call ChangeGroup function here
                        ChangeGroup(self.GridCurrentCategoryAtr_B_001());
                        self.GridCurrentCodeAndName_B_001(itemModel ? itemModel.itemCode + ' ' + itemModel.itemName : '');
                        self.GridCurrentDisplaySet_B_001(itemModel ? itemModel.displaySet == 1 ? true : false : false);
                        self.GridCurrentItemAbName_B_001(itemModel ? itemModel.itemAbName : '');
                        self.GridCurrentCategoryAtrName_B_001(itemModel ? itemModel.categoryAtrName : '');
                        //when itemModel != undefined , need disable INP_002
                        if (itemModel != undefined) {
                            self.enable_B_INP_002(false);
                        }
                    });
                    self.GridCurrentCategoryAtr_B_001.subscribe(function (newValue) {
                        //when change to different group, need  call ChangeGroup function for Set Value to layout
                        ChangeGroup(newValue);
                    });
                    function ChangeGroup(newValue) {
                        $('#screenC').hide();
                        $('#screenD').hide();
                        $('#screenE').hide();
                        $('#screenF').hide();
                        switch (newValue) {
                            case 0:
                                //支給
                                $('#screenC').show();
                                self.screenModel.screenModelC.CurrentItemMaster(self.GridlistCurrentItem_B_001());
                                break;
                            case 1:
                                //控除
                                $('#screenD').show();
                                self.screenModel.screenModelD.CurrentItemMaster(self.GridlistCurrentItem_B_001());
                                break;
                            case 2:
                                //勤怠
                                $('#screenE').show();
                                self.screenModel.screenModelE.CurrentItemMaster(self.GridlistCurrentItem_B_001());
                                break;
                            case 3:
                                //記事
                                $('#screenF').show();
                                self.screenModel.screenModelF.CurrentItemMaster(self.GridlistCurrentItem_B_001());
                                break;
                        }
                        //why don't have その他 ? because it show nothing
                    }
                    //first load , need call LoadGridList
                    self.LoadGridList();
                    self.enable_B_INP_002.subscribe(function (newValue) {
                        if (newValue) {
                            //it mean new item mode
                            self.setNewItemMode();
                        }
                        else {
                            //it mean update item mode
                            self.setUpdateItemMode();
                        }
                    });
                    //set text editer data
                    //INP_002
                    self.texteditor_B_INP_002 = {
                        value: self.B_INP_002_text,
                        option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                            textmode: "text",
                            placeholder: "",
                            textalign: "left"
                        })),
                        enable: self.enable_B_INP_002
                    };
                    //INP_003
                    self.texteditor_B_INP_003 = {
                        value: self.GridCurrentItemName_B_001,
                        option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                            textmode: "text",
                            placeholder: "",
                            textalign: "left"
                        }))
                    };
                    //INP_004
                    self.texteditor_B_INP_004 = {
                        value: self.GridCurrentItemAbName_B_001,
                        option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                            textmode: "text",
                            placeholder: "",
                            textalign: "left"
                        }))
                    };
                }
                setNewItemMode() {
                    let self = this;
                    //set selected code is '' for trigger subscribe
                    self.GridlistCurrentCode_B_001('');
                    //disable delete button
                    self.B_BTN_004_enable(false);
                    //disable 有効期間設定 button
                    self.screenModel.screenModelC.C_BTN_001_enable(false);
                    self.screenModel.screenModelC.C_BTN_002_enable(false);
                    self.screenModel.screenModelD.D_BTN_001_enable(false);
                    self.screenModel.screenModelD.D_BTN_002_enable(false);
                }
                setUpdateItemMode() {
                    let self = this;
                    //if from new mode change to update mode , need clear all ntsError 
                    $('#B_INP_002').ntsError('clear');
                    //enable delete button
                    self.B_BTN_004_enable(true);
                    //enable 有効期間設定 button
                    self.screenModel.screenModelC.C_BTN_001_enable(true);
                    self.screenModel.screenModelC.C_BTN_002_enable(true);
                    self.screenModel.screenModelD.D_BTN_001_enable(true);
                    self.screenModel.screenModelD.D_BTN_002_enable(true);
                }
                GetCurrentItemMaster() {
                    //get item master The user entered on the form 
                    let self = this;
                    //this is item master Constructor
                    let itemMaster = new b.service.model.ItemMaster(self.B_INP_002_text(), self.GridCurrentItemName_B_001(), self.GridCurrentCategoryAtr_B_001(), self.GridCurrentCategoryAtrName_B_001(), self.GridCurrentItemAbName_B_001(), self.GridlistCurrentItem_B_001() ? self.GridlistCurrentItem_B_001().itemAbNameO : self.GridCurrentItemAbName_B_001(), self.GridlistCurrentItem_B_001() ? self.GridlistCurrentItem_B_001().itemAbNameE : self.GridCurrentItemAbName_B_001(), self.GridCurrentDisplaySet_B_001() == true ? 1 : 0, self.GridCurrentUniteCode_B_001(), self.getCurrentZeroDisplaySet(), self.getCurrentItemDisplayAtr(), 1);
                    //set sub item constructor
                    itemMaster.itemSalary = self.screenModel.screenModelC.GetCurrentItemSalary();
                    itemMaster.itemDeduct = self.screenModel.screenModelD.GetCurrentItemDeduct();
                    itemMaster.itemAttend = self.screenModel.screenModelE.getCurrentItemAttend();
                    return itemMaster;
                }
                LoadGridList(ItemCode) {
                    let self = this;
                    let categoryAtr = self.categoryAtr;
                    //load dispSet 
                    //if 0  mean
                    // no view この項目名を廃止する 
                    //else view all
                    let dispSet = self.checked_B_002() ? -1 : 0;
                    //call service load findAllItemMaster
                    b.service.findAllItemMaster(categoryAtr, dispSet).done(function (MasterItems) {
                        self.GridlistItems_B_001(MasterItems);
                        //set selected first item in list
                        if (self.GridlistItems_B_001().length > 0)
                            // if not itemcode parameter
                            if (!ItemCode)
                                //set GridlistCurrentCode_B_001 selected first item in gridlist
                                self.GridlistCurrentCode_B_001(self.GridlistItems_B_001()[0].itemCode);
                            else
                                //set  selected == param itemcode
                                self.GridlistCurrentCode_B_001(ItemCode);
                    }).fail(function (res) {
                        alert(res);
                    });
                }
                deleteItem() {
                    let self = this;
                    let ItemMaster = self.GetCurrentItemMaster();
                    let index = self.GridlistItems_B_001.indexOf(self.GridlistCurrentItem_B_001());
                    //if has item selected
                    if (index >= 0) {
                        //show dialog
                        nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function () {
                            //if yes call service delete item
                            b.service.deleteItemMaster(ItemMaster).done(function (any) {
                                //reload grid and set select code after delete item success
                                let selectItemCode;
                                //if after delete gridlist length >0
                                if (self.GridlistItems_B_001().length - 1 > 1) {
                                    if (index < self.GridlistItems_B_001().length - 1)
                                        //if not last selected item , set selected same position
                                        selectItemCode = self.GridlistItems_B_001()[index - 1].itemCode;
                                    else
                                        //else selected item Before it
                                        selectItemCode = self.GridlistItems_B_001()[index - 2].itemCode;
                                }
                                else
                                    //length < 0 no select any thing
                                    selectItemCode = '';
                                //reload gruid list
                                self.LoadGridList(selectItemCode);
                            }).fail(function (res) {
                                alert(res);
                            });
                        });
                    }
                }
                getCurrentZeroDisplaySet() {
                    let Result;
                    let self = this;
                    let CurrentGroup = self.GridCurrentCategoryAtr_B_001();
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
                }
                getCurrentItemDisplayAtr() {
                    //like getCurrentZeroDisplaySet
                    let Result;
                    let self = this;
                    let CurrentGroup = self.GridCurrentCategoryAtr_B_001();
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
                }
                openADialog() {
                    //open select type dialog for new item
                    let self = this;
                    nts.uk.ui.windows.sub.modal('../a/index.xhtml', { height: 480, width: 630, dialogClass: "no-close" }).onClosed(function () {
                        if (nts.uk.ui.windows.getShared('groupCode') != undefined) {
                            //get group from session
                            let groupCode = Number(nts.uk.ui.windows.getShared('groupCode'));
                            //set layout for new.
                            self.enable_B_INP_002(true);
                            self.GridCurrentCategoryAtr_B_001(groupCode);
                        }
                    });
                }
                submitData() {
                    let self = this;
                    let ItemMaster = self.GetCurrentItemMaster();
                    //if self.enable_B_INP_002 == true is mean New mode
                    if (self.enable_B_INP_002()) {
                        self.addNewItemMaster(ItemMaster);
                    }
                    else {
                        //else update mode
                        self.updateItemMaster(ItemMaster);
                    }
                }
                addNewItemMaster(ItemMaster) {
                    let self = this;
                    //call add service
                    b.service.addItemMaster(ItemMaster).done(function (any) {
                        //after add , reload grid list
                        self.LoadGridList(ItemMaster.itemCode);
                    }).fail(function (res) {
                        alert(res);
                    });
                }
                updateItemMaster(ItemMaster) {
                    let self = this;
                    //call update service
                    b.service.updateItemMaster(ItemMaster).done(function (any) {
                        //after add , reload grid list
                        self.LoadGridList(ItemMaster.itemCode);
                    }).fail(function (res) {
                        alert(res);
                    });
                }
                openJDialog() {
                    let self = this;
                    nts.uk.ui.windows.sub.modal('../j/index.xhtml', { height: 700, width: 970, dialogClass: "no-close" }).onClosed(function () {
                    });
                }
            }
            viewmodel.ScreenModel = ScreenModel;
            class ComboboxItemModel {
                constructor(code, name) {
                    this.code = code;
                    this.name = name;
                }
            }
        })(viewmodel = b.viewmodel || (b.viewmodel = {}));
    })(b = qmm012.b || (qmm012.b = {}));
})(qmm012 || (qmm012 = {}));
