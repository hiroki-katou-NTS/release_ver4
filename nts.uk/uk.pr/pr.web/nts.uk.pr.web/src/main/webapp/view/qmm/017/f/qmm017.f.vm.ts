module nts.qmm017 {

    export class FScreen {
        listBoxItemType: KnockoutObservable<ListBox>;
        listBoxItems: KnockoutObservable<ListBox>;
        baseYm: KnockoutObservable<any>;

        constructor() {
            var self = this;
            var hList001 = [
                { code: '1', name: '全て' },
            ];
            self.listBoxItemType = ko.observable(new ListBox(hList001));
            self.listBoxItems = ko.observable(new ListBox([]));
            self.listBoxItemType().selectedCode.subscribe(function(codeChange) {
                if (codeChange === '1') {
                    service.getListSystemVariable().done(function(lstItem: Array<model.SystemVariableDto>) {
                        self.listBoxItems().itemList([]);
                        _.forEach(lstItem, function(item: model.SystemVariableDto) {
                            self.listBoxItems().itemList.push({ code: item.systemVariableCode, name: item.systemVariableName });
                        });
                    });
                }
            });
        }
    }
}