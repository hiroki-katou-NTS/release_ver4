module nts.uk.at.view.kaf002.m4 {
    import service = nts.uk.at.view.kaf002.shr.service;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    export module viewmodel {
        export class ScreenModel {
            appStamp: KnockoutObservable<vmbase.AppStampOnlineRecord> = ko.observable(new vmbase.AppStampOnlineRecord(0,null)); 
            supFrameNo: number = 10;
            stampPlaceDisplay: KnockoutObservable<number> = ko.observable(0);
            stampCombinationList: KnockoutObservableArray<vmbase.StampCombination> = ko.observableArray([]);  
            
            start(appStampData: any, data: vmbase.StampRequestSettingDto, listWorkLocation: Array<any>){
                var self = this;    
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
            
            register(application : vmbase.Application, approvalList: Array<vmbase.AppApprovalPhase>){
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
                        appApprovalPhaseCmds: approvalList 
                    }
                    service.insert(command)
                    .done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function(){
                            location.reload();
                            $('.cm-memo').focus();
                            nts.uk.ui.block.clear();
                        });     
                    })
                    .fail(function(res) { 
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId}).then(function(){nts.uk.ui.block.clear();});
                    });
                }
            }
            
            update(application : vmbase.Application, approvalList: Array<vmbase.AppApprovalPhase>){
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
                    appStampOnlineRecordCmd: ko.mapping.toJS(self.appStamp()),
                    appApprovalPhaseCmds: approvalList 
                }
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
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function(){nts.uk.ui.block.clear();});    
                    } else {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId}).then(function(){nts.uk.ui.block.clear();});    
                    }
                });  
            }
        }
    }
}