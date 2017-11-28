module nts.uk.at.view.kml002.e.viewmodel {
    export class ScreenModel {
        columns: KnockoutObservable<any>;
        columnsTime: KnockoutObservable<any>;
        columnsAmount: KnockoutObservable<any>;
        checked: KnockoutObservable<boolean>;
        checkedAmount: KnockoutObservable<boolean>;
        checkedTime: KnockoutObservable<boolean>;
        rightItemcolumns: KnockoutObservable<any>;
        rightItemcolumnsAmount: KnockoutObservable<any>;
        rightItemcolumnsTime: KnockoutObservable<any>;
        methods: KnockoutObservableArray<any>;
        selectedMethod: any;
        categoryItems: KnockoutObservableArray<any>;
        categoryItemsTime: KnockoutObservableArray<any>;
        catCode: KnockoutObservable<number>;
        catCodeTime: KnockoutObservable<number>;
        unitPriceItems: KnockoutObservableArray<any>;
        uPCd: KnockoutObservable<number>;
        roundingItems: KnockoutObservableArray<any>;
        roundingCd: KnockoutObservable<number>;
        processingList: KnockoutObservableArray<any>;
        selectedProcessing: KnockoutObservable<number>;
        attrLabel: KnockoutObservable<String>;
        itemNameLabel: KnockoutObservable<String>;

        currentCodeList: KnockoutObservableArray<any>;
        currentCodeListAmount: KnockoutObservableArray<any>;
        currentCodeListTime: KnockoutObservableArray<any>;
        items: KnockoutObservableArray<ItemModel>;
        itemsAmount: KnockoutObservableArray<ItemModel>;
        itemsTime: KnockoutObservableArray<ItemModel>;
        allItem: KnockoutObservableArray<ItemModel>;
        allItemAmount: KnockoutObservableArray<ItemModel>;
        allItemTime: KnockoutObservableArray<ItemModel>;
        enable: KnockoutObservable<boolean>;
        currentRightCodeList: KnockoutObservableArray<any>;
        currentRightCodeListAmount: KnockoutObservableArray<any>;
        currentRightCodeListTime: KnockoutObservableArray<any>;
        rightItems: KnockoutObservableArray<NewItemModel>;
        rightItemsAmount: KnockoutObservableArray<NewItemModel>;
        rightItemsTime: KnockoutObservableArray<NewItemModel>;
        enableReturn: KnockoutObservable<boolean>;
        enableReturnAmount: KnockoutObservable<boolean>;
        enableReturnTime: KnockoutObservable<boolean>;
        unitSelect: KnockoutObservable<any>;
        listBudget: KnockoutObservableArray<any>;
        currentData: any;
        currentDataMoney: any;
        constructor() {
            var self = this;

            var data = nts.uk.ui.windows.getShared("KML002_A_DATA");
            self.currentData = data.amount;
            self.currentDataMoney = data.unitPrice;
            self.attrLabel = ko.observable(data.attribute);
            self.itemNameLabel = ko.observable(data.itemName);
            self.unitSelect = ko.observable(0);
            self.items = ko.observableArray([]);
            self.itemsAmount = ko.observableArray([]);
            self.itemsTime = ko.observableArray([]);
            self.rightItems = ko.observableArray([]);
            self.rightItemsAmount = ko.observableArray([]);
            self.rightItemsTime = ko.observableArray([]);
            self.listBudget = ko.observableArray([]);
            self.enable = ko.observable(true);
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'code', width: 50, hidden: true },
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'name', width: 180, formatter: _.escape }
            ]);
            self.columnsTime = ko.observableArray([
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'code', width: 50, hidden: true },
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'name', width: 180, formatter: _.escape }
            ]);
            self.columnsAmount = ko.observableArray([
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'code', width: 50, hidden: true },
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'name', width: 180, formatter: _.escape }
            ]);

            self.currentCodeList = ko.observableArray([]);
            self.currentCodeListAmount = ko.observableArray([]);
            self.currentCodeListTime = ko.observableArray([]);
            self.rightItemcolumns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'code', width: 50, hidden: true },
                { headerText: nts.uk.resource.getText("KML002_36"), prop: 'operatorAtr', width: 80 },
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'name', width: 160, formatter: _.escape }
            ]);
            self.rightItemcolumnsAmount = ko.observableArray([
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'code', width: 50, hidden: true },
                { headerText: nts.uk.resource.getText("KML002_36"), prop: 'operatorAtr', width: 80 },
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'name', width: 160, formatter: _.escape }
            ]);
            self.rightItemcolumnsTime = ko.observableArray([
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'code', width: 50, hidden: true },
                { headerText: nts.uk.resource.getText("KML002_36"), prop: 'operatorAtr', width: 80 },
                { headerText: nts.uk.resource.getText("KML002_7"), prop: 'name', width: 160, formatter: _.escape }
            ]);

            self.currentRightCodeList = ko.observableArray([]);
            self.currentRightCodeListAmount = ko.observableArray([]);
            self.currentRightCodeListTime = ko.observableArray([]);
            self.allItem = ko.observableArray([]);
            self.allItemAmount = ko.observableArray([]);
            self.allItemTime = ko.observableArray([]);
            self.checked = ko.observable(true);
            self.checkedAmount = ko.observable(true);
            self.checkedTime = ko.observable(true);

            self.methods = ko.observableArray([
                { code: '0', name: nts.uk.resource.getText("KML002_50") },
                { code: '1', name: nts.uk.resource.getText("KML002_58") }
            ]);

            self.selectedMethod = ko.observable(0);

            self.categoryItems = ko.observableArray([
                { catCode: 0, catName: nts.uk.resource.getText("KML002_29") },
                { catCode: 1, catName: nts.uk.resource.getText("KML002_32") }
            ]);
            self.categoryItemsTime = ko.observableArray([
                { catCodeTime: 0, catNameTime: nts.uk.resource.getText("KML002_29") },
                { catCodeTime: 1, catNameTime: nts.uk.resource.getText("KML002_32") }
            ]);

            self.catCode = ko.observable(0);
            self.catCodeTime = ko.observable(0);
            if (self.unitSelect() == 1) {
                $('.method-a').hide();
                $('.method-b').hide();
                $('.cal-method-selection').hide();
                $('.method-c').show();

            } else if (self.unitSelect() == 0) {
                if (self.selectedMethod() == 0) {
                    $('.method-a').show();
                    $('.method-b').hide();
                    $('.method-c').hide();
                    self.formulaTimeUnit();
                } else {
                    $('.method-a').hide();
                    $('.method-b').show();
                    $('.method-c').hide();
                    self.formulaTime();
                }
            }
            self.unitSelect.subscribe(function(value) {
                if (value == 1) {
                    $('.method-a').hide();
                    $('.method-b').hide();
                    $('.cal-method-selection').hide();
                    $('.method-c').show();
                    self.getData();

                }
            });
            self.unitSelect(data.unit);
            self.selectedMethod.subscribe(function(value) {
                var dfd = $.Deferred();
                if (value == 0) {
                    $('.method-a').show();
                    $('.method-b').hide();
                    self.formulaTimeUnit();
                } else if (value == 1) {
                    $('.method-a').hide();
                    $('.method-b').show();
                    self.formulaTime();
                }

            });

            self.unitPriceItems = ko.observableArray([
                { uPCd: UnitPrice.UNIT_PRICE_1, uPName: nts.uk.resource.getText("KML002_53") },
                { uPCd: UnitPrice.UNIT_PRICE_2, uPName: nts.uk.resource.getText("KML002_54") },
                { uPCd: UnitPrice.UNIT_PRICE_3, uPName: nts.uk.resource.getText("KML002_55") },
                { uPCd: UnitPrice.CONTRACT, uPName: nts.uk.resource.getText("KML002_56") },
                { uPCd: UnitPrice.STANDARD, uPName: nts.uk.resource.getText("KML002_57") }
            ]);

            self.uPCd = ko.observable(0);

            self.roundingItems = ko.observableArray([
                { roundingCd: RoundingTime.ONE_MINUTE, roundingName: nts.uk.resource.getText("KML002_53") },
                { roundingCd: RoundingTime.FIVE_MINS, roundingName: nts.uk.resource.getText("KML002_54") },
                { roundingCd: RoundingTime.SIX_MINS, roundingName: nts.uk.resource.getText("KML002_55") },
                { roundingCd: RoundingTime.TEN_MINS, roundingName: nts.uk.resource.getText("KML002_56") },
                { roundingCd: RoundingTime.FIFTEEN_MINS, roundingName: nts.uk.resource.getText("KML002_54") },
                { roundingCd: RoundingTime.TWENTY_MINS, roundingName: nts.uk.resource.getText("KML002_55") },
                { roundingCd: RoundingTime.THIRTY_MINS, roundingName: nts.uk.resource.getText("KML002_56") },
                { roundingCd: RoundingTime.SIXTY_MINS, roundingName: nts.uk.resource.getText("KML002_57") }
            ]);

            self.roundingCd = ko.observable(0);
            self.processingList = ko.observableArray([
                { code: '0', name: nts.uk.resource.getText("Enum_Rounding_Down") },
                { code: '1', name: nts.uk.resource.getText("Enum_Rounding_Up") }
            ]);

            self.selectedProcessing = ko.observable(0);

            if (self.catCode() === 0) {
                self.enable(true);
            } else {
                self.enable(false);
            }

            var devChange = false;

            if (self.currentData != null) {
                if (self.unitSelect() == 0) {
                    if (self.currentData.calMethodAtr == 0) {
                        self.checked(self.currentData.actualDisplayAtr == 0 ? false : true);
                        self.bindData(self.currentData.lstTimeUnitFuncs);
                    } else {
                        self.checkedTime(self.currentData.actualDisplayAtrTime);
                        self.selectedMethod(self.currentData.calMethodAtr == 0 ? false : true);
                        self.catCode(self.currentData.categoryIndicatorTime == 0 ? +false : +true);
                        self.bindDataMoney(self.currentData.lstMoney);
                    }
                } else {
                    self.checkedAmount(self.currentData.actualDisplayAtrAmount == 0 ? false : true);
                    self.selectedMethod(self.currentData.calMethodAtrAmount == 0 ? false : true);
                    self.catCodeTime(self.currentData.categoryIndicatorAmount == 0 ? +false : +true);
                    self.bindDataAmout(self.currentData.lstMoney);
                    $('.method-a').hide();
                    $('.method-b').hide();
                    $('.cal-method-selection').hide();
                    $('.method-c').show();
                }

            }

            self.checked.subscribe(function(value) {
                if (!devChange) {
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_194" }).ifYes(() => {
                        devChange = false;

                        self.displayItemsRule(self.allItem(), value);
                        self.rightItems.removeAll();
                        $("#treegridItems").ntsGridList('deselectAll');
                    }).ifNo(() => {
                        devChange = true;

                        if (value) {
                            self.checked(false);
                            return;
                        } else {
                            self.checked(true);
                            return;
                        }
                    })
                }

                devChange = false;
            });
            self.checkedAmount.subscribe(function(value) {
                if (!devChange) {
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_194" }).ifYes(() => {
                        devChange = false;

                        self.displayItemsRuleAmount(self.allItemAmount(), self.catCode(), value);
                        self.rightItemsAmount.removeAll();
                        $("#treegridItemsB").ntsGridList('deselectAll');
                    }).ifNo(() => {
                        devChange = true;

                        if (value) {
                            self.checked(false);
                            return;
                        } else {
                            self.checked(true);
                            return;
                        }
                    })
                }

                devChange = false;
            });
            self.checkedTime.subscribe(function(value) {
                if (!devChange) {
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_194" }).ifYes(() => {
                        devChange = false;
                        self.displayItemsRuleTime(self.allItemTime(), self.catCodeTime(), value);
                        self.rightItemsTime.removeAll();
                        $("#treegridItemsTime").ntsGridList('deselectAll');
                    }).ifNo(() => {
                        devChange = true;

                        if (value) {
                            self.checked(false);
                            return;
                        } else {
                            self.checked(true);
                            return;
                        }
                    })
                }

                devChange = false;
            });

            self.catCode.subscribe(function(value) {
                if (!devChange) {
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_193" }).ifYes(() => {
                        devChange = false;

                        if (value == 0) {
                            self.displayItemsRuleAmount(self.allItemAmount(), value, self.checkedAmount());
                            self.rightItemsAmount.removeAll();
                        } else {
                            self.itemsAmount(_.filter(self.allItemAmount(), ['itemType', GrantPeriodicMethod.EXTERNAL]));
                            self.rightItemsAmount.removeAll();
                        }

                        $("#treegridItemsAmount").ntsGridList('deselectAll');
                    }).ifNo(() => {
                        devChange = true;

                        if (value == 0) {
                            self.catCode(1);
                            return;
                        } else {
                            self.catCode(0);
                            return;
                        }
                    })
                }

                devChange = false;
            });

            self.catCodeTime.subscribe(function(value) {
                if (!devChange) {
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_193" }).ifYes(() => {
                        devChange = false;

                        if (value == 0) {
                            self.displayItemsRuleTime(self.allItemTime(), value, self.checkedTime());
                            self.rightItemsTime.removeAll();
                        } else {
                            self.itemsTime(_.filter(self.allItemTime(), ['itemType', GrantPeriodicMethod.EXTERNAL]));
                            self.rightItemsTime.removeAll();
                        }

                        $("#treegridItemsTime").ntsGridList('deselectAll');
                    }).ifNo(() => {
                        devChange = true;

                        if (value == 0) {
                            self.catCodeTime(1);
                            return;
                        } else {
                            self.catCodeTime(0);
                            return;
                        }
                    })
                }

                devChange = false;
            });


            self.enableReturn = ko.observable(true);
            self.enableReturnAmount = ko.observable(true);
            self.enableReturnTime = ko.observable(true);

            if (self.rightItems().length >= 1 || self.rightItemsAmount().length >= 1) {
                self.enableReturn(true);
                self.enableReturnAmount(true);
            } else {
                self.enableReturn(false);
                self.enableReturnAmount(true);
            }
        }

        /**
         * Start page.
         */
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            if (self.unitSelect() == 0) {
                if (self.selectedMethod() == 0) {
                    $.when(self.formulaTimeUnit()).done(function() {

                        if (self.allItem().length > 0) {
                            self.displayItemsRule(_.clone(self.allItem()), self.checked());
                            self.bindData(self.currentData.lstTimeUnitFuncs);
                        }

                    }).fail(function(res) {
                        dfd.reject(res);
                    });
                } else {
                    $.when(self.formulaTime()).done(function() {

                        if (self.allItemTime().length > 0) {
                            self.displayItemsRuleTime(_.clone(self.allItemTime()), self.catCodeTime(), self.checkedTime());
                            self.bindDataMoney(self.currentData.lstMoney);
                        }
                    }).fail(function(res) {
                        dfd.reject(res);
                    });
                }
            } else if (self.unitSelect() == 1) {
                $.when(self.getData()).done(function() {

                    if (self.allItemAmount().length > 0) {
                        self.displayItemsRuleAmount(_.clone(self.allItemAmount()), self.catCode(), self.checkedAmount());
                        self.bindDataAmout(self.currentData.lstMoney);
                    }
                }).fail(function(res) {
                    dfd.reject(res);
                });
            };
            dfd.resolve();
            return dfd.promise();
        } l

        submit() {
            var self = this;
            var data = nts.uk.ui.windows.getShared("KML002_A_DATA");

            if (self.unitSelect() == 0) {
                if (self.selectedMethod() == 0) {
                    if (self.rightItems().length == 0) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_129" });
                        return;
                    }
                } else {
                    if (self.rightItemsTime().length == 0) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_129" });
                        return;
                    }
                }
                var formTime = [];
                var formAmount = [];

                for (var i = 0; i < self.rightItemsTime().length; i++) {
                    var unitTime = {
                        verticalCalCd: data.verticalCalCd,
                        verticalCalItemId: data.itemId,
                        dispOrderTime: self.rightItemsTime()[i].id,
                        externalBudgetCd: self.rightItemsTime()[i].itemType == GrantPeriodicMethod.EXTERNAL ? self.rightItemsTime()[i].trueCode : null,
                        attendanceItemIdTime: self.rightItemsTime()[i].itemType == GrantPeriodicMethod.DAILY ? self.rightItemsTime()[i].trueCode : null,
                        presetItemIdTime: self.rightItemsTime()[i].itemType == GrantPeriodicMethod.SCHEDULE ? self.rightItemsTime()[i].trueCode : null,
                        operatorAtrTime: self.rightItemsTime()[i].operatorAtr == nts.uk.resource.getText("KML002_37") ? 0 : 1
                    }
                    formTime.push(unitTime);
                }
                for (var i = 0; i < self.rightItems().length; i++) {
                    var unitAmount = {
                        verticalCalCd: data.verticalCalCd,
                        verticalCalItemId: data.itemId,
                        dispOrder: self.rightItems()[i].id,
                        attendanceItemId: self.rightItems()[i].itemType == GrantPeriodicMethod.DAILY ? self.rightItems()[i].trueCode : null,
                        presetItemId: self.rightItems()[i].itemType == GrantPeriodicMethod.SCHEDULE ? self.rightItems()[i].trueCode : null,
                        operatorAtr: self.rightItems()[i].operatorAtr == nts.uk.resource.getText("KML002_37") ? 0 : 1,
                        name: self.rightItems()[i].name

                    }
                    formAmount.push(unitAmount);
                }

                var formulaAmount = {
                    verticalCalCd: data.verticalCalCd,
                    verticalCalItemId: data.itemId,
                    calMethodAtr: self.selectedMethod(),
                    moneyFunc: {
                        categoryIndicatorTime: self.catCodeTime(),
                        actualDisplayAtrTime: self.checkedTime() ? 1 : 0,
                        lstMoney: formTime
                    },
                    timeUnit: {
                        roundingTime: self.roundingCd(),
                        roundingAtr: self.selectedProcessing(),
                        unitPrice: self.uPCd(),
                        actualDisplayAtr: self.checked() ? 1 : 0,
                        lstTimeUnitFuncs: formAmount
                    }
                }

                nts.uk.ui.windows.setShared("KML002_E_DATA", formulaAmount);
            } else {

                if (self.rightItemsAmount().length == 0) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_129" });
                    return;
                }
                var formMoney = [];

                for (var i = 0; i < self.rightItemsAmount().length; i++) {
                    var unitMoney = {
                        verticalCalCd: data.verticalCalCd,
                        verticalCalItemId: data.itemId,
                        dispOrderAmount: self.rightItemsAmount()[i].id,
                        externalBudgetCdAmount: self.rightItemsAmount()[i].itemType == GrantPeriodicMethod.EXTERNAL ? self.rightItemsAmount()[i].trueCode : null,
                        attendanceItemIdAmount: self.rightItemsAmount()[i].itemType == GrantPeriodicMethod.DAILY ? self.rightItemsAmount()[i].trueCode : null,
                        presetItemIdAmount: self.rightItemsAmount()[i].itemType == GrantPeriodicMethod.SCHEDULE ? self.rightItemsAmount()[i].trueCode : null,
                        operatorAtrAmount: self.rightItemsAmount()[i].operatorAtr == nts.uk.resource.getText("KML002_37") ? 0 : 1
                    }
                    formMoney.push(unitMoney);
                }
                var moneyData = {
                    verticalCalCd: data.verticalCalCd,
                    verticalCalItemId: data.itemId,
                    categoryIndicator: self.catCode(),
                    actualDisplayAtr: self.checkedAmount() ? 1 : 0,
                    lstMoney: formMoney
                }
                nts.uk.ui.windows.setShared("KML002_E_DATA", moneyData);
            }

            nts.uk.ui.windows.close();
        }

        cancel() {
            var self = this;
            nts.uk.ui.windows.close();

        }


        getData(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();

            var data = nts.uk.ui.windows.getShared("KML002_A_DATA");
            let array = [];
            let i = 2;
            let param = {
                //BUDGET_ATR
                budgetAtr: 1,
                //UNIT_ATR
                unitAtr: 0
            }
            service.getByAtr(param).done((lst) => {
                let sortedData = _.orderBy(lst, ['externalBudgetCode'], ['asc']);
                _.map(sortedData, function(item: any) {
                    array.push({
                        id: i,
                        code: item.externalBudgetCode,
                        name: item.externalBudgetName + nts.uk.resource.getText("KML002_44"),
                        itemType: GrantPeriodicMethod.EXTERNAL
                    })
                    i = i + 1;
                });

                let sortedLst = _.orderBy(array, ['id'], ['asc']);
                self.itemsAmount(sortedLst);
                self.allItemAmount(sortedLst);
                self.listBudget(sortedLst);
                dfd.resolve();
            })

            return dfd.promise();
        }
        formulaTimeUnit(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.allItem([]);

            var data = nts.uk.ui.windows.getShared("KML002_A_DATA");
            var dailyAttendanceAtrs = [];
            dailyAttendanceAtrs.push(5);
            var param = {
                dailyAttendanceItemAtrs: dailyAttendanceAtrs,
                scheduleAtr: data.attributeId,
                budgetAtr: data.attributeId,
                unitAtr: 0
            };
            service.getDailyItems(param).done(function(data) {
                let temp = [];

                let items = _.sortBy(data, ['companyId', 'dispOrder']);

                _.forEach(items, function(item: service.BaseItemsDto) {
                    var name = item.itemName + nts.uk.resource.getText("KML002_43");
                    temp.push(new ItemModel(item.id, name, item.itemType));
                });
                self.allItem(temp);
                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);
            });

            return dfd.promise();
        }
        formulaTime(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.allItemTime([]);

            var data = nts.uk.ui.windows.getShared("KML002_A_DATA");
            var dailyAttendanceAtrs = [];
            dailyAttendanceAtrs.push(3);
            var param = {
                dailyAttendanceItemAtrs: dailyAttendanceAtrs,
                scheduleAtr: data.attributeId,
                budgetAtr: data.attributeId,
                unitAtr: 0
            };
            service.getDailyItems(param).done(function(data) {
                let temp = [];

                let items = _.sortBy(data, ['companyId', 'dispOrder']);

                _.forEach(items, function(item: service.BaseItemsDto) {
                    var name = item.itemName + nts.uk.resource.getText("KML002_43");
                    temp.push(new ItemModel(item.id, name, item.itemType));
                });

                self.allItemTime(temp);

                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);
            });

            return dfd.promise();
        }
        getDataRight() {
            var self = this;
            var data = nts.uk.ui.windows.getShared("KML002_A_DATA");
            _.forEach(data, function(res: NewItemModel) {

                self.rightItemsAmount.push(new NewItemModel(res.code, res.operatorAtr, res.name, res.id));
            });
        }

        /**
         * Display When unitSelect() == 1
         */
        displayItemsRuleAmount(allItemAmount: any, category: number, display: boolean) {
            let self = this;
            let temp = [];
            if (category == CategoryIndicator.EXTERNAL_BUDGET_RECORD_ITEMS && display) {
                self.itemsAmount(_.filter(allItemAmount, ['itemType', GrantPeriodicMethod.SCHEDULE]));
            } else if (category == CategoryIndicator.ATTENDANCE_ITEM && display) {
                self.itemsAmount(_.filter(allItemAmount, ['itemType', GrantPeriodicMethod.SCHEDULE]));
            } else {
                self.itemsAmount(_.filter(allItemAmount, function(item: ItemModel) {
                    return item.itemType == GrantPeriodicMethod.DAILY || item.itemType == GrantPeriodicMethod.SCHEDULE;
                }));
            }
        }

        /**
         * Display When selectedMethod() == 0
         */
        displayItemsRule(allItem: any, display: boolean) {
            let self = this;
            let temp = [];

            if (display) {
                self.items(_.filter(allItem, ['itemType', GrantPeriodicMethod.SCHEDULE]));
            } else {
                self.items(_.filter(allItem, function(item: ItemModel) {
                    return item.itemType == GrantPeriodicMethod.DAILY || item.itemType == GrantPeriodicMethod.SCHEDULE;
                }));

            }
        }

        displayItemsRuleTime(allItemTime: any, category: number, display: boolean) {
            let self = this;
            let temp = [];
            if (category == CategoryIndicator.EXTERNAL_BUDGET_RECORD_ITEMS && display) {
                self.itemsTime(_.filter(allItemTime, ['itemType', GrantPeriodicMethod.SCHEDULE]));
            } else if (category == CategoryIndicator.ATTENDANCE_ITEM && display) {
                self.itemsTime(_.filter(allItemTime, ['itemType', GrantPeriodicMethod.SCHEDULE]));
            } else {
                self.itemsTime(_.filter(allItemTime, function(item: ItemModel) {
                    return item.itemType == GrantPeriodicMethod.DAILY || item.itemType == GrantPeriodicMethod.SCHEDULE;
                }));
            }
        }


        /**
         * Addition function.
         */
        addition() {
            let self = this;

            if (self.currentCodeList().length + self.rightItems().length > 100) {
                nts.uk.ui.dialog.info({ messageId: "Msg_195" });
            } else {
                _.forEach(self.currentCodeList(), function(sonCute) {
                    var item = _.find(self.items(), function(o) { return o.code == sonCute; });

                    let i = self.rightItems().length;

                    self.rightItems.push({
                        code: i.toString(),
                        trueCode: item.code.slice(0, -1),
                        itemType: item.itemType,
                        operatorAtr: nts.uk.resource.getText("KML002_37"),
                        name: item.name,
                        id: self.rightItems().length + 1
                    });

                    i = i + 1;
                });
            }

            if (self.rightItems().length >= 1) {
                self.enableReturn(true);
            } else {
                self.enableReturn(false);
            }
        }

        /**
         * Subtraction function.
         */
        subtraction() {
            let self = this;

            if (self.currentCodeList().length + self.rightItems().length > 100) {
                nts.uk.ui.dialog.info({ messageId: "Msg_195" });
            } else {
                _.forEach(self.currentCodeList(), function(sonCute) {
                    var item = _.find(self.items(), function(o) { return o.code == sonCute; });

                    let i = self.rightItems().length;

                    self.rightItems.push({
                        code: i.toString(),
                        trueCode: item.code.slice(0, -1),
                        itemType: item.itemType,
                        operatorAtr: nts.uk.resource.getText("KML002_38"),
                        name: item.name,
                        id: self.rightItems().length + 1
                    });

                    i = i + 1;
                });
            }

            if (self.rightItems().length >= 1) {
                self.enableReturn(true);
            } else {
                self.enableReturn(false);
            }
        }

        /**
         * Return items.
         */
        returnItems() {
            let self = this;
            let array = [];

            self.rightItems(_.filter(self.rightItems(), function(item) {
                return _.indexOf(self.currentRightCodeList(), item.code) < 0;
            }));

            for (let i = 0; i < self.rightItems().length; i++) {
                self.rightItems()[i].code = i.toString();
            }

            self.currentRightCodeList.removeAll();
            self.rightItems(self.rightItems());

            if (self.rightItems().length >= 1) {
                self.enableReturn(true);
            } else {
                self.enableReturn(false);
            }
        }

        /**
         * Addition function.
        */
        additionAmount() {
            let self = this;

            if (self.currentCodeListAmount().length + self.rightItemsAmount().length > 100) {
                nts.uk.ui.dialog.info({ messageId: "Msg_195" });
            } else {
                _.forEach(self.currentCodeListAmount(), function(sonCute) {
                    var item = _.find(self.itemsAmount(), function(o) { return o.code == sonCute; });

                    let i = self.rightItemsAmount().length;

                    self.rightItemsAmount.push({
                        code: i.toString(),
                        trueCode: item.code.slice(0, -1),
                        itemType: item.itemType,
                        operatorAtr: nts.uk.resource.getText("KML002_37"),
                        name: item.name,
                        id: self.rightItemsAmount().length + 1
                    });

                    i = i + 1;
                });
            }

            if (self.rightItemsAmount().length >= 1) {
                self.enableReturnAmount(true);
            } else {
                self.enableReturnAmount(false);
            }
        }

        /**
         * Subtraction function.
         */
        subtractionAmount() {
            let self = this;

            if (self.currentCodeListAmount().length + self.rightItemsAmount().length > 100) {
                nts.uk.ui.dialog.info({ messageId: "Msg_195" });
            } else {
                _.forEach(self.currentCodeListAmount(), function(sonCute) {
                    var item = _.find(self.itemsAmount(), function(o) { return o.code == sonCute; });

                    let i = self.rightItemsAmount().length;

                    self.rightItemsAmount.push({
                        code: i.toString(),
                        trueCode: item.code.slice(0, -1),
                        itemType: item.itemType,
                        operatorAtr: nts.uk.resource.getText("KML002_38"),
                        name: item.name,
                        id: self.rightItems().length + 1
                    });

                    i = i + 1;
                });
            }

            if (self.rightItemsAmount().length >= 1) {
                self.enableReturnAmount(true);
            } else {
                self.enableReturnAmount(false);
            }
        }

        /**
         * Return items.
         */
        returnItemsAmount() {
            let self = this;
            let array = [];

            self.rightItemsAmount(_.filter(self.rightItemsAmount(), function(item) {
                return _.indexOf(self.currentRightCodeListAmount(), item.code) < 0;
            }));

            for (let i = 0; i < self.rightItemsAmount().length; i++) {
                self.rightItemsAmount()[i].code = i.toString();
            }

            self.currentRightCodeListAmount.removeAll();
            self.rightItemsAmount(self.rightItems());

            if (self.rightItemsAmount().length >= 1) {
                self.enableReturnAmount(true);
            } else {
                self.enableReturnAmount(false);
            }
        }


        additionTime() {
            let self = this;

            if (self.currentCodeListTime().length + self.rightItemsTime().length > 100) {
                nts.uk.ui.dialog.info({ messageId: "Msg_195" });
            } else {
                _.forEach(self.currentCodeListTime(), function(sonCute) {
                    var item = _.find(self.itemsTime(), function(o) { return o.code == sonCute; });

                    let i = self.rightItemsTime().length;

                    self.rightItemsTime.push({
                        code: i.toString(),
                        trueCode: item.code.slice(0, -1),
                        itemType: item.itemType,
                        operatorAtr: nts.uk.resource.getText("KML002_37"),
                        name: item.name,
                        id: self.rightItemsTime().length + 1
                    });

                    i = i + 1;
                });
            }

            if (self.rightItemsTime().length >= 1) {
                self.enableReturnTime(true);
            } else {
                self.enableReturnTime(false);
            }
        }

        /**
         * Subtraction function.
         */
        subtractionTime() {
            let self = this;

            if (self.currentCodeListTime().length + self.rightItemsTime().length > 100) {
                nts.uk.ui.dialog.info({ messageId: "Msg_195" });
            } else {
                _.forEach(self.currentCodeListTime(), function(sonCute) {
                    var item = _.find(self.itemsTime(), function(o) { return o.code == sonCute; });

                    let i = self.rightItemsTime().length;

                    self.rightItemsTime.push({
                        code: i.toString(),
                        trueCode: item.code.slice(0, -1),
                        itemType: item.itemType,
                        operatorAtr: nts.uk.resource.getText("KML002_38"),
                        name: item.name,
                        id: self.rightItemsTime().length + 1
                    });

                    i = i + 1;
                });
            }

            if (self.rightItemsTime().length >= 1) {
                self.enableReturnTime(true);
            } else {
                self.enableReturnTime(false);
            }
        }

        /**
         * Return items.
         */
        returnItemsTime() {
            let self = this;
            let array = [];

            self.rightItemsTime(_.filter(self.rightItemsTime(), function(item) {
                return _.indexOf(self.currentRightCodeListTime(), item.code) < 0;
            }));

            for (let i = 0; i < self.rightItemsTime().length; i++) {
                self.rightItemsTime()[i].code = i.toString();
            }

            self.currentRightCodeListTime.removeAll();
            self.rightItemsTime(self.rightItems());

            if (self.rightItemsTime().length >= 1) {
                self.enableReturnTime(true);
            } else {
                self.enableReturnTime(false);
            }
        }

        bindData(lstTimeUnitFuncs: any) {
            var self = this;
            self.rightItems.removeAll();

            _.forEach(lstTimeUnitFuncs, function(item) {
                var itemCd = "";
                var realCd = "";

                if (item.attendanceItemId != null) {
                    itemCd = item.attendanceItemId + item.dispOrder;
                    realCd = item.attendanceItemId
                } else if (item.externalBudgetCd != null) {
                    itemCd = item.externalBudgetCd + item.dispOrder;
                    realCd = item.externalBudgetCd
                } else if (item.presetItemId != null) {
                    itemCd = item.presetItemId + item.dispOrder;
                    realCd = item.presetItemId
                }

                var getItemByCd = _.find(self.allItem(), function(o) { return o.code.slice(0, -1) == realCd; });
                var dataType = 0;

                if (item.presetItemId != null) {
                    dataType = GrantPeriodicMethod.SCHEDULE;
                } else if (item.attendanceItemId != null) {
                    dataType = GrantPeriodicMethod.DAILY;
                } else if (item.externalBudgetCd != null) {
                    dataType = GrantPeriodicMethod.EXTERNAL;
                }

                var itemData = {
                    code: itemCd,
                    trueCode: realCd,
                    itemType: dataType,
                    operatorAtr: item.operatorAtr == 0 ? nts.uk.resource.getText("KML002_37") : nts.uk.resource.getText("KML002_38"),
                    name: getItemByCd != null ? getItemByCd.name : "",
                    id: item.dispOrder
                };

                self.rightItems.push(itemData);
            });
        }
        bindDataMoney(lstMoney: any) {
            var self = this;
            self.rightItemsTime.removeAll();

            _.forEach(lstMoney, function(item) {
                var itemCd = "";
                var realCd = "";

                if (item.attendanceItemIdTime != null) {
                    itemCd = item.attendanceItemIdTime + item.dispOrderTime;
                    realCd = item.attendanceItemIdTime
                } else if (item.externalBudgetCd != null) {
                    itemCd = item.externalBudgetCd + item.dispOrderTime;
                    realCd = item.externalBudgetCd
                } else if (item.presetItemIdTime != null) {
                    itemCd = item.presetItemIdTime + item.dispOrderTime;
                    realCd = item.presetItemIdTime
                }

                var getItemByCd = _.find(self.allItemTime(), function(o) { return o.code.slice(0, -1) == realCd; });
                var dataType = 0;

                if (item.presetItemIdTime != null) {
                    dataType = GrantPeriodicMethod.SCHEDULE;
                } else if (item.attendanceItemIdTime != null) {
                    dataType = GrantPeriodicMethod.DAILY;
                } else if (item.externalBudgetCd != null) {
                    dataType = GrantPeriodicMethod.EXTERNAL;
                }

                var itemData = {
                    code: itemCd,
                    trueCode: realCd,
                    itemType: dataType,
                    operatorAtr: item.operatorAtrTime == 0 ? nts.uk.resource.getText("KML002_37") : nts.uk.resource.getText("KML002_38"),
                    name: getItemByCd != null ? getItemByCd.name : "",
                    id: item.dispOrderTime
                };

                self.rightItemsTime.push(itemData);
            });
        }
        bindDataAmout(lstMoney: any) {
            var self = this;
            self.rightItemsAmount.removeAll();

            _.forEach(lstMoney, function(item) {
                var itemCd = "";
                var realCd = "";

                if (item.attendanceItemIdAmount != null) {
                    itemCd = item.attendanceItemIdAmount + item.dispOrderAmount;
                    realCd = item.attendanceItemIdAmount
                } else if (item.externalBudgetCdAmount != null) {
                    itemCd = item.externalBudgetCdAmount + item.dispOrderAmount;
                    realCd = item.externalBudgetCdAmount
                } else if (item.presetItemIdAmount != null) {
                    itemCd = item.presetItemIdAmount + item.dispOrderAmount;
                    realCd = item.presetItemIdAmount
                }

                var getItemByCd = _.find(self.allItemAmount(), function(o) { return o.code.slice(0, -1) == realCd; });
                var dataType = 0;

                if (item.presetItemIdAmount != null) {
                    dataType = GrantPeriodicMethod.SCHEDULE;
                } else if (item.attendanceItemIdAmount != null) {
                    dataType = GrantPeriodicMethod.DAILY;
                } else if (item.externalBudgetCdAmount != null) {
                    dataType = GrantPeriodicMethod.EXTERNAL;
                }

                var itemData = {
                    code: itemCd,
                    trueCode: realCd,
                    itemType: dataType,
                    operatorAtr: item.operatorAtrAmount == 0 ? nts.uk.resource.getText("KML002_37") : nts.uk.resource.getText("KML002_38"),
                    name: getItemByCd != null ? getItemByCd.name : "",
                    id: item.dispOrderAmount
                };

                self.rightItemsAmount.push(itemData);
            });
        }
    }


    class ItemModel {
        code: string;
        name: string;
        itemType: number;
        constructor(code: string, name: string, itemType: number) {
            this.code = code;
            this.name = name;
            this.itemType = itemType;
        }
    }
    export class NewItemModel {
        code: string;
        operatorAtr: string;
        name: string;
        id: any
        constructor(code: string, operatorAtr: string, name: string, id: any) {
            this.code = code;
            this.operatorAtr = operatorAtr;
            this.name = name;
            this.id = id;
        }
    }
    export interface IFormulaTimeUnit {
        verticalCalCd?: String;
        verticalCalItemId?: String;
        roundingTime?: number;
        roundingAtr?: number;
        unitPrice?: number;
        actualDisplayAtr?: number;


    }

    export enum GrantPeriodicMethod {
        /* 0- 日次の勤怠項目 */
        DAILY = 0,
        /* 1- 予定項目 */
        SCHEDULE,
        /* 2- 外部予算実績項目 */
        EXTERNAL
    }
    export enum DailyAttendanceAtr {
        /* 0- 金額 */
        DAILY = 3,
        /* 1- 時間 */
        SCHEDULE = 5
    }

    export enum CategoryIndicator {
        /* 1- 勤怠項目 */
        ATTENDANCE_ITEM = 0,
        /* 0- 外部予算実績項目 */
        EXTERNAL_BUDGET_RECORD_ITEMS = 1

    }

    export enum UseAtr {
        /** 0- 利用しない **/
        DO_NOT_USE = 0,
        /** 1- 利用する **/
        USE
    }

    export enum CalMethodAtr {
        /** 1- 時間項目×単価 **/
        TIME_ITEM_X_UNIT_PRICE = 0,
        /** 0- 金額項目 **/
        AMOUNT_ITEM = 1

    }
    export enum RoundingTime {
        /** 0- 1分 **/
        ONE_MINUTE = 0,
        /** 1- 5分 **/
        FIVE_MINS = 1,
        /** 2- 6分 **/
        SIX_MINS = 2,
        /** 3- 10分 **/
        TEN_MINS = 3,
        /** 4- 15分 **/
        FIFTEEN_MINS = 4,
        /** 4- 20分 **/
        TWENTY_MINS = 5,
        /** 4- 30分 **/
        THIRTY_MINS = 6,
        /** 4- 60分 **/
        SIXTY_MINS = 7
    }
    export enum UnitPrice {

        /** 0- 単価0 **/
        UNIT_PRICE_1 = 0,
        /** 1- 単価2 **/
        UNIT_PRICE_2 = 1,
        /** 2- 単価3 **/
        UNIT_PRICE_3 = 2,
        /** 3- 契約単価 **/
        CONTRACT = 3,
        /** 4-基準単価 **/
        STANDARD = 4
    }
}