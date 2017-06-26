/// <reference path="../qmm005.ts"/>
module qmm005.a {
    __viewContext.ready(() => {
        __viewContext["viewModel"] = new ViewModel();
        __viewContext.bind(__viewContext["viewModel"]);
        nts.uk.ui.confirmSave(__viewContext["viewModel"].dirty);
    });
}