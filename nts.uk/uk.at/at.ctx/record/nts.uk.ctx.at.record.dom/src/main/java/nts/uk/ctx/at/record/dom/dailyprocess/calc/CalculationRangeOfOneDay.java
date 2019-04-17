package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.SubHolOccurrenceInfo;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTimeSheet;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeFrame;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethod;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfEachPremiumHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaisingSalaryTime;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkingTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.common.DailyTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.zerotime.ZeroTime;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.overtime.StatutoryPrioritySet;
import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
import nts.uk.ctx.at.shared.dom.worktime.common.CommonRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimezoneNo;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
import nts.uk.ctx.at.shared.dom.worktime.common.HDWorkTimeSheetSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.LegalOTSetting;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.RestClockManageAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneShortTimeWorkSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkRestTimezone;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDivision;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 1日の計算範囲
 * 
 * @author keisuke_hoshina
 *
 */
@Getter
public class CalculationRangeOfOneDay {

	//1日の範囲
	private TimeSpanForCalc oneDayOfRange;

	//勤務情報
	private WorkInfoOfDailyPerformance workInformationOfDaily;
	
	@Setter
	//出退勤
	private TimeLeavingOfDailyPerformance attendanceLeavingWork;

	//所定時間設定
	private PredetermineTimeSetForCalc predetermineTimeSetForCalc;
	
	/*----------------Optional-----------------------*/
	//インターバル制度管理
	
	//休暇使用合計残時間未割当
	private Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime = Finally.empty();// 時間休暇�?算残時�?
	
	//非勤務時間帯
	private Optional<NonWorkingTimeSheet> nonWorkingTimeSheet;
	/*----------------------Finally------------------------*/
	//加給時間
	
	//就業時間内時間帯
	private Finally<WithinWorkTimeSheet> withinWorkingTimeSheet = Finally.empty();

	@Setter
	//就業時間外時間帯
	private Finally<OutsideWorkTimeSheet> outsideWorkTimeSheet = Finally.empty();

	//出勤前時間
	private Finally<AttendanceTime> beforeAttendance = Finally.of(new AttendanceTime(0)); 
	
	//退勤後時間
	private Finally<AttendanceTime> afterLeaving = Finally.of(new AttendanceTime(0));



	public CalculationRangeOfOneDay(Finally<WithinWorkTimeSheet> withinWorkingTimeSheet,
			Finally<OutsideWorkTimeSheet> outsideWorkTimeSheet, TimeSpanForCalc oneDayOfRange,
			TimeLeavingOfDailyPerformance attendanceLeavingWork, PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime,
			WorkInfoOfDailyPerformance workInformationofDaily,
			Optional<NonWorkingTimeSheet> nonWorkingTimeSheet) {
		this.withinWorkingTimeSheet = withinWorkingTimeSheet;
		this.outsideWorkTimeSheet = outsideWorkTimeSheet;
		this.oneDayOfRange = oneDayOfRange;
		this.attendanceLeavingWork = attendanceLeavingWork;
		this.predetermineTimeSetForCalc = predetermineTimeSetForCalc;
		this.timeVacationAdditionRemainingTime = timeVacationAdditionRemainingTime;
		this.workInformationOfDaily = workInformationofDaily;
		this.nonWorkingTimeSheet = nonWorkingTimeSheet;
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
	 * @param bonuspaySetting
	 * @param overTimeHourSetList
	 * @param fixOff
	 * @param overDayEndCalcSet
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
	 * @param calcSetinIntegre
	 * @param statutorySet
	 * @param prioritySet
	 * @param fixWoSetting 
	 * @param ootsukaIWFlag 
	 * @param integrationOfDaily 
	 */
	public void createWithinWorkTimeSheet(WorkingSystem workingSystem, WorkTimeMethodSet setMethod,
			RestClockManageAtr clockManage,  Optional<OutingTimeOfDailyPerformance> dailyGoOutSheet, CommonRestSetting commonSet,
			Optional<FixedRestCalculateMethod> fixedCalc, WorkTimeDivision workTimeDivision, 
			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			FixedWorkSetting fixedWorkSetting, Optional<BonusPaySetting> bonuspaySetting,
			List<OverTimeOfTimeZoneSet> overTimeHourSetList, List<HDWorkTimeSheetSetting> fixOff, Optional<ZeroTime> overDayEndCalcSet,
			List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem, WorkType beforeDay, WorkType toDay, WorkType afterDay,
			BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, CalAttrOfDailyPerformance calcSetinIntegre,
			LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet, WorkTimeSetting workTime,List<BreakTimeOfDailyPerformance> breakTimeOfDailyList
			,MidNightTimeSheet midNightTimeSheet,DailyCalculationPersonalInformation personalInfo,Optional<CoreTimeSetting> coreTimeSetting,
			HolidayCalcMethodSet holidayCalcMethodSet,DailyUnit dailyUnit,List<TimeSheetOfDeductionItem> breakTimeList,
    		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
    		Optional<WorkTimeCode> siftCode, 
    		boolean late, boolean leaveEarly, WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
    		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet,Optional<WorkTimezoneCommonSet> commonSetting,
    		WorkingConditionItem conditionItem,Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
    		List<ShortWorkingTimeSheet> shortTimeSheets,WorkTimezoneShortTimeWorkSet workTimeShortTimeSet,
    		Optional<WorkInformation> beforeInfo, Optional<WorkInformation> afterInfo, List<EmTimeZoneSet> fixWoSetting,
    		Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets) {
		/* 固定控除時間帯の作成 */
		DeductionTimeSheet deductionTimeSheet = DeductionTimeSheet.createTimeSheetForFixBreakTime(
				setMethod, clockManage, dailyGoOutSheet, this.oneDayOfRange, commonSet, attendanceLeavingWork,
				fixedCalc, workTimeDivision, breakTimeOfDailyList, shortTimeSheets,workTimeShortTimeSet,commonSetting,holidayCalcMethodSet
				,predetermineTimeSetForCalc,toDay,fixWoSetting);
		
		
		val fixedWorkTImeZoneSet = new CommonFixedWorkTimezoneSet();
		fixedWorkTImeZoneSet.forFixed(fixedWorkSetting.getLstHalfDayWorkTimezone());
		
		 Optional<DeductLeaveEarly> leaveLateSet = regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()
					 ?Optional.of(regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly())
					 :Optional.empty();
					 
		theDayOfWorkTimesLoop(workingSystem, predetermineTimeSetForCalc, fixedWorkTImeZoneSet,fixedWorkSetting.getCommonSetting(), bonuspaySetting,
				overTimeHourSetList, fixOff, overDayEndCalcSet, holidayTimeWorkItem, beforeDay, toDay, afterDay,
				breakdownTimeDay, dailyTime, calcSetinIntegre, statutorySet, prioritySet, deductionTimeSheet,
				workTime,midNightTimeSheet,personalInfo,holidayCalcMethodSet,coreTimeSetting,dailyUnit,breakTimeList, 
				vacationClass, timevacationUseTimeOfDaily,  
				siftCode, leaveEarly, leaveEarly, illegularAddSetting, 
				flexAddSetting, regularAddSetting, holidayAddtionSet, commonSetting,conditionItem,predetermineTimeSetByPersonInfo,
				beforeInfo,afterInfo,leaveLateSet,specificDateAttrSheets,workTimeDivision);
	}

	/**
	 * 時間帯作成(勤務回数分ループ) 就業時間内・外作成処理
	 * 
	 * @param workingSystem
	 *            労働制クラス
	 * @param predetermineTimeSet
	 *            所定時間設定クラス
	 * @param fixedWorkSetting
	 *            固定勤務設定クラス
	 * @param workTimeCommonSet
	 *            就業時間帯の共通設定クラス
	 * @param bonuspaySetting
	 *            加給設定クラス
	 * @param overTimeHourSetList
	 *            残業時間の時間帯設定クラス
	 * @param fixOff
	 *            固定勤務の休日出勤用勤務時間帯クラス
	 * @param overDayEndCalcSet
	 *            0時跨ぎ計算設定クラス
	 * @param overDayEndSet
	 *            就業時間帯の共通設定クラス
	 * @param holidayTimeWorkItem
	 *            休出時間帯
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
	 * @param calcSetinIntegre
	 *            残業時間の自動計算設定クラス
	 * @param statutorySet
	 *            法定内残業設定
	 * @param prioritySet
	 *            法定内優先設定
	 * @param deductionTimeSheet
	 *            控除時間帯
	 * @param workTimeDivision 
	 * @param ootsukaIWFlag 
	 * @param integrationOfDaily 
	 */
	public void theDayOfWorkTimesLoop(WorkingSystem workingSystem, PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
			WorkTimezoneCommonSet workTimeCommonSet, Optional<BonusPaySetting> bonuspaySetting,
			List<OverTimeOfTimeZoneSet> overTimeHourSetList, List<HDWorkTimeSheetSetting> fixOff, Optional<ZeroTime> overDayEndCalcSet,
			List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem, WorkType beforeDay, WorkType toDay, WorkType afterDay,
			BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, CalAttrOfDailyPerformance calcSetinIntegre,
			LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet,
			DeductionTimeSheet deductionTimeSheet, WorkTimeSetting workTime,MidNightTimeSheet midNightTimeSheet,
			DailyCalculationPersonalInformation personalInfo,HolidayCalcMethodSet holidayCalcMethodSet,
			Optional<CoreTimeSetting> coreTimeSetting,DailyUnit dailyUnit,List<TimeSheetOfDeductionItem> breakTimeList,
    		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily, 
    		Optional<WorkTimeCode> siftCode, 
    		boolean late, boolean leaveEarly, WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
    		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet,Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
    		Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<WorkInformation> beforeInfo, Optional<WorkInformation> afterInfo,
    		Optional<DeductLeaveEarly> deductLeaveEarly,Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets, WorkTimeDivision workTimeDivision) {
		if (workingSystem.isExcludedWorkingCalculate()) {
			/* 計算対象外 */
			return;
		}
		for (int workNumber = 1; workNumber <= attendanceLeavingWork.getTimeLeavingWorks().size(); workNumber++) {
			
			/* 就業内時間帯作成 */
			//打刻はある前提で動く
			val createWithinWorkTimeSheet = WithinWorkTimeSheet.createAsFixed(attendanceLeavingWork.getAttendanceLeavingWork(new WorkNo(workNumber)).get(),
																			  toDay,
																			  predetermineTimeSetForCalc, 
																			  lstHalfDayWorkTimezone,
																			  workTimeCommonSet,
																			  deductionTimeSheet,
																			  bonuspaySetting,
																			  midNightTimeSheet,
																			  workNumber,
																			  coreTimeSetting,
																			  holidayCalcMethodSet,
																			  workTimeCommonSet.getLateEarlySet(),
																			  dailyUnit,breakTimeList,
																			  vacationClass, 
																			  late, 
																				leaveEarly, 
																				workingSystem, 
																				illegularAddSetting, 
																				flexAddSetting, 
																				regularAddSetting, 
																				holidayAddtionSet, 
																				CalcMethodOfNoWorkingDay.isCalculateFlexTime, 
																				Optional.of(new SettingOfFlexWork(new FlexCalcMethodOfHalfWork(new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.OneDay, FlexCalcMethod.OneDay),
																						                                                       new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.OneDay, FlexCalcMethod.OneDay)))), 
																				Optional.of(workTime.getWorkTimeDivision().getWorkTimeDailyAtr()), 
																				siftCode, 
																				new AttendanceTime(1440), 
																				Finally.of(timevacationUseTimeOfDaily),
																				commonSetting,
																				conditionItem,
																				predetermineTimeSetByPersonInfo,
																				deductLeaveEarly,
																				specificDateAttrSheets);
			if(withinWorkingTimeSheet.isPresent()) {
				withinWorkingTimeSheet.get().getWithinWorkTimeFrame().addAll(createWithinWorkTimeSheet.getWithinWorkTimeFrame());
			}
			else {
				withinWorkingTimeSheet.set(createWithinWorkTimeSheet);
			}
			/* 就業外�?時間帯作�? */
			//打刻はある前提で動く
			val createOutSideWorkTimeSheet = OutsideWorkTimeSheet.createOutsideWorkTimeSheet(overTimeHourSetList, fixOff,
					attendanceLeavingWork.getAttendanceLeavingWork(new WorkNo(workNumber)).get(),
					workNumber, overDayEndCalcSet, workTimeCommonSet, holidayTimeWorkItem, beforeDay, toDay, afterDay, workTime,
					workingSystem, breakdownTimeDay, dailyTime, calcSetinIntegre.getOvertimeSetting(), statutorySet, prioritySet
					,bonuspaySetting,midNightTimeSheet,personalInfo,deductionTimeSheet,dailyUnit,holidayCalcMethodSet,createWithinWorkTimeSheet, 
					vacationClass, timevacationUseTimeOfDaily, predetermineTimeSetForCalc, 
					siftCode,  leaveEarly, leaveEarly, illegularAddSetting, flexAddSetting, regularAddSetting, holidayAddtionSet,commonSetting,
					conditionItem,predetermineTimeSetByPersonInfo,coreTimeSetting, beforeInfo, afterInfo, specificDateAttrSheets,workTimeDivision.getWorkTimeDailyAtr()
					);
			if(!outsideWorkTimeSheet.isPresent()) {
				//outsideWorkTimeSheet.set(createOutSideWorkTimeSheet);
				this.outsideWorkTimeSheet = Finally.of(createOutSideWorkTimeSheet);
			}
			else {
				//残業
				if(outsideWorkTimeSheet.get().getOverTimeWorkSheet().isPresent()) {
					List<OverTimeFrameTimeSheetForCalc> addOverList = createOutSideWorkTimeSheet.getOverTimeWorkSheet().isPresent()? createOutSideWorkTimeSheet.getOverTimeWorkSheet().get().getFrameTimeSheets():Collections.emptyList();
					outsideWorkTimeSheet.get().getOverTimeWorkSheet().get().getFrameTimeSheets().addAll(addOverList);
				}
				else {
					this.outsideWorkTimeSheet = Finally.of(new OutsideWorkTimeSheet(createOutSideWorkTimeSheet.getOverTimeWorkSheet(),this.outsideWorkTimeSheet.get().getHolidayWorkTimeSheet()));
				}
				//休�?
				if(outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().isPresent()) {
					List<HolidayWorkFrameTimeSheetForCalc> addHolList = createOutSideWorkTimeSheet.getHolidayWorkTimeSheet().isPresent()? createOutSideWorkTimeSheet.getHolidayWorkTimeSheet().get().getWorkHolidayTime():Collections.emptyList();
					outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().get().getWorkHolidayTime().addAll(addHolList);
				}
				else {
					this.outsideWorkTimeSheet = Finally.of(new OutsideWorkTimeSheet(this.outsideWorkTimeSheet.get().getOverTimeWorkSheet(),createOutSideWorkTimeSheet.getHolidayWorkTimeSheet()));
				}
			}
		}
		
		List<OverTimeFrameTimeSheetForCalc> paramList = new ArrayList<>();
		if(!this.withinWorkingTimeSheet.isPresent()) {
			this.withinWorkingTimeSheet = Finally.of(new WithinWorkTimeSheet(Arrays.asList(new WithinWorkTimeFrame(new EmTimeFrameNo(5), 
																									 new TimeZoneRounding(new TimeWithDayAttr(0), new TimeWithDayAttr(0), new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN, Rounding.ROUNDING_DOWN)), 
																									 new TimeSpanForCalc(new TimeWithDayAttr(0), new TimeWithDayAttr(0)), 
																									 Collections.emptyList(), 
																									 Collections.emptyList(), 
																									 Collections.emptyList(), 
																									 Optional.empty(), 
																									 Collections.emptyList(), 
																									 Optional.empty(), 
																									 Optional.empty())),
																			 Collections.emptyList(),
																			 Optional.empty(),
																			 Optional.empty()));
		}
		if(this.outsideWorkTimeSheet.isPresent()
			&& this.outsideWorkTimeSheet.get().getOverTimeWorkSheet().isPresent()) {
			paramList = this.outsideWorkTimeSheet.get().getOverTimeWorkSheet().get().getFrameTimeSheets();
		}
		val overTimeFrame = forOOtsukaPartMethod(statutorySet, 
							 dailyTime,
							 paramList,
							 calcSetinIntegre.getOvertimeSetting(),
							 breakdownTimeDay,
							 overTimeHourSetList,
							 dailyUnit,
							 holidayCalcMethodSet,
							 vacationClass,
							 timevacationUseTimeOfDaily,
							 afterDay,
							 predetermineTimeSetForCalc,
							 siftCode,
							 leaveEarly,
							 workingSystem,
							 holidayAddtionSet,
							 regularAddSetting,
							 flexAddSetting,
							 illegularAddSetting,
							 leaveEarly,commonSetting,
							 conditionItem,
							 predetermineTimeSetByPersonInfo,coreTimeSetting,workTimeDivision);
		if(!overTimeFrame.isEmpty()) {
			if(outsideWorkTimeSheet.isPresent()) {
				if(outsideWorkTimeSheet.get().getOverTimeWorkSheet().isPresent()) {
					outsideWorkTimeSheet.get().getOverTimeWorkSheet().get().getFrameTimeSheets().addAll(overTimeFrame);
					return;
				}
				//残業追�?
				else {
					this.outsideWorkTimeSheet = Finally.of(new OutsideWorkTimeSheet(Optional.of(new OverTimeSheet(new RaisingSalaryTime(),
						  																					  	  overTimeFrame,
						  																					  	  new SubHolOccurrenceInfo()
																					)),
												this.outsideWorkTimeSheet.get().getHolidayWorkTimeSheet()
												));
				}
			}
			//�?定外インスタンス作�?
			else {
				this.outsideWorkTimeSheet = Finally.of(new OutsideWorkTimeSheet(Optional.of(new OverTimeSheet(new RaisingSalaryTime(),
																										  overTimeFrame,
																										  new SubHolOccurrenceInfo()
							   						 						)),
					   														Optional.of(new HolidayWorkTimeSheet(new RaisingSalaryTime(),
					   																							 Collections.emptyList(), 
					   																							 new SubHolOccurrenceInfo()))
					   														));
			}
		}		
	}

	
	/**
	 * 大塚　固定勤務の流動残業対応(所定内割増を残業時間帯へ移す)
	 * @param statutorySet
	 * @param dailyTime
	 * @param overTimeWorkFrameTimeSheetList
	 * @param autoCalculationSet
	 * @param breakdownTimeDay
	 * @param overTimeHourSetList
	 * @param dailyUnit
	 * @param holidayCalcMethodSet
	 * @param vacationClass
	 * @param timevacationUseTimeOfDaily
	 * @param workType
	 * @param predetermineTimeSet
	 * @param siftCode
	 * @param personalCondition
	 * @param late
	 * @param workingSystem
	 * @param holidayAddtionSet
	 * @param regularAddSetting
	 * @param flexAddSetting
	 * @param illegularAddSetting
	 * @param leaveEarly
	 * @param workTimeDivision 
	 * @return
	 */
	private List<OverTimeFrameTimeSheetForCalc> forOOtsukaPartMethod(LegalOTSetting statutorySet, DailyTime dailyTime, List<OverTimeFrameTimeSheetForCalc> overTimeWorkFrameTimeSheetList, 
									  AutoCalOvertimeSetting autoCalculationSet, BreakDownTimeDay breakdownTimeDay, List<OverTimeOfTimeZoneSet> overTimeHourSetList, 
									  DailyUnit dailyUnit, HolidayCalcMethodSet holidayCalcMethodSet,
									  VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily, WorkType workType, 
									  PredetermineTimeSetForCalc predetermineTimeSet, Optional<WorkTimeCode> siftCode, 
									  boolean late, WorkingSystem workingSystem, HolidayAddtionSet holidayAddtionSet, WorkRegularAdditionSet regularAddSetting, 
									  WorkFlexAdditionSet flexAddSetting, WorkDeformedLaborAdditionSet illegularAddSetting, boolean leaveEarly, Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
									  Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<CoreTimeSetting> coreTimeSetting, WorkTimeDivision workTimeDivision) {
		
		if(!this.withinWorkingTimeSheet.isPresent())
			return Collections.emptyList();
		List<WithinWorkTimeFrame> renewWithinFrame = new ArrayList<>();
		List<OverTimeFrameTimeSheetForCalc> returnList = new ArrayList<>();
		//所定内就業時間枠のループ
		for(WithinWorkTimeFrame timeSheet : this.withinWorkingTimeSheet.get().getWithinWorkTimeFrame()) {
			//割増時間帯が作成されているか確認
			if(timeSheet.getPremiumTimeSheetInPredetermined().isPresent()) {
				
					val newTimeSpanList = timeSheet.timeSheet.getTimeSpan().getNotDuplicationWith(timeSheet.getPremiumTimeSheetInPredetermined().get().getTimeSheet());
					//就業時間枠時間帯と割増時間帯の重なっていない部分で、
					//就業時間枠時間帯を作り直す
					for(TimeSpanForCalc newTimeSpan : newTimeSpanList) {
						renewWithinFrame.add(new WithinWorkTimeFrame(timeSheet.getWorkingHoursTimeNo(),
																	 new TimeZoneRounding(newTimeSpan.getStart(),newTimeSpan.getEnd(),timeSheet.getTimeSheet().getRounding()),
																	 newTimeSpan.getSpan(),
																	 timeSheet.duplicateNewTimeSpan(newTimeSpan),
																	 timeSheet.duplicateNewTimeSpan(newTimeSpan),
																	 timeSheet.getDuplicatedBonusPayNotStatic(timeSheet.getBonusPayTimeSheet(), newTimeSpan),//加給
																	 timeSheet.getMidNightTimeSheet().isPresent()
																	 	?timeSheet.getDuplicateMidNightNotStatic(timeSheet.getMidNightTimeSheet().get(),newTimeSpan)
																	 	:Optional.empty(),//深夜
																	 timeSheet.getDuplicatedSpecBonusPayzNotStatic(timeSheet.getSpecBonusPayTimesheet(), newTimeSpan),//特定日加給
																	 timeSheet.getLateTimeSheet(),
																	 timeSheet.getLeaveEarlyTimeSheet()
											 ));
						
					}
					
					returnList.add(new OverTimeFrameTimeSheetForCalc(new TimeZoneRounding(timeSheet.getPremiumTimeSheetInPredetermined().get().getTimeSheet().getStart(),timeSheet.getPremiumTimeSheetInPredetermined().get().getTimeSheet().getEnd(),null),
																						timeSheet.getPremiumTimeSheetInPredetermined().get().getTimeSheet(),
													  								 Collections.emptyList(),
													  								 Collections.emptyList(),
													  								 Collections.emptyList(),
													  								 Collections.emptyList(),
													  								 Optional.empty(),
													  								 new OverTimeFrameTime(new OverTimeFrameNo(10),
													  										 			   TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
													  										 			   TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
													  										 			   new AttendanceTime(0),
													  										 			   new AttendanceTime(0)),
													  								 StatutoryAtr.Excess,
													  								 false,
													  								 new EmTimezoneNo(0),
													  								 false,
													  								 Optional.empty(),
													  								 Optional.empty()
													  								 ));
				
			}
		}
		//所定内割増時間初期化
		if(this.withinWorkingTimeSheet.isPresent()) {
			this.withinWorkingTimeSheet.get().resetPremiumTimeSheet();
		}
		if(!renewWithinFrame.isEmpty()) {
			this.withinWorkingTimeSheet.get().getWithinWorkTimeFrame().clear();
			this.withinWorkingTimeSheet.get().getWithinWorkTimeFrame().addAll(renewWithinFrame);
		}
		return OverTimeFrameTimeSheetForCalc.diciaionCalcStatutory(statutorySet, 
															dailyTime, 
															returnList, 
															autoCalculationSet, 
															breakdownTimeDay, 
															overTimeHourSetList, 
															dailyUnit, 
															holidayCalcMethodSet, 
															withinWorkingTimeSheet.get(), 
															vacationClass, 
															timevacationUseTimeOfDaily, 
															workType, 
															predetermineTimeSet, 
															siftCode, 
															late, 
															leaveEarly, 
															workingSystem, 
															illegularAddSetting, 
															flexAddSetting, 
															regularAddSetting, 
															holidayAddtionSet,commonSetting,conditionItem,
															predetermineTimeSetByPersonInfo,coreTimeSetting,workTimeDivision.getWorkTimeDailyAtr()
															);
	}

	/**
	 * 加給時間を計算する(就内・残業・休出時間帯の
	 */
	public List<BonusPayTime> calcBonusPayTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,
											   CalAttrOfDailyPerformance calcAtrOfDaily, BonusPayAtr bonusPayAtr) {
		
		List<BonusPayTime> overTimeBonusPay = new ArrayList<>();
		List<BonusPayTime> holidayWorkBonusPay = new ArrayList<>();
		List<BonusPayTime> withinBonusPay = new ArrayList<>();
		if(this.withinWorkingTimeSheet != null && withinWorkingTimeSheet.isPresent())
			withinBonusPay = withinWorkingTimeSheet.get().calcBonusPayTimeInWithinWorkTime(raisingAutoCalcSet,bonusPayAutoCalcSet, bonusPayAtr,calcAtrOfDaily);
		
		if(this.outsideWorkTimeSheet != null && this.outsideWorkTimeSheet.isPresent())
		{
			if(this.outsideWorkTimeSheet.get().getOverTimeWorkSheet().isPresent()) { 
				overTimeBonusPay = outsideWorkTimeSheet.get().getOverTimeWorkSheet().get().calcBonusPayTimeInOverWorkTime(raisingAutoCalcSet, bonusPayAutoCalcSet, bonusPayAtr, calcAtrOfDaily);
			}
			
			if(this.outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().isPresent()) {
				holidayWorkBonusPay = outsideWorkTimeSheet.get().getHolidayWorkTimeSheet().get().calcBonusPayTimeInHolidayWorkTime(raisingAutoCalcSet, bonusPayAutoCalcSet, bonusPayAtr, calcAtrOfDaily);
			}
		}
		return calcBonusPayTime(withinBonusPay,overTimeBonusPay,holidayWorkBonusPay);
	}
	
	/**
	 * 就�??残業�??休�?時間�??特定加給時間の合計を求め�?
	 */
	public List<BonusPayTime> calcSpecBonusPayTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,
												   CalAttrOfDailyPerformance calcAtrOfDaily,BonusPayAtr bonusPayAtr){
		List<BonusPayTime> overTimeBonusPay = new ArrayList<>();
		List<BonusPayTime> holidayWorkBonusPay = new ArrayList<>();
		List<BonusPayTime> withinBonusPay = new ArrayList<>();
		
		if(withinWorkingTimeSheet.isPresent())
			 withinBonusPay = withinWorkingTimeSheet.get().calcSpecifiedBonusPayTimeInWithinWorkTime(raisingAutoCalcSet,bonusPayAutoCalcSet, bonusPayAtr,calcAtrOfDaily);

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
	 * 就・残�?休�?�?給時間を合計す�?
	 * @param withinBonusPay
	 * @param overTimeBonusPay
	 * @param holidayWorkBonusPay
	 * @return�?合計後�?�?算時�?(Noでユニ�?ク)
	 */
	private List<BonusPayTime> calcBonusPayTime(List<BonusPayTime> withinBonusPay ,
								   List<BonusPayTime> overTimeBonusPay ,
								   List<BonusPayTime> holidayWorkBonusPay){
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
	 * 受け取っ�?2つの�?給時間が持つ時間を合�?
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
	 * �?��間�?合計を算�?
	 * @param bonusPayList�?�?給時間のリス�?
	 * @param bonusPayNo�?�?給時間?�??
	 * @return�?合計時間�?�?給時間
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
	 * 受け取った加給時間?�?�を持つ�?給時間を取�?
	 * @param bonusPayTime �?給時間
	 * @param bonusPayNo�?�?給時間?�??
	 * @return�?�?給時間リス�?
	 */
	private List<BonusPayTime> getByBonusPayNo(List<BonusPayTime> bonusPayTime,int bonusPayNo){
		return bonusPayTime.stream().filter(tc -> tc.getBonusPayTimeItemNo() == bonusPayNo).collect(Collectors.toList());
	}
	
	
	/**
	 * 控除時間を取�?
	 * @param dedClassification 
	 * @param dedAtr
	 * @param statutoryAtrs
	 * @param pertimesheet
	 * @return
	 */
	public TimeWithCalculation calcWithinTotalTime(ConditionAtr dedClassification, DeductionAtr dedAtr,StatutoryAtr statutoryAtr,TimeSheetRoundingAtr pertimesheet
			,PremiumAtr premiumAtr,HolidayCalcMethodSet holidayCalcMethodSet,Optional<WorkTimezoneCommonSet> commonSetting) {
		if(statutoryAtr.isStatutory()) {
			if(this.withinWorkingTimeSheet.isPresent()) {
				return TimeWithCalculation.sameTime(this.withinWorkingTimeSheet.get().calculationAllFrameDeductionTime(dedAtr, dedClassification,premiumAtr,holidayCalcMethodSet,commonSetting));
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
	 * フレ�?��スの時間帯作�?
	 * @param ootsukaIWFlag 
	 * @param integrationOfDaily 
	 * @param integrationOfDaily 
	 */
	 public void createTimeSheetAsFlex(
			 		WorkingSystem workingSystem, PredetermineTimeSetForCalc predetermineTimeSetForCalc,
					Optional<BonusPaySetting> bonuspaySetting,
					List<HDWorkTimeSheetSetting> fixOff,List<OverTimeOfTimeZoneSet> overTimeHourSetList,List<HolidayWorkFrameTimeSheet> holidayTimeWorkItem,  
					Optional<ZeroTime> overDayEndCalcSet,WorkType beforeDay, WorkType toDay, WorkType afterDay,
					BreakDownTimeDay breakdownTimeDay, DailyTime dailyTime, CalAttrOfDailyPerformance calcSetinIntegre,
					LegalOTSetting statutorySet, StatutoryPrioritySet prioritySet,
					WorkTimeSetting workTime,
					FlexWorkSetting flexWorkSetting,Optional<OutingTimeOfDailyPerformance> goOutTimeSheetList,
					TimeSpanForCalc oneDayTimeSpan,TimeLeavingOfDailyPerformance attendanceLeaveWork,WorkTimeDivision workTimeDivision
					,List<BreakTimeOfDailyPerformance> breakTimeOfDailyList,MidNightTimeSheet midNightTimeSheet,DailyCalculationPersonalInformation personalInfo,
					HolidayCalcMethodSet holidayCalcMethodSet,Optional<CoreTimeSetting> coreTimeSetting,DailyUnit dailyUnit,List<TimeSheetOfDeductionItem> breakTimeList,
            		VacationClass vacationClass, TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
            		Optional<WorkTimeCode> siftCode, 
            		boolean late, boolean leaveEarly, WorkDeformedLaborAdditionSet illegularAddSetting, WorkFlexAdditionSet flexAddSetting, 
            		WorkRegularAdditionSet regularAddSetting, HolidayAddtionSet holidayAddtionSet,Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
            		Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,List<ShortWorkingTimeSheet> shortTimeSheets,
            		WorkTimezoneShortTimeWorkSet workTimeShortTimeSet,Optional<WorkInformation> beforeInfo, Optional<WorkInformation> afterInfo,
            		List<EmTimeZoneSet> fixWoSetting,Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets){

		 //控除時間帯の作�?
		 val deductionTimeSheet = provisionalDeterminationOfDeductionTimeSheet(goOutTimeSheetList,
				 oneDayTimeSpan, attendanceLeaveWork, workTimeDivision,breakTimeOfDailyList,flexWorkSetting.getOffdayWorkTime().getRestTimezone(),flexWorkSetting.getRestSetting(), shortTimeSheets,
				 workTimeShortTimeSet,commonSetting,holidayCalcMethodSet,predetermineTimeSetForCalc,toDay,fixWoSetting);
		 /*固定勤務�?時間帯作�?*/
		 val fixedWorkTimeZoneSet = new CommonFixedWorkTimezoneSet();
		 fixedWorkTimeZoneSet.forFlex(flexWorkSetting.getLstHalfDayWorkTimezone());
		 Optional<DeductLeaveEarly> leaveLateSet = flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()
				 								 ?Optional.of(flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly())
				 								 :Optional.empty();
		 theDayOfWorkTimesLoop( workingSystem,  predetermineTimeSetForCalc,
				 	fixedWorkTimeZoneSet,  flexWorkSetting.getCommonSetting(),  bonuspaySetting,
					overTimeHourSetList,  fixOff,  overDayEndCalcSet,
					holidayTimeWorkItem,  beforeDay,  toDay,  afterDay,
					 breakdownTimeDay,  dailyTime,  calcSetinIntegre,
					 statutorySet,  prioritySet,
					 deductionTimeSheet,  workTime,midNightTimeSheet,personalInfo,holidayCalcMethodSet,coreTimeSetting,dailyUnit,breakTimeList,
					 vacationClass, timevacationUseTimeOfDaily, siftCode, 
					  leaveEarly, leaveEarly, illegularAddSetting, flexAddSetting, regularAddSetting, holidayAddtionSet
					 ,commonSetting,conditionItem,predetermineTimeSetByPersonInfo,beforeInfo,afterInfo,leaveLateSet,specificDateAttrSheets,workTimeDivision);
		 /*コアタイ�?のセ�?��*/
		 //this.withinWorkingTimeSheet.set(withinWorkingTimeSheet.get().createWithinFlexTimeSheet(flexWorkSetting.getCoreTimeSetting()));
		 if(this.withinWorkingTimeSheet.isPresent())
			 this.withinWorkingTimeSheet = Finally.of(withinWorkingTimeSheet.get().createWithinFlexTimeSheet(flexWorkSetting.getCoreTimeSetting(),deductionTimeSheet));
	 }
	
//	 /**
//	 * 流動休�?用の控除時間帯作�?
//	 */
//	 public void createFluidBreakTime(DeductionAtr deductionAtr) {
//	 DeductionTimeSheet.createDedctionTimeSheet(acqAtr, setMethod,
//	 clockManage, dailyGoOutSheet, oneDayRange, CommonSet,
//	 attendanceLeaveWork, fixedCalc, workTimeDivision, noStampSet, fixedSet,
//	 breakTimeSheet);
//	
//	 }

	// ?�＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊�?
	//
	// /**
	// * 流動勤務�?時間帯作�?
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
	// //�?定時間設定をコピ�?して計算用の�?定時間設定を作�?する
	// this.predetermineTimeSetForCalc = new PredetermineTimeSetForCalc(
	// predetermineTimeSet.getAdditionSet(),
	// predetermineTimeSet.getSpecifiedTimeSheet().getTimeSheets(),
	// predetermineTimeSet.getSpecifiedTimeSheet().getAMEndTime(),
	// predetermineTimeSet.getSpecifiedTimeSheet().getPMStartTime());
	// //出�?勤�?��ー�?
	// for(AttendanceLeavingWork attendanceLeavingWork :
	// attendanceLeavingWork.getAttendanceLeavingWork(workNo)) {
	// //事前に�?��早�?、控除時間帯を取得す�?
	// this.getForDeductionTimeSheetList(workNo, attendanceLeavingWork,
	// predetermineTimeSet, deductionTimeSheet ,workInformationOfDaily,
	// workType, withinWorkTimeFrame);
	// }
	// //「�?勤系」か「休�?系」か判断する
	// boolean isWeekDayAttendance = worktype.isWeekDayAttendance();
	// //時間休暇�?算残時間未割当�?時間休暇�?算残時�?
	//
	// if(isWeekDayAttendance) {//出勤系の場�?
	// //流動勤務（就�??�平日??
	// WithinWorkTimeSheet newWithinWorkTimeSheet =
	// withinWorkTimeSheet.createAsFluidWork(predetermineTimeSetForCalc,
	// worktype, workInformationOfDaily, fluidWorkSetting, deductionTimeSheet);
	// //流動勤務（就外�?�平日??
	//
	// }else{//休�?系の場�?
	// //流動勤務（休日出勤??
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
	// * 事前に�?��早�?、控除時間帯を取得す�?
	// * @param workNo
	// * @param attendanceLeavingWork 出�?勤
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
	// //�?定時間帯を取得す�?(流動計算で使用する�?定時間�?作�?)
	// createPredetermineTimeSheetForFluid(workNo, predetermineTimeSet,
	// workType, workInformationOfDaily);
	// //計算�?��を判断する
	// withinWorkTimeFrame.createWithinWorkTimeFrameForFluid(attendanceLeavingWork,
	// dailyWork, predetermineTimeSetForCalc);
	// //�?��時間帯を控除
	// withinWorkTimeFrame.getLateTimeSheet().lateTimeCalcForFluid(withinWorkTimeFrame,
	// lateRangeForCalc, workTimeCommonSet, lateDecisionClock,
	// deductionTimeSheet);
	// //控除時間帯の仮確�?
	// this.provisionalDeterminationOfDeductionTimeSheet(deductionTimeSheet);
	// //早�?時間帯を控除
	//
	// //勤務間の休�?設定を取�?
	//
	// }
	//
	// /**
	// * 計算用�?定時間設定を作�?する?�流動用??
	// * @return
	// */
	// public void createPredetermineTimeSheetForFluid(
	// int workNo,
	// PredetermineTimeSet predetermineTimeSet,
	// WorkType workType,
	// WorkInformationOfDaily workInformationOfDaily) {
	//
	// //予定と実績が同じ勤務かど�?��確�?
	// if(workInformationOfDaily.isMatchWorkInfomation()/*予定時間帯に値が�?って�?��か�?チェ�?��を追�?する�?��あ�?*/)
	// {
	// //予定時間帯を取得す�?
	// ScheduleTimeSheet scheduleTimeSheet =
	// workInformationOfDaily.getScheduleTimeSheet(workNo);
	// //�?定時間帯設定�?時間帯を�?て取得す�?
	// List<TimeSheetWithUseAtr> timeSheetList =
	// predetermineTimeSet.getSpecifiedTimeSheet().getTimeSheets();
	// //変更対象の時間帯を取�?
	// List<TimeSheetWithUseAtr> list = timeSheetList.stream().filter(ts ->
	// ts.getCount()==workNo).collect(Collectors.toList());
	// TimeSheetWithUseAtr timeSheet = list.get(0);
	// //予定時間帯と変更対象の時間帯を基に時間帯を作�?
	// TimeSheetWithUseAtr targetTimeSheet = new TimeSheetWithUseAtr(
	// timeSheet.getUseAtr(),
	// scheduleTimeSheet.getAttendance(),
	// scheduleTimeSheet.getLeaveWork(),
	// workNo);
	// //変更対象以外�?時間帯を取�?
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
	// //午前勤務�?�午後勤務�?場合に時間帯を補正する処�?
	// this.predetermineTimeSetForCalc.getPredetermineTimeSheet().correctPredetermineTimeSheet(workType.getDailyWork());
	// }
	//
	//
	 /**
	 * 控除時間帯の仮確�?
	 */
	 public static DeductionTimeSheet provisionalDeterminationOfDeductionTimeSheet(Optional<OutingTimeOfDailyPerformance> goOutTimeSheetList,
				TimeSpanForCalc oneDayTimeSpan,TimeLeavingOfDailyPerformance attendanceLeaveWork,WorkTimeDivision workTimeDivision
				,List<BreakTimeOfDailyPerformance> breakTimeOfDailyList,FlowWorkRestTimezone flowRestTimezone,FlowWorkRestSetting flowRestSetting,
				List<ShortWorkingTimeSheet> shortTimeSheets,WorkTimezoneShortTimeWorkSet workTimeShortTimeSet,Optional<WorkTimezoneCommonSet> commonSetting
				,HolidayCalcMethodSet holidayCalcMethodSet,PredetermineTimeSetForCalc predetermineTimeSetForCalc,WorkType worktype,List<EmTimeZoneSet> fixWoSetting) {
		 //控除用
		 val dedTimeSheet = DeductionTimeSheet.provisionalDecisionOfDeductionTimeSheet(DeductionAtr.Deduction, goOutTimeSheetList,
				 oneDayTimeSpan, attendanceLeaveWork, workTimeDivision,breakTimeOfDailyList,flowRestTimezone,flowRestSetting, shortTimeSheets,
				 workTimeShortTimeSet,commonSetting,holidayCalcMethodSet,predetermineTimeSetForCalc,worktype,fixWoSetting);
	 	 //計上用
	 	 val recordTimeSheet = DeductionTimeSheet.provisionalDecisionOfDeductionTimeSheet(DeductionAtr.Appropriate, goOutTimeSheetList,
	 			 oneDayTimeSpan, attendanceLeaveWork, workTimeDivision,breakTimeOfDailyList,flowRestTimezone,flowRestSetting, shortTimeSheets,
	 			 workTimeShortTimeSet,commonSetting,holidayCalcMethodSet,predetermineTimeSetForCalc,worktype,fixWoSetting);
	 
	 	return new DeductionTimeSheet(dedTimeSheet,recordTimeSheet,breakTimeOfDailyList,goOutTimeSheetList,shortTimeSheets); 
	 }
	 
	 /**
	  * 大塚モード使用時専用の��?��、早��?削除処��?
	  * 大塚モード使用時専用の�?��、早�?削除処�?
	  * 大塚モード使用時専用の遅刻、早退削除処理
	  */
	 public void cleanLateLeaveEarlyTimeForOOtsuka() {
		 if(this.getWithinWorkingTimeSheet() != null
			&& this.getWithinWorkingTimeSheet().isPresent()){
			 this.withinWorkingTimeSheet.get().cleanLateLeaveEarlyTimeForOOtsuka();
		 }
	 }
	 
	 public void clearLeavingTime() {
		 this.attendanceLeavingWork = new TimeLeavingOfDailyPerformance(this.getAttendanceLeavingWork().getEmployeeId(), new WorkTimes(0), Collections.emptyList(), this.getAttendanceLeavingWork().getYmd());
	 }
	 

	 
}
