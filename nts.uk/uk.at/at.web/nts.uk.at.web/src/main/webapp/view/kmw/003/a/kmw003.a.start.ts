module nts.uk.at.view.kmw003.a {
    let __viewContext: any = window["__viewContext"] || {};
    __viewContext.ready(function() {
        var screenModel = __viewContext.vm = new nts.uk.at.view.kmw003.a.viewmodel.ScreenModel();
        screenModel.startPage().done(() => {
            //this.bind(screenModel, dialogOptions);
            //cursor move direction 
            screenModel.selectedDirection.subscribe((value) => {
                if (value == 0) {
                    $("#dpGrid").ntsGrid("directEnter", "below", "");
                } else {
                    $("#dpGrid").ntsGrid("directEnter", "right", "");
                }
            });
            
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
        });
    });
}