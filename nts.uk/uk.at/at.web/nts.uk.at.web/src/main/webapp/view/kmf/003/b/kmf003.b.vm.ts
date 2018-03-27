module nts.uk.at.view.kmf003.b.viewmodel {
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
        lengthOfServiceData: any;
        grantHdData: any;
        checkDataExisted: KnockoutObservable<boolean>;
        flag: KnockoutObservable<boolean>;
        
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
            
            self.checkDataExisted = ko.observable(false);
            self.flag = ko.observable(true);
            
            if(self.conditionData.useCondition == true){
                var style = $('<style>table td.allow-pay { display: table-cell; }</style>');
                $('html > head').append(style);
            } else {
                var style = $('<style>table td.allow-pay { display: none; } table.contents-data td.pay-day-cal { width: 260px }</style>');
                $('html > head').append(style);
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
            
            $.when(self.getLengthOfService(), self.getGrantHdTbl()).done(function() {
                let combinedData = [];
                
                for(var i = 0; i < self.lengthOfServiceData.length; i++){
                    var gNum = self.grantHdData[i] != null ? self.grantHdData[i].grantNum : self.grantHdData[self.grantHdData.length - 1].grantNum;
                    if(self.lengthOfServiceData[i].grantNum == gNum) {
                        var item : IItem = {
                            grantYearHolidayNo: self.lengthOfServiceData[i].grantNum,
                            conditionNo: self.grantHdData[i] != null ? self.grantHdData[i].conditionNo : (self.grantHdData[self.grantHdData.length - 1] != null ? self.grantHdData[self.grantHdData.length - 1].conditionNo : ""),
                            yearHolidayCode: self.lengthOfServiceData[i].yearHolidayCode,
                            lengthOfServiceYears: self.lengthOfServiceData[i].year,
                            lengthOfServiceMonths: self.lengthOfServiceData[i].month,
                            grantDays: self.grantHdData[i] != null ? self.grantHdData[i].grantDays : (self.grantHdData[self.grantHdData.length - 1] != null ? self.grantHdData[self.grantHdData.length - 1].grantDays : ""),
                            limitedTimeHdDays: self.grantHdData[i] != null ? self.grantHdData[i].limitTimeHd : (self.grantHdData[self.grantHdData.length - 1] != null ? self.grantHdData[self.grantHdData.length - 1].limitTimeHd : ""),
                            limitedHalfHdCnt: self.grantHdData[i] != null ? self.grantHdData[i].limitDayYear : (self.grantHdData[self.grantHdData.length - 1] != null ? self.grantHdData[self.grantHdData.length - 1].limitDayYear : ""),
                            grantReferenceDate: self.lengthOfServiceData[i].standGrantDay,
                            grantSimultaneity: self.lengthOfServiceData[i].allowStatus,
                            grantDate: ""
                        };
                    } else {
                        var item : IItem = {
                            grantYearHolidayNo: self.lengthOfServiceData[i].grantNum,
                            conditionNo: "",
                            yearHolidayCode: self.lengthOfServiceData[i].yearHolidayCode,
                            lengthOfServiceYears: self.lengthOfServiceData[i].year,
                            lengthOfServiceMonths: self.lengthOfServiceData[i].month,
                            grantDays: "",
                            limitedTimeHdDays: "",
                            limitedHalfHdCnt: "",
                            grantReferenceDate: self.lengthOfServiceData[i].standGrantDay,
                            grantSimultaneity: self.lengthOfServiceData[i].allowStatus,
                            grantDate: ""
                        };
                    }
                    
                    combinedData.push(new Item(item));
                }
                
                if(combinedData.length > 0) {
                    self.checkDataExisted(true);
                }
                
                self.bindData(combinedData);
                
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        /**
         * Get data from LengthOfServiceTbl.
         */
        getLengthOfService(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            
            service.findByCode(self.conditionData.code).done(function(data){
                let sortedData = _.orderBy(data, ['grantNum'], ['asc']);
                self.lengthOfServiceData = sortedData;
                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        /**
         * Get data from GrantHdTbl.
         */
        getGrantHdTbl(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            var conditionNo = 1;
            
            service.findGrantByCode(conditionNo, self.conditionData.code).done(function(data){
                let sortedData = _.orderBy(data, ['grantNum'], ['asc']);
                self.grantHdData = sortedData;
                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        /**
         * Binding data to screen.
         */
        bindData(data: any){
            var self = this;
            var flagDay = false;
            var flagYear = false;
            
            self.items.removeAll();
            
            service.checkData().done(function(check){
                if(check.manageType == 1 && check.reference == 1) {
                    flagDay = true;
                }

                if (check.maxManageType == 1 && check.maxReference == 1 && check.timeManageType == 1) {
                    flagYear = true;
                }
                
                //Update case
                for(var i = 0; i < data.length; i++){
                    var item : IItem = {
                        grantYearHolidayNo: data[i].grantYearHolidayNo(),
                        conditionNo: data[i].conditionNo(),
                        yearHolidayCode: data[i].yearHolidayCode(),
                        lengthOfServiceYears: data[i].lengthOfServiceYears(),
                        lengthOfServiceMonths: data[i].lengthOfServiceMonths(),
                        grantDays: data[i].grantDays(),
                        limitedTimeHdDays: data[i].limitedTimeHdDays(),
                        limitedHalfHdCnt: data[i].limitedHalfHdCnt(),
                        grantReferenceDate: data[i].grantSimultaneity() == 0 ? data[i].grantReferenceDate() : 1,
                        grantReferenceDateEnable: data[i].grantSimultaneity() == 1 ? false : true,
                        grantSimultaneity: data[i].grantSimultaneity(),
                        grantDate: data[i].grantDate(),
                        ltdEnable: flagYear,
                        lthEnable: flagDay 
                    };
                    self.items.push(new Item(item));
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
                        grantReferenceDate: data.length > 0 ? (data[data.length - 1].grantSimultaneity() ? 1 : 0) : 0,
                        grantReferenceDateEnable: data.length > 0 ? (data[data.length - 1].grantSimultaneity() ? false : true) : true,
                        grantSimultaneity: data.length > 0 ? data[data.length - 1].grantSimultaneity() : false,
                        grantDate: "",
                        ltdEnable: flagYear,
                        lthEnable: flagDay 
                    };
                    self.items.push(new Item(item));    
                }
            });
        }
        
        /**
         * Calculate data function.
         */
        calculate() {
            var self = this;
            var checkErr = true;
            
            if (nts.uk.ui.errors.hasError()) {
                return;    
            }
            
            if(self.referenceDate() !== ""){
                var monthDay = String(self.conditionData.dateSelected); 
                monthDay = nts.uk.text.padLeft(monthDay, '0', 4); 
                var dateSelected = moment.utc(monthDay, "MMDDYY"); 
                var grantHolidayTblList = [];
                _.forEach(self.items(), function(item) {
                     if (item.lengthOfServiceYears() != null || item.lengthOfServiceMonths() != null || item.grantDays() != null || item.limitedTimeHdDays() != null || item.limitedHalfHdCnt() != null) {
                         grantHolidayTblList.push({
                            grantNum: item.grantYearHolidayNo(),
                            conditionNo: item.conditionNo(),
                            yearHolidayCode: item.yearHolidayCode(),
                            year: item.lengthOfServiceYears(),
                            month: item.lengthOfServiceMonths(),
                            grantDays: item.grantDays(),
                            limitTimeHd: item.limitedTimeHdDays(),
                            limitDayYear: item.limitedHalfHdCnt(),
                            standGrantDay: item.grantReferenceDate(),
                            allowStatus: item.grantSimultaneity() ? 1 : 0
                        });
                    }
                }); 
                
                // if no data then return
                if (grantHolidayTblList == null || grantHolidayTblList.length == 0) {
                    return;
                }
                
                _.forEach(grantHolidayTblList, function(item) {
                    if(checkErr && item.month != null && item.year != null && (item.grantDays == null || item.grantDays == "")) {
                        checkErr = false;
                        nts.uk.ui.dialog.alert({ messageId: "Msg_270" }).then(() => {
                            $('#b2_1').focus();
                        });
                        return;
                    }
                });
                    
                var dataTranfer: any = {
                    grantHolidayTblList: grantHolidayTblList,
                    useSimultaneousGrant: self.conditionData.useCondition ? 1 : 0, 
                    referDate: new Date(self.referenceDate()), 
                    simultaneousGrantDate: dateSelected
                }
                
                if(checkErr) {
                    service.calculateGrantDate(dataTranfer).done(function(res) {
                        var results = [];
                        for(var i = 0; i < res.length; i++){
                            var item : IItem = {
                                grantYearHolidayNo: res[i].grantNum,
                                conditionNo: res[i].conditionNo,
                                yearHolidayCode: res[i].yearHolidayCode,
                                lengthOfServiceYears: res[i].year,
                                lengthOfServiceMonths: res[i].month,
                                grantDays: res[i].grantDays,
                                limitedTimeHdDays: res[i].limitTimeHd,
                                limitedHalfHdCnt: res[i].limitDayYear,
                                grantReferenceDate: res[i].standGrantDay,
                                grantSimultaneity: res[i].allowStatus,
                                grantDate: res[i].grantDate
                            };
                            
                            results.push(new Item(item));
                        }
                        
                        self.bindData(results);
                    }).fail(function(error){
                        nts.uk.ui.dialog.alertError({ messageId: error.messageId }).then(() => {
                            if(error.messageId === "Msg_266") {
                                $('.year-input1').focus();
                            } else if(error.messageId === "Msg_268") {
                                
                            } else if(error.messageId === "Msg_269") {
                                $('.year-input1').focus();
                            } else if(error.messageId === "Msg_270") {
                                $('#b2_1').focus();
                            }
                        }); 
                    });
                }
            } else {
                nts.uk.ui.dialog.alert({ messageId: "Msg_272" }).then(() => {
                    $('#reference-date').focus();
                });
            }
        }
        
        /**
         * Add or Update data to db.
         */
        submit() {
            var self = this;
            var checkErr = true;
            
            $('#reference-date').ntsError('clear');
            
            if (nts.uk.ui.errors.hasError()) {
                return;    
            }

            var grantHolidayTblList = [];
            _.forEach(self.items(), function(item, index) {
                grantHolidayTblList.push({
                    grantNum: index + 1,
                    conditionNo: item.conditionNo(),
                    yearHolidayCode: item.yearHolidayCode(),
                    year: item.lengthOfServiceYears(),
                    month: item.lengthOfServiceMonths(),
                    grantDays: item.grantDays(),
                    limitTimeHd: item.limitedTimeHdDays(),
                    limitDayYear: item.limitedHalfHdCnt(),
                    standGrantDay: item.grantReferenceDate(),
                    allowStatus: item.grantSimultaneity() ? 1 : 0
                });
            });
            
            // if no data then return
            if (grantHolidayTblList == null || grantHolidayTblList.length == 0) {
                nts.uk.ui.windows.setShared("KMF003_HAVE_DATA", false);
                return;
            }
            
            _.forEach(grantHolidayTblList, function(item) {
                if(checkErr) {
                    if((item.year != null || item.month != null) && item.grantDays == null) {
                        if(Number(item.year) == 0 && Number(item.month) == 0) {
                            checkErr = false;
                            nts.uk.ui.dialog.alert({ messageId: "Msg_268" }).then(() => {
                                $('#b2_1').focus();
                            });
                            return;
                        } else {
                            checkErr = false;
                            nts.uk.ui.dialog.alert({ messageId: "Msg_270" }).then(() => {
                                $('#b2_1').focus();
                            });
                            return;
                        }                        
                    } else if((item.year == null || item.month == null) && item.grantDays != null) {
                        if(Number(item.year) == 0 && Number(item.month) == 0) {
                            checkErr = false;
                            nts.uk.ui.dialog.alert({ messageId: "Msg_268" }).then(() => {
                                $('#b2_1').focus();
                            });
                            return;
                        }                       
                    }
                }
            });
            
            if(checkErr){
                service.addYearHolidayGrant(grantHolidayTblList).done(function(){
                    nts.uk.ui.windows.setShared("KMF003_HAVE_DATA", true);
                    self.checkDataExisted(true);
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                }).fail(function(error){
                    nts.uk.ui.dialog.alertError({ messageId: error.messageId }).then(() => {
                        if(error.messageId === "Msg_266") {
                            $('.year-input1').focus();
                        } else if(error.messageId === "Msg_268") {
                            
                        } else if(error.messageId === "Msg_269") {
                            $('.year-input1').focus();
                        } else if(error.messageId === "Msg_270") {
                            $('#b2_1').focus();
                        }
                    }); 
                });
            }
        }
        
        /**
         * Close dialog.
         */
        cancel() {
            var self = this;
            nts.uk.ui.windows.setShared("KMF003_HAVE_DATA", self.checkDataExisted());
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
        
        //Set check or uncheck checkbox list
        checkAllowPayBelow(index: number, value: boolean): void {
            var self = this;
            var checkMonths = self.checkTotalMonths(index);
            
            if (!checkMonths && value) {
                if(self.flag()) {
                    self.count(1);
                    self.flag(false);
                    self.items()[index].grantSimultaneity(false);   
                    nts.uk.ui.dialog.alert({ messageId: "Msg_267" }).then(() => {
                        for (let i = 0; i < self.items().length; i++) {
                            self.items()[i].grantReferenceDate(self.lengthOfServiceData[i] != null ? self.lengthOfServiceData[i].standGrantDay : 0);
                            self.items()[i].grantSimultaneity(false);
                        }
                        
                        $('.year-input' + index).focus();
                        self.flag(true);
                    });
                    return;
                }
            }
            
            if (value) {
                for (let i = index; i < self.items().length; i++) {
                    self.items()[i].grantReferenceDate(1);
                    self.items()[i].grantReferenceDateEnable(false);
                    self.items()[i].grantSimultaneity(value);
                }
            } else {
                if(index == 0) {
                    self.items()[index].grantReferenceDateEnable(true);
                    self.items()[index].grantSimultaneity(value);
                } else {
                    for (let i = 0; i < index; i++) {
                        self.items()[i].grantReferenceDateEnable(true);
                        self.items()[i + 1].grantReferenceDateEnable(true);
                        self.items()[i].grantSimultaneity(value);
                    }
                }
            }
            
            self.items.valueHasMutated();
        }
        
        //Check the total month and return false if that total < 12
        checkTotalMonths(index: number): boolean {
            var self = this;
            
            if(self.count() == 1) {
                self.count(0);
                return true;
            }
            
            if(Number(self.items()[0].lengthOfServiceYears()) <= 0 && Number(self.items()[0].lengthOfServiceMonths()) <= 0 && Number(self.items()[0].grantDays()) <= 0 
                        && Number(self.items()[0].limitedHalfHdCnt()) <= 0 && Number(self.items()[0].limitedTimeHdDays()) <= 0) {
                return false;
            }
            
            if(self.items()[index].grantDays() != null || self.items()[index].lengthOfServiceMonths() != null) {
                var totalMonths = Number(self.items()[index].lengthOfServiceMonths()) + Number(self.items()[index].grantDays());
                
                if (Number(self.items()[index].lengthOfServiceYears()) <= 0 && totalMonths < 12) {            
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
        grantReferenceDateEnable: KnockoutObservable<boolean>;
        grantSimultaneity: KnockoutObservable<boolean>;
        grantDate: KnockoutObservable<string>;
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
            self.grantReferenceDateEnable = ko.observable(param.grantReferenceDateEnable);
            self.grantSimultaneity = ko.observable(param.grantSimultaneity);    
            self.grantDate = ko.observable(param.grantDate);   
            self.grantSimultaneity.subscribe(function(value){
                checkAllowPayBelow(self);
            });
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
        grantReferenceDateEnable: boolean;   
        grantSimultaneity: boolean;   
        grantDate: string;
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
        var itemJS = ko.toJS(item);
        
        var index = itemJS.grantYearHolidayNo - 1;
        self.checkAllowPayBelow(index, item.grantSimultaneity());
    }

}