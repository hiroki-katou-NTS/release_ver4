module nts.uk.pr.view.qmm011.a.start {
    __viewContext.ready(function() {
        var screenModel = new nts.uk.pr.view.qmm011.a.viewmodel.ScreenModel();
        screenModel.startPage().done(function(res: any) {
            __viewContext.bind(res);
            screenModel.dirtyUnemployeeInsurance.reset();
            screenModel.dirtyAccidentInsurance.reset();
        });
    });
}