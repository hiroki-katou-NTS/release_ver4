module nts.uk.pr.view.qpp018.c {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.start().done(data => {
            __viewContext.bind(data);
        });
    });
}