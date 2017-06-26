__viewContext.ready(function () {
    class ScreenModel {
        modalValue: KnockoutObservable<string>;
        isTransistReturnData: KnockoutObservable<boolean>;
        numbereditor2: any;
        
        constructor() {
            var self = this;
            self.modalValue = ko.observable("Goodbye world!");
            self.isTransistReturnData = ko.observable(nts.uk.ui.windows.getShared("isTransistReturnData"));
            self.numbereditor2 = {
                value: ko.observable(12),
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.NumberEditorOption({
                    grouplength: 3,
                    decimallength: 2,
                    symbolChar: '%',
                    symbolPosition: 'right'})),
                required: ko.observable(false),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            // Reset child value
//            nts.uk.ui.windows.setShared("childValue", null);
        }
        
        
        CloseModalSubWindow() {
            // Set child value
            nts.uk.ui.windows.setShared("childValue", this.modalValue(), this.isTransistReturnData());
            nts.uk.ui.windows.close();
        }
    }
    
    // Get parent value
    $("#parentInstruct").text("My parent say: " + nts.uk.ui.windows.getShared("parentValue"));
    
    this.bind(new ScreenModel());
});
