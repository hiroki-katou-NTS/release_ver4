/// <reference path="../../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.ui.at.ksu002.a {
	import c = nts.uk.ui.calendar;

	const template = `
		<div class="cf" data-bind="
			attr: {
				tabindex: $component.params.tabIndex
			},
			ntsDatePicker: { 
				value: yearMonth,
				dateFormat: 'yearmonth' ,
				valueFormat: 'YYYYMM',
				fiscalMonthsMode: true,
				defaultClass: 'round-orange',
				showJumpButtons: true
			}"></div>
		<div class="title-label">
			<span data-bind="i18n: 'KSU002_23'"></span>
			<span data-bind="i18n: 'KSU002_7'"></span>
		</div>		
        <div data-bind="
			attr: {
				tabindex: $component.params.tabIndex
			},
			ntsComboBox: {
				width: '250px',
				name: $component.$i18n('KSU002_22'),
				value: $component.selectedRangeIndex,
				options: $component.dateRanges,
				optionsValue: 'id',
				optionsText: 'title',
				editable: false,
				selectFirstIfNull: true,
				columns: [
					{ prop: 'title', length: 10 },
				]
			}"></div>
		<div class="title-label">
			<span data-bind="i18n: 'KSU002_6'"></span>
			<span data-bind="i18n: 'KSU002_7'"></span>
		</div>
		<div class="cf" data-bind="
			attr: {
				tabindex: $component.params.tabIndex
			},
			ntsSwitchButton: {
				name: $i18n('KSU002_6'),
				value: $component.mode,
				options: [
					{ code: 1, name: $i18n('KSU002_8') },
					{ code: 0, name: $i18n('KSU002_9') }
				],
				optionsText: 'name',
				optionsValue: 'code'
			}"></div>					
		<style type="text/css" rel="stylesheet">
            .title-date {
				margin: 5px 0;
				border: 1px solid #cccccc;
				background-color: #edfac2;
				border-radius: 5px;
				padding: 6px;
				display: inline-block;
			}
			.title-date>div {
				float: left;
				display: block;
			}
			.title-date>div.title-label {
				padding: 0 25px;
				line-height: 32px;
			}
			.title-date .nts-switch-button {
				min-width: 90px;
				min-height: 32px;
			}
			.title-date .nts-datepicker-wrapper>input,
			.title-date .nts-datepicker-wrapper>button {
				vertical-align: top;
			}
			.title-date .nts-datepicker-wrapper>input {
				height: 20px;
			}
			.title-date .nts-datepicker-wrapper>button {
				height: 29px;
			}
			.title-date .nts-datepicker-wrapper.arrow-bottom:before,
			.title-date .nts-datepicker-wrapper.arrow-bottom:after {
				left: 45px;
			}
        </style>
        <style type="text/css" rel="stylesheet" data-bind="html: $component.style"></style>
    `;

	const COMPONENT_NAME = 'title-date';

	@handler({
		bindingName: COMPONENT_NAME,
		validatable: true,
		virtual: false
	})
	export class TitleDateComponentBindingHandler implements KnockoutBindingHandler {
		init(element: HTMLElement, valueAccessor: () => any, allBindingsAccessor: KnockoutAllBindingsAccessor, viewModel: any, bindingContext: KnockoutBindingContext): void | { controlsDescendantBindings: boolean; } {
			const name = COMPONENT_NAME;
			const dateRange = valueAccessor();
			const mode = allBindingsAccessor.get('mode');
			const workplaceId = allBindingsAccessor.get('workplace-id');
			const tabIndex = element.getAttribute('tabindex') || '1';
			const params = { mode, dateRange, tabIndex, workplaceId };
			const component = { name, params };

			element.classList.add('cf');
			element.classList.add('title-date');

			element.removeAttribute('tabindex');

			ko.applyBindingsToNode(element, { component }, bindingContext);

			return { controlsDescendantBindings: true };
		}
	}

	const API = {
		BASE_DATE: '/screen/ksu/ksu002/getListOfPeriodsClose'
	};

	@component({
		name: COMPONENT_NAME,
		template
	})
	export class TitleDateComponent extends ko.ViewModel {
		public yearMonth: KnockoutObservable<string> = ko.observable(moment().format('YYYYMM'));

		public selectedRangeIndex: KnockoutObservable<number> = ko.observable(1);

		public dateRanges: KnockoutObservableArray<DateOption> = ko.observableArray([]);

		public mode: KnockoutObservable<ACHIEVEMENT> = ko.observable(ACHIEVEMENT.NO);

		constructor(private params: Params) {
			super();

			const vm = this;
			const baseD = moment();
			const begin = baseD.clone().startOf('month').toDate();
			const finish = baseD.clone().endOf('month').toDate();

			if (!params) {
				vm.params = {
					tabIndex: "1",
					dateRange: ko.observable({ begin, finish }),
					mode: ko.observable(1),
					workplaceId: ko.observable('')
				};
			}

			const { mode, dateRange, workplaceId } = params;

			if (mode === undefined) {
				vm.params.mode = ko.observable(ACHIEVEMENT.NO);
			}

			if (dateRange === undefined) {
				vm.params.dateRange = ko.observable({ begin, finish });
			}

			if (workplaceId === undefined) {
				vm.params.workplaceId = ko.observable('');
			}
		}

		created() {
			const vm = this;
			const cache: CacheData = {
				yearMonth: null,
				dateRange: null,
				mode: 0
			};
			const proccesPeriod = (response: Period) => {
				const MD_FORMAT = 'MM/DD';
				const YMD_FORMAT = 'YYYY/MM/DD';

				if (response) {
					const { yearMonth, periodsClose } = response;

					if (vm.yearMonth() !== `${yearMonth}`) {
						vm.yearMonth(`${yearMonth}`);
					} else {
						vm.yearMonth.valueHasMutated();
					}

					$.Deferred()
						.resolve(true)
						// clear old data sources of date range
						.then(() => vm.dateRanges([]))
						// load new data
						.then(() => {
							if (periodsClose && periodsClose.length) {
								vm.dateRanges(periodsClose
									.map((m, id) => {
										const mb = moment.utc(m.startDate, YMD_FORMAT);
										const me = moment.utc(m.endDate, YMD_FORMAT);

										return {
											id: id + 1,
											title: `${m.closureName}${vm.$i18n('KSU002_7')}${mb.format(MD_FORMAT)}${vm.$i18n('KSU002_5')}${me.format(MD_FORMAT)}`,
											begin: mb.toDate(),
											finish: me.toDate(),
											wpId: m.workplaceId
										};
									}));
							}
						});
				}
			};

			// first load
			vm.$ajax('at', API.BASE_DATE).then(proccesPeriod);

			vm.yearMonth
				.subscribe((ym: string) => {
					const cmd = { yearMonth: Number(ym) };

					// first load
					if (cache.yearMonth === null) {
						cache.yearMonth = cmd.yearMonth;
						vm.$ajax('at', API.BASE_DATE, cmd).then(proccesPeriod);
					} else if (cache.yearMonth !== cmd.yearMonth) {
						vm.$dialog
							.confirm({ messageId: 'Msg_1732' })
							.then((v) => {
								if (v === 'yes') {
									cache.yearMonth = cmd.yearMonth;
									vm.$ajax('at', API.BASE_DATE, cmd).then(proccesPeriod);
								} else {
									// rollback data
									vm.yearMonth(`${cache.yearMonth}`);
								}
							});
					}
				});

			vm.selectedRangeIndex
				.subscribe(c => {
					if ([null, undefined].indexOf(c) > -1) {
						cache.dateRange = null;
					} else {
						const dateRanges = ko.unwrap(vm.dateRanges);

						const exist = _.find(dateRanges, (f) => f.id === c);

						if (exist) {
							if (cache.dateRange === null) {
								cache.dateRange = c;

								const { finish, begin, wpId } = exist;

								vm.params.workplaceId(wpId);
								vm.params.dateRange({ finish, begin });
							} else if (cache.dateRange !== c) {
								vm.$dialog
									.confirm({ messageId: 'Msg_1732' })
									.then((v) => {
										if (v === 'yes') {
											cache.dateRange = c;

											const { finish, begin, wpId } = exist;
			
											vm.params.workplaceId(wpId);
											vm.params.dateRange({ finish, begin });
										} else {
											// rollback data
											vm.selectedRangeIndex(cache.dateRange);
										}
									});
							}
						}
					}
				});

			vm.mode
				.subscribe(c => {
					if (cache.mode === null) {
						cache.mode = c;
						vm.params.mode(c);
					} else if (cache.mode !== c) {
						vm.$dialog
							.confirm({ messageId: 'Msg_1732' })
							.then((v) => {
								if (v === 'yes') {
									cache.mode = c;
									vm.params.mode(c);
								} else {
									// rollback data
									vm.mode(cache.mode);
								}
							});
					}
				});
		}
	}

	interface Params {
		tabIndex: string;
		dateRange: KnockoutObservable<c.DateRange | null>;
		mode: KnockoutObservable<ACHIEVEMENT>;
		workplaceId: KnockoutObservable<string>;
	}

	interface DateOption extends c.DateRange {
		id: number;
		title: string;
		wpId: string;
	}

	interface Closure {
		closureName: string;
		endDate: string;
		startDate: string;
		workplaceId: string;
	}

	interface Period {
		periodsClose: Closure[];
		yearMonth: number;
		employeeInfo: EmployeeInfo;
	}

	interface EmployeeInfo {
		employeeCd: string;
		employeeName: string;
	}

	export enum ACHIEVEMENT {
		YES = 1,
		NO = 0
	}

	/**
	 * Cache data for rollback
	 * after choose no of comfirm message: Msg_1732
	 */
	interface CacheData {
		yearMonth: number | null;
		dateRange: number | null;
		mode: number;
	}
}
