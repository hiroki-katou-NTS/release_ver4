module nts.uk.at.view.knr002.a {
    
    __viewContext.ready(function() {
        var screenModel = new nts.uk.at.view.knr002.a.viewmodel.ScreenModel(); 
        screenModel.startPage().done(() => {
            __viewContext.bind(screenModel);
        });        
    });
}