module nts.uk.at.view.kaf000.b.viewmodel {
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import shrvm = nts.uk.at.view.kaf000.shr;
    import UserType = nts.uk.at.view.kaf000.shr.model.UserType;
    import ApprovalAtr = nts.uk.at.view.kaf000.shr.model.ApprovalAtr;
    import Status = nts.uk.at.view.kaf000.shr.model.Status;
    import Approver = nts.uk.at.view.kdl034.a.viewmodel.Approver;
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export abstract class ScreenModel {
        displayGoback: KnockoutObservable<boolean> = ko.observable(false);
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
        reasonOutputMess: string = nts.uk.resource.getText('KAF000_1');
        reasonOutputMessFull: KnockoutObservable<string> = ko.observable('');
        reasonOutputMessDealine: string = nts.uk.resource.getText('KAF000_2');
        reasonOutputMessDealineFull: KnockoutObservable<string> = ko.observable('');
        messageArea: KnockoutObservable<boolean> = ko.observable(true);
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
        version: number = 0;
        editable: KnockoutObservable<boolean> = ko.observable(false);
        
        constructor(listAppMetadata: Array<shrvm.model.ApplicationMetadata>, currentApp: shrvm.model.ApplicationMetadata) {
            let self = this;
            //reason input event
            // Metadata
            nts.uk.characteristics.restore("AppListExtractCondition").done((obj) => {
                if(nts.uk.util.isNullOrUndefined(obj)){
                    self.displayGoback(false);        
                } else {
                    self.displayGoback(true);                 
                }
            }).fail(()=>{
                self.displayGoback(false);      
            });
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
        
        abstract getBoxReason(): any;
        
        abstract getAreaReason(): any;
        
        abstract resfreshReason(appReason: string): any;

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
                //write log
                let appType = data.applicationDto.applicationType;
                if(appType != 0){
                    let paramLog = {programId: 'KAF000',
                                    screenId: 'B', 
                                    queryString: 'apptype='+appType};
                    service.writeLog(paramLog);
                }
                self.inputCommandEvent().version = data.applicationDto.version;
                self.version = data.applicationDto.version;
                self.dataApplication(data.applicationDto);
                self.appType(data.applicationDto.applicationType);
                self.approvalRootState(ko.mapping.fromJS(data.listApprovalPhaseStateDto)());
                self.displayReturnReasonPanel(!nts.uk.util.isNullOrEmpty(data.applicationDto.reversionReason));
                if(self.displayReturnReasonPanel()){
                    let returnReason = data.applicationDto.reversionReason;
                    $("#returnReason").html(returnReason.replace(/\n/g,"\<br/>"));
                }
                let deadlineMsg = data.outputMessageDeadline;
                if (!nts.uk.text.isNullOrEmpty(deadlineMsg.message)) {
                    self.reasonOutputMessFull(self.reasonOutputMess + deadlineMsg.message);
                }
                if (!nts.uk.text.isNullOrEmpty(deadlineMsg.deadline)) {
                    self.reasonOutputMessDealineFull(self.reasonOutputMessDealine + deadlineMsg.deadline);
                }
                self.messageArea(deadlineMsg.chkShow);
                self.getDetailCheck(self.inputDetail());
                nts.uk.ui.block.clear();
                dfd.resolve();
            }).fail((res) => {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() {
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
                self.editable(data.initMode==0?false:true);
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
            nts.uk.request.jump("/view/kaf/000/b/index.xhtml", {
                'listAppMeta': _.map(self.listAppMeta, x => { return x.appID }),
                'currentApp': prevAppInfo.appID
            });
        }

        private getPrevAppInfo(): shrvm.model.ApplicationMetadata {
            let self = this;
            let index = _.findIndex(self.listAppMeta, x => { return x.appID == self.appID() });
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
            nts.uk.request.jump("/view/kaf/000/b/index.xhtml", {
                'listAppMeta': _.map(self.listAppMeta, x => { return x.appID }),
                'currentApp': nextAppInfo.appID
            });
        }

        private getNextAppInfo(): shrvm.model.ApplicationMetadata {
            let self = this;
            let index = _.findIndex(self.listAppMeta, x => { return x.appID == self.appID() });
            if (index < self.listAppMeta.length - 1) {
                return new shrvm.model.ApplicationMetadata(self.listAppMeta[index + 1].appID, self.listAppMeta[index + 1].appType, self.listAppMeta[index + 1].appDate);
            }
            return null;
        }

        /**
         *  btn Approve　承認
         */
        btnApprove() {
            // self.getBoxReason();
            nts.uk.ui.block.invisible();
            let self = this, 
                inputCommonData = {
                    applicationDto: self.dataApplication(),
                    memo: self.reasonToApprover(),
                    comboBoxReason: self.getBoxReason(),
                    textAreaReason: self.getAreaReason()
                };
            // self.inputCommonData(new model.InputCommonData(self.dataApplication(), self.reasonToApprover()));
            if(!appcommon.CommonProcess.checklenghtReason(inputCommonData.comboBoxReason+":"+inputCommonData.textAreaReason,"#appReason")){
                return;
            }
            let approveCmd = self.appType() != 10 ? inputCommonData : self.getHolidayShipmentCmd(self.reasonToApprover());
            service.approveApp(approveCmd, self.appType()).done(function(data) {
                self.resfreshReason(data.appReason);
                self.sendMail('Msg_220', data);
            }).fail(function(res: any) {
                self.showError(res);
            });
        }
        /**
        *  btn Deny　否認
        */
        btnDeny() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommonData(new model.InputCommonData(self.dataApplication(), self.reasonToApprover()));
            let denyCmd = self.appType() != 10 ? self.inputCommonData() : self.getHolidayShipmentCmd(self.reasonToApprover());
            service.denyApp(denyCmd, self.appType()).done(function(data) {
                self.sendMail('Msg_222', data);
            }).fail(function(res: any) {
                self.showError(res);
            });
        }

        sendMail(msg, data) {
            let self = this,
            vm: nts.uk.at.view.kaf011.b.viewmodel.ScreenModel = __viewContext['viewModel'];
            if (data.processDone) {
                nts.uk.ui.dialog.info({ messageId: msg }).then(function() {
                    if (data.autoSendMail) {
                        appcommon.CommonProcess.displayMailResultKAF000(data);
                    }
                });
                if(!nts.uk.util.isNullOrEmpty(data.reflectAppId)){//TH goi xu ly phan anh
                    let lstIdRef = [data.reflectAppId];
                    if(self.appType() == 10){//kaf011
                        let absAppID = vm.absWk().appID();
                        let recAppID = vm.recWk().appID();
                        if(data.reflectAppId == absAppID && recAppID != null){
                            lstIdRef.push(recAppID);
                        }
                        if(data.reflectAppId == recAppID && absAppID != null){
                            lstIdRef.push(absAppID);
                        }
                    }
                    service.reflectAppSingle(lstIdRef).done(function(){
                        self.start(moment.utc().format("YYYY/MM/DD")).done(()=>{
                            nts.uk.ui.block.clear();        
                        });
                    }).fail(() => {
                        self.start(moment.utc().format("YYYY/MM/DD")).done(() => {
                            nts.uk.ui.block.clear();
                        });
                    });;
                }else{
                    self.start(moment.utc().format("YYYY/MM/DD")).done(()=>{
                            nts.uk.ui.block.clear();        
                        });
                }
                
            } else {
                nts.uk.ui.block.clear();
            }
            
        }

       callReflect(data) {
            let self = this;
            service.reflectAppSingle(data).always(()=> self.reloadPage());
        }

        reloadPage() {
            let self = this;
            self.start(moment.utc().format("YYYY/MM/DD")).done(() => {
                nts.uk.ui.block.clear();
            });
        }

        btnRemand() {
            let self = this;
            let command = { appID: self.getAppId(), version: self.dataApplication().version };
            setShared("KDL034_PARAM", command);
            nts.uk.ui.windows.sub.modal("/view/kdl/034/a/index.xhtml").onClosed(() => {
                self.start(moment.utc().format("YYYY/MM/DD")).done(()=>{
                    nts.uk.ui.block.clear();        
                });
            });
        }

        getAppId() {
            let self = this,
                isHolidayShipmentApp = self.appType() == 10,
                resultIds = [];

            if (isHolidayShipmentApp) {
                let vm: nts.uk.at.view.kaf011.b.viewmodel.ScreenModel = __viewContext['viewModel'],
                    recID = vm.recWk().appID(),
                    absID = vm.absWk().appID();
                if (recID) {
                    resultIds.push(recID);
                }
                if (absID) {
                    resultIds.push(absID);
                }
            } else {
                resultIds.push(self.appID());
            }
            return resultIds;

        }

        /**
        *  btn Release //解除
        */
        btnRelease() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommonData(new model.InputCommonData(self.dataApplication(), self.reasonToApprover()));
            let releaseCmd = self.appType() != 10 ? self.inputCommonData() : self.getHolidayShipmentCmd(self.reasonToApprover());
            nts.uk.ui.dialog.confirm({ messageId: 'Msg_248' }).ifYes(function() {
                service.releaseApp(releaseCmd, self.appType()).done(function(data) {
                    self.sendMail('Msg_221', data);
                }).fail(function(res: any) {
                    self.showError(res);
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
            let command = { appID: self.appID() };
            setShared("KDL030_PARAM", command);
            nts.uk.ui.windows.sub.modal("/view/kdl/030/a/index.xhtml").onClosed(() => {
                self.start(moment.utc().format("YYYY/MM/DD")).done(()=>{
                    nts.uk.ui.block.clear();       
                });
            });
        }
        /**
         *  btn Delete 削除
         */
        btnDelete() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommandEvent(new model.InputCommandEvent(self.inputCommandEvent().version, self.appID(), self.appReasonEvent()));
            let deleteCmd = self.appType() != 10 ? self.inputCommandEvent() : self.getHolidayShipmentCmd(self.appReasonEvent());
            nts.uk.ui.dialog.confirm({ messageId: 'Msg_18' }).ifYes(function() {
                service.deleteApp(deleteCmd, self.appType()).done(function(data) {
                    nts.uk.ui.dialog.info({ messageId: 'Msg_16' }).then(function() {
                            //kiểm tra list người xác nhận, nếu khác null thì show info 392
                            if (data.autoSendMail) {
                                appcommon.CommonProcess.displayMailDeleteRs(data);
                            } else {
                                shrvm.model.CommonProcess.callCMM045();
                            }
                    });
                }).fail(function(res: any) {
                    self.showError(res);
                });
            }).ifNo(function() {
                nts.uk.ui.block.clear();
            });
        }

        getHolidayShipmentCmd(memo) {
            let self = this,
                shipmentCmd,
                vm: nts.uk.at.view.kaf011.b.viewmodel.ScreenModel = __viewContext['viewModel'];

            shipmentCmd = {
                absAppID: vm.absWk().appID(),
                recAppID: vm.recWk().appID(),
                appVersion: vm.version,
                memo: memo ? memo : "",
                comboBoxReason: self.getBoxReason(),
                textAreaReason: self.getAreaReason()
            }

            return shipmentCmd;

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
                shrvm.model.CommonProcess.callCMM045();
            }

            if (self.listAppMeta.length == 1) {
                shrvm.model.CommonProcess.callCMM045();
            }
            //nếu vị trí vừa xóa khác vị trí cuối
            if (index != self.listAppMeta.length) {
                //gán lại appId mới tại vị trí chính nó
                //self.btnAfter();
                shrvm.model.CommonProcess.callCMM045();
            } else {
                //nếu nó ở vị trí cuối thì lấy appId ở vị trí trước nó
                //                self.btnBefore();
                shrvm.model.CommonProcess.callCMM045();
            }

        }

        /**
         *  btn Cancel
         */
        btnCancel() {
            nts.uk.ui.block.invisible();
            let self = this;
            self.inputCommandEvent(new model.InputCommandEvent(self.inputCommandEvent().version, self.appID(), self.appReasonEvent()));
            let cancelCmd = self.appType() != 10 ? self.inputCommandEvent() : self.getHolidayShipmentCmd(self.appReasonEvent());
            nts.uk.ui.dialog.confirm({ messageId: 'Msg_249' }).ifYes(function() {
                service.cancelApp(cancelCmd, self.appType()).done(function() {
                    nts.uk.ui.dialog.info({ messageId: "Msg_224" }).then(() => {
                        self.start(moment.utc().format("YYYY/MM/DD")).done(()=>{
                            nts.uk.ui.block.clear();        
                        });
                    });
                }).fail(function(res: any) {
                    self.showError(res);
                });
            }).ifNo(function() {
                nts.uk.ui.block.clear();
            });
        }

        showError(res) {
            if (res.optimisticLock == true) {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function() {
                    location.reload();
                });
            } else {
                nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() {
                    nts.uk.ui.block.clear();
                    if (res.messageId === "Msg_198") {
                        shrvm.model.CommonProcess.callCMM045();
                    }
                });
            }
        }

        convertToApproverList(): Array<Approver> {
            var self = this;
            let listApprover = [];
            listApprover.push(new Approver(self.appID(), self.employeeName(), null));
            _.forEach(ko.mapping.toJS(self.approvalRootState()), function(approvalPhase) {
                _.forEach(approvalPhase, function(approvalFrame) {
                    if ((!nts.uk.util.isNullOrEmpty(approvalFrame.approverID)) || (!nts.uk.util.isNullOrEmpty(approvalFrame.representerID))) {
                        if (!nts.uk.util.isNullOrEmpty(approvalFrame.approverID)) {
                            listApprover.push(new Approver(approvalFrame.approverID, approvalFrame.approverName, approvalFrame.phaseOrder));
                        } else {
                            listApprover.push(new Approver(approvalFrame.representerID, approvalFrame.representerName, approvalFrame.phaseOrder));
                        }
                    }
                });
            });
            return listApprover;
        }

        callCMM045A(){
            shrvm.model.CommonProcess.callCMM045();
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
