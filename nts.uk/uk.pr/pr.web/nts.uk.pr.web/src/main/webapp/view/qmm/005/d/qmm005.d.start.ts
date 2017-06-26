module qmm005.d {
    __viewContext.ready(() => {
        __viewContext["viewModel"] = new ViewModel();
        __viewContext.bind(__viewContext["viewModel"]);
    });
}