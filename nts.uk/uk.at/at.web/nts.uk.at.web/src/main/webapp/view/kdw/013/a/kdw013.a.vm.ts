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

    const { formatTime, setTimeOfDate, getTimeOfDate, getTask, getBackground, getTitles } = share;
    const { randomId } = nts.uk.util;

    const DATE_FORMAT = 'YYYY-MM-DD';
    const DATE_TIME_FORMAT = 'YYYY-MM-DDTHH:mm:00.000\\Z';

    const API: API = {
        ADD: '/screen/at/kdw013/a/add_confirm',
        // DeleteWorkResultConfirmationCommand
        DELETE: '/screen/at/kdw013/a/delete_confirm',
        // start page query params
        START: '/screen/at/kdw013/a/start',
        // 対象社員を選択する
        // 日付を変更する
        // Chọn nhân viên ở A2_4
        // Chọn ngày ở [画面イメージ]A6_1/[固有部品]A1_1
        CHANGE_DATE: '/screen/at/kdw013/a/changeDate',
        // RegisterWorkContentCommand
        REGISTER: '/screen/at/kdw013/a/register_work_content'
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

        breakTime= ko.observableArray([]);

        businessHours= ko.observableArray([]);

        weekends: KnockoutObservable<boolean> = ko.observable(true);
        editable: KnockoutObservable<boolean> = ko.observable(true);
        firstDay: KnockoutObservable<number> = ko.observable(1);
        scrollTime: KnockoutObservable<number> = ko.observable(420);
        slotDuration: KnockoutObservable<number> = ko.observable(30);
        initialDate: KnockoutObservable<Date> = ko.observable(new Date());
        isShowBreakTime: KnockoutObservable<boolean> = ko.observable(false);
        dateRange: KnockoutObservable<Partial<calendar.DatesSet>> = ko.observable({});
        initialView: KnockoutObservable<string> = ko.observable('oneDay');
        availableView: KnockoutObservableArray<calendar.InitialView> = ko.observableArray(['oneDay', 'fullWeek']);
        validRange: KnockoutObservable<Partial<calendar.DatesSet>> = ko.observable({end: '9999-12-32'});

        employee: KnockoutObservable<string> = ko.observable('');

        confirmers!: KnockoutComputed<calendar.Employee[]>;
        // need map with [KDW013_21, KDW013_22, KDW013_23, KDW013_24] resource
        attendanceTimes!: KnockoutComputed<calendar.AttendanceTime[]>;

        // Change date range data model
        $datas: KnockoutObservable<ChangeDateDto | null> = ko.observable(null);

        // settings (first load data)
        $settings: KnockoutObservable<StartProcessDto | null> = ko.observable(null);
    
        dataChanged: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            super();

            const vm = this;
            let $query = vm.getQuery();
            const { employee } = vm;
            const { mode } = $query;
            const cache: ChangeDateParam & { pair: -1 | 0 | 1 | 2 } = { ...initialCache(), pair: 0 };
            const sameCache = (params: ChangeDateParam): -1 | 0 | 1 | 2 => {
                if (cache.refDate !== params.refDate) {
                    if (cache.displayPeriod.end === params.displayPeriod.end) {
                        if (cache.displayPeriod.start === params.displayPeriod.start) {
                            return -1;
                        }
                    }
                }

                if (cache.employeeId !== params.employeeId) {
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
            const computedEvents = (data: SelectTargetEmployeeDto | null, settings: StartProcessDto | null) => {
                if (cache.pair === -1) {
                    return;
                }

                if (data && settings) {
                    const { lstWorkRecordDetailDto } = data;
                    const { tasks } = settings;

                    if (lstWorkRecordDetailDto && tasks) {
                        const events = _
                            .chain(lstWorkRecordDetailDto)
                            .map(({ date, employeeId, lstWorkDetailsParamDto }) => {
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

                                            const { end, start, workingHours } = timeZone;
                                            const {
                                                workCD1,
                                                workCD2,
                                                workCD3,
                                                workCD4,
                                                workCD5
                                            } = workGroup;
                                            const task = getTask(workGroup, tasks) || { displayInfo: {} } as any as c.TaskDto;
                                            
                                            const wg = {
                                                workCD1,
                                                workCD2,
                                                workCD3,
                                                workCD4,
                                                workCD5
                                            }

                                            const { timeWithDay: startTime } = start;
                                            const { timeWithDay: endTime } = end;
                                            
                                            return {
                                                start: setTimeOfDate($date, startTime),
                                                end: setTimeOfDate($date, endTime || (startTime + 60)),
                                                title: getTitles(wg, tasks),
                                                backgroundColor : getBackground(wg, tasks),
                                                textColor: '',
                                                extendedProps: {
                                                    id: randomId(),
                                                    status: 'normal' as any,
                                                    remarks,
                                                    employeeId,
                                                    supportFrameNo,
                                                    workCD1,
                                                    workCD2,
                                                    workCD3,
                                                    workCD4,
                                                    workCD5,
                                                    workLocationCD,
                                                    workingHours,
                                                    isChanged: false
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
            };

            vm.$datas
                .subscribe((datas) => computedEvents(datas, ko.unwrap(vm.$settings)));
    
            vm.events.subscribe((datas) => vm.dataChanged(true));

            vm.$settings
                .subscribe((settings) => computedEvents(ko.unwrap(vm.$datas), settings));

            vm.$toggle = {
                save: ko.computed({
                    read: () => {
                        const $settings = ko.unwrap(vm.$settings);
                        
                        if (!vm.dataChanged()) {
                            return false;
                        }

                        if (!$settings) {
                            return true;
                        }

                        const { startManHourInputResultDto } = $settings;

                        if (!startManHourInputResultDto) {
                            return true;
                        }

                        const { taskFrameUsageSetting } = startManHourInputResultDto;

                        if (!taskFrameUsageSetting) {

                            return true;
                        }
                        

//                        const { frameSettingList } = taskFrameUsageSetting;

//                        if (frameSettingList && frameSettingList.length) {
//                            return !!_.find(frameSettingList, ({ useAtr, frameNo }) => frameNo === 2 && useAtr === 1);
//                        }

                        return true;
                    }
                }),
                remove: ko.computed({
                    read: () => {
                        
                        let confirms = _.get(vm.$datas(),'lstComfirmerDto');
                        const editable = ko.unwrap(vm.editable);
                        let confimer = _.find(confirms, ['confirmSID',vm.$user.employeeId]);
                        return !editable && !!confimer;
                    }
                }),
                confirm: ko.computed({
                    read: () => {
                        let confirms = _.get(vm.$datas(),'lstComfirmerDto');
                        if (!_.isEmpty(confirms) && confirms.length >= 5) {
                            return false;
                        }
                        if (_.find(confirms, { confirmSID: vm.$user.employeeId })) {
                            return false;
                        }
                        const editable = ko.unwrap(vm.editable);
                        let confimer = _.find(_.get(vm.$datas(),'lstComfirmerDto'), ['confirmSID',vm.$user.employeeId]);
                        return !editable && !confimer;
                    }
                }),
            };

            if (mode) {
                // URLの値元に画面モードを判定する
                vm.editable(mode === '0');
            }

            ko.computed({
                read: () => {
                    const employeeId = ko.unwrap(vm.editable) === false ? ko.unwrap(vm.employee) : vm.$user.employeeId;
                    const date = ko.unwrap(vm.initialDate);
                    const dateRange = ko.unwrap(vm.dateRange);
                    const { start, end } = dateRange;
                    const setting = ko.unwrap(vm.$settings);

                    if (!employeeId || !setting) {
                        return;
                    }
                    
                    let itemIds = _.map(_.get(setting, 'manHrInputDisplayFormat.displayManHrRecordItems', []), item => { return item.attendanceItemId });

                    if (!!start && !!end && moment(date).isBetween(start, end)) {
                        const params: ChangeDateParam = {
                            employeeId,
                            refDate: moment(date).startOf('day').format(DATE_TIME_FORMAT),
                            displayPeriod: {
                                start: moment(start).startOf('day').format(DATE_TIME_FORMAT),
                                end: moment(end).subtract(1, 'day').startOf('day').format(DATE_TIME_FORMAT)
                            },
                            itemIds
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
                                .then((data: ChangeDateDto) => {
                                    vm.$datas(data);
                                    vm.dataChanged(false);
                                })
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
    
            vm.breakTime = ko.computed({
                read: () => {
                    const datas = ko.unwrap(vm.$datas);

                    if (datas) {

                        const { lstWorkRecordDetailDto } = datas;

                        return _
                            .chain(lstWorkRecordDetailDto)
                            .filter(({actualContent}) => { return !!actualContent.breakTimeSheets.length })
                            .map(({actualContent, date}) => {
                                const {breakTimeSheets} = actualContent;
                                return {
                                    dayOfWeek: vm.getDOW(date),
                                    breakTimes: _.map(breakTimeSheets, ({start, end}) => { return { start, end }; })
                                };
                            }).value();

                    }

                    return [];
                }
            });
    
            vm.businessHours = ko.computed({
                read: () => {
                    const datas = ko.unwrap(vm.$datas);

                    if (datas) {

                        const { lstWorkRecordDetailDto } = datas;

                        return _
                            .chain(lstWorkRecordDetailDto)
                            .filter(({actualContent}) => { return !!actualContent.start.timeWithDay || !!actualContent.end.timeWithDay })
                            .map(({actualContent, date}) => {
                                const {start, end} = actualContent;
                                return {
                                    dayOfWeek: vm.getDOW(date),
                                    start: start.timeWithDay,
                                    end: end.timeWithDay
                                };
                            }).value();

                    }

                    return [];
                }
            });

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
                            .filter(({ employeeId }) => employeeId === employeeId)
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
                .fail(function(error) {
                    vm.$dialog.error({ messageId: error.messageId });
                })
                .then((response: StartProcessDto) => {

                    vm.$window
                        .storage('KDW013_SETTING')
                        .then((value: any) => {
                            if (value) {
                                vm.initialView(value.initialView || 'oneDay');
                                vm.firstDay(value.firstDay !== undefined ? value.firstDay : 1);
                                vm.scrollTime(value.scrollTime || 420);
                                vm.slotDuration(value.slotDuration || 30);
                            }
                        });


                    vm.$settings(response);
                })
                .always(() => vm.$blockui('clear'));
        }

        getDOW(date){
            const vm = this;
            const dateRange = ko.unwrap(vm.dateRange);
            if (dateRange) {
                const start = moment(dateRange.start);
                const end = moment(dateRange.end);
                let range = end.diff(start, 'days');
                let dates = [] ;
                for (let i = 0; i <= range; i++) {
                    dates.push(start.clone().add(i, 'days').format('YYYY/MM/DD'));
                }
                return _.indexOf(dates, date) + 1;
            }
           
                return 0;
        }

        mounted() {
            const vm = this;

            _.extend(window, { vm });
        }

        getQuery(){
            let query = location.search.substring(1);
            if (!query || !query.match(/=/)) {
                return {};
            }
            return JSON.parse('{"' + decodeURI(query).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"') + '"}');
    
        }

        equipmentInput(){
            console.log('equipmentInput click');
        }

        saveData() {
            const vm = this;
            const { events, dateRange } = vm;
            const { HAND_CORRECTION_MYSELF, HAND_CORRECTION_OTHER } = EditStateSetting;
            const { start, end } = ko.unwrap(dateRange);

            if (!start || !end) {
                return;
            }

            const $events = ko.unwrap(events);
            const dateRanges = () => {
                const dates: Date[] = [];
                const begin = moment(start);

                while (begin.isBefore(end, 'day')) {
                    dates.push(begin.toDate());

                    begin.add(1, 'day');
                }

                return dates;
            };
    
            let sid = vm.employee() ? vm.employee() : vm.$user.employeeId;
    
            let editStateSetting = !vm.employee() ? HAND_CORRECTION_MYSELF : vm.employee() == vm.$user.employeeId ? HAND_CORRECTION_MYSELF : HAND_CORRECTION_OTHER;
    
            let mode =  vm.editable() ? 0 : vm.employee() === vm.$user.employeeId ? 0 : 1;

            let changedDates = dateRanges().map(date => {
                const events = _.filter($events, (e) => { return moment(e.start).isSame(date, 'day') });
                const data = _.find(vm.$datas().lstWorkRecordDetailDto, (e) => { return moment(e.date).isSame(date, 'day') });

                if (events.length != _.size(_.get(data, 'lstWorkDetailsParamDto'))) {
                    return { date: date, changed: true };
                }


                const isChanged = _.find(events, (e) => { return _.get(e, 'extendedProps.isChanged') });

                if (isChanged) {
                    return { date: date, changed: true };
                }

                return { date: date, changed: false };


            });

            
            const command: RegisterWorkContentCommand = {
                changedDates: _.chain(changedDates).filter(d => { return d.changed }).map(d => moment(d.date).format(DATE_TIME_FORMAT)).value(),
                editStateSetting,
                employeeId: sid,
                mode,
                workDetails: dateRanges().map((date) => {
                    const lstWorkDetailsParamCommand = _
                        .chain($events)
                        .filter(({ start }) => moment(start).isSame(date, 'day'))
                        .map(({ start, end, extendedProps }) => {
                            const {
                                workCD1,
                                workCD2,
                                workCD3,
                                workCD4,
                                workCD5,
                                workLocationCD,
                                remarks,
                                supportFrameNo
                            } = extendedProps;
                           
                            return {
                                remarks,
                                supportFrameNo,
                                workGroup: {
                                    workCD1: !_.isEmpty(workCD1) ? workCD1 : undefined,
                                    workCD2: !_.isEmpty(workCD2) ? workCD2 : undefined,
                                    workCD3: !_.isEmpty(workCD3) ? workCD3 : undefined,
                                    workCD4: !_.isEmpty(workCD4) ? workCD4 : undefined,
                                    workCD5: !_.isEmpty(workCD5) ? workCD5 : undefined,
                                },
                                workLocationCD: workLocationCD == "" ? null :workLocationCD,
                                timeZone: {
                                    end: getTimeOfDate(end),
                                    start: getTimeOfDate(start)
                                }
                            };
                        })
                        .value();

                    return {
                        date: moment(date).format(DATE_TIME_FORMAT),
                        lstWorkDetailsParamCommand
                    };
                })
            };

            vm
                .$blockui('grayout')
                // 作業を登録する
                .then(() => vm.$ajax('at', API.REGISTER, command))
                .then((response: RegisterWorkContentDto) => {
                    vm.dataChanged(false);
                    if (response) {
    
                        const { lstErrorMessageInfo, lstOvertimeLeaveTime } = response;
                        if (!lstErrorMessageInfo || lstErrorMessageInfo.length === 0) {
                            return vm.$dialog
                                .info({ messageId: 'Msg_15' })
                                .then(() => lstOvertimeLeaveTime);
                        } else {

                            let errors = lstErrorMessageInfo.map(x => {
                                return {
                                    message: x.messageError,
                                    messageId: x.resourceID,
                                    supplements: {}
                                };
                            });

                            nts.uk.ui.dialog.bundledErrors({ errors });
                        }
                    }
                    
                   

                    return $
                        .Deferred()
                        .resolve()
                        .then(() => null);
                    
                    
                    
                })
                .fail((response: ErrorMessage) => {
                    const { messageId, parameterIds } = response;

                    return vm.$dialog
                        // Msg_2066, Msg_2080
                        .error({ messageId, messageParams: parameterIds })
                        .then(() => null);
                })
                .then((data: OvertimeLeaveTime[] | null) => {
                    if (data && data.length) {
                        vm.openDialogCaculationResult(data);
                    }
                })
                .always(() => vm.$blockui('clear'));
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
                    .$blockui('grayout')
                    // 作業実績を確認する
                    .then(() => vm.$ajax('at', API.ADD, command))
                    .then((lstComfirmerDto: ConfirmerDto[]) => {
                        const _datas = ko.unwrap($datas);

                        if (_datas) {
                            _datas.lstComfirmerDto = lstComfirmerDto;

                            // update confirmers
                            $datas.valueHasMutated();
                            vm.dataChanged(false);
                        } else {
                            $datas({ lstComfirmerDto, lstWorkRecordDetailDto: [], workCorrectionStartDate: '', workGroupDtos: [] });
                        }
                    })
                    //.then(() => vm.editable.valueHasMutated())
                    .always(() => vm.$blockui('clear'));
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
                    .$blockui('grayout')
                    // 作業実績の確認を解除する
                    .then(() => vm.$ajax('at', API.DELETE, command))
                    .then((lstComfirmerDto: ConfirmerDto[]) => {
                        const _datas = ko.unwrap($datas);

                        if (_datas) {
                            _datas.lstComfirmerDto = lstComfirmerDto;

                            // update confirmers
                            $datas.valueHasMutated();
                            vm.dataChanged(false);
                        } else {
                            $datas({ lstComfirmerDto, lstWorkRecordDetailDto: [], workCorrectionStartDate: '', workGroupDtos: [] });
                        }
                    })
                    // trigger reload event on child component
                    //.then(() => vm.editable.valueHasMutated())
                    .always(() => vm.$blockui('clear'));
            }
        }

        private openDialogCaculationResult(data: OvertimeLeaveTime[]) {
            const vm = this;

            vm.$window
                .modal('at', '/view/kdw/013/d/index.xhtml', data)
                .then(() => { });
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
            template: `
            <div data-bind="ntsAccordion: { active: 0}">
                <h3>
                    <label data-bind="i18n: 'KDW013_4'"></label>
                </h3>

                <div class='fc-employees'>
                        <div data-bind="ntsComboBox: {
                        name: $component.$i18n('KDW013_5'),
                        options: $component.departments,
                        visibleItemsCount: 14,
                        value: $component.department,
                        editable: true,
                        selectFirstIfNull: true,
                        optionsValue: 'workplaceId',
                        optionsText: 'wkpDisplayName',
                        columns: [
                            { prop: 'workplaceCode', length: 4 },
                            { prop: 'wkpDisplayName', length: 10 }
                        ]
                    }"></div>
                <ul class="list-employee" data-bind="foreach: { data: $component.employees, as: 'item' }">
                    <li class="item" data-bind="
                        click: function() { $component.selectEmployee(item.employeeId) },
                        timeClick: -1,
                        css: {
                            'selected': ko.computed(function() { return item.employeeId === ko.unwrap($component.params.employee); })
                        }">
                        <div data-bind="text: item.employeeCode"></div>
                        <div data-bind="text: item.employeeName"></div>
                    </li>
                </ul>
                </div>
            </div> 
            `
        })
        export class EmployeeDepartmentComponent extends ko.ViewModel {
            department: KnockoutObservable<string> = ko.observable('');

            employees!: KnockoutComputed<EmployeeBasicInfoDto[]>;
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
                                let emps = _.filter(employeeInfos,{'workplaceId': $dept });
                                // updating
                                return loaded ? [] : _.filter(lstEmployeeInfo, (o) => {
                                   return !!_.find(emps, { 'employeeId': o.employeeId });
                                });
                            }
                        }

                        return [];
                    },
                    write: (value: any) => {

                    }
                });
    
    
                vm.department.subscribe((value) => {
                    const emps = ko.unwrap(vm.employees);
                    if (vm.employees().length) {
                        
                        const [first] = emps;
                        vm.params.employee(first.employeeId);
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
    
                vm.departments
                    .subscribe((deps) => {
                        if (!_.isEmpty(deps)) {

                            let empInfo = _.find(vm.params.$settings().refWorkplaceAndEmployeeDto.employeeInfos, { 'employeeId': vm.$user.employeeId });

                            if (empInfo) {

                                let selectedWkp = _.find(deps, { 'workplaceId': empInfo.workplaceId });

                                if (selectedWkp) {

                                    vm.department(selectedWkp.workplaceId);
                                }
                            }

                        }
                    });

                vm.employees
                    .subscribe((emps: EmployeeBasicInfoDto[]) => {
                        if (emps.length && !ko.unwrap(vm.params.employee)) {

                            let emp = _.find(emps, { 'employeeId': vm.$user.employeeId });
                            if (emp) {
                                vm.params.employee(emp.employeeId);
                            } else {
                                const [first] = emps;
                                vm.params.employee(first.employeeId);
                            }

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

                //department.valueHasMutated();
            }
        }
    }

    export module datePicker {
        type Kdw013DatePickerParams = {
            initialDate: Date | KnockoutObservable<Date>;
            firstDay: KnockoutObservable<number>;
        };

        @component({
            name: 'kdw013-date-picker',
            template: `<div id='fc-date-picker'></div>
            <style rel="stylesheet">
                .fc-date-picker{
                    height:240px;
                    padding: 0 !important;
                }
                .fc-date-picker .nts-input {
                    display: none;
                }
            </style>
        `
        })
        export class Kdw013DatePickerComponent extends ko.ViewModel {
            constructor(public params: Kdw013DatePickerParams) {
                $('#fc-date-picker').fcdatepicker({
                language: 'ja-JP',
                format: "YYYY/MM/DD",
                date: params.initialDate(),
                autoShow: true,
                weekStart: params.firstDay(),
                inline: true
            });
                params.firstDay.subscribe(value => {
                    if (value == undefined) {
                        return;
                    }
                    $('#fc-date-picker').fcdatepicker('destroy');
                    $('#fc-date-picker').fcdatepicker({
                                    language: 'ja-JP',
                                    format: "YYYY/MM/DD",
                                    date: params.initialDate(),
                                    autoShow: true,
                                    weekStart: value,
                                    inline: true
                                });
                });
                ko.utils.registerEventHandler('#fc-date-picker', "change", function(event) {
                    if(!moment(vm.initialDate()).isSame(moment($('#fc-date-picker').fcdatepicker('getDate'))))
                   vm.initialDate($('#fc-date-picker').fcdatepicker('getDate'));
                });
                params.initialDate.subscribe((value) => {
                    $('#fc-date-picker').fcdatepicker('setDate', value);
                });
                super();
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
            template: `
             <div data-bind="ntsAccordion: { active: 0}">
                <h3>
                    <label data-bind="i18n: 'KDW013_6'"></label>
                </h3>
                <div class='fc-employees'>
                    <ul data-bind="foreach: { data: $component.params.confirmers, as: 'item' }">
                        <li class="item">
                            <div data-bind="text: item.code"></div>
                            <div data-bind="text: item.name"></div>
                        </li>
                   </ul>
                </div>
            </div>
           `
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
            template: `
            <div class='edit-popup'>
                    <ul>
                        <li data-bind="i18n: 'KDW013_77' ,click:$component.openFdialog"></li>
                        <li data-bind="i18n: 'KDW013_78' ,click:$component.removeFav"></li>
                    </ul>
            </div>
            <div data-bind="ntsAccordion: { active: 0}">
                <h3>
                    <label data-bind="i18n: 'KDW013_76'"></label>
                </h3>
                <div class='fc-oneday-events'>
                    <ul data-bind="foreach: { data: $component.params.items, as: 'item' }">
                        <li class="title" data-bind="attr: {
                            'data-id': _.get(item.extendedProps, 'relateId', ''),
                            'data-color': item.backgroundColor
                        }">
                            <div data-bind="style: {
                                'background-color': item.backgroundColor
                            }"></div>
                            <div style="display: flex;">
                                <label  class='limited-label' style='width:90%;cursor: pointer;'  data-bind='text: item.title'>
                                </label>
                                <i data-bind="ntsIcon: { no: 2, width: 20, height: 25  },click: function(item,evn) { $component.editFav(evn,_.get(item.extendedProps, 'favId', '')) }">
                                </i>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <style rel="stylesheet">
                .fc-container .fc-oneday-events .edit-popup{
                    visibility: hidden;
                    position: fixed;
                    z-index: 99;
                    box-shadow: 1px 1px 5px 2px #888;
                    background-color: #fff;
                    padding: 0 10px;
                }
                .fc-container .fc-oneday-events .edit-popup ul li{
                    cursor: pointer;
                    padding: 5px 5px;
                }
                .fc-container .fc-oneday-events .edit-popup ul li:hover{
                    background: #CDE2CD;
                    color: #0086EA ;
                }
                .fc-container .fc-oneday-events .show {
                    visibility: visible;
                }
            </style>
           `
        })
        export class Kdw013EventComponent extends ko.ViewModel {
            constructor(public params: EventParams) {
                super();
            }
            
            editFav(e,id) {
                let editPopup = $('.fc-oneday-events .edit-popup');
                let pst = e.target;
                if(!pst){
                   editPopup.removeClass('show'); 
                }
                const { top, left, height, width } = pst.getBoundingClientRect();
                editPopup.addClass('show');
                editPopup.css({ "top": top, "left": left+width });
                editPopup.data('favId', id);
            }
            
            removeFav(data) {
                const vm = this;
                let id = $('.fc-oneday-events .edit-popup').data('favId');

                let newArrays = _.filter(vm.params.items(), item => {
                    return _.get(item, 'extendedProps.favId') != id;
                });
            
                vm.params.items(newArrays);
                $('.fc-oneday-events .edit-popup').removeClass('show');
            }
            
            openFdialog(data) {
                const vm = this;
                $('.fc-oneday-events .edit-popup').removeClass('show');
                let id = $('.fc-oneday-events .edit-popup').data('favId');
                vm.$window.shared('KDW013_A_TO_F_PARAMS', id);
                //gọi màn F
                console.log(id);
            }
        }

        type EventParams = {
            items: KnockoutObservableArray<any>;
            mode: KnockoutComputed<boolean>;
        };
    }
}