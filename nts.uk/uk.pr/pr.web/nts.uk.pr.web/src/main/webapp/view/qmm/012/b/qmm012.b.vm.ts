module nts.uk.pr.view.qmm012.b {

    import model = qmm012.share.model;
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import block = nts.uk.ui.block;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;

    export module viewModel {
        export class ScreenModel {
            
            // category comboBox
            categoryList: KnockoutObservableArray<model.ItemModel>;
            selectedCategory: KnockoutObservable<string>;
            
            // statement gridList
            statementItemDataList: KnockoutObservableArray<IStatementItemData> = ko.observableArray([]);
            statementItemDataSelected: KnockoutObservable<StatementItemData> = ko.observable(null);
            
            // define gridColumns
            gridColumns: any;
            
            constructor() {
                let self = this;
                
                // category comboBox
                self.categoryList = ko.observableArray([
                    new model.ItemModel(model.CategoryAtr.PAYMENT_ITEM.toString(), getText('QMM012_3')),
                    new model.ItemModel(model.CategoryAtr.DEDUCTION_ITEM.toString(), getText('QMM012_4')),
                    new model.ItemModel(model.CategoryAtr.ATTEND_ITEM.toString(), getText('QMM012_5')),
                    new model.ItemModel(model.CategoryAtr.REPORT_ITEM.toString(), getText('QMM012_6')),
                    new model.ItemModel(model.CategoryAtr.OTHER_ITEM.toString(), getText('QMM012_7'))
                ]);
                self.selectedCategory = ko.observable(model.CategoryAtr.PAYMENT_ITEM.toString());
                
                for(let i = 0; i < 100; i++) {
                    self.statementItemDataList.push({cid: i.toString(), 
                                                    salaryItemId: i.toString(),
                                                    statementItem: {categoryAtr: i,
                                                                    itemNameCd: i,
                                                                    defaultAtr: i,
                                                                    valueAtr: i,
                                                                    deprecatedAtr: i,
                                                                    socialInsuaEditableAtr: i,
                                                                    intergrateCd: i
                                                                }, 
                                                    statementItemName: {
                                                                    name: i.toString(),
                                                                    shortName: i.toString(),
                                                                    otherLanguageName: i.toString(),
                                                                    englishName: i.toString()
                                                                },
                                                    paymentItemSet: null,
                                                    statementItemDisplaySet: null,
                                                    itemRangeSet: null,
                                                    validityPeriodAndCycleSet: null,
                                                    breakdownItemSet: null,
                                                    categoryAtr: i,
                                                    itemNameCd: i.toString(),
                                                    name: i.toString(),
                                                    deprecatedAtr: i,
                                                    deductionItemSet: null,
                                                    timeItemSet: null
                                                    }); 
                }
                self.statementItemDataSelected(new StatementItemData(self.statementItemDataList()[0], self));
                
                self.gridColumns = [
                                        { headerText: '', key: 'salaryItemId', width: 0, formatter: _.escape, hidden: true },
                                        { headerText: getText('QMM012_27'), key: 'categoryAtr', width: 80 , formatter: _.escape },
                                        { headerText: getText('QMM012_32'), key: 'itemNameCd', width: 60, formatter: _.escape },
                                        { headerText: getText('QMM012_33'), key: 'name', width: 200, formatter: _.escape },
                                        { headerText: getText('QMM012_34'), key: 'deprecatedAtr', width: 50, formatter: _.escape }
                                   ];

            }//end constructor
            
            startPage(): JQueryPromise<any> {
                let self = this;
                let deferred = $.Deferred();
                deferred.resolve();
                return deferred.promise();
            }
            
            public create(): void {
                
            }
            
            public register(): void {
                
            }
            
            public copy(): void {
                
            }
            
            public deleteItem(): void {
                
            }
            
            public outputExcel(): void {
                
            }
            
            public modifyLog(): void {
                
            }
            
            public registerPrintingName(): void {
                
            }
            
        }
        
        class StatementItemData {
            cid: string;
            salaryItemId: KnockoutObservable<string>;
            statementItem: KnockoutObservable<StatementItem>;
            statementItemName: KnockoutObservable<StatementItemName>;
            paymentItemSet: KnockoutObservable<PaymentItemSet>;
            statementItemDisplaySet: KnockoutObservable<StatementItemDisplaySet>;
            itemRangeSet: KnockoutObservable<ItemRangeSet>;
            deductionItemSet: KnockoutObservable<DeductionItemSet>;
            
            constructor(data: IStatementItemData, screenModel: ScreenModel) {
                let self = this;
                
                if (data) {
                    self.cid = data.cid;
                    self.salaryItemId = ko.observable(data.salaryItemId);
                    self.statementItem = ko.observable(new StatementItem(data.statementItem));
                    self.statementItemName = ko.observable(new StatementItemName(data.statementItemName));
                    self.paymentItemSet = ko.observable(new PaymentItemSet(data.paymentItemSet));
                    self.statementItemDisplaySet = ko.observable(new StatementItemDisplaySet(data.statementItemDisplaySet));
                    self.itemRangeSet = ko.observable(new ItemRangeSet(data.itemRangeSet));
                    self.deductionItemSet = ko.observable(new DeductionItemSet(data.deductionItemSet));
                } else {
                    //TODO
                    console.log("Xử lý đi em");
                }
                
                self.salaryItemId.subscribe(x => {
                    if(x) {
                        data = _.filter(screenModel.statementItemDataList(), function(o) {
                            return x == o.salaryItemId;
                        })[0];
                        
                        self.statementItem(new StatementItem(data.statementItem));
                        self.statementItemName(new StatementItemName(data.statementItemName));
                        self.paymentItemSet(new PaymentItemSet(data.paymentItemSet));
                        self.statementItemDisplaySet(new StatementItemDisplaySet(data.statementItemDisplaySet));
                        self.itemRangeSet(new ItemRangeSet(data.itemRangeSet));
                        self.deductionItemSet(new DeductionItemSet(data.deductionItemSet));
                    } else {
                        //TODO
                        console.log("Xử lý đi em");
                    }
                });
            }
        }
        
        class StatementItem {
            categoryAtr: number;
            categoryName: KnockoutObservable<string>;
            itemNameCd: KnockoutObservable<number>;
            defaultAtr: KnockoutObservable<number>;
            valueAtr: KnockoutObservable<number>;
            deprecatedAtr: KnockoutObservable<number>;
            socialInsuaEditableAtr: KnockoutObservable<number>;
            intergrateCd: KnockoutObservable<number>;
            
            constructor(data: IStatementItem) {
                let self = this;
                
                if (data) {
                    self.categoryAtr = data.categoryAtr;
                    
                    if(data.categoryAtr == model.CategoryAtr.PAYMENT_ITEM) {
                        self.categoryName = ko.observable(getText('QMM012_3'));
                    } else if(data.categoryAtr == model.CategoryAtr.DEDUCTION_ITEM) {
                        self.categoryName = ko.observable(getText('QMM012_4'));
                    } else if(data.categoryAtr == model.CategoryAtr.ATTEND_ITEM) {
                        self.categoryName = ko.observable(getText('QMM012_5'));
                    }
                    
                    self.itemNameCd = ko.observable(data.itemNameCd);
                    self.defaultAtr = ko.observable(data.defaultAtr);
                    self.valueAtr = ko.observable(data.valueAtr);
                    self.deprecatedAtr = ko.observable(data.deprecatedAtr);
                    self.socialInsuaEditableAtr = ko.observable(data.socialInsuaEditableAtr);
                    self.intergrateCd = ko.observable(data.intergrateCd);
                } else {
                    //TODO lấy category đang được chọn
//                    self.categoryAtr = data.categoryAtr;
//                    
//                    if(data.categoryAtr == model.CategoryAtr.PAYMENT_ITEM) {
//                        self.categoryName = ko.observable(getText('QMM012_3'));
//                    } else if(data.categoryAtr == model.CategoryAtr.DEDUCTION_ITEM) {
//                        self.categoryName = ko.observable(getText('QMM012_4'));
//                    } else if(data.categoryAtr == model.CategoryAtr.ATTEND_ITEM) {
//                        self.categoryName = ko.observable(getText('QMM012_5'));
//                    }
                    
                    self.itemNameCd = ko.observable(null);
                    self.defaultAtr = ko.observable(null);
                    self.valueAtr = ko.observable(null);
                    self.deprecatedAtr = ko.observable(null);
                    self.socialInsuaEditableAtr = ko.observable(null);
                    self.intergrateCd = ko.observable(null);
                }
            }
        }
        
        class StatementItemName {
            name: KnockoutObservable<string>;
            shortName: KnockoutObservable<string>;
            otherLanguageName: KnockoutObservable<string>;
            englishName: KnockoutObservable<string>;
            
            constructor(data: IStatementItemName) {
                let self = this;
                
                if (data) {
                    self.name = ko.observable(data.name);
                    self.shortName = ko.observable(data.shortName);
                    self.otherLanguageName = ko.observable(data.otherLanguageName);
                    self.englishName = ko.observable(data.englishName);
                } else {
                    self.name = ko.observable(null);
                    self.shortName = ko.observable(null);
                    self.otherLanguageName = ko.observable(null);
                    self.englishName = ko.observable(null);
                }
            }
        }
        
        class PaymentItemSet {
            breakdownItemUseAtr: KnockoutObservable<number>;
            laborInsuranceCategory: KnockoutObservable<number>;
            settingAtr: KnockoutObservable<number>;
            everyoneEqualSet: KnockoutObservable<number>;
            monthlySalary: KnockoutObservable<number>;
            hourlyPay: KnockoutObservable<number>;
            dayPayee: KnockoutObservable<number>;
            monthlySalaryPerday: KnockoutObservable<number>;
            averageWageAtr: KnockoutObservable<number>;
            socialInsuranceCategory: KnockoutObservable<number>;
            taxAtr: KnockoutObservable<number>;
            taxableAmountAtr: KnockoutObservable<number>;
            limitAmount: KnockoutObservable<number>;
            limitAmountAtr: KnockoutObservable<number>;
            taxLimitAmountCode: KnockoutObservable<string>;
            note: KnockoutObservable<string>;
            
            // category comboBox
            taxList: KnockoutObservableArray<model.ItemModel>;
            
            // Covered switch button
            coveredList: KnockoutObservableArray<model.ItemModel>;
            
            // settingAtr radio button
            settingAtrList: KnockoutObservableArray<model.BoxModel>;
            
            constructor(data: IPaymentItemSet) {
                let self = this;
                
                self.taxList = ko.observableArray([
                    new model.ItemModel(model.TaxAtr.TAXATION.toString(), getText('QMM012_x')),
                    new model.ItemModel(model.TaxAtr.LIMIT_TAX_EXEMPTION.toString(), getText('QMM012_x')),
                    new model.ItemModel(model.TaxAtr.NO_LIMIT_TAX_EXEMPTION.toString(), getText('QMM012_x')),
                    new model.ItemModel(model.TaxAtr.COMMUTING_EXPENSES_MANUAL.toString(), getText('QMM012_x')),
                    new model.ItemModel(model.TaxAtr.COMMUTING_EXPENSES_USING_COMMUTER.toString(), getText('QMM012_x'))
                ]);
                
                self.coveredList = ko.observableArray([
                    new model.ItemModel(model.CoveredAtr.COVERED.toString(), getText('QMM012_41')),
                    new model.ItemModel(model.CoveredAtr.NOT_COVERED.toString(), getText('QMM012_42'))
                ]);
                
                self.settingAtrList = ko.observableArray([
                    new model.BoxModel(model.SettingClassification.DESIGNATE_BY_ALL_MEMBERS, getText('QMM012_45')),
                    new model.BoxModel(model.SettingClassification.DESIGNATE_FOR_EACH_SALARY_CONTRACT_TYPE, getText('QMM012_46'))
                ]);
                
                if (data) {
                    self.breakdownItemUseAtr = ko.observable(data.breakdownItemUseAtr);
                    self.laborInsuranceCategory = ko.observable(data.laborInsuranceCategory);
                    self.settingAtr = ko.observable(data.settingAtr);
                    self.everyoneEqualSet = ko.observable(data.everyoneEqualSet);
                    self.monthlySalary = ko.observable(data.monthlySalary);
                    self.hourlyPay = ko.observable(data.hourlyPay);
                    self.dayPayee = ko.observable(data.dayPayee);
                    self.monthlySalaryPerday = ko.observable(data.monthlySalaryPerday);
                    self.averageWageAtr = ko.observable(data.averageWageAtr);
                    self.socialInsuranceCategory = ko.observable(data.socialInsuranceCategory);
                    self.taxAtr = ko.observable(data.taxAtr);
                    self.taxableAmountAtr = ko.observable(data.taxableAmountAtr);
                    self.limitAmount = ko.observable(data.limitAmount);
                    self.limitAmountAtr = ko.observable(data.limitAmountAtr);
                    self.taxLimitAmountCode = ko.observable(data.taxLimitAmountCode);
                    self.note = ko.observable(data.note);
                } else {
                    self.breakdownItemUseAtr = ko.observable(null);
                    self.laborInsuranceCategory = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.settingAtr = ko.observable(model.SettingClassification.DESIGNATE_FOR_EACH_SALARY_CONTRACT_TYPE);
                    self.everyoneEqualSet = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.monthlySalary = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.hourlyPay = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.dayPayee = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.monthlySalaryPerday = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.averageWageAtr = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.socialInsuranceCategory = ko.observable(model.CoveredAtr.NOT_COVERED);
                    self.taxAtr = ko.observable(model.TaxAtr.TAXATION);
                    self.taxableAmountAtr = ko.observable(null);
                    self.limitAmount = ko.observable(null);
                    self.limitAmountAtr = ko.observable(null);
                    self.taxLimitAmountCode = ko.observable(null);
                    self.note = ko.observable(null);
                }
            }
        }
        
        class StatementItemDisplaySet {
            zeroDisplayAtr: KnockoutObservable<number>;
            itemNameDisplay: KnockoutObservable<number>;
            
            // zeroDisplayAtrList switch button
            zeroDisplayAtrList: KnockoutObservableArray<model.ItemModel>;
            
            // number -> boolean for checkbox
            itemNameDisplayCustom: KnockoutObservable<boolean>;
            
            constructor(data: IStatementItemDisplaySet) {
                let self = this;
                
                self.zeroDisplayAtrList = ko.observableArray([
                    new model.ItemModel(model.Display.SHOW.toString(), getText('QMM012_53')),
                    new model.ItemModel(model.Display.NOT_SHOW.toString(), getText('QMM012_54'))
                ]);
                
                if (data) {
                    self.zeroDisplayAtr = ko.observable(data.zeroDisplayAtr);
                    self.itemNameDisplay = ko.observable(data.itemNameDisplay);
                } else {
                    self.zeroDisplayAtr = ko.observable(model.Display.NOT_SHOW);
                    self.itemNameDisplay = ko.observable(model.Display.NOT_SHOW);
                }
                
                self.itemNameDisplayCustom = ko.observable(self.itemNameDisplay() == 1);
                
                self.itemNameDisplayCustom.subscribe(x => {
                    if (x) {
                        self.itemNameDisplay(1);
                    } else {
                        self.itemNameDisplay(0);
                    }
                });
            }
        }
        
        class ItemRangeSet {
            rangeValueAtr: KnockoutObservable<number>;
            errorUpperLimitSettingAtr: KnockoutObservable<number>;
            errorUpperRangeValueAmount: KnockoutObservable<number>;
            errorUpperRangeValueTime: KnockoutObservable<number>;
            errorUpperRangeValueNum: KnockoutObservable<number>;
            errorLowerLimitSettingAtr: KnockoutObservable<number>;
            errorLowerRangeValueAmount: KnockoutObservable<number>;
            errorLowerRangeValueTime: KnockoutObservable<number>;
            errorLowerRangeValueNum: KnockoutObservable<number>;
            alarmUpperLimitSettingAtr: KnockoutObservable<number>;
            alarmUpperRangeValueAmount: KnockoutObservable<number>;
            alarmUpperRangeValueTime: KnockoutObservable<number>;
            alarmUpperRangeValueNum: KnockoutObservable<number>;
            alarmLowerLimitSettingAtr: KnockoutObservable<number>;
            alarmLowerRangeValueAmount: KnockoutObservable<number>;
            alarmLowerRangeValueTime: KnockoutObservable<number>;
            alarmLowerRangeValueNum: KnockoutObservable<number>;
            
            // number -> boolean for checkbox
            errorUpperLimitSettingAtrCus: KnockoutObservable<boolean>;
            errorLowerLimitSettingAtrCus: KnockoutObservable<boolean>;
            alarmUpperLimitSettingAtrCus: KnockoutObservable<boolean>;
            alarmLowerLimitSettingAtrCus: KnockoutObservable<boolean>;
            
            // option for numberEditor binding
            numberEditorOption: KnockoutObservable<any>;
            
            constructor(data: IItemRangeSet) {
                let self = this;
            
                self.numberEditorOption = {
                    grouplength: 3,
                    decimallength: 3,
                    width: "200px",
                    textalign: "right",
                    currencyformat: "JPY"
                };
                
                if (data) {
                    self.rangeValueAtr = ko.observable(data.rangeValueAtr);
                    self.errorUpperLimitSettingAtr = ko.observable(data.errorUpperLimitSettingAtr);
                    self.errorUpperRangeValueAmount = ko.observable(data.errorUpperRangeValueAmount);
                    self.errorUpperRangeValueTime = ko.observable(data.errorUpperRangeValueTime);
                    self.errorUpperRangeValueNum = ko.observable(data.errorUpperRangeValueNum);
                    self.errorLowerLimitSettingAtr = ko.observable(data.errorLowerLimitSettingAtr);
                    self.errorLowerRangeValueAmount = ko.observable(data.errorLowerRangeValueAmount);
                    self.errorLowerRangeValueTime = ko.observable(data.errorLowerRangeValueTime);
                    self.errorLowerRangeValueNum = ko.observable(data.errorLowerRangeValueNum);
                    self.alarmUpperLimitSettingAtr = ko.observable(data.alarmUpperLimitSettingAtr);
                    self.alarmUpperRangeValueAmount = ko.observable(data.alarmUpperRangeValueAmount);
                    self.alarmUpperRangeValueTime = ko.observable(data.alarmUpperRangeValueTime);
                    self.alarmUpperRangeValueNum = ko.observable(data.alarmUpperRangeValueNum);
                    self.alarmLowerLimitSettingAtr = ko.observable(data.alarmLowerLimitSettingAtr);
                    self.alarmLowerRangeValueAmount = ko.observable(data.alarmLowerRangeValueAmount);
                    self.alarmLowerRangeValueTime = ko.observable(data.alarmLowerRangeValueTime);
                    self.alarmLowerRangeValueNum = ko.observable(data.alarmLowerRangeValueNum);
                } else {
                    self.rangeValueAtr = ko.observable(null);
                    self.errorUpperLimitSettingAtr = ko.observable(null);
                    self.errorUpperRangeValueAmount = ko.observable(null);
                    self.errorUpperRangeValueTime = ko.observable(null);
                    self.errorUpperRangeValueNum = ko.observable(null);
                    self.errorLowerLimitSettingAtr = ko.observable(null);
                    self.errorLowerRangeValueAmount = ko.observable(null);
                    self.errorLowerRangeValueTime = ko.observable(null);
                    self.errorLowerRangeValueNum = ko.observable(null);
                    self.alarmUpperLimitSettingAtr = ko.observable(null);
                    self.alarmUpperRangeValueAmount = ko.observable(null);
                    self.alarmUpperRangeValueTime = ko.observable(null);
                    self.alarmUpperRangeValueNum = ko.observable(null);
                    self.alarmLowerLimitSettingAtr = ko.observable(null);
                    self.alarmLowerRangeValueAmount = ko.observable(null);
                    self.alarmLowerRangeValueTime = ko.observable(null);
                    self.alarmLowerRangeValueNum = ko.observable(null);
                }
                
                self.errorUpperLimitSettingAtrCus = ko.observable(self.errorUpperLimitSettingAtr() == 1);
                self.errorLowerLimitSettingAtrCus = ko.observable(self.errorLowerLimitSettingAtr() == 1);
                self.alarmUpperLimitSettingAtrCus = ko.observable(self.alarmUpperLimitSettingAtr() == 1);
                self.alarmLowerLimitSettingAtrCus = ko.observable(self.alarmLowerLimitSettingAtr() == 1);
                
                self.errorUpperLimitSettingAtrCus.subscribe(x => {
                    if (x) {
                        self.errorUpperLimitSettingAtr(1);
                    } else {
                        self.errorUpperLimitSettingAtr(0);
                    }
                });
                
                self.errorLowerLimitSettingAtrCus.subscribe(x => {
                    if (x) {
                        self.errorLowerLimitSettingAtr(1);
                    } else {
                        self.errorLowerLimitSettingAtr(0);
                    }
                });
                
                self.alarmUpperLimitSettingAtrCus.subscribe(x => {
                    if (x) {
                        self.alarmUpperLimitSettingAtr(1);
                    } else {
                        self.alarmUpperLimitSettingAtr(0);
                    }
                });
                
                self.alarmLowerLimitSettingAtrCus.subscribe(x => {
                    if (x) {
                        self.alarmLowerLimitSettingAtr(1);
                    } else {
                        self.alarmLowerLimitSettingAtr(0);
                    }
                });
            }
        }
        
        class DeductionItemSet {
            deductionItemAtr: number;
            breakdownItemUseAtr: number;
            note: string;
            
            // breakdownItemUse switch button
            breakdownItemUseList: KnockoutObservableArray<model.ItemModel>;
            
            constructor(data: IDeductionItemSet) {
                let self = this;
                
                self.breakdownItemUseList = ko.observableArray([
                    new model.ItemModel(model.BreakdownItemUseAtr.USE.toString(), getText('QMM012_72')),
                    new model.ItemModel(model.BreakdownItemUseAtr.NOT_USE.toString(), getText('QMM012_73'))
                ]);
            
                if (data) {
                    self.deductionItemAtr = ko.observable(data.deductionItemAtr);
                    self.breakdownItemUseAtr = ko.observable(data.breakdownItemUseAtr);
                    self.note = ko.observable(data.note);
                } else {
                    self.deductionItemAtr = ko.observable(null);
                    self.breakdownItemUseAtr = ko.observable(model.BreakdownItemUseAtr.NOT_USE);
                    self.note = ko.observable(null);
                }
            }
        }
        
        interface IStatementItemData {
            cid: string;
            salaryItemId: string;
            statementItem: IStatementItem;
            statementItemName: IStatementItemName;
            paymentItemSet: IPaymentItemSet;
            deductionItemSet: IDeductionItemSet;
            timeItemSet: ITimeItemSet;
            statementItemDisplaySet: IStatementItemDisplaySet;
            itemRangeSet: IItemRangeSet;
            validityPeriodAndCycleSet: IValidityPeriodAndCycleSet;
            breakdownItemSet: IBreakdownItemSet;
            
            // only show in gridlist
            categoryAtr: number;
            itemNameCd: string;
            name: string;
            deprecatedAtr: number;
        }
        
        interface IStatementItem {
            categoryAtr: number;
            itemNameCd: number;
            defaultAtr: number;
            valueAtr: number;
            deprecatedAtr: number;
            socialInsuaEditableAtr: number;
            intergrateCd: number;
        }
        
        interface IStatementItemName {
            name: string;
            shortName: string;
            otherLanguageName: string;
            englishName: string;
        }
        
        interface IPaymentItemSet {
            breakdownItemUseAtr: number;
            laborInsuranceCategory: number;
            settingAtr: number;
            everyoneEqualSet: number;
            monthlySalary: number;
            hourlyPay: number;
            dayPayee: number;
            monthlySalaryPerday: number;
            averageWageAtr: number;
            socialInsuranceCategory: number;
            taxAtr: number;
            taxableAmountAtr: number;
            limitAmount: number;
            limitAmountAtr: number;
            taxLimitAmountCode: string;
            note: string;
        }
        
        interface IDeductionItemSet {
            deductionItemAtr: number;
            breakdownItemUseAtr: number;
            note: string;
        }
        
        interface ITimeItemSet {
            averageWageAtr: number;
            workingDaysPerYear: number;
            timeCountAtr: number;
            note: string;
        }
        
        interface IStatementItemDisplaySet {
            zeroDisplayAtr: number;
            itemNameDisplay: number;
        }
        
        interface IItemRangeSet {
            rangeValueAtr: number;
            errorUpperLimitSettingAtr: number;
            errorUpperRangeValueAmount: number;
            errorUpperRangeValueTime: number;
            errorUpperRangeValueNum: number;
            errorLowerLimitSettingAtr: number;
            errorLowerRangeValueAmount: number;
            errorLowerRangeValueTime: number;
            errorLowerRangeValueNum: number;
            alarmUpperLimitSettingAtr: number;
            alarmUpperRangeValueAmount: number;
            alarmUpperRangeValueTime: number;
            alarmUpperRangeValueNum: number;
            alarmLowerLimitSettingAtr: number;
            alarmLowerRangeValueAmount: number;
            alarmLowerRangeValueTime: number;
            alarmLowerRangeValueNum: number;
        }
        
        interface IValidityPeriodAndCycleSet {
            cycleSettingAtr: number;
            january: number;
            february: number;
            march: number;
            april: number;
            may: number;
            june: number;
            july: number;
            august: number;
            september: number;
            october: number;
            november: number;
            december: number;
            periodAtr: number;
            yearPeriodStart: number;
            yearPeriodEnd: number;
        }
        
        interface IBreakdownItemSet {
            breakdownItemCode: number;
            breakdownItemName: string;
        }
    }  
}