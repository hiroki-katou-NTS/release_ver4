var qmm006;
(function (qmm006) {
    var c;
    (function (c) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    var self = this;
                    self.currentCode = ko.observable();
                    self.currentCode1 = ko.observable();
                    self.items = ko.observableArray([]);
                    self.items1 = ko.observableArray([]);
                    self.columns = ko.observableArray([
                        { headerText: 'コード', key: 'lineBankCode', width: 50, formatter: _.escape },
                        { headerText: '名称', key: 'lineBankName', width: 160, formatter: _.escape },
                        { headerText: '口座区分', key: 'accountAtr', width: 120, formatter: _.escape },
                        { headerText: '口座番号', key: 'accountNo', width: 100, formatter: _.escape }
                    ]);
                }
                ScreenModel.prototype.startPage = function () {
                    return this.findAll();
                };
                ScreenModel.prototype.findAll = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    qmm006.c.service.findAll()
                        .done(function (data) {
                        if (data.length > 0) {
                            self.items(data);
                            self.items1(data);
                        }
                        else {
                            self.items([]);
                            self.items1([]);
                        }
                        dfd.resolve();
                    }).fail(function (res) { });
                    return dfd.promise();
                };
                ScreenModel.prototype.transfer = function () {
                    var self = this;
                    var oldLineBankCode = self.currentCode();
                    var newLineBankCode = self.currentCode1();
                    if (oldLineBankCode == null || newLineBankCode == null) {
                        nts.uk.ui.dialog.alert("＊が選択されていません。");
                    }
                    else if (oldLineBankCode == newLineBankCode) {
                        nts.uk.ui.dialog.alert("統合元と統合先で同じコードの＊が選択されています。\r\n  ＊を確認してください。");
                    }
                    else {
                        var data = {
                            oldLineBankCode: oldLineBankCode,
                            newLineBankCode: newLineBankCode,
                        };
                        nts.uk.ui.dialog.confirm("置換元のマスタを削除しますか？[はい/いいえ]").ifYes(function () {
                            c.service.transfer(data)
                                .done(function () {
                                nts.uk.ui.windows.setShared("currentCode", newLineBankCode, true);
                                nts.uk.ui.windows.close();
                            })
                                .fail(function (error) {
                                nts.uk.ui.dialog.alert(error.message);
                            });
                        }).ifNo(function () {
                            return;
                        }).then(function () { });
                    }
                };
                ScreenModel.prototype.closeDialog = function () {
                    nts.uk.ui.windows.close();
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var LineBankC = (function () {
                function LineBankC(lineBankCode, lineBankName, accountAtr, accountNo) {
                    this.lineBankCode = ko.observable(lineBankCode);
                    this.lineBankName = ko.observable(lineBankName);
                    this.accountAtr = ko.observable(accountAtr);
                    this.accountNo = ko.observable(accountNo);
                }
                return LineBankC;
            }());
        })(viewmodel = c.viewmodel || (c.viewmodel = {}));
    })(c = qmm006.c || (qmm006.c = {}));
})(qmm006 || (qmm006 = {}));
;
//# sourceMappingURL=viewmodel.js.map