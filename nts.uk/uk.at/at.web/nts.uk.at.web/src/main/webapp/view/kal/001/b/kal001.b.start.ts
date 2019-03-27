module nts.uk.at.view.kal001.b {  
    __viewContext.ready(function() {
        let param =  nts.uk.ui.windows.getShared("extractedAlarmData");
        let extractedAlarmData : Array<model.ValueExtractAlarmDto> = param.listAlarmExtraValueWkReDto;
        let screenModel = new viewmodel.ScreenModel(param);

        if (!extractedAlarmData || extractedAlarmData.length <= 0) {// same condiditon dataExtractAlarm.nullData
            nts.uk.ui.dialog.info({ messageId: "Msg_835" });
            screenModel.flgActive(false);
        }

        screenModel.startPage().done(function() {
            __viewContext.bind(screenModel);            
            $("#grid").igGrid("option", "dataSource", extractedAlarmData);
//            screenModel.dataSource= extractedAlarmData;
        });
    });
}