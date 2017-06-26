module kml001.c.viewmodel {
    import servicebase = kml001.shr.servicebase;
    import vmbase = kml001.shr.vmbase;
    export class ScreenModel {
        copyDataFlag: KnockoutObservable<boolean>;
        lastestStartDate: KnockoutObservable<string>;
        newStartDate: KnockoutObservable<string>;
        size: KnockoutObservable<number>;
        textKML001_47: KnockoutObservable<string>;
        constructor() {
            var self = this;
            self.copyDataFlag = ko.observable(true);
            self.lastestStartDate = ko.observable(nts.uk.ui.windows.getShared('lastestStartDate'));
            self.newStartDate = ko.observable(null);
            self.newStartDate.subscribe(function(value){
                if(self.errorStartDate()) $("#startDateInput").ntsError('set', {messageId:"Msg_102"});     
            });
            self.size = ko.observable(nts.uk.ui.windows.getShared('size'));
            self.textKML001_47 = ko.observable(nts.uk.resource.getText('KML001_47',[self.lastestStartDate()]));
        }
        
        /**
         * check error on new input date
         */
        errorStartDate(): boolean {
            var self = this;
            return ((self.newStartDate()== null)|| vmbase.ProcessHandler.validateDateInput(self.newStartDate(),self.lastestStartDate()));     
        }
        
        /**
         * process parameter and close dialog 
         */
        submitAndCloseDialog(): void {
            var self = this;
            if(self.errorStartDate()) $("#startDateInput").ntsError('set', {messageId:"Msg_102"}); 
            else {
                nts.uk.ui.windows.setShared('newStartDate', self.newStartDate());
                nts.uk.ui.windows.setShared('copyDataFlag', self.copyDataFlag());
                nts.uk.ui.windows.close(); 
            }
        }
        
        /**
         * close dialog and do nothing
         */
        closeDialog(): void {
            $("#startDateInput").ntsError('clear');
            nts.uk.ui.windows.close();   
        }
    }
}