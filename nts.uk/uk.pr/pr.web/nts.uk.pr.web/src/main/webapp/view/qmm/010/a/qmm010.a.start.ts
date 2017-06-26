module nts.uk.pr.view.qmm010.a {
    __viewContext.ready(function() {
        var screenModel = new nts.uk.pr.view.qmm010.a.viewmodel.ScreenModel();
        screenModel.startPage().done(function(res: any) {
            __viewContext.bind(res);
        });
    });
}