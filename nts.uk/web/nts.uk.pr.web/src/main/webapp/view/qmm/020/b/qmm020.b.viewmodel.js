var qmm020;
(function (qmm020) {
    var b;
    (function (b) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    var self = this;
                    self.maxDate = "";
                    self.isInsert = ko.observable(false);
                    self.itemList = ko.observableArray([]);
                    self.currentItem = ko.observable(new ComHistItem({
                        histId: '',
                        startYm: '',
                        endYm: '',
                        payCode: '',
                        bonusCode: ''
                    }));
                    self.maxItem = ko.observable(new b.service.model.CompanyAllotSettingDto());
                    self.start();
                }
                // start function
                ScreenModel.prototype.start = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    //Get list startDate, endDate of History 
                    //get allot Company
                    b.service.getAllotCompanyList().done(function (companyAllots) {
                        if (companyAllots.length > 0) {
                            var _items = [];
                            //push data to listItem of hist List
                            for (var i_1 in companyAllots) {
                                var item = companyAllots[i_1];
                                if (item) {
                                    _items.push(new ComHistItem({
                                        histId: (item.historyId || ""),
                                        startYm: (item.startDate || ""),
                                        endYm: (item.endDate || ""),
                                        payCode: (item.paymentDetailCode || ""),
                                        bonusCode: (item.bonusDetailCode || "")
                                    }));
                                }
                            }
                            self.itemList(_items);
                            self.companyAllots = companyAllots;
                            self.currentItem().setSource(self.companyAllots);
                            self.currentItem().histId(companyAllots[0].historyId);
                        }
                        else {
                            //self.allowClick(false);
                            dfd.resolve();
                        }
                    }).fail(function (res) {
                        // Alert message
                        alert(res);
                    });
                    ///////////////////////////////////////
                    ///////////////////////////////////////
                    //get Row with max Date 
                    b.service.getAllotCompanyMaxDate().done(function (itemMax) {
                        //console.log(current);
                        self.maxDate = (itemMax.startDate || "").toString();
                        self.maxItem(itemMax);
                    }).fail(function (res) {
                        alert(res);
                    });
                    // Return.
                    return dfd.promise();
                };
                //Update data
                ScreenModel.prototype.register = function () {
                    var self = this;
                    var current = _.find(self.companyAllots, function (item) { return item.historyId == self.currentItem().histId(); });
                    debugger;
                    if (current) {
                        b.service.insertComAllot(current).done(function () {
                        }).fail(function (res) {
                            alert(res);
                        });
                    }
                    self.start();
                };
                // reload data
                ScreenModel.prototype.reload = function () {
                    var dfd = $.Deferred();
                    var self = this;
                };
                //Open dialog Add History
                ScreenModel.prototype.openJDialog = function () {
                    var self = this;
                    //getMaxDate
                    var historyScreenType = "1";
                    //Get value TabCode + value of selected Name in History List
                    var valueShareJDialog = historyScreenType + "~" + self.maxDate;
                    nts.uk.ui.windows.setShared('valJDialog', valueShareJDialog);
                    nts.uk.ui.windows.sub.modal('/view/qmm/020/j/index.xhtml', { title: '明細書の紐ずけ＞履歴追加' })
                        .onClosed(function () {
                        var returnJDialog = nts.uk.ui.windows.getShared('returnJDialog');
                        var modeRadio = returnJDialog.split("~")[0];
                        var returnValue = returnJDialog.split("~")[1];
                        if (returnValue != '') {
                            var items = self.itemList();
                            var addItem = new ComHistItem({
                                histId: new Date().getTime().toString(),
                                startYm: returnValue,
                                endYm: '999912',
                                payCode: '',
                                bonusCode: ''
                            });
                            items.push(addItem);
                            // Goi cap nhat vao currentItem
                            // Trong truong hop them moi NHANH, copy payCode, bonusCode tu Previous Item
                            if (modeRadio === "1") {
                                debugger;
                                self.currentItem().histId(addItem.histId());
                                self.currentItem().startYm(returnValue);
                                self.currentItem().endYm('999912');
                                self.currentItem().payCode(self.maxItem().paymentDetailCode);
                                self.currentItem().bonusCode(self.maxItem().bonusDetailCode);
                                //get Payment Name
                                if (self.currentItem().payCode() != '') {
                                    b.service.getAllotLayoutName(self.currentItem().payCode()).done(function (stmtName) {
                                        self.currentItem().payName(stmtName);
                                    }).fail(function (res) {
                                        self.currentItem().payName('');
                                    });
                                }
                                else {
                                    self.currentItem().payName('');
                                }
                                //get Bonus Name
                                if (self.currentItem().bonusCode() != '') {
                                    b.service.getAllotLayoutName(self.currentItem().bonusCode()).done(function (stmtName) {
                                        self.currentItem().bonusName(stmtName);
                                    }).fail(function (res) {
                                        self.currentItem().bonusName('');
                                    });
                                }
                                else {
                                    self.currentItem().bonusName('');
                                }
                            }
                            else {
                                self.currentItem().histId(addItem.histId());
                                self.currentItem().startYm(returnValue);
                                self.currentItem().endYm('999912');
                                self.currentItem().payCode('');
                                self.currentItem().bonusCode('');
                                self.currentItem().payName('');
                                self.currentItem().bonusName('');
                            }
                            self.itemList([]);
                            self.itemList(items);
                        }
                    });
                };
                //Open dialog Edit History
                ScreenModel.prototype.openKDialog = function () {
                    var self = this;
                    nts.uk.ui.windows.setShared("endYM", self.currentItem().endYm());
                    nts.uk.ui.windows.setShared('scrType', '1');
                    nts.uk.ui.windows.setShared('startYM', self.maxDate);
                    var current = _.find(self.companyAllots, function (item) { return item.historyId == self.currentItem().histId(); });
                    if (current) {
                        nts.uk.ui.windows.setShared('currentItem', current);
                    }
                    nts.uk.ui.windows.sub.modal('/view/qmm/020/k/index.xhtml', { title: '明細書の紐ずけ＞履歴編集' }).onClosed(function () {
                        debugger;
                        self.start();
                    });
                };
                //Open L Dialog
                ScreenModel.prototype.openLDialog = function () {
                    alert('2017');
                };
                //Click to button Select Payment
                ScreenModel.prototype.openPaymentMDialog = function () {
                    var self = this;
                    var valueShareMDialog = self.currentItem().startYm();
                    //debugger;
                    nts.uk.ui.windows.setShared('valMDialog', valueShareMDialog);
                    nts.uk.ui.windows.sub.modal('/view/qmm/020/m/index.xhtml', { title: '明細書の選択' }).onClosed(function () {
                        //get selected code from M dialog
                        var stmtCodeSelected = nts.uk.ui.windows.getShared('stmtCodeSelected');
                        self.currentItem().payCode(stmtCodeSelected);
                        //get Name payment Name
                        b.service.getAllotLayoutName(self.currentItem().payCode()).done(function (stmtName) {
                            self.currentItem().payName(stmtName);
                        }).fail(function (res) {
                            alert(res);
                        });
                    });
                };
                //Click to button Select Bonus
                ScreenModel.prototype.openBonusMDialog = function () {
                    var self = this;
                    var valueShareMDialog = self.currentItem().startYm();
                    //debugger;
                    nts.uk.ui.windows.setShared('valMDialog', valueShareMDialog);
                    nts.uk.ui.windows.sub.modal('/view/qmm/020/m/index.xhtml', { title: '明細書の選択' }).onClosed(function () {
                        //get selected code from M dialog
                        var stmtCodeSelected = nts.uk.ui.windows.getShared('stmtCodeSelected');
                        self.currentItem().bonusCode(stmtCodeSelected);
                        //get Name payment Name
                        b.service.getAllotLayoutName(self.currentItem().bonusCode()).done(function (stmtName) {
                            self.currentItem().bonusName(stmtName);
                        }).fail(function (res) {
                            alert(res);
                        });
                    });
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var ComHistItem = (function () {
                function ComHistItem(param) {
                    var self = this;
                    self.id = param.histId;
                    self.histId = ko.observable(param.histId);
                    self.startYm = ko.observable(param.startYm);
                    self.endYm = ko.observable(param.endYm);
                    self.payCode = ko.observable(param.payCode);
                    self.bonusCode = ko.observable(param.bonusCode);
                    self.startEnd = param.startYm + '~' + param.endYm;
                    self.payName = ko.observable(param.payName || '');
                    self.bonusName = ko.observable(param.bonusName || '');
                    self.histId.subscribe(function (newValue) {
                        if (typeof newValue != 'string') {
                            return;
                        }
                        var current = _.find(self.listSource, function (item) { return item.historyId == newValue; });
                        if (current) {
                            self.startYm(current.startDate);
                            self.endYm(current.endDate);
                            self.payCode(current.paymentDetailCode);
                            self.bonusCode(current.bonusDetailCode);
                            self.startEnd = self.startYm() + '~' + self.endYm();
                            if (current.paymentDetailCode != '') {
                                b.service.getAllotLayoutName(current.paymentDetailCode).done(function (stmtName) {
                                    self.payName(stmtName);
                                }).fail(function (res) {
                                    self.payName('');
                                });
                            }
                            else {
                                self.payName('');
                            }
                            if (current.bonusDetailCode != '') {
                                b.service.getAllotLayoutName(current.bonusDetailCode).done(function (stmtName) {
                                    self.bonusName(stmtName);
                                }).fail(function (res) {
                                    self.bonusName('');
                                });
                            }
                            else {
                                self.bonusName('');
                            }
                        }
                        else {
                            var newItem = {
                                paymentDetailCode: '',
                                bonusDetailCode: '',
                                startDate: 0,
                                endDate: 0,
                                historyId: self.histId()
                            };
                            self.listSource.push(newItem);
                            self.payName('');
                            self.bonusName('');
                        }
                    });
                    self.payCode.subscribe(function (newValue) {
                        //console.log(self.listSource);
                        var current = _.find(self.listSource, function (item) { return item.historyId == self.histId(); });
                        if (current) {
                            current.paymentDetailCode = newValue;
                        }
                    });
                    self.bonusCode.subscribe(function (newValue) {
                        var current = _.find(self.listSource, function (item) { return item.historyId == self.histId(); });
                        if (current) {
                            current.bonusDetailCode = newValue;
                        }
                    });
                    self.startYm.subscribe(function (newValue) {
                        var current = _.find(self.listSource, function (item) { return item.historyId == self.histId(); });
                        if (current) {
                            current.startDate = newValue;
                        }
                    });
                    self.endYm.subscribe(function (newValue) {
                        var current = _.find(self.listSource, function (item) { return item.historyId == self.histId(); });
                        if (current) {
                            current.endDate = newValue;
                        }
                    });
                }
                ComHistItem.prototype.setSource = function (list) {
                    this.listSource = list || [];
                };
                return ComHistItem;
            }());
        })(viewmodel = b.viewmodel || (b.viewmodel = {}));
    })(b = qmm020.b || (qmm020.b = {}));
})(qmm020 || (qmm020 = {}));
