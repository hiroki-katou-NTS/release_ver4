module qrm001.a {
    __viewContext.ready(function() {
        var screenModel = new qrm001.a.viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}