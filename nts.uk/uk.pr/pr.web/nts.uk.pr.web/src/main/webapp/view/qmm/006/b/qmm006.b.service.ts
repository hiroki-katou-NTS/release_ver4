module qmm006.b.service {
    var paths: any = {
        findBankAll: "basic/system/bank/find/all"
    }

    /**
     * get data of Bank
     */
    export function findBankAll(): JQueryPromise<Array<any>> {
        return nts.uk.request.ajax("com", paths.findBankAll);
    }
}
