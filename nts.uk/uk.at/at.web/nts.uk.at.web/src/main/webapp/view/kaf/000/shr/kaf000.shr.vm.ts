module nts.uk.at.view.kaf000.shr{
    import setShared = nts.uk.ui.windows.setShared;
    export module model {
        // loại người đăng nhập
        // người đại diện tương đương người approver, người confirm có ưu tiên cao hơn
        export enum UserType { 
                 APPLICANT_APPROVER = 0,
                 APPROVER = 1,
                 APPLICANT = 2,
                 OTHER = 3,        
            };
        // trạng thái của phase chứa user
        export enum ApprovalAtr { 
             UNAPPROVED = 0,   
             APPROVED = 1,
             DENIAL = 2,
             REMAND = 3,
        };
        export enum Status {
            NOTREFLECTED = 0, // 未反映
            WAITREFLECTION = 1, //反映待ち
            REFLECTED = 2, //反映済
            WAITCANCEL = 3, //取消待ち
            CANCELED = 4, //取消済
            REMAND = 5,//差し戻し
            DENIAL = 6, //否認
            PASTAPP = 99, //過去申請 
        };
        
        export class ApplicationMetadata {
                appID: string;
                appType: number;
                appDate: Date;
                constructor(appID: string, appType: number, appDate: Date) {
                    this.appID = appID;
                    this.appType = appType;
                    this.appDate = appDate;
                }
            } 
        export class ApprovalRootOutput{
            workplaceId : KnockoutObservable<string>;
            approvalId : KnockoutObservable<string>;
            employeeId : KnockoutObservable<string>;
            historyId : KnockoutObservable<string>;
            applicationType : KnockoutObservable<number>;
            startDate :  KnockoutObservable<string>;
            endDate : KnockoutObservable<string>;
            branchId : KnockoutObservable<string>;
            anyItemApplicationId : KnockoutObservable<string>;
            confirmationRootType : KnockoutObservable<number>;
            employmentRootAtr : KnockoutObservable<number>;
            beforeApprovers : KnockoutObservableArray<ApprovalPhaseImport>;
            afterApprovers : KnockoutObservableArray<ApprovalPhaseOutput>;
            errorFlag : KnockoutObservable<number>;
            constructor(workplaceId : string,approvalId : string,
                        employeeId : string,historyId : string,
                        applicationType : number,startDate : string,
                        endDate: string,branchId: string,
                        anyItemApplicationId : string,confirmationRootType : number,employmentRootAtr :number,
                        beforeApprovers : Array<ApprovalPhaseImport>,afterApprovers : Array<ApprovalPhaseOutput>,
                        errorFlag : number){
                this.workplaceId = ko.observable(workplaceId);
                this.approvalId = ko.observable(approvalId);
                this.employeeId  = ko.observable(employeeId);
                this.historyId = ko.observable(historyId); 
                this.applicationType = ko.observable(applicationType);
                this.startDate = ko.observable(startDate);
                this.endDate = ko.observable(endDate);
                this.branchId = ko.observable(branchId);
                this.anyItemApplicationId = ko.observable(anyItemApplicationId);
                this.confirmationRootType = ko.observable(confirmationRootType);
                this.employmentRootAtr = ko.observable(employmentRootAtr);
                this.beforeApprovers = ko.observableArray(beforeApprovers);
                this.afterApprovers = ko.observableArray(afterApprovers);
                this.errorFlag = ko.observable(errorFlag);
            }
        }//end class ApprovalRootOutput
        
        //class ApprovalPhaseOutput
        export class ApprovalPhaseOutput{
            branchId : KnockoutObservable<string>;
            approvalPhaseId : KnockoutObservable<string>;
            approvalForm : KnockoutObservable<number>;
            browsingPhase : KnockoutObservable<number>;
            orderNumber : KnockoutObservable<number>;
            approvers :  KnockoutObservableArray<ApproverInfo>;
            constructor(branchId : string,approvalPhaseId : string,
                        approvalForm : number,browsingPhase : number,
                        orderNumber : number,approvers : Array<ApproverInfo>){
                this.branchId = ko.observable(branchId);
                this.approvalPhaseId = ko.observable(approvalPhaseId);
                this.approvalForm  = ko.observable(approvalForm);
                this.browsingPhase = ko.observable(browsingPhase); 
                this.orderNumber = ko.observable(orderNumber);
                this.approvers = ko.observableArray(approvers);
                
            }
        }//end class ApprovalPhaseOutput
        
        //class ApproverInfo
        export class ApproverInfo{
            sid : KnockoutObservable<string>;
            approvalPhaseId : KnockoutObservable<string>;
            isConfirmPerson : KnockoutObservable<boolean>;
            orderNumber : KnockoutObservable<number>;
            name : KnockoutObservable<string>;
            constructor(sid : string,approvalPhaseId : string,
                        isConfirmPerson : boolean,orderNumber : number,name : string){
                this.sid = ko.observable(sid);
                this.approvalPhaseId = ko.observable(approvalPhaseId);
                this.isConfirmPerson  = ko.observable(isConfirmPerson);
                this.orderNumber = ko.observable(orderNumber); 
                this.name = ko.observable(name); 
                
            }
        }//end class ApproverInfo
        
        //class ApprovalPhaseImport
        export class ApprovalPhaseImport{
            branchId : KnockoutObservable<string>;
            approvalPhaseId : KnockoutObservable<string>;
            approvalForm : KnockoutObservable<number>;
            browsingPhase : KnockoutObservable<number>;
            orderNumber : KnockoutObservable<number>;
            approverDtos :  KnockoutObservableArray<ApproverImport>;
            constructor(branchId : string,approvalPhaseId : string,
                        approvalForm : number,browsingPhase : number,
                        orderNumber : number,approverDtos : Array<ApproverImport>){
                this.branchId = ko.observable(branchId);
                this.approvalPhaseId = ko.observable(approvalPhaseId);
                this.approvalForm  = ko.observable(approvalForm);
                this.browsingPhase = ko.observable(browsingPhase); 
                this.orderNumber = ko.observable(orderNumber);
                this.approverDtos = ko.observableArray(approverDtos);
                
            }
            
        }//end class ApprovalPhaseImport
        
        //class ApproverImport
        export class ApproverImport{
            approvalPhaseId : KnockoutObservable<string>;
            approverId : KnockoutObservable<string>;
            jobTitleId : KnockoutObservable<string>;
            employeeId : KnockoutObservable<string>;
            orderNumber : KnockoutObservable<number>;
            approvalAtr : KnockoutObservable<number>;
            confirmPerson : KnockoutObservable<number>;
            constructor(
                        approvalPhaseId : string,
                        approverId : string,jobTitleId : string,
                        employeeId : string,orderNumber : number,
                        approvalAtr : number,confirmPerson : number){
                this.approvalPhaseId = ko.observable(approvalPhaseId);
                this.approverId  = ko.observable(approverId);
                this.jobTitleId = ko.observable(jobTitleId); 
                this.employeeId = ko.observable(employeeId);
                this.orderNumber = ko.observable(orderNumber);
                this.approvalAtr = ko.observable(approvalAtr); 
                this.confirmPerson = ko.observable(confirmPerson);
                }
        }//end class ApproverImport
        
        //class ObjApprovalRootInput    
        export class ObjApprovalRootInput{
            sid : string;
            employmentRootAtr : number;
            appType : number;
            standardDate :  string;
            constructor (
                        sid : string,employmentRootAtr : number,
                        appType : number,standardDate : string){
                this.sid = sid; 
                this.employmentRootAtr =employmentRootAtr;
                this.appType = appType;
                this.standardDate = standardDate; 
            }
        }//end class ObjApprovalRootInput
        
        
        //class outputMessageDeadline
        export class OutputMessageDeadline{
            message : string;
            deadline : string;
            constructor(message : string,deadline : string){
                this.message = message;
                this.deadline = deadline;
            }
        }// end class outputMessageDeadline
        
        export class AppApprovalPhase {
            appID: string;
            phaseID: string;
            approvalForm: number;
            dispOrder: number;
            approvalATR: number;
            listFrame : Array<ApprovalFrame>;
            constructor(appID: string, phaseID: string, approvalForm: number, dispOrder: number, 
                    approvalATR: number,
                    listFrame : Array<ApprovalFrame>) {
                this.appID = appID;
                this.phaseID = phaseID;
                this.approvalForm = approvalForm;
                this.dispOrder = dispOrder;
                this.approvalATR = approvalATR;
                this.listFrame = listFrame;
            }
        }

        // class ApprovalFrame
        export class ApprovalFrame {
            frameID : string;
            dispOrder:number;
            listApproveAccepted: Array<ApproveAccepted>;
            constructor(frameID : string, dispOrder: number,listApproveAccepted: Array<ApproveAccepted>) {
                this.frameID = frameID;
                this.dispOrder = dispOrder;
                this.listApproveAccepted = listApproveAccepted;
                
            }
        }//end class frame  

        //class ApproveAccepted
        export class ApproveAccepted {
            appAccedtedID : string;
            approverSID: string;
            approvalATR: number;
            confirmATR: number;
            approvalDate: string;
            reason: string;
            representerSID: string;
            constructor(appAccedtedID : string,
                    approverSID: string,
                    approvalATR: number,
                    confirmATR: number,
                    approvalDate: string,
                    reason: string,
                    representerSID: string){
                this.appAccedtedID = appAccedtedID;
                this.approverSID = approverSID;
                this.approvalATR = approvalATR;
                this.confirmATR = confirmATR;
                this.approvalDate = approvalDate;
                this.reason = reason;
                this.representerSID = representerSID;
            }
        }
        
        export class CommonProcess {
            public static checkWorkTypeWorkTime(workTypeCD: string, workTimeCD: string, itemID: string): boolean{
                let itemTarget = "#"+itemID;
                let workTypeCDFlg = nts.uk.util.isNullOrUndefined(workTypeCD)||nts.uk.util.isNullOrEmpty(workTypeCD);
                let workTimeCDFlg = nts.uk.util.isNullOrUndefined(workTimeCD)||nts.uk.util.isNullOrEmpty(workTimeCD);
                if(workTypeCDFlg){
                    $(itemTarget).ntsError('set', '勤務種類を選択ください');   
                    $(itemTarget).css("border","1px solid red");
                    return false;         
                }
                if(workTimeCDFlg){
                    $(itemTarget).ntsError('set', '就業時間を選択ください');   
                    $(itemTarget).css("border","1px solid red");
                    return false;    
                }        
                return true;
            }
            
            public static getComboBoxReason(selectID: string, listID: Array<string>, displaySet: boolean): string{
                if(!displaySet){
                    return "";    
                }
                if(nts.uk.util.isNullOrEmpty(selectID)){
                    return "";        
                }         
                let reasonValue = _.find(listID, o => { return o.reasonId == selectID; }).reasonName;
                if(nts.uk.util.isNullOrUndefined(reasonValue)){
                    return "";    
                }
                return reasonValue;
            }
            
            public static getTextAreaReason(reasonText: string, displaySet: boolean, enableSet: boolean): string{
                if(!displaySet){
                    return "";    
                }
                if(!enableSet){
                    return "";    
                }
                if(nts.uk.util.isNullOrEmpty(reasonText)){
                    return "";    
                }
                return reasonText;
            }
            
            public static checkAppReason(appReasonRequired: boolean, inputReasonDisp: boolean, detailReasonDisp: boolean, appReason: string): boolean {
                if(appReasonRequired == false) {
                    return true;
                }
                if(inputReasonDisp == false){
                    if(detailReasonDisp == false){
                        return true;
                    }
                }
                if(nts.uk.util.isNullOrEmpty(appReason)){
                    // throw new BusinessException("Msg_115");
                    return false;
                }
                return true;
        
            }
            public static checklenghtReason(reason :string,elementID : string) : boolean{
                if(nts.uk.text.countHalf(reason.replace(":","\n")) > 400){
                   nts.uk.ui.dialog.alertError({messageId : 'Msg_960'}).then(function(){nts.uk.ui.block.clear();});
                   $(elementID).focus();
                   return false;
                }
               return true;
            }
            
            public static displayMailDeleteRs(data: ProcessResult): void {
                let autoSuccessMail = "", autoFailMail = "";
                data.autoSuccessMail.forEach((value, index) => { 
                    autoSuccessMail += value;
                    if(index != data.autoSuccessMail.length-1){
                        autoSuccessMail += ",";        
                    }     
                });
                data.autoFailMail.forEach((value, index) => { 
                    autoFailMail += value;
                    if(index != data.autoFailMail.length-1){
                        autoFailMail += ",";        
                    }     
                });
                if(!nts.uk.util.isNullOrEmpty(autoSuccessMail)&&!nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [autoSuccessMail] }).then(() => {
                        nts.uk.ui.dialog.info({ messageId: 'Msg_768', messageParams: [autoFailMail] }).then(() => {
                            this.callCMM045();
                        });
                    });        
                } else if(!nts.uk.util.isNullOrEmpty(autoSuccessMail)&&nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [autoSuccessMail] }).then(() => {
                        this.callCMM045();
                    });    
                } else if(nts.uk.util.isNullOrEmpty(autoSuccessMail)&&!nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_768', messageParams: [autoFailMail] }).then(() => {
                        this.callCMM045();
                    });    
                } else {
                    this.callCMM045();       
                }
            }
            
            public static displayMailResult(data: ProcessResult): void {
                let autoSuccessMail = "", autoFailMail = "";
                data.autoSuccessMail.forEach((value, index) => { 
                    autoSuccessMail += value;
                    if(index != data.autoSuccessMail.length-1){
                        autoSuccessMail += ",";        
                    }     
                });
                data.autoFailMail.forEach((value, index) => { 
                    autoFailMail += value;
                    if(index != data.autoFailMail.length-1){
                        autoFailMail += ",";        
                    }     
                });
                if(!nts.uk.util.isNullOrEmpty(autoSuccessMail)&&!nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [autoSuccessMail] }).then(() => {
                        nts.uk.ui.dialog.info({ messageId: 'Msg_768', messageParams: [autoFailMail] }).then(() => {
                            location.reload();
                        });
                    });        
                } else if(!nts.uk.util.isNullOrEmpty(autoSuccessMail)&&nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [autoSuccessMail] }).then(() => {
                        location.reload();
                    });    
                } else if(nts.uk.util.isNullOrEmpty(autoSuccessMail)&&!nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_768', messageParams: [autoFailMail] }).then(() => {
                        location.reload();
                    });    
                } else {
                    location.reload();        
                }
            }
            
            public static displayMailResultKAF000(data: ProcessResult): void {
                let autoSuccessMail = "", autoFailMail = "";
                data.autoSuccessMail.forEach((value, index) => { 
                    autoSuccessMail += value;
                    if(index != data.autoSuccessMail.length-1){
                        autoSuccessMail += ",";        
                    }     
                });
                data.autoFailMail.forEach((value, index) => { 
                    autoFailMail += value;
                    if(index != data.autoFailMail.length-1){
                        autoFailMail += ",";        
                    }     
                });
                if(!nts.uk.util.isNullOrEmpty(autoSuccessMail)&&!nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [autoSuccessMail] }).then(() => {
                        nts.uk.ui.dialog.info({ messageId: 'Msg_768', messageParams: [autoFailMail] });
                    });        
                } else if(!nts.uk.util.isNullOrEmpty(autoSuccessMail)&&nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [autoSuccessMail] });
                } else if(nts.uk.util.isNullOrEmpty(autoSuccessMail)&&!nts.uk.util.isNullOrEmpty(autoFailMail)){
                    nts.uk.ui.dialog.info({ messageId: 'Msg_768', messageParams: [autoFailMail] });
                }
            }
            
            public static openDialogKDL030(data: string, self: any, appID: string): void {
                let command = {appID: data};
                setShared("KDL030_PARAM", command);
                nts.uk.ui.windows.sub.modal("/view/kdl/030/a/index.xhtml").onClosed(() => {
                    location.reload();
                });    
            }
            public static callCMM045(){
                nts.uk.characteristics.restore("AppListExtractCondition").done((obj) => {
                    let paramUrl = 0;
                    if (obj !== undefined && obj !== null){
                        paramUrl = obj.appListAtr;
                    }
                    nts.uk.localStorage.setItem('UKProgramParam', 'a=' + paramUrl);
                    nts.uk.request.jump("/view/cmm/045/a/index.xhtml");
                });
            }
        }
        
        interface ProcessResult {
            isProcessDone: boolean,
            isAutoSendMail: boolean,
            autoSuccessMail: Array<string>,
            autoFailMail: Array<string>,
            appID: string    
        }
    }
}