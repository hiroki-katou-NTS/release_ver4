module qmm034.a {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            nts.uk.ui.confirmSave(screenModel.dirtyObject);
            __viewContext.bind(screenModel);
            screenModel.dirtyObject.reset();
        });
    });
}