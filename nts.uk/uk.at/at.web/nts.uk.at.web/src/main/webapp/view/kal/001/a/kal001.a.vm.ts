module nts.uk.at.view.kal001.a.model {
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import textUK = nts.uk.text;
    import block = nts.uk.ui.block;
    import service = nts.uk.at.view.kal001.a.service;
    import errors = nts.uk.ui.errors;
    export class ScreenModel {
        
        // search component
        ccg001ComponentOption: GroupOption;


        // employee list component
        listComponentOption: any;
        selectedCode: KnockoutObservable<string>;
        multiSelectedCode: KnockoutObservableArray<string>;
        isShowAlreadySet: KnockoutObservable<boolean>;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
        isDialog: KnockoutObservable<boolean>;
        isShowNoSelectRow: KnockoutObservable<boolean>;
        isMultiSelect: KnockoutObservable<boolean>;
        isShowWorkPlaceName: KnockoutObservable<boolean>;
        isShowSelectAllButton: KnockoutObservable<boolean>;
        employeeList: KnockoutObservableArray<UnitModel>;     
        empCount  : KnockoutObservable<number>;
        
        // right component
        alarmCombobox: KnockoutObservableArray<any> = ko.observableArray([]);
        currentAlarmCode : KnockoutObservable<string> = ko.observable('');
        periodByCategory : KnockoutObservableArray<PeriodByCategory> = ko.observableArray([]);
        checkAll  : KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            let self = this;
            
        //search component
            self.ccg001ComponentOption = {
                /** Common properties */
                systemType: 2,
                showEmployeeSelection: false,
                showQuickSearchTab: true,
                showAdvancedSearchTab: true,
                showBaseDate: false,
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
                
                returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                    self.employeeList(_.map(data.listEmployee, (e) =>{ return new UnitModelDto(e)}));
                }
            }
                  
            
            
          // employee list component
            self.selectedCode = ko.observable('');
            self.multiSelectedCode = ko.observableArray([]);
            self.isShowAlreadySet = ko.observable(false);
            self.alreadySettingList = ko.observableArray([
                {code: '1', isAlreadySetting: true},
                {code: '2', isAlreadySetting: true}
            ]);
            self.isDialog = ko.observable(false);
            self.isShowNoSelectRow = ko.observable(false);
            self.isMultiSelect = ko.observable(true);
            self.isShowWorkPlaceName = ko.observable(true);
            self.isShowSelectAllButton = ko.observable(false);
            this.employeeList = ko.observableArray<UnitModel>([]);
            self.listComponentOption = {
                isShowAlreadySet: self.isShowAlreadySet(),
                isMultiSelect: self.isMultiSelect(),
                listType: ListType.EMPLOYEE,
                employeeInputList: self.employeeList,
                selectType: SelectType.SELECT_ALL,  
                selectedCode: self.multiSelectedCode,
                isDialog: self.isDialog(),
                isShowNoSelectRow: self.isShowNoSelectRow(),
                alreadySettingList: self.alreadySettingList,
                isShowWorkPlaceName: self.isShowWorkPlaceName(),
                isShowSelectAllButton: self.isShowSelectAllButton(), 
                isSelectAllAfterReload: true,
                maxRows  : 18,
                maxWidth: 420
            };
            self.empCount = ko.observable(0);
            self.currentAlarmCode.subscribe((newCode) => {
                errors.clearAll();
            });
                   
        }

        public startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred<any>();
            $("#fixed-table").ntsFixedTable({ height: 400, width: 450 });
            block.invisible();
            service.getAlarmByUser().done((alarmData)=>{
                
                self.alarmCombobox(alarmData);
                                
                if(self.alarmCombobox().length>0){
                    
                    self.currentAlarmCode(self.alarmCombobox()[0].alarmCode);                
                    service.getCheckConditionTime(self.currentAlarmCode()).done((checkTimeData)=>{
                        self.periodByCategory(_.map((checkTimeData), (item) =>{
                            return new PeriodByCategory(item);
                        }));
                        self.alarmCodeChange();
                        dfd.resolve();
                    }).fail((errorCheckTime) =>{
                        alertError(errorCheckTime).then(function() {
                                nts.uk.request.jumpToTopPage();
                            });
                    }).always(()=>{
                        $('#extract').focus();
                        block.clear();    
                    });
                                        
                }else{
                     dfd.resolve();  
                }
            }).fail((errorAlarm)=>{
                 alertError(errorAlarm);
                 block.clear();
            });
            return dfd.promise();
        }
        
        public alarmCodeChange(): void{
            let self = this;
            self.currentAlarmCode.subscribe((newCode)=>{
                    $(".nts-combobox").ntsError("clear");
                    service.getCheckConditionTime(newCode).done((checkTimeData)=>{
                        self.periodByCategory(_.map((checkTimeData), (item) =>{
                            return new PeriodByCategory(item);
                        }));                        
                        self.periodByCategory(_.sortBy(self.periodByCategory(), 'category'));
                         
                        let w4d4 = _.find(self.periodByCategory(), function(a) { return a.category == 2 });
                        if(w4d4 && w4d4.dateValue().startDate==null && w4d4.dateValue().endDate==null)
                           alertError({messageId : 'Msg_1193'});              
                    }).fail((errorTime)=>{
                        alertError(errorTime);
                    });
                    
                    self.checkAll(false);
            });
        }
        

        public clickCheckAll(): void{
            let self = this;
            self.checkAll(!self.checkAll());
            if(self.checkAll()){
                let periodArr = self.periodByCategory();
                periodArr.forEach((p: PeriodByCategory) =>{
                    p.checkBox(true);
                });
                self.periodByCategory(periodArr);    
            }else{
                let periodArr = self.periodByCategory();
                periodArr.forEach((p: PeriodByCategory) =>{
                    p.checkBox(false);
                });
                self.periodByCategory(periodArr);        
            }
            return;  
        }
        
        public checkBoxAllOrNot(checkBox: boolean): void{
            var self = this;
            if(checkBox && self.periodByCategory().filter(e => e.checkBox()==checkBox).length == self.periodByCategory().length)
                self.checkAll(true);
            else
                self.checkAll(false);

            
                            
        }
        
        public open_Dialog(): any {
            let self = this;
            let listSelectedEmpployee : Array<UnitModel> = self.employeeList().filter(e => self.multiSelectedCode().indexOf(e.code)>-1);
            let listPeriodByCategory = self.periodByCategory().filter(x => x.checkBox()==true);
            let start = performance.now();
            if(listSelectedEmpployee.length==0){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_834" });
                return;
            }
            if(self.currentAlarmCode()=='' ){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_1167" });
                return;
            }
            if(listPeriodByCategory.length==0){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_1168" });
                return;    
            }    
            
            $(".nts-custom").find('.nts-input').trigger("validate");
            if ($(".nts-custom").find('.nts-input').ntsError("hasError")) return;
            
            let listPeriodByCategoryTemp : KnockoutObservableArray<PeriodByCategoryTemp> = ko.observableArray([]);
             _.forEach(listPeriodByCategory, function(item: PeriodByCategory) {
                 if(item.checkBox()){
                    listPeriodByCategoryTemp.push(new PeriodByCategoryTemp(item));    
                 }             
                 
             });
            let params = {
                listSelectedEmpployee : listSelectedEmpployee,
                currentAlarmCode : self.currentAlarmCode(),
                listPeriodByCategory : listPeriodByCategoryTemp(),
                totalEmpProcess : listSelectedEmpployee.length
            };
   
            setShared("KAL001_A_PARAMS", params);
            modal("/view/kal/001/d/index.xhtml").onClosed(() => {
                // Set param to screen export B
                let paramD = getShared("KAL001_D_PARAMS");
                if (paramD) {
                    setShared("extractedAlarmData", paramD);
                    modal("/view/kal/001/b/index.xhtml").onClosed(() => {
                    });
                }
            });
        }

    }
    
    export class DateValue{
        startDate : string;
        endDate: string;
        constructor(startDate: string, endDate: string){
            this.startDate = (startDate);
            this.endDate = (endDate);
        }
    }
    
    export class PeriodByCategory{
        category : number;
        categoryName: string;
        dateValue: KnockoutObservable<DateValue>;
        checkBox: KnockoutObservable<boolean>;
        typeInput :  string = "fullDate";
        required: KnockoutObservable<boolean>;
        year:  KnockoutObservable<number> = ko.observable(1999);
        visible: KnockoutObservable<boolean>;
        id : number;
        period36Agreement : number;
        isMultiMonthAverage : KnockoutObservable<boolean>;
        multiMonthAverage :  KnockoutObservable<number> = ko.observable(190001);;
        isHoliday : KnockoutObservable<boolean>;
        nameRequired : string;
        nameStartRequired : string;
        nameEndRequired : string;
        isEnable : KnockoutObservable<boolean>;
        constructor(dto:  service.CheckConditionTimeDto){
            let self = this;
            this.category = dto.category;
            this.categoryName = dto.categoryName;
            this.period36Agreement = dto.period36Agreement;
            if(dto.category == CATEGORY.SCHEDULE_4_WEEK){
                this.isEnable =  ko.observable(false);
            } else {
                this.isEnable =  ko.observable(true);
            }
            if(dto.category==2 || dto.category==5 || dto.category==8 || dto.category == 0 || dto.category == 6){
                this.dateValue= ko.observable(new DateValue(dto.startDate, dto.endDate) );
                this.typeInput = "fullDate"; 
                this.isMultiMonthAverage = ko.observable(false);
                this.isHoliday = ko.observable(false);
                this.nameRequired = getText("KAL004_7");
                this.nameStartRequired = getText("KAL004_74");
                this.nameEndRequired = getText("KAL004_76");
                    
            }else if(dto.category ==7 || dto.category == 9 || dto.category == CATEGORY.SCHEDULE_MONTHLY || dto.category == CATEGORY.SCHEDULE_YEAR){
                this.dateValue= ko.observable(new DateValue(dto.startMonth, dto.endMonth));
                this.typeInput = "yearmonth";   
                this.isMultiMonthAverage = ko.observable(false);
                this.isHoliday = ko.observable(false);
                this.nameRequired = getText("KAL004_7");
                this.nameStartRequired = getText("KAL004_90");
                this.nameEndRequired = getText("KAL004_91");
                
            } else if(dto.category ==12){
                if(dto.period36Agreement == 6){ //'36???????????????'
                    this.year = ko.observable(dto.year);
                    this.dateValue= ko.observable(new DateValue(dto.startMonth, dto.endMonth)); 
                    this.typeInput ="yearmonth"; 
                    this.isMultiMonthAverage = ko.observable(false);
                    this.nameRequired = getText("KAL004_7");
                    this.nameStartRequired = getText("KAL004_90");
                    this.nameEndRequired = getText("KAL004_91");                  
                }else if(dto.period36Agreement == 0 || dto.period36Agreement == 1 || dto.period36Agreement == 2){ //'36?????????1???2???4??????'
                    this.dateValue= ko.observable(new DateValue(dto.startDate, dto.endDate) );
                    this.typeInput = "fullDate";     
                    this.nameRequired = getText("KAL004_7");
                    this.nameStartRequired = getText("KAL004_74");
                    this.nameEndRequired = getText("KAL004_76");
                    this.isMultiMonthAverage = ko.observable(false);              
                    
                }else if(dto.period36Agreement == 7){ //'36????????????????????????'
                    this.dateValue= ko.observable(new DateValue(dto.startMonth, dto.endMonth));
                    this.typeInput = "yearmonth";   
                    this.isMultiMonthAverage = ko.observable(true); 
                    this.multiMonthAverage(parseInt(dto.startMonth));
                } else{
                    this.dateValue = ko.observable(new DateValue(dto.startMonth, dto.endMonth));
                    this.nameRequired = getText("KAL004_7");
                    this.nameStartRequired = getText("KAL004_90");
                    this.nameEndRequired = getText("KAL004_91"); 
                    this.typeInput = "yearmonth";
                    this.isMultiMonthAverage = ko.observable(false);
                }
                this.isHoliday = ko.observable(false);
            } else if(dto.category = CATEGORY.ATTENDANCE_RATE_FOR_ANNUAL_HOLIDAYS){
                this.isHoliday = ko.observable(true);
                this.isMultiMonthAverage = ko.observable(false);
            } else if(dto.category = CATEGORY.MASTER_CHECK){
                this.isHoliday = ko.observable(true);
                this.isMultiMonthAverage = ko.observable(false);
            }
            
            this.id = dto.category + dto.tabOrder +1;
            
            this.checkBox = ko.observable(false);
            
            this.checkBox.subscribe((v)=>{
                if(v ==false) 
                {
                     $("#fixed-table").find("tr[categorynumber='"+self.id+"']").find(".nts-custom").find(".nts-input").ntsError("clear");    
                }
                
            })
            this.required = ko.computed(() =>{ return this.checkBox()}); 
            this.visible = ko.computed(()=>{
                if(this.category ==12 && this.period36Agreement == 6)    return true;
                else return false;
            });
        }
        
        public setClick() : void{
            this.checkBox(!this.checkBox());    
            __viewContext["viewmodel"].checkBoxAllOrNot(this.checkBox());
        }
        
        public  getFormattedDate(date : number) : string {
            var d = new Date(date),
                month = '' + (d.getMonth() + 1),
                day = '' + d.getDate(),
                year = d.getFullYear();
        
            if (month.length < 2) month = '0' + month;
            if (day.length < 2) day = '0' + day;
        
            return [year, month, day].join('/');
        }
    }
    
    
    
    // employee list component
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
            affiliationCode?: string;
            affiliationId?: string;
            affiliationName?: string;            
            isAlreadySetting?: boolean;
        }
        export class UnitModelDto implements UnitModel{
            id: string;
            code: string;
            name: string;
        	affiliationCode: string;
        	affiliationId: string;
        	affiliationName: string;
            isAlreadySetting: boolean;
            
            constructor(employee : EmployeeSearchDto){
                this.id = employee.employeeId;
                this.code = employee.employeeCode;
                this.name = employee.employeeName;
                this.affiliationId = employee.affiliationId;
                this.affiliationCode = employee.affiliationCode;
                this.affiliationName = employee.affiliationName;    
            }            
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
    
    
    // search component
    export interface EmployeeSearchDto {
        employeeId: string;
        employeeCode: string;
        employeeName: string;
        workplaceCode: string;
        workplaceId: string;
        workplaceName: string;
    }
    export interface GroupOption {
        /** Common properties */
        showEmployeeSelection: boolean; // ???????????????
        systemType: number; // ??????????????????
        showQuickSearchTab: boolean; // ??????????????????
        showAdvancedSearchTab: boolean; // ????????????
        showBaseDate: boolean; // ???????????????
        showClosure: boolean; // ?????????????????????
        showAllClosure: boolean; // ???????????????
        showPeriod: boolean; // ??????????????????
        periodFormatYM: boolean; // ??????????????????
    
        /** Required parameter */
        baseDate?: string; // ?????????
        periodStartDate?: string; // ?????????????????????
        periodEndDate?: string; // ?????????????????????
        inService: boolean; // ????????????
        leaveOfAbsence: boolean; // ????????????
        closed: boolean; // ????????????
        retirement: boolean; // ????????????
    
        /** Quick search tab options */
        showAllReferableEmployee: boolean; // ??????????????????????????????
        showOnlyMe: boolean; // ????????????
        showSameWorkplace: boolean; // ?????????????????????
        showSameWorkplaceAndChild: boolean; // ????????????????????????????????????
    
        /** Advanced search properties */
        showEmployment: boolean; // ????????????
        showWorkplace: boolean; // ????????????
        showClassification: boolean; // ????????????
        showJobTitle: boolean; // ????????????
        showWorktype: boolean; // ????????????
        isMutipleCheck: boolean; // ???????????????
        // showDepartment: boolean; // ???????????? not covered
        // showDelivery: boolean; not covered
    
        /** Data returned */
        returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
    }
    export interface Ccg001ReturnedData {
        baseDate: string; // ?????????
        closureId?: number; // ??????ID
        periodStart: string; // ?????????????????????)
        periodEnd: string; // ????????????????????????
        listEmployee: Array<EmployeeSearchDto>; // ????????????
    } 
    
     export enum CATEGORY {
        SCHEDULE_DAILY = 0,
        SCHEDULE_WEEKLY = 1,
        SCHEDULE_4_WEEK = 2,
        SCHEDULE_MONTHLY = 3,
        SCHEDULE_YEAR = 4,
        DAILY = 5,
        WEEKLY = 6,
        MONTHLY = 7,
        APPLICATION_APPROVAL = 8,
        MULTIPLE_MONTHS = 9,
        ANY_PERIOD = 10,
        ATTENDANCE_RATE_FOR_ANNUAL_HOLIDAYS = 11,
        _36_AGREEMENT = 12,
        MAN_HOUR_CHECK = 13,
        MASTER_CHECK = 14,
    }
     export class PeriodByCategoryTemp {
        category : number;
        categoryName: string;
        startDate : string;
        endDate : string;
        checkBox: boolean;
        typeInput :  string = "fullDate";
        required: boolean;
        year:  number = 1999;
        visible: boolean;
        id : number;
        period36Agreement : number; 
           constructor(dto:  PeriodByCategory){
            let self = this;
            this.category = dto.category;
            this.categoryName = dto.categoryName; 
            if(dto.category != CATEGORY.ATTENDANCE_RATE_FOR_ANNUAL_HOLIDAYS
                && dto.category != CATEGORY.MASTER_CHECK){   
                this.startDate = dto.dateValue().startDate;    
                this.endDate = dto.dateValue().endDate;    
            }
            this.checkBox = dto.checkBox(); 
            this.required = dto.required();
            this.visible = dto.visible();
            this.year = dto.year();
            this.period36Agreement = dto.period36Agreement;
            if(dto.category == 12 && dto.period36Agreement == 7){
                this.startDate = dto.multiMonthAverage().toString().slice(0, 4)+"/" + dto.multiMonthAverage().toString().slice(4, 6);
                this.endDate = dto.multiMonthAverage().toString().slice(0, 4)+"/" + dto.multiMonthAverage().toString().slice(4, 6);
            }
            this.typeInput = dto.typeInput;
          }
      }
}

