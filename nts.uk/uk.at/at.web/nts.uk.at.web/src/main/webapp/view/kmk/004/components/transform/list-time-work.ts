/// <reference path="../../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.at.view.kmk004.components {

	import IYear = nts.uk.at.view.kmk004.components.transform.IYear;
	import SIDEBAR_TYPE = nts.uk.at.view.kmk004.p.SIDEBAR_TYPE;

	interface Params {
		selectedYear: KnockoutObservable<number | null>;
		checkEmployee?: KnockoutObservable<boolean>
		years: KnockoutObservableArray<IYear>;
		workTimes: KnockoutObservableArray<WorkTimeL>;
		selectId: KnockoutObservable<string>;
		type: SIDEBAR_TYPE;
	}

	const API = {
		GET_WORK_TIME_BY_COM: 'screen/at/kmk004/viewL/getWorkingHoursByCompany',
		GET_WORK_TIME_BY_WKP: 'screen/at/kmk004/viewM/getWorkingHoursByWkp',
		GET_WORK_TIME_BY_EMP: 'screen/at/kmk004/viewN/getWorkingHoursByEmp',
		GET_WORK_TIME_BY_SHA: 'screen/at/kmk004/viewO/getWorkingHoursByEmployee'
	};

	const DEFOR_TYPE = 1;

	const template = `
        <div class="list-time">
            <table>
                <tbody>
                    <tr>
                        <!-- ko if: checkEmployee -->
                            <td class= "check-row1"></td>
                        <!-- /ko -->
                        <td class= "label-row1">
                            <div data-bind="i18n: 'KMK004_221'"></div>
                        </td>
                        <td class= "label-row2">
                            <div data-bind="i18n: 'KMK004_222'"></div>
                        </td>
                    </tr>
                    <!-- ko foreach: workTimes -->
                    <tr>
                        <!-- ko if: $parent.checkEmployee -->
                            <td class= "check-column1">
                                <div data-bind="ntsCheckBox: { checked: $data.check }"></div>
                            </td>
                        <!-- /ko -->
                        <td class="label-column1" data-bind="text: $data.nameMonth"></td>
                        <td class="label-column2">
                            <!-- ko if: $parent.checkEmployee -->
                                <input class="lable-input" 
                                    data-bind="ntsTimeEditor: {
                                        value: $data.laborTime,
                                        enable: $data.check,
                                        inputFormat: 'time',
                                        option: {
                                            width: '60px',
                                            textalign: 'center'}, 
                                        mode: 'time'}" />
                            <!-- /ko -->
                            <!-- ko ifnot: $parent.checkEmployee -->
                                <input class="lable-input" 
                                    data-bind="ntsTimeEditor: {
                                        value: $data.laborTime,
                                        enable: $parent.checkNullYear, 
                                        inputFormat: 'time',
                                        option: {
                                            width: '60px',
                                            textalign: 'center'}, 
                                        mode: 'time'}"/>
                            <!-- /ko -->
                        </td>
                    </tr>
                    <!-- /ko -->
                    <!-- ko ifnot: checkEmployee -->
                        <tr>
                            <td class="label-column1">
                                <div data-bind="i18n: 'KMK004_223'"></div>
                            </td>
                            <td class="label-column2">
                                <div data-bind="text: total"></div>
                            </td>
                        </tr>
                    <!-- /ko -->
                </tbody>
            </table>
        </div>
    `;

	@component({
		name: 'time-work',
		template
	})

	class ListTimeWork extends ko.ViewModel {

		public workTimes: KnockoutObservableArray<WorkTimeL> = ko.observableArray([]);
		public total: KnockoutObservable<string> = ko.observable('');
		public selectedYear: KnockoutObservable<number | null> = ko.observable(null);
		public checkNullYear: KnockoutObservable<boolean> = ko.observable(false);
		public checkEmployee: KnockoutObservable<boolean> = ko.observable(false);
		public years: KnockoutObservableArray<IYear>;
		public type: SIDEBAR_TYPE;
		public selectId: KnockoutObservable<string>;

		created(params: Params) {
			const vm = this;
			vm.selectedYear = params.selectedYear;
			vm.years = params.years;
			vm.checkEmployee = params.checkEmployee;
			vm.workTimes = params.workTimes;
			vm.selectId = params.selectId;
			vm.type = params.type;
			vm.initList(9999, false);

			vm.workTimes.subscribe((wts) => {
				const total: number = wts.reduce((p, c) => p += Number(c.laborTime()), 0);
				const first: string = Math.floor(total / 60) + '';
				var last: string = total % 60 + '';

				if (last.length < 2) {
					last = '0' + last;
				}

				vm.total(first + ':' + last);

			});

			vm.selectedYear
				.subscribe(() => {
					vm.reloadData();
				});

		}

		reloadData() {
			const vm = this;
			const comInput = { workType: DEFOR_TYPE, year: vm.selectedYear()};
			const wkpInput = { workplaceId: vm.selectId, workType: DEFOR_TYPE, year: vm.selectedYear()};
			const empInput = { employmentCode: vm.selectId, workType: DEFOR_TYPE, year: vm.selectedYear()};
			const shaInput = { sId: vm.selectId, workType: DEFOR_TYPE, year: vm.selectedYear()};

			if (vm.type !== 'Com_Company' && !vm.selectId){
				return;
			}
			if (ko.unwrap(vm.selectedYear) != null) {
				vm.checkNullYear(true);
			}

			switch (vm.type) {
				case 'Com_Company':
					vm.$ajax(API.GET_WORK_TIME_BY_COM, comInput)
						.then((data: IWorkTimeL[]) => {
							if (data.length > 0) {
								const workTime: IWorkTimeL[] = [];

								data.map(m => {
									const s: IWorkTimeL = { check: true, yearMonth: m.yearMonth, laborTime: m.laborTime };
									workTime.push(s);
								});

								vm.workTimes(workTime.map(m => new WorkTimeL({ ...m, parent: vm.workTimes })));
							}
						});
					break;

				case 'Com_Workplace':
					vm.$ajax(API.GET_WORK_TIME_BY_WKP, wkpInput)
						.then((data: IWorkTimeL[]) => {
							if (data.length > 0) {
								const workTime: IWorkTimeL[] = [];

								data.map(m => {
									const s: IWorkTimeL = { check: true, yearMonth: m.yearMonth, laborTime: m.laborTime };
									workTime.push(s);
								});

								vm.workTimes(workTime.map(m => new WorkTimeL({ ...m, parent: vm.workTimes })));
							}
						});
					break;

				case 'Com_Employment':
					vm.$ajax(API.GET_WORK_TIME_BY_EMP, empInput)
						.then((data: IWorkTimeL[]) => {
							if (data.length > 0) {
								const workTime: IWorkTimeL[] = [];
								
								data.map(m => {
									const s: IWorkTimeL = { check: true, yearMonth: m.yearMonth, laborTime: m.laborTime };
									workTime.push(s);
								});

								vm.workTimes(workTime.map(m => new WorkTimeL({ ...m, parent: vm.workTimes })));
							}
						});
					break;

				case 'Com_Person':
					vm.$ajax(API.GET_WORK_TIME_BY_SHA, shaInput)
						.then((data: IWorkTimeL[]) => {
							if (data.length > 0) {
								const workTime: IWorkTimeL[] = [];
								var check: boolean = true;
								
								data.map(m => {
									const s: IWorkTimeL = { check: check, yearMonth: m.yearMonth, laborTime: m.laborTime };
									workTime.push(s);
								});

								vm.workTimes(workTime.map(m => new WorkTimeL({ ...m, parent: vm.workTimes })));
							}
						});
					break;
			}
		}

		initList(year: number, check: boolean) {
			const vm = this
			var check: boolean = true;

			if (ko.unwrap(vm.checkEmployee)) {
				check = false;
			}

			const IWorkTime1: IWorkTimeL[] = [{ check: check, yearMonth: year * 100 + 1, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 2, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 3, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 4, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 5, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 6, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 7, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 8, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 9, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 10, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 11, laborTime: 0 },
			{ check: check, yearMonth: year * 100 + 12, laborTime: 0 }];
			vm.workTimes(IWorkTime1.map(m => new WorkTimeL({ ...m, parent: vm.workTimes })));
		}
	}
}

interface IWorkTimeL {
	check: boolean;
	yearMonth: number;
	laborTime: number;
}

class WorkTimeL {
	check: KnockoutObservable<boolean> = ko.observable(false);
	yearMonth: KnockoutObservable<number | null> = ko.observable(null);
	nameMonth: KnockoutObservable<string> = ko.observable('');
	laborTime: KnockoutObservable<number> = ko.observable(0);

	constructor(params?: IWorkTime & { parent: KnockoutObservableArray<WorkTimeL> }) {
		const md = this;

		md.create(params);
		this.laborTime.subscribe(c => params.parent.valueHasMutated());
	}

	public create(param?: IWorkTime) {
		const md = this;
		md.check(param.check);
		md.yearMonth(param.yearMonth);
		md.laborTime(param.laborTime);

		switch (param.yearMonth.toString().substring(4, 6)) {
			case "01":
				md.nameMonth('1月度')
				break
			case "02":
				md.nameMonth('2月度')
				break
			case "03":
				md.nameMonth('3月度')
				break
			case "04":
				md.nameMonth('4月度')
				break
			case "05":
				md.nameMonth('5月度')
				break
			case "06":
				md.nameMonth('6月度')
				break
			case "07":
				md.nameMonth('7月度')
				break
			case "08":
				md.nameMonth('8月度')
				break
			case "09":
				md.nameMonth('9月度')
				break
			case "10":
				md.nameMonth('10月度')
				break
			case "11":
				md.nameMonth('11月度')
				break
			case "12":
				md.nameMonth('12月度')
				break
		}
	}
}

