module nts.uk.at.view.kdl001.a {
    export module viewmodel {
        export class ScreenModel {
            columns: KnockoutObservableArray<NtsGridListColumn>;
            multiSelectMode: KnockoutObservable<boolean>;
            isSelection: KnockoutObservable<boolean> = ko.observable(false);
            selectAbleItemList: KnockoutObservableArray<WorkTimeSet>;
            selectAbleCodeList: KnockoutObservableArray<string>;
            selectedCodeList: KnockoutObservableArray<string>;
            selectedCode: KnockoutObservable<string>;
            searchOption: KnockoutObservable<number>;
            startTimeOption: KnockoutObservable<number>;
            startTime: KnockoutObservable<number>;
            endTimeOption: KnockoutObservable<number>;
            endTime: KnockoutObservable<number>;
            isEnableSwitchButton: KnockoutObservable<boolean> = ko.observable(false);
            gridHeight: number = 260;
            initialWorkTimeCodes: Array<String>;
            constructor() {
                var self = this;
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KDL001_12'), prop: 'code', width: 50 },
                    { headerText: nts.uk.resource.getText('KDL001_13'), prop: 'name', width: 130 },
                    { headerText: nts.uk.resource.getText('KDL001_14'), prop: 'workTime1', width: 200 },
                    //{ headerText: nts.uk.resource.getText('KDL001_15'), prop: 'workTime2', width: 200 }, //tam thoi comment theo yeu cau cua oohashi san
                    { headerText: nts.uk.resource.getText('KDL001_16'), prop: 'workAtr', width: 160 },
                    { headerText: nts.uk.resource.getText('KDL001_17'), prop: 'remark', template: '<span>${remark}</span>' }
                ]);
                self.multiSelectMode = nts.uk.ui.windows.getShared('kml001multiSelectMode');
                self.isSelection = nts.uk.ui.windows.getShared('kml001isSelection');
                if (!self.multiSelectMode) {
                    self.gridHeight = 260;
                } else {
                    self.gridHeight = 255;
                }
                self.selectAbleCodeList = ko.observableArray(<Array<string>>nts.uk.ui.windows.getShared('kml001selectAbleCodeList'));
                self.selectedCodeList = ko.observableArray(<Array<string>>nts.uk.ui.windows.getShared('kml001selectedCodeList'));
                self.selectedCode = ko.observable(null);
                self.searchOption = ko.observable(0);
                self.startTimeOption = ko.observable(1);
                self.startTime = ko.observable('');
                self.endTimeOption = ko.observable(1);
                self.endTime = ko.observable('');
                self.selectAbleItemList = ko.observableArray([]);
                self.initialWorkTimeCodes = ko.observableArray([]);
            }

            startPage(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                nts.uk.ui.block.invisible();
                kdl001.a.service.findByCodeList(self.selectAbleCodeList())
                    .done(function(data) {
                        data = _.sortBy(data, (item: any) => { return item.code; });
                            self.bindItemList(data);
                        if (!nts.uk.util.isNullOrEmpty(self.selectAbleItemList())) {
                            if (nts.uk.util.isNullOrEmpty(self.selectedCodeList())) {
                                self.selectedCodeList([_.first(self.selectAbleItemList()).code]);
                                self.selectedCode(_.first(self.selectAbleItemList()).code);
                            } else {
                                let valueSelect = _.find(self.selectAbleItemList(), data => {
                                    return data.code == _.first(self.selectedCodeList());
                                });
                                self.selectedCode(valueSelect != undefined ? _.first(self.selectedCodeList()) : "");
                            }
                        } else {
                            self.selectedCodeList([]);
                            self.selectedCode(null);
                        }
                        nts.uk.ui.block.clear();
                        dfd.resolve();
                    })
                    .fail(function(res) {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() { nts.uk.ui.block.clear(); });
                    });
                return dfd.promise();
            }
            bindItemList(data) {
                let self = this;
                self.selectAbleItemList().clear();
                self.selectAbleItemList.push(new WorkTimeSet());
                if (!nts.uk.util.isNullOrEmpty(data)) {
                    self.selectAbleItemList(self.selectAbleItemList().concat(_.map(data, item => { return new WorkTimeSet(item) })));
                }
                
                // Set initial work time list.
                self.initialWorkTimeCodes = _.map(self.selectAbleItemList(), function(item) { return item.code })
            }

            search() {
                nts.uk.ui.block.invisible();
                var self = this;
                let command = {
                    codelist: self.initialWorkTimeCodes,
                    startTime: nts.uk.util.isNullOrEmpty(self.startTime()) ? null : self.startTime(),
                    endTime: nts.uk.util.isNullOrEmpty(self.endTime()) ? null : self.endTime()
                }
                kdl001.a.service.findByTime(command)
                    .done(function(data) {
                        data = _.sortBy(data, (item: any) => { return item.code; });
                        self.selectAbleItemList(data);
                        if (!nts.uk.util.isNullOrEmpty(self.selectAbleItemList())) {
                            self.selectedCodeList([_.first(self.selectAbleItemList()).code]);
                            self.selectedCode(_.first(self.selectAbleItemList()).code);
                        } else {
                            self.selectedCodeList([]);
                            self.selectedCode(null);
                        }
                        nts.uk.ui.block.clear();
                    })
                    .fail(function(res) {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() { nts.uk.ui.block.clear(); });
                    });
            }

            returnData() {
                nts.uk.ui.block.invisible();
                var self = this;
                self.startTimeOption(1);
                self.startTime('');
                self.endTimeOption(1);
                self.endTime('');
                kdl001.a.service.findByCodeList(self.selectAbleCodeList())
                    .done(function(data) {
                        data = _.sortBy(data, (item: any) => { return item.code; });
                        self.bindItemList(data);
                        if (!nts.uk.util.isNullOrEmpty(self.selectAbleItemList())) {
                            if (nts.uk.util.isNullOrEmpty(self.selectedCodeList())) {
                                self.selectedCodeList([_.first(self.selectAbleItemList()).code]);
                            }
                            if(nts.uk.util.isNullOrEmpty(self.selectedCode())){
                                self.selectedCode(_.first(self.selectAbleItemList()).code);
                            }
                        } else {
                            self.selectedCodeList([]);
                            self.selectedCode(null);
                        }
                        nts.uk.ui.block.clear();
                    })
                    .fail(function(res) {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() { nts.uk.ui.block.clear(); });
                    });
                $("#inputStartTime").focus();
            }

            submitAndCloseDialog() {
                nts.uk.ui.block.invisible();
                var self = this;
                if (!self.multiSelectMode)
                    self.selectedCodeList(nts.uk.util.isNullOrUndefined(self.selectedCode()) ? [] : [self.selectedCode()]);
                if (self.selectedCodeList().length == 0) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_29" }).then(function() { nts.uk.ui.block.clear(); });
                } else {
                    nts.uk.ui.windows.setShared('kml001selectedCodeList', self.selectedCodeList());
                    nts.uk.ui.windows.setShared('kml001selectedTimes', self.getSelectedTimeItems(self.selectedCodeList()));
                    nts.uk.ui.block.clear();
                    nts.uk.ui.windows.close();
                }
            }

            getSelectedTimeItems(codes) {
                let self = this;
                var timeItems = _.filter(self.selectAbleItemList(), item => { return codes.indexOf(item.code) > -1 });
                var mappedItems = _.map(timeItems, item => { return new TimeItem(item) });
                return mappedItems;
            }

            closeDialog() {
                nts.uk.ui.windows.close();
            }
        }
        export class TimeItem {
            selectedWorkTimeCode: string;
            selectedWorkTimeName: string;
            first: Time;
            second: Time;
            constructor(data?) {
                if (data) {
                    this.selectedWorkTimeCode = data.code;
                    this.selectedWorkTimeName = data.name;
                    this.first = new Time(data.firstStartTime, data.firstEndTime);
                    this.second = new Time(data.secondStartTime, data.secondEndTime);
                }
            }
        }
        export class Time {
            start: number;
            end: number;
            constructor(startTime, endTime) {
                this.start = startTime;
                this.end = endTime;
            }

        }

        export class WorkTimeSet {
            code: string = "";
            name: string = "選択なし";
            workTime1: string = "";
            workTime2: string = "";
            workAtr: string = "";
            remark: string = "";
            firstStartTime: number = null;
            firstEndTime: number = null;
            secondStartTime: number = null;
            secondEndTime: number = null;
            constructor(data?) {
                if (data) {
                    this.code = data.code;
                    this.name = data.name;
                    this.workTime1 = data.workTime1;
                    this.workTime2 = data.workTime2;
                    this.workAtr = data.workAtr;
                    this.remark = data.remark;
                    this.firstStartTime = data.firstStartTime;
                    this.firstEndTime = data.firstEndTime;
                    this.secondStartTime = data.secondStartTime;
                    this.secondEndTime = data.secondEndTime;

                }

            }

        }

        interface IWorkTimeSet {
            code: string;
            name: string;
            workTime1: string;
            //workTime2: string;
            workAtr: string;
            remark: string;



        }
    }
}