module nts.qmm017 {

    export class GScreen {
        listBoxItemType: KnockoutObservable<ListBox>;
        listBoxItems: KnockoutObservable<ListBox>;
        baseYm: KnockoutObservable<any>;

        constructor() {
            var self = this;
            self.listBoxItemType = ko.observable(new ListBox([]));
            self.listBoxItems = ko.observable(new ListBox([]));
            self.listBoxItemType().selectedCode.subscribe(function(codeChange) {

            });
        }
    }
}