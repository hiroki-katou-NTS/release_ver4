/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kaf007_ref.a.viewmodel {
	import Application = nts.uk.at.view.kaf000.shr.viewmodel.Application;
	import AppWorkChange = nts.uk.at.view.kaf007_ref.shr.viewmodel.AppWorkChange;
	import ModelDto = nts.uk.at.view.kaf007_ref.shr.viewmodel.ModelDto;
	import AppType = nts.uk.at.view.kaf000.shr.viewmodel.model.AppType;
	import Kaf000AViewModel = nts.uk.at.view.kaf000.a.viewmodel.Kaf000AViewModel;
	import ReflectWorkChangeApp = nts.uk.at.view.kaf007_ref.shr.viewmodel.ReflectWorkChangeApp;

	@bean()
	export class Kaf007AViewModel extends Kaf000AViewModel {

		appType: KnockoutObservable<number> = ko.observable(AppType.WORK_CHANGE_APPLICATION);
		application: KnockoutObservable<Application> = ko.observable(new Application(this.appType()));
		model: KnockoutObservable<ModelDto> = ko.observable(null);
		isSendMail: KnockoutObservable<Boolean>;
		reflectWorkChange: ReflectWorkChangeApp;
		appWorkChange: AppWorkChange;
		setupType: number;
		workTypeLst: any[];
		comment1: KnockoutObservable<string> = ko.observable("");
		comment2: KnockoutObservable<string> = ko.observable("");
		isStraightGo: KnockoutObservable<boolean> = ko.observable(false);
		isStraightBack: KnockoutObservable<boolean> = ko.observable(false);

		created(params: any) {
			const vm = this;
			vm.isSendMail = ko.observable(false);
			vm.reflectWorkChange = new ReflectWorkChangeApp("", 1);
			vm.setupType = null;
			vm.appWorkChange = new AppWorkChange("", "", "", "", null, null, null, null);

			vm.$blockui("show");
			vm.loadData([], [], vm.appType())
				.then((loadDataFlag: any) => {
					if (loadDataFlag) {
						let empLst: any = [],
							dateLst: any = [],
							appDispInfoStartupOutput = ko.toJS(vm.appDispInfoStartupOutput),
							command = { empLst, dateLst, appDispInfoStartupOutput };
						return vm.$ajax(API.startNew, command);
					}
				}).then((successData: any) => {
					if (successData) {
						console.log(successData);
						vm.fetchData(successData);
					}
				}).fail((failData: any) => {
					console.log(failData);
					if (failData.messageId === "Msg_43") {
						vm.$dialog.error(failData).then(() => { vm.$jump("com", "/view/ccg/008/a/index.xhtml"); });

					} else {
						vm.$dialog.error(failData);
					}
				}).always(() => {vm.$blockui("hide"); $('#kaf000-a-component4-singleDate').focus();});
		}

		mounted() {
			const vm = this;

			vm.application.subscribe(app => {
				if (app) {
					let startDate = app.opAppStartDate();
					let endDate = app.opAppEndDate();
					let checkFormat = vm.validateAppDate(startDate, endDate);

					if (checkFormat) {
						vm.changeAppDate();
					}
				}
			});
		}

		validateAppDate(start: string, end: string) {
			let startDate = moment(start);
			let endDate = moment(end);
			if (startDate.isValid() && endDate.isValid()) {
				return true;
			}
			return false;
		}

		changeAppDate() {
			const vm = this;

			vm.$errors("clear");
			let startDate = vm.application().opAppStartDate(),
				endDate = vm.application().opAppEndDate();
			let appDates = []
			appDates.push(startDate, endDate);
			let command = {
				empLst: ko.toJS(vm.application().employeeIDLst),
				dateLst: appDates,
				appWorkChangeDispInfo: ko.toJS(vm.model)
			};

			vm.$validate([
				'#kaf000-a-component4 .nts-input'
			]).then((valid: boolean) => {
				if (valid) {
					return vm.$blockui("show").then(() => vm.$ajax(API.changeAppDate, command));
				}
			}).done((res: any) => {
				vm.fetchData(res);
				// $('#kaf000-a-component4-singleDate').focus();
			}).fail(err => {
				console.log(err)
				if (err.messageId === "Msg_43") {
					vm.$dialog.error(err).then(() => { vm.$jump("com", "/view/ccg/008/a/index.xhtml"); });

				} else {
					vm.$dialog.error(err);
				}
			}).always(() => vm.$blockui("hide"));
		}

		fetchData(params: any) {
			const vm = this;
			vm.model({
				workTypeCD: ko.observable(params.workTypeCD),
				workTimeCD: ko.observable(params.workTimeCD),
				appDispInfoStartupOutput: ko.observable(params.appDispInfoStartupOutput),
				reflectWorkChangeAppDto: ko.observable(params.reflectWorkChangeAppDto),
				workTypeLst: params.workTypeLst,
				setupType: ko.observable(params.setupType),
				predetemineTimeSetting: ko.observable(params.predetemineTimeSetting),
				appWorkChangeSet: params.appWorkChangeSet
			});
			// vm.reflectWorkChange = params.reflectWorkChangeAppDto;
			vm.reflectWorkChange.companyId = params.reflectWorkChangeAppDto.companyId;
			vm.reflectWorkChange.whetherReflectAttendance(params.reflectWorkChangeAppDto.whetherReflectAttendance);
			vm.getWorkDispName(params.workTypeLst,
				params.workTypeCD,
				params.workTimeCD,
				params.appDispInfoStartupOutput.appDispInfoWithDateOutput.opWorkTimeLst);
			if (params.appWorkChangeSet.initDisplayWorktimeAtr === 1) {
				vm.appWorkChange.startTime1(null);
				vm.appWorkChange.endTime1(null);
				vm.appWorkChange.startTime2(null);
				vm.appWorkChange.endTime2(null);
			} else {
				var lstTimezone = [];

				if(params.predetemineTimeSetting) [
					lstTimezone = params.predetemineTimeSetting.prescribedTimezoneSetting.lstTimezone;
				]
				var time1 = _.filter(lstTimezone, ['workNo', 1]);
				var time2 = _.filter(lstTimezone, ['workNo', 2]);

				vm.appWorkChange.startTime1(time1.length > 0 ? time1[0].start : null);
				vm.appWorkChange.endTime1(time1.length > 0 ? time1[0].end : null);
				vm.appWorkChange.startTime2(time2.length > 0 ? time2[0].start : null);
				vm.appWorkChange.endTime2(time2.length > 0 ? time2[0].end : null);
			}
			vm.comment1(vm.model().appWorkChangeSet.comment1.comment);
			$("#comment1")
				.css("color", vm.model().appWorkChangeSet.comment1.colorCode)
				.css("fontWeight", vm.model().appWorkChangeSet.comment1.bold == true ? "bold" : "");
			vm.comment2(vm.model().appWorkChangeSet.comment2.comment);
			$(".comment2")
				.css("color", vm.model().appWorkChangeSet.comment2.colorCode)
				.css("fontWeight", vm.model().appWorkChangeSet.comment2.bold == true ? "bold" : "");
		}

		getWorkDispName(workTypeLst: any, workTypeCode: string, workTimeCode: string, workTimeLst: any) {
			const vm = this;

			vm.appWorkChange.workTimeCode(workTimeCode);
			vm.appWorkChange.workTypeCode(workTypeCode);
			var dataWorkType = _.filter(workTypeLst, (x) => { return workTypeCode === x.workTypeCode });
			vm.appWorkChange.workTypeName(dataWorkType.length > 0 ? dataWorkType[0].name : vm.$i18n('KAF007_79'));
			if(workTimeCode) {
				var dataWorktTime = _.filter(workTimeLst, (x) => { return workTimeCode === x.worktimeCode });
				vm.appWorkChange.workTimeName(dataWorktTime.length > 0 ? dataWorktTime[0].workTimeDisplayName.workTimeName : vm.$i18n('KAF007_79'));
			} else {
				vm.appWorkChange.workTimeName(null);
			}
		}

		register() {
			const vm = this;

			let holidayDateLst: any[] = [];
			let timeZone1 = null;
			if(vm.appWorkChange.startTime1() !== null && vm.appWorkChange.endTime1() !== null && ko.toJS(vm.appWorkChange.startTime1) !== "" && ko.toJS(vm.appWorkChange.endTime1) !== "") {
				timeZone1 = {
					workNo: 1,
					timeZone: {
						startTime: vm.appWorkChange.startTime1(),
						endTime: vm.appWorkChange.endTime1()
					}
				}
			}

			let timeZone2 = null;
			if (vm.appWorkChange.startTime2() !== null && vm.appWorkChange.endTime2() !== null && ko.toJS(vm.appWorkChange.startTime2) !== "" && ko.toJS(vm.appWorkChange.endTime2) !== "") {
				timeZone2 = {
					workNo: 2,
					timeZone: {
						startTime: vm.appWorkChange.startTime2(),
						endTime: vm.appWorkChange.endTime2()
					}
				}
			}

			let timeZoneWithWorkNoLst = [];

			if (timeZone1 !== null && vm.reflectWorkChange.whetherReflectAttendance() === 1 && vm.model().setupType() === 0) {
				timeZoneWithWorkNoLst.push(timeZone1);
			}
			if (timeZone2 !== null && vm.appDispInfoStartupOutput().appDispInfoNoDateOutput.managementMultipleWorkCycles
			 && vm.reflectWorkChange.whetherReflectAttendance() === 1 && vm.model().setupType() === 0) {
				timeZoneWithWorkNoLst.push(timeZone2);
			}

			let appWorkChangeDto = {
				straightGo: vm.isStraightGo() ? 1 : 0,
				straightBack: vm.isStraightBack() ? 1 : 0,
				opWorkTypeCD: vm.model().workTypeCD(),
				opWorkTimeCD: vm.model().workTimeCD(),
				timeZoneWithWorkNoLst: timeZoneWithWorkNoLst
			}

			let applicationDto = {
				appDate: vm.application().appDate(),
				appID: vm.application().appID(),
				appType: vm.application().appType,
				employeeIDLst: vm.application().employeeIDLst(),
				opAppEndDate: vm.application().opAppEndDate(),
				opAppReason: vm.application().opAppReason(),
				opAppStandardReasonCD: vm.application().opAppStandardReasonCD(),
				opAppStartDate: vm.application().opAppStartDate(),
				opReversionReason: vm.application().opReversionReason(),
				opStampRequestMode: vm.application().opStampRequestMode(),
				prePostAtr: vm.application().prePostAtr(),
				inputDate: moment(new Date()).format('YYYY/MM/DD HH:mm:ss'),
				enteredPerson: vm.$user.employeeId
			}

			let command = {
				mode: true,
				companyId: vm.$user.companyId,
				applicationDto: ko.toJS(applicationDto),
				appWorkChangeDto: ko.toJS(appWorkChangeDto),
				isError: vm.model().appDispInfoStartupOutput().appDispInfoWithDateOutput.opErrorFlag,
				appDispInfoStartupDto: ko.toJS(vm.model().appDispInfoStartupOutput)
			}

			vm.$blockui("show");
			// if(vm.reflectWorkChange.whetherReflectAttendance() === 1) {
			// 	vm.$validate('.nts-input').then((valid) => { if(!valid) return;})
			// }
			
			vm.$validate('#kaf000-a-component4 .nts-input', '#kaf000-a-component3-prePost', '#kaf000-a-component5-comboReason')
				.then(isValid => {
					if (isValid) {
						if(vm.reflectWorkChange.whetherReflectAttendance() === 1 && vm.model().setupType() === 0) {
							return vm.$validate('.nts-input');
						} 
						return true;
					}
				})
				.then((isValid) => {
					if(isValid) {
						if(!_.isLength(vm.appWorkChange.startTime2()) && _.isLength(vm.appWorkChange.endTime2())) {
							vm.$errors({'#time2Start': {messageId: 'Msg_1956'}});
							return false;
						}
						if(_.isLength(vm.appWorkChange.startTime2()) && !_.isLength(vm.appWorkChange.endTime2())) {
							vm.$errors({'#time2End': {messageId: 'Msg_1956'}});
							return false;
						}
						return true;
					}
				})
				.then(result => {
					if (!result) return;
					return vm.$ajax(API.checkBeforeRegister, command);
				}).then(res => {
					if (res == undefined) return;
					if (!_.isEmpty(res.holidayDateLst)) {
						holidayDateLst = res.holidayDateLst;
					}
					if (_.isEmpty(res.confirmMsgLst)) {
						return vm.registerData(command);
					} else {
						let listTemp = _.clone(res.confirmMsgLst);
						vm.handleConfirmMessage(listTemp, command);
					}
				}).done(result => {
					if (result != undefined) {
						if (_.isEmpty(holidayDateLst)) {
							vm.$dialog.info({ messageId: "Msg_15" }).then(() => {
								location.reload();
							});
						} else {
							let dispMsg = nts.uk.resource.getMessage('Msg_15') + "\n";
							let x = nts.uk.resource.getMessage('Msg_1663', [holidayDateLst.join('、')]);
							dispMsg += x;
							vm.$dialog.info(dispMsg).then(() => {
								location.reload();
							})
						}
					}
				})
				.fail(err => {
					let messageId, messageParams;
					if(err.errors) {
						let errors = err.errors;
						messageId = errors[0].messageId;
					} else {
						messageId = err.messageId;
						messageParams = [err.parameterIds.join('、')];
					}
					vm.$dialog.error({ messageId: messageId, messageParams: messageParams });
				})
				.always(() => vm.$blockui("hide"));
		}

		handleConfirmMessage(listMes: any, res: any): any {
			let vm = this;
			if (!_.isEmpty(listMes)) {
				let item = listMes.shift();
				return vm.$dialog.confirm({ messageId: item.msgID, messageParams: item.paramLst })
					.then((value) => {
						if (value == 'yes') {
							if (_.isEmpty(listMes)) {
								return vm.registerData(res);
							} else {
								return vm.handleConfirmMessage(listMes, res);
							}

						}
					});
			}
		}

		registerData(params: any): any {
			let vm = this;

			return vm.$ajax(API.register, params);
		}

		// public conditionA14() {
		// 	const vm = this;

		// 	return ko.computed(() => {
		// 		if(vm.model() !== null && vm.model().setupType() !== null && vm.model().setupType() === 0 && vm.model().reflectWorkChangeAppDto().whetherReflectAttendance === 1) {
		// 			return true;
		// 		};
		// 		return false;
		// 	}, vm);
		// }
	}

	const API = {
		startNew: "at/request/application/workchange/startNew",
		register: "at/request/application/workchange/addworkchange",
		changeAppDate: "at/request/application/workchange/changeAppDate",
		checkBeforeRegister: "at/request/application/workchange/checkBeforeRegisterPC"
	}
}