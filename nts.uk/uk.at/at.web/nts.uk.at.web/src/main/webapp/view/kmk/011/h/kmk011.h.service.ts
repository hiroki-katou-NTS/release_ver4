module nts.uk.at.view.kmk011.h {

    export module service {
        /**
         * define path to service
         */
        var path: any = {
            save: "at/record/divergence/time/history/divergenceRefTimeUsageUnit/save",
            find: "at/record/divergence/time/history/divergenceRefTimeUsageUnit/find"
        };

        export function save(data: model.DivergenceReferenceTimeUsageUnit): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.save, data);
        }

        export function find(): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.find);
        }
    }
    export module model {
        export class DivergenceReferenceTimeUsageUnit {
            workTypeUseSet: number;

            constructor(workTypeUseSet: number) {
                this.workTypeUseSet = workTypeUseSet;
            }
        }
    }
}