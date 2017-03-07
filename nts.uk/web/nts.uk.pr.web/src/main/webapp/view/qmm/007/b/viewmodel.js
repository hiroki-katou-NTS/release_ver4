var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var qmm007;
                (function (qmm007) {
                    var b;
                    (function (b) {
                        var viewmodel;
                        (function (viewmodel) {
                            var service = nts.uk.pr.view.qmm007.a.service;
                            var ScreenModel = (function () {
                                function ScreenModel() {
                                    var self = this;
                                    self.unitPriceHistoryModel = ko.mapping.fromJS(nts.uk.ui.windows.getShared('unitPriceHistoryModel'));
                                    self.lastHistory = self.unitPriceHistoryModel.startMonth();
                                    self.historyTakeOver = ko.observable('latest');
                                }
                                ScreenModel.prototype.startPage = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    dfd.resolve();
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.btnApplyClicked = function () {
                                    var self = this;
                                    if (self.historyTakeOver() == 'beginning') {
                                        self.unitPriceHistoryModel.budget(0);
                                        self.unitPriceHistoryModel.fixPaySettingType('Company');
                                        self.unitPriceHistoryModel.fixPayAtr('NotApply');
                                        self.unitPriceHistoryModel.fixPayAtrMonthly('NotApply');
                                        self.unitPriceHistoryModel.fixPayAtrDayMonth('NotApply');
                                        self.unitPriceHistoryModel.fixPayAtrDaily('NotApply');
                                        self.unitPriceHistoryModel.fixPayAtrHourly('NotApply');
                                        self.unitPriceHistoryModel.memo('');
                                    }
                                    service.create(ko.toJS(self.unitPriceHistoryModel), false).done(function () {
                                        nts.uk.ui.windows.setShared('isCreated', true);
                                        nts.uk.ui.windows.close();
                                    });
                                };
                                ScreenModel.prototype.btnCancelClicked = function () {
                                    nts.uk.ui.windows.close();
                                };
                                return ScreenModel;
                            }());
                            viewmodel.ScreenModel = ScreenModel;
                        })(viewmodel = b.viewmodel || (b.viewmodel = {}));
                    })(b = qmm007.b || (qmm007.b = {}));
                })(qmm007 = view.qmm007 || (view.qmm007 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
