module nts.uk.pr.view.qmm011.e {
    import InsuranceBusinessTypeDto = nts.uk.pr.view.qmm011.a.service.model.InsuranceBusinessTypeDto;

    export module service {
        var paths: any = {
            updateInsuranceBusinessType: "pr/insurance/labor/businesstype/update"
        };

        //Function update 
        export function updateInsuranceBusinessType(insuranceBusinessType: InsuranceBusinessTypeDto): JQueryPromise<void> {
            //set up respone
            var dfd = $.Deferred<void>();
            //set up data request
            var data = { insuranceBusinessType: insuranceBusinessType };
            //call service server
            nts.uk.request.ajax(paths.updateInsuranceBusinessType, data)
                .done(function(res: void) {
                    dfd.resolve(res);
                }).fail(function(res: any) {
                    dfd.reject(res);
                })
            return dfd.promise();
        }
    }
}