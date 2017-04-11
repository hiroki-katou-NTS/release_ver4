var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var qmm016;
                (function (qmm016) {
                    var k;
                    (function (k) {
                        var viewmodel;
                        (function (viewmodel) {
                            var ScreenModel = (function () {
                                function ScreenModel() {
                                    var self = this;
                                    self.dialogOptions = nts.uk.ui.windows.getShared('options');
                                    self.demensionItemList = ko.observableArray([]);
                                    self.selectedDemension = ko.observable(undefined);
                                }
                                ScreenModel.prototype.startPage = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    k.service.loadDemensionSelectionList().done(function (res) {
                                        var filteredDemensionItemList = _.filter(res, function (item) {
                                            var ignoredItem = _.find(self.dialogOptions.selectedDemensionDto, function (selected) {
                                                return item.type == selected.type && item.code == selected.code;
                                            });
                                            return ignoredItem == undefined;
                                        });
                                        self.demensionItemList(filteredDemensionItemList);
                                        dfd.resolve();
                                    });
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.btnApplyClicked = function () {
                                    var self = this;
                                    if (self.selectedDemension()) {
                                        var callBackData = {
                                            demension: self.selectedDemension()
                                        };
                                        self.dialogOptions.onSelectItem(callBackData);
                                        nts.uk.ui.windows.close();
                                    }
                                };
                                ScreenModel.prototype.btnCancelClicked = function () {
                                    nts.uk.ui.windows.close();
                                };
                                return ScreenModel;
                            }());
                            viewmodel.ScreenModel = ScreenModel;
                        })(viewmodel = k.viewmodel || (k.viewmodel = {}));
                    })(k = qmm016.k || (qmm016.k = {}));
                })(qmm016 = view.qmm016 || (view.qmm016 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
