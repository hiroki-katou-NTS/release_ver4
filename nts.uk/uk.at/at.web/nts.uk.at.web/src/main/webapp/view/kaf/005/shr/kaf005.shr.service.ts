module nts.uk.at.view.kaf005.shr.service {
    var paths: any = {
        getOvertimeByUI: "at/request/application/overtime/getOvertimeByUI",
        findByChangeAppDate: "at/request/application/overtime/findByChangeAppDate",
        checkConvertPrePost: "at/request/application/overtime/checkConvertPrePost",
        createOvertime: "at/request/application/overtime/create",
        deleteOvertime: "at/request/application/overtime/delete",
        updateOvertime: "at/request/application/overtime/update",
        checkBeforeRegister: "at/request/application/overtime/checkBeforeRegister",
    }

    /** Get TitleMenu */
    export function getOvertimeByUI(param: any): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.getOvertimeByUI, param);
    }
    
    export function findByChangeAppDate(param: any): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.findByChangeAppDate, param);
    }
    
    export function checkConvertPrePost(prePostAtr: string): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.checkConvertPrePost, prePostAtr);
    }
    
    export function createOvertime(overtime: any): JQueryPromise<void> {
        return nts.uk.request.ajax("at", paths.createOvertime,overtime);
    }
    
     export function deleteOvertime(appID : string): JQueryPromise<void> { 
        return nts.uk.request.ajax("at", paths.deleteOvertime,appID);
    }
    
     export function updateOvertime(overtime:any): JQueryPromise<void> {
        return nts.uk.request.ajax("at", paths.updateOvertime ,overtime);
    }
    
    export function checkBeforeRegister(overtime:any): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.checkBeforeRegister ,overtime);
    }
}