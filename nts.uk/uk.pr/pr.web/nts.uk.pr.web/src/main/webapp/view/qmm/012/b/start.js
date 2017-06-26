var qmm012;
(function (qmm012) {
    var b;
    (function (b) {
        __viewContext.ready(function () {
            let screenModel = new qmm012.b.ScreenModel(new qmm012.c.viewmodel.ScreenModel(), new qmm012.d.viewmodel.ScreenModel(), new qmm012.e.viewmodel.ScreenModel(), new qmm012.f.viewmodel.ScreenModel(), new qmm012.g.viewmodel.ScreenModel());
            let screenModelB = new b.viewmodel.ScreenModel(screenModel);
            __viewContext.bind(screenModelB);
        });
        class ScreenModel {
            constructor(screenModelC, screenModelD, screenModelE, screenModelF, screenModelG) {
                this.screenModelC = screenModelC;
                this.screenModelD = screenModelD;
                this.screenModelE = screenModelE;
                this.screenModelF = screenModelF;
                this.screenModelG = screenModelG;
            }
        }
        b.ScreenModel = ScreenModel;
    })(b = qmm012.b || (qmm012.b = {}));
})(qmm012 || (qmm012 = {}));
