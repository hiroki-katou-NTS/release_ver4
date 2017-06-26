module nts.qmm017 {

    export class IScreen {
        listBoxItemType: KnockoutObservable<ListBox>;
        listBoxItems: KnockoutObservable<ListBox>;
        baseYm: KnockoutObservable<any>;

        constructor(root) {
            var self = this;
            self.baseYm = ko.observable(root.viewModel017b().startYearMonth());
            root.viewModel017b().startYearMonth.subscribe(function(yM) {
                self.baseYm(yM);
            });
            var hList001 = [
                { code: '1', name: '全て' },
            ];
            self.listBoxItemType = ko.observable(new ListBox(hList001));
            self.listBoxItems = ko.observable(new ListBox([]));
            self.listBoxItemType().selectedCode.subscribe(function(codeChange) {

                if (codeChange === '1') {
                    self.listBoxItems().itemList([]);
                    let baseYm = 0;
                    if (self.baseYm().indexOf('/') !== -1) {
                        baseYm = self.baseYm().replace('/', '');
                    } else {
                        baseYm = self.baseYm();
                    }
                    service.getListWageTable(baseYm).done(function(lstWageTbl) {
                        _.forEach(lstWageTbl, function(wageTbl: model.WageTableDto) {
                            self.listBoxItems().itemList.push({ code: wageTbl.code, name: wageTbl.name });
                        });
                    });
                }
            });
        }
    }
}