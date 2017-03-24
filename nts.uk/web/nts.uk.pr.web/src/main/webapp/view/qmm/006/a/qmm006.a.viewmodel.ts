module qmm006.a.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<any>;
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode: KnockoutObservable<any>;
        currentLineBank: KnockoutObservable<LineBank>;
        roundingRules: KnockoutObservableArray<any>;

        dataSource: KnockoutObservableArray<any>;
        dataSource2: KnockoutObservableArray<any>;
        //findAll lan dau tien thi k chay clearError va se select first row
        isFirstFindAll: KnockoutObservable<boolean>;
        isDeleteEnable: KnockoutObservable<boolean>;
        //Khong cho lap lai thong bao 2 lan trong ham subscribe
        notLoopAlert: KnockoutObservable<boolean>;
        //check enable texteditor
        isEnable: KnockoutObservable<boolean>;
        //check enable btn003
        enableBtn003: KnockoutObservable<boolean>;

        bankName: KnockoutObservable<string>;
        branchName: KnockoutObservable<string>;

        countLineBank: KnockoutObservable<number>;
        indexLineBank: KnockoutObservable<number>;

        messageList: KnockoutObservableArray<any>;
        dirty: nts.uk.ui.DirtyChecker;


        constructor() {
            var self = this;
            self.items = ko.observableArray([]);
            //set currentCode = '' vi khi findAll k co du lieu se vao ham clearForm vs currentCode = null, khi do moi vao dc ham subscribe
            self.currentCode = ko.observable('');

            self.dataSource = ko.observableArray([]);
            self.dataSource2 = ko.observableArray([]);

            self.columns = ko.observableArray([
                { headerText: 'コード', key: 'lineBankCode', width: 45, formatter: _.escape },
                { headerText: '名称', key: 'lineBankName', width: 120, formatter: _.escape },
                { headerText: '口座区分', key: 'accountAtr', width: 110, formatter: _.escape },
                { headerText: '口座番号', key: 'accountNo', width: 100, formatter: _.escape }
            ]);
            self.currentLineBank = ko.observable(new LineBank(null, null, null, null, 0, null, null, null, []));
            self.dirty = new nts.uk.ui.DirtyChecker(self.currentLineBank);
            self.roundingRules = ko.observableArray([
                { code: '0', name: '普通' },
                { code: '1', name: '当座' },
            ]);

            self.isFirstFindAll = ko.observable(true);
            self.isDeleteEnable = ko.observable(true);
            self.isEnable = ko.observable(false);
            self.notLoopAlert = ko.observable(true);
            self.enableBtn003 = ko.observable(false);

            self.bankName = ko.observable('');
            self.branchName = ko.observable('');

            self.countLineBank = ko.observable(0);
            self.indexLineBank = ko.observable(0);

            self.messageList = ko.observableArray([
                { messageId: "ER001", message: "＊が入力されていません。" },
                { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                { messageId: "ER007", message: "＊が選択されていません。" },
                { messageId: "ER008", message: "選択された＊は使用されているため削除できません。" },
                { messageId: "ER010", message: "対象データがありません。" },
            ]);

            self.currentCode.subscribe(function(codeChange) {
                //lan dau khong co error nen khong dc nhay vao ham clearError 
                if (!self.isFirstFindAll()) {
                    self.clearError();
                }
                //khong cho lap checkDirty khi thay doi row ma du lieu da dc chinh sua
                if (!self.notLoopAlert()) {
                    self.notLoopAlert(true);
                    return;
                }
                //khi clearForm thi se chi checkDirty o clearForm ma k checkDirty trong subscribe
                if (codeChange == null) {
                    self.setCurrentLineBank(codeChange);
                    return;
                }
                if (self.dirty.isDirty()) {
                    //"変更された内容が登録されていません。"---AL001 
                    nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                        //khi du lieu bi thay doi
                        self.setCurrentLineBank(codeChange);
                    }).ifNo(function() {
                        self.notLoopAlert(false);
                        self.currentCode(self.currentLineBank().lineBankCode());
                    });
                } else {
                    //khi du lieu k doi
                    self.setCurrentLineBank(codeChange);
                }
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

        setCurrentLineBank(codeChange) {
            var self = this;
            var lineBank = self.getLineBank(codeChange);
            self.getNameForBankAndBranch(lineBank);
            self.currentLineBank(lineBank);
            self.dirty = new nts.uk.ui.DirtyChecker(self.currentLineBank);
            self.isDeleteEnable(true);
            self.isEnable(false);
            self.indexLineBank(
                _.findIndex(self.items(), function(x) {
                    return x.lineBankCode === codeChange;
                }));
            //truong hop clearForm thi codeChange = null
            if (codeChange == null) {
                self.isDeleteEnable(false);
                self.isEnable(true);
            }
        }
        
        //get data from DB
        findAll() {
            var self = this;
            var dfd = $.Deferred();
            qmm006.a.service.findAll()
                .done(function(data) {
                    //neu co du lieu
                    if (data.length > 0) {
                        if (data.length > 1) {
                            self.enableBtn003(true);
                        } else {
                            self.enableBtn003(false);
                        }
                        self.items(data);
                        self.countLineBank(data.length);
                        //chi select row dau tien trong lan dau tien
                        if (self.isFirstFindAll()) {
                            self.dirty = new nts.uk.ui.DirtyChecker(self.currentLineBank);
                            self.currentCode(data[0].lineBankCode);
                            self.isFirstFindAll(false);
                        }
                    } else {
                        self.items([]);
                        self.clearForm();
                    }
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }


        saveData() {
            var self = this;
            var command = {
                accountAtr: self.currentLineBank().accountAtr(),
                accountNo: self.currentLineBank().accountNo(),
                bankCode: self.currentLineBank().bankCode(),
                branchCode: self.currentLineBank().branchCode(),
                consignor: [
                    { "code": self.currentLineBank().consignors()[0].consignorCode(), "memo": self.currentLineBank().consignors()[0].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[1].consignorCode(), "memo": self.currentLineBank().consignors()[1].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[2].consignorCode(), "memo": self.currentLineBank().consignors()[2].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[3].consignorCode(), "memo": self.currentLineBank().consignors()[3].consignorMemo() },
                    { "code": self.currentLineBank().consignors()[4].consignorCode(), "memo": self.currentLineBank().consignors()[4].consignorMemo() },
                ],

                lineBankCode: self.currentLineBank().lineBankCode(),
                lineBankName: self.currentLineBank().lineBankName(),
                memo: self.currentLineBank().memo(),
                requesterName: self.currentLineBank().requesterName()
            };
            qmm006.a.service.saveData(self.isEnable(), command)
                .done(function() {
                    //load lai list va chi vao row moi them
                    $.when(self.findAll()).done(function() {
                        //chi vao row moi insert hoac moi update
                        self.dirty = new nts.uk.ui.DirtyChecker(self.currentLineBank);
                        self.currentCode(command.lineBankCode);
                    });
                })
                .fail(function(error) {
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
                    } else if (error.messageId == self.messageList()[2].messageId) {
                        var message = self.messageList()[2].message;
                        if (!command.bankCode) {
                            $('#A_LBL_004').ntsError('set', message);
                        }
                        if (!command.branchCode) {
                            $('#A_LBL_007').ntsError('set', message);
                        }
                    } else if (error.messageId == self.messageList()[1].messageId) {
                        var message = self.messageList()[1].message;
                        $('#A_INP_001').ntsError('set', message);
                    }
                });
        }

        remove() {
            var self = this;
            //"データを削除します。\r\nよろしいですか？"---AL002
            nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？")
                .ifYes(function() {
                    var command = {
                        accountAtr: self.currentLineBank().accountAtr(),
                        bankCode: self.currentLineBank().bankCode(),
                        branchCode: self.currentLineBank().branchCode(),
                        consignor: [
                            { "code": self.currentLineBank().consignors()[0].consignorCode(), "memo": self.currentLineBank().consignors()[0].consignorMemo() },
                            { "code": self.currentLineBank().consignors()[1].consignorCode(), "memo": self.currentLineBank().consignors()[1].consignorMemo() },
                            { "code": self.currentLineBank().consignors()[2].consignorCode(), "memo": self.currentLineBank().consignors()[2].consignorMemo() },
                            { "code": self.currentLineBank().consignors()[3].consignorCode(), "memo": self.currentLineBank().consignors()[3].consignorMemo() },
                            { "code": self.currentLineBank().consignors()[4].consignorCode(), "memo": self.currentLineBank().consignors()[4].consignorMemo() },
                        ],
                        lineBankCode: self.currentLineBank().lineBankCode(),
                        lineBankName: self.currentLineBank().lineBankName(),
                        memo: self.currentLineBank().memo(),
                        requesterName: self.currentLineBank().requesterName()
                    };
                    qmm006.a.service.remove(command)
                        .done(function() {
                            self.findAll().done(function() {
                                if (self.countLineBank() > self.indexLineBank() && self.indexLineBank() != -1) {
                                    self.currentCode(self.items()[self.indexLineBank()].lineBankCode);
                                } else if (self.countLineBank() == self.indexLineBank()) {
                                    self.currentCode(self.items()[self.indexLineBank() - 1].lineBankCode);
                                } else if (self.indexLineBank() == -1) {
                                    self.clearForm();
                                }
                            })
                        }).fail(function(error) {
                            nts.uk.ui.dialog.alert(self.messageList()[3].message);
                        });
                })
                .ifNo(function() {
                });
        }

        openBDialog() {
            var self = this;
            qmm006.a.service.checkExistBankAndBranch()
                .done(function() {
                    var lineBank = self.currentLineBank();
                    nts.uk.ui.windows.sub.modal("/view/qmm/006/b/index.xhtml", { title: "銀行情報一覧", dialogClass: "no-close" }).onClosed(function() {
                        if (nts.uk.ui.windows.getShared("selectedBank") != null) {
                            lineBank.branchCode(nts.uk.ui.windows.getShared("selectedBank").code);
                            self.bankName(nts.uk.ui.windows.getShared("selectedBank").parentName);
                            lineBank.bankCode(nts.uk.ui.windows.getShared("selectedBank").parentCode);
                            self.branchName(nts.uk.ui.windows.getShared("selectedBank").name);
                            //chi clear error cua LBL004 & 007
                            $('#A_LBL_004').ntsError('clear');
                            $('#A_LBL_007').ntsError('clear');
                        }
                    });
                })
                .fail(function(error) {
                    nts.uk.ui.dialog.alert(self.messageList()[4].message);
                });


        }

        openCDialog() {
            var self = this;
            //neu co 0 hoac 1 row trong list thi bt003 disable
            //ng dung co the sua css trong man hinh de enable bt003
            if (self.items().length > 1) {
                if (self.dirty.isDirty()) {
                    //"変更された内容が登録されていません。"---AL001 
                    nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                        self.afterCloseCDialog();
                    }).ifNo(function() {
                    })
                } else {
                    self.afterCloseCDialog();
                }
            } else {
                nts.uk.ui.dialog.alert(self.messageList()[4].message);
                self.enableBtn003(false);
            }
        }

        afterCloseCDialog() {
            var self = this;
            nts.uk.ui.windows.sub.modal("/view/qmm/006/c/index.xhtml", { title: "振込元銀行の登録　＞　振込元銀行", dialogClass: "no-close" }).onClosed(function() {
                self.findAll().done(function() {
                    if (nts.uk.ui.windows.getShared("currentCode")) {
                        self.currentCode(nts.uk.ui.windows.getShared("currentCode"));
                    }
                });
            });
        }

        btn007() {
            //to-do
        }

        jumpToQmm002A() {
            var self = this;
            if (self.dirty.isDirty()) {
                //"変更された内容が登録されていません。"---AL001 
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function() {
                    nts.uk.request.jump("/view/qmm/002/a/index.xhtml");
                }).ifNo(function() {
                })
            } else {
                nts.uk.request.jump("/view/qmm/002/a/index.xhtml");
            }
        }

        clearForm(): any {
            var self = this;
            if (self.dirty.isDirty()) {
                //"変更された内容が登録されていません。"---AL001 
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。")
                    .ifYes(function() {
                        if (self.currentCode(null)) {
                            // clearForm  -> change data -> clearForm
                            // an clearForm roi sua du lieu roi an clearForm, hien thong bao, an Yes.
                            self.setCurrentLineBank(null);
                        } else {
                            self.currentCode(null);
                        }
                    })
                    .ifNo(function() { })
            } else {
                self.currentCode(null);
            }
        }

        clearError() {
            $('#A_INP_001').ntsError('clear');
            $('#A_INP_002').ntsError('clear');
            $('#A_INP_003').ntsError('clear');
            $('#A_LBL_004').ntsError('clear');
            $('#A_LBL_007').ntsError('clear');
            $('#A_INP_004').ntsError('clear');
        }

        getLineBank(curCode) {
            var self = this;
            let data = _.find(self.items(), function(x) {
                return x.lineBankCode === curCode;
            });
            if (!data) {
                return new LineBank(null, null, null, null, 0, null, null, null, []);
            }
            return new LineBank(data.bankCode, data.branchCode, data.lineBankCode, data.lineBankName,
                data.accountAtr, data.accountNo, data.memo, data.requesterName, data.consignors);
        }
        
        //get gia tri cho LBL_005 va LBL_007(bankName and branchName)
        getNameForBankAndBranch(lineBank) {
            var self = this;
            //find bankName
            var tmp = _.find(this.dataSource2(), function(x) {
                return x.code === lineBank.bankCode();
            });
            //find branchName
            var tmp1 = _.find(self.dataSource2(), function(x) {
                return x.code === lineBank.branchCode() && x.parentCode === lineBank.bankCode();
            });
            if (tmp != undefined) {
                self.bankName(tmp.name);
            } else {
                self.bankName('');
            }
            if (tmp1 != undefined) {
                self.branchName(tmp1.name);
            } else {
                self.branchName('');
            }
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
