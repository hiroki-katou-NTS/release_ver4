__viewContext.ready(function(){
    var viewModel = new kdl024.a.viewmodel.ScreenModel();
    viewModel.start().done(() => {
         __viewContext.bind(viewModel);
          $('#inpCode').focus();
    });
})