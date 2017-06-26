module qmm020.a.service {
        //duong dan   
        var paths = {
            getAllotCompanySettingList: "pr/core/allot/findallcompanyallot"
        }
        
        /**
         * Get list allot company
         */
        export function getAllotCompanyList(): JQueryPromise<Array<model.CompanyAllotSettingDto>> {
            var dfd = $.Deferred<Array<any>>();
            nts.uk.request.ajax(paths.getAllotCompanySettingList)
                .done(function(res: Array<any>){
                    dfd.resolve(res);
                })
                .fail(function(res) {
                    dfd.reject(res);
                })
            return dfd.promise(); 
        }
         
        /**
         * 
         * 
         */
        export module model{
         export class CompanyAllotSettingDto {
                    companyCode: string;
                    historyId: string;
                    startYM: number;
                    endYM:number;
                    bonusStmtCode : string;
                    payStmtCode:string;
                }
        
        }
}