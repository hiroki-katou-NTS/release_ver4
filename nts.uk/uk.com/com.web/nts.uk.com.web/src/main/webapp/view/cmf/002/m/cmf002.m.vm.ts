module nts.uk.com.view.cmf002.m.viewmodel {
    import block = nts.uk.ui.block;
    import model = cmf002.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import close = nts.uk.ui.windows.close;
    import hasError = nts.uk.ui.errors.hasError;
    import error = nts.uk.ui.errors;
    import dialog = nts.uk.ui.dialog;
    
    export class ScreenModel {
        initInTimeDataFormatSetting: any = {
            nullValueSubs: 0,
            outputMinusAsZero: 0,
            fixedValue: 0,
            valueOfFixedValue: "",
            timeSeletion: 0,
            fixedLengthOutput: 0,
            fixedLongIntegerDigit: null,
            fixedLengthEditingMethod: 0,
            delimiterSetting: 2,
            previousDayOutputMethod: 0,
            nextDayOutputMethod: 0,
            minuteFractionDigit: null,
            decimalSelection: 0,
            minuteFractionDigitProcessCls: 1,
            valueOfNullValueSubs: ""
        };
        inTimeDataFormatSetting: KnockoutObservable<model.InTimeDataFormatSetting> = ko.observable(new model.InTimeDataFormatSetting(this.initInTimeDataFormatSetting));
        //initComponent
        inputMode: KnockoutObservable<boolean> = ko.observable(true);
        selectedValue: KnockoutObservable<any>;
        nextDaySelectList: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getNextDay());
        preDaySelectList: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getPreDay());
        timeSelectedList: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getTimeSelected());
        decimalSelectList: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getDecimalSelect());
        itemListRounding: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getRounding());
        separatorSelectList: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getSeparator());
        fixedValueOperationItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getNotUseAtr());
        formatSelectionItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getNotUseAtr());
        fixedLengthOutputItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getNotUseAtr());
        fixedLengthEditingMethodItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getFixedLengthEditingMethod());
        nullValueReplaceItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getNotUseAtr());
        fixedValueItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getNotUseAtr());
        fixedValueOperationSymbolItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray([
            new model.ItemModel(0, '+'),
            new model.ItemModel(1, '-')
        ]);

        //Defaut Mode Screen
        // 0 = Individual
        // 1 = initial
        selectModeScreen: KnockoutObservable<number> = ko.observable();

        enableRequired: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            let self = this;
        }


        sendData() {
            error.clearAll();
            let self = this;

            if (self.inTimeDataFormatSetting().fixedLengthOutput() == 1 && self.inTimeDataFormatSetting().fixedValue() == 0) {
                $("#M9_2_2").ntsError('check');
            }
            
            if (!hasError()) {
                let data = ko.toJS(self.inTimeDataFormatSetting);
                
                if(!self.inTimeDataFormatSetting().timeSeletion() == 0 || !self.inTimeDataFormatSetting().decimalSelection() == 1){
                  data.minuteFractionDigit = null;
                  data.minuteFractionDigitProcessCls = 1;
                }
                
                if(!self.inTimeDataFormatSetting().fixedLengthOutput() == 1){
                    data.fixedLongIntegerDigit = null;
                    data.fixedLengthEditingMethod = 0;  
                }
                
                if (!self.inTimeDataFormatSetting().nullValueSubs() == 1) {
                    data.valueOfNullValueSubs = "";
                }
                              
                if (self.inTimeDataFormatSetting().fixedValue() == 1) {
                    data.timeSeletion = 0;
                    data.decimalSelection = 0;
                    data.outputMinusAsZero = 0;
                    data.delimiterSetting = 1;
                    data.nextDayOutputMethod = 0;
                    data.previousDayOutputMethod = 0;
                    data.fixedLongIntegerDigit = null;
                    data.fixedLengthEditingMothed = 0;
                    data.valueOfNullValueSubs = null;
                    data.fixedLengthOutput = 0;
                    data.nullValueSubs = 0;
                }else {
                    data.valueOfFixedValue = "";
                }
                
                
                data.outputMinusAsZero = data.outputMinusAsZero ? 1 : 0;
                if (self.selectModeScreen() == model.DATA_FORMAT_SETTING_SCREEN_MODE.INIT) {
                    service.sendPerformSettingByInTime(data).done(result => {
                        dialog.info({ messageId: "Msg_15" }).then(() => {
                            close();
                        });
                    });
                } else {
                    setShared('CMF002_C_PARAMS', {formatSetting: data});
                    close();
                }
            }
        }
        //※M1　～　※M6
        enableFormatSelectionCls() {
            let self = this;
            return (self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE );
        }

        //※M2　
        enableFixedValueOperationCls() {
            let self = this;
            return (self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE );
        }
        enableFixedValueOperation() {
            let self = this;
            return (self.fixedValueOperation() == model.NOT_USE_ATR.USE && self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        //※M3
        enableFixedLengthOutputCls() {
            let self = this;
            return (self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        enableFixedLengthOutput() {
            let self = this;
            $("#M9_2_2").ntsError('clear');
            return (self.inTimeDataFormatSetting().fixedLengthOutput() == model.NOT_USE_ATR.USE && self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        //※M4
        enableNullValueReplaceCls() {
            let self = this;
            return (self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE );
        }
        enableNullValueReplace() {
            let self = this;
            return (self.inTimeDataFormatSetting().nullValueSubs() == model.NOT_USE_ATR.USE && self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        //※M5
        enableSelectTimeCls() {
            let self = this;
            return (self.inTimeDataFormatSetting().timeSeletion() == model.getTimeSelected()[0].code && self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        //※M6
        decimalSelectionCls() {
            let self = this;
            return (self.inTimeDataFormatSetting().timeSeletion() == model.getTimeSelected()[0].code && self.inTimeDataFormatSetting().decimalSelection() == model.getTimeSelected()[1].code && self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }


        enableFixedValueCls() {
            let self = this;
            return (self.inputMode);
        }
        enableFixedValue() {
            let self = this;
            return (self.inTimeDataFormatSetting().fixedValue() == model.NOT_USE_ATR.USE);
        }

        start(): JQueryPromise<any> {
            //block.invisible();
            let self = this;
            let dfd = $.Deferred();
            //Check Mode Screen 
            let params = getShared('CMF002_M_PARAMS');
            self.selectModeScreen(params.screenMode);
            if (self.selectModeScreen() == model.DATA_FORMAT_SETTING_SCREEN_MODE.INDIVIDUAL && params.formatSetting) {
                // get data shared
                self.inTimeDataFormatSetting(new model.InTimeDataFormatSetting(params.formatSetting));
                dfd.resolve();
                return dfd.promise();
            }

            self.startFindData();

            dfd.resolve();
            return dfd.promise();
        }

        startFindData() {
            let self = this;
            service.findPerformSettingByInTime().done(result => {
                if (result) {
                    self.inTimeDataFormatSetting(new model.InTimeDataFormatSetting(result));
                    return;
                }
                self.inTimeDataFormatSetting(new model.InTimeDataFormatSetting(self.initInTimeDataFormatSetting));
            });
        }

        cancelCharacterSetting() {
            close();
        }

    }
}