module nts.uk.com.view.kwr002.c.viewmodel {
    import blockUI = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import TabPanel = nts.uk.ui.tabpanel;
    export class ScreenModel {

        attendanceCode: KnockoutObservable<String>;
        attendanceName: KnockoutObservable<String>;
        useSeal: KnockoutObservableArray<any>;
        sealStamp: KnockoutObservableArray<String>;
        useSealValue: KnockoutObservable<any>;
        attendanceItemListDaily: KnockoutObservableArray<model.AttendanceItem>;
        attendanceItemListMonthly: KnockoutObservableArray<model.AttendanceItem>;
        attendanceItemList: KnockoutObservableArray<model.AttendanceItem>;
        attendanceRecExpDaily: KnockoutObservableArray<viewmodel.model.AttendanceRecExp>;
        attendanceRecExpMonthly: KnockoutObservableArray<viewmodel.model.AttendanceRecExp>;
        attendanceRecItemList: KnockoutObservableArray<viewmodel.model.AttendanceRecItem>;

        columns: KnockoutObservableArray<any>;

        tabs: KnockoutObservableArray<any>;
        selectedTab: KnockoutObservable<string>;

        sealName1: KnockoutObservable<String>;
        sealName2: KnockoutObservable<String>;
        sealName3: KnockoutObservable<String>;
        sealName4: KnockoutObservable<String>;
        sealName5: KnockoutObservable<String>;
        sealName6: KnockoutObservable<String>;
        currentCode: KnockoutObservable<number>;





        constructor() {
            var self = this;

            self.attendanceCode = ko.observable('');
            self.attendanceName = ko.observable('');
            self.sealStamp = ko.observableArray([]);
            self.attendanceItemListDaily = ko.observableArray([]);
            self.attendanceItemListMonthly = ko.observableArray([]);
            self.attendanceItemList = ko.observableArray([]);
            self.attendanceRecExpDaily = ko.observableArray([]);
            self.attendanceRecExpMonthly = ko.observableArray([]);
            self.attendanceRecItemList = ko.observableArray([]);
            self.sealName1 = ko.observable('');
            self.sealName2 = ko.observable('');
            self.sealName3 = ko.observable('');
            self.sealName4 = ko.observable('');
            self.sealName5 = ko.observable('');
            self.sealName6 = ko.observable('');


            self.currentCode = ko.observable(0);
            //            self.columns = ko.observableArray([
            //                { headerText: nts.uk.resource.getText('KWR002_86'), key: 'attendanceCode', width: 100 },
            //                { headerText: nts.uk.resource.getText('KWR002_87'), key: 'name', formatter: _.escape, width: 200 }
            //            ]);
            this.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KWR002_86'), prop: 'attendanceItemId', width: 100 },
                { headerText: nts.uk.resource.getText('KWR002_87'), prop: 'attendanceItemName', width: 200 }
            ]);
            self.useSealValue = ko.observable(true);
            self.useSeal = ko.observableArray([
                { code: true, name: nts.uk.resource.getText("KWR002_63") },
                { code: false, name: nts.uk.resource.getText("KWR002_64") }
            ]);
            self.tabs = ko.observableArray([
                { id: 'tab-1', title: nts.uk.resource.getText("KWR002_88"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-2', title: nts.uk.resource.getText("KWR002_89"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true) }
            ]);
            self.selectedTab = ko.observable('tab-1');

            self.selectedTab.subscribe((codeChange) => {
                if (codeChange === 'tab-1') {
                    //                    self.attendanceItemList([]);
                    self.attendanceItemList(self.attendanceItemListDaily());
                }
                else {
                    //                    self.attendanceItemList([]);
                    self.attendanceItemList(self.attendanceItemListMonthly());
                }
            })

            $('.sub').click((element) => {

                var bind = element.currentTarget.dataset.bind;

                var columnIndex: number = bind.substr(bind.indexOf('.') - 2, 1);

                var position = bind.substr(bind.indexOf('.') + 1);

                var attendanceItemName: String;

                var position: number;

                var exportAtr: number;

                if (position == "upperPosition") {
                    position = 1;
                }
                else position = 2;

                if (self.selectedTab() == "tab-1") {
                    exportAtr = 1;
                    if (position == 1)
                        attendanceItemName = self.attendanceRecExpDaily()[columnIndex].upperPosition;
                    else
                        attendanceItemName = self.attendanceRecExpDaily()[columnIndex].lowwerPosition;

                } else {
                    exportAtr = 2;
                    if (position == 1)
                        attendanceItemName = self.attendanceRecExpMonthly()[columnIndex].upperPosition;
                    else
                        attendanceItemName = self.attendanceRecExpMonthly()[columnIndex].lowwerPosition;
                }




                setShared('attendanceItem', {
                    attendanceItemName: attendanceItemName,
                    layoutCode: Number(self.attendanceCode()),
                    layoutName: self.attendanceName(),
                    columnIndex: columnIndex,
                    position: position,
                    exportAtr: exportAtr
                }, true)
                var link: string;
                if (exportAtr == 1 && columnIndex <= 6) link = '/view/kwr/002/d/index.xhtml';
                else link = '/view/kwr/002/e/index.xhtml';
                blockUI.grayout();

                nts.uk.ui.windows.sub.modal(link).onClosed(function(): any {
                    if (attendanceItemName == '') {

                        if (exportAtr == 1) {

                            self.attendanceRecExpDaily()[columnIndex].exportAtr = exportAtr;
                            self.attendanceRecExpDaily()[columnIndex].columnIndex = columnIndex;
                            self.attendanceRecExpDaily()[columnIndex].userAtr = false;

                        } else {

                            self.attendanceRecExpMonthly()[columnIndex].exportAtr = exportAtr;
                            self.attendanceRecExpMonthly()[columnIndex].columnIndex = columnIndex;
                            self.attendanceRecExpMonthly()[columnIndex].userAtr = false;

                        }


                    }
                    var attendanceItem = getShared('attendanceRecordExport');
                    if (attendanceItem) {
                        if (exportAtr == 1) {
                            if (position == 1)
                                self.attendanceRecExpDaily()[columnIndex].upperPosition = attendanceItem.attendanceItemName;
                            self.attendanceRecExpDaily()[columnIndex].lowwerPosition = attendanceItem.attendanceItemName;
                        } else {
                            if (position == 1)
                                self.attendanceRecExpMonthly()[columnIndex].upperPosition = attendanceItem.attendanceItemName;
                            self.attendanceRecExpMonthly()[columnIndex].lowwerPosition = attendanceItem.attendanceItemName;
                        }


                        var item: viewmodel.model.AttendanceRecItem;

                        item.layoutCode = attendanceItem.layoutCode;
                        item.layoutName = attendanceItem.layoutName;
                        item.columnIndex = attendanceItem.columnName;
                        item.position = attendanceItem.position;
                        item.exportAtr = attendanceItem.exportAtr;
                        item.attendanceId = attendanceItem.attendanceId;
                        item.attendanceItemName = attendanceItem.attendanceItemName;

                        self.updateAttendanceRecItemList(item);
                    }


                })

            });
        }

        decision(): void {
            var self = this;
            setShared('attendanceRecExpDaily', self.attendanceItemListDaily(), true);
            setShared('attendanceRecExpMonthly', self.attendanceItemListMonthly(), true);
            setShared('attendanceRecItemList', self.attendanceRecItemList(), true);
            setShared('useSeal', self.useSeal());
            nts.uk.ui.windows.close();
        }

        cancel(): void {
            setShared('attendanceRecExpDaily', null, true);
            setShared('attendanceRecExpMonthly', null, true);
            setShared('attendanceRecItemList', null, true);
            nts.uk.ui.windows.close();
        }

        findAttendanceRecItem(attendanceRecItem: viewmodel.model.AttendanceRecItem): number {
            var self = this;
            if (self.attendanceRecItemList().length == 0) return -1;
            var i: any;
            for (i = 0; i < self.attendanceRecItemList().length; i++) {
                var item = self.attendanceRecItemList()[i];
                if (item.exportAtr == attendanceRecItem.exportAtr && item.columnIndex == attendanceRecItem.columnIndex && item.position == attendanceRecItem.position)
                    return i;
            }
            return -1;
        }

        updateAttendanceRecItemList(attendanceRecItem: viewmodel.model.AttendanceRecItem): void {
            var self = this;
            var index = self.findAttendanceRecItem(attendanceRecItem);
            if (index == -1) {
                self.attendanceRecItemList().push(attendanceRecItem);
            }
            else self.attendanceRecItemList()[index] = attendanceRecItem;

        }
        start_page(): JQueryPromise<any> {
            blockUI.invisible();
            var self = this;
            var dfd = $.Deferred();

            let attendanceRecExpSetCode: any = getShared('attendanceRecExpSetCode');
            let attendanceRecExpSetName: any = getShared('attendanceRecExpSetName');
            let useSeal: any = getShared('useSeal');

            if (!attendanceRecExpSetCode) {
                self.attendanceCode('1');
                self.attendanceName('Default Name');
            }
            else {
                self.attendanceCode(attendanceRecExpSetCode);
                self.attendanceName(attendanceRecExpSetName);
            }
            self.useSealValue(useSeal == 1 ? true : false);

            var code: number = Number(self.attendanceCode());
            service.findAllAttendanceRecExportDaily(code).done(function(listattendanceRecExpDailyList: Array<model.AttendanceRecExp>) {
                if (listattendanceRecExpDailyList.length > 0) {
                    listattendanceRecExpDailyList.forEach(item => {
                        var columnIndex: number = item.columnIndex;
                        self.attendanceRecExpDaily()[columnIndex] = item;
                    })
                }

            });

            service.findAllAttendanceRecExportMonthly(code).done(function(listattendanceRecExpMonthlyList: Array<model.AttendanceRecExp>) {
                if (listattendanceRecExpMonthlyList.length > 0) {
                    listattendanceRecExpMonthlyList.forEach(item => {
                        var columnIndex: number = item.columnIndex;
                        self.attendanceRecExpMonthly()[columnIndex] = item;
                    })
                }

            });

            service.getSealStamp(code).done(function(sealStampList: Array<String>) {
                if (sealStampList.length > 0) {
                    self.sealName1(sealStampList[0]);
                    self.sealName2(sealStampList[1]);
                    self.sealName3(sealStampList[2]);
                    self.sealName4(sealStampList[3]);
                    self.sealName5(sealStampList[4]);
                    self.sealName6(sealStampList[5]);

                }
            });
            service.getAttendanceSingleList().done(function(listAttendanceItem: Array<model.AttendanceItem>) {
                if (listAttendanceItem.length != 0) {
                    self.attendanceItemListDaily(listAttendanceItem);
                    self.currentCode(1);
                }

            });

            service.getAttendanceCalculateList().done(function(listAttendanceItem: Array<model.AttendanceItem>) {
                if (listAttendanceItem.length > 0) {
                    self.attendanceItemListMonthly(listAttendanceItem);
                    listAttendanceItem.forEach(item => {
                        self.attendanceItemListDaily.push(item);
                    })

                    self.attendanceItemList(self.attendanceItemListDaily());
                }
                dfd.resolve();
            })

            blockUI.clear();
            return dfd.promise();
        }
    }

    export module model {

        export class AttendanceRecExp {

            exportAtr: number;
            columnIndex: number;
            userAtr: Boolean;
            upperPosition: String;
            lowwerPosition: String;

            constructor(exportAtr: number, columnIndex: number, userAtr: Boolean, upperPosition: String, lowwerPosition: String) {

                this.exportAtr = exportAtr;
                this.columnIndex = columnIndex;
                this.userAtr = userAtr;
                this.upperPosition = upperPosition;
                this.lowwerPosition = lowwerPosition;
            }
        }

        export class AttendanceRecItem {

            attendanceItemName: string;
            layoutCode: number;
            layoutName: string;
            columnIndex: number;
            position: number;
            exportAtr: number;
            attendanceId: any;
            constructor(attendanceItemName: string, layoutCode: number, layoutName: string, indexColumn: number, position: number, exportAtr: number, attendanceId: any) {
                this.attendanceItemName = attendanceItemName;
                this.layoutCode = layoutCode;
                this.layoutName = layoutName;
                this.columnIndex = indexColumn;
                this.position = position;
                this.exportAtr = exportAtr;
                this.attendanceId = attendanceId;
            }

        }
        export class AttendanceItem {
            attendanceItemId: number;
            attendanceItemName: String;
            screenUseItem: number;

            constructor(code: number, name: String, screenType: number) {
                this.attendanceItemId = code;
                this.attendanceItemName = name;
                this.screenUseItem = screenType;
            }
        }
    }

}