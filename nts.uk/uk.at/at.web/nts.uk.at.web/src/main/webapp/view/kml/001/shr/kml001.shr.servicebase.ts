module kml001.shr.servicebase {
    var paths: any = {
        personCostCalculationSelect: "at/schedule/budget/premium/findPersonCostCalculationByCompanyID",
        personCostCalculationInsert: "at/schedule/budget/premium/insertPersonCostCalculation",
        personCostCalculationUpdate: "at/schedule/budget/premium/updatePersonCostCalculation",
        personCostCalculationDelete: "at/schedule/budget/premium/deletePersonCostCalculation",
        premiumItemSelect: "at/schedule/budget/premium/findPremiumItemByCompanyID",
        premiumItemUpdate: "at/schedule/budget/premium/updatePremiumItem",
        getAttendanceItems: "at/share/attendanceitem/getPossibleAttendanceItem",
        getAttendanceItemByType:  "at/share/attendanceType/getByType/",
    }
    
    export function personCostCalculationSelect(): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.personCostCalculationSelect);
    }
   
    export function personCostCalculationInsert(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.personCostCalculationInsert, command);
    }
    
    export function personCostCalculationUpdate(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.personCostCalculationUpdate, command);
    }
    
    export function personCostCalculationDelete(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.personCostCalculationDelete, command);
    }
    
    export function premiumItemSelect(): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.premiumItemSelect);
    }
    
    export function premiumItemUpdate(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.premiumItemUpdate, command);
    }
    
    export function getAttendanceItems(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.getAttendanceItems, command);
    }
    
    export function getAttendanceItemByType(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.getAttendanceItemByType + command);
    }
    
}