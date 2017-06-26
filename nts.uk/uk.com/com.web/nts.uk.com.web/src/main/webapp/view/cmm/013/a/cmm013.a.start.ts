module cmm013.a {
    __viewContext.ready(function() {
        var screenModel = new cmm013.a.viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);  
        });      
    });
}