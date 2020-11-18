module nts.uk.at.view.ksu003.a.viewmodel {
	import getText = nts.uk.resource.getText;
	import setShared = nts.uk.ui.windows.setShared;
	import getShared = nts.uk.ui.windows.getShared;
	import errorDialog = nts.uk.ui.dialog.alertError;
	import block = nts.uk.ui.block;
	import kdl045 = nts.uk.at.view.kdl045.share.model;
	import exTable = nts.uk.ui.exTable;
	import chart = nts.uk.ui.chart;
	import storage = uk.localStorage;
	import formatById = nts.uk.time.format.byId;
	import parseTime = nts.uk.time.parseTime;
	import model = nts.uk.at.view.ksu003.a.model;
	import duration = nts.uk.time.minutesBased.duration; // convert time 
	/**
	 * ScreenModel
	 */
	var ruler: any;
	export class ScreenModel {

		KEY: string = 'USER_KSU003_INFOR';
		employeeIdLogin: string = __viewContext.user.employeeId;

		/*A2_1_3*/
		targetDate: KnockoutObservable<string> = ko.observable('2020/05/02');
		targetDateDay: KnockoutObservable<string> = ko.observable('');
		/*A2_2*/
		organizationName: KnockoutObservable<string> = ko.observable('');
		/*A2_5*/
		selectedDisplayPeriod: KnockoutObservable<number> = ko.observable(1);
		/*A3_2*/
		itemList: KnockoutObservableArray<ItemModel>;
		selectOperationUnit: KnockoutObservable<string> = ko.observable('0');
		/*A3_4*/
		sortList: KnockoutObservableArray<ItemModel>;
		checked: KnockoutObservable<string> = ko.observable('0');
		/*A3_3 */
		checkedName: KnockoutObservable<boolean> = ko.observable(true);

		showA9: boolean;

		// dùng cho xử lý của botton toLeft, toRight, toDown
		indexBtnToLeft: KnockoutObservable<number> = ko.observable(0);
		indexBtnToDown: KnockoutObservable<number> = ko.observable(0);

		/*Data get from Screen A*/
		dataFromA: KnockoutObservable<model.InforScreenADto> = ko.observable();
		dataScreen003A: KnockoutObservable<model.DataScreenA> = ko.observable();
		dataScreen045A: KnockoutObservable<model.DataFrom045> = ko.observable();
		dataA6: KnockoutObservable<model.A6Data> = ko.observable();
		dataInitStartKsu003Dto: KnockoutObservable<model.GetInfoInitStartKsu003Dto> = ko.observable();
		displayWorkInfoByDateDto: KnockoutObservable<Array<model.DisplayWorkInfoByDateDto>> = ko.observable();
		displayDataKsu003: KnockoutObservable<Array<model.DisplayWorkInfoByDateDto>> = ko.observable();
		getFixedWorkInformationDto: KnockoutObservable<Array<any>> = ko.observable();
		dataColorA6: Array<any> = [];

		/** 社員勤務情報　dto */
		employeeWorkInfo: KnockoutObservable<model.EmployeeWorkInfoDto> = ko.observable();
		employeeScheduleInfo: Array<any> = [];

		/** A6_color① */
		workingInfoColor: KnockoutObservable<string> = ko.observable('');
		/** A7 color */
		totalTimeColor: KnockoutObservable<string> = ko.observable('');
		/** A3_2 pixel */
		operationUnit: KnockoutObservable<number> = ko.observable(3.5);
		operationOneMinus: KnockoutObservable<number> = ko.observable(12);

		localStore: KnockoutObservable<ILocalStore> = ko.observable();
		startScrollGc: KnockoutObservable<number> = ko.observable();

		lstEmpId: Array<any> = [];
		startScrollY: number = 8;
		timeRange: number = 0;

		timeBrkText: KnockoutObservable<string> = ko.observable();
		timeBrkMinu: KnockoutObservable<number> = ko.observable();

		checkGetInfo: boolean = false;
		dataOfGantChart: Array<ITimeGantChart> = [];
		midDataGC: Array<any> = [];
		lstEmpIdByTime: Array<any> = [];
		lstEmpIdByEmp: Array<any> = [];
		disableDs: any = [];
		leftDs: any = [];
		tooltip: any = [];

		/** Các loại thời gian */
		constructor() {
			let self = this;
			// get data from sc A
			self.dataFromA = ko.observable(getShared('dataFromA'));
			self.lstEmpId = _.flatMap(self.dataFromA().listEmp, c => [c.id]);
			self.targetDate(self.dataFromA().daySelect);
			self.targetDateDay(self.targetDate() + moment(self.targetDate()).format('(ddd)'));
			self.tooltip = getShared("dataTooltip");
			self.itemList = ko.observableArray([
				new ItemModel('0', getText('KSU003_13')),
				new ItemModel('1', getText('KSU003_14')),
				new ItemModel('2', getText('KSU003_15')),
				new ItemModel('3', getText('KSU003_16')),
				new ItemModel('4', getText('KSU003_17'))
			]);

			self.sortList = ko.observableArray([
				new ItemModel('0', getText('KSU003_59')),
				new ItemModel('1', getText('KSU003_60'))
			]);
			self.showA9 = true;

			/* 開始時刻順に並び替える(A3_3)はチェックされている */
			self.checked.subscribe((value) => {
				self.sortEmployee(value);
			});

			/** A3_4 */
			self.checkedName.subscribe((value) => {
				self.localStore().showWplName = value;
				storage.setItemAsJson(self.KEY, self.localStore());
			});

			/** A2_5 */
			self.selectedDisplayPeriod.subscribe((value) => {
				self.localStore().displayFormat = value;
				storage.setItemAsJson(self.KEY, self.localStore());
			});

			/** 操作単位選択に選択する A3_2 */
			self.selectOperationUnit.subscribe((value) => {
				let c = parseInt(value) + 1;
				if (value == '1') {
					self.operationOneMinus(6);
				}

				if (value == '2') {
					self.operationOneMinus(4);
				}

				if (value == '3') {
					self.operationOneMinus(2);
					c = 6
				}

				if (value == '4') {
					self.operationOneMinus(1);
					c = 12;
				}

				if (!_.isNil(ruler))
					ruler.setSnatchInterval(c);

				self.localStore().operationUnit = value;
				storage.setItemAsJson(self.KEY, self.localStore());


			});
			// tính tổng thời gian khi thay đổi start và end time
			$("#extable-ksu003").on("extablecellupdated", (dataCell: any) => {
				if (self.checkGetInfo == true) {
					self.checkGetInfo = false;
					return;
				}
				let index = dataCell.originalEvent.detail.rowIndex;
				let dataMid = $("#extable-ksu003").exTable('dataSource', 'middle').body[index];
				if (dataCell.originalEvent.detail.columnKey === "worktypeCode" || dataCell.originalEvent.detail.columnKey === "worktimeCode") {
					self.getEmpWorkFixedWorkInfo(dataMid.workTypeCode, dataMid.worktimeCode);
				}
				if (dataCell.originalEvent.detail.columnKey === "startTime1" || dataCell.originalEvent.detail.columnKey === "startTime2" ||
					dataCell.originalEvent.detail.columnKey === "endTime1" || dataCell.originalEvent.detail.columnKey === "endTime2") {
					let timeConvert = self.convertTime(dataMid.startTime1, dataMid.endTime1, dataMid.startTime2, dataMid.endTime2);
					self.employeeScheduleInfo.forEach((x, i) => {
						if (i === dataCell.originalEvent.detail.rowIndex) {
							if (dataCell.originalEvent.detail.columnKey === "startTime1") {
								x.startTime1 = timeConvert.start;
							}
							if (dataCell.originalEvent.detail.columnKey === "startTime2") {
								x.startTime2 = timeConvert.start2;
							}
							if (dataCell.originalEvent.detail.columnKey === "endTime1") {
								x.endTime1 = timeConvert.end;
							}
							if (dataCell.originalEvent.detail.columnKey === "endTime2") {
								x.endTime2 = timeConvert.end2;
							}
						}
					})
					let totalTime = null;
					if (timeConvert.end != "" || (timeConvert.end2 != "" && timeConvert.start2 != "")) {
						totalTime = (timeConvert.end - timeConvert.start) + (timeConvert.end2 - timeConvert.start2);
						totalTime = formatById("Clock_Short_HM", totalTime);
					}
					$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "totalTime", totalTime != null ? totalTime : "");
				}

				// check thời gian Tú chưa làm - TQP 時刻が不正かチェックする
			});
		}


		/**
		 * startPage
		 */
		public startPage(): JQueryPromise<any> {
			block.grayout();
			let self = this;
			let dfd = $.Deferred<any>();


			self.getData();
			self.hoverEvent(self.targetDate());
			// A2_2 TQP 
			// self.organizationName(self.dataInitStartKsu003Dto().displayInforOrganization().displayName());
			dfd.resolve();
			block.clear();
			return dfd.promise();
		}

		public getData() {
			let self = this;
			let canModified = 0;
			if (self.dataFromA().dayEdit < self.targetDate()) {
				canModified = 1;
			}
			let data003A: model.DataScreenA = {
				startDate: self.dataFromA().startDate, // 開始日		
				endDate: self.dataFromA().endDate, // 終了日
				/** 基準の組織 */
				unit: self.dataFromA().unit,
				id: self.dataFromA().unit == 0 ? self.dataFromA().workplaceId : self.dataFromA().workplaceGroupId,
				name: self.dataFromA().workplaceName,
				timeCanEdit: self.dataFromA().dayEdit, //いつから編集可能か
				/** 複数回勤務管理 */
				targetInfor: 0,//対象情報 : 複数回勤務 (1 :true,0:false)
				canModified: canModified,//修正可能 CanModified
				scheCorrection: [],//スケジュール修正の機能制御  WorkTimeForm
				employeeInfo: [],
			}
			self.dataScreen003A(data003A);
			self.getInforFirt();
			self.getWorkingByDate(self.targetDate(), 1);
		}

		hoverEvent(targetDate: any) {
			let self = this;
			let tooltip = [];
			tooltip = _.filter(self.tooltip, (x: any) => { return x.ymd === targetDate });
			if (tooltip.length > 0) {
				let htmlToolTip = tooltip[0].htmlTooltip;
				$('#event').tooltip({
					content: htmlToolTip != null ? htmlToolTip : "",
					tooltipClass: "tooltip-styling"
				});

				//$("#event").tooltip().tooltip("open");
			}
		}

		// Tạo data để truyền vào ExTable
		public convertDataIntoExtable() {

			let self = this, disableDs: any = [], leftDs: any = [], middleDs: any = [],
				timeGantChart: Array<ITimeGantChart> = [], typeOfTime: string, startTimeArr: any = [];
			let gcFixedWorkTime: Array<model.IFixedFlowFlexTime> = [],
				gcBreakTime: Array<model.IBreakTime> = [],
				gcOverTime: Array<model.IOverTime> = [],
				gcSupportTime: any = null,
				gcFlowTime: Array<model.IFixedFlowFlexTime> = [],
				gcFlexTime: Array<model.IFixedFlowFlexTime> = [],
				gcCoreTime: Array<model.ICoreTime> = [],
				gcHolidayTime: Array<model.IHolidayTime> = [],
				gcShortTime: Array<model.IShortTime> = [];

			let displayWorkInfoByDateDto = null, displayWorkInfo: any = [];

			_.forEach(self.lstEmpId, (schedule: any) => {
				let timeZoneDto: any = {
					startTime: { dayDivision: 0, time: 600 },//開始時刻 
					endTime: { dayDivision: 0, time: 660 }
				};
				let timeZoneDto1: any = [];
				timeZoneDto1.push({
					startTime: { dayDivision: 0, time: 600 },//開始時刻 
					endTime: { dayDivision: 0, time: 650 }
				});

				let dailyAttdTimeVacationDto: any = [];

				dailyAttdTimeVacationDto.push({
					timeAbbyakLeave: 0, //時間年休使用時間
					timeOff: 0,//時間代休使用時間
					excessPaidHoliday: 0,//超過有休使用時間
					specialHoliday: 0,//特別休暇使用時間
					frameNO: 0,//特別休暇枠NO
					childNursingLeave: 0,//子の看護休暇使用時間
					nursingCareLeave: 0//介護休暇使用時間
				});

				let timeVacationDto: any = {
					timeZone: timeZoneDto1,
					usageTime: dailyAttdTimeVacationDto
				}

				let timeVacationAndType: any = {
					typeVacation: 0,
					timeVacation: timeVacationDto
				}

				let timeVacationAndTypes: any = [];
				let shortTime: any = [];
				timeVacationAndTypes.push(timeVacationAndType);

				shortTime.push({
					startTime: { dayDivision: 0, time: 480 },//開始時刻 
					endTime: { dayDivision: 0, time: 540 }
				})

				let employeeWorkInfoDto: any = {
					isCheering: 1, //応援か : SupportAtr
					isConfirmed: 0, //確定済みか 0:false or 1:true
					bounceAtr: 1, //直帰区分 0:false or 1:true
					directAtr: 1, //直行区分 0:false or 1:true
					isNeedWorkSchedule: 1,//勤務予定が必要か 0:false or 1:true
					employeeId: schedule,//社員ID
					date: self.dataFromA().startDate,//年月日
					//optional.empty;
					supportTimeZone: timeZoneDto,//応援時間帯
					wkpNameSupport: "SP",//応援先の職場名称
					listTimeVacationAndType: timeVacationAndTypes, //Map<時間休暇種類, 時間休暇>
					shiftCode: "shiftCode",//シフトコード
					shiftName: "shiftName",//シフト名称
					shortTime: shortTime
				}

				let breakTimeZoneDto: any = [];
				breakTimeZoneDto.push({
					breakFrameNo: 1,//休憩枠NO
					startTime: 705, //開始 - 勤怠打刻(実打刻付き)
					endTime: 765, //終了 - 勤怠打刻(実打刻付き)
					breakTime: 1050//休憩時間: 勤怠時間 

				});

				breakTimeZoneDto.push({
					breakFrameNo: 2,//休憩枠NO
					startTime: 1050, //開始 - 勤怠打刻(実打刻付き)
					endTime: 1110, //終了 - 勤怠打刻(実打刻付き)
					breakTime: 1050//休憩時間: 勤怠時間 

				});

				breakTimeZoneDto.push({
					breakFrameNo: 3,//休憩枠NO
					startTime: 1320, //開始 - 勤怠打刻(実打刻付き)
					endTime: 1380, //終了 - 勤怠打刻(実打刻付き)
					breakTime: 1050//休憩時間: 勤怠時間 

				});

				let breakTimeOfDailyAttdDto = [];
				breakTimeOfDailyAttdDto.push({
					breakType: 0,
					breakTimeSheets: breakTimeZoneDto
				});

				let employeeWorkScheduleDto: any = {
					startTime1: 480, //開始時刻１
					startTime1Status: 0, //開始時刻１編集状態 : EditStateSetting
					endTime1: 1250, //終了時刻１
					endTime1Status: 1, //終了時刻１編集状態 : EditStateSetting
					startTime2: 1300, //開始時刻2
					startTime2Status: 2, //開始時刻2編集状態 : EditStateSetting
					endTime2: 1600, //終了時刻2
					endTime2Status: 2, //終了時刻2編集状態 : EditStateSetting
					listBreakTimeZoneDto: breakTimeOfDailyAttdDto, //List<休憩時間帯> (EA : List＜休憩時間帯＞＝勤務予定．休憩時間帯) 
					workTypeCode: "010",//勤務種類コード
					breakTimeStatus: 2,//休憩時間帯編集状態 : EditStateSetting
					workTypeStatus: 0,//勤務種類編集状態 : EditStateSetting
					workTimeCode: "001",//就業時間帯コード
					workTimeStatus: 0//就業時間帯編集状態 : EditStateSetting
				};

				let timeZoneDto2: any = {
					startTime: { dayDivision: 0, time: 500 },//開始時刻 
					endTime: { dayDivision: 0, time: 560 }
				};
				let timeZoneDto3: any = {
					startTime: { dayDivision: 0, time: 900 },//開始時刻 
					endTime: { dayDivision: 0, time: 1100 }
				};
				let timeZoneDto4: any = {
					startTime: { dayDivision: 0, time: 1160 },//開始時刻 
					endTime: { dayDivision: 0, time: 1300 }
				};
				let timeZoneDto5: any = {
					startTime: { dayDivision: 0, time: 1400 },//開始時刻 
					endTime: { dayDivision: 0, time: 1600 }
				};

				let timeZoneDto6: any = {
					startTime: { dayDivision: 0, time: 360 },//開始時刻 
					endTime: { dayDivision: 0, time: 480 }
				};
				let timeZoneDto7: any = {
					startTime: { dayDivision: 0, time: 1110 },//開始時刻 
					endTime: { dayDivision: 0, time: 1200 }
				};
				let changeableWorkTime: any = [];
				changeableWorkTime.push({
					workNo: 1,//勤務NO
					changeableStartTime: timeZoneDto6,//開始時刻の変更可能な時間帯
					changeableEndTime: timeZoneDto7//終了時刻の変更可能な時間帯
				});

				let eixedWorkInforDto: any = {
					workTimeName: "TIMENAME", //就業時間帯名称
					coreStartTime: 540, //コア開始時刻
					coreEndTime: 600, //コア終了時刻
					overtimeHours: changeableWorkTime, //List<残業時間帯>
					startTimeRange1: timeZoneDto2, //日付開始時刻範囲時間帯1
					etartTimeRange1: timeZoneDto3,//日付終了時刻範囲時間帯1
					workTypeName: "TYPENAME",//勤務種類名称
					startTimeRange2: timeZoneDto4, //日付開始時刻範囲時間帯2
					etartTimeRange2: timeZoneDto5,//日付終了時刻範囲時間帯2
					fixBreakTime: 1, //休憩時間帯を固定にする (0:false 1:true) - 0 : không có brk time, 1 : có 
					workType: 0

				};

				displayWorkInfoByDateDto = {
					empId: schedule,
					workInfoDto: employeeWorkInfoDto,
					workScheduleDto: employeeWorkScheduleDto,
					fixedWorkInforDto: eixedWorkInforDto
				}
				self.dataScreen003A().employeeInfo.forEach((x: any, index) => {
					displayWorkInfo.forEach((y: any) => {
						if (x.empId === y.empId) {
							self.dataScreen003A().employeeInfo[index] = y;
						}
					})
				})
				displayWorkInfo.push(displayWorkInfoByDateDto);

			})
			self.dataScreen003A().employeeInfo[0] = displayWorkInfo[0];
			self.dataScreen003A().employeeInfo[0].empId = "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570";

			_.forEach(_.filter(self.dataScreen003A().employeeInfo, (x) => { return x.empId === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570"; }), (schedule: model.DisplayWorkInfoByDateDto) => {
				gcFixedWorkTime = [], gcBreakTime = [], gcOverTime = [], gcSupportTime = null,
					gcFlowTime = [], gcFlexTime = [], gcCoreTime = [], gcHolidayTime = [],
					gcShortTime = [], typeOfTime = "";
				let color = self.setColorEmployee(schedule.workInfoDto.isNeedWorkSchedule, schedule.workInfoDto.isCheering);
				if (color === '#ddddd2') {
					disableDs.push({
						empId: schedule.empId,
						color: color
					});
				}
				leftDs.push({
					empId: schedule.empId,
					color: color
				});

				let startTime1 = "", startTime2 = "", endTime1 = "", endTime2 = "";
				if (schedule.workScheduleDto != null) {
					startTime1 = schedule.workScheduleDto.startTime1 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.startTime1) : "",
						startTime2 = schedule.workScheduleDto.startTime2 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.startTime2) : "",
						endTime1 = schedule.workScheduleDto.endTime1 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.endTime1) : "",
						endTime2 = schedule.workScheduleDto.endTime2 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.endTime2) : "";
				}
				let totalTime: any = "", totalBreakTime: any = 0, totalBreakTime1: any = 0, totalBreakTime2: any = 0;
				let workTypeName = "", workTimeName = "";
				if (schedule.workScheduleDto != null) {
					totalTime = self.calcTotalTime(schedule.workScheduleDto);
					self.setColorWorkingInfo(schedule.empId, schedule.workInfoDto.isConfirmed, schedule.workScheduleDto);
					if (schedule.workScheduleDto.workTypeCode != null && schedule.fixedWorkInforDto != null && schedule.fixedWorkInforDto.workTypeName != null) {
						workTypeName = schedule.fixedWorkInforDto.workTypeName;
					} else {
						workTypeName = schedule.workScheduleDto.workTypeCode + getText('KSU003_54')
					}
					// set work time name
					if (schedule.workScheduleDto.workTimeCode == null) {
						workTimeName = getText('KSU003_55');
					} else {
						if (schedule.fixedWorkInforDto != null && schedule.fixedWorkInforDto.workTimeName != null) {
							workTimeName = schedule.fixedWorkInforDto.workTimeName;
						} else {
							workTimeName = schedule.workScheduleDto.workTimeCode + getText('KSU003_54');
						}
					}
				}

				// Push data of gant chart
				// Thời gian cố định
				// 勤務固定情報　dto．Optional<勤務タイプ>==固定勤務 && 社員勤務情報　dto．応援か＝＝時間帯応援元　or 応援ではない

				if (schedule.workInfoDto != null && schedule.workScheduleDto != null && schedule.fixedWorkInforDto != null) {
					if (schedule.fixedWorkInforDto.workType == WorkTimeForm.FIXED &&
						(schedule.workInfoDto.isCheering == SupportAtr.TIMEZONE_SUPPORTER || schedule.workInfoDto.isCheering == SupportAtr.NOT_CHEERING)) {
						// 社員勤務予定dto.Optional<開始時刻1>, 社員勤務予定dto.Optional<終了時刻1>
						gcFixedWorkTime.push({
							empId: schedule.workInfoDto.employeeId,
							timeNo: 1,
							color: "#ccccff",
							// 社員勤務情報　dto > 応援か
							isCheering: schedule.workInfoDto.isCheering,
							// 勤務固定情報　dto > Optional<勤務タイプ>
							workType: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.workType : null,
							// 社員勤務予定　dto > Optional<開始時刻1>, Optional<終了時刻1>
							startTime: schedule.workScheduleDto.startTime1,
							endTime: schedule.workScheduleDto.endTime1,
							// 勤務固定情報　dto > Optional<日付開始時刻範囲時間帯1>, Optional<日付終了時刻範囲時間帯1>
							startTimeRange: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.startTimeRange1 : null,
							etartTimeRange: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.etartTimeRange1 : null

						});
						startTimeArr.add(schedule.workScheduleDto.startTime1);
						// 複数回勤務管理.使用区別＝＝true
						// 社員勤務予定dto.Optional<開始時刻2>, 社員勤務予定dto.Optional<終了時刻2>
						if (self.dataScreen003A().targetInfor == 1) {
							gcFixedWorkTime.push({
								empId: schedule.workInfoDto.employeeId,
								timeNo: 2,
								color: "#ccccff",
								isCheering: schedule.workInfoDto.isCheering,
								workType: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.workType : null,

								startTime: schedule.workScheduleDto.startTime2,
								endTime: schedule.workScheduleDto.endTime2,
								startTimeRange: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.startTimeRange2 : null,
								etartTimeRange: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.etartTimeRange2 : null
							});
							startTimeArr.add(schedule.workScheduleDto.startTime2);
						}
						typeOfTime = "Fixed"
					}
				}
				if (schedule.fixedWorkInforDto != null && schedule.workScheduleDto != null) {
					// Thời gian break time
					// 勤務固定情報　dto.Optional<休憩時間帯を固定にする>＝falseの時に、休憩時間横棒が生成されない。 (defaut gcBreakTime = [])
					// 勤務固定情報　dto.Optional<休憩時間帯を固定にする>＝trueの時に、社員勤務予定 dto．Optional<List<休憩時間帯>>から休憩時間横棒を生成する。
					if (schedule.fixedWorkInforDto.fixBreakTime == 1) {

						gcBreakTime.push({
							// 社員勤務情報　dto > 社員ID
							empId: schedule.workInfoDto.employeeId,
							// 社員勤務予定　dto > Optional<List<休憩時間帯>>
							lstBreakTime: schedule.workScheduleDto.listBreakTimeZoneDto,
							color: "#ff9999",
							// 勤務固定情報　dto > Optional<休憩時間帯を固定にする>
							fixBreakTime: schedule.fixedWorkInforDto.fixBreakTime
						})
						schedule.workScheduleDto.listBreakTimeZoneDto.forEach(brk => {
							brk.breakTimeSheets.forEach(brkT => {
								let brkTChart = self.convertTimeToChart(brkT.startTime, brkT.endTime),
									timeChart = self.convertTimeToChart(schedule.workScheduleDto.startTime1, schedule.workScheduleDto.endTime1),
									brkTChart2, timeChart2;

								if (_.inRange(brkTChart.startTime, timeChart.startTime, timeChart.endTime) &&
									_.inRange(brkTChart.endTime, timeChart.startTime, timeChart.endTime)) {
									totalBreakTime1 += self.calcTotalBreakTime(brkTChart, timeChart);
								}

								if (schedule.workScheduleDto.startTime2 != null) {
									brkTChart2 = self.convertTimeToChart(brkT.startTime, brkT.endTime)
									timeChart2 = self.convertTimeToChart(schedule.workScheduleDto.startTime2, schedule.workScheduleDto.endTime2)
									if (_.inRange(brkTChart.startTime, timeChart2.startTime, timeChart2.endTime) &&
										_.inRange(brkTChart.endTime, timeChart2.startTime, timeChart2.endTime)) {
										totalBreakTime2 += self.calcTotalBreakTime(brkTChart2, timeChart2);
									}
								}
							})
							totalBreakTime = totalBreakTime2 + totalBreakTime1;
						})
					}

					// Thời gian làm thêm
					// 勤務固定情報 dto.Optional<List<残業時間帯>>から時間外労働時間横棒を生成する。
					gcOverTime.push({
						// 社員勤務情報　dto > 社員ID
						empId: schedule.workInfoDto.employeeId,
						// 勤務固定情報　dto > Optional<List<残業時間帯>>
						lstOverTime: schedule.fixedWorkInforDto.overtimeHours,
						color: "#ffff00"
					})

					// Thời gian lưu động 
					// 勤務固定情報　dto．Optional<勤務タイプ>== 流動勤務&&社員勤務情報　dto．応援か＝＝時間帯応援元　or 応援ではない
					if (schedule.fixedWorkInforDto.workType == WorkTimeForm.FLOW
						&& (schedule.workInfoDto.isCheering == SupportAtr.TIMEZONE_SUPPORTER || schedule.workInfoDto.isCheering == SupportAtr.NOT_CHEERING)) {
						// 社員勤務予定dto.Optional<開始時刻1>, 社員勤務予定dto.Optional<終了時刻1>
						gcFlowTime.push({
							timeNo: 1,
							// 社員勤務情報　dto > 社員ID
							empId: schedule.workInfoDto.employeeId,
							// 勤務固定情報　dto > Optional<勤務タイプ>
							workType: schedule.fixedWorkInforDto.workType,
							color: "#ffc000",
							// 社員勤務情報　dto > 応援か
							isCheering: schedule.workInfoDto.isCheering,

							// 社員勤務予定　dto > Optional<開始時刻1>, Optional<終了時刻1>
							startTime: schedule.workScheduleDto.startTime1,
							endTime: schedule.workScheduleDto.endTime1,

							// // 勤務固定情報　dto > Optional<日付開始時刻範囲時間帯1>, Optional<日付終了時刻範囲時間帯1>, 
							startTimeRange: schedule.fixedWorkInforDto.startTimeRange1,
							etartTimeRange: schedule.fixedWorkInforDto.etartTimeRange1

						});

						startTimeArr.add(schedule.workScheduleDto.startTime2);
						// 複数回勤務管理.使用区別＝＝true
						// 社員勤務予定dto.Optional<開始時刻2>, 社員勤務予定dto.Optional<終了時刻2>
						if (self.dataScreen003A().targetInfor == 1) {
							gcFlowTime.push({
								timeNo: 1,
								empId: schedule.workInfoDto.employeeId,
								workType: schedule.fixedWorkInforDto.workType,
								color: "#ffc000",
								isCheering: schedule.workInfoDto.isCheering,
								startTime: schedule.workScheduleDto.startTime2,
								endTime: schedule.workScheduleDto.endTime2,
								startTimeRange: schedule.fixedWorkInforDto.startTimeRange2,
								etartTimeRange: schedule.fixedWorkInforDto.etartTimeRange2
							});
							startTimeArr.add(schedule.workScheduleDto.startTime2);
						}
						typeOfTime = "Changeable"
					}

					// Thời gian Flex time
					// 勤務固定情報　dto．Optional<勤務タイプ>==フレックス勤務 && 社員勤務情報　dto．応援か＝＝時間帯応援元　or 応援ではない
					if (schedule.fixedWorkInforDto.workType == WorkTimeForm.FLEX
						&& (schedule.workInfoDto.isCheering == SupportAtr.TIMEZONE_SUPPORTER || schedule.workInfoDto.isCheering == SupportAtr.NOT_CHEERING)) {
						// 社員勤務予定dto.Optional<開始時刻1>, 社員勤務予定dto.Optional<終了時刻1>
						gcFlexTime.push({
							timeNo: 1,
							// 社員勤務情報　dto > 社員ID
							empId: schedule.workInfoDto.employeeId,
							// 勤務固定情報　dto > Optional<勤務タイプ>
							workType: schedule.fixedWorkInforDto.workType,
							color: "#ccccff",
							// 社員勤務情報　dto > 応援か
							isCheering: schedule.workInfoDto.isCheering,
							// 社員勤務予定　dto > Optional<開始時刻1>, Optional<終了時刻1>
							startTime: schedule.workScheduleDto.startTime1,
							endTime: schedule.workScheduleDto.endTime1,
							// // 勤務固定情報　dto > Optional<日付開始時刻範囲時間帯1>, Optional<日付終了時刻範囲時間帯1>
							startTimeRange: schedule.fixedWorkInforDto.startTimeRange1,
							etartTimeRange: schedule.fixedWorkInforDto.etartTimeRange1
						});
						startTimeArr.add(schedule.workScheduleDto.startTime1);
						typeOfTime = "Flex"
					}

					// Thời gian core time
					// 勤務固定情報　dto．Optional<勤務タイプ>== フレックス勤務 && 勤務固定情報　dto．Optional<コア開始時刻>とOptional<コア終了時刻>が存在する
					if (schedule.fixedWorkInforDto.workType == WorkTimeForm.FLEX
						&& schedule.fixedWorkInforDto.coreStartTime != null && schedule.fixedWorkInforDto.coreEndTime != null) {
						gcCoreTime.push({
							// 社員勤務情報　dto > 社員ID
							empId: schedule.workInfoDto.employeeId,
							color: "#00ffcc",
							// 勤務固定情報　dto > Optional<勤務タイプ>, Optional<コア開始時刻>, Optional<コア終了時刻>
							workType: schedule.fixedWorkInforDto.workType,
							coreStartTime: schedule.fixedWorkInforDto.coreStartTime,
							coreEndTime: schedule.fixedWorkInforDto.coreEndTime
						});
					}

					// Thời gian holiday time
					// 社員勤務情報　dto．Optional<Map<時間休暇種類, 時間休暇>> 存在する
					if (schedule.workInfoDto.listTimeVacationAndType.length > 0) {
						gcHolidayTime.push({
							// 社員勤務情報　dto > 社員ID, Optional<Map<時間休暇種類, 時間休暇>>
							empId: schedule.workInfoDto.employeeId,
							color: "#c4bd97",
							listTimeVacationAndType: schedule.workInfoDto.listTimeVacationAndType
						})
					}

					// Thời gian chăm sóc / giữ trẻ (short time)
					// 社員勤務情報　dto．Optional<育児介護短時間> が存在する
					if (schedule.workInfoDto.shortTime.length > 0) {
						gcShortTime.push({
							// 社員勤務情報　dto > 社員ID, Optional<Map<時間休暇種類, 時間休暇>>
							empId: schedule.workInfoDto.employeeId,
							color: "#6fa527",
							listShortTime: schedule.workInfoDto.shortTime
						})
					}
				}
				self.timeBrkMinu(totalBreakTime);
				totalBreakTime = formatById("Clock_Short_HM", (totalBreakTime * 5));
				self.timeBrkText(totalBreakTime);
				if (schedule.workScheduleDto != null) {
					middleDs.push({
						empId: schedule.empId, cert: getText('KSU003_22'), worktypeCode: schedule.workScheduleDto.workTypeCode,
						worktype: workTypeName, worktimeCode: schedule.workScheduleDto.workTimeCode, worktime: workTimeName,
						startTime1: startTime1, endTime1: endTime1,
						startTime2: startTime2, endTime2: endTime2,
						totalTime: totalTime, breaktime: self.timeBrkText(), color: self.dataColorA6
					});
				} else {
					middleDs.push({
						empId: schedule.empId, cert: getText('KSU003_22'), worktypeCode: "",
						worktype: "", worktimeCode: "", worktime: "",
						startTime1: "", endTime1: "",
						startTime2: "", endTime2: "",
						totalTime: "", breaktime: "", color: ""
					});
				}

				timeGantChart.push({
					empId: schedule.empId,
					typeOfTime: typeOfTime,
					gantCharts: self.dataScreen003A().targetInfor,
					gcFixedWorkTime: gcFixedWorkTime,
					gcBreakTime: gcBreakTime,
					gcOverTime: gcOverTime,
					gcSupportTime: gcSupportTime,
					gcFlowTime: gcFlowTime,
					gcFlexTime: gcFlexTime,
					gcCoreTime: gcCoreTime,
					gcHolidayTime: gcHolidayTime,
					gcShortTime: gcShortTime
				});
			});
			self.leftDs = leftDs;
			self.disableDs = disableDs;
			self.dataOfGantChart = timeGantChart;
			self.midDataGC = middleDs;
			startTimeArr = _.sortBy(startTimeArr, [function(o: any) { return o; }]);
			self.startScrollY = startTimeArr[0];
			self.initExtableData(timeGantChart, leftDs, middleDs, disableDs);
		}

		public nextDay() {
			let self = this;
			self.changeTargetDate(1, 1);
			self.hoverEvent(self.targetDate());
			$("#extable-ksu003").children().remove();
			$("#extable-ksu003").removeData();
			self.getWorkingByDate(self.targetDate(), 1);
		}

		public nextAllDay() {
			let self = this;
			self.changeTargetDate(1, 7);
			self.hoverEvent(self.targetDate());
			$("#extable-ksu003").children().remove();
			$("#extable-ksu003").removeData();
			self.getWorkingByDate(self.targetDate(), 1);
		}

		public prevDay() {
			let self = this;
			self.changeTargetDate(0, 1);
			self.hoverEvent(self.targetDate());
			$("#extable-ksu003").children().remove();
			$("#extable-ksu003").removeData();
			self.getWorkingByDate(self.targetDate(), 1);
		}

		public prevAllDay() {
			let self = this;
			self.changeTargetDate(0, 7);
			self.hoverEvent(self.targetDate());
			$("#extable-ksu003").children().remove();
			$("#extable-ksu003").removeData();
			self.getWorkingByDate(self.targetDate(), 1);
		}

		public changeTargetDate(nextOrprev: number, index: number) {
			let self = this;
			if (nextOrprev === 1) {
				let time: any = moment(moment(self.targetDate()).add(index, 'd').format('YYYY/MM/DD'));
				self.targetDate(time._i);
			} else {
				let time: any = moment(moment(self.targetDate()).subtract(index, 'd').format('YYYY/MM/DD'));
				self.targetDate(time._i);
			}
			self.targetDateDay(self.targetDate() + moment(self.targetDate()).format('(ddd)'));
		}
		/** Tính tổng thời gian làm việc */
		public calcTotalBreakTime(brkTChart: any, timeChart: any) {
			let totalBreak = 0;
			if (brkTChart.startTime <= timeChart.startTime && brkTChart.endTime <= timeChart.endTime)
				totalBreak = brkTChart.endTime - timeChart.startTime;

			if (brkTChart.startTime <= timeChart.startTime && brkTChart.endTime >= timeChart.endTime)
				totalBreak = timeChart.endTime - timeChart.startTime;

			if (brkTChart.startTime >= timeChart.startTime && brkTChart.endTime >= timeChart.endTime)
				totalBreak = timeChart.endTime - brkTChart.startTime;

			if (brkTChart.startTime >= timeChart.startTime && brkTChart.endTime <= timeChart.endTime)
				totalBreak = brkTChart.endTime - brkTChart.startTime;

			return totalBreak;
		}

		public getInforFirt(): JQueryPromise<any> {
			let self = this;
			let dfd = $.Deferred<any>();
			let targetOrgDto = {
				unit: self.dataFromA().unit,
				workplaceId: self.dataFromA().workplaceId,
				workplaceGroupId: self.dataFromA().workplaceGroupId
			}
			// ①<<ScreenQuery>> 初期起動の情報取得
			service.getDataStartScreen(targetOrgDto)
				.done((data: model.GetInfoInitStartKsu003Dto) => {
					self.dataInitStartKsu003Dto(data);
					self.organizationName(self.dataInitStartKsu003Dto().displayInforOrganization.displayName);
					self.dataScreen003A().targetInfor = data.manageMultiDto.useATR;
					self.timeRange = self.dataInitStartKsu003Dto().byDateDto.dispRange == 0 ? 24 : 48;
					self.dataScreen003A().targetInfor = self.dataInitStartKsu003Dto().manageMultiDto.useATR;
					self.dataScreen003A().scheCorrection = self.dataInitStartKsu003Dto().functionControlDto != null ? self.dataInitStartKsu003Dto().functionControlDto.changeableWorks : [0, 1, 2, 3];
				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
				}).always(function() {
				});
			return dfd.promise();
		}

		// ②<<ScreenQuery>> 日付別勤務情報で表示する
		public getWorkingByDate(targetDate: any, check: number): JQueryPromise<any> {
			let self = this;
			let dfd = $.Deferred<any>();
			block.grayout();
			let targetOrg = {
				unit: self.dataFromA().unit,
				workplaceId: self.dataFromA().workplaceId != null ? self.dataFromA().workplaceId : null,
				workplaceGroupId: self.dataFromA().workplaceGroupId != null ? self.dataFromA().workplaceGroupId : null
			}
			let lstEmpId = _.flatMap(self.dataFromA().listEmp, c => [c.id]);
			let param = {
				targetOrg: targetOrg,
				lstEmpId: lstEmpId,
				date: targetDate
			};
			service.displayDataKsu003(param)
				.done((data: Array<model.DisplayWorkInfoByDateDto>) => {
					self.dataScreen003A().employeeInfo = data;

					self.employeeScheduleInfo = _.map(self.dataScreen003A().employeeInfo, (x) => ({
						empId: x.empId,
						startTime1: x.workScheduleDto != null && x.workScheduleDto.startTime1 != null ? x.workScheduleDto.startTime1 : "",
						endTime1: x.workScheduleDto != null && x.workScheduleDto.endTime1 != null ? x.workScheduleDto.endTime1 : "",
						startTime2: x.workScheduleDto != null && x.workScheduleDto.startTime2 != null ? x.workScheduleDto.startTime2 : "",
						endTime2: x.workScheduleDto != null && x.workScheduleDto.endTime2 != null ? x.workScheduleDto.endTime2 : "",
						listBreakTimeZoneDto: x.workScheduleDto != null && x.workScheduleDto.listBreakTimeZoneDto != null ? x.workScheduleDto.listBreakTimeZoneDto : ""
					}))

					self.employeeScheduleInfo = self.employeeScheduleInfo.sort(function(a, b) {
						return _.findIndex(self.dataFromA().listEmp, x => { return x.id == a.empId }) - _.findIndex(self.dataFromA().listEmp, x => { return x.id == b.empId });
					});

				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
				}).always(function() {
					if (check === 1)
						self.convertDataIntoExtable();
					block.clear();
				});

			return dfd.promise();
		}

		public initExtableData(dataBindChart: Array<ITimeGantChart>, leftDs: any, middleDs: any, disableDs: any) {
			let self = this;
			let local: ILocalStore = {
				startTimeSort: self.checked(),
				showWplName: self.checkedName() == true,
				operationUnit: self.selectOperationUnit(),
				displayFormat: self.selectedDisplayPeriod(),
				showHide: self.indexBtnToLeft(),
				lstSortByTime: self.lstEmpIdByTime,
				lstSortByEmp: self.lstEmpIdByEmp
			}
			setTimeout(() => {
				self.localStore(local);
				storage.getItem(self.KEY).ifPresent((data: any) => {
					let userInfor: ILocalStore = JSON.parse(data);
					self.indexBtnToLeft(userInfor.showHide);
					self.selectOperationUnit(userInfor.operationUnit);
					self.selectedDisplayPeriod(userInfor.displayFormat);
					self.checked(userInfor.startTimeSort);
					self.checkedName(userInfor.showWplName);
				});
				self.initExTable(dataBindChart, leftDs, middleDs, disableDs);
				self.showHide();
				if (!_.isNil(self.startScrollY)) {
					self.startScrollY = formatById("Clock_Short_HM", self.startScrollY);
					let time = self.convertTimePixel(self.startScrollY);
					self.startScrollGc(time * self.operationUnit() - 42);
				}
				$("#extable-ksu003").exTable("scrollBack", 0, { h: self.startScrollGc() });
				self.dataA6 = null;
			}, 200)
		}

		// 社員勤務予定と勤務固定情報を取得する
		public getEmpWorkFixedWorkInfo(workTypeCode: string, workTimeCode: string): JQueryPromise<any> {
			let self = this;
			let dfd = $.Deferred<any>();
			let targetOrgDto = {
				workType: workTypeCode,
				workTime: workTimeCode
			}
			service.getEmpWorkFixedWorkInfo(targetOrgDto)
				.done((data: any) => {
				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
				}).always(function() {
				});
			return dfd.promise();
		}

		// 勤務種類を変更する
		public changeWorkType(): JQueryPromise<any> {
			let self = this;
			let dfd = $.Deferred<any>();
			let targetOrgDto = {
				workType: self.dataA6().workTypeCode,
				workTime: self.dataA6().workTimeCode
			}
			service.changeWorkType(targetOrgDto)
				.done((data: any) => {
				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
				}).always(function() {
				});
			return dfd.promise();
		}

		// 社員を並び替える
		public sortEmployee(value: any) {
			let self = this;
			let param = {
				ymd: self.targetDate(),
				lstEmpId: self.lstEmpId
			}
			let dataSort = _.map(self.dataScreen003A().employeeInfo, (x: model.DisplayWorkInfoByDateDto) => ({
				empId: x.empId,
				startTime: x.workScheduleDto != null ? x.workScheduleDto.startTime1 : 0
			}))
			if (value === '0') {
				dataSort = dataSort.sort(function(a, b) { return b.startTime - a.startTime });
				self.lstEmpIdByTime = _.map(dataSort, (x: any) => { return x.empId });
				self.localStore().lstSortByTime = self.lstEmpIdByTime;
				storage.setItemAsJson(self.KEY, self.localStore());
				self.destroyAndCreateGrid(self.lstEmpIdByTime);
			} else {
				service.sortEmployee(param)
					.done((data: Array<model.DisplayWorkInfoByDateDto>) => {
						self.lstEmpIdByEmp = data;
						self.localStore().lstSortByEmp = self.lstEmpIdByEmp;
						storage.setItemAsJson(self.KEY, self.localStore());
						self.destroyAndCreateGrid(self.lstEmpIdByEmp);
					}).fail(function(error) {
						errorDialog({ messageId: error.messageId });
					}).always(function() {
					});
			}
			self.localStore().startTimeSort = value;
			storage.setItemAsJson(self.KEY, self.localStore());
		}

		destroyAndCreateGrid(lstId: any) {
			let self = this;
			let leftDs: any = [];
			leftDs.push({
				empId: lstId,
				color: ""
			});
			$("#extable-ksu003").children().remove();
			$("#extable-ksu003").removeData();
			self.getWorkingByDate(self.targetDate(), 1)
		}

		// open dialog kdl045
		// cần làm lại sau
		public openKdl045Dialog() {
			let self = this;
			block.grayout();

			let data: any = _.filter(self.dataScreen003A().employeeInfo, (x) => { return x.empId === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570"; });
			let lineNo = _.findIndex(self.dataScreen003A().employeeInfo, (x) => { return x.empId === data[0].empId; });
			let dataShare: model.ParamKsu003 = {
				employeeInfo: data[0],
				targetInfor: self.dataScreen003A().targetInfor,
				canModified: self.dataScreen003A().canModified,
				scheCorrection: self.dataScreen003A().scheCorrection,
				unit: self.dataFromA().unit,
				targetId: self.dataFromA().unit === 0 ? self.dataFromA().workplaceId : self.dataFromA().workplaceGroupId,
				workplaceName: self.dataFromA().workplaceName
			};
			setShared('dataShareTo045', dataShare);
			nts.uk.ui.windows.sub.modal('/view/kdl/045/a/index.xhtml').onClosed(() => {
				self.dataScreen045A(getShared('dataFromKdl045'));
				let lstBrkTime = self.dataScreen045A().workScheduleDto.listBreakTimeZoneDto, totalBrkTime = "";
				let lstBreak: any = lstBrkTime;
				let totalTimebr: any = null;
				lstBrkTime.forEach((x: any) => {
					totalTimebr += x.endTime - x.startTime;
				})

				totalBrkTime = totalTimebr != null ? formatById("Clock_Short_HM", totalTimebr) : ""

				let totalTime = self.calcTotalTime(self.dataScreen045A().workScheduleDto);
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "worktypeCode", self.dataScreen045A().workScheduleDto.workTypeCode);
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "worktype", self.dataScreen045A().fixedWorkInforDto.workTypeName);
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "worktimeCode", self.dataScreen045A().workScheduleDto.workTimeCode);
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "worktime", self.dataScreen045A().fixedWorkInforDto.workTimeName);
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "startTime1", formatById("Clock_Short_HM", (self.dataScreen045A().workScheduleDto.startTime1)));
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "endTime1", formatById("Clock_Short_HM", (self.dataScreen045A().workScheduleDto.endTime1)));
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "startTime2", formatById("Clock_Short_HM", (self.dataScreen045A().workScheduleDto.startTime2)));
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "endTime2", formatById("Clock_Short_HM", (self.dataScreen045A().workScheduleDto.endTime2)));
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "totalTime", totalTime);
				$("#extable-ksu003").exTable("cellValue", "middle", data[0].empId, "breaktime", totalBrkTime);
				let datafilter: Array<ITimeGantChart> = _.filter(self.dataOfGantChart, (x: any) => { return x.empId === data[0].empId }), fixedGc: any = [], indexS = 0, indexF = 0;;
				if (datafilter.length > 0) {
					self.updateGantChart(datafilter, lineNo, fixedGc, lstBreak, indexS, indexF);
				}

				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.workTypeCode = self.dataScreen045A().workScheduleDto.workTypeCode;
				self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto.workTypeName = self.dataScreen045A().fixedWorkInforDto.workTypeName;
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.workTimeCode = self.dataScreen045A().workScheduleDto.workTimeCode;
				self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto.workTimeName = self.dataScreen045A().fixedWorkInforDto.workTimeName;
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.startTime1 = self.dataScreen045A().workScheduleDto.startTime1;
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.endTime1 = self.dataScreen045A().workScheduleDto.endTime1;
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.startTime2 = self.dataScreen045A().workScheduleDto.startTime2;
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.endTime2 = self.dataScreen045A().workScheduleDto.endTime2;
				self.checkGetInfo = true;
				block.clear();
			});
		}

		public updateGantChart(datafilter: Array<ITimeGantChart>, lineNo: any, fixedGc: any, lstBreak: any, indexS: number, indexF: number) {
			let self = this;
			if (datafilter[0].typeOfTime != "") {
				let timeChart = null, timeChart2 = null
				if (datafilter[0].typeOfTime === "Fixed") {
					let fixed = datafilter[0].gcFixedWorkTime;
					timeChart = self.convertTimeToChart(fixed[0].startTime, fixed[0].endTime);

					fixedGc.push(self.addChartWithType045(ruler, "Fixed", `lgc${lineNo}`, timeChart, lineNo, null, 0, 999, 0, 999));

					if (fixed.length > 1 && fixed[1].startTime != null && fixed[1].endTime != null) {
						timeChart2 = self.convertTimeToChart(fixed[1].startTime, fixed[1].endTime);
						//lgc = self.addChartWithTypes(ruler, "Fixed", `lgc${i}`, timeChart, i);
						fixedGc.push(self.addChartWithType045(ruler, "Fixed", `rgc${lineNo}`, timeChart2, lineNo, null, 0, 999, 0, 999));
						indexS = indexS++;
					}
				}

				if (datafilter[0].typeOfTime === "Changeable") {
					let changeable = datafilter[0].gcFlowTime
					timeChart = self.convertTimeToChart(changeable[0].startTime, changeable[0].endTime);

					fixedGc.push(self.addChartWithType045(ruler, "Changeable", `lgc${lineNo}`, timeChart, lineNo, 0, 999, 0, 999));

					if (changeable.length > 1 && changeable[1].startTime != null && changeable[1].endTime != null) {
						timeChart2 = self.convertTimeToChart(changeable[1].startTime, changeable[1].endTime);
						fixedGc.push(self.addChartWithType045(ruler, "Changeable", `rgc${lineNo}`, timeChart2, lineNo, 0, 999, 0, 999));
						indexS = indexS++;
					}
				}

				if (datafilter[0].typeOfTime === "Flex") {
					let flex = datafilter[0].gcFlexTime
					timeChart = self.convertTimeToChart(flex[0].startTime, flex[0].endTime);

					fixedGc.push(self.addChartWithType045(ruler, "Flex", `lgc${lineNo}`, timeChart, lineNo));

					let coreTime = datafilter[0].gcCoreTime;
					if (coreTime.length > 0) {
						timeChart2 = self.convertTimeToChart(coreTime[0].coreStartTime, coreTime[0].coreEndTime);
						fixedGc.push(self.addChartWithType045(ruler, "CoreTime", `lgc${lineNo}_` + indexF, timeChart2, lineNo, `lgc${lineNo}`));

						indexF = ++indexF;
					}
				}
			}

			/*let suportTime = datafilter[0].gcSupportTime;
			if (suportTime != null) {
				timeChart = self.convertTimeToChart(suportTime[0].coreStartTime, suportTime[0].coreEndTime);
				fixedGc.push(self.addChartWithType045(ruler, "SupportTime", `lgc${i}_` + indexF, timeChart, i, `lgc${i}`))

				indexF = ++indexF;
			}*/

			let overTime = datafilter[0].gcOverTime;
			if (overTime.length > 0) {
				overTime[0].lstOverTime.forEach(x => {
					let timeChartOver = self.convertTimeToChart(x.changeableStartTime.startTime.time, x.changeableStartTime.endTime.time),
						id = `lgc${lineNo}_` + indexF, parent = `lgc${lineNo}`;
					if (datafilter[0].gantCharts === 1) {
						if (x.changeableStartTime.startTime.time >= datafilter[0].gcFixedWorkTime[1].startTime &&
							x.changeableStartTime.endTime.time <= datafilter[0].gcFixedWorkTime[1].endTime) {
							id = `rgc${lineNo}_` + indexF, parent = `rgc${lineNo}`;
						}
					}
					fixedGc.push(self.addChartWithType045(ruler, "OT", id, timeChartOver, lineNo, parent, 0, 999, 0, 999, 1000));
					indexF = ++indexF;
					id = `lgc${lineNo}_` + indexF;
					if (x.changeableStartTime.startTime.time >= datafilter[0].gcFixedWorkTime[1].startTime &&
						x.changeableStartTime.endTime.time <= datafilter[0].gcFixedWorkTime[1].endTime) {
						id = `rgc${lineNo}_` + indexF;
					}
					timeChartOver = self.convertTimeToChart(x.changeableEndTime.startTime.time, x.changeableEndTime.endTime.time);
					fixedGc.push(self.addChartWithType045(ruler, "OT", id, timeChartOver, lineNo, parent, 0, 999, 0, 999, 1000));
					indexF = ++indexF;
				})
			}

			datafilter[0].gcBreakTime[0].lstBreakTime[0].breakTimeSheets = lstBreak;
			if (lstBreak.length > 0) {
				lstBreak.forEach((y: any) => {
					let timeChartBrk = self.convertTimeToChart(y.startTime, y.endTime),
						id = `lgc${lineNo}_` + indexF, parent = `lgc${lineNo}`;
					if (datafilter[0].gantCharts === 1) {
						if (y.startTime >= datafilter[0].gcFixedWorkTime[1].startTime &&
							y.endTime <= datafilter[0].gcFixedWorkTime[1].endTime) {
							id = `rgc${lineNo}_` + indexF, parent = `rgc${lineNo}`;
						}
					}
					fixedGc.push(self.addChartWithType045(ruler, "BreakTime", id, timeChartBrk, lineNo, parent, 0, 999, 0, 999, 1001));
					indexF = ++indexF;
				})
			} else {
				$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "breaktime", "0:00");
			}

			if (datafilter[0].gcShortTime.length > 0) {
				datafilter[0].gcShortTime[0].listShortTime.forEach(x => {
					let timeChartShort = self.convertTimeToChart(x.startTime.time, x.endTime.time),
						id = `lgc${lineNo}_` + indexF, parent = `lgc${lineNo}`;
					if (x.startTime.time >= datafilter[0].gcFixedWorkTime[1].startTime &&
						x.endTime.time <= datafilter[0].gcFixedWorkTime[1].endTime) {
						id = `rgc${lineNo}_` + indexF, parent = `rgc${lineNo}`;
					}

					fixedGc.push(self.addChartWithType045(ruler, "ShortTime", id, timeChartShort, lineNo, parent, 0, 999, 0, 999, 1002));
					indexF = ++indexF;
				})
			}

			if (datafilter[0].gcHolidayTime.length > 0) {
				datafilter[0].gcHolidayTime[0].listTimeVacationAndType.forEach(x => {
					x.timeVacation.timeZone.forEach(hld => {
						let timeChartHoliday = self.convertTimeToChart(hld.startTime.time, hld.endTime.time),
							id = `lgc${lineNo}_` + indexF, parent = `lgc${lineNo}`;

						if (hld.startTime.time >= datafilter[0].gcFixedWorkTime[1].startTime &&
							hld.endTime.time <= datafilter[0].gcFixedWorkTime[1].endTime) {
							id = `rgc${lineNo}_` + indexF, parent = `rgc${lineNo}`;
						}

						fixedGc.push(self.addChartWithType045(ruler, "HolidayTime", id, timeChartHoliday, lineNo, parent, 0, 999, 0, 999, 1003));
						indexF = ++indexF;
					})
				})
			}

			ruler.replaceAt(lineNo, [
				...fixedGc
			]);
		}

		public calcTotalTime(workScheduleDto: model.EmployeeWorkScheduleDto, ) {
			let totalTime: any = null;
			if (workScheduleDto.endTime2 != null && workScheduleDto.startTime2 != null)
				totalTime = (workScheduleDto.endTime1 - workScheduleDto.startTime1) + (workScheduleDto.endTime2 - workScheduleDto.startTime2);
			else if (workScheduleDto.endTime1 != null && workScheduleDto.startTime1 != null)
				totalTime = (workScheduleDto.endTime1 - workScheduleDto.startTime1);

			totalTime = totalTime != "" ? formatById("Clock_Short_HM", totalTime) : "";
			return totalTime;
		}

		/** A1_3 - Open dialog G */
		public openGDialog() {
			let self = this;
			block.grayout();
			let dataShare = {
				employeeIDs: self.lstEmpId,
				startDate: self.targetDate(),
				endDate: self.targetDate(),
				employeeInfo: self.dataFromA().listEmp
			};
			setShared('dataShareDialogG', dataShare);
			nts.uk.ui.windows.sub.modal('/view/ksu/001/g/index.xhtml').onClosed(() => {
				block.clear();
			});
		}

		public openKdl003Dialog() {
			let self = this;
			block.grayout();
			let dataShare = {
				workTypeCode: "009",
				workTimeCode: "012"
			};
			setShared('parentCodes', dataShare, true);
			nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(() => {
				let dataShare = getShared('childData');
				// 勤務固定情報を取得する
				service.getFixedWorkInformation(dataShare).done((data: any) => {
					self.getFixedWorkInformationDto(data);
				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
				}).always(function() {
				});
				block.clear();
			});
		}

		/** A1_4 - Close modal */
		public closeDialog(): void {
			nts.uk.ui.windows.close();
		}

		/*A4_color*/
		public setColorEmployee(needSchedule: number, cheering: number) {
			if (needSchedule == 0) {
				return '#ddddd2';
			}

			switch (cheering) {
				case SupportAtr.ALL_DAY_SUPPORTER:
					return '#c3d69b';

				case SupportAtr.ALL_DAY_RESPONDENT:
					return '#fedfe6';

				case SupportAtr.TIMEZONE_SUPPORTER:
					return '#ebf1de';

				case SupportAtr.TIMEZONE_RESPONDENT:
					return '#ffccff';
			}
		}

		/** A6_color① - chua xong o phan login va xin bang don */
		public setColorWorkingInfo(empId: string, isConfirmed: number, workScheduleDto: any) {
			let self = this, workTypeColor = "", workTimeColor = "", startTime1Color = "", endTime1Color = "",
				startTime2Color = "", endTime2Color = "", breakTimeColor = "";

			if (isConfirmed == 0) {
				self.workingInfoColor('#ddddd2');
			} else {
				self.workingInfoColor('#eccefb');
			}
			// work type
			if (workScheduleDto.workTypeStatus != null) {
				if (workScheduleDto.workTypeStatus === EditStateSetting.HAND_CORRECTION_MYSELF) {
					workTypeColor = "94b7fe"
				} else if (workScheduleDto.workTypeStatus === EditStateSetting.HAND_CORRECTION_OTHER) {
					workTypeColor = "cee6ff"
				} else if (workScheduleDto.workTypeStatus === EditStateSetting.REFLECT_APPLICATION) {
					workTypeColor = "bfea60"
				}
			}

			// work time
			if (workScheduleDto.workTimeStatus != null) {
				if (workScheduleDto.workTimeStatus === EditStateSetting.HAND_CORRECTION_MYSELF) {
					workTimeColor = "94b7fe"
				} else if (workScheduleDto.workTimeStatus === EditStateSetting.HAND_CORRECTION_OTHER) {
					workTimeColor = "cee6ff"
				} else if (workScheduleDto.workTimeStatus === EditStateSetting.REFLECT_APPLICATION) {
					workTimeColor = "bfea60"
				}
			}

			// start time 1
			if (workScheduleDto.startTime1Status != null) {
				if (workScheduleDto.startTime1Status === EditStateSetting.HAND_CORRECTION_MYSELF) {
					startTime1Color = "94b7fe"
				} else if (workScheduleDto.startTime1Status === EditStateSetting.HAND_CORRECTION_OTHER) {
					startTime1Color = "cee6ff"
				} else if (workScheduleDto.startTime1Status === EditStateSetting.REFLECT_APPLICATION) {
					startTime1Color = "bfea60"
				}
			}

			// end time 1
			if (workScheduleDto.endTime1Status != null) {
				if (workScheduleDto.endTime1Status === EditStateSetting.HAND_CORRECTION_MYSELF) {
					endTime1Color = "94b7fe"
				} else if (workScheduleDto.endTime1Status === EditStateSetting.HAND_CORRECTION_OTHER) {
					endTime1Color = "cee6ff"
				} else if (workScheduleDto.endTime1Status === EditStateSetting.REFLECT_APPLICATION) {
					endTime1Color = "bfea60"
				}
			}

			// start time 2
			if (workScheduleDto.startTime2Status != null) {
				if (workScheduleDto.startTime2Status === EditStateSetting.HAND_CORRECTION_MYSELF) {
					startTime2Color = "94b7fe"
				} else if (workScheduleDto.startTime2Status === EditStateSetting.HAND_CORRECTION_OTHER) {
					startTime2Color = "cee6ff"
				} else if (workScheduleDto.startTime2Status === EditStateSetting.REFLECT_APPLICATION) {
					startTime2Color = "bfea60"
				}
			}

			// end time 2
			if (workScheduleDto.endTime2Status != null) {
				if (workScheduleDto.endTime2Status === EditStateSetting.HAND_CORRECTION_MYSELF) {
					endTime2Color = "94b7fe"
				} else if (workScheduleDto.endTime2Status === EditStateSetting.HAND_CORRECTION_OTHER) {
					endTime2Color = "cee6ff"
				} else if (workScheduleDto.endTime2Status === EditStateSetting.REFLECT_APPLICATION) {
					endTime2Color = "bfea60"
				}
			}

			// break time
			if (workScheduleDto.breakTimeStatus != null) {
				if (workScheduleDto.breakTimeStatus === EditStateSetting.HAND_CORRECTION_MYSELF) {
					breakTimeColor = "94b7fe"
				} else if (workScheduleDto.breakTimeStatus === EditStateSetting.HAND_CORRECTION_OTHER) {
					breakTimeColor = "cee6ff"
				}
			}


			self.dataColorA6.push({
				empId: empId,
				workTypeColor: workTypeColor,
				workTimeColor: workTimeColor,
				startTime1Color: startTime1Color,
				endTime1Color: endTime1Color,
				startTime2Color: startTime2Color,
				endTime2Color: endTime2Color,
				breakTimeColor: breakTimeColor

			});
		}

		// A7_color①
		public a7SetColor(empId: string, isConfirmed: number, workScheduleDto: any) {
			let self = this, breakTimeColor = "";

			if (isConfirmed == 0) {
				self.totalTimeColor('#ddddd2');
			} else {
				self.totalTimeColor('#eccefb');
			}

			// break time
			if (workScheduleDto.breakTimeStatus != null) {
				if (workScheduleDto.breakTimeStatus === EditStateSetting.HAND_CORRECTION_MYSELF) {
					breakTimeColor = "94b7fe"
				} else if (workScheduleDto.breakTimeStatus === EditStateSetting.HAND_CORRECTION_OTHER) {
					breakTimeColor = "cee6ff"
				}
			}
			return breakTimeColor;
		}

		public convertTimePixel(timeStart: any) {
			let time = 0;
			if (timeStart != null) {
				// convert string to pixel
				time = Math.round(duration.parseString(timeStart).toValue() / 5);
			}
			return time;
		}

		initExTable(timeGantChart: Array<ITimeGantChart>, leftDs: any, midData: any, disableDs: any): void {
			let self = this;
			let displayRange = self.dataInitStartKsu003Dto().byDateDto.dispRange == 0 ? 24 : 48, totalBreakTime = "0:00";
			if (leftDs[0].empId === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570") {

				let middleContentDeco: any = [], leftContentDeco: any = [], detailContentDeco: any = [];

				// phần leftMost
				let leftmostColumns = [],
					leftmostHeader = {},
					leftmostContent = {}, disableDSFilter = [];

				leftmostColumns = [{
					key: "empName",
					icon: { for: "body", class: "icon-leftmost", width: "25px" },
					headerText: getText('KSU003_20'), width: "160px", control: "link",
					css: { whiteSpace: "pre" }
				}, { key: "cert", headerText: getText('KSU003_21'), width: "40px", control: "button", handler: function() { self.openKdl045Dialog() } }];

				leftmostHeader = {
					columns: leftmostColumns,
					rowHeight: "33px",
					width: "200px"
				};

				let leftmostDs = [], middleDs = [];
				let i = 0;
				//for (let i = 0; i < self.dataFromA().listEmp.length; i++) {
				if (leftDs[0].empId === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570" &&
					self.dataFromA().listEmp[i].id === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570") {
					let dataLeft = self.dataFromA().listEmp[i],
						datafilter = _.filter(midData, (x: any) => { return x.empId === self.dataFromA().listEmp[i].id }),
						dataMid = datafilter[0],
						eName: any = dataLeft.code + " " + dataLeft.name;
					totalBreakTime = dataMid.breaktime;
					let leftDSFilter = _.filter(leftDs, (x: any) => { return x.empId === self.dataFromA().listEmp[i].id });
					disableDSFilter = _.filter(disableDs, (x: any) => { return x.empId === self.dataFromA().listEmp[i].id });

					leftmostDs.push({ empId: self.dataFromA().listEmp[i].id, empName: eName, cert: getText('KSU003_22') });

					leftContentDeco.push(new CellColor("empName", leftDSFilter[0].empId, leftDSFilter[0].color));
					leftContentDeco.push(new CellColor("cert", leftDSFilter[0].empId, leftDSFilter[0].color));
					if (disableDSFilter.length > 0) {
						middleContentDeco.push(new CellColor("worktypeCode", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("worktype", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("worktimeCode", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("worktime", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("startTime1", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("endTime1", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("startTime2", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("endTime2", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("totalTime", disableDSFilter[0].empId, disableDSFilter[0].color));
						middleContentDeco.push(new CellColor("breaktime", disableDSFilter[0].empId, disableDSFilter[0].color));
						for (let z = 0; z <= displayRange; z++) {
							detailContentDeco.push(new CellColor(z.toString(), disableDSFilter[0].empId, disableDSFilter[0].color));
						}

					}

					middleDs.push({
						empId: dataMid.empId, worktypeCode: dataMid.worktypeCode,
						worktype: dataMid.worktype, worktimeCode: dataMid.worktimeCode, worktime: dataMid.worktime,
						startTime1: dataMid.startTime1, endTime1: dataMid.endTime1,
						startTime2: dataMid.startTime2, endTime2: dataMid.endTime2,
						totalTime: dataMid.totalTime, breaktime: totalBreakTime
					});
				}
				//}

				leftmostContent = {
					columns: leftmostColumns,
					dataSource: leftmostDs,
					primaryKey: "empId",
					features: [{
						name: "BodyCellStyle",
						decorator: leftContentDeco
					}]
				};

				// Phần middle
				let middleColumns = [], middleHeader = {}, middleContent = {};
				middleColumns = [
					{
						headerText: getText('KSU003_23'), group: [
							{ headerText: "", key: "worktypeCode", width: "40px", handlerType: "input", dataType: "text" },
							{ headerText: "", key: "worktype", width: "38px" }]
					},
					{
						headerText: getText('KSU003_25'), group: [
							{ headerText: "", key: "worktimeCode", width: "40px", handlerType: "input", dataType: "text" },
							{ headerText: "", key: "worktime", width: "38px" }]
					},
					{
						headerText: getText('KSU003_27'), group: [
							{ headerText: "", key: "startTime1", width: "40px", handlerType: "input", dataType: "time" }]
					},
					{
						headerText: getText('KSU003_28'), group: [
							{ headerText: "", key: "endTime1", width: "40px", handlerType: "input", dataType: "time" }]
					},
					{
						headerText: getText('KSU003_29'), group: [
							{ headerText: "", key: "startTime2", width: "40px", handlerType: "input", dataType: "time" }]
					},
					{
						headerText: getText('KSU003_30'), group: [
							{ headerText: "", key: "endTime2", width: "40px", handlerType: "input", dataType: "time" }]
					},
					{
						headerText: getText('KSU003_31'), group: [
							{ headerText: "", key: "totalTime", width: "40px" }]
					},
					{
						headerText: getText('KSU003_32'), group: [
							{ headerText: "", key: "breaktime", width: "35px" }]
					}
				];

				middleHeader = {
					columns: middleColumns,
					width: "391px",
					rowHeight: "33px"
				};
				middleContent = {
					columns: middleColumns,
					dataSource: middleDs,
					primaryKey: "empId",
					features: [{
						name: "BodyCellStyle",
						decorator: middleContentDeco
					}]
				};

				let width = "42px", detailColumns = [], detailHeaderDs = [], detailContent = {}, detailHeader = {};

				// A8_2 TQP
				// let timesOfHeader : model.DisplaySettingByDateDto  = self.dataInitStartKsu003Dto().byDateDto();
				for (let y = 0; y <= displayRange; y++) {
					if (y == 0) {
						detailColumns.push({ key: "empId", width: "0px", headerText: "ABC", visible: false });
					} else {
						detailColumns.push({ key: (y - 1).toString(), width: width });
					}
				}
				// Phần detail
				detailHeaderDs = [{ empId: "" }];
				let detailHeaders = {};
				for (let y = 0; y <= displayRange; y++) {
					detailHeaderDs[0][y] = y.toString();
					detailHeaders[y] = "";
				}

				detailHeader = {
					columns: detailColumns,
					dataSource: detailHeaderDs,
					rowHeight: "33px",
					width: "700px"
				};

				let detailContentDs = [];
				//for (let z = 0; z < self.dataFromA().listEmp.length; z++) {
				let z = 0;
				if (leftDs[0].empId === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570" &&
					self.dataFromA().listEmp[z].id === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570") {
					let datafilter = _.filter(timeGantChart, (x: any) => { return x.empId === self.dataFromA().listEmp[z].id });
					detailContentDs.push({ empId: datafilter[0].empId, ...detailHeaders });
				}
				//}

				detailContent = {
					columns: detailColumns,
					dataSource: detailContentDs,
					primaryKey: "empId",
					features: [{
						name: "BodyCellStyle",
						decorator: detailContentDeco
					}]
				};

				let extable = new exTable.ExTable($("#extable-ksu003"), {
					headerHeight: "33px",
					bodyRowHeight: "30px",
					bodyHeight: "400px",
					horizontalSumHeaderHeight: "75px",
					horizontalSumBodyHeight: "140px",
					horizontalSumBodyRowHeight: "20px",
					areaResize: false,
					manipulatorId: self.employeeIdLogin,
					manipulatorKey: "empId",
					bodyHeightMode: "fixed",
					showTooltipIfOverflow: true,
					windowXOccupation: 40,
					windowYOccupation: 200
				}).LeftmostHeader(leftmostHeader).LeftmostContent(leftmostContent)
					.MiddleHeader(middleHeader).MiddleContent(middleContent)
					.DetailHeader(detailHeader).DetailContent(detailContent);

				extable.create();

				ruler = extable.getChartRuler();

				self.addTypeOfChart(ruler);

				let lstBreakTime: any = [], totalBreakTimeNew: any = 0, totalBreakTime1: number = 0, totalBreakTime2: number = 0;
				// (5' ~ 3.5 pixel ~ 12 khoảng trong 60', 10' ~ 7 ~ 6, 15' ~ 10.5 ~ 4, 30' ~ 21 ~ 2, 60' ~ 42 ~ 1)	
				// unitToPx = khoảng pixel theo số phút 
				// start theo pixel = unitToPx * start * (khoảng-pixel/ phút)
				// end theo pixel = unitToPx * end * (khoảng-pixel/ phút)
				//for (let i = 0; i < self.dataFromA().listEmp.length; i++) {
				i = 0;

				if (leftDs[0].empId === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570" &&
					self.dataFromA().listEmp[i].id === "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570") {
					let datafilter: Array<ITimeGantChart> = _.filter(timeGantChart, (x: any) => { return x.empId === self.dataFromA().listEmp[i].id });

					let timeChart: any = null, timeChart2: any = null, lgc = "", rgc = "", checkGc = 0, timeChartOver: any = null, timeChartBrk: any = null, timeChartHoliday: any = null, timeChartShort: any = null;
					let indexS = 0;
					let indexF = 0;
					self.addAllChart(datafilter, i, timeChart, timeChart2, lgc, rgc, checkGc, timeChartOver, timeChartBrk, timeChartHoliday, timeChartShort, indexS, indexF, lstBreakTime, midData, 0);
					i = 0;
					$(lgc).on("gcresize", (e: any) => {
						let param = e.detail;
						let startMinute = 0, endMinute = 0;
						startMinute = duration.create(param[0] * 5).text;
						endMinute = duration.create(param[1] * 5).text;
						if (param[2]) {
							$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "startTime1", startMinute);
						} else {
							$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "endTime1", endMinute);
						}
						let totalTimeNew = 0;

						lstBreakTime.forEach((brk: any) => {
							let startMinutes = param[0], endMinutes = param[1];
							if (_.inRange(brk.startTime, startMinutes, endMinutes) || _.inRange(brk.endTime, startMinutes, endMinutes) ||
								_.inRange(brk.startTime, timeChart2.startTime, timeChart2.endTime) || _.inRange(brk.endTime, timeChart2.startTime, timeChart2.endTime)) {
								totalTimeNew += (brk.endTime - brk.startTime);
							}
						})

						totalBreakTimeNew = formatById("Clock_Short_HM", (totalTimeNew * 5));
						$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "breaktime", totalBreakTimeNew === 0 ? "0:00" : totalBreakTimeNew);
					});

					$(rgc).on("gcresize", (e: any) => {
						let param = e.detail;
						let startMinute, endMinute;
						startMinute = duration.create(param[0] * 5).text;
						endMinute = duration.create(param[1] * 5).text;
						if (param[2]) {
							$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "startTime2", startMinute);
						} else {
							$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "endTime2", endMinute);
						}
						let totalTimeNew = 0;
						lstBreakTime.forEach((brk: any) => {
							let startMinutes = param[0], endMinutes = param[1];
							if (_.inRange(brk.startTime, startMinutes, endMinutes) || _.inRange(brk.endTime, startMinutes, endMinutes) ||
								_.inRange(brk.startTime, timeChart.startTime, timeChart.endTime) || _.inRange(brk.endTime, timeChart.startTime, timeChart.endTime)) {
								totalTimeNew += (brk.endTime - brk.startTime);
							}
						})
						totalBreakTimeNew = formatById("Clock_Short_HM", (totalTimeNew * 5));
						$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "breaktime", totalBreakTimeNew === 0 ? "0:00" : totalBreakTimeNew);
					});

					$(lgc).on("gcdrop", (e: any) => {
						let param = e.detail;
						let minutes = duration.create(param[0] * 5).text;
						$("#extable").exTable("cellValue", "middle", i + "", "startTime1", minutes);
						minutes = duration.create(param[1] * 5).text;
						$("#extable").exTable("cellValue", "middle", i + "", "endTime1", minutes);
					});

					$(rgc).on("gcdrop", (e: any) => {
						let param = e.detail;
						let minutes = duration.create(param[0] * 5).text;
						$("#extable").exTable("cellValue", "middle", i + "", "startTime1", minutes);
						minutes = duration.create(param[1] * 5).text;
						$("#extable").exTable("cellValue", "middle", i + "", "endTime1", minutes);
					});

					let totalTimeBrk = 0;
					let recharge = function(detail: any) {
						let time = duration.parseString(detail.value).toValue();
						let startMinutes: any = null, endMinutes: any = null, startMinutes2: any = null, endMinutes2: any = null;
						if (detail.columnKey === "startTime1") {
							ruler.extend(detail.rowIndex, `lgc${detail.rowIndex}`, Math.round(time / 5));
							startMinutes = Math.round(time / 5);
							endMinutes = timeChart.endTime;
						} else if (detail.columnKey === "endTime1") {
							ruler.extend(detail.rowIndex, `lgc${detail.rowIndex}`, null, Math.round(time / 5));
							startMinutes = timeChart.startTime;
							endMinutes = Math.round(time / 5);
						} else if (detail.columnKey === "startTime2") {
							ruler.extend(detail.rowIndex, `rgc${detail.rowIndex}`, Math.round(time / 5));
							startMinutes2 = Math.round(time / 5);
							endMinutes2 = timeChart2.endTime;
						} else if (detail.columnKey === "endTime2") {
							ruler.extend(detail.rowIndex, `rgc${detail.rowIndex}`, null, Math.round(time / 5));
							startMinutes2 = timeChart2.startTime;
							endMinutes2 = Math.round(time / 5);
						}

						let totalTimeNews = 0, totalTimeNews1 = 0;
						if (detail.columnKey === "startTime1" || detail.columnKey === "endTime1" || detail.columnKey === "startTime2" || detail.columnKey === "endTime2") {
							lstBreakTime.forEach((brk: any) => {
								let brkChart = {
									startTime: brk.startTime,
									endTime: brk.endTime
								}, timeCharts = {
									startTime: startMinutes == null ? timeChart.startTime : startMinutes,
									endTime: endMinutes == null ? timeChart.endTime : endMinutes
								}
								if (_.inRange(brk.startTime, startMinutes, endMinutes) || _.inRange(brk.endTime, startMinutes, endMinutes)) {
									totalTimeNews += self.calcTotalBreakTime(brkChart, timeCharts);
								}
								if (timeChart2 != null) {
									if (_.inRange(brk.startTime, startMinutes2, endMinutes2) || _.inRange(brk.endTime, startMinutes2, endMinutes2)) {
										timeCharts = {
											startTime: startMinutes2 == null ? timeChart2.startTime : startMinutes2,
											endTime: endMinutes2 == null ? timeChart2.endTime : endMinutes2
										}
										totalTimeNews1 += self.calcTotalBreakTime(brkChart, timeCharts);
									}
								}
							})
							totalTimeBrk = formatById("Clock_Short_HM", ((totalTimeNews + totalTimeNews1) * 5));;
							$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "breaktime", totalTimeBrk === 0 ? "0:00" : totalTimeBrk);
						}
					};

					$("#extable-ksu003").on("extablecellupdated", (e: any) => {
						recharge(e.detail);
					});

					$("#extable-ksu003").on("extablecellretained", (e: any) => {
						recharge(e.detail);
					});
				}
				//}

				storage.getItem(self.KEY).ifPresent((data: any) => {
					let userInfor: ILocalStore = JSON.parse(data);
					if (userInfor.operationUnit === "0") {
						ruler.setSnatchInterval(1);
					} else if (userInfor.operationUnit === "1") {
						ruler.setSnatchInterval(2);
					} else if (userInfor.operationUnit === "2") {
						ruler.setSnatchInterval(3);
					} else if (userInfor.operationUnit === "3") {
						ruler.setSnatchInterval(6);
					} else if (userInfor.operationUnit === "4") {
						ruler.setSnatchInterval(12);
					}
				});


				// set lock slide and resize chart
				//ruler.setLock([0, 1, 3], true);

				// set height grid theo localStorage đã lưu
				self.setPositionButonDownAndHeightGrid();
			}
		}

		addAllChart(datafilter: Array<ITimeGantChart>, i: number, timeChart: any, timeChart2: any,
			lgc: any, rgc: any, checkGc: any, timeChartOver: any, timeChartBrk: any,
			timeChartHoliday: any, timeChartShort: any, indexS: number, indexF: number, lstBreakTime: any, midData: any, check: number) {
			let self = this, spgc: any = null, over: any = null, brkgc: any = null, short: any = null, holiday: any = null;
			if (datafilter != null) {
				if (datafilter[0].typeOfTime != "") {
					// add chart for FIXED TIME - thời gian cố định
					if (datafilter[0].typeOfTime === "Fixed") {
						let fixed = datafilter[0].gcFixedWorkTime;
						timeChart = self.convertTimeToChart(fixed[0].startTime, fixed[0].endTime);

						lgc = self.addChartWithTypes(ruler, "Fixed", `lgc${i}`, timeChart, i, 0, 999, 0, 999);
						/*lgc = ruler.addChartWithType("Fixed", {
							id: `lgc${i}`,
							start: timeChart.startTime,
							end: timeChart.endTime,
							lineNo: i
						});*/

						if (fixed.length > 1 && fixed[1].startTime != null && fixed[1].endTime != null) {
							timeChart2 = self.convertTimeToChart(fixed[1].startTime, fixed[1].endTime);
							//lgc = self.addChartWithTypes(ruler, "Fixed", `lgc${i}`, timeChart, i);
							rgc = self.addChartWithTypes(ruler, "Fixed", `rgc${i}`, timeChart2, i, 0, 999, 0, 999);
							indexS = indexS++;
							checkGc = 1;
						}
					}

					// add chart for CHANGEABLE TIME - thời gian lưu động
					if (datafilter[0].typeOfTime === "Changeable") {
						let changeable = datafilter[0].gcFlowTime
						timeChart = self.convertTimeToChart(changeable[0].startTime, changeable[0].endTime);

						lgc = self.addChartWithTypes(ruler, "Changeable", `lgc${i}`, timeChart, i, 0, 999, 0, 999);

						if (changeable.length > 1 && changeable[1].startTime != null && changeable[1].endTime != null) {
							timeChart2 = self.convertTimeToChart(changeable[1].startTime, changeable[1].endTime);
							rgc = self.addChartWithTypes(ruler, "Changeable", `rgc${i}`, timeChart2, i, 0, 999, 0, 999);
							indexS = indexS++;
							checkGc = 1;
						}
					}

					// add chart for FLEX TIME - thời gian flex
					if (datafilter[0].typeOfTime === "Flex") {
						let flex = datafilter[0].gcFlexTime
						timeChart = self.convertTimeToChart(flex[0].startTime, flex[0].endTime);

						lgc = self.addChartWithTypes(ruler, "Flex", `lgc${i}`, timeChart, i, 0, 999, 0, 999);

						let coreTime = datafilter[0].gcCoreTime;
						if (coreTime.length > 0) {
							timeChart2 = self.convertTimeToChart(coreTime[0].coreStartTime, coreTime[0].coreEndTime);
							self.addChildChartWithTypes(ruler, "CoreTime", `lgc${i}_` + indexF, timeChart2, i, `lgc${i}`)

							indexF = ++indexF;
						}
					}
				};

				let suportTime = datafilter[0].gcSupportTime;
				if (suportTime != null) {
					timeChart = self.convertTimeToChart(suportTime[0].coreStartTime, suportTime[0].coreEndTime);
					spgc = self.addChildChartWithTypes(ruler, "SupportTime", `lgc${i}_` + indexF, timeChart, i, `lgc${i}`)

					indexF = ++indexF;
				}

				let overTime = datafilter[0].gcOverTime;
				if (overTime.length > 0) {
					overTime[0].lstOverTime.forEach(x => {
						timeChartOver = self.convertTimeToChart(x.changeableStartTime.startTime.time, x.changeableStartTime.endTime.time);
						over = self.addChildChartWithTypes(ruler, "OT", `lgc${i}_` + indexF, timeChartOver, i, `lgc${i}`)

						indexF = ++indexF;

						timeChartOver = self.convertTimeToChart(x.changeableEndTime.startTime.time, x.changeableEndTime.endTime.time);
						over = self.addChildChartWithTypes(ruler, "OT", `lgc${i}_` + indexF, timeChartOver, i, `lgc${i}`)

						indexF = ++indexF;
					})
				}

				let breakTime = datafilter[0].gcBreakTime[0].lstBreakTime[0].breakTimeSheets;
				if (breakTime.length > 0) {
					breakTime.forEach(y => {
						timeChartBrk = self.convertTimeToChart(y.startTime, y.endTime);
						let id = `lgc${i}_` + indexF, parent = `lgc${i}`;
						if (datafilter[0].gantCharts === 1 && checkGc === 1) {
							if (y.startTime >= datafilter[0].gcFixedWorkTime[1].startTime &&
								y.endTime <= datafilter[0].gcFixedWorkTime[1].endTime) {
								id = `rgc${i}_` + indexF, parent = `rgc${i}`;
							}
						}

						lstBreakTime.push({
							startTime: timeChartBrk.startTime,
							endTime: timeChartBrk.endTime,
							id: id
						})
						brkgc = ruler.addChartWithType("BreakTime", {
							id: id,
							parent: parent,
							lineNo: i,
							start: timeChartBrk.startTime,
							end: timeChartBrk.endTime,
							limitStartMin: 0,
							limitStartMax: 999,
							limitEndMin: 0,
							limitEndMax: 999,
							zindex: 1001,
							resizeFinished: (b: any, e: any, p: any) => {
							},
							dropFinished: (b: any, e: any) => {
								let datafilterBrk = _.filter(midData, (x: any) => { return x.empId === self.dataFromA().listEmp[i].id }),
									dataMidBrk = datafilterBrk[0];
								breakTime = dataMidBrk.breaktime;
								if (b !== timeChartBrk.startTime && e !== timeChartBrk.endTime) {
									$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "breaktime", self.timeBrkText() == null ? "0:00" : self.timeBrkText() + " ");
								} else {
									$("#extable-ksu003").exTable("cellValue", "middle", "ae7fe82e-a7bd-4ce3-adeb-5cd403a9d570", "breaktime", self.timeBrkText() == null ? "0:00" : self.timeBrkText());
								}
							}
						});
						indexF = ++indexF;
					})
				}

				let shortTime = datafilter[0].gcShortTime;
				if (shortTime.length > 0) {
					shortTime[0].listShortTime.forEach(x => {
						timeChartShort = self.convertTimeToChart(x.startTime.time, x.endTime.time);
						short = self.addChildChartWithTypes(ruler, "ShortTime", `lgc${i}_` + indexF, timeChartShort, i, `lgc${i}`, 0, 999, 0, 999, null, 1002)
						/*,
						resizeFinished: (b : any, e : any, p : any) => {
							let minutes;
							if (p) {
								minutes = duration.create(b * 5).text;
							} else {
								minutes = duration.create(e * 5).text;
							}
						},
						dropFinished: (b : any, e : any) => {
							let minutes;
							if (b) {
								minutes = duration.create(b * 5).text;
							}
						}*/
						indexF = ++indexF;
					})
				}

				let holidayTime = datafilter[0].gcHolidayTime;
				if (holidayTime.length > 0) {
					holidayTime[0].listTimeVacationAndType.forEach(x => {
						x.timeVacation.timeZone.forEach(hld => {
							timeChartHoliday = self.convertTimeToChart(hld.startTime.time, hld.endTime.time);
							holiday = self.addChildChartWithTypes(ruler, "HolidayTime", `lgc${i}_` + indexF, timeChartHoliday, i, `lgc${i}`, 0, 999, 0, 999, null, 1003);
							indexF = ++indexF;
						})
					})
				}
			}
			if (check === 1) {
				let gc: any = {
					support: spgc,
					over: over,
					break: brkgc,
					short: short,
					holiday: holiday
				}
				return gc;
			}
		}

		addChartWithType045(ruler: any, type: any, id: any, timeChart: any, lineNo: any, parent?: any,
			limitStartMin?: any, limitStartMax?: any, limitEndMin?: any, limitEndMax?: any, zIndex?: any) {
			let self = this, timeEnd = self.convertTimePixel(self.timeRange === 24 ? "24:00" : "48:00");
			return {
				type: type,
				options: {
					id: id,
					start: timeChart.startTime <= timeEnd ? timeChart.startTime : timeEnd,
					end: timeChart.endTime <= timeEnd ? timeChart.endTime : timeEnd,
					lineNo: lineNo,
					parent: parent,
					limitStartMin: limitStartMin,
					limitStartMax: limitStartMax,
					limitEndMin: limitEndMin,
					limitEndMax: limitEndMax,
					zIndex: !_.isNil(zIndex) ? zIndex : 1000
				}
			}
		}

		addChartWithTypes(ruler: any, type: any, id: any, timeChart: any, lineNo: any,
			limitStartMin?: any, limitStartMax?: any, limitEndMin?: any, limitEndMax?: any) {
			let self = this, timeEnd = self.convertTimePixel(self.timeRange === 24 ? "24:00" : "48:00");
			return ruler.addChartWithType(type, {
				id: id,
				start: timeChart.startTime <= timeEnd ? timeChart.startTime : timeEnd,
				end: timeChart.endTime <= timeEnd ? timeChart.endTime : timeEnd,
				lineNo: lineNo,
				limitStartMin: limitStartMin,
				limitStartMax: limitStartMax,
				limitEndMin: limitEndMin,
				limitEndMax: limitEndMax
			});
		}

		addChildChartWithTypes(ruler: any, type: any, id: any, timeChart: any, lineNo: any, parent?: any,
			limitStartMin?: any, limitStartMax?: any, limitEndMin?: any, limitEndMax?: any, title?: any, zIndex?: any) {
			let self = this, timeEnd = self.convertTimePixel(self.timeRange === 24 ? "24:00" : "48:00");
			return ruler.addChartWithType(type, {
				id: id,
				parent: parent,
				start: timeChart.startTime <= timeEnd ? timeChart.startTime : timeEnd,
				end: timeChart.endTime <= timeEnd ? timeChart.endTime : timeEnd,
				lineNo: lineNo,
				limitStartMin: limitStartMin,
				limitStartMax: limitStartMax,
				limitEndMin: limitEndMin,
				limitEndMax: limitEndMax,
				title: title,
				zIndex: !_.isNil(zIndex) ? zIndex : 1000
			});
		}


		convertTimeToChart(startTime: any, endTime: any) {
			let self = this, convertTime = null;

			startTime = formatById("Clock_Short_HM", startTime);
			endTime = formatById("Clock_Short_HM", endTime);
			startTime = self.convertTimePixel(startTime);
			endTime = self.convertTimePixel(endTime);
			convertTime = {
				startTime: startTime,
				endTime: endTime
			}
			return convertTime;
		}

		addTypeOfChart(ruler: any) {
			let self = this;
			ruler.addType({
				name: "Fixed",
				color: "#ccccff",
				lineWidth: 30,
				canSlide: false,
				unitToPx: self.operationUnit()
			});

			ruler.addType({
				name: "Changeable",
				color: "#ffc000",
				lineWidth: 30,
				canSlide: true,
				unitToPx: self.operationUnit()
			});

			ruler.addType({
				name: "Flex",
				color: "#ccccff",
				lineWidth: 30,
				canSlide: true,
				unitToPx: self.operationUnit()
			});

			ruler.addType({
				name: "BreakTime",
				followParent: true,
				color: "#ff9999",
				lineWidth: 30,
				canSlide: true,
				unitToPx: self.operationUnit(),
				pin: true,
				rollup: true,
				roundEdge: true,
				fixed: "Both"
			});

			ruler.addType({
				name: "OT",
				followParent: true,
				color: "#ffff00",
				lineWidth: 30,
				canSlide: false,
				unitToPx: self.operationUnit(),
				pin: true,
				rollup: true,
				fixed: "Both"
			});

			ruler.addType({
				name: "HolidayTime",
				followParent: true,
				color: "#c4bd97",
				lineWidth: 30,
				canSlide: false,
				unitToPx: self.operationUnit(),
				pin: true,
				rollup: true,
				roundEdge: false,
				fixed: "Both"
			});

			ruler.addType({
				name: "ShortTime",
				followParent: true,
				color: "#6fa527",
				lineWidth: 30,
				canSlide: false,
				unitToPx: self.operationUnit(),
				pin: true,
				rollup: true,
				roundEdge: false,
				fixed: "Both"
			});

			ruler.addType({
				name: "CoreTime",
				color: "#00ffcc",
				lineWidth: 30,
				unitToPx: self.operationUnit(),
				fixed: "Both"
			});
		}

		setPositionButonToRightToLeft() {
			let self = this;
			self.indexBtnToLeft(0);

			let marginleftOfbtnToRight: number = 0;

			$(".toLeft").css("display", "none");
			marginleftOfbtnToRight = $("#extable-ksu003").width() - 32;
			$(".toRight").css('margin-left', marginleftOfbtnToRight + 'px');
		}

		setPositionButonDownAndHeightGrid() {
			let self = this;

			$("#extable-ksu003").exTable("setHeight", 10 * 30 + 18);
			$(".toDown").css({ "margin-top": 10 * 30 + 8 + 'px' });

			if (self.indexBtnToLeft() % 2 != 0) {
				$("#master-wrapper").css({ 'overflow-y': 'hidden' });
			} else {
				$("#master-wrapper").css({ 'overflow-y': 'auto' });
				$("#master-wrapper").css({ 'overflow-x': 'hidden' });
			}
			$("#extable-ksu003").exTable("scrollBack", 0, { h: self.startScrollGc() });
		}

		setWidth(): any {
			$(".ex-header-detail").width(window.innerWidth - 572);
			$(".ex-body-detail").width(window.innerWidth - 554);
			$("#extable-ksu003").width(window.innerWidth - 554);
		}

		toLeft() {
			let self = this;
			if (self.indexBtnToLeft() % 2 == 0) {
				if (self.showA9) {
					$("#extable-ksu003").exTable("hideMiddle");
				}
				$(".toLeft").css("margin-left", "210px");
				if (navigator.userAgent.indexOf("Chrome") == -1) {
					$(".toLeft").css("margin-left", 100 + 'px');
				}
			} else {
				if (self.showA9) {
					$("#extable-ksu003").exTable("showMiddle");
				}
				$(".ex-header-middle").css("width", 560 + 'px' + '!important')
				/*$(".ex-header-detail").css("width", 1009 + 'px !important');
				$(".ex-body-detail").css("width", 1025 + 'px !important');*/
				$(".toLeft").css("margin-left", 577 + 'px');
				if (navigator.userAgent.indexOf("Chrome") == -1) {
					$(".toLeft").css("margin-left", 562 + 'px');
				}
			}
			self.indexBtnToLeft(self.indexBtnToLeft() + 1);
			self.localStore().showHide = self.indexBtnToLeft();
			storage.setItemAsJson(self.KEY, self.localStore());
			$("#extable-ksu003").exTable("scrollBack", 0, { h: self.startScrollGc() });
		}

		toDown() {
			let self = this;
			let exTableHeight = window.innerHeight - 92 - 31 - 50 - 50 - 35 - 27 - 27 - 20;
			exTableHeight = 17 * 30 + 18;
			$("#master-wrapper").css({ 'overflow-x': 'hidden' });
			if (self.indexBtnToDown() % 2 == 0) {
				//$(".toDown").css("background", "url() no-repeat center");
				$("#extable-ksu003").exTable("setHeight", exTableHeight);
				$(".toDown").css('margin-top', exTableHeight - 8 + 'px');
				$("#master-wrapper").css({ 'overflow-y': 'auto' });

			} else {
				exTableHeight = 10 * 30 + 18;
				//$(".toDown").css("background", "url() no-repeat center");
				$("#extable-ksu003").exTable("setHeight", exTableHeight);
				$(".toDown").css('margin-top', exTableHeight - 8 + 'px');

			}
			self.indexBtnToDown(self.indexBtnToDown() + 1);
		}

		showHide() {
			let self = this;
			if (self.indexBtnToLeft() % 2 == 0) {

				/*$(".ex-header-detail").css("width", 1009 + 'px !important');
				$(".ex-body-detail").css("width", 1025 + 'px !important');*/

				if (!self.showA9) {
					$("#extable-ksu003").exTable("showMiddle");
				}
				$(".toLeft").css("margin-left", 577 + 'px');
				if (navigator.userAgent.indexOf("Chrome") == -1) {
					$(".toLeft").css("margin-left", 562 + 'px');
				}
			} else {
				if (self.showA9) {
					$("#extable-ksu003").exTable("hideMiddle");
				}
				$(".toLeft").css("margin-left", "210px");
				if (navigator.userAgent.indexOf("Chrome") == -1) {
					$(".toLeft").css("margin-left", 200 + 'px');
				}
			}
		}

		public convertTime(timeStart: any, timeEnd: any, timeStart2: any, timeEnd2: any) {

			if (timeStart != "") {
				timeStart = Math.round(duration.parseString(timeStart).toValue());
			}

			if (timeEnd != "") {
				timeEnd = Math.round(duration.parseString(timeEnd).toValue());
			}

			if (timeStart2 != "") {
				timeStart2 = Math.round(duration.parseString(timeStart2).toValue());
			}

			if (timeEnd2 != "") {
				timeEnd2 = Math.round(duration.parseString(timeEnd2).toValue());
			}
			let timeConvert = {
				start: timeStart != "" ? timeStart : "",
				end: timeEnd != "" ? timeEnd : "",
				start2: timeStart2 != "" ? timeStart2 : "",
				end2: timeEnd2 != "" ? timeEnd2 : ""
			}
			return timeConvert;
		}

	}

	/**
 * 応援区分
 */
	export enum SupportAtr {
		/**
		 * 応援ではない
		 */
		NOT_CHEERING = 1,
		/**
		 * 時間帯応援元
		 */
		TIMEZONE_SUPPORTER = 2,
		/**
		 * 時間帯応援先
		 */
		TIMEZONE_RESPONDENT = 3,
		/**
		 * 終日応援元
		 */
		ALL_DAY_SUPPORTER = 4,
		/**
		 * 終日応援先
		 */
		ALL_DAY_RESPONDENT = 5
	}

	export enum EditStateSetting {
		/**
		* 手修正（本人）
		*/
		HAND_CORRECTION_MYSELF = 0,
		/**
		 * 手修正（他人） 
		 */
		HAND_CORRECTION_OTHER = 1,
		/**
		 * 申請反映
		 */
		REFLECT_APPLICATION = 2,
		/**
		 * 打刻反映
		 */
		IMPRINT = 3
	}

	export enum WorkTimeForm {
		/**
		* 固定勤務 
		*/
		FIXED = 0,
		/**
		 * フレックス勤務
		 */
		FLEX = 1,
		/**
		 * 流動勤務
		 */
		FLOW = 2,
		/**
		 * 時差勤務
		 */
		TIMEDIFFERENCE = 3
	}

	class ItemModel {
		code: string;
		name: string;

		constructor(code: string, name: string) {
			this.code = code;
			this.name = name;
		}
	}

	interface ILocalStore {
		startTimeSort: string;
		showWplName: boolean;
		operationUnit: string;
		displayFormat: number;
		showHide: number;
		lstSortByTime: Array<any>;
		lstSortByEmp: Array<any>;
	}

	interface ITimeGantChart {
		empId: string,
		typeOfTime: string,
		gantCharts: number,
		gcFixedWorkTime: Array<model.IFixedFlowFlexTime>,
		gcBreakTime: Array<model.IBreakTime>,
		gcOverTime: Array<model.IOverTime>,
		gcSupportTime: any,
		gcFlowTime: Array<model.IFixedFlowFlexTime>,
		gcFlexTime: Array<model.IFixedFlowFlexTime>,
		gcCoreTime: Array<model.ICoreTime>,
		gcHolidayTime: Array<model.IHolidayTime>,
		gcShortTime: Array<model.IShortTime>;
	};

	class CellColor {
		columnKey: any;
		rowId: any;
		innerIdx: any;
		clazz: any;
		constructor(columnKey: any, rowId: any, clazz: any, innerIdx?: any) {
			this.columnKey = columnKey;
			this.rowId = rowId;
			this.innerIdx = innerIdx;
			this.clazz = clazz;
		}
	}
}