__viewContext.ready(function() {
    var param = nts.uk.ui.windows.getShared('param');
    
    var screenModel = new qmm019.f.viewmodel.ScreenModel(param);
    screenModel.start().done(function() {
        __viewContext.bind(screenModel);
    });
});