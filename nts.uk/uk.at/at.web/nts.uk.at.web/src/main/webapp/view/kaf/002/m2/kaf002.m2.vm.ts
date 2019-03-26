module nts.uk.at.view.kaf002.m2 {
    import service = nts.uk.at.view.kaf002.shr.service;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import setShared = nts.uk.ui.windows.setShared;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel {
        export class ScreenModel {
            extendsMode: KnockoutObservable<boolean> = ko.observable(false);
            extendsModeDisplay: KnockoutObservable<boolean> = ko.observable(true);
            appStampList: KnockoutObservableArray<vmbase.AppStampWork> = ko.observableArray([]);    
            supFrameNo: number = 5;
            stampPlaceDisplay: KnockoutObservable<number> = ko.observable(0);
            workLocationList: Array<vmbase.IWorkLocation> = [];
            displayAllLabel: KnockoutObservable<string> = ko.observable(nts.uk.resource.getText("KAF002_56")); 
            displayItemNo: number = this.supFrameNo;
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
                // self.supFrameNo = data.supFrameDispNO;
                self.refreshData();
                self.stampPlaceDisplay(stampRequestSetDto.stampPlaceDisp);
                self.getAttendanceItem(appDate, [self.employeeID]).done(()=>{
                    if(!nts.uk.util.isNullOrUndefined(appStampData)){
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
            }
            
            extendsModeEvent(){
                var self = this;
                self.extendsMode(!self.extendsMode());    
                self.extendsModeDisplay(!self.extendsMode()); 
            }
            
            refreshData(){
                var self = this;   
                self.setWorkItem(self.attendanceItems);
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
                $('#appDate').trigger("validate");
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
                    stampRequestMode: 1,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: _.map(self.filterAppStamp(self.appStampList()), item => self.convertToJS(item)),
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: null,
                    checkOver1Year: true
                }
                if(nts.uk.util.isNullOrEmpty(command.appStampWorkCmds)){
                    $('.m2-time-editor').first().ntsError('set', {messageId:"Msg_308"});        
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
                    stampRequestMode: 1,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: _.map(self.filterAppStamp(self.appStampList()), item => self.convertToJS(item)),
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
            
            validateInput(appStampList: KnockoutObservableArray<vmbase.AppStampWork>){
                _.forEach(appStampList(), (x,i) =>{
                    if(!nts.uk.util.isNullOrEmpty(x.startTime().value())){
                        $(".m2-start-input:eq("+i+")").ntsError('check');            
                    } else {
                            
                    } 
                    if(!nts.uk.util.isNullOrEmpty(x.endTime().value())){
                        $(".m2-end-input:eq("+i+")").ntsError('check');    
                    } else {
                            
                    }     
                });    
            }
            
            setWorkItem(attendanceItems: any){
                let self = this;
                self.appStampList.removeAll();
                for(let i=1;i<=self.displayItemNo;i++) {
                    if(i==1){
                        self.setValueAppStamp(attendanceItems, 31, 34, 30, 33, 0);
                    }
                    if(i==2){
                        self.setValueAppStamp(attendanceItems, 41, 44, 40, 43, 1);    
                    }
                    if(i==3){
                        self.setValueAppStamp(attendanceItems, 51, 53, 50, 52, 2);    
                    }
                    if(i==4){
                        self.setValueAppStamp(attendanceItems, 59, 61, 58, 60, 3);     
                    }
                    if(i==5){
                        self.setValueAppStamp(attendanceItems, 67, 69, 66, 68, 4);    
                    }
                }
            }
            
            setValueAppStamp(attendanceItems: any, startTimeID: any, endTimeID: any, startPlaceID: any, endPlaceID: any, order: any){
                let self = this, 
                    startTime, endTime, startPlaceCD, endPlaceCD,
                    startTimeParam, startTimeCheckValue, startTimeCheckEnable, 
                    endTimeParam, endTimeCheckValue, endTimeCheckEnable,
                    startPlaceCDParam, startPlaceCheckValue, startPlaceCheckEnable,
                    endPlaceCDParam, endPlaceCheckValue, endPlaceCheckEnable;  
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
                        0,
                        order,
                        0,
                        new vmbase.CheckBoxLocation("","",true,false),
                        new vmbase.CheckBoxLocation("","",true,false),
                        new vmbase.CheckBoxTime(startTimeParam, startTimeCheckValue, startTimeCheckEnable),
                        new vmbase.CheckBoxLocation(startPlaceCDParam,self.findWorkLocationName(startPlaceCDParam), startPlaceCheckValue, startPlaceCheckEnable),
                        new vmbase.CheckBoxTime(endTimeParam, endTimeCheckValue, endTimeCheckEnable),
                        new vmbase.CheckBoxLocation(endPlaceCDParam,self.findWorkLocationName(endPlaceCDParam), endPlaceCheckValue, endPlaceCheckEnable)     
                    
                ));     
            }
            
            getAttendanceItem(date: any, employeeList: Array<any>){
                let self = this;
                var dfd = $.Deferred();
                service.getAttendanceItem({
                    employeeIDLst: employeeList, 
                    date: date,
                    stampRequestMode: 1
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
            
            filterAppStamp(appStamp: KnockoutObservableArray<vmbase.AppStampWork>){
                var self = this;
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