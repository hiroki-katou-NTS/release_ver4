var qet002;
(function (qet002) {
    var a;
    (function (a) {
        var viewmodel;
        (function (viewmodel) {
            var ScreenModel = (function () {
                function ScreenModel() {
                    var self = this;
                    self.targetYear = ko.observable(2016);
                    self.isLowerLimit = ko.observable(true);
                    self.isUpperLimit = ko.observable(true);
                    self.lowerLimitValue = ko.observable(null);
                    self.japanYear = ko.observable('(' + nts.uk.time.yearInJapanEmpire('2016') + ')');
                    self.upperLimitValue = ko.observable(null);
                    self.targetYear.subscribe(function (val) {
                        self.japanYear("" + nts.uk.time.yearInJapanEmpire(val));
                    });
                }
                ScreenModel.prototype.start = function () {
                    var dfd = $.Deferred();
                    dfd.resolve();
                    return dfd.promise();
                };
                ScreenModel.prototype.printData = function () {
                    var hasError = false;
                    if (this.targetYear() == null) {
                        hasError = true;
                        $('#target-year-input').ntsError('set', '未入力エラー');
                    }
                    if (this.isLowerLimit() == true) {
                        if (this.lowerLimitValue() == null) {
                            hasError = true;
                            $('#lower-limit-input').ntsError('set', '未入力エラー');
                        }
                    }
                    if (this.isUpperLimit() == true) {
                        if (this.upperLimitValue() == null) {
                            hasError = true;
                            $('#upper-limit-input').ntsError('set', '未入力エラー');
                        }
                    }
                    if ((this.isLowerLimit() == true) && (this.isUpperLimit() == true)) {
                        if (this.lowerLimitValue() > this.upperLimitValue()) {
                            hasError = true;
                            $('#lower-limit-input').ntsError('set', '未入力エラー');
                        }
                    }
                    if (this.isLowerLimit() == false) {
                        $('#lower-limit-input').ntsError('clear');
                    }
                    if (this.isUpperLimit() == false) {
                        $('#upper-limit-input').ntsError('clear');
                    }
                    if (hasError) {
                        return;
                    }
                    a.service.printService(this).done(function (data) {
                    }).fail(function (res) {
                        nts.uk.ui.dialog.alert(res.message);
                    });
                };
                return ScreenModel;
            }());
            viewmodel.ScreenModel = ScreenModel;
            var AccumulatedPaymentResultViewModel = (function () {
                function AccumulatedPaymentResultViewModel(empDesignation, empCode, empName, taxAmount, socialInsuranceAmount, widthHoldingTaxAmount, enrollmentStatus, directionalStatus, amountAfterTaxDeduction) {
                    var self = this;
                    self.empDesignation = empDesignation;
                    self.empCode = empCode;
                    self.empName = empName;
                    self.taxAmount = taxAmount;
                    self.socialInsuranceAmount = socialInsuranceAmount;
                    self.widthHoldingTaxAmount = widthHoldingTaxAmount;
                    self.enrollmentStatus = enrollmentStatus;
                    self.directionalStatus = directionalStatus;
                    self.amountAfterTaxDeduction = amountAfterTaxDeduction;
                }
                return AccumulatedPaymentResultViewModel;
            }());
            viewmodel.AccumulatedPaymentResultViewModel = AccumulatedPaymentResultViewModel;
        })(viewmodel = a.viewmodel || (a.viewmodel = {}));
    })(a = qet002.a || (qet002.a = {}));
})(qet002 || (qet002 = {}));
//# sourceMappingURL=qet002.a.vm.js.map