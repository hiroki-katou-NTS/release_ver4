module cmm013.d.viewmodel {
    export class ScreenModel {
        itemList: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservable<string>;
        enable: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            self.itemList = ko.observableArray([
                new BoxModel("1", '明細書に印字する行'),
                new BoxModel("2", '明細書に印字しない行（この行は印刷はされませんが、値の参照・修正が可能です）')
            ]);
            self.selectedCode = ko.observable("1");
            self.enable = ko.observable(true);
        }

        chooseItem() {
            var self = this;
            nts.uk.ui.windows.setShared('selectedCode', self.selectedCode());
            nts.uk.ui.windows.close();
        }

        closeDialog() {

            nts.uk.ui.windows.close();
        }
    }

    class BoxModel {
        id: string;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }
}
