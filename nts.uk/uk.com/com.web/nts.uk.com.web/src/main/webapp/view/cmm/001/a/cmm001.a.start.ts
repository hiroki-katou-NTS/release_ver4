__viewContext.ready(function() {
    var screenModel = new cmm001.a.ViewModel();
    screenModel.start(undefined).done(function(){
        nts.uk.ui.confirmSave(screenModel.dirtyObject);
        __viewContext.bind(screenModel);
        screenModel.dirtyObject.reset();
    });
   
});
