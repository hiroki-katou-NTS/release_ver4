module nts.uk.com.view.cmf004.b {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.start().done(()=>{
            __viewContext.bind(screenModel);
            $('#kcp005component').ntsListComponent(screenModel.kcp005ComponentOption);
            $('#kcp005component1').ntsListComponent(screenModel.kcp005ComponentOption);
        });
    });
}