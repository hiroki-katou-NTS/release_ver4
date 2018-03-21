module nts.uk.at.view.kmk011.g {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            saveComHist: "at/record/divergence/time/history/companyDivergenceRefTime/save",
            saveWkTypeHist: "at/record/divergence/time/history/workTypeDivergenceRefTime/save",
            findByHistId: "at/record/divergence/time/history/companyDivergenceRefTime/find"
        };
        
       export function saveComHist(data: model.CreateComHistoryCommand): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.saveComHist, data);
        }
        
        export function saveWkTypeHist(data: model.CreateWkTypeHistoryCommand): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.saveWkTypeHist, data);
        }
        
        export function findByHistoryId(historyId: string): JQueryPromise<model.CreateComHistoryCommand> {
            return nts.uk.request.ajax("at", path.findByHistId + "/" + historyId);
        }
    }
    
    export module model {
        export class CreateComHistoryCommand {
            historyId: string;
            startDate: string;
            endDate: string;
            isCopyData: boolean;
            
            constructor(historyId: string, startDate: string, endDate: string) {
                this.historyId = historyId;
                this.startDate = startDate;
                this.endDate = endDate;
            }
        }
        
        export class CreateWkTypeHistoryCommand {
            workTypeCodes: string;
            historyId: string;
            startDate: string;
            endDate: string;
            isCopyData: boolean;
            
            constructor(workTypeCodes: string, historyId: string, startDate: string, endDate: string, isCopyData: boolean) {
                this.workTypeCodes = workTypeCodes;
                this.historyId = historyId;
                this.startDate = startDate;
                this.endDate = endDate;
                this.isCopyData = isCopyData;
            }
        }
    }
}