module nts.uk.com.view.qmm011.d {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        __viewContext.bind(screenModel);
        _.defer(() => {$('#D2_3').focus()});
    });
}