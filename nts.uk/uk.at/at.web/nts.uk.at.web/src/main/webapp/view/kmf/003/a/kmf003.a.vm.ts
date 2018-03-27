module nts.uk.at.view.kmf003.a.viewmodel {
    export class ScreenModel {
        //Grid data
        columns: KnockoutObservable<any>;
        singleSelectedCode: KnockoutObservable<any>;
        items: KnockoutObservableArray<ItemModel>;
        currentCode: KnockoutObservable<any>;
        
        //Top input form
        code: KnockoutObservable<string>;
        editMode: KnockoutObservable<boolean>;
        name: KnockoutObservable<string>;
        useConditionCls: KnockoutObservable<boolean>;  
        grantDate: KnockoutObservable<string>;
        enableGrantDate: KnockoutObservable<boolean>;  
        A6_2Data: KnockoutObservableArray<any>;
        A6_2SelectedRuleCode: any;
        A7_4Data: KnockoutObservableArray<any>;
        A7_4SelectedRuleCode: any;
        symbols: KnockoutObservable<string>;
        limitedValue01: KnockoutObservable<string>;
        limitedValue02: KnockoutObservable<string>;
        limitedValue03: KnockoutObservable<string>;
        limitedValue04: KnockoutObservable<string>;
        limitedValue05: KnockoutObservable<string>;
        
        //Bottom input form
        useCls01: KnockoutObservable<boolean>;
        useCls02: KnockoutObservable<boolean>;
        useCls03: KnockoutObservable<boolean>;
        useCls04: KnockoutObservable<boolean>;
        useCls05: KnockoutObservable<boolean>;  
        
        useCls02Enable: KnockoutObservable<boolean>;
        useCls03Enable: KnockoutObservable<boolean>;
        useCls04Enable: KnockoutObservable<boolean>;
        useCls05Enable: KnockoutObservable<boolean>;
        
        conditionValue01: KnockoutObservable<string>;
        conditionValue02: KnockoutObservable<string>;
        conditionValue03: KnockoutObservable<string>;
        conditionValue04: KnockoutObservable<string>;
        conditionValue05: KnockoutObservable<string>;
        note: KnockoutObservable<string>;
        conditionValue01Enable: KnockoutObservable<boolean>;
        conditionValue02Enable: KnockoutObservable<boolean>;
        conditionValue03Enable: KnockoutObservable<boolean>;
        conditionValue04Enable: KnockoutObservable<boolean>;
        conditionValue05Enable: KnockoutObservable<boolean>;
        btnSetting02Enable: KnockoutObservable<boolean>;
        btnSetting03Enable: KnockoutObservable<boolean>;
        btnSetting04Enable: KnockoutObservable<boolean>;
        btnSetting05Enable: KnockoutObservable<boolean>;
        
        isNewMode: KnockoutObservable<boolean>;
        grantHTData: any;
        
        showLblSet01: KnockoutObservable<boolean>;
        showLblSet02: KnockoutObservable<boolean>;
        showLblSet03: KnockoutObservable<boolean>;
        showLblSet04: KnockoutObservable<boolean>;
        showLblSet05: KnockoutObservable<boolean>;

        constructor() {
            var self = this;
            
            //Grid data
            self.items = ko.observableArray([]);
            
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText("KMF003_8"), prop: 'code', width: 50 },
                { headerText: nts.uk.resource.getText("KMF003_9"), prop: 'name', width: 200, formatter: _.escape }
            ]);
            
            self.singleSelectedCode = ko.observable("");
            self.currentCode = ko.observable();
            
            self.isNewMode = ko.observable(false);
            
            self.showLblSet01 = ko.observable(false);
            self.showLblSet02 = ko.observable(false);
            self.showLblSet03 = ko.observable(false);
            self.showLblSet04 = ko.observable(false);
            self.showLblSet05 = ko.observable(false);
            
            //Controls display
            self.controlsDisplay();
            
            //Enable or disable for setting form
            self.conditionSettingForm();
            
            //Bind data to from when user select item on grid
            self.singleSelectedCode.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                self.isNewMode(true);
     
                $('.a9_1').show();
                $('.a9_2').show();
                $('.a9_3').show();
                $('.a9_4').show();
                $('.a9_5').show();
                $('.a9_6').hide();
                
                if(value.length > 0){
                    service.findByCode(value).done(function(data) {
                        self.editMode(false);
                        self.useCls01(true);
                        
                        self.conditionValue01Enable(true);
                        
                        self.useCls02Enable(true);
                        self.useCls03Enable(true);
                        self.useCls04Enable(true);
                        self.useCls05Enable(true);
                        
                        if(data.grantConditions.length > 0) {
                            for(var i = 0; i < data.grantConditions.length; i++) {
                                self.getGHTdata(data.grantConditions[i].conditionNo, data.grantConditions[i].yearHolidayCode);                                
                            }
                        }
                        
                        self.code(data.yearHolidayCode);
                        self.name(data.yearHolidayName);
                        self.A6_2SelectedRuleCode(data.standardCalculation);  
                        self.A7_4SelectedRuleCode(data.calculationMethod);
                        self.useConditionCls(data.useSimultaneousGrant === 1 ? true : false);
                        self.grantDate(data.simultaneousGrandMonthDays.toString());
                        self.note(data.yearHolidayNote);                    
                        self.conditionValue01(data.grantConditions[0] && (data.grantConditions[0].conditionValue != null ? data.grantConditions[0].conditionValue.toString() : ""));
                        self.useCls02(data.grantConditions[1] && data.grantConditions[1].useConditionAtr == 1 ? true : false);
                        self.conditionValue02(data.grantConditions[1] && (data.grantConditions[1].conditionValue != null ? data.grantConditions[1].conditionValue.toString() : ""));
                        self.useCls03(data.grantConditions[2] && data.grantConditions[2].useConditionAtr == 1 ? true : false);
                        self.conditionValue03(data.grantConditions[2] && (data.grantConditions[2].conditionValue != null ? data.grantConditions[2].conditionValue.toString() : ""));
                        self.useCls04(data.grantConditions[3] && data.grantConditions[3].useConditionAtr == 1 ? true : false);
                        self.conditionValue04(data.grantConditions[3] && (data.grantConditions[3].conditionValue != null ? data.grantConditions[3].conditionValue.toString() : ""));
                        self.useCls05(data.grantConditions[4] && data.grantConditions[4].useConditionAtr == 1 ? true : false);
                        self.conditionValue05(data.grantConditions[4] && (data.grantConditions[4].conditionValue != null ? data.grantConditions[4].conditionValue.toString() : ""));
                        self.btnSetting02Enable(data.grantConditions[1] && data.grantConditions[1].useConditionAtr == 1);
                        self.btnSetting03Enable(data.grantConditions[2] && data.grantConditions[2].useConditionAtr == 1);
                        self.btnSetting04Enable(data.grantConditions[3] && data.grantConditions[3].useConditionAtr == 1);
                        self.btnSetting05Enable(data.grantConditions[4] && data.grantConditions[4].useConditionAtr == 1);  
                        self.showLblSet01(data.grantConditions[0] ? data.grantConditions[0].hadSet : false);
                        self.showLblSet02(data.grantConditions[1] ? data.grantConditions[1].hadSet : false);
                        self.showLblSet03(data.grantConditions[2] ? data.grantConditions[2].hadSet : false);
                        self.showLblSet04(data.grantConditions[3] ? data.grantConditions[3].hadSet : false);
                        self.showLblSet05(data.grantConditions[4] ? data.grantConditions[4].hadSet : false); 
                                    
                        self.setFocus();  
                        
                        self.setConditionValueChanges();
                        
                        // clear all error
                        nts.uk.ui.errors.clearAll();
                    }).fail(function(res) {
                          
                    });
                }
            });    
            
            if(self.useConditionCls() == true){
                self.enableGrantDate(true);
            } else {
                self.enableGrantDate(false);
            }
            
            self.useConditionCls.subscribe(function(value) {
                if(!value){
                    self.enableGrantDate(false);
                } else {
                    self.enableGrantDate(true);
                }
            });  
        }

        /**
         * Start page.
         */
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            
            $.when(self.getData()).done(function() {
                                
                if (self.items().length > 0) {
                    self.singleSelectedCode(self.items()[0].code);
                }
                
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);    
            });

            return dfd.promise();
        }
        
        /**
         * Get data from db.
         */
        getData(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.items([]);
            service.findAll().done(function(data) {
                _.forEach(data, function(item) {
                    self.items.push(new ItemModel(item.yearHolidayCode, item.yearHolidayName));
                });
                
                dfd.resolve(data);
            }).fail(function(res) {
                dfd.reject(res);    
            });
            
            return dfd.promise();
        }
        
        getGHTdata(conditionNo: number, yearHolidayCode: string) {
            var self = this;
            
            service.findGrantHolidayTblByCodes(conditionNo, yearHolidayCode).done(function(data) {
                if(data.length > 0) {
                    self.grantHTData = data;
                    
                    _.forEach(self.grantHTData, function(item) {
                        if(item.conditionNo === 1){
                            $('.a9_1').hide();
                        } else {
                            $('.a9_1').show();
                        }
                        
                        if(item.conditionNo === 2){
                            $('.a9_2').hide();
                        } else {
                            $('.a9_2').show();
                        }
                        
                        if(item.conditionNo === 3){
                            $('.a9_3').hide();
                        } else {
                            $('.a9_3').show();
                        }
                        
                        if(item.conditionNo === 4){
                            $('.a9_4').hide();
                        } else {
                            $('.a9_4').show();
                        }
                        
                        if(item.conditionNo === 5){
                            $('.a9_5').hide();
                        } else {
                            $('.a9_5').show();
                        }
                    });
                }
            }).fail(function(res) {
            });
        }
        
        /**
         * Clear data input on form
         */
        cleanForm(){
            var self = this;

            // clear all error
            nts.uk.ui.errors.clearAll();
            
            self.isNewMode(true);
            
            //Top input form
            self.code("");
            self.name("");              
            self.useConditionCls(false);            
            self.grantDate("101"); 
            self.A6_2SelectedRuleCode(0);  
            self.A7_4SelectedRuleCode(0);
            self.symbols("%");
            self.limitedValue01("100");
            self.limitedValue02("");
            self.limitedValue03("");
            self.limitedValue04("");
            self.limitedValue05("");
            
            //Bottom input form
            self.conditionValue01("");
            self.conditionValue02("");
            self.conditionValue03("");
            self.conditionValue04("");
            self.conditionValue05("");
            self.note("");
            self.conditionValue01Enable(false);
            self.conditionValue02Enable(false);
            self.conditionValue03Enable(false);
            self.conditionValue04Enable(false);
            self.conditionValue05Enable(false);
            self.btnSetting02Enable(false);
            self.btnSetting03Enable(false);
            self.btnSetting04Enable(false);
            self.btnSetting05Enable(false); 
            
            self.showLblSet01(false);
            self.showLblSet02(false);
            self.showLblSet03(false);
            self.showLblSet04(false);
            self.showLblSet05(false);
            
            self.useCls01(false);
            self.useCls02(false);
            self.useCls03(false);
            self.useCls04(false);
            self.useCls05(false);
            self.useCls02Enable(false);
            self.useCls03Enable(false);
            self.useCls04Enable(false);
            self.useCls05Enable(false);
            
            //Grid data
            self.singleSelectedCode([]);
            
            self.editMode(true);
            
            self.setFocus();
            $('.a9_6').show();
        }
        
        setFocus() {
            var self = this;
            if (self.editMode()) {
                $("#input-code").focus();
            } else {
                $("#input-name").focus();    
            }   
        }
        
        /**
         * Execute add or update data to db
         */
        addFunction(){
            var self = this;
            
            // clear all error
            nts.uk.ui.errors.clearAll();
            
            // validate
            $(".input-code").trigger("validate");
            $(".input-name").trigger("validate");
            $(".a7_7").trigger("validate");
            
            if(self.name().trim() === "") {
                self.name("");
                $(".input-name").trigger("validate");
            }
            
            if(!self.editMode()) {
                if(self.A7_4SelectedRuleCode() == 0) {
                    if(self.conditionValue01() === "" || Number(self.conditionValue01()) > 100 || Number(self.conditionValue01()) < 0) {
                        $('#cond01').ntsError('set', {messageId:"Msg_262"});
                    } else if(self.useCls02() && (self.conditionValue02() === "" || Number(self.conditionValue02()) > 100 || Number(self.conditionValue02()) < 0)) {
                        $('#cond02').ntsError('set', {messageId:"Msg_262"});
                    } else if(self.useCls03() && (self.conditionValue03() === "" || Number(self.conditionValue03()) > 100 || Number(self.conditionValue03()) < 0)) {
                        $('#cond03').ntsError('set', {messageId:"Msg_262"});
                    } else if(self.useCls04() && (self.conditionValue04() === "" || Number(self.conditionValue04()) > 100 || Number(self.conditionValue04()) < 0)) {
                        $('#cond04').ntsError('set', {messageId:"Msg_262"});
                    } else if(self.useCls05() && (self.conditionValue05() === "" || Number(self.conditionValue05()) > 100 || Number(self.conditionValue05()) < 0)) {
                        $('#cond05').ntsError('set', {messageId:"Msg_262"});
                    }
                } else {
                    if(self.conditionValue01() === "" || Number(self.conditionValue01()) > 366 || Number(self.conditionValue01()) < 0) {
                        $('#cond01').ntsError('set', {messageId:"Msg_263"});
                    } else if(self.useCls02() && (self.conditionValue02() === "" || Number(self.conditionValue02()) > 366 || Number(self.conditionValue02()) < 0)) {
                        $('#cond02').ntsError('set', {messageId:"Msg_263"});
                    } else if(self.useCls03() && (self.conditionValue03() === "" || Number(self.conditionValue03()) > 366 || Number(self.conditionValue03()) < 0)) {
                        $('#cond03').ntsError('set', {messageId:"Msg_263"});
                    } else if(self.useCls04() && (self.conditionValue04() === "" || Number(self.conditionValue04()) > 366 || Number(self.conditionValue04()) < 0)) {
                        $('#cond04').ntsError('set', {messageId:"Msg_263"});
                    } else if(self.useCls05() && (self.conditionValue05() === "" || Number(self.conditionValue05()) > 366 || Number(self.conditionValue05()) < 0)) {
                        $('#cond05').ntsError('set', {messageId:"Msg_263"});
                    }
                }
            }            
            
            if (nts.uk.ui.errors.hasError()) {
                return;    
            }
                
            var code = self.code();
            var name = self.name();
            var calMethod = Number(self.A7_4SelectedRuleCode());
            var standMethod = Number(self.A6_2SelectedRuleCode());
            var useConditionCls = self.useConditionCls() == true ? 1 : 0;
            var grantMd = Number(self.grantDate());
            var memo = self.note();
            var grandConditions = new Array<GrantCondition>();
            
            if(self.conditionValue01() != null || self.editMode()){
                grandConditions.push(new GrantCondition({
                    conditionNo: 1,
                    yearHolidayCode: code,
                    useConditionAtr: 1,
                    conditionValue: self.conditionValue01() !== "" ? Number(self.conditionValue01()) : self.conditionValue01()
                }));
            } else {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return;
            }
            
            if(self.conditionValue02() != null){
                grandConditions.push(new GrantCondition({
                    conditionNo: 2,
                    yearHolidayCode: code,
                    useConditionAtr: self.useCls02() == true ? 1 : 0,
                    conditionValue: self.conditionValue02() !== "" ? Number(self.conditionValue02()) : self.conditionValue02()
                }));
            } else if(self.useCls02() && self.conditionValue02().trim() === "") {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return;
            }
            
            if(self.conditionValue03() != null){
                grandConditions.push(new GrantCondition({
                    conditionNo: 3,
                    yearHolidayCode: code,
                    useConditionAtr: self.useCls03() == true ? 1 : 0,
                    conditionValue: self.conditionValue03() !== "" ? Number(self.conditionValue03()) : self.conditionValue03()
                }));
            } else if(self.useCls03() && self.conditionValue03().trim() === "") {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return;
            }
            
            if(self.conditionValue04() != null){
                grandConditions.push(new GrantCondition({
                    conditionNo: 4,
                    yearHolidayCode: code,
                    useConditionAtr: self.useCls04() == true ? 1 : 0,
                    conditionValue: self.conditionValue04() !== "" ? Number(self.conditionValue04()) : self.conditionValue04()
                }));
            } else if(self.useCls04() && self.conditionValue04().trim() === "") {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return;
            }
            
            if(self.conditionValue05() != null){
                grandConditions.push(new GrantCondition({
                    conditionNo: 5,
                    yearHolidayCode: code,
                    useConditionAtr: self.useCls05() == true ? 1 : 0,
                    conditionValue: self.conditionValue05() !== "" ? Number(self.conditionValue05()) : self.conditionValue05()
                }));
            } else if(self.useCls05() && self.conditionValue05().trim() === "") {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return;
            }
            
            var data = new YearHolidayGrantDto(code, name, calMethod, standMethod, useConditionCls, grantMd, memo, grandConditions);
            
            if(!self.editMode()){
                let check = [];
                let flag = true;
                
                _.forEach(data.grantConditions, function(item) {
                    if(isNaN(item.conditionValue)) {
                        flag = false;
                        
                        if(self.A7_4SelectedRuleCode() == 0) {
                            if(item.conditionNo == 2) {
                                $('#cond02').ntsError('set', {messageId:"Msg_262"});
                                return;
                            } else if(item.conditionNo == 3) {
                                $('#cond03').ntsError('set', {messageId:"Msg_262"});
                                return;
                            } else if(item.conditionNo == 4) {
                                $('#cond04').ntsError('set', {messageId:"Msg_262"});
                                return;
                            } else if(item.conditionNo == 5) {
                                $('#cond05').ntsError('set', {messageId:"Msg_262"});
                                return;
                            }
                        } else {
                            if(item.conditionNo == 2) {
                                $('#cond02').ntsError('set', {messageId:"Msg_263"});
                                return;
                            } else if(item.conditionNo == 3) {
                                $('#cond03').ntsError('set', {messageId:"Msg_263"});
                                return;
                            } else if(item.conditionNo == 4) {
                                $('#cond04').ntsError('set', {messageId:"Msg_263"});
                                return;
                            } else if(item.conditionNo == 5) {
                                $('#cond05').ntsError('set', {messageId:"Msg_263"});
                                return;
                            }
                        }
                    }
                });
                
                _.forEach(data.grantConditions, function(item) {
                    if(item.useConditionAtr == 1) {
                        check.push(item);
                    }
                });
                
                if(check.length > 1) {
                    _.forEach(check, function(item, index) {
                        var value = check[index + 1] != null ? check[index + 1].conditionValue : 0;
                        
                        if(flag && item.conditionValue <= value) {
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_264" });
                            flag = false;
                            return;
                        }
                    });
                    
                    if(flag) {
                        self.updateMode(data);
                    }
                } else {
                    if(flag) {
                        self.updateMode(data);
                    }
                }
            } else {
                self.addMode(data);
            }
        }
        
        //Add data to db
        addMode(data: YearHolidayGrantDto){
            var self = this;
            
            if (nts.uk.ui.errors.hasError()) {
                return;    
            }
            
            service.addYearHolidayGrant(data).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                self.getData();
                self.singleSelectedCode(data.yearHolidayCode);
            }).fail(function(error) {
                $('.input-code').ntsError('set', error);
            }).always(function() {
                nts.uk.ui.block.clear();      
            });
        }
        
        //Update data to db
        updateMode(data: YearHolidayGrantDto){
            var self = this;
            
            service.updateYearHolidayGrant(data).done(function() {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                self.getData();
                self.singleSelectedCode(data.yearHolidayCode);
            }).fail(function(error) {
                nts.uk.ui.dialog.alertError(error.message);
            }).always(function() {
                nts.uk.ui.block.clear();      
            });
        }
        
        /**
         * Delete data by code
         */
        deleteFunction(){
            var self = this;
            
            let count = 0;
            for (let i = 0; i <= self.items().length; i++){
                if(self.items()[i].code == self.singleSelectedCode()){
                    count = i;
                    break;
                }
            }
            
            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                service.deleteYearHolidayGrant(self.singleSelectedCode()).done(function() {
                    self.getData().done(function(){
                        // if number of item from list after delete == 0 
                        if(self.items().length==0){
                            self.cleanForm();
                            return;
                        }
                        // delete the last item
                        if(count == ((self.items().length))){
                            self.singleSelectedCode(self.items()[count-1].code);
                            return;
                        }
                        // delete the first item
                        if(count == 0 ){
                            self.singleSelectedCode(self.items()[0].code);
                            return;
                        }
                        // delete item at mediate list 
                        else if(count > 0 && count < self.items().length){
                            self.singleSelectedCode(self.items()[count].code);    
                            return;
                        }
                    });
                    
                    nts.uk.ui.dialog.info({ messageId: "Msg_16" });
                }).fail(function(error) {
                    nts.uk.ui.dialog.alertError(error.message);
                }).always(function() {
                    nts.uk.ui.block.clear();      
                });
            });
        }
        
        /**
         * Open screen B in dialog
         */
        openBDialog(conditionNo: number) {
            var self = this;
            
            var optionValue = '';
            var conditionValue = '';
            var afterValue = '';
            
            //Call validate
            var validateFlag = self.validateToOpenDialogB(conditionNo);
            if(!validateFlag){
                return false;    
            }
            
            if (nts.uk.ui.errors.hasError()) {
                return;    
            }
            
            if(conditionNo === 1){
                conditionValue = self.conditionValue01();
                afterValue = $('.a7_8').text();
            } else if(conditionNo === 2){
                conditionValue = self.conditionValue02();
                afterValue = $('.a7_12').text();
            } else if(conditionNo === 3){
                conditionValue = self.conditionValue03();
                afterValue = $('.a7_16').text();
            } else if(conditionNo === 4){
                conditionValue = self.conditionValue04();
                afterValue = $('.a7_20').text();
            } else if(conditionNo === 5){
                conditionValue = self.conditionValue05();
                afterValue = $('.a7_24').text();
            }
            
            if(Number(self.A7_4SelectedRuleCode()) === 0) {
                optionValue = nts.uk.resource.getText("KMF003_21");
            } else {
                optionValue = nts.uk.resource.getText("KMF003_22");
            }
            
            var data = {
                code: self.code(),
                name: self.name(),
                conditionValue: {
                    option: optionValue,
                    value: conditionValue,
                    afterValue: afterValue
                },
                useCondition: self.useConditionCls(),
                dateSelected: self.useConditionCls() ? self.grantDate() : "",
                conditionNo: conditionNo
            };
            
            nts.uk.ui.windows.setShared("KMF003_CONDITION_NO", data);
            
            if(conditionNo === 1) {
                nts.uk.ui.windows.sub.modal("/view/kmf/003/b/index.xhtml").onClosed(() => {
                    var dataIsNotNull = nts.uk.ui.windows.getShared("KMF003_HAVE_DATA");
                    
                    if(dataIsNotNull) {
                        if(conditionNo === 1){
                            self.showLblSet01(true);
                        } else if(conditionNo === 2){
                            self.showLblSet02(true);
                        } else if(conditionNo === 3){
                            self.showLblSet03(true);
                        } else if(conditionNo === 4){
                            self.showLblSet04(true);
                        } else if(conditionNo === 5){
                            self.showLblSet05(true);
                        }
                    } else {
                        if(conditionNo === 1){
                            self.showLblSet01(false);
                        } else if(conditionNo === 2){
                            self.showLblSet02(false);
                        } else if(conditionNo === 3){
                            self.showLblSet03(false);
                        } else if(conditionNo === 4){
                            self.showLblSet04(false);
                        } else if(conditionNo === 5){
                            self.showLblSet05(false);
                        }
                    }
                    
                    nts.uk.ui.windows.setShared("KMF003_CANCEL_DATA", true);
                });   
            } else {
                nts.uk.ui.windows.sub.modal("/view/kmf/003/b1/index.xhtml").onClosed(() => {
                    var dataIsNotNull = nts.uk.ui.windows.getShared("KMF003_HAVE_DATA");
                    
                    if(dataIsNotNull) {
                        if(conditionNo === 1){
                            self.showLblSet01(true);
                        } else if(conditionNo === 2){
                            self.showLblSet02(true);
                        } else if(conditionNo === 3){
                            self.showLblSet03(true);
                        } else if(conditionNo === 4){
                            self.showLblSet04(true);
                        } else if(conditionNo === 5){
                            self.showLblSet05(true);
                        }
                    } else {
                        if(conditionNo === 1){
                            self.showLblSet01(false);
                        } else if(conditionNo === 2){
                            self.showLblSet02(false);
                        } else if(conditionNo === 3){
                            self.showLblSet03(false);
                        } else if(conditionNo === 4){
                            self.showLblSet04(false);
                        } else if(conditionNo === 5){
                            self.showLblSet05(false);
                        }
                    }
                    
                    nts.uk.ui.windows.setShared("KMF003_CANCEL_DATA", true);
                });   
            }
        }
        
        /**
         * Validate before open dialog B.
         */
        validateToOpenDialogB(conditionNo: number){
            var self = this;
            
            if(self.useConditionCls() && self.grantDate() === ""){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_261" });
                return false;
            }
            
            if(conditionNo === 1 && (self.conditionValue01() === "" || self.conditionValue01() === undefined)){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return false;
            } else if(conditionNo === 2 && (self.conditionValue02() === "" || self.conditionValue02() === undefined)){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return false;
            } else if(conditionNo === 3 && (self.conditionValue03() === "" || self.conditionValue03() === undefined)){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return false;
            } else if(conditionNo === 4 && (self.conditionValue04() === "" || self.conditionValue04() === undefined)){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return false;
            } else if(conditionNo === 5 && (self.conditionValue05() === "" || self.conditionValue05() === undefined)){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_271" });
                return false;
            }
            
            return true;
        }
        
        /**
         * Form controls display.
         */
        controlsDisplay() {
            var self = this;
            
            //Top input form
            self.code = ko.observable("");
            self.editMode = ko.observable(true);  
            self.name = ko.observable("");              
            self.useConditionCls = ko.observable(false);            
            self.grantDate = ko.observable("");   
            self.enableGrantDate = ko.observable(true);           
            self.A6_2Data = ko.observableArray([
                { code: '0', name: nts.uk.resource.getText("KMF003_17") },
                { code: '1', name: nts.uk.resource.getText("KMF003_18") }
            ]);
            self.A6_2SelectedRuleCode = ko.observable(0);            
            self.A7_4Data = ko.observableArray([
                { code: '0', name: nts.uk.resource.getText("KMF003_21") },
                { code: '1', name: nts.uk.resource.getText("KMF003_22") }
            ]);
            self.A7_4SelectedRuleCode = ko.observable(0);
            self.symbols = ko.observable("%");
            self.limitedValue01 = ko.observable("100");
            self.limitedValue02 = ko.observable("");
            self.limitedValue03 = ko.observable("");
            self.limitedValue04 = ko.observable("");
            self.limitedValue05 = ko.observable("");
            
            //Bottom input form
            self.useCls01 = ko.observable(false);
            self.useCls02 = ko.observable(false);
            self.useCls03 = ko.observable(false);
            self.useCls04 = ko.observable(false);
            self.useCls05 = ko.observable(false);         
            self.conditionValue01 = ko.observable("");
            self.conditionValue02 = ko.observable("");
            self.conditionValue03 = ko.observable("");
            self.conditionValue04 = ko.observable("");
            self.conditionValue05 = ko.observable("");
            self.note = ko.observable("");
            self.conditionValue01Enable = ko.observable(false);
            self.conditionValue02Enable = ko.observable(false);
            self.conditionValue03Enable = ko.observable(false);
            self.conditionValue04Enable = ko.observable(false);
            self.conditionValue05Enable = ko.observable(false);
            self.btnSetting02Enable = ko.observable(false);
            self.btnSetting03Enable = ko.observable(false);
            self.btnSetting04Enable = ko.observable(false);
            self.btnSetting05Enable = ko.observable(false);
             
            self.useCls02Enable = ko.observable(true);
            self.useCls03Enable = ko.observable(true);
            self.useCls04Enable = ko.observable(true);
            self.useCls05Enable = ko.observable(true);
        }
        
        /**
         * Enable or disable for setting form.
         */
        conditionSettingForm() {
            var self = this;
            var count = 0;
            
            self.A7_4SelectedRuleCode.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                if(value == 0){
                    self.symbols("%");
                    self.limitedValue01("100");
                    self.setConditionValueChanges();
                    
                    if((Number(self.conditionValue01()) > 100 || Number(self.conditionValue01()) < 0) && !self.isNewMode()) {
                        $('#cond01').ntsError('set', {messageId:"Msg_262"});
                    }
                } else if(value == 1) {
                    self.symbols("日");
                    self.limitedValue01("366");
                    self.setConditionValueChanges();
                    
                    if((Number(self.conditionValue01()) > 366 || Number(self.conditionValue01()) < 0) && !self.isNewMode()) {
                        $('#cond01').ntsError('set', {messageId:"Msg_263"});
                    }
                }
                
                self.isNewMode(false);
            });
            
            self.conditionValue01.subscribe(function(value) {
                var result = 0;
                
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                if(value === "") {
                    return false;
                }
                
                if(self.A7_4SelectedRuleCode() == 0 && (Number(value) > 100 || Number(self.conditionValue01()) < 0)){
                    $('#cond01').ntsError('set', {messageId:"Msg_262"});
                } else if(self.A7_4SelectedRuleCode() == 1 && (Number(value) > 366 || Number(self.conditionValue01()) < 0)){
                    $('#cond01').ntsError('set', {messageId:"Msg_263"});
                } else {
                    if(self.useCls02()) {
                        if(self.conditionValue01() == undefined || self.conditionValue01() == "") {
                            result = 0;
                        } else {
                            result = Number(value) - 1;
                        }
                        
                        self.limitedValue02(result < 0 ? "" : result.toString());
                    } else {
                        if(self.useCls03()) {
                            if(self.conditionValue01() == undefined || self.conditionValue01() == "") {
                                result = 0;
                            } else {
                                result = Number(value) - 1;
                            }
                            self.limitedValue03(result < 0 ? "" : result.toString());
                        } else {
                            if(self.useCls04()) {
                                if(self.conditionValue01() == undefined || self.conditionValue01() == "") {
                                    result = 0;
                                } else {
                                    result = Number(value) - 1;
                                }
                                self.limitedValue04(result < 0 ? "" : result.toString());
                            } else {
                                if(self.useCls05()) {
                                    if(self.conditionValue01() == undefined || self.conditionValue01() == "") {
                                        result = 0;
                                    } else {
                                        result = Number(value) - 1;
                                    }
                                    self.limitedValue05(result < 0 ? "" : result.toString());
                                }
                            }
                        }
                    }
                }
            });
            
            self.conditionValue02.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                var result = Number(value) - 1;
                
                if(self.A7_4SelectedRuleCode() == 0 && (Number(value) > (Number(self.conditionValue01()) - 1) || Number(self.conditionValue02()) < 0)){
                    $('#cond02').ntsError('set', {messageId:"Msg_262"});
                } else if(self.A7_4SelectedRuleCode() == 1 && (Number(value) > (Number(self.conditionValue01()) - 1) || Number(self.conditionValue02()) < 0)){
                    $('#cond02').ntsError('set', {messageId:"Msg_263"});
                } else {
                    if(self.useCls03()) {
                        if(self.conditionValue02() == undefined || self.conditionValue02() == "") {
                            result = 0;
                        } else {
                            result = Number(value) - 1;
                        }
                        self.limitedValue03(result < 0 ? "" : result.toString());
                    } else {
                        if(self.useCls04()) {
                            if(self.conditionValue02() == undefined || self.conditionValue02() == "") {
                                result = 0;
                            } else {
                                result = Number(value) - 1;
                            }
                            self.limitedValue04(result < 0 ? "" : result.toString());
                        } else {
                            if(self.useCls05()) {
                                if(self.conditionValue02() == undefined || self.conditionValue02() == "") {
                                    result = 0;
                                } else {
                                    result = Number(value) - 1;
                                }
                                self.limitedValue05(result < 0 ? "" : result.toString());
                            }
                        }
                    }
                }
            });
            
            self.conditionValue03.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                var result = Number(value) - 1;
                
                if(self.A7_4SelectedRuleCode() == 0){
                    if((Number(value) > (Number(self.limitedValue03())) || Number(self.conditionValue03()) < 0)) {
                        $('#cond03').ntsError('set', {messageId:"Msg_262"});
                    } else {
                        if(self.useCls04()) {
                            if(self.conditionValue03() == undefined || self.conditionValue03() == "") {
                                result = 0;
                            } else {
                                result = Number(value) - 1;
                            }
                            
                            self.limitedValue04(result < 0 ? "" : result.toString());
                        } else {
                            if(self.useCls05()) {
                                if(self.conditionValue03() == undefined || self.conditionValue03() == "") {
                                    result = 0;
                                } else {
                                    result = Number(value) - 1;
                                }
                                
                                self.limitedValue05(result < 0 ? "" : result.toString());
                            }
                        }
                    }                  
                } else if(self.A7_4SelectedRuleCode() == 1){
                    if((Number(value) > (Number(self.limitedValue03())) || Number(self.conditionValue03()) < 0)) {
                        $('#cond03').ntsError('set', {messageId:"Msg_263"});
                    } else {
                        if(self.useCls04()) {
                            if(self.conditionValue03() == undefined || self.conditionValue03() == "") {
                                result = 0;
                            } else {
                                result = Number(value) - 1;
                            }
                            
                            self.limitedValue04(result < 0 ? "" : result.toString());
                        } else {
                            if(self.useCls05()) {
                                if(self.conditionValue03() == undefined || self.conditionValue03() == "") {
                                    result = 0;
                                } else {
                                    result = Number(value) - 1;
                                }
                                
                                self.limitedValue05(result < 0 ? "" : result.toString());
                            }
                        }
                    }   
                }
            });
            
            self.conditionValue04.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                var result = Number(value) - 1;
                
                if(self.A7_4SelectedRuleCode() == 0){
                    if((Number(value) > (Number(self.limitedValue04())) || Number(self.conditionValue04()) < 0)) {
                        $('#cond04').ntsError('set', {messageId:"Msg_262"});
                    } else {
                        if(self.useCls05()) {
                            if(self.conditionValue04() == undefined || self.conditionValue04() == "") {
                                result = 0;
                            } else {
                                result = Number(value) - 1;
                            }
                            
                            self.limitedValue05(result < 0 ? "" : result.toString());
                        }
                    } 
                } else if(self.A7_4SelectedRuleCode() == 1){
                    if((Number(value) > (Number(self.limitedValue04())) || Number(self.conditionValue04()) < 0)) {
                        $('#cond04').ntsError('set', {messageId:"Msg_263"});
                    } else {
                        if(self.useCls05()) {
                            if(self.conditionValue04() == undefined || self.conditionValue04() == "") {
                                result = 0;
                            } else {
                                result = Number(value) - 1;
                            }
                            
                            self.limitedValue05(result < 0 ? "" : result.toString());
                        }
                    } 
                } else {
                    if(self.useCls05()) {
                        if(self.conditionValue04() == undefined || self.conditionValue04() == "") {
                            result = 0;
                        } else {
                            result = Number(value) - 1;
                        }
                        
                        self.limitedValue05(result < 0 ? "" : result.toString());
                    }
                }
            });
            
            self.conditionValue05.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                var result = 0;
                
                if(self.conditionValue05() == undefined || self.conditionValue05() == "") {
                    result = 0;
                } else {
                    result = Number(value) - 1;
                }
                
                if(self.A7_4SelectedRuleCode() == 0){
                    if((Number(value) > (Number(self.limitedValue05())) || Number(self.conditionValue05()) < 0)) {
                        $('#cond05').ntsError('set', {messageId:"Msg_262"});
                    }
                } else if(self.A7_4SelectedRuleCode() == 1){
                    if((Number(value) > (Number(self.limitedValue05())) || Number(self.conditionValue05()) < 0)) {
                        $('#cond05').ntsError('set', {messageId:"Msg_263"});
                    }
                }
            });
            
            self.useCls02.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                if(value == true){
                    self.conditionValue02Enable(true);
                    self.btnSetting02Enable(!self.editMode());
                    self.setConditionValues(Number(self.conditionValue01()), 2);
                    
                    if(self.useCls03()) {
                        self.setConditionValues(Number(self.conditionValue02()), 3);
                        
                        if(self.useCls04()) {
                            self.setConditionValues(Number(self.conditionValue03()), 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5);
                            }
                        } else {
                            self.setConditionValues(0, 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue03()), 5);
                            }
                        }
                    } else {
                        self.setConditionValues(0, 3);
                        
                        if(self.useCls04()) {
                            self.setConditionValues(Number(self.conditionValue02()), 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5);
                            }
                        } else {
                            self.setConditionValues(0, 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue02()), 5);
                            }
                        }
                    }
                } else {
                    self.conditionValue02Enable(false);
                    self.btnSetting02Enable(false);
                    self.limitedValue02("");
                    
                    if(self.useCls03()) {
                        self.setConditionValues(Number(self.conditionValue01()), 3);
                    } else if(self.useCls04()) {
                        self.setConditionValues(Number(self.conditionValue01()), 4);
                    } else if(self.useCls05()) {
                        self.setConditionValues(Number(self.conditionValue01()), 5);
                    }  
                }
            });
            
            self.useCls03.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                if(value == true){
                    self.conditionValue03Enable(true);
                    self.btnSetting03Enable(true);
                    
                    if(self.useCls02()) {
                        self.setConditionValues(Number(self.conditionValue02()), 3); 
                        
                        if(self.useCls04()) {
                            self.setConditionValues(Number(self.conditionValue03()), 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5);
                            }
                        } else {
                            self.setConditionValues(0, 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue03()), 5);
                            }
                        }
                    } else {
                        self.setConditionValues(Number(self.conditionValue01()), 3); 
                        
                        if(self.useCls04()) {
                            self.setConditionValues(Number(self.conditionValue03()), 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5);
                            }
                        } else {
                            self.setConditionValues(0, 4);
                            
                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue03()), 5);
                            }
                        }
                    }
                } else {
                    self.conditionValue03Enable(false);
                    self.btnSetting03Enable(false);
                    self.limitedValue03("");
                    
                    if(self.useCls02()) {
                        if(self.useCls04()) {
                            self.setConditionValues(Number(self.conditionValue02()), 4);
                        } else {
                            self.setConditionValues(0, 4);
                            
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue02()), 5);
                            }
                        }
                    } else {
                        if(self.useCls04()) {
                            self.setConditionValues(Number(self.conditionValue01()), 4);
                        } else {
                            self.setConditionValues(0, 4);

                            if (self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue01()), 5);
                            }
                        }
                    }
                }
            });
            
            self.useCls04.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                if(value == true){
                    self.conditionValue04Enable(true);
                    self.btnSetting04Enable(true);
                    
                    if(self.useCls02()) {
                        if(self.useCls03()) {
                            self.setConditionValues(Number(self.conditionValue03()), 4); 
                            
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5); 
                            }
                        } else {
                            self.setConditionValues(Number(self.conditionValue02()), 4); 
                            
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5); 
                            }
                        }
                    } else {
                        if(self.useCls03()) {
                            self.setConditionValues(Number(self.conditionValue03()), 4); 
                            
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5); 
                            }
                        } else {
                            self.setConditionValues(Number(self.conditionValue01()), 4); 
                            
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue04()), 5); 
                            }
                        }
                    }
                } else {
                    self.conditionValue04Enable(false);
                    self.btnSetting04Enable(false);
                    self.limitedValue04("");
                    
                    if(self.useCls02()) {
                        if(self.useCls03()) {
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue03()), 5); 
                            }
                        } else {
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue02()), 5); 
                            }
                        }
                    } else {
                        if(self.useCls03()) {
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue03()), 5); 
                            }
                        } else {
                            if(self.useCls05()) {
                                self.setConditionValues(Number(self.conditionValue01()), 5); 
                            }
                        }
                    }
                }
            });
            
            self.useCls05.subscribe(function(value) {
                // clear all error
                nts.uk.ui.errors.clearAll();
                
                if(value == true){
                    self.conditionValue05Enable(true);
                    self.btnSetting05Enable(true);
                    
                    if(self.useCls04()) {
                        self.setConditionValues(Number(self.conditionValue04()), 5);
                    } else if(self.useCls03()) {
                        self.setConditionValues(Number(self.conditionValue03()), 5); 
                    } else if(self.useCls02()) {
                        self.setConditionValues(Number(self.conditionValue02()), 5); 
                    } else {
                        self.setConditionValues(Number(self.conditionValue01()), 5); 
                    }
                } else {
                    self.conditionValue05Enable(false);
                    self.btnSetting05Enable(false);
                    self.limitedValue05("");
                    self.setConditionValues(0, 5);
                }
            });
        }
        
        /**
         * Set condition values.
         */
        setConditionValues(value: number, position: number) {
            var self = this;
            var result = value - 1;
            
            if(position == 2) {
                self.limitedValue02(result < 0 ? "" : (result.toString() === "NaN" ? "" : result.toString()));
            } else if (position == 3) {
                self.limitedValue03(result < 0 ? "" : (result.toString() === "NaN" ? "" : result.toString()));
            } else if (position == 4) {
                self.limitedValue04(result < 0 ? "" : (result.toString() === "NaN" ? "" : result.toString()));
            } else if (position == 5) {
                self.limitedValue05(result < 0 ? "" : (result.toString() === "NaN" ? "" : result.toString()));
            }
        }
        
        /**
         * Set condition values when change condition type.
         */
        setConditionValueChanges() {
            var self = this;
            var result02 = Number(self.conditionValue01()) - 1;
            var result03 = Number(self.conditionValue02()) - 1;
            var result04 = Number(self.conditionValue03()) - 1;
            var result05 = Number(self.conditionValue04()) - 1;
            
            self.limitedValue02(self.useCls02() ? (result02 <= 0 ? "" : result02.toString()) : "");
            self.limitedValue03(self.useCls03() ? (result03 <= 0 ? "" : result03.toString()) : "");
            self.limitedValue04(self.useCls04() ? (result04 <= 0 ? "" : result04.toString()) : "");
            self.limitedValue05(self.useCls05() ? (result05 <= 0 ? "" : result05.toString()) : "");
        }
    }
    
    class ItemModel {
        code: string;
        name: string;
        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;       
        }
    } 
    
    class YearHolidayGrantDto {
        yearHolidayCode: string;
        yearHolidayName: string;
        calculationMethod: number;
        standardCalculation: number;
        useSimultaneousGrant: number;
        simultaneousGrandMonthDays: number;
        yearHolidayNote: string;
        grantConditions: Array<GrantCondition>;  
        constructor(yearHolidayCode: string, yearHolidayName: string, calculationMethod: number, standardCalculation: number, useSimultaneousGrant: number, 
                simultaneousGrandMonthDays: number, yearHolidayNote: string, grantConditions: Array<GrantCondition>) {
            this.yearHolidayCode = yearHolidayCode;
            this.yearHolidayName = yearHolidayName;     
            this.calculationMethod = calculationMethod;
            this.standardCalculation = standardCalculation; 
            this.useSimultaneousGrant = useSimultaneousGrant;
            this.simultaneousGrandMonthDays = simultaneousGrandMonthDays; 
            this.yearHolidayNote = yearHolidayNote;
            this.grantConditions = grantConditions;   
        }
    }
    
    class GrantCondition {
        yearHolidayCode: string;
        conditionNo: number;
        conditionValue: any;
        useConditionAtr: number; 
        constructor(param: IGrantCondition) {
            this.yearHolidayCode = param.yearHolidayCode;
            this.conditionNo = param.conditionNo;
            this.conditionValue = param.conditionValue;
            this.useConditionAtr = param.useConditionAtr;       
        }
    }
    
    interface IGrantCondition {
        yearHolidayCode: string;
        conditionNo: number;
        conditionValue: any;
        useConditionAtr: number;     
    }
}
