module nts.uk.pr.view.qpp021.b {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
           //nts.uk.ui.confirmSave(screenModel.currentItemDirty);
           // nts.uk.ui.confirmSave(screenModel.items2Dirty);
            __viewContext.bind(screenModel);
        });
    });
}