module qmm005.b {
    // api define in qmm005.ts
    var webapi = common.webapi();
    export module services {
        export function getData(processingNo: number) {
            var dfd = $.Deferred();
            nts.uk.request.ajax(webapi.qmm005b.getdata, { processingNo: processingNo })
                .done(function(resp) {
                    dfd.resolve(resp);
                })
                .fail(function(mess) {
                    dfd.reject(mess);
                });

            return dfd.promise();
        }

        export function updatData(model: any) {
            var dfd = $.Deferred();
            nts.uk.request.ajax(webapi.qmm005b.update, model)
                .done(function(resp) {
                    dfd.resolve(resp);
                })
                .fail(function(mess) {
                    dfd.reject(mess);
                });

            return dfd.promise();
        }

        export function deleteData(model: any) {
            var dfd = $.Deferred();
            nts.uk.request.ajax(webapi.qmm005b.delete, model)
                .done(function(resp) {
                    dfd.resolve(resp);
                })
                .fail(function(mess) {
                    dfd.reject(mess);
                });

            return dfd.promise();
        }
    }
}