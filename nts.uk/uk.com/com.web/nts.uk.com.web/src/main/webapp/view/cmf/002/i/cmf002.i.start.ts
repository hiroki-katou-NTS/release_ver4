module nts.uk.com.view.cmf002.i {
    __viewContext.ready(function() {
        __viewContext['screenModel'] = new viewmodel.ScreenModel();
//        __viewContext['screenModel'].start().done(function() {
            __viewContext.bind(__viewContext['screenModel']);
//        });
    });
}