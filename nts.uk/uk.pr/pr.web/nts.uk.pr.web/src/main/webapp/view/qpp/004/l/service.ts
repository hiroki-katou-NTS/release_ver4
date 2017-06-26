module qpp004.l.service {
    var paths = {
        createpaymentdata: "pr/proto/paymentdata/add"
    };
    
    /**
     * Get list payment date processing.
     */
    export function processCreatePaymentData(options: any): JQueryPromise<any> {
        var dfd = $.Deferred<any>();
            
           nts.uk.request.ajax(paths.createpaymentdata, options)
                .done(function(res: any){
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })    
        return dfd.promise(); 
    }
    
   
}