module kml001.b {
    __viewContext.ready(function() {
        var screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);
            if(screenModel.allUse()!=0){
                for(let i=0;i<10;i++){
                    if(screenModel.premiumItemList()[i].useAtr()!=0) {
                        $('.premiumName:eq('+i+')').focus();
                        break;
                    }
                }    
            }
        });
    });
}
