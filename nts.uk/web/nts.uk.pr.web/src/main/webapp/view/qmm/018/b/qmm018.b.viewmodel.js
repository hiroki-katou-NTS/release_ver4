var qmm018;
(function (qmm018) {
    var b;
    (function (b) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    var self = this;
                    self.items = ko.observableArray([]);
                    self.currentCodeListSwap = ko.observableArray([]);
                    self.oldCurrentCodeListSwap = ko.observableArray([]);
                }
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    qmm018.b.service.itemSelect(nts.uk.ui.windows.getShared('categoryAtr')).done(function (data) {
                        if (!data.length) {
                            $("#label-span").ntsError('set', qmm018.shr.viewmodelbase.Error.ER010);
                        }
                        else {
                            data.forEach(function (dataItem) {
                                self.items().push(new qmm018.shr.viewmodelbase.ItemModel(dataItem.itemCode, dataItem.itemAbName));
                            });
                            self.currentCodeListSwap.subscribe(function (value) {
                                if (!value.length)
                                    $("#label-span").ntsError('set', qmm018.shr.viewmodelbase.Error.ER007);
                                else
                                    $("#label-span").ntsError('clear');
                            });
                        }
                        dfd.resolve();
                        self.currentCodeListSwap(nts.uk.ui.windows.getShared('selectedItemList'));
                        self.oldCurrentCodeListSwap(nts.uk.ui.windows.getShared('selectedItemList'));
                    }).fail(function (res) {
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.submitData = function () {
                    var self = this;
                    nts.uk.ui.windows.setShared('selectedItemList', self.currentCodeListSwap());
                    nts.uk.ui.windows.close();
                };
                ScreenModel.prototype.closeWindow = function () {
                    var self = this;
                    nts.uk.ui.windows.setShared('selectedItemList', self.oldCurrentCodeListSwap());
                    nts.uk.ui.windows.close();
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
        })(viewmodel = b.viewmodel || (b.viewmodel = {}));
    })(b = qmm018.b || (qmm018.b = {}));
})(qmm018 || (qmm018 = {}));
//# sourceMappingURL=qmm018.b.viewmodel.js.map