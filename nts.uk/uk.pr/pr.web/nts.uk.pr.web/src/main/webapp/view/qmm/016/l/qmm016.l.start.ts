module nts.uk.pr.view.qmm016.l {
    __viewContext.ready(function() {
        var screenModel = new nts.uk.pr.view.qmm016.l.viewmodel.ScreenModel();
        screenModel.startPage().done(function(res: any) {
            __viewContext.bind(res);
        });
    });
}