module nts.uk.pr.view.qmm042.a.viewmodel {
    import getText = nts.uk.resource.getText;
    import dialog = nts.uk.ui.dialog;
    import block = nts.uk.ui.block;
    import validation = nts.uk.ui.validation;
    import errors = nts.uk.ui.errors;

    export class ScreenModel {

        priceValidator = new validation.NumberValidator(getText("QMM042_13"), "SalaryUnitPrice", {required: true});
        isRegistrable: KnockoutObservable<boolean> = ko.observable(false);

        ccgcomponent: GroupOption;
        salaryPerUnitPriceNames: KnockoutObservableArray<IndividualPriceName> = ko.observableArray([]);
        salaryPerUnitPriceNamesSelectedCode: KnockoutObservable<string> = ko.observable(null);

        workIndividualPricesDisplay: Array<WorkIndividualPrice> = [];

        perUnitPriceShortName: KnockoutObservable<string> = ko.observable('');
        perUnitPriceCode: KnockoutObservable<string> = ko.observable('');

        yearMonthFilter = ko.observable(201811);

        employeeInfoImports: any;
        listEmployee: any;

        constructor() {
            let self = this;
            self.salaryPerUnitPriceNamesSelectedCode.subscribe(function (selectcode) {
                self.workIndividualPricesDisplay = [];
                errors.clearAll();
                if (selectcode) {
                    let temp = _.find(self.salaryPerUnitPriceNames(), function (o) {
                        return o.code == selectcode;
                    });
                    if (temp) {
                        self.perUnitPriceCode(temp.code);
                        self.perUnitPriceShortName(temp.shortName);
                    }
                    self.filterData();
                }

            });
        }

        filterData(): void {
            let self = this;
            $('#A4_5').ntsError('check');
            if (!self.listEmployee) {
                return;
            }
            block.invisible();
            let command = {
                personalUnitPriceCode: self.salaryPerUnitPriceNamesSelectedCode(),
                employeeIds: self.listEmployee.map(v => v.employeeId),
                yearMonthFilter: self.yearMonthFilter()
            };
            service.employeeSalaryUnitPriceHistory(command).done(function (dataNameAndAmount) {
                self.employeeInfoImports = dataNameAndAmount.employeeInfoImports;
                let personalAmountData = dataNameAndAmount.workIndividualPrices.map(x => new WorkIndividualPrice(x));
                for (let personalAmount of personalAmountData) {
                    let employeeInfo = _.find(self.employeeInfoImports, x => x.sid === personalAmount.employeeID);
                    personalAmount.employeeCode = employeeInfo.scd;
                    personalAmount.businessName = employeeInfo.businessName;
                }
                personalAmountData = _.sortBy(personalAmountData, ['employeeCode']);
                self.workIndividualPricesDisplay = personalAmountData;
                $("#grid").mGrid("destroy");
                self.loadMGrid();
            }).always(() => {
                block.clear();
            })
        }

        loadMGrid() {
            let self = this;
            let height = $(window).height() - 90 - 285;
            let width = $(window).width() + 20 - 1170;
            new nts.uk.ui.mgrid.MGrid($("#grid")[0], {
                width: "560px",
                height: "300px",
                subWidth: width + 'px',
                subHeight: height + 'px',
                headerHeight: '23px',
                dataSource: self.workIndividualPricesDisplay,
                primaryKey: 'historyID',
                primaryKeyDataType: 'string',
                rowVirtualization: true,
                virtualization: true,
                virtualizationMode: 'continuous',
                enter: 'right',
                autoFitWindow: false,
                hidePrimaryKey: true,
                errorsOnPage: false,
                columns: [
                    {headerText: "id", key: 'historyID', dataType: 'string', hidden: true},
                    // A5_10
                    {
                        headerText: getText("QMM042_10"), key: 'employeeCode', dataType: 'string', width: '90px',
                        ntsControl: "Label"
                    },
                    // A5_11
                    {
                        headerText: getText("QMM042_11"), key: 'businessName', dataType: 'string', width: "145px",
                        ntsControl: "Label"
                    },
                    // A5_12
                    {
                        headerText: getText("QMM042_12"), key: 'period', dataType: 'string', width: "175px",
                        ntsControl: "Label"
                    },
                    // A5_13
                    {
                        headerText: getText("QMM042_13"), key: 'amountOfMoney', dataType: 'string', width: "150px",
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            primitiveValue: "SalaryUnitPrice",
                            required: true
                        }
                    }
                ],
                features: [
                    {
                        name: "Sorting",
                        columnSettings: [
                            {columnKey: "employeeCode", allowSorting: true, type: "String"},
                            {columnKey: "businessName", allowSorting: true, type: "String"},
                            {columnKey: "period", allowSorting: true, type: "String"},
                            {columnKey: "amount", allowSorting: true, type: "Number"}
                        ]
                    },
                    {
                        name: 'HeaderStyles',
                        columns: [
                            {key: 'employeeCode', colors: ['left-align']},
                            {key: 'businessName', colors: ['left-align']},
                            {key: 'period', colors: ['left-align']},
                            {key: 'amount', colors: ['left-align']}
                        ]
                    },
                    {
                        name: 'Paging',
                        pageSize: 20,
                        currentPageIndex: 0
                    },
                ]
            }).create();
        }

        isValidForm() {
            return _.isEmpty($("#grid").mGrid("errors", true));
        }

        registerAmount(): void {
            let self = this;
            block.invisible();
            if (errors.hasError() || !self.isValidForm()) {
                block.clear();
                return;
            }
            service.empSalUnitUpdateAll({
                payrollInformationCommands: $("#grid").mGrid("dataSource", true)
            }).done(function () {
                dialog.info({messageId: "Msg_15"});
            }).always(() => {
                block.clear();
            })
        }

        startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            block.invisible();
            service.employeeReferenceDate().done(function (data) {
                self.yearMonthFilter(data.salCurrProcessDate);
                self.reloadCcg001(data.empExtraRefeDate);
                service.salaryPerUnitPriceName().done(function (individualPriceName) {
                    if (individualPriceName.length == 0) {
                        self.isRegistrable(false);
                        dialog.alertError({messageId: "MsgQ_170"});
                    } else {
                        self.isRegistrable(true);
                        self.salaryPerUnitPriceNames(individualPriceName);
                        self.salaryPerUnitPriceNamesSelectedCode(self.salaryPerUnitPriceNames()[0].code);
                    }
                    self.loadMGrid();
                    block.clear();
                    dfd.resolve(self);
                }).fail((err) => {
                    block.clear();
                    dfd.reject();
                });
            }).fail((err) => {
                block.clear();
                dfd.reject();
            });
            return dfd.promise();
        }

        reloadCcg001(paymentDate: string): void {
            let self = this;
            self.ccgcomponent = {
                /** Common properties */
                systemType: 3, // ??????????????????
                showEmployeeSelection: true,
                showQuickSearchTab: true,
                showAdvancedSearchTab: true,
                showBaseDate: true,
                showClosure: false,
                showAllClosure: false,
                showPeriod: false,
                periodFormatYM: false,

                /** Required parameter */
                baseDate: moment(new Date(paymentDate)).format("YYYY-MM-DD"), // ?????????
                periodStartDate: moment(new Date('06/05/1990')).format("YYYY-MM-DD"), // ?????????????????????
                periodEndDate: moment(new Date('06/05/2018')).format("YYYY-MM-DD"), // ?????????????????????
                inService: true,
                leaveOfAbsence: true,
                closed: true,
                retirement: true,

                /** Quick search tab options */
                showAllReferableEmployee: true,
                showOnlyMe: false,
                showSameWorkplace: false,
                showSameWorkplaceAndChild: false,

                /** Advanced search properties */
                showEmployment: true,
                showDepartment: true,
                showWorkplace: false,
                showClassification: false,
                showJobTitle: false,
                showWorktype: false,
                isMutipleCheck: true,
                showOnStart: true,
                tabindex: 2,

                returnDataFromCcg001: function (data: Ccg001ReturnedData) {
                    self.listEmployee = data.listEmployee;
                    self.filterData();
                }
            };
            $('#com-ccg001').ntsGroupComponent(self.ccgcomponent);
        }
    }

    export interface GroupOption {

        /** Common properties */
        showEmployeeSelection?: boolean; // ???????????????
        systemType: number; // ??????????????????
        showQuickSearchTab?: boolean; // ??????????????????
        showAdvancedSearchTab?: boolean; // ????????????
        showBaseDate?: boolean; // ???????????????
        showClosure?: boolean; // ?????????????????????
        showAllClosure?: boolean; // ???????????????
        showPeriod?: boolean; // ??????????????????
        periodFormatYM?: boolean; // ??????????????????
        isInDialog?: boolean;

        /** Required parameter */
        baseDate?: string; // ?????????
        periodStartDate?: string; // ?????????????????????
        periodEndDate?: string; // ?????????????????????
        inService: boolean; // ????????????
        leaveOfAbsence: boolean; // ????????????
        closed: boolean; // ????????????
        retirement: boolean; // ????????????

        /** Quick search tab options */
        showAllReferableEmployee?: boolean; // ??????????????????????????????
        showOnlyMe?: boolean; // ????????????
        showSameWorkplace?: boolean; // ?????????????????????
        showSameWorkplaceAndChild?: boolean; // ????????????????????????????????????

        /** Advanced search properties */
        showEmployment?: boolean; // ????????????
        showDepartment?: boolean; // ????????????
        showWorkplace?: boolean; // ????????????
        showClassification?: boolean; // ????????????
        showJobTitle?: boolean; // ????????????
        showWorktype?: boolean; // ????????????
        isMutipleCheck?: boolean; // ???????????????
        isTab2Lazy?: boolean;
        showOnStart?: boolean;
        tabindex?: number;

        /** Data returned */
        returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
    }

    export interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceId: string;
        workplaceName: string;
    }

    export interface Ccg001ReturnedData {
        baseDate: string; // ?????????
        closureId?: number; // ??????ID
        periodStart: string; // ?????????????????????)
        periodEnd: string; // ????????????????????????
        listEmployee: Array<EmployeeSearchDto>; // ????????????
    }

    export interface IIndividualPriceName {
        code: string,
        name: string,
        abolition: number,
        shortName: string,
        integrationCode: string,
        note: string
    }

    export class IndividualPriceName {
        code: string;
        name: string;
        abolition: number;
        shortName: string;
        integrationCode: string;
        note: string;

        constructor(param: IIndividualPriceName) {
            if (param) {
                this.code = param.code;
                this.name = param.name;
                this.abolition = param.abolition;
                this.shortName = param.shortName;
                this.integrationCode = param.integrationCode;
                this.note = param.note;
            }
        }
    }

    export interface IWorkIndividualPrice {
        employeeID: string,
        historyID: string,
        employeeCode: string,
        businessName: string,
        startYaerMonth: number,
        endYearMonth: number,
        amountOfMoney: number,
    }

    export class WorkIndividualPrice {
        employeeID: string;
        historyID: string;
        employeeCode: string = '';
        businessName: string = '';

        startYaerMonth: number;
        endYearMonth: number;

        period: string;

        amountOfMoney: string = '0';

        constructor(param: IWorkIndividualPrice) {
            if (param) {
                this.employeeID = param.employeeID;
                this.historyID = param.historyID;
                this.employeeCode = param.employeeCode;
                this.businessName = param.businessName;
                this.startYaerMonth = param.startYaerMonth;
                this.endYearMonth = param.endYearMonth;
                this.amountOfMoney = param.amountOfMoney.toString();
                this.period = nts.uk.time.formatYearMonth(param.startYaerMonth) + ' ??? ' + nts.uk.time.formatYearMonth(param.endYearMonth);
            }
        }
    }

    export interface IEmployeeInfoImport {
        sid: string,
        scd: string,
        businessName: string,
    }

    export class EmployeeInfoImport {
        sid: string;
        scd: string;
        businessName: string;

        constructor(param: IEmployeeInfoImport) {
            this.sid = param.sid;
            this.scd = param.scd;
            this.businessName = param.businessName;
        }
    }
}


