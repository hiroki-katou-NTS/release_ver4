module nts.uk.pr.view.qmm016.k {
    export module service {
        var pathService = {
            loadDemensionSelectionList: "pr/proto/wagetable/demensions"
        };

        //fuction connection service load demension
        export function loadDemensionSelectionList(): JQueryPromise<model.DemensionItemDto[]> {
            return nts.uk.request.ajax(pathService.loadDemensionSelectionList);
        }

        export module model {

            export class DemensionItemDto {
                /** The type. */
                type: number;
                /** The code. */
                code: string;
                /** The name. */
                name: string;
                /** The is code mode. */
                isCodeMode: boolean;
                /** The is range mode. */
                isRangeMode: boolean;
            }
        }
    }
}