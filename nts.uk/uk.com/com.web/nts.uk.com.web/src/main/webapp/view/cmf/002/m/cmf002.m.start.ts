module nts.uk.com.view.cmf002.m {
    __viewContext.ready(function() {
        __viewContext['screenModel'] = new viewmodel.ScreenModel();
        __viewContext['screenModel'].start().done(function() {
            __viewContext.bind(__viewContext['screenModel']);
            _.defer(() => {
                __viewContext['screenModel'].inTimeDataFormatSetting().fixedValue() == 1 ? $('#M11_1').focus() : $('#M2_1').focus();
            });
        });
    });
}