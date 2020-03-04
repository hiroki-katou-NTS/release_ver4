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
        messageArea : KnockoutObservable<boolean> = ko.observable(false);
        
        errorFlag: number = 0;
        errorMsg: string = '';
        
        approvalRootState: any = ko.observableArray([]);
        messFullDisp: KnockoutObservable<boolean> = ko.observable(false);
        messDealineFullDisp: KnockoutObservable<boolean> = ko.observable(false);
        
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
            nts.uk.ui.block.invisible();
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
                    switch(data.errorFlag){
                        case 1:
                            // $('#listApproverRootState').ntsError('set', {messageId:"Msg_324"});
                            // $("#inputdate").ntsError('set', {messageId:"Msg_324"});
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_324" }).then(function(){
                                nts.uk.ui.block.clear();
                            });
                            break;
                        case 2: 
                            // $('#listApproverRootState').ntsError('set', {messageId:"Msg_238"});
                            // $("#inputdate").ntsError('set', {messageId:"Msg_238"});
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_238" }).then(function(){
                                nts.uk.ui.block.clear();
                            });
                            break;
                        case 3:
                            // $('#listApproverRootState').ntsError('set', {messageId:"Msg_237"});
                            // $("#inputdate").ntsError('set', {messageId:"Msg_237"});
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_237" }).then(function(){
                                nts.uk.ui.block.clear();
                            });
                            break;
                        default: 
                    }  
                }
                let deadlineMsg = data.outputMessageDeadline;
                if(!nts.uk.text.isNullOrEmpty(deadlineMsg.message)){
                    self.reasonOutputMessFull(deadlineMsg.message.replace(/\n/ig, '<br/>'));    
                }
                if(!_.isEmpty(self.reasonOutputMessFull())){
                    self.messFullDisp(true);    
                }
                if(!nts.uk.text.isNullOrEmpty(deadlineMsg.deadline)){
                    self.reasonOutputMessDealineFull(deadlineMsg.deadline);
                }
                if(!_.isEmpty(self.reasonOutputMessDealineFull())){
                    self.messDealineFullDisp(true);    
                }
                self.messageArea(deadlineMsg.chkShow);
                if(self.appType() == 0){
                     if(deadlineMsg.chkShow) {  
                        $('#message_ct').removeClass();
                        $('#message_ct').addClass("message");
                    } else {
                        $('#message_ct').removeClass();
                        $('#message_ct').addClass("message_none");
                    }
                }
                nts.uk.ui.block.clear(); 
                dfd.resolve(data);
            }).fail((res)=>{
                nts.uk.ui.block.clear(); 
                dfd.reject(res);    
            });            
            return dfd.promise();
        }
        
        isFirstIndexFrame(loopPhase, loopFrame, loopApprover) {
            let self = this;
            if(_.size(loopFrame.listApprover()) > 1) {
                return _.findIndex(loopFrame.listApprover(), o => o == loopApprover) == 0;  
            }
            let firstIndex = _.chain(loopPhase.listApprovalFrame()).filter(x => _.size(x.listApprover()) > 0).orderBy(x => x.frameOrder()).first().value().frameOrder();  
            let approver = _.find(loopPhase.listApprovalFrame(), o => o == loopFrame);
            if(approver) {
                return approver.frameOrder() == firstIndex;    
            }
            return false;
        }
        
        getFrameIndex(loopPhase, loopFrame, loopApprover) {
            let self = this;
            if(_.size(loopFrame.listApprover()) > 1) {
                return _.findIndex(loopFrame.listApprover(), o => o == loopApprover);     
            }
            return _.findIndex(loopPhase.listApprovalFrame(), o => o == loopFrame);    
        }
        
        frameCount(listFrame) {
            let self = this;    
            let listExist = _.filter(listFrame, x => _.size(x.listApprover()) > 0);
            if(_.size(listExist) > 1) { 
                return _.size(listExist);
            }
            return _.chain(listExist).map(o => self.approverCount(o.listApprover())).value()[0];        
        }
        
        approverCount(listApprover) {
            let self = this;
            return _.chain(listApprover).countBy().values().value()[0];     
        }
        
        getApproverAtr(approver) {
            if(approver.approvalAtrName() !='未承認'){
                if(approver.representerName().length > 0){
                    if(approver.representerMail().length > 0){
                        return approver.representerName() + '(@)';
                    } else {
                        return approver.representerName();
                    }    
                } else {
                    if(approver.approverMail().length > 0){
                        return approver.approverName() + '(@)';
                    } else {
                        return approver.approverName();
                    }
                }
            } else {
                var s = '';
                s = s + approver.approverName();
                if(approver.approverMail().length > 0){
                    s = s + '(@)';
                }
                if(approver.representerName().length > 0){
                    if(approver.representerMail().length > 0){
                        s = s + '(' + approver.representerName() + '(@))';
                    } else {
                        s = s + '(' + approver.representerName() + ')';
                    }
                }   
                return s;
            }        
        }
    }
    
    
}