module nts.uk.at.view.kaf002.m3 {
    import service = nts.uk.at.view.kaf002.shr.service;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import setShared = nts.uk.ui.windows.setShared;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel {
        export class ScreenModel {
            appStampList: KnockoutObservableArray<vmbase.AppStampCancel> = ko.observableArray([]); 
            supFrameNo: number = 5;
            stampPlaceDisplay: KnockoutObservable<number> = ko.observable(0);
            editable: KnockoutObservable<boolean> = ko.observable(true);
            screenMode: KnockoutObservable<number> = ko.observable(0);
            attendanceItems: Array<any> = [];
            employeeID: string;
            
            start(appStampData: any, data: vmbase.AppStampNewSetDto, listWorkLocation: Array<vmbase.IWorkLocation>, 
                editable: any, screenMode: any, employeeID: string, appDate: any){
                var self = this;    
                self.employeeID = employeeID;
                self.screenMode(screenMode);
                self.editable(editable); 
                //self.supFrameNo = data.supFrameDispNO;
                self.getAttendanceItem(appDate, [self.employeeID]).done(()=>{
                    let appStampCancelCmds = appStampData.appStampCancelCmds;
                    if(!nts.uk.util.isNullOrUndefined(appStampCancelCmds)){
                        if(appStampData.reflected){
                            _.forEach(appStampCancelCmds, item => {
                                let stampLoop = _.find(self.appStampList(), (o) => { 
                                    return (o.stampFrameNo==item.stampFrameNo)&&(o.stampAtr==item.stampAtr); 
                                });     
                                self.appStampList.remove(stampLoop);   
                            });    
                        } else {
                            _.forEach(appStampCancelCmds, item => {
                                let indexLoop = _.findIndex(self.appStampList(), (o) => { 
                                    return (o.stampFrameNo==item.stampFrameNo)&&(o.stampAtr==item.stampAtr); 
                                });  
                                self.appStampList()[indexLoop].cancelAtr(item.cancelAtr);
                            }); 
                        }
                    } else {
                        self.refreshData();    
                    }
                    nts.uk.ui.block.clear();        
                }).fail(()=>{
                    nts.uk.ui.block.clear();    
                }); 
            }
            
            register(application : vmbase.Application, checkBoxValue: boolean){
                nts.uk.ui.block.invisible();
                var self = this;
                let command = {
                    appID: "",
                    inputDate: application.inputDate(),
                    enteredPerson: application.enteredPerson(),
                    applicationDate: application.appDate(),
                    titleReason: application.titleReason(), 
                    detailReason: application.contentReason(),
                    employeeID: application.employeeID(),
                    stampRequestMode: 2,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: null, 
                    appStampCancelCmds: _.map(self.filterAppStamp(self.appStampList()), item => self.convertToJS(item)),
                    appStampOnlineRecordCmd: null,
                    checkOver1Year: true
                }
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
                nts.uk.ui.block.invisible();
                var self = this;
                let command = {
                    version: application.version,
                    appID: application.applicationID(),
                    inputDate: application.inputDate(),
                    enteredPerson: application.enteredPerson(),
                    applicationDate: application.appDate(),
                    titleReason: application.titleReason(), 
                    detailReason: application.contentReason(),
                    employeeID: application.employeeID(),
                    stampRequestMode: 2,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: null,
                    appStampCancelCmds: _.map(self.filterAppStamp(self.appStampList()), item => self.convertToJS(item)),
                    appStampOnlineRecordCmd: null
                }
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
            
            filterAppStamp(appStamp: KnockoutObservableArray<vmbase.AppStampWork>){
                var self = this;
                return _.filter(appStamp, item => { return item.cancelAtr()==1 });  
            }
            
            convertToJS(appStamp: KnockoutObservable<vmbase.AppStampCancel>){
                return {
                    stampAtr: appStamp.stampAtr,
                    stampFrameNo: appStamp.stampFrameNo,
                    cancelAtr: appStamp.cancelAtr()
                }           
            }
            
            createLabel(stampFrameNo: number, stampAtr: number){
                let label = "";
                switch(stampAtr){
                    case 0: {
                        if(stampFrameNo<2){
                            label = nts.uk.resource.getText("KAF002_65",[stampFrameNo+1]);        
                        } else {
                            label = nts.uk.resource.getText("KAF002_66",[stampFrameNo-1]);        
                        }   
                        break;
                    }    
                    case 1:
                        label = nts.uk.resource.getText("KAF002_67",[stampFrameNo+1]); 
                        break;  
                    case 2:
                        label = nts.uk.resource.getText("KAF002_68",[stampFrameNo+1]); 
                        break;
                    case 3:
                        label = nts.uk.resource.getText("KAF002_69",[stampFrameNo+1]); 
                        break;
                    case 4:
                        label = nts.uk.resource.getText("KAF002_70",[stampFrameNo+1]); 
                        break;
                    default: break;
                }    
                return label;
            }
            
            getAttendanceItem(date: any, employeeList: Array<any>){
                let self = this;
                var dfd = $.Deferred();
                service.getAttendanceItem({
                    employeeIDLst: employeeList, 
                    date: date,
                    stampRequestMode: 2
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
            
            refreshData(){
                var self = this;   
                self.appStampList.removeAll();
                self.setGoOutItem(self.attendanceItems);
                self.setChildCareItem(self.attendanceItems);
                self.setCareItem(self.attendanceItems);
                self.setWorkItem(self.attendanceItems);
            }
            
            setValueAppStamp(attendanceItems: any, startTimeID: any, endTimeID: any, stampFrameNo: number, stampAtr: number){
                let self = this, 
                    startTime, endTime,
                    startTimeParam, endTimeParam, 
                    label; 
                startTime = _.find(attendanceItems, (o) => { return o.itemId == startTimeID; });    
                if(!nts.uk.util.isNullOrUndefined(startTime)){
                    if(!nts.uk.util.isNullOrUndefined(startTime.value)){
                        startTimeParam = nts.uk.time.format.byId("ClockDay_Short_HM", parseInt(startTime.value));    
                    } else {
                        startTimeParam = "";    
                    }
                } else {
                    startTimeParam = "";   
                }
                endTime = _.find(attendanceItems, (o) => { return o.itemId == endTimeID; });    
                if(!nts.uk.util.isNullOrUndefined(endTime)){
                    if(!nts.uk.util.isNullOrUndefined(endTime.value)){
                        endTimeParam = nts.uk.time.format.byId("ClockDay_Short_HM", parseInt(endTime.value)); 
                    } else {
                        endTimeParam = "";    
                    }  
                } else {
                    endTimeParam = "";     
                } 
                if((!nts.uk.util.isNullOrEmpty(startTimeParam))||(!nts.uk.util.isNullOrEmpty(endTimeParam))){
                    label = self.createLabel(stampFrameNo, stampAtr);
                    return new vmbase.AppStampCancel(stampFrameNo, stampAtr, label, startTimeParam, endTimeParam, 0);               
                } else {
                    return null;    
                }
                    
            }
            
            setGoOutItem(attendanceItems: any){
                let self = this;
                let a = [];
                a.push(self.setValueAppStamp(attendanceItems, 88, 90, 0, 1));
                a.push(self.setValueAppStamp(attendanceItems, 93, 95, 1, 1));  
                a.push(self.setValueAppStamp(attendanceItems, 67, 70, 2, 1));   
                a.push(self.setValueAppStamp(attendanceItems, 103, 105, 3, 1)); 
                a.push(self.setValueAppStamp(attendanceItems, 108, 110, 4, 1));  
                a.push(self.setValueAppStamp(attendanceItems, 113, 115, 5, 1)); 
                a.push(self.setValueAppStamp(attendanceItems, 118, 120, 6, 1)); 
                a.push(self.setValueAppStamp(attendanceItems, 123, 125, 7, 1)); 
                a.push(self.setValueAppStamp(attendanceItems, 128, 130, 8, 1));
                a.push(self.setValueAppStamp(attendanceItems, 133, 135, 9, 1));
                _.remove(a, o => { return o == null});
                _.forEach(a, item => { self.appStampList.push(item); }); 
            }
            
            setChildCareItem(attendanceItems: any){
                let self = this;
                let a = [];
                a.push(self.setValueAppStamp(attendanceItems, 747, 748, 0, 2));
                a.push(self.setValueAppStamp(attendanceItems, 749, 750, 1, 2));   
                _.remove(a, o => { return o == null});
                _.forEach(a, item => { self.appStampList.push(item); });
            }
            
            setCareItem(attendanceItems: any){
                let self = this;
                let a = [];
                a.push(self.setValueAppStamp(attendanceItems, 751, 752, 0, 3));
                a.push(self.setValueAppStamp(attendanceItems, 753, 754, 1, 3)); 
                _.remove(a, o => { return o == null});
                _.forEach(a, item => { self.appStampList.push(item); });
            }   
            
            setWorkItem(attendanceItems: any){
                let self = this;
                let a = [];
                a.push(self.setValueAppStamp(attendanceItems, 31, 34, 0, 0));
                a.push(self.setValueAppStamp(attendanceItems, 41, 44, 1, 0)); 
                a.push(self.setValueAppStamp(attendanceItems, 51, 53, 2, 0)); 
                a.push(self.setValueAppStamp(attendanceItems, 59, 61, 3, 0)); 
                a.push(self.setValueAppStamp(attendanceItems, 67, 69, 4, 0));
                _.remove(a, o => { return o == null});
                _.forEach(a, item => { self.appStampList.push(item); });
            }
        }
    }
}