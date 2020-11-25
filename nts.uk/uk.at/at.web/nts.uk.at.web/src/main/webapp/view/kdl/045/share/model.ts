module nts.uk.at.view.kdl045.share.model {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import modal = nts.uk.ui.windows.sub.modal;
    import getText = nts.uk.resource.getText;
    
    
    export class ParamKsu003 {
        employeeInfo: EmployeeInformation;//社員情報
        targetInfor: number;//対象情報 : 複数回勤務 (1 :true,0:false)
        canModified: number;//修正可能 CanModified
        scheCorrection: Array<number>;//スケジュール修正の機能制御  WorkTimeForm
        constructor(employeeInfo: EmployeeInformation,
            targetInfor: number,
            canModified: number,
            scheCorrection: Array<number>) {
            let self = this;
            self.employeeInfo = employeeInfo;
            self.targetInfor = targetInfor;
            self.canModified = canModified;
            self.scheCorrection = scheCorrection;
        }
    }
    
    /**
     * 社員情報
     */
    export class EmployeeInformation {
        employeeCode: string;//社員コード
        employeeName: string;//社員名称   
        employeeWorkInfoDto: EmployeeWorkInfoDto;//対象社員の社員勤務情報dto
        employeeWorkScheduleDto: EmployeeWorkScheduleDto;//対象社員の社員勤務予定dto
        fixedWorkInforDto: FixedWorkInforDto;//対象社員の勤務固定情報dto
        constructor(employeeCode: string,
            employeeName: string,
            employeeWorkInfoDto: EmployeeWorkInfoDto,
            employeeWorkScheduleDto: EmployeeWorkScheduleDto,
            fixedWorkInforDto: FixedWorkInforDto) {
            let self = this;
            self.employeeCode = employeeCode;
            self.employeeName = employeeName;
            self.employeeWorkInfoDto = employeeWorkInfoDto;
            self.employeeWorkScheduleDto = employeeWorkScheduleDto;
            self.fixedWorkInforDto = fixedWorkInforDto;
        }
    }

   // interface : 対象社員の社員勤務情報dto - 社員勤務情報dto
    export interface IEmployeeWorkInfoDto {
        isCheering: number; //応援か : SupportAtr
        isConfirmed: number; //確定済みか 0:false or 1:true
        bounceAtr: number; //直帰区分 0:false or 1:true
        directAtr: number; //直行区分 0:false or 1:true
        isNeedWorkSchedule: number;//勤務予定が必要か 0:false or 1:true
        employeeId: string;//社員ID
        date: string;//年月日
        //optional.empty;
        supportTimeZone :TimeZoneDto;//応援時間帯
        wkpNameSupport : string;//応援先の職場名称
        listTimeVacationAndType : Array<TimeVacationAndType> //Map<時間休暇種類, 時間休暇>
        shiftCode : string;//シフトコード
        shiftName : string;//シフト名称
        shortTime : TimeZoneDto;//List<育児介護短時間帯>
    }
    
    //対象社員の社員勤務情報dto - 社員勤務情報dto
    export class EmployeeWorkInfoDto {
        isCheering: number; //応援か : SupportAtr
        isConfirmed: number; //確定済みか 0:false or 1:true
        bounceAtr: number; //直帰区分 0:false or 1:true
        directAtr: number; //直行区分 0:false or 1:true
        isNeedWorkSchedule: number;//勤務予定が必要か 0:false or 1:true
        employeeId: string;//社員ID
        date: string;//年月日
        //optional.empty;
        supportTimeZone :TimeZoneDto;//応援時間帯
        wkpNameSupport : string;//応援先の職場名称
        listTimeVacationAndType : Array<TimeVacationAndType> //Map<時間休暇種類, 時間休暇>
        shiftCode : string;//シフトコード
        shiftName : string;//シフト名称
        shortTime : TimeZoneDto;//List<育児介護短時間帯>
        constructor(param : IEmployeeWorkInfoDto) {
            let self = this;
            self.isCheering = param.isCheering;
            self.isConfirmed = param.isConfirmed;
            self.bounceAtr = param.bounceAtr;
            self.directAtr = param.directAtr;
            self.isNeedWorkSchedule = param.isNeedWorkSchedule;
            self.employeeId = param.employeeId;
            self.date = param.date;
            self.supportTimeZone = param.supportTimeZone;
            self.wkpNameSupport = param.wkpNameSupport;
            self.listTimeVacationAndType = param.listTimeVacationAndType;
            self.shiftCode = param.shiftCode;
            self.shiftName = param.shiftName;
            self.shortTime = param.shortTime;
        }
    }
    
    //時間休暇
    export class TimeVacationDto {
        timeZone: TimeZoneDto; //時間帯リスト
        usageTime: DailyAttdTimeVacationDto;//使用時間
        constructor(timeZone: TimeZoneDto,
            usageTime: DailyAttdTimeVacationDto) {
            this.timeZone = timeZone;
            this.usageTime = usageTime;
        }
    }
    
    //時間帯(実装コードなし/使用不可)
    export class TimeZoneDto {
        startTime: TimeOfDayDto;//開始時刻 
        endTime: TimeOfDayDto;//終了時刻
        constructor(startTime: TimeOfDayDto,
            endTime: TimeOfDayDto) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    //時刻(日区分付き)
    export class TimeOfDayDto {
        dayDivision: number;//日区分   : DayDivision
        time: number;//時刻
        constructor(dayDivision: number,
            time: number) {
            this.dayDivision = dayDivision;
            this.time = time;
        }
    }
    //日別勤怠の時間休暇使用時間
    export class DailyAttdTimeVacationDto {
        timeAbbyakLeave: number; //時間年休使用時間
        timeOff: number;//時間代休使用時間
        excessPaidHoliday: number;//超過有休使用時間
        specialHoliday: number;//特別休暇使用時間
        frameNO: number;//特別休暇枠NO
        childNursingLeave: number;//子の看護休暇使用時間
        nursingCareLeave: number;//介護休暇使用時間
        constructor(timeAbbyakLeave: number,
            timeOff: number,
            excessPaidHoliday: number,
            specialHoliday: number,
            frameNO: number,
            childNursingLeave: number,
            nursingCareLeave: number) {
            this.timeAbbyakLeave = timeAbbyakLeave;
            this.timeOff = timeOff;
            this.excessPaidHoliday = excessPaidHoliday;
            this.specialHoliday = specialHoliday;
            this.frameNO = frameNO;
            this.childNursingLeave = childNursingLeave;
            this.nursingCareLeave = nursingCareLeave;
        }
    }
    
    //Map<時間休暇種類, 時間休暇>
    export class TimeVacationAndType {
        typeVacation: number; //時間休暇種類 : TimeVacationType
        timeVacation: TimeVacationDto; //時間休暇 
        constructor(typeVacation: number,
            timeVacation: TimeVacationDto) {
            this.typeVacation = typeVacation;
            this.timeVacation = timeVacation;
        }
    }
    
    // interface : 対象社員の社員勤務予定dto - 社員勤務予定dto
    export interface IEmployeeWorkScheduleDto {
        startTime1: number; //開始時刻１
        startTime1Status: number; //開始時刻１編集状態 : EditStateSetting
        endTime1: number; //終了時刻１
        endTime1Status: number; //終了時刻１編集状態 : EditStateSetting
        startTime2: number; //開始時刻2
        startTime2Status: number; //開始時刻2編集状態 : EditStateSetting
        endTime2: number; //終了時刻2
        endTime2Status: number; //終了時刻2編集状態 : EditStateSetting
        listBreakTimeZoneDto : Array<BreakTimeOfDailyAttdDto> //List<休憩時間帯>
        workTypeCode: string;//勤務種類コード
        breakTimeStatus: number;//休憩時間帯編集状態 : EditStateSetting
        workTypeStatus: number;//勤務種類編集状態 : EditStateSetting
        workTimeCode: string;//就業時間帯コード
        workTimeStatus: number;//就業時間帯編集状態 : EditStateSetting
    }
    
     // 対象社員の社員勤務予定dto - 社員勤務予定dto
    export class EmployeeWorkScheduleDto {
        startTime1: number; //開始時刻１
        startTime1Status: number; //開始時刻１編集状態 : EditStateSetting
        endTime1: number; //終了時刻１
        endTime1Status: number; //終了時刻１編集状態 : EditStateSetting
        startTime2: number; //開始時刻2
        startTime2Status: number; //開始時刻2編集状態 : EditStateSetting
        endTime2: number; //終了時刻2
        endTime2Status: number; //終了時刻2編集状態 : EditStateSetting
        listBreakTimeZoneDto: Array<BreakTimeOfDailyAttdDto> //List<休憩時間帯> (EA : List＜休憩時間帯＞＝勤務予定．休憩時間帯) 
        workTypeCode: string;//勤務種類コード
        breakTimeStatus: number;//休憩時間帯編集状態 : EditStateSetting
        workTypeStatus: number;//勤務種類編集状態 : EditStateSetting
        workTimeCode: string;//就業時間帯コード
        workTimeStatus: number;//就業時間帯編集状態 : EditStateSetting
        constructor(param: IEmployeeWorkScheduleDto) {
            let self = this;
            self.startTime1 = param.startTime1;
            self.startTime1Status = param.startTime1Status;
            self.endTime1 = param.endTime1;
            self.endTime1Status = param.endTime1Status;
            self.startTime2 = param.startTime2;
            self.startTime2Status = param.startTime2Status;
            self.endTime2 = param.endTime2;
            self.endTime2Status = param.endTime2Status;
            self.listBreakTimeZoneDto = param.listBreakTimeZoneDto;
            self.workTypeCode = param.workTypeCode;
            self.breakTimeStatus = param.breakTimeStatus;
            self.workTypeStatus = param.workTypeStatus;
            self.workTimeCode = param.workTimeCode;
            self.workTimeStatus = param.workTimeStatus;
        }
    }
    
    /**
     * 日別勤怠の休憩時間帯   (EA : List＜休憩時間帯＞＝勤務予定．休憩時間帯) 
     */
    export class BreakTimeOfDailyAttdDto{
        breakType: number; //休憩種類 : 0: 就業時間帯から参照 (or 実績) and 1: スケジュールから参照 (or 予定)
        breakTimeSheets : Array<BreakTimeZoneDto>;//時間帯
        constructor(breakType: number,
            breakTimeSheets: Array<BreakTimeZoneDto>) {
            this.breakType = breakType;
            this.breakTimeSheets = breakTimeSheets;
            
        }
    }
    
    
    /**
     * 時間帯
     */
    export class BreakTimeZoneDto {
        breakFrameNo: number;//休憩枠NO
        startTime: number; //開始 - 勤怠打刻(実打刻付き)
        endTime: number; //終了 - 勤怠打刻(実打刻付き)
        breakTime : number;//休憩時間: 勤怠時間 
        constructor(breakFrameNo: number,
            startTime: number,
            endTime: number,
            breakTime : number) {
            this.breakFrameNo = breakFrameNo;
            this.startTime = startTime;
            this.endTime = endTime;
            this.breakTime = breakTime;
        }
    }
    
    // interface : 対象社員の勤務固定情報dto - 勤務固定情報dto
    export interface IFixedWorkInforDto {
        workTimeName: string; //就業時間帯名称
        coreStartTime: number; //コア開始時刻
        coreEndTime: number; //コア終了時刻
        overtimeHours : Array<ChangeableWorkTime>; //List<残業時間帯>
        startTimeRange1: TimeZoneDto; //日付開始時刻範囲時間帯1
        etartTimeRange1: TimeZoneDto;//日付終了時刻範囲時間帯1
        workTypeName: string;//勤務種類名称
        startTimeRange2: TimeZoneDto; //日付開始時刻範囲時間帯2
        etartTimeRange2: TimeZoneDto;//日付終了時刻範囲時間帯2
        fixBreakTime : number; //休憩時間帯を固定にする (0:false 1:true)
        workType:number;//勤務タイプ : WorkTimeForm
    }
    
    // 対象社員の勤務固定情報dto - 勤務固定情報dto
    export class FixedWorkInforDto {
        workTimeName: string; //就業時間帯名称
        coreStartTime: number; //コア開始時刻
        coreEndTime: number; //コア終了時刻
        overtimeHours : Array<ChangeableWorkTime>; //List<残業時間帯>
        startTimeRange1: TimeZoneDto; //日付開始時刻範囲時間帯1
        etartTimeRange1: TimeZoneDto;//日付終了時刻範囲時間帯1
        workTypeName: string;//勤務種類名称
        startTimeRange2: TimeZoneDto; //日付開始時刻範囲時間帯2
        etartTimeRange2: TimeZoneDto;//日付終了時刻範囲時間帯2
        fixBreakTime : number; //休憩時間帯を固定にする (0:false 1:true)
        workType :number;//勤務タイプ : WorkTimeForm
         constructor(param: FixedWorkInforDto) {
            let self = this;
            self.workTimeName = param.workTimeName;
            self.coreStartTime = param.coreStartTime;
            self.coreEndTime = param.coreEndTime;
            self.overtimeHours = param.overtimeHours;
            self.startTimeRange1 = param.startTimeRange1;
            self.etartTimeRange1 = param.etartTimeRange1;
            self.workTypeName = param.workTypeName;
            self.startTimeRange2 = param.startTimeRange2;
            self.etartTimeRange2 = param.etartTimeRange2;
            self.fixBreakTime = param.fixBreakTime;
            self.workType = param.workType;
        }
    }
    
    /**
     * 勤務NOごとの変更可能な勤務時間帯
     */
    export class ChangeableWorkTime {
        workNo: number;//勤務NO
        changeableStartTime: TimeZoneDto;//開始時刻の変更可能な時間帯
        changeableEndTime: TimeZoneDto;//終了時刻の変更可能な時間帯
        constructor(changeableStartTime: TimeZoneDto,
            changeableEndTime: TimeZoneDto) {
            this.changeableStartTime = changeableStartTime;
            this.changeableEndTime = changeableEndTime;
        }
    }

    /**
     * 応援区分
     */
    export enum SupportAtr {
        /**
         * 応援ではない
         */
        NOT_CHEERING = 0,
        /**
         * 時間帯応援元
         */
        TIMEZONE_SUPPORTER = 1,
        /**
         * 時間帯応援先
         */
        TIMEZONE_RESPONDENT = 2,
        /**
         * 終日応援元
         */
        ALL_DAY_SUPPORTER = 3,
        /**
         * 終日応援先
         */
        ALL_DAY_RESPONDENT = 4
    }
    
     /**
     * 時間休暇種類
     */
    export enum TimeVacationType {
        /**
         * 出勤前
         */
        ATWORK = 0,
        /**
         * 退勤後
         */
        OFFWORK = 1,
        /**
         * 出勤前2
         */
        ATWORK2 = 2,
        /**
         * 退勤後2
         */
        OFFWORK2 = 3,
        /**
         * 私用外出
         */
        PRIVATE = 4,
        /**
         * 組合外出
         */
        UNION = 5
    }
    
    /**
     * 日区分
     */
    export enum DayDivision{
         /**
         * 当日
         */
        THIS_DAY = 0,
        /**
         * 翌日
         */
        NEXT_DAY = 1,
        /**
         * 翌々日
         */
        TW0_DAY_LATE = 2,
        /**
         * 前日
         */
        DAY_BEFORE = 3
    }
    
     /**
     * 日別実績の編集状態 
     */
    export enum EditStateSetting{
         /**
         * 手修正（本人）
         */
        HAND_CORRECTION_MYSELF = 0,
        /**
         * 手修正（他人） 
         */
        HAND_CORRECTION_OTHER = 1,
        /**
         * 申請反映
         */
        REFLECT_APPLICATION = 2,
        /**
         * 打刻反映
         */
        IMPRINT = 3
    }
    
    /**
     * 就業時間帯の勤務形態
     */
    export enum WorkTimeForm{
         /**
         * 固定勤務 
         */
        FIXED = 0,
        /**
         * フレックス勤務
         */
        FLEX = 1,
        /**
         * 流動勤務
         */
        FLOW = 2,
        /**
         * 時差勤務
         */
        TIMEDIFFERENCE = 3
    }
    
    /**
     * 修正可能
     */
     export enum CanModified{
         /**
         * 参照
         */
        REFERENCE = 0,
        /**
         * 修正
         */
        FIX = 1
    }

}