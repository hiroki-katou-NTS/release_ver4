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
                    var i;
                    (function (i) {
                        var viewmodel;
                        (function (viewmodel) {
                            var commonService = nts.uk.pr.view.qmm008._0.common.service;
                            var ScreenModel = (function () {
                                function ScreenModel(officeName, pensionModel) {
                                    var self = this;
                                    self.listAvgEarnLevelMasterSetting = [];
                                    self.listPensionAvgearnModel = ko.observableArray([]);
                                    self.pensionRateModel = new PensionRateModel(pensionModel.historyId, pensionModel.officeCode(), officeName, pensionModel.startMonth(), pensionModel.endMonth(), pensionModel.autoCalculate(), pensionModel.fundRateItems(), pensionModel.roundingMethods(), pensionModel.childContributionRate());
                                    self.leftShow = ko.observable(true);
                                    self.rightShow = ko.observable(true);
                                    self.leftBtnText = ko.computed(function () { if (self.leftShow())
                                        return "—"; return "+"; });
                                    self.rightBtnText = ko.computed(function () { if (self.rightShow())
                                        return "—"; return "+"; });
                                    // Common NtsNumberEditor Option
                                    self.numberEditorCommonOption = ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                                        grouplength: 3
                                    }));
                                }
                                /**
                                 * Start page.
                                 */
                                ScreenModel.prototype.startPage = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    self.loadAvgEarnLevelMasterSetting().done(function () {
                                        return self.loadPensionAvgearn().done(function () {
                                            return dfd.resolve();
                                        });
                                    });
                                    return dfd.promise();
                                };
                                /**
                                 * Load AvgEarnLevelMasterSetting list.
                                 */
                                ScreenModel.prototype.loadAvgEarnLevelMasterSetting = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    commonService.getAvgEarnLevelMasterSettingList().done(function (res) {
                                        self.listAvgEarnLevelMasterSetting = res;
                                        dfd.resolve();
                                    });
                                    return dfd.promise();
                                };
                                /**
                                 * Load PensionAvgEarn.
                                 */
                                ScreenModel.prototype.loadPensionAvgearn = function () {
                                    var self = this;
                                    var dfd = $.Deferred();
                                    i.service.findPensionAvgearn(self.pensionRateModel.historyId).done(function (res) {
                                        res.forEach(function (item) {
                                            self.listPensionAvgearnModel.push(new PensionAvgearnModel(item.historyId, item.levelCode, 
                                            /**new PensionAvgearnValueModel(
                                                item.companyFund.maleAmount,
                                                item.companyFund.femaleAmount,
                                                item.companyFund.unknownAmount),*/
                                            new PensionAvgearnValueModel(item.companyFundExemption.maleAmount, item.companyFundExemption.femaleAmount, item.companyFundExemption.unknownAmount), new PensionAvgearnValueModel(item.companyPension.maleAmount, item.companyPension.femaleAmount, item.companyPension.unknownAmount), 
                                            /**new PensionAvgearnValueModel(
                                                item.personalFund.maleAmount,
                                                item.personalFund.femaleAmount,
                                                item.personalFund.unknownAmount),*/
                                            new PensionAvgearnValueModel(item.personalFundExemption.maleAmount, item.personalFundExemption.femaleAmount, item.personalFundExemption.unknownAmount), new PensionAvgearnValueModel(item.personalPension.maleAmount, item.personalPension.femaleAmount, item.personalPension.unknownAmount), item.childContributionAmount));
                                        });
                                        dfd.resolve();
                                    });
                                    return dfd.promise();
                                };
                                /**
                                 * Collect data from input.
                                 */
                                ScreenModel.prototype.collectData = function () {
                                    var self = this;
                                    var data = [];
                                    self.listPensionAvgearnModel().forEach(function (item) {
                                        data.push(ko.toJS(item));
                                    });
                                    return data;
                                };
                                /**
                                 * Call service to save pensionAvgearn.
                                 */
                                ScreenModel.prototype.save = function () {
                                    var self = this;
                                    i.service.updatePensionAvgearn(self.collectData(), self.pensionRateModel.officeCode).done(function () {
                                        return self.closeDialog();
                                    });
                                };
                                /**
                                 * Button toggle Pension welfare pension.
                                 */
                                ScreenModel.prototype.leftToggle = function () {
                                    this.leftShow(!this.leftShow());
                                };
                                /**
                                 * Button toggle right.
                                 */
                                ScreenModel.prototype.rightToggle = function () {
                                    this.rightShow(!this.rightShow());
                                };
                                /**
                                 * ReCalculate the listPensionAvgearnModel
                                 */
                                ScreenModel.prototype.reCalculate = function () {
                                    var self = this;
                                    // Clear current listPensionAvgearnModel
                                    self.listPensionAvgearnModel.removeAll();
                                    // Recalculate listPensionAvgearnModel
                                    self.listAvgEarnLevelMasterSetting.forEach(function (item) {
                                        self.listPensionAvgearnModel.push(self.calculatePensionAvgearn(item));
                                    });
                                };
                                /**
                                 * Calculate the PensionAvgearn
                                 */
                                ScreenModel.prototype.calculatePensionAvgearn = function (levelMasterSetting) {
                                    var self = this;
                                    var model = self.pensionRateModel;
                                    var rateItems = self.pensionRateModel.fundRateItems;
                                    var roundingMethods = self.pensionRateModel.roundingMethods;
                                    var personalRounding = self.convertToRounding(roundingMethods.pensionSalaryPersonalComboBoxSelectedCode());
                                    var companyRounding = self.convertToRounding(roundingMethods.pensionSalaryCompanyComboBoxSelectedCode());
                                    var rate = levelMasterSetting.avgEarn / 1000;
                                    var autoCalculate = self.pensionRateModel.autoCalculate;
                                    if (autoCalculate == AutoCalculate.Auto) {
                                        return new PensionAvgearnModel(model.historyId, levelMasterSetting.code, new PensionAvgearnValueModel(self.rounding(companyRounding, rateItems.salaryCompanySonExemption() * rate), self.rounding(companyRounding, rateItems.salaryCompanyDaughterExemption() * rate), self.rounding(companyRounding, rateItems.salaryCompanyUnknownExemption() * rate)), new PensionAvgearnValueModel(self.rounding(companyRounding, rateItems.salaryCompanySonBurden() * rate), self.rounding(companyRounding, rateItems.salaryCompanyDaughterBurden() * rate), self.rounding(companyRounding, rateItems.salaryCompanyUnknownBurden() * rate)), new PensionAvgearnValueModel(self.rounding(personalRounding, rateItems.salaryPersonalSonExemption() * rate), self.rounding(personalRounding, rateItems.salaryPersonalDaughterExemption() * rate), self.rounding(personalRounding, rateItems.salaryPersonalUnknownExemption() * rate)), new PensionAvgearnValueModel(self.rounding(personalRounding, rateItems.salaryPersonalSonBurden() * rate), self.rounding(personalRounding, rateItems.salaryPersonalDaughterBurden() * rate), self.rounding(personalRounding, rateItems.salaryPersonalUnknownBurden() * rate)), model.childContributionRate() * rate);
                                    }
                                    else {
                                        return new PensionAvgearnModel(model.historyId, levelMasterSetting.code, new PensionAvgearnValueModel(Number.Zero, Number.Zero, Number.Zero), new PensionAvgearnValueModel(Number.Zero, Number.Zero, Number.Zero), new PensionAvgearnValueModel(Number.Zero, Number.Zero, Number.Zero), new PensionAvgearnValueModel(Number.Zero, Number.Zero, Number.Zero), model.childContributionRate() * rate);
                                    }
                                };
                                // rounding 
                                ScreenModel.prototype.rounding = function (roudingMethod, roundValue) {
                                    var self = this;
                                    var backupValue = roundValue;
                                    switch (roudingMethod) {
                                        case Rounding.ROUNDUP: return Math.ceil(backupValue);
                                        case Rounding.TRUNCATION: return Math.floor(backupValue);
                                        case Rounding.ROUNDDOWN:
                                            if ((backupValue * 10) % 10 > 5)
                                                return Math.ceil(backupValue);
                                            else
                                                return Math.floor(backupValue);
                                        case Rounding.DOWN4_UP5: return self.roudingDownUp(backupValue, 4);
                                        case Rounding.DOWN5_UP6: return self.roudingDownUp(backupValue, 5);
                                    }
                                };
                                ScreenModel.prototype.roudingDownUp = function (value, down) {
                                    var newVal = Math.round(value * 10) / 10;
                                    if ((newVal * 10) % 10 > down)
                                        return Math.ceil(value);
                                    else
                                        return Math.floor(value);
                                };
                                //value to string rounding
                                ScreenModel.prototype.convertToRounding = function (stringValue) {
                                    switch (stringValue) {
                                        case "0": return Rounding.ROUNDUP;
                                        case "1": return Rounding.TRUNCATION;
                                        case "2": return Rounding.DOWN4_UP5;
                                        case "3": return Rounding.ROUNDDOWN;
                                        case "4": return Rounding.DOWN5_UP6;
                                        default: return Rounding.ROUNDUP;
                                    }
                                };
                                /**
                                 * Close dialog.
                                 */
                                ScreenModel.prototype.closeDialog = function () {
                                    nts.uk.ui.windows.close();
                                };
                                return ScreenModel;
                            }());
                            viewmodel.ScreenModel = ScreenModel;
                            /**
                             * PensionRate Model
                             */
                            var PensionRateModel = (function () {
                                function PensionRateModel(historyId, officeCode, officeName, startMonth, endMonth, autoCalculate, rateItems, roundingMethods, childContributionRate) {
                                    this.historyId = historyId;
                                    this.officeCode = officeCode;
                                    this.officeName = officeName;
                                    this.startMonth = startMonth;
                                    this.endMonth = endMonth;
                                    this.autoCalculate = autoCalculate;
                                    this.fundRateItems = rateItems;
                                    this.roundingMethods = roundingMethods;
                                    this.childContributionRate = ko.observable(childContributionRate);
                                }
                                return PensionRateModel;
                            }());
                            viewmodel.PensionRateModel = PensionRateModel;
                            /**
                             * PensionAvgearn Model
                             */
                            var PensionAvgearnModel = (function () {
                                function PensionAvgearnModel(historyId, levelCode, 
                                    //companyFund: PensionAvgearnValueModel,
                                    companyFundExemption, companyPension, 
                                    //personalFund: PensionAvgearnValueModel,
                                    personalFundExemption, personalPension, childContributionAmount) {
                                    this.historyId = historyId;
                                    this.levelCode = levelCode;
                                    //this.companyFund = companyFund;
                                    this.companyFundExemption = companyFundExemption;
                                    this.companyPension = companyPension;
                                    //this.personalFund = personalFund;
                                    this.personalFundExemption = personalFundExemption;
                                    this.personalPension = personalPension;
                                    this.childContributionAmount = ko.observable(childContributionAmount);
                                }
                                return PensionAvgearnModel;
                            }());
                            viewmodel.PensionAvgearnModel = PensionAvgearnModel;
                            /**
                             * PensionAvgearnValue Model
                             */
                            var PensionAvgearnValueModel = (function () {
                                function PensionAvgearnValueModel(maleAmount, femaleAmount, unknownAmount) {
                                    this.maleAmount = ko.observable(maleAmount);
                                    this.femaleAmount = ko.observable(femaleAmount);
                                    this.unknownAmount = ko.observable(unknownAmount);
                                }
                                return PensionAvgearnValueModel;
                            }());
                            viewmodel.PensionAvgearnValueModel = PensionAvgearnValueModel;
                            var Rounding = (function () {
                                function Rounding() {
                                }
                                Rounding.ROUNDUP = 'RoundUp';
                                Rounding.TRUNCATION = 'Truncation';
                                Rounding.ROUNDDOWN = 'RoundDown';
                                Rounding.DOWN5_UP6 = 'Down5_Up6';
                                Rounding.DOWN4_UP5 = 'Down4_Up5';
                                return Rounding;
                            }());
                            viewmodel.Rounding = Rounding;
                            (function (Number) {
                                Number[Number["Zero"] = 0] = "Zero";
                                Number[Number["One"] = 1] = "One";
                                Number[Number["Three"] = 3] = "Three";
                            })(viewmodel.Number || (viewmodel.Number = {}));
                            var Number = viewmodel.Number;
                            (function (AutoCalculate) {
                                AutoCalculate[AutoCalculate["Auto"] = 0] = "Auto";
                                AutoCalculate[AutoCalculate["Manual"] = 1] = "Manual";
                            })(viewmodel.AutoCalculate || (viewmodel.AutoCalculate = {}));
                            var AutoCalculate = viewmodel.AutoCalculate;
                        })(viewmodel = i.viewmodel || (i.viewmodel = {}));
                    })(i = qmm008.i || (qmm008.i = {}));
                })(qmm008 = view.qmm008 || (view.qmm008 = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
