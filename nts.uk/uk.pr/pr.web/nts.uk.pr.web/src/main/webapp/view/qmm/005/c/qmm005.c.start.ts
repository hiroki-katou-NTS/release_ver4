module qmm005.c {
    __viewContext.ready(() => {
        __viewContext["viewModel"] = new ViewModel();
        __viewContext.bind(__viewContext["viewModel"]);
    });
}