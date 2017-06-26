module qmm005.a {
    // api define in qmm005.ts
    var webapi = common.webapi();

    export module services {
        export function getData() {
            var dfd = $.Deferred();
            nts.uk.request.ajax(webapi.qmm005a.getdata)
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
            nts.uk.request.ajax(webapi.qmm005a.update, model)
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