module nts.uk.pr.view.qmm017.e {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            $('#E5_1').focus();
        });
    });
}