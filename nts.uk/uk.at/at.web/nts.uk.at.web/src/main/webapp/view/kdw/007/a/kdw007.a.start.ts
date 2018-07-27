module nts.uk.at.view.kdw007.a {
    __viewContext.ready(function() {
        let isDaily = null;
        this.transferred.ifPresent(param => {
           isDaily = param.ShareObject;
        });
        var screenModel = new viewmodel.ScreenModel(isDaily);
        screenModel.startPage(null).done(function() {
            __viewContext.bind(screenModel);
            if (screenModel.lstErrorAlarm().length > 0) {
                $("#errorAlarmWorkRecordName").focus();
            } else {
                $("#errorAlarmWorkRecordCode").focus();
            }
        });
    });
}