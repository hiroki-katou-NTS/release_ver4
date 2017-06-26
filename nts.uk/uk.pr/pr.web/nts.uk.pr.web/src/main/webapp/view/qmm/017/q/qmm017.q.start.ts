__viewContext.ready(function() {
        var param = nts.uk.ui.windows.getShared('formulaManual');
        var screenModel = new nts.uk.pr.view.qmm017.q.viewmodel.ScreenModel(param);
        this.bind(screenModel);
});