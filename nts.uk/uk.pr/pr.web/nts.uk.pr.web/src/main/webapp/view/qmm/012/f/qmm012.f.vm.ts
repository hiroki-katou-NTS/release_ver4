module qmm012.f.viewmodel {
    export class ScreenModel {
        //F 001
        roundingRules_ZeroDisplay: KnockoutObservableArray<any>;
        CurrentItemMaster: KnockoutObservable<qmm012.b.service.model.ItemMaster> = ko.observable(null);
        CurrentZeroDisplaySet: KnockoutObservable<number> = ko.observable(1);
        checked_NoDisplay: KnockoutObservable<boolean> = ko.observable(false);
        CurrentItemDisplayAtr: KnockoutObservable<number> = ko.observable(1);
        noDisplayNames_Enable: KnockoutObservable<boolean> = ko.observable(false);
        constructor() {
            let self = this;
            //F_001
            self.roundingRules_ZeroDisplay = ko.observableArray([
                { code: 1, name: 'ゼロを表示する' },
                { code: 0, name: 'ゼロを表示しない' }
            ]);

            self.checked_NoDisplay.subscribe(function(NewValue: boolean) {
                self.CurrentItemDisplayAtr(NewValue ? 0 : 1);
            });
            self.CurrentZeroDisplaySet.subscribe(function(newValue) {
                if (newValue == 0) {
                    self.noDisplayNames_Enable(true);
                } else {
                    self.noDisplayNames_Enable(false);
                }
            });
        }
        loadData(itemMaster: qmm012.b.service.model.ItemMaster): JQueryPromise<any> {
            let self = this;
            var dfd = $.Deferred<any>();
            self.CurrentZeroDisplaySet(itemMaster ? itemMaster.zeroDisplaySet : 1);
            self.checked_NoDisplay(itemMaster ? itemMaster.itemDisplayAtr == 0 ? true : false : false);
            dfd.resolve();
            return dfd.promise();
        }
    }
}