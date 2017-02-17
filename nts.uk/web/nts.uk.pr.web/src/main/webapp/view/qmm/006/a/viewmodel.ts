module qmm006.a.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<any>;
        dataSource: KnockoutObservableArray<any>;
        dataSource2: KnockoutObservableArray<any>;

        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode: KnockoutObservable<any>;

        currentLineBank: KnockoutObservable<LineBank>;
        roundingRules: KnockoutObservableArray<any>;

        isEnable: KnockoutObservable<boolean>;
        isAppear: KnockoutObservable<boolean>;

        bankName: KnockoutObservable<string>;
        branchName: KnockoutObservable<string>;
        messageList: KnockoutObservableArray<any>;

        constructor() {
            var self = this;
            self.items = ko.observableArray([]);
            self.dataSource = ko.observableArray([]);
            self.dataSource2 = ko.observableArray([]);
            self.currentCode = ko.observable();

            self.messageList = ko.observableArray([
                { messageId: "＊が入力されていません。", message: "＊が入力されていません。" },
                { messageId: "＊が選択されていません。", message: "＊が選択されていません。" },
                { messageId: "入力した＊は既に存在しています。\r\n ＊を確認してください。", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" }

            ]);

            self.columns = ko.observableArray([
                { headerText: 'コード', key: 'lineBankCode', width: 45 },
                { headerText: '名称', key: 'lineBankName', width: 120 },
                { headerText: '口座区分', key: 'accountAtr', width: 110 },
                { headerText: '口座番号', key: 'accountNo', width: 100 }
            ]);
            self.currentLineBank = ko.observable(new LineBank(null, null, null, null, 0, null, null, null, []));
            self.roundingRules = ko.observableArray([
                { code: '0', name: '普通' },
                { code: '1', name: '当座' },
            ]);

            self.bankName = ko.observable('');
            self.branchName = ko.observable('');

            self.isEnable = ko.observable(false);
            self.isAppear = ko.observable(true);

            //theo doi su thay doi cua currentCode khi click
            self.currentCode.subscribe(function(codeChanged) {
                var lineBank = self.findLineBank(codeChanged);
                var tmp = _.find(self.dataSource2(), function(x) {
                    return x.code === lineBank.bankCode();
                });
                var tmp1 = _.find(self.dataSource2(), function(x) {
                    return x.code === lineBank.branchCode() && x.parentCode === lineBank.bankCode();
                });
                if (tmp != undefined) {
                    self.bankName(tmp.name);
                } else self.bankName('');
                if (tmp1 != undefined) {
                    self.branchName(tmp1.name);
                } else {
                    self.isAppear(false);
                    self.branchName('');
                }
                self.isEnable(false);
                self.currentLineBank(lineBank);
            });
        }

        startPage() {
            var self = this;
            var dfd = $.Deferred();
            $.when(self.findBankAll()).done(function() {
                self.findAll().done(function() {
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }

        OpenBDialog() {
            var self = this;
            var lineBank = self.currentLineBank();
            nts.uk.ui.windows.sub.modal("/view/qmm/006/b/index.xhtml", { title: "銀行情報一覧", dialogClass: "no-close" }).onClosed(function() {
                if (nts.uk.ui.windows.getShared("selectedBank") != null) {
                    if (nts.uk.ui.windows.getShared("selectedBank").parentCode == null) {
                        lineBank.bankCode(nts.uk.ui.windows.getShared("selectedBank").code);
                        self.bankName(nts.uk.ui.windows.getShared("selectedBank").name);
                        lineBank.branchCode(nts.uk.ui.windows.getShared("selectedBank").parentCode);
                        self.branchName(nts.uk.ui.windows.getShared("selectedBank").parentName);
                    } else {
                        lineBank.branchCode(nts.uk.ui.windows.getShared("selectedBank").code);
                        self.bankName(nts.uk.ui.windows.getShared("selectedBank").parentName);
                        lineBank.bankCode(nts.uk.ui.windows.getShared("selectedBank").parentCode);
                        self.branchName(nts.uk.ui.windows.getShared("selectedBank").name);
                        self.isAppear(true);
                    }
                    self.clearError();
                }
            });
        }

        OpenCDialog() {
            var self = this;
            nts.uk.ui.windows.sub.modal("/view/qmm/006/c/index.xhtml", { title: "振込元銀行の登録　＞　振込元銀行", dialogClass: "no-close" });
        }

        findLineBank(codeNew): LineBank {
            let self = this;
            let data = _.find(self.items(), function(item) {
                return item.lineBankCode === codeNew;
            });
            if (!data) {
                return new LineBank(null, null, null, null, 0, null, null, null, []);
            }
            return new LineBank(data.bankCode, data.branchCode, data.lineBankCode, data.lineBankName, data.accountAtr, data.accountNo, data.memo, data.requesterName, data.consignors);
        }

        clearForm(): any {
            var self = this;
            self.currentLineBank(new LineBank(null, null, null, null, 0, null, null, null, []));
            self.currentCode("");
            self.isEnable(true);
        }

        selectedFirstItem(item): LineBank {
            var self = this;
            self.currentCode(item.lineBankCode);
            return new LineBank(item.bankCode, item.branchCode,
                item.lineBankCode, item.lineBankName,
                item.accountAtr, item.accountNo,
                item.memo, item.requesterName, item.consignors);
        }

        remove() {
            var self = this;
            if (self.items().length == 0) {
                return;
            }

            var command = {
                accountAtr: self.currentLineBank().accountAtr(),//A_SEL_001
                accountNo: self.currentLineBank().accountNo(),//A_INP_003
                bankCode: self.currentLineBank().bankCode(),//A_LBL_004
                branchCode: self.currentLineBank().branchCode(),//A_LBL_007
                consignor: [
                    { "code": self.currentLineBank().consignors()[0].consignorCode(), "memo": self.currentLineBank().consignors()[0].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[1].consignorCode(), "memo": self.currentLineBank().consignors()[1].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[2].consignorCode(), "memo": self.currentLineBank().consignors()[2].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[3].consignorCode(), "memo": self.currentLineBank().consignors()[3].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[4].consignorCode(), "memo": self.currentLineBank().consignors()[4].consignorMemo() },
                ],
                lineBankCode: self.currentLineBank().lineBankCode(),//A_INP_001,
                lineBankName: self.currentLineBank().lineBankName(), //A_INP_002
                memo: self.currentLineBank().memo(),//A_INP_005
                requesterName: self.currentLineBank().requesterName()//A_INP_004
            };
            qmm006.a.service.remove(command)
                .done(function() {
                    self.findAll();
                }).fail(function(error) {
                    alert(error.message);
                });
        }

        //find list Bank
        findBankAll() {
            var self = this;
            var lineBank = self.currentLineBank();
            var dfd = $.Deferred();
            qmm006.a.service.findBankAll()
                .done(function(data) {
                    if (data.length > 0) {
                        var bankData = [];
                        _.forEach(data, function(item) {
                            var childs = _.map(item.bankBranch, function(itemChild: any) {
                                return new BankBranch(itemChild.bankBranchCode, itemChild.bankBranchName, item.bankCode, item.bankName, item.bankCode + itemChild.bankBranchCode, []);
                            });
                            bankData.push(new BankBranch(item.bankCode, item.bankName, null, null, item.bankCode, childs));
                        });
                        self.dataSource(bankData);
                        self.dataSource2(nts.uk.util.flatArray(self.dataSource(), "childs"));
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        findAll() {
            var self = this;
            var dfd = $.Deferred();
            qmm006.a.service.findAll()
                .done(function(data) {
                    if (data.length > 0) {
                        self.items(data);
                        self.currentLineBank(self.selectedFirstItem(data[0]));
                    } else {
                        self.items([]);
                        self.clearForm();
                        self.isAppear(false);
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }


        saveData() {
            var self = this;
            var dfd = $.Deferred();

            self.clearError();

            var command = {
                accountAtr: self.currentLineBank().accountAtr(),//A_SEL_001
                accountNo: self.currentLineBank().accountNo(),//A_INP_003
                bankCode: self.currentLineBank().bankCode(),//A_LBL_004
                branchCode: self.currentLineBank().branchCode(),//A_LBL_007
                consignor: [
                    { "code": self.currentLineBank().consignors()[0].consignorCode(), "memo": self.currentLineBank().consignors()[0].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[1].consignorCode(), "memo": self.currentLineBank().consignors()[1].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[2].consignorCode(), "memo": self.currentLineBank().consignors()[2].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[3].consignorCode(), "memo": self.currentLineBank().consignors()[3].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[4].consignorCode(), "memo": self.currentLineBank().consignors()[4].consignorMemo() },
                ],

                lineBankCode: self.currentLineBank().lineBankCode(),//A_INP_001,
                lineBankName: self.currentLineBank().lineBankName(), //A_INP_002
                memo: self.currentLineBank().memo(),//A_INP_005
                requesterName: self.currentLineBank().requesterName()//A_INP_004
            };
            qmm006.a.service.saveData(self.isEnable(), command)
                .done(function() {
                }).fail(function(error) {
                    if (error.messageId == self.messageList()[0].messageId) {
                        var message = self.messageList()[0].message;
                        if (!command.lineBankCode) {
                            $('#A_INP_001').ntsError('set', message);
                        }
                        if (!command.lineBankName) {
                            $('#A_INP_002').ntsError('set', message);
                        }
                        if (!command.accountNo) {
                            $('#A_INP_003').ntsError('set', message);
                        }
                    } else if (error.messageId == self.messageList()[1].messageId) {
                        var message = self.messageList()[1].message;
                        if (!command.bankCode) {
                            $('#A_LBL_004').ntsError('set', message);
                        }
                        if (!command.branchCode) {
                            $('#A_LBL_007').ntsError('set', message);
                        }
                    } else if (error.messageId == self.messageList()[2].messageId) {
                        var message = self.messageList()[2].message;
                        $('#A_INP_001').ntsError('set', error.message);
                    }
                }).then(function() {
                    //load lai list va chi vao row moi them
                    $.when(self.findAll()).done(function() {
                        self.currentCode(command.lineBankCode);
                        self.isEnable(false);
                    });
                });
            return dfd.promise();
        }

        clearError() {
            $('#A_INP_001').ntsError('clear');
            $('#A_INP_002').ntsError('clear');
            $('#A_INP_003').ntsError('clear');
            $('#A_LBL_004').ntsError('clear');
            $('#A_LBL_007').ntsError('clear');
        }
    }

    class LineBank {
        bankCode: KnockoutObservable<string>;
        branchCode: KnockoutObservable<string>;
        consignors: KnockoutObservableArray<Consignor>;
        lineBankCode: KnockoutObservable<string>;
        lineBankName: KnockoutObservable<string>;
        accountAtr: KnockoutObservable<number>;
        accountNo: KnockoutObservable<string>;
        memo: KnockoutObservable<string>;
        requesterName: KnockoutObservable<string>;


        constructor(bankCode: string, branchCode: string, lineBankCode: string, lineBankName: string, accountAtr: number, accountNo: string, memo: string, requesterName: string, consignors: Array<any>) {
            this.bankCode = ko.observable(bankCode);
            this.branchCode = ko.observable(branchCode);
            this.lineBankCode = ko.observable(lineBankCode);
            this.lineBankName = ko.observable(lineBankName);
            this.accountAtr = ko.observable(accountAtr);
            this.accountNo = ko.observable(accountNo);
            this.memo = ko.observable(memo);
            this.requesterName = ko.observable(requesterName);
            this.consignors = ko.observableArray([]);

            for (var i = 0; i <= 4; i++) {
                var consignorItem = consignors[i];
                switch (i) {
                    case 0:
                        if (consignorItem) {
                            this.consignors.push(new Consignor("①", consignorItem.code, consignorItem.memo));
                        } else {
                            this.consignors.push(new Consignor("①", "", ""));
                        }

                        break;
                    case 1:
                        if (consignorItem) {
                            this.consignors.push(new Consignor("②", consignorItem.code, consignorItem.memo));
                        } else {
                            this.consignors.push(new Consignor("②", "", ""));
                        }
                        break;
                    case 2:
                        if (consignorItem) {
                            this.consignors.push(new Consignor("③", consignorItem.code, consignorItem.memo));
                        } else {
                            this.consignors.push(new Consignor("③", "", ""));
                        }
                        break;
                    case 3:
                        if (consignorItem) {
                            this.consignors.push(new Consignor("④", consignorItem.code, consignorItem.memo));
                        } else {
                            this.consignors.push(new Consignor("④", "", ""));
                        }
                        break;
                    case 4:
                        if (consignorItem) {
                            this.consignors.push(new Consignor("⑤", consignorItem.code, consignorItem.memo));
                        } else {
                            this.consignors.push(new Consignor("⑤", "", ""));
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    class Consignor {
        noIcon: KnockoutObservable<string>;
        consignorCode: KnockoutObservable<string>;
        consignorMemo: KnockoutObservable<string>;

        constructor(noIcon: string, consignorCode: string, consignorMemo: string) {
            this.noIcon = ko.observable(noIcon);
            this.consignorCode = ko.observable(consignorCode);
            this.consignorMemo = ko.observable(consignorMemo);
        }
    }

    class BankBranch {
        code: string;
        name: string;
        nodeText: string;
        parentCode: string;
        parentName: string;
        treeCode: string;
        childs: any;
        constructor(code: string, name: string, parentCode: string, parentName: string, treeCode: string, childs: Array<BankBranch>) {
            var self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;
            self.parentCode = parentCode;
            self.parentName = parentName;
            self.treeCode = treeCode;
        }
    }
}
