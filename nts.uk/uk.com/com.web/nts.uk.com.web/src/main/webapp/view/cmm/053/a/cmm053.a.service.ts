module nts.uk.com.view.cmm053.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    var paths: any = {
        getSettingManager: "screen/com/cmm053/find/settingOfManager/{0}",
        getInfoEmLogin: "workflow/approvermanagement/workroot/getInforPsLogin",
        getWpName: "screen/com/kcp010/getLoginWkp",
        getEmployeeByCode: "workflow/approvermanagement/workroot/find/getEmployeeByCode",
        getPastHistory: "workflow/approvermanagement/workroot/find/settingOfManager/getPastHistory/{0}",
        insertHistoryByManagerSetting: "workflow/approvermanagement/workroot/managersetting/insert",
        updateHistoryByManagerSetting: "workflow/approvermanagement/workroot/managersetting/update",
        deleteHistoryByManagerSetting: "workflow/approvermanagement/workroot/managersetting/delete",
        checkApprovalSetting:"screen/com/cmm053/check-approval-setting",
        checkBfReg: "workflow/approvermanagement/workroot/checkBfRegCMM053"
    }
    
    

    export function getSettingManager(employeeId: string): JQueryPromise<any> {
        return ajax(format(paths.getSettingManager, employeeId));
    }

    export function getInfoEmLogin(): JQueryPromise<any> {
        return ajax(paths.getInfoEmLogin);
    }

    export function getWpName(): JQueryPromise<any> {
        return ajax(paths.getWpName);
    }

    export function getEmployeeByCode(employeeParamFind): JQueryPromise<any> {
        return ajax(paths.getEmployeeByCode, employeeParamFind);
    }

    export function getPastHistory(employeeId: string): JQueryPromise<any> {
        return ajax(format(paths.getPastHistory, employeeId));
    }

    export function updateHistoryByManagerSetting(command): JQueryPromise<any> {
        return ajax(paths.updateHistoryByManagerSetting, command);
    }

    export function insertHistoryByManagerSetting(command): JQueryPromise<any> {
        return ajax(paths.insertHistoryByManagerSetting, command);
    }

    export function deleteHistoryByManagerSetting(command): JQueryPromise<any> {
        return ajax(paths.deleteHistoryByManagerSetting, command);
    }
    
    export function checkApprovalSetting(command): JQueryPromise<any> {
        return ajax(paths.checkApprovalSetting, command);
    }
    
    export function checkBfReg(command): JQueryPromise<any> {
        return ajax(paths.checkBfReg, command);
    }
}