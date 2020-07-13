module nts.uk.at.view.kdp004.a {

	import ajax = nts.uk.request.ajax;

	export module service {
		let url = {
			startPage: 'at/record/stamp/finger/get-finger-stamp-setting',
			stampInput: 'at/record/stamp/finger/register-finger-stamp',
			confirmUseOfStampInput: 'at/record/stamp/employment_system/confirm_use_of_stamp_input',
			loginAdminMode: 'ctx/sys/gateway/kdp/login/adminmode',
			loginEmployeeMode: 'ctx/sys/gateway/kdp/login/employeemode',
			fingerAuth: '',
			getError: 'at/record/stamp/employment_system/get_omission_contents',
			getStampToSuppress: 'at/record/stamp/employment_system/get_stamp_to_suppress',
			getLogginSetting: 'ctx/sys/gateway/kdp/login/getLogginSetting'
		}

		export function startPage(): JQueryPromise<any> {
			return ajax("at", url.startPage);
		}

		export function stampInput(data): JQueryPromise<any> {
			return ajax("at", url.stampInput, data);
		}

		export function confirmUseOfStampInput(data): JQueryPromise<any> {
			return ajax("at", url.confirmUseOfStampInput, data);
		}

		export function login(isAdmin, data) {
			return ajax("at", isAdmin ? url.loginAdminMode : url.loginEmployeeMode, data);
		}

		export function fingerAuth(param) {
			let dfd = $.Deferred<any>();
			//dfd.resolve({ result: Math.floor(Math.random() * 2) === 1 ? true : false });

			dfd.resolve({ result: param.fingerAuthCkb, messageId: param.selectedMsg });
			//return ajax("at", url.fingerAuth);
			return dfd.promise();
		}

		export function getError(data): JQueryPromise<any> {
			return ajax("at", url.getError, data);
		}

		export function getStampToSuppress(): JQueryPromise<any> {
			return ajax("at", url.getStampToSuppress);
		}

		export function getLogginSetting(): JQueryPromise<any> {
			return ajax("at", url.getLogginSetting);
		}
	}

}

