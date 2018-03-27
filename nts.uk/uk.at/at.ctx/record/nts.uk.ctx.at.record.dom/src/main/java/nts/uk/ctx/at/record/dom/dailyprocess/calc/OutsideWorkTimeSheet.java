package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.record.dom.actualworkinghours.SubHolOccurrenceInfo;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaisingSalaryTime;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 就業時間外時間帯
 * 
 * @author keisuke_hoshina
 *
 */
@Getter
public class OutsideWorkTimeSheet {

	private Optional<OverTimeSheet> overTimeWorkSheet;

	private Optional<HolidayWorkTimeSheet> holidayWorkTimeSheet;

	/**
	 * Constructor 
	 */
	public OutsideWorkTimeSheet(Optional<OverTimeSheet> overTimeWorkSheet,
			Optional<HolidayWorkTimeSheet> holidayWorkTimeSheet) {
		super();
		this.overTimeWorkSheet = overTimeWorkSheet;
		this.holidayWorkTimeSheet = holidayWorkTimeSheet;
	}
	
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
	public static OutsideWorkTimeSheet createOutsideWorkTimeSheet(List<OverTimeOfTimeZoneSet> overTimeHourSetList,
			List<HDWorkTimeSheetSetting> fixOff, TimeLeavingWork attendanceLeave, int workNo, OverDayEndCalcSet dayEndSet,
			WorkTimezoneCommonSet overDayEndSet, List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem, WorkType beforeDay,
			WorkType toDay, WorkType afterDay, WorkTimeSetting workTime, WorkingSystem workingSystem,
			BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, AutoCalOvertimeSetting autoCalculationSet,
			LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet,BonusPaySetting bonusPaySetting,MidNightTimeSheet midNightTimeSheet,
			DailyCalculationPersonalInformation personalInfo,DeductionTimeSheet deductionTimeSheet) {
		
		List<HolidayWorkFrameTimeSheetForCalc> holidayWorkFrameTimeSheetForCalc = new ArrayList<>();
		List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheet = new ArrayList<>();
		if (toDay.isWeekDayAttendance()) {
			/* 就業時間外時間帯の平日出勤の処理 */
			overTimeWorkFrameTimeSheet = OverTimeFrameTimeSheetForCalc.createOverWorkFrame(
					overTimeHourSetList, workingSystem, attendanceLeave, workNo, breakdownTimeDay, dailyTime,
					autoCalculationSet, statutorySet, prioritySet,bonusPaySetting,midNightTimeSheet,
					personalInfo,true,deductionTimeSheet);

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
//			new HolidayWorkFrameTimeSheet(new HolidayWorkFrameNo(1), new TimeSpanForCalc(new TimeWithDayAttr(0),new TimeWithDayAttr(0)));
//					fixOff.getWorkTimezone(), attendanceLeave, dayEndSet, overDayEndSet, holidayTimeWorkItem, beforeDay,
//					toDay, afterDay);
			holidayWorkFrameTimeSheetForCalc = HolidayWorkFrameTimeSheetForCalc.createHolidayTimeWorkFrame(attendanceLeave,fixOff,toDay,bonusPaySetting,midNightTimeSheet,deductionTimeSheet);

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
		return new OutsideWorkTimeSheet(
				   Optional.of(new OverTimeSheet(new RaisingSalaryTime(),
						   						 overTimeWorkFrameTimeSheet,
						   						new SubHolOccurrenceInfo()
						   						 ))
				   ,
				   Optional.of(new HolidayWorkTimeSheet(new RaisingSalaryTime(),
						   								holidayWorkFrameTimeSheetForCalc, 
						   								new SubHolOccurrenceInfo()))
				   );
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
			AutoCalOvertimeSetting autoCalculationOfOverTimeWork,
			AutoCalcSetOfHolidayWorkTime autoCalcSetOfHolidayWorkTime) {
		Optional<OverTimeSheet> overTimeWork = Optional.empty();
		Optional<HolidayWorkTimeSheet> holidayTimeSheet = Optional.empty();
		if (overTimeWorkSheet.isPresent()) {
//			overTimeWork = Optional.of(overTimeWorkSheet.get().calcMidNightTime(overTimeWorkSheet.get(),
//					autoCalculationOfOverTimeWork));

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

//	public void addtimesheet(OutsideWorkTimeSheet outsideSheet) {
//		if(this.overTimeWorkSheet.isPresent()) {
//			
//		}
//		else {
//			this.overTimeWorkSheet = outsideSheet.getOverTimeWorkSheet();
//		}
//			
//	}
//	
//	private void addoverTimesheet(OverTime)
	
	/**
	 * 残業時間の中にある控除時間を算出する
	 * @param dedAtr
	 * @param atr
	 * @return 控除時間
	 */
	public AttendanceTime caluclationAllOverTimeFrameTime(DeductionAtr dedAtr,ConditionAtr atr) {
		if(this.overTimeWorkSheet.isPresent()) {
			return this.overTimeWorkSheet.get().calculationAllFrameDeductionTime(dedAtr,atr);
		}
		return new AttendanceTime(0);
	}
	
	/**
	 * 休出時間の中にある控除時間を算出する
	 * @param dedAtr
	 * @param atr
	 * @return　控除時間
	 */
	public AttendanceTime caluclationAllHolidayFrameTime(DeductionAtr dedAtr,ConditionAtr atr) {
		if(this.holidayWorkTimeSheet.isPresent()) {
			return this.holidayWorkTimeSheet.get().calculationAllFrameDeductionTime(dedAtr,atr);
		}
		return new AttendanceTime(0);
	}


	
}
