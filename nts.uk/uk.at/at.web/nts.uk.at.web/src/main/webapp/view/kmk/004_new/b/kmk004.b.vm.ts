/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kmk004_new.b {
	import share = nts.uk.at.view.kmk004_new;
	export module b {

		@bean()
		export class ViewModel extends ko.ViewModel {

			public tabs: KnockoutObservableArray<string> = ko.observableArray([]);
			public tabSetting: share.ParamsTabSetting = new share.ParamsTabSetting();

			create() {
			}

			mounted() {
				const vm = this;
				vm.tabs(['Com_Company', 'Com_Workplace', 'Com_Person', 'Com_Employment']);
			}
		}
	}
}
