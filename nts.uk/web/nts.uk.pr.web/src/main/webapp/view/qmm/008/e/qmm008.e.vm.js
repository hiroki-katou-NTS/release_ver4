var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var qmm008;
                (function (qmm008) {
                    var e;
                    (function (e) {
                        var viewmodel;
                        (function (viewmodel) {
                            var bservice = nts.uk.pr.view.qmm008.b.service;
                            var postcodeService = nts.uk.pr.view.base.postcode.service;
                            var ScreenModel = (function () {
                                function ScreenModel() {
                                    var self = this;
                                    self.enabled = ko.observable(true);
                                    self.deleteButtonControll = ko.observable(true);
                                    self.officeItems = ko.observableArray([]);
                                    self.columns2 = ko.observableArray([
                                        { headerText: 'コード', key: 'code', width: 100 },
                                        { headerText: '名称', key: 'name', width: 150 }
                                    ]);
                                    self.listOptions = ko.observableArray([new optionsModel(1, "基本情報"), new optionsModel(2, "保険料マスタの情報")]);
                                    self.selectedValue = ko.observable(new optionsModel(1, ""));
                                    self.isTransistReturnData = ko.observable(nts.uk.ui.windows.getShared("isTransistReturnData"));
                                    self.tabs = ko.observableArray([
                                        { id: 'tab-1', title: '基本情報', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                                        { id: 'tab-2', title: '保険料マスタの情報', content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) },
                                    ]);
                                    self.selectedTab = ko.observable('tab-1');
                                    self.officeModel = ko.observable(new SocialInsuranceOfficeModel());
                                    self.textInputOption = ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                                        textmode: "text",
                                        width: "100",
                                        textalign: "center"
                                    }));
                                    self.selectedOfficeCode = ko.observable('');
                                    self.previousSelectedOfficeCode = ko.observable('');
                                    self.showConfirmDialog = ko.observable(true);
                                    self.errorList = ko.observableArray([
                                        { messageId: "ER001", message: "＊が入力されていません。" },
                                        { messageId: "ER007", message: "＊が選択されていません。" },
                                        { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                                        { messageId: "ER008", message: "選択された＊は使用されているため削除できません。" },
                                        { messageId: "AL001", message: "変更された内容が登録されていません。\r\n よろしいですか。" },
                                        { messageId: "AL002", message: "データを削除します。\r\nよろしいですか？" },
                                    ]);
                                    self.dirty = new nts.uk.ui.DirtyChecker(ko.observable(''));
                                    self.selectedOfficeCode.subscribe(function (selectedOfficeCode) {
                                        if (selectedOfficeCode != '') {
                                            if (self.dirty.isDirty() && self.showConfirmDialog() && selectedOfficeCode != self.previousSelectedOfficeCode()) {
                                                nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function () {
                                                    self.showConfirmDialog(false);
                                                    self.dirty.reset();
                                                    self.loadItemOffice(selectedOfficeCode);
                                                }).ifNo(function () {
                                                    self.selectedOfficeCode(self.previousSelectedOfficeCode());
                                                });
                                            }
                                            else {
                                                if (selectedOfficeCode != self.previousSelectedOfficeCode()) {
                                                    self.dirty.reset();
                                                    self.loadItemOffice(selectedOfficeCode);
                                                }
                                            }
                                        }
                                    });
                                }
                                ScreenModel.prototype.loadItemOffice = function (selectedOfficeCode) {
                                    var self = this;
                                    $('.save-error').ntsError('clear');
                                    if (selectedOfficeCode != null && selectedOfficeCode != undefined && selectedOfficeCode != "") {
                                        self.enabled(false);
                                        self.deleteButtonControll(true);
                                        $.when(self.load(selectedOfficeCode)).done(function () {
                                            self.previousSelectedOfficeCode(selectedOfficeCode);
                                            self.showConfirmDialog(true);
                                        }).fail(function (res) {
                                        });
                                    }
                                };
                                ScreenModel.prototype.start = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    self.loadAllInsuranceOfficeData().done(function () {
                                        if (self.officeItems().length > 0) {
                                        }
                                        else {
                                            self.addNew();
                                        }
                                        dfd.resolve();
                                    });
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.loadAllInsuranceOfficeData = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    bservice.findInsuranceOffice('').done(function (data) {
                                        if (data != null) {
                                            self.officeItems([]);
                                            data.forEach(function (item, index) {
                                                self.officeItems.push(new ItemModel(item.code, item.name));
                                            });
                                            dfd.resolve(data);
                                        }
                                        else {
                                            dfd.resolve(null);
                                        }
                                    });
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.load = function (officeCode) {
                                    if (officeCode != null && officeCode != '') {
                                        var self = this;
                                        e.service.getOfficeItemDetail(officeCode).done(function (data) {
                                            self.officeModel().officeCode(data.code);
                                            self.officeModel().officeName(data.name);
                                            self.officeModel().shortName(data.shortName);
                                            self.officeModel().PicName(data.picName);
                                            self.officeModel().PicPosition(data.picPosition);
                                            self.officeModel().potalCode(data.potalCode);
                                            self.officeModel().address1st(data.address1st);
                                            self.officeModel().kanaAddress1st(data.kanaAddress1st);
                                            self.officeModel().address2nd(data.address2nd);
                                            self.officeModel().kanaAddress2nd(data.kanaAddress2nd);
                                            self.officeModel().phoneNumber(data.phoneNumber);
                                            self.officeModel().healthInsuOfficeRefCode1st(data.healthInsuOfficeRefCode1st);
                                            self.officeModel().healthInsuOfficeRefCode2nd(data.healthInsuOfficeRefCode2nd);
                                            self.officeModel().pensionOfficeRefCode1st(data.pensionOfficeRefCode1st);
                                            self.officeModel().pensionOfficeRefCode2nd(data.pensionOfficeRefCode2nd);
                                            self.officeModel().welfarePensionFundCode(data.welfarePensionFundCode);
                                            self.officeModel().officePensionFundCode(data.officePensionFundCode);
                                            self.officeModel().healthInsuCityCode(data.healthInsuCityCode);
                                            self.officeModel().healthInsuOfficeSign(data.healthInsuOfficeSign);
                                            self.officeModel().pensionCityCode(data.pensionCityCode);
                                            self.officeModel().pensionOfficeSign(data.pensionOfficeSign);
                                            self.officeModel().healthInsuOfficeCode(data.healthInsuOfficeCode);
                                            self.officeModel().healthInsuAssoCode(data.healthInsuAssoCode);
                                            self.officeModel().memo(data.memo);
                                            self.dirty = new nts.uk.ui.DirtyChecker(self.officeModel);
                                        });
                                    }
                                    return;
                                };
                                ScreenModel.prototype.convertDatatoList = function (data) {
                                    var OfficeItemList = [];
                                    data.forEach(function (item, index) {
                                        OfficeItemList.push(new ItemModel(item.code, item.name));
                                    });
                                    return OfficeItemList;
                                };
                                ScreenModel.prototype.save = function () {
                                    var self = this;
                                    if (!self.enabled())
                                        self.updateOffice();
                                    else {
                                        self.registerOffice();
                                    }
                                };
                                ScreenModel.prototype.updateOffice = function () {
                                    var self = this;
                                    e.service.update(self.collectData()).done(function () {
                                        self.loadAllInsuranceOfficeData().done(function () {
                                            self.selectedOfficeCode(self.officeModel().officeCode());
                                        });
                                        self.dirty.reset();
                                    }).fail(function () {
                                    });
                                };
                                ScreenModel.prototype.registerOffice = function () {
                                    var self = this;
                                    e.service.register(self.collectData()).done(function () {
                                        self.loadAllInsuranceOfficeData().done(function () {
                                            self.selectedOfficeCode(self.officeModel().officeCode());
                                        });
                                        self.dirty.reset();
                                    }).fail(function (res) {
                                        if (res.messageId == self.errorList()[2].messageId) {
                                            $('#officeCode').ntsError('set', self.errorList()[2].message);
                                        }
                                        if (res.messageId == self.errorList()[0].messageId) {
                                            if (!self.officeModel().officeCode())
                                                $('#officeCode').ntsError('set', self.errorList()[0].message);
                                            if (!self.officeModel().officeName())
                                                $('#officeName').ntsError('set', self.errorList()[0].message);
                                            if (!self.officeModel().PicPosition())
                                                $('#picPosition').ntsError('set', self.errorList()[0].message);
                                        }
                                    });
                                };
                                ScreenModel.prototype.removeWithDirtyCheck = function () {
                                    var self = this;
                                    nts.uk.ui.dialog.confirm(self.errorList()[5].message).ifYes(function () {
                                        self.dirty.reset();
                                        self.remove();
                                    }).ifNo(function () {
                                    });
                                };
                                ScreenModel.prototype.remove = function () {
                                    var self = this;
                                    if (self.selectedOfficeCode() != '') {
                                        e.service.remove(self.selectedOfficeCode()).done(function () {
                                        }).fail(function () {
                                        });
                                        self.loadAllInsuranceOfficeData().done(function () {
                                            if (self.officeItems().length == 0) {
                                                self.addNew();
                                            }
                                            else {
                                                self.selectedOfficeCode(self.officeItems()[0].code);
                                            }
                                        });
                                    }
                                };
                                ScreenModel.prototype.collectData = function () {
                                    var self = this;
                                    var a = new e.service.model.finder.OfficeItemDto("company code", self.officeModel().officeCode(), self.officeModel().officeName(), self.officeModel().shortName(), self.officeModel().PicName(), self.officeModel().PicPosition(), self.officeModel().potalCode(), self.officeModel().address1st(), self.officeModel().address2nd(), self.officeModel().kanaAddress1st(), self.officeModel().kanaAddress2nd(), self.officeModel().phoneNumber(), self.officeModel().healthInsuOfficeRefCode1st(), self.officeModel().healthInsuOfficeRefCode2nd(), self.officeModel().pensionOfficeRefCode1st(), self.officeModel().pensionOfficeRefCode2nd(), self.officeModel().welfarePensionFundCode(), self.officeModel().officePensionFundCode(), self.officeModel().healthInsuCityCode(), self.officeModel().healthInsuOfficeSign(), self.officeModel().pensionCityCode(), self.officeModel().pensionOfficeSign(), self.officeModel().healthInsuOfficeCode(), self.officeModel().healthInsuAssoCode(), self.officeModel().memo());
                                    return a;
                                };
                                ScreenModel.prototype.addNewWithDirtyCheck = function () {
                                    var self = this;
                                    if (self.dirty.isDirty()) {
                                        nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function () {
                                            self.addNew();
                                            self.dirty.reset();
                                        }).ifNo(function () {
                                        });
                                    }
                                    else {
                                        self.addNew();
                                        self.showConfirmDialog(true);
                                    }
                                };
                                ScreenModel.prototype.addNew = function () {
                                    var self = this;
                                    self.officeModel().officeCode('');
                                    self.officeModel().officeName('');
                                    self.officeModel().shortName('');
                                    self.officeModel().PicName('');
                                    self.officeModel().PicPosition('');
                                    self.officeModel().potalCode('');
                                    self.officeModel().address1st('');
                                    self.officeModel().kanaAddress1st('');
                                    self.officeModel().address2nd('');
                                    self.officeModel().kanaAddress2nd('');
                                    self.officeModel().phoneNumber('');
                                    self.officeModel().healthInsuOfficeRefCode1st('');
                                    self.officeModel().healthInsuOfficeRefCode2nd('');
                                    self.officeModel().pensionOfficeRefCode1st('');
                                    self.officeModel().pensionOfficeRefCode2nd('');
                                    self.officeModel().welfarePensionFundCode('');
                                    self.officeModel().officePensionFundCode('');
                                    self.officeModel().healthInsuCityCode('');
                                    self.officeModel().healthInsuOfficeSign('');
                                    self.officeModel().pensionCityCode('');
                                    self.officeModel().pensionOfficeSign('');
                                    self.officeModel().healthInsuOfficeCode('');
                                    self.officeModel().healthInsuAssoCode('');
                                    self.officeModel().memo('');
                                    self.enabled(true);
                                    self.deleteButtonControll(false);
                                    self.selectedOfficeCode('');
                                    self.showConfirmDialog(false);
                                    self.previousSelectedOfficeCode('');
                                    self.dirty = new nts.uk.ui.DirtyChecker(self.officeModel);
                                };
                                ScreenModel.prototype.closeDialogWithDirtyCheck = function () {
                                    var self = this;
                                    if (self.dirty.isDirty()) {
                                        nts.uk.ui.dialog.confirm(self.errorList()[4].message).ifYes(function () {
                                            nts.uk.ui.windows.setShared("codeOfNewOffice", null, this.isTransistReturnData());
                                            nts.uk.ui.windows.close();
                                            self.dirty.reset();
                                        }).ifNo(function () {
                                        });
                                    }
                                    else {
                                        self.closeDialog();
                                    }
                                };
                                ScreenModel.prototype.closeDialog = function () {
                                    var self = this;
                                    nts.uk.ui.windows.setShared("codeOfNewOffice", self.officeModel().officeCode(), this.isTransistReturnData());
                                    nts.uk.ui.windows.close();
                                };
                                return ScreenModel;
                            }());
                            viewmodel.ScreenModel = ScreenModel;
                            var SocialInsuranceOfficeModel = (function () {
                                function SocialInsuranceOfficeModel() {
                                    this.officeCode = ko.observable('');
                                    this.officeName = ko.observable('');
                                    this.shortName = ko.observable('');
                                    this.PicName = ko.observable('');
                                    this.PicPosition = ko.observable('');
                                    this.potalCode = ko.observable('');
                                    this.address1st = ko.observable('');
                                    this.kanaAddress1st = ko.observable('');
                                    this.address2nd = ko.observable('');
                                    this.kanaAddress2nd = ko.observable('');
                                    this.phoneNumber = ko.observable('');
                                    this.healthInsuOfficeRefCode1st = ko.observable('');
                                    this.healthInsuOfficeRefCode2nd = ko.observable('');
                                    this.pensionOfficeRefCode1st = ko.observable('');
                                    this.pensionOfficeRefCode2nd = ko.observable('');
                                    this.welfarePensionFundCode = ko.observable('');
                                    this.officePensionFundCode = ko.observable('');
                                    this.healthInsuCityCode = ko.observable('');
                                    this.healthInsuOfficeSign = ko.observable('');
                                    this.pensionCityCode = ko.observable('');
                                    this.pensionOfficeSign = ko.observable('');
                                    this.healthInsuOfficeCode = ko.observable('');
                                    this.healthInsuAssoCode = ko.observable('');
                                    this.memo = ko.observable('');
                                }
                                SocialInsuranceOfficeModel.prototype.setPostCode = function (postcode) {
                                    var self = this;
                                    self.potalCode(postcode.postcode);
                                    self.address1st(postcodeService.toAddress(postcode));
                                    self.kanaAddress1st(postcodeService.toKana(postcode));
                                };
                                SocialInsuranceOfficeModel.prototype.searchPostCode = function () {
                                    var self = this;
                                    var messageList = [
                                        { messageId: "ER001", message: "＊が入力されていません。" },
                                        { messageId: "ER005", message: "入力した＊は既に存在しています。\r\n ＊を確認してください。" },
                                        { messageId: "ER010", message: "対象データがありません。" }
                                    ];
                                    postcodeService.findPostCodeZipCodeToRespone(self.potalCode()).done(function (data) {
                                        if (data.errorCode == '0') {
                                            for (var _i = 0, messageList_1 = messageList; _i < messageList_1.length; _i++) {
                                                var datamessage = messageList_1[_i];
                                                if (datamessage.messageId == data.message) {
                                                    $('#inp_postCode').ntsError('set', datamessage.message);
                                                }
                                            }
                                        }
                                        else if (data.errorCode == '1') {
                                            self.setPostCode(data.postcode);
                                            $('#inp_postCode').ntsError('clear');
                                        }
                                        else {
                                            postcodeService.findPostCodeZipCodeSelection(self.potalCode()).done(function (res) {
                                                if (res.errorCode == '0') {
                                                    for (var _i = 0, messageList_2 = messageList; _i < messageList_2.length; _i++) {
                                                        var datamessage = messageList_2[_i];
                                                        if (datamessage.messageId == res.message) {
                                                            $('#inp_postCode').ntsError('set', datamessage.message);
                                                        }
                                                    }
                                                }
                                                else if (res.errorCode == '1') {
                                                    self.setPostCode(res.postcode);
                                                    $('#inp_postCode').ntsError('clear');
                                                }
                                            }).fail(function (error) {
                                                console.log(error);
                                            });
                                        }
                                    }).fail(function (error) {
                                        console.log(error);
                                    });
                                };
                                return SocialInsuranceOfficeModel;
                            }());
                            viewmodel.SocialInsuranceOfficeModel = SocialInsuranceOfficeModel;
                            var ItemModel = (function () {
                                function ItemModel(code, name) {
                                    this.code = code;
                                    this.name = name;
                                }
                                return ItemModel;
                            }());
                            viewmodel.ItemModel = ItemModel;
                            var optionsModel = (function () {
                                function optionsModel(id, name) {
                                    var self = this;
                                    self.id = id;
                                    self.name = name;
                                }
                                return optionsModel;
                            }());
                            viewmodel.optionsModel = optionsModel;
                        })(viewmodel = e.viewmodel || (e.viewmodel = {}));
                    })(e = qmm008.e || (qmm008.e = {}));
                })(qmm008 = view.qmm008 || (view.qmm008 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
