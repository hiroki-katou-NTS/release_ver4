module nts.uk.at.view.kmf004.a {
    var __viewContext = window['__viewContext'] || {};
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
			__viewContext.bind(screenModel);
			$(".radio-wrapper").focus(function() {
				$(".radio-wrapper").css("border", "none");
				$(".radio-wrapper").css("box-shadow", "none");
			});

        });
    });
}