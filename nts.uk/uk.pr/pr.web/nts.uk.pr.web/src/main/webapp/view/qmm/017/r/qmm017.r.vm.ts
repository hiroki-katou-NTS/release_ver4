module nts.qmm017 {
    export class RScreen {
        listBoxItemType: KnockoutObservable<ListBox>;
        listBoxItems: KnockoutObservable<ListBox>;
        startYm: any;

        constructor(root) {
            var self = this;
            self.startYm = ko.observable(root.viewModel017b().startYearMonth());
            root.viewModel017b().startYearMonth.subscribe(function(yMChange) {
                if (yMChange.indexOf('/') !== -1) {
                    self.startYm(yMChange.replace('/', ''));
                } else {
                    self.startYm(yMChange);
                }
            });
            var rList001 = [
                { code: '1', name: '会社単価項目（会社単価＠）' },
                { code: '2', name: '個人単価項目（個人単価＠）' }
            ];
            self.listBoxItemType = ko.observable(new ListBox(rList001));
            self.listBoxItems = ko.observable(new ListBox([]));
            self.listBoxItemType().selectedCode.subscribe(function(codeChange) {
                if (codeChange === '1') {
                    self.listBoxItems().itemList([]);
                    service.getListCompanyUnitPrice(self.startYm()).done(function(lstCompanyUnitPrice: Array<model.CompanyUnitPriceDto>) {
                        _.forEach(lstCompanyUnitPrice, function(companyUnitPrice: model.CompanyUnitPriceDto) {
                            self.listBoxItems().itemList.push({ code: companyUnitPrice.unitPriceCode, name: companyUnitPrice.unitPriceName });
                        });
                    });
                } else if (codeChange === '2') {
                    self.listBoxItems().itemList([]);
                    service.getListPersonalUnitPrice().done(function(lstPersonalUnitPrice: Array<model.PersonalCompanyUnitPriceDto>) {
                        _.forEach(lstPersonalUnitPrice, function(personalUnitPrice: model.PersonalUnitPriceDto) {
                            self.listBoxItems().itemList.push({ code: personalUnitPrice.personalUnitPriceCode, name: personalUnitPrice.personalUnitPriceName });
                        });
                    });
                } 
            });
        }
    }
}