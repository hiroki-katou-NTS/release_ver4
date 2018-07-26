module nts.uk.at.view.kdp003.c {
    export module service {


        var servicePath = {
            findStampingOutput: "at/function/statement/findStampingOutput/"
        }

        export function findStampOutput(stampCode: string): JQueryPromise<model.StampingOutputItemSetDto> {
            return nts.uk.request.ajax(servicePath.findStampingOutput + stampCode);

        }
        
         export module model {
            export interface StampingOutputItemSetDto {
                stampOutputSetCode: string;
                stampOutputSetName: string;
                outputEmbossMethod: boolean;
                outputWorkHours: boolean
                outputSetLocation: boolean;
                outputPosInfor: boolean;
                outputOT: boolean;
                outputNightTime: boolean;
                outputSupportCard: boolean;
            }
           

        }

    }
}

