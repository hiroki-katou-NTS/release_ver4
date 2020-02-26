module nts.uk.at.view.kdw003.a {
    let __viewContext: any = window["__viewContext"] || {};
    __viewContext.ready(function() {
        let dataShare: any;
        this.transferred.ifPresent(data => {
            console.log(data);
            dataShare = data;
        });
        var screenModel = __viewContext.vm = new nts.uk.at.view.kdw003.a.viewmodel.ScreenModel(dataShare);
        screenModel.startPage().done((data) => {
            //this.bind(screenModel, dialogOptions);
            //cursor move direction 
            if (data.bindDataMap) {
                screenModel.processMapData(data.data);
                //screenModel.loadKcp009();
                nts.uk.ui.block.clear();
            }
            __viewContext.bind(screenModel);
            if (data.bindDataMap) {
                screenModel.setSprFromItem(data.data);
                screenModel.processFlex(data.data, true);
            }
            
            screenModel.loadKcp009();
            
        });
    });
}