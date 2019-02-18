module nts.uk.at.view.kaf011.a.screenModel {

    import dialog = nts.uk.ui.dialog.info;
    import text = nts.uk.resource.getText;
    import formatDate = nts.uk.time.formatDate;
    import common = nts.uk.at.view.kaf011.shr.common;
    import service = nts.uk.at.view.kaf011.shr.service;
    import block = nts.uk.ui.block;
    import jump = nts.uk.request.jump;
    import alError = nts.uk.ui.dialog.alertError;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import appcommon = nts.uk.at.view.kaf000.shr.model;
    export class ViewModel {
        screenModeNew: KnockoutObservable<boolean> = ko.observable(true);
        prePostTypes = ko.observableArray([
            { code: 0, text: text('KAF011_14') },
            { code: 1, text: text('KAF011_15') }]);

        prePostSelectedCode: KnockoutObservable<number> = ko.observable(0);

        appComItems = ko.observableArray([
            { code: 0, text: text('KAF011_19') },
            { code: 1, text: text('KAF011_20') },
            { code: 2, text: text('KAF011_21') },
        ]);
        appComSelectedCode: KnockoutObservable<number> = ko.observable(0);

        appDate: KnockoutObservable<Date> = ko.observable(moment().toDate());

        recWk: KnockoutObservable<common.AppItems> = ko.observable(new common.AppItems());

        absWk: KnockoutObservable<common.AppItems> = ko.observable(new common.AppItems());

        appReasons = ko.observableArray([]);

        appReasonSelectedID: KnockoutObservable<string> = ko.observable('');

        reason: KnockoutObservable<string> = ko.observable('');

        kaf000_a = new kaf000.a.viewmodel.ScreenModel();

        employeeID: KnockoutObservable<string> = ko.observable('');

        employeeName: KnockoutObservable<string> = ko.observable('');

        checkBoxValue: KnockoutObservable<boolean> = ko.observable(false);
        enableSendMail: KnockoutObservable<boolean> = ko.observable(false);

        drawalReqSet: KnockoutObservable<common.DrawalReqSet> = ko.observable(new common.DrawalReqSet(null));

        showReason: KnockoutObservable<number> = ko.observable(0);

        displayPrePostFlg: KnockoutObservable<number> = ko.observable(0);

        appTypeSet: KnockoutObservable<common.AppTypeSet> = ko.observable(new common.AppTypeSet(null));

        employeeList = ko.observableArray([]);

        selectedEmployee = ko.observable(null);

        totalEmployeeText = ko.observable('');

        transferDate: KnockoutObservable<string> = ko.observable(null);
        
        firstLoad: KnockoutObservable<boolean> = ko.observable(false);
        
        remainDays: KnockoutObservable<number> = ko.observable(null);
        constructor() {
            let self = this;

            self.appComSelectedCode.subscribe((newCode) => {
                
                setTimeout(()=>self.clearTextboxError(), 100);
                
            });
            self.appReasons.subscribe((appReasons) => {
                if (appReasons) {
                    let defaultReason = _.find(appReasons, { 'defaultFlg': 1 }),
                        displayFixedReason = self.appTypeSet().displayFixedReason() != 0;

                    if (defaultReason && displayFixedReason) {
                        self.appReasonSelectedID(defaultReason.reasonID);
                    }
                }

            });

            self.recWk().wkTimeCD.subscribe((newWkTimeCD) => {
                if (newWkTimeCD && nts.uk.ui._viewModel) {
                    $('#recTimeBtn').ntsError("clear");
                }
            });

            self.absWk().wkTimeCD.subscribe((newWkTimeCD) => {
                if (newWkTimeCD && nts.uk.ui._viewModel) {
                    $('#absTimeBtn').ntsError("clear");
                }
            });
            
            self.absWk().wkTypeCD.subscribe((newWkTypeCd)=>{
                if (nts.uk.ui._viewModel) {
                    $('.absWkingTime').ntsError("clear");
                }
            });
            
            self.employeeList.subscribe((datas) => {
                if (datas.length) {
                    self.totalEmployeeText(text('KAF011_79', [datas.length]));
                    self.selectedEmployee(datas[0]);
                }

            });
        }
        
        clearTextboxError() {
            nts.uk.ui.errors.clearAll();
            return;
        }
        enablePrepost() {
            let self = this;
            return self.screenModeNew() && self.appTypeSet().canClassificationChange() != 0;
        }

        start(): JQueryPromise<any> {
            block.invisible();
            var self = this,
                dfd = $.Deferred(),
                employeeIDs = [],
                transferDate;

            __viewContext.transferred.ifPresent(data => {
                employeeIDs = data.employeeIds;
                if (!nts.uk.util.isNullOrUndefined(data.appDate)) {
                    transferDate = moment(data.appDate).toDate();
                    self.appDate(transferDate);

                    self.transferDate(transferDate);
                }
            });
            let startParam = {
                sIDs: employeeIDs,
                appDate: self.appDate(),
                uiType: 0
            };

            service.start(startParam).done((data: common.IHolidayShipment) => {
                self.setDataFromStart(data);
                $("#fixed-table").ntsFixedTable({ width: 100 });
            }).fail((error) => {
                alError({ messageId: error.messageId, messageParams: error.parameterIds });
            }).always(() => {
                block.clear();
                dfd.resolve();
                $("#recDatePicker").focus();
            });
            return dfd.promise();
        }



        clearData() {
            let self = this;
            self.recWk().wkTime1(new common.WorkingHour());
            self.recWk().wkTime2(new common.WorkingHour());
            self.recWk().wkTimeName('');
            self.recWk().wkTimeCD('');
            self.recWk().wkText('');
        }

        openCMM018() {
            let self = this;
            jump("com", "/view/cmm/018/a/index.xhtml", { screen: "Application", employeeId: self.employeeID() });
        }

        setDataFromStart(data: common.IHolidayShipment) {
            let self = this;
            if (data) {
                self.remainDays(data.absRecMng);
                self.employeeList(_.map(data.employees, (emp) => { return { sid: emp.sid, code: emp.scd, name: emp.bussinessName } }));
                self.employeeName(data.employeeName);
                self.prePostSelectedCode(data.preOrPostType);
                self.recWk().setWkTypes(data.recWkTypes || []);
                self.absWk().setWkTypes(data.absWkTypes || []);
                self.appReasons(data.appReasonComboItems || []);
                self.employeeID(data.employeeID);
                self.checkBoxValue(data.applicationSetting.manualSendMailAtr == 1 ? true : false);
                self.enableSendMail(!data.sendMailWhenRegisterFlg);
                self.drawalReqSet(new common.DrawalReqSet(data.drawalReqSet || null));
                self.showReason(data.applicationSetting.appReasonDispAtr);
                self.displayPrePostFlg(data.applicationSetting.displayPrePostFlg);
                self.appTypeSet(new common.AppTypeSet(data.appTypeSet || null));
                self.recWk().wkTimeName(data.wkTimeName || null);
                self.recWk().wkTimeCD(data.wkTimeCD || null);
            }
        }
        validateControl() {
            let self = this,
                isRecError = self.checkRecTime(),
                isAbsError = self.checkAbsTime();
            $(".kaf-011-combo-box ,.nts-input,#swb_pre_post_type").trigger("validate");
            let isKibanControlError = nts.uk.ui.errors.hasError();
            return isRecError || isAbsError || isKibanControlError;

        }
        checkRecTime() {
            let self = this,
                comCode = self.appComSelectedCode(),
                isRecCreate = comCode == 1 || comCode == 0,
                isError = false;
            if (isRecCreate) {
                let wkTimeCd = self.recWk().wkTimeCD();
                if (!wkTimeCd) {
                    $('#recTimeBtn').ntsError('set', { messageId: 'MsgB_2', messageParams: [text('KAF011_30')] });
                    isError = true;
                }
            }
            return isError;

        }
        checkAbsTime() {
            let self = this,
                comCode = self.appComSelectedCode(),
                isAbsCreate = comCode == 2 || comCode == 0,
                isError = false;
            if (isAbsCreate) {
                let isUseWkTime = self.absWk().changeWorkHoursType();
                if (isUseWkTime) {
                    let wkTimeCd = self.absWk().wkTimeCD();
                    if (!wkTimeCd) {
                        $('#absTimeBtn').ntsError('set', { messageId: 'MsgB_2', messageParams: [text('KAF011_30')] });
                        isError = true;
                    }
                }
            }
            return isError;
        }
        genSaveCmd(): common.ISaveHolidayShipmentCommand {
            let self = this,
                returnCmd = common.ISaveHolidayShipmentCommand = {
                    recCmd: ko.mapping.toJS(self.recWk()),
                    absCmd: ko.mapping.toJS(self.absWk()),
                    comType: self.appComSelectedCode(),
                    usedDays: 1,
                    appCmd: {
                        appReasonText: '',
                        applicationReason: self.reason(),
                        prePostAtr: self.prePostSelectedCode(),
                        employeeID: self.employeeList()[0] ? self.employeeList()[0].sid : null,
                        appVersion: 0,
                        remainDays: self.remainDays()
                    }
                }, selectedReason = self.appReasonSelectedID() ? _.find(self.appReasons(), { 'reasonID': self.appReasonSelectedID() }) : null;
            if (selectedReason) {
                returnCmd.appCmd.appReasonText = selectedReason.reasonTemp;
            }
            returnCmd.absCmd.changeWorkHoursType = returnCmd.absCmd.changeWorkHoursType ? 1 : 0;
            return returnCmd;

        }
        register() {
            let self = this,
                saveCmd = self.genSaveCmd();

            let isControlError = self.validateControl();
            if (isControlError) { return; }

            // let isCheckReasonError = !self.checkReason(),
            let checkBoxValue = self.checkBoxValue();
            // if (isCheckReasonError) { return; }
            block.invisible();
            service.save(saveCmd).done((data) => {
                dialog({ messageId: 'Msg_15' }).then(function() {
                    if (data.autoSendMail) {
                        appcommon.CommonProcess.displayMailResult(data);
                    } else {
                        if (checkBoxValue) {
                            appcommon.CommonProcess.openDialogKDL030(data.appID);
                        } else {
                            location.reload();
                        }
                    }
                });
            }).fail((error) => {
                alError({ messageId: error.messageId, messageParams: error.parameterIds });
            }).always(() => {
                block.clear();
                $("#recDatePicker").focus();
            });
        }

        showAppReason(): boolean {
            let self = this;
            if (self.screenModeNew()) {
                return self.appTypeSet().displayAppReason() != 0;
            } else {
                return self.appTypeSet().displayAppReason() != 0 || self.appTypeSet().displayFixedReason() != 0;
            }

        }

        checkReason(): boolean {
            let self = this,
                appReason = self.getReason();
            let appReasonError = !appcommon.CommonProcess.checkAppReason(true, self.appTypeSet().displayFixedReason() != 0, self.appTypeSet().displayAppReason() != 0, appReason);
            if (appReasonError) {
                nts.uk.ui.dialog.alertError({ messageId: 'Msg_115' });
                return false;
            }
            let isCheckLengthError: boolean = !appcommon.CommonProcess.checklenghtReason(appReason, "#appReason");
            if (isCheckLengthError) {
                return false;
            }
            return true;
        }

        getReason(): string {
            let appReason = '',
                self = this,
                inputReasonID = self.appReasonSelectedID(),
                inputReasonList = self.appReasons(),
                detailReason = self.reason();
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

        openKDL009() {
            let self = this;
            let lstid = [];
            _.each(self.employeeList(), function(emp) {
                lstid.push(emp.sid);
            });
            let data = {
                employeeIds: lstid.length > 0 ? lstid : [self.employeeID()],
                baseDate: moment(new Date()).format("YYYYMMDD")
            }
            setShared('KDL009_DATA', data);
            if (data.employeeIds.length > 1) {
                modal("/view/kdl/009/a/multi.xhtml");
            } else {
                modal("/view/kdl/009/a/single.xhtml");
            }
        }
    }





}