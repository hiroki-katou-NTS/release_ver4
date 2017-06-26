module qpp004.a.viewmodel {
    export class ScreenModel {
        
        paymentDateProcessingList: KnockoutObservableArray<any>;
        selectedPaymetnProcessing: KnockoutObservable<any>;
        displayCurrentYearMonthProcessing: KnockoutObservable<string>;
        currentPaymentDateProcessing: KnockoutObservable<any>;
        
        /**
         * Init screen model.
         */
        constructor() {
            var self = this;
            
            self.paymentDateProcessingList = ko.observableArray([]);
            self.selectedPaymetnProcessing = ko.observable('');
            self.displayCurrentYearMonthProcessing = ko.observable(null);
            self.currentPaymentDateProcessing = ko.observable(null);
            
            self.selectedPaymetnProcessing.subscribe(function(newValue) {
                var currentDateMaster = _.find(self.paymentDateProcessingList(), function(item) {
                    return item.processingNo == newValue;
                });
                
                self.currentPaymentDateProcessing(currentDateMaster);
                
                self.displayCurrentYearMonthProcessing(nts.uk.time.formatYearMonth(currentDateMaster.currentProcessingYm));
            });
        }
        
        /**
         * Start page.
         * Load all data which is need for binding data.
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            
            // Page load dfd.
            var dfd = $.Deferred();
            
            // Resolve start page dfd after load all data.
            $.when(qpp004.a.service.getPaymentDateProcessingMasterList()).done(function(data) {
                
                self.paymentDateProcessingList(data);
                
                dfd.resolve();
                
            }).fail(function(res) {
                
            });
            
            return dfd.promise();
        }
        
        /**
         * Redirect to page process create data
         */
        redirectToCreateData(): any {
            var self = this;
            var data = self.currentPaymentDateProcessing();
            data["displayCurrentProcessingYm"] = self.displayCurrentYearMonthProcessing();
            
            nts.uk.request.jump("/view/qpp/004/b/index.xhtml", data);
        }
    }
}