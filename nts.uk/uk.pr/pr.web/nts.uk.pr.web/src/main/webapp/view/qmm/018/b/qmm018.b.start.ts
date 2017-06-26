module qmm018.b {
    __viewContext.ready(function() { 
        var screenModel = new qmm018.b.viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}
