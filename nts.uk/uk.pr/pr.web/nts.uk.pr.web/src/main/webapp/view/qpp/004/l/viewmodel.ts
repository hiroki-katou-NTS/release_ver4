module qpp004.l.viewmodel {
    export class ScreenModel {
        completeList: KnockoutObservableArray<any>;
        errorList: KnockoutObservableArray<any>;
        countError: KnockoutObservable<number>;
        buttonStatus: KnockoutObservable<any>;
        buttonText: KnockoutObservable<string>;
        visibleErrorList: KnockoutObservable<boolean>;
        timer: nts.uk.ui.sharedvm.KibanTimer;
        stopProcess: any;
        processingState: KnockoutObservable<number>;
        processingStateText: KnockoutObservable<string>;
        processingNumberOfPerson: KnockoutObservable<number>;
        numberOfPerson: KnockoutObservable<number>;
        
        /**
         * Init screen model.
         */
        constructor() {
            var self = this;
            
            self.timer = new nts.uk.ui.sharedvm.KibanTimer('timer');
            self.completeList = ko.observableArray([]);
            self.errorList = ko.observableArray([]);
            self.countError = ko.computed(function(){
               return self.errorList().length; 
            });
            self.buttonStatus = ko.observable(null);
            self.buttonText = ko.observable(null);
            
            self.visibleErrorList = ko.observable(false);
            
            self.processingState = ko.observable(null);
            self.processingStateText = ko.observable(null);
            
            self.numberOfPerson = ko.observable(0);
            self.processingNumberOfPerson = ko.observable(0);
             
            self.stopProcess = function() {
                var self = this;
                var status = self.buttonStatus().status;
                if (status == 1) {
                    self.stopTimer();
                } else {
                    // close dialog
                    nts.uk.ui.windows.close();
                }
            }
        }
        
        /**
         * Start page.
         * Load all data which is need for binding data.
         */
        startPage(data): any {
            var self = this;
                        
            var index = ko.observable(0);      
            
            self.numberOfPerson(data.personIdList.length);
            
            if (data.personIdList.length > 0 ) {
                self.buttonStatus({status: 1, displayText: "中止"});
                self.buttonText("中止");
                
                self.processingState(0);
                self.processingStateText("データの作成中");    
            } else {
                self.stopTimer();
            }
            
            _.forEach(data.personIdList, function(personId){
                            
                if (self.buttonStatus().status == 1) {
                    index(index()  +1);
                        
                    // Resolve start page dfd after load all data.
                    $.when(self.createPaymentData(personId, data, index())).done(function(res){
                        if (res) {
                            self.errorList.push(res);
                        }
                        
                        self.processingNumberOfPerson(index());
                    }).fail(function() {
                        self.stopTimer();
                    });
                }
                    
            });
        
            self.processingNumberOfPerson.subscribe(function(value){
                console.log(value);
                if (value == data.personIdList.length) {
                    self.stopTimer();
                }
            });
        }
        
        stopTimer(): any {
            var self = this;  
            self.timer.end();
            self.buttonStatus({status: 0, displayText: "閉じる"});
            self.buttonText("閉じる");
            
            self.processingState(1);
            self.processingStateText("完了");   
        }
              
        /**
         * Request create data payment
         */
        createPaymentData(personId, data, index): any {
            var self = this;
            var dfd = $.Deferred();
            
            var parameter = {
                    personId: personId.id,
                    personName: personId.name,
                    processingNo: data.processingNo,
                    processingYearMonth: data.processingYearMonth
                };
            
            $.when(qpp004.l.service.processCreatePaymentData(parameter)).done(function(data) {
                self.completeList.push(personId);
                dfd.resolve();   
            }).fail(function(res) {
                self.visibleErrorList(true);
                var error = {};
                error = {
                   index: index,
                   personId: personId.id,
                   personName: personId.name, 
                   errorMessage: res.message,
                   contenError: nts.uk.text.format("{0} (社員CD: {1})", res.message, personId.code)
                };
                
                dfd.resolve(error); 
            });
            
            return dfd.promise();
        }
                
        
    }
}