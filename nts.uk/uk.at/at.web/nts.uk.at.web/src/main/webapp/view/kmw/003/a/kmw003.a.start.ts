module nts.uk.at.view.kmw003.a {
    let __viewContext: any = window["__viewContext"] || {};
    __viewContext.ready(function() {
        let value = __viewContext.transferred.value;
        if(value == undefined || value == null){
           value = false; 
        } else {
           value =  value.isKmwCall;
        }
        let screenModel = __viewContext.vm = new nts.uk.at.view.kmw003.a.viewmodel.ScreenModel(value);
        // set pg-name if pg-name = ''
        if ($("#pg-name").text() == '') $("#pg-name").text("KMW003A " + nts.uk.resource.getText("KMW003_1"));
        screenModel.startPage().done(() => {
            
            let dialogOptions = {
               forGrid: true,
                headers: [
                    new nts.uk.ui.errors.ErrorHeader("employeeCode", "社員コード", "auto", true),
                    new nts.uk.ui.errors.ErrorHeader("employeeName", "社員名", "auto", true),
                    new nts.uk.ui.errors.ErrorHeader("columnName", "対象項目", "auto", true),
                    new nts.uk.ui.errors.ErrorHeader("message", "エラー内容", "auto", true)
                ] 
            }
            __viewContext.bind(screenModel, dialogOptions);
                screenModel.isStartScreen(false);
        });
    });
}