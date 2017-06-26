module qpp014.g.service {
    var paths: any = {
        findAllLineBank: "basic/system/bank/linebank/findAll",
        findAllBankBranch: "basic/system/bank/find/all",
    }

    /**
     * Get data from DB LINE_BANK to screen
     */
    export function findAllLineBank(): JQueryPromise<Array<any>> {
        return nts.uk.request.ajax("com", paths.findAllLineBank);
    }
    
    /**
     * Get data from DB LINE_BANK to screen
     */
    export function findAllBankBranch(): JQueryPromise<Array<any>> {
        return nts.uk.request.ajax("com", paths.findAllBankBranch);
    }
}



