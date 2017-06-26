module nts.uk.pr.view.qmm017.p {
    export module viewmodel {
        export class ScreenModel {
            items: KnockoutObservableArray<any>;
            columns: KnockoutObservableArray<any>;
            currentCodeList: KnockoutObservableArray<any>;
            subject: string;

            constructor(data) {
                var self = this;
                self.subject = "基準金額に設定する、" + data.subject + "を";
                self.items = ko.observableArray(data.itemList);
                self.columns = ko.observableArray([
                    { headerText: 'コード', prop: 'code', width: 50 },
                    { headerText: '名称', prop: 'name', width: 145 }
                ]);
                self.currentCodeList = ko.observableArray(data.selectedItems);
            }

            closeAndReturnData() {
                var self = this;
                let baseAmountListItem = self.currentCodeList();
                if (baseAmountListItem.length > 0) {
                    nts.uk.ui.windows.setShared('baseAmountListItem', baseAmountListItem);
                    nts.uk.ui.windows.close();
                } else {
                    nts.uk.ui.dialog.alert("項目が選択されていません。");    
                }
            }

            closeDialog() {
                nts.uk.ui.windows.close();
            }
        }
    }

    class ItemModel {
        code: string;
        name: string;
        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
}