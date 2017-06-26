module nts.uk.pr.view.qmm007.a {
    export module viewmodel {
        import UnitPriceHistoryDto = service.model.UnitPriceHistoryDto;
        import ScreenBaseModel = base.simplehistory.viewmodel.ScreenBaseModel;
        export class ScreenModel extends ScreenBaseModel<service.model.UnitPrice, service.model.UnitPriceHistory> {
            // UnitPriceHistory Model
            unitPriceHistoryModel: KnockoutObservable<UnitPriceHistoryModel>;

            // Setting type
            isContractSettingEnabled: KnockoutObservable<boolean>;

            // Flags
            isLoading: KnockoutObservable<boolean>;

            // Nts text editor options
            textEditorOption: KnockoutObservable<nts.uk.ui.option.TextEditorOption>;
            // Switch button data source
            switchButtonDataSource: KnockoutObservableArray<SwitchButtonDataSource>;

            // Dirty checker
            dirtyChecker: nts.uk.ui.DirtyChecker;

            constructor() {
                super({
                    functionName: '会社一律金額',
                    service: service.instance,
                    removeMasterOnLastHistoryRemove: true
                });
                var self = this;
                self.isLoading = ko.observable(true);
                self.unitPriceHistoryModel = ko.observable<UnitPriceHistoryModel>(new UnitPriceHistoryModel(self.getDefaultUnitPriceHistory()));
                self.dirtyChecker = new nts.uk.ui.DirtyChecker(self.unitPriceHistoryModel);
                self.switchButtonDataSource = ko.observableArray<SwitchButtonDataSource>([
                    { code: ApplySetting.APPLY, name: '対象' },
                    { code: ApplySetting.NOTAPPLY, name: '対象外' }
                ]);

                // Setting type
                self.isContractSettingEnabled = ko.computed(() => {
                    return self.unitPriceHistoryModel().fixPaySettingType() == SettingType.CONTRACT;
                })

                // Nts text editor options
                self.textEditorOption = ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    textalign: "left"
                }));
            }

            /**
             * Override
             * Create or Update UnitPriceHistory.
             */
            onSave(): JQueryPromise<string> {
                var self = this;
                var dfd = $.Deferred<string>();
                // Validate.
                self.validate();
                // Return if has error.
                if ($('.nts-input').ntsError('hasError')) {
                    dfd.reject();
                    return dfd.promise();
                }

                if (self.isNewMode()) {
                    service.instance.create(ko.toJS(self.unitPriceHistoryModel())).done(res => {
                        dfd.resolve(res.uuid);
                        self.dirtyChecker.reset();
                    }).fail(res => {
                        dfd.reject();
                        self.clearErrors();
                        self.setMessages(res.messageId);
                    });
                } else {
                    service.instance.update(ko.toJS(self.unitPriceHistoryModel())).done((res) => {
                        dfd.resolve(self.unitPriceHistoryModel().id);
                        self.dirtyChecker.reset();
                    }).fail(res => {
                        dfd.reject();
                        self.clearErrors();
                        self.setMessages(res.messageId);
                    });
                }
                return dfd.promise();
            }

           /**
            * Override
            * Load UnitPriceHistory detail.
            */
            onSelectHistory(id: string): void {
                var self = this;
                self.isLoading(true);
                service.instance.findHistoryByUuid(id).done(dto => {
                    self.setUnitPriceHistoryModel(dto);
                    self.dirtyChecker.reset();
                    self.isLoading(false);
                });
            }

            /**
             * Override
             * Clear all input and switch to new mode.
             */
            onRegistNew(): void {
                var self = this;
                self.clearInput();
                self.dirtyChecker.reset();
            }

            // Override
            isDirty(): boolean {
                var self = this;
                return self.dirtyChecker.isDirty();
            }

            // Override
            clearErrors(): void {
                $('#inpCode').ntsError('clear');
                $('#inpName').ntsError('clear');
                $('#inpStartMonth').ntsError('clear');
                $('#inpBudget').ntsError('clear');
            }

            private setMessages(messageId: string): void {
                var self = this;
                switch (messageId) {
                    case 'ER005':
                        $('#inpCode').ntsError('set', '入力した＊は既に存在しています。\r\n ＊を確認してください。');
                        break;
                    case 'ER011':
                        $('#inpStartMonth').ntsError('set', '対象データがありません。');
                        break;
                    default:// Do nothing.
                        break;
                }
            }

            /**
             * Set the UnitPriceHistoryModel
             */
            private setUnitPriceHistoryModel(dto: UnitPriceHistoryDto): void {
                var model: UnitPriceHistoryModel = this.unitPriceHistoryModel();
                model.id = dto.id;
                model.unitPriceCode(dto.unitPriceCode);
                model.unitPriceName(dto.unitPriceName);
                model.startMonth(dto.startMonth);
                model.endMonth(dto.endMonth);
                model.budget(dto.budget);
                model.fixPaySettingType(dto.fixPaySettingType);
                model.fixPayAtr(dto.fixPayAtr);
                model.fixPayAtrMonthly(dto.fixPayAtrMonthly);
                model.fixPayAtrDayMonth(dto.fixPayAtrDayMonth);
                model.fixPayAtrDaily(dto.fixPayAtrDaily);
                model.fixPayAtrHourly(dto.fixPayAtrHourly);
                model.memo(dto.memo);
            }

            private validate(): void {
                $('#inpCode').ntsEditor('validate');
                $('#inpName').ntsEditor('validate');
                $('#inpStartMonth').ntsEditor('validate');
                $('#inpBudget').ntsEditor('validate');
            }

            private clearInput(): void {
                var self = this;
                self.setUnitPriceHistoryModel(self.getDefaultUnitPriceHistory());
            }

            /**
             * Get default history
             */
            private getDefaultUnitPriceHistory(): UnitPriceHistoryDto {
                var defaultHist: any = {};
                defaultHist.id = '';
                defaultHist.unitPriceCode = '';
                defaultHist.unitPriceName = '';
                defaultHist.startMonth = parseInt(nts.uk.time.formatDate(new Date(), 'yyyyMM'));
                defaultHist.endMonth = 999912;
                defaultHist.budget = 0;
                defaultHist.fixPaySettingType = SettingType.COMPANY;
                defaultHist.fixPayAtr = ApplySetting.APPLY;
                defaultHist.fixPayAtrMonthly = ApplySetting.APPLY;
                defaultHist.fixPayAtrDayMonth = ApplySetting.APPLY;
                defaultHist.fixPayAtrDaily = ApplySetting.APPLY;
                defaultHist.fixPayAtrHourly = ApplySetting.APPLY;
                defaultHist.memo = '';
                return defaultHist;
            }

        }
        export class UnitPriceHistoryModel {
            id: string;
            unitPriceCode: KnockoutObservable<string>;
            unitPriceName: KnockoutObservable<string>;
            startMonth: KnockoutObservable<number>;
            endMonth: KnockoutObservable<number>;
            budget: KnockoutObservable<number>;
            fixPaySettingType: KnockoutObservable<string>;
            fixPayAtr: KnockoutObservable<string>;
            fixPayAtrMonthly: KnockoutObservable<string>;
            fixPayAtrDayMonth: KnockoutObservable<string>;
            fixPayAtrDaily: KnockoutObservable<string>;
            fixPayAtrHourly: KnockoutObservable<string>;
            memo: KnockoutObservable<string>;

            constructor(historyDto: UnitPriceHistoryDto) {
                this.id = historyDto.id;
                this.unitPriceCode = ko.observable(historyDto.unitPriceCode);
                this.unitPriceName = ko.observable(historyDto.unitPriceName);
                this.startMonth = ko.observable(historyDto.startMonth);
                this.endMonth = ko.observable(historyDto.endMonth);
                this.budget = ko.observable(historyDto.budget);
                this.fixPaySettingType = ko.observable(historyDto.fixPaySettingType);
                this.fixPayAtr = ko.observable(historyDto.fixPayAtr);
                this.fixPayAtrMonthly = ko.observable(historyDto.fixPayAtrMonthly);
                this.fixPayAtrDayMonth = ko.observable(historyDto.fixPayAtrDayMonth);
                this.fixPayAtrDaily = ko.observable(historyDto.fixPayAtrDaily);
                this.fixPayAtrHourly = ko.observable(historyDto.fixPayAtrHourly);
                this.memo = ko.observable(historyDto.memo);
            }
        }

        export class SettingType {
            static COMPANY = 'Company';
            static CONTRACT = 'Contract';
        }

        export class ApplySetting {
            static APPLY = 'Apply';
            static NOTAPPLY = 'NotApply';
        }

        export interface SwitchButtonDataSource {
            code: string;
            name: string;
        }
    }
}