module nts.uk.ui.helpbutton.viewmodel {
    export class ScreenModel {
        enable: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            self.enable = ko.observable(true);
        }
    }
}