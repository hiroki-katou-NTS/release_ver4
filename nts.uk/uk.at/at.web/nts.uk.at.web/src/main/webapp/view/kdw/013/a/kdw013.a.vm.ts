module nts.uk.ui.at.kdw013.a {
    /**
     * 日別勤怠の編集状態
     */
    enum EditStateSetting {
        /** 手修正（本人） */
        HAND_CORRECTION_MYSELF = 0,
        /** 手修正（他人） */
        HAND_CORRECTION_OTHER = 1,
        /** 申請反映 */
        REFLECT_APPLICATION = 2,
        /** 打刻反映 */
        IMPRINT = 3,
        /** 申告反映 */
        DECLARE_APPLICATION = 4

    };

    const { formatTime, setTimeOfDate } = share;
    const { randomId } = nts.uk.util;

    const DATE_FORMAT = 'YYYY-MM-DD';
    const DATE_TIME_FORMAT = 'YYYY-MM-DDTHH:mm:00.000\\Z';

    const API: API = {
        ADD: '/screen/at/kdw013/a/add',
        // DeleteWorkResultConfirmationCommand
        DELETE: '/screen/at/kdw013/a/delete_work_result_confirm',
        // 対象社員を選択する
        // Chọn nhân viên ở A2_4
        // same api: CHANGE_DATE
        SELECT: '/screen/at/kdw013/a/select',
        // 日付を変更する
        // Chọn ngày ở [画面イメージ]A6_1/[固有部品]A1_1
        CHANGE_DATE: '/screen/at/kdw013/a/changeDate',
        // RegisterWorkContentCommand
        REGISTER: '/screen/at/kdw013/a/register_work_content',
        REG_WORKTIME: '/screen/at/kdw013/register_work_man_hours',
        START: '/screen/at/kdw013/a/start'
    };

    const initialCache = (): ChangeDateParam => ({
        displayPeriod: {
            end: '',
            start: ''
        },
        employeeId: '',
        refDate: ''
    })

    @handler({
        bindingName: 'kdw-toggle',
        validatable: true,
        virtual: false
    })
    export class KDWToggleBindingHandler implements KnockoutBindingHandler {
        init(element: HTMLButtonElement, valueAccessor: () => KnockoutComputed<boolean>) {
            const toggler = valueAccessor();
            const visible = ko.computed({
                read: () => {
                    const visible = ko.unwrap(toggler);

                    return visible;
                },
                disposeWhenNodeIsRemoved: element
            });
            const disable = ko.computed({
                read: () => {
                    const enable = ko.unwrap(toggler);

                    return !enable;
                },
                disposeWhenNodeIsRemoved: element
            });

            ko.applyBindingsToNode(element, { visible, disable });

            element.removeAttribute('data-bind');
        }
    }

    @bean()
    export class ViewModel extends ko.ViewModel {
        $toggle!: {
            save: KnockoutComputed<boolean>;
            remove: KnockoutComputed<boolean>;
            confirm: KnockoutComputed<boolean>;
        };

        events: KnockoutObservableArray<calendar.EventApi> = ko.observableArray([]);

        breakTime: KnockoutObservable<calendar.BreakTime> = ko.observable();

        businessHours: KnockoutObservableArray<calendar.BussinessHour> = ko.observableArray([]);

        weekends: KnockoutObservable<boolean> = ko.observable(true);
        editable: KnockoutObservable<boolean> = ko.observable(true);
        firstDay: KnockoutObservable<number> = ko.observable(1);
        scrollTime: KnockoutObservable<number> = ko.observable(480);
        slotDuration: KnockoutObservable<number> = ko.observable(30);
        initialDate: KnockoutObservable<Date> = ko.observable(new Date());
        dateRange: KnockoutObservable<Partial<calendar.DatesSet>> = ko.observable({});
        initialView: KnockoutObservable<string> = ko.observable('fullWeek');
        availableView: KnockoutObservableArray<calendar.InitialView> = ko.observableArray(['oneDay', 'fullWeek']);
        validRange: KnockoutObservable<Partial<calendar.DatesSet>> = ko.observable({});

        employee: KnockoutObservable<string> = ko.observable('');

        confirmers!: KnockoutComputed<calendar.Employee[]>;
        // need map with [KDW013_21, KDW013_22, KDW013_23, KDW013_24] resource
        attendanceTimes!: KnockoutComputed<calendar.AttendanceTime[]>;

        // Change date range data model
        $datas: KnockoutObservable<ChangeDateDto | null> = ko.observable(null);

        // settings (first load data)
        $settings: KnockoutObservable<StartProcessDto | null> = ko.observable(null);

        constructor() {
            super();

            const vm = this;
            const { $query, employee } = vm;
            const { mode } = $query;
            const cache: ChangeDateParam & { pair: -1 | 0 | 1 | 2 } = { ...initialCache(), pair: 0 };
            const sameCache = (params: ChangeDateParam): -1 | 0 | 1 | 2 => {
                if (cache.employeeId !== params.employeeId) {
                    if (cache.displayPeriod.end === params.displayPeriod.end) {
                        if (cache.displayPeriod.start === params.displayPeriod.start) {
                            return -1;
                        }
                    }

                    if (params.displayPeriod.start && params.displayPeriod.end) {
                        return 0;
                    }
                }

                if (cache.displayPeriod.end === params.displayPeriod.end) {
                    if (cache.displayPeriod.start === params.displayPeriod.start) {
                        if (cache.refDate === params.refDate) {
                            return 2;
                        }

                        return 1;
                    }
                }

                return 0;
            };
            const updateCache = (params: ChangeDateParam) => {
                cache.displayPeriod.end = params.displayPeriod.end;
                cache.displayPeriod.start = params.displayPeriod.start;

                cache.refDate = params.refDate;
            };

            vm.$datas
                .subscribe((data: SelectTargetEmployeeDto) => {
                    if (cache.pair === -1) {
                        return;
                    }

                    if (data) {
                        const { lstWorkRecordDetailDto } = data;

                        if (lstWorkRecordDetailDto) {
                            const events = _
                                .chain(lstWorkRecordDetailDto)
                                .map(({ date, sid, lstWorkDetailsParamDto }) => {
                                    const events: calendar.EventRaw[] =
                                        _.chain(lstWorkDetailsParamDto)
                                            .map(({
                                                remarks,
                                                supportFrameNo,
                                                timeZone,
                                                workGroup,
                                                workLocationCD,
                                            }) => {
                                                const $date = moment(date, DATE_FORMAT).toDate();

                                                const { end, start } = timeZone;
                                                const {
                                                    workCD1,
                                                    workCD2,
                                                    workCD3,
                                                    workCD4,
                                                    workCD5
                                                } = workGroup;

                                                const { timeWithDay: startTime } = start;
                                                const { timeWithDay: endTime } = end;

                                                return {
                                                    start: setTimeOfDate($date, startTime),
                                                    end: setTimeOfDate($date, endTime || (startTime + 60)),
                                                    title: '',
                                                    backgroundColor: '',
                                                    textColor: '',
                                                    extendedProps: {
                                                        id: randomId(),
                                                        status: 'normal' as any,
                                                        remarks,
                                                        sId: sid,
                                                        workCD1,
                                                        workCD2,
                                                        workCD3,
                                                        workCD4,
                                                        workCD5,
                                                        workLocationCD
                                                    } as any
                                                };
                                            })
                                            .value();

                                    return events;
                                })
                                .flatten()
                                .value();

                            vm.events(events);

                            return;
                        }
                    }

                    vm.events([]);
                });

            vm.$toggle = {
                save: ko.computed({
                    read: () => {
                        return true;
                    }
                }),
                remove: ko.computed({
                    read: () => {
                        const editable = ko.unwrap(vm.editable);

                        return !editable;
                    }
                }),
                confirm: ko.computed({
                    read: () => {
                        const editable = ko.unwrap(vm.editable);

                        return !editable;
                    }
                }),
            };

            if (mode) {
                // URLの値元に画面モードを判定する
                vm.editable(mode === '0');
            }

            ko.computed({
                read: () => {
                    const employeeId = ko.unwrap(vm.employee) || vm.$user.employeeId;
                    const date = ko.unwrap(vm.initialDate);
                    const dateRange = ko.unwrap(vm.dateRange);
                    const { start, end } = dateRange;

                    if (!!start && !!end && moment(date).isBetween(start, end)) {
                        const params: ChangeDateParam = {
                            employeeId,
                            refDate: moment(date).startOf('day').format(DATE_TIME_FORMAT),
                            displayPeriod: {
                                start: moment(start).startOf('day').format(DATE_TIME_FORMAT),
                                end: moment(end).subtract(1, 'day').endOf('day').format(DATE_TIME_FORMAT)
                            }
                        };
                        cache.pair = sameCache(params);

                        if (cache.pair !== 2) {
                            updateCache(params);
                        }

                        if (cache.pair <= 0) {
                            // s.h.i.t
                            // vm.$datas(null);

                            vm
                                .$blockui('grayout')
                                .then(() => vm.$ajax('at', API.CHANGE_DATE, params))
                                .then((data: ChangeDateDto) => vm.$datas(data))
                                .always(() => vm.$blockui('clear'));
                        }
                    }
                }
            }).extend({ rateLimit: 250 });

            vm.confirmers = ko.computed({
                read: () => {
                    const datas = ko.unwrap(vm.$datas);
                    const $date = ko.unwrap(vm.initialDate);
                    const $moment = moment($date).format(DATE_FORMAT);

                    if (datas) {
                        const { lstComfirmerDto } = datas;

                        return _
                            .chain(lstComfirmerDto)
                            .filter(({ confirmDateTime }) => (confirmDateTime || "").indexOf($moment) === 0)
                            .map(({
                                confirmSID: id,
                                confirmSCD: code,
                                businessName: name
                            }) => ({
                                id,
                                code,
                                name,
                                selected: false
                            }))
                            .value();
                    }

                    return [] as calendar.Employee[];
                }
            }).extend({ rateLimit: 500 });

            vm.attendanceTimes = ko.computed({
                read: () => {
                    const datas = ko.unwrap(vm.$datas);
                    const employee = ko.unwrap(vm.employee);

                    // need update by employId if: mode=1
                    const employeeId = employee || vm.$user.employeeId;

                    if (datas) {
                        const { lstWorkRecordDetailDto } = datas;

                        return _
                            .chain(lstWorkRecordDetailDto)
                            // .orderBy(['date'])
                            .filter(({ sid }) => sid === employeeId)
                            .map(({
                                date: strDate,
                                actualContent,
                            }) => {
                                const events: string[] = [];
                                const date = moment(strDate, DATE_FORMAT).toDate();
                                const { breakHours, end, start, totalWorkingHours } = actualContent;

                                if (start) {
                                    const { timeWithDay } = start;

                                    if (_.isNumber(timeWithDay)) {
                                        events.push(vm.$i18n('KDW013_21', [formatTime(timeWithDay, 'Time_Short_HM')]));
                                    }
                                }

                                if (end) {
                                    const { timeWithDay } = end;

                                    if (_.isNumber(timeWithDay)) {
                                        events.push(vm.$i18n('KDW013_22', [formatTime(timeWithDay, 'Time_Short_HM')]));
                                    }
                                }

                                if (_.isNumber(breakHours)) {
                                    events.push(vm.$i18n('KDW013_23', [formatTime(breakHours, 'Time_Short_HM')]));
                                }

                                if (_.isNumber(totalWorkingHours)) {
                                    events.push(vm.$i18n('KDW013_24', [formatTime(totalWorkingHours, 'Time_Short_HM')]));
                                }

                                return { date, events, };
                            })
                            .value();
                    }

                    return [] as calendar.AttendanceTime[];
                }
            }).extend({ rateLimit: 500 });

            // get settings Msg_1960
            vm
                .$blockui('grayout')
                .then(() => vm.$ajax('at', API.START))
                .then((response: StartProcessDto) => {
                    // 作業利用設定チェック
                    if (!response) {
                        return vm.$dialog.error({ messageId: 'Msg_1960' });
                    }

                    const { startManHourInputResultDto } = response;

                    if (!startManHourInputResultDto) {
                        return vm.$dialog.error({ messageId: 'Msg_1960' });
                    }

                    const { taskFrameUsageSetting, tasks } = startManHourInputResultDto;

                    if (!taskFrameUsageSetting) {
                        return vm.$dialog.error({ messageId: 'Msg_1960' });
                    }

                    const { frameSettingList } = taskFrameUsageSetting;

                    if (!frameSettingList || frameSettingList.length === 0) {
                        return vm.$dialog.error({ messageId: 'Msg_1960' });
                    }

                    // 作業マスタチェック
                    if (!tasks || tasks.length === 0) {
                        return vm.$dialog.error({ messageId: 'Msg_1961' });
                    }

                    vm.$settings(response);
                })
                .always(() => vm.$blockui('clear'));
        }

        mounted() {
            const vm = this;

            _.extend(window, { vm });
        }

        saveData() {
            const vm = this;
            const { events } = vm;
            const { HAND_CORRECTION_MYSELF } = EditStateSetting;
            const command: RegisterWorkContentCommand = {
                editStateSetting: HAND_CORRECTION_MYSELF,
                employeeId: vm.$user.employeeId,
                mode: 0,
                workDetails: ko.unwrap(events).map(({ start, extendedProps }) => {
                    const { workCD1, workCD2, workCD3, workCD4, workCD5, workplace: workLocationCD, descriptions: remarks } = extendedProps;

                    return {
                        date: moment(start).toISOString(),
                        lstWorkDetailsParamCommand: [{
                            remarks,
                            supportFrameNo: 1,
                            workGroup: {
                                workCD1,
                                workCD2,
                                workCD3,
                                workCD4,
                                workCD5
                            },
                            workLocationCD
                        }]
                    };
                })
            };

            vm.$blockui('grayout')
                // 作業を登録する
                .then(() => vm.$ajax('at', API.REGISTER, command))
                .then((response: RegisterWorkContentDto) => {

                    if (response) {
                        const { lstErrorMessageInfo, lstOvertimeLeaveTime } = response;

                        if (!lstErrorMessageInfo || lstErrorMessageInfo.length === 0) {
                            return vm.$dialog
                                .info({ messageId: 'Msg_15' })
                                .then(() => lstOvertimeLeaveTime);
                        }

                        return vm.$dialog
                            // ,Msg_2066, Msg_2080
                            .error({ messageId: 'Msg_2081' })
                            .then(() => null);
                    }

                    return $.Deferred().resolve(null);
                })
                .then((data) => {
                    if (data && data.length) {
                        vm.openDialogCaculationResult(data);
                    }
                });
        }

        // 日付を変更する
        // UKDesign.UniversalK.就業.KDW_日別実績.KDW013_工数入力.A:工数入力.メニュー別OCD.日付を変更する
        datesSet(start: Date, end: Date) {
            const vm = this;

            vm.dateRange({ start, end });
        }

        // 作業実績を確認する
        confirm() {
            const vm = this;
            const { $user, $datas, employee, initialDate } = vm;
            const date = ko.unwrap(initialDate);
            const employeeId = ko.unwrap(employee);

            if (employeeId) {
                const command: AddWorkRecodConfirmationCommand = {
                    //対象者
                    // get from A2_5 control
                    employeeId,
                    //対象日
                    // get from initialDate
                    date: moment(date).toISOString(),
                    //確認者
                    // 作業詳細.作業グループ
                    confirmerId: $user.employeeId
                };

                vm
                    // 作業実績を確認する
                    .$ajax('at', API.ADD, command)
                    .then((lstComfirmerDto: ConfirmerDto[]) => {
                        const _datas = ko.unwrap($datas);

                        if (_datas) {
                            _datas.lstComfirmerDto = lstComfirmerDto;

                            // update confirmers
                            $datas.valueHasMutated();
                        } else {
                            $datas({ lstComfirmerDto, lstWorkRecordDetailDto: [], workCorrectionStartDate: '', workGroupDtos: [] });
                        }
                    })
                    .then(() => vm.editable.valueHasMutated());
            }
        }

        // 作業実績の確認を解除する
        removeConfirm() {
            const vm = this;
            const { $user, $datas, employee, initialDate } = vm;
            const date = ko.unwrap(initialDate);
            const employeeId = ko.unwrap(employee);

            if (employeeId) {
                const command = {
                    //対象者
                    // get from A2_5 control
                    employeeId,
                    //対象日
                    // get from initialDate
                    date: moment(date).toISOString(),
                    //確認者
                    // 作業詳細.作業グループ
                    confirmerId: $user.employeeId
                };

                vm
                    // 作業実績の確認を解除する
                    .$ajax('at', API.DELETE, command)
                    .then((lstComfirmerDto: ConfirmerDto[]) => {
                        const _datas = ko.unwrap($datas);

                        if (_datas) {
                            _datas.lstComfirmerDto = lstComfirmerDto;

                            // update confirmers
                            $datas.valueHasMutated();
                        } else {
                            $datas({ lstComfirmerDto, lstWorkRecordDetailDto: [], workCorrectionStartDate: '', workGroupDtos: [] });
                        }
                    })
                    // trigger reload event on child component
                    .then(() => vm.editable.valueHasMutated());
            }
        }

        private openDialogCaculationResult(data: OvertimeLeaveTime[]) {
            const vm = this;

            vm.$window
                .modal('at', '/view/kdw/013/d/index.xhtml', data).then(() => { });
        }
    }

    export module department {
        type EmployeeDepartmentParams = {
            mode: KnockoutObservable<boolean>;
            employee: KnockoutObservable<string>;
            $settings: KnockoutObservable<a.StartProcessDto | null>;
        };

        @component({
            name: 'kdw013-department',
            template: `<h3 data-bind="i18n: 'KDW013_4'"></h3>
            <div data-bind="ntsComboBox: {
                name: $component.$i18n('KDW013_5'),
                options: $component.departments,
                visibleItemsCount: 20,
                value: $component.department,
                editable: true,
                selectFirstIfNull: true,
                optionsValue: 'workplaceId',
                optionsText: 'wkpDisplayName',
                columns: [
                    { prop: 'workplaceId', length: 4 },
                    { prop: 'wkpDisplayName', length: 10 }
                ]
            }"></div>
            <ul class="list-employee" data-bind="foreach: { data: $component.employees, as: 'item' }">
                <li class="item" data-bind="
                    click: function() { $component.selectEmployee(item.sid) },
                    timeClick: -1,
                    css: {
                        'selected': ko.computed(function() { return item.sid === ko.unwrap($component.params.employee); })
                    }">
                    <div data-bind="text: item.employeeCode"></div>
                    <div data-bind="text: item.employeeName"></div>
                </li>
            </ul>`
        })
        export class EmployeeDepartmentComponent extends ko.ViewModel {
            department: KnockoutObservable<string> = ko.observable('');

            employees!: KnockoutComputed<EmployeeBasicInfoImport[]>;
            departments!: KnockoutComputed<WorkplaceInfoDto[]>;

            constructor(private params: EmployeeDepartmentParams) {
                super();

                const vm = this;
                const { $settings, mode } = params;

                vm.employees = ko.computed({
                    read: () => {
                        const loaded = ko.unwrap(mode);
                        const $sets = ko.unwrap($settings);
                        const $dept = ko.unwrap(vm.department);

                        if ($sets) {
                            const { refWorkplaceAndEmployeeDto } = $sets;

                            if (refWorkplaceAndEmployeeDto) {
                                const { employeeInfos, lstEmployeeInfo } = refWorkplaceAndEmployeeDto;

                                // updating
                                return loaded ? [] : lstEmployeeInfo.filter(({ sid }) => employeeInfos[sid] === $dept);
                            }
                        }

                        return [];
                    },
                    write: (value: any) => {

                    }
                });

                vm.departments = ko.computed({
                    read: () => {
                        const loaded = ko.unwrap(mode);
                        const $sets = ko.unwrap($settings);

                        if ($sets) {
                            const { refWorkplaceAndEmployeeDto } = $sets;

                            if (refWorkplaceAndEmployeeDto) {
                                const { workplaceInfos } = refWorkplaceAndEmployeeDto;

                                return loaded ? [] : workplaceInfos;
                            }
                        }

                        return [];
                    },
                    write: (value: any) => {

                    }
                });

                vm.employees
                    .subscribe((emps: EmployeeBasicInfoImport[]) => {
                        if (emps.length && !ko.unwrap(vm.params.employee)) {
                            const [first] = emps;

                            vm.params.employee(first.sid);
                        }
                    });
            }

            mounted() {
                const vm = this;
                const { $el } = vm;

                $($el)
                    .removeAttr('data-bind')
                    .find('[data-bind]')
                    .removeAttr('data-bind');
            }

            public selectEmployee(id: string) {
                const vm = this;
                const { department } = vm;
                const { employee } = vm.params;

                employee(id);

                department.valueHasMutated();
            }
        }
    }

    export module approved {
        type Kdw013ApprovedParams = {
            mode: KnockoutObservable<boolean>;
            confirmers: KnockoutObservableArray<calendar.Employee>;
            $settings: KnockoutObservable<a.StartProcessDto | null>;
        };

        @component({
            name: 'kdw013-approveds',
            template: `<h3 data-bind="i18n: 'KDW013_6'"></h3>
            <ul data-bind="foreach: { data: $component.params.confirmers, as: 'item' }">
                <li class="item">
                    <div data-bind="text: item.code"></div>
                    <div data-bind="text: item.name"></div>
                </li>
            </ul>`
        })
        export class Kdw013ApprovedComponent extends ko.ViewModel {
            constructor(public params: Kdw013ApprovedParams) {
                super();
            }
        }
    }

    export module event {
        @component({
            name: 'kdw013-events',
            template: `<h3 data-bind="i18n: 'KDW013_7'"></h3>
            <ul data-bind="foreach: { data: $component.params.items, as: 'item' }">
                <li class="title" data-bind="attr: {
                    'data-id': _.get(item.extendedProps, 'relateId', ''),
                    'data-color': item.backgroundColor
                }">
                    <div data-bind="style: {
                        'background-color': item.backgroundColor
                    }"></div>
                    <div data-bind="text: item.title"></div>
                </li>
            </ul>`
        })
        export class Kdw013EventComponent extends ko.ViewModel {
            constructor(public params: EventParams) {
                super();
            }
        }

        type EventParams = {
            items: KnockoutObservableArray<any>;
            mode: KnockoutComputed<boolean>;
        };
    }

    const initData = () => {
        const vm = new ko.ViewModel();
        const commands: AddAttendanceTimeZoneParam = {
            employeeId: vm.$user.employeeId,
            editStateSetting: 1,
            workDetails: [{
                date: moment().toISOString(),
                lstWorkDetailsParam: [{
                    remarks: '',
                    supportFrameNo: 1,
                    timeZone: {
                        end: {
                            reasonTimeChange: {
                                engravingMethod: 0,
                                timeChangeMeans: 0
                            },
                            timeWithDay: 0
                        },
                        start: {
                            reasonTimeChange: {
                                engravingMethod: 0,
                                timeChangeMeans: 0
                            },
                            timeWithDay: 0
                        },
                        workingHours: 1
                    },
                    workGroup: {
                        workCD1: '',
                        workCD2: '',
                        workCD3: '',
                        workCD4: '',
                        workCD5: ''
                    },
                    workLocationCD: ''
                }]
            }]
        };

        vm
            .$ajax('at', API.REG_WORKTIME, commands)
            .then(function () { });
    }

    _.extend(window, { initData });
}