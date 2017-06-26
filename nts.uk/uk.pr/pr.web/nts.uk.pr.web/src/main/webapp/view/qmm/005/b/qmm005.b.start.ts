/// <reference path="../qmm005.ts"/>

__viewContext.primitiveValueConstraints["processingYear"] = {
    min: 1970,
    max: 9999,
    required: true,
    charType: "Numeric",
    valueType: "Numeric"
}

module qmm005.b {
    __viewContext.ready(() => {
        __viewContext["viewModel"] = new ViewModel();
        __viewContext.bind(__viewContext["viewModel"]);
    });
}