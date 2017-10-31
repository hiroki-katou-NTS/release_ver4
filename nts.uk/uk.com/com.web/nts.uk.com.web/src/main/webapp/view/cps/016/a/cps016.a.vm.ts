module nts.uk.com.view.cps016.a.viewmodel {
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import textUK = nts.uk.text;
    import block = nts.uk.ui.block;
    export class ScreenModel {
        listItems: KnockoutObservableArray<ISelectionItem> = ko.observableArray([]);
        perInfoSelectionItem: KnockoutObservable<SelectionItem> = ko.observable(new SelectionItem({ selectionItemId: '', selectionItemName: '' }));
        rulesFirst: KnockoutObservableArray<IRule>;
        checkCreate: KnockoutObservable<boolean>;
        closeUp: KnockoutObservable<boolean>;

        constructor() {
            let self = this,
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem(),
                formatSelection = perInfoSelectionItem.formatSelection();
            self.checkCreate = ko.observable(true);
            self.closeUp = ko.observable(false);
            self.rulesFirst = ko.observableArray([
                { id: 0, name: getText('Enum_SelectionCodeCharacter_NUMBER_TYPE') },
                { id: 1, name: getText('Enum_SelectionCodeCharacter_CHARATERS_TYPE') }
            ]);

            //selectionItemIdのsubscribe
            perInfoSelectionItem.selectionItemId.subscribe(x => {
                if (x) {
                    nts.uk.ui.errors.clearAll();
                    service.getPerInfoSelectionItem(x).done((_perInfoSelectionItem: ISelectionItem) => {
                        if (_perInfoSelectionItem) {
                            perInfoSelectionItem.selectionItemName(_perInfoSelectionItem.selectionItemName);
                            perInfoSelectionItem.memo(_perInfoSelectionItem.memo);
                            perInfoSelectionItem.integrationCode(_perInfoSelectionItem.integrationCode);
                            perInfoSelectionItem.selectionItemClassification(_perInfoSelectionItem.selectionItemClassification === 1 ? true : false);

                            let iformat = _perInfoSelectionItem.formatSelection;
                            formatSelection.selectionCode(iformat.selectionCode);
                            formatSelection.selectionCodeCharacter(iformat.selectionCodeCharacter);
                            formatSelection.selectionName(iformat.selectionName);
                            formatSelection.selectionExternalCode(iformat.selectionExternalCode);
                        }
                    });
                }
                self.checkCreate(false);
            });
        }

        //開始
        start(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();

            nts.uk.ui.errors.clearAll();
            service.getAllSelectionItems().done((itemList: Array<ISelectionItem>) => {
                if (itemList && itemList.length > 0) {
                    itemList.forEach(x => self.listItems.push(x));
                    self.perInfoSelectionItem().selectionItemId(self.listItems()[0].selectionItemId);
                } else {//0件の場合: エラーメッセージの表示(#Msg_455)
                    alertError({ messageId: "Msg_455" });
                    self.registerDataSelectioItem();
                }
                dfd.resolve();
            }).fail(error => {//0件の場合: エラーメッセージの表示(#Msg_455)
                alertError({ messageId: "Msg_455" });
            });

            return dfd.promise();
        }

        //新規ボタン
        registerDataSelectioItem() {
            let self = this,
                perInfoSelectionItem: SelectionItem = self.perInfoSelectionItem(),
                formatSelection = perInfoSelectionItem.formatSelection();

            nts.uk.ui.errors.clearAll();
            perInfoSelectionItem.selectionItemName('');
            perInfoSelectionItem.memo('');
            perInfoSelectionItem.integrationCode('');
            perInfoSelectionItem.selectionItemClassification(false);
            formatSelection.selectionCode('');
            formatSelection.selectionName('');
            formatSelection.selectionExternalCode('');
            formatSelection.selectionCodeCharacter(false);
            self.checkCreate(true);
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
        addDataSelectioItem() {
            var self = this;

            if (self.validate()) {
                if (self.checkCreate() == true) {
                    self.add();
                } else {
                    self.update();
                }
            }
        }

        //新規モード
        add() {
            let self = this,
                currentItem: SelectionItem = self.perInfoSelectionItem(),
                listItems: Array<SelectionItem> = self.listItems(),
                _selectionItemName = _.find(listItems, x => x.selectionItemName == currentItem.selectionItemName()),
                formatSelection = currentItem.formatSelection(),
                command = ko.toJS(currentItem);

            //「個人情報の選択項目」を登録する
            service.saveDataSelectionItem(command).done(function(selectId) {
                self.listItems.removeAll();
                //画面項目「選択項目名称一覧：選択項目名称一覧」を登録する
                service.getAllSelectionItems().done((itemList: Array<ISelectionItem>) => {
                    if (itemList && itemList.length) {
                        itemList.forEach(x => self.listItems.push(x));
                    }
                });
                self.listItems.valueHasMutated();
                self.perInfoSelectionItem().selectionItemId(selectId);

                //「CPS017_個人情報の選択肢の登録」をモーダルダイアログで起動する
                confirm({ messageId: "Msg_456" }).ifYes(() => {
                    modal('/view/cps/017/a/index.xhtml', { title: '', height: 1000, width: 1500 }).onClosed(function(): any {
                    });
                }).ifNo(() => {
                    self.listItems.valueHasMutated();
                    return;
                })
            }).fail(error => {
                alertError({ messageId: "Msg_513" });
            });
        }

        //更新モード
        update() {
            let self = this,
                currentItem: SelectionItem = self.perInfoSelectionItem(),
                formatSelection = currentItem.formatSelection(),
                listItems: Array<SelectionItem> = self.listItems(),
                _selectionItemName = _.find(listItems, x => x.selectionItemName == currentItem.selectionItemName()),
                oldIndex = _.findIndex(listItems, x => x.selectionItemId == currentItem.selectionItemId()),
                command = ko.toJS(currentItem);

            //「個人情報の選択項目」を更新する
            service.updateDataSelectionItem(command).done(function() {
                self.listItems.removeAll();
                //画面項目「選択項目名称一覧：選択項目名称一覧」を更新する
                service.getAllSelectionItems().done((itemList: Array<ISelectionItem>) => {
                    if (itemList && itemList.length) {
                        itemList.forEach(x => self.listItems.push(x));
                    }

                    let newItem = itemList[oldIndex];
                    currentItem.selectionItemId(newItem.selectionItemId);
                });
                nts.uk.ui.dialog.alert({ messageId: "Msg_15" });
                self.listItems.valueHasMutated();

            }).fail(error => {
                alertError({ messageId: "Msg_513" });
            });
        }

        //削除ボタン
        removeDataSelectioItem() {
            let self = this,
                items = ko.unwrap(self.listItems),
                currentItem: SelectionItem = self.perInfoSelectionItem(),
                formatSelection = currentItem.formatSelection(),
                listItems: Array<SelectionItem> = self.listItems(),
                oldIndex = _.findIndex(listItems, x => x.selectionItemId == currentItem.selectionItemId()),
                command = ko.toJS(currentItem),
                lastIndex = items.length - 1;

            if (items.length > 0) {
                confirm({ messageId: "Msg_551" }).ifYes(() => {
                    service.removeDataSelectionItem(command).done(function() {
                        self.listItems.removeAll();
                        service.getAllSelectionItems().done((itemList: Array<ISelectionItem>) => {
                            if (itemList && itemList.length) {
                                itemList.forEach(x => self.listItems.push(x));
                                if (oldIndex == lastIndex) {
                                    oldIndex--;
                                }
                                let newItem = itemList[oldIndex];
                                currentItem.selectionItemId(newItem.selectionItemId);
                            } else {
                                self.registerDataSelectioItem();
                            }
                        });
                        self.listItems.valueHasMutated();
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

        // 選択肢の登録ボタン
        OpenCPS017() {
            let self = this;

            modal('/view/cps/017/a/index.xhtml', { title: '', height: 1000, width: 1500 }).onClosed(function(): any {
            });
        }

    }

    interface ISelectionItem {
        selectionItemId: string;
        selectionItemName: string;
        memo?: string;
        selectionItemClassification?: number;
        contractCode?: string;
        integrationCode?: string;
        formatSelection?: IFormatSelection;
    }

    class SelectionItem {
        selectionItemId: KnockoutObservable<string> = ko.observable('');
        selectionItemName: KnockoutObservable<string> = ko.observable('');
        memo: KnockoutObservable<string> = ko.observable('');
        selectionItemClassification: KnockoutObservable<boolean> = ko.observable(false);
        contractCode: KnockoutObservable<string> = ko.observable('');
        integrationCode: KnockoutObservable<string> = ko.observable('');
        formatSelection: KnockoutObservable<FormatSelection> = ko.observable(new FormatSelection(undefined));

        constructor(param: ISelectionItem) {
            let self = this;
            self.selectionItemId(param.selectionItemId || '');
            self.selectionItemName(param.selectionItemName || '');
            self.memo(param.memo || '');
            self.selectionItemClassification(param.selectionItemClassification === 1 ? true : false);
            self.contractCode(param.contractCode || '');
            self.integrationCode(param.integrationCode || '');

            let _format = self.formatSelection(),
                _iformat = param.formatSelection;
            if (_iformat) {
                _format.selectionCode(_iformat.selectionCode);
                _format.selectionCodeCharacter(_iformat.selectionCodeCharacter);
                _format.selectionName(_iformat.selectionName);
                _format.selectionExternalCode(_iformat.selectionExternalCode);
            }
        }
    }

    interface IFormatSelection {
        selectionCode: number;
        selectionCodeCharacter?: number;
        selectionName: number;
        selectionExternalCode: number;
    }

    class FormatSelection {
        selectionCode: KnockoutObservable<number> = ko.observable('');
        selectionCodeCharacter: KnockoutObservable<boolean> = ko.observable(false);
        selectionName: KnockoutObservable<number> = ko.observable('');
        selectionExternalCode: KnockoutObservable<number> = ko.observable('');

        constructor(param: IFormatSelection) {
            let self = this;
            if (param) {
                self.selectionCode(param.selectionCode || '');
                self.selectionCodeCharacter(param.selectionCodeCharacter === 1 ? true : false);
                self.selectionName(param.selectionName || '');
                self.selectionExternalCode(param.selectionExternalCode || '');
            }
        }
    }

    interface IRule {
        id: number;
        name: string;
    }
}