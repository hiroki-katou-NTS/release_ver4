module qmm020.j.viewmodel {
    import option = nts.uk.ui.option;

    export class ScreenModel {
        displayMode: KnockoutObservable<number> = ko.observable(1);
        startYm: KnockoutObservable<number> = ko.observable(197001);
        endDate: KnockoutObservable<Date> = ko.observable(undefined);
        baseDate: KnockoutObservable<Date> = ko.observable(undefined);
        startDate: KnockoutObservable<Date> = ko.observable(undefined);
        selectedMode: KnockoutObservable<number> = ko.observable(1);
        txtCopyHistory: KnockoutObservable<string> = ko.observable(undefined);
        constructor() {
            var self = this;
            // display mode
            self.displayMode(nts.uk.ui.windows.getShared('J_MODE') || 1);
            self.startYm(nts.uk.ui.windows.getShared("J_BASEDATE") || 197001);

            // resize window
            self.displayMode.subscribe((v) => {
                nts.uk.ui.windows.getSelf().setWidth(490);
                if (v == 2) {
                    nts.uk.ui.windows.getSelf().setHeight(420);
                } else {
                    nts.uk.ui.windows.getSelf().setHeight(300);
                }
            });

            // trigger resize window
            self.displayMode.valueHasMutated();

            // start first method
            self.start();
        }


        start() {
            let self = this;           

            self.txtCopyHistory("最新の履歴（" + nts.uk.time.formatYearMonth(self.startYm()) + "）から引き継ぐ");
        }

        createHistoryDocument() {
            let self = this;
            
            self.closeDialog();
        }

        closeDialog() { nts.uk.ui.windows.close(); }
    }

    enum Error {
        ER023 = <any>"履歴の期間が重複しています。"
    }
}