module nts.uk.at.view.cdl024 {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
        screenModel.startPage().done(function() {
            
            $("#multi-list").ntsGridList({
                height: 336,
                dataSource: screenModel.items,
                optionsValue: 'code',
                columns: screenModel.columns,
                multiple: true,
                value: [].slice.call(screenModel.currentCodeList)
            });
            
            __viewContext.bind(screenModel);
        });
    })
}