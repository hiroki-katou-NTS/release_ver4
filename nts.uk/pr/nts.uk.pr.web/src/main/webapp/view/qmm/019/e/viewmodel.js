var qmm019;
(function (qmm019) {
    var e;
    (function (e) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                /**
                 * Init screen model.
                 */
                function ScreenModel() {
                    var self = this;
                    self.selectedLayoutAtr = ko.observable(null);
                    self.selectLayoutCode = ko.observable(null);
                    self.selectLayoutName = ko.observable(null);
                    self.selectLayoutStartYm = ko.observable(null);
                    self.selectLayoutEndYm = ko.observable(null);
                    self.selectLayout = ko.observable(null);
                    self.layoutStartYm = ko.observable(null);
                }
                // start function
                ScreenModel.prototype.start = function () {
                    var self = this;
                    var layoutCode = nts.uk.ui.windows.getShared('stmtCode');
                    var startYm = nts.uk.ui.windows.getShared('startYm');
                    self.layoutStartYm(nts.uk.time.formatYearMonth(startYm));
                    e.service.getLayout(layoutCode, startYm).done(function (layout) {
                        self.selectLayout(layout);
                        self.startDiaglog();
                    }).fail(function (res) {
                        alert(res);
                    });
                    var dfd = $.Deferred();
                    dfd.resolve();
                    // Return.
                    return dfd.promise();
                };
                ScreenModel.prototype.startDiaglog = function () {
                    var self = this;
                    var layout = self.selectLayout();
                    var code = layout.stmtCode.trim();
                    if (code.length < 2) {
                        code = "0" + code;
                    }
                    self.selectLayoutCode(code);
                    self.selectLayoutName(layout.stmtName);
                    self.selectLayoutStartYm(nts.uk.time.formatYearMonth(layout.startYm));
                    self.selectLayoutEndYm(nts.uk.time.formatYearMonth(layout.endYm));
                };
                ScreenModel.prototype.layoutProcess = function () {
                    var self = this;
                    //履歴の編集-削除処理
                    if ($("#layoutDetele").is(":checked")) {
                        self.dataDelete();
                    }
                    else {
                        self.dataUpdate();
                    }
                };
                ScreenModel.prototype.dataDelete = function () {
                    var self = this;
                    e.service.deleteLayout(self.selectLayout()).done(function () {
                        alert("履歴を削除しました。");
                        nts.uk.ui.windows.close();
                    }).fail(function (res) {
                        alert(res);
                    });
                };
                ScreenModel.prototype.dataUpdate = function () {
                    var self = this;
                    var layoutInfor = self.selectLayout();
                    layoutInfor.startYmOriginal = +self.layoutStartYm().replace('/', '');
                    layoutInfor.startYm = +$("#INP_001").val().replace('/', '');
                    e.service.updateLayout(layoutInfor).done(function () {
                        alert("履歴を修正しました。");
                        nts.uk.ui.windows.close();
                    }).fail(function (res) {
                        alert(res);
                    });
                };
                ScreenModel.prototype.closeDialog = function () {
                    nts.uk.ui.windows.close();
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
        })(viewmodel = e.viewmodel || (e.viewmodel = {}));
    })(e = qmm019.e || (qmm019.e = {}));
})(qmm019 || (qmm019 = {}));
