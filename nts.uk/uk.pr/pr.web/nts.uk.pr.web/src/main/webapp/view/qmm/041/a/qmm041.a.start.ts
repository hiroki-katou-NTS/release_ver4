module nts.uk.pr.view.qmm041.a {
    __viewContext.ready(() => {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(() => {
            __viewContext.bind(screenModel);
        });
    });
}