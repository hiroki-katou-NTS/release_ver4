module qmm020.b.service {
    //duong dan   
    var paths = {
        getAllotCompanySettingList: "pr/core/allot/findallcompanyallot",
        getLayoutName: "pr/core/allot/findcompanyallotlayoutname/{0}",
        getMaxDate: "pr/core/allot/findcompanyallotmaxdate",
        updateAllotCompanySetting: "pr/core/allot/update",
        insertAllotCompanySetting: "pr/core/allot/insert"
    }
    /** 
     * Get list payment date processing.
     */
    export function getAllotCompanyList(): JQueryPromise<Array<model.CompanyAllotSettingDto>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getAllotCompanySettingList)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    /**
     * Get layout master name 
     */
    export function getAllotLayoutName(stmtCode: string): JQueryPromise<string> {
        var dfd = $.Deferred<any>();
        var _path = nts.uk.text.format(paths.getLayoutName, stmtCode);
        var options = {
            dataType: 'text',
            contentType: 'text/plain'
        };
        nts.uk.request.ajax(_path, undefined, options)
            .done(function(res: string) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }


    /**
     * get Data of Item with Max End date
     */
    export function getAllotCompanyMaxDate(): JQueryPromise<model.CompanyAllotSettingDto> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.getMaxDate)
            .done(function(res: any) {
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
        command.payStmtCode = updateAllotCompanyCommand.payCode();
        command.bonusStmtCode = updateAllotCompanyCommand.bonusCode();
        command.startDate = updateAllotCompanyCommand.startYm();
        command.endDate = updateAllotCompanyCommand.endYm();
        command.historyId = updateAllotCompanyCommand.historyId();

        nts.uk.request.ajax(paths.updateAllotCompanySetting, command)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })  
            .fail(function(res) {
                dfd.reject(res);
            })

        return dfd.promise();
    }


    export function insertComAllot(insertAllotCompanyCommand: any) {
        var dfd = $.Deferred<Array<any>>();
        let command = {} as IAllotCompanyDto;
        command.payStmtCode = insertAllotCompanyCommand.payCode();
        command.bonusStmtCode = insertAllotCompanyCommand.bonusCode();
        command.startDate = insertAllotCompanyCommand.startYm();
        command.endDate = insertAllotCompanyCommand.endYm();
        command.historyId = insertAllotCompanyCommand.historyId();
        nts.uk.request.ajax(paths.insertAllotCompanySetting, command)
            .done(function(res: Array<any>) {
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
    export module model {
        export class CompanyAllotSettingDto {
            paymentDetailCode: string;
            bonusDetailCode: string;
            startDate: number;
            endDate: number;
            historyId: string;
        }
        export class LayoutDto {
            companyCode: string;
            stmtCode: string;
            stmtName: string;
        }
    }

    interface IAllotCompanyDto {
        payStmtCode: string;
        bonusStmtCode: string;
        startDate: number;
        endDate: number;
        historyId: string;
    }
}