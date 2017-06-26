module kml001.d {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        __viewContext.bind(screenModel);
        if(screenModel.isUpdate()) $('#startDateInput').focus();
    });
}
