module nts.uk.com.view.kwr002.d {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            setTimeout(function() {
                __viewContext.bind(screenModel);
                $('#attendanceName').focus();
            }, 100);

        });
    });
}