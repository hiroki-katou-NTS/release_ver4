module qmm012.b.viewmodel {
    export class ScreenModel {
        enable: KnockoutObservable<boolean> = ko.observable(true);
        //combobox
        //001
        B_Sel_ItemClassification: KnockoutObservableArray<ComboboxItemModel>;
        B_Selected_ItemClassification: KnockoutObservable<number> = ko.observable(1);
        //gridlist
        GridlistItems: KnockoutObservableArray<service.model.ItemMaster> = ko.observableArray([]);
        GridColumns: KnockoutObservableArray<any>;
        GridlistCurrentCode: KnockoutObservable<any> = ko.observable();
        GridlistCurrentItem: KnockoutObservable<service.model.ItemMaster> = ko.observable(null);
        GridCurrentItemAbName: KnockoutObservable<string> = ko.observable('');
        GridCurrentItemName: KnockoutObservable<string> = ko.observable('');
        GridCurrentDisplaySet: KnockoutObservable<boolean> = ko.observable(false);
        GridCurrentUniteCode: KnockoutObservable<string> = ko.observable('');
        GridCurrentCategoryAtr: KnockoutObservable<number> = ko.observable(0);
        GridCurrentCategoryAtrName: KnockoutObservable<string> = ko.observable('');
        B_Inp_Code_text: KnockoutObservable<string> = ko.observable('');
        categoryAtr: number = -1;
        //Checkbox
        //B_002
        Checked_AlsoDisplayAbolition: KnockoutObservable<boolean> = ko.observable(false);
        //combobox
        //text editer
        texteditor_B_Inp_Code: any;
        texteditor_B_Inp_Name: any;
        texteditor_B_Inp_AbbreviatedName: any;
        enable_B_Inp_Code: KnockoutObservable<boolean> = ko.observable(false);
        screenModel: qmm012.b.ScreenModel;
        B_Btn_DeleteButton_enable: KnockoutObservable<boolean> = ko.observable(true);
        dirty: nts.uk.ui.DirtyChecker;
        dirtyItemMaster: KnockoutObservable<service.model.ItemMaster> = ko.observable(null);
        dirtyOldValue: DirtyValue = new DirtyValue(1, '', false);
        constructor(screenModel: qmm012.b.ScreenModel) {
            let self = this;
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
            self.B_Selected_ItemClassification.subscribe(function(newValue) {
                if (self.dirtyOldValue.selItemClassification != newValue) {
                    self.dirtyItemMaster(self.getCurrentItemMaster());
                    let categoryAtr;
                    switch (newValue) {
                        case 1:
                            //select  all  
                            categoryAtr = -1
                            break;
                        case 2:
                            // 支給
                            categoryAtr = 0
                            break;
                        case 3:
                            // 控除
                            categoryAtr = 1
                            break;
                        case 4:
                            // 勤怠
                            categoryAtr = 2
                            break;
                        case 5:
                            //記事
                            categoryAtr = 3
                            break;
                        case 6:
                            //その他
                            categoryAtr = 9
                            break;
                    }
                    self.activeDirty(function() {
                        self.dirtyOldValue.selItemClassification = newValue;
                        self.categoryAtr = categoryAtr;
                        //then load gridlist
                        self.loadGridList();
                    }, function() {
                        self.dirtyOldValue.selItemClassification = newValue;
                        self.categoryAtr = categoryAtr;
                        self.GridlistCurrentItem(self.GridlistCurrentItem());
                        //then load gridlist
                        self.loadGridList();
                    }, function() {
                        self.B_Selected_ItemClassification(self.dirtyOldValue.selItemClassification);
                    });

                }
            });
            self.Checked_AlsoDisplayAbolition.subscribe(function(newValue) {
                if (self.dirtyOldValue.selAlsoDisplayAbolition != newValue) {
                    self.activeDirty(
                        function() {
                            self.dirtyOldValue.selAlsoDisplayAbolition = newValue;
                            self.loadGridList()
                        },
                        function() {
                            self.dirtyOldValue.selAlsoDisplayAbolition = newValue;
                            //then load gridlist
                            self.loadGridList();
                        }, function() {
                            self.Checked_AlsoDisplayAbolition(self.dirtyOldValue.selAlsoDisplayAbolition);
                        }
                    );

                }
            })
            // set gridlist data
            //gridlist column
            self.GridColumns = ko.observableArray([
                { headerText: '項目区分', prop: 'categoryAtrName', width: 80 },
                { headerText: 'コード', prop: 'itemCode', width: 50 },
                { headerText: '名称', prop: 'itemName', width: 150 },
                { headerText: '廃止', prop: 'displaySet', width: 20, formatter: makeIcon },
                { headerText: '廃止', prop: 'itemKey', width: 20, hidden: true }
            ]);

            function makeIcon(val) {
                if (val == 1)
                    //it  mean この項目名を廃止する , bind X icon
                    return "<div class = 'NoIcon' > </div>";
                return "";
            }
            self.GridlistCurrentCode.subscribe(function(newValue) {
                var item = _.find(self.GridlistItems(), function(ItemModel: service.model.ItemMaster) {
                    return ItemModel.itemKey == newValue;
                });
                if (newValue != self.dirtyOldValue.lstCode) {
                    self.activeDirty(function() {
                        self.GridlistCurrentItem(item);
                    }, function() {
                        self.GridlistCurrentItem(item);
                    }, function() {
                        self.GridlistCurrentCode(self.dirtyOldValue.lstCode);
                    });
                }
            });

            self.GridlistCurrentItem.subscribe(function(itemModel: service.model.ItemMaster) {
                self.clearAllValidateError();
                self.GridCurrentItemName(itemModel ? itemModel.itemName : '');
                self.GridCurrentUniteCode(itemModel ? itemModel.uniteCode : '');
                //set text for B_Inp_Code
                self.B_Inp_Code_text(itemModel ? itemModel.itemCode : '');
                self.GridCurrentDisplaySet(itemModel ? itemModel.displaySet == 1 ? true : false : false);
                self.GridCurrentItemAbName(itemModel ? itemModel.itemAbName : '');
                self.GridCurrentCategoryAtrName(itemModel ? itemModel.categoryAtrName : '');
                //when itemModel != undefined , need disable INP_002
                if (itemModel ? itemModel.itemCode != '' : false) {
                    self.enable_B_Inp_Code(false);
                    self.GridCurrentCategoryAtr(itemModel.categoryAtr);
                }
                self.ChangeGroup(self.GridCurrentCategoryAtr()).done(function() {
                    self.dirtyItemMaster(self.getCurrentItemMaster());
                    if (self.dirty)
                        self.dirty.reset();
                    self.dirtyOldValue.lstCode = self.GridlistCurrentCode();
                });
            });
            //first load , need call loadGridList
            self.loadGridList();
            self.enable_B_Inp_Code.subscribe(function(newValue) {
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
            self.B_Inp_Code_text.subscribe(function(newValue) {
                if (self.enable_B_Inp_Code()) {
                    if (isNaN(parseInt(newValue))) {
                        $('#B_Inp_Code').ntsError('set', 'is not number');//i don't know message
                    }
                }
            })

        }
        ChangeGroup(newValue): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            let self = this;
            $('#screenC').hide();
            $('#screenD').hide();
            $('#screenE').hide();
            $('#screenF').hide();
            switch (newValue) {
                case 0:
                    //支給
                    $('#screenC').show();
                    self.screenModel.screenModelC.loadData(self.GridlistCurrentItem()).done(function() {
                        dfd.resolve();
                    });
                    break;
                case 1:
                    //控除
                    $('#screenD').show();
                    self.screenModel.screenModelD.loadData(self.GridlistCurrentItem()).done(function() {
                        dfd.resolve();
                    });
                    break;
                case 2:
                    //勤怠
                    $('#screenE').show();
                    self.screenModel.screenModelE.loadData(self.GridlistCurrentItem()).done(function() {
                        dfd.resolve();
                    });
                    break;
                case 3:
                    //記事
                    $('#screenF').show();
                    self.screenModel.screenModelF.loadData(self.GridlistCurrentItem()).done(function() {
                        dfd.resolve();
                    });
                    break;
                case 9:
                    dfd.resolve();
                    break;
            }
            return dfd.promise();
            //why don't have その他 ? because it show nothing
        }
        clearAllValidateError() {
            $('.save-error').ntsError('clear');
        }
        reload() {
            var self = this;
            location.reload(true);
        }
        setNewItemMode() {
            let self = this;
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
        }
        clearContent() {
            let self = this;
            self.clearAllValidateError();
            self.GridCurrentItemName('');
            self.GridCurrentUniteCode('');
            //set text for B_Inp_Code
            self.B_Inp_Code_text('');
            self.GridCurrentDisplaySet(false);
            self.GridCurrentItemAbName('');
            self.GridCurrentCategoryAtrName('');
            //when itemModel != undefined , need disable INP_002
            self.enable_B_Inp_Code(true);
            self.GridlistCurrentItem(self.getCurrentItemMaster());
        }
        setUpdateItemMode() {
            let self = this;
            //if from new mode change to update mode , need clear all ntsError 
            $('#B_Inp_Code').ntsError('clear');
            //enable delete button
            self.B_Btn_DeleteButton_enable(true);
            //enable 有効期間設定 button
            self.screenModel.screenModelC.C_Btn_PeriodSetting_enable(true);
            self.screenModel.screenModelC.C_Btn_BreakdownSetting_enable(true);
            self.screenModel.screenModelD.D_Btn_PeriodSetting_enable(true);
            self.screenModel.screenModelD.D_Btn_BreakdownSetting_enable(true);
        }

        getCurrentItemMaster() {
            //get item master The user entered on the form 
            let self = this;
            //this is item master Constructor
            let itemMaster = new service.model.ItemMaster(
                self.B_Inp_Code_text(),
                self.GridCurrentItemName(),
                self.GridCurrentCategoryAtr(),
                self.GridCurrentCategoryAtrName(),
                self.GridCurrentItemAbName(),
                self.GridlistCurrentItem() ? self.GridlistCurrentItem().itemAbNameO : "",
                self.GridlistCurrentItem() ? self.GridlistCurrentItem().itemAbNameE : "",
                self.GridCurrentDisplaySet() == true ? 1 : 0,
                self.GridCurrentUniteCode(),
                self.getCurrentZeroDisplaySet(),
                self.getCurrentItemDisplayAtr(),
                1
            )
            //set sub item constructor
            itemMaster.itemSalary = self.screenModel.screenModelC.GetCurrentItemSalary();
            itemMaster.itemDeduct = self.screenModel.screenModelD.GetCurrentItemDeduct();            itemMaster.itemAttend = self.screenModel.screenModelE.getCurrentItemAttend();
            return itemMaster;
        }
        validateItemMaster() {
            let self = this;
            $("#B_Inp_Code").ntsEditor("validate");
            $("#B_Inp_Name").ntsEditor("validate");
            $("#B_Inp_AbbreviatedName").ntsEditor("validate");
            $("#C_Inp_LimitAmount").ntsEditor("validate");
            if ($('.nts-editor').ntsError("hasError")) {
                return true;
            }
            return false;
        }
        loadGridList(ItemKey?) {
            let self = this;
            let categoryAtr = self.categoryAtr;
            //load dispSet 
            //if 0  mean
            // no only view この項目名を廃止する 
            //else view all
            let dispSet = self.Checked_AlsoDisplayAbolition() ? -1 : 0;
            //call service load findAllItemMaster
            service.findAllItemMaster(categoryAtr, dispSet).done(function(MasterItems: Array<service.model.ItemMaster>) {
                self.GridlistItems(MasterItems);
                //set selected first item in list
                if (self.dirty)
                    self.dirty.reset();
                if (self.GridlistItems().length > 0) {
                    // if not ItemKey parameter
                    if (!ItemKey) {
                        //set GridlistCurrentCode selected first item in gridlist
                        self.GridlistCurrentItem(self.GridlistItems()[0]);
                        self.dirtyOldValue.lstCode = self.GridlistItems()[0].itemKey;
                        self.GridlistCurrentCode(self.GridlistItems()[0].itemKey);
                    }
                    else {
                        //set  selected == param ItemKey
                        var item = _.find(self.GridlistItems(), function(ItemModel: service.model.ItemMaster) {
                            return ItemModel.itemKey == ItemKey;
                        });
                        if (item) {
                            self.GridlistCurrentItem(item);
                            self.dirtyOldValue.lstCode = ItemKey;
                            self.GridlistCurrentCode(ItemKey);
                        }
                        else {
                            self.GridlistCurrentItem(self.GridlistItems()[0]);
                            self.dirtyOldValue.lstCode = self.GridlistItems()[0].itemKey;
                            self.GridlistCurrentCode(self.GridlistItems()[0].itemKey);
                        }
                    }
                    self.dirtyItemMaster(self.getCurrentItemMaster());
                    self.dirty = new nts.uk.ui.DirtyChecker(self.dirtyItemMaster);
                } else {
                    //if no item, show message set new mode
                    nts.uk.ui.dialog.alert("対象データがありません。");
                    self.setNewItemMode();
                }
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res);
            });
        }

        deleteItem() {
            let self = this;
            let ItemMaster = self.getCurrentItemMaster();
            let index = self.GridlistItems.indexOf(self.GridlistCurrentItem());
            //if has item selected
            if (index >= 0) {
                //show dialog
                nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                    //if yes call service delete item
                    service.deleteItemMaster(ItemMaster).done(function(any) {
                        //reload grid and set select code after delete item success
                        let selectItemKey;
                        //if after delete gridlist length >0
                        if (self.GridlistItems().length - 1 > 1) {
                            if (index < self.GridlistItems().length - 1)
                                //if not last selected item , set selected same position
                                selectItemKey = self.GridlistItems()[index + 1].itemKey;
                            else
                                //else selected item Before it
                                selectItemKey = self.GridlistItems()[index - 1].itemKey;
                        } else
                            //length < 0 no select any thing
                            selectItemKey = '';
                        //reload gruid list
                        self.loadGridList(selectItemKey);
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alert(res);
                    });
                });
            }
        }
        getCurrentZeroDisplaySet() {
            let Result;
            let self = this;
            let CurrentGroup = self.GridCurrentCategoryAtr();
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
            let CurrentGroup = self.GridCurrentCategoryAtr();
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
            let self = this;
            nts.uk.ui.windows.sub.modal('../a/index.xhtml', { height: 480, width: 630, dialogClass: "no-close", title: "項目名の登録＞項目区分の選択" }).onClosed(function(): any {
                if (nts.uk.ui.windows.getShared('groupCode') != undefined) {
                    //get group from session
                    let groupCode = Number(nts.uk.ui.windows.getShared('groupCode'));
                    //set layout for new.
                    self.GridCurrentCategoryAtr(groupCode);
                    self.setNewItemMode();
                }
            });
        }

        copyButtonClick() {
            let self = this;
            self.activeDirty(function() { });
        }

        exportExcelButtonClick() {
            let self = this;
            self.activeDirty(function() { });
        }
        submitData() {
            let self = this;
            if (!self.validateItemMaster()) {
                let ItemMaster = self.getCurrentItemMaster();
                //if self.enable_B_Inp_Code == true is mean New mode
                if (self.enable_B_Inp_Code()) {
                    self.addNewItemMaster(ItemMaster);
                } else {
                    //else update mode
                    self.updateItemMaster(ItemMaster);
                }
            }
        }

        addNewItemMaster(itemMaster: service.model.ItemMaster) {
            let self = this;
            //check before call services
            if (isNaN(parseInt(self.B_Inp_Code_text()))) {
                $('#B_Inp_Code').ntsError('set', 'is not number');//i don't know message
            }
            else {
                service.findItemMaster(itemMaster.categoryAtr, itemMaster.itemCode).done(function(itemMasterRes: service.model.ItemMaster) {
                    if (itemMasterRes != undefined) {
                        $('#B_Inp_Code').ntsError('set', '入力したコードは既に存在しています。');
                    } else {
                        service.addItemMaster(itemMaster).done(function(any) {
                            //after add , reload grid list
                            self.loadGridList(itemMaster.itemKey);
                        }).fail(function(res) {
                            nts.uk.ui.dialog.alert(res);
                        });
                    }
                });
            }

        }

        updateItemMaster(ItemMaster: service.model.ItemMaster) {
            let self = this;
            //call update service
            service.updateItemMaster(ItemMaster).done(function(any) {
                //after add , reload grid list
                self.loadGridList(ItemMaster.itemKey);
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res);
            });
        }
        registerPrintingNameButtonClick() {
            let self = this;
            self.activeDirty(function() { self.openJDialog() }, function() { self.openJDialog() });
        }
        addNewButtonClick() {
            let self = this;
            //set value client has input on form 
            self.activeDirty(function() { self.openADialog() }, function() { self.openADialog() });
        }
        openJDialog() {
            let self = this;
            nts.uk.ui.windows.sub.modal('../j/index.xhtml', {
                height: 700, width: 970, dialogClass: "no-close", title: "印字名称の登録"
            }).onClosed(function(): any {
                self.loadGridList(self.GridlistCurrentCode());
            });
        }
        activeDirty(MainFunction, YesFunction?, NoFunction?) {
            let self = this;
            self.dirtyItemMaster(self.getCurrentItemMaster());
            //    if (self.dirty ? !self.dirty.isDirty() : true) {
            MainFunction();
            //    } else {
            //    nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。  ").ifYes(function() {
            //reset data on form when not save 
            //      self.GridlistCurrentItem(self.GridlistCurrentItem());
            //      if (YesFunction)
            //      YesFunction();
            //    }).ifNo(function() {
            //       if (NoFunction)
            //           NoFunction();
            //   })
            //   }
        }
    }
    class DirtyValue {
        selItemClassification: number;
        lstCode: string;
        selAlsoDisplayAbolition: boolean;
        constructor(selItemClassification: number,
            lstCode: string, selAlsoDisplayAbolition: boolean) {
            this.selItemClassification = selItemClassification;
            this.lstCode = lstCode;
            this.selAlsoDisplayAbolition = selAlsoDisplayAbolition;
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