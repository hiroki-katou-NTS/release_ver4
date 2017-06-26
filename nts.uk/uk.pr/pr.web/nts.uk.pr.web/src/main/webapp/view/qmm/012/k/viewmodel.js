var qmm012;
(function (qmm012) {
    var k;
    (function (k) {
        var viewmodel;
        (function (viewmodel) {
            class ScreenModel {
                constructor() {
                    this.GridlistItems_K_001 = ko.observableArray([]);
                    this.GridlistCurrentCode_K_001 = ko.observable('');
                    this.GridlistCurrentItem = ko.observable(null);
                    let self = this;
                    self.GridColumns_K_001 = ko.observableArray([
                        { headerText: 'コード', prop: 'commuNoTaxLimitCode', width: 40 },
                        { headerText: '名称', prop: 'commuNoTaxLimitName', width: 110 },
                        { headerText: '限度額', prop: 'commuNoTaxLimitValue', width: 110 }
                    ]);
                    self.LoadData();
                    self.GridlistCurrentCode_K_001.subscribe(function (newValue) {
                        var item = _.find(self.GridlistItems_K_001(), function (ItemModel) {
                            return ItemModel.commuNoTaxLimitCode == newValue;
                        });
                        self.GridlistCurrentItem(item);
                    });
                }
                LoadData() {
                    let self = this;
                    k.service.getCommutelimitsByCompanyCode().done(function (CommuteNoTaxLimits) {
                        self.GridlistItems_K_001(CommuteNoTaxLimits);
                        let selectedCode = nts.uk.ui.windows.getShared('commuNoTaxLimitCode');
                        if (!selectedCode) {
                            if (CommuteNoTaxLimits.length) {
                                self.GridlistCurrentCode_K_001(CommuteNoTaxLimits[0].commuNoTaxLimitCode);
                            }
                        }
                        else
                            self.GridlistCurrentCode_K_001(selectedCode);
                    }).fail(function (res) {
                        alert(res);
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
            viewmodel.ScreenModel = ScreenModel;
        })(viewmodel = k.viewmodel || (k.viewmodel = {}));
    })(k = qmm012.k || (qmm012.k = {}));
})(qmm012 || (qmm012 = {}));
