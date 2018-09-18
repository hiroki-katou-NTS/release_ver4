module nts.uk.pr.view.qmm012.k.viewmodel {
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;
    import setShared = nts.uk.ui.windows.setShared;

    export class ScreenModel {
        listTaxExemptLimit: KnockoutObservableArray<TaxExemptLimit> = ko.observableArray([]);
        selectedTaxExemptLimit: KnockoutObservable<TaxExemptLimit> = ko.observable(null);
        currentCode: KnockoutObservable<string> = ko.observable('');
        constructor() {
            let self = this;

            self.currentCode.subscribe(taxFreeamountCode => {
                let getTaxExemptLimit = _.find(self.listTaxExemptLimit(), function(x) { return x.taxFreeamountCode == taxFreeamountCode; });
                self.selectedTaxExemptLimit(getTaxExemptLimit);
            });
            service.getAllTaxAmountByCompanyId().done(function(data: Array<TaxExemptLimit>) {
                if (data && data.length > 0) {
                    let dataSort = _.sortBy(data, ["taxFreeamountCode"]);
                    self.listTaxExemptLimit(dataSort);
                    self.currentCode(self.listTaxExemptLimit()[0].taxFreeamountCode);
                }
            }).fail(function(error) {
                alertError(error);
            }).always(() => {
            });
        }

        setTaxExemption() {
            let self = this;
            if (self.selectedTaxExemptLimit()) {
                setShared("QMM012_TaxExemptLimit", self.selectedTaxExemptLimit(null));
            } else {
                setShared("QMM012_TaxExemptLimit", self.selectedTaxExemptLimit());
            }
            nts.uk.ui.windows.close();
        }

        cancelSetting() {
            nts.uk.ui.windows.close();
        }
    }
    class TaxExemptLimit {
        taxFreeamountCode: string;
        taxExemptionName: string;
        taxExemption: number;

        constructor(taxFreeamountCode: string, taxExemptionName: string, taxExemption: number) {
            this.taxFreeamountCode = taxFreeamountCode;
            this.taxExemptionName = taxExemptionName;
            this.taxExemption = taxExemption;
        }
    }
}