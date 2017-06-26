__viewContext.ready(function() {
    __viewContext.viewModel = {
        viewmodelA: new qmm020.a.viewmodel.ScreenModel(),
        viewmodelB: new qmm020.b.viewmodel.ScreenModel(),
        viewmodelC: new qmm020.c.viewmodel.ScreenModel(),
        viewmodelD: new qmm020.d.viewmodel.ScreenModel()
    };
    __viewContext.bind(__viewContext.viewModel);
});