__viewContext.ready(function() {
    let param = nts.uk.ui.windows.getShared('paramFromScreenC');
    var screenModel = new nts.uk.pr.view.qmm017.l.viewmodel.ScreenModelLScreen(param);
    screenModel.start().done(function() {
        __viewContext.bind(screenModel);
        $("#easy-formula-name").focus();
    });
});