module cps008.a {
    let __viewContext: any = window['__viewContext'] || {};
    __viewContext.ready(function() {
        __viewContext['viewModel'] = new viewmodel.ScreenModel();
        __viewContext.bind(__viewContext['viewModel']);
    });
}