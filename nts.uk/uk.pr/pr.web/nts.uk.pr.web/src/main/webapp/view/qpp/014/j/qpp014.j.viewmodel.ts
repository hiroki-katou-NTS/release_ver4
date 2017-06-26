module qpp014.j {
    export class ScreenModel {
        dataBankBranch: KnockoutObservableArray<any>;
        dataBankBranch2: KnockoutObservableArray<any>;
        dataLineBank: KnockoutObservableArray<any>;
        //radiogroup
        selectedId_J_SEL_001: KnockoutObservable<number>;
        itemList_J_SEL_001: KnockoutObservableArray<BoxModel_J_SEL_001>;
        //combobox
        //J_SEL_002
        itemList_J_SEL_002: KnockoutObservableArray<ItemModel_J_SEL_002>;
        selectedCode_J_SEL_002: KnockoutObservable<ItemModel_J_SEL_002>;
        //J_SEL_003
        itemList_J_SEL_003: KnockoutObservableArray<ItemModel_J_SEL_002>;
        selectedCode_J_SEL_003: KnockoutObservable<ItemModel_J_SEL_002>;
        //gridview
        items_J_LST_001: KnockoutObservableArray<ItemModel_J_LST_001>;
        currentCode_J_LST_001: KnockoutObservable<any>;
        currentCode_J_SEL_004: KnockoutObservable<any>;
        currentProcessingYm: any;
        dateOfPayment: any;
        isEnable: KnockoutObservable<boolean>;
        isHidden: KnockoutObservable<boolean>;

        constructor() {
            let self = this;
            self.dataBankBranch = ko.observableArray([]);
            self.dataBankBranch2 = ko.observableArray([]);
            self.dataLineBank = ko.observableArray([]);

            self.itemList_J_SEL_001 = ko.observableArray([
                new BoxModel_J_SEL_001(0, '銀行集計'),
                new BoxModel_J_SEL_001(1, '明細出力')
            ]);

            self.selectedId_J_SEL_001 = ko.observable(0);

            self.itemList_J_SEL_002 = ko.observableArray([
                new ItemModel_J_SEL_002('振込先順 '),
                new ItemModel_J_SEL_002('個人　コード順'),
            ]);

            self.selectedCode_J_SEL_002 = ko.observable(self.itemList_J_SEL_002()[0]);

            self.itemList_J_SEL_003 = ko.observableArray([
                new ItemModel_J_SEL_002('漢字出力'),
                new ItemModel_J_SEL_002('カナ出力'),
            ]);
            self.selectedCode_J_SEL_003 = ko.observable(self.itemList_J_SEL_003()[0]);
            self.isEnable = ko.observable(false);
            self.items_J_LST_001 = ko.observableArray([]);
            self.currentCode_J_LST_001 = ko.observable();
            self.currentCode_J_SEL_004 = ko.observable(1);

            self.currentProcessingYm = nts.uk.time.parseYearMonth(nts.uk.ui.windows.getShared("data").currentProcessingYm).format() + "(" +
                nts.uk.time.yearmonthInJapanEmpire(nts.uk.time.parseYearMonth(nts.uk.ui.windows.getShared("data").currentProcessingYm).format()) + ")";

            self.dateOfPayment = ko.observable(moment(nts.uk.ui.windows.getShared("dateOfPayment")).format("YYYY/MM/DD") +
                "(" + nts.uk.time.yearmonthInJapanEmpire(moment(nts.uk.ui.windows.getShared("dateOfPayment")).format("YYYY/MM")).toString() + " " +
                moment(nts.uk.ui.windows.getShared("dateOfPayment")).format("DD") + " 日)");

            self.isEnable = ko.computed(function() {
                return self.selectedId_J_SEL_001() == 0 ? false : true;
            });

            if (nts.uk.ui.windows.getShared("sparePayAtr") != 3) {
                $('#J_SEL_004').css('display', 'none');
                $('#J_LBL_009').css('display', 'none');
            } else {
                $('#J_SEL_004').css('display', '');
                $('#J_LBL_009').css('display', '');
            }

            $.when(self.findAllBankBranch()).done(function() {
                self.findAllLineBank().done(function() {
                    for (let i = 0; i < self.dataLineBank().length; i++) {
                        var tmp = _.find(self.dataBankBranch2(), function(x) {
                            return x.branchId === self.dataLineBank()[i].branchId;
                        });
                        self.items_J_LST_001.push(new ItemModel_J_LST_001(self.dataLineBank()[i].lineBankCode, tmp.code,
                            self.dataLineBank()[i].lineBankName, tmp.name, self.dataLineBank()[i].accountAtr, self.dataLineBank()[i].accountNo, self.dataLineBank()[i].branchId));
                    }
                });
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
         * Close dialog
         */
        closeDialog(): void {
            nts.uk.ui.windows.close();
        }

        /**
         * Print file PDF
         */
        saveAsPdf(): void {
            var self = this;
            if (self.currentCode_J_LST_001() == undefined || self.currentCode_J_LST_001().length < 1) {
                nts.uk.ui.dialog.alert("振込元銀行が選択されていません。");//ER007
            } else {
                var branchIdList = [];
                for (let i = 0; i < self.currentCode_J_LST_001().length; i++) {
                    branchIdList.push(
                        _.find(self.items_J_LST_001(), function(x) {
                            return x.label === self.currentCode_J_LST_001()[i];
                        }).branchId);
                }
                var command = {
                    fromBranchId: branchIdList,
                    processingNo: nts.uk.ui.windows.getShared("processingNo"),
                    processingYm: nts.uk.ui.windows.getShared("data").currentProcessingYm,
                    payDate: moment.utc(nts.uk.ui.windows.getShared("dateOfPayment"), "YYYY/MM/DD").toISOString(),
                    sparePayAtr: nts.uk.ui.windows.getShared("sparePayAtr"),
                    selectedId_J_SEL_001: self.selectedId_J_SEL_001(),
                    currentCode_J_SEL_004: self.currentCode_J_SEL_004(),
                    transferDate: nts.uk.time.yearmonthInJapanEmpire(moment(nts.uk.ui.windows.getShared("dateOfPayment")).format("YYYY/MM")).toString() + " "
                    + moment(nts.uk.ui.windows.getShared("dateOfPayment")).format("DD") + " 日"
                };
                if (self.selectedId_J_SEL_001() == 1) {
                    qpp014.j.service.saveAsPdfA(command)
                        .done(function() { })
                        .fail(function(error) {
                            if (error.messageId == 'ER010') {
                                nts.uk.ui.dialog.alert("対象データがありません。");
                            }
                        });
                } else {
                    qpp014.j.service.saveAsPdfB(command)
                        .done(function() { })
                        .fail(function(error) {
                            if (error.messageId == 'ER010') {
                                nts.uk.ui.dialog.alert("対象データがありません。");
                            }
                        });
                }
            }
        }
    }
    export class BoxModel_J_SEL_001 {
        id: number;
        name: string;
        constructor(id, name) {
            let self = this;
            self.id = id;
            self.name = name;
        }
    }

    export class ItemModel_J_SEL_002 {
        name: string;

        constructor(name: string) {
            this.name = name;
        }
    }

    export class ItemModel_J_LST_001 {
        lineBankCode: string;
        branchCode: string;
        lineBankName: string;
        branchName: string;
        accountAtr: number;
        accountNo: string;
        branchId: string;
        label: string;

        constructor(lineBankCode: string, branchCode: string, lineBankName: string, branchName: string, accountAtr: number, accountNo: string, branchId: string) {
            this.lineBankCode = lineBankCode;
            this.branchCode = branchCode;
            this.lineBankName = lineBankName;
            this.branchName = branchName;
            this.accountAtr = accountAtr;
            this.accountNo = accountNo;
            this.branchId = branchId;
            this.label = this.lineBankCode + ' - ' + this.branchCode;
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
