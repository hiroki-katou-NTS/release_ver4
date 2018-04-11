module nts.uk.at.view.kal001.a.model {
    import getText = nts.uk.resource.getText;
    import confirm = nts.uk.ui.dialog.confirm;
    import alertError = nts.uk.ui.dialog.alertError;
    import info = nts.uk.ui.dialog.info;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
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
                systemType: 1,
                showEmployeeSelection: true,
                showQuickSearchTab: true,
                showAdvancedSearchTab: true,
                showBaseDate: true,
                showClosure: true,
                showAllClosure: true,
                showPeriod: true,
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
                selectType: SelectType.SELECT_BY_SELECTED_CODE,
                selectedCode: self.multiSelectedCode,
                isDialog: self.isDialog(),
                isShowNoSelectRow: self.isShowNoSelectRow(),
                alreadySettingList: self.alreadySettingList,
                isShowWorkPlaceName: self.isShowWorkPlaceName(),
                isShowSelectAllButton: self.isShowSelectAllButton(),
                maxRows  : 22
            };
        
            
                   
        }

        public startPage(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred<any>();
            $("#fixed-table").ntsFixedTable({ height: 300, width: 600 });
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
            let listPeriodByCategory = self.periodByCategory().filter(x => x.category==2);
          
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
            
            block.invisible();
            service.extractAlarm(listSelectedEmpployee, self.currentAlarmCode(), listPeriodByCategory).done((dataExtractAlarm: service.ExtractedAlarmDto)=>{
                
                if(dataExtractAlarm.extracting) {
                    nts.uk.ui.dialog.info({ messageId: "Msg_993" });    
                    return;
                }
                if(dataExtractAlarm.nullData){
                      nts.uk.ui.dialog.info({ messageId: "Msg_835" });   
                      return;
                }
                
                nts.uk.ui.windows.setShared("extractedAlarmData", dataExtractAlarm.extractedAlarmData);
                modal("/view/kal/001/b/index.xhtml").onClosed(() => {
                    
                });
            }).fail((errorExtractAlarm)=>{
                alertError(errorExtractAlarm);
            }).always(()=>{
                block.clear();    
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
        typeInput :  string;
        constructor(dto:  service.CheckConditionTimeDto){
            this.category = dto.category;
            this.categoryName = dto.categoryName;
            if(dto.category==2 || dto.category==5){
                this.dateValue= ko.observable(new DateValue(dto.startDate, dto.endDate) );
                this.typeInput = "fullDate";     
            }else{
                this.dateValue= ko.observable(new DateValue(dto.startMonth, dto.endMonth));
                this.typeInput = "yearmonth";   
            }
            this.checkBox = ko.observable(false);
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
}

