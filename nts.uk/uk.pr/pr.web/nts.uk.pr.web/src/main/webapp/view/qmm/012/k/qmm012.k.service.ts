module qmm012.k {
    export module service {
        var paths: any = {
            getListByCompanyCode: "core/commutelimit/find/bycompanycode"
        }
        export function getCommutelimitsByCompanyCode(): JQueryPromise<Array<qmm023.a.service.model.CommuteNoTaxLimitDto>> {
            var dfd = $.Deferred<Array<any>>();
            var _path = paths.getListByCompanyCode;
            nts.uk.request.ajax(_path)
                .done(function(res: Array<any>) {
                    dfd.resolve(res);
                }).fail(function(res) {
                    dfd.reject(res.message);
                })
            return dfd.promise();
        }
    }

}