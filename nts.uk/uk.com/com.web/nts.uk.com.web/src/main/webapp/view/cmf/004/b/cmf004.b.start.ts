module nts.uk.com.view.cmf004.b {
    __viewContext.ready(function() {
       __viewContext['screenModel'] = new viewmodel.ScreenModel();
       let vm: any = __viewContext['screenModel'];
        vm.start().done(()=>{
            __viewContext.bind(vm);
            $('#B5_1_horizontalScrollContainer').remove();
            $('#kcp005component').ntsListComponent(vm.kcp005ComponentOptionScreenG);
            $('#kcp005component1').ntsListComponent(vm.kcp005ComponentOptionScreenH);
            
        });
        $('#B3_1').focus();
    });
}