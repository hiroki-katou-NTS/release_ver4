/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />


module nts.uk.at.ktg005.a {

	const requestUrl = {
		getEmployeeStampData: 'at/record/stamp/employment_system/get_employee_stamp_data',
		confirmUseOfStampInput: 'at/record/stamp/employment_system/confirm_use_of_stamp_input',
		registerStampInput: 'at/record/stamp/employment_system/register_stamp_input'
	}

	@bean()
	export class ViewModel extends ko.ViewModel {

		executionAppResult: KnockoutObservable<IExecutionAppResult> = ko.observable({
			name: '',
			approvedNumber: null,
			unapprovedNumber: null,
			denialNumber: null,
			remandNumber: null,
			dueDate: '',
			useAppDeadLine: 0,
			detailSettingAppStatus: []
		});


		constructor() {
			super();
		}

		created(params: any) {

			let vm = this;

			//vm.$ajax(requestUrl.getSettingStampInput).then((setting: IStampSetting) => {

			//});
		}

		getText(param: string) {
			let vm = this;
			return vm.$i18n.text("KTG005_7", [param]);
		}

		getDate(param: string) {
			let vm = this;

			if (vm.executionAppResult().useAppDeadLine === 1) {

				return moment(param, 'yyyy/MM/DD HH:mm:ss').format('MM/dd(W)')
			}

			return vm.$i18n.text("KTG005_8");
		}


		displayDetail(formType: number) {

			let vm = this,

				detailSetting = _.find(vm.executionAppResult().detailSettingAppStatus, ['item', ApplicationStatusWidgetItem[formType]]);


			return detailSetting ? detailSetting.appDisplayAtr === 1 : false;

		}

	}

	export interface IExecutionAppResult {
		//名称
		name: string;
		//承認件数
		approvedNumber: number;
		//未承認件数
		unapprovedNumber: number;
		//否認件数
		denialNumber: number;
		//差し戻し件数
		remandNumber: number;
		//締め切り日									
		dueDate: string;
		//申請締切利用設定
		useAppDeadLine: number;
		//申請状況の詳細設定
		detailSettingAppStatus: Array<IDetailSettingAppStatus>;
	}

	export interface IDetailSettingAppStatus {
		//表示区分
		appDisplayAtr: number;
		//項目
		item: number;
	}

	enum ApplicationStatusWidgetItem {
		//承認された件数
		NumberOfApprovedCases = 0,
		//未承認件数
		NumberOfUnApprovedCases = 1,
		//否認された件数
		NumberOfDenial = 2,
		//差し戻し件数
		NumberOfRemand = 3,
		//今月の申請締め切り日
		ApplicationDeadlineForThisMonth = 4
	}
}

