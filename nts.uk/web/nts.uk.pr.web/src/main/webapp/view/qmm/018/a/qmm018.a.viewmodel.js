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
                    self.texteditor1 = ko.observable({
                        value: ko.computed(function () {
                            var s;
                            ko.utils.arrayForEach(self.averagePay().selectedSalaryItems(), function (item) { if (!s) {
                                s = item.itemAbName;
                            }
                            else {
                                s += " + " + item.itemAbName;
                            } });
                            return s;
                        })
                    });
                    self.texteditor2 = ko.observable({
                        value: ko.computed(function () {
                            var s;
                            ko.utils.arrayForEach(self.averagePay().selectedAttendItems(), function (item) { if (!s) {
                                s = item.itemAbName;
                            }
                            else {
                                s += " + " + item.itemAbName;
                            } });
                            return s;
                        })
                    });
                    self.isUpdate = false;
                    self.dirty = new nts.uk.ui.DirtyChecker(self.averagePay);
                }
                /**
                 * get init data
                 */
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    // get average pay items
                    qmm018.a.service.averagePayItemSelect().done(function (data) {
                        if (data) {
                            // if data exist go to update case
                            self.averagePay(new AveragePay(data.roundTimingSet, data.attendDayGettingSet, data.roundDigitSet, data.exceptionPayRate));
                            // get salary items
                            qmm018.a.service.averagePayItemSelectBySalary().done(function (dataSalary) {
                                self.loadData(dataSalary, self.averagePay().selectedSalaryItems, false);
                                self.dirty.reset();
                            }).fail(function (res) {
                            });
                            // get attend items
                            if (self.averagePay().attendDayGettingSet()) {
                                qmm018.a.service.averagePayItemSelectByAttend().done(function (dataAttend) {
                                    self.loadData(dataAttend, self.averagePay().selectedSalaryItems, false);
                                    self.dirty.reset();
                                }).fail(function (res) {
                                });
                            }
                            self.isUpdate = true;
                            self.dirty.reset();
                        }
                        else {
                            // if data no exist go to insert case
                            self.averagePay(new AveragePay(0, 0, 0, null));
                            self.isUpdate = false;
                            self.dirty.reset();
                        }
                        dfd.resolve();
                        // error check on salary list and attend list
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                    return dfd.promise();
                };
                /**
                 * save average setting
                 */
                ScreenModel.prototype.saveData = function (isUpdate) {
                    var self = this;
                    var dfd = $.Deferred();
                    var error = false;
                    // check errors on required
                    if (!self.averagePay().selectedSalaryItems().length) {
                        $("#inp-3").ntsError('set', qmm018.shr.viewmodelbase.Error.ER007);
                        error = true;
                    }
                    if (self.averagePay().attendDayGettingSet()) {
                        if (!self.averagePay().selectedAttendItems().length) {
                            $("#inp-1").ntsError('set', qmm018.shr.viewmodelbase.Error.ER007);
                            error = true;
                        }
                    }
                    if (self.averagePay().exceptionPayRate() == null) {
                        $("#inp-2").ntsError('set', qmm018.shr.viewmodelbase.Error.ER001);
                        error = true;
                    }
                    // insert or update if no error
                    if (!error && self.dirty.isDirty()) {
                        //create data
                        var command = {
                            roundTimingSet: self.averagePay().roundTimingSet(),
                            attendDayGettingSet: self.averagePay().attendDayGettingSet(),
                            roundDigitSet: self.averagePay().roundDigitSet(),
                            exceptionPayRate: self.averagePay().exceptionPayRate(),
                            selectedSalaryItems: _.map(self.averagePay().selectedSalaryItems(), function (o) { return o.itemCode; }),
                            selectedAttendItems: _.map(self.averagePay().selectedAttendItems(), function (o) { return o.itemCode; })
                        };
                        if (isUpdate) {
                            qmm018.a.service.averagePayItemUpdate(command).done(function (data) {
                                self.dirty.reset();
                                dfd.resolve();
                            }).fail(function (res) {
                                dfd.reject();
                            });
                        }
                        else {
                            qmm018.a.service.averagePayItemInsert(command).done(function (data) {
                                self.dirty.reset();
                                dfd.resolve();
                            }).fail(function (res) {
                                dfd.reject();
                            });
                        }
                    }
                    return dfd.promise();
                };
                /**
                 * open B screen
                 */
                ScreenModel.prototype.openSubWindow = function (n) {
                    var self = this;
                    if (!n) {
                        // set salary data
                        nts.uk.ui.windows.setShared('selectedItemList', self.averagePay().selectedSalaryItems());
                        nts.uk.ui.windows.setShared('categoryAtr', qmm018.shr.viewmodelbase.CategoryAtr.PAYMENT);
                    }
                    else {
                        // set attend data
                        nts.uk.ui.windows.setShared('selectedItemList', self.averagePay().selectedAttendItems());
                        nts.uk.ui.windows.setShared('categoryAtr', qmm018.shr.viewmodelbase.CategoryAtr.PERSONAL_TIME);
                    }
                    nts.uk.ui.windows.sub.modal("/view/qmm/018/b/index.xhtml", { title: "対象項目の選択", dialogClass: "no-close" }).onClosed(function () {
                        var selectedList = nts.uk.ui.windows.getShared('selectedItemList'); // Get selected form B screen, n = 0: ItemSalary, n = 2: ItemAttend
                        if (!n) {
                            // set data to salary item list 
                            self.loadData(selectedList, self.averagePay().selectedSalaryItems, _.isEqual(selectedList, self.averagePay().selectedSalaryItems()));
                        }
                        else {
                            // set data to attend item list 
                            self.loadData(selectedList, self.averagePay().selectedAttendItems, _.isEqual(selectedList, self.averagePay().selectedAttendItems()));
                        }
                    });
                };
                /**
                 * set data to KnockoutObservableArray from Array source
                 */
                ScreenModel.prototype.loadData = function (dataSource, dataDestination, isDataEqual) {
                    if (dataSource.length) {
                        if (!isDataEqual) {
                            dataDestination.removeAll();
                            dataSource.forEach(function (item) { dataDestination.push(new qmm018.shr.viewmodelbase.ItemModel(item.itemCode, item.itemAbName)); });
                        }
                    }
                    else {
                        dataDestination([]);
                    }
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var AveragePay = (function () {
                function AveragePay(roundTimingSet, attendDayGettingSet, roundDigitSet, exceptionPayRate) {
                    var self = this;
                    self.roundTimingSet = ko.observable(roundTimingSet);
                    self.attendDayGettingSet = ko.observable(attendDayGettingSet);
                    self.roundDigitSet = ko.observable(roundDigitSet);
                    self.exceptionPayRate = ko.observable(exceptionPayRate);
                    self.oldExceptionPayRate = ko.observable(exceptionPayRate);
                    self.selectedSalaryItems = ko.observableArray([]);
                    self.selectedAttendItems = ko.observableArray([]);
                    self.roundTimingSet.subscribe(function (value) { self.roundTimingSet(value ? 1 : 0); });
                    self.attendDayGettingSet.subscribe(function (value) {
                        if (!value) {
                            self.roundDigitSet(0);
                            $("#inp-1").ntsError('clear');
                            self.selectedAttendItems([]);
                        }
                    });
                    self.exceptionPayRate.subscribe(function (value) {
                        if ($("#inp-2").ntsError('hasError')) {
                            self.oldExceptionPayRate(exceptionPayRate);
                        }
                        else {
                            exceptionPayRate = value;
                            self.oldExceptionPayRate(value);
                        }
                    });
                    self.selectedSalaryItems.subscribe(function (value) {
                        if (!value.length)
                            $("#inp-3").ntsError('set', qmm018.shr.viewmodelbase.Error.ER007);
                        else
                            $("#inp-3").ntsError('clear');
                    });
                    self.selectedAttendItems.subscribe(function (value) {
                        if (!self.attendDayGettingSet())
                            $("#inp-1").ntsError('clear');
                        else {
                            if (!value.length)
                                $("#inp-1").ntsError('set', qmm018.shr.viewmodelbase.Error.ER007);
                            else
                                $("#inp-1").ntsError('clear');
                        }
                    });
                }
                return AveragePay;
            }());
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qmm018.a || (qmm018.a = {}));
})(qmm018 || (qmm018 = {}));
