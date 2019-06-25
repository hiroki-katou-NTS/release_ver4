module nts.uk.at.view.ktg029.a.viewmodel {
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import info = nts.uk.ui.dialog.info;
    export var STORAGE_KEY_TRANSFER_DATA = "nts.uk.request.STORAGE_KEY_TRANSFER_DATA";
    export class ScreenModel {
        currentMonth: KnockoutObservable<period>;
        nextMonth: KnockoutObservable<period>;
        switchDate: KnockoutObservable<boolean>;
        checked: KnockoutObservable<boolean>;
        txtDatePeriod: KnockoutObservable<string>;
        btnSwitch: KnockoutObservable<string>;
        displayOvertime: KnockoutObservable<boolean>; 
        displayHoliInstruct: KnockoutObservable<boolean>;
        displayApproved: KnockoutObservable<boolean>;
        displayUnApproved: KnockoutObservable<boolean>;
        displayDenied: KnockoutObservable<boolean>;
        displayRemand: KnockoutObservable<boolean>;
        displayDeadlineMonth: KnockoutObservable<boolean>;
        displayPresenceDailyPer: KnockoutObservable<boolean>;
        displayWorkRecord: KnockoutObservable<boolean>;
        displayOverTimeHours: KnockoutObservable<boolean>;
        displayFlexTime: KnockoutObservable<boolean>;
        displayRestTime: KnockoutObservable<boolean>;
        displayNightWorkHours: KnockoutObservable<boolean>;
        displayLateOrEarlyRetreat: KnockoutObservable<boolean>;
        /* A17 */
        displayYearlyHoliDay: KnockoutObservable<boolean>;
        displayHaftDayOff: KnockoutObservable<boolean>;
        displayHoursOfHoliday: KnockoutObservable<boolean>;
        /* A18 */
        displayYearRemainNo: KnockoutObservable<boolean>;
        // A19
        displayPlannedYearHoliday: KnockoutObservable<boolean>;
        // A20
        displayRemainAlternationNo: KnockoutObservable<boolean>;
        // A21 
        displayRemainsLeft: KnockoutObservable<boolean>;
        displayPublicHDNo: KnockoutObservable<boolean>;
        displayHDRemainNo: KnockoutObservable<boolean>;
        displayCareLeaveNo: KnockoutObservable<boolean>;
        displaySPHDRamainNo: KnockoutObservable<boolean>;
        displaySixtyhExtraRest: KnockoutObservable<boolean>;
        dataRecord: KnockoutObservable<OptionalWidgetInfo>;
        
        constructor() {
            var self = this;
            self.switchDate = ko.observable(true);      
            self.currentMonth = ko.observable(new period("",""));
            self.nextMonth = ko.observable(new period("",""));
            self.txtDatePeriod = ko.observable("");
            self.btnSwitch = ko.observable(getText('KTG029_7'));
            self.checked = ko.observable(true);
            
            self.displayOvertime = ko.observable(false); 
            self.displayHoliInstruct = ko.observable(false);
            self.displayApproved = ko.observable(false);
            self.displayUnApproved = ko.observable(false);
            self.displayDenied = ko.observable(false);
            self.displayRemand = ko.observable(false);
            self.displayDeadlineMonth = ko.observable(false);
            self.displayPresenceDailyPer = ko.observable(false);
            self.displayWorkRecord = ko.observable(false);
            self.displayOverTimeHours = ko.observable(false);
            self.displayFlexTime = ko.observable(false);
            self.displayRestTime = ko.observable(false);
            self.displayNightWorkHours = ko.observable(false);
            self.displayLateOrEarlyRetreat = ko.observable(false);
            self.displayYearlyHoliDay = ko.observable(false);
            self.displayHaftDayOff = ko.observable(false);
            self.displayHoursOfHoliday = ko.observable(false);
            self.displayYearRemainNo = ko.observable(false);
            self.displayPlannedYearHoliday = ko.observable(false);
            self.displayRemainAlternationNo = ko.observable(false); 
            self.displayRemainsLeft = ko.observable(false);
            self.displayPublicHDNo = ko.observable(false);
            self.displayHDRemainNo = ko.observable(false);
            self.displayCareLeaveNo = ko.observable(false);
            self.displaySPHDRamainNo = ko.observable(false);
            self.displaySixtyhExtraRest = ko.observable(false);
            self.dataRecord = ko.observable(null);
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            block.invisible();
            var topPagePartCode = $(location).attr('search').split('=')[1];
            if(nts.uk.text.isNullOrEmpty(topPagePartCode)){
                block.clear();
                dfd.resolve();
                return dfd.promise();   
            }            
            new service.Service().getOptionalWidgetDisplay(topPagePartCode).done(function(data: any){
                if(data!=null){
                    self.excuteDisplay(data.optionalWidgetImport);
                    self.excuteDate(data.datePeriodDto);
                    self.switchMonth();
                    dfd.resolve();
                }else{
                    block.clear();
                }  
            });           
            return dfd.promise();
        }
        private getInfor(code: string, strDate: string, endDate: string): void{
            var self = this;
            block.invisible();
            var param ={
                code: code,
                strMonth: strDate,
                endMonth: endDate
            };
            new service.Service().getOptionalWidgetInfo(param).done(function(data: OptionalWidget){
                self.dataRecord(new OptionalWidgetInfo(data));
                block.clear();
            });           
        }
        private excuteDisplay(data: any):void{
            var self = this;
            if(data==null)
                return;
            var listDisplayItems = data.widgetDisplayItemExport;
            listDisplayItems.forEach(function (item){
                if(item.displayItemType == widgetDisplayItem.OVERTIME_WORK_NO){
                    self.displayOvertime(item.notUseAtr == 1 ? true: false); 
                }else if(item.displayItemType == widgetDisplayItem.INSTRUCTION_HD_NO){
                    self.displayHoliInstruct(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.APPROVED_NO){
                    self.displayApproved(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.UNAPPROVED_NO){
                    self.displayUnApproved(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.DENIED_NO){
                    self.displayDenied(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.REMAND_NO){
                    self.displayRemand(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.APP_DEADLINE_MONTH){
                    self.displayDeadlineMonth(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.PRESENCE_DAILY_PER){
                    self.displayPresenceDailyPer(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.REFER_WORK_RECORD){
                    self.displayWorkRecord(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.OVERTIME_HOURS){
                    self.displayOverTimeHours(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.FLEX_TIME){
                    self.displayFlexTime(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.REST_TIME){
                    self.displayRestTime(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.NIGHT_WORK_HOURS){
                    self.displayNightWorkHours(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.LATE_OR_EARLY_RETREAT){
                    self.displayLateOrEarlyRetreat(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.YEARLY_HD){
                    /* A17 */
                    self.displayYearlyHoliDay(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.HAFT_DAY_OFF){
                    /* A17 */
                    self.displayHaftDayOff(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.HOURS_OF_HOLIDAY_UPPER_LIMIT){
                    /* A17 */
                    self.displayHoursOfHoliday(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.RESERVED_YEARS_REMAIN_NO){
                    self.displayYearRemainNo(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.PLANNED_YEAR_HOLIDAY){
                    self.displayPlannedYearHoliday(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.REMAIN_ALTERNATION_NO){
                    self.displayRemainAlternationNo(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.REMAINS_LEFT){
                    self.displayRemainsLeft(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.PUBLIC_HD_NO){
                    self.displayPublicHDNo(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.HD_REMAIN_NO){
                    self.displayHDRemainNo(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.CARE_LEAVE_NO){
                    self.displayCareLeaveNo(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.SPHD_RAMAIN_NO){
                    self.displaySPHDRamainNo(item.notUseAtr == 1 ? true: false);
                }else if(item.displayItemType == widgetDisplayItem.SIXTYH_EXTRA_REST){
                    self.displaySixtyhExtraRest(item.notUseAtr == 1 ? true: false);
                }
            });
        }
        
        private excuteDate(data: any) :void{
            var self = this;
            if(data==null){
                self.createDate();
            }else{
                self.currentMonth(new period(data.strCurrentMonth, data.endCurrentMonth));
                self.nextMonth(new period(data.strNextMonth, data.endNextMonth));
            }
        }
        private createDate() :void{
            var self =  this;
            var today = new Date();
            self.currentMonth(self.getStrEndMonth(today));
            
            if (today.getMonth() == 11) {
                var nexMonth = new Date(today.getFullYear() + 1, 0, 1);
            } else {
                var nexMonth = new Date(today.getFullYear(), today.getMonth() + 1, 1);
            }
            self.nextMonth(self.getStrEndMonth(nexMonth));
        }
        private getStrEndMonth(date: Date):period{
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var lastDay = new Date(y, m, 0);
            return new period(y+'/'+m+'/'+'01', y+'/'+m+'/'+lastDay.getDate());
        }
        private switchMonth():void{
            var self = this;
            var code = $(location).attr('search').split('=')[1];
            if(isNaN(self.currentMonth().strMonth)){
                return;    
            }
            if(self.switchDate()){
                var strMonth = self.currentMonth().strMonth.getMonth()+1;
                if(strMonth<10){
                     strMonth='0'+strMonth; 
                } 
                var strDay = self.currentMonth().strMonth.getDate();
                if(strDay<10){
                     strDay='0'+strDay; 
                } 
                var endMonth = self.currentMonth().endMonth.getMonth()+1;
                if(endMonth<10){
                     endMonth='0'+endMonth; 
                }
                var endDay = self.currentMonth().endMonth.getDate();
                if(endDay<10){
                     endDay='0'+endDay; 
                } 
                self.txtDatePeriod(strMonth+'/'+strDay+getText('KTG029_3')+endMonth+'/'+endDay+getText('KTG029_5'));
                self.getInfor(code, self.currentMonth().strMonth, self.currentMonth().endMonth);
                self.btnSwitch(getText('KTG029_8'));
                self.switchDate(false);
            }else{
                var strMonth = self.nextMonth().strMonth.getMonth()+1;
                if(strMonth<10){
                     strMonth='0'+strMonth; 
                } 
                var strDay = self.nextMonth().strMonth.getDate();
                if(strDay<10){
                     strDay='0'+strDay; 
                } 
                var endMonth = self.nextMonth().endMonth.getMonth()+1;
                if(endMonth<10){
                     endMonth='0'+endMonth; 
                }
                var endDay = self.nextMonth().endMonth.getDate();
                if(endDay<10){
                     endDay='0'+endDay; 
                } 
                self.txtDatePeriod(strMonth+'/'+strDay+getText('KTG029_3')+endMonth+'/'+endDay+getText('KTG029_5'));
                self.getInfor(code, self.nextMonth().strMonth, self.nextMonth().endMonth);
                self.btnSwitch(getText('KTG029_7'));
                self.switchDate(true);
            }
        }
        
        openKAF015Dialog() {
            var self = this;
            window.top.location = window.location.origin + '/nts.uk.at.web/view/kaf/015/a/index.xhtml';
        }
        private conVerDate(date: Date):string {
            var strDay = date.getDate();
            if(strDay<10){
                 strDay='0'+strDay; 
            } 
            var strMonth = date.getMonth()+1;
            if(strMonth<10){
                 strMonth='0'+strMonth; 
            } 
            return date.getFullYear()+'/'+strMonth+'/'+strDay;
        }
        
        private openCMM045Dialog():void {
            parent.nts.uk.ui.block.grayout();
            var self = this;
//          ※URLパラメータ　＝　照会モード
            if(self.switchDate()){
                var strDate = self.conVerDate(self.nextMonth().strMonth);
                var endDate = self.conVerDate(self.nextMonth().endMonth);
            }else{
                var strDate = self.conVerDate(self.currentMonth().strMonth);
                var endDate = self.conVerDate(self.currentMonth().endMonth);
            }
            let paramSave = {  
                startDate: strDate,
                endDate: endDate,
                appListAtr: 0,
                appType: -1,
                unapprovalStatus: true,
                approvalStatus: false,
                denialStatus: false,
                agentApprovalStatus: false,
                remandStatus: false,
                cancelStatus: false,
                appDisplayAtr: 0,
                listEmployeeId: [],
                empRefineCondition: ""
            };
            nts.uk.characteristics.remove("AppListExtractCondition").done(function() {
                parent.nts.uk.characteristics.save('AppListExtractCondition', paramSave).done(function() {
                    parent.nts.uk.ui.block.clear();
                    nts.uk.localStorage.setItem('UKProgramParam', 'a=0');
                    window.top.location = window.location.origin + '/nts.uk.at.web/view/cmm/045/a/index.xhtml';
                });    
            });          
        }
        
        openKDW003Dialog() {
            var self = this;
            var employeeIds = [];
            employeeIds.push(__viewContext.user.employeeId);
            if(self.switchDate()){
                var strDate = self.conVerDate(self.nextMonth().strMonth);
                var endDate = self.conVerDate(self.nextMonth().endMonth);
            }else{
                var strDate = self.conVerDate(self.currentMonth().strMonth);
                var endDate = self.conVerDate(self.currentMonth().endMonth);
            }
            let initParam = {
                screenMode: 0, 
                lstEmployee: employeeIds,
                errorRefStartAtr: self.checked(),
                transitionDesScreen: null
            };
            let extractionParam = {
                displayFormat: 0,
                startDate: strDate,
                endDate: endDate,
                lstExtractedEmployee: employeeIds,
                individualTarget: __viewContext.user.employeeId
            };
            uk.sessionStorage.setItemAsJson(STORAGE_KEY_TRANSFER_DATA, {initParam: initParam, extractionParam: extractionParam});
            window.top.location = window.location.origin + '/nts.uk.at.web/view/kdw/003/a/index.xhtml';
        }
        
        openKDL033Dialog() {
            let self = this;
//            parent.nts.uk.ui.windows.sub.modal('at','/view/kdl/033/a/index.xhtml').onClosed(function(): any {
//            });

        }
        
        openKDL029Dialog() {
            let self = this; 
            let lstid = [];
            if(self.switchDate()){
                var endDate = self.conVerDate(self.nextMonth().endMonth);
            }else{
                var endDate = self.conVerDate(self.currentMonth().endMonth);
            }
            lstid.push(__viewContext.user.employeeId);
            let param = {employeeIds: lstid, baseDate: moment(endDate).format("YYYY/MM/DD")};
            parent.nts.uk.ui.windows.setShared('KDL029_PARAM', param);
            parent.nts.uk.ui.windows.sub.modal('at','/view/kdl/029/a/index.xhtml');
            //parent.nts.uk.ui.windows.sub.modal("/view/kdl/029/a/index.xhtml");
        }
        
        openKDL005Dialog() {
            let self = this;
            var employeeIds = [];
            employeeIds.push(__viewContext.user.employeeId);
            if(self.switchDate()){
                var endDate = self.conVerDate(self.nextMonth().endMonth);
            }else{
                var endDate = self.conVerDate(self.currentMonth().endMonth);
            }
            let param = {
                baseDate: moment(endDate, "YYYY/MM/DD").format("YYYYMMDD"),
                employeeIds: employeeIds
            };
            parent.nts.uk.ui.windows.setShared("KDL005_DATA", param);
            parent.nts.uk.ui.windows.sub.modal('at','/view/kdl/005/a/single.xhtml');
        }
        
        openKDL009Dialog() {
            let self = this;
            var employeeIds = [];
            employeeIds.push(__viewContext.user.employeeId);
            if(self.switchDate()){
                var strDate = self.conVerDate(self.nextMonth().strMonth);
            }else{
                var strDate = self.conVerDate(self.currentMonth().strMonth);
            }
            let param = {
                baseDate: moment(strDate, "YYYY/MM/DD").format("YYYYMMDD"),
                employeeIds: employeeIds
            };
            parent.nts.uk.ui.windows.setShared("KDL009_DATA", param);
            parent.nts.uk.ui.windows.sub.modal('at','/view/kdl/009/a/single.xhtml');
        }
        
        openKDL017Dialog() {
            let self = this;
//            parent.nts.uk.ui.windows.sub.modal('at','/view/kdl/017/a/index.xhtml').onClosed(function(): any {
//            });

        }
        openKDL020Dialog() {
            let self = this;
            if(self.switchDate()){
                var endDate = self.nextMonth().endMonth;
            }else{
                var endDate = self.currentMonth().endMonth;
            }
            var employeeIds = [];
            employeeIds.push(__viewContext.user.employeeId);
            parent.nts.uk.ui.windows.setShared('KDL020A_PARAM', { baseDate: endDate, employeeIds:  employeeIds});
            parent.nts.uk.ui.windows.sub.modal('at','/view/kdl/020/a/index.xhtml').onClosed(function(): any {});
        }
    }
    export class period{
        strMonth: Date;
        endMonth: Date;
        constructor(strMonth: string, endMonth: string){
            this.strMonth = new Date(strMonth);
            this.endMonth = new Date(endMonth);
        }    
    }
    export interface TimeOTDto{
        hours: number;
        min: number;
    }
    export interface DeadlineOfRequestDto {
        use: boolean;
        deadLine: Date;
    }
    export interface YearlyHolidayInfoDto {
        day: number;
        hours: TimeOTDto;
        remaining: number;
        timeYearLimit: TimeOTDto;
    }
    export interface YearlyHolidayDto {
        nextTime: string;
        grantedDaysNo: number;
        nextTimeInfo: YearlyHolidayInfoDto;
        nextGrantDate: string;
        nextGrantDateInfo: YearlyHolidayInfoDto;
        afterGrantDateInfo: YearlyHolidayInfoDto;
        attendanceRate: number;
        workingDays: number;
        calculationMethod: number;
        useSimultaneousGrant: number;
        showGrantDate: boolean;
    }
    export interface RemainingNumberDto{
        name: string;
        before: number;
        after: number;
        grantDate: string;
        showAfter: boolean;
    }
    export interface OptionalWidget{
        overTime: number;
        holidayInstruction: number;
        approved: number;
        unApproved: number;
        deniedNo: number;
        remand: number;
        appDeadlineMonth: DeadlineOfRequestDto;
        presenceDailyPer: boolean;
        overtimeHours: TimeOTDto;
        flexTime: TimeOTDto;
        restTime: TimeOTDto;
        nightWorktime: TimeOTDto;
        lateRetreat: number;
        earlyRetreat: number;
        yearlyHoliday: YearlyHolidayDto;
        reservedYearsRemainNo: RemainingNumberDto;
        remainAlternationNoDay: number;
        remainsLeft: number;
        publicHDNo: number;
        childRemainNo: RemainingNumberDto;
        careLeaveNo: RemainingNumberDto;
        sphdramainNo: Array<RemainingNumberDto>;
        extraRest: TimeOTDto;  
    }
    export class YearlyHolidayInfo {
        day: number;
        hours: string;
        displayHours: boolean;
        remaining: number;
        timeYearLimit: string;
        constructor(dto: YearlyHolidayInfoDto){
            this.day = dto.day;
            this.hours = (dto.hours.hours<10?('0'+dto.hours.hours):dto.hours.hours)+':'+(dto.hours.min<10?('0'+dto.hours.min):dto.hours.min);
            this.displayHours = (dto.hours.hours == 0 && dto.hours.min == 0)?false:true;
            this.remaining = dto.remaining;
            this.timeYearLimit = (dto.timeYearLimit.hours<10?('0'+dto.timeYearLimit.hours):dto.timeYearLimit.hours)+':'+(dto.timeYearLimit.min<10?('0'+dto.timeYearLimit.min):dto.timeYearLimit.min);
        }
    }
    export class YearlyHoliday {
        nextTime: string;
        grantedDaysNo: number;
        nextTimeInfo: YearlyHolidayInfo;
        nextGrantDate: string;
        nextGrantDateInfo: YearlyHolidayInfo;
        afterGrantDateInfo: YearlyHolidayInfo;
        attendanceRate: number;
        workingDays: number;
        calculationMethod: number;
        useSimultaneousGrant: number;
        showGrantDate: boolean;
        constructor(dto: YearlyHolidayDto){
            this.nextTime = dto.nextTime == null ? '': dto.nextTime.substr(-8);
            this.grantedDaysNo = dto.grantedDaysNo;
            this.nextTimeInfo = new YearlyHolidayInfo(dto.nextTimeInfo);
            this.nextGrantDate = dto.nextGrantDate == null ? '': dto.nextGrantDate.substr(-8);
            this.nextGrantDateInfo = new YearlyHolidayInfo(dto.nextGrantDateInfo);
            this.afterGrantDateInfo = new YearlyHolidayInfo(dto.afterGrantDateInfo);
            this.attendanceRate = dto.attendanceRate;
            this.workingDays = dto.workingDays;
//            if(dto.calculationMethod==3){
//                info({ messageId: "Msg_1315" });
//            }
            this.calculationMethod = dto.calculationMethod;
            this.useSimultaneousGrant = dto.useSimultaneousGrant;
            this.showGrantDate = dto.showGrantDate;
        }
    }
    export class RemainingNumber{
        name: string;
        before: number;
        after: number;
        grantDate: string;
        showAfter: boolean;   
        constructor(dto: RemainingNumberDto){
            this.name = dto.name;
            this.before = dto.before;
            this.after = dto.after;
            this.grantDate = dto.grantDate !=null ? moment(dto.grantDate,'YYYY/MM/DD').format('YY/MM/DD'): '';
            this.showAfter = dto.showAfter;
        }     
    }
    export class OptionalWidgetInfo{
        overTime: number;
        holidayInstruction: number;
        approved: number;
        unApproved: number;
        deniedNo: number;
        remand: number;
        appDeadlineUse: boolean;
        appDeadlineMonth: Date;
        presenceDailyPer: boolean;
        overtimeHours: string;
        flexTime: string;
        restTime: string;
        nightWorktime: string
        lateRetreat: number;
        earlyRetreat: number;
        yearlyHoliday: YearlyHoliday;
        reservedYearsRemainNo: RemainingNumber;
        remainAlternationNoDay: number;
        remainsLeft: number;
        publicHDNo: number;
        childRemainNo: RemainingNumber;
        careLeaveNo: RemainingNumber;
        sPHDRamainNo: Array<RemainingNumber>;
        extraRest: string;
        constructor (data: OptionalWidget){
            this.overTime = data.overTime;
            this.holidayInstruction = data.holidayInstruction;
            this.approved = data.approved;
            this.unApproved = data.unApproved;
            this.deniedNo = data.deniedNo;
            this.remand = data.remand;
            this.appDeadlineUse = data.appDeadlineMonth.use;
            this.appDeadlineMonth =  data.appDeadlineMonth.deadLine;
            this.presenceDailyPer = data.presenceDailyPer;
            this.overtimeHours = (data.overtimeHours.hours<10?('0'+data.overtimeHours.hours):data.overtimeHours.hours)+':'+(data.overtimeHours.min<10?('0'+data.overtimeHours.min):data.overtimeHours.min);
            this.flexTime = (data.flexTime.hours<10?('0'+data.flexTime.hours):data.flexTime.hours)+':'+(data.flexTime.min<10?('0'+data.flexTime.min):data.flexTime.min);
            this.restTime = (data.restTime.hours<10?('0'+data.restTime.hours):data.restTime.hours)+':'+(data.restTime.min<10?('0'+data.restTime.min):data.restTime.min);
            this.nightWorktime = (data.nightWorktime.hours<10?('0'+data.nightWorktime.hours):data.nightWorktime.hours)+':'+(data.nightWorktime.min<10?('0'+data.nightWorktime.min):data.nightWorktime.min);
            this.lateRetreat = data.lateRetreat;
            this.earlyRetreat = data.earlyRetreat;
            this.yearlyHoliday = new YearlyHoliday(data.yearlyHoliday);
            this.reservedYearsRemainNo = new RemainingNumber(data.reservedYearsRemainNo);
            this.remainAlternationNoDay = data.remainAlternationNoDay;
            this.remainsLeft = data.remainsLeft;
            this.publicHDNo = data.publicHDNo;
            this.childRemainNo = new RemainingNumber(data.childRemainNo);
            this.careLeaveNo = new RemainingNumber(data.careLeaveNo);
            this.sPHDRamainNo = data.sphdramainNo.map(c=>new RemainingNumber(c));
            this.extraRest = (data.extraRest.hours<10?('0'+data.extraRest.hours):data.extraRest.hours)+':'+(data.extraRest.min<10?('0'+data.extraRest.min):data.extraRest.min);
        }
    }
    
    export enum widgetDisplayItem{
        OVERTIME_WORK_NO = 0,
        INSTRUCTION_HD_NO = 1,
        APPROVED_NO = 2,
        UNAPPROVED_NO = 3,
        DENIED_NO = 4,
        REMAND_NO = 5,
        APP_DEADLINE_MONTH = 6,
        PRESENCE_DAILY_PER = 7,
        REFER_WORK_RECORD = 8,
        OVERTIME_HOURS = 9,
        FLEX_TIME = 10,
        REST_TIME = 11,
        NIGHT_WORK_HOURS = 12,
        LATE_OR_EARLY_RETREAT = 13,
        YEARLY_HD = 14,
        HAFT_DAY_OFF = 15,
        HOURS_OF_HOLIDAY_UPPER_LIMIT = 16,
        RESERVED_YEARS_REMAIN_NO = 17,
        PLANNED_YEAR_HOLIDAY = 18,
        REMAIN_ALTERNATION_NO = 19,
        REMAINS_LEFT = 20,
        PUBLIC_HD_NO = 21,
        HD_REMAIN_NO = 22,
        CARE_LEAVE_NO = 23,
        SPHD_RAMAIN_NO = 24,
        SIXTYH_EXTRA_REST = 25
    }
    export enum NotUseAtr{
        USE = 1,
        NOT_USE =0    
    }
}

