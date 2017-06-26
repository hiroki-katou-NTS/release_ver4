module qpp014.b {
    __viewContext.ready(function() {
        var data = this.transferred.get();
        if (typeof data === 'undefined' || data === null) {
            nts.uk.request.jump("/view/qpp/014/a/index.xhtml", {});
        } else {
            var screenModel = new qpp014.b.viewmodel.ScreenModel(data);
            screenModel.startPage().done(function() {
                __viewContext.bind(screenModel);
            });
        }
    });
}
