module nts.uk.at.view.kaf002.m4 {
    import service = nts.uk.at.view.kaf002.shr.service;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import setShared = nts.uk.ui.windows.setShared;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export module viewmodel {
        export class ScreenModel {
            appStamp: KnockoutObservable<vmbase.AppStampOnlineRecord> = ko.observable(new vmbase.AppStampOnlineRecord(0,null)); 
            supFrameNo: number = 10;
            stampPlaceDisplay: KnockoutObservable<number> = ko.observable(0);
            stampCombinationList: KnockoutObservableArray<vmbase.StampCombination> = ko.observableArray([]);  
            editable: KnockoutObservable<boolean> = ko.observable(true);
            screenMode: KnockoutObservable<number> = ko.observable(0);
            
            start(appStampData: any, data: vmbase.StampRequestSettingDto, listWorkLocation: Array<any>, editable: any, screenMode: any, appDate: any){
                var self = this;    
                self.screenMode(screenMode);
                self.editable(editable);
                self.supFrameNo = data.supFrameDispNO;
                self.stampPlaceDisplay(data.stampPlaceDisp);
                service.getStampCombinationAtr().done(data => {
                    let a = [];
                    _.forEach(data, (item, index) => {
                        a.push(new vmbase.StampCombination(index, item.name));        
                    });   
                    self.stampCombinationList(a);
                    if(!nts.uk.util.isNullOrUndefined(appStampData)){
                        self.appStamp().stampCombinationAtr(appStampData.stampCombinationAtr);
                        self.appStamp().appTime(appStampData.appTime);
                    }
                    nts.uk.ui.block.clear();
                }).fail(res => {
                    nts.uk.ui.block.clear();
                });
            }
            
            register(application : vmbase.Application, checkBoxValue: boolean){
                $(".m4-time-editor").trigger("validate");
                if (!nts.uk.ui.errors.hasError())
                {
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
                        stampRequestMode: 3,
                        appStampGoOutPermitCmds: null,
                        appStampWorkCmds: null, 
                        appStampCancelCmds: null,
                        appStampOnlineRecordCmd: ko.mapping.toJS(self.appStamp()),
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
                    stampRequestMode: 3,
                    appStampGoOutPermitCmds: null,
                    appStampWorkCmds: null, 
                    appStampCancelCmds: null,
                    appStampOnlineRecordCmd: ko.mapping.toJS(self.appStamp())
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
        }
    }
}