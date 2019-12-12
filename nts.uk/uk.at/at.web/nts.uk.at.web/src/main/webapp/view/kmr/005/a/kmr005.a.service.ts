module nts.uk.at.view.kmr005.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    var paths = {
        startup: "bento/report/startup",
        exportFile: "bento/report/reservation/month"
    }
    
    export function startup(): JQueryPromise<any> {
        return nts.uk.request.ajax("at", paths.startup);
    }
        
    export function exportFile(): JQueryPromise<any> {
        return nts.uk.request.exportFile("at", paths.exportFile);
    }
}