module ksm004.d.service{
    var paths={
        addCalendarCompany:"at/schedule/calendar/addcalendarcompany",
        updateCalendarCompany:"at/schedule/calendar/updatecalendarcompany",
        
        addCalendarClass:"at/schedule/calendar/addcalendarclass",
        updateCalendarClass:"at/schedule/calendar/updatecalendarclass",
        
        addCalendarWorkplace:"at/schedule/calendar/addcalendarworkplace",
        updateCalendarWorkplace:"at/schedule/calendar/updatecalendarworkplace",
        
        getHolidayByDate : "at/schedule/holiday/getHolidayByDate/"
    }    
    /**
     * add new calendar Company
     */
    export function addCalendarCompany(calendarCompany : Array<viewmodel.model.CalendarCompany>) : JQueryPromise<Array<view.model.CalendarCompany>>{
        return nts.uk.request.ajax("at",paths.addCalendarCompany,calendarCompany);    
    }
    /**
     * update calendar Company
     */
    export function updateCalendarCompany(calendarCompany : Array<viewmodel.model.CalendarCompany>) : JQueryPromise<Array<view.model.CalendarCompany>>{
        return nts.uk.request.ajax("at",paths.updateCalendarCompany,calendarCompany);    
    }
    /**
     * add new calendar class
     */
    export function addCalendarClass(calendarClass : Array<viewmodel.model.CalendarClass>) : JQueryPromise<Array<view.model.CalendarClass>>{
        return nts.uk.request.ajax("at",paths.addCalendarClass,calendarClass);    
    }
    /**
     * update calendar class
     */
    export function updateCalendarClass(calendarClass : Array<viewmodel.model.CalendarCompany>) : JQueryPromise<Array<view.model.CalendarClass>>{
        return nts.uk.request.ajax("at",paths.updateCalendarClass,calendarClass);    
    }
    /**
     * add new calendar Workplace
     */
    export function addCalendarWorkplace(calendarWorkplace : Array<viewmodel.model.CalendarWorkplace>) : JQueryPromise<Array<view.model.CalendarWorkplace>>{
        return nts.uk.request.ajax("at",paths.addCalendarWorkplace,calendarWorkplace);    
    }
    /**
     * update calendar Workplace
     */
    export function updateCalendarWorkplace(calendarWorkplace : Array<viewmodel.model.CalendarWorkplace>) : JQueryPromise<Array<view.model.CalendarWorkplace>>{
        return nts.uk.request.ajax("at",paths.updateCalendarWorkplace,calendarWorkplace);    
    }
    
    /**
     * add new calendar Company
     */
    export function getHolidayByDate(date : number) : JQueryPromise<Array<>>{
        return nts.uk.request.ajax("at",paths.getHolidayByDate+date);    
    }
    
}