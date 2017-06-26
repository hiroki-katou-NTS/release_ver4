module nts.uk.pr.view.qpp007.b {
    export module viewmodel {
        import SalaryPrintSettingDto = service.model.SalaryPrintSettingDto;

        export class ScreenModel {
            salaryPrintSettingModel: KnockoutObservable<SalaryPrintSettingModel>;

            constructor() {
                var self = this;
                self.salaryPrintSettingModel = ko.observable<SalaryPrintSettingModel>();
            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<any>();
                self.loadSalaryPrintSetting().done(() =>
                    dfd.resolve());
                return dfd.promise();
            }

            /**
             * Call service to load salary print setting.
             */
            private loadSalaryPrintSetting(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                service.findSalaryPrintSetting().done(function(res: SalaryPrintSettingDto) {
                    self.salaryPrintSettingModel(new SalaryPrintSettingModel(res));
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
             * Validate settings.
             */
            private isValidSetting(): boolean {
                var self = this;
                var isValid = true;
                var model = self.salaryPrintSettingModel();
                $('#this-contents-area').ntsError('clear');
                if (model.showPayment() == false
                    && model.sumPersonSet() == false
                    && model.sumMonthPersonSet() == false
                    && model.sumEachDeprtSet() == false
                    && model.sumMonthDeprtSet() == false
                    && model.sumDepHrchyIndexSet() == false
                    && model.sumMonthDepHrchySet() == false
                    && model.totalSet() == false
                    && model.monthTotalSet() == false) {
                    $('#this-contents-area').ntsError('set', '設定が正しくありません。');
                    isValid = false;
                }
                if (model.sumDepHrchyIndexSet() == true
                    || model.sumMonthDepHrchySet() == true) {
                    if (model.numberOfSelectedIndexs() > 5) {
                        $('#this-contents-area').ntsError('set', '部門階層が正しく指定されていません');
                        isValid = false;
                    }
                    if (model.numberOfSelectedIndexs() == 0) {
                        $('#this-contents-area').ntsError('set', '部門階層が正しく指定されていません');
                        isValid = false;
                    }
                }
                return isValid;
            }

            /**
             * Call service to save print setting.
             */
            public saveSetting(): void {
                var self = this;
                if (self.isValidSetting()) {
                    service.saveSalaryPrintSetting(ko.toJS(self.salaryPrintSettingModel));
                    nts.uk.ui.windows.close();
                }
            }

            /**
             * Cancel and close dialog.
             */
            public cancel(): void {
                nts.uk.ui.windows.close();
            }
        }

        export class SalaryPrintSettingModel {
            showPayment: KnockoutObservable<boolean>;
            sumPersonSet: KnockoutObservable<boolean>;
            sumMonthPersonSet: KnockoutObservable<boolean>;
            sumEachDeprtSet: KnockoutObservable<boolean>;
            sumMonthDeprtSet: KnockoutObservable<boolean>;
            sumDepHrchyIndexSet: KnockoutObservable<boolean>;
            sumMonthDepHrchySet: KnockoutObservable<boolean>;
            hrchyIndex1: KnockoutObservable<boolean>;
            hrchyIndex2: KnockoutObservable<boolean>;
            hrchyIndex3: KnockoutObservable<boolean>;
            hrchyIndex4: KnockoutObservable<boolean>;
            hrchyIndex5: KnockoutObservable<boolean>;
            hrchyIndex6: KnockoutObservable<boolean>;
            hrchyIndex7: KnockoutObservable<boolean>;
            hrchyIndex8: KnockoutObservable<boolean>;
            hrchyIndex9: KnockoutObservable<boolean>;
            numberOfSelectedIndexs: KnockoutObservable<number>;
            totalSet: KnockoutObservable<boolean>;
            monthTotalSet: KnockoutObservable<boolean>;
            constructor(dto: SalaryPrintSettingDto) {
                this.showPayment = ko.observable(dto.showPayment);
                this.sumPersonSet = ko.observable(dto.sumPersonSet);
                this.sumMonthPersonSet = ko.observable(dto.sumMonthPersonSet);
                this.sumEachDeprtSet = ko.observable(dto.sumEachDeprtSet);
                this.sumMonthDeprtSet = ko.observable(dto.sumMonthDeprtSet);
                this.sumDepHrchyIndexSet = ko.observable(dto.sumDepHrchyIndexSet);
                this.sumMonthDepHrchySet = ko.observable(dto.sumMonthDepHrchySet);
                this.hrchyIndex1 = ko.observable(dto.hrchyIndex1);
                this.hrchyIndex2 = ko.observable(dto.hrchyIndex2);
                this.hrchyIndex3 = ko.observable(dto.hrchyIndex3);
                this.hrchyIndex4 = ko.observable(dto.hrchyIndex4);
                this.hrchyIndex5 = ko.observable(dto.hrchyIndex5);
                this.hrchyIndex6 = ko.observable(dto.hrchyIndex6);
                this.hrchyIndex7 = ko.observable(dto.hrchyIndex7);
                this.hrchyIndex8 = ko.observable(dto.hrchyIndex8);
                this.hrchyIndex9 = ko.observable(dto.hrchyIndex9);
                var self = this;
                this.numberOfSelectedIndexs = ko.computed(() => {
                    var count = 0;
                    if (self.hrchyIndex1()) {
                        count += 1;
                    }
                    if (self.hrchyIndex2()) {
                        count += 1;
                    }
                    if (self.hrchyIndex3()) {
                        count += 1;
                    }
                    if (self.hrchyIndex4()) {
                        count += 1;
                    }
                    if (self.hrchyIndex5()) {
                        count += 1;
                    }
                    if (self.hrchyIndex6()) {
                        count += 1;
                    }
                    if (self.hrchyIndex7()) {
                        count += 1;
                    }
                    if (self.hrchyIndex8()) {
                        count += 1;
                    }
                    if (self.hrchyIndex9()) {
                        count += 1;
                    }
                    return count;
                });
                this.totalSet = ko.observable(dto.totalSet);
                this.monthTotalSet = ko.observable(dto.monthTotalSet);
            }
        }
    }
}