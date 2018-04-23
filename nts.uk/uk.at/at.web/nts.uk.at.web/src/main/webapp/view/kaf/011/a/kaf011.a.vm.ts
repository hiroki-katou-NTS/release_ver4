module nts.uk.at.view.kaf011.a.screenModel {

    import dialog = nts.uk.ui.dialog.info;
    import text = nts.uk.resource.getText;
    import formatDate = nts.uk.time.formatDate;
    import common = nts.uk.at.view.kaf011.shr.common;
    import service = nts.uk.at.view.kaf011.shr.service;
    import block = nts.uk.ui.block;
    import jump = nts.uk.request.jump;
    import alError = nts.uk.ui.dialog.alertError;

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

        manualSendMailAtr: KnockoutObservable<number> = ko.observable(1);

        drawalReqSet: KnockoutObservable<common.DrawalReqSet> = ko.observable(new common.DrawalReqSet(null));

        showReason: KnockoutObservable<number> = ko.observable(0);

        displayPrePostFlg: KnockoutObservable<number> = ko.observable(0);

        appTypeSet: KnockoutObservable<common.AppTypeSet> = ko.observable(new common.AppTypeSet(null));

        constructor() {
            let self = this;
            self.appComSelectedCode.subscribe((newCode) => {
                if (newCode == 0) { return; };
                if (newCode == 1) {
                    $("#absDatePinker").ntsError("clear");
                }
                if (newCode == 2) {
                    $("#recDatePicker").ntsError("clear");
                    $("#recTime1Start ,#recTime1End").ntsError("clear");
                }

            });
        }


        start(): JQueryPromise<any> {
            block.invisible();
            var self = this,
                dfd = $.Deferred(),
                startParam = {
                    sID: null,
                    appDate: self.appDate(),
                    uiType: 0
                };

            service.start(startParam).done((data: common.IHolidayShipment) => {
                self.setDataFromStart(data);

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
                self.employeeName(data.employeeName);
                self.prePostSelectedCode(data.preOrPostType);
                self.recWk().setWkTypes(data.recWkTypes || []);
                self.absWk().setWkTypes(data.absWkTypes || []);
                self.appReasons(data.appReasonComboItems || []);
                self.employeeID(data.employeeID);
                self.manualSendMailAtr(data.applicationSetting.manualSendMailAtr);
                self.drawalReqSet(new common.DrawalReqSet(data.drawalReqSet || null));
                self.showReason(data.applicationSetting.appReasonDispAtr);
                self.displayPrePostFlg(data.applicationSetting.displayPrePostFlg);
                self.appTypeSet(new common.AppTypeSet(data.appTypeSet || null));
            }
        }
        validate() {

            $(".kaf-011-combo-box ,.nts-input").trigger("validate");

        }

        register() {
            let self = this,
                saveCmd: common.ISaveHolidayShipmentCommand = {
                    recCmd: ko.mapping.toJS(self.recWk()),
                    absCmd: ko.mapping.toJS(self.absWk()),
                    comType: self.appComSelectedCode(),
                    usedDays: 1,
                    appCmd: {
                        appReasonText: '',
                        applicationReason: self.reason(),
                        prePostAtr: self.prePostSelectedCode(),
                        enteredPersonSID: self.employeeID(),
                        appVersion: 0
                        ,
                    }
                },
                selectedReason = _.find(self.appReasons(), { 'reasonID': self.appReasonSelectedID() }),
                appReason = self.getReason();

            if (selectedReason) {
                saveCmd.appCmd.appReasonText = selectedReason.reasonTemp;
            }
            let isHasErrorWhenCheckLengthReason: boolean = !nts.uk.at.view.kaf000.shr.model.CommonProcess.checklenghtReason(appReason, "#appReason");
            if (isHasErrorWhenCheckLengthReason) {
                return;
            }
            saveCmd.absCmd.changeWorkHoursType = saveCmd.absCmd.changeWorkHoursType ? 1 : 0;
            self.validate();
            if (nts.uk.ui.errors.hasError()) { return; }
            block.invisible();
            service.save(saveCmd).done(() => {
                dialog({ messageId: 'Msg_15' }).then(function() {
                    location.reload();
                });
            }).fail((error) => {
                alError({ messageId: error.messageId, messageParams: error.parameterIds });
            }).always(() => {
                block.clear();
                $("#recDatePicker").focus();
            });
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
            //chưa có màn hình KDL009
            //            nts.uk.ui.windows.sub.modal('/view/kdl/009/a/index.xhtml').onClosed(function(): any {
            //
            //            });

        }




    }





}