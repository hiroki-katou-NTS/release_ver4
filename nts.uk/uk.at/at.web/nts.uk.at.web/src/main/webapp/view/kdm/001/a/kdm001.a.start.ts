module nts.uk.at.view.kdm001.a {
    __viewContext.ready(function() {
        var viewmodelA = new nts.uk.at.view.kdm001.a.viewmodel.ScreenModel();
        var viewmodelB = new nts.uk.at.view.kdm001.b.viewmodel.ScreenModel();
        __viewContext.viewModel = {
            viewmodelA: viewmodelA,
            viewmodelB: viewmodelB
        };
        __viewContext.bind(__viewContext.viewModel);
        
        viewmodelA.startPage().done(function() {
            $('#ccgcomponentA').ntsGroupComponent(viewmodelA.ccgcomponent);
            $('#emp-componentA').focus();
            nts.uk.ui.block.clear();
            let isStart = 0;
            $(".tab-b").click(function() {
                if (isStart ==0){
                    viewmodelB.startPage().done(function() {
                        $('#emp-component').focus();
                        nts.uk.ui.block.clear();
                        isStart = 1;
                    });
                }
            });
        });
    });
}
