module nts.uk.at.view.kdl005.a.viewmodel {
	export class ScreenModel {
		date: KnockoutObservable<any>;
		empList: KnockoutObservableArray<string> = ko.observableArray([]);
		dataHoliday: KnockoutObservable<any> = ko.observable();

		//_____KCP005________
		listComponentOption: any = [];
		selectedCode: KnockoutObservable<string>;
		multiSelectedCode: KnockoutObservableArray<string>;
		isShowAlreadySet: KnockoutObservable<boolean>;
		alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
		isDialog: KnockoutObservable<boolean>;
		isShowNoSelectRow: KnockoutObservable<boolean>;
		isMultiSelect: KnockoutObservable<boolean>;
		isShowWorkPlaceName: KnockoutObservable<boolean>;
		isShowSelectAllButton: KnockoutObservable<boolean>;
		disableSelection: KnockoutObservable<boolean>;

		employeeList: KnockoutObservableArray<UnitModel> = ko.observableArray<UnitModel>([]);
		paramData: any = nts.uk.ui.windows.getShared('KDL005_DATA');

		// search
		items: KnockoutObservableArray<ItemModel> = ko.observableArray([]);

		// area left
		employeeCodeName: KnockoutObservable<string> = ko.observable('');

		//
		columns: KnockoutObservableArray<any>;
		currentCode: KnockoutObservable<any>;
		currentCodeList: KnockoutObservableArray<any>;
		count: number = 100;
		switchOptions: KnockoutObservableArray<any>;
		holidayData: KnockoutObservableArray<HolidayInfo> = ko.observableArray([]);
		holidayDataOld: KnockoutObservableArray<HolidayInfo> = ko.observableArray([]);

		//
		itemList: KnockoutObservableArray<ItemModel>;
		selectedCodeCbb: KnockoutObservable<string>;
		isEnable: KnockoutObservable<boolean>;
		isEditable: KnockoutObservable<boolean>;
		checkBindData: boolean = true;

		checkSolid: number = 0;

		constructor() {
			let self = this;
			self.date = ko.observable(new Date());
			self.empList = ko.observableArray([]);

			//_____KCP005________
			self.selectedCode = ko.observable('1');
			self.multiSelectedCode = ko.observableArray(['0', '1', '4']);
			self.isShowAlreadySet = ko.observable(false);
			self.alreadySettingList = ko.observableArray([
				{ code: '1', isAlreadySetting: true },
				{ code: '2', isAlreadySetting: true }
			]);
			self.isDialog = ko.observable(false);
			self.isShowNoSelectRow = ko.observable(false);
			self.isMultiSelect = ko.observable(false);
			self.isShowWorkPlaceName = ko.observable(false);
			self.isShowSelectAllButton = ko.observable(false);
			self.disableSelection = ko.observable(false);

			//
			self.columns = ko.observableArray([
				{ headerText: nts.uk.resource.getText('KDL005_61'), key: 'accrualDate', width: 170 },
				{ headerText: nts.uk.resource.getText('KDL005_64'), key: 'digestionStatus', width: 130 },
				{ headerText: nts.uk.resource.getText('KDL005_53'), key: 'deadline', width: 130 },
				{ headerText: nts.uk.resource.getText('KDL005_54'), key: 'digestionDate', width: 180 }
			]);

			self.switchOptions = ko.observableArray([
				{ code: "1", name: '四捨五入' },
				{ code: "2", name: '切り上げ' },
				{ code: "3", name: '切り捨て' }
			]);
			self.currentCode = ko.observable(0);
			self.currentCodeList = ko.observableArray([]);

			if (self.paramData.length > 1) {
				$("#area-right").show();
			} else {
				$("#area-right").hide();
			}

			//
			self.listComponentOption = {
				isShowAlreadySet: self.isShowAlreadySet(),
				isMultiSelect: false,
				listType: ListType.EMPLOYEE,
				employeeInputList: self.employeeList,
				selectType: SelectType.SELECT_FIRST_ITEM,
				selectedCode: self.selectedCode,
				isDialog: self.isDialog(),
				isShowNoSelectRow: self.isShowNoSelectRow(),
				alreadySettingList: self.alreadySettingList,
				isShowWorkPlaceName: self.isShowWorkPlaceName(),
				isShowSelectAllButton: self.isShowSelectAllButton(),
				disableSelection: self.disableSelection(),
				maxRows: 15
			};

			$('#kcp005component').ntsListComponent(self.listComponentOption);

			self.listComponentOption.selectedCode.subscribe((value: any) => {
				if (value == null) return;
				let name: any = _.filter(self.employeeList(), (x: any) => {
					return _.isEqual(x.code, value);
				});
				self.employeeCodeName(name[0].code + " " + name[0].name);
				self.bindDataToGrid(value);
			})

			//
			self.itemList = ko.observableArray([
				new ItemModel(1, nts.uk.resource.getText('KDL005_63')),
				new ItemModel(2, nts.uk.resource.getText('KDL005_48'))
			]);

			self.selectedCodeCbb = ko.observable('1');
			self.isEnable = ko.observable(true);
			self.isEditable = ko.observable(true);

			self.selectedCodeCbb.subscribe((value: any) => {
				self.holidayData([]);
				if (value == 1) {
					self.bindDataToGrid(self.listComponentOption.selectedCode);
				} else {
					let data = (_.filter(self.dataHoliday().remainNumConfirmDto.detailRemainingNumbers, (x: any) => {
						return _.includes(x.digestionStatus, "残り");
					}));
					if (data.length > 0) {
						_.forEach(data, (z: any, index: number) => {
							self.bindDataToText(z, index);
						});
					}
				}

				if (self.checkSolid != 0) {
					$("#single-list > tbody > tr:nth-child(" + self.checkSolid + ") > td").css("border-bottom", "1px #CCC solid");
				}
			})
		}

		startPage(): JQueryPromise<any> {
			let self = this, dfd = $.Deferred<any>();
			let param = {
				employeeIds: self.paramData,
				baseDate: ""
			};
			service.getHolidaySub(param).done((data: any) => {
				self.dataHoliday(data);
				_.forEach(data.empImport, (a: any, ind) => {
					self.employeeList.push({ id: ind, code: a.employeeCode, name: a.employeeName, workplaceName: 'HN' })
				});
				self.listComponentOption.selectedCode(self.employeeList()[0].code);

				if (self.checkSolid != 0) {
					$("#single-list > tbody > tr:nth-child(" + self.checkSolid + ") > td").css("border-bottom", "1px #CCC solid");
				}
			});
			dfd.resolve();
			return dfd.promise();
		}

		bindDataToGrid(value: any) {
			let self = this;
			if (_.isNil(self.dataHoliday())) return;
			self.holidayData([]);
			if (self.dataHoliday().remainNumConfirmDto == null) {
				let sid = _.map(_.filter(self.dataHoliday().empImport, (o: any) => _.isEqual(o.employeeCode, value)), (z: any) => {
					return z.employeeId;
				})
				let param = {
					employeeIds: sid,
					baseDate: ""
				};
				service.getHolidaySub(param).done((data: any) => {
					_.forEach(data.remainNumConfirmDto.detailRemainingNumbers, (z: any, index: number) => {
						self.bindDataToText(z, index);
					});
					self.showHideItem(data);
					self.holidayDataOld = self.holidayData;
				});
			} else {
				_.forEach(self.dataHoliday().remainNumConfirmDto.detailRemainingNumbers, (z: any, index: number) => {
					self.bindDataToText(z, index);
				});
				self.showHideItem(self.dataHoliday());
				self.holidayDataOld = self.holidayData;
			}
		}

		bindDataToText(z: any, index: number) {
			let self = this, textA3_11_12_13 = "", text_A3_31_32 = "", text_A4_41_42_43 = "",
				text45 = "<span style='color:#FF2D2D;'>" + z.dueDateStatus + "</span>";

			textA3_11_12_13 = z.occurrenceDateStatus + " " + z.accrualDate + " " + z.numberOccurrences;
			text_A3_31_32 = "<span>" + text45 + " " + z.deadline; + "</span>"
			text_A4_41_42_43 = z.digestionDateStatus + " " + z.digestionDate + " " + z.digestionCount

			self.holidayData.push(new HolidayInfo(textA3_11_12_13, z.digestionStatus, text_A3_31_32, text_A4_41_42_43));

			if (z.dueDateStatus.length > 0 && (_.includes(z.occurrenceDateStatus, nts.uk.resource.getText('KDL005_40')) 
				|| _.includes(z.digestionDateStatus, nts.uk.resource.getText('KDL005_40')))) {
				if (self.checkSolid != 0) return;
				
				self.checkSolid = index;
			}
		}

		showHideItem(data: any) {
			let remainNumConfirmDto = data.remainNumConfirmDto;

			// ※１ && ※4
			if (remainNumConfirmDto != null && remainNumConfirmDto.management == 1 &&
				remainNumConfirmDto.dayCloseDeadline.length > 0) {
				$("#A2_10").show();
				$("#A2_21").show();
			} else {
				$("#A2_10").hide();
				$("#A2_21").hide();
			}

			// ※2
			if (remainNumConfirmDto != null && remainNumConfirmDto.management == 1) {
				$("#area-info").show();
				$("#area-non-info").hide();
			} else {
				$("#area-info").hide();
				$("#area-non-info").show();
			}

			// ※3
			if (remainNumConfirmDto != null && remainNumConfirmDto.unit == 0) {
				$("#A6_1").hide();
				$("#A6_2").hide();
				$("#A6_3").hide();
			} else {
				$("#A6_1").show();
				$("#A6_2").show();
				$("#A6_3").show();
			}
		}

		cancel() {
			nts.uk.ui.windows.close();
		}
	}

	export interface IEmployeeParam {
		employeeIds: Array<string>;
		baseDate: string;
	}

	export class ListType {
		static EMPLOYMENT = 1;
		static Classification = 2;
		static JOB_TITLE = 3;
		static EMPLOYEE = 4;
	}

	export interface UnitModel {
		id?: string;
		code: string;
		name?: string;
		workplaceName?: string;
		isAlreadySetting?: boolean;
		optionalColumn?: any;
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

	/** 残数確認ダイアログDTO */
	export class RemainNumberConfirmDto {
		expiredWithinMonth: KnockoutObservable<string>; // 1ヶ月以内期限切れ数 - A2_9
		unit: KnockoutObservable<number>; // 単位  0 : 日, 1 : 時間
		dayCloseDeadline: KnockoutObservable<string>; // 期限の一番近い日 - A2_21
		detailRemainingNumbers: KnockoutObservableArray<RemainNumberDetailedInfo>; // 残数詳細一覧 - 残数詳細情報
		currentRemainNumber: KnockoutObservable<string>; // 現時点残数 - A2_5
		employeeId: KnockoutObservable<string>; // 社員ID
		management: KnockoutObservable<number>; // 管理する
		constructor(
			expiredWithinMonth: string,
			unit: number,
			dayCloseDeadline: string,
			detailRemainingNumbers: Array<RemainNumberDetailedInfo>,
			currentRemainNumber: string,
			employeeId: string,
			management: number
		) {
			let self = this;
			self.expiredWithinMonth = ko.observable(expiredWithinMonth);
			self.unit = ko.observable(unit);
			self.dayCloseDeadline = ko.observable(dayCloseDeadline);
			self.detailRemainingNumbers = ko.observableArray(detailRemainingNumbers);
			self.currentRemainNumber = ko.observable(currentRemainNumber);
			self.employeeId = ko.observable(employeeId);
			self.management = ko.observable(management);
		}
	}

	/** 残数詳細情報 */
	export class RemainNumberDetailedInfo {
		deadline: KnockoutObservable<string>; // 期限日 - A3_32
		dueDateStatus: KnockoutObservable<string>; // 期限日状況 - A3_31
		digestionCount: KnockoutObservable<string>; // 消化数 - A3_43
		digestionDate: KnockoutObservable<string>; // 消化日  - A3_42
		digestionDateStatus: KnockoutObservable<string>; // 消化日状況  - A3_41
		digestionStatus: KnockoutObservable<string>; // 消化状況  - A3_21
		numberOccurrences: KnockoutObservable<string>; // 発生数 - A3_13
		accrualDate: KnockoutObservable<string>; // 発生日 - A3_12
		occurrenceDateStatus: KnockoutObservable<string>; // 発生日状況 - A3_11
		constructor(
			deadline: string,
			dueDateStatus: string,
			digestionCount: string,
			digestionDate: string,
			digestionDateStatus: string,
			digestionStatus: string,
			numberOccurrences: string,
			accrualDate: string,
			occurrenceDateStatus: string
		) {
			let self = this;
			self.deadline = ko.observable(deadline);
			self.dueDateStatus = ko.observable(dueDateStatus);
			self.digestionCount = ko.observable(digestionCount);
			self.digestionDate = ko.observable(digestionDate);
			self.digestionDateStatus = ko.observable(digestionDateStatus);
			self.digestionStatus = ko.observable(digestionStatus);
			self.numberOccurrences = ko.observable(numberOccurrences);
			self.accrualDate = ko.observable(accrualDate);
			self.occurrenceDateStatus = ko.observable(occurrenceDateStatus);
		}
	}

	export class ItemModel {
		code: number;
		name: string;
		displayName?: string;
		constructor(code: number, name: string, displayName?: string) {
			this.code = code;
			this.name = name;
			this.displayName = displayName;
		}
	}

	class HolidayInfo {
		accrualDate: string;
		digestionStatus: string;
		deadline: string;
		digestionDate: string;
		constructor(accrualDate: string, digestionStatus: string, deadline: string, digestionDate: string) {
			this.accrualDate = accrualDate;
			this.digestionStatus = digestionStatus;
			this.deadline = deadline;
			this.digestionDate = digestionDate;
		}
	}

}