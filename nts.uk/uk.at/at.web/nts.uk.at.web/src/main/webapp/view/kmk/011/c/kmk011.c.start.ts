module nts.uk.at.view.kmk011.c {
    import blockUI = nts.uk.ui.block;
    
    __viewContext.ready(function() {
        let mainTab = new viewmodel.ScreenModel();

        
        mainTab.start_page().done(function(screenModel){
            __viewContext.bind(mainTab); 
        });
    });
}
