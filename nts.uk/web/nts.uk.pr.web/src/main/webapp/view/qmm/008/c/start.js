__viewContext.ready(function () {
    var screenModel = new nts.uk.pr.view.qmm008.c.viewmodel.ScreenModel(nts.uk.ui.windows.getShared("officeCodeOfParentValue"));
    this.bind(screenModel);
});
