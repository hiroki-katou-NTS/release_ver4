module nts.uk.at.view.kaf002.m2 {
    import service = nts.uk.at.view.kaf002.shr.service;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    export module viewmodel {
        export class ScreenModel {
            extendsMode: KnockoutObservable<boolean> = ko.observable(false);
            extendsModeDisplay: KnockoutObservable<boolean> = ko.observable(true);
            appStampList: KnockoutObservableArray<vmbase.AppStampWork> = ko.observableArray([]);    
            supFrameNo: number = 1;
            stampPlaceDisplay: KnockoutObservable<number> = ko.observable(0);
            workLocationList: Array<vmbase.IWorkLocation> = [];
            displayAllLabel: KnockoutObservable<string> = ko.observable(nts.uk.resource.getText("KAF002_13", nts.uk.resource.getText('KAF002_29'))); 
            displayItemNo: number = this.supFrameNo;
            constructor(){
                var self = this;
                self.extendsMode.subscribe((v)=>{ 
                    if(v){
                        self.refreshData();
                    }
                });        
            }
            
            start(appStampData: any, data: vmbase.StampRequestSettingDto, listWorkLocation: Array<vmbase.IWorkLocation>){
                var self = this;    
                self.workLocationList = listWorkLocation;
                self.supFrameNo = data.supFrameDispNO;
                self.refreshData();
                self.stampPlaceDisplay(data.stampPlaceDisp);
                if(!nts.uk.util.isNullOrUndefined(appStampData)){
                    self.appStampList.removeAll();
                    _.forEach(appStampData, item => {
                        self.appStampList.push(
                            new vmbase.AppStampWork(
                                item.stampAtr,
                                item.stampFrameNo,
                                item.stampGoOutReason,
                                new vmbase.CheckBoxLocation(item.supportCard,'',true,false),
                                new vmbase.CheckBoxLocation(item.supportLocation,self.findWorkLocationName(item.supportLocation),true,false),
                                new vmbase.CheckBoxTime(item.startTime,true,false),
                                new vmbase.CheckBoxLocation(item.startLocation,self.findWorkLocationName(item.startLocation),true,false),
                                new vmbase.CheckBoxTime(item.endTime,true,false),
                                new vmbase.CheckBoxLocation(item.endLocation,self.findWorkLocationName(item.endLocation),true,false) 
                        ));        
                    });
                }
                nts.uk.ui.block.clear();
            }
            
            extendsModeEvent(){
                var self = this;
                self.displayItemNo = 5;
                self.extendsMode(!self.extendsMode());    
                self.extendsModeDisplay(!self.extendsMode()); 
            }
            
            refreshData(){
                var self = this;
                self.appStampList.removeAll();
                for(let i=1;i<=self.displayItemNo;i++) {
                    self.appStampList.push(
                        new vmbase.AppStampWork(
                            0,
                            i,
                            0,
                            new vmbase.CheckBoxLocation('','',true,false),
                            new vmbase.CheckBoxLocation('','',true,false),
                            new vmbase.CheckBoxTime(null,true,false),
                            new vmbase.CheckBoxLocation('','',true,false),
                            new vmbase.CheckBoxTime(null,true,false),
                            new vmbase.CheckBoxLocation('','',true,false)));    
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
            
            register(application : vmbase.Application){
                var self = this;
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
                    appStampWorkCmds: _.map(self.appStampList(), (item) => self.convertToJS(item)),
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: null 
                }
                if(!nts.uk.util.isNullOrEmpty(command.appStampWorkCmds)){
                    nts.uk.ui.block.invisible();
                    service.insert(command)
                    .done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function(){
                            location.reload();
                            $('.cm-memo').focus();
                            nts.uk.ui.block.clear();
                        });     
                    })
                    .fail(function(res) { 
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function(){nts.uk.ui.block.clear();});  
                    }); 
                }
            }
            
            update(application : vmbase.Application){
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
                    stampRequestMode: 1,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: _.map(self.appStampList(), (item) => self.convertToJS(item)),
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: null 
                }
                if(!nts.uk.util.isNullOrEmpty(command.appStampWorkCmds)){
                    nts.uk.ui.block.invisible();
                    service.update(command)
                    .done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function(){
                            location.reload();
                            $('.cm-memo').focus();
                            nts.uk.ui.block.clear();
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
        }
    }
}