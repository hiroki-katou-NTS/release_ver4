module nts.uk.at.view.kdp005.a {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            $( "a.ui-tabs-anchor" ).addClass( "limited-label" );
        });
    });
}
