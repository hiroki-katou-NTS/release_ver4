module nts.uk.com.view.ccg008.c {
    __viewContext.ready(function() {
        var screenModel = new c.viewmodel.ScreenModel();
        screenModel.start().done(function() {
            __viewContext.bind(screenModel);
        });
    });
}