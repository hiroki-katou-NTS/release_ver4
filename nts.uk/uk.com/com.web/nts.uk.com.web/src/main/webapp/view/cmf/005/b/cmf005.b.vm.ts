module nts.uk.com.view.cmf005.b.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import model = cmf005.share.model;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    //D Screen
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
        //Radio button
        rdSelected: KnockoutObservable<any>;
        optionCategory: KnockoutObservable<any>;
        optionDeleteSet: KnockoutObservable<any>;

        //information category
        deleteSetName: KnockoutObservable<string>;


        // B5_2_2
        systemTypeCbb: KnockoutObservable<model.ItemModel>;
        systemType: KnockoutObservable<number>;

        //B5_3
        listDataCategory: KnockoutObservableArray<model.ItemCategory>;
        listColumnHeader: KnockoutObservableArray<NtsGridListColumn>;
        currentCategory: KnockoutObservableArray<any>;

        //datepicker B5_2_2
        enableDate: KnockoutObservable<boolean>;
        enableMonth: KnockoutObservable<boolean>;
        enableYear: KnockoutObservable<boolean>;
        requiredDate: KnockoutObservable<boolean>;
        requiredMonth: KnockoutObservable<boolean>;
        requiredYear: KnockoutObservable<boolean>;
        dateValue: KnockoutObservable<any>;
        monthValue: KnockoutObservable<any>;
        yearValue: KnockoutObservable<any>;
        startDateDailyString: KnockoutObservable<string>;
        endDateDailyString: KnockoutObservable<string>;
        startDateMothlyString: KnockoutObservable<string>;
        endDateMothlyString: KnockoutObservable<string>;
        startDateYearlyString: KnockoutObservable<string>;
        endDateYearlyString: KnockoutObservable<string>;

        //B7_1
        saveBeforDeleteOption: KnockoutObservableArray<model.ItemModel>;
        isSaveBeforeDeleteFlg: number;
        isDelete : KnockoutObservableArray<boolean>;
        
        //B8_1
        isExistCompressPasswordFlg: KnockoutObservable<boolean>;
        passwordForCompressFile: KnockoutObservable<string>;
        confirmPasswordForCompressFile: KnockoutObservable<string>;
        // check password constraint 
        passwordConstraint: KnockoutObservable<string>;

        //B9_2
        supplementExplanation: KnockoutObservable<string>;

        //D
        // reference date
        referenceDate: string;
        //Radio button
        itemTitleAtr: KnockoutObservableArray<any>;
        selectedTitleAtr: KnockoutObservable<number>;

        ccg001ComponentOption: GroupOption;

        listComponentOption: any;
        selectedEmployeeCode: KnockoutObservableArray<string>;
        employeeList: KnockoutObservableArray<UnitModel>;
        initEmployeeList: KnockoutObservableArray<UnitModel>;
        alreadySettingPersonal: KnockoutObservableArray<UnitAlreadySettingModel>;

        //E
        employeeDeletionList: KnockoutObservableArray<EmployeeDeletion>;
        columnEmployees: KnockoutObservableArray<NtsGridListColumn>;
        currentCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;
        categoryDeletionList: KnockoutObservableArray<CategoryDeletion>;
        delId : KnockoutObservable<string>;

        constructor() {
            var self = this;
            self.initComponents();
            self.setDefault();
        }

        initComponents() {
            var self = this;
            //View menu step
            self.stepList = [
                { content: '.step-1' },
                { content: '.step-2' },
                { content: '.step-3' }
            ];
            self.activeStep = ko.observable(0);
            self.stepSelected = ko.observable({ id: 'step-1', content: '.step-1' });

            //Radio button
            self.optionCategory = ko.observable({ value: 1, text: getText("CMF005_15") });
            self.optionDeleteSet = ko.observable({ value: 2, text: getText("CMF005_16") });
            self.deleteSetName = ko.observable('');

            //B5_3
            self.listDataCategory = ko.observableArray([]);
            self.currentCode = ko.observable();
            self.currentCategory = ko.observableArray([]);
            self.listColumnHeader = ko.observableArray([
                { headerText: '', key: 'categoryId', hidden: true },
                { headerText: getText('CMF005_24'), key: 'categoryName', width: 220 },
                { headerText: getText('CMF005_25'), key: 'timeStore', width: 100, formatter: timeStore },
                { headerText: getText('CMF005_26'), key: 'storageRangeSaved', width: 100, formatter: storageRangeSaved }
            ]);

            //DatePcicker B6_1
            self.enableDate = ko.observable(true);
            self.enableMonth = ko.observable(true);
            self.enableYear = ko.observable(true);

            self.requiredDate = ko.observable(false);
            self.requiredMonth = ko.observable(false);
            self.requiredYear = ko.observable(false);

            self.startDateDailyString = ko.observable("");
            self.endDateDailyString = ko.observable("");

            self.startDateMothlyString = ko.observable("");
            self.endDateMothlyString = ko.observable("");

            self.startDateYearlyString = ko.observable("");
            self.endDateYearlyString = ko.observable("");

            self.dateValue = ko.observable({});
            self.monthValue = ko.observable({});
            self.yearValue = ko.observable({});

            self.startDateDailyString.subscribe(function(value) {
                self.dateValue().startDate = value;
                self.dateValue.valueHasMutated();
            });
            self.endDateDailyString.subscribe(function(value) {
                self.dateValue().endDate = value;
                self.dateValue.valueHasMutated();
            });

            self.startDateMothlyString.subscribe(function(value) {
                self.monthValue().startDate = value;
                self.monthValue.valueHasMutated();
            });
            self.endDateMothlyString.subscribe(function(value) {
                self.monthValue().endDate = value;
                self.monthValue.valueHasMutated();
            });

            self.startDateYearlyString.subscribe(function(value) {
                self.yearValue().startDate = value;
                self.yearValue.valueHasMutated();
            });
            self.endDateYearlyString.subscribe(function(value) {
                self.yearValue().endDate = value;
                self.yearValue.valueHasMutated();
            });

            //B7_1
            self.saveBeforDeleteOption = ko.observableArray([
                new model.ItemModel(model.SAVE_BEFOR_DELETE_ATR.YES, getText('CMF005_35')),
                new model.ItemModel(model.SAVE_BEFOR_DELETE_ATR.NO, getText('CMF005_36'))
            ]);

            //B8_1
            self.passwordForCompressFile = ko.observable("");
            self.confirmPasswordForCompressFile = ko.observable("");

            //B9_2
            self.supplementExplanation = ko.observable("");

            //D
            //referenceDate init toDay
            self.referenceDate = moment.utc().format("YYYY/MM/DD");

            self.systemType = ko.observable(1);
            self.initEmployeeList = ko.observableArray([]);
            self.employeeList = ko.observableArray([]);
            self.employeeDeletionList = ko.observableArray([]);
            self.categoryDeletionList = ko.observableArray([]);
            self.selectedEmployeeCode = ko.observableArray([]);
            self.alreadySettingPersonal = ko.observableArray([]);
            self.itemTitleAtr = ko.observableArray([
                { value: 0, titleAtrName: resource.getText('CMF005_51') },
                { value: 1, titleAtrName: resource.getText('CMF005_52') }]);
            self.selectedTitleAtr = ko.observable(0);
            self.selectedTitleAtr.subscribe(function(value) {
                if (value == 1) {
                    self.applyKCP005ContentSearch(self.initEmployeeList());
                }
                else {
                    self.applyKCP005ContentSearch([]);
                }
            });
            self.initComponentCCG001();
            self.initComponnentKCP005();

            //E   
            this.columnEmployees = ko.observableArray([
                { headerText: getText('CMF005_56'), key: 'code', width: 150 },
                { headerText: getText('CMF005_57'), key: 'name', width: 250 }
            ]);
            self.delId = ko.observable("");

        }

        /**
        * start page data 
        */
        public startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();

            dfd.resolve(self);
            return dfd.promise();
        }

        /**
         * Setting value default  screen B
         */
        setDefault() {
            var self = this;
            //set B3_1 "ON"
            self.rdSelected = ko.observable(1);
            
            //B6_2_2
            self.dateValue = ko.observable({
                startDate: moment.utc().subtract(1, "M").add(1, "d").format("YYYY/MM/DD"),
                endDate: moment.utc().format("YYYY/MM/DD")
            });
            self.monthValue = ko.observable({ startDate: moment.utc().subtract(1, "M").add(1, "d").format("YYYY/MM"), endDate: moment.utc().subtract(1, "M").add(1, "d").format("YYYY/MM") });
            self.yearValue = ko.observable({ startDate:  moment.utc().subtract(1, "M").add(1, "d").format("YYYY"), endDate: moment.utc().subtract(1, "M").add(1, "d").format("YYYY") });

            //B7_2_1
            self.isSaveBeforeDeleteFlg = ko.observable(model.SAVE_BEFOR_DELETE_ATR.YES);
            //B8_2_1
            self.isExistCompressPasswordFlg = ko.observable(true);
            self.passwordConstraint = ko.observable("");
            self.isDelete = ko.observable(self.isSaveBeforeDeleteFlg() && self.isExistCompressPasswordFlg());
            /**
            * Clear validate
             */
             self.isExistCompressPasswordFlg.subscribe(function(value) {
                 if (value) {
                     self.passwordConstraint("PasswordCompressFile");
                     $(".passwordInput").trigger("validate");
                 } else {
                     nts.uk.util.value.reset($("#B8_2_2"), $("#B8_2_2").val());
                     nts.uk.util.value.reset($("#B8_3_2"), $("#B8_3_2").val());
                     self.passwordConstraint("");
                     $('.passwordInput').ntsError('clear');
                 }
            });
            
             self.dateValue.subscribe(function(value) {
                 nts.uk.ui.errors.clearAll();
                 $(".validate_form .ntsDatepicker").trigger("validate");
             });

             self.monthValue.subscribe(function(value) {
                 nts.uk.ui.errors.clearAll();
                 $(".validate_form .ntsDatepicker").trigger("validate");
             });

             self.yearValue.subscribe(function(value) {
                 nts.uk.ui.errors.clearAll();
                 $(".validate_form .ntsDatepicker").trigger("validate");
             }); 
            
             self.isSaveBeforeDeleteFlg.subscribe(function(value) {
                 if (value == 0) {
                     $("#B8_3_2").ntsError('clear');
                     $('#B8_2_2').ntsError('clear');
                 }
                 self.isDelete((value == 1 ? true : false) && self.isExistCompressPasswordFlg());

             });
            self.isExistCompressPasswordFlg.subscribe(function(value) {
                if (value == false) {
                    $("#B8_3_2").ntsError('clear');
                    $('#B8_2_2').ntsError('clear');
                }
                self.isDelete(value && self.isSaveBeforeDeleteFlg());
             });
        }

        /**
         *Get status display button select category 
         */
        isEnableBtnOpenC() {
            var self = this;
            if (self.rdSelected() == 1) {
                return true;
            } else {
                return false;
            }
        }

        /**
         *Open screen C 
         */
        openScreenC() {
            var self = this;
            setShared("CMF005CParams_ListCategory", self.listDataCategory());
            setShared("CMF005CParams_SystemType", self.systemTypeCbb);
            nts.uk.ui.errors.clearAll();
            modal("/view/cmf/005/c/index.xhtml").onClosed(() => {

                let categoryC = getShared('CMF005COutput_ListCategoryChose');
                let systemTypeC = getShared('CMF005COutput_SystemTypeChose');

                if (systemTypeC) {
                    self.systemTypeCbb = systemTypeC;
                    self.systemType(systemTypeC.code);
                    $("#B5_2_2").html(systemTypeC.name);
                }
                if (categoryC && (categoryC.length > 0)) {
                    self.listDataCategory.removeAll();
                    self.requiredDate(false);
                    self.requiredMonth(false);
                    self.requiredYear(false);
                    for (let i = 0; i < categoryC.length; i++) {
                        self.listDataCategory.push(categoryC[i]);

                        if (!self.requiredMonth() && categoryC[i].timeStore == model.TIME_STORE.MONTHLY) {
                            self.requiredMonth(true);
                        }

                        if (!self.requiredYear() && categoryC[i].timeStore == model.TIME_STORE.ANNUAL) {
                            self.requiredYear(true);
                        }

                        if (!self.requiredDate() && categoryC[i].timeStore == model.TIME_STORE.DAILY) {
                            self.requiredDate(true);
                        }
                    }

                }

                $("#B4_2").focus();
            });
        }

        /**
         *Open screen D 
         */
        private nextScreenD(): void {
            let self = this;
            if (self.validateForm()) {
                if (self.isExistCompressPasswordFlg()) {
                    if (self.passwordForCompressFile() == self.confirmPasswordForCompressFile()) {
                        if (self.listDataCategory().length > 0) {
                            self.nextFromBToD();
                        } else {
                            alertError({ messageId: 'Msg_471' });
                        }
                    } else {
                        alertError({ messageId: 'Msg_566' });
                    }
                } else {
                    if (self.listDataCategory().length > 0) {
                        self.nextFromBToD();
                    } else {
                        alertError({ messageId: 'Msg_471' });
                    }
                }
            }
        }

        /**
         *Check validate client
         */
        private validateForm() : boolean {
            $(".validate_form").trigger("validate");
            $(".validate_form .ntsDatepicker").trigger("validate");
            if (nts.uk.ui.errors.hasError()) {
                return false;
            }
            return true;
        };

        /**
         * function next wizard by on click button 
         */
        private nextFromBToD() {
            let self = this;
            self.next();
            $('#D4_2').focus();
            self.scrollBottomToLeft();
        }

        /**
        * function next wizard by on click button 
        */
        private next() {
            $('#ex_accept_wizard').ntsWizard("next");
        }
        /**
         * function previous wizard by on click button 
         */
        private previous() {
            $('#ex_accept_wizard').ntsWizard("prev");
        }

        /**
        * Validate  DatePicker
        * return : boolean (true: Valid ,false: Invalid)
        */
        validateDatePicker() {
            let self = this;
            if (self.requiredDate()) {
                if (self.dateValue().startDate && self.dateValue().endDate) {
                    if (self.dateValue().startDate > self.dateValue().endDate) {
                       alertError({ messageId: 'Msg_465' });
                        return false;
                    }
                } else {
                    alertError({ messageId: 'Msg_463' });
                    return false;
                }
            }
            if (self.requiredMonth()) {
                if (self.monthValue().startDate && self.monthValue().endDate) {
                    if (self.monthValue().startDate > self.monthValue().endDate) {
                        alertError({ messageId: 'Msg_465' });
                        return false;
                    }
                } else {
                    alertError({ messageId: 'Msg_463' });
                    return false;
                }
            }
            if (self.requiredYear()) {
                if (self.yearValue().startDate && self.yearValue().endDate) {
                    if (self.yearValue().startDate > self.yearValue().endDate) {
                        alertError({ messageId: 'Msg_465' });
                        return false;
                    }
                } else {
                    alertError({ messageId: 'Msg_463' });
                    return false;
                }
            }

            return true;
        }

        /**
        * Validate Compress Password 
        * return : boolean (true: Valid ,false: Invalid)
        */
        checkPass() {
            let self = this;

            //check pass on
            if (self.isExistCompressPasswordFlg()) {
                // check pass empty
                if (self.passwordForCompressFile() && self.confirmPasswordForCompressFile()) {
                    // compare pass
                    if (self.passwordForCompressFile() == self.confirmPasswordForCompressFile()) {
                        return true;
                    } else {
                        alertError({ messageId: 'Msg_566' });
                        return false;
                    }
                } else {
                    alertError({ messageId: 'Msg_463' });
                    return false;
                }
            } else {
                return true;
            }
        }

        /**
         * Open screen A
         */
        backScreenA() {
            nts.uk.request.jump("/view/cmf/005/a/index.xhtml");
        }


        //load D screen
        initComponentCCG001() {
            let self = this;
            // Set component option
            self.ccg001ComponentOption = {
                /** Common properties */
                showEmployeeSelection: false,
                systemType: self.systemType(),
                showQuickSearchTab: true,
                showAdvancedSearchTab: true,
                showBaseDate: true,
                showClosure: false,
                showAllClosure: false,
                showPeriod: false,
                periodFormatYM: false,

                /** Quick search tab options */
                showAllReferableEmployee: true,
                showOnlyMe: true,
                showSameWorkplace: true,
                showSameWorkplaceAndChild: true,

                /** Advanced search properties */
                showEmployment: true,
                showWorkplace: true,
                showClassification: true,
                showJobTitle: true,
                showWorktype: true,
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
                    self.initEmployeeList(data.listEmployee);
                    self.applyKCP005ContentSearch(data.listEmployee);
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
                selectedCode: self.selectedEmployeeCode,
                isDialog: true,
                isShowNoSelectRow: false,
                alreadySettingList: self.alreadySettingPersonal,
                isShowWorkPlaceName: true,
                isShowSelectAllButton: true,
                maxWidth: 550,
                maxRows: 15,
                isSelectAllAfterReload: true
            };
        }

        /**
        * back to B
        */
        private previousB(): void {
            var self = this;
            self.previous();
            if (self.rdSelected() == 1) {
                $("#B3_5").focus();
            }
        }
        
        /**
         * back to D
         */
        private previousD(): void {
            var self = this;
            self.previous();
            $('#D4_2').focus();
        }

        /**
        * validation D form
        */
        private validateD(): boolean {
            var self = this;
            if ((self.selectedTitleAtr() == 1 && self.selectedEmployeeCode() && self.selectedEmployeeCode().length > 0)
                || (self.selectedTitleAtr() == 0 )) {
                return true;
            } else {
                nts.uk.ui.dialog.error({ messageId: "Msg_498", messageParams: ["X", "Y"] });
                return false;
            }
        }

        /**
         * update the list of selected employees
         */
        setEmployeeDeletionList() {
            var self = this;
            self.employeeDeletionList.removeAll();
            if (self.selectedTitleAtr() == 1) {
                let empCodeLength = self.selectedEmployeeCode().length;
                let empListLength = self.employeeList().length;
                for (var i = 0; i < empCodeLength; i++) {
                    for (var j = 0; j < empListLength; j++) {
                        let employee = self.employeeList()[j];
                        if (employee.code == self.selectedEmployeeCode()[i]) {
                            self.employeeDeletionList.push(
                                new EmployeeDeletion(employee.code, employee.name, employee.id,
                                    employee.code, employee.name));
                        }
                    }
                }
                self.employeeDeletionList(_.orderBy(self.employeeDeletionList(), ['code'], ['asc']));
            }
        }

        /**
         * update the list of selected categories
         */
        setCategoryDeletionList() {
            var self = this;
            self.categoryDeletionList.removeAll();
            for (var i = 0; i < self.listDataCategory().length; i++) {
                let category = self.listDataCategory()[i];
                self.categoryDeletionList.push(new CategoryDeletion(category.categoryId));
            }
        }

        /**
         * next to E screen
         */
        private nextFromDToE(): void {
            var self = this;
            if (self.validateD()) {
                self.setEmployeeDeletionList();
                self.setCategoryDeletionList();
                self.initE();
                self.next();
                $("#E20_2").focus();
            }
        }

        /**
         * initial E screen
         */
        initE() {
            var self = this;
            $("#E4_2").html(self.deleteSetName());
            $("#E5_2").html(self.supplementExplanation());
            $("#E6_2_2").html(self.systemTypeCbb.name);
        }

        private gotoscreenF(): void {
            let self = this;
            let params = {};
                params.delId = self.delId();
                params.deleteSetName = self.deleteSetName();
                params.dateValue = self.dateValue();
                params.monthValue = self.monthValue();
                params.yearValue = self.yearValue();
                params.saveBeforDelete = self.isSaveBeforeDeleteFlg();

            setShared("CMF005_E_PARAMS", params);
            modal("/view/cmf/005/f/index.xhtml").onClosed(() => {
                nts.uk.request.jump("/view/cmf/005/a/index.xhtml");
            });
        }

        private saveManualSetting(): void {
            let self = this;
            let manualSetting = new ManualSettingModal(self.deleteSetName(), self.supplementExplanation(), self.systemType(),
                moment.utc(self.referenceDate, 'YYYY/MM/DD'), moment.utc().toISOString(),
                moment.utc(self.dateValue().startDate, 'YYYY/MM/DD'), moment.utc(self.dateValue().endDate, 'YYYY/MM/DD'),
                moment.utc(self.monthValue().startDate, 'YYYY/MM'), moment.utc(self.monthValue().endDate, 'YYYY/MM'),
                self.yearValue().startDate, self.yearValue().endDate,
                Number(self.isSaveBeforeDeleteFlg()), Number(self.isExistCompressPasswordFlg()), self.passwordForCompressFile(),
                Number(self.selectedTitleAtr()), self.employeeDeletionList(), self.categoryDeletionList());
            
            service.addManualSetDel(manualSetting).done(function(data: any) {
                self.delId(data);
                self.gotoscreenF();
            }).fail(function(error) {
                alertError(error);

            }).always(() => {
            });
        }
        
        /**
         * 
         */
        private scrollBottomToLeft() : void {
            $("#contents-area").scrollLeft(0);
        }
    }

    function timeStore(value, row) {
        if (value == model.TIME_STORE.MONTHLY) {
            return getText('Enum_TimeStore_MONTHLY');
        } else if (value == model.TIME_STORE.ANNUAL) {
            return getText('Enum_TimeStore_ANNUAL');
        } else if (value == model.TIME_STORE.FULL_TIME) {
            return getText('Enum_TimeStore_FULL_TIME');
        } else if (value == model.TIME_STORE.DAILY) {
            return getText('Enum_TimeStore_DAILY');
        }
    }

    function storageRangeSaved(value, row) {
        if (value == model.STORAGE_RANGE_SAVE.EARCH_EMP) {
            return getText('Enum_StorageRangeSaved_EARCH_EMP');
        } else if (value == model.STORAGE_RANGE_SAVE.ALL_EMP) {
            return getText('Enum_StorageRangeSaved_ALL_EMP');
        }
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

    export class ListType {
        static EMPLOYMENT = 1;
        static Classification = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }

    export class SelectType {
        static SELECT_BY_SELECTED_CODE = 1;
        static SELECT_ALL = 2;
        static SELECT_FIRST_ITEM = 3;
        static NO_SELECT = 4;
    }

    export interface UnitAlreadySettingModel {
        code: string;
        isAlreadySetting: boolean;
    }

    export class EmployeeDeletion {
        code: string;
        name: string;
        employeeId: string;
        employeeCode:string;
        businessName: string;

        constructor(code: string, name: string, employeeId: string, employeeCode:string, businessName: string) {
            this.code = code;
            this.name = name;
            this.employeeId = employeeId;
            this.employeeCode = employeeCode;
            this.businessName = businessName;
        }
    }


    export class ManualSettingModal {
        delName: string;
        suppleExplanation: string;
        systemType: number;
        referenceDate: string;
        executionDateAndTime: string;
        dayStartDate: string;
        dayEndDate: string;
        monthStartDate: string;
        monthEndDate: string;
        startYear: number;
        endYear: number;
        isSaveBeforeDeleteFlg: number;
        isExistCompressPasswordFlg: number;
        passwordForCompressFile: string;
        haveEmployeeSpecifiedFlg: number;
        employees: Array<EmployeeDeletion>;
        categories: Array<CategoryDeletion>;

        constructor(delName: string, suppleExplanation: string, systemType: number, referenceDate: string,
            executionDateAndTime: string, dayStartDate: string, dayEndDate: string,
            monthStartDate: string, monthEndDate: string,
            startYear: number, endYear: number, isSaveBeforeDeleteFlg: number, isExistCompressPasswordFlg: number,
            passwordForCompressFile: string, haveEmployeeSpecifiedFlg: number, employees: Array<EmployeeDeletion>,
            categories: Array<CategoryDeletion>) {
            this.delName = delName;
            this.suppleExplanation = suppleExplanation;
            this.systemType = systemType;
            this.referenceDate = referenceDate;
            this.executionDateAndTime = executionDateAndTime;
            this.dayStartDate = dayStartDate;
            this.dayEndDate = dayEndDate;
            this.monthStartDate = monthStartDate;
            this.monthEndDate = monthEndDate;
            this.startYear = startYear;
            this.endYear = endYear;
            this.isSaveBeforeDeleteFlg = isSaveBeforeDeleteFlg;
            this.isExistCompressPasswordFlg = isExistCompressPasswordFlg;
            this.passwordForCompressFile = passwordForCompressFile;
            this.haveEmployeeSpecifiedFlg = haveEmployeeSpecifiedFlg;
            this.employees = employees;
            this.categories = categories;
        }
    }

    export class CategoryDeletion {
        categoryId: string;
        periodDeletion: string;
        constructor(categoryId: string) {
            this.categoryId = categoryId;
            this.periodDeletion = null;
        }
        
        constructor(categoryId: string,periodDeletion:string) {
            this.categoryId = categoryId;
            this.periodDeletion = periodDeletion;
        }
    }
}