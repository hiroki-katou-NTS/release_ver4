module nts.uk.pr.view.qmm007.a {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            nts.uk.ui.confirmSave(screenModel.dirtyChecker);
            __viewContext.bind(screenModel);
        });
    });
}