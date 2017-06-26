module nts.uk.pr.view.qmm008.h {
    export module viewmodel {
        import ListHealthInsuranceAvgEarnDto = service.model.ListHealthInsuranceAvgEarnDto;
        import HealthInsuranceAvgEarnValue = service.model.HealthInsuranceAvgEarnValue;
        import HealthInsuranceRateItemModel = nts.uk.pr.view.qmm008.b.viewmodel.HealthInsuranceRateItemModel;
        import HealthInsuranceRoundingModel = nts.uk.pr.view.qmm008.b.viewmodel.HealthInsuranceRoundingModel;
        import HealthInsuranceRateModelofScreenA = nts.uk.pr.view.qmm008.b.viewmodel.HealthInsuranceRateModel;
        import InsuranceOfficeItemDto = nts.uk.pr.view.qmm008.b.service.model.finder.InsuranceOfficeItemDto;

        export class ScreenModel {
            listHealthInsuranceAvgearn: KnockoutObservableArray<HealthInsuranceAvgEarnModel>;
            healthInsuranceRateModel: HealthInsuranceRateModel;
            numberEditorCommonOption: KnockoutObservable<nts.uk.ui.option.NumberEditorOption>;
            errorList: KnockoutObservableArray<any>;
            dirty: nts.uk.ui.DirtyChecker;
            constructor(officeName: string, healthModel: HealthInsuranceRateModelofScreenA) {
                var self = this;
                self.healthInsuranceRateModel = new HealthInsuranceRateModel(
                    healthModel.officeCode(),
                    officeName,
                    healthModel.historyId,
                    healthModel.startMonth(),
                    healthModel.endMonth(),
                    healthModel.autoCalculate(),
                    healthModel.rateItems(),
                    healthModel.roundingMethods());

                self.listHealthInsuranceAvgearn = ko.observableArray<HealthInsuranceAvgEarnModel>([]);

                // Common NtsNumberEditor Option
                self.numberEditorCommonOption = ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                    grouplength: 3
                }));
                //dirty check
                self.dirty = new nts.uk.ui.DirtyChecker(ko.observable(''));
                self.errorList = ko.observableArray([
                    { messageId: "AL001", message: "変更された内容が登録されていません。\r\n よろしいですか。" },
                    { messageId: "AL002", message: "データを削除します。\r\nよろしいですか？" },
                ]);
            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                self.loadHealthInsuranceAvgearn().done(() =>
                    dfd.resolve());
                return dfd.promise();
            }

            /**
             * Load HealthInsuranceAvgEarn.
             */
            private loadHealthInsuranceAvgearn(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                service.findHealthInsuranceAvgEarn(self.healthInsuranceRateModel.historyId).done(res => {
                    var salMin: number = 0;
                    res.listHealthInsuranceAvgearnDto.forEach(item => {
                        self.listHealthInsuranceAvgearn.push(
                            new HealthInsuranceAvgEarnModel(
                                item.grade,
                                item.avgEarn,
                                salMin,
                                item.upperLimit,
                                new HealthInsuranceAvgEarnValueModel(
                                    item.personalAvg.healthGeneralMny,
                                    item.personalAvg.healthNursingMny,
                                    item.personalAvg.healthBasicMny,
                                    item.personalAvg.healthSpecificMny),
                                new HealthInsuranceAvgEarnValueModel(
                                    item.companyAvg.healthGeneralMny,
                                    item.companyAvg.healthNursingMny,
                                    item.companyAvg.healthBasicMny,
                                    item.companyAvg.healthSpecificMny)
                            )
                        );
                        salMin = item.upperLimit;
                    });
                    self.dirty = new nts.uk.ui.DirtyChecker(self.listHealthInsuranceAvgearn);
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
             * Collect data from input.
             */
            private collectData(): ListHealthInsuranceAvgEarnDto {
                var self = this;
                var data: ListHealthInsuranceAvgEarnDto = { historyId: self.healthInsuranceRateModel.historyId, listHealthInsuranceAvgearnDto: [] };
                self.listHealthInsuranceAvgearn().forEach(item => {
                    data.listHealthInsuranceAvgearnDto.push(ko.toJS(item));
                });
                return data;
            }

            /**
             * Call service to save healthInsuaranceAvgearn.
             */
            public save(): void {
                var self = this;
                // Return if has error.
                if (!nts.uk.ui._viewModel.errors.isEmpty()) {
                    return;
                }
                service.updateHealthInsuranceAvgearn(self.collectData(), self.healthInsuranceRateModel.officeCode).done(() =>
                    self.closeDialog());
            }

            private clearError(): void {
                $('.has-error').ntsError('clear');
            }

            /**
             * ReCalculate the healthInsuranceAvgearn list
             */
            public reCalculate(): void {
                var self = this;
                self.clearError();
                // Clear current listHealthInsuranceAvgearn
                self.listHealthInsuranceAvgearn.removeAll();

                // Recalculate listHealthInsuranceAvgearn
                service.recalculateHealthInsuranceAvgearn(self.healthInsuranceRateModel.historyId).done(res => {
                    var salMin: number = 0;
                    res.listHealthInsuranceAvgearnDto.forEach(item => {
                        self.listHealthInsuranceAvgearn.push(
                            new HealthInsuranceAvgEarnModel(
                                item.grade,
                                item.avgEarn,
                                salMin,
                                item.upperLimit,
                                new HealthInsuranceAvgEarnValueModel(
                                    item.personalAvg.healthGeneralMny,
                                    item.personalAvg.healthNursingMny,
                                    item.personalAvg.healthBasicMny,
                                    item.personalAvg.healthSpecificMny),
                                new HealthInsuranceAvgEarnValueModel(
                                    item.companyAvg.healthGeneralMny,
                                    item.companyAvg.healthNursingMny,
                                    item.companyAvg.healthBasicMny,
                                    item.companyAvg.healthSpecificMny)
                            )
                        );
                        salMin = item.upperLimit;
                    });
                });
            }

            private closeDialogWithDirtyCheck(): void {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.errorList()[0].message).ifYes(function() {
                        self.dirty.reset();
                        self.closeDialog();
                    }).ifCancel(function() {
                    });
                }
                else {
                    self.closeDialog();
                }
            }

            /**
             * Close dialog.
             */
            private closeDialog(): void {
                nts.uk.ui.windows.close();
            }
        }

        /**
         * HealthInsuranceRate Model
         */
        export class HealthInsuranceRateModel {
            officeCode: string;
            officeName: string;
            historyId: string;
            startMonth: string;
            endMonth: string;
            autoCalculate: number;
            rateItems: HealthInsuranceRateItemModel;
            roundingMethods: HealthInsuranceRoundingModel;
            constructor(officeCode: string, officeName: string, historyId: string, startMonth: string, endMonth: string, autoCalculate: number, rateItems: HealthInsuranceRateItemModel, roundingMethods: HealthInsuranceRoundingModel) {
                this.officeCode = officeCode;
                this.officeName = officeName;
                this.historyId = historyId;
                this.startMonth = startMonth;
                this.endMonth = endMonth;
                this.autoCalculate = autoCalculate;
                this.rateItems = rateItems;
                this.roundingMethods = roundingMethods;
            }
        }

        /**
         * HealthInsuranceAvgEarn Model
         */
        export class HealthInsuranceAvgEarnModel {
            grade: number;
            avgEarn: number;
            lowerLimit: number;
            upperLimit: number;
            companyAvg: HealthInsuranceAvgEarnValueModel;
            personalAvg: HealthInsuranceAvgEarnValueModel;
            constructor(grade: number, avgEarn: number, lowerLimit: number,
                upperLimit: number, personalAvg: HealthInsuranceAvgEarnValueModel,
                companyAvg: HealthInsuranceAvgEarnValueModel) {
                this.grade = grade;
                this.avgEarn = avgEarn;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
                this.companyAvg = companyAvg;
                this.personalAvg = personalAvg;
            }
        }

        /**
         * HealthInsuranceAvgEarnValue Model
         */
        export class HealthInsuranceAvgEarnValueModel {
            healthGeneralMny: KnockoutObservable<number>;
            healthNursingMny: KnockoutObservable<number>;
            healthBasicMny: KnockoutObservable<number>;
            healthSpecificMny: KnockoutObservable<number>;
            constructor(general: number, nursing: number, basic: number, specific: number) {
                this.healthGeneralMny = ko.observable(general);
                this.healthNursingMny = ko.observable(nursing);
                this.healthBasicMny = ko.observable(basic);
                this.healthSpecificMny = ko.observable(specific);
            }
        }
    }
}