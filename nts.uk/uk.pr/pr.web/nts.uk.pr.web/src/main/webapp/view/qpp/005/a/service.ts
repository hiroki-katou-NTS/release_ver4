module nts.uk.pr.view.qpp005.a {
    export module service {
        var servicePath = {
            getPaymentData: "/screen/pr/qpp005/paymentdata/find",
            insertData: "pr/proto/paymentdata/insert",
            updateData: "pr/proto/paymentdata/update",
            remove: "pr/proto/paymentdata/remove"
        };

        export function getPaymentData(personId: string, employeeCode: string): JQueryPromise<qpp005.a.viewmodel.PaymentDataResultViewModel> {
            var dfd = $.Deferred<qpp005.a.viewmodel.PaymentDataResultViewModel>();
            var query = {
                personId: personId,
                employeeCode: employeeCode
            };
            nts.uk.request.ajax(servicePath.getPaymentData, query).done(function(res: qpp005.a.viewmodel.PaymentDataResultViewModel) {
                dfd.resolve(res);
            }).fail(function(res) {
               dfd.reject(res);
            });

            return dfd.promise();
        }

        export function register(employee: qpp005.a.viewmodel.Employee, paymentData: qpp005.a.viewmodel.PaymentDataResultViewModel): any {
            var result = {
                paymentHeader: ko.toJS(paymentData.paymentHeader),
                categories: ko.toJS(paymentData.categories)
            };
            result.paymentHeader.personName = employee.name;
            var isCreated = result.paymentHeader.created;
            if (!isCreated) {
                return nts.uk.request.ajax(servicePath.insertData, result);
            }

            return nts.uk.request.ajax(servicePath.updateData, result);
        }
        
        export function remove(personId, processingYM): any {
            var result = {
                personId: personId,
                processingYM: processingYM
            };

            return nts.uk.request.ajax(servicePath.remove, result);
        }
    }
}