module qmm018.a {
    __viewContext.ready(function() {
        var screenModel = new qmm018.a.viewmodel.ScreenModel();
        nts.uk.ui.confirmSave(screenModel.dirty);
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}
