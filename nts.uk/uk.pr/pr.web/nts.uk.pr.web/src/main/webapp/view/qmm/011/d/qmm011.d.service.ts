module nts.uk.com.view.qmm011.d {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            getOccAccInsurBus: "exio/monsalabonus/laborinsur/getOccAccInsurBus",
            updateOccAccInsurBus: "exio/monsalabonus/laborinsur/updateOccAccInsurBus",
        };

        export function getOccAccInsurBus(): JQueryPromise<any> {
            return nts.uk.request.ajax(path.getOccAccInsurBus);
        }

        export function updateOccAccInsurBus(data :any): JQueryPromise<any> {
            return nts.uk.request.ajax(path.updateOccAccInsurBus, data);
        }
    }
}