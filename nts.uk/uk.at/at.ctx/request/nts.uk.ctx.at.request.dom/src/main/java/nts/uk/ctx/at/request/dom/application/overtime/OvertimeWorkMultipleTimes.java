package nts.uk.ctx.at.request.dom.application.overtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.request.dom.adapter.CalculationParams;
import nts.uk.ctx.at.request.dom.application.AppReason;
import nts.uk.ctx.at.request.dom.setting.company.appreasonstandard.AppStandardReasonCode;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.common.TimeZoneWithWorkNo;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.affiliationinfor.AffiliationInforOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.WorkTimes;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakgoout.BreakFrameNo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.calcategory.CalAttrOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeActualStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.CalculationState;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.outsideworktime.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.com.time.TimeZone;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UKDesign.?????????????????????.NittsuSystem.UniversalK.??????.contexts.????????????.??????.??????????????????.????????????.?????????????????????
 * ?????????????????????
 */
@Getter
@AllArgsConstructor
public class OvertimeWorkMultipleTimes {
    // ???????????????
    private List<OvertimeHour> overtimeHours;

    // ????????????
    private List<OvertimeReason> overtimeReasons;

    /**
     * ????????????
     * @param overtimeHours List<???????????????>
     * @param overtimeReasons List<????????????>
     * @return ?????????????????????
     */
    public static OvertimeWorkMultipleTimes create(List<OvertimeHour> overtimeHours, List<OvertimeReason> overtimeReasons) {
        overtimeHours.sort(Comparator.comparing(i -> i.getOvertimeHours().getStart()));
        for (int i = 0; i < overtimeHours.size() - 1; i++) {
            if (overtimeHours.get(i).getOvertimeHours().getDuplicatedWith(overtimeHours.get(i + 1).getOvertimeHours()).isPresent()) {
                throw new BusinessException("Msg_3238");
            }
        }

        List<OvertimeHour> newOvertimeHours = new ArrayList<>();
        List<OvertimeReason> newOvertimeReasons = new ArrayList<>();
        for (int i = 0; i < overtimeHours.size(); i++) {
            OvertimeHour overtimeHour = overtimeHours.get(i);
            OvertimeNumber overtimeNumber = new OvertimeNumber(i + 1);
            newOvertimeHours.add(new OvertimeHour(overtimeNumber, overtimeHour.getOvertimeHours()));
            overtimeReasons.stream().filter(r -> r.getOvertimeNumber().equals(overtimeHour.getOvertimeNumber())).findFirst().ifPresent(reason -> {
                newOvertimeReasons.add(new OvertimeReason(overtimeNumber, reason.getFixedReasonCode(), reason.getApplyReason()));
            });
        }
        return new OvertimeWorkMultipleTimes(newOvertimeHours, newOvertimeReasons);
    }

    /**
     * ????????????????????????????????????
     * @return Optional<???????????????????????????>
     */
    public Optional<AppStandardReasonCode> createFixedReason() {
        if (overtimeReasons.isEmpty()) return Optional.empty();
        return overtimeReasons.get(0).getFixedReasonCode();
    }

    /**
     * ???????????????????????????
     * @return Optional<????????????>
     */
    public Optional<AppReason> createApplyReason() {
        List<String> appReasons = overtimeReasons.stream().filter(r -> r.getApplyReason().isPresent()).map(r -> r.getApplyReason().get().v()).collect(Collectors.toList());
        if (appReasons.isEmpty()) return Optional.empty();
        return Optional.of(new AppReason(StringUtil.cutOffAsLengthHalf(StringUtils.join(appReasons, "/"), 400)));
    }

    /**
     * ????????????????????????????????????????????????????????????
     * @param timeZones List<?????????(??????NO??????)>
     * @return List<?????????(??????NO??????)>
     */
    public List<TimeZoneWithWorkNo> getWorkingHoursToCalculateOvertime(List<TimeZoneWithWorkNo> timeZones) {
        if (CollectionUtil.isEmpty(timeZones) || CollectionUtil.isEmpty(this.overtimeHours))
            return timeZones;
        timeZones.sort(Comparator.comparing(TimeZoneWithWorkNo::getWorkNo));
        TimeZoneWithWorkNo zone1 = timeZones.get(0);
        TimeZoneWithWorkNo zone2 = timeZones.size() > 1 ? timeZones.get(1) : null;
        TimeWithDayAttr startTime = this.overtimeHours.get(0).getOvertimeHours().getStart();
        TimeWithDayAttr endTime = this.overtimeHours.get(this.overtimeHours.size() - 1).getOvertimeHours().getEnd();
        if (zone1.getTimeZone().getStartTime().greaterThan(startTime)) zone1.getTimeZone().setStartTime(startTime);
        if (zone2 == null) {
            if (zone1.getTimeZone().getEndTime().lessThan(endTime))
                zone1.getTimeZone().setEndTime(endTime);
            return Collections.singletonList(zone1);
        }
        TimeWithDayAttr end1 = overtimeHours.stream()
                .filter(i -> i.getOvertimeHours().getEnd().lessThanOrEqualTo(zone2.getTimeZone().getStartTime()))
                .reduce((first, second) -> second)
                .map(i -> i.getOvertimeHours().getEnd()).orElse(null);
        if (end1 != null) {
            if (zone1.getTimeZone().getEndTime().lessThan(end1))
                zone1.getTimeZone().setEndTime(end1);
            TimeWithDayAttr start2 = overtimeHours.stream()
                    .filter(i -> i.getOvertimeHours().getStart().greaterThanOrEqualTo(end1))
                    .findFirst()
                    .map(i -> i.getOvertimeHours().getStart()).orElse(null);
            if (start2 != null) {
                if (zone2.getTimeZone().getStartTime().greaterThan(start2))
                    zone2.getTimeZone().setStartTime(start2);
            }
        }
        if (zone2.getTimeZone().getEndTime().lessThan(endTime))
            zone2.getTimeZone().setEndTime(endTime);
        return Arrays.asList(zone1, zone2);
    }

    /**
     * ???????????????????????????????????????????????????????????????
     * @param require
     * @param companyId ??????ID
     * @param employeeId ?????????ID
     * @param date ?????????
     * @param workInfo ????????????
     * @param workingHours ????????????List
     * @param breakTimes ???????????????List
     * @return ???????????????List
     */
    public List<BreakTimeSheet> getBreakTimeToCalculateOvertime(Require require,
                                                                String companyId,
                                                                String employeeId,
                                                                GeneralDate date,
                                                                WorkInformation workInfo,
                                                                List<TimeZoneWithWorkNo> workingHours,
                                                                List<BreakTimeSheet> breakTimes,
                                                                boolean managementMultipleWorkCycles) {
        List<TimeZoneWithWorkNo> predeterminedWorkingHours = workingHours;
        if (workInfo.getWorkTimeCodeNotNull().isPresent()) {
            Optional<PredetemineTimeSetting> predTimeSet = require.getPredetemineTimeSetting(companyId, workInfo.getWorkTimeCode().v());
            if (predTimeSet.isPresent()) {
                predeterminedWorkingHours = predTimeSet.get().getPrescribedTimezoneSetting().getLstTimezone()
                        .stream().filter(i -> i.isUsed() && (i.getWorkNo() == 1 || managementMultipleWorkCycles))
                        .map(i -> new TimeZoneWithWorkNo(i.getWorkNo(), i.getStart().v(), i.getEnd().v()))
                        .collect(Collectors.toList());
            }
            Optional<WorkTimeSetting> workTimeSetting = require.getWorkTimeSetting(companyId, workInfo.getWorkTimeCode().v());
            if (workTimeSetting.isPresent() && workTimeSetting.get().getWorkTimeDivision().getWorkTimeMethodSet() == WorkTimeMethodSet.FLOW_WORK) {
                breakTimes = this.getFlowWorkBreakTime(require, employeeId, date, workInfo, predTimeSet, workingHours);
                predeterminedWorkingHours.get(0).getTimeZone().setEndTime(this.getFlowWorkEndTime(require, employeeId, date, workInfo, predTimeSet, workingHours));
            }
        }
        List<BreakTimeSheet> result = this.calculateNewBreakTimes(breakTimes, predeterminedWorkingHours, workingHours);
        if (result.size() > 10) throw new BusinessException("Msg_3236");
        return result;
    }

    /**
     * [prv-1]??????????????? ??????????????????????????????
     * @param require
     * @param employeeId ?????????ID
     * @param date ?????????
     * @param workInfo ????????????
     * @param predTimeSet ??????????????????
     * @param workingHours ????????????List
     * @return ???????????????List
     */
    private List<BreakTimeSheet> getFlowWorkBreakTime(Require require,
                                                      String employeeId,
                                                      GeneralDate date,
                                                      WorkInformation workInfo,
                                                      Optional<PredetemineTimeSetting> predTimeSet,
                                                      List<TimeZoneWithWorkNo> workingHours) {
        WorkInfoOfDailyAttendance workInformation = new WorkInfoOfDailyAttendance(
                workInfo,
                CalculationState.No_Calculated,
                NotUseAttribute.Not_use,
                NotUseAttribute.Not_use,
                DayOfWeek.MONDAY,
                new ArrayList<>(),
                Optional.empty()
        );
        CalAttrOfDailyAttd calCategory = CalAttrOfDailyAttd.defaultData();
        AffiliationInforOfDailyAttd affiliationInforOfDailyPerfor = new AffiliationInforOfDailyAttd(
                null,
                null,
                null,
                null,
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        List<TimeZoneWithWorkNo> tmpWorkingHours = new ArrayList<>();
        workingHours.forEach(wh -> {
            if (wh.getWorkNo().v() == workingHours.size() && predTimeSet.isPresent()) {
                tmpWorkingHours.add(new TimeZoneWithWorkNo(
                        wh.getWorkNo().v(),
                        wh.getTimeZone().getStartTime().v(),
                        predTimeSet.get().getEndDateClock().v()
                ));
            } else {
                tmpWorkingHours.add(wh);
            }
        });

        TimeLeavingOfDailyAttd attendanceLeave = new TimeLeavingOfDailyAttd(
                tmpWorkingHours.stream().map(i -> new TimeLeavingWork(
                        i.getWorkNo(),
                        new TimeActualStamp(
                                WorkStamp.createByAutomaticSet(i.getTimeZone().getStartTime()),
                                WorkStamp.createByAutomaticSet(i.getTimeZone().getStartTime()),
                                1
                        ),
                        new TimeActualStamp(
                                WorkStamp.createByAutomaticSet(i.getTimeZone().getEndTime()),
                                WorkStamp.createByAutomaticSet(i.getTimeZone().getEndTime()),
                                1
                        )
                )).collect(Collectors.toList()),
                new WorkTimes(1)
        );

        IntegrationOfDaily dailyAttendance = new IntegrationOfDaily(
                employeeId,
                date,
                workInformation,
                calCategory,
                affiliationInforOfDailyPerfor,
                Optional.empty(),
                new ArrayList<>(),
                Optional.empty(),
                new BreakTimeOfDailyAttd(),
                Optional.empty(),
                Optional.of(attendanceLeave),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                new ArrayList<>(),
                Optional.empty(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                Optional.empty()
        );
        ChangeDailyAttendance changeAtt = new ChangeDailyAttendance(
                false,
                false,
                true,
                false,
                ScheduleRecordClassifi.RECORD,
                false
        );
        return new ArrayList<>(require.process(dailyAttendance, changeAtt).getBreakTime().getBreakTimeSheets());
    }

    /**
     * [prv-2]???????????????New???????????????
     * @param breakTimes ???????????????List
     * @param predeterminedWorkingHours ????????????List????????????
     * @param workingHours ????????????List???????????????
     * @return ???????????????List
     */
    private List<BreakTimeSheet> calculateNewBreakTimes(List<BreakTimeSheet> breakTimes, List<TimeZoneWithWorkNo> predeterminedWorkingHours, List<TimeZoneWithWorkNo> workingHours) {
        // ??????????????????List(??????)
        // ?????????????????????????????????????????????????????????????????????????????????????????????
        // ?????????????????????????????????????????????????????????????????????????????????
        // ????????????????????????????????????????????????????????????
        // ??????????????????(????????????????????????????????????????????????????????????????????????
        // ???????????????(?????????????????????????????????????????????????????????????????????????????????????????????

        List<TimeSpanForCalc> timeLine = new ArrayList<>();
        timeLine.addAll(predeterminedWorkingHours.stream().map(i -> new TimeSpanForCalc(i.getTimeZone().getStartTime(), i.getTimeZone().getEndTime())).collect(Collectors.toList()));
        this.overtimeHours.forEach(ot -> {
            if (timeLine.stream().noneMatch(t -> t.contains(ot.getOvertimeHours()))) {
                timeLine.add(ot.getOvertimeHours());
            }
        });
        timeLine.sort(Comparator.comparing(TimeSpanForCalc::getStart));

        if (!timeLine.isEmpty()) {
            new TimeSpanForCalc(timeLine.get(0).getStart(), timeLine.get(timeLine.size() - 1).getEnd())
                    .getNotDuplicatedWith(timeLine).forEach(bt -> {
                breakTimes.add(new BreakTimeSheet(new BreakFrameNo(1), bt.getStart(), bt.getEnd()));
            });
        }

        TimeZone workingTime = new TimeZone(null, null);
        workingHours.forEach(wh -> {
            if (workingTime.getStartTime() == null || workingTime.getStartTime().greaterThan(wh.getTimeZone().getStartTime())) {
                workingTime.setStartTime(wh.getTimeZone().getStartTime());
            }
            if (workingTime.getEndTime() == null || workingTime.getEndTime().lessThan(wh.getTimeZone().getEndTime())) {
                workingTime.setEndTime(wh.getTimeZone().getEndTime());
            }
        });
        new TimeSpanForCalc(workingTime.getStartTime(), workingTime.getEndTime())
                .getNotDuplicatedWith(timeLine).forEach(bt -> {
            breakTimes.add(new BreakTimeSheet(new BreakFrameNo(1), bt.getStart(), bt.getEnd()));
        });

        breakTimes.sort(Comparator.comparing(BreakTimeSheet::getStartTime));

        List<BreakTimeSheet> result = new ArrayList<>();
        for (int i = 0; i < breakTimes.size(); i++) {
            if (!result.isEmpty()) {
                if (result.get(result.size() - 1).convertToTimeSpanForCalc().contains(breakTimes.get(i).convertToTimeSpanForCalc())) {
                    continue;
                } else if (breakTimes.get(i).convertToTimeSpanForCalc().contains(result.get(result.size() - 1).convertToTimeSpanForCalc())) {
                    result.get(result.size() - 1).setStartTime(breakTimes.get(i).getStartTime());
                    result.get(result.size() - 1).setEndTime(breakTimes.get(i).getEndTime());
                    continue;
                } else if (result.get(result.size() - 1).convertToTimeSpanForCalc().contains(breakTimes.get(i).getStartTime())) {
                    result.get(result.size() - 1).setEndTime(breakTimes.get(i).getEndTime());
                    continue;
                } else if (result.get(result.size() - 1).convertToTimeSpanForCalc().contains(breakTimes.get(i).getEndTime())) {
                    result.get(result.size() - 1).setStartTime(breakTimes.get(i).getStartTime());
                    continue;
                }
            }
            result.add(new BreakTimeSheet(new BreakFrameNo(result.size() + 1), breakTimes.get(i).getStartTime(), breakTimes.get(i).getEndTime()));
        }

        return result;
    }

    /**
     * [prv-6]?????????????????????????????????????????????????????????
     * @param require Require
     * @param employeeId ?????????ID
     * @param date ?????????
     * @param workInformation ????????????
     * @param predTimeSet ??????????????????
     * @param workingHours ????????????List
     * @return ????????????
     */
    private TimeWithDayAttr getFlowWorkEndTime(Require require, String employeeId, GeneralDate date, WorkInformation workInformation, Optional<PredetemineTimeSetting> predTimeSet, List<TimeZoneWithWorkNo> workingHours) {
        List<TimeZoneWithWorkNo> tmpWorkingHours = new ArrayList<>();
        workingHours.forEach(wh -> {
            if (wh.getWorkNo().v() == workingHours.size() && predTimeSet.isPresent()) {
                tmpWorkingHours.add(new TimeZoneWithWorkNo(
                        wh.getWorkNo().v(),
                        wh.getTimeZone().getStartTime().v(),
                        predTimeSet.get().getEndDateClock().v()
                ));
            } else {
                tmpWorkingHours.add(wh);
            }
        });

        CalculationParams params = new CalculationParams(
                employeeId,
                date,
                workInformation.getWorkTypeCode(),
                workInformation.getWorkTimeCode(),
                tmpWorkingHours,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
        IntegrationOfDaily calcResult = require.tempCalculateOneDayAttendanceTime(params);
        if (!calcResult.getAttendanceTimeOfDailyPerformance().isPresent()
                || !calcResult.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
            return workingHours.get(workingHours.size() - 1).getTimeZone().getEndTime();
        }
        List<OverTimeFrameTimeSheet> overTimeWorkFrameTimeSheet = calcResult
                .getAttendanceTimeOfDailyPerformance().get()
                .getActualWorkingTimeOfDaily()
                .getTotalWorkingTime()
                .getExcessOfStatutoryTimeOfDaily()
                .getOverTimeWork().get()
                .getOverTimeWorkFrameTimeSheet();
        TimeWithDayAttr minOvertimeStart = overTimeWorkFrameTimeSheet.stream().map(i -> i.getTimeSpan().getStart()).sorted().findFirst().orElse(workingHours.get(workingHours.size() - 1).getTimeZone().getEndTime());
        return minOvertimeStart;
    }

    public interface Require {
        /**
         * ???????????????????????????????????????
         */
        Optional<WorkTimeSetting> getWorkTimeSetting(String companyId, String code);

        /**
         * 1?????????????????????????????????
         */
        IntegrationOfDaily tempCalculateOneDayAttendanceTime(CalculationParams params);

        /**
         * ?????????????????????????????????
         */
        Optional<PredetemineTimeSetting> getPredetemineTimeSetting(String companyId, String workTimeCode);

        IntegrationOfDaily process(IntegrationOfDaily domainDaily, ChangeDailyAttendance changeAtt);
    }
}
