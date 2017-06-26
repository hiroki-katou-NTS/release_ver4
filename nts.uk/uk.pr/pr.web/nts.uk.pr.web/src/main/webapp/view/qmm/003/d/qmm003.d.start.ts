module qmm003.d.start {
    __viewContext.ready(function() {
        let screenModel = new qmm003.d.viewmodel.ScreenModel() ;
       screenModel.start().done(function(){
             __viewContext.bind(screenModel);
       });
    });    
    }