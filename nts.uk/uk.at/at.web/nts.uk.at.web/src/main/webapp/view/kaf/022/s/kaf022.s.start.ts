module nts.uk.at.view.kaf022.s {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            $("#reason-temp").focus();
        });
    });
}