module nts.uk.com.view.cmf002.j.viewmodel {

    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import model = cmf002.share.model;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import hasError = nts.uk.ui.errors.hasError;
    import error = nts.uk.ui.errors;
    import dialog = nts.uk.ui.dialog;

    export class ScreenModel {
        characterDataFormatSetting: KnockoutObservable<model.CharacterDataFormatSetting> = ko.observable(new model.CharacterDataFormatSetting({
            effectDigitLength: 0,
            startDigit: null,
            endDigit: null,
            cdEditting: 0,
            cdEditDigit: null,
            cdEdittingMethod: null,
            spaceEditting: 0,
            cdConvertCd: "",
            cdConvertName: "",
            nullValueReplace: 0,
            valueOfNullValueReplace: "",
            fixedValue: 0,
            valueOfFixedValue: ""

        }));
        effectDigitLengthItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray([
            new model.ItemModel(model.FORMAT_SELECTION.DECIMAL, getText('CMF002_165')),
            new model.ItemModel(model.FORMAT_SELECTION.NO_DECIMAL, getText('CMF002_166'))
        ]);
        codeEditingItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray([
            new model.ItemModel(model.NOT_USE_ATR.USE, getText('CMF002_149')),
            new model.ItemModel(model.NOT_USE_ATR.NOT_USE, getText('CMF002_150'))
        ]);
        nullValueReplaceItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray([
            new model.ItemModel(model.NOT_USE_ATR.USE, getText('CMF002_149')),
            new model.ItemModel(model.NOT_USE_ATR.NOT_USE, getText('CMF002_150'))
        ]);
        fixedValueItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray([
            new model.ItemModel(model.NOT_USE_ATR.USE, getText('CMF002_149')),
            new model.ItemModel(model.NOT_USE_ATR.NOT_USE, getText('CMF002_150'))
        ]);
        codeEditingMethodItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray(model.getFixedLengthEditingMethod());
        spaceEditingItem: KnockoutObservableArray<model.ItemModel> = ko.observableArray([
            new model.ItemModel(0, getText('CMF002_395')),
            new model.ItemModel(1, getText('CMF002_396')),
            new model.ItemModel(2, getText('CMF002_397'))
        ]);

        modeScreen: KnockoutObservable<number> = ko.observable(0);
        isEnable: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            var self = this;

        }

        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            let params = getShared('CMF002_J_PARAMS');
            self.modeScreen(params.screenMode);
            if (self.modeScreen() == model.DATA_FORMAT_SETTING_SCREEN_MODE.INDIVIDUAL && params.formatSetting) {
                // get data shared
                self.characterDataFormatSetting(new model.CharacterDataFormatSetting(params.formatSetting));
            } else {
                service.getCharacterDataFormatSetting().done(result => {
                    if (result) {
                        self.characterDataFormatSetting(new model.CharacterDataFormatSetting(result));
                    }
                });
            }
            dfd.resolve();
            return dfd.promise();
        }

        saveCharacterSetting() {
            error.clearAll();
            let self = this;
            let command = ko.toJS(self.characterDataFormatSetting);
            if (self.characterDataFormatSetting().effectDigitLength() != model.NOT_USE_ATR.USE) {
                $('#J2_2_1').ntsError('clear');
                $('#J2_2_3').ntsError('clear');
                command.startDigit = null;
                command.endDigit = null;
            }
            if (self.characterDataFormatSetting().cdEditting() != model.NOT_USE_ATR.USE) {
                $('#J3_2_1').ntsError('clear');
                command.cdEditDigit = null;
                command.spaceEditting = null;
            }
            if (self.characterDataFormatSetting().nullValueReplace() != model.NOT_USE_ATR.USE) {
                command.valueOfNullValueReplace = null;
            }
            if (self.characterDataFormatSetting().fixedValue() != model.NOT_USE_ATR.USE) {
                command.valueOfFixedValue = null;
            }
            if (self.characterDataFormatSetting().startDigit() == null && (self.characterDataFormatSetting().effectDigitLength() == model.NOT_USE_ATR.USE) && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE) {
                $("#J2_2_1").ntsError('check');
            }
            if (self.characterDataFormatSetting().endDigit() == null && (self.characterDataFormatSetting().effectDigitLength() == model.NOT_USE_ATR.USE) && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE) {
                $("#J2_2_3").ntsError('check');
            }
            if (self.characterDataFormatSetting().cdEditDigit() == null && (self.characterDataFormatSetting().effectDigitLength() == model.NOT_USE_ATR.USE) && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE) {
                $("#J3_2_1").ntsError('check');
            }
            if (!hasError()) {

                if (self.characterDataFormatSetting().startDigit() > self.characterDataFormatSetting().endDigit()) {
                    $("#J2_2_1").ntsError('set', { messageId: "Msg_830" });
                    $('#J2_2_3').ntsError('set', { messageId: "Msg_830" });
                } else {
                    if (self.modeScreen() != model.DATA_FORMAT_SETTING_SCREEN_MODE.INDIVIDUAL) {
                        // get data shared
                        service.setCharacterDataFormatSetting(command).done(function() {
                            dialog.info({ messageId: "Msg_15" }).then(() => {
                                nts.uk.ui.windows.close();
                            });
                        });
                    } else {
                        setShared('CMF002_C_PARAMS', { formatSetting: command });
                        nts.uk.ui.windows.close();
                    }
                }
            }
        }
        //
        enableEffectDigitLength() {
            var self = this;
            if (self.characterDataFormatSetting().effectDigitLength() == model.NOT_USE_ATR.USE && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE) {
                return true;
            } else {
                $('#J2_2_1').ntsError('clear');
                $('#J2_2_3').ntsError('clear');
                return false;
            }
        }
        enableCodeEditing() {
            var self = this;
            if (self.characterDataFormatSetting().cdEditting() == model.NOT_USE_ATR.USE && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE) {
                return true;
            } else {
                $('#J3_2_1').ntsError('clear');
                return false;
            }
        }
        enableNullValueReplace() {
            var self = this;
            return (self.characterDataFormatSetting().nullValueReplace() == model.NOT_USE_ATR.USE && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        enableFixedValue() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.USE);
        }
        nullValueReplaceItemcls() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE)
        }
        enableSpaceEditting() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE)
        }
        enableSpaceEdditingCls() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE)
        }
        CdEditting() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE)
        }
        effectDigitLengthCls() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE)
        }
        open002_V2() {
            var self = this;
            nts.uk.ui.windows.sub.modal("/view/cmf/002/v2/index.xhtml").onClosed(() => {
                let params = getShared('CMF002_J_PARAMS');
                self.cdConvertCd(params.outputCodeConvert.convertCode);
                self.cdConvertName(params.outputCodeConvert.convertName);
            });
        }

        cancelCharacterSetting() {
            nts.uk.ui.windows.close();
        }
    }
}




