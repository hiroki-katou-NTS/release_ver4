module nts.uk.at.view.kdw002.c {
    export module service {
        var paths: any = {
            //daily
            getEmpRole: "at/record/workrecord/authfuncrest/find-emp-roles",
            getDailyAttdItemByRoleID : "at/shared/scherec/dailyattditem/auth/getdailyattd",
            getListDailyAttdItem : "at/shared/scherec/dailyattditem/getalldailyattd",
            updateDailyAttdItem :"at/shared/scherec/dailyattditem/auth/updatedailyattd",
            //monthly
            getListMonthlyAttdItem:"at/record/attendanceitem/monthly/findall"
            }
        //daily
        export function getEmpRole(): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getEmpRole);
        }
        export function updateDailyAttdItem(command): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.updateDailyAttdItem,command);
        }
        
        export function getListDailyAttdItem(): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getListDailyAttdItem);
        }
        
        export function getDailyAttdItemByRoleID(roleID:string): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getDailyAttdItemByRoleID +"/" +roleID);
        }
        
        // monthly
        export function getListMonthlyAttdItem(): JQueryPromise<any> {
            return nts.uk.request.ajax(paths.getListMonthlyAttdItem);
        }
        
        
    }
}
