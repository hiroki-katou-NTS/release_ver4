module qmm012.b {
    __viewContext.ready(function() {
        let screenModel = new qmm012.b.ScreenModel(
            new c.viewmodel.ScreenModel(),
            new d.viewmodel.ScreenModel(),
            new e.viewmodel.ScreenModel(),
            new f.viewmodel.ScreenModel(),
            new g.viewmodel.ScreenModel()
        );
        let screenModelB = new viewmodel.ScreenModel(screenModel);
        nts.uk.ui.confirmSave(screenModelB.dirty);
        __viewContext.bind(screenModelB);
    });

    export class ScreenModel {
        screenModelC: c.viewmodel.ScreenModel;
        screenModelD: d.viewmodel.ScreenModel;
        screenModelE: e.viewmodel.ScreenModel;
        screenModelF: f.viewmodel.ScreenModel;
        screenModelG: g.viewmodel.ScreenModel;

        constructor(
            screenModelC: c.viewmodel.ScreenModel,
            screenModelD: d.viewmodel.ScreenModel,
            screenModelE: e.viewmodel.ScreenModel,
            screenModelF: f.viewmodel.ScreenModel,
            screenModelG: g.viewmodel.ScreenModel
        ) {
            this.screenModelC = screenModelC;
            this.screenModelD = screenModelD;
            this.screenModelE = screenModelE;
            this.screenModelF = screenModelF;
            this.screenModelG = screenModelG;
        }

    }
}
