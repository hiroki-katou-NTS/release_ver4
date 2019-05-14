module nts.uk.at.view.ksc001.b {

    import NtsWizardStep = service.model.NtsWizardStep;
    import PeriodDto = service.model.PeriodDto;
    import UserInfoDto = service.model.UserInfoDto;
    import ScheduleExecutionLogSaveDto = service.model.ScheduleExecutionLogSaveDto;
    import ScheduleExecutionLogSaveRespone = service.model.ScheduleExecutionLogSaveRespone;
    import baseService = nts.uk.at.view.kdl023.base.service;
    import DailyPatternSetting = baseService.model.DailyPatternSetting;
    import ReflectionSetting = baseService.model.ReflectionSetting;
    import getText = nts.uk.resource.getText;
    // 休日反映方法
    import ReflectionMethod = baseService.model.ReflectionMethod;
    import DayOffSetting = baseService.model.DayOffSetting;

    export module viewmodel {
        export class ScreenModel {

            // step setup
            stepList: Array<NtsWizardStep>;
            stepSelected: KnockoutObservable<NtsWizardStep>;

            // setup ccg001
            ccgcomponent: GroupOption;

            // Options
            baseDate: KnockoutObservable<Date> = ko.observable(new Date());
            selectedEmployee: KnockoutObservableArray<EmployeeSearchDto> = ko.observableArray([]);

            selectRebuildAtr: KnockoutObservableArray<RadioBoxModel>;
            selectImplementAtr: KnockoutObservableArray<RadioBoxModel>;
            selectedImplementAtrCode: KnockoutObservable<number>;
            selectRebuildAtrCode: KnockoutObservable<number>;
            checkReCreateAtrAllCase: KnockoutObservable<number> = ko.observable(0);
            checkProcessExecutionAtrRebuild: KnockoutObservable<number> = ko.observable(0);
            checkCreateMethodAtrPersonalInfo: KnockoutObservable<number> = ko.observable(0);
            resetMasterInfo: KnockoutObservable<boolean> = ko.observable(false);
            confirm: KnockoutObservable<boolean> = ko.observable(false);

            recreateConverter: KnockoutObservable<boolean> = ko.observable(false);
            recreateEmployeeOffWork: KnockoutObservable<boolean> = ko.observable(false);
            recreateDirectBouncer: KnockoutObservable<boolean> = ko.observable(false);
            recreateShortTermEmployee: KnockoutObservable<boolean> = ko.observable(false);
            recreateWorkTypeChange: KnockoutObservable<boolean> = ko.observable(false);
            resetWorkingHours: KnockoutObservable<boolean> = ko.observable(false);
            resetStartEndTime: KnockoutObservable<boolean> = ko.observable(false);
            resetTimeAssignment: KnockoutObservable<boolean> = ko.observable(false);

            periodDate: KnockoutObservable<any> = ko.observable({});
            copyStartDate: KnockoutObservable<any> = ko.observable(new Date());
            startDateString: KnockoutObservable<string> = ko.observable("");
            endDateString: KnockoutObservable<string> = ko.observable("");

            lstLabelInfomation: KnockoutObservableArray<string> = ko.observableArray([]);
            infoCreateMethod: KnockoutObservable<string> = ko.observable('');
            infoPeriodDate: KnockoutObservable<string> = ko.observable('');
            lengthEmployeeSelected: KnockoutObservable<string> = ko.observable('');

            // Employee tab
            lstPersonComponentOption: any;
            selectedEmployeeCode: KnockoutObservableArray<string> = ko.observableArray([]);
            employeeName: KnockoutObservable<string>;
            employeeList: KnockoutObservableArray<UnitModel>;
            alreadySettingPersonal: KnockoutObservableArray<UnitAlreadySettingModel> = ko.observableArray([]);
            ccgcomponentPerson: GroupOption;
            personalScheduleInfo: KnockoutObservable<PersonalSchedule> = ko.observable(new PersonalSchedule());
            responeReflectionSetting: KnockoutObservable<ReflectionSetting> = ko.observable(null);
            responeDailyPatternSetting: KnockoutObservable<DailyPatternSetting> = ko.observable(null);

            //for control field
            isReCreate: KnockoutObservable<boolean>;
            isReSetting: KnockoutObservable<boolean>;
            isRebuildTargetOnly: KnockoutObservable<boolean>;
            isEnableRadioboxRebuildAtr: KnockoutObservable<boolean>;

            //list
            lstCreateMethod: KnockoutObservableArray<any>;
            lstReCreate: KnockoutObservableArray<any>;
            lstProcessExecution: KnockoutObservableArray<any>;
            periodStartDate: KnockoutObservable<moment.Moment> = ko.observable(moment());
            periodEndDate: KnockoutObservable<moment.Moment> = ko.observable(moment());

            constructor() {
                let self = this;
                let lstRadioBoxModelImplementAtr: RadioBoxModel[] = [];
                let lstRadioBoxModelRebuildAtr: RadioBoxModel[] = [];

                self.stepList = [
                    { content: '.step-1' },
                    { content: '.step-2' },
                    { content: '.step-3' },
                    { content: '.step-4' }
                ];
                self.reloadCcg001();

                self.stepSelected = ko.observable({ id: 'step-1', content: '.step-1' });

                lstRadioBoxModelImplementAtr.push(new RadioBoxModel(ImplementAtr.GENERALLY_CREATED, getText("KSC001_74")));
                lstRadioBoxModelImplementAtr.push(new RadioBoxModel(ImplementAtr.RECREATE, getText("KSC001_75")));
                lstRadioBoxModelRebuildAtr.push(new RadioBoxModel(ReBuildAtr.REBUILD_ALL, getText("KSC001_89")));
                lstRadioBoxModelRebuildAtr.push(new RadioBoxModel(ReBuildAtr.REBUILD_TARGET_ONLY, getText("KSC001_90")));

                self.selectImplementAtr = ko.observableArray(lstRadioBoxModelImplementAtr);
                self.selectedImplementAtrCode = ko.observable(ImplementAtr.GENERALLY_CREATED);
                self.selectRebuildAtr = ko.observableArray(lstRadioBoxModelRebuildAtr);
                self.selectRebuildAtrCode = ko.observable(ReBuildAtr.REBUILD_ALL);

                //for control field
                self.isReCreate = ko.computed(function() {
                    return self.selectedImplementAtrCode() == ImplementAtr.RECREATE;
                });

                // for is reseting
                self.isReSetting = ko.computed(function() {
                    return self.checkProcessExecutionAtrRebuild() == ProcessExecutionAtr.RECONFIG && self.isReCreate();
                });

                self.isEnableRadioboxRebuildAtr = ko.computed(() => {
                    return self.checkProcessExecutionAtrRebuild() == ProcessExecutionAtr.REBUILD && self.isReCreate();
                });

                self.isRebuildTargetOnly = ko.computed(() => {
                    return self.selectRebuildAtrCode() == ReBuildAtr.REBUILD_TARGET_ONLY
                        && self.isEnableRadioboxRebuildAtr();
                });

                self.periodDate.subscribe((newValue) => {
                    let newDate = ({});
                    if (newValue.startDate) {
                        self.copyStartDate(newValue.startDate);
                        newDate = {
                            startDate: moment.utc(newValue.startDate),
                            endDate: moment.utc(newValue.endDate)
                        }
                        self.periodStartDate(newDate.startDate);
                        self.periodEndDate(newDate.endDate);
                    }
                });

                self.lstCreateMethod = ko.observableArray(__viewContext.enums.CreateMethodAtr);
                self.lstReCreate = ko.observableArray(__viewContext.enums.ReCreateAtr);
                self.lstProcessExecution = ko.observableArray(__viewContext.enums.ProcessExecutionAtr);
            }
            /**
             * save to client service PersonalSchedule by employeeId
            */
            private savePersonalScheduleByEmployeeId(employeeId: string, data: PersonalSchedule): void {
                nts.uk.characteristics.save("PersonalSchedule_" + employeeId, data);
            }
            /**
             * save to client service PersonalSchedule
            */
            private savePersonalSchedule(data: PersonalSchedule): void {
                let self = this,
                    user: any = __viewContext.user;
                self.savePersonalScheduleByEmployeeId(user.employeeId, data);
            }

            /**
             * find by client service PersonalSchedule by employee
            */
            private findPersonalScheduleByEmployeeId(employeeId: string): JQueryPromise<PersonalSchedule> {
                return nts.uk.characteristics.restore("PersonalSchedule_" + employeeId);
            }

            /**
             * find by client service PersonalSchedule
            */
            private findPersonalSchedule(): JQueryPromise<PersonalSchedule> {
                let self = this,
                    user: any = __viewContext.user;
                return nts.uk.characteristics.restore("PersonalSchedule_" + user.employeeId);
            }
            /**
             * function next wizard by on click button 
             */
            private next(): JQueryPromise<void> {
                return $('#wizard').ntsWizard("next");
            }
            /**
             * function previous wizard by on click button 
             */
            private previous(): JQueryPromise<void> {
                return $('#wizard').ntsWizard("prev");
            }
            /**
             * function next two page wizard by on click button 
             */
            private nextTwo(): JQueryPromise<void> {
                let index = $('#wizard').ntsWizard("getCurrentStep");
                index = index + 2;
                return $('#wizard').ntsWizard("goto", index);
            }
            /**
            * function previous wizard by on click button 
            */
            private previousTwo(): JQueryPromise<void> {
                let index = $('#wizard').ntsWizard("getCurrentStep");
                index = index - 2;
                return $('#wizard').ntsWizard("goto", index);
            }
            /**
             * function convert string to Date
             */
            private toDate(strDate: string): Date {
                return moment(strDate, 'YYYY/MM/DD').toDate();
            }

            public reloadCcg001(): void {
                let self = this;
                if ($('.ccg-sample-has-error').ntsError('hasError')) {
                    return;
                }

                self.ccgcomponent = {
                    /** Common properties */
                    systemType: 2, // システム区分
                    showEmployeeSelection: false, // 検索タイプ
                    showQuickSearchTab: true, // クイック検索
                    showAdvancedSearchTab: true, // 詳細検索
                    showBaseDate: false, // 基準日利用
                    showClosure: false, // 就業締め日利用
                    showAllClosure: false, // 全締め表示
                    showPeriod: true, // 対象期間利用
                    periodFormatYM: false, // 対象期間精度

                    /** Required parameter */
                    baseDate: self.baseDate().toISOString(), // 基準日
                    periodStartDate: self.periodStartDate().toISOString(), // 対象期間開始日
                    periodEndDate: self.periodEndDate().toISOString(), // 対象期間終了日
                    inService: true, // 在職区分
                    leaveOfAbsence: true, // 休職区分
                    closed: true, // 休業区分
                    retirement: true, // 退職区分

                    /** Quick search tab options */
                    showAllReferableEmployee: true, // 参照可能な社員すべて
                    showOnlyMe: true, // 自分だけ
                    showSameWorkplace: true, // 同じ職場の社員
                    showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

                    /** Advanced search properties */
                    showEmployment: true, // 雇用条件
                    showWorkplace: true, // 職場条件
                    showClassification: true, // 分類条件
                    showJobTitle: true, // 職位条件
                    showWorktype: true, // 勤種条件
                    isMutipleCheck: true, // 選択モード

                    /** Return data */
                    returnDataFromCcg001: function(data: any) {
                        const mappedEmployeeList = _.map(data.listEmployee, employeeSearch => {return {code: employeeSearch.employeeCode,
                                                name: employeeSearch.employeeName,
                                                workplaceName: employeeSearch.workplaceName}});
                        self.employeeList(mappedEmployeeList);
                        self.selectedEmployee(data.listEmployee);
                        self.applyKCP005ContentSearch(data.listEmployee);
                    }
                }
            }
            /**
           * start page data 
           */
            public startPage(): JQueryPromise<any> {
                let self = this,
                    dfd = $.Deferred();
                // block ui
                nts.uk.ui.block.invisible();

                // find closure by id = 1
                service.findPeriodById(1).done(function(data) {
                    // update start date end date to ccg001
                    self.periodDate({
                        startDate: data.startDate,
                        endDate: data.endDate
                    });
                    self.reloadCcg001();
                    dfd.resolve(self);
                });
                return dfd.promise();
            }
            /**
            * apply ccg001 search data to kcp005
            */
            public applyKCP005ContentSearch(dataList: EmployeeSearchDto[]): void {
                let self = this,
                    employeeSearchs: UnitModel[] = [],
                    listSelectedEmpCode: any = [];
                self.employeeList([]);
                self.selectedEmployeeCode([]);
                _.each(dataList, (employeeSearch) =>{
                    employeeSearchs.push({
                        code: employeeSearch.employeeCode,
                        name: employeeSearch.employeeName,
                        workplaceName: employeeSearch.workplaceName
                    });
                    listSelectedEmpCode.push(employeeSearch.employeeCode);
                });

                // update employee list by ccg001 search 
                self.employeeList(employeeSearchs);
                self.selectedEmployeeCode(listSelectedEmpCode);

                // update kc005
                self.lstPersonComponentOption = {
                    isShowAlreadySet: false,
                    isMultiSelect: true,
                    listType: ListType.EMPLOYEE,
                    employeeInputList: self.employeeList,
                    selectType: SelectType.SELECT_BY_SELECTED_CODE,
                    selectedCode: self.selectedEmployeeCode,
                    isDialog: true,
                    isShowNoSelectRow: false,
                    alreadySettingList: self.alreadySettingPersonal,
                    isShowWorkPlaceName: true,
                    isShowSelectAllButton: false,
                    isSelectAllAfterReload: true,
                    maxWidth: 550,
                    maxRows: 10,
                    tabindex: 5
                };

            }
            /**
             * function next page by selection employee goto page (C)
             */
            private nextPageEmployee(): void {
                var self = this;
                if ($('.nts-input').ntsError('hasError')) {
                    return;
                }
                // check selection employee 
                if (self.selectedEmployeeCode && self.selectedEmployee() && self.selectedEmployeeCode().length > 0) {
                    var user: any = __viewContext.user;
                    self.findPersonalScheduleByEmployeeId(user.employeeId).done(function(data) {
                        self.updatePersonalScheduleData(data);

                        // focus by done
                        self.next().done(function() {
                            $('#inputSelectImplementAtr').focus();
                        });
                    }).fail(function(error) {
                        console.log(error);
                    });
                }
                else {
                    // show message by not choose employee of kcp005
                    nts.uk.ui.dialog.alertError({ messageId: 'Msg_206' });
                }
            }

            /**
             * update PersonalSchedule by find by employee id login
             */
            private updatePersonalScheduleData(data: PersonalSchedule): void {
                var self = this;
                if (data) {
                    self.personalScheduleInfo(data);
                }
            }
            /**
             * convert ui to PersonalSchedule
             */
            private toPersonalScheduleData(): PersonalSchedule {
                var self = this;
                var user: any = __viewContext.user;
                var data: PersonalSchedule = new PersonalSchedule();

                data.employeeId = user.employeeId;
                data.implementAtr = self.selectedImplementAtrCode();
                data.reCreateAtr = self.checkReCreateAtrAllCase();
                data.processExecutionAtr = self.checkProcessExecutionAtrRebuild();
                data.rebuildTargetAtr = self.selectRebuildAtrCode();
                data.recreateConverter = self.recreateConverter();
                data.recreateEmployeeOffWork = self.recreateEmployeeOffWork();
                data.recreateDirectBouncer = self.recreateDirectBouncer();
                data.recreateShortTermEmployee = self.recreateShortTermEmployee();
                data.recreateWorkTypeChange = self.recreateWorkTypeChange();
                data.resetWorkingHours = self.resetWorkingHours();
                data.resetStartEndTime = self.resetStartEndTime();
                data.resetMasterInfo = self.resetMasterInfo();
                data.resetTimeAssignment = self.resetTimeAssignment();
                data.confirm = self.confirm();
                data.createMethodAtr = self.checkCreateMethodAtrPersonalInfo();

                return data;
            }
            /**
             * function previous page by selection employee goto page (C)
             */
            private previousPageC(): void {
                var self = this;
                self.previous();
            }

            /**
             * function next page by selection employee goto next page
             */
            private nextPageC(): void {
                var self = this;
                if ((self.selectedImplementAtrCode() == ImplementAtr.RECREATE)
                    && self.checkProcessExecutionAtrRebuild() == ProcessExecutionAtr.RECONFIG) {
                    //build string for Screen E
                    self.buildString();
                    //goto screen E
                    self.nextTwo().done(function() {
                        $('#buttonFinishPageE').focus();
                    });
                }
                else {
                    self.next().done(function() {
                        $('#inputCreateMethodAtr').focus();
                    });
                }

            }

            /**
             * Validate copy paste schedule
             */
            private isInValidCopyPasteSchedule(): boolean {
                let self = this;
                if (self.checkCreateMethodAtrPersonalInfo() == CreateMethodAtr.COPY_PAST_SCHEDULE) {
                    $('#copy-start-date').ntsEditor('validate');
                }
                return $('.nts-input').ntsError('hasError');
            }
            /**
             * function previous page by selection employee goto page (D)
             */
            private previousPageD(): void {
                var self = this;
                if (self.isInValidCopyPasteSchedule()) {
                    return;
                }
                self.previous();
            }
            /**
             * function next page by selection employee goto page (E)
             */
            private nextPageD(): void {
                var self = this;
                if (self.isInValidCopyPasteSchedule()) {
                    return;
                }

                // check D1_4 is checked
                if (self.checkCreateMethodAtrPersonalInfo() == CreateMethodAtr.PATTERN_SCHEDULE) {

                    if (self.responeReflectionSetting()) {

                        // next page E by pattern code of self
                        self.findByPatternCodeAndOpenPageE(self.responeReflectionSetting().selectedPatternCd);
                    }
                    else {
                        self.findPersonalSchedule().done(function(res) {
                            if (res && res != null) {
                                // next page E by pattern code of res
                                self.findByPatternCodeAndOpenPageE(res.patternCode);
                            } else {
                                self.findAllPattern();
                            }
                        });
                    }
                } else {
                    self.openDialogPageE();
                }

            }

            /**
             * Find all pattern
             */
            private findAllPattern(): void {
                let self = this;
                _.defer(() => {
                    nts.uk.ui.block.invisible();
                    baseService.findAllPattern()
                        .done(allData => {
                            // next page E by pattern code of all data first
                            if (allData && allData.length > 0) {
                                self.responeDailyPatternSetting(allData[0]);
                                self.openDialogPageE();
                            } else {
                                // show message error 531
                                nts.uk.ui.dialog.alertError({ messageId: 'Msg_531' });
                            }
                        }).always(() => nts.uk.ui.block.clear());
                });
            }

            /**
             * find by pattern code and open dialog E
             */
            private findByPatternCodeAndOpenPageE(patternCode: string): void {
                var self = this;
                _.defer(() => {
                    nts.uk.ui.block.invisible();
                    baseService.findPatternByCode(patternCode).done(function(res) {
                        if (res && res != null) {
                            self.responeDailyPatternSetting(res);
                            self.openDialogPageE();
                        }
                        else {
                            nts.uk.ui.dialog.alertError({ messageId: 'Msg_531' });
                        }
                    }).always(() => nts.uk.ui.block.clear());
                });
            }

            /**
             * open dialog E
             */
            private openDialogPageE(): void {
                var self = this;
                self.buildString();
                self.next().done(function() {
                    $('#buttonFinishPageE').focus();
                });
            }
            /**
             * function previous page by selection employee goto page (E)
             */
            private previousPageE(): void {
                var self = this;

                if ((self.selectedImplementAtrCode() == ImplementAtr.RECREATE)
                    && self.checkProcessExecutionAtrRebuild() == ProcessExecutionAtr.RECONFIG) {
                    //back screen C
                    self.previousTwo();
                } else {
                    self.previous();
                }
            }
            /**
             * finish next page by selection employee goto page (F)
             */
            private finish(): void {
                var self = this;
                nts.uk.ui.block.invisible();
                service.checkThreeMonth(self.toDate(self.periodDate().startDate)).done(function(check) {
                    nts.uk.ui.block.clear();
                    if (check) {
                        // show message confirm 567
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_567' }).ifYes(function() {
                            self.createByCheckMaxMonth();
                        }).ifNo(function() {
                            return;
                        });
                    } else {
                        self.createByCheckMaxMonth();
                    }
                }).fail(function(error) {
                    console.log(error);
                });
            }

            /**
             * function build string to page E
             */
            private buildString() {
                var self = this;
                var lstLabelInfomation: string[] = [];

                //NO1
                if (self.selectedImplementAtrCode() == ImplementAtr.GENERALLY_CREATED) {
                    lstLabelInfomation.push(getText("KSC001_35"));
                } else {
                    lstLabelInfomation.push(getText("KSC001_36"));

                    //NO3
                    if (self.checkProcessExecutionAtrRebuild() == 0) {
                        lstLabelInfomation.push(getText("KSC001_37")
                            + getText("KSC001_7"));

                        if (self.selectRebuildAtrCode() == 0) {
                            lstLabelInfomation.push("　" + getText("KSC001_38")
                                + getText("KSC001_89"));
                        } else {
                            lstLabelInfomation.push("　" + getText("KSC001_38")
                                + getText("KSC001_90"));

                            if (self.recreateConverter()) {
                                lstLabelInfomation.push("　　" + getText("KSC001_38")
                                    + getText("KSC001_91"));
                            }

                            if (self.recreateEmployeeOffWork()) {
                                lstLabelInfomation.push("　　" + getText("KSC001_38")
                                    + getText("KSC001_92"));
                            }

                            if (self.recreateDirectBouncer()) {
                                lstLabelInfomation.push("　　" + getText("KSC001_38")
                                    + getText("KSC001_93"));
                            }

                            if (self.recreateShortTermEmployee()) {
                                lstLabelInfomation.push("　　" + getText("KSC001_38")
                                    + getText("KSC001_94"));
                            }

                            if (self.recreateWorkTypeChange()) {
                                lstLabelInfomation.push("　　" + getText("KSC001_38")
                                    + getText("KSC001_95"));
                            }
                        }
                    } else {
                        lstLabelInfomation.push(getText("KSC001_37")
                            + getText("KSC001_8"));

                        if (self.resetWorkingHours()) {
                            lstLabelInfomation.push("　" + getText("KSC001_38")
                                + getText("KSC001_96"));
                        }

                        if (self.resetMasterInfo()) {
                            lstLabelInfomation.push("　" + getText("KSC001_38")
                                + getText("KSC001_97"));
                        }

                        if (self.resetStartEndTime()) {
                            lstLabelInfomation.push("　" + getText("KSC001_38")
                                + getText("KSC001_98"));
                        }

                        if (self.resetTimeAssignment()) {
                            lstLabelInfomation.push("　" + getText("KSC001_38")
                                + getText("KSC001_99"));
                        }
                    }

                    //NO2
                    if (self.checkReCreateAtrAllCase() == ReCreateAtr.ALLCASE) {
                        lstLabelInfomation.push(getText("KSC001_37")
                            + getText("KSC001_4"));
                    }
                    if (self.checkReCreateAtrAllCase() == ReCreateAtr.ONLYUNCONFIRM) {
                        lstLabelInfomation.push(getText("KSC001_37")
                            + getText("KSC001_5"));
                    }

                }

                if (self.confirm()) {
                    lstLabelInfomation.push(getText("KSC001_17"));
                }
                self.lstLabelInfomation(lstLabelInfomation);

                //reset infoCreateMethod !important
                self.infoCreateMethod('');
                //check select recreate and select resetting
                if (!((self.selectedImplementAtrCode() == ImplementAtr.RECREATE)
                    && self.checkProcessExecutionAtrRebuild() == ProcessExecutionAtr.RECONFIG)) {

                    // set to view
                    if (self.checkCreateMethodAtrPersonalInfo() == CreateMethodAtr.PERSONAL_INFO) {
                        self.infoCreateMethod(getText("KSC001_22"));
                    }

                    // set to view
                    if (self.checkCreateMethodAtrPersonalInfo() == CreateMethodAtr.PATTERN_SCHEDULE) {
                        self.infoCreateMethod(getText("KSC001_23"));
                    }
                    // set to view
                    if (self.checkCreateMethodAtrPersonalInfo() == CreateMethodAtr.COPY_PAST_SCHEDULE) {
                        self.infoCreateMethod(getText("KSC001_39",
                            [moment(self.copyStartDate()).format('YYYY/MM/DD')]));
                    }
                }
                // set to view info
                self.infoPeriodDate(getText("KSC001_46",
                    [self.periodDate().startDate, self.periodDate().endDate]));
                self.lengthEmployeeSelected(getText("KSC001_47",
                    [self.selectedEmployeeCode().length]));
            }
            /**
             * function createPersonalSchedule to client by check month max
             */
            private createByCheckMaxMonth(): void {
                let self = this;
                _.defer(() => {
                    nts.uk.ui.block.invisible();
                    service.checkMonthMax(self.toDate(self.periodDate().startDate)).done(checkMax => {
                        nts.uk.ui.block.clear();
                        if (checkMax) {
                            nts.uk.ui.dialog.confirm({ messageId: 'Msg_568' }).ifYes(function() {
                                self.createPersonalSchedule();
                            }).ifNo(function() {
                                return;
                            });
                        } else {
                            self.createPersonalSchedule();
                        }
                    });
                });
            }
            /**
             * function createPersonalSchedule to client
             */
            private createPersonalSchedule(): void {
                let self = this;
                nts.uk.ui.dialog.confirm({ messageId: 'Msg_569' }).ifYes(function() {
                    // C1_5 is check
                    if (self.selectedImplementAtrCode() == ImplementAtr.RECREATE) {
                        nts.uk.ui.dialog.confirm({ messageId: 'Msg_570' }).ifYes(function() {
                            self.savePersonalScheduleData();
                        }).ifNo(function() {
                            return;
                        });
                    }
                    else {
                        self.savePersonalScheduleData();
                    }
                }).ifNo(function() {
                    return;
                });

            }

            /**
             * save PersonalSchedule data
             */
            private savePersonalScheduleData(): void {
                let self = this;
                self.savePersonalSchedule(self.toPersonalScheduleData());
                service.addScheduleExecutionLog(self.collectionData()).done(function(data) {
                    nts.uk.ui.block.clear();
                    nts.uk.ui.windows.setShared('inputData', data);
                    nts.uk.ui.windows.sub.modal("/view/ksc/001/f/index.xhtml").onClosed(function() {
                    });
                });

            }


            /**
             * open dialog KDL023
             */
            private showDialogKDL023(): void {
                let self = this,
                    data: PersonalSchedule = new PersonalSchedule();
                self.findPersonalSchedule().done(function(dataInfo) {
                    if (dataInfo && dataInfo != null) {
                        data = dataInfo;
                    }
                    nts.uk.ui.windows.setShared('reflectionSetting', self.convertPersonalScheduleToReflectionSetting(data));
                    nts.uk.ui.windows.sub.modal('/view/kdl/023/b/index.xhtml').onClosed(() => {
                        let dto = nts.uk.ui.windows.getShared('returnedData');
                        self.responeReflectionSetting(dto);
                    });
                });

            }
            /**
             * convert data personal schedule to refelctionSetting
             */
            private convertPersonalScheduleToReflectionSetting(data: PersonalSchedule): ReflectionSetting {
                let self = this,
                    dto: ReflectionSetting = {
                    calendarStartDate: self.periodDate().startDate,
                    calendarEndDate: self.periodDate().endDate,
                    selectedPatternCd: data.patternCode,
                    patternStartDate: self.periodDate().startDate,
                    reflectionMethod: data.holidayReflect,
                    statutorySetting: self.convertWorktypeSetting(data.statutoryHolidayUseAtr, data.statutoryHolidayWorkType),
                    holidaySetting: self.convertWorktypeSetting(data.holidayUseAtr, data.holidayWorkType),
                    nonStatutorySetting: self.convertWorktypeSetting(data.legalHolidayUseAtr, data.legalHolidayWorkType)
                };
                return dto;
            }

            /**
            * find employee id in selected
            */
            public findEmployeeIdByCode(employeeCode: string): string {
                let self = this,
                    employeeId = '';
                for (let employee of self.selectedEmployee()) {
                    if (employee.employeeCode === employeeCode) {
                        employeeId = employee.employeeId;
                    }
                }
                return employeeId;
            }
            /**
             * find employee id in selection employee code
             */
            public findEmployeeIdsByCode(employeeCodes: string[]): string[] {
                let self = this,
                    employeeIds: string[] = [];
                for (let employeeCode of employeeCodes) {
                    let employeeId = self.findEmployeeIdByCode(employeeCode);
                    if (employeeId && !(employeeId === '')) {
                        employeeIds.push(employeeId);
                    }
                }
                return employeeIds;
            }
            /**
             * collection data => command save
             */
            private collectionData(): ScheduleExecutionLogSaveDto {
                var self = this,
                    data: PersonalSchedule = self.toPersonalScheduleData(),
                    dto: ScheduleExecutionLogSaveDto = {
                    periodStartDate: self.toDate(self.periodDate().startDate),
                    periodEndDate: self.toDate(self.periodDate().endDate),
                    implementAtr: data.implementAtr,
                    reCreateAtr: data.reCreateAtr,
                    processExecutionAtr: data.processExecutionAtr,
                    reTargetAtr: data.rebuildTargetAtr,
                    resetWorkingHours: data.resetWorkingHours,
                    resetMasterInfo: data.resetMasterInfo,
                    reTimeAssignment: data.resetTimeAssignment,
                    reConverter: data.recreateConverter,
                    reStartEndTime: data.resetStartEndTime,
                    reEmpOffWork: data.recreateEmployeeOffWork,
                    reShortTermEmp: data.recreateShortTermEmployee,
                    reWorkTypeChange: data.recreateWorkTypeChange,
                    reDirectBouncer: data.recreateDirectBouncer,
                    // TODO
                    protectHandCorrect: null,
                    confirm: data.confirm,
                    createMethodAtr: data.createMethodAtr,
                    copyStartDate: self.toDate(self.copyStartDate()),
                    employeeIds: self.findEmployeeIdsByCode(self.selectedEmployeeCode())
                };
                return dto;
            }

            /**
             * convert work type setting
             */
            private convertWorktypeSetting(use: number, worktypeCode: string): DayOffSetting {
                let data: DayOffSetting = {
                    useClassification: use == UseAtr.USE,
                    workTypeCode: worktypeCode
                };
                return data;
            }

        }


        // 実施区分
        export enum ImplementAtr {
            // 通常作成
            GENERALLY_CREATED = 0,

            // 再作成
            RECREATE = 1
        }

        // 再作成区分
        export enum ReCreateAtr {
            // 全件
            ALLCASE = 0,

            // 未確定データのみ
            ONLYUNCONFIRM = 1
        }

        // 作成方法区分
        export enum CreateMethodAtr {
            // 個人情報
            PERSONAL_INFO = 0,

            // パターンスケジュール
            PATTERN_SCHEDULE = 1,

            // 過去スケジュールコピー
            COPY_PAST_SCHEDULE = 2
        }

        // 処理実行区分
        export enum ProcessExecutionAtr {
            // もう一度作り直す 
            REBUILD = 0,

            // 再設定する
            RECONFIG = 1
        }

        // 再作成対象区分
        export enum ReBuildAtr {
            // 全員
            REBUILD_ALL = 0,

            // 対象者のみ
            REBUILD_TARGET_ONLY = 1
        }

        // 利用区分
        export enum UseAtr {
            // 使用しない
            NOTUSE = 0,

            // 使用する
            USE = 1
        }

        // 個人スケジュールの作成
        export class PersonalSchedule {
            // 社員ID
            employeeId: string;

            //実施区分
            implementAtr: ImplementAtr;

            // 再作成区分
            reCreateAtr: ReCreateAtr;

            // 処理実行区分
            processExecutionAtr: ProcessExecutionAtr;

            // 再作成対象区分
            rebuildTargetAtr: ReBuildAtr;

            // 異動者を再作成
            recreateConverter: boolean;

            // 休職休業者を再作成
            recreateEmployeeOffWork: boolean;

            // 直行直帰者を再作成
            recreateDirectBouncer: boolean;

            // 短時間勤務者を再作成
            recreateShortTermEmployee: boolean;

            // 勤務種別変更者を再作成
            recreateWorkTypeChange: boolean;

            // 勤務開始・終了時刻を再設定
            resetWorkingHours: boolean;

            // 休憩開始・終了時刻を再設定
            resetStartEndTime: boolean;

            // マスタ情報を再設定
            resetMasterInfo: boolean;

            // 申し送り時間を再設定
            resetTimeAssignment: boolean;

            // 作成時に確定済みにする
            confirm: boolean;

            // 作成方法区分
            createMethodAtr: CreateMethodAtr

            // パターンコード
            patternCode: string;

            // 休日反映方法
            holidayReflect: ReflectionMethod;

            // パターン開始日
            patternStartDate: Date;

            // 法内休日利用区分
            legalHolidayUseAtr: UseAtr

            // 法内休日勤務種類
            legalHolidayWorkType: string;

            // 法外休日利用区分
            statutoryHolidayUseAtr: UseAtr;

            //法外休日勤務種類
            statutoryHolidayWorkType: string

            // 祝日利用区分
            holidayUseAtr: UseAtr;

            // 祝日勤務種類
            holidayWorkType: string;

            constructor() {
                let self = this;

                self.employeeId = '';
                self.implementAtr = ImplementAtr.GENERALLY_CREATED;
                self.reCreateAtr = ReCreateAtr.ALLCASE;
                self.processExecutionAtr = ProcessExecutionAtr.REBUILD;
                self.rebuildTargetAtr = ReBuildAtr.REBUILD_ALL;
                self.recreateConverter = false;
                self.recreateEmployeeOffWork = false;
                self.recreateDirectBouncer = false;
                self.recreateShortTermEmployee = false;
                self.recreateWorkTypeChange = false;
                self.resetWorkingHours = false;
                self.resetStartEndTime = false;
                self.resetMasterInfo = false;
                self.resetTimeAssignment = false;
                self.confirm = false;
                self.createMethodAtr = CreateMethodAtr.PERSONAL_INFO;
                self.patternCode = '02';
                self.holidayReflect = ReflectionMethod.Overwrite;
                self.patternStartDate = new Date();
                self.legalHolidayUseAtr = UseAtr.NOTUSE;
                self.legalHolidayWorkType = '';
                self.statutoryHolidayUseAtr = UseAtr.NOTUSE;
                self.statutoryHolidayWorkType = '';
                self.holidayUseAtr = UseAtr.NOTUSE;
                self.holidayWorkType = '';
            }
        }

        export class RadioBoxModel {
            id: number;
            name: string;
            constructor(id: number, name: string) {
                this.id = id;
                this.name = name;
            }
        }
        export class ListType {
            static EMPLOYMENT = 1;
            static Classification = 2;
            static JOB_TITLE = 3;
            static EMPLOYEE = 4;
        }

        export interface UnitModel {
            code: string;
            name?: string;
            workplaceName?: string;
            isAlreadySetting?: boolean;
        }

        export class SelectType {
            static SELECT_BY_SELECTED_CODE = 1;
            static SELECT_ALL = 2;
            static SELECT_FIRST_ITEM = 3;
            static NO_SELECT = 4;
        }

        export interface UnitAlreadySettingModel {
            code: string;
            isAlreadySetting: boolean;
        }

        export interface EmployeeSearchDto {
            employeeId: string;

            employeeCode: string;

            employeeName: string;

            workplaceCode: string;

            workplaceId: string;

            workplaceName: string;
        }

        export interface GroupOption {
            baseDate?: KnockoutObservable<Date>;
            // クイック検索タブ
            isQuickSearchTab: boolean;
            // 参照可能な社員すべて
            isAllReferableEmployee: boolean;
            //自分だけ
            isOnlyMe: boolean;
            //おなじ部門の社員
            isEmployeeOfWorkplace: boolean;
            //おなじ＋配下部門の社員
            isEmployeeWorkplaceFollow: boolean;
            // 詳細検索タブ
            isAdvancedSearchTab: boolean;
            //複数選択 
            isMutipleCheck: boolean;

            //社員指定タイプ or 全社員タイプ
            isSelectAllEmployee: boolean;

            onSearchAllClicked: (data: EmployeeSearchDto[]) => void;

            onSearchOnlyClicked: (data: EmployeeSearchDto) => void;

            onSearchOfWorkplaceClicked: (data: EmployeeSearchDto[]) => void;

            onSearchWorkplaceChildClicked: (data: EmployeeSearchDto[]) => void;

            onApplyEmployee: (data: EmployeeSearchDto[]) => void;
        }
    }
}