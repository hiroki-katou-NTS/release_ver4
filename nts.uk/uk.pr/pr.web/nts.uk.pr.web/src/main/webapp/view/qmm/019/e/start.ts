__viewContext.ready(function () {
    var screenModel = new qmm019.e.viewmodel.ScreenModel();
    var vm = screenModel;
    screenModel.start().done(function() {
            __viewContext.bind(screenModel);
        });
});