module nts.uk.at.view.ksu006.a {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            
            // fixbug in IE10
            $('#comboExternalBudget').on('mousedown', function() {
                $('#comboExternalBudget').focus();
            });
        });
    });
}