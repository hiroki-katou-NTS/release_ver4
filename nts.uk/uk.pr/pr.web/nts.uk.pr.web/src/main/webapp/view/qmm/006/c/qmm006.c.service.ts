module qmm006.c.service {
    var paths: any = {
        findAll: "basic/system/bank/linebank/findAll",
        transfer: "basic/system/bank/linebank/transfer"
    }

    /**
     * get data from database to screen
     */
    export function findAll(): JQueryPromise<Array<any>> {
        return nts.uk.request.ajax("com", paths.findAll);
    }

    /**
     * change lineBankCode in database PERSON_BANK_ACCOUNT
     */
    export function transfer(data): JQueryPromise<any> {
        return nts.uk.request.ajax('com', paths.transfer, data);
    }
}