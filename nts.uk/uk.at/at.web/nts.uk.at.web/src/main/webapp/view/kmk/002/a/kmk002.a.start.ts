module nts.uk.at.view.kmk002.a {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            // Init igGrid
            screenModel.optionalItemHeader.optionalItem.initIgGrid();
        });
    });
}