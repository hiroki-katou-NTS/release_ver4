module nts.uk.at.view.kaf002.m1 {
    import service = nts.uk.at.view.kaf002.shr.service;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import setShared = nts.uk.ui.windows.setShared;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel {
        export class ScreenModel {
            stampAtr: KnockoutObservable<number> = ko.observable(1);
            extendsMode: KnockoutObservable<boolean> = ko.observable(false);
            extendsModeDisplay: KnockoutObservable<boolean> = ko.observable(true);
            appStampList: KnockoutObservableArray<vmbase.AppStampGoOutPermit> = ko.observableArray([]);
            supFrameNo: number = 10;
            stampPlaceDisplay: KnockoutObservable<number> = ko.observable(0);
            stampAtrList: KnockoutObservableArray<vmbase.SimpleObject> = ko.observableArray([]);
            stampGoOutAtrList: KnockoutObservableArray<any> = ko.observableArray([]);
            workLocationList: Array<vmbase.IWorkLocation> = [];
            displayAllLabel: string = nts.uk.resource.getText("KAF002_13", [nts.uk.resource.getText('Com_Out')]);
            displayItemNo: number = this.supFrameNo;
            editable: KnockoutObservable<boolean> = ko.observable(true);
            screenMode: KnockoutObservable<number> = ko.observable(0);
            attendanceItems: Array<any> = [];
            employeeID: string;
            
            start(appStampData: any, data: vmbase.AppStampNewSetDto, listWorkLocation: Array<vmbase.IWorkLocation>, 
                editable: any, screenMode: any, employeeID: string, appDate: any){
                var self = this;    
                if(!nts.uk.util.isNullOrUndefined(appStampData)){
                    let stampLoop = _.find(appStampData, (o) => { return o.stampFrameNo > 2; });
                    if(!nts.uk.util.isNullOrUndefined(stampLoop)){
                        self.extendsModeDisplay(false);    
                    }        
                }
                let stampRequestSetDto = data.appStampSetDto.stampRequestSettingDto;
                self.employeeID = employeeID;
                self.screenMode(screenMode);
                self.editable(editable);
                self.workLocationList = listWorkLocation;
                // self.supFrameNo = stampRequestSetDto.supFrameDispNO;
                self.stampPlaceDisplay(stampRequestSetDto.stampPlaceDisp);
                if(stampRequestSetDto.stampAtr_GoOut_Disp==1) self.stampAtrList.push({ code: 1, name: nts.uk.resource.getText('KAF002_31') });
                if(stampRequestSetDto.stampAtr_Care_Disp==1) self.stampAtrList.push({ code: 2, name: nts.uk.resource.getText('KAF002_32') });
                if(stampRequestSetDto.stampAtr_Sup_Disp==1) self.stampAtrList.push({ code: 3, name: nts.uk.resource.getText('KAF002_33') });
                self.stampAtr(_.first(self.stampAtrList()).code);
                if(stampRequestSetDto.stampGoOutAtr_Private_Disp==1) self.stampGoOutAtrList.push({ code: 0, name: nts.uk.resource.getText('KAF002_40') });
                if(stampRequestSetDto.stampGoOutAtr_Public_Disp==1) self.stampGoOutAtrList.push({ code: 1, name: nts.uk.resource.getText('KAF002_41') });
                if(stampRequestSetDto.stampGoOutAtr_Compensation_Disp==1) self.stampGoOutAtrList.push({ code: 2, name: nts.uk.resource.getText('KAF002_42') });
                if(stampRequestSetDto.stampGoOutAtr_Union_Disp==1) self.stampGoOutAtrList.push({ code: 3, name: nts.uk.resource.getText('KAF002_43') });
                self.getAttendanceItem(appDate, [self.employeeID]).done(()=>{
                    if(!nts.uk.util.isNullOrUndefined(appStampData)){
                        self.stampAtr(appStampData[0].stampAtr);
                        _.forEach(appStampData, item => {
                            let stampLoop = _.find(self.appStampList(), (o) => { return o.stampFrameNo() == item.stampFrameNo; });
                            if(nts.uk.util.isNullOrUndefined(stampLoop)){
                                return;    
                            }
                            self.appStampList.remove(stampLoop);
                            let stampInit = new vmbase.AppStampGoOutPermit(
                                    item.stampAtr,
                                    item.stampFrameNo,
                                    item.stampGoOutReason,
                                    new vmbase.CheckBoxTime(item.startTime,true,false),
                                    new vmbase.CheckBoxLocation(item.startLocation,self.findWorkLocationName(item.startLocation),true,false),
                                    new vmbase.CheckBoxTime(item.endTime,true,false),
                                    new vmbase.CheckBoxLocation(item.endLocation,self.findWorkLocationName(item.endLocation),true,false) 
                            );      
                            self.appStampList.push(stampInit);
                        });
                        self.appStampList(_.sortBy(self.appStampList(), (o) => { return o.stampFrameNo(); }));
                    }
                    nts.uk.ui.block.clear();      
                }).fail(()=>{
                    nts.uk.ui.block.clear();    
                }); 
                
                self.stampAtr.subscribe((value)=>{ 
                    nts.uk.ui.errors.clearAll();
                    if(value == 1){
                        self.displayItemNo = 10;   
                        self.extendsModeDisplay(!self.extendsMode() && (self.stampAtr() == 1));      
                    } else {
                        self.displayItemNo = 2;
                        self.extendsModeDisplay(!self.extendsMode() && (self.stampAtr() == 1)); 
                    }
                    self.refreshData();
                });
            }
            
            extendsModeEvent(){
                var self = this;
                self.extendsMode(!self.extendsMode());    
                self.extendsModeDisplay(!self.extendsMode() && (self.stampAtr() == 1)); 
            }
            
            refreshData(){
                var self = this;
                let stampGoOutAtr = _.first(self.stampGoOutAtrList()).code;
                if(self.stampAtr()==1){
                    self.displayItemNo = 10;
                    self.setGoOutItem(self.attendanceItems);      
                } else if(self.stampAtr()==2){
                    self.displayItemNo = 2;     
                    self.setChildCareItem(self.attendanceItems);   
                } else {
                    self.displayItemNo = 2;
                    self.setCareItem(self.attendanceItems);       
                } 
            }
            
            findWorkLocationName(workLocationCD: string): string {
                var self = this;
                let workLocationObject: any = _.find(self.workLocationList, item => { return item.workLocationCD == workLocationCD });
                if(nts.uk.util.isNullOrUndefined(workLocationObject)){
                    return "";        
                } else {
                    return workLocationObject.workLocationName;
                } 
            }
            
            register(application : vmbase.Application, checkBoxValue: boolean){
                var self = this;
                self.validateInput(self.appStampList);
                if(nts.uk.ui.errors.hasError()){
                    return;    
                }
                let command = {
                    appID: "",
                    inputDate: application.inputDate(),
                    enteredPerson: application.enteredPerson(),
                    applicationDate: application.appDate(),
                    titleReason: application.titleReason(), 
                    detailReason: application.contentReason(),
                    employeeID: application.employeeID(),
                    stampRequestMode: 0,
                    appStampGoOutPermitCmds: _.map(self.filterAppStamp(self.appStampList()), item => self.convertToJS(item)),
                    appStampWorkCmds: null,
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: null,
                    checkOver1Year: true
                }
                if(nts.uk.util.isNullOrEmpty(command.appStampGoOutPermitCmds)){
                    $('.m1-time-editor').first().ntsError('set', {messageId:"Msg_308"});        
                } else {
                    nts.uk.ui.block.invisible();
                    service.insert(command).done((data) => {
                        self.insertDone(data, checkBoxValue);
                    }).fail(function(res) {
                        if (res.messageId == "Msg_1518") {//confirm
                            nts.uk.ui.dialog.confirm({ messageId: res.messageId }).ifYes(() => {
                                command.checkOver1Year = false;
                                service.insert(command).done((data) => {
                                    self.insertDone(data, checkBoxValue);
                                }).fail((res) => {
                                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                                        .then(function() { nts.uk.ui.block.clear(); });
                                });
                            }).ifNo(() => {
                                nts.uk.ui.block.clear();
                            });

                        } else {
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { nts.uk.ui.block.clear(); });
                        }
                    });  
                }
            }
            
            insertDone(data, checkBoxValue) {
                nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                    if (data.autoSendMail) {
                        appcommon.CommonProcess.displayMailResult(data);
                    } else {
                        if (checkBoxValue) {
                            appcommon.CommonProcess.openDialogKDL030(data.appID);
                        } else {
                            location.reload();
                        }
                    }
                });
            }
            
            update(application : vmbase.Application){
                var self = this;
                self.validateInput(self.appStampList);
                if(nts.uk.ui.errors.hasError()){
                    return;    
                }
                let command = {
                    version: application.version,
                    appID: application.applicationID(),
                    inputDate: application.inputDate(),
                    enteredPerson: application.enteredPerson(),
                    applicationDate: application.appDate(),
                    titleReason: application.titleReason(), 
                    detailReason: application.contentReason(),
                    employeeID: application.employeeID(),
                    stampRequestMode: 0,
                    appStampGoOutPermitCmds: _.map(self.filterAppStamp(self.appStampList()), item => self.convertToJS(item)),
                    appStampWorkCmds: null,
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: null 
                }
                if(!nts.uk.util.isNullOrEmpty(command.appStampGoOutPermitCmds)){
                    nts.uk.ui.block.invisible();
                    service.update(command)
                    .done((data) => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                            if(data.autoSendMail){
                                appcommon.CommonProcess.displayMailResult(data);    
                            } else {
                                location.reload();
                            }
                        });     
                    })
                    .fail(function(res) { 
                        if(res.optimisticLock == true){
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function(){
                                location.reload();
                            });    
                        } else {
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();}); 
                        }
                    });  
                }
            }
            
            filterAppStamp(appStamp: KnockoutObservableArray<vmbase.AppStampGoOutPermit>){
                var self = this;
                if(self.stampAtr()==1){
                    if(self.stampPlaceDisplay()==1){
                        return _.filter(appStamp, item => {
                            return  (item.startTime().checked() && !nts.uk.util.isNullOrEmpty(item.startTime().value())) ||
                                    (item.startLocation().checked() && !nts.uk.util.isNullOrEmpty(item.startLocation().code())) ||
                                    (item.endTime().checked() && !nts.uk.util.isNullOrEmpty(item.endTime().value()))        
                        });     
                    } else {
                        return _.filter(appStamp, item => {
                            return  (item.startTime().checked() && !nts.uk.util.isNullOrEmpty(item.startTime().value())) ||
                                    (item.endTime().checked() && !nts.uk.util.isNullOrEmpty(item.endTime().value()))        
                        });       
                    }
                } else {
                    return _.filter(appStamp, item => {
                        return  (item.startTime().checked() && !nts.uk.util.isNullOrEmpty(item.startTime().value())) ||
                                (item.endTime().checked() && !nts.uk.util.isNullOrEmpty(item.endTime().value()))     
                    });     
                }   
            }
            
            convertToJS(appStamp: KnockoutObservable<vmbase.AppStampGoOutPermit>){
                return {
                    stampAtr: appStamp.stampAtr(),
                    stampFrameNo: appStamp.stampFrameNo(),
                    stampGoOutAtr: appStamp.stampGoOutAtr(),
                    startTime: appStamp.startTime().value(),
                    startLocation: appStamp.startLocation().code(),
                    endTime: appStamp.endTime().value(),
                    endLocation: appStamp.endLocation().code()    
                }           
            }
            
            openSelectLocationDialog(timeType: string, frameNo: number){
                var self = this;
                nts.uk.ui.windows.setShared('KDL010SelectWorkLocation', self.appStampList()[frameNo][timeType+'Location']().code());
                nts.uk.ui.windows.sub.modal("/view/kdl/010/a/index.xhtml", { title: "割増項目の設定", dialogClass: "no-close" }).onClosed(function() {
                    if(nts.uk.ui.windows.getShared("KDL010workLocation")!=null){
                        let workLocation = nts.uk.ui.windows.getShared("KDL010workLocation");
                        self.appStampList()[frameNo][timeType+'Location']().code(workLocation);   
                        self.appStampList()[frameNo][timeType+'Location']().name(self.findWorkLocationName(workLocation));   
                    }
                });      
            }
            
            validateInput(appStampList: KnockoutObservableArray<vmbase.AppStampGoOutPermit>){
                _.forEach(appStampList(), (x,i) =>{
                    if(!nts.uk.util.isNullOrEmpty(x.startTime().value())){
                        $(".m1-start-input:eq("+i+")").ntsError('check');            
                    } else {
                            
                    } 
                    if(!nts.uk.util.isNullOrEmpty(x.endTime().value())){
                        $(".m1-end-input:eq("+i+")").ntsError('check');    
                    } else {
                            
                    }     
                });    
            }
            
            getAttendanceItem(date: any, employeeList: Array<any>){
                let self = this;
                var dfd = $.Deferred();
                service.getAttendanceItem({
                    employeeIDLst: employeeList, 
                    date: date,
                    stampRequestMode: 0
                }).done((data)=>{
                    if(!nts.uk.util.isNullOrEmpty(data)){
                        self.attendanceItems = data[0].attendanceItems;    
                    }
                    self.refreshData();
                    dfd.resolve();
                }).fail((res)=>{
                    dfd.reject(res);    
                });    
                return dfd.promise();       
            }
            
            setGoOutItem(attendanceItems: any){
                let self = this;
                self.appStampList.removeAll();
                for(let i=1;i<=self.displayItemNo;i++) {
                    if(i==1){
                        self.setValueAppStamp(attendanceItems, 86, 88, 90, 87, 89, 0);
                    }
                    if(i==2){
                        self.setValueAppStamp(attendanceItems, 91, 93, 95, 92, 94, 1);    
                    }
                    if(i==3){
                        self.setValueAppStamp(attendanceItems, 96, 67, 70, 65, 68, 2);    
                    }
                    if(i==4){
                        self.setValueAppStamp(attendanceItems, 101, 103, 105, 102, 104, 3);     
                    }
                    if(i==5){
                        self.setValueAppStamp(attendanceItems, 106, 108, 110, 107, 109, 4);    
                    }
                    if(i==6){
                        self.setValueAppStamp(attendanceItems, 111, 113, 115, 112, 114, 5);    
                    }
                    if(i==7){
                        self.setValueAppStamp(attendanceItems, 116, 118, 120, 117, 119, 6);    
                    }
                    if(i==8){
                        self.setValueAppStamp(attendanceItems, 121, 123, 125, 122, 124, 7);    
                    }
                    if(i==9){
                        self.setValueAppStamp(attendanceItems, 126, 128, 130, 127, 129, 8);    
                    }
                    if(i==10){
                        self.setValueAppStamp(attendanceItems, 131, 133, 135, 132, 134, 9);     
                    }
                }
            }
            
            setChildCareItem(attendanceItems: any){
                let self = this;
                self.appStampList.removeAll();
                for(let i=1;i<=self.displayItemNo;i++) {
                    if(i==1){
                        self.setValueAppStamp(attendanceItems, null, 747, 748, null, null, 0);
                    }
                    if(i==2){
                        self.setValueAppStamp(attendanceItems, null, 749, 750, null, null, 1);    
                    }
                }    
            }
            
            setCareItem(attendanceItems: any){
                let self = this;
                self.appStampList.removeAll();
                for(let i=1;i<=self.displayItemNo;i++) {
                    if(i==1){
                        self.setValueAppStamp(attendanceItems, null, 751, 752, null, null, 0);
                    }
                    if(i==2){
                        self.setValueAppStamp(attendanceItems, null, 753, 754, null, null, 1);    
                    }
                }    
            }   
            
            setValueAppStamp(attendanceItems: any, goOutAtrID: any, startTimeID: any, endTimeID: any, startPlaceID: any, endPlaceID: any, order: any){
                let self = this, 
                    goOutAtr, startTime, endTime, startPlaceCD, endPlaceCD,
                    goOutAtrParam, 
                    startTimeParam, startTimeCheckValue, startTimeCheckEnable, 
                    endTimeParam, endTimeCheckValue, endTimeCheckEnable,
                    startPlaceCDParam, startPlaceCheckValue, startPlaceCheckEnable,
                    endPlaceCDParam, endPlaceCheckValue, endPlaceCheckEnable;
                goOutAtr = _.find(attendanceItems, (o) => { return o.itemId == goOutAtrID; });    
                if(!nts.uk.util.isNullOrUndefined(goOutAtr)){
                    if(!nts.uk.util.isNullOrUndefined(goOutAtr.value)){
                        goOutAtrParam = parseInt(goOutAtr.value);       
                    } else {
                        goOutAtrParam = _.first(self.stampGoOutAtrList()).code;    
                    }
                } else {
                    goOutAtrParam = _.first(self.stampGoOutAtrList()).code;     
                }      
                startTime = _.find(attendanceItems, (o) => { return o.itemId == startTimeID; });    
                if(!nts.uk.util.isNullOrUndefined(startTime)){
                    if(!nts.uk.util.isNullOrUndefined(startTime.value)){
                        startTimeParam = parseInt(startTime.value);    
                        startTimeCheckEnable = true;
                        startTimeCheckValue = false;
                    } else {
                        startTimeParam = null;    
                        startTimeCheckEnable = false;
                        startTimeCheckValue = true;
                    }
                } else {
                    startTimeParam = null;  
                    startTimeCheckEnable = false;
                    startTimeCheckValue = true;    
                }
                endTime = _.find(attendanceItems, (o) => { return o.itemId == endTimeID; });    
                if(!nts.uk.util.isNullOrUndefined(endTime)){
                    if(!nts.uk.util.isNullOrUndefined(endTime.value)){
                        endTimeParam = parseInt(endTime.value); 
                        endTimeCheckEnable = true;
                        endTimeCheckValue = false;
                    } else {
                        endTimeParam = null;    
                        endTimeCheckEnable = false;
                        endTimeCheckValue = true;
                    }  
                } else {
                    endTimeParam = null;     
                    endTimeCheckEnable = false;
                    endTimeCheckValue = true; 
                }
                startPlaceCD = _.find(attendanceItems, (o) => { return o.itemId == startPlaceID; });    
                if(!nts.uk.util.isNullOrUndefined(startPlaceCD)){
                    if(!nts.uk.util.isNullOrUndefined(startPlaceCD.value)){
                        startPlaceCDParam = startPlaceCD.value;   
                        startPlaceCheckEnable = true;
                        startPlaceCheckValue = false;
                    } else {
                        startPlaceCDParam = null;    
                        startPlaceCheckEnable = false;
                        startPlaceCheckValue = true;
                    }
                } else {
                    startPlaceCDParam = null;     
                    startPlaceCheckEnable = false;
                    startPlaceCheckValue = true; 
                }
                endPlaceCD = _.find(attendanceItems, (o) => { return o.itemId == endPlaceID; });    
                if(!nts.uk.util.isNullOrUndefined(endPlaceCD)){
                    if(!nts.uk.util.isNullOrUndefined(endPlaceCD.value)){
                        endPlaceCDParam = endPlaceCD.value; 
                        endPlaceCheckEnable = true;
                        endPlaceCheckValue = false;   
                    } else {
                        endPlaceCDParam = null;  
                        endPlaceCheckEnable = false;
                        endPlaceCheckValue = true;  
                    }
                } else {
                    endPlaceCDParam = null;      
                    endPlaceCheckEnable = false;
                    endPlaceCheckValue = true;
                }   
                self.appStampList.push(
                    new vmbase.AppStampGoOutPermit(
                        self.stampAtr(),
                        order,
                        goOutAtrParam,
                        new vmbase.CheckBoxTime(startTimeParam, startTimeCheckValue, startTimeCheckEnable),
                        new vmbase.CheckBoxLocation(startPlaceCDParam,self.findWorkLocationName(startPlaceCDParam), startPlaceCheckValue, startPlaceCheckEnable),
                        new vmbase.CheckBoxTime(endTimeParam, endTimeCheckValue, endTimeCheckEnable),
                        new vmbase.CheckBoxLocation(endPlaceCDParam,self.findWorkLocationName(endPlaceCDParam), endPlaceCheckValue, endPlaceCheckEnable) 
                ));     
            }
        }
    }
}