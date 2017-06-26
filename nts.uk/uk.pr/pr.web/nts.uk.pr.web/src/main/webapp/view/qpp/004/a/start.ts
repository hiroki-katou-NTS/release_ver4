__viewContext.ready(function() {
    var screenModel = new qpp004.a.viewmodel.ScreenModel();

    screenModel.startPage().done(function() {
        __viewContext.bind(screenModel);
    });
    
});