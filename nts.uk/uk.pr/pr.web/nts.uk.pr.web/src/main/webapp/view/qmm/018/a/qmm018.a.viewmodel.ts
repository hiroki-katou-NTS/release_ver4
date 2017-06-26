module qmm018.a.viewmodel {
    export class ScreenModel {
        averagePay: KnockoutObservable<AveragePay>;
        texteditor3: KnockoutObservable<any>;
        texteditor1: KnockoutObservable<any>;
        isUpdate: boolean;
        dirty: nts.uk.ui.DirtyChecker;
        constructor() {
            var self = this;
            self.averagePay = ko.observable(new AveragePay(null, null, null, null, null, null));
            self.texteditor3 = ko.observable({
                value: ko.computed(function() {
                    let s: string;
                    ko.utils.arrayForEach(self.averagePay().selectedSalaryItems(), function(item) { if (!s) { s = item.itemAbName } else { s += " + " + item.itemAbName } });
                    return s;
                })
            });
            self.texteditor1 = ko.observable({
                value: ko.computed(function() {
                    let s: string;
                    ko.utils.arrayForEach(self.averagePay().selectedAttendItems(), function(item) { if (!s) { s = item.itemAbName } else { s += " + " + item.itemAbName } });
                    return s;
                })
            });
            self.isUpdate = false;
            self.dirty = new nts.uk.ui.DirtyChecker(self.averagePay);
        }

        /**
         * get init data
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            // get average pay items
            qmm018.a.service.averagePayItemSelect().done(function(data) {
                if (data) {
                    // if data exist go to update case
                    self.averagePay(
                        new AveragePay(
                            data.roundTimingSet,
                            data.attendDayGettingSet,
                            data.roundDigitSet,
                            data.exceptionPayRate,
                            data.itemsSalary,
                            data.itemsAttend));
                    self.isUpdate = true;
                    self.dirty.reset();
                } else {
                    // if data no exist go to insert case
                    self.averagePay(new AveragePay(0, 0, 0, null, null, null));
                    self.isUpdate = false;
                    self.dirty.reset();
                }
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }

        /**
         * save average setting
         */
        saveData(isUpdate: boolean): void {
            var self = this;
            let error = false;
            // check errors on required

            if (!self.averagePay().selectedSalaryItems().length) { $("#inp-3").ntsError('set', shr.viewmodelbase.Error.ER007); error = true; }
            if (self.averagePay().attendDayGettingSet() && !self.averagePay().selectedAttendItems().length) { $("#inp-1").ntsError('set', shr.viewmodelbase.Error.ER007); error = true; }
            if (self.averagePay().exceptionPayRate() == null) { $("#inp-2").ntsError('set', shr.viewmodelbase.Error.ER001); error = true; }

            // insert or update if no error
            if (!error && self.dirty.isDirty()) {
                //create data
                let command = {
                    roundTimingSet: self.averagePay().roundTimingSet(),
                    attendDayGettingSet: self.averagePay().attendDayGettingSet(),
                    roundDigitSet: self.averagePay().roundDigitSet(),
                    exceptionPayRate: self.averagePay().exceptionPayRate(),
                    selectedSalaryItems: _.map(self.averagePay().selectedSalaryItems(), function(o) { return o.itemCode; }),
                    selectedAttendItems: _.map(self.averagePay().selectedAttendItems(), function(o) { return o.itemCode; })
                }
                if (isUpdate) {
                    qmm018.a.service.averagePayItemUpdate(command).done(function(data) {
                        self.dirty.reset();
                    }).fail(function(res) {
                        self.processErrorResponse(res,command);
                    });
                } else {
                    qmm018.a.service.averagePayItemInsert(command).done(function(data) {
                        self.dirty.reset();
                    }).fail(function(res) {
                        self.processErrorResponse(res,command);
                    });
                }
            }
        }

        /**
         * open B screen
         */
        openSubWindow(n: number): void {
            var self = this;
            if (!n) {
                // set salary data
                nts.uk.ui.windows.setShared('selectedItemList', self.averagePay().selectedSalaryItems());
                nts.uk.ui.windows.setShared('categoryAtr', shr.viewmodelbase.CategoryAtr.PAYMENT);
            } else {
                // set attend data
                nts.uk.ui.windows.setShared('selectedItemList', self.averagePay().selectedAttendItems());
                nts.uk.ui.windows.setShared('categoryAtr', shr.viewmodelbase.CategoryAtr.PERSONAL_TIME);
            }
            nts.uk.ui.windows.sub.modal("/view/qmm/018/b/index.xhtml", { title: "対象項目の選択", dialogClass: "no-close" }).onClosed(function() {
                let selectedList: Array<shr.viewmodelbase.ItemModel> = nts.uk.ui.windows.getShared('selectedItemList'); // Get selected form B screen, n = 0: ItemSalary, n = 2: ItemAttend
                if (!n) {
                    // set data to salary item list 
                    self.loadData(selectedList, self.averagePay().selectedSalaryItems, _.isEqual(selectedList, self.averagePay().selectedSalaryItems()));
                } else {
                    // set data to attend item list 
                    self.loadData(selectedList, self.averagePay().selectedAttendItems, _.isEqual(selectedList, self.averagePay().selectedAttendItems()));
                }
            });
        }

        /**
         * set data to KnockoutObservableArray from Array source
         */
        loadData(dataSource: Array<any>, dataDestination: KnockoutObservableArray<any>, isDataEqual: boolean): void {
            if (dataSource.length) {
                if (!isDataEqual) {
                    dataDestination.removeAll();
                    dataSource.forEach(function(item) { dataDestination.push(new shr.viewmodelbase.ItemModel(item.itemCode, item.itemAbName)) });
                }
            } else {
                dataDestination([]);
            }
        }
        
        /**
         * process response error
         */
        processErrorResponse(res: any, command: any) {
            if (res.messageId == "ER001") {
                $("#inp-2").ntsError('set', shr.viewmodelbase.Error.ER001);
            }
            if (res.messageId == "ER007") {
                if (command.selectedSalaryItems.length == 0) {
                    $("#inp-3").ntsError('set', shr.viewmodelbase.Error.ER007);
                }
                if(command.attendDayGettingSet && command.selectedAttendItems.length == 0) {
                    $("#inp-1").ntsError('set', shr.viewmodelbase.Error.ER007);
                }
            }
        }
    }

    class AveragePay {
        roundTimingSet: KnockoutObservable<number>;
        attendDayGettingSet: KnockoutObservable<number>;
        roundDigitSet: KnockoutObservable<number>;
        exceptionPayRate: KnockoutObservable<number>;
        oldExceptionPayRate: KnockoutObservable<number>;
        selectedSalaryItems: KnockoutObservableArray<shr.viewmodelbase.ItemModel>;
        selectedAttendItems: KnockoutObservableArray<shr.viewmodelbase.ItemModel>;
        constructor(roundTimingSet: number, attendDayGettingSet: number, roundDigitSet: number, exceptionPayRate: number, selectedSalaryItems: Array<any>, selectedAttendItems: Array<any>) {
            var self = this;
            self.roundTimingSet = ko.observable(roundTimingSet);
            self.attendDayGettingSet = ko.observable(attendDayGettingSet);
            self.roundDigitSet = ko.observable(roundDigitSet);
            self.exceptionPayRate = ko.observable(exceptionPayRate);
            self.oldExceptionPayRate = ko.observable(exceptionPayRate);
            self.selectedSalaryItems = ko.observableArray(selectedSalaryItems);
            self.selectedAttendItems = ko.observableArray(selectedAttendItems);
            self.roundTimingSet.subscribe(function(value) { self.roundTimingSet(value ? 1 : 0); });
            self.attendDayGettingSet.subscribe(function(value) {
                if (!value) {
                    self.roundDigitSet(0);
                    $("#inp-1").ntsError('clear');
                    self.selectedAttendItems([]);
                }
            });
            self.exceptionPayRate.subscribe(function(value) {
                if ($("#inp-2").ntsError('hasError')) { self.oldExceptionPayRate(exceptionPayRate); }
                else { exceptionPayRate = value; self.oldExceptionPayRate(value); }
            });
            self.selectedSalaryItems.subscribe(function(value) {
                if (!value.length) $("#inp-3").ntsError('set', shr.viewmodelbase.Error.ER007); else $("#inp-3").ntsError('clear');
            });
            self.selectedAttendItems.subscribe(function(value) {
                if (!self.attendDayGettingSet()) $("#inp-1").ntsError('clear');
                else {
                    if (!value.length) $("#inp-1").ntsError('set', shr.viewmodelbase.Error.ER007); else $("#inp-1").ntsError('clear');
                }
            });
        }
    }
}