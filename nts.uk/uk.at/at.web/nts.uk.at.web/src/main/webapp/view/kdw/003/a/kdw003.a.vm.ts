module nts.uk.at.view.kdw003.a.viewmodel {
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getText = nts.uk.resource.getText;
    import character = nts.uk.characteristics;
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
        { headerText: getText("KDW003_41"), key: 'date', dataType: 'String', width: '90px', ntsControl: 'Label' },
        { headerText: getText("KDW003_42"), key: 'sign', dataType: 'boolean', width: '35px', ntsControl: 'Checkbox' },
        { headerText: getText("KDW003_32"), key: 'employeeCode', dataType: 'String', width: '120px', ntsControl: 'Label' },
        { headerText: getText("KDW003_33"), key: 'employeeName', dataType: 'String', width: '190px', ntsControl: 'Label' },
        { headerText: '', key: "picture-person", dataType: "string", width: '35px', ntsControl: 'Image' }
    ];

    var CHECK_INPUT = {
        "759": "760", "760": "759", "761": "762",
        "762": "761", "763": "764", "764": "763",
        "765": "766", "766": "765", "157": "159",
        "159": "157", "163": "165", "165": "163",
        "169": "171", "171": "169",
        "175": "177", "177": "175", "181": "183",
        "183": "181", "187": "189", "189": "187",
        "193": "195", "195": "193", "199": "201",
        "201": "199", "205": "207", "207": "205",
        "211": "213", "213": "211",
        "7": "8", "8": "7", "9": "10",
        "10": "9", "11": "12", "12": "11",
        "13": "14", "14": "13", "15": "16",
        "16": "15",
        "17": "18", "18": "17", "19": "20",
        "20": "19", "21": "22", "22": "21",
        "23": "24", "24": "23", "25": "26",
        "26": "25"
    }

    var ITEM_CHANGE = [28, 29, 31, 34, 41, 44, 623, 625];

    var DEVIATION_REASON_MAP = { "438": 1, "443": 2, "448": 3, "453": 4, "458": 5, "801": 6, "806": 7, "811": 8, "816": 9, "821": 10 };

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
        headersGrid: any = [];
        columnSettings: KnockoutObservableArray<any> = ko.observableArray([]);
        sheetsGrid: KnockoutObservableArray<any> = ko.observableArray([]);
        fixColGrid: KnockoutObservableArray<any>;
        dailyPerfomanceData: KnockoutObservableArray<any> = ko.observableArray([]);
        cellStates: KnockoutObservableArray<any> = ko.observableArray([]);
        rowStates: KnockoutObservableArray<any> = ko.observableArray([]);
        dpData: Array<any> = [];
        headerColors: any = [];
        textColors: KnockoutObservableArray<any> = ko.observableArray([]);
        lstDate: KnockoutObservableArray<any> = ko.observableArray([]);
        optionalHeader: Array<any> = [];
        employeeModeHeader: Array<any> = [];
        dateModeHeader: Array<any> = [];
        errorModeHeader: Array<any> = [];
        formatCodes: KnockoutObservableArray<any> = ko.observableArray([]);
        autBussCode: KnockoutObservableArray<any> = ko.observableArray([]);
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
            { columnKey: 'employeeName', isFixed: true },
            { columnKey: 'picture-person', isFixed: true }
        ];
        errorModeFixCol: Array<any> = [
            { columnKey: 'id', isFixed: true },
            { columnKey: 'state', isFixed: true },
            { columnKey: 'error', isFixed: true },
            { columnKey: 'employeeCode', isFixed: true },
            { columnKey: 'employeeName', isFixed: true },
            { columnKey: 'date', isFixed: true },
            { columnKey: 'picture-person', isFixed: true }
        ];
        // date ranger component
        dateRanger: KnockoutObservable<any> = ko.observable(null);

        datePicker: KnockoutObservable<any> = ko.observable(null);
        // date picker component
        selectedDate: KnockoutObservable<any> = ko.observable(null);

        showButton: KnockoutObservable<AuthorityDetailModel> = ko.observable(null);

        referenceVacation: KnockoutObservable<ReferenceVacation> = ko.observable(new ReferenceVacation(false, false, false, false, false, null));

        employmentCode: KnockoutObservable<any> = ko.observable("");

        editValue: KnockoutObservableArray<InfoCellEdit> = ko.observableArray([]);

        itemValueAll: KnockoutObservableArray<any> = ko.observableArray([]);

        itemValueAllTemp: KnockoutObservableArray<any> = ko.observableArray([]);

        lockMessage: KnockoutObservable<any> = ko.observable("");

        dataHoliday: KnockoutObservable<DataHoliday> = ko.observable(new DataHoliday(null, null, null, null, null));
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
        comboItemsCompact: KnockoutObservableArray<any> = ko.observableArray([]);
        comboTimeLimit: KnockoutObservableArray<any> = ko.observableArray([]);
        showPrincipal: KnockoutObservable<any> = ko.observable(true);
        showSupervisor: KnockoutObservable<any> = ko.observable(true);
        dataAll: KnockoutObservable<any> = ko.observable(null);
        hasLstHeader: boolean = true;
        dPErrorDto: KnockoutObservable<any> = ko.observable();
        listCareError: KnockoutObservableArray<any> = ko.observableArray([]);
        listCareInputError: KnockoutObservableArray<any> = ko.observableArray([]);
        listCheckHolidays: KnockoutObservableArray<any> = ko.observableArray([]);
        listCheck28: KnockoutObservableArray<any> = ko.observableArray([]);
        listCheckDeviation: any = [];
        listErrorMonth: any = [];
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

        initScreenSPR: any = 0;
        showDateRange: KnockoutObservable<any> = ko.observable(true);

        flexShortage: KnockoutObservable<FlexShortage> = ko.observable(new FlexShortage(null, null, null));
        optionNoOfHolidays: any = {
            width: "70",
            decimallength: 1,
            unitID: 'DAYS'
        }
        calcFlex: KnockoutObservable<CalcFlex> = ko.observable(null);
        showFlex: KnockoutObservable<any> = ko.observable(false);
        breakTimeDay: KnockoutObservable<BreakTimeDay> = ko.observable(null);
        canFlex: KnockoutObservable<any> = ko.observable(false);
        lstErrorFlex: any = [];

        sprStampSourceInfo: KnockoutObservable<any> = ko.observable(null);

        itemMonth: any = [];
        itemValueMonthParent: any = {};
        valueUpdateMonth: any = null;
        valueFlexCheck: any;

        textStyles: any = [];
        showTextStyle: boolean = true;
        clickFromExtract: boolean = true;

        sprRemoveApprovalAll: any;

        dataSourceLoadOld: any = {};

        lstHeaderReceive: any;

        workTypeNotFound: any = [];

        isVisibleMIGrid: KnockoutObservable<boolean> = ko.observable(false);
        isStartScreen: KnockoutObservable<boolean> = ko.observable(true);
        listAttendanceItemId: KnockoutObservableArray<any> = ko.observableArray([]);
        monthYear: KnockoutObservable<string> = ko.observable(null);

        agreementInfomation: KnockoutObservable<AgreementInfomation> = ko.observable(new AgreementInfomation());
        ntsMControl: any = [];
        hasEmployee: boolean = true;

        lstDomainOld: any = [];
        lstDataSourceLoad: any;
        lstDomainEdit: any = [];

        flagCalculation: boolean = false;

        showLock: KnockoutObservable<boolean> = ko.observable(true);
        unLock: KnockoutObservable<boolean> = ko.observable(false);

        itemChange: any = [];

        characteristics: Characteristics = {};
        loadFirst: boolean = true;
        
        clickCounter: CLickCount = new CLickCount();
        
        itemInputName: any = [];

        constructor(dataShare: any) {
            var self = this;

            self.initLegendButton();
            self.initDateRanger();
            self.initDisplayFormat();
            self.headersGrid = self.employeeModeHeader;
            self.fixColGrid = ko.observableArray(self.employeeModeFixCol);
            // show/hide header number
            self.showHeaderNumber.subscribe((val) => {
                if (!self.loadFirst) {
                    self.characteristics.showNumberHeader = val;
                    character.save('characterKdw003a', self.characteristics);
                    self.dislayNumberHeaderText();
                }
            });
            // show/hide profile icon
            self.showProfileIcon.subscribe((val) => {
                //if (self.displayFormat() == 1 || self.displayFormat() ==2) {
                if (!self.loadFirst) {
                    self.characteristics.showProfile = val;
                    character.save('characterKdw003a', self.characteristics);
                    self.displayProfileIcon(self.displayFormat());
                    // }
                }
            });

            self.displayWhenZero.subscribe(val => {
                //self.displayWhenZero();
                if (!self.loadFirst) {
                    self.characteristics.showZero = val;
                    character.save('characterKdw003a', self.characteristics);
                    self.displayNumberZero();
                }
            });
            
            self.selectedDirection.subscribe((value) => {
                if (!self.loadFirst) {
                    if (value == 0) {
                        $("#dpGrid").mGrid("directEnter", "below");
                         self.characteristics.moveMouse = 0;
                    } else {
                        $("#dpGrid").mGrid("directEnter", "right");
                         self.characteristics.moveMouse = 1;
                    }
                   character.save('characterKdw003a', self.characteristics);
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
                self.shareObject().mapDataShare(dataShare.initParam, dataShare.extractionParam, dataShare.dataSPR);
                self.showDateRange(self.shareObject().changePeriodAtr);
            }

            //            self.flexShortage.subscribe((val:any) => {
            //            });
            self.flexShortage.valueHasMutated();
            self.isVisibleMIGrid.subscribe((value) => {
                if (value) {
                    self.getNameMonthly();
                }
            });

            $(".grid-container").attr('style', 'height: ' + (1038) + 'px !IMPORTANT');

            self.dataHoliday.subscribe(val => {
                if (val.dispCompensationDay || val.dispCompensationTime)
                    $('#fixed-table td:nth-child(1), #fixed-table th:nth-child(1)').show();
                else
                    $('#fixed-table td:nth-child(1), #fixed-table th:nth-child(1)').hide();
                if (val.dispSubstitute)
                    $('#fixed-table td:nth-child(2), #fixed-table th:nth-child(2)').show();
                else
                    $('#fixed-table td:nth-child(2), #fixed-table th:nth-child(2)').hide();
                if (val.dispAnnualDay || val.dispAnnualTime)
                    $('#fixed-table td:nth-child(3), #fixed-table th:nth-child(3)').show();
                else
                    $('#fixed-table td:nth-child(3), #fixed-table th:nth-child(3)').hide();
                if (val.dispReserve)
                    $('#fixed-table td:nth-child(4), #fixed-table th:nth-child(4)').show();
                else
                    $('#fixed-table td:nth-child(4), #fixed-table th:nth-child(4)').hide();
            });
            $(window).on('resize', function() {
                var win = $(this); //this = window
                //$(".grid-container").attr('style', 'height: ' + (win.height() -150) + 'px !IMPORTANT');
            });
        }
        
        helps(event, data) {
            var self = this;
            $('#tooltip').css({
                'left': event.pageX + 15,
                'top': event.pageY - 12,
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
                    { colorCode: '#FD4D4D', labelText: getText("KDW003_44") },
                    { colorCode: '#F6F636', labelText: getText("KDW003_45") },
                ]
            };
        }

        initDateRanger() {
            var self = this;
            self.dateRanger.subscribe((dateRange) => {
                if (dateRange && dateRange.startDate && dateRange.endDate) {

                    var elementDate = dateRange.startDate;
                    //                    if (moment(elementDate, "YYYY/MM/DD").isValid()) {
                    //                        while (!moment(elementDate, "YYYY/MM/DD").isAfter(moment(dateRange.endDate, "YYYY/MM/DD"))) {
                    //                            self.lstDate.push({ date: elementDate });
                    //                            elementDate = moment(elementDate, "YYYY/MM/DD").add(1, 'd').format("YYYY/MM/DD");
                    //                        }
                    //                    }
                    if (self.displayFormat() == 1) {
                        self.datePicker().startDate = dateRange.startDate;
                        self.datePicker().endDate = dateRange.endDate;
                        self.datePicker.valueHasMutated();
                    }
                }
            });
            self.datePicker.subscribe((dateRange) => {
                if (dateRange && dateRange.startDate && dateRange.endDate) {
                    //                    setTimeout(function (){
                    self.selectedDate(dateRange.startDate);
                    //                    }, 10);
                }
            });
            self.datePicker({
                startDate: moment().add(-1, "M").add(1, "d").format("YYYY/MM/DD"),
                endDate: moment().format("YYYY/MM/DD")
            });
            self.dateRanger({
                startDate: moment().add(-1, "M").add(1, "d").format("YYYY/MM/DD"),
                endDate: moment().format("YYYY/MM/DD")
            });
        }

        initDisplayFormat() {
            var self = this;
            self.displayFormatOptions = ko.observableArray([
                { code: 0, name: getText("Enum_DisplayFormat_Individual") },
                { code: 1, name: getText("Enum_DisplayFormat_ByDate") },
                { code: 2, name: getText("Enum_DisplayFormat_ErrorAlarm") }
            ]);
            self.displayFormat(0);
        }

        createSumColumn(data: any) {
            var self = this;
            _.each(data.lstControlDisplayItem.columnSettings, function(item) {
                if (self.displayFormat() == 0) {
                    if (item.columnKey == "date") {
                        item['summaryCalculator'] = "合計";
                    }
                } else {
                    if (item.columnKey == "employeeCode") {
                        item.allowSummaries = true;
                        item['summaryCalculator'] = "合計";
                    }
                }
                if (item.typeFormat != null && item.typeFormat != undefined) {
                    if (item.typeFormat == 2) {
                        //so lan
                        item['summaryCalculator'] = "Number";
                    }
                    else if (item.typeFormat == 5) {
                        //thoi gian
                        item['summaryCalculator'] = "Time"
                    }
                    else if (item.typeFormat == 3) {
                        //so tien 
                        item['summaryCalculator'] = "Number"
                    } else {
                        if (item['summaryCalculator'] != "合計") item['summaryCalculator'] = ""
                    }
                } else {
                    item['summaryCalculator'] = ""
                }
                delete item.typeFormat;
                self.columnSettings(data.lstControlDisplayItem.columnSettings);
            });
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            character.restore("characterKdw003a").done((obj: Characteristics) => {
                if (obj != undefined && __viewContext.user.employeeId == obj.employeeId && __viewContext.user.companyId == obj.companyId) {
                    self.characteristics = obj;
                    self.showHeaderNumber(obj.showNumberHeader);
                    self.showProfileIcon(obj.showProfile);
                    self.displayWhenZero(obj.showZero);

                    self.displayFormat(obj.formatExtract);

                    self.formatCodes(obj.authenSelectFormat);
                    
                    self.selectedDirection(obj.moveMouse)
                }

                var param = {
                    dateRange: dateRangeParam ? {
                        startDate: moment(dateRangeParam.startDate).utc().toISOString(),
                        endDate: moment(dateRangeParam.endDate).utc().toISOString()
                    } : null,
                    displayFormat: _.isEmpty(self.shareObject()) ? (_.isEmpty(self.characteristics) ? 0 : self.characteristics.formatExtract) : self.shareObject().displayFormat,
                    initScreen: _.isEmpty(self.characteristics) ? 0 : 1,
                    mode: _.isEmpty(self.shareObject()) ? 0 : self.shareObject().screenMode,
                    lstEmployee: [],
                    formatCodes: self.formatCodes(),
                    objectShare: _.isEmpty(self.shareObject()) ? null : self.shareObject(),
                    showError: _.isEmpty(self.shareObject()) ? null : self.shareObject().errorRefStartAtr
                };
                // delete grid in localStorage
                self.deleteGridInLocalStorage();

                let dateRangeParam = nts.uk.ui.windows.getShared('DateRangeKDW003');
                if (!(_.isEmpty(self.shareObject()))) {
                    self.displayFormat(param.displayFormat);
                    if (self.shareObject().transitionDesScreen == undefined || self.shareObject().transitionDesScreen == null) {
                        $("#back-navigate").css("visibility", "hidden");
                    }
                } else {
                    self.displayFormat(param.displayFormat);
                    $("#back-navigate").css("visibility", "hidden");
                }
                self.hideComponent();
                self.showTextStyle = true;
                character.restore("characterKdw003a").done((obj: Characteristics) => {
                    self.characteristics.employeeId = __viewContext.user.employeeId;
                    self.characteristics.companyId = __viewContext.user.companyId;
                    self.characteristics.showZero = self.displayWhenZero();
                    self.characteristics.showProfile = self.showProfileIcon();
                    self.characteristics.showNumberHeader = self.showHeaderNumber();
                    self.characteristics.formatExtract = param.displayFormat;
                    self.characteristics.authenSelectFormat = param.formatCodes;
                    self.characteristics.moveMouse = self.selectedDirection();
                    character.save('characterKdw003a', self.characteristics);
                });
                nts.uk.ui.block.invisible();
                nts.uk.ui.block.grayout();
                service.startScreen(param).done((data) => {
                    //self.processMapData(data);
                    if (data.lstEmployee == undefined || data.lstEmployee.length == 0) {
                        nts.uk.ui.dialog.alert({ messageId: "Msg_1342" }).then(function() {
                            self.hasEmployee = false;
                            nts.uk.ui.block.clear();
                            dfd.resolve({ bindDataMap: true, data: data });
                        });
                    } else {
                        dfd.resolve({ bindDataMap: true, data: data });
                    }
                }).fail(function(error) {
                    if (error.messageId == "Msg_672") {
                        nts.uk.ui.dialog.info({ messageId: "Msg_672" });
                    } else if (error.messageId != undefined && error.messageId != "KDW/003/a") {
                        nts.uk.ui.dialog.alert(error.messageId == "Msg_1430" ? error.message : { messageId: error.messageId }).then(function() {
                            nts.uk.request.jumpToTopPage();
                        });

                    } else if ((error.messageId == undefined && error.errors.length > 0)) {
                        nts.uk.ui.dialog.bundledErrors({ errors: error.errors }).then(function() {
                            nts.uk.request.jumpToTopPage();
                        });
                    } else {
                        setShared("selectedPerfFmtCodeList", "");
                        modal("/view/kdw/003/c/index.xhtml").onClosed(() => {
                            var dataTemp = nts.uk.ui.windows.getShared('KDW003C_Output');
                            if (dataTemp != undefined) {
                                let data = [dataTemp];

                                let param = {
                                    dateRange: dateRangeParam ? {
                                        startDate: moment(dateRangeParam.startDate).utc().toISOString(),
                                        endDate: moment(dateRangeParam.endDate).utc().toISOString()
                                    } : null,
                                    displayFormat: _.isEmpty(self.shareObject()) ? (_.isEmpty(self.characteristics) ? 0 : self.characteristics.formatExtract) : self.shareObject().displayFormat,
                                    initScreen: _.isEmpty(self.characteristics) ? 0 : 1,
                                    mode: _.isEmpty(self.shareObject()) ? 0 : self.shareObject().screenMode,
                                    lstEmployee: [],
                                    formatCodes: data,
                                    objectShare: _.isEmpty(self.shareObject()) ? null : self.shareObject()
                                };
                                self.characteristics.authenSelectFormat = param.formatCodes;
                                character.save('characterKdw003a', self.characteristics);
                                nts.uk.ui.block.invisible();
                                nts.uk.ui.block.grayout();
                                service.startScreen(param).done((data) => {
                                    //self.processMapData(data);
                                    nts.uk.ui.block.clear();
                                    dfd.resolve({ bindDataMap: true, data: data });
                                }).fail(function(error) {
                                    if (error.messageId != undefined) {
                                        nts.uk.ui.dialog.alert({ messageId: error.messageId }).then(function() {
                                            nts.uk.request.jumpToTopPage();
                                        });

                                    } else if ((error.messageId == undefined && error.errors.length > 0)) {
                                        nts.uk.ui.dialog.bundledErrors({ errors: error.errors }).then(function() {
                                            nts.uk.request.jumpToTopPage();
                                        });

                                        //nts.uk.ui.dialog.alert(error.message);
                                        nts.uk.ui.block.clear();
                                    };
                                });
                            };
                        });

                    };
                });
            })
            return dfd.promise();
        }

        processMapData(data) {
            var self = this;
            let startTime: number = performance.now();
            console.log(data);
            self.loadFirst = false;
            //self.lstDomainOld = data.domainOld;
            //self.lstDomainEdit = _.cloneDeep(data.domainOld);
            if (data.typeBussiness != localStorage.getItem('kdw003_type')) {
                localStorage.removeItem(window.location.href + '/dpGrid');
            }
            localStorage.setItem('kdw003_type', data.typeBussiness);
            self.dateRanger().startDate = data.dateRange.startDate;
            self.dateRanger().endDate = data.dateRange.endDate;
            self.dateRanger.valueHasMutated();
            //if(self.displayFormat() == 1) //self.selectedDate(data.dateRange.startDate);
            // pair name input
            self.itemInputName = data.lstControlDisplayItem.itemInputName;
            self.dataAll(data);
            self.itemValueAll(data.itemValues);
            self.comment(data.comment != null ? '■ ' + data.comment : null);
            self.formatCodes(data.lstControlDisplayItem.formatCode);
            self.autBussCode(data.autBussCode);
            self.createSumColumn(data);
            // combo box
            self.comboItemsCalc(data.lstControlDisplayItem.comboItemCalc);
            self.comboItemsReason(data.lstControlDisplayItem.comboItemReason);
            self.comboItemsDoWork(data.lstControlDisplayItem.comboItemDoWork);
            self.comboItemsCompact(data.lstControlDisplayItem.comboItemCalcCompact);
            self.comboTimeLimit(data.lstControlDisplayItem.comboTimeLimit);

            self.employmentCode(data.employmentCode);
            self.lstAttendanceItem(data.lstControlDisplayItem.lstAttendanceItem);
            let showCheckbox = _.isEmpty(self.shareObject()) ? data.showPrincipal : data.showSupervisor;
            self.showButton(new AuthorityDetailModel(data.authorityDto, data.lstControlDisplayItem.settingUnit, showCheckbox));
            self.showLock(self.showButton().available12());
            self.unLock(false);
            //            self.referenceVacation(new ReferenceVacation(data.yearHolidaySettingDto == null ? false : data.yearHolidaySettingDto.manageAtr, data.substVacationDto == null ? false : data.substVacationDto.manageAtr, data.compensLeaveComDto == null ? false : data.compensLeaveComDto.manageAtr, data.com60HVacationDto == null ? false : data.com60HVacationDto.manageAtr, self.showButton()));
            // Fixed Header
            self.fixHeaders(data.lstFixedHeader);
            self.showPrincipal(data.showPrincipal);
            self.showSupervisor(data.showSupervisor);
            self.lstHeaderReceive = _.cloneDeep(data.lstControlDisplayItem.lstHeader);
            if (data.lstControlDisplayItem.lstHeader.length == 0) self.hasLstHeader = false;
            if (self.showPrincipal() || data.lstControlDisplayItem.lstHeader.length == 0) {
                self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3], self.fixHeaders()[4]];
                self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[7], self.fixHeaders()[4]];
                self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[7], self.fixHeaders()[4]];
            } else {
                self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3]];
                self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[7]];
                self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[7]];
            }
            if (self.showSupervisor()) {
                self.employeeModeHeader.push(self.fixHeaders()[8]);
                self.dateModeHeader.push(self.fixHeaders()[8]);
                self.errorModeHeader.push(self.fixHeaders()[8]);
            }
            self.lstEmployee(_.orderBy(data.lstEmployee, ['code'], ['asc']));
            self.receiveData(data);
            //                let employeeLogin: any = _.find(self.lstEmployee(), function(data){
            //                    return data.loginUser == true;
            //                }); 
            self.employIdLogin = __viewContext.user.employeeId;
            self.selectedEmployee(_.isEmpty(self.shareObject()) ? self.employIdLogin : (self.shareObject().displayFormat == 0 ? self.shareObject().individualTarget : (self.lstEmployee().length == 0 ? "" : self.lstEmployee()[0].id)));
            self.initCcg001();
            self.loadCcg001();
            if (!self.hasEmployee) return;
            self.loadKcp009();
            self.showTighProcess(data.showTighProcess);
            self.extractionData();
            console.log("khoi tao Object: " + (performance.now() - startTime));
            self.loadGrid();
            console.log("thoi gian load grid: " + (performance.now() - startTime));
            //  self.extraction();
            console.log("thoi gian load ccg: " + (performance.now() - startTime));
            // no20
            self.dPErrorDto(data.dperrorDto);
            self.displayNumberZero();
            self.displayProfileIcon(self.displayFormat());
            self.dislayNumberHeaderText();
            console.log("thoi gian load 0: " + (performance.now() - startTime));
            //set SPR
            if (data.showErrorDialog) {
                self.showErrorDialog();
            }
            //alert("time load ALL: "+ (performance.now() - startTime));
        }

        loadRemainNumberTable() {
            let self = this;
            service.getRemainNum(self.selectedEmployee()).done((data: any) => {
                self.dataHoliday(new DataHoliday(data.annualLeave, data.reserveLeave, data.compensatoryLeave, data.substitutionLeave, data.nextGrantDate));
                self.referenceVacation(
                    new ReferenceVacation(
                        data.annualLeave == null ? false : data.annualLeave.manageYearOff,
                        data.reserveLeave == null ? false : data.reserveLeave.manageRemainNumber,
                        data.substitutionLeave == null ? false : data.substitutionLeave.manageAtr,
                        data.compensatoryLeave == null ? false : data.compensatoryLeave.manageCompenLeave,
                        data.com60HVacation == null ? false : data.com60HVacation.manageAtr,
                        self.showButton()));
            });
        }

        setSprFromItem(data: any){
            let self = this;
            if (!_.isEmpty(self.shareObject()) && self.shareObject().initClock != null && self.initScreenSPR == 0) {
                if (data.showQuestionSPR == SPRCheck.SHOW_CONFIRM) {
                    let sprStamp = { employeeId: "", date: "", change31: false, change34: false };
                    // show dialog confirm 
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_1214" }).ifYes(() => {
                        // update check
                        if (data.changeSPR.change31) {
                            //let objectName = {};
                            //                            objectName["A31"] = "" + self.convertMinute(self.shareObject().initClock.goOut);
                            //                            $("#dpGrid").ntsGrid("updateRow", "_" + data.changeSPR.rowId31, objectName);
                            //$("#dpGrid").mGrid("updateCell", "_" + data.changeSPR.rowId31, "A31", self.convertMinute(self.shareObject().initClock.goOut));
                            self.updateCellSpr("_" + data.changeSPR.rowId31, "A31", self.convertMinute(self.shareObject().initClock.goOut));
                            sprStamp.change31 = true;
                        }

                        if (data.changeSPR.change34) {
                            //let objectName = {};
                            //objectName["A34"] = "" + self.convertMinute(self.shareObject().initClock.liveTime);
                            self.updateCellSpr("_" + data.changeSPR.rowId34, "A34", self.convertMinute(self.shareObject().initClock.liveTime));
                            //$("#dpGrid").ntsGrid("updateRow", "_" + data.changeSPR.rowId34, objectName);
                            sprStamp.change34 = true;
                        }

                        if (data.changeSPR.showPrincipal) {
                            //let objectName = {};
                            //objectName["sign"] = false;
                            $("#dpGrid").mGrid("updateCell", "_" + data.changeSPR.rowId31, "sign", false);
                            //$("#dpGrid").ntsGrid("updateRow", "_" + data.changeSPR.rowId31, objectName);
                        }

                        if (data.changeSPR.showSupervisor) {
                            //let objectName = {};
                            //objectName["approval"] = false;
                            // $("#dpGrid").ntsGrid("updateRow", "_" + data.changeSPR.rowId31, objectName);
                            $("#dpGrid").mGrid("updateCell", "_" + data.changeSPR.rowId31, "approval", false);
                            self.sprRemoveApprovalAll = "_" + data.changeSPR.rowId31;
                        }
                        if ((data.changeSPR.change31 || data.changeSPR.change34) && self.shareObject().initClock.canEdit) {
                            self.sprStampSourceInfo(sprStamp);
                        }
                    });
                } else if (data.showQuestionSPR == SPRCheck.INSERT) {
                    let sprStamp = { employeeId: "", date: "", change31: false, change34: false };
                    if (data.changeSPR.change31) {
                        //let objectName = {};
                        //objectName["A31"] = "" + self.convertMinute(self.shareObject().initClock.goOut);
                        //$("#dpGrid").ntsGrid("updateRow", "_" + data.changeSPR.rowId31, objectName);
                        self.updateCellSpr("_" + data.changeSPR.rowId31, "A31", self.convertMinute(self.shareObject().initClock.goOut));
                        sprStamp.change31 = true;
                    }

                    if (data.changeSPR.change34) {
                        //let objectName = {};
                        //objectName["A34"] = "" + self.convertMinute(self.shareObject().initClock.liveTime);
                        //$("#dpGrid").ntsGrid("updateRow", "_" + data.changeSPR.rowId34, objectName);
                        self.updateCellSpr("_" + data.changeSPR.rowId34, "A34", self.convertMinute(self.shareObject().initClock.liveTime));
                        sprStamp.change34 = true;
                    }
                    if ((data.changeSPR.change31 || data.changeSPR.change34) && self.shareObject().initClock.canEdit) {
                        self.sprStampSourceInfo(sprStamp);
                    }
                }
                //update
            }
        }
        
        updateCellSpr(rowId: any, item: any, value: any) {
            let self = this;
            let dfd = $.Deferred();
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            $("#dpGrid").mGrid("updateCell", rowId, item, value);
            self.inputProcess(rowId, item, value).done(value => {
                _.each(value.cellEdits, itemResult => {
                    $("#dpGrid").mGrid("updateCell", itemResult.id, itemResult.item, itemResult.value, true);
                })
                nts.uk.ui.block.clear();
                dfd.resolve();
            });
            return dfd.promise();
        }

        convertMinute(value): string {
            return Math.floor(value / 60) + ':' + value % 60;
        }

        processFlex(data, showListError): JQueryPromise<any> {
            let dfd = $.Deferred(),
                self = this;
            if (self.displayFormat() === 0) {
                self.loadRemainNumberTable();
            }
            let monthResult = data.monthResult;
            if (monthResult != null) {
                // set agreementInfo
                self.agreementInfomation().mapDataAgreement(monthResult.agreementInfo);
                let listFormatDaily: any[] = monthResult.formatDaily;
                self.listAttendanceItemId((monthResult.results != null && monthResult.results.length != 0) ? monthResult.results[0].items : []);

                _.each(listFormatDaily, (item) => {
                    let formatDailyItem = _.find(self.listAttendanceItemId(), { 'itemId': item.attendanceItemId });
                    if (formatDailyItem) {
                        formatDailyItem['columnWidth'] = (!_.isNil(item) && !!item.columnWidth) ? item.columnWidth : 100;
                        formatDailyItem['order'] = item.order;
                    }
                });
                let arr: any[] = _.orderBy(self.listAttendanceItemId(), ['order'], ['asc']);
                self.listAttendanceItemId(arr);
                self.monthYear(nts.uk.time.formatYearMonth(data.monthResult.month));
                // reload MiGrid
                // delete localStorage miGrid
                localStorage.removeItem(window.location.href + '/miGrid');
                self.isVisibleMIGrid(data.monthResult.hasItem);
                self.isVisibleMIGrid.valueHasMutated();
                //
            } else {
                self.agreementInfomation().mapDataAgreement({ showAgreement: false });
            }

            if (monthResult != null && monthResult.flexShortage != null && monthResult.flexShortage.showFlex && self.displayFormat() == 0) {
                self.showFlex(true);
                self.canFlex(monthResult.flexShortage.canflex);
                self.breakTimeDay(monthResult.flexShortage.breakTimeDay);
                self.calcFlex(new CalcFlex(monthResult.flexShortage.value18, monthResult.flexShortage.value19, monthResult.flexShortage.value21, monthResult.flexShortage.value189, monthResult.flexShortage.value190, monthResult.flexShortage.value191));
                self.itemMonth = [];
                self.valueFlexCheck = monthResult.flexShortage.calc;
                let fst: FlexShortage = self.flexShortage();

                //fst.parent = ko.observable(self);
                fst.bindDataChange(ko.toJS(self.calcFlex), monthResult.flexShortage.redConditionMessage, monthResult.flexShortage.messageNotForward, monthResult.flexShortage.error, monthResult.flexShortage.messageError, showListError && self.canFlex())
                // set error dialog b
                //if(self.canFlex()) self.lstErrorFlex = data.monthResult.flexShortage.messageError;
                self.itemMonthLayout(data);
                self.itemValueMonthParent = data.monthResult.flexShortage.monthParent;
                self.valueUpdateMonth = self.itemValueMonthParent;
                self.valueUpdateMonth["redConditionMessage"] = monthResult.flexShortage.redConditionMessage;
                //co can kiem tra loi flex ko
                self.valueUpdateMonth["hasFlex"] = true && self.canFlex();
                dfd.resolve();
                nts.uk.ui.block.clear();
                //self.flexShortage(new FlexShortage(self, self.calcFlex(),  self.breakTimeDay()));
                // アルゴリズム「フレックス不足の相殺が実施できるかチェックする」
            } else {
                self.showFlex(false);
                self.lstErrorFlex = [];
                if (self.displayFormat() === 0) {
                    self.itemValueMonthParent = data.monthResult == undefined ? {} : data.monthResult.flexShortage.monthParent;
                    self.valueUpdateMonth = self.itemValueMonthParent;
                    self.valueUpdateMonth["hasFlex"] = false;
                }
                nts.uk.ui.block.clear();
                dfd.resolve();
            }
            if(self.displayFormat() === 0) self.valueUpdateMonth["needCallCalc"] = (data.monthResult == null || data.monthResult == undefined) ?  false : data.monthResult.needCallCalc;
            return dfd.promise();
        }

        itemMonthLayout(data: any) {
            let self = this;
            self.itemMonth = [];
            //self.itemMonth.push(data.flexShortage.value18);
            self.itemMonth.push(data.monthResult.flexShortage.value189);
            self.itemMonth.push(data.monthResult.flexShortage.value190);
        }
        receiveData(data) {
            var self = this;
            self.dpData = data.lstData;
            self.cellStates(data.lstCellState);
            self.optionalHeader = data.lstControlDisplayItem.lstHeader;
            self.sheetsGrid(data.lstControlDisplayItem.lstSheet);
            self.sheetsGrid.valueHasMutated();
            if (self.showTextStyle || self.clickFromExtract) {
                self.textStyles = data.textStyles;
            }
        }

        proceed() {
            var self = this;
            this.insertUpdate();
        }

        proceedSave() {
            var self = this;
            this.insertUpdate();
        }

        insertUpdate() {
            var self = this;
            if (!self.hasEmployee) return;
            if (self.dialogShow != undefined && self.dialogShow.$dialog != null) {
                self.dialogShow.close();
            }
            if (self.workTypeNotFound.length > 0) {
                self.showErrorDialog();
                return;
            }
            // insert flex
            let errorGrid: any = $("#dpGrid").mGrid("errors");
            let checkDataCare: boolean = true;
            if (errorGrid == undefined || errorGrid.length == 0) {
                nts.uk.ui.block.invisible();
                nts.uk.ui.block.grayout();
                self.listCareError([]);
                self.listCareInputError([]);
                self.listCheckHolidays([]);
                self.listCheck28([]);
                self.listCheckDeviation = [];
                self.listErrorMonth = [];
                let dataChange: any = $("#dpGrid").mGrid("updatedCells");
                var dataSource = $("#dpGrid").mGrid("dataSource");
                let dataChangeProcess: any = [];
                let dataCheckSign: any = [];
                let dataCheckApproval: any = [];
                let sprStampSourceInfo: any = null;
                _.each(dataChange, (data: any) => {
                    let dataTemp = _.find(dataSource, (item: any) => {
                        return item.id == data.rowId;
                    });

                    if (data.columnKey != "sign" && data.columnKey != "approval") {
                        if (data.columnKey.indexOf("Code") == -1 && data.columnKey.indexOf("NO") == -1) {
                            if (data.columnKey.indexOf("Name") != -1) {
                            } else {

                                // check itemCare 
                                let groupCare = self.checkItemCare(Number(data.columnKey.substring(1, data.columnKey.length)));
                                if (groupCare == 0 || groupCare == 1 || groupCare == 2) {
                                    if (self.checkErrorData(groupCare, data, dataSource) == false || self.listCareInputError().length > 0) {
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
                    } else {
                        if (data.columnKey == "sign") {
                            dataCheckSign.push({ rowId: data.rowId, itemId: "sign", value: data.value, employeeId: dataTemp.employeeId, date: dataTemp.dateDetail.utc().toISOString(), flagRemoveAll: false });
                        } else {
                            let flag = false;
                            if (data.rowId == self.sprRemoveApprovalAll) {
                                flag = true;
                                self.sprRemoveApprovalAll = null;
                            }
                            dataCheckApproval.push({ rowId: data.rowId, itemId: "approval", value: data.value, employeeId: dataTemp.employeeId, date: dataTemp.dateDetail.utc().toISOString(), flagRemoveAll: flag });
                        }
                    }
                });
                if (!_.isEmpty(self.shareObject()) && self.shareObject().initClock != null && self.initScreenSPR == 0) {
                    if (self.sprStampSourceInfo() != null) {
                        sprStampSourceInfo = self.sprStampSourceInfo();
                        sprStampSourceInfo.employeeId = self.shareObject().initClock.employeeId;
                        sprStampSourceInfo.date = self.shareObject().initClock.dateSpr.utc().toISOString();
                    }
                }
                let dataParent = {
                    itemValues: dataChangeProcess,
                    dataCheckSign: dataCheckSign,
                    dataCheckApproval: dataCheckApproval,
                    mode: self.displayFormat(),
                    spr: sprStampSourceInfo,
                   // dailyOlds: self.lstDomainOld,
                   //dailyEdits: self.lstDomainEdit,
                    flagCalculation: self.flagCalculation
                }
                if (self.displayFormat() == 0) {
                    if (!_.isEmpty(self.shareObject()) && self.shareObject().initClock != null) {
                        dataParent["employeeId"] = self.shareObject().initClock.employeeId;
                        dataParent["dateRange"] = { startDate: self.shareObject().initClock.dateSpr.utc(), endDate: self.shareObject().initClock.dateSpr.utc() };
                    } else {
                        dataParent["employeeId"] = dataSource.length > 0 ? dataSource[0].employeeId : null;
                        dataParent["dateRange"] = dataSource.length > 0 ? { startDate: dataSource[0].dateDetail, endDate: dataSource[dataSource.length - 1].dateDetail } : null;
                    }
                    dataParent["monthValue"] = self.valueUpdateMonth;
                }

                let checkDailyChange = (dataChangeProcess.length > 0 || dataCheckSign.length > 0 || dataCheckApproval.length > 0) && checkDataCare;
                if (checkDailyChange || (self.valueUpdateMonth != null && self.valueUpdateMonth.items) || self.flagCalculation) {
                    let dfd = $.Deferred();
                    service.addAndUpdate(dataParent).done((dataAfter) => {
                        // alert("done");
                        dataChange = {};
                        let errorFlex = false;
                        if (!_.isEmpty(dataAfter.flexShortage)) {
                            if (dataAfter.flexShortage.error && dataAfter.flexShortage.messageError.length != 0) {
                                $("#next-month").ntsError("clear");
                                _.each(dataAfter.flexShortage.messageError, value => {
                                    $("#next-month").ntsError("set", value.message, value.messageId);
                                });
                                errorFlex = true;
                            } else {
                                $("#next-month").ntsError("clear");
                            }
                        }

                        if (_.isEmpty(dataAfter.errorMap) && dataAfter.errorMap[5] == undefined && !errorFlex) {
                            if (self.valueUpdateMonth != null || self.valueUpdateMonth != undefined) {
                                self.valueUpdateMonth.items = [];
                            }
                            self.initScreenSPR = 1;
                            self.clickFromExtract = false;
                            self.showTextStyle = false;
                            if (!self.flagCalculation) {
                                if (checkDailyChange) {
                                    // self.reloadScreen();
                                    self.loadRowScreen(false, false);
                                } else {
                                    self.loadRowScreen(true, false);
                                    //nts.uk.ui.block.clear();
                                }
                            }else{
                              self.loadRowScreen(false, true);
                            }
                        } else {
                            nts.uk.ui.block.clear();
                            let errorAll = false;
                            if (dataAfter.errorMap[0] != undefined) {
                                self.listCareError(dataAfter.errorMap[0])
                                // nts.uk.ui.dialog.alertError({ messageId: "Msg_996" })
                                errorAll = true;
                            }
                            if (dataAfter.errorMap[1] != undefined) {
                                self.listCareInputError(dataAfter.errorMap[1])
                                // nts.uk.ui.dialog.alertError({ messageId: "Msg_1108" })
                                errorAll = true;
                            }
                            if (dataAfter.errorMap[2] != undefined) {
                                self.listCheckHolidays(dataAfter.errorMap[2]);
                                if (self.valueUpdateMonth != null || self.valueUpdateMonth != undefined) {
                                    self.valueUpdateMonth.items = [];
                                }
                                self.initScreenSPR = 1;
                                self.clickFromExtract = false;
                                self.showTextStyle = false;
                                self.loadRowScreen(false, self.flagCalculation);
                                errorAll = true;
                            }

                            if (dataAfter.errorMap[3] != undefined) {
                                self.listCheck28(dataAfter.errorMap[3]);
                                errorAll = true;
                            }

                            if (dataAfter.errorMap[4] != undefined) {
                                self.listCheckDeviation = dataAfter.errorMap[4];
                                errorAll = true;
                            }
                            
                            if (dataAfter.errorMap[5] != undefined) {
                                self.listErrorMonth = dataAfter.errorMap[5];
                                errorAll = true;
                            }
                            if(errorAll) self.showErrorDialog();
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

        btnCalculation_Click() {
            let self = this;
            if (!self.hasEmployee) return;
            self.calculate(false);
        }

        btnReCalculation_Click() {
            let self = this;
            if (!self.hasEmployee) return;
            self.calculate(true);
        }

        calculate(updateAll: boolean) {
            var self = this;
            if (self.dialogShow != undefined && self.dialogShow.$dialog != null) {
                self.dialogShow.close();
            }
            if (self.workTypeNotFound.length > 0) {
                self.showErrorDialog();
                return;
            }
            // insert flex
            let errorGrid: any = $("#dpGrid").mGrid("errors");
            let checkDataCare: boolean = true;
            if (errorGrid == undefined || errorGrid.length == 0) {
                nts.uk.ui.block.invisible();
                nts.uk.ui.block.grayout();
                self.listCareError([]);
                self.listCareInputError([]);
                self.listCheckHolidays([]);
                self.listCheck28([]);
                self.listCheckDeviation = [];
                self.listErrorMonth = [];
                let dataChange: any = $("#dpGrid").mGrid("updatedCells");
                var dataSource = $("#dpGrid").mGrid("dataSource");
                let dataChangeProcess: any = [];
                let dataCheckSign: any = [];
                let dataCheckApproval: any = [];
                let sprStampSourceInfo: any = null;
                _.each(dataChange, (data: any) => {
                    let dataTemp = _.find(dataSource, (item: any) => {
                        return item.id == data.rowId;
                    });
                    if (data.columnKey != "sign" && data.columnKey != "approval") {
                        if (data.columnKey.indexOf("Code") == -1 && data.columnKey.indexOf("NO") == -1) {
                            if (data.columnKey.indexOf("Name") != -1) {
                            } else {
                                // check itemCare 
                                let groupCare = self.checkItemCare(Number(data.columnKey.substring(1, data.columnKey.length)));
                                if (groupCare == 0 || groupCare == 1 || groupCare == 2) {
                                    if (self.checkErrorData(groupCare, data, dataSource) == false || self.listCareInputError().length > 0) {
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
                                let dataMap = new InfoCellEdit(data.rowId, data.columnKey.substring(1, data.columnKey.length), value, layoutAndType == undefined ? "" : layoutAndType.valueType, layoutAndType == undefined ? "" : layoutAndType.layoutCode, dataTemp.employeeId, dataTemp.dateDetail.utc().toISOString(), 0, data.columnKey);
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
                            item = _.find(self.lstAttendanceItem(), (data) => {
                                return String(data.id) === columnKey;
                            })

                            let layoutAndType: any = _.find(self.itemValueAll(), (item: any) => {
                                return item.itemId == columnKey;
                            });
                            let dataMap = new InfoCellEdit(data.rowId, columnKey, String(data.value), layoutAndType.valueType, layoutAndType.layoutCode, dataTemp.employeeId, dataTemp.dateDetail.utc().toISOString(), item.typeGroup, data.columnKey);
                            dataChangeProcess.push(dataMap);
                        }
                    }
                });
                //if (!_.isEmpty(self.lstDomainOld)) {
                    let dataParent = {
                        itemValues: dataChangeProcess,
                        dataCheckSign: dataCheckSign,
                        dataCheckApproval: dataCheckApproval,
                        mode: self.displayFormat(),
                        spr: sprStampSourceInfo,
                        //dailyOlds: self.lstDomainOld,
                        //dailyEdits: self.lstDomainEdit,
                        flagCalculation: updateAll
                    }
                    if (self.displayFormat() == 0) {
                        if (!_.isEmpty(self.shareObject()) && self.shareObject().initClock != null) {
                            dataParent["employeeId"] = self.shareObject().initClock.employeeId;
                            dataParent["dateRange"] = { startDate: self.shareObject().initClock.dateSpr.utc(), endDate: self.shareObject().initClock.dateSpr.utc() };
                        } else {
                            dataParent["employeeId"] = dataSource.length > 0 ? dataSource[0].employeeId : null;
                            dataParent["dateRange"] = dataSource.length > 0 ? { startDate: dataSource[0].dateDetail, endDate: dataSource[dataSource.length - 1].dateDetail } : null;
                        }
                        dataParent["monthValue"] = self.valueUpdateMonth;
                    }
                    self.removeErrorRefer();
                    let dfd = $.Deferred();
                    service.calculation(dataParent).done((data) => {
                        if (data.resultError != null && !_.isEmpty(data.resultError.flexShortage)) {
                            if (data.resultError.flexShortage.error && data.resultError.flexShortage.messageError.length != 0) {
                                $("#next-month").ntsError("clear");
                                 _.each(data.resultError.flexShortage.messageError, value => {
                                    $("#next-month").ntsError("set", value.message, value.messageId);
                                });
                            } else {
                                $("#next-month").ntsError("clear");
                            }
                        }
                        if (data.resultError == null || !_.isEmpty(data.resultError.errorMap[5])) {
                            //self.lstDomainEdit = data.calculatedRows;
                            let lstValue = data.resultValues;
                            _.forEach(self.dpData, row => {
                                let cellDatas = row.cellDatas;
                                let rrow = _.find(lstValue, (r: any) => { return row.employeeId == r.employeeId && r.date == row.date });
                                _.forEach(cellDatas, cell => {
                                    let editedCell = _.find(dataChangeProcess, (item: any) => { return (item.rowId.indexOf(row.id) >= 0 && item.columnKey == cell.columnKey); });
                                    let editedCell2 = _.find(self.cellStates(), (item: any) => { return (item.rowId.indexOf(row.id) >= 0 && item.columnKey == cell.columnKey); });
                                    if ((editedCell == null
                                        && (editedCell2 == null || (!editedCell2.state.contains("mgrid-manual-edit-other") && !editedCell2.state.contains("mgrid-manual-edit-target"))))
                                        || updateAll) {
                                        let itemId = self.getItemIdFromColumnKey(cell.columnKey);
                                        let itemValue = _.find(rrow.items, (i: any) => { return i.itemId == itemId });
                                        if (itemValue)
                                            $("#dpGrid").mGrid("updateCell", "_" + row.id, cell.columnKey, itemValue.value == null ? "" : itemValue.value);
                                    }
                                });
                            });
                            self.flagCalculation = true;
                            nts.uk.ui.block.clear();
                            if (data.resultError != null && data.resultError.errorMap[5] != null) {
                                self.listErrorMonth = data.resultError.errorMap[5];
                            }
                        } else {
                            nts.uk.ui.block.clear();
                            if (data.resultError.errorMap[0] != undefined) {
                                self.listCareError(data.resultError.errorMap[0])
                                // nts.uk.ui.dialog.alertError({ messageId: "Msg_996" })
                            }
                            if (data.resultError.errorMap[1] != undefined) {
                                self.listCareInputError(data.resultError.errorMap[1])
                                // nts.uk.ui.dialog.alertError({ messageId: "Msg_1108" })
                            }
                            if (data.resultError.errorMap[2] != undefined) {
                                self.listCheckHolidays(data.resultError.errorMap[2]);
                                self.loadRowScreen(false, true);
                            }
    
                            if (data.resultError.errorMap[3] != undefined) {
                                self.listCheck28(data.resultError.errorMap[3]);
                            }
    
                            if (data.resultError.errorMap[4] != undefined) {
                                self.listCheckDeviation = data.resultError.errorMap[4];
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
//                } else 
//                    nts.uk.ui.block.clear();
            }
        }

        getItemIdFromColumnKey(columnKey: string): string {
            let itemId: string = "";
            if (columnKey != "sign" && columnKey != "approval") {
                if (columnKey.indexOf("Code") == -1 && columnKey.indexOf("NO") == -1) {
                    if (columnKey.indexOf("Name") != -1) {
                        itemId = "";
                    } else {
                        itemId = columnKey.substring(1, columnKey.length);
                    }
                } else {
                    if (columnKey.indexOf("Code") != -1) {
                        itemId = columnKey.substring(4, columnKey.length);
                    } else {
                        itemId = columnKey.substring(2, columnKey.length);
                    }
                }
            }
            return itemId;
        }

        loadRowScreen(onlyLoadMonth: boolean, onlyCalc: boolean) {
            var self = this;
            let lstEmployee = [];
            if (self.displayFormat() === 0) {
                let lst = _.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                });
                if (lst != undefined) lstEmployee.push(lst);
            } else {
                lstEmployee = self.lstEmployee();
            }

            let dataSource = $("#dpGrid").mGrid("dataSource");

            let dataChange: any = $("#dpGrid").mGrid("updatedCells");
            let rowIdsTemp = _.uniqBy(dataChange, function(e) {
                return e.rowId;
            });
            
            if (onlyCalc) {
                rowIdsTemp = _.map(_.uniqBy(dataSource, function(e) {
                    return e.id;
                }), mapRow =>{
                   return {rowId: mapRow.id} 
                });
            }
            
            let rowIds = _.map(_.cloneDeep(rowIdsTemp), (value) => {
                return value.rowId.substring(1, value.rowId.length);
            });

            let lstData = _.map(_.sortBy(_.filter(self.dailyPerfomanceData(), (v) => _.includes(rowIds, v.id)), (sort) => {
                return new Date(sort.date);
            }), (map) => {
                map.date = moment(map.date).utc().toISOString();
                map.state = "";
                map.error = "";
                map.sign = false;
                map.approval = false;
                map.typeGroup = "";
                return map;
            });

            let param = {
                lstAttendanceItem: self.lstAttendanceItem(),
                lstEmployee: lstEmployee,
                dateRange: {
                    startDate: onlyLoadMonth ? null : lstData[0].date,
                    endDate: onlyLoadMonth ? null : lstData[lstData.length - 1].date
                },
                mode: _.isEmpty(self.shareObject()) ? 0 : self.shareObject().screenMode,
                displayFormat: self.displayFormat(),
                lstData: lstData,
                lstHeader: self.lstHeaderReceive,
                autBussCode: self.autBussCode(),
                dateMonth: moment(self.dateRanger().endDate).utc().toISOString(),
                onlyLoadMonth: onlyLoadMonth,
                //dailys: self.lstDomainEdit,
                dateExtract: {
                    startDate: moment(self.dateRanger().startDate).toISOString(),
                    endDate: moment(self.dateRanger().endDate).toISOString()
                },
                identityProcess: self.dataAll().identityProcessDto,
                showLock: self.showLock()
            }

            let dfd = $.Deferred();
            service.loadRow(param).done((data) => {
                if (onlyLoadMonth) {
                    self.processFlex(data, true);
                    nts.uk.ui.block.clear();
                    return dfd.resolve();
                }
                self.showTighProcess(data.showTighProcess);
                //self.lstDomainEdit = data.domainOld;
                //self.lstDomainOld = _.cloneDeep(data.domainOld);
                self.processFlex(data, true);
                let dataSourceRow, dataSource, dataSourceNew, dataRowTemp = [];
                dataSourceRow = _.cloneDeep(self.formatDate(data.lstData));
                _.forEach(dataSourceRow, (valueUpate) => {
                    _.each(valueUpate, (value, key) => {
                        $("#dpGrid").mGrid("updateCell", valueUpate.id, key, value, true)
                    });
                })
                
                setTimeout(() => {
                    $("#dpGrid").mGrid("clearState", _.map(rowIdsTemp, (value) => {
                        return value.rowId;
                    }))
                    _.each(data.lstCellState, (valt) => {
                        console.log("column key:" + valt.columnKey);
                        $("#dpGrid").mGrid("setState", valt.rowId, valt.columnKey, valt.state);
                    });
                    if (!self.displayWhenZero()) {
                        $("#dpGrid").mGrid("hideZero", true)
                        $("#dpGrid").mGrid("hideZero", false)
                        $("#dpGrid").mGrid("hideZero", true)
                    } else {
                        $("#dpGrid").mGrid("hideZero", false)
                        $("#dpGrid").mGrid("hideZero", true)
                        $("#dpGrid").mGrid("hideZero", false)
                    }
                    nts.uk.ui.block.clear();
                }, 1000);
                dfd.resolve();
            });
            return dfd.promise();
        }

        getNameMonthly(): JQueryPromise<any> {
            let dfd = $.Deferred(), self = this, arrItemId: string[] = [];
            if (self.listAttendanceItemId().length <= 0) {
                dfd.resolve();
                return;
            }

            _.each(self.listAttendanceItemId(), (attendanceItemId) => {
                arrItemId.push(attendanceItemId.itemId);
            });

            service.getNameMonthlyAttItem(arrItemId).done(data => {
                if (!self.isStartScreen()) {
                    // neu dang khoi dong man hinh thi k dc destroy
                    $('#miGrid').igGrid("destroy");
                    self.isStartScreen(false);
                }
                self.loadMIGrid(data);
                dfd.resolve();
            }).fail(() => {
                dfd.reject();
            });
            return dfd.promise();
        }

        checkIsColumn(dataCell: any, key: any): boolean {
            let check = false;
            _.each(dataCell, (item: any) => {
                if (item.columnKey.indexOf("NO" + key) != -1) {
                    check = true;
                    return;
                }
            });
            return check;
        }

        getPrimitiveValue(value: any, atr: any): string {
            var self = this;
            let valueResult: string = "";
            if (atr != undefined && atr != null) {
                if (atr == 6) {
                    // Time
                    valueResult = value == "" ? null : String(self.getHoursAll(value));
                } else if (atr == 5) {
                    valueResult = value == "" ? null : String(self.getHoursTime(value));
                } else {
                    valueResult = value;
                }
            } else {
                valueResult = value;
            }
            return valueResult;
        }

        getHours(value: any): number {
            return Number(value.split(':')[0]) * 60 + Number(value.split(':')[1]);
        }
        //time
        getHoursTime(value): number {
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
        getHoursAll(value: any): number {
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
        
        convertToHours(value: any): string {
            let self = this;
            let hours = value < 0 ? String(0 - Math.floor(Math.abs(value / 60))) : String(Math.floor(value / 60));
            let minutes = String(Math.abs(value) % 60);
            if (Number(minutes) < 10) minutes = "0" + minutes;
            return hours + ":" + minutes;
        }

        //check data item in care and childCare 
        // child care = 0 , care = 1, other = 2;
        checkItemCare(itemId: any): number {
            if (itemId == 759 || itemId == 760 || itemId == 761 || itemId == 762) {
                return 0;
            } else if (itemId == 763 || itemId == 764 || itemId == 765 || itemId == 766) {
                return 1;
            } else {
                return 2;
            }
        }

        // check data error group care , child care
        checkErrorData(group: number, data: any, dataSource: any): boolean {
            var self = this;
            data["itemId"] = data.columnKey.substring(1, data.columnKey.length);
            let rowItemSelect: any = _.find(dataSource, function(value: any) {
                return value.id == data.rowId;
            });
            self.checkInputCare(data, rowItemSelect);
            if (group != 2) {
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

        checkInputCare(data: any, rowItemSelect: any) {
            var self = this;
            if (CHECK_INPUT[data.itemId] != undefined
                && ((self.isNNUE(rowItemSelect["A" + CHECK_INPUT[data.itemId]]) && data.value == "")
                    || (data.value != "" && !self.isNNUE(rowItemSelect["A" + CHECK_INPUT[data.itemId]])))) {
                data["itemId"] = data.columnKey.substring(1, data.columnKey.length);
                data["group"] = Number(CHECK_INPUT[data.itemId]);
                self.listCareInputError.push(data);
            }
        }

        isNNUE(value: any): boolean {
            if (value != "") return true;
            else return false;
        }

        hideComponent() {
            var self = this;
            if (self.displayFormat() == 0) {
                $("#container").css("display", "block");
                $("#daterangepicker").css("display", "block");
                $("#cbListDate").css("display", "none");
                $("#btnVacationRemaining").show();
                $('#numberHoliday').show();
                $('#fixed-table').show();
                //  $("#content-grid").attr('style', 'top: 244px !IMPORTANT');
            } else if (self.displayFormat() == 1) {
                $("#daterangepicker").css("display", "none");
                $("#cbListDate").css("display", "block");
                $("#container").css("display", "none");
                $("#btnVacationRemaining").hide();
                $('#numberHoliday').hide();
                $('#fixed-table').hide();
                $('#flex').hide();
                // $("#content-grid").attr('style', 'top: 225px !IMPORTANT');
            } else {
                $("#daterangepicker").css("display", "block");
                $("#cbListDate").css("display", "none");
                $("#container").css("display", "none");
                $("#btnVacationRemaining").hide();
                $('#numberHoliday').hide();
                $('#fixed-table').hide();
                $('#flex').hide();
                // $("#content-grid").attr('style', 'top: 180px !IMPORTANT');
            }
        }

        reloadScreen() {
            var self = this;
            console.log(self.dailyPerfomanceData());
            // if (!nts.uk.ui.errors.hasError()) {
            nts.uk.ui.errors.clearAll();
            self.hideComponent();
            self.removeErrorRefer();
            $.when(self.findEmployee()).done(data => {
                let lstEmployee = data;
                if (self.displayFormat() === 1) {
                    if (self.datePicker().startDate !== self.dateRanger().startDate &&
                        self.datePicker().endDate !== self.dateRanger().endDate) {
                        self.datePicker().startDate = self.dateRanger().startDate;
                        self.datePicker().endDate = self.dateRanger().endDate;
                        self.datePicker.valueHasMutated();
                    }
                }
                let param = {
                    dateRange: self.hasEmployee ? {
                        startDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                        endDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                    } : null,
                    displayFormat: self.hasEmployee ? self.displayFormat() : _.isEmpty(self.shareObject()) ? 0 : self.shareObject().displayFormat,
                    initScreen: self.hasEmployee ? 1 : 0,
                    mode: _.isEmpty(self.shareObject()) ? 0 : self.shareObject().screenMode,
                    lstEmployee: lstEmployee,
                    formatCodes: self.formatCodes(),
                    objectShare: null,
                    showLock: self.showLock()
                };
                self.characteristics.formatExtract = param.displayFormat;
                character.save('characterKdw003a', self.characteristics);
                nts.uk.ui.block.invisible();
                nts.uk.ui.block.grayout();
                service.startScreen(param).done((data) => {
                    if (_.isEmpty(data.lstEmployee) && _.isEmpty(data.lstControlDisplayItem.lstHeader)) {
                         self.destroyGrid();
                         self.processFlex(data, false);
                         //self.hasEmployee = false;
//                        nts.uk.ui.dialog.alert({ messageId: "Msg_916" }).then(function() {
//                            return;
//                        });
                    } else {
                        self.initScreenSPR = 1;
                        //self.lstDomainOld = data.domainOld;
                        //self.lstDomainEdit = _.cloneDeep(data.domainOld);
                        if (data.typeBussiness != localStorage.getItem('kdw003_type')) {
                            localStorage.removeItem(window.location.href + '/dpGrid');
                        }
                        localStorage.setItem('kdw003_type', data.typeBussiness);
                        self.dataAll(data);
                        self.itemInputName = data.lstControlDisplayItem.itemInputName;
                        self.formatCodes(data.lstControlDisplayItem.formatCode);
                        self.autBussCode(data.autBussCode);
                        self.lstAttendanceItem(data.lstControlDisplayItem.lstAttendanceItem);
                        self.itemValueAll(data.itemValues);
                        self.createSumColumn(data);
                        self.columnSettings(data.lstControlDisplayItem.columnSettings);
                        self.showPrincipal(data.showPrincipal);
                        self.showSupervisor(data.showSupervisor);
                        self.showTighProcess(data.showTighProcess);
                        self.lstHeaderReceive = _.cloneDeep(data.lstControlDisplayItem.lstHeader);
                        //self.showLock(self.showButton().available12());
                        //self.unLock(false);
                        if (data.lstControlDisplayItem.lstHeader.length == 0) self.hasLstHeader = false;
                        if (self.showPrincipal() || data.lstControlDisplayItem.lstHeader.length == 0) {
                            self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3], self.fixHeaders()[4]];
                            self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[7], self.fixHeaders()[4]];
                            self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[7], self.fixHeaders()[4]];
                        } else {
                            self.employeeModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[3]];
                            self.dateModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[7]];
                            self.errorModeHeader = [self.fixHeaders()[0], self.fixHeaders()[1], self.fixHeaders()[2], self.fixHeaders()[5], self.fixHeaders()[6], self.fixHeaders()[3], self.fixHeaders()[7]];
                        }
                        if (self.showSupervisor()) {
                            self.employeeModeHeader.push(self.fixHeaders()[8]);
                            self.dateModeHeader.push(self.fixHeaders()[8]);
                            self.errorModeHeader.push(self.fixHeaders()[8]);
                        }
                        self.receiveData(data);
                        self.extraction();
                        // no20
                        self.dPErrorDto(data.dperrorDto);
                        // flex
                        self.processFlex(data, false);
                        self.displayNumberZero();
                        //self.displayProfileIcon(self.displayFormat());
                        self.dislayNumberHeaderText();
                        //check visable MIGrid
                        if (self.displayFormat() != 0) {
                            self.isVisibleMIGrid(false);
                        }
                        if (!self.hasEmployee) {
                            self.loadKcp009();
                            self.hasEmployee = true;
                        }
                   }
                    nts.uk.ui.block.clear();
                }).fail(function(error) {
                    if (error.messageId == "Msg_672") {
                        nts.uk.ui.dialog.info({ messageId: "Msg_672" })
                    } else { 
                        if (error.messageId != undefined) {
                            nts.uk.ui.dialog.alert(error.messageId == "Msg_1430" ? error.message : { messageId: error.messageId }).then(function() {
                                nts.uk.request.jumpToTopPage();
                            });

                        } else if ((error.messageId == undefined && error.errors.length > 0)) {
                            nts.uk.ui.dialog.bundledErrors({ errors: error.errors }).then(function() {
                                nts.uk.request.jumpToTopPage();
                            });

                        };
                    }
                    nts.uk.ui.block.clear();
                });
            });
        }

        findEmployee(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred()
            let lstEmployee = [];
            if (self.displayFormat() === 0) {
                let lst = _.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                });

                if (lst != undefined) {
                    lstEmployee.push(lst);
                    dfd.resolve(lstEmployee);
                }
                else if (self.selectedEmployee() != undefined) {
                    //let dfd2 = $.Deferred();
                    service.searchEmployee(self.selectedEmployee()).done(data => {
                        let emp = {
                            id: data.employeeId,
                            code: data.employeeCode,
                            businessName: data.businessName,
                            workplaceName: data.orgName,
                            workplaceId: "",
                            depName: '',
                            isLoginUser: false
                        }
                        lstEmployee.push(emp);
                        self.lstEmployee(lstEmployee);
                        dfd.resolve(lstEmployee);
                        //  dfd2.resolve();
                    });
                    // dfd2.promise();
                }
            } else {
                lstEmployee = self.lstEmployee();
                dfd.resolve(lstEmployee);
            }
            return dfd.promise();
        }

        removeErrorRefer() {
            var self = this;
            if (!self.hasEmployee) return;
            self.listCareError([]);
            self.listCareInputError([]);
            self.listCheckHolidays([]);
            self.listCheck28([]);
            self.workTypeNotFound = [];
            self.listCheckDeviation = [];
            self.listErrorMonth = [];
        }

        btnExtraction_Click() {
            let self = this;
            if (!self.hasEmployee || nts.uk.ui.errors.hasError()) return;
            self.showTextStyle = false;
            self.clickFromExtract = true;
            self.clickCounter = new CLickCount();
            if (self.isVisibleMIGrid()) {
                //set = false de co the jump den ham subscribe
                self.isVisibleMIGrid(false);
            }
            self.reloadScreen();
        }

        showErrorDialog() {
            var self = this;
            if (!self.hasEmployee) return;
            let lstEmployee = [];
            let uiErrors: any = $("#dpGrid").mGrid("errors");
            let errorValidateScreeen: any = [];
            if (self.displayFormat() === 0) {
                _.each(uiErrors, value => {
                    let dateCon = _.find(self.dpData, (item: any) => {
                        return item.id == value.rowId.substring(1, value.rowId.length);
                    });

                    let object = { date: dateCon.date, employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: value.message, itemName: "", columnKey: value.columnKey };
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
                let itemOtherInGroup = CHECK_INPUT[value.itemId+""];
                let itemGroup = self.itemInputName[Number(itemOtherInGroup)];
                let nameGroup: any = (itemGroup == undefined) ? "" : itemGroup;
                object.message = nts.uk.resource.getMessage("Msg_1108", [object.itemName, nameGroup]);
                errorValidateScreeen.push(object);
            });

            //CheckHolidays
            _.each(self.listCheckHolidays(), value => {
                let object = { date: value.date, employeeCode: self.dpData[0].employeeCode, employeeName: self.dpData[0].employeeName, message: value.valueType, itemName: value.rowId, columnKey: value.itemId };
                errorValidateScreeen.push(object);
            });

            // check item28 input listCheck28
            _.each(self.listCheck28(), value => {
                let dateCon = _.find(self.dpData, (item: any) => {
                    return item.id == value.rowId.substring(1, value.rowId.length);
                });
                let object = { date: dateCon.date, employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: value.layoutCode, itemName: "", columnKey: value.itemId };
                //                let item = _.find(self.optionalHeader, (data) => {
                //                    return String(data.key) === "A" + value.itemId;
                //                })
                let item = _.find(self.optionalHeader, (data) => {
                    if (data.group != undefined && data.group != null) {
                        return String(data.group[0].key) === "Code" + value.itemId;
                    } else {
                        return String(data.key) === "A" + value.itemId;
                    }
                })
                object.itemName = (item == undefined) ? "" : item.headerText;
                errorValidateScreeen.push(object);
            });

            // item28 , search
            _.each(self.workTypeNotFound, value => {
                let dateCon = _.find(self.dpData, (item: any) => {
                    return item.id == value.rowId.substring(1, value.rowId.length);
                });
                let object = { date: dateCon.date, employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: value.message, itemName: "", columnKey: value.itemId };
                let item = _.find(self.optionalHeader, (data) => {
                    if (data.group != undefined && data.group != null) {
                        return String(data.group[0].key) === value.columnKey;
                    }
                })
                object.itemName = (item == undefined) ? "" : item.headerText;
                errorValidateScreeen.push(object);
            });

            //error check PC
            _.each(self.listCheckDeviation, value => {
                let dateCon = _.find(self.dpData, (item: any) => {
                    return item.employeeId == value.employeeId && item.date == value.date;
                });
                let object = { date: dateCon.date, employeeCode: dateCon.employeeCode, employeeName: dateCon.employeeName, message: value.valueType, itemName: "", columnKey: value.itemId };
                let item = _.find(self.optionalHeader, (data) => {
                    if (data.group != undefined && data.group != null) {
                        return String(data.group[0].key) === "Code" + value.itemId;
                    } else {
                        return data.key != undefined && String(data.key) === "A" + value.itemId;
                    }
                })
                object.itemName = (item == undefined) ? "" : item.headerText;
                object.message = nts.uk.resource.getMessage("Msg_1298", [object.itemName, value.value])
                errorValidateScreeen.push(object);
            });

            //error month 
            _.each(self.listErrorMonth, value => {
                let emp = _.find(self.lstEmployee(), (employee) => {
                    return employee.id === value.employeeId;
                });
                let object = { date: "", employeeCode: emp.code, employeeName: emp.businessName, value, message: value.message, columnKey: "" };
                errorValidateScreeen.push(object);
            })

            // error month flex
            if (self.displayFormat() === 0) {
                let lst = _.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                });
                _.each(self.lstErrorFlex, value => {
                    let object = { date: "", employeeCode: lst.code, employeeName: lst.businessName, value, itemName: getText("KDW003_78"), columnKey: "" };
                    errorValidateScreeen.push(object);
                });
            }

            if (self.displayFormat() === 0) {
                let emp =  _.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                })
                if (emp != null && emp != undefined) {
                    lstEmployee.push(emp);
                }
            } else {
                lstEmployee = self.lstEmployee();
            }
            if (lstEmployee.length > 0) {
                let param = {
                    dateRange: {
                        startDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                        endDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                    },
                    lstEmployee: lstEmployee
                };
                setShared("paramToGetError", param);
                setShared("errorValidate", errorValidateScreeen);
                self.dialogShow = nts.uk.ui.windows.sub.modeless("/view/kdw/003/b/index.xhtml").onClosed(function() {
                    let errorCodes = nts.uk.ui.windows.getShared('shareToKdw003aa');
                });
            }
        }

        changeExtractionCondition() {
            var self = this;
            if (!self.hasEmployee) return;
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
                let errorParam = { initMode: 0, selectedItems: [] };
                setShared("KDW003D_ErrorParam", errorParam);
                modal("/view/kdw/003/d/index.xhtml").onClosed(() => {
                    nts.uk.ui.block.clear();
                    let errorCodes = nts.uk.ui.windows.getShared('KDW003D_Output');
                    if (errorCodes != undefined && errorCodes.length > 0) {
                        let param = {
                            dateRange: {
                                startDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                                endDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                            },
                            lstEmployee: lstEmployee,
                            displayFormat: self.displayFormat(),
                            mode: _.isEmpty(self.shareObject()) ? 0 : self.shareObject().screenMode,
                            errorCodes: errorCodes,
                            formatCodes: self.formatCodes()
                        };
                        nts.uk.ui.block.invisible();
                        nts.uk.ui.block.grayout();
                        service.selectErrorCode(param).done((data) => {
                            //self.lstDomainOld = data.domainOld;
                            //self.lstDomainEdit = _.cloneDeep(data.domainOld);
                            self.dataAll(data);
                            self.removeErrorRefer();
                            self.createSumColumn(data);
                            self.columnSettings(data.lstControlDisplayItem.columnSettings);
                            self.receiveData(data);
                            self.extraction();
                            self.displayNumberZero();
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
            if (!self.hasEmployee) return;
            if (!nts.uk.ui.errors.hasError()) {
                setShared("selectedPerfFmtCodeList", self.formatCodes());
                modal("/view/kdw/003/c/index.xhtml").onClosed(() => {
                    var dataTemp = nts.uk.ui.windows.getShared('KDW003C_Output');
                    if (dataTemp != undefined) {
                        let data = [dataTemp];
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
                            dateRange: self.hasEmployee ? {
                                startDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                                endDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                            } : null,
                            displayFormat: self.hasEmployee ? self.displayFormat() : _.isEmpty(self.shareObject()) ? 0 : self.shareObject().displayFormat,
                            initScreen: self.hasEmployee ? 1 : 0,
                            mode: _.isEmpty(self.shareObject()) ? 0 : self.shareObject().screenMode,
                            lstEmployee: lstEmployee,
                            formatCodes: data,
                            objectShare: null,
                            showLock: self.showLock()
                        };
                        self.characteristics.authenSelectFormat = data;
                        character.save('characterKdw003a', self.characteristics);
                        nts.uk.ui.block.invisible();
                        nts.uk.ui.block.grayout();
                        service.startScreen(param).done((data) => {
                            //self.lstDomainOld = data.domainOld;
                            //self.lstDomainEdit = _.cloneDeep(data.domainOld);
                            self.dataAll(data);
                            self.removeErrorRefer();
                            self.createSumColumn(data);
                            self.columnSettings(data.lstControlDisplayItem.columnSettings);
                            self.receiveData(data);
                            self.extraction();
                            self.displayNumberZero();
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
            if (!self.hasEmployee) return;
            if (!nts.uk.ui.errors.hasError()) {
                let lstEmployee = [];
                if (self.displayFormat() === 0) {
                    lstEmployee.push(_.find(self.lstEmployee(), (employee) => {
                        return employee.id === self.selectedEmployee();
                    }));
                    setShared("KDL014A_PARAM", {
                        startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        endDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        employeeID: lstEmployee[0].code
                    });
                    modal("/view/kdl/014/a/index.xhtml").onClosed(() => {
                    });

                } else if (self.displayFormat() === 1) {
                    lstEmployee = self.lstEmployee().map((data) => {
                        return data.code;
                    });
                    setShared("KDL014B_PARAM", {
                        startDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        endDate: moment(self.dateRanger().startDate).utc().toISOString(),
                        lstEmployee: lstEmployee
                    });
                    modal("/view/kdl/014/b/index.xhtml").onClosed(() => {
                    });
                }
            }
        }

        employmentOk() {
            let _self = this;
            if (!_self.hasEmployee) return;
            if (!nts.uk.ui.errors.hasError()) {
                nts.uk.ui.block.grayout();
                modal("/view/kdl/006/a/index.xhtml").onClosed(() => {
                    nts.uk.ui.block.clear();
                });
            }
        }

        tighProcess() {
            let self = this;
            if (!self.hasEmployee) return;
            var dataSource = $("#dpGrid").mGrid("dataSource");
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            let dataRowEnd = dataSource[dataSource.length - 1];
            service.addClosure({ employeeId: dataRowEnd.employeeId, date: dataRowEnd.dateDetail }).done((data) => {
                self.processLockButton(self.showLock());
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                nts.uk.ui.block.clear();
            });
            nts.uk.ui.block.clear();
        }

        proceedLock() {
            let self = this;
            //Msg_984
            nts.uk.ui.dialog.info({ messageId: "Msg_984" });
            if (!self.hasEmployee) return;
            self.showLock(true);
            self.unLock(false);
            self.processLockButton(true);
        }

        proceedUnLock() {
            let self = this;
            //Msg_982
            nts.uk.ui.dialog.caution({ messageId: "Msg_982" }).then(() => {
                if (!self.hasEmployee) return;
                self.showLock(false);
                self.unLock(true);
                self.processLockButton(false);
            });
            //nts.uk.ui.dialog.info({ messageId: "Msg_982" });
        }

        processLockButton(showLock: boolean) {
            let self = this;
            if (!self.hasEmployee) return;
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            let lstData = _.map(self.dailyPerfomanceData(), (map) => {
                map.date = moment(map.date).toISOString();
                map.state = "";
                return map;
            });
            let lstEmployee = [];
            if (self.displayFormat() === 0) {
                let lst: any = _.find(self.lstEmployee(), (employee) => {
                    return employee.id === self.selectedEmployee();
                });
                if (lst != undefined) lstEmployee.push(lst);
            } else {
                lstEmployee = self.lstEmployee();
            }
            let param: any = {
                lstAttendanceItem: self.lstAttendanceItem(),
                lstEmployee: lstEmployee,
                dateRange: self.hasEmployee ? {
                    startDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().startDate).utc().toISOString(),
                    endDate: self.displayFormat() === 1 ? moment(self.selectedDate()) : moment(self.dateRanger().endDate).utc().toISOString()
                } : null,
                mode: _.isEmpty(self.shareObject()) ? 0 : self.shareObject().screenMode,
                displayFormat: self.displayFormat(),
                lstData: lstData,
                lstHeader: self.lstHeaderReceive,
                showLock: showLock
            }

            let dfd = $.Deferred();
            service.lock(param).done((data) => {
                let dataSourceRow = _.cloneDeep(self.formatDate(data.lstData));
                _.forEach(dataSourceRow, (valueUpdate) => {
                    $("#dpGrid").mGrid("updateCell", valueUpdate.id, "state", valueUpdate.state, true)
                })
                setTimeout(() => {
                    let rowIdsTemp = _.uniqBy(data.lstCellState, function(e) {
                        return e.rowId;
                    });
                    $("#dpGrid").mGrid("clearState", _.map(rowIdsTemp, (value) => {
                        return value.rowId;
                    }))
                    _.each(data.lstCellState, (valt) => {
                        console.log("column key:" + valt.columnKey);
                        $("#dpGrid").mGrid("setState", valt.rowId, valt.columnKey, valt.state);
                    });
                    nts.uk.ui.block.clear();
                }, 1000);
                dfd.resolve();
            }).fail(function(error) {
                nts.uk.ui.block.clear();
            });
            return dfd.promise();
        }

        btnSetting_Click() {
            let self = this;
            if (!self.hasEmployee) return;
            var container = $("#setting-content");
            if (container.css("visibility") === 'hidden') {
                container.css("visibility", "visible");
                //                container.css("top", "0px");
                //                container.css("left", "0px");
            }
            $(document).mouseup(function(e) {
                // if the target of the click isn't the container nor a descendant of the container
                if (!container.is(e.target) && container.has(e.target).length === 0) {
                    container.css("visibility", "hidden");
                    //                    container.css("top", "-9999px");
                    //                    container.css("left", "-9999px");
                }
            });
        }

        btnVacationRemaining_Click() {
            let self = this;
            if (!self.hasEmployee) return;
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
            if (!self.hasEmployee) return;
            let command = {
                lstHeader: {},
                formatCode: self.autBussCode(),
                lstHeaderMiGrid: {}
            };
            let jsonColumnWith = localStorage.getItem(window.location.href + '/dpGrid');
            let jsonColumnWidthMiGrid = localStorage.getItem(window.location.href + '/miGrid');
            
            let valueTemp = 0;
            _.forEach($.parseJSON(jsonColumnWith), (valueP, keyP) =>{
                if (keyP != "reparer") {
                    _.forEach(valueP, (value, key) => {
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
                }
            })
           
            if(self.isVisibleMIGrid()){
                let columnWidthMiGrid = $.parseJSON(jsonColumnWidthMiGrid.replace(/_/g, ''));
                delete columnWidthMiGrid.monthYear;
                command.lstHeaderMiGrid = columnWidthMiGrid;
            }
            
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            service.saveColumnWidth(command).done(() =>{
                nts.uk.ui.block.clear(); 
            }).fail(() => {
                nts.uk.ui.block.clear(); 
            });
        }

        deleteGridInLocalStorage(): void {
            // delete localStorage dpGrid
            localStorage.removeItem(window.location.href + '/dpGrid');
            // delete localStorage miGrid
            localStorage.removeItem(window.location.href + '/miGrid');
        }

        extractionData() {
            var self = this;
            self.headersGrid = [];
            self.fixColGrid([]);
            if (self.displayFormat() == 0) {
                self.fixColGrid(self.employeeModeFixCol);
            } else if (self.displayFormat() == 1) {
                self.fixColGrid(self.dateModeFixCol);
            } else if (self.displayFormat() == 2) {
                self.fixColGrid(self.errorModeFixCol);
            }
            if (self.showPrincipal() && self.hasLstHeader) {
                let sign = _.find(self.fixColGrid(), (data: any) => {
                    return data.columnKey === 'sign';
                }
                )
                if (sign == undefined) self.fixColGrid.push({ columnKey: 'sign', isFixed: true });
            }
            if (self.showSupervisor() && self.hasLstHeader) {
                let sign = _.find(self.fixColGrid(), (data: any) => {
                    return data.columnKey === 'approval';
                }
                )
                if (sign == undefined) self.fixColGrid.push({ columnKey: 'approval', isFixed: true });
            }
            self.loadHeader(self.displayFormat());
            self.dailyPerfomanceData(_.cloneDeep(self.dpData));
        }

        extraction() {
            var self = this;
            if (self.hasEmployee) self.destroyGrid();
            self.extractionData();
            self.loadGrid();
            self.displayProfileIcon(self.displayFormat());
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
                return value.rowId == id && (_.filter(value.state, state => {
                    return state == "ntsgrid-disable"
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
            if (!self.hasEmployee) return;
            $("#dpGrid").mGrid("checkAll", "sign", true);
            $("#dpGrid").mGrid("checkAll", "approval", true);
        }

        releaseAll() {
            var self = this;
            if (!self.hasEmployee) return;
            $("#dpGrid").mGrid("uncheckAll", "sign", true);
            $("#dpGrid").mGrid("uncheckAll", "approval", true);
        }

        destroyGrid() {
            if ($("#dpGrid").hasClass("mgrid")) {
                $("#dpGrid").mGrid("destroy");
                $("#dpGrid").removeClass("mgrid");
                $("#dpGrid").off();
            }
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
            self.headerColors = [];
            _.forEach(self.headersGrid, (header) => {

                if (header.group != null && header.group != undefined && header.group.length > 0 && header.group[0].color != "") {
                    self.headerColors.push({
                        key: header.group[0].key,
                        colors: [header.group[0].color]
                    });
                    self.headerColors.push({
                        key: header.group[1].key,
                        colors: [header.group[1].color]
                    });
                } else {
                    if (header.color && header.color != "") {
                        self.headerColors.push({
                            key: header.key,
                            colors: [header.color]
                        });
                    }
                }
            });
        }

        formatDate(lstData) {
            var self = this;
            let data = lstData.map((data) => {
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
                    workplaceId: data.workplaceId,
                    employmentCode: data.employmentCode,
                    dateDetail: moment(data.date, "YYYY/MM/DD"),
                    typeGroup: data.typeGroup
                }
                _.each(data.cellDatas, function(item) {
                    object[item.columnKey] = item.value;
                });
                return object;
            });
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
            if (!$("#dpGrid").data("igGridPaging")) return;
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
            if (!$("#dpGrid").data("igGridPaging")) return;
            let currentPageIndex = $("#dpGrid").igGridPaging("option", "currentPageIndex");
            let pageSize = $("#dpGrid").igGridPaging("option", "pageSize");
            let startIndex: any = currentPageIndex * pageSize;
            let endIndex: any = startIndex + pageSize;
            let total = 0;
            _.forEach(data, function(d, i) {
                if (i < startIndex || i >= endIndex) return;
                if (d != "") {
                    total = total + moment.duration(d).asMinutes();
                }
            });
            let hours = total > 0 ? Math.floor(total / 60) : Math.ceil(total / 60);
            let minus = Math.abs(total % 60);
            minus = (minus < 10) ? '0' + minus : minus;
            return ((total < 0 && hours == 0) ? "-" + hours : hours) + ":" + minus;
        }

        totalMoney(data) {
            if (!$("#dpGrid").data("igGridPaging")) return;
            let total = 0;
            let currentPageIndex = $("#dpGrid").igGridPaging("option", "currentPageIndex");
            let pageSize = $("#dpGrid").igGridPaging("option", "pageSize");
            let startIndex: any = currentPageIndex * pageSize;
            let endIndex: any = startIndex + pageSize;
            _.forEach(data, function(d, i) {
                let valueResult = "";
                if (i < startIndex || i >= endIndex) return;
                if (String(d).indexOf(",") != -1) {
                    for (let i = 0; i < String(d).split(',').length; i++) {
                        valueResult += String(d).split(',')[i];
                    }
                    let n = parseFloat(valueResult);
                    if (!isNaN(n)) total += n;
                } else {
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
                targetBtnText: getText("KCP009_3"),
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
            if (false && false && false) {
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
                periodStartDate: self.dateRanger() == null ? new Date().toISOString() :  self.dateRanger().startDate, // 対象期間開始日
                periodEndDate: self.dateRanger() == null ? new Date().toISOString() : self.dateRanger().endDate, // 対象期間終了日
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
                    if (self.hasEmployee) {
                        self.loadKcp009();
                    }
                    self.dateRanger({ startDate: dataList.periodStart, endDate: dataList.periodEnd });
                    self.hasEmployee = true;
                    self.btnExtraction_Click();
                },
            }
        }

        createKeyLoad(): any {
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

        createDateList(startDate: any, endDate: any): any {
            var dateArray = [];
            var currentDate: any = moment(startDate);
            var stopDate: any = moment(endDate);
            while (currentDate <= stopDate) {
                dateArray.push(moment(currentDate).format('YYYY-MM-DD'))
                currentDate = moment(currentDate).add(1, 'days');
            }
            return dateArray;
        }
        convertDateToString(date: any): any {
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
            //self.setColorWeekend();
            //console.log(self.formatDate(self.dailyPerfomanceData()));
            self.createNtsMControl();
            self.lstDataSourceLoad = self.formatDate(_.cloneDeep(self.dailyPerfomanceData()));
            let startTime = performance.now();
            let subWidth = "50px";
            if (self.displayFormat() === 0) {
                subWidth = "135px";
            } else if (self.displayFormat() === 1) {
                subWidth = "135px";
            } else {
                subWidth = "155px";
            }
            new nts.uk.ui.mgrid.MGrid($("#dpGrid")[0], {
                subWidth : subWidth,
                height: (window.screen.availHeight - 250) + "px",
                headerHeight: '40px',
                dataSource: self.lstDataSourceLoad,
                minRows: 31,
                maxRows: 50,
                primaryKey: 'id',
                rowVirtualization: true,
                virtualization: true,
                virtualizationMode: 'continuous',
                enter: self.selectedDirection() == 0 ? 'below' : 'right',
                autoFitWindow: true,
                preventEditInError: false,
                columns: self.headersGrid,
                hidePrimaryKey: true,
                userId: self.employIdLogin,
                getUserId: function(primaryKey) {
                    let ids = primaryKey.split("_");
                    return ids[2] + "-" + ids[3] + "-" + ids[4] + "-" + ids[5] + "-" + ids[6];
                },
                features: [
                    //{ name: 'Paging', pageSize: 31, currentPageIndex: 0 },
                    { name: 'ColumnFixing', fixingDirection: 'left', showFixButtons: false, columnSettings: self.fixColGrid() },
                    { name: 'Resizing', columnSettings: [{ columnKey: 'id', allowResizing: false, minimumWidth: 0 }] },
                    { name: 'MultiColumnHeaders' },
                    {
                        name: 'Summaries',
                        columnSettings: self.columnSettings()
                    },
                    { name: 'HeaderStyles', columns: self.headerColors },
                    { name: 'CellStyles', states: self.cellStates() },
                    {
                        name: "Sheet",
                        initialDisplay: self.sheetsGrid()[0].name,
                        sheets: self.sheetsGrid()
                    },
                    { name: 'Copy' }
                ],
                ntsControls: self.ntsMControl
            }).create();
            console.log("load grid detail:" + (performance.now() - startTime));
        }

        createNtsFeatures() {
            var self = this;
            let lstNtsFeature = [
                { name: 'CopyPaste' },
                { name: 'CellEdit' },
                { name: 'CellState', rowId: 'rowId', columnKey: 'columnKey', state: 'state', states: self.cellStates() },
                { name: 'RowState', rows: self.rowStates() },
                { name: 'TextColor', rowId: 'rowId', columnKey: 'columnKey', color: 'color', colorsTable: self.textColors() },
                { name: 'HeaderStyles', columns: self.headerColors() },
                {
                    name: 'TextStyle',
                    rowId: 'rowId',
                    columnKey: 'columnKey',
                    style: 'style',
                    styles: self.textStyles
                },
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

        createNtsMControl(): void {
            let self = this;
            self.ntsMControl = [
                { name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                {
                    name: 'Link2',
                    click: function(rowId, key, event) {
                        if (!self.clickCounter.clickLinkGrid) {
                            self.clickCounter.clickLinkGrid = true;
                            let value = $("#dpGrid").mGrid("getCellValue", rowId, "Code" + key.substring(4, key.length));
                            let dialog: TypeDialog = new TypeDialog(key.substring(4, key.length), self.lstAttendanceItem(), value, rowId);
                            dialog.showDialog(self);
                            nts.uk.ui.block.clear();
                        }
                    },
                    controlType: 'LinkLabel'
                },
                { name: 'TextEditorNumberSeparated', controlType: 'TextEditor', constraint: { valueType: 'Integer', required: true, format: "Number_Separated" } },
                { name: 'TextEditorTimeShortHM', controlType: 'TextEditor', constraint: { valueType: 'Time', required: true, format: "Time_Short_HM" } },
                { name: 'ComboboxCalc', options: self.comboItemsCalc(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumnsCalc(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },
                { name: 'ComboboxReason', options: self.comboItemsReason(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumns(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },
                { name: 'ComboboxDoWork', options: self.comboItemsDoWork(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumns(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },
                { name: 'ComboItemsCompact', options: self.comboItemsCompact(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumns(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },
                { name: 'ComboboxTimeLimit', options: self.comboTimeLimit(), optionsValue: 'code', optionsText: 'name', columns: self.comboColumns(), editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true, spaceSize: 'small' },

                {
                    name: 'FlexImage', source: 'ui-icon ui-icon-locked', click: function(key, rowId, evt) {
                        let data = $("#dpGrid").mGrid("getCellValue", rowId, key);
                        if (data != "") {
                            let lock = data.split("|");
                            let tempD = "<span>";
                            for (let i = 1; i < lock.length; i++) {
                                if (lock[i] == "D" || lock[i] == "M") tempD += getText("KDW003_66") + '<br/>';
                                if (lock[i] == "C") tempD += getText("KDW003_67") + '<br/>';
                                if (lock[i] == "S") tempD += getText("KDW003_113") + '<br/>';
                                if (lock[i] == "CM") tempD += getText("KDW003_112") + '<br/>';
                                if (lock[i] == "AM") tempD += getText("KDW003_68") + '<br/>';
                                if (lock[i] == "H") tempD += getText("KDW003_70") + '<br/>';
                                if (lock[i] == "A") tempD += getText("KDW003_69") + '</span>';
                                $('#textLock').html(tempD);
                            }
                        }
                        self.helps(evt, "");
                    },
                    controlType: 'FlexImage'
                },
                { name: 'Image', source: 'image-icon', controlType: 'Image' },
                {
                    name: 'Button', controlType: 'Button', text: getText("KDW003_63"), enable: true, click: function(data) {
                        __viewContext.vm.clickButtonApplication(data);
                    }
                },
                {
                    name: 'ButtonList', controlType: 'Button', text: getText("KDW003_110"), enable: true, click: function(data) {
                        __viewContext.vm.clickButtonList(data);
                    }
                }
            ]
        }

        loadMIGrid(data: any): void {
            let self = this,
                dataSourceMIGrid: any[] = [{ monthYear: self.monthYear() }],
                columnsMIGrid: any[] = [{ headerText: getText("KDW003_40"), key: 'monthYear', dataType: 'string', width: '75px' }],
                totalWidthColumn: number = 75,
                maxWidth: number = window.screen.availWidth - 200;

            _.forEach(self.listAttendanceItemId(), (id) => {
                let attendanceItemId: any = _.find(data, { 'attendanceItemId': id.itemId }),
                    cDisplayType = self.convertToCDisplayType(id.valueType);
                columnsMIGrid.push(
                    {
                        headerText: attendanceItemId.attendanceItemName, key: '_' + attendanceItemId.attendanceItemId, width: id.columnWidth + 'px',
                        constraint: {
                            cDisplayType: cDisplayType,
                            required: false
                        }
                    }
                );
                dataSourceMIGrid[0]['_' + attendanceItemId.attendanceItemId] = (id.value != null && cDisplayType == 'Clock') ? self.convertToHours(id.value) : id.value;
                totalWidthColumn += id.columnWidth;
            });

            $("#miGrid").ntsGrid({
                width: (totalWidthColumn >= maxWidth) ? maxWidth + 'px' : totalWidthColumn + 'px',
                dataSource: dataSourceMIGrid,
                primaryKey: 'monthYear',
                autoFitWindow: false,
                columns: columnsMIGrid,
                features: [
                    { name: 'ColumnFixing', fixingDirection: 'left', showFixButtons: false, columnSettings: [{ columnKey: 'monthYear', isFixed: true }] },
                    { name: 'Resizing' }
                ],
                ntsFeatures: [],
                ntsControls: []
            });
        }

        convertToCDisplayType(value: string): string {
            switch (value) {
                case "TIME":
                case "CLOCK":
                    return "Clock";
                case "DAYS": return "Decimal";
                case "COUNT": return "Integer";
                case "CURRENCY": return "Currency";
                default: return "String";
            }
        }

        clickButtonApplication(data) {
            let self = this;
            let source: any = $("#dpGrid").mGrid("dataSource");
            let rowItemSelect: any = _.find(source, function(value: any) {
                return value.id == data.id;
            })
            let dfd = $.Deferred();
            let dataShare: any = {
                date: rowItemSelect.dateDetail.format("YYYY/MM/DD"),
                listValue: []
            }
            let date = moment(dataShare.date, "YYYY/MM/DD");
            $.when(service.getApplication()).done((data) => {
                dataShare.listValue = data;
                setShared("shareToKdw003e", dataShare);
                modal("/view/kdw/003/e/index.xhtml").onClosed(() => {
                    let screen = nts.uk.ui.windows.getShared("shareToKdw003a");
                    if (screen == undefined) screen = 1905;
                    let transfer = {
                        appDate: dataShare.date,
                        uiType: 1,
                        employeeIDs: [],
                        stampRequestMode: 1,
                        screenMode: 0
                    };
                    switch (screen) {
                        case 0:
                            //KAF005-残業申請（早出）
                            nts.uk.request.jump("/view/kaf/005/a/index.xhtml?overworkatr=0", transfer);
                            break;

                        case 1:
                            //KAF005-残業申請（通常） 
                            nts.uk.request.jump("/view/kaf/005/a/index.xhtml?overworkatr=1", transfer);
                            break;

                        case 2:
                            //KAF005-残業申請（早出・通常）
                            nts.uk.request.jump("/view/kaf/005/a/index.xhtml?overworkatr=2", transfer);
                            break;

                        case 3:
                            //KAF006-休暇申請
                            nts.uk.request.jump("/view/kaf/006/a/index.xhtml", transfer);
                            break;

                        case 4:
                            //KAF007-勤務変更申請
                            nts.uk.request.jump("/view/kaf/007/a/index.xhtml", transfer);
                            break;

                        //                        case 5:
                        //                            //KAF008-出張申請
                        //                            nts.uk.request.jump("/view/kaer);
                        //                            break;

                        case 6:
                            //KAF009-直行直帰申請
                            nts.uk.request.jump("/view/kaf/009/a/index.xhtml", transfer);
                            break;

                        case 7:
                            //KAF010-休出時間申請
                            nts.uk.request.jump("/view/kaf/010/a/index.xhtml", transfer);
                            break;

                        case 8:
                            //KAF002-打刻申請（外出許可）
                            transfer.stampRequestMode = 0;
                            transfer.screenMode = 1;
                            nts.uk.request.jump("/view/kaf/002/b/index.xhtml", transfer);
                            break;

                        case 9:
                            //KAF002-打刻申請（出退勤打刻漏れ）
                            transfer.stampRequestMode = 1;
                            transfer.screenMode = 1;
                            nts.uk.request.jump("/view/kaf/002/b/index.xhtml", transfer);
                            break;

                        case 10:
                            //KAF002-打刻申請（打刻取消）
                            transfer.stampRequestMode = 2;
                            transfer.screenMode = 1;
                            nts.uk.request.jump("/view/kaf/002/b/index.xhtml", transfer);
                            break;

                        case 11:
                            //KAF002-打刻申請（レコーダイメージ）
                            transfer.stampRequestMode = 3;
                            transfer.screenMode = 1;
                            nts.uk.request.jump("/view/kaf/002/b/index.xhtml", transfer);
                            break;

                        case 12:
                            //KAF002-打刻申請（その他）
                            transfer.stampRequestMode = 4;
                            transfer.screenMode = 1;
                            nts.uk.request.jump("/view/kaf/002/b/index.xhtml", transfer);
                            break;

                        //                        case 14:
                        //                            //KAF004-遅刻早退取消申請
                        //                            nts.uk.request.jump("/view/kaer);
                        //                            break;

                        case 15:
                            //KAF011-振休振出申請
                            nts.uk.request.jump("/view/kaf/011/a/index.xhtml", transfer);
                            break;
                        default:
                            break;
                    }
                });
                dfd.resolve();
            });
            dfd.promise();
        }

        clickButtonList(data) {

            let self = this;
            let source: any = $("#dpGrid").mGrid("dataSource");
            let rowItemSelect: any = _.find(source, function(value: any) {
                return value.id == data.id;
            })

            let dataShareCmm = {
                appListAtr: 0,
                appType: -1,
                unapprovalStatus: true,
                approvalStatus: false,//false
                denialStatus: false,//false
                agentApprovalStatus: false,//false
                /**承認状況＿差戻*/
                remandStatus: false,//false 
                /**承認状況＿取消*/
                cancelStatus: false,//false
                /**申請表示対象*/
                appDisplayAtr: 0,//0
                /**社員IDリスト*/
                listEmployeeId: [rowItemSelect.employeeId],//[]
                /**社員絞込条件*/
                empRefineCondition: "",//'' ,
                startDate: __viewContext.vm.displayFormat() === 1 ? moment(__viewContext.vm.selectedDate()).format("YYYY/MM/DD") : moment(__viewContext.vm.dateRanger().startDate).format("YYYY/MM/DD"),
                endDate: __viewContext.vm.displayFormat() === 1 ? moment(__viewContext.vm.selectedDate()).format("YYYY/MM/DD") : moment(__viewContext.vm.dateRanger().endDate).format("YYYY/MM/DD")

            }
            nts.uk.localStorage.setItem('UKProgramParam', 'a=0');
            nts.uk.characteristics.save('AppListExtractCondition', dataShareCmm);
            nts.uk.request.jump("/view/cmm/045/a/index.xhtml");

        }

        reloadGrid() {
            var self = this;
            nts.uk.ui.block.invisible();
            nts.uk.ui.block.grayout();
            //            self.destroyGrid();
            //            self.createSumColumn(self.dataAll());
            //            self.columnSettings(self.dataAll().lstControlDisplayItem.columnSettings);
            //            self.receiveData(self.dataAll());
            //            self.extractionData();
            //            self.loadGrid();
            self.extraction();
            nts.uk.ui.block.clear();
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
                //self.displayProfileIcon(mode);
                _.forEach(self.dateModeHeader, (header) => {
                    if (header.constraint == null) {
                        delete header.constraint;
                    }
                    delete header.group;
                    tempList.push(header);
                });
            } else if (mode == 2) {
                //self.displayProfileIcon(mode); 
                _.forEach(self.errorModeHeader, (header) => {
                    if (header.constraint == null) {
                        delete header.constraint;
                    }
                    delete header.group;
                    tempList.push(header);
                });
            }
            _.forEach(self.optionalHeader, (header) => {
                if (header.inputProcess != null && header.inputProcess != undefined) {
                    header.inputProcess = self.inputProcess;
                } else {
                    delete header.inputProcess;
                }
                if (header.constraint == null || header.constraint == undefined) {
                    delete header.constraint;
                } else {
                    header.constraint["cDisplayType"] = header.constraint.cdisplayType;
                    if (header.constraint.cDisplayType != null && header.constraint.cDisplayType != undefined) {
                        if (header.constraint.cDisplayType != "Primitive" && header.constraint.cDisplayType != "Combo") {
                            if (header.constraint.cDisplayType.indexOf("Currency") != -1) {
                                header["columnCssClass"] = "currency-symbol";
                                header.constraint["min"] = header.constraint.min;
                                header.constraint["max"] = header.constraint.max;
                            } else if (header.constraint.cDisplayType == "Clock") {
                                header["columnCssClass"] = "halign-right";
                                header.constraint["min"] = header.constraint.min;
                                header.constraint["max"] = header.constraint.max;
                            } else if (header.constraint.cDisplayType == "Integer") {
                                header["columnCssClass"] = "halign-right";
                            } else if (header.constraint.cDisplayType == "TimeWithDay") {
                                header.constraint["min"] = "-12:00";
                                header.constraint["max"] = "71:59"
                            }
                            delete header.constraint.primitiveValue;
                        } else {

                            if (header.constraint.cDisplayType == "Primitive") {
                                if (header.group == undefined || header.group.length == 0) {
                                    delete header.constraint.cDisplayType;
                                    if (header.constraint.primitiveValue.indexOf("AttendanceTime") != -1) {
                                        header["columnCssClass"] = "halign-right";
                                    }
                                    if (header.constraint.primitiveValue == "BreakTimeGoOutTimes" || header.constraint.primitiveValue == "WorkTimes" || "AnyItemTimes" || "AnyTimesMonth") {
                                        header["columnCssClass"] = "halign-right";
                                        header.constraint["decimallength"] = 2;
                                    }
                                } else {
                                    delete header.group[0].constraint.cDisplayType;
                                    delete header.group[0].values;
                                    delete header.constraint;
                                    delete header.group[1].constraint;
                                }
                            } else if (header.constraint.cDisplayType == "Combo") {
                                if (header.group[0].constraint.values == undefined && header.group[0].constraint != null){
                                    header.group[0].constraint["min"] = 0;
                                    header.group[0].constraint["max"] = Number(header.group[0].constraint.primitiveValue);
                                    delete header.group[0].values;
                                }else{
                                     delete header.group[0].constraint.min;
                                     delete header.group[0].constraint.max;
                                }
                                header.group[0].constraint["cDisplayType"] = header.group[0].constraint.cdisplayType;
                                delete header.group[0].constraint.cdisplayType
                                delete header.group[0].constraint.primitiveValue;
                                delete header.constraint;
                                delete header.group[1].constraint;
                            }
                        }
                    }
                    if (header.constraint != undefined) {
                       delete header.constraint.cdisplayType;
                       delete header.constraint.values; 
                    }
                }
                if (header.group != null && header.group != undefined) {
                    if (header.group.length > 0) {
                        if (header.group[0].constraint == undefined) delete header.group[0].constraint;
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
                        delete header.group[1].inputProcess;
                        delete header.inputProcess;

                        if (header.group[0].inputProcess != null && header.group[0].inputProcess != undefined) {
                            header.group[0].inputProcess = self.inputProcess;
                        } else {
                            delete header.group[0].inputProcess;
                        }

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
            self.headersGrid = tempList;
            return self.headersGrid;
        }

        pushDataEdit(evt, ui): InfoCellEdit {
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
                dataEdit = new InfoCellEdit(ui.rowID, ui.columnKey, ui.value, 1, "", employeeIdSelect.employeeId, moment(dateCon.date).utc().toISOString(), 0);
                self.editValue.push(dataEdit);
                return dataEdit;
            }
        }

        displayProfileIcon(mode) {
            var self = this;
            if (!self.hasEmployee) return;
            if (mode == 0) return;
            if (self.showProfileIcon()) {
                $("#dpGrid").mGrid("showColumn", "picture-person");
            } else {
                $("#dpGrid").mGrid("hideColumn", "picture-person");
            }
        }

        dislayNumberHeaderText() {
            var self = this;
            if (!self.hasEmployee) return;
            let headerText;
            _.each(self.optionalHeader, header => {
                if (header.headerText != "提出済みの申請" && header.headerText != "申請" && header.headerText != "申請一覧") {
                    if (header.group == undefined && header.group == null) {
                        if (self.showHeaderNumber()) {
                            headerText = header.headerText + " " + header.key.substring(1, header.key.length);
                            $("#dpGrid").mGrid("headerText", header.key, headerText, false);
                        } else {
                            headerText = header.headerText.split(" ")[0];
                            $("#dpGrid").mGrid("headerText", header.key, headerText, false);
                        }
                    } else {
                        if (self.showHeaderNumber()) {
                            headerText = header.headerText + " " + header.group[1].key.substring(4, header.group[1].key.length);
                            $("#dpGrid").mGrid("headerText", header.headerText, headerText, true);
                        } else {
                            headerText = header.headerText.split(" ")[0];
                            $("#dpGrid").mGrid("headerText", headerText + " " + header.group[1].key.substring(4, header.group[1].key.length), headerText, true);
                        }
                    }
                }
            });
        }

        displayNumberZero() {
            let self = this;
            if (!self.hasEmployee) return;
            if (!self.displayWhenZero()) {
                $("#dpGrid").mGrid("hideZero", true)
            } else {
                $("#dpGrid").mGrid("hideZero", false)
            }
        }

        search(columnKey, rowId, val, valOld) {
            let dfd = $.Deferred();
            let i = 0;
            let data: any = $("#dpGrid").mGrid("dataSource");
            let rowItemSelect: any = _.find(data, function(value: any) {
                return value.id == rowId;
            })
            let typeGroup;
            let splitTypeGroup = rowItemSelect.typeGroup.split("|");
            if (splitTypeGroup != undefined) {
                typeGroup = _.find(splitTypeGroup, function(value: any) {
                    let splitData = value.split(":");
                    return "Code" + splitData[0] === columnKey;
                })
            }

            if (typeGroup != undefined && typeGroup != null) {
                let param = {
                    typeDialog: typeGroup.split(":")[1],
                    param: {
                        workTypeCode: typeGroup.split(":")[1] == 4 ? columnKey.substring(4, columnKey.length) : val,
                        employmentCode: rowItemSelect.employmentCode,
                        workplaceId: rowItemSelect.workplaceId,
                        date: rowItemSelect.dateDetail,
                        selectCode: val,
                        employeeId: rowItemSelect.employeeId,
                        itemId: Number(columnKey.substring(4, columnKey.length)),
                        valueOld: valOld
                    }
                }
                var object = {};
                if (val == "") {
                    dfd.resolve(getText("KDW003_82"));
                } else {
                    $.when(service.findCodeName(param)).done((data) => {
                        if (data != undefined) {
                            let typeError = _.find(__viewContext.vm.workTypeNotFound, data => {
                                return data.columnKey == columnKey && data.rowId == rowId;
                            });
                            if (data.errorFind == 1) {
                                if (typeError == undefined) {
                                    __viewContext.vm.workTypeNotFound.push({ columnKey: columnKey, rowId: rowId, message: nts.uk.resource.getMessage("Msg_1293") });
                                } else {
                                    typeError.message = nts.uk.resource.getMessage("Msg_1293");
                                }
                                nts.uk.ui.dialog.alertError({ messageId: "Msg_1293" })
                            } else if (data.errorFind == 2) {
                                if (typeError == undefined) {
                                    __viewContext.vm.workTypeNotFound.push({ columnKey: columnKey, rowId: rowId, message: nts.uk.resource.getMessage("Msg_1314") });
                                } else {
                                    typeError.message = nts.uk.resource.getMessage("Msg_1314");
                                }
                                nts.uk.ui.dialog.alertError({ messageId: "Msg_1314" })
                            } else {
                                _.remove(__viewContext.vm.workTypeNotFound, dataTemp => {
                                    return dataTemp.columnKey == columnKey && dataTemp.rowId == rowId;
                                });
                            }

                            dfd.resolve(data == undefined ? getText("KDW003_81") : data.name);
                        } else {
                            _.remove(__viewContext.vm.workTypeNotFound, dataTemp => {
                                return dataTemp.columnKey == columnKey && dataTemp.rowId == rowId;
                            });
                            dfd.resolve(data == undefined ? getText("KDW003_81") : data.name);
                        }
                    });
                }

            } else {
                dfd.resolve("");
            }
            return dfd.promise();
        }

        inputProcess(rowId, columnKey, value) {
            let dfd = $.Deferred(),
                keyId: any,
                valueError: any,
                dataChange: any,
                dataChageRow: any;
            __viewContext.vm.flagCalculation = false;
            $("#next-month").ntsError("clear");
            
            if (columnKey.indexOf("Code") != -1) {
                keyId = columnKey.substring(4, columnKey.length);
                valueError = _.find(__viewContext.vm.workTypeNotFound, data => {
                    return data.columnKey == columnKey && data.rowId == rowId;
                });
                _.remove(__viewContext.vm.listCheck28(), error =>{
                     return Number(error.itemId) == Number(keyId) && error.rowId == rowId;
                })
            } else {
                if (columnKey.indexOf("NO") != -1) {
                    keyId = columnKey.substring(2, columnKey.length);
                } else {
                    keyId = columnKey.substring(1, columnKey.length);
                }
            }

            let itemChange = _.find(ITEM_CHANGE, function(o) { return o === Number(keyId); });
            if (itemChange == undefined) {
                dfd.resolve({ id: rowId, item: columnKey, value: value });
            }
            else {
                if (valueError != undefined) {
                    dfd.resolve({ id: rowId, item: columnKey, value: value })
                } else {
                    nts.uk.ui.block.invisible();
                    nts.uk.ui.block.grayout();
                    
                    let dataTemp = _.find(__viewContext.vm.lstDataSourceLoad, (item: any) => {
                        return item.id == rowId;
                    });

                    let layoutAndType: any = _.find(__viewContext.vm.itemValueAll(), (item: any) => {
                        return item.itemId == keyId;
                    });
                    let item = _.find(__viewContext.vm.lstAttendanceItem(), (value) => {
                        return String(value.id) === keyId;
                    })
                    let valuePrimitive: any = __viewContext.vm.getPrimitiveValue(value, item.attendanceAtr);
                    let dataMap = new InfoCellEdit(rowId, Number(keyId), valuePrimitive, layoutAndType == undefined ? "" : layoutAndType.valueType, layoutAndType == undefined ? "" : layoutAndType.layoutCode, dataTemp.employeeId, dataTemp.dateDetail.utc().toISOString(), item.typeGroup, columnKey);

                    // get item change in row
                    dataChange = $("#dpGrid").mGrid("updatedCells");
                    dataChageRow = _.map(_.filter(dataChange, row => {
                        return row.columnKey != "sign" && row.columnKey != "approval" && row.columnKey.indexOf("Name") == -1 && row.rowId == rowId && row.columnKey != columnKey;
                    }), allValue => {
                        let itemTemp, keyIdRow;
                        keyIdRow = Number(allValue.columnKey.replace(/\D/g, ""));
                        itemTemp = _.find(__viewContext.vm.lstAttendanceItem(), (value) => {
                        return value.id === keyIdRow;
                         })
                       let valueTemp: any = __viewContext.vm.getPrimitiveValue(allValue.value, itemTemp.attendanceAtr);
                       let layoutTemp: any = _.find(__viewContext.vm.itemValueAll(), (item: any) => {
                           return item.itemId == keyIdRow;
                       });
                       return new InfoCellEdit(rowId, keyIdRow, valueTemp, layoutTemp == undefined ? "" : layoutTemp.valueType, layoutTemp == undefined ? "" : layoutTemp.layoutCode, dataTemp.employeeId, dataTemp.dateDetail.utc().toISOString(), itemTemp.typeGroup, "USE");
                    });
                    dataChageRow.push(dataMap);
                    
                    let param = {
                        //dailyEdits: __viewContext.vm.lstDomainEdit,
                        itemEdits: dataChageRow
                    };
                    service.calcTime(param).done((value) => {
                        //__viewContext.vm.lstDomainEdit = value.dailyEdits;
                        _.each(value.cellEdits, itemResult => {
                            $("#dpGrid").mGrid("updateCell", itemResult.id, itemResult.item, itemResult.value, true);
                        })
                        nts.uk.ui.block.clear();
                        //dfd.resolve(value.cellEdits);
                        dfd.resolve({});
                    }).fail(error => {
                         __viewContext.vm.listCheck28.push({ itemId: keyId, layoutCode: error.message, rowId: rowId});
                         nts.uk.ui.dialog.alertError({ messageId: error.messageId });
                         nts.uk.ui.block.clear();
                         dfd.resolve({ id: rowId, item: columnKey, value: value });
                    });
                }
            }
            return dfd.promise();
        }

        getDataShare(data: any) {
            var self = this;
            var param = {
                dateRange: data.dateRangeParam ? {
                    startDate: moment(data.dateRangeParam.startDate).utc().toISOString(),
                    endDate: moment(data.dateRangeParam.endDate).utc().toISOString()
                } : null,
                displayFormat: 0,
                initScreen: 0,
                lstEmployee: [],
                formatCodes: self.formatCodes()
            };
        }

        navigateView() {
            //
            var self = this;
            let path: any = _.isEmpty(self.shareObject()) ? "" : self.shareObject().transitionDesScreen;
            nts.uk.request.jump("at", path);
        }

        openKDL020Dialog() {
            let self = this;
            setShared('KDL020A_PARAM', { baseDate: new Date(), employeeIds: [self.selectedEmployee()] });
            modal('/view/kdl/020/a/index.xhtml');
        }

        openKDL009Dialog() {
            var self = this;
            var param = {
                employeeIds: [self.selectedEmployee()],
                baseDate: moment(new Date()).toISOString().split("T")[0].replace('-', '').replace('-', '')
            };
            setShared('KDL009_DATA', param);
            //            if(param.employeeIds.length > 1) {
            //                modal("/view/kdl/009/a/multi.xhtml");
            //            } else {
            modal("/view/kdl/009/a/single.xhtml");
            //            }             
        }

        openkdl029Dialog() {
            let self = this;
            let param = {
                employeeIds: [self.selectedEmployee()],
                baseDate: moment(new Date()).format("YYYY/MM/DD")
            }
            setShared('KDL029_PARAM', param);
            modal('/view/kdl/029/a/index.xhtml');
        }

        openKDL005Dialog() {
            var self = this;
            var param = {
                employeeIds: [self.selectedEmployee()],
                baseDate: moment(new Date()).toISOString().split("T")[0].replace('-', '').replace('-', '')
            };
            setShared('KDL005_DATA', param);
            //            if(param.employeeIds.length > 1) {
            //                modal("/view/kdl/005/a/multi.xhtml");
            //            } else {
            modal("/view/kdl/005/a/single.xhtml");
            //            }
        }

    }

    export class AuthorityDetailModel {
        available1: KnockoutObservable<boolean> = ko.observable(false);
        available4: KnockoutObservable<boolean> = ko.observable(false);
        available2: KnockoutObservable<boolean> = ko.observable(false);
        available3: KnockoutObservable<boolean> = ko.observable(false);
        available5: KnockoutObservable<boolean> = ko.observable(false);
        available8: KnockoutObservable<boolean> = ko.observable(false);
        available9: KnockoutObservable<boolean> = ko.observable(false);
        available8Authority: KnockoutObservable<boolean> = ko.observable(false);
        available11: KnockoutObservable<boolean> = ko.observable(false);
        available12: KnockoutObservable<boolean> = ko.observable(false);
        available22: KnockoutObservable<boolean> = ko.observable(false);
        available24: KnockoutObservable<boolean> = ko.observable(false);
        available7: KnockoutObservable<boolean> = ko.observable(false);
        available23: KnockoutObservable<boolean> = ko.observable(false);
        available25: KnockoutObservable<boolean> = ko.observable(false);
        available17: KnockoutObservable<boolean> = ko.observable(false);
        available18: KnockoutObservable<boolean> = ko.observable(false);
        available19: KnockoutObservable<boolean> = ko.observable(false);
        available20: KnockoutObservable<boolean> = ko.observable(false);
        available21: KnockoutObservable<boolean> = ko.observable(false);
        constructor(data: Array<DailyPerformanceAuthorityDto>, authority: any, showCheckbox) {
            var self = this;
            if (!data) return;
            this.available1(self.checkAvailable(data, 1));
            this.available4(self.checkAvailable(data, 4));
            this.available2(self.checkAvailable(data, 2));
            this.available3(self.checkAvailable(data, 3));
            this.available5(self.checkAvailable(data, 5));
            this.available8(self.checkAvailable(data, 8));
            this.available9(self.checkAvailable(data, 9));
            this.available12(self.checkAvailable(data, 12));
            this.available22(self.checkAvailable(data, 22));
            this.available24(self.checkAvailable(data, 24));
            this.available7(self.checkAvailable(data, 7));
            this.available23(self.checkAvailable(data, 23));
            this.available25(self.checkAvailable(data, 25));
            if (self.checkAvailable(data, 25) && showCheckbox) {
                $("#btn-signAll").css("visibility", "visible");
                $("#btn-releaseAll").css("visibility", "visible");

            } else {
                $("#btn-signAll").css("visibility", "hidden");
                $("#btn-releaseAll").css("visibility", "hidden");
            }
            // $("#btn-signAll").css("visibility", "hidden");
            this.available17(self.checkAvailable(data, 17));
            this.available18(self.checkAvailable(data, 18));
            this.available19(self.checkAvailable(data, 19));
            this.available20(self.checkAvailable(data, 20));
            this.available21(self.checkAvailable(data, 21));
            this.available8Authority(this.available8() && authority)
            this.available11(self.checkAvailable(data, 11));

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
        yearHoliday20: KnockoutObservable<boolean> = ko.observable(false);
        yearHoliday19: KnockoutObservable<boolean> = ko.observable(false);
        substVacation: KnockoutObservable<boolean> = ko.observable(false);
        compensLeave: KnockoutObservable<boolean> = ko.observable(false);
        com60HVacation: KnockoutObservable<boolean> = ko.observable(false);
        authentication: KnockoutObservable<AuthorityDetailModel> = ko.observable(null);
        allVacation: KnockoutObservable<boolean> = ko.observable(false);

        constructor(yearHoliday: any, reserveHoliday: any, substVacation: any, compensLeave: any, com60HVacation: any, authentication: AuthorityDetailModel) {
            var self = this;
            this.yearHoliday20(yearHoliday && authentication.available20());
            this.substVacation(substVacation && authentication.available18());
            this.yearHoliday19(reserveHoliday && authentication.available19());
            this.compensLeave(compensLeave && authentication.available17());
            this.com60HVacation(com60HVacation && authentication.available21());
            this.allVacation(this.yearHoliday20() || this.substVacation() || this.yearHoliday19() || this.compensLeave() || this.com60HVacation());
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
            item = _.find(self.data, function(data) {
                return data.id == self.attendenceId;
            })
            switch (item.typeGroup) {
                case 1:
                    // KDL002  
                    let dfd = $.Deferred();
                    let dataSource: any = $("#dpGrid").mGrid("dataSource");
                    let rowItemSelect: any = _.find(dataSource, function(value: any) {
                        return value.id == self.rowId();
                    })

                    let dataSourceOld: any = selfParent.formatDate(selfParent.dailyPerfomanceData());
                    let item28Old = _.find(dataSourceOld, valueData => {
                        return valueData.id == self.rowId() && valueData["Code28"] != undefined;
                    });

                    let param1 = {
                        typeDialog: 1,
                        param: {
                            workTypeCode: self.selectedCode(),
                            employmentCode: selfParent.employmentCode(),
                            workplaceId: "",
                            date: rowItemSelect.dateDetail,
                            selectCode: self.selectedCode(),
                            employeeId: rowItemSelect.employeeId,
                            itemId: item.id,
                            valueOld: item28Old == undefined ? null : item28Old["Code28"]
                        }
                    }
                    service.findAllCodeName(param1).done((data) => {
                        self.listCode([]);
                        self.listCode(_.map(data, 'code'));
                        setShared('KDL002_Multiple', false, true);
                        //all possible items
                        setShared('KDL002_AllItemObj', nts.uk.util.isNullOrEmpty(self.listCode()) ? [] : self.listCode(), true);
                        //selected items
                        setShared('KDL002_SelectedItemId', [self.selectedCode()], true);
                        modal('/view/kdl/002/a/index.xhtml', { title: '乖離時間の登録＞対象項目', }).onClosed(function(): any {
                            var lst = nts.uk.ui.windows.getShared('KDL002_SelectedNewItem');
                            if (lst != undefined && lst[0].code != "") {
                                self.updateCodeName(self.rowId(), self.attendenceId, lst[0].name, lst[0].code, self.selectedCode());
                            } else {
                                self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                            }
                        })
                        dfd.resolve();
                    });
                    dfd.promise();
                    break;
                case 2:
                    //KDL001 
                    let dfd2 = $.Deferred();
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
                        setShared('kml001multiSelectMode', false);
                        setShared('kml001selectAbleCodeList', nts.uk.util.isNullOrEmpty(self.listCode()) ? [] : self.listCode());
                        setShared('kml001selectedCodeList', [self.selectedCode()]);
                        setShared('kml001isSelection', true);
                        modal("/view/kdl/001/a/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                            let codes: any = nts.uk.ui.windows.getShared("kml001selectedCodeList");
                            if (codes) {
                                codeName = _.find(data, (item: any) => {
                                    return item.code == codes[0];
                                });
                                if (codeName != undefined && codes[0] != "") {
                                    self.updateCodeName(self.rowId(), self.attendenceId, codeName.name, codeName.code, self.selectedCode());
                                } else {
                                    self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                                }
                            }
                        });
                        dfd2.resolve();
                    });
                    dfd2.promise()
                    break;
                case 3:
                    //KDL010 
                    let dfd3 = $.Deferred();
                    nts.uk.ui.block.invisible();
                    setShared('KDL010SelectWorkLocation', self.selectedCode());
                    modal("/view/kdl/010/a/index.xhtml", { dialogClass: "no-close" }).onClosed(() => {
                        var self = this;
                        var returnWorkLocationCD = nts.uk.ui.windows.getShared("KDL010workLocation");
                        if (returnWorkLocationCD !== undefined && returnWorkLocationCD != "") {
                            let dataKDL: any;
                            let param3 = {
                                typeDialog: 3
                            }
                            service.findAllCodeName(param3).done((data: any) => {
                                codeName = _.find(data, (item: any) => {
                                    return item.code == returnWorkLocationCD;
                                });
                                if (codeName != undefined) {
                                    self.updateCodeName(self.rowId(), self.attendenceId, codeName.name, codeName.code, self.selectedCode());
                                }
                                dfd3.resolve();
                            });
                        }
                        else {
                            if (returnWorkLocationCD == "") self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                            dfd3.resolve();
                        }
                    });
                    dfd3.promise()
                    break;
                case 4:
                    //KDL032
                    let dfd4 = $.Deferred();
                    let no = DEVIATION_REASON_MAP[self.attendenceId];
                    nts.uk.ui.block.invisible();
                    let dataSetShare = {
                        reasonCD: self.selectedCode(),
                        divergenceTimeID: no
                    };
                    setShared('KDL032', dataSetShare);
                    modal("/view/kdl/032/a/index.xhtml", { dialogClass: "no-close" }).onClosed(() => {
                        var self = this;
                        var returnData = nts.uk.ui.windows.getShared("ReturnData");
                        if (returnData !== undefined && returnData != "") {
                            let dataKDL: any;
                            let param3 = {
                                typeDialog: 4,
                                param: {
                                    workTypeCode: no,
                                    selectCode: self.selectedCode()
                                }
                            };
                            service.findAllCodeName(param3).done((data: any) => {
                                let codeName = _.find(data, (item: any) => {
                                    return item.code == returnData && item.id == "" + no;
                                });
                                self.updateCodeName(self.rowId(), self.attendenceId, codeName.name, codeName.code, self.selectedCode());
                                dfd4.resolve();
                            });
                        }
                        else {
                            if (returnData == "") self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                            nts.uk.ui.block.clear();
                            dfd4.resolve();
                        }
                    });
                    dfd4.promise()
                    break;
                case 5:
                    //CDL008 
                    let dateCon = selfParent.displayFormat() === 1 ? moment(selfParent.selectedDate()) : moment(selfParent.dateRanger().endDate).utc().toISOString();

                    let param5 = {
                        typeDialog: 5,
                        param: {
                            date: dateCon
                        }
                    }
                    let data5: any;
                    let dfd5 = $.Deferred();
                    service.findAllCodeName(param5).done((data: any) => {
                        data5 = data;
                        codeName = _.find(data5, (item: any) => {
                            return item.code == self.selectedCode();
                        });
                        //                        setShared('inputCDL008', {
                        //                            selectedCodes: codeName == undefined ? "" : codeName.id,
                        //                            baseDate: dateCon,
                        //                            isMultiple: false
                        //                        }, true);
                        setShared('inputCDL008', {
                            selectedCodes: codeName == undefined ? "" : codeName.id,
                            baseDate: dateCon,
                            isMultiple: false,
                            selectedSystemType: 2,
                            isrestrictionOfReferenceRange: true,
                            showNoSelection: false,
                            isShowBaseDate: false
                        }, true);
                        dfd5.resolve()
                    });
                    dfd5.promise();
                    modal('com', '/view/cdl/008/a/index.xhtml').onClosed(function(): any {
                        // Check is cancel.
                        if (nts.uk.ui.windows.getShared('CDL008Cancel')) {
                             __viewContext.vm.clickCounter.clickLinkGrid = false;
                            return;
                        }
                        //view all code of selected item 
                        var output = nts.uk.ui.windows.getShared('outputCDL008');
                        if (output != "") {
                            codeName = _.find(data5, (item: any) => {
                                return item.id == output;
                            });
                            self.updateCodeName(self.rowId(), self.attendenceId, codeName.name, codeName.code, self.selectedCode());
                        } else {
                            self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                        }
                    })
                    break;
                case 6:
                    //KCP002
                    let dfd6 = $.Deferred();
                    setShared('inputCDL003', {
                        selectedCodes: self.selectedCode(),
                        showNoSelection: false,
                        isMultiple: false
                    }, true);

                    modal('com', '/view/cdl/003/a/index.xhtml').onClosed(function(): any {
                        //view all code of selected item 
                        var output = nts.uk.ui.windows.getShared('outputCDL003');
                        if (output != undefined && output != "") {
                            let param6 = {
                                typeDialog: 6
                            }
                            service.findAllCodeName(param6).done((data: any) => {
                                codeName = _.find(data, (item: any) => {
                                    return item.code == output;
                                });
                                self.updateCodeName(self.rowId(), self.attendenceId, codeName.name, codeName.code, self.selectedCode());
                                dfd.resolve()
                            });
                        } else {
                             __viewContext.vm.clickCounter.clickLinkGrid = false;
                            if (output == "") self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                            dfd6.resolve()
                        }
                    })
                    dfd6.promise();
                    break;
                case 7:
                    //KCP003 
                    let dateCon7 = selfParent.displayFormat() === 1 ? moment(selfParent.selectedDate()) : moment(selfParent.dateRanger().endDate).utc().toISOString();
                    let param7 = {
                        typeDialog: 7,
                        param: {
                            date: dateCon7
                        }
                    }
                    let data7: any;
                    let dfd7 = $.Deferred();
                    service.findAllCodeName(param7).done((data: any) => {
                        data7 = data;
                        codeName = _.find(data, (item: any) => {
                            return item.code == self.selectedCode();
                        });
                        setShared('inputCDL004', {
                            baseDate: dateCon7,
                            selectedCodes: codeName == undefined ? "" : codeName.id,
                            showNoSelection: false,
                            isMultiple: false,
                            isShowBaseDate: false
                        }, true);
                        dfd7.resolve();
                    });
                    dfd7.promise();
                    modal('com', '/view/cdl/004/a/index.xhtml').onClosed(function(): any {
                        var isCancel = nts.uk.ui.windows.getShared('CDL004Cancel');
                        if (isCancel) {
                            __viewContext.vm.clickCounter.clickLinkGrid = false;
                            return;
                        }
                        var output = nts.uk.ui.windows.getShared('outputCDL004');
                        if (output != "") {
                            codeName = _.find(data7, (item: any) => {
                                return item.id == output;
                            });
                            self.updateCodeName(self.rowId(), self.attendenceId, codeName.name, codeName.code, self.selectedCode());
                        } else {
                            self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                        }
                    })
                    break;
                case 8:
                    let dfd8 = $.Deferred();
                    setShared('CDL002Params', {
                        isMultiple: false,
                        selectedCodes: self.selectedCode(),
                        showNoSelection: false
                    }, true);

                    modal('com', '/view/cdl/002/a/index.xhtml').onClosed(function(): any {
                        // nts.uk.ui.block.clear();
                        var isCancel = nts.uk.ui.windows.getShared('CDL002Cancel');
                        if (isCancel) {
                             __viewContext.vm.clickCounter.clickLinkGrid = false;
                            return;
                        }
                        var output = nts.uk.ui.windows.getShared('CDL002Output');
                        if (output != "") {
                            let param8 = {
                                typeDialog: 8,
                            }
                            service.findAllCodeName(param8).done((data: any) => {
                                nts.uk.ui.block.clear();
                                codeName = _.find(data, (item: any) => {
                                    return item.code == output;
                                });
                                self.updateCodeName(self.rowId(), self.attendenceId, codeName.name, codeName.code, self.selectedCode());
                                dfd8.resolve();
                            });
                        } else {
                            self.updateCodeName(self.rowId(), self.attendenceId, getText("KDW003_82"), "", self.selectedCode());
                            dfd8.resolve();
                        }
                    })
                    dfd8.promise();
                    break;
            }
            nts.uk.ui.block.clear();
        }

        updateCodeName(rowId: any, itemId: any, name: any, code: any, valueOld: any) {
            let dfd = $.Deferred();
            __viewContext.vm.clickCounter.clickLinkGrid = false;
            if (code == valueOld) {
                dfd.resolve();
            } else {
                nts.uk.ui.block.invisible();
                nts.uk.ui.block.grayout();
                _.remove(__viewContext.vm.workTypeNotFound, dataTemp => {
                    return dataTemp.columnKey == "Code" + itemId && dataTemp.rowId == rowId;
                });
                __viewContext.vm.inputProcess(rowId, "Code" + itemId, code).done(value => {
                    _.each(value.cellEdits, itemResult => {
                        $("#dpGrid").mGrid("updateCell", itemResult.id, itemResult.item, itemResult.value, true);
                    })
                    nts.uk.ui.block.clear();
                    dfd.resolve();
                });
                $("#dpGrid").mGrid("updateCell", rowId, "Name" + itemId, name)
                $("#dpGrid").mGrid("updateCell", rowId, "Code" + itemId, code);
            }
            return dfd.promise();
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
        valueType: any;
        layoutCode: string;
        employeeId: string;
        date: any;
        typeGroup: number;
        columnKey: string;
        constructor(rowId: any, itemId: any, value: any, valueType: any, layoutCode: string, employeeId: string, date: any, typeGroup: number, columnKey?: string) {
            this.rowId = rowId;
            this.itemId = itemId;
            this.value = value;
            this.valueType = valueType;
            this.layoutCode = layoutCode;
            this.employeeId = employeeId;
            this.date = date;
            this.typeGroup = typeGroup;
            this.columnKey = columnKey;
        }
    }

    class DataHoliday {
        dispCompensationDay: boolean;
        dispCompensationTime: boolean;
        dispSubstitute: boolean;
        dispAnnualDay: boolean;
        dispAnnualTime: boolean;
        dispReserve: boolean;
        compensationDay: string;
        compensationTime: string;
        substitute: string;
        annualDay: string;
        annualTime: string;
        reserve: string;

        dispSysDate: string = getText("KDW003_121", [moment(new Date()).format("YYYY/MM/DD")]);
        dispNextGrantDate: string;

        constructor(annualLeave: any, reserveLeave: any, compensatoryLeave: any, substitutionLeave: any, nextGrantDate: any) {
            this.dispNextGrantDate = nextGrantDate != null ? getText("KDW003_122", [nextGrantDate]) : getText("KDW003_123");
            this.dispCompensationDay = compensatoryLeave == null ? false : compensatoryLeave.manageCompenLeave;
            this.dispCompensationTime = compensatoryLeave == null ? false : compensatoryLeave.manageTimeOff;
            this.dispSubstitute = substitutionLeave == null ? false : substitutionLeave.manageAtr;
            this.dispAnnualDay = annualLeave == null ? false : annualLeave.manageYearOff;
            this.dispAnnualTime = annualLeave == null ? false : annualLeave.manageTimeOff;
            this.dispReserve = reserveLeave == null ? false : reserveLeave.manageRemainNumber;
            if (this.dispCompensationDay && compensatoryLeave.compenLeaveRemain != null) {
                this.compensationDay = getText("KDW003_8", [compensatoryLeave.compenLeaveRemain]);
                if (compensatoryLeave.compenLeaveRemain < 0)
                    $("#fixed-table td.remain-compen-day").css("color", "#ff0000");
                else
                    $("#fixed-table td.remain-compen-day").css("color", "");
            } else
                this.compensationDay = "";
            if (this.dispCompensationTime && compensatoryLeave.timeRemain != null) {
                this.compensationTime = nts.uk.time.format.byId("Time_Short_HM", compensatoryLeave.timeRemain);
                if (compensatoryLeave.timeRemain < 0)
                    $("#fixed-table td.remain-compen-time").css("color", "#ff0000");
                else
                    $("#fixed-table td.remain-compen-time").css("color", "");
            } else
                this.compensationTime = "";
            if (this.dispSubstitute && substitutionLeave.holidayRemain != null) {
                this.substitute = getText("KDW003_8", [substitutionLeave.holidayRemain]);
                if (substitutionLeave.holidayRemain < 0)
                    $("#fixed-table td.remain-subst-day").css("color", "#ff0000");
                else
                    $("#fixed-table td.remain-subst-day").css("color", "");
            } else
                this.substitute = "";
            if (this.dispAnnualDay && annualLeave.annualLeaveRemain != null) {
                this.annualDay = getText("KDW003_8", [annualLeave.annualLeaveRemain]);
                if (annualLeave.annualLeaveRemain < 0)
                    $("#fixed-table td.remain-annual-day").css("color", "#ff0000");
                else
                    $("#fixed-table td.remain-annual-day").css("color", "");
            } else
                this.annualDay = "";
            if (this.dispAnnualTime && annualLeave.timeRemain != null) {
                this.annualTime = nts.uk.time.format.byId("Time_Short_HM", annualLeave.timeRemain);
                if (annualLeave.timeRemain < 0)
                    $("#fixed-table td.remain-annual-time").css("color", "#ff0000");
                else
                    $("#fixed-table td.remain-annual-time").css("color", "");
            } else
                this.annualTime = "";
            if (this.dispReserve && reserveLeave.remainNumber != null) {
                this.reserve = getText("KDW003_8", [reserveLeave.remainNumber]);
                if (reserveLeave.remainNumber < 0)
                    $("#fixed-table td.remain-reserve-day").css("color", "#ff0000");
                else
                    $("#fixed-table td.remain-reserve-day").css("color", "");
            } else
                this.reserve = "";
        }
    }

    class ShareObject {
        changePeriodAtr: boolean; //期間を変更する có cho thay đổi khoảng thời gian hay không
        errorRefStartAtr: boolean; //エラー参照を起動する có hiện mode lỗi hay ko
        initClock: any; //打刻初期値-社員ID Optional giờ check tay SPR
        lstEmployeeShare: any; //社員一覧 社員ID danh sách nhân viên được chọn
        screenMode: any; //画面モード-日別実績の修正の画面モード  mode approval hay ko 
        targetClosure: any; //処理締め-締めID targetClosure lấy closureId 
        transitionDesScreen: any; //遷移先の画面 - Optional //truyền từ màn hình nào sang

        dateTarget: any; //日付別で起動- Optional ngày extract mode 2
        displayFormat: any; //表示形式 mode hiển thị 
        individualTarget: any; //個人別で起動 ngày bắt đầu
        lstExtractedEmployee: any;//抽出した社員一覧
        startDate: any;//期間 khoảng thời gian
        endDate: any;//期間 khoảng thời gian
        constructor() {
        }
        mapDataShare(dataInit: any, dataExtract: any, dataSPR: any) {
            var self = this;
            if (dataInit != undefined) {
                this.changePeriodAtr = dataInit.changePeriodAtr;
                this.errorRefStartAtr = dataInit.errorRefStartAtr;
                this.initClock = dataInit.initClock == undefined ? null : new SPRTime(dataInit.initClock);
                this.lstEmployeeShare = dataInit.lstEmployee;
                this.screenMode = dataInit.screenMode;
                this.targetClosure = dataInit.targetClosure;
                this.transitionDesScreen = dataInit.transitionDesScreen;
                if (this.screenMode == ScreenMode.APPROVAL) {
                    $("#ccg001").hide();
                }
            }
            if (dataExtract != undefined) {
                this.dateTarget = moment(dataExtract.dateTarget, "YYYY/MM/DD");
                this.displayFormat = dataExtract.displayFormat;
                this.individualTarget = dataExtract.individualTarget;
                this.lstExtractedEmployee = null//dataExtract.lstExtractedEmployee;
                this.startDate = moment(dataExtract.startDate, "YYYY/MM/DD");
                this.endDate = moment(dataExtract.endDate, "YYYY/MM/DD");
            }

            if (dataSPR != undefined) {
                this.changePeriodAtr = true;
                this.errorRefStartAtr = true;
                this.initClock = new SPRTime({ dateSpr: dataSPR.dateTarget, canEdit: true, employeeId: dataSPR.employeeId, liveTime: dataSPR.liveTime, goOut: dataSPR.goOut });
                this.lstEmployeeShare = [];
                this.screenMode = dataSPR.screenMode;
                this.targetClosure = null;
                this.transitionDesScreen = null;
                this.dateTarget = moment(dataSPR.dateTarget, "YYYY/MM/DD");
                this.displayFormat = dataExtract.displayFormat;
                this.individualTarget = null;
                this.lstExtractedEmployee = [];
                this.startDate = moment(dataSPR.dateTarget, "YYYY/MM/DD");
                this.endDate = moment(dataSPR.dateTarget, "YYYY/MM/DD");
            }
        }
    }

    class SPRTime {
        dateSpr: any
        canEdit: any;
        employeeId: any;
        //退勤打刻
        liveTime: any;
        //出勤打刻
        goOut: any;
        constructor(data: any) {
            this.dateSpr = moment(data.dateSpr, "YYYY/MM/DD");
            this.canEdit = data.canEdit;
            this.employeeId = data.employeeId;
            this.liveTime = data.liveTime == undefined ? "" : data.liveTime;
            this.goOut = data.goOut == undefined ? "" : data.goOut;
        }
    }

    class FlexShortage {
        shortageTime: KnockoutObservable<any> = ko.observable();
        nextMonthTransferredMoneyTime: KnockoutObservable<string> = ko.observable("");

        noOfHolidays: KnockoutObservable<any> = ko.observable("");
        nameNoOfHolidays: any;
        noOfHolidaysOld: KnockoutObservable<any> = ko.observable("");

        absentDeductionTime: KnockoutObservable<any> = ko.observable();
        nameAbsentDeductionTime: any;
        absentDeductionTimeOld: KnockoutObservable<any> = ko.observable();

        initLoad = 0;
        messageRed: KnockoutObservable<any> = ko.observable();
        messageRedValue: KnockoutObservable<any> = ko.observable();
        messageNoForward: KnockoutObservable<any> = ko.observable();

        constructor(parent: any, dataCalc: CalcFlex, breakTimeDay: BreakTimeDay) {
            let self = this;
            this.nameNoOfHolidays = getText('Com_PaidHoliday');
            this.nameAbsentDeductionTime = getText('KDW003_79');
            self.bindDataChange(dataCalc, "", "", [], false);
            self.noOfHolidays.subscribe(val => {
                if (self.initLoad > 0) {
                    //if (Number(val) != Number(self.noOfHolidaysOld())) {
                        self.calc();
                    //}
                }
            });

            self.absentDeductionTime.subscribe(val => {
                if (self.initLoad > 0) {
                    //if (Number(val) != Number(self.absentDeductionTimeOld())) {
                        self.calc();
                    //}
                }
            });
        };

        bindDataChange(dataCalc: CalcFlex, redConditionMessage: any, messageNotForward: any, error: boolean, lstError: any, showLstError: boolean) {
            if (!dataCalc) {
                return;
            }

            let self = this,
                val18 = dataCalc.value18,
                val19 = dataCalc.value19,
                val21 = dataCalc.value21,
                val189 = dataCalc.value189,
                val190 = dataCalc.value190,
                val191 = dataCalc.value191;
            //set old value 
            self.noOfHolidaysOld(val189);
            self.absentDeductionTimeOld(val190);

            self.messageRed(getText('KDW003_80', [redConditionMessage]));
            self.messageRedValue(redConditionMessage);
            self.messageNoForward(messageNotForward);

            self.shortageTime(getText("KDW003_89", [self.convertToHours((Number(val191) + Number(val19))), self.convertToHours(Number(val19))]));
            self.nextMonthTransferredMoneyTime(getText("KDW003_111", [self.convertToHours((Number(val18) + Number(val21)))]));
            self.noOfHolidays(Number(val189));
            self.absentDeductionTime(Number(val190));
            if (error && __viewContext.vm.canFlex()) {
                $("#next-month").attr('style', 'background-color: red !important');
            } else {
                $("#next-month").attr('style', 'background-color: white !important');
            }
//            if (showLstError) {
//                self.displayListError(lstError);
//            }
            self.initLoad = 1;
        }

        calc(): JQueryPromise<any> {
            let dfd = $.Deferred();
            let self = this;
            $("#next-month").ntsError("clear");
            if ((__viewContext.vm.canFlex())) {
                __viewContext.vm.valueUpdateMonth = {};
                if (__viewContext.vm.itemMonth.length > 0) {
                    let dataFlexUpdate = __viewContext.vm.itemValueMonthParent,
                        items = _.map(__viewContext.vm.itemMonth, value => {
                            if (value.itemId == 189) {
                                value.value = self.noOfHolidays();
                            }
                            if (value.itemId == 190) {
                                value.value = self.absentDeductionTime();
                            }
                            return value;
                        });

                    if (dataFlexUpdate) {
                        dataFlexUpdate["items"] = items;
                        dataFlexUpdate["redConditionMessage"] = self.messageRedValue();
                        __viewContext.vm.valueUpdateMonth = dataFlexUpdate;
                    }
                } else {
                    __viewContext.vm.valueUpdateMonth["redConditionMessage"] = self.messageRedValue();
                }
                dfd.resolve();
            } else {
                dfd.resolve();
            }
            return dfd.promise();
        }

        natural(value: any): number {
            let temp = Number(value);
            return temp < 0 ? 0 - Math.floor(Math.abs(temp)) : Math.floor(temp);
        }

        convertToHours(value: any): string {
            let self = this;
            let hours = value < 0 ? String(0 - Math.floor(Math.abs(value / 60))) : String(Math.floor(value / 60));
            let minutes = String(Math.abs(value) % 60);
            if (Number(minutes) < 10) minutes = "0" + minutes;
            return hours + ":" + minutes;
        }

        displayListError(lstError: any) {
            if (lstError.length == 0) {
                $("#next-month").ntsError("clear");
            } else {
                $("#next-month").ntsError("clear");
                _.forEach(lstError, value => {
                    $("#next-month").ntsError("set", value.message, value.messageId);
                });
            }
        }

        checkColorDetail(timeCheck: any, value: any, breakTimeDay: BreakTimeDay) {
            let self = this;
            let check1174 = false,
                check1175 = false;
            if (check1174 || check1175) {
                $("#next-month").attr('style', 'background-color: red !important');
            } else {
                $("#next-month").attr('style', 'background-color: white !important');
            }
            if (check1174) {
                $("#next-month").ntsError("set", nts.uk.resource.getMessage("Msg_1174", [timeCheck]), "Msg_1174");
            } else {
                if ($("#next-month").ntsError("hasError", "Msg_1174")) $("#next-month").ntsError("clearByCode", "Msg_1174");
            }
            if (check1175) {
                $("#next-month").ntsError("set", nts.uk.resource.getMessage("Msg_1175"), "Msg_1175");
            } else {
                if ($("#next-month").ntsError("hasError", "Msg_1175")) $("#next-month").ntsError("clearByCode", "Msg_1175");
            }
        }
    }

    class CalcFlex {
        value18: any;
        value19: any;
        value21: any;
        value189: any;
        value190: any;
        value191: any;
        constructor(value18: any, value19: any, value21: any, value189: any, value190: any, value191: any) {
            this.value18 = Number(value18.value);
            this.value19 = Number(value19.value);
            this.value21 = Number(value21.value);
            this.value189 = Number(value189.value);
            this.value190 = Number(value190.value);
            this.value191 = Number(value191.value);
        }
    }

    interface BreakTimeDay {
        day: any;
        am: any;
        pm: any;
    }

    export enum ScreenMode {
        //通常
        NORMAL = 0,
        //承認
        APPROVAL = 1
    }

    export enum SPRCheck {
        NOT_INSERT = 0,
        INSERT = 1,
        SHOW_CONFIRM = 2

    }

    class AgreementInfomation {
        agreementTime: KnockoutObservable<string> = ko.observable("");
        cssAgree: any;

        agreementExcess: KnockoutObservable<string> = ko.observable("");
        cssFrequency: any;

        showAgreement: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            // this.agreementTime = "";
            this.cssAgree = "";

            // this.agreementExcess = "";
            this.cssFrequency = "";

        }

        mapDataAgreement(data: any): void {
            this.showAgreement(data.showAgreement);
            if (!data.showAgreement) return;
            this.agreementTime(getText("KDW003_74", [data.agreementTime36, data.maxTime]));
            this.cssAgree = data.cssAgree;
            this.agreementExcess(getText("KDW003_76", [data.excessFrequency, data.maxNumber]));
            this.cssFrequency = data.cssFrequency;

            this.processState(data.cssAgree, data.cssFrequency);
        }

        processState(cssAgree: any, cssFrequency: any) {
            if (cssAgree != "" && cssAgree != null) {
                $('#agree-time').removeClass();
                $("#agree-time").addClass(cssAgree);
            } else {
                $('#agree-time').removeClass();
            }

            if (cssFrequency != "" && cssFrequency != null) {
                $("#agree-excess").removeClass();
                $("#agree-excess").addClass(cssFrequency);
            } else {
                $("#agree-excess").removeClass();
            }
        }
    }

    interface Characteristics {

        companyId: string;
        employeeId: string;

        showZero: boolean;
        showProfile: boolean;
        showNumberHeader: boolean;

        formatExtract: number;

        authenSelectFormat: any;
        
        moveMouse: number;
    }
    
    class CLickCount{
        clickLinkGrid: boolean;
        clickErrorRefer: boolean;
        constructor() {
            this.clickLinkGrid = false;
            this.clickErrorRefer = false;
        } 
    } 
}