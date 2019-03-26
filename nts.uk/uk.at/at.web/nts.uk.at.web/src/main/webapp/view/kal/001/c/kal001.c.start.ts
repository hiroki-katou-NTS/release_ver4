module nts.uk.at.view.kal001.c {  
    __viewContext.ready(function() {
        let processId : string =  nts.uk.ui.windows.getShared("processId");
        let screenModel = new viewmodel.ScreenModel(processId); 

        screenModel.startPage().done(() =>{
            __viewContext.bind(screenModel); 
        });      
    });
}