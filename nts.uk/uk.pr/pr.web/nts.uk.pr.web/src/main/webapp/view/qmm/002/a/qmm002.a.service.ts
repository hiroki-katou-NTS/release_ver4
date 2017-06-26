module nts.uk.pr.view.qmm002.a {
    export module service {
        var paths = {
            getBankList: "basic/system/bank/find/all",
            addBranchList: "basic/system/bank/branch/add",
            updateBranchList: "basic/system/bank/branch/update",
            removeBranch: "basic/system/bank/branch/remove",
            removeBank: "basic/system/bank/remove",
            check: "basic/system/bank/find/check"
        };

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
        
        export function checkBankList(): JQueryPromise<any> {
            let dfd = $.Deferred<any>();
            nts.uk.request.ajax("com", paths.check)
                .done(function(res) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        export function addBank(isCreated, bankInfo): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            var path = isCreated ? paths.addBranchList : paths.updateBranchList;

            nts.uk.request.ajax("com", path, bankInfo)
                .done(function(res: any) {
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
        
        export function removeBank(isParentNode, bankCode, branchId): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            var path = isParentNode ? paths.removeBank : paths.removeBranch;
            var obj = {
                bankCode: bankCode,
                branchId: branchId   
            };
            nts.uk.request.ajax("com", path, obj)
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