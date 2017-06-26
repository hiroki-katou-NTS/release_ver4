module qpp014.e.service {
    var paths: any = {
        addBankTransfer: "pr/proto/payment/banktransfer/add"
    }

    /**
     * insert data into DB BANK_TRANSFER
     */
    export function addBankTransfer(command: any): JQueryPromise<any> {
        return nts.uk.request.ajax(paths.addBankTransfer, command);
    }
}






