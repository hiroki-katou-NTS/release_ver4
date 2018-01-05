module nts.uk.com.view.cps017.a.viewmodel {
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import textUK = nts.uk.text;
    import block = nts.uk.ui.block;
    export class ScreenModel {
        //listSelectionItem
        listItems: KnockoutObservableArray<ISelectionItem> = ko.observableArray([]);
        perInfoSelectionItem: KnockoutObservable<SelectionItem> = ko.observable(new SelectionItem({ selectionItemId: '', selectionItemName: '' }));

        // history:
        listHistorySelection: KnockoutObservableArray<IHistorySelection> = ko.observableArray([]);
        historySelection: KnockoutObservable<HistorySelection> = ko.observable(new HistorySelection({ histId: '', selectionItemId: '' }));

        //Selection:
        listSelection: KnockoutObservableArray<ISelection> = ko.observableArray([]);
        selection: KnockoutObservable<Selection> = ko.observable(new Selection({ selectionID: '', histId: '' }));

        //OrderSelection:
        listOrderSelection: KnockoutObservableArray<IOrderSelection> = ko.observableArray([]);
        orderSelection: KnockoutObservable<OrderSelection> = ko.observable(new OrderSelection({ selectionID: '', histId: '' }));

        //Check insert/upadte
        checkCreate: KnockoutObservable<boolean>;
        checkCreateaaa: KnockoutObservable<boolean>;
        closeUp: KnockoutObservable<boolean> = ko.observable(false);
        isDialog: KnockoutObservable<boolean> = ko.observable(false);
        //hoatt
        selHistId: KnockoutObservable<string> = ko.observable('');
        enableDelHist: KnockoutObservable<boolean> = ko.observable(false);
        enableSelName: KnockoutObservable<boolean> = ko.observable(true);
        enDisDelSelec: KnockoutObservable<boolean> = ko.observable(false);
        revDisSel01: KnockoutObservable<boolean> = ko.observable(false);
        revDisSel02: KnockoutObservable<boolean> = ko.observable(false);
        revDisSel03: KnockoutObservable<boolean> = ko.observable(false);
        revDisSel04: KnockoutObservable<boolean> = ko.observable(false);
        disbleAdUpHist: KnockoutObservable<boolean> = ko.observable(true);
        constraints: KnockoutObservable<any> = ko.observable();

        constructor() {
            let self = this,
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem(),
                listItems: Array<SelectionItem> = self.listItems(),
                historySelection: HistorySelection = self.historySelection(),
                listHistorySelection: Array<HistorySelection> = self.listHistorySelection(),
                selection: Selection = self.selection();

            //check insert/update
            self.checkCreate = ko.observable(true);
            self.checkCreateaaa = ko.observable(true);

            //Subscribe: 項目変更→項目のID変更
            perInfoSelectionItem.selectionItemId.subscribe(x => {
                if (x) {

                    let selectedObject: ISelectionItem = _.find(self.listItems(), (item) => {
                        return item.selectionItemId == x;
                    });

                    if (selectedObject != undefined) {
                        perInfoSelectionItem.selectionItemName(selectedObject.selectionItemName);
                        perInfoSelectionItem.selectionCodeCharacter(selectedObject.formatSelection.selectionCodeCharacter);
                        
                        self.constraints.selectionCode = selectedObject.formatSelection.selectionCode;
                        self.constraints.selectionName = selectedObject.formatSelection.selectionName;
                        self.constraints.selectionExternalCode = selectedObject.formatSelection.selectionExternalCode;
                        //self.perInfoSelectionItem().selectionItemId(self.listItems()[0].selectionItemId);
                    }
                    //history
                    service.getAllPerInfoHistorySelection(x).done((_selectionItemList: IHistorySelection) => {
                        let changeData: Array<IHistorySelection> = _.each(_selectionItemList, (item) => {
                            item.displayDate = item.startDate + "  " + getText('CPS017_12') + "  " + item.endDate;
                            return item;
                        });

                        self.listHistorySelection(changeData);
                        //self.historySelection().histId(self.listHistorySelection().length == 0 ? '' : self.listHistorySelection()[0].histId);
                        if (self.listHistorySelection().length == 0 || self.listHistorySelection() == undefined) {
                            self.disbleAdUpHist(false);
                            self.enableDelHist(false);
                            self.enableSelName(false);
                            self.revDisSel02(false);
                            self.revDisSel01(false);
                            self.revDisSel03(false);
                            self.revDisSel04(false);

                            historySelection.histId(undefined);
                            nts.uk.ui.errors.clearAll();
                            self.selection().selectionID('');
                            self.selection().externalCD('');
                            self.selection().selectionCD('');
                            self.selection().selectionName('');
                            self.selection().memoSelection('');
                        } else {
                            self.historySelection().histId(self.listHistorySelection()[0].histId);
                        }
                    });
                } else {
                    //historySelection.histId(undefined);
                    self.registerData();
                }
            });

            //sub theo historyID:
            historySelection.histId.subscribe(x => {
                if (x) {
                    let histCur = _.find(self.listHistorySelection(), a => a.histId == x);
                    if (histCur != undefined) {
                        if (histCur.endDate !== '9999/12/31' || self.listHistorySelection().length <= 1) {
                            self.enableDelHist(false);
                            self.revDisSel02(false);
                            self.revDisSel01(false);
                            self.revDisSel03(false);
                            self.revDisSel04(false);
                            self.enableSelName(false);
                        } else {
                            self.enableDelHist(true);
                            self.revDisSel01(true);
                            self.revDisSel02(true);
                            self.revDisSel03(true);
                            self.revDisSel04(true);
                            self.enableSelName(true);
                            self.registerData();
                        }
                    }

                    let adUpHist = _.find(self.listHistorySelection(), a => a.histId == x);
                    if (adUpHist != undefined) {
                        if (adUpHist.endDate !== '9999/12/31' || self.listHistorySelection().length == 0) {
                            self.disbleAdUpHist(false);
                            self.revDisSel02(false);
                            self.revDisSel01(false);
                            self.revDisSel03(false);
                            self.revDisSel04(false);
                        } else {
                            self.disbleAdUpHist(true);
                            self.revDisSel01(true);
                            self.revDisSel02(true);
                            self.revDisSel03(true);
                            self.revDisSel04(true);
                        }
                    }

                    let ondeHisIdlits = _.find(self.listHistorySelection(), a => a.histId == x);
                    if (ondeHisIdlits != undefined) {
                        if (ondeHisIdlits.endDate == '9999/12/31') {
                            self.enableSelName(true);
                            self.registerData();
                        } else {
                            self.enableSelName(false);
                        }
                    }


                    self.listSelection.removeAll();
                    service.getAllOrderItemSelection(x).done((itemList: Array<ISelection>) => {                        if (itemList && itemList.length > 0) {
                            self.checkCreateaaa(false);
                            //self.enableSelName(true);
                            //self.revDisSel02(true);

                            // fix responsive bug
                            ko.utils.arrayPushAll(self.listSelection, itemList);
                            //itemList.forEach(x => self.listSelection.push(x));

                            self.selection().selectionID(self.listSelection()[0].selectionID);
                        } else {
                            //self.enableSelName(true);
                            self.revDisSel02(false);
                            self.revDisSel03(false);
                            //self.registerData();
                            //$("#code").focus();
                        }

                        self.listSelection.valueHasMutated();

                    });
                } else {
                    self.listSelection.removeAll();
                    self.registerData();
                }
            });

            // sub theo selectionID: 
            selection.selectionID.subscribe(x => {
                if (x) {
                    nts.uk.ui.errors.clearAll();
                    let selectLists = _.find(self.listSelection(), (item) => {
                        return item.selectionID == x;
                    });
                    selection.selectionCD(selectLists.selectionCD);
                    selection.selectionName(selectLists.selectionName);
                    selection.externalCD(selectLists.externalCD);
                    selection.memoSelection(selectLists.memoSelection);
                    $("#name").focus();
                } else {
                    self.registerData();
                }

            });

        }

        //開始
        start(): JQueryPromise<any> {
            let self = this,
                historySelection: HistorySelection = self.historySelection(),
                listHistorySelection: Array<HistorySelection> = self.listHistorySelection(),
                _selectId = _.find(listHistorySelection, x => x.selectionItemId == historySelection.selectionItemId),
                comand: HistorySelection = ko.toJS(historySelection),
                dfd = $.Deferred();

            nts.uk.ui.errors.clearAll();
            //xu ly dialog: 
            let param = getShared('CPS017_PARAMS');
            // ドメインモデル「個人情報の選択項目」をすべて取得する
            service.getAllSelectionItems().done((itemList: Array<ISelectionItem>) => {
                if (itemList && itemList.length > 0) {
                    self.checkCreate(true);
                    self.listItems(itemList);
                    if (param != null && param != undefined) {
                        self.isDialog(param.isDialog);
                        self.closeUp(true);
                        self.perInfoSelectionItem().selectionItemId(param.selectionItemId);
                    } else {
                        self.perInfoSelectionItem().selectionItemId(self.listItems()[0].selectionItemId);
                    }
                } else {
                    self.checkCreate(false);
                    alertError({ messageId: "Msg_455" });
                    //                    self.registerData();
                    self.enableSelName(false);
                    self.perInfoSelectionItem().selectionItemId(self.listItems()[0].selectionItemId);
                }
                dfd.resolve();
            }).fail(error => {
                alertError({ messageId: "Msg_455" });
            });
            //self.checkCreate(false);

            return dfd.promise();
        }

        //新規ボタン
        registerData() {
            let self = this,
                selection: Selection = self.selection();
            nts.uk.ui.errors.clearAll();
            selection.selectionID(undefined);
            selection.externalCD('');
            selection.selectionCD('');
            selection.selectionName('');
            selection.memoSelection('');
            self.checkCreateaaa(true);
            self.enableSelName(true);
            $("#code").focus();
        }

        //検証チェック 
        validate() {
            $(".nts-editor").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return false;
            }
            return true;
        }

        //登録ボタン
        addData() {
            let self = this;
            if (self.validate()) {
                if (self.checkCreateaaa() == true) {
                    self.add();
                } else {
                    self.update();
                }
            }
        }

        //新規モード
        add() {
            let self = this,
                currentItem: Selection = self.selection(),
                listSelection: Array<ISelection> = self.listSelection(),
                _selectionCD = _.find(listSelection, x => x.selectionCD == currentItem.selectionCD()),
                histId = self.historySelection().histId(),
                oldIds = listSelection.map(m => m.selectionID),
                histList: HistorySelection = self.historySelection(),
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem();
            if(!self.checkSelectionConstraints()) return;
            let oldIndex = _.find(listSelection, x => x.selectionID == currentItem.selectionID());

            currentItem.histId(self.historySelection().histId());
            let command = ko.toJS(currentItem);

            if (_selectionCD) {
                $('#code').ntsError('set', { messageId: "Msg_3" });

            } else {
                service.saveDataSelection(command).done(function() {
                    self.checkCreateaaa(false);
                    self.listSelection.removeAll();

                    service.getAllOrderItemSelection(histId)
                        .done((itemList: Array<ISelection>) => {
                            if (itemList && itemList.length) {
                                itemList.forEach(x => self.listSelection.push(x));
                                //
                                let itemSelected = _.find(itemList, item => _.indexOf(oldIds, item.selectionID) == -1);

                                if (itemSelected) {
                                    self.selection().selectionID(itemSelected.selectionID);
                                    self.revDisSel02(true);
                                    self.revDisSel03(true);
                                }

                                nts.uk.ui.dialog.alert({ messageId: "Msg_15" }).then(function() {
                                    if (itemList.length == 1) {
                                        nts.uk.ui.dialog.alert({ messageId: "Msg_530" });
                                    }
                                });

                            }

                        });
                    perInfoSelectionItem.selectionItemId.valueHasMutated();

                    self.listSelection.valueHasMutated();

                    $("#name").focus();
                });
            }

        }

        //更新モード
        update() {
            let self = this,
                currentItem: Selection = self.selection(),
                listSelection: Array<Selection> = self.listSelection(),
                _selectionCD = _.find(listSelection, x => x.selectionCD == currentItem.selectionCD());
            if(!self.checkSelectionConstraints()) return;
            currentItem.histId(self.historySelection().histId());
            let command = ko.toJS(currentItem);

            service.updateDataSelection(command).done(function() {
                self.checkCreateaaa(false);
                self.listSelection.removeAll();
                service.getAllOrderItemSelection(self.historySelection().histId()).done((itemList: Array<ISelection>) => {
                    if (itemList && itemList.length) {
                        itemList.forEach(x => self.listSelection.push(x));
                    }
                    let oldIndex = _.findIndex(itemList, x => x.selectionID == currentItem.selectionID());
                    let newItem = itemList[oldIndex];
                    currentItem.selectionID(newItem.selectionID);
                });
                nts.uk.ui.dialog.alert({ messageId: "Msg_15" });
                self.listSelection.valueHasMutated();

            });
        }

        //削除ボタン
        remove() {
            let self = this,
                items = ko.unwrap(self.listSelection),
                currentItem: Selection = self.selection(),
                listSelection: Array<Selection> = self.listSelection(),
                histList: HistorySelection = self.historySelection(),
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem();

            currentItem.histId(self.historySelection().histId());
            let command = ko.toJS(currentItem);
            let oldIndex = _.findIndex(listSelection, x => x.selectionID == currentItem.selectionID());
            let lastIndex = items.length - 1;

            if (items.length > 0) {
                confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.removeDataSelection(command).done(function() {
                        self.listSelection.removeAll();
                        service.getAllOrderItemSelection(self.historySelection().histId()).done((itemList: Array<ISelection>) => {
                            if (itemList && itemList.length) {
                                itemList.forEach(x => self.listSelection.push(x));
                                if (oldIndex == lastIndex) {
                                    oldIndex--;
                                }
                                let newItem = itemList[oldIndex];
                                currentItem.selectionID(newItem.selectionID);
                            } else {
                                self.registerData();
                                histList.histId.valueHasMutated();
                            }
                            //                            histList.histId.valueHasMutated();
                        });
                        self.listItems.valueHasMutated();
                        perInfoSelectionItem.selectionItemId.valueHasMutated();

                        nts.uk.ui.dialog.alert({ messageId: "Msg_16" });
                    });

                }).ifNo(() => {
                    self.listItems.valueHasMutated();
                    return;
                })
            } else {
                alertError({ messageId: "Msg_521" });
                self.registerDataSelectioItem();
            }
        }

        // 履歴削除をする
        removeHistory() {
            let self = this,
                items = ko.unwrap(self.listHistorySelection),
                currentItem: HistorySelection = self.historySelection(),
                listHistorySelection: Array<HistorySelection> = self.listHistorySelection(),
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem(),
                listItems: Array<SelectionItem> = self.listItems();

            currentItem.histId(self.historySelection().histId());
            let command = ko.toJS(currentItem);
            let oldIndex = _.findIndex(listHistorySelection, x => x.histId == currentItem.histId());
            let lastIndex = items.length - 1;

            if (items.length > 0) {
                confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.removeHistory(command).done(function() {
                        self.listHistorySelection.removeAll();
                        service.getAllPerInfoHistorySelection(self.historySelection().histId()).done((itemList: Array<IHistorySelection>) => {
                            if (itemList && itemList.length) {
                                itemList.forEach(x => self.listHistorySelection.push(x));
                                if (oldIndex == lastIndex) {
                                    oldIndex--;
                                }
                                let newItem = itemList[oldIndex];
                                currentItem.histId(newItem.histId);
                                self.listItems.valueHasMutated();
                            }
                        });
                        perInfoSelectionItem.selectionItemId.valueHasMutated();
                        nts.uk.ui.dialog.alert({ messageId: "Msg_16" });
                    });
                    $("#name").focus();
                }).ifNo(() => {
                    self.listItems.valueHasMutated();
                    return;
                })
            } else {
                alertError({ messageId: "Msg_521" });
                self.registerDataSelectioItem();
            }
        }

        // 選択肢未登録会社へ反映する
        ReflUnrComp() {
            let self = this,
                currentItem: IHistorySelection = ko.toJS(self.historySelection),
                listHistorySelection: Array<HistorySelection> = self.listHistorySelection(),
                selectHistory = _.find(listHistorySelection, x => x.histId == currentItem.histId),

                perInfoSelectionItem: ISelectionItem = ko.toJS(self.perInfoSelectionItem),
                listItems: Array<SelectionItem> = self.listItems(),
                selectionItemList = _.find(listItems, x => x.selectionItemId == perInfoSelectionItem.selectionItemId),
                selItemList: SelectionItem = self.perInfoSelectionItem();

            let command = ko.toJS(perInfoSelectionItem);

            confirm({ messageId: "Msg_532", messageParams: ["1"] }).ifYes(() => {
                service.reflUnrComp(command).done(function() {
                    self.listHistorySelection.removeAll();
                    service.getAllPerInfoHistorySelection(self.historySelection().histId()).done((itemList: Array<>) => {
                        if (itemList && itemList.length) {
                            itemList.forEach(x => self.listHistorySelection.push(x));
                        }
                    });
                    self.listItems.valueHasMutated();
                    selItemList.selectionItemId.valueHasMutated();
                    nts.uk.ui.dialog.alert({ messageId: "Msg_81" });
                });
            }).ifNo(() => {
                self.listItems.valueHasMutated();
                return;
            })
        }

        //ダイアログC画面
        openDialogB() {
            let self = this,
                hist: HistorySelection = self.historySelection(),
                currentItem: OrderSelection = self.orderSelection();

            setShared('selectedHisId', self.historySelection().histId());
            block.invisible();
            modal('/view/cps/017/b/index.xhtml', { title: '' }).onClosed(function(): any {

                hist.histId.valueHasMutated();

                block.clear();
            });
        }

        //ダイアログC画面
        openDialogC() {
            let self = this,
                currentItem: HistorySelection = self.historySelection(),
                listHistorySelection: Array<HistorySelection> = self.listHistorySelection(),
                selectHistory = _.find(listHistorySelection, x => x.histId == currentItem.histId()),
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem(),
                listItems: Array<SelectionItem> = self.listItems(),
                selectionItemNameList = _.find(listItems, x => x.selectionItemName == perInfoSelectionItem.selectionItemName());

            setShared('CPS017C_PARAMS', { selectHistory: selectHistory, name: selectionItemNameList.selectionItemName });
            block.invisible();
            modal('/view/cps/017/c/index.xhtml', { title: '' }).onClosed(function(): any {
                //reload lai History:
                perInfoSelectionItem.selectionItemId.valueHasMutated();
                block.clear();
            });
        }

        //ダイアログD画面
        openDialogD() {
            let self = this,
                selectHistory = _.find(self.listHistorySelection(), x => x.histId == self.historySelection().histId()),
                selectionItemNameList = _.find(self.listItems(), x => x.selectionItemName == self.perInfoSelectionItem().selectionItemName()),
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem(),
                listItems: Array<SelectionItem> = self.listItems();

            param = {
                sel_history: selectHistory,
                sel_name: selectionItemNameList.selectionItemName
            };
            setShared('CPS017D_PARAMS', param);
            block.invisible();
            modal('/view/cps/017/d/index.xhtml', { title: '' }).onClosed(function(): any {
                //reload lai History:
                perInfoSelectionItem.selectionItemId.valueHasMutated();
                block.clear();
            });
        }
        close() {
            nts.uk.ui.windows.close();
        }
        
        //check constraints from cps 016
        checkSelectionConstraints(){
            let self = this,
            selCD = self.selection().selectionCD(),
            selName = self.selection().selectionName(),
            exCd = self.selection().externalCD(),
            allValid = true;
            if(!self.constraints) return false;
            if(selCD.length != self.constraints.selectionCode){
                allValid = false;
                $('#code').ntsError('set', "Selection code length must equals " + self.constraints.selectionCode);
            }
            if(selName.length != self.constraints.selectionName){
                allValid = false;
                $('#name').ntsError('set',  "Selection code length must equals " + self.constraints.selectionName);
            }
            if(exCd.length != self.constraints.selectionExternalCode && exCd != ""){
                allValid = false;
                $('#exCode').ntsError('set',  "Selection code length must equals " + self.constraints.selectionExternalCode);
            }
            return allValid;
        }
    }

    //SelectionItem
    interface ISelectionItem {
        selectionItemId: string;
        selectionItemName: string;
        formatSelection: any;
    }

    class SelectionItem {
        selectionItemId: KnockoutObservable<string> = ko.observable('');
        selectionItemName: KnockoutObservable<string> = ko.observable('');
        selectionCodeCharacter: KnockoutObservable<number> = ko.observable(1);
        constructor(param: ISelectionItem) {
            let self = this;
            self.selectionItemId(param.selectionItemId || '');
            self.selectionItemName(param.selectionItemName || '');

        }
    }

    //history:
    interface IHistorySelection {
        histId?: string;
        selectionItemId?: string;
        companyId: string;
        startDate: string;
        endDate: string;
    }

    class HistorySelection {
        histId: KnockoutObservable<string> = ko.observable('');
        selectionItemId: KnockoutObservable<string> = ko.observable('');
        companyId: KnockoutObservable<string> = ko.observable('');
        startDate: KnockoutObservable<string> = ko.observable('');
        endDate: KnockoutObservable<string> = ko.observable('');

        constructor(param: IHistorySelection) {
            let self = this;
            self.histId(param.histId || '');
            self.selectionItemId(param.selectionItemId || '');
            self.companyId(param.companyId || '');
            self.startDate(param.startDate || '');
            self.endDate(param.endDate || '');
        }

    }

    //Selection
    interface ISelection {
        selectionID?: string;
        histId?: string;
        selectionCD: string;
        selectionName: string;
        externalCD: string;
        memoSelection: string;
        initSelection: number;
    }
    class Selection {
        selectionID: KnockoutObservable<string> = ko.observable('');
        histId: KnockoutObservable<string> = ko.observable('');
        selectionCD: KnockoutObservable<string> = ko.observable('');
        selectionName: KnockoutObservable<string> = ko.observable('');
        externalCD: KnockoutObservable<string> = ko.observable('');
        memoSelection: KnockoutObservable<string> = ko.observable('');
        initSelection: KnockoutObservable<number> = ko.observable();

        constructor(param: ISelection) {
            let self = this;
            self.selectionID(param.selectionID || '');
            self.histId(param.histId || '');
            self.selectionCD(param.selectionCD || '');
            self.selectionName(param.selectionName || '');
            self.externalCD(param.externalCD || '');
            self.memoSelection(param.memoSelection || '');
            self.initSelection(param.initSelection || '');

        }
    }

    //Order Selection
    interface IOrderSelection {
        selectionID?: string;
        histId?: string;
        disporder: number;
        initSelection: number;
    }

    class OrderSelection {
        selectionID: KnockoutObservable<string> = ko.observable('');
        histId: KnockoutObservable<string> = ko.observable('');
        disporder: KnockoutObservable<number> = ko.observable('');
        initSelection: KnockoutObservable<number> = ko.observable('');

        constructor(param: OrderSelection) {
            let self = this;
            self.selectionID(param.selectionID || '');
            self.histId(param.histId || '');
            self.disporder(param.disporder || '');
            self.initSelection(param.initSelection || '');
        }
    }
}

function makeIcon(value, row) {
    if (value == 1)
        return "●";
    return '';
}
