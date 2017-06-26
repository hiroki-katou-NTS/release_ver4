module qmm002.d.service {
    var paths: any = {
        getBankList: "basic/system/bank/find/all",
        addBank: "basic/system/bank/add",
        updateBank: "basic/system/bank/update",
        removeBank: "basic/system/bank/remove"
    }

    export function getBankList(): JQueryPromise<Array<any>> {
        let dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.getBankList)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            });
        return dfd.promise();
    }

    export function addBank(isCreated, bankInfo): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        var path = isCreated ? paths.addBank : paths.updateBank;
        
        nts.uk.request.ajax("com", path, bankInfo)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    
    export function removeBank(bankInfo): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax("com", paths.removeBank, bankInfo)
            .done(function(res: any) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
}