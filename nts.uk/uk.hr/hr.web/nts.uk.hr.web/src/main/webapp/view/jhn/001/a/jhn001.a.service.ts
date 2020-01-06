module jhn001.a.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    let paths = {
        getAll: "hr/notice/report/findAll/{0}",
        getReportDetails: "hr/notice/report/findOne/{0}",
        remove:"hr/notice/report/delete/{0}",
        saveData: "hr/notice/report/save",
        getListDoc: 'hr/notice/report/regis/person/document/findAll',
        layout: {
            getAll: "ctx/pereg/person/maintenance/findSimple/{0}",
            getDetails: "ctx/pereg/person/maintenance/findLayoutData",
            'register': 'facade/pereg/register'
        }
    };
    

    /**
    * Get list Maintenance Layout
    */
    export function getAll(abolition) {
        return ajax(format(paths.getAll, abolition));
    }

    export function getReportDetails(reportClsId) {
        return ajax(format(paths.getReportDetails, reportClsId));
    }
    
    export function removeData(reportClsId) {
        return ajax(format(paths.remove, reportClsId));
    }

   /**
    * add  Maintenance Layout
    */
    export function saveData(data: any) {
        return ajax(paths.saveData, data);
    }
    
    export function getCurrentLayout(query: any) {
        return nts.uk.request.ajax('com', "ctx/pereg/person/maintenance/findLayoutData" , query);
    }
    
    export function getListDoc(param: any) {
        return ajax('hr' , paths.getListDoc, param);
    }
}