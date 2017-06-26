module kml001.c {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        __viewContext.bind(screenModel);
        $('#startDateInput').focus();
    });
}
