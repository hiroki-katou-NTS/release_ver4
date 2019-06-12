module nts.uk.com.view.cmf005.b {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    export module service {
        
        var paths = {
            addManualSetDel: "ctx/sys/assist/app/addManualSetDel",
            getSystemDate: "ctx/sys/assist/app/getSystemDate",
        }

              
        export function addManualSetDel(manualSet: any): JQueryPromise<any> {
            return ajax('com', paths.addManualSetDel, manualSet);
        };
        
        export function getSystemDate(): JQueryPromise<any> {
            return ajax('com', paths.getSystemDate);
        };
        
    }
}
