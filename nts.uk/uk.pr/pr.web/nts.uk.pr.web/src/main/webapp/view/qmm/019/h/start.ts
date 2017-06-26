
__viewContext.ready(function () {
  
    var screenModel = new qmm019.h.viewmodel.ScreenModel();
    screenModel.start().done(function() {
        __viewContext.bind(screenModel);
    });
    //this.bind(screenModel);
});