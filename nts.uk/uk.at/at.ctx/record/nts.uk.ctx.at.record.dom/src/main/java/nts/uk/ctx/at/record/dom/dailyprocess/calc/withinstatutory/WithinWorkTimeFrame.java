package nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.uk.ctx.at.record.dom.MidNightTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ConditionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionOffSetTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.SpecBonusPayTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.LateEarlyAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.OtherEmTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.TimeSheet;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 就業時間内時間枠
 * @author keisuke_hoshina
 *
 */
@Getter
public class WithinWorkTimeFrame extends CalculationTimeSheet{// implements LateLeaveEarlyManagementTimeFrame {

	private final EmTimeFrameNo workingHoursTimeNo;
	
	//遅刻時間帯・・・deductByLateLeaveEarlyを呼ぶまでは値が無い
	private Optional<LateTimeSheet> lateTimeSheet;
	//早退時間帯・・・deductByLateLeaveEarlyを呼ぶまでは値が無い
	private Optional<LeaveEarlyTimeSheet> leaveEarlyTimeSheet;
	//計算用所定内割増時間
	@Setter
	private Optional<WithinPremiumTimeSheetForCalc> premiumTimeSheetInPredetermined;
	
	
	/**
	 * constructor
	 * @param workingHoursTimeNo
	 * @param timeSheet
	 * @param calculationTimeSheet
	 */
	public WithinWorkTimeFrame(
			EmTimeFrameNo workingHoursTimeNo,
			TimeZoneRounding timeSheet,
			TimeSpanForCalc calculationTimeSheet,
			List<TimeSheetOfDeductionItem> recorddeductionTimeSheets,
			List<TimeSheetOfDeductionItem> deductionTimeSheets,
			List<BonusPayTimeSheetForCalc> bonusPayTimeSheet,
			Optional<MidNightTimeSheetForCalc> midNighttimeSheet,
			List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet,
			Optional<LateTimeSheet> lateTimeSheet,
			Optional<LeaveEarlyTimeSheet> leaveEarlyTimeSheet) {
		
		super(timeSheet, calculationTimeSheet,recorddeductionTimeSheets,deductionTimeSheets,bonusPayTimeSheet,specifiedBonusPayTimeSheet,midNighttimeSheet);
		this.workingHoursTimeNo = workingHoursTimeNo;
		this.lateTimeSheet = lateTimeSheet;
		this.leaveEarlyTimeSheet = leaveEarlyTimeSheet;
		this.premiumTimeSheetInPredetermined = Optional.empty();
	}
	

	
	public TimeZoneRounding getTimeSheet() {
		return this.timeSheet;
	}
	
	
	
	/**
	 * 遅刻時間帯の作成
	 * @param workTime
	 * @param goWorkTime
	 * @param workNo
	 * @param workTimeCommonSet
	 * @param withinWorkTimeSheet
	 * @param deductionTimeSheet
	 * @param graceTimeSetting
	 * @param workTimeCalcMethodDetailOfHoliday
	 */
	public static LateTimeSheet createLateTimeSheet(
			TimeLeavingWork timeLeavingWork
			,OtherEmTimezoneLateEarlySet otherEmTimezoneLateEarlySet
			,Optional<LateDecisionClock> lateDesClock
			,WithinWorkTimeFrame duplicateTimeSheet
			,DeductionTimeSheet deductionTimeSheet
			,int workNo
			,Optional<TimezoneUse> optional
			,Optional<CoreTimeSetting> coreTimeSetting,List<TimeSheetOfDeductionItem> breakTimeList,WorkType workType,PredetermineTimeSetForCalc predetermineTimeForSet) {

		
		//遅刻時間帯の作成
		LateTimeSheet latetimesheet = LateTimeSheet.createLateTimeSheet(
				lateDesClock,
				timeLeavingWork,
				otherEmTimezoneLateEarlySet.getGraceTimeSet(),
				duplicateTimeSheet,
				deductionTimeSheet,
				coreTimeSetting,
				optional,
				workNo,breakTimeList,workType,predetermineTimeForSet);
	
		return latetimesheet;
		
	}
	
	/**
	 * 早退時間帯の作成
	 * @param workTime
	 * @param goWorkTime
	 * @param workNo
	 * @param workTimeCommonSet
	 * @param withinWorkTimeSheet
	 * @param deductionTimeSheet
	 * @param graceTimeSetting
	 * @param workTimeCalcMethodDetailOfHoliday
	 */
	public static LeaveEarlyTimeSheet createLeaveEarlyTimeSheet(
			TimeLeavingWork timeLeavingWork
			,OtherEmTimezoneLateEarlySet otherEmTimezoneLateEarlySet
			,Optional<LeaveEarlyDecisionClock> leaveEarlyDesClock
			,WithinWorkTimeFrame duplicateTimeSheet
			,DeductionTimeSheet deductionTimeSheet
			,int workNo
			,Optional<TimezoneUse> optional
			,Optional<CoreTimeSetting> coreTimeSetting,List<TimeSheetOfDeductionItem> breakTimeList
			,WorkType workType,PredetermineTimeSetForCalc predetermineTimeForSet) {

		
		//早退時間帯の作成
		LeaveEarlyTimeSheet leaveEarlytimesheet = LeaveEarlyTimeSheet.createLeaveEarlyTimeSheet(
				leaveEarlyDesClock,
				timeLeavingWork,
				otherEmTimezoneLateEarlySet.getGraceTimeSet(),
				duplicateTimeSheet,
				deductionTimeSheet,
				coreTimeSetting,
				optional,
				workNo,breakTimeList,workType,predetermineTimeForSet);
	
		return leaveEarlytimesheet;
		
	}


//
//	@Override
//	public LateTimeSheet getLateTimeSheet() {
//		return this.lateTimeSheet.get();
//	}
//
//	@Override
//	public LeaveEarlyTimeSheet getLeaveEarlyTimeSheet() {
//		return this.leaveEarlyTimeSheet.get();
//	}
	
	/**
	 * 実働時間を求め、就業時間を計算する
	 * @param holidayAdditionAtr
	 * @param dedTimeSheet
	 * @return
	 */
	public AttendanceTime calcActualWorkTimeAndWorkTime(HolidayAdditionAtr holidayAdditionAtr,
														TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
														WorkingSystem workingSystem,
														WorkRegularAdditionSet addSettingOfRegularWork,
														WorkDeformedLaborAdditionSet addSettingOfIrregularWork, 
														WorkFlexAdditionSet addSettingOfFlexWork,
														HolidayAddtionSet holidayAddtionSet,
														boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
														boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
														HolidayCalcMethodSet holidayCalcMethodSet,
														nts.uk.ctx.at.shared.dom.PremiumAtr premiumAtr,Optional<WorkTimezoneCommonSet> commonSetting
														) {
		AttendanceTime actualTime = calcActualTime();
		AttendanceTime dedAllTime = new AttendanceTime(0);
		val dedTimeSheets = this.deductionTimeSheet;
		if(!dedTimeSheets.isEmpty()) {
			dedAllTime = new AttendanceTime(dedTimeSheets.stream()
									  					 .map(tc -> tc.calcTotalTime().valueAsMinutes())
									  					 .collect(Collectors.summingInt(tc -> tc)));
		}
		if(dedAllTime.greaterThan(0)) {
			actualTime = actualTime.minusMinutes(dedAllTime.valueAsMinutes());
		}
		AttendanceTime workTime = calcWorkTime(actualTime);
		/*就業時間算出ロジックをここに*/
		
		//控除時間の内、時間休暇で相殺した時間を計算
		DeductionOffSetTime timeVacationOffSetTime = //(dedTimeSheet.isPresent())
													 // ?dedTimeSheet.get().calcTotalDeductionOffSetTime(lateTimeOfDaily,lateTimeSheet,leaveEarlyTimeOfDaily,leaveEarlyTimeSheet)
													  //控除時間帯が存在する前提で動いていたため、控除時間帯が無かったらオールゼロで修正
												     // :new DeductionOffSetTime(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0));
													 new DeductionOffSetTime(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0));

		//遅刻、早退時間を就業時間から控除
		if(jugmentDeductLateEarly(premiumAtr,holidayCalcMethodSet,commonSetting)) {
			//遅刻控除時間を計算
			int lateDeductTime = 0;
			if(this.lateTimeSheet.isPresent()) {
				lateDeductTime = this.lateTimeSheet.get().calcDedctionTime(late,NotUseAtr.USE).getTime().valueAsMinutes();
			}
			//早退控除時間を計算
			int leaveEarlyDeductTime = 0;
			if(this.leaveEarlyTimeSheet.isPresent()) {
				leaveEarlyDeductTime = this.leaveEarlyTimeSheet.get().calcDedctionTime(leaveEarly,NotUseAtr.USE).getTime().valueAsMinutes();
			}
			int lateLeaveEarlySubtraction = lateDeductTime + leaveEarlyDeductTime;
			workTime = new AttendanceTime(workTime.valueAsMinutes() - lateLeaveEarlySubtraction);
		}
		
		//時間休暇使用の残時間を計算 
		//timevacationUseTimeOfDaily.subtractionDeductionOffSetTime(timeVacationOffSetTime);
		//就業時間に加算する時間休暇を就業時間へ加算     
		workTime = new AttendanceTime(workTime.valueAsMinutes() + calcTimeVacationAddTime(holidayAddtionSet,
																						  getCalculationByActualTimeAtr(workingSystem,
																													  	addSettingOfRegularWork,
																													  	addSettingOfIrregularWork,
																													  	addSettingOfFlexWork),
																  						  timeVacationOffSetTime).valueAsMinutes());
		return workTime;
	}
	
	/**
	 * 就業時間を計算する
	 * @param actualTime
	 * @return　就業時間
	 */
	public AttendanceTime calcWorkTime(AttendanceTime actualTime) {
		return actualTime;
	}
	
	/**
	 * 実働時間を計算する
	 * @return　実働時間
	 */
	public AttendanceTime calcActualTime() {
//		return new AttendanceTime(((CalculationTimeSheet)this).getCalcrange().lengthAsMinutes());	
//		TimeZoneRounding a = ((CalculationTimeSheet)this).getTimeSheet();
//		TimeSpanForCalc b = a.timeSpan();
		return new AttendanceTime(((CalculationTimeSheet)this).getTimeSheet().timeSpan().lengthAsMinutes());
	}

	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊↓高須
	
//	/**
//	 * 計算範囲を判断（流動）　　流動時の就業時間内時間枠
//	 */
//	public void createWithinWorkTimeFrameForFluid(
//			AttendanceLeavingWork attendanceLeavingWork,
//			DailyWork dailyWork,
//			PredetermineTimeSetForCalc predetermineTimeSetForCalc) {
//		TimeSpanForCalc timeSheet = new TimeSpanForCalc(
//				attendanceLeavingWork.getAttendance().getEngrave().getTimesOfDay(),
//				attendanceLeavingWork.getLeaveWork().getEngrave().getTimesOfDay());
//		this.correctTimeSheet(dailyWork, timeSheet, predetermineTimeSetForCalc);
//	}
//	
//	/**
//	 * 勤務の単位を基に時間帯の開始、終了を補正
//	 * @param dailyWork 1日の勤務
//	 */
//	public WithinWorkTimeFrame correctTimeSheet(
//			DailyWork dailyWork,
//			TimeSpanForCalc timeSheet,
//			PredetermineTimeSetForCalc predetermineTimeSetForCalc) {
//		
//		//丸め設定を作成
//		Finally<TimeRoundingSetting> rounding = Finally.of(new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN,Rounding.ROUNDING_DOWN));
//		
//		if (dailyWork.getAttendanceHolidayAttr().isHalfDayWorking()) {
//			TimeSpanForCalc timeSheetForRounding = this.getHalfDayWorkingTimeSheetOf(dailyWork.getAttendanceHolidayAttr(),timeSheet,predetermineTimeSetForCalc);
//			TimeSpanWithRounding calcRange = new TimeSpanWithRounding(timeSheetForRounding.getStart(),timeSheetForRounding.getEnd(),rounding);
//			CalculationTimeSheet t = new CalculationTimeSheet(calcRange,timeSheetForRounding,Optional.empty());
//			return new WithinWorkTimeFrame(workingHoursTimeNo,timeSheet,t);
//		}
//	}
	
	/**
	 *　指定条件の控除項目だけの控除時間
	 * @param forcsList
	 * @param atr
	 * @return
	 */
	public AttendanceTime forcs(ConditionAtr atr,DeductionAtr dedAtr){
		AttendanceTime dedTotalTime = new AttendanceTime(0);
		val loopList = (dedAtr.isAppropriate())?this.getRecordedTimeSheet():this.deductionTimeSheet;
		for(TimeSheetOfDeductionItem deduTimeSheet: loopList) {
			if(deduTimeSheet.checkIncludeCalculation(atr)) {
				val addTime = deduTimeSheet.calcTotalTime().valueAsMinutes();
				dedTotalTime = dedTotalTime.addMinutes(addTime);
			}
		}
		return dedTotalTime;
	}
	
//	
//	/**
//	 * 控除時間中の時間休暇相殺時間の計算
//	 * @return
//	 */
//	public DeductionOffSetTime calcTotalDeductionOffSetTime() {
//		
//		//2017/10/13はここからスタート
//		
//		
//	}
	
	/**
	 * 時間休暇からの加算時間を取得
	 * @author ken_takasu
	 * 
	 * @return
	 */
	public AttendanceTime calcTimeVacationAddTime(HolidayAddtionSet holidayAddtionSet,
												  CalcurationByActualTimeAtr calculationByActualTimeAtr,
												  DeductionOffSetTime timeVacationOffSetTime
												  ) {
		AttendanceTime addTime = new AttendanceTime(0);
		//実働のみで計算するかチェックする
		if(calculationByActualTimeAtr.isCalclationByActualTime()) {
			//年休分を加算するかチェックする
			if(holidayAddtionSet.getAdditionVacationSet().getAnnualHoliday()==NotUseAtr.USE) {
				addTime = new AttendanceTime(addTime.valueAsMinutes() + timeVacationOffSetTime.getAnnualLeave().valueAsMinutes());
			}
			//積立年休分を含めて求めるかチェックする
			if(holidayAddtionSet.getAdditionVacationSet().getYearlyReserved()==NotUseAtr.USE) {
				addTime = new AttendanceTime(addTime.valueAsMinutes() + timeVacationOffSetTime.getRetentionYearly().valueAsMinutes());
			}
			//特別休暇分を含めて求めるかチェックする
			if(holidayAddtionSet.getAdditionVacationSet().getSpecialHoliday()==NotUseAtr.USE) {
				addTime = new AttendanceTime(addTime.valueAsMinutes() + timeVacationOffSetTime.getSpecialHoliday().valueAsMinutes());
			}
		}
		return addTime;
	}
	
	/**
	 * 時間休暇相殺時間を修行時間に含めるか判断する
	 * @author ken_takasu
	 * @param workingSystem
	 * @param addSettingOfRegularWork
	 * @param addSettingOfIrregularWork
	 * @param addSettingOfFlexWork
	 * @return
	 */
	public CalcurationByActualTimeAtr getCalculationByActualTimeAtr(WorkingSystem workingSystem,
																	WorkRegularAdditionSet addSettingOfRegularWork,
																	WorkDeformedLaborAdditionSet addSettingOfIrregularWork, 
																	WorkFlexAdditionSet addSettingOfFlexWork) {
		
		switch (workingSystem) {
		case REGULAR_WORK:
			if(addSettingOfRegularWork == null){
				return CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME;
			}
			return addSettingOfRegularWork.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation();
			
		case FLEX_TIME_WORK:
			if(addSettingOfFlexWork == null) {
				return CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME;
			}
			return addSettingOfFlexWork.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getCalculateActualOperation();

		case VARIABLE_WORKING_TIME_WORK:
			if(addSettingOfIrregularWork == null) {
				return CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME;
			}
			return addSettingOfIrregularWork.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation();

		case EXCLUDED_WORKING_CALCULATE:
			return CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME;
		default:
			throw new BusinessException(new RawErrorMessage("不正な労働制です"));
		}
	}

	/**
	 * 就内時間枠を作る
	 * @param duplicateTimeSheet
	 * @param deductionTimeSheet
	 * @param bonusPaySetting
	 * @param midNightTimeSheet
	 * @return
	 */
	public static WithinWorkTimeFrame createWithinWorkTimeFrame(WithinWorkTimeFrame duplicateTimeSheet,
																DeductionTimeSheet deductionTimeSheet,
																BonusPaySetting bonusPaySetting,
																MidNightTimeSheet midNightTimeSheet,
																Optional<LateDecisionClock> lateDesClock,
																Optional<LeaveEarlyDecisionClock> leaveEarlyDesClock,
																TimeLeavingWork timeLeavingWork,
																HolidayCalcMethodSet holidayCalcMethodSet,
																int workNo,
																WorkTimezoneLateEarlySet workTimezoneLateEarlySet,
																Optional<TimezoneUse> optional,
																Optional<CoreTimeSetting> coreTimeSetting,List<TimeSheetOfDeductionItem> breakTimeList
																,WorkType workType,PredetermineTimeSetForCalc predetermineTimeForSet,
																Optional<WorkTimezoneCommonSet> commonSetting) {
		
		EmTimeZoneSet dupTimeSheet = new EmTimeZoneSet(duplicateTimeSheet.getWorkingHoursTimeNo(),   
													   new TimeZoneRounding(duplicateTimeSheet.getTimeSheet().getStart(),
															   				duplicateTimeSheet.getTimeSheet().getEnd(),
															   				duplicateTimeSheet.getTimeSheet().getRounding()));		
		//遅刻時間帯の作成
  		LateTimeSheet lateTimeSheet = createLateTimeSheet(timeLeavingWork,
                   workTimezoneLateEarlySet.getOtherEmTimezoneLateEarlySet(LateEarlyAtr.LATE),
                   lateDesClock,
                   duplicateTimeSheet,
                   deductionTimeSheet,
                   workNo,
                   optional,
                   coreTimeSetting,breakTimeList,workType,predetermineTimeForSet);
  		//遅刻時間を計算する
  		AttendanceTime lateDeductTime = lateTimeSheet.getForDeducationTimeSheet().isPresent()?lateTimeSheet.getForDeducationTimeSheet().get().calcTotalTime():new AttendanceTime(0);  	
  		if(lateDesClock.isPresent()) {//←のifはフレックスの最低勤務時間利用の場合に下記処理を走らせたくない為追加
  	  		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
  	  			//就業時間内時間帯から控除するか判断し控除する      
  	   			if(!holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().decisionLateDeductSetting(lateDeductTime,
  	   																													   workTimezoneLateEarlySet.getOtherEmTimezoneLateEarlySet(LateEarlyAtr.LATE).getGraceTimeSet(),
  	   																													   commonSetting)) {
  	   				TimeWithDayAttr test1 = predetermineTimeForSet.getTimeSheets(workType.getAttendanceHolidayAttr(), workNo).get().getStart();
  	   				if(coreTimeSetting.isPresent()&&coreTimeSetting.get().isUseTimeSheet()) {	   				
  	   					test1 = getDecisionCoreTimeSheet(predetermineTimeForSet,coreTimeSetting.get(),workType).getStartTime();
  	   				}
  	   				//遅刻時間帯の終了時刻を開始時刻にする
  	    			dupTimeSheet = new EmTimeZoneSet(duplicateTimeSheet.getWorkingHoursTimeNo(), 
  	             									 new TimeZoneRounding(lateTimeSheet.getForDeducationTimeSheet().isPresent()?lateTimeSheet.getForDeducationTimeSheet().get().getTimeSheet().getStart()
//  	                                    																						:duplicateTimeSheet.getTimeSheet().getStart(),
  	             											 																	:test1,
  	                      							 duplicateTimeSheet.getTimeSheet().getEnd(),
  	                      							 duplicateTimeSheet.getTimeSheet().getRounding()));
  	   			}
  	  		}
  		}
  		//早退時間帯の作成
  		LeaveEarlyTimeSheet LeaveEarlyTimeSheet = createLeaveEarlyTimeSheet(timeLeavingWork,
                     workTimezoneLateEarlySet.getOtherEmTimezoneLateEarlySet(LateEarlyAtr.EARLY),
                     leaveEarlyDesClock,
                     duplicateTimeSheet,
                     deductionTimeSheet,
                     workNo,
                     optional,
                     coreTimeSetting, breakTimeList,workType,predetermineTimeForSet);  
  		//早退時間を計算する
  		AttendanceTime LeaveEarlyDeductTime = LeaveEarlyTimeSheet.getForDeducationTimeSheet().isPresent()?LeaveEarlyTimeSheet.getForDeducationTimeSheet().get().calcTotalTime():new AttendanceTime(0);
  		if(leaveEarlyDesClock.isPresent()) {//←のifはフレックスの最低勤務時間利用の場合に下記処理を走らせたくない為追加
  	  		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
  	  			//就業時間内時間帯から控除するか判断し控除する
  	  	  		if(!holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().decisionLateDeductSetting(LeaveEarlyDeductTime, 
  	  	  																												   workTimezoneLateEarlySet.getOtherEmTimezoneLateEarlySet(LateEarlyAtr.EARLY).getGraceTimeSet(),
  	  	  																												   commonSetting)) {
  	  	  			TimeWithDayAttr test2 = predetermineTimeForSet.getTimeSheets(workType.getAttendanceHolidayAttr(), workNo).get().getEnd();
  	  	  			if(coreTimeSetting.isPresent()&&coreTimeSetting.get().isUseTimeSheet()) {	   				
	   					test2 = getDecisionCoreTimeSheet(predetermineTimeForSet,coreTimeSetting.get(),workType).getEndTime();
	   				}
  	  	     		//早退時間帯の開始時刻を終了時刻にする
  	  	     		dupTimeSheet = new EmTimeZoneSet(new EmTimeFrameNo(workNo), 
  	  	              							 new TimeZoneRounding(dupTimeSheet.getTimezone().getStart(),
  	  	                       										  LeaveEarlyTimeSheet.getForDeducationTimeSheet().isPresent()?LeaveEarlyTimeSheet.getForDeducationTimeSheet().get().getTimeSheet().getEnd()
//  	  	                                     																				         :dupTimeSheet.getTimezone().getEnd(),
  	  	                       												  															 :test2,
  	  	                       					 dupTimeSheet.getTimezone().getRounding()));
  	  	    		}
  	  		}
  		}
			
		//控除時間帯
		List<TimeSheetOfDeductionItem> dedTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(dupTimeSheet.getTimezone().getTimeSpan(), DeductionAtr.Deduction);
		List<TimeSheetOfDeductionItem> recordTimeSheet = deductionTimeSheet.getDupliRangeTimeSheet(dupTimeSheet.getTimezone().getTimeSpan(), DeductionAtr.Appropriate);
		
		/*加給*/
		List<BonusPayTimeSheetForCalc> bonusPayTimeSheet = getBonusPayTimeSheetIncludeDedTimeSheet(bonusPaySetting, dupTimeSheet.getTimezone().getTimeSpan(), recordTimeSheet, recordTimeSheet);
		/*特定日*/
		List<SpecBonusPayTimeSheetForCalc> specifiedBonusPayTimeSheet = getSpecBonusPayTimeSheetIncludeDedTimeSheet(bonusPaySetting, dupTimeSheet.getTimezone().getTimeSpan(), recordTimeSheet, recordTimeSheet);

		/*深夜*/
		Optional<MidNightTimeSheetForCalc> duplicatemidNightTimeSheet = getMidNightTimeSheetIncludeDedTimeSheet(midNightTimeSheet, dupTimeSheet.getTimezone().getTimeSpan(), recordTimeSheet, recordTimeSheet);
		return new WithinWorkTimeFrame(duplicateTimeSheet.getWorkingHoursTimeNo(),
									   dupTimeSheet.getTimezone(),
									   duplicateTimeSheet.getCalcrange(),
									   recordTimeSheet,dedTimeSheet,bonusPayTimeSheet,duplicatemidNightTimeSheet,specifiedBonusPayTimeSheet,
									   Optional.of(lateTimeSheet),
									   Optional.of(LeaveEarlyTimeSheet));
	}
	
	/**
	 * 遅刻早退を控除するかどうか判断
	 * @return
	 */
	public boolean jugmentDeductLateEarly(nts.uk.ctx.at.shared.dom.PremiumAtr premiumAtr,HolidayCalcMethodSet holidayCalcMethodSet,Optional<WorkTimezoneCommonSet> commonSetting) {
		//就業の休暇の就業時間計算方法詳細．遅刻・早退を控除する
//		NotUseAtr workTimeDeductLateLeaveEarly = holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()
//																				?holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly()
//																				:NotUseAtr.NOT_USE;	
		NotUseAtr workTimeDeductLateLeaveEarly = NotUseAtr.NOT_USE;
		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
			if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().isDeductLateLeaveEarly(commonSetting)) {
				workTimeDeductLateLeaveEarly = NotUseAtr.USE;
			}
		}
		
		//割増の休暇の就業時間計算方法詳細．遅刻・早退を控除する
		NotUseAtr premiumDeductLateLeaveEarly = holidayCalcMethodSet.getPremiumCalcMethodOfHoliday().getAdvanceSet().isPresent()
																				?holidayCalcMethodSet.getPremiumCalcMethodOfHoliday().getAdvanceSet().get().getCalculateIncludIntervalExemptionTime()
																			    :NotUseAtr.NOT_USE;
		
		boolean result = false;
//		if(premiumAtr.isRegularWork()&&workTimeDeductLateLeaveEarly == NotUseAtr.NOT_USE) {
//			return true;
//		}else 
		if(premiumAtr.isPremium()
				 &&workTimeDeductLateLeaveEarly == NotUseAtr.USE
				 &&premiumDeductLateLeaveEarly==NotUseAtr.NOT_USE){
			return true;
		}
		return result;
	}
	
	/**
	 * 大塚モード使用時専用の遅刻、早退削除処理
	 */
	public void cleanLateLeaveEarlyTimeForOOtsuka() {
		lateTimeSheet = Optional.empty();
		leaveEarlyTimeSheet = Optional.empty();
	}
	
	
	/**
	 * コアタイム時間帯を午前終了、午後開始で補正したコアタイム時間帯の取得
	 * コアありフレの場合に就業時間内時間帯から遅刻早退を控除しない場合用
	 * @param predetermineTimeForSet
	 * @return
	 */
	public static TimeSheet getDecisionCoreTimeSheet(PredetermineTimeSetForCalc predetermineTimeForSet,CoreTimeSetting coreTimeSetting,WorkType workType) {
		
		TimeSheet result = coreTimeSetting.getCoreTimeSheet();
		
		switch (workType.getAttendanceHolidayAttr()) {
		case MORNING:
			TimeWithDayAttr end = result.getEndTime();
			if(predetermineTimeForSet.getAMEndTime().lessThan(end.valueAsMinutes())) {
				end = predetermineTimeForSet.getAMEndTime();
			}
			result = new TimeSheet(result.getStartTime(), end);
			return result;
		case AFTERNOON:
			TimeWithDayAttr start = result.getStartTime();
			if(predetermineTimeForSet.getPMStartTime().greaterThan(start.valueAsMinutes())) {
				start = predetermineTimeForSet.getPMStartTime();
			}
			result = new TimeSheet(start,result.getEndTime());
			return result;
		case FULL_TIME:
		case HOLIDAY:
			return result;
		default:
			throw new RuntimeException("unknown attr:" + workType.getAttendanceHolidayAttr());
		}
			
	}

}








