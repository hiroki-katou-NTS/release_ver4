module qrm007.a {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        nts.uk.ui.confirmSave(screenModel.dirty);
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}
