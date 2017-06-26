module nts.uk.pr.view.qmm017.l {
    export module viewmodel {
        export class ScreenModelLScreen {
            easyFormulaName: KnockoutObservable<string>;
            comboBoxFormulaType: KnockoutObservable<ComboBox>;
            comboBoxBaseAmount: KnockoutObservable<ComboBox>;
            baseAmountFixedValue: KnockoutObservable<number>;
            baseAmountListItem: KnockoutObservableArray<any>;
            baseAmountFixedValueOption: KnockoutObservable<any>;
            baseAmountSelectionItems: KnockoutObservable<string>;
            optionBaseAmountSelectionItems: KnockoutObservable<any>;
            comboBoxBaseValue: KnockoutObservable<ComboBox>;
            baseValueFixedValue: KnockoutObservable<number>;
            premiumRate: KnockoutObservable<number>;
            premiumRateInputOption: KnockoutObservable<any>;
            switchButtonRoundingFiguresD: KnockoutObservable<SwitchButton>;
            switchButtonRoundingFiguresF: KnockoutObservable<SwitchButton>;
            comboBoxCoefficient: KnockoutObservable<ComboBox>;
            coefficientFixedValue: KnockoutObservable<number>;
            comboBoxAdjustmentAtr: KnockoutObservable<ComboBox>;
            paramIsUpdate: any;
            paramDirtyData: any;
            companyUnitPriceItems: KnockoutObservableArray<any>;
            personalUnitPriceItems: KnockoutObservableArray<any>;
            paymentItems: KnockoutObservableArray<any>;
            deductionItems: KnockoutObservableArray<any>;
            startYm: any;

            constructor(param) {
                var self = this;
                self.paramIsUpdate = param.isUpdate;
                self.paramDirtyData = param.dirtyData;
                self.startYm = param.startYm;
                self.initOriginal();
                if (self.paramIsUpdate === true) {
                    self.easyFormulaName(self.paramDirtyData.easyFormulaName);
                    self.comboBoxFormulaType().selectedCode(self.paramDirtyData.easyFormulaTypeAtr.toString());
                    self.comboBoxBaseAmount().selectedCode(self.paramDirtyData.baseAmountDevision.toString());
                    self.baseAmountFixedValue(self.paramDirtyData.baseFixedAmount);
                    self.baseAmountListItem(self.paramDirtyData.referenceItemCodes);
                    self.comboBoxBaseValue().selectedCode(self.paramDirtyData.baseValueDevision.toString());
                    self.baseValueFixedValue(self.paramDirtyData.baseFixedValue);
                    self.premiumRate(self.paramDirtyData.premiumRate);
                    self.switchButtonRoundingFiguresD().selectedRuleCode(self.paramDirtyData.roundProcessingDevision.toString());
                    self.switchButtonRoundingFiguresF().selectedRuleCode(self.paramDirtyData.totalRounding.toString());
                    self.comboBoxCoefficient().selectedCode(self.paramDirtyData.coefficientDivision.toString());
                    self.coefficientFixedValue(self.paramDirtyData.coefficientFixedValue);
                    self.comboBoxAdjustmentAtr().selectedCode(self.paramDirtyData.adjustmentDevision.toString());
                }
            }

            exportTextListItemName(lstItem: Array<ItemModel>, lstItemCode: Array<string>) {
                var textListItemName = '';
                for (let i = 0; i < lstItemCode.length; i++) {
                    if (i !== lstItemCode.length - 1) {
                        textListItemName += (_.find(lstItem, function(item) { return item.code == lstItemCode[i] }).name + ' + ');
                    } else {
                        textListItemName += (_.find(lstItem, function(item) { return item.code == lstItemCode[i] }).name);
                    }
                }
                return textListItemName;
            }

            initOriginal() {
                var self = this;
                self.easyFormulaName = ko.observable(null);
                self.comboBoxFormulaType = ko.observable(new ComboBox([
                    new ItemModel('0', '計算式１'),
                    new ItemModel('1', '計算式２'),
                    new ItemModel('2', '計算式3')
                ]));
                //              0 Fixed value
                //              1 Company Unit price 
                //              2 Person unit price
                //              3 Payment items
                //              4 Deduction item
                self.comboBoxBaseAmount = ko.observable(new ComboBox([
                    new ItemModel('0', '固定値'),
                    new ItemModel('2', '個人単価'),
                    new ItemModel('3', '支給項目'),
                    new ItemModel('4', '控除項目')
                ]));
                // if formula type 3, base amount attribute: 0, 1, 3, 4
                // if formula type 1 or 2, base amount attribute: 0, 2, 3, 4
                self.comboBoxFormulaType().selectedCode.subscribe(function(codeChange) {
                    if (codeChange === '2') {
                        self.comboBoxBaseAmount().itemList([
                            new ItemModel('0', '固定値'),
                            new ItemModel('1', '会社単価'),
                            new ItemModel('3', '支給項目'),
                            new ItemModel('4', '控除項目')
                        ]);
                    } else {
                        self.comboBoxBaseAmount().itemList([
                            new ItemModel('0', '固定値'),
                            new ItemModel('2', '個人単価'),
                            new ItemModel('3', '支給項目'),
                            new ItemModel('4', '控除項目')
                        ]);
                    }
                });
                self.baseAmountFixedValueOption = ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    decimallength: 2,
                    currencyformat: "JPY"
                }));
                self.baseAmountFixedValue = ko.observable(0);
                self.baseAmountListItem = ko.observableArray([]);
                self.baseAmountSelectionItems = ko.observable('');
                self.optionBaseAmountSelectionItems = ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "290px",
                    textalign: "left"
                }));
                //Fixed value  0
                //Standard number of days 1
                //Number of working days 2
                //Work day 3
                //Work days + paid usage 4
                //Reference days * Reference time 5
                //Number of working days * Reference time 6
                //Working days * Reference time 7
                //(Attendance days + number of paid use) * reference time 8
                //Working time 9
                self.comboBoxBaseValue = ko.observable(new ComboBox([
                    new ItemModel('0', '固定値'),
                    new ItemModel('1', '基準日数'),
                    new ItemModel('2', '要勤務日数'),
                    new ItemModel('3', '出勤日数'),
                    new ItemModel('4', '出勤日数+有給使用数'),
                    new ItemModel('5', '基準日数*基準時間'),
                    new ItemModel('6', '要勤務日数*基準時間'),
                    new ItemModel('7', '出勤日数*基準時間'),
                    new ItemModel('8', '(出勤日数+有給使用数)*基準時間'),
                    new ItemModel('9', '出勤時間')
                ]));
                self.baseValueFixedValue = ko.observable(0);
                self.premiumRate = ko.observable(0);
                self.premiumRateInputOption = ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                    grouplength: 3,
                    decimallength: 0,
                    symbolChar: '%'
                }));
                //Round figure: 0
                //Round up: 1
                //Truncate: 2
                //No round figure: 3
                self.switchButtonRoundingFiguresD = ko.observable(new SwitchButton([
                    new ItemModel('0', '四捨五入'),
                    new ItemModel('1', '切り上げ'),
                    new ItemModel('2', '切り捨て'),
                    new ItemModel('3', '端数処理なし')
                ]));
                //Round figure: 0
                //Round up: 1
                //Truncate: 2
                self.switchButtonRoundingFiguresF = ko.observable(new SwitchButton([
                    new ItemModel('0', '四捨五入'),
                    new ItemModel('1', '切り上げ'),
                    new ItemModel('2', '切り捨て')
                ]));
                //TO DO: get items which have CTG_ATR = 2 from table QCAMT_ITEM to add to the item model list from the fourth item 
                self.comboBoxCoefficient = ko.observable(new ComboBox([
                    new ItemModel('0000', '固定値'),
                    new ItemModel('F200', '基準日数'),
                    new ItemModel('F000', '出勤日数+有給使用数')
                ]));
                self.coefficientFixedValue = ko.observable(0);
                //Do not adjust: 0
                //Plus adjustment: 1
                //Minus adjustment: 2
                //Plus and minus inversion: 3
                self.comboBoxAdjustmentAtr = ko.observable(new ComboBox([
                    new ItemModel('0', '調整しない'),
                    new ItemModel('1', 'プラス調整'),
                    new ItemModel('2', 'マイナス調整'),
                    new ItemModel('3', 'プラスマイナス反転')
                ]));

                self.companyUnitPriceItems = ko.observableArray([]);
                self.personalUnitPriceItems = ko.observableArray([]);
                self.paymentItems = ko.observableArray([]);
                self.deductionItems = ko.observableArray([]);
            }

            start(): JQueryPromise<any> {
                var self = this;
                var dfdMaster = $.Deferred<any>();
                var dfdGetListCompanyUP = $.Deferred<any>();
                var dfdGetListPersonalUP = $.Deferred<any>();
                var dfdGetPaymentItems = $.Deferred<any>();
                var dfdGetDeductionItems = $.Deferred<any>();
                var dfdGetTimeItems = $.Deferred<any>();
                service.getListItemMaster(2)
                    .done(function(lstItem: Array<model.ItemMasterDto>) {
                        if (lstItem && lstItem.length > 0) {
                            _.forEach(lstItem, item => {
                                self.comboBoxCoefficient().itemList.push(new ItemModel(item.itemCode, item.itemName));
                            });
                        }
                        dfdGetTimeItems.resolve();
                    })
                    .fail(function() {
                        // Alert message
                        nts.uk.ui.dialog.alert(res);
                        dfdGetTimeItems.reject();
                    });
                service.getListCompanyUnitPrice(self.startYm.replace('/', ''))
                    .done(function(lstCompanyUnitPrice: Array<model.CompanyUnitPriceDto>) {
                        if (lstCompanyUnitPrice && lstCompanyUnitPrice.length > 0) {
                            _.forEach(lstCompanyUnitPrice, item => {
                                self.companyUnitPriceItems.push(new ItemModel(item.companyUnitPriceCode, item.companyUnitPriceName));
                            });
                        }
                        dfdGetListCompanyUP.resolve();
                    }).fail(function() {
                        dfdGetListCompanyUP.reject();
                    });

                service.getListPersonalUnitPrice()
                    .done(function(lstPersonalUnitPrice: Array<model.PersonalUnitPriceDto>) {
                        if (lstPersonalUnitPrice && lstPersonalUnitPrice.length > 0) {
                            _.forEach(lstPersonalUnitPrice, item => {
                                self.personalUnitPriceItems.push(new ItemModel(item.personalUnitPriceCode, item.personalUnitPriceName));
                            });
                        }
                        dfdGetListPersonalUP.resolve();
                    }).fail(function() {
                        dfdGetListPersonalUP.reject();
                    });

                service.getListItemMaster(0)
                    .done(function(lstItem: Array<model.ItemMasterDto>) {
                        if (lstItem && lstItem.length > 0) {
                            _.forEach(lstItem, item => {
                                self.paymentItems.push(new ItemModel(item.itemCode, item.itemName));
                            });
                        }
                        dfdGetPaymentItems.resolve();
                    })
                    .fail(function() {
                        // Alert message
                        nts.uk.ui.dialog.alert(res);
                        dfdGetPaymentItems.reject();
                    });

                service.getListItemMaster(1)
                    .done(function(lstItem: Array<model.ItemMasterDto>) {
                        if (lstItem && lstItem.length > 0) {
                            _.forEach(lstItem, item => {
                                self.deductionItems.push(new ItemModel(item.itemCode, item.itemName));
                            });
                        }
                        dfdGetDeductionItems.resolve();
                    })
                    .fail(function() {
                        // Alert message
                        nts.uk.ui.dialog.alert(res);
                        dfdGetDeductionItems.reject();
                    });

                $.when(dfdGetTimeItems.promise(), dfdGetListCompanyUP.promise(), dfdGetListPersonalUP.promise(), dfdGetPaymentItems.promise(), dfdGetDeductionItems.promise())
                    .done(function() {
                        if (self.baseAmountListItem().length > 0) {
                            var textListItemName = '';
                            if (self.comboBoxBaseAmount().selectedCode() === '1') {
                                textListItemName = self.exportTextListItemName(self.companyUnitPriceItems(), self.baseAmountListItem());
                            } else if (self.comboBoxBaseAmount().selectedCode() === '2') {
                                textListItemName = self.exportTextListItemName(self.personalUnitPriceItems(), self.baseAmountListItem());
                            } else if (self.comboBoxBaseAmount().selectedCode() === '3') {
                                textListItemName = self.exportTextListItemName(self.paymentItems(), self.baseAmountListItem());
                            } else if (self.comboBoxBaseAmount().selectedCode() === '4') {
                                textListItemName = self.exportTextListItemName(self.deductionItems(), self.baseAmountListItem());
                            }
                        } else {
                            self.baseAmountSelectionItems('');
                        };
                        self.baseAmountSelectionItems(textListItemName);
                        self.baseAmountListItem.subscribe(function(lstItem) {
                            if (lstItem && lstItem.length > 0) {
                                var textListItemName = '';
                                if (self.comboBoxBaseAmount().selectedCode() === '1') {
                                    textListItemName = self.exportTextListItemName(self.companyUnitPriceItems(), lstItem);
                                } else if (self.comboBoxBaseAmount().selectedCode() === '2') {
                                    textListItemName = self.exportTextListItemName(self.personalUnitPriceItems(), lstItem);
                                } else if (self.comboBoxBaseAmount().selectedCode() === '3') {
                                    textListItemName = self.exportTextListItemName(self.paymentItems(), lstItem);
                                } else if (self.comboBoxBaseAmount().selectedCode() === '4') {
                                    textListItemName = self.exportTextListItemName(self.deductionItems(), lstItem);
                                }
                                self.baseAmountSelectionItems(textListItemName);
                            } else {
                                self.baseAmountSelectionItems('');
                            }
                        });
                        dfdMaster.resolve();
                    })
                    .fail(function() {
                        dfdMaster.reject();
                    });
                // Return.
                return dfdMaster.promise();
            }

            openDialogP() {
                var self = this;
                let param = {
                    subject: '',
                    itemList: [],
                    selectedItems: self.baseAmountListItem()
                };
                if (self.comboBoxBaseAmount().selectedCode() === '1') {
                    param.subject = '会社単価';
                    param.itemList = self.companyUnitPriceItems();
                } else if (self.comboBoxBaseAmount().selectedCode() === '2') {
                    param.subject = '個人単価';
                    param.itemList = self.personalUnitPriceItems();
                } else if (self.comboBoxBaseAmount().selectedCode() === '3') {
                    param.subject = '支給項目';
                    param.itemList = self.paymentItems();
                } else if (self.comboBoxBaseAmount().selectedCode() === '4') {
                    param.subject = '控除項目';
                    param.itemList = self.deductionItems();
                }
                nts.uk.ui.windows.setShared('paramFromScreenL', param);
                nts.uk.ui.windows.sub.modal('/view/qmm/017/p/index.xhtml', { title: '項目の選択', width: 350, height: 480 }).onClosed(() => {
                    let baseAmountListItem = nts.uk.ui.windows.getShared('baseAmountListItem');
                    if (baseAmountListItem.length > 0) {
                        self.baseAmountListItem(baseAmountListItem);
                    }
                });
            }

            closeAndReturnData() {
                var self = this;
                let easyFormulaDetail = {
                    easyFormulaCode: self.paramDirtyData.easyFormulaCode,
                    easyFormulaName: self.easyFormulaName(),
                    easyFormulaTypeAtr: self.comboBoxFormulaType().selectedCode(),
                    baseAmountDevision: self.comboBoxBaseAmount().selectedCode(),
                    baseFixedAmount: self.baseAmountFixedValue(),
                    referenceItemCodes: self.baseAmountListItem(),
                    baseValueDevision: self.comboBoxBaseValue().selectedCode(),
                    baseFixedValue: self.baseValueFixedValue(),
                    premiumRate: self.premiumRate(),
                    roundProcessingDevision: self.switchButtonRoundingFiguresD().selectedRuleCode(),
                    totalRounding: self.switchButtonRoundingFiguresF().selectedRuleCode(),
                    coefficientDivision: self.comboBoxCoefficient().selectedCode(),
                    coefficientFixedValue: self.coefficientFixedValue(),
                    adjustmentDevision: self.comboBoxAdjustmentAtr().selectedCode()
                };
                if (easyFormulaDetail.baseAmountDevision === '0') {
                    easyFormulaDetail.referenceItemCodes = [];
                } else {
                    easyFormulaDetail.baseFixedAmount = 0;
                }
                if (easyFormulaDetail.baseValueDevision !== '0') {
                    easyFormulaDetail.baseFixedValue = 0;
                }
                if (easyFormulaDetail.coefficientDivision !== '0000') {
                    easyFormulaDetail.coefficientFixedValue = 0;
                }
                nts.uk.ui.windows.setShared('easyFormulaDetail', easyFormulaDetail);
                nts.uk.ui.windows.close();
            }

            closeDialog() {
                nts.uk.ui.windows.close();
            }
        }

        export class ComboBox {
            itemList: KnockoutObservableArray<ItemModel>;
            itemName: KnockoutObservable<string>;
            currentCode: KnockoutObservable<string>
            selectedCode: KnockoutObservable<string>;

            constructor(itemList: Array<ItemModel>) {
                var self = this;
                self.itemList = ko.observableArray(itemList);
                self.itemName = ko.observable('');
                self.currentCode = ko.observable('0');
                self.selectedCode = ko.observable('0');
            }
        }

        export class SwitchButton {
            roundingRules: KnockoutObservableArray<any>;
            selectedRuleCode: any;

            constructor(itemList: Array<ItemModel>) {
                var self = this;
                self.roundingRules = ko.observableArray(itemList);
                self.selectedRuleCode = ko.observable('0');
            }
        }

        class ItemModel {
            code: string;
            name: string;

            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
    }
}