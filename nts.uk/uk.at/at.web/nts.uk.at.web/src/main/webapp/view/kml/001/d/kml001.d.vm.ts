module kml001.d.viewmodel {
    import servicebase = kml001.shr.servicebase;
    import vmbase = kml001.shr.vmbase;
    export class ScreenModel {
        isUpdate: KnockoutObservable<boolean>;
        deleteAble: KnockoutObservable<boolean>;
        beforeIndex: number;
        size: number;
        personCostList: KnockoutObservableArray<vmbase.PersonCostCalculation>;
        currentPersonCost: KnockoutObservable<vmbase.PersonCostCalculation>;
        isLast: KnockoutObservable<boolean>;
        beforeStartDate: KnockoutObservable<string>;
        currentEndDate: KnockoutObservable<string>;
        newStartDate: KnockoutObservable<string>;
        constructor() {
            var self = this;
            self.personCostList = ko.observableArray(<Array<vmbase.PersonCostCalculation>>nts.uk.ui.windows.getShared('personCostList'));
            self.currentPersonCost = ko.observable(<vmbase.PersonCostCalculation>nts.uk.ui.windows.getShared('currentPersonCost'));
            self.size = _.size(self.personCostList());
            self.isLast = ko.observable((_.findIndex(self.personCostList(), self.currentPersonCost())==(self.size-1))?true:false);
            self.deleteAble = ko.observable((self.isLast()&&(self.size>1))); // can delete when item is last and list have more than one item
            self.beforeIndex = _.findIndex(self.personCostList(), function(o) { return o.startDate() == self.currentPersonCost().startDate(); })-1;
            self.beforeStartDate = ko.observable((self.beforeIndex>=0)?self.personCostList()[self.beforeIndex].startDate():"1900/1/1");
            self.currentEndDate = ko.observable(self.currentPersonCost().endDate());
            self.newStartDate = ko.observable(self.currentPersonCost().startDate());
            self.newStartDate.subscribe(function(value){
                if(self.errorStartDate()) $("#startDateInput").ntsError('set', {messageId:"Msg_65"});     
            });
            self.isUpdate = ko.observable(false);
            self.isUpdate.subscribe(function(value) { 
                if(value) {
                    if(self.errorStartDate()) $("#startDateInput").ntsError('set', {messageId:"Msg_65"});
                } else { 
                    $("#startDateInput").ntsError('clear');
                }
            });
        }
        
        /**
         * check error on newStartDate
         */
        errorStartDate(): boolean {
            var self = this;
            return (
                (self.newStartDate()== null) ||
                !vmbase.ProcessHandler.validateDateRange(self.newStartDate(),kml001.shr.vmbase.ProcessHandler.getOneDayAfter(self.beforeStartDate()),self.currentEndDate())
                );    
        }
        
        /**
         * update/delete data when no error and close dialog
         */
        submitAndCloseDialog(): void {
            var self = this;
            if(self.isUpdate()) {
                if(self.errorStartDate()) $("#startDateInput").ntsError('set', {messageId:"Msg_65"}); 
                else {
                    self.currentPersonCost().startDate(self.newStartDate());
                    servicebase.personCostCalculationUpdate(vmbase.ProcessHandler.toObjectPersonCost(self.currentPersonCost()))
                        .done(function(res: Array<any>) {
                            nts.uk.ui.windows.setShared('isEdited', 0);
                            nts.uk.ui.windows.close();
                        })
                        .fail(function(res) {
                            nts.uk.ui.dialog.alert(res.message);       
                        });
                }
            } else {
                if(1==1) { 
                    nts.uk.ui.dialog.confirm(vmbase.MSG.MSG018).ifYes(function(){
                        servicebase.personCostCalculationDelete(vmbase.ProcessHandler.toObjectPersonCost(self.currentPersonCost()))
                        .done(function(res: Array<any>) {
                            nts.uk.ui.windows.setShared('isEdited', 1);
                            nts.uk.ui.windows.close();
                        })
                        .fail(function(res) {
                            nts.uk.ui.dialog.alert(res.message);    
                        });
                    });
                    
                } else{ $("#startDateInput").ntsError('set', {messageId:"Msg_128"}); }
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