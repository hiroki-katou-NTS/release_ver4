module nts.uk.pr.view.qmm040.a {
    __viewContext.ready(function () {
        __viewContext['screenModel'] = new viewmodel.ScreenModel();
        __viewContext['screenModel'].startPage().done(function () {
            __viewContext.bind(__viewContext['screenModel']);
        });
    });
}