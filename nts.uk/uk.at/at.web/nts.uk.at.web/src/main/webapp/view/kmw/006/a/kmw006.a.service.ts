module nts.uk.at.view.kmw006.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    
    var paths = {
        checkStatus: "at/record/monthlyclosure/checkStatus",
        getInfors: "at/record/monthlyclosure/getInfors",
        getAllPersonNo: "at/record/monthlyclosure/getAllPersonNo"
    }

    export function checkStatus(data: any): JQueryPromise<any> {
//        let _path = format(paths.checkStatus, data);
        return ajax("at", paths.checkStatus, data);
    };
        
    export function getInfors(data: any): JQueryPromise<any> {
        return ajax("at", paths.getInfors, data);
    }
    
    export function getAllPersonNo(data: any): JQueryPromise<any> {
        return ajax("at", paths.getAllPersonNo, data);
    }
    
}