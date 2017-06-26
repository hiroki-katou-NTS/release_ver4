__viewContext.ready(function() {
    var screenModel = new nts.uk.pr.view.qmm008.c.viewmodel.ScreenModel();
    $.when(screenModel.startPage()).done(function() {
        __viewContext.bind(screenModel);
        screenModel.dirty.reset();
    });
});