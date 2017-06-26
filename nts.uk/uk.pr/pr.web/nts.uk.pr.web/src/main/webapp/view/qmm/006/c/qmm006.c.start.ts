module qmm006.c {
    __viewContext.ready(function() {
        var screenModel = new qmm006.c.viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}