
module nts.uk.at.view.kdl005.test {
 __viewContext.ready(function() {
		service.getSid().done((data : any) => {
			let screenModel = new viewmodel.ScreenModel(data);
			__viewContext.bind(screenModel);
		})
    });
}