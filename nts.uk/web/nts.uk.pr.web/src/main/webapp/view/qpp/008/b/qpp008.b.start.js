var qpp008;
(function (qpp008) {
    var b;
    (function (b) {
        __viewContext.ready(function () {
            var screenModel = new b.viewmodel.ScreenModel();
            screenModel.startPage().done(function () {
                nts.uk.ui.confirmSave(screenModel.printSettingDirty);
                __viewContext.bind(screenModel);
            });
        });
    })(b = qpp008.b || (qpp008.b = {}));
})(qpp008 || (qpp008 = {}));
//# sourceMappingURL=qpp008.b.start.js.map