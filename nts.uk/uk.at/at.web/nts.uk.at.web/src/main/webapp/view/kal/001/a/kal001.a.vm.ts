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
                maxRows  : 22
            };
            self.empCount = ko.observable(0);
            self.currentAlarmCode.subscribe((newCode) => {
                errors.clearAll();
            });
                   
        }

        public startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred<any>();
            $("#fixed-table").ntsFixedTable({ height: 280, width: 600 });
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
                        alertError(errorCheckTime);
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
                 listPeriodByCategoryTemp.push(new PeriodByCategoryTemp(item));
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
        
        constructor(dto:  service.CheckConditionTimeDto){
            let self = this;
            this.category = dto.category;
            this.categoryName = dto.categoryName;
            this.period36Agreement = dto.period36Agreement;
            
            if(dto.category==2 || dto.category==5){
                this.dateValue= ko.observable(new DateValue(dto.startDate, dto.endDate) );
                this.typeInput = "fullDate"; 
                    
            }else if(dto.category ==7 || dto.category == 9 ){
                this.dateValue= ko.observable(new DateValue(dto.startMonth, dto.endMonth));
                this.typeInput = "yearmonth";   
                
            } else if(dto.category ==12){
                if(dto.categoryName=='36協定　年間'){
                    this.year = ko.observable(dto.year);
                    this.dateValue= ko.observable(new DateValue(dto.startMonth, dto.endMonth)); 
                    this.typeInput ="yearmonth"; 
                                      
                }else if(dto.categoryName=='36協定　1・2・4週間'){
                    this.dateValue= ko.observable(new DateValue(dto.startDate, dto.endDate) );
                    this.typeInput = "fullDate";                     
                    
                } else{
                    this.dateValue = ko.observable(new DateValue(dto.startMonth, dto.endMonth));
                     this.typeInput = "yearmonth";
                }
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
                if(this.category ==12 && this.categoryName =="36協定　年間")    return true;
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
            workplaceCode?: string;
            workplaceId?: string;
            workplaceName?: string;            
            isAlreadySetting?: boolean;
        }
        export class UnitModelDto implements UnitModel{
            id: string;
            code: string;
            name: string;
            workplaceCode: string;
            workplaceId: string;
            workplaceName: string;
            isAlreadySetting: boolean;
            
            constructor(employee : EmployeeSearchDto){
                this.id = employee.employeeId;
                this.code = employee.employeeCode;
                this.name = employee.employeeName;
                this.workplaceId = employee.workplaceId;
                this.workplaceCode = employee.workplaceCode;
                this.workplaceName = employee.workplaceName;    
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
        showEmployeeSelection: boolean; // 検索タイプ
        systemType: number; // システム区分
        showQuickSearchTab: boolean; // クイック検索
        showAdvancedSearchTab: boolean; // 詳細検索
        showBaseDate: boolean; // 基準日利用
        showClosure: boolean; // 就業締め日利用
        showAllClosure: boolean; // 全締め表示
        showPeriod: boolean; // 対象期間利用
        periodFormatYM: boolean; // 対象期間精度
    
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
        // showDepartment: boolean; // 部門条件 not covered
        // showDelivery: boolean; not covered
    
        /** Data returned */
        returnDataFromCcg001: (data: Ccg001ReturnedData) => void;
    }
    export interface Ccg001ReturnedData {
        baseDate: string; // 基準日
        closureId?: number; // 締めID
        periodStart: string; // 対象期間（開始)
        periodEnd: string; // 対象期間（終了）
        listEmployee: Array<EmployeeSearchDto>; // 検索結果
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
            this.startDate = dto.dateValue().startDate;    
            this.endDate = dto.dateValue().endDate;    
            this.checkBox = dto.checkBox(); 
            this.required = dto.required();
            this.visible = dto.visible();
            this.year = dto.year();
            this.period36Agreement = dto.period36Agreement;
          }
      }
}

