module nts.uk.pr.view.qui001.a {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        __viewContext.bind(screenModel);
        _.defer(() => {$('#A222_4').focus()});
    });
}