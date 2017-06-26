/// <reference path="../qmm005.ts"/>
module qmm005.e {
    // api define in qmm005.ts
    var webapi = common.webapi();
    export module services {
        export function getData(processingNo: number) {
            let def = $.Deferred();
            nts.uk.request.ajax(webapi.qmm005d.getdata, { processingNo: processingNo }).done(function(resp) {
                def.resolve(resp);
            });
            return def.promise();
        }

        export function updateData(model: any) {
            let def = $.Deferred();
            nts.uk.request.ajax(webapi.qmm005d.update, model).done(function(resp) {
                def.resolve(resp);
            });
            return def.promise();
        }
    }
}