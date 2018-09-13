module nts.uk.com.view.qmm011.f {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            updateEmpInsurHis: "core/monsalabonus/laborinsur/updateEmpInsurHis",
            updateAccInsurHis: "core/monsalabonus/laborinsur/updateAccInsurHis"
        };

        export function updateEmpInsurHis(data :any): JQueryPromise<any> {
            return nts.uk.request.ajax(path.updateEmpInsurHis, data);
        }
        
        export function updateAccInsurHis(data :any): JQueryPromise<any> {
            return nts.uk.request.ajax(path.updateAccInsurHis, data);
        }
    }
}