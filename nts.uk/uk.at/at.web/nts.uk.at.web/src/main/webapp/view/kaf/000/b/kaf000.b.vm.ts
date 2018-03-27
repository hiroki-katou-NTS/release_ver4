module nts.uk.at.view.kaf000.b.viewmodel {
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import shrvm = nts.uk.at.view.kaf000.shr;
    import UserType = nts.uk.at.view.kaf000.shr.model.UserType;
    import ApprovalAtr = nts.uk.at.view.kaf000.shr.model.ApprovalAtr;
    import Status = nts.uk.at.view.kaf000.shr.model.Status;
    import Approver = nts.uk.at.view.kdl034.a.viewmodel.Approver;
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    export abstract class ScreenModel {
        // Metadata
        appID: KnockoutObservable<string>;
        appType: KnockoutObservable<number>;
        //listPhase
        listPhase: KnockoutObservableArray<shrvm.model.AppApprovalPhase> = ko.observableArray([]);
        //listPhaseID
        listPhaseID: Array<string>;
        //list appID 
        listReasonByAppID: KnockoutObservableArray<string> = ko.observableArray([]);

        /**InputCommonData
         * value obj 
         */
        reasonToApprover: KnockoutObservable<string> = ko.observable('');
        reasonOutputMess : string = nts.uk.resource.getText('KAF000_1');
        reasonOutputMessFull: KnockoutObservable<string> = ko.observable('');
        reasonOutputMessDealine : string = nts.uk.resource.getText('KAF000_2');
        reasonOutputMessDealineFull: KnockoutObservable<string> = ko.observable('');
        messageArea : KnockoutObservable<boolean> = ko.observable(true);
        reasonApp: KnockoutObservable<string> = ko.observable('');
        inputCommonData: KnockoutObservable<model.InputCommonData> = ko.observable(null);
        dataApplication: KnockoutObservable<model.ApplicationDto> = ko.observable(null);
        //application
        inputDetail: KnockoutObservable<model.InputGetDetailCheck>;
        //obj input
        //obj output message deadline
        outputMessageDeadline: KnockoutObservable<shrvm.model.OutputMessageDeadline> = ko.observable(null);
        //obj DetailedScreenPreBootModeOutput
        outputDetailCheck: KnockoutObservable<model.DetailedScreenPreBootModeOutput> = ko.observable(null);
        //obj InputCommandEvent
        inputCommandEvent: KnockoutObservable<model.InputCommandEvent>;
        //enable enableBefore
        enableBefore: KnockoutObservable<boolean> = ko.observable(true);
        //enable enableAfter
        enableAfter: KnockoutObservable<boolean> = ko.observable(true);
        //listAppID
        listAppMeta: Array<any>;
        //item InputCommandEvent
        appReasonEvent: KnockoutObservable<string> = ko.observable('');
        approvalList: Array<vmbase.AppApprovalPhase> = [];
        displayButtonControl: KnockoutObservable<model.DisplayButtonControl> = ko.observable(new model.DisplayButtonControl());

        approvalRootState: any = ko.observableArray([]);
        empEditable: KnockoutObservable<boolean> = ko.observable(true);

        displayApprovalButton: KnockoutObservable<boolean> = ko.observable(false);
        enableApprovalButton: KnockoutObservable<boolean> = ko.observable(false);
        displayDenyButton: KnockoutObservable<boolean> = ko.observable(false);
        enableDenyButton: KnockoutObservable<boolean> = ko.observable(false);
        displayRemandButton: KnockoutObservable<boolean> = ko.observable(false);
        enableRemandButton: KnockoutObservable<boolean> = ko.observable(false);
        displayReleaseButton: KnockoutObservable<boolean> = ko.observable(false);
        enableReleaseButton: KnockoutObservable<boolean> = ko.observable(false);
        displayUpdateButton: KnockoutObservable<boolean> = ko.observable(false);
        enableUpdateButton: KnockoutObservable<boolean> = ko.observable(false);
        displayDeleteButton: KnockoutObservable<boolean> = ko.observable(false);
        enableDeleteButton: KnockoutObservable<boolean> = ko.observable(false);
        displayCancelButton: KnockoutObservable<boolean> = ko.observable(false);
        enableCancelButton: KnockoutObservable<boolean> = ko.observable(false);
        displayApprovalLabel: KnockoutObservable<boolean> = ko.observable(false);
        displayDenyLabel: KnockoutObservable<boolean> = ko.observable(false);
        displayApprovalReason: KnockoutObservable<boolean> = ko.observable(false);
        enableApprovalReason: KnockoutObservable<boolean> = ko.observable(false);
        displayReturnReasonPanel: KnockoutObservable<boolean> = ko.observable(false);

        constructor(listAppMetadata: Array<shrvm.model.ApplicationMetadata>, currentApp: shrvm.model.ApplicationMetadata) {
            let self = this;
            //reason input event
            // Metadata
            self.listAppMeta = listAppMetadata;
            self.appType = ko.observable(currentApp.appType);
            self.appID = ko.observable(currentApp.appID);
            self.inputCommandEvent = ko.observable(new model.InputCommandEvent(0, self.appID(), self.appReasonEvent()));
            //application
            self.inputDetail = ko.observable(new model.InputGetDetailCheck(self.appID(), "2022/01/01"));

            if (self.appID() == self.listAppMeta[0].appID) {
                self.enableBefore(false);
            } else {
                self.enableBefore(true);
            }

            if (self.appID() == self.listAppMeta[self.listAppMeta.length - 1].appID) {
                self.enableAfter(false);
            } else {
                self.enableAfter(true);
            }
        }

        abstract update(): any;

        start(baseDate: any): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputDetail().baseDate = baseDate;
            let dfd = $.Deferred<any>();
            nts.uk.at.view.kaf000.b.service.getAppDataDate({
                appTypeValue: self.appType(),
                appDate: baseDate,
                isStartup: true,
                appID: self.appID()
            }).done((data) => {
                self.inputCommandEvent().version = data.applicationDto.version;
                self.dataApplication(data.applicationDto);
                self.appType(data.applicationDto.applicationType);
                self.approvalRootState(ko.mapping.fromJS(data.listApprovalPhaseStateDto)());
                self.displayReturnReasonPanel(!nts.uk.util.isNullOrEmpty(data.applicationDto.reversionReason));
                let deadlineMsg = data.outputMessageDeadline;
                if(!nts.uk.text.isNullOrEmpty(deadlineMsg.message)){
                    self.reasonOutputMessFull(self.reasonOutputMess + deadlineMsg.message);    
                }
                if(!nts.uk.text.isNullOrEmpty(deadlineMsg.deadline)){
                    self.reasonOutputMessDealineFull(self.reasonOutputMessDealine + deadlineMsg.deadline);
                }
                self.messageArea(deadlineMsg.chkShow);
                self.getDetailCheck(self.inputDetail());
                nts.uk.ui.block.clear();
                dfd.resolve();
            }).fail((res) => {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function(){
                    nts.uk.request.jump("com", "/view/ccg/008/a/index.xhtml"); 
                    nts.uk.ui.block.clear();
                });  
            });

            return dfd.promise();
        }   //end start

        setControlButton(
            userTypeValue: any, // phân loại người dùng
            approvalAtrValue: any, // trạng thái approval của Phase
            state: any, // trạng thái đơn
            canApprove: any,  // có thể bấm nút approval không true, false
            expired: any, // phân biệt thời hạn
            loginFlg: any // login có phải người viết đơn/ người xin hay k 
        ) {
            var self = this;
            self.displayApprovalButton((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPROVER)
                && (approvalAtrValue != ApprovalAtr.APPROVED));
            self.enableApprovalButton((state == Status.DENIAL || state == Status.NOTREFLECTED || state == Status.REMAND)
                && canApprove
                && !expired);

            self.displayDenyButton((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPROVER)
                && (approvalAtrValue != ApprovalAtr.DENIAL));
            self.enableDenyButton((state == Status.DENIAL || state == Status.WAITREFLECTION || state == Status.NOTREFLECTED || state == Status.REMAND)
                && canApprove
                && !expired);

            self.displayRemandButton((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPROVER));
            self.enableRemandButton(
                ((state == Status.DENIAL || state == Status.NOTREFLECTED || state == Status.REMAND)
                && canApprove
                && !expired) ||
                (state == Status.WAITREFLECTION 
                && canApprove
                && (approvalAtrValue == ApprovalAtr.APPROVED || approvalAtrValue == ApprovalAtr.DENIAL)
                && !expired)
            );

            self.displayReleaseButton((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPROVER));
            self.enableReleaseButton((state == Status.DENIAL || state == Status.WAITREFLECTION || state == Status.NOTREFLECTED || state == Status.REMAND)
                && canApprove
                && (approvalAtrValue == ApprovalAtr.APPROVED || approvalAtrValue == ApprovalAtr.DENIAL));

            self.displayUpdateButton((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPLICANT || userTypeValue == UserType.OTHER));
            self.enableUpdateButton(state == Status.NOTREFLECTED || state == Status.REMAND);

            self.displayDeleteButton((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPLICANT || userTypeValue == UserType.OTHER));
            self.enableDeleteButton(state == Status.NOTREFLECTED || state == Status.REMAND);

            self.displayCancelButton(loginFlg);
            self.enableCancelButton(state == Status.REFLECTED);

            self.displayApprovalLabel((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPROVER)
                && (approvalAtrValue == ApprovalAtr.APPROVED));

            self.displayDenyLabel((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPROVER)
                && (approvalAtrValue == ApprovalAtr.DENIAL));
            
            self.displayApprovalReason((userTypeValue == UserType.APPLICANT_APPROVER || userTypeValue == UserType.APPROVER));
            self.enableApprovalReason((state == Status.DENIAL || state == Status.WAITREFLECTION || state == Status.NOTREFLECTED || state == Status.REMAND)
                && canApprove
                && !expired);
        }
        
        //get all reason by app ID
        getAllReasonByAppID(appID: string) {
            let self = this;
            let dfd = $.Deferred<any>();
            service.getAllReasonByAppID(appID).done(function(data) {
                self.listReasonByAppID(data);
                if (self.listReasonByAppID().length > 0) {
                    self.reasonApp(self.listReasonByAppID()[0].toString());
                    if (self.listReasonByAppID().length > 1) {
                        self.reasonToApprover(self.listReasonByAppID()[1].toString());
                    }
                }
                dfd.resolve(data);
            }).fail(function(res: any) {
                dfd.reject();
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
            });;
            return dfd.promise();
        }
        //get detail check 
        getDetailCheck(inputGetDetail: any) {
            let self = this;
            let dfd = $.Deferred<any>();
            service.getDetailCheck(inputGetDetail).done(function(data) {
                self.setControlButton(
                    data.user,
                    data.approvalATR,
                    data.reflectPlanState,
                    data.authorizableFlags,
                    data.alternateExpiration,
                    data.loginInputOrApproval);
                dfd.resolve(data);
            }).fail(function(res: any) {
                dfd.reject();
                nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
            });
            return dfd.promise();
        }
        /**
         * btn before　←
         */
        btnBefore() {
            let self = this;
            var prevAppInfo = self.getPrevAppInfo();
            nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { 'listAppMeta': self.listAppMeta, 'currentApp': prevAppInfo });
        }

        private getPrevAppInfo(): shrvm.model.ApplicationMetadata {
            let self = this;
            let index = _.findIndex(self.listAppMeta, ["appID", self.appID()]);
            if (index > 0) {
                return new shrvm.model.ApplicationMetadata(self.listAppMeta[index - 1].appID, self.listAppMeta[index - 1].appType, self.listAppMeta[index - 1].appDate);
            }
            return null;
        }

        /**
         * btn after　→
         */
        btnAfter() {
            let self = this;
            var nextAppInfo = self.getNextAppInfo();
            nts.uk.request.jump("/view/kaf/000/b/index.xhtml", { 'listAppMeta': self.listAppMeta, 'currentApp': nextAppInfo });
        }

        private getNextAppInfo(): shrvm.model.ApplicationMetadata {
            let self = this;
            let index = _.findIndex(self.listAppMeta, ["appID", self.appID()]);
            if (index < self.listAppMeta.length - 1) {
                return new shrvm.model.ApplicationMetadata(self.listAppMeta[index + 1].appID, self.listAppMeta[index + 1].appType, self.listAppMeta[index + 1].appDate);
            }
            return null;
        }

        /**
         *  btn Approve　承認
         */
        btnApprove() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommonData(new model.InputCommonData(self.dataApplication(), self.reasonToApprover()));
            service.approveApp(self.inputCommonData()).done(function(data) {
                nts.uk.ui.dialog.alert({ messageId: 'Msg_220' }).then(function() {
                    if (!nts.uk.util.isNullOrUndefined(data)) {
                        nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [data] }).then(() => {
                            location.reload();
                        });
                    } else {
                        location.reload();
                    }
                });
            }).fail(function(res: any) {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { nts.uk.ui.block.clear(); });
            });
        }
        /**
        *  btn Deny　否認
        */
        btnDeny() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommonData(new model.InputCommonData(self.dataApplication(), self.reasonToApprover()));
            service.denyApp(self.inputCommonData()).done(function(data) {
                nts.uk.ui.dialog.alert({ messageId: 'Msg_222' }).then(function() {
                    if (!nts.uk.util.isNullOrUndefined(data)) {
                        nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [data] }).then(() => {
                            location.reload();
                        });
                    } else {
                        location.reload();
                    }
                });
            }).fail(function(res: any) {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { nts.uk.ui.block.clear(); });
            });
        }
        
        btnRemand(){
            var self = this;
            let command = self.convertToApproverList();
            setShared("KDL034_PARAM", command);
            nts.uk.ui.windows.sub.modal("/view/kdl/034/a/index.xhtml");     
        }

        /**
        *  btn Release //解除
        */
        btnRelease() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommonData(new model.InputCommonData(self.dataApplication(), self.reasonToApprover()));
            nts.uk.ui.dialog.confirm({ messageId: 'Msg_248' }).ifYes(function() {
                service.releaseApp(self.inputCommonData()).done(function() {
                    nts.uk.ui.dialog.info({ messageId: 'Msg_221' }).then(() => {
                        location.reload();
                    });
                }).fail(function(res: any) {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { nts.uk.ui.block.clear(); });
                });
            }).ifNo(() => {
                nts.uk.ui.block.clear();
            });
        }

        /**
         *  btn Registration　登録
         */
        btnRegistration() {

        }
        /**
         *  btn References 実績参照
         */
        btnReferences() {
            let self = this;
            // send (Cid,Eid,date) in screen KDL004
            //nts.uk.request.jump("/view/kdl/004/a/index.xhtml");
        }
        /**
         *  btn SendEmail メール送信
         */
        btnSendEmail() {
            let self = this;
            // send (Cid, appId , content, Eid, date) in screen KDL030
            //nts.uk.request.jump("/view/kdl/030/a/index.xhtml");
        }
        /**
         *  btn Delete 削除
         */
        btnDelete() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommandEvent(new model.InputCommandEvent(self.inputCommandEvent().version, self.appID(), self.appReasonEvent()));
            nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                service.deleteApp(self.inputCommandEvent()).done(function(data) {
                    nts.uk.ui.dialog.alert({ messageId: 'Msg_16' }).then(function() {
                        //kiểm tra list người xác nhận, nếu khác null thì show info 392
                        if (!nts.uk.util.isNullOrEmpty(data)) {
                            nts.uk.ui.dialog.info({ messageId: 'Msg_392', messageParams: [data] }).then(function() {
                                //self.setScreenAfterDelete();    
                            });
                        }
                        //                        else{
                        //                            //self.setScreenAfterDelete();
                        //                        }
                        nts.uk.request.jump("/view/cmm/045/a/index.xhtml");
                    });
                }).fail(function(res: any) {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                        nts.uk.request.jump("../test/index.xhtml");
                    });
                });
            }).ifNo(function() {
                nts.uk.ui.block.clear();
            });
        }
        setScreenAfterDelete() {
            let self = this;
            //lấy vị trí appID vừa xóa trong listAppID
            let index = _.findIndex(self.listAppMeta, ["appID", self.appID()]);
            if (index > -1 && index != self.listAppMeta.length - 1) {
                //xóa appID vừa xóa trong list
                self.appID(self.listAppMeta[index + 1].appID);
            }

            self.listAppMeta.splice(index, 1);
            //if list # null    
            if (self.listAppMeta.length == 0) {
                //nếu list null thì trả về màn hình mẹ
                nts.uk.request.jump("/view/cmm/045/a/index.xhtml");
            }

            if (self.listAppMeta.length == 1) {
                nts.uk.request.jump("/view/cmm/045/a/index.xhtml");
            }
            //nếu vị trí vừa xóa khác vị trí cuối
            if (index != self.listAppMeta.length) {
                //gán lại appId mới tại vị trí chính nó
                //self.btnAfter();
                nts.uk.request.jump("/view/cmm/045/a/index.xhtml");
            } else {
                //nếu nó ở vị trí cuối thì lấy appId ở vị trí trước nó
                //                self.btnBefore();
                nts.uk.request.jump("/view/cmm/045/a/index.xhtml");
            }

        }

        /**
         *  btn Cancel 
         */
        btnCancel() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommandEvent(new model.InputCommandEvent(self.inputCommandEvent().version, self.appID(), self.appReasonEvent()));
            nts.uk.ui.dialog.confirm({ messageId: 'Msg_249' }).ifYes(function() {
                service.cancelApp(self.inputCommandEvent()).done(function() {
                    nts.uk.ui.dialog.alert({ messageId: "Msg_224" }).then(() => {
                        location.reload();
                    });
                }).fail(function(res: any) {
                    if (res.optimisticLock == true) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function() {
                            location.reload();
                        });
                    } else {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { nts.uk.ui.block.clear(); });
                    }
                });
            }).ifNo(function() {
                nts.uk.ui.block.clear();
            });
        }
        
        convertToApproverList(): Array<Approver> {
            var self = this;
            let listApprover = [];
            listApprover.push(new Approver(self.appID(), self.employeeName(), null));
            _.forEach(ko.mapping.toJS(self.approvalRootState()), function(approvalPhase) {
                _.forEach(approvalPhase, function(approvalFrame) {
                    if((!nts.uk.util.isNullOrEmpty(approvalFrame.approverID))||(!nts.uk.util.isNullOrEmpty(approvalFrame.representerID))){
                        if(!nts.uk.util.isNullOrEmpty(approvalFrame.approverID)){
                            listApprover.push(new Approver(approvalFrame.approverID, approvalFrame.approverName, approvalFrame.phaseOrder));        
                        } else {
                            listApprover.push(new Approver(approvalFrame.representerID, approvalFrame.representerName, approvalFrame.phaseOrder));    
                        }      
                    }     
                });
            });
            return listApprover;
        }


    }

    export module model {
        //class OutputGetAllDataApp
        export class OutputGetAllDataApp {
            applicationDto: ApplicationDto;
            listOutputPhaseAndFrame: Array<OutputPhaseAndFrame>;
            constructor(applicationDto: ApplicationDto,
                listOutputPhaseAndFrame: Array<OutputPhaseAndFrame>) {
                this.applicationDto = applicationDto;
                this.listOutputPhaseAndFrame = listOutputPhaseAndFrame;
            }
        }//end class OutputGetAllDataApp


        //class Application 
        export class ApplicationDto {
            version: number;
            applicationID: string;
            prePostAtr: number;
            inputDate: string;
            enteredPersonSID: string;
            reversionReason: string;
            applicationDate: string;
            applicationReason: string;
            applicationType: number;
            applicantSID: string;
            reflectPlanScheReason: number;
            reflectPlanTime: string;
            reflectPlanState: number;
            reflectPlanEnforce: number;
            reflectPerScheReason: number;
            reflectPerTime: string;
            reflectPerState: number;
            reflectPerEnforce: number;
            startDate: string;
            endDate: string;
            listPhase: Array<shrvm.model.AppApprovalPhase>;
            constructor(
                version: number,
                applicationID: string,
                prePostAtr: number,
                inputDate: string,
                enteredPersonSID: string,
                reversionReason: string,
                applicationDate: string,
                applicationReason: string,
                applicationType: number,
                applicantSID: string,
                reflectPlanScheReason: number,
                reflectPlanTime: string,
                reflectPlanState: number,
                reflectPlanEnforce: number,
                reflectPerScheReason: number,
                reflectPerTime: string,
                reflectPerState: number,
                reflectPerEnforce: number,
                startDate: string,
                endDate: string,
                listPhase: Array<shrvm.model.AppApprovalPhase>) {
                this.version = version;
                this.applicationID = applicationID;
                this.prePostAtr = prePostAtr;
                this.inputDate = inputDate;
                this.enteredPersonSID = enteredPersonSID;
                this.reversionReason = reversionReason;
                this.applicationDate = applicationDate;
                this.applicationReason = applicationReason;
                this.applicationType = applicationType;
                this.applicantSID = applicantSID;
                this.reflectPlanScheReason = reflectPlanScheReason;
                this.reflectPlanTime = reflectPlanTime;
                this.reflectPlanState = reflectPlanState;
                this.reflectPlanEnforce = reflectPlanEnforce;
                this.reflectPerScheReason = reflectPerScheReason;
                this.reflectPerTime = reflectPerTime;
                this.reflectPerState = reflectPerState;
                this.reflectPerEnforce = reflectPerEnforce;
                this.startDate = startDate;
                this.endDate = endDate;
                this.listPhase = listPhase;
            }
        }//end class Application


        //class OutputPhaseAndFrame 
        export class OutputPhaseAndFrame {
            appApprovalPhase: shrvm.model.AppApprovalPhase;
            listApprovalFrame: Array<shrvm.model.ApprovalFrame>;
            constructor(
                appApprovalPhase: shrvm.model.AppApprovalPhase,
                listApprovalFrame: Array<shrvm.model.ApprovalFrame>) {
                this.appApprovalPhase = appApprovalPhase;
                this.listApprovalFrame = listApprovalFrame;
            }
        }//end class OutputPhaseAndFrame

        //class InputGetDetailCheck 
        export class InputGetDetailCheck {
            applicationID: string;
            baseDate: string;
            constructor(applicationID: string,
                baseDate: string) {
                this.applicationID = applicationID;
                this.baseDate = baseDate;

            }
        }//end class InputGetDetailCheck


        //class DetailedScreenPreBootModeOutput
        export class DetailedScreenPreBootModeOutput {
            user: number;
            reflectPlanState: number;
            authorizableFlags: boolean;
            approvalATR: number;
            alternateExpiration: boolean;
            loginInputOrApproval: boolean;
            constructor(user: number,
                reflectPlanState: number,
                authorizableFlags: boolean,
                approvalATR: number,
                alternateExpiration: boolean,
                loginInputOrApproval: boolean) {
                this.user = user;
                this.reflectPlanState = reflectPlanState;
                this.authorizableFlags = authorizableFlags;
                this.approvalATR = approvalATR;
                this.alternateExpiration = alternateExpiration;
                this.loginInputOrApproval = loginInputOrApproval;
            }
        }//end class DetailedScreenPreBootModeOutput



        //class InputApprove
        export class InputCommonData {
            applicationDto: ApplicationDto;
            memo: string;
            constructor(applicationDto: ApplicationDto, memo: string) {
                this.applicationDto = applicationDto;
                this.memo = memo;
            }
        }

        //class InputCommandEvent
        export class InputCommandEvent {
            version: number;
            appId: string;
            applicationReason: string;
            constructor(version: number, appId: string, applicationReason: string) {
                this.version = version;
                this.appId = appId;
                this.applicationReason = applicationReason;
            }
        }


        export class DisplayButtonControl {
            // B1-8 Update
            displayUpdate: KnockoutObservable<boolean>;
            enableUpdate: KnockoutObservable<boolean>;

            // B1-4 Approval
            displayApproval: KnockoutObservable<boolean>;
            enableApproval: KnockoutObservable<boolean>;

            // B1-5 Deny
            displayDeny: KnockoutObservable<boolean>;
            enableDeny: KnockoutObservable<boolean>;

            // B1-6 Remand
            displayRemand: KnockoutObservable<boolean>;
            enableRemand: KnockoutObservable<boolean>;

            // B1-7 Release
            displayRelease: KnockoutObservable<boolean>;
            enableRelease: KnockoutObservable<boolean>;

            // B1-12 Delete
            displayDelete: KnockoutObservable<boolean>;
            enableDelete: KnockoutObservable<boolean>;

            // B1-13 Cancel
            displayCancel: KnockoutObservable<boolean>;
            enableCancel: KnockoutObservable<boolean>;

            // B1-14 ApprovalLabel
            displayApprovalLabel: KnockoutObservable<boolean>;

            // B1-15 DenyLabel
            displayDenyLabel: KnockoutObservable<boolean>;

            // B3-1
            displayReturnReasonPanel: KnockoutObservable<boolean>;

            // B4-1 
            displayReturnReasonLabel: KnockoutObservable<boolean>;

            // B4-2 
            displayReturnReason: KnockoutObservable<boolean>;
            enableReturnReason: KnockoutObservable<boolean>;
            displayMessageArea: KnockoutObservable<boolean>;
            enableMessageComment: KnockoutObservable<boolean>;
            constructor() {
                this.displayUpdate = ko.observable(true);
                this.enableUpdate = ko.observable(false);
                this.displayApproval = ko.observable(true);
                this.enableApproval = ko.observable(false);
                this.displayDeny = ko.observable(true);
                this.enableDeny = ko.observable(false);
                this.displayRemand = ko.observable(true);
                this.enableRemand = ko.observable(false);
                this.displayRelease = ko.observable(true);
                this.enableRelease = ko.observable(false);
                this.displayDelete = ko.observable(true);
                this.enableDelete = ko.observable(false);
                this.displayCancel = ko.observable(true);
                this.enableCancel = ko.observable(false);
                this.displayApprovalLabel = ko.observable(true);
                this.displayDenyLabel = ko.observable(true);
                this.displayReturnReasonPanel = ko.observable(true);
                this.displayReturnReasonLabel = ko.observable(true);
                this.displayReturnReason = ko.observable(true);
                this.enableReturnReason = ko.observable(true);
                this.displayMessageArea = ko.observable(true);
                this.enableMessageComment = ko.observable(false);

            }
        }

    }
}