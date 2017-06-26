module qmm023.a.service {
    var paths: any = {
        getListByCompanyCode: "core/commutelimit/find/bycompanycode",
        insertData: "core/commutelimit/insert",
        updateData: "core/commutelimit/update",
        deleteData: "core/commutelimit/delete"
    }

    export function getCommutelimitsByCompanyCode(): JQueryPromise<Array<model.CommuteNoTaxLimitDto>> {
        let dfd = $.Deferred<Array<any>>();
        let _path = paths.getListByCompanyCode;
        nts.uk.request.ajax(_path)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            }).fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function insertUpdateData(isUpdate: any, commuteNoTaxLimitCommand: any): JQueryPromise<Array<any>> {
        let dfd = $.Deferred<Array<any>>();
        let _path = isUpdate ? paths.updateData : paths.insertData;
        let command = ko.toJS(commuteNoTaxLimitCommand);
        nts.uk.request.ajax(_path, command)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function deleteData(deleteCode: any): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.deleteData, ko.toJS(deleteCode))
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res: any) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export module model {
        // layout
        export class CommuteNoTaxLimitDto {
            companyCode: string;
            commuNoTaxLimitCode: string;
            commuNoTaxLimitName: string;
            commuNoTaxLimitValue: number;

            constructor(companyCode: string, commuNoTaxLimitCode: string, commuNoTaxLimitName: string,
                commuNoTaxLimitValue: number) {
                this.companyCode = companyCode;
                this.commuNoTaxLimitCode = commuNoTaxLimitCode;
                this.commuNoTaxLimitName = commuNoTaxLimitName;
                this.commuNoTaxLimitValue = commuNoTaxLimitValue;
            }
        }

    }

}