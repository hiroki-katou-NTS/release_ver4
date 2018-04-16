module nts.uk.at.view.kaf018.e.viewmodel {
    import text = nts.uk.resource.getText;
    import getShared = nts.uk.ui.windows.getShared;
    import formatDate = nts.uk.time.formatDate;
    import info = nts.uk.ui.dialog.info;
    import error = nts.uk.ui.dialog.alertError;
    import confirm = nts.uk.ui.dialog.confirm;
    import block = nts.uk.ui.block;
    import shareModel = kaf018.share.model;

    export class ScreenModel {
        listWkpStatusConfirm: Array<ApprovalStatusActivity>;
        useSetting: shareModel.UseSetting;
        closureId: string;
        closureName: string;
        processingYm: string;
        startDateFormat: string;
        endDateFormat: string;
        startDate: Date;
        endDate: Date;
        isConfirmData: boolean
        listWkpActive: any;
        listWorkplaceId: Array<string>;
        listEmpCd: Array<string>;

        person: number;
        daily: number;
        monthly: number;

        constructor() {
            var self = this;

            self.listWkpStatusConfirm = [];
            self.listWkpActive = [];
            self.person = TransmissionAttr.PERSON;
            self.daily = TransmissionAttr.DAILY;
            self.monthly = TransmissionAttr.MONTHLY;
        }

        /**
         * 起動する
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            block.invisible();
            let params = getShared("KAF018E_PARAMS");
            if (params) {
                self.closureId = params.closureId;
                self.closureName = params.closureName;
                self.processingYm = params.processingYm;
                self.startDateFormat = formatDate(new Date(params.startDate), 'yyyy/MM/dd');
                self.endDateFormat = formatDate(new Date(params.endDate), 'yyyy/MM/dd');
                self.startDate = params.startDate;
                self.endDate = params.endDate;
                self.isConfirmData = params.isConfirmData;
                let listWorkplace = params.listWorkplace;
                self.listEmpCd = params.listEmployeeCode;

                let listWorkplaceId = [];
                _.each(listWorkplace, function(item) {
                    listWorkplaceId.push(item.code)
                })
                let obj = {
                    startDate: self.startDate,
                    endDate: self.endDate,
                    isConfirmData: self.isConfirmData,
                    listWorkplaceId: listWorkplaceId,
                    listEmpCd: self.listEmpCd
                };

                service.getUseSetting().done(function(setting) {
                    self.useSetting = setting;
                    service.getStatusActivity(obj).done(function(data: any) {
                        _.each(data, function(item) {
                            let wkp = _.find(listWorkplace, { code: item.wkpId });
                            self.listWkpStatusConfirm.push(new ApprovalStatusActivity(item.wkpId, wkp.name, item.monthConfirm, item.monthUnconfirm, item.bossConfirm, item.bossUnconfirm, item.personConfirm, item.personUnconfirm))
                            self.listWkpActive.push({ code: item.wkpId, name: wkp.name });

                            let width = 500;
                            if (self.useSetting.monthlyConfirm) {
                                width += 100;
                            }
                            if (self.useSetting.useBossConfirm) {
                                width += 100;
                            }
                            if (self.useSetting.usePersonConfirm) {
                                width += 100;
                            }
                            $("#fixed-table").ntsFixedTable({ width: width, height: 186 });
                        })
                        dfd.resolve();
                    }).always(function() {
                        block.clear();
                    })
                }).fail(function() {
                    block.clear();
                })
            }
            else {
                dfd.reject();
            }
            return dfd.promise();
        }

        sendMail(value: TransmissionAttr) {
            var self = this;
            block.invisible();
            let listWkp = [];
            _.each(self.listWkpStatusConfirm, function(item) {
                listWkp.push({ wkpId: item.code, isCheckOn: item.check() ? 1 : 0 })
            })
            //アルゴリズム「承認状況未確認メール送信」を実行する
            service.checkSendUnconfirmedMail(listWkp).done(function() {
                let messageId = "";
                switch (value) {
                    case TransmissionAttr.PERSON:
                        messageId = "Msg_796";
                        break;
                    case TransmissionAttr.DAILY:
                        messageId = "Msg_797";
                        break;
                    case TransmissionAttr.MONTHLY:
                        messageId = "Msg_798";
                        break;
                }
                confirm({ messageId: messageId }).ifYes(() => {
                    block.invisible();
                    // アルゴリズム「承認状況未確認メール送信実行」を実行する
                    let obj = {
                        "type": value,
                        listWkp: listWkp,
                        startDate: self.startDate,
                        endDate: self.endDate,
                        listEmpCd: self.listEmpCd
                    };
                    service.exeSendUnconfirmedMail(obj).done(function(result: any) {
                        if (result.ok) {
                            info({ messageId: "Msg_792" });
                        }
                        else {
                            error({ messageId: "Msg_793" });
                        }
                    }).fail(function(err) {
                        error({ messageId: err.messageId });
                    }).always(function() {
                        block.clear();
                    });
                })
            }).fail(function(err) {
                error({ messageId: err.messageId });
            }).always(function() {
                block.clear();
            });
        }

        private getRecord(value?: number) {
            return value ? value + "件" : "";
        }

        gotoF(index) {
            var self = this;

            let params = {
                closureId: self.closureId,
                closureName: self.closureName,
                processingYm: self.processingYm,
                startDate: self.startDate,
                endDate: self.endDate,
                isConfirmData: self.isConfirmData,
                listWkp: self.listWkpActive,
                selectedWplIndex: index(),
                listEmployeeCode: self.listEmpCd,
            };
            nts.uk.request.jump('/view/kaf/018/f/index.xhtml', params);
        }
    }


    class ApprovalStatusActivity {
        code: string;
        name: string;
        monthConfirm: number;
        monthUnconfirm: number;
        dayBossUnconfirm: number;
        dayBossConfirm: number;
        dayPrincipalUnconfirm: number;
        dayPrincipalConfirm: number;
        check: KnockoutObservable<boolean>;
        enable: boolean;

        constructor(code: string, name: string, monthConfirm: number, monthUnconfirm: number, dayBossUnconfirm: number, dayBossConfirm: number, dayPrincipalUnconfirm: number, dayPrincipalConfirm: number) {
            /*this.code = code;
            this.name = name;
            this.monthConfirm = monthConfirm ? monthConfirm : null;
            this.monthUnconfirm = monthUnconfirm ? monthUnconfirm : null;
            this.dayBossUnconfirm = dayBossUnconfirm ? dayBossUnconfirm : null;
            this.dayBossConfirm = dayBossConfirm ? dayBossConfirm : null;
            this.dayPrincipalUnconfirm = dayPrincipalUnconfirm ? dayPrincipalUnconfirm : null;
            this.dayPrincipalConfirm = dayPrincipalConfirm ? dayPrincipalConfirm : null;
            if (dayPrincipalUnconfirm == 0 && dayBossUnconfirm == 0 && monthUnconfirm == 0) {
                //this.enable = false;
                this.enable = true;
                this.check = ko.observable(this.enable);
            }
            else {
                this.enable = true;
                this.check = ko.observable(this.enable);
            }*/
            this.code = code;
            this.name = name;
            this.monthConfirm = monthConfirm ? monthConfirm : 1;
            this.monthUnconfirm = monthUnconfirm ? monthUnconfirm : 2;
            this.dayBossUnconfirm = dayBossUnconfirm ? dayBossUnconfirm : 3;
            this.dayBossConfirm = dayBossConfirm ? dayBossConfirm : 4;
            this.dayPrincipalUnconfirm = dayPrincipalUnconfirm ? dayPrincipalUnconfirm : 5;
            this.dayPrincipalConfirm = dayPrincipalConfirm ? dayPrincipalConfirm : 6;
            if (dayPrincipalUnconfirm == 0 && dayBossUnconfirm == 0 && monthUnconfirm == 0) {
                //this.enable = false;
                this.enable = true;
                this.check = ko.observable(this.enable);
            }
            else {
                this.enable = true;
                this.check = ko.observable(this.enable);
            }
        }
    }

    //送信区分
    enum TransmissionAttr {
        //本人
        PERSON = 1,
        //日次
        DAILY = 2,
        //月次
        MONTHLY = 3
    }

}