module nts.uk.at.view.kal003.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    
    var paths = {
        getAllData: "at/function/alarm/checkcondition/findAll/{0}",
        getOneData: "at/function/alarm/checkcondition/findOne/{0}/{1}",
        registerData: "at/function/alarm/checkcondition/register",
        deleteData: "at/function/alarm/checkcondition/delete",
        getAllFixedConData: "at/record/erroralarm/fixeddata/getallfixedcondata",
        getDailyErrorAlarmCheck: "at/function/alarm/checkcondition/findDailyErrorAlarmCheck",
        getClsNameByCodes: "bs/employee/classification/getClsNameByCds",
        getEmpNameByCodes: "bs/employee/employment/findNamesByCodes",
        getJobNameByIds: "bs/employee/jobtitle/getNamesByIds",
        getBusTypeByCodes: "at/record/worktypeselection/getNamesByCodes",
        getAgreementHour: "at/function/alarm/checkcondition/agree36/findcondot",
        getAgreementError: "at/function/alarm/checkcondition/agree36/finderror",
        getAgreementNameError: "at/function/alarm/checkcondition/agree36/findnameerror"
    }

    export function getAllData(category: number): JQueryPromise<any> {
        let _path = format(paths.getAllData, category);
        return ajax("at", _path);
    };
    
    export function getOneData(category: number, code: string): JQueryPromise<any> {
        let _path = format(paths.getOneData, category, code);
        return ajax("at", _path);
    };

    export function registerData(data: any): JQueryPromise<any> {
        return ajax("at", paths.registerData, data);
    };

    export function deleteData(data: any): JQueryPromise<any> {
        return ajax("at", paths.deleteData, data);  
    }
    
    export function getDailyErrorAlarmCheck(): JQueryPromise<any> {
        return ajax("at", paths.getDailyErrorAlarmCheck);
    }
    /**
     * get All Fixed Condition WorkRecord data 
     */
    export function getAllFixedConData(): JQueryPromise<Array<any>>{
        return ajax("at", paths.getAllFixedConData);
    }
    
    export function getClsNameByCodes(data: Array<string>): JQueryPromise<any> {
        return ajax("com", paths.getClsNameByCodes, data);
    } 
    
    export function getEmpNameByCodes(data: Array<string>): JQueryPromise<any> {
        return ajax("com", paths.getEmpNameByCodes, data);
    } 
    
    export function getBusTypeNamesByCodes(data: Array<string>): JQueryPromise<any> {
        return ajax("at", paths.getBusTypeByCodes, data);
    }
    
    export function getJobNamesByIds(data: Array<string>): JQueryPromise<any> {
        return ajax("com", paths.getJobNameByIds, data);
    }
    
    export function getAgreementHour(): JQueryPromise<any> {
        return ajax("at", paths.getAgreementHour);
    }

    export function getAgreementError(): JQueryPromise<any> {
        return ajax("at", paths.getAgreementError);
    }
}