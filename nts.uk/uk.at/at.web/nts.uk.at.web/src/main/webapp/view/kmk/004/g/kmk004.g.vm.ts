/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.kmk004.g {

	const API = {

		REGISTER: '/at/record/stamp/employment/system/register-stamp-input'
	};

	@bean()
	export class ViewModel extends ko.ViewModel {
		selected = ko.observable('Com_Company');

		startYM: KnockoutObservable<number> = ko.observable();

		created() {

		}

		mounted() {

		}
	}

}
