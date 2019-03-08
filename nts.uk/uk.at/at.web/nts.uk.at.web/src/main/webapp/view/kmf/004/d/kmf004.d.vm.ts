module nts.uk.at.view.kmf004.d.viewmodel {
    import isNullOrEmpty = nts.uk.text.isNullOrEmpty;
    export class ScreenModel {
        grantDates: KnockoutObservableArray<GrantDateTbl>;
        lstGrantDate: KnockoutObservableArray<GrantDateItem>;
        columns: KnockoutObservableArray<any>;
        selectedCode: KnockoutObservable<string>;
        grantDateCode: KnockoutObservable<string>;
        grantDateName: KnockoutObservable<string>;
        provisionCheck: KnockoutObservable<boolean>;
        items: KnockoutObservableArray<Item>;
        editMode: KnockoutObservable<boolean>;
        fixedAssignCheck: KnockoutObservable<boolean>;
        numberOfDays: KnockoutObservable<number>;
        sphdCode: any;
        codeEnable: KnockoutObservable<boolean>;
        numberOfDaysEnable: KnockoutObservable<boolean>;
        daysReq: KnockoutObservable<boolean>;
        newModeEnable: KnockoutObservable<boolean>;
        isDelete: KnockoutObservable<boolean>;
        
        provisionActive: KnockoutObservable<boolean> = ko.observable(true);

        constructor() {
            let self = this;
            
            self.sphdCode = nts.uk.ui.windows.getShared("KMF004_A_DATA");
            
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KMF004_5"), key: 'grantDateCode', width: 60 },
                { headerText: nts.uk.resource.getText("KMF004_6"), key: 'grantDateName', width: 160, formatter: _.escape}
            ]);
            
            self.grantDates = ko.observableArray([]);
            self.lstGrantDate = ko.observableArray([]);
            self.selectedCode = ko.observable("");

            self.grantDateCode = ko.observable("");
            self.grantDateName = ko.observable("");

            self.provisionCheck = ko.observable(false);
            
            self.newModeEnable = ko.observable(true);
            self.isDelete = ko.observable(false);

            self.items = ko.observableArray([]);
            self.editMode = ko.observable(false); 

            self.fixedAssignCheck = ko.observable(false); 
            self.numberOfDays = ko.observable();
            
            self.codeEnable = ko.observable(true);
            
            self.numberOfDaysEnable = ko.observable(false);
            
            self.daysReq = ko.observable(false);

            self.selectedCode.subscribe(function(grantDateCode) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                if(grantDateCode.length > 0){
                    var selectedItem = _.find(self.grantDates, function(o) { return o.grantDateCode == grantDateCode; });
                    
                    self.grantDateCode(selectedItem.grantDateCode);
                    self.grantDateName(selectedItem.grantDateName);
                    self.provisionCheck(selectedItem.specified);
                    self.fixedAssignCheck(selectedItem.fixedAssign);
                    self.numberOfDays(selectedItem.numberOfDays);
                    if (self.provisionCheck() == true) {
                        self.provisionActive(false);
                    } else {
                        self.provisionActive(true);
                    }
                    self.codeEnable(false);
                    self.editMode(true);
                    self.newModeEnable(true);
                    
                    if(!self.isDelete()) {
                        $("#inpPattern").focus();
                    }
                    
                    service.findByGrantDateCd(self.sphdCode, selectedItem.grantDateCode).done(function(data) {
                        self.elapseBind(data);
                    }).fail(function(res) {
                        
                    });
                }
            });  
            
            self.fixedAssignCheck.subscribe(function(value) {
                if(value){
                    self.numberOfDaysEnable(true);
                    self.daysReq(true);
                    // clear all error
                    nts.uk.ui.errors.clearAll();
                } else {
                    self.numberOfDaysEnable(false);
                    self.daysReq(false);
                    // clear all error
                    nts.uk.ui.errors.clearAll();
                }
            });  
        }

        /** get data when start dialog **/
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();

            $.when(self.getData()).done(function() {
                                    
                if (self.lstGrantDate().length > 0) {
                    self.selectedCode(self.lstGrantDate()[0].grantDateCode);
                    self.selectedCode.valueHasMutated();
                } else {
                    self.newModeEnable(false);
                    self.newMode();
                }
                
                nts.uk.ui.errors.clearAll();
                
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            nts.uk.ui.errors.clearAll();
            
            return dfd.promise();
        }
        
        /** get data from db **/
        getData(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            
            self.lstGrantDate([]);
            
            service.findBySphdCd(self.sphdCode).done(function(data) {
                self.grantDates = data;
                
                _.forEach(data, function(item) {
                    self.lstGrantDate.push(new GrantDateItem(item.grantDateCode, item.grantDateName));
                });
                
                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        /** bind elapse year data **/
        elapseBind(data: any) {
            let self = this; 
            
            self.items([]);
            
            if(data.length > 0) {
                for(var i = 0; i < data.length; i++){
                    var item : IItem = {
                        grantDateCode: data[i].grantDateCode,
                        elapseNo: data[i].elapseNo,
                        months: data[i].months,
                        years: data[i].years,
                        grantedDays: data[i].grantedDays
                    };
                    
                    self.items.push(new Item(item));
                }
                
                for(var j = data.length; j < 20; j++) {
                    var item : IItem = {
                        grantDateCode: data[0].grantDateCode,
                        elapseNo: j + 1,
                        months: null,
                        years: null,
                        grantedDays: null
                    };
                    
                    self.items.push(new Item(item));    
                }
            } else {
                for(var i = 0; i < 20; i++){
                    var item : IItem = {
                        grantDateCode: self.grantDateCode(),
                        elapseNo: i + 1,
                        months: null,
                        years: null,
                        grantedDays: null
                    };
                    
                    self.items.push(new Item(item));
                }
            }
        }
        
        /** update or insert data when click button register **/
        register() {  
            let self = this; 
            let checkErr = false;
                        
            $("#inpCode").trigger("validate");
            $("#inpPattern").trigger("validate");
                
            if (nts.uk.ui.errors.hasError()) {
                return;       
            }
            
            
            
            let elapseData = [];
            _.forEach(self.items(), function(item, index) {
                elapseData.push({
                    specialHolidayCode: self.sphdCode,
                    grantDateCode: self.grantDateCode(),
                    elapseNo: index + 1,
                    months: item.months(),
                    years: item.years(),
                    grantedDays: item.grantedDays()
                });
            });
            
            if(elapseData.length > 0) {
                var evens = _.remove(elapseData, function(item) {
                    return isNullOrEmpty(item.months) && isNullOrEmpty(item.years) && isNullOrEmpty(item.grantedDays);
                });
            }
            
            _.forEach(elapseData, function(item) {
                if(isNullOrEmpty(item.grantedDays)  && (!isNullOrEmpty(item.months)|| !isNullOrEmpty(item.months))) {
                    nts.uk.ui.dialog.alertError({ messageId: "Msg_101" });
                    checkErr = true;
                    return;
                }
            });
            
            // 「経過年数に対する付与日数」は1件以上登録すること
            if(elapseData.length <= 0) {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_144" });
                return;
            }
            
            let dataItem : service.GrantDateTblDto = {
                specialHolidayCode: self.sphdCode,
                grantDateCode: self.grantDateCode(),
                grantDateName: self.grantDateName(),
                isSpecified: self.provisionCheck(),
                fixedAssign: self.fixedAssignCheck(),
                numberOfDays: self.numberOfDays(),
                elapseYear: elapseData
            };
            
            if(self.daysReq() && dataItem.numberOfDays === "") {
                $("#granted-days-number").ntsError("set", "固定付与日数を入力してください", "FND_E_REQ_INPUT");
                return;
            }
            
            if(!checkErr) {
                if(!self.editMode()) {
                    nts.uk.ui.block.invisible();
                    service.addGrantDate(dataItem).done(function(errors){
                        if (errors && errors.length > 0) {
                            self.addListError(errors);    
                        } else {
                            $.when(self.getData()).done(function() {
                                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => { 
                                    self.selectedCode(dataItem.grantDateCode);
                                    self.selectedCode.valueHasMutated();
                                });
                            });
                        }
                    }).fail(function(error){
                        nts.uk.ui.dialog.alertError({ messageId: error.messageId });
                    }).always(function() {
                        nts.uk.ui.block.clear();
                    });
                } else {
                    nts.uk.ui.block.invisible();
                    service.updateGrantDate(dataItem).done(function(errors){
                        if (errors && errors.length > 0) {
                            self.addListError(errors);    
                        } else {
                            $.when(self.getData()).done(function() {
                                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => { 
                                    self.selectedCode(dataItem.grantDateCode);
                                    self.selectedCode.valueHasMutated();
                                });
                            });
                        }
                    }).fail(function(error){
                        nts.uk.ui.dialog.alertError({ messageId: error.messageId });
                    }).always(function() {
                        nts.uk.ui.block.clear();
                    });
                }
            }
        }
        
        prescribedEnable() {
            let self = this;
            return !(self.editMode() == true && self.provisionCheck() == true);
        }
        
        //  new mode 
        newMode() {
            let self = this;
            
            self.codeEnable(true);
            self.newModeEnable(false);
            self.editMode(false);
            self.selectedCode("");
            self.grantDateCode("");
            self.grantDateName("");
            if (self.lstGrantDate().length) {
                self.provisionCheck(false);
            } else {
                self.provisionCheck(true);
            }
            self.provisionActive(true);
            self.fixedAssignCheck(false);
            self.numberOfDays("");
            self.elapseBind([]);
            
            $("#inpCode").focus();
            
            nts.uk.ui.errors.clearAll();  
        }
        
        /** remove item from list **/
        remove() {
            let self = this;
            
            
            let count = 0;
            for (let i = 0; i <= self.lstGrantDate().length; i++){
                if(self.lstGrantDate()[i].grantDateCode == self.selectedCode()){
                    count = i;
                    break;
                }
            }
            
            if(self.provisionCheck()) {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_1219" });
                nts.uk.ui.block.clear();
            } else {
                nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                    service.deleteGrantDate(self.sphdCode, self.grantDateCode()).done(function() {
                        self.getData().done(function(){
                            self.isDelete(true);
                            // if number of item from list after delete == 0 
                            if(self.lstGrantDate().length==0){
                                self.newMode();
                                return;
                            }
                            // delete the last item
                            if(count == ((self.lstGrantDate().length))){
                                self.selectedCode(self.lstGrantDate()[count-1].grantDateCode);
                                return;
                            }
                            // delete the first item
                            if(count == 0 ){
                                self.selectedCode(self.lstGrantDate()[0].grantDateCode);
                                return;
                            }
                            // delete item at mediate list 
                            else if(count > 0 && count < self.lstGrantDate().length){
                                self.selectedCode(self.lstGrantDate()[count].grantDateCode);    
                                return;
                            }
                        });
                        
                        nts.uk.ui.dialog.info({ messageId: "Msg_16" }).then(() => {
                            if(self.editMode()) {
                                $("#inpPattern").focus();
                            } else {
                                $("#inpCode").focus();
                            }
                        });
                    }).fail(function(error) {
                        nts.uk.ui.dialog.alertError(error.message);
                    }).always(function() {
                        nts.uk.ui.block.clear();      
                    });
                })
            }
        } 
        
        /** close dialog **/
        closeDialog(){
            nts.uk.ui.windows.close();
        }
        
        /** set errors **/
        addListError(errorsRequest: Array<string>) {
            var errors = [];
            _.forEach(errorsRequest, function(err) {
                errors.push({message: nts.uk.resource.getMessage(err), messageId: err, supplements: {} });
            });
            
            nts.uk.ui.dialog.bundledErrors({ errors: errors});
        }
    }
    
    class GrantDateItem {
        grantDateCode: KnockoutObservable<string>;
        grantDateName: KnockoutObservable<string>;
        
        constructor(grantDateCode: string, grantDateName: string) {
            this.grantDateCode = grantDateCode;
            this.grantDateName = grantDateName;       
        }
    }

    export class GrantDateTbl {
        grantDateCode: KnockoutObservable<string>;
        grantDateName: KnockoutObservable<string>;
        isSpecified: KnockoutObservable<number>;
        fixedAssign: KnockoutObservable<number>;
        numberOfDays: KnockoutObservable<number>;

        constructor(grantDateCode: string, grantDateName: string, isSpecified: number, fixedAssign: number, numberOfDays: number) {
            this.grantDateCode = grantDateCode;
            this.grantDateName = grantDateName;
            this.isSpecified = isSpecified;
            this.fixedAssign = fixedAssign;
            this.numberOfDays = numberOfDays;
        }
    }

    export interface IItem{
        grantDateCode: KnockoutObservable<string>;
        elapseNo: KnockoutObservable<number>;
        months: KnockoutObservable<number>;
        years: KnockoutObservable<number>;
        grantedDays: KnockoutObservable<number>;
    }

    export class Item {
        grantDateCode: KnockoutObservable<string>;
        elapseNo: KnockoutObservable<number>;
        months: KnockoutObservable<number>;
        years: KnockoutObservable<number>;
        grantedDays: KnockoutObservable<number>;

        constructor(param: IItem) {
            var self = this;
            self.grantDateCode = ko.observable(param.grantDateCode);
            self.elapseNo = ko.observable(param.elapseNo);
            self.months = ko.observable(param.months);
            self.years = ko.observable(param.years);
            self.grantedDays = ko.observable(param.grantedDays);
        }
    }
}