module kmk011.a {
    __viewContext.ready(function() {
        var screenModel = new kmk011.a.viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        }); 
    });
}