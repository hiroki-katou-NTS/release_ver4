package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.actualworkinghours.SubHolOccurrenceInfo;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaisingSalaryTime;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.ot.zerotime.ZeroTime;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
//import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
//import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
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

	//残業時間帯
	private Optional<OverTimeSheet> overTimeWorkSheet;

	//休出時間帯
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
	 * @param overDayEndCalcSet
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
	 * @param createWithinWorkTimeSheet 
	 * @param workTimeDailyAtr 
	 * @param integrationOfDaily 
	 * @return
	 */
	public static OutsideWorkTimeSheet createOutsideWorkTimeSheet(List<OverTimeOfTimeZoneSet> overTimeHourSetList,
			List<HDWorkTimeSheetSetting> fixOff, TimeLeavingWork attendanceLeave, int workNo, Optional<ZeroTime> overDayEndCalcSet,
			WorkTimezoneCommonSet overDayEndSet, List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem, WorkType beforeDay,
			WorkType toDay, WorkType afterDay, WorkTimeSetting workTime, WorkingSystem workingSystem,
			BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, AutoCalOvertimeSetting autoCalculationSet,
			LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet,Optional<BonusPaySetting> bonuspaySetting,MidNightTimeSheet midNightTimeSheet,
			DailyCalculationPersonalInformation personalInfo,DeductionTimeSheet deductionTimeSheet,DailyUnit dailyUnit,HolidayCalcMethodSet holidayCalcMethodSet, WithinWorkTimeSheet createWithinWorkTimeSheet,
    		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
    		PredetermineTimeSetForCalc predetermineTimeSet, Optional<WorkTimeCode> siftCode, 
    		boolean late, boolean leaveEarly, WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
    		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet,Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
    		Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<CoreTimeSetting> coreTimeSetting,
    		Optional<WorkInformation> beforeInfo, Optional<WorkInformation> afterInfo,Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets, WorkTimeDailyAtr workTimeDailyAtr) {
		
		List<HolidayWorkFrameTimeSheetForCalc> holidayWorkFrameTimeSheetForCalc = new ArrayList<>();
		List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheet = new ArrayList<>();
		if (toDay.isWeekDayAttendance()) {
			/* 就業時間外時間帯の平日出勤の処理 */
			overTimeWorkFrameTimeSheet = OverTimeFrameTimeSheetForCalc.createOverWorkFrame(
					overTimeHourSetList, workingSystem, attendanceLeave, workNo, breakdownTimeDay, dailyTime,
					autoCalculationSet, statutorySet, prioritySet,bonuspaySetting,midNightTimeSheet,
					personalInfo,true,deductionTimeSheet,dailyUnit,holidayCalcMethodSet,createWithinWorkTimeSheet, 
					vacationClass, timevacationUseTimeOfDaily, toDay,
					predetermineTimeSet, siftCode, leaveEarly, leaveEarly, illegularAddSetting, flexAddSetting, regularAddSetting, holidayAddtionSet,commonSetting,conditionItem,
					predetermineTimeSetByPersonInfo,coreTimeSetting, specificDateAttrSheets,workTimeDailyAtr);

			/* 0時跨ぎ処理 */
			if(overDayEndCalcSet.isPresent()) {
				OverDayEnd overTimeDayEnd = OverDayEnd.forOverTime(overDayEndSet.isZeroHStraddCalculateSet(), overTimeWorkFrameTimeSheet, beforeDay, toDay,afterDay, beforeInfo,afterInfo,overDayEndCalcSet.get());
				overTimeWorkFrameTimeSheet = overTimeDayEnd.getOverTimeList();
				holidayWorkFrameTimeSheetForCalc = overTimeDayEnd.getHolList();
			}
		} else {
			holidayWorkFrameTimeSheetForCalc = HolidayWorkFrameTimeSheetForCalc.createHolidayTimeWorkFrame(attendanceLeave,fixOff,toDay,bonuspaySetting,midNightTimeSheet,deductionTimeSheet,commonSetting, specificDateAttrSheets);

			/* 0時跨ぎ */
			if(overDayEndCalcSet.isPresent()) {
				OverDayEnd holidayWorkDayEnd = OverDayEnd.forHolidayWorkTime(overDayEndSet.isZeroHStraddCalculateSet(), holidayWorkFrameTimeSheetForCalc, beforeDay, toDay,afterDay,beforeInfo,afterInfo,overDayEndCalcSet.get());
				overTimeWorkFrameTimeSheet = holidayWorkDayEnd.getOverTimeList();
				holidayWorkFrameTimeSheetForCalc = holidayWorkDayEnd.getHolList();
			}

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
