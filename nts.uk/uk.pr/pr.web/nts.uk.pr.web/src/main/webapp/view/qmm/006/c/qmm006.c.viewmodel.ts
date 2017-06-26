module qmm006.c.viewmodel {
    export class ScreenModel {
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        items: KnockoutObservableArray<any>;
        items1: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;
        currentCode1: KnockoutObservable<any>;

        constructor() {
            var self = this;
            self.currentCode = ko.observable();
            self.currentCode1 = ko.observable();
            self.items = ko.observableArray([]);
            self.items1 = ko.observableArray([]);

            self.columns = ko.observableArray([
                { headerText: 'コード', key: 'lineBankCode', width: 45, formatter: _.escape },
                { headerText: '名称', key: 'lineBankName', width: 120, formatter: _.escape },
                { headerText: '口座区分', key: 'accountAtr', width: 110, formatter: _.escape },
                { headerText: '口座番号', key: 'accountNo', width: 100, formatter: _.escape }
            ]);
        }

        startPage(): JQueryPromise<any> {
            return this.findAll();
        }

        /**
         * get data from database
         */
        findAll(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            qmm006.c.service.findAll()
                .done(function(data) {
                    if (data.length > 1) {
                        self.items(data);
                        self.items1(data);
                    } else {
                        //ER010- data is none
                        nts.uk.ui.dialog.alert("対象データがありません。");
                    }
                    dfd.resolve();
                }).fail(function() { });
            return dfd.promise();

        }

        /**
         * forward property 'currentCode' to screen A, close dialog
         */
        transferData(data, newLineBankCode): void {
            service.transfer(data)
                .done(function() {
                    nts.uk.ui.windows.setShared("currentCode", newLineBankCode, true);
                    nts.uk.ui.windows.close();
                })
                .fail(function(error) {
                    nts.uk.ui.dialog.alert(error.message);
                });
        }

        /**
         * change lineBankCode in database PERSON_BANK_ACCOUNT base-on data on screen
         */
        transfer(): void {
            var self = this;
            var oldLineBankCode = self.currentCode();
            var newLineBankCode = self.currentCode1();
            if (oldLineBankCode == null) {
                nts.uk.ui.dialog.alert("置換元情報が選択されていません。");//ER007
                return;
            }
            if (newLineBankCode == null) {
                nts.uk.ui.dialog.alert("置換先情報が選択されていません。");//ER007
                return;
            }
            if (oldLineBankCode == newLineBankCode) {
                //ER009
                let messageError = nts.uk.text.format("統合元と統合先で同じコードの{0}が選択されています。\r\n  振込元銀行を確認してください。", self.currentCode());
                nts.uk.ui.dialog.alert(messageError);
                return;
            } else {
                //Al003
                nts.uk.ui.dialog.confirm("統合元から統合先へデータを置換えます。\r\n よろしいですか？").ifYes(function() {
                    //Do you want to delete lineBank?
                    nts.uk.ui.dialog.confirm("置換元のマスタを削除しますか？[はい/いいえ]").ifYes(function() {
                        var data = {
                            oldLineBankCode: oldLineBankCode,
                            newLineBankCode: newLineBankCode,
                            allowDelete: 1,
                        }
                        self.transferData(data, newLineBankCode);
                    }).ifNo(function() {
                        var data = {
                            oldLineBankCode: oldLineBankCode,
                            newLineBankCode: newLineBankCode,
                            allowDelete: 0,
                        }
                        self.transferData(data, newLineBankCode);
                    })
                }).ifCancel(function() {
                    return;
                })
            }
        }

        /**
         * close dialog
         */
        closeDialog(): void {
            nts.uk.ui.windows.close();
        }
    }

    class LineBankC {
        lineBankCode: KnockoutObservable<string>;
        lineBankName: KnockoutObservable<string>;
        accountAtr: KnockoutObservable<number>;
        accountNo: KnockoutObservable<string>;

        constructor(lineBankCode: string, lineBankName: string, accountAtr: number, accountNo: string) {
            this.lineBankCode = ko.observable(lineBankCode);
            this.lineBankName = ko.observable(lineBankName);
            this.accountAtr = ko.observable(accountAtr);
            this.accountNo = ko.observable(accountNo);
        }
    }
};