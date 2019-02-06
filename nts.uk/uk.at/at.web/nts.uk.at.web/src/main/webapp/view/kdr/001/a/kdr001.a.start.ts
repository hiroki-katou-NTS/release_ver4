module nts.uk.at.view.kdr001.a {
    __viewContext.ready(function() {
        let screenModel = new viewmodel.ScreenModel();
         __viewContext['viewContext'] = screenModel;
        screenModel.startPage().done(function(self) {
            __viewContext.bind(self);
            $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent).done(function() {
                self.employeeList = ko.observableArray<UnitModel>([]);
                self.applyKCP005ContentSearch([]);
                // Load employee list component
                $('#employeeSearch').ntsListComponent(self.lstPersonComponentOption).done(function() {
                });
                
            });
            
            $('#A1_1').focus();
        });
    });
}
