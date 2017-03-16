module qmm012.c.viewmodel {
    export class ScreenModel {
        //combobox
        //001
        ComboBoxItemList_C_SEL_001: KnockoutObservableArray<C_SEL_001_ComboboxItemModel>;
        selectedCode_C_SEL_001: KnockoutObservable<number>;
        //Switch
        //002 003 005 006 007 008 009 010
        roundingRules_C_002_003_005To010: KnockoutObservableArray<any>;
        selectedRuleCode_C_003: any;
        selectedRuleCode_C_005: any;
        selectedRuleCode_C_006: any;
        selectedRuleCode_C_007: any;
        selectedRuleCode_C_008: any;
        selectedRuleCode_C_009: any;
        selectedRuleCode_C_010: any;
        //011
        roundingRules_C_011: KnockoutObservableArray<any>;
        selectedRuleCode_C_011: any;
        //017
        roundingRules_C_017: KnockoutObservableArray<any>;
        selectedRuleCode_C_017: any;
        //end switch
        //radiogroup
        //004
        RadioItemList_C_004: KnockoutObservableArray<any>;
        selectedId_C_004: KnockoutObservable<number>;
        //currencyeditor_C_001
        currencyeditor_C_001: any;
        currencyeditor_C_INP_001: any;
        currencyeditor_C_INP_002: any;
        currencyeditor_C_INP_003: any;
        currencyeditor_C_INP_004: any;
        currencyeditor_C_INP_005: any;
        //shared
        groupName: KnockoutObservable<any>;
        CurrentItemPeriod: KnockoutObservable<qmm012.b.service.model.ItemPeriodModel> = ko.observable(null);
        CurrentItemSalary: KnockoutObservable<service.model.ItemSalaryModel> = ko.observable(null);
        CurrentItemMaster: KnockoutObservable<qmm012.b.service.model.ItemMasterModel> = ko.observable(null);
        CurrentLimitMny: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeHigh: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeHigh: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentMemo: KnockoutObservable<string> = ko.observable("");
        CurrentTaxAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentSocialInsAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentLaborInsAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentFixPayAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentApplyForAllEmpFlg: KnockoutObservable<number> = ko.observable(0);
        CurrentApplyForMonthlyPayEmp: KnockoutObservable<number> = ko.observable(0);
        CurrentApplyForDaymonthlyPayEmp: KnockoutObservable<number> = ko.observable(0);
        CurrentApplyForDaylyPayEmp: KnockoutObservable<number> = ko.observable(0);
        CurrentApplyForHourlyPayEmp: KnockoutObservable<number> = ko.observable(0);
        CurrentAvePayAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentLimitMnyAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentItemDisplayAtr: KnockoutObservable<number> = ko.observable(1);
        CurrentZeroDisplaySet: KnockoutObservable<number> = ko.observable(1);
        CurrentLimitMnyRefItemCd: KnockoutObservable<string> = ko.observable("");
        CurrentErrRangeHighAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeHighAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeLowAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeLowAtr: KnockoutObservable<number> = ko.observable(0);
        C_SEL_012_Selected: KnockoutObservable<boolean> = ko.observable(false);
        C_SEL_013_Selected: KnockoutObservable<boolean> = ko.observable(false);
        C_SEL_014_Selected: KnockoutObservable<boolean> = ko.observable(false);
        C_SEL_015_Selected: KnockoutObservable<boolean> = ko.observable(false);
        C_SEL_016_Selected: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            var self = this;
            self.ComboBoxItemList_C_SEL_001 = ko.observableArray([
                new C_SEL_001_ComboboxItemModel(1, '課税'),
                new C_SEL_001_ComboboxItemModel(2, '非課税(限度あり）'),
                new C_SEL_001_ComboboxItemModel(3, '非課税(限度なし）'),
                new C_SEL_001_ComboboxItemModel(4, '通勤費(手入力)'),
                new C_SEL_001_ComboboxItemModel(5, '通勤費(定期券利用)')
            ]);
            self.selectedCode_C_SEL_001 = ko.observable(1);
            //end combobox data
            //start Switch Data
            //005 006 007 008 009 010
            self.roundingRules_C_002_003_005To010 = ko.observableArray([
                { code: 0, name: '対象' },
                { code: 1, name: '対象外' }
            ]);
            self.selectedRuleCode_C_003 = ko.observable(1);
            self.selectedRuleCode_C_005 = ko.observable(1);
            self.selectedRuleCode_C_006 = ko.observable(1);
            self.selectedRuleCode_C_007 = ko.observable(1);
            self.selectedRuleCode_C_008 = ko.observable(1);
            self.selectedRuleCode_C_009 = ko.observable(1);
            self.selectedRuleCode_C_010 = ko.observable(1);
            //011
            self.roundingRules_C_011 = ko.observableArray([
                { code: 1, name: 'ゼロを表示する' },
                { code: 0, name: 'ゼロを表示しない' }
            ]);
            self.selectedRuleCode_C_011 = ko.observable(1);
            //017
            self.roundingRules_C_017 = ko.observableArray([
                { code: 0, name: '固定額' },
                { code: 1, name: '非課税限度額' },
                { code: 2, name: '個人の交通機' },
                { code: 3, name: '個人の交通用' },
            ]);
            self.selectedRuleCode_C_017 = ko.observable(1);
            //endSwitch Data

            //start radiogroup data
            //004
            self.RadioItemList_C_004 = ko.observableArray([
                new BoxModel(0, '全員一律で指定する'),
                new BoxModel(1, '給与契約形態ごとに指定する')
            ]);
            self.selectedId_C_004 = ko.observable(1);
            //end radiogroup data
            //currencyeditor_C_001
            self.currencyeditor_C_001 = {
                value: ko.observable(),
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                required: ko.observable(false),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            //C_001
            self.currencyeditor_C_INP_001 = {
                value: self.CurrentLimitMny,
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                required: ko.observable(false)
            };
            //C_002
            self.currencyeditor_C_INP_002 = {
                value: self.CurrentErrRangeHigh,
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                required: ko.observable(false)
            };
            //C_003
            self.currencyeditor_C_INP_003 = {
                value: self.CurrentAlRangeHigh,
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                required: ko.observable(false)
            };
            //C_004
            self.currencyeditor_C_INP_004 = {
                value: self.CurrentErrRangeLow,
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                required: ko.observable(false)
            };
            //C_005
            self.currencyeditor_C_INP_005 = {
                value: self.CurrentAlRangeLow,
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                required: ko.observable(false)
            };
            //end currencyeditor
            //end textarea
            self.selectedCode_C_SEL_001.subscribe(function(newValue) {
                $('#C_LBL_002').show();
                $('#C_Div_002').show();
                $('#C_BTN_003').show();
                $('#C_Div_004').show();
                switch (newValue) {
                    case 1:
                        $('#C_Div_001').hide();
                        break;
                    case 2:
                    case 3:
                    case 4:
                        $('#C_Div_001').show();
                        break;
                    case 5:
                        $('#C_Div_001').show();
                        $('#C_LBL_002').hide();
                        $('#C_Div_002').hide();
                        $('#C_Div_003').show();
                        $('#C_BTN_003').hide();
                        $('#C_Div_004').hide();
                        break;
                }
            });
            self.CurrentItemMaster.subscribe(function(ItemMaster: qmm012.b.service.model.ItemMasterModel) {
                if (ItemMaster) {
                    service.findItemSalary(ItemMaster.itemCode).done(function(ItemSalary: service.model.ItemSalaryModel) {
                        self.CurrentItemSalary(ItemSalary);
                    }).fail(function(res) {
                        // Alert message
                        alert(res);
                    });
                } else {
                    self.CurrentItemSalary(null);
                }
                self.CurrentZeroDisplaySet(ItemMaster ? ItemMaster.zeroDisplaySet : 1);
                self.C_SEL_012_Selected(ItemMaster ? ItemMaster.itemDisplayAtr == 0 ? true : false : false);
            });
            self.C_SEL_012_Selected.subscribe(function(NewValue: boolean) {
                self.CurrentItemDisplayAtr(NewValue ? 0 : 1);
            });
            self.C_SEL_013_Selected.subscribe(function(NewValue: boolean) {
                self.CurrentErrRangeHighAtr(NewValue ? 1 : 0);
            });
            self.C_SEL_014_Selected.subscribe(function(NewValue: boolean) {
                self.CurrentAlRangeHighAtr(NewValue ? 1 : 0);
            });
            self.C_SEL_015_Selected.subscribe(function(NewValue: boolean) {
                self.CurrentErrRangeLowAtr(NewValue ? 1 : 0);
            });
            self.C_SEL_016_Selected.subscribe(function(NewValue: boolean) {
                self.CurrentAlRangeLowAtr(NewValue ? 1 : 0);
            });

            //            self.CurrentItemPeriod.subscribe(function(ItemPeriod: qmm012.b.service.model.ItemPeriodModel) {
            //               
            //            });
            self.CurrentItemSalary.subscribe(function(NewValue: service.model.ItemSalaryModel) {

                self.CurrentLimitMny(NewValue ? NewValue.limitMny : 0);
                self.CurrentErrRangeHigh(NewValue ? NewValue.errRangeHigh : 0);
                self.CurrentAlRangeHigh(NewValue ? NewValue.alRangeHigh : 0);
                self.CurrentErrRangeLow(NewValue ? NewValue.errRangeLow : 0);
                self.CurrentAlRangeLow(NewValue ? NewValue.alRangeLow : 0);
                self.CurrentMemo(NewValue ? NewValue.memo : "");
                self.CurrentTaxAtr(NewValue ? NewValue.taxAtr : 0);
                self.CurrentSocialInsAtr(NewValue ? NewValue.socialInsAtr : 0);
                self.CurrentLaborInsAtr(NewValue ? NewValue.laborInsAtr : 0);
                self.CurrentFixPayAtr(NewValue ? NewValue.fixPayAtr : 0);
                self.CurrentApplyForAllEmpFlg(NewValue ? NewValue.applyForAllEmpFlg : 0);
                self.CurrentApplyForMonthlyPayEmp(NewValue ? NewValue.applyForMonthlyPayEmp : 0);
                self.CurrentApplyForDaymonthlyPayEmp(NewValue ? NewValue.applyForDaymonthlyPayEmp : 0);
                self.CurrentApplyForDaylyPayEmp(NewValue ? NewValue.applyForDaylyPayEmp : 0);
                self.CurrentApplyForHourlyPayEmp(NewValue ? NewValue.applyForHourlyPayEmp : 0);
                self.CurrentAvePayAtr(NewValue ? NewValue.avePayAtr : 0);
                self.CurrentLimitMnyAtr(NewValue ? NewValue.limitMnyAtr : 0);
                self.CurrentLimitMnyRefItemCd(NewValue ? NewValue.limitMnyRefItemCd : "");


                self.C_SEL_013_Selected(NewValue ? NewValue.errRangeHighAtr == 0 ? false : true : false);
                self.C_SEL_014_Selected(NewValue ? NewValue.alRangeHighAtr == 0 ? false : true : false);
                self.C_SEL_015_Selected(NewValue ? NewValue.errRangeLowAtr == 0 ? false : true : false);
                self.C_SEL_016_Selected(NewValue ? NewValue.alRangeLowAtr == 0 ? false : true : false);


            });

            self.CurrentLimitMnyAtr.subscribe(function(newValue) {
                $('#C_Div_002').hide();
                $('#C_Div_003').hide();
                switch (newValue) {
                    case 0:
                    case 1:
                        $('#C_Div_002').show();
                        break;
                    case 2:
                    case 3:
                        $('#C_Div_003').show();
                        break;
                }

            });
        }

        openKDialog() {
            nts.uk.ui.windows.sub.modal('../k/index.xhtml', { height: 490, width: 330, dialogClass: "no-close" }).onClosed(function(): any {
            });
        }

        openHDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('itemMaster' , self.CurrentItemMaster());
            nts.uk.ui.windows.sub.modal('../h/index.xhtml', { height: 570, width: 735, dialogClass: "no-close" }).onClosed(function(): any {
            });
        }

        openIDialog() {
            nts.uk.ui.windows.sub.modal('../i/index.xhtml', { height: 600, width: 1015, dialogClass: "no-close" }).onClosed(function(): any {
            });
        }
    }



    class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            this.id = id;
            this.name = name;
        }
    }

    class GridItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    class C_SEL_001_ComboboxItemModel {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }
}