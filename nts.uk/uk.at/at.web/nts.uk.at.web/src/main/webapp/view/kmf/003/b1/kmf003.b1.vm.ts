module nts.uk.at.view.kmf003.b1.viewmodel {
    export class ScreenModel {
        code: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        conditionValue: KnockoutObservable<string>;
        dateSelected: KnockoutObservable<string>;
        referenceDate: KnockoutObservable<string>;
        items: KnockoutObservableArray<Item>;
        grantReferenceDateOptions: KnockoutObservableArray<GrantReferenceDateOption>;
        payDayCalculate: KnockoutObservable<string>;
        displayDateSelected: KnockoutObservable<boolean>;
        conditionData: any;
        count: KnockoutObservable<number>;
        no1Data: any;
        currentData: any;
        
        constructor() {
            var self = this;
            
            self.conditionData = nts.uk.ui.windows.getShared("KMF003_CONDITION_NO");
            self.code = ko.observable(self.conditionData.code);
            self.name = ko.observable(self.conditionData.name);            
            self.conditionValue = ko.observable(
                nts.uk.resource.getText("KMF003_37", [self.conditionData.conditionValue.option, self.conditionData.conditionValue.value, self.conditionData.conditionValue.afterValue])
            );
            self.dateSelected = ko.observable(self.convertToMonthDay(self.conditionData.dateSelected));
            
            if(self.conditionData.dateSelected === "") {
                self.displayDateSelected = ko.observable(false);
            } else {
                self.displayDateSelected = ko.observable(true);
            }
            
            self.referenceDate = ko.observable("");
            self.items = ko.observableArray([]);
            
            self.grantReferenceDateOptions = ko.observableArray([
                new GrantReferenceDateOption(0, nts.uk.resource.getText("Enum_GrantReferenceDate_HIRE_DATE")),
                new GrantReferenceDateOption(1, nts.uk.resource.getText("Enum_GrantReferenceDate_YEAR_HD_REFERENCE_DATE"))
            ]);
            
            self.payDayCalculate = ko.observable("");
            self.count = ko.observable(0);
        }

        /**
         * Start page.
         */
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            
            $.when(self.getDataFromNo1(), self.getDataFromCurrent()).done(function() {
                if(self.currentData.length > 0 && self.currentData.length == self.no1Data.length) {
                    self.bindData(self.currentData, false);
                } else if(self.currentData.length > 0 && self.currentData.length > self.no1Data.length) {
                    
                } else if(self.currentData.length > 0 && self.currentData.length < self.no1Data.length) {
                    
                } else {
                    self.bindData(self.no1Data, true);
                }
                
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        /**
         * Get data from No 1.
         */
        getDataFromNo1(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            var conditionNo = 1;
            
            service.findByCode(conditionNo, self.conditionData.code).done(function(data){
                let sortedData = _.orderBy(data, ['grantYearHolidayNo'], ['asc']);
                self.no1Data = sortedData;
                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        /**
         * Get data from current.
         */
        getDataFromCurrent(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            
            service.findByCode(self.conditionData.conditionNo, self.conditionData.code).done(function(data){
                let sortedData = _.orderBy(data, ['grantYearHolidayNo'], ['asc']);
                self.currentData = sortedData;
                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        /**
         * Binding data to screen.
         */
        bindData(data: any, isNew: boolean){
            var self = this;
            
            self.items.removeAll();
            
            //Update case
            if(isNew) {
                for(var i = 0; i < data.length; i++){
                    var item : IItem = {
                        grantYearHolidayNo: data[i].grantYearHolidayNo,
                        conditionNo: self.conditionData.conditionNo,
                        yearHolidayCode: data[i].yearHolidayCode,
                        lengthOfServiceYears: data[i].lengthOfServiceYears,
                        lengthOfServiceMonths: data[i].lengthOfServiceMonths,
                        grantDays: null,
                        limitedTimeHdDays: null,
                        limitedHalfHdCnt: null,
                        grantReferenceDate: 0,
                        grantSimultaneity: data.length > 0 ? data[data.length - 1].grantSimultaneity : false,
                        grantDate: "",
                        gdEnable: true,
                        ltdEnable: true,
                        lthEnable: true 
                    };
                    self.items.push(new Item(item));
                }
            } else {
                for(var i = 0; i < data.length; i++){
                    var item : IItem = {
                        grantYearHolidayNo: data[i].grantYearHolidayNo,
                        conditionNo: self.conditionData.conditionNo,
                        yearHolidayCode: data[i].yearHolidayCode,
                        lengthOfServiceYears: data[i].lengthOfServiceYears,
                        lengthOfServiceMonths: data[i].lengthOfServiceMonths,
                        grantDays: data[i].grantDays,
                        limitedTimeHdDays: data[i].limitedTimeHdDays,
                        limitedHalfHdCnt: data[i].limitedHalfHdCnt,
                        grantReferenceDate: data[i].grantReferenceDate,
                        grantSimultaneity: data[i].grantSimultaneity,
                        grantDate: data[i].grantDate,
                        gdEnable: true,
                        ltdEnable: true,
                        lthEnable: true
                    };
                    self.items.push(new Item(item));
                }
            }            
            
            for(var j = data.length; j < 20; j++) {
                var item : IItem = {
                    grantYearHolidayNo: j + 1,
                    conditionNo: self.conditionData.conditionNo,
                    yearHolidayCode: self.conditionData.code,
                    lengthOfServiceYears: null,
                    lengthOfServiceMonths: null,
                    grantDays: null,
                    limitedTimeHdDays: null,
                    limitedHalfHdCnt: null,
                    grantReferenceDate: 0,
                    grantSimultaneity: data.length > 0 ? data[data.length - 1].grantSimultaneity : false,
                    grantDate: "",
                    gdEnable: false,
                    ltdEnable: false,
                    lthEnable: false
                };
                self.items.push(new Item(item));    
            }
        }
        
        /**
         * Add or Update data to db.
         */
        submit() {
            var self = this;
            
            $('#reference-date').ntsError('clear');
            
            if (nts.uk.ui.errors.hasError()) {
                return;    
            }

            var grantHolidayTblList = [];
            _.forEach(self.items(), function(item) {
                if ((item.lengthOfServiceYears() != null || item.lengthOfServiceMonths() != null || item.grantDays() != null || item.limitedTimeHdDays() != null || 
                    item.limitedHalfHdCnt() != null) && (item.lengthOfServiceYears().toString() != "" || item.lengthOfServiceMonths().toString() != "" || item.grantDays().toString() != "" || 
                    item.limitedTimeHdDays().toString() != "" || item.limitedHalfHdCnt().toString() != "")) {
                    grantHolidayTblList.push({
                        grantYearHolidayNo: item.grantYearHolidayNo(),
                        conditionNo: item.conditionNo(),
                        yearHolidayCode: item.yearHolidayCode(),
                        lengthOfServiceYears: item.lengthOfServiceYears(),
                        lengthOfServiceMonths: item.lengthOfServiceMonths(),
                        grantDays: item.grantDays(),
                        limitedTimeHdDays: item.limitedTimeHdDays(),
                        limitedHalfHdCnt: item.limitedHalfHdCnt(),
                        grantReferenceDate: item.grantReferenceDate(),
                        grantSimultaneity: item.grantSimultaneity() ? 1 : 0
                    });
                }
            });
            
            // if no data then return
            if (grantHolidayTblList == null || grantHolidayTblList.length == 0) {
                nts.uk.ui.windows.setShared("KMF003_HAVE_DATA", false);
                return;
            }
            
            // else have data then continue
            var dataTranfer: any = {
                conditionNo: self.conditionData.conditionNo,
                yearHolidayCode: self.code(), 
                grantHolidayList: grantHolidayTblList, 
            }
        
            service.addYearHolidayGrant(dataTranfer).done(function(){
                nts.uk.ui.windows.setShared("KMF003_HAVE_DATA", true);
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
            }).fail(function(error){
                nts.uk.ui.dialog.alertError({ messageId: error.messageId });    
            });
        }
        
        /**
         * Close dialog.
         */
        cancel() {
            var calcelData = nts.uk.ui.windows.getShared("KMF003_CANCEL_DATA");
            nts.uk.ui.windows.setShared("KMF003_HAVE_DATA", calcelData);
            nts.uk.ui.windows.close();
        }
        
        //Convert MD number to Date
        convertToMonthDay(monthDay: string) : string{
            if(monthDay != "") {
                var md = String(monthDay); 
                md = nts.uk.text.padLeft(md, '0', 4); 
                return moment.utc(md, "MMDD").format("MMMDo"); 
            }
            
            return monthDay; 
        }
        
        //Check the total month and return false if that total < 12
        checkTotalMonths(index: number): boolean {
            var self = this;
            
            if(self.count() == 1) {
                self.count(0);
                return true;
            }
            
            if(self.items()[index].lengthOfServiceYears() != null || self.items()[index].lengthOfServiceMonths() != null) {
                var totalMonths = Number(self.items()[index].lengthOfServiceMonths()) + Number(self.items()[index].grantDays());
                
                if (Number(self.items()[index].lengthOfServiceYears()) == 0 && totalMonths < 12) {            
                    return false;
                }
            }
            
            return true;
        }
    }
    
    export class Item {
        grantYearHolidayNo: KnockoutObservable<number>;
        conditionNo: KnockoutObservable<number>;
        yearHolidayCode: KnockoutObservable<string>;        
        lengthOfServiceYears: KnockoutObservable<number>;
        lengthOfServiceMonths: KnockoutObservable<number>;
        grantDays: KnockoutObservable<number>;
        limitedTimeHdDays: KnockoutObservable<number>;
        limitedHalfHdCnt: KnockoutObservable<number>;
        grantReferenceDate: KnockoutObservable<number>;
        grantSimultaneity: KnockoutObservable<boolean>;
        grantDate: KnockoutObservable<string>;
        gdEnable: KnockoutObservable<boolean>;
        ltdEnable: KnockoutObservable<boolean>;
        lthEnable: KnockoutObservable<boolean>;
        
        constructor(param: IItem) {
            var self = this;
            self.grantYearHolidayNo = ko.observable(param.grantYearHolidayNo);
            self.conditionNo = ko.observable(param.conditionNo);
            self.yearHolidayCode = ko.observable(param.yearHolidayCode);            
            self.lengthOfServiceYears = ko.observable(param.lengthOfServiceYears);
            self.lengthOfServiceMonths = ko.observable(param.lengthOfServiceMonths);
            self.grantDays = ko.observable(param.grantDays);
            self.limitedTimeHdDays = ko.observable(param.limitedTimeHdDays);
            self.limitedHalfHdCnt = ko.observable(param.limitedHalfHdCnt);
            self.grantReferenceDate = ko.observable(param.grantReferenceDate);
            self.grantSimultaneity = ko.observable(param.grantSimultaneity);    
            self.grantDate = ko.observable(param.grantDate);   
            self.grantSimultaneity.subscribe(function(value){
                checkAllowPayBelow(self);
            });
            self.gdEnable = ko.observable(param.gdEnable);  
            self.ltdEnable = ko.observable(param.ltdEnable);  
            self.lthEnable = ko.observable(param.lthEnable);  
        }
    }
    
    export interface IItem {
        grantYearHolidayNo: number;
        conditionNo: number;
        yearHolidayCode: string;
        lengthOfServiceYears: number;
        lengthOfServiceMonths: number;
        grantDays: number;
        limitedTimeHdDays: number;
        limitedHalfHdCnt: number;
        grantReferenceDate: number;
        grantSimultaneity: boolean;   
        grantDate: string;
        gdEnable: boolean;
        ltdEnable: boolean;
        lthEnable: boolean; 
    }
     
    export class GrantReferenceDateOption {
        code: number;
        name: string;
        
        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    
    function checkAllowPayBelow(item: Item): void {
        var self = nts.uk.ui._viewModel.content;
        var index = _.indexOf(self.items(), item);
        self.checkAllowPayBelow(index, item.grantSimultaneity());
    }

}