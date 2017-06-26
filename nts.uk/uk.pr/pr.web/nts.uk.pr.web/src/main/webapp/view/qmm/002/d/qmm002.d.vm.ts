module qmm002.d.viewmodel {
    export class ScreenModel {
        selectedPaymentDate: KnockoutObservable<any>;
        items: KnockoutObservableArray<any>;
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode: KnockoutObservable<any>;
        currentItem: KnockoutObservable<Bank>;
        currentCodeList: KnockoutObservableArray<any>;
        INP_001: any;
        INP_002: any;
        INP_003: any;
        INP_004: any;
        isCreated: any;
        dirty: nts.uk.ui.DirtyChecker;
        confirmDirty: boolean = false;
        messages: KnockoutObservable<any>;

        constructor() {
            var self = this;
            self.items = ko.observableArray([]);
            self.currentItem = ko.observable(new Bank(null, null,null, null));
            self.currentCode = ko.observable();
            self.currentCodeList = ko.observableArray([]);
            self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
            self.columns = ko.observableArray([
                { headerText: 'コード', width: 50, key: 'codeDisplay', hidden: true },
                { headerText: 'コード/名称', width: 50, key: 'nameDisplay' }
            ]);

            self.messages = ko.observableArray([
                { messageId: "ER001", message: "には未入力の必須項目の名称を表記" },
                { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n＊を確認してください。" },
                { messageId: "AL001", message: "変更された内容が登録されていません。\r\nよろしいですか。" }
            ]);

            self.currentCode.subscribe(function(codeChanged) {
                if (!self.checkDirty()) {
                    self.currentItem(self.getCode(codeChanged));
                    self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                } else {
                    if (self.confirmDirty) {
                        self.confirmDirty = false;
                        return;
                    }
                    nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                        self.currentItem(self.getCode(codeChanged));
                        self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                    }).ifNo(function() {
                        self.confirmDirty = true;
                        self.currentCode(self.currentItem().code())
                    })
                }
                self.isCreated(false);
            });

            self.isCreated = ko.observable(true);
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();

            self.getBankList().done(function(data) {
                var hadData = self.isEmptyList(data);
                self.isCreated(!hadData);
                if (hadData) {
                    self.currentItem(self.selectedFirst(data[0]));
                    self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                }
                dfd.resolve();
            });

            return dfd.promise();
        }

        /**
         * Event button 002
         */
        addBank(): any {
            var self = this;
            var bankInfo = {
                bankCode: self.currentItem().code(),
                bankName: self.currentItem().name(),
                bankNameKana: self.currentItem().nameKana(),
                memo: self.currentItem().memo()
            };

            var dfd = $.Deferred();
            qmm002.d.service.addBank(self.isCreated(), bankInfo).done(function() {
                self.dirty = new nts.uk.ui.DirtyChecker(ko.observable(bankInfo))
                dfd.resolve();
            }).fail(function(error) {
                var messageList = self.messages();
                if (error.messageId == messageList[0].messageId) { // ER001
                    $('#INP_001').ntsError('set', messageList[0].message);
                    $('#INP_002').ntsError('set', messageList[0].message);
                } if (error.messageId == messageList[1].messageId) { // ER005 
                    $('#INP_001').ntsError('set', messageList[1].message);
                }
            }).then(function() {
                $.when(self.getBankList()).done(function() {
                    self.currentCode(bankInfo.bankCode);
                });
            });
        }

        close() {
            var self = this;
            if (!self.checkDirty()) {
                nts.uk.ui.windows.close();
            } else {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                    nts.uk.ui.windows.close();
                })
            }
        }

        Delete(): any {
            var self = this;
            var bankInfo = {
                bankCode: self.currentItem().code(),
                bankName: self.currentItem().name(),
                bankNameKana: self.currentItem().nameKana(),
                memo: self.currentItem().memo()
            };

            self.cleanForm();
            var dfd = $.Deferred();
            qmm002.d.service.removeBank(bankInfo).done(function() {
                dfd.resolve();
            }).fail(function(error) {
                alert(error);
            }).then(function() {
                $.when(self.getBankList()).done(function() {
                });
            });
        }

        cleanForm(): any {
            var self = this;
            self.confirmDirty = true;
            if (!self.checkDirty()) {
                self.currentItem(new Bank(null, null,null,null));
                self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                self.currentCode("");
                self.isCreated(true);
            } else {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                    self.currentItem(new Bank(null,null,null,null));
                    self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                    self.currentCode("");
                    self.isCreated(true);
                })
            }
        }

        getCode(codeChange): Bank {
            var self = this;
            var itemBank = _.find(self.items(), function(item) {
                return item.code() === codeChange;
            });

            if (!itemBank) {
                return new Bank("", "", "", "");
            }

            return new Bank(itemBank.code(), itemBank.name(), itemBank.nameKana(), itemBank.memo());
        }

        getBankList(): any {
            var self = this;
            var dfd = $.Deferred();
            qmm002.d.service.getBankList().done(function(data) {
                var list001: Array<Bank> = [];
                _.forEach(data, function(itemBank) {
                 list001.push(new Bank(itemBank.bankCode,itemBank.bankName,itemBank.bankNameKana,itemBank.memo));
                });
                self.items(list001);
                dfd.resolve(data);
            }).fail(function(res) {
                // error
            });

            return dfd.promise();
        }

        /**
         * Check item list had data?
         */
        isEmptyList(data): boolean {
            return data.length > 0;
        }

        /**
         * Selected first item bank
         */
        selectedFirst(item): Bank {
            var self = this;
            self.currentCode(item.bankCode);
            return new Bank(item.bankCode, item.bankName, item.bankNameKana, item.memo);
        }

        checkDirty(): boolean {
            var self = this;
            if (self.dirty.isDirty()) {
                return true;
            } else {
                return false;
            }
        }
    }

    class Bank {
        code: KnockoutObservable<string>;
        codeDisplay: string;
        name: KnockoutObservable<string>;
        nameDisplay: string;
        nameKana: KnockoutObservable<string>;
        memo: KnockoutObservable<string>;

        constructor(bankCode: string, bankName: string, bankNameKana: string, memo: string) {
            this.code = ko.observable(bankCode);
            this.codeDisplay = bankCode;
            this.name = ko.observable(bankName);
            this.nameDisplay = bankCode + "  " + bankName;
            this.nameKana = ko.observable(bankNameKana);
            this.memo = ko.observable(memo);
        }
    }
}