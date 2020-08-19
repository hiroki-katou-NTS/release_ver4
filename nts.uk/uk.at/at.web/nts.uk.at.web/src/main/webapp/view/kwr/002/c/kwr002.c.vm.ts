module nts.uk.com.view.kwr002.c.viewmodel {
    import blockUI = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    export class ScreenModel {

        attendanceCode: KnockoutObservable<String>;
        attendanceName: KnockoutObservable<String>;
        useSeal: KnockoutObservableArray<any>;
        sealStamp: KnockoutObservableArray<String>;
        useSealValue: KnockoutObservable<any>;
        attendanceItemListDaily: KnockoutObservableArray<viewmodel.model.AttendanceItem>;
        attendanceItemListMonthly: KnockoutObservableArray<viewmodel.model.AttendanceItem>;
        attendanceItemList: KnockoutObservableArray<viewmodel.model.AttendanceItem>;
        attendanceRecExpDaily: KnockoutObservableArray<viewmodel.model.AttendanceRecExp>;
        attendanceRecExpMonthly: KnockoutObservableArray<viewmodel.model.AttendanceRecExp>;
        attendanceRecItemList: KnockoutObservableArray<viewmodel.model.AttendanceRecItem>;

        confirmMarkC18_1: KnockoutObservableArray<any>;
        confirmMarkValue: KnockoutObservable<any>;
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

        // Condition Ver25
        condition2: KnockoutObservable<boolean>;
        condition3: KnockoutObservable<boolean>;
        condition4: KnockoutObservable<boolean>;
        condition5: KnockoutObservable<boolean>;

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
            self.condition2 = ko.observable(false);
            self.condition3 = ko.observable(false);
            self.condition4 = ko.observable(false);
            self.condition5 = ko.observable(false);

            //            console.log(self.attendanceCode());

            this.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KWR002_86'), key: 'attendanceItemId', width: 100 },
                { headerText: nts.uk.resource.getText('KWR002_87'), key: 'attendanceItemName', width: 200 }
            ]);
            self.useSealValue = ko.observable(true);
            self.useSeal = ko.observableArray([
                { code: true, name: nts.uk.resource.getText("KWR002_63") },
                { code: false, name: nts.uk.resource.getText("KWR002_64") }
            ]);
            self.confirmMarkValue = ko.observable(true);

            self.confirmMarkC18_1 = ko.observableArray([
                { code: true, name: nts.uk.resource.getText("KWR002_195") },
                { code: false, name: nts.uk.resource.getText("KWR002_196") }
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

                //                console.log($(element).attr('id'));

                var bind = (element.currentTarget as any).dataset.bind;

                var openSign: number = bind.indexOf('[');
                var closeSign: number = bind.indexOf(']');
                var columnIndex: number;
                if (closeSign - openSign > 2)
                    columnIndex = bind.substr(bind.indexOf('.') - 3, 2);
                else
                    columnIndex = bind.substr(bind.indexOf('.') - 2, 1);


                var position = bind.substr(bind.indexOf('.') + 1);

                var attendanceItemName: String;

                var positionTop: any = $(element).hasClass('top');
                var positionBot: any = $(element).hasClass('bot');

                var position: number;

                var exportAtr: number;

                var attItem: model.AttendanceRecExp;
                
                if (position.search("upper") != -1) {
                    position = 1;
                }
                else position = 2;

                // Set param to share with dialog kdl047
                const shareParam: model.SharedParams = new model.SharedParams();
                if (self.selectedTab() == "tab-1") {
                    exportAtr = 1;
                    if (position == 1)
                        if (!self.attendanceRecExpDaily()[columnIndex].upperPosition()) attendanceItemName = "";
                        else attendanceItemName = self.attendanceRecExpDaily()[columnIndex].upperPosition();
                    else
                        if (!self.attendanceRecExpDaily()[columnIndex].lowwerPosition()) attendanceItemName = "";
                        else attendanceItemName = self.attendanceRecExpDaily()[columnIndex].lowwerPosition();

                    attItem = self.attendanceRecExpDaily()[columnIndex];

                    // Set param to share with dialog kdl047
                    shareParam.titleLine.displayFlag = true;
                    shareParam.titleLine.layoutCode = self.attendanceCode();
                    shareParam.titleLine.layoutName = self.attendanceName();
                    const positionText = position == 1 ? "上" : "下";
                    shareParam.titleLine.directText = getText('KWR002_131')+ columnIndex + getText('KWR002_132') + positionText + getText('KWR002_133');
                    shareParam.itemNameLine.displayFlag = true;
                    shareParam.itemNameLine.displayInputCategory = 2;
                    shareParam.itemNameLine.name = attendanceItemName;
                    shareParam.attribute.selectionCategory = 2;
                    shareParam.attribute.attributeList = [
                        new model.AttendaceType(1, getText('KWR002_141')),
                        new model.AttendaceType(2, getText('KWR002_142')),
                        new model.AttendaceType(3, getText('KWR002_143'))
                    ]
                    shareParam.attribute.selected = attItem.exportAtr;
                    shareParam.diligenceProjectList = [

                    ]
                    shareParam.selectedTime = 3;
                } else {
                    exportAtr = 2;
                    if (position == 1)
                        if (!self.attendanceRecExpMonthly()[columnIndex].upperPosition()) attendanceItemName = "";
                        else attendanceItemName = self.attendanceRecExpMonthly()[columnIndex].upperPosition();
                    else
                        if (!self.attendanceRecExpMonthly()[columnIndex].lowwerPosition()) attendanceItemName = "";
                        else attendanceItemName = self.attendanceRecExpMonthly()[columnIndex].lowwerPosition();
                    attItem = self.attendanceRecExpMonthly()[columnIndex];

                    // Set param to share with dialog kdl048
                    shareParam.titleLine.displayFlag = true;
                    shareParam.titleLine.layoutCode = self.attendanceCode();
                    shareParam.titleLine.layoutName = self.attendanceName();
                    const positionText = position == 1 ? "上" : "下";
                    shareParam.titleLine.directText = getText('KWR002_131')+ columnIndex + getText('KWR002_132') + positionText + getText('KWR002_133');
                    shareParam.itemNameLine.displayFlag = true;
                    shareParam.itemNameLine.displayInputCategory = 2;
                    shareParam.itemNameLine.name = attendanceItemName;
                    shareParam.attribute.selectionCategory = 2;
                    shareParam.attribute.attributeList = [
                        new model.AttendaceType(4, getText('KWR002_171')),
                        new model.AttendaceType(5, getText('KWR002_172')),
                        new model.AttendaceType(7, getText('KWR002_173'))
                    ]
                    shareParam.attribute.selected = attItem.exportAtr;
                    shareParam.diligenceProjectList = [
                        new model.DiligenceProject(27, '予定時間', '予定時間', 27),
                        new model.DiligenceProject(216, '残業１', '残業１', 216),
                        new model.DiligenceProject(461, '勤務回数', '勤務回数', 461),
                        new model.DiligenceProject(534, '休憩回数', '休憩回数', 534),
                        new model.DiligenceProject(641, 'aaaaaaaaa', 'aaaaaaaaa', 641),
                        new model.DiligenceProject(642, 'tukijikan', 'tukijikan', 642),
                        new model.DiligenceProject(643, 'tukikin', 'tukikin', 643),
                        new model.DiligenceProject(644, '出有ｵﾝ無ｵﾌ有ｶｳﾝﾄ（日次ﾄﾘｶﾞ）ｄ', '出有ｵﾝ無ｵﾌ有ｶｳﾝﾄ（日次ﾄﾘｶﾞ）ｄ', 644),
                        new model.DiligenceProject(645, '出有ｵﾝ有ｵﾌ無ｶｳﾝﾄ（日次ﾄﾘｶﾞ）（bb）', '出有ｵﾝ有ｵﾌ無ｶｳﾝﾄ（日次ﾄﾘｶﾞ）（bb）', 645),
                        new model.DiligenceProject(680, '任意項目４０', '任意項目４０', 680),
                        new model.DiligenceProject(681, '任意項目４１', '任意項目４１', 681),
                        new model.DiligenceProject(682, '任意項目４２月別', '任意項目４２月別', 682),
                        new model.DiligenceProject(683, '任意項目４３', '任意項目４３', 683),
                        new model.DiligenceProject(267, '振替休日１', '振替休日１', 267),
                        new model.DiligenceProject(268, '計算休日出勤１', '計算休日出勤１', 268),
                        new model.DiligenceProject(269, '計算振替休日１', '計算振替休日１', 269),
                        new model.DiligenceProject(270, '事前休日出勤１', '事前休日出勤１', 270)
                    ]
                    shareParam.selectedTime = 3;
                }
                // End set param


                var attendanceItemTemp: model.AttendanceRecItem = new model.AttendanceRecItem("", 0, "", columnIndex, position, exportAtr, null, 0);

                var index: number = self.findAttendanceRecItem(attendanceItemTemp);
                setShared('attendanceItem', shareParam, true);
                var link: string;
                if (exportAtr == 1 && columnIndex <= 9) link = '/view/kwr/002/d/index.xhtml';
                else link = '/view/kdl/048/index.xhtml';
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
                            if (position == 1) {
                                self.attendanceRecExpDaily()[columnIndex].upperPosition(attendanceItem.attendanceItemName);
                                self.attendanceRecExpDaily()[columnIndex].upperShow(true);
                            }
                            else {
                                self.attendanceRecExpDaily()[columnIndex].lowwerPosition(attendanceItem.attendanceItemName);
                                self.attendanceRecExpDaily()[columnIndex].lowerShow(true);
                            }
                        } else {
                            if (position == 1) {
                                self.attendanceRecExpMonthly()[columnIndex].upperPosition(attendanceItem.attendanceItemName);
                                self.attendanceRecExpMonthly()[columnIndex].upperShow(true);
                            }
                            else {
                                self.attendanceRecExpMonthly()[columnIndex].lowwerPosition(attendanceItem.attendanceItemName);
                                self.attendanceRecExpMonthly()[columnIndex].lowerShow(true);
                            }
                        }


                        var item: viewmodel.model.AttendanceRecItem;
                        item = new model.AttendanceRecItem(attendanceItem.attendanceItemName, attendanceItem.layoutCode, attendanceItem.layoutName,
                            attendanceItem.columnIndex, attendanceItem.position, attendanceItem.exportAtr,
                            attendanceItem.attendanceId, attendanceItem.attribute)
                        self.updateAttendanceRecItemList(item);

                    }
                })

            });
        }

        decision(): void {
            var self = this;
            var attendanceRecExpDailyRes: viewmodel.model.AttendanceRecExpRespond[] = [];
            if (!self.attendanceRecExpDaily()[0]) {
                attendanceRecExpDailyRes.push(new viewmodel.model.AttendanceRecExpRespond(0, 0, false, null, null));
            }
            var attendanceRecExpMonthlyRes: viewmodel.model.AttendanceRecExpRespond[] = [];
            if (!self.attendanceRecExpMonthly()[0]) {
                attendanceRecExpMonthlyRes.push(new viewmodel.model.AttendanceRecExpRespond(0, 0, false, null, null));
            }

            self.attendanceRecExpDaily().forEach((item) => {
                attendanceRecExpDailyRes.push(new viewmodel.model.AttendanceRecExpRespond(item.exportAtr, item.columnIndex, item.userAtr, item.upperPosition(), item.lowwerPosition()));
            });
            self.attendanceRecExpMonthly().forEach((item) => {
                attendanceRecExpMonthlyRes.push(new viewmodel.model.AttendanceRecExpRespond(item.exportAtr, item.columnIndex, item.userAtr, item.upperPosition(), item.lowwerPosition()));
            });
            setShared('attendanceRecExpDaily', attendanceRecExpDailyRes, true);
            setShared('attendanceRecExpMonthly', attendanceRecExpMonthlyRes, true);
            setShared('attendanceRecItemList', self.attendanceRecItemList(), true);

            self.sealStamp().push(self.sealName1());
            self.sealStamp().push(self.sealName2());
            self.sealStamp().push(self.sealName3());
            self.sealStamp().push(self.sealName4());
            self.sealStamp().push(self.sealName5());
            self.sealStamp().push(self.sealName6());
            setShared('sealStamp', _.reject(self.sealStamp(), (it) => _.isEmpty(it)), true);
            setShared('useSeal', self.useSealValue());
            nts.uk.ui.windows.close();
        }

        cancel(): void {
            // setShared('attendanceRecExpDaily', null, true);
            // setShared('attendanceRecExpMonthly', null, true);
            // setShared('attendanceRecItemList', null, true);
            // setShared('sealStamp', null, true);
            // setShared('useSeal', null, true);
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

            let attendanceRecExpDaily = getShared('attendanceRecExpDaily');
            let attendanceRecExpMonthly = getShared('attendanceRecExpMonthly');
            let attendanceRecItemList = getShared('attendanceRecItemList');
            let attendanceRecExpSetCode: any = getShared('attendanceRecExpSetCode');
            let attendanceRecExpSetName: any = getShared('attendanceRecExpSetName');
            let sealStamp: any = getShared('sealStamp');
            let useSeal: any = getShared('useSeal');

            if (!attendanceRecExpSetCode) {
                self.attendanceCode('1');
                self.attendanceName('デフォルト名');
            }
            else {
                self.attendanceCode(attendanceRecExpSetCode);
                self.attendanceName(attendanceRecExpSetName);
            }

            // Ver25: Add display item control
            const fontSize = getShared('fontSize');
            self.condition2(fontSize === 'small' || fontSize === 'medium');
            self.condition3(fontSize === 'small');

            service.getApprovalProcessingUseSetting().done(response => {
                self.condition4(response.useMonthApproverConfirm)
                self.condition5(response.useMonthApproverConfirm)
            })
            // End Ver25: Add display item control

            if (self.condition5()) {
                $('#switch-button').focusComponent();
            }

            if (attendanceRecExpDaily != null || attendanceRecExpMonthly != null || attendanceRecItemList != null) {
                self.useSealValue(useSeal);
                attendanceRecExpDaily.forEach((item: any) => {
                    var columnIndex: number = item.columnIndex;
                    self.attendanceRecExpDaily()[columnIndex] = new viewmodel.model.AttendanceRecExp(item.exportAtr, item.columnIndex, item.userAtr, item.upperPosition + "", item.lowwerPosition + "");
                });

                for (var i: number = 1; i <= 13; i++) {
                        if (!self.attendanceRecExpDaily()[i]) {
                            self.attendanceRecExpDaily()[i] = new viewmodel.model.AttendanceRecExp(1, i, false, "", "");
                        }
                    }

                attendanceRecExpMonthly.forEach((item: any) => {
                    var columnIndex: number = item.columnIndex;
                    self.attendanceRecExpMonthly()[columnIndex] = new viewmodel.model.AttendanceRecExp(item.exportAtr, item.columnIndex, item.userAtr, item.upperPosition + "", item.lowwerPosition + "");

                });

                 for (var i: number = 1; i <= 16; i++) {
                        if (!self.attendanceRecExpMonthly()[i]) {
                            self.attendanceRecExpMonthly()[i] = new viewmodel.model.AttendanceRecExp(2, i, false, "", "");
                        }
                    }

                self.attendanceRecItemList(attendanceRecItemList);
                self.sealName1(sealStamp[0]);
                self.sealName2(sealStamp[1]);
                self.sealName3(sealStamp[2]);
                self.sealName4(sealStamp[3]);
                self.sealName5(sealStamp[4]);
                self.sealName6(sealStamp[5]);

                dfd.resolve();
            }
            else {
                self.useSealValue(useSeal)
                var code: number = Number(self.attendanceCode());
                   $.when(service.findAllAttendanceRecExportDaily(code), service.findAllAttendanceRecExportMonthly(code), service.getSealStamp(code)).done((listattendanceRecExpDailyList: Array<model.AttendanceRecExp>, listattendanceRecExpMonthlyList: Array<model.AttendanceRecExp>,sealStampList: Array<String>) => {
                       if (listattendanceRecExpDailyList.length > 0) {
                           listattendanceRecExpDailyList.forEach(item => {
                               var columnIndex: number = item.columnIndex;
                               self.attendanceRecExpDaily()[columnIndex] = new viewmodel.model.AttendanceRecExp(item.exportAtr, item.columnIndex, item.userAtr, item.upperPosition + "", item.lowwerPosition + "");
                           })
                       }
                       for (var i: number = 1; i <= 13; i++) {
                           if (!self.attendanceRecExpDaily()[i]) {
                               self.attendanceRecExpDaily()[i] = new viewmodel.model.AttendanceRecExp(1, i, false, "", "");
                           }
                       }

                       if (listattendanceRecExpMonthlyList.length > 0) {
                           listattendanceRecExpMonthlyList.forEach(item => {
                               var columnIndex: number = item.columnIndex;
                               self.attendanceRecExpMonthly()[columnIndex] = new viewmodel.model.AttendanceRecExp(item.exportAtr, item.columnIndex, item.userAtr, item.upperPosition + "", item.lowwerPosition + "");
                           })
                       }
                       for (var i: number = 1; i <= 16; i++) {
                           if (!self.attendanceRecExpMonthly()[i]) {
                               self.attendanceRecExpMonthly()[i] = new viewmodel.model.AttendanceRecExp(2, i, false, "", "");
                           }
                       }

                       if (sealStampList.length > 0) {
                           self.sealName1(sealStampList[0]);
                           self.sealName2(sealStampList[1]);
                           self.sealName3(sealStampList[2]);
                           self.sealName4(sealStampList[3]);
                           self.sealName5(sealStampList[4]);
                           self.sealName6(sealStampList[5]);

                       }

                       dfd.resolve();
                })

            }
            blockUI.clear();
            return dfd.promise();
            //***
            //            service.getAttendanceSingleList().done(function(listAttendanceItem: Array<model.AttendanceItem>) {
            //                if (listAttendanceItem.length != 0) {
            //                    self.attendanceItemListDaily(listAttendanceItem);
            //                    self.currentCode(1);
            //                }
            //
            //            });
            //
            //            service.getAttendanceCalculateList(2).done(function(listAttendanceItem: Array<model.AttendanceItem>) {
            //                if (listAttendanceItem.length > 0) {
            //                    self.attendanceItemListMonthly( _.orderBy(listAttendanceItem, [e => Number(e.attendanceItemId)], ['asc']));
            //                }
            //            });
            //
            //            service.getAttendanceCalculateList(1).done(function(listAttendanceItem: Array<model.AttendanceItem>) {
            //                if (listAttendanceItem.length > 0) {
            //
            //                    listAttendanceItem.forEach(item => {
            //                        self.attendanceItemListDaily.push(item);
            //                    })
            //
            //                    self.attendanceItemListDaily(_.orderBy(self.attendanceItemListDaily(), [e => Number(e.attendanceItemId)], ['asc']));
            //                    self.attendanceItemList(self.attendanceItemListDaily());
            //                }
            //
            //
            //            })




        }

        setSharedParams(columnIndex: string, position: number): any {
            const vm = this;
            let params: any = {};
            params.layoutCode = vm.attendanceCode();
            params.layoutName = vm.attendanceName();
            const positionText = position == 1 ? "上" : "下";
            params.directText = getText('KWR002_131')
                + columnIndex
                + getText('KWR002_132')
                + positionText
                + getText('KWR002_133');
            params.itemNameLineDisplayFlag = true;
            params.itemNameIndicatesInputDivision = 2;
            // c12_1~6

            params.attributeSelectionCategory = 2;
            params.attributesList = {
                1: getText('KWR002_141'),
                2: getText('KWR002_141'),
                3: getText('KWR002_141')
            };
            params.selectedAttribute = vm.attendanceRecItemList()[columnIndex].attribute;
            // 勤怠項目ID<List>
            params.attendanceItemIds;
            // 勤怠項目名称 <List>
            params.attendanceItemNames;
            // 勤怠項目の属性<List>
            params.attendanceItemAttrs;
            // 勤怠項目の表示番号<List>
            params.attendanceItemDisplayNumber;
            // 選択済み勤怠項目ID
            params.attendanceItemSelectedId =vm.attendanceRecItemList()[columnIndex].attendanceId;
            return params;
        }
    }

    export module model {

        export class AttendanceRecExp {

            exportAtr: number;
            columnIndex: number;
            userAtr: Boolean;
            upperPosition: KnockoutObservable<string>;
            lowwerPosition: KnockoutObservable<string>;
            upperShow: KnockoutObservable<boolean>;
            lowerShow: KnockoutObservable<boolean>;

            constructor(exportAtr: number, columnIndex: number, userAtr: Boolean, upperPosition: string, lowwerPosition: string) {
                var self = this;
                self.exportAtr = exportAtr;
                self.columnIndex = columnIndex;
                self.userAtr = userAtr;
                self.upperPosition = ko.observable(upperPosition);
                self.lowwerPosition = ko.observable(lowwerPosition);

                self.upperShow = self.upperPosition() == "" ? ko.observable(false) : ko.observable(true);
                self.lowerShow = self.lowwerPosition() == "" ? ko.observable(false) : ko.observable(true);
            }
        }

        export class AttendanceRecExpRespond {

            exportAtr: number;
            columnIndex: number;
            userAtr: Boolean;
            upperPosition: string;
            lowwerPosition: string;

            constructor(exportAtr: number, columnIndex: number, userAtr: Boolean, upperPosition: string, lowwerPosition: string) {

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
            attribute: number;
            constructor(attendanceItemName: string, layoutCode: number, layoutName: string, indexColumn: number, position: number, exportAtr: number, attendanceId: any, attribute: number) {
                this.attendanceItemName = attendanceItemName;
                this.layoutCode = layoutCode;
                this.layoutName = layoutName;
                this.columnIndex = indexColumn;
                this.position = position;
                this.exportAtr = exportAtr;
                this.attendanceId = attendanceId;
                this.attribute = attribute;
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

        export class ApprovalProcessingUseSetting {
            companyId: string;
            useDayApproverConfirm: boolean;
            useMonthApproverConfirm: boolean;
            lstJobTitleNotUse: string[];
            supervisorConfirmErrorAtr: ConfirmationOfManagerOrYouself;
        }

        export class ConfirmationOfManagerOrYouself {
            value: number;
            nameId: string;
        }

        // Display object mock
        export class SharedParams {
            // タイトル行
            titleLine: TitleLineObject = new TitleLineObject();
            // 項目名行
            itemNameLine: ItemNameLineObject = new ItemNameLineObject();
            // 属性
            attribute: AttributeObject = new AttributeObject();
            // List<勤怠項目>
            diligenceProjectList: DiligenceProject[] = [];
            // 選択済み勤怠項目ID
            selectedTime: number = 0;

            constructor(init?: Partial<SharedParams>) {
                (<any>Object).assign(this, init);
            }
        }
        
        export class TitleLineObject {
            // 表示フラグ
            displayFlag: boolean = false;
            // 出力項目コード
            layoutCode: String | null = null;
            // 出力項目名
            layoutName: String | null = null;
            // コメント
            directText: String | null = null;

            constructor(init?: Partial<TitleLineObject>) {
                (<any>Object).assign(this, init);
            }
        }
        export class ItemNameLineObject {
            // 表示フラグ
            displayFlag: boolean = false;
            // 表示入力区分
            displayInputCategory: number = 1;
            // 名称
            name: String | null = null;

            constructor(init?: Partial<ItemNameLineObject>) {
                (<any>Object).assign(this, init);
            }
        }
        export class AttributeObject {
            // 選択区分
            selectionCategory: number = 2;
            // List<属性>
            attributeList: AttendaceType[] = [];
            // 選択済み
            selected: number = 1;

            constructor(init?: Partial<AttributeObject>) {
                (<any>Object).assign(this, init);
            }
        }

        export class AttendaceType {
            attendanceTypeCode: number;
            attendanceTypeName: string;
            constructor(attendanceTypeCode: number, attendanceTypeName: string) {
                this.attendanceTypeCode = attendanceTypeCode;
                this.attendanceTypeName = attendanceTypeName;
            }
        }

        export class DiligenceProject {
            // ID
            id: any;
            // 名称
            name: any;
            // 属性
            attributes: any;
            // 表示番号
            indicatesNumber: any;

            constructor(id: any, name: any, attributes: any, indicatesNumber:any) {
                this.id = id;
                this.name = name;
                this.attributes = attributes;
                this.indicatesNumber = indicatesNumber;
            }
        }
            }

}