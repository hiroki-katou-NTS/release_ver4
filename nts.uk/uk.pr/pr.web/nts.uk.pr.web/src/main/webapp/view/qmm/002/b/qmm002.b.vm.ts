module nts.uk.pr.view.qmm002.b {
    export module viewmodel {
        export class ScreenModel {
            lst_001: any;
            lst_002: any;
            selectedCodes: any;
            messages: KnockoutObservable<any>;

            constructor() {
                var self = this;
                self.lst_001 = ko.observableArray([]);
                self.lst_002 = ko.observableArray([]);
                self.selectedCodes = ko.observableArray([]);
                self.messages = ko.observableArray([
                    { messageId: "AL002", message: "データを削除します。\r\nよろしいですか？" },
                    { messageId: "ER005", message: "入力した銀行コードは既に存在しています。\r\n銀行コードを確認してください。" },
                    { messageId: "ER008", message: "選択された＊は使用されているため削除できません。" },
                    { messageId: "ER007", message: "＊が選択されていません。" }
                ]);
            }

            startPage() {
                var self = this;
                var list = nts.uk.ui.windows.getShared('listItem');
                self.lst_001(list);
            }

            /**
             * close screen qmm002b
             */
            close() {
                var self = this;
                nts.uk.ui.windows.close();
            }


            /**
             * Delete List Bank, Branch
             */
            btn_001() {
                var self = this;
                if (!self.selectedCodes().length) {
                    nts.uk.ui.dialog.confirm(self.messages()[3].message)
                    return;
                }
                nts.uk.ui.dialog.confirm(self.messages()[0].message).ifYes(function() {
                    var keyBank = [];
                    _.forEach(self.selectedCodes(), function(item) {
                        var code = item.split('-');
                        var bankCode = code[0];
                        var branchId = self.getNode(item, bankCode);
                        keyBank.push({
                            bankCode: bankCode,
                            branchId: branchId
                        });
                    });

                    var data =
                        {
                            bank: keyBank,
                        };
                    service.removeBank(data).done(function() {
                        self.close();
                    }).fail(function(error) {
                        var messageList = self.messages();
                        if (error.messageId == messageList[2].messageId) { // ER008
                            var messageError = nts.uk.text.format(messageList[2].message, self.selectedCodes());
                            nts.uk.ui.dialog.alert(messageError);
                        }
                    });
                });

            }

            /**
             * select node information
             */
            getNode(codeNew, parentId): String {
                var self = this;
                self.lst_002(nts.uk.util.flatArray(self.lst_001(), "childs"))
                var node = _.find(self.lst_002(), function(item: BankInfo) {
                    return item.treeCode == codeNew;
                });

                return node.branchId;
            }
        }

        export class BankInfo {
            treeCode: string;
            code: string;
            branchId: string;
            name: string;
            displayName: string;
            nameKata: string;
            memo: string;
            childs: Array<BankInfo>;
            parentCode: string;

            constructor(treeCode?: string, code?: string, branchId?: string, name?: string, nameKata?: string, memo?: string, childs?: Array<BankInfo>, parentCode?: string) {
                var self = this;
                self.treeCode = treeCode;
                self.code = code;
                self.branchId = branchId;
                self.name = name;
                self.displayName = self.code + "  " + self.name;
                self.nameKata = nameKata;
                self.memo = memo;
                self.childs = childs;
                self.parentCode = parentCode;
            }
        }
    }
}