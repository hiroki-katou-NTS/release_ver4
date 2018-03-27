package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.json.DupDetector;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaiseSalaryTimeOfDailyPerfor;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalcSetOfHolidayWorkTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.OverDayEndCalcSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.RaisingSalaryCalcAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.worktime.common.CommonRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixOffdayWorkTimezone;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowFixedRestSet;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowRestCalcMethod;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 1日の計算範囲
 * 
 * @author keisuke_hoshina
 *
 */
@Getter
public class CalculationRangeOfOneDay {

	private Finally<WithinWorkTimeSheet> withinWorkingTimeSheet = Finally.empty();

	@Setter
	private Finally<OutsideWorkTimeSheet> outsideWorkTimeSheet = Finally.empty();

	private TimeSpanForCalc oneDayOfRange;

	private WorkInfoOfDailyPerformance workInformationOfDaily;

	private TimeLeavingOfDailyPerformance attendanceLeavingWork;

	private PredetermineTimeSetForCalc predetermineTimeSetForCalc;

	private Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime = Finally.empty();// 時間休暇加算残時間

	private Optional<DeductionTimeSheet> temporaryDeductionTimeSheet = Optional.empty(); // 休憩時間の算出ロジック(概要設計)が終わるまで一時的措置のため設置
																							// 2017.11.15
																							// by
																							// hoshina

	public CalculationRangeOfOneDay(Finally<WithinWorkTimeSheet> withinWorkingTimeSheet,
			Finally<OutsideWorkTimeSheet> outsideWorkTimeSheet, TimeSpanForCalc oneDayOfRange,
			TimeLeavingOfDailyPerformance attendanceLeavingWork, PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime,
			WorkInfoOfDailyPerformance workInformationofDaily) {
		this.withinWorkingTimeSheet = withinWorkingTimeSheet;
		this.outsideWorkTimeSheet = outsideWorkTimeSheet;
		this.oneDayOfRange = oneDayOfRange;
		this.attendanceLeavingWork = attendanceLeavingWork;
		this.predetermineTimeSetForCalc = predetermineTimeSetForCalc;
		this.timeVacationAdditionRemainingTime = timeVacationAdditionRemainingTime;
		this.workInformationOfDaily = workInformationofDaily;
	}

	/**
	 * 就業時間帯の作成
	 * 
	 * @param workingSystem
	 * @param setMethod
	 * @param clockManage
	 * @param dailyGoOutSheet
	 * @param CommonSet
	 * @param fixedCalc
	 * @param workTimeDivision
	 * @param noStampSet
	 * @param fluidSet
	 * @param breakmanage
	 * @param workTimeMethodSet
	 * @param fluRestTime
	 * @param fluidprefixBreakTimeSet
	 * @param predetermineTimeSet
	 * @param fixedWorkSetting
	 * @param workTimeCommonSet
	 * @param bonusPaySetting
	 * @param overTimeHourSetList
	 * @param fixOff
	 * @param dayEndSet
	 * @param overDayEndSet
	 * @param holidayTimeWorkItem
	 * @param beforeDay
	 *            前日の勤務種類
	 * @param toDay
	 *            当日の勤務種類
	 * @param afterDay
	 *            翌日の勤務種類
	 * @param breakdownTimeDay
	 * @param dailyTime
	 *            法定労働時間
	 * @param autoCalculationSet
	 * @param statutorySet
	 * @param prioritySet
	 */
	public void createWithinWorkTimeSheet(WorkingSystem workingSystem, WorkTimeMethodSet setMethod,
			RestClockManageAtr clockManage, OutingTimeOfDailyPerformance dailyGoOutSheet, CommonRestSetting commonSet,
			Optional<FixedRestCalculateMethod> fixedCalc, WorkTimeDivision workTimeDivision, 
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			FixedWorkSetting fixedWorkSetting, BonusPaySetting bonusPaySetting,
			List<OverTimeOfTimeZoneSet> overTimeHourSetList, List<HDWorkTimeSheetSetting> fixOff, OverDayEndCalcSet dayEndSet,
			List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem, WorkType beforeDay, WorkType toDay, WorkType afterDay,
			BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, AutoCalOvertimeSetting autoCalculationSet,
			LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet, WorkTimeSetting workTime,List<BreakTimeOfDailyPerformance> breakTimeOfDailyList
			,MidNightTimeSheet midNightTimeSheet,DailyCalculationPersonalInformation personalInfo) {
		/* 固定控除時間帯の作成 */
		DeductionTimeSheet deductionTimeSheet = DeductionTimeSheet.createTimeSheetForFixBreakTime(
				setMethod, clockManage, dailyGoOutSheet, this.oneDayOfRange, commonSet, attendanceLeavingWork,
				fixedCalc, workTimeDivision, breakTimeOfDailyList);
		this.temporaryDeductionTimeSheet = Optional.of(deductionTimeSheet);
		
		val fixedWorkTImeZoneSet = new CommonFixedWorkTimezoneSet();
		fixedWorkTImeZoneSet.forFixed(fixedWorkSetting.getLstHalfDayWorkTimezone());
		theDayOfWorkTimesLoop(workingSystem, predetermineTimeSetForCalc, fixedWorkTImeZoneSet,fixedWorkSetting.getCommonSetting(), bonusPaySetting,
				overTimeHourSetList, fixOff, dayEndSet, holidayTimeWorkItem, beforeDay, toDay, afterDay,
				breakdownTimeDay, dailyTime, autoCalculationSet, statutorySet, prioritySet, deductionTimeSheet,
				workTime,midNightTimeSheet,personalInfo);
	}

	/**
	 * 時間帯作成(勤務回数分のループ) 就業時間内・外の処理
	 * 
	 * @param workingSystem
	 *            労働制クラス
	 * @param predetermineTimeSet
	 *            所定時間設定クラス
	 * @param fixedWorkSetting
	 *            固定勤務設定クラス
	 * @param workTimeCommonSet
	 *            就業時間帯の共通設定クラス
	 * @param bonusPaySetting
	 *            加給設定クラス
	 * @param overTimeHourSetList
	 *            残業時間の時間帯設定クラス
	 * @param fixOff
	 *            固定勤務の休日出勤用勤務時間帯クラス
	 * @param dayEndSet
	 *            0時跨ぎ計算設定クラス
	 * @param overDayEndSet
	 *            就業時間帯の共通設定クラス
	 * @param holidayTimeWorkItem
	 *            休出枠時間帯
	 * @param beforeDay
	 *            勤務種類クラス
	 * @param toDay
	 *            勤務種類クラス
	 * @param afterDay
	 *            勤務種類クラス
	 * @param breakdownTimeDay
	 *            1日の時間内訳クラス
	 * @param dailyTime
	 *            法定労働時間
	 * @param autoCalculationSet
	 *            残業時間の自動計算設定クラス
	 * @param statutorySet
	 *            法定内残業設定
	 * @param prioritySet
	 *            法定内優先設定
	 * @param deductionTimeSheet
	 *            控除時間帯
	 */
	public void theDayOfWorkTimesLoop(WorkingSystem workingSystem, PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
			WorkTimezoneCommonSet workTimeCommonSet, BonusPaySetting bonusPaySetting,
			List<OverTimeOfTimeZoneSet> overTimeHourSetList, List<HDWorkTimeSheetSetting> fixOff, OverDayEndCalcSet dayEndSet,
			List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem, WorkType beforeDay, WorkType toDay, WorkType afterDay,
			BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, AutoCalOvertimeSetting autoCalculationSet,
			LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet,
			DeductionTimeSheet deductionTimeSheet, WorkTimeSetting workTime,MidNightTimeSheet midNightTimeSheet,
			DailyCalculationPersonalInformation personalInfo) {
		if (workingSystem.isExcludedWorkingCalculate()) {
			/* 計算対象外の処理 */
			return;
		}
		for (int workNumber = 1; workNumber <= attendanceLeavingWork.getTimeLeavingWorks().size(); workNumber++) {
			
			/* 就業内の時間帯作成 */
			//打刻はある前提で動く
			val createWithinWorkTimeSheet = WithinWorkTimeSheet.createAsFixed(attendanceLeavingWork.getAttendanceLeavingWork(new WorkNo(workNumber)).get(),
																			  toDay,
																			  predetermineTimeSetForCalc, 
																			  lstHalfDayWorkTimezone,
																			  workTimeCommonSet,
																			  deductionTimeSheet,
																			  bonusPaySetting,
																			  midNightTimeSheet,
																			  workNumber);
			if(withinWorkingTimeSheet.isPresent()) {
				withinWorkingTimeSheet.get().getWithinWorkTimeFrame().addAll(createWithinWorkTimeSheet.getWithinWorkTimeFrame());
			}
			else {
				withinWorkingTimeSheet.set(createWithinWorkTimeSheet);
			}
			/* 就業外の時間帯作成 */
			//打刻はある前提で動く
			val createOutSideWorkTimeSheet = OutsideWorkTimeSheet.createOutsideWorkTimeSheet(overTimeHourSetList, fixOff,
					attendanceLeavingWork.getAttendanceLeavingWork(new WorkNo(workNumber)).get(),
					workNumber, dayEndSet, workTimeCommonSet, holidayTimeWorkItem, beforeDay, toDay, afterDay, workTime,
					workingSystem, breakdownTimeDay, dailyTime, autoCalculationSet, statutorySet, prioritySet
					,bonusPaySetting,midNightTimeSheet,personalInfo,deductionTimeSheet);
			if(!outsideWorkTimeSheet.isPresent()) {
				//outsideWorkTimeSheet.set(createOutSideWorkTimeSheet);
				this.outsideWorkTimeSheet = Finally.of(createOutSideWorkTimeSheet);
			}
			else {
				if(outsideWorkTimeSheet.get().getOverTimeWorkSheet().isPresent()) {
					List<OverTimeFrameTimeSheetForCalc> addOverList = createOutSideWorkTimeSheet.getOverTimeWorkSheet().isPresent()? createOutSideWorkTimeSheet.getOverTimeWorkSheet().get().getFrameTimeSheets():Collections.emptyList();
					outsideWorkTimeSheet.get().getOverTimeWorkSheet().get().getFrameTimeSheets().addAll(addOverList);
				}
				else {
					this.outsideWorkTimeSheet = Finally.of(new OutsideWorkTimeSheet(createOutSideWorkTimeSheet.getOverTimeWorkSheet(),this.outsideWorkTimeSheet.get().getHolidayWorkTimeSheet()));
				}
				if(outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().isPresent()) {
					List<HolidayWorkFrameTimeSheetForCalc> addHolList = createOutSideWorkTimeSheet.getHolidayWorkTimeSheet().isPresent()? createOutSideWorkTimeSheet.getHolidayWorkTimeSheet().get().getWorkHolidayTime():Collections.emptyList();
					outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().get().getWorkHolidayTime().addAll(addHolList);
				}
				else {
					this.outsideWorkTimeSheet = Finally.of(new OutsideWorkTimeSheet(this.outsideWorkTimeSheet.get().getOverTimeWorkSheet(),createOutSideWorkTimeSheet.getHolidayWorkTimeSheet()));
				}
			}
		}
}




	
	/**
	 * 各深夜時間の算出結果から深夜時間の合計を算出する
	 * 
	 * @return 深夜時間
	 */
	public ExcessOfStatutoryTimeOfDaily calcMidNightTime(ExcessOfStatutoryTimeOfDaily excessOfDaily,
			AutoCalcSetOfHolidayWorkTime autoCalcSet) {
		// ExcessOverTimeWorkMidNightTime excessHolidayWorkMidNight =
		// excessOfDaily.getOverTimeWork().get().calcMidNightTimeIncludeOverTimeWork();
		// HolidayMidnightWork excessMidNight =
		// excessOfDaily.getWorkHolidayTime().get().calcMidNightTimeIncludeHolidayWorkTime(autoCalcSet);
		int beforeTime = 0;
		int totalTime = 0/* 残業深夜と休出深夜の合計算出 */;
		excessOfDaily.setExcessOfStatutoryMidNightTime(
				new ExcessOfStatutoryMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(totalTime)), new AttendanceTime(beforeTime)));
		return excessOfDaily;
	}

	/**
	 * 就内・残業内・休出時間内の加給時間の合計を求める
	 */
	public List<BonusPayTime> calcBonusPayTime(RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,
											   CalAttrOfDailyPerformance calcAtrOfDaily, BonusPayAtr bonusPayAtr) {
		
		List<BonusPayTime> overTimeBonusPay = new ArrayList<>();
		List<BonusPayTime> holidayWorkBonusPay = new ArrayList<>();
		List<BonusPayTime> withinBonusPay = new ArrayList<>();
		if(withinWorkingTimeSheet.isPresent())
			withinBonusPay = withinWorkingTimeSheet.get().calcBonusPayTimeInWithinWorkTime(raisingAutoCalcSet,bonusPayAutoCalcSet, bonusPayAtr,calcAtrOfDaily);
		
		if(outsideWorkTimeSheet.isPresent())
		{
			if(outsideWorkTimeSheet.get().getOverTimeWorkSheet().isPresent()) { 
				overTimeBonusPay = outsideWorkTimeSheet.get().getOverTimeWorkSheet().get().calcBonusPayTimeInOverWorkTime(raisingAutoCalcSet, bonusPayAutoCalcSet, bonusPayAtr, calcAtrOfDaily);
			}
			
			if(outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().isPresent()) {
				holidayWorkBonusPay = outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().get().calcBonusPayTimeInHolidayWorkTime(raisingAutoCalcSet, bonusPayAutoCalcSet, bonusPayAtr, calcAtrOfDaily);
			}
		}
		return calcBonusPayTime(withinBonusPay,overTimeBonusPay,holidayWorkBonusPay);
	}
	
	/**
	 * 就内・残業内・休出時間内の特定加給時間の合計を求める
	 */
	public List<BonusPayTime> calcSpecBonusPayTime(RaisingSalaryCalcAtr raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,
												   CalAttrOfDailyPerformance calcAtrOfDaily,BonusPayAtr bonusPayAtr){
		List<BonusPayTime> overTimeBonusPay = new ArrayList<>();
		List<BonusPayTime> holidayWorkBonusPay = new ArrayList<>();
		List<BonusPayTime> withinBonusPay = new ArrayList<>();
		
		if(withinWorkingTimeSheet.isPresent())
				withinWorkingTimeSheet.get().calcSpecifiedBonusPayTimeInWithinWorkTime(raisingAutoCalcSet,bonusPayAutoCalcSet, bonusPayAtr,calcAtrOfDaily);

		if(outsideWorkTimeSheet.isPresent())
		{
			if(outsideWorkTimeSheet.get().getOverTimeWorkSheet().isPresent()) { 
				overTimeBonusPay = outsideWorkTimeSheet.get().getOverTimeWorkSheet().get().calcSpecBonusPayTimeInOverWorkTime(raisingAutoCalcSet, bonusPayAutoCalcSet, bonusPayAtr, calcAtrOfDaily);
			}
			
			if(outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().isPresent()) {
				holidayWorkBonusPay = outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().get().calcSpecBonusPayTimeInHolidayWorkTime(raisingAutoCalcSet, bonusPayAutoCalcSet, bonusPayAtr, calcAtrOfDaily);
			}
		}
		return calcBonusPayTime(withinBonusPay,overTimeBonusPay,holidayWorkBonusPay);
	}
	
	/**
	 * 就・残・休の加給時間を合計する
	 * @param withinBonusPay
	 * @param overTimeBonusPay
	 * @param holidayWorkBonusPay
	 * @return　合計後の加算時間(Noでユニーク)
	 */
	private List<BonusPayTime> calcBonusPayTime(List<BonusPayTime> withinBonusPay ,
								   List<BonusPayTime> overTimeBonusPay ,
								   List<BonusPayTime> holidayWorkBonusPay){
		AttendanceTime bonusTime = new AttendanceTime(0);
		List<BonusPayTime> returnList = new ArrayList<>();
		
		overTimeBonusPay.addAll(holidayWorkBonusPay);
		val excessPayTime = overTimeBonusPay;
		for(int bonusPayNo = 1 ; bonusPayNo <= 10 ; bonusPayNo++) {
			returnList.add(addWithinAndExcessBonusTime(sumBonusPayTime(getByBonusPayNo(withinBonusPay,bonusPayNo),bonusPayNo),
													   sumBonusPayTime(getByBonusPayNo(excessPayTime,bonusPayNo),bonusPayNo)
													   ,bonusPayNo));
		}
		return returnList;
	}
	
	/**
	 * 受け取った2つの加給時間が持つ時間を合算
	 * @param within
	 * @param excess
	 * @param bonusPayNo
	 * @return
	 */
	private BonusPayTime addWithinAndExcessBonusTime(BonusPayTime within,BonusPayTime excess,int bonusPayNo) {
		return new BonusPayTime(bonusPayNo,
								within.getBonusPayTime().addMinutes(excess.getBonusPayTime().valueAsMinutes()),
								within.getWithinBonusPay().addMinutes(excess.getWithinBonusPay().getTime(), 
																	  excess.getWithinBonusPay().getCalcTime()),
								within.getExcessBonusPayTime().addMinutes(excess.getExcessBonusPayTime().getTime(), 
										  								  excess.getExcessBonusPayTime().getCalcTime())
								);
	}
	
	/**
	 * 各時間の合計を算出
	 * @param bonusPayList　加給時間のリスト
	 * @param bonusPayNo　加給時間Ｎｏ
	 * @return　合計時間の加給時間
	 */
	private BonusPayTime sumBonusPayTime(List<BonusPayTime> bonusPayList, int bonusPayNo) {
		AttendanceTime bonusPayTime =  new AttendanceTime(bonusPayList.stream().map(tc -> tc.getBonusPayTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
		AttendanceTime withinTime = new AttendanceTime(bonusPayList.stream().map(tc -> tc.getWithinBonusPay().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
		AttendanceTime withinCalcTime = new AttendanceTime(bonusPayList.stream().map(tc -> tc.getWithinBonusPay().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
		AttendanceTime excessTime = new AttendanceTime(bonusPayList.stream().map(tc -> tc.getExcessBonusPayTime().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
		AttendanceTime excessCalcTime = new AttendanceTime(bonusPayList.stream().map(tc -> tc.getExcessBonusPayTime().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
		
		return new BonusPayTime(bonusPayNo,
								bonusPayTime,
								TimeWithCalculation.createTimeWithCalculation(withinTime, withinCalcTime),
								TimeWithCalculation.createTimeWithCalculation(excessTime, excessCalcTime));
	}
	
	/**
	 * 受け取った加給時間Ｎｏを持つ加給時間を取得
	 * @param bonusPayTime 加給時間
	 * @param bonusPayNo　加給時間Ｎｏ
	 * @return　加給時間リスト
	 */
	private List<BonusPayTime> getByBonusPayNo(List<BonusPayTime> bonusPayTime,int bonusPayNo){
		return bonusPayTime.stream().filter(tc -> tc.getBonusPayTimeItemNo() == bonusPayNo).collect(Collectors.toList());
	}
	
	
	/**
	 * 控除時間を取得
	 * @param dedClassification 
	 * @param dedAtr
	 * @param statutoryAtrs
	 * @param pertimesheet
	 * @return
	 */
	public TimeWithCalculation calcWithinTotalTime(ConditionAtr dedClassification, DeductionAtr dedAtr,StatutoryAtr statutoryAtr,TimeSheetRoundingAtr pertimesheet) {
		if(statutoryAtr.isStatutory()) {
			if(this.withinWorkingTimeSheet.isPresent()) {
				return TimeWithCalculation.sameTime(this.withinWorkingTimeSheet.get().calculationAllFrameDeductionTime(dedAtr, dedClassification));
			}
		}
		else if(statutoryAtr.isExcess()) {
			if(this.getOutsideWorkTimeSheet().isPresent()) {
				AttendanceTime overTime = this.getOutsideWorkTimeSheet().get().caluclationAllOverTimeFrameTime(dedAtr, dedClassification);
				AttendanceTime holidaytime = this.getOutsideWorkTimeSheet().get().caluclationAllHolidayFrameTime(dedAtr, dedClassification);
				return TimeWithCalculation.sameTime(overTime.addMinutes(holidaytime.valueAsMinutes()));
			}
		}
		return TimeWithCalculation.sameTime(new AttendanceTime(0));
	}

	 /**
	 * フレックスの時間帯作成
	 */
	 public void createTimeSheetAsFlex(
			 		WorkingSystem workingSystem, PredetermineTimeSetForCalc predetermineTimeSetForCalc,
					BonusPaySetting bonusPaySetting,
					List<HDWorkTimeSheetSetting> fixOff,List<OverTimeOfTimeZoneSet> overTimeHourSetList,List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem,  
					OverDayEndCalcSet dayEndSet,WorkType beforeDay, WorkType toDay, WorkType afterDay,
					BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, AutoCalOvertimeSetting autoCalculationSet,
					LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet,
					WorkTimeSetting workTime,
					FlexWorkSetting flexWorkSetting,OutingTimeOfDailyPerformance outingTimeSheetofDaily,
					TimeSpanForCalc oneDayTimeSpan,TimeLeavingOfDailyPerformance attendanceLeaveWork,WorkTimeDivision workTimeDivision
					,List<BreakTimeOfDailyPerformance> breakTimeOfDailyList,MidNightTimeSheet midNightTimeSheet,DailyCalculationPersonalInformation personalInfo){
		 //if(!flexTimeSet.getUseFixedRestTime()){
			// predetermineTimeSetForCalc.correctPredetermineTimeSheet(dailyWork);
			 /*遅刻早退処理*/
			// for() {
			//	 WithinWorkTimeSheet.createWorkingHourSet(workType,predetermineTimeSet,fixedWorkSetting);
				 /*遅刻時間の計算*/
				 /*早退時間の計算*/
			// }
			 //WithinWorkTimeSheet.createWorkingHourSet(workType,predetermineTimeSetForCalc,fixedWorkSetting);
		 //}
		 //控除時間帯の作成
		 val deductionTimeSheet = provisionalDeterminationOfDeductionTimeSheet(outingTimeSheetofDaily,
				 oneDayTimeSpan, attendanceLeaveWork, workTimeDivision,breakTimeOfDailyList,flexWorkSetting.getOffdayWorkTime().getRestTimezone(),flexWorkSetting.getRestSetting());
		 this.temporaryDeductionTimeSheet = Optional.of(deductionTimeSheet);
		 /*固定勤務の時間帯作成*/
		 val fixedWorkTimeZoneSet = new CommonFixedWorkTimezoneSet();
		 fixedWorkTimeZoneSet.forFlex(flexWorkSetting.getLstHalfDayWorkTimezone());
		 theDayOfWorkTimesLoop( workingSystem,  predetermineTimeSetForCalc,
				 	fixedWorkTimeZoneSet,  flexWorkSetting.getCommonSetting(),  bonusPaySetting,
					overTimeHourSetList,  fixOff,  dayEndSet,
					holidayTimeWorkItem,  beforeDay,  toDay,  afterDay,
					 breakdownTimeDay,  dailyTime,  autoCalculationSet,
					 statutorySet,  prioritySet,
					 deductionTimeSheet,  workTime,midNightTimeSheet,personalInfo);
		 /*コアタイムのセット*/
		 //this.withinWorkingTimeSheet.set(withinWorkingTimeSheet.get().createWithinFlexTimeSheet(flexWorkSetting.getCoreTimeSetting()));
		 this.withinWorkingTimeSheet = Finally.of(withinWorkingTimeSheet.get().createWithinFlexTimeSheet(flexWorkSetting.getCoreTimeSetting()));
	 }
	
//	 /**
//	 * 流動休憩用の控除時間帯作成
//	 */
//	 public void createFluidBreakTime(DeductionAtr deductionAtr) {
//	 DeductionTimeSheet.createDedctionTimeSheet(acqAtr, setMethod,
//	 clockManage, dailyGoOutSheet, oneDayRange, CommonSet,
//	 attendanceLeaveWork, fixedCalc, workTimeDivision, noStampSet, fixedSet,
//	 breakTimeSheet);
//	
//	 }

	// ＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	//
	// /**
	// * 流動勤務の時間帯作成
	// */
	// public void createFluidWork(
	// int workNo,
	// WorkTime workTime,
	// AttendanceLeavingWorkOfDaily attendanceLeavingWork,
	// DeductionTimeSheet deductionTimeSheet,
	// PredetermineTimeSet predetermineTimeSet,
	// WithinWorkTimeSheet withinWorkTimeSheet,
	// WithinWorkTimeFrame withinWorkTimeFrame,
	// HolidayWorkTimeSheet holidayWorkTimeSheet,
	// WorkType worktype) {
	// //所定時間設定をコピーして計算用の所定時間設定を作成する
	// this.predetermineTimeSetForCalc = new PredetermineTimeSetForCalc(
	// predetermineTimeSet.getAdditionSet(),
	// predetermineTimeSet.getSpecifiedTimeSheet().getTimeSheets(),
	// predetermineTimeSet.getSpecifiedTimeSheet().getAMEndTime(),
	// predetermineTimeSet.getSpecifiedTimeSheet().getPMStartTime());
	// //出退勤分ループ
	// for(AttendanceLeavingWork attendanceLeavingWork :
	// attendanceLeavingWork.getAttendanceLeavingWork(workNo)) {
	// //事前に遅刻早退、控除時間帯を取得する
	// this.getForDeductionTimeSheetList(workNo, attendanceLeavingWork,
	// predetermineTimeSet, deductionTimeSheet ,workInformationOfDaily,
	// workType, withinWorkTimeFrame);
	// }
	// //「出勤系」か「休出系」か判断する
	// boolean isWeekDayAttendance = worktype.isWeekDayAttendance();
	// //時間休暇加算残時間未割当←時間休暇加算残時間
	//
	// if(isWeekDayAttendance) {//出勤系の場合
	// //流動勤務（就内、平日）
	// WithinWorkTimeSheet newWithinWorkTimeSheet =
	// withinWorkTimeSheet.createAsFluidWork(predetermineTimeSetForCalc,
	// worktype, workInformationOfDaily, fluidWorkSetting, deductionTimeSheet);
	// //流動勤務（就外、平日）
	//
	// }else{//休出系の場合
	// //流動勤務（休日出勤）
	// HolidayWorkTimeSheet holidayWorkTimeSheet =
	// holidayWorkTimeSheet.createholidayWorkTimeSheet(attendanceLeavingWork,
	// workingTimes, deductionTimeSheet, worktype, holidayWorkTimeOfDaily,
	// calcRange);
	// }
	//
	//
	// }
	//
	// /**
	// * 事前に遅刻早退、控除時間帯を取得する
	// * @param workNo
	// * @param attendanceLeavingWork 出退勤
	// * @return
	// */
	// public List<TimeSheetOfDeductionItem> getForDeductionTimeSheetList(
	// int workNo,
	// AttendanceLeavingWork attendanceLeavingWork,
	// PredetermineTimeSet predetermineTimeSet,
	// DeductionTimeSheet deductionTimeSheet,
	// WorkInformationOfDaily workInformationOfDaily,
	// WorkType workType,
	// WithinWorkTimeFrame withinWorkTimeFrame){
	//
	// //所定時間帯を取得する(流動計算で使用する所定時間の作成)
	// createPredetermineTimeSheetForFluid(workNo, predetermineTimeSet,
	// workType, workInformationOfDaily);
	// //計算範囲を判断する
	// withinWorkTimeFrame.createWithinWorkTimeFrameForFluid(attendanceLeavingWork,
	// dailyWork, predetermineTimeSetForCalc);
	// //遅刻時間帯を控除
	// withinWorkTimeFrame.getLateTimeSheet().lateTimeCalcForFluid(withinWorkTimeFrame,
	// lateRangeForCalc, workTimeCommonSet, lateDecisionClock,
	// deductionTimeSheet);
	// //控除時間帯の仮確定
	// this.provisionalDeterminationOfDeductionTimeSheet(deductionTimeSheet);
	// //早退時間帯を控除
	//
	// //勤務間の休憩設定を取得
	//
	// }
	//
	// /**
	// * 計算用所定時間設定を作成する（流動用）
	// * @return
	// */
	// public void createPredetermineTimeSheetForFluid(
	// int workNo,
	// PredetermineTimeSet predetermineTimeSet,
	// WorkType workType,
	// WorkInformationOfDaily workInformationOfDaily) {
	//
	// //予定と実績が同じ勤務かどうか確認
	// if(workInformationOfDaily.isMatchWorkInfomation()/*予定時間帯に値が入っているかのチェックを追加する必要あり*/)
	// {
	// //予定時間帯を取得する
	// ScheduleTimeSheet scheduleTimeSheet =
	// workInformationOfDaily.getScheduleTimeSheet(workNo);
	// //所定時間帯設定の時間帯を全て取得する
	// List<TimeSheetWithUseAtr> timeSheetList =
	// predetermineTimeSet.getSpecifiedTimeSheet().getTimeSheets();
	// //変更対象の時間帯を取得
	// List<TimeSheetWithUseAtr> list = timeSheetList.stream().filter(ts ->
	// ts.getCount()==workNo).collect(Collectors.toList());
	// TimeSheetWithUseAtr timeSheet = list.get(0);
	// //予定時間帯と変更対象の時間帯を基に時間帯を作成
	// TimeSheetWithUseAtr targetTimeSheet = new TimeSheetWithUseAtr(
	// timeSheet.getUseAtr(),
	// scheduleTimeSheet.getAttendance(),
	// scheduleTimeSheet.getLeaveWork(),
	// workNo);
	// //変更対象以外の時間帯を取得
	// List<TimeSheetWithUseAtr> list2 = timeSheetList.stream().filter(ts ->
	// ts.getCount()!=workNo).collect(Collectors.toList());
	// TimeSheetWithUseAtr timeSheet2 = list2.get(0);
	//
	// List<TimeSheetWithUseAtr> newTimeSheetList =
	// Arrays.asList(targetTimeSheet,timeSheet2);
	//
	// this.predetermineTimeSetForCalc = new PredetermineTimeSetForCalc(
	// this.predetermineTimeSetForCalc.getAdditionSet(),
	// newTimeSheetList,
	// this.predetermineTimeSetForCalc.getAMEndTime(),
	// this.predetermineTimeSetForCalc.getPMStartTime());
	// }
	// //午前勤務、午後勤務の場合に時間帯を補正する処理
	// this.predetermineTimeSetForCalc.getPredetermineTimeSheet().correctPredetermineTimeSheet(workType.getDailyWork());
	// }
	//
	//
	 /**
	 * 控除時間帯の仮確定
	 */
	 public static DeductionTimeSheet provisionalDeterminationOfDeductionTimeSheet(OutingTimeOfDailyPerformance outingTimeSheetofDaily,
				TimeSpanForCalc oneDayTimeSpan,TimeLeavingOfDailyPerformance attendanceLeaveWork,WorkTimeDivision workTimeDivision
				,List<BreakTimeOfDailyPerformance> breakTimeOfDailyList,FlowWorkRestTimezone flowRestTimezone,FlowWorkRestSetting flowRestSetting) {
		 //控除用
		 val dedTimeSheet = DeductionTimeSheet.provisionalDecisionOfDeductionTimeSheet(DeductionAtr.Deduction, outingTimeSheetofDaily,
				 oneDayTimeSpan, attendanceLeaveWork, workTimeDivision,breakTimeOfDailyList,flowRestTimezone,flowRestSetting);
	 	 //計上用
	 	 val recordTimeSheet = DeductionTimeSheet.provisionalDecisionOfDeductionTimeSheet(DeductionAtr.Appropriate, outingTimeSheetofDaily,
	 			 oneDayTimeSpan, attendanceLeaveWork, workTimeDivision,breakTimeOfDailyList,flowRestTimezone,flowRestSetting);
	 
	 	return new DeductionTimeSheet(dedTimeSheet,recordTimeSheet); 
	 }
	 
}
