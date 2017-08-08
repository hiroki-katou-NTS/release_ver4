module nts.uk.pr.view.ccg007.a {
    export module viewmodel {
        import ContractDto = service.ContractDto;
        export class ScreenModel {
            contractCode: KnockoutObservable<string>;
            password: KnockoutObservable<string>;
            parentLayoutId: KnockoutObservable<string>;
            newTopPageCode: KnockoutObservable<string>;
            newTopPageName: KnockoutObservable<string>;
            isDuplicateCode: KnockoutObservable<boolean>;
            check: KnockoutObservable<boolean>;
            constructor() {
                var self = this;
                self.contractCode = ko.observable('');
                self.password = ko.observable('');
            }
            start(): JQueryPromise<void> {
                var self = this;
                var dfd = $.Deferred<void>();
                dfd.resolve();
                return dfd.promise();
            }
            private AuthContract() {
                var self = this;
                if (!nts.uk.ui.errors.hasError()) {
                    service.submitForm({ contractCode: _.escape(self.contractCode()), password: _.escape(self.password()) }).done(function() {
                        nts.uk.characteristics.remove("contractInfo");
                        nts.uk.characteristics.save("contractInfo", { contractCode: _.escape(self.contractCode()), contractPassword: _.escape(self.password()) });
                        nts.uk.ui.windows.close();
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds });
                    });
                }
            }
        }
    }
}