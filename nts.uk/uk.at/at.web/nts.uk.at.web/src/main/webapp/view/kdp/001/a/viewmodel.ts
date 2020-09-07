/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

const requestUrl = {
	getEmployeeStampData: 'at/record/stamp/employment_system/get_employee_stamp_data',
	confirmUseOfStampInput: 'at/record/stamp/employment_system/confirm_use_of_stamp_input',
	registerStampInput: 'at/record/stamp/employment_system/register_stamp_input',
	getSettingStampInput: 'at/record/stamp/employment_system/get_setting_stamp_input',
	getOmissionContents: 'at/record/stamp/employment_system/get_omission_contents',
	getStampToSuppress: 'at/record/stamp/employment_system/get_stamp_to_suppress'
}

const notUseMessage = [
	{ text: "Msg_1644", value: 1 },
	{ text: "Msg_1645", value: 2 },
	{ text: "Msg_1619", value: 3 }
]

const Mode = {
	Personal: 1, // 個人
	Shared: 2  // 共有 
}

const daysColor = [
	{ day: 0, color: '#FF0000' },
	{ day: 6, color: '#0000FF' }
]

const STAMP_MEANS_PORTAL = 4;
const DEFAULT_PAGE_NO = 1;

const DEFAULT_GRAY = '#E8E9EB';
@bean()
class KDP001AViewModel extends ko.ViewModel {

	systemDate: KnockoutObservable<any> = ko.observable(moment.utc());
	screenMode: KnockoutObservable<any> = ko.observable();
	stampDatas: KnockoutObservableArray<IStampInfoDisp> = ko.observableArray([]);
	usedSatus: KnockoutObservable<number> = ko.observable(0);
	settingDateTimeColor: KnockoutObservable<any> = ko.observable({
		textColor: '#ccccff',
		backgroundColor: '#0033cc'
	});
	countTime: KnockoutObservable<number> = ko.observable(0);
	settingCountTime: KnockoutObservable<number> = ko.observable(60);
	resultDisplayTime: KnockoutObservable<number> = ko.observable(10);

	buttonSetting: KnockoutObservable<IStampToSuppress> = ko.observable({
		goingToWork: true,
		departure: true,
		goOut: true,
		turnBack: true
	} as IStampToSuppress);

	stampResultDisplay: KnockoutObservable<IStampResultDisplay> = ko.observable({
		companyId: "",
		displayItemId: [],
		notUseAttr: 0
	});
	buttons: KnockoutObservableArray<IButtonSettingsDto> = ko.observableArray([]);

	constructor() {
		super();
		let vm = this;
		vm.buttonSetting.subscribe((data: StampToSuppress) => {
			let vm = this;
		});
	}

	created(params: any) {

		let vm = this;

		let url = $(location).attr('search');
		let urlParam: string = url.split("=")[1];

		vm.screenMode(!!urlParam ? urlParam : null);
		this.$blockui("invisible");
		vm.$ajax(requestUrl.confirmUseOfStampInput, { stampMeans: STAMP_MEANS_PORTAL }).then((result) => {
			this.$blockui("clear");
			vm.usedSatus(result.used);
			vm.systemDate(moment(vm.$date.now()));



			this.$blockui("invisible");
			vm.$ajax(requestUrl.getSettingStampInput).then((setting: IStampSetting) => {

				if (!!setting) {
					let buttons: Array<IButtonSettingsDto> = [];

					if (_.has(setting, 'portalStampSettings.buttonSettings')) {
						buttons = _.chain(setting.portalStampSettings.buttonSettings)
							.uniqBy('buttonPositionNo')
							.filter(function(o) { return 1 <= o.buttonPositionNo && o.buttonPositionNo <= 4; })
							.sortBy(['buttonPositionNo'])
							.value();
					}

					(!vm.showButtonGoOutAndBack() && buttons.length > 2) ? buttons.splice(2) : buttons.splice(4);

					vm.buttons(buttons);

					if (_.has(setting, 'stampResultDisplayDto')) {
						vm.stampResultDisplay(setting.stampResultDisplayDto);
					}

					if (_.has(setting, 'portalStampSettings.displaySettingsStampScreen.settingDateTimeColor')) {
						vm.settingDateTimeColor(setting.portalStampSettings.displaySettingsStampScreen.settingDateTimeColor);
					}

					if (_.has(setting, 'portalStampSettings.displaySettingsStampScreen.serverCorrectionInterval')) {
						vm.settingCountTime(setting.portalStampSettings.displaySettingsStampScreen.serverCorrectionInterval);
					}
					if (_.has(setting, 'portalStampSettings.displaySettingsStampScreen.resultDisplayTime')) {
						vm.resultDisplayTime(setting.portalStampSettings.displaySettingsStampScreen.resultDisplayTime);
					}

					vm.$date.interval(vm.settingCountTime() * 60000);

					setInterval(() => {
						if (vm.countTime() == vm.settingCountTime() * 60) {
							vm.systemDate(moment(vm.$date.now()));

							this.$ajax(requestUrl.getStampToSuppress).then((data: IStampToSuppress) => {

								vm.buttonSetting(data);

							}).always(() => {
								vm.countTime(0);
							});

						} else {

							vm.systemDate(vm.systemDate().add(1, 'seconds'));
							vm.countTime(vm.countTime() + 1);

						}
					}, 1000);
				}
			}).always(() => {
				this.$blockui("clear");
			});;

			this.$blockui("invisible");
			vm.$ajax(requestUrl.getEmployeeStampData).then((data: Array<Array<IStampInfoDisp>>) => {
				this.$blockui("clear");
				let items = _(data).flatMap('stampRecords').value() as any[];

				items = _.orderBy(items, ['stampTimeWithSec'], ['desc']);

				vm.stampDatas(items || []);

				if (!vm.isScreenCD()) {

					if (vm.screenMode() == 'a' || vm.screenMode() == 'b') {

						$("#fixed-table").ntsFixedTable({ height: 53, width: 215 });
					} else {

						if (!vm.screenMode()) {

							$("#fixed-table").ntsFixedTable({ height: 85, width: 200 });
						}
					}
				}
			}).always(() => {
				this.$blockui("clear");
			});;
			vm.getStampToSuppress();

		}).always(() => {
			this.$blockui("clear");
		});
	}

	public getDateColor(data: IStampInfoDisp) {

		let day = moment.utc(data.stampTimeWithSec).day(),

			dayColor = _.find(daysColor, ['day', day]);

		return dayColor ? dayColor.color : '#000000';
	}

	public getStampToSuppress() {
		let vm = this;
		vm.$blockui("invisible");
		this.$ajax(requestUrl.getStampToSuppress).then((data: IStampToSuppress) => {
			vm.$blockui("clear");
			vm.buttonSetting(data);
		});
	}

	public toTopPage() {
		let vm = this;
		vm.$jump('com', '/view/ccg/008/a/index.xhtml');
	}

	public getNotUseMessage() {
		let vm = this,
			messageItem = _.find(notUseMessage, ['value', vm.usedSatus()]);

		return messageItem ? this.$i18n.message(messageItem.text, [this.$i18n.text('KDP001_1')]) : '';

	}

	public getStampTime(data: IStampInfoDisp) {

		return moment.utc(data.stampTimeWithSec).format("HH:mm");
	}

	public convertToShortMDW(data: IStampInfoDisp) {

		return _.has(data, 'stampTimeWithSec') ? moment.utc(data.stampTimeWithSec).format("MM/DD(ddd)") : '';
	}

	public getSystemDate() {
		let vm = this;

		return vm.systemDate().format("YYYY/MM/DD(ddd)");
	}

	public getSystemTime() {
		let vm = this,
			time = vm.systemDate().format("HH:mm");


		return time;
	}



	public getBGButton(data: IButtonSettingsDto) {
		let vm = this,
			buttonNo = data.buttonPositionNo,
			setting = vm.buttonSetting(),
			color = DEFAULT_GRAY,
			item: IButtonSettingsDto = _.find(vm.buttons(), ['buttonPositionNo', data.buttonPositionNo]);
		if (!!item) {
			color = item.buttonDisSet.backGroundColor;
		}

		if (buttonNo == 1) {
			return setting.goingToWork ? DEFAULT_GRAY : color;
		}
		if (buttonNo == 2) {
			return setting.departure ? DEFAULT_GRAY : color;
		}
		if (buttonNo == 3) {
			return setting.goOut ? DEFAULT_GRAY : color;
		}
		if (buttonNo == 4) {
			return setting.turnBack ? DEFAULT_GRAY : color;
		}
		return false;

	}

	public stamp(vm: KDP001AViewModel, data) {

		let cmd: IRegisterStampInputCommand = {
			datetime: vm.systemDate().format('YYYY/MM/DD HH:mm:ss'),
			buttonPositionNo: data.buttonPositionNo,
			refActualResults: {
				cardNumberSupport: null,
				workLocationCD: null,
				workTimeCode: null,
				overtimeDeclaration: null
			}

		};

		this.$blockui("invisible");
		this.$ajax(requestUrl.registerStampInput, cmd).then((result) => {
			switch (data.buttonPositionNo) {
				case 1:
				case 3:
				case 4:
					vm.openDialogB(result, data.buttonPositionNo);
					break;

				case 2: {
					if (vm.stampResultDisplay().notUseAttr === 1) {
						vm.openDialogC(result, data.buttonPositionNo);
					} else {
						vm.openDialogB(result, data.buttonPositionNo);
					}
					break;
				}
			}
		}).fail((error) => {
			this.$dialog.alert({ messageId: error.messageId });
		}).always(() => {
			this.$blockui("clear");
		});
	}

	public openDialogB(dateParam, buttonDisNo) {

		let vm = this;
		nts.uk.ui.windows.setShared("resultDisplayTime", vm.resultDisplayTime());

		nts.uk.ui.windows.setShared("infoEmpToScreenB", {
			employeeId: vm.$user.employeeId,
			employeeCode: vm.$user.employeeCode,
			mode: Mode.Personal,
		});
		nts.uk.ui.windows.sub.modal('/view/kdp/002/b/index.xhtml').onClosed(function(): any {
			vm.$blockui("invisible");
			vm.$ajax(requestUrl.getOmissionContents, { pageNo: DEFAULT_PAGE_NO, buttonDisNo: buttonDisNo , stampMeans: STAMP_MEANS_PORTAL}).then((res) => {
				if (res && res.dailyAttdErrorInfos && res.dailyAttdErrorInfos.length > 0) {

					vm.$window.storage('KDP010_2T', res);

					nts.uk.ui.windows.sub.modal('/view/kdp/002/t/index.xhtml').onClosed(function(): any {

						let returnData = nts.uk.ui.windows.getShared('KDP010_T');
						if (!returnData.isClose && returnData.errorDate) {

							let transfer = returnData.btn.transfer;
							vm.$jump(returnData.btn.screen, transfer);
						}

						vm.reLoadStampDatas();
						vm.getStampToSuppress();
					});
				} else {
					vm.reLoadStampDatas();
					vm.getStampToSuppress();
				}
			}).always(() => {
				vm.$blockui("hide");
			});
		});
	}

	public openDialogC(dateParam, buttonDisNo) {
		let vm = this;

		nts.uk.ui.windows.setShared('KDP010_2C', vm.stampResultDisplay().displayItemId);

		nts.uk.ui.windows.setShared("infoEmpToScreenC", {
			employeeId: vm.$user.employeeId,
			employeeCode: vm.$user.employeeCode,
			mode: Mode.Personal,
			stampDate: dateParam
		});

		nts.uk.ui.windows.sub.modal('/view/kdp/002/c/index.xhtml').onClosed(function(): any {
			vm.$blockui("invisible");
			vm.$ajax(requestUrl.getOmissionContents, { pageNo: DEFAULT_PAGE_NO, buttonDisNo: buttonDisNo, stampMeans: STAMP_MEANS_PORTAL }).then((res) => {
				if (res && res.dailyAttdErrorInfos && res.dailyAttdErrorInfos.length > 0) {

					vm.$window.storage('KDP010_2T', res);

					nts.uk.ui.windows.sub.modal('/view/kdp/002/t/index.xhtml').onClosed(function(): any {
						let returnData = nts.uk.ui.windows.getShared('KDP010_T');
						if (!returnData.isClose && returnData.errorDate) {

							let transfer = returnData.btn.transfer;
							vm.$jump(returnData.btn.screen, transfer);
						}

						vm.reLoadStampDatas();
						vm.getStampToSuppress();
					});
				} else {
					vm.reLoadStampDatas();
					vm.getStampToSuppress();
				}
			}).always(() => {
				vm.$blockui("clear");
			});
		});
	}

	public reLoadStampDatas() {
		let vm = this;
		vm.$blockui("invisible");
		vm.$ajax(requestUrl.getEmployeeStampData).then((data: Array<Array<IStampInfoDisp>>) => {
			vm.$blockui("clear");
			let items = _(data).flatMap('stampRecords').value() as any[];

			items = _.orderBy(items, ['stampTimeWithSec'], ['desc']);

			vm.stampDatas(items || []);

		}).always(() => {
			vm.$blockui("clear");
		});;
	}

	public isScreenCD() {
		let vm = this;
		return vm.screenMode() == 'c' || vm.screenMode() == 'd';
	}

	public showButtonGoOutAndBack() {
		let vm = this;
		return vm.screenMode() == 'b' || vm.screenMode() == 'c';
	}

	public showTable() {
		let vm = this;
		console.log(vm.screenMode());
		return vm.screenMode() != 'a' && vm.screenMode() != 'b';
	}
	public isWidget() {
		let vm = this;
		return !vm.screenMode();
	}

	public getTextAlign(data: IStampInfoDisp) {


		let value = data.buttonValueType;
		if (ButtonType.GOING_TO_WORK == value || ButtonType.RESERVATION_SYSTEM == value) {

			return 'left';

		}

		if (ButtonType.WORKING_OUT == value) {

			return 'right';

		}

		return 'center';
	}

}

enum ButtonType {
	// 系

	GOING_TO_WORK = 1,
	// 系

	WORKING_OUT = 2,
	// "外出系"

	GO_OUT = 3,
	// 戻り系

	RETURN = 4,
	// 予約系

	RESERVATION_SYSTEM = 5
}

interface IStampToSuppress {
	/**
	 * 出勤
	 */
	goingToWork: boolean;
	/**
	 * 退勤
	 */
	departure: boolean;
	/**
	 * 外出
	 */
	goOut: boolean;
	/**
	 * 戻り
	 */
	turnBack: boolean;
}

interface IStampResultDisplay {
	companyId: string;
	displayItemId: Array<number>;
	notUseAttr: number
}

interface IStampSetting {
	portalStampSettings: IPortalStampSettingsDto;
	stampResultDisplayDto: IStampResultDisplay;
}

interface IPortalStampSettingsDto {

	// 打刻画面の表示設定
	displaySettingsStampScreen: IDisplaySettingsStampScreenDto;

	// 打刻ボタン設定
	buttonSettings: Array<IButtonSettingsDto>;
}

interface IDisplaySettingsStampScreenDto {
	resultDisplayTime: number
	serverCorrectionInterval: number
	/** 打刻画面の日時の色設定 */
	settingDateTimeColor: ISettingDateTimeColorOfStampScreenDto;
}

interface ISettingDateTimeColorOfStampScreenDto {
	/** 文字色 */
	textColor: string;

	/** 背景色 */
	backgroundColor: string;
}

interface IButtonSettingsDto {
	/** ボタン位置NO */
	buttonPositionNo: number;

	/** ボタンの表示設定 */
	buttonDisSet: IButtonDisSetDto;

}
interface IButtonDisSetDto {
	/** ボタン名称設定 */
	buttonNameSet: IButtonNameSetDto;

	/** 背景色 */
	backGroundColor: string;
}

interface IButtonNameSetDto {
	/** 文字色 */
	textColor: string;

	/** ボタン名称 */
	buttonName: string;
}
interface IStampInfoDisp {
	/**
	 * 打刻日時
	 */
	stampTimeWithSec: Date;
	/**
	 * 打刻区分
	 */
	stampAtr: string;
	/**
	 * 打刻
	 */
	stamp: IStamp;

	correctTimeStampValue: number;

	stampHow: number;

	buttonValueType: number;
}

interface IStamp {
	/**
	 * 打刻する方法
	 */
	relieve: IRelieve;
}

interface IRelieve {
	/** 打刻手段*/
	stampMeans: IStampMeans;
}

interface IStampMeans {
	value: number;

	name: string;
}
interface IRegisterStampInputCommand {
	/**
	 * 打刻日時
	 */
	datetime: Date;

	/**
	 * ボタン位置NO
	 */

	buttonPositionNo: number;
	/**
	 * 実績への反映内容
	 */

	refActualResults: IRefectActualResultCommand;
}

interface IRefectActualResultCommand {

	cardNumberSupport: string;

	/**
	 * 打刻場所コード 勤務場所コード old
	 */

	workLocationCD: string;

	/**
	 * 就業時間帯コード
	 */

	workTimeCode: string;

	/**
	 * 時間外の申告
	 */

	overtimeDeclaration: IOvertimeDeclarationComamnd;
}

interface IOvertimeDeclarationComamnd {
	overTime: number;

	/**
	 * 時間外深夜時間
	 */
	overLateNightTime: number;
}