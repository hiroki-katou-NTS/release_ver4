var nts;
(function (nts) {
    var qmm017;
    (function (qmm017) {
        var ScreenModel = (function () {
            function ScreenModel() {
                var self = this;
                self.itemsBagRepository = [];
                self.isNewMode = ko.observable(true);
                self.isUpdateMode = ko.observable(false);
                self.isNewMode.subscribe(function (val) {
                    self.isUpdateMode(!val);
                });
                self.treeGridHistory = ko.observable(new TreeGrid());
                self.a_sel_001 = ko.observableArray([
                    { id: 'tab-1', title: '基本情報', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                    { id: 'tab-2', title: '計算式の設定', content: '.tab-content-2', enable: self.isUpdateMode, visible: ko.observable(true) }
                ]);
                self.selectedTabASel001 = ko.observable('tab-1');
                self.startYearMonth = ko.observable('');
                self.startYearMonth.subscribe(function (newValue) {
                    self.viewModel017b().startYearMonth(newValue);
                });
                self.currentNode = ko.observable(null);
                self.currentParentNode = ko.observable(null);
                self.treeGridHistory().singleSelectedCode.subscribe(function (codeChange) {
                    if (codeChange !== null) {
                        self.bindDataByChanging(codeChange);
                    }
                });
                self.viewModel017b = ko.observable(new qmm017.BScreen(self));
                self.viewModel017c = ko.observable(new qmm017.CScreen(self));
                self.viewModel017d = ko.observable(new qmm017.DScreen());
                self.viewModel017e = ko.observable(new qmm017.EScreen());
                self.viewModel017f = ko.observable(new qmm017.FScreen());
                self.viewModel017g = ko.observable(new qmm017.GScreen());
                self.viewModel017h = ko.observable(new qmm017.HScreen(self));
                self.viewModel017i = ko.observable(new qmm017.IScreen(self));
                self.viewModel017r = ko.observable(new qmm017.RScreen(self));
            }
            ScreenModel.prototype.bindDataByChanging = function (codeChange) {
                var self = this;
                if (codeChange !== null) {
                    var currentNode = null;
                    var currentParentNode = null;
                    self.isNewMode(false);
                    for (var order = 0; order < self.treeGridHistory().items().length; order++) {
                        var foundNode = findNode(self.treeGridHistory().items()[order], self.treeGridHistory().singleSelectedCode());
                        if (foundNode) {
                            self.currentNode(foundNode);
                            self.viewModel017b().historyId(foundNode.code);
                            self.currentParentNode(self.treeGridHistory().items()[order]);
                        }
                    }
                    var rangeYearMonth = self.currentNode().name.split('~');
                    self.startYearMonth(rangeYearMonth[0].trim());
                    self.a_sel_001()[1].enable(true);
                    self.viewModel017b().formulaCode(self.currentParentNode().code);
                    self.viewModel017b().formulaName(self.currentParentNode().name);
                    qmm017.service.findFormula(self.currentParentNode().code, self.currentNode().code)
                        .done(function (currentFormula) {
                        self.viewModel017b().selectedDifficultyAtr(currentFormula.difficultyAtr);
                        qmm017.service.getFormulaDetail(self.currentParentNode().code, self.currentNode().code, currentFormula.difficultyAtr)
                            .done(function (currentFormulaDetail) {
                            if (currentFormula && currentFormula.difficultyAtr === 1) {
                                self.itemsBagRepository = [];
                                self.fillItemsBagRepository().done(function () {
                                    self.viewModel017c().formulaManualContent().textArea(self.replaceCodesToNames(currentFormulaDetail.formulaContent));
                                    self.viewModel017c().comboBoxReferenceMonthAtr().selectedCode(currentFormulaDetail.referenceMonthAtr);
                                    self.viewModel017c().comboBoxRoudingMethod().selectedCode(currentFormulaDetail.roundAtr);
                                    self.viewModel017c().comboBoxRoudingPosition().selectedCode(currentFormulaDetail.roundDigit);
                                }).fail(function (res) {
                                    alert(res);
                                });
                            }
                            else if (currentFormula.difficultyAtr === 0 && currentFormula.conditionAtr === 0) {
                                self.viewModel017c().noneConditionalEasyFormula(new qmm017.EasyFormula(0, self.viewModel017b));
                                if (currentFormulaDetail.easyFormula[0]) {
                                    self.viewModel017c().noneConditionalEasyFormula().easyFormulaName(currentFormulaDetail.easyFormula[0].formulaEasyDetail.easyFormulaName);
                                }
                            }
                            else if (currentFormula.difficultyAtr === 0 && currentFormula.conditionAtr === 1 && currentFormula.refMasterNo < 6) {
                                self.viewModel017c().defaultEasyFormula(new qmm017.EasyFormula(0, self.viewModel017b));
                                if (currentFormulaDetail.easyFormula[0]) {
                                    self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(currentFormulaDetail.easyFormula[0].value);
                                    self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings(currentFormulaDetail.easyFormula[0].fixFormulaAtr);
                                    self.viewModel017c().defaultEasyFormula().easyFormulaCode(currentFormulaDetail.easyFormula[0].formulaEasyDetail.easyFormulaCode);
                                    self.viewModel017c().defaultEasyFormula().easyFormulaName(currentFormulaDetail.easyFormula[0].formulaEasyDetail.easyFormulaName);
                                }
                            }
                            else if (currentFormula.difficultyAtr === 0 && currentFormula.conditionAtr === 1 && currentFormula.refMasterNo === 6) {
                                self.viewModel017c().defaultEasyFormula(new qmm017.EasyFormula(0, self.viewModel017b));
                                _.forEach(currentFormulaDetail.easyFormula, function (easyFormula) {
                                    if (easyFormula.easyFormulaCode === '000') {
                                        self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(easyFormula.value);
                                        self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings(easyFormula.fixFormulaAtr);
                                        if (easyFormula.fixFormulaAtr === 1) {
                                            self.viewModel017c().defaultEasyFormula().easyFormulaCode(easyFormula.formulaEasyDetail.easyFormulaCode);
                                            self.viewModel017c().defaultEasyFormula().easyFormulaName(easyFormula.formulaEasyDetail.easyFormulaName);
                                        }
                                    }
                                    if (easyFormula.easyFormulaCode === '001') {
                                        self.viewModel017c().monthlyEasyFormula().easyFormulaFixMoney(easyFormula.value);
                                        self.viewModel017c().monthlyEasyFormula().selectedRuleCodeEasySettings(easyFormula.fixFormulaAtr);
                                        if (easyFormula.fixFormulaAtr === 1) {
                                            self.viewModel017c().monthlyEasyFormula().easyFormulaCode(easyFormula.formulaEasyDetail.easyFormulaCode);
                                            self.viewModel017c().monthlyEasyFormula().easyFormulaName(easyFormula.formulaEasyDetail.easyFormulaName);
                                        }
                                    }
                                    if (easyFormula.easyFormulaCode === '002') {
                                        self.viewModel017c().dailyMonthlyEasyFormula().easyFormulaFixMoney(easyFormula.value);
                                        self.viewModel017c().dailyMonthlyEasyFormula().selectedRuleCodeEasySettings(easyFormula.fixFormulaAtr);
                                        if (easyFormula.fixFormulaAtr === 1) {
                                            self.viewModel017c().dailyMonthlyEasyFormula().easyFormulaCode(easyFormula.formulaEasyDetail.easyFormulaCode);
                                            self.viewModel017c().dailyMonthlyEasyFormula().easyFormulaName(easyFormula.formulaEasyDetail.easyFormulaName);
                                        }
                                    }
                                    if (easyFormula.easyFormulaCode === '003') {
                                        self.viewModel017c().dailyEasyFormula().easyFormulaFixMoney(easyFormula.value);
                                        self.viewModel017c().dailyEasyFormula().selectedRuleCodeEasySettings(easyFormula.fixFormulaAtr);
                                        if (easyFormula.fixFormulaAtr === 1) {
                                            self.viewModel017c().dailyEasyFormula().easyFormulaCode(easyFormula.formulaEasyDetail.easyFormulaCode);
                                            self.viewModel017c().dailyEasyFormula().easyFormulaName(easyFormula.formulaEasyDetail.easyFormulaName);
                                        }
                                    }
                                    if (easyFormula.easyFormulaCode === '004') {
                                        self.viewModel017c().hourlyEasyFormula().easyFormulaFixMoney(easyFormula.value);
                                        self.viewModel017c().hourlyEasyFormula().selectedRuleCodeEasySettings(easyFormula.fixFormulaAtr);
                                        if (easyFormula.fixFormulaAtr === 1) {
                                            self.viewModel017c().hourlyEasyFormula().easyFormulaCode(easyFormula.formulaEasyDetail.easyFormulaCode);
                                            self.viewModel017c().hourlyEasyFormula().easyFormulaName(easyFormula.formulaEasyDetail.easyFormulaName);
                                        }
                                    }
                                });
                            }
                        })
                            .fail(function (res) {
                            alert(res);
                        });
                        self.viewModel017b().selectedConditionAtr(currentFormula.conditionAtr);
                        self.viewModel017b().comboBoxUseMaster().selectedCode(currentFormula.refMasterNo.toString());
                    })
                        .fail(function (res) {
                        alert(res);
                    });
                }
            };
            ScreenModel.prototype.placeItemNameToTextArea = function (mode, self) {
                if (mode === 0) {
                    var currentTextArea = self.viewModel017c().formulaManualContent().textArea();
                    var itemType = _.find(self.viewModel017d().listBoxItemType().itemList(), function (itemType) {
                        return itemType.code === self.viewModel017d().listBoxItemType().selectedCode();
                    });
                    var itemTypeDisplayName = itemType.name.slice(5, 8);
                    var itemDetailDisplayName = '';
                    if (self.viewModel017d().listBoxItems().selectedCode() !== '') {
                        var itemDetail = _.find(self.viewModel017d().listBoxItems().itemList(), function (item) {
                            return item.code === self.viewModel017d().listBoxItems().selectedCode();
                        });
                        itemDetailDisplayName = itemDetail.name;
                    }
                    self.viewModel017c().formulaManualContent().textArea(self.viewModel017c().formulaManualContent().insertString(currentTextArea, itemTypeDisplayName + itemDetailDisplayName, $("#input-text")[0].selectionStart));
                }
                else if (mode === 1) {
                    var currentTextArea = self.viewModel017c().formulaManualContent().textArea();
                    var itemDetailDisplayName = '';
                    if (self.viewModel017e().listBoxItems().selectedCode() !== '') {
                        if (self.viewModel017e().listBoxItems().selectedCode() === '1') {
                            itemDetailDisplayName = '関数＠条件式（,,）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '2') {
                            itemDetailDisplayName = '関数＠かつ（,）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '3') {
                            itemDetailDisplayName = '関数＠または（,）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '4') {
                            itemDetailDisplayName = '関数＠四捨五入（ ）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '5') {
                            itemDetailDisplayName = '関数＠切捨て（ ）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '6') {
                            itemDetailDisplayName = '関数＠切上げ（ ）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '7') {
                            itemDetailDisplayName = '関数＠最大値（,）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '8') {
                            itemDetailDisplayName = '関数＠最小値（,）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '9') {
                            itemDetailDisplayName = '関数＠家族人数（,）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '10') {
                            itemDetailDisplayName = '関数＠月加算（,）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '11') {
                            itemDetailDisplayName = '関数＠年抽出（  ）';
                        }
                        else if (self.viewModel017e().listBoxItems().selectedCode() === '12') {
                            itemDetailDisplayName = '関数＠月抽出（  ）';
                        }
                    }
                    self.viewModel017c().formulaManualContent().textArea(self.viewModel017c().formulaManualContent().insertString(currentTextArea, itemDetailDisplayName, $("#input-text")[0].selectionStart));
                }
                else if (mode === 6) {
                    var currentTextArea = self.viewModel017c().formulaManualContent().textArea();
                    var itemType = _.find(self.viewModel017r().listBoxItemType().itemList(), function (itemType) {
                        return itemType.code === self.viewModel017r().listBoxItemType().selectedCode();
                    });
                    var itemTypeDisplayName = itemType.name.slice(7, 12);
                    var itemDetailDisplayName = '';
                    if (self.viewModel017r().listBoxItems().selectedCode() !== '') {
                        var itemDetail = _.find(self.viewModel017r().listBoxItems().itemList(), function (item) {
                            return item.code === self.viewModel017r().listBoxItems().selectedCode();
                        });
                        itemDetailDisplayName = itemDetail.name;
                    }
                    self.viewModel017c().formulaManualContent().textArea(self.viewModel017c().formulaManualContent().insertString(currentTextArea, itemTypeDisplayName + itemDetailDisplayName, $("#input-text")[0].selectionStart));
                }
                else if (mode === 4) {
                    var currentTextArea = self.viewModel017c().formulaManualContent().textArea();
                    var itemDetailDisplayName = '';
                    if (self.viewModel017h().listBoxItems().selectedCode() !== '') {
                        var itemDetail = _.find(self.viewModel017h().listBoxItems().itemList(), function (item) {
                            return item.code === self.viewModel017h().listBoxItems().selectedCode();
                        });
                        itemDetailDisplayName = '計算式＠' + itemDetail.name;
                    }
                }
                else if (mode === 5) {
                    var currentTextArea = self.viewModel017c().formulaManualContent().textArea();
                    var itemDetailDisplayName = '';
                    if (self.viewModel017i().listBoxItems().selectedCode() !== '') {
                        var itemDetail = _.find(self.viewModel017i().listBoxItems().itemList(), function (item) {
                            return item.code === self.viewModel017i().listBoxItems().selectedCode();
                        });
                        itemDetailDisplayName = '賃金TBL＠' + itemDetail.name;
                    }
                }
            };
            ScreenModel.prototype.replaceNamesToCodes = function (content) {
                var self = this;
                var replacedContent = content;
                _.forEach(self.itemsBagRepository, function (item) {
                    if (replacedContent.indexOf(item.name) !== -1) {
                        replacedContent = replacedContent.replace(item.name, item.code);
                    }
                });
                return replacedContent;
            };
            ScreenModel.prototype.replaceCodesToNames = function (content) {
                var self = this;
                var replacedContent = content;
                _.forEach(self.itemsBagRepository, function (item) {
                    if (replacedContent.indexOf(item.code) !== -1) {
                        replacedContent = replacedContent.replace(item.code, item.name);
                    }
                });
                return replacedContent;
            };
            ScreenModel.prototype.start = function () {
                var self = this;
                var dfdStart = $.Deferred();
                var treeHistoryPromise = self.bindHistoryTree();
                $.when(treeHistoryPromise)
                    .done(function () {
                    self.treeGridHistory().singleSelectedCode(self.treeGridHistory().items()[0].childs[0].code);
                    dfdStart.resolve();
                })
                    .fail(function () {
                    dfdStart.reject();
                });
                return dfdStart.promise();
            };
            ScreenModel.prototype.fillItemsBagRepository = function () {
                var self = this;
                var dfdItemsBags = $.Deferred();
                self.itemsBagRepository.push({ code: '3＠1', name: '関数＠条件式' }, { code: '3＠2', name: '関数＠かつ' }, { code: '3＠3', name: '関数＠または' }, { code: '3＠4', name: '関数＠四捨五入' }, { code: '3＠5', name: '関数＠切捨て' }, { code: '3＠6', name: '関数＠切上げ' }, { code: '3＠7', name: '関数＠最大値' }, { code: '3＠8', name: '関数＠最小値' }, { code: '3＠9', name: '関数＠家族人数' }, { code: '3＠10', name: '関数＠月加算' }, { code: '3＠11', name: '関数＠年抽出' }, { code: '3＠12', name: '関数＠月抽出' });
                qmm017.service.getListItemMaster(0).done(function (lstItem) {
                    _.forEach(lstItem, function (item) {
                        self.itemsBagRepository.push({ code: '0＠' + item.itemCode, name: '支給＠' + item.itemName });
                    });
                }).then(function () {
                    qmm017.service.getListItemMaster(1).done(function (lstItem) {
                        _.forEach(lstItem, function (item) {
                            self.itemsBagRepository.push({ code: '1＠' + item.itemCode, name: '控除＠' + item.itemName });
                        });
                    });
                }).then(function () {
                    qmm017.service.getListItemMaster(2).done(function (lstItem) {
                        _.forEach(lstItem, function (item) {
                            self.itemsBagRepository.push({ code: '2＠' + item.itemCode, name: '勤怠＠' + item.itemName });
                        });
                    });
                }).then(function () {
                    qmm017.service.findOtherFormulas(self.viewModel017b().formulaCode(), self.toYearMonthInt(self.viewModel017b().startYearMonth())).done(function (lstFormula) {
                        _.forEach(lstFormula, function (formula) {
                            self.itemsBagRepository.push({ code: '5＠' + formula.formulaCode, name: '計算式＠' + formula.formulaName });
                        });
                    });
                }).then(function () {
                    qmm017.service.getListWageTable(self.toYearMonthInt(self.viewModel017b().startYearMonth())).done(function (lstWageTable) {
                        _.forEach(lstWageTable, function (wageTbl) {
                            self.itemsBagRepository.push({ code: '6＠' + wageTbl.code, name: '賃金TBL＠' + wageTbl.name });
                        });
                    });
                }).then(function () {
                    qmm017.service.getListCompanyUnitPrice(self.toYearMonthInt(self.viewModel017b().startYearMonth())).done(function (lstCompanyUnitPrice) {
                        _.forEach(lstCompanyUnitPrice, function (companyUnitPrice) {
                            self.itemsBagRepository.push({ code: '7＠' + companyUnitPrice.unitPriceCode, name: '会社単価＠' + companyUnitPrice.unitPriceName });
                        });
                    });
                }).then(function () {
                    qmm017.service.getListPersonalUnitPrice().done(function (lstPersonalUnitPrice) {
                        _.forEach(lstPersonalUnitPrice, function (personalUnitPrice) {
                            self.itemsBagRepository.push({ code: '8＠' + personalUnitPrice.personalUnitPriceCode, name: '個人単価＠' + personalUnitPrice.personalUnitPriceName });
                        });
                        dfdItemsBags.resolve();
                    });
                }).fail(function (res) {
                    dfdItemsBags.reject(res);
                });
                return dfdItemsBags.promise();
            };
            ScreenModel.prototype.toYearMonthInt = function (yearmonth) {
                if (yearmonth.indexOf('/') !== -1) {
                    return yearmonth.replace('/', '');
                }
                else {
                    return yearmonth;
                }
            };
            ScreenModel.prototype.resetToNewMode = function () {
                var self = this;
                self.treeGridHistory().singleSelectedCode(null);
                self.selectedTabASel001('tab-1');
                self.isNewMode(true);
                self.startYearMonth('');
                self.viewModel017b().formulaCode('');
                self.viewModel017b().formulaName('');
                self.viewModel017b().selectedDifficultyAtr(0);
                self.viewModel017b().selectedConditionAtr(0);
                self.viewModel017b().comboBoxUseMaster().selectedCode(1);
                self.currentNode(null);
                self.currentParentNode(null);
            };
            ScreenModel.prototype.bindHistoryTree = function () {
                var self = this;
                var dfdHistoryTree = $.Deferred();
                var itemsTreeGridHistory = [];
                var itemsTreeGridFormula = [];
                var nodesTreeGrid = [];
                qmm017.service.getAllFormula().done(function (lstFormulaDto) {
                    if (lstFormulaDto) {
                        var groupsFormulaByCode_1 = _.groupBy(lstFormulaDto, 'formulaCode');
                        var lstFormulaCode = Object.keys(groupsFormulaByCode_1);
                        _.forEach(lstFormulaCode, function (formulaCode) {
                            var nodeHistory = [];
                            var lstHistoryEachCode = groupsFormulaByCode_1[formulaCode];
                            for (var orderHistory = 0; orderHistory < lstHistoryEachCode.length; orderHistory++) {
                                nodeHistory.push(new Node(lstHistoryEachCode[orderHistory].historyId, nts.uk.time.formatYearMonth(lstHistoryEachCode[orderHistory].startDate)
                                    + ' ~ '
                                    + nts.uk.time.formatYearMonth(lstHistoryEachCode[orderHistory].endDate), []));
                            }
                            var nodeFormula = new Node(lstHistoryEachCode[0].formulaCode, lstHistoryEachCode[0].formulaName, self.sortFormulaHistory(nodeHistory));
                            nodesTreeGrid.push(nodeFormula);
                        });
                        self.treeGridHistory().items(nodesTreeGrid);
                        self.resetToNewMode();
                    }
                    else {
                        self.treeGridHistory().items(nodesTreeGrid);
                    }
                    dfdHistoryTree.resolve();
                }).fail(function (res) {
                    alert(res);
                    dfdHistoryTree.reject();
                });
                return dfdHistoryTree.promise();
            };
            ScreenModel.prototype.sortFormulaHistory = function (lstFormulaHistory) {
                return lstFormulaHistory.sort(function (formulaHistoryA, formulaHistoryB) {
                    return formulaHistoryB.name.split(' ~ ')[0].replace('/', '') - formulaHistoryA.name.split(' ~ ')[0].replace('/', '');
                });
            };
            ScreenModel.prototype.registerFormulaMaster = function () {
                var self = this;
                if (self.isNewMode()) {
                    var referenceMasterNo = null;
                    if (self.viewModel017b().selectedDifficultyAtr() === 0 && self.viewModel017b().selectedConditionAtr() === 0) {
                        referenceMasterNo = 0;
                    }
                    else if (self.viewModel017b().selectedDifficultyAtr() === 0 && self.viewModel017b().selectedConditionAtr() === '1') {
                        referenceMasterNo = self.viewModel017b().comboBoxUseMaster().selectedCode();
                    }
                    var startDate = '';
                    if (self.viewModel017b().startYearMonth().indexOf('/') !== -1) {
                        startDate = self.viewModel017b().startYearMonth().replace('/', '');
                    }
                    else {
                        startDate = self.viewModel017b().startYearMonth();
                    }
                    var command = {
                        formulaCode: self.viewModel017b().formulaCode(),
                        formulaName: self.viewModel017b().formulaName(),
                        difficultyAtr: self.viewModel017b().selectedDifficultyAtr(),
                        startDate: startDate,
                        endDate: 999912,
                        conditionAtr: self.viewModel017b().selectedConditionAtr(),
                        refMasterNo: referenceMasterNo
                    };
                    qmm017.service.registerFormulaMaster(command)
                        .done(function () {
                        self.resetToNewMode();
                    })
                        .fail(function (res) {
                        alert(res);
                    });
                }
                else {
                    var command = {
                        formulaCode: self.viewModel017b().formulaCode(),
                        formulaName: self.viewModel017b().formulaName(),
                        difficultyAtr: self.viewModel017b().selectedDifficultyAtr(),
                        historyId: self.currentNode().code,
                        easyFormulaDto: [],
                        formulaContent: '',
                        referenceMonthAtr: '',
                        roundAtr: '',
                        roundDigit: ''
                    };
                    if (command.difficultyAtr === 1) {
                        command.formulaContent = self.replaceNamesToCodes(self.viewModel017c().formulaManualContent().textArea());
                        command.referenceMonthAtr = self.viewModel017c().comboBoxReferenceMonthAtr().selectedCode();
                        command.roundAtr = self.viewModel017c().comboBoxRoudingMethod().selectedCode();
                        command.roundDigit = self.viewModel017c().comboBoxRoudingPosition().selectedCode();
                    }
                    else {
                        if (self.viewModel017b().selectedConditionAtr() == 0) {
                            var noneConditionalEasyFormulaDetail = self.viewModel017c().noneConditionalEasyFormula().easyFormulaDetail();
                            noneConditionalEasyFormulaDetail.easyFormulaCode = '000';
                            noneConditionalEasyFormulaDetail.maxLimitValue = 0;
                            noneConditionalEasyFormulaDetail.minLimitValue = 0;
                            command.easyFormulaDto.push({
                                easyFormulaCode: '000',
                                value: self.viewModel017c().noneConditionalEasyFormula().easyFormulaFixMoney(),
                                referenceMasterNo: "0000000000",
                                formulaDetail: noneConditionalEasyFormulaDetail,
                                fixFormulaAtr: self.viewModel017c().noneConditionalEasyFormula().selectedRuleCodeEasySettings()
                            });
                        }
                        else if (self.viewModel017b().selectedConditionAtr() == 1 && self.viewModel017b().comboBoxUseMaster().selectedCode() < 6) {
                            var defaultEasyFormulaDetail = self.viewModel017c().defaultEasyFormula().easyFormulaDetail();
                            defaultEasyFormulaDetail.easyFormulaCode = '000';
                            defaultEasyFormulaDetail.maxLimitValue = 0;
                            defaultEasyFormulaDetail.minLimitValue = 0;
                            command.easyFormulaDto.push({
                                easyFormulaCode: '000',
                                value: self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(),
                                referenceMasterNo: "000000000" + self.viewModel017c().useMasterCode(),
                                formulaDetail: defaultEasyFormulaDetail,
                                fixFormulaAtr: self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings()
                            });
                            if (command.easyFormulaDto[0].fixFormulaAtr !== 0) {
                                command.easyFormulaDto[0].value = 0;
                            }
                        }
                        else if (self.viewModel017b().selectedConditionAtr() == 1 && self.viewModel017b().comboBoxUseMaster().selectedCode() === '6') {
                            var defaultEasyFormulaDetail = self.viewModel017c().defaultEasyFormula().easyFormulaDetail();
                            defaultEasyFormulaDetail.easyFormulaCode = '000';
                            defaultEasyFormulaDetail.maxLimitValue = 0;
                            defaultEasyFormulaDetail.minLimitValue = 0;
                            command.easyFormulaDto.push({
                                easyFormulaCode: '000',
                                value: self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(),
                                referenceMasterNo: "0000000060",
                                formulaDetail: defaultEasyFormulaDetail,
                                fixFormulaAtr: self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings()
                            });
                            if (command.easyFormulaDto[0].fixFormulaAtr !== '0') {
                                command.easyFormulaDto[0].value = 0;
                            }
                            if (self.viewModel017c().monthlyEasyFormula().selectedRuleCodeEasySettings() !== '2') {
                                var monthlyEasyFormulaDetail = self.viewModel017c().monthlyEasyFormula().easyFormulaDetail();
                                monthlyEasyFormulaDetail.easyFormulaCode = '001';
                                monthlyEasyFormulaDetail.maxLimitValue = 0;
                                monthlyEasyFormulaDetail.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '001',
                                    value: self.viewModel017c().monthlyEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000061",
                                    formulaDetail: monthlyEasyFormulaDetail,
                                    fixFormulaAtr: self.viewModel017c().monthlyEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            else {
                                var defaultEasyFormulaDetail_1 = self.viewModel017c().defaultEasyFormula().easyFormulaDetail();
                                defaultEasyFormulaDetail_1.easyFormulaCode = '001';
                                defaultEasyFormulaDetail_1.maxLimitValue = 0;
                                defaultEasyFormulaDetail_1.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '001',
                                    value: self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000061",
                                    formulaDetail: defaultEasyFormulaDetail_1,
                                    fixFormulaAtr: self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            if (self.viewModel017c().dailyMonthlyEasyFormula().selectedRuleCodeEasySettings() !== '2') {
                                var dailyMonthlyEasyFormulaDetail = self.viewModel017c().dailyMonthlyEasyFormula().easyFormulaDetail();
                                dailyMonthlyEasyFormulaDetail.easyFormulaCode = '002';
                                dailyMonthlyEasyFormulaDetail.maxLimitValue = 0;
                                dailyMonthlyEasyFormulaDetail.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '002',
                                    value: self.viewModel017c().dailyMonthlyEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000062",
                                    formulaDetail: dailyMonthlyEasyFormulaDetail,
                                    fixFormulaAtr: self.viewModel017c().dailyMonthlyEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            else {
                                var defaultEasyFormulaDetail_2 = self.viewModel017c().defaultEasyFormula().easyFormulaDetail();
                                defaultEasyFormulaDetail_2.easyFormulaCode = '002';
                                defaultEasyFormulaDetail_2.maxLimitValue = 0;
                                defaultEasyFormulaDetail_2.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '002',
                                    value: self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000062",
                                    formulaDetail: defaultEasyFormulaDetail_2,
                                    fixFormulaAtr: self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            if (self.viewModel017c().dailyEasyFormula().selectedRuleCodeEasySettings() !== '2') {
                                var dailyEasyFormulaDetail = self.viewModel017c().dailyEasyFormula().easyFormulaDetail();
                                dailyEasyFormulaDetail.easyFormulaCode = '003';
                                dailyEasyFormulaDetail.maxLimitValue = 0;
                                dailyEasyFormulaDetail.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '003',
                                    value: self.viewModel017c().dailyEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000063",
                                    formulaDetail: dailyEasyFormulaDetail,
                                    fixFormulaAtr: self.viewModel017c().dailyEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            else {
                                var defaultEasyFormulaDetail_3 = self.viewModel017c().defaultEasyFormula().easyFormulaDetail();
                                defaultEasyFormulaDetail_3.easyFormulaCode = '003';
                                defaultEasyFormulaDetail_3.maxLimitValue = 0;
                                defaultEasyFormulaDetail_3.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '003',
                                    value: self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000063",
                                    formulaDetail: defaultEasyFormulaDetail_3,
                                    fixFormulaAtr: self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            if (self.viewModel017c().hourlyEasyFormula().selectedRuleCodeEasySettings() !== '2') {
                                var hourlyEasyFormulaDetail = self.viewModel017c().hourlyEasyFormula().easyFormulaDetail();
                                hourlyEasyFormulaDetail.easyFormulaCode = '004';
                                hourlyEasyFormulaDetail.maxLimitValue = 0;
                                hourlyEasyFormulaDetail.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '004',
                                    value: self.viewModel017c().hourlyEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000064",
                                    formulaDetail: hourlyEasyFormulaDetail,
                                    fixFormulaAtr: self.viewModel017c().hourlyEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            else {
                                var defaultEasyFormulaDetail_4 = self.viewModel017c().defaultEasyFormula().easyFormulaDetail();
                                defaultEasyFormulaDetail_4.easyFormulaCode = '004';
                                defaultEasyFormulaDetail_4.maxLimitValue = 0;
                                defaultEasyFormulaDetail_4.minLimitValue = 0;
                                command.easyFormulaDto.push({
                                    easyFormulaCode: '004',
                                    value: self.viewModel017c().defaultEasyFormula().easyFormulaFixMoney(),
                                    referenceMasterNo: "0000000064",
                                    formulaDetail: defaultEasyFormulaDetail_4,
                                    fixFormulaAtr: self.viewModel017c().defaultEasyFormula().selectedRuleCodeEasySettings()
                                });
                            }
                            if (command.easyFormulaDto[1].fixFormulaAtr !== 0) {
                                command.easyFormulaDto[1].value = 0;
                            }
                            if (command.easyFormulaDto[2].fixFormulaAtr !== 0) {
                                command.easyFormulaDto[2].value = 0;
                            }
                            if (command.easyFormulaDto[3].fixFormulaAtr !== 0) {
                                command.easyFormulaDto[3].value = 0;
                            }
                            if (command.easyFormulaDto[4].fixFormulaAtr !== 0) {
                                command.easyFormulaDto[4].value = 0;
                            }
                        }
                    }
                    qmm017.service.updateFormulaMaster(command)
                        .done(function () {
                        self.resetToNewMode();
                    })
                        .fail(function (res) {
                        alert(res);
                    });
                }
            };
            ScreenModel.prototype.openDialogJ = function () {
                var self = this;
                var lastestHistoryNode = self.currentParentNode().childs[0];
                var lastestHistory = lastestHistoryNode.name;
                var lastestYearMonth = lastestHistory.split("~");
                var lastestStartYm = lastestYearMonth[0].trim();
                var param = {
                    formulaCode: self.viewModel017b().formulaCode(),
                    formulaName: self.viewModel017b().formulaName(),
                    startYm: lastestStartYm,
                    difficultyAtr: self.viewModel017b().selectedDifficultyAtr(),
                    conditionAtr: self.viewModel017b().selectedConditionAtr(),
                    referenceMasterNo: self.viewModel017b().comboBoxUseMaster().selectedCode()
                };
                nts.uk.ui.windows.setShared('paramFromScreenA', param);
                nts.uk.ui.windows.sub.modal('/view/qmm/017/j/index.xhtml', { title: '履歴の追加', width: 540, height: 545 }).onClosed(function () {
                    self.start();
                });
            };
            ScreenModel.prototype.openDialogK = function () {
                var self = this;
                var currentHistory = self.currentNode().name;
                var currentYearMonth = currentHistory.split(" ~ ");
                var currentStartYm = currentYearMonth[0];
                var currentEndYm = currentYearMonth[1];
                var param = {
                    formulaCode: self.viewModel017b().formulaCode(),
                    formulaName: self.viewModel017b().formulaName(),
                    historyId: self.currentNode().code,
                    startYm: currentStartYm,
                    endYm: currentEndYm,
                    difficultyAtr: self.viewModel017b().selectedDifficultyAtr()
                };
                nts.uk.ui.windows.setShared('paramFromScreenA', param);
                nts.uk.ui.windows.sub.modal('/view/qmm/017/k/index.xhtml', { title: '履歴の編集', width: 540, height: 380 }).onClosed(function () {
                    self.start();
                });
            };
            ScreenModel.prototype.openDialogQ = function (root) {
                var self = root;
                var param = {
                    formulaContent: self.viewModel017c().formulaManualContent().textArea(),
                    itemsBag: self.itemsBagRepository
                };
                nts.uk.ui.windows.setShared('formulaManual', param);
                nts.uk.ui.windows.sub.modal('/view/qmm/017/q/index.xhtml', { title: 'お試し計算', width: 840, height: 615 }).onClosed(function () {
                });
            };
            return ScreenModel;
        }());
        qmm017.ScreenModel = ScreenModel;
        function findNode(targetNode, searchString) {
            if (targetNode.code === searchString) {
                if (targetNode.childs.length > 0) {
                    return targetNode.childs[0];
                }
                else if (targetNode.childs.length === 0) {
                    return targetNode;
                }
            }
            for (var count = 0; count < targetNode.childs.length; count++) {
                var foundNode = findNode(targetNode.childs[count], searchString);
                if (foundNode) {
                    return foundNode;
                }
            }
        }
        var TreeGrid = (function () {
            function TreeGrid() {
                var self = this;
                self.items = ko.observableArray([new Node('001', 'Formula 1', [
                        new Node('0001-2', '2017/03 ~ 9999/12', []),
                        new Node('0001-1', '2016/06 ~ 2017/03', [])
                    ]), new Node('002', 'Formula 2', [
                        new Node('0002-2', '2017/03 ~ 9999/12', []),
                        new Node('0002-1', '2016/06 ~ 2017/03', [])
                    ])
                ]);
                self.selectedCode = ko.observableArray([]);
                self.singleSelectedCode = ko.observable(null);
                self.index = 0;
            }
            return TreeGrid;
        }());
        qmm017.TreeGrid = TreeGrid;
        var Node = (function () {
            function Node(code, name, childs) {
                var self = this;
                self.code = code;
                self.name = name;
                if (childs.length > 0) {
                    self.nodeText = self.code + ' ' + self.name;
                }
                else if (childs.length === 0) {
                    self.nodeText = self.name;
                }
                self.childs = childs;
            }
            return Node;
        }());
        qmm017.Node = Node;
    })(qmm017 = nts.qmm017 || (nts.qmm017 = {}));
})(nts || (nts = {}));
