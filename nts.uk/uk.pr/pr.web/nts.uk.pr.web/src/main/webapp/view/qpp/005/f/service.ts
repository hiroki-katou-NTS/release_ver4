module nts.uk.pr.view.qpp005.f {
    export module service {
        var servicePath = {
            getCommuteNotaxLimit: "pr/proto/paymentdata/findCommuteNotaxLimit/{0}/{1}",
        };

        export function getCommuteNotaxLimit(personId: string, startYearmonth: number): any {
            var dfd = $.Deferred();
            var _path = nts.uk.text.format(servicePath.getCommuteNotaxLimit, personId, startYearmonth);

            nts.uk.request.ajax(_path).done(function(res) {
                dfd.resolve(res);
            }).fail(function(res) {
                dfd.reject(res);
            });

            return dfd.promise();
        }

    }
}