module nts.uk.at.view.kaf018.e.viewmodel {
    import getText = nts.uk.resource.getText;
    import getShared = nts.uk.ui.windows.getShared;
    import formatDate = nts.uk.time.formatDate;
    import info = nts.uk.ui.dialog.info;
    import error = nts.uk.ui.dialog.alertError;
    import confirm = nts.uk.ui.dialog.confirm;
    import block = nts.uk.ui.block;
    import shareModel = kaf018.share.model;

    export class ScreenModel {
        listWkpStatusConfirm: Array<ApprSttActivityDta>;
        useSetting: shareModel.UseSetting;
        closureID: string;
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
        multiSelectedWorkplaceId: Array<any>;
        inputContent: any;

        person: number;
        daily: number;
        monthly: number;
        isCheckedAll: KnockoutObservable<boolean> = ko.observable(false);
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
                self.closureID = params.closureID;
                self.closureName = params.closureName;
                self.processingYm = params.processingYm;
                self.startDateFormat = formatDate(new Date(params.startDate), 'yyyy/MM/dd');
                self.endDateFormat = formatDate(new Date(params.endDate), 'yyyy/MM/dd');
                self.startDate = params.startDate;
                self.endDate = params.endDate;
                self.isConfirmData = params.isConfirmData;
                let listWorkplace = params.listWorkplace;
                self.listEmpCd = params.listEmployeeCode;
                self.inputContent = params.inputContent;

                let listWorkplaceId = [];
                _.each(listWorkplace, function(item) {
                    listWorkplaceId.push(item.code)
                })
                let obj = {
                    startDate: self.startDate,
                    endDate: self.endDate,
                    isConfirmData: self.isConfirmData,
                    listWorkplaceId: listWorkplaceId,
                    listEmpCd: self.listEmpCd,
                    closureID: params.closureID
                };

                service.getUseSetting().done(function(setting) {
                    self.useSetting = setting;
                    service.getStatusActivity(obj).done(function(data: any) {
                        if(data.error){
                            nts.uk.ui.dialog.error({messageId: "Msg_1430", messageParams: ["承認者"]});
                        }
                        _.each(data.lstData, function(item) {
                            let wkp = _.find(listWorkplace, { code: item.wkpId });
                            self.listWkpStatusConfirm.push(new ApprSttActivityDta(item.wkpId, wkp.name, self.getRecord(item.monthConfirm),
                                 item.monthUnconfirm, self.getRecord(item.bossUnconfirm),
                                 self.getRecord(item.bossConfirm), self.getRecord(item.personUnconfirm),
                                 self.getRecord(item.personConfirm),
                                 !(item.personUnconfirm == 0 && item.bossUnconfirm == 0 && item.monthUnconfirm == 0)));
                            self.listWkpActive.push({ code: item.wkpId, name: wkp.name });
                        })
                        self.initNtsGrid(self.listHidden());
                        block.clear();
                        dfd.resolve();
                    }).fail(function(res){
                        nts.uk.ui.dialog.error({messageId: res.messageId, messageParams: res.parameterIds});
                        block.clear();
                        dfd.resolve();
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
        listHidden(): Array<any>{
            let self = this;
            let lstHidden = [];
             _.each(self.listWkpStatusConfirm, function(item, index) {
                if(item.enable == false){
                    lstHidden.push(item.code);
                }
            });
            return lstHidden;
        }
        initNtsGrid(lstHidden: Array<any>) {
            var self = this;
            $("#gridE").ntsGrid({
                width: self.ntsGridWidthCal(),
                height: '629px',
                dataSource: self.listWkpStatusConfirm,
                primaryKey: 'code',
                rowVirtualization: true,
                virtualization: true,
                hidePrimaryKey: true,
                virtualizationMode: 'continuous',
                columns: [
                    { headerText: getText('KAF018_52'), key: 'name', dataType: 'string', width: '210px', ntsControl: 'LinkLabel' },
                    { headerText: getText('KAF018_53'), key: 'monthConfirm', dataType: 'string', width: '100px', hidden: !self.useSetting.monthlyConfirm},
                    { headerText: getText('KAF018_54'), 
                        group:[{ headerText: getText('KAF018_99'), key: 'dayBossUnconfirm', dataType: 'string', width: '100px' },
                                { headerText: getText('KAF018_100'), key: 'dayBossConfirm', dataType: 'string', width: '100px' }],
                        hidden: !self.useSetting.useBossConfirm},
                    { headerText: getText('KAF018_55'), 
                        group:[{ headerText: getText('KAF018_56'), key: 'dayPrincipalUnconfirm', dataType: 'string', width: '100px' },
                                { headerText: getText('KAF018_57'), key: 'dayPrincipalConfirm', dataType: 'string', width: '100px' }],
                        hidden: !self.useSetting.usePersonConfirm},
                    { headerText: getText('KAF018_58'), key: 'check', dataType: 'boolean', width: '120px', 
                            showHeaderCheckbox: true, ntsControl: 'Checkbox',  hiddenRows: lstHidden},
                    { headerText: 'ID', key: 'code', dataType: 'string', width: '0px', ntsControl: 'Label'}
                ],
                features: [
                    {
                        name: 'Selection',
                        mode: 'row',
                        multipleSelection: true
                    },
                    { name: "MultiColumnHeaders" }
                ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox' },
                    { name: 'LinkLabel' ,click: function(rowId){self.gotoF(rowId)}, controlType: 'LinkLabel' }],
            });
            $("#gridE").setupSearchScroll("igGrid", true);
            $("#gridE").focus();
        }
        
        ntsGridWidthCal() {
            var self = this;
            let width = 350;
            if (self.useSetting.monthlyConfirm) {
                width += 100;
            }
            if (self.useSetting.useBossConfirm) {
                width += 200;
            }
            if (self.useSetting.usePersonConfirm) {
                width += 200;
            }
            return width;
        }

        sendMail(value: TransmissionAttr) {
            var self = this;
            block.invisible();
            let listWkp = [];
            _.each(self.listWkpStatusConfirm, function(item) {
                listWkp.push({ wkpId: item.code, isCheckOn: item.check ? 1 : 0 })
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
                        listEmpCd: self.listEmpCd,
                        closureID: self.closureID
                    };
                    service.exeSendUnconfirmedMail(obj).done(function(result: any) {
                        shareModel.showMsgSendEmail(result);
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

        getRecord(value?: number) {
            return value ? value + "件" : "";
        }

        gotoF(id) {
            var self = this;
            let indexs = null;
            _.each(self.listWkpStatusConfirm, function(item, index) {
                if(item.code == id){
                    indexs = index;
                }
            });
            let params = {
                closureID: self.closureID,
                closureName: self.closureName,
                processingYm: self.processingYm,
                startDate: self.startDate,
                endDate: self.endDate,
                isConfirmData: self.isConfirmData,
                listWkp: self.listWkpActive,
                selectedWplIndex: indexs,
                listEmployeeCode: self.listEmpCd,
                inputContent: self.inputContent
                
            };
            nts.uk.request.jump('/view/kaf/018/f/index.xhtml', params);
        }

        goBackA() {
            var self = this;
            let params = {
                inputContent: self.inputContent
            };
            nts.uk.request.jump('/view/kaf/018/a/index.xhtml', params);
        }
    }


    export class ApprovalStatusActivity {
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

        constructor(code: string, name: string, monthUnconfirm: number, monthConfirm: number, dayBossUnconfirm: number, dayBossConfirm: number, dayPrincipalUnconfirm: number, dayPrincipalConfirm: number) {
            this.code = code;
            this.name = name;
            this.monthUnconfirm = monthUnconfirm ? monthUnconfirm : null;
            this.monthConfirm = monthConfirm ? monthConfirm : null;
            this.dayBossUnconfirm = dayBossUnconfirm ? dayBossUnconfirm : null;
            this.dayBossConfirm = dayBossConfirm ? dayBossConfirm : null;
            this.dayPrincipalUnconfirm = dayPrincipalUnconfirm ? dayPrincipalUnconfirm : null;
            this.dayPrincipalConfirm = dayPrincipalConfirm ? dayPrincipalConfirm : null;
            if (dayPrincipalUnconfirm == 0 && dayBossUnconfirm == 0 && monthUnconfirm == 0) {
                this.enable = false;
            }
            else {
                this.enable = true;
            }
            this.check = ko.observable(false);
        }
    }
    export class ApprSttActivityDta {
        code: string;
        name: string;
        monthConfirm: string;
        monthUnconfirm: number;
        dayBossUnconfirm: string;
        dayBossConfirm: string;
        dayPrincipalUnconfirm: string;
        dayPrincipalConfirm: string;
        enable: boolean;
        check: boolean;
        constructor(code: string, name: string, monthConfirm: string, monthUnconfirm: number, dayBossUnconfirm: string,
            dayBossConfirm: string, dayPrincipalUnconfirm: string, dayPrincipalConfirm: string, enable: boolean) {
            this.code = code;
            this.name = name;
            this.monthUnconfirm = monthUnconfirm;
            this.monthConfirm = monthConfirm;
            this.dayBossUnconfirm = dayBossUnconfirm;
            this.dayBossConfirm = dayBossConfirm;
            this.dayPrincipalUnconfirm = dayPrincipalUnconfirm;
            this.dayPrincipalConfirm = dayPrincipalConfirm;
            this.enable = enable;
            this.check = false;
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