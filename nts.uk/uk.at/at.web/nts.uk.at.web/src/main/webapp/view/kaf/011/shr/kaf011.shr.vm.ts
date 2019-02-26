module nts.uk.at.view.kaf011.shr {

    import dialog = nts.uk.ui.dialog.info;
    import text = nts.uk.resource.getText;
    import formatDate = nts.uk.time.formatDate;
    import block = nts.uk.ui.block;
    import alError = nts.uk.ui.dialog.alertError;

    export module common {


        export interface IWorkingHour {
            startTime: Date;
            endTime: Date;
            startType: number;
            endType: number;
        }
        export class WorkingHour {
            startTime: KnockoutObservable<number> = ko.observable(null);
            startTimeDisplay: KnockoutObservable<number> = ko.observable(null);
            endTime: KnockoutObservable<number> = ko.observable(null);
            endTimeDisplay: KnockoutObservable<number> = ko.observable(null);
            startTypes: KnockoutObservableArray<any> = ko.observableArray([
                { code: 1, text: text('KAF011_39') },
                { code: 0, text: text('KAF011_40') }
            ]);
            startType: KnockoutObservable<number> = ko.observable(0);
            endTypes: KnockoutObservableArray<any> = ko.observableArray([
                { code: 1, text: text('KAF011_42') },
                { code: 0, text: text('KAF011_43') }
            ]);
            endType: KnockoutObservable<number> = ko.observable(0);


            timeOption = ko.mapping.fromJS({
                timeWithDay: true,
                width: "70px"
            });
            constructor(IWorkingHour?) {
                if (IWorkingHour) {
                    let startTime = IWorkingHour.startTime || null,
                        endTime = IWorkingHour.endTime || null;
                    this.startTime(startTime);
                    this.endTime(endTime);
                    this.startType(IWorkingHour.startType != undefined ? IWorkingHour.startType : 1);
                    this.endType(IWorkingHour.endType != undefined ? IWorkingHour.endType : 1);
                    this.startTimeDisplay(startTime);
                    this.endTimeDisplay(endTime);
                }
            }
            clearData() {
                this.startTime(null);
                this.endTime(null);
                this.startTimeDisplay(null);
                this.endTimeDisplay(null);
            }
            updateData(param) {
                if (param) {
                    let startTime = param.startTime,
                        endTime = param.endTime;
                    this.startTime(startTime);
                    this.endTime(endTime);
                    this.startTimeDisplay(startTime);
                    this.endTimeDisplay(endTime);
                }
            }
        }
        export interface IWorkTime {
            selectedWorkTypeCode: string;
            first: any;
            second: any;
            selectedWorkTimeCode: string;
            selectedWorkTimeName: string;

        }

        export interface IHolidayShipment {
            applicationSetting: any;
            appEmploymentSettings: Array<any>;
            approvalFunctionSetting: any;
            refDate: string;
            recWkTypes: Array<any>;
            absWkTypes: Array<any>;
            preOrPostType: any;
            appReasonComboItems: Array<any>;
            employeeID: string;
            employeeName: string;
            drawalReqSet: any;
            appTypeSet: any;
            absApp: any;
            application: any;
            recApp: any;
            transferDate: any;
        }

        export interface IAppTypeSet {
            appType: number;
            canClassificationChange: number;
            companyId: string;
            displayAppReason: number;
            displayFixedReason: number;
            displayInitialSegment: number;
            sendMailWhenApproval: number;
            sendMailWhenRegister: number;

        }

        export class AppTypeSet {
            appType: KnockoutObservable<number> = ko.observable(0);
            canClassificationChange: KnockoutObservable<number> = ko.observable(0);
            companyId: KnockoutObservable<string> = ko.observable('');
            displayAppReason: KnockoutObservable<number> = ko.observable(0);
            displayFixedReason: KnockoutObservable<number> = ko.observable(0);
            displayInitialSegment: KnockoutObservable<number> = ko.observable(0);
            sendMailWhenApproval: KnockoutObservable<number> = ko.observable(0);
            sendMailWhenRegister: KnockoutObservable<number> = ko.observable(0);
            constructor(data: IAppTypeSet) {
                if (data) {
                    this.appType(data.appType);
                    this.canClassificationChange(data.canClassificationChange);
                    this.companyId(data.companyId);
                    this.displayAppReason(data.displayAppReason);
                    this.displayFixedReason(data.displayFixedReason);
                    this.displayInitialSegment(data.displayInitialSegment);
                    this.sendMailWhenApproval(data.sendMailWhenApproval);
                    this.sendMailWhenRegister(data.sendMailWhenRegister);
                }

            }
        }

        export interface IWorkType {
            /* 勤務種類コード */
            workTypeCode: string;
            /* 勤務種類名称 */
            name: string;
        }
        export interface ICallerParameter {
            workTypeCodes: Array<string>;
            selectedWorkTypeCode: string;
            workTimeCodes: Array<string>;
            selectedWorkTimeCode: string;
        }

        export class DrawalReqSet {
            deferredComment: KnockoutObservable<string> = ko.observable('');
            deferredBold: KnockoutObservable<boolean> = ko.observable(false);
            deferredLettleColor: KnockoutObservable<string> = ko.observable('');
            pickUpComment: KnockoutObservable<string> = ko.observable('');
            pickUpBold: KnockoutObservable<boolean> = ko.observable(false);
            pickUpLettleColor: KnockoutObservable<string> = ko.observable('');
            deferredWorkTimeSelect: KnockoutObservable<number> = ko.observable(0);
            simulAppliReq: KnockoutObservable<number> = ko.observable(0);
            permissionDivision: KnockoutObservable<any> = ko.observable(null);
            constructor(drawalReqSet) {
                if (drawalReqSet) {
                    this.deferredComment(drawalReqSet.deferredComment || '');
                    this.deferredBold(drawalReqSet.deferredBold || false);
                    this.pickUpComment(drawalReqSet.pickUpComment || '');
                    this.pickUpBold(drawalReqSet.pickUpBold || false);
                    this.deferredWorkTimeSelect(drawalReqSet.deferredWorkTimeSelect || 0);
                    this.simulAppliReq(drawalReqSet.simulAppliReq || 0);
                    this.permissionDivision(drawalReqSet.permissionDivision != undefined ? drawalReqSet.permissionDivision : 1);
                    this.deferredLettleColor(drawalReqSet.deferredLettleColor);
                    this.pickUpLettleColor(drawalReqSet.pickUpLettleColor);
                    let comItems;
                    if (this.simulAppliReq() == 1) {
                        comItems = [
                            { code: 0, text: text('KAF011_19') }
                        ]

                    } else {
                        comItems = [
                            { code: 0, text: text('KAF011_19') },
                            { code: 1, text: text('KAF011_20') },
                            { code: 2, text: text('KAF011_21') },
                        ]

                    }

                    let vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'];
                    vm.appComItems(comItems);
                }
            }
        }
        export class AppItems {
            appID: KnockoutObservable<string> = ko.observable(null);
            wkTypes: KnockoutObservableArray<IWorkType> = ko.observableArray([]);
            wkType: KnockoutObservable<WkType> = ko.observable(new WkType(null));
            wkTypeCD: KnockoutObservable<string> = ko.observable(null);
            wkTimeCD: KnockoutObservable<string> = ko.observable(null);
            wkTimeName: KnockoutObservable<string> = ko.observable('');
            wkTime1: KnockoutObservable<WorkingHour> = ko.observable(new WorkingHour());
            wkTime2: KnockoutObservable<WorkingHour> = ko.observable(new WorkingHour());
            wkText: KnockoutObservable<string> = ko.observable('');
            appDate: KnockoutObservable<Date> = ko.observable(null);
            changeWorkHoursType: KnockoutObservable<any> = ko.observable(false);

            constructor(data) {
                let self = this;
                if (data) {
                    self.appID(data.appID);
                    self.wkTypeCD(data.wkTypeCD);
                    self.wkTimeCD(data.wkTimeCD);
                    self.wkTime1(new WorkingHour(data.wkTime1));
                    self.wkTime2(new WorkingHour(data.wkTime2));
                    self.appDate(data.appDate);
                    self.changeWorkHoursType(data.changeWorkHoursType);
                }
                self.wkTimeCD.subscribe((newWkTimeCD) => {
                    let self = this,
                        vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'],
                        changeWkTypeParam = {
                            wkTypeCD: self.wkTypeCD(),
                            wkTimeCD: newWkTimeCD
                        };
                    if (newWkTimeCD && !vm.firstLoad()) {
                        block.invisible();
                        service.getSelectedWorkingHours(changeWkTypeParam).done((data: IChangeWorkType) => {
                            self.setDataFromWkDto(data);
                        }).fail(() => {

                        }).always(() => {
                            self.updateWorkingText();
                            block.clear();
                        });
                    }

                });
                self.wkTypeCD.subscribe((newWkType) => {
                    let vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'];
                    if (!_.isEmpty(newWkType) && !vm.firstLoad()) {
                        let changeWkTypeParam = {
                            wkTypeCD: newWkType,
                            wkTimeCD: self.wkTimeCD()
                        };
                        block.invisible();
                        service.changeWkType(changeWkTypeParam).done((data: IChangeWorkType) => {
                            self.setDataFromWkDto(data);
                        }).always(() => { block.clear(); });
                    }
                });

                self.wkTypes.subscribe((items) => {
                    if (items.length && !(_.find(items, ['workTypeCode', self.wkTypeCD()]))) {
                        self.wkTypeCD(items[0].workTypeCode);
                    }

                });

                self.appDate.subscribe((newDate) => {
                    let vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'],
                        absDate = vm.absWk().appDate(),
                        recDate = vm.recWk().appDate(),
                        changeDateParam = {
                            holidayDate: absDate,
                            takingOutDate: recDate,
                            comType: vm.appComSelectedCode(),
                            uiType: 0
                        }

                    if (!newDate || !vm.screenModeNew() || nts.uk.ui.errors.hasError()) { return; }
                    block.invisible();
                    service.changeDay(changeDateParam).done((data: IHolidayShipment) => {
                        vm.recWk().setWkTypes(data.recWkTypes || []);
                        vm.absWk().setWkTypes(data.absWkTypes || []);
                        if(vm.displayPrePostFlg()==0){
                            vm.prePostSelectedCode(data.preOrPostType);    
                        }
                        vm.kaf000_a.start(vm.employeeID(), 1, 10, moment(data.refDate).format("YYYY/MM/DD")).done(() => {
                        });
                    }).always(() => {
                        block.clear();
                    });;
                });
            }
            setDataFromWkDto(data) {
                let self = this,
                    vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'];

                if (data) {
                    if (!vm.firstLoad()) {
                        if (data.timezoneUseDtos) {
                            $("#recTime1Start").ntsError("clear");
                            $("#recTime1End").ntsError("clear");
                            let timeZone1 = data.timezoneUseDtos[0];
                            let timeZone2 = data.timezoneUseDtos[1];

                            timeZone1 ? self.wkTime1().updateData(timeZone1) : self.wkTime1().clearData();

                            timeZone2 ? self.wkTime2().updateData(timeZone2) : self.wkTime2().clearData();

                        } else {
                            self.wkTime1().clearData();
                            self.wkTime2().clearData();
                        }
                    }
                    if (data.wkType) {
                        self.wkType().workAtr(data.wkType.workAtr);
                        self.wkType().morningCls(data.wkType.morningCls);
                        self.wkType().afternoonCls(data.wkType.afternoonCls);
                    }
                }
            }
            setWkTypes(wkTypeDtos: Array<any>) {
                let self = this;
                this.wkTypes(_.map(wkTypeDtos, wkType => {
                    return { workTypeCode: wkType.workTypeCode, name: wkType.workTypeCode + ' ' + wkType.name };
                }));


            }

            enableWkTime() {
                let self = this, vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'];
                if (!self.changeWorkHoursType() || vm.drawalReqSet().permissionDivision() == 0) {
                    return false;
                }

                return true;

            }

            showAbsWorkTimeZone() {
                let self = this, vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'],
                    workAtr = self.wkType().workAtr(),
                    wkTimeSelect = vm.drawalReqSet().deferredWorkTimeSelect();

                //利用しない
                if (workAtr == 0 || wkTimeSelect == 0) {
                    return false;
                }

                let morningType = self.wkType().morningCls(),
                    afternoonType = self.wkType().afternoonCls(),
                    Pause = 8,
                    Attendance = 0;
                //利用する
                if (wkTimeSelect == 1) {
                    //午前と午後
                    if (workAtr == 1) {
                        let isAHalfWorkDay = (afternoonType == Attendance && morningType == Pause) || (afternoonType == Pause && morningType == Attendance);
                        if (isAHalfWorkDay) {
                            return true;
                        } else {
                            let wkType = "1234569",
                                isAHalfDayOff = (wkType.indexOf(afternoonType) != -1 && morningType == Pause) || (wkType.indexOf(morningType) != -1 && afternoonType == Pause);
                            if (isAHalfDayOff) {
                                return true;
                            } else {
                                return false;
                            }

                        }
                    }
                }
                //半休時のみ利用する
                if (wkTimeSelect == 2) {
                    //午前と午後
                    if (workAtr == 1) {
                        if (self.isBothTypeNotAttendance(afternoonType, morningType)) {
                            return false;
                        }
                    } else {
                        return false;
                    }

                }
                return true;
            }

            showWorkingTime1() {
                let self = this, vm: nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'],
                    workAtr = self.wkType().workAtr(),
                    wkTimeSelect = vm.drawalReqSet().deferredWorkTimeSelect();
                ////利用しない
                if (workAtr == 0 || wkTimeSelect == 0) {
                    return false;
                }
                let morningType = self.wkType().morningCls(),
                    afternoonType = self.wkType().afternoonCls(),
                    Pause = 8,
                    Attendance = 0;
                //半休時のみ利用する
                //利用する
                let isWkTimeIsHoliday = wkTimeSelect == 1 || wkTimeSelect == 2;
                if (isWkTimeIsHoliday) {
                    if (workAtr == 1) {
                        if (self.isBothTypeNotAttendance(afternoonType, morningType)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
            isBothTypeNotAttendance(afternoonType, morningType) {
                let Attendance = 0;
                return afternoonType != Attendance && morningType != Attendance;
            }

            parseTime(value) {
                return nts.uk.time.parseTime(value, true).format()
            }
            updateWorkingText() {
                let self = this,
                    wkTimeCDText = self.wkTimeCD() || "",
                    wkTimeNameText = self.wkTimeName() || text('KAF011_68'),
                    wkText = self.wkTimeName() ? text('KAF011_70', [wkTimeCDText, wkTimeNameText, '', '', '']) : text('KAF011_68', [wkTimeCDText]);
                self.wkText(wkText);
            }

            changeAbsDateToHoliday() {
                let self = __viewContext['viewModel'], saveCmd: common.ISaveHolidayShipmentCommand = {
                    recCmd: ko.mapping.toJS(self.recWk()),
                    absCmd: ko.mapping.toJS(self.absWk()),
                    comType: self.appComSelectedCode(),
                    usedDays: 1,
                    appCmd: {
                        appReasonText: self.appReasonSelectedID(),
                        applicationReason: self.reason(),
                        prePostAtr: self.prePostSelectedCode(),
                        enteredPersonSID: self.employeeID(),
                        appVersion: self.version(),
                    }
                };
                block.invisible();
                service.changeAbsDateToHoliday(saveCmd).done((payoutType) => {
                    nts.uk.request.jump("/view/kaf/010/a/index.xhtml", { appID: self.absWk().appID(), appDate: self.absWk().appDate(), payoutType: payoutType, applicant: [self.employeeID()], uiType: 1 });
                }).fail((error) => {
                    alError({ messageId: error.messageId, messageParams: error.parameterIds });
                }).always(() => {
                    block.clear();
                });


            }


            openCDialog() {
                let self = this,
                    vm = nts.uk.at.view.kaf011.a.screenModel.ViewModel = __viewContext['viewModel'];
                nts.uk.ui.windows.setShared('KAF_011_PARAMS', {
                    prePostSelectedCode: vm.prePostSelectedCode(),
                    appReasons: vm.appReasons(),
                    reason: vm.reason(),
                    appReasonSelectedID: vm.appReasonSelectedID(),
                    absApp: ko.mapping.toJS(vm.absWk()),
                    version: vm.version,
                    employeeID : vm.employeeID()
                }, true);

                nts.uk.ui.windows.sub.modal('/view/kaf/011/c/index.xhtml').onClosed(function(): any {

                    let newAbsAppID = nts.uk.ui.windows.getShared('KAF_011_C_PARAMS');
                    if (newAbsAppID) {
                        if (newAbsAppID != 'Msg_198') {
                            vm.startPage(newAbsAppID, true);
                        } else {
                            nts.uk.request.jump('../../../cmm/045/a/index.xhtml');
                        }
                    }

                });

            }



            openKDL003() {
                let self = this,
                    selectedWorkTypeCode = self.wkTypeCD(),
                    WorkTimeCd = self.wkTimeCD();

                nts.uk.ui.windows.setShared('parentCodes', {
                    workTypeCodes: [selectedWorkTypeCode],
                    selectedWorkTypeCode: selectedWorkTypeCode,
                    workTimeCodes: [],
                    selectedWorkTimeCode: WorkTimeCd,
                }, true);


                nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(function(): any {
                    //view all code of selected item 
                    var childData: IWorkTime = nts.uk.ui.windows.getShared('childData');
                    if (childData) {
                        let oldCD = self.wkTimeCD();
                        self.wkTimeCD(childData.selectedWorkTimeCode);
                        if (oldCD == self.wkTimeCD()) {
                            self.wkTimeCD.valueHasMutated();
                        }
                        if (childData.first) {
                            $("#recTime1Start").ntsError("clear");
                            $("#recTime1End").ntsError("clear");
                            self.wkTimeName(childData.selectedWorkTimeName);
                        }
                        if (childData.selectedWorkTimeCode && childData.selectedWorkTimeName) {
                            self.updateWorkingText();
                        }
                    }
                });

            }
        }
        export class WkType {
            workAtr: KnockoutObservable<any> = ko.observable(null);
            afternoonCls: KnockoutObservable<any> = ko.observable(null);
            morningCls: KnockoutObservable<any> = ko.observable(null);
            constructor(wkType?) {
                if (wkType) {
                    this.workAtr(wkType.workAtr);
                    this.afternoonCls(wkType.afternoonCls);
                    this.morningCls(wkType.morningCls);
                }
            }
        }
        export interface ISaveHolidayShipmentCommand {
            recCmd: IRecruitmentAppCommand;
            absCmd: IAbsenceLeaveAppCommand;
            comType: number;
            usedDays: number;
            appCmd: IApplicationCommand;
        }

        export interface IRecruitmentAppCommand {
            appDate: string;
            wkTypeCD: string;
            workLocationCD: string;
            wkTime1: IWkTimeCommand;
            wkTime2: IWkTimeCommand;
        }
        export interface IWkTimeCommand {
            startTime: number;
            startType: number;
            endTime: number;
            endType: number;
            wkTimeCD: string;
        }

        export interface IAbsenceLeaveAppCommand {
            appDate: string;
            wkTypeCD: string;
            changeWorkHoursType: number;
            workLocationCD: string;
            wkTime1: IWkTimeCommand;
            wkTime2: IWkTimeCommand;
        }

        export interface IApplicationCommand {
            appReasonText: string;
            applicationReason: string;
            prePostAtr: number;
            enteredPersonSID: string;
            appVersion: number;
        }
        export interface IChangeWorkType {
            timezoneUseDtos: Array<ITimezoneUse>;
            wkType: any;
        }

        export interface ITimezoneUse {
            useAtr: number;
            workNo: number;
            start: number;
            end: number;
        }


        export interface IApprovalSetting {
            bottomComment: boolean;
            bottomCommentFontColor: string;
            bottomCommentFontWeight: boolean;
            companyID: string;
            resultDisp: number;
            stampAtr_Care_Disp: number;
            stampAtr_Child_Care_Disp: number;
            stampAtr_GoOut_Disp: number;
            stampAtr_Sup_Disp: number;
            stampAtr_Work_Disp: number;
            stampGoOutAtr_Compensation_Disp: number;
            stampGoOutAtr_Private_Disp: number;
            stampGoOutAtr_Public_Disp: number;
            stampGoOutAtr_Union_Disp: number;
            stampPlaceDisp: number;
            supFrameDispNO: number;
            topComment: string;
            topCommentFontColor: string;
            topCommentFontWeight: boolean;
        }
    }
}