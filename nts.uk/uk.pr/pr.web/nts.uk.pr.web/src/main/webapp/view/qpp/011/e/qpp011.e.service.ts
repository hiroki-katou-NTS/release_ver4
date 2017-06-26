module qpp011.e.service {
    var paths = {
        findAllResidential: "pr/core/residential/findallresidential",
    }

    /**
     * Get list payment date processing.
     */

    export function findAllResidential(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.findAllResidential)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
}