module nts.uk.pr.view.qmm008.i {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel(nts.uk.ui.windows.getShared("officeName"),nts.uk.ui.windows.getShared("pensionModel"));
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}