module qmm003.a.start {
    __viewContext.ready(function() {
        let screenModel = new qmm003.a.viewmodel.ScreenModel();
        screenModel.start(undefined).done(function() {
            nts.uk.ui.confirmSave(screenModel.dirtyObject);
            __viewContext.bind(screenModel);
        });
    });
}