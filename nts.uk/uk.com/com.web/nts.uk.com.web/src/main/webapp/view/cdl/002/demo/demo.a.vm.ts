module demo.a.viewmodel {
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    export class ScreenModel {
        selectedItem: KnockoutObservable<any>;
        isMultiSelect: KnockoutObservable<boolean>;
        isDisplayUnselect: KnockoutObservable<boolean>;
        selectedCode: KnockoutObservable<string>;
        selectedCodes: KnockoutObservableArray<string>;
        
        selectionOption: KnockoutObservableArray<any>;
        selectedOption: KnockoutObservable<number>;
        constructor() {
            var self = this;
            self.isMultiSelect = ko.observable(true);
            self.selectedCodes = ko.observableArray(['01', '04']);
            self.selectedCode = ko.observable('02');
            self.selectedItem = ko.observable(self.isMultiSelect() ? self.selectedCodes() : self.selectedCode());
            self.isDisplayUnselect = ko.observable(false);
            self.isMultiSelect.subscribe(function(data) {
                if (data) {
                    if (self.isDisplayUnselect()) {
                        self.isDisplayUnselect(false);
                    }
                    self.selectedItem(self.selectedCodes());
                } else {
                    self.selectedItem(self.selectedCode());
                }
            });
            
            self.isDisplayUnselect.subscribe(function(data) {
                if (data && self.isMultiSelect()) {
                    nts.uk.ui.dialog.alert("Displaying Unselect Item is not available for Multiple Selection!");
                    self.isDisplayUnselect(false);
                }
            })
            
            
            self.selectionOption = ko.observableArray([
                {code : 0, name: 'Single Selection'},
                {code : 1, name: 'Multiple Selection'},
            ]);
            self.selectedOption = ko.observable(self.isMultiSelect() ? 1 : 0);
            self.selectedOption.subscribe(function(data: number) {
                if (data == 0) {
                    self.isMultiSelect(false);
                }
                else {
                    self.isMultiSelect(true);
                }
            });
        }
        // Open Dialog CDL002
        private openDialog() {
            let self = this;
            setShared('CDL002Params', {
                isMultiple: self.isMultiSelect(),
                selectedCodes: self.selectedItem(),
                showNoSelection: self.isDisplayUnselect(),
            }, true);
            
            nts.uk.ui.windows.sub.modal("/view/cdl/002/a/index.xhtml").onClosed(function() {
                var isCancel = getShared('CDL002Cancel');
                if (isCancel) {
                    return;
                }
                var output = getShared('CDL002Output');
                self.selectedItem(output);
            });
        }
        
    }
}