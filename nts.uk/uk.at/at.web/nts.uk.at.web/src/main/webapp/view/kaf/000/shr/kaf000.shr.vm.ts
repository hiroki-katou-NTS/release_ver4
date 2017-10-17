module nts.uk.at.view.kaf000.shr{
    
    export module model {
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
        
    }
}