module nts.uk.at.view.kdw003.a.viewmodel {
    export interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceCode: string;
        workplaceId: string;
        workplaceName: string;
    }

    var LIST_FIX_HEADER = [
        { headerText: 'ID', key: 'id', dataType: 'String', width: '30px', ntsControl: 'Label' },
        { headerText: '状<br/>態', key: 'state', dataType: 'String', width: '30px', ntsControl: 'Label' },
        { headerText: 'ER/AL', key: 'error', dataType: 'String', width: '60px', ntsControl: 'Label' },
        { headerText: nts.uk.resource.getText("KDW003_41"), key: 'date', dataType: 'String', width: '90px', ntsControl: 'Label' },
        { headerText: nts.uk.resource.getText("KDW003_42"), key: 'sign', dataType: 'boolean', width: '35px', ntsControl: 'Checkbox' },
        { headerText: nts.uk.resource.getText("KDW003_32"), key: 'employeeCode', dataType: 'String', width: '120px', ntsControl: 'Label' },
        { headerText: nts.uk.resource.getText("KDW003_33"), key: 'employeeName', dataType: 'String', width: '190px', ntsControl: 'Label' },
        { headerText: '', key: "picture-person", dataType: "string", width: '35px', ntsControl: 'Image' }
    ];

    export class ScreenModel {
        fixHeaders: KnockoutObservableArray<any> = ko.observableArray([]);

        legendOptions: any;
        //grid user setting
        cursorMoveDirections: KnockoutObservableArray<any> = ko.observableArray([
            { code: 0, name: "縦" },
            { code: 1, name: "横" }
        ]);
        selectedDirection: KnockoutObservable<any> = ko.observable(0);
        displayWhenZero: KnockoutObservable<boolean> = ko.observable(false);
        showHeaderNumber: KnockoutObservable<boolean> = ko.observable(false);
        showProfileIcon: KnockoutObservable<boolean> = ko.observable(false);
        //ccg001 component: search employee
        ccg001: any;
        baseDate: KnockoutObservable<Date> = ko.observable(new Date());
        //kcp009 component: employee picker
        selectedEmployee: KnockoutObservable<any> = ko.observable(null);
        lstEmployee: KnockoutObservableArray<any> = ko.observableArray([]);;
        //data grid
        displayFormatOptions: KnockoutObservableArray<any>;
        displayFormat: KnockoutObservable<number> = ko.observable(null);
        headersGrid: KnockoutObservableArray<any>;
        sheetsGrid: KnockoutObservableArray<any> = ko.observableArray([]);
        fixColGrid: KnockoutObservableArray<any>;
        dailyPerfomanceData: KnockoutObservableArray<any> = ko.observableArray([]);
        cellStates: KnockoutObservableArray<any> = ko.observableArray([]);
        rowStates: KnockoutObservableArray<any> = ko.observableArray([]);
        dpData: Array<any> = [];
        headerColors: KnockoutObservableArray<any> = ko.observableArray([]);
        textColors: KnockoutObservableArray<any> = ko.observableArray([]);
        lstDate: KnockoutObservableArray<any> = ko.observableArray([]);
        optionalHeader: Array<any> = [];
        employeeModeHeader: Array<any> = [];
        dateModeHeader: Array<any> = [];
        errorModeHeader: Array<any> = [];
        formatCodes: KnockoutObservableArray<any> = ko.observableArray([]);
        employeeModeFixCol: Array<any> = [
            { columnKey: 'id', isFixed: true },
            { columnKey: 'state', isFixed: true },
            { columnKey: 'error', isFixed: true },
            { columnKey: 'date', isFixed: true }
        ];
        dateModeFixCol: Array<any> = [
            { columnKey: 'id', isFixed: true },
            { columnKey: 'state', isFixed: true },
            { columnKey: 'error', isFixed: true },
            { columnKey: 'employeeCode', isFixed: true },
            { columnKey: 'employeeName', isFixed: true }
        ];
        errorModeFixCol: Array<any> = [
            { columnKey: 'id', isFixed: true },
            { columnKey: 'state', isFixed: true },
            { columnKey: 'error', isFixed: true },
            { columnKey: 'employeeCode', isFixed: true },
            { columnKey: 'employeeName', isFixed: true },
            { columnKey: 'date', isFixed: true }
        ];
        // date ranger component
        dateRanger: KnockoutObservable<any> = ko.observable(null);
        // date picker component
        selectedDate: KnockoutObservable<any> = ko.observable(null);

        showButton: KnockoutObservable<AuthorityDetailModel> = ko.observable(null);

        comboItems: KnockoutObservableArray<any> = ko.observableArray([new ItemModel('1', '基本給'),
            new ItemModel('2', '役職手当'),
            new ItemModel('3', '基本給2')]);

        comboColumns: KnockoutObservableArray<any> = ko.observableArray([{ prop: 'code', length: 4 },
            { prop: 'name', length: 8 }]);

        constructor() {
            var self = this;
            self.initLegendButton();
            self.initDateRanger();
            self.initDisplayFormat();
            self.headersGrid = ko.observableArray(self.employeeModeHeader);
            self.fixColGrid = ko.observableArray(self.employeeModeFixCol);
            // show/hide header number
            self.showHeaderNumber.subscribe((val) => {
                self.reloadGrid();
            });
            // show/hide profile icon
            self.showProfileIcon.subscribe((val) => {
                self.reloadGrid();
            });
        }

        initLegendButton() {
            var self = this;
            self.legendOptions = {
                items: [
                    { colorCode: '#94B7FE', labelText: '手修正（本人）' },
                    { colorCode: '#CEE6FF', labelText: '手修正（他人）' },
                    { colorCode: '#BFEA60', labelText: '申請反映' },
                    { colorCode: '#F69164', labelText: '計算値' },
                    { colorCode: '#FD4D4D', labelText: nts.uk.resource.getText("KDW003_44") },
                    { colorCode: '#F6F636', labelText: nts.uk.resource.getText("KDW003_45") },
                ]
            };
        }

        initDateRanger() {
            var self = this;
            self.dateRanger.subscribe((dateRange) => {
                if (dateRange && dateRange.startDate && dateRange.endDate) {
                    self.selectedDate(dateRange.startDate);
                    var elementDate = dateRange.startDate;
                    while (!moment(elementDate, "YYYY/MM/DD").isAfter(dateRange.endDate)) {
                        self.lstDate.push({ date: elementDate });
                        elementDate = moment(elementDate, "YYYY/MM/DD").add(1, 'd').format("YYYY/MM/DD");
                    }
                }
            });
            self.dateRanger({
                startDate: moment().add(-1, "M").format("YYYY/MM/DD"),
                endDate: moment().format("YYYY/MM/DD")
            });
        }

        initDisplayFormat() {
            var self = this;
            self.displayFormatOptions = ko.observableArray([
                { code: 0, name: nts.uk.resource.getText("Enum_DisplayFormat_Individual") },
                { code: 1, name: nts.uk.resource.getText("Enum_DisplayFormat_ByDate") },
                { code: 2, name: nts.uk.resource.getText("Enum_DisplayFormat_ErrorAlarm") }
            ]);
            self.displayFormat(0);
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            var param = {
                dateRange: {
                    startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                    endDate: moment(self.dateRanger().endDate).utc().toISOString()
                },
                lstEmployee: []
            };
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            service.startScreen(param).done((data) => {
                console.log(data);
                self.formatCodes(data.lstControlDisplayItem.formatCode);
                _.each(data.lstControlDisplayItem.lstHeader, function(item) {
                    if (item.group.length > 0) {
                        delete item.group[0].group;
                        if (item.group[0].dataType == "String") {
                            item.group[0].onChange = self.search;
                            delete item.group[0].ntsControl;
                        } else {
                            delete item.group[0].onChange;
                            delete item.group[0].ntsControl;
                        }
                        delete item.group[1].group;
                    } else {
                        delete item.group;
                    }
                });
                _.each(data.lstFixedHeader, function(item) {
                    delete item.group;
                });
                // Fixed Header
                self.showButton = ko.observable(new AuthorityDetailModel(data.authorityDto));
                self.fixHeaders(data.lstFixedHeader);
                self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3], self.fixHeaders()[4]];
                self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[4]];
                self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[4]];

                self.lstEmployee(_.orderBy(data.lstEmployee, ['code'], ['asc']));
                self.receiveData(data);
                self.selectedEmployee(self.lstEmployee()[0].id);
                self.extractionData();
                self.loadGrid();
                self.extraction();
                self.initCcg001();
                self.loadCcg001();
                nts.uk.ui.block.clear();
                dfd.resolve();
            }).fail(function(error) {
                nts.uk.ui.dialog.alert(error.message);
                nts.uk.ui.block.clear();
            });
            return dfd.promise();
        }

        receiveData(data) {
            var self = this;
            self.dpData = data.lstData;
            self.cellStates(data.lstCellState);
            self.optionalHeader = data.lstControlDisplayItem.lstHeader;
            self.sheetsGrid(data.lstControlDisplayItem.lstSheet);
            self.sheetsGrid.valueHasMutated();
        }

        btnExtraction_Click() {
            var self = this;
            if (self.displayFormat() == 0) {
                $("#emp-component").css("display", "block");
                $("#cbListDate").css("display", "none");
            } else if (self.displayFormat() == 1) {
                $("#cbListDate").css("display", "block");
                $("#emp-component").css("display", "none");
            } else {
                $("#cbListDate").css("display", "none");
                $("#emp-component").css("display", "none");
            }
            let lstEmployee = [];
            if (self.displayFormat() === 0) {
                lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                }));
            } else {
                lstEmployee = self.lstEmployee();
            }
            let param = {
                dateRange: {
                    startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                    endDate: moment(self.dateRanger().endDate).utc().toISOString()
                },
                lstEmployee: lstEmployee
            };
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            service.startScreen(param).done((data) => {
                self.formatCodes(data.lstControlDisplayItem.formatCode);
                _.each(data.lstControlDisplayItem.lstHeader, function(item) {
                    if (item.group.length > 0) {
                        delete item.group[0].group;
                        if (item.group[0].dataType == "String") {
                            item.group[0].onChange = self.search;
                            delete item.group[0].ntsControl;
                        } else {
                            delete item.group[0].onChange;
                            delete item.group[0].ntsControl;
                        }
                        delete item.group[1].group;
                    } else {
                        delete item.group;
                    }
                });
                _.each(data.lstFixedHeader, function(item) {
                    delete item.group;
                });
                self.receiveData(data);
                self.extraction();
                nts.uk.ui.block.clear();
            }).fail(function(error) {
                nts.uk.ui.dialog.alert(error.message);
                nts.uk.ui.block.clear();
            });
        }

        showErrorDialog() {
            var self = this;
            let lstEmployee = [];
            if (self.displayFormat() === 0) {
                lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                }));
            } else {
                lstEmployee = self.lstEmployee();
            }
            let param = {
                dateRange: {
                    startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                    endDate: moment(self.dateRanger().endDate).utc().toISOString()
                },
                lstEmployee: lstEmployee
            };
            nts.uk.ui.windows.setShared("paramToGetError", param);
            nts.uk.ui.windows.sub.modal("/view/kdw/003/b/index.xhtml").onClosed(() => {
            });
        }
        changeExtractionCondition() {
            var self = this;
            if (self.displayFormat() == 0) {
                $("#emp-component").css("display", "block");
                $("#cbListDate").css("display", "none");
            } else if (self.displayFormat() == 1) {
                $("#cbListDate").css("display", "block");
                $("#emp-component").css("display", "none");
            } else {
                $("#cbListDate").css("display", "none");
                $("#emp-component").css("display", "none");
            }
            let lstEmployee = [];
            if (self.displayFormat() === 0) {
                lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                }));
            } else {
                lstEmployee = self.lstEmployee();
            }
            //  let errorCodes =["0001","0002","003"];      
            nts.uk.ui.windows.sub.modal("/view/kdw/003/d/index.xhtml").onClosed(() => {
                 nts.uk.ui.block.clear();
                let errorCodes = nts.uk.ui.windows.getShared('errorAlarmList');
                if (errorCodes != undefined) {
                    let param = {
                        dateRange: {
                            startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                            endDate: moment(self.dateRanger().endDate).utc().toISOString()
                        },
                        lstEmployee: lstEmployee,
                        errorCodes: errorCodes
                    };
                    nts.uk.ui.block.invisible();
                    nts.uk.ui.block.grayout();
                    service.selectErrorCode(param).done((data) => {
                        _.each(data.lstControlDisplayItem.lstHeader, function(item) {
                            if (item.group.length > 0) {
                                delete item.group[0].group;
                                if (item.group[0].dataType == "String") {
                                    item.group[0].onChange = self.search;
                                    delete item.group[0].ntsControl;
                                } else {
                                    delete item.group[0].onChange;
                                    delete item.group[0].ntsControl;
                                }
                                delete item.group[1].group;
                            } else {
                                delete item.group;
                            }
                        });
                        _.each(data.lstFixedHeader, function(item) {
                            delete item.group;
                        });
                        self.receiveData(data);
                        self.extraction();
                        nts.uk.ui.block.clear();
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alert(error.message);
                        nts.uk.ui.block.clear();
                    });
                }
            });
        }

        selectDisplayItem() {
            var self = this;
            nts.uk.ui.windows.setShared("selectedPerfFmtCodeList", self.formatCodes());
            nts.uk.ui.windows.sub.modal("/view/kdw/003/c/index.xhtml").onClosed(() => {
                var dataTemp = nts.uk.ui.windows.getShared('dailyPerfFmtList');
                if (dataTemp != undefined) {
                    let data = [dataTemp.dailyPerformanceFormatCode()];
                    if (self.displayFormat() == 0) {
                        $("#emp-component").css("display", "block");
                        $("#cbListDate").css("display", "none");
                    } else if (self.displayFormat() == 1) {
                        $("#cbListDate").css("display", "block");
                        $("#emp-component").css("display", "none");
                    } else {
                        $("#cbListDate").css("display", "none");
                        $("#emp-component").css("display", "none");
                    }
                    let lstEmployee = [];
                    if (self.displayFormat() === 0) {
                        lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                            return employee.id === self.selectedEmployee();
                        }));
                    } else {
                        lstEmployee = self.lstEmployee();
                    }
                    let param = {
                        dateRange: {
                            startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                            endDate: moment(self.dateRanger().endDate).utc().toISOString()
                        },
                        lstEmployee: lstEmployee,
                        formatCodes: data
                    };
                    nts.uk.ui.block.invisible();
                    nts.uk.ui.block.grayout();
                    service.selectFormatCode(param).done((data) => {
                        _.each(data.lstControlDisplayItem.lstHeader, function(item) {
                            if (item.group.length > 0) {
                                delete item.group[0].group;
                                if (item.group[0].dataType == "String") {
                                    item.group[0].onChange = self.search;
                                    delete item.group[0].ntsControl;
                                } else {
                                    delete item.group[0].onChange;
                                    delete item.group[0].ntsControl;
                                }
                                delete item.group[1].group;
                            } else {
                                delete item.group;
                            }
                        });
                        _.each(data.lstFixedHeader, function(item) {
                            delete item.group;
                        });
                        self.receiveData(data);
                        self.extraction();
                        nts.uk.ui.block.clear();
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alert(error.message);
                        nts.uk.ui.block.clear();
                    });
                }
            });
        }

        referencesActualResult() {
            var self = this;
            let lstEmployee = [];
            if (self.displayFormat() === 0) {
                lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                }));
                nts.uk.ui.windows.setShared("kdl014startDateA", moment(self.dateRanger().startDate).utc().toISOString());
                nts.uk.ui.windows.setShared("kdl014endDateA", moment(self.dateRanger().startDate).utc().toISOString());
                nts.uk.ui.windows.setShared("kdl014employeeCodeA", lstEmployee[0].code);
                nts.uk.ui.windows.sub.modal("/view/kdl/014/a/index.xhtml").onClosed(() => {
                });

            } else if (self.displayFormat() === 1) {
                lstEmployee = self.lstEmployee().map((data) => {
                    return data.code;
                });
                nts.uk.ui.windows.setShared("kdl014startDateB", moment(self.dateRanger().startDate).utc().toISOString());
                nts.uk.ui.windows.setShared("kdl014endDateB", moment(self.dateRanger().startDate).utc().toISOString());
                nts.uk.ui.windows.setShared("kdl014lstEmployeeB", lstEmployee);
                nts.uk.ui.windows.sub.modal("/view/kdl/014/b/index.xhtml").onClosed(() => {
                });
            }
        }

        employmentOk() {
            let _self = this;
            nts.uk.ui.block.grayout();
            nts.uk.ui.windows.sub.modal("/view/kdl/006/a/index.xhtml").onClosed(() => {
                nts.uk.ui.block.clear();
            });
        }
        btnSetting_Click() {
            var container = $("#setting-content");
            if (container.css("visibility") === 'hidden') {
                container.css("visibility", "visible");
                container.css("top", "0px");
                container.css("left", "0px");
            }
            $(document).mouseup(function(e) {
                // if the target of the click isn't the container nor a descendant of the container
                if (!container.is(e.target) && container.has(e.target).length === 0) {
                    container.css("visibility", "hidden");
                    container.css("top", "-9999px");
                    container.css("left", "-9999px");
                }
            });
        }

        btnVacationRemaining_Click() {
            var container = $("#vacationRemaining-content");
            if (container.css("visibility") === 'hidden') {
                container.css("visibility", "visible");
                container.css("top", "0px");
                container.css("left", "0px");
            }
            $(document).mouseup(function(e) {
                // if the target of the click isn't the container nor a descendant of the container
                if (!container.is(e.target) && container.has(e.target).length === 0) {
                    container.css("visibility", "hidden");
                    container.css("top", "-9999px");
                    container.css("left", "-99990px");
                }
            });
        }

        btnSaveColumnWidth_Click() {
            var self = this;
            let command = {
                lstHeader: {}
            };
            _.forEach(self.headersGrid(), (header) => {
                if (nts.uk.ntsNumber.isNumber(header.key)) {
                    command.lstHeader[header.key] = header.width.split("px")[0];
                }
            });
            service.saveColumnWidth(command);
        }

        extractionData() {
            var self = this;
            self.headersGrid([]);
            self.fixColGrid([]);
            if (self.displayFormat() == 0) {
                self.fixColGrid(self.employeeModeFixCol);
            } else if (self.displayFormat() == 1) {
                self.fixColGrid(self.dateModeFixCol);
            } else if (self.displayFormat() == 2) {
                self.fixColGrid(self.errorModeFixCol);
            }
            self.loadHeader(self.displayFormat());
            self.dailyPerfomanceData(self.filterData(self.displayFormat()));
        }

        extraction() {
            var self = this;
            $("#dpGrid").ntsGrid("destroy");
            self.extractionData();
            self.loadGrid();
        }

        isDisableRow(id) {
            var self = this;
            for (let i = 0; i < self.rowStates().length; i++) {
                return self.rowStates()[i].rowId == id;
            }
        }

        isDisableSign(id) {
            var self = this;
            for (let i = 0; i < self.cellStates().length; i++) {
                return self.cellStates()[i].rowId == id && self.cellStates()[i].columnKey == 'sign';
            }
        }

        signAll() {
            var self = this;
            _.forEach(self.dailyPerfomanceData(), (data) => {
                if (!self.isDisableRow(data.id)) {
                    data.sign = !data.sign;
                }
            });
            self.dailyPerfomanceData.valueHasMutated();
            $("#grid2").ntsGrid("destroy");
            self.loadGrid();
        }

        setColorWeekend() {
            var self = this;
            self.textColors([]);
            _.forEach(self.dailyPerfomanceData(), (data) => {
                if (moment(data.date, "YYYY/MM/DD").day() == 6) {
                    self.textColors.push({
                        rowId: "_" + data.id,
                        columnKey: 'date',
                        color: '#4F81BD'
                    });
                } else if (moment(data.date, "YYYY/MM/DD").day() == 0) {
                    self.textColors.push({
                        rowId: "_" + data.id,
                        columnKey: 'date',
                        color: '#e51010'
                    });
                }
            });
        }

        setHeaderColor() {
            var self = this;
            self.headerColors([]);
            _.forEach(self.headersGrid(), (header) => {
                if (header.color) {
                    self.headerColors.push({
                        key: header.key,
                        color: header.color
                    });
                }
                if (header.group != undefined && header.group.length > 0) {
                    self.headerColors.push({
                        key: header.group[0].key,
                        color: header.group[0].color
                    });
                    self.headerColors.push({
                        key: header.group[1].key,
                        color: header.group[1].color
                    });
                }
            });
        }

        formatDate(lstData) {
            var self = this;
            return lstData.map((data) => {
                var object = {
                    id: "_" + data.id,
                    state: data.state,
                    error: data.error,
                    date: moment(data.date, "YYYY/MM/DD").format("MM/DD(dd)"),
                    sign: data.sign,
                    employeeId: data.employeeId,
                    employeeCode: data.employeeCode,
                    employeeName: data.employeeName
                }
                _.each(data.cellDatas, function(item) { object[item.columnKey] = item.value });
                return object;
            });
        }

        filterData(mode: number) {
            var self = this;
            if (mode == 0) {
                return _.filter(self.dpData, (data) => { return data.employeeId == self.selectedEmployee() });
            } else if (mode == 1) {
                return _.filter(self.dpData, (data) => { return data.date === moment(self.selectedDate()).format('YYYY/MM/DD') });
            } else if (mode == 2) {
                return _.filter(self.dpData, (data) => { return data.error !== '' });
            }
        }

        //load kcp009 component: employee picker
        loadKcp009() {
            let self = this;
            var kcp009Options = {
                systemReference: 1,
                isDisplayOrganizationName: true,
                employeeInputList: self.lstEmployee,
                targetBtnText: nts.uk.resource.getText("KCP009_3"),
                selectedItem: self.selectedEmployee,
                tabIndex: 1
            };
            // Load listComponent
            $('#emp-component').ntsLoadListComponent(kcp009Options);
        }

        //init ccg001
        initCcg001() {
            let self = this;
            self.ccg001 = {
                baseDate: self.baseDate,
                //Show/hide options
                isQuickSearchTab: true,
                isAdvancedSearchTab: true,
                isAllReferableEmployee: true,
                isOnlyMe: true,
                isEmployeeOfWorkplace: true,
                isEmployeeWorkplaceFollow: true,
                isMutipleCheck: true,
                isSelectAllEmployee: false,
                /**
                * @param dataList: list employee returned from component.
                * Define how to use this list employee by yourself in the function's body.
                */
                onSearchAllClicked: function(dataList: EmployeeSearchDto[]) {
                    self.lstEmployee(dataList.map((data: EmployeeSearchDto) => {
                        return {
                            id: data.employeeId,
                            code: data.employeeCode,
                            businessName: data.employeeName,
                            workplaceName: data.workplaceName,
                            depName: '',
                            isLoginUser: false
                        };
                    }));
                    self.lstEmployee(_.orderBy(self.lstEmployee(), ['code'], ['asc']));
                    self.selectedEmployee(self.lstEmployee()[0].id);
                    self.loadKcp009();
                    self.btnExtraction_Click();
                },
                onSearchOnlyClicked: function(data: EmployeeSearchDto) {
                    self.lstEmployee([]);
                    self.lstEmployee.push({
                        id: data.employeeId,
                        code: data.employeeCode,
                        businessName: data.employeeName,
                        workplaceName: data.workplaceName,
                        depName: '',
                        isLoginUser: false
                    });
                    self.lstEmployee(_.orderBy(self.lstEmployee(), ['code'], ['asc']));
                    self.selectedEmployee(self.lstEmployee()[0].id);
                    self.loadKcp009();
                    self.btnExtraction_Click();
                },
                onSearchOfWorkplaceClicked: function(dataList: EmployeeSearchDto[]) {
                    self.lstEmployee(dataList.map((data: EmployeeSearchDto) => {
                        return {
                            id: data.employeeId,
                            code: data.employeeCode,
                            businessName: data.employeeName,
                            workplaceName: data.workplaceName,
                            depName: '',
                            isLoginUser: false
                        };
                    }));
                    self.lstEmployee(_.orderBy(self.lstEmployee(), ['code'], ['asc']));
                    self.selectedEmployee(self.lstEmployee()[0].id);
                    self.loadKcp009();
                    self.btnExtraction_Click();
                },
                onSearchWorkplaceChildClicked: function(dataList: EmployeeSearchDto[]) {
                    self.lstEmployee(dataList.map((data: EmployeeSearchDto) => {
                        return {
                            id: data.employeeId,
                            code: data.employeeCode,
                            businessName: data.employeeName,
                            workplaceName: data.workplaceName,
                            depName: '',
                            isLoginUser: false
                        };
                    }));
                    self.lstEmployee(_.orderBy(self.lstEmployee(), ['code'], ['asc']));
                    self.selectedEmployee(self.lstEmployee()[0].id);
                    self.loadKcp009();
                    self.btnExtraction_Click();
                },
                onApplyEmployee: function(dataList: EmployeeSearchDto[]) {
                    self.lstEmployee(dataList.map((data: EmployeeSearchDto) => {
                        return {
                            id: data.employeeId,
                            code: data.employeeCode,
                            businessName: data.employeeName,
                            workplaceName: data.workplaceName,
                            depName: '',
                            isLoginUser: false
                        };
                    }));
                    self.selectedEmployee(self.lstEmployee()[self.lstEmployee().length - 1].id);
                    self.loadKcp009();
                    self.btnExtraction_Click();
                }
            }
        }

        //load ccg001 component: search employee
        loadCcg001() {
            var self = this;
            $('#ccg001').ntsGroupComponent(self.ccg001);
        }

        loadGrid() {
            var self = this;
            var summary: ISummaryColumn = {
                columnKey: 'salary',
                allowSummaries: true
            }
            var summaries = [];
            var rowState = {
                rowId: 0,
                disable: true
            }
            self.setHeaderColor();
            self.setColorWeekend();
            console.log(self.formatDate(self.dailyPerfomanceData()));
            $("#dpGrid").ntsGrid({
                width: (window.screen.availWidth - 200) + "px",
                height: '500px',
                dataSource: self.formatDate(self.dailyPerfomanceData()),
                primaryKey: 'id',
                rowVirtualization: true,
                virtualization: true,
                virtualizationMode: 'continuous',
                enter: self.selectedDirection() == 0 ? 'below' : 'right',
                autoFitWindow: false,
                preventEditInError: false,
                avgRowHeight: 20,
                autoAdjustHeight: true,
                adjustVirtualHeights: true,
                columns: self.headersGrid(),
                hidePrimaryKey: true,
                features: [
                    { name: 'Paging', pageSize: 31, currentPageIndex: 0 },
                    { name: 'ColumnFixing', fixingDirection: 'left', showFixButtons: false, columnSettings: self.fixColGrid() },
                    { name: 'Resizing', columnSettings: [{ columnKey: 'id', allowResizing: false, minimumWidth: 0 }] },
                    { name: 'MultiColumnHeaders' }
                ],
                ntsFeatures: self.createNtsFeatures(),
                ntsControls: [
                    { name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                    { name: 'Image', source: 'ui-icon ui-icon-locked', controlType: 'Image' },
                    {
                        name: 'Link2',
                        click: function(ui, evt) {
                            alert('Do something12344:' + ui);
                            var rowId;
                            var rows = $("#dpGrid").igGridSelection("selectedRows");
                            $.each(rows, function(ix, el) {
                                rowId = el.element.attr("data-id");
                                var cellValue = $("#dpGrid").igGrid("getCellValue", rowId, "ContractID");
                                alert(cellValue);
                            });
                        },
                        controlType: 'LinkLabel'
                    },
                    { name: 'TextEditorNumberSeparated', controlType: 'TextEditor', constraint: { valueType: 'Integer', required: true, format: "Number_Separated" } },
                    { name: 'TextEditorTimeShortHM', controlType: 'TextEditor', constraint: { valueType: 'Time', required: true, format: "Time_Short_HM" } },
                    { name: 'Combobox2', options: self.comboItems(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumns(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true },
                ]
            });
        }

        reloadGrid() {
            var self = this;
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            self.extraction();
            nts.uk.ui.block.clear();
        }

        createNtsFeatures() {
            var self = this;
            let lstNtsFeature = [
                { name: 'CopyPaste' },
                { name: 'CellEdit' },
                { name: 'CellState', rowId: 'rowId', columnKey: 'columnKey', state: 'state', states: self.cellStates() },
                { name: 'RowState', rows: self.rowStates() },
                { name: 'TextColor', rowId: 'rowId', columnKey: 'columnKey', color: 'color', colorsTable: self.textColors() },
                { name: 'HeaderStyles', columns: self.headerColors() }
            ];
            if (self.sheetsGrid().length > 0) {
                lstNtsFeature.push({
                    name: "Sheet",
                    initialDisplay: self.sheetsGrid()[0].name,
                    sheets: self.sheetsGrid()
                });
            }
            return lstNtsFeature;
        }

        loadHeader(mode) {
            var self = this;
            let tempList = [];
            if (mode == 0) {
                _.forEach(self.employeeModeHeader, (header) => {
                    tempList.push(header);
                });
            } else if (mode == 1) {
                self.displayProfileIcon();
                _.forEach(self.dateModeHeader, (header) => {
                    tempList.push(header);
                });
            } else if (mode == 2) {
                _.forEach(self.errorModeHeader, (header) => {
                    tempList.push(header);
                });
            }
            self.dislayNumberHeaderText();
            _.forEach(self.optionalHeader, (header) => {
                tempList.push(header);
            });
            self.headersGrid(tempList);
        }

        displayProfileIcon() {
            var self = this;
            if (self.showProfileIcon()) {
                _.remove(self.dateModeHeader, function(header) {
                    return header.key === "picture-person";
                });
                _.remove(self.dateModeFixCol, function(header) {
                    return header.columnKey === "picture-person";
                });
                self.dateModeHeader.splice(5, 0, LIST_FIX_HEADER[7]);
                self.dateModeFixCol.push({ columnKey: 'picture-person', isFixed: true });
            } else {
                _.remove(self.dateModeHeader, function(header) {
                    return header.key === "picture-person";
                });
                _.remove(self.dateModeFixCol, function(header) {
                    return header.columnKey === "picture-person";
                });
            }
        }

        dislayNumberHeaderText() {
            var self = this;
            if (self.showHeaderNumber()) {
                self.optionalHeader.map((header) => {
                    if (header.headerText) {
                        header.headerText = header.headerText + " " + header.key;
                    }
                    return header;
                });
            } else {
                self.optionalHeader.map((header) => {
                    if (header.headerText) {
                        header.headerText = header.headerText.split(" ")[0];
                    }
                    return header;
                });
            }
        }
        search(val) {
            let dfd = $.Deferred();
            let i = 0;
            let result = "Not found";
            while (i < 500) {
                i++;
            }
            if (val === "001") {
                result = "結果01";
            } else if (val === "002") {
                result = "結果02"
            }
            dfd.resolve(result);
            return dfd.promise();
        }
    }
    export class AuthorityDetailModel {
        available1: KnockoutObservable<boolean> = ko.observable(true);
        available4: KnockoutObservable<boolean> = ko.observable(true);
        available2: KnockoutObservable<boolean> = ko.observable(true);
        available3: KnockoutObservable<boolean> = ko.observable(true);
        available5: KnockoutObservable<boolean> = ko.observable(true);
        available8: KnockoutObservable<boolean> = ko.observable(true);
        available22: KnockoutObservable<boolean> = ko.observable(true);
        available24: KnockoutObservable<boolean> = ko.observable(true);
        available7: KnockoutObservable<boolean> = ko.observable(true);
        available23: KnockoutObservable<boolean> = ko.observable(true);
        available25: KnockoutObservable<boolean> = ko.observable(true);
        constructor(data: Array<DailyPerformanceAuthorityDto>) {
            var self = this;
            if (!data) return;
            this.available1(self.checkAvailable(data, 1));
            this.available4(self.checkAvailable(data, 4));
            this.available2(self.checkAvailable(data, 2));
            this.available3(self.checkAvailable(data, 3));
            this.available5(self.checkAvailable(data, 5));
            this.available8(self.checkAvailable(data, 8));
            this.available22(self.checkAvailable(data, 22));
            this.available24(self.checkAvailable(data, 24));
            this.available7(self.checkAvailable(data, 7));
            this.available23(self.checkAvailable(data, 23));
            this.available25(self.checkAvailable(data, 25));

        }
        checkAvailable(data: Array<DailyPerformanceAuthorityDto>, value: number): boolean {
            let self = this;
            var check = _.find(data, function(o) {
                return o.functionNo === value;
            })
            if (check == null) return true;
            else return check.availability;
        };
    }
    class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    export interface DailyPerformanceAuthorityDto {
        isDefaultInitial: number;
        roleID: string;
        functionNo: number;
        availability: boolean;
    }
}