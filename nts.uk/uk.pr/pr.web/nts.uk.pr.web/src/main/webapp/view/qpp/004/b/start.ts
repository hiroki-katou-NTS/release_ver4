__viewContext.ready(function() {
    var data = this.transferred.get();
    if(typeof data  === 'undefined'||data === null){
        nts.uk.request.jump("/view/qpp/004/a/index.xhtml", {});
    } else {
        var screenModel = new qpp004.b.viewmodel.ScreenModel(data);
        this.bind(screenModel);
    }    
});