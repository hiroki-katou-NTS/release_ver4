module nts.uk.at.view.kdw001.d {
    import getText = nts.uk.resource.getText;

    export module viewmodel {
        export class ScreenModel {
            //Declare screenName flag to forware screen B or screen J
            screenName: string;
            
            periodDate: KnockoutObservable<string>;
            
            numberEmployee: KnockoutObservable<string>;
            
            dailyCreated: KnockoutObservable<string>;
            dailyCal: KnockoutObservable<string>;
            approvalResult: KnockoutObservable<string>;
            monthCount: KnockoutObservable<string>;
            
            dailyCreatedVisible: KnockoutObservable<boolean>;
            dailyCalVisible: KnockoutObservable<boolean>;
            approvalVisible: KnockoutObservable<boolean>;
            monthCountVisible: KnockoutObservable<boolean>;
            
            constructor() {
                let self = this;
                
              self.periodDate = ko.observable('');
              self.numberEmployee = ko.observable('');
              self.dailyCreated = ko.observable('');
              self.dailyCal = ko.observable('');
              self.approvalResult = ko.observable('');
              self.monthCount = ko.observable('');
                
                 self.dailyCreatedVisible = ko.observable(true);
                 self.dailyCalVisible = ko.observable(true);
                 self.approvalVisible = ko.observable(true);
                 self.monthCountVisible = ko.observable(true);
                
                
                
                
                //Get screenName value from a screen
                __viewContext.transferred.ifPresent(data => {
                    self.screenName = data.screenName;
                });
            }
            
            opendScreenBorJ() {
                $("#wizard").ntsWizard("prev");        
            }
            
            opendScreenE() {
                nts.uk.ui.windows.setShared("KDWL001E", __viewContext["viewmodel"].params);
                nts.uk.ui.windows.sub.modal("/view/kdw/001/e/index.xhtml");
            }
        }
    }
}
