var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var qpp007;
                (function (qpp007) {
                    var c;
                    (function (c) {
                        var viewmodel;
                        (function (viewmodel) {
                            var ScreenModel = (function () {
                                function ScreenModel() {
                                    var self = this;
                                    self.isLoading = ko.observable(true);
                                    self.isNewMode = ko.observable(true);
                                    self.outputSettings = ko.observableArray([]);
                                    self.outputSettingSelectedCode = ko.observable('');
                                    self.temporarySelectedCode = ko.observable('');
                                    self.outputSettingDetailModel = ko.observable(new OutputSettingDetailModel(ko.observableArray([])));
                                    self.reportItems = ko.observableArray([]);
                                    self.reportItemSelected = ko.observable('');
                                    self.allAggregateItems = ko.observableArray([]);
                                    self.dirtyChecker = new nts.uk.ui.DirtyChecker(self.outputSettingDetailModel);
                                    for (var i = 1; i < 30; i++) {
                                        this.outputSettings.push(new OutputSettingHeader('00' + i, '基本給' + i));
                                    }
                                    this.reportItemColumns = ko.observableArray([
                                        { headerText: '区分', prop: 'categoryNameJPN', width: 50 },
                                        {
                                            headerText: '集約', prop: 'isAggregate', width: 40,
                                            formatter: function (isAggregate) {
                                                if (isAggregate == 'true') {
                                                    return '<div class="halign-center"><i class="icon icon-dot"></i></div>';
                                                }
                                                return '';
                                            }
                                        },
                                        { headerText: 'コード', prop: 'code', width: 100 },
                                        { headerText: '名称', prop: 'name', width: 100 },
                                    ]);
                                    self.outputSettingDetailModel.subscribe(function (data) {
                                        self.reloadReportItems();
                                        data.reloadReportItems = self.reloadReportItems.bind(self);
                                    });
                                    self.temporarySelectedCode.subscribe(function (code) {
                                        if (!code) {
                                            return;
                                        }
                                        var executeIfConfirmed = function () {
                                            self.outputSettingSelectedCode(self.temporarySelectedCode());
                                            self.onSelectOutputSetting(code);
                                        };
                                        var executeIfCanceled = function () {
                                            self.temporarySelectedCode(self.outputSettingSelectedCode());
                                        };
                                        if (self.outputSettingSelectedCode() == code) {
                                            return;
                                        }
                                        else {
                                            self.confirmDirtyAndExecute(executeIfConfirmed, executeIfCanceled);
                                        }
                                    });
                                }
                                ScreenModel.prototype.startPage = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    $.when(self.loadAllOutputSetting(), self.loadMasterItems(), self.loadAggregateItems()).done(function () {
                                        self.isLoading(false);
                                        if (!self.outputSettings || self.outputSettings().length == 0) {
                                            self.enableNewMode();
                                        }
                                        else {
                                            self.temporarySelectedCode(self.outputSettings()[0].code);
                                        }
                                        dfd.resolve();
                                    });
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.onCommonSettingBtnClicked = function () {
                                    var self = this;
                                    nts.uk.ui.windows.sub.modal('/view/qpp/007/j/index.xhtml', { title: '集計項目の設定', dialogClass: 'no-close' })
                                        .onClosed(function () {
                                        self.loadAggregateItems().done(function () {
                                            self.loadOutputSettingDetail(self.outputSettingDetailModel().settingCode());
                                        });
                                    });
                                };
                                ScreenModel.prototype.onNewModeBtnClicked = function () {
                                    var self = this;
                                    self.confirmDirtyAndExecute(function () {
                                        self.clearError();
                                        self.enableNewMode();
                                    });
                                };
                                ScreenModel.prototype.onSaveBtnClicked = function () {
                                    var self = this;
                                    self.validate();
                                    if (!nts.uk.ui._viewModel.errors.isEmpty()) {
                                        return;
                                    }
                                    var data = self.collectData();
                                    if (self.isNewMode()) {
                                        data.createMode = true;
                                    }
                                    else {
                                        data.createMode = false;
                                    }
                                    c.service.save(data).done(function () {
                                        self.isNewMode(false);
                                        self.dirtyChecker.reset();
                                        self.loadAllOutputSetting();
                                    }).fail(function (res) {
                                        console.log(res);
                                    });
                                };
                                ScreenModel.prototype.onRemoveBtnClicked = function () {
                                    var self = this;
                                    if (self.outputSettingSelectedCode) {
                                        nts.uk.ui.dialog.confirm("データを削除します。\r\n よろしいですか？").ifYes(function () {
                                            c.service.remove(self.outputSettingSelectedCode()).done(function () {
                                                self.loadAllOutputSetting();
                                                if (!self.outputSettings || self.outputSettings.length == 0) {
                                                    self.clearError();
                                                    self.enableNewMode();
                                                }
                                            });
                                        });
                                    }
                                };
                                ScreenModel.prototype.onCloseBtnClicked = function () {
                                    var self = this;
                                    self.confirmDirtyAndExecute(function () {
                                        nts.uk.ui.windows.close();
                                    });
                                };
                                ScreenModel.prototype.onSelectOutputSetting = function (code) {
                                    var self = this;
                                    self.loadOutputSettingDetail(code).done(function () {
                                        self.isNewMode(false);
                                        self.isLoading(false);
                                        self.clearError();
                                    });
                                };
                                ScreenModel.prototype.collectData = function () {
                                    var self = this;
                                    var model = self.outputSettingDetailModel();
                                    var data = new OutputSettingDto();
                                    data.code = model.settingCode();
                                    data.name = model.settingName();
                                    var settings = new Array();
                                    model.categorySettings().forEach(function (setting) {
                                        settings.push(new CategorySettingDto(setting.categoryName, setting.outputItems().map(function (item) {
                                            var mappedItem = item;
                                            if (!item.isAggregateItem) {
                                                mappedItem = new OutputItem();
                                                mappedItem.code = item.code;
                                                mappedItem.name = item.name;
                                                mappedItem.isAggregateItem = false;
                                            }
                                            return mappedItem;
                                        })));
                                    });
                                    data.categorySettings = settings;
                                    return data;
                                };
                                ScreenModel.prototype.clearError = function () {
                                    if (nts.uk.ui._viewModel) {
                                        $('#inpCode').ntsError('clear');
                                        $('#inpName').ntsError('clear');
                                    }
                                };
                                ScreenModel.prototype.validate = function () {
                                    $('#inpCode').ntsEditor('validate');
                                    $('#inpName').ntsEditor('validate');
                                };
                                ScreenModel.prototype.confirmDirtyAndExecute = function (functionToExecute, functionToExecuteIfNo) {
                                    var self = this;
                                    if (self.dirtyChecker.isDirty()) {
                                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。\r\n よろしいですか。").ifYes(function () {
                                            functionToExecute();
                                        }).ifNo(function () {
                                            if (functionToExecuteIfNo) {
                                                functionToExecuteIfNo();
                                            }
                                        });
                                    }
                                    else {
                                        functionToExecute();
                                    }
                                };
                                ScreenModel.prototype.enableNewMode = function () {
                                    var self = this;
                                    self.outputSettingDetailModel(new OutputSettingDetailModel(ko.observableArray([])));
                                    self.outputSettingSelectedCode(null);
                                    self.dirtyChecker.reset();
                                    self.isNewMode(true);
                                };
                                ScreenModel.prototype.reloadReportItems = function () {
                                    var self = this;
                                    var data = self.outputSettingDetailModel();
                                    if (!data || data.categorySettings().length == 0) {
                                        self.reportItems([]);
                                        return;
                                    }
                                    var reportItemList = [];
                                    data.categorySettings().forEach(function (setting) {
                                        var categoryName = setting.categoryName;
                                        setting.outputItems().forEach(function (item) {
                                            reportItemList.push(new ReportItem(item.code, item.name, categoryName, item.isAggregateItem));
                                        });
                                    });
                                    self.reportItems(reportItemList);
                                };
                                ScreenModel.prototype.loadAllOutputSetting = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    c.service.findAllOutputSettings().done(function (data) {
                                        self.outputSettings(data);
                                        dfd.resolve();
                                    }).fail(function (res) {
                                        nts.uk.ui.dialog.alert(res);
                                        dfd.reject();
                                    });
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.loadOutputSettingDetail = function (code) {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    c.service.findOutputSettingDetail(code).done(function (data) {
                                        self.outputSettingDetailModel(new OutputSettingDetailModel(self.allAggregateItems, data));
                                        self.dirtyChecker.reset();
                                        dfd.resolve();
                                    }).fail(function (res) {
                                        nts.uk.ui.dialog.alert(res);
                                        dfd.reject();
                                    });
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.loadAggregateItems = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    c.service.findAllAggregateItems().done(function (res) {
                                        self.allAggregateItems.removeAll();
                                        res.forEach(function (item) {
                                            self.allAggregateItems.push({
                                                code: item.salaryAggregateItemCode,
                                                name: item.salaryAggregateItemName,
                                                taxDivision: item.taxDivision,
                                            });
                                        });
                                        dfd.resolve();
                                    });
                                    return dfd.promise();
                                };
                                ScreenModel.prototype.loadMasterItems = function () {
                                    var dfd = $.Deferred();
                                    dfd.resolve();
                                    return dfd.promise();
                                };
                                return ScreenModel;
                            }());
                            viewmodel.ScreenModel = ScreenModel;
                            var OutputSettingHeader = (function () {
                                function OutputSettingHeader(code, name) {
                                    this.code = code;
                                    this.name = name;
                                }
                                return OutputSettingHeader;
                            }());
                            viewmodel.OutputSettingHeader = OutputSettingHeader;
                            var OutputSettingDto = (function () {
                                function OutputSettingDto() {
                                }
                                return OutputSettingDto;
                            }());
                            viewmodel.OutputSettingDto = OutputSettingDto;
                            var OutputSettingDetailModel = (function () {
                                function OutputSettingDetailModel(aggregateItems, outputSetting) {
                                    this.settingCode = ko.observable(outputSetting != undefined ? outputSetting.code : '');
                                    this.settingName = ko.observable(outputSetting != undefined ? outputSetting.name : '');
                                    this.aggregateItems = aggregateItems;
                                    var settings = [];
                                    if (outputSetting == undefined) {
                                        settings = this.toModel();
                                    }
                                    else {
                                        settings = this.toModel(outputSetting.categorySettings);
                                    }
                                    this.categorySettings = ko.observableArray(settings);
                                    this.categorySettingTabs = ko.observableArray([
                                        { id: SalaryCategory.PAYMENT, title: '支給', content: '#payment', enable: ko.observable(true), visible: ko.observable(true) },
                                        { id: SalaryCategory.DEDUCTION, title: '控除', content: '#deduction', enable: ko.observable(true), visible: ko.observable(true) },
                                        { id: SalaryCategory.ATTENDANCE, title: '勤怠', content: '#attendance', enable: ko.observable(true), visible: ko.observable(true) },
                                        { id: SalaryCategory.ARTICLE_OTHERS, title: '記事・その他', content: '#article-others', enable: ko.observable(true), visible: ko.observable(true) }
                                    ]);
                                    this.selectedCategory = ko.observable(SalaryCategory.PAYMENT);
                                    var self = this;
                                    self.categorySettings().forEach(function (setting) {
                                        setting.outputItems.subscribe(function (newValue) {
                                            self.reloadReportItems();
                                        });
                                    });
                                }
                                OutputSettingDetailModel.prototype.toModel = function (categorySettings) {
                                    var settings = [];
                                    var test;
                                    if (categorySettings != undefined && categorySettings.length > 0) {
                                        test = categorySettings;
                                    }
                                    settings[0] = this.filterSettingByCategory(SalaryCategory.PAYMENT, test);
                                    settings[1] = this.filterSettingByCategory(SalaryCategory.DEDUCTION, test);
                                    settings[2] = this.filterSettingByCategory(SalaryCategory.ATTENDANCE, test);
                                    settings[3] = this.filterSettingByCategory(SalaryCategory.ARTICLE_OTHERS, test);
                                    return settings;
                                };
                                OutputSettingDetailModel.prototype.filterSettingByCategory = function (category, categorySettings) {
                                    var cateTempSetting = { category: category, outputItems: [] };
                                    if (categorySettings == undefined) {
                                        return new CategorySettingModel(category, this.aggregateItems, cateTempSetting);
                                    }
                                    var categorySetting = categorySettings.filter(function (item) { return item.category == category; })[0];
                                    if (categorySetting == undefined) {
                                        categorySetting = cateTempSetting;
                                    }
                                    return new CategorySettingModel(category, this.aggregateItems, categorySetting);
                                };
                                return OutputSettingDetailModel;
                            }());
                            viewmodel.OutputSettingDetailModel = OutputSettingDetailModel;
                            var CategorySettingDto = (function () {
                                function CategorySettingDto(category, outputItems) {
                                    this.category = category;
                                    this.outputItems = outputItems;
                                }
                                return CategorySettingDto;
                            }());
                            viewmodel.CategorySettingDto = CategorySettingDto;
                            var ReportItem = (function () {
                                function ReportItem(code, name, categoryName, isAggregate) {
                                    this.code = code;
                                    this.name = name;
                                    this.isAggregate = isAggregate;
                                    var self = this;
                                    switch (categoryName) {
                                        case SalaryCategory.PAYMENT:
                                            self.categoryNameJPN = '支給';
                                            break;
                                        case SalaryCategory.DEDUCTION:
                                            self.categoryNameJPN = '控除';
                                            break;
                                        case SalaryCategory.ATTENDANCE:
                                            self.categoryNameJPN = '勤怠';
                                            break;
                                        case SalaryCategory.ARTICLE_OTHERS:
                                            self.categoryNameJPN = '記事・その他';
                                            break;
                                        default:
                                            self.categoryNameJPN = '';
                                    }
                                }
                                return ReportItem;
                            }());
                            viewmodel.ReportItem = ReportItem;
                            var CategorySettingModel = (function () {
                                function CategorySettingModel(categoryName, aggregateItems, categorySetting) {
                                    var self = this;
                                    self.categoryName = categoryName;
                                    self.aggregateItems = ko.observableArray([]);
                                    self.aggregateItemsSelected = ko.observableArray([]);
                                    self.masterItems = ko.observableArray([]);
                                    self.masterItemsSelected = ko.observableArray([]);
                                    self.outputItems = ko.observableArray(categorySetting != undefined ? categorySetting.outputItems : []);
                                    self.outputItemSelected = ko.observable(null);
                                    self.outputItemsSelected = ko.observableArray([]);
                                    if (categoryName == SalaryCategory.PAYMENT) {
                                        aggregateItems().forEach(function (item) {
                                            if (item.taxDivision == TaxDivision.PAYMENT) {
                                                self.aggregateItems.push(item);
                                            }
                                        });
                                    }
                                    if (categoryName == SalaryCategory.DEDUCTION) {
                                        aggregateItems().forEach(function (item) {
                                            if (item.taxDivision == TaxDivision.DEDUCTION) {
                                                self.aggregateItems.push(item);
                                            }
                                        });
                                    }
                                    for (var i = 1; i < 15; i++) {
                                        this.masterItems.push({ code: '00' + i, name: '基本給' + i, paymentType: 'Salary', taxDivision: 'Deduction' });
                                    }
                                    this.outputItemColumns = ko.observableArray([
                                        {
                                            headerText: '集約', prop: 'isAggregateItem', width: 40,
                                            formatter: function (data) {
                                                if (data == 'true') {
                                                    return '<div class="halign-center"><i class="icon icon-dot"></i></div>';
                                                }
                                                return '';
                                            }
                                        },
                                        { headerText: 'コード', prop: 'code', width: 50 },
                                        { headerText: '名称', prop: 'name', width: 60 },
                                        {
                                            headerText: '削除', prop: 'code', width: 60,
                                            formatter: function (code) {
                                                return '<div class="halign-center"><button class="icon icon-close" id="' + code + '" >'
                                                    + '</button></div>';
                                            }
                                        }
                                    ]);
                                    ko.bindingHandlers.rended = {
                                        init: function (element, valueAccessor, allBindings, viewModel, bindingContext) { },
                                        update: function (element, valueAccessor, allBindings, viewModel, bindingContext) {
                                            var code = valueAccessor();
                                            viewModel.outputItems().forEach(function (item) {
                                                $('#' + item.code).on('click', function () {
                                                    code(item.code);
                                                    viewModel.remove();
                                                    code(null);
                                                });
                                            });
                                        }
                                    };
                                }
                                CategorySettingModel.prototype.moveMasterItem = function () {
                                    if (this.masterItemsSelected()[0]) {
                                        var self = this;
                                        var selectedItems = [];
                                        self.masterItemsSelected().forEach(function (selectedCode) {
                                            selectedItems.push(self.masterItems().filter(function (item) {
                                                return selectedCode == item.code;
                                            })[0]);
                                        });
                                        selectedItems.forEach(function (item) {
                                            self.masterItems.remove(item);
                                            self.outputItems.push({
                                                code: item.code,
                                                name: item.name,
                                                isAggregateItem: false,
                                            });
                                        });
                                        self.masterItemsSelected([]);
                                    }
                                };
                                CategorySettingModel.prototype.moveAggregateItem = function () {
                                    if (this.aggregateItemsSelected()[0]) {
                                        var self = this;
                                        var selectedItems = [];
                                        self.aggregateItemsSelected().forEach(function (selectedCode) {
                                            selectedItems.push(self.aggregateItems().filter(function (item) {
                                                return selectedCode == item.code;
                                            })[0]);
                                        });
                                        selectedItems.forEach(function (item) {
                                            self.aggregateItems.remove(item);
                                            self.outputItems.push({
                                                code: item.code,
                                                name: item.name,
                                                isAggregateItem: true,
                                            });
                                        });
                                        self.aggregateItemsSelected([]);
                                    }
                                };
                                CategorySettingModel.prototype.remove = function () {
                                    var self = this;
                                    var selectedItem = self.outputItems().filter(function (item) {
                                        return item.code == self.outputItemSelected();
                                    })[0];
                                    self.outputItems.remove(selectedItem);
                                    if (selectedItem.isAggregateItem) {
                                        self.aggregateItems.push({
                                            code: selectedItem.code,
                                            name: selectedItem.name,
                                            taxDivision: TaxDivision.PAYMENT,
                                        });
                                        return;
                                    }
                                    self.masterItems.push({
                                        code: selectedItem.code,
                                        name: selectedItem.name,
                                        paymentType: PaymentType.SALARY,
                                        taxDivision: TaxDivision.DEDUCTION
                                    });
                                };
                                return CategorySettingModel;
                            }());
                            viewmodel.CategorySettingModel = CategorySettingModel;
                            var AggregateItem = (function () {
                                function AggregateItem() {
                                }
                                return AggregateItem;
                            }());
                            viewmodel.AggregateItem = AggregateItem;
                            var MasterItem = (function () {
                                function MasterItem() {
                                }
                                return MasterItem;
                            }());
                            viewmodel.MasterItem = MasterItem;
                            var OutputItem = (function () {
                                function OutputItem() {
                                }
                                return OutputItem;
                            }());
                            viewmodel.OutputItem = OutputItem;
                            var SalaryCategory = (function () {
                                function SalaryCategory() {
                                }
                                SalaryCategory.PAYMENT = 'Payment';
                                SalaryCategory.DEDUCTION = 'Deduction';
                                SalaryCategory.ATTENDANCE = 'Attendance';
                                SalaryCategory.ARTICLE_OTHERS = 'ArticleOthers';
                                return SalaryCategory;
                            }());
                            viewmodel.SalaryCategory = SalaryCategory;
                            var TaxDivision = (function () {
                                function TaxDivision() {
                                }
                                TaxDivision.PAYMENT = 'Payment';
                                TaxDivision.DEDUCTION = 'Deduction';
                                return TaxDivision;
                            }());
                            viewmodel.TaxDivision = TaxDivision;
                            var SalaryOutputDistinction = (function () {
                                function SalaryOutputDistinction() {
                                }
                                SalaryOutputDistinction.HOURLY = 'Hourly';
                                SalaryOutputDistinction.MINUTLY = 'Minutely';
                                return SalaryOutputDistinction;
                            }());
                            viewmodel.SalaryOutputDistinction = SalaryOutputDistinction;
                            var PaymentType = (function () {
                                function PaymentType() {
                                }
                                PaymentType.SALARY = 'Salary';
                                PaymentType.BONUS = 'Bonus';
                                return PaymentType;
                            }());
                            viewmodel.PaymentType = PaymentType;
                        })(viewmodel = c.viewmodel || (c.viewmodel = {}));
                    })(c = qpp007.c || (qpp007.c = {}));
                })(qpp007 = view.qpp007 || (view.qpp007 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
