__viewContext.ready(function() {
        var param = nts.uk.ui.windows.getShared('paramFromScreenL');
        var screenModel = new nts.uk.pr.view.qmm017.p.viewmodel.ScreenModel(param);
        this.bind(screenModel);
});