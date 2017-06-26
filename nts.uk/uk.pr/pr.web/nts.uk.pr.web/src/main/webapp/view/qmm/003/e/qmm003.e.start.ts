module qmm003.e.start {
    __viewContext.ready(function() {
        let screenModel = new qmm003.e.viewmodel.ScreenModel() ;
        screenModel.start().done(function(){
             __viewContext.bind(screenModel);
        });
       
    });    
    }