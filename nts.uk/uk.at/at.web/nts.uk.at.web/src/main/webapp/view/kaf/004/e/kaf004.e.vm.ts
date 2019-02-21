module nts.uk.at.view.kaf004.e.viewmodel {
    import model = nts.uk.at.view.kaf000.b.viewmodel.model;
    import service = nts.uk.at.view.kaf004.b.service;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export class ScreenModel extends kaf000.b.viewmodel.ScreenModel {
        // applicantName
        applicantName: KnockoutObservable<string> = ko.observable("");
        // date editor
        date: KnockoutObservable<string> = ko.observable(moment().format('YYYY/MM/DD'));
        //latetime editor
        lateTime1: KnockoutObservable<number> = ko.observable(0);
        lateTime2: KnockoutObservable<number> = ko.observable(0);
        //check send mail
        isSendMail: KnockoutObservable<boolean> = ko.observable(true);
        //check late
        late1: KnockoutObservable<boolean> = ko.observable(false);
        late2: KnockoutObservable<boolean> = ko.observable(false);
        //check early
        early1: KnockoutObservable<boolean> = ko.observable(false);
        early2: KnockoutObservable<boolean> = ko.observable(false);
        //labor time
        earlyTime1: KnockoutObservable<number> = ko.observable(0);
        earlyTime2: KnockoutObservable<number> = ko.observable(0);
        //combobox
        ListTypeReason: KnockoutObservableArray<TypeReason> = ko.observableArray([]);
        itemName: KnockoutObservable<string> = ko.observable('');
        currentCode: KnockoutObservable<number> = ko.observable(3);
        selectedCode: KnockoutObservable<string> = ko.observable('');
        //MultilineEditor
        appreason: KnockoutObservable<string> = ko.observable('');
        //Show Screen
        showScreen: KnockoutObservable<string> = ko.observable('');
        postAtr: KnockoutObservable<number> = ko.observable(0);
        isVisibleTimeF: KnockoutObservable<boolean> = ko.observable(true);
        isLblTimeF: KnockoutObservable<boolean> = ko.observable(false);
        //DuDT: 2017.10.27処理が対応できてない、とりあえず値が固定する
        txtlateTime1: KnockoutObservable<string> = ko.observable('2:00');
        txtearlyTime1: KnockoutObservable<string> = ko.observable('1:00');
        txtEarlyTime2: KnockoutObservable<string> = ko.observable('0:30');
        txtlateTime2: KnockoutObservable<string> = ko.observable('1:00');
        version: number = 0;
        displayOrder: KnockoutObservable<number> = ko.observable(0);
        actualCancelType: KnockoutObservable<string> = ko.observable('');

        appCommonSetting: KnockoutObservable<AppComonSetting> = ko.observable(new AppComonSetting());
        constructor(listAppMetadata: Array<model.ApplicationMetadata>, currentApp: model.ApplicationMetadata) {
            super(listAppMetadata, currentApp);
            var self = this;
            //Show Screen
            self.startPage();
        }

        startPage(): JQueryPromise<any> {
            nts.uk.ui.block.invisible();
            var self = this;
            var dfd = $.Deferred();
            service.getByCode(self.appID()).done(function(data) {
                self.displayOrder(data.workManagementMultiple.useATR);
                self.ListTypeReason(_.map(data.listApplicationReasonDto, x => { return { reasonID: x.reasonID, reasonTemp: x.reasonTemp }; }));
                if (data.listApplicationReasonDto) {
                    self.selectedCode(self.ListTypeReason()[0].reasonID);
                }
                self.appCommonSetting(new AppComonSetting(data.appCommonSettingDto));
                self.applicantName(data.applicantName);
                self.appreason(data.lateOrLeaveEarlyDto.appReason);
                self.date(data.lateOrLeaveEarlyDto.applicationDate);
                self.actualCancelType(data.lateOrLeaveEarlyDto.actualCancelAtr == 1 ? '取消する' : '取消しない');
                self.lateTime1(data.lateOrLeaveEarlyDto.lateTime1);
                self.lateTime2(data.lateOrLeaveEarlyDto.lateTime2);
                self.late1(data.lateOrLeaveEarlyDto.late1 == 1 ? true : false);
                self.late2(data.lateOrLeaveEarlyDto.late2 == 1 ? true : false);
                self.early1(data.lateOrLeaveEarlyDto.early1 == 1 ? true : false);
                self.early2(data.lateOrLeaveEarlyDto.early2 == 1 ? true : false);
                self.earlyTime1(data.lateOrLeaveEarlyDto.earlyTime1);
                self.earlyTime2(data.lateOrLeaveEarlyDto.earlyTime2);
                self.showScreen(data.lateOrLeaveEarlyDto.postAtr == 1 ? 'F' : '');
                self.postAtr(data.lateOrLeaveEarlyDto.postAtr);
                self.version = data.lateOrLeaveEarlyDto.version;
                self.late1.subscribe(value => { $("#inpLate1").trigger("validate"); });
                self.early1.subscribe(value => { $("#inpEarlyTime1").trigger("validate"); });
                self.late2.subscribe(value => { $("#inpLate2").trigger("validate"); });
                self.early2.subscribe(value => { $("#inpEarlyTime2").trigger("validate"); });
                if (self.showScreen() === 'F') {
                    self.isVisibleTimeF(false);
                    self.isLblTimeF(true);


                } else {
                    self.isVisibleTimeF(true);
                    self.isLblTimeF(false);
                    self.txtEarlyTime2("");
                    self.txtlateTime2("");
                    self.txtlateTime1("");
                    self.txtearlyTime1("");
                    $("#lblLateTime1").css("margin-left", "0px");
                    $("#lblLateTime2").css("margin-left", "0px");
                }
                $("#inputdate").focus();
                nts.uk.ui.block.clear();
                dfd.resolve();
            }).fail((res) => {
                if (res.messageId == 'Msg_426') {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.ui.block.clear();
                    });
                } else {
                    nts.uk.ui.dialog.alertError({ messageId: res.messageId }).then(function() {
                        nts.uk.request.jump("com", "view/ccg/008/a/index.xhtml");
                    });
                }
                nts.uk.ui.block.clear();
                dfd.reject();
            });

            return dfd.promise();
        }
        
        showReasonText(){
            let appSetting =this.appCommonSetting().appTypeDiscreteSetting();
        return     appSetting.displayReasonFlg()!=0 || appSetting.typicalReasonDisplayFlg()!=0;
        }

        update() {
            var self = this;
            $('#appReason').ntsError('check');
            if (!nts.uk.ui.errors.hasError()) {
                //DuDT: 2017.10.27処理が対応できてない、とりあえず値が固定する
                if (self.showScreen() === 'F') {
                    self.lateTime1(null);
                    self.earlyTime1(null);
                    self.lateTime2(null);
                    self.earlyTime2(null);
                }

                let txtReasonTmp = self.selectedCode();
                if (!nts.uk.text.isNullOrEmpty(self.selectedCode())) {
                    let reasonText = _.find(self.ListTypeReason(), function(data) { return data.reasonID == self.selectedCode() });
                    txtReasonTmp = reasonText.reasonTemp;
                }
                if (!appcommon.CommonProcess.checklenghtReason(!nts.uk.text.isNullOrEmpty(txtReasonTmp) ? txtReasonTmp + "\n" + self.appreason() : self.appreason(), "#appReason")) {
                    return;
                }
                var lateOrLeaveEarly: LateOrLeaveEarly = {
                    version: self.version,
                    appID: self.appID(),
                    appDate: self.date(),
                    postAtr: self.postAtr(),
                    sendMail: self.isSendMail(),
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
                    appApprovalPhaseCmds: self.approvalList,
                    actualCancel: self.actualCancelType() == '取消する' ? 1 : 0
                };
                nts.uk.ui.block.invisible();
                service.updateLateOrLeaveEarly(lateOrLeaveEarly).done((data) => {
                    nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(function() {
                        if (data.autoSendMail) {
                            appcommon.CommonProcess.displayMailResult(data);
                        } else {
                            location.reload();
                        }
                    });
                }).fail((res) => {
                    if (res.optimisticLock == true) {
                        nts.uk.ui.dialog.alertError({ messageId: "Msg_197" }).then(function() {
                            location.reload();
                        });
                    } else {
                        nts.uk.ui.dialog.alertError({ messageId: res.messageId, messageParams: res.parameterIds }).then(function() { nts.uk.ui.block.clear(); });
                    }
                });

            }

        }
        
        getBoxReason(){
            var self = this;
            return appcommon.CommonProcess.getComboBoxReason(self.selectedCode(), self.ListTypeReason(), self.appCommonSetting().appTypeDiscreteSetting().displayReasonFlg() != 0);
        }
    
        getAreaReason(){
            var self = this;
            return appcommon.CommonProcess.getTextAreaReason(self.appreason(), self.appCommonSetting().appTypeDiscreteSetting().typicalReasonDisplayFlg() != 0, true);   
        }
        
        resfreshReason(appReason: string){
            var self = this;
            self.selectedCode('');   
            self.appreason(appReason); 
        }
    }


    interface TypeReason {
        reasonID: string;
        reasonTemp: string;
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

    interface LateOrLeaveEarly {
        version: number;
        appID: string;
        appDate: string;
        postAtr: number;
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
        appApprovalPhaseCmds: Array<any>;
        actualCancel:number;
    }
}