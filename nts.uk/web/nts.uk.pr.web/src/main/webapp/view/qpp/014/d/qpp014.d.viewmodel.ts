// TreeGrid Node
module qpp014.d.viewmodel {
    export class ScreenModel {
        countItems: KnockoutObservable<number>;
        processingDate: KnockoutObservable<string>;
        d_SEL_001_selectedCode: KnockoutObservable<any>;
        d_SEL_002_selectedCode: KnockoutObservable<any>;
        d_LST_001_items: KnockoutObservableArray<any>;
        d_LST_001_itemSelected: KnockoutObservable<any>;
        d_nextScreen: KnockoutObservable<string>;
        transferDate: KnockoutObservable<string>;
        d_lbl_015: KnockoutObservable<string>;
        d_lbl_016: KnockoutObservable<string>;

        constructor(data: any) {
            let self = this;
            self.transferDate = ko.observable('2016/12/12');
            self.d_SEL_001_selectedCode = ko.observable(1);
            self.d_SEL_002_selectedCode = ko.observable(1);
            self.d_LST_001_items = ko.observableArray([]);
            for (let i = 1; i < 31; i++) {
                self.d_LST_001_items.push(({ code: '00' + i, name: '基本給' + i, description: ('description' + i) }));
            }
            self.countItems = ko.observable(self.d_LST_001_items().length);
            self.d_LST_001_itemSelected = ko.observable(0);
            self.d_nextScreen = ko.computed(function() {
                //check value of D_SEL_002 to jump to screen G or H after click E_BTN_002
                return self.d_SEL_002_selectedCode() == 2 ? 'screen_g' : 'screen_h';
            });

            self.processingDate = ko.observable(nts.uk.time.formatYearMonth(data.currentProcessingYm));
            self.d_lbl_015 = ko.observable('( ' + data.processingNo + ' : ');
            self.d_lbl_016 = ko.observable(data.processingName + ' )');
        }

        openEDialog() {
            var self = this;
            nts.uk.ui.windows.setShared("processingDate", self.processingDate(), true);
            nts.uk.ui.windows.setShared("transferDate", self.transferDate(), true);
            nts.uk.ui.windows.sub.modal("/view/qpp/014/e/index.xhtml", { title: "振込データの作成結果一覧", dialogClass: "no-close" }).onClosed(function() {
                //if close button, not next screen
                if (!nts.uk.ui.windows.getShared("closeDialog")) {
                    $('#wizard').ntsWizard("next");
                }
            });
        }
    }
};
