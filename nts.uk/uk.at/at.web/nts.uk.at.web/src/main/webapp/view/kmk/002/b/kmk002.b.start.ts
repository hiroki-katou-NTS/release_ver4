module nts.uk.at.view.kmk002.b {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            // init NtsGrid
            screenModel.empCondition.initNtsGrid();
        });
    });
}
