module nts.uk.at.view.kaf005.a.viewmodel {
	import ItemModel = nts.uk.at.view.kaf005.shr.footer.viewmodel.ItemModel;
	import MessageInfo = nts.uk.at.view.kaf005.shr.footer.viewmodel.MessageInfo;
	import OverTime = nts.uk.at.view.kaf005.shr.viewmodel.OverTime;
	import HolidayTime = nts.uk.at.view.kaf005.shr.viewmodel.HolidayTime;
	import RestTime = nts.uk.at.view.kaf005.shr.viewmodel.RestTime;
	import WorkHours = nts.uk.at.view.kaf005.shr.work_info.viewmodel.WorkHours;
	import Work = nts.uk.at.view.kaf005.shr.work_info.viewmodel.Work;
	import WorkInfo = nts.uk.at.view.kaf005.shr.work_info.viewmodel.WorkInfo;
	import OvertimeWork = nts.uk.at.view.kaf005.shr.header.viewmodel.OvertimeWork;
	import Application = nts.uk.at.view.kaf000.shr.viewmodel.Application;
	import AppType = nts.uk.at.view.kaf000.shr.viewmodel.model.AppType;
	import Kaf000AViewModel = nts.uk.at.view.kaf000.a.viewmodel.Kaf000AViewModel;
	import AppInitParam = nts.uk.at.view.kaf000.shr.viewmodel.AppInitParam;
	import formatTime = nts.uk.time.format.byId;

	@bean()
	class Kaf005AViewModel extends Kaf000AViewModel {

		appType: KnockoutObservable<number> = ko.observable(AppType.OVER_TIME_APPLICATION);
		application: KnockoutObservable<Application>;
		isSendMail: KnockoutObservable<Boolean>;
		isAgentMode: KnockoutObservable<boolean> = ko.observable(false);
		overTimeWork: KnockoutObservableArray<OvertimeWork> = ko.observableArray([]);
		workInfo: KnockoutObservable<WorkInfo> = ko.observable(null);
		restTime: KnockoutObservableArray<RestTime> = ko.observableArray([]);
		holidayTime: KnockoutObservableArray<HolidayTime> = ko.observableArray([]);
		overTime: KnockoutObservableArray<OverTime> = ko.observableArray([]);
		messageInfos: KnockoutObservableArray<any> = ko.observableArray([]);
		dataSource: DisplayInfoOverTime;
		visibleModel: VisibleModel = new VisibleModel();
		isCalculation: Boolean = false;
		created(params: AppInitParam) {
			// new 
			const vm = this;
			vm.application = ko.observable(new Application(ko.toJS(vm.appType)));

			vm.createRestTime(vm.restTime);
			vm.createHolidayTime(vm.holidayTime);
			vm.createOverTime(vm.overTime);
			vm.bindWorkInfo(null);
			vm.bindMessageInfo(null);
			let empLst: Array<string> = [],
				dateLst: Array<string> = [];
			if (!_.isEmpty(params)) {
				if (!_.isEmpty(params.employeeIds)) {
					empLst = params.employeeIds;
				}
				if (!_.isEmpty(params.baseDate)) {
					let paramDate = moment(params.baseDate).format('YYYY/MM/DD');
					dateLst = [paramDate];
					vm.application().appDate(paramDate);
					vm.application().opAppStartDate(paramDate);
					vm.application().opAppEndDate(paramDate);
				}
				if (params.isAgentMode) {
					vm.isAgentMode(params.isAgentMode);
				}
			}
			vm.isSendMail = ko.observable(false);
			vm.$blockui("show");
			// load setting common KAF000
			vm.loadData(empLst, dateLst, vm.appType())
				.then((loadDataFlag: any) => {
					vm.application().appDate.subscribe(value => {
						console.log(value);
						if (value) {
							vm.changeDate();
						}
					});
					vm.application().prePostAtr.subscribe(value => {
						if (_.isEmpty(value)) {
							if (vm.appDispInfoStartupOutput().appDispInfoNoDateOutput.applicationSetting.appDisplaySetting.prePostDisplayAtr != 0) {
								vm.visibleModel.c15_3(!ko.toJS(vm.visibleModel.c15_3));								
							}
						}
					});
					if (loadDataFlag) {
						let param1 = {

						} as FirstParam;
						param1.companyId = vm.$user.companyId;
						// param1.dateOp = '2020/11/13';
						param1.overtimeAppAtr = OvertimeAppAtr.EARLY_NORMAL_OVERTIME;
						param1.appDispInfoStartupDto = ko.toJS(vm.appDispInfoStartupOutput);
						// param1.startTimeSPR = 100;
						// param1.endTimeSPR = 200;
						param1.isProxy = true;
						let command = {
							companyId: param1.companyId,
							dateOp: param1.dateOp,
							overtimeAppAtr: param1.overtimeAppAtr,
							appDispInfoStartupDto: param1.appDispInfoStartupDto,
							startTimeSPR: param1.startTimeSPR,
							endTimeSPR: param1.endTimeSPR,
							isProxy: param1.isProxy,
						};
						// load setting đơn xin
						return vm.$ajax(API.start, command);
					}
				}).then((successData: any) => {
					if (successData) {
						vm.dataSource = successData;
						vm.visibleModel = vm.createVisibleModel(vm.dataSource);
						vm.bindOverTimeWorks(vm.dataSource);
						vm.bindWorkInfo(vm.dataSource);
						vm.bindRestTime(vm.dataSource);
						vm.bindHolidayTime(vm.dataSource);
						vm.bindOverTime(vm.dataSource);
						vm.bindMessageInfo(vm.dataSource);
					}
				}).fail((failData: any) => {
					// xử lý lỗi nghiệp vụ riêng
					vm.handleErrorCustom(failData).then((result: any) => {
						if (result) {
							// xử lý lỗi nghiệp vụ chung
							vm.handleErrorCommon(failData);
						}
					});
				}).always(() => {
					vm.$blockui("hide");
					$('#kaf000-a-component4-singleDate').focus();
				});
		}


		handleErrorCustom(failData: any): any {
			const vm = this;
			if (failData.messageId == "Msg_26") {
				vm.$dialog.error({ messageId: failData.messageId, messageParams: failData.parameterIds })
					.then(() => {
						vm.$jump("com", "/view/ccg/008/a/index.xhtml");
					});
				return $.Deferred().resolve(false);
			}
			return $.Deferred().resolve(true);
		}

		handleConfirmMessage(listMes: any): any {
			const vm = this;
			if (_.isEmpty(listMes)) {
				return $.Deferred().resolve(true);
			}
			let msg = listMes[0];

			return vm.$dialog.confirm({ messageId: msg.msgID, messageParams: msg.paramLst })
				.then((value) => {
					if (value === 'yes') {
						return vm.handleConfirmMessage(_.drop(listMes));
					} else {
						return $.Deferred().resolve(false);
					}
				});
		}

		mounted() {
			const self = this;


		}

		createRestTime(restTime: KnockoutObservableArray<RestTime>) {
			const self = this;
			let restTimeArray = [];
			let length = 10;
			for (let i = 1; i < length + 1; i++) {
				let item = {} as RestTime;
				item.frameNo = String(i);
				item.displayNo = ko.observable('');
				item.start = ko.observable(null);
				item.end = ko.observable(null);
				restTimeArray.push(item);
			}
			restTime(restTimeArray);
		}

		createHolidayTime(holidayTime: KnockoutObservableArray<RestTime>) {
			const self = this;
			let holidayTimeArray = [];
			/*
			let length = 10;
			for (let i = 1; i < length + 1; i++) {
				let item = {} as HolidayTime;
				item.frameNo = String(i);
				item.displayNo = ko.observable('');
				item.start = ko.observable(null);
				item.preApp = ko.observable(null);
				item.actualTime = ko.observable(null);
				holidayTimeArray.push(item);
			}
			holidayTime(holidayTimeArray);
			 */
		}
		createOverTime(overTime: KnockoutObservableArray<OverTime>) {
			const self = this;
			let overTimeArray = [] as any;
			/*
			let length = 10;
			for (let i = 1; i < length + 1; i++) {
				let item = {} as OverTime;
				item.frameNo = String(i);
				item.displayNo = ko.observable('');
				item.applicationTime = ko.observable(null);
				item.preTime = ko.observable(null);
				item.actualTime = ko.observable(null);
				overTimeArray.push(item);
			}
			*/
			overTime(overTimeArray);
		}

		changeDate() {
			console.log('change date');
			const self = this;
			let param1 = {

			} as FirstParam;
			param1.companyId = self.$user.companyId;
			param1.dateOp = ko.toJS(self.appDispInfoStartupOutput).appDispInfoWithDateOutput.baseDate;
			param1.overtimeAppAtr = OvertimeAppAtr.EARLY_NORMAL_OVERTIME;
			param1.appDispInfoStartupDto = ko.toJS(self.appDispInfoStartupOutput);
			// param1.startTimeSPR = 100;
			// param1.endTimeSPR = 200;
			let command = {
				companyId: param1.companyId,
				dateOp: param1.dateOp,
				overtimeAppAtr: param1.overtimeAppAtr,
				appDispInfoStartupDto: param1.appDispInfoStartupDto,
				startTimeSPR: param1.startTimeSPR,
				endTimeSPR: param1.endTimeSPR,
				overTimeAppSet: self.dataSource.infoNoBaseDate.overTimeAppSet,
				worktypes: self.dataSource.infoBaseDateOutput.worktypes
			}
			self.$ajax(API.changeDate, command)
				.done((res: DisplayInfoOverTime) => {
					self.dataSource.infoWithDateApplicationOp = res.infoWithDateApplicationOp;
					self.dataSource.calculationResultOp = res.calculationResultOp;
					self.dataSource.workdayoffFrames = res.workdayoffFrames;
					self.dataSource.appDispInfoStartup = res.appDispInfoStartup;

					self.bindOverTimeWorks(self.dataSource);
					self.bindWorkInfo(self.dataSource);
					self.bindRestTime(self.dataSource);
					self.bindHolidayTime(self.dataSource);
					self.bindOverTime(self.dataSource);

				})
				.fail((res: any) => {

				})
				.always(() => self.$blockui('hide'));
		}

		toAppOverTime() {
			const vm = this;
			let dataSource = vm.dataSource;
			let appOverTime = {} as AppOverTime;
			appOverTime.overTimeClf = dataSource.overtimeAppAtr;
			let workInfo = vm.workInfo() as WorkInfo;
			let workInfoOp = {} as WorkInformation;
			// work type and time
			// A4 ---
			if (!_.isNil(workInfo.workType())) {
				workInfoOp.workType = workInfo.workType().code;
				workInfoOp.workTime = workInfo.workTime().code;
				appOverTime.workInfoOp = workInfoOp;
			}
			appOverTime.workHoursOp = [] as Array<TimeZoneWithWorkNo>;
			if (!_.isNil(workInfo.workHours1.start())
				&& !_.isNil(workInfo.workHours1.end())) {
				let timeZone = {} as TimeZoneWithWorkNo;
				timeZone.workNo = 1;
				timeZone.timeZone = {} as TimeZone_New;
				timeZone.timeZone.startTime = workInfo.workHours1.start();
				timeZone.timeZone.endTime = workInfo.workHours1.end();
				appOverTime.workHoursOp.push(timeZone);
			}

			if (!_.isNil(workInfo.workHours2.start())
				&& !_.isNil(workInfo.workHours2.end())) {
				let timeZone = {} as TimeZoneWithWorkNo;
				timeZone.workNo = 2;
				timeZone.timeZone = {} as TimeZone_New;
				timeZone.timeZone.startTime = workInfo.workHours2.start();
				timeZone.timeZone.endTime = workInfo.workHours2.end();
				appOverTime.workHoursOp.push(timeZone);
			}
			// A5 ---
			let restTime = vm.restTime() as Array<RestTime>;
			appOverTime.breakTimeOp = [] as Array<TimeZoneWithWorkNo>;
			_.forEach(restTime, (item: RestTime) => {
				if (!_.isNil(item.start()) && !_.isNil(item.end())) {
					let timeZone = {} as TimeZoneWithWorkNo;
					timeZone.workNo = Number(item.frameNo);
					timeZone.timeZone = {} as TimeZone_New;
					timeZone.timeZone.startTime = item.start();
					timeZone.timeZone.endTime = item.end();
					appOverTime.breakTimeOp.push(timeZone);
				}
			});

			// A6 ---
			appOverTime.applicationTime = {} as ApplicationTime;
			let applicationTime = appOverTime.applicationTime;
			applicationTime.applicationTime = [] as Array<OvertimeApplicationSetting>;

			let overTime = vm.overTime() as Array<OverTime>;
			_.forEach(overTime, (item: OverTime) => {
				if (!_.isNil(item.applicationTime())) {
					let overtimeApplicationSetting = {} as OvertimeApplicationSetting;
					if (item.applicationTime() > 0) {
						overtimeApplicationSetting.applicationTime = item.applicationTime();
						overtimeApplicationSetting.frameNo = Number(item.frameNo);
						overtimeApplicationSetting.attendanceType = AttendanceType.NORMALOVERTIME;
						applicationTime.applicationTime.push(overtimeApplicationSetting);						
					}
				}
			});
			let holidayTime = vm.holidayTime() as Array<HolidayTime>;
			_.forEach(holidayTime, (item: HolidayTime) => {
				if (!_.isNil(item.start())) {
					let overtimeApplicationSetting = {} as OvertimeApplicationSetting;
					overtimeApplicationSetting.applicationTime = item.start();
					overtimeApplicationSetting.frameNo = Number(item.frameNo);
					overtimeApplicationSetting.attendanceType = AttendanceType.BREAKTIME;
					applicationTime.applicationTime.push(overtimeApplicationSetting);
				}
			})

			applicationTime.reasonDissociation = [] as Array<ReasonDivergence>;



			// 
			appOverTime.application = {} as ApplicationDto;
			appOverTime.application = ko.toJS(vm.application);
			appOverTime.application.employeeID = vm.$user.employeeId;
			appOverTime.application.inputDate = moment(new Date()).format('YYYY/MM/DD HH:mm:ss');
			appOverTime.application.enteredPerson = vm.$user.employeeId;



			return appOverTime;
		}

		register() {
			const vm = this;
			vm.$blockui("show");
			let appDispInfoStartupOutput = ko.toJS(vm.appDispInfoStartupOutput);
			let commandCheck = {} as ParamCheckBeforeRegister;
			let applicationTemp = vm.toAppOverTime();
			commandCheck.require = true;
			commandCheck.companyId = vm.$user.companyId;
			commandCheck.appOverTime = applicationTemp;
			commandCheck.displayInfoOverTime = vm.dataSource;

			let appOverTimeTemp = null as AppOverTime;

			// validate chung KAF000
			vm.$validate('#kaf000-a-component4 .nts-input', '#kaf000-a-component3-prePost', '#kaf000-a-component5-comboReason')
				.then((isValid) => {
					if (isValid) {
						// validate riêng cho màn hình
						return vm.$validate('.inputTime');
					}
				}).then((result) => {
					// check trước khi đăng kí
					if (result) {
						return vm.$ajax('at', API.checkBefore, commandCheck);
					}
				}).then((result: CheckBeforeOutput) => {
					if (!_.isNil(result)) {
						appOverTimeTemp = result.appOverTime;
						// xử lý confirmMsg
						return vm.handleConfirmMessage(result.confirmMsgOutputs);
					}
				}).then((result) => {
					if (result) {
						let commandRegister = {} as RegisterCommand;
						commandRegister.companyId = vm.$user.companyId;
						appOverTimeTemp.application = applicationTemp.application;
						commandRegister.appOverTime = appOverTimeTemp;
						if (!_.isNil(appDispInfoStartupOutput.appDispInfoWithDateOutput.opListApprovalPhaseState)) {
							commandRegister.approvalPhaseState = appDispInfoStartupOutput.appDispInfoWithDateOutput.opListApprovalPhaseState;
						}
						// 残業申請の表示情報．申請表示情報．申請設定（基準日関係なし）．メールサーバ設定済区分
						commandRegister.isMail = appDispInfoStartupOutput.appDispInfoNoDateOutput.mailServerSet;
						// 残業申請の表示情報．申請表示情報．申請設定（基準日関係なし）．申請設定．申請種類別設定
						commandRegister.appTypeSetting = appDispInfoStartupOutput.appDispInfoNoDateOutput.applicationSetting.appTypeSetting[0];
						// đăng kí 
						return vm.$ajax('at', API.register, commandRegister).then(() => {
							return vm.$dialog.info({ messageId: "Msg_15" }).then(() => {
								return true;
							});
						});
					}
				}).then((result) => {
					if (result) {
						// gửi mail sau khi đăng kí
						// return vm.$ajax('at', API.sendMailAfterRegisterSample);
						return true;
					}
				}).fail((failData) => {
					// xử lý lỗi nghiệp vụ riêng
					vm.handleErrorCustom(failData).then((result: any) => {
						if (result) {
							// xử lý lỗi nghiệp vụ chung
							vm.handleErrorCommon(failData);
						}
					});
				}).always(() => {
					vm.$blockui("hide");
				});
		}
		// header
		bindOverTimeWorks(res: DisplayInfoOverTime) {
			const self = this;
			let overTimeWorks = [];
			{
				let item = new OvertimeWork();
				let currentMonth = res.infoNoBaseDate.agreeOverTimeOutput.currentMonth;
				item.yearMonth = ko.observable(currentMonth);
				overTimeWorks.push(item);
			}
			{
				let item = new OvertimeWork();
				let nextMonth = res.infoNoBaseDate.agreeOverTimeOutput.nextMonth;
				item.yearMonth = ko.observable(nextMonth);
				overTimeWorks.push(item);
			}
			self.overTimeWork(overTimeWorks);
		}
		//  work-info 
		bindWorkInfo(res: DisplayInfoOverTime) {
			const self = this;
			if (!ko.toJS(self.workInfo)) {
				let workInfo = {} as WorkInfo;
				let workType = {} as Work;
				let workTime = {} as Work;
				let workHours1 = {} as WorkHours;
				workHours1.start = ko.observable(null);
				workHours1.end = ko.observable(null);
				workHours1.start.subscribe((value) => {
					if (_.isNumber(value)) {
						self.getBreakTimes();
					}
				})
				workHours1.end.subscribe((value) => {
					if (_.isNumber(value)) {
						self.getBreakTimes();
					}
				})
				let workHours2 = {} as WorkHours;
				workHours2.start = ko.observable(null);
				workHours2.end = ko.observable(null);
				workInfo.workType = ko.observable(workType);
				workInfo.workTime = ko.observable(workTime);
				workInfo.workHours1 = workHours1;
				workInfo.workHours2 = workHours2;
				self.workInfo(workInfo);

				return;
			}
			let infoWithDateApplication = res.infoWithDateApplicationOp as InfoWithDateApplication;
			let workType = {} as Work;
			let workTime = {} as Work;
			let workHours1 = self.workInfo().workHours1 as WorkHours;
			let workHours2 = self.workInfo().workHours2 as WorkHours;
			if (!_.isNil(infoWithDateApplication)) {
				workType.code = infoWithDateApplication.workTypeCD;
				if (!_.isNil(workType.code)) {
					let workTypeList = res.infoBaseDateOutput.worktypes as Array<WorkType>;
					let item = _.find(workTypeList, (item: WorkType) => item.workTypeCode == workType.code)
					if (!_.isNil(item)) {
						workType.name = item.name;
					}
				} else {
					workType.name = self.$i18n('KAF_005_345');
				}
				workTime.code = infoWithDateApplication.workTimeCD;
				if (!_.isNil(workTime.code)) {
					let workTimeList = res.appDispInfoStartup.appDispInfoWithDateOutput.opWorkTimeLst as Array<WorkTime>;
					if (!_.isEmpty(workTimeList)) {
						let item = _.find(workTimeList, (item: WorkTime) => item.worktimeCode == workTime.code);
						if (!_.isNil(item)) {
							workTime.name = item.workTimeDisplayName.workTimeName;
						}
					}
				} else {
					workTime.name = self.$i18n('KAF_005_345');
				}
				// set input time
				let workHoursDto = infoWithDateApplication.workHours;
				if (workHoursDto) {
					workHours1.start(workHoursDto.startTimeOp1);
					workHours1.end(workHoursDto.endTimeOp1);
					workHours2.start(workHoursDto.startTimeOp2);
					workHours2.end(workHoursDto.endTimeOp2);
				}

			}
			self.workInfo().workType(workType);
			self.workInfo().workTime(workTime);
			self.workInfo().workHours1 = workHours1;
			self.workInfo().workHours2 = workHours2;

		}

		bindMessageInfo(res: DisplayInfoOverTime) {
			const self = this;
			if (_.isNil(res)) {
				let itemList = [
					new ItemModel('1', ''),
					new ItemModel('2', ''),
					new ItemModel('3', '')
				];
				let messageArray = [] as Array<MessageInfo>;
				let messageInfo = {} as MessageInfo;
				messageInfo.titleDrop = ko.observable('');
				messageInfo.listDrop = ko.observableArray(itemList);
				messageInfo.titleInput = ko.observable('');
				messageInfo.valueInput = ko.observable(null);
				messageInfo.selectedCode = ko.observable('1');
				messageArray.push(messageInfo);
				messageArray.push(messageInfo);

				self.messageInfos(messageArray);
				return;
			}
			let messageInfo = self.messageInfos() as Array<MessageInfo>;

			// #KAF005_90　{0}:残業申請の表示情報．基準日に関係しない情報．乖離時間枠．名称　←　NO = 1
			let divergenceTimeRoots = res.infoNoBaseDate.divergenceTimeRoot as Array<DivergenceTimeRoot>;
			if (!_.isEmpty(divergenceTimeRoots)) {
				let findNo1 = _.find(divergenceTimeRoots, { divergenceTimeNo: 1 });
				let findNo2 = _.find(divergenceTimeRoots, { divergenceTimeNo: 2 });
				if (!_.isNil(findNo1)) {
					messageInfo[0].titleDrop(findNo1.divTimeName);
					messageInfo[0].titleInput(findNo1.divTimeName);
				}
				if (!_.isNil(findNo2)) {
					messageInfo[1].titleDrop(findNo2.divTimeName);
					messageInfo[1].titleInput(findNo2.divTimeName);
				}
			}
			let messageInfo1 = 	self.messageInfos()[0] as MessageInfo;
			let messageInfo2 = 	self.messageInfos()[1] as MessageInfo;
			// 
			if (self.visibleModel.c11_1()) {
				let itemList = [] as Array<ItemModel>;
				let findResut = _.find(res.infoNoBaseDate.divergenceReasonInputMethod, { divergenceTimeNo: 1 });
				_.forEach(findResut.reasons, (item: DivergenceReasonSelect) => {
					let i = {} as ItemModel;
					i.code = item.divergenceReasonCode;
					i.name = self.$i18n('KAF005_340') + item.divergenceReasonCode + ' ' + item.reason;
					itemList.push(i);
					
				});
				messageInfo1.listDrop(itemList);
			} else {
				messageInfo1.selectedCode(null);
			}
			
			if (self.visibleModel.c12_1()) {
				let itemList = [] as Array<ItemModel>;
				let findResut = _.find(res.infoNoBaseDate.divergenceReasonInputMethod, { divergenceTimeNo: 2 });
				_.forEach(findResut.reasons, (item: DivergenceReasonSelect) => {
					let i = {} as ItemModel;
					i.code = item.divergenceReasonCode;
					i.name = self.$i18n('KAF005_340') + item.divergenceReasonCode + ' ' + item.reason;
					itemList.push(i);
					
				});
				messageInfo2.listDrop(itemList);
			} else {
				messageInfo2.selectedCode(null);
			}

		}

		bindRestTime(res: DisplayInfoOverTime) {
			const self = this;
			let infoWithDateApplication = res.infoWithDateApplicationOp as InfoWithDateApplication;
			let restTimeArray = self.restTime() as Array<RestTime>;
			if (!_.isNil(infoWithDateApplication)) {
				let breakTime = infoWithDateApplication.breakTime;
				if (!_.isNil(breakTime)) {
					if (!_.isEmpty(breakTime.timeZones)) {
						_.forEach(breakTime.timeZones, (item: TimeZone, index) => {
							if (Number(index) < 10) {
								let restItem = restTimeArray[index] as RestTime;
								restItem.start(item.start);
								restItem.end(item.end);
							}
						})

					}

				}
			} else {
				_.forEach(self.restTime(), (item: RestTime) => {
					item.start(null);
					item.end(null);
				});
			}
			self.restTime(_.clone(restTimeArray));
		}
		
		setColorForOverTime(isCalculation: Boolean, dataSource: DisplayInfoOverTime) {
			const self = this;
			if (!isCalculation) {
				
				return;
			}
			let overTimes = self.overTime() as Array<OverTime>;
			let overStateOutput = dataSource.calculationResultOp.overStateOutput;
			
			
			_.forEach(overTimes, (item: OverTime) => {
				if (item.type == AttendanceType.NORMALOVERTIME) {
					let backgroundColor = '';
					// ・事前申請超過：「残業申請の表示情報．計算結果．事前申請・実績の超過状態」を確認する
					if (!_.isNil(overStateOutput.advanceExcess)) {
						let excessStateDetail = overStateOutput.advanceExcess.excessStateDetail;
						if (!_.isEmpty(excessStateDetail)) {
							let result = _.find(excessStateDetail, (i: ExcessStateDetail) => {
								return i.frame == Number(item.frameNo) && i.type == AttendanceType.NORMALOVERTIME;
							});
							if (!_.isNil(result)) {
								if (result.excessState == ExcessState.EXCESS_ALARM) {
									backgroundColor = BACKGROUND_COLOR.bgC4;
								}
							}
						}
						
					}
					
					
					// ・実績申請超過：「残業申請の表示情報．計算結果．事前申請・実績の超過状態」を確認する
					if (!_.isNil(overStateOutput.achivementExcess)) {
						let excessStateDetail = overStateOutput.advanceExcess.excessStateDetail;
						if (!_.isEmpty(excessStateDetail)) {
							let result = _.find(excessStateDetail, (i: ExcessStateDetail) => {
								return i.frame == Number(item.frameNo) && i.type == AttendanceType.NORMALOVERTIME;
							});
							if (!_.isNil(result)) {
								if (result.excessState == ExcessState.EXCESS_ERROR) {
									backgroundColor = BACKGROUND_COLOR.bgC2;
								} else if (result.excessState == ExcessState.EXCESS_ALARM) {
									backgroundColor = BACKGROUND_COLOR.bgC3;
								}
								
							}
						}
						
					}
					
					// ・計算値：「残業申請の表示情報．計算結果」を確認する
					if (!_.isNil(dataSource.calculationResultOp.applicationTimes)) {
						let applicationTime = dataSource.calculationResultOp.applicationTimes[0].applicationTime;
						if (!_.isEmpty(applicationTime)) {
							let result = _.find(applicationTime, (i: OvertimeApplicationSetting) => {
								return i.frameNo == Number(item.frameNo) && i.attendanceType == AttendanceType.NORMALOVERTIME;
							});
							if (!_.isNil(result)) {
								if (result.applicationTime > 0) {
									backgroundColor = BACKGROUND_COLOR.bgC1;									
								}
							}
						}
					}
					
					item.backgroundColor(backgroundColor);
					
					
				} else if (item.type == AttendanceType.BONUSPAYTIME){
					
					
					
				} else if (item.type == AttendanceType.BONUSSPECIALDAYTIME) {
					
					
					
				}
			});
			
			
		}
		
		bindOverTime(res: DisplayInfoOverTime) {
			const self = this;
			let overTimeArray = [] as Array<OverTime>;
			let overTimeQuotaList = res.infoBaseDateOutput.quotaOutput.overTimeQuotaList as Array<OvertimeWorkFrame>;
			if (_.isEmpty(res.infoBaseDateOutput.quotaOutput.overTimeQuotaList)) return;
				// A6_7
				_.forEach(overTimeQuotaList, (item: OvertimeWorkFrame) => {
					let overTime = {} as OverTime;
					overTime.frameNo = String(item.overtimeWorkFrNo);
					overTime.displayNo = ko.observable(item.overtimeWorkFrName);
					overTime.applicationTime = ko.observable(self.isCalculation ? 0 : null);
					overTime.preTime = ko.observable(null);
					overTime.actualTime = ko.observable(null);
					overTime.type = AttendanceType.NORMALOVERTIME;
					overTime.visible = ko.computed(() => {
						return self.visibleModel.c2();
					}, self);
					overTime.backgroundColor = ko.observable('');
					overTimeArray.push(overTime);
			});
			// A6_27 A6_32 of row
			{
				let overTime1 = {} as OverTime;
				overTime1.frameNo = String(_.isEmpty(overTimeArray) ? 0 : overTimeArray.length);
				overTime1.displayNo = ko.observable(self.$i18n('KAF005_63'));
				overTime1.applicationTime = ko.observable(self.isCalculation ? 0 : null);
				overTime1.preTime = ko.observable(null);
				overTime1.actualTime = ko.observable(null);
				overTime1.type = AttendanceType.BONUSPAYTIME;
				overTime1.visible = ko.computed(() => {
						return self.visibleModel.c2() && self.visibleModel.c16();
					}, self);
				overTime1.backgroundColor = ko.observable('');	
				overTimeArray.push(overTime1);
				
				let overTime2 = {} as OverTime;
				overTime2.frameNo = String(overTimeArray.length - 1);
				overTime2.displayNo = ko.observable(self.$i18n('KAF005_65'));
				overTime2.applicationTime = ko.observable(self.isCalculation ? 0 : null);
				overTime2.preTime = ko.observable(self.isCalculation ? 0 : null);
				overTime2.actualTime = ko.observable(self.isCalculation ? 0 : null);
				
				overTime2.visible = ko.computed(() => {
						return self.visibleModel.c2() && self.visibleModel.c16();
					}, self);
					
				overTime2.backgroundColor = ko.observable('');					
				overTimeArray.push(overTime1);
				
				
			}

			// A6_8
			let calculationResultOp = res.calculationResultOp;
			if (!_.isNil(calculationResultOp)) {
				if (!_.isEmpty(calculationResultOp.applicationTimes)) {
					let applicationTime = calculationResultOp.applicationTimes[0].applicationTime;
					if (!_.isEmpty(applicationTime)) {
						_.forEach(applicationTime, (item: OvertimeApplicationSetting) => {
							let findOverTimeArray = _.find(overTimeArray, { frameNo: String(item.frameNo) }) as OverTime;
							if (!_.isNil(findOverTimeArray) && item.attendanceType == AttendanceType.NORMALOVERTIME) {
								findOverTimeArray.applicationTime(!self.isCalculation ? null : item.applicationTime);
							}
						});
					}
				}
			}
			// A6_9

			let opPreAppContentDisplayLst = res.appDispInfoStartup.appDispInfoWithDateOutput.opPreAppContentDispDtoLst;
			if (!_.isEmpty(opPreAppContentDisplayLst)) {
				let apOptional = opPreAppContentDisplayLst[0].apOptional;
				if (apOptional) {
					let applicationTime = apOptional.applicationTime;
					if (!_.isEmpty(applicationTime)) {
						_.forEach(applicationTime, (item: OvertimeApplicationSetting) => {
							let findOverTimeArray = _.find(overTimeArray, { frameNo: item.frameNo }) as OverTime;

							if (!_.isNil(findOverTimeArray) && item.attendanceType == AttendanceType.NORMALOVERTIME) {
								findOverTimeArray.preTime(item.applicationTime);
							}
						})
					}
				}
			}
			// A6_11
			let infoWithDateApplicationOp = res.infoWithDateApplicationOp;
			if (!_.isNil(infoWithDateApplicationOp)) {
				if (!_.isNil(infoWithDateApplicationOp.applicationTime)) {
					let applicationTime = infoWithDateApplicationOp.applicationTime.applicationTime;
					if (!_.isEmpty(applicationTime)) {
						_.forEach(applicationTime, (item: OvertimeApplicationSetting) => {
							let findOverTimeArray = _.find(overTimeArray, { frameNo: item.frameNo }) as OverTime;

							if (!_.isNil(findOverTimeArray) && item.attendanceType == AttendanceType.NORMALOVERTIME) {
								findOverTimeArray.actualTime(item.applicationTime);
							}
						})
					}
				}
			}

			self.overTime(overTimeArray);
			self.setColorForOverTime(self.isCalculation, self.dataSource);

		}

		bindHolidayTime(res: DisplayInfoOverTime) {
			const self = this;
			let holidayTimeArray = [] as Array<HolidayTime>;
			let workdayoffFrames = res.workdayoffFrames as Array<WorkdayoffFrame>;

			let calculationResultOp = res.calculationResultOp;
			// A7_7
			if (!_.isEmpty(workdayoffFrames)) {
				_.forEach(workdayoffFrames, (item: WorkdayoffFrame) => {
					// let findHolidayTimeArray = _.find(holidayTimeArray, { frameNo: String(item.workdayoffFrNo) }) as HolidayTime;
					let itemPush = {} as HolidayTime;
					// if (!_.isNil(findHolidayTimeArray)) {
						
					//	findHolidayTimeArray.displayNo(item.workdayoffFrName);
					// }
					itemPush.frameNo = String(item.workdayoffFrNo);
					itemPush.displayNo = ko.observable(item.workdayoffFrName);
					itemPush.start = ko.observable(null);
					itemPush.preApp = ko.observable(null);
					itemPush.actualTime = ko.observable(null);
					itemPush.type = AttendanceType.BREAKTIME;
					itemPush.visible = ko.computed(() => {
						return self.visibleModel.c30_1();
					}, this);
					holidayTimeArray.push(itemPush);
					
				})
			}
			
			// A7_11 A_15 A_19
			{
				let item = {} as HolidayTime;
				// A7_11
				item.frameNo = String(_.isEmpty(holidayTimeArray) ? 0 : holidayTimeArray.length);
				item.displayNo = ko.observable(self.$i18n('KAF005_341'));
				item.start = ko.observable(null);
				item.preApp = ko.observable(null);
				item.actualTime = ko.observable(null);
				item.type = AttendanceType.MIDDLE_BREAK_TIME;
				item.visible = ko.computed(() => {
									return self.visibleModel.c30_2();
								}, this);
				holidayTimeArray.push(item);	
			}
			
			{
				let item = {} as HolidayTime;
				// A7_11
				item.frameNo = String(_.isEmpty(holidayTimeArray) ? 0 : holidayTimeArray.length);
				item.displayNo = ko.observable(self.$i18n('KAF005_342'));
				item.start = ko.observable(null);
				item.preApp = ko.observable(null);
				item.actualTime = ko.observable(null);
				item.type = AttendanceType.MIDDLE_EXORBITANT_HOLIDAY;
				item.visible = ko.computed(() => {
									return self.visibleModel.c30_3();
								}, this);
				holidayTimeArray.push(item);	
			}
			
			{
				let item = {} as HolidayTime;
				// A7_11
				item.frameNo = String(_.isEmpty(holidayTimeArray) ? 0 : holidayTimeArray.length);
				item.displayNo = ko.observable(self.$i18n('KAF005_343'));
				item.start = ko.observable(null);
				item.preApp = ko.observable(null);
				item.actualTime = ko.observable(null);
				item.type = AttendanceType.MIDDLE_HOLIDAY_HOLIDAY;
				item.visible = ko.computed(() => {
									return self.visibleModel.c30_4();
								}, this);
				holidayTimeArray.push(item);	
			}
			
			

			// A7_8
			if (!_.isEmpty(calculationResultOp)) {

				if (!_.isEmpty(calculationResultOp.applicationTimes)) {
					let applicationTime = calculationResultOp.applicationTimes[0].applicationTime;
					if (!_.isEmpty(applicationTime)) {
						_.forEach(applicationTime, (item: OvertimeApplicationSetting) => {
							if (item.attendanceType == AttendanceType.BREAKTIME) {
								let findHolidayTimeArray = _.find(holidayTimeArray, { frameNo: String(item.frameNo) }) as HolidayTime;
	
								if (!_.isNil(findHolidayTimeArray)) {
									findHolidayTimeArray.start(item.applicationTime);
								}								
							}
						})
					}
				}

			}
			// A7_9
			// 申請表示情報．申請表示情報(基準日関係あり)．表示する事前申請内容．残業申請．申請時間．申請時間．申請時間
			let opPreAppContentDisplayLst = res.appDispInfoStartup.appDispInfoWithDateOutput.opPreAppContentDispDtoLst;
			if (!_.isEmpty(opPreAppContentDisplayLst)) {
				let apOptional = opPreAppContentDisplayLst[0].apOptional;
				if (apOptional) {
					let applicationTime = apOptional.applicationTime;
					if (!_.isEmpty(applicationTime)) {
						_.forEach(applicationTime, (item: OvertimeApplicationSetting) => {
							let findHolidayTimeArray = _.find(holidayTimeArray, { frameNo: String(item.frameNo) }) as HolidayTime;

							if (!_.isNil(findHolidayTimeArray) && item.attendanceType == AttendanceType.BREAKTIME) {
								findHolidayTimeArray.preApp(item.applicationTime);
							}
						})
					}
				}
			}

			// A7_10
			// 申請日に関係する情報．実績の申請時間．申請時間．申請時間

			self.holidayTime(_.clone(holidayTimeArray));

		}


		openDialogKdl003() {
			const self = this;
			nts.uk.ui.windows.setShared( 'parentCodes', {
                workTypeCodes: _.map( _.uniqBy( self.dataSource.infoBaseDateOutput.worktypes, e => e.workTypeCode ), (item: any) => item.workTypeCode ),
                selectedWorkTypeCode: self.workInfo().workType().code,
                workTimeCodes: _.map( self.dataSource.appDispInfoStartup.appDispInfoWithDateOutput.opWorkTimeLst, (item: any) => item.worktimeCode ),
                selectedWorkTimeCode: self.workInfo().workTime().code
            }, true);

			nts.uk.ui.windows.sub.modal( '/view/kdl/003/a/index.xhtml' ).onClosed( function(): any {
                //view all code of selected item 
                let childData = nts.uk.ui.windows.getShared('childData');
                if (childData) {
					let workType = {} as Work;
					workType.code = childData.selectedWorkTypeCode;
					workType.name = childData.selectedWorkTypeName;
					self.workInfo().workType(workType);
					let workTime = {} as Work;
                    workTime.code = childData.selectedWorkTimeCode;
					workTime.name = childData.selectedWorkTimeName;
					self.workInfo().workTime(workTime);
                }
            })

		}
		getFormatTime(number: number) {
			if (_.isNil(number)) return '';
			return (formatTime("Time_Short_HM", number));
		}
		createVisibleModel(res: DisplayInfoOverTime) {
			const self = this;
			let visibleModel = self.visibleModel;
			// 「残業申請の表示情報．基準日に関する情報．残業申請で利用する残業枠．残業枠一覧」 <> empty
			let c2 = !_.isEmpty(res.infoBaseDateOutput.quotaOutput.overTimeQuotaList);
			visibleModel.c2(c2);
			// 
			let c6 = true;
			visibleModel.c6(c6);

			// 「残業申請の表示情報．基準日に関係しない情報．残業申請設定．申請詳細設定．時刻計算利用区分」= する
			let c7 = res.infoNoBaseDate.overTimeAppSet.applicationDetailSetting.timeCalUse == NotUseAtr.USE
			visibleModel.c7(c7);

			// 「残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．NO = 1」 <> empty And
			// 残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．乖離理由を選択肢から選ぶ = true
			let c11_1 = true;
			let findResut = _.find(res.infoNoBaseDate.divergenceReasonInputMethod, { divergenceTimeNo: 1 });
			let c11_1_1 = _.isNil(findResut) ? false : !_.isEmpty(findResut.reasons);
			let c11_1_2 = c11_1_1 ? findResut.divergenceReasonSelected : false;
			c11_1 = c11_1_1 && c11_1_2;
			visibleModel.c11_1(c11_1);

			// 「残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．NO = 1」 <> empty　AND
			// 残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．乖離理由を入力する = true
			let c11_2 = true;
			let c11_2_1 = _.isNil(findResut) ? false : !_.isEmpty(findResut.reasons);
			let c11_2_2 = c11_2_1 ? findResut.divergenceReasonInputed : false;
			c11_2 = c11_2_1 && c11_2_2;
			visibleModel.c11_2(c11_2);

			// 「残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．NO = 2」 <> empty　AND
			// 残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．乖離理由を選択肢から選ぶ = true
			let c12_1 = true;
			findResut = _.find(res.infoNoBaseDate.divergenceReasonInputMethod, { divergenceTimeNo: 2 });
			let c12_1_1 = _.isNil(findResut) ? false : !_.isEmpty(findResut.reasons);
			let c12_1_2 = c12_1_1 ? findResut.divergenceReasonSelected : false;
			c12_1 = c12_1_1 && c12_1_2;
			visibleModel.c12_1(c12_1);

			// 「残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．NO = 2」 <> empty　AND
			// 残業申請の表示情報．基準日に関係しない情報．利用する乖離理由．乖離理由を入力する = true
			let c12_2 = true;
			findResut = _.find(res.infoNoBaseDate.divergenceReasonInputMethod, { divergenceTimeNo: 2 });
			let c12_2_1 = _.isNil(findResut) ? false : !_.isEmpty(findResut.reasons);
			let c12_2_2 = c12_2_1 ? findResut.divergenceReasonInputed : false;
			c12_2 = c12_2_2 && c12_2_2;
			visibleModel.c12_2(c12_2);

			// （「事前事後区分」が表示する　AND　「事前事後区分」が「事後」を選択している）　OR
			// （「事前事後区分」が表示しない　AND　「残業申請の表示情報．申請表示情報．申請表示情報(基準日関係あり)．事前事後区分」= 「事後」）
			let c15_3 = false;
			visibleModel.c15_3(c15_3);
			if (res.appDispInfoStartup.appDispInfoNoDateOutput.applicationSetting.appDisplaySetting.prePostDisplayAtr == 0) {
				c15_3 = res.appDispInfoStartup.appDispInfoWithDateOutput.prePostAtr == 1;
				visibleModel.c15_3(c15_3);
			}
			

			// 「残業申請の表示情報．基準日に関係しない情報．残業休日出勤申請の反映．時間外深夜時間を反映する」= する
			let c16 = res.infoNoBaseDate.overTimeReflect.nightOvertimeReflectAtr == NotUseAtr.USE;
			visibleModel.c16(c16);


			// 「残業申請の表示情報．基準日に関する情報．残業申請で利用する残業枠．フレックス時間表示区分」= true
			let c17 = res.infoBaseDateOutput.quotaOutput.flexTimeClf
			visibleModel.c17(c17);

			// ※15-3 = ×　AND　
			// 「残業申請の表示情報．基準日に関係しない情報．残業休日出勤申請の反映．残業申請．事前．休憩・外出を申請反映する」= する
			let c18_1 = true;
			visibleModel.c18_1(c18_1);

			// ※7 = ○　OR　※18-1 = ○
			let c18 = true;
			visibleModel.c18(c18);



			// 「残業申請の表示情報．基準日に関係しない情報．残業休日出勤申請の反映．残業申請．実績の勤務情報へ反映する」= する
			let c26 = res.infoNoBaseDate.overTimeReflect.overtimeWorkAppReflect.reflectActualWorkAtr == NotUseAtr.USE;
			visibleModel.c26(c26);


			// 「残業申請の表示情報．基準日に関係しない情報．残業申請設定．申請詳細設定．時間入力利用区分」= する
			let c28 = res.infoNoBaseDate.overTimeAppSet.applicationDetailSetting.timeInputUse == NotUseAtr.USE;
			visibleModel.c28(c28);


			// ※7 = ○　AND 「残業申請の表示情報．申請表示情報．申請表示情報(基準日関係なし)．複数回勤務の管理」= true
			let c29 = c7 && true;
			visibleModel.c29(c29);

			// 「残業申請の表示情報．計算結果．申請時間．申請時間．type」= 休出時間 があるの場合
			let c30_1 = true;
			visibleModel.c30_1(c30_1);

			let c30_2 = true;
			visibleModel.c30_2(c30_2);

			let c30_3 = true;
			visibleModel.c30_3(c30_3);

			let c30_4 = true;
			visibleModel.c30_4(c30_4);

			// let c30 = c30_1 || c30_2 || c30_3 || c30_4;
			// visibleModel.c30(c30);




			return visibleModel;
		}
		
		getBreakTimes() {
			const self = this;
			self.$blockui("show");
			let command = {} as ParamBreakTime;
			command.companyId = self.$user.companyId;
			command.workTypeCode = self.workInfo().workType().code;
			command.workTimeCode = self.workInfo().workTime().code;
			command.startTime = self.workInfo().workHours1.start();
			command.endTime = self.workInfo().workHours1.end();
			command.actualContentDisplayDtos = self.dataSource.appDispInfoStartup.appDispInfoWithDateOutput.opActualContentDisplayLstl;
			self.$ajax(API.breakTimes, command)
				.done((res: BreakTimeZoneSetting) => {
					if (res) {
						if (!_.isEmpty(res.timeZones)) {
							_.forEach(self.restTime(), (item: RestTime) => {
								let data = res.timeZones.shift();
								if (!_.isNil(data)) {
									item.start(data.start);
									item.end(data.end);
								} else {
									item.start(null);
									item.end(null);
								}
							});
						} else {
							_.forEach(self.restTime(), (item: RestTime) => {
								item.start(null);
								item.end(null);
							});
						}
					}
				})
				.fail((res: any) => {
					
				})
				.always(() => self.$blockui('hide'));
		}

		calculate() {
			const self = this;
			self.$blockui("show");
			console.log('calculate');
			let command = {} as ParamCalculationCMD;
			command.companyId = self.$user.companyId;
			command.employeeId = self.$user.employeeId;
			command.dateOp = ko.toJS(self.application).appDate;
			if (ko.toJS(self.appDispInfoStartupOutput).appDispInfoNoDateOutput.applicationSetting.appDisplaySetting.prePostDisplayAtr == 1) {
				command.prePostInitAtr = ko.toJS(self.application).prePostAtr;
			} else {
				command.prePostInitAtr = ko.toJS(self.appDispInfoStartupOutput).appDispInfoWithDateOutput.prePostAtr;
			}

			command.overtimeLeaveAppCommonSet = self.dataSource.infoNoBaseDate.overTimeAppSet.overtimeLeaveAppCommonSetting;
			if (self.dataSource.appDispInfoStartup.opPreAppContentDisplayLst) {
				let opPreAppContentDisplayLst = self.dataSource.appDispInfoStartup.opPreAppContentDisplayLst;
				if (!_.isEmpty(opPreAppContentDisplayLst)) {
					let preAppContentDisplay = opPreAppContentDisplayLst[0];
					if (!_.isNil(preAppContentDisplay.apOptional)) {
						let appOverTime = preAppContentDisplay.apOptional;
						command.advanceApplicationTime = appOverTime.applicationTime;
					}
				}
			}
			if (self.dataSource.infoWithDateApplicationOp) {
				command.achieveApplicationTime = self.dataSource.infoWithDateApplicationOp.applicationTime;
			}
			let workContent = {} as WorkContent;
			let workInfo = self.workInfo() as WorkInfo;
			workContent.workTypeCode = workInfo.workType().code as string;
			workContent.workTimeCode = workInfo.workTime().code as string;

			let timeZoneArray = [] as Array<TimeZone>;
			let timeZone = {} as TimeZone;
			if (!(_.isNil(workInfo.workHours1.start()) || _.isNil(workInfo.workHours1.end()))) {
				timeZone.frameNo = 1;
				timeZone.start = workInfo.workHours1.start();
				timeZone.end = workInfo.workHours1.end();
				timeZoneArray.push(timeZone);
			}
			timeZone = {} as TimeZone;
			if (!(_.isNil(workInfo.workHours2.start()) || _.isNil(workInfo.workHours2.end()))) {
				timeZone.frameNo = 2;
				timeZone.start = workInfo.workHours2.start();
				timeZone.end = workInfo.workHours2.end();
				timeZoneArray.push(timeZone);
			}

			workContent.timeZones = timeZoneArray;
			let breakTimeSheetArray = [] as Array<BreakTimeSheet>;
			let restTime = self.restTime() as Array<RestTime>;

			_.forEach(restTime, (item: RestTime) => {
				if (!(_.isNil(ko.toJS(item.start)) || _.isNil(ko.toJS(item.end)))) {
					let breakTimeSheet = {} as BreakTimeSheet;
					breakTimeSheet.breakFrameNo = Number(item.frameNo);
					breakTimeSheet.startTime = ko.toJS(item.start);
					breakTimeSheet.endTime = ko.toJS(item.end);
					breakTimeSheet.breakTime = 0;
					breakTimeSheetArray.push(breakTimeSheet);
				}

			});
			workContent.breakTimes = breakTimeSheetArray;
			command.workContent = workContent;
			self.$ajax(API.calculate, command)
				.done((res: DisplayInfoOverTime) => {
					if (res) {
						self.dataSource.calculationResultOp = res.calculationResultOp;
						self.dataSource.workdayoffFrames = res.workdayoffFrames;
						self.isCalculation = true;
						self.bindOverTime(self.dataSource);
						self.bindHolidayTime(self.dataSource);
					}
				})
				.fail((res: any) => {

				})
				.always(() => self.$blockui("hide"));

		}


	}
	const API = {
		start: 'at/request/application/overtime/start',
		changeDate: 'at/request/application/overtime/changeDate',
		checkBefore: 'at/request/application/overtime/checkBeforeRegister',
		register: 'at/request/application/overtime/register',
		calculate: 'at/request/application/overtime/calculate',
		breakTimes: 'at/request/application/overtime/breakTimes'
	}
	
	const BACKGROUND_COLOR = {
		// 計算値
		bgC1: '#F69164',
		// 超過エラー
		bgC2: '#FD4D4D',
		// 超過アラーム
		bgC3: '#F6F636',
		// 事前超過
		bgC4: '#ffc0cb'
		
	}
	export class VisibleModel {
		public c2: KnockoutObservable<Boolean>;
		public c6: KnockoutObservable<Boolean>;
		public c7: KnockoutObservable<Boolean>;
		public c11_1: KnockoutObservable<Boolean>;
		public c11_2: KnockoutObservable<Boolean>;
		public c12_1: KnockoutObservable<Boolean>;
		public c12_2: KnockoutObservable<Boolean>;
		public c15_3: KnockoutObservable<Boolean>;
		public c16: KnockoutObservable<Boolean>;
		public c17: KnockoutObservable<Boolean>;
		public c18: KnockoutObservable<Boolean>;
		public c18_1: KnockoutObservable<Boolean>;
		public c26: KnockoutObservable<Boolean>;
		public c28: KnockoutObservable<Boolean>;
		public c29: KnockoutObservable<Boolean>;
		public c30_1: KnockoutObservable<Boolean>;
		public c30_2: KnockoutObservable<Boolean>;
		public c30_3: KnockoutObservable<Boolean>;
		public c30_4: KnockoutObservable<Boolean>;
		public c30: KnockoutObservable<Boolean>;


		constructor() {
			const self = this;
			this.c2 = ko.observable(true);
			this.c6 = ko.observable(true);
			this.c7 = ko.observable(true);
			this.c11_1 = ko.observable(true);
			this.c11_2 = ko.observable(true);
			this.c12_1 = ko.observable(true);
			this.c12_2 = ko.observable(true);
			this.c15_3 = ko.observable(null);
			this.c16 = ko.observable(true);
			this.c17 = ko.observable(true);
			this.c18 = ko.observable(true);
			this.c18_1 = ko.observable(true);
			this.c26 = ko.observable(true);
			this.c28 = ko.observable(true);
			this.c29 = ko.observable(true);
			this.c30_1 = ko.observable(true);
			this.c30_2 = ko.observable(true);
			this.c30_3 = ko.observable(true);
			this.c30_4 = ko.observable(true);
			this.c30 = ko.computed(() => {
				return this.c30_1() || this.c30_2() || this.c30_3() || this.c30_4();
			}, this)
		}
	}
	enum NotUseAtr {
		Not_USE,
		USE
	}
	export interface ParamCalculationCMD {
		companyId: string;
		employeeId: string;
		dateOp: string;
		prePostInitAtr: number;
		overtimeLeaveAppCommonSet: OvertimeLeaveAppCommonSet;
		advanceApplicationTime: ApplicationTime;
		achieveApplicationTime: ApplicationTime;
		workContent: WorkContent;
	}
	export interface DisplayInfoOverTime {
		infoBaseDateOutput: InfoBaseDateOutput;
		infoNoBaseDate: InfoNoBaseDate;
		workdayoffFrames: Array<WorkdayoffFrame>;
		overtimeAppAtr: OvertimeAppAtr;
		appDispInfoStartup: any;
		isProxy: Boolean;
		calculationResultOp?: CalculationResult;
		infoWithDateApplicationOp?: InfoWithDateApplication;
	}
	export interface WorkdayoffFrame {
		workdayoffFrNo: number;
		workdayoffFrName: string;
	}
	export interface CalculationResult {
		flag: number;
		overTimeZoneFlag: number;
		overStateOutput: OverStateOutput;
		applicationTimes: Array<ApplicationTime>;
	}
	export interface OverStateOutput {
		isExistApp: boolean;
		advanceExcess: OutDateApplication;
		achivementStatus: number;
		achivementExcess: OutDateApplication;
	}
	export enum ExcessState {
		NO_EXCESS,
		EXCESS_ALARM,
		EXCESS_ERROR
	}
	export interface OutDateApplication {
		flex: number;
		excessStateMidnight: Array<ExcessStateMidnight>;
		overTimeLate: number;
		excessStateDetail: Array<ExcessStateDetail>;
		
	}
	export interface ExcessStateMidnight {
		excessState: number;
		legalCfl: number;
	}
	export enum StaturoryAtrOfHolidayWork {
		WithinPrescribedHolidayWork,
		ExcessOfStatutoryHolidayWork,
		PublicHolidayWork
	}
	export interface ExcessStateDetail {
		frame: number;
		type: number;
		excessState: number
	}
	export interface ParamBreakTime {
		companyId: string;
		workTypeCode: string;
		workTimeCode: string;
		startTime: number;
		endTime: number;
		actualContentDisplayDtos: any;
	}
	
	export interface InfoWithDateApplication {
		workTypeCD?: string;
		workTimeCD?: string;
		workHours?: WorkHoursDto;
		breakTime?: BreakTimeZoneSetting;
		applicationTime?: ApplicationTime;
	}
	export interface BreakTimeZoneSetting {
		timeZones?: Array<TimeZone>;
	}
	export interface TimeZone {
		frameNo: number;
		start: number;
		end: number;
	}
	export interface WorkHoursDto {
		startTimeOp1: number;
		endTimeOp1: number;
		startTimeOp2: number;
		endTimeOp2: number;
	}
	export interface InfoBaseDateOutput {
		worktypes: Array<WorkType>;
		quotaOutput: QuotaOuput;
	}
	export interface QuotaOuput {
		flexTimeClf: boolean;
		overTimeQuotaList: Array<OvertimeWorkFrame>;
	}
	export interface OvertimeWorkFrame {
		companyId: string;
		overtimeWorkFrNo: number;
		useClassification: number;
		transferFrName: string;
		overtimeWorkFrName: string;

	}
	export interface WorkType {
		workTypeCode: string;
		name: string;
	}

	export interface WorkTime {
		worktimeCode: string;
		workTimeDisplayName: WorkTimeDisplayName;
	}
	export interface WorkTimeDisplayName {
		workTimeName: string;
	}
	export interface InfoNoBaseDate {
		overTimeReflect: any;
		overTimeAppSet: OvertimeAppSet;
		agreeOverTimeOutput: AgreeOverTimeOutput;
		divergenceReasonInputMethod: Array<DivergenceReasonInputMethod>;
		divergenceTimeRoot: Array<DivergenceTimeRoot>;
	}
	export interface DivergenceReasonInputMethod {
		divergenceTimeNo: number;
		divergenceReasonInputed: boolean;
		divergenceReasonSelected: boolean;
		reasons: Array<DivergenceReasonSelect>;
	}
	
	export interface DivergenceReasonSelect {
		divergenceReasonCode: string;
		reason: string;
	}
	export interface DivergenceTimeRoot {
		divergenceTimeNo: number;
		companyId: string;
		divTimeUseSet: number;
		divTimeName: string;
		divType: number;
	}
	export interface DivergenceReasonInputMethod {
		divergenceTimeNo: number;
		companyId: string;
		divergenceReasonInputed: boolean;
		divergenceReasonSelected: boolean;
		reasons: Array<DivergenceReasonSelect>;
	}
	export interface OvertimeAppSet {
		companyID: string;
		overtimeLeaveAppCommonSetting: any;
		overtimeQuotaSet: Array<any>;
		applicationDetailSetting: any;
	}
	export interface AgreeOverTimeOutput {
		detailCurrentMonth: AgreementTimeImport;
		detailNextMonth: AgreementTimeImport;
		currentMonth: string;
		nextMonth: string;
	}
	export interface AgreementTimeImport {
		employeeId: string;
		confirmed?: AgreeTimeOfMonthExport;
		afterAppReflect?: AgreeTimeOfMonthExport;
		confirmedMax?: AgreMaxTimeOfMonthExport;
		afterAppReflectMax?: AgreMaxTimeOfMonthExport;
		errorMessage?: string;
	}
	export interface AgreeTimeOfMonthExport {
		agreementTime: number;
		limitErrorTime: number;
		limitAlarmTime: number
		exceptionLimitErrorTime?: number;
		exceptionLimitAlarmTime?: number;
		status: number
	}
	export interface AgreMaxTimeOfMonthExport {
		agreementTime: number;
		maxTime: number;
		status: number;
	}
	enum OvertimeAppAtr {

		EARLY_OVERTIME,
		NORMAL_OVERTIME,
		EARLY_NORMAL_OVERTIME
	}
	export enum AttendanceType {

		NORMALOVERTIME,
		BREAKTIME,
		BONUSPAYTIME,
		BONUSSPECIALDAYTIME,
		MIDNIGHT,
		SHIFTNIGHT,
		MIDDLE_BREAK_TIME,
		MIDDLE_EXORBITANT_HOLIDAY,
		MIDDLE_HOLIDAY_HOLIDAY
		
		
		
		
		
	}

	export interface FirstParam { // start param
		companyId: string; // 会社ID
		appType?: number; // 申請種類
		sids?: Array<string>; // 申請者リスト
		dates?: Array<string>; // 申請対象日リスト
		mode: number; // 新規詳細モード
		dateOp?: string // 申請日
		overtimeAppAtr: number; // 残業申請区分
		appDispInfoStartupDto: any; // 申請表示情報
		startTimeSPR?: number; // SPR連携の開始時刻
		endTimeSPR?: number; // SPR連携の終了時刻
		isProxy: boolean; // 代行申請か
	}

	export interface SecondParam { // start param
		companyId: string; // 会社ID
		employeeId: string; // 社員ID
		appDate: string; // 申請日
		prePostInitAtr: number; // 事前事後区分
		overtimeLeaveAppCommonSet: OvertimeLeaveAppCommonSet; // 残業休出申請共通設定
		advanceApplicationTime: ApplicationTime; // 事前の申請時間
		achivementApplicationTime: ApplicationTime; // 実績の申請時間
		workContent: WorkContent; // 勤務内容
	}
	export interface OvertimeLeaveAppCommonSet {
		preExcessDisplaySetting: number; // 事前超過表示設定
		extratimeExcessAtr: number; // 時間外超過区分
		extratimeDisplayAtr: number; // 時間外表示区分
		performanceExcessAtr: number; // 実績超過区分
		checkOvertimeInstructionRegister: number; // 登録時の指示時間超過チェック
		checkDeviationRegister: number; // 登録時の乖離時間チェック
		overrideSet: number; // 実績超過打刻優先設定

	}
	export interface ApplicationTime {
		applicationTime: Array<OvertimeApplicationSetting>; //  申請時間
		flexOverTime: number; // フレックス超過時間
		overTimeShiftNight: OverTimeShiftNight; // 就業時間外深夜時間
		anyItem: Array<AnyItemValue>; // 任意項目
		reasonDissociation: Array<any>; // 乖離理由
	}
	export interface OvertimeApplicationSetting {
		frameNo: number;
		attendanceType: number;
		applicationTime: number
	}
	export interface OverTimeShiftNight {
		midNightHolidayTimes: Array<any>;
		midNightOutSide: number;
		overTimeMidNight: number;
	}
	export interface AnyItemValue {
		itemNo: number;
		times: number;
		amount: number;
		time: number
	}
	export interface ReasonDivergence {

		reason: DivergenceReason;
		reasonCode: string;
		diviationTime: number;
	}
	export interface DivergenceReason {

	}
	export interface WorkContent {
		workTypeCode: string;
		workTimeCode: string;
		timeZones: Array<TimeZone>;
		breakTimes: Array<BreakTimeSheet>;
	}
	export interface TimeZone {
		start: number;
		end: number;
	}
	export interface BreakTimeSheet {
		breakFrameNo: number;
		startTime: number;
		endTime: number;
		breakTime: number;
	}
	export interface TimeZoneWithWorkNo {
		workNo: number;
		timeZone: TimeZone_New;
	}
	export interface TimeZone_New {
		startTime: number;
		endTime: number;
	}
	export interface AppOverTime {
		overTimeClf: number;
		applicationTime: ApplicationTime;
		breakTimeOp?: Array<TimeZoneWithWorkNo>;
		workHoursOp?: Array<TimeZoneWithWorkNo>;
		workInfoOp?: WorkInformation;
		detailOverTimeOp?: AppOvertimeDetail;
		application: ApplicationDto;
	}
	export interface AppOvertimeDetail {
		applicationTime: number;
		yearMonth: number;
		actualTime: number;
		limitErrorTime: number;
		limitAlarmTime: number;
		exceptionLimitErrorTime: number;
		exceptionLimitAlarmTime: number;
		year36OverMonth: Array<number>;
		numOfYear36Over: number;
		actualTimeAnnual: number;
		limitTime: number;
		appTimeAgreeUpperLimit: number;
		overTime: number;
		upperLimitTimeMonth: number;
		averageTimeLst: Array<Time36UpLimitMonth>;
		upperLimitTimeAverage: number;

	}
	export interface Time36UpLimitMonth {
		periodYearStart: number;
		periodYearEnd: number;
		averageTime: number;
		totalTime: number;
	}
	export interface ApplicationDto {
		version: number;
		appID: string;
		prePostAtr: number;
		employeeID: string;
		appType: number;
		appDate: string;
		enteredPerson: string;
		inputDate: string;
		reflectionStatus: ReflectionStatus
		opStampRequestMode?: number;
		opReversionReason?: string;
		opAppStartDate?: string;
		opAppEndDate?: string;
		opAppReason?: string;
		opAppStandardReasonCD?: number;
	}
	export interface ReflectionStatus {

	}
	export interface WorkInformation {
		workType: string;
		workTime: string;
	}

	export interface ParamCheckBeforeRegister {
		require: boolean;
		companyId: string;
		displayInfoOverTime: DisplayInfoOverTime;
		appOverTime: AppOverTime;
	}
	export interface CheckBeforeOutput {
		appOverTime: AppOverTime;
		confirmMsgOutputs: Array<any>;
	}
	export interface RegisterCommand {
		companyId: string;
		appOverTime: AppOverTime;
		approvalPhaseState: Array<any>;
		isMail: Boolean;
		appTypeSetting: any;
	}















}