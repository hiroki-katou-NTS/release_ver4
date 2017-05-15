var qpp014;
(function (qpp014) {
    var g;
    (function (g) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel(data) {
                    var self = this;
                    self.dataBankBranch = ko.observableArray([]);
                    self.dataBankBranch2 = ko.observableArray([]);
                    self.dataLineBank = ko.observableArray([]);
                    self.g_INP_001 = ko.observable();
                    self.g_SEL_001_items = ko.observableArray([]);
                    self.g_SEL_002_items = ko.observableArray([]);
                    self.g_SEL_002_itemSelected = ko.observable();
                    self.g_SEL_001_itemSelected = ko.observable(self.g_SEL_001_items()[0]);
                    self.accountAtr = ko.observable(0);
                    self.accountNo = ko.observable(0);
                    self.g_INP_002 = {
                        value: ko.observable(12)
                    };
                    self.processingDate = ko.observable(nts.uk.time.formatYearMonth(data.currentProcessingYm));
                    self.processingDateInJapanEmprire = ko.computed(function () {
                        return nts.uk.time.yearmonthInJapanEmpire(self.processingDate()).toString();
                    });
                    self.g_SEL_003_itemSelected = ko.observable(1);
                    self.yearMonthDateInJapanEmpire = ko.computed(function () {
                        if (self.g_INP_001() == undefined || self.g_INP_001() == null || self.g_INP_001() == "") {
                            return '';
                        }
                        return "(" + nts.uk.time.yearInJapanEmpire(moment(self.g_INP_001()).format('YYYY')).toString() +
                            moment(self.g_INP_001()).format('MM') + " 月 " + moment(self.g_INP_001()).format('DD') + " 日)";
                    });
                    self.processingNo = ko.observable(data.processingNo + ' : ');
                    self.processingName = ko.observable(data.processingName + ' )');
                    $.when(self.findAllBankBranch()).done(function () {
                        self.findAllLineBank().done(function () {
                            var tmp = [];
                            var tmp1 = null;
                            if (self.dataLineBank().length > 0) {
                                for (var i = 0; i < self.dataLineBank().length; i++) {
                                    tmp1 = _.find(self.dataBankBranch2(), function (x) {
                                        return x.branchId == self.dataLineBank()[i].branchId;
                                    });
                                    tmp.push(new ItemModel_G_SEL_001(self.dataLineBank()[i].lineBankCode, self.dataLineBank()[i].lineBankName, tmp1.code));
                                }
                            }
                            else {
                                nts.uk.ui.dialog.alert("対象データがありません。");
                            }
                            self.g_SEL_001_items(tmp);
                        });
                    });
                    self.g_SEL_001_itemSelected.subscribe(function () {
                        var tmp = _.find(self.dataLineBank(), function (x) {
                            return x.lineBankCode == self.g_SEL_001_itemSelected();
                        });
                        self.accountAtr(tmp.accountAtr);
                        self.accountNo(tmp.accountNo);
                        self.g_SEL_002_items(tmp.consignors);
                        self.g_SEL_002_itemSelected(self.g_SEL_002_items()[0]);
                    });
                }
                ScreenModel.prototype.findAllBankBranch = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    qpp014.g.service.findAllBankBranch()
                        .done(function (dataBr) {
                        var bankData = [];
                        _.forEach(dataBr, function (item) {
                            var childs = _.map(item.bankBranch, function (itemChild) {
                                return new BankBranch(itemChild.bankBranchCode, itemChild.bankBranchID, itemChild.bankBranchName, item.bankCode, item.bankName, item.bankCode + itemChild.bankBranchCode, []);
                            });
                            bankData.push(new BankBranch(item.bankCode, null, item.bankName, null, null, item.bankCode, childs));
                        });
                        self.dataBankBranch(bankData);
                        self.dataBankBranch2(nts.uk.util.flatArray(self.dataBankBranch(), "childs"));
                        dfd.resolve();
                    })
                        .fail(function () {
                        dfd.reject();
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.findAllLineBank = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    qpp014.g.service.findAllLineBank()
                        .done(function (dataLB) {
                        self.dataLineBank(dataLB);
                        dfd.resolve();
                    })
                        .fail(function () {
                        dfd.reject();
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.openIDialog = function () {
                    var self = this;
                    nts.uk.ui.windows.setShared("processingDateInJapanEmprire", self.processingDateInJapanEmprire(), true);
                    nts.uk.ui.windows.setShared("label", self.g_SEL_001_items()[_.findIndex(self.g_SEL_001_items(), function (x) {
                        return x.code === self.g_SEL_001_itemSelected();
                    })].label, true);
                    nts.uk.ui.windows.sub.modal("/view/qpp/014/i/index.xhtml", { title: "振込データテキスト出力結果一覧", dialogClass: "no-close" }).onClosed(function () {
                    });
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var ItemModel_G_SEL_001 = (function () {
                function ItemModel_G_SEL_001(code, name, branchCode) {
                    this.code = code;
                    this.name = name;
                    this.branchCode = branchCode;
                    this.label = ' ' + this.code + ' - ' + this.branchCode + ' ' + this.name;
                }
                return ItemModel_G_SEL_001;
            }());
            viewmodel.ItemModel_G_SEL_001 = ItemModel_G_SEL_001;
            var ItemModel_G_SEL_002 = (function () {
                function ItemModel_G_SEL_002(code) {
                    this.code = code;
                }
                return ItemModel_G_SEL_002;
            }());
            viewmodel.ItemModel_G_SEL_002 = ItemModel_G_SEL_002;
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
        })(viewmodel = g.viewmodel || (g.viewmodel = {}));
    })(g = qpp014.g || (qpp014.g = {}));
})(qpp014 || (qpp014 = {}));
;
//# sourceMappingURL=qpp014.g.viewmodel.js.map