module a1 {

    import MainModel = nts.uk.com.view.cmm048.a.viewmodel.MainModel;
    import EmployeeModel = nts.uk.com.view.cmm048.a.viewmodel.EmployeeModel;
    import EmployeeInfoContactModel = nts.uk.com.view.cmm048.a.viewmodel.EmployeeInfoContactModel;
    import PersonContactModel = nts.uk.com.view.cmm048.a.viewmodel.PersonContactModel;
    import PasswordPolicyModel = nts.uk.com.view.cmm048.a.viewmodel.PasswordPolicyModel;
    
    class ScreenModel {

        employee: EmployeeModel;        
        passwordPolicy: PasswordPolicyModel;
        
        /**
         * Constructor
         */
        constructor(model: MainModel) {
            let _self = this;
        
            _self.employee = model.employee;            
            _self.passwordPolicy = model.passwordPolicy;
        }

        /**
         * Start tab
         */
        public startTab(): void {
            let _self = this;
        }
    } 

    /**
     * Knockout Binding Handler - Tab 1
     */
    class CMM048A1BindingHandler implements KnockoutBindingHandler {

        /**
         * Constructor
         */
        constructor() { }

        /**
         * Init
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void { }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            let webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["com"] + '/')
                .mergeRelativePath('/view/cmm/048/a1/index.xhtml').serialize();
            // Get data
            let input = valueAccessor();
            let model: MainModel = input.model;

            let screenModel = new ScreenModel(model);
            $(element).load(webserviceLocator, () => {
                ko.cleanNode($(element)[0]);
                ko.applyBindingsToDescendants(screenModel, $(element)[0]);
                screenModel.startTab();
            });
        }
    }
    ko.bindingHandlers['ntsCMM048A1'] = new CMM048A1BindingHandler();
}