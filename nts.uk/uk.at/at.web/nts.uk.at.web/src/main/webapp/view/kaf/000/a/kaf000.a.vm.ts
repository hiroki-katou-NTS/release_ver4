module nts.uk.at.view.kaf000.a.viewmodel{
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import shrvm = nts.uk.at.view.kaf000.shr;
    export class ScreenModel{
        /**
         * List
         */
        //listPhaseID
        listPhaseID: Array<String>;
        //List  Approval Root 
        listApprovalRoot :  KnockoutObservableArray<Array<shrvm.model.ApprovalRootOutput>> = ko.observableArray([]);
         //Item  Approval Root 
        approvalRoot :  KnockoutObservableArray<shrvm.model.ApprovalRootOutput> = ko.observableArray([]);
        
        /**
         * obj input
         */
        //obj 
        
        objApprovalRootInput : KnockoutObservable<shrvm.model.ObjApprovalRootInput>;
        
        //obj output message deadline
        outputMessageDeadline : KnockoutObservable<shrvm.model.OutputMessageDeadline> = ko.observable(null);
        
        //app type
        appType : KnockoutObservable<number> = ko.observable(0);
        
        approvalList: Array<shrvm.model.AppApprovalPhase> = [];
        reasonOutputMess : string = nts.uk.resource.getText('KAF000_1');
        reasonOutputMessFull: KnockoutObservable<string> = ko.observable('');
        reasonOutputMessDealine : string = nts.uk.resource.getText('KAF000_2');
        reasonOutputMessDealineFull: KnockoutObservable<string> = ko.observable('');
        messageArea : KnockoutObservable<boolean> = ko.observable(true);
        
        errorFlag: number = 0;
        errorMsg: string = '';
        
        approvalRootState: any = ko.observableArray([]);
        
        constructor(){
            let self = this;
            let baseDate = new Date();
            let date = baseDate.getFullYear + "/" + baseDate.getMonth + "/" + baseDate.getDate;
            self.objApprovalRootInput = ko.observable(new shrvm.model.ObjApprovalRootInput("", 1,1, date))
        }
        /**
         *
           sid 社員ID（申請本人の社員ID）
           employmentRootAtr 就業ルート区分
           subjectRequest 対象申請
           baseDate 基準日
           workplaceID 
         */
        start( sid: any, employmentRootAtr: any,appType: any,standardDate: any, overtimeAtr: any): JQueryPromise<any> {
            let self = this;
            self.objApprovalRootInput().sid = sid;
            self.objApprovalRootInput().employmentRootAtr =employmentRootAtr;
            self.objApprovalRootInput().appType = appType;
            self.objApprovalRootInput().standardDate = standardDate;
            
            self.appType(appType);
            
            let dfd = $.Deferred();
            
            //Call approval list
            self.getAppDataDate(appType, standardDate, true,sid, overtimeAtr).done(function(data) {
                dfd.resolve(data); 
            }).fail((res)=>{
                nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function(){
                    nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml"); 
                    nts.uk.ui.block.clear();
                });  
            });
            return dfd.promise();
        }
        
        getAppDataDate(appType: number, appDate: string, isStartup: boolean, employeeID: string, overtimeAtr: any): JQueryPromise<any> {
            var self = this;
            let dfd = $.Deferred<any>();
            nts.uk.at.view.kaf000.a.service.getAppDataDate({
                appTypeValue: appType,
                appDate: appDate,
                isStartup: isStartup,
                employeeID: employeeID,
                overtimeAtrParam: overtimeAtr
            }).done((data)=>{
                $('#listApproverRootState').ntsError('clear');
                if(!nts.uk.util.isNullOrEmpty(data.errorFlag)){
                    if(data.errorFlag==0){
                        if(!nts.uk.util.isNullOrEmpty(data.listApprovalPhaseStateDto)){
                            self.approvalRootState(ko.mapping.fromJS(data.listApprovalPhaseStateDto)());       
                        }
                    }
                    if(isStartup==false){
                        switch(data.errorFlag){
                            case 1:
                                $('#listApproverRootState').ntsError('set', {messageId:"Msg_324"});
                                nts.uk.ui.dialog.alertError({ messageId: "Msg_324" }).then(function(){
                                    nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
                                });
                                break;
                            case 2: 
                                $('#listApproverRootState').ntsError('set', {messageId:"Msg_238"});
                                nts.uk.ui.dialog.alertError({ messageId: "Msg_238" }).then(function(){
                                    nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
                                });
                                break;
                            case 3:
                                $('#listApproverRootState').ntsError('set', {messageId:"Msg_237"});
                                nts.uk.ui.dialog.alertError({ messageId: "Msg_237" }).then(function(){
                                    nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml");
                                });
                                break;
                            default: 
                        }       
                    }
                }
                let deadlineMsg = data.outputMessageDeadline;
                if(!nts.uk.text.isNullOrEmpty(deadlineMsg.message)){
                    self.reasonOutputMessFull(deadlineMsg.message.replace(/\n/ig, '<br/>'));    
                }
                if(!nts.uk.text.isNullOrEmpty(deadlineMsg.deadline)){
                    self.reasonOutputMessDealineFull(deadlineMsg.deadline);
                }
                self.messageArea(deadlineMsg.chkShow);
                dfd.resolve(data);
            }).fail((res)=>{
                dfd.reject(res);    
            });            
            return dfd.promise();
        }
    }
    
    
}