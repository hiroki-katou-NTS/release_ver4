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
        export class Template {
           public static TEMPKAF000B = `
            <div id="functions-area">
                <div>
                    
                    <div style="display: inline-block;" data-bind="if: displayGoback">
                        <a class="goback link-button" data-bind="click: callCMM045A">一覧へ戻る</a>
                    </div>
                    
                    <button class="kaf000-function-btn" data-bind="click: btnBefore, enable: enableBefore">
                        <i class="icon icon-button-arrow-left"></i>
                    </button>
                    
                    <button class="kaf000-function-btn" data-bind="click: btnAfter, enable: enableAfter">
                        <i class="icon icon-button-arrow-right"></i>
                    </button>
                    <div style="display: inline-block;">
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayApprovalButton">
                            <button class="proceed kaf000-function-btn" data-bind="click: btnApprove,                enable: enableApprovalButton() &amp;&amp; errorEmpty()               ">承認</button>
                        </div>
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayApprovalLabel">
                            <div class="comment">
                                <label>【承認】</label>
                            </div>
                        </div>
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayDenyButton">
                            <button class="kaf000-function-btn" data-bind="click: btnDeny,            enable: enableDenyButton() &amp;&amp; errorEmpty()           ">否認</button>
                        </div>
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayDenyLabel">
                            <div class="comment">
                                <label>【否認】</label>
                            </div>
                        </div>
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayReleaseButton">
                            <button class="kaf000-function-btn" data-bind="click: btnRelease,            enable: enableReleaseButton()           ">解除</button>
                        </div>
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayRemandButton">
                            <button class="kaf000-function-btn" data-bind="click: btnRemand,            enable: enableRemandButton()              ">差し戻し</button>
                        </div>
                    </div>
                    <div class="kaf000-function-div-update">
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayUpdateButton">
                            <button id="updateKAF000" class="proceed kaf000-function-btn" data-bind="click: update, enable : enableUpdateButton()">登録</button>

                        </div>
                        
                        <div class="controlButtonCell_kaf000" style="visibility: hidden;">
                            <button class="kaf000-function-btn">印刷</button>
                        </div>
                        
                        <div class="controlButtonCell_kaf000" style="visibility: hidden;">
                            <button class="kaf000-function-btn" data-bind="click: btnReferences">実績参照</button>
                        </div>
                        
                        <div class="controlButtonCell_kaf000">
                            <button class="kaf000-function-btn" data-bind="click: btnSendEmail">メール送信</button>
                        </div>
                    </div>
                    <div class="kaf000-function-div-delete">
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayDeleteButton">
                            <button class="danger kaf000-function-btn" data-bind="click: btnDelete, enable : enableDeleteButton()">削除</button>
                        </div>
                    </div>
                    <div class="kaf000-function-div-cancel" data-bind="if: false">
                        
                        <div class="controlButtonCell_kaf000" data-bind="if: displayCancelButton">
                            <button class="danger kaf000-function-btn" data-bind="click: btnCancel, enable : enableCancelButton()">取消</button>
                        </div>
                    </div>
                    <br />
                </div>
            </div>
            <div id="contents-area">
            <div id="appNew">
                
                <div id="message-area" data-bind="visible : messageArea">
                    <table>
                        <tr>
                            
                            
                            <td class="message-td" data-bind="visible : reasonOutputMessFull() != null">
                                <div data-bind="text: reasonOutputMessFull()"></div>
                            </td>
                        </tr>
                        <tr>
                            
                            
                            <td class="message-td" data-bind="visible:  reasonOutputMessDealineFull() != null ">
                                <div data-bind="text: reasonOutputMessDealineFull()"></div>
                            </td>
                        </tr>
                    </table>
                </div>

                <div data-bind="if: displayReturnReasonPanel">
                    
                    <div id="message-area" style="margin-top: 16px;">
                        
                        <div class="message-td" id="returnReason"></div> 
                    </div>
                </div>

                <div data-bind="if: displayApprovalReason">
                    
                    <div style="margin-top: 16px; float: left; margin-right: 47px">
                        <div data-bind="ntsFormLabel: {constraint: 'ApproverReason', enable: true }">承認コメント</div>
                    </div>
                    
                    <div style="margin-top: 16px;">
                        <textarea style="width: 628px; height: 50px" data-bind="ntsMultilineEditor: { value: reasonToApprover,                           constraint: 'ApproverReason',                          enable: enableApprovalReason(),                          name:'#[KAF000_28]' }"></textarea>

                    </div>
                </div><!-- ko if: appType() == 7 -->
            <div data-bind="with: cm">
            <div id="cm-content">
                <div class="cm-content-left">
                    <div class="table cm-table" data-bind="style: { display: screenMode() === 1 ? '' : 'none' }">
                        <div class="cell cm-column1">
                            <div data-bind="ntsFormLabel: {}">申請者</div>
                        </div>
                        <div class="cell cm-column2-kaf002">
                            <span class="label" data-bind="text: employeeName"></span>
                        </div>
                    </div>
                    <div class="table cm-table" data-bind="style: { display: screenMode() !== 1 ? '' : 'none' }">
                        <div class="cell cm-column1">
                            <div data-bind="ntsFormLabel: {}">申請者</div>
                        </div>
                        <div class="cell cm-column2-kaf002">
                            <span class="label" data-bind="text: textC3_2"></span>
                        </div>
                    </div>
                    <div class="table cm-table" data-bind="style: { display: screenMode() === 1 ? '' : 'none' }">
                        <div class="cell cm-column1">
                            <div data-bind="ntsFormLabel: { required: true }">日付</div>
                        </div>
                        <div class="cell cm-column2-kaf002">
                            <div class="cm-time-editor" id="appDate" data-bind="ntsDatePicker: {            name: '#[KAF002_9]',            value: application().appDate,            dateFormat: 'YYYY/MM/DD',            valueFormat: 'YYYY/MM/DD',            required: true }"></div>
                        </div>
                    </div>
                    <div class="table cm-table" data-bind="style: { display: screenMode() !== 1 ? '' : 'none' }">
                        <div class="cell cm-column1">
                            <div data-bind="ntsFormLabel: { required: true }">日付</div>
                        </div>
                        <div class="cell cm-middle">
                            <span class="label" data-bind="text: application().appDate() + ' ( 入力日 : ' + application().inputDate() + ' )'"></span>  
                        </div>
                    </div>
                    <div class="cm-title-div" data-bind="text: topComment().text,                style: {                color: topComment().color,                'font-weight': topComment().fontWeight() ? 'bold' : 'normal'                }"></div>
                    <div>
                        <div data-bind="if: stampRequestMode() == 0">
                            <div data-bind="with: m1"><div>
    <div class="table m1-table">
        <div class="cell m1-column1">
            <div data-bind="ntsFormLabel: {}">種類</div>
        </div>
        <div class="cell m1-column2">
            <div class="cf m1-switch" data-bind="ntsSwitchButton: {       name: '#[KAF002_16]',       options: stampAtrList,       optionsValue: 'code',       optionsText: 'name',       value: stampAtr,       enable: screenMode()==1 }"></div>
        </div>
    </div>
    <div class="table m1-table" data-bind="let: { screenEditable: editable }">
        <div class="cell m1-column1">
            <div data-bind="ntsFormLabel: {}">申請時間</div>
        </div>
        <div class="cell m1-column2">
            <div data-bind="foreach: appStampList">
                <div data-bind="if: ($parent.stampAtr()==1) &amp;&amp; ((!($index()-2&gt;0))||(($index()-2&gt;0)&amp;&amp;(!$parent.extendsModeDisplay())))">
                    <div class="panel panel-frame m1-panel">
                        <div class="table">
                            <div class="cell m1-column3">
                                <span class="label m1-label" data-bind="text: nts.uk.resource.getText('KAF002_59',[$index()+1])"></span>
                            </div>
                            <div class="cell">
                                <div class="m1-inline-div" data-bind="with: startTime">
                                    <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m1-time-editor m1-start-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',              constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />   
                                </div>
                                <div class="m1-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                    <div class="m1-inline-div" data-bind="with: startLocation">
                                        <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('start', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                        <span class="label m1-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m1DisableEdit: !checked() }"></span>
                                    </div>
                                </div>                  
                            </div>
                        </div>
                        <div class="table">
                            <div class="cell m1-column3">
                                <span class="label m1-label" data-bind="text: nts.uk.resource.getText('KAF002_60',[$index()+1])"></span>
                            </div>
                            <div class="cell">
                                <div class="m1-inline-div" data-bind="with: endTime">
                                    <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m1-time-editor m1-end-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />    
                                </div>  
                                <div class="m1-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                    <div class="m1-inline-div" data-bind="with: endLocation">
                                        <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable}"></div>
                                        <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('end', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                        <span class="label m1-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m1DisableEdit: !checked() }"></span> 
                                    </div>
                                </div>          
                            </div>
                        </div>
                        <div class="table">
                            <div class="cf m1-switch" data-bind="ntsSwitchButton: {             name: '#[KAF002_24]',             options: $parent.stampGoOutAtrList,             optionsValue: 'code',             optionsText: 'name',             value: stampGoOutAtr,             enable: screenEditable }"></div> 
                        </div>
                    </div>
                </div>
                <div data-bind="if: $parent.stampAtr() == 2">
                    <div class="panel panel-frame m1-panel">
                        <div class="table">
                            <div class="cell m1-column3">
                                <span class="label m1-label" data-bind="text: nts.uk.resource.getText('KAF002_57',[$index()+1])"></span>
                            </div>
                            <div class="cell">
                                <div class="m1-inline-div" data-bind="with: startTime">
                                    <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m1-time-editor m1-start-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',              constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />   
                                </div>                  
                            </div>
                        </div>
                        <div class="table">
                            <div class="cell m1-column3">
                                <span class="label m1-label" data-bind="text: nts.uk.resource.getText('KAF002_58',[$index()+1])"></span>
                            </div>
                            <div class="cell">
                                <div class="m1-inline-div" data-bind="with: endTime">
                                    <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m1-time-editor m1-end-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />    
                                </div>                      
                            </div>
                        </div>
                    </div>
                </div>
                <div data-bind="if: $parent.stampAtr() == 3">
                    <div class="panel panel-frame m1-panel">
                        <div class="table">
                            <div class="cell m1-column3">
                                <span class="label m1-label" data-bind="text: nts.uk.resource.getText('KAF002_57',[$index()+1])"></span>
                            </div>
                            <div class="cell">
                                <div class="m1-inline-div" data-bind="with: startTime">
                                    <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m1-time-editor m1-start-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',              constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />   
                                </div>                  
                            </div>
                        </div>
                        <div class="table">
                            <div class="cell m1-column3">
                                <span class="label m1-label" data-bind="text: nts.uk.resource.getText('KAF002_58',[$index()+1])"></span>
                            </div>
                            <div class="cell">
                                <div class="m1-inline-div" data-bind="with: endTime">
                                    <div class="m1-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m1-time-editor m1-end-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />    
                                </div>                          
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <a class="hyperlink" href="#" data-bind="text: displayAllLabel, click: extendsModeEvent,                 style: { display: extendsModeDisplay() ? '' : 'none' }"></a>
            </div>
        </div>
    </div>
</div>
                            </div>
                        </div>      
                        <div data-bind="if: stampRequestMode() == 1">
                            <div data-bind="with: m2"><div>
    <div class="table">
        <div class="cell m2-column1">
            <div data-bind="ntsFormLabel: {}">申請時間</div>
        </div>
        <div class="cell m2-column2" data-bind="let: { screenEditable: editable }">
            <div data-bind="foreach: appStampList">
                <div data-bind="if: (!($index()-1&gt;0))||(($index()-1&gt;0)&amp;&amp;(!$parent.extendsModeDisplay()))">
                    <div class="panel panel-frame m2-panel">
                        <div class="table">
                            <div class="cell m2-column3">
                                <div class="cell m2-column3" data-bind="if: !($index()&gt;1)">
                                    <span class="label m2-label" data-bind="text: nts.uk.resource.getText('KAF002_61',[$index()+1])"></span>
                                </div>
                                <div class="cell m2-column3" data-bind="if: $index()&gt;1">
                                    <span class="label m2-label" data-bind="text: nts.uk.resource.getText('KAF002_63',[$index()-1])"></span>
                                </div>
                            </div>
                            <div class="cell">
                                <div class="m2-inline-div" data-bind="with: startTime">
                                    <div class="m2-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m2-time-editor m2-start-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',              constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />   
                                </div>
                                <div class="m2-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                    <div class="m2-inline-div" data-bind="with: startLocation">
                                        <div class="m2-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('start', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                        <span class="label m2-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m2DisableEdit: !checked() }"></span>
                                    </div>
                                </div>                  
                            </div>
                        </div>
                        <div class="table">
                            <div class="cell m2-column3">
                                <div class="cell" data-bind="if: !($index()&gt;1)">
                                    <span class="label m2-label" data-bind="text: nts.uk.resource.getText('KAF002_62',[$index()+1])"></span>
                                </div>
                                <div class="cell" data-bind="if: $index()&gt;1">
                                    <span class="label m2-label" data-bind="text: nts.uk.resource.getText('KAF002_64',[$index()-1])"></span>
                                </div>
                            </div>
                            <div class="cell">
                                <div class="m2-inline-div" data-bind="with: endTime">
                                    <div class="m2-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                    <input class="m2-time-editor m2-end-input" data-bind="ntsTimeWithDayEditor: {               name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',               value: value,              enable: checked()&amp;&amp;screenEditable() }" />    
                                </div>  
                                <div class="m2-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                    <div class="m2-inline-div" data-bind="with: endLocation">
                                        <div class="m2-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable}"></div>
                                        <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('end', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                        <span class="label m2-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m2DisableEdit: !checked() }"></span> 
                                    </div>
                                </div>              
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <a class="hyperlink" href="#" data-bind="text: displayAllLabel, click: extendsModeEvent,                 style: { display: extendsModeDisplay() ? '' : 'none' }"></a>
            </div>
        </div>
    </div>
</div>
                            </div>
                        </div>  
                        <div data-bind="if: stampRequestMode() == 2">
                            <div data-bind="with: m3"><div class="table">
    <div class="cell m3-column1">
        <div data-bind="ntsFormLabel: {}">取消対象</div>
    </div>
    <div class="cell" id="m3-div" data-bind="let: { screenEditable: editable }">
        <div id="m3-backgroud"></div>
        <div class="table" style="background-color: #CFF1A5;">
            <div class="cell" style="width: 120px;"></div>
            <div class="cell" style="width: 430px; padding-left: 20px;">
                <span class="label m3-label">実績の打刻を削除</span>
                <button data-bind="ntsHelpButton: {image: '/view/kaf/002/m3/m3-msg.PNG', position: 'right top', enable: screenEditable }">?</button>
            </div>
        </div>
        <div class="m3-table-div">
            <table id="m3-table">
                <tbody data-bind="foreach: appStampList">
                    <tr>
                        <td>
                            <div>
                                <span class="label m3-label" data-bind="text: label"></span>
                            </div>
                        </td>
                        <td>
                            <span class="label m3-label m3-time-detail-1" data-bind="text: startTime"></span>
                            <span class="label m3-label" style="width: 10px;">～</span>
                            <span class="label m3-label m3-time-detail-2" data-bind="text: endTime"></span>
                            <div class="cf m3-switch" data-bind="ntsSwitchButton: {         name: '#[KAF002_28]',         options: ko.observableArray([                   { code: 1, name: '取消する' },                   { code: 0, name: '取消しない' }               ]),         optionsValue: 'code',         optionsText: 'name',         value: cancelAtr,         enable: screenEditable }"></div>
                        </td>
                    </tr>   
                </tbody>
            </table>    
        </div>
    </div>
</div>
                            </div>
                        </div>  
                        <div data-bind="if: stampRequestMode() == 3">
                            <div data-bind="with: m4"><div class="table m4-table" data-bind="let: { screenEditable: editable }">
    <div class="cell m4-column1">
        <div data-bind="ntsFormLabel: { required: true }">申請時間</div>
    </div>
    <div class="cell m4-column2">
        <span class="m4-combo-box" data-bind="ntsComboBox: {     name: '#[KAF002_19]',     options: stampCombinationList,     optionsValue: 'value',     value: appStamp().stampCombinationAtr,     optionsText: 'name',     enable: screenEditable,     columns: [      { prop: 'name', length: 10 },     ]}"></span>  
        <div>
            <input class="m4-time-editor" data-bind="ntsTimeEditor: {     name: '#[KAF002_20]',     value: appStamp().appTime,      inputFormat: 'time',     mode: 'time',     constraint:'TimeDayClock',     required: true,     enable: screenEditable }" />
        </div>
    </div>
</div>
                            </div>
                        </div>  
                        <div data-bind="if: stampRequestMode() == 4">
                            <div data-bind="with: m5"><div>
    <div class="table m5-table">
        <div class="cell m5-column1">
            <div data-bind="ntsFormLabel: {}">種類</div>
        </div>
        <div class="cell m5-column2">
            <div class="cf m5-switch" data-bind="ntsSwitchButton: {       name: '#[KAF002_16]',       options: stampAtrList,       optionsValue: 'code',       optionsText: 'name',       value: stampAtr,       enable: screenMode()==1 }"></div>
        </div>
    </div>
    <div class="table m5-table">
        <div class="cell m5-column1">
            <div data-bind="ntsFormLabel: {}">申請時間</div>
        </div>
        <div class="cell m5-column2" data-bind="let: { screenEditable: editable }">
            <div data-bind="if: stampAtr() == 0">
                <div id="m5-div-a">
                    <div id="m5-backgroud-a"></div>
                    <table id="m5-table-a">
                        <thead>
                            <tr>
                                <td></td>
                                <td>
                                    <span class="label">開始</span>
                                </td>
                                <td>
                                    <span class="label">終了</span>
                                </td>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: appStampList">
                            <tr>
                                <td>
                                    <div class="cell" data-bind="if: !($index()&gt;1)">
                                        <span class="label" data-bind="text: nts.uk.resource.getText('KAF002_65',[$index()+1])"></span>
                                    </div>
                                    <div class="cell" data-bind="if: $index()&gt;1">
                                        <span class="label" data-bind="text: nts.uk.resource.getText('KAF002_66',[$index()-1])"></span>
                                    </div>
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: startTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-start-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />   
                                    </div>
                                    <div class="m5-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                        <div class="m5-inline-div" data-bind="with: startLocation">
                                            <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                            <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('start', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                            <span class="label m5-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m5DisableEdit: !checked() }"></span>
                                        </div>
                                    </div>  
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: endTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-end-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',                constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />    
                                    </div>  
                                    <div class="m5-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                        <div class="m5-inline-div" data-bind="with: endLocation">
                                            <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable}"></div>
                                            <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('end', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                            <span class="label m5-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m5DisableEdit: !checked() }"></span> 
                                        </div>
                                    </div>
                                </td>
                            </tr>   
                        </tbody>
                    </table>    
                </div>
            </div>
            <div data-bind="if: stampAtr() == 1">
                <div id="m5-div-b">
                    <div id="m5-backgroud-b"></div> 
                    <table id="m5-table-b">
                        <thead>
                            <tr>
                                <td></td>
                                <td>
                                    <span class="label">外出種類</span>
                                </td>
                                <td>
                                    <span class="label">開始</span>
                                </td>
                                <td>
                                    <span class="label">終了</span>
                                </td>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: appStampList">
                            <tr>
                                <td>
                                    <span class="label" data-bind="text: nts.uk.resource.getText('KAF002_67',[$index()+1])"></span> 
                                </td>
                                <td>
                                    <span class="m5-combo-box" data-bind="ntsComboBox: {             name: '#[KAF002_24]',             options: $parent.stampGoOutAtrList,             optionsValue: 'code',             value: stampGoOutAtr,             optionsText: 'name',             enable: screenEditable,             columns: [              { prop: 'name', length: 3 },             ]}"></span>    
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: startTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-start-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />   
                                    </div>
                                    <div class="m5-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                        <div class="m5-inline-div" data-bind="with: startLocation">
                                            <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                            <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('start', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                            <span class="label m5-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m5DisableEdit: !checked() }"></span>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: endTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-end-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',                constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />    
                                    </div>  
                                    <div class="m5-inline-div" data-bind="if: $parent.stampPlaceDisplay() == 1">
                                        <div class="m5-inline-div" data-bind="with: endLocation">
                                            <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable}"></div>
                                            <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('end', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                            <span class="label m5-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m5DisableEdit: !checked() }"></span> 
                                        </div>
                                    </div>
                                </td>
                            </tr>   
                        </tbody>
                    </table>
                </div>
            </div>
            <div data-bind="if: stampAtr() == 2">
                <div id="m5-div-c">
                    <div id="m5-backgroud-c"></div>
                    <table id="m5-table-c">
                        <thead>
                            <tr>
                                <td></td>
                                <td>
                                    <span class="label">開始</span>
                                </td>
                                <td>
                                    <span class="label">終了</span>
                                </td>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: appStampList">
                            <tr>
                                <td>
                                    <span class="label" data-bind="text: nts.uk.resource.getText('KAF002_68',[$index()+1])"></span> 
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: startTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-start-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />   
                                    </div>
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: endTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-end-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',                constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />    
                                    </div>
                                </td>
                            </tr>   
                        </tbody>
                    </table>    
                </div>
            </div>
            <div data-bind="if: stampAtr() == 3">
                <div id="m5-div-d">
                    <div id="m5-backgroud-d"></div>
                    <table id="m5-table-d">
                        <thead>
                            <tr>
                                <td></td>
                                <td>
                                    <span class="label">開始</span>
                                </td>
                                <td>
                                    <span class="label">終了</span>
                                </td>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: appStampList">
                            <tr>
                                <td>
                                    <span class="label" data-bind="text: nts.uk.resource.getText('KAF002_69',[$index()+1])"></span> 
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: startTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-start-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />   
                                    </div>
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: endTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-end-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',                constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />    
                                    </div>
                                </td>
                            </tr>   
                        </tbody>
                    </table>    
                </div>
            </div>
            <div data-bind="if: stampAtr() == 4">
                <div id="m5-div-e" data-bind="style: { width: stampPlaceDisplay() == 1 ? '894px' : '673px' }">
                    <div id="m5-backgroud-e"></div>
                    <table id="m5-table-e">
                        <thead>
                            <tr>
                                <td></td>
                                <td>
                                    <span class="label">開始</span>
                                </td>
                                <td>
                                    <span class="label">終了</span>
                                </td>
                                <td>
                                    <span class="label">応援カード</span>
                                </td>
                                <td data-bind="style: { display: stampPlaceDisplay() == 1 ? '' : 'none' }">
                                    <span class="label">場所</span>
                                </td>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: appStampList">
                            <tr>
                                <td>
                                    <span class="label" data-bind="text: nts.uk.resource.getText('KAF002_70',[$index()+1])"></span> 
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: startTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-start-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',               constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />   
                                    </div>
                                </td>
                                <td>
                                    <div class="m5-inline-div" data-bind="with: endTime">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable }"></div>
                                        <input class="m5-time-editor m5-end-input" data-bind="ntsTimeWithDayEditor: {                name: '#[KAF002_20]',                constraint:'TimeWithDayAttr',                value: value,               enable: checked()&amp;&amp;screenEditable() }" />    
                                    </div>
                                </td>
                                <td>    
                                    <div class="m5-inline-div" data-bind="with: supportCard">
                                        <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable}"></div>
                                        <button data-bind="click: function(){ $parents[1].openSelectCardDialog() }, enable: checked()&amp;&amp;screenEditable()">応援カード選択</button>
                                        <span class="label m5-label limited-label" data-bind="name: '#[KAF002_25]', text: name, css: { m5DisableEdit: !checked() }"></span> 
                                    </div>
                                </td>
                                <td data-bind="style: { display: $parent.stampPlaceDisplay() == 1 ? '' : 'none' }">
                                    <div data-bind="style: { display: $parent.stampPlaceDisplay() == 1 ? 'inline-block' : 'none' }">
                                        <div class="m5-inline-div" data-bind="with: supportLocation">
                                            <div class="m5-checkBox" data-bind="ntsCheckBox: { checked: checked, enable: enable}"></div>
                                            <button data-bind="click: function(){ $parents[1].openSelectLocationDialog('support', $index()) }, enable: checked()&amp;&amp;screenEditable()">場所選択</button>
                                            <span class="label m5-label limited-label" data-bind="name: '#[KAF002_26]', text: name, css: { m5DisableEdit: !checked() }"></span> 
                                        </div>
                                    </div>
                                </td>
                            </tr>   
                        </tbody>
                    </table>    
                </div>
            </div>      
        </div>
    </div>
</div>
                            </div>
                        </div>  
                    </div>
                    <div class="cm-title-div" data-bind="text: botComment().text,                style: {                color: botComment().color,                'font-weight': botComment().fontWeight() ? 'bold' : 'normal'                }"></div>
                    <div class="table cm-table" data-bind="if: inputReasonsDisp() != 0">
                        <div class="cell cm-column1">
                            <div data-bind="ntsFormLabel: {}">定型理由</div>    
                        </div>
                        <div class="cell cm-column2-kaf002">
                            <span class="cm-combo-box" data-bind="ntsComboBox: {          name: '#[KAF002_10]',          options: inputReasons,          optionsValue: 'id',          value: currentReason,          optionsText: 'content',          enable: editable,          columns: [           { prop: 'content', length: 19 },          ]}"></span> 
                        </div>
                    </div>
                    <div class="table cm-table" data-bind="if: detailReasonDisp() != 0">
                        <div class="cell cm-column1">
                            <div data-bind="ntsFormLabel: {}">申請理由</div>    
                        </div>
                        <div class="cell cm-column2-kaf002">
                            <span>
                                <textarea id="appReason" class="cm-memo" data-bind="ntsMultilineEditor: {           name: '#[KAF002_11]',          value: application().contentReason,          enable: editable }"></textarea>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="cm-content-right">
                    <div class="panel panel-frame panel-gray-bg cm-panel" data-bind="style: { display: resultDisplay() == 1 ? '' : 'none' }">実績の内容</div>        
                </div>
            </div>
            </div><!-- /ko --><!-- ko if: appType() == 9 -->
    
        <div style="display: inline-block;">
            <div class="valign-center control-group">
                <div class="text_form_label" data-bind="ntsFormLabel: {}">申請者</div>
                <label data-bind="text: applicantName"></label>
            </div>
            <div class="valign-center control-group">
                <div class="text_form_label" data-bind="ntsFormLabel: {}">事前事後区分</div>
                <label data-bind="text: postAtr() == 0 ? '事前' : '事後'">事前</label>
            </div>
            <div class="valign-center control-group" data-bind="visible: showScreen() == 'F'">
                <div class="text_form_label" data-bind="ntsFormLabel: {}">実績の取消</div>
                <label data-bind="text: actualCancelType"></label>
            </div>
            <div class="valign-center control-group">
                <div class="text_form_label" data-bind="ntsFormLabel: { required: true}">日付</div>
                <label data-bind="text:date"></label>
            </div>
            
            <div class="valign-center control-group">
                <div data-bind="visible: showScreen() != 'F'" style="display: inline-block; vertical-align: top;">
                    <div class="text_form_label" data-bind="ntsFormLabel: { required: true}">遅刻/早退</div>
                </div>
                <div data-bind="visible: showScreen() == 'F'" style="display: inline-block; vertical-align: top;">
                    <div class="text_form_label" data-bind="ntsFormLabel: { required: true}">遅刻早退区分</div>
                </div>
                <div style="display: inline-block; "><!-- ko if: showScreen() == 'F' -->
                        <div class="valign-center control-group row 004e-div" style="margin-left: 5px;">
                            
                            <label class="text_form_label" data-bind="ntsFormLabel: {}">予定</label>
                            <label style="margin-left: 15px; ">08:30　 ～　12:00</label>
                            <label style="margin-left: 25px;">13:00　～　17:30</label>
                            <br /><br />                        
                            <label class="text_form_label" data-bind="ntsFormLabel: {}" style="margin-top: 10px">実績</label>
                            <label style="margin-left: 15px;">10:30　 ～　11:00</label>
                            <label style="margin-left: 25px; ">14:00　～　17:00</label>
                            <br />
                        </div><!-- /ko -->
                    <div class="valign-center control-group row4 004e-div" style="margin-top: 15px;">
                        <div class="time_div">
                            <div class="time_ckb" data-bind="ntsCheckBox: { checked: late1 }">遅刻</div>
                        <input data-bind="ntsTimeEditor: {value: lateTime1, inputFormat: 'time',mode: 'time', name:'#[KAF004_42]'}, visible: isVisibleTimeF" />
                        <label id="lblLateTime1" data-bind="ntsFormLabel: {text: txtlateTime1}; visible: isLblTimeF" style="margin-right: 35px">2:00</label>
                        </div>
                        <div class="time_div">
                        
                        <div class="time_ckb" data-bind="ntsCheckBox: { checked: early1 }">早退</div>
                        <input data-bind="ntsTimeEditor: {value: earlyTime1, inputFormat: 'time',mode: 'time', name:'#[KAF004_43]'}, visible: isVisibleTimeF" />
                        <label data-bind="ntsFormLabel: {text: txtearlyTime1}; visible: isLblTimeF" style="margin-right: 35px">1:00</label>
                        </div>
                    </div>
                    <div class="valign-center control-group row4 004e-div" style="margin-top: 20px;" data-bind="if: displayOrder() != 0">
                    <div class="time_div">
                        <div class="time_ckb" data-bind="ntsCheckBox: { checked: late2 }">遅刻２</div>
                        <input class="col1" data-bind="ntsTimeEditor: {value: lateTime2, inputFormat: 'time',mode: 'time', name:'#[KAF004_50]'}, visible: isVisibleTimeF" />
                        <label id="lblLateTime2" data-bind="ntsFormLabel: {text: txtlateTime2}; visible: isLblTimeF" style="margin-right: 35px">1:00</label>
                        </div>
                        <div class="time_div">
                        <div class="time_ckb" data-bind="ntsCheckBox: { checked: early2 }">早退２</div>
                        <input class="col2" data-bind="ntsTimeEditor: {value: earlyTime2, inputFormat: 'time',mode: 'time', name:'#[KAF004_51]'}, visible: isVisibleTimeF" />
                        <label data-bind="ntsFormLabel: {text: txtEarlyTime2}; visible: isLblTimeF" style="margin-right: 35px">0:30</label>
                    </div>
                    </div>
                </div>
            </div><!-- ko if: appCommonSetting().appTypeDiscreteSetting().typicalReasonDisplayFlg()!=0 -->
            <div class="valign-center control-group">
                <div class="text_form_label" data-bind="ntsFormLabel: {}">定型理由</div>
                <div id="combo-box" data-bind="ntsComboBox: {         name: '#[KAF004_17]',         dropDownAttachedToBody:true,         options: ListTypeReason,         optionsValue: 'reasonID',         visibleItemsCount: 5,         value: selectedCode,         optionsText: 'reasonTemp',         enable: true,         columns: [          { prop: 'reasonTemp', length: 15  },                  ]}"></div>
            </div><!-- /ko --><!-- ko if: showReasonText()  -->
            <div class="valign-center control-group">
                <div class="text_form_label" data-bind="ntsFormLabel: {}">申請理由</div>
                
                    <textarea id="appReason" style="height: 85px;" data-bind="ntsMultilineEditor: {         name: '#[KAF004_18]',         value: appreason,         option: {          resizeable: false,                         width: '610'                        },         enable:appCommonSetting().appTypeDiscreteSetting().displayReasonFlg()!=0         }"></textarea>
                
            </div><!-- /ko -->
        </div>      
        <div style="display: inline-block; vertical-align: top;">
            <div class="panel panel-gray-bg panel-frame004" style="height: 150px ; margin-left: 100px ;min-height: 150px">
                <label>実際の内容 :</label> <BR /> <BR />
                <label>日付 :</label> <BR /> <BR /> <label>勤務種類
                    :</label> <BR /> <label>就業時間帯 :</label> <BR /> <BR />
                <label>勤務時間 :</label>
            </div>
        </div><!-- /ko --><!-- ko if: appType() == 4 -->
        <div style="width: 1200px"><div data-bind="let: { $vm: $data }">
    
    <div class="left-contents-009">
        <div class="valign-center control-group" data-bind="visible: !employeeFlag()">
            <div class="lblTitleKAF009" data-bind="ntsFormLabel: {}">申請者</div>
            <label class="lblEmployeeName" data-bind="text : employeeName"></label>
        </div>
        <div class="cf valign-top control-group" data-bind="visible: employeeFlag()">
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">申請者</div>
            
            <div id="list-box" style="margin-left: 79px;" data-bind="ntsListBox: {        options: employeeList,        optionsValue: 'id',        optionsText: 'name',        multiple:false,        value: selectedEmplCodes,        enable: true,        rows: 3,        columns: [         { key: 'name',length: 11}        ]}">
            </div>
        </div>
        <div class="cf valign-center control-group" data-bind="visible: employeeFlag()">
            <div class="cm-column"></div>
            
            <label style="margin-left: 139px;" data-bind="text : totalEmployee"></label>
        </div>
        
        <div class="valign-center control-group " data-bind="visible: prePostDisp()">
            <div class="lblTitleKAF009" data-bind="ntsFormLabel:{required : true}">事前事後区分</div>
            <div data-bind="visible: screenModeNew" class="mode-style">
                <div id="pre_post" data-bind="ntsSwitchButton: {          options:ko.observableArray([                        { prePostCode: 0, prePostName: '事前' },                        { prePostCode: 1, prePostName: '事後' }                        ]),          optionsValue: 'prePostCode',          optionsText: 'prePostName',          value: prePostSelected,          enable: prePostEnable,          required : true,          name: '事前事後区分'}">
                </div>
            </div>
            <div data-bind="if: !screenModeNew()" class="mode-style">
                <div data-bind="if: prePostSelected() &lt; 2" class="mode-style">
                    <span data-bind="text: prePostSelected()==0 ? '事前' : '事後'"></span>
                </div>
            </div>
        </div>
        <div class="valign-center control-group">
            
            <div class="lblTitleKAF009" data-bind="ntsFormLabel:{required:true}">申請日付</div>
            <div data-bind="if: screenModeNew()" class="mode-style">
                <div id="inputdate" data-bind="ntsDatePicker: {          name: '#[KAF009_12]',          required:true,           value: appDate,           dateFormat: 'YYYY/MM/DD',          valueFormat: 'YYYY/MM/DD'}"></div>
            </div>
            <div data-bind="if: !screenModeNew()" class="mode-style">
                <span data-bind="text: appDate"></span>
            </div>
        </div>
        
        <div class="valign-center control-group">
            <div class="table">
                <div class="cell valign-top">
                    <div class="lblTitleKAF009" data-bind="ntsFormLabel: {required:true}">勤務時間</div>
                </div>
                <div class="cell valign-center">
                    <div style="display: inline-block; width: 250px;">
                        <input class="row-cell-margin inputTime-kaf009" id="inpStartTime1" data-bind="ntsTimeWithDayEditor: { name: '#[KAF009_39]',                constraint:'TimeWithDayAttr',                value: timeStart1,                 enable: isNewScreen,                 readonly: false }" />
                        <label class="valign-center link-label">~</label>
                    </div>
                    <div class="row-cell-margin" style="display: inline-block; width: 250px;">
                        <input class="row-cell-margin inputTime-kaf009" id="inpEndTime1" data-bind="ntsTimeWithDayEditor: {name: '#[KAF009_40]',                 constraint:'TimeWithDayAttr',                value: timeEnd1,                 enable: isNewScreen,                 readonly: false}" />
                    </div>
                    <div class="cell valign-center row-cell-margin" style="display: none">
                        
                        <button id="btnLocation1" class="location-button-009" data-bind="click: function(){openLocationDialog(1)}, enable: isNewScreen">場所選択</button>
                        <label id="lblLocation" data-bind="text : workLocationCD() + '　' +workLocationName()"></label>
                    </div>
                </div>
            </div>
            
            <div class="table">
                <div class="cell valign-top lblTitleKAF009"></div>
                <div class="cell valign-center">
                    <div style="display: inline-block; width: 250px;">
                        <div class="row-cell-margin kaf009-lblComment" data-bind="if: commentGo1().length!=0">
                            <div data-bind="if: commentGo1().trim().length!=0">
                                (<label data-bind="text: commentGo1,style:{color:colorGo, fontWeight:fontWeightGo() &gt; 0 ? 'bold':'normal'}"></label>)
                            </div>
                        </div>
                    </div>
                    <div class="row-cell-margin" style="display: inline-block; width: 250px;">
                        <div class="row-cell-margin kaf009-lblComment" data-bind="if: commentBack1().length!=0">
                            <div data-bind="if: commentBack1().trim().length!=0">
                                (<label data-bind="text: commentBack1,style:{color:colorBack, fontWeight:fontWeightBack() &gt; 0 ? 'bold':'normal'}"></label>)
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        
        <div class="valign-center control-group" data-bind="if: useMulti()" style="display: none">
            <div class="table">
                <div class="cell valign-center">
                    <div class="lblTitleKAF009" data-bind="ntsFormLabel: {required:false}">勤務時間2</div>
                </div>
                <div class="cell valign-center">
                    <div style="display: inline-block; width: 250px;">
                        <input class="row-cell-margin inputTime-kaf009" id="inpStartTime2" data-bind="ntsTimeWithDayEditor: {name: '#[KAF009_41]',                constraint:'TimeWithDayAttr',                value: timeStart2,                enable: isNewScreen }" />
                        <label class="valign-center link-label">~</label>
                    </div>
                    <div class="row-cell-margin" style="display: inline-block; width: 250px;">
                        <input class="row-cell-margin inputTime-kaf009" id="inpEndTime2" data-bind="ntsTimeWithDayEditor: {name: '#[KAF009_42]',                 constraint:'TimeWithDayAttr',                value: timeEnd2,                enable: isNewScreen }" />
                    </div>
                    <div class="cell row-cell-margin" style="display: inline-block">
                        <button id="btnLocation2" class="location-button-009" data-bind="click: function(){openLocationDialog(2)}, enable: isNewScreen">場所選択</button>
                        <label data-bind="text : workLocationCD2() + '　' +workLocationName2()"></label>
                    </div>
                </div>
            </div>
            
            <div class="table">
                <div class="cell valign-top lblTitleKAF009"></div>
                <div class="cell valign-center">
                    <div style="display: inline-block; width: 250px;">
                        <div class="row-cell-margin kaf009-lblComment">
                            (<label data-bind="text: commentGo2,style:{color:colorGo, fontWeight:fontWeightGo() &gt; 0 ? 'bold':'normal'}"></label>)
                        </div>
                    </div>
                    <div class="row-cell-margin" style="display: inline-block; width: 250px;">
                        <div class="row-cell-margin kaf009-lblComment">
                            (<label data-bind="text: commentBack2,style:{color:colorBack, fontWeight:fontWeightBack() &gt; 0 ? 'bold':'normal'}"></label>)
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        
        <div class="valign-center control-group">
            <div data-bind="if: checkboxDisplay">
                <div data-bind="ntsCheckBox: { checked: workChangeAtr ,             enable: checkboxEnable,             text: '勤務を変更する' }"></div>
            </div>
            <HR data-bind="display: typeSiftVisible() ? '' : 'none' " />
            <div class="table clsWorkType" data-bind="if: workChangeBtnDisplay">
                <div class="cell valign-center ">
                    <div class="valign-center control-group" data-bind="ntsFormLabel:{ required: true }">勤務種類</div>
                    <BR />
                    <div class="valign-center control-group" data-bind="ntsFormLabel:{ required: true }">就業時間帯</div>
                </div>
                <div class="cell valign-center">
                    <button id="workSelect" data-bind="enable: workChangeAtr() &amp;&amp; isNewScreen(), click: openDialogKdl003">選択</button>
                </div>
                <div class="cell valign-center">
                    <label class="lblWorkTypeCd" data-bind="text: workTypeCd"></label>
                    <LABEL data-bind="text: workTypeName"></LABEL> 
                    <BR /> 
                    <label class="lblSiftCd" data-bind="text: siftCD"></label> 
                    <LABEL data-bind="text: siftName"></LABEL>
                </div>
            </div>
        </div>
        
        <div class="valign-top control-group overlay-reason" data-bind="if: displayTypicalReason()">
            <div class="lblTitleKAF009" data-bind="ntsFormLabel:{}">定型理由</div>
            <div id="combo-box" data-bind="ntsComboBox: {         name: '#[KAF009_26]',         options: reasonCombo,         optionsValue: 'reasonId',         optionsText: 'reasonName',         value: selectedReason,         enable: enableTypicalReason,         columns: [{ prop: 'reasonName', length: 10 }]}">
            </div>
        </div>
        
        <div class="valign-top " data-bind="if: displayReason()">
            <div class="lblTitleKAF009" data-bind="ntsFormLabel: {constraint: 'AppReason'}">申請理由</div>
            <div id="inpReason">
                <textarea id="inpReasonTextarea" data-bind="ntsMultilineEditor: { value: multilContent,              option: multiOption,               name: '#[KAF009_29]',              enable: enableReason,              }"></textarea>
            </div>
        </div>
    </div>
    
    <div class="right-contents">
        <div class="panel panel-gray-bg panel-frame kaf009-panel">
            <div>
                <label>実績内容 :</label>
            </div>
            <BR /> 
            <div>
                <label>日付: </label>
                <label data-bind="text: realTimeDate"></label>
            </div>
            <BR /> 
            <div>
                <label>勤務種類 :</label>
                <label data-bind="text: realTimeWorkType"></label> 
            </div>
            <div>
                <label>勤務時間帯 :</label>
                <label data-bind="text: realTimeWorkTime"></label> 
            </div>
            <BR /> 
            <div>
                <label>勤務時間 :</label>
                <label data-bind="text: realTimeHour1"></label> 
            </div>
            <div>
                <label>勤務時間2 :</label>
                <label data-bind="text: realTimeHour2"></label>
            </div>
        </div>
    </div>
</div>
        </div><!-- /ko --><!-- ko if: appType() == 0 -->
            <div><div data-bind="let: { $vm: $data }">
    <div data-bind="if: screenModeNew()&amp;&amp;indicationOvertimeFlg()">
        <div class="cf valign-center control-group" data-bind="style: {padding: screenModeNew()== true ? '0px 10px 15px 12px' : '15px 10px 15px 12px'}" style="margin-left: -2px; background: #f8efd4;width: 778px;">
                        
            <div class="pull-left">時間外労働　：</div>
            <div class="pull-left">
                <table id="kaf005_overtimeAgreement_table">
                    <colgroup>
                        <col width="85px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                    </colgroup>
                    <thead>
                        <tr>
                            
                            <th class="kaf005_overtimeAgreement_header">年月</th>
                                                    
                            <th class="kaf005_overtimeAgreement_header">36時間</th>
                                                    
                            <th class="kaf005_overtimeAgreement_header">実績</th>
                                
                            <th class="kaf005_overtimeAgreement_header" style="display: none">申請</th>
                            
                            <th class="kaf005_overtimeAgreement_header" style="display: none">合計</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: overtimeWork">
                        <tr>
                                                    
                            <td data-bind="text: yearMonth, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: limitTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: actualTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td style="display: none" data-bind="text: appTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td style="display: none" data-bind="text: totalTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>      
    </div>
    <div data-bind="if: (!screenModeNew())&amp;&amp;indicationOvertimeFlg()">
        <div class="cf valign-center control-group" data-bind="style: {padding: screenModeNew()== true ? '0px 10px 15px 12px' : '15px 10px 15px 12px'}" style="margin-left: -2px; background: #f8efd4;width: 800px;">
                        
            <div class="pull-left">時間外労働　：</div>
            <div class="pull-left">
                <table id="kaf005_overtimeAgreement_table">
                    <colgroup>
                        <col width="85px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                    </colgroup>
                    <thead>
                        <tr>
                            
                            <th class="kaf005_overtimeAgreement_header">年月</th>
                                                    
                            <th class="kaf005_overtimeAgreement_header">36時間</th>
                                                    
                            <th class="kaf005_overtimeAgreement_header">実績</th>
                                
                            <th class="kaf005_overtimeAgreement_header">申請</th>
                            
                            <th class="kaf005_overtimeAgreement_header">合計</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: overtimeWork">
                        <tr>
                                                    
                            <td data-bind="text: yearMonth, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: limitTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: actualTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td data-bind="text: appTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td data-bind="text: totalTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>      
    </div>
    
    <div class="left-contents">
        <div class="cf valign-center control-group" data-bind="visible: !employeeFlag()">
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">申請者</div>
            
            <label class="lblEmployeeName fix-margin-left" data-bind="text : employeeName"></label>
            
        </div>
        <div class="cf valign-top control-group" data-bind="visible: employeeFlag()">
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">申請者</div>
            
            <div id="list-box" class="fix-margin-left" data-bind="ntsListBox: {        options: employeeList,        optionsValue: 'id',        optionsText: 'name',        multiple:false,        value: selectedEmplCodes,        enable: true,        rows: 3,        columns: [         { key: 'name',length: 11 }        ]}">
            </div>
        </div>
        <div class="cf valign-center control-group" data-bind="visible: employeeFlag()">
            <div class="cm-column"></div>
            
            <label style="margin-left: 139px;" data-bind="text : totalEmployee"></label>
        </div>
        <div class="valign-center control-group" data-bind="if: displayPrePostFlg">
            
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{ required: true }">事前事後区分</div>
            <div data-bind="if: screenModeNew" style="display: inline-block;">
                
                <div tabindex="5" class="cf fix-margin-left" id="kaf005-pre-post-select" data-bind="ntsSwitchButton: {name: '事前事後スイッチ',          options:ko.observableArray([                        { prePostCode: 0, prePostName: '事前' },                        { prePostCode: 1, prePostName: '事後' }                        ]),          optionsValue: 'prePostCode',          optionsText: 'prePostName',          value: prePostSelected,          enable: prePostEnable,          required: true }">
                </div>
            </div>
            <div data-bind="if: !screenModeNew()" style="display: inline-block;">
                <span class="label fix-margin-left-kaf005" data-bind="text: prePostSelected()==0 ? '事前' : '事後'"></span>
            </div>
        </div>
        <div class="valign-center control-group">
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{required: true}">申請日</div>
            <div data-bind="if: screenModeNew" style="display: inline-block;">
                
                <div tabindex="6" id="inputdate" class="fix-margin-left" data-bind="ntsDatePicker: {name: '申請日',required:true, value: appDate, dateFormat: 'YYYY/MM/DD'}, acceptJapaneseCalendar: true"></div>
            </div>
            <div data-bind="if: !screenModeNew()" style="display: inline-block;">
                <span class="label fix-margin-left-kaf005" data-bind="text: appDate"></span>
            </div>
        </div>
        <div data-bind="if: displayCaculationTime">
            
            <div class="table" data-bind="visible : typeSiftVisible()">
                <div class="cell valign-center" style="width: 105px;">
                    
                    <div class="valign-center control-group" data-bind="ntsFormLabel:{required: true}">勤務種類</div>
                    <BR />
                    
                    <div class="valign-center control-group" data-bind="ntsFormLabel:{required: true}">就業時間帯</div>
                </div>
                <div class="cell valign-top" data-bind="if: workTypeChangeFlg" style="width: 25px;">
                    
                    <button tabindex="7" class="workSelect" data-bind="enable: $vm.editable,click : openDialogKdl003">選択</button>
                </div>
                <div class="cell valign-center">
                    <div class="valign-center control-group">
                        
                        <label class="lblWorkTypeCd" data-bind="text: workTypeCd"></label>
                                            
                        <LABEL data-bind="text: workTypeName"></LABEL> 
                    </div>
                    <div class="valign-center control-group">
                                            
                        <label class="lblSiftCd" data-bind="text: siftCD"></label> 
                                            
                        <LABEL data-bind="text: siftName"></LABEL>
                    </div>
                </div>
            </div>
            
            <div class="valign-center control-group">
                <div class="table">
                    <div class="cell valign-top cm-column2">
                                                
                        <div class="lblTitle" data-bind="ntsFormLabel: {required: true}" style="margin-right: 16px">勤務時間</div>
                    </div>
                    <div class="cell valign-center">
                        <div>
                                                        
                            <input tabindex="8" class="row-cell-margin inputTime-kaf005 right-content" id="inpStartTime1" data-bind="ntsTimeWithDayEditor: { name: '#[KAF005_333]', constraint:'TimeWithDayAttr',value: timeStart1, enable: $vm.editable, readonly: false, required: true }" />
                                                            
                            <label class="valign-center link-label-kaf005">~</label>
                        </div>
                    </div>
                    <div class="cell valign-center">
                        <div class="row-cell-margin">
                                                        
                            <input tabindex="9" id="inpEndTime1" class="right-content inputTime-kaf005" data-bind="ntsTimeWithDayEditor: {name: '#[KAF005_334]', constraint:'TimeWithDayAttr', value: timeEnd1, enable: $vm.editable, readonly: false, required: true}" />
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="valign-center control-group" data-bind="if: useMulti(),visible : false">
                <div class="table">
                    <div class="cell valign-top cm-column2">
                        <div class="lblTitle"></div>
                    </div>
                    <div class="cell valign-center">
                        <div>
                                                        
                            <input tabindex="10" class="row-cell-margin inputTime-kaf005 right-content" id="inpStartTime2" data-bind="ntsTimeWithDayEditor: {name: '#[KAF005_335]',constraint:'TimeWithDayAttr',value: timeStart2}" />
                                                            
                            <label class="valign-center link-label-kaf005">~</label>
                        </div>
                    </div>
                    <div class="cell valign-center">
                        <div>
                                                        
                            <input tabindex="11" class="row-cell-margin inputTime-kaf005 right-content" id="inpEndTime2" data-bind="ntsTimeWithDayEditor: {name: '#[KAF005_336]', constraint:'TimeWithDayAttr',value: timeEnd2}" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="cf valign-top control-group" data-bind="if: displayCaculationTime() &amp; restTimeDisFlg()">
                        
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {}">休憩時間</div>
            </div>
            <div class="table-time">
                <table id="fixed-table">
                    <colgroup>
                        <col width="109px" />
                        <col width="115px" />
                        <col width="115px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="ui-widget-header" rowspan="2"></th>
                                                    
                            <th class="ui-widget-header" rowspan="2">開始</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">終了</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: restTime"> 
                        <tr>
                                                    
                            <td class="header" data-bind="text: frameName"></td>
                                                    
                            <td><input tabindex="12" class="right-content" data-bind="attr: { id: 'restTimeStart_'+attendanceID()+'_'+frameNo()},          ntsTimeWithDayEditor: {          name: '#[KAF005_337]',           value: startTime,           constraint:'TimeWithDayAttr',           enable: false,          option: {width: '85px', timeWithDay: true}}" /></td>
                                                    
                            <td><input tabindex="12" class="right-content" data-bind="attr: { id: 'restTimeEnd_'+attendanceID()+'_'+frameNo()},          ntsTimeWithDayEditor: {          name: '#[KAF005_338]',           value: endTime,           constraint:'TimeWithDayAttr',           enable: false,          option: {width: '85px', timeWithDay: true}}" /></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div data-bind="if: displayCaculationTime">
                        
            <button data-bind="click: CaculationTime, enable: $vm.editable" tabindex="14" style="margin: 15px 0px 10px 290px;" class="caret-bottom">計算</button>
        </div>
        <div class="cf valign-top control-group cell" data-bind="if: prePostSelected() == 0">
                
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {required: true}">残業時間</div>
            </div>  
            <div class="table-time">
                <table id="fixed-overtime-hour-table-pre">
                    <colgroup>
                        <col width="110px" />
                        <col width="110px" />
                        
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="ui-widget-header" rowspan="2"></th>
                                                
                            <th class="ui-widget-header" rowspan="2">申請時間</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: overtimeHours">
                        <tr>
                                                    
                            <td class="header" data-bind="attr: { id: 'overtimeHoursHeader_'+attendanceID()+'_'+frameNo()}, text: frameName"></td>
                                                    
                            <td data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()}, style: { 'background-color' : color }"><input tabindex="15" class="right-content overtimeHoursCheck" data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()}, style: { 'background-color' : color },          ntsTimeEditor: {          name: nameID,           value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable() &amp;&amp; $vm.enableOvertimeInput() }" /></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="cf valign-top control-group cell" data-bind="if: prePostSelected() == 1">
                
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {required: true}">残業時間</div>
            </div>  
            <div class="table-time">
                <table id="fixed-overtime-hour-table">
                    <colgroup>
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="ui-widget-header" rowspan="2"></th>
                                                
                            <th class="ui-widget-header" rowspan="2">申請時間</th>
                             
                            <th class="ui-widget-header" rowspan="2">事前申請</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">実績時間</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: overtimeHours">
                        <tr>
                                                    
                            <td class="header" data-bind="attr: { id: 'overtimeHoursHeader_'+attendanceID()+'_'+frameNo()}, text: frameName"></td>
                                                    
                            <td data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()}, style: { 'background-color' : color }"><input tabindex="15" class="right-content overtimeHoursCheck" data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()}, style: { 'background-color' : color },          ntsTimeEditor: {          name: nameID,           value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable() &amp;&amp; $vm.enableOvertimeInput() }" /></td>
                                                    
                            <td class="right-content" data-bind="text: preAppTime"></td>
                                                    
                            <td class="right-content" data-bind="text: caculationTime"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="cf valign-top control-group" data-bind="if: displayBreakTimeFlg">
                    
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {required : true}">休出時間</div>
            </div>  
            <div class="table-time">
                <table id="fixed-break_time-table">
                    <colgroup>
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                    </colgroup>
                    <thead>
                        <tr>
                                                    
                            <th class="ui-widget-header" rowspan="2"></th>
                                                    
                            <th class="ui-widget-header" rowspan="2">申請時間</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">事前申請</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: breakTimes">
                        <tr>
                                                    
                            <td class="header" data-bind=" text: frameName"></td>
                                                    
                            <td><input tabindex="16" class="right-content" data-bind="ntsTimeEditor: {          name: '#[KAF005_76]',          value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable }" /></td>
                                                    
                            <td class="right-content" data-bind="text: preAppTime"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div data-bind="if: prePostSelected() == 1">
            <div class="cf valign-top control-group" data-bind="if: displayBonusTime">
                            
                <div class="cm-column" style="display: inline-block;">
                    <div class="lblTitle pull-left cm-column" data-bind="ntsFormLabel: {}">加給時間</div>
                </div>
                <div class="table-time">
                    <table id="fixed-bonus_time-table">
                        <colgroup>
                            <col width="110px" />
                            <col width="110px" />
                            <col width="110px" />
                        </colgroup>
                        <thead>
                            <tr>
                                                        
                                <th class="ui-widget-header" rowspan="2"></th>
                                                        
                                <th class="ui-widget-header" rowspan="2">申請時間</th>
                                                        
                                <th class="ui-widget-header" rowspan="2">事前申請</th>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: bonusTimes">
                            <tr>
                                                                                
                                <td class="header" data-bind="text: frameName"></td>
                                 
                                <td>
                                
                                <input tabindex="17" class="right-content" data-bind="ntsTimeEditor: {          name:itemName,          value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable }" /></td>
                                 
                                <td class="right-content" data-bind="text: preAppTime"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div data-bind="if: prePostSelected() == 0">
            <div class="cf valign-top control-group" data-bind="if: displayBonusTime">
                            
                <div class="cm-column" style="display: inline-block;">
                    <div class="lblTitle pull-left cm-column" data-bind="ntsFormLabel: {}">加給時間</div>
                </div>
                <div class="table-time">
                    <table id="fixed-bonus_time-table-pre">
                        <colgroup>
                            <col width="110px" />
                            <col width="110px" />
                        </colgroup>
                        <thead>
                            <tr>
                                                        
                                <th class="ui-widget-header" rowspan="2"></th>
                                                        
                                <th class="ui-widget-header" rowspan="2">申請時間</th>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: bonusTimes">
                            <tr>
                                                                                
                                <td class="header" data-bind="text: frameName"></td>
                                 
                                <td>
                                
                                <input tabindex="17" class="right-content" data-bind="ntsTimeEditor: {          name:itemName,          value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable}" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <div class="valign-center control-group overlay-reason" data-bind="if: typicalReasonDisplayFlg">
                        
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{}">定型理由</div>
                        
            <div tabindex="18" class="combo-box" data-bind="ntsComboBox: {name: '#[KAF005_87]',          options: reasonCombo,         optionsValue: 'reasonId',         optionsText: 'reasonName',         value: selectedReason,         enable: $vm.editable,         dropDownAttachedToBody: true,         columns: [{ prop: 'reasonName', length: 10 }]}">
            </div>
        </div>
        
        <div class="cf valign-top control-group" data-bind="if: isShowReason()">
                        
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">申請理由</div>
             
            <div id="multiline">
                <textarea tabindex="19" id="appReason" class="textarea-kaf005" data-bind="ntsMultilineEditor: {name: '#[KAF005_89]',          value: multilContent,         option: {          resizeable: false,                         width: '500',                         required: requiredReason()                        },                        enable:displayAppReasonContentFlg() &amp;&amp; $vm.editable()        }"></textarea>
            </div>
        </div>
        
        <div class="valign-center control-group overlay-reason" data-bind="if: displayDivergenceReasonForm">
             
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{}">乖離定型理由</div>
             
            <div tabindex="20" class="combo-box" data-bind="ntsComboBox: {name: '#[KAF005_91]',          options: reasonCombo2,         optionsValue: 'reasonId',         optionsText: 'reasonName',         value: selectedReason2,         enable: $vm.editable,         dropDownAttachedToBody: true,         columns: [{ prop: 'reasonName', length: 10 }]}">
            </div>
        </div>
        
        <div class="cf valign-top control-group" data-bind="if: displayDivergenceReasonInput">
             
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">乖離理由</div>
             
            <div id="multiline"> 
                <textarea tabindex="21" id="divergenceReason" class="textarea-kaf005" data-bind="ntsMultilineEditor: {name: '#[KAF005_93]',          value: multilContent2,         option: {          resizeable: false,                         width: '500',                         required: requiredReason2()                        },                        enable: $vm.editable        }"></textarea>
            </div>
        </div>
    </div>
    
    <div class="right-contents panel" style="background-color: #E7E6E6;" data-bind="visible: isRightContent">
            <label data-bind="visible: referencePanelFlg" style="margin-left: 38px">実績の内容</label> 
            <BR data-bind="visible: referencePanelFlg" />
            <HR data-bind="visible: referencePanelFlg" style="width: 250px;" />
        <div class="panel panel-gray-bg panel-frame" data-bind="visible: referencePanelFlg" style="margin-top: 2px">
            <div class="control-group">
            
            <label>日付 :</label>
            
            <label data-bind="text : appDateReference"></label>
            </div>
            <div class="control-group">
            
            <label>勤務種類 :</label>
            
            <label data-bind="text : workTypeCodeReference"></label>
            
            <label data-bind="text : workTypeNameReference"></label>
            </div>
            <div class="control-group">
            
            <label>勤務時間帯 :</label>
            
            <label data-bind="text : siftCodeReference"></label>
            
            <label data-bind="text : siftNameReference"></label>
            </div>
            <div class="control-group">
            
            <label>勤務時間 :</label>
            
            <label data-bind="text : workClockFrom1To1Reference"></label>
            </div>
            <div class="control-group" data-bind="visible: displayWorkClockFrom2To2Reference">
            
            <label style="margin-left: 72px" data-bind="text : workClockFrom2To2Reference"></label>
            </div>
            <div class="control-group" data-bind="foreach: overtimeHoursReference">
                    
                    <label data-bind="text : frameName"></label>
                    
                    <label data-bind="text : applicationTime"></label>
                    <BR />
            </div>
            <div class="control-group">
            
            <label>就業時間外深夜 :</label>
            
            <label data-bind="text : overTimeShiftNightRefer"></label>
            </div>
            <div class="control-group">
            
            <label>ﾌﾚｯｸｽ超過時間 :</label>
            
            <label data-bind="text : flexExessTimeRefer"></label>
            </div>
        </div>
        <div class="panel panel-gray-bg panel-frame control-group" data-bind="visible: allPreAppPanelFlg()">
            
            <label>【事前申請】</label>
             <BR /> <BR /> 
            
            <div>
                    <label data-bind=" if: !(preAppPanelFlg())" style="margin-left: 7px">事前申請なし</label>
                    <div data-bind=" if: preAppPanelFlg()">
                        <div class="control-group">
                        
                        <label>日付 :</label>
                        
                        <label data-bind="text : appDatePre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>勤務種類 :</label>
                        
                        <label data-bind="text : workTypeCodePre"></label>
                        
                        <label data-bind="text : workTypeNamePre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>勤務時間帯 :</label>
                        
                        <label data-bind="text : siftCodePre"></label>
                        
                        <label data-bind="text : siftNamePre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>勤務時間 :</label>
                        
                        <label data-bind="text : workClockFrom1To1Pre"></label>
        
        
        
        
                        </div>
                        <div class="control-group" data-bind="visible: displayWorkClockFrom2To2Pre">
                        
                        <label style="margin-left: 72px" data-bind="text : workClockFrom2To2Pre"></label>
        
        
        
        
                        </div>
                        
                        <div class="control-group" data-bind="foreach: overtimeHoursPre">
                        
                        <label data-bind="text : frameName"></label>
                        
                        <label data-bind="text : applicationTime"></label>
                        <BR />
                        </div>
                        <div class="control-group">
                        
                        <label>就業時間外深夜 :</label>
                        
                        <label data-bind="text : overTimeShiftNightPre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>ﾌﾚｯｸｽ超過時間 :</label>
                        
                        <label data-bind="text : flexExessTimePre"></label>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>
            </div><!-- /ko --><!-- ko if: appType() == 1 -->
            <div><div data-bind="let: { $vm: $data }">
    <div class="left-content-kaf006">
            
            <div data-bind="if : !employeeFlag()">
                
                <div class="line_1 column1" data-bind="ntsFormLabel:{}">申請者</div>
                
                <div class="line_1 thut">
                    <label data-bind="text : employeeName"></label>
                </div>
                <div id="numberOfRemaining1" class="line_1" data-bind="if: disAll">
                    
                    <table id="reamaning">
                        <tr style="background-color: #97D155">
                            <td data-bind="css: { display1: !subHdDis()}">
                                <div>代休振休残数</div>
                            </td>
                            <td data-bind="css: { display1: !subVacaDis()}">
                                <div>振休振休残数</div>
                            </td>
                            <td data-bind="css: { display1: !yearDis()}">
                                <div>年休残数</div>
                            </td>
                            <td data-bind="css: { display1: !stockDis()}">
                                <div>ストック休暇残数</div>
                            </td>
                        </tr>
                        <tr>
                            <td class="flRight" data-bind="css: { display1: !subHdDis()}">
                                <div data-bind="text: subHdRemain"></div>
                            </td>
                            <td class="flRight" data-bind="css: { display1: !subVacaDis()}">
                                <div data-bind="text: subVacaRemain"></div>
                            </td>
                            <td class="flRight" data-bind="css: { display1: !yearDis()}">
                                <div data-bind="text: yearRemain"></div>
                            </td>
                            <td class="flRight" data-bind="css: { display1: !stockDis()}">
                                <div data-bind="text: stockRemain"></div>
                            </td>
                        </tr>
                    </table>
                </div>
        </div>
        
        <div data-bind="if : employeeFlag()">
            <div>
                
                <div id="applicant" class="line_1 column1" data-bind="ntsFormLabel:{}">申請者</div>
                
                <div class="line_1 thut">
                    <div id="list-box" data-bind="ntsListBox: {        options: employeeList,        optionsValue: 'id',        optionsText: 'name',        multiple:false,        value: selectedEmplCodes,        enable: true,        rows: 3,        columns: [ { key: 'name',length: 11} ]}">
                </div>
                </div>
                <div id="numberOfRemaining2" class="line_1">
                    
                    <table id="reamaning">
                        <tr style="background-color: #97D155">
                            <td>代休振休残数</td>
                            <td>振休振休残数</td>
                            <td>年休残数</td>
                            <td>ストック休暇残数</td>
                        </tr>
                        <tr>
                            <td class="flRight" data-bind="text: subHdRemain"></td>
                            <td class="flRight" data-bind="text: subVacaRemain"></td>
                            <td class="flRight" data-bind="text: yearRemain"></td>
                            <td class="flRight" data-bind="text: stockRemain"></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="totalEmp"><label data-bind="text : totalEmployee"></label></div>
        </div>
        <table id="tbl">
            <tr data-bind="if: displayPrePostFlg">
                <td><div data-bind="ntsFormLabel:{}">事前事後区分</div></td>
                <td class="thut"><div data-bind="if: screenModeNew" style="display: inline-block;">
                        
                        <div id="switch_prePost" data-bind="ntsSwitchButton: {name: '事前事後区分',          options:ko.observableArray([                        { prePostCode: 0, prePostName: '事前' },                        { prePostCode: 1, prePostName: '事後' }                        ]),          optionsValue: 'prePostCode',          required: true,          optionsText: 'prePostName',          value: prePostSelected,          enable: prePostEnable}">
                        </div>
                    </div>
                    <div data-bind="if: !screenModeNew()" style="display: inline-block;">
                        <span style="margin-left: -3px;" data-bind="text: prePostSelected()==0 ? '事前' : '事後'"></span>
                    </div></td>
            </tr>
            <tr>
                <td><div data-bind="ntsFormLabel: {required: true}" style="margin-right: 16px">申請日付</div></td>
                <td><div style="float: left; margin-right: 15px" data-bind="ntsCheckBox: { checked: displayEndDateFlg,         enable: enableDisplayEndDate,         text: '複数日'},        visible: screenModeNew">
                    </div>
                    <div data-bind="if: screenModeNew()">
                        
                        <div id="inputdate" data-bind="ntsDatePicker: {name: '申請日付',          required:true,          value: appDate,          dateFormat: 'YYYY/MM/DD'},           acceptJapaneseCalendar: true,          visible: !displayEndDateFlg()"></div>
                        <div id="daterangepicker" data-bind="ntsDateRangePicker: { name: '申請日付',          startName:'申請日付',          endName: '申請日付',          required:true, enable: true,           showNextPrevious: false,           value: dateValue,           maxRange: 'none'},        visible: displayEndDateFlg()"></div>
                    </div>
                    <div data-bind="if: !screenModeNew()">
                        <span style="margin-left: 6px;" data-bind="text: appDate"></span>
                    </div>
                    <div style="clear: both"></div>
                </td>
            </tr>
            <tr>
                <td></td>
                <td class="thut">
                    
                    <label class="ntsRadioBox" style="margin-right: 5px"> <input type="radio" name="textRadio" data-bind="checkedValue: 0,         checked: selectedAllDayHalfDayValue,         enable: enbAllDayHalfDayFlg" />
                        <span class="box"></span><span class="label" data-bind="text: '終日休暇'"></span>
                    </label> 
                    
                    <label class="ntsRadioBox"> <input type="radio" name="textRadio" data-bind="checkedValue: 1,           checked: selectedAllDayHalfDayValue,          enable: enbAllDayHalfDayFlg" />
                        <span class="box"></span><span class="label" data-bind="text: '半日休暇'"></span>
                    </label>
                </td>
            </tr>
            <tr>
                <td>
                    
                    <div class="lblTitle-kaf006 cm-column2-kaf006 control-group-kaf006" data-bind="ntsFormLabel:{required: true}">休暇種類</div>
                </td>
                <td class="thut">
                    
                    <div id="hdType" data-bind="ntsSwitchButton: {         name: '休暇種類',         options: holidayTypes,         optionsValue: 'code',         optionsText: 'name',         value: holidayTypeCode,         required: true,         enable: screenModeNew }"></div>
                </td>
            </tr>
        </table>
        <div data-bind="css : {disappear: !displayStartFlg()}">
            <div class="valign-center-kaf006  control-group-kaf006" data-bind="visible: contentFlg">
                
                <div class="lblTitle-kaf006 cm-column-kaf006" data-bind="ntsFormLabel: {required: true}">勤務種類</div>
                
                <div class="combo-box-kaf006" id="workTypes" data-bind="ntsComboBox: {name: '勤務種類',          options: typeOfDutys,         optionsValue: 'typeOfDutyID',         optionsText: 'typeOfDutyName',         value: selectedTypeOfDuty,         required: true,         enable: enbWorkType}">
                </div>
                
                <div data-bind="ntsCheckBox: { checked: displayHalfDayValue,             enable: enbHalfDayFlg,             text: '利用可能な全ての組み合わせを表示'}"></div>
            </div>
            
            <div data-bind="visible:changeWorkHourValueFlg">
                <div class="valign-center-kaf006 control-group-kaf006">
                    
                    <div data-bind="ntsCheckBox: { checked: changeWorkHourValue,        enable: enbChangeWorkHourFlg,        text: '就業時間帯を変更する'}"></div>
                    
                    <HR data-bind="" style="width: 1000px;" />
                </div>
                <div class="valign-center-kaf006 control-group-kaf006 margin-left-for-workChange-kaf006">
                    <div class="table">
                        <div class="cell valign-top-kaf006 cm-column2-kaf006">
                            
                            <div class="lblTitle-kaf006" data-bind="ntsFormLabel: {required: false}">就業時間帯</div>
                        </div>
                        <div class="cell valign-top-kaf006 cm-column2-kaf006">
                            
                            <button class="proceed" data-bind="click: btnSelectWorkTimeZone,          enable : enbbtnWorkTime() &amp; changeWorkHourValue()">就業時間帯選択</button>
                        </div>
                        <div class="cell valign-center-kaf006">
                            
                            <label class="row-cell-margin-kaf006" data-bind="text: displayWorkTimeName"></label>
                        </div>
                    </div>
                </div>
                <div class="valign-center-kaf006 control-group-kaf006 margin-left-for-workChange-kaf006">
                    <div class="table">
                        <div class="cell valign-top-kaf006 cm-column2-kaf006">
                            
                            <div class="lblTitle-kaf006" data-bind="ntsFormLabel: {required: false}">勤務時間</div>
                        </div>
                        <div class="cell valign-top-kaf006 cm-column2-kaf006">
                            <div>
                                
                                <input class="row-cell-margin-kaf006 inputDate-kaf006 right-content-kaf006" id="inpStartTime1" data-bind="ntsTimeWithDayEditor: {            name: '#[KAF006_58]',            constraint:'TimeWithDayAttr',            value: timeStart1,            enable : eblTimeStart1 }" />
                                
                                <label class="valign-center-kaf006 link-label-kaf006">~</label>
                            </div>
                        </div>
                        <div class="cell valign-center-kaf006">
                            <div>
                                
                                <input class="row-cell-margin-kaf006 inputDate-kaf006 right-content-kaf006" id="inpEndTime1" data-bind="ntsTimeWithDayEditor: {            name: '#[KAF006_59]',             constraint:'TimeWithDayAttr',            value: timeEnd1,             enable : eblTimeEnd1}" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="valign-center-kaf006  control-group-kaf006" data-bind="if: hdTypeDis">
                <div data-bind="if: fix">
                    <div class="valign-center-kaf006 control-group-kaf006">
                        
                        <div class="lblTitle-kaf006 cm-column-kaf006" data-bind="ntsFormLabel:{required: true}">続柄・喪主</div>
                        
                        <div class="combo-box-kaf006" id="relaCD-combo" data-bind="ntsComboBox: {name: '続柄・喪主',            options: relationCombo,           optionsValue: 'relationCd',           optionsText: 'relationName',           value: selectedRelation,           enable: relaEnable,           required: requiredRela,           columns: [{ prop: 'relationName',length: 10 }]}">
                        </div>
                        
                        <div data-bind="if: mournerDis()" style="display: inline-block; margin-left: 10px;">
                            <div data-bind="ntsCheckBox: { checked: isCheck, enable: relaMourner}">喪主</div>
                        </div>
                    </div>
                    <div class="valign-center-kaf006 control-group-kaf006">
                    <div data-bind="if: relaResonDis">
                        
                        <div class="lblTitle-kaf006 cm-column-kaf006" data-bind="ntsFormLabel: {required: true}">続柄入力</div>
                        
                        <input id="relaReason" style="margin: 0px 0px 0px 15px !important;" data-bind="ntsTextEditor: {             name: '続柄入力',                                           value: relaReason,                                           constraint: 'RelationshipReasonPrimitive',                                            required: true,                                           enable: relaRelaReason,                                           option: {textalign: 'left',                                            width: '380px',                                            placeholder:'3親等以内の続柄を入力してください。'}                                       }" />
                         
                         <div class="maxDay" data-bind="if: maxDayDis">
                            <div data-bind="text: maxDayline1"></div>
                            <div data-bind="text: maxDayline2"></div>
                         </div> 
                    </div>
                    <div data-bind="if: !relaResonDis()">
                         
                         <div class="maxDay limit-fix" data-bind="if: maxDayDis">
                            <div data-bind="text: maxDayline1"></div>
                            <div data-bind="text: maxDayline2"></div>
                         </div> 
                    </div>              
                    </div>
                </div>
                <div data-bind="if: !fix() &amp; dataMax()">
                    <div class="maxDay limit-fix" data-bind="if: maxDayDis">
                        <div data-bind="text: maxDayline1"></div>
                        <div data-bind="text: maxDayline2"></div>
                 </div>
                </div>
            </div>
            
            <div class="valign-center-kaf006 control-group-kaf006 overlay-reason-kaf006" data-bind="if: typicalReasonDisplayFlg">
                
                <div class="lblTitle-kaf006 cm-column-kaf006" data-bind="ntsFormLabel:{}">定型理由</div>
                
                <div class="combo-box-kaf006" data-bind="ntsComboBox: {name: '#[KAF006_24]',          options: reasonCombo ,         optionsValue: 'reasonId',         optionsText: 'reasonName',         value: selectedReason,         enable: enbReasonCombo,         columns: [{ prop: 'reasonName',length: 10 }]}">
                </div>
            </div>
            
            <div class="cf valign-top-kaf006 control-group-kaf006" data-bind="if: displayAppReasonContentFlg">
                
                <div class="lblTitle-kaf006 cm-column-kaf006" data-bind="ntsFormLabel: {}">申請理由</div>
                
                <div id="multiline">
                    <textarea id="appReason" class="textarea-kaf006" data-bind="ntsMultilineEditor: {name: '#[KAF006_26]',          value: multilContent,         enable: enbContentReason,         option: {          resizeable: false,                         width: '500',                         required: requiredReason()                        },        }"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
            </div><!-- /ko --><!-- ko if: appType() == 2 --><div data-bind="let: { $vm: $data }">
<div data-bind="with: appWorkChange">
    
    <div class="left-contents-kaf007">
        
        <div class="valign-center control-group-kaf007">

            <div class="lblTitle-kaf007" style="vertical-align: top;" data-bind="ntsFormLabel: {}">申請者</div>
            
            <label class="lblEmployeeName" data-bind="text: $parent.employeeName,visible:!$parent.screenModeNew()==true"></label>
            <div style="display: inline-block;" class="control-group-kaf007" data-bind="if:$parent.screenModeNew()==true">
                <label data-bind="text:$parent.employeeName,visible:!$parent.employeeList().length" class="text_line"></label>
                <div data-bind="if:$parent.employeeList().length">
                    <div id="list-box" data-bind="ntsListBox: {            options: $parent.employeeList ,            optionsValue: 'code',            optionsText: 'name',            multiple: false,            value: $parent.selectedEmployee ,            enable: true,            rows: 5 }"></div>
                    <div class="control-group-kaf007" data-bind="text:$parent.totalEmployeeText"></div>
                </div>
            </div>
        </div>
        
        <div class="valign-center control-group-kaf007 " data-bind="if: $parent.prePostDisp">
            <div class="lblTitle-kaf007" data-bind="ntsFormLabel:{}">事前事後区分</div>
            
            <div data-bind="if: $parent.screenModeNew" class="mode-style">
                <div id="pre-post" class="cf" data-bind="ntsSwitchButton: {         options:ko.observableArray([                       { prePostCode: 0, prePostName: '事前' },                       { prePostCode: 1, prePostName: '事後' }                       ]),         optionsValue: 'prePostCode',         optionsText: 'prePostName',         value:  application().prePostAtr,         enable: $parent.prePostEnable,         required:true,         name:'事前事後区分'         }">
                </div>
            </div>
            <div data-bind="if: !$parent.screenModeNew()" class="mode-style">
                <span data-bind="text: application().prePostAtr()==0 ? '事前' : '事後'"></span>
            </div>
        </div>
        
        <div class="valign-center control-group-kaf007">
            <div class="lblTitle-kaf007" data-bind="ntsFormLabel:{required:true}">申請日付</div>
                        
            
            <div data-bind="if: $parent.screenModeNew()" class="mode-style">
                <div data-bind="ntsCheckBox: { checked: $parent.multiDate ,                text: '複数日',               enable: true }"></div>

                <div id="daterangepicker" data-bind="ntsDateRangePicker: {              name:'#[KAF007_12]',              startName: '#[KAF007_12]' ,              endName: '#[KAF007_12]' ,               value: $parent.datePeriod,               maxRange: 'none',               required: $parent.multiDate ,               showNextPrevious: false               },         visible:$parent.multiDate()"></div>


                <div id="singleDate" data-bind="ntsDatePicker: {            value: $parent.dateSingle,             valueFormat: 'YYYY/MM/DD' ,             dateFormat: 'YYYY/MM/DD' ,             required:!$parent.multiDate() ,             name:'申請日付'            },      visible:!$parent.multiDate()"></div>



            </div>
            <div data-bind="if: !$parent.screenModeNew()" class="mode-style">
                <span data-bind="text: application().startDate"></span>
                
                <label data-bind="visible: application().startDate() != application().endDate()" class="valign-center link-app-date-label-kaf007">~</label>
                <span data-bind="text: application().endDate, visible: application().startDate() != application().endDate()"></span>
            </div>

            <label data-bind="text: $parent.appChangeSetting().commentContent1      ,css: { 'bold': $parent.appChangeSetting().commentFontWeight1  }      ,style:{color:$parent.appChangeSetting().commentFontColor1}"></label>
        </div>
        
        <div class="valign-center control-group-kaf007">
            
            
            <hr align="left" class="hr-style" />
            <div class="table">
                <div class="cell valign-center">
                    
                    <div class="work-select-margin" data-bind="ntsFormLabel:{required:true}">勤務種類</div>
                    <br />
                    
                    <div class="work-select-margin" data-bind="ntsFormLabel:{required:true}">就業時間帯</div>
                </div>
                <div class="cell valign-center">
                    
                    
                    <button id="workSelect-kaf007" data-bind=" click: $parent.openKDL003Click">選択</button>
                </div>
                <div class="cell valign-center">
                    
                    <label class="lblWorkTypeCd" data-bind="text: workChange().workTypeCd() + '　' + workChange().workTypeName()"></label><br />
                    
                    <label class="lblSiftCd" data-bind="text: workChange().workTimeCd() + '　' + workChange().workTimeName()"></label>
                </div>
            </div>
        </div>
        
        <div class="valign-center control-group-kaf007" data-bind="visible: $parent.isWorkChange">
            <div class="table">
                <div class="cell valign-top">
                    <div class="lblTitle-kaf007" data-bind="ntsFormLabel: {required:true}">勤務時間</div>
                </div>
                <div class="cell valign-center row-width-inputTime">
                    <div>
                        
                        <input class="row-cell-margin right-content-kaf007 inputTime-kaf007" id="inpStartTime1" data-bind="ntsTimeWithDayEditor: { name: '勤務時間直行',                constraint:'TimeWithDayAttr',                value: workChange().workTimeStart1,                 enable: $parent.enableTime() ,                 readonly: false,                required: $parent.requiredCheckTime }" />

                    </div>
                </div>
                <div class="cell valign-center row-width-inputTime" style="margin-left: 90px; width: auto !important; display: inline-block;">
                    <div class="row-cell-margin">
                        
                        <label class="valign-center link-label-kaf007">~</label>
                        
                        <input id="inpEndTime1" class="right-content-kaf007 inputTime-kaf007" data-bind="ntsTimeWithDayEditor: {name: '勤務時間直帰',                 constraint:'TimeWithDayAttr',                 value: workChange().workTimeEnd1,                 enable: $parent.enableTime() ,                 readonly: false,                required: $parent.requiredCheckTime }" />
                        <label data-bind="text: $parent.appChangeSetting().commentContent2      ,css: { 'bold': $parent.appChangeSetting().commentFontWeight2  }      ,style:{color:$parent.appChangeSetting().commentFontColor2}"></label>
                    </div>
                </div>

            </div>
            
        </div>
        
         <div class="valign-center control-group-kaf007" data-bind="if: $parent.isMultipleTime"> 
            <div class="table">
                <div class="cell valign-center">
                    <div class="lblTitle-kaf007" data-bind="ntsFormLabel: {required:false}">勤務時間2</div>
                </div>
                <div class="cell valign-center row-width-inputTime">
                    
                    <div>
                        <input class="row-cell-margin right-content-kaf007 inputTime-kaf007" id="inpStartTime2" data-bind="ntsTimeWithDayEditor: {name: '勤務時間２直行',                constraint:'TimeWithDayAttr',                value: workChange().workTimeStart2}" />

                    </div>
                </div>
                
                <div style="margin-left: 90px;width: auto !important;display: inline-block;" class="cell valign-center row-width-inputTime">
                    <div class="row-cell-margin">
                        
                        <label class="valign-center link-label-kaf007">~</label>
                        <input id="inpEndTime2" class="right-content-kaf007 inputTime-kaf007" data-bind="ntsTimeWithDayEditor: {name: '勤務時間２直帰',                 constraint:'TimeWithDayAttr',                value: workChange().workTimeEnd2}" />
                    </div>
                </div>
            </div>
            
        </div>
        
        <div class="valign-center control-group-kaf007" data-bind="if: false">
            <div class="table">
                <div class="cell valign-center">
                    <div class="lblTitle-kaf007" data-bind="ntsFormLabel: {required:false}">休憩時間</div>
                </div>
                <div class="cell valign-center row-width-inputTime">
                    
                    <div>
                        <input class="row-cell-margin right-content-kaf007 inputTime-kaf007" id="breakTimeStart1" data-bind="ntsTimeWithDayEditor: {name: '開始時刻',                constraint:'TimeWithDayAttr',                value: workChange().breakTimeStart1,                enable: $parent.editable}" />

                    </div>
                </div>
                
                <div class="cell valign-center row-width-inputTime">
                    <div class="row-cell-margin">
                        
                        <label class="valign-center link-label-kaf007">~</label>
                        <input class="right-content-kaf007 inputTime-kaf007" id="breakTimeEnd1" data-bind="ntsTimeWithDayEditor: {name: '終了時刻',                 constraint:'TimeWithDayAttr',                value: workChange().breakTimeEnd1,                enable: $parent.editable}" />
                    </div>
                </div>
            </div>
        </div>
        
        <div class="valign-center control-group-kaf007" data-bind="visible:$parent.showExcludeHoliday()">
            <div class="table">
                <div class="cell valign-center">
                    <div class="lblTitle-kaf007" data-bind="ntsFormLabel: {required:false}">休日に関して</div>
                </div>
                <div class="cell valign-center">
                    
                    
                    <div>
                        <div data-bind="ntsCheckBox: { checked: $parent.excludeHolidayAtr ,                text: 'スケジュールが休日の場合は除く',               enable: $parent.editable }"></div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="valign-center control-group-kaf007 overlay-reason-kaf007" data-bind="if: $parent.typicalReasonDisplayFlg">
            
            <div class="lblTitle-kaf007" data-bind="ntsFormLabel:{}">定型理由</div>
            
            <div id="combo-box" data-bind="ntsComboBox: {         name: '#[KAF007_26]',         options: $parent.reasonCombo,         optionsValue: 'reasonId',         optionsText: 'reasonName',         value: $parent.selectedReason,         enable: $parent.editable,         columns: [{ prop: 'reasonName', length: 10 }]}">
            </div>
        </div>
        
        <div class="valign-center control-group-kaf007" data-bind="if: $parent.showReasonText()">
            
            <div class="lblTitle-kaf007" data-bind="ntsFormLabel: {}">申請理由</div>
            
            <div id="multiline-kaf007">
                <textarea id="inpReasonTextarea" data-bind="ntsMultilineEditor: {value: $parent.multilContent,               option: {                resizeable: false,                               width: '450',                               textalign: 'left'                              },                             required : false,                              name: '#[KAF007_29]',                             enable: $parent.displayAppReasonContentFlg }"></textarea>
            </div>
        </div>
    </div>
    
    <div class="right-contents-kaf007" data-bind="visible: $parent.showRightContent()  ">
        <div class="panel panel-gray-bg panel-kaf007 panel-frame" data-bind="with: $parent.recordWorkInfo">
            
            <label>実績内容</label> <br />
            
            <div class="valign-center control-group-kaf007">
                <div class="table">
                    <div class="cell valign-center">
                        <label class="label-right">日付</label>
                    </div>
                    <div class="cell valign-center colon-margin">:</div>
                    <div class="cell valign-center">
                        <label data-bind="text : appDate"> </label>
                        
                    </div>
                </div>
                <br />
            </div>
            
            <div class="valign-center control-group-kaf007">
                <div class="table">
                    <div class="cell valign-center">
                        <label class="label-right">勤務種類</label>
                    </div>
                    <div class="cell valign-center colon-margin">:</div>
                    <div class="cell valign-center">
                        <label data-bind="text : workTypeCode"> </label> <label data-bind="text : workTypeName"></label>
                    </div>
                </div>
            </div>
            
            <div class="valign-center control-group-kaf007">
                <div class="table">
                    <div class="cell valign-center">
                        <label class="label-right">勤務時間帯</label>
                    </div>
                    <div class="cell valign-center colon-margin">:</div>
                    <div class="cell valign-center">
                        <label data-bind="text : workTimeCode"> </label> <label data-bind="text : workTimeName"></label>
                    </div>
                </div>
                <br />
            </div>
            
            <div class="valign-center control-group-kaf007">
                <div class="table">
                    <div class="cell valign-center">
                        <label class="label-right">勤務時間</label>
                    </div>
                    <div class="cell valign-center colon-margin">:</div>
                    <div class="cell valign-center">
                        <label data-bind="text : $parents[1].convertIntToTime(startTime1())">
                        </label>
                        
                        <label data-bind="visible: startTime1() &gt; 0" class="valign-center">~</label>
                        <label data-bind="text : $parents[1].convertIntToTime(endTime1())"></label>
                    </div>
                </div>
            </div>
            
            <div class="valign-center control-group-kaf007">
                <div class="table">
                    <div class="cell valign-center">
                        <label class="label-right">勤務時間2</label>
                    </div>
                    <div class="cell valign-center colon-margin">:</div>
                    <div class="cell valign-center">
                        <label data-bind="text : $parents[1].convertIntToTime(startTime2())">
                        </label>
                        
                        <label data-bind="visible: startTime2() &gt; 0" class="valign-center">~</label>
                        <label data-bind="text : $parents[1].convertIntToTime(endTime2())"></label>
                    </div>
                </div>
            </div>
            
            <div class="valign-center control-group-kaf007">
                <div class="table">
                    <div class="cell valign-center">
                        <label class="label-right">休憩時間１</label>
                    </div>
                    <div class="cell valign-center colon-margin">:</div>
                    <div class="cell valign-center">
                        <label data-bind="text : $parents[1].convertIntToTime(breakTimeStart())">
                        </label>
                        
                        <label data-bind="visible: breakTimeStart() &gt; 0" class="valign-center">~</label> <label data-bind="text : $parents[1].convertIntToTime(breakTimeEnd())"></label>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div><!-- /ko --><!-- ko if: appType() == 6 -->
            <div><div data-bind="let: { $vm: $data }">
    <div data-bind="if: screenModeNew()&amp;&amp;indicationOvertimeFlg()">
        <div class="cf valign-center control-group" data-bind="style: {padding: screenModeNew()== true ? '0px 10px 15px 12px' : '15px 10px 15px 12px'}" style="margin-left: -2px; background: #f8efd4;width: 778px;">
                        
            <div class="pull-left">時間外労働　：</div>
            <div class="pull-left">
                <table id="kaf010_overtimeAgreement_table">
                    <colgroup>
                        <col width="85px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                    </colgroup>
                    <thead>
                        <tr>
                            
                            <th class="kaf010_overtimeAgreement_header">年月</th>
                                                    
                            <th class="kaf010_overtimeAgreement_header">36時間</th>
                                                    
                            <th class="kaf010_overtimeAgreement_header">実績</th>
                                
                            <th class="kaf010_overtimeAgreement_header" style="display: none">申請</th>
                            
                            <th class="kaf010_overtimeAgreement_header" style="display: none">合計</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: overtimeWork">
                        <tr>
                                                    
                            <td data-bind="text: yearMonth, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: limitTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: actualTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td style="display: none" data-bind="text: appTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td style="display: none" data-bind="text: totalTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>      
    </div>
    <div data-bind="if: (!screenModeNew())&amp;&amp;indicationOvertimeFlg()">
        <div class="cf valign-center control-group" data-bind="style: {padding: screenModeNew()== true ? '0px 10px 15px 12px' : '15px 10px 15px 12px'}" style="margin-left: -2px; background: #f8efd4;width: 800px;">
                        
            <div class="pull-left">時間外労働　：</div>
            <div class="pull-left">
                <table id="kaf005_overtimeAgreement_table">
                    <colgroup>
                        <col width="85px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                        <col width="70px" />
                    </colgroup>
                    <thead>
                        <tr>
                            
                            <th class="kaf005_overtimeAgreement_header">年月</th>
                                                    
                            <th class="kaf005_overtimeAgreement_header">36時間</th>
                                                    
                            <th class="kaf005_overtimeAgreement_header">実績</th>
                                
                            <th class="kaf005_overtimeAgreement_header">申請</th>
                            
                            <th class="kaf005_overtimeAgreement_header">合計</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: overtimeWork">
                        <tr>
                                                    
                            <td data-bind="text: yearMonth, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: limitTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                                                    
                            <td data-bind="text: actualTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td data-bind="text: appTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                            
                            <td data-bind="text: totalTime, style: { 'background-color': backgroundColor, 'color': textColor }"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>      
    </div>
    
    <div class="left-contents">
        <div class="valign-center control-group" data-bind="visible: !employeeFlag()">
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">申請者</div>
            
            <label class="lblEmployeeName fix-margin-left-emp-kaf010" data-bind="text : employeeName"></label>
        </div>
        <div class="cf valign-top control-group" data-bind="visible: employeeFlag()">
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">申請者</div>
            
            <div id="list-box" class="fix-margin-left" data-bind="ntsListBox: {        options: employeeList,        optionsValue: 'id',        optionsText: 'name',        multiple:false,        value: selectedEmplCodes,        enable: true,        rows: 3,        columns: [         { key: 'name',length: 11}        ]}">
            </div>
        </div>
        <div class="cf valign-center control-group" data-bind="visible: employeeFlag()">
            <div class="cm-column"></div>
            
            <label style="margin-left: 139px;" data-bind="text : totalEmployee"></label>
        </div>
        <div class="valign-center control-group" data-bind="if: displayPrePostFlg">
            
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{ required: true }">事前事後区分</div>
            <div data-bind="if: screenModeNew" style="display: inline-block;">
                
                <div tabindex="5" class="cf fix-margin-left" id="kaf010-pre-post-select" data-bind="ntsSwitchButton: {name: '事前事後スイッチ',          options:ko.observableArray([                        { prePostCode: 0, prePostName: '事前' },                        { prePostCode: 1, prePostName: '事後' }                        ]),          optionsValue: 'prePostCode',          optionsText: 'prePostName',          value: prePostSelected,          enable: prePostEnable,          required: true }">
                </div>
            </div>
            <div data-bind="if: !screenModeNew()" style="display: inline-block;">
                <span class="label fix-margin-left-kaf010" data-bind="text: prePostSelected()==0 ? '事前' : '事後'"></span>
            </div>
        </div>
        <div class="valign-center control-group">
            
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{required: true}">申請日</div>
            <div data-bind="if: screenModeNew" style="display: inline-block;">
                
                <div tabindex="6" id="inputdate" class="fix-margin-left" data-bind="ntsDatePicker: {name: '申請日',required:true, value: appDate, dateFormat: 'YYYY/MM/DD',enable: enbAppDate}, acceptJapaneseCalendar: true"></div>
            </div>
            <div data-bind="if: !screenModeNew()" style="display: inline-block;">
                <span class="label fix-margin-left-kaf010" data-bind="text: appDate"></span>
            </div>
        </div>
        <div>
            
            <div class="table">
                <div class="cell valign-center" style="width: 105px;">
                    
                    <div class="valign-center control-group" data-bind="ntsFormLabel:{required: true}">勤務種類</div>
                    <BR />
                    
                    <div class="valign-center control-group" data-bind="ntsFormLabel:{required: true}">就業時間帯</div>
                </div>
                <div class="cell valign-center">
                    
                    <button tabindex="7" class="workSelect" data-bind="enable: $vm.editable,click : openDialogKdl003">選択</button>
                </div>
                <div class="cell valign-center">
                    <div id="kaf010-workType-workTime-div" style="padding-right: 10px;margin-left: 10px;">
                        
                        <label class="lblWorkTypeCd" data-bind="text: workTypeCd"></label>
                        
                        <LABEL data-bind="text: workTypeName"></LABEL> <BR />
                        
                        <label class="lblSiftCd" data-bind="text: siftCD"></label>
                        
                        <LABEL data-bind="text: siftName"></LABEL>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="valign-center control-group">
            <div class="table">
                <div class="cell valign-top cm-column2">
                    
                    <div class="lblTitle" data-bind="ntsFormLabel: {required: true}" style="margin-right: 16px">勤務時間</div>
                </div>
                <div class="cell valign-center">
                    <div>
                        
                        <input tabindex="8" class="row-cell-margin inputTime-kaf010 right-content" id="inpStartTime1" data-bind="ntsTimeWithDayEditor: {          name: '#[KAF010_333]',          constraint:'TimeWithDayAttr',         value: timeStart1,          enable: $vm.editable,          readonly: false,          required: true }" />
                        
                        <label class="valign-center link-label-kaf010">~</label>
                    </div>
                </div>
                <div class="cell valign-center">
                    <div class="row-cell-margin">
                        
                        <input tabindex="9" id="inpEndTime1" class="right-content inputTime-kaf010" data-bind="ntsTimeWithDayEditor: {         name: '#[KAF010_334]',          constraint:'TimeWithDayAttr',          value: timeEnd1,          enable: $vm.editable,          readonly: false,          required: true}" />
                    </div>
                </div>
            </div>
        </div>
        <div class="valign-center control-group" data-bind="if: screenModeNew()">
            <div class="table">
                <div class="cell valign-top cm-column2">
                <div class="lblTitle"></div>
                </div>
                
            </div>
        </div>
        
        <div class="valign-center control-group" data-bind="if: !screenModeNew()">
            <div class="table" style="margin-bottom: 9px;">
                <div class="cell valign-top cm-column2">
                    <div class="lblTitle"></div>
                </div>
                <lable class="label fix-margin-left" data-bind="text: goSelected1()==0 ? '直行しない' : '直行する'"></lable>
                <label class="valign-center link-label-kaf010b"></label>    
                <lable class="label fix-margin-left" data-bind="text: backSelected1()==0 ? '直帰しない' : '直帰する'"></lable>
            </div>
        </div>
        
        <div data-bind="visible : false">
            <div class="valign-center control-group" data-bind="if: useMulti()">
                <div class="table" data-bind="if: displayCaculationTime">
                    <div class="cell valign-top cm-column2">
                        <div class="lblTitle" data-bind="ntsFormLabel: {required: false}" style="margin-right: 16px">勤務時間2</div>
                    </div>
                    <div class="cell valign-center">
                        <div>
                            
                            <input tabindex="10" class="row-cell-margin inputTime-kaf010 right-content" id="inpStartTime2" data-bind="ntsTimeWithDayEditor: {name: '#[KAF010_335]',constraint:'TimeWithDayAttr',value: timeStart2}" />
                            
                            <label class="valign-center link-label-kaf005">~</label>
                        </div>
                    </div>
                    <div class="cell valign-center">
                        <div>
                            
                            <input tabindex="11" class="row-cell-margin inputTime-kaf010 right-content" id="inpEndTime2" data-bind="ntsTimeWithDayEditor: {name: '#[KAF010_336]', constraint:'TimeWithDayAttr',value: timeEnd2}" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="valign-center control-group">
                <div class="table">
                    <div class="cell valign-top cm-column2">
                                <div class="lblTitle"></div>
                    </div>
                    
                    <div tabindex="14" class="row-cell-margin right-content" data-bind="ntsSwitchButton: {name: '直行する・しないスイッチ',            options:ko.observableArray([                          { goCode2: 1, goName2: '直行する' },                          { goCode2: 0, goName2: '直行しない' }                          ]),            optionsValue: 'goCode2',            optionsText: 'goName2',            value: goSelected2}">
                    </div>
                    
                    <div tabindex="15" class="row-cell-margin right-content" data-bind="ntsSwitchButton: {name: '直帰する・しないスイッチ',            options:ko.observableArray([                          { backCode2: 1,  backName2: '直帰する' },                          { backCode2: 0,  backName2: '直帰しない' }                          ]),            optionsValue: 'backCode2',            optionsText: 'backName2',            value: backSelected2}">
                    </div>
                </div>
            </div>
        </div>
        <div class="cf valign-top control-group" data-bind="if: displayCaculationTime() &amp; restTimeDisFlg()">
                        
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {}">休憩時間</div>
            </div>
            <div class="table-time">
                <table id="fixed-table-holiday">
                    <colgroup>
                        <col width="109px" />
                        <col width="115px" />
                        <col width="115px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="ui-widget-header" rowspan="2"></th>
                                                    
                            <th class="ui-widget-header" rowspan="2">開始</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">終了</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: restTime"> 
                        <tr>
                                                    
                            <td class="header" data-bind="text: frameName"></td>
                                                    
                            <td><input tabindex="16" class="right-content" data-bind="attr: { id: 'restTimeStart_'+attendanceID()+'_'+frameNo()},            ntsTimeWithDayEditor: {name: '#[KAF010_337]',           value: startTime,           constraint:'TimeWithDayAttr',           option: {width: '85px', timeWithDay: true},           enable: false}" /></td>
                            
                            <td><input tabindex="17" class="right-content" data-bind="attr: { id: 'restTimeEnd_'+attendanceID()+'_'+frameNo()},           ntsTimeWithDayEditor: {name: '#[KAF010_338]',           value: endTime,           constraint:'TimeWithDayAttr',           option: {width: '85px',           timeWithDay: true},           enable: false}" /></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div data-bind="if: displayCaculationTime">
                        
            <button data-bind="click: CaculationTime, enable: $vm.editable" tabindex="18" style="margin: 15px 0px 10px 290px;" class="caret-bottom">計算</button>
        </div>
        <div class="cf valign-top control-group" data-bind="if: prePostSelected() == 0">
                    
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {required : true}">休出時間</div>
            </div>  
            <div class="table-time">
                <table id="fixed-break_time-table-holiday-pre">
                    <colgroup>
                        <col width="110px" />
                        <col width="110px" />
                    </colgroup>
                    <thead>
                        <tr>
                                                    
                            <th class="ui-widget-header" rowspan="2"></th>
                                                    
                            <th class="ui-widget-header" rowspan="2">申請時間</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: breakTimes">
                        <tr>
                                                    
                            <td class="header" data-bind="attr: { id: 'overtimeHoursHeader_'+attendanceID()+'_'+frameNo()},text: frameName"></td>
                                                    
                            <td data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()}, style: { 'background-color' : color }"><input tabindex="19" class="right-content breakTimesCheck" data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()},style: { 'background-color' : color },         ntsTimeEditor: {          name: '#[KAF010_56]',           value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable() &amp;&amp; $vm.enableOvertimeInput() }" /></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="cf valign-top control-group" data-bind="if: prePostSelected() == 1">
                    
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {required : true}">休出時間</div>
            </div>  
            <div class="table-time">
                <table id="fixed-break_time-table-holiday">
                    <colgroup>
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                    </colgroup>
                    <thead>
                        <tr>
                                                    
                            <th class="ui-widget-header" rowspan="2"></th>
                                                    
                            <th class="ui-widget-header" rowspan="2">申請時間</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">事前申請</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">実績時間</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: breakTimes">
                        <tr>
                                                    
                            <td class="header" data-bind="attr: { id: 'overtimeHoursHeader_'+attendanceID()+'_'+frameNo()},text: frameName"></td>
                                                    
                            <td data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()}, style: { 'background-color' : color }"><input tabindex="19" class="right-content breakTimesCheck" data-bind="attr: { id: 'overtimeHoursCheck_'+attendanceID()+'_'+frameNo()},style: { 'background-color' : color },         ntsTimeEditor: {          name: '#[KAF010_56]',           value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable() &amp;&amp; $vm.enableOvertimeInput() }" /></td>
                                                    
                            <td class="right-content" data-bind="text: preAppTime"></td>
                                                    
                            <td class="right-content" data-bind="text: caculationTime"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="cf valign-top control-group cell" data-bind="visible : false">
                
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left" data-bind="ntsFormLabel: {required: false}">残業時間</div>
            </div>  
            <div class="table-time">
                <table id="fixed-overtime-hour-table-holiday">
                    <colgroup>
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="ui-widget-header" rowspan="2"></th>
                                                
                            <th class="ui-widget-header" rowspan="2">申請時間</th>
                             
                            <th class="ui-widget-header" rowspan="2">事前申請</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">実績時間</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: overtimeHours">
                        <tr>
                                                    
                            <td class="header" data-bind="attr: { id: 'overtimeHoursHeader_'+attendanceID()+'_'+frameNo()}, text: frameName"></td>
                                                    
                            <td data-bind="attr: { id: 'hoursCheck_'+attendanceID()+'_'+frameNo()}"><input tabindex="15" class="right-content" data-bind="attr: { id: 'hoursCheck_'+attendanceID()+'_'+frameNo()},          ntsTimeEditor: {          name: nameID ,          value: applicationTime,           option: {width: '80px'},           inputFormat: 'time',           mode: 'time',          constraint:'OvertimeAppPrimitiveTime',          enable: $vm.editable}" /></td>
                                                    
                            <td class="right-content" data-bind="text: preAppTime"></td>
                                                    
                            <td class="right-content" data-bind="text: caculationTime"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="cf valign-top control-group" data-bind="if: displayBonusTime, visible: false">
                        
            <div class="cm-column" style="display: inline-block;">
                <div class="lblTitle pull-left cm-column" data-bind="ntsFormLabel: {}">加給時間</div>
            </div>
            <div class="table-time">
                <table id="fixed-bonus_time-table-holiday">
                    <colgroup>
                        <col width="110px" />
                        <col width="110px" />
                        <col width="110px" />
                    </colgroup>
                    <thead>
                        <tr>
                                                    
                            <th class="ui-widget-header" rowspan="2"></th>
                                                    
                            <th class="ui-widget-header" rowspan="2">申請時間</th>
                                                    
                            <th class="ui-widget-header" rowspan="2">事前申請</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: bonusTimes">
                        <tr>
                                                                            
                            <td class="header" data-bind="text: frameName"></td>
                             
                            <td>
                            
                            <input tabindex="17" class="right-content" data-bind="ntsTimeEditor: {         name:itemName,         value: applicationTime,          option: {width: '80px'},          inputFormat: 'time',          mode: 'time',         constraint:'OvertimeAppPrimitiveTime',         enable: $vm.editable}" /></td>
                             
                            <td class="right-content" data-bind="text: preAppTime"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="valign-center control-group overlay-reason" data-bind="if: typicalReasonDisplayFlg">
                        
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{}">定型理由</div>
                        
            <div tabindex="23" class="combo-box" data-bind="ntsComboBox: {name: '#[KAF005_87]',          options: reasonCombo,         optionsValue: 'reasonId',         optionsText: 'reasonName',         value: selectedReason,         enable: $vm.editable,         columns: [{ prop: 'reasonName', length: 10 }]}">
            </div>
        </div>
        
        <div class="cf valign-top control-group" data-bind="if: isShowReason()">
                        
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">申請理由</div>
             
            <div id="multiline">
                <textarea tabindex="24" id="appReason" class="textarea-kaf010" data-bind="ntsMultilineEditor: {name: '#[KAF005_89]',          value: multilContent,         option: {          resizeable: false,                         width: '500',                         required: requiredReason()                        },                        enable:displayAppReasonContentFlg() &amp;&amp; $vm.editable()        }"></textarea>
            </div>
        </div>
        
        <div class="valign-center control-group overlay-reason" data-bind="if: displayDivergenceReasonForm, visible: false">
             
            <div class="lblTitle cm-column" data-bind="ntsFormLabel:{}">乖離定型理由</div>
             
            <div tabindex="25" class="combo-box" data-bind="ntsComboBox: {name: '#[KAF005_91]',          options: reasonCombo2,         optionsValue: 'reasonId',         optionsText: 'reasonName',         value: selectedReason2,         enable: $vm.editable,         columns: [{ prop: 'reasonName', length: 10 }]}">
            </div>
        </div>
        
        <div class="cf valign-top control-group" data-bind="if: displayDivergenceReasonInput, visible: false">
             
            <div class="lblTitle cm-column" data-bind="ntsFormLabel: {}">乖離理由</div>
             
            <div id="multiline"> 
                <textarea tabindex="26" id="divergenceReason" class="textarea-kaf010" data-bind="ntsMultilineEditor: {name: '#[KAF005_93]',          value: multilContent2,         option: {          resizeable: false,                         width: '500',                         required: requiredReason2()                        },                        enable: $vm.editable        }"></textarea>
            </div>
        </div>
    </div>
    
    <div class="right-contents panel" style="background-color: #E7E6E6;" data-bind="visible: false"> 
            <label data-bind="visible: referencePanelFlg" style="margin-left: 38px">実績の内容</label> 
            <BR data-bind="visible: referencePanelFlg" />
            <HR data-bind="visible: referencePanelFlg" style="width: 250px;" />
        <div class="panel panel-gray-bg panel-frame" data-bind="visible: referencePanelFlg" style="margin-top: 2px">
            <div class="control-group">
            
            <label>日付 :</label>
            
            <label data-bind="text : appDateReference"></label>
            </div>
            <div class="control-group">
            
            <label>勤務種類 :</label>
            
            <label data-bind="text : workTypeCodeReference"></label>
            
            <label data-bind="text : workTypeNameReference"></label>
            </div>
            <div class="control-group">
            
            <label>勤務時間帯 :</label>
            
            <label data-bind="text : siftCodeReference"></label>
            
            <label data-bind="text : siftNameReference"></label>
            </div>
            <div class="control-group">
            
            <label>勤務時間 :</label>
            
            <label data-bind="text : workClockFrom1To1Reference"></label>
            </div>
            <div class="control-group" data-bind="visible: displayWorkClockFrom2To2Reference">
            
            <label style="margin-left: 72px" data-bind="text : workClockFrom2To2Reference"></label>
            </div>
            <div class="control-group" data-bind="foreach: overtimeHoursReference">
                    
                    <label data-bind="text : frameName"></label>
                    
                    <label data-bind="text : applicationTime"></label>
                    <BR />
            </div>
            <div class="control-group">
            
            <label>就業時間外深夜 :</label>
            
            <label data-bind="text : overTimeShiftNightRefer"></label>
            </div>
            <div class="control-group">
            
            <label>ﾌﾚｯｸｽ超過時間 :</label>
            
            <label data-bind="text : flexExessTimeRefer"></label>
            </div>
        </div>
        <div class="panel panel-gray-bg panel-frame control-group" data-bind="visible: allPreAppPanelFlg()">
            
            <label>【事前申請】</label>
             <BR /> <BR /> 
            
            <div>
                    <label data-bind=" if: !(preAppPanelFlg())" style="margin-left: 7px">事前申請なし</label>
                    <div data-bind=" if: preAppPanelFlg()">
                        <div class="control-group">
                        
                        <label>日付 :</label>
                        
                        <label data-bind="text : appDatePre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>勤務種類 :</label>
                        
                        <label data-bind="text : workTypeCodePre"></label>
                        
                        <label data-bind="text : workTypeNamePre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>勤務時間帯 :</label>
                        
                        <label data-bind="text : siftCodePre"></label>
                        
                        <label data-bind="text : siftNamePre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>勤務時間 :</label>
                        
                        <label data-bind="text : workClockFrom1To1Pre"></label>
        
        
        
        
                        </div>
                        <div class="control-group" data-bind="visible: displayWorkClockFrom2To2Pre">
                        
                        <label style="margin-left: 72px" data-bind="text : workClockFrom2To2Pre"></label>
        
        
        
        
                        </div>
                        
                        <div class="control-group" data-bind="foreach: overtimeHoursPre">
                        
                        <label data-bind="text : frameName"></label>
                        
                        <label data-bind="text : applicationTime"></label>
                        <BR />
                        </div>
                        <div class="control-group">
                        
                        <label>就業時間外深夜 :</label>
                        
                        <label data-bind="text : overTimeShiftNightPre"></label>
                        </div>
                        <div class="control-group">
                        
                        <label>ﾌﾚｯｸｽ超過時間 :</label>
                        
                        <label data-bind="text : flexExessTimePre"></label>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>
            </div><!-- /ko --><!-- ko if: appType() == 10 -->
            <div><div id="container" data-bind="let: { $vm: $data }">
    <div id="content_left">
        <div class="div_line" id="number_Info">
            <div class="div_line_col_1">
                <label data-bind="text : '残数情報：'"></label>
            </div>
            <div class="div_line_col_2">
                <table id="fixed-table">
                    <colgroup>
                        <col width="100px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="ui-widget-header" style="text-align: center;">振休振休残数</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td data-bind="text:remainDays() +'日'"></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="div_line">
            <div class="div_line_col_1">
                <div data-bind="ntsFormLabel: {}">申請者</div>
            </div>
            <div class="div_line_col_2">
                <label data-bind="text:employeeName,visible:!screenModeNew()==true" class="text_line"></label>
            </div>
            <div class="div_line_col_2" data-bind="if:screenModeNew()==true">
                <label data-bind="text:employeeName,visible:!employeeList().length" class="text_line"></label>
                <div data-bind="if:employeeList().length">
                    <div id="list-box" data-bind="ntsListBox: {            options: employeeList ,            optionsValue: 'code',            optionsText: 'name',            multiple: false,            value: selectedEmployee ,            enable: true,            rows: 5 }"></div>
                    <div class="div_line" data-bind="text:totalEmployeeText"></div>
                </div>
            </div>

        </div>
        <div class="div_line" data-bind="if:displayPrePostFlg()!=0">
            <div class="div_line_col_1">
                <div data-bind="ntsFormLabel: {  }">事前事後区分</div>
            </div>
            <div class="div_line_col_2">
                <div data-bind="if:screenModeNew()==true">
                    <div id="swb_pre_post_type" class="cf" data-bind="ntsSwitchButton: {        name: '事前事後区分',        options: prePostTypes ,        optionsValue: 'code',        optionsText: 'text',        value: prePostSelectedCode ,        enable: enablePrepost(),        required:displayPrePostFlg()!=0         }         "></div>
                </div>
                <div data-bind="if:screenModeNew()==false">
                    <label class="text_line" data-bind="text:prePostText"></label>
                </div>
            </div>

        </div>
        <div class="div_line" data-bind="if:screenModeNew()==true">
            <div class="div_line_col_1">
                <div data-bind="ntsFormLabel: {  }">申請組み合わせ</div>
            </div>
            <div class="div_line_col_2">
                <div id="swb_app_com" class="cf" data-bind="ntsSwitchButton: {        options: appComItems ,        optionsValue: 'code',        optionsText: 'text',        value: appComSelectedCode ,        enable: true ,        required:true        }"></div>
            </div>
        </div>
        
        <div data-bind="if:appComSelectedCode() !=2" class="div_line">
            <div data-bind=" with : recWk" id="app_rec_container" class="div_line">
                <div id="app_rec_header" class="div_line app_header">
                    振出申請</div>

                <div class="div_line">
                    <hr style="float: none;" />
                </div>

                <div class="div_line">
                    <div class="div_line_col_1">
                        <div data-bind="ntsFormLabel: { required:true }">日付</div>
                    </div>
                    <div class="div_line_col_2" data-bind="if:$parent.screenModeNew()==true">
                        <div id="recDatePicker" data-bind="ntsDatePicker: {value: appDate ,valueFormat:'YYYY/MM/DD',required:true,name:'日付'}"></div>
                    </div>
                    <div data-bind="if:$parent.screenModeNew() ==false" class="div_line_col_2 app_date">
                        <label class="text_line" data-bind="text:appDate"></label>
                    </div>

                </div>

                <div class="div_line">
                    <div class="div_line_col_1">
                        <div data-bind="ntsFormLabel: { required:true }">勤務種類</div>
                    </div>
                    <div class="div_line_col_2">
                        <div class="kaf-011-combo-box" data-bind="ntsComboBox: {           name: '勤務種類',           options: wkTypes ,           optionsValue: 'workTypeCode' ,           visibleItemsCount: 5 ,           value: wkTypeCD ,           optionsText: 'name',           editable: false,           required:true,           enable: $parent.screenModeNew ,           columns: [            { prop: 'name', length: 10 },           ]}">
                        </div>

                    </div>
                </div>
                <div class="div_line">
                    <div class="div_line_col_1">
                        <div data-bind="ntsFormLabel: { }">就業時間帯</div>
                    </div>
                    <div class="div_line_col_2 working_hour">
                        <button id="recTimeBtn" data-bind=" click: openKDL003,enable:$parent.kdl003BtnEnable">選択</button>
                        <div data-bind="text:wkText"></div>
                    </div>
                </div>
                <div class="div_line">
                    <div class="div_line_col_1">
                        <div data-bind="ntsFormLabel: { }">勤務時間</div>
                    </div>
                    <div class="div_line_col_2 working_hour" data-bind="with: wkTime1 ">
                        <input id="recTime1Start" data-bind="ntsTimeWithDayEditor: { name: '勤務時間(開始)',         value: startTime , enable: $parents[1].drawalReqSet().permissionDivision()!=0 , readonly: false , required: true ,option: timeOption }" />
                        
                        <div>～</div>
                        <input id="recTime1End" data-bind="ntsTimeWithDayEditor: { name: '勤務時間(終了)',         value: endTime , enable: $parents[1].drawalReqSet().permissionDivision()!=0 , readonly: false , required: true ,option: timeOption }" />

                        
                    </div>
                </div>

                <div id="app_rec_des" class="div_line" data-bind="with:$parent.drawalReqSet">
                    <div class="div_line" data-bind="text:       pickUpComment      ,css: { 'bold': pickUpBold  },style:{color:pickUpLettleColor}"></div>
                </div>

            </div>
        </div>
        
        <div data-bind="if:appComSelectedCode() !=1">
            <div id="app_break_container" class="div_line" data-bind="with: absWk ">
                <div id="app_break_header" class="div_line app_header">
                    振休振休申請</div>

                <div class="div_line">
                    <hr style="float: none;" />
                </div>

                <div class="div_line">
                    <div class="div_line_col_1">
                        <div data-bind="ntsFormLabel: { required:true }">日付</div>
                    </div>

                    <div class="div_line_col_2" data-bind="if:$parent.screenModeNew()==true">
                        <div id="absDatePicker" data-bind="ntsDatePicker: {value: appDate ,valueFormat:'YYYY/MM/DD' ,required:true,name:'日付'}"></div>
                    </div>

                    <div data-bind="if:$parent.screenModeNew() ==false" class="div_line_col_2 app_date">
                        <label class="text_line" data-bind="text:appDate"></label>
                    </div>
                    <div data-bind="if:$parent.screenModeNew() ==false">
                        <div class="div_line_col_3">
                            
                            <button style="display: inline-block;" data-bind="click:openCDialog">振休振休日変更</button>
                            <div style="display: inline-block;" data-bind="if:wkType().workAtr() == 0">
                                <button data-bind="click:changeAbsDateToHoliday">休出変更</button>
                                <span class="caret-right caret-inline"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="div_line">
                    <div class="div_line_col_1">
                        <div data-bind="ntsFormLabel: { required:true }">勤務種類</div>
                    </div>
                    <div class="div_line_col_2">
                        <div class="kaf-011-combo-box" data-bind="ntsComboBox: {           name: '勤務種類',           options: wkTypes ,           optionsValue: 'workTypeCode' ,           visibleItemsCount: 5 ,           value: wkTypeCD ,           optionsText: 'name',           editable: false,           required:true,           enable: $parent.screenModeNew,           columns: [            { prop: 'name', length: 10 },           ]}">
                        </div>

                    </div>
                </div>
                <div class="div_line change_woking_hours" data-bind="if:showAbsWorkTimeZone()">
                    <div class="div_line">
                        <div data-bind="ntsCheckBox: { checked: changeWorkHoursType }">就業時間帯を変更する</div>
                    </div>
                    <hr style="width: 958px;" />
                    <div class="div_line">
                        <div class="div_line_col_1">
                            <div data-bind="ntsFormLabel: { }">就業時間帯</div>
                        </div>
                        <div class="div_line_col_2 working_hour">
                            <button id="absTimeBtn" data-bind=" enable: changeWorkHoursType ,click:openKDL003 ">選択</button>
                            <div data-bind="text:wkText"></div>
                        </div>
                    </div>
                    <div data-bind="if:showWorkingTime1()">
                        <div class="div_line">
                            <div class="div_line_col_1">
                                <div data-bind="ntsFormLabel: { }">勤務時間</div>
                            </div>
                            <div>
                                <div class="div_line_col_2 working_hour" data-bind="with: wkTime1 ">
                                    <input class="absWkingTime" data-bind="ntsTimeWithDayEditor: { name: '勤務時間(開始)',         value: startTime , enable:$parent.enableWkTime()==true , readonly: false , required: $parent.changeWorkHoursType ,option: timeOption }" />
                                    <div>～</div>
                                    <input class="absWkingTime" data-bind="ntsTimeWithDayEditor: { name: '勤務時間(終了)',         value: endTime , enable:$parent.enableWkTime()==true , readonly: false , required: $parent.changeWorkHoursType  ,option: timeOption}" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="app_break_des" class="div_line" data-bind="with:$parent.drawalReqSet">
                    <div class="div_line" data-bind="text:       deferredComment      ,css: { 'bold': deferredBold } ,style:{color:deferredLettleColor}"></div>
                </div>
            </div>
        </div>
        <div data-bind="if:appTypeSet().displayFixedReason() !=0">
            <div class="div_line">
                <div class="div_line_col_1">
                    <div data-bind="ntsFormLabel: { }">定型理由</div>
                </div>
                <div class="div_line_col_2">
                    <div id="reasonCombo" class="kaf-011-combo-box" data-bind="ntsComboBox: {           name: '定型理由',           options: appReasons ,           optionsValue: 'reasonID' ,           visibleItemsCount: 5 ,           value: appReasonSelectedID ,           optionsText: 'reasonTemp',           editable: false,           enable: kaf011ReasonIsEditable ,           columns:[            { prop: 'reasonTemp', length: 10 },           ]}">
                    </div>
                </div>
            </div>
        </div>
        <div data-bind="if:showAppReason()">
            <div class="div_line">
                
                <div class="div_line_col_1">
                    <div data-bind="ntsFormLabel: { constraint: 'AppReason' }">申請理由</div>
                </div>
                <div class="div_line_col_2">
                    <textarea style="height: 100px ;position: relative;" id="appReason" data-bind="ntsMultilineEditor: {          constraint: 'AppReason',          value: reason ,          enable: kaf011ReasonIsEditable ,          name: '申請理由',          option: {          resizeable: false ,                         width: '500'                         }                        }"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>
            </div><!-- /ko -->
                
                <div style="clear: both"></div>
                </div>
                <div id="appr">
        <div class="contents-area cf" data-bind="if: approvalRootState().length != 0">
            <div class="contents-area cf" style="padding-top: 20px;">
                <div id="label-1">
                    <div data-bind="ntsFormLabel: {}">承認者一覧</div>
                </div>
                <div id="table-1">
                    <table id="table-phase">
                        <thead>
                            <tr class="bg-green">
                                <th class="phase-app" style="width: 111px;"></th>
                                <th class="phase-app" style="width: 111px;"></th>
                                <th class="phase-app" style="width: 230px;">社員名（@はメールアドレス登録者）</th>
                                <th class="phase-app" style="width: 111px;">状況</th>
                                <th class="phase-app" style="width: 111px;">コメント</th>
                            </tr>
                        </thead>
                        <tbody data-bind="foreach: approvalRootState"><!-- ko foreach: $data.listApprovalFrame -->
                                <tr><!-- ko if: $index() === 0 -->
                                        <td data-bind="attr: {rowspan: $parent.listApprovalFrame().length}, text: 'フェーズ'+$parent.phaseOrder()" style="width: 111px;" class="bg-green"></td><!-- /ko -->
                                    <td data-bind="text: '承認者'+$data.frameOrder()" style="width: 111px; text-align: left; padding-left: 5px;" class="bg-green"></td>
                                    <td>
                                        <span class="limited-label" style="width: 230px; text-align: left; padding-left: 5px;" data-bind="text: ko.pureComputed(function(){             if($data.approvalAtrName() !='未承認'){              if($data.approverName().length &gt; 0){               if($data.approverMail().length &gt; 0){                return $data.approverName() + '(@)';               } else {                return $data.approverName();               }              } else {               if($data.representerMail().length &gt; 0){                return $data.representerName() + '(@)';               } else {                return $data.representerName();               }              }             } else {              var s = '';              _.forEach($data.listApprover(),function(data,index){               if(index != 0){                s = s + ',';               }               s = s + data.approverName();               if(data.approverMail().length &gt; 0){                s = s + '(@)';               }               if(data.representerName().length &gt; 0){                if(data.representerMail().length &gt; 0){                 s = s + '(' + data.representerName() + '(@))';                } else {                 s = s + '(' + data.representerName() + ')';                }               }               });              return s;             }            })">
                                        </span>
                                    </td>
                                    <td>
                                        <span class="limited-label" style="width: 111px;" data-bind="text: $data.approvalAtrName"></span>
                                    </td>
                                    <td>
                                        <span class="limited-label" style="width: 111px; text-align: left; padding-left: 5px;" data-bind="text: $data.approvalReason"></span>   
                                    </td>
                                </tr><!-- /ko -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
                </div>
            </div>`
        }
    }
}