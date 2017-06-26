module qmm020.m.service {
    let paths = {
        getData: 'pr/core/allotlayouthist/getdata'
    };

    export function getData(baseYm: number): JQueryPromise<Array<any>> {
        // set default Yearmonth
        baseYm = baseYm || 197001;

        var dfd = $.Deferred();
        nts.uk.request.ajax(paths.getData + "/" + baseYm, { baseYm: baseYm })
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            });
        
        return dfd.promise();
    }
}