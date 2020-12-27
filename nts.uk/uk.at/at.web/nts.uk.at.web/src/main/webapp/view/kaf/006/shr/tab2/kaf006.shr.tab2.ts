module nts.uk.at.view.kaf006.shr.tab2.viewmodel {

    @component({
        name: 'kaf006-shr-tab2',
        template: `
        <div id="kaf006tab2">
            <div class="table" style="margin-top: 5px;">
                <div class="cell col-1">
                    <div class="cell valign-center" data-bind="ntsFormLabel:{ required: false }, text: $i18n('KAF006_28')"></div>
                </div>
                <div class="cell">
                    <div class="table" style="padding-bottom: 5px;" data-bind="visible: $parent.condition19Over60">
                        <div class="cell col-1" data-bind="text: $i18n('Com_ExsessHoliday')"></div>
                        <input style="width: 50px;" class="cell" data-bind="ntsTimeEditor: {
                            name: $i18n('Com_ExsessHoliday'),
                            value: $parent.over60H,
                            constraint: 'TimeOffPrimitive',
                            mode: 'time'
                        }" />
                    </div>
                    <div class="table" style="padding-bottom: 5px;" data-bind="visible: $parent.condition19Substitute">
                        <div class="cell col-1" data-bind="text: $i18n('KAF006_30')"></div>
                        <input style="width: 50px;" class="cell" data-bind="ntsTimeEditor: {
                            name: $i18n('KAF006_30'),
                            value: $parent.timeOff,
                            constraint: 'TimeOffPrimitive',
                            mode: 'time'
                        }" />
                    </div>
                    <div class="table" style="padding-bottom: 5px;" data-bind="visible: $parent.condition19Annual">
                        <div class="cell col-1" data-bind="text: $i18n('KAF006_29')"></div>
                        <input style="width: 50px;" class="cell" data-bind="ntsTimeEditor: {
                            name: $i18n('KAF006_29'),
                            value: $parent.annualTime,
                            constraint: 'TimeOffPrimitive',
                            mode: 'time'
                        }" />
                    </div>
                    <div class="table" style="padding-bottom: 5px;" data-bind="$parent.condition19ChildNursing">
                        <div class="cell col-1" data-bind="text: $i18n('Com_ChildNurseHoliday')"></div>
                        <input style="width: 50px;" class="cell" data-bind="ntsTimeEditor: {
                            name: $i18n('Com_ChildNurseHoliday'),
                            value: $parent.childNursing,
                            constraint: 'TimeOffPrimitive',
                            mode: 'time'
                        }" />
                    </div>
                    <div class="table" style="padding-bottom: 5px;" data-bind="$parent.condition19Nursing">
                        <div class="cell col-1" data-bind="text: $i18n('Com_CareHoliday')"></div>
                        <input style="width: 50px;" class="cell" data-bind="ntsTimeEditor: {
                            name: $i18n('Com_CareHoliday'),
                            value: $parent.nursing,
                            constraint: 'TimeOffPrimitive',
                            mode: 'time'
                        }" />
                    </div>
                    <hr style="width: 250px; margin-inline-start: initial;"/>
                    <div class="table">
                        <div class="cell col-1" data-bind="text: $i18n('KAF006_31')"></div>
                        <div class="cell" data-bind="text: total" style="width: 85px; text-align: center;"></div>
                        <div class="cell" data-bind="text: $i18n('KAF006_32'), visible: $parent.isChangeWorkHour"></div>
                        <div class="cell" data-bind="text: $parent.timeRequired, visible: $parent.isChangeWorkHour"></div>
                    </div>
                </div>
            </div>
            <div data-bind="visible: $parent.condition11">
                <div style="margin-top: 10px;" data-bind="ntsCheckBox: {
                    checked: isChangeWorkHour,
                    text: $i18n('KAF006_18')
                }"></div>
                <hr style="width: 700px; margin-inline-start: initial;" />
                <div class="table">
                    <div class="cell col-1"></div>
                    <div class="cell">
                        <div style="padding-bottom: 5px;">
                            <div class="cell col-1">
                                <div class="valign-center cell" data-bind="ntsFormLabel:{ required: false }, text: $i18n('KAF006_19')"></div>
                            </div>
                            <div class="cell">
                                <button style="margin-right: 5px;" data-bind="text: $i18n('KAF006_20'), enable: $parent.isChangeWorkHour, click: openKDL003"></button>
                            </div>
                            <div class="cell" data-bind="text: $parent.selectedWorkTimeDisp"></div>
                        </div>
                        <div style="padding-bottom: 5px;">
                            <div class="cell col-1">
                                <div class="valign-center cell" data-bind="ntsFormLabel:{ required: false }, text: $i18n('KAF006_22')"></div>
                            </div>
                            <div class="cell">
                                <input style="width: 120px;" data-bind="ntsTimeWithDayEditor: {
                                    name: $i18n('KAF006_58'),
                                    value: $parent.startTime1,
                                    constraint: 'TimeWithDayAttr',
                                    options: {
                                        timeWithDay: true,
                                        width: '120'
                                    }
                                }, enable: $parent.condition30" />
                                <span data-bind="text: $i18n('KAF006_47')"></span>
                                <input style="width: 120px;" data-bind="ntsTimeWithDayEditor: {
                                    name: $i18n('KAF006_59'),
                                    value: $parent.endTime1,
                                    constraint: 'TimeWithDayAttr',
                                    options: {
                                        timeWithDay: true,
                                        width: '120'
                                    }
                                }, enable: $parent.condition30" />
                            </div>
                        </div>
                        <div style="padding-bottom: 5px;" data-bind="visible: $parent.condition12">
                            <div class="cell col-1">
                                <div class="valign-center cell" data-bind="ntsFormLabel:{ required: false }, text: $i18n('KAF006_23')"></div>
                            </div>
                            <div class="cell">
                                <input style="width: 120px;" data-bind="ntsTimeWithDayEditor: {
                                    name: $i18n('KAF006_60'),
                                    value: $parent.startTime2,
                                    constraint: 'TimeWithDayAttr',
                                    options: {
                                        timeWithDay: true,
                                        width: '120'
                                    }
                                }, enable: $parent.condition30" />
                                <span data-bind="text: $i18n('KAF006_47')"></span>
                                <input style="width: 120px;" data-bind="ntsTimeWithDayEditor: {
                                    name: $i18n('KAF006_61'),
                                    value: $parent.endTime2,
                                    constraint: 'TimeWithDayAttr',
                                    options: {
                                        timeWithDay: true,
                                        width: '120'
                                    }
                                }, enable: $parent.condition30" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
        </div>
        `
    })

    class Kaf006Tab2ViewModel extends ko.ViewModel {
        // workTypeLst: KnockoutObservableArray<any> = ko.observableArray([]);
        // selectedWorkTypeCD: KnockoutObservable<any>;
        isChangeWorkHour: KnockoutObservable<boolean> = ko.observable(false);

        // 60H超休
        over60H: KnockoutObservable<number> = ko.observable();
        // 時間代休
        timeOff: KnockoutObservable<number> = ko.observable();
        // 時間年休
        annualTime: KnockoutObservable<number> = ko.observable();
        // 子の看護
        childNursing: KnockoutObservable<number> = ko.observable();
        // 介護時間
        nursing: KnockoutObservable<number> = ko.observable();

        total: any = ko.observable('0:00');

        created(params: any) {
            const vm = this;

            if (params) {
                // vm.workTypeLst = params.workTypeLst;
                // vm.selectedWorkTypeCD = params.selectedWorkTypeCD;
                vm.isChangeWorkHour = params.isChangeWorkHour;
                vm.over60H = params.over60H;
                vm.timeOff = params.timeOff;
                vm.annualTime = params.annualTime;
                vm.childNursing = params.childNursing;
                vm.nursing = params.nursing;
            }

            // vm.total = ko.observable(nts.uk.time.format.byId("Time_Short_HM", (vm.over60H() ? vm.over60H() : 0) + (vm.timeOff() ? vm.timeOff() : 0) 
            //     + (vm.annualTime() ? vm.annualTime() : 0) + (vm.childNursing() ? vm.childNursing() : 0) + (vm.nursing() ? vm.nursing() : 0)));
            vm.total = ko.computed(() => {
                return nts.uk.time.format.byId("Time_Short_HM", (vm.over60H() ? vm.over60H() : 0) + (vm.timeOff() ? vm.timeOff() : 0) 
                    + (vm.annualTime() ? vm.annualTime() : 0) + (vm.childNursing() ? vm.childNursing() : 0) + (vm.nursing() ? vm.nursing() : 0));
            });
        }

        mounted() {

        }

        openKDL003() {
            ko.contextFor(this.$el).$parent.content.openKDL003();
        }
    }
}