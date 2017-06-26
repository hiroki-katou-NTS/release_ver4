module qrm009.service {
    var paths: any = {
        exportRetirementPaymentPDF: "/screen/pr/qrm009/saveAsPdf"
    }

    export function exportRetirementPaymentPDF(query): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.exportFile(paths.exportRetirementPaymentPDF, query)
            .done((res) => {
                dfd.resolve(res);
            })
            .fail((res) => {
                dfd.reject(res);
            });
        return dfd.promise();
    }
}