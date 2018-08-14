module nts.uk.at.view.kmk002.a {
    import OptionalItemDto = nts.uk.at.view.kmk002.a.service.model.OptionalItemDto;
    import OptionalItemHeaderDto = nts.uk.at.view.kmk002.a.service.model.OptionalItemHeader;
    import CalcResultRangeDto = nts.uk.at.view.kmk002.a.service.model.CalcResultRangeDto;
    import FormulaDto = nts.uk.at.view.kmk002.a.service.model.FormulaDto;
    import FormulaSettingDto = nts.uk.at.view.kmk002.a.service.model.FormulaSettingDto;
    import ItemSelectionDto = nts.uk.at.view.kmk002.a.service.model.ItemSelectionDto;
    import SettingItemDto = nts.uk.at.view.kmk002.a.service.model.SettingItemDto;
    import AttendanceItemDto = nts.uk.at.view.kmk002.a.service.model.AttendanceItemDto;
    import RoundingDto = nts.uk.at.view.kmk002.a.service.model.RoundingDto;
    import OptItemEnumDto = nts.uk.at.view.kmk002.a.service.model.OptItemEnumDto;
    import EnumConstantDto = nts.uk.at.view.kmk002.a.service.model.EnumConstantDto;
    import EnumAdaptor = nts.uk.at.view.kmk002.a.service.model.EnumAdaptor;

    export module viewmodel {

        export class ScreenModel {
            optionalItemHeader: OptionalItemHeader;

            constructor() {
                let self = this;
                self.optionalItemHeader = new OptionalItemHeader();

            }

            /**
             * Start page.
             */
            public startPage(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // set ntsFixedTable style
                $("#tbl-calc-formula").ntsFixedTable({ height: 250 });

                // Load data.
                self.loadEnum()
                    .done(() => {
                        self.optionalItemHeader.loadOptionalItemHeaders()
                            .done(() => self.optionalItemHeader.initialize()
                                .done(() => dfd.resolve()));
                    });

                return dfd.promise();
            }

            /**
             * Load enum constant
             */
            private loadEnum(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();
                service.getOptItemEnum().done(optItemEnum => {
                    Enums.ENUM_OPT_ITEM = optItemEnum;
                    Enums.ENUM_OPT_ITEM.calcAtr.reverse(); // reverse to correct order.
                    dfd.resolve();
                });
                return dfd.promise();
            }

        }

        //******************************************************************
        //************************* VIEW MODEL *****************************
        //******************************************************************

        /**
         * The class OptionalItem
         */
        class OptionalItem {
            static selectedFormulas: KnockoutObservableArray<number> = ko.observableArray([]);

            optionalItemNo: KnockoutObservable<number>;
            optionalItemName: KnockoutObservable<string>;
            optionalItemAtr: KnockoutObservable<number>;
            usageAtr: KnockoutObservable<number>;
            empConditionAtr: KnockoutObservable<number>;
            performanceAtr: KnockoutObservable<number>;
            calcResultRange: CalculationResultRange;
            calcFormulas: KnockoutObservableArray<Formula>;
            applyFormula: KnockoutObservable<string>;
            selectedFormulaAbove: number;
            selectedFormulaBelow: number;
            unit: KnockoutObservable<string>;

            // Switch button data source
            usageClsDatasource: KnockoutObservableArray<any>;
            empConClsDatasource: KnockoutObservableArray<any>;
            perfClsDatasource: KnockoutObservableArray<any>;
            atrDataSource: EnumConstantDto[];

            // function
            getOptItemNoAbove: () => Array<string>;

            // flag
            hasChanged: boolean;
            isUsed: KnockoutObservable<boolean>;
            checkedAllFormula: KnockoutObservable<boolean>;
            isTimeSelected: KnockoutObservable<boolean>;
            isNumberSelected: KnockoutObservable<boolean>;
            isAmountSelected: KnockoutObservable<boolean>;
            enableUnit: KnockoutComputed<boolean>;
            isCheckedFromChild = false;

            // stash
            optionalItemAtrStash: number;
            performanceAtrStash: number;
            optionalItemDtoStash: OptionalItemDto;

            constructor() {
                this.optionalItemNo = ko.observable('');
                this.optionalItemName = ko.observable('');
                this.optionalItemAtr = ko.observable(null);
                this.usageAtr = ko.observable(0);
                this.empConditionAtr = ko.observable(0);
                this.performanceAtr = ko.observable(0);
                this.calcResultRange = new CalculationResultRange();
                this.calcFormulas = ko.observableArray<Formula>([]);
                this.applyFormula = ko.observable('');
                this.selectedFormulaAbove = 0;
                this.selectedFormulaBelow = 0;
                this.unit = ko.observable('');

                // flags
                this.hasChanged = false;
                this.isUsed = ko.observable(false);
                this.checkedAllFormula = ko.observable(false);
                this.isTimeSelected = ko.observable(false);
                this.isAmountSelected = ko.observable(false);
                this.isNumberSelected = ko.observable(false);
                this.enableUnit = ko.computed(() => {
                    return this.isNumberSelected() || this.isAmountSelected();
                });

                // init datasource
                this.initDatasource();

                // init subscribe
                this.initSubscribe();

            }

            /**
             * Get symbol value by formula id.
             */
            public getSymbolById(id: string): string {
                let self = this;
                let rs = _.find(self.calcFormulas(), item => item.formulaId == id);
                return rs ? rs.symbolValue : 'formula not found';
            }

            /**
             * Get formula name by id
             */
            public getFormulaNameById(id: string): string {
                let self = this;
                let rs = _.find(self.calcFormulas(), item => item.formulaId == id);
                return rs ? rs.formulaName() : 'formula not found';
            }

            /**
             * Initial datasource
             */
            private initDatasource(): void {
                let self = this;
                self.usageClsDatasource = ko.observableArray([
                    { code: 1, name: nts.uk.resource.getText("KMK002_14") }, // used
                    { code: 0, name: nts.uk.resource.getText("KMK002_15") } // not used
                ]);
                self.empConClsDatasource = ko.observableArray([
                    { code: 0, name: nts.uk.resource.getText("KMK002_17") }, // not apply
                    { code: 1, name: nts.uk.resource.getText("KMK002_18") } // apply
                ]);
                self.perfClsDatasource = ko.observableArray([
                    { code: 1, name: nts.uk.resource.getText("KMK002_22") }, // daily
                    { code: 0, name: nts.uk.resource.getText("KMK002_23") } // monthly
                ]);
                self.atrDataSource = [];
            }

            /**
             * Initial subscribe
             */
            private initSubscribe(): void {
                let self = this;

                // Event on click checkAll checkbox
                self.checkedAllFormula.subscribe(vl => {

                    // if the value is changed because of a child element (formula) then do nothing.
                    if (self.isCheckedFromChild == true) {

                        // reset flag.
                        self.isCheckedFromChild = false;

                        // return and do nothing.
                        return;
                    }

                    // check all unchecked formula
                    if (vl === true) {
                        _.each(self.calcFormulas(), item => {
                            if (item.selected() == false) {
                                item.isCheckFromParent = true
                                item.check();
                            }
                        });
                        return;
                    }

                    // uncheck all checked formula
                    if (vl === false) {
                        _.each(self.calcFormulas(), item => {
                            if (item.selected() == true) {
                                item.isCheckFromParent = true
                                item.uncheck();
                            }
                        });
                    }
                });

                // selected formula subscribe
                OptionalItem.selectedFormulas.subscribe(vl => {
                    // set selected formula below and above.
                    self.setSelectedFormulaBelowAndAbove(vl);
                });

                // optionalItemNo dirty check
                self.optionalItemNo.subscribe(v => {
                    self.hasChanged = true;
                });

                // uncheck all if remove all formulas
                self.calcFormulas.subscribe(vl => {
                    if (nts.uk.util.isNullOrEmpty(vl)) {
                        self.checkedAllFormula(false);
                        self.applyFormula('');
                    }

                    // set apply formula
                    self.setApplyFormula();
                });

                // Event on performanceAtr value changed
                self.performanceAtr.subscribe(value => {

                    // if value change because of select new optional item
                    // or new value == value in stash
                    // then do nothing
                    if (self.hasChanged || value == self.performanceAtrStash) {
                        Formula.performanceAtr = value; // param for screen C
                        return;
                    }

                    // if has formulas
                    if (self.isFormulaSet()) {
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_506' }).ifYes(() => {
                            Formula.performanceAtr = value; // param for screen C

                            // remove all formulas
                            self.removeAllFormulas();

                            // save new value to stash
                            self.performanceAtrStash = value;

                        }).ifNo(() => {
                            // get old value from stash
                            self.performanceAtr(self.performanceAtrStash);
                        });
                    } else {
                        // save new value to stash
                        self.performanceAtrStash = value;
                    }
                });

                // Event on optionalItemAtr value changed
                self.optionalItemAtr.subscribe(value => {

                    // if value change because of select new optional item
                    // or new value == value in stash
                    // then do nothing
                    if (self.hasChanged || value == self.optionalItemAtrStash) {
                        return;
                    }

                    // Check whether has formula or calculation result range is set.
                    if (self.isFormulaSet() || self.calcResultRange.isSet()) {
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_573' }).ifYes(() => {

                            // remove all formulas
                            self.removeAllFormulas();

                            // reset calc result range.
                            self.calcResultRange.resetValue();

                            self.checkSelectedAtr();

                            // save new value to stash
                            self.optionalItemAtrStash = value;

                        }).ifNo(() => {
                            // get old value from stash
                            self.optionalItemAtr(self.optionalItemAtrStash);
                        });
                    } else {
                        self.checkSelectedAtr();
                        // save new value to stash
                        self.optionalItemAtrStash = value;
                    }
                });
            }

            /**
             * Checks whether an item of a nonexistent line is set in a formula
             */
            public findInvalidFormula(): Formula[] {
                let self = this;

                // find invalid formulas
                return _.filter(self.calcFormulas(), item => {

                    // only check formula of type 'formula setting'
                    if (item.isTypeOfFormulaSetting()) {
                        let leftItem = item.formulaSetting.leftItem;
                        let rightItem = item.formulaSetting.rightItem;

                        // check whether left item is a nonexistent formula
                        if (item.isSettingMethodOfItemSelection(leftItem)
                            && !self.isFormulaExist(leftItem.formulaItemId)) {
                            return true;
                        }

                        // check whether right item is a nonexistent formula
                        if (item.isSettingMethodOfItemSelection(rightItem)
                            && !self.isFormulaExist(rightItem.formulaItemId)) {
                            return true;
                        }
                    }

                    // setting is valid
                    return false;
                });

            }

            /**
             * Check whether daily rounding is enabled or not
             */
            public isDailyRoundingEnabled(): boolean {
                let self = this;
                return self.isUsed() && self.performanceAtr() == 1; 
            }

            /**
             * Check a formula's existent
             */
            private isFormulaExist(id: string): boolean {
                let self = this;
                return _.find(self.calcFormulas(), item => item.formulaId == id) != undefined;
            }

            /**
             * Validate required formulaName & required setting formula
             */
            public validateListFormula(): void {
                let self = this;
                self.calcFormulas().forEach((item, index) => {
                    // order start from 1, index start from 0
                    index++;
                    $('#formulaName' + index).ntsEditor('validate');
                    $('#settingResult' + index).ntsEditor('validate');
                });
            }

            /**
             * Remove all formula.
             */
            private removeAllFormulas(): void {
                let self = this;

                // clear error
                $('.inp-formula-required').ntsError('clear');

                // remove all formula
                self.calcFormulas([]);
            }

            /**
             * Remove selected formula
             */
            private removeSelectedFormulas(): void {
                let self = this;
                let updatedList = self.calcFormulas();

                OptionalItem.selectedFormulas().forEach(order => {
                    $('#formulaName' + order).ntsError('clear');
                    $('#settingResult' + order).ntsError('clear');

                    // remove item.
                    _.remove(updatedList, item => item.orderNo() == order);
                });

                // update formula list.
                self.calcFormulas(updatedList);

                // clear selected
                OptionalItem.selectedFormulas([]);
            }

            /**
             * Add formula above
             */
            public addFormulaAbove(): void {
                let self = this;

                // check before add
                // if zz is used
                // or list formula has at least 1 item and no formula checked
                // => show error message and return
                if (self.isFormulaSet() && self.canNotAddFormula()) {
                    return;
                }

                let aboveOrder = self.selectedFormulaAbove;

                // update order of below items.
                self.updateOrderAfter(aboveOrder - 1);

                // add formula.
                self.addFormulaAtOrder(aboveOrder);

                // recheck the checkAll checkbox
                self.reCheckAll();

            }

            /**
             * Set last formula to apply formula
             */
            private setApplyFormula(): void {
                let self = this
                let lastFormula = _.last(this.calcFormulas());
                if (lastFormula) {
                    let settingResult = lastFormula.settingResult();

                    // replace symbol = formula name
                    if (lastFormula.isTypeOfFormulaSetting()) {
                        let leftItem = lastFormula.formulaSetting.leftItem;
                        let rightItem = lastFormula.formulaSetting.rightItem;
                        if (leftItem.settingMethod == 0) {
                            let symbolVal = this.getSymbolById(leftItem.formulaItemId);
                            settingResult = _.replace(settingResult, symbolVal,
                                this.getFormulaNameById(leftItem.formulaItemId));
                        }
                        if (rightItem.settingMethod == 0) {
                            let symbolVal = this.getSymbolById(rightItem.formulaItemId);
                            settingResult = _.replace(settingResult, symbolVal,
                                this.getFormulaNameById(rightItem.formulaItemId));
                        }
                    }

                    this.applyFormula(settingResult);
                }
            }

            /**
             * Get selectable formulas for selected formula for screen D
             * @param orderNo: the order of selected formula
             */
            private getSelectableFormulas(orderNo: number): Array<FormulaDto> {
                let self = this;
                let filtered = _.filter(self.calcFormulas(), item => item.orderNo() < orderNo);
                return _.map(filtered, item => item.toDto());
            }

            /**
             * add formula at order
             * @param order: number
             */
            private addFormulaAtOrder(order: number): void {
                let self = this;

                // If this is the first formula ever added
                if (!self.isFormulaSet()) {
                    order = 1;

                    OptionalItem.selectedFormulas([]);
                }

                let f = new Formula();

                // bind function
                f.reCheckAll = self.reCheckAll.bind(self);
                f.getSymbolById = self.getSymbolById.bind(self);
                f.setApplyFormula = self.setApplyFormula.bind(self);
                f.getSelectableFormulas = self.getSelectableFormulas.bind(self);
                f.getOptItemNoAbove = self.getOptItemNoAbove.bind(self);

                // Set order
                f.orderNo(order);

                // Set symbol
                f.symbolValue = 'A';
                let lastSymbol = self.getLastSymbol();
                if (lastSymbol) {
                    f.symbolValue = FormulaSorter.getNextSymbolOf(self.getLastSymbol());
                }

                // set optional item no
                f.optionalItemNo = self.optionalItemNo();

                // add new formula
                self.calcFormulas.push(f);

                // sort by orderNo
                self.sortListFormula();

            }

            /**
             * Check / unchecked the check all Checkbox.
             */
            private reCheckAll(): void {
                let self = this;

                // is checked from child flag.
                self.isCheckedFromChild = true;

                if (self.isAllFormulaChecked() == true) {
                    self.checkedAllFormula(true);
                } else {
                    self.checkedAllFormula(false);
                    self.isCheckedFromChild = false;
                }

            }

            /**
             * Check whether all formulas are checked
             */
            private isAllFormulaChecked(): boolean {
                let self = this;
                let checked = true;

                _.each(self.calcFormulas(), item => {
                    if (item.selected() == false) {
                        checked = false;
                    }
                });

                return checked;
            }

            /**
             * Check whether all formulas are unchecked
             */
            private isAllFormulaUnChecked(): boolean {
                let self = this;
                let unchecked = true;

                _.each(self.calcFormulas(), item => {
                    if (item.selected() == true) {
                        unchecked = false;
                    }
                });

                return unchecked;
            }

            /**
             * Update order of all formula after selected order
             */
            private updateOrderAfter(orderNo: number): void {
                let self = this;
                self.updateAllFormulaOrder(orderNo);
                self.updateSelectedFormulaOrder(orderNo);
            }

            /**
             * Update order of selected formulas
             */
            private updateSelectedFormulaOrder(orderNo: number): void {
                let self = this;
                let mapped = OptionalItem.selectedFormulas()
                    .map(item => {
                        if (item > orderNo) {
                            item += 1;
                        }
                        return item;
                    });
                OptionalItem.selectedFormulas(mapped);
            }

            /**
             * Update order of all formulas
             */
            private updateAllFormulaOrder(orderNo: number): void {
                let self = this;
                self.calcFormulas()
                    .filter(item => item.orderNo() > orderNo)
                    .forEach(item => item.orderNo(item.orderNo() + 1));
            }

            /**
             * Add formula below
             */
            public addFormulaBelow(): void {
                let self = this;

                // check before add
                // if zz is used
                // or list formula has at least 1 item and no formula checked
                // => show error message and return
                if (self.isFormulaSet() && self.canNotAddFormula()) {
                    return;
                }

                let belowOrder = self.selectedFormulaBelow;

                // update order of below items.
                self.updateOrderAfter(belowOrder - 1);

                // add new formula
                self.addFormulaAtOrder(belowOrder);

                // recheck the checkAll checkbox
                self.reCheckAll();

            }

            /**
             * Check whether a formula can be added 
             */
            private canNotAddFormula(): boolean {
                let self = this;

                // symbol has reached ZZ or no formula selected
                if (!self.hasSelectedFormula() || self.hasReachedZZ()) {
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_508' });
                    return true;
                }

                // Optional item's maximum number of formula is 50
                if (self.calcFormulas().length >= 50) {
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_762' });
                    return true;
                }

                // can add formula.
                return false;
            }

            /**
             * Confirm whether a calculation formula can be added (Check whether ZZ is used in the symbol)
             */
            private hasReachedZZ(): boolean {
                let self = this;
                return self.getLastSymbol() == 'ZZ';
            }

            /**
             * Check if one or more calculation expressions are checked
             */
            public hasSelectedFormula(): boolean {
                let self = this;
                return !nts.uk.util.isNullOrEmpty(OptionalItem.selectedFormulas());
            }

            /**
             * Check whether the symbol of the line to be deleted is included in the settings of other lines
             */
            private isInUse(): boolean {
                let self = this;

                // Find in list formulas the item that used this formula
                return _.find(OptionalItem.selectedFormulas(), selectedOrder => {
                    let id = self.getFormulaIdOf(selectedOrder);
                    return _.find(self.calcFormulas(), formula => formula.hasUsed(id)) != undefined;
                }) != undefined;
            }

            /**
             * Get formula id by order number
             * @param orderNo: the order number
             */
            private getFormulaIdOf(orderNo: number): string {
                let self = this;
                let found = _.find(self.calcFormulas(), formula => formula.orderNo() === orderNo);
                return found.formulaId;
            }

            /**
             * Sort list formula by order number
             */
            private sortListFormula(): void {
                let self = this;
                // sort by orderNo
                let sortedList = _.sortBy(self.calcFormulas(), item => item.orderNo());
                self.calcFormulas(sortedList);
            }

            /**
             * Add formula below
             */
            public removeFormula(): void {
                let self = this;

                // Check before remove
                if (!self.hasSelectedFormula()) {
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_508' });
                    return;
                }
                if (self.isInUse()) {
                    nts.uk.ui.dialog.confirm({ messageId: 'Msg_113' }).ifYes(() => {
                        // Remove selected formulas.
                        self.removeSelectedFormulas();

                        // reset formula order
                        self.resetFormulaOrder();
                    }).ifNo(() => {
                        return;
                    });
                } else {
                    // Remove selected formulas.
                    self.removeSelectedFormulas();

                    // reset formula order
                    self.resetFormulaOrder();
                }

            }

            /**
             * reset formula order.
             */
            private resetFormulaOrder(): void {
                let self = this;
                let index = 1;
                _.each(self.calcFormulas(), item => {
                    item.orderNo(index);
                    index++;
                });
            }

            /**
             * check if at least one formula exist 
             */
            public isFormulaSet(): boolean {
                return !nts.uk.util.isNullOrEmpty(this.calcFormulas()) && this.calcFormulas().length > 0
            }

            /**
             * Open dialog B
             */
            public openDialogB(): void {
                let self = this;
                nts.uk.ui.windows.setShared('paramForB', self.optionalItemNo());
                nts.uk.ui.windows.sub.modal('/view/kmk/002/b/index.xhtml');
            }

            /**
             * Convert view model to dto
             */
            public toDto(): OptionalItemDto {
                let self = this;
                let dto: OptionalItemDto = <OptionalItemDto>{};

                if (self.usageAtr() == 0) {
                    // get original data from stash
                    dto = jQuery.extend(true, {}, self.optionalItemDtoStash);

                    // set updated name & useAtr
                    dto.usageAtr = self.usageAtr();
                    dto.optionalItemName = self.optionalItemName();

                    // return dto
                    return dto;
                }

                // get current value of view model
                dto.optionalItemNo = self.optionalItemNo();
                dto.optionalItemName = self.optionalItemName();
                dto.optionalItemAtr = self.optionalItemAtr();
                dto.usageAtr = self.usageAtr();
                dto.empConditionAtr = self.empConditionAtr();
                dto.performanceAtr = self.performanceAtr();
                dto.calcResultRange = self.calcResultRange.toDto(self.optionalItemDtoStash.calcResultRange);
                dto.unit = self.enableUnit() ? self.unit() : self.optionalItemDtoStash.unit;
                dto.formulas = self.calcFormulas().map(item => item.toDto());

                return dto;
            }

            /**
             * Convert dto to view model
             */
            public fromDto(dto: OptionalItemDto): void {
                let self = this;

                // save data to stash
                self.optionalItemDtoStash = jQuery.extend(true, {}, dto);

                // bind to view model
                self.optionalItemNo(dto.optionalItemNo);
                self.optionalItemName(dto.optionalItemName);
                self.optionalItemAtr(dto.optionalItemAtr);
                self.usageAtr(dto.usageAtr);
                self.empConditionAtr(dto.empConditionAtr);
                self.performanceAtr(dto.performanceAtr);
                self.unit(dto.unit);
                self.calcResultRange.fromDto(dto.calcResultRange);

                // reset apply formula
                self.applyFormula('');

                // set list formula
                self.convertToListFormulaModel(dto.formulas);

                // set data source
                self.atrDataSource = Enums.ENUM_OPT_ITEM.itemAtr;

                // Stash
                self.optionalItemAtrStash = dto.optionalItemAtr;
                self.performanceAtrStash = dto.performanceAtr;

                // reset flag.
                self.hasChanged = false;
            }

            /**
             * Clear all input value.
             */
            public clearAll(): void {
                let self = this;
                self.optionalItemNo('');
                self.optionalItemName('');
                self.optionalItemAtr(0);
                self.usageAtr(0);
                self.empConditionAtr(0);
                self.performanceAtr(0);
                self.calcResultRange.resetValue();
                self.calcFormulas([]);
            }

            /**
             * Convert list formula dto to view model
             */
            private convertToListFormulaModel(list: Array<FormulaDto>): void {
                let self = this;

                let mapped: Array<Formula> = list.map(item => {
                    let formula = new Formula();

                    // bind function
                    formula.reCheckAll = self.reCheckAll.bind(self);
                    formula.getSymbolById = self.getSymbolById.bind(self);
                    formula.setApplyFormula = self.setApplyFormula.bind(self);
                    formula.getSelectableFormulas = self.getSelectableFormulas.bind(self);
                    formula.getOptItemNoAbove = self.getOptItemNoAbove.bind(self);

                    // convert dto to viewmodel
                    formula.fromDto(item);

                    // force to mutate
                    formula.timeDailyUnit.valueHasMutated();
                    formula.timeMonthlyUnit.valueHasMutated();

                    return formula;
                });

                // clear current list formula
                self.calcFormulas([]);

                // set new mapped list formula.
                self.calcFormulas(mapped);

                // sort list formula by orderNo
                self.sortListFormula();

                // set formula setting result
                _.each(self.calcFormulas(), formula => {
                    if (formula.isTypeOfFormulaSetting()) {
                        formula.setFormulaSettingResult(formula.formulaSetting);
                    } else {
                        formula.setItemSelectionResult(formula.itemSelection);
                    }
                });

            }

            /**
             * Check selected optional attribute
             */
            public checkSelectedAtr(): void {
                let self = this;
                if (self.optionalItemAtr() == EnumAdaptor.valueOf('AMOUNT', Enums.ENUM_OPT_ITEM.itemAtr)) {
                    self.isTimeSelected(false);
                    self.isNumberSelected(false);
                    self.isAmountSelected(true);
                }
                if (self.optionalItemAtr() == EnumAdaptor.valueOf('NUMBER', Enums.ENUM_OPT_ITEM.itemAtr)) {
                    self.isTimeSelected(false);
                    self.isNumberSelected(true);
                    self.isAmountSelected(false);
                }
                if (self.optionalItemAtr() == EnumAdaptor.valueOf('TIME', Enums.ENUM_OPT_ITEM.itemAtr)) {
                    self.isTimeSelected(true);
                    self.isNumberSelected(false);
                    self.isAmountSelected(false);
                }
            }

            /**
             * Get last symbol value of list formula.
             */
            private getLastSymbol(): string {
                let self = this;

                // return null if has no formula
                if (!self.isFormulaSet()) {
                    return null;
                };

                // find the last symbol.
                let lastSymbol = 'A';
                self.calcFormulas().forEach(item => {
                    // Check two string's length first
                    // Because z > aa using localeCompare
                    if (item.symbolValue.length > lastSymbol.length) {
                        lastSymbol = item.symbolValue;
                        return;
                    }
                    if (item.symbolValue.length == lastSymbol.length) {
                        if (item.symbolValue.localeCompare(lastSymbol) > 0) {
                            lastSymbol = item.symbolValue;
                        }
                    }
                });
                return lastSymbol;
            }

            /**
             * Set selected formula below and above
             */
            private setSelectedFormulaBelowAndAbove(array: Array<number>): void {
                let self = this;

                let above = 999;
                let below = 1;

                array.forEach(order => {

                    // set below = bottom selected order + 1
                    if (order >= below) {
                        below = order + 1;
                    }

                    // set above = top selected
                    if (order <= above) {
                        above = order;
                    }
                });

                self.selectedFormulaAbove = above;
                self.selectedFormulaBelow = below;
            }

        }

        /**
         * The class CalculationResultRange
         */
        class CalculationResultRange {
            upperCheck: KnockoutObservable<boolean>;
            lowerCheck: KnockoutObservable<boolean>;
            numberUpper: KnockoutObservable<number>;
            numberLower: KnockoutObservable<number>;
            amountUpper: KnockoutObservable<number>;
            amountLower: KnockoutObservable<number>;
            timeUpper: KnockoutObservable<number>;
            timeLower: KnockoutObservable<number>;

            constructor() {
                this.upperCheck = ko.observable(false);
                this.lowerCheck = ko.observable(false);
                this.numberUpper = ko.observable(null);
                this.numberLower = ko.observable(null);
                this.amountUpper = ko.observable(null);
                this.amountLower = ko.observable(null);
                this.timeUpper = ko.observable(null);
                this.timeLower = ko.observable(null);
            }

            /**
             * Check if limit range is set.
             */
            public isSet(): boolean {
                let self = this;
                return self.upperCheck()
                    || self.lowerCheck()
                    || !!self.numberUpper()
                    || !!self.numberLower()
                    || !!self.amountUpper()
                    || !!self.amountLower()
                    || !!self.timeUpper()
                    || !!self.timeLower();
            }

            /**
             * Validate all result range input
             */
            public validateInput(): void {
                let self = this;
                if (self.upperCheck()) {
                    $('#inp-upper-amount').ntsEditor('validate');
                    $('#inp-upper-number').ntsEditor('validate');
                    $('#inp-upper-time').ntsEditor('validate');
                }
                if (self.lowerCheck()) {
                    $('#inp-lower-amount').ntsEditor('validate');
                    $('#inp-lower-number').ntsEditor('validate');
                    $('#inp-lower-time').ntsEditor('validate');
                }
            }

            /**
             * Reset to default value
             */
            public resetValue(): void {
                let self = this;
                self.upperCheck(false);
                self.lowerCheck(false);
                self.numberUpper(null);
                self.numberLower(null);
                self.amountUpper(null);
                self.amountLower(null);
                self.timeUpper(null);
                self.timeLower(null);

                // clear error
                self.clearError();
            }

            /**
             * Clear input error
             */
            private clearError(): void {
                $('#inp-upper-amount').ntsError('clear');
                $('#inp-upper-number').ntsError('clear');
                $('#inp-upper-time').ntsError('clear');
                $('#inp-lower-amount').ntsError('clear');
                $('#inp-lower-number').ntsError('clear');
                $('#inp-lower-time').ntsError('clear');
            }

            /**
             * Convert dto to view model
             */
            public fromDto(dto: CalcResultRangeDto): void {
                let self = this;
                self.upperCheck(dto.upperCheck);
                self.lowerCheck(dto.lowerCheck);
                self.numberUpper(dto.numberUpper);
                self.numberLower(dto.numberLower);
                self.timeUpper(dto.timeUpper);
                self.timeLower(dto.timeLower);
                self.amountUpper(dto.amountUpper);
                self.amountLower(dto.amountLower);
            }

            /**
             * Convert view model to dto
             */
            public toDto(calcResultRangeStash: CalcResultRangeDto): CalcResultRangeDto {
                let self = this;
                let dto = <CalcResultRangeDto>{};
                dto.upperCheck = self.upperCheck();
                dto.lowerCheck = self.lowerCheck();

                if (self.upperCheck()) {
                    dto.numberUpper = self.numberUpper();
                    dto.amountUpper = self.amountUpper();
                    dto.timeUpper = self.timeUpper();
                } else {
                    // get stored data from stash
                    dto.numberUpper = calcResultRangeStash.numberUpper;
                    dto.amountUpper = calcResultRangeStash.amountUpper;
                    dto.timeUpper = calcResultRangeStash.timeUpper;
                }

                if (self.lowerCheck()) {
                    dto.numberLower = self.numberLower();
                    dto.amountLower = self.amountLower();
                    dto.timeLower = self.timeLower();
                } else {
                    // get stored data from stash
                    dto.numberLower = calcResultRangeStash.numberLower;
                    dto.amountLower = calcResultRangeStash.amountLower;
                    dto.timeLower = calcResultRangeStash.timeLower;
                }
                return dto;
            }
        }

        /**
         * The class Optional Item Header
         */
        class OptionalItemHeader {
            columns: any;
            selectedCode: KnockoutObservable<string>;
            optionalItemHeaders: KnockoutObservableArray<OptionalItemHeaderDto>;
            optionalItem: OptionalItem;
            hasSelected: KnockoutObservable<boolean>;

            constructor() {
                let self = this;
                self.optionalItemHeaders = ko.observableArray([]);
                self.optionalItem = new OptionalItem();
                self.hasSelected = ko.observable(true);

                self.selectedCode = ko.observable('');
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KMK002_7'), key: 'itemNo', width: 40 },
                    { headerText: nts.uk.resource.getText('KMK002_8'), key: 'itemName', width: 100, formatter: _.escape },
                    {
                        headerText: nts.uk.resource.getText('KMK002_9'), key: 'performanceAtr', width: 75,
                        formatter: atr => {
                            if (atr == 0) {
                                return nts.uk.resource.getText("KMK002_23");
                            }
                            return nts.uk.resource.getText("KMK002_22")
                        }
                    },
                    {
                        headerText: nts.uk.resource.getText('KMK002_10'), key: 'usageAtr', width: 50,
                        formatter: used => {
                            if (used == 1) {
                                return '<div style="text-align: center;max-height: 18px;">'
                                    + '<i class="icon icon-78"></i></div>';
                            }
                            return '';
                        }
                    }
                ]);

            }

            /**
             * Initialization
             */
            public initialize(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // Select first item
                let itemNo = self.optionalItemHeaders()[0].itemNo;
                self.selectedCode(itemNo);

                self.loadOptionalItemDetail(itemNo).done(() => {

                    // init usageAtr subscribe.
                    self.optionalItem.usageAtr.subscribe(vl => {
                        if (vl === 1) {
                            self.optionalItem.isUsed(true);
                        } else {
                            self.optionalItem.isUsed(false);
                        }
                    });

                    // force to check enable/disable condition
                    self.optionalItem.usageAtr.valueHasMutated();

                    // init selected code subscribe
                    self.selectedCode.subscribe(itemNo => {
                        if (itemNo) {
                            self.hasSelected(true);
                            self.loadOptionalItemDetail(itemNo);
                            // clear error.
                            $('.nts-editor').ntsError('clear');
                        } else {
                            self.optionalItem.clearAll();
                            self.hasSelected(false);
                        }
                    });

                    // resolve
                    dfd.resolve();
                });

                return dfd.promise();
            }

            /**
             * Save optional item detail.
             */
            public saveOptionalItemDetail(): JQueryPromise<void> {
                let self = this;

                // Validate data.
                if (!self.isValidData()) {
                    return;
                }

                let dfd = $.Deferred<void>();

                // block ui.
                nts.uk.ui.block.invisible();

                let command = self.optionalItem.toDto();

                // call webservice to save optional item
                service.saveOptionalItem(command)
                    .done(() => {
                        // reload optional item list.
                        self.loadOptionalItemHeaders();

                        // show message save successful
                        nts.uk.ui.dialog.info({ messageId: 'Msg_15' });

                        dfd.resolve();
                    })
                    .fail(res => nts.uk.ui.dialog.bundledErrors(res))
                    .always(() => nts.uk.ui.block.clear()); // clear block ui.;

                return dfd.promise();
            }

            /**
             * Data validation
             */
            private isValidData(): boolean {
                let self = this;

                // validate input optional item name
                $('#inpName').ntsEditor('validate');

                // useAtr == not used
                // skip all check except input name
                if (self.optionalItem.usageAtr() == 0) {
                    // check has error.
                    if ($('.nts-editor').ntsError('hasError')) {
                        return false;
                    }
                    return true;
                }

                // validate required formulaName & required setting formula
                self.optionalItem.validateListFormula();

                // validate calculation result range input
                self.optionalItem.calcResultRange.validateInput();

                // check has error.
                if ($('.nts-editor').ntsError('hasError')) {
                    return false;
                }

                // validate list formula
                let invalidFormulas = self.optionalItem.findInvalidFormula();
                if (!nts.uk.util.isNullOrEmpty(invalidFormulas)) {

                    // set bundle errors
                    let messages = { Msg_111: [] };
                    _.each(invalidFormulas, formula => {
                        $('#settingResult' + formula.orderNo()).ntsError('set',
                            { messageId: "Msg_111", messageParams: [formula.orderNo()] });
                    });

                    return false;
                };

                return true;
            }

            /**
             * Load optional item headers
             */
            public loadOptionalItemHeaders(): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                // block ui
                nts.uk.ui.block.invisible();

                // get optional item headers
                service.findOptionalItemHeaders()
                    .done(res => {
                        self.optionalItemHeaders(res);
                        dfd.resolve();
                    })
                    .always(() => nts.uk.ui.block.clear()); // clear block ui.
                return dfd.promise();

            }

            /**
             * Load optional item detail.
             */
            private loadOptionalItemDetail(itemNo: string): JQueryPromise<void> {
                let self = this;
                let dfd = $.Deferred<void>();

                Formula.performanceAtr = self.optionalItem.performanceAtr(); // param for c screen

                // wait for selected event done then block ui.
                _.defer(() => nts.uk.ui.block.invisible());

                // get optional item detail
                service.findOptionalItemDetail(itemNo)
                    .done(res => {
                        // clear selected formula
                        OptionalItem.selectedFormulas([]);

                        // bind function
                        self.optionalItem.getOptItemNoAbove = self.getOptItemNoAbove.bind(self);

                        // convert dto to view model.
                        self.optionalItem.fromDto(res);

                        // focus optional item name input
                        $('#inpName').focus();

                        self.optionalItem.checkSelectedAtr();

                        dfd.resolve();

                    }).always(() => nts.uk.ui.block.clear()); // clear block ui.

                return dfd.promise();
            }

            /**
             * Get list optional item above of selected optional item.
             */
            private getOptItemNoAbove(): Array<string> {
                let self = this;
                let selectedNo = parseInt(self.selectedCode());
                return self.optionalItemHeaders()
                    .filter(item => parseInt(item.itemNo) < selectedNo)
                    .map(item => item.itemNo);
            }
        }

        /**
         * The class Formula
         */
        class Formula {
            formulaId: string;
            optionalItemNo: number;
            formulaName: KnockoutObservable<string>;
            formulaAtr: KnockoutObservable<number>;
            symbolValue: string;
            orderNo: KnockoutObservable<number>;
            settingResult: KnockoutObservable<string>;

            // param for c screen
            static performanceAtr: number;

            // Calculation setting
            calcAtr: KnockoutObservable<number>;
            formulaSetting: FormulaSettingDto;
            itemSelection: ItemSelectionDto;

            //Rounding
            timeMonthlyRounding: KnockoutObservable<number>;
            timeMonthlyUnit: KnockoutObservable<number>;
            timeDailyRounding: KnockoutObservable<number>;
            timeDailyUnit: KnockoutObservable<number>;
            numberMonthlyRounding: KnockoutObservable<number>;
            numberMonthlyUnit: KnockoutObservable<number>;
            numberDailyRounding: KnockoutObservable<number>;
            numberDailyUnit: KnockoutObservable<number>;
            amountMonthlyRounding: KnockoutObservable<number>;
            amountMonthlyUnit: KnockoutObservable<number>;
            amountDailyRounding: KnockoutObservable<number>;
            amountDailyUnit: KnockoutObservable<number>;

            // flags
            selected: KnockoutObservable<boolean>;
            isCheckFromParent = false;

            // function
            reCheckAll: () => void;
            getSymbolById: (id: string) => string;
            setApplyFormula: () => void;
            getOptItemNoAbove: () => Array<string>;
            getSelectableFormulas: (orderNo: number) => Array<FormulaDto>;

            // Enums datasource
            formulaAtrDs: EnumConstantDto[];
            calcAtrDs: EnumConstantDto[];
            numberUnitDs: EnumConstantDto[];
            numberRoundingDs: EnumConstantDto[];
            amountUnitDs: EnumConstantDto[];
            amountRoundingDs: EnumConstantDto[];
            timeUnitDs: EnumConstantDto[];
            timeRoundingDailyDs: KnockoutComputed<Array<EnumConstantDto>>;
            timeRoundingMonthlyDs: KnockoutComputed<Array<EnumConstantDto>>;
            timeRoundingFullDs: EnumConstantDto[];
            timeRoundingFilterdDs: EnumConstantDto[];

            // stash
            formulaAtrStash: number;
            calcAtrStash: number;
            timeMonthlyUnitStash: number;
            timeDailyUnitStash: number;

            constructor() {
                this.formulaId = nts.uk.util.randomId();
                this.optionalItemNo = '';
                this.formulaName = ko.observable('');
                this.formulaAtr = ko.observable(0);
                this.symbolValue = '';
                this.orderNo = ko.observable(null);
                this.selected = ko.observable(false);
                this.settingResult = ko.observable('');

                // Calculation setting.
                this.calcAtr = ko.observable(0);
                this.formulaSetting = this.getDefaultFormulaSetting();
                this.itemSelection = this.getDefaultItemSelection();

                // stash
                this.calcAtrStash = 0;
                this.formulaAtrStash = 0;
                this.timeMonthlyUnitStash = 1;
                this.timeDailyUnitStash = 1;

                // Rounding
                this.timeMonthlyRounding = ko.observable(0);
                this.timeMonthlyUnit = ko.observable(1);
                this.timeDailyRounding = ko.observable(0);
                this.timeDailyUnit = ko.observable(1);
                this.numberMonthlyRounding = ko.observable(0);
                this.numberMonthlyUnit = ko.observable(0);
                this.numberDailyRounding = ko.observable(0);
                this.numberDailyUnit = ko.observable(0);
                this.amountMonthlyRounding = ko.observable(0);
                this.amountMonthlyUnit = ko.observable(1);
                this.amountDailyRounding = ko.observable(0);
                this.amountDailyUnit = ko.observable(1);

                // initial data source
                this.initDatasource();

                // init subscribe
                this.initSubscribe();
            }

            /**
             * Initial data source
             */
            private initDatasource(): void {
                let self = this;
                self.formulaAtrDs = Enums.ENUM_OPT_ITEM.formulaAtr;
                self.calcAtrDs = Enums.ENUM_OPT_ITEM.calcAtr;
                self.timeUnitDs = Enums.ENUM_OPT_ITEM.timeRounding.unit;
                self.timeRoundingFullDs = Enums.ENUM_OPT_ITEM.timeRounding.rounding;
                self.timeRoundingFilterdDs = self.timeRoundingFullDs
                    .filter(item => item.fieldName != "ROUNDING_DOWN_OVER");
                self.amountUnitDs = Enums.ENUM_OPT_ITEM.amountRounding.unit;
                self.amountRoundingDs = Enums.ENUM_OPT_ITEM.amountRounding.rounding;
                self.numberUnitDs = Enums.ENUM_OPT_ITEM.numberRounding.unit;
                self.numberRoundingDs = Enums.ENUM_OPT_ITEM.numberRounding.rounding;

                self.timeRoundingMonthlyDs = ko.computed(() => {
                    if (self.isTimeUnit15or30(self.timeMonthlyUnit())) {
                        // save new value to stash
                        self.timeMonthlyUnitStash = self.timeMonthlyUnit();

                        // show full data source
                        return self.timeRoundingFullDs;

                    } else {
                        // save new value to stash
                        self.timeMonthlyUnitStash = self.timeMonthlyUnit();

                        // Remove item ROUNDING_DOWN_OVER(2, "未満切捨、以上切上", "Enum_Rounding_Down_Over")
                        // from data source list
                        return self.timeRoundingFilterdDs;

                    }
                });

                self.timeRoundingDailyDs = ko.computed(() => {
                    if (self.isTimeUnit15or30(self.timeDailyUnit())) {
                        // save new value to stash
                        self.timeDailyUnitStash = self.timeDailyUnit();

                        // show full data source
                        return self.timeRoundingFullDs;

                    } else {
                        // save new value to stash
                        self.timeDailyUnitStash = self.timeDailyUnit();

                        // Remove item ROUNDING_DOWN_OVER(2, "未満切捨、以上切上", "Enum_Rounding_Down_Over")
                        // from data source list
                        return self.timeRoundingFilterdDs;

                    }
                });
            }

            /**
             * Initial subscribe
             */
            private initSubscribe(): void {
                let self = this;

                // Event on selected (checkbox)
                self.selected.subscribe(vl => {

                    if (self.isCheckFromParent == false) {
                        // check if all formula checked
                        self.reCheckAll();

                    }

                    // add to selected formulas if checked
                    if (vl === true) {
                        OptionalItem.selectedFormulas.push(self.orderNo());
                    }

                    // remove from selected formulas if unchecked
                    if (vl === false) {
                        OptionalItem.selectedFormulas.remove(self.orderNo());
                    }

                    // reset flag
                    self.isCheckFromParent = false;

                });

                // event on formula name changed
                self.formulaName.subscribe(vl => {
                    self.setApplyFormula();
                });

                // event on set formula setting or item selection.
                self.settingResult.subscribe(vl => {
                    // set apply formula
                    self.setApplyFormula();
                });

                // Event on formulaAtr value changed
                self.formulaAtr.subscribe(value => {

                    // if new value == value in stash then do nothing
                    if (value == self.formulaAtrStash) {
                        return;
                    }

                    // Check whether the formula has setting or not
                    if (self.hasSetting()) {
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_192' }).ifYes(() => {
                            // clear the setting
                            self.clearFormulaSetting();

                            // save new value to stash
                            self.formulaAtrStash = value;
                        }).ifNo(() => {
                            // get old value from stash
                            self.formulaAtr(self.formulaAtrStash);
                        });
                    } else {
                        // save new value to stash
                        self.formulaAtrStash = value;
                    }
                });

                // Event on calcAtr value changed
                self.calcAtr.subscribe(value => {

                    // if new value == value in stash then do nothing
                    if (value == self.calcAtrStash) {
                        return;
                    }

                    // Check whether the formula has setting or not
                    if (self.hasSetting()) {
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_126' }).ifYes(() => {
                            // clear the setting
                            self.clearFormulaSetting();

                            // save new value to stash
                            self.calcAtrStash = value;
                        }).ifNo(() => {
                            // get old value from stash
                            self.calcAtr(self.calcAtrStash);
                        });
                    } else {
                        // save new value to stash
                        self.calcAtrStash = value;
                    }
                });

            }

            /**
             * Check whether calculation attribute is formula setting or not.
             */
            public isTypeOfFormulaSetting(): boolean {
                let self = this;
                return self.calcAtr() == EnumAdaptor.valueOf('FORMULA_SETTING', Enums.ENUM_OPT_ITEM.calcAtr);
            }

            /**
             * Check whether the setting method is item selection or not
             */
            public isSettingMethodOfItemSelection(settingItem: SettingItemDto): boolean {
                let self = this;
                return settingItem.settingMethod == EnumAdaptor.valueOf('ITEM_SELECTION',
                    Enums.ENUM_OPT_ITEM.settingMethod);
            }

            /**
             * Check the selected time rounding unit value.
             * Return true if the value == 15 or 30.
             * @param vl: selected time rounding unit
             */
            private isTimeUnit15or30(vl: number): boolean {
                return vl === EnumAdaptor.valueOf('ROUNDING_TIME_15MIN', Enums.ENUM_OPT_ITEM.timeRounding.unit)
                    || vl === EnumAdaptor.valueOf('ROUNDING_TIME_30MIN', Enums.ENUM_OPT_ITEM.timeRounding.unit);
            }

            /**
             * Clear formula setting
             */
            private clearFormulaSetting(): void {
                let self = this;
                self.formulaSetting = self.getDefaultFormulaSetting();
                self.itemSelection = self.getDefaultItemSelection();
                self.settingResult('');
            }

            /**
             * Check whether the the formula of the @orderNo has been used in this formula
             * @param formulaId: the formula id
             */
            public hasUsed(formulaId: string): boolean {
                let self = this;
                return self.formulaSetting.leftItem.formulaItemId == formulaId
                    || self.formulaSetting.rightItem.formulaItemId == formulaId;
            }

            /**
             * get default formula setting.
             */
            private getDefaultFormulaSetting(): FormulaSettingDto {
                let self = this;
                let data = <FormulaSettingDto>{};
                data.minusSegment = 0;
                data.operator = 0;
                data.leftItem = {
                    settingMethod: 0,
                    dispOrder: 1,
                    inputValue: null,
                    formulaItemId: ''
                }
                data.rightItem = {
                    settingMethod: 0,
                    dispOrder: 2,
                    inputValue: null,
                    formulaItemId: ''
                }
                return data;
            }

            /**
             * get default item selection
             */
            private getDefaultItemSelection(): ItemSelectionDto {
                let data = <ItemSelectionDto>{};
                data.minusSegment = 0;
                data.attendanceItems = [];
                return data;
            }

            /**
             * Check whether formula has setting
             */
            private hasSetting(): boolean {
                let self = this;
                return !nts.uk.util.isNullOrEmpty(self.settingResult());
            }

            /**
             * set to selected
             */
            public check(): void {
                let self = this;
                self.selected(true);
            }

            /**
             * set to unselected
             */
            public uncheck(): void {
                let self = this;
                self.selected(false);
            }

            /**
             * Check calculation attribute is formula setting or item selection 
             */
            public isFormulaSettingSelected(): boolean {
                let self = this;
                return self.calcAtr() == 1;
            }

            /**
             * Check whether time atr is selected
             */
            public isTimeSelected(): boolean {
                let self = this;
                return self.formulaAtr() == EnumAdaptor.valueOf('TIME', Enums.ENUM_OPT_ITEM.formulaAtr);
            }

            /**
             * Check whether number atr is selected
             */
            public isNumberSelected(): boolean {
                let self = this;
                return self.formulaAtr() == EnumAdaptor.valueOf('NUMBER', Enums.ENUM_OPT_ITEM.formulaAtr);
            }

            /**
             * Check whether amount atr is selected
             */
            public isAmountSelected(): boolean {
                let self = this;
                return self.formulaAtr() == EnumAdaptor.valueOf('AMOUNT', Enums.ENUM_OPT_ITEM.formulaAtr);
            }

            /**
            * Open dialog C: Item selection
            */
            public openDialogC(): void {
                let self = this;

                // Set param
                let dto = self.toDto();
                let param = <ParamToC>{};
                param.formulaId = dto.formulaId;
                param.performanceAtr = Formula.performanceAtr;
                param.formulaAtr = dto.formulaAtr;
                param.formulaAtrName = EnumAdaptor.localizedNameOf(dto.formulaAtr, Enums.ENUM_OPT_ITEM.formulaAtr);
                param.formulaName = dto.formulaName;
                param.itemSelection = dto.itemSelection;
                param.selectableOptItemNos = self.getOptItemNoAbove();
                nts.uk.ui.windows.setShared('paramToC', param);

                // Open dialog.
                nts.uk.ui.windows.sub.modal('/view/kmk/002/c/index.xhtml').onClosed(() => {
                    let dto: ItemSelectionDto = nts.uk.ui.windows.getShared('returnFromC');
                    if (dto) {
                        // set itemSelection setting
                        self.itemSelection = dto;

                        // set result display
                        self.setItemSelectionResult(dto);
                    }
                });
            }

            /**
             * Open dialog D: Formula setting
             */
            public openDialogD(): void {
                let self = this;

                // set pram.
                let dto = self.toDto();
                let param = <ParamToD>{};
                param.formulaId = dto.formulaId;
                param.formulaAtr = EnumAdaptor.localizedNameOf(dto.formulaAtr, Enums.ENUM_OPT_ITEM.formulaAtr);
                param.formulaName = dto.formulaName;
                param.formulaSetting = dto.formulaSetting;
                param.selectableFormulas = self.getSelectableFormulas(self.orderNo());
                param.operatorDatasource = Enums.ENUM_OPT_ITEM.operatorAtr;

                nts.uk.ui.windows.setShared('paramToD', param);

                // open dialog D.
                nts.uk.ui.windows.sub.modal('/view/kmk/002/d/index.xhtml').onClosed(() => {
                    let dto: FormulaSettingDto = nts.uk.ui.windows.getShared('returnFromD');
                    if (dto) {
                        // set formula setting.
                        self.formulaSetting = dto;

                        // set result display
                        self.setFormulaSettingResult(dto);
                    }
                });
            }

            /**
             * Display the setting of formula setting
             */
            public setFormulaSettingResult(dto: FormulaSettingDto): void {
                let self = this;
                let result;
                let leftItem;
                let rightItem;

                // get item selection enum value.
                let operator: string = EnumAdaptor.localizedNameOf(dto.operator, Enums.ENUM_OPT_ITEM.operatorAtr);

                // set left item
                if (self.isSettingMethodOfItemSelection(dto.leftItem)) {
                    leftItem = self.getSymbolById(dto.leftItem.formulaItemId);
                } else {
                    leftItem = dto.leftItem.inputValue;
                }

                // set right item
                if (self.isSettingMethodOfItemSelection(dto.rightItem)) {
                    rightItem = self.getSymbolById(dto.rightItem.formulaItemId);
                } else {
                    rightItem = dto.rightItem.inputValue;
                }

                // set result
                result = leftItem + ' ' + operator + ' ' + rightItem;
                self.settingResult(result);

                // clear error
                $('#settingResult' + self.orderNo()).ntsEditor('validate');
            }

            /**
             * Display the setting of formula setting
             */
            public setItemSelectionResult(dto: ItemSelectionDto): void {
                let self = this;

                // set result display
                let result = '';
                dto.attendanceItems.forEach(item => {
                    result += item.operatorText + ' ' + item.attendanceItemName + ' ';
                });

                // remove first '+'
                if (_.startsWith(result, '+')) {
                    result = result.substring(2);
                }

                // set result
                self.settingResult(result);

                // clear error
                $('#settingResult' + self.orderNo()).ntsEditor('validate');
            }

            /**
             * Convert viewmodel to dto
             */
            public toDto(): FormulaDto {
                let self = this;
                let dto: FormulaDto = <FormulaDto>{};

                dto.formulaId = self.formulaId;
                dto.optionalItemNo = self.optionalItemNo;
                dto.orderNo = self.orderNo();
                dto.formulaName = self.formulaName();
                dto.formulaAtr = self.formulaAtr();
                dto.symbolValue = self.symbolValue;
                dto.calcAtr = self.calcAtr();
                // clone object
                dto.formulaSetting = jQuery.extend(true, {}, self.formulaSetting);
                dto.itemSelection = jQuery.extend(true, {}, self.itemSelection);

                // Rounding
                let monthly = <RoundingDto>{};
                monthly.numberRounding = self.numberMonthlyRounding();
                monthly.numberUnit = self.numberMonthlyUnit();
                monthly.timeRounding = self.timeMonthlyRounding();
                monthly.timeUnit = self.timeMonthlyUnit();
                monthly.amountRounding = self.amountMonthlyRounding();
                monthly.amountUnit = self.amountMonthlyUnit();

                let daily = <RoundingDto>{};
                daily.numberRounding = self.numberDailyRounding();
                daily.numberUnit = self.numberDailyUnit();
                daily.timeRounding = self.timeDailyRounding();
                daily.timeUnit = self.timeDailyUnit();
                daily.amountRounding = self.amountDailyRounding();
                daily.amountUnit = self.amountDailyUnit();

                dto.monthlyRounding = monthly;
                dto.dailyRounding = daily;

                return dto;
            }

            /**
             * Convert dto to viewmodel
             */
            public fromDto(dto: FormulaDto): void {
                let self = this;
                self.formulaId = dto.formulaId;
                self.optionalItemNo = dto.optionalItemNo;
                self.formulaName(dto.formulaName);
                self.formulaAtr(dto.formulaAtr);
                self.symbolValue = dto.symbolValue;
                self.orderNo(dto.orderNo);

                // Calc setting
                self.calcAtr(dto.calcAtr);
                if (dto.formulaSetting) {
                    self.formulaSetting = dto.formulaSetting;
                }
                if (dto.itemSelection) {
                    self.itemSelection = dto.itemSelection;
                }

                // Daily rounding
                if (dto.dailyRounding) {
                    self.numberDailyRounding(dto.dailyRounding.numberRounding);
                    self.numberDailyUnit(dto.dailyRounding.numberUnit);
                    self.amountDailyRounding(dto.dailyRounding.amountRounding);
                    self.amountDailyUnit(dto.dailyRounding.amountUnit);
                    self.timeDailyRounding(dto.dailyRounding.timeRounding);
                    self.timeDailyUnit(dto.dailyRounding.timeUnit);
                }

                // Monthly rounding
                self.numberMonthlyRounding(dto.monthlyRounding.numberRounding);
                self.numberMonthlyUnit(dto.monthlyRounding.numberUnit);
                self.amountMonthlyRounding(dto.monthlyRounding.amountRounding);
                self.amountMonthlyUnit(dto.monthlyRounding.amountUnit);
                self.timeMonthlyRounding(dto.monthlyRounding.timeRounding);
                self.timeMonthlyUnit(dto.monthlyRounding.timeUnit);

                // save to stash
                self.formulaAtrStash = dto.formulaAtr;
                self.calcAtrStash = dto.calcAtr;
                self.timeMonthlyUnitStash = dto.monthlyRounding.timeUnit;
                self.timeDailyUnitStash = dto.dailyRounding.timeUnit;

            }
        }

        class Enums {
            static ENUM_OPT_ITEM: OptItemEnumDto;
        }

        class FormulaSorter {
            public static SORTER: Array<Sorter> = FormulaSorter.initSorter();

            /**
             * initial sorter
             */
            private static initSorter(): Array<Sorter> {
                let arr = new Array<Sorter>();
                let order = 1;

                for (let i = 65; i <= 90; i++) {
                    arr.push({ order: order, value: String.fromCharCode(i) });
                    order++;
                }

                for (let i = 65; i <= 90; i++) {
                    for (let j = 65; j <= 90; j++) {
                        arr.push({ order: order, value: String.fromCharCode(i) + String.fromCharCode(j) });
                        order++;
                    }
                }
                return arr;
            }

            /**
             * get symbol of selected order
             * @param order: the order of symbol
             */
            public static getValueOf(order: number): string {
                let item = _.find(FormulaSorter.SORTER, item => item.order == order);
                if (item) {
                    return item.value;
                }
                return 'NOT FOUND';
            }

            /**
             * get order of selected symbol
             * @param value: the selected symbol
             */
            public static getOrderOf(value: string): number {
                let item = _.find(FormulaSorter.SORTER, item => item.value == value);
                if (item) {
                    return item.order;
                }
                // Error.
                return null;
            }

            /**
             * get next symbol of current selected symbol
             * @param symbolValue: current selected symbol
             */
            public static getNextSymbolOf(symbolValue: string): string {
                let nextOd = FormulaSorter.getOrderOf(symbolValue) + 1;
                return FormulaSorter.getValueOf(nextOd);
            }
        }
        interface Sorter {
            order: number;
            value: string;
        }
        export interface ParamToC {
            formulaId: string;
            performanceAtr: number;
            formulaAtr: number;
            formulaAtrName: string;
            formulaName: string;
            itemSelection: ItemSelectionDto;
            selectableOptItemNos: Array<string>;
        }
        export interface ParamToD {
            formulaId: string;
            formulaName: string;
            formulaAtr: string;
            formulaSetting: FormulaSettingDto;
            operatorDatasource: Array<EnumConstantDto>;
            selectableFormulas: Array<FormulaDto>;
        }
    }
}