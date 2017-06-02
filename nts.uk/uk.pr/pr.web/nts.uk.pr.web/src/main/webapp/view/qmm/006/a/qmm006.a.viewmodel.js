var qmm006;
(function (qmm006) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
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
                    self.currentLineBank = ko.observable(new LineBank(null, null, null, 0, null, null, null, []));
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
                    self.isNotCheckDirty = ko.observable(false);
                    self.bankCode = ko.observable('');
                    self.branchCode = ko.observable('');
                    self.bankName = ko.observable('');
                    self.branchName = ko.observable('');
                    self.countLineBank = ko.observable(0);
                    self.indexLineBank = ko.observable(0);
                    self.messageList = ko.observableArray([
                        { messageId: "ER001", message: "が入力されていません。" },
                        { messageId: "ER005", message: "入力したコードは既に存在しています。\r\n コードを確認してください。" },
                        { messageId: "ER007", message: "が選択されていません。" },
                        { messageId: "ER008", message: "選択された{0}は使用されているため削除できません。" },
                        { messageId: "ER010", message: "対象データがありません。" },
                    ]);
                    self.currentCode.subscribe(function (codeChange) {
                        if (self.dirty.isDirty()) {
                            // dont allow loop checkDirty when change data and change row 
                            if (!self.notLoopAlert()) {
                                self.notLoopAlert(true);
                                return;
                            }
                            // only checkDirty in clearForm, not checkDirty in subscribe
                            // when remove(), not check dirty
                            if (codeChange == null || self.isNotCheckDirty()) {
                                self.isNotCheckDirty(false);
                                self.setCurrentLineBank(codeChange);
                                return;
                            }
                            //"変更された内容が登録されていません。"---AL001 
                            nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function () {
                                self.clearError();
                                //data is changed
                                self.setCurrentLineBank(codeChange);
                            }).ifCancel(function () {
                                self.notLoopAlert(false);
                                self.currentCode(self.currentLineBank().lineBankCode());
                            });
                        }
                        else {
                            // no error in the first findAll(), so dont allow jump to clearError() 
                            if (!self.isFirstFindAll()) {
                                self.clearError();
                            }
                            // dont allow loop checkDirty when change data and change row 
                            if (!self.notLoopAlert()) {
                                self.notLoopAlert(true);
                                return;
                            }
                            // only checkDirty in clearForm, not checkDirty in subscribe
                            // when remove(), not check dirty
                            if (codeChange == null || self.isNotCheckDirty()) {
                                self.isNotCheckDirty(false);
                                self.setCurrentLineBank(codeChange);
                                return;
                            }
                            //data isn't changed
                            self.setCurrentLineBank(codeChange);
                        }
                    });
                }
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    $.when(self.findBankAll()).done(function () {
                        self.findAll().done(function () {
                            dfd.resolve();
                        }).fail(function (res) {
                            dfd.reject(res);
                        });
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                    return dfd.promise();
                };
                /**
                 * get data from database, set into screen
                 */
                ScreenModel.prototype.setCurrentLineBank = function (codeChange) {
                    var self = this;
                    var lineBank = self.getLineBank(codeChange);
                    self.getInfoBankBranch(lineBank);
                    self.currentLineBank(lineBank);
                    self.dirty.reset();
                    self.isDeleteEnable(true);
                    self.isEnable(false);
                    self.indexLineBank(_.findIndex(self.items(), function (x) {
                        return x.lineBankCode === codeChange;
                    }));
                    //when clearForm, codeChange = null
                    if (codeChange == null) {
                        self.isDeleteEnable(false);
                        self.isEnable(true);
                    }
                };
                /**
                 * get data from DB
                 */
                ScreenModel.prototype.findAll = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    qmm006.a.service.findAll()
                        .done(function (data) {
                        self.countLineBank(data.length);
                        //if data exist
                        if (data.length > 0) {
                            if (data.length > 1) {
                                self.enableBtn003(true);
                            }
                            else {
                                self.enableBtn003(false);
                            }
                            self.items(data);
                            //only select first row in the first
                            if (self.isFirstFindAll()) {
                                self.dirty.reset();
                                self.currentCode(data[0].lineBankCode);
                                self.isFirstFindAll(false);
                            }
                        }
                        else {
                            self.items([]);
                            if (self.isFirstFindAll()) {
                                self.clearForm();
                                self.isFirstFindAll(false);
                            }
                        }
                        dfd.resolve();
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                    return dfd.promise();
                };
                /**
                 * save data from screen to database
                 */
                ScreenModel.prototype.saveData = function () {
                    var self = this;
                    //if input 0-9, auto insert '0' before
                    if (self.currentLineBank().lineBankCode() != null && self.currentLineBank().lineBankCode().length == 1) {
                        self.currentLineBank().lineBankCode("0" + self.currentLineBank().lineBankCode());
                    }
                    var command = {
                        accountAtr: self.currentLineBank().accountAtr(),
                        accountNo: self.currentLineBank().accountNo(),
                        branchId: self.currentLineBank().branchId(),
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
                        .done(function () {
                        //load data and select new data
                        $.when(self.findAll()).done(function () {
                            //select new data which is inserted or updated
                            self.dirty.reset();
                            self.currentCode(command.lineBankCode);
                        });
                    })
                        .fail(function (error) {
                        if (error.messageId == self.messageList()[0].messageId) {
                            var message = self.messageList()[0].message;
                            if (!command.lineBankCode) {
                                $('#inp_lineBankCode').ntsError('set', message);
                            }
                            if (!command.lineBankName) {
                                $('#inp_lineBankName').ntsError('set', message);
                            }
                            if (!command.accountNo) {
                                $('#inp_accountNumber').ntsError('set', message);
                            }
                        }
                        else if (error.messageId == self.messageList()[2].messageId) {
                            var message = self.messageList()[2].message;
                            if (!command.branchId) {
                                $('#lbl_bankCode').ntsError('set', message);
                                $('#lbl_branchCode').ntsError('set', message);
                            }
                        }
                        else if (error.messageId == self.messageList()[1].messageId) {
                            var message = self.messageList()[1].message;
                            $('#inp_lineBankCode').ntsError('set', message);
                        }
                    });
                };
                /**
                 * remove data in dababase
                 */
                ScreenModel.prototype.remove = function () {
                    var self = this;
                    nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？") //AL002
                        .ifYes(function () {
                        var command = {
                            lineBankCode: self.currentLineBank().lineBankCode(),
                        };
                        self.isNotCheckDirty(true);
                        qmm006.a.service.remove(command)
                            .done(function () {
                            self.findAll().done(function () {
                                if (self.countLineBank() == 0) {
                                    self.clearForm();
                                }
                                else if (self.countLineBank() > self.indexLineBank()) {
                                    self.currentCode(self.items()[self.indexLineBank()].lineBankCode);
                                }
                                else if (self.countLineBank() == self.indexLineBank()) {
                                    self.currentCode(self.items()[self.indexLineBank() - 1].lineBankCode);
                                }
                            });
                        }).fail(function (error) {
                            if (error.messageId == self.messageList()[3].messageId) {
                                self.isNotCheckDirty(false);
                                var messageError = nts.uk.text.format(self.messageList()[3].message, self.currentLineBank().lineBankName());
                                nts.uk.ui.dialog.alert(messageError);
                            }
                        });
                    })
                        .ifCancel(function () {
                    });
                };
                /**
                 * open B dialog
                 */
                ScreenModel.prototype.openBDialog = function () {
                    var self = this;
                    qmm006.a.service.checkExistBankAndBranch()
                        .done(function () {
                        var lineBank = self.currentLineBank();
                        nts.uk.ui.windows.sub.modal("/view/qmm/006/b/index.xhtml", { title: "銀行情報一覧", dialogClass: "no-close" }).onClosed(function () {
                            if (nts.uk.ui.windows.getShared("selectedBank") != null) {
                                self.branchCode(nts.uk.ui.windows.getShared("selectedBank").code);
                                self.bankName(nts.uk.ui.windows.getShared("selectedBank").parentName);
                                self.bankCode(nts.uk.ui.windows.getShared("selectedBank").parentCode);
                                self.branchName(nts.uk.ui.windows.getShared("selectedBank").name);
                                lineBank.branchId(nts.uk.ui.windows.getShared("selectedBank").branchId);
                                //only clear error of LBL004 & LBL007
                                $('#lbl_bankCode').ntsError('clear');
                                $('#lbl_branchCode').ntsError('clear');
                            }
                        });
                    })
                        .fail(function (error) {
                        nts.uk.ui.dialog.alert(self.messageList()[4].message);
                    });
                };
                /**
                 * open C dialog
                 */
                ScreenModel.prototype.openCDialog = function () {
                    var self = this;
                    //bt003 disable if list has 1 row or less
                    //in case user can fix css in screen to enable bt003
                    if (self.items().length > 1) {
                        if (self.dirty.isDirty()) {
                            //AL001 
                            nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function () {
                                //delete the data being changed
                                self.setCurrentLineBank(self.currentCode());
                                self.afterCloseCDialog();
                            }).ifCancel(function () {
                            });
                        }
                        else {
                            self.afterCloseCDialog();
                        }
                    }
                    else {
                        nts.uk.ui.dialog.alert(self.messageList()[4].message);
                        self.enableBtn003(false);
                    }
                };
                /**
                 * set value for currentCode property
                 */
                ScreenModel.prototype.afterCloseCDialog = function () {
                    var self = this;
                    nts.uk.ui.windows.sub.modal("/view/qmm/006/c/index.xhtml", { title: "振込元銀行の登録　＞　振込元銀行", dialogClass: "no-close" }).onClosed(function () {
                        self.findAll().done(function () {
                            if (nts.uk.ui.windows.getShared("currentCode")) {
                                self.currentCode(nts.uk.ui.windows.getShared("currentCode"));
                            }
                        });
                    });
                };
                ScreenModel.prototype.btn007 = function () {
                    //to-do
                };
                /**
                 * go to screen A of QMM002
                 */
                ScreenModel.prototype.jumpToQmm002A = function () {
                    var self = this;
                    if (self.dirty.isDirty()) {
                        //AL001 
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function () {
                            nts.uk.request.jump("/view/qmm/002/a/index.xhtml");
                        }).ifCancel(function () {
                        });
                    }
                    else {
                        nts.uk.request.jump("/view/qmm/002/a/index.xhtml");
                    }
                };
                /**
                 * clear data on screen, new mode
                 */
                ScreenModel.prototype.clearForm = function () {
                    var self = this;
                    if (self.dirty.isDirty() && !self.isNotCheckDirty()) {
                        //AL001 
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。")
                            .ifYes(function () {
                            if (self.currentCode(null)) {
                                // clearForm  -> change data -> clearForm
                                // press button clearForm, fix data and press button clearForm again, display notice, press Yes.
                                self.clearError();
                                self.setCurrentLineBank(null);
                            }
                            else {
                                self.currentCode(null);
                            }
                        })
                            .ifCancel(function () { });
                    }
                    else {
                        self.currentCode(null);
                    }
                };
                /**
                 * clear error on screen
                 */
                ScreenModel.prototype.clearError = function () {
                    $('#inp_lineBankCode').ntsError('clear');
                    $('#inp_lineBankName').ntsError('clear');
                    $('#inp_accountNumber').ntsError('clear');
                    $('#lbl_bankCode').ntsError('clear');
                    $('#lbl_branchCode').ntsError('clear');
                    $('#inp_transferRequestName').ntsError('clear');
                    $('#inp_memo').ntsError('clear');
                    $('.consignor').ntsError('clear');
                };
                /**
                 * get data base-on currentCode
                 */
                ScreenModel.prototype.getLineBank = function (curCode) {
                    var self = this;
                    var data = _.find(self.items(), function (x) {
                        return x.lineBankCode === curCode;
                    });
                    if (!data) {
                        return new LineBank(null, null, null, 0, null, null, null, []);
                    }
                    return new LineBank(data.branchId, data.lineBankCode, data.lineBankName, data.accountAtr, data.accountNo, data.memo, data.requesterName, data.consignors);
                };
                /**
                 * get data for bankName, bankCode, branchName, branchCode
                 */
                ScreenModel.prototype.getInfoBankBranch = function (lineBank) {
                    var self = this;
                    if (lineBank.branchId() == null) {
                        self.bankCode('');
                        self.bankName('');
                        self.branchCode('');
                        self.branchName('');
                    }
                    else {
                        //find bankName, bankCode, branchName, branchCode
                        var tmp = _.find(this.dataSource2(), function (x) {
                            return x.branchId === lineBank.branchId();
                        });
                        self.bankCode(tmp.parentCode);
                        self.bankName(tmp.parentName);
                        self.branchCode(tmp.code);
                        self.branchName(tmp.name);
                    }
                };
                /**
                 * get info of Bank from database
                 */
                ScreenModel.prototype.findBankAll = function () {
                    var self = this;
                    var lineBank = self.currentLineBank();
                    var dfd = $.Deferred();
                    qmm006.a.service.findBankAll()
                        .done(function (data) {
                        if (data.length > 0) {
                            var bankData = [];
                            _.forEach(data, function (item) {
                                var childs = _.map(item.bankBranch, function (itemChild) {
                                    return new BankBranch(itemChild.bankBranchCode, itemChild.bankBranchID, itemChild.bankBranchName, item.bankCode, item.bankName, item.bankCode + itemChild.bankBranchCode, []);
                                });
                                bankData.push(new BankBranch(item.bankCode, null, item.bankName, null, null, item.bankCode, childs));
                            });
                            self.dataSource(bankData);
                            self.dataSource2(nts.uk.util.flatArray(self.dataSource(), "childs"));
                        }
                        dfd.resolve();
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                    return dfd.promise();
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var LineBank = (function () {
                function LineBank(branchId, lineBankCode, lineBankName, accountAtr, accountNo, memo, requesterName, consignors) {
                    this.branchId = ko.observable(branchId);
                    this.lineBankCode = ko.observable(lineBankCode);
                    this.lineBankName = ko.observable(lineBankName);
                    this.accountAtr = ko.observable(accountAtr);
                    this.accountNo = ko.observable(accountNo);
                    this.memo = ko.observable(memo);
                    this.requesterName = ko.observable(requesterName);
                    this.consignors = ko.observableArray([]);
                    this.branchId = ko.observable(branchId);
                    for (var i = 0; i <= 4; i++) {
                        var consignorItem = consignors[i];
                        switch (i) {
                            case 0:
                                this.createConsignorItem(consignorItem, "①");
                                break;
                            case 1:
                                this.createConsignorItem(consignorItem, "②");
                                break;
                            case 2:
                                this.createConsignorItem(consignorItem, "③");
                                break;
                            case 3:
                                this.createConsignorItem(consignorItem, "④");
                                break;
                            case 4:
                                this.createConsignorItem(consignorItem, "⑤");
                                break;
                            default:
                                break;
                        }
                    }
                }
                /**
                 * create consignor on screen
                 */
                LineBank.prototype.createConsignorItem = function (item, icon) {
                    var self = this;
                    if (item) {
                        self.consignors.push(new Consignor(icon, item.code, item.memo));
                    }
                    else {
                        self.consignors.push(new Consignor(icon, "", ""));
                    }
                };
                return LineBank;
            }());
            var Consignor = (function () {
                function Consignor(noIcon, consignorCode, consignorMemo) {
                    this.noIcon = ko.observable(noIcon);
                    this.consignorCode = ko.observable(consignorCode);
                    this.consignorMemo = ko.observable(consignorMemo);
                }
                return Consignor;
            }());
            var BankBranch = (function () {
                function BankBranch(code, branchId, name, parentCode, parentName, treeCode, childs) {
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
                return BankBranch;
            }());
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qmm006.a || (qmm006.a = {}));
})(qmm006 || (qmm006 = {}));
//# sourceMappingURL=qmm006.a.viewmodel.js.map