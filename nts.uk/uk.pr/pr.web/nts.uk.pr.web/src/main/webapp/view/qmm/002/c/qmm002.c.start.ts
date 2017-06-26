module qmm002.c {
    __viewContext.ready(function() {
        var screenModel = new qmm002.c.viewmodel.ScreenModel();
        screenModel.startPage();
        this.bind(screenModel);
    });
}