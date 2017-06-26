module qmm006.a.service {
    var paths: any = {
        saveData: "basic/system/bank/linebank/add",
        findAll: "basic/system/bank/linebank/findAll",
        update: "basic/system/bank/linebank/update",
        remove: "basic/system/bank/linebank/remove",
        findBankAll: "basic/system/bank/find/all",
        checkExistBankAndBranch: "basic/system/bank/find/check"
    }

    /**
     * remove data (lineBank) in database
     */
    export function remove(command): JQueryPromise<any> {
        return nts.uk.request.ajax("com", paths.remove, command);
    }

    /**
     * update or insert data (lineBank) in database
     * define update mode or insert mode base-on isEnable property
     */
    export function saveData(isEnable, command): JQueryPromise<any> {
        var path = isEnable ? paths.saveData : paths.update;
        return nts.uk.request.ajax("com", path, command);
    }

    /**
     * get data from database to screen
     */
    export function findAll(): JQueryPromise<Array<any>> {
        return nts.uk.request.ajax("com", paths.findAll);
    }

    /**
     * get data of Bank 
     */
    export function findBankAll(): JQueryPromise<Array<any>> {
        return nts.uk.request.ajax("com", paths.findBankAll);
    }

    /**
     * check exist data of Bank
     */
    export function checkExistBankAndBranch(): JQueryPromise<any> {
        return nts.uk.request.ajax("com", paths.checkExistBankAndBranch);
    }
}
