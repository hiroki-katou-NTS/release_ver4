module nts.uk.at.view.kal003.b.service {
    import req_ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    var paths = {
            getAttendComparison:            "at/record/attendanceitem/daily/getattendcomparison/{0}",
            getAttendCoutinousTime:         "at/record/attendanceitem/daily/getattendcoutinoustime",
            getAttendCoutinousWork:         "at/share/worktype/findNotDeprecated",
            getAttendCoutinousTimeZone:     "at/record/attendanceitem/daily/getattendcoutinoustimezone",
            getAttendCompound:              "at/record/attendanceitem/daily/getattendcompound/{0}",
            getAttendNameByIds:             "at/record/attendanceitem/daily/getattendnamebyids",
            getErrorAlarmCondition:         "at/record/attendanceitem/daily/geterroralarmcondition/{0}",
            getAttendanceItemByCodes:       "at/record/divergencetime/setting/AttendanceDivergenceName",
            findWorkTypeByCodes:            "at/share/worktype/findNotDeprecatedByListCode",
            //update #100050 daily
            getAttendanceItemByAtr:         "at/record/businesstype/attendanceItem/getListByAttendanceAtr/",
            getMonthlyAttendanceItemByAtr: "at/record/businesstype/attendanceItem/getListMonthlyByAttendanceAtr/",
            getOptItemByAtr: "at/record/attendanceitem/daily/getattendcomparison/",
            // start MinhVV Edit
            getEnumTypeCheckWorkRecordMultipleMonth: "/at/function/alarm/checkcondition/kal003b/get-enum-type-check-work-record-multiple-month",
            // End MinhVV
            getEnumSingleValueCompareTypse: "/at/function/alarm/checkcondition/kal003b/getEnumSingleValueCompareTypse",
            getEnumRangeCompareType:        "/at/function/alarm/checkcondition/kal003b/getEnumRangeCompareType",
            getEnumTypeCheckWorkRecord:     "/at/function/alarm/checkcondition/kal003b/getEnumTypeCheckWorkRecord",
            getEnumTargetSelectionRange:    "/at/function/alarm/checkcondition/kal003b/getEnumTargetSelectionRange",
            getEnumTargetServiceType:       "/at/function/alarm/checkcondition/kal003b/getEnumTargetServiceType",
            getEnumLogicalOperator:         "/at/function/alarm/checkcondition/kal003b/getEnumLogicalOperator",
            //monthly
            getAttdItemMonByAtr:         "at/record/attendanceitem/monthly/findbyatr/{0}",
            
            //Update ticket #101187
//            getSpecialholidayframe : "at/share/worktype/specialholidayframe/findspecbyabolish",
            getSpecialHoliday : "shared/specialholiday/findByCid",
            //End Update ticket #101187
            
            getMonthlyAttendanceItemByCodes: "at/record/divergencetime/setting/getMonthlyAttendanceDivergenceName",
            getListMonthlyByAtrPrimitive: "at/record/businesstype/attendanceItem/getListMonthlyByAtrPrimitive/",
            getMonthlyOptItemByAtr: "at/record/attendanceitem/monthly/getattendcomparison/",
            
            //getname monthly
            getNameMonthly  :"screen/at/correctionofdailyperformance/getNameMonthlyAttItem",

            // schedule
            getEnumDaiCheckItemType: "at/function/alarm/checkcondition/kal003b/getEnumDaiCheckItemType",
            getCheckTimeType: "at/function/alarm/checkcondition/kal003b/getCheckTimeType",
            getTimeZoneTargetRange: "at/function/alarm/checkcondition/kal003b/getTimeZoneTargetRange",
            getEnumMonCheckItemType: "at/function/alarm/checkcondition/kal003b/getEnumMonCheckItemType",
            getEnumTypeOfDays: "at/function/alarm/checkcondition/kal003b/getEnumTypeOfDays",
            getEnumTypeOfTime: "at/function/alarm/checkcondition/kal003b/getEnumTypeOfTime",
            getEnumTypeOfVacations: "at/function/alarm/checkcondition/kal003b/getEnumTypeOfVacations",
            getEnumTypeOfContrast: "at/function/alarm/checkcondition/kal003b/getEnumTypeOfContrast",
            getEnumYearCheckItemType: "at/function/alarm/checkcondition/kal003b/getEnumYearCheckItemType",
            getEnumWeeklyCheckItemType: "at/function/alarm/checkcondition/kal003b/getEnumWeeklyCheckItemType"

    }
    // MinhVV ADD
    export function getEnumTypeCheckWorkRecordMultipleMonth() : JQueryPromise<any> {
        return req_ajax(paths.getEnumTypeCheckWorkRecordMultipleMonth);
    }
    ////アルゴリズム「日次の初期起動」を実行する
    //command: checkItem => return List<AttdItemDto>
    export function getDailyItemChkItemComparison(checkItem) : JQueryPromise<any> {
        return req_ajax(format(paths.getAttendComparison, checkItem));
    }

    //return List<AttdItemDto>
    export function getAttendCoutinousTime() : JQueryPromise<any> {
        return req_ajax(paths.getAttendCoutinousTime);
    }
    
    // return List<WorkTypeDto>
    export function getAttendCoutinousWork() : JQueryPromise<any> {
        return req_ajax(paths.getAttendCoutinousWork);
    }

    //return List<SimpleWorkTimeSettingDto>
    export function getAttendCoutinousTimeZone() : JQueryPromise<any> {
        return req_ajax(paths.getAttendCoutinousTimeZone);
    }
    
    //command erAlCheckId => return ???
    export function getAttendCompound(erAlCheckId) : JQueryPromise<any> {
        return req_ajax(format(paths.getAttendCompound, erAlCheckId));
    }

    //command List<Integer> dailyAttendanceItemIds => return List<DailyAttendanceItemNameAdapterDto>
    export function getAttendNameByIds(command) : JQueryPromise<any> {
        return req_ajax(paths.getAttendNameByIds, command);
    }
    
    // command erAlCheckId => return ErrorAlarmWorkRecordDto
    export function getErrorAlarmCondition(erAlCheckId) : JQueryPromise<any> {
        return req_ajax(format(paths.getErrorAlarmCondition, erAlCheckId));
    }

    //the same kdw007
    export function getAttendanceItemByCodes(codes) : JQueryPromise<any> {
        return req_ajax("at", paths.getAttendanceItemByCodes, codes);
    }
    
    export function getAttendanceItemByAtr(atr) : JQueryPromise<any>  {
        return nts.uk.request.ajax("at", paths.getAttendanceItemByAtr + atr);
    }
    export function getMonthlyAttendanceItemByCodes(atr): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.getMonthlyAttendanceItemByCodes ,atr);
    }
    /**
     * Find work type by list codes.
     */
    export function findWorkTypeByCodes(command: Array<string>): JQueryPromise<any> {
        return req_ajax(paths.findWorkTypeByCodes, command);
    }
    // get all Enums:
    export function getEnumSingleValueCompareTypse() : JQueryPromise<any> {
        return req_ajax(paths.getEnumSingleValueCompareTypse);
    }
    export function getEnumRangeCompareType() : JQueryPromise<any> {
        return req_ajax(paths.getEnumRangeCompareType);
    }
    export function getEnumTypeCheckWorkRecord() : JQueryPromise<any> {
        return req_ajax(paths.getEnumTypeCheckWorkRecord);
    }
    export function getEnumTargetSelectionRange() : JQueryPromise<any> {
        return req_ajax(paths.getEnumTargetSelectionRange);
    }
    export function getEnumTargetServiceType() : JQueryPromise<any> {
        return req_ajax(paths.getEnumTargetServiceType);
    }
    export function getEnumLogicalOperator() : JQueryPromise<any> {
        return req_ajax(paths.getEnumLogicalOperator);
    }
    //monthly
     export function getAttdItemMonByAtr(atr:number) : JQueryPromise<any>  {
        return nts.uk.request.ajax("at", paths.getAttdItemMonByAtr,atr);
    }
    
    //Update ticket #100187
//    export function getSpecialholidayframe() : JQueryPromise<any>  {
//        return nts.uk.request.ajax("at", paths.getSpecialholidayframe);
//    }
    
    export function getSpecialHoliday() : JQueryPromise<any>  {
        return nts.uk.request.ajax("at", paths.getSpecialHoliday);
    }
    //End ticket #100187
    
     export function getAttendanceItemByCodesNew(codes, mode) {
        if (mode == 1) //monthly
            return nts.uk.request.ajax("at", paths.getMonthlyAttendanceItemByCodes, codes);
        else
            return nts.uk.request.ajax("at", paths.getAttendanceItemByCodes, codes);
    }

    export function getAttendanceItemByAtrNew(atr, mode) {
        if (mode == 1) //monthly
            //return nts.uk.request.ajax("at", paths.getListMonthlyByAtrPrimitive + atr);
            return nts.uk.request.ajax("at", paths.getMonthlyAttendanceItemByAtr + atr);
        else //daily
            return nts.uk.request.ajax("at", paths.getAttendanceItemByAtr + atr);
    }

    export function getOptItemByAtrNew(atr, mode) {
        if (mode == 1) //monthly
            return nts.uk.request.ajax("at", paths.getMonthlyOptItemByAtr + atr);
            
        else //daily
            return nts.uk.request.ajax("at", paths.getOptItemByAtr + atr);
    }
    
    export function getNameMonthly(listID : any): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getNameMonthly,listID);
        }
    
    export function getEnumDaiCheckItemType() : JQueryPromise<any> {
        return req_ajax(paths.getEnumDaiCheckItemType);
    }
    
    export function getCheckTimeType() : JQueryPromise<any> {
        return req_ajax(paths.getCheckTimeType);
    }
    
    export function getTimeZoneTargetRange(): JQueryPromise<any> {
        return req_ajax(paths.getTimeZoneTargetRange);
    }
    
    export function getEnumMonCheckItemType() : JQueryPromise<any> {
        return req_ajax(paths.getEnumMonCheckItemType);
    }
    
    export function getEnumTypeOfContrast() : JQueryPromise<any> {
        return req_ajax(paths.getEnumTypeOfContrast);
    }
    
    export function getEnumTypeOfDays() : JQueryPromise<any> {
        return req_ajax(paths.getEnumTypeOfDays);
    }
    
    export function getEnumTypeOfTime(): JQueryPromise<any> {
        return req_ajax(paths.getEnumTypeOfTime);
    }
    
    export function getEnumTypeOfVacations() : JQueryPromise<any> {
        return req_ajax(paths.getEnumTypeOfVacations);
    }
    
    export function getEnumYearCheckItemType() : JQueryPromise<any> {
        return req_ajax(paths.getEnumYearCheckItemType);
    }
    
    export function getEnumWeeklyCheckItemType() : JQueryPromise<any> {
        return req_ajax(paths.getEnumWeeklyCheckItemType);
    }
    
}
