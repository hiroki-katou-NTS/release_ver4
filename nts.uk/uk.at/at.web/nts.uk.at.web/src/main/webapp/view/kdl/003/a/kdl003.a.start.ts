module nts.uk.at.view.kdl003.a {
    __viewContext.ready(function() {
        var parentCodes = nts.uk.ui.windows.getShared('parentCodes');
        var screenModel = new nts.uk.at.view.kdl003.a.viewmodel.ScreenModel(parentCodes);
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            $("#inputStartTime").focus();
        });
    });
}
