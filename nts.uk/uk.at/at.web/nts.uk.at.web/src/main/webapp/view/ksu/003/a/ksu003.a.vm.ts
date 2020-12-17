module nts.uk.at.view.ksu003.a.viewmodel {
	import getText = nts.uk.resource.getText;
	import setShared = nts.uk.ui.windows.setShared;
	import getShared = nts.uk.ui.windows.getShared;
	import errorDialog = nts.uk.ui.dialog.alertError;
	import dialog = nts.uk.ui.dialog;
	import bundledErrors = nts.uk.ui.dialog.bundledErrors;
	import block = nts.uk.ui.block;
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
		employeeIdLogin: string = __viewContext.user.employeeId; // Employee login

		targetDate: KnockoutObservable<string> = ko.observable('2020/05/02'); // A2_1_3
		targetDateDay: KnockoutObservable<string> = ko.observable('');

		organizationName: KnockoutObservable<string> = ko.observable(''); // A2_2
		selectedDisplayPeriod: KnockoutObservable<number> = ko.observable(1); // A2_5

		itemList: KnockoutObservableArray<ItemModel>; /*A3_2*/
		selectOperationUnit: KnockoutObservable<string> = ko.observable('0');

		sortList: KnockoutObservableArray<ItemModel>; /*A3_4*/
		checked: KnockoutObservable<string> = ko.observable();

		checkedName: KnockoutObservable<boolean> = ko.observable(false); // A3_3

		showA9: boolean;
		checkNext: KnockoutObservable<boolean> = ko.observable(true); // xử lý next day
		checkPrv: KnockoutObservable<boolean> = ko.observable(true);
		
		indexBtnToLeft: KnockoutObservable<number> = ko.observable(0);// xử lý của botton toLeft, toRight
		indexBtnToDown: KnockoutObservable<number> = ko.observable(0);// xử lý của botton toDown, toUp
		
		dataFromA: KnockoutObservable<model.InforScreenADto> = ko.observable(); //Data get from Screen A
		dataScreen003A: KnockoutObservable<model.DataScreenA> = ko.observable();
		dataScreen003AFirst: KnockoutObservable<model.DataScreenA> = ko.observable();
		dataScreen045A: KnockoutObservable<model.DataFrom045> = ko.observable();
		dataInitStartKsu003Dto: KnockoutObservable<model.GetInfoInitStartKsu003Dto> = ko.observable();
		displayWorkInfoByDateDto: KnockoutObservable<Array<model.DisplayWorkInfoByDateDto>> = ko.observable();
		displayDataKsu003: KnockoutObservable<Array<model.DisplayWorkInfoByDateDto>> = ko.observable();
		fixedWorkInformationDto: Array<model.FixedWork> = [];
		
		workingInfoColor: KnockoutObservable<string> = ko.observable(''); // A6_color①
		totalTimeColor: KnockoutObservable<string> = ko.observable(''); // A7 color
		dataColorA6: Array<any> = [];
		
		employeeWorkInfo: KnockoutObservable<model.EmployeeWorkInfoDto> = ko.observable(); //社員勤務情報　dto
		employeeScheduleInfo: Array<any> = [];
		
		operationUnit: KnockoutObservable<number> = ko.observable(3.5); // A3_2 pixel (mỗi ô đang để 40px)
		operationOneMinus: KnockoutObservable<number> = ko.observable(12);

		localStore: KnockoutObservable<ILocalStore> = ko.observable();
		lstEmpId: Array<IEmpidName> = [];
		timeRange: number = 0; // thời gian bắt đầu ở header phần detail
		initDispStart: number = 0; // thời gian bắt đầu của scroll phần detail

		timeBrkText: KnockoutObservable<string> = ko.observable();
		
		checkGetInfo: boolean = false;
		
		dataOfGantChart: Array<ITimeGantChart> = []; // data của từng phần trên extable
		midDataGC: Array<any> = [];
		disableDs: any = [];
		leftDs: any = [];
		
		tooltip: any = [];
		
		allGcShow: any = []; // lưu gant chart và data để tạo lại
		allTimeChart: any = [];
		
		
		checkClearTime: boolean = true; // check when change work time, work type
		checkUpdateMidChart: boolean = true;
		checkUpdateTime: any = [];
		
		lstDis: any = [];
		lstBreakSum: any = []; // list break time xuất hiện trên màn hình
		
		totalTimeWork: any = 0;
		checkDisByDate : boolean = true;

		constructor() {
			let self = this;
			// get data from sc A
			self.dataFromA = ko.observable(getShared('dataFromA'));
			if(self.dataFromA().daySelect <= self.dataFromA().dayEdit){
				self.checkDisByDate = false;
			}
			self.lstEmpId = _.flatMap(self.dataFromA().listEmp, c => [{ empId: c.id, name: c.name, code: c.code }]);
			self.targetDate(self.dataFromA().daySelect);
			if (self.targetDate() === (self.dataFromA().startDate)) {
				self.checkPrv(false);
			}
			if (self.targetDate() === (self.dataFromA().endDate)) {
				self.checkNext(false);
			}
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

			storage.getItem(self.KEY).ifPresent((data: any) => {
				let userInfor: ILocalStore = JSON.parse(data);
				self.indexBtnToLeft(userInfor.showHide);
				self.selectOperationUnit(userInfor.operationUnit);
				self.selectedDisplayPeriod(userInfor.displayFormat);
				self.checked(userInfor.startTimeSort);
				self.checkedName(userInfor.showWplName);
				self.lstEmpId = self.lstEmpId.sort(function(a: any, b: any) {
					return _.findIndex(userInfor.lstEmpIdSort, x => { return x.empId == a.empId }) - _.findIndex(userInfor.lstEmpIdSort, x => { return x.empId == b.empId });
				})
			});
			/* 開始時刻順に並び替える(A3_3)はチェックされている */
			self.checked.subscribe((value) => {
				let checkSort = [];
				storage.getItem(self.KEY).ifPresent((data: any) => {
					checkSort = $("#extable-ksu003").exTable('updatedCells');
				})
				if (checkSort.length > 0) {
					dialog.confirm({ messageId: "Msg_447" }).ifYes(() => {
						self.sortEmployee(value);
					}).ifNo(() => {
						return;
					})
				} else {
					self.sortEmployee(value);
				}

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

			self.checkUpdateTime = {
				name: "",
				id: 0
			}
			// tính tổng thời gian khi thay đổi start và end time
			$("#extable-ksu003").on("extablecellupdated", (dataCell: any) => {
				let index: number = dataCell.originalEvent.detail.rowIndex;
				let dataMid = $("#extable-ksu003").exTable('dataSource', 'middle').body[index];
				let empId = self.lstEmpId[dataCell.originalEvent.detail.rowIndex].empId;
				let dataFixed = _.filter(self.dataScreen003A().employeeInfo, x => { return x.empId === empId }),
				dataFixInfo = _.filter(self.fixedWorkInformationDto, x => { return x.empId === empId });

				if (self.checkGetInfo == false && self.checkUpdateMidChart == true) {
					if (dataCell.originalEvent.detail.columnKey === "worktimeCode" && dataMid.worktimeCode != "") {
						self.checkUpdateTime.name = "worktimeCode";
						self.checkUpdateTime.id = 1;
						self.inputWorkInfo(index, dataCell, dataFixed, empId);
					}

					// 勤務種類を変更する (nhập thủ công worktype code , worktime code )
					if ((dataCell.originalEvent.detail.columnKey === "worktypeCode" && dataMid.worktypeCode != "")) {
						self.checkUpdateTime.name = "worktypeCode";
						self.checkUpdateTime.id = 2;
						let targetOrgDto = {
							workTypeCode: dataMid.worktypeCode.trim(),
							workTimeCode: dataMid.worktimeCode.trim()
						}
						service.changeWorkType(targetOrgDto).done((data: any) => {

							if (data != null && !_.isNil(data)) {
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", "");

								if (dataCell.originalEvent.detail.columnKey !== "worktimeCode")
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeCode", "");

								$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", "");

								if (data.holiday == false) {
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", getText('KSU003_55'));
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeName", data.workTypeName);
								} else {
									self.checkClearTime = false;
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", getText('KSU003_55'));
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeName", data.workTypeName);
									ruler.replaceAt(index, [{ // xóa chart khi là ngày nghỉ
										type: "Flex",
										options: {
											id: `lgc` + index,
											start: -1,
											end: -1,
											lineNo: index
										}
									}]);

									return;
								}
								self.inputWorkInfo(index, dataCell, dataFixed, empId);
							} else {
								self.getEmpWorkFixedWorkInfo($("#extable-ksu003").exTable('dataSource', 'middle').body[index].worktypeCode,
									$("#extable-ksu003").exTable('dataSource', 'middle').body[index].worktimeCode, empId, index,"type");
								/*self.midContent.dataSource[index] = ({
									empId: empId, worktypeCode: $("#extable-ksu003").exTable('dataSource', 'middle').body[index].worktypeCode, worktypeName: "", worktimeCode: "", worktimeName: "",
									startTime1: "", endTime1: "", startTime2: "", endTime2: "", totalTime: "", breaktime: ""
								});
								$("#extable-ksu003").exTable("updateTable", "middle", self.headerMidExtable, self.midContent);*/
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeCode", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", getText('KSU003_55'));
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeName", "");
							}
						}).fail(function(error) {
							errorDialog({ messageId: error.messageId });
						}).always(function() {
						});
					}
					let columnKey = dataCell.originalEvent.detail.columnKey;
					if ((columnKey === "startTime1" || columnKey === "startTime2" ||
						columnKey === "endTime1" || columnKey === "endTime2") && self.checkUpdateTime.id == 0) {
						self.checkTimeInfo(dataMid.worktypeCode, dataMid.worktimeCode, dataMid.startTime1.trim(), dataMid.startTime2.trim(), dataMid.endTime1.trim(), dataMid.endTime2.trim());
						let timeConvert = self.convertTime(dataMid.startTime1.trim(), dataMid.endTime1.trim(), dataMid.startTime2.trim(), dataMid.endTime2.trim());
						self.employeeScheduleInfo.forEach((x, i) => {
							if (i === dataCell.originalEvent.detail.rowIndex) {
								if (columnKey === "startTime1") {
									x.startTime1 = timeConvert.start;
									dataFixed[0].workScheduleDto.startTime1 = x.startTime1;
									if(x.startTime1 == ""){
									return;
									}
								}
								if (columnKey === "startTime2") {
									x.startTime2 = timeConvert.start2;
									dataFixed[0].workScheduleDto.startTime2 = x.startTime2;
									if(x.startTime2 == ""){
									return;
									}
								}
								if (columnKey === "endTime1") {
									x.endTime1 = timeConvert.end;
									dataFixed[0].workScheduleDto.endTime1 = x.endTime1;
									if(x.endTime1 == ""){
									return;
									}
								}
								if (columnKey === "endTime2") {
									x.endTime2 = timeConvert.end2;
									dataFixed[0].workScheduleDto.endTime2 = x.endTime2;
									if(x.endTime2 == ""){
									return;
									}
								}
							}
						})
						if((timeConvert.start == "" && timeConvert.end != "") || (timeConvert.start != "" && timeConvert.end == "") 
						|| (timeConvert.start2 == "" && timeConvert.end2 != "") || (timeConvert.start2 != "" && timeConvert.end2 == "")) 
						return;
						// tính lại tổng time
						let lstTime : any = [],timeRangeLimit = ((self.timeRange * 60) / 5),totalBrkTime : any = null;
						self.lstBreakSum = [];
						lstTime = self.calcChartTypeTime(dataFixed[0], dataFixed[0].workScheduleDto.listBreakTimeZoneDto, 
						timeRangeLimit, lstTime, 1);
						for (let e = 0; e < dataFixed[0].workInfoDto.listTimeVacationAndType.length; e++) {
						let y = dataFixed[0].workInfoDto.listTimeVacationAndType[e];
						lstTime = self.calcChartTypeTime(dataFixed[0], y.timeVacation.timeZone, timeRangeLimit, lstTime);
						}
						lstTime = self.calcChartTypeTime(dataFixed[0], dataFixed[0].workInfoDto.shortTime, 
						timeRangeLimit, lstTime);
						
						lstTime = self.calcChartTypeTime(dataFixed[0], dataFixInfo[0].fixedWorkInforDto.overtimeHours, timeRangeLimit, lstTime);

						let totalTime = self.calcAllTime(dataFixed[0], lstTime,timeRangeLimit);
							
						totalBrkTime = self.calcAllBrk(lstTime);	
						totalBrkTime = totalBrkTime != null ? formatById("Clock_Short_HM", totalBrkTime * 5) : "";
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", totalBrkTime);
						if (!self.checkClearTime == false) {
							$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", totalTime != null ? totalTime : "");
						}
					}
				}
			});
		}

		/**
		 * startPage
		 */
		public startPage(): JQueryPromise<any> {
			block.grayout();
			let self = this, dfd = $.Deferred<any>();
			self.getData();
			self.hoverEvent(self.targetDate());
			dfd.resolve();
			block.clear();
			return dfd.promise();
		}

		public inputWorkInfo(index: number, dataCell: any, dataFixed: any, empId: string) {
			let self = this;
			self.getEmpWorkFixedWorkInfo($("#extable-ksu003").exTable('dataSource', 'middle').body[index].worktypeCode,
				$("#extable-ksu003").exTable('dataSource', 'middle').body[index].worktimeCode, empId, index, "time").done(() => {
					self.removeChart(index);
					if (dataCell.originalEvent.detail.value != "" &&
						dataCell.originalEvent.detail.value != null) {
						self.checkClearTime = true;

						let lstBrkTime = dataFixed[0].workScheduleDto.listBreakTimeZoneDto, totalBrkTime : any = null;
						
						let lstTime : any = [],timeRangeLimit = ((self.timeRange * 60) / 5);
						self.lstBreakSum = [];
						lstTime = self.calcChartTypeTime(dataFixed[0], dataFixed[0].workScheduleDto.listBreakTimeZoneDto, 
						timeRangeLimit, lstTime, 1);
						for (let e = 0; e < dataFixed[0].workInfoDto.listTimeVacationAndType.length; e++) {
						let y = dataFixed[0].workInfoDto.listTimeVacationAndType[e];
						lstTime = self.calcChartTypeTime(dataFixed[0], y.timeVacation.timeZone, timeRangeLimit, lstTime);
						}
						lstTime = self.calcChartTypeTime(dataFixed[0], dataFixed[0].workInfoDto.shortTime, 
						timeRangeLimit, lstTime);
						let dataFixInfo = _.filter(self.fixedWorkInformationDto, x => { return x.empId === empId });
						lstTime = self.calcChartTypeTime(dataFixed[0], dataFixInfo[0].fixedWorkInforDto.overtimeHours, timeRangeLimit, lstTime);

						let totalTime = self.calcAllTime(dataFixed[0], lstTime,timeRangeLimit);
							
						totalBrkTime = self.calcAllBrk(lstTime);	
						totalBrkTime = totalBrkTime != null ? formatById("Clock_Short_HM", totalBrkTime * 5) : "";
						
						let schedule: model.EmployeeWorkScheduleDto = dataFixed[0].workScheduleDto,
							fixed: model.FixedWorkInforDto = dataFixed[0].fixedWorkInforDto,
							info: model.EmployeeWorkInfoDto = dataFixed[0].workInfoDto;

						$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeCode", schedule.workTypeCode);
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeName", fixed.workTypeName);
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeCode", schedule.workTimeCode);
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", fixed.workTimeName);
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", formatById("Clock_Short_HM", (schedule.startTime1)));
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", formatById("Clock_Short_HM", (schedule.endTime1)));
						if (schedule.startTime2 != null)
							$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", formatById("Clock_Short_HM", (schedule.startTime2)));
						else
							$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", "");

						if (schedule.endTime2 != null)
							$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", formatById("Clock_Short_HM", (schedule.endTime2)));
						else
							$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", "");
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", totalTime);
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", totalBrkTime);

						self.dataScreen003A().employeeInfo[index].workScheduleDto.workTypeCode = schedule.workTypeCode;
						self.dataScreen003A().employeeInfo[index].workScheduleDto.workTimeCode = schedule.workTimeCode;
						self.dataScreen003A().employeeInfo[index].workScheduleDto.startTime1 = schedule.startTime1;
						self.dataScreen003A().employeeInfo[index].workScheduleDto.endTime1 = schedule.endTime1;
						self.dataScreen003A().employeeInfo[index].workScheduleDto.startTime2 = schedule.startTime2;
						self.dataScreen003A().employeeInfo[index].workScheduleDto.endTime2 = schedule.endTime2;
						let dataMid = $("#extable-ksu003").exTable('dataSource', 'middle').body[index];
						self.checkTimeInfo(dataMid.worktypeCode, dataMid.worktimeCode, dataMid.startTime1.trim(), 
						dataMid.startTime2.trim(), dataMid.endTime1.trim(), dataMid.endTime2.trim());

						if (lstBrkTime != null && lstBrkTime.length > 0) {
							self.dataScreen003A().employeeInfo[index].workScheduleDto.listBreakTimeZoneDto = lstBrkTime;
						}

						self.dataScreen003A().employeeInfo[index].workInfoDto.directAtr = info.directAtr;
						self.dataScreen003A().employeeInfo[index].workInfoDto.bounceAtr = info.bounceAtr;

						self.dataScreen003A().employeeInfo[index].fixedWorkInforDto.workTypeName = fixed.workTypeName;
						self.dataScreen003A().employeeInfo[index].fixedWorkInforDto.workTimeName = fixed.workTimeName;
						self.dataScreen003A().employeeInfo[index].fixedWorkInforDto.workType = fixed.workType;
						self.dataScreen003A().employeeInfo[index].fixedWorkInforDto.fixBreakTime = fixed.fixBreakTime;

						self.convertDataIntoExtable(index);

						let datafilter: Array<ITimeGantChart> = _.filter(self.dataOfGantChart, (x: any) => { return x.empId === empId });
						if (datafilter.length > 0) {
							//self.updateGantChart(datafilter, lineNo, fixedGc, lstBreak, indexS, indexF);
							self.addAllChart(datafilter, index, [], self.midDataGC, "");
							ruler.replaceAt(index, [
								...self.allGcShow
							]);
						}
						self.checkUpdateTime.name = "";
						self.checkUpdateTime.id = 0;
						return;
					}
				})
		}

		setDataToMidExtable(empId: string, schedule: model.EmployeeWorkScheduleDto, fixed: model.FixedWorkInforDto, totalTime: any, totalBrkTime: any) {
			let workTypeName = "", workTimeName = "";
			if (schedule == null) {
				workTimeName = getText('KSU003_55');
			}
			if (fixed != null) {
				if (fixed.workTypeName != null && fixed.workTypeName != "") {
					workTypeName = fixed.workTypeName;
				}
				if (fixed.workTimeName != null && fixed.workTimeName != "") {
					workTimeName = fixed.workTimeName;
				}
			}
			if (schedule.workTimeCode == null || schedule.workTimeCode == "") {
				workTypeName = getText('KSU003_55');
			} else if (schedule.workTypeCode != null && schedule.workTypeCode != "" && fixed != null
				&& (fixed.workTypeName == null || fixed.workTypeName == "")) {
				workTypeName = schedule.workTypeCode + getText('KSU003_54')
			}
			// set work time name
			if ((schedule.workTimeCode == null || schedule.workTimeCode == "")) {
				workTimeName = getText('KSU003_55');
			} else {
				if (fixed != null && (fixed.workTimeName == null || fixed.workTimeName == "")) {
					workTimeName = schedule.workTimeCode + getText('KSU003_54');
				}
			}
			$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeCode", schedule.workTypeCode);
			$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeName", workTypeName);
			$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeCode", schedule.workTimeCode);
			$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", workTimeName);
			$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", formatById("Clock_Short_HM", (schedule.startTime1)));
			$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", formatById("Clock_Short_HM", (schedule.endTime1)));

			if (schedule.startTime2 != null)
				$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", formatById("Clock_Short_HM", (schedule.startTime2)));
			if (schedule.endTime2 != null)
				$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", formatById("Clock_Short_HM", (schedule.endTime2)));

			$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", totalTime);
			$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", totalBrkTime);
		}

		removeChart(index: number) {
			ruler.replaceAt(index, [{ // xóa chart khi là ngày nghỉ
				type: "Flex",
				options: {
					id: `lgc` + index,
					start: -1,
					end: -1,
					lineNo: index
				}
			}]);
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
			self.dataScreen003AFirst(data003A);
			self.getInforFirt();
			let local: ILocalStore = {
				startTimeSort: self.checked(),
				showWplName: self.checkedName() == true,
				operationUnit: self.selectOperationUnit(),
				displayFormat: self.selectedDisplayPeriod(),
				showHide: self.indexBtnToLeft(),
				lstEmpIdSort: _.map(self.lstEmpId, (x: IEmpidName) => { return x.empId })
			}
			self.localStore(local);

			self.getWorkingByDate(self.targetDate(), 1).done(() => {
				self.convertDataIntoExtable();
				self.initExtableData();
			});
		}
		
		// ①<<ScreenQuery>> 初期起動の情報取得
		public getInforFirt() {
			let self = this;
			let dfd = $.Deferred<any>();
			let targetOrgDto = {
				unit: self.dataFromA().unit,
				workplaceId: self.dataFromA().workplaceId,
				workplaceGroupId: self.dataFromA().workplaceGroupId
			}
			service.getDataStartScreen(targetOrgDto)
				.done((data: model.GetInfoInitStartKsu003Dto) => {
					self.dataInitStartKsu003Dto(data);
					self.organizationName(self.dataInitStartKsu003Dto().displayInforOrganization.displayName);
					self.dataScreen003A().targetInfor = data.manageMultiDto.useATR;
					self.timeRange = self.dataInitStartKsu003Dto().byDateDto.dispRange == 0 ? 24 : 48;
					self.initDispStart = self.dataInitStartKsu003Dto().byDateDto.initDispStart;
					self.dataScreen003A().targetInfor = self.dataInitStartKsu003Dto().manageMultiDto.useATR;
					self.dataScreen003A().scheCorrection = self.dataInitStartKsu003Dto().functionControlDto != null
						? self.dataInitStartKsu003Dto().functionControlDto.changeableWorks : [0, 1, 2, 3];
				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
					dfd.reject();
				}).always(function() {
				});
		}

		// ②<<ScreenQuery>> 日付別勤務情報で表示する
		public getWorkingByDate(targetDate: any, check?: number): JQueryPromise<any> {
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
					if (self.checked() === "0") {
						let dataSort = self.sortEmpByTime(data);
						dataSort = dataSort.sort(function(a, b) {
							return (a.startTime != null ? a.startTime : Infinity) - (b.startTime != null ? b.startTime : Infinity);
						});
						self.lstEmpId = self.lstEmpId.sort(function(a: any, b: any) {
							return _.findIndex(dataSort, x => { return x.empId == a.empId }) - _.findIndex(dataSort, x => { return x.empId == b.empId });
						});
					}
					data = data.sort(function(a: any, b: any) {
						return _.findIndex(self.lstEmpId, x => { return x.empId == a.empId }) - _.findIndex(self.lstEmpId, x => { return x.empId == b.empId });
					});
					self.dataScreen003A().employeeInfo = data;
					self.dataScreen003AFirst().employeeInfo = data;
					self.fixedWorkInformationDto = _.map(self.dataScreen003A().employeeInfo, (z) => ({
						empId: z.empId,
						fixedWorkInforDto : z.fixedWorkInforDto
					}))
					self.employeeScheduleInfo = _.map(self.dataScreen003A().employeeInfo, (x) => ({
						empId: x.empId,
						startTime1: x.workScheduleDto != null && x.workScheduleDto.startTime1 != null ? x.workScheduleDto.startTime1 : "",
						endTime1: x.workScheduleDto != null && x.workScheduleDto.endTime1 != null ? x.workScheduleDto.endTime1 : "",
						startTime2: x.workScheduleDto != null && x.workScheduleDto.startTime2 != null ? x.workScheduleDto.startTime2 : "",
						endTime2: x.workScheduleDto != null && x.workScheduleDto.endTime2 != null ? x.workScheduleDto.endTime2 : "",
						listBreakTimeZoneDto: x.workScheduleDto != null &&
							x.workScheduleDto.listBreakTimeZoneDto != null ? x.workScheduleDto.listBreakTimeZoneDto : ""
					}));

					self.employeeScheduleInfo = self.employeeScheduleInfo.sort(function(a, b) {
						return _.findIndex(self.lstEmpId, x => { return x.empId == a.empId }) -
							_.findIndex(self.lstEmpId, x => { return x.empId == b.empId });
					});
					dfd.resolve(data);
				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
					dfd.reject();
				}).always(function() {
					block.clear();
				});
			return dfd.promise();
		}
		
		// 社員勤務予定と勤務固定情報を取得する
		public getEmpWorkFixedWorkInfo(workTypeCode: string, workTimeCode: string, empId?: string, index?: number, type?: string): JQueryPromise<any> {
			let self = this;
			let dfd = $.Deferred<any>(), indexEmp = 0;
			let targetOrgDto = [{
				workTypeCode: workTypeCode,
				workTimeCode: workTimeCode
			}]
			indexEmp = _.findIndex(self.dataScreen003A().employeeInfo, x => { return x.empId === empId });
			service.getEmpWorkFixedWorkInfo(targetOrgDto)
				.done((data: model.DisplayWorkInfoByDateDto) => {
					if (data.fixedWorkInforDto.workType == null) return;
					self.dataScreen003A().employeeInfo[indexEmp].fixedWorkInforDto = data.fixedWorkInforDto;
					self.dataScreen003A().employeeInfo[indexEmp].workScheduleDto = data.workScheduleDto;
					self.fixedWorkInformationDto[indexEmp].fixedWorkInforDto = data.fixedWorkInforDto;

					dfd.resolve(data);
				}).fail(function(error) {
					errorDialog({ messageId: error.messageId });
					ruler.replaceAt(index, [{ // xóa chart khi là ngày nghỉ
						type: "Flex",
						options: {
							id: `lgc` + index,
							start: -1,
							end: -1,
							lineNo: index
						}
					}]);
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", "");
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", "");
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", "");
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", "");
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeCode", "");
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", "");
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", "");
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", getText('KSU003_55'));
					$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeName", "");
				}).always(function() {
				});
			return dfd.promise();
		}

		// 勤務種類を変更する

		public sortEmpByTime(employeeInfo: Array<model.DisplayWorkInfoByDateDto>) {
			let dataSort = _.map(employeeInfo, (x: model.DisplayWorkInfoByDateDto) => ({
				empId: x.empId,
				startTime: (x.workScheduleDto != null && (x.workScheduleDto.startTime1 != null)) ? x.workScheduleDto.startTime1 : null
			}))

			return dataSort;
		}

		// 社員を並び替える
		public sortEmployee(value: any) {
			let self = this;
			let dataSort = self.sortEmpByTime(self.dataScreen003A().employeeInfo);
			let param = {
				ymd: self.targetDate(),
				lstEmpId: _.map(self.lstEmpId, (x: IEmpidName) => { return { empId: x.empId, code: x.code } })
			}
			if (value === '0') {
				dataSort = dataSort.sort(function(a: any, b: any) {
					return (a.startTime != null ? a.startTime : Infinity) - (b.startTime != null ? b.startTime : Infinity);
				});
				self.lstEmpId = self.lstEmpId.sort(function(a: any, b: any) {
					return _.findIndex(dataSort, x => { return x.empId == a.empId }) - _.findIndex(dataSort, x => { return x.empId == b.empId });
				});
				self.localStore().lstEmpIdSort = self.lstEmpId;
				storage.setItemAsJson(self.KEY, self.localStore());
				self.destroyAndCreateGrid(self.lstEmpId, 1);
			} else {
				service.sortEmployee(param)
					.done((data: Array<model.DisplayWorkInfoByDateDto>) => {
						self.lstEmpId = self.lstEmpId.sort(function(a: any, b: any) {
							return _.findIndex(data, x => { return x == a.empId }) - _.findIndex(data, x => { return x == b.empId });
						});
						self.localStore().lstEmpIdSort = self.lstEmpId;
						storage.setItemAsJson(self.KEY, self.localStore());
						self.destroyAndCreateGrid(self.lstEmpId, 1);
					}).fail(function(error) {
						errorDialog({ messageId: error.messageId });
					}).always(function() {
					});
			}
			self.localStore().startTimeSort = value;
			storage.setItemAsJson(self.KEY, self.localStore());
		}
		
		// Tạo data để truyền vào ExTable
		public convertDataIntoExtable(index?: number) {

			let self = this, disableDs: any = [], leftDs: any = [], middleDs: any = [],
				timeGantChart: Array<ITimeGantChart> = [], typeOfTime: string, startTimeArr: any = [], lstTime: any = [];
			let gcFixedWorkTime: Array<model.IFixedFlowFlexTime> = [],
				gcBreakTime: Array<model.IBreakTime> = [],
				gcOverTime: Array<model.IOverTime> = [],
				gcSupportTime: any = null,
				gcFlowTime: Array<model.IFixedFlowFlexTime> = [],
				gcFlexTime: Array<model.IFixedFlowFlexTime> = [],
				gcCoreTime: Array<model.ICoreTime> = [],
				gcHolidayTime: Array<model.IHolidayTime> = [],
				gcShortTime: Array<model.IShortTime> = [];
			_.forEach(_.isNil(index) ? self.dataScreen003A().employeeInfo : [self.dataScreen003A().employeeInfo[index]], (schedule: model.DisplayWorkInfoByDateDto, index) => {
				gcFixedWorkTime = [], gcBreakTime = [], gcOverTime = [], gcSupportTime = null,
					gcFlowTime = [], gcFlexTime = [], gcCoreTime = [], gcHolidayTime = [],
					gcShortTime = [], typeOfTime = "", self.lstBreakSum = [];
				let color = self.setColorEmployee(schedule.workInfoDto.isNeedWorkSchedule, schedule.workInfoDto.isCheering);
				let colorA6 = self.setColorWorkingInfo(schedule.empId, schedule.workInfoDto.isConfirmed
					, schedule.workScheduleDto != null ? schedule.workScheduleDto : null, schedule.workInfoDto.isNeedWorkSchedule);
				if (schedule.workInfoDto.isConfirmed == 0 && schedule.workInfoDto.isNeedWorkSchedule == 0) {
					disableDs.push({
						empId: schedule.empId,
						color: "#ddddd2",
					});
				}
				leftDs.push({
					empId: schedule.empId,
					color: color,
					colorA6: colorA6
				});

				let startTime1 = "", startTime2 = "", endTime1 = "", endTime2 = "";
				if (schedule.workScheduleDto != null) {
					startTime1 = schedule.workScheduleDto.startTime1 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.startTime1) : "",
						startTime2 = schedule.workScheduleDto.startTime2 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.startTime2) : "",
						endTime1 = schedule.workScheduleDto.endTime1 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.endTime1) : "",
						endTime2 = schedule.workScheduleDto.endTime2 != null ? formatById("Clock_Short_HM", schedule.workScheduleDto.endTime2) : "";
				}
				let workTypeName = "", workTimeName = "";
				if (schedule.fixedWorkInforDto != null) {
					if (schedule.fixedWorkInforDto.workTypeName != null && schedule.fixedWorkInforDto.workTypeName != "") {
						workTypeName = schedule.fixedWorkInforDto.workTypeName;
					}
					if (schedule.fixedWorkInforDto.workTimeName != null && schedule.fixedWorkInforDto.workTimeName != "") {
						workTimeName = schedule.fixedWorkInforDto.workTimeName;
					}
				}
				if (schedule.workScheduleDto == null && schedule.fixedWorkInforDto == null) {
					workTimeName = getText('KSU003_55');
				}
				if (schedule.workScheduleDto != null && schedule.fixedWorkInforDto != null) {
					// Bind *worktypecode - *worktimecode
					if (schedule.workScheduleDto.workTypeCode == null || schedule.workScheduleDto.workTypeCode == "") {
						workTypeName = getText('KSU003_55');
					} else if (schedule.workScheduleDto.workTypeCode != null && schedule.workScheduleDto.workTypeCode != "" && schedule.fixedWorkInforDto != null
						&& (schedule.fixedWorkInforDto.workTypeName == null || schedule.fixedWorkInforDto.workTypeName == "")) {
						workTypeName = schedule.workScheduleDto.workTypeCode + getText('KSU003_54')
					}
					// set work time name
					if (schedule.workScheduleDto.workTimeCode == null || schedule.workScheduleDto.workTimeCode == "") {
						workTimeName = getText('KSU003_55');
					} else {
						if (schedule.fixedWorkInforDto != null && (schedule.fixedWorkInforDto.workTimeName == "" || schedule.fixedWorkInforDto.workTimeName == null)) {
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
							endTimeRange: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.endTimeRange1 : null

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
								endTimeRange: schedule.fixedWorkInforDto != null ? schedule.fixedWorkInforDto.endTimeRange2 : null
							});
							startTimeArr.add(schedule.workScheduleDto.startTime2);
						}
						typeOfTime = "Fixed"
					}
				}

				// Tính tổng thời gian làm việc
				let lstBreak = 1, lstTime: any = [], timeRangeLimit = ((self.timeRange * 60) / 5);
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

						// Tính tổng BREAK-TIME khi khởi động
						lstTime = self.calcChartTypeTime(schedule, schedule.workScheduleDto.listBreakTimeZoneDto, timeRangeLimit, lstTime, lstBreak);
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
					// Tính tổng thời gian làm thêm
					lstTime = self.calcChartTypeTime(schedule, schedule.fixedWorkInforDto.overtimeHours, timeRangeLimit, lstTime);

					// Thời gian lưu động 
					// 勤務固定情報　dto．Optional<勤務タイプ>== 流動勤務&&社員勤務情報　dto．応援か＝＝時間帯応援元　or 応援ではない
					if (schedule.fixedWorkInforDto.workType == WorkTimeForm.FLOW
						&& (schedule.workInfoDto.isCheering == SupportAtr.TIMEZONE_SUPPORTER || schedule.workInfoDto.isCheering == SupportAtr.NOT_CHEERING)) {
						// 社員勤務予定dto.Optional<開始時刻1>, 社員勤務予定dto.Optional<終了時刻1>
						gcFlowTime.push({
							timeNo: 1,
							empId: schedule.workInfoDto.employeeId,
							workType: schedule.fixedWorkInforDto.workType,
							color: "#ffc000",
							isCheering: schedule.workInfoDto.isCheering,
							startTime: schedule.workScheduleDto.startTime1,
							endTime: schedule.workScheduleDto.endTime1,
							startTimeRange: schedule.fixedWorkInforDto.startTimeRange1,
							endTimeRange: schedule.fixedWorkInforDto.endTimeRange1

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
								endTimeRange: schedule.fixedWorkInforDto.endTimeRange2
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
							endTimeRange: schedule.fixedWorkInforDto.endTimeRange1
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
					// Tính tổng thời gian holiday time
					lstTime = self.calcChartTypeTime(schedule, schedule.workInfoDto.listTimeVacationAndType, timeRangeLimit, lstTime);

					// Tính tổng thời gian holiday time
					for (let e = 0; e < schedule.workInfoDto.listTimeVacationAndType.length; e++) {
						let y = schedule.workInfoDto.listTimeVacationAndType[e];
						lstTime = self.calcChartTypeTime(schedule, y.timeVacation.timeZone, timeRangeLimit, lstTime);
					}

					// Thời gian chăm sóc / giữ trẻ (short time)
					// 社員勤務情報　dto．Optional<育児介護短時間> が存在する
					if (schedule.workInfoDto.shortTime != null && schedule.workInfoDto.shortTime.length > 0) {
						gcShortTime.push({
							// 社員勤務情報　dto > 社員ID, Optional<Map<時間休暇種類, 時間休暇>>
							empId: schedule.workInfoDto.employeeId,
							color: "#6fa527",
							listShortTime: schedule.workInfoDto.shortTime
						})
					}
					// Tính tổng thời gian short time
					lstTime = self.calcChartTypeTime(schedule, schedule.workInfoDto.shortTime, timeRangeLimit, lstTime);
				}
				
				// Tổng thời gian break time
				let brkTotal : any = 0;
				brkTotal = self.calcAllBrk(lstTime);

				self.totalTimeWork = self.calcAllTime(schedule, lstTime, timeRangeLimit);
				brkTotal = formatById("Clock_Short_HM", (brkTotal * 5));
				self.timeBrkText(brkTotal);
				if (schedule.workScheduleDto != null) {
					middleDs.push({
						empId: schedule.empId, cert: getText('KSU003_22'),
						worktypeCode: schedule.workScheduleDto.workTypeCode == null ? "" : schedule.workScheduleDto.workTypeCode,
						worktype: workTypeName,
						worktimeCode: schedule.workScheduleDto.workTimeCode == null ? "" : schedule.workScheduleDto.workTimeCode,
						worktime: workTimeName,
						startTime1: startTime1, endTime1: endTime1,
						startTime2: startTime2, endTime2: endTime2,
						totalTime: self.totalTimeWork, breaktime: self.timeBrkText(),
						color: colorA6
					});
				} else {
					middleDs.push({
						empId: schedule.empId, cert: getText('KSU003_22'), worktypeCode: "",
						worktype: workTypeName, worktimeCode: "", worktime: workTimeName,
						startTime1: "", endTime1: "",
						startTime2: "", endTime2: "",
						totalTime: "", breaktime: "", color: ""
					});
				}
				// dữ liệu của chart
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
			// tạo dữ liệu để truyền vào init extable
			self.leftDs = leftDs;
			self.disableDs = disableDs;
			self.dataOfGantChart = timeGantChart;
			self.midDataGC = middleDs;
			startTimeArr = _.sortBy(startTimeArr, [function(o: any) { return o; }]);
		}

		public initExtableData() {
			let self = this;
			setTimeout(() => {
				self.initExtableChart(self.dataOfGantChart, self.leftDs, self.midDataGC, self.disableDs);
				self.showHide();
				$("#extable-ksu003").exTable("scrollBack", 0, { h: Math.floor(self.initDispStart * 12) });
			}, 200)
		}

		destroyAndCreateGrid(lstId: any, check: any) {
			let self = this;
			let leftDs: any = [];
			leftDs.push({
				empId: _.map(lstId, (x: IEmpidName) => { return x.empId }),
				color: ""
			});
			$("#extable-ksu003").children().remove();
			$("#extable-ksu003").removeData();

			if (check == 1)
				self.initExtableData();
			else {
				self.getWorkingByDate(self.targetDate(), 1).done(() => {
					self.convertDataIntoExtable();
					self.initExtableData();
				});
			}
		}

		// Khởi tạo EXTABLE-GANTCHART
		initExtableChart(timeGantChart: Array<ITimeGantChart>, leftDs: any, midData: any, disableDs: any): void {
			block.grayout();
			let self = this;
			let displayRange = self.timeRange, totalBreakTime = "0:00";

			let middleContentDeco: any = [], leftContentDeco: any = [], detailContentDeco: any = [];

			// phần leftMost
			let leftmostColumns = [],
				leftmostHeader = {},
				leftmostContent = {}, disableDSFilter: any = [];

			leftmostColumns = [{
				key: "empName",
				icon: { for: "body", class: "icon-leftmost", width: "25px" },
				headerText: getText('KSU003_20'), width: "160px", control: "link",
				css: { whiteSpace: "pre" }
			}, {
				key: "cert", headerText: getText('KSU003_21'), width: "40px", control: "button", handler: function(e: any) {
					self.openKdl045Dialog(e.empId)
				}
			}];

			leftmostHeader = {
				columns: leftmostColumns,
				rowHeight: "33px",
				width: "200px"
			};

			let leftmostDs = [], middleDs = [];
			for (let i = 0; i < self.lstEmpId.length; i++) {
				let dataLeft: any = _.filter(self.lstEmpId, (x: any) => { return x.empId === self.lstEmpId[i].empId }),
					datafilter = _.filter(midData, (x: any) => { return x.empId === self.lstEmpId[i].empId }),
					dataMid = datafilter[0],
					eName: any = dataLeft[0].code + " " + dataLeft[0].name;
				totalBreakTime = _.isNil(dataMid) ? "" : dataMid.breaktime;
				let leftDSFilter = _.filter(leftDs, (x: any) => { return x.empId === self.lstEmpId[i].empId });
				disableDSFilter = _.filter(disableDs, (x: any) => { return x.empId === self.lstEmpId[i].empId }); // list không dùng schedule

				leftmostDs.push({ empId: self.lstEmpId[i].empId, empName: eName, cert: getText('KSU003_22') });
				if(_.isEmpty(leftDSFilter)){
					leftContentDeco.push(new CellColor("empName", leftDSFilter[0].empId, leftDSFilter[0].color)); // set màu cho emp name
					leftContentDeco.push(new CellColor("cert", leftDSFilter[0].empId, leftDSFilter[0].color));
				}
				
				let checkColor = ({
					worktypeCode: 1,
					worktypeName: 1,
					worktimeCode: 1,
					worktimeName: 1,
					startTime1: 1,
					endTime1: 1,
					startTime2: 1,
					endTime2: 1,
					totalTime: 1,
					breaktime: 1
				})
				if (disableDSFilter.length > 0) {
					leftContentDeco.push(new CellColor("empName", disableDSFilter[0].empId, "xseal", leftDSFilter[0].color)); // set màu cho emp name khi bị dis
					leftContentDeco.push(new CellColor("cert", disableDSFilter[0].empId, "xseal", leftDSFilter[0].color));
					middleContentDeco.push(new CellColor("worktypeCode", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("worktypeName", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("worktimeCode", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("worktimeName", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("startTime1", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("endTime1", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("startTime2", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("endTime2", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("totalTime", disableDSFilter[0].empId, "xseal"));
					middleContentDeco.push(new CellColor("breaktime", disableDSFilter[0].empId, "xseal"));
					$("#extable-ksu003").exTable("cellValue", "middle", disableDSFilter[0].empId, "worktimeName", getText('KSU003_55'));
					// set dis for detail DS
					for (let z = self.dataInitStartKsu003Dto().byDateDto.dispStart; z <= (displayRange + self.dataInitStartKsu003Dto().byDateDto.dispStart); z++) {
						detailContentDeco.push(new CellColor(z.toString(), disableDSFilter[0].empId, disableDSFilter[0].color));
					}
				} else {
					let isNeedWorkSchedule = self.dataScreen003A().employeeInfo[i].workInfoDto.isNeedWorkSchedule,
					canModified = self.dataScreen003A().canModified, // 修正可能
					isConfirmed = self.dataScreen003A().employeeInfo[i].workInfoDto.isConfirmed; // 確定済みか
					// set ẩn hiện A6, A7, A8
					
					if (isNeedWorkSchedule != 1) { // ※2 & ※3
						middleContentDeco.push(new CellColor("worktypeCode", self.lstEmpId[i].empId, "xseal", 0)); // 4 
						middleContentDeco.push(new CellColor("worktypeName", self.lstEmpId[i].empId, "xseal", 0)); // 4
						middleContentDeco.push(new CellColor("worktimeName", self.lstEmpId[i].empId, "xseal", 0)); // 4
						checkColor.worktypeCode = 0;
						checkColor.worktypeName = 0;
						checkColor.worktimeName = 0;
						self.lstDis.push({
							empId: self.lstEmpId[i].empId
						})

					} else {// [※2]=〇

						if (self.dataScreen003A().employeeInfo[i].fixedWorkInforDto != null &&
							self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType != null &&
							self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType === WorkTimeForm.FIXED) { // 10

						}
						if (canModified == 0 || isConfirmed == 1) { // [※3]=〇 ※4 x x 確定済みか
							middleContentDeco.push(new CellColor("worktypeCode", self.lstEmpId[i].empId, "xseal", 0));
							middleContentDeco.push(new CellColor("worktypeName", self.lstEmpId[i].empId, "xseal", 0));
							middleContentDeco.push(new CellColor("worktimeName", self.lstEmpId[i].empId, "xseal", 0));
							middleContentDeco.push(new CellColor("worktimeCode", self.lstEmpId[i].empId, "xseal", 0));
							checkColor.worktypeCode = 0;
							checkColor.worktypeName = 0;
							checkColor.worktimeName = 0;
							checkColor.worktimeCode = 0;
							self.lstDis.push({
								empId: self.lstEmpId[i].empId
							})
						}

						if (canModified == 1) { // [※4]=〇 // 修正可能
						if (_.isNil(dataMid.worktimeCode) || dataMid.worktimeCode == "") {
									middleContentDeco.push(new CellColor("worktimeCode", self.lstEmpId[i].empId, "xseal", 0)); // ※9 x
									checkColor.worktimeCode = 0;
									self.lstDis.push({
										empId: self.lstEmpId[i].empId
									})
						}
						if (self.dataScreen003A().employeeInfo[i].fixedWorkInforDto! = null && self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType != null) {
							if (self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType == 0 || // [※5]=〇
								self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType == 1 ||
								self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType == 2) {
								if (self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType != null &&
									self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.workType === WorkTimeForm.FLEX) { // ※7 x
									middleContentDeco.push(new CellColor("startTime2", self.lstEmpId[i].empId, "xseal", 0));
									middleContentDeco.push(new CellColor("endTime2", self.lstEmpId[i].empId, "xseal", 0));
									checkColor.startTime2 = 0;
									checkColor.endTime2 = 0;
								}
							} else {
								middleContentDeco.push(new CellColor("startTime2", self.lstEmpId[i].empId, "xseal", 0));
								middleContentDeco.push(new CellColor("endTime2", self.lstEmpId[i].empId, "xseal", 0));
								checkColor.startTime2 = 0;
								checkColor.endTime2 = 0;
							}
						}
						} else {

							middleContentDeco.push(new CellColor("startTime2", self.lstEmpId[i].empId, "xseal", 0));
							middleContentDeco.push(new CellColor("endTime2", self.lstEmpId[i].empId, "xseal", 0));
							checkColor.startTime2 = 0;
							checkColor.endTime2 = 0;

							middleContentDeco.push(new CellColor("startTime1", self.lstEmpId[i].empId, "xseal", 0));
							middleContentDeco.push(new CellColor("endTime1", self.lstEmpId[i].empId, "xseal", 0));
							checkColor.startTime1 = 0;
							checkColor.endTime1 = 0;

							middleContentDeco.push(new CellColor("worktimeCode", self.lstEmpId[i].empId, "xseal", 0)); // ※9 x
							checkColor.worktimeCode = 0;
							self.lstDis.push({
								empId: self.lstEmpId[i].empId
							});
						}
						if (self.dataScreen003A().employeeInfo[i].fixedWorkInforDto != null &&
							self.dataScreen003A().employeeInfo[i].fixedWorkInforDto.fixBreakTime === 0) { // ※8
							middleContentDeco.push(new CellColor("breaktime", disableDSFilter[0].empId, "xseal", 0));
							checkColor.breaktime = 0;
						}
					}

					if (dataMid.color != "" && dataMid.color !== null && self.dataScreen003A().employeeInfo[i].workInfoDto.isNeedWorkSchedule == 1) {
						if (checkColor.worktypeCode != 0)
							middleContentDeco.push(new CellColor("worktypeCode", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.workTypeColor));

						if (checkColor.worktypeName != 0)
							middleContentDeco.push(new CellColor("worktypeName", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.workTypeColor));

						if (checkColor.worktimeCode != 0)
							middleContentDeco.push(new CellColor("worktimeCode", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.workTimeColor));

						if (checkColor.worktimeName != 0)
							middleContentDeco.push(new CellColor("worktimeName", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.workTimeColor));

						if (checkColor.startTime1 != 0)
							middleContentDeco.push(new CellColor("startTime1", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.startTime1Color));

						if (checkColor.endTime1 != 0)
							middleContentDeco.push(new CellColor("endTime1", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.endTime1Color));

						if (checkColor.startTime2 != 0)
							middleContentDeco.push(new CellColor("startTime2", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.startTime2Color));

						if (checkColor.endTime2 != 0)
							middleContentDeco.push(new CellColor("endTime2", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.endTime2Color));

						if (checkColor.breaktime != 0)
							middleContentDeco.push(new CellColor("breaktime", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.breakTimeColor));

						if (checkColor.totalTime != 0)
							middleContentDeco.push(new CellColor("totalTime", self.lstEmpId[i].empId,
								dataMid.color.workingInfoColor === "#eccefb" ? "#eccefb" : dataMid.color.totalTimeColor));
					}
					
					if(self.checkDisByDate == false){
					middleContentDeco.push(new CellColor("worktypeCode", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("worktypeName", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("worktimeCode", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("worktimeName", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("startTime1", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("endTime1", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("startTime2", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("endTime2", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("totalTime", self.lstEmpId[i].empId, "xseal"));
					middleContentDeco.push(new CellColor("breaktime", self.lstEmpId[i].empId, "xseal"));
					}
				}

				middleDs.push({
					empId: dataMid.empId, worktypeCode: dataMid.worktypeCode + " ",
					worktypeName: dataMid.worktype, worktimeCode: dataMid.worktimeCode + " ", worktimeName: disableDSFilter.length > 0 ? "" : dataMid.worktime,
					startTime1: dataMid.startTime1 + " ", endTime1: dataMid.endTime1+ " ",
					startTime2: dataMid.startTime2+ " ", endTime2: dataMid.endTime2+ " ",
					totalTime: dataMid.totalTime, breaktime: totalBreakTime === "" ? "0:00" : totalBreakTime
				});
			}

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
						{ headerText: "", key: "worktypeCode", width: "40px", handlerType: "input", dataType: "text", primitiveValue: "WorkTypeCode", required : true },
						{
							headerText: "", key: "worktypeName", width: "38px", control: "link", primitiveValue: "WorkTypeName", css: { whiteSpace: "pre" }, handler: function(e: any) {
								self.openKdl003Dialog(e.worktypeCode, e.worktimeCode, e.empId);
							}
						}]
				},
				{
					headerText: getText('KSU003_25'), group: [
						{ headerText: "", key: "worktimeCode", width: "40px", handlerType: "input", dataType: "text", primitiveValue: "WorkTimeCode", required : true },
						{
							headerText: "", key: "worktimeName", width: "38px", control: "link", primitiveValue: "WorkTimeName", css: { whiteSpace: "pre" }, handler: function(e: any) {
								self.openKdl003Dialog(e.worktypeCode, e.worktimeCode, e.empId);
							}
						}]
				},
				{
					headerText: getText('KSU003_27'), group: [
						{ headerText: "", key: "startTime1", width: "40px", handlerType: "input", dataType: "duration", primitiveValue: "TimeWithDayAttr", required : true }]
				},
				{
					headerText: getText('KSU003_28'), group: [
						{ headerText: "", key: "endTime1", width: "40px", handlerType: "input", dataType: "duration", primitiveValue: "TimeWithDayAttr", required : true }]
				},
				{
					headerText: getText('KSU003_29'), group: [
						{ headerText: "", key: "startTime2", width: "40px", handlerType: "input", dataType: "duration", primitiveValue: "TimeWithDayAttr", required : true }]
				},
				{
					headerText: getText('KSU003_30'), group: [
						{ headerText: "", key: "endTime2", width: "40px", handlerType: "input", dataType: "duration", primitiveValue: "TimeWithDayAttr", required : true }]
				},
				{
					headerText: getText('KSU003_31'), group: [
						{ headerText: "", key: "totalTime", width: "40px", dataType: "duration", primitiveValue: "TimeWithDayAttr", required : true }]
				},
				{
					headerText: getText('KSU003_32'), group: [
						{ headerText: "", key: "breaktime", width: "35px", dataType: "duration", primitiveValue: "TimeWithDayAttr", required : true }]
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
			for (let y = self.dataInitStartKsu003Dto().byDateDto.dispStart; y <= (displayRange + self.dataInitStartKsu003Dto().byDateDto.dispStart); y++) {
				if (y == self.dataInitStartKsu003Dto().byDateDto.dispStart) {
					detailColumns.push({ key: "empId", width: "0px", headerText: "ABC", visible: false });
				} else {
					detailColumns.push({ key: (y - 1).toString(), width: width });
				}
			}
			// Phần detail
			detailHeaderDs = [{ empId: "" }];
			let detailHeaders = {};
			for (let y = self.dataInitStartKsu003Dto().byDateDto.dispStart; y <= (displayRange + self.dataInitStartKsu003Dto().byDateDto.dispStart); y++) {
				detailHeaderDs[0][y] = y.toString();
				detailHeaders[y] = "";
			}

			detailHeader = {
				columns: detailColumns,
				dataSource: detailHeaderDs,
				rowHeight: "33px",
				width: "966px"
			};

			let detailContentDs = [];
			for (let z = 0; z < self.lstEmpId.length; z++) {
				let datafilter = _.filter(timeGantChart, (x: any) => { return x.empId === self.lstEmpId[z].empId });
				detailContentDs.push({ empId: datafilter[0].empId, ...detailHeaders });
			}

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
				errorMessagePopup: true,
				windowXOccupation: 40,
				windowYOccupation: 200
			}).LeftmostHeader(leftmostHeader).LeftmostContent(leftmostContent)
				.MiddleHeader(middleHeader).MiddleContent(middleContent)
				.DetailHeader(detailHeader).DetailContent(detailContent);

			extable.create();
			ruler = extable.getChartRuler();
			self.addTypeOfChart(ruler);

			let lstBreakTime: any = [], lstTimeChart: any = [], totalTimeBrk = 0, dfd = $.Deferred();;
			// (5' ~ 3.5 pixel ~ 12 khoảng trong 60', 10' ~ 7 ~ 6, 15' ~ 10.5 ~ 4, 30' ~ 21 ~ 2, 60' ~ 42 ~ 1)	
			// unitToPx = khoảng pixel theo số phút 
			// start theo pixel = unitToPx * start * (khoảng-pixel/ phút)
			// end theo pixel = unitToPx * end * (khoảng-pixel/ phút)
			for (let i = 0; i < self.lstEmpId.length; i++) {
				let datafilter: Array<ITimeGantChart> = _.filter(timeGantChart, (x: any) => { return x.empId === self.lstEmpId[i].empId });
				self.addAllChart(datafilter, i, lstBreakTime, midData, "");
			}
			// Thay đổi gant chart khi thay đổi giờ
			let recharge = function(detail: any) {
				let index = detail.rowIndex, dataMid = $("#extable-ksu003").exTable('dataSource', 'middle').body[index];
				let empId = self.lstEmpId[detail.rowIndex].empId, time = null, timeChart: any = null, timeChart2: any = null;
				lstTimeChart = _.filter(self.allTimeChart, (x: any) => { return x.empId === empId })

				if (lstTimeChart.length > 0) {
					if (detail.columnKey === "startTime1" || detail.columnKey === "endTime1" || detail.columnKey === "startTime2" || detail.columnKey === "endTime2")
						time = duration.parseString(detail.value).toValue();
					timeChart = lstTimeChart[0].timeChart;
					timeChart2 = lstTimeChart[0].timeChart2;
					if (detail.columnKey === "startTime1") {
						ruler.extend(detail.rowIndex, `lgc${detail.rowIndex}`, Math.floor(time / 5));
						if(time == "") return;
					} else if (detail.columnKey === "endTime1") {
						ruler.extend(detail.rowIndex, `lgc${detail.rowIndex}`, null, Math.floor(time / 5));
						if(time == "") return;
					} else if (detail.columnKey === "startTime2" && timeChart2 != null) {
						ruler.extend(detail.rowIndex, `rgc${detail.rowIndex}`, Math.floor(time / 5));
						if(time == "") return;
					} else if (detail.columnKey === "endTime2" && timeChart2 != null) {
						ruler.extend(detail.rowIndex, `rgc${detail.rowIndex}`, null, Math.floor(time / 5));
						if(time == "") return;
					}
				}
			};

			$("#extable-ksu003").on("extablecellupdated", (e: any) => {
				recharge(e.detail);
			});

			$("#extable-ksu003").on("extablecellretained", (e: any) => {
				recharge(e.detail);
			});

			$("#hr-row2").css("width", window.innerWidth - 40 + 'px');
			if (window.innerWidth == 1280) {
				$("#note-sort").css("margin-left", "1171px");
			}
			if (window.innerWidth == 1320) {
				$("#note-sort").css("margin-left", "1245px");
				if (navigator.userAgent.indexOf("Chrome") == -1) {

				}
			}

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
			block.clear();
		}

		/** ADD-CHART-ZONE */
		addAllChart(datafilter: Array<ITimeGantChart>, i: number, lstBreakTime: any, midData: any, screen: string, lstBrkNew?: any) {
			let self = this, fixedGc: any = [], totalBreakTimeNew: any = 0, timeRangeLimit = Math.floor((self.timeRange * 60) / 5);
			let timeChart: any = null, timeChart2: any = null, lgc = null, rgc = null, timeChartOver: any = null, timeChartCore: any = null,
				timeChartBrk: any = null, timeChartHoliday: any = null, timeChartShort: any = null, indexLeft = 0, indexRight = 0;
			let timeMinus: any = [], timeMinus2: any = [];
			if (datafilter != null) {
				if (datafilter[0].typeOfTime != "" || datafilter[0].typeOfTime != null) {
					// add chart for FIXED-TIME - thời gian cố định
					if (datafilter[0].typeOfTime === "Fixed" || datafilter[0].gcFixedWorkTime.length > 0) {
						let fixed = datafilter[0].gcFixedWorkTime;
						timeChart = self.convertTimeToChart(fixed[0].startTime, fixed[0].endTime);
						timeMinus.push({
							startTime: fixed[0].startTime,
							endTime: fixed[0].endTime
						})
						let limitTime = self.checkLimitTime(fixed, timeRangeLimit, 0)
						if (timeMinus[0].startTime < timeMinus[0].endTime && (self.timeRange === 24 && timeMinus[0].startTime < 1440 && timeMinus[0].startTime != null || self.timeRange === 48 && timeMinus[0].startTime < 2880 && timeMinus[0].startTime != null)) {
							lgc = ruler.addChartWithType("Fixed", {
								id: `lgc${i}`,
								lineNo: i,
								start: self.checkTimeOfChart(timeChart.startTime, timeRangeLimit),
								end: self.checkTimeOfChart(timeChart.endTime, timeRangeLimit),
								limitStartMin: limitTime.limitStartMin,
								limitStartMax: limitTime.limitStartMax,
								limitEndMin: limitTime.limitEndMin,
								limitEndMax: limitTime.limitEndMax,
								resizeFinished: (b: any, e: any, p: any) => {
									console.log("test");
								},
								dropFinished: (b: any, e: any) => {
									console.log("test");
								}
							});
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "Fixed", `lgc${i}`, timeChart, i, null, 0, 9999, 0, 9999));
							indexLeft = indexLeft++;
						}

						if (fixed.length > 1 && fixed[1].startTime != null && fixed[1].endTime != null) {
							timeChart2 = self.convertTimeToChart(fixed[1].startTime, fixed[1].endTime);
							timeMinus2.push({
								startTime: fixed[1].startTime,
								endTime: fixed[1].endTime
							})
							//lgc = self.addChartWithTypes(ruler, "Fixed", `lgc${i}`, timeChart, i);
							let limitTime = self.checkLimitTime(fixed, timeRangeLimit, 1)
							if (timeMinus[0].startTime < timeMinus[0].endTime && (self.timeRange === 24 && timeMinus2[0].startTime < 1440 && timeMinus2[0].startTime != null || 
							self.timeRange === 48 && timeMinus2[0].startTime < 2880 && timeMinus2[0].startTime != null)) {
								rgc = ruler.addChartWithType("Fixed", {
									id: `rgc${i}`,
									lineNo: i,
									start: self.checkTimeOfChart(timeChart2.startTime, timeRangeLimit),
									end: self.checkTimeOfChart(timeChart2.endTime, timeRangeLimit),
									limitStartMin: limitTime.limitStartMin,
									limitStartMax: limitTime.limitStartMax,
									limitEndMin: limitTime.limitEndMin,
									limitEndMax: limitTime.limitEndMax,
									resizeFinished: (b: any, e: any, p: any) => {
										console.log("test");
									},
									dropFinished: (b: any, e: any) => {
										console.log("test");
									}
								});
							}

							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "Fixed", `rgc${i}`, timeChart2, i, null, 0, 9999, 0, 9999));
							indexRight = indexRight++;
						}
					}
					// add chart for CHANGEABLE-TIME - thời gian lưu động
					if (datafilter[0].typeOfTime === "Changeable" || datafilter[0].gcFlowTime.length > 0) {
						let changeable = datafilter[0].gcFlowTime
						timeChart = self.convertTimeToChart(changeable[0].startTime, changeable[0].endTime);
						timeMinus.push({
							startTime: changeable[0].startTime,
							endTime: changeable[0].endTime
						})
						let limitTime = self.checkLimitTime(changeable, timeRangeLimit, 0)
						
						if (timeMinus[0].startTime < timeMinus[0].endTime && (self.timeRange === 24 && timeMinus[0].startTime < 1440 && timeMinus[0].startTime != null || 
							self.timeRange === 48 && timeMinus[0].startTime < 2880 && timeMinus[0].startTime != null)) {
								lgc = ruler.addChartWithType("Changeable", {
							id: `lgc${i}`,
							lineNo: i,
							start: self.checkTimeOfChart(timeChart.startTime, timeRangeLimit),
							end: self.checkTimeOfChart(timeChart.endTime, timeRangeLimit),
							limitStartMin: limitTime.limitStartMin,
							limitStartMax: limitTime.limitStartMax,
							limitEndMin: limitTime.limitEndMin,
							limitEndMax: limitTime.limitEndMax,
							resizeFinished: (b: any, e: any, p: any) => {
								console.log("test");
							},
							dropFinished: (b: any, e: any) => {
								console.log("test");
							}
						});

						fixedGc.push(self.addChartWithType045(datafilter[0].empId, "Changeable", `lgc${i}`, timeChart, i, null, 0, 9999, 0, 9999));
						indexLeft = indexLeft++;
							}
						
						if (changeable.length > 1 && changeable[1].startTime != null && changeable[1].endTime != null) {
							timeChart2 = self.convertTimeToChart(changeable[1].startTime, changeable[1].endTime);
							timeMinus2.push({
								startTime: changeable[1].startTime,
								endTime: changeable[1].endTime
							})
							let limitTime = self.checkLimitTime(changeable, timeRangeLimit, 1)
							
							if (timeMinus[0].startTime < timeMinus[0].endTime && (self.timeRange === 24 && timeMinus2[0].startTime < 1440 && timeMinus2[0].startTime != null || 
							self.timeRange === 48 && timeMinus2[0].startTime < 2880 && timeMinus2[0].startTime != null)) {
								rgc = ruler.addChartWithType("Changeable", {
								id: `rgc${i}`,
								lineNo: i,
								start: self.checkTimeOfChart(timeChart2.startTime, timeRangeLimit),
								end: self.checkTimeOfChart(timeChart2.endTime, timeRangeLimit),
								limitStartMin: limitTime.limitStartMin,
								limitStartMax: limitTime.limitStartMax,
								limitEndMin: limitTime.limitEndMin,
								limitEndMax: limitTime.limitEndMax,
								resizeFinished: (b: any, e: any, p: any) => {
									console.log("test");
								},
								dropFinished: (b: any, e: any) => {
									console.log("test");
								}
							});
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "Changeable", `rgc${i}`, timeChart2, i, null, 0, 9999, 0, 9999));
							indexRight = indexRight++;
							}
							
						}
					}

					// add chart for FLEX-TIME - thời gian flex
					if (datafilter[0].typeOfTime === "Flex" || datafilter[0].gcFlexTime.length > 0) {
						let flex = datafilter[0].gcFlexTime;
						let coreTime = datafilter[0].gcCoreTime;
						timeChart = self.convertTimeToChart(flex[0].startTime, flex[0].endTime);
						timeMinus.push({
							startTime: flex[0].startTime,
							endTime: flex[0].endTime
						})
						if (coreTime.length > 0) {
							timeChartCore = self.convertTimeToChart(coreTime[0].coreStartTime, coreTime[0].coreEndTime);
						}
						let limitTime = self.checkLimitTime(flex, timeRangeLimit, 0);
						if (timeMinus[0].startTime < timeMinus[0].endTime && timeChart.startTime < timeRangeLimit) {
							lgc = ruler.addChartWithType("Flex", {
								id: `lgc${i}`,
								lineNo: i,
								start: self.checkTimeOfChart(timeChart.startTime, timeRangeLimit),
								end: self.checkTimeOfChart(timeChart.endTime, timeRangeLimit),
								limitStartMin: limitTime.limitStartMin,
								limitStartMax: limitTime.limitStartMax,
								limitEndMin: limitTime.limitEndMin,
								limitEndMax: limitTime.limitEndMax,
								resizeFinished: (b: any, e: any, p: any) => {
									console.log("test");
								},
								dropFinished: (b: any, e: any) => {
									console.log("test");
								}
							});
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "Flex", `lgc${i}`, timeChart, i));
							indexLeft = ++indexLeft;
							// CORE-TIME
							if (coreTime.length > 0 && (_.inRange(coreTime[0].coreStartTime, timeMinus[0].startTime, timeMinus[0].endTime) ||
								_.inRange(coreTime[0].coreEndTime, timeMinus[0].startTime, timeMinus[0].endTime))) {
								ruler.addChartWithType("CoreTime", {
									id: `lgc${i}_` + indexLeft,
									parent: `lgc${i}`,
									lineNo: i,
									start: timeChartCore.startTime,
									end: timeChartCore.endTime,
									pin: true
								});
								fixedGc.push(self.addChartWithType045(datafilter[0].empId, "CoreTime", `lgc${i}_` + indexLeft, timeChartCore, i, `lgc${i}`));
								indexLeft = ++indexLeft;
							}
						}
					}
					if (timeChart != null) {
						self.allTimeChart.push({
							empId: datafilter[0].empId,
							timeChart: timeChart,
							timeChart2: timeChart2
						})
					}
				};

				// Lần này chưa đối ứng
				/*let suportTime = datafilter[0].gcSupportTime;
				if (suportTime != null) {
					timeChart = self.convertTimeToChart(suportTime[0].coreStartTime, suportTime[0].coreEndTime);
					spgc = self.addChildChartWithTypes(ruler, "SupportTime", `lgc${i}_` + indexF, timeChart, i, `lgc${i}`)
		
					indexF = ++indexF;
				}*/

				let overTime = datafilter[0].gcOverTime;
				if (overTime.length > 0 && datafilter[0].typeOfTime !== "Changeable") {
					for (let o = 0; o < overTime[0].lstOverTime.length; o++) {
						let y = overTime[0].lstOverTime[o];
						timeChartOver = self.convertTimeToChart(y.startTime, y.endTime);
						let id = `lgc${i}_` + indexLeft, parent = `lgc${i}`;
						if ((timeMinus.length > 0 && timeMinus[0].startTime != null && timeMinus[0].endTime != null) &&
							(timeChart.startTime < Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) &&
							(_.inRange(timeChartOver.startTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) ||
								_.inRange(timeChartOver.endTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)))) {
							self.addChildChartWithTypes(ruler, "OT", id, timeChartOver, i, parent, 0, 9999, 0, 9999, null, 1000)
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "OT", id, timeChartOver, i, parent, 0, 9999, 0, 9999, 1000));
							indexLeft = ++indexLeft;
							/*lstTime = _.filter(lstTime, x => { return (x.start == timeChartOver.startTime && x.end < timeChartOver.endTime) || (x.start < timeChartOver.startTime && x.end == timeChartOver.endTime) })
							if (_.isEmpty(lstTime)) {
								lstTime.push({
									start: timeChartOver.startTime,
									end: timeChartOver.endTime
								})
							}*/
						}

						if ((timeMinus2.length > 0 && timeMinus2[0].startTime != null && timeMinus2[0].endTime != null) &&
							(timeChart2.startTime < Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) &&
							(_.inRange(timeChartOver.startTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) ||
								_.inRange(timeChartOver.endTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)))) {
							id = `rgc${i}_` + indexRight, parent = `rgc${i}`;
							self.addChildChartWithTypes(ruler, "OT", id, timeChartOver, i, parent, 0, 9999, 0, 9999, null, 1000)
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "OT", id, timeChartOver, i, parent, 0, 9999, 0, 9999, 1000));
							indexRight = ++indexRight;
							/*lstTimeFilter = _.filter(lstTime, x => { return (x.start == timeChartOver.startTime && x.end < timeChartOver.endTime) || (x.start < timeChartOver.startTime && x.end == timeChartOver.endTime) })
							if (_.isEmpty(lstTimeFilter)) {
								lstTime.push({
									start: timeChartOver.startTime,
									end: timeChartOver.endTime
								})
							}*/
						}
					}
				};
				let startTime1 = 0, startTime2 = 0, endTime1 = 0, endTime2 = 0;
				let breakTime: any = [];
				if (datafilter[0].gcBreakTime.length > 0) {
					breakTime = datafilter[0].gcBreakTime[0].lstBreakTime;
					if (screen === "KDL045") {
						breakTime = lstBrkNew;
						datafilter[0].gcBreakTime[0].lstBreakTime = lstBrkNew;
					}
				}

				if (breakTime.length > 0) {
					for (let o = 0; o < breakTime.length; o++) {
						let y = breakTime[o];
						timeChartBrk = self.convertTimeToChart(_.isNil(y.startTime) ? y.start : y.startTime, _.isNil(y.endTime) ? y.end : y.endTime);
						let id = `lgc${i}_` + indexLeft, parent = `lgc${i}`;
						startTime1 = self.checkTimeOfChart(timeChartBrk.startTime, timeRangeLimit);
						endTime1 = self.checkTimeOfChart(timeChartBrk.endTime, timeRangeLimit);

						if ((timeMinus.length > 0 && timeMinus[0].startTime != null && timeMinus[0].endTime != null) &&
							(timeChart.startTime < Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) &&
							(_.inRange(timeChartBrk.startTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) ||
								_.inRange(timeChartBrk.endTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)))) {

							ruler.addChartWithType("BreakTime", {
								id: id,
								parent: parent,
								lineNo: i,
								start: startTime1,
								end: endTime1,
								limitStartMin: 0,
								limitStartMax: 9999,
								limitEndMin: 0,
								limitEndMax: 9999,
								zindex: 1001,
								resizeFinished: (b: any, e: any, p: any) => {
								},
								dropFinished: (b: any, e: any) => {
									let datafilterBrk = _.filter(midData, (x: any) => { return x.empId === self.lstEmpId[i].empId }),
										dataMidBrk = datafilterBrk[0], timeChartBrk2 : any = null, checkChange = 0;
									breakTime = dataMidBrk.breaktime;
									for (let l = 0; l < breakTime.length; l++) {
										timeChartBrk2 = self.convertTimeToChart(_.isNil(y.startTime) ? y.start : y.startTime, _.isNil(y.endTime) ? y.end : y.endTime);
										if (b === timeChartBrk2.startTime && e === timeChartBrk2.endTime) {
											checkChange = 1;
										}
									}
									if (checkChange == 0) {
										$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "breaktime", breakTime == null ? "0:00" : breakTime + " "); // + " " để phân biệt khi thay đổi vị trí nhưng không thay đổi giá trị
									} else {
										$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "breaktime", breakTime == null ? "0:00" : breakTime);
									}
								}
							});
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "BreakTime", id, timeChartBrk, i, parent, 0, 9999, 0, 9999, 1001));
							indexLeft = ++indexLeft;
						}

						if (datafilter[0].gantCharts === 1) {
							if ((timeMinus2.length > 0 && timeMinus2[0].startTime != null && timeMinus2[0].endTime != null) &&
								(timeChart2.startTime < Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) &&
								(_.inRange(timeChartBrk.startTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) ||
									_.inRange(timeChartBrk.endTime, 0, Math.floor(timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)))) {
								id = `rgc${i}_` + indexRight, parent = `rgc${i}`;
								ruler.addChartWithType("BreakTime", {
									id: id,
									parent: parent,
									lineNo: i,
									start: startTime1,
									end: endTime1,
									limitStartMin: 0,
									limitStartMax: 9999,
									limitEndMin: 0,
									limitEndMax: 9999,
									zindex: 1001,
									resizeFinished: (b: any, e: any, p: any) => {
									},
									dropFinished: (b: any, e: any) => {
										let datafilterBrk = _.filter(midData, (x: any) => { return x.empId === self.lstEmpId[i].empId }),
											dataMidBrk = datafilterBrk[0];
										breakTime = dataMidBrk.breaktime;
										if (b !== timeChartBrk.startTime && e !== timeChartBrk.endTime) {
											$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "breaktime", self.timeBrkText() == null ? "0:00" : self.timeBrkText() + " ");
										} else {
											$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "breaktime", self.timeBrkText() == null ? "0:00" : self.timeBrkText());
										}
									}
								});
								fixedGc.push(self.addChartWithType045(datafilter[0].empId, "BreakTime", id, timeChartBrk, i, parent, 0, 9999, 0, 9999, 1001));
								indexRight = ++indexRight;
							}
						}

						lstBreakTime.push({
							startTime: timeChartBrk.startTime,
							endTime: timeChartBrk.endTime,
							id: id,
							empId: datafilter[0].empId
						})

					}
				} else {
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "breaktime", "0:00");
				}

				let shortTime = datafilter[0].gcShortTime;
				if (shortTime.length > 0) {
					for (let o = 0; o < shortTime[0].listShortTime.length; o++) {
						let y = shortTime[0].listShortTime[o];
						timeChartShort = self.convertTimeToChart(y.startTime.time, y.endTime.time);
						startTime1 = self.checkTimeOfChart(timeChartShort.startTime, timeRangeLimit);
						endTime1 = self.checkTimeOfChart(timeChartShort.endTime, timeRangeLimit);
						let id = `lgc${i}_` + indexLeft, parent = `lgc${i}`;

						if (timeMinus.length > 0 && (_.inRange(y.startTime.time, timeMinus[0].startTime, timeMinus[0].endTime) ||
							_.inRange(y.endTime.time, timeMinus[0].startTime, timeMinus[0].endTime))) {
							self.addChildChartWithTypes(ruler, "ShortTime", id, timeChartShort, i, parent, 0, 9999, 0, 9999, null, 1002)
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "ShortTime", id, timeChartShort, i, parent, 0, 9999, 0, 9999, 1002));
							indexLeft = ++indexLeft;
						}

						if (timeMinus2.length > 0 && (_.inRange(y.startTime.time, timeMinus2[0].startTime, timeMinus2[0].endTime) ||
							_.inRange(y.endTime.time, timeMinus2[0].startTime, timeMinus2[0].endTime))) {
							id = `rgc${i}_` + indexRight, parent = `rgc${i}`;
							self.addChildChartWithTypes(ruler, "ShortTime", id, timeChartShort, i, parent, 0, 9999, 0, 9999, null, 1002)
							fixedGc.push(self.addChartWithType045(datafilter[0].empId, "ShortTime", id, timeChartShort, i, parent, 0, 9999, 0, 9999, 1002));
							indexRight = ++indexRight;
						}

					}
				}

				let holidayTime = datafilter[0].gcHolidayTime;
				if (holidayTime.length > 0) {
					for (let o = 0; o < holidayTime[0].listTimeVacationAndType.length; o++) {
						let y = holidayTime[0].listTimeVacationAndType[o];
						for (let e = 0; e < y.timeVacation.timeZone.length; e++) {
							let hld = y.timeVacation.timeZone[e];
							timeChartHoliday = self.convertTimeToChart(hld.startTime.time, hld.endTime.time);

							startTime1 = self.checkTimeOfChart(timeChartHoliday.startTime, timeRangeLimit);
							endTime1 = self.checkTimeOfChart(timeChartHoliday.endTime, timeRangeLimit);
							let id = `lgc${i}_` + indexLeft, parent = `lgc${i}`;

							if (timeMinus.length > 0 && (_.inRange(hld.startTime.time, timeMinus[0].startTime, timeMinus[0].endTime) ||
								_.inRange(hld.endTime.time, timeMinus[0].startTime, timeMinus[0].endTime))) {
								if ((self.timeRange === 24 && hld.startTime.time < 1440 || self.timeRange === 48 && hld.endTime.time < 2880)) {
									self.addChildChartWithTypes(ruler, "HolidayTime", id, timeChartHoliday, i, parent, 0, 9999, 0, 9999, null, 1003);
									fixedGc.push(self.addChartWithType045(datafilter[0].empId, "HolidayTime", id, timeChartHoliday, i, parent, 0, 9999, 0, 9999, 1003));
									indexLeft = ++indexLeft;
								}
							}

							if (timeMinus2.length > 0 && (_.inRange(hld.startTime.time, timeMinus2[0].startTime, timeMinus2[0].endTime) ||
								_.inRange(hld.endTime.time, timeMinus2[0].startTime, timeMinus2[0].endTime))) {
								id = `rgc${i}_` + indexRight, parent = `rgc${i}`;
								if ((self.timeRange === 24 && hld.startTime.time < 1440 || self.timeRange === 48 && hld.endTime.time < 2880)) {
									self.addChildChartWithTypes(ruler, "HolidayTime", id, timeChartHoliday, i, parent, 0, 9999, 0, 9999, null, 1003);
									fixedGc.push(self.addChartWithType045(datafilter[0].empId, "HolidayTime", id, timeChartHoliday, i, parent, 0, 9999, 0, 9999, 1003));
									indexRight = ++indexRight;
								}
							}
						}
					}
				}
			}

			// thay đổi giá trị khi kéo thanh chart
			$(lgc).on("gcresize", (e: any) => {
				let param = e.detail;
				let startMinute = 0, endMinute = 0;
				startMinute = duration.create(param[0] * 5).text;
				endMinute = duration.create(param[1] * 5).text;
				if (param[2]) {
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "startTime1", startMinute);
				} else {
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "endTime1", endMinute);
				}
			});

			$(rgc).on("gcresize", (e: any) => {
				let param = e.detail;
				let startMinute, endMinute;
				startMinute = duration.create(param[0] * 5).text;
				endMinute = duration.create(param[1] * 5).text;
				if (param[2]) {
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "startTime2", startMinute);
				} else {
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "endTime2", endMinute);
				}
			});

			$(lgc).on("gcdrop", (e: any) => {
				let param = e.detail;
				let startMinute = 0, endMinute = 0;
				startMinute = duration.create(param[0] * 5).text;
				endMinute = duration.create(param[1] * 5).text;
				if (param) {
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "startTime1", startMinute);
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "endTime1", endMinute);
				}
			});

			$(rgc).on("gcdrop", (e: any) => {
				let param = e.detail;
				let startMinute, endMinute;
				startMinute = duration.create(param[0] * 5).text;
				endMinute = duration.create(param[1] * 5).text;
				if (param) {
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "startTime2", startMinute);
					$("#extable-ksu003").exTable("cellValue", "middle", datafilter[0].empId, "endTime2", endMinute);
				}
			});
			self.allGcShow = fixedGc;
		}
		
		// Kiểm tra giới hạn của thời gian 
		checkLimitTime(time: any, timeRangeLimit: any, index: number) {
			let self = this;
			let timeLimit = null, limitStartMin = 0, limitStartMax = timeRangeLimit, limitEndMin = 0, limitEndMax = timeRangeLimit;

			if (time[index].startTimeRange != null) {
				if (index > 0) {
					limitStartMin = time[index].endTime;
				}
				limitStartMin = self.checkRangeLimitTime(time, timeRangeLimit, 1, 0) ? Math.floor((time[index].startTimeRange.startTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) : self.dataInitStartKsu003Dto().byDateDto.dispStart;
				limitStartMax = self.checkRangeLimitTime(time, timeRangeLimit, 2, 0) ? Math.floor((time[index].startTimeRange.endTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) : timeRangeLimit;
				if (time[index].startTimeRange.startTime.time <= self.dataInitStartKsu003Dto().byDateDto.dispStart) {
					limitStartMin = self.dataInitStartKsu003Dto().byDateDto.dispStart;
				}
				if (time[index].startTimeRange.endTime.time <= self.dataInitStartKsu003Dto().byDateDto.dispStart) {
					limitStartMax = self.dataInitStartKsu003Dto().byDateDto.dispStart;
				}
			} else if(time[0].startTimeRange != null){
				
				limitStartMin = Math.floor((time[0].startTimeRange.startTime.time) / 5)
			}

			if (time[index].endTimeRange != null) {
				limitEndMin = self.checkRangeLimitTime(time, timeRangeLimit, 3, 0) ? Math.floor((time[index].endTimeRange.startTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) : timeRangeLimit;
				limitEndMax = self.checkRangeLimitTime(time, timeRangeLimit, 4, 0) ? Math.floor((time[index].endTimeRange.endTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) : timeRangeLimit;
				if (time[index].endTimeRange.startTime.time <= self.dataInitStartKsu003Dto().byDateDto.dispStart) {
					limitEndMin = self.dataInitStartKsu003Dto().byDateDto.dispStart;
				}
				if (time[index].endTimeRange.endTime.time <= self.dataInitStartKsu003Dto().byDateDto.dispStart) {
					limitEndMax = self.dataInitStartKsu003Dto().byDateDto.dispStart;
				}
			}

			timeLimit = {
				limitStartMin: limitStartMin,
				limitStartMax: limitStartMax,
				limitEndMin: limitEndMin,
				limitEndMax: limitEndMax
			}
			return timeLimit;
		}

		checkTimeOfChart(time: any, timeRangeLimit: any) {
			// check start time
			let self = this;
			if (time > timeRangeLimit) {
				time = timeRangeLimit;
			}

			if (time < Math.floor((self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5)) {
				time = Math.floor((self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5);
			}

			return time;
		}

		addChartWithType045(empId: string, type: any, id: any, timeChart: any, lineNo: any, parent?: any,
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
					zIndex: !_.isNil(zIndex) ? zIndex : 1000,
					resizeFinished: (b: any, e: any, p: any) => {
					if(!_.isNil(id) && _.includes(id, 'lgc')){
						if(p == true)
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", formatById("Clock_Short_HM", (b) * 5)); 
						else
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", formatById("Clock_Short_HM", (e) * 5));
					} else {
						if(p == true)
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", formatById("Clock_Short_HM", (b) * 5)); 
						else
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", formatById("Clock_Short_HM", (e) * 5));
					}
					
						console.log("test");
					},
					dropFinished: (b: any, e: any) => {
					if(!_.isNil(parent) && _.includes(id, 'lgc')){
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", formatById("Clock_Short_HM", (b) * 5)); 
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", formatById("Clock_Short_HM", (e) * 5));
					} else {
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", formatById("Clock_Short_HM", (b) * 5)); 
						$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", formatById("Clock_Short_HM", (e) * 5));
					}
					}
				}
			}
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
		/** ADD-TYPE-CHART */
		addTypeOfChart(ruler: any) {
			let self = this, fixed = "None";
			if(self.checkDisByDate == false){
				fixed = "Both"
			}
			
			ruler.addType({
				name: "Fixed", color: "#ccccff", lineWidth: 30, canSlide: false, unitToPx: self.operationUnit(), fixed: fixed
			});

			ruler.addType({
				name: "Changeable", color: "#ffc000", lineWidth: 30, canSlide: true, unitToPx: self.operationUnit(), fixed: fixed
			});

			ruler.addType({
				name: "Flex", color: "#ccccff", lineWidth: 30, canSlide: true, unitToPx: self.operationUnit(), fixed: fixed
			});

			ruler.addType({
				name: "BreakTime", followParent: true, color: "#ff9999", lineWidth: 30, canSlide: self.checkDisByDate == false ? false : true,
				unitToPx: self.operationUnit(), pin: true, rollup: true, roundEdge: true, fixed: "Both"
			});

			ruler.addType({
				name: "OT", followParent: true, color: "#ffff00", lineWidth: 30, canSlide: false,
				unitToPx: self.operationUnit(), pin: true, rollup: true, fixed: "Both"
			});

			ruler.addType({
				name: "HolidayTime", followParent: true, color: "#c4bd97", lineWidth: 30, canSlide: false,
				unitToPx: self.operationUnit(), pin: true, rollup: true, roundEdge: false, fixed: "Both"
			});

			ruler.addType({
				name: "ShortTime", followParent: true, color: "#6fa527", lineWidth: 30, canSlide: false,
				unitToPx: self.operationUnit(), pin: true, rollup: true, roundEdge: false, fixed: "Both"
			});

			ruler.addType({
				name: "CoreTime", color: "#00ffcc", lineWidth: 30, unitToPx: self.operationUnit(), fixed: "Both"
			});
		}

		// Kiểm tra giới hạn hiển thị của thanh chart
		checkRangeLimitTime(flex: any, timeRangeLimit: number, type: number, index: number) {
			let check = false, self = this;

			if (flex.length > 0) {
				if (type == 1 && flex[index].startTimeRange != null && Math.floor((flex[index].startTimeRange.startTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) <= timeRangeLimit) {
					check = true;
				}
				if (type == 2 && flex[index].startTimeRange != null && Math.floor((flex[index].startTimeRange.endTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) < timeRangeLimit) {
					check = true;
				}
				if (type == 3 && flex[index].endTimeRange != null && Math.floor((flex[index].endTimeRange.startTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) <= timeRangeLimit) {
					check = true;
				}
				if (type == 4 && flex[index].endTimeRange != null && Math.floor((flex[index].endTimeRange.endTime.time - self.dataInitStartKsu003Dto().byDateDto.dispStart * 60) / 5) < timeRangeLimit) {
					check = true;
				}
			}
			return check;
		}
		/** COLOR-ZONE */
		/*A4_color*/
		setColorEmployee(needSchedule: number, cheering: number) {
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
			return "";
		}
		checkColorA6(workInfo: any) {
			let workTypeColor = "";
			if (workInfo === EditStateSetting.HAND_CORRECTION_MYSELF) {
				workTypeColor = "#94b7fe"
			} else if (workInfo === EditStateSetting.HAND_CORRECTION_OTHER) {
				workTypeColor = "#cee6ff"
			} else if (workInfo === EditStateSetting.REFLECT_APPLICATION) {
				workTypeColor = "#bfea60"
			}
			return workTypeColor;
		}
		/** A6_color① - chua xong o phan login va xin bang don */
		setColorWorkingInfo(empId: string, isConfirmed: number, workScheduleDto: any, isNeedWorkSchedule: any) {
			let self = this, workTypeColor = "", workTimeColor = "", startTime1Color = "", endTime1Color = "",
				startTime2Color = "", endTime2Color = "", breakTimeColor = "", workingInfoColor = "";

			if (isConfirmed == 0 && isNeedWorkSchedule == 0) {
				workingInfoColor = '#ddddd2';
			}
			if (isConfirmed == 1) {
				workingInfoColor = '#eccefb';
			}
			if (workScheduleDto != null) {
				// work type
				if (workScheduleDto.workTypeStatus != null) {
					workTypeColor = self.checkColorA6(workScheduleDto.workTypeStatus);
				}
				// work time
				if (workScheduleDto.workTimeStatus != null) {
					workTimeColor = self.checkColorA6(workScheduleDto.workTimeStatus);
				}
				// start time 1
				if (workScheduleDto.startTime1Status != null) {
					startTime1Color = self.checkColorA6(workScheduleDto.startTime1Status);
				}
				// end time 1
				if (workScheduleDto.endTime1Status != null) {
					endTime1Color = self.checkColorA6(workScheduleDto.endTime1Status);
				}
				// start time 2
				if (workScheduleDto.startTime2Status != null) {
					startTime2Color = self.checkColorA6(workScheduleDto.startTime2Status);
				}
				// end time 2
				if (workScheduleDto.endTime2Status != null) {
					endTime2Color = self.checkColorA6(workScheduleDto.endTime2Status);
				}
				// break time
				if (workScheduleDto.breakTimeStatus != null) {
					breakTimeColor = self.checkColorA6(workScheduleDto.endTime2Status);
				}
			}

			return ({
				workTypeColor: workTypeColor,
				workTimeColor: workTimeColor,
				startTime1Color: startTime1Color,
				endTime1Color: endTime1Color,
				startTime2Color: startTime2Color,
				endTime2Color: endTime2Color,
				breakTimeColor: breakTimeColor,
				workingInfoColor: workingInfoColor

			});
		}


		setPositionButonToRightToLeft() {
			let self = this, marginleftOfbtnToRight: number = 0;
			self.indexBtnToLeft(0);
			$(".toLeft").css("display", "none");
			marginleftOfbtnToRight = $("#extable-ksu003").width() - 32;
			$(".toRight").css('margin-left', marginleftOfbtnToRight + 'px');
		}

		setPositionButonDownAndHeightGrid() {
			let self = this;

			$("#extable-ksu003").exTable("setHeight", 10 * 30 + 18);
			$(".toDown").css({ "margin-top": 10 * 30 + 8 + 'px' });
			/*$("#master-wrapper").css({ 'overflow-y': 'auto' });
			$("#master-wrapper").css({ 'overflow-x': 'hidden' });*/
			//}
			$("#extable-ksu003").exTable("scrollBack", 0, { h: Math.floor(self.initDispStart * 12) });
			$("#functon-area-row2-left").focus();
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
					$(".toLeft").css("margin-left", 193 + 'px');
				if (window.innerHeight < 700) {
					$(".ex-header-detail").css({ "width": 966 + 'px' });
					$(".ex-body-detail").css({ "width": 983 + 'px' });
				}
				
			} else {
				if (self.showA9) {
					$("#extable-ksu003").exTable("showMiddle");
				}
				$(".ex-header-middle").css("width", 560 + 'px' + '!important')
				$(".toLeft").css("margin-left", 585 + 'px');
				if (window.innerHeight < 700) {
					$(".ex-header-detail").css({ "width": 588 + 'px' });
					$(".ex-body-detail").css({ "width": 604 + 'px' });
				}
			}

			self.indexBtnToLeft(self.indexBtnToLeft() + 1);
			self.localStore().showHide = self.indexBtnToLeft();
			storage.setItemAsJson(self.KEY, self.localStore());
			$("#extable-ksu003").exTable("scrollBack", 0, { h: Math.floor(self.initDispStart * 12) });
		}

		toDown() {
			let self = this;
			let exTableHeight = window.innerHeight - 92 - 31 - 50 - 50 - 35 - 27 - 27 - 20;
			exTableHeight = 17 * 30 + 18;
			$("#master-wrapper").css({ 'overflow-x': 'hidden' });
			if (self.indexBtnToDown() % 2 == 0) {
				$("#extable-ksu003").exTable("setHeight", exTableHeight);
				$(".toDown").css('margin-top', exTableHeight - 8 + 'px');
				$("#master-wrapper").css({ 'overflow-y': 'auto' });
				$("#master-wrapper").css({ 'overflow-x': 'hidden' });
				$("#contents-area").css({ 'overflow-x': 'hidden' });
					if (window.innerWidth >= 1320) {
						$("#A1_4").css("margin-right", "0px")
						$("#note-color").css("margin-right", "18px")
						$("#hr-row2").css("width", "1264px")
					}
					if (window.innerWidth < 1320) {
					$("#note-sort").css("margin-left", "1158px");}
			} else {
				exTableHeight = 10 * 30 + 18;
				$("#extable-ksu003").exTable("setHeight", exTableHeight);
				$(".toDown").css('margin-top', exTableHeight - 8 + 'px');
				$("#contents-area").css({ 'overflow-x': 'hidden' });
					
					if (window.innerWidth >= 1320) {
						$("#A1_4").css("margin-right", "20px")
						$("#note-color").css("margin-right", "35px")
					}
					
					if (window.innerWidth < 1320) {
					$("#note-sort").css("margin-left", "1171px");
					} 
			}
			self.indexBtnToDown(self.indexBtnToDown() + 1);
		}

		showHide() {
			let self = this;
			if (self.indexBtnToLeft() % 2 == 0) {
				if (!self.showA9) {
					$("#extable-ksu003").exTable("showMiddle");
				}
				$(".toLeft").css("margin-left", 585 + 'px');
			} else {
				if (self.showA9) {
					$("#extable-ksu003").exTable("hideMiddle");
				}
				$(".toLeft").css("margin-left", 193 + 'px');
			}

			if (window.innerHeight < 700) {
				if (window.innerWidth < 1320) {
					if (self.indexBtnToLeft() % 2 == 0) {
						$(".ex-header-detail").css({ "width": 588 + 'px' });
						$(".ex-body-detail").css({ "width": 604 + 'px' });
					} else {
						$(".ex-header-detail").css({ "width": 966 + 'px' });
						$(".ex-body-detail").css({ "width": 983 + 'px' });
					}
					
					$("#label-display").css("margin-left", 55 + 'px');
					$(".toDown").css('margin-left', 0 + 'px');
					if (navigator.userAgent.indexOf("Chrome") == -1) {
					$(".toDown").css('margin-left', 0 + 'px');
				}
				}
			} else {
					$(".toDown").css('margin-left', -5 + 'px');
				}
			$("#functon-area-row2-left").focus();
		}

		calcTimeDuplicate(timeStart: any, timeEnd: any, startTime: any, endTime: any) {
			let duplicateTime: any = [];
			duplicateTime = ({
				startTime: timeStart,
				endTime: timeEnd
			});
			if (_.inRange(timeStart, startTime, endTime) || _.inRange(timeEnd, startTime, endTime)) {
				if (timeStart < startTime && timeEnd < endTime) {
					duplicateTime = ({
						startTime: startTime,
						endTime: timeEnd
					});
				}

				if (timeStart > startTime && timeEnd > endTime) {
					duplicateTime = ({
						startTime: startTime,
						endTime: endTime
					});
				}
			}
			return duplicateTime;
		}

		// Tính tổng giờ làm các chart
		calcSumTime(brkT: any, schedule: any, totalBreakTime1: any, totalBreakTime2: any, totalBreakTime: any) {
			let self = this;
			let brkTChart = self.convertTimeChart(_.isNil(brkT.startTime) ? brkT.start : brkT.startTime, _.isNil(brkT.endTime) ? brkT.end : brkT.endTime),
				timeChart = self.convertTimeChart(schedule.workScheduleDto.startTime1, schedule.workScheduleDto.endTime1),
				brkTChart2: any = null, timeChart2: any = null;

			if (_.inRange(brkTChart.startTime, timeChart.startTime, timeChart.endTime) ||
				_.inRange(brkTChart.endTime, timeChart.startTime, timeChart.endTime)) {
				totalBreakTime1 += self.calcBreakTime(brkTChart, timeChart);
			}

			if (schedule.workScheduleDto.startTime2 != null && schedule.fixedWorkInforDto.workType != WorkTimeForm.FLEX) {
				brkTChart2 = self.convertTimeChart(_.isNil(brkT.startTime) ? brkT.start : brkT.startTime, _.isNil(brkT.endTime) ? brkT.end : brkT.endTime)
				timeChart2 = self.convertTimeChart(schedule.workScheduleDto.startTime2, schedule.workScheduleDto.endTime2)
				if (_.inRange(brkTChart2.startTime, timeChart2.startTime, timeChart2.endTime) ||
					_.inRange(brkTChart2.endTime, timeChart2.startTime, timeChart2.endTime)) {
					totalBreakTime2 += self.calcBreakTime(brkTChart2, timeChart2);
				}
			}
			totalBreakTime = totalBreakTime2 + totalBreakTime1;
			return totalBreakTime;
		}

		// Tính tổng BREAK-TIME khi kéo gant chart
		calcBreakTime(brkTChart: any, timeChart: any) {
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
		
		calcTotalTime(workScheduleDto: model.EmployeeWorkScheduleDto, whenCall?: any) {
			let totalTime: any = null;
			if (!_.isNil(workScheduleDto)) {
				if (workScheduleDto.endTime2 != null && workScheduleDto.startTime2 != null)
					totalTime = (workScheduleDto.endTime1 - workScheduleDto.startTime1) + (workScheduleDto.endTime2 - workScheduleDto.startTime2);
				else if (workScheduleDto.endTime1 != null && workScheduleDto.startTime1 != null)
					totalTime = (workScheduleDto.endTime1 - workScheduleDto.startTime1);
				if (_.isNil(whenCall))
					totalTime = totalTime != null ? formatById("Clock_Short_HM", totalTime) : "";
			}
			return totalTime;
		}
		
		calcAllBrk(lstTime : any){
			let self = this, brkTotal = 0,lstTimeFilter : any =[];
			for(let br = 0; br < self.lstBreakSum.length; br++){
					lstTimeFilter = _.filter(lstTime, (x: any) => { return (x.start == self.lstBreakSum[br].start && x.end < self.lstBreakSum[br].end) || 
					(x.start < self.lstBreakSum[br].start && x.end == self.lstBreakSum[br].end) });
					if(!_.isEmpty(lstTimeFilter)){
						for(let brk = 0; brk < lstTimeFilter.length; brk++){
							brkTotal += (self.lstBreakSum[br].end - self.lstBreakSum[br].start) - (lstTimeFilter[brk].end - lstTimeFilter[brk].start)
						}
					} else {
						brkTotal += self.lstBreakSum[br].end - self.lstBreakSum[br].start;
					}
				}
				return brkTotal;
		}

		calcAllTime(schedule: any, lstTime: any, timeRangeLimit: any) {
			// Tính tổng thời gian làm việc
			let self = this;
			let totalTimeAll: any = 0, totalTimeWork = 0,
				start1 = (schedule.workScheduleDto != null && schedule.workScheduleDto.startTime1 != null) ? (self.checkTimeOfChart(schedule.workScheduleDto.startTime1, timeRangeLimit * 5)) : 0,
				end1 = (schedule.workScheduleDto != null && schedule.workScheduleDto.endTime1 != null) ? (self.checkTimeOfChart(schedule.workScheduleDto.endTime1, timeRangeLimit * 5)) : 0,
				start2 = (schedule.workScheduleDto != null && schedule.workScheduleDto.startTime2 != null) ? (self.checkTimeOfChart(schedule.workScheduleDto.startTime2, timeRangeLimit * 5)) : 0,
				end2 = (schedule.workScheduleDto != null && schedule.workScheduleDto.endTime2 != null) ? (self.checkTimeOfChart(schedule.workScheduleDto.endTime2, timeRangeLimit * 5)) : 0;
			lstTime.forEach((total: any) => {
				totalTimeAll += (total.end * 5) - (total.start * 5)
			});
			if (start2 != 0 && end2 != 0)
				totalTimeWork = ((end2 - (end2 % 5)) - (start2 - (start2 % 5))) + ((end1 - (end1 % 5)) - (start1 - (start1 % 5)));
			else if (end1 != 0)
				totalTimeWork = ((end1 - (end1 % 5)) - (start1 - (start1 % 5)));

			totalTimeWork = totalTimeWork - totalTimeAll;
			totalTimeWork = totalTimeWork != 0 ? formatById("Clock_Short_HM", totalTimeWork) : "";

			return totalTimeWork;
		}

		// Tính tổng từng loại thời gian
		calcChartTypeTime(schedule: any, typeChart: any, timeRangeLimit: any, lstTime: any, lstBreak?: any) {
			let self = this, startCalc= 0, endCalc=0, lstTimeFilter: any = [];
			for (let o = 0; o < typeChart.length; o++) {
				let brkT: any = typeChart[o];
				let timeChartBrk = self.convertTimeToChart(_.isNil(brkT.startTime) ? brkT.start : (_.isNil(brkT.startTime.time) ? brkT.startTime : brkT.startTime.time), 
				_.isNil(brkT.endTime) ? brkT.end : (_.isNil(brkT.endTime.time) ? brkT.endTime : brkT.endTime.time)),
					timeChart = self.convertTimeToChart(schedule.workScheduleDto.startTime1, schedule.workScheduleDto.endTime1), timeChart2: any = null;

				startCalc = self.checkTimeOfChart(timeChartBrk.startTime, timeRangeLimit);
				endCalc = self.checkTimeOfChart(timeChartBrk.endTime, timeRangeLimit);

				let duplicateTime = self.calcTimeDuplicate(startCalc, endCalc, timeChart.startTime, timeChart.endTime);

				startCalc = duplicateTime.startTime;
				endCalc = duplicateTime.endTime;

				if ((timeChart != null && timeChart.startTime != null && timeChart.endTime != null) &&
					(timeChart.startTime < (timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) &&
					(_.inRange(timeChart.startTime, 0, (timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) ||
						_.inRange(timeChart.endTime, 0, (timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)))) {
							
							if(!_.isNil(lstBreak) && lstBreak == 1 &&  (_.inRange(startCalc, timeChart.startTime, timeChart.endTime) || _.inRange(endCalc, timeChart.startTime, timeChart.endTime))){
								self.lstBreakSum.push({
								start : startCalc,
								end : endCalc
							})
							}

					lstTimeFilter = _.filter(lstTime, (x: any) => { return (x.start == timeChartBrk.startTime && x.end < timeChartBrk.endTime) || (x.start < timeChartBrk.startTime && x.end == timeChartBrk.endTime) })
					if ((_.isEmpty(lstTimeFilter) && (_.inRange(startCalc, timeChart.startTime, timeChart.endTime) || _.inRange(endCalc, timeChart.startTime, timeChart.endTime))))  {
						lstTime.push({
							start: startCalc,
							end: endCalc
						})
					}
					if (_.isEmpty(lstTime) && !_.isNil(lstBreak) && lstBreak != 2) {
						lstTime.push({
							start: startCalc,
							end: endCalc
						})
					}
				}

				if (self.dataScreen003A().targetInfor === 1) {
					timeChart2 = self.convertTimeToChart(schedule.workScheduleDto.startTime2, schedule.workScheduleDto.endTime2)
					if ((schedule.workScheduleDto.startTime2 != null && schedule.workScheduleDto.endTime2 != null) &&
						(timeChart2.startTime < (timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) &&
						(_.inRange(timeChartBrk.startTime, 0, (timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)) ||
							_.inRange(timeChartBrk.endTime, 0, (timeRangeLimit - self.dataInitStartKsu003Dto().byDateDto.dispStart * 12)))) {
								
								if(!_.isNil(lstBreak) && lstBreak == 1 &&  (_.inRange(startCalc, timeChart2.startTime, timeChart2.endTime) || _.inRange(endCalc, timeChart2.startTime, timeChart2.endTime))){
								self.lstBreakSum.push({
								start : startCalc,
								end : endCalc
							})
							}
							
						lstTimeFilter = _.filter(lstTime, (x: any) => { return (x.start == timeChartBrk.startTime && x.end < timeChartBrk.endTime) || (x.start < timeChartBrk.startTime && x.end == timeChartBrk.endTime) })
						if ((_.isEmpty(lstTimeFilter) && (_.inRange(startCalc, timeChart2.startTime, timeChart2.endTime) || _.inRange(endCalc, timeChart2.startTime, timeChart2.endTime))))  {
							lstTime.push({
								start: startCalc,
								end: endCalc
							})
						}

						if (_.isEmpty(lstTime) && !_.isNil(lstBreak) && lstBreak != 2) {
							lstTime.push({
								start: startCalc,
								end: endCalc
							})
						}

					}
				}
			}
			return lstTime;
		}

		public nextDay() {
			let self = this;
			let checkSort = $("#extable-ksu003").exTable('updatedCells');
			if (checkSort.length > 0) {
				dialog.confirm({ messageId: "Msg_447" }).ifYes(() => {
					self.nextDayImpl();
				}).ifNo(() => { return; });
			} else {
				self.nextDayImpl();
			}
		}

		public nextDayImpl() {
			let self = this;
			self.changeTargetDate(1, 1);
			self.checkNext(true);
			self.checkPrv(true);
			/*$("#icon-prev-all").css("filter", "contrast(1)");
			$("#icon-prev-one").css("filter", "contrast(1)");*/
			if (self.dataFromA().endDate <= self.targetDate()) {
				self.targetDate(self.dataFromA().endDate);
				self.checkNext(false);
				/*$("#icon-next-one").css("filter", "contrast(0.7)");
				$("#icon-next-all").css("filter", "contrast(0.7)");*/
			}
			self.hoverEvent(self.targetDate());
			self.destroyAndCreateGrid(self.lstEmpId, 0);
		}

		public nextAllDay() {
			let self = this, i = 7,
				nextDay: any = moment(moment(self.targetDate()).add(7, 'd').format('YYYY/MM/DD')),
				checkSort = $("#extable-ksu003").exTable('updatedCells');
			if (checkSort.length > 0) {
				dialog.confirm({ messageId: "Msg_447" }).ifYes(() => {
					self.nextAllDayImpl(i, nextDay);
				}).ifNo(() => { return; });
			} else {
				self.nextAllDayImpl(i, nextDay);
			}
		}

		public nextAllDayImpl(i: number, nextDay: any) {
			let self = this;
			self.checkNext(true);
			self.checkPrv(true);

			/*$("#icon-prev-all").css("filter", "contrast(1)");
			$("#icon-prev-one").css("filter", "contrast(1)");*/
			if (self.dataFromA().endDate <= nextDay._i) {
				let date: number = moment(self.dataFromA().endDate).date() - moment(self.targetDate()).date();
				self.checkNext(false);
				/*$("#icon-next-all").css("filter", "contrast(0.7)");
				$("#icon-next-one").css("filter", "contrast(0.7)");*/

				self.changeTargetDate(1, date);
			} else {
				self.changeTargetDate(1, i);
			}
			self.hoverEvent(self.targetDate());
			self.destroyAndCreateGrid(self.lstEmpId, 0);
		}

		public prevDay() {
			let self = this;
			let checkSort = $("#extable-ksu003").exTable('updatedCells');
			if (checkSort.length > 0) {
				dialog.confirm({ messageId: "Msg_447" }).ifYes(() => {
					self.prevDayImpl();
				}).ifNo(() => { return; });
			} else {
				self.prevDayImpl();
			}
		}

		public prevDayImpl() {
			let self = this;
			self.changeTargetDate(0, 1);
			self.checkPrv(true);
			self.checkNext(true);
			/*$("#icon-next-all").css("filter", "contrast(1)");
			$("#icon-next-one").css("filter", "contrast(1)");*/
			if (self.dataFromA().startDate >= self.targetDate()) {
				self.targetDate(self.dataFromA().startDate);
				self.checkPrv(false);
				/*$("#icon-prev-one").css("filter", "contrast(0.7)");
				$("#icon-prev-all").css("filter", "contrast(0.7)");*/
			}
			self.hoverEvent(self.targetDate());
			self.destroyAndCreateGrid(self.lstEmpId, 0);
		}

		public prevAllDay() {
			let self = this, i = 7;
			let prvDay: any = moment(moment(self.targetDate()).subtract(7, 'd').format('YYYY/MM/DD'));
			let checkSort = $("#extable-ksu003").exTable('updatedCells');
			if (checkSort.length > 0) {
				dialog.confirm({ messageId: "Msg_447" }).ifYes(() => {
					self.prevAllDayImpl(i, prvDay);
				}).ifNo(() => { return; });
			} else {
				self.prevAllDayImpl(i, prvDay);
			}
		}

		public prevAllDayImpl(i: number, prvDay: any) {
			let self = this;
			self.checkPrv(true);
			self.checkNext(true);
			/*$("#icon-next-all").css("filter", "contrast(1)");
			$("#icon-next-one").css("filter", "contrast(1)");*/
			if (self.dataFromA().startDate >= prvDay._i) {
				let date: number = moment(self.targetDate()).date() - moment(self.dataFromA().startDate).date();
				self.checkPrv(false);
				/*$("#icon-prev-all").css("filter", "contrast(0.7)");
				$("#icon-prev-one").css("filter", "contrast(0.7)");*/

				self.changeTargetDate(0, date);
			} else {
				self.changeTargetDate(0, 7);
			}
			self.hoverEvent(self.targetDate());
			self.destroyAndCreateGrid(self.lstEmpId, 0);
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

		public checkTimeInfo(worktypeCode: any, worktimeCode: any, startTime1: any, startTime2: any, endTime1: any, endTime2: any) {
			let command: any = {
				workType: worktypeCode,
				workTime: worktimeCode,
				workTime1: startTime1 != "" && startTime1 != null ? new TimeZoneDto(new TimeOfDayDto(0, _.isString(startTime1) ? Math.floor(duration.parseString(startTime1).toValue()) : startTime1),
					new TimeOfDayDto(0, _.isString(endTime1) ? Math.floor(duration.parseString(endTime1).toValue()) : endTime1)) : null,
				workTime2: startTime2 != "" && startTime2 != null ? new TimeZoneDto(new TimeOfDayDto(0, _.isString(startTime2) ? Math.floor(duration.parseString(startTime2).toValue()) : startTime2),
					new TimeOfDayDto(0, _.isString(endTime2) ? Math.floor(duration.parseString(endTime2).toValue()) : endTime2)) : null,
			}
			service.checkTimeIsIncorrect(command).done(function(result) {
				let errors = [];
				for (let i = 0; i < result.length; i++) {
					if (!result[i].check) {
						if (result[i].timeSpan == null) {
							errors.push({
								message: nts.uk.resource.getMessage('Msg_439', getText('KDL045_12')),
								messageId: "Msg_439",
								supplements: {}
							});
						} else {
							if (result[i].timeSpan.startTime == result[i].timeSpan.endTime) {
								errors.push({
									message: nts.uk.resource.getMessage('Msg_2058', [result[i].nameError, result[i].timeInput]),
									messageId: "Msg_2058",
									supplements: {}
								});
							} else {
								errors.push({
									message: nts.uk.resource.getMessage('Msg_1772', [result[i].nameError, formatById("Clock_Short_HM", result[i].timeSpan.startTime), formatById("Clock_Short_HM", result[i].timeSpan.endTime)]),
									messageId: "Msg_1772",
									supplements: {}
								});
							}
						}
					}
				}

				if (errors.length > 0){
					
					let errorsInfo  = _.uniqBy(errors, x =>{return x.message});
					bundledErrors({ errors: errorsInfo });
					return;
				}
					
			}).fail(function(res: any) {
				errorDialog({ messageId: res.messageId, messageParams: res.parameterIds });
			}).always(function() {
				block.clear();
			});
		}

		convertTime(timeStart: any, timeEnd: any, timeStart2: any, timeEnd2: any) {
			if (timeStart != "") timeStart = Math.floor(duration.parseString(timeStart).toValue());
			if (timeEnd != "") timeEnd = Math.floor(duration.parseString(timeEnd).toValue());
			if (timeStart2 != "") timeStart2 = Math.floor(duration.parseString(timeStart2).toValue());
			if (timeEnd2 != "") timeEnd2 = Math.floor(duration.parseString(timeEnd2).toValue());

			let timeConvert = {
				start: timeStart != "" ? timeStart : "",
				end: timeEnd != "" ? timeEnd : "",
				start2: timeStart2 != "" ? timeStart2 : "",
				end2: timeEnd2 != "" ? timeEnd2 : ""
			}
			return timeConvert;
		}

		convertTimeChart(startTime: any, endTime: any) {
			let  convertTime = null;
			convertTime = {
				startTime: startTime,
				endTime: endTime
			}
			return convertTime;
		}

		convertTimeToChart(startTime: any, endTime: any) {
			let self = this, convertTime = null;

			startTime = startTime != null ? formatById("Clock_Short_HM", startTime) : "0:00";
			endTime = endTime != null ? formatById("Clock_Short_HM", endTime) : "0:00";
			startTime = self.convertTimePixel(startTime);
			endTime = self.convertTimePixel(endTime);
			convertTime = {
				startTime: startTime,
				endTime: endTime
			}
			return convertTime;
		}

		convertTimePixel(timeStart: any) {
			let time = 0, self = this;
			if (timeStart != null) {
				// convert string to pixel
				time = Math.floor((duration.parseString(timeStart).toValue() - (self.dataInitStartKsu003Dto().byDateDto.dispStart * 60)) / 5);
			}
			return time;
		}

		/** 行事(A2_1_6)をクリックする  (Click "event"(A2_1_6) */
		hoverEvent(targetDate: any) {
			let self = this, tooltip = _.filter(self.tooltip, (x: any) => { return x.ymd === targetDate });
			let check = 0;
			if (tooltip.length > 0) {
				let htmlToolTip = tooltip[0].htmlTooltip;
				$('#event').on({
				  "click": function() {
				    $(this).tooltip({ items: "#event", content: htmlToolTip != null ? htmlToolTip : "",
					tooltipClass: "tooltip-styling"});
				    $(this).tooltip("open");
					check = 1;
				  },
				  "mouseout": function(a : any) {    
					if(check == 1)  
				     $(this).tooltip("disable");   
				  }
				});
			}
		}
		
		// open dialog kdl045
		public openKdl045Dialog(empId: string) {
			let self = this, lineNo = _.findIndex(self.lstEmpId, (x) => { return x.empId === empId; }),
				dataMid = $("#extable-ksu003").exTable('dataSource', 'middle').body[lineNo];
			block.grayout();
			if (self.dataScreen003A().employeeInfo[lineNo].workScheduleDto != null) {
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.workTypeCode = dataMid.worktypeCode.trim();
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.workTimeCode = dataMid.worktimeCode.trim();
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.startTime1 = dataMid.startTime1.trim();
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.endTime1 = dataMid.endTime1.trim();
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.startTime2 = dataMid.startTime2.trim();
				self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.endTime2 = dataMid.endTime2.trim();

				if (self.dataScreen003AFirst().employeeInfo[lineNo].workScheduleDto.workTimeCode == "") {
					self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.listBreakTimeZoneDto = [];
				}
			}

			let data: Array<model.DisplayWorkInfoByDateDto> = _.filter(self.dataScreen003A().employeeInfo, (x) => { return x.empId === empId; }); // lấy dữl iệu theo empId từ list dữ liệu của màn KSU003	

			let dataShare: any = {
				employeeInfo: data[0],
				targetInfor: self.dataScreen003A().targetInfor,
				canModified: self.dataScreen003A().canModified, // comment tạm để test
				scheCorrection: self.dataScreen003A().scheCorrection,
				unit: self.dataFromA().unit,
				targetId: self.dataFromA().unit === 0 ? self.dataFromA().workplaceId : self.dataFromA().workplaceGroupId,
				workplaceName: self.dataFromA().workplaceName
			};
			setShared('dataShareTo045', dataShare);
			nts.uk.ui.windows.sub.modal('/view/kdl/045/a/index.xhtml').onClosed(() => {
				self.dataScreen045A(getShared('dataFromKdl045'));
				if (!_.isNil(self.dataScreen045A())) {
					self.checkGetInfo = true;
					let lstBrkTime = self.dataScreen045A().workScheduleDto.listBreakTimeZoneDto, totalBrkTime = "",
						lstBreak: any = lstBrkTime, totalTimebr: any = null;
					lstBrkTime.forEach((x: any) => {
						totalTimebr += x.endTime - x.startTime;
					})

					totalBrkTime = totalTimebr != null ? formatById("Clock_Short_HM", totalTimebr) : ""

					let totalTime = self.calcTotalTime(self.dataScreen045A().workScheduleDto),
						schedule = self.dataScreen045A().workScheduleDto,
						fixed = self.dataScreen045A().fixedWorkInforDto,
						info = self.dataScreen045A().workInfoDto;
					self.setDataToMidExtable(empId, schedule, fixed, totalTime, totalBrkTime);
					if(_.isNull(self.dataScreen003A().employeeInfo[lineNo].workScheduleDto)){
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto = {
						startTime1: schedule.startTime1,
						startTime1Status: schedule.endTime1,
						endTime1: schedule.endTime1,
						endTime1Status: null,
						startTime2: schedule.startTime2,
						startTime2Status: null,
						endTime2: schedule.endTime2,
						endTime2Status: null,
						listBreakTimeZoneDto: lstBrkTime.length > 0 ? lstBrkTime : [],
						workTypeCode: schedule.workTypeCode,
						breakTimeStatus: null,
						workTypeStatus: null,
						workTimeCode: schedule.workTimeCode,
						workTimeStatus: null
						}
					} else {
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.startTime1 = schedule.startTime1;
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.startTime1Status = schedule.endTime1;
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.endTime1 = schedule.endTime1;
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.startTime2 = schedule.startTime2;
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.endTime2 = schedule.endTime2;
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.listBreakTimeZoneDto = lstBrkTime.length > 0 ? lstBrkTime : [];
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.workTypeCode = schedule.workTypeCode;
						self.dataScreen003A().employeeInfo[lineNo].workScheduleDto.workTimeCode = schedule.workTimeCode;
					}	
					
					if(_.isNull(self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto)){
						self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto = {
						workTimeName: fixed.workTimeName,
						coreStartTime: null,
						coreEndTime: null,
						overtimeHours: [],
						startTimeRange1: null,
						endTimeRange1: null,
						workTypeName: fixed.workTypeName,
						startTimeRange2: null,
						endTimeRange2: null,
						fixBreakTime: fixed.fixBreakTime,
						workType: fixed.workType
						}
					} else {
						self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto.workTimeName = fixed.workTimeName;
						self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto.workTypeName = fixed.workTypeName;
						self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto.fixBreakTime = fixed.fixBreakTime;
						self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto.workType = fixed.workType;
					}

						self.dataScreen003A().employeeInfo[lineNo].workInfoDto.directAtr = info.directAtr;
						self.dataScreen003A().employeeInfo[lineNo].workInfoDto.bounceAtr = info.bounceAtr;
						
						if(schedule.workTimeCode == null || schedule.workTimeCode == ""){
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeCode", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", "");
								$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", getText('KSU003_55'));
								return;
						}

					self.convertDataIntoExtable();

					let datafilter: Array<ITimeGantChart> = _.filter(self.dataOfGantChart, (x: any) => { return x.empId === empId });
					if (datafilter.length > 0) {
						//self.updateGantChart(datafilter, lineNo, fixedGc, lstBreak, indexS, indexF);
						self.addAllChart(datafilter, lineNo, [], self.midDataGC, "KDL045", lstBreak);
						ruler.replaceAt(lineNo, [
							...self.allGcShow
						]);
					}
				}
				self.checkGetInfo = false;
				block.clear();
			});
		}


		/** A1_3 - Open dialog G */
		public openGDialog() {
			let self = this;
			block.grayout();
			let dataShare = {
				employeeIDs: _.map(self.lstEmpId, (x: IEmpidName) => { return x.empId }),
				startDate: self.targetDate(),
				endDate: self.targetDate(),
				employeeInfo: self.dataFromA().listEmp
			};
			setShared('dataShareDialogG', dataShare);
			nts.uk.ui.windows.sub.modal('/view/ksu/001/g/index.xhtml').onClosed(() => {
				block.clear();
			});
		}

		public openKdl003Dialog(workTypeCode: string, workTimeCode: string, empId: string) {
			let self = this;
			block.grayout();
			nts.uk.ui.windows.setShared('parentCodes', {
				workTypeCodes: [],
				selectedWorkTypeCode: workTypeCode.length > 3 ? workTypeCode.slice(0, 3) : workTypeCode,
				workTimeCodes: [],
				selectedWorkTimeCode: workTimeCode.length > 3 ? workTimeCode.slice(0, 3) : workTimeCode
			}, true);
			let checkOpen = _.filter(self.disableDs, (x: any) => { return x.empId === empId });
			let checkOpen2 = _.filter(self.lstDis, (x: any) => { return x.empId === empId });
			if (checkOpen.length < 1 && checkOpen2.length < 1) {
				nts.uk.ui.windows.sub.modal('/view/kdl/003/a/index.xhtml').onClosed(() => {
					let dataShareKdl003 = getShared('childData');

					if (!_.isNil(dataShareKdl003)) {
						let param = [{
							workTypeCode: dataShareKdl003.selectedWorkTypeCode,
							workTimeCode: dataShareKdl003.selectedWorkTimeCode
						}]
						// 社員勤務予定と勤務固定情報を取得する
						service.getEmpWorkFixedWorkInfo(param).done((data: model.DataFrom045) => {
							
							if (!_.isNil(data)) {
								
								if(data.fixedWorkInforDto.workType == null){
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeCode", data.workScheduleDto.workTypeCode);
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime1", "");
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "startTime2", " ");
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime1", "");
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "endTime2", " ");
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeCode", "");
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "totalTime", "");
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "breaktime", "");
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktimeName", getText('KSU003_55'));
									$("#extable-ksu003").exTable("cellValue", "middle", empId, "worktypeName", data.fixedWorkInforDto.workTypeName);
									return;
								}
								
								if (data.workScheduleDto.startTime1) {
									self.checkClearTime = false;
									self.checkUpdateMidChart = false;
								}
								let lineNo = _.findIndex(self.dataScreen003A().employeeInfo, (x) => { return x.empId === empId; });
								self.dataScreen003A().employeeInfo[lineNo].fixedWorkInforDto = data.fixedWorkInforDto;
								self.dataScreen003A().employeeInfo[lineNo].workScheduleDto = data.workScheduleDto;

								let totalTime = self.calcTotalTime(data.workScheduleDto), totalTimebr = 0;

								if (data.workScheduleDto.listBreakTimeZoneDto != null && data.workScheduleDto.listBreakTimeZoneDto.length > 0) {
									data.workScheduleDto.listBreakTimeZoneDto.forEach((x: any) => {
										totalTimebr += (!_.isNil(x.endTime) ? x.endTime : x.end) - (!_.isNil(x.startTime) ? x.startTime : x.start);
									})
								}

								let totalBrkTime = totalTimebr != null ? formatById("Clock_Short_HM", totalTimebr) : "";

								self.setDataToMidExtable(empId, data.workScheduleDto, data.fixedWorkInforDto, totalTime, totalBrkTime);
								self.convertDataIntoExtable(lineNo);

								let datafilter: Array<ITimeGantChart> = _.filter(self.dataOfGantChart, (x: any) => { return x.empId === empId });
								if (datafilter.length > 0) {
									//self.updateGantChart(datafilter, lineNo, fixedGc, lstBreak, indexS, indexF);
									self.addAllChart(datafilter, lineNo, [], self.midDataGC, "KDL003");
									if (self.allGcShow.length > 0) {
										ruler.replaceAt(lineNo, [
											...self.allGcShow
										]);
									} else {
										ruler.replaceAt(lineNo, [{ // xóa chart khi là ngày nghỉ
											type: "Flex",
											options: {
												id: `lgc` + lineNo,
												start: -1,
												end: -1,
												lineNo: lineNo
											}
										}]);
									}
								}

							}

						}).fail(function(error) {
							errorDialog({ messageId: error.messageId });
						}).always(function() {
							self.checkUpdateMidChart = true;
						});
					}
					block.clear();
				});
			}
			block.clear();
		}

		/** A1_4 - Close modal */
		public closeDialog(): void {
			nts.uk.ui.windows.close();
		}

	}

	export enum SupportAtr {
		NOT_CHEERING = 1,
		TIMEZONE_SUPPORTER = 2,
		TIMEZONE_RESPONDENT = 3,
		ALL_DAY_SUPPORTER = 4,
		ALL_DAY_RESPONDENT = 5
	}

	export enum EditStateSetting {
		HAND_CORRECTION_MYSELF = 0,
		HAND_CORRECTION_OTHER = 1,
		REFLECT_APPLICATION = 2,
		IMPRINT = 3
	}

	export enum WorkTimeForm {
		FIXED = 0,
		FLEX = 1,
		FLOW = 2,
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
		lstEmpIdSort: Array<any>;
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

	interface IEmpidName {
		empId: string,
		name: string,
		code: string
	}

	//時間帯(実装コードなし/使用不可)
	export class TimeZoneDto {
		startTime: TimeOfDayDto;//開始時刻 
		endTime: TimeOfDayDto;//終了時刻
		constructor(startTime: TimeOfDayDto,
			endTime: TimeOfDayDto) {
			this.startTime = startTime;
			this.endTime = endTime;
		}
	}

	//時刻(日区分付き)
	export class TimeOfDayDto {
		dayDivision: number;//日区分   : DayDivision
		time: number;//時刻
		constructor(dayDivision: number,
			time: number) {
			this.dayDivision = dayDivision;
			this.time = time;
		}
	}

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
