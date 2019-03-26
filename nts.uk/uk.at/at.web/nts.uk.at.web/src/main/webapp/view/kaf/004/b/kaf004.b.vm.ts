module nts.uk.at.view.kaf004.b.viewmodel {
    import kaf002 = nts.uk.at.view.kaf002;
    import vmbase = nts.uk.at.view.kaf002.shr.vmbase;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    import setShared = nts.uk.ui.windows.setShared;
    const employmentRootAtr: number = 1; // EmploymentRootAtr: Application
    const applicationType: number = 9; // Application Type: Stamp Application

    export class ScreenModel {
        dateType: string = 'YYYY/MM/DD';
        //latetime editor
        lateTime1: KnockoutObservable<number> = ko.observable(null);
        lateTime2: KnockoutObservable<number> = ko.observable(null);
        //check send mail
        checkBoxValue: KnockoutObservable<boolean> = ko.observable(false);
        enableSendMail: KnockoutObservable<boolean> = ko.observable(false);
        //check late
        late1: KnockoutObservable<boolean> = ko.observable(false);
        late2: KnockoutObservable<boolean> = ko.observable(false);
        //check early
        early1: KnockoutObservable<boolean> = ko.observable(false);
        early2: KnockoutObservable<boolean> = ko.observable(false);
        //labor time
        earlyTime1: KnockoutObservable<number> = ko.observable(null);
        earlyTime2: KnockoutObservable<number> = ko.observable(null);
        //combobox
        ListTypeReason: KnockoutObservableArray<TypeReason> = ko.observableArray([]);
        itemName: KnockoutObservable<string> = ko.observable('');
        currentCode: KnockoutObservable<number> = ko.observable(null);
        selectedCode: KnockoutObservable<string> = ko.observable('');
        //MultilineEditor
        appreason: KnockoutObservable<string> = ko.observable('');
        //Show Screen
        showScreen: string;
        employeeID: string = '';
        applicantName: KnockoutObservable<string> = ko.observable("");
        kaf000_a2: nts.uk.at.view.kaf000.a.viewmodel.ScreenModel;

        //Chua lay dc thong tin  fix cứng time
        fixtime1: KnockoutObservable<string> = ko.observable("08:30 ~ 12:00     13:00 ~ 17:30");
        fixtime2: KnockoutObservable<string> = ko.observable("10:30 ~ 11:00     14:00 ~ 17:00");
        //DisplayOrder
        displayOrder: KnockoutObservable<number> = ko.observable(0);

        appCommonSetting: KnockoutObservable<AppComonSetting> = ko.observable(new AppComonSetting());

        constructor() {
            var self = this;
            //Show Screen
            self.showScreen = __viewContext.transferred.value.showScreen;
            self.kaf000_a2 = new kaf000.a.viewmodel.ScreenModel();
            self.startPage().done((commonSet: vmbase.AppStampNewSetDto) => {
                self.employeeID = commonSet.employeeID;
                self.kaf000_a2.start(
                    self.employeeID,
                    employmentRootAtr,
                    applicationType,
                    moment.utc().format(self.dateType)).done(() => {
                        nts.uk.ui.block.clear();
                    }).fail(function(res) {
                        nts.uk.ui.dialog.alertError(res.message).then(function() { nts.uk.ui.block.clear(); });
                    });
            });
            self.appCommonSetting().generalDate.subscribe(value => {
                if (!$("#inputdate").ntsError("hasError")) {
                    nts.uk.ui.block.invisible();
                    self.kaf000_a2.getAppDataDate(9, moment(value).format(self.dateType), false, self.employeeID).always(() => { nts.uk.ui.block.clear(); });
                }
            });

        }

        startPage(): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            var self = this;
            var dfd = $.Deferred();
            service.getByCode("").done(function(data) {
                self.enableSendMail(data.appCommonSettingDto.appTypeDiscreteSettingDtos[0].sendMailWhenRegisterFlg == 1 ? false : true);
                self.checkBoxValue(data.appCommonSettingDto.applicationSettingDto.manualSendMailAtr == 1 ? true : false);
                self.ListTypeReason.removeAll();
                _.forEach(data.listApplicationReasonDto, data => {
                    let reasonTmp: TypeReason = { reasonID: data.reasonID, reasonTemp: data.reasonTemp };
                    self.ListTypeReason.push(reasonTmp);
                    if (data.defaultFlg == 1) {
                        self.selectedCode(data.reasonID);
                    }
                });
                self.appCommonSetting().appTypeDiscreteSetting(new AppTypeDiscreteSetting(data.appCommonSettingDto.appTypeDiscreteSettingDtos[0]));
                self.displayOrder(data.workManagementMultiple.useATR);
                self.applicantName(data.applicantName);
                self.late1.subscribe(value => { $("#inpLate1").trigger("validate"); });
                self.early1.subscribe(value => { $("#inpEarlyTime1").trigger("validate"); });
                self.late2.subscribe(value => { $("#inpLate2").trigger("validate"); });
                self.early2.subscribe(value => { $("#inpEarlyTime2").trigger("validate"); });
                dfd.resolve(data);
            }).fail(function(res) {
                if (res.messageId == 'Msg_426') {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.ui.block.clear();
                    });
                } else {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");
                        nts.uk.ui.block.clear();
                    });
                }
                dfd.reject(res);
            });
            return dfd.promise();
        }


        /** Create Button Click */
        registryButtonClick() {
            var self = this;
            let errorFlag = self.kaf000_a2.errorFlag;
            let errorMsg = self.kaf000_a2.errorMsg;
            if (errorFlag != 0) {
                nts.uk.ui.dialog.alertError({ messageId: errorMsg }).then(function() { nts.uk.ui.block.clear(); });
            } else {
                if (!nts.uk.ui.errors.hasError()) {
                    /**  0: 事前の受付制限
                         1: 事後の受付制限
                    */
                    let prePostAtr = 1;
                    if (self.showScreen == 'B') {
                        //[画面Bのみ]遅刻時刻早退時刻がともに設定されているとき、遅刻時刻>=早退時刻 (#Msg_381#)
                        if ((self.late1() && self.early1() && self.lateTime1() >= self.earlyTime1())
                            || (self.late2() && self.early2() && self.lateTime2() >= self.earlyTime2())) {
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_381" });
                            return;
                        }
                        //[画面Bのみ]遅刻、早退、遅刻2、早退2のチェックがある遅刻時刻、早退時刻は入力必須(#Msg_470#)
                        if (self.late1()
                            && self.early1()
                            && self.late2()
                            && self.early2()
                            && (self.lateTime1() == null || self.earlyTime1() == null)) {
                            nts.uk.ui.dialog.alertError({ messageId: "Msg_470" });
                            return;
                        }

                        prePostAtr = 0;
                    }



                    let isShowReasonText = self.appCommonSetting().appTypeDiscreteSetting().displayReasonFlg() != 0,
                        isShowReasonCombo = self.appCommonSetting().appTypeDiscreteSetting().typicalReasonDisplayFlg() != 0,
                        appReason = self.getReason(),
                        txtReasonTmp = !nts.uk.util.isNullOrEmpty(self.selectedCode()) ? _.find(self.ListTypeReason(), { 'reasonID': self.selectedCode() }).reasonTemp : "",
                        appReasonError = !nts.uk.at.view.kaf000.shr.model.CommonProcess.checkAppReason(true, isShowReasonCombo, isShowReasonText, appReason);


                    if (appReasonError) {
                        nts.uk.ui.dialog.alertError({ messageId: 'Msg_115' });
                        return false;
                    }

                    let isCheckLengthError: boolean = !nts.uk.at.view.kaf000.shr.model.CommonProcess.checklenghtReason(appReason, "#appReason");
                    if (isCheckLengthError) {
                        return false;
                    }

                    let lateOrLeaveEarly = {
                        prePostAtr: prePostAtr,
                        applicationDate: self.appCommonSetting().generalDate(),
                        sendMail: self.checkBoxValue(),
                        late1: self.late1() ? 1 : 0,
                        lateTime1: self.lateTime1(),
                        early1: self.early1() ? 1 : 0,
                        earlyTime1: self.earlyTime1(),
                        late2: self.late2() ? 1 : 0,
                        lateTime2: self.lateTime2(),
                        early2: self.early2() ? 1 : 0,
                        earlyTime2: self.earlyTime2(),
                        reasonTemp: txtReasonTmp,
                        appReason: self.appreason(),
                        actualCancel: self.showScreen == 'C' ? 1 : 0,
                        checkOver1Year: true
                    };
                    nts.uk.ui.block.invisible();
                    service.createLateOrLeaveEarly(lateOrLeaveEarly).done((data) => {
                        self.createDone(data);
                    }).fail((res) => {
                        if (res.messageId == "Msg_1518") {//confirm
                            nts.uk.ui.dialog.confirm({ messageId: res.messageId }).ifYes(() => {
                                lateOrLeaveEarly.checkOver1Year = false;
                                service.createLateOrLeaveEarly(lateOrLeaveEarly).done((data) => {
                                    self.createDone(data);
                                }).fail((res) => {
                                    nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds })
                                        .then(function() { nts.uk.ui.block.clear(); });
                                });
                            }).ifNo(() => {
                                nts.uk.ui.block.clear();
                            });
                        } else {
                            nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { nts.uk.ui.block.clear(); });
                        }
                    });
                }
            }
        }

        createDone(data) {
            let self = this;
            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                nts.uk.ui.block.clear();
                if (data.autoSendMail) {
                    appcommon.CommonProcess.displayMailResult(data);
                } else {
                    if (self.checkBoxValue()) {
                        appcommon.CommonProcess.openDialogKDL030(data.appID);
                    } else {
                        location.reload();
                    }
                }
            });
        }
        getReason(): string {
            let appReason = '',
                self = this,
                inputReasonID = self.selectedCode(),
                inputReasonList = self.ListTypeReason(),
                detailReason = self.appreason();
            let inputReason: string = '';
            if (!nts.uk.util.isNullOrEmpty(inputReasonID)) {
                inputReason = _.find(inputReasonList, { 'reasonID': inputReasonID }).reasonTemp;
            }
            if (!nts.uk.util.isNullOrEmpty(inputReason) && !nts.uk.util.isNullOrEmpty(detailReason)) {
                appReason = inputReason + ":" + detailReason;
            } else if (!nts.uk.util.isNullOrEmpty(inputReason) && nts.uk.util.isNullOrEmpty(detailReason)) {
                appReason = inputReason;
            } else if (nts.uk.util.isNullOrEmpty(inputReason) && !nts.uk.util.isNullOrEmpty(detailReason)) {
                appReason = detailReason;
            }
            return appReason;
        }
    }

    class AppComonSetting {
        appTypeDiscreteSetting = ko.observable(new AppTypeDiscreteSetting());
        generalDate = ko.observable(null);
        constructor(data?) {
            if (data) {
                //this.generalDate(data.generalDate);
                this.appTypeDiscreteSetting(new AppTypeDiscreteSetting(data.appTypeDiscreteSettingDtos[0]));
            }

        }
    }
    class AppTypeDiscreteSetting {
        displayReasonFlg = ko.observable(0);
        typicalReasonDisplayFlg = ko.observable(0);
        constructor(data?) {
            if (data) {
                this.displayReasonFlg(data.displayReasonFlg);
                this.typicalReasonDisplayFlg(data.typicalReasonDisplayFlg);
            }
        }

    }

    interface TypeReason {
        reasonID: string;
        reasonTemp: string;
    }

    interface LateOrLeaveEarly {
        applicantName: string;
        applicationDate: string;
        sendMail: boolean
        late1: number;
        lateTime1: number;
        early1: number;
        earlyTime1: number;
        late2: number;
        lateTime2: number;
        early2: number;
        earlyTime2: number;
        reasonTemp: string;
        appReason: string;
        prePostAtr: number;
        appApprovalPhaseCmds: Array<any>;
        actualCancel: number
    }
}