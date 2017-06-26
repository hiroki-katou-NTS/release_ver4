__viewContext.ready(function() {
    let param = nts.uk.ui.windows.getShared('paramFromScreenA');
    var screenModel = new nts.uk.pr.view.qmm017.k.viewmodel.ScreenModelKScreen(param);
    this.bind(screenModel);
});