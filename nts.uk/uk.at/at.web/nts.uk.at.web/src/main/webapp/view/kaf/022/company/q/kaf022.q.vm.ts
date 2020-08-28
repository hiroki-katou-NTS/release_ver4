module nts.uk.at.view.kaf022.q.viewmodel {
    import text = nts.uk.resource.getText;

    export class ScreenModelQ {
        itemListD15: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: text('KAF022_75')},
            {code: 0, name: text('KAF022_82')}
        ]);
        itemListD13: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 1, name: text('KAF022_36')},
            {code: 0, name: text('KAF022_37')}
        ]);
        itemList: KnockoutObservableArray<ItemModel> = ko.observableArray([
            {code: 0, name: "0"},
            {code: 1, name: "1"},
            {code: 2, name: "2"},
            {code: 3, name: "3"},
            {code: 4, name: "4"},
            {code: 5, name: "5"},
            {code: 6, name: "6"},
        ]);

        appReasonDispAtr: KnockoutObservable<number>;
        preExcessAtr: KnockoutObservable<number>;
        atdExcessAtr: KnockoutObservable<number>;
        warningDays: KnockoutObservable<number>;
        dispWorkplaceNameAtr: KnockoutObservable<number>;

        constructor() {
            const self = this;
            self.appReasonDispAtr = ko.observable(0);
            self.preExcessAtr = ko.observable(0);
            self.atdExcessAtr = ko.observable(0);
            self.warningDays = ko.observable(0);
            self.dispWorkplaceNameAtr = ko.observable(0);

            $("#fixed-table-q1").ntsFixedTable({});
            $("#fixed-table-q2").ntsFixedTable({});
        }

        initData(allData: any): void {
            const self = this;
            let data = allData.approvalListDisplaySetting;
            if (data) {
                self.appReasonDispAtr(data.appReasonDispAtr || 0);
                self.preExcessAtr(data.preExcessAtr || 0);
                self.atdExcessAtr(data.atdExcessAtr || 0);
                self.warningDays(data.warningDays || 0);
                self.dispWorkplaceNameAtr(data.dispWorkplaceNameAtr || 0);
            }
        }

        collectData(): any {
            const self = this;
            return {
                appReasonDispAtr: self.appReasonDispAtr(),
                preExcessAtr: self.preExcessAtr(),
                atdExcessAtr: self.atdExcessAtr(),
                warningDays: self.warningDays(),
                dispWorkplaceNameAtr: self.dispWorkplaceNameAtr()
            };
        }

    }

    class ItemModel {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }

}