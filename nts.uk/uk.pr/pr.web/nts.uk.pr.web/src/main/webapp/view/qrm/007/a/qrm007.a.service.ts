module qrm007.a.service {
    var paths: any = {
        retirePayItemSelect: "pr/core/retirement/payitem/findBycompanyCode", //qremt_Retire_Pay_Item_SEL_1
        retirePayItemUpdate: "pr/core/retirement/payitem/update" //qremt_Retire_Pay_Item_UPD_1
    }
    
    // qremt_Retire_Pay_Item_SEL_1 function
    export function retirePayItemSelect(): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.retirePayItemSelect)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    
    // qremt_Retire_Pay_Item_UPD_1 function
    export function retirePayItemUpdate(command): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.retirePayItemUpdate, command)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    
}