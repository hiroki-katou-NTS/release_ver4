module nts.uk.at.view.kmk011.g {
    export module service {
        /**
         * define path to service
         */
        var path: any = {
            saveComHist: "at/record/divergence/time/history/companyDivergenceRefTime/save",
            saveWkTypeHist: "at/record/divergence/time/history/workTypeDivergenceRefTime/save",
            findComHistByHistId: "at/record/divergence/time/history/companyDivergenceRefTime/find",
            findWkTypeHistByHistId: "at/record/divergence/time/history/workTypeDivergenceRefTime/find"
        };
        
       export function saveComHist(data: model.CreateComHistoryCommand): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.saveComHist, data);
        }
        
        export function saveWkTypeHist(data: model.CreateWkTypeHistoryCommand): JQueryPromise<any> {
            return nts.uk.request.ajax("at", path.saveWkTypeHist, data);
        }
        
        export function findComHistByHistoryId(historyId: string): JQueryPromise<model.CreateComHistoryCommand> {
            return nts.uk.request.ajax("at", path.findComHistByHistId + "/" + historyId);
        }
        
        export function findWkTypeHistByHistoryId(historyId: string): JQueryPromise<model.CreateWkTypeHistoryCommand> {
            return nts.uk.request.ajax("at", path.findWkTypeHistByHistId + "/" + historyId);
        }
    }
    
    export module model {
        export class CreateComHistoryCommand {
            historyId: string;
            startDate: string;
            endDate: string;
            isCopyData: number;
            
            constructor(historyId: string, startDate: string, endDate: string, isCopyData: number) {
                this.historyId = historyId;
                this.startDate = startDate;
                this.endDate = endDate;
                this.isCopyData = isCopyData;
            }
        }
        
        export class CreateWkTypeHistoryCommand {
            workTypeCodes: string;
            historyId: string;
            startDate: string;
            endDate: string;
            isCopyData: number;
            
            constructor(workTypeCodes: string, historyId: string, startDate: string, endDate: string, isCopyData: number) {
                this.workTypeCodes = workTypeCodes;
                this.historyId = historyId;
                this.startDate = startDate;
                this.endDate = endDate;
                this.isCopyData = isCopyData;
            }
        }
    }
}