module nts.uk.at.view.kmw006.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    
    var paths = {
        checkStatus: "at/record/monthlyclosure/checkStatus",
        getInfors: "at/record/monthlyclosure/getInfors",
        getInforsWithNoParams: "at/record/monthlyclosure/getInforsWithNoParams",
        getAllPersonNo: "at/record/monthlyclosure/getAllPersonNo"
    }

    export function checkStatus(data: any): JQueryPromise<any> {
        return ajax("at", paths.checkStatus, data);
    };
        
    export function getInfors(data: any): JQueryPromise<any> {
        return ajax("at", paths.getInfors, data);
    }
    
     export function getInforsWithNoParams(): JQueryPromise<any> {
        return ajax("at", paths.getInforsWithNoParams);
    }
    
    export function getAllPersonNo(data: any): JQueryPromise<any> {
        return ajax("at", paths.getAllPersonNo, data);
    }
    
}