var qmm013;
(function (qmm013) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    this.confirmDirty = false;
                    var self = this;
                    self.items = ko.observableArray([]);
                    self.currentItem = ko.observable(new PersonalUnitPrice(null, null, null, null, null, null, null, null, null, null, null, null, null));
                    self.listItems = ko.observableArray([]);
                    self.currentCode = ko.observable();
                    self.displayAll = ko.observable(true);
                    self.isCreated = ko.observable(true);
                    self.isEnableDelete = ko.observable(true);
                    self.isFirstGetData = ko.observable(false);
                    self.notCheckDirty = ko.observable(false);
                    self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                    self.indexRow = ko.observable(0);
                    self.notLoop = ko.observable(false);
                    self.columns = ko.observableArray([
                        { headerText: 'コード', width: 100, key: 'code', formatter: _.escape },
                        { headerText: '名称', width: 150, key: 'name', formatter: _.escape },
                        { headerText: '廃止', width: 50, key: 'abolition', }
                    ]);
                    self.messages = ko.observableArray([
                        { messageId: "AL001", message: "変更された内容が登録されていません。\r\nよろしいですか。" },
                        { messageId: "ER001", message: "が入力されていません。" },
                        { messageId: "ER005", message: "入力したコードは既に存在しています。\r\nコードを確認してください。" },
                        { messageId: "AL002", message: "データを削除します。\r\nよろしいですか？" },
                        { messageId: "ER026", message: "更新対象のデータが存在しません。" },
                    ]);
                    self.itemList = ko.observableArray([
                        new BoxModel(0, '全員一律で指定する'),
                        new BoxModel(1, '給与約束形態ごとに指定する')
                    ]);
                    self.roundingRules = ko.observableArray([
                        { code: '1', name: '対象' },
                        { code: '0', name: '対象外' }
                    ]);
                    self.SEL_004 = ko.observableArray([
                        { code: '0', name: '時間単価' },
                        { code: '1', name: '日額' },
                        { code: '2', name: 'その他' },
                    ]);
                    self.currentCode.subscribe(function (newCode) {
                        //in case first getData, no error so not jump clearError()
                        if (self.isFirstGetData()) {
                            self.clearError();
                        }
                        self.isFirstGetData(true);
                        //don't allow checkDirty
                        if (self.notCheckDirty()) {
                            self.selectedUnitPrice(newCode);
                            self.notCheckDirty(false);
                            return;
                        }
                        if (!self.checkDirty()) {
                            self.selectedUnitPrice(newCode);
                            self.isCreated(false);
                            self.isEnableDelete(true);
                        }
                        else {
                            //don't loop subscribe function
                            if (self.confirmDirty) {
                                self.confirmDirty = false;
                                self.isEnableDelete(true);
                                return;
                            }
                            nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。").ifYes(function () {
                                self.selectedUnitPrice(newCode);
                                self.isCreated(false);
                                self.isEnableDelete(true);
                            }).ifNo(function () {
                                self.confirmDirty = true;
                                self.currentCode(self.currentItem().personalUnitPriceCode());
                            });
                        }
                    });
                    self.displayAll.subscribe(function (newValue) {
                        // don't loop subscribe function
                        // in case change data, change state button SEL_001 and choise 'NO'
                        if (self.notLoop()) {
                            self.notLoop(false);
                            return;
                        }
                        if (!self.checkDirty()) {
                            self.getPersonalUnitPriceList().done(function () {
                                //in case no dirty
                                //if row is chose has column '廃止' is 'X', select first row in new list
                                if (!self.currentItem().displaySet() && self.currentCode() != "") {
                                    var tmp = _.find(self.listItems(), function (x) {
                                        return x.personalUnitPriceCode === self.currentCode();
                                    });
                                    var tmp1 = new PersonalUnitPrice(tmp.personalUnitPriceCode, tmp.personalUnitPriceName, tmp.personalUnitPriceShortName, tmp.displaySet ? false : true, null, tmp.paymentSettingType, tmp.fixPaymentAtr, tmp.fixPaymentMonthly, tmp.fixPaymentDayMonth, tmp.fixPaymentDaily, tmp.fixPaymentHoursly, tmp.unitPriceAtr, tmp.memo);
                                    self.currentItem(tmp1);
                                    self.dirty.reset();
                                }
                                else {
                                    self.selectedFirstUnitPrice();
                                }
                            });
                        }
                        else {
                            nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。")
                                .ifYes(function () {
                                //self.notCheckDirty(true);
                                self.getPersonalUnitPriceList().done(function () {
                                    //in case dirty
                                    if (self.currentCode() == "") {
                                        self.notCheckDirty(true);
                                        self.selectedFirstUnitPrice();
                                    }
                                    //if row is chose has column '廃止' is 'X', select first row in new list
                                    if (self.currentItem().displaySet()) {
                                        self.notCheckDirty(true);
                                        self.selectedFirstUnitPrice();
                                    }
                                    else {
                                        //if row is chose has column '廃止' isn't 'X', keep the same position in new list
                                        var tmp = _.find(self.listItems(), function (x) {
                                            return x.personalUnitPriceCode === self.currentCode();
                                        });
                                        var tmp1 = new PersonalUnitPrice(tmp.personalUnitPriceCode, tmp.personalUnitPriceName, tmp.personalUnitPriceShortName, tmp.displaySet ? false : true, null, tmp.paymentSettingType, tmp.fixPaymentAtr, tmp.fixPaymentMonthly, tmp.fixPaymentDayMonth, tmp.fixPaymentDaily, tmp.fixPaymentHoursly, tmp.unitPriceAtr, tmp.memo);
                                        self.currentItem(tmp1);
                                        self.dirty.reset();
                                    }
                                });
                            })
                                .ifNo(function () {
                                self.notLoop(true);
                                self.displayAll(!self.displayAll());
                            });
                        }
                    });
                    /**
                     * paymentSettingType is number, convert to boolean type
                     */
                    self.isCompany = ko.computed(function () {
                        return !(self.currentItem().paymentSettingType() == 0);
                    });
                }
                ScreenModel.prototype.startPage = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    self.getPersonalUnitPriceList().done(function () {
                        self.selectedFirstUnitPrice();
                        dfd.resolve();
                    });
                    return dfd.promise();
                };
                /**
                 * get data from data base to screen
                 */
                ScreenModel.prototype.getPersonalUnitPriceList = function () {
                    var self = this;
                    var dfd = $.Deferred();
                    a.service.getPersonalUnitPriceList(self.displayAll()).done(function (data) {
                        var items = _.map(data, function (item) {
                            var abolition = item.displaySet == true ? "" : '<i class="icon icon-close"></i>';
                            return new ItemModel(item.personalUnitPriceCode, item.personalUnitPriceName, abolition);
                        });
                        self.listItems(data);
                        self.items(items);
                        dfd.resolve();
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                    return dfd.promise();
                };
                ScreenModel.prototype.selectedUnitPrice = function (code) {
                    var self = this;
                    if (!code) {
                        return;
                    }
                    a.service.getPersonalUnitPrice(code).done(function (res) {
                        self.currentItem(self.selectedFirst(res));
                        self.dirty.reset();
                    }).fail(function (res) {
                        alert(res.message);
                    });
                };
                ScreenModel.prototype.selectedFirst = function (item) {
                    var self = this;
                    return new PersonalUnitPrice(item.personalUnitPriceCode, item.personalUnitPriceName, item.personalUnitPriceShortName, item.displaySet ? false : true, item.uniteCode, item.paymentSettingType, item.fixPaymentAtr, item.fixPaymentMonthly, item.fixPaymentDayMonth, item.fixPaymentDaily, item.fixPaymentHoursly, item.unitPriceAtr, item.memo);
                };
                ScreenModel.prototype.checkDirty = function () {
                    var self = this;
                    if (self.dirty.isDirty()) {
                        return true;
                    }
                    else {
                        return false;
                    }
                };
                ;
                /**
                 * 新規(Clear form)
                 */
                ScreenModel.prototype.btn_001 = function () {
                    var self = this;
                    if (self.isFirstGetData()) {
                        self.clearError();
                    }
                    if (!self.checkDirty() || self.notCheckDirty()) {
                        self.currentItem(new PersonalUnitPrice(null, null, null, false, "unitCode", 0, 1, 1, 1, 1, 1, 0, null));
                        self.dirty.reset();
                        self.currentCode("");
                        self.isCreated(true);
                        self.isEnableDelete(false);
                    }
                    else {
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。").ifYes(function () {
                            self.currentItem(new PersonalUnitPrice(null, null, null, false, "unitCode", 0, 1, 1, 1, 1, 1, 0, null));
                            self.dirty.reset();
                            self.currentCode("");
                            self.isCreated(true);
                            self.isEnableDelete(false);
                        });
                    }
                };
                ScreenModel.prototype.closeDialog = function () {
                    var self = this;
                    if (!self.checkDirty()) {
                        nts.uk.ui.windows.close();
                    }
                    else {
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。")
                            .ifYes(function () {
                            nts.uk.ui.windows.close();
                        })
                            .ifNo(function () { });
                    }
                };
                /**
                 * 登録(Add button)
                 */
                ScreenModel.prototype.btn_002 = function () {
                    var self = this;
                    //self.confirmDirty = true;
                    //if input 0-9, auto insert '0' before
                    if (self.currentItem().personalUnitPriceCode() != null && self.currentItem().personalUnitPriceCode().length == 1) {
                        self.currentItem().personalUnitPriceCode("0" + self.currentItem().personalUnitPriceCode());
                    }
                    var PersonalUnitPrice = {
                        personalUnitPriceCode: self.currentItem().personalUnitPriceCode(),
                        personalUnitPriceName: self.currentItem().personalUnitPriceName(),
                        personalUnitPriceShortName: self.currentItem().personalUnitPriceShortName(),
                        displaySet: self.currentItem().displaySet() ? 0 : 1,
                        uniteCode: null,
                        paymentSettingType: self.currentItem().paymentSettingType(),
                        fixPaymentAtr: self.currentItem().fixPaymentAtr(),
                        fixPaymentMonthly: self.currentItem().fixPaymentMonthly(),
                        fixPaymentDayMonth: self.currentItem().fixPaymentDayMonth(),
                        fixPaymentDaily: self.currentItem().fixPaymentDaily(),
                        fixPaymentHoursly: self.currentItem().fixPaymentHoursly(),
                        unitPriceAtr: self.currentItem().unitPriceAtr(),
                        memo: self.currentItem().memo()
                    };
                    a.service.addPersonalUnitPrice(self.isCreated(), PersonalUnitPrice).done(function () {
                        self.getPersonalUnitPriceList();
                        //define update mode or insert mode
                        if (self.currentItem().personalUnitPriceCode() != self.currentCode()) {
                            self.confirmDirty = true;
                        }
                        self.currentCode(PersonalUnitPrice.personalUnitPriceCode);
                        self.isCreated(false);
                        self.dirty.reset();
                    }).fail(function (error) {
                        if (error.messageId == self.messages()[2].messageId) {
                            $('#INP_002').ntsError('set', self.messages()[2].message);
                        }
                        else if (error.messageId == self.messages()[1].messageId) {
                            if (!PersonalUnitPrice.personalUnitPriceCode) {
                                $('#INP_002').ntsError('set', self.messages()[1].message);
                            }
                            if (!PersonalUnitPrice.personalUnitPriceName) {
                                $('#INP_003').ntsError('set', self.messages()[1].message);
                            }
                        }
                        else if (error.messageId == self.messages()[4].messageId) {
                            nts.uk.ui.dialog.alert(self.messages()[4].message);
                        }
                    });
                };
                /**
                 * 削除(Delete button)
                 */
                ScreenModel.prototype.btn_004 = function () {
                    var self = this;
                    nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function () {
                        var data = {
                            personalUnitPriceCode: self.currentItem().personalUnitPriceCode()
                        };
                        self.indexRow(_.findIndex(self.items(), function (x) {
                            return x.code === self.currentCode();
                        }));
                        a.service.removePersonalUnitPrice(data).done(function () {
                            // reload list   
                            self.getPersonalUnitPriceList().done(function () {
                                self.notCheckDirty(true);
                                if (self.items().length > self.indexRow()) {
                                    self.currentCode(self.items()[self.indexRow()].code);
                                }
                                else if (self.indexRow() == 0) {
                                    self.btn_001();
                                }
                                else if (self.items().length == self.indexRow() && self.indexRow() != 0) {
                                    self.currentCode(self.items()[self.indexRow() - 1].code);
                                }
                            });
                        }).fail(function (error) {
                            alert(error.message);
                        });
                    });
                };
                ScreenModel.prototype.selectedFirstUnitPrice = function () {
                    var self = this;
                    if (self.items().length > 0) {
                        self.currentCode(self.items()[0].code);
                        self.selectedUnitPrice(self.items()[0].code);
                    }
                    else {
                        self.btn_001();
                    }
                };
                ScreenModel.prototype.clearError = function () {
                    $('#INP_002').ntsError('clear');
                    $('#INP_003').ntsError('clear');
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var ItemModel = (function () {
                function ItemModel(code, name, abolition) {
                    this.code = code;
                    this.name = name;
                    this.abolition = abolition;
                }
                return ItemModel;
            }());
            var BoxModel = (function () {
                function BoxModel(id, name) {
                    var self = this;
                    self.id = id;
                    self.name = name;
                }
                return BoxModel;
            }());
            var PersonalUnitPrice = (function () {
                function PersonalUnitPrice(personalUnitPriceCode, personalUnitPriceName, personalUnitPriceShortName, displaySet, uniteCode, paymentSettingType, fixPaymentAtr, fixPaymentMonthly, fixPaymentDayMonth, fixPaymentDaily, fixPaymentHoursly, unitPriceAtr, memo) {
                    this.personalUnitPriceCode = ko.observable(personalUnitPriceCode);
                    this.personalUnitPriceName = ko.observable(personalUnitPriceName);
                    this.personalUnitPriceShortName = ko.observable(personalUnitPriceShortName);
                    this.displaySet = ko.observable(displaySet);
                    this.uniteCode = ko.observable(uniteCode);
                    this.paymentSettingType = ko.observable(paymentSettingType);
                    this.fixPaymentAtr = ko.observable(fixPaymentAtr);
                    this.fixPaymentMonthly = ko.observable(fixPaymentMonthly);
                    this.fixPaymentDayMonth = ko.observable(fixPaymentDayMonth);
                    this.fixPaymentDaily = ko.observable(fixPaymentDaily);
                    this.fixPaymentHoursly = ko.observable(fixPaymentHoursly);
                    this.unitPriceAtr = ko.observable(unitPriceAtr);
                    this.memo = ko.observable(memo);
                }
                return PersonalUnitPrice;
            }());
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qmm013.a || (qmm013.a = {}));
})(qmm013 || (qmm013 = {}));
//# sourceMappingURL=qmm013.a.vm.js.map