module nts.uk.pr.view.qmm002.b {
    export module service {
        var paths = {
            removeListBank: "basic/system/bank/remove/list"
        };
        
        export function removeBank(data): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            var path = paths.removeListBank;
            nts.uk.request.ajax("com", path, data)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
    }    
}