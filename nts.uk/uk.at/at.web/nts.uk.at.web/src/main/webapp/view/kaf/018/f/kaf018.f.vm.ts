module nts.uk.at.view.kaf018.f.viewmodel {
    import text = nts.uk.resource.getText;
    import getShared = nts.uk.ui.windows.getShared;
    import formatDate = nts.uk.time.formatDate;
    import formatText = nts.uk.text.format;
    import shareModel = kaf018.share.model;
    import block = nts.uk.ui.block;

    export class ScreenModel {
        legendOptions: any;

        useSetting: shareModel.UseSetting;

        closureId: string;
        closureName: string;
        processingYm: string;
        currentMonth: string;
        startDateFormat: string;
        endDateFormat: string;
        startDate: Date;
        endDate: Date;
        listWkp: any;
        selectedWplIndex: number;
        selectedWplId: KnockoutObservable<string>;
        selectedWplName: KnockoutObservable<string>;
        listEmpCd: Array<string>;

        enableNext: KnockoutObservable<boolean>;
        enablePre: KnockoutObservable<boolean>;

        arrDay: shareModel.Time[] = [];
        dtPrev: KnockoutObservable<Date> = ko.observable(null);
        dtAft: KnockoutObservable<Date> = ko.observable(null);

        dataWkpSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataComSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataPublicHoliday: KnockoutObservableArray<any> = ko.observableArray([]);

        constructor() {
            var self = this;
            this.legendOptions = {
                items: [
                    { cssClass: { className: 'bg-canceled-application', colorPropertyName: 'background-color' }, labelText: text("KAF018_87") },
                    { cssClass: { className: 'bg-updating-cell', colorPropertyName: 'background-color' }, labelText: text("KAF018_66") },
                    { cssClass: { className: 'bg-unapproved-application', colorPropertyName: 'background-color' }, labelText: text("KAF018_89") },
                    { cssClass: { className: 'bg-weekdays', colorPropertyName: 'background-color' }, labelText: text("KAF018_88") }
                ]
            };
            self.listWkp = [];
            self.selectedWplId = ko.observable('');
            self.selectedWplName = ko.observable('');
            self.enableNext = ko.observable(false);
            self.enablePre = ko.observable(false);
        }

        /**
         * 起動する
         */
        startPage(): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred();
            block.invisible();
            let params = getShared("KAF018F_PARAMS");
            if (params) {
                self.closureId = params.closureId;
                self.closureName = params.closureName;
                self.processingYm = params.processingYm;
                self.currentMonth = formatDate(new Date(params.processingYm), 'MM')
                self.startDateFormat = formatDate(new Date(params.startDate), 'yyyy/MM/dd');
                self.endDateFormat = formatDate(new Date(params.endDate), 'yyyy/MM/dd');
                self.startDate = params.startDate;
                self.endDate = params.endDate;
                self.listWkp = params.listWkp;
                self.selectedWplIndex = params.selectedWplIndex;
                self.listEmpCd = params.listEmployeeCode;
            }
            self.dtPrev(new Date(self.startDateFormat));
            self.dtAft(new Date(self.endDateFormat));
            self.setArrDate();
            self.getWkpName();
            service.getUseSetting().done(function(setting) {
                self.useSetting = setting;
                self.initExTable().done(function() {
                    block.clear();
                });
            }).fail(function() {
                block.clear();
            })

            dfd.resolve();
            return dfd.promise();
        }

        getWkpName() {
            let self = this;
            self.enablePre(self.selectedWplIndex != 0)
            self.enableNext(self.selectedWplIndex != (self.listWkp.length - 1))

            let wkp = self.listWkp[self.selectedWplIndex];
            self.selectedWplId(wkp.wkpId);
            self.selectedWplName(wkp.wkpName);
        }

        nextWkp() {
            let self = this;
            self.selectedWplIndex++;
            self.getWkpName();
            self.updateExTable();
        }

        preWkp() {
            let self = this;
            self.selectedWplIndex--;
            self.getWkpName();
            self.updateExTable();
        }

        setArrDate() {
            let self = this;
            let currentDay = new Date(self.dtPrev().toString());
            while (currentDay <= self.dtAft()) {
                self.arrDay.push(new shareModel.Time(currentDay));
                currentDay.setDate(currentDay.getDate() + 1);
            }
        };

        getEmpPerformance(): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred();
            let obj = {
                wkpId: self.selectedWplId(),
                startDate: self.startDate,
                endDate: self.endDate,
                listEmpCd: self.listEmpCd
            };
            service.getEmpPerformance(obj).done(function(data: any) {
                let list = self.convertToEmpPerformance(data);
                dfd.resolve(list);
            });
            return dfd.promise();
        }

        convertToEmpPerformance(data): Array<EmpPerformance> {
            let self = this;
            let index = 0;
            let listEmpPerformance = Array<EmpPerformance>();
            _.each(data, function(item) {
                let monthConfirm, personConfirm, bossConfirm;

                if (item.monthConfirm) {
                    monthConfirm = text("KAF018_92");
                } else {
                    monthConfirm = "";
                }

                if (item.personConfirm) {
                    personConfirm = text("KAF018_92");
                } else {
                    personConfirm = "";
                }

                if (item.bossConfirm) {
                    bossConfirm = text("KAF018_92");
                } else {
                    bossConfirm = "";
                }
                item.dailyPerformance = _.sortBy(item.dailyPerformance, [function(o) { return o.targetDate; }]);
                listEmpPerformance.push(new EmpPerformance(index.toString(), item.sid, item.sname, monthConfirm, personConfirm, bossConfirm, item.dailyPerformance));
                index++;
            })
            return listEmpPerformance;
        }

        /**
         * Create exTable
         */
        initExTable(): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred();
            self.getEmpPerformance().done(function(listData: any) {
                let sv1 = self.setColorForCellHeaderDetail();
                let sv2 = self.setColorForCellContentDetail(listData);
                $.when(sv1, sv2).done(function(detailHeaderDeco, detailContentDeco) {
                    let initExTable = self.setFormatData(detailHeaderDeco, detailContentDeco, listData);
                    new nts.uk.ui.exTable.ExTable($("#extable"), {
                        headerHeight: "50px", bodyRowHeight: "18px", bodyHeight: "324px",
                        horizontalSumBodyRowHeight: "0px",
                        areaResize: true,
                        bodyHeightMode: "fixed",
                        windowXOccupation: 50,
                        windowYOccupation: 20,
                        primaryTable: $("#extable")
                    })
                        .LeftmostHeader(initExTable.leftmostHeader).LeftmostContent(initExTable.leftmostContent)
                        .DetailHeader(initExTable.detailHeader).DetailContent(initExTable.detailContent)
                        .create();

                    dfd.resolve();
                })
            });
            return dfd.promise();
        }

        /**
         * Update exTable
         */
        updateExTable() {
            let self = this;
            block.invisible();
            self.getEmpPerformance().done(function(listData: any) {
                let sv1 = self.setColorForCellHeaderDetail();
                let sv2 = self.setColorForCellContentDetail(listData);
                $.when(sv1, sv2).done(function(detailHeaderDeco, detailContentDeco) {
                    let initExTable = self.setFormatData(detailHeaderDeco, detailContentDeco, listData);
                    $("#extable").exTable("updateTable", "leftmost", initExTable.leftmostHeader, initExTable.leftmostContent, true);
                    $("#extable").exTable("updateTable", "detail", initExTable.detailHeader, initExTable.detailContent, true);
                }).always(() => {
                    block.clear();
                })
            }).fail(() => {
                block.clear();
            })
        }

        setFormatData(detailHeaderDeco, detailContentDeco, listData) {
            let self = this;
            let leftmostColumns = [];
            let leftmostHeader = {};
            let leftmostContent = {};

            let detailHeaderColumns = [];
            let detailHeader = {};
            let detailContentColumns = [];
            let detailContent = {};

            //create leftMost Header and Content
            leftmostColumns = [
                { headerText: text("KAF018_60"), key: "sName", width: "150px", control: "link" }
            ];

            if (self.useSetting.monthlyConfirm) {
                leftmostColumns.push({ headerText: formatText(text("KAF018_61"), self.currentMonth), key: "monthConfirm", width: "50px" });
            }
            if (self.useSetting.usePersonConfirm) {
                leftmostColumns.push({ headerText: text("KAF018_62"), key: "personConfirm", width: "50px" });
            }
            if (self.useSetting.useBossConfirm) {
                leftmostColumns.push({ headerText: text("KAF018_63"), key: "bossConfirm", width: "50px" });
            }

            leftmostHeader = {
                columns: leftmostColumns,
                rowHeight: "50px",
                width: "300px"
            };
            leftmostContent = {
                columns: leftmostColumns,
                dataSource: listData,
                primaryKey: "index"
            };

            // create detail Columns and detail Content Columns
            let currentDay = new Date(self.dtPrev().toString());
            while (currentDay <= self.dtAft()) {
                let time = new shareModel.Time(currentDay);
                detailHeaderColumns.push({
                    key: "_" + time.yearMonthDay, width: "40px", headerText: self.getDay(time)
                });
                detailContentColumns.push({
                    key: "__" + time.yearMonthDay, width: "40px"
                });
                currentDay.setDate(currentDay.getDate() + 1);
            }

            //create Detail Header
            detailHeader = {
                columns: [{
                    headerText: text("KAF018_64"),
                    group: detailHeaderColumns
                }],
                width: "900px",
                features: [
                    {
                        name: "HeaderRowHeight",
                        rows: { 0: "20px", 1: "30px" }
                    },
                    {
                        name: "HeaderCellStyle",
                        decorator: detailHeaderDeco
                    }]
            };

            //create Detail Content
            detailContent = {
                columns: detailContentColumns,
                dataSource: listData,
                primaryKey: "index",
                features: [{
                    name: "BodyCellStyle",
                    decorator: detailContentDeco
                }]
            };
            return {
                leftmostHeader: leftmostHeader,
                leftmostContent: leftmostContent,
                detailHeader: detailHeader,
                detailContent: detailContent
            };
        }

        getDay(time: shareModel.Time): string {
            if (time.day == "1") {
                return time.month + '/' + time.day + "<br/>" + time.weekDay;
            } else {
                return time.day + "<br/>" + time.weekDay;
            }
        }

        /**
         * Set color for cell header: 日付セル背景色文字色制御
         * 
         */
        setColorForCellHeaderDetail(): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            let detailHeaderDeco = [];
            // getDataSpecDateAndHoliday always query to server
            // because date is changed when click nextMonth or backMonth
            $.when(self.getDataSpecDateAndHoliday()).done(() => {
                _.each(self.arrDay, (date) => {
                    let ymd = date.yearMonthDay;
                    let dateFormat = moment(date.yearMonthDay).format('YYYY/MM/DD');
                    if (_.includes(self.dataWkpSpecificDate(), dateFormat) || _.includes(self.dataComSpecificDate(), dateFormat)) {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-specific-date"));
                    } else if (_.includes(self.dataPublicHoliday(), dateFormat)) {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-sunday color-schedule-sunday"));
                    } else if (date.weekDay === '土') {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-saturday color-schedule-saturday"));
                    } else if (date.weekDay === '日') {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-sunday color-schedule-sunday"));
                    } else {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-weekdays color-weekdays"));
                    }
                });
                dfd.resolve(detailHeaderDeco);
            });
            return dfd.promise();
        }

        /**
         * Get data WkpSpecificDate, ComSpecificDate, PublicHoliday
         */
        getDataSpecDateAndHoliday(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                obj = {
                    workplaceId: self.selectedWplId,
                    startDate: self.startDate,
                    endDate: self.endDate
                };
            service.getDataSpecDateAndHoliday(obj).done(function(data) {
                self.dataWkpSpecificDate(data.listWkpSpecificDate);
                self.dataComSpecificDate(data.listComSpecificDate);
                self.dataPublicHoliday(data.listPublicHoliday);
                dfd.resolve();
            }).fail(function() {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * Set color for cell detail
         * 
         */
        setColorForCellContentDetail(listData): JQueryPromise<any> {
            let self = this, dfd = $.Deferred();
            let detailContentDeco = [];
            for (let i = 0; i < listData.length; i++) {
                let currentDay = new Date(self.dtPrev().toString());
                let j = 0;
                while (currentDay <= self.dtAft()) {
                    let time = new shareModel.Time(currentDay);
                    let key = "__" + time.yearMonthDay;
                    let clazz = "";
                    switch (listData[i].dailyReport[j].performance) {
                        case 0:
                            clazz = "bg-canceled-application";
                            break;
                        case 1:
                            clazz = "bg-updating-cell";
                            break;
                        case 2:
                            clazz = "bg-unapproved-application";
                            break;
                        case 3:
                            clazz = "bg-weekdays";
                            break;
                    }
                    detailContentDeco.push(new shareModel.CellColor(key, i.toString(), clazz));
                    if (listData[i].dailyReport[j].hasError) {
                        listData[i][key] = "ER";
                    }
                    else {
                        listData[i][key] = "";
                    }

                    currentDay.setDate(currentDay.getDate() + 1);
                    j++;
                }
            }
            dfd.resolve(detailContentDeco);
            return dfd.promise();
        }
    }

    class EmpPerformance {
        index: String;
        sId: String;
        sName: String;
        monthConfirm: String;
        personConfirm: String;
        bossConfirm: String;
        dailyReport: Array<DailyPerformance>;

        constructor(index: String, sId: String, sName: String, monthConfirm: String, personConfirm: String, bossConfirm: String, dailyReport: Array<number>) {
            this.index = index;
            this.sId = sId;
            this.sName = sName;
            this.monthConfirm = monthConfirm;
            this.personConfirm = personConfirm;
            this.bossConfirm = bossConfirm;
            this.dailyReport = dailyReport;
        }
    }

    class DailyPerformance {
        targetDate: Date;
        performance: number;
        hasError: boolean;
        constructor(targetDate: Date, performance: number, hasError: boolean) {
            this.targetDate = targetDate;
            this.performance = performance;
            this.hasError = hasError;
        }
    }
}