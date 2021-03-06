module nts.uk.pr.view.qmm025.a.viewmodel {
    import time = nts.uk.time;
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import validation = nts.uk.ui.validation;
    import isNullOrUndefined = nts.uk.util.isNullOrUndefined;
    import isNullOrEmpty = nts.uk.util.isNullOrEmpty;
    import info = nts.uk.ui.dialog.info;
    import dialog = nts.uk.ui.dialog;

    export class ScreenModel {
        year: KnockoutObservable<string> = ko.observable(null);
        japanYear: KnockoutObservable<string> = ko.observable(null);

        empDeptItems: Array<EmpInfoDeptDto> = [];
        empAmountItems: Array<RsdtTaxPayAmountDto> = [];

        ccg001ComponentOption: GroupOption = null;
        baseDate: string;
        empSearchItems: Array<EmployeeSearchDto>;

        residentTaxValidator = new validation.NumberValidator(getText("QMM025_28"), "ResidentTax", {required: true});

        employIdLogin: any;

        enableA2_8: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            let self = this;
            self.employIdLogin = __viewContext.user.employeeId;

            self.year.subscribe(newYear => {
                let year = self.formatYear(newYear);
                let yearJp = time.yearInJapanEmpire(year);
                if (isNullOrUndefined(yearJp)) {
                    self.japanYear("");
                } else {
                    self.japanYear("(" + yearJp.toString() + ")");
                }

            });

            self.ccg001ComponentOption = <GroupOption>{
                /** Common properties */
                systemType: 3,
                showEmployeeSelection: true,
                showQuickSearchTab: false,
                showAdvancedSearchTab: true,
                showBaseDate: true,
                showClosure: null,
                showAllClosure: null,
                showPeriod: null,
                periodFormatYM: null,

                /** Required parameter */
                baseDate: moment.utc().toISOString(),
                periodStartDate: null,
                periodEndDate: null,
                inService: true,
                leaveOfAbsence: true,
                closed: true,
                retirement: true,

                /** Quick search tab options */
                showAllReferableEmployee: null,
                showOnlyMe: null,
                showSameWorkplace: null,
                showSameWorkplaceAndChild: null,

                /** Advanced search properties */
                showEmployment: true,
                showDepartment: true,
                showWorkplace: false,
                showClassification: true,
                showJobTitle: true,
                showWorktype: true,
                isMutipleCheck: true,
                tabindex: 6,
                showOnStart: true,

                /**
                 * Self-defined function: Return data from CCG001
                 * @param: data: the data return from CCG001
                 */
                returnDataFromCcg001: function (data: Ccg001ReturnedData) {
                    self.empSearchItems = data.listEmployee;
                    self.baseDate = data.baseDate;
                    self.initData();
                }
            }
        }

        startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            block.invisible();
            // Start component
            $('#com-ccg001').ntsGroupComponent(self.ccg001ComponentOption);
            self.year(self.formatYear(new Date()));
            self.loadMGrid();
            block.clear();
            dfd.resolve();
            return dfd.promise();
        }

        loadMGrid() {
            let self = this;
            let height = $(window).height() - 90 - 290; 
            let width = $(window).width() + 20 - 1170;
            new nts.uk.ui.mgrid.MGrid($("#grid")[0], {
                width: "1170px",
                height: "200px",
                subWidth: width + "px",
                subHeight: height + "px",
                headerHeight: '30px',
                dataSource: self.empAmountItems,
                primaryKey: 'sid',
                primaryKeyDataType: 'string',
                rowVirtualization: true,
                virtualization: true,
                virtualizationMode: 'continuous',
                enter: 'right',
                autoFitWindow: false,
                hidePrimaryKey: true,
                errorsOnPage: false,
                columns: [
                    {headerText: "ID", key: 'sid', dataType: 'string', hidden: true},
                    {
                        headerText: '', key: 'selectedEmp', width: "35px", dataType: 'boolean',
                        checkbox: true,
                        ntsControl: 'CheckBoxEmp'
                    },
                    // A3_2
                    {
                        headerText: getText("QMM025_9"), key: 'departmentName', dataType: 'string', width: "100px",
                        ntsControl: "Label"
                    },
                    // A3_3
                    {
                        headerText: getText("QMM025_10"), key: 'empCd', dataType: 'string', width: "100px",
                        ntsControl: "Label"
                    },
                    // A3_4
                    {
                        headerText: getText("QMM025_11"), key: 'empName', dataType: 'string', width: "100px",
                        ntsControl: "Label"
                    },
                    // A3_5
                    {
                        headerText: getText("QMM025_12"), key: 'rsdtTaxPayeeName', dataType: 'string', width: "100px",
                        ntsControl: "Label", headerCssClass: "left-align",
                    },
                    // A3_6
                    {
                        headerText: getText("QMM025_13"), key: 'yearTaxAmount', dataType: 'string', width: "100px",
                        ntsControl: "Label", columnCssClass: "halign-right"
                    },
                    // A3_7
                    {
                        headerText: getText("QMM025_14"), key: 'inputAtr', width: "35px", dataType: 'boolean',
                        ntsControl: 'CheckBoxInputAtr'
                    },

                    {
                        headerText: getText("QMM025_15"), key: 'amountJune', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_16"), key: 'amountJuly', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_17"), key: 'amountAugust', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_18"), key: 'amountSeptember', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_19"), key: 'amountOctober', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_20"), key: 'amountNovember', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_21"), key: 'amountDecember', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_22"), key: 'amountJanuary', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_23"), key: 'amountFebruary', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_24"), key: 'amountMarch', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_25"), key: 'amountApril', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    },
                    {
                        headerText: getText("QMM025_26"), key: 'amountMay', dataType: 'string', width: '130px',
                        columnCssClass: 'currency-symbol',
                        constraint: {
                            cDisplayType: "Currency",
                            min: self.residentTaxValidator.constraint.min,
                            max: self.residentTaxValidator.constraint.max,
                            required: false
                        }
                    }
                ],
                ntsControls: [
                    {
                        name: 'CheckBoxInputAtr', options: {value: 1, text: ''}, optionsValue: 'value',
                        optionsText: 'text', controlType: 'CheckBox', enable: true,
                        onChange: function (id, columnKey, value, rowData) {
                            self.selectInputAtr(id, value);
                        }
                    },
                    {
                        name: 'CheckBoxEmp', options: {value: 1, text: ''}, optionsValue: 'value',
                        optionsText: 'text', controlType: 'CheckBox', enable: true,
                        onChange: function (id, columnKey, value, rowData) {
                            self.selectEmp(id, value, rowData);
                        }
                    }
                ],
                features: [
                    {
                        name: "Sorting",
                        columnSettings: [
                            {columnKey: "departmentName", allowSorting: true, type: "String"},
                            {columnKey: "empCd", allowSorting: true, type: "String"},
                            {columnKey: "empName", allowSorting: true, type: "String"},
                            {columnKey: "rsdtTaxPayeeName", allowSorting: true},
                            {columnKey: "yearTaxAmount", allowSorting: true, type: "String"},
                            {columnKey: "inputAtr", allowSorting: true, type: "String"},
                            {columnKey: "amountJune", allowSorting: true, type: "Number"},
                            {columnKey: "amountJuly", allowSorting: true, type: "Number"},
                            {columnKey: "amountAugust", allowSorting: true, type: "Number"},
                            {columnKey: "amountSeptember", allowSorting: true, type: "Number"},
                            {columnKey: "amountOctober", allowSorting: true, type: "Number"},
                            {columnKey: "amountNovember", allowSorting: true, type: "Number"},
                            {columnKey: "amountDecember", allowSorting: true, type: "Number"},
                            {columnKey: "amountJanuary", allowSorting: true, type: "Number"},
                            {columnKey: "amountFebruary", allowSorting: true, type: "Number"},
                            {columnKey: "amountMarch", allowSorting: true, type: "Number"},
                            {columnKey: "amountApril", allowSorting: true, type: "Number"},
                            {columnKey: "amountMay", allowSorting: true, type: "Number"}
                        ]
                    },
                    {
                        name: 'Resizing',
                        columnSettings: [{
                            columnKey: 'sid', allowResizing: false, minimumWidth: 0
                        }]
                    },
                    {
                        name: 'HeaderStyles',
                        columns: [
                            {key: 'selectedEmp', colors: ['left-align']},
                            {key: 'departmentName', colors: ['left-align']},
                            {key: 'empCd', colors: ['left-align']},
                            {key: 'empName', colors: ['left-align']},
                            {key: 'rsdtTaxPayeeName', colors: ['left-align']},
                            {key: 'yearTaxAmount', colors: ['left-align']}
                        ]
                    },
                    {
                        name: 'Paging',
                        pageSize: 20,
                        currentPageIndex: 0,
                        loaded: function () {
                            self.setPageStatus();
                        }
                    },
                    {
                        name: "ColumnFixing",
                        showFixButtons: false,
                        fixingDirection: 'left',
                        columnSettings: [
                            {
                                columnKey: "sid",
                                isFixed: true
                            },
                            {
                                columnKey: "selectedEmp",
                                isFixed: true
                            },
                            {
                                columnKey: "departmentName",
                                isFixed: true
                            },
                            {
                                columnKey: "empCd",
                                isFixed: true
                            },
                            {
                                columnKey: "empName",
                                isFixed: true
                            },
                            {
                                columnKey: "rsdtTaxPayeeName",
                                isFixed: true
                            },
                            {
                                columnKey: "yearTaxAmount",
                                isFixed: true
                            }
                        ]
                    }
                ]
            }).create();
            self.setPageStatus();
        }

        selectEmp(id, value, rowData: RsdtTaxPayAmountDto) {
            let self = this;
            self.setDelete(id, value, rowData.inputAtr);
            self.enableA2_8(!_.isEmpty(self.getSidSelected()));
        }

        selectInputAtr(id, value) {
            let self = this;
            self.setDisable(id, value)
        }

        setPageStatus() {
            let self = this;
            let data = $("#grid").mGrid("dataSource");
            _.each(data, (item: RsdtTaxPayAmountDto) => {
                self.setDelete(item.sid, item.selectedEmp, item.inputAtr);
            })
        }

        setDelete(sid: string, isDelete: boolean, isAllMonth: boolean) {
            let self = this;
            self.setStateControl(sid, "selectedEmp", isDelete);
            self.setStateControl(sid, "departmentName", isDelete);
            self.setStateControl(sid, "empCd", isDelete);
            self.setStateControl(sid, "empName", isDelete);
            self.setStateControl(sid, "rsdtTaxPayeeName", isDelete);
            self.setStateControl(sid, "yearTaxAmount", isDelete);
            self.disableControl(sid, "inputAtr", isDelete);
            self.disableControl(sid, "amountJune", isDelete);
            self.disableControl(sid, "amountJuly", isDelete);
            self.setDisable(sid, isDelete ? false : isAllMonth);
        }

        setDisable(sid: string, isAllMonth: boolean) {
            let self = this;
            self.disableControl(sid, "amountAugust", !isAllMonth);
            self.disableControl(sid, "amountSeptember", !isAllMonth);
            self.disableControl(sid, "amountOctober", !isAllMonth);
            self.disableControl(sid, "amountNovember", !isAllMonth);
            self.disableControl(sid, "amountDecember", !isAllMonth);
            self.disableControl(sid, "amountJanuary", !isAllMonth);
            self.disableControl(sid, "amountFebruary", !isAllMonth);
            self.disableControl(sid, "amountMarch", !isAllMonth);
            self.disableControl(sid, "amountApril", !isAllMonth);
            self.disableControl(sid, "amountMay", !isAllMonth);
        }

        setStateControl(rowId, columnKey, isDelete) {
            if (isDelete) {
                $("#grid").mGrid("setState", rowId, columnKey, ['delete']);
            } else {
                $("#grid").mGrid("clearState", rowId, columnKey, ['delete'])
            }
        }

        disableControl(rowId, columnKey, isDisable) {
            if (isDisable) {
                $("#grid").mGrid("disableNtsControlAt", rowId, columnKey)
            } else {
                $("#grid").mGrid("enableNtsControlAt", rowId, columnKey)
            }
        }

        formatYear(date) {
            return moment(date).format("YYYY");
        }

        createParamGet() {
            let self = this;
            let listSId = _.map(self.empSearchItems, (item: EmployeeSearchDto) => {
                return item.employeeId;
            });
            let param = {
                listSId: listSId,
                baseDate: self.baseDate,
                year: self.formatYear(self.year())
            };
            return param;
        }

        /**
         * ????????????
         */
        initData(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();
            block.invisible();
            $("#A2_3").ntsError('check');
            if (nts.uk.ui.errors.hasError()) {
                $("#grid").mGrid("destroy");
                self.empAmountItems = [];
                self.loadMGrid();
                block.clear();
                return;
            }
            let param = self.createParamGet();
            let getEmpInfoDept = service.getEmpInfoDept(param);
            let getRsdtTaxPayAmount = service.getRsdtTaxPayAmount(param);

            $.when(getEmpInfoDept, getRsdtTaxPayAmount).done((depts, amounts) => {
                self.empDeptItems = EmpInfoDeptDto.fromApp(depts);
                let data: Array<RsdtTaxPayAmountDto> = RsdtTaxPayAmountDto.fromApp(amounts, self.empDeptItems,
                    self.empSearchItems);
                self.empAmountItems = data;
                $("#grid").mGrid("destroy");
                self.loadMGrid();
            }).always(() => {
                $("#A2_3").focus();
                block.clear();
                dfd.resolve();
            })
            return dfd.promise();
        }

        /**
         * ?????????????????????
         */
        getEmpAmount() {
            let self = this;
            block.invisible();
            let param = self.createParamGet();
            let getRsdtTaxPayAmount = service.getRsdtTaxPayAmount(param);
            $.when(getRsdtTaxPayAmount).done((amounts: Array<IRsdtTaxPayAmountDto>) => {
                let data: Array<RsdtTaxPayAmountDto> = RsdtTaxPayAmountDto.fromApp(amounts, self.empDeptItems,
                    self.empSearchItems);
                self.empAmountItems = data;
                $("#grid").mGrid("destroy");
                self.loadMGrid();
            }).always(() => {
                block.clear();
                self.focusA3_1();
            })
        }

        /**
         * ??????????????????????????????
         */
        registerAmount() {
            let self = this;
            block.invisible();
            if (nts.uk.ui.errors.hasError() || !self.isValidForm()) {
                block.clear();
                return false;
            }

            let empAmountItems: Array<RsdtTaxPayAmountDto> = $("#grid").mGrid("dataSource", true);
            service.registerTaxPayAmount(new RegisterCommand(empAmountItems, self.formatYear(self.year()))).done(() => {
                info({messageId: "Msg_15"}).then(() => {
                    self.getEmpAmount();
                });
            }).always(() => {
                self.focusA3_1();
                block.clear();
            })
        }

        /**
         * ??????????????????????????????
         */
        deleteAmount() {
            let self = this;
            block.invisible();
            dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                service.deleteTaxPayAmount(new DeleteCommand(self.getSidSelected(), self.formatYear(self.year()))).done(() => {
                    info({messageId: "Msg_16"}).then(() => {
                        self.enableA2_8(false);
                        self.getEmpAmount();
                    });
                }).always(() => {
                    self.focusA3_1();
                    block.clear();
                })
            }).ifNo(function() {
                block.clear();
                return false;
            })
        }

        getSidSelected(): Array<string> {
            let empAmountItems: Array<RsdtTaxPayAmountDto> = $("#grid").mGrid("dataSource", true);
            let listEmpSelected = _.filter(empAmountItems, (item: RsdtTaxPayAmountDto) => {
                return item.selectedEmp;
            });
            let listSId = _.map(listEmpSelected, (item: RsdtTaxPayAmountDto) => {
                return item.sid;
            });
            return listSId;
        }

        isValidForm() {
            let self = this,
                check: any;
            let errorList = $("#grid").mGrid("errors", true);
            if (_.isEmpty(errorList)) {
                return true;
            }
            let empAmountItems: Array<RsdtTaxPayAmountDto> = $("#grid").mGrid("dataSource", true);
            let isValid = true;
            _.each(errorList, error => {
                let emp = _.find(empAmountItems, (item: RsdtTaxPayAmountDto) => {
                    return item.sid == error.rowId;
                });
                if (emp.selectedEmp) return;
                if (emp.inputAtr) {
                    isValid = false;
                }
                if (error.columnKey == "amountJune" || error.columnKey == "amountJuly") {
                    isValid = false;
                }
            });
            return isValid;
        }


        jumpToCps001() {
            nts.uk.request.jump("com", "/view/cps/001/a/index.xhtml");
        }

        focusA3_1() {
            $("#grid_container").focus();
        }

    }

    class EmpInfoDeptDto {
        sid: string;//??????ID
        departmentName: string;//???????????????

        constructor() {
        }

        static fromApp(items: Array<IEmpInfoDeptDto>): Array<EmpInfoDeptDto> {
            let results: Array<EmpInfoDeptDto> = [];
            _.each(items, (item: IEmpInfoDeptDto) => {
                let dto: EmpInfoDeptDto = new EmpInfoDeptDto();
                dto.sid = item.sid;
                dto.departmentName = item.departmentName;
                results.push(dto);
            })

            return results;
        }
    }

    interface IEmpInfoDeptDto {
        sid: string;//??????ID
        departmentName: string;//???????????????
    }

    class RsdtTaxPayAmountDto {
        sid: string;//??????ID
        selectedEmp: boolean;
        departmentName: string;//???????????????
        empCd: string;//???????????????
        empName: string;//????????????
        year: number;//??????
        rsdtTaxPayeeName: string;//??????????????????.??????
        yearTaxAmount: string;//?????????
        inputAtr: boolean;//??????????????????????????????.????????????
        amountJanuary: string;//??????????????????????????????.???????????????.1????????????
        amountFebruary: string;//??????????????????????????????.???????????????.2????????????
        amountMarch: string;//??????????????????????????????.???????????????.3????????????
        amountApril: string;//??????????????????????????????.???????????????.4????????????
        amountMay: string;//??????????????????????????????.???????????????.5????????????
        amountJune: string;//??????????????????????????????.???????????????.6????????????
        amountJuly: string;//??????????????????????????????.???????????????.7????????????
        amountAugust: string;//??????????????????????????????.???????????????.8????????????
        amountSeptember: string;//??????????????????????????????.???????????????.9????????????
        amountOctober: string;//??????????????????????????????.???????????????.10????????????
        amountNovember: string;//??????????????????????????????.???????????????.11????????????
        amountDecember: string;//??????????????????????????????.???????????????.12????????????

        constructor() {
        }

        static fromApp(items: Array<IRsdtTaxPayAmountDto>,
                       empDeptItems: Array<EmpInfoDeptDto>,
                       empSearchItems: Array<EmployeeSearchDto>): Array<RsdtTaxPayAmountDto> {
            let results: Array<RsdtTaxPayAmountDto> = [];
            _.each(items, (item: IRsdtTaxPayAmountDto) => {
                let dto: RsdtTaxPayAmountDto = new RsdtTaxPayAmountDto();
                dto.sid = item.sid;
                dto.selectedEmp = false;
                let empDept: EmpInfoDeptDto = _.find(empDeptItems, {'sid': item.sid});
                if (empDept == null) {
                    dto.departmentName = "";
                } else {
                    dto.departmentName = empDept.departmentName;
                }

                let empSearch: EmployeeSearchDto = _.find(empSearchItems, {'employeeId': item.sid});
                if (empSearch == null) {
                    dto.empCd = "";
                    dto.empName = "";
                } else {
                    dto.empCd = empSearch.employeeCode;
                    dto.empName = empSearch.employeeName;
                }

                dto.year = item.year;
                dto.rsdtTaxPayeeName = item.rsdtTaxPayeeName;
                dto.inputAtr = ResidentTaxInputAtr.ALL_MONTH == item.inputAtr;
                if (dto.inputAtr) {
                    dto.yearTaxAmount = _.sum([
                        item.amountJanuary, item.amountFebruary, item.amountMarch, item.amountApril, item.amountMay,
                        item.amountJune, item.amountJuly, item.amountAugust, item.amountSeptember, item.amountOctober,
                        item.amountNovember, item.amountDecember
                    ]).toString();
                } else {
                    dto.yearTaxAmount = _.sum([item.amountJune, item.amountJuly * 11]).toString();
                }
                dto.yearTaxAmount = nts.uk.ntsNumber.formatNumber(dto.yearTaxAmount, new nts.uk.ui.option.NumberEditorOption({grouplength: 3}));

                dto.amountJanuary = isNullOrUndefined(item.amountJanuary) ? null : item.amountJanuary.toString();
                dto.amountFebruary = isNullOrUndefined(item.amountFebruary) ? null : item.amountFebruary.toString();
                dto.amountMarch = isNullOrUndefined(item.amountMarch) ? null : item.amountMarch.toString();
                dto.amountApril = isNullOrUndefined(item.amountApril) ? null : item.amountApril.toString();
                dto.amountMay = isNullOrUndefined(item.amountMay) ? null : item.amountMay.toString();
                dto.amountJune = isNullOrUndefined(item.amountJune) ? null : item.amountJune.toString();
                dto.amountJuly = isNullOrUndefined(item.amountJuly) ? null : item.amountJuly.toString();
                dto.amountAugust = isNullOrUndefined(item.amountAugust) ? null : item.amountAugust.toString();
                dto.amountSeptember = isNullOrUndefined(item.amountSeptember) ? null : item.amountSeptember.toString();
                dto.amountOctober = isNullOrUndefined(item.amountOctober) ? null : item.amountOctober.toString();
                dto.amountNovember = isNullOrUndefined(item.amountNovember) ? null : item.amountNovember.toString();
                dto.amountDecember = isNullOrUndefined(item.amountDecember) ? null : item.amountDecember.toString();
                results.push(dto);
            })

            return results;
        }
    }

    interface IRsdtTaxPayAmountDto {
        sid: string;//??????ID
        year: number;//??????
        rsdtTaxPayeeName: string;//??????????????????.??????
        inputAtr: number;//??????????????????????????????.????????????
        amountJanuary: number;//??????????????????????????????.???????????????.1????????????
        amountFebruary: number;//??????????????????????????????.???????????????.2????????????
        amountMarch: number;//??????????????????????????????.???????????????.3????????????
        amountApril: number;//??????????????????????????????.???????????????.4????????????
        amountMay: number;//??????????????????????????????.???????????????.5????????????
        amountJune: number;//??????????????????????????????.???????????????.6????????????
        amountJuly: number;//??????????????????????????????.???????????????.7????????????
        amountAugust: number;//??????????????????????????????.???????????????.8????????????
        amountSeptember: number;//??????????????????????????????.???????????????.9????????????
        amountOctober: number;//??????????????????????????????.???????????????.10????????????
        amountNovember: number;//??????????????????????????????.???????????????.11????????????
        amountDecember: number;//??????????????????????????????.???????????????.12????????????
    }

    class RegisterCommand {
        listEmpPayAmount: Array<EmpPayAmountCommand>;
        year: string;

        constructor(empAmountItems: Array<RsdtTaxPayAmountDto>, year: string) {
            this.listEmpPayAmount = EmpPayAmountCommand.toCommand(empAmountItems);
            this.year = year;
        }
    }

    class EmpPayAmountCommand {
        sid: string;//??????ID
        rsdtTaxPayeeName: string;//??????????????????.??????
        inputAtr: number;//??????????????????????????????.????????????
        amountJanuary: string;//??????????????????????????????.???????????????.1????????????
        amountFebruary: string;//??????????????????????????????.???????????????.2????????????
        amountMarch: string;//??????????????????????????????.???????????????.3????????????
        amountApril: string;//??????????????????????????????.???????????????.4????????????
        amountMay: string;//??????????????????????????????.???????????????.5????????????
        amountJune: string;//??????????????????????????????.???????????????.6????????????
        amountJuly: string;//??????????????????????????????.???????????????.7????????????
        amountAugust: string;//??????????????????????????????.???????????????.8????????????
        amountSeptember: string;//??????????????????????????????.???????????????.9????????????
        amountOctober: string;//??????????????????????????????.???????????????.10????????????
        amountNovember: string;//??????????????????????????????.???????????????.11????????????
        amountDecember: string;//??????????????????????????????.???????????????.12????????????

        constructor(data: RsdtTaxPayAmountDto) {
            let dataDefault = "0";
            this.sid = data.sid;
            this.amountJune = isNullOrEmpty(data.amountJune) ? dataDefault : data.amountJune;
            this.amountJuly = isNullOrEmpty(data.amountJuly) ? dataDefault : data.amountJuly;
            this.rsdtTaxPayeeName = data.rsdtTaxPayeeName;
            if (data.inputAtr) {
                this.inputAtr = ResidentTaxInputAtr.ALL_MONTH;
                this.amountJanuary = isNullOrEmpty(data.amountJanuary) ? dataDefault : data.amountJanuary;
                this.amountFebruary = isNullOrEmpty(data.amountFebruary) ? dataDefault : data.amountFebruary;
                this.amountMarch = isNullOrEmpty(data.amountMarch) ? dataDefault : data.amountMarch;
                this.amountApril = isNullOrEmpty(data.amountApril) ? dataDefault : data.amountApril;
                this.amountMay = isNullOrEmpty(data.amountMay) ? dataDefault : data.amountMay;
                this.amountAugust = isNullOrEmpty(data.amountAugust) ? dataDefault : data.amountAugust;
                this.amountSeptember = isNullOrEmpty(data.amountSeptember) ? dataDefault : data.amountSeptember;
                this.amountOctober = isNullOrEmpty(data.amountOctober) ? dataDefault : data.amountOctober;
                this.amountNovember = isNullOrEmpty(data.amountNovember) ? dataDefault : data.amountNovember;
                this.amountDecember = isNullOrEmpty(data.amountDecember) ? dataDefault : data.amountDecember;
            } else {
                this.inputAtr = ResidentTaxInputAtr.NOT_ALL_MONTH;
                this.amountJanuary = this.amountJuly;
                this.amountFebruary = this.amountJuly;
                this.amountMarch = this.amountJuly;
                this.amountApril = this.amountJuly;
                this.amountMay = this.amountJuly;
                this.amountAugust = this.amountJuly;
                this.amountSeptember = this.amountJuly;
                this.amountOctober = this.amountJuly;
                this.amountNovember = this.amountJuly;
                this.amountDecember = this.amountJuly;
            }
        }

        static toCommand(datas: Array<RsdtTaxPayAmountDto>) {
            let result: Array<EmpPayAmountCommand> = [];
            _.each(datas, (item: RsdtTaxPayAmountDto) => {
                if (item.selectedEmp) return;
                result.push(new EmpPayAmountCommand(item));
            })
            return result;
        }
    }

    class DeleteCommand {
        listSId: Array<string>;
        year: string;

        constructor(listSId: Array<string>, year: string) {
            this.listSId = listSId;
            this.year = year;
        }
    }

    // Note: Defining these interfaces are optional
    interface GroupOption {
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
        showWorkplace?: boolean; // ????????????
        showClassification?: boolean; // ????????????
        showJobTitle?: boolean; // ????????????
        showWorktype?: boolean; // ????????????
        isMutipleCheck?: boolean; // ???????????????
        isTab2Lazy?: boolean;

        /** Data returned */
        returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
    }

    interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceId: string;
        workplaceName: string;
    }

    interface Ccg001ReturnedData {
        baseDate: string; // ?????????
        closureId?: number; // ??????ID
        periodStart: string; // ?????????????????????)
        periodEnd: string; // ????????????????????????
        listEmployee: Array<EmployeeSearchDto>; // ????????????
    }

    class CellState {
        rowId: number;
        columnKey: string;
        state: Array<any>

        constructor(rowId: any, columnKey: string, state: Array<any>) {
            this.rowId = rowId;
            this.columnKey = columnKey;
            this.state = state;
        }
    }

    /**
     * ?????????????????????
     */
    enum ResidentTaxInputAtr {
        ALL_MONTH = 1,
        NOT_ALL_MONTH = 0
    }

}