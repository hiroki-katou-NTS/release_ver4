module nts.uk.ui.tabpanel.viewmodel {
    
    export class ScreenModel {
        tabs: KnockoutObservableArray<NtsTabPanelModel>;
        selectedTab: KnockoutObservable<string>;

        constructor() {
            var self = this;
            self.tabs = ko.observableArray([
                {id: 'tab-1', title: 'Tab Title 1', content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-2', title: 'Tab Title 2', content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-3', title: 'Tab Title 3', content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-4', title: 'Tab Title 4', content: '.tab-content-4', enable: ko.observable(true), visible: ko.observable(true)}
            ]);
            self.selectedTab = ko.observable('tab-2');
        }
    }
}