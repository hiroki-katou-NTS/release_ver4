module nts.uk.pr.view.qmm007.c {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            submitPayrollUnitPriceHis: "core/wageprovision/companyuniformamount/submitPayrollUnitPriceHis"
        };

        export function submitPayrollUnitPriceHis(data :any): JQueryPromise<any> {
            return nts.uk.request.ajax(path.submitPayrollUnitPriceHis, data);
        }

    }
}