//__viewContext.ready(function () {
//    var screenModel = new qmm019.g.viewmodel.ScreenModel();
//    var vm = screenModel;
//    
////    screenModel.startPage().done(function() {
////        __viewContext.bind(vm);
////    });
//    this.bind(vm);
//});

__viewContext.ready(function () {
    //import { ZipCodeValidator } from "./ZipCodeValidator";
    var screenModel = new qmm019.g.viewmodel.ScreenModel();
    screenModel.start().done(function() {
        __viewContext.bind(screenModel);
    });
});