var qmm018;
(function (qmm018) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    var self = this;
                    self.averagePay = ko.observable(new AveragePay(null, null, null, null));
                    self.selectedItemList1 = ko.observableArray([]);
                    self.selectedItemList2 = ko.observableArray([]);
                    self.unSelectedItemList1 = ko.observableArray([]);
                    self.unSelectedItemList2 = ko.observableArray([]);
                    self.texteditor1 = ko.observable({
                        value: ko.computed(function () {
                            var s;
                            ko.utils.arrayForEach(self.selectedItemList1(), function (item) { if (!s) {
                                s = item.name;
                            }
                            else {
                                s += " + " + item.name;
                            } });
                            return s;
                        })
                    });
                    self.texteditor2 = ko.observable({
                        value: ko.computed(function () {
                            var s;
                            ko.utils.arrayForEach(self.selectedItemList2(), function (item) { if (!s) {
                                s = item.name;
                            }
                            else {
                                s += " + " + item.name;
                            } });
                            return s;
                        })
                    });
                    self.selectedItemList1.subscribe(function (value) {
                        if (!value.length)
                            $("#inp-3").ntsError('set', 'ER007');
                        else
                            $("#inp-3").ntsError('clear');
                    });
                    self.selectedItemList2.subscribe(function (value) {
                        if (!value.length)
                            $("#inp-1").ntsError('set', 'ER007');
                        else
                            $("#inp-1").ntsError('clear');
                    });
                    self.isUpdate = false;
                }
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    qmm018.a.service.qapmt_Ave_Pay_SEL_1().done(function (data) {
                        if (data) {
                            qmm018.a.service.qcamt_Item_Salary_SEL_3().done(function (data2) {
                                console.log(data2);
                                dfd.resolve();
                            }).fail(function (res) {
                            });
                            qmm018.a.service.qcamt_Item_SEL_5(0, 1).done(function (data2) {
                                console.log(data2);
                                dfd.resolve();
                            }).fail(function (res) {
                            });
                            qmm018.a.service.qcamt_Item_Salary_SEL_4().done(function (data2) {
                                console.log(data2);
                                dfd.resolve();
                            }).fail(function (res) {
                            });
                            qmm018.a.service.qcamt_Item_SEL_5(2, 1).done(function (data2) {
                                console.log(data2);
                                dfd.resolve();
                            }).fail(function (res) {
                            });
                            self.averagePay(new AveragePay(data.roundTimingSet, data.attendDayGettingSet, data.roundDigitSet, data.exceptionPayRate));
                            self.isUpdate = true;
                        }
                        else {
                            self.averagePay(new AveragePay(0, 0, 0, null));
                            self.isUpdate = false;
                        }
                        dfd.resolve();
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.saveData = function (isUpdate) {
                    var self = this;
                    var dfd = $.Deferred();
                    var command = ko.mapping.toJS(self.averagePay());
                    if (isUpdate) {
                        qmm018.a.service.qapmt_Ave_Pay_UPD_1(command).done(function (data) {
                            dfd.resolve();
                        }).fail(function (res) {
                            nts.uk.ui.dialog.alert("Update Fail");
                        });
                    }
                    else {
                        qmm018.a.service.qapmt_Ave_Pay_INS_1(command).done(function (data) {
                            qmm018.a.service.qcamt_Item_Salary_UPD_2(1).done(function () {
                                if (self.averagePay().attendDayGettingSet()) {
                                    if (self.isUpdate) {
                                        qmm018.a.service.qcamt_Item_Salary_SEL_2(1).done(function () {
                                            qmm018.a.service.qcamt_Item_Salary_UPD_2(1).done(function () {
                                            });
                                        });
                                    }
                                    qmm018.a.service.qcamt_Item_Salary_UPD_2(1).done(function () {
                                    });
                                }
                            });
                            dfd.resolve();
                        }).fail(function (res) {
                            nts.uk.ui.dialog.alert("Register Fail");
                        });
                    }
                    return dfd.promise();
                };
                ScreenModel.prototype.openSubWindow = function (n) {
                    var self = this;
                    if (!n) {
                        nts.uk.ui.windows.setShared('selectedItemList', self.selectedItemList1());
                        nts.uk.ui.windows.setShared('categoryAtr', 1);
                    }
                    else {
                        nts.uk.ui.windows.setShared('selectedItemList', self.selectedItemList2());
                        nts.uk.ui.windows.setShared('categoryAtr', 2);
                    }
                    nts.uk.ui.windows.sub.modal("/view/qmm/018/b/index.xhtml", { title: "労働日数項目一覧", dialogClass: "no-close" }).onClosed(function () {
                        var selectedList = nts.uk.ui.windows.getShared('selectedItemList');
                        var unSelectedList = nts.uk.ui.windows.getShared('unSelectedItemList');
                        if (!n) {
                            if (selectedList.length) {
                                if (!_.isEqual(selectedList, self.selectedItemList1())) {
                                    self.selectedItemList1.removeAll();
                                    ko.utils.arrayForEach(selectedList, function (item) { self.selectedItemList1.push(item); });
                                    if (unSelectedList.length) {
                                        self.unSelectedItemList1.removeAll();
                                        ko.utils.arrayForEach(unSelectedList, function (item) { self.unSelectedItemList1.push(item); });
                                    }
                                    else {
                                        self.unSelectedItemList1([]);
                                    }
                                }
                            }
                            else {
                                self.selectedItemList1([]);
                            }
                        }
                        else {
                            if (selectedList.length) {
                                if (!_.isEqual(selectedList, self.selectedItemList2())) {
                                    self.selectedItemList2.removeAll();
                                    ko.utils.arrayForEach(selectedList, function (item) { self.selectedItemList2.push(item); });
                                    if (unSelectedList.length) {
                                        self.unSelectedItemList2.removeAll();
                                        ko.utils.arrayForEach(unSelectedList, function (item) { self.unSelectedItemList2.push(item); });
                                    }
                                    else {
                                        self.unSelectedItemList2([]);
                                    }
                                }
                            }
                            else {
                                self.selectedItemList2([]);
                            }
                        }
                    });
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var ItemModel = (function () {
                function ItemModel(code, name) {
                    this.code = code;
                    this.name = name;
                }
                return ItemModel;
            }());
            var AveragePay = (function () {
                function AveragePay(roundTimingSet, attendDayGettingSet, roundDigitSet, exceptionPayRate) {
                    var self = this;
                    self.roundTimingSet = ko.observable(roundTimingSet);
                    self.attendDayGettingSet = ko.observable(attendDayGettingSet);
                    self.roundDigitSet = ko.observable(roundDigitSet);
                    self.exceptionPayRate = ko.observable(exceptionPayRate);
                    self.oldExceptionPayRate = ko.observable(exceptionPayRate);
                    self.roundTimingSet.subscribe(function (value) { self.roundTimingSet(value ? 1 : 0); });
                    self.exceptionPayRate.subscribe(function (value) {
                        if ($("#inp-2").ntsError("hasError")) {
                            self.oldExceptionPayRate(exceptionPayRate);
                        }
                        else {
                            exceptionPayRate = value;
                            self.oldExceptionPayRate(value);
                        }
                    });
                }
                return AveragePay;
            }());
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qmm018.a || (qmm018.a = {}));
})(qmm018 || (qmm018 = {}));
