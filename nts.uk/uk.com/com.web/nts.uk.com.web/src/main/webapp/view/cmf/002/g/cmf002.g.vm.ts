module nts.uk.com.view.cmf002.g.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import dialog = nts.uk.ui.dialog;
    import model = cmf002.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import modal = nts.uk.ui.windows.sub.modal;

    export class ScreenModel {
        listOutputCodeConvert: KnockoutObservableArray<OutputCodeConvert> = ko.observableArray([]);
        selectedCodeConvert: KnockoutObservable<string> = ko.observable('');
        selectedConvertDetail: KnockoutObservable<number> = ko.observable(0);
        enableBtn: KnockoutObservable<boolean> = ko.observable(true);
        screenMode: KnockoutObservable<number>;

        codeConvertCurrent: KnockoutObservable<OutputCodeConvert> = ko.observable(new OutputCodeConvert('', '', 0, []));

        acceptWithoutSettingItems: KnockoutObservableArray<model.ItemModel>;

        constructor() {
            let self = this;
            self.screenMode = ko.observable(model.SCREEN_MODE.UPDATE);
            $("#fixed-table").ntsFixedTable({ height: 184, width: 600 });

            self.acceptWithoutSettingItems = ko.observableArray([
                new model.ItemModel(model.NOT_USE_ATR.USE, getText('CMF002_131')),
                new model.ItemModel(model.NOT_USE_ATR.NOT_USE, getText('CMF002_132')),
            ]);

            self.selectedCodeConvert.subscribe(function(convertCode: string) {
                if (convertCode) {
                    block.invisible();
                    self.enableBtn(true);
                    service.getOutputCodeConvertByConvertCode(convertCode).done(function(data) {
                        if (data) {
                            self.codeConvertCurrent().listCdConvertDetail.removeAll();

                            self.codeConvertCurrent().convertCode(data.convertCode);
                            self.codeConvertCurrent().convertName(data.convertName);
                            self.codeConvertCurrent().acceptWithoutSetting(data.acceptWithoutSetting);

                            var detail: Array<any> = _.sortBy(data.listCdConvertDetail, ['lineNumber']);

                            for (let i = 0; i < detail.length; i++) {
                                self.codeConvertCurrent().listCdConvertDetail.push(new CdConvertDetail(detail[i].convertCode, detail[i].lineNumber, detail[i].outputItem, detail[i].systemCode));
                            }

                            self.screenMode(model.SCREEN_MODE.UPDATE);

                            self.setFocusItem(FOCUS_TYPE.ROW_PRESS, model.SCREEN_MODE.UPDATE);
                        }
                    }).fail(function(error) {
                        dialog.alertError(error);
                    }).always(function() {
                        block.clear();
                    });
                }
            });
        } // END constructor

        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();

            self.initialScreen();

            dfd.resolve();
            return dfd.promise();
        }

        initialScreen(convertCodeParam?: string) {
            let self = this;
            block.invisible();
            nts.uk.ui.errors.clearAll();

            service.getOutputCodeConvertByCompanyId().done(function(dataOutputCodeConvertJson: Array<any>) {
                if (dataOutputCodeConvertJson.length > 0) {
                    let _codeConvertResult: Array<any> = _.sortBy(dataOutputCodeConvertJson, ['convertCode']);
                    let _listOutputCodeConvert: Array<OutputCodeConvert> = _.map(_codeConvertResult, x => {
                        return new OutputCodeConvert(x.convertCode, x.convertName, x.acceptWithoutSetting, x.listCdConvertDetail);
                    });

                    let _codeConvert: string;
                    if (convertCodeParam) {
                        _codeConvert = convertCodeParam;
                    } else {
                        _codeConvert = _listOutputCodeConvert[0].convertCode();
                    }
                    self.selectedCodeConvert(_codeConvert);

                    self.listOutputCodeConvert(_listOutputCodeConvert);

                    self.screenMode(model.SCREEN_MODE.UPDATE);
                } else {
                    self.settingCreateMode();
                }
            }).fail(function(error) {
                dialog.alertError(error);
            }).always(function() {
                block.clear();
            });
        } // END initialScreen

        btnAddCdConvertDetails() {
            let self = this;
            block.invisible();

            self.codeConvertCurrent().listCdConvertDetail.push(new CdConvertDetail('', self.codeConvertCurrent().listCdConvertDetail().length + 1, '', ''));

            self.selectedConvertDetail(self.codeConvertCurrent().listCdConvertDetail().length);
            $("#fixed-table tr")[self.codeConvertCurrent().listCdConvertDetail().length - 1].scrollIntoView();

            let indexFocus: number = self.codeConvertCurrent().listCdConvertDetail().length;

            self.setFocusItem(FOCUS_TYPE.ADD_ROW_PRESS, model.SCREEN_MODE.UPDATE, indexFocus);

            block.clear();
        } // END Add table>tbody>tr

        btnRemoveCdConvertDetails() {
            let self = this;
            let indexFocus: number = 0;
            block.invisible();

            self.codeConvertCurrent().listCdConvertDetail.remove(function(item)
            { return item.lineNumber() == (self.selectedConvertDetail()); })

            for (var i = 0; i < self.codeConvertCurrent().listCdConvertDetail().length; i++) {
                self.codeConvertCurrent().listCdConvertDetail()[i].lineNumber(i + 1);
            }

            if (self.selectedConvertDetail() >= self.codeConvertCurrent().listCdConvertDetail().length) {
                self.selectedConvertDetail(self.codeConvertCurrent().listCdConvertDetail().length);
                indexFocus = self.codeConvertCurrent().listCdConvertDetail().length;
            } else {
                indexFocus = self.selectedConvertDetail();
            }

            self.setFocusItem(FOCUS_TYPE.DEL_ROW_PRESS, model.SCREEN_MODE.UPDATE, indexFocus);
            self.selectedConvertDetail.valueHasMutated();

            block.clear();
             $('#fixed-table').focus();
        } // END Remove table>tbody>tr

        btnCreateCodeConvert() {
            let self = this;
            self.enableBtn(false);
            block.invisible();
            self.settingCreateMode();
            block.clear();
            $('#G3_1').focus();

        }

        btnRegOutputCodeConvert() {
            let self = this;
            nts.uk.ui.errors.clearAll();
            block.invisible();
            $('.nts-input').trigger("validate");

            for (var i = 0; i < self.codeConvertCurrent().listCdConvertDetail().length; i++) {
                self.codeConvertCurrent().listCdConvertDetail()[i].convertCode(self.codeConvertCurrent().convertCode());
            }

            let currentOutputCodeConvert = self.codeConvertCurrent;

            if (model.SCREEN_MODE.NEW == self.screenMode()) {
                if (_.isEmpty(currentOutputCodeConvert().convertCode())) {
                    dialog.alertError({ messageId: "Msg_660" });
                    block.clear();
                    return;
                } else {
                    let existCode = self.listOutputCodeConvert().filter(x => x.systemCode() === currentOutputCodeConvert().systemCode());
                    if (existCode.length > 0) {
                        dialog.alertError({ messageId: "Msg_661" });
                        block.clear();
                        return;
                    }
                }
            }

            let _outputItemDuplicate: Array<any> = [];
            for (let detail of currentOutputCodeConvert().listCdConvertDetail()) {
                if (!_.isEmpty(detail.systemCode())) {
                    // check duplicate OutputItem detail
                    let data = currentOutputCodeConvert().listCdConvertDetail().filter(x => x.systemCode() === detail.systemCode());
                    if (data.length >= 2) {
                        _outputItemDuplicate.push(detail);
                    }
                }
            }

            // check duplicate OutputItem detail
            if (!_.isEmpty(_outputItemDuplicate)) {
                let _errorOutputItemDuplicate: Array<any> = _.uniqBy(ko.toJS(_outputItemDuplicate), 'outputItem');
                for (let i = 0; i < _errorOutputItemDuplicate.length; i++) {
                    $('tr[data-id=' + _errorOutputItemDuplicate[i].lineNumber + ']').find("input").eq(1).ntsError('set', { messageId: 'Msg_661', messageParams: [_errorOutputItemDuplicate[i].systemCode] });
                }
                //dialog.alertError({ messageId: "Msg_661" });
            }

            if (!nts.uk.ui.errors.hasError()) {
                if (model.SCREEN_MODE.NEW == self.screenMode()) {
                    service.addOutputCodeConvert(ko.toJS(self.codeConvertCurrent())).done((outputConvertCode) => {
                        dialog.info({ messageId: "Msg_15" }).then(() => {
                            self.initialScreen(self.codeConvertCurrent().convertCode());
                        });
                    }).fail(function(error) {
                        dialog.alertError(error);
                    }).always(function() {
                        block.clear();
                        $('#G2_3').focus();
                    });
                } else {
                    service.updateOutputCodeConvert(ko.toJS(self.codeConvertCurrent())).done((outputConvertCode) => {
                        dialog.info({ messageId: "Msg_15" }).then(() => {
                            self.initialScreen(self.selectedCodeConvert());
                        });
                    }).fail(function(error) {
                        dialog.alertError(error);
                    }).always(function() {
                        block.clear();
                        $('#G2_3').focus();
                    });
                }
            } else {
                block.clear();
                $('#G2_3').focus();
            }
            self.enableBtn(true);
            _.defer(() => { $('#G2_3').focus(); });
        }


        btnDeleteOutputCodeConvert() {
            let self = this
            let _listOutputCodeConvert = self.listOutputCodeConvert;
            let _codeConvertCurrent = self.codeConvertCurrent;
            block.invisible();

            if (_codeConvertCurrent().listCdConvertDetail().length > 0) {
                dialog.alertError({ messageId: "Msg_659" });
                block.clear();
                $('#G2_3').focus();
                return;
                
            }


            dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                service.removeOutputCodeConvert(ko.toJS(_codeConvertCurrent)).done(function() {

                    let index: number = _.findIndex(_listOutputCodeConvert(), function(x)
                    { return x.convertCode() == _codeConvertCurrent().convertCode() });

                    if (index >= 0) {
                        self.listOutputCodeConvert.splice(index, 1);
                        if (index >= _listOutputCodeConvert().length) {
                            index = _listOutputCodeConvert().length - 1;
                        }
                    }

                    dialog.info({ messageId: "Msg_16" }).then(() => {
                        if (_listOutputCodeConvert().length > 0) {
                            self.initialScreen(_listOutputCodeConvert()[index].convertCode());
                            self.screenMode(model.SCREEN_MODE.UPDATE);
                            $('#G2_3').focus();
                        } else {
                            self.settingCreateMode();
                        }
                    });
                }).fail(function(error) {
                    dialog.alertError(error);
                }).always(function() {
                    block.clear();
                    $('#G2_3').focus();
                });
            }).then(() => {
                block.clear();
                $('#G2_3').focus();
            });
            $('#G2_3').focus();
        }

        btnCloseDialog() {
            close();
        }

        settingCreateMode() {
            let self = this;
            nts.uk.ui.errors.clearAll();

            self.selectedCodeConvert('');

            self.codeConvertCurrent().convertCode('');
            self.codeConvertCurrent().convertName('');
            self.codeConvertCurrent().acceptWithoutSetting(1);

            self.codeConvertCurrent().listCdConvertDetail.removeAll();
            self.selectedConvertDetail(0);
            self.btnAddCdConvertDetails();

            self.screenMode(model.SCREEN_MODE.NEW);

            self.setFocusItem(FOCUS_TYPE.ADD_PRESS, model.SCREEN_MODE.NEW);
        }

        setFocusItem(focus: number, screenMode: number, index?: number) {
            let self = this;
            if (focus == FOCUS_TYPE.ADD_ROW_PRESS || focus == FOCUS_TYPE.DEL_ROW_PRESS) {
                $('tr[data-id=' + index + ']').find("input").first().focus();
            }
            _.defer(() => { nts.uk.ui.errors.clearAll() });
        }


    } //end screenModel


    export enum FOCUS_TYPE {
        INIT = 0,
        ADD_PRESS = 1,
        REG_PRESS = 2,
        DEL_PRESS = 3,
        ROW_PRESS = 4,
        ADD_ROW_PRESS = 5,
        DEL_ROW_PRESS = 6
    }

    export class OutputCodeConvert {
        convertCode: KnockoutObservable<string>;
        dispConvertCode: string;

        convertName: KnockoutObservable<string>;
        dispConvertName: string;

        acceptWithoutSetting: KnockoutObservable<number>;

        listCdConvertDetail: KnockoutObservableArray<CdConvertDetail>;

        constructor(code: string, name: string, acceptWithoutSetting: number, listCdConvertDetail: Array<any>) {
            this.convertCode = ko.observable(code);
            this.dispConvertCode = code;
            this.convertName = ko.observable(name);
            this.dispConvertName = name;
            this.acceptWithoutSetting = ko.observable(acceptWithoutSetting);
            this.listCdConvertDetail = ko.observableArray(listCdConvertDetail);
        }
    }

    export class CdConvertDetail {
        convertCode: KnockoutObservable<string>;
        lineNumber: KnockoutObservable<number>;
        outputItem: KnockoutObservable<string>;
        systemCode: KnockoutObservable<string>;

        constructor(convertCode: string, lineNumber: number, outputItem: string, systemCode: string) {
            this.convertCode = ko.observable(convertCode);
            this.lineNumber = ko.observable(lineNumber);
            this.outputItem = ko.observable(outputItem);
            this.systemCode = ko.observable(systemCode);
        }
    }

}

$(function() {
    $('#fixed-table tbody').on('click', 'tr', function() {
        var index = $(this).attr('data-id');
        //alert( 'Row index: '+ index );
        nts.uk.ui.errors.clearAll();
        nts.uk.ui._viewModel.content.selectedConvertDetail(index);
    });
})



