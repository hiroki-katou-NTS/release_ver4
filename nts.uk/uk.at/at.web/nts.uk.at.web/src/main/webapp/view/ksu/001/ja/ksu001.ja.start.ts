module ksu001.ja {
    import getShare = nts.uk.ui.windows.getShared;

    let __viewContext: any = window["__viewContext"] || {};
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        __viewContext.bind(screenModel);
    });
}