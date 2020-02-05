module nts.uk.at.view.kmf022.m.viewmodel {
    import flat = nts.uk.util.flatArray;
    import text = nts.uk.resource.getText;
    import clearError = nts.uk.ui.errors.clearAll;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    export class ScreenModel {
        // update ver27enumVer27z
        enumVer27 = ko.observableArray([
            { code: 0, name: text("KAF022_378") },
            { code: 1, name: text("KAF022_379") }
        ]);

        enumUseAtr = ko.observableArray([
            { code: 1, name: text("KAF022_100") },
            { code: 0, name: text("KAF022_101") }
        ]);

        prerequisiteUseAtr = ko.observableArray([
            { code: 0, name: text("KAF022_291") },  
            { code: 1, name: text("KAF022_292") }
        ]);

        otAppSettingFlgAtr = ko.observableArray([
            { code: 0, name: text("KAF022_291") },
            { code: 1, name: text("KAF022_292") }
        ]);

        timeCalculationUseAtr = ko.observableArray([
            { code: 0, name: text("KAF022_295") },
            { code: 1, name: text("KAF022_296") }
        ]);

        atWorkAtr = ko.observableArray([
            { code: 0, name: text("KAF022_37") },
            { code: 1, name: text("KAF022_301") },
            { code: 2, name: text("KAF022_302") },
            { code: 3, name: text("KAF022_303") }
        ]);

        timeInputUseAtr = ko.observableArray([  
            { code: 1, name: text("KAF022_308") },
            { code: 0, name: text("KAF022_309") }
        ]);

        listM23 = ko.observableArray([
            { code: 1, name: text("KAF022_305") },
            { code: 0, name: text("KAF022_306") }
        ]);

        lateOrLeaveAppCancelAtr = ko.observableArray([
            { code: 1, name: text("KAF022_311") },  
            { code: 0, name: text("KAF022_312") }
        ]);

        lateOrLeaveAppSettingAtr = ko.observableArray([
            { code: 1, name: text("KAF022_313") },
            { code: 0, name: text("KAF022_314") }
        ]);

        kcp004WorkplaceListOption: any = {
            maxRows: 10,
            treeType: 1,
            tabindex: 1,
            systemType: 2,
            selectType: 3,
            isDialog: false,
            isMultipleUse: false,
            isMultiSelect: false,
            isShowAlreadySet: true,
            isShowSelectButton: true
        };

        baseDate: KnockoutObservable<Date> = ko.observable(new Date());
        selectedWorkplaceId: KnockoutObservableArray<String> = ko.observableArray([]);
        alreadySettingList: KnockoutObservableArray<any> = ko.observableArray([]);

        lstAppApprovalSettingWkp: Array<IApplicationApprovalSettingWkp> = [];
        selectedSetting: ApplicationApprovalSettingWkp = new ApplicationApprovalSettingWkp(null);
        hasLoadedKcp004: boolean = false;
        allowRegister: KnockoutObservable<boolean> = ko.observable(true);

        // update ver27
        selectVer27: KnockoutObservable<number> = ko.observable(0);

        isUpdateMode: KnockoutComputed<boolean> = ko.computed(() => {
            let self = this,
                asl = self.alreadySettingList(),
                swid = self.selectedWorkplaceId();

            return !!_.find(asl, f => f.workplaceId === swid);
        });

        copyAble: KnockoutComputed<boolean> = ko.computed(() => {
            let self = this,
                isUpdate = self.isUpdateMode;

            return !!self.selectVer27() && isUpdate();
        });
        textKAF022_285 : KnockoutObservable<string> = ko.observable('');
        constructor() {
            var self = this;
            
            // get text KAF022_285
            self.textKAF022_285(nts.uk.resource.getText("KAF022_285") + "("
                        + nts.uk.text.getCharType('AppUseSetRemark').viewName +
                        + __viewContext.primitiveValueConstraints.AppUseSetRemark.maxLength/2
                        + "文字)");

            _.extend(self.kcp004WorkplaceListOption, {
                baseDate: self.baseDate,
                alreadySettingList: self.alreadySettingList,
                selectedWorkplaceId: self.selectedWorkplaceId
            });

            self.selectVer27.subscribe(v => {
                self.allowRegister(true);
                if (v == 1 && !self.hasLoadedKcp004) {
                    $('#wkp-list').ntsTreeComponent(self.kcp004WorkplaceListOption).done(() => {
                        $('#wkp-list').focusTreeGridComponent();
                        self.reloadData();
                        self.hasLoadedKcp004 = true;
                    });
                } else {
                    self.reloadData();
                }
            });

            self.selectedWorkplaceId.subscribe((val) => {
                if(val){
                    self.allowRegister(true);    
                }else{
                    self.allowRegister(false);    
                }
                
                let exsistedSetting = _.find(self.lstAppApprovalSettingWkp, (setting: IApplicationApprovalSettingWkp) => {
                    return val === setting.wkpId;
                });

                if (exsistedSetting) {
                    self.selectedSetting.update(exsistedSetting);
                } else {
                    self.selectedSetting.update(null);
                    self.selectedSetting.wkpName("");
                }

                self.selectedSetting.wkpId(val);
            });

            $('#wkp-list').ntsTreeComponent(self.kcp004WorkplaceListOption).done(() => {
                self.reloadData();
                $('#wkp-list').focusTreeGridComponent();
            });
        }

        reloadData() {
            let self = this,
                s27 = self.selectVer27(),
                lwps = $('#wkp-list').getDataList(),
                flwps = flat(_.cloneDeep(lwps), "childs");

            // clear all msg when reload data.
            clearError();

            if (!!s27) {
                self.selectedSetting.wkpId.valueHasMutated();
                nts.uk.ui.block.invisible();

                service.getAll(flwps.map((wkp) => { return wkp.workplaceId; }))
                    .done((dataResults: Array<IApplicationApprovalSettingWkp>) => {
                        self.lstAppApprovalSettingWkp = dataResults;

                        self.alreadySettingList(dataResults.map((data) => ({
                            workplaceId: data.wkpId,
                            isAlreadySetting: true
                        })));

                        self.selectedWorkplaceId.valueHasMutated();

                        nts.uk.ui.block.clear();
                    });
            } else {
                service.getCom().done(config => {
                    if (config) {
                        _.extend(config, {
                            companyId: config.companyID
                        });
                    }

                    self.selectedSetting.update(config);
                });
            }
        }

        openDialogCopy() {
            let self = this,
                lwps = $('#wkp-list').getDataList(),
                rstd = $('#wkp-list').getRowSelected(),
                flwps = flat(_.cloneDeep(lwps), "childs"),
                wkp = _.find(flwps, wkp => wkp.workplaceId == _.head(rstd).workplaceId),
                param = {
                    targetType: 4,
                    name: wkp ? wkp.name : '',
                    code: wkp ? wkp.workplaceCode : '',
                    baseDate: ko.toJS(self.baseDate),
                    itemListSetting: _.map(self.alreadySettingList(), m => m.workplaceId)
                };

            setShared("CDL023Input", param);

            // open dialog CDL023
            nts.uk.ui.windows.sub.modal('com', '/view/cdl/023/a/index.xhtml').onClosed(() => {
                // get data respond
                let lstSelection: Array<string> = getShared("CDL023Output");

                if (_.size(lstSelection)) {
                    nts.uk.ui.dialog.confirm({ messageId: "Msg_19" }).ifYes(() => {
                        let cps = ko.mapping.toJS(self.selectedSetting),
                            commands: Array<any> = _.map(lstSelection, wkpId => _.extend(_.clone(cps), {
                                wkpId: wkpId
                            }));

                        _.each(commands, cmd => {
                            _.unset(cmd, ["update"]);
                            _.unset(cmd, ["initSettingList"]);

                            _.each(cmd.approvalFunctionSettingDtoLst, afs => {
                                _.unset(afs, ["update"]);

                                _.extend(afs, {
                                    goOutTimeBeginDisFlg: Number(afs.goOutTimeBeginDisFlg),
                                    breakInputFieldDisFlg: Number(afs.breakInputFieldDisFlg),
                                    requiredInstructionFlg: Number(afs.requiredInstructionFlg)
                                });
                            });
                        });

                        nts.uk.at.view.kmf022.m.service.update(commands).done(() => {
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                                self.reloadData();
                            });
                        });
                    });
                } else if (lstSelection && lstSelection.length == 0) {
                    nts.uk.ui.dialog.alert({ messageId: "Msg_888" })
                }
            });
        }

        update() {
            let self = this;
            if(!self.allowRegister()){
                return;
            }
            $('.memo').trigger("validate");
            
            let command = ko.mapping.toJS(self.selectedSetting);
            
            _.each(command.approvalFunctionSettingDtoLst, (setting: any) => {
                // remove private function
                delete setting.update;   

                // convert boolean type to number type
                setting.breakInputFieldDisFlg = Number(setting.breakInputFieldDisFlg);
                setting.goOutTimeBeginDisFlg = Number(setting.goOutTimeBeginDisFlg);
                setting.requiredInstructionFlg = Number(setting.requiredInstructionFlg);
            });

            command = _.omit(command, ['update', 'wkpName', 'initSettingList']);

            if (nts.uk.ui.errors.hasError() === false) {
                
                if (!!self.selectVer27()) {
                    service.update([command]).done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                            nts.uk.ui.block.grayout();
                            self.reloadData();
                            nts.uk.ui.block.clear();
                        });
                    }).fail(msg => {
                        nts.uk.ui.dialog.alert({ messageId: "Msg_59" });
                    });
                } else {
                    _.extend(command, {
                        comAppConfigDetails: _.clone(command.approvalFunctionSettingDtoLst)
                    });

                    command = _.omit(command, ["wkpId", "approvalFunctionSettingDtoLst"]);

                    service.saveCom(command).done(() => {
                        nts.uk.ui.dialog.info({ messageId: "Msg_15" }).then(() => {
                            nts.uk.ui.block.grayout();
                            self.reloadData();
                            nts.uk.ui.block.clear();
                        });
                    }).fail(() => {
                        nts.uk.ui.dialog.alert({ messageId: "Msg_59" });
                    });
                }
            }
        }

        remove() {
            let self = this,
                command = ko.toJS(self.selectedSetting);

            _.each(command.approvalFunctionSettingDtoLst, (setting: any) => {
                // remove private function
                delete setting.update;

                // convert boolean type to number type
                setting.breakInputFieldDisFlg = Number(setting.breakInputFieldDisFlg);
                setting.goOutTimeBeginDisFlg = Number(setting.goOutTimeBeginDisFlg);
                setting.requiredInstructionFlg = Number(setting.requiredInstructionFlg);
            });

            command = _.omit(command, ['update', 'wkpName', 'initSettingList']);

            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                nts.uk.at.view.kmf022.m.service.remove(command).done(() => {
                    nts.uk.ui.dialog.info({ messageId: 'Msg_16' }).then(() => {
                        self.reloadData();
                    });
                });
            });
        }
    }

    export interface IApplicationApprovalSettingWkp {
        // 会社ID
        companyId: string;
        // 職場ID
        wkpId: string;
        // 選択
        selectionFlg: number;
        // 申請承認機能設定
        approvalFunctionSettingDtoLst: Array<IApprovalFunctionSetting>;
    }

    export class ApplicationApprovalSettingWkp {

        // 会社ID
        companyId: KnockoutObservable<string> = ko.observable('');
        // 職場ID
        wkpId: KnockoutObservable<string> = ko.observable('');
        // 職場Name
        wkpName: KnockoutObservable<string> = ko.observable('');
        // 選択
        selectionFlg: KnockoutObservable<number> = ko.observable(0);
        // 申請承認機能設定
        approvalFunctionSettingDtoLst: KnockoutObservableArray<ApprovalFunctionSetting> = ko.observableArray([]);

        constructor(param: IApplicationApprovalSettingWkp) {
            let self = this;

            self.wkpId.subscribe((val) => {
                if(val){
                    let $wkpl = $('#wkp-list');

                    if ($wkpl.getDataList && $wkpl.getRowSelected) {
                        let lwps = $wkpl.getDataList(),
                            rstd = $wkpl.getRowSelected(),
                            flwps = flat(_.cloneDeep(lwps), "childs"),
                            wkp = _.find(flwps, wkp => wkp.workplaceId == _.head(rstd).workplaceId);
    
                        self.wkpName(wkp ? wkp.name : '');
                    }
                }
            });

            self.update(param);
        }

        update(param: IApplicationApprovalSettingWkp) {
            let self = this;

            self.approvalFunctionSettingDtoLst(this.initSettingList());

            self.wkpId(param ? param.wkpId : '');
            self.selectionFlg(param ? param.selectionFlg : 0);

            self.companyId(param ? param.companyId : __viewContext.user.companyId);

            if (param) {
                _.each(param.approvalFunctionSettingDtoLst, (setting: IApprovalFunctionSetting) => {
                    let foundSetting = _.find(self.approvalFunctionSettingDtoLst(), (targetSetting: ApprovalFunctionSetting) => {
                        return targetSetting.appType() == setting.appType;
                    });

                    if (foundSetting) {
                        foundSetting.update(setting);
                    }
                });
            }
        }

        // initial new setting list
        initSettingList = () => _.map([0, 1, 2, 3, 4, 6, 8, 9, 7, 10, 14], m => new ApprovalFunctionSetting(null, m));
    }

    export interface IApprovalFunctionSetting {
        //申請種類
        appType: number;
        //備考
        memo: string;
        //利用区分
        useAtr: number;
        //休出時間申請の事前必須設定
        prerequisiteForpauseFlg: number;
        // 残業申請の事前必須設定
        otAppSettingFlg: number;
        //時間年休申請の時刻計算を利用する
        holidayTimeAppCalFlg: number;
        // 遅刻早退取消申請の実績取消
        lateOrLeaveAppCancelFlg: number;
        //遅刻早退取消申請の実績取消を申請時に選択
        lateOrLeaveAppSettingFlg: number;
        //休憩入力欄を表示する
        breakInputFieldDisFlg: number;
        //休憩時間を表示する
        breakTimeDisFlg: number;
        //出退勤時刻初期表示区分
        atworkTimeBeginDisFlg: number;
        //実績から外出を初期表示する
        goOutTimeBeginDisFlg: number;
        // 時刻計算利用区分
        timeCalUseAtr: number;
        //時間入力利用区分
        timeInputUseAtr: number;
        //退勤時刻初期表示区分
        timeEndDispFlg: number;
        //指示が必須
        requiredInstructionFlg: number;
        //指示利用設定 - 指示区分
        instructionAtr: number;
        //指示利用設定 - 備考
        instructionMemo: string;
        //指示利用設定 - 利用区分
        instructionUseAtr: number;
    }

    export class ApprovalFunctionSetting {
        //申請種類
        appType: KnockoutObservable<number>;
        //備考
        memo: KnockoutObservable<string>;
        //利用区分
        useAtr: KnockoutObservable<number>;
        //休出時間申請の事前必須設定
        prerequisiteForpauseFlg: KnockoutObservable<number>;
        // 残業申請の事前必須設定
        otAppSettingFlg: KnockoutObservable<number>;
        //時間年休申請の時刻計算を利用する
        holidayTimeAppCalFlg: KnockoutObservable<number>;
        // 遅刻早退取消申請の実績取消
        lateOrLeaveAppCancelFlg: KnockoutObservable<number>;
        //遅刻早退取消申請の実績取消を申請時に選択
        lateOrLeaveAppSettingFlg: KnockoutObservable<number>;
        //休憩入力欄を表示する
        breakInputFieldDisFlg: KnockoutObservable<number>;
        //休憩時間を表示する
        breakTimeDisFlg: KnockoutObservable<number>;
        //出退勤時刻初期表示区分
        atworkTimeBeginDisFlg: KnockoutObservable<number>;
        //実績から外出を初期表示する
        goOutTimeBeginDisFlg: KnockoutObservable<number>;
        // 時刻計算利用区分
        timeCalUseAtr: KnockoutObservable<number>;
        //時間入力利用区分
        timeInputUseAtr: KnockoutObservable<number>;
        //退勤時刻初期表示区分
        timeEndDispFlg: KnockoutObservable<number>;
        //指示が必須
        requiredInstructionFlg: KnockoutObservable<number>;
        //指示利用設定 - 指示区分
        instructionAtr: KnockoutObservable<number>;
        //指示利用設定 - 備考
        instructionMemo: KnockoutObservable<string>;
        //指示利用設定 - 利用区分
        instructionUseAtr: KnockoutObservable<number>;

        constructor(param: IApprovalFunctionSetting, appTypeParam?: number) {
            let self = this;
            self.appType = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.appType : appTypeParam);
            self.memo = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.memo : '');
            self.useAtr = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.useAtr : 1);
            self.prerequisiteForpauseFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.prerequisiteForpauseFlg : 0);
            self.otAppSettingFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.otAppSettingFlg : 0);
            self.holidayTimeAppCalFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.holidayTimeAppCalFlg : 0);
            self.lateOrLeaveAppCancelFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.lateOrLeaveAppCancelFlg : 0);
            self.lateOrLeaveAppSettingFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.lateOrLeaveAppSettingFlg : 0);
            self.breakInputFieldDisFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.breakInputFieldDisFlg : 1);
            if (appTypeParam == 6) {
                self.breakInputFieldDisFlg = ko.observable(1);
            }
            self.breakTimeDisFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.breakTimeDisFlg : 0);
            self.atworkTimeBeginDisFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.atworkTimeBeginDisFlg : 0);
            self.goOutTimeBeginDisFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.goOutTimeBeginDisFlg : 0);
            self.timeCalUseAtr = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.timeCalUseAtr : 0);
            self.timeInputUseAtr = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.timeInputUseAtr : 0);
            self.timeEndDispFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.timeEndDispFlg : 0);
            self.requiredInstructionFlg = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.requiredInstructionFlg : 0);
            self.instructionAtr = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.instructionAtr : 0);
            self.instructionMemo = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.instructionMemo : '');
            self.instructionUseAtr = ko.observable(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.instructionUseAtr : 0);
        }

        update(param: IApprovalFunctionSetting, appTypeParam?: number) {
            let self = this;
            self.appType(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.appType : appTypeParam);
            self.memo(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.memo : '');
            self.useAtr(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.useAtr : 1);
            self.prerequisiteForpauseFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.prerequisiteForpauseFlg : 0);
            self.otAppSettingFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.otAppSettingFlg : 0);
            self.holidayTimeAppCalFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.holidayTimeAppCalFlg : 0);
            self.lateOrLeaveAppCancelFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.lateOrLeaveAppCancelFlg : 0);
            self.lateOrLeaveAppSettingFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.lateOrLeaveAppSettingFlg : 0);
            self.breakInputFieldDisFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.breakInputFieldDisFlg : 0);
            if (appTypeParam == 6) {
                self.breakInputFieldDisFlg = ko.observable(1);
            }
            self.breakTimeDisFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.breakTimeDisFlg : 0);
            self.atworkTimeBeginDisFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.atworkTimeBeginDisFlg : 0);
            self.goOutTimeBeginDisFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.goOutTimeBeginDisFlg : 0);
            self.timeCalUseAtr(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.timeCalUseAtr : 0);
            self.timeInputUseAtr(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.timeInputUseAtr : 0);
            self.timeEndDispFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.timeEndDispFlg : 0);
            self.requiredInstructionFlg(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.requiredInstructionFlg : 0);
            self.instructionAtr(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.instructionAtr : 0);
            self.instructionMemo(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.instructionMemo : '');
            self.instructionUseAtr(param && !nts.uk.util.isNullOrUndefined(param.appType) ? param.instructionUseAtr : 0);
        }
    }
}