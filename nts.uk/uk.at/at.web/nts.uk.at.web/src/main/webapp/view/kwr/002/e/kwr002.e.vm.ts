module nts.uk.com.view.kwr002.e {
    const MONTHLY_ATRRIBUTE: number = 2;
    export module viewmodel {

        export class ScreenModel {

            layoutCode: KnockoutObservable<string>;
            layoutName: KnockoutObservable<string>;
            dailyMonthly: KnockoutObservable<string>;
            columnIndex: KnockoutObservable<string>;
            position: KnockoutObservable<string>;
            attendanceRecordName: KnockoutObservable<string>;
            currentCodeList: KnockoutObservableArray<string>;
            columns: KnockoutObservableArray<any>;
            gridItems: KnockoutObservableArray<model.GridItem>;
            selectionTypeList: KnockoutObservableArray<model.SelectionType>;
            selectionTypeValue: KnockoutObservable<number>;
            selectedGridItems: KnockoutObservableArray<model.SelectedItem>;
            formulaContent: KnockoutObservable<string>;
            attendanceItem: KnockoutObservable<any>;

            constructor() {
                let self = this;

                self.layoutCode = ko.observable('');
                self.layoutName = ko.observable('');
                self.dailyMonthly = ko.observable('');
                self.columnIndex = ko.observable('');
                self.position = ko.observable('');
                self.attendanceRecordName = ko.observable('');
                self.currentCodeList = ko.observableArray([]);
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KWR002_176'), key: 'code', width: 70 },
                    { headerText: nts.uk.resource.getText('KWR002_177'), key: 'name', width: 250 }
                ]);
                self.gridItems = ko.observableArray([]);
                self.selectionTypeList = ko.observableArray([
                    new model.SelectionType(16, nts.uk.resource.getMessage("Msg_1209", [])),
                    new model.SelectionType(17, nts.uk.resource.getMessage("Msg_1210", [])),
                    new model.SelectionType(18, nts.uk.resource.getMessage("Msg_1211", [])),
                    new model.SelectionType(22, nts.uk.resource.getMessage("Msg_1633", [])),
                ]);
                self.selectionTypeValue = ko.observable(0);
                self.selectionTypeValue.subscribe(function(codeChange) {
                    if (codeChange == 0) {
                        return;
                    }
                    self.gridItems([]);
                    self.currentCodeList([]);
                    self.selectedGridItems([]);
                    self.findAttndRecByScreen(codeChange);
                });
                self.selectedGridItems = ko.observableArray([]);
                self.selectedGridItems.subscribe(function(changeList) {
                    var tempContent: string = '';
                    changeList.forEach(function(item) {
                        tempContent += item.action + item.name;
                    });
                    self.formulaContent(tempContent);
                });
                self.formulaContent = ko.observable('');
                self.attendanceItem = ko.observable(null);

            }

            start(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();

                self.attendanceItem(nts.uk.ui.windows.getShared('attendanceItem'));
                var attendanceItem = self.attendanceItem();
                if (attendanceItem.attendanceId != null || attendanceItem.attendanceId != undefined) {
                    self.selectionTypeValue(attendanceItem.attribute);
                    self.selectedGridItems(attendanceItem.attendanceId);
                    dfd.resolve();
                }
                else {
                    var attendanceRecordKey: model.AttendanceRecordKeyDto = {
                        code: attendanceItem.layoutCode,
                        columnIndex: attendanceItem.columnIndex,
                        position: attendanceItem.position,
                        exportAtr: attendanceItem.exportAtr
                    };
                    service.getCalculateAttndRecInfo(attendanceRecordKey).done(function(calculateAttendanceRecordDto: model.CalculateAttendanceRecordDto) {
                        if (calculateAttendanceRecordDto != null) {
                            if (calculateAttendanceRecordDto.attribute != null || calculateAttendanceRecordDto.attribute != undefined) {
                                self.selectionTypeValue(calculateAttendanceRecordDto.attribute);
                            } else {
                                self.selectionTypeValue(16);
                            }
                            var calculateAttendanceRecordList: Array<model.SelectedItem> = [];
                            calculateAttendanceRecordDto.addedItem.forEach(function(item) {
                                calculateAttendanceRecordList.push(new model.SelectedItem(model.Action.ADDITION.toString(), item.attendanceItemId, item.attendanceItemName));
                            });
                            calculateAttendanceRecordDto.subtractedItem.forEach(function(item) {
                                calculateAttendanceRecordList.push(new model.SelectedItem(model.Action.SUBTRACTION.toString(), item.attendanceItemId, item.attendanceItemName));
                            });
                            calculateAttendanceRecordList.sort(function(a, b) { return a.code - b.code; });
                            self.selectedGridItems(calculateAttendanceRecordList);
                        }
                        else {
                            self.selectionTypeValue(16);
                        }
                        dfd.resolve();
                    });
                }
                var layouCodeText = attendanceItem.layoutCode < 10 ? "0" + attendanceItem.layoutCode : "" + attendanceItem.layoutCode;
                self.layoutCode(layouCodeText);
                self.layoutName(attendanceItem.layoutName);
                // process display
                if (attendanceItem.exportAtr == 1) {
                    self.dailyMonthly('日次');
                }
                else {
                    self.dailyMonthly('月次');
                }
                self.columnIndex(attendanceItem.columnIndex);
                // process display
                if (attendanceItem.position == 1) {
                    self.position('上');
                }
                else {
                    self.position('下');
                }
                self.attendanceRecordName(attendanceItem.attendanceItemName);

                return dfd.promise();
            }

            private findAttndRecByScreen(value: any): void {
                var self = this;
                var itemList: Array<model.GridItem> = [];
                var attendanceTypeKey: model.AttendanceTypeKey = new model.AttendanceTypeKey(value, self.attendanceItem().exportAtr);
                service.getAllAttndByAtrAndType(attendanceTypeKey).done(function(attData: Array<model.AttendanceRecordItemDto>) {
                    if (attData.length > 0) {
                        attData.forEach(item => {
                            itemList.push(new model.GridItem(item.attendanceItemId, item.attendanceItemName));
                        });
                    }
                    self.gridItems(itemList);
                });
            }

            writeItemWithAdd() {
                var self = this;
                var selectedItems: Array<model.GridItem> = [];
                _.forEach(self.currentCodeList(), function(item) {
                    var selectedItem = _.find(self.gridItems(), function(e) { return e.code == item; });
                    selectedItems.push(selectedItem);
                });
                if (selectedItems.length == 0) {
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_1140' });
                    return;
                }
                else {
                    _.forEach(selectedItems, function(item: model.GridItem) {
                        self.selectedGridItems.push(new model.SelectedItem(model.Action.ADDITION.toString(), item.code, item.name));
                    });
                    self.selectedGridItems(self.selectedGridItems().sort(function(a, b) { return a.code - b.code; }));
                    self.currentCodeList([]);
                }
            }

            writeItemWithSubtract() {
                var self = this;
                var selectedItems: Array<model.GridItem> = [];
                _.forEach(self.currentCodeList(), function(item) {
                    var selectedItem = _.find(self.gridItems(), function(e) { return e.code == item; });
                    selectedItems.push(selectedItem);
                });
                if (selectedItems.length == 0) {
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_1140' });
                    return;
                }
                else {
                    _.forEach(selectedItems, function(item: model.GridItem) {
                        self.selectedGridItems.push(new model.SelectedItem(model.Action.SUBTRACTION.toString(), item.code, item.name));
                    });
                    self.selectedGridItems(self.selectedGridItems().sort(function(a, b) { return a.code - b.code; }));
                    self.currentCodeList([]);
                }
            }

            deleteItem(data) {
                var self = this;
                var seletedItems = self.selectedGridItems();
                var item: model.SelectedItem;
                for (var i = 0; i < seletedItems.length; i++) {
                    item = seletedItems[i];
                    if (item == data) {
                        seletedItems.splice(i, 1);
                        break;
                    }
                }
                self.selectedGridItems(seletedItems);
            }

            decide() {
                $('.nts-input').trigger("validate");
                _.defer(() => {
                    if (!$('.nts-editor').ntsError("hasError") && !$('.ntsControl').ntsError("hasError")) {
                        var self = this;
                        var outputItems: Array<model.SelectedItem>;
                        outputItems = self.selectedGridItems();

                        if (outputItems.length == 0) {
                            nts.uk.ui.dialog.alertError({ messageId: 'Msg_1141' });
                            return;
                        }
                        else {
                            var attendanceItem = self.attendanceItem();
                            nts.uk.ui.windows.setShared('attendanceRecordExport', {
                                attendanceItemName: self.attendanceRecordName(),
                                layoutCode: attendanceItem.layoutCode,
                                layoutName: attendanceItem.layoutName,
                                columnIndex: attendanceItem.columnIndex,
                                position: attendanceItem.position,
                                exportAtr: attendanceItem.exportAtr,
                                attendanceId: outputItems,
                                attribute: self.selectionTypeValue()
                            });
                        }
                        nts.uk.ui.windows.close();
                    }
                });
            }

            cancel() {
                nts.uk.ui.windows.close();
            }
        }
    }
}