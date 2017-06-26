module qmm020.c.service {
    //duong dan   
    var paths = {
        getEmployAllotSettingHeaderList: "pr/core/allot/findallemployeeallotheader",
        getEmployAllotSettingDetailList: "pr/core/allot/findallemployeeallotdetail",
        getAllEmployeeAllotSettingList: "pr/core/allot/findAllEmployeeAllotSettingList/{0}",
        getMaxDate: "pr/core/allot/findallemployeeallotheaderMax",
        insertAllotEmployeeSetting: "pr/core/allot/insertAllEmployeeSetting",
        getEmployeeDetail: "pr/core/allot/findEmployeeDetail/{0}",
        getEmployeeName: "basic/organization/employment/findallemployments",
        getLayoutName: "pr/core/allot/findcompanyallotlayoutname/{0}"
    }
    /**
     * Get list payment date processing.
     */
    export function getEmployeeAllotHeaderList(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getEmployAllotSettingHeaderList)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    /**
     * Get employee list with payment doc, bunus doc
     */
    export function getEmployeeAllotDetailList(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getEmployAllotSettingDetailList)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function getAllEmployeeAllotSetting(histId: string): JQueryPromise<Array<any>> {
        let dfd = $.Deferred<Array<any>>();
        let _path = nts.uk.text.format(paths.getAllEmployeeAllotSettingList, histId);
        nts.uk.request.ajax("pr", _path).done(function(res: Array<any>) {
            dfd.resolve(res);
        }).fail(function(error) {
            dfd.reject(error);
        })
        return dfd.promise();
    }
    export function getEmployeeName(): JQueryPromise<Array<any>> {
        let dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.getEmployeeName)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function getEmployeeDetail(histId: string): JQueryPromise<Array<any>> {
        let dfd = $.Deferred<Array<any>>();
        let _path = nts.uk.text.format(paths.getEmployeeDetail, histId);
        nts.uk.request.ajax(_path)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function getAllotEmployeeMaxDate(): JQueryPromise<any> {
        let dfd = $.Deferred<any>();
        let _path = nts.uk.text.format(paths.getMaxDate);

        nts.uk.request.ajax(_path).done(function(res: any) {
            dfd.resolve(res);
        })
            .fail(function(error) {
                dfd.reject(error);
            })
        return dfd.promise();
    }
    export function insertAllotEm(insertAllotEmployeeCommand: any) {
        var dfd = $.Deferred<Array<any>>();
        let command = {} as IEmployeeModel;
        command.historyId = insertAllotEmployeeCommand.historyId;
        command.employeeCode = insertAllotEmployeeCommand.employeeCode;
        command.paymentDetailCode = insertAllotEmployeeCommand.paymentDetailCode;
        command.bonusDetailCode = insertAllotEmployeeCommand.bonusDetailCode;
        command.startYm = insertAllotEmployeeCommand.startYm;
        command.endYm = insertAllotEmployeeCommand.endYm;
        nts.uk.request.ajax(paths.insertAllotEmployeeSetting, command)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })

    }
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
    interface IEmployeeModel {
        companyCode?: string;
        historyId?: string;

        employeeCode?: string;
        employeeName?: string;
        paymentDetailCode?: string;
        paymentDetailName?: string;
        bonusDetailCode?: string;
        bonusDetailName?: string;
        startYm?: string;
        endYm?: string;
        startEnd?: string;
    }

}
