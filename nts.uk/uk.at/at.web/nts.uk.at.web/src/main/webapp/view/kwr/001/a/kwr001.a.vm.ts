module nts.uk.at.view.kwr001.a {
    import ComponentOption = kcp.share.list.ComponentOption;
    import service = nts.uk.at.view.kwr001.a.service;
    import blockUI = nts.uk.ui.block;
    
    export module viewmodel {
        export class ScreenModel {
            dataOutputType: KnockoutObservableArray<any>;
            
            // datepicker A1_6
            requiredDatePicker: KnockoutObservable<boolean>; 
            enableDatePicker: KnockoutObservable<boolean>; 
            datepickerValue: KnockoutObservable<any>;
            startDatepicker: KnockoutObservable<string>;
            endDatepicker: KnockoutObservable<string>; 
            // datepicker A1_6
            
            // switch button A6_2
            selectedDataOutputType: KnockoutObservable<number>;
            
            // dropdownlist A7_3
            itemListCodeTemplate: KnockoutObservableArray<ItemModel>;
            selectedCodeA7_3: KnockoutObservable<string>;
            
            // dropdownlist A9_2
            itemListTypePageBrake: KnockoutObservableArray<ItemModel>;
            selectedCodeA9_2: KnockoutObservable<number>;
            
            // radio button group A13_1
            itemListConditionSet: KnockoutObservableArray<any>;
            selectedCodeA13_1: KnockoutObservable<number>;
            
            // start variable of CCG001
            ccg001ComponentOption: GroupOption;
            // end variable of CCG001
            
            // start declare KCP005
            listComponentOption: any;
            multiSelectedCode: KnockoutObservableArray<string>;
            isShowAlreadySet: KnockoutObservable<boolean>;
            alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
            isDialog: KnockoutObservable<boolean>;
            isShowNoSelectRow: KnockoutObservable<boolean>;
            isMultiSelect: KnockoutObservable<boolean>;
            isShowWorkPlaceName: KnockoutObservable<boolean>;
            isShowSelectAllButton: KnockoutObservable<boolean>;
            employeeList: KnockoutObservableArray<UnitModel>;
            // end KCP005
            
            requiredTemp: KnockoutObservable<boolean>;
            enableTemp: KnockoutObservable<boolean>;
            
            checkedA10_2: KnockoutObservable<boolean>;
            checkedA10_3: KnockoutObservable<boolean>;
            checkedA10_4: KnockoutObservable<boolean>;
            checkedA10_5: KnockoutObservable<boolean>;
            checkedA10_6: KnockoutObservable<boolean>;
            checkedA10_7: KnockoutObservable<boolean>;
            
            checkedA10_10: KnockoutObservable<boolean>;
            checkedA10_11: KnockoutObservable<boolean>;
            checkedA10_12: KnockoutObservable<boolean>;
            checkedA10_13: KnockoutObservable<boolean>;
            checkedA10_14: KnockoutObservable<boolean>;
            checkedA10_15: KnockoutObservable<boolean>;
            checkedA10_16: KnockoutObservable<boolean>;
            checkedA10_17: KnockoutObservable<boolean>;
            checkedA10_18: KnockoutObservable<boolean>;
            
            enableByCumulativeWkp: KnockoutObservable<boolean>;
            enableByOutputFormat: KnockoutObservable<boolean>;
            enableBtnConfigure: KnockoutObservable<boolean>;
            enableConfigErrCode: KnockoutObservable<boolean>;
            
            constructor() {
                let self = this;
                
                self.enableConfigErrCode = ko.observable(true);
                self.enableByOutputFormat = ko.observable(true);
                self.enableBtnConfigure = ko.observable(true);
                self.requiredTemp = ko.observable(true);
                self.enableTemp = ko.observable(true);;
                
                self.checkedA10_2 = ko.observable(false);
                self.checkedA10_3 = ko.observable(false);
                self.checkedA10_4 = ko.observable(false);
                self.checkedA10_5 = ko.observable(false);
                self.checkedA10_6 = ko.observable(false);
                self.checkedA10_7 = ko.observable(false);
                
                self.checkedA10_10 = ko.observable(false);
                self.checkedA10_11 = ko.observable(false);
                self.checkedA10_12 = ko.observable(false);
                self.checkedA10_13 = ko.observable(false);
                self.checkedA10_14 = ko.observable(false);
                self.checkedA10_15 = ko.observable(false);
                self.checkedA10_16 = ko.observable(false);
                self.checkedA10_17 = ko.observable(false);
                self.checkedA10_18 = ko.observable(false);
                
                self.enableByCumulativeWkp = ko.observable(true);
                
                self.checkedA10_7.subscribe(function(value) {
                    if (value == true) {
                        self.enableByCumulativeWkp(true);        
                    } else {
                        self.enableByCumulativeWkp(false);    
                    }
                })
                self.checkedA10_7.valueHasMutated();
                
                // start set variable for datepicker A1_6
                self.enableDatePicker = ko.observable(true);
                self.requiredDatePicker = ko.observable(true);
                
                self.startDatepicker = ko.observable("");
                self.endDatepicker = ko.observable("");
                self.datepickerValue = ko.observable({});
                
                self.startDatepicker.subscribe(function(value){
                    self.datepickerValue().startDate = value;
                    self.datepickerValue.valueHasMutated();        
                });
                
                self.endDatepicker.subscribe(function(value){
                    self.datepickerValue().endDate = value;   
                    self.datepickerValue.valueHasMutated();      
                });
                // end set variable for datepicker A1_6
                
                // start set variable for CCG001
                self.ccg001ComponentOption = {
                    /** Common properties */
                    systemType: 2,
                    showEmployeeSelection: false,
                    showQuickSearchTab: true,
                    showAdvancedSearchTab: true,
                    showBaseDate: true,
                    showClosure: false,
                    showAllClosure: false,
                    showPeriod: false,
                    periodFormatYM: false,
                    
                    /** Required parameter */
                    baseDate: moment().toISOString(),
                    periodStartDate: moment().toISOString(),
                    periodEndDate: moment().toISOString(),
                    inService: true,
                    leaveOfAbsence: true,
                    closed: true,
                    retirement: true,
                    
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
                    
                    /**
                    * Self-defined function: Return data from CCG001
                    * @param: data: the data return from CCG001
                    */
                    returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                        self.employeeList.removeAll();
                        var employeeSearchs: UnitModel[] = [];
                        _.forEach(data.listEmployee, function(value) {
                            var employee: UnitModel = {
                                id: value.employeeId,
                                code: value.employeeCode,
                                name: value.employeeName,
                            };
                            employeeSearchs.push(employee);
                        });
                        self.employeeList(employeeSearchs);
                    }
                }
                // end set variable for CCG001
                
                // TODO: hoangdd - goi service lay enum thay cho viec set cung resource
                self.dataOutputType = ko.observableArray([
                    { code: '0', name: nts.uk.resource.getText("KWR001_10") },
                    { code: '1', name: nts.uk.resource.getText("KWR001_11") }
                ]);
                self.selectedDataOutputType = ko.observable(0);
                self.selectedDataOutputType.subscribe(function(value) {
                    if (value == 0) {
                        self.enableByOutputFormat(true);                        
                    } else {
                        self.enableByOutputFormat(false);    
                    }
                })
                self.selectedDataOutputType.valueHasMutated();

                // TODO: hoangdd - lay du lieu tu service
                self.itemListCodeTemplate = ko.observableArray([]);
                
                // TODO: hoangdd - lay du lieu tu service
                self.itemListTypePageBrake = ko.observableArray([
                    new ItemModel('0', 'なし'),
                    new ItemModel('1', '社員'),
                    new ItemModel('2', '職場')
                ]);
                
                self.selectedCodeA9_2 = ko.observable(1);
                
                self.selectedCodeA7_3 = ko.observable(''); 
                
                // TODO: hoangdd - lay du lieu tu service
                self.itemListConditionSet = ko.observableArray([
                    new BoxModel(0, nts.uk.resource.getText("KWR001_38")),
                    new BoxModel(1, nts.uk.resource.getText("KWR001_39"))
                ]);
                
                self.selectedCodeA13_1 = ko.observable(0);
                self.selectedCodeA13_1.subscribe(function(value) {
                    if (value==1) {
                        self.enableConfigErrCode(true);
                    } else {
                        self.enableConfigErrCode(false);
                    }
                })
                self.selectedCodeA13_1.valueHasMutated();
                
                // start define KCP005
                self.multiSelectedCode = ko.observableArray([]);
                self.isShowAlreadySet = ko.observable(false);
                self.alreadySettingList = ko.observableArray([]);
                self.isDialog = ko.observable(true);
                self.isShowNoSelectRow = ko.observable(false);
                self.isMultiSelect = ko.observable(true);
                self.isShowWorkPlaceName = ko.observable(false);
                self.isShowSelectAllButton = ko.observable(false);
                this.employeeList = ko.observableArray<UnitModel>([]);
                self.listComponentOption = {
                    isShowAlreadySet: self.isShowAlreadySet(),
                    isMultiSelect: self.isMultiSelect(),
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.SELECT_BY_SELECTED_CODE,
                    selectedCode: self.multiSelectedCode,
                    isDialog: self.isDialog(),
                    isShowNoSelectRow: self.isShowNoSelectRow(),
                    alreadySettingList: self.alreadySettingList,
                    isShowWorkPlaceName: self.isShowWorkPlaceName(),
                    isShowSelectAllButton: self.isShowSelectAllButton(),
                    tabindex: 5,
                    maxRows: 17
                };
                // end define KCP005
            }
            
            public startPage(): JQueryPromise<void>  {
                var dfd = $.Deferred<void>();
                var self = this;
                
                // TODO - hoangdd: goi service lay domain cho A7_6. gio dang fix cung
                self.enableBtnConfigure(true);
                
                $.when(self.getDataCharateristic()).done(function(dataCharacteristic: any) {
                    let isExist = !(_.isUndefined(dataCharacteristic) || _.isNull(dataCharacteristic));
                    self.getDataStartPageService(isExist).done(function(dataService: any) {
                        
                        self.itemListCodeTemplate(dataService.lstOutputItemDailyWorkSchedule);
                        
                        switch(dataService.strReturn) {
                            // return screen A, show data from characteristic
                            case SHOW_CHARACTERISTIC:
                                self.renewDataPage();
                                break;
                            // return screen A, don't have data characteristic
                            case STRING_EMPTY:
                                break;
                            case OPEN_SCREEN_C:
                                self.openScreenC();
                                break;
                            default:
                                break;
                        }
                    }).fail(function(error) {
                       nts.uk.ui.dialog.alertError(error);     
                    }).always(function() {
                        dfd.resolve();    
                    });
                });
                
                return dfd.promise();
            }
            
            private renewDataPage(): void {
                let self = this;
                let companyId: string = __viewContext.user.companyId;
                let userId: string = __viewContext.user.employeeId;
                service.restoreCharacteristic(companyId, userId).done(function(data: any) {
                    let workScheduleOutputCondition: WorkScheduleOutputConditionDto = data;
                    self.selectedDataOutputType(workScheduleOutputCondition.outputType);
                    self.selectedCodeA7_3(workScheduleOutputCondition.code);
                    self.selectedCodeA9_2(workScheduleOutputCondition.pageBreakIndicator);
                    self.checkedA10_2(workScheduleOutputCondition.settingDetailTotalOuput.details);
                    self.checkedA10_3(workScheduleOutputCondition.settingDetailTotalOuput.personalTotal);
                    self.checkedA10_4(workScheduleOutputCondition.settingDetailTotalOuput.workplaceTotal);
                    self.checkedA10_5(workScheduleOutputCondition.settingDetailTotalOuput.totalNumberDay);
                    self.checkedA10_6(workScheduleOutputCondition.settingDetailTotalOuput.grossTotal);
                    self.checkedA10_7(workScheduleOutputCondition.settingDetailTotalOuput.cumulativeWorkplace);
                    if (workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal) {
                        self.checkedA10_10(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.firstLevel);
                        self.checkedA10_11(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.secondLevel);
                        self.checkedA10_12(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.thirdLevel);
                        self.checkedA10_13(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.fourthLevel);
                        self.checkedA10_14(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.fifthLevel);
                        self.checkedA10_15(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.sixthLevel);
                        self.checkedA10_16(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.seventhLevel);
                        self.checkedA10_17(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.eighthLevel);
                        self.checkedA10_18(workScheduleOutputCondition.settingDetailTotalOuput.workplaceHierarchyTotal.ninthLevel);
                    }
                    self.selectedCodeA13_1(workScheduleOutputCondition.conditionSetting);
                })
            }
            
            // get data from service
            private getDataStartPageService(isExist: boolean): JQueryPromise<any> {
                var dfd = $.Deferred<any>();
                let self = this;
                service.getDataStartPage(isExist).done(function(data: any) {
                    self.startDatepicker(data.startDate);
                    self.endDatepicker(data.endDate);
                    self.ccg001ComponentOption.baseDate = moment.utc(self.endDatepicker(), DATE_FORMAT_YYYY_MM_DD).toISOString();
                    dfd.resolve(data);
                })                
                return dfd.promise();
            }
            
            // run after create success html 
            public executeBindingComponent(): void {
                let self = this;
                
                // start component CCG001
                // start component KCP005
                $.when($('#ccgcomponent').ntsGroupComponent(self.ccg001ComponentOption), 
                            $('#component-items-list').ntsListComponent(self.listComponentOption)).done(function() {
                    $('.ntsStartDatePicker').focus();
                    blockUI.clear();
                });
            }
            
            // get data from characteristic
            private getDataCharateristic(): JQueryPromise<any> {
                var dfd = $.Deferred<any>();
                let self = this;
                
                let companyId: string = __viewContext.user.companyId;
                let userId: string = __viewContext.user.employeeId;

                $.when(service.restoreCharacteristic(companyId, userId)).done(function(data: WorkScheduleOutputConditionDto) {
                if (_.isUndefined(data)) {
                    // TODO - hoangdd: fake data default according spec
                    let totalWorkplaceHierachy = new TotalWorkplaceHierachy(false, false, false, false, false, false, false, false, false);
                    let workScheduleSettingTotalOutput = new WorkScheduleSettingTotalOutput(false, false, false, false, false, false, totalWorkplaceHierachy);
                    let workScheduleOutputCondition = new WorkScheduleOutputConditionDto(companyId, userId, 0, '', 0, workScheduleSettingTotalOutput, 0, []);
                    service.saveCharacteristic(companyId, userId, workScheduleOutputCondition);    
                }
                
                    dfd.resolve(data);
                });
                
                return dfd.promise();
            }
            
            openScreenB (): void {
                let self = this;
                let companyId: string = __viewContext.user.companyId;
                let userId: string = __viewContext.user.employeeId;
                service.restoreCharacteristic(companyId, userId).done(function(data: any) {
                    let workScheduleOutputCondition: WorkScheduleOutputConditionDto = data;
                    nts.uk.ui.windows.setShared('KWR001_B_errorAlarmCode', _.isUndefined(data) ? [] : workScheduleOutputCondition.errorAlarmCode, true);
                    nts.uk.ui.windows.sub.modal('/view/kwr/001/b/index.xhtml').onClosed(function(): any {
                        workScheduleOutputCondition.errorAlarmCode = nts.uk.ui.windows.getShared('KWR001_B_errorAlarmCode');
                        service.saveCharacteristic(companyId, userId, workScheduleOutputCondition);
                    });  
                })
            }
            openScreenC (): void {
                let self = this;
                let codeChoose = self.getCodeFromListCode(self.selectedCodeA7_3(), self.itemListCodeTemplate());
                
                nts.uk.ui.windows.setShared('KWR001_C', codeChoose, true);
                nts.uk.ui.windows.sub.modal('/view/kwr/001/c/index.xhtml').onClosed(function(): any {
                    nts.uk.ui.windows.getShared('KWR001_C');
                });
            }
            
            private getCodeFromListCode(pos: string, lstCode: ItemModel[]): string {
                let self = this;
                let codeChoose = _.find(lstCode, function(o) { 
                    return pos == o.code; 
                });
                if (_.isUndefined(codeChoose)) {
                    return null;
                }
                return codeChoose.code;
            }
            
            /**
             * find employee id in selected
             */
            private findEmployeeIdByCode(employeeCode: string): string {
                var self = this;
                var employeeId = '';
                for (var employee of self.employeeList()) {
                    if (employee.id === employeeCode) {
                        employeeId = employee.id;
                    }
                }
                return employeeId;
            }
            
            /**
             * find employee id in selected
             */
            private findEmployeeIdsByCodes(employeeCodes: UnitModel[]): string[] {
                var self = this;
                var employeeIds: string[] = [];
                for (var employeeCode of employeeCodes) {
                    employeeIds.push(self.findEmployeeIdByCode(employeeCode.id));
                }
                return employeeIds;
            }
            
            processExcel(): void {
                let self = this;
                if (self.validateMinimumOneChecked()) {
                    self.saveWorkScheduleOutputCondition();
                    let companyId: string = __viewContext.user.companyId;
                    let userId: string = __viewContext.user.employeeId;
                    service.restoreCharacteristic(companyId, userId).done(function(data: WorkScheduleOutputConditionDto) {
                        var user: any = __viewContext.user;
                        var dto: WorkScheduleOutputQueryDto = {
                            lstEmployeeId: self.findEmployeeIdsByCodes(self.employeeList()),
                            startDate: self.toDate(self.startDatepicker()),
                            endDate: self.toDate(self.startDatepicker()),
                            fileType: 0,
                            condition: data
                        };
                        service.exportExcel(dto);
                    });
                }
                
            }
            
            private processPdf(): void {
                let self = this;
                if (self.validateMinimumOneChecked()) {
                    self.saveWorkScheduleOutputCondition();
                    let companyId: string = __viewContext.user.companyId;
                    let userId: string = __viewContext.user.employeeId;
                    service.restoreCharacteristic(companyId, userId).done(function(data: WorkScheduleOutputConditionDto) {
                        var user: any = __viewContext.user;
                        var dto: WorkScheduleOutputQueryDto = {
                            lstEmployeeId: self.findEmployeeIdsByCodes(self.employeeList()),
                            startDate: self.toDate(self.startDatepicker()),
                            endDate: self.toDate(self.startDatepicker()),
                            fileType: 1,
                            condition: data
                        };
                        service.exportExcel(dto);
                    });
                }
            }
            
            private validateMinimumOneChecked(): boolean {
                let self = this;
                if (!self.checkedA10_2() && !self.checkedA10_3() && !self.checkedA10_4() && !self.checkedA10_5()
                        && !self.checkedA10_6() && self.checkedA10_7() && !self.checkedA10_10() && !self.checkedA10_11()
                        && !self.checkedA10_12() && !self.checkedA10_13() && !self.checkedA10_14() && !self.checkedA10_15() 
                        && !self.checkedA10_16() && !self.checkedA10_17() && !self.checkedA10_18()) {
                    nts.uk.ui.dialog.error({ messageId: "Msg_1141" });
                    return false;
                }
                
                if (!self.checkedA10_2() && !self.checkedA10_3() && !self.checkedA10_4() && !self.checkedA10_5()
                        && !self.checkedA10_6() && !self.checkedA10_7()) {
                    nts.uk.ui.dialog.error({ messageId: "Msg_1141" });
                    return false;
                }
                return true;
            }
            
            private saveWorkScheduleOutputCondition(): void {
                let self = this;
                
                let companyId: string = __viewContext.user.companyId;
                let userId: string = __viewContext.user.employeeId;
                service.restoreCharacteristic(companyId, userId).done(function(data: any) {
                    // return data default
                    if (!self.checkedA10_7()) {
                        self.checkedA10_10(false);
                        self.checkedA10_11(false);
                        self.checkedA10_12(false);
                        self.checkedA10_13(false);
                        self.checkedA10_14(false);
                        self.checkedA10_15(false);
                        self.checkedA10_16(false);
                        self.checkedA10_17(false);
                        self.checkedA10_18(false);
                    }
                    
                    if (self.selectedDataOutputType() == 1) {
                        self.checkedA10_3(false);
                        self.checkedA10_5(false);
                    }
                    
                    let totalWorkplaceHierachy = new TotalWorkplaceHierachy(self.checkedA10_10(), self.checkedA10_11(), 
                                                                            self.checkedA10_12(), self.checkedA10_13(), 
                                                                            self.checkedA10_14(), self.checkedA10_15(), 
                                                                            self.checkedA10_16(), self.checkedA10_17(), 
                                                                            self.checkedA10_18());
                    let workScheduleSettingTotalOutput = new WorkScheduleSettingTotalOutput(self.checkedA10_2(), self.checkedA10_3(), 
                                                                                            self.checkedA10_4(), self.checkedA10_5(), 
                                                                                            self.checkedA10_6(), self.checkedA10_7(), 
                                                                                            totalWorkplaceHierachy);
                    
                    let codeChoose = self.getCodeFromListCode(self.selectedCodeA7_3(), self.itemListCodeTemplate());
                    let errorAlarmCode: any[];
                    
                    errorAlarmCode = data.errorAlarmCode;
                    
                    let workScheduleOutputCondition = new WorkScheduleOutputConditionDto(companyId, userId, self.selectedDataOutputType(), 
                                                                                    codeChoose, self.selectedCodeA9_2(), 
                                                                                    workScheduleSettingTotalOutput, self.selectedCodeA13_1(), errorAlarmCode);
                    service.saveCharacteristic(companyId, userId, workScheduleOutputCondition); 
                })    
            }
            
            private toDate(strDate: string): Date {
                return moment(strDate, 'YYYY/MM/DD').toDate();
            }
        }
        
        const DATE_FORMAT_YYYY_MM_DD = "YYYY/MM/DD";
        const STRING_EMPTY = "";
        const SHOW_CHARACTERISTIC = "SHOW_CHARACTERISTIC";
        const OPEN_SCREEN_C = "Open_ScrC";
        
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        export interface UnitModel {
            id: string;
            code: string;
            name?: string;
            workplaceName?: string;
            isAlreadySetting?: boolean;
        }

        export class SelectType {
            static SELECT_BY_SELECTED_CODE = 1;
            static SELECT_ALL = 2;
            static SELECT_FIRST_ITEM = 3;
            static NO_SELECT = 4;
        }
        
        class ItemModel {
            code: string;
            name: string;
        
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        
        class BoxModel {
            id: number;
            name: string;
            constructor(id, name){
                var self = this;
                self.id = id;
                self.name = name;
            }
        }

        // start CCG001
        export interface GroupOption {
            /** Common properties */
            showEmployeeSelection: boolean; // 検索タイプ
            systemType: number; // システム区分
            showQuickSearchTab: boolean; // クイック検索
            showAdvancedSearchTab: boolean; // 詳細検索
            showBaseDate: boolean; // 基準日利用
            showClosure: boolean; // 就業締め日利用
            showAllClosure: boolean; // 全締め表示
            showPeriod: boolean; // 対象期間利用
            periodFormatYM: boolean; // 対象期間精度
            isInDialog?: boolean;
        
            /** Required parameter */
            baseDate?: string; // 基準日
            periodStartDate?: string; // 対象期間開始日
            periodEndDate?: string; // 対象期間終了日
            inService: boolean; // 在職区分
            leaveOfAbsence: boolean; // 休職区分
            closed: boolean; // 休業区分
            retirement: boolean; // 退職区分
        
            /** Quick search tab options */
            showAllReferableEmployee: boolean; // 参照可能な社員すべて
            showOnlyMe: boolean; // 自分だけ
            showSameWorkplace: boolean; // 同じ職場の社員
            showSameWorkplaceAndChild: boolean; // 同じ職場とその配下の社員
        
            /** Advanced search properties */
            showEmployment: boolean; // 雇用条件
            showWorkplace: boolean; // 職場条件
            showClassification: boolean; // 分類条件
            showJobTitle: boolean; // 職位条件
            showWorktype: boolean; // 勤種条件
            isMutipleCheck: boolean; // 選択モード
        
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
            baseDate: string; // 基準日
            closureId?: number; // 締めID
            periodStart: string; // 対象期間（開始)
            periodEnd: string; // 対象期間（終了）
            listEmployee: Array<EmployeeSearchDto>; // 検索結果
        }
        // end CCG001
        
        class WorkScheduleOutputConditionDto {
            companyId: string;
            userId: string;
            outputType: number;
            code: string;
            pageBreakIndicator: number;
            settingDetailTotalOutput: WorkScheduleSettingTotalOutput;
            conditionSetting: number;
            errorAlarmCode: string[];
            
            constructor(companyId: string, userId: string, outputType: number, code: string, pageBreakIndicator: number,
                            settingDetailTotalOuput: WorkScheduleSettingTotalOutput, conditionSetting: number, errorAlarmCode?: string[]) {
                this.companyId = companyId;
                this.userId = userId;
                this.outputType = outputType;
                this.code = code;
                this.pageBreakIndicator = pageBreakIndicator;
                this.settingDetailTotalOutput = settingDetailTotalOuput;
                this.conditionSetting =  conditionSetting;
                if (errorAlarmCode) {
                    this.errorAlarmCode = errorAlarmCode;        
                }
            }
        }
        
        class WorkScheduleSettingTotalOutput {
            details: boolean;
            personalTotal: boolean;
            workplaceTotal: boolean;
            totalNumberDay: boolean;
            grossTotal: boolean;
            cumulativeWorkplace: boolean;
            
            workplaceHierarchyTotal: TotalWorkplaceHierachy;
            
            constructor( details: boolean, personalTotal: boolean, workplaceTotal: boolean, totalNumberDay: boolean,
                            grossTotal: boolean, cumulativeWorkplace: boolean, workplaceHierarchyTotal?: TotalWorkplaceHierachy) {
                this.details = details;
                this.personalTotal = personalTotal;
                this.workplaceTotal = workplaceTotal;
                this.totalNumberDay = totalNumberDay;
                this.grossTotal = grossTotal;
                this.cumulativeWorkplace = cumulativeWorkplace;
                if (workplaceHierarchyTotal) {
                    this.workplaceHierarchyTotal = workplaceHierarchyTotal;
                }
            }
        }
        
        class WorkScheduleOutputQueryDto {
            lstEmployeeId: Array<string>;
            startDate: Date;
            endDate: Date;
            fileType: number;
            condition: WorkScheduleOutputConditionDto;
            
            constructor(lstEmployeeId: Array<string>, startDate: Date, endDate: Date, condition: WorkScheduleOutputConditionDto, fileType: number) {
                this.lstEmployeeId = lstEmployeeId;
                this.startDate = startDate;
                this.endDate = endDate;
                this.condition = condition;
                this.fileType = fileType;
            }
        }
        
        class TotalWorkplaceHierachy {
            firstLevel: boolean;
            secondLevel: boolean;
            thirdLevel: boolean;
            fourthLevel: boolean;
            fifthLevel: boolean;
            sixthLevel: boolean;
            seventhLevel: boolean;
            eighthLevel: boolean;
            ninthLevel: boolean; 
            
            constructor(firstLevel?: boolean, secondLevel?: boolean, thirdLevel?: boolean, fourthLevel?: boolean,
                            fifthLevel?: boolean, sixthLevel?: boolean, seventhLevel?: boolean, 
                            eighthLevel?: boolean, ninthLevel?: boolean ) {
                if (firstLevel) {
                    this.firstLevel = firstLevel;
                }
                
                if (secondLevel) {
                    this.secondLevel = secondLevel;
                }
                
                if (thirdLevel) {
                    this.thirdLevel = thirdLevel;
                }
                
                if (fourthLevel) {
                    this.fourthLevel = fourthLevel;
                }
                
                if (fifthLevel) {
                    this.fifthLevel = fifthLevel;
                }
                
                if (sixthLevel) {
                    this.sixthLevel = sixthLevel;
                }
                
                if (seventhLevel) {
                    this.seventhLevel = seventhLevel;
                }
                
                if (eighthLevel) {
                    this.eighthLevel = eighthLevel;
                }
                
                if (ninthLevel) {
                    this.ninthLevel = ninthLevel;
                }
            }
        }
    }
}