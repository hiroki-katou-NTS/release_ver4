module nts.uk.at.view.kaf018.b {
    //import setShared = nts.uk.ui.windows.setShared;
//    let __viewContext: any = window['__viewContext'] || {}; 
//    __viewContext.ready(() => {
//    __viewContext.transferred.ifPresent(data => {
//        setShared('KAF018BInput', data);
//    }); 
//    let screenModel = new kaf018.b.viewmodel.ScreenModel();
//        
//        __viewContext.bind(screenModel);
//        $('#combo-box').focus();
//    });

    let __viewContext: any = window['__viewContext'] || {};
    __viewContext.ready(() => {
        __viewContext.transferred.ifPresent(data => {
            nts.uk.ui.windows.setShared("KAF018BInput", data);
        });
        let screenModel = new kaf018.b.viewmodel.ScreenModel();        
        screenModel.startPage().done(function(){
            __viewContext.bind(screenModel);          
            // $("#H3_1_1").focus(); 
        })
    });
}