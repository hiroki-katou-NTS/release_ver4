package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.BreakdownTimeDay;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalculationOfOverTimeWork;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.worktime.commonsetting.overworkset.StatutoryOverTimeWorkSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.FixOffdayWorkTime;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.OverTimeHourSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedworkset.WorkTimeCommonSet;
import nts.uk.ctx.at.shared.dom.worktime_old.WorkTime;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 就業時間外時間帯
 * 
 * @author keisuke_hoshina
 *
 */
@Getter
@AllArgsConstructor
public class OutsideWorkTimeSheet {
	private ExcessOfStatutoryTimeOfDaily excessOfStatutoryTimeOfDaily;

	private Optional<OverTimeSheet> overTimeWorkSheet;

	private Optional<HolidayWorkTimeSheet> holidayWorkTimeSheet;

	/**
	 * 就業時間外時間帯を作成する
	 * 
	 * @param overTimeHourSetList
	 * @param fixOff
	 * @param attendanceLeave
	 * @param workNo
	 * @param dayEndSet
	 * @param overDayEndSet
	 * @param holidayTimeWorkItem
	 * @param beforeDay
	 * @param toDay
	 * @param afterDay
	 * @param workTime
	 * @param workingSystem
	 * @param breakdownTimeDay
	 * @param dailyTime
	 * @param autoCalculationSet
	 * @param statutorySet
	 * @param prioritySet
	 * @return
	 */
	public static void createOutsideWorkTimeSheet(List<OverTimeHourSet> overTimeHourSetList,
			FixOffdayWorkTime fixOff, TimeLeavingWork attendanceLeave, int workNo, OverDayEndCalcSet dayEndSet,
			WorkTimeCommonSet overDayEndSet, List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem, WorkType beforeDay,
			WorkType toDay, WorkType afterDay, WorkTime workTime, WorkingSystem workingSystem,
			BreakdownTimeDay breakdownTimeDay, DailyTime dailyTime, AutoCalculationOfOverTimeWork autoCalculationSet,
			StatutoryOverTimeWorkSet statutorySet, StatutoryPrioritySet prioritySet) {
		Optional<OverTimeSheet> overTimeWorkSheet;
		Optional<HolidayWorkTimeSheet> holidayWorkTimeSheet;
		if (toDay.isWeekDayAttendance()) {
			/* 就業時間外時間帯の平日出勤の処理 */
//			List<OverTimeFrameTimeSheet> overTimeWorkFrameTimeSheet = OverTimeFrameTimeSheet.createOverWorkFrame(
//					overTimeHourSetList, workingSystem, attendanceLeave, workNo, breakdownTimeDay, dailyTime,
//					autoCalculationSet, statutorySet, prioritySet);

			/* 0時跨ぎ処理 */
			OverDayEnd processOverDayEnd = new OverDayEnd();
//			OverDayEnd.SplitOverTimeWork process = processOverDayEnd.new SplitOverTimeWork(dayEndSet, overDayEndSet,
//					overTimeWorkFrameTimeSheet, beforeDay, toDay, afterDay);
//			if (process.getHolList().size() > 0) {
//				/* 日別実績の休日出勤時間 作成 */
//				HolidayWorkTimeOfDaily holidayWorkTimeOfDaily = new HolidayWorkTimeOfDaily(process.getHolList(),
//						Collections.emptyList(), Finally.empty());
//				/* 休日出勤時間帯 作成 */
//				holidayWorkTimeSheet = Optional.of(new HolidayWorkTimeSheet(holidayWorkTimeOfDaily));
//			} else {
//				holidayWorkTimeSheet = Optional.empty();
//			}

		} else {
			/* 休日出勤 */
			List<HolidayWorkFrameTimeSheet> outsideWorkTimeSheet = new ArrayList<>();//new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(1), new TimeSpanForCalc(new TimeWithDayAttr(0),new TimeWithDayAttr(0))); 
//					HolidayWorkFrameTimeSheet.getHolidayWorkTimeOfDaily(
//					fixOff.getWorkTimezone(), attendanceLeave, dayEndSet, overDayEndSet, holidayTimeWorkItem, beforeDay,
//					toDay, afterDay);

			/* 0時跨ぎ */
			OverDayEnd overEnd = new OverDayEnd();
//			OverDayEnd.SplitHolidayWorkTime process = overEnd.new SplitHolidayWorkTime(dayEndSet, overDayEndSet,
//					holidayTimeWorkItem, beforeDay, toDay, afterDay);
//			/* 日別実績の残業時間 作成 */
//			if (process.getDedList().size() > 0) {
//				OverTimeOfDaily overTimeWorkOfDaily = new OverTimeOfDaily(process.getDedList(), Collections.emptyList(),
//						Finally.empty());
//				/* 残業時間帯 作成 */
//				overTimeWorkSheet = Optional.of(new OverTimeSheet(overTimeWorkOfDaily));
//			} else {
//				overTimeWorkSheet = Optional.empty();
//			}

		}

//		return new OutsideWorkTimeSheet(new ExcessOfStatutoryTimeOfDaily(new ExcessOfStatutoryMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0)) ,Optional.of(overTimeWorkSheet.get().getOverWorkTimeOfDaily()),Optional.of(holidayWorkTimeSheet.get().getWorkHolidayTime()))
//				   ,overTimeWorkSheet
//				   ,holidayWorkTimeSheet);

	}

	/**
	 * 法定外深夜時間の計算
	 */
	// public ExcessOfStatutoryTimeOfDaily
	// calcMidNightTimeIncludeExcessWorkTime(Optional<OverTimeWorkSheet>
	// overTimeWorkSheet,Optional<HolidayWorkTimeSheet> holidayWorkSheet
	// ,AutoCalculationOfOverTimeWork
	// autoCalculationOfOverTimeWork,AutoCalcSetOfHolidayWorkTime
	// autoCalcSetOfHolidayWorkTime) {
	public void calcMidNightTimeIncludeExcessWorkTime(Optional<OverTimeSheet> overTimeWorkSheet,
			Optional<HolidayWorkTimeSheet> holidayWorkSheet,
			AutoCalculationOfOverTimeWork autoCalculationOfOverTimeWork,
			AutoCalcSetOfHolidayWorkTime autoCalcSetOfHolidayWorkTime) {
		Optional<OverTimeSheet> overTimeWork = Optional.empty();
		Optional<HolidayWorkTimeSheet> holidayTimeSheet = Optional.empty();
		if (overTimeWorkSheet.isPresent()) {
			overTimeWork = Optional.of(overTimeWorkSheet.get().reCreateToCalcExcessWork(overTimeWorkSheet.get(),
					autoCalculationOfOverTimeWork));

		}
		if (holidayWorkSheet.isPresent()) {
			// holidayWorkSheet =
			// Optional.of(holidayWorkSheet.get().reCreateToCalcExcessWork(holidayWorkSheet.get(),autoCalcSetOfHolidayWorkTime));
		}

		// ExcessOfStatutoryMidNightTime totalExcessTime = new
		// ExcessOfStatutoryMidNightTime(midNightExcessTime.getTime().addMinutes(holidayWorkExcessTime.getTiem().getTime(),
		// holidayWorkExcessTime.getTiem().getCalcTime()));

		// return new
		// ExcessOfStatutoryTimeOfDaily(totalExcessTime,Optional.empty(),Optional.empty());
	}

}
