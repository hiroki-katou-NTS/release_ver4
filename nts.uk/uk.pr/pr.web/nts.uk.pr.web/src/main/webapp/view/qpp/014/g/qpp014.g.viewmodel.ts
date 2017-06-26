module qpp014.g.viewmodel {
    export class ScreenModel {
        dataBankBranch: KnockoutObservableArray<any>;
        dataBankBranch2: KnockoutObservableArray<any>;
        dataLineBank: KnockoutObservableArray<any>;
        g_INP_001: KnockoutObservable<any>;
        g_SEL_001_items: KnockoutObservableArray<ItemModel_G_SEL_001>;
        g_SEL_001_itemSelected: KnockoutObservable<any>;
        g_SEL_002_items: KnockoutObservableArray<ItemModel_G_SEL_002>;
        g_SEL_002_itemSelected: KnockoutObservable<any>;
        g_INP_002: any;
        g_SEL_003_itemSelected: KnockoutObservable<any>;
        yearMonthDateInJapanEmpire: any;
        processingDate: any;
        processingDateInJapanEmprire: any;
        processingNo: any;
        processingName: any;
        accountAtr: KnockoutObservable<number>;
        accountNo: KnockoutObservable<number>;
  
        constructor(data: any) {
            let self = this;
            self.dataBankBranch = ko.observableArray([]);
            self.dataBankBranch2 = ko.observableArray([]);
            self.dataLineBank = ko.observableArray([]);
            self.g_INP_001 = ko.observable();
            self.g_SEL_001_items = ko.observableArray([]);
            self.g_SEL_002_items = ko.observableArray([]);
            self.g_SEL_002_itemSelected = ko.observable();
            self.g_SEL_001_itemSelected = ko.observable();
            self.accountAtr = ko.observable(0);
            self.accountNo = ko.observable(0);
            self.g_INP_002 = {
                value: ko.observable(12)
            };
            self.processingDate = ko.observable(nts.uk.time.formatYearMonth(data.currentProcessingYm));
            self.processingDateInJapanEmprire = ko.computed(function() {
                return nts.uk.time.yearmonthInJapanEmpire(self.processingDate()).toString();
            });
            self.g_SEL_003_itemSelected = ko.observable(1);
            self.yearMonthDateInJapanEmpire = ko.computed(function() {
                if (self.g_INP_001() == undefined || self.g_INP_001() == null || self.g_INP_001() == "") {
                    return '';
                }
                return "(" + nts.uk.time.yearInJapanEmpire(moment(self.g_INP_001()).format('YYYY')).toString() +
                    moment(self.g_INP_001()).format('MM') + " 月 " + moment(self.g_INP_001()).format('DD') + " 日)";
            });
            self.processingNo = ko.observable(data.processingNo + ' : ');
            self.processingName = ko.observable(data.processingName + ' )');
            $.when(self.findAllBankBranch()).done(function() {
                self.findAllLineBank().done(function() {
                    var tmp = [];
                    var tmp1 = null;
                    for (var i = 0; i < self.dataLineBank().length; i++) {
                        tmp1 = _.find(self.dataBankBranch2(), function(x) {
                            return x.branchId == self.dataLineBank()[i].branchId;
                        });
                        tmp.push(new ItemModel_G_SEL_001(self.dataLineBank()[i].lineBankCode, self.dataLineBank()[i].lineBankName, tmp1.code));
                    }
                    self.g_SEL_001_items(tmp);
                });
            });
            
            self.g_SEL_001_itemSelected.subscribe(function(newValue : any) {
                var tmp = _.find(self.dataLineBank(), function(x) {
                    return x.lineBankCode == newValue;
                });
                self.accountAtr(tmp.accountAtr);
                self.accountNo(tmp.accountNo);
                self.g_SEL_002_items(tmp.consignors);
            });
        }

        /**
        * get data from DB BRANCH
        */
        findAllBankBranch(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            qpp014.g.service.findAllBankBranch()
                .done(function(dataBr) {
                    var bankData = [];
                    _.forEach(dataBr, function(item) {
                        var childs = _.map(item.bankBranch, function(itemChild: any) {
                            return new BankBranch(itemChild.bankBranchCode, itemChild.bankBranchID, itemChild.bankBranchName, item.bankCode, item.bankName, item.bankCode + itemChild.bankBranchCode, []);
                        });
                        bankData.push(new BankBranch(item.bankCode, null, item.bankName, null, null, item.bankCode, childs));
                    });
                    self.dataBankBranch(bankData);
                    self.dataBankBranch2(nts.uk.util.flatArray(self.dataBankBranch(), "childs"));
                    dfd.resolve();
                })
                .fail(function() {
                    dfd.reject();
                });
            return dfd.promise();
        }

        /**
         * find all lineBank from DB LINE_BANK
         */
        findAllLineBank(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            qpp014.g.service.findAllLineBank()
                .done(function(dataLB) {
                    if (dataLB.length > 0) {
                        self.dataLineBank(dataLB);
                    } else {
                        nts.uk.ui.dialog.alert("対象データがありません。");//ER010
                    }
                    dfd.resolve();
                })
                .fail(function() {
                    dfd.reject();
                });
            return dfd.promise();
        }

        /**
         * open dialog I
         */
        openIDialog() {
            var self = this;
            nts.uk.ui.windows.setShared("processingDateInJapanEmprire", self.processingDateInJapanEmprire(), true);
            nts.uk.ui.windows.setShared("label", self.g_SEL_001_items()[_.findIndex(self.g_SEL_001_items(), function(x) {
                return x.code === self.g_SEL_001_itemSelected();
            })].label, true);
            nts.uk.ui.windows.sub.modal("/view/qpp/014/i/index.xhtml", { title: "振込データテキスト出力結果一覧", dialogClass: "no-close" }).onClosed(function() {
            });
        }
    }

    export class ItemModel_G_SEL_001 {
        code: string;
        name: string;
        branchCode: string;
        label: string;

        constructor(code: string, name: string, branchCode: string) {
            this.code = code;
            this.name = name;
            this.branchCode = branchCode;
            this.label = ' ' + this.code + ' - ' + this.branchCode + ' ' + this.name;
        }
    }
    export class ItemModel_G_SEL_002 {
        code: string;

        constructor(code: string) {
            this.code = code;
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
        branchId: string;
        constructor(code: string, branchId: string, name: string, parentCode: string, parentName: string, treeCode: string, childs: Array<BankBranch>) {
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
