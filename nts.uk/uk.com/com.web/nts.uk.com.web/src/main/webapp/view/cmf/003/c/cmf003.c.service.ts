module nts.uk.com.view.cmf003.c {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    
    export module service {
            var paths = {
                //TODO: Fake ws
                getCategoryBySytem: "ctx/sys/assist/app/findCategory/{0}",
                getSystemType: "ctx/sys/assist/app/getSysType"
            }
         
            export function getSysTypes(): JQueryPromise<any> {
                return ajax("com", paths.getSystemType);
            }
        
            export function getConditionList(systemType: number): JQueryPromise<any> {
                let _path = format(paths.getCategoryBySytem, systemType);
                return ajax('com', _path);
            };
        
    }
}