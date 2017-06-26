module qmm018.a.service {
    var paths: any = {
        averagePayItemSelect: "pr/core/averagepay/findByCompanyCode",
        averagePayItemInsert: "pr/core/averagepay/register",
        averagePayItemUpdate: "pr/core/averagepay/update"
    }
    
    /**
     * select average pay item
     */
    export function averagePayItemSelect(): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.averagePayItemSelect);
    }
    
    /**
     * insert average pay item, salary items, attend items
     */
    export function averagePayItemInsert(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.averagePayItemInsert, command);
    }
    
    /**
     * update average pay item, salary items, attend items
     */
    export function averagePayItemUpdate(command): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.averagePayItemUpdate, command);
    }
}