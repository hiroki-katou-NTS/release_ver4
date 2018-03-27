module nts.uk.at.view.kmk011.i {
    import blockUI = nts.uk.ui.block;

    import viewModel = nts.uk.at.view.kmk011.i.viewmodel;

    __viewContext.ready(function() {
        let mainTab = new viewModel.ScreenModel();

        mainTab.start_page().done(function(screenModel) {
            __viewContext.bind(mainTab);
        });
    });
}
