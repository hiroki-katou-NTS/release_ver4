module nts.uk.at.view.kmk015.b {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            $("#inp-period-startYMD").focus();
        });
    });
}