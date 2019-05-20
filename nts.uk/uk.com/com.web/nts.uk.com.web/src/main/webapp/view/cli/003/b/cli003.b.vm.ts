module nts.uk.com.view.cli003.b.viewmodel {
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import errors = nts.uk.ui.errors;
    //C screen
    import Ccg001ReturnedData = nts.uk.com.view.ccg.share.ccg.service.model.Ccg001ReturnedData;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;
    import ComponentOption = kcp.share.list.ComponentOption;
    import ListType = kcp.share.list.ListType;
    import SelectType = kcp.share.list.SelectType;
    import UnitModel = kcp.share.list.UnitModel;
    import UnitAlreadySettingModel = kcp.share.list.UnitAlreadySettingModel;


    export class ScreenModel {
        //wizard
        stepList: Array<NtsWizardStep> = [];
        stepSelected: KnockoutObservable<NtsWizardStep>;
        activeStep: KnockoutObservable<number>;


        //B
        itemList: KnockoutObservableArray<ItemModel>;
        dataTypeList: KnockoutObservableArray<ItemModel>;
        itemName: KnockoutObservable<string>;
        currentCode: KnockoutObservable<number>
        logTypeSelectedCode: KnockoutObservable<string>;
        dataTypeSelectedCode: KnockoutObservable<number>;
        selectedCodes: KnockoutObservableArray<string>;
        isEnable: KnockoutObservable<boolean>;
        checkFormatDate: KnockoutObservable<string>;

        //C
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;
        employeeList: KnockoutObservableArray<UnitModel>;
        initEmployeeList: KnockoutObservableArray<UnitModel>;
        enable: KnockoutObservable<boolean>;
        enableTagetDate: KnockoutObservable<boolean>;
        required: KnockoutObservable<boolean>;
        dateValue: KnockoutObservable<any>;
        startDateString: KnockoutObservable<string>;
        endDateString: KnockoutObservable<string>;
        dateCtoE: KnockoutObservable<string>;
        targetEmployeeIdList: KnockoutObservableArray<any>;

        //D
        roundingRulesOperator: KnockoutObservableArray<any>;
        startDateOperator: KnockoutObservable<string>;
        endDateOperator: KnockoutObservable<string>;
        selectedRuleCodeOperator: any;
        employeeListOperator: KnockoutObservableArray<UnitModel>;
        initEmployeeListOperator: KnockoutObservableArray<UnitModel>;
        selectedTitleAtrOperator: KnockoutObservable<number>;
        selectedEmployeeCodeOperator: KnockoutObservableArray<string>;
        listEmployeeIdOperator: KnockoutObservableArray<any>;
        startDateNameOperator: KnockoutObservable<string>;
        endDateNameOperator: KnockoutObservable<string>;

        //E
        dateOperator: KnockoutObservable<string>;
        isDisplayTarget: KnockoutObservable<boolean>;
        displayTargetDate: KnockoutObservable<boolean>;
        logTypeSelectedName: KnockoutObservable<string>;
        tarGetDataTypeSelectedName: KnockoutObservable<string>;
        targetNumber: KnockoutObservable<string>;
        operatorNumber: KnockoutObservable<string>;

        //F

        columnsIgGrid: KnockoutObservableArray<IgGridColumnSwitchModel>;
        supColumnsIgGrid: KnockoutObservableArray<IgGridColumnSwitchModel>;
        columnsHeaderLogRecord: KnockoutObservableArray<String> = ko.observableArray(['2', '3', '7', '19', '20', '22']);
        columnsHeaderLogStartUp: KnockoutObservableArray<String> = ko.observableArray(['2', '3', '7', '18', '19']);
        columnsHeaderLogPersionInfo: KnockoutObservableArray<String> = ko.observableArray(['2', '3', '7', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '31', '33', '36']);
        columnsHeaderLogDataCorrect: KnockoutObservableArray<String> = ko.observableArray(['2', '3', '7', '20', '21', '22', '23', '24', '26', '27', '30', '31']);
        listLogBasicInforModel: LogBasicInfoModel[];
        logBasicInforCsv: LogBasicInfoModel[];
        isDisplayText: KnockoutObservable<boolean> = ko.observable(false);
        maxlength: KnockoutObservable<number> = ko.observable(1000);

        // I
        itemOutPutSelect: KnockoutObservable<string>;
        listItemNo: KnockoutObservableArray<String>;
        listLogBasicInforAllModel: LogBasicInforAllModel[];
        columnsIgAllGrid: KnockoutObservableArray<IgGridColumnAllModel>;
        listLogSetItemDetailDto: KnockoutObservableArray<LogSetItemDetailDto>;
        listLogDataExport: KnockoutObservableArray<any>;
        listHeaderSort: KnockoutObservableArray<any>;
        constructor() {
            var self = this;
            $("#ccgcomponent").hide();
            self.stepList = [
                { content: '.step-1' },
                { content: '.step-2' },
                { content: '.step-3' },
                { content: '.step-4' },
                { content: '.step-5' }
            ];
            // B

            //C   
            self.initComponentC();
            self.initComponnentKCP005();
            self.initComponentCCG001();


            //D 
            self.initComponentD();
            self.initComponnentKCP005Operator();
            //E
            self.initComponentE();

            self.itemList = ko.observableArray([
                new ItemModel(RECORD_TYPE.LOGIN, 'ログイン'),
                new ItemModel(RECORD_TYPE.START_UP, '起動'),
                //    new ItemModel(RECORD_TYPE.UPDATE_MASTER, 'マスタ修正'),
                new ItemModel(RECORD_TYPE.UPDATE_PERSION_INFO, '個人情報修正'),
                //    new ItemModel(RECORD_TYPE.DATA_REFERENCE, 'データ参照'),
                //      new ItemModel(RECORD_TYPE.DATA_MANIPULATION, 'データ操作'),
                new ItemModel(RECORD_TYPE.DATA_CORRECT, 'データ修正')
                //      new ItemModel(RECORD_TYPE.MY_NUMBER, 'マイナンバー'),
                //      new ItemModel(RECORD_TYPE.TERMINAL_COMMUNICATION_INFO, '情報端末通信')

            ]);
            self.dataTypeList = ko.observableArray([
                new ItemModel(0, getText('Enum_DataType_Schedule')),
                new ItemModel(1, getText('Enum_DataType_DailyResults')),
                new ItemModel(2, getText('Enum_DataType_MonthlyResults'))
                //                new ItemTypeModel(3, getText('Enum_DataType_AnyPeriodSummary')),
                //                new ItemTypeModel(4, getText('Enum_DataType_ApplicationApproval')),
                //                new ItemTypeModel(5, getText('Enum_DataType_Notification')),
                //                new ItemTypeModel(6, getText('Enum_DataType_SalaryDetail')),
                //                new ItemTypeModel(7, getText('Enum_DataType_BonusDetail')),
                //                new ItemTypeModel(8, getText('Enum_DataType_YearEndAdjustment')),
                //                new ItemTypeModel(9, getText('Enum_DataType_MonthlyCalculation')),
                //                new ItemTypeModel(10, getText('Enum_DataType_RisingSalaryBack'))
            ]);
            self.itemName = ko.observable('');
            self.currentCode = ko.observable(3);
            self.logTypeSelectedCode = ko.observable(RECORD_TYPE.LOGIN);
            self.dataTypeSelectedCode = ko.observable(0);
            self.isEnable = ko.observable(true);
            // end screen B
            self.activeStep = ko.observable(0);
            self.displayStep2 = ko.observable(false);
            self.stepSelected = ko.observable({ id: 'step-1', content: '.step-1' });
            self.checkFormatDate = ko.observable('1');
        }

        //C list selectedEmployeeCodeTarget
        initComponentC() {
            var self = this;

            self.initEmployeeList = ko.observableArray([]);
            self.employeeDeletionList = ko.observableArray([]);
            self.categoryDeletionList = ko.observableArray([]);
            self.selectedEmployeeCodeTarget = ko.observableArray([]);
            // update id sau check  
            self.targetEmployeeIdList = ko.observableArray([]);

            self.alreadySettingPersonal = ko.observableArray([]);

            self.employeeList = ko.observableArray([]);
            //Date
            self.enable = ko.observable(true);
            self.enableTagetDate = ko.observable(true);
            self.required = ko.observable(true);

            self.startDateString = ko.observable("");
            self.endDateString = ko.observable("");
            self.dateValue = ko.observable({});

            self.startDateString.subscribe(function(value) {
                self.dateValue().startDate = value;
                self.dateValue.valueHasMutated();
            });

            self.endDateString.subscribe(function(value) {
                self.dateValue().endDate = value;
                self.dateValue.valueHasMutated();
            });

            self.dateValue = ko.observable({
                startDate: moment.utc().format("YYYY/MM/DD"),
                endDate: moment.utc().format("YYYY/MM/DD")
            });



            self.roundingRules = ko.observableArray([
                { code: EMPLOYEE_SPECIFIC.SPECIFY, name: getText('CLI003_17') },
                { code: EMPLOYEE_SPECIFIC.ALL, name: getText('CLI003_18') }
            ]);
            self.selectedRuleCode = ko.observable(1);
            self.selectedTitleAtr = ko.observable(0);
            self.selectedTitleAtr.subscribe(function(value) {
                if (value == 1) {
                    self.applyKCP005ContentSearch(self.initEmployeeList());
                }
                else {
                    self.applyKCP005ContentSearch([]);
                }
            });


        }
        initComponentCCG001() {
            let self = this;
            // Set component option 
            self.ccg001ComponentOption = {
                showEmployeeSelection: false,
                systemType: 5,
                showQuickSearchTab: false,
                showAdvancedSearchTab: true,
                showBaseDate: true,
                showClosure: false,
                showAllClosure: false,
                showPeriod: false,
                periodFormatYM: false,


                /** Quick search tab options */
                showAllReferableEmployee: false,
                showOnlyMe: false,
                showSameWorkplace: false,
                showSameWorkplaceAndChild: false,


                /** Advanced search properties */
                showEmployment: true,
                showWorkplace: true,
                showClassification: true,
                showJobTitle: true,
                showWorktype: false,
                isMutipleCheck: true,



                /** Required parameter */
                baseDate: moment().toISOString(),
                periodStartDate: moment().toISOString(),
                periodEndDate: moment().toISOString(),
                inService: true,
                leaveOfAbsence: true,
                closed: true,
                retirement: true,
                /**
                * Self-defined function: Return data from CCG001
                * @param: data: the data return from CCG001
                */
                returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                    self.selectedTitleAtr(1);                 
                     data.listEmployee=_.orderBy(data.listEmployee,['employeeCode'], ['asc', 'asc']);
                    self.employeeList();
                    if (self.activeStep() == 1) {
                        self.initEmployeeList(data.listEmployee);
                        self.applyKCP005ContentSearch(data.listEmployee);
                    }
                    if (self.activeStep() == 2) {
                        self.initEmployeeListOperator(data.listEmployee);
                        self.applyKCP005ContentSearchOperator(data.listEmployee);
                    }

                }
            }
        }

        applyKCP005ContentSearch(dataEmployee: EmployeeSearchDto[]) {
            var self = this;
            var employeeSearchs: UnitModel[] = [];
            _.forEach(dataEmployee, function(item: EmployeeSearchDto) {
                employeeSearchs.push(new UnitModel(item.employeeId, item.employeeCode,
                    item.employeeName, item.workplaceName));
            });
            self.employeeList(employeeSearchs);
        }

        initComponnentKCP005() {
            //KCP005
            var self = this;
            self.listComponentOption = {
                isShowAlreadySet: false,
                isMultiSelect: true,
                listType: ListType.EMPLOYEE,
                employeeInputList: self.employeeList,
                selectType: SelectType.SELECT_ALL,
                selectedCode: self.selectedEmployeeCodeTarget,
                isDialog: true,
                isShowNoSelectRow: false,
                alreadySettingList: self.alreadySettingPersonal,
                isShowWorkPlaceName: true,
                isShowSelectAllButton: true,
                maxWidth: 550,
                maxRows: 8
            };
        }
        //start page data 
        public startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.switchCodeChange();
            dfd.resolve(self);
            return dfd.promise();
        }

        //update the list of selected employees C targetEmployeeIdList

        setEmployeeListTarget() {
            var self = this;
            self.targetEmployeeIdList.removeAll();
            if (self.selectedRuleCode() == EMPLOYEE_SPECIFIC.SPECIFY) {
                let empCodeLength = self.selectedEmployeeCodeTarget().length;
                let empListLength = self.employeeList().length;
                for (var i = 0; i < empCodeLength; i++) {
                    for (var j = 0; j < empListLength; j++) {
                        let employee = self.employeeList()[j];
                        if (employee.code == self.selectedEmployeeCodeTarget()[i]) {
                            self.targetEmployeeIdList.push(employee.id);
                        }
                    }
                }
            }

        }

        /**
         *Check validate client
         */
        private validateForm() {
            $(".validate_form").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return false;
            }
            return true;
        };

        //D component
        initComponentD() {
            var self = this;
            self.readonly = ko.observable(false);
            self.employeeDeletionListOperator = ko.observableArray([]);
            self.initEmployeeListOperator = ko.observableArray([]);
            self.selectedEmployeeCodeOperator = ko.observableArray([]);
            self.alreadySettingPersonalOperator = ko.observableArray([]);
            self.employeeListOperator = ko.observableArray([]);
            self.listEmployeeIdOperator = ko.observableArray([]);
            self.startDateOperator = ko.observable(moment.utc().format("YYYY/MM/DD 0:00:00"));
            self.endDateOperator = ko.observable(moment.utc().format("YYYY/MM/DD 23:59:59"));
            self.startDateNameOperator = ko.observable(getText('CLI003_52'));
            self.endDateNameOperator = ko.observable(getText('CLI003_53'));
            self.roundingRulesOperator = ko.observableArray([
                { code: EMPLOYEE_SPECIFIC.SPECIFY, name: getText('CLI003_17') },
                { code: EMPLOYEE_SPECIFIC.ALL, name: getText('CLI003_18') }
            ]);
            self.selectedRuleCodeOperator = ko.observable(1);
            self.selectedTitleAtrOperator = ko.observable(0);
            self.selectedTitleAtrOperator.subscribe(function(value) {
                if (value == 1) {
                    self.applyKCP005ContentSearchOperator(self.initEmployeeListOperator());
                }
                else {
                    self.applyKCP005ContentSearchOperator([]);
                }
            });
        }


        initComponentE() {
            var self = this;
            self.dateOperator = ko.observable("");
            self.logTypeSelectedName = ko.observable("");
            self.tarGetDataTypeSelectedName = ko.observable("");
            self.isDisplayTarget = ko.observable(false);
            self.displayTargetDate = ko.observable(true);
            self.targetNumber = ko.observable("");
            self.operatorNumber = ko.observable("");
        }
        applyKCP005ContentSearchOperator(dataEmployee: EmployeeSearchDto[]) {
            var self = this;
            var employeeSearchs: UnitModel[] = [];
            _.forEach(dataEmployee, function(item: EmployeeSearchDto) {
                employeeSearchs.push(new UnitModel(item.employeeId, item.employeeCode,
                    item.employeeName, item.workplaceName));
            });
            self.employeeListOperator(employeeSearchs);
        }

        initComponnentKCP005Operator() {
            //KCP005
            var self = this;
            self.listComponentOptionOperator = {
                isShowAlreadySet: false,
                isMultiSelect: true,
                listType: ListType.EMPLOYEE,
                employeeInputList: self.employeeListOperator,
                selectType: SelectType.SELECT_ALL,
                selectedCode: self.selectedEmployeeCodeOperator,
                isDialog: true,
                isShowNoSelectRow: false,
                alreadySettingList: self.alreadySettingPersonalOperator,
                isShowWorkPlaceName: true,
                isShowSelectAllButton: true,
                maxWidth: 550,
                maxRows: 8
            };
        }

        //D array ID Operator
        getListEmployeeIdOperator() {
            var self = this;
            self.listEmployeeIdOperator.removeAll();
            if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.SPECIFY) {
                let empCodeLength = self.selectedEmployeeCodeOperator().length;
                let empListLength = self.employeeListOperator().length;
                for (var i = 0; i < empCodeLength; i++) {
                    for (var j = 0; j < empListLength; j++) {
                        let employee = self.employeeListOperator()[j];
                        if (employee.code == self.selectedEmployeeCodeOperator()[i]) {
                            self.listEmployeeIdOperator.push(employee.id);
                        }
                    }
                }
            }
        }

        //E 
        getlogTypeName() {
            var self = this;
            for (var i = 0; i < self.itemList().length; i++) {
                let temp = self.itemList()[i];
                if (temp.code == self.logTypeSelectedCode()) {
                    self.logTypeSelectedName(temp.name);
                }
            }
        }
        getTargetDataTypeName() {
            var self = this;
            for (var i = 0; i < self.dataTypeList().length; i++) {
                let temp = self.dataTypeList()[i];
                if (temp.code == self.dataTypeSelectedCode()) {
                    self.tarGetDataTypeSelectedName(temp.name);
                }
            }
        }

        getDateOperator() {
            var self = this;
            self.dateOperator(self.startDateOperator() + " ~ " + self.endDateOperator());
        }
        getOperatorNumber() {
            var self = this;
            if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.SPECIFY) {
                self.operatorNumber(nts.uk.text.format(nts.uk.resource.getText("CLI003_57"), self.listEmployeeIdOperator().length));
            }
            if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.ALL) {
                self.operatorNumber(getText('CLI003_18'));
            }
        }
        getTargetDate() {
            var self = this;
            self.dateCtoE = self.dateValue().startDate + " ~ " + self.dateValue().endDate;
        }
        getTargetNumber() {
            var self = this;
            if (self.selectedRuleCode() == EMPLOYEE_SPECIFIC.SPECIFY) {
                self.targetNumber(nts.uk.text.format(nts.uk.resource.getText("CLI003_57"), self.selectedEmployeeCodeTarget().length));
            }
            if (self.selectedRuleCode() == EMPLOYEE_SPECIFIC.ALL) {
                self.targetNumber(getText('CLI003_18'));
            }
        }

        //F
        startScreenF(): JQueryPromise<any> {
            let self = this;
            self.initComponentScreenF().done(function() {
            });
        }
        initComponentScreenF() {
            var self = this
            let dfd = $.Deferred<any>();
            //F igGrid
            self.columnsIgGrid = ko.observableArray([]);
            self.supColumnsIgGrid = ko.observableArray([]);
            self.listLogBasicInforModel = [];
            self.isDisplayText(false);
            let recordType = Number(self.logTypeSelectedCode());

            // set param log
            var format = 'YYYY/MM/DD HH:mm:ss';
            let paramLog = {
                listTagetEmployeeId: self.targetEmployeeIdList(),
                listOperatorEmployeeId: self.listEmployeeIdOperator(),
                startDateTaget: moment(self.dateValue().startDate, "YYYY/MM/DD").toISOString(),
                //   endDateTaget: moment(self.dateValue().endDate, "YYYY/MM/DD").toISOString(),
                startDateOperator: moment.utc(self.startDateOperator(), format).toISOString(),
                endDateOperator: moment.utc(self.endDateOperator(), format).toISOString(),
                recordType: self.logTypeSelectedCode(),
                targetDataType: self.dataTypeSelectedCode()
            };
            if (self.checkFormatDate() === '2') {
                paramLog.endDateTaget = moment(self.dateValue().endDate, "YYYY/MM/DD" ).endOf('month').toDate();
            } else {
                paramLog.endDateTaget = moment.utc(self.dateValue().endDate, "YYYY/MM/DD").toISOString();
            }
            // set param for get parent header name
            let paramOutputItem = {
                recordType: self.logTypeSelectedCode()
            };
            let checkProcess = false;
            switch (recordType) {
                case RECORD_TYPE.LOGIN: {
                    paramOutputItem.itemNos = self.columnsHeaderLogRecord();
                    checkProcess = true;
                    break
                }
                case RECORD_TYPE.START_UP: {
                    paramOutputItem.itemNos = self.columnsHeaderLogStartUp();
                    checkProcess = true;
                    break;
                }
                case RECORD_TYPE.UPDATE_PERSION_INFO: {
                    paramOutputItem.itemNos = self.columnsHeaderLogPersionInfo();
                    checkProcess = true;
                    break
                }
                case RECORD_TYPE.DATA_CORRECT: {
                    paramOutputItem.itemNos = self.columnsHeaderLogDataCorrect();
                    checkProcess = true;
                    break;
                }
                default: {
                    break;
                }
            }
            if (checkProcess) {
                // get log out put items
                block.grayout();
                service.getLogOutputItemsByRecordTypeItemNos(paramOutputItem).done(function(dataOutputItems: Array<any>) {
                    if (dataOutputItems.length > 0) {
                        // Get Log basic infor
                        service.getLogBasicInfoByModifyDate(paramLog).done(function(data: Array<LogBasicInfoModel>) {

                            if (data.length > 0) {
                                // order by list
                                if (recordType == RECORD_TYPE.LOGIN || recordType == RECORD_TYPE.START_UP) {
                                    data = _.orderBy(data, ['modifyDateTime', 'employeeCodeLogin'], ['desc', 'asc']);
                                }
                                if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO || recordType == RECORD_TYPE.DATA_CORRECT) {
                                    data = _.orderBy(data, ['modifyDateTime', 'employeeCodeTaget'], ['desc', 'asc']);
                                }
                                // generate columns header parent
                                self.setListColumnHeaderLog(recordType, dataOutputItems);
                                let countLog = 1;
                                if (data.length > self.maxlength()) {
                                    self.isDisplayText(true);
                                }
                                // process sub header with record type = persion infro and data correct
                                _.forEach(data, function(logBasicInfoModel) {
                                    if (countLog <= self.maxlength()) {
                                        let logtemp = "";
                                        if (recordType == RECORD_TYPE.LOGIN || recordType == RECORD_TYPE.START_UP) {
                                            self.listLogBasicInforModel.push(logBasicInfoModel);
                                        }
                                        if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                                            logtemp = self.getSubHeaderPersionInfo(logBasicInfoModel);
                                            self.listLogBasicInforModel.push(logtemp);
                                        }
                                        if (recordType == RECORD_TYPE.DATA_CORRECT) {
                                            logtemp = self.getSubHeaderDataCorect(logBasicInfoModel);
                                            self.listLogBasicInforModel.push(logBasicInfoModel);
                                        }
                                        countLog++;
                                    } else {
                                        return false;
                                    }
                                });
                                // Generate table
                                if (recordType == RECORD_TYPE.DATA_CORRECT) {
                                    self.generateDataCorrectLogGrid();
                                } else if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                                    self.generatePersionInforGrid();
                                } else {
                                    self.generateIgGrid();
                                }

                            } else {
                                alertError({ messageId: "Msg_1220" }).then(function() {
                                    self.previousScreenE();
                                    nts.uk.ui.block.clear();
                                });
                            }
                            dfd.resolve();
                        }).always(() => {
                            block.clear();
                            nts.uk.ui.errors.clearAll();
                        }).fail(function(error) {
                            alertError(error);
                            block.clear();
                            nts.uk.ui.errors.clearAll();
                            dfd.resolve();
                        });

                    } else {
                        alertError({ messageId: "Msg_1221" }).then(function() {
                            self.previousScreenE();
                            nts.uk.ui.block.clear();
                        });
                        block.clear();
                        nts.uk.ui.errors.clearAll();
                        dfd.resolve();
                    }

                }).fail(function(error) {
                    alertError(error);
                    block.clear();
                    nts.uk.ui.errors.clearAll();
                    dfd.resolve();
                });
            }
            return dfd.promise();
        }

        getSubHeaderDataCorect(logBasicInfoModel: LogBasicInfoModel) {
            let tempList = logBasicInfoModel.lstLogOutputItemDto;
            var subColumHeaderTemp: IgGridColumnModel[] = [];
            _.forEach(logBasicInfoModel.lstLogOutputItemDto, function(logOutputItemDto) {
                // generate columns header chidrent
                switch (logOutputItemDto.itemNo) {
                    case ITEM_NO.ITEM_NO22:
                    case ITEM_NO.ITEM_NO23:
                    case ITEM_NO.ITEM_NO24: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_TAGET_DATE, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO26: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_CORRECT_ATTR, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO27: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_NAME, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO30: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_VALUE_BEFOR, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO31: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_VALUE_AFTER, ITEM_PROPERTY.STR, false));
                        break;
                    }
                }
            });
            logBasicInfoModel.subColumnsHeaders = subColumHeaderTemp;
            return logBasicInfoModel;
        }
        getSubHeaderPersionInfo(logBasicInfoModel: LogBasicInfoModel) {
            let tempList = logBasicInfoModel.lstLogOutputItemDto;
            var subColumHeaderTemp: IgGridColumnModel[] = [];
            _.forEach(logBasicInfoModel.lstLogOutputItemDto, function(logOutputItemDto) {
                // generate columns header chidrent
                switch (logOutputItemDto.itemNo) {
                    case ITEM_NO.ITEM_NO23: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_CATEGORY_NAME, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO99: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_TAGET_DATE, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO24: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_INFO_OPERATE_ATTR, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO29: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_NAME, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO31: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_VALUE_BEFOR, ITEM_PROPERTY.STR, false));
                        break;
                    }
                    case ITEM_NO.ITEM_NO33: {
                        subColumHeaderTemp.push(new IgGridColumnModel(logOutputItemDto.itemName, ITEM_PROPERTY.ITEM_VALUE_AFTER, ITEM_PROPERTY.STR, false));
                        break;
                    }
                }
            });
            logBasicInfoModel.subColumnsHeaders = subColumHeaderTemp;
            return logBasicInfoModel;
        }

        generateIgGrid() {
            var self = this;
            $("#igGridLog").igGrid({
                width: "100%",
                height: "calc(100% - 5px)",
                features: [
                    {
                        name: "Tooltips"
                    },
                    {
                        name: "Paging",
                        type: "local",
                        pageSize: 100
                    },
                    {
                        name: "Sorting",
                        type: "local"
                    },
                    {
                        name: "Resizing",
                        deferredResizing: false,
                        allowDoubleClickToResize: true
                    },
                    {
                        name: "Filtering",
                        type: "local",
                        filterDropDownItemIcons: false,
                        filterDropDownWidth: 200,
                        filterDialogHeight : "390px",
                        filterDialogWidth : "515px",
                        columnSettings: [
                            { columnKey: "parentKey", allowFiltering: false },
                            { columnKey: "operationId", allowFiltering: false }
                        ]
                    }
                ],
                enableTooltip : true,
                rowVirtualization: true,
                virtualization: true,
                virtualizationMode: 'continuous',
                dataSource: self.listLogBasicInforModel,
                columns: self.columnsIgGrid()
            });
        }
        generatePersionInforGrid() {
            var self = this;
            let listLogBasicInfor = self.listLogBasicInforModel;
            //generate generateHierarchialGrid
            $("#igGridLog").igHierarchicalGrid({
                width: "100%",
                height: "calc(100% - 15px)",
                dataSource: listLogBasicInfor,
                features: [
                    {
                        name: "Tooltips",
                        inherit: true
                    },
                    {
                        name: "Responsive",
                        enableVerticalRendering: false
                    },
                    {
                        name: "Resizing",
                        deferredResizing: false,
                        allowDoubleClickToResize: true,
                        inherit: true
                    },
                    {
                        name: "Sorting",
                        inherit: false

                    },
                    {
                        name: "Paging",
                        pageSize: 100,
                        type: "local",
                        inherit: true
                    },
                    {
                        name: "Filtering",
                        type: "local",
                        filterDropDownItemIcons: false,
                        filterDropDownWidth: 200,
                         filterDialogHeight : "390px",
                        filterDialogWidth : "515px",
                        columnSettings: [
                            { columnKey: "parentKey", allowFiltering: false },
                            { columnKey: "operationId", allowFiltering: false }
                        ]
                    }
                ],
                autoGenerateColumns: false,
                primaryKey: "parentKey",
                hidePrimaryKey: true,
                columns: self.columnsIgGrid(),
                autoGenerateLayouts: false,
                virtualizationMode: 'continuous',
                columnLayouts: [
                    {
                        width: "100%",
                        childrenDataProperty: "lstLogPerCateCorrectRecordDto",
                        autoGenerateColumns: false,
                        hidePrimaryKey: true,
                        primaryKey: "childrentKey",
                        foreignKey: "operationId",
                        columns: [
                            { key: "childrentKey", headerText: "", dataType: "string", hidden: true },
                            { key: "categoryName", headerText: "categoryName", dataType: "string", width: "120px", formatter: _.escape },
                            { key: "targetDate", headerText: "targetDate", dataType: "string", width: "120px" },
                            { key: "itemName", headerText: "itemName", dataType: "string", width: "120px", formatter: _.escape },
                            { key: "infoOperateAttr", headerText: "infoOperateAttr", dataType: "string", width: "120px" },
                            { key: "valueBefore", headerText: "valueBefore", dataType: "string", width: "150px", formatter: _.escape },
                            { key: "valueAfter", headerText: "valueAfter", dataType: "string", width: "150px", formatter: _.escape }

                        ],
                        features: [
                            {
                                name: 'Selection',
                                mode: "row",
                                multipleSelection: false
                            },
                            {
                                name: "Responsive",
                                enableVerticalRendering: false,
                                columnSettings: []
                            }
                        ]
                    }
                ],
            });
            self.checkSubHeader();
        }
        generateDataCorrectLogGrid() {
            var self = this;
            let listLogBasicInfor = self.listLogBasicInforModel;
            //generate generateHierarchialGrid
            $("#igGridLog").igHierarchicalGrid({
                width: "100%",
                height: "calc(100% - 15px)",
                dataSource: listLogBasicInfor,
                features: [
                    {
                        name: "Tooltips",
                        inherit: true
                    },
                    {
                        name: "Responsive",
                        enableVerticalRendering: false
                    },
                    {
                        name: "Resizing",
                        deferredResizing: false,
                        allowDoubleClickToResize: true,
                        inherit: true
                    },
                    {
                        name: "Sorting",
                        inherit: false
                    },
                    {
                        name: "Paging",
                        pageSize: 100,
                        type: "local",
                        inherit: true
                    },
                    {
                        name: "Filtering",
                        type: "local",
                        filterDropDownItemIcons: false,
                        filterDropDownWidth: 200,
                        filterDialogHeight : "390px",
                        filterDialogWidth : "515px",
                        columnSettings: [
                            { columnKey: "parentKey", allowFiltering: false },
                            { columnKey: "operationId", allowFiltering: false }
                        ]
                    }
                ],
                autoGenerateColumns: false,
                primaryKey: "parentKey",
                hidePrimaryKey: true,
                columns: self.columnsIgGrid(),
                autoGenerateLayouts: false,
                virtualizationMode: 'continuous',
                columnLayouts: [
                    {
                        width: "100%",
                        childrenDataProperty: "lstLogDataCorrectRecordRefeDto",
                        hidePrimaryKey: true,
                        autoGenerateColumns: false,
                        primaryKey: "childrentKey",
                        foreignKey: "operationId",
                        columns: [
                            { key: "childrentKey", headerText: "", dataType: "string", hidden: true },
                            { key: "targetDate", headerText: "targetDate", dataType: "string", width: "170px" },
                            { key: "itemName", headerText: "itemName", dataType: "string", width: "170px", formatter: _.escape },
                            { key: "valueBefore", headerText: "valueBefore", dataType: "string", width: "170px", formatter: _.escape },
                            { key: "valueAfter", headerText: "valueAfter", dataType: "string", width: "170px", formatter: _.escape },
                            { key: "correctionAttr", headerText: "correctionAttr", dataType: "string", width: "170px" }
                        ],
                        features: [
                            
                            {
                                name: 'Selection',
                                multipleSelection: false
                            },
                            {
                                name: "Responsive",
                                enableVerticalRendering: false,
                                columnSettings: []
                            }
                           
                        ]
                    }
                ],
            });

            self.checkSubHeader();

        }
        checkSubHeader() {
            $("#igGridLog").scroll(function() {
                var showedIcon = $("#igGridLog").data("icon-showed");
                if (!_.isNil(showedIcon)) {
                    showedIcon.click();
                }
            });
            var self = this;
            $(document).delegate("#igGridLog", "igchildgridcreated", function(evt, ui) {
                var headerSetting = $(ui.element).data("headersetting");
                var header = ui.element.find("th[role='columnheader']");
                ui.element.parent().addClass("default-overflow");
                ui.element.parent().css("overflow-x","");

                let helpButton = $('<button>', {
                    text: getText('?'),
                    'data-bind': 'ntsHelpButton: { textId: "CLI003_68", textParams: ["{#CLI003_68}"], position: "right center" }'
                });

                let textHeaderCheck = getText('CLI003_61');
                for (var i = 0; i < headerSetting.length; i++) {
                    var currentSetting = headerSetting[i];

                    if (currentSetting.headerText == textHeaderCheck) {
                        var xHeader = header.filter("th[aria-label='" + currentSetting.key + "']").find(".ui-iggrid-headertext");
                        var x = xHeader.text(currentSetting.headerText);
                        x.append(helpButton);
                        xHeader.attr("id","help-button-id");
                    } else {
                        header.filter("th[aria-label='" + currentSetting.key + "']")
                            .find(".ui-iggrid-headertext").text(currentSetting.headerText)
                    }
                }
                helpButton.click(function() {
                    var container = helpButton.closest(".igscroll-touchscrollable");
                    var tooltip = helpButton.parent().find(".nts-help-button-image");
                    $(".ui-iggrid-header.ui-widget-header").css("overflow", "visible");
                    $("#help-button-id").css({"overflow":"visible"});
                    tooltip.css("width","350px");
                    if (tooltip.css("display") !== "none") {
                        container.addClass("default-overflow");
                        container.removeClass("overflow-show");
                        container.css("overflow-x","");
                        $("#igGridLog").data("icon-showed", helpButton);
                    } else {
                        container.removeClass("default-overflow");
                        container.addClass("overflow-show");
                        container.css("overflow-x","auto");
                        $("#igGridLog").data("icon-showed", null);
                    }
                });
                //  binding new viewmodel for only button help
                ko.applyBindings({}, helpButton[0]);
            });

            $(document).delegate("#igGridLog", "igchildgridcreating", function(evt, ui) {
                evt;
                var childSource = ui.options.dataSource;
                var ds = $("#igGridLog").igGrid("option", "dataSource");
                var parentSource = _.isArray(ds) ? ds : ds._data;
                var headerSetting = [];
                var newSource = [];
                let recordType = self.logTypeSelectedCode();
                if (childSource.length > 0) {
                    for (var i = 0; i < parentSource.length; i++) {
                        if (parentSource[i].parentKey === childSource[0].parentKey) {
                            headerSetting = parentSource[i].subColumnsHeaders;
                            if (recordType == RECORD_TYPE.DATA_CORRECT) {
                                newSource = _.cloneDeep(parentSource[i].lstLogDataCorrectRecordRefeDto);
                                newSource = _.orderBy(newSource, ['targetDate', 'showOrder'], ['asc', 'asc']);
                            }
                            if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                                newSource = _.cloneDeep(parentSource[i].lstLogPerCateCorrectRecordDto);
                            }
                        }
                    }
                    ui.options.dataSource = newSource;
                    $(ui.element).data("headersetting", headerSetting);
                }
            });
            $(document).delegate("#igGridLog", "iggridresizingcolumnresizing", function(evt, ui) {
                $(".ui-iggrid-scrolldiv.ui-widget-content.igscroll-touchscrollable.default-overflow").css("overflow-x", "auto");
                $(".ui-iggrid-header.ui-widget-header").css({"width":"100% !important"}); // th
                $(".ui-iggrid-headertext").css({"white-space":"nowrap","overflow":"hidden","display":"block"}); // span
            });
        }

        setListColumnHeaderLog(recordType: number, listOutputItem: Array<any>) {
            var self = this;
            self.columnsIgGrid.push(new IgGridColumnSwitchModel("primarykey", -1, recordType));
            self.columnsIgGrid.push(new IgGridColumnSwitchModel("parentkey", -2, recordType));
            let lstSubHeader = [22, 23, 24, 29, 30, 31, 33, 25, 26, 27, 28];
            let flg = true;
            let lstSubHeaderPersion = [25, 26, 27, 28];
            let lstSubHeaderDataCorrect = [22, 23, 24];
            _.forEach(listOutputItem, function(item) {
                if (lstSubHeader.indexOf(item.itemNo) > -1) {
                    if ((recordType == RECORD_TYPE.LOGIN || recordType == RECORD_TYPE.UPDATE_PERSION_INFO)
                        && ITEM_NO.ITEM_NO22 == item.itemNo) {
                        self.columnsIgGrid.push(new IgGridColumnSwitchModel(item.itemName, item.itemNo, recordType));
                    }
                    if (lstSubHeaderPersion.indexOf(item.itemNo) > -1 && recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        if (flg) {
                            self.supColumnsIgGrid.push(new IgGridColumnSwitchModel(item.itemName, item.itemNo, recordType));
                            flg = false;
                        }
                    } else {
                        self.supColumnsIgGrid.push(new IgGridColumnSwitchModel(item.itemName, item.itemNo, recordType));
                    }
                    if (lstSubHeaderDataCorrect.indexOf(item.itemNo) > -1 && recordType == RECORD_TYPE.DATA_CORRECT) {
                        if (flg) {
                            self.supColumnsIgGrid.push(new IgGridColumnSwitchModel(item.itemName, item.itemNo, recordType));
                            flg = false;
                        }
                    } else {
                        self.supColumnsIgGrid.push(new IgGridColumnSwitchModel(item.itemName, item.itemNo, recordType));
                    }
                }
                else {
                    self.columnsIgGrid.push(new IgGridColumnSwitchModel(item.itemName, item.itemNo, recordType));
                }
            });
        }
        exportCsvF() {
            let self = this;
            self.logBasicInforCsv = [];
            let recordType = Number(self.logTypeSelectedCode());
            _.forEach(self.listLogBasicInforModel, function(logBaseInfo) {
                let lstDataCorrect: DataCorrectLogModel[] = [];
                let lstPerCorrect: PerCateCorrectRecordModel[] = [];

                switch (recordType) {

                    case RECORD_TYPE.UPDATE_PERSION_INFO: {
                        //setting list persion correct
                        _.forEach(logBaseInfo.lstLogPerCateCorrectRecordDto, function(persionCorrect) {
                            lstPerCorrect.push(new PerCateCorrectRecordModel({
                                operationId: persionCorrect.operationId, targetDate: persionCorrect.targetDate,
                                categoryName: persionCorrect.categoryName, itemName: persionCorrect.itemName, valueBefore: persionCorrect.valueBefore, valueAfter: persionCorrect.valueAfter,
                                infoOperateAttr: persionCorrect.infoOperateAttr
                            }))
                        });
                        break;
                    }
                    case RECORD_TYPE.DATA_CORRECT: {
                        //setting list data correct
                        _.forEach(logBaseInfo.lstLogDataCorrectRecordRefeDto, function(dataCorrect) {
                            lstDataCorrect.push(new DataCorrectLogModel({
                                operationId: dataCorrect.operationId, targetDate: dataCorrect.targetDate,
                                targetDataType: dataCorrect.targetDataType, itemName: dataCorrect.itemName, valueBefore: dataCorrect.valueBefore, valueAfter: dataCorrect.valueAfter,
                                remarks: dataCorrect.remarks, correctionAttr: dataCorrect.correctionAttr
                            }))
                        });
                        break;
                    }
                    default: {
                        break;
                    }
                }

                let logBaseInfoTemp: LogBasicInfoModel = new LogBasicInfoModel({ loginBasicInfor: logBaseInfo, lstLogDataCorrectRecordRefeDto: lstDataCorrect, lstLogPerCateCorrectRecordDto: lstPerCorrect });
                self.logBasicInforCsv.push(logBaseInfoTemp);
            });

            let params = {
                recordType: Number(self.logTypeSelectedCode()),
                lstLogBasicInfoDto: self.logBasicInforCsv,
                lstHeaderDto: self.columnsIgGrid(),
                lstSupHeaderDto: self.supColumnsIgGrid()
            };
            service.logSettingExportCsv(params).done(() => {

            });
        }

        checkDestroyIgGrid() {
            let self = this;
            let recordType = Number(self.logTypeSelectedCode());
            if (recordType == RECORD_TYPE.DATA_CORRECT ||
                recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                //generate table data correct
                if ($('#igGridLog_container').length > 0 || $('#igGridLog_employeeCodeTaget').length > 0) {
                    $("#igGridLog").igHierarchicalGrid("destroy");
                }
            } else {
                if ($('#igGridLog_container').length > 0) {
                    $("#igGridLog").igGrid("destroy");
                }
            }
        }
        /**
        * start button screen C
        */
        backScreenCtoB() {
            var self = this;

            if (self.validateForm()) {
                self.previous();
                $("#ccgcomponent").hide();
                $('#list-box_b').focus();
                self.scrollToLeftTop();
            }

        }
        nextScreenCtoD() {
            var self = this;
            self.setEmployeeListTarget();
            self.getlogTypeName();
            self.getTargetDataTypeName();
            if (self.validateForm()) {
                if (self.selectedRuleCode() == EMPLOYEE_SPECIFIC.SPECIFY) {
                    if (self.selectedEmployeeCodeTarget().length > 0) {
                        self.next();
                        if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.SPECIFY) {
                            $("#ccgcomponent").show();
                            $("#employeeSearchD").show();
                        } else {
                            $("#ccgcomponent").hide();
                            $("#employeeSearchD").hide();
                        }
                    } else {
                        alertError({ messageId: 'Msg_1216', messageParams: [getText('CLI003_16')] });
                    }
                } else if (self.selectedRuleCode() == EMPLOYEE_SPECIFIC.ALL) {
                    self.next();
                    if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.SPECIFY) {
                        $("#ccgcomponent").show();
                        $("#employeeSearchD").show();
                    } else {
                        $("#ccgcomponent").hide();
                        $("#employeeSearchD").hide();
                    }
                }

            }

            $("#D1_4 .ntsDatepicker").first().attr("tabindex", -1).focus();
            self.scrollToLeftTop();
        }
        //and button screen C

        //B
        nextScreenCD() {
            let self = this;
            //  self.initComponentC();
            //  self.initComponentD();
            //present date time C   
            let checkLogType = parseInt(self.logTypeSelectedCode());
            let targetDataType = self.dataTypeSelectedCode();
            self.checkFormatDate('1');
            if (checkLogType == RECORD_TYPE.DATA_CORRECT
                && (targetDataType === '2' || targetDataType === '3'
                    || targetDataType === '6' || targetDataType === '7')) {
                self.dateValue = ko.observable({
                    startDate: moment.utc().format("YYYY/MM"),
                    endDate: moment.utc().format("YYYY/MM")
                });
                self.checkFormatDate('2');
            }

            if (checkLogType == RECORD_TYPE.DATA_CORRECT && targetDataType === "") {
                checkLogType = null;
            }
            if (checkLogType != RECORD_TYPE.UPDATE_PERSION_INFO) {
                 self.enableTagetDate(true);
            }
            switch (checkLogType) {
                case RECORD_TYPE.LOGIN:
                case RECORD_TYPE.START_UP:
                case RECORD_TYPE.UPDATE_MASTER:
                case RECORD_TYPE.TERMINAL_COMMUNICATION_INFO: {
                    $("#ex_accept_wizard-t-1").parents("li").hide();
                    $("#ex_accept_wizard").ntsWizard("goto", 2);
                    if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.SPECIFY) {
                        $("#ccgcomponent").show();
                        $("#employeeSearchD").show();
                    } else {
                        $("#ccgcomponent").hide();
                        $("#employeeSearchD").hide();
                    }
                    $("#D1_4 .ntsDatepicker").first().attr("tabindex", -1).focus();
                    self.scrollToLeftTop();
                    break;
                }
                case RECORD_TYPE.UPDATE_PERSION_INFO:
                    {
                        self.enableTagetDate(false);
                    }
                case RECORD_TYPE.DATA_REFERENCE:
                case RECORD_TYPE.DATA_MANIPULATION:
                case RECORD_TYPE.DATA_CORRECT:
                case RECORD_TYPE.MY_NUMBER: {
                    $("#ex_accept_wizard").ntsWizard("goto", 1);
                    if (self.selectedRuleCode() == EMPLOYEE_SPECIFIC.SPECIFY) {
                        $("#ccgcomponent").show();
                        $("#employeeSearch").show();
                    } else {
                        $("#ccgcomponent").hide();
                        $("#employeeSearch").hide();
                    }
                    $("#C1_4 .ntsStartDatePicker").focus();

                    self.scrollToLeftTop();
                    break;
                }
                default: {
                    break;
                }
            }

        }


        backScreenD() {
            var self = this;
            self.previous();
            if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.SPECIFY) {
                $("#ccgcomponent").show();
                $("#employeeSearchD").show();
            } else {
                $("#ccgcomponent").hide();
                $("#employeeSearchD").hide();
            }
            $("#D1_4 .ntsDatepicker").first().attr("tabindex", -1).focus();
            self.scrollToLeftTop();
        }
        /**
        * Open screen F
        */
        nextScreenF() {
            var self = this;
            // init compnent screen F
            self.startScreenF();
            self.checkDestroyIgGrid();
            self.next();
        }
        /**
        * Open screen I
        */
        nextScreenI() {
            var self = this;
            var paramRecordType = ko.observable(self.logTypeSelectedCode());
            var paramDataType = ko.observable(self.dataTypeSelectedCode());
            nts.uk.ui.windows.setShared("recordType", paramRecordType);
            nts.uk.ui.windows.setShared("tarGetDataType", paramDataType);
            $('#contents-area').focus();
            self.columnsIgAllGrid = ko.observableArray([]);
            self.listLogBasicInforModel = [];
            // set param log for export file CSV
            var format = 'YYYY/MM/DD HH:mm:ss';
            let paramLog = {
                // recordType=0,1 k co taget
                listTagetEmployeeId: self.targetEmployeeIdList(),
                listOperatorEmployeeId: self.listEmployeeIdOperator(),
                startDateTaget: moment.utc(self.dateValue().startDate, "YYYY/MM/DD").toISOString(),
                //                endDateTaget: moment.utc(self.dateValue().endDate, "YYYY/MM/DD").toISOString(),
                startDateOperator: moment.utc(self.startDateOperator(), format).toISOString(),
                endDateOperator: moment.utc(self.endDateOperator(), format).toISOString(),
                recordType: self.logTypeSelectedCode()
            };

            if (self.checkFormatDate() === '2') {             
                paramLog.endDateTaget = moment(self.dateValue().endDate, "YYYY/MM/DD" ).endOf('month').toDate();              
            } else {
                paramLog.endDateTaget = moment.utc(self.dateValue().endDate, "YYYY/MM/DD").toISOString();
                //  moment().endOf('month');
            }
            //fix itemNo
            let paramOutputItem = {
                recordType: self.logTypeSelectedCode()
            }

            let dfd = $.Deferred<any>();
            nts.uk.ui.windows.sub.modal("/view/cli/003/i/index.xhtml").onClosed(() => {
                let dataSelect = nts.uk.ui.windows.getShared("datacli003"),
                    selectCancel = nts.uk.ui.windows.getShared("selectCancel");
                paramLog.logDisplaySettingCode = dataSelect;
                // function get logdisplaysetting by code
                self.listItemNo = ko.observableArray([]);
                self.listLogBasicInforAllModel = [];
                self.listLogSetItemDetailDto = ko.observableArray([]);
                self.listHeaderSort = ko.observableArray([]);
                if (selectCancel == false) {
                    block.grayout();
                    service.logSettingExportCsvScreenI(paramLog).done(() => {
                    }).fail(function(error) {
                        alertError(error);
                        dfd.resolve();
                    }).always(() => {
                        block.clear();
                        errors.clearAll();

                    });
                }
                errors.clearAll();
                dfd.resolve();
            });
            //  return dfd.promise();  
        }

        setListColumnHeaderLogScreenI(recordType: number, listOutputItem: Array<any>) {
            var self = this;
            switch (recordType) {
                case RECORD_TYPE.LOGIN: {
                    _.forEach(listOutputItem, function(item) {
                        switch (item.itemNo) {
                            case ITEM_NO.ITEM_NO1: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userIdLogin", "string", ITEM_NO.ITEM_NO1));
                                break;
                            }
                            case ITEM_NO.ITEM_NO2: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userNameLogin", "string", ITEM_NO.ITEM_NO2));
                                break;
                            }
                            case ITEM_NO.ITEM_NO3: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employeeCodeLogin", "string", ITEM_NO.ITEM_NO3));
                                break;
                            }
                            case ITEM_NO.ITEM_NO4: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "ipAddress", "string", ITEM_NO.ITEM_NO4));
                                break;
                            }
                            case ITEM_NO.ITEM_NO5: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "pcName", "string", ITEM_NO.ITEM_NO5));
                                break;
                            }
                            case ITEM_NO.ITEM_NO6: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "account", "string", ITEM_NO.ITEM_NO6));
                                break;
                            }
                            case ITEM_NO.ITEM_NO7: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "modifyDateTime", "string", ITEM_NO.ITEM_NO7));
                                break;
                            }
                            case ITEM_NO.ITEM_NO8: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employmentAuthorityName", "string", ITEM_NO.ITEM_NO8));
                                break;
                            }
                            case ITEM_NO.ITEM_NO9: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "salarytAuthorityName", "string", ITEM_NO.ITEM_NO9));
                                break;
                            }
                            case ITEM_NO.ITEM_NO10: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personelAuthorityName", "string", ITEM_NO.ITEM_NO10));
                                break;
                            }
                            case ITEM_NO.ITEM_NO11: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "officeHelperAuthorityName", "string", ITEM_NO.ITEM_NO11));
                                break;
                            }
                            case ITEM_NO.ITEM_NO12: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "accountAuthorityName", "string", ITEM_NO.ITEM_NO12));
                                break;
                            }
                            case ITEM_NO.ITEM_NO13: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "myNumberAuthorityName", "string", ITEM_NO.ITEM_NO13));
                                break;
                            }
                            case ITEM_NO.ITEM_NO14: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "groupCompanyAddminAuthorityName", "string", ITEM_NO.ITEM_NO14));
                                break;
                            }
                            case ITEM_NO.ITEM_NO15: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "companyAddminAuthorityName", "string", ITEM_NO.ITEM_NO15));
                                break;
                            }
                            case ITEM_NO.ITEM_NO16: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "systemAdminAuthorityName", "string", ITEM_NO.ITEM_NO16));
                                break;
                            }
                            case ITEM_NO.ITEM_NO17: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personalInfoAuthorityName", "string", ITEM_NO.ITEM_NO17));
                                break;
                            }
                            case ITEM_NO.ITEM_NO18: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "menuName", "string", ITEM_NO.ITEM_NO18));
                                break;
                            }
                            case ITEM_NO.ITEM_NO19: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "loginStatus", "string", ITEM_NO.ITEM_NO19));
                                break;
                            }
                            case ITEM_NO.ITEM_NO20: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "loginMethod", "string", ITEM_NO.ITEM_NO20));
                                break;
                            }
                            case ITEM_NO.ITEM_NO21: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "accessResourceUrl", "string", ITEM_NO.ITEM_NO21));
                                break;
                            }
                            case ITEM_NO.ITEM_NO22: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "note", "string", ITEM_NO.ITEM_NO22));
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    });
                    break;
                }
                case RECORD_TYPE.START_UP: {
                    _.forEach(listOutputItem, function(item) {
                        switch (item.itemNo) {
                            case ITEM_NO.ITEM_NO1: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userIdLogin", "string", ITEM_NO.ITEM_NO1));
                                break;
                            }
                            case ITEM_NO.ITEM_NO2: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userNameLogin", "string", ITEM_NO.ITEM_NO2));
                                break;
                            }
                            case ITEM_NO.ITEM_NO3: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employeeCodeLogin", "string", ITEM_NO.ITEM_NO3));
                                break;
                            }
                            case ITEM_NO.ITEM_NO4: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "ipAddress", "string", ITEM_NO.ITEM_NO4));
                                break;
                            }
                            case ITEM_NO.ITEM_NO5: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "pcName", "string", ITEM_NO.ITEM_NO5));
                                break;
                            }
                            case ITEM_NO.ITEM_NO6: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "account", "string", ITEM_NO.ITEM_NO6));
                                break;
                            }
                            case ITEM_NO.ITEM_NO7: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "modifyDateTime", "string", ITEM_NO.ITEM_NO7));
                                break;
                            }
                            case ITEM_NO.ITEM_NO8: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employmentAuthorityName", "string", ITEM_NO.ITEM_NO8));
                                break;
                            }
                            case ITEM_NO.ITEM_NO9: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "salarytAuthorityName", "string", ITEM_NO.ITEM_NO9));
                                break;
                            }
                            case ITEM_NO.ITEM_NO10: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personelAuthorityName", "string", ITEM_NO.ITEM_NO10));
                                break;
                            }
                            case ITEM_NO.ITEM_NO11: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "officeHelperAuthorityName", "string", ITEM_NO.ITEM_NO11));
                                break;
                            }
                            case ITEM_NO.ITEM_NO12: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "accountAuthorityName", "string", ITEM_NO.ITEM_NO12));
                                break;
                            }
                            case ITEM_NO.ITEM_NO13: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "myNumberAuthorityName", "string", ITEM_NO.ITEM_NO13));
                                break;
                            }
                            case ITEM_NO.ITEM_NO14: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "groupCompanyAddminAuthorityName", "string", ITEM_NO.ITEM_NO14));
                                break;
                            }
                            case ITEM_NO.ITEM_NO15: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "companyAddminAuthorityName", "string", ITEM_NO.ITEM_NO15));
                                break;
                            }
                            case ITEM_NO.ITEM_NO16: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "systemAdminAuthorityName", "string", ITEM_NO.ITEM_NO16));
                                break;
                            }
                            case ITEM_NO.ITEM_NO17: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personalInfoAuthorityName", "string", ITEM_NO.ITEM_NO17));
                                break;
                            }
                            case ITEM_NO.ITEM_NO18: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "note", "string", ITEM_NO.ITEM_NO18));
                                break;
                            }
                            case ITEM_NO.ITEM_NO19: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "menuName", "string", ITEM_NO.ITEM_NO19));
                                break;
                            }
                            case ITEM_NO.ITEM_NO20: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "menuNameReSource", "string", ITEM_NO.ITEM_NO20));
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    });
                    break;
                }
                case RECORD_TYPE.UPDATE_PERSION_INFO: {
                    _.forEach(listOutputItem, function(item) {
                        switch (item.itemNo) {
                            case ITEM_NO.ITEM_NO1: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userIdLogin", "string", ITEM_NO.ITEM_NO1));
                                break;
                            }
                            case ITEM_NO.ITEM_NO2: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userNameLogin", "string", ITEM_NO.ITEM_NO2));
                                break;
                            }
                            case ITEM_NO.ITEM_NO3: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employeeCodeLogin", "string", ITEM_NO.ITEM_NO3));
                                break;
                            }
                            case ITEM_NO.ITEM_NO4: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "ipAddress", "string", ITEM_NO.ITEM_NO4));
                                break;
                            }
                            case ITEM_NO.ITEM_NO5: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "pcName", "string", ITEM_NO.ITEM_NO5));
                                break;
                            }
                            case ITEM_NO.ITEM_NO6: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "account", "string", ITEM_NO.ITEM_NO6));
                                break;
                            }
                            case ITEM_NO.ITEM_NO7: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "modifyDateTime", "string", ITEM_NO.ITEM_NO7));
                                break;
                            }
                            case ITEM_NO.ITEM_NO8: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employmentAuthorityName", "string", ITEM_NO.ITEM_NO8));
                                break;
                            }
                            case ITEM_NO.ITEM_NO9: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "salarytAuthorityName", "string", ITEM_NO.ITEM_NO9));
                                break;
                            }
                            case ITEM_NO.ITEM_NO10: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personelAuthorityName", "string", ITEM_NO.ITEM_NO10));
                                break;
                            }
                            case ITEM_NO.ITEM_NO11: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "officeHelperAuthorityName", "string", ITEM_NO.ITEM_NO11));
                                break;
                            }
                            case ITEM_NO.ITEM_NO12: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "accountAuthorityName", "string", ITEM_NO.ITEM_NO12));
                                break;
                            }
                            case ITEM_NO.ITEM_NO13: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "myNumberAuthorityName", "string", ITEM_NO.ITEM_NO13));
                                break;
                            }
                            case ITEM_NO.ITEM_NO14: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "groupCompanyAddminAuthorityName", "string", ITEM_NO.ITEM_NO14));
                                break;
                            }
                            case ITEM_NO.ITEM_NO15: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "companyAddminAuthorityName", "string", ITEM_NO.ITEM_NO15));
                                break;
                            }
                            case ITEM_NO.ITEM_NO16: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "systemAdminAuthorityName", "string", ITEM_NO.ITEM_NO16));
                                break;
                            }
                            case ITEM_NO.ITEM_NO17: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personalInfoAuthorityName", "string", ITEM_NO.ITEM_NO17));
                                break;
                            }
                            case ITEM_NO.ITEM_NO18: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "menuName", "string", ITEM_NO.ITEM_NO18));
                                break;
                            }
                            case ITEM_NO.ITEM_NO19: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userIdTaget", "string", ITEM_NO.ITEM_NO19));
                                break;
                            }
                            case ITEM_NO.ITEM_NO20: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userNameTaget", "string", ITEM_NO.ITEM_NO20));
                                break;
                            }
                            case ITEM_NO.ITEM_NO21: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employeeCodeTaget", "string", ITEM_NO.ITEM_NO21));
                                break;
                            }

                            case ITEM_NO.ITEM_NO22: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "categoryProcess", "string", ITEM_NO.ITEM_NO22));
                                break;
                            }
                            case ITEM_NO.ITEM_NO23: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "categoryName", "string", ITEM_NO.ITEM_NO23));
                                break;
                            }
                            case ITEM_NO.ITEM_NO24: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "methodCorrection", "string", ITEM_NO.ITEM_NO24));
                                break;
                            }
                            case ITEM_NO.ITEM_NO25: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "tarGetYmd", "string", ITEM_NO.ITEM_NO25));
                                break;
                            }
                            case ITEM_NO.ITEM_NO26: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "tarGetYm", "string", ITEM_NO.ITEM_NO26));
                                break;
                            }
                            case ITEM_NO.ITEM_NO27: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "tarGetY", "string", ITEM_NO.ITEM_NO27));
                                break;
                            }
                            case ITEM_NO.ITEM_NO28: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "keyString", "string", ITEM_NO.ITEM_NO28));
                                break;
                            }
                            case ITEM_NO.ITEM_NO29: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemName", "string", ITEM_NO.ITEM_NO29));
                                break;
                            }
                            case ITEM_NO.ITEM_NO30: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemvalueBefor", "string", ITEM_NO.ITEM_NO30));
                                break;
                            }
                            case ITEM_NO.ITEM_NO31: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemContentValueBefor", "string", ITEM_NO.ITEM_NO31));
                                break;
                            }
                            case ITEM_NO.ITEM_NO32: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemvalueAppter", "string", ITEM_NO.ITEM_NO32));
                                break;
                            }
                            case ITEM_NO.ITEM_NO33: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemContentValueAppter", "string", ITEM_NO.ITEM_NO33));
                                break;
                            }
                            case ITEM_NO.ITEM_NO34: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemEditAddition", "string", ITEM_NO.ITEM_NO34));
                                break;
                            }
                            case ITEM_NO.ITEM_NO35: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "tarGetYmdEditAddition", "string", ITEM_NO.ITEM_NO35));
                                break;
                            }
                            case ITEM_NO.ITEM_NO36: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "note", "string", ITEM_NO.ITEM_NO36));
                                break;
                            }

                            default: {
                                break;
                            }
                        }
                    });
                    break;
                }
                case RECORD_TYPE.DATA_CORRECT: {
                    _.forEach(listOutputItem, function(item) {
                        //start
                        switch (item.itemNo) {
                            case ITEM_NO.ITEM_NO1: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userIdLogin", "string", ITEM_NO.ITEM_NO1));
                                break;
                            }
                            case ITEM_NO.ITEM_NO2: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userNameLogin", "string", ITEM_NO.ITEM_NO2));
                                break;
                            }
                            case ITEM_NO.ITEM_NO3: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employeeCodeLogin", "string", ITEM_NO.ITEM_NO3));
                                break;
                            }
                            case ITEM_NO.ITEM_NO4: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "ipAddress", "string", ITEM_NO.ITEM_NO4));
                                break;
                            }
                            case ITEM_NO.ITEM_NO5: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "pcName", "string", ITEM_NO.ITEM_NO5));
                                break;
                            }
                            case ITEM_NO.ITEM_NO6: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "account", "string", ITEM_NO.ITEM_NO6));
                                break;
                            }
                            case ITEM_NO.ITEM_NO7: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "modifyDateTime", "string", ITEM_NO.ITEM_NO7));
                                break;
                            }
                            case ITEM_NO.ITEM_NO8: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employmentAuthorityName", "string", ITEM_NO.ITEM_NO8));
                                break;
                            }
                            case ITEM_NO.ITEM_NO9: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "salarytAuthorityName", "string", ITEM_NO.ITEM_NO9));
                                break;
                            }
                            case ITEM_NO.ITEM_NO10: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personelAuthorityName", "string", ITEM_NO.ITEM_NO10));
                                break;
                            }
                            case ITEM_NO.ITEM_NO11: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "officeHelperAuthorityName", "string", ITEM_NO.ITEM_NO11));
                                break;
                            }
                            case ITEM_NO.ITEM_NO12: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "accountAuthorityName", "string", ITEM_NO.ITEM_NO12));
                                break;
                            }
                            case ITEM_NO.ITEM_NO13: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "myNumberAuthorityName", "string", ITEM_NO.ITEM_NO13));
                                break;
                            }
                            case ITEM_NO.ITEM_NO14: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "groupCompanyAddminAuthorityName", "string", ITEM_NO.ITEM_NO14));
                                break;
                            }
                            case ITEM_NO.ITEM_NO15: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "companyAddminAuthorityName", "string", ITEM_NO.ITEM_NO15));
                                break;
                            }
                            case ITEM_NO.ITEM_NO16: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "systemAdminAuthorityName", "string", ITEM_NO.ITEM_NO16));
                                break;
                            }
                            case ITEM_NO.ITEM_NO17: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "personalInfoAuthorityName", "string", ITEM_NO.ITEM_NO17));
                                break;
                            }
                            case ITEM_NO.ITEM_NO18: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "menuName", "string", ITEM_NO.ITEM_NO18));
                                break;
                            }
                            case ITEM_NO.ITEM_NO19: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userIdTaget", "string", ITEM_NO.ITEM_NO19));
                                break;
                            }
                            case ITEM_NO.ITEM_NO20: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "userNameTaget", "string", ITEM_NO.ITEM_NO20));
                                break;
                            }
                            case ITEM_NO.ITEM_NO21: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "employeeCodeTaget", "string", ITEM_NO.ITEM_NO21));
                                break;
                            }
                            case ITEM_NO.ITEM_NO22: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "tarGetYmd", "string", ITEM_NO.ITEM_NO22));
                                break;
                            }
                            case ITEM_NO.ITEM_NO23: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "tarGetYm", "string", ITEM_NO.ITEM_NO23));
                                break;
                            }
                            case ITEM_NO.ITEM_NO24: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "tarGetY", "string", ITEM_NO.ITEM_NO24));
                                break;
                            }
                            case ITEM_NO.ITEM_NO25: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "keyString", "string", ITEM_NO.ITEM_NO25));
                                break;
                            }
                            case ITEM_NO.ITEM_NO26: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "catagoryCorection", "string", ITEM_NO.ITEM_NO26));
                                break;
                            }
                            case ITEM_NO.ITEM_NO27: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemName", "string", ITEM_NO.ITEM_NO27));
                                break;
                            }
                            case ITEM_NO.ITEM_NO28: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemvalueBefor", "string", ITEM_NO.ITEM_NO28));
                                break;
                            }
                            case ITEM_NO.ITEM_NO29: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemvalueAppter", "string", ITEM_NO.ITEM_NO29));
                                break;
                            }
                            case ITEM_NO.ITEM_NO30: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemContentValueBefor", "string", ITEM_NO.ITEM_NO30));
                                break;
                            }
                            case ITEM_NO.ITEM_NO31: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "itemContentValueAppter", "string", ITEM_NO.ITEM_NO31));
                                break;
                            }
                            case ITEM_NO.ITEM_NO32: {
                                self.columnsIgAllGrid.push(new IgGridColumnAllModel(item.itemName, "note", "string", ITEM_NO.ITEM_NO32));
                                break;
                            }
                            default: {
                                break;
                            }

                        }
                        //end

                    });

                }
                    break;
                default: {
                    break;
                }
            }
        }
        
        // export 
        exportCsvI(params: any) {
            let self = this;
            let dfd = $.Deferred<any>();
            block.grayout();
            service.logSettingExportCsvScreenI(params).done(() => {
            }).fail(function(error) {
                alertError(error);
                dfd.resolve();
            }).always(() => {
                block.clear();
                errors.clearAll();

            });
            return dfd.promise();
        }

        backScreenDtoBC() {
            var self = this;
            //back to Screen B
            if (self.validateForm()) {
                switch (Number(self.logTypeSelectedCode())) {
                    case RECORD_TYPE.LOGIN:
                    case RECORD_TYPE.START_UP:
                    case RECORD_TYPE.UPDATE_MASTER:
                    case RECORD_TYPE.TERMINAL_COMMUNICATION_INFO: {
                        $("#ccgcomponent").hide();
                        $("#ex_accept_wizard-t-1").parents("li").show();
                        $("#ex_accept_wizard").ntsWizard("goto", 0);
                        $('#list-box_b').focus();
                        self.scrollToLeftTop();
                        break;
                    }
                    // back to Screen C
                    case RECORD_TYPE.UPDATE_PERSION_INFO:
                    case RECORD_TYPE.DATA_REFERENCE:
                    case RECORD_TYPE.DATA_MANIPULATION:
                    case RECORD_TYPE.DATA_CORRECT:
                    case RECORD_TYPE.MY_NUMBER: {
                        self.previous();
                        if (self.selectedRuleCode() == EMPLOYEE_SPECIFIC.SPECIFY) {
                            $("#ccgcomponent").show();
                            $("#employeeSearch").show();
                        } else {
                            $("#ccgcomponent").hide();
                            $("#employeeSearch").hide();
                        }
                        $(".ntsStartDate").focus();
                        self.scrollToLeftTop();
                        $("#C1_4 .ntsStartDatePicker").focus();
                        break;
                    }
                }
            }


        }

        nextScreenDtoE() {
            let self = this;
            self.getListEmployeeIdOperator();
            self.getlogTypeName();
            self.getTargetDataTypeName();
            self.getDateOperator();
            self.getOperatorNumber();
            self.getTargetNumber();
            self.getTargetDate();

            //Check to display or not Screen C infomation.
            self.isDisplayTarget(false);
            self.displayTargetDate(true);

            switch (Number(self.logTypeSelectedCode())) {
                case RECORD_TYPE.UPDATE_PERSION_INFO:
                    {
                        self.isDisplayTarget(true);
                        self.displayTargetDate(false);
                        break;
                    }
                case RECORD_TYPE.DATA_REFERENCE:
                case RECORD_TYPE.DATA_MANIPULATION:
                case RECORD_TYPE.DATA_CORRECT:
                case RECORD_TYPE.MY_NUMBER: {
                    self.isDisplayTarget(true);
                    break;
                }
            }
            if (self.validateForm()) {
                if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.SPECIFY) {
                    if (self.selectedEmployeeCodeOperator().length > 0) {
                        $("#ccgcomponent").hide();
                        self.next();
                    }
                    else {
                        alertError({ messageId: 'Msg_1216', messageParams: [getText('CLI003_23')] });
                    }
                }
                if (self.selectedRuleCodeOperator() == EMPLOYEE_SPECIFIC.ALL) {
                    $("#ccgcomponent").hide();
                    self.next();
                }
            }
            $('#E2_3').focus();
            self.scrollToLeftTop();
        }
        previousScreenE() {
            var self = this;
            self.previous();
            $('#E2_3').focus();
            self.checkDestroyIgGrid();
            self.scrollToLeftTop();
        }
        next() {
            $('#ex_accept_wizard').ntsWizard("next");
        }

        previous() {
            $('#ex_accept_wizard').ntsWizard("prev");
        }
        private scrollToLeftTop(): void {
            $("#contents-area").scrollLeft(0);
            $("#contents-area").scrollTop(0);
        }
        public switchCodeChange(): void {
            let self = this;
            self.selectedRuleCode.subscribe((newCode) => {
                if (newCode == EMPLOYEE_SPECIFIC.ALL) {
                    $("#ccgcomponent").hide();
                    $("#employeeSearch").hide();
                } else {
                    $("#ccgcomponent").show();
                    $("#employeeSearch").show();
                }
            });
            self.selectedRuleCodeOperator.subscribe((newCode) => {
                if (newCode == EMPLOYEE_SPECIFIC.ALL) {
                    $("#ccgcomponent").hide();
                    $("#employeeSearchD").hide();
                } else {
                    $("#ccgcomponent").show();
                    $("#employeeSearchD").show();
                }
            });

        }
    }


    class ItemModel {
        code: number;
        name: string;
        description: string;
        constructor(code: number, name: string, description: string) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
    }
    class TargetDataTypeModel {
        code: number;
        name: string;
        description: string;
        constructor(code: number, name: string, description: string) {
            this.code = code;
            this.name = name;
            this.description = description;
        }
    }

    class LogBasicInfoModel {
        parentKey: string;
        operationId: string;
        userNameLogin: string;
        employeeCodeLogin: string;
        userIdTaget: string;
        userNameTaget: string;
        employeeCodeTaget: string;
        modifyDateTime: string;
        menuName: string;
        note: string;
        methodName: string;
        loginStatus: string;
        processAttr: string;
        lstLogDataCorrectRecordRefeDto: KnockoutObservableArray<DataCorrectLogModel>;
        lstLogLoginDto: KnockoutObservableArray<LoginLogModel>;
        lstLogOutputItemDto: KnockoutObservableArray<LogOutputItemDto>;
        subColumnsHeaders: KnockoutObservableArray<IgGridColumnModel>;
        lstLogPerCateCorrectRecordDto: KnockoutObservableArray<PerCateCorrectRecordModel>;
        isDisplayText: boolean;
        constructor(param: LogBasicInfoParam) {
            this.userNameLogin = param.loginBasicInfor.userNameLogin;
            this.employeeCodeLogin = param.loginBasicInfor.employeeCodeLogin;
            this.userIdTaget = param.loginBasicInfor.userIdTaget;
            this.userNameTaget = param.loginBasicInfor.userNameTaget;
            this.employeeCodeTaget = param.loginBasicInfor.employeeCodeTaget;
            this.modifyDateTime = param.loginBasicInfor.modifyDateTime;
            this.processAttr = param.loginBasicInfor.processAttr;
            this.lstLogLoginDto = param.loginBasicInfor.lstLogLoginDto;
            this.lstLogOutputItemDto = param.loginBasicInfor.lstLogOutputItemDto;
            this.menuName = param.loginBasicInfor.menuName;
            this.note = param.loginBasicInfor.note;
            this.methodName = param.loginBasicInfor.methodName;
            this.loginStatus = param.loginBasicInfor.loginStatus;
            this.lstLogDataCorrectRecordRefeDto = param.lstLogDataCorrectRecordRefeDto ? param.lstLogDataCorrectRecordRefeDto : [];
            this.lstLogPerCateCorrectRecordDto = param.lstLogPerCateCorrectRecordDto ? param.lstLogPerCateCorrectRecordDto : [];
        }
    }

    class LogBasicInforAllModel {
        operationId: string;
        userNameLogin: string;
        employeeCodeLogin: string;
        userIdTaget: string;
        userNameTaget: string;
        employeeIdTaget: string;
        employeeCodeTaget: string;
        ipAddress: string;
        modifyDateTime: string;
        processAttr: string;
        menuName: string;
        note: string;
        menuNameReSource: string;
        methodName: string;
        loginStatus: string;
        userIdLogin: string;
        pcName: string;
        account: string;
        employmentAuthorityName: string;
        salarytAuthorityName: string;
        personelAuthorityName: string;
        officeHelperAuthorityName: string;
        accountAuthorityName: string;
        myNumberAuthorityName: string;
        groupCompanyAddminAuthorityName: string;
        companyAddminAuthorityName: string;
        systemAdminAuthorityName: string;
        personalInfoAuthorityName: string;
        accessResourceUrl: string;
        constructor(userNameLogin: string, employeeCodeLogin: string, userIdTaget: string, userNameTaget: string,
            employeeIdTaget: string, employeeCodeTaget: string, ipAddress: string, modifyDateTime: string,
            processAttr: string, menuName: string, note: string, menuNameReSource: string, methodName: string,
            loginStatus: string, userIdLogin: string, pcName: string, account: string,
            employmentAuthorityName: string, salarytAuthorityName: string, personelAuthorityName: string,
            officeHelperAuthorityName: string, accountAuthorityName: string, myNumberAuthorityName: string,
            groupCompanyAddminAuthorityName: string,
            companyAddminAuthorityName: string,
            systemAdminAuthorityName: string,
            personalInfoAuthorityName: string,
            accessResourceUrl: string) {
            this.userNameLogin = userNameLogin;
            this.employeeCodeLogin = employeeCodeLogin;
            this.userIdTaget = userIdTaget;
            this.userNameTaget = userNameTaget;
            this.employeeIdTaget = employeeIdTaget;
            this.employeeCodeTaget = employeeCodeTaget;
            this.ipAddress = ipAddress;
            this.modifyDateTime = modifyDateTime;
            this.processAttr = processAttr;
            this.menuName = menuName;
            this.note = note;
            this.menuNameReSource;
            this.methodName = methodName;
            this.loginStatus = loginStatus;
            this.userIdLogin = userIdLogin;
            this.pcName = pcName;
            this.account = account;
            this.employmentAuthorityName = employmentAuthorityName;
            this.salarytAuthorityName = salarytAuthorityName;
            this.personelAuthorityName = personelAuthorityName;
            this.officeHelperAuthorityName = officeHelperAuthorityName;
            this.accountAuthorityName = accountAuthorityName;
            this.myNumberAuthorityName = myNumberAuthorityName;
            this.groupCompanyAddminAuthorityName = groupCompanyAddminAuthorityName;
            this.companyAddminAuthorityName = companyAddminAuthorityName;
            this.systemAdminAuthorityName = systemAdminAuthorityName;
            this.personalInfoAuthorityName = personalInfoAuthorityName;
            this.accessResourceUrl = accessResourceUrl;

        }
    }

    class DataCorrectLogModel {
        parentKey: string;
        childrentKey: string;
        operationId: string;
        targetDate: string;
        targetDataType: number;
        itemName: string;
        valueBefore: string;
        valueAfter: string;
        remarks: string;
        correctionAttr: string;
        showOrder: number
        constructor(param: DataCorrectParam) {
            this.operationId = param.operationId;
            this.targetDate = param.targetDate;
            this.targetDataType = param.targetDataType;
            this.itemName = param.itemName;
            this.valueBefore = param.valueBefore;
            this.valueAfter = param.valueAfter;
            this.remarks = param.remarks;
            this.correctionAttr = param.correctionAttr;
        }
    }
    class PerCateCorrectRecordModel {
        parentKey: string;
        childrentKey: string;
        operationId: string;
        targetDate: string;
        categoryName: string;
        itemName: string;
        valueBefore: string;
        valueAfter: string;
        infoOperateAttr: string;
        constructor(param: PersionCorrectParam) {
            this.operationId = param.operationId;
            this.targetDate = param.targetDate;
            this.itemName = param.itemName;
            this.valueBefore = param.valueBefore;
            this.valueAfter = param.valueAfter;
            this.infoOperateAttr = param.infoOperateAttr;
            this.categoryName = param.categoryName;
        }
    }

    class LogOutputItemDto {
        itemNo: number;
        itemName: string;
        recordType: number;
        sortOrder: number;
        parentKey: string;
        constructor(itemNo: number, itemName: string, recordType: number, sortOrder: number, parentKey: string) {
            this.itemNo = itemNo;
            this.itemName = itemName;
            this.recordType = recordType;
            this.sortOrder = sortOrder;
            this.parentKey = parentKey;
        }
    }

    class IgGridColumnModel {
        headerText: string;
        key: string;
        dataType: string;
        hidden: boolean;
        // setting for using export csv
        itemName: string;
        constructor(headerText: string, key: string, dataType: string, hidden: boolean) {
            this.headerText = headerText;
            this.key = key;
            this.dataType = dataType;
            this.hidden = hidden;
            this.itemName = headerText;
        }
    }

    class IgGridColumnSwitchModel {
        headerText: string;
        key: string;
        dataType: string;
        hidden: boolean;
        itemName: string;
        width: string;
        constructor(headerText: string, itemNo: number, recordType: number) {
            this.headerText = headerText;
            this.hidden = false;
            this.dataType = ITEM_PROPERTY.ITEM_SRT;
            this.itemName = headerText;
            switch (itemNo) {
                case -1: {
                    this.key = ITEM_PROPERTY.ITEM_OPERATION_ID;
                    this.hidden = true;
                    break;
                }
                case -2: {
                    this.key = ITEM_PROPERTY.ITEM_PARRENT_KEY;
                    this.hidden = true;
                    break;
                }
                case ITEM_NO.ITEM_NO2: {
                    this.key = ITEM_PROPERTY.ITEM_USER_NAME_LOGIN;
                    if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.width = "120px";
                    } else {
                        this.width = "170px";
                    }

                    break;
                }
                case ITEM_NO.ITEM_NO3: {
                    this.key = ITEM_PROPERTY.ITEM_EMP_CODE_LOGIN;
                    if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.width = "120px";
                    } else {
                        this.width = "170px";
                    }
                    break;
                }
                case ITEM_NO.ITEM_NO7: {
                    this.key = ITEM_PROPERTY.ITEM_MODIFY_DATE;
                    this.width = "170px";
                    break;
                }
                case ITEM_NO.ITEM_NO18: {
                    this.key = ITEM_PROPERTY.ITEM_NOTE;
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO19: {
                    if (recordType == RECORD_TYPE.LOGIN) {
                        this.key = ITEM_PROPERTY.ITEM_LOGIN_STATUS;
                        this.width = "120px";
                    }
                    if (recordType == RECORD_TYPE.START_UP) {
                        this.key = ITEM_PROPERTY.ITEM_MENU_NAME;
                        this.width = "170px";
                    }
                    break;
                }
                case ITEM_NO.ITEM_NO20: {
                    if (recordType == RECORD_TYPE.LOGIN) {
                        this.key = ITEM_PROPERTY.ITEM_METHOD_NAME;
                    }
                    if (recordType == RECORD_TYPE.DATA_CORRECT
                        || recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.key = ITEM_PROPERTY.ITEM_USER_NAME_TAGET;
                    }
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO21: {
                    if (recordType == RECORD_TYPE.DATA_CORRECT
                        || recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.key = ITEM_PROPERTY.ITEM_EMP_CODE_TAGET;
                    }
                    if (recordType == RECORD_TYPE.DATA_CORRECT) {
                        this.width = "170px";
                    } else {
                        this.width = "120px";
                    }
                    break;
                }
                case ITEM_NO.ITEM_NO22: {
                    if (recordType == RECORD_TYPE.LOGIN) {
                        this.key = ITEM_PROPERTY.ITEM_NOTE;
                    }
                    if (recordType == RECORD_TYPE.DATA_CORRECT) {
                        this.key = ITEM_PROPERTY.ITEM_TAGET_DATE;
                    }
                    if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.key = ITEM_PROPERTY.ITEM_PROCESS_ATTR;
                    }
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO23: {
                    if (recordType == RECORD_TYPE.DATA_CORRECT) {
                        this.key = ITEM_PROPERTY.ITEM_TAGET_DATE;
                    }
                    if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.key = ITEM_PROPERTY.ITEM_CATEGORY_NAME;
                    }
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO24: {
                    if (recordType == RECORD_TYPE.DATA_CORRECT) {
                        this.key = ITEM_PROPERTY.ITEM_TAGET_DATE;
                    }
                    if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.key = ITEM_PROPERTY.ITEM_INFO_OPERATE_ATTR;
                    }
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO26: {
                    this.key = ITEM_PROPERTY.ITEM_CORRECT_ATTR;
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO27: {
                    if (recordType == RECORD_TYPE.DATA_CORRECT) {
                        this.key = ITEM_PROPERTY.ITEM_NAME;
                    }
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO29: {
                    this.key = ITEM_PROPERTY.ITEM_NAME;
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO30: {
                    this.key = ITEM_PROPERTY.ITEM_VALUE_BEFOR;
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO31: {
                    if (recordType == RECORD_TYPE.DATA_CORRECT) {
                        this.key = ITEM_PROPERTY.ITEM_VALUE_AFTER;
                    }
                    if (recordType == RECORD_TYPE.UPDATE_PERSION_INFO) {
                        this.key = ITEM_PROPERTY.ITEM_VALUE_BEFOR;
                    }
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO33: {
                    this.key = ITEM_PROPERTY.ITEM_VALUE_AFTER;
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO36: {
                    this.key = ITEM_PROPERTY.ITEM_NOTE;
                    this.width = "120px";
                    break;
                }
                case ITEM_NO.ITEM_NO99: {
                    this.key = ITEM_PROPERTY.ITEM_TAGET_DATE;
                    this.width = "120px";
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
    class IgGridColumnAllModel {
        headerText: string;
        key: string;
        dataType: string;
        itemNo: number;

        // setting for using export csv
        itemName: string;
        constructor(headerText: string, key: string, dataType: string, itemNo: number) {
            this.headerText = headerText;
            this.key = key;
            this.dataType = dataType;
            this.itemNo = itemNo;
            this.itemName = headerText;
        }
    }

    /**
    * The enum of RECORD_TYPE
    */
    export enum RECORD_TYPE {
        LOGIN = 0,
        START_UP = 1,
        UPDATE_MASTER = 2,
        UPDATE_PERSION_INFO = 3,
        DATA_REFERENCE = 4,
        DATA_MANIPULATION = 5,
        DATA_CORRECT = 6,
        MY_NUMBER = 7,
        TERMINAL_COMMUNICATION_INFO = 8
    }

    /**
* The enum of RECORD_TYPE
*/
    export enum ITEM_NO {
        ITEM_NO1 = 1,
        ITEM_NO2 = 2,
        ITEM_NO3 = 3,
        ITEM_NO4 = 4,
        ITEM_NO5 = 5,
        ITEM_NO6 = 6,
        ITEM_NO7 = 7,
        ITEM_NO8 = 8,
        ITEM_NO9 = 9,
        ITEM_NO10 = 10,
        ITEM_NO11 = 11,
        ITEM_NO12 = 12,
        ITEM_NO13 = 13,
        ITEM_NO14 = 14,
        ITEM_NO15 = 15,
        ITEM_NO16 = 16,
        ITEM_NO17 = 17,
        ITEM_NO18 = 18,
        ITEM_NO19 = 19,
        ITEM_NO20 = 20,
        ITEM_NO21 = 21,
        ITEM_NO22 = 22,
        ITEM_NO23 = 23,
        ITEM_NO24 = 24,
        ITEM_NO25 = 25,
        ITEM_NO26 = 26,
        ITEM_NO27 = 27,
        ITEM_NO28 = 28,
        ITEM_NO29 = 29,
        ITEM_NO30 = 30,
        ITEM_NO31 = 31,
        ITEM_NO32 = 32,
        ITEM_NO33 = 33,
        ITEM_NO34 = 34,
        ITEM_NO35 = 35,
        ITEM_NO36 = 36,
        ITEM_NO99 = 99
    }

    /**
    * The enum of property setting data
    */
    export enum ITEM_PROPERTY {
        ITEM_SRT = "string",
        ITEM_USER_NAME_LOGIN = "userNameLogin",
        ITEM_EMP_CODE_LOGIN = "employeeCodeLogin",
        ITEM_MODIFY_DATE = "modifyDateTime",
        ITEM_LOGIN_STATUS = "loginStatus",
        ITEM_METHOD_NAME = "methodName",
        ITEM_NOTE = "note",
        ITEM_MENU_NAME = "menuName",
        ITEM_USER_NAME_TAGET = "userNameTaget",
        ITEM_EMP_CODE_TAGET = "employeeCodeTaget",
        ITEM_PROCESS_ATTR = "processAttr",
        ITEM_CATEGORY_NAME = "categoryName",
        ITEM_TAGET_DATE = "targetDate",
        ITEM_INFO_OPERATE_ATTR = "infoOperateAttr",
        ITEM_NAME = "itemName",
        ITEM_VALUE_BEFOR = "valueBefore",
        ITEM_VALUE_AFTER = "valueAfter",
        ITEM_CORRECT_ATTR = "correctionAttr",
        ITEM_OPERATION_ID = "operationId",
        ITEM_PARRENT_KEY = "parentKey"
    }
    /*C
    *the enum of EMPLOYEE_SPECIFIC
    */
    export enum EMPLOYEE_SPECIFIC {
        SPECIFY = 1,
        ALL = 2
    }

    export class UnitModel {
        id: string;
        code: string;
        name: string;
        workplaceName: string;

        constructor(id: string, code: string, name: string, workplaceName: string) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.workplaceName = workplaceName;
        }
    }

    export interface LogBasicInfoParam {
        loginBasicInfor: LogBasicInfoModel;
        lstLogDataCorrectRecordRefeDto: KnockoutObservableArray<DataCorrectLogModel>;
        lstLogPerCateCorrectRecordDto: KnockoutObservableArray<PerCateCorrectRecordModel>
    }
    export interface DataCorrectParam {
        operationId: string;
        targetDate: string;
        targetDataType: number;
        itemName: string;
        valueBefore: string;
        valueAfter: string;
        remarks: string;
        correctionAttr: string;
    }
    export interface PersionCorrectParam {
        operationId: string;
        targetDate: string;
        categoryName: string;
        itemName: string;
        valueBefore: string;
        valueAfter: string;
        infoOperateAttr: string;
    }
    export class LogSetItemDetailDto {
        logSetId: string;
        itemNo: number;
        frame: number;
        isUseCondFlg: number;
        condition: string;
        sybol: number;

        constructor(logSetId: string, itemNo: string, frame: string, isUseCondFlg: string, condition: string, sybol: number) {
            this.logSetId = logSetId;
            this.itemNo = itemNo;
            this.frame = frame;
            this.isUseCondFlg = isUseCondFlg;
            this.condition = condition;
            this.sybol = sybol;
        }
    }
}


