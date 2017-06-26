module qmm003.c.start {
    __viewContext.ready(function() {
        let screenModel = new qmm003.c.viewmodel.ScreenModel() ;
        screenModel.start().done(function(){
            __viewContext.bind(screenModel);
        });
        
    });    
    }