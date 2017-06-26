/// <reference path="../qmm005.ts"/>
module qmm005.e {
    __viewContext.ready(() => {
        __viewContext["viewModel"] = new ViewModel();
        __viewContext.bind(__viewContext["viewModel"]);
    });
}