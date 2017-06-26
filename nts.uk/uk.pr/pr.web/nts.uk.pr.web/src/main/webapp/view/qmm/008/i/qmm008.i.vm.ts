module nts.uk.pr.view.qmm008.i {
    export module viewmodel {
        import FunRateItemModel = nts.uk.pr.view.qmm008.c.viewmodel.FunRateItemModel;
        import PensionRateItemModel = nts.uk.pr.view.qmm008.c.viewmodel.PensionRateItemModel;
        import PensionRateRoundingModel = nts.uk.pr.view.qmm008.c.viewmodel.PensionRateRoundingModel;
        import PensionRateModelFromScreenA = nts.uk.pr.view.qmm008.c.viewmodel.PensionRateModel;
        import ListPensionAvgearnDto = nts.uk.pr.view.qmm008.i.service.model.ListPensionAvgearnDto;

        export class ScreenModel {
            listPensionAvgearnModel: KnockoutObservableArray<PensionAvgearnModel>;
            numberEditorCommonOption: KnockoutObservable<nts.uk.ui.option.NumberEditorOption>;

            // expand table var
            leftShow: KnockoutObservable<boolean>;
            rightShow: KnockoutObservable<boolean>;
            leftBtnText: KnockoutComputed<string>;
            rightBtnText: KnockoutComputed<string>;

            pensionRateModel: PensionRateModel;
            dirty: nts.uk.ui.DirtyChecker;
            errorList: KnockoutObservableArray<any>;
            constructor(officeName: string, pensionModel: PensionRateModelFromScreenA) {
                var self = this;
                self.listPensionAvgearnModel = ko.observableArray<PensionAvgearnModel>([]);
                self.pensionRateModel = new PensionRateModel(
                    pensionModel.historyId,
                    pensionModel.officeCode(),
                    officeName,
                    pensionModel.startMonth(),
                    pensionModel.endMonth(),
                    pensionModel.autoCalculate(),
                    pensionModel.fundInputApply(),
                    pensionModel.rateItems(),
                    pensionModel.fundRateItems(),
                    pensionModel.roundingMethods(),
                    pensionModel.childContributionRate());
                self.leftShow = ko.observable(true);
                self.rightShow = ko.observable(true);
                self.leftBtnText = ko.computed(function() { if (self.leftShow()) return "—"; return "+"; });
                self.rightBtnText = ko.computed(function() { if (self.rightShow()) return "—"; return "+"; });

                // Common NtsNumberEditor Option
                self.numberEditorCommonOption = ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                    grouplength: 3
                }));
                self.dirty = new nts.uk.ui.DirtyChecker(self.listPensionAvgearnModel);
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
                $.when(self.loadPensionAvgearn()).done(() => {
                    dfd.resolve();
                });
                return dfd.promise();
            }


            /**
             * Load PensionAvgEarn.
             */
            private loadPensionAvgearn(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                service.findPensionAvgearn(self.pensionRateModel.historyId).done(res => {
                    var salMin: number = 0;
                    res.listPensionAvgearnDto.forEach(item => {
                        self.listPensionAvgearnModel.push(new PensionAvgearnModel(
                            item.grade,
                            item.avgEarn,
                            salMin,
                            item.upperLimit,
                            new PensionAvgearnValueModel(
                                item.companyFund.maleAmount,
                                item.companyFund.femaleAmount,
                                item.companyFund.unknownAmount),
                            new PensionAvgearnValueModel(
                                item.companyFundExemption.maleAmount,
                                item.companyFundExemption.femaleAmount,
                                item.companyFundExemption.unknownAmount),
                            new PensionAvgearnValueModel(
                                item.companyPension.maleAmount,
                                item.companyPension.femaleAmount,
                                item.companyPension.unknownAmount),
                            new PensionAvgearnValueModel(
                                item.personalFund.maleAmount,
                                item.personalFund.femaleAmount,
                                item.personalFund.unknownAmount),
                            new PensionAvgearnValueModel(
                                item.personalFundExemption.maleAmount,
                                item.personalFundExemption.femaleAmount,
                                item.personalFundExemption.unknownAmount),
                            new PensionAvgearnValueModel(
                                item.personalPension.maleAmount,
                                item.personalPension.femaleAmount,
                                item.personalPension.unknownAmount),
                            item.childContributionAmount));
                        salMin = item.upperLimit;
                    });
                    self.dirty.reset();
                    dfd.resolve();
                });
                return dfd.promise();
            }

            /**
             * Collect data from input.
             */
            private collectData(): ListPensionAvgearnDto {
                var self = this;
                var data: ListPensionAvgearnDto = { historyId: self.pensionRateModel.historyId, listPensionAvgearnDto: [] };
                self.listPensionAvgearnModel().forEach(item => {
                    data.listPensionAvgearnDto.push(ko.toJS(item));
                });
                return data;
            }

            /**
             * Call service to save pensionAvgearn.
             */
            private save(): void {
                var self = this;
                // Return if has error.
                if (!nts.uk.ui._viewModel.errors.isEmpty()) {
                    return;
                }
                service.updatePensionAvgearn(self.collectData(), self.pensionRateModel.officeCode)
                    .done(() => self.closeDialog());
            }

            /**
             * Button toggle Pension welfare pension.
             */
            private leftToggle(): void {
                this.leftShow(!this.leftShow());
            }

            /**
             * Button toggle right.
             */
            private rightToggle(): void {
                this.rightShow(!this.rightShow());
            }

            private clearError(): void {
                $('.has-error').ntsError('clear');
            }

            /**
             * ReCalculate the listPensionAvgearnModel
             */
            public reCalculate(): void {
                // TODO: LÀM LẠI
                var self = this;
                self.clearError();
                // Clear current listPensionAvgearnModel
                self.listPensionAvgearnModel.removeAll();

                // Recalculate listPensionAvgearnModel
                service.recalculatePensionAvgearn(self.pensionRateModel.historyId)
                    .done(res => {
                        var salMin: number = 0;
                        res.listPensionAvgearnDto.forEach(item => {
                            self.listPensionAvgearnModel.push(new PensionAvgearnModel(
                                item.grade,
                                item.avgEarn,
                                salMin,
                                item.upperLimit,
                                new PensionAvgearnValueModel(
                                    item.companyFund.maleAmount,
                                    item.companyFund.femaleAmount,
                                    item.companyFund.unknownAmount),
                                new PensionAvgearnValueModel(
                                    item.companyFundExemption.maleAmount,
                                    item.companyFundExemption.femaleAmount,
                                    item.companyFundExemption.unknownAmount),
                                new PensionAvgearnValueModel(
                                    item.companyPension.maleAmount,
                                    item.companyPension.femaleAmount,
                                    item.companyPension.unknownAmount),
                                new PensionAvgearnValueModel(
                                    item.personalFund.maleAmount,
                                    item.personalFund.femaleAmount,
                                    item.personalFund.unknownAmount),
                                new PensionAvgearnValueModel(
                                    item.personalFundExemption.maleAmount,
                                    item.personalFundExemption.femaleAmount,
                                    item.personalFundExemption.unknownAmount),
                                new PensionAvgearnValueModel(
                                    item.personalPension.maleAmount,
                                    item.personalPension.femaleAmount,
                                    item.personalPension.unknownAmount),
                                item.childContributionAmount));
                            salMin = item.upperLimit;
                        });
                        self.dirty.reset();
                    });
            }

            private closeDialogWithDirtyCheck(): void {
                var self = this;
                if (self.dirty.isDirty()) {
                    nts.uk.ui.dialog.confirm(self.errorList()[0].message).ifYes(function() {
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
         * PensionRate Model
         */
        export class PensionRateModel {
            historyId: string;
            officeCode: string;
            officeName: string;
            startMonth: string;
            endMonth: string;
            autoCalculate: number;
            fundInputApply: number;
            rateItems: PensionRateItemModel;
            fundRateItems: FunRateItemModel;
            roundingMethods: PensionRateRoundingModel
            childContributionRate: KnockoutObservable<number>;
            constructor(
                historyId: string,
                officeCode: string,
                officeName: string,
                startMonth: string,
                endMonth: string,
                autoCalculate: number,
                fundInputApply: number,
                rateItems: PensionRateItemModel,
                fundRateItems: FunRateItemModel,
                roundingMethods: PensionRateRoundingModel,
                childContributionRate: number) {
                this.historyId = historyId;
                this.officeCode = officeCode;
                this.officeName = officeName;
                this.startMonth = startMonth;
                this.endMonth = endMonth;
                this.autoCalculate = autoCalculate;
                this.fundInputApply = fundInputApply;
                this.rateItems = rateItems;
                this.fundRateItems = fundRateItems;
                this.roundingMethods = roundingMethods;
                this.childContributionRate = ko.observable(childContributionRate);
            }
        }

        /**
         * PensionAvgearn Model
         */
        export class PensionAvgearnModel {
            grade: number;
            avgEarn: number;
            lowerLimit: number;
            upperLimit: number;
            companyFund: PensionAvgearnValueModel;
            companyFundExemption: PensionAvgearnValueModel;
            companyPension: PensionAvgearnValueModel;
            personalFund: PensionAvgearnValueModel;
            personalFundExemption: PensionAvgearnValueModel;
            personalPension: PensionAvgearnValueModel;
            childContributionAmount: KnockoutObservable<number>;
            constructor(
                grade: number,
                avgEarn: number,
                lowerLimit: number,
                upperLimit: number,
                companyFund: PensionAvgearnValueModel,
                companyFundExemption: PensionAvgearnValueModel,
                companyPension: PensionAvgearnValueModel,
                personalFund: PensionAvgearnValueModel,
                personalFundExemption: PensionAvgearnValueModel,
                personalPension: PensionAvgearnValueModel,
                childContributionAmount: number) {
                this.grade = grade;
                this.avgEarn = avgEarn;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
                this.companyFund = companyFund;
                this.companyFundExemption = companyFundExemption;
                this.companyPension = companyPension;
                this.personalFund = personalFund;
                this.personalFundExemption = personalFundExemption;
                this.personalPension = personalPension;
                this.childContributionAmount = ko.observable(childContributionAmount);
            }
        }

        /**
         * PensionAvgearnValue Model
         */
        export class PensionAvgearnValueModel {
            maleAmount: KnockoutObservable<number>;
            femaleAmount: KnockoutObservable<number>;
            unknownAmount: KnockoutObservable<number>;
            constructor(maleAmount: number, femaleAmount: number, unknownAmount: number) {
                this.maleAmount = ko.observable(maleAmount);
                this.femaleAmount = ko.observable(femaleAmount);
                this.unknownAmount = ko.observable(unknownAmount);
            }
        }
    }
}