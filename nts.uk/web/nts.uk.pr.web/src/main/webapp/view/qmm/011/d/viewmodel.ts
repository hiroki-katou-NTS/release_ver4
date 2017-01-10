module qmm011.d {
    export module viewmodel {
        export class ScreenModel {
            dsel001: KnockoutObservableArray<any>;
            selectedId: KnockoutObservable<number>;
            enable: KnockoutObservable<boolean>;

            constructor() {
                var self = this;
                self.dsel001 = ko.observableArray([
                    new BoxModel(1, '最新の履歴（N）から引き継ぐ'),
                    new BoxModel(2, '初めから作成する')
                ]);
                self.selectedId = ko.observable(1);
                self.enable = ko.observable(true);
            }
        }

      export class BoxModel {
            id: number;
            name: string;
            constructor(id, name) {
                var self = this;
                self.id = id;
                self.name = name;
            }
        }

    }
}