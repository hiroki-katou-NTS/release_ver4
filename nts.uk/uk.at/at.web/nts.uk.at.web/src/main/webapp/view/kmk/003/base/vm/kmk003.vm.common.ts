module nts.uk.at.view.kmk003.a {
    import CommonRestSettingDto = service.model.common.CommonRestSettingDto;
    import FlowRestSetDto = service.model.common.FlowRestSetDto;
    import FlowFixedRestSetDto = service.model.common.FlowFixedRestSetDto;
    import FlowWorkRestSettingDetailDto = service.model.common.FlowWorkRestSettingDetailDto;
    import FlowWorkRestSettingDto = service.model.common.FlowWorkRestSettingDto;
    import TimeRoundingSettingDto = service.model.common.TimeRoundingSettingDto;
    import TimeZoneRoundingDto = service.model.common.TimeZoneRoundingDto;
    import HDWorkTimeSheetSettingDto = service.model.common.HDWorkTimeSheetSettingDto;
    import DeductionTimeDto = service.model.common.DeductionTimeDto;
    import TimezoneOfFixedRestTimeSetDto = service.model.common.TimezoneOfFixedRestTimeSetDto;
    import FlowRestSettingDto = service.model.common.FlowRestSettingDto;
    import FlowRestTimezoneDto = service.model.common.FlowRestTimezoneDto;
    import IntervalTimeDto = service.model.common.IntervalTimeDto;
    import IntervalTimeSettingDto = service.model.common.IntervalTimeSettingDto;
    import DesignatedTimeDto = service.model.common.DesignatedTimeDto;
    import SubHolTransferSetDto = service.model.common.SubHolTransferSetDto;
    import WorkTimezoneOtherSubHolTimeSetDto = service.model.common.WorkTimezoneOtherSubHolTimeSetDto;
    import WorkTimezoneMedicalSetDto = service.model.common.WorkTimezoneMedicalSetDto;
    import TotalRoundingSetDto = service.model.common.TotalRoundingSetDto;
    import GoOutTimeRoundingSettingDto = service.model.common.GoOutTimeRoundingSettingDto;
    import DeductGoOutRoundingSetDto = service.model.common.DeductGoOutRoundingSetDto;
    import GoOutTypeRoundingSetDto = service.model.common.GoOutTypeRoundingSetDto;
    import GoOutTimezoneRoundingSetDto = service.model.common.GoOutTimezoneRoundingSetDto;
    import WorkTimezoneGoOutSetDto = service.model.common.WorkTimezoneGoOutSetDto;
    import WorkTimezoneCommonSetDto = service.model.common.WorkTimezoneCommonSetDto;
    import InstantRoundingDto = service.model.common.InstantRoundingDto;
    import RoundingSetDto = service.model.common.RoundingSetDto;
    import PrioritySettingDto = service.model.common.PrioritySettingDto;
    import WorkTimezoneStampSetDto = service.model.common.WorkTimezoneStampSetDto;
    import WorkTimezoneLateNightTimeSetDto = service.model.common.WorkTimezoneLateNightTimeSetDto;
    import WorkTimezoneShortTimeWorkSetDto = service.model.common.WorkTimezoneShortTimeWorkSetDto;
    import HolidayFramsetDto = service.model.common.HolidayFramsetDto;
    import ExtraordWorkOTFrameSetDto = service.model.common.ExtraordWorkOTFrameSetDto;
    import WorkTimezoneExtraordTimeSetDto = service.model.common.WorkTimezoneExtraordTimeSetDto;
    import EmTimezoneLateEarlyCommonSetDto = service.model.common.EmTimezoneLateEarlyCommonSetDto;
    import GraceTimeSettingDto = service.model.common.GraceTimeSettingDto;
    import OtherEmTimezoneLateEarlySetDto = service.model.common.OtherEmTimezoneLateEarlySetDto;
    import WorkTimezoneLateEarlySetDto = service.model.common.WorkTimezoneLateEarlySetDto;
    import StampReflectTimezoneDto = service.model.common.StampReflectTimezoneDto;
    import OverTimeOfTimeZoneSetDto = service.model.common.OverTimeOfTimeZoneSetDto;
    import FlowWorkRestTimezoneDto = service.model.common.FlowWorkRestTimezoneDto;
    import EmTimeZoneSetDto = service.model.common.EmTimeZoneSetDto;
    import FixedWorkRestSetDto = service.model.common.FixedWorkRestSetDto;
    import FixedWorkTimezoneSetDto = service.model.common.FixedWorkTimezoneSetDto;

    import LateEarlyAtr = service.model.common.LateEarlyAtr;
    import WorkSystemAtr = service.model.common.WorkSystemAtr;
    import SubHolidayOriginAtr = service.model.common.SubHolidayOriginAtr;
    import SubHolTransferSetAtr = service.model.common.SubHolTransferSetAtr;

    export module viewmodel {
        export module common {

            export class DesignatedTimeModel {
                oneDayTime: KnockoutObservable<number>;
                halfDayTime: KnockoutObservable<number>;

                constructor() {
                    this.oneDayTime = ko.observable(0);
                    this.halfDayTime = ko.observable(0);
                }

                updataData(data: DesignatedTimeDto) {
                    this.oneDayTime(data.oneDayTime);
                    this.halfDayTime(data.halfDayTime);
                }

                toDto(): DesignatedTimeDto {
                    var dataDTO: DesignatedTimeDto = {                       
                        oneDayTime: nts.uk.util.isNullOrEmpty(this.oneDayTime()) ? 0 : this.oneDayTime(),
                        halfDayTime: nts.uk.util.isNullOrEmpty(this.halfDayTime()) ? 0 : this.halfDayTime()
                    };
                    return dataDTO;
                }
            }

            export class SubHolTransferSetModel {
                certainTime: KnockoutObservable<number>;
                useDivision: KnockoutObservable<boolean>;
                designatedTime: DesignatedTimeModel;
                subHolTransferSetAtr: KnockoutObservable<number>;

                constructor() {
                    this.certainTime = ko.observable(0);
                    this.useDivision = ko.observable(false);
                    this.designatedTime = new DesignatedTimeModel();
                    this.subHolTransferSetAtr = ko.observable(SubHolTransferSetAtr.SPECIFIED_TIME_SUB_HOL);
                }

                updateData(data: SubHolTransferSetDto) {
                    this.certainTime(data.certainTime);
                    this.useDivision(data.useDivision);
                    this.designatedTime.updataData(data.designatedTime);
                    this.subHolTransferSetAtr(data.subHolTransferSetAtr);
                }

                toDto(): SubHolTransferSetDto {
                    let dataDTO: SubHolTransferSetDto = {
                        certainTime: nts.uk.util.isNullOrEmpty(this.certainTime()) ? 0 : this.certainTime(),
                        useDivision: this.useDivision(),
                        designatedTime: this.designatedTime.toDto(),
                        subHolTransferSetAtr: this.subHolTransferSetAtr(),
                    };
                    return dataDTO;
                }
            }

            export class TimeRoundingSettingModel {
                roundingTime: KnockoutObservable<number>;
                rounding: KnockoutObservable<number>;

                constructor() {
                    this.roundingTime = ko.observable(0);
                    this.rounding = ko.observable(1);
                }

                updateData(data: TimeRoundingSettingDto) {
                    this.roundingTime(data.roundingTime);
                    this.rounding(data.rounding);
                }

                toDto(): TimeRoundingSettingDto {
                    var dataDTO: TimeRoundingSettingDto = {
                        roundingTime: this.roundingTime(),
                        rounding: this.rounding(),
                    };
                    return dataDTO;
                }

                resetData() {
                    this.roundingTime(0);                                       
                    this.rounding(1);
                }
            }

            export class WorkTimezoneOtherSubHolTimeSetModel {
                subHolTimeSet: SubHolTransferSetModel;
                workTimeCode: KnockoutObservable<string>;
                originAtr: KnockoutObservable<number>;

                constructor() {
                    this.subHolTimeSet = new SubHolTransferSetModel();
                    this.workTimeCode = ko.observable('');
                    this.originAtr = ko.observable(0);
                }

                static getDefaultData(): Array<WorkTimezoneOtherSubHolTimeSetModel> {
                    let dayOffTime = new WorkTimezoneOtherSubHolTimeSetModel();
                    dayOffTime.originAtr(SubHolidayOriginAtr.WORK_DAY_OFF_TIME);
                    let overTime = new WorkTimezoneOtherSubHolTimeSetModel();
                    overTime.originAtr(SubHolidayOriginAtr.FROM_OVER_TIME);
                    let list: WorkTimezoneOtherSubHolTimeSetModel[] = [];
                    list.push(dayOffTime);
                    list.push(overTime);
                    return list;
                }

                updateData(data: WorkTimezoneOtherSubHolTimeSetDto) {
                    this.subHolTimeSet.updateData(data.subHolTimeSet);
                    this.workTimeCode(data.workTimeCode);
                    this.originAtr(data.originAtr);
                }

                toDto(): WorkTimezoneOtherSubHolTimeSetDto {
                    var dataDTO: WorkTimezoneOtherSubHolTimeSetDto = {
                        subHolTimeSet: this.subHolTimeSet.toDto(),
                        workTimeCode: this.workTimeCode(),
                        originAtr: this.originAtr()
                    };
                    return dataDTO;
                }
            }

            export class WorkTimezoneMedicalSetModel {
                roundingSet: TimeRoundingSettingModel;
                workSystemAtr: KnockoutObservable<number>;
                applicationTime: KnockoutObservable<number>;

                constructor() {
                    this.roundingSet = new TimeRoundingSettingModel();
                    this.workSystemAtr = ko.observable(0);
                    this.applicationTime = ko.observable(0);
                }

                static getDefaultData(): Array<WorkTimezoneMedicalSetModel> {
                    let dayShift = new WorkTimezoneMedicalSetModel();
                    dayShift.workSystemAtr(WorkSystemAtr.DAY_SHIFT);
                    let nightShift = new WorkTimezoneMedicalSetModel();
                    nightShift.workSystemAtr(WorkSystemAtr.NIGHT_SHIFT);
                    let list: WorkTimezoneMedicalSetModel[] = [];
                    list.push(dayShift);
                    list.push(nightShift);
                    return list;
                }

                updateData(data: WorkTimezoneMedicalSetDto) {
                    this.roundingSet.updateData(data.roundingSet);
                    this.workSystemAtr(data.workSystemAtr);
                    this.applicationTime(data.applicationTime);
                }

                toDto(): WorkTimezoneMedicalSetDto {
                    var dataDTO: WorkTimezoneMedicalSetDto = {
                        roundingSet: this.roundingSet.toDto(),
                        workSystemAtr: this.workSystemAtr(),
                        applicationTime: nts.uk.util.isNullOrEmpty(this.applicationTime()) ? 0 : this.applicationTime()    //QA 87128
                    };
                    return dataDTO;
                }

                resetData() {
                    this.roundingSet.resetData();
                    this.applicationTime(0);
                }
            }

            export class WorkTimezoneCommonSetModel {
                zeroHStraddCalculateSet: KnockoutObservable<boolean>;
                intervalSet: IntervalTimeSettingModel;
                subHolTimeSet: WorkTimezoneOtherSubHolTimeSetModel[];
                raisingSalarySet: KnockoutObservable<string>;
                medicalSet: WorkTimezoneMedicalSetModel[];
                goOutSet: WorkTimezoneGoOutSetModel;
                stampSet: WorkTimezoneStampSetModel;
                lateNightTimeSet: WorkTimezoneLateNightTimeSetModel;
                shortTimeWorkSet: WorkTimezoneShortTimeWorkSetModel;
                extraordTimeSet: WorkTimezoneExtraordTimeSetModel;
                lateEarlySet: WorkTimezoneLateEarlySetModel;

                constructor() {
                    this.zeroHStraddCalculateSet = ko.observable(false);
                    this.intervalSet = new IntervalTimeSettingModel();
                    this.subHolTimeSet = WorkTimezoneOtherSubHolTimeSetModel.getDefaultData();
                    this.raisingSalarySet = ko.observable('');
                    this.medicalSet = WorkTimezoneMedicalSetModel.getDefaultData();
                    this.goOutSet = new WorkTimezoneGoOutSetModel();
                    this.stampSet = new WorkTimezoneStampSetModel();
                    this.lateNightTimeSet = new WorkTimezoneLateNightTimeSetModel();
                    this.shortTimeWorkSet = new WorkTimezoneShortTimeWorkSetModel();
                    this.extraordTimeSet = new WorkTimezoneExtraordTimeSetModel();
                    this.lateEarlySet = new WorkTimezoneLateEarlySetModel();
                }

                updateData(data: WorkTimezoneCommonSetDto) {
                    if (data) {
                        this.zeroHStraddCalculateSet(data.zeroHStraddCalculateSet);
                        this.intervalSet.updateData(data.intervalSet);

                        let newSubHolTimeSet: WorkTimezoneOtherSubHolTimeSetModel[] = _.map(data.subHolTimeSet, (dataDTO) => {
                            let dataModel: WorkTimezoneOtherSubHolTimeSetModel = new WorkTimezoneOtherSubHolTimeSetModel();
                            dataModel.updateData(dataDTO);
                            return dataModel;
                        });
                        let newWorkDayOffTimeSet = _.find(newSubHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.WORK_DAY_OFF_TIME);
                        let workDayOffTimeSet = _.find(this.subHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.WORK_DAY_OFF_TIME);
                        if (nts.uk.util.isNullOrUndefined(newWorkDayOffTimeSet)) {
                            workDayOffTimeSet.updateData(WorkTimezoneCommonSetModel.getDefaultSubHol(SubHolidayOriginAtr.WORK_DAY_OFF_TIME).toDto());
                        } else {
                            workDayOffTimeSet.updateData(newWorkDayOffTimeSet.toDto());
                        }
                        let newOverTimeSet = _.find(newSubHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.FROM_OVER_TIME);
                        let overTimeSet = _.find(this.subHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.FROM_OVER_TIME);
                        if (nts.uk.util.isNullOrUndefined(newOverTimeSet)) {
                            overTimeSet.updateData(WorkTimezoneCommonSetModel.getDefaultSubHol(SubHolidayOriginAtr.FROM_OVER_TIME).toDto());
                        } else {
                            overTimeSet.updateData(newOverTimeSet.toDto());
                        }

                        this.raisingSalarySet(data.raisingSalarySet);

                        let newMedicalSet: WorkTimezoneMedicalSetModel[] = _.map(data.medicalSet, (dataDTO) => {
                            let dataModel: WorkTimezoneMedicalSetModel = new WorkTimezoneMedicalSetModel();
                            dataModel.updateData(dataDTO);
                            return dataModel;
                        });
                        let newDayShift = _.find(newMedicalSet, o => o.workSystemAtr() == WorkSystemAtr.DAY_SHIFT);
                        let dayShift = _.find(this.medicalSet, o => o.workSystemAtr() == WorkSystemAtr.DAY_SHIFT);
                        if (nts.uk.util.isNullOrUndefined(newDayShift)) {
                            dayShift.updateData(WorkTimezoneCommonSetModel.getDefaultMedical(WorkSystemAtr.DAY_SHIFT).toDto());
                        } else {
                            dayShift.updateData(newDayShift.toDto());
                        }
                        let newNightShift = _.find(newMedicalSet, o => o.workSystemAtr() == WorkSystemAtr.NIGHT_SHIFT);
                        let nightShift = _.find(this.medicalSet, o => o.workSystemAtr() == WorkSystemAtr.NIGHT_SHIFT);
                        if (nts.uk.util.isNullOrUndefined(newNightShift)) {
                            nightShift.updateData(WorkTimezoneCommonSetModel.getDefaultMedical(WorkSystemAtr.NIGHT_SHIFT).toDto());
                        } else {
                            nightShift.updateData(newNightShift.toDto());
                        }

                        this.goOutSet.updateData(data.goOutSet);
                        this.stampSet.updateData(data.stampSet);
                        this.lateNightTimeSet.updateData(data.lateNightTimeSet);
                        this.shortTimeWorkSet.updateData(data.shortTimeWorkSet);
                        this.extraordTimeSet.updateData(data.extraordTimeSet);
                        this.lateEarlySet.updateData(data.lateEarlySet);
                    }
                }

                toDto(): WorkTimezoneCommonSetDto {
                    let subHolTimeSet: WorkTimezoneOtherSubHolTimeSetDto[] = _.map(this.subHolTimeSet, (dataModel) => dataModel.toDto());
                    let medicalSet: WorkTimezoneMedicalSetDto[] = _.map(this.medicalSet, (dataModel) => dataModel.toDto());
                    var dataDTO: WorkTimezoneCommonSetDto = {
                        zeroHStraddCalculateSet: this.zeroHStraddCalculateSet(),
                        intervalSet: this.intervalSet.toDto(),
                        subHolTimeSet: subHolTimeSet,
                        raisingSalarySet: this.raisingSalarySet(),
                        medicalSet: medicalSet,
                        goOutSet: this.goOutSet.toDto(),
                        stampSet: this.stampSet.toDto(),
                        lateNightTimeSet: this.lateNightTimeSet.toDto(),
                        shortTimeWorkSet: this.shortTimeWorkSet.toDto(),
                        extraordTimeSet: this.extraordTimeSet.toDto(),
                        lateEarlySet: this.lateEarlySet.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.zeroHStraddCalculateSet(false);
                    this.intervalSet.resetData();

                    let workDayOffTimeSet = _.find(this.subHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.WORK_DAY_OFF_TIME);
                    workDayOffTimeSet.updateData(WorkTimezoneCommonSetModel.getDefaultSubHol(SubHolidayOriginAtr.WORK_DAY_OFF_TIME).toDto());
                    let overTimeSet = _.find(this.subHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.FROM_OVER_TIME);
                    overTimeSet.updateData(WorkTimezoneCommonSetModel.getDefaultSubHol(SubHolidayOriginAtr.FROM_OVER_TIME).toDto());

                    this.raisingSalarySet('');

                    let dayShift = _.find(this.medicalSet, o => o.workSystemAtr() == WorkSystemAtr.DAY_SHIFT);
                    dayShift.updateData(WorkTimezoneCommonSetModel.getDefaultMedical(WorkSystemAtr.DAY_SHIFT).toDto());
                    let nightShift = _.find(this.medicalSet, o => o.workSystemAtr() == WorkSystemAtr.NIGHT_SHIFT);
                    nightShift.updateData(WorkTimezoneCommonSetModel.getDefaultMedical(WorkSystemAtr.NIGHT_SHIFT).toDto());

                    this.goOutSet.resetData();
                    this.stampSet.resetData();
                    this.lateNightTimeSet.resetData();
                    this.shortTimeWorkSet.resetData();
                    this.extraordTimeSet.resetData();
                    this.lateEarlySet.resetData();
                }

                public static getDefaultSubHol(originAtr: SubHolidayOriginAtr): WorkTimezoneOtherSubHolTimeSetModel {
                    let defaultObj = new WorkTimezoneOtherSubHolTimeSetModel();
                    defaultObj.originAtr(originAtr);
                    return defaultObj;
                }

                public static getDefaultMedical(workSystemAtr: WorkSystemAtr): WorkTimezoneMedicalSetModel {
                    let defaultObj = new WorkTimezoneMedicalSetModel();
                    defaultObj.workSystemAtr(workSystemAtr);
                    return defaultObj;
                }

                public getWorkDayOffTimeSet(): WorkTimezoneOtherSubHolTimeSetModel {
                    let self = this;
                    return _.find(self.subHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.WORK_DAY_OFF_TIME);
                }
                public getOverTimeSet(): WorkTimezoneOtherSubHolTimeSetModel {
                    let self = this;
                    return _.find(self.subHolTimeSet, o => o.originAtr() == SubHolidayOriginAtr.FROM_OVER_TIME);
                }

                public getMedicalDayShift(): WorkTimezoneMedicalSetModel {
                    let self = this;
                    return _.find(self.medicalSet, o => o.workSystemAtr() == WorkSystemAtr.DAY_SHIFT);
                }
                public getMedicalNightShift(): WorkTimezoneMedicalSetModel {
                    let self = this;
                    return _.find(self.medicalSet, o => o.workSystemAtr() == WorkSystemAtr.NIGHT_SHIFT);
                }
            }

            export class TotalRoundingSetModel {
                setSameFrameRounding: KnockoutObservable<number>;
                frameStraddRoundingSet: KnockoutObservable<number>;

                constructor() {
                    this.setSameFrameRounding = ko.observable(0);
                    this.frameStraddRoundingSet = ko.observable(0);
                }

                updateData(data: TotalRoundingSetDto) {
                    this.setSameFrameRounding(data.setSameFrameRounding);
                    this.frameStraddRoundingSet(data.frameStraddRoundingSet);
                }

                toDto(): TotalRoundingSetDto {
                    var dataDTO: TotalRoundingSetDto = {
                        setSameFrameRounding: this.setSameFrameRounding(),
                        frameStraddRoundingSet: this.frameStraddRoundingSet()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.setSameFrameRounding(0);
                    this.frameStraddRoundingSet(0);
                }
            }



            export class CommonRestSettingModel {
                calculateMethod: KnockoutObservable<number>;
                constructor() {
                    this.calculateMethod = ko.observable(0);
                }

                updateData(data: CommonRestSettingDto) {
                    this.calculateMethod(data.calculateMethod);
                }

                toDto(): CommonRestSettingDto {
                    var dataDTO: CommonRestSettingDto = {
                        calculateMethod: this.calculateMethod()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.calculateMethod(0);
                }

            }

            export class FlowRestSetModel {
                useStamp: KnockoutObservable<boolean>;
                useStampCalcMethod: KnockoutObservable<number>;
                timeManagerSetAtr: KnockoutObservable<number>;
                calculateMethod: KnockoutObservable<number>;

                constructor() {
                    this.useStamp = ko.observable(false);
                    this.useStampCalcMethod = ko.observable(0);
                    this.timeManagerSetAtr = ko.observable(0);
                    this.calculateMethod = ko.observable(0);
                }

                updateData(data: FlowRestSetDto) {
                    this.useStamp(data.useStamp);
                    this.useStampCalcMethod(data.useStampCalcMethod);
                    this.timeManagerSetAtr(data.timeManagerSetAtr);
                    this.calculateMethod(data.calculateMethod);
                }

                toDto(): FlowRestSetDto {
                    var dataDTO: FlowRestSetDto = {
                        useStamp: this.useStamp(),
                        useStampCalcMethod: this.useStampCalcMethod(),
                        timeManagerSetAtr: this.timeManagerSetAtr(),
                        calculateMethod: this.calculateMethod()
                    };
                    return dataDTO;

                }

                resetData() {
                    this.useStamp(false);
                    this.useStampCalcMethod(0);
                    this.timeManagerSetAtr(0);
                    this.calculateMethod(0);
                }
            }

            export class FlowFixedRestSetModel {
                isReferRestTime: KnockoutObservable<boolean>;
                usePrivateGoOutRest: KnockoutObservable<boolean>;
                useAssoGoOutRest: KnockoutObservable<boolean>;
                calculateMethod: KnockoutObservable<number>;

                constructor() {
                    this.isReferRestTime = ko.observable(false);
                    this.usePrivateGoOutRest = ko.observable(false);
                    this.useAssoGoOutRest = ko.observable(false);
                    this.calculateMethod = ko.observable(0);
                }

                updatedData(data: FlowFixedRestSetDto) {
                    this.isReferRestTime(data.isReferRestTime);
                    this.usePrivateGoOutRest(data.usePrivateGoOutRest);
                    this.useAssoGoOutRest(data.useAssoGoOutRest);
                    this.calculateMethod(data.calculateMethod);
                }

                toDto(): FlowFixedRestSetDto {
                    var dataDTO: FlowFixedRestSetDto = {
                        isReferRestTime: this.isReferRestTime(),
                        usePrivateGoOutRest: this.usePrivateGoOutRest(),
                        useAssoGoOutRest: this.useAssoGoOutRest(),
                        calculateMethod: this.calculateMethod()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.isReferRestTime(false);
                    this.usePrivateGoOutRest(false);
                    this.useAssoGoOutRest(false);
                    this.calculateMethod(0);
                }
            }

            export class FlowWorkRestSettingDetailModel {
                flowRestSetting: FlowRestSetModel;
                flowFixedRestSetting: FlowFixedRestSetModel;
                usePluralWorkRestTime: KnockoutObservable<boolean>;

                constructor() {
                    this.flowRestSetting = new FlowRestSetModel();
                    this.flowFixedRestSetting = new FlowFixedRestSetModel();
                    this.usePluralWorkRestTime = ko.observable(false);
                }

                updateData(data: FlowWorkRestSettingDetailDto) {
                    this.flowRestSetting.updateData(data.flowRestSetting);
                    this.flowFixedRestSetting.updatedData(data.flowFixedRestSetting);
                    this.usePluralWorkRestTime(data.usePluralWorkRestTime);
                }

                toDto(): FlowWorkRestSettingDetailDto {
                    var dataDTO: FlowWorkRestSettingDetailDto = {
                        flowRestSetting: this.flowRestSetting.toDto(),
                        flowFixedRestSetting: this.flowFixedRestSetting.toDto(),
                        usePluralWorkRestTime: this.usePluralWorkRestTime()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.flowRestSetting.resetData();
                    this.flowFixedRestSetting.resetData();
                    this.usePluralWorkRestTime(false);
                }
            }

            export class FlowWorkRestSettingModel {
                commonRestSetting: CommonRestSettingModel;
                flowRestSetting: FlowWorkRestSettingDetailModel;

                constructor() {
                    this.commonRestSetting = new CommonRestSettingModel();
                    this.flowRestSetting = new FlowWorkRestSettingDetailModel();
                }

                updateData(data: FlowWorkRestSettingDto) {
                    this.commonRestSetting.updateData(data.commonRestSetting);
                    this.flowRestSetting.updateData(data.flowRestSetting);
                }

                toDto(): FlowWorkRestSettingDto {
                    var dataDTO: FlowWorkRestSettingDto = {
                        commonRestSetting: this.commonRestSetting.toDto(),
                        flowRestSetting: this.flowRestSetting.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.commonRestSetting.resetData();
                    this.flowRestSetting.resetData();
                }
            }


            export class TimeZoneRoundingModel {
                rounding: TimeRoundingSettingModel;
                start: KnockoutObservable<number>;
                end: KnockoutObservable<number>;

                constructor() {
                    this.rounding = new TimeRoundingSettingModel();
                    this.start = ko.observable(0);
                    this.end = ko.observable(0);
                }

                updateData(data: TimeZoneRoundingDto) {
                    this.rounding.updateData(data.rounding);
                    this.start(data.start);
                    this.end(data.end);
                }

                toDto(): TimeZoneRoundingDto {
                    var dataDTO: TimeZoneRoundingDto = {
                        rounding: this.rounding.toDto(),
                        start: this.start(),
                        end: this.end()
                    };
                    return dataDTO;
                }
            }


            export class HDWorkTimeSheetSettingModel {
                workTimeNo: KnockoutObservable<number>;
                timezone: TimeZoneRoundingModel;
                isLegalHolidayConstraintTime: KnockoutObservable<boolean>;
                inLegalBreakFrameNo: KnockoutObservable<number>;
                isNonStatutoryDayoffConstraintTime: KnockoutObservable<boolean>;
                outLegalBreakFrameNo: KnockoutObservable<number>;
                isNonStatutoryHolidayConstraintTime: KnockoutObservable<boolean>;
                outLegalPubHDFrameNo: KnockoutObservable<number>;

                constructor() {
                    this.workTimeNo = ko.observable(0);
                    this.timezone = new TimeZoneRoundingModel();
                    this.isLegalHolidayConstraintTime = ko.observable(false);
                    this.inLegalBreakFrameNo = ko.observable(1);
                    this.isNonStatutoryDayoffConstraintTime = ko.observable(false);
                    this.outLegalBreakFrameNo = ko.observable(1);
                    this.isNonStatutoryHolidayConstraintTime = ko.observable(false);
                    this.outLegalPubHDFrameNo = ko.observable(1);
                }

                updateData(data: HDWorkTimeSheetSettingDto) {
                    this.workTimeNo(data.workTimeNo);
                    this.timezone.updateData(data.timezone);
                    this.isLegalHolidayConstraintTime(data.isLegalHolidayConstraintTime);
                    this.inLegalBreakFrameNo(data.inLegalBreakFrameNo);
                    this.isNonStatutoryDayoffConstraintTime(data.isNonStatutoryDayoffConstraintTime);
                    this.outLegalBreakFrameNo(data.outLegalBreakFrameNo);
                    this.isNonStatutoryHolidayConstraintTime(data.isNonStatutoryHolidayConstraintTime);
                    this.outLegalPubHDFrameNo(data.outLegalPubHDFrameNo);
                }

                toDto(): HDWorkTimeSheetSettingDto {
                    var dataDTO: HDWorkTimeSheetSettingDto = {
                        workTimeNo: this.workTimeNo(),
                        timezone: this.timezone.toDto(),
                        isLegalHolidayConstraintTime: this.isLegalHolidayConstraintTime(),
                        inLegalBreakFrameNo: this.inLegalBreakFrameNo(),
                        isNonStatutoryDayoffConstraintTime: this.isNonStatutoryDayoffConstraintTime(),
                        outLegalBreakFrameNo: this.outLegalBreakFrameNo(),
                        isNonStatutoryHolidayConstraintTime: this.isNonStatutoryHolidayConstraintTime(),
                        outLegalPubHDFrameNo: this.outLegalPubHDFrameNo()
                    };
                    return dataDTO;
                }
            }

            export class TimeRangeModel {
                column1: KnockoutObservable<TimeRange>;
            }

            export class TimeRange {
                startTime: number;
                endTime: number;
            }

            export abstract class FixedTableDataConverter<C, O> {
                convertedList: KnockoutObservableArray<C>;
                originalList: KnockoutObservableArray<O>;
                originalListTemp: Array<any>;
                convertedListTemp: Array<any>;

                constructor() {
                    let self = this;
                    self.convertedList = ko.observableArray([]);
                    self.originalList = ko.observableArray([]);
                    self.originalListTemp = [];
                    self.convertedListTemp = [];
                    
                    self.originalList.subscribe(newList => {
                        let newTemp = self.toOriginalListTemp(newList);
                        if (self.isNotEqual(newTemp, self.originalListTemp)) {
                            self.originalListTemp = newTemp;
                            self.convertedList(self.toConvertedList());
                        }
                    });

                    self.convertedList.subscribe(newList => {
                        let newTemp = self.toConvertedListTemp(newList);
                        if (self.isNotEqual(newTemp, self.convertedListTemp)) {
                            self.convertedListTemp = newTemp;
                            self.originalList(self.fromConvertedList(newList));
                        }
                    });
                }

                /**
                 * To converted list temp
                 */
                abstract toConvertedListTemp(list: Array<C>): any;

                /**
                 * To original list temp
                 */
                abstract toOriginalListTemp(list: Array<O>): any;

                /**
                 * Convert to list time range
                 */
                abstract toConvertedList(): Array<C>;

                /**
                 * Revert to original list
                 */
                abstract fromConvertedList(newList: Array<C>): Array<O>;

                /**
                 * Evaluate 2 arrays
                 */
                isNotEqual(value, other): boolean {
                    return !_.isEqual(value, other);
                }
            }

            export abstract class TimeRangeModelConverter<T> extends FixedTableDataConverter<TimeRangeModel, T> {

                toConvertedListTemp(list: Array<TimeRangeModel>): any {
                    return _.map(list, item => {
                        return { start: item.column1().startTime, end: item.column1().endTime };
                    });
                }

                toOriginalListTemp(list: Array<any>): any {
                    return _.map(list, item => {
                        return { start: item.start(), end: item.end() };
                    });
                }

                /**
                 * Convert to TimeRangeItem
                 */
                public toTimeRangeItem(start: number, end: number): TimeRangeModel {
                    return { column1: ko.observable({ startTime: start, endTime: end }) };
                }
            }

            export class DeductionTimeModel {
                start: KnockoutObservable<number>;
                end: KnockoutObservable<number>;

                constructor() {
                    this.start = ko.observable(0);
                    this.end = ko.observable(0);
                }

                updateData(data: DeductionTimeDto) {
                    this.start(data.start);
                    this.end(data.end);
                }

                toDto(): DeductionTimeDto {
                    var dataDTO: DeductionTimeDto = {
                        start: this.start(),
                        end: this.end()
                    };
                    return dataDTO;
                }
            }

            export class TimezoneOfFixedRestTimeSetModel extends TimeRangeModelConverter<DeductionTimeModel> {
                timezones: KnockoutObservableArray<DeductionTimeModel>;

                constructor() {
                    super();
                    this.timezones = this.originalList;
                }

                toConvertedList(): Array<TimeRangeModel> {
                    let self = this;
                    return _.map(self.timezones(), tz => self.toTimeRangeItem(tz.start(), tz.end()));
                }

                fromConvertedList(newList: Array<TimeRangeModel>): Array<DeductionTimeModel> {
                    return _.map(newList, newVl => {
                        let vl = new DeductionTimeModel();
                        vl.start(newVl.column1().startTime);
                        vl.end(newVl.column1().endTime);
                        return vl;
                    });
                }

                updateData(data: TimezoneOfFixedRestTimeSetDto) {
                    let mapped = _.map(data.timezones, dto => {
                        let model = new DeductionTimeModel();
                        model.updateData(dto);
                        return model
                    });
                    this.timezones(mapped);
                }

                toDto(): TimezoneOfFixedRestTimeSetDto {
                    var timezones: DeductionTimeDto[] = [];
                    _.forEach(this.timezones(), tz => timezones.push(tz.toDto()));
                    var dataDTO: TimezoneOfFixedRestTimeSetDto = {
                        timezones: timezones
                    };
                    return dataDTO;
                }

                resetData() {
                    this.timezones([]);
                }
            }

            export class FlowRestSettingModel {
                flowRestTime: KnockoutObservable<number>;
                flowPassageTime: KnockoutObservable<number>;

                constructor() {
                    this.flowRestTime = ko.observable(0);
                    this.flowPassageTime = ko.observable(0);
                }

                updateData(data: FlowRestSettingDto) {
                    this.flowRestTime(data.flowRestTime);
                    this.flowPassageTime(data.flowPassageTime);
                }

                toDto(): FlowRestSettingDto {
                    var dataDTO: FlowRestSettingDto = {
                        flowRestTime: this.flowRestTime(),
                        flowPassageTime: this.flowPassageTime()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.flowRestTime(0);
                    this.flowPassageTime(0);
                }
            }

            export class FlowRestTimezoneModel extends FixedTableDataConverter<FixedTableTimeEditorModel, FlowRestSettingModel> {
                flowRestSets: KnockoutObservableArray<FlowRestSettingModel>;
                useHereAfterRestSet: KnockoutObservable<boolean>;
                hereAfterRestSet: FlowRestSettingModel;

                constructor() {
                    super();
                    this.flowRestSets = ko.observableArray([]);
                    this.useHereAfterRestSet = ko.observable(false);
                    this.hereAfterRestSet = new FlowRestSettingModel();
                }

                toConvertedListTemp(list: Array<FixedTableTimeEditorModel>): any {
                    return _.map(list, item => {
                        return { start: item.startCol(), end: item.endCol() };
                    });
                }

                /**
                 * To original list temp
                 */
                toOriginalListTemp(list: Array<FlowRestSettingModel>): any {
                    return _.map(list, item => {
                        return { start: item.flowRestTime(), end: item.flowPassageTime() };
                    });
                }

                /**
                 * Convert to list time range
                 */
                toConvertedList(): Array<FixedTableTimeEditorModel> {
                    let self = this;
                    return _.map(self.flowRestSets(), rs => self.toTimeEditorItem(rs.flowRestTime(), rs.flowPassageTime()));
                }

                /**
                 * Revert to original list
                 */
                fromConvertedList(newList: Array<FixedTableTimeEditorModel>): Array<FlowRestSettingModel> {
                    return _.map(newList, newVl => {
                        let vl = new FlowRestSettingModel();
                        vl.flowRestTime(newVl.startCol());
                        vl.flowPassageTime(newVl.endCol());
                        return vl;
                    });
                }

                private toTimeEditorItem(start: number, end: number): any {
                    return { startCol: ko.observable(start), endCol: ko.observable(end) };
                }

                updateData(data: FlowRestTimezoneDto) {
                    let mapped = _.map(data.flowRestSets, dto => {
                        let model = new FlowRestSettingModel();
                        model.updateData(dto);
                        return model
                    });
                    this.flowRestSets(mapped);
                    this.useHereAfterRestSet(data.useHereAfterRestSet);
                    this.hereAfterRestSet.updateData(data.hereAfterRestSet);
                }

                toDto(): FlowRestTimezoneDto {
                    let flowRestSets = this.flowRestSets().map(item => item.toDto());
                    var dataDTO: FlowRestTimezoneDto = {
                        flowRestSets: flowRestSets,
                        useHereAfterRestSet: this.useHereAfterRestSet(),
                        hereAfterRestSet: this.hereAfterRestSet.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.flowRestSets([]);
                    this.useHereAfterRestSet(false);
                    this.hereAfterRestSet.resetData();
                }
            }

            export class FixedTableTimeEditorModel {
                startCol: KnockoutObservable<number>;
                endCol: KnockoutObservable<number>;
            }

            export class FlowWorkRestTimezoneModel {
                fixRestTime: KnockoutObservable<boolean>;
                fixedRestTimezone: TimezoneOfFixedRestTimeSetModel;
                flowRestTimezone: FlowRestTimezoneModel;

                constructor() {
                    this.fixRestTime = ko.observable(true);
                    this.fixedRestTimezone = new TimezoneOfFixedRestTimeSetModel();
                    this.flowRestTimezone = new FlowRestTimezoneModel();
                }

                updateData(data: FlowWorkRestTimezoneDto) {
                    this.fixRestTime(data.fixRestTime);
                    this.fixedRestTimezone.updateData(data.fixedRestTimezone);
                    this.flowRestTimezone.updateData(data.flowRestTimezone);
                }

                toDto(): FlowWorkRestTimezoneDto {
                    var dataDTO: FlowWorkRestTimezoneDto = {
                        fixRestTime: this.fixRestTime(),
                        fixedRestTimezone: this.fixedRestTimezone.toDto(),
                        flowRestTimezone: this.flowRestTimezone.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.fixRestTime(true);
                    this.fixedRestTimezone.resetData();
                    this.flowRestTimezone.resetData();
                }
            }



            export class IntervalTimeModel {
                intervalTime: KnockoutObservable<number>;
                rounding: TimeRoundingSettingModel;

                constructor() {
                    this.intervalTime = ko.observable(0);
                    this.rounding = new TimeRoundingSettingModel();
                }

                updateData(data: IntervalTimeDto) {
                    this.intervalTime(data.intervalTime);
                    this.rounding.updateData(data.rounding);
                }

                toDto(): IntervalTimeDto {
                    var dataDTO: IntervalTimeDto = {
                        intervalTime: this.intervalTime(),
                        rounding: this.rounding.toDto()
                    };
                    return dataDTO;
                }
            }

            export class IntervalTimeSettingModel {
                useIntervalExemptionTime: KnockoutObservable<boolean>;
                intervalExemptionTimeRound: TimeRoundingSettingModel;
                intervalTime: IntervalTimeModel;
                useIntervalTime: KnockoutObservable<boolean>;

                constructor() {
                    this.useIntervalExemptionTime = ko.observable(false);
                    this.intervalExemptionTimeRound = new TimeRoundingSettingModel();
                    this.intervalTime = new IntervalTimeModel();
                    this.useIntervalTime = ko.observable(false);
                }

                updateData(data: IntervalTimeSettingDto) {
                    this.useIntervalExemptionTime(data.useIntervalExemptionTime);
                    this.intervalExemptionTimeRound.updateData(data.intervalExemptionTimeRound);
                    this.intervalTime.updateData(data.intervalTime);
                    this.useIntervalTime(data.useIntervalTime);
                }

                toDto(): IntervalTimeSettingDto {
                    var dataDTO: IntervalTimeSettingDto = {
                        useIntervalExemptionTime: this.useIntervalExemptionTime(),
                        intervalExemptionTimeRound: this.intervalExemptionTimeRound.toDto(),
                        intervalTime: this.intervalTime.toDto(),
                        useIntervalTime: this.useIntervalTime()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.useIntervalExemptionTime(false);

                }
            }

            export class GoOutTimeRoundingSettingModel {
                roundingMethod: KnockoutObservable<number>;
                roundingSetting: TimeRoundingSettingModel;

                constructor() {
                    this.roundingMethod = ko.observable(0);
                    this.roundingSetting = new TimeRoundingSettingModel();
                }

                updataData(data: GoOutTimeRoundingSettingDto) {
                    this.roundingMethod(data.roundingMethod);
                    this.roundingSetting.updateData(data.roundingSetting);
                }

                toDto(): GoOutTimeRoundingSettingDto {
                    var dataDTO: GoOutTimeRoundingSettingDto = {
                        roundingMethod: this.roundingMethod(),
                        roundingSetting: this.roundingSetting.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.roundingMethod(0);
                    this.roundingSetting.resetData();
                }
            }

            export class DeductGoOutRoundingSetModel {
                deductTimeRoundingSetting: GoOutTimeRoundingSettingModel;
                approTimeRoundingSetting: GoOutTimeRoundingSettingModel;

                constructor() {
                    this.deductTimeRoundingSetting = new GoOutTimeRoundingSettingModel();
                    this.approTimeRoundingSetting = new GoOutTimeRoundingSettingModel();
                }

                updateData(data: DeductGoOutRoundingSetDto) {
                    this.deductTimeRoundingSetting.updataData(data.deductTimeRoundingSetting);
                    this.approTimeRoundingSetting.updataData(data.approTimeRoundingSetting);
                }

                toDto(): DeductGoOutRoundingSetDto {
                    var dataDTO: DeductGoOutRoundingSetDto = {
                        deductTimeRoundingSetting: this.deductTimeRoundingSetting.toDto(),
                        approTimeRoundingSetting: this.approTimeRoundingSetting.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.deductTimeRoundingSetting.resetData();
                    this.approTimeRoundingSetting.resetData();
                }
            }


            export class GoOutTypeRoundingSetModel {
                officalUseCompenGoOut: DeductGoOutRoundingSetModel;
                privateUnionGoOut: DeductGoOutRoundingSetModel;

                constructor() {
                    this.officalUseCompenGoOut = new DeductGoOutRoundingSetModel();
                    this.privateUnionGoOut = new DeductGoOutRoundingSetModel();
                }

                updateData(data: GoOutTypeRoundingSetDto) {
                    if (data && data.officalUseCompenGoOut) {
                        this.officalUseCompenGoOut.updateData(data.officalUseCompenGoOut);
                    }
                    if (data && data.privateUnionGoOut) {
                        this.privateUnionGoOut.updateData(data.privateUnionGoOut);
                    }
                }

                toDto(): GoOutTypeRoundingSetDto {
                    var dataDTO: GoOutTypeRoundingSetDto = {
                        officalUseCompenGoOut: this.officalUseCompenGoOut.toDto(),
                        privateUnionGoOut: this.privateUnionGoOut.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.officalUseCompenGoOut.resetData();
                    this.privateUnionGoOut.resetData();
                }
            }

            export class GoOutTimezoneRoundingSetModel {
                pubHolWorkTimezone: GoOutTypeRoundingSetModel;
                workTimezone: GoOutTypeRoundingSetModel;
                ottimezone: GoOutTypeRoundingSetModel;

                constructor() {
                    this.pubHolWorkTimezone = new GoOutTypeRoundingSetModel();
                    this.workTimezone = new GoOutTypeRoundingSetModel();
                    this.ottimezone = new GoOutTypeRoundingSetModel();
                }

                updateData(data: GoOutTimezoneRoundingSetDto) {
                    this.pubHolWorkTimezone.updateData(data.pubHolWorkTimezone);
                    this.workTimezone.updateData(data.workTimezone);
                    this.ottimezone.updateData(data.ottimezone);
                }

                toDto(): GoOutTimezoneRoundingSetDto {
                    var dataDTO: GoOutTimezoneRoundingSetDto = {
                        pubHolWorkTimezone: this.pubHolWorkTimezone.toDto(),
                        workTimezone: this.workTimezone.toDto(),
                        ottimezone: this.ottimezone.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.pubHolWorkTimezone.resetData();
                    this.workTimezone.resetData();
                    this.ottimezone.resetData();
                }
            }


            export class WorkTimezoneGoOutSetModel {
                totalRoundingSet: TotalRoundingSetModel;
                diffTimezoneSetting: GoOutTimezoneRoundingSetModel;

                constructor() {
                    this.totalRoundingSet = new TotalRoundingSetModel();
                    this.diffTimezoneSetting = new GoOutTimezoneRoundingSetModel();
                }

                updateData(data: WorkTimezoneGoOutSetDto) {
                    this.totalRoundingSet.updateData(data.totalRoundingSet);
                    this.diffTimezoneSetting.updateData(data.diffTimezoneSetting);
                }

                toDto(): WorkTimezoneGoOutSetDto {
                    var dataDTO: WorkTimezoneGoOutSetDto = {
                        totalRoundingSet: this.totalRoundingSet.toDto(),
                        diffTimezoneSetting: this.diffTimezoneSetting.toDto()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.totalRoundingSet.resetData();
                    this.diffTimezoneSetting.resetData();
                }
            }

            export class InstantRoundingModel {
                fontRearSection: KnockoutObservable<number>;
                roundingTimeUnit: KnockoutObservable<number>;

                constructor() {
                    this.fontRearSection = ko.observable(0);
                    this.roundingTimeUnit = ko.observable(0);
                }

                updateData(data: InstantRoundingDto) {
                    this.fontRearSection(data.fontRearSection);
                    this.roundingTimeUnit(data.roundingTimeUnit);
                }

                toDto(): InstantRoundingDto {
                    var dataDTO: InstantRoundingDto = {
                        fontRearSection: this.fontRearSection(),
                        roundingTimeUnit: this.roundingTimeUnit()
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.fontRearSection(0);
                    this.roundingTimeUnit(0);
                }
            }

            export class RoundingSetModel {
                roundingSet: InstantRoundingModel;
                section: KnockoutObservable<number>;

                constructor(section: number) {
                    this.roundingSet = new InstantRoundingModel();
                    this.section = ko.observable(section);
                }

                updateData(data: RoundingSetDto) {
                    this.roundingSet.updateData(data.roundingSet);
                    this.section(data.section);
                }

                toDto(): RoundingSetDto {
                    var dataDTO: RoundingSetDto = {
                        roundingSet: this.roundingSet.toDto(),
                        section: this.section()
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.roundingSet.resetData();
                }
            }

            export class PrioritySettingModel {
                priorityAtr: KnockoutObservable<number>;
                stampAtr: KnockoutObservable<number>;


                constructor(priorityAtr: number) {
                    this.priorityAtr = ko.observable(priorityAtr);
                    this.stampAtr = ko.observable(0);
                }

                updateData(data: PrioritySettingDto) {
                    this.priorityAtr(data.priorityAtr);
                    this.stampAtr(data.stampAtr);
                }

                toDto(): PrioritySettingDto {
                    var dataDTO: PrioritySettingDto = {
                        priorityAtr: this.priorityAtr(),
                        stampAtr: this.stampAtr()
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.stampAtr(0);
                }
            }

            export class WorkTimezoneStampSetModel {
                roundingSets: RoundingSetModel[];
                prioritySets: PrioritySettingModel[];

                constructor() {
                    this.roundingSets = [];
                    this.prioritySets = [];
                    this.initPrioritySets();
                    this.initRoundingSets();
                }

                initPrioritySets() {
                    var dataPriorityModel1: PrioritySettingModel = new PrioritySettingModel(0);
                    var dataPriorityModel2: PrioritySettingModel = new PrioritySettingModel(1);
                    this.prioritySets.push(dataPriorityModel1);
                    this.prioritySets.push(dataPriorityModel2);
                }

                initRoundingSets() {
                    var dataRoundingModel1: RoundingSetModel = new RoundingSetModel(0);
                    var dataRoundingModel2: RoundingSetModel = new RoundingSetModel(1);
                    this.roundingSets.push(dataRoundingModel1);
                    this.roundingSets.push(dataRoundingModel2);
                }

                updateData(data: WorkTimezoneStampSetDto) {
                    var self = this;
                    data.roundingSets.forEach(function(dataRoundingDTO, index) {
                        //                        var dataRoundingModel: RoundingSetModel = new RoundingSetModel(index);
                        //                        dataRoundingModel.updateData(dataRoundingDTO);
                        self.roundingSets[dataRoundingDTO.section].updateData(dataRoundingDTO);
                    });

                    data.prioritySets.forEach(function(dataPriorityDTO, index) {
                        self.prioritySets[dataPriorityDTO.priorityAtr].updateData(dataPriorityDTO);
                    });
                }

                toDto(): WorkTimezoneStampSetDto {
                    var roundingSets: RoundingSetDto[] = [];
                    for (var dataRoundingModel of this.roundingSets) {
                        roundingSets.push(dataRoundingModel.toDto());
                    }
                    var prioritySets: PrioritySettingDto[] = [];
                    for (var dataPriorityModel of this.prioritySets) {
                        prioritySets.push(dataPriorityModel.toDto());
                    }
                    var dataDTO: WorkTimezoneStampSetDto = {
                        roundingSets: roundingSets,
                        prioritySets: prioritySets
                    };

                    return dataDTO;
                }
                
                resetData() {
                    this.roundingSets.forEach(function(item, index) {
                        item.resetData();
                    });

                    this.prioritySets.forEach(function(item, index) {
                        item.resetData();
                    });
                }
            }

            export class WorkTimezoneLateNightTimeSetModel {
                roundingSetting: TimeRoundingSettingModel;

                constructor() {
                    this.roundingSetting = new TimeRoundingSettingModel();
                }

                updateData(data: WorkTimezoneLateNightTimeSetDto) {
                    this.roundingSetting.updateData(data.roundingSetting);
                }

                toDto(): WorkTimezoneLateNightTimeSetDto {
                    var dataDTO: WorkTimezoneLateNightTimeSetDto = {
                        roundingSetting: this.roundingSetting.toDto()
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.roundingSetting.resetData();    
                }
            }


            export class WorkTimezoneShortTimeWorkSetModel {
                nursTimezoneWorkUse: KnockoutObservable<boolean>;
                employmentTimeDeduct: KnockoutObservable<boolean>;
                childCareWorkUse: KnockoutObservable<boolean>;

                constructor() {
                    this.nursTimezoneWorkUse = ko.observable(false);
                    this.employmentTimeDeduct = ko.observable(false);
                    this.childCareWorkUse = ko.observable(false);
                }

                updateData(data: WorkTimezoneShortTimeWorkSetDto) {
                    this.nursTimezoneWorkUse(data.nursTimezoneWorkUse);
                    this.employmentTimeDeduct(data.employmentTimeDeduct);
                    this.childCareWorkUse(data.childCareWorkUse);
                }

                toDto(): WorkTimezoneShortTimeWorkSetDto {
                    var dataDTO: WorkTimezoneShortTimeWorkSetDto = {
                        nursTimezoneWorkUse: this.nursTimezoneWorkUse(),
                        employmentTimeDeduct: this.employmentTimeDeduct(),
                        childCareWorkUse: this.childCareWorkUse()
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.nursTimezoneWorkUse(false);
                    this.employmentTimeDeduct(false);
                    this.childCareWorkUse(false);    
                }
            }


            export class HolidayFramsetModel {
                inLegalBreakoutFrameNo: KnockoutObservable<number>;
                outLegalBreakoutFrameNo: KnockoutObservable<number>;
                outLegalPubHolFrameNo: KnockoutObservable<number>;

                constructor() {
                    this.inLegalBreakoutFrameNo = ko.observable(1);
                    this.outLegalBreakoutFrameNo = ko.observable(1);
                    this.outLegalPubHolFrameNo = ko.observable(1);
                }

                updataData(data: HolidayFramsetDto) {
                    this.inLegalBreakoutFrameNo(data.inLegalBreakoutFrameNo);
                    this.outLegalBreakoutFrameNo(data.outLegalBreakoutFrameNo);
                    this.outLegalPubHolFrameNo(data.outLegalPubHolFrameNo);
                }

                toDto(): HolidayFramsetDto {
                    var dataDTO: HolidayFramsetDto = {
                        inLegalBreakoutFrameNo: this.inLegalBreakoutFrameNo(),
                        outLegalBreakoutFrameNo: this.outLegalBreakoutFrameNo(),
                        outLegalPubHolFrameNo: this.outLegalPubHolFrameNo()
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.inLegalBreakoutFrameNo(1);
                    this.outLegalBreakoutFrameNo(1);
                    this.outLegalPubHolFrameNo(1);
                }
            }

            export class ExtraordWorkOTFrameSetModel {
                otFrameNo: KnockoutObservable<number>;
                inLegalWorkFrameNo: KnockoutObservable<number>;
                settlementOrder: KnockoutObservable<number>;

                constructor() {
                    this.otFrameNo = ko.observable(1);
                    this.inLegalWorkFrameNo = ko.observable(1);
                    this.settlementOrder = ko.observable(1);
                }

                updateData(data: ExtraordWorkOTFrameSetDto) {
                    this.otFrameNo(data.otFrameNo);
                    this.inLegalWorkFrameNo(data.inLegalWorkFrameNo);
                    this.settlementOrder(data.settlementOrder);
                }

                toDto(): ExtraordWorkOTFrameSetDto {
                    var dataDTO: ExtraordWorkOTFrameSetDto = {
                        otFrameNo: this.otFrameNo(),
                        inLegalWorkFrameNo: this.inLegalWorkFrameNo(),
                        settlementOrder: this.settlementOrder()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.otFrameNo(1);
                    this.inLegalWorkFrameNo(1);
                    this.settlementOrder(1);
                }
            }

            export class WorkTimezoneExtraordTimeSetModel {
                holidayFrameSet: HolidayFramsetModel;
                timeRoundingSet: TimeRoundingSettingModel;
                otFrameSet: ExtraordWorkOTFrameSetModel;
                calculateMethod: KnockoutObservable<number>;

                constructor() {
                    this.holidayFrameSet = new HolidayFramsetModel();
                    this.timeRoundingSet = new TimeRoundingSettingModel();
                    this.otFrameSet = new ExtraordWorkOTFrameSetModel();
                    this.calculateMethod = ko.observable(0);
                }

                updateData(data: WorkTimezoneExtraordTimeSetDto) {
                    this.holidayFrameSet.updataData(data.holidayFrameSet);
                    this.timeRoundingSet.updateData(data.timeRoundingSet);
                    this.otFrameSet.updateData(data.otFrameSet);
                    this.calculateMethod(data.calculateMethod);
                }

                toDto(): WorkTimezoneExtraordTimeSetDto {
                    var dataDTO: WorkTimezoneExtraordTimeSetDto = {
                        holidayFrameSet: this.holidayFrameSet.toDto(),
                        timeRoundingSet: this.timeRoundingSet.toDto(),
                        otFrameSet: this.otFrameSet.toDto(),
                        calculateMethod: this.calculateMethod()
                    };
                    return dataDTO;
                }
                
                resetData() {
                    this.holidayFrameSet.resetData();
                    this.timeRoundingSet.resetData();
                    this.otFrameSet.resetData();
                    this.calculateMethod(0);
                }
            }

            export class EmTimezoneLateEarlyCommonSetModel {
                delFromEmTime: KnockoutObservable<boolean>;

                constructor() {
                    this.delFromEmTime = ko.observable(false);
                }

                updateData(data: EmTimezoneLateEarlyCommonSetDto) {
                    this.delFromEmTime(data.delFromEmTime);
                }

                toDto(): EmTimezoneLateEarlyCommonSetDto {
                    var dataDTO: EmTimezoneLateEarlyCommonSetDto = {
                        delFromEmTime: this.delFromEmTime()
                    };
                    return dataDTO;
                }

            }

            export class GraceTimeSettingModel {
                includeWorkingHour: KnockoutObservable<boolean>;
                graceTime: KnockoutObservable<number>;

                constructor() {
                    this.includeWorkingHour = ko.observable(false);
                    this.graceTime = ko.observable(0);
                }

                updateData(data: GraceTimeSettingDto) {
                    this.includeWorkingHour(data.includeWorkingHour);
                    this.graceTime(data.graceTime);
                }

                toDto(): GraceTimeSettingDto {
                    var dataDTO: GraceTimeSettingDto = {
                        includeWorkingHour: this.includeWorkingHour(),
                        graceTime: this.graceTime()
                    };
                    return dataDTO;
                }
            }

            export class OtherEmTimezoneLateEarlySetModel {
                delTimeRoundingSet: TimeRoundingSettingModel;
                stampExactlyTimeIsLateEarly: KnockoutObservable<boolean>;
                graceTimeSet: GraceTimeSettingModel;
                recordTimeRoundingSet: TimeRoundingSettingModel;
                lateEarlyAtr: KnockoutObservable<number>;

                constructor() {
                    this.delTimeRoundingSet = new TimeRoundingSettingModel();
                    this.stampExactlyTimeIsLateEarly = ko.observable(false);
                    this.graceTimeSet = new GraceTimeSettingModel();
                    this.recordTimeRoundingSet = new TimeRoundingSettingModel();
                    this.lateEarlyAtr = ko.observable(0);
                }

                static getDefaultData(): Array<OtherEmTimezoneLateEarlySetModel> {
                    let lateSet = new OtherEmTimezoneLateEarlySetModel();
                    lateSet.lateEarlyAtr(LateEarlyAtr.LATE);
                    let leaveEarlySet = new OtherEmTimezoneLateEarlySetModel();
                    leaveEarlySet.lateEarlyAtr(LateEarlyAtr.EARLY);
                    let list: OtherEmTimezoneLateEarlySetModel[] = [];
                    list.push(lateSet);
                    list.push(leaveEarlySet);
                    return list;
                }

                updateData(data: OtherEmTimezoneLateEarlySetDto) {
                    this.delTimeRoundingSet.updateData(data.delTimeRoundingSet);
                    this.stampExactlyTimeIsLateEarly(data.stampExactlyTimeIsLateEarly);
                    this.graceTimeSet.updateData(data.graceTimeSet);
                    this.recordTimeRoundingSet.updateData(data.recordTimeRoundingSet);
                    this.lateEarlyAtr(data.lateEarlyAtr);
                }

                toDto(): OtherEmTimezoneLateEarlySetDto {
                    var dataDTO: OtherEmTimezoneLateEarlySetDto = {
                        delTimeRoundingSet: this.delTimeRoundingSet.toDto(),
                        stampExactlyTimeIsLateEarly: this.stampExactlyTimeIsLateEarly(),
                        graceTimeSet: this.graceTimeSet.toDto(),
                        recordTimeRoundingSet: this.recordTimeRoundingSet.toDto(),
                        lateEarlyAtr: this.lateEarlyAtr()
                    };

                    return dataDTO;
                }
            }

            export class WorkTimezoneLateEarlySetModel {
                commonSet: EmTimezoneLateEarlyCommonSetModel;
                otherClassSets: OtherEmTimezoneLateEarlySetModel[];

                constructor() {
                    this.commonSet = new EmTimezoneLateEarlyCommonSetModel();
                    this.otherClassSets = OtherEmTimezoneLateEarlySetModel.getDefaultData();
                }

                updateData(data: WorkTimezoneLateEarlySetDto) {
                    if (data) {
                        this.commonSet.updateData(data.commonSet);

                        let newOtherClassSets: OtherEmTimezoneLateEarlySetModel[] = _.map(data.otherClassSets, (dataDTO) => {
                            let dataModel: OtherEmTimezoneLateEarlySetModel = new OtherEmTimezoneLateEarlySetModel();
                            dataModel.updateData(dataDTO);
                            return dataModel;
                        });

                        let newLateSet = _.find(newOtherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.LATE);
                        let lateSet = _.find(this.otherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.LATE);
                        if (nts.uk.util.isNullOrUndefined(newLateSet)) {
                            lateSet.updateData(WorkTimezoneLateEarlySetModel.getDefaultOtherSet(LateEarlyAtr.LATE).toDto());
                        } else {
                            lateSet.updateData(newLateSet.toDto());
                        }
                        let newLeaveEarlySet = _.find(newOtherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.EARLY);
                        let leaveEarlySet = _.find(this.otherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.EARLY);
                        if (nts.uk.util.isNullOrUndefined(newLeaveEarlySet)) {
                            leaveEarlySet.updateData(WorkTimezoneLateEarlySetModel.getDefaultOtherSet(LateEarlyAtr.EARLY).toDto());
                        } else {
                            leaveEarlySet.updateData(newLeaveEarlySet.toDto());
                        }
                    }
                }

                toDto(): WorkTimezoneLateEarlySetDto {
                    let otherClassSets: OtherEmTimezoneLateEarlySetDto[] = _.map(this.otherClassSets, (dataModel) => dataModel.toDto());
                    var dataDTO: WorkTimezoneLateEarlySetDto = {
                        commonSet: this.commonSet.toDto(),
                        otherClassSets: otherClassSets
                    };
                    return dataDTO;
                }

                resetData() {
                    //this.commonSet.resetData();
                    let lateSet = _.find(this.otherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.LATE);
                    lateSet.updateData(WorkTimezoneLateEarlySetModel.getDefaultOtherSet(LateEarlyAtr.LATE).toDto());
                    let leaveEarlySet = _.find(this.otherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.EARLY);
                    leaveEarlySet.updateData(WorkTimezoneLateEarlySetModel.getDefaultOtherSet(LateEarlyAtr.EARLY).toDto());
                }

                public static getDefaultOtherSet(lateEarlyAtr: LateEarlyAtr): OtherEmTimezoneLateEarlySetModel {
                    let defaultObj = new OtherEmTimezoneLateEarlySetModel();
                    defaultObj.lateEarlyAtr(lateEarlyAtr);
                    return defaultObj;
                }

                public getLateSet(): OtherEmTimezoneLateEarlySetModel {
                    let self = this;
                    return _.find(self.otherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.LATE);
                }
                public getLeaveEarlySet(): OtherEmTimezoneLateEarlySetModel {
                    let self = this;
                    return _.find(self.otherClassSets, o => o.lateEarlyAtr() == LateEarlyAtr.EARLY);
                }
            }

            export class EmTimeZoneSetModel {
                employmentTimeFrameNo: KnockoutObservable<number>;
                timezone: TimeZoneRoundingModel;

                constructor() {
                    this.employmentTimeFrameNo = ko.observable(0);
                    this.timezone = new TimeZoneRoundingModel();
                }

                updateData(data: EmTimeZoneSetDto) {
                    this.employmentTimeFrameNo(data.employmentTimeFrameNo);
                    this.timezone.updateData(data.timezone);
                }

                toDto(): EmTimeZoneSetDto {
                    var dataDTO: EmTimeZoneSetDto = {
                        employmentTimeFrameNo: this.employmentTimeFrameNo(),
                        timezone: this.timezone.toDto()
                    };
                    return dataDTO;
                }
            }

            export class OverTimeOfTimeZoneSetModel {
                workTimezoneNo: KnockoutObservable<number>;
                restraintTimeUse: KnockoutObservable<boolean>;
                earlyOTUse: KnockoutObservable<boolean>;
                timezone: TimeZoneRoundingModel;
                otFrameNo: KnockoutObservable<number>;
                legalOTframeNo: KnockoutObservable<number>;
                settlementOrder: KnockoutObservable<number>;

                constructor() {
                    this.workTimezoneNo = ko.observable(0);
                    this.restraintTimeUse = ko.observable(false);
                    this.earlyOTUse = ko.observable(false);
                    this.timezone = new TimeZoneRoundingModel();
                    this.otFrameNo = ko.observable(0);
                    this.legalOTframeNo = ko.observable(0);
                    this.settlementOrder = ko.observable(0);
                }

                updateData(data: OverTimeOfTimeZoneSetDto) {
                    this.workTimezoneNo(data.workTimezoneNo);
                    this.restraintTimeUse(data.restraintTimeUse);
                    this.earlyOTUse(data.earlyOTUse);
                    this.timezone.updateData(data.timezone);
                    this.otFrameNo(data.otFrameNo);
                    this.legalOTframeNo(data.legalOTframeNo);
                    this.settlementOrder(data.settlementOrder);
                }

                toDto(): OverTimeOfTimeZoneSetDto {
                    var dataDTO: OverTimeOfTimeZoneSetDto = {
                        workTimezoneNo: this.workTimezoneNo(),
                        restraintTimeUse: this.restraintTimeUse(),
                        earlyOTUse: this.earlyOTUse(),
                        timezone: this.timezone.toDto(),
                        otFrameNo: this.otFrameNo(),
                        legalOTframeNo: this.legalOTframeNo(),
                        settlementOrder: this.settlementOrder()
                    };
                    return dataDTO;
                }
            }

            export class StampReflectTimezoneModel {
                workNo: KnockoutObservable<number>;
                classification: KnockoutObservable<number>;
                endTime: KnockoutObservable<number>;
                startTime: KnockoutObservable<number>;

                constructor() {
                    this.workNo = ko.observable(0);
                    this.classification = ko.observable(0);
                    this.endTime = ko.observable(0);
                    this.startTime = ko.observable(0);
                }

                updateData(data: StampReflectTimezoneDto) {
                    this.workNo(data.workNo);
                    this.classification(data.classification);
                    this.endTime(data.endTime);
                    this.startTime(data.startTime);
                }

                toDto(): StampReflectTimezoneDto {
                    var dataDTO: StampReflectTimezoneDto = {
                        workNo: this.workNo(),
                        classification: this.workNo(),
                        endTime: this.endTime(),
                        startTime: this.startTime()
                    };
                    return dataDTO;
                }
            }

            export class FixedWorkRestSetModel {
                commonRestSet: CommonRestSettingModel;
                fixedRestCalculateMethod: KnockoutObservable<number>;

                constructor() {
                    this.commonRestSet = new CommonRestSettingModel();
                    this.fixedRestCalculateMethod = ko.observable(0);
                }

                updateData(data: FixedWorkRestSetDto) {
                    this.commonRestSet.updateData(data.commonRestSet);
                    this.fixedRestCalculateMethod(data.fixedRestCalculateMethod);
                }

                toDto(): FixedWorkRestSetDto {
                    let dataDTO: FixedWorkRestSetDto = {
                        commonRestSet: this.commonRestSet.toDto(),
                        fixedRestCalculateMethod: this.fixedRestCalculateMethod()
                    };
                    return dataDTO;
                }

                resetData() {
                    this.commonRestSet.resetData();
                    this.fixedRestCalculateMethod(0);
                }
            }

            export class FixedWorkTimezoneSetModel {
                lstWorkingTimezone: KnockoutObservableArray<EmTimeZoneSetModel>;
                lstOTTimezone: KnockoutObservableArray<OverTimeOfTimeZoneSetModel>;

                constructor() {
                    this.lstWorkingTimezone = ko.observableArray([]);
                    this.lstOTTimezone = ko.observableArray([]);
                }

                updateData(data: FixedWorkTimezoneSetDto) {
                    this.updateWorkingTimezone(data.lstWorkingTimezone);
                    this.updateOvertimeZone(data.lstOTTimezone);
                }

                updateOvertimeZone(lstOTTimezone: OverTimeOfTimeZoneSetDto[]) {
                    this.lstOTTimezone([]);
                    var dataModelTimezone: OverTimeOfTimeZoneSetModel[] = [];
                    for (var dataDTO of lstOTTimezone) {
                        var dataModel: OverTimeOfTimeZoneSetModel = new OverTimeOfTimeZoneSetModel();
                        dataModel.updateData(dataDTO);
                        dataModelTimezone.push(dataModel);
                    }
                    this.lstOTTimezone(dataModelTimezone);
                }


                updateWorkingTimezone(lstWorkingTimezone: EmTimeZoneSetDto[]) {
                    let self = this;
                    let updatedList = lstWorkingTimezone.map(item => {
                        let m = new EmTimeZoneSetModel();
                        m.updateData(item);
                        return m;
                    });
                    self.lstWorkingTimezone(updatedList);
                }

                getWorkingTimezoneByEmploymentTimeFrameNo(employmentTimeFrameNo: number) {
                    return _.find(this.lstWorkingTimezone(), workingtimezone => workingtimezone.employmentTimeFrameNo() == employmentTimeFrameNo);
                }
                toDto(): FixedWorkTimezoneSetDto {
                    let lstWorkingTimezone: EmTimeZoneSetDto[] = _.map(this.lstWorkingTimezone(), (dataModel) => dataModel.toDto());
                    let lstOTTimezone: OverTimeOfTimeZoneSetDto[] = _.map(this.lstOTTimezone(), (dataModel) => dataModel.toDto());

                    let dataDTO: FixedWorkTimezoneSetDto = {
                        lstWorkingTimezone: lstWorkingTimezone,
                        lstOTTimezone: lstOTTimezone
                    };
                    return dataDTO;
                }

                resetData() {
                    this.lstWorkingTimezone([]);
                    this.lstOTTimezone([]);
                }
            }

        }
    }
}