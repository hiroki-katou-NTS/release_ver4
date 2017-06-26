module qmm005.c {
    // api define in qmm005.ts
    var webapi = common.webapi();

    export module services {

        export function getData() {
        }

        export function insertData(model: any) {
            let def = $.Deferred();
            model.vuongDepTrai = '201709';
            nts.uk.request.ajax(webapi.qmm005c.insert, model).done(function(resp) {
                def.resolve(resp);
            });
            return def.promise();
        }
    }
}