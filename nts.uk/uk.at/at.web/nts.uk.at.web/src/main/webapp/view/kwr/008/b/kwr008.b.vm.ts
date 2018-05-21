module nts.uk.at.view.kwr008.b.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import model = nts.uk.at.view.kwr008.share.model;
    
    export class ScreenModel {
        //enum mode
        isNewMode: KnockoutObservable<boolean> = ko.observable(true);

        //enum value output format
        valOutFormat: KnockoutObservableArray<any> = ko.observableArray([]);

        //B2_2
        listStandardImportSetting: KnockoutObservableArray<SetOutputSettingCode> = ko.observableArray([]);
        selectedCode: KnockoutObservable<string> = ko.observable('');
        currentSetOutputSettingCode :KnockoutObservable<SetOutputSettingCode> 
                = ko.observable(new SetOutputSettingCode(null));

        //B5_3
        itemRadio: KnockoutObservableArray<any> = ko.observableArray([]);
        //Rule 36.
        rule36CalculationName: string;

        //B4
        outputItem: KnockoutObservableArray<any> = ko.observableArray([]);

        isCheckedAll: KnockoutObservable<boolean> = ko.observable(false);
        
        constructor() {
            let self = this;
            
            //B5_3
            self.itemRadio = ko.observableArray([
                new model.ItemModel(0, getText('KWR008_37')),
                new model.ItemModel(1, getText('KWR008_38')),
                new model.ItemModel(2, getText('KWR008_39'))
            ]);
            self.rule36CalculationName = getText('KWR008_32');
            for (var i = 1; i <= 10; i++) {
                self.outputItem.push(new OutputItemData(i,
                    null,
                    false,
                    '',
                    0,
                    '',
                    i == 1 //set is 36協定時間 if it's fist OutputItem
                 ));
            }
            //event select change
            self.selectedCode.subscribe((code) => {
                block.invisible();
                nts.uk.ui.errors.clearAll();
                if (code) {
                    service.getListItemOutput(code).done(r => {
                        let dataSorted = [];
                        if (r && r.length > 0) {
                            dataSorted = _.sortBy(r, ['sortBy']);
                            //check exist item 36協定時間
                            var item36AgreementTime = _.find(dataSorted, (item) => {return item.item36AgreementTime;});
                            if (!item36AgreementTime) {
                                dataSorted.unshift({item36AgreementTime: true});
                            }

                            for (var i = 0; i < dataSorted.length; i++) {
                                if (i >= self.outputItem().length) {
                                    self.outputItem.push(new OutputItemData(i + 1,
                                     dataSorted[i].cd,
                                     dataSorted[i].useClass,
                                     dataSorted[i].headingName,
                                     dataSorted[i].valOutFormat,
                                     '',
                                     dataSorted[i].item36AgreementTime));
                                } else {
                                    self.outputItem()[i].updateData(i + 1,
                                        dataSorted[i].cd,
                                        dataSorted[i].useClass,
                                        dataSorted[i].headingName,
                                        dataSorted[i].valOutFormat,
                                        '',
                                        dataSorted[i].item36AgreementTime,
                                        null);
                                }
                                if (!dataSorted[i].item36AgreementTime) {
                                    let addItems = _.filter(dataSorted[i].listOperationSetting, (x) => { return x.operation === 1; }).map((item) => { return item.attendanceItemId; });
                                    let subItems = _.filter(dataSorted[i].listOperationSetting, (x) => { return x.operation === 0; }).map((item) => { return item.attendanceItemId; });
                                    let resultData = {
                                        lstAddItems: addItems,
                                        lstSubItems: subItems
                                    };
                                    self.buildOutputItem(resultData, self.outputItem()[i]);
                                }
                            }
                        }
                        for (var i = dataSorted.length; i < self.outputItem().length; i++) {
                            self.outputItem()[i].updateData(i+1,
                                null,
                                false,
                                '',
                                0,
                                '',
                                i == 0, //set is 36協定時間 if it's fist OutputItem
                                 null);
                        }
                    }).always(function() {
                        self.updateMode(code);
                        block.clear();
                    });
                } else {
                    block.clear();
                    self.registerMode();
                }
            });

            self.isCheckedAll = ko.computed({
                read: function() {
                    let itemOut: any = _.filter(self.outputItem(), (x) => { return !x.useClass(); });
                    if (itemOut && itemOut.length > 0) {
                        return false;
                    } else {
                        return true;
                    }
                },
                write: function(val) {
                    ko.utils.arrayForEach(self.outputItem(), function(item) {
                        item.useClass(val);
                    });
                },
                owner: this
            });

            $('#table-output-items').ntsFixedTable({ height: 320, width: 1200 });
        }

        public startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();

            block.invisible();
            //fill data B2_2
            service.getOutItemSettingCode().done((data) => {
                var dataSorted = _.sortBy(data, ['cd']);
                for (let i = 0, count = data.length; i < count; i++) {
                    self.listStandardImportSetting.push(new SetOutputSettingCode(dataSorted[i]));
                }
                var KWR008BParam = nts.uk.ui.windows.getShared("KWR008_B_Param");
                if (KWR008BParam && KWR008BParam.selectedCd) {
                    self.selectedCode(KWR008BParam.selectedCd);
                    self.updateMode(KWR008BParam.selectedCd);
                }

                service.getValueOutputFormat().done(data => {
                    for (let i = 0, count = data.length; i < count; i++) {
                        self.valOutFormat.push(new model.ItemModel(data[i].value, data[i].localizedName));
                    }
                }).always(function() {
                    dfd.resolve(self);
                    block.clear();
                });
            }).always(function() {
                self.checkListItemOutput();
                dfd.resolve(self);
                block.clear();
            });

            return dfd.promise();
        }

        listStandardImportSetting_Sort() {
            let self = this;
            self.listStandardImportSetting.sort((a, b) => {
                return (+a.cd() === +b.cd()) ? 0 : (+a.cd() < +b.cd()) ? -1 : 1;
            });
        }
        //Open dialog KDW007
        openKDW007(sortBy) {
            let self = this;
            nts.uk.ui.block.invisible();
            let index = _.findIndex(self.outputItem(), (x) => {return x.sortBy() === sortBy(); });
            if (index == -1) {
                nts.uk.ui.block.clear();
                return;
            }

            self.getListItemByAtr(self.outputItem()[index].valOutFormat()).done((lstItem) => {
                let lstItemCode = lstItem.map((item) => { return item.attendanceItemId; });
                let lstAddItems = _.filter(self.outputItem()[index].listOperationSetting(), (item) => {
                    return item.operation();
                }).map((item) => { return item.attendanceItemId(); });
                let lstSubItems = _.filter(self.outputItem()[index].listOperationSetting(), (item) => {
                    return !item.operation();
                }).map((item) => { return item.attendanceItemId(); });
                let param = {
                    lstAllItems: lstItemCode,
                    lstAddItems: lstAddItems,
                    lstSubItems: lstSubItems
                };
                nts.uk.ui.windows.setShared("KDW007Params", param);
                nts.uk.ui.windows.sub.modal("at", "/view/kdw/007/c/index.xhtml").onClosed(() => {
                    let resultData = nts.uk.ui.windows.getShared("KDW007CResults");
                    if (!resultData) {
                        nts.uk.ui.block.clear();
                        return;
                    }
                    self.buildOutputItem(resultData, self.outputItem()[index]).done(() => {
                    }).always(function() {
                        block.clear();
                    });
                });
            });
        }

        buildOutputItemByOper(lstItems : Array<any>, outputItem, isAdd : boolean) : JQueryPromise {
            let self = this;
            let dfd = $.Deferred<any>();
            let operationName = "";
            if (lstItems && lstItems.length > 0) {
                service.getAttendanceItemByCodes(lstItems).done((items) => {
                    _.forEach(items, (item) => {
                        if (isAdd) {
                            outputItem.listOperationSetting.push(new OperationCondition(item.attendanceItemId, 1, item.attendanceItemName));
                            if (operationName) {
                                operationName = operationName + " + " + item.attendanceItemName;
                            } else {
                                operationName = item.attendanceItemName;
                            }
                        } else {
                            outputItem.listOperationSetting.push(new OperationCondition(item.attendanceItemId, 0, item.attendanceItemName));
                            if (operationName) {
                                operationName = operationName + " - " + item.attendanceItemName;
                            } else {
                                operationName = item.attendanceItemName;
                            }
                        }
                    });
                }).always(function() {
                    dfd.resolve(operationName);
                });
            } else {
                dfd.resolve(operationName);
            }
            return dfd.promise();
        }
        
        //re-build output item from Kdw007 result
        buildOutputItem(resultData, outputItem): JQueryPromise {
            let self = this;
            let dfd = $.Deferred<any>();
            let operationName = "";

            outputItem.listOperationSetting.removeAll();
            //add
            self.buildOutputItemByOper(resultData.lstAddItems, outputItem, true).done((name) => {
                operationName = name;
            }).always(function() {
                //sub
                self.buildOutputItemByOper(resultData.lstSubItems, outputItem, false).done((name) => {
                    if (name) {
                        if (operationName) {
                            operationName = operationName + " - " + name;
                        } else {
                            operationName = name;
                        }
                    }
                }).always(function() {
                    outputItem.outputTargetItem(operationName);
                    dfd.resolve();
                });
            });
            return dfd.promise();
        }

        // get data for kdw007
        getListItemByAtr(valOutFormat) {
            let self = this;
            let dfd = $.Deferred<any>();
            if (valOutFormat === 1) {
                //With type 回数 - Times
                service.getAttendanceItemByAtr(2).done((lstAtdItem) => {
                    service.getOptItemByAtr(1).done((lstOptItem) => {
                        for (let i = 0; i < lstOptItem.length; i++) {
                            lstAtdItem.push(lstOptItem[i]);
                        }
                        dfd.resolve(lstAtdItem);
                    });
                });
            } else if (valOutFormat === 0) {
                //With type 時間 - Time
                service.getAttendanceItemByAtr(5).done((lstAtdItem) => {
                    service.getOptItemByAtr(0).done((lstOptItem) => {
                        for (let i = 0; i < lstOptItem.length; i++) {
                            lstAtdItem.push(lstOptItem[i]);
                        }
                        dfd.resolve(lstAtdItem);
                    });
                });
            } else if (valOutFormat === 2) {
                //With type 時刻 - TimeWithDay
                service.getAttendanceItemByAtr(6).done((lstAtdItem) => {
                    dfd.resolve(lstAtdItem);
                });
            } else if (valOutFormat === 3) {
                //With type 金額 - AmountMoney
                service.getAttendanceItemByAtr(3).done((lstAtdItem) => {
                    service.getOptItemByAtr(2).done((lstOptItem) => {
                        for (let i = 0; i < lstOptItem.length; i++) {
                            lstAtdItem.push(lstOptItem[i]);
                        }
                        dfd.resolve(lstAtdItem);
                    });
                });
            }
            return dfd.promise();
        }

        checkListItemOutput() {
            var self = this;

            if (self.listStandardImportSetting().length == 0) {

                self.registerMode();
            } else {

                if (!self.selectedCode()) {
                    self.selectedCode(self.listStandardImportSetting()[0].cd());
                    self.updateMode(self.listStandardImportSetting()[0].cd());
                }
            }
        }
        //mode update
        updateMode(code: string) {
            let self = this;
            
            for (var i = self.outputItem().length; i < 10; i++) {
                self.outputItem.push(new OutputItemData(i + 1,
                null,
                false,
                '',
                0,
                '',
                i == 0 //set is 36協定時間 if it's fist OutputItem
                ));
            }
            self.outputItem()[0].outputTargetItem(self.rule36CalculationName);
            
            if (code) {
                let selectedIndex = _.findIndex(self.listStandardImportSetting(), (obj) => { return obj.cd() == code; });

                self.isNewMode(false);
                if (selectedIndex > -1) {
                    self.currentSetOutputSettingCode(self.listStandardImportSetting()[selectedIndex]);
                    $('#B3_3').focus();
                } else {
                    $('#B3_2').focus();
                    self.selectedCode('');
                }
            }
            
        }

        //mode register
        registerMode() {
            let self = this;

            self.isNewMode(true); 
            self.currentSetOutputSettingCode(new SetOutputSettingCode(null));
            self.selectedCode('');
            for (var i = 0; i < self.outputItem().length; i++) {
                self.outputItem()[i].updateData(i + 1,
                    null,
                    false,
                    '',
                    0,
                    '',
                    i == 0, //set is 36協定時間 if it's fist OutputItem
                    null);
            }
            self.outputItem()[0].outputTargetItem(self.rule36CalculationName);
            $("#B3_2").focus();
        }

        //do register
        doRegister() {
            let self = this;
            block.invisible();
            let itemOutByName: any = _.filter(self.outputItem(), v => { return v.headingName().trim(); });
            let itemOutUseClass: any = _.filter(itemOutByName, v => { return v.useClass(); });

            if (!itemOutUseClass || itemOutUseClass.length == 0) {
                alertError({ messageId: "Msg_880" });
                block.clear();
                return;
            }

            for (var i = 0; i < itemOutByName.length; i++) {
                // item Rule 36 - do not checking
                if (itemOutByName[i].item36AgreementTime()) {
                    continue;
                }
                if (itemOutByName[i].listOperationSetting().length == 0) {
                    alertError({ messageId: "Msg_881" });
                    block.clear();
                    return;
                } else if (itemOutByName[i].listOperationSetting().length > 50) {
                    alertError({ messageId: "Msg_882" });
                    block.clear();
                    return;
                }
            }
            $('.nts-input').trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                block.clear();
                return;
            }

            self.currentSetOutputSettingCode().buildListItemOutput(ko.toJS(itemOutByName));
            let data: model.OutputSettingCodeDto = ko.toJS(self.currentSetOutputSettingCode);

            if (self.isNewMode()) {
                service.registerOutputItemSetting(data).done(() => {
                    self.currentSetOutputSettingCode().displayCode = self.currentSetOutputSettingCode().cd();
                    self.currentSetOutputSettingCode().displayName = self.currentSetOutputSettingCode().name();
                    self.listStandardImportSetting.push(self.currentSetOutputSettingCode());
                    self.listStandardImportSetting_Sort();
                    info({ messageId: 'Msg_15' }).then(() => {
                        self.selectedCode(self.currentSetOutputSettingCode().cd());
                    });
                }).fail(err => {
                    alertError({messageId: err.messageId});
                }).always(function() {
                    block.clear();
                });
            } else {
                service.updateOutputItemSetting(data).done(() => {
                    info({ messageId: 'Msg_15' }).then(() => {
                        let selectedIndex = _.findIndex(self.listStandardImportSetting(), (obj) => { return obj.cd() == self.selectedCode(); });
                        if (selectedIndex > -1) {
                            self.currentSetOutputSettingCode().displayName = self.currentSetOutputSettingCode().name();
                            self.listStandardImportSetting.replace(self.listStandardImportSetting()[selectedIndex], self.currentSetOutputSettingCode());
                            self.selectedCode(self.currentSetOutputSettingCode().cd());
                        }
                    });
                }).fail(err => {
                    console.log(err);
                }).always(function() {
                    block.clear();
                });
            }
        }

        //do delete
        doDelete() {
            var self = this;
            confirm({ messageId: 'Msg_18' }).ifYes(() => {

                let selectedIndex = _.findIndex(self.listStandardImportSetting(), (obj) => { return obj.cd() == self.selectedCode(); });

                let data = ko.toJS(self.listStandardImportSetting()[selectedIndex]);
                self.listStandardImportSetting.remove(item=>{return item.cd() == data.cd;});
                // send request remove item
                service.deleteOutputItemSetting(data).done(() => {
                    info({ messageId: 'Msg_16' }).then(() => {
                        if (self.listStandardImportSetting().length == 0) {
                            self.selectedCode('');
                        } else {
                            if (selectedIndex >= self.listStandardImportSetting().length) {
                                self.selectedCode(self.listStandardImportSetting()[self.listStandardImportSetting().length - 1].cd());
                            } else {
                                self.selectedCode(self.listStandardImportSetting()[selectedIndex].cd());
                            }
                        }
                    });
                });

            });
        }

        //cancel register
        doCancel() {
            let self = this;
            setShared("KWR008_B_Result", {selectedCd: self.selectedCode()});
            nts.uk.ui.windows.close();
        }
    }

    export class OperationCondition {
        attendanceItemId: KnockoutObservable<number> = ko.observable(null);
        operation: KnockoutObservable<number> = ko.observable(0); //0: '-'; 1: '+'
        name: KnockoutObservable<string> = ko.observable('');
        constructor(attendanceItemId: number, operation: number, name: string) {
            let self = this;
            self.attendanceItemId(attendanceItemId || null);
            self.operation(operation || 0);
            self.name(name || '');
        }
    }
    export class OutputItemData {
        sortBy: KnockoutObservable<number>= ko.observable(1);
        cd: KnockoutObservable<string>= ko.observable('');
        useClass: KnockoutObservable<boolean>= ko.observable(false);
        headingName: KnockoutObservable<string>= ko.observable('');
        valOutFormat: KnockoutObservable<number>= ko.observable(0);
        outputTargetItem: KnockoutObservable<string>= ko.observable('');
        item36AgreementTime: KnockoutObservable<boolean> = ko.observable(false);
        listOperationSetting: KnockoutObservableArray<OperationCondition> = ko.observableArray([]);
        constructor(sortBy: number, cd: string, useClass: boolean, headingName: string, valOutFormat: number, outputTargetItem: string, item36AgreementTime: boolean) {
            let self = this;
            self.sortBy(sortBy || 1);
            self.cd(cd);
            self.useClass(useClass || false);
            self.headingName(headingName || '');
            self.valOutFormat(valOutFormat || 0);
            self.outputTargetItem(outputTargetItem || '');
            self.item36AgreementTime(item36AgreementTime || false);
        }

        updateData(sortBy: number, cd: string, useClass: boolean, headingName: string, valOutFormat: number, outputTargetItem: string, item36AgreementTime: boolean, listOperationSetting: KnockoutObservableArray<OperationCondition>) {
            let self = this;
            self.sortBy(sortBy || 1);
            self.cd(cd);
            self.useClass(useClass || false);
            self.headingName(headingName || '');
            self.valOutFormat(valOutFormat || 0);
            self.outputTargetItem(outputTargetItem || '');
            self.item36AgreementTime(item36AgreementTime || false);
            self.listOperationSetting(listOperationSetting? listOperationSetting : []);
        }

        buildListOperationSetting(listOperation: Array<any>) {
            let self = this;
            if (listOperation && listOperation.length > 0) {
                for (var i = 0; i < listOperation.length; i++) {
                    self.listOperationSetting.push(new OperationCondition(
                        listOperation[i].attendanceItemId,
                        listOperation[i].operation,
                        listOperation[i].name));
                }
            } else {
                self.listOperationSetting([]);
            }
        }
    }
    
    export class SetOutputSettingCode {
        cd: KnockoutObservable<string> = ko.observable('');
        displayCode: string;
        name: KnockoutObservable<string> = ko.observable('');
        displayName: string;
        outNumExceedTime36Agr: KnockoutObservable<boolean> = ko.observable(false);
        displayFormat: KnockoutObservable<number> = ko.observable(0);
        listItemOutput: KnockoutObservableArray<OutputItemData> = ko.observableArray([]);
        constructor(param) {
            let self = this;
            self.cd(param ? param.cd || '' : '');
            self.displayCode = self.cd();
            self.name(param ? param.name || '' : '');
            self.displayName = self.name();
            self.outNumExceedTime36Agr(param ? param.outNumExceedTime36Agr || false : false);
            self.displayFormat(param ? param.displayFormat || 0 : 0);
        }

        buildListItemOutput(listItemOutput: Array<any>) {
            let self = this;
            if (listItemOutput && listItemOutput.length > 0) {
                self.listItemOutput([]);
                for (var i = 0; i < listItemOutput.length; i++) {
                    var outputItemData = new OutputItemData(
                        i + 1,
                        listItemOutput[i].cd,
                        listItemOutput[i].useClass,
                        listItemOutput[i].headingName,
                        listItemOutput[i].valOutFormat,
                        listItemOutput[i].outputTargetItem,
                        listItemOutput[i].item36AgreementTime);
                    if (listItemOutput[i].listOperationSetting) {
                        outputItemData.buildListOperationSetting(listItemOutput[i].listOperationSetting);
                    }
                    self.listItemOutput.push(outputItemData)
                }
            } else {
                self.listItemOutput([]);
            }
        }
    }
}
