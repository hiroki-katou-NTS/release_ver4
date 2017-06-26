module qmm012.k.viewmodel {
    export class ScreenModel {
        taxLimitGridColumns: any;
        taxLimitListItems: KnockoutObservableArray<qmm023.a.service.model.CommuteNoTaxLimitDto> = ko.observableArray([]);
        taxLimitGridCurrentCode: KnockoutObservable<string> = ko.observable('');
        GridlistCurrentItem: KnockoutObservable<qmm023.a.service.model.CommuteNoTaxLimitDto> = ko.observable(null);
        constructor() {
            let self = this;
            self.taxLimitGridColumns = ko.observableArray([
                { headerText: 'コード', prop: 'commuNoTaxLimitCode', width: 40 },
                { headerText: '名称', prop: 'commuNoTaxLimitName', width: 110 },
                { headerText: '限度額', prop: 'commuNoTaxLimitValue', width: 110 }
            ]);
            self.LoadData();
            self.taxLimitGridCurrentCode.subscribe(function(newValue) {
                var item = _.find(self.taxLimitListItems(), function(ItemModel: qmm023.a.service.model.CommuteNoTaxLimitDto) {
                    return ItemModel.commuNoTaxLimitCode == newValue;
                });
                self.GridlistCurrentItem(item);
            })
        }

        LoadData() {
            let self = this;
            service.getCommutelimitsByCompanyCode().done(function(CommuteNoTaxLimits: Array<qmm023.a.service.model.CommuteNoTaxLimitDto>) {
                self.taxLimitListItems(CommuteNoTaxLimits);
                let selectedCode = nts.uk.ui.windows.getShared('commuNoTaxLimitCode');
                if (!selectedCode) {
                    if (CommuteNoTaxLimits.length) {
                        self.taxLimitGridCurrentCode(CommuteNoTaxLimits[0].commuNoTaxLimitCode);
                    } else {
                        nts.uk.ui.dialog.alert("対象データがありません。");
                    }
                } else
                    self.taxLimitGridCurrentCode(selectedCode);
            }).fail(function(res) {
                nts.uk.ui.dialog.alert(res);
            });
        }
        SubmitDialog() {
            let self = this;
            nts.uk.ui.windows.setShared('CommuteNoTaxLimitDto', self.GridlistCurrentItem());
            nts.uk.ui.windows.close();

        }
        CloseDialog() {
            nts.uk.ui.windows.close();
        }
    }
}