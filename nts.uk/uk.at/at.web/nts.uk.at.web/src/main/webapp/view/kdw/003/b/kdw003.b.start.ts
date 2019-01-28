module nts.uk.at.view.kdw003.b {  
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        let param = nts.uk.ui.windows.getShared("paramToGetError");
        let errorValidate = nts.uk.ui.windows.getShared("errorValidate");
        let message = nts.uk.ui.windows.getShared("messageKdw003a");
        screenModel.startPage(param, errorValidate, message).done(function() {
            __viewContext.bind(screenModel);
        });
    });
}