module qpp009.a.start{
    __viewContext.ready(function () {
        var screenModel = new qpp009.a.viewmodel.ScreenModel();
        screenModel.start().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}