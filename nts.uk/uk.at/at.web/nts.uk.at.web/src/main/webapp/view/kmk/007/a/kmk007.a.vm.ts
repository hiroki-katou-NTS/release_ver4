module nts.uk.at.view.kmk007.a.viewmodel {
    export class ScreenModel {
        columns: KnockoutObservableArray<any>;
        currentCode: KnockoutObservable<any>;
        currentWorkType: KnockoutObservable<WorkType>;
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;

        itemListOneDay: KnockoutObservableArray<ItemModel>;
        listWorkType: KnockoutObservableArray<any> = ko.observableArray([]);
        listSpecialHlFrame: KnockoutObservableArray<any>;
        listAbsenceFrame: KnockoutObservableArray<any>;
        oneDay: KnockoutObservable<WorkTypeSet>;
        currentOneDayCls: KnockoutObservable<number>;
        currentMorningCls: KnockoutObservable<number>;
        currentAfternoonCls: KnockoutObservable<number>;
        currentOneDay: KnockoutObservable<WorkTypeSet>;
        currentMorning: KnockoutObservable<WorkTypeSet>;
        currentAfternoon: KnockoutObservable<WorkTypeSet>;
        itemHodidayAtr: KnockoutObservableArray<ItemModel>;
        itemCloseAtr: KnockoutObservableArray<ItemModel>;
        itemListHaftDay: KnockoutObservableArray<ItemModel>;
        itemCalculatorMethod: KnockoutObservableArray<ItemModel>;
        enableMethod: KnockoutObservable<boolean>;
        isCreated: KnockoutObservable<boolean>;
        index: KnockoutObservable<number>;

        checkDisabled: KnockoutObservable<boolean> = ko.observable(false);
        isEnable: KnockoutObservable<boolean> = ko.observable(true);
        langId: KnockoutObservable<string> = ko.observable('ja');


        constructor() {
            var self = this,
                lwt: any = self.listWorkType,
                iwork: any = {
                    workTypeCode: '',
                    workAtr: 0,
                    digestPublicHd: 0,
                    holidayAtr: 0,
                    countHodiday: 0,
                    closeAtr: 0,
                    sumAbsenseNo: 0,
                    sumSpHodidayNo: 0,
                    timeLeaveWork: 0,
                    attendanceTime: 0,
                    genSubHodiday: 0,
                    dayNightTimeAsk: 0
                };

            self.selectedRuleCode = ko.observable(1);
            self.listSpecialHlFrame = ko.observableArray([]);
            self.listAbsenceFrame = ko.observableArray([]);

            self.oneDay = ko.observable(new WorkTypeSet(iwork));
            self.currentOneDay = ko.observable(new WorkTypeSet(iwork));
            self.currentMorning = ko.observable(new WorkTypeSet(iwork));
            self.currentAfternoon = ko.observable(new WorkTypeSet(iwork));

            self.enableMethod = ko.observable(false);
            self.isCreated = ko.observable(false);
            self.index = ko.observable(0);
            self.currentOneDayCls = ko.observable(0);
            self.currentMorningCls = ko.observable(0);
            self.currentAfternoonCls = ko.observable(0);
            self.currentWorkType = ko.observable(new WorkType({
                workTypeCode: '',
                name: '',
                abbreviationName: '',
                symbolicName: '',
                abolishAtr: 0,
                memo: '',
                workAtr: 0,
                oneDayCls: 0,
                morningCls: 0,
                afternoonCls: 0,
                calculatorMethod: 0,
                oneDay: ko.toJS(self.oneDay),
                morning: ko.toJS(self.oneDay),
                afternoon: ko.toJS(self.oneDay),
            }));


            //1日-勤務種類の分類 
            self.itemListOneDay = ko.observableArray([
                new ItemModel(0, nts.uk.resource.getText('Enum_WorkTypeClassification_Attendance'), 1),
                new ItemModel(1, nts.uk.resource.getText('Enum_WorkTypeClassification_Holiday'), 6),
                new ItemModel(2, nts.uk.resource.getText('Enum_WorkTypeClassification_AnnualHoliday'), 1),
                new ItemModel(3, nts.uk.resource.getText('Enum_WorkTypeClassification_YearlyReserved'), 1),
                new ItemModel(4, nts.uk.resource.getText('Enum_WorkTypeClassification_SpecialHoliday'), 2),
                new ItemModel(5, nts.uk.resource.getText('Enum_WorkTypeClassification_Absence'), 3),
                new ItemModel(6, nts.uk.resource.getText('Enum_WorkTypeClassification_SubstituteHoliday'), 5),
                new ItemModel(7, nts.uk.resource.getText('Enum_WorkTypeClassification_Shooting'), 1),
                new ItemModel(8, nts.uk.resource.getText('Enum_WorkTypeClassification_Pause'), 6),
                new ItemModel(9, nts.uk.resource.getText('Enum_WorkTypeClassification_TimeDigestVacation'), 4),
                new ItemModel(10, nts.uk.resource.getText('Enum_WorkTypeClassification_ContinuousWork'), 7),
                new ItemModel(11, nts.uk.resource.getText('Enum_WorkTypeClassification_HolidayWork'), 7),
                new ItemModel(12, nts.uk.resource.getText('Enum_WorkTypeClassification_LeaveOfAbsence'), 7),
                new ItemModel(13, nts.uk.resource.getText('Enum_WorkTypeClassification_Closure'), 7)
            ]);

            //午前と午後-勤務種類の分類 
            self.itemListHaftDay = ko.observableArray([
                new ItemModel(0, nts.uk.resource.getText('Enum_WorkTypeClassification_Attendance'), 1),
                new ItemModel(1, nts.uk.resource.getText('Enum_WorkTypeClassification_Holiday'), 6),
                new ItemModel(2, nts.uk.resource.getText('Enum_WorkTypeClassification_AnnualHoliday'), 1),
                new ItemModel(3, nts.uk.resource.getText('Enum_WorkTypeClassification_YearlyReserved'), 1),
                new ItemModel(4, nts.uk.resource.getText('Enum_WorkTypeClassification_SpecialHoliday'), 2),
                new ItemModel(5, nts.uk.resource.getText('Enum_WorkTypeClassification_Absence'), 3),
                new ItemModel(6, nts.uk.resource.getText('Enum_WorkTypeClassification_SubstituteHoliday'), 5),
                new ItemModel(7, nts.uk.resource.getText('Enum_WorkTypeClassification_Shooting'), 1),
                new ItemModel(8, nts.uk.resource.getText('Enum_WorkTypeClassification_Pause'), 6),
                new ItemModel(9, nts.uk.resource.getText('Enum_WorkTypeClassification_TimeDigestVacation'), 4)
            ]);


            //休日区分
            self.itemHodidayAtr = ko.observableArray([
                new ItemModel(0, nts.uk.resource.getText('Enum_HolidayAtr_STATUTORY_HOLIDAYS'), 0),
                new ItemModel(1, nts.uk.resource.getText('Enum_HolidayAtr_NON_STATUTORY_HOLIDAYS'), 0),
                new ItemModel(2, nts.uk.resource.getText('Enum_HolidayAtr_PUBLIC_HOLIDAY'), 0)
            ]);

            //休業区分
            self.itemCloseAtr = ko.observableArray([
                new ItemModel(0, nts.uk.resource.getText('Enum_CloseAtr_PRENATAL'), 0),
                new ItemModel(1, nts.uk.resource.getText('Enum_CloseAtr_POSTPARTUM'), 0),
                new ItemModel(2, nts.uk.resource.getText('Enum_CloseAtr_CHILD_CARE'), 0),
                new ItemModel(3, nts.uk.resource.getText('Enum_CloseAtr_CARE'), 0)
            ]);

            //出勤率の計算方法
            self.itemCalculatorMethod = ko.observableArray([
                new ItemModel(0, nts.uk.resource.getText('Enum_CalculateMethod_DO_NOT_GO_TO_WORK'), 0),
                new ItemModel(1, nts.uk.resource.getText('Enum_CalculateMethod_MAKE_ATTENDANCE_DAY'), 0),
                new ItemModel(2, nts.uk.resource.getText('Enum_CalculateMethod_EXCLUDE_FROM_WORK_DAY'), 0),
                new ItemModel(3, nts.uk.resource.getText('Enum_CalculateMethod_TIME_DIGEST_VACATION'), 0),
            ]);

            self.roundingRules = ko.observableArray([
                { code: '0', name: nts.uk.resource.getText('KMK007_19') },
                { code: '1', name: nts.uk.resource.getText('KMK007_20') }
            ]);


            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMK007_7'), key: 'workTypeCode', width: 70, formatter: _.escape },
                { headerText: nts.uk.resource.getText('KMK007_8'), key: 'name', width: 110, formatter: _.escape },
                { headerText: nts.uk.resource.getText('KMK007_10'), key: 'icon', width: 30 }
            ]);

            self.currentCode = ko.observable();

            self.currentWorkType().oneDayCls.subscribe(function(newOneDayCls) {
                self.checkCalculatorMethod(newOneDayCls);

                if (newOneDayCls == self.currentOneDayCls()) {
                    self.setWorkTypeSet(self.currentWorkType().oneDay(), ko.toJS(self.currentOneDay()));
                } else {
                    self.setWorkTypeSet(self.currentWorkType().oneDay(), ko.toJS(self.oneDay));
                }
            });

            self.currentWorkType().morningCls.subscribe(function(newOneDayCls) {

                let afternoonCode = self.currentWorkType().afternoonCls();
                if (self.itemListOneDay()[newOneDayCls].priority < self.itemListOneDay()[afternoonCode].priority) {
                    self.checkCalculatorMethod(newOneDayCls);
                } else {
                    self.checkCalculatorMethod(afternoonCode);
                }

                if (newOneDayCls == self.currentMorningCls()) {
                    self.setWorkTypeSet(self.currentWorkType().morning(), ko.toJS(self.currentMorning));
                } else {
                    self.setWorkTypeSet(self.currentWorkType().morning(), ko.toJS(self.oneDay));
                }
            });

            self.currentWorkType().afternoonCls.subscribe(function(newOneDayCls) {
                let morningCode = self.currentWorkType().morningCls();
                if (self.itemListOneDay()[newOneDayCls].priority < self.itemListOneDay()[morningCode].priority) {
                    self.checkCalculatorMethod(newOneDayCls);
                } else {
                    self.checkCalculatorMethod(morningCode);
                }

                if (newOneDayCls == self.currentAfternoonCls()) {
                    self.setWorkTypeSet(self.currentWorkType().afternoon(), ko.toJS(self.currentAfternoon));
                } else {
                    self.setWorkTypeSet(self.currentWorkType().afternoon(), ko.toJS(self.oneDay));
                }
            });

            self.currentCode.subscribe(function(newValue) {
                nts.uk.ui.errors.clearAll();
                if (newValue) {
                    self.checkDisabled(false);
                    $('#input-workTypeName').focus();
                    self.isCreated(false);

                    self.index(_.findIndex(ko.toJS(lwt), x => x.workTypeCode == newValue));

                    service.findWorkType(newValue).done(function(workTypeRes) {
                        let itemWorkType: any = ko.toJS(self.convertToModel(workTypeRes)),
                            itemWorkTypeLang = _.find(ko.toJS(lwt), (x: IWorkType) => x.workTypeCode == newValue);

                        //set current worktypeset
                        self.currentOneDay(itemWorkType.oneDay);
                        self.currentMorning(itemWorkType.morning);
                        self.currentAfternoon(itemWorkType.afternoon);

                        //set current code 
                        self.currentOneDayCls(itemWorkType.oneDayCls);
                        self.currentMorningCls(itemWorkType.morningCls);
                        self.currentAfternoonCls(itemWorkType.afternoonCls);

                        let cwt = self.currentWorkType();
                        {
                            cwt.workTypeCode(itemWorkType.workTypeCode);
                            cwt.name(itemWorkType.name);
                            cwt.nameNotJP(itemWorkTypeLang.nameNotJP);
                            cwt.dispName(self.langId() == 'ja' ? itemWorkType.name : itemWorkTypeLang.nameNotJP);
                            cwt.abbreviationName(itemWorkType.abbreviationName);
                            cwt.abNameNotJP(itemWorkTypeLang.abNameNotJP);
                            cwt.dispAbName(self.langId() == 'ja' ? itemWorkType.abbreviationName : itemWorkTypeLang.abNameNotJP);
                            cwt.symbolicName(itemWorkType.symbolicName);
                            cwt.abolishAtr(itemWorkType.abolishAtr);
                            cwt.memo(itemWorkType.memo);
                            cwt.workAtr(itemWorkType.workAtr);
                            cwt.oneDayCls(itemWorkType.oneDayCls);
                            cwt.morningCls(itemWorkType.morningCls);
                            cwt.afternoonCls(itemWorkType.afternoonCls);
                            cwt.calculatorMethod(itemWorkType.calculatorMethod);
                        }

                        self.setWorkTypeSet(cwt.oneDay(), itemWorkType.oneDay);
                        self.setWorkTypeSet(cwt.morning(), itemWorkType.morning);
                        self.setWorkTypeSet(cwt.afternoon(), itemWorkType.afternoon);
                    });
                } else {
                    self.isCreated(true);
                    $('#input-workTypeCode').focus();
                    self.checkDisabled(true);
                }

            });

            self.langId.subscribe(() => {
                self.changeLanguage();
            });
        }


        startPage(): JQueryPromise<any> {
            let self = this,
                lwt = self.listWorkType,
                dfd = $.Deferred();

            $("#clear-button").focus();
            // switch language
            $("#switch-language")['ntsSwitchMasterLanguage']();
            $("#switch-language").on("selectionChanged", (event: any) => self.langId(event['detail']['languageId']));

            self.getSpecialHolidayFrame();
            self.getAbsenceFrame();

            self.getWorkType().done(function() {
                let lwtData = ko.toJS(lwt);
                if (lwtData.length > 0) {
                    self.currentCode(lwtData[0].workTypeCode);
                }
                else {
                    self.cleanForm();
                }
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Check language to save
         */
        private saveData(): void {
            let self = this,
                lang = ko.toJS(self.langId);

            if (lang == 'ja') {
                self.addWorkType();
            } else {
                self.insertWorkTypeLanguage();
            }
        }

        private setWorkTypeSet(worktypeset: WorkTypeSet, itemWorkType: IWorkTypeSet): void {

            worktypeset.workTypeCode(itemWorkType.workTypeCode);
            worktypeset.attendanceTime(itemWorkType.attendanceTime);
            worktypeset.closeAtr(itemWorkType.closeAtr);
            worktypeset.countHodiday(itemWorkType.countHodiday);
            worktypeset.dayNightTimeAsk(itemWorkType.dayNightTimeAsk);
            worktypeset.digestPublicHd(itemWorkType.digestPublicHd);
            worktypeset.genSubHodiday(itemWorkType.genSubHodiday);
            worktypeset.holidayAtr(itemWorkType.holidayAtr);
            worktypeset.sumAbsenseNo(itemWorkType.sumAbsenseNo);
            worktypeset.sumSpHodidayNo(itemWorkType.sumSpHodidayNo);
            worktypeset.timeLeaveWork(itemWorkType.timeLeaveWork);
            worktypeset.workAtr(itemWorkType.workAtr);
        }

        private openDiablogC() {
            var self = this,
                lwtData = ko.toJS(self.listWorkType);
            nts.uk.ui.windows.setShared("KMK007_WORK_TYPES", lwtData);

            nts.uk.ui.windows.sub.modal("/view/kmk/007/c/index.xhtml").onClosed(() => {
                self.getWorkType();
                $('#single-list_container').focus();
            });
        }

        private openBDialog(itemId: number) {
            var self = this;
            nts.uk.ui.windows.setShared("KMK007_ITEM_ID", itemId);
            nts.uk.ui.windows.sub.modal("/view/kmk/007/b/index.xhtml").onClosed(() => {
                self.getSpecialHolidayFrame();
                self.getAbsenceFrame();
                $('.combo-box-focus').focus();
            });
        }

        /**
         * Insert work type
         */
        private addWorkType(): any {
            var self = this,
                workType = self.currentWorkType(),
                length = workType.workTypeCode().length,
                worktypeCode = workType.workTypeCode();

            if (!!length && length < 3) {
                if (length == 1) {
                    workType.workTypeCode('00' + worktypeCode);
                } else {
                    workType.workTypeCode('0' + worktypeCode);
                }
            }

            if (workType.workAtr() == WorkAtr.ONE_DAY) {
                workType.morningCls(0);
                workType.afternoonCls(0);
            } else if (workType.workAtr() == WorkAtr.MORNING) {
                workType.oneDayCls(0);
            }

            workType.name(workType.dispName());
            workType.abbreviationName(workType.dispAbName());
            workType.oneDay().workTypeCode(workType.workTypeCode());

            workType.morning().workTypeCode(workType.workTypeCode());
            workType.afternoon().workTypeCode(workType.workTypeCode());

            let command: any = ko.toJS(workType);

            command.abolishAtr = Number(command.abolishAtr);
            self.changeBooleanToNumber(command.oneDay);
            self.changeBooleanToNumber(command.morning);
            self.changeBooleanToNumber(command.afternoon);

            $("#input-workTypeCode").trigger("validate");
            $("#input-workTypeName").trigger("validate");
            $("#abbreviation-name-input").trigger("validate");
            $("#symbolic-name-input").trigger("validate");
            $("#memo-input").trigger("validate");

            if (nts.uk.ui.errors.hasError()) {
                return;
            }
            if ((workType.oneDayCls() == 4 && workType.oneDay().sumSpHodidayNo() == "") ||
                (workType.morningCls() == 4 && workType.morning().sumSpHodidayNo() == "") ||
                (workType.afternoonCls() == 4 && workType.afternoon().sumSpHodidayNo() == "")) {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_921" });
                return;
            }
            if ((workType.oneDayCls() == 5 && workType.oneDay().sumAbsenseNo() == "")
                || (workType.morningCls() == 5 && workType.morning().sumAbsenseNo() == "") ||
                (workType.afternoonCls() == 5 && workType.afternoon().sumAbsenseNo() == "")) {
                nts.uk.ui.dialog.alertError({ messageId: "Msg_922" });
                return;
            }
            nts.uk.ui.block.invisible();
            service.addWorkType(self.isCreated(), command).done(function() {
                self.isCreated(false);
                self.getWorkType().done(function() {
                    self.currentCode(workType.workTypeCode());
                });

                nts.uk.ui.dialog.info(nts.uk.resource.getMessage('Msg_15'));
            }).fail(function(error) {
                nts.uk.ui.dialog.alertError(error.message);
            }).always(function() {
                nts.uk.ui.block.clear();
            });
        }

        /**
         * Change booean value to number
         */
        private changeBooleanToNumber(wts: IWorkTypeSet): void {
            wts.digestPublicHd = Number(wts.digestPublicHd);
            wts.attendanceTime = Number(wts.attendanceTime);
            wts.countHodiday = Number(wts.countHodiday);
            wts.dayNightTimeAsk = Number(wts.dayNightTimeAsk);
            wts.genSubHodiday = Number(wts.genSubHodiday);
            wts.timeLeaveWork = Number(wts.timeLeaveWork);
        }

        /**
         * Delete Work Type
         */
        private removeWorkType(): any {
            let self = this;
            nts.uk.ui.block.invisible();

            let workTypeCd = self.currentCode();

            nts.uk.ui.dialog.confirm({ messageId: "Msg_18" }).ifYes(() => {
                service.deleteWorkType(workTypeCd).done(function() {
                    nts.uk.ui.dialog.info(nts.uk.resource.getMessage('Msg_16'));
                    self.getWorkType().done(function() {
                        if (self.listWorkType().length == 0) {
                            self.cleanForm();
                        } else if (self.index() == self.listWorkType().length) {
                            self.currentCode(self.listWorkType()[self.index() - 1].workTypeCode);
                        } else {
                            self.currentCode(self.listWorkType()[self.index()].workTypeCode);
                        }
                    });
                }).fail(function(error) {
                    self.isCreated(false);
                    nts.uk.ui.dialog.alertError(error.message);
                }).always(function() {
                    nts.uk.ui.block.clear();
                });
            }).ifNo(() => {
                nts.uk.ui.block.clear();
            });
        }

        /**
         * Check Calculator Method based on work type cls
         */
        private checkCalculatorMethod(workTypeSetCode: number): void {
            let self = this;
            if (self.langId() != 'ja') {
                self.enableMethod(false);
            } else {
                if (workTypeSetCode == WorkTypeCls.Holiday || workTypeSetCode == WorkTypeCls.Pause || workTypeSetCode == WorkTypeCls.LeaveOfAbsence || workTypeSetCode == WorkTypeCls.Closure || workTypeSetCode == WorkTypeCls.ContinuousWork) {
                    self.currentWorkType().calculatorMethod(CalculatorMethod.DO_NOT_GO_TO_WORK);
                    self.enableMethod(false);
                } else if (workTypeSetCode == WorkTypeCls.Attendance || workTypeSetCode == WorkTypeCls.AnnualHoliday || workTypeSetCode == WorkTypeCls.YearlyReserved || workTypeSetCode == WorkTypeCls.Shooting) {
                    self.currentWorkType().calculatorMethod(CalculatorMethod.MAKE_ATTENDANCE_DAY);
                    self.enableMethod(false);
                } else if (workTypeSetCode == WorkTypeCls.SubstituteHoliday) {
                    self.currentWorkType().calculatorMethod(CalculatorMethod.EXCLUDE_FROM_WORK_DAY);
                    self.enableMethod(false);
                } else if (workTypeSetCode == WorkTypeCls.TimeDigestVacation) {
                    self.currentWorkType().calculatorMethod(CalculatorMethod.TIME_DIGEST_VACATION);
                    self.enableMethod(false);
                } else if (workTypeSetCode == WorkTypeCls.SpecialHoliday) {
                    self.currentWorkType().calculatorMethod(CalculatorMethod.MAKE_ATTENDANCE_DAY);
                    self.enableMethod(true);
                } else {
                    self.currentWorkType().calculatorMethod(CalculatorMethod.DO_NOT_GO_TO_WORK);
                    self.enableMethod(true);
                }
            }
        }

        /**
         * Reset display
         */
        private cleanForm(): void {
            var self = this,
                lwtData = ko.toJS(self.listWorkType),
                cwt = self.currentWorkType(),
                od = cwt.oneDay(),
                mn = cwt.morning(),
                af = cwt.afternoon();

            self.checkDisabled(true);

            cwt.workTypeCode('');
            cwt.dispName('');
            cwt.dispAbName('');
            cwt.symbolicName('');
            cwt.abolishAtr(0);
            cwt.memo('');
            cwt.workAtr(0);
            cwt.oneDayCls(0);
            cwt.morningCls(0);
            cwt.afternoonCls(0);
            cwt.calculatorMethod(0);

            od.workTypeCode('');
            od.workAtr(0);
            od.digestPublicHd(0);
            od.holidayAtr(0);
            od.countHodiday(0);
            od.closeAtr(0);
            od.sumAbsenseNo(0);
            od.sumSpHodidayNo(0);
            od.timeLeaveWork(0);
            od.attendanceTime(0);
            od.genSubHodiday(0);
            od.dayNightTimeAsk(0);

            mn.workTypeCode('');
            mn.workAtr(0);
            mn.digestPublicHd(0);
            mn.holidayAtr(0);
            mn.countHodiday(0);
            mn.closeAtr(0);
            mn.sumAbsenseNo(0);
            mn.sumSpHodidayNo(0);
            mn.timeLeaveWork(0);
            mn.attendanceTime(0);
            mn.genSubHodiday(0);
            mn.dayNightTimeAsk(0);

            af.workTypeCode('');
            af.workAtr(0);
            af.digestPublicHd(0);
            af.holidayAtr(0);
            af.countHodiday(0);
            af.closeAtr(0);
            af.sumAbsenseNo(0);
            af.sumSpHodidayNo(0);
            af.timeLeaveWork(0);
            af.attendanceTime(0);
            af.genSubHodiday(0);
            af.dayNightTimeAsk(0);
            self.currentCode("");
            if (lwtData.length > 0) {
                nts.uk.ui.errors.clearAll();
            }
        }

        /**
         * Get data from database 
         */
        private getWorkType(): any {
            let self = this,
                dfd = $.Deferred(),
                lwt = self.listWorkType;

            lwt.removeAll();

            service.loadWorkType().done(data => {
                if (data && !!data.length) {
                    lwt(_(data).orderBy(['dispOrder', 'workTypeCode'], ['asc', 'asc'])
                        .map(x => $.extend({
                            icon: !!x.abolishAtr ? '<i class="icon icon-dot"></i>' : ''
                        }, x)).value());
                }
                dfd.resolve();
            }).fail((res) => { dfd.reject(); });
            return dfd.promise();
        }

        /**
         * Convert data to WorkType view model
         */
        private convertToModel(item): WorkType {
            var self = this;
            var workType = new WorkType({
                workTypeCode: item.workTypeCode,
                name: item.name,
                abbreviationName: item.abbreviationName,
                symbolicName: item.symbolicName,
                abolishAtr: item.abolishAtr,
                memo: item.memo,
                workAtr: item.workAtr,
                oneDayCls: item.oneDayCls,
                morningCls: item.morningCls,
                afternoonCls: item.afternoonCls,
                calculatorMethod: item.calculatorMethod,
                oneDay: ko.toJS(self.oneDay),
                morning: ko.toJS(self.oneDay),
                afternoon: ko.toJS(self.oneDay),
                dispOrder: item.dispOrder
            });

            // one day
            if (item.workTypeSets && item.workTypeSets.length > 0) {
                _.forEach(item.workTypeSets, function(itemWorkTypeSet) {
                    var workTypeSet = new WorkTypeSet({
                        workTypeCode: item.workTypeCode,
                        workAtr: itemWorkTypeSet.workAtr,
                        digestPublicHd: itemWorkTypeSet.digestPublicHd,
                        holidayAtr: itemWorkTypeSet.holidayAtr,
                        countHodiday: itemWorkTypeSet.countHodiday,
                        closeAtr: itemWorkTypeSet.closeAtr,
                        sumAbsenseNo: itemWorkTypeSet.sumAbsenseNo,
                        sumSpHodidayNo: itemWorkTypeSet.sumSpHodidayNo,
                        timeLeaveWork: itemWorkTypeSet.timeLeaveWork,
                        attendanceTime: itemWorkTypeSet.attendanceTime,
                        genSubHodiday: itemWorkTypeSet.genSubHodiday,
                        dayNightTimeAsk: itemWorkTypeSet.dayNightTimeAsk
                    });

                    if (itemWorkTypeSet.workAtr == WorkAtr.ONE_DAY) {
                        workType.oneDay(workTypeSet);
                    } else if (itemWorkTypeSet.workAtr == WorkAtr.MORNING) {
                        workType.morning(workTypeSet);
                    } else if (itemWorkTypeSet.workAtr == WorkAtr.AFTERNOON) {
                        workType.afternoon(workTypeSet);
                    }
                });
            }

            return workType;
        }

        /**
         * Get data special holiday frame form database
         */
        private getSpecialHolidayFrame(): any {
            var self = this;
            var dfd = $.Deferred();
            service.getAllSpecialHolidayFrame().done(function(data) {
                if (data.length != 0) {
                    self.listSpecialHlFrame.removeAll();
                    _.forEach(data, function(item) {
                        if (item.deprecateSpecialHd == 0) {
                            var specialHlFrame = new ItemModel(item.specialHdFrameNo, item.specialHdFrameName, item.deprecateSpecialHd)
                            self.listSpecialHlFrame.push(ko.toJS(specialHlFrame));
                        }
                    });
                }
                dfd.resolve();
            }).fail((res) => { });
            return dfd.promise();
        }

        /**
         * Get data absence frame from database
         */
        private getAbsenceFrame(): any {
            var self = this;
            var dfd = $.Deferred();
            service.getAllAbsenceFrame().done(function(data) {
                if (data.length != 0) {
                    self.listAbsenceFrame.removeAll();
                    _.forEach(data, function(item) {
                        if (item.deprecateAbsence == 0) {
                            var absenceFrame = new ItemModel(item.absenceFrameNo, item.absenceFrameName, item.deprecateAbsence)
                            self.listAbsenceFrame.push(ko.toJS(absenceFrame));
                        }
                    });
                }
                dfd.resolve();
            }).fail((res) => { });
            return dfd.promise();
        }

        /**
         * When change language
         */
        private changeLanguage(): void {
            let self = this,
                grid = $("#single-list"),
                lang: string = ko.toJS(self.langId);

            if (lang == 'ja') {
                self.getWorkType();
                self.isEnable(true);

                $("#left-content").css('width', '320');
                $("#single-list").igGrid("option", "width", "280px");

                //remove columns otherLanguageName
                var cols = $("#single-list").igGrid("option", "columns");
                cols.splice(2, 1);
                $("#single-list").igGrid("option", "columns", cols);
                self.currentCode.valueHasMutated();
            } else {
                self.findWorkTypeLanguage();
            }
        }

        /**
         * find data WorkTypeLanguage
         */
        private findWorkTypeLanguage(): JQueryPromise<any> {
            let self = this,
                lwt = self.listWorkType,
                dfd = $.Deferred();

            service.findByLangId(self.langId()).done((data) => {
                _.each(data, (x) => {
                    let item = _.find(lwt(), ['workTypeCode', x.workTypeCode]);
                    if (item) {
                        item.nameNotJP = x.name;
                        item.abNameNotJP = x.abbreviationName;
                    }
                });

                self.enableMethod(false);
                self.isEnable(false);
                $("#single-list").igGrid("option", "width", "340px");
                $("#left-content").css('width', '380');

                var cols = $("#single-list").igGrid("option", "columns");
                if ($("#single-list").igGrid("option", "columns").length == 3) {
                    //add columns otherLanguageName   
                    var newColumn = { headerText: nts.uk.resource.getText('KMK007_9'), key: 'nameNotJP', width: 100, formatter: _.escape };
                    cols.splice(2, 0, newColumn);
                    $("#single-list").igGrid("option", "columns", cols);
                }

                self.currentCode.valueHasMutated();

                dfd.resolve();
            }).fail(() => {
                dfd.reject();
            });
            return dfd.promise();
        }

        /**
         * insert name and abName to WorkTypeLanguage
         */
        private insertWorkTypeLanguage(): void {
            let self = this,
                dfd = $.Deferred(),
                lang: string = ko.toJS(self.langId),
                cwt: any = ko.toJS(self.currentWorkType);

            let obj = {
                langId: lang,
                name: cwt.dispName,
                abName: cwt.dispAbName,
                workTypeCode: cwt.workTypeCode,
            }

            service.insert(obj).done(() => {
                nts.uk.ui.dialog.info(nts.uk.resource.getMessage('Msg_15'));
                self.getWorkType();
                self.findWorkTypeLanguage();
                dfd.resolve();
            }).fail(() => {
                dfd.reject();
            });
            dfd.promise();
        }

        /**
         * Print file excel
         */
        private exportExcel(): void {
            var self = this;
            nts.uk.ui.block.grayout();
            let langId = self.langId();
            service.saveAsExcel(langId).done(function() {
            }).fail(function(error) {
                nts.uk.ui.dialog.alertError(error.message);
            }).always(function() {
                nts.uk.ui.block.clear();
            });
        }
    }

    export enum WorkAtr {
        ONE_DAY = 0,
        MORNING = 1,
        AFTERNOON = 2
    }

    export enum CalculatorMethod {
        DO_NOT_GO_TO_WORK = 0,
        MAKE_ATTENDANCE_DAY = 1,
        EXCLUDE_FROM_WORK_DAY = 2,
        TIME_DIGEST_VACATION = 3
    }

    export enum WorkTypeCls {
        //出勤
        Attendance,
        // 休日
        Holiday,
        // 年休
        AnnualHoliday,
        // 積立年休
        YearlyReserved,
        //特別休暇
        SpecialHoliday,
        //欠勤
        Absence,
        //代休
        SubstituteHoliday,
        //振出
        Shooting,
        //振休
        Pause,
        //時間消化休暇
        TimeDigestVacation,
        //連続勤務
        ContinuousWork,
        //休日出勤
        HolidayWork,
        //休職
        LeaveOfAbsence,
        //休業
        Closure
    }

    export class ItemWorkTypeModel {
        workTypeCode: string;
        workTypeName: string;
        defaultMenu: number;
        icon: string;
        constructor(workTypeCode: string, workTypeName: string, defaultMenu: number) {
            this.workTypeCode = workTypeCode;
            this.workTypeName = workTypeName;
            this.defaultMenu = defaultMenu;
            if (defaultMenu == 0) {
                this.icon = "";
            } else {
                this.icon = '<i class="icon icon-dot"></i>';
            }
        }
    }

    class ItemModel {
        code: number;
        name: string;
        priority: number;
        constructor(code: number, name: string, priority: number) {
            this.code = code;
            this.name = name;
            this.priority = priority;
        }
    }

    export interface IWorkType {
        workTypeCode: string;
        nameNotJP?: string;
        name: string;
        abNameNotJP?: string;
        abbreviationName: string;
        symbolicName: string;
        abolishAtr: number;
        memo: string;
        workAtr: number;
        oneDayCls: number;
        morningCls: number;
        afternoonCls: number;
        calculatorMethod: number;
        oneDay?: IWorkTypeSet;
        morning?: IWorkTypeSet;
        afternoon?: IWorkTypeSet;
        dispOrder?: number;
        dispAbName?: string;
        dispName?: string;
    }

    export class WorkType {
        workTypeCode: KnockoutObservable<string>;
        nameNotJP: KnockoutObservable<string>;
        name: KnockoutObservable<string>;
        dispName: KnockoutObservable<string>;
        abNameNotJP: KnockoutObservable<string>;
        abbreviationName: KnockoutObservable<string>;
        dispAbName: KnockoutObservable<string>;
        symbolicName: KnockoutObservable<string>;
        abolishAtr: KnockoutObservable<any>;
        memo: KnockoutObservable<string>;
        workAtr: KnockoutObservable<number>;
        oneDayCls: KnockoutObservable<number>;
        morningCls: KnockoutObservable<number>;
        afternoonCls: KnockoutObservable<number>;
        calculatorMethod: KnockoutObservable<number>;
        icon: string;
        oneDay: KnockoutObservable<WorkTypeSet>;
        morning: KnockoutObservable<WorkTypeSet>;
        afternoon: KnockoutObservable<WorkTypeSet>;
        dispOrder: KnockoutObservable<number>;

        constructor(param: IWorkType) {
            this.workTypeCode = ko.observable(param.workTypeCode || '');
            this.nameNotJP = ko.observable(param.nameNotJP);
            this.name = ko.observable(param.name);
            this.dispName = ko.observable(param.dispName);
            this.abNameNotJP = ko.observable(param.abNameNotJP);
            this.abbreviationName = ko.observable(param.abbreviationName);
            this.dispAbName = ko.observable(param.dispAbName);
            this.symbolicName = ko.observable(param.symbolicName);
            this.abolishAtr = ko.observable(!!param.abolishAtr);
            this.memo = ko.observable(param.memo);
            this.workAtr = ko.observable(param.workAtr);
            this.oneDayCls = ko.observable(param.oneDayCls);
            this.morningCls = ko.observable(param.morningCls);
            this.afternoonCls = ko.observable(param.afternoonCls);
            this.calculatorMethod = ko.observable(param.calculatorMethod);
            this.oneDay = ko.observable(new WorkTypeSet(param.oneDay));
            this.morning = ko.observable(new WorkTypeSet(param.morning));
            this.afternoon = ko.observable(new WorkTypeSet(param.afternoon));
            this.dispOrder = ko.observable(param.dispOrder);

            if (param.abolishAtr == 0) {
                this.icon = "";
            } else {
                this.icon = '<i class="icon icon-dot"></i>';
            }
        }
    }

    export interface IWorkTypeSet {
        workTypeCode?: string;
        workAtr?: number;
        digestPublicHd?: number;
        holidayAtr?: number;
        countHodiday?: number;
        closeAtr?: number;
        sumAbsenseNo?: number;
        sumSpHodidayNo?: number;
        timeLeaveWork?: number;
        attendanceTime?: number;
        genSubHodiday?: number;
        dayNightTimeAsk?: number;
    }

    export class WorkTypeSet {
        workTypeCode: KnockoutObservable<string>;
        workAtr: KnockoutObservable<any>;
        digestPublicHd: KnockoutObservable<any>;
        holidayAtr: KnockoutObservable<any>;
        countHodiday: KnockoutObservable<any>;
        closeAtr: KnockoutObservable<any>;
        sumAbsenseNo: KnockoutObservable<any>;
        sumSpHodidayNo: KnockoutObservable<any>;
        timeLeaveWork: KnockoutObservable<any>;
        attendanceTime: KnockoutObservable<any>;
        genSubHodiday: KnockoutObservable<any>;
        dayNightTimeAsk: KnockoutObservable<any>;

        constructor(param: IWorkTypeSet) {
            if (param) {
                this.workTypeCode = ko.observable(param.workTypeCode || '');
                this.workAtr = ko.observable(param.workAtr);
                this.digestPublicHd = ko.observable(!!param.digestPublicHd);
                this.holidayAtr = ko.observable(param.holidayAtr);
                this.countHodiday = ko.observable(!!param.countHodiday);
                this.closeAtr = ko.observable(param.closeAtr);
                this.sumAbsenseNo = ko.observable(param.sumAbsenseNo);
                this.sumSpHodidayNo = ko.observable(param.sumSpHodidayNo);
                this.timeLeaveWork = ko.observable(!!param.timeLeaveWork);
                this.attendanceTime = ko.observable(!!param.attendanceTime);
                this.genSubHodiday = ko.observable(!!param.genSubHodiday);
                this.dayNightTimeAsk = ko.observable(!!param.dayNightTimeAsk);
            }
        }
    }
}