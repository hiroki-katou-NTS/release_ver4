__viewContext.ready(function () {
    class ScreenModel {
        ResidentCode: KnockoutObservable<string>;
        
        constructor() {
            this.ResidentCode = ko.observable('abc');
        }
        setError() {
            $("#text").ntsError("set", "Errors."); 
        }
        
        clearError() {
             $("#text").ntsError("clear"); 
        }
        
        closeDialog() {
             nts.uk.ui.windows.close();   
        }
        
        positionHis() {
             nts.uk.ui.windows.close();   
        }
    }
    
    this.bind(new ScreenModel());
});
