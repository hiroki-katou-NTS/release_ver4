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

    export class ScreenModel {
        characterDataFormatSetting: KnockoutObservable<model.CharacterDataFormatSetting> = ko.observable(new model.CharacterDataFormatSetting(0, null, null, 0, null, null, null, null, 0, "", 0, ""));
        effectDigitLengthItem: KnockoutObservableArray<model.ItemModel>;
        codeEditingItem: KnockoutObservableArray<model.ItemModel>;
        nullValueReplaceItem: KnockoutObservableArray<model.ItemModel>;
        fixedValueItem: KnockoutObservableArray<model.ItemModel>;
        codeEditingMethodItem: KnockoutObservableArray<model.ItemModel>;
        spaceEditingItem: KnockoutObservableArray<model.ItemModel>;
        codeConvertCode: KnockoutObservable<model.AcceptanceCodeConvert>;
        dispConvertName: KnockoutObservable<string>;
        modeScreen: KnockoutObservable<number> = ko.observable(0);
        isEnable: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            var self = this;
            self.initComponent();
            self.validate();
        }

        initComponent() {
            var self = this;
            let parrams = getShared('CMF002CParams');
            self.modeScreen(parrams.modeScreen);
            self.codeConvertCode = ko.observable(new model.AcceptanceCodeConvert("", "", 0));
            self.dispConvertName = ko.observable(self.codeConvertCode().convertCode() + self.codeConvertCode().convertName());
            service.getCharacterDataFormatSetting().done(function(data: Array<any>) {
                if (data && data.length) {
                    let _rsList: Array<model.characterDataFormatSetting> = _.map(data, rs => {
                        return new model.characterDataFormatSetting(rs.effectDigitLength(), rs.startDigit(), rs.endDigit(), rs.codeEditing(), rs.codeEditDigit(), rs.codeEditingMethod(), rs.spaceEditing(), rs.codeConvertCode(), rs.nullValueReplace(), rs.valueOfNullValueReplace(), rs.fixedValue(), rs.valueOfFixedValue());
                    });
                    self.characterDataFormatSetting(_rsList);
                } else {
                    self.characterDataFormatSetting = ko.observable(new model.CharacterDataFormatSetting(0, null, null, 0, null, null, null, null, 0, "", 0, ""));
                }
            }
            if (parrams.modeScreen) {
                self.isEnable(false);
            }
            if (!parrams.modeScreen) {
                self.isEnable(true);
            }

            self.effectDigitLengthItem = ko.observableArray([
                new model.ItemModel(model.FORMAT_SELECTION.DECIMAL, getText('CMF002_165')),
                new model.ItemModel(model.FORMAT_SELECTION.NO_DECIMAL, getText('CMF002_166'))
            ]);
            self.codeEditingItem = ko.observableArray([
                new model.ItemModel(model.NOT_USE_ATR.USE, getText('CMF002_149')),
                new model.ItemModel(model.NOT_USE_ATR.NOT_USE, getText('CMF002_150'))
            ]);
            self.nullValueReplaceItem = ko.observableArray([
                new model.ItemModel(model.NOT_USE_ATR.USE, getText('CMF002_149')),
                new model.ItemModel(model.NOT_USE_ATR.NOT_USE, getText('CMF002_150'))
            ]);
            self.fixedValueItem = ko.observableArray([
                new model.ItemModel(1, getText('CMF002_149')),
                new model.ItemModel(0, getText('CMF002_150'))
            ]);
            self.codeEditingMethodItem = ko.observableArray([
                new model.ItemModel(model.FIXED_LENGTH_EDITING_METHOD.ZERO_BEFORE, getText('Enum_FixedLengthEditingMethod_ZERO_BEFORE')),
                new model.ItemModel(model.FIXED_LENGTH_EDITING_METHOD.ZERO_AFTER, getText('Enum_FixedLengthEditingMethod_ZERO_AFTER')),
                new model.ItemModel(model.FIXED_LENGTH_EDITING_METHOD.SPACE_BEFORE, getText('Enum_FixedLengthEditingMethod_SPACE_BEFORE')),
                new model.ItemModel(model.FIXED_LENGTH_EDITING_METHOD.SPACE_AFTER, getText('Enum_FixedLengthEditingMethod_SPACE_AFTER'))
            ]);
            self.spaceEditingItem = ko.observableArray([
                new model.ItemModel(model.DECIMAL_POINT_CLASSIFICATION.NO_OUTPUT_DECIMAL_POINT, getText('Enum_DecimalPointClassification_NO_OUTPUT_DECIMAL_POINT')),
                new model.ItemModel(model.DECIMAL_POINT_CLASSIFICATION.OUTPUT_DECIMAL_POINT, getText('Enum_DecimalPointClassification_OUTPUT_DECIMAL_POINT'))
            ]);
        }
        validate() {
            var self = this;
            self.characterDataFormatSetting().effectDigitLength.subscribe(function(selectedValue: any) {
                if (selectedValue == 0) {
                    $('#J2_2_1').ntsError('clear');
                    $('#J2_2_3').ntsError('clear');
                } else {
                    $('#J2_2_1').ntsError('check');
                    $('#J2_2_3').ntsError('check');
                }
            });
            self.characterDataFormatSetting().codeEditing.subscribe(function(selectedValue: any) {
                if (selectedValue == 0) {
                    $('#J3_2_1').ntsError('clear');
                } else {
                    $('#J3_2_1').ntsError('check');
                }
            });
            self.characterDataFormatSetting().nullValueReplace.subscribe(function(selectedValue: any) {
                if (selectedValue == 0) {
                    $('#J6_2').ntsError('clear');
                } else {
                    $('#J6_2').ntsError('check');
                }
            });
            self.characterDataFormatSetting().fixedValue.subscribe(function(selectedValue: any) {
                if (selectedValue == 0) {
                    $('#J7_2').ntsError('clear');
                    if (self.characterDataFormatSetting().effectDigitLength() == model.FORMAT_SELECTION.DECIMAL) {
                        $('#J2_2_1').ntsError('check');
                        $('#J2_2_3').ntsError('check');
                    }
                    if (self.characterDataFormatSetting().codeEditing() == model.NOT_USE_ATR.USE) {
                        $('#J3_2_1').ntsError('check');
                    }
                    if (self.characterDataFormatSetting().nullValueReplace() == model.NOT_USE_ATR.USE) {
                        $('#J6_2').ntsError('check');
                    }
                } else {
                    $('#J7_2').ntsError('check');
                    $('#J2_2_1').ntsError('clear');
                    $('#J2_2_3').ntsError('clear');
                    $('#J3_2_1').ntsError('clear');
                    $('#J6_2').ntsError('clear');
                }
            });
        }
        start(): JQueryPromise<any> {
            //block.invisible();
            var self = this;
            var dfd = $.Deferred();
            dfd.resolve();
            return dfd.promise();
        }
        enableEffectDigitLengthCls() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE && self.modeScreen);
        }
        enableCodeEditingCls() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE && self.modeScreen);
        }

        enableSpaceEditing() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE && self.modeScreen);
        }
        enableConvertCode() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE && self.modeScreen);
        }
        enableNullValueReplaceCls() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE && self.modeScreen);
        }

        enableFixedValueCls() {
            var self = this;
            return (self.modeScreen);
        }
        enableEffectDigitLength() {
            var self = this;
            return (self.characterDataFormatSetting().effectDigitLength() == model.NOT_USE_ATR.USE && self.modeScreen && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        enableCodeEditing() {
            var self = this;
            return (self.characterDataFormatSetting().codeEditing() == model.NOT_USE_ATR.USE && self.modeScreen && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        enableNullValueReplace() {
            var self = this;
            return (self.characterDataFormatSetting().nullValueReplace() == model.NOT_USE_ATR.USE && self.modeScreen && self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.NOT_USE);
        }
        enableFixedValue() {
            var self = this;
            return (self.characterDataFormatSetting().fixedValue() == model.NOT_USE_ATR.USE && self.modeScreen);
        }
        open002_V2() {

        }
        saveCharacterSetting() {
            let self = this;
            let command = ko.toJS(self.characterDataFormatSetting());
            service.setCharacterDataFormatSetting(command).done(function() {
                nts.uk.ui.windows.close();
            }).fail(error => {
                alertError({ messageId: "Msg" });
            });
        }
        cancelCharacterSetting() {
            nts.uk.ui.windows.close();
        }
        gotoScreenJ() {
            nts.uk.ui.windows.sub.modal("/view/cmf/002/j/index.xhtml");
        }
        gotoScreenM() {
            nts.uk.ui.windows.sub.modal("/view/cmf/002/m/index.xhtml");
        }
        gotoScreenN() {
            nts.uk.ui.windows.sub.modal("/view/cmf/002/n/index.xhtml");
        }
        gotoScreenL() {
            nts.uk.ui.windows.sub.modal("/view/cmf/002/l/index.xhtml");
        }
    }
}