module nts.uk.at.view.kmk011.d {
    import blockUI = nts.uk.ui.block;
    
    __viewContext.ready(function() {
        let mainTab = new viewmodel.ScreenModel();
        
        mainTab.start_page(0).done(function(screenModel){
            let histId: string = null;
            screenModel.histList().length > 0 ? histId = screenModel.histList()[0].historyId : histId = null;
            screenModel.selectedHist(histId);
            __viewContext.bind(screenModel); 
//            screenModel.onSelectTabOne();
        });
    });
}
