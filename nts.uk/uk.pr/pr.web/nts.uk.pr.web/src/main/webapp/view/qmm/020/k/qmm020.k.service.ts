module qmm020.k.service {
    var paths:any = {
        delAllotCompanySetting: "pr/core/allot/delete",
        updateAllotCompanySetting: "pr/core/allot/update"
    }
    //delete
    export function delComAllot(delAllotCompanyCmd: any) {
        var dfd = $.Deferred<Array<any>>();
        let command = {} as IAllotCompanyDto;
        
        command.payStmtCode = delAllotCompanyCmd.paymentDetailCode;
        command.bonusStmtCode = delAllotCompanyCmd.bonusDetailCode;
        command.startDate = delAllotCompanyCmd.startDate;
        command.endDate = delAllotCompanyCmd.endDate;
        command.historyId = delAllotCompanyCmd.historyId;
    
        nts.uk.request.ajax(paths.delAllotCompanySetting , command)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })

        return dfd.promise();
    }
    
    //Update ALLOT company  
    export function updateComAllot(updateAllotCompanyCommand: any) {
        var dfd = $.Deferred<Array<any>>();
        let command = {} as IAllotCompanyDto;
        debugger;
        command.payStmtCode = updateAllotCompanyCommand.paymentDetailCode;
        command.bonusStmtCode = updateAllotCompanyCommand.bonusDetailCode;
        command.startDate = updateAllotCompanyCommand.startDate;
        command.endDate = updateAllotCompanyCommand.endDate;
        command.historyId = updateAllotCompanyCommand.historyId;

        nts.uk.request.ajax(paths.updateAllotCompanySetting, command)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })  
            .fail(function(res) {
                dfd.reject(res);
            })

        return dfd.promise();
    }
    
    interface IAllotCompanyDto {
        payStmtCode: string;
        bonusStmtCode: string;
        startDate: number;
        endDate: number;
        historyId: string;
    }
}