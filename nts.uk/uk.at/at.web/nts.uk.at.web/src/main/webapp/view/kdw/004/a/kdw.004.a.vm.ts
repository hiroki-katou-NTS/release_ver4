module nts.uk.at.view.kdw004.a.viewmodel {
    import text = nts.uk.resource.getText;

    enum ApprovalStatus {
        Approved = 0,
        UnApproved = 1,
        CannotApproved = 2,
        Disable = 3,
        Notthing = 4
    }

    let SATURDAY_BACKGROUND_COLOR = "#8BD8FF",
        SUNDAY_BACKGROUND_COLOR = "#FABF8F",
        SATURDAY_TEXT_COLOR = "#0031FF",
        SUNDAY_TEXT_COLOR = "#FF0000",
        TEMPLATE_EMPLOYEE_NAME_HEADER = `<a class='limited-label' href='javascript:void(0)' style='width: 200px; text-decoration: underline; cursor: pointer;' data-bind="click: clickNameJumpToKdw003.bind($data, '\${employeeId}')">\${employeeName}</a>`,
        getTemplateDisplayStt = (headerTxtId, startDate) => `{{if \${${headerTxtId}} == '${ApprovalStatus.Notthing}' }}
            <a class='notthing' href='javascript:void(0)' data-bind="click: clickStatusJumpToKdw003.bind($data, '\${employeeId}', '${startDate}')" >本</a>
            {{elseif \${${headerTxtId}} == '${ApprovalStatus.Approved}' }}
            <a class='approved' href='javascript:void(0)' data-bind="click: clickStatusJumpToKdw003.bind($data, '\${employeeId}', '${startDate}')">済</a>
            {{elseif \${${headerTxtId}} == '${ApprovalStatus.UnApproved}' }}
            <a class='unapproved' href='javascript:void(0)' data-bind="click: clickStatusJumpToKdw003.bind($data, '\${employeeId}', '${startDate}')">未</a>
            {{elseif \${${headerTxtId}} == '${ApprovalStatus.CannotApproved}' }}
            <a class='cannotApproved' href='javascript:void(0);'>他</a>
            {{elseif \${${headerTxtId}} != '${ApprovalStatus.Approved}' && \${${headerTxtId}} != '${ApprovalStatus.UnApproved}' && \${${headerTxtId}} != '${ApprovalStatus.CannotApproved}'  }}
            <span class='disable'></span>
            {{/if}}`;

    export class ScreenModelKDW004A { 
        legendOptions: any = {
            items: [
                { className: 'approved-img',legendCellChar: "済", labelText: text('KDW004_8') },
                { className: 'cannotApproved-img',legendCellChar: "他", labelText: text('KDW004_9') },
                { className: 'unapproved-img',legendCellChar: "未", labelText: text('KDW004_10') },
                { className: 'disable-img',legendCellChar: "", labelText: text('KDW004_15') },
                { className: 'nodata-img', legendCellChar: "本",labelText: text('KDW004_16') }
            ],
            template: '<div class="#{className}">#{legendCellChar}</div><div class="legend-item-label">#{labelText}</div>'
            //<div class="legend-cell #{legendCellClass}">#{legendCellChar}</div><div class="legend-explain">#{legendExplain}</div>
        };

        lstData: Array<any> = [];
        lstColumns: Array<any> = [];
        lstHeaderColor: Array<any> = [];

        datePeriod: KnockoutObservable<any> = ko.observable(null);
        currentPageSize: KnockoutObservable<any> = ko.observable(12);
        selectedClosure: KnockoutObservable<any> = ko.observable(null);
        lstClosure: KnockoutObservableArray<any> = ko.observableArray([]);

        constructor() {
            let self = this;

            self.selectedClosure.subscribe(val => {
                if (val) {
                    let currentYearMonth,
                        closures = self.lstClosure();

                    _.each(closures, x => {
                        if (x.closureId == val) {
                            currentYearMonth = x.currentYearMonth;
                        }
                    });

                    service.getDateRange(val, currentYearMonth).done((dateRange) => {
                        self.datePeriod({
                            startDate: moment(dateRange.startDate).format("YYYY/MM/DD"),
                            endDate: moment(dateRange.endDate).format("YYYY/MM/DD")
                        });

                        //disable tabIndex button date range
                        $(".ntsDateRangeButton").prop('tabindex', -1);
                    });
                }
            });
        }

        startPage = (): JQueryPromise<any> => {
            let self = this,
                dfd = $.Deferred();

            nts.uk.ui.block.grayout();

            service.startscreen().done((result: OneMonthApprovalStatus) => {
                self.lstClosure(result.lstClosure);

                self.selectedClosure(result.lstClosure[0].closureId);

                self.datePeriod({
                    startDate: moment(result.startDate).format("YYYY/MM/DD"),
                    endDate: moment(result.endDate).format("YYYY/MM/DD")
                });
                if(result.lstEmployee != null){
                    self.lstData = self.convertToGridData(result.lstEmployee);
                } else {
                    nts.uk.ui.dialog.alert({ messageId: result.messageID  });
                }
                self.generateColumns();
                self.loadGrid();
                self.addClickEventDateHeader();

                dfd.resolve();

                nts.uk.ui.block.clear();
            }).fail((error) => {
                nts.uk.ui.block.clear();
                dfd.reject(error.messageId);
            });
            return dfd.promise();
        }

        extract = () => {
            let self = this,
                param = {
                    closureIdParam: self.selectedClosure(),
                    startDateParam: moment(self.datePeriod().startDate).utc().toISOString(),
                    endDateParam: moment(self.datePeriod().endDate).utc().toISOString(),
                };

            nts.uk.ui.block.grayout();

            service.extractApprovalStatusData(param).done((result: OneMonthApprovalStatus) => {
                let approvalSttGrid = document.getElementById('approvalSttGrid');

                ko.cleanNode(approvalSttGrid_headers);
                ko.cleanNode(approvalSttGrid);
    
                //self.lstData = self.convertToGridData(result.lstEmployee);
                if(result.lstEmployee != null){
                    self.lstData = self.convertToGridData(result.lstEmployee);
                } else {
                    self.lstData = [];
                    nts.uk.ui.dialog.alert({ messageId: result.messageID  });
                }
                self.generateColumns();
                self.loadGrid();
                self.setHeadersColor();
                self.addClickEventDateHeader();

                ko.applyBindings(self, approvalSttGrid);
                ko.applyBindings(self, approvalSttGrid_headers);

                nts.uk.ui.block.clear();
            }).fail((error) => {
                nts.uk.ui.block.clear();
                nts.uk.ui.dialog.alert({ messageId: error.messageId });
            });
        }

        clickDateJumpToKdw003 = (date) => {
            let self = this,
                initParam: DPCorrectionInitParam = {
                    //画面モード
                    screenMode: DPCorrectionScreenMode.APPROVAL,
                    //社員一覧
                    lstEmployee: _.map(self.lstData, data => data.employeeId),
                    //エラー参照を起動する
                    errorRefStartAtr: false,
                    // fix bug 101435
                    //期間を変更する
                    changePeriodAtr: true,
                    //処理締め
                    targetClosure: self.selectedClosure(),
                    //Optional
                    //打刻初期値
                    initClock: null,
                    //遷移先の画面
                    transitionDesScreen: '/view/kdw/004/a/index.xhtml'
                },
                extractionParam: DPCorrectionExtractionParam = {
                    //表示形式
                    displayFormat: DPCorrectionDisplayFormat.DATE,
                    //期間
                    startDate: date,
                    endDate: date,
                    //抽出した社員一覧
                    lstExtractedEmployee: _.map(self.lstData, data => data.employeeId),
                    //Optional
                    //日付別で起動
                    dateTarget: date,
                    individualTarget: undefined
                };

            nts.uk.request.jump("at", "/view/kdw/003/a/index.xhtml", {
                initParam: initParam,
                extractionParam: extractionParam
            });
        }

        clickStatusJumpToKdw003 = (employeeId, startDate) => {
            let self = this,
                initParam: DPCorrectionInitParam = {
                    screenMode: DPCorrectionScreenMode.APPROVAL,
                    lstEmployee: [employeeId],
                    errorRefStartAtr: false,
                    // fix bug 101435
                    //期間を変更する
                    changePeriodAtr: true,
                    targetClosure: self.selectedClosure(),
                    initClock: undefined,
                    transitionDesScreen: '/view/kdw/004/a/index.xhtml'
                },
                extractionParam: DPCorrectionExtractionParam = {
                    displayFormat: DPCorrectionDisplayFormat.INDIVIDUAl,
                    startDate: startDate,
                    endDate: startDate,
                    lstExtractedEmployee: [employeeId],
                    dateTarget: startDate,
                    individualTarget: employeeId
                };

            nts.uk.request.jump("at", "/view/kdw/003/a/index.xhtml", {
                initParam: initParam,
                extractionParam: extractionParam
            });
        }

        clickNameJumpToKdw003 = (employeeId) => {
            let self = this,
                initParam: DPCorrectionInitParam = {
                    //画面モード
                    screenMode: DPCorrectionScreenMode.APPROVAL,
                    //社員一覧
                    //fix bug 
                    //lstEmployee: _.map(self.lstData, data => data.employeeId),
                    lstEmployee:[employeeId],
                    //エラー参照を起動する
                    errorRefStartAtr: false,
                    // fix bug 101435
                    //期間を変更する
                    changePeriodAtr: true,
                    //処理締め
                    targetClosure: self.selectedClosure(),
                    //Optional
                    //打刻初期値
                    initClock: null,
                    //遷移先の画面
                    transitionDesScreen: '/view/kdw/004/a/index.xhtml'
                }, extractionParam: DPCorrectionExtractionParam = {
                    //表示形式
                    displayFormat: DPCorrectionDisplayFormat.INDIVIDUAl,
                    //期間
                    startDate: self.datePeriod().startDate,
                    endDate: self.datePeriod().endDate,
                    //抽出した社員一覧
                    lstExtractedEmployee: employeeId,
                    //初期表示年月日
                    dateTarget: null,
                    //初期表示社員
                    individualTarget: employeeId
                };

            nts.uk.request.jump("at", "/view/kdw/003/a/index.xhtml", {
                initParam: initParam,
                extractionParam: extractionParam
            });
        }

        convertToGridData = (lstEmployee: Array<ApprovalEmployee>): Array<any> => {
            return _.map(lstEmployee, m => {
                _.each(m.lstStatus, s => {
                    m[moment(s.date).format('YYYYMMDD')] = s.status;
                });

                return m;
            });
        }

        loadGrid = (index?: any, pageIndex?: any) => {
            let self = this;

            $("#approvalSttGrid").igGrid({
                primaryKey: "employeeCode",
                dataSource: self.lstData,
                width: 1245,
                height: 426,
                autofitLastColumn: false,
                autoGenerateColumns: false,
                alternateRowStyles: false,
                dataSourceType: "json",
                autoCommit: true,
                rowVirtualization: false,
                virtualizationMode: "fixed",
                columns: self.lstColumns,
                features: [{
                    name: 'MultiColumnHeaders'
                }, {
                        name: 'Paging',
                        'type': "local",
                        currentPageIndex: pageIndex || 0,
                        pageSize: index || 12,
                        pageSizeChanging: (ui, args) => {
                            let approvalSttGrid = document.getElementById('approvalSttGrid'),
                                approvalSttGrid_headers = document.getElementById('approvalSttGrid_headers');

                            ko.cleanNode(approvalSttGrid);

                            self.currentPageSize(args.newPageSize);
                            self.loadGrid(args.newPageSize);

                            self.setHeadersColor();
                            self.addClickEventDateHeader();

                            ko.applyBindings(self, approvalSttGrid);
                            ko.applyBindings(self, approvalSttGrid_headers);
                        },
                        pageIndexChanging: (ui, args) => {
                            let approvalSttGrid = document.getElementById('approvalSttGrid'),
                                approvalSttGrid_headers = document.getElementById('approvalSttGrid_headers');

                            ko.cleanNode(approvalSttGrid);

                            self.loadGrid(self.currentPageSize(), args.newPageIndex);
                            self.setHeadersColor();
                            self.addClickEventDateHeader();

                            ko.applyBindings(self, approvalSttGrid);
                            ko.applyBindings(self, approvalSttGrid_headers);
                        }
                    }
                ]
            });
        }

        generateColumns = () => {
            let self = this,
                period = self.datePeriod(),
                index = moment(period.startDate);

            self.lstColumns = [{
                key: "employeeId",
                width: "115px",
                headerText: text('KDW004_7'),
                dataType: "string",
                hidden: true
            },
                {
                    key: "employeeCode",
                    width: "115px",
                    headerText: text('KDW004_13'),
                    dataType: "string"
                },
                {
                    key: "employeeName",
                    width: "200px",
                    headerText: text('KDW004_14'),
                    dataType: "string",
                    template: TEMPLATE_EMPLOYEE_NAME_HEADER
                }
            ];

            self.lstHeaderColor = [];

            while (!index.isAfter(period.endDate)) {
                let dayName = index.format("dd"),
                    headerNoId = index.format("YYYYMMDD" + 'i'),
                    headerTxtId = index.format("YYYYMMDD");

                // if Saturday
                if (index.format("d") == "6") {
                    self.lstHeaderColor.push({
                        id: headerNoId,
                        background_color: SATURDAY_BACKGROUND_COLOR,
                        text_color: SATURDAY_TEXT_COLOR
                    });

                    self.lstHeaderColor.push({
                        id: headerTxtId,
                        background_color: SATURDAY_BACKGROUND_COLOR,
                        text_color: SATURDAY_TEXT_COLOR
                    });
                    // if Sunday
                } else if (index.format("d") == "0") {
                    self.lstHeaderColor.push({
                        id: headerNoId,
                        background_color: SUNDAY_BACKGROUND_COLOR,
                        text_color: SUNDAY_TEXT_COLOR
                    });

                    self.lstHeaderColor.push({
                        id: headerTxtId,
                        background_color: SUNDAY_BACKGROUND_COLOR,
                        text_color: SUNDAY_TEXT_COLOR
                    });
                }

                self.lstColumns.push({
                    key: headerNoId,
                    width: "30px",
                    headerText: index.format("D"),
                    dataType: "string",
                    group: [
                        {
                            key: headerTxtId,
                            width: "30px",
                            headerText: dayName,
                            dataType: "string",
                            template: getTemplateDisplayStt(headerTxtId, index.format("YYYY/MM/DD"))
                        }
                    ]
                });

                index = index.add(1, "d");
            }
        }

        setHeadersColor = () => {
            let self = this;

            _.each(self.lstHeaderColor, color => {
                $(`#approvalSttGrid_${color.id}`)
                    .css('color', color.text_color)
                    .css('background-color', color.background_color);
            });
        }

        addClickEventDateHeader = () => {
            let self = this,
                approvalSttGrid = document.getElementById('approvalSttGrid'),
                approvalSttGrid_headers = document.getElementById('approvalSttGrid_headers');

            ko.cleanNode(approvalSttGrid_headers);

            let index = moment(self.datePeriod().startDate);

            while (!index.isAfter(self.datePeriod().endDate)) {
                $(`#approvalSttGrid_${moment(index).format("YYYYMMDD")}i`)
                    .css('cursor', 'pointer')
                    .css('text-decoration', 'underline')
                    .attr('data-bind', `click: clickDateJumpToKdw003.bind($data, '${moment(index).format("YYYY/MM/DD")}')`);

                index = index.add(1, "d");
            }
        }
    }

    export class OneMonthApprovalStatus {
        lstClosure: Array<Closure>;
        startDate: string;
        endDate: string;
        lstEmployee: Array<ApprovalEmployee>;
    }

    export class Closure {
        closureId: number;
        closureName: string;
        currentYearMonth: number;
    }

    export class ApprovalEmployee {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        lstStatus: Array<DateApprovalStatus>;
    }

    export class DateApprovalStatus {
        date: string;
        status: number;
    }

    export interface DPCorrectionInitParam {
        //画面モード
        screenMode: DPCorrectionScreenMode;
        //社員一覧
        lstEmployee: any;
        //エラー参照を起動する
        errorRefStartAtr: boolean;
        //期間を変更する
        changePeriodAtr: boolean;
        //処理締め
        targetClosue: number;
        //Optional
        //打刻初期値
        initClock: any;
        //遷移先の画面
        transitionDesScreen: any;
    }

    export interface DPCorrectionExtractionParam {
        //表示形式
        displayFormat: DPCorrectionDisplayFormat;
        //期間
        startDate: string;
        endDate: string;
        //抽出した社員一覧
        lstExtractedEmployee: any;
        //Optional
        //日付別で起動
        dateTarget: any;
        individualTarget: any;
    }

    export enum DPCorrectionScreenMode {
        //通常
        NORMAL = 0,
        //承認
        APPROVAL = 1
    }

    export enum DPCorrectionDisplayFormat {
        //個人別
        INDIVIDUAl = 0,
        //日付別
        DATE = 1,
        //エラー・アラーム
        ErrorAlarm = 2
    }
}