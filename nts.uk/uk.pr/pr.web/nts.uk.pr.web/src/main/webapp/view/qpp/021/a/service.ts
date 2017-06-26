module nts.uk.pr.view.qpp021.a {
    export module service {
        var servicePath = {
            printService: "/file/paymentdata/print",
        };

        export function print(query: any): JQueryPromise<qpp021.a.viewmodel.PaymentDataResultViewModel> {
            var dfd = $.Deferred<qpp021.a.viewmodel.PaymentDataResultViewModel>();
            new nts.uk.ui.file.FileDownload("/file/paymentdata/print", query).print().done(function() {
                dfd.resolve();
            }).fail(function(res){
                dfd.reject(res);
            });

            return dfd.promise();
        }
    }
}