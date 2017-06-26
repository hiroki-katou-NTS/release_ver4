__viewContext.ready(function() {
    var screenModel = new qpp004.l.viewmodel.ScreenModel();

    var data = nts.uk.ui.windows.getShared("data");

    this.bind(screenModel);

    screenModel.timer.start();
    screenModel.startPage(data);


});
