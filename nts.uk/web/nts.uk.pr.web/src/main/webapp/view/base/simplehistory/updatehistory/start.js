var nts;
(function (nts) {
    var uk;
    (function (uk) {
        var pr;
        (function (pr) {
            var view;
            (function (view) {
                var base;
                (function (base) {
                    var simplehistory;
                    (function (simplehistory) {
                        var updatehistory;
                        (function (updatehistory) {
                            __viewContext.ready(function () {
                                var screenModel = new updatehistory.viewmodel.ScreenModel();
                                screenModel.startPage().done(function () {
                                    __viewContext.bind(screenModel);
                                });
                            });
                        })(updatehistory = simplehistory.updatehistory || (simplehistory.updatehistory = {}));
                    })(simplehistory = base.simplehistory || (base.simplehistory = {}));
                })(base = view.base || (view.base = {}));
            })(view = pr.view || (pr.view = {}));
        })(pr = uk.pr || (uk.pr = {}));
    })(uk = nts.uk || (nts.uk = {}));
})(nts || (nts = {}));
//# sourceMappingURL=start.js.map