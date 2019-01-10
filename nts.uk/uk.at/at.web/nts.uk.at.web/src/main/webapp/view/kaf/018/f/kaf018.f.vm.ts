module nts.uk.at.view.kaf018.f.viewmodel {
    import text = nts.uk.resource.getText;
    import getShared = nts.uk.ui.windows.getShared;
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
        isConfirmData: boolean
        selectedWplIndex: number;
        selectedWplId: KnockoutObservable<string>;
        selectedWplName: KnockoutObservable<string>;
        listEmpCd: Array<string>;
        inputContent: any;

        enableNext: KnockoutObservable<boolean>;
        enablePre: KnockoutObservable<boolean>;

        arrDay: shareModel.Time[] = [];
        dtPrev: KnockoutObservable<Date> = ko.observable(null);
        dtAft: KnockoutObservable<Date> = ko.observable(null);

        dataWkpSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataComSpecificDate: KnockoutObservableArray<any> = ko.observableArray([]);
        dataPublicHoliday: KnockoutObservableArray<any> = ko.observableArray([]);

        // 実績確認済
        colorConfirmed = 'bg-actual-verified';
        // 実績上司未確認
        colorBossUnconfirm = 'bg-actual-superior-unverified';
        // 本人未確認
        colorPersonUnconfirm = 'bg-actual-person-unverified';
        // 実績対象外
        colorExcluded = 'bg-actual-excluded';

        dateFormat = "yyyy/MM/dd";
        closureID: number = null;
        constructor() {
            var self = this;
            this.legendOptions = {
                items: [
                    { cssClass: { className: self.colorConfirmed, colorPropertyName: 'background-color' }, labelText: text("KAF018_89") },
                    { cssClass: { className: self.colorBossUnconfirm, colorPropertyName: 'background-color' }, labelText: text("KAF018_66") },
                    { cssClass: { className: self.colorPersonUnconfirm, colorPropertyName: 'background-color' }, labelText: text("KAF018_87") },
                    { cssClass: { className: self.colorExcluded, colorPropertyName: 'background-color' }, labelText: text("KAF018_88") }
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
                self.currentMonth = params.processingYm.split('/')[1];
                self.startDateFormat = self.formatDate(new Date(params.startDate));
                self.endDateFormat = self.formatDate(new Date(params.endDate));
                self.startDate = params.startDate;
                self.endDate = params.endDate;
                self.listWkp = params.listWkp;
                self.isConfirmData = params.isConfirmData;
                self.selectedWplIndex = params.selectedWplIndex;
                self.listEmpCd = params.listEmployeeCode;
                self.inputContent = params.inputContent;
                self.closureID = params.closureID
            }

            self.dtPrev(new Date(self.startDateFormat));
            self.dtAft(new Date(self.endDateFormat));
            self.setArrDate();
            self.getWkpName();
            service.getUseSetting().done(function(setting) {
                self.useSetting = setting;
                self.initExTable().done(function() {
                    self.focusE5();
                    block.clear();
                });
            }).fail(function() {
                block.clear();
            })

            dfd.resolve();
            return dfd.promise();
        }

        backToE(): void {
            let self = this;
            let params = {
                closureID: self.closureID,
                processingYm: self.processingYm,
                startDate: self.startDate,
                endDate: self.endDate,
                closureName: self.closureName,
                listWorkplace: self.listWkp,
                isConfirmData: self.isConfirmData,
                listEmployeeCode: self.listEmpCd,
                inputContent: self.inputContent
            };
            nts.uk.request.jump('/view/kaf/018/e/index.xhtml', params);
        }

        getWkpName() {
            let self = this;
            self.enablePre(self.selectedWplIndex != 0)
            self.enableNext(self.selectedWplIndex != (self.listWkp.length - 1))

            let wkp = self.listWkp[self.selectedWplIndex];
            self.selectedWplId(wkp.code);
            self.selectedWplName(wkp.name);
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

        focusE5() {
            $("#extable").focus();
        }

        setArrDate() {
            let self = this;
            let currentDay = new Date(self.dtPrev().toString());
            while (currentDay <= self.dtAft()) {
                self.arrDay.push(new shareModel.Time(currentDay));
                currentDay.setDate(currentDay.getDate() + 1);
            }
        };

        formatDate(date: Date) {
            let self = this;
            return nts.uk.time.formatDate(date, self.dateFormat);
        }

        getEmpPerformance(): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred();
            let obj = {
                wkpId: self.selectedWplId(),
                startDate: self.startDate,
                endDate: self.endDate,
                listEmpCd: self.listEmpCd,
                closureID: self.closureID
            };
            service.getEmpPerformance(obj).done(function(data: Array<EmpPerformanceDto>) {
                let lstData = _.sortBy(data, o => o.sname, 'asc');
                let lstEmp = self.confirmDuplicateEmp(lstData);
                dfd.resolve(self.convertToEmpPerformance(lstEmp));
            });
            return dfd.promise();
        }

        /**
         * 「社員一覧」に対象の社員IDについて行が存在するかを確認　※同じ社員で複数期間となる場合、同じ社員IDが存在
         */
        confirmDuplicateEmp(data: Array<EmpPerformanceDto>): Array<EmpPerformancePeriod> {
            let empIdTemp = "";
            let listEmp: Array<EmpPerformancePeriod> = [];
            _.each(data, function(item: EmpPerformanceDto) {
                let empPeriod: EmpPeriod = new EmpPeriod(item.startDate, item.endDate,
                    item.approvalStatus, item.listDailyConfirm, item.listErrorStatus);
                if (empIdTemp == item.sid) {
                    let emp: EmpPerformancePeriod = _.find(listEmp, { sid: item.sid });
                    emp.listEmpPeriod.push(empPeriod);
                } else {
                    empIdTemp = item.sid
                    let listEmpPeriod: Array<EmpPeriod> = [];
                    listEmpPeriod.push(empPeriod);
                    let empDuplicate: EmpPerformancePeriod = new EmpPerformancePeriod(item.sid, item.sname, listEmpPeriod);
                    listEmp.push(empDuplicate);
                }
            });
            return listEmp;
        }

        convertToEmpPerformance(data: Array<EmpPerformancePeriod>): Array<EmpPerformance> {
            let self = this;
            let index = 0;
            let listEmpPerformance = Array<EmpPerformance>();
            _.each(data, function(item) {
                let isMonthConfirm = true, isPersonConfirm = true, isBossConfirm = true;
                let monthConfirm, personConfirm, bossConfirm, dailyPerformance: Array<DailyPerformance> = [];

                _.each(item.listEmpPeriod, function(empPeriod) {
                    if (empPeriod.approvalStatus != ApprovalStatusForEmployee.APPROVED) {
                        isMonthConfirm = false;
                    }
                    if (!(_.filter(empPeriod.listDailyConfirm, { bossConfirm: false, personConfirm: false }).length == 0
                        && _.filter(empPeriod.listDailyConfirm, { personConfirm: true }).length > 1)) {
                        isPersonConfirm = false;
                    }
                    if (!(_.filter(empPeriod.listDailyConfirm, { bossConfirm: false }).length == 0
                        && _.filter(empPeriod.listDailyConfirm, { bossConfirm: true }).length > 0)) {
                        isBossConfirm = false;
                    }

                    // 「社員ID.期間」内での日付部分の背景色が「Color：実績対象」の場合
                    let performancePeriod = -1;
                    if (self.useSetting.usePersonConfirm) {
                        performancePeriod = Performance.PERSON_UNCONFIRM;
                    } else if (!self.useSetting.usePersonConfirm && self.useSetting.useBossConfirm) {
                        performancePeriod = Performance.BOSS_UNCONFIRM;
                    } else if (!self.useSetting.usePersonConfirm && !self.useSetting.useBossConfirm) {
                        performancePeriod = Performance.CONFIRMED;
                    }

                    let startDate = new Date(empPeriod.startDate.toString());
                    let endDate = new Date(empPeriod.endDate.toString());
                    // 日別確認（リスト）＜職場ID、社員ID、対象日、本人確認、上司確認＞のループ                
                    let currentDay = new Date(empPeriod.startDate.toString());
                    while (currentDay <= endDate) {
                        let performance = performancePeriod;
                        let hasError = false;

                        let objDaily = _.find(empPeriod.listDailyConfirm, { targetDate: self.formatDate(currentDay) });
                        if (objDaily != null) {
                            if (self.useSetting.usePersonConfirm && self.useSetting.useBossConfirm) {
                                if (!objDaily.personConfirm && !objDaily.bossConfirm) {
                                    performance = Performance.PERSON_UNCONFIRM;
                                } else if (objDaily.personConfirm && !objDaily.bossConfirm) {
                                    performance = Performance.BOSS_UNCONFIRM;
                                } else if (objDaily.bossConfirm) {
                                    performance = Performance.CONFIRMED;
                                }
                            } else if (self.useSetting.usePersonConfirm && !self.useSetting.useBossConfirm) {
                                if (!objDaily.personConfirm) {
                                    performance = Performance.PERSON_UNCONFIRM;
                                } else if (objDaily.personConfirm) {
                                    performance = Performance.CONFIRMED;
                                }
                            } else if (!self.useSetting.usePersonConfirm && self.useSetting.useBossConfirm) {
                                if (!objDaily.bossConfirm) {
                                    performance = Performance.BOSS_UNCONFIRM;
                                } else if (objDaily.bossConfirm) {
                                    performance = Performance.CONFIRMED;
                                }
                            }

                            // エラー状況(リスト)＜職場ID、社員ID、対象日＞のループ
                            let objError = _.find(empPeriod.listErrorStatus, function(x) { return x == self.formatDate(currentDay); });
                            if (objError != null) {
                                hasError = true;
                            }
                        }
                        dailyPerformance.push(new DailyPerformance(self.formatDate(currentDay), performance, hasError));
                        currentDay.setDate(currentDay.getDate() + 1);
                    }
                })
                if (isMonthConfirm) {
                    monthConfirm = text("KAF018_92");
                }
                else {
                    monthConfirm = "";
                }
                if (isPersonConfirm) {
                    personConfirm = text("KAF018_92");
                }
                else {
                    personConfirm = "";
                }
                if (isBossConfirm) {
                    bossConfirm = text("KAF018_92");
                }
                else {
                    bossConfirm = "";
                }
                listEmpPerformance.push(new EmpPerformance(index.toString(), item.sid, item.sname, monthConfirm, personConfirm, bossConfirm, dailyPerformance));
                index++;
            })
            return self.addExcluedDay(listEmpPerformance);
        }

        /**
         * 「存在しない」場合は、行を追加して、日付部分の背景色を「Color：実績対象外」でセットする
         */
        addExcluedDay(data: Array<EmpPerformance>): Array<EmpPerformance> {
            let self = this;
            _.each(data, function(item) {
                let startDate = new Date(self.dtPrev().toString());
                let endDate = new Date(self.dtAft().toString());
                let currentDay = new Date(self.dtPrev().toString());

                while (currentDay <= endDate) {
                    let targetDay = moment(currentDay).format('YYYY/MM/DD');
                    let daily = _.find(item.dailyReport, { targetDate: targetDay })
                    if (!daily) {
                        daily = new DailyPerformance(targetDay, Performance.EXCLUDED, false);
                        item.dailyReport.push(daily);
                    }
                    currentDay.setDate(currentDay.getDate() + 1);
                }
                item.dailyReport = _.sortBy(item.dailyReport, o => o.targetDate, 'asc');
            })
            return data;
        }

        /**
         * Create exTable
         */
        initExTable(): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred();
            self.getEmpPerformance().done(function(listData: any) {
                let detailContentDeco = self.setColorForCellContentDetail(listData);
                let leftmostDeco = self.setStyleForCellContentHeader(listData);
                self.setColorForCellHeaderDetail().done(function(detailHeaderDeco) {
                    let initExTable = self.setFormatData(leftmostDeco, detailHeaderDeco, detailContentDeco, listData);
                    new nts.uk.ui.exTable.ExTable($("#extable"), {
                        headerHeight: "69px", bodyRowHeight: "23px", bodyHeight: "299px",
                        horizontalSumBodyRowHeight: "0px",
                        areaResize: false,
                        remainSizes: false,
                        bodyHeightMode: "fixed",
                        windowXOccupation: 50,
                        windowYOccupation: 20,
                        showTooltipIfOverflow: true,
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
                let detailContentDeco = self.setColorForCellContentDetail(listData);
                let leftmostDeco = self.setStyleForCellContentHeader(listData);
                self.setColorForCellHeaderDetail().done(function(detailHeaderDeco) {
                    let initExTable = self.setFormatData(leftmostDeco, detailHeaderDeco, detailContentDeco, listData);
                    $("#extable").exTable("updateTable", "leftmost", initExTable.leftmostHeader, initExTable.leftmostContent, true);
                    $("#extable").exTable("updateTable", "detail", initExTable.detailHeader, initExTable.detailContent, true);
                }).always(() => {
                    block.clear();
                })
            }).fail(() => {
                block.clear();
            })
        }

        setFormatData(leftmostDeco, detailHeaderDeco, detailContentDeco, listData) {
            let self = this;
            let leftmostColumns = [];
            let leftmostHeader = {};
            let leftmostContent = {};
            let leftmostEmpNameHeaderWidth = 0;

            let detailHeaderColumns = [];
            let detailHeader = {};
            let detailContentColumns = [];
            let detailContent = {};

            //create leftMost Header and Content            
            leftmostColumns = [
                { headerText: text("KAF018_60"), key: "sName", width: "200px", handler: function(rData, rowIdx, key) { } }
            ];
            leftmostEmpNameHeaderWidth += 352;

            if (self.useSetting.monthlyConfirm) {
                leftmostColumns.push({ headerText: formatText(text("KAF018_61"), self.currentMonth), key: "monthConfirm", width: "44px", headerHandler: function() { } });
                leftmostEmpNameHeaderWidth -= 44;
            }
            if (self.useSetting.usePersonConfirm) {
                leftmostColumns.push({ headerText: text("KAF018_62"), key: "personConfirm", width: "54px" });
                leftmostEmpNameHeaderWidth -= 54;
            }
            if (self.useSetting.useBossConfirm) {
                leftmostColumns.push({ headerText: text("KAF018_63"), key: "bossConfirm", width: "54px" });
                leftmostEmpNameHeaderWidth -= 54;
            }
            leftmostColumns[0].width = leftmostEmpNameHeaderWidth;

            leftmostHeader = {
                columns: leftmostColumns,
                rowHeight: "69px",
                width: "352px"
            };
            leftmostContent = {
                columns: leftmostColumns,
                dataSource: listData,
                primaryKey: "index",
                features: [{
                    name: "BodyCellStyle",
                    decorator: leftmostDeco
                }]
            };

            // create detail Columns and detail Content Columns
            let currentDay = new Date(self.dtPrev().toString());
            while (currentDay <= self.dtAft()) {
                let time = new shareModel.Time(currentDay);
                detailHeaderColumns.push({
                    key: "_" + time.yearMonthDay, 
                    width: "28px", 
                    headerText: time.day,
                    group:[{
                        key: "___" + time.yearMonthDay, 
                        width: "28px", 
                        headerText: time.weekDay
                    }]
                });
                detailContentColumns.push({
                    key: "__" + time.yearMonthDay, width: "28px"
                });
                currentDay.setDate(currentDay.getDate() + 1);
            }

            //create Detail Header
            detailHeader = {
                columns: [{
                    headerText: text("KAF018_64"),
                    group: detailHeaderColumns
                }],
                width: "868px",
                features: [
                    {
                        name: "HeaderRowHeight",
                        rows: { 0: "22px", 1: "23px", 2: "23px" }
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
                highlight: false,
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
            return time.day + "<br/>" + time.weekDay;
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
                    let dateFormat = moment(date.yearMonthDay).format("YYYY/MM/DD");
                    if (_.includes(self.dataWkpSpecificDate(), dateFormat) || _.includes(self.dataComSpecificDate(), dateFormat)) {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-specific-date"));
                        detailHeaderDeco.push(new shareModel.CellColor("___" + ymd, "2", "bg-schedule-specific-date"));
                    } else if (_.includes(self.dataPublicHoliday(), dateFormat)) {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-sunday color-schedule-sunday"));
                        detailHeaderDeco.push(new shareModel.CellColor("___" + ymd, "2", "bg-schedule-sunday color-schedule-sunday"));
                    } else if (date.weekDay === '土') {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-saturday color-schedule-saturday"));
                        detailHeaderDeco.push(new shareModel.CellColor("___" + ymd, "2", "bg-schedule-saturday color-schedule-saturday"));
                    } else if (date.weekDay === '日') {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-schedule-sunday color-schedule-sunday"));
                        detailHeaderDeco.push(new shareModel.CellColor("___" + ymd, "2", "bg-schedule-sunday color-schedule-sunday"));
                    } else {
                        detailHeaderDeco.push(new shareModel.CellColor("_" + ymd, "1", "bg-weekdays color-weekdays"));
                        detailHeaderDeco.push(new shareModel.CellColor("___" + ymd, "1", "bg-weekdays color-weekdays"));
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
        setColorForCellContentDetail(listData: Array<EmpPerformance>) {
            let self = this;
            let detailContentDeco = [];
            for (let i = 0; i < listData.length; i++) {
                let currentDay = new Date(self.dtPrev().toString());
                let j = 0;
                while (currentDay <= self.dtAft()) {
                    let time = new shareModel.Time(currentDay);
                    let key = "__" + time.yearMonthDay;
                    let clazz = "";
                    if (listData[i].dailyReport[j]) {
                        switch (listData[i].dailyReport[j].performance) {
                            case Performance.CONFIRMED:
                                clazz = self.colorConfirmed;
                                break;
                            case Performance.BOSS_UNCONFIRM:
                                clazz = self.colorBossUnconfirm;
                                break;
                            case Performance.PERSON_UNCONFIRM:
                                clazz = self.colorPersonUnconfirm;
                                break;
                            case Performance.EXCLUDED:
                                clazz = self.colorExcluded;
                                break;
                        }
                        detailContentDeco.push(new shareModel.CellColor(key, i.toString(), clazz));
                        if (listData[i].dailyReport[j].hasError) {
                            listData[i][key] = text("KAF018_90");
                        }
                        else {
                            listData[i][key] = "";
                        }
                    }
                    currentDay.setDate(currentDay.getDate() + 1);
                    j++;
                }
            }
            return detailContentDeco;
        }

        /**
         * Set color for cell detail
         * 
         */
        setStyleForCellContentHeader(listData: Array<EmpPerformance>) {
            let self = this;
            let leftmostDeco = [];
            _.each(listData, function(item) {
                leftmostDeco.push(new shareModel.CellColor("sName", item.index, "emp-name" + item.index));
            })
            return leftmostDeco;
        }
        
        goBackA() {
            var self = this;
            let params = {
                inputContent: self.inputContent
            };
            nts.uk.request.jump('/view/kaf/018/a/index.xhtml', params);
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

        constructor(index: String, sId: String, sName: String, monthConfirm: String, personConfirm: String, bossConfirm: String, dailyReport: Array<DailyPerformance>) {
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
        targetDate: String;
        performance: Performance;
        hasError: boolean;
        constructor(targetDate: String, performance: number, hasError: boolean) {
            this.targetDate = targetDate;
            this.performance = performance;
            this.hasError = hasError;
        }
    }

    enum Performance {
        NONE = 0,
        // 実績確認済
        CONFIRMED = 1,
        // 実績上司未確認
        BOSS_UNCONFIRM = 2,
        // 本人未確認
        PERSON_UNCONFIRM = 3,
        // 実績対象外
        EXCLUDED = 4
    }

    class EmpPerformanceDto {
        sid: string;
        sname: string;
        startDate: string;
        endDate: string;
        approvalStatus: ApprovalStatusForEmployee;
        listDailyConfirm: Array<DailyConfirmDto>;
        listErrorStatus: Array<Date>;
    }

    class DailyConfirmDto {
        targetDate: Date;
        personConfirm: boolean;
        bossConfirm: boolean;
    }

    enum ApprovalStatusForEmployee {
        UNAPPROVED = 0,
        DURING_APPROVAL = 1,
        APPROVED = 2
    }

    class EmpPerformancePeriod {
        sid: string;
        sname: string;
        listEmpPeriod: Array<EmpPeriod>;
        constructor(sid: string, sname: string, listEmpPeriod: Array<EmpPeriod>) {
            this.sid = sid;
            this.sname = sname;
            this.listEmpPeriod = listEmpPeriod;
        }
    }

    class EmpPeriod {
        startDate: string;
        endDate: string;
        approvalStatus: ApprovalStatusForEmployee;
        listDailyConfirm: Array<DailyConfirmDto>;
        listErrorStatus: Array<Date>;
        constructor(startDate: string, endDate: string, approvalStatus: ApprovalStatusForEmployee,
            listDailyConfirm: Array<DailyConfirmDto>, listErrorStatus: Array<Date>) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.approvalStatus = approvalStatus;
            this.listDailyConfirm = listDailyConfirm;
            this.listErrorStatus = listErrorStatus;
        }
    }
}