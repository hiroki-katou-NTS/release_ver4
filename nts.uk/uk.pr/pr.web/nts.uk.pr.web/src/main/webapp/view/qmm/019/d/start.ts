__viewContext.ready(function () {
    var screenModel = new qmm019.d.viewmodel.ScreenModel();
    var vm = screenModel;
    
    screenModel.start().done(function() {
            __viewContext.bind(screenModel);
        });
    //this.bind(vm);
});