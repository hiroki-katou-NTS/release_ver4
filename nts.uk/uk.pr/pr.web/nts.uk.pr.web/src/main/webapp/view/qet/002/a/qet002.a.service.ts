module qet002.a {
    export module service {
        var servicePath = {
            printService: "/screen/pr/qet002/generate",
        };
        
        
        // Print Report Service
        export function printService(query: viewmodel.ScreenModel): JQueryPromise<void> {
            var dfd = $.Deferred<void>();
             var accquery = {
                 targetYear: query.targetYear(),
                 isLowerLimit: query.isLowerLimit(),
                 lowerLimitValue: query.lowerLimitValue(),
                 isUpperLimit: query.isUpperLimit(),
                 upperLimitValue: query.upperLimitValue()
             }
            nts.uk.request.exportFile(servicePath.printService, accquery).done(function() {
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            });

            return dfd.promise();
        }
    }
}

