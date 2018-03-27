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
        columnSettings: KnockoutObservableArray<any> = ko.observableArray([]);
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
        lstAttendanceItem: KnockoutObservableArray<any> = ko.observableArray([]);
        //A13_1 コメント
        comment: KnockoutObservable<any> = ko.observable(null);
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

        referenceVacation: KnockoutObservable<ReferenceVacation> = ko.observable(null);

        employmentCode: KnockoutObservable<any> = ko.observable("");

        editValue: KnockoutObservableArray<InfoCellEdit> = ko.observableArray([]);
        
        itemValueAll: KnockoutObservableArray<any> = ko.observableArray([]);
        
        itemValueAllTemp: KnockoutObservableArray<any> = ko.observableArray([]);
        
        lockMessage: KnockoutObservable<any> = ko.observable("");

        dataHoliday: KnockoutObservable<DataHoliday> =  ko.observable(new DataHoliday("0","0","0","0","0","0"));
        comboItems: KnockoutObservableArray<any> = ko.observableArray([new ItemModel('1', '基本給'),
            new ItemModel('2', '役職手当'),
            new ItemModel('3', '基本給2')]);

        comboColumns: KnockoutObservableArray<any> = ko.observableArray([{ prop: 'code', length: 1 },
            { prop: 'name', length: 2 }]);
        comboColumnsCalc: KnockoutObservableArray<any> = ko.observableArray([{ prop: 'code', length: 1 },
            { prop: 'name', length: 8 }]);
        comboItemsDoWork: KnockoutObservableArray<any> = ko.observableArray([]);
        comboItemsReason: KnockoutObservableArray<any> = ko.observableArray([]);
        comboItemsCalc: KnockoutObservableArray<any> = ko.observableArray([]);
        showPrincipal: KnockoutObservable<any> = ko.observable(true);
        showSupervisor: KnockoutObservable<any> = ko.observable(true);
        dataAll: KnockoutObservable<any> = ko.observable(null);
        hasLstHeader : boolean  =  true;
        dPErrorDto: KnockoutObservable<any> = ko.observable();
        listCareError: KnockoutObservableArray<any> = ko.observableArray([]);
        listCareInputError: KnockoutObservableArray<any> = ko.observableArray([]);
        listCheckHolidays:  KnockoutObservableArray<any> = ko.observableArray([]);
        employIdLogin: any;
        dialogShow: any;
        //contain data share
        screenModeApproval: KnockoutObservable<any> = ko.observable(null);
        changePeriod: KnockoutObservable<any> = ko.observable(true);
        errorReference: KnockoutObservable<any> = ko.observable(true);
        activationSourceRefer: KnockoutObservable<any> = ko.observable(null);
        tighten: KnockoutObservable<any> = ko.observable(null);
        //button A2_6
        showTighProcess: KnockoutObservable<any> = ko.observable(true);
        //get object share
        shareObject: KnockoutObservable<ShareObject> = ko.observable(new ShareObject());

        constructor(dataShare:any) {
            var self = this;
            self.initLegendButton();
            self.initDateRanger();
            self.initDisplayFormat();
            self.headersGrid = ko.observableArray(self.employeeModeHeader);
            self.fixColGrid = ko.observableArray(self.employeeModeFixCol);
            // show/hide header number
            self.showHeaderNumber.subscribe((val) => {
                let headerText
                _.each(self.optionalHeader, header => {
                    if (header.headerText != "提出済みの申請" && header.headerText != "申請") {
                        if (header.group == undefined && header.group == null) {
                            if (self.showHeaderNumber()) {
                                headerText = header.headerText + " " + header.key.substring(1, header.key.length);
                                 $("#dpGrid").ntsGrid("headerText", header.key, headerText, false);
                            } else {
                                headerText = header.headerText.split(" ")[0];
                                 $("#dpGrid").ntsGrid("headerText", header.key, headerText, false);
                            }
                        } else {
                            if (self.showHeaderNumber()) {
                                headerText = header.headerText + " " + header.group[1].key.substring(4, header.group[1].key.length);
                                 $("#dpGrid").ntsGrid("headerText", header.headerText, headerText, true);
                            } else {
                                headerText = header.headerText.split(" ")[0];
                                 $("#dpGrid").ntsGrid("headerText", header.headerText, headerText, true);
                            }
                        }
                    }
                });
            });
            // show/hide profile icon
            self.showProfileIcon.subscribe((val) => {
                self.reloadGrid();
            });
            
            self.displayWhenZero.subscribe(val =>{
                let dataSource = $("#dpGrid").igGrid("option", "dataSource");
                let dataTemp = [];
                if (self.displayWhenZero()) {
                    _.each(dataSource, data => {
                        var dtt: any = {};
                        _.each(data, (val, indx) => {
                            if (String(val) == "0" || String(val) == "0:00") {
                                dtt[indx] = "";
                            } else {
                                dtt[indx] = val;
                            }
                        });
                        dataTemp.push(dtt);
                    });
                    $("#dpGrid").igGrid("option", "dataSource", dataTemp);
                } else {
                   let dataSourceOld : any = self.formatDate(self.dailyPerfomanceData());
                   let dataChange: any = $("#dpGrid").ntsGrid("updatedCells");
                   let group : any = _.groupBy(dataChange, "rowId");
                    _.each(dataSourceOld, data => {
                        var dtt: any = {};
                        if (group[data.id]) {
                             dtt = data;
                            _.each(group[data.id], val => {
                                dtt[val.columnKey] = val.value;
                            });
                            dataTemp.push(dtt);
                        } else {
                            dataTemp.push(data);
                        }
                    });
                    $("#dpGrid").igGrid("option", "dataSource", dataTemp);
                }
            });
            //$("#fixed-table").ntsFixedTable({ height: 50, width: 300 });
            $(document).mouseup(function(e) {
                var container = $(".ui-tooltip");
                if (!container.is(e.target) &&
                    container.has(e.target).length === 0) {
                     $("#tooltip").hide();
                }
            });
            if (dataShare != undefined) {
                self.shareObject().mapDataShare(dataShare.initParam, dataShare.extractionParam);
            }
        }
         helps(event, data){
             var self = this;
             $('#tooltip').css({
                 'left': event.pageX+15,
                 'top':  event.pageY-12,
                 'display': 'none',
                 'position': 'fixed',
             });
             self.lockMessage(data);
             $("#tooltip").show();
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
                    if (moment(elementDate, "YYYY/MM/DD").isValid()) {
                        while (!moment(elementDate, "YYYY/MM/DD").isAfter(dateRange.endDate)) {
                            self.lstDate.push({ date: elementDate });
                            elementDate = moment(elementDate, "YYYY/MM/DD").add(1, 'd').format("YYYY/MM/DD");
                        }
                }
                }
            });
            self.dateRanger({
                startDate: moment().add(-1, "M").add(1 ,"d").format("YYYY/MM/DD"),
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
        createSumColumn(data : any){
            var self = this;
            _.each(data.lstControlDisplayItem.columnSettings, function(item) {
                if (self.displayFormat() == 0) {
                    if (item.columnKey == "date") {
                        item.allowSummaries = true;
                        item['summaryOperands'] = [{ type: "custom", order: 0, summaryCalculator: function() { return "合計"; } }];
                    }
                } else {
                    if (item.columnKey == "employeeCode") {
                        item.allowSummaries = true;
                        item['summaryOperands'] = [{ type: "custom", order: 0, summaryCalculator: function() { return "合計"; } }];
                    }
                }
                if (item.typeFormat !=  null && item.typeFormat != undefined) {
                    if (item.typeFormat == 2) {
                        //so lan
                        item.allowSummaries = true;
                        item['summaryOperands'] = [{
                            rowDisplayLabel: "合計",
                            type: "custom",
                            summaryCalculator: $.proxy(self.totalNumber, this),
                            order: 0
                        }]
                    }
                    else if (item.typeFormat == 5) {
                        //thoi gian
                        item.allowSummaries = true;
                        item['summaryOperands'] = [{
                            rowDisplayLabel: "合計",
                            type: "custom",
                            summaryCalculator: $.proxy(self.totalTime, this),
                            order: 0
                        }]
                    }
                    else if (item.typeFormat == 3) {
                        //so tien 
                        item.allowSummaries = true;
                        item['summaryOperands'] = [{
                            rowDisplayLabel: "合計",
                            type: "custom",
                            summaryCalculator: $.proxy(self.totalMoney, this),
                            order: 0
                        }]
                    }
                }
                delete item.typeFormat;
                self.columnSettings(data.lstControlDisplayItem.columnSettings);
            });
        }
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            let dateRangeParam = nts.uk.ui.windows.getShared('DateRangeKDW003');
            var param = {
                dateRange: dateRangeParam? {
                    startDate: moment(dateRangeParam.startDate).utc().toISOString(),
                    endDate: moment(dateRangeParam.endDate).utc().toISOString()
                }: null,
                displayFormat : 0,
                initScreen: 0,
                lstEmployee: [],
                formatCodes: self.formatCodes(),
                objectShare: self.shareObject()
            };
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            service.startScreen(param).done((data) => {
                console.log(data);
                if (data.typeBussiness != localStorage.getItem('kdw003_type')) {
                    localStorage.removeItem(window.location.href + '/dpGrid');
                }
                localStorage.setItem('kdw003_type', data.typeBussiness);
                self.dateRanger().startDate = data.dateRange.startDate;
                self.dateRanger().endDate = data.dateRange.endDate;
                self.dateRanger.valueHasMutated();
                self.dataAll(data);
                self.itemValueAll(data.itemValues);
                self.comment(data.comment != null ? '■ ' + data.comment : null);
                self.formatCodes(data.lstControlDisplayItem.formatCode);
                self.createSumColumn(data);
                // combo box
                self.comboItemsCalc(data.lstControlDisplayItem.comboItemCalc);
                self.comboItemsReason(data.lstControlDisplayItem.comboItemReason);
                self.comboItemsDoWork(data.lstControlDisplayItem.comboItemDoWork);
                
                self.employmentCode(data.employmentCode);
                self.lstAttendanceItem(data.lstControlDisplayItem.lstAttendanceItem);
                self.showButton = ko.observable(new AuthorityDetailModel(data.authorityDto, data.lstControlDisplayItem.settingUnit));
                self.referenceVacation(new ReferenceVacation(data.yearHolidaySettingDto == null ? false : data.yearHolidaySettingDto.manageAtr, data.substVacationDto == null ? false : data.substVacationDto.manageAtr, data.compensLeaveComDto == null ? false : data.compensLeaveComDto.manageAtr, data.com60HVacationDto == null ? false : data.com60HVacationDto.manageAtr, self.showButton()));
                self.showTighProcess(data.identityProcessDto.useConfirmByYourself);
                // Fixed Header
                self.fixHeaders(data.lstFixedHeader);
                self.showPrincipal(data.showPrincipal);
                self.showSupervisor(data.showSupervisor);
                if(data.lstControlDisplayItem.lstHeader.length == 0) self.hasLstHeader = false;
                if (self.showPrincipal() || data.lstControlDisplayItem.lstHeader.length == 0) {
                    self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3], self.fixHeaders()[4]];
                    self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[4]];
                    self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[4]];
                } else {
                    self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3]];
                    self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6]];
                    self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3]];
                }
                if(self.showSupervisor()){
                    self.employeeModeHeader.push(self.fixHeaders()[8]);
                    self.dateModeHeader.push(self.fixHeaders()[8]);
                    self.errorModeHeader.push(self.fixHeaders()[8]);
                }
                self.lstEmployee(_.orderBy(data.lstEmployee, ['code'], ['asc']));
                self.receiveData(data);
                let employeeLogin: any = _.find(self.lstEmployee(), function(data){
                    return data.loginUser == true;
                });
                self.employIdLogin = employeeLogin;
                self.selectedEmployee(employeeLogin.id);
                self.extractionData();
                self.loadGrid();
                //  self.extraction();
                self.initCcg001();
                self.loadCcg001();
                // no20
                self.dPErrorDto(data.dperrorDto);
                nts.uk.ui.block.clear();
                dfd.resolve();
            }).fail(function(error) {
                if (error.messageId == "KDW/003/a") {
                    //self.selectDisplayItem();
                nts.uk.ui.windows.setShared("selectedPerfFmtCodeList", "");
                nts.uk.ui.windows.sub.modal("/view/kdw/003/c/index.xhtml").onClosed(() => {
                    var dataTemp = nts.uk.ui.windows.getShared('dailyPerfFmtList');
                    if (dataTemp != undefined) {
                        let data = [dataTemp.dailyPerformanceFormatCode()];
                        
                        let param = {
                            dateRange: {
                                startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                                endDate: moment(self.dateRanger().endDate).utc().toISOString()
                            },
                            lstEmployee: [],
                            formatCodes: data
                        };
                        nts.uk.ui.block.invisible();
                        nts.uk.ui.block.grayout();
                        service.selectFormatCode(param).done((data) => {
                            self.formatCodes(data.lstControlDisplayItem.formatCode);
                            self.createSumColumn(data);
                            self.employmentCode(data.employmentCode);
                            self.lstAttendanceItem(data.lstControlDisplayItem.lstAttendanceItem);
                            self.showButton(new AuthorityDetailModel(data.authorityDto, data.lstControlDisplayItem.settingUnit));
                            self.referenceVacation(new ReferenceVacation(data.yearHolidaySettingDto == null ? false : data.yearHolidaySettingDto.manageAtr, data.substVacationDto == null ? false : data.substVacationDto.manageAtr, data.compensLeaveComDto == null ? false : data.compensLeaveComDto.manageAtr, data.com60HVacationDto == null ? false : data.com60HVacationDto.manageAtr, self.showButton()));
                            // Fixed Header
                            self.fixHeaders(data.lstFixedHeader);
                            self.showPrincipal(data.showPrincipal);
                            self.showSupervisor(data.showSupervisor);
                            if (data.showPrincipal) {
                                self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3], self.fixHeaders()[4]];
                                self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[4]];
                                self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[4]];
                            } else {
                                self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3]];
                                self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6]];
                                self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3]];
                            }
                            if (self.showSupervisor()) {
                                self.employeeModeHeader.push(self.fixHeaders()[8]);
                                self.dateModeHeader.push(self.fixHeaders()[8]);
                                self.errorModeHeader.push(self.fixHeaders()[8]);
                            }
                            self.lstEmployee(_.orderBy(data.lstEmployee, ['code'], ['asc']));
                            self.receiveData(data);
                            let employeeLogin: any = _.find(self.lstEmployee(), function(data) {
                                return data.loginUser == true;
                            });
                            self.selectedEmployee(employeeLogin.id);
                            self.extractionData();
                            self.loadGrid();
                            //  self.extraction();
                            self.initCcg001();
                            self.loadCcg001();
                            // no20
                            self.dPErrorDto(data.dperrorDto);
                            nts.uk.ui.block.clear();
                            dfd.resolve();
                        }).fail(function(error) {
                            nts.uk.ui.dialog.alert(error.message);
                            nts.uk.ui.block.clear();
                        });
                    }
                });
                }
                else {
                    nts.uk.ui.dialog.alert(error.message);
                }
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
        
        proceed() {
            var self = this;
            this.insertUpdate();
        }
        
        proceedSave() {
            var self = this;     
            this.insertUpdate();
        }
        
        insertUpdate(){
           var self = this;
            if (self.dialogShow != undefined && self.dialogShow.$dialog != null) {
                self.dialogShow.close();
            }
            let errorGrid: any = $("#dpGrid").ntsGrid("errors");
            let checkDataCare: boolean = true;
            if (errorGrid == undefined || errorGrid.length == 0) {
                nts.uk.ui.block.invisible();
                nts.uk.ui.block.grayout();
                self.listCareError([]);
                self.listCareInputError([]);
                self.listCheckHolidays([]);
                let dataChange: any = $("#dpGrid").ntsGrid("updatedCells");
                var dataSource = $("#dpGrid").igGrid("option", "dataSource");
                let dataChangeProcess: any = [];
                let dataCheckSign: any = [];
                let dataCheckApproval: any = [];
                _.each(dataChange, (data: any) => {
                    let dataTemp = _.find(dataSource, (item: any) => {
                        return item.id == data.rowId;
                    });
                     if (data.columnKey != "sign" && data.columnKey != "approval") {
                        if (data.columnKey.indexOf("Code") == -1 && data.columnKey.indexOf("NO") == -1) {
                            if (data.columnKey.indexOf("Name") != -1) {
                                // todo
                            } else {

                                // check itemCare 
                                let groupCare = self.checkItemCare(Number(data.columnKey.substring(1, data.columnKey.length)));
                                if (groupCare == 0 || groupCare == 1) {
                                    if (self.checkErrorData(groupCare, data, dataSource) == false || self.listCareInputError().length >0) {
                                        checkDataCare = false;
                                    }
                                }
                                //get layout , and type
                                let layoutAndType: any = _.find(self.itemValueAll(), (item: any) => {
                                    return item.itemId == data.columnKey.substring(1, data.columnKey.length);
                                });
                                let item = _.find(self.lstAttendanceItem(), (value) => {
                                    return String(value.id) === data.columnKey.substring(1, data.columnKey.length);
                                })
                                let value: any;
                                value = self.getPrimitiveValue(data.value, item.attendanceAtr);
                                let dataMap = new InfoCellEdit(data.rowId, data.columnKey.substring(1, data.columnKey.length), value, layoutAndType == undefined ? "" : layoutAndType.valueType, layoutAndType == undefined ? "" : layoutAndType.layoutCode, dataTemp.employeeId, dataTemp.dateDetail.utc().toISOString(), 0);
                                dataChangeProcess.push(dataMap);
                            }
                        } else {
                            let columnKey: any;
                            let item: any;
                            if (data.columnKey.indexOf("Code") != -1) {
                                columnKey = data.columnKey.substring(4, data.columnKey.length);
                            } else {
                                columnKey = data.columnKey.substring(2, data.columnKey.length);
                            }
                            //TO Thanh: move find logic out if condition
                            item = _.find(self.lstAttendanceItem(), (data) => {
                                return String(data.id) === columnKey;
                            })

                            let layoutAndType: any = _.find(self.itemValueAll(), (item: any) => {
                                return item.itemId == columnKey;
                            });
                            let dataMap = new InfoCellEdit(data.rowId, columnKey, String(data.value), layoutAndType.valueType, layoutAndType.layoutCode, dataTemp.employeeId, dataTemp.dateDetail.utc().toISOString(), item.typeGroup);
                            dataChangeProcess.push(dataMap);
                        }
                    }else{
                       if(data.columnKey == "sign"){
                            dataCheckSign.push({rowId: data.rowId, itemId: "sign", value: data.value,employeeId: dataTemp.employeeId, date: dataTemp.dateDetail.utc().toISOString()});
                       }else{
                            dataCheckApproval.push({rowId: data.rowId, itemId: "approval", value: data.value,employeeId: dataTemp.employeeId, date: dataTemp.dateDetail.utc().toISOString()});
                       } 
                    }
                });
                
                let dataParent = { itemValues: dataChangeProcess, dataCheckSign : dataCheckSign, dataCheckApproval: dataCheckApproval, mode: self.displayFormat()}
                if(self.displayFormat() ==0){
                    dataParent["employeeId"] = dataSource[0].employeeId;
                    dataParent["dateRange"] = {startDate: dataSource[0].dateDetail, endDate: dataSource[dataSource.length -1].dateDetail}
                }
                if ((dataChangeProcess.length > 0  || dataCheckSign.length > 0 || dataCheckApproval.length > 0) && checkDataCare) {
                    let dfd = $.Deferred();
                    service.addAndUpdate(dataParent).done((data) => {
                        // alert("done");
                        dataChange = {};
                        if (_.isEmpty(data)) {
                            self.btnExtraction_Click();
                        } else {
                            nts.uk.ui.block.clear();
                            if (data[0] != undefined) {
                                self.listCareError(data[0])
                               // nts.uk.ui.dialog.alertError({ messageId: "Msg_996" })
                            } else if (data[1] != undefined){
                                self.listCareInputError(data[1])
                               // nts.uk.ui.dialog.alertError({ messageId: "Msg_1108" })
                            } else if (data[2] != undefined){
                                self.listCheckHolidays(data[2]);
                                self.btnExtraction_Click();
                            }
                         self.showErrorDialog();
                        }
                        dfd.resolve();
                    }).fail((data) => {
                        nts.uk.ui.block.clear();
                        nts.uk.ui.dialog.alert(data.message);
                        dfd.resolve();
                    });
                    dfd.promise();
                } else {
                    nts.uk.ui.block.clear();
                    if (!checkDataCare) {
                       // nts.uk.ui.dialog.alertError({ messageId: "Msg_996" })
                        self.showErrorDialog();
                    }
                }
            } 
        }
        
        checkIsColumn(dataCell: any, key: any): boolean {
            let check = false;
          _.each(dataCell, (item: any) =>{
              if (item.columnKey.indexOf("NO" + key) != -1) {
                  check = true;
                  return;
              }
              });
            return check;
        }
        
        getPrimitiveValue(value : any, atr: any): string{
            var self = this;
            let valueResult : string = "";
            if (atr != undefined && atr != null) {
                    if (atr == 6) {
                        // Time
                        valueResult = value == "" ? null : String(self.getHoursAll(value));
                    } else if(atr == 5){
                         valueResult =  value == "" ? null : String(self.getHoursTime(value));
                    } else{
                         valueResult = value;
                    }
            } else {
                valueResult = value;
            }
            return valueResult;
        }
        
        getHours(value: any) : number{
            return Number(value.split(':')[0]) * 60 + Number(value.split(':')[1]);
        }
        //time
        getHoursTime(value) : number{
              var self = this;
            if (value.indexOf(":") != -1) {
                if (value.indexOf("-") != -1) {
                    let valueTemp = value.split('-')[1];
                    return 0 - self.getHours(valueTemp);
                } else {
                    return self.getHours(value);
                }
            } else {
                return value;
            }
        }
        
        //time day
        getHoursAll(value: any) : number{
             var self = this;
            if (value.indexOf(":") != -1) {
                if (value.indexOf("-") != -1) {
                    let valueTemp = value.split('-')[1];
                    return self.getHours(valueTemp) - 24 * 60;
                } else {
                    return self.getHours(value);
                }
            } else {
                return value;
            }
        }
        
        //check data item in care and childCare 
        // child care = 0 , care = 1, other = 2;
        checkItemCare(itemId : any): number {
            if(itemId == 759 || itemId == 760 || itemId == 761 || itemId == 762){
               return 0; 
            }else if(itemId == 763 || itemId == 764 || itemId == 765 || itemId == 766){
               return 1; 
            }else{
               return 2; 
            }
        }
        
        // check data error group care , child care
        checkErrorData(group : number, data: any, dataSource :any ) : boolean{
            var self = this;
            if (group != 2) {
                let rowItemSelect: any = _.find(dataSource, function(value: any) {
                        return value.id == data.rowId;
                    });
                data["itemId"] = data.columnKey.substring(1, data.columnKey.length);
                self.checkInputCare(data, rowItemSelect);
                if (data.value != "") {
                    if (group == 0) {
                        if ((rowItemSelect.A763 != undefined && rowItemSelect.A763 != "") || (rowItemSelect.A763 != undefined && rowItemSelect.A763 != "")
                            || (rowItemSelect.A763 != undefined && rowItemSelect.A763 != "") || (rowItemSelect.A763 != undefined && rowItemSelect.A763 != "")) {
                            // alert Error
                            self.listCareError.push(data);
                            return false;
                            // nts.uk.ui.dialog.alertError({ messageId: "Msg_996" })
                        }

                    } else if (group == 1) {
                        if ((rowItemSelect.A759 != undefined && rowItemSelect.A759 != "") || (rowItemSelect.A760 != undefined && rowItemSelect.A760 != "")
                            || (rowItemSelect.A761 != undefined && rowItemSelect.A761 != "") || (rowItemSelect.A762 != undefined && rowItemSelect.A762 != "")) {
                            // alert Error
                            self.listCareError.push(data);
                            return false;
                            //nts.uk.ui.dialog.alertError({ messageId: "Msg_996" })
                        }
                    }
                }
            }
            return true;
        }
        
        checkInputCare(data: any, rowItemSelect : any){
            var self = this;
            switch(Number(data.itemId)){
                case 759:
                    if(!self.isNNUE(rowItemSelect.A760) || data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                       data["group"] = 760;
                       self.listCareInputError.push(data); 
                    }
                    break;
                case 760:
                 if(!self.isNNUE(rowItemSelect.A759)||data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                     data["group"] = 759;
                       self.listCareInputError.push(data); 
                    }
                    break;
                case 761:
                 if(!self.isNNUE(rowItemSelect.A762)||data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                       data["group"] = 762;
                       self.listCareInputError.push(data); 
                    }
                    break;
                case 762:
                 if(!self.isNNUE(rowItemSelect.A761)||data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                     data["group"] = 761;
                       self.listCareInputError.push(data); 
                    }
                    break;
                case 763:
                 if(!self.isNNUE(rowItemSelect.A764)||data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                       data["group"] = 764;
                       self.listCareInputError.push(data); 
                    }
                    break;
                case 764:
                 if(!self.isNNUE(rowItemSelect.A763)||data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                       data["group"] = 763;
                       self.listCareInputError.push(data); 
                    }
                    break;
                case 765:
                 if(!self.isNNUE(rowItemSelect.A766)||data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                       data["group"] = 766;
                       self.listCareInputError.push(data); 
                    }
                    break;
                case 766:
                 if(!self.isNNUE(rowItemSelect.A765)||data.value ==""){
                       data["itemId"] = data.columnKey.substring(1,data.columnKey.length);
                       data["group"] = 765;
                       self.listCareInputError.push(data); 
                    }
                    break;
            }
        }
        
        isNNUE(value : any) : boolean{
            if(value != undefined && value != "" && value != null) return true;
            else return false;
        }
        
        hideComponent() {
            var self = this;
            if (self.displayFormat() == 0) {
                $("#emp-component").css("display", "block");
                $("#cbListDate").css("display", "none");
                $('#numberHoliday').show();
                $('#fixed-table').show();
               //  $("#content-grid").attr('style', 'top: 244px !IMPORTANT');
            } else if (self.displayFormat() == 1) {
                $("#cbListDate").css("display", "block");
                $("#emp-component").css("display", "none");
                $('#numberHoliday').hide();
                $('#fixed-table').hide();
               // $("#content-grid").attr('style', 'top: 225px !IMPORTANT');
            } else {
                $("#cbListDate").css("display", "none");
                $("#emp-component").css("display", "none");
                $('#numberHoliday').hide();
                $('#fixed-table').hide();
               // $("#content-grid").attr('style', 'top: 180px !IMPORTANT');
            }
        }
        btnExtraction_Click() {
            var self = this;
            console.log(self.dailyPerfomanceData());
            if (!nts.uk.ui.errors.hasError()) {
                self.hideComponent();
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
                        startDate:  self.displayFormat() === 1 ?  moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                        endDate:  self.displayFormat() === 1 ?  moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                    },
                    displayFormat : self.displayFormat(),
                    initScreen: 1,
                    lstEmployee: lstEmployee,
                    formatCodes: self.formatCodes(),
                    objectShare: self.shareObject()
                };
                nts.uk.ui.block.invisible();
                nts.uk.ui.block.grayout();
                service.startScreen(param).done((data) => {
                    if (data.typeBussiness != localStorage.getItem('kdw003_type')) {
                        localStorage.removeItem(window.location.href + '/dpGrid');
                    }
                    localStorage.setItem('kdw003_type', data.typeBussiness);
                    self.formatCodes(data.lstControlDisplayItem.formatCode);
                    let idC = self.createKeyLoad();
                    //TO Thanh: set data for list attendance item after load by extract click
                    self.lstAttendanceItem(data.lstControlDisplayItem.lstAttendanceItem);
                    self.itemValueAll(data.itemValues);
                    self.createSumColumn(data);
                    self.columnSettings(data.lstControlDisplayItem.columnSettings);
                    self.showPrincipal(data.showPrincipal);
                    self.showSupervisor(data.showSupervisor);
                    self.showTighProcess(data.identityProcessDto.useConfirmByYourself && self.displayFormat() === 0);
                    if (data.lstControlDisplayItem.lstHeader.length == 0) self.hasLstHeader = false;
                    if ( self.showPrincipal() || data.lstControlDisplayItem.lstHeader.length == 0) {
                        self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3], self.fixHeaders()[4]];
                        self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[4]];
                        self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[4]];
                    } else {
                        self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3]];
                        self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6]];
                        self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3]];
                    }
                    if (self.showSupervisor()) {
                        if (self.showSupervisor()) {
                            self.employeeModeHeader.push(self.fixHeaders()[8]);
                            self.dateModeHeader.push(self.fixHeaders()[8]);
                            self.errorModeHeader.push(self.fixHeaders()[8]);
                        }
                    }
                    self.receiveData(data);
                    self.extraction();
                    // no20
                    self.dPErrorDto(data.dperrorDto);
                    nts.uk.ui.block.clear();
                }).fail(function(error) {
                    nts.uk.ui.dialog.alert(error.message);
                    nts.uk.ui.block.clear();
                });
            }
        }

        showErrorDialog() {
            var self = this;
            let lstEmployee = [];
            let uiErrors : any = $("#dpGrid").ntsGrid("errors");
            let errorValidateScreeen : any = [];
            if (self.displayFormat() === 0) {
                _.each(uiErrors, value => {
                    let dateCon = _.find(self.dpData, (item: any) => {
                        return item.id == value.rowId.substring(1, value.rowId.length);
                    });

                    let object = { date: dateCon.date , employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: value.message, itemName: "", columnKey: value.columnKey };
                    let item = _.find(self.optionalHeader, (data) => {
                        if (data.group != undefined && data.group != null) {
                            return String(data.group[0].key) === value.columnKey;
                        } else {
                            return String(data.key) === value.columnKey;
                        }
                    })
                    object.itemName = (item == undefined) ? "" : item.headerText; 
                    errorValidateScreeen.push(object);
                });
            } else {
                _.each(uiErrors, value => {
                    let dateCon = _.find(self.dpData, (item: any) => {
                        return item.id == value.rowId.substring(1, value.rowId.length);
                    });

                    let object = { date: dateCon.date, employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: value.message, itemName: "", columnKey: value.columnKey };
                    let item = _.find(self.optionalHeader, (data) => {
                        return String(data.key) === value.columnKey;
                    })
                    object.itemName = (item == undefined) ? "" : item.headerText;
                    errorValidateScreeen.push(object);
                });
            };
             // get error insert , update
            _.each(self.listCareError(), value => {
                let dateCon = _.find(self.dpData, (item: any) => {
                    return item.id == value.rowId.substring(1, value.rowId.length);
                });
                let object = { date: dateCon.date, employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: nts.uk.resource.getMessage("Msg_996"), itemName: "", columnKey: value.itemId };
                let item = _.find(self.optionalHeader, (data) => {
                    return String(data.key) === "A" + value.itemId;
                })
                object.itemName = (item == undefined) ? "" : item.headerText;
                errorValidateScreeen.push(object);
            });
            
            // careinput
            _.each(self.listCareInputError(), value => {
                let dateCon = _.find(self.dpData, (item: any) => {
                    return item.id == value.rowId.substring(1, value.rowId.length);
                });
                let object = { date: dateCon.date, employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: nts.uk.resource.getMessage("Msg_1108"), itemName: "", columnKey: value.itemId };
                let item = _.find(self.optionalHeader, (data) => {
                    return String(data.key) === "A" + value.itemId;
                })
                object.itemName = (item == undefined) ? "" : item.headerText;
                
                 let itemGroup = _.find(self.optionalHeader, (data) => {
                    return String(data.key) === "A" + value.group;
                })
                let nameGroup : any = (itemGroup == undefined) ? "" : itemGroup.headerText;
                object.message = nts.uk.resource.getMessage("Msg_1108", [object.itemName, nameGroup]);
                errorValidateScreeen.push(object);
            });
            
            //CheckHolidays
              _.each(self.listCheckHolidays(), value => {
                  let object = { date: value.date, employeeCode: self.dpData[0].employeeCode, employeeName: self.dpData[0].employeeName, message: "大塚用連続休暇チェック設定.表示するメッセージ", itemName: value.rowId, columnKey: value.itemId };
                   errorValidateScreeen.push(object); 
              });
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
            nts.uk.ui.windows.setShared("errorValidate", errorValidateScreeen);
            self.dialogShow = nts.uk.ui.windows.sub.modeless("/view/kdw/003/b/index.xhtml");
        }
        changeExtractionCondition() {
            var self = this;
            if (!nts.uk.ui.errors.hasError()) {
                self.hideComponent();
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
                                startDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                                endDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                            },
                            lstEmployee: lstEmployee,
                            displayFormat : self.displayFormat(),
                            errorCodes: errorCodes,
                            formatCodes: self.formatCodes()
                        };
                        nts.uk.ui.block.invisible();
                        nts.uk.ui.block.grayout();
                        service.selectErrorCode(param).done((data) => {
                            self.createSumColumn(data);
                            self.columnSettings(data.lstControlDisplayItem.columnSettings);
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
        }

        selectDisplayItem() {
            var self = this;
            if (!nts.uk.ui.errors.hasError()) {
                nts.uk.ui.windows.setShared("selectedPerfFmtCodeList", self.formatCodes());
                nts.uk.ui.windows.sub.modal("/view/kdw/003/c/index.xhtml").onClosed(() => {
                    var dataTemp = nts.uk.ui.windows.getShared('dailyPerfFmtList');
                    if (dataTemp != undefined) {
                        let data = [dataTemp.dailyPerformanceFormatCode()];
                       self.hideComponent();
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
                                startDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                                endDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                            },
                            lstEmployee: lstEmployee,
                            displayFormat : self.displayFormat(),
                            formatCodes: data
                        };
                        nts.uk.ui.block.invisible();
                        nts.uk.ui.block.grayout();
                        service.selectFormatCode(param).done((data) => {
                            self.createSumColumn(data);
                            self.columnSettings(data.lstControlDisplayItem.columnSettings);
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
        }

        referencesActualResult() {
            var self = this;
            if (!nts.uk.ui.errors.hasError()) {
                let lstEmployee = [];
                if (self.displayFormat() === 0) {
                    lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                        return employee.id === self.selectedEmployee();
                    }));
                    nts.uk.ui.windows.setShared("KDL014A_PARAM", {
                        startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        endDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        employeeID: lstEmployee[0].code
                    });
                    nts.uk.ui.windows.sub.modal("/view/kdl/014/a/index.xhtml").onClosed(() => {
                    });

                } else if (self.displayFormat() === 1) {
                    lstEmployee = self.lstEmployee().map((data) => {
                        return data.code;
                    });
                     nts.uk.ui.windows.setShared("KDL014B_PARAM", {
                        startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        endDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        lstEmployee: lstEmployee
                    });
                    nts.uk.ui.windows.sub.modal("/view/kdl/014/b/index.xhtml").onClosed(() => {
                    });
                }
            }
        }

        employmentOk() {
            let _self = this;
            if (!nts.uk.ui.errors.hasError()) {
                nts.uk.ui.block.grayout();
                nts.uk.ui.windows.sub.modal("/view/kdl/006/a/index.xhtml").onClosed(() => {
                    nts.uk.ui.block.clear();
                });
            }
        }
        
        tighProcess(){
            let self = this;
            var dataSource = $("#dpGrid").igGrid("option", "dataSource");
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            let dataRowEnd = dataSource[dataSource.length - 1];
            service.addClosure({ employeeId: dataRowEnd.employeeId, date: dataRowEnd.dataDetail }).done((data) => {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                nts.uk.ui.block.clear();
            });
            nts.uk.ui.block.clear();
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
                lstHeader: {},
                formatCode: self.formatCodes()
            };
            let jsonColumnWith = localStorage.getItem(window.location.href + '/dpGrid');
            let valueTemp = 0;
            _.forEach($.parseJSON(jsonColumnWith), (value, key) => {
                if (key.indexOf('A') != -1) {
                    if (nts.uk.ntsNumber.isNumber(key.substring(1, key.length))) {
                        command.lstHeader[key.substring(1, key.length)] = value;
                    }
                }
                if (key.indexOf('Code') != -1 || key.indexOf('NO') != -1) {
                    valueTemp = value;
                } else if (key.indexOf('Name') != -1) {
                    command.lstHeader[key.substring(4, key.length)] = value + valueTemp;
                    valueTemp = 0;
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
            if(self.showPrincipal() && self.hasLstHeader){
                let sign = _.find( self.fixColGrid(), (data: any) =>{
                    return data.columnKey === 'sign';
                }
                )
                if(sign == undefined) self.fixColGrid.push({ columnKey: 'sign', isFixed: true });
            }
            if(self.showSupervisor() && self.hasLstHeader){
                let sign = _.find( self.fixColGrid(), (data: any) =>{
                    return data.columnKey === 'approval';
                }
                )
                if(sign == undefined) self.fixColGrid.push({ columnKey: 'approval', isFixed: true });
            }
            self.loadHeader(self.displayFormat());
            self.dailyPerfomanceData(self.filterData(self.displayFormat()));
        }
        
        extraction() {
            var self = this;
            let start = performance.now();
            self.destroyGrid();
            console.log("destroy grid :" + (start- performance.now()));
            start = performance.now();
            self.extractionData();
            console.log("calc load extractionData :" + (start- performance.now()));
            self.loadGrid();
        }

        isDisableRow(id) {
            var self = this;
            for (let i = 0; i < self.rowStates().length; i++) {
                return self.rowStates()[i].rowId == id;
            }
        }

        isDisableSign(id, data) {
            var self = this;
            return _.filter(data, (value: any) => {
                return value.rowId == id &&  (_.filter(value.state, state =>{
                  return  state == "ntsgrid-disable"  
                }).length > 0);
            });
        }
        
        lstDisableSign() {
            var self = this;
            return _.filter(self.cellStates(), (data: any) => {
                return data.columnKey == "sign";
            });
        }
        
        lstDisableApproval() {
           var self = this;
            return _.filter(self.cellStates(), (data: any) => {
                return data.columnKey == "approval";
            });
        }
        
        signAll() {
            var self = this;
            $("#dpGrid").ntsGrid("checkAll", "sign");
            $("#dpGrid").ntsGrid("checkAll", "approval");
        }
        releaseAll() {
            var self = this;
            $("#dpGrid").ntsGrid("uncheckAll", "sign");
            $("#dpGrid").ntsGrid("uncheckAll", "approval");
        }
        destroyGrid() {
            $("#dpGrid").ntsGrid("destroy");
            $("#dpGrid").remove();
            $(".nts-grid-sheet-buttons").remove();
            $('<table id="dpGrid"></table>').appendTo('#gid');
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
                if (header.group !=  null && header.group != undefined && header.group.length > 0) {
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
            let start = performance.now();
            let data =  lstData.map((data) => {
                var object = {
                    id: "_" + data.id,
                    state: data.state,
                    error: data.error,
                    date: moment(data.date, "YYYY/MM/DD").format("MM/DD(dd)"),
                    sign: data.sign,
                    approval: data.approval,
                    employeeId: data.employeeId,
                    employeeCode: data.employeeCode,
                    employeeName: data.employeeName,
                    workplaceId : data.workplaceId,
                    employmentCode : data.employmentCode,
                    dateDetail : moment(data.date, "YYYY/MM/DD"),
                    typeGroup : data.typeGroup
                }
                _.each(data.cellDatas, function(item) { object[item.columnKey] = item.value });
                return object;
            });
             console.log("calc load source :" + (start- performance.now()));
            return data;
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
        
        totalNumber(data) {
            let total = 0;
            let currentPageIndex = $("#dpGrid").igGridPaging("option", "currentPageIndex");
            let pageSize = $("#dpGrid").igGridPaging("option", "pageSize");
            let startIndex: any = currentPageIndex * pageSize;
            let endIndex: any = startIndex + pageSize;
            _.forEach(data, function(d, i) {
                if (i < startIndex || i >= endIndex) return;
                let n = parseInt(d);
                if (!isNaN(n)) total += n;
            });
            return total;
        }
        totalTime(data) {
            let currentPageIndex = $("#dpGrid").igGridPaging("option", "currentPageIndex");
            let pageSize = $("#dpGrid").igGridPaging("option", "pageSize");
            let startIndex: any = currentPageIndex * pageSize;
            let endIndex: any = startIndex + pageSize;
            let total = moment.duration("0");
            _.forEach(data, function(d, i) {
                if (i < startIndex || i >= endIndex) return;
                total.add(moment.duration(d));
            });
            let time = total.asHours();
            let hour = Math.floor(time);
            let minute = (time - hour) * 60;
            let roundMin = Math.round(minute);
            let minuteStr = roundMin < 10 ? ("0" + roundMin) : String(roundMin);
            return hour + ":" + minuteStr;
        }
        
         totalMoney(data) {
            let total = 0;
            let currentPageIndex = $("#dpGrid").igGridPaging("option", "currentPageIndex");
            let pageSize = $("#dpGrid").igGridPaging("option", "pageSize");
            let startIndex: any = currentPageIndex * pageSize;
            let endIndex: any = startIndex + pageSize;
            _.forEach(data, function(d, i) {
                 let  valueResult = "";
                if (i < startIndex || i >= endIndex) return;
                if (String(d).indexOf(",") != -1) {
                    for (let i = 0; i < String(d).split(',').length; i++) {
                        valueResult += String(d).split(',')[i];
                    }
                    let n = parseFloat(valueResult);
                    if (!isNaN(n)) total += n;
                }else{
                    let n = parseFloat(d);
                    if (!isNaN(n)) total += n;
                }
            });
            return total.toLocaleString('en');
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
            if ($('.ccg-sample-has-error').ntsError('hasError')) {
                return;
            }
            if (false && false &&  false) {
                nts.uk.ui.dialog.alertError("Base Date or Closure or Period must be shown!");
                return;
            }
            
            self.ccg001 = {
                /** Common properties */
                systemType: 2,
                showEmployeeSelection: false, // 検索タイプ
                showQuickSearchTab: true, // クイック検索
                showAdvancedSearchTab: true, // 詳細検索
                showBaseDate: false, // 基準日利用
                showClosure: true, // 就業締め日利用
                showAllClosure: false, // 全締め表示
                showPeriod: true, // 対象期間利用
                periodFormatYM: false, // 対象期間精度

                /** Required parameter */
                //baseDate: self.baseDate().toISOString(), // 基準日
                periodStartDate: new Date(self.dateRanger().startDate).toISOString(), // 対象期間開始日
                periodEndDate: new Date(self.dateRanger().endDate).toISOString(), // 対象期間終了日
                inService: true, // 在職区分
                leaveOfAbsence: true, // 休職区分
                closed: true, // 休業区分
                retirement: false, // 退職区分

                /** Quick search tab options */
                showAllReferableEmployee: true, // 参照可能な社員すべて
                showOnlyMe: true, // 自分だけ
                showSameWorkplace: true, // 同じ職場の社員
                showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

                /** Advanced search properties */
                showEmployment: true, // 雇用条件
                showWorkplace: true, // 職場条件
                showClassification: true, // 分類条件
                showJobTitle: true, // 職位条件
                showWorktype: true, // 勤種条件
                // 選択モード

                /** Return data */
                returnDataFromCcg001: function(dataList: any) {
                   // self.selectedEmployee(dataList.listEmployee);
                    self.lstEmployee(dataList.listEmployee.map((data: EmployeeSearchDto) => {
                        return {
                            id: data.employeeId,
                            code: data.employeeCode,
                            businessName: data.employeeName,
                            workplaceName: data.workplaceName,
                            workplaceId: data.workplaceId,
                            depName: '',
                            isLoginUser: false
                        };
                    }));
                    self.lstEmployee(_.orderBy(self.lstEmployee(), ['code'], ['asc']));
                    self.selectedEmployee(self.lstEmployee()[0].id);
                    self.loadKcp009();
                    self.btnExtraction_Click();
                },
            }
        }
        
        createKeyLoad() : any {
            var self = this;
            let lstEmployee = [];
            let data = [];
            if (self.displayFormat() === 0) {
                lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                }));
            } else {
                lstEmployee = self.lstEmployee();
            }
            let format = "YYYY-MM-DD";
             moment(self.dateRanger().startDate).format(format)
            let startDate = self.displayFormat() === 1 ? moment(self.selectedDate()).format(format) : moment(self.dateRanger().startDate).format(format);
            let endDate = self.displayFormat() === 1 ? moment(self.selectedDate()).format(format) : moment(self.dateRanger().endDate).format(format);
            let i = 0;
            if (self.displayFormat() === 0) {
                _.each(self.createDateList(self.dateRanger().startDate, (self.dateRanger().endDate)), value => {
                    data.push(self.displayFormat() + "_" + self.lstEmployee()[0].id + "_" + self.convertDateToString(value) + "_" + endDate + "_" + i)
                    i++;
                });
            } else if (self.displayFormat() === 1) {
                _.each(lstEmployee, value => {
                    data.push(self.displayFormat() + "_" + value.id + "_" + startDate + "_" + endDate)
                    i++;
                });
            } else {
                _.each(lstEmployee, value => {
                    _.each(self.createDateList(self.dateRanger().startDate, (self.dateRanger().endDate)), value2 => {
                        data.push(self.displayFormat() + "_" + value.id + "_" + self.convertDateToString(value2) + "_" + endDate + "_" + i)
                        i++;
                    });
                });
            }
          return data;
        }
        
        createDateList(startDate: any, endDate: any) :any{
             var dateArray = [];
            var currentDate : any  = moment(startDate);
            var stopDate : any= moment(endDate);
            while (currentDate <= stopDate) {
                dateArray.push(moment(currentDate).format('YYYY-MM-DD'))
                currentDate = moment(currentDate).add(1, 'days');
            }
            return dateArray;
        }
        convertDateToString(date :any) : any{
          return moment(date).format('YYYY-MM-DD');
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
            //console.log(self.formatDate(self.dailyPerfomanceData()));
            let start = performance.now();
            let dataSource = self.formatDate(self.dailyPerfomanceData());
            $("#dpGrid").ntsGrid({
                width: (window.screen.availWidth - 200) + "px",
                height: '650px',
                dataSource: dataSource,
//                dataSourceAdapter: function(ds) {
//                    let start = performance.now();
//                    let data = ds.map((data) => {
//                        var object = {
//                            id: data.id,
//                            state: data.state,
//                            error: data.error,
//                            date: moment(data.date, "YYYY/MM/DD").format("MM/DD(dd)"),
//                            sign: data.sign,
//                            employeeId: data.employeeId,
//                            employeeCode: data.employeeCode,
//                            employeeName: data.employeeName
//                        }
//                        _.each(data.cellDatas, function(item) { object[item.columnKey] = item.value });
//                        return object;
//                    });
//                    console.log("calc load source :" + (start - performance.now()));
//                    return data;
//                },
                primaryKey: 'id',
                rowVirtualization: true,
                virtualization: true,
                virtualizationMode: 'continuous',
                enter: self.selectedDirection() == 0 ? 'below' : 'right',
                autoFitWindow: false,
                preventEditInError: false,
                columns: self.headersGrid(),
                hidePrimaryKey: true,
                userId: self.employIdLogin.id,
                getUserId: function(primaryKey) {
                   let ids =  primaryKey.split("_");
                   return ids[2]+"-"+ ids[3]+"-"+ids[4]+"-"+ids[5]+"-"+ids[6]; 
                 },
               // recordKeys: self.createKeyLoad(),
                features: [
                    { name: 'Paging', pageSize: 31, currentPageIndex: 0 },
                    { name: 'ColumnFixing', fixingDirection: 'left', showFixButtons: false, columnSettings: self.fixColGrid() },
                    { name: 'Resizing', columnSettings: [{ columnKey: 'id', allowResizing: false, minimumWidth: 0 }] },
                    { name: 'MultiColumnHeaders' },
                    {
                        name: 'Summaries',
                        showSummariesButton: false,
                        showDropDownButton: false,
                        columnSettings: self.columnSettings(),
                        resultTemplate: '{1}'
                    }
                ],
                ntsFeatures: self.createNtsFeatures(),
                ntsControls: [
                    { name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                    {
                        name: 'Link2',
                        click: function(rowId, key, event) {
                           // self.helps(event);
                            //let cell : any = $("#dpGrid").igGrid("activeCell");
                           // if (cell) {
                                let value = $("#dpGrid").igGrid("getCellValue", rowId, "Code" + key.substring(4, key.length));
                                let dialog: TypeDialog = new TypeDialog(key.substring(4, key.length), self.lstAttendanceItem(), value, rowId);
                                dialog.showDialog(self);
                                nts.uk.ui.block.clear();
                          //  }
                        },
                        controlType: 'LinkLabel'
                    },
                    { name: 'TextEditorNumberSeparated', controlType: 'TextEditor', constraint: { valueType: 'Integer', required: true, format: "Number_Separated" } },
                    { name: 'TextEditorTimeShortHM', controlType: 'TextEditor', constraint: { valueType: 'Time', required: true, format: "Time_Short_HM" } },
                    { name: 'ComboboxCalc',  width: '250px', options: self.comboItemsCalc(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumnsCalc(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },
                    { name: 'ComboboxReason',  width: '120px',  options: self.comboItemsReason(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumns(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },
                    { name: 'ComboboxDoWork',  width: '120px', options: self.comboItemsDoWork(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumns(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },
                    {
                        name: 'FlexImage', source: 'ui-icon ui-icon-locked', click: function(key, rowId, evt) {
                            let data = $("#dpGrid").igGrid("getCellValue", rowId, key);
                            if(data!= ""){
                                 let lock = data.split("|");
                                 let tempD = "<span>";
                                 for (let i =1 ; i< lock.length ; i++){
                                     if(lock[i]=="D" || lock[i]=="M") tempD +=  nts.uk.resource.getText("KDW003_66")+'<br/>'; 
                                     if(lock[i]=="C") tempD += nts.uk.resource.getText("KDW003_67")+'</span>'; 
                                     $('#textLock').html(tempD);
                                }
                                }
                            self.helps(evt, "");
                        },
                        controlType: 'FlexImage'
                    },
                    {
                        name: 'Button', controlType: 'Button', text: nts.uk.resource.getText("KDW003_63"), enable: true, click: function(data) {
                            let source: any = $("#dpGrid").igGrid("option", "dataSource");
                            let rowItemSelect: any = _.find(source, function(value: any) {
                                return value.id == data.id;
                            })
                            let errorCode = _.map(_.filter(self.dPErrorDto(), (value: any) => {
                                return value.employeeId == rowItemSelect.employeeId && value.processingDate == rowItemSelect.dateDetail.format("YYYY/MM/DD");
                            }), value => {
                                return value.errorCode;
                            });
                            let dfd = $.Deferred();
                            let dataShare: any = {
                                date: rowItemSelect.dateDetail.format("YYYY/MM/DD"),
                                listValue: []
                            }
                            let date = moment(dataShare.date, "YYYY/MM/DD");
                            if (errorCode.length > 0) {
                                $.when(service.getApplication(errorCode)).done((data) => {
                                    dataShare.listValue = data;
                                    nts.uk.ui.windows.setShared("shareToKdw003e", dataShare);
                                    nts.uk.ui.windows.sub.modal("/view/kdw/003/e/index.xhtml").onClosed(() => {
                                        let screen = nts.uk.ui.windows.getShared("shareToKdw003a");
                                        if(screen == undefined) screen = 1905;
                                        switch (screen.code) {
                                            case 0:
                                                //KAF005-残業申請
                                                nts.uk.request.jump("/view/kaf/005/a/index.xhtml", { date: date });
                                             break;
                                            case 1:
                                                //KAF006-休暇
                                                nts.uk.request.jump("/view/kaf/006/a/index.xhtml", { date: date });
                                                break;
                                            case 2:
                                                //KAF007-勤務変更申請
                                                nts.uk.request.jump("/view/kaf/007/a/index.xhtml", { date: date });
                                                break;
                                            case 3:
                                                //KAF008-出張申請
                                                nts.uk.request.jump("/view/kaf/008/a/index.xhtml", { date: date });
                                                break;
                                            case 4:
                                                //KAF009-直行直帰申請
                                                nts.uk.request.jump("/view/kaf/009/a/index.xhtml", { date: date });
                                                break;
                                            case 6:
                                                //KAF010-休日出勤時間申請
                                                nts.uk.request.jump("/view/kaf/010/a/index.xhtml", { date: date });
                                                break;
                                            case 7:
                                                //KAF002-打刻申請
                                                nts.uk.request.jump("/view/kaf/002/a/index.xhtml", { date: date });
                                                break;
                                            case 9:
                                                //KAF004-遅刻早退取消申請
                                                nts.uk.request.jump("/view/kaf/004/a/index.xhtml", { date: date });
                                                break;
                                            case 10:
                                                //KAF011-振休振出申請
                                                nts.uk.request.jump("/view/kaf/011/a/index.xhtml", { date: date });
                                                break;
                                            default:
                                                break;
                                        }
                                     });
                                    dfd.resolve();
                                });
                            } else {
                                dataShare.listValue = [];
                                nts.uk.ui.windows.setShared("shareToKdw003e", dataShare);
                                nts.uk.ui.windows.sub.modal("/view/kdw/003/e/index.xhtml").onClosed(() => {
                                });
                                dfd.resolve();
                            }
                            dfd.promise();
                        }
                    }
                ]
            });
            console.log("load grid ALL" + (performance.now()- start));
        }
        
        reloadGrid() {
            var self = this;
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            self.createSumColumn(self.dataAll());
            self.columnSettings(self.dataAll().lstControlDisplayItem.columnSettings);
            self.receiveData(self.dataAll());
            self.extractionData();
            self.loadGrid();
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
//            let lzyLoad = {
//                name: "LoadOnDemand",
//                allKeysPath: "/kdw003/lazyload/keys",
//                pageRecordsPath: "/kdw003/lazyload/data",
//            }
//            if(self.displayFormat() == 1){
//               lstNtsFeature.push(lzyLoad); 
//            }
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
                    delete header.group;
                     if (header.constraint == null) {
                        delete header.constraint;
                    }
                    tempList.push(header);
                });
            } else if (mode == 1) {
                self.displayProfileIcon();
                _.forEach(self.dateModeHeader, (header) => {
                    if (header.constraint == null) {
                        delete header.constraint;
                    }
                    delete header.group;
                    tempList.push(header);
                });
            } else if (mode == 2) {
                _.forEach(self.errorModeHeader, (header) => {
                    if (header.constraint == null) {
                        delete header.constraint;
                    }
                    delete header.group;
                    tempList.push(header);
                });
            }
            self.dislayNumberHeaderText();
            _.forEach(self.optionalHeader, (header) => {
                if (header.constraint == null || header.constraint == undefined) {
                    delete header.constraint;
                }else{
                    header.constraint["cDisplayType"] = header.constraint.cdisplayType;
                    if (header.constraint.cDisplayType != null && header.constraint.cDisplayType != undefined) {
                        if (header.constraint.cDisplayType != "Primitive" && header.constraint.cDisplayType != "Combo") {
                            if (header.constraint.cDisplayType.indexOf("Currency") != -1) {
                                header["columnCssClass"] = "currency-symbol";
                                header.constraint["min"] = "0";
                                header.constraint["max"] = "9999999999"
                            } else if (header.constraint.cDisplayType == "Clock") {
                                header["columnCssClass"] = "right-align";
                                header.constraint["min"] = "-10:00";
                                header.constraint["max"] = "100:30"
                            } else if (header.constraint.cDisplayType == "Integer") {
                                header["columnCssClass"] = "right-align";
                            }
                            delete header.constraint.primitiveValue;
                        } else {
                           
                            if (header.constraint.cDisplayType == "Primitive") {
                                delete header.group[0].constraint.cDisplayType
                            } else if (header.constraint.cDisplayType == "Combo") {
                                    header.group[0].constraint["min"] = 0;
                                    header.group[0].constraint["max"] = Number(header.group[0].constraint.primitiveValue);
                                    header.group[0].constraint["cDisplayType"] =  header.group[0].constraint.cdisplayType;
                                   delete header.group[0].constraint.cdisplayType
                                   delete header.group[0].constraint.primitiveValue;
                            }
                            delete header.constraint;
                            delete header.group[1].constraint;
                        }
                    }
                    if(header.constraint != undefined) delete header.constraint.cdisplayType;
                }
                if (header.group != null && header.group != undefined) {
                    if (header.group.length > 0) {
                       if( header.group[0].constraint == undefined) delete header.group[0].constraint;
                       delete header.group[1].constraint;
                        delete header.group[0].group;
                        delete header.key;
                        delete header.dataType;
                       // delete header.width;
                        delete header.ntsControl;
                        delete header.changedByOther;
                        delete header.changedByYou;
                       // delete header.color;
                        delete header.hidden;
                        delete header.ntsType;
                        delete header.onChange;
                        delete header.group[1].ntsType;
                        delete header.group[1].onChange;
                        if (header.group[0].dataType == "String") {
                            header.group[0].onChange = self.search;
                           // delete header.group[0].onChange;
                            delete header.group[0].ntsControl;
                        } else {
                            delete header.group[0].onChange;
                            delete header.group[0].ntsControl;
                        }
                        delete header.group[1].group;
                    } else {
                        delete header.group;
                    }
                }
                tempList.push(header);
            });
            return self.headersGrid(tempList);
        }
        
          pushDataEdit(evt, ui) : InfoCellEdit {
            var self = this;
            var dataEdit: InfoCellEdit
            let edit: InfoCellEdit = _.find(self.editValue, function(item: any) {
                return item.rowID == ui.rowID && item.columnKey == ui.columnKey;
            });

            if (edit) {
                edit.value = ui.value;
                return edit;
            }
            else {
                let dateCon = _.find(self.dpData, (item: any) => {
                    return item.id == ui.rowID.substring(1, ui.rowID.length);
                });

                let employeeIdSelect: any = _.find(self.dailyPerfomanceData(), function(item: any) {
                    return item.id == ui.rowID.substring(1, ui.rowID.length);
                });
                dataEdit = new InfoCellEdit(ui.rowID, ui.columnKey, ui.value, 1, "", employeeIdSelect.employeeId,  moment(dateCon.date).utc().toISOString(), 0);
                self.editValue.push(dataEdit);
                return dataEdit;
            }
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
                        if (header.group == undefined || header.group == null) {
                            header.headerText = header.headerText + " " + header.key.substring(1, header.key.length);
                        }else{
                            header.headerText = header.headerText + " " + header.group[1].key.substring(4, header.group[1].key.length)
                        }
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

        search(columnKey, rowId, val) {
            let dfd = $.Deferred();
            let i = 0;
            let data: any = $("#dpGrid").igGrid("option", "dataSource");
            let rowItemSelect: any = _.find(data, function(value: any) {
                return value.id == rowId;
            })
            let typeGroup;
            let splitTypeGroup = rowItemSelect.typeGroup.split("|");
            if(splitTypeGroup != undefined){
            typeGroup = _.find(splitTypeGroup, function(value: any) {
                let splitData = value.split(":");
                return "Code"+splitData[0] === columnKey;
            })
            }
            if (typeGroup != undefined && typeGroup != null) {
                let param = {
                    typeDialog: typeGroup.split(":")[1],
                    param: {
                        workTypeCode: val,
                        employmentCode: rowItemSelect.employmentCode,
                        workplaceId: rowItemSelect.workplaceId,
                        date: rowItemSelect.dateDetail,
                        selectCode: val
                    }
                }
                var object = {};
                if (val == "") {
                    dfd.resolve(nts.uk.resource.getText("KDW003_82"));
                } else {
                    $.when(service.findCodeName(param)).done((data) => {
                        dfd.resolve(data == undefined ? nts.uk.resource.getText("KDW003_81") : data.name);
                    });
                }
                
            }else{
                   dfd.resolve("");
            }
            return dfd.promise();
        }
        
        getDataShare(data : any){
            var self = this;
           var param = {
                dateRange: data.dateRangeParam? {
                    startDate: moment(data.dateRangeParam.startDate).utc().toISOString(),
                    endDate: moment(data.dateRangeParam.endDate).utc().toISOString()
                }: null,
                displayFormat : 0,
                initScreen: 0,
                lstEmployee: [],
                formatCodes: self.formatCodes()
            }; 
        }
    }
    export class AuthorityDetailModel {
        available1: KnockoutObservable<boolean> = ko.observable(true);
        available4: KnockoutObservable<boolean> = ko.observable(true);
        available2: KnockoutObservable<boolean> = ko.observable(true);
        available3: KnockoutObservable<boolean> = ko.observable(true);
        available5: KnockoutObservable<boolean> = ko.observable(true);
        available8: KnockoutObservable<boolean> = ko.observable(true);
        available8Authority: KnockoutObservable<boolean> = ko.observable(true);
        available22: KnockoutObservable<boolean> = ko.observable(true);
        available24: KnockoutObservable<boolean> = ko.observable(true);
        available7: KnockoutObservable<boolean> = ko.observable(true);
        available23: KnockoutObservable<boolean> = ko.observable(true);
        available25: KnockoutObservable<boolean> = ko.observable(true);
        available17: KnockoutObservable<boolean> = ko.observable(true);
        available18: KnockoutObservable<boolean> = ko.observable(true);
        available19: KnockoutObservable<boolean> = ko.observable(true);
        available20: KnockoutObservable<boolean> = ko.observable(true);
        available21: KnockoutObservable<boolean> = ko.observable(true);
        constructor(data: Array<DailyPerformanceAuthorityDto>, authority : any) {
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
            if(self.checkAvailable(data, 25)){
                $("#btn-signAll").css("visibility", "visible"); 
            }else{
                $("#btn-signAll").css("visibility", "hidden");
            }
           // $("#btn-signAll").css("visibility", "hidden");
            this.available17(self.checkAvailable(data, 17));
            this.available18(self.checkAvailable(data, 18));
            this.available19(self.checkAvailable(data, 19));
            this.available20(self.checkAvailable(data, 20));
            this.available21(self.checkAvailable(data, 21));
            this.available8Authority(this.available8() && authority)

        }
        checkAvailable(data: Array<DailyPerformanceAuthorityDto>, value: number): boolean {
            let self = this;
            var check = _.find(data, function(o) {
                return o.functionNo === value;
            })
            if (check == null) return false;
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

    export class ReferenceVacation {
        yearHoliday20: KnockoutObservable<boolean> = ko.observable(true);
        yearHoliday19: KnockoutObservable<boolean> = ko.observable(true);
        substVacation: KnockoutObservable<boolean> = ko.observable(true);
        compensLeave: KnockoutObservable<boolean> = ko.observable(true);
        com60HVacation: KnockoutObservable<boolean> = ko.observable(true);
        authentication: KnockoutObservable<AuthorityDetailModel> = ko.observable(null);
        allVacation: KnockoutObservable<boolean> = ko.observable(true);

        constructor(yearHoliday: any, substVacation: any, compensLeave: any, com60HVacation: any, authentication: AuthorityDetailModel) {
            var self = this;
            this.yearHoliday20(yearHoliday && authentication.available20());
            this.substVacation(substVacation && authentication.available18());
            this.yearHoliday19(yearHoliday && authentication.available19());
            this.compensLeave(compensLeave && authentication.available17());
            this.com60HVacation(com60HVacation && authentication.available21());
            this.allVacation(this.yearHoliday20() || this.substVacation() || this.yearHoliday19() || this.compensLeave() ||  this.com60HVacation());
        }
    }

    export class TypeDialog {

        attendenceId: string;
        data: Array<DPAttendanceItem>;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
        selectedCode: KnockoutObservable<string>;
        listCode: KnockoutObservableArray<any>;
        rowId: KnockoutObservable<any>;
        constructor(attendenceId: string, data: Array<DPAttendanceItem>, selectedCode: string, rowId: any) {
            var self = this;
            self.attendenceId = attendenceId;
            self.data = data;
            self.selectedCode = ko.observable(selectedCode);
            self.listCode = ko.observableArray([]);
            self.rowId = ko.observable(rowId);
        };

        showDialog(parent: any) {
            var self = this;
            var selfParent = parent;
            let item: DPAttendanceItem;
            let codeName: any;
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            let dfd = $.Deferred();
            item = _.find(self.data, function(data) {
                return data.id == self.attendenceId;
            })
            switch (item.typeGroup) {
                case 1:
                    // KDL002  
                    let param1 = {
                        typeDialog: 1,
                        param: {
                            workTypeCode: self.selectedCode(),
                            employmentCode: selfParent.employmentCode(),
                            workplaceId: "",
                            date: new Date(),
                            selectCode: self.selectedCode()
                        }
                    }
                    service.findAllCodeName(param1).done((data) => {
                        self.listCode([]);
                        self.listCode(_.map(data, 'code'));
                        nts.uk.ui.windows.setShared('KDL002_Multiple', false, true);
                        //all possible items
                        nts.uk.ui.windows.setShared('KDL002_AllItemObj', nts.uk.util.isNullOrEmpty(self.listCode()) ? [] : self.listCode(), true);
                        //selected items
                        nts.uk.ui.windows.setShared('KDL002_SelectedItemId', [self.selectedCode()], true);
                        nts.uk.ui.windows.sub.modal('/view/kdl/002/a/index.xhtml', { title: '乖離時間の登録＞対象項目', }).onClosed(function(): any {
                            var lst = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                            if (lst) {
                                var objectName = {};
                                objectName["Name" + self.attendenceId] = lst[0].name;
                                var objectCode = {};
                                objectCode["Code" + self.attendenceId] = lst[0].code;

                                $.when($("#dpGrid").ntsGrid("updateRow", self.rowId(), objectName),
                                    $("#dpGrid").ntsGrid("updateRow", self.rowId(), objectCode)
                                ).done(() => {
                                    nts.uk.ui.block.clear();
                                    dfd.resolve();
                                })
                            } else {
                                 nts.uk.ui.block.clear();
                                dfd.resolve();
                            }
                        })
                    });
                    dfd.promise();
                    break;
                case 2:
                    //KDL001 
                    let employeeIdSelect: any = _.find(selfParent.dailyPerfomanceData(), function(item: any) {
                        return item.id == self.rowId().substring(1, self.rowId().length);
                    });
                    let employee: any = _.find(selfParent.lstEmployee(), function(item: any) {
                        return item.id == employeeIdSelect.employeeId;
                    });
                    let param2 = {
                        typeDialog: 2,
                        param: {
                            workplaceId: employee.workplaceId
                        }
                    }
                    service.findAllCodeName(param2).done((data) => {
                        self.listCode([]);
                        self.listCode(_.map(data, 'code'));
                        nts.uk.ui.windows.setShared('kml001multiSelectMode', false);
                        nts.uk.ui.windows.setShared('kml001selectAbleCodeList', nts.uk.util.isNullOrEmpty(self.listCode()) ? [] : self.listCode());
                        nts.uk.ui.windows.setShared('kml001selectedCodeList', []);
                        nts.uk.ui.windows.sub.modal("/view/kdl/001/a/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                            let codes: any = nts.uk.ui.windows.getShared("kml001selectedCodeList");
                            if (codes) {
                                codeName = _.find(data, (item: any) => {
                                    return item.code == codes[0];
                                });
                                var objectName = {};
                                objectName["Name" + self.attendenceId] = codeName.name;
                                var objectCode = {};
                                objectCode["Code" + self.attendenceId] = codeName.code;

                                $.when($("#dpGrid").ntsGrid("updateRow", self.rowId(), objectName),
                                    $("#dpGrid").ntsGrid("updateRow", self.rowId(), objectCode)
                                ).done(() => {
                                    nts.uk.ui.block.clear();
                                    dfd.resolve();
                                })
                            }
                        });
                    });
                    dfd.promise()
                    break;
                case 3:
                    //KDL010 
                    nts.uk.ui.block.invisible();
                    nts.uk.ui.windows.setShared('KDL010SelectWorkLocation', self.selectedCode());
                    nts.uk.ui.windows.sub.modal("/view/kdl/010/a/index.xhtml", { dialogClass: "no-close" }).onClosed(() => {
                        var self = this;
                        var returnWorkLocationCD = nts.uk.ui.windows.getShared("KDL010workLocation");
                        if (returnWorkLocationCD !== undefined) {
                            let dataKDL: any;
                            let param3 = {
                                typeDialog: 3
                            }
                            service.findAllCodeName(param3).done((data: any) => {
                                codeName = _.find(data, (item: any) => {
                                    return item.code == returnWorkLocationCD;
                                });
                                var objectName = {};
                                objectName["Name" + self.attendenceId] = codeName.name;
                                var objectCode = {};
                                objectCode["Code" + self.attendenceId] = codeName.code;

                                $.when($("#dpGrid").ntsGrid("updateRow", self.rowId(), objectName),
                                    $("#dpGrid").ntsGrid("updateRow", self.rowId(), objectCode)
                                ).done(() => {
                                    nts.uk.ui.block.clear();
                                    dfd.resolve();
                                })
                            });
                        }
                        else {
                            nts.uk.ui.block.clear();
                        }
                    });
                    dfd.promise()
                    break;
                case 4:
                    //KDL032 
                    break;
                case 5:
                    //CDL008 
                    let dateCon = _.find(selfParent.dpData, (item: any) => {
                        return item.id == self.rowId().substring(1, self.rowId().length);
                    });
                    
                     let param5 = {
                            typeDialog: 5,
                            param: {
                                date: moment(dateCon.date)
                            }
                        }
                    let data5 : any;
                    let dfd1 = $.Deferred();
                    service.findAllCodeName(param5).done((data: any) => {
                        data5 = data;
                        codeName = _.find(data5, (item: any) => {
                            return item.code == self.selectedCode();
                        });
                        nts.uk.ui.windows.setShared('inputCDL008', {
                            selectedCodes: codeName == undefined ? "" : codeName.id,
                            baseDate: moment(dateCon.date),
                            isMultiple: false
                    }, true);
                        dfd1.resolve()
                    });
                    dfd1.promise();
                    nts.uk.ui.windows.sub.modal('com', '/view/cdl/008/a/index.xhtml').onClosed(function(): any {
                        // Check is cancel.
                        if (nts.uk.ui.windows.getShared('CDL008Cancel')) {
                            return;
                        }
                        //view all code of selected item 
                        var output = nts.uk.ui.windows.getShared('outputCDL008');
                            codeName = _.find(data5, (item: any) => {
                                return item.id == output;
                            });
                            var objectName = {};
                            objectName["Name" + self.attendenceId] = codeName.name;
                            var objectCode = {};
                            objectCode["Code" + self.attendenceId] = codeName.code;

                            $.when($("#dpGrid").ntsGrid("updateRow", self.rowId(), objectName),
                                $("#dpGrid").ntsGrid("updateRow", self.rowId(), objectCode)
                            ).done(() => {
                                nts.uk.ui.block.clear();
                                dfd.resolve();
                            })
                    })
                    break;
                case 6:
                    //KCP002
                    nts.uk.ui.windows.setShared('inputCDL003', {
                        selectedCodes: self.selectedCode(),
                        showNoSelection: false,
                        isMultiple: false
                    }, true);

                    nts.uk.ui.windows.sub.modal('com', '/view/cdl/003/a/index.xhtml').onClosed(function(): any {
                        //view all code of selected item 
                        var output = nts.uk.ui.windows.getShared('outputCDL003');
                        if (output) {
                            let param6 = {
                                typeDialog: 6
                            }
                            service.findAllCodeName(param6).done((data: any) => {
                                codeName = _.find(data, (item: any) => {
                                    return item.code == output;
                                });
                                var objectName = {};
                                objectName["Name" + self.attendenceId] = codeName.name;
                                var objectCode = {};
                                objectCode["Code" + self.attendenceId] = codeName.code;

                                $.when($("#dpGrid").ntsGrid("updateRow", self.rowId(), objectName),
                                    $("#dpGrid").ntsGrid("updateRow", self.rowId(), objectCode)
                                ).done(() => {
                                    nts.uk.ui.block.clear();
                                    dfd.resolve();
                                })
                            });
                        }
                    })
                    break;
                case 7:
                    //KCP003 
                    let dateCon7 = _.find(selfParent.dpData, (item: any) => {
                        return item.id == self.rowId().substring(1, self.rowId().length);
                    });
                     let param7 = {
                            typeDialog: 7,
                            param: {
                                date: moment(dateCon7.date)
                            }
                        }
                    let data7 : any;
                    let dfd7 = $.Deferred();
                    service.findAllCodeName(param7).done((data: any) => {
                        data7 = data;
                        codeName = _.find(data, (item: any) => {
                            return item.code == self.selectedCode();
                        });
                        nts.uk.ui.windows.setShared('inputCDL004', {
                            baseDate: moment(dateCon7.date),
                            selectedCodes: codeName == undefined ? "" : codeName.id,
                            showNoSelection: false,
                            isMultiple: false
                    }, true);
                         dfd7.resolve();
                    });
                    dfd7.promise();
                    nts.uk.ui.windows.sub.modal('com', '/view/cdl/004/a/index.xhtml').onClosed(function(): any {
                        var isCancel = nts.uk.ui.windows.getShared('CDL004Cancel');
                        if (isCancel) {
                            return;
                        }
                        var output = nts.uk.ui.windows.getShared('outputCDL004');
                            codeName = _.find(data7, (item: any) => {
                                return item.id == output;
                            });
                            var objectName = {};
                            objectName["Name" + self.attendenceId] = codeName.name;
                            var objectCode = {};
                            objectCode["Code" + self.attendenceId] = codeName.code;

                            $.when($("#dpGrid").ntsGrid("updateRow", self.rowId(), objectName),
                                $("#dpGrid").ntsGrid("updateRow", self.rowId(), objectCode)
                            ).done(() => {
                                nts.uk.ui.block.clear();
                                dfd.resolve();
                            })
                    })
                    break;
                case 8:
                    nts.uk.ui.windows.setShared('CDL002Params', {
                        isMultiple: false,
                        selectedCodes: self.selectedCode(),
                        showNoSelection: false
                    }, true);

                    nts.uk.ui.windows.sub.modal('com', '/view/cdl/002/a/index.xhtml').onClosed(function(): any {
                       // nts.uk.ui.block.clear();
                        var isCancel = nts.uk.ui.windows.getShared('CDL002Cancel');
                        if (isCancel) {
                            return;
                        }
                        var output = nts.uk.ui.windows.getShared('CDL002Output');
                        let param8 = {
                            typeDialog: 8,
                        }
                        service.findAllCodeName(param8).done((data: any) => {
                            nts.uk.ui.block.clear();
                            codeName = _.find(data, (item: any) => {
                                return item.code == output;
                            });
                            var objectName = {};
                            objectName["Name" + self.attendenceId] = codeName.name;
                            var objectCode = {};
                            objectCode["Code" + self.attendenceId] = codeName.code;

                            $.when($("#dpGrid").ntsGrid("updateRow", self.rowId(), objectName),
                                $("#dpGrid").ntsGrid("updateRow", self.rowId(), objectCode)
                            ).done(() => {
                                nts.uk.ui.block.clear();
                                dfd.resolve();
                            })
                        });
                    })
                    break;
            }
            nts.uk.ui.block.clear();
        }
    }
    export interface DPAttendanceItem {
        id: string;
        name: string;
        displayNumber: number;
        userCanSet: boolean;
        lineBreakPosition: number;
        attendanceAtr: number;
        /*DUTY(1, "勤務種類"),
        WORK_HOURS(2, "就業時間帯"), 
        SERVICE_PLACE(3, "勤務場所"), 
        REASON(4, "乖離理由"),
        WORKPLACE(5, "職場"),--
        CLASSIFICATION(6, "分類") 
        POSSITION(7, "職位")-- 
        EMPLOYMENT(8, "雇用区分") */
        typeGroup: number;
    }

    class InfoCellEdit {
        rowId: any;
        itemId: any;
        value: any;
        valueType: number;
        layoutCode: string;
        employeeId: string;
        date: any;
        typeGroup : number;
        constructor(rowId: any, itemId: any, value: any, valueType: number, layoutCode: string, employeeId: string, date: any, typeGroup: number) {
            this.rowId = rowId;
            this.itemId = itemId;
            this.value = value;
            this.valueType = valueType;
            this.layoutCode = layoutCode;
            this.employeeId = employeeId;
            this.date = date;
            this.typeGroup = typeGroup;
        }
    }
    
    class DataHoliday {
        compensation: string;
        substitute: string;
        paidYear: string;
        paidHalf: string;
        paidHours: string;
        fundedPaid: string;
        constructor(compensation: string, substitute: string, paidYear: string, paidHalf: string, paidHours: string, fundedPaid: string) {
            this.compensation = nts.uk.resource.getText("KDW003_8", [compensation])
            this.substitute = nts.uk.resource.getText("KDW003_8", [substitute])
            this.paidYear = nts.uk.resource.getText("KDW003_8", [paidYear])
            //            this.paidHalf = nts.uk.resource.getText("KDW003_10", paidHalf)
            //            this.paidHours = nts.uk.resource.getText("KDW003_11", paidHours)
            this.fundedPaid = nts.uk.resource.getText("KDW003_8", [fundedPaid])
        }
    }
        
    class ShareObject {
        changePeriodAtr: boolean; //期間を変更する có cho thay đổi khoảng thời gian hay không
        errorRefStartAtr: boolean; //エラー参照を起動する có hiện mode lỗi hay ko
        initClock: any; //打刻初期値-社員ID Optional giờ check tay SPR
        lstEmployee: any; //社員一覧 社員ID danh sách nhân viên được chọn
        screenMode: any; //画面モード-日別実績の修正の画面モード  mode approval hay ko 
        targetClosure: any; //処理締め-締めID targetClosure lấy closureId 
        transitionDesScreen: any; //遷移先の画面 - Optional //truyền từ màn hình nào sang

        dateTarget: any; //日付別で起動- Optional ngày extract mode 2
        displayFormat: any; //表示形式 mode hiển thị 
        individualStartUp: any; //個人別で起動 ngày bắt đầu
        lstExtratedEmployee: any;//抽出した社員一覧
        startDate: any;//期間 khoảng thời gian
        endDate: any;//期間 khoảng thời gian
        constructor() {
        }
        mapDataShare(dataInit: any, dataExtract: any) {
            var self = this;
            if (dataInit != undefined) {
                this.changePeriodAtr = dataInit.changePeriodAtr;
                this.errorRefStartAtr = dataInit.errorRefStartAtr;
                this.initClock = dataInit.initClock;
                this.lstEmployee = dataInit.lstEmployee;
                this.screenMode = dataInit.screenMode;
                this.targetClosure = dataInit.targetClosure;
                this.transitionDesScreen = dataInit.transitionDesScreen;
            }
            if (dataExtract != undefined) {
                this.dateTarget = moment(dataExtract.dateTarget, "YYYY/MM/DD");
                this.displayFormat = dataExtract.displayFormat;
                this.individualStartUp = dataExtract.individualStartUp;
                this.lstExtratedEmployee = dataExtract.lstExtratedEmployee;
                this.startDate = moment(dataExtract.startDate, "YYYY/MM/DD");;
                this.endDate = moment(dataExtract.endDate, "YYYY/MM/DD");
            }
        }
    }
}