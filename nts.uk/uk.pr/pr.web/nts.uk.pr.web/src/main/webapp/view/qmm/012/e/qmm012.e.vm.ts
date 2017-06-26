module qmm012.e.viewmodel {
    export class ScreenModel {
        enable: KnockoutObservable<boolean>;
        Currencyeditor_ErrorUpper: any;
        Currencyeditor_AlarmUpper: any;
        Currencyeditor_ErrorLower: any;
        Currencyeditor_AlarmLower: any;
        //E_004
        Checked_NoDisplay: KnockoutObservable<boolean> = ko.observable(false);
        //E_005
        Checked_ErrorUpper: KnockoutObservable<boolean> = ko.observable(false);
        //E_006
        Checked_AlarmHigh: KnockoutObservable<boolean> = ko.observable(false);
        //E_007
        Checked_ErrorLower: KnockoutObservable<boolean> = ko.observable(false);
        //E_008
        Checked_AlarmLower: KnockoutObservable<boolean> = ko.observable(false);
        //E_001To003
        Roundingrules_TimeNumberClassification: KnockoutObservableArray<any>;
        Roundingrules_WorkingDaysPerYear: KnockoutObservableArray<any>;
        Roundingrules_ZeroDisplay: KnockoutObservableArray<any>;
        selected_ZeroDisplay: any;
        CurrentItemMaster: KnockoutObservable<qmm012.b.service.model.ItemMaster> = ko.observable(null);
        CurrentItemAttend: KnockoutObservable<service.model.ItemAttend> = ko.observable(null);
        CurrentAvePayAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentItemAtr: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentErrRangeHigh: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeLow: KnockoutObservable<number> = ko.observable(0);
        CurrentAlRangeHigh: KnockoutObservable<number> = ko.observable(0);
        CurrentWorkDaysScopeAtr: KnockoutObservable<number> = ko.observable(1);
        CurrentMemo: KnockoutObservable<string> = ko.observable("");
        CurrentZeroDisplaySet: KnockoutObservable<number> = ko.observable(1);
        CurrentItemDisplayAtr: KnockoutObservable<number> = ko.observable(1);
        noDisplayNames_Enable: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            let self = this;
            //E_001 To 003
            //E_001To003
            self.Roundingrules_TimeNumberClassification = ko.observableArray([
                { code: 0, name: '時間' },
                { code: 1, name: '回数' }
            ]);
            self.Roundingrules_WorkingDaysPerYear = ko.observableArray([
                { code: 1, name: '対象' },
                { code: 0, name: '対象外' }
            ]);
            self.Roundingrules_ZeroDisplay = ko.observableArray([
                { code: 1, name: 'ゼロを表示する' },
                { code: 0, name: 'ゼロを表示しない' }
            ]);
            self.enable = ko.observable(false);
            //E_001
            self.Currencyeditor_ErrorUpper = {
                value: self.CurrentErrRangeHigh,
                constraint: 'ErrRangeHigh',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable : self.Checked_ErrorUpper
            };
            //E_002
            self.Currencyeditor_AlarmUpper = {
                value: self.CurrentAlRangeHigh,
                constraint: 'AlRangeHigh',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable : self.Checked_AlarmHigh
            };
            //E_003
            self.Currencyeditor_ErrorLower = {
                value: self.CurrentErrRangeLow,
                constraint: 'ErrRangeLow',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable : self.Checked_ErrorLower
            };
            //E_004
            self.Currencyeditor_AlarmLower = {
                value: self.CurrentAlRangeLow,
                constraint: 'AlRangeLow',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    currencyformat: "JPY",
                    currencyposition: 'right'
                })),
                enable : self.Checked_AlarmLower
            };
            self.CurrentItemAttend.subscribe(function(ItemAttend: service.model.ItemAttend) {
                self.CurrentAvePayAtr(ItemAttend ? ItemAttend.avePayAtr : 0);
                self.CurrentItemAtr(ItemAttend ? ItemAttend.itemAtr : 0);
                self.CurrentErrRangeLow(ItemAttend ? ItemAttend.errRangeLow : 0);
                self.CurrentErrRangeHigh(ItemAttend ? ItemAttend.errRangeHigh : 0);
                self.CurrentAlRangeLow(ItemAttend ? ItemAttend.alRangeLow : 0);
                self.CurrentAlRangeHigh(ItemAttend ? ItemAttend.alRangeHigh : 0);
                self.CurrentWorkDaysScopeAtr(ItemAttend ? ItemAttend.workDaysScopeAtr : 1);
                self.CurrentMemo(ItemAttend ? ItemAttend.memo : "");
                self.Checked_ErrorUpper(ItemAttend ? ItemAttend.errRangeHighAtr == 0 ? false : true : false);
                self.Checked_AlarmHigh(ItemAttend ? ItemAttend.errRangeLowAtr == 0 ? false : true : false);
                self.Checked_ErrorLower(ItemAttend ? ItemAttend.alRangeHighAtr == 0 ? false : true : false);
                self.Checked_AlarmLower(ItemAttend ? ItemAttend.alRangeLowAtr == 0 ? false : true : false);
            });
            self.Checked_NoDisplay.subscribe(function(NewValue) {
                self.CurrentItemDisplayAtr(NewValue ? 0 : 1);
            });
            self.CurrentZeroDisplaySet.subscribe(function(newValue) {
                if (newValue == 0) {
                    self.noDisplayNames_Enable(true);
                } else {
                    self.noDisplayNames_Enable(false);
                }
            });
        }
        loadData(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred<any>();
            self.CurrentItemMaster(itemMaster);
            if (itemMaster.itemCode) {
                self.loadItemAttend(itemMaster).done(function() {
                    dfd.resolve();
                });
            } else {
                self.clearContent();
                dfd.resolve();
            }
            self.CurrentZeroDisplaySet(itemMaster ? itemMaster.zeroDisplaySet : 1);
            self.Checked_NoDisplay(itemMaster ? itemMaster.itemDisplayAtr == 0 ? true : false : false);
            return dfd.promise();
        }
        clearContent() {
            let self = this;
            self.CurrentAvePayAtr(0);
            self.CurrentItemAtr(0);
            self.CurrentErrRangeLow(0);
            self.CurrentErrRangeHigh(0);
            self.CurrentAlRangeLow(0);
            self.CurrentZeroDisplaySet(1);
            self.CurrentAlRangeHigh(0);
            self.CurrentWorkDaysScopeAtr(1);
            self.CurrentMemo("");
            self.Checked_ErrorUpper(false);
            self.Checked_AlarmHigh(false);
            self.Checked_ErrorLower(false);
            self.Checked_AlarmLower(false);

        }
        loadItemAttend(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred<any>();
            service.findItemAttend(itemMaster.itemCode).done(function(ItemAttend: service.model.ItemAttend) {
                self.CurrentItemAttend(ItemAttend);
                dfd.resolve(ItemAttend);
            }).fail(function(res) {
                // Alert message
                nts.uk.ui.dialog.alert(res);
            });
            return dfd.promise();
        }
        getCurrentItemAttend() {
            //return Item Attend customer input on form
            let self = this;
            let itemAttend = new service.model.ItemAttend(
                self.CurrentAvePayAtr(),
                self.CurrentItemAtr(),
                self.Checked_AlarmHigh() ? 1 : 0,
                self.CurrentErrRangeLow(),
                self.Checked_ErrorUpper() ? 1 : 0,
                self.CurrentErrRangeHigh(),
                self.Checked_AlarmLower() ? 1 : 0,
                self.CurrentAlRangeLow(),
                self.Checked_ErrorLower() ? 1 : 0,
                self.CurrentAlRangeHigh(),
                self.CurrentWorkDaysScopeAtr(),
                self.CurrentMemo());
            return itemAttend;
        }

    }


}