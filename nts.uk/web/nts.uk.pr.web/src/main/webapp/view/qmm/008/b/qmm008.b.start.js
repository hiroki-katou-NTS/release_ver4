__viewContext.ready(function () {
    var screenModel = new nts.uk.pr.view.qmm008.b.viewmodel.ScreenModel();
    $.when(screenModel.startPage()).done(function () {
        __viewContext.bind(screenModel);
    });
});
//# sourceMappingURL=qmm008.b.start.js.map