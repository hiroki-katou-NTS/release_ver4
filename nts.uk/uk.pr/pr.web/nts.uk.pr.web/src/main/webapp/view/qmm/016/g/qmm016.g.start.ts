module nts.uk.pr.view.qmm016.g {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            $("#G1_1").focus();
        });
    });
}