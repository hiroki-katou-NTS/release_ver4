module qpp004.a.service {
    var paths = {
        getPaymentDateProcessingList: "pr/proto/paymentdatemaster/processing/findall"
    }
    
    /**
     * Get list payment date processing.
     */
    export function getPaymentDateProcessingMasterList(): JQueryPromise<Array<PaymentDateProcessingMasterModel>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getPaymentDateProcessingList)
            .done(function(res: Array<any>){
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise(); 
    }
    
    
    
    /**
     * PaymentDateProcessingMasterModel.
     * Contain enum define information.
     */
    export interface PaymentDateProcessingMasterModel {
        /**
         * Payroll bonus attribute
         */
        payBonusAttribute: number;
    
        /**
         * Processing Number.
         */
        processingNo: number;
    
        /**
         * Processing Name.
         */
        processingName: string;
    
        /**
         * Current processing year month.
         */
        currentProcessingYm: number;
    
        /**
         * Display attribute.
         */
        displayAttribute: number;
    }
}