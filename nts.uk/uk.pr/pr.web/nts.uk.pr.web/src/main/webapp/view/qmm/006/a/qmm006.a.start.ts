module qmm006.a {
    __viewContext.ready(function() {
        var screenModel = new qmm006.a.viewmodel.ScreenModel();

        screenModel.startPage().done(function() {
            nts.uk.ui.confirmSave(screenModel.dirty);
            __viewContext.bind(screenModel);
        });
    });
}