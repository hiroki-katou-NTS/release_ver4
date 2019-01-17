module nts.uk.at.view.kmf004.a.service {
    import format = nts.uk.text.format;
    import ajax = nts.uk.request.ajax;
    var paths: any = {
        findByCid: "shared/specialholiday/findByCid",
        getSpecialHoliday: "shared/specialholiday/getSpecialHoliday/{0}",
        add: "shared/specialholiday/add",
        update: "shared/specialholiday/update",
        remove: "shared/specialholiday/delete",
        getAllAbsenceFrame: "at/share/worktype/absenceframe/findAll",
        getAllSpecialHolidayFrame: "at/share/worktype/specialholidayframe/findAll",
        findEmpByCodes: "bs/employee/employment/findNamesByCodesWithNull",
        findClsByCodes: "bs/employee/classification/getClsNameByCds",
        findAllItemFrame: "shared/specialholiday/findAllItemFrame",
    }

    export function findAllItemFrame(selectedCode): JQueryPromise<Array<any>> {
        var path = paths.findAllItemFrame;
        return nts.uk.request.ajax("at", path);
    }
    
 
    export function findByCid(): JQueryPromise<any> {
        var path = nts.uk.text.format(paths.findByCid);
        return nts.uk.request.ajax("at", path);
    }
    
    export function getSpecialHoliday(specialHolidayCode: number): JQueryPromise<any> {
        var path = nts.uk.text.format(paths.getSpecialHoliday, specialHolidayCode);
        return nts.uk.request.ajax("at", path);
    }
 
    export function add(data: SpecialHolidayItem): JQueryPromise<any> {
        var path = nts.uk.text.format(paths.add);
        return nts.uk.request.ajax("at", path, data);
    }
 
    export function update(data: SpecialHolidayItem): JQueryPromise<any> {
        var path = nts.uk.text.format(paths.update);
        return nts.uk.request.ajax("at", path, data);
    }
 
    export function remove(specialHolidayCode: number): JQueryPromise<any> {
        var path = nts.uk.text.format(paths.remove);
        return nts.uk.request.ajax("at", path, { specialHolidayCode: specialHolidayCode });
    }
 
    export function getAllAbsenceFrame(): JQueryPromise<Array<any>> {
        var path = paths.getAllAbsenceFrame;
        return nts.uk.request.ajax("at", path);
    }
 
    export function getAllSpecialHolidayFrame(): JQueryPromise<Array<any>> {
        var path = paths.getAllSpecialHolidayFrame;
        return nts.uk.request.ajax("at", path);
    }

    export function findEmpByCodes(codes): JQueryPromise<any> {
        return ajax("com", paths.findEmpByCodes,codes);
    }
    export function findClsByCodes(codes): JQueryPromise<any> {
        return ajax("com",paths.findClsByCodes,codes);
    }
 
    export interface SpecialHolidayItem {
        companyId: string,
        specialHolidayCode: number,
        specialHolidayName: string,
        regularCommand: GrantRegular,
        periodicCommand: GrantPeriodic,
        leaveResCommand: SpecialLeaveRestriction,
        targetItemCommand: TargetItem,
        memo: string
    }
 
    export interface GrantRegular {
        companyId: string,
        specialHolidayCode: number,
        typeTime: number,
        grantDate: number,
        allowDisappear: number,
        grantTime: GrantTime
    }
 
    export interface GrantTime {
        fixGrantDate: FixGrantDate,
        grantDateTbl: any
    }
 
    export interface FixGrantDate {
        interval: number,
        grantDays: number
    }
 
    export interface GrantPeriodic {
        companyId: string,
        specialHolidayCode: number,
        timeSpecifyMethod: number,
        availabilityPeriod: AvailabilityPeriod,
        expirationDate: SpecialVacationDeadline,
        limitCarryoverDays: number
    }
 
    export interface AvailabilityPeriod {
        startDate: string,
        endDate: string
    }
 
    export interface SpecialVacationDeadline {
        months: number,
        years: number
    }
 
    export interface SpecialLeaveRestriction {
        companyId: string,
        specialHolidayCode: number,
        restrictionCls: number,
        ageLimit: number,
        genderRest: number,
        restEmp: number,
        listCls: Array<string>,
        ageStandard: AgeStandard,
        ageRange: AgeRange,
        gender: number,
        listEmp: Array<string>
    }
 
    export interface AgeStandard {
        ageCriteriaCls: number,
        ageBaseDate: string
    }
 
    export interface AgeRange {
        ageLowerLimit: number,
        ageHigherLimit: number
    }
 
    export interface TargetItem {
        absenceFrameNo: Array<number>,
        frameNo: Array<number>
    }
}