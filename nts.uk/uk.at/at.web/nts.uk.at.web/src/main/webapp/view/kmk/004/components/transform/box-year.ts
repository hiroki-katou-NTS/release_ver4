/// <reference path="../../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kmk004.components.transform {

	import SIDEBAR_TYPE = nts.uk.at.view.kmk004.p.SIDEBAR_TYPE;
	import KMK004_API = nts.uk.at.view.kmk004.KMK004_API;

	interface Params {
		selectedYear: KnockoutObservable<number | null>;
		type: SIDEBAR_TYPE;
		selectedId?: KnockoutObservable<string>;
		isLoadInitData: KnockoutObservable<boolean>;
	}

	const template = `
		<button id = "btn_year" data-bind="enable: initBtnEnable, click: openQDialog, i18n: 'KMK004_233'"></button>
        <div tabindex="6" class="listbox">
            <div id="list-box" data-bind="ntsListBox: {
                options: years,
                optionsValue: 'year',
                optionsText: 'year',
                multiple: false,
                value: selectedYear,
                rows: 5,
                columns: [
                    { key: 'isChanged', length: 1 },
                    { key: 'yearName', length: 4 }
                ]}"></div>
        </div>
        <div class="note color-attendance" data-bind="i18n: 'KMK004_212'"></div>
    `;

	@component({
		name: 'box-year',
		template
	})

	class BoxYear extends ko.ViewModel {

		public years: KnockoutObservableArray<IYear> = ko.observableArray([]);
		public selectedYear: KnockoutObservable<number | null> = ko.observable(null);
		public type: SIDEBAR_TYPE;
		public selectedId: KnockoutObservable<string> = ko.observable('');
		isLoadInitData: KnockoutObservable<boolean>;
		initBtnEnable: KnockoutObservable<boolean> = ko.observable(false);

		constructor(private params: Params) {
			super();
			const vm = this;
			
			vm.isLoadInitData = vm.params.isLoadInitData;
			vm.isLoadInitData.subscribe((value: boolean) => {
				if (value) {
					if (vm.type == 'Com_Company') {
						vm.initData();
					} else {
						vm.loadData();
					}
					vm.isLoadInitData(false);
				}
			});
		}

		created(params: Params) {
			const vm = this;
			vm.selectedYear = params.selectedYear;
			vm.type = params.type;
			vm.selectedId = params.selectedId;

			if (vm.type != 'Com_Person') {
				vm.initBtnEnable(true);
			}
		}

		mounted() {
			const vm = this;
			vm.initData(0);
			vm.selectedId
				.subscribe(() => {
					vm.loadData(0);
					if (vm.params.type == 'Com_Person' && vm.selectedId() != '') {
						vm.initBtnEnable(true);
					}

				});

			vm.selectedId.valueHasMutated();
		}

		initData(selectedIndex: number = 0) {

			const vm = this;
			vm.years([]);
			switch (vm.type) {
				case 'Com_Company':
					vm.$ajax(KMK004_API.COM_INIT_SCREEN)
						.then((data: any) => {
							data = _.orderBy(data.years, ['year'], ['desc']);

							_.forEach(data, ((value: any) => {
								const y: IYear = new IYear(value.year);
								vm.years.push(y);
							}));
						})
						.then(() => {
							if (ko.unwrap(vm.years) != []) {
								vm.selectedYear(ko.unwrap(vm.years)[selectedIndex].year);
							} else {
								vm.selectedYear(null);
							}
						});
					break;

				case 'Com_Workplace':
					if (ko.unwrap(vm.selectedId) != '') {
						vm.$ajax(KMK004_API.WKP_INIT_SCREEN)
							.then((data: any) => {
								data = _.orderBy(data.years, ['year'], ['desc']);

								_.forEach(data, ((value: any) => {
									const y: IYear = new IYear(value.year);
									vm.years.push(y);
								}));
							})
							.then(() => {
								if (ko.unwrap(vm.years) != []) {
									vm.selectedYear(ko.unwrap(vm.years)[selectedIndex].year);
								} else {
									vm.selectedYear(null);
								}
							});
					}
					break;

				case 'Com_Employment':
					if (ko.unwrap(vm.selectedId) != '') {
						vm.$ajax(KMK004_API.EMP_INIT_SCREEN)
							.then((data: any) => {
								data = _.orderBy(data.years, ['year'], ['desc']);
								_.forEach(data, ((value: any) => {
									const y: IYear = new IYear(value.year);
									vm.years.push(y);
								}));
							})
							.then(() => {
								if (ko.unwrap(vm.years) != []) {
									vm.selectedYear(ko.unwrap(vm.years)[selectedIndex].year);
								} else {
									vm.selectedYear(null);
								}
							});
					}
					break;

				case 'Com_Person':
					break;
			}

		}

		loadData(selectedIndex: number = 0) {

			const vm = this;
			vm.years([]);
			switch (vm.type) {
				case 'Com_Company':
					break;

				case 'Com_Workplace':
					if (ko.unwrap(vm.selectedId) != '') {
						vm.$ajax(KMK004_API.WKP_SELECT + '/' + ko.toJS(vm.selectedId()))
							.then((data: any) => {
								data = _.orderBy(data.years, ['year'], ['desc']);
								_.forEach(data, ((value: any) => {
									const y: IYear = new IYear(value.year);
									vm.years.push(y);
								}));
							})
							.then(() => {
								if (ko.unwrap(vm.years) != []) {
									vm.selectedYear(ko.unwrap(vm.years)[selectedIndex].year);
								} else {
									vm.selectedYear(null);
								}
							});
					}
					break;

				case 'Com_Employment':
					if (ko.unwrap(vm.selectedId) != '') {
						vm.$ajax(KMK004_API.EMP_SELECT + '/' + ko.toJS(vm.selectedId()))
							.then((data: any) => {
								data = _.orderBy(data.years, ['year'], ['desc']);
								_.forEach(data, ((value: any) => {
									const y: IYear = new IYear(value.year);
									vm.years.push(y);
								}));
							})
							.then(() => {
								if (ko.unwrap(vm.years) != []) {
									vm.selectedYear(ko.unwrap(vm.years)[selectedIndex].year);
								} else {
									vm.selectedYear(null);
								}
							});
					}
					break;

				case 'Com_Person':
					if (ko.unwrap(vm.selectedId) != null && ko.unwrap(vm.selectedId) != '') {
						vm.$ajax(KMK004_API.SHA_SELECT + '/' + ko.unwrap(vm.selectedId))
							.then((data: any) => {
								data = _.orderBy(data.years, ['year'], ['desc']);
								vm.years([]);
								_.forEach(data, ((value: any) => {
									const y: IYear = new IYear(value.year);
									vm.years.push(y);
								}));
							})
							.then(() => {

								if (ko.unwrap(vm.years) != []) {
									vm.selectedYear(ko.unwrap(vm.years)[selectedIndex].year);
								} else {
									vm.selectedYear(null);
								}
							});
					}
					break;
			}

		}

		openQDialog() {
			const vm = this;
			const param = { years: ko.unwrap(vm.years).map((m: IYear) => m.year) };
			vm.$window.modal('/view/kmk/004/q/index.xhtml', param).then((result) => {
				if (result) {
					vm.years.push(new IYear(parseInt(result.year), true));
					vm.years(_.orderBy(ko.unwrap(vm.years), ['year'], ['desc']));
					vm.selectedYear(ko.unwrap(vm.years)[0].year);
				}
			});
		}
	}

	export class IYear {
		isNew: boolean = false;
		isChanged: string;
		year: number;
		yearName: string;

		constructor(year: number, isNew?: boolean) {
			this.year = year;
			this.yearName = year.toString() + '年度';
			if (isNew) {
				this.isNew = isNew;
				this.isChanged = '＊';
			}
		}
	}

}
