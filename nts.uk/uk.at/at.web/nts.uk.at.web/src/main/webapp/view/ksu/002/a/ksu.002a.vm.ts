/// <reference path="../../../../lib/nittsu/viewcontext.d.ts" />

module nts.uk.ui.at.ksu002.a {
	import m = nts.uk.ui.memento;
	import c = nts.uk.ui.calendar;

	type DayData = c.DayData<ScheduleData>;
	type DayDataRawObsv = c.DayData<ObserverScheduleData>;
	type DayDataMementoObsv = c.DayData<ObserverScheduleData<WorkSchedule<Date>>>;

	const API = {
		UNAME: '/sys/portal/webmenu/username',
		GSCHE: '/screen/ksu/ksu002/displayInWorkInformation'
	};

	const memento: m.Options = {
		size: 20,
		// callback function raise when undo or redo
		replace: function (data: DayDataRawObsv[], replacer: DayData) {
			const exist = _.find(data, f => moment(f.date).isSame(replacer.date, 'date'));

			if (exist) {
				const { data } = exist;
				const { wtime, wtype, value } = replacer.data;

				data.wtime.code(wtime.code);
				data.wtime.name(wtime.name);

				data.wtype.code(wtype.code);
				data.wtype.name(wtype.name);

				data.value.begin(value.begin);
				data.value.finish(value.finish);
			}
		}
	};

	@bean()
	export class ViewModel extends ko.ViewModel {
		currentUser!: KnockoutComputed<string>;
		showC: KnockoutObservable<boolean> = ko.observable(true);

		mode: KnockoutObservable<EDIT_MODE> = ko.observable('copy');
		baseDate: KnockoutObservable<c.DateRange | null> = ko.observable(null);
		schedules: m.MementoObservableArray<DayDataRawObsv> = ko.observableArray([]).extend({ memento }) as any;

		workplaceId: KnockoutObservable<string> = ko.observable('');
		achievement: KnockoutObservable<ACHIEVEMENT> = ko.observable(ACHIEVEMENT.NO);
		workData: KnockoutObservable<null | WorkData> = ko.observable(null);

		created() {
			const vm = this;
			const bussinesName: KnockoutObservable<string> = ko.observable('');

			vm.currentUser = ko.computed({
				read: () => {
					const bName = ko.unwrap(bussinesName);

					return `${vm.$i18n('KSU002_1')}&nbsp;&nbsp;&nbsp;&nbsp;${vm.$user.employeeCode}&nbsp;&nbsp;${bName}`;
				},
				owner: vm
			});

			vm.$ajax('com', API.UNAME)
				.then((name: string) => bussinesName(name));

			// call to api and get data
			vm.baseDate
				.subscribe((d: c.DateRange) => {
					if (!d) {
						return;
					}

					const { begin, finish } = d;

					if (!begin || !finish) {
						return;
					}

					const command = {
						listSid: [vm.$user.employeeId],
						startDate: moment(begin).toISOString(),
						endDate: moment(finish).toISOString()
					};

					vm.$blockui('show')
						.then(() => vm.$ajax('at', API.GSCHE, command))
						.then((response: WorkSchedule<string>[]) => _.chain(response)
							.orderBy(['date'])
							.map(m => ({
								...m,
								date: moment(m.date, 'YYYY/MM/DD')
							}))
							.value()
						)
						.then((response: WorkSchedule[]) => {
							if (response && response.length) {
								const { NO } = ACHIEVEMENT;
								const arch = ko.unwrap(vm.achievement);
								const clones: DayDataMementoObsv[] = ko.unwrap(vm.schedules);

								_.each(response, ($raw) => {
									const exits = _.find(clones, c => $raw.date.isSame(c.date, 'date'));

									if (exits) {
										exits.data = {
											$raw: {
												...$raw,
												date: $raw.date.toDate()
											},
											wtype: {
												code: ko.observable($raw.workTypeCode),
												name: ko.observable($raw.workTypeName)
											},
											wtime: {
												code: ko.observable($raw.workTimeCode),
												name: ko.observable($raw.workTimeName)
											},
											value: {
												begin: ko.observable($raw.startTime),
												finish: ko.observable($raw.endTime),
												required: ko.observable(WORKTYPE_SETTING.NOT_REQUIRED)
											},
											holiday: ko.observable(null),
											event: ko.observable(null),
											comfirmed: ko.observable($raw.confirmed),
											achievement: ko.observable(arch === NO ? null : $raw.achievements)
										};

										const { dateInfoDuringThePeriod } = $raw;

										if (dateInfoDuringThePeriod) {
											const {
												holiday,
												specificDay,
												listSpecDayNameCompany,
												listSpecDayNameWorkplace,
												optCompanyEventName,
												optWorkplaceEventName
											} = dateInfoDuringThePeriod;

											// What is this???
											if (holiday) {
												exits.data.holiday(`${listSpecDayNameCompany.join('\n')}\n${listSpecDayNameWorkplace.join('\n')}`);
											}

											if (specificDay || optCompanyEventName || optWorkplaceEventName) {
												exits.data.event(`${optCompanyEventName || ''}\n${optWorkplaceEventName || ''}`);
											}
										}
									}
								});

								vm.schedules(clones);
								vm.schedules.reset();

								vm.$nextTick(() => {
									$(vm.$el).find('[data-bind]').removeAttr('data-bind');
								});
							}
						})
						.always(() => vm.$blockui('hide'));
				});

			// UI-4
			vm.achievement
				.subscribe((c) => {
					const { NO } = ACHIEVEMENT;
					const undo = vm.schedules.undoAble();
					const redo = vm.schedules.redoAble();

					const schedules: DayDataMementoObsv[] = ko.unwrap(vm.schedules);

					// soft reset (only undo, redo)
					vm.schedules.reset(!(undo || redo));

					// reset data
					_.each(schedules, (sc) => {
						const { data } = sc;
						const { $raw, wtype, wtime, value } = data;

						// UI-4-1 実績表示を「する」に選択する
						// UI-4-2 実績表示を「しない」に選択する
						if (!!$raw.achievements || c === NO) {
							wtype.code($raw.workTypeCode);
							wtype.name($raw.workTypeName);

							wtime.code($raw.workTimeCode);
							wtime.name($raw.workTimeName);

							value.begin($raw.startTime);
							value.finish($raw.endTime);
						}

						// state of achievement (both data & switch select)
						data.achievement(c === NO ? null : $raw.achievements);
					});
				});
		}

		mounted() {
			const vm = this;

			$(vm.$el).find('[data-bind]').removeAttr('data-bind');

			_.extend(window, { vm });
		}

		// UI-8: Undo-Redoの処理
		undoOrRedo(action: 'undo' | 'redo') {
			const vm = this;

			if (action === 'undo') {
				vm.schedules.undo();
			} else if (action === 'redo') {
				vm.schedules.redo();
			}
		}

		// edit data on copy mode
		clickDayCell(type: c.CLICK_CELL, dayData: DayDataRawObsv) {
			const vm = this;
			const mode = ko.unwrap(vm.mode);
			const { REQUIRED } = WORKTYPE_SETTING;
			const workData = ko.unwrap(vm.workData);
			const preview: DayData = ko.toJS(dayData);

			if (type === 'info' && mode === 'copy' && workData) {
				const { wtime, wtype } = workData;
				const wrap: DayDataRawObsv[] = ko.unwrap(vm.schedules);
				const current = _.find(wrap, f => moment(f.date).isSame(preview.date, 'date'));

				if (current && !current.data.achievement()) {
					/**
					 * Required & deferred & wtime exist ?
					 */

					// UI-5: 不正な勤務情報の貼り付けのチェック
					if (wtype.type === REQUIRED && wtime.code === 'none') {
						vm.$dialog.error({ messageId: 'Msg_1809' });
					} else {
						// UI-5: エラーがならない場合は、常に勤務情報を勤務予定セルに反映する。
						$.Deferred()
							.resolve(true)
							// change data
							.then(() => {
								const { data } = current;

								data.wtype.code(wtype.code);
								data.wtype.name(wtype.name);
								data.value.required(wtype.type);

								if (wtime.code === 'none') {
									data.wtime.code('');
									data.wtime.name('');

									data.value.begin(null);
									data.value.finish(null);
								} else if (wtime.code !== 'deferred') {
									data.wtime.code(wtime.code);
									data.wtime.name(wtime.name);

									data.value.begin(wtime.value.begin);
									data.value.finish(wtime.value.finish);
								}
							})
							// save after change data
							.then(() => vm.schedules.memento({ current, preview }));
					}
				}
			}
		}

		// edit data on edit mode
		changeDayCell(current: DayData) {
			const vm = this;
			const wrap: DayDataRawObsv[] = ko.unwrap(vm.schedules);
			const preview = _.find(wrap, f => moment(f.date).isSame(current.date, 'date'));

			if (preview) {
				$.Deferred()
					.resolve(true)
					// save to memento before change data
					.then(() => vm.schedules.memento({ current, preview }))
					.then(() => {
						const { data } = preview;
						const { wtime, wtype, value } = current.data;

						data.wtime.code(wtime.code);
						data.wtime.name(wtime.name);

						data.wtype.code(wtype.code);
						data.wtype.name(wtype.name);

						data.value.begin(value.begin);
						data.value.finish(value.finish);
					});
			}
		}
	}

	interface WorkSchedule<D = moment.Moment> {
		// 実績か
		achievements: boolean;
		active: boolean;
		// 確定済みか
		confirmed: boolean;
		// 年月日
		date: D;
		edit: boolean;
		// 社員ID
		employeeId: string;
		endTime: null | number;
		endTimeEditState: null | number;
		// データがあるか
		haveData: boolean;
		isActive: boolean;
		isEdit: boolean;
		needToWork: boolean;
		startTime: null | number;
		startTimeEditState: null | number;
		supportCategory: number;
		workHolidayCls: null | string;
		workTimeCode: string;
		workTimeEditStatus: null | string;
		workTimeName: string;
		workTypeCode: string;
		workTypeEditStatus: null | string;
		workTypeName: string;
		dateInfoDuringThePeriod: DateInfoDuringThePeriod;
	}

	interface DateInfoDuringThePeriod {
		// 祝日であるか
		holiday: boolean;
		// 会社の特定日名称リスト
		listSpecDayNameCompany: string[];
		// 職場の特定日名称リスト
		listSpecDayNameWorkplace: string[];
		// 会社行事名称
		optCompanyEventName: string | null;
		//  職場行事名称
		optWorkplaceEventName: string | null;
		// 特定日であるか
		specificDay: boolean;
	}
}