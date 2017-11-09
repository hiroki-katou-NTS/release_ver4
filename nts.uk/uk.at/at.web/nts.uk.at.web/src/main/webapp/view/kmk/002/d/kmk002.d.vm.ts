module nts.uk.at.view.kmk002.d {
    export module viewmodel {

        import FormulaSettingDto = nts.uk.at.view.kmk002.a.service.model.FormulaSettingDto;
        import SettingItemDto = nts.uk.at.view.kmk002.a.service.model.SettingItemDto;
        import EnumConstantDto = nts.uk.at.view.kmk002.a.service.model.EnumConstantDto;
        import FormulaDto = nts.uk.at.view.kmk002.a.service.model.FormulaDto;
        import ParamToD = nts.uk.at.view.kmk002.a.viewmodel.ParamToD;

        export class ScreenModel {
            formulaSetting: FormulaSetting;

            constructor() {
                this.formulaSetting = new FormulaSetting();
            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // Get params
                let dto = nts.uk.ui.windows.getShared('paramToD');

                // Set params to view model
                self.formulaSetting.fromDto(dto);

                dfd.resolve();
                return dfd.promise();
            }

            /**
             * Return data to caller screen & close dialog.
             */
            public apply(): void {
                let self = this;
                if (self.formulaSetting.isValid()) {
                    nts.uk.ui.windows.setShared('returnFromD', self.formulaSetting.toDto());
                    self.close();
                }
            }

            /**
             * Close dialog
             */
            public close(): void {
                nts.uk.ui.windows.close();
            }
        }
        class FormulaSetting {
            formulaId: string;
            formulaName: string;
            formulaAtr: string;
            selectableFormulas: KnockoutObservableArray<FormulaDto>;

            minusSegment: KnockoutObservable<boolean>;
            operator: KnockoutObservable<number>;
            leftItem: FormulaSettingItem;
            rightItem: FormulaSettingItem;

            // enable / disable flag
            isLeftCbbEnable: KnockoutComputed<boolean>;
            isRightCbbEnable: KnockoutComputed<boolean>;
            isFormulasEmpty: KnockoutComputed<boolean>;

            // datasource
            operatorDatasource: KnockoutObservableArray<EnumConstantDto>;

            constructor() {
                this.minusSegment = ko.observable(false);
                this.operator = ko.observable(0);
                this.leftItem = new FormulaSettingItem();
                this.rightItem = new FormulaSettingItem();
                this.selectableFormulas = ko.observableArray([]);

                // enable / disable flag
                this.isFormulasEmpty = ko.computed(() => {
                    if (nts.uk.util.isNullOrEmpty(this.selectableFormulas())) {
                        return true;
                    }
                    return false;
                });
                this.isLeftCbbEnable = ko.computed(() => {
                    if (this.isFormulasEmpty() || this.leftItem.isInputValue()) {
                        return false;
                    }
                    return true;
                });
                this.isRightCbbEnable = ko.computed(() => {
                    if (this.isFormulasEmpty() || this.rightItem.isInputValue()) {
                        return false;
                    }
                    return true;
                });

                // fixed 
                this.leftItem.dispOrder = 1;
                this.leftItem.settingMethod(0);
                this.rightItem.dispOrder = 2;
                this.rightItem.settingMethod(1);

                this.operatorDatasource = ko.observableArray([]);

                // default value
                this.formulaName = '';
                this.formulaAtr = '';

            }

            /**
             * Data input validation
             */
            public isValid(): boolean {
                let self = this;

                // Check divide by zero
                if (self.rightItem.settingMethod() == 1 && self.operator() == 3 && self.rightItem.inputValue() == 0) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_638" });
                    return false;
                }

                // both item setting method = number.
                if (self.isBothNumberInput()) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_420" });
                    return false;
                }

                // Check required input of right item
                if (self.rightItem.settingMethod() == SettingMethod.NUMBER_INPUT) {
                    $('#inp-right-item').ntsEditor('validate');
                }

                // Check required input of left item
                if (self.leftItem.settingMethod() == SettingMethod.NUMBER_INPUT) {
                    $('#inp-left-item').ntsEditor('validate');
                }

                // Validate calculation
                // If operator is '+' or '-' , 
                // both item must have the same attribute if setting method is both item selection 
                if (self.operator() == 0 || self.operator() == 1) {
                    if (self.isBothItemSelect() && self.isDifferentAtr()) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_114" });
                        return false;
                    }
                }

                // Validate number input.
                if ($('.nts-editor').ntsError('hasError')) {
                    return false;
                }

                return true;

            }

            /**
             * Check if attribute of left item and right item is different.
             */
            private isDifferentAtr(): boolean {
                let self = this;
                let leftItem: FormulaDto = self.findFormulaById(self.leftItem.formulaItemId());
                let rightItem: FormulaDto = self.findFormulaById(self.rightItem.formulaItemId());

                if (leftItem.formulaAtr != rightItem.formulaAtr) {
                    return true;
                }

                return false;
            }

            /**
             * Check if both item's method setting is item selection
             */
            private isBothItemSelect(): boolean {
                let self = this;
                if (self.leftItem.settingMethod() == SettingMethod.ITEM_SELECTION
                    && self.rightItem.settingMethod() == SettingMethod.ITEM_SELECTION) {
                    return true;
                }
                return false;
            }

            /**
             * Check if both item's method setting is numerical input.
             */
            private isBothNumberInput(): boolean {
                let self = this;
                if (self.leftItem.settingMethod() == SettingMethod.NUMBER_INPUT
                    && self.rightItem.settingMethod() == SettingMethod.NUMBER_INPUT) {
                    return true;
                }
                return false;
            }

            /**
             * find formula by id.
             */
            private findFormulaById(id: string): any {
                let self = this;
                let f = _.find(self.selectableFormulas(), item => item.formulaId == id);
                return f;
            }

            /**
             * Convert to viewmodel
             */
            public fromDto(dto: ParamToD): void {
                let self = this;
                self.formulaId = dto.formulaId;
                self.formulaName = dto.formulaName;
                self.formulaAtr = dto.formulaAtr;
                self.minusSegment(dto.formulaSetting.minusSegment == 1 ? true: false);
                self.operator(dto.formulaSetting.operator);
                self.leftItem.fromDto(dto.formulaSetting.leftItem);
                self.rightItem.fromDto(dto.formulaSetting.rightItem);
                self.selectableFormulas(dto.selectableFormulas);
                self.operatorDatasource(dto.operatorDatasource);
            }

            /**
             * convert viewmodel to dto
             */
            public toDto(): FormulaSettingDto {
                let self = this;
                let dto: FormulaSettingDto = <FormulaSettingDto>{};

                dto.minusSegment = self.minusSegment() == true ? 1 : 0;
                dto.operator = self.operator();
                dto.leftItem = self.leftItem.toDto();
                dto.rightItem = self.rightItem.toDto();

                return dto;
            }

        }
        /**
         * Formula setting item
         */
        class FormulaSettingItem {
            settingMethod: KnockoutObservable<number>;
            dispOrder: number;
            inputValue: KnockoutObservable<number>;
            formulaItemId: KnockoutObservable<string>;
            settingItemStash: SettingItemDto;

            constructor() {
                this.settingMethod = ko.observable(0);
                this.inputValue = ko.observable(0);
                this.formulaItemId = ko.observable('');
            }

            /**
             * is input value check
             */
            public isInputValue(): boolean {
                if (this.settingMethod() == SettingMethod.ITEM_SELECTION) {
                    return false;
                }
                return true;
            }

            /**
             * convert dto to viewmodel
             */
            public fromDto(dto: SettingItemDto): void {
                this.settingMethod(dto.settingMethod);
                this.dispOrder = dto.dispOrder;
                this.inputValue(dto.inputValue);
                this.formulaItemId(dto.formulaItemId);

                // save data to stash
                this.settingItemStash = jQuery.extend(true, {}, dto);
            }

            /**
             * convert viewmodel to dto
             */
            public toDto(): SettingItemDto {
                let self = this;
                let dto: SettingItemDto = <SettingItemDto>{};

                dto.settingMethod = this.settingMethod();
                dto.dispOrder = this.dispOrder;
                dto.inputValue = this.inputValue();
                dto.formulaItemId = this.formulaItemId();

                // get original data from stash if control is disabled
                if (this.settingMethod() == SettingMethod.ITEM_SELECTION) {
                    // reset input value
                    dto.inputValue = this.settingItemStash.inputValue;
                } else {
                    // reset formulaItem id
                    dto.formulaItemId = this.settingItemStash.formulaItemId;
                }

                return dto;
            }
        }

        enum SettingMethod {
            ITEM_SELECTION = 0,
            NUMBER_INPUT = 1
        }
    }
}