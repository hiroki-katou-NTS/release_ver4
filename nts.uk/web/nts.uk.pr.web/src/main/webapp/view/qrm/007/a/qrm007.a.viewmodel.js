var qrm007;
(function (qrm007) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            class ScreenModel {
                constructor() {
                    var self = this;
                    self.retirementPayItemList = ko.observableArray([]);
                    self.currentCode = ko.observable("");
                    self.currentItem = ko.observable(new RetirementPayItem("", 0, "", "", "", "", "", ""));
                    self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                }
                startPage() {
                    var self = this;
                    var dfd = $.Deferred();
                    self.findRetirementPayItemList(false)
                        .done(function () {
                        $(document).delegate("#lst-1", "iggridselectionrowselectionchanging", function (evt, ui) {
                            if (self.dirty.isDirty()) {
                                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\nよろしいですか。 ").
                                    ifYes(function () {
                                    $('#inp-1').ntsError('clear');
                                    $('#inp-2').ntsError('clear');
                                    self.currentCode(ui.row.id);
                                    self.currentItem(RetirementPayItem.converToObject(_.find(self.retirementPayItemList(), function (o) { return o.itemCode == self.currentCode(); })));
                                    self.dirty.reset();
                                }).ifNo(function () {
                                    self.currentCode(ui.selectedRows[0].id);
                                });
                            }
                            else {
                                $('#inp-1').ntsError('clear');
                                $('#inp-2').ntsError('clear');
                                self.currentCode(ui.row.id);
                                self.currentItem(RetirementPayItem.converToObject(_.find(self.retirementPayItemList(), function (o) { return o.itemCode == self.currentCode(); })));
                                self.dirty.reset();
                            }
                        });
                        dfd.resolve();
                    }).fail(function (res) {
                        dfd.reject(res);
                    });
                    return dfd.promise();
                }
                /**
                 * find retirement payment item by company code
                 * @param notFirstTime : check current find is first time or not
                 */
                findRetirementPayItemList(notFirstTime) {
                    var self = this;
                    var dfd = $.Deferred();
                    qrm007.a.service.retirePayItemSelect()
                        .done(function (data) {
                        self.retirementPayItemList.removeAll();
                        if (data.length) {
                            data.forEach(function (dataItem) {
                                self.retirementPayItemList.push(ko.mapping.toJS(new RetirementPayItem(dataItem.companyCode, dataItem.category, dataItem.itemCode, dataItem.itemName, dataItem.printName, dataItem.englishName, dataItem.fullName, dataItem.memo)));
                            });
                            if (!notFirstTime) {
                                self.currentCode(_.first(self.retirementPayItemList()).itemCode);
                            }
                            self.currentItem(RetirementPayItem.converToObject(_.find(self.retirementPayItemList(), function (o) { return o.itemCode == self.currentCode(); })));
                            self.dirty = new nts.uk.ui.DirtyChecker(self.currentItem);
                        }
                        dfd.resolve();
                    })
                        .fail(function (res) {
                        self.retirementPayItemList.removeAll();
                        dfd.reject(res);
                    });
                    return dfd.promise();
                }
                /**
                 * update retirement payment item
                 */
                updateRetirementPayItemList() {
                    var self = this;
                    var dfd = $.Deferred();
                    if (self.dirty.isDirty()) {
                        let command = ko.mapping.toJS(self.currentItem());
                        qrm007.a.service.retirePayItemUpdate(command)
                            .done(function (data) {
                            self.findRetirementPayItemList(true);
                            dfd.resolve();
                        }).fail(function (res) {
                            dfd.reject(res);
                        });
                    }
                    return dfd.promise();
                }
                /**
                 * event update selected retirement payment item
                 */
                saveData() {
                    var self = this;
                    self.updateRetirementPayItemList();
                }
            }
            viewmodel.ScreenModel = ScreenModel;
            class RetirementPayItem {
                constructor(companyCode, category, itemCode, itemName, printName, englishName, fullName, memo) {
                    var self = this;
                    self.companyCode = ko.observable(companyCode);
                    self.category = ko.observable(category);
                    self.itemCode = ko.observable(itemCode);
                    self.itemName = ko.observable(itemName);
                    self.printName = ko.observable(printName);
                    self.englishName = ko.observable(englishName);
                    self.fullName = ko.observable(fullName);
                    self.memo = ko.observable(memo);
                }
                static converToObject(object) {
                    return new RetirementPayItem(object.companyCode, object.category, object.itemCode, object.itemName, object.printName, object.englishName, object.fullName, object.memo);
                }
            }
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qrm007.a || (qrm007.a = {}));
})(qrm007 || (qrm007 = {}));
