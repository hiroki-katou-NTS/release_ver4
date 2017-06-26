module nts.uk.pr.view.qmm008.h {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel(nts.uk.ui.windows.getShared("officeName"),nts.uk.ui.windows.getShared("healthModel"));
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}