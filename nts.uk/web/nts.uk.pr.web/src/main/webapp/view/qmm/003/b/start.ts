module qmm003.b.start {
    __viewContext.ready(function() {
        let screenModel = new qmm003.b.viewmodel.ScreenModel() ;
        __viewContext.bind(screenModel);
    });
}