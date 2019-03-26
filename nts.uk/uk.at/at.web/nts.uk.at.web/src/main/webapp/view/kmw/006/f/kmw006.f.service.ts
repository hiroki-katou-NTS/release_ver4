module nts.uk.at.view.kmw006.f.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    
    var paths = {
        getMonthlyClosureLog: "at/record/monthlyclosure/getMonthlyClosure/{0}",
        executeMonthlyClosure: "at/record/monthlyclosure/execution",
        completeConfirm: "at/record/monthlyclosure/completeConfirm/{0}",
        getResults: "at/record/monthlyclosure/getResults/{0}",
        getPersonCompleteNo: "at/record/monthlyclosure/getPersonCompleteNo/{0}",
        getDurationTime: "at/record/monthlyclosure/getDurationTime/{0}",
        getListError: "at/record/monthlyclosure/getListError/{0}"
    }

    export function getMonthlyClosureLog(id: string): JQueryPromise<any> {
        let _path = format(paths.getMonthlyClosureLog, id);
        return ajax("at", _path);
    };
    
    export function executeMonthlyClosure(data: any): JQueryPromise<any> {
        return ajax("at", paths.executeMonthlyClosure, data);
    }
    
    export function completeConfirm(id: string): JQueryPromise<any> {
        let _path = format(paths.completeConfirm, id);
        return ajax("at", _path);
    }
    
     export function getResults(id: string): JQueryPromise<any> {
        let _path = format(paths.getResults, id);
        return ajax("at", _path);
    }
    
    export function getPersonCompleteNo(id: string): JQueryPromise<any> {
        let _path = format(paths.getPersonCompleteNo, id);
        return ajax("at", _path);
    }

    export function getDurationTime(id: string): JQueryPromise<any> {
        let _path = format(paths.getDurationTime, id);
        return ajax("at", _path);
    }

    export function getListError(id: string): JQueryPromise<any> {
       let _path = format(paths.getListError, id);
        return ajax("at", _path); 
    }
}