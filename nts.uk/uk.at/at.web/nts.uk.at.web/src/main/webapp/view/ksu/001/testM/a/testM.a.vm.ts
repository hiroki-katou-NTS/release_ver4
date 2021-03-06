module nts.uk.at.view.testM.a {
    import service = nts.uk.at.view.testM.a.service;
    import blockUI = nts.uk.ui.block;
    import dialog = nts.uk.ui.dialog;
    
    export module viewmodel {
        
        const DATE_FORMAT_YYYY_MM_DD = "YYYY/MM/DD";
        
        export class ScreenModel {
           
            // CCG001
            ccg001ComponentOption: GroupOption;
            
            datepickerValue: KnockoutObservable<any>;
            startDateString: KnockoutObservable<string>;
            endDateString: KnockoutObservable<string>;            
            
            // KCP005 start
            listComponentOption: any;
            selectedIdEmployee: KnockoutObservableArray<string>;
            selectedCodeEmployee: KnockoutObservableArray<string>;
            isShowAlreadySet: KnockoutObservable<boolean>;
            alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
            isDialog: KnockoutObservable<boolean>;
            isShowNoSelectRow: KnockoutObservable<boolean>;
            isMultiSelect: KnockoutObservable<boolean>;
            isShowWorkPlaceName: KnockoutObservable<boolean>;
            isShowSelectAllButton: KnockoutObservable<boolean>;
            employeeList: KnockoutObservableArray<UnitModel>;
            showOptionalColumn: KnockoutObservable<boolean>;
            // KCP005 end
            
            
            lstOutputItemCode: KnockoutObservableArray<ItemModel>;
            selectedOutputItemCode: KnockoutObservable<string>;
            
            checkedCardNOUnregisteStamp: KnockoutObservable<boolean>;
            enableCardNOUnregisteStamp: KnockoutObservable<boolean>;
            listEmpSetShare: any =ko.observableArray([]);
            constructor() {
                let self = this;
                self.declareCCG001();
               
                self.startDateString = ko.observable("");
                self.endDateString = ko.observable("");
                self.datepickerValue = ko.observable({});
                
                self.selectedIdEmployee = ko.observableArray([]);
                self.selectedCodeEmployee = ko.observableArray([]);
                self.isShowAlreadySet = ko.observable(false);
                self.alreadySettingList = ko.observableArray([]);
                self.isDialog = ko.observable(true);
                self.isShowNoSelectRow = ko.observable(true);
                self.isMultiSelect = ko.observable(true);
                self.isShowWorkPlaceName = ko.observable(true);
                self.isShowSelectAllButton = ko.observable(false);
                self.showOptionalColumn = ko.observable(false);
                self.employeeList = ko.observableArray<UnitModel>([]);
                self.listComponentOption = {
                    isShowAlreadySet: self.isShowAlreadySet(),
                    isMultiSelect: self.isMultiSelect(),
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.NO_SELECT,
                    selectedCode: self.selectedCodeEmployee,
                    isDialog: self.isDialog(),
                    isShowNoSelectRow: self.isShowNoSelectRow(),
                    alreadySettingList: self.alreadySettingList,
                    isShowWorkPlaceName: self.isShowWorkPlaceName(),
                    isShowSelectAllButton: self.isShowSelectAllButton(),
                    showOptionalColumn: self.showOptionalColumn(),
                    maxRows: 15
                };
                
                self.lstOutputItemCode = ko.observableArray([]);
        
                self.selectedOutputItemCode = ko.observable('');
                self.checkedCardNOUnregisteStamp = ko.observable(false);
                self.enableCardNOUnregisteStamp = ko.observable(true);
                
                self.conditionBinding();
                self.selectedCodeEmployee.subscribe(function(value) {
                    let emps = _.filter(self.employeeList(), function(emp) {
                        return _.indexOf(self.selectedCodeEmployee(), emp.code) != -1;
                    });
                    self.listEmpSetShare(emps);
                })
            }
            
            /**
            * start screen
            */
            public startPage(): JQueryPromise<void>  {
                var dfd = $.Deferred<void>();
                let self = this,
                    companyId: string = __viewContext.user.companyId,
                    userId: string = __viewContext.user.employeeId;
                
                $.when(service.getDataStartPage(), service.restoreCharacteristic(companyId, userId))
                                    .done((dataStartPage, dataCharacteristic) => {
                    // get data from server
                    self.startDateString(dataStartPage.startDate);
                    self.endDateString(dataStartPage.endDate);
                    self.ccg001ComponentOption.periodStartDate = moment.utc(dataStartPage.startDate, DATE_FORMAT_YYYY_MM_DD).toISOString();
                    self.ccg001ComponentOption.periodEndDate = moment.utc(dataStartPage.endDate, DATE_FORMAT_YYYY_MM_DD).toISOString();
                      
                    let arrOutputItemCodeTmp: ItemModel[] = [];
                    _.forEach(dataStartPage.lstStampingOutputItemSetDto, function(value) {
                        arrOutputItemCodeTmp.push(new ItemModel(value.stampOutputSetCode, value.stampOutputSetName));  
                    });
                    self.lstOutputItemCode(arrOutputItemCodeTmp);                    
                                        
                    // get data from characteris
                    if (!_.isUndefined(dataCharacteristic)) {
                        self.checkedCardNOUnregisteStamp(dataCharacteristic.cardNumNotRegister);
                        self.selectedOutputItemCode(dataCharacteristic.outputSetCode);    
                    }
                    
                    // enable button when exist Authority of employment form                                        
                    self.enableCardNOUnregisteStamp(dataStartPage.existAuthEmpl);
                                        
                    dfd.resolve();
                })
                return dfd.promise();
            }
            
            /**
            * binding component CCG001 and KCP005
            */
            public executeComponent(): JQueryPromise<void> {
                var dfd = $.Deferred<void>();
                let self = this;
                blockUI.grayout();
                $.when($('#com-ccg001').ntsGroupComponent(self.ccg001ComponentOption), 
                        $('#employee-list').ntsListComponent(self.listComponentOption)).done(() => {
                   self.changeHeightKCP005(); 
                   dfd.resolve();     
                });
                return dfd.promise();
            }
            
            private declareCCG001(): void {
                let self = this;
                // Set component option
                self.ccg001ComponentOption = {
                    /** Common properties */
                    systemType: 2,
                    showEmployeeSelection: false,
                    showQuickSearchTab: true,
                    showAdvancedSearchTab: true,
                    showBaseDate: false,
                    showClosure: false,
                    showAllClosure: false,
                    showPeriod: true,
                    periodFormatYM: false,
                    
                    /** Required parameter */
                    baseDate: moment().toISOString(),
                    periodStartDate: moment().toISOString(),
                    periodEndDate: moment().toISOString(),
                    inService: true,
                    leaveOfAbsence: true,
                    closed: true,
                    retirement: false,
                    
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
                    showWorktype: false,
                    isMutipleCheck: true,
                    
                    /**
                    * Self-defined function: Return data from CCG001
                    * @param: data: the data return from CCG001
                    */
                    returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                        let arrEmployeelst: UnitModel[] = [];
                        _.forEach(data.listEmployee, function(value) {
                            arrEmployeelst.push({ code: value.employeeCode, name: value.employeeName, affiliationName: value.affiliationName, id: value.employeeId });
                        });
                        self.employeeList(arrEmployeelst);
                    }
                }    
            }
            
            /**
            * Export excel
            */
            private exportExcel(): void {
                
             var self = this;
            
            nts.uk.ui.windows.setShared('KSU001M', self.listEmpSetShare());
            nts.uk.ui.windows.sub.modal("/view/ksu/001/m/index.xhtml", { dialogClass: "no-close" }).onClosed(() => {
       
            });
            }
            
            /**
            * validate when export
            */
            private validateExportExcel(): boolean {
                let self = this;
                if (!self.checkedCardNOUnregisteStamp()) {
                    if (_.isEmpty(self.selectedCodeEmployee())) {
                        dialog.alertError({ messageId: "Msg_1204"});
                        return false;
                    }
                } 
                
                if (_.isEmpty(self.selectedOutputItemCode())) {
                    dialog.alertError({ messageId: "Msg_1205"});
                    return false;
                }
                
                // when don't have error
                return true;
            }
            
            /**
            * Open screen C
            */
            private openPreviewScrC(): void {
                let self = this,
                data: any = {};
                if (!self.validateExportExcel()) {
                    return;
                }
                //parameter
                data.startDate = self.datepickerValue().startDate;
                data.endDate = self.datepickerValue().endDate;
                data.lstEmployee = self.convertDataEmployee(self.employeeList(), self.selectedCodeEmployee());
                data.outputSetCode = self.selectedOutputItemCode();
                data.cardNumNotRegister = self.checkedCardNOUnregisteStamp();
                nts.uk.request.jump("/view/kdp/003/c/index.xhtml",data);
               
               
            }
            
            /**
            * Open screen B
            */
            private openScrB(): void {
                   let _self = this;
                    nts.uk.ui.windows.setShared("datakdp003.b",  _self.selectedOutputItemCode());
                    nts.uk.ui.windows.sub.modal("/view/kdp/003/b/index.xhtml").onClosed(() => {
                    let currentStampOutputCd = nts.uk.ui.windows.getShared("datakdp003.a");
                    if (!_.isNil(currentStampOutputCd)) {
                        service.findAll().done((lstStampingOutputItem) => {
                            let arrOutputItemCodeTmp: ItemModel[] = [];
                            _.forEach(lstStampingOutputItem, function(value) {
                                arrOutputItemCodeTmp.push(new ItemModel(value.stampOutputSetCode, value.stampOutputSetName));  
                            });
                            _self.lstOutputItemCode(arrOutputItemCodeTmp);
                            _self.selectedOutputItemCode(currentStampOutputCd);
                        }) 
                    }
                    nts.uk.ui.block.clear();
                });
            }
            
            /**
            * Subscribe Event
            */
            private conditionBinding(): void {
                let self = this;
                
                self.startDateString.subscribe(function(value){
                    self.datepickerValue().startDate = value;
                    self.datepickerValue.valueHasMutated();        
                });
                
                self.endDateString.subscribe(function(value){
                    self.datepickerValue().endDate = value;   
                    self.datepickerValue.valueHasMutated();      
                });
                
                self.checkedCardNOUnregisteStamp.subscribe((newValue) => {
//                    if (newValue) {
//                        $('#ccg001-btn-search-drawer').addClass("disable-cursor");
//                    } else {
//                        $('#ccg001-btn-search-drawer').removeClass("disable-cursor");
//                    }
                })
            }
            
            /**
            * convert data to data object matching java
            */
            private convertDataEmployee(data: UnitModel[], employeeCd: string[]): EmployeeInfor[] {
                let mapCdId : { [key:string]:string; } = {};
                let mapCdName : { [key:string]:string; } = {};
                
                let arrEmployee: EmployeeInfor[] = [];
                _.forEach(data, function(value) {
                    mapCdId[value.code] = value.id; 
                    mapCdName[value.code] = value.name; 
                });
                
                _.forEach(employeeCd, function(value) {
                    arrEmployee.push({employeeID: mapCdId[value], employeeCD: value, employeeName: mapCdName[value]}); 
                });
                
                return arrEmployee;
            }
            
            /**
            * set height table in KCP005 after initialize
            */
            private changeHeightKCP005(): void {
                let _document: any = document,
                    isIE = /*@cc_on!@*/false || !!_document.documentMode;
                if (isIE) {
                    let heightKCP = $('div[id$=displayContainer]').height();
                    $('div[id$=displayContainer]').height(heightKCP + 3);
                    $('div[id$=scrollContainer]').height(heightKCP + 3);    
                }
            }
        }
        
        export interface EmployeeInfor {
            employeeID: string;
            employeeCD: string;
            employeeName?: string;
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
            showWorkplace?: boolean; // ????????????
            showClassification?: boolean; // ????????????
            showJobTitle?: boolean; // ????????????
            showWorktype?: boolean; // ????????????
            isMutipleCheck?: boolean; // ???????????????
            isTab2Lazy?: boolean;
        
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
        
        
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        export interface UnitModel {
            code: string;
            name?: string;
            affiliationName?: string;
            id?: string;
            isAlreadySetting?: boolean;
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
        
        export class ItemModel {
            code: string;
            name: string;
        
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        
        export class OutputConditionEmbossing {
            userID: string;
            outputSetCode: string;
            cardNumNotRegister: boolean;
            
            constructor(userID: string, outputSetCode: string, cardNumNotRegister: boolean) {
                this.userID = userID;
                this.outputSetCode = outputSetCode;
                this.cardNumNotRegister = cardNumNotRegister;
            }
        }
        
        class OutputConditionOfEmbossingDto {
            startDate: string;
            endDate: string;
            lstStampingOutputItemSetDto: StampingOutputItemSetDto[];
            
            constructor(startDate: string, endDate: string, lstStampingOutputItemSetDto: StampingOutputItemSetDto[]) {
                this.startDate = startDate;
                this.endDate = endDate;
                this.lstStampingOutputItemSetDto = lstStampingOutputItemSetDto;
            }
        }
        
        class StampingOutputItemSetDto {
            stampOutputSetName: string;
            stampOutputSetCode: string;
            
            constructor(stampOutputSetName: string, stampOutputSetCode: string) {
                this.stampOutputSetName = stampOutputSetName;
                this.stampOutputSetCode = stampOutputSetCode;
            }
        }
    }
}