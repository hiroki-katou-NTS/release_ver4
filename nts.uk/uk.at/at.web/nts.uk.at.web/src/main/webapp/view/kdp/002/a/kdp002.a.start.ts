module nts.uk.at.view.kdp002.a {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
			$(window).resize(function () {reCalGridWidthHeight()});
        });
    });
}
