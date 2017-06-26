/// <reference path="../qmm005.ts"/>
module qmm005.b {
    export class ViewModel {
        toggle: KnockoutObservable<boolean> = ko.observable(true);
        processingNo: KnockoutObservable<number> = ko.observable(0);
        lbl002: KnockoutObservable<string> = ko.observable('');
        lbl003: KnockoutObservable<string> = ko.observable('');
        lbl005: KnockoutObservable<string> = ko.observable('');
        inp001: KnockoutObservable<number> = ko.observable(0);
        lst001: KnockoutObservable<number> = ko.observable(0);
        lst001Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);
        lst002Data: KnockoutObservableArray<TableRowItem> = ko.observableArray([]);
        dataSources: Array<IPaydayDto> = [];
        enabled: KnockoutObservable<boolean> = ko.observable(true);
        dirty: IDirty = {
            inp001: new nts.uk.ui.DirtyChecker(this.inp001),
            lst001: new nts.uk.ui.DirtyChecker(this.lst001),
            lst002: new nts.uk.ui.DirtyChecker(this.lst002Data),
            isDirty: function() {
                return this.inp001.isDirty() || this.lst002.isDirty();
            },
            reset: function() {
                this.inp001.reset();
                this.lst001.reset();
                this.lst002.reset();
            }
        };
        constructor() {
            let self = this;

            // toggle width of dialog
            self.toggle.subscribe(function(v) {
                if (v) {
                    $('#contents-area').css('width', '');
                    $('#contents-area').css('padding-bottom', '');
                    if (window["large"]) {
                        nts.uk.ui.windows.getSelf().setWidth(1025);
                    } else {
                        $('#contents-area').css('padding-bottom', '10px');
                        nts.uk.ui.windows.getSelf().setWidth(1035);
                    }
                } else {
                    if (window["large"]) {
                        nts.uk.ui.windows.getSelf().setWidth(1465);
                    } else {
                        $('#contents-area').css('width', '1465px');
                        $('#contents-area').css('padding-bottom', '10px');
                        nts.uk.ui.windows.getSelf().setWidth(1300);
                    }
                }
            });
            self.toggle.valueHasMutated();

            // processingNo
            let dataRow = nts.uk.ui.windows.getShared('dataRow');
            self.processingNo(dataRow.index());
            self.lbl002(dataRow.index());
            self.lbl003(dataRow.label());

            self.lst001.subscribe(function(v) {
                let value = v && !!parseInt(v.toString());
                self.enabled(value);

                if (value && v != parseInt(self.dirty.lst001.initialState)) {
                    if (self.dirty.isDirty()) {
                        nts.uk.ui.dialog.confirm("変更された内容が登録されていません。よろしいですか。?").ifYes(function() {
                            let lst001Data = self.lst001Data(),
                                currentItem = _.find(lst001Data, function(item) { return item.index == v; });
                            if (currentItem) {
                                self.inp001(currentItem.value);
                                self.reloadData(currentItem.value);
                            }
                        }).ifNo(function() {
                            self.lst001(parseInt(self.dirty.lst001.initialState));
                        });
                    } else {
                        let lst001Data = self.lst001Data(),
                            currentItem = _.find(lst001Data, function(item) { return item.index == v; });
                        if (currentItem) {
                            self.inp001(currentItem.value);
                            self.reloadData(currentItem.value);
                        }
                    }
                }
            });

            self.inp001.subscribe(function(v) {
                let value = v && !!parseInt(v.toString());
                if (value) {
                    self.lbl005("(" + v["yearInJapanEmpire"]() + ")");
                } else {
                    self.lbl005("");
                }

                if (self.lst001() == null || self.lst001() == undefined || !self.lst001()) {
                    for (let i = 0; i < 12; i++) {
                        let row = self.lst002Data()[i] as TableRowItem;
                        if (row) {
                            row.year(v && parseInt(v.toString()) || new Date().getFullYear());
                            row.year.valueHasMutated();
                        }
                    }
                }
            });
            self.start();
        }

        start() {
            let self = this,
                dfd = $.Deferred(),
                lst001Data: Array<common.SelectItem> = [],
                dataRow = nts.uk.ui.windows.getShared('dataRow');

            services.getData(self.processingNo()).done(function(resp: Array<IPaydayDto>) {
                if (resp && resp.length > 0) {
                    self.dataSources = _.orderBy(resp, ["processingYm"], ["asc"]);
                    for (let i: number = 0, rec: IPaydayDto; rec = self.dataSources[i]; i++) {
                        let year = rec.processingYm["getYearInYm"](),
                            yearIJE = year + "(" + year["yearInJapanEmpire"]() + ")";

                        if (!_.find(lst001Data, function(item) { return item.value == year; })) {
                            lst001Data.push(new common.SelectItem({ index: lst001Data.length + 1, label: yearIJE, value: year, selected: year == dataRow.sel001() }));
                        }
                    }
                    self.lst001Data(lst001Data);

                    // Selected year match with parent row
                    let selectRow = _.find(lst001Data, function(item) { return item.selected; });
                    if (selectRow) {
                        self.lst001(selectRow.index);
                        self.lst001.valueHasMutated();
                        // Load data to table
                        self.reloadData(selectRow.value);
                        dfd.resolve();
                    }
                }
            });
            return dfd.promise();
        }

        reloadData(year) {
            let self = this,
                lst002Data: Array<TableRowItem> = [],
                dataRow = nts.uk.ui.windows.getShared('dataRow');

            // Redefined year value
            year = year || dataRow.sel001();

            let dataSources = self.dataSources.filter(function(item) { return item.processingYm["getYearInYm"]() == year; });

            for (let i: number = 0, rec: IPaydayDto; rec = self.dataSources[i]; i++) {
                let rec: IPaydayDto = dataSources[i];
                if (rec) {
                    let $moment = moment.utc(rec.payDate),
                        month = $moment.month() + 1,
                        sel002Data: Array<common.SelectItem> = [];

                    for (var j: number = 1; j <= $moment.daysInMonth(); j++) {
                        var date = moment.utc([$moment.year(), $moment.month(), j]);
                        sel002Data.push(new common.SelectItem({
                            index: j,
                            label: date.format("YYYY/MM/DD"),
                            value: date.toDate()
                        }));
                    }

                    let row = _.find(lst002Data, function(item) { return item.month() == month; });
                    if (!row) {
                        row = new TableRowItem({
                            month: month,
                            year: year,
                            sel001: rec.payBonusAtr == 1 ? true : false,
                            sel002: $moment.date(),
                            sel002Data: sel002Data,
                            inp003: moment.utc(rec.stdDate).toDate(),
                            inp004: rec.socialInsLevyMon["formatYearMonth"]("/"),
                            inp005: rec.stmtOutputMon["formatYearMonth"]("/"),
                            inp006: moment.utc(rec.socialInsStdDate).toDate(),
                            inp007: moment.utc(rec.empInsStdDate).toDate(),
                            inp008: moment.utc(rec.incomeTaxStdDate).toDate(),
                            inp009: moment.utc(rec.accountingClosing).toDate(),
                            inp010: rec.neededWorkDay
                        });
                        lst002Data.push(row);
                    } else {
                        row.sel001(rec.payBonusAtr == 1 ? true : false);
                    }
                }
            }
            self.lst002Data(lst002Data);
            self.dirty.reset();
        }

        toggleColumns(item, event): void {
            let self = this;
            self.toggle(!self.toggle());
        }

        showModalDialogE(item, event): void {
            let self = this;
            nts.uk.ui.windows.setShared('viewModelB', item);
            nts.uk.ui.windows.sub.modal("../e/index.xhtml", { width: 540, height: 600, title: '処理区分の追加', dialogClass: "no-close" })
                .onClosed(() => {
                    let model = nts.uk.ui.windows.getShared("viewModelE");
                    if (model) {
                        // update data to current processing year month when close dialog
                        for (var i in self.lst002Data()) {
                            if (model.flectionStartMonth <= parseInt(i.toString()) + 1) {
                                let row: TableRowItem = self.lst002Data()[i];
                                if (row) {
                                    if (model.sel003) {
                                        row.sel002(new Date(row.year(), row.month(), 0).getDate());
                                    }

                                    if (model.sel004) {
                                        row.inp003(moment.utc([row.year(), row.month() - 1, 1]).toDate());
                                    }

                                    if (model.sel005) {
                                        row.inp004(moment.utc(new Date(row.year(), row.month() - 1, 0)).format("YYYY/MM"));
                                    }

                                    if (model.sel006) {
                                        row.inp006(moment.utc(new Date(row.year(), row.month() - 1, 0)).toDate());
                                    }

                                    if (model.sel007) {
                                        row.inp007(moment.utc(new Date(row.year(), row.month() - 1, 0).setDate(1)).toDate());
                                    }

                                    if (model.sel008) {
                                        row.inp008(moment.utc([row.year() + 1, row.month() - 1, 1]).toDate());
                                    }

                                    if (model.sel009) {
                                        row.inp009(moment.utc([row.year(), row.month() - 1, 1]).toDate());
                                    }
                                }
                            }
                        }
                    }
                });
        }

        newData(item, event) {
            let self = this;
            if (self.dirty.isDirty()) {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。よろしいですか。?").ifYes(function() {
                    self.lst001(null);
                    self.inp001(null);
                    self.dirty.reset();
                });
            } else {
                self.lst001(null);
                self.inp001(null);
                self.dirty.reset();
            }
        }

        saveData(item, event) {
            let self = this,
                lst002Data = ko.toJS(self.lst002Data()),
                data: Array<IPaydayDto> = [];
            for (var i in lst002Data) {
                let item = lst002Data[i],
                    dataSources = _.clone(self.dataSources),
                    processingYm = parseInt((item.year + '' + item.month)['formatYearMonth']()),
                    filterProcessings = _.filter(dataSources, function(item) { return item.processingYm == processingYm; });

                // update salary info
                let salary = _.find(filterProcessings, function(item) { return item.payBonusAtr == 0; });
                if (salary != undefined) {
                    salary.processingNo = self.processingNo();
                    salary.payBonusAtr = 0;
                    salary.processingYm = parseInt((item.year + '' + item.month)['formatYearMonth']());
                    salary.sparePayAtr = 0;
                    salary.payDate = moment.utc(item.sel002Data[item.sel002 - 1].label).toISOString();
                    salary.stdDate = moment.utc(item.inp003).toISOString();
                    salary.accountingClosing = moment.utc(item.inp009).toISOString();
                    salary.socialInsLevyMon = parseInt(item.inp004["formatYearMonth"]());
                    salary.socialInsStdDate = moment.utc(item.inp006).toISOString();
                    salary.incomeTaxStdDate = moment.utc(item.inp008).toISOString();
                    salary.neededWorkDay = item.inp010;
                    salary.empInsStdDate = moment.utc(item.inp007).toISOString();
                    salary.stmtOutputMon = parseInt(item.inp005["formatYearMonth"]());
                } else {
                    // Add new salary data
                    filterProcessings.push({
                        processingNo: self.processingNo(),
                        payBonusAtr: 0,
                        processingYm: parseInt((self.inp001() + '' + item.month)['formatYearMonth']()),
                        sparePayAtr: 0,
                        payDate: moment.utc(item.sel002Data[item.sel002 - 1].label).toISOString(),
                        stdDate: moment.utc(item.inp003).toISOString(),
                        accountingClosing: moment.utc(item.inp009).toISOString(),
                        socialInsLevyMon: parseInt(item.inp004["formatYearMonth"]()),
                        socialInsStdDate: moment.utc(item.inp006).toISOString(),
                        incomeTaxStdDate: moment.utc(item.inp008).toISOString(),
                        neededWorkDay: item.inp010,
                        empInsStdDate: moment.utc(item.inp007).toISOString(),
                        stmtOutputMon: parseInt(item.inp005["formatYearMonth"]())
                    });
                }

                // If month has bonus
                if (item.sel001 == true) {
                    if (filterProcessings.length == 1) {
                        // Add new bonus data
                        filterProcessings.push({
                            processingNo: self.processingNo(),
                            payBonusAtr: 1,
                            processingYm: parseInt((self.inp001() + '' + item.month)['formatYearMonth']()),
                            sparePayAtr: 0,
                            payDate: moment.utc(item.sel002Data[item.sel002 - 1].label).toISOString(),
                            stdDate: moment.utc(item.inp003).toISOString(),
                            accountingClosing: moment.utc(item.inp009).toISOString(),
                            socialInsLevyMon: parseInt(item.inp004["formatYearMonth"]()),
                            socialInsStdDate: moment.utc(item.inp006).toISOString(),
                            incomeTaxStdDate: moment.utc(item.inp008).toISOString(),
                            neededWorkDay: item.inp010,
                            empInsStdDate: moment.utc(item.inp007).toISOString(),
                            stmtOutputMon: parseInt(item.inp005["formatYearMonth"]())
                        });
                    } else {
                        // update exist bonus infomation
                        let bonus = _.find(filterProcessings, function(item) { return item.payBonusAtr == 1; });
                        if (bonus) {
                            //bonus.processingNo = self.processingNo();
                            //bonus.payBonusAtr = 0;
                            bonus.processingYm = parseInt((self.inp001() + '' + item.month)['formatYearMonth']());
                            //bonus.sparePayAtr = 0;
                            bonus.payDate = moment.utc(bonus.payDate).toISOString();
                            bonus.stdDate = moment.utc(bonus.stdDate).toISOString();
                            bonus.accountingClosing = moment.utc(bonus.accountingClosing).toISOString();
                            //bonus.socialInsLevyMon = parseInt(item.inp004["formatYearMonth"]());
                            bonus.socialInsStdDate = moment.utc(bonus.socialInsStdDate).toISOString();
                            bonus.incomeTaxStdDate = moment.utc(bonus.incomeTaxStdDate).toISOString();
                            bonus.neededWorkDay = item.inp010;
                            bonus.empInsStdDate = moment.utc(bonus.empInsStdDate).toISOString();
                            //bonus.stmtOutputMon = parseInt(item.inp005["formatYearMonth"]());
                        }
                    }
                } else { // remove bonus info
                    filterProcessings = _.filter(filterProcessings, function(item) { return item.payBonusAtr == 0; });
                }
                // push info to model
                data = data.concat(filterProcessings);
            }
            data = _.orderBy(data, ["payBonusAtr", "processingYm"], ["asc", "asc"]);
            services.updatData({ processingNo: self.processingNo(), payDays: data }).done(() => {
                //nts.uk.ui.dialog.info("sucessfull");
                let lst001 = parseInt(self.dirty.lst001.initialState);
                self.dirty.reset();
                if (!lst001) {
                    lst001 = parseInt(self.inp001().toString());
                }
                self.start().then(() => {
                    self.dirty.reset();
                    let selected = _.find(self.lst001Data(), function(ii) { return ii.value == lst001; });
                    if (selected) {
                        self.lst001(selected.index);
                    } else {
                        self.lst001(lst001);
                    }
                    self.dirty.reset();
                });
            });
        }

        deleteData(item, event) {
            let self = this;
            // Note
            /// No document for this action
        }

        closeDialog(item, event) {
            let self = this;
            if (self.dirty.isDirty()) {
                nts.uk.ui.dialog.confirm("変更された内容が登録されていません。よろしいですか。?").ifYes(function() { nts.uk.ui.windows.close(); });
            } else {
                nts.uk.ui.windows.close();
            }
        }
    }

    interface ITableRowItem {
        month: number;
        year: number;
        sel001: boolean;
        sel002: number;
        sel002Data: Array<common.SelectItem>;
        inp003: Date;
        inp004: string;
        inp005: string;
        inp006: Date;
        inp007: Date;
        inp008: Date;
        inp009: Date;
        inp010: number;
    }

    class TableRowItem {
        month: KnockoutObservable<number> = ko.observable(0);
        year: KnockoutObservable<number> = ko.observable(0);
        sel001: KnockoutObservable<boolean> = ko.observable(false);
        sel002: KnockoutObservable<number> = ko.observable(0);
        sel002L: KnockoutObservable<string> = ko.observable('');
        inp003: KnockoutObservable<Date> = ko.observable(null);
        inp004: KnockoutObservable<string> = ko.observable('');
        inp005: KnockoutObservable<string> = ko.observable('');
        inp006: KnockoutObservable<Date> = ko.observable(null);
        inp007: KnockoutObservable<Date> = ko.observable(null);
        inp008: KnockoutObservable<Date> = ko.observable(null);
        inp009: KnockoutObservable<Date> = ko.observable(null);
        inp010: KnockoutObservable<number> = ko.observable(0);

        sel002Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);

        constructor(param: ITableRowItem) {
            let self = this;
            self.month(param.month);
            self.year(param.year);
            self.year.subscribe(function(v) {
                if (v) {
                    self.sel001(false);
                    let sel002Data: Array<common.SelectItem> = [], $moment = moment(new Date(v, self.month() - 1, 1));
                    for (let i = 1; i <= $moment.daysInMonth(); i++) {
                        let date = moment(new Date(v, self.month() - 1, i));
                        sel002Data.push(new common.SelectItem({
                            index: i,
                            label: date.format("YYYY/MM/DD"),
                            value: date.toDate()
                        }));
                    }
                    self.sel002Data(sel002Data);
                    self.sel002.valueHasMutated();
                    self.inp003(moment.utc([v, self.month() - 1, 1]).toDate())
                    self.inp004(moment.utc(new Date(v, self.month() - 1, 0)).format("YYYY/MM"));
                    self.inp005(moment.utc([v, self.month() - 1, 1]).format("YYYY/MM"));
                    self.inp006(moment.utc(new Date(v, self.month() - 1, 0)).toDate());
                    self.inp007(moment.utc([v, self.month() - 1, 1]).toDate());
                    self.inp008(moment.utc([v + 1, 0, 1]).toDate());
                    self.inp009(moment.utc(new Date(v, self.month(), 0)).toDate());
                    self.inp010(moment.utc([v, self.month() - 1, 1]).toDate()["getWorkDays"]());
                }
            });
            self.year.valueHasMutated();

            self.sel001(param.sel001);
            self.sel002(param.sel002);
            self.sel002.subscribe(function(v) {
                if (v) {
                    let currentSel002 = _.find(self.sel002Data(), function(item) { return item.index == v; });
                    if (currentSel002) {
                        self.sel002L(currentSel002.value["getDayJP"]());
                    }
                }
            });
            self.sel002.valueHasMutated();

            self.inp003(param.inp003);
            self.inp004(param.inp004);
            self.inp005(param.inp005);
            self.inp006(param.inp006);
            self.inp007(param.inp007);
            self.inp008(param.inp008);
            self.inp009(param.inp009);
            self.inp010(param.inp010);

            self.sel002Data(param.sel002Data);


        }
    }

    interface IPaydayDto {
        processingNo: number;
        payBonusAtr: number;
        processingYm: number;
        sparePayAtr: number;
        payDate: string;
        stdDate: string;
        accountingClosing: string;
        socialInsLevyMon: number;
        socialInsStdDate: string;
        incomeTaxStdDate: string;
        neededWorkDay: number;
        empInsStdDate: string;
        stmtOutputMon: number;
    }

    interface IDirty {
        inp001: nts.uk.ui.DirtyChecker,
        lst001: nts.uk.ui.DirtyChecker,
        lst002: nts.uk.ui.DirtyChecker,
        isDirty: any,
        reset: any
    }
}