module nts.uk.at.view.kaf002.m5 {
    import service = nts.uk.at.view.kaf002.shr.service;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import setShared = nts.uk.ui.windows.setShared;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel {
        export class ScreenModel {
            supFrameNo: number = 1;
            stampPlaceDisplay: KnockoutObservable<number> = ko.observable(0);
            stampAtr: KnockoutObservable<number> = ko.observable(0); 
            appStampList: KnockoutObservableArray<vmbase.AppStampWork> = ko.observableArray([]);
            stampAtrList: KnockoutObservableArray<any> = ko.observableArray([]);
            stampGoOutAtrList: KnockoutObservableArray<any> = ko.observableArray([]);
            workLocationList: Array<vmbase.IWorkLocation> = [];
            displayItemNo: number = 5;
            editable: KnockoutObservable<boolean> = ko.observable(true);
            screenMode: KnockoutObservable<number> = ko.observable(0);
            attendanceItems: Array<any> = [];
            employeeID: string;
            
            start(appStampData: any, data: vmbase.AppStampNewSetDto, listWorkLocation: Array<vmbase.IWorkLocation>, 
                editable: any, screenMode: any, employeeID: string, appDate: any){
                var self = this;   
                let stampRequestSetDto = data.appStampSetDto.stampRequestSettingDto;
                self.employeeID = employeeID;
                self.screenMode(screenMode);
                self.editable(editable); 
                self.workLocationList = listWorkLocation;
                self.supFrameNo = 1;
                self.stampPlaceDisplay(stampRequestSetDto.stampPlaceDisp);
                if(stampRequestSetDto.stampAtr_Work_Disp==1) self.stampAtrList.push({ code: 0, name: nts.uk.resource.getText('KAF002_29') });
                if(stampRequestSetDto.stampAtr_GoOut_Disp==1) self.stampAtrList.push({ code: 1, name: nts.uk.resource.getText('KAF002_31') });
                if(stampRequestSetDto.stampAtr_Care_Disp==1) self.stampAtrList.push({ code: 2, name: nts.uk.resource.getText('KAF002_32') });
                if(stampRequestSetDto.stampAtr_Sup_Disp==1) self.stampAtrList.push({ code: 3, name: nts.uk.resource.getText('KAF002_33') });
                self.stampAtrList.push({ code: 4, name: nts.uk.resource.getText('KAF002_34') });
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
                            let stampInit = new vmbase.AppStampWork(
                                    item.stampAtr,
                                    item.stampFrameNo,
                                    item.stampGoOutReason,
                                    new vmbase.CheckBoxLocation(item.supportCard,"",true,false),
                                    new vmbase.CheckBoxLocation(item.supportLocation,self.findWorkLocationName(item.supportLocation),true,false),
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
                    self.refreshData();
                });
                nts.uk.ui.block.clear();
            }
            
            refreshData(){
                var self = this;
                let stampGoOutAtr = _.first(self.stampGoOutAtrList()).code;
                switch(self.stampAtr()){
                    case 0: {
                        self.displayItemNo = 5;
                        self.setWorkItem(self.attendanceItems);
                        break;
                    }
                    case 1: {
                        self.displayItemNo = 10;
                        self.setGoOutItem(self.attendanceItems); 
                        break;
                    }
                    case 2: {
                        self.displayItemNo = 2;     
                        self.setChildCareItem(self.attendanceItems); 
                        break;
                    }
                    case 3: {
                        self.displayItemNo = 2;
                        self.setCareItem(self.attendanceItems);
                        break;
                    }
                    case 4: { 
                        self.displayItemNo = 10; 
                        self.appStampList.removeAll();
                        for(let i=1;i<=self.displayItemNo;i++) {
                            self.appStampList.push(
                                new vmbase.AppStampWork(
                                    self.stampAtr(),
                                    i-1,
                                    stampGoOutAtr,
                                    new vmbase.CheckBoxLocation("","",true,false),
                                    new vmbase.CheckBoxLocation("","",true,false),
                                    new vmbase.CheckBoxTime(null,true,false),
                                    new vmbase.CheckBoxLocation(null,self.findWorkLocationName(null), true , false),
                                    new vmbase.CheckBoxTime(null,true,false),
                                    new vmbase.CheckBoxLocation(null,self.findWorkLocationName(null), true , false)     
                                
                            ));     
                        }
                        break;
                    }
                    default: break;      
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
                    stampRequestMode: 4,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: _.filter(
                                                _.map(self.appStampList(), (item) => self.convertToJS(item)), 
                                                o => { return !nts.uk.util.isNullOrEmpty(o.startTime)||
                                                              !nts.uk.util.isNullOrEmpty(o.startLocation)||
                                                              !nts.uk.util.isNullOrEmpty(o.endTime)}),
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: null,
                    checkOver1Year: true
                }
                if(nts.uk.util.isNullOrEmpty(command.appStampWorkCmds)){
                    $('.m5-time-editor').first().ntsError('set', {messageId:"Msg_308"});
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
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                                .then(function() { nts.uk.ui.block.clear(); });
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
                    stampRequestMode: 4,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: _.map(self.appStampList(), (item) => self.convertToJS(item)),
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: null
                }
                if(!nts.uk.util.isNullOrEmpty(command.appStampWorkCmds)){
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
            
            openSelectCardDialog(frameNo: number){
                // alert('KDL018');
            }
            
            validateInput(appStampList: KnockoutObservableArray<vmbase.AppStampWork>){
                _.forEach(appStampList(), (x,i) =>{
                    if(!nts.uk.util.isNullOrEmpty(x.startTime().value())){
                        $(".m5-start-input:eq("+i+")").ntsError('check');            
                    } else {
                            
                    } 
                    if(!nts.uk.util.isNullOrEmpty(x.endTime().value())){
                        $(".m5-end-input:eq("+i+")").ntsError('check');    
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
                    stampRequestMode: 4
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
            
            setWorkItem(attendanceItems: any){
                let self = this;
                self.appStampList.removeAll();
                for(let i=1;i<=self.displayItemNo;i++) {
                    if(i==1){
                        self.setValueAppStamp(attendanceItems, null, 31, 34, 30, 33, 0);
                    }
                    if(i==2){
                        self.setValueAppStamp(attendanceItems, null, 41, 44, 40, 43, 1);    
                    }
                    if(i==3){
                        self.setValueAppStamp(attendanceItems, null, 51, 53, 50, 52, 2);    
                    }
                    if(i==4){
                        self.setValueAppStamp(attendanceItems, null, 59, 61, 58, 60, 3);     
                    }
                    if(i==5){
                        self.setValueAppStamp(attendanceItems, null, 67, 69, 66, 68, 4);    
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
                    new vmbase.AppStampWork(
                        self.stampAtr(),
                        order,
                        goOutAtrParam,
                        new vmbase.CheckBoxLocation("","",true,false),
                        new vmbase.CheckBoxLocation("","",true,false),
                        new vmbase.CheckBoxTime(startTimeParam, startTimeCheckValue, startTimeCheckEnable),
                        new vmbase.CheckBoxLocation(startPlaceCDParam,self.findWorkLocationName(startPlaceCDParam), startPlaceCheckValue, startPlaceCheckEnable),
                        new vmbase.CheckBoxTime(endTimeParam, endTimeCheckValue, endTimeCheckEnable),
                        new vmbase.CheckBoxLocation(endPlaceCDParam,self.findWorkLocationName(endPlaceCDParam), endPlaceCheckValue, endPlaceCheckEnable)     
                    
                ));     
            }
            
            filterAppStamp(appStamp: KnockoutObservableArray<vmbase.AppStampWork>){
                var self = this;
                if((self.stampAtr()==0)||(self.stampAtr()==1)){
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
                } else if(self.stampAtr()==4){
                    return _.filter(appStamp, item => {
                        return  (item.startTime().checked() && !nts.uk.util.isNullOrEmpty(item.startTime().value())) ||
                                (item.supportCard().checked() && !nts.uk.util.isNullOrEmpty(item.supportCard().code())) ||
                                (item.endTime().checked() && !nts.uk.util.isNullOrEmpty(item.endTime().value()))        
                    });        
                } else {
                    return _.filter(appStamp, item => {
                        return  (item.startTime().checked() && !nts.uk.util.isNullOrEmpty(item.startTime().value())) ||
                                (item.endTime().checked() && !nts.uk.util.isNullOrEmpty(item.endTime().value()))     
                    });     
                }   
            }
            
            convertToJS(appStamp: KnockoutObservable<vmbase.AppStampWork>){
                return {
                    stampAtr: appStamp.stampAtr(),
                    stampFrameNo: appStamp.stampFrameNo(),
                    stampGoOutAtr: appStamp.stampGoOutAtr(),
                    supportCard: appStamp.supportCard().code(),
                    supportLocation: appStamp.supportLocation().code(),
                    startTime: appStamp.startTime().value(),
                    startLocation: appStamp.startLocation().code(),
                    endTime: appStamp.endTime().value(),
                    endLocation: appStamp.endLocation().code()     
                }           
            }
        }
    }
}