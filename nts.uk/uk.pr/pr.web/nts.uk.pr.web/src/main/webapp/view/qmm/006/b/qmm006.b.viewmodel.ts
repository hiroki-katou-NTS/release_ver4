module qmm006.b.viewmodel {
    export class ScreenModel {
        dataSource: any;
        dataSource2: any;
        singleSelectedCode: KnockoutObservable<any>;
        selectedBank: KnockoutObservable<any>;
        selectedCodes: any;
        headers: any;
        messageList: KnockoutObservableArray<any>;

        constructor() {
            var self = this;
            self.dataSource = ko.observableArray([]),
                self.dataSource2 = ko.observableArray([]),
                self.singleSelectedCode = ko.observable();
            self.selectedBank = ko.observable();
            self.selectedCodes = ko.observableArray([]);
            self.headers = ko.observableArray(["Item Value Header", "コード/名称"]);
            self.messageList = ko.observableArray([
                { messageId: "ER007", message: "支店がが選択されていません。" },
                { messageId: "ER010", message: "対象データがありません。" },
            ]);

            self.singleSelectedCode.subscribe(function(codeChanged) {
                self.selectedBank(self.getBank(codeChanged));
            });
        }

        /**
         * find data in Bank base-on treeCode
         */
        getBank(codeNew): Bank {
            let self = this;
            let bank: Bank = _.find(self.dataSource2(), function(item: any) {
                return item.treeCode === codeNew;
            });
            return bank;
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.findBankAll().done(function() {
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }

        /**
         * get data from database BANK, set to property dataSource
         */
        findBankAll(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            qmm006.b.service.findBankAll()
                .done(function(data) {
                    if (data.length > 0) {
                        var bankData = [];
                        _.forEach(data, function(item) {
                            var childs = _.map(item.bankBranch, function(itemChild: any) {
                                return new Bank(itemChild.bankBranchCode, itemChild.bankBranchID, itemChild.bankBranchName, item.bankCode, item.bankName, item.bankCode + itemChild.bankBranchCode, []);
                            });
                            bankData.push(new Bank(item.bankCode, null, item.bankName, null, null, item.bankCode, childs));
                        });
                        self.dataSource(bankData);
                        self.dataSource2(nts.uk.util.flatArray(self.dataSource(), "childs"));
                        //select first row child of first row parent
                        if (data[0].bankBranch != null) {
                            self.singleSelectedCode(data[0].bankCode + data[0].bankBranch[0].bankBranchCode);
                        }
                        //select first row parent
                        else {
                            self.singleSelectedCode(data[0].bankCode);
                        }
                    } else {
                        nts.uk.ui.dialog.alert(self.messageList()[1].message);
                    }

                    dfd.resolve();
                }).fail(function(res) { });
            return dfd.promise();
        }

        /**
         * forward data 'selectedBank' to screen A, close dialog
         */
        closeDialog(): void {
            nts.uk.ui.windows.setShared("selectedBank", null, true);
            nts.uk.ui.windows.close();
        }

        /**
         * forward data 'selectedBank' to screen A, close dialog
         */
        transferData(): void {
            var self = this;
            //define row selected
            if (_.find(self.dataSource(), function(x: any) {
                return x.treeCode === self.singleSelectedCode();
            }) == undefined) {
                // select row child will transfer data to screen QMM006.a
                nts.uk.ui.windows.setShared("selectedBank", self.selectedBank(), true);
                nts.uk.ui.windows.close();
            } else {
                // select row parent will appear alert
                nts.uk.ui.dialog.alert(self.messageList()[0].message);
            }
        }
    }

    class Bank {
        code: string;
        name: string;
        nodeText: string;
        parentCode: string;
        parentName: string;
        treeCode: string;
        childs: any;
        branchId: string;
        constructor(code: string, branchId: string, name: string, parentCode: string, parentName: string, treeCode: string, childs: Array<Bank>) {
            var self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;
            self.parentCode = parentCode;
            self.parentName = parentName;
            self.treeCode = treeCode;
            self.branchId = branchId;
        }
    }
};