module nts.uk.com.view.cas013.a {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
        //     _.defer(() => {
        //         screenModel.applyKCP005ContentSearch([]);
        //         $('#kcp005component').ntsListComponent(screenModel.kcp005ComponentOption);
        // });
    });
})}