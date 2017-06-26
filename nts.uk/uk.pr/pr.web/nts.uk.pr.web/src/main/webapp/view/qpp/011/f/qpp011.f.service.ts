module qpp011.f.service {
    var paths = {
        findAllResidential: "pr/core/residential/findallresidential",
        getlistLocation: "pr/core/residential/getlistLocation",
        saveAsPdfB: "screen/pr/QPP011/saveAsPdfB",
        saveAsPdfC: "screen/pr/QPP011/saveAsPdfC",
        saveText: "screen/pr/QPP011/savePaymentData"
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
    export function getlistLocation(): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<any>();
        nts.uk.request.ajax(paths.getlistLocation)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function saveAsPdf(check: any,command: any): JQueryPromise<any> {
        if(check == 1) {
           var path  = paths.saveAsPdfB;
        } else {
           var path  = paths.saveAsPdfC;
        }
        return nts.uk.request.exportFile(path, command);
    }
    
    export function saveText(command: any): JQueryPromise<any> {
        var path  = paths.saveText;

        return nts.uk.request.exportFile(path, command);
    }
}