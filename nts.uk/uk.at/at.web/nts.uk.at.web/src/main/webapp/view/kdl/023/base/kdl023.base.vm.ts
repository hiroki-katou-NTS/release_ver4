module nts.uk.at.view.kdl023.base.viewmodel {

    import WorkDayDivision = service.model.WorkDayDivision;
    import WeeklyWorkSetting = service.model.WeeklyWorkSetting;
    import PublicHoliday = service.model.PublicHoliday;
    import ReflectionMethod = service.model.ReflectionMethod;
    import ReflectionSettingDto = service.model.ReflectionSetting;
    import WorkType = service.model.WorkType;
    import WorkTime = service.model.WorkTime;
    import DailyPatternSetting = service.model.DailyPatternSetting;
    import DailyPatternValue = service.model.DailyPatternValue;
    import formatDate = nts.uk.time.formatDate;
    import WorkCycleReflectionDto = nts.uk.at.view.kdl023.base.service.model.WorkCycleReflectionDto;
    import GetStartupInfoParamDto = nts.uk.at.view.kdl023.base.service.model.GetStartupInfoParam;
    import BootMode = nts.uk.at.view.kdl023.base.service.model.BootMode;
    import WorkCreateMethod = nts.uk.at.view.kdl023.base.service.model.WorkCreateMethod;
    import GetWorkCycleAppImageParamDto = nts.uk.at.view.kdl023.base.service.model.GetWorkCycleAppImageParam
    import RefImageEachDayDto = nts.uk.at.view.kdl023.base.service.model.RefImageEachDayDto;
    import MonthlyPatternRegisterCommand = nts.uk.at.view.kdl023.base.service.model.MonthlyPatternRegisterCommand;
    import WorkMonthlySetting = nts.uk.at.view.kdl023.base.service.model.WorkMonthlySetting;
    import WorkInformationToRegis = nts.uk.at.view.kdl023.base.service.model.WorkInformationToRegis;
    const CONST = {
        DATE_FORMAT: 'yyyy-MM-yy',
        YEAR_MONTH: 'yyyy/MM',
        MOMENT_DATE_FORMAT: 'YYYY-MM-DD'
    }

    export abstract class BaseScreenModel extends ko.ViewModel{
        dailyPatternList: KnockoutObservableArray<DailyPatternSetting>;
        listWorkType: KnockoutObservableArray<WorkType>;
        listWorkTime: KnockoutObservableArray<WorkTime>;
        patternStartDate: moment.Moment;
        calendarStartDate: moment.Moment = moment();
        calendarEndDate: moment.Moment;

        reflectionSetting: KnockoutObservable<ReflectionSetting> =   ko.observable(new ReflectionSetting());
        dailyPatternSetting: DailyPatternSetting;
        weeklyWorkSetting: WeeklyWorkSetting;
        listHoliday: Array<any>;
        isReflectionMethodEnable: KnockoutObservable<boolean> = ko.observable(true);
        isMasterDataUnregisterd: KnockoutObservable<boolean>;
        isOutOfCalendarRange: KnockoutObservable<boolean>;
        isDataEmpty: boolean;
        shared: ReflectionSettingDto;

        // Calendar component
        calendarData: KnockoutObservable<any>;
        yearMonthPicked: KnockoutObservable<number>;
        cssRangerYM: any;
        optionDates: KnockoutObservableArray<OptionDate>;
        firstDay: number;
        yearMonth: KnockoutObservable<number>;
        startDate: number;
        endDate: number;
        workplaceId: KnockoutObservable<string>;
        eventDisplay: KnockoutObservable<boolean>;
        eventUpdatable: KnockoutObservable<boolean>;
        holidayDisplay: KnockoutObservable<boolean>;
        cellButtonDisplay: KnockoutObservable<boolean>;
        workplaceName: KnockoutObservable<string>;
        dateValue: KnockoutObservable<any> = ko.observable({
            startDate: formatDate( new Date(), 'yyyy/MM'),
            endDate: formatDate( new Date(), 'yyyy/MM')
        });

        listPubHoliday: KnockoutObservableArray<WorkType> = ko.observableArray([]);
        listSatHoliday: KnockoutObservableArray<WorkType> = ko.observableArray([]);
        listNonSatHoliday: KnockoutObservableArray<WorkType> = ko.observableArray([]);

        reflectionMethod: KnockoutObservable<number> = ko.observable(0);
        workCycleEnable1: KnockoutObservable<boolean> = ko.observable(false);
        workCycleEnable2: KnockoutObservable<boolean> = ko.observable(false);
        workCycleEnable3: KnockoutObservable<boolean> = ko.observable(false);

        reflectionOrderList1: KnockoutObservableArray<any> = ko.observableArray([]);
        reflectionOrderList2: KnockoutObservableArray<any> = ko.observableArray([]);
        reflectionOrderList3: KnockoutObservableArray<any> = ko.observableArray([]);
        reflectionOrder1: KnockoutObservable<number> = ko.observable();
        reflectionOrder2: KnockoutObservable<number> = ko.observable();
        reflectionOrder3: KnockoutObservable<number> = ko.observable();

        isExecMode: KnockoutObservable<boolean> = ko.observable(false);
        loadWindowsParam: GetStartupInfoParamDto;
        isOverWrite: KnockoutObservable<boolean> = ko.observable(false);
        reflectionParam: KnockoutObservable<GetWorkCycleAppImageParamDto> = ko.observable();
        selectedPatternCd: KnockoutObservable<string> = ko.observable();

        pubHoliday: KnockoutObservable<string> = ko.observable();
        satHoliday: KnockoutObservable<string> = ko.observable();
        nonSatHoliday: KnockoutObservable<string> = ko.observable();
        refImageEachDayDto: KnockoutObservableArray<RefImageEachDayDto> = ko.observableArray([]);
        slideDays: KnockoutObservable<number> = ko.observable(0);
        workMonthlySetting: KnockoutObservableArray<WorkMonthlySetting> = ko.observableArray([]);

        promise: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            super();
            let vm = this;
            vm.listHoliday = [];
            vm.dailyPatternList = ko.observableArray<DailyPatternSetting>([]);
            vm.listWorkType = ko.observableArray<WorkType>([]);
            vm.listWorkTime = ko.observableArray<WorkTime>([]);
            vm.isMasterDataUnregisterd = ko.observable(false);
            vm.isOutOfCalendarRange = ko.observable(false);
            vm.isDataEmpty = false;
            vm.calendarStartDate = moment();
            // Calendar component
            vm.yearMonthPicked = ko.observable(parseInt(moment().format('YYYYMM'))); // default: current system date.
            vm.cssRangerYM = {
            };
            vm.optionDates = ko.observableArray<OptionDate>([]);
            vm.firstDay = 0; // default: sunday.
            vm.startDate = 1; // default: first date of month.
            vm.endDate = 31; // default: last date of month.
            vm.workplaceId = ko.observable("0");
            vm.workplaceName = ko.observable("");
            vm.eventDisplay = ko.observable(false);
            vm.eventUpdatable = ko.observable(false);
            vm.holidayDisplay = ko.observable(true);
            vm.cellButtonDisplay = ko.observable(false);
            vm.reflectionOrderList1([
				{code: WorkCreateMethod.WORK_CYCLE, name: vm.$i18n('KDL023_3')},
				{code: WorkCreateMethod.WEEKLY_WORK, name: vm.$i18n('KDL023_40')},
				{code: WorkCreateMethod.PUB_HOLIDAY, name: vm.$i18n('KDL023_8')}
            ]);

            vm.reflectionOrderList2([
                {code: WorkCreateMethod.NON, name: vm.$i18n('KDL023_39')},
            ]);
            vm.reflectionOrderList3([
                {code: WorkCreateMethod.NON, name: vm.$i18n('KDL023_39')},
            ]);
        }

        /**
         * Start page event.
         */
        public startPage(): JQueryPromise<any> {
            let vm = this;
            vm.$blockui('invisible')
            let dfd = $.Deferred();

            // Load data.
            $.when(vm.getParamFromCaller(), // Get param from parent screen.
                vm.loadWindows(this.loadWindowsParam), // Load windows.
                //vm.loadWorktimeList(),  Load worktime list.
                vm.loadDailyPatternHeader(), // Load daily pattern header.
                vm.loadWeeklyWorkSetting()) // Load weekly work setting.
                .done(() => {

                    // Check if dailyPatternList has data.
                    if (nts.uk.util.isNullOrEmpty(vm.dailyPatternList())) {
                        dfd.resolve();
                        return;
                    }

                    // validate selected code
                    // if selected code is not valid, select first item.
                    vm.reflectionSetting().fromDto(vm.shared);
                    _.find(vm.dailyPatternList(),
                        pattern => vm.reflectionSetting().selectedPatternCd() == pattern.patternCode) != undefined ?
                        vm.reflectionSetting().selectedPatternCd() :
                        vm.reflectionSetting().selectedPatternCd(vm.dailyPatternList()[0].patternCode);

                    // Load daily pattern detail.
                    vm.loadDailyPatternDetail(
                        vm.reflectionSetting().selectedPatternCd()).done(() => {
                        // Xu ly hien thi calendar.
                        //vm.optionDates(vm.getOptionDates());
                        dfd.resolve();
                    });

                    // Init subscribe.
                    vm.reflectionSetting().selectedPatternCd.subscribe(code => {
                        vm.loadDailyPatternDetail(code);
                    });

                    // Set tabindex.
                    vm.isReflectionMethodEnable.subscribe(val => {
                        if (val) {
                            $('#reflection-method-radio-group').attr('tabindex', '8');
                        } else {
                            $('#reflection-method-radio-group').attr('tabindex', '-1');
                        }
                    });

                    vm.reflectionMethod.subscribe(val => {
						if(val === 2){
							vm.reflectionSetting().statutorySetting.useClassification(false);
							vm.reflectionSetting().nonStatutorySetting.useClassification(false);
							vm.reflectionSetting().holidaySetting.useClassification(false);
							vm.workCycleEnable1(true);
							$('#kdl023-holiday-warning-label').hide();
						} else {
							vm.workCycleEnable1(false);
							vm.workCycleEnable2(false);
							vm.workCycleEnable3(false);
							$('#kdl023-holiday-warning-label').show();
							vm.reflectionSetting().statutorySetting.useClassification(true);
							vm.reflectionSetting().nonStatutorySetting.useClassification(true);
							vm.reflectionSetting().holidaySetting.useClassification(true);
						}
					});

                    vm.reflectionOrder1.subscribe(val =>{
						let array2 = ([
							{code: WorkCreateMethod.NON, name: vm.$i18n('KDL023_39')},
							{code: WorkCreateMethod.WORK_CYCLE, name: vm.$i18n('KDL023_3')},
							{code: WorkCreateMethod.WEEKLY_WORK, name: vm.$i18n('KDL023_40')},
							{code: WorkCreateMethod.PUB_HOLIDAY, name: vm.$i18n('KDL023_8')}
						]);

						let array3 = ([
							{code: WorkCreateMethod.NON, name: vm.$i18n('KDL023_39')},
							{code: WorkCreateMethod.WORK_CYCLE, name: vm.$i18n('KDL023_3')},
							{code: WorkCreateMethod.WEEKLY_WORK, name: vm.$i18n('KDL023_40')},
							{code: WorkCreateMethod.PUB_HOLIDAY, name: vm.$i18n('KDL023_8')}
						]);

						let remove2 =  array2.indexOf(array2.filter(e => e.code === val)[0]);
						if(remove2 > -1){
							array2.splice(remove2, 1);
						}
						let remove3 = array3.indexOf(array3.filter(e => e.code === val)[0]);
						if(remove3 > -1){
							array3.splice(remove3, 1);
						}
						let remove4 = array3.indexOf(array3.filter(e => e.code === vm.reflectionOrder2())[0]);
						if(remove4 > -1 && vm.reflectionOrder2() > -1){
							array3.splice(remove4, 1);
						}
						vm.reflectionOrderList2(array2);
						vm.reflectionOrderList3(array3);

						if(vm.reflectionOrder2() === val){
							vm.reflectionOrder2(WorkCreateMethod.NON);
						}
						if(vm.reflectionOrder3() === val){
							vm.reflectionOrder2(WorkCreateMethod.NON);
						}
						vm.workCycleEnable2(true);
                        let arrCheck = ([val,vm.reflectionOrder2(), vm.reflectionOrder3()]);
						vm.checkReflectionOrder(arrCheck)
                    });
					
					vm.workCycleEnable1.subscribe(isEnable => {
						if (!isEnable) {
							return;
						}

						vm.reflectionOrder1.valueHasMutated();
					})

                    vm.reflectionOrder2.subscribe( val => {
                        if(val === WorkCreateMethod.NON){
                            vm.reflectionOrder3(WorkCreateMethod.NON);
                            vm.workCycleEnable3(false);
                        }else{
                            let array3 = ([
                                {code: WorkCreateMethod.NON, name: vm.$i18n('KDL023_39')},
                                {code: WorkCreateMethod.WORK_CYCLE, name: vm.$i18n('KDL023_3')},
                                {code: WorkCreateMethod.WEEKLY_WORK, name: vm.$i18n('KDL023_40')},
                                {code: WorkCreateMethod.PUB_HOLIDAY, name: vm.$i18n('KDL023_8')}
                            ]);
                            let remove3 = array3.indexOf(array3.filter(e => e.code === vm.reflectionOrder1())[0]);
                            if(remove3 > -1){
                                array3.splice(remove3, 1);
                            }
                            let remove4 = array3.indexOf(array3.filter(e => e.code === val)[0]);
                            if(remove4 > -1){
                                array3.splice(remove4, 1);
                            }
                            vm.reflectionOrderList3(array3);

                            if(vm.reflectionOrder3() === val){
                                vm.reflectionOrder3(WorkCreateMethod.NON);
                            }
                            vm.workCycleEnable3(true);
                        }

                        let arrCheck = ([vm.reflectionOrder1(), val, vm.reflectionOrder3()]);
                        vm.checkReflectionOrder(arrCheck)
                    });
					
					vm.workCycleEnable2.subscribe(isEnable => {
						if (!isEnable) {
							return;
						}

						vm.reflectionOrder2.valueHasMutated();
					})

                    vm.reflectionOrder3.subscribe(val => {
						let arrCheck = ([vm.reflectionOrder1(), vm.reflectionOrder2(), val]);
						vm.checkReflectionOrder(arrCheck);
					})
					
					vm.workCycleEnable3.subscribe(isEnable => {
						if (!isEnable) {
							return;
						}

						vm.reflectionOrder3.valueHasMutated();
					})

                    // Force change to set tab index.
                    vm.reflectionSetting().holidaySetting.useClassification.valueHasMutated();

                    vm.setCalendarData(vm.refImageEachDayDto());

                    vm.reflectionSetting().selectedPatternCd.subscribe(() => {
                        vm.promise(true);
                    });

                    if(vm.isExecMode()){
                        $('.ntsStartDatePicker').focus();
                    }else{
                        $('#cbb-daily-work-pattern').focus();
                    }
                }).fail(res => {
                    vm.$dialog.alert(res.message)
                    dfd.fail();
                }).always(() => {
                    // Show message Msg_37 then close dialog.
                    if (vm.isDataEmpty) {
                        vm.showErrorThenCloseDialog();
						dfd.fail();
                    }
                    vm.$blockui('clear')
                });
            return dfd.promise();
        }

		private checkReflectionOrder(arrCheck : number[]) {
        	const vm = this;

			let weeklyEnable = arrCheck.filter(e => e === WorkCreateMethod.WEEKLY_WORK).length;
			let holidayEnable = arrCheck.filter(e => e === WorkCreateMethod.PUB_HOLIDAY).length;
			if (weeklyEnable > 0) {
				vm.reflectionSetting().statutorySetting.useClassification(true);
				vm.reflectionSetting().nonStatutorySetting.useClassification(true);
			} else {
				vm.reflectionSetting().statutorySetting.useClassification(false);
				vm.reflectionSetting().nonStatutorySetting.useClassification(false);
			}
			if (holidayEnable > 0) {
				vm.reflectionSetting().holidaySetting.useClassification(true);
			} else {
				vm.reflectionSetting().holidaySetting.useClassification(false);
			}
		}

        /**
         * Close dialog
         */
        public closeDialog(): void {
            nts.uk.ui.windows.close();
        }

        /**
         * Forward button clicked
         */
        public forward(): void {
            let self = this;
            let slideDays = self.slideDays() + 1;
            // Do nothing if option dates is empty.
            if (self.isOptionDatesEmpty()) {
                return;
            }
            self.onBtnApplySetting(slideDays);
            self.slideDays(slideDays)
            // // Subtract 1 day from pattern start date.
            // self.patternStartDate.subtract(1, 'days');
            //
            // // Reload calendar
            // self.optionDates(self.getOptionDates());
        }

        /**
         * Backward button clicked.
         */
        public backward(): void {
            let self = this;
            let slideDays = self.slideDays() - 1;
            // Do nothing if option dates is empty. 
            if (self.isOptionDatesEmpty()) {
                return;
            }
            self.onBtnApplySetting(slideDays);
            self.slideDays(slideDays)
            // // Subtract 1 day from pattern start date.
            // self.patternStartDate.subtract(1, 'days');
            //
            // // Reload calendar
            // self.optionDates(self.getOptionDates());
        }

        /**
         * Event when click apply button.
         */
        public onBtnApplySettingClicked(): void{
            this.slideDays(0);
            this.onBtnApplySetting(this.slideDays());
        }

        public onBtnApplySetting(slideDay: number): void {
            let vm = this;
            vm.$blockui('invisible')

			let holidayListsErrors = [];
			if (vm.listSatHoliday.length == 0){
				holidayListsErrors.push({
					message: nts.uk.resource.getMessage('MsgB_2', [vm.$i18n('KDL023_6')]),
					messageId: "MsgB_2",
					supplements: {}
				});
			}

			if (vm.listNonSatHoliday.length == 0){
				holidayListsErrors.push({
					message: nts.uk.resource.getMessage('MsgB_2', [vm.$i18n('KDL023_7')]),
					messageId: "MsgB_2",
					supplements: {}
				});
			};

			if (vm.listPubHoliday.length == 0) {
				holidayListsErrors.push({
					message: nts.uk.resource.getMessage('MsgB_2', [vm.$i18n('KDL023_8')]),
					messageId: "MsgB_2",
					supplements: {}
				});
			}

			if(holidayListsErrors.length > 0){
				nts.uk.ui.dialog.bundledErrors({ errors: holidayListsErrors });
				vm.$blockui('clear');

				return;
			}

            let dateString = vm.yearMonthPicked().toString() + vm.startDate.toString();
            let year:number = +dateString.substring(0,4);
            let month = dateString.substring(4,6);
            let day = dateString.substring(6,8);
            let endDay = moment(new Date(year, +(month)-1, +(day))).endOf('month').toDate().getDate();
            let startDate = year+'-'+month+'-'+day;
            let endDate = year+'-'+month+'-'+endDay;
            let legalHolidayCd = '';
            let nonStatutoryHolidayCd = '';
            let holidayCd = '';
            let refOrder:Array<number> = [];
            let defaultStartDate = startDate;
            let defaultEndDate = endDate;

            if(vm.isExecMode()){
                defaultStartDate = vm.reflectionSetting().calendarStartDate();
                defaultEndDate = vm.reflectionSetting().calendarEndDate();
            }

            if(vm.reflectionSetting().statutorySetting.useClassification()){
                legalHolidayCd = vm.reflectionSetting().statutorySetting.workTypeCode();
            }
            if(vm.reflectionSetting().nonStatutorySetting.useClassification()){
                nonStatutoryHolidayCd = vm.reflectionSetting().nonStatutorySetting.workTypeCode();
            }
            if(vm.reflectionSetting().holidaySetting.useClassification()){
                holidayCd = vm.reflectionSetting().holidaySetting.workTypeCode();
            }

            if(vm.reflectionMethod() === 2){
                if(vm.reflectionOrder1() != WorkCreateMethod.NON){
                    refOrder.push(vm.reflectionOrder1());
                    if(vm.reflectionOrder2() != WorkCreateMethod.NON){
                        refOrder.push(vm.reflectionOrder2());
                        if(vm.reflectionOrder3() != WorkCreateMethod.NON){
                            refOrder.push(vm.reflectionOrder3());
                        }
                    }
                }
            } else if(vm.reflectionMethod() === 0){
                refOrder = [WorkCreateMethod.WORK_CYCLE, WorkCreateMethod.PUB_HOLIDAY, WorkCreateMethod.WEEKLY_WORK];
            } else{
                refOrder = [WorkCreateMethod.PUB_HOLIDAY, WorkCreateMethod.WORK_CYCLE, WorkCreateMethod.WEEKLY_WORK];
            }
            if(refOrder.length === 0){
                vm.$dialog.error({ messageId: "MsgB_2", messageParams: [vm.$i18n('KDL023_31')]});
                vm.$blockui('clear');
                $('#cbb-reflection-order-1').focus();
                return;
            }
            else{
                vm.reflectionParam({
                    creationPeriodStartDate : defaultStartDate,
                    creationPeriodEndDate : defaultEndDate,
                    workCycleCode : vm.reflectionSetting().selectedPatternCd(),
                    refOrder : refOrder,
                    numOfSlideDays : slideDay,
                    legalHolidayCd: legalHolidayCd,
                    nonStatutoryHolidayCd: nonStatutoryHolidayCd,
                    holidayCd: holidayCd
                })

                service.getReflectionWorkCycleAppImage(vm.reflectionParam()).done( (val) =>{
                    vm.refImageEachDayDto(val);
                    vm.setCalendarData(val);
                    vm.promise(false);
                }).fail( () => {

                    }
                ).always(()=>{
                        vm.$blockui('clear');
                    }
                );
                vm.setPatternRange() // Set pattern's range
                    .done(() => {
                        //vm.optionDates(vm.getOptionDates()); // Reload calendar
                        $('#component-calendar-kcp006').focus(); // Set focus control
                    }).always(() => {

                });
            }
        }

        /**
         * Get default PatternReflection
         */
        private getDefaultPatternReflection(): service.model.ReflectionSetting {
            let self = this;
            return {
                selectedPatternCd: '',
                patternStartDate: self.calendarStartDate.format(CONST.DATE_FORMAT),
                reflectionMethod: 0, // Overwrite
                statutorySetting: {
                    useClassification: true,
                    workTypeCode: ''
                },
                nonStatutorySetting: {
                    useClassification: true,
                    workTypeCode: ''
                },
                holidaySetting: {
                    useClassification: true,
                    workTypeCode: ''
                }
            }
        };

        /**
         * Load daily pattern.
         */
        private loadDailyPatternHeader(): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();
            service.findAllPattern().done(function(list: Array<DailyPatternSetting>) {
                if (list && list.length > 0) {
                    self.dailyPatternList(list);
                } else {
                    self.isDataEmpty = true;
                }
                dfd.resolve();
            }).fail(() => {
                self.showErrorThenCloseDialog();
                dfd.fail();
            });
            return dfd.promise();
        }

        /**
         * Load daily pattern detail.
         */
        private loadDailyPatternDetail(code: string): JQueryPromise<void> {
            nts.uk.ui.block.invisible();
            let self = this;
            let dfd = $.Deferred<void>();
            service.findPatternByCode(code).done(res => {
                self.dailyPatternSetting = res;
                dfd.resolve();
            }).always(() => {
                nts.uk.ui.block.clear();
            });
            return dfd.promise();
        }

        /**
         * Load weekly work setting.
         */
        private loadWeeklyWorkSetting(): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();
            service.findWeeklyWorkSetting().done(function(weeklyWorkSetting: WeeklyWorkSetting) {
                self.weeklyWorkSetting = weeklyWorkSetting;
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Load holiday list.
         */
        private loadHolidayList(): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();
            service.getHolidayByListDate(self.getListDateOnCalendar()).done(function(list: Array<PublicHoliday>) {
                self.listHoliday = list;
                dfd.resolve();
            });
            return dfd.promise();
        }


        /**
         * Load worktype list.
         */
        private loadWindows(param?: GetStartupInfoParamDto): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();
            service.startUpWindows(param).done(function(list: WorkCycleReflectionDto) {
                if(list){
                    self.listPubHoliday(list.pubHoliday);
                    self.listSatHoliday(list.satHoliday);
                    self.listNonSatHoliday(list.nonSatHoliday);
                    self.isExecMode(param.bootMode === 1);
                    self.refImageEachDayDto(list.reflectionImage);
                    if(param.bootMode === 1){
                        $('#daterangepicker').focus();
                    }
                }else {
                    self.isDataEmpty = true;
                }
                dfd.resolve();
            }).fail(() => {
                self.showErrorThenCloseDialog();
                dfd.fail();
            });
            return dfd.promise();
        }

        /**
         * Load work time list.
         */
        private loadWorktimeList(): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();
            service.getAllWorkTime().done(function(list: Array<WorkTime>) {
                self.listWorkTime(list);
                dfd.resolve();
            });
            return dfd.promise();
        }

        /**
         * Get option dates for calendar.
         * @param: startDate (YYYYMM)
         */
        private getOptionDates(): Array<OptionDate> {
            let self = this;
            // Update pattern start date value of reflection setting.
            self.reflectionSetting().patternStartDate(self.patternStartDate.format(CONST.DATE_FORMAT));

            // Get calendar's range.
            let range = self.calendarEndDate.diff(self.calendarStartDate, 'days') + 1;

            // Reset flag.
            self.isMasterDataUnregisterd(false);

            let currentDate = moment(self.patternStartDate);
            let result: Array<OptionDate> = [];

            // Backward processing
            if (currentDate.isAfter(self.calendarStartDate, 'day')) {
                // Previous day on calendar.
                currentDate = currentDate.subtract(1, 'days');

                while (currentDate.isSameOrAfter(self.calendarStartDate, 'day')) { // Loop until reach calendar start date.
                    // Reversed list dailyPatternValue loop.
                    let listDailyPatternVal;
                    if (self.dailyPatternSetting.dailyPatternVals) {
                        listDailyPatternVal = self.dailyPatternSetting.dailyPatternVals.slice().reverse();
                    }

                    // Master data is registered.
                    if (listDailyPatternVal && listDailyPatternVal.length > 0) {
                        listDailyPatternVal.some(dailyPatternValue => {
                            result = result.concat(self.loopBackwardPatternDays(dailyPatternValue, currentDate));

                            // Break loop condition.
                            let isLoopEnd = currentDate.isBefore(self.calendarStartDate, 'day');
                            return isLoopEnd;
                        });
                    }

                    // Master data is unregistered.
                    else {
                        let dailyPatternValue = {
                            dispOrder: undefined,
                            workTypeSetCd: undefined,
                            workingHoursCd: undefined,
                            days: range
                        }
                        result = result.concat(self.loopBackwardPatternDays(dailyPatternValue, currentDate));
                    }
                }
                // Reset current date to pattern start date.
                currentDate = moment(self.patternStartDate);
            }

            // Forward processing
            while (currentDate.isSameOrBefore(self.calendarEndDate, 'day')) { // Loop until reach calendar end date.
                // List dailyPatternValue loop.
                let listDailyPatternVal = self.dailyPatternSetting.dailyPatternVals;

                // Set is out of calendar's range observable.
                self.setIsOutOfCalendarRange(listDailyPatternVal);

                // Master data is registered.
                if (listDailyPatternVal && listDailyPatternVal.length > 0) {
                    listDailyPatternVal.some(dailyPatternValue => {
                        result = result.concat(self.loopForwardPatternDays(dailyPatternValue, currentDate));

                        // Break loop condition.
                        let isLoopEnd = false;

                        // Break loop if current date reach calendar's end date.
                        isLoopEnd = currentDate.isAfter(self.calendarEndDate, 'day');

                        // If pattern's total days is out of calendar's range.
                        if (!self.isExecMode() && self.isOutOfCalendarRange()) {
                            isLoopEnd = false; // continue to loop.
                        }

                        return isLoopEnd;
                    });
                }

                // Master data is unregistered.
                else {
                    let dailyPatternValue = {
                        dispOrder: undefined,
                        workTypeSetCd: undefined,
                        workingHoursCd: undefined,
                        days: range
                    }
                    result = result.concat(self.loopForwardPatternDays(dailyPatternValue, currentDate));
                }
            }
            return result;
        }

        /**
         * Loop backward daily pattern days.
         */
        private loopBackwardPatternDays(dailyPatternValue: DailyPatternValue, currentDate: moment.Moment): Array<OptionDate> {
            let self = this;
            let result = [];
            let dayOfPattern = 1;

            // Day of pattern loop.
            while (dayOfPattern <= dailyPatternValue.days) {

                // Break loop if current date reach calendar's start date.
                if (currentDate.isBefore(self.calendarStartDate, 'day')) {
                    break;
                }

                // Get display text and push to option date list.
                let optionDate = self.getDisplayText(dailyPatternValue, currentDate);
                result.push(optionDate);

                // Next day of pattern.
                dayOfPattern++;

                // Reserve dayOfPattern if reflection method = fill in the blank
                if (optionDate.textColor == '#ff0000' // Is holiday
                    && self.isFillInTheBlankChecked()) {
                    dayOfPattern--;
                }

                //  When is on screen B: only process in calendar's range.
                if (self.isExecMode()) {
                    // Skip to previous day if current date is after calendar's end date.
                    if (currentDate.isAfter(self.calendarEndDate, 'day')) {
                        _.remove(result, item => item === optionDate);
                        // reset flag because today does not count (out of calendar's range)
                        self.isMasterDataUnregisterd(false);
                    }
                }

                // Previous day on calendar.
                currentDate = currentDate.subtract(1, 'days');

            }
            return result;
        }

        /**
         * Get total days of pattern.
         */
        private getTotalDaysOfPattern(listDailyPatternVal: Array<DailyPatternValue>): number {
            let sum = 0;
            _.forEach(listDailyPatternVal, i => {
                sum += i.days;
            });
            return sum;
        }

        /**
         * Loop forward daily pattern days.
         */
        private loopForwardPatternDays(dailyPatternValue: DailyPatternValue, currentDate: moment.Moment): Array<OptionDate> {
            let self = this;
            let result = [];
            let dayOfPattern = 1;

            // Day of pattern loop.
            while (dayOfPattern <= dailyPatternValue.days) {

                // Get display text and push to option date list.
                let optionDate = self.getDisplayText(dailyPatternValue, currentDate);
                result.push(optionDate);

                // Next day of pattern.
                dayOfPattern++;

                // Reserve dayOfPattern if reflection method = fill in the blank
                if (optionDate.textColor == '#ff0000' // Is holiday
                    && self.isFillInTheBlankChecked()) {
                    dayOfPattern--;
                }

                // Next day on calendar.
                currentDate = currentDate.add(1, 'days');

                // Current date reach calendar's end date.
                if (currentDate.isAfter(self.calendarEndDate, 'day')) {
                    // Break loop if is on screen B
                    if (self.isExecMode()) {
                        break;
                    }
                    // Break loop if is on screen A and pattern's total days is in calendar's range.
                    if (!self.isExecMode() && !self.isOutOfCalendarRange()) {
                        break;
                    }
                    // Or else continue loop.
                }

                //  When is on screen B: only process in calendar's range.
                if (self.isExecMode()) {
                    // Skip to next day if current date is before calendar's start date.
                    if (currentDate.isSameOrBefore(self.calendarStartDate, 'day')) {
                        _.remove(result, item => item === optionDate);
                        // reset flag because today does not count (out of calendar's range)
                        self.isMasterDataUnregisterd(false);
                    }
                }
            }
            return result;
        }

        /**
         * Get display text.
         */
        private getDisplayText(dailyPatternValue: DailyPatternValue, currentDate: moment.Moment): OptionDate {
            let self = this;

            // Is holiday
            if (self.isHolidaySettingChecked() && self.isHoliday(currentDate)) {
                return {
                    start: currentDate.format(CONST.DATE_FORMAT),
                    textColor: '#ff0000',
                    backgroundColor: 'white',
                    listText: [
                        self.getWorktypeNameByCode(self.reflectionSetting().holidaySetting.workTypeCode())
                    ]
                }
            }

            // Is statutory holiday
            if (self.isStatutorySettingChecked() && self.getWorkDayDivision(currentDate.day()) == WorkDayDivision.NonWorkingDayInLaw) {
                return {
                    start: currentDate.format(CONST.DATE_FORMAT),
                    textColor: '#ff0000',
                    backgroundColor: 'white',
                    listText: [
                        self.getWorktypeNameByCode(self.reflectionSetting().statutorySetting.workTypeCode())
                    ]
                }
            }

            // Is non-statutory holiday
            if (self.isNonStatutorySettingChecked() && self.getWorkDayDivision(currentDate.day()) == WorkDayDivision.NonWorkingDayOutrage) {
                return {
                    start: currentDate.format(CONST.DATE_FORMAT),
                    textColor: '#ff0000',
                    backgroundColor: 'white',
                    listText: [
                        self.getWorktypeNameByCode(self.reflectionSetting().nonStatutorySetting.workTypeCode())
                    ]
                }
            }

            // Is working day.
            let worktype = self.getWorktypeNameByCode(dailyPatternValue.workTypeSetCd);
            let worktime = self.getWorktimeNameByCode(dailyPatternValue.workingHoursCd);
            if (!worktype) {
                self.isMasterDataUnregisterd(true);
                return {
                    start: currentDate.format(CONST.DATE_FORMAT),
                    textColor: '#0000ff',
                    backgroundColor: 'white',
                    listText: [
                        nts.uk.resource.getText('KSM005_43') // display no master data
                    ]
                }
            } else if (worktime) { // work time is set => work day
                return {
                    start: currentDate.format(CONST.DATE_FORMAT),
                    textColor: '#0000ff',
                    backgroundColor: 'white',
                    listText: [
                        worktype,
                        worktime
                    ]
                }
            } else { // worktime not set => day off
                return {
                    start: currentDate.format(CONST.DATE_FORMAT),
                    textColor: 'red',
                    backgroundColor: 'white',
                    listText: [
                        worktype
                    ]
                }
            }
        }

        /**
         * Get worktype name by code.
         */
        private getWorktypeNameByCode(code: string): string {
            let self = this;
            let result = _.find(self.listWorkType(), wt => wt.workTypeCode == code);
            if (result) {
                return result.name;
            }
            return '';
        }

        /**
         * Get worktime name by code.
         */
        private getWorktimeNameByCode(code: string): string {
            let self = this;
            if (code) {
                // worktime is set => work day
                let result = _.find(self.listWorkTime(), wt => wt.code == code);
                if (result) {
                    return result.name;
                }
                return nts.uk.resource.getText('KSM005_43'); // display this if no data found
            }
            // worktime not set => off day
            return '';
        }

        /**
         * Get list date displaying on calendar.
         */
        private getListDateOnCalendar(): Array<string> {
            let self = this;
            let resultList = [];
            let currentDate = moment(self.calendarStartDate);
            while (currentDate.isSameOrBefore(self.calendarEndDate)) {
                resultList.push(currentDate.format('YYYY/MM/DD'));
                currentDate.add(1, 'days');
            }
            return resultList;
        }

        /**
         * Get work day division.
         */
        private getWorkDayDivision(dayOfWeek: number): WorkDayDivision {
            let self = this;
            switch (dayOfWeek) {
                case 0:
                    return self.weeklyWorkSetting.sunday;
                case 1:
                    return self.weeklyWorkSetting.monday;
                case 2:
                    return self.weeklyWorkSetting.tuesday;
                case 3:
                    return self.weeklyWorkSetting.wednesday;
                case 4:
                    return self.weeklyWorkSetting.thursday;
                case 5:
                    return self.weeklyWorkSetting.friday;
                case 6:
                    return self.weeklyWorkSetting.saturday;
                default:
                    return WorkDayDivision.WorkingDay;
            }
        }

        /**
         * Get isStatutorySetting checkbox value
         */
        private isStatutorySettingChecked(): boolean {
            let self = this;
            return self.reflectionSetting().statutorySetting.useClassification();
        }

        /**
         * Get isNonStatutorySetting checkbox value
         */
        private isNonStatutorySettingChecked(): boolean {
            let self = this;
            return self.reflectionSetting().nonStatutorySetting.useClassification();
        }

        /**
         * Get isHolidaySetting checkbox value
         */
        private isHolidaySettingChecked(): boolean {
            let self = this;
            return self.reflectionSetting().holidaySetting.useClassification();
        }

        /**
         * Check option dates data
         */
        public isOptionDatesEmpty(): boolean {
            let self = this;
            return nts.uk.util.isNullOrEmpty(self.optionDates());
        }

        /**
         * Check if isFillInTheBlank radio is selected.
         */
        private isFillInTheBlankChecked(): boolean {
            let self = this;
            return ReflectionMethod.FillInTheBlank == self.reflectionSetting().reflectionMethod();
        }

        /**
         * Set is out of calendar's range observable.
         */
        private setIsOutOfCalendarRange(listDailyPatternVal: Array<DailyPatternValue>): void {
            let self = this;

            // Get calendar's range.
            let calendarRange = self.calendarEndDate.diff(self.calendarStartDate, 'days') + 1;

            // Get total days.
            let totalDays = self.getTotalDaysOfPattern(listDailyPatternVal);

            // Set value
            self.isOutOfCalendarRange(false);
            if (totalDays > calendarRange) {
                self.isOutOfCalendarRange(true);
            }
        }

        /**
         * Set pattern range.
         */
        private setPatternRange(patternStartDate?: string): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();

            // Is ref mode
            if (!self.isExecMode()) {
                let parsedYm = nts.uk.time.formatYearMonth(self.yearMonthPicked());

                // Set pattern range.
                self.patternStartDate = moment(parsedYm, 'YYYY-MM').startOf('month');

                // Set calendar range.
                self.calendarStartDate = moment(self.patternStartDate);
                self.calendarEndDate = moment(self.patternStartDate).endOf('month');

                // Set pattern start date if has been set.
                if (patternStartDate) {
                    self.patternStartDate = moment(patternStartDate, CONST.DATE_FORMAT);
                }

                // Load holiday list.
                self.loadHolidayList().done(() => {
                    dfd.resolve();
                });
            }

            // Is on Exec Mode
            else {
                // Reset pattern range.
                self.patternStartDate = moment(self.shared.patternStartDate);
                if (patternStartDate) {
                    self.patternStartDate = moment(patternStartDate, CONST.DATE_FORMAT);
                }
                dfd.resolve();
            }

            return dfd.promise();
        }

        /**
         * Check if the day is holiday
         * @param: day
         */
        private isHoliday(day: moment.Moment): boolean {
            let self = this;
            let result = _.find(self.listHoliday, d => d.date == parseInt(day.format('YYYYMMDD')));
            if (result) {
                return true;
            }
            return false;
        }

        /**
         * Show error then close dialog.
         */
        private showErrorThenCloseDialog(): void {
            let vm = this;
            nts.uk.ui.dialog.alertError({ messageId: "Msg_37" }).then(() => {
				vm.$blockui("clear");
				vm.closeDialog();
			});
        }

        /**
         * Get param from caller (parent) screen.
         */
        private getParamFromCaller(): JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();

            // Get param from caller screen.
            self.shared = nts.uk.ui.windows.getShared("reflectionSetting");

            // Get default value if data not exist.
            if (!self.shared) {
                self.shared = self.getDefaultPatternReflection();
            }


            // Init patternReflection setting.
            self.reflectionSetting().fromDto(self.shared);

            // Is on Exec Mode
            if (self.shared.calendarStartDate && self.shared.calendarEndDate) {
                self.isExecMode(true);

                // Set calendar range.
                self.calendarStartDate = moment(self.shared.calendarStartDate, CONST.MOMENT_DATE_FORMAT);
                self.calendarEndDate = moment(self.shared.calendarEndDate, CONST.MOMENT_DATE_FORMAT);

				self.dateValue({
					startDate: self.calendarStartDate.format('YYYY/MM'),
					endDate: self.calendarEndDate.format('YYYY/MM')
				});

                // Date range must <= 31 days
                // If end date parameter out of range -> set end date to 31 days after start date parameter.
                let range = moment.duration(self.calendarEndDate.diff(self.calendarStartDate));
                if (range.asDays() > 31) {
                    self.calendarEndDate = moment(self.calendarStartDate).add(1, 'months').subtract(1, 'days');
                }
                self.startDate = self.calendarStartDate.date();
                self.endDate = self.calendarEndDate.date();
                self.yearMonthPicked(parseInt(self.calendarStartDate.format('YYYYMM')));

                // Set pattern range.
                self.setPatternRange(self.shared.patternStartDate);

                // Load holiday list.
                self.loadHolidayList().done(() => dfd.resolve());

            }
            // Is on Ref Mode
            else {
                self.isExecMode(false);
				$('#reflect-setting').addClass("pad-top");
                self.setPatternRange(self.shared.patternStartDate).done(() => dfd.resolve());
                self.loadHolidayList().done(() => dfd.resolve());
            }
            //set data startup windows
            self.setDataLoadWindows(self.isExecMode());
            return dfd.promise();
        }

        private setDataLoadWindows(isExecMode: boolean){
            const self = this;
            let mode = BootMode.REF_MODE;
            let defaultStartDate = moment(new Date()).startOf('month').format(CONST.MOMENT_DATE_FORMAT).toString();
            let defaultEndDate = moment(new Date()).endOf('month').format(CONST.MOMENT_DATE_FORMAT).toString();
            if(isExecMode) {
                mode =  BootMode.EXEC_MODE;
                defaultStartDate = self.shared.calendarStartDate;
                defaultEndDate = self.shared.calendarEndDate;
            }
            self.loadWindowsParam = {
                bootMode : mode,
                creationPeriodStartDate : defaultStartDate,
                creationPeriodEndDate : defaultEndDate,
                workCycleCode : self.shared.selectedPatternCd,
                refOrder : self.takeRefOrderList(mode),
                numOfSlideDays : 0
            }
        }

        private takeRefOrderList(bootMode: number): Array<number>{
            if(bootMode === BootMode.REF_MODE){
                return [WorkCreateMethod.WORK_CYCLE,WorkCreateMethod.PUB_HOLIDAY,WorkCreateMethod.WEEKLY_WORK];
            }
            if(bootMode === BootMode.EXEC_MODE){
                return [WorkCreateMethod.WORK_CYCLE,WorkCreateMethod.PUB_HOLIDAY,WorkCreateMethod.WEEKLY_WORK];
            }
        }

        private setCalendarData(data: Array<RefImageEachDayDto>){
            const self = this;
            let temp:Array<OptionDate> = ([]);
            data.forEach( (item) => {
                temp.push(self.setOptionDate(item));}
            );
            self.optionDates(temp);
            let workMonthlySettingTemp: Array<WorkMonthlySetting> = ([]);
            if(self.isExecMode()){
                //self.reflectionSetting.monthlyPatternCode('001');
                data.forEach( (item) => {
                    workMonthlySettingTemp.push(self.setMonthlySetting(item));}
                );
                self.workMonthlySetting(workMonthlySettingTemp);
            }
        }
        private setOptionDate(refImage: RefImageEachDayDto):OptionDate{
            let start = refImage.date;
            let textColor;
            if(refImage.workStyles === 0){
                textColor = '#ff0000';
            }else if(refImage.workStyles === 3){
                textColor = '#0000ff';
            } else {
                textColor = '#FF7F27';
            }

            let backgroundColor = 'white';
            let listText: Array<string> = [
                refImage.workInformation.workTypeName ? refImage.workInformation.workTypeName : '',
                refImage.workInformation.workTimeName ? refImage.workInformation.workTimeName : ''];
            let result:OptionDate = {
                start: moment(start).format('YYYY-MM-DD'),
                textColor: textColor,
                backgroundColor: backgroundColor,
                listText: listText
            }
            return result;
        }

        private setMonthlySetting(refImage: RefImageEachDayDto):WorkMonthlySetting{
            let workInformation:WorkInformationToRegis = {
                workTypeCode: refImage.workInformation.workTypeCode,
                workTimeCode: refImage.workInformation.workTimeCode
            }
            let result:WorkMonthlySetting = {
                workInformation: workInformation,
                ymdk:  moment(refImage.date).format('YYYY-MM-DD'),
                monthlyPatternCode: this.reflectionSetting().monthlyPatternCode()
            }
            return result;
        }
        public decide(): void {
            let vm = this;
            if(vm.promise()){
                vm.$dialog.confirm({ messageId: "Msg_1738" }).then((result: 'yes' | 'no') => {
                    if(result === 'yes'){
                        vm.register();
                    }
                });
            }else{
                vm.register();
            }

        }

        private register(): void{
            const vm = this;
            let param : MonthlyPatternRegisterCommand = {
                isOverWrite : vm.isOverWrite(),
                workMonthlySetting: vm.workMonthlySetting()
            }
            // If calendar's setting is empty.
            if (vm.isOptionDatesEmpty()) {
                vm.$dialog.error({ messageId: "Msg_512" });
                return;
            }
            service.registerMonthlyPattern(param).done(() => {
                nts.uk.ui.windows.setShared('returnedData', ko.toJS(vm.reflectionSetting()));
                vm.$dialog.info({ messageId: "Msg_15" }).then(function () {
                    vm.closeDialog();
                });
            }).fail(() => {
                vm.$dialog.error({ messageId: "Msg_340" }).then(() => {
                    vm.closeDialog();
                });
            });
        }
    }

    export class ReflectionSetting {
        calendarStartDate: KnockoutObservable<string> = ko.observable(''); // 開始日
        calendarEndDate: KnockoutObservable<string> = ko.observable(''); // 終了日
        selectedPatternCd: KnockoutObservable<string> = ko.observable(''); // 月間パターンコード
        patternStartDate: KnockoutObservable<string> = ko.observable('');
        // 上書き
        reflectionMethod: KnockoutObservable<ReflectionMethod> = ko.observable(ReflectionMethod.Overwrite);
        // 法定休日勤務種類
        statutorySetting: DayOffSetting = new DayOffSetting();
        // 法定外休日勤務種類
        nonStatutorySetting: DayOffSetting = new DayOffSetting();
        // 祝日勤務種類
        holidaySetting: DayOffSetting = new DayOffSetting();
        // 月間パターンコード
        monthlyPatternCode: KnockoutObservable<string> = ko.observable('');

        constructor(data?: service.model.ReflectionSetting) {
            let self = this;
            if(data) {
                self.calendarStartDate = ko.observable(data.calendarStartDate ? data.calendarStartDate : '');
                self.calendarEndDate = ko.observable(data.calendarEndDate ? data.calendarEndDate : '');
                self.patternStartDate = ko.observable(data.patternStartDate);
                self.selectedPatternCd = ko.observable(data.selectedPatternCd);
                self.reflectionMethod = ko.observable(data.reflectionMethod);
                self.statutorySetting = new DayOffSetting(data.statutorySetting);
                self.nonStatutorySetting = new DayOffSetting(data.nonStatutorySetting);
                self.holidaySetting = new DayOffSetting(data.holidaySetting);
                self.monthlyPatternCode = ko.observable(data.monthlyPatternCode ? data.monthlyPatternCode : '');
            }
        }

        public static newSetting(): ReflectionSetting {
            let newSetting = <service.model.ReflectionSetting>{};
            newSetting.calendarStartDate = moment().startOf('month').format(CONST.DATE_FORMAT);
            newSetting.calendarEndDate = moment().endOf('month').format(CONST.DATE_FORMAT);
            newSetting.selectedPatternCd = '';
            newSetting.patternStartDate = newSetting.calendarStartDate;
            newSetting.reflectionMethod = 0;
            let dummy = <service.model.DayOffSetting>{};
            dummy.useClassification = false;
            dummy.workTypeCode = '';
            newSetting.statutorySetting = dummy;
            newSetting.nonStatutorySetting = dummy;
            newSetting.holidaySetting = dummy;
			newSetting.monthlyPatternCode = '';

            return new ReflectionSetting(newSetting);
        }

        public fromDto(dto: service.model.ReflectionSetting) {
            let self = this;
            if (dto.calendarStartDate && dto.calendarEndDate) {
                self.calendarStartDate(dto.calendarStartDate);
                self.calendarEndDate(dto.calendarEndDate);
                self.monthlyPatternCode = ko.observable(dto.monthlyPatternCode);
            }
            self.selectedPatternCd(dto.selectedPatternCd);
            self.patternStartDate(dto.patternStartDate);
            self.reflectionMethod(dto.reflectionMethod);
            self.statutorySetting.fromDto(dto.statutorySetting);
            self.nonStatutorySetting.fromDto(dto.nonStatutorySetting);
            self.holidaySetting.fromDto(dto.holidaySetting);
        }
    }
    class DayOffSetting {
        useClassification: KnockoutObservable<boolean> = ko.observable(true);
        workTypeCode: KnockoutObservable<string>= ko.observable('');
        constructor(data?: service.model.DayOffSetting) {
            if(data){
                this.useClassification = ko.observable(data.useClassification);
                this.workTypeCode = ko.observable(data.workTypeCode);
            }
        }

        public fromDto(dto: service.model.DayOffSetting) {
            let self = this;
            self.useClassification(dto.useClassification);
            self.workTypeCode(dto.workTypeCode);
        }
    }

    interface OptionDate {
        start: string; // YYYY/MM/DD
        textColor: string;
        backgroundColor: string;
        listText: Array<string>;
    }

    export class GetStartupInfoParam{
        // Screen mode
        bootMode: KnockoutObservable<number>;
        // 開始日
        creationPeriodStartDate: KnockoutObservable<string>;
        // 終了日
        creationPeriodEndDate: KnockoutObservable<string>;
        //  勤務サイクルコード
        workCycleCode: KnockoutObservable<string>;
        // 勤務サイクル反映順序(OrderedList)
        refOrder: KnockoutObservableArray<number>;

        // スライド日数
        numOfSlideDays: KnockoutObservable<number>;

        constructor(data: GetStartupInfoParamDto){
            const model = this;
            model.bootMode = ko.observable(data.bootMode)
            model.creationPeriodStartDate = ko.observable(data.creationPeriodStartDate)
            model.creationPeriodEndDate = ko.observable(data.creationPeriodEndDate)
            model.workCycleCode = ko.observable(data.workCycleCode)
            model.refOrder = ko.observableArray(data.refOrder)
            model.numOfSlideDays = ko.observable(data.numOfSlideDays)
        }
    }
    export class GetWorkCycleAppImageParam{
        //  開始日
        creationPeriodStartDate: KnockoutObservable<string>;
        //  終了日
        creationPeriodEndDate: KnockoutObservable<string>;
        workCycleCode: KnockoutObservable<string>;
        // 勤務サイクル反映順序(OrderedList)
        refOrder: KnockoutObservableArray<number>;
        // スライド日数
        numOfSlideDays: KnockoutObservable<number>;
        // 法定休日勤務種類コード
        legalHolidayCd: KnockoutObservable<string>;
        // 法定外休日勤務種類コード
        nonStatutoryHolidayCd: KnockoutObservable<string>;
        // 祝日勤務種類コード
        holidayCd: KnockoutObservable<string>;
        constructor(data: GetWorkCycleAppImageParamDto){
            const model = this;
            model.creationPeriodStartDate = ko.observable(data.creationPeriodStartDate)
            model.creationPeriodEndDate = ko.observable(data.creationPeriodEndDate)
            model.workCycleCode = ko.observable(data.workCycleCode)
            model.refOrder = ko.observableArray(data.refOrder)
            model.numOfSlideDays = ko.observable(data.numOfSlideDays)
            model.legalHolidayCd = ko.observable(data.legalHolidayCd)
            model.nonStatutoryHolidayCd = ko.observable(data.nonStatutoryHolidayCd)
            model.holidayCd = ko.observable	(data.holidayCd)
        }
    }
}