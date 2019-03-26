package nts.uk.ctx.at.record.dom.daily.overtimework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
//import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
//import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculationMinusExist;
//import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AttendanceItemDictionaryForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.FlexWithinWorkTimeSheet;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManageReGetClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTimeSheetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethod;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfEachPremiumHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
//import nts.uk.ctx.at.shared.dom.calculation.holiday.time.OverTimeFrame;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
//import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
//import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
//import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
//import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.ExceededPredAddVacationCalc;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.OverTimeCalcNoBreak;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
//import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 日別実績の残業時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class OverTimeOfDaily {
	//残業枠時間帯
	private List<OverTimeFrameTimeSheet> overTimeWorkFrameTimeSheet;
	//残業枠時間
	private List<OverTimeFrameTime> overTimeWorkFrameTime;
	//法定外深夜時間
	private Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime; 
	//残業拘束時間
	private AttendanceTime overTimeWorkSpentAtWork = new AttendanceTime(0);
	//変形法定内残業
	private AttendanceTime irregularWithinPrescribedOverTimeWork = new AttendanceTime(0);
	//フレックス時間
	@Setter
	private FlexTime flexTime = new FlexTime(TimeDivergenceWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)),new AttendanceTime(0));
	
	public OverTimeOfDaily(List<OverTimeFrameTimeSheet> frameTimeSheetList, List<OverTimeFrameTime> frameTimeList
							   ,Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime) {
		this.overTimeWorkFrameTimeSheet = new ArrayList<>(frameTimeSheetList);
		this.overTimeWorkFrameTime = new ArrayList<>(frameTimeList);
		this.excessOverTimeWorkMidNightTime = excessOverTimeWorkMidNightTime;
	}
	
	public OverTimeOfDaily(List<OverTimeFrameTimeSheet> frameTimeSheetList,
							List<OverTimeFrameTime> frameTimeList,
						    Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime,
						    AttendanceTime irregularTime,
						    FlexTime flexTime,
						    AttendanceTime overTimeWork
						    ) {
		this.overTimeWorkFrameTimeSheet = new ArrayList<>(frameTimeSheetList);
		this.overTimeWorkFrameTime = new ArrayList<>(frameTimeList);
		this.excessOverTimeWorkMidNightTime = excessOverTimeWorkMidNightTime;
		this.irregularWithinPrescribedOverTimeWork = irregularTime;
		this.flexTime = flexTime;
		this.overTimeWorkSpentAtWork = overTimeWork;
	}

	
	/**
	 * 勤務回数を見て開始時刻が正しいか判定する
	 * @param startTime
	 * @param workNo
	 * @param attendanceTime
	 * @return
	 */
	public static boolean startDicision(TimeWithDayAttr startTime, int workNo, TimeWithDayAttr attendanceTime) {
		if(workNo == 0) {
			return (startTime.v() < attendanceTime.v());
		}
		else{
			return (startTime.v() >= attendanceTime.v());
		}
	}
	
	/**
	 * 残業時間が含んでいる加給時間の計算
	 * @return 加給時間リスト
	 */
	public List<BonusPayTime> calcBonusPay(AutoCalRaisingSalarySetting bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(OverTimeFrameTimeSheetWork frameTimeSheet : overTimeWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	
	/**
	 * 残業時間が含んでいる特定日加給時間の計算
	 * @return 加給時間リスト
	 */
	public List<BonusPayTime> calcSpecifiedBonusPay(AutoCalRaisingSalarySetting bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily){
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(OverTimeFrameTimeSheetWork frameTimeSheet : overTimeWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
//		}
		return bonusPayList;
	}
	/**
	 * 残業時間が含んでいる深夜時間の算出
	 * @return 日別実績の深夜時間帯クラス
	 */
	public ExcessOverTimeWorkMidNightTime calcMidNightTimeIncludeOverTimeWork(AutoCalOvertimeSetting autoCalcSet) {
		int totalTime = 0;
//		for(OverTimeFrameTimeSheetWork frameTime : overTimeWorkFrameTimeSheet) {
//			/*↓分岐の条件が明確になったら記述*/
//			AutoCalcSet setting;
//			if(frameTime.getWithinStatutoryAtr().isStatutory()) {
//				setting = autoCalcSet.getLegalOvertimeHours();
//			}
//			else if(frameTime.isGoEarly()) {
//				setting = autoCalcSet.getEarlyOvertimeHours();
//			}
//			else {
//				setting = autoCalcSet.getNormalOvertimeHours();
//			}
//			totalTime += frameTime.calcMidNight(setting.getCalculationClassification());
//		}
		return new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(totalTime)));
	}
	
	/**
	 * 全枠の残業時間の合計の算出
	 * @return　残業時間
	 */
	public int calcTotalFrameTime() {
		int totalTime = 0;
		for(OverTimeFrameTime overTimeWorkFrameTime :overTimeWorkFrameTime) {
			totalTime += overTimeWorkFrameTime.getOverTimeWork().getTime().valueAsMinutes();
		}
		return totalTime;
	}
	
	/**
	 * 全枠の振替残業時間の合計の算出
	 * @return　振替残業時間
	 */
	public int calcTransTotalFrameTime() {
		int transTotalTime = 0;
		for(OverTimeFrameTime overTimeWorkFrameTime :overTimeWorkFrameTime) {
			transTotalTime += overTimeWorkFrameTime.getTransferTime().getTime().valueAsMinutes();
		}
		return transTotalTime;
	}
	
	public OverTimeOfDaily createFromJavaType(List<OverTimeFrameTime> frameTimeList,
											  ExcessOverTimeWorkMidNightTime midNightTime,
											  AttendanceTime irregularTime,
											  FlexTime flexTime,
											  AttendanceTime overTimeWork) {
		return new OverTimeOfDaily(Collections.emptyList(),
								   frameTimeList,
								   Finally.of(midNightTime),
								   irregularTime,
								   flexTime,
								   overTimeWork
								   );
	}
	
	/**
	 * メンバー変数の時間計算を指示するクラス
	 * @param integrationOfDaily 
	 * @param flexPreAppTime 
	 * @return 計算結果
	 */
	public static OverTimeOfDaily calculationTime(ManageReGetClass recordReGet,
												  CalcMethodOfNoWorkingDay calcMethod,
												  WorkType workType,
												  Optional<SettingOfFlexWork> flexCalcMethod,
												  VacationClass vacationClass,
												  StatutoryDivision statutoryDivision,
												  Optional<WorkTimeCode> siftCode,
												  Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
												  Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet,  
												  AttendanceTime flexPreAppTime,
												  WorkingConditionItem conditionItem,
												  Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<CoreTimeSetting> coreTimeSetting,AttendanceTime beforeApplicationTime) {
		val overTimeSheet = recordReGet.getCalculationRangeOfOneDay().getOutsideWorkTimeSheet().get().getOverTimeWorkSheet().get();
		//枠時間帯入れる
		val overTimeFrameTimeSheet = overTimeSheet.changeOverTimeFrameTimeSheet();
		//枠時間計算
		val overTimeFrame = overTimeSheet.collectOverTimeWorkTime(recordReGet.getIntegrationOfDaily().getCalAttr().getOvertimeSetting(),
																  workType,
																  eachWorkTimeSet,
																  eachCompanyTimeSet,
																  recordReGet.getIntegrationOfDaily(), 
																  recordReGet.getStatutoryFrameNoList());
		//残業内の深夜時間計算
		val excessOverTimeWorkMidNightTime = Finally.of(calcExcessMidNightTime(overTimeSheet,recordReGet.getIntegrationOfDaily().getCalAttr().getOvertimeSetting(),beforeApplicationTime,recordReGet.getIntegrationOfDaily().getCalAttr()));
		//変形法定内残業時間計算
		val irregularTime = overTimeSheet.calcIrregularTime();
		//フレックス時間
		FlexTime flexTime = new FlexTime(TimeDivergenceWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)),new AttendanceTime(0));
		//フレ時間の計算に挑戦
		if(recordReGet.getWorkTimeSetting().isPresent() && recordReGet.getWorkTimeSetting().get().getWorkTimeDivision().getWorkTimeDailyAtr().isFlex() && recordReGet.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet() != null) {
			
			val changeVariant = ((FlexWithinWorkTimeSheet)recordReGet.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get());
			flexTime =  changeVariant.createWithinWorkTimeSheetAsFlex(calcMethod,recordReGet.getHolidayCalcMethodSet(),recordReGet.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getCalAtr(),workType,
					new SettingOfFlexWork(new FlexCalcMethodOfHalfWork(new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.Half, FlexCalcMethod.Half),
																	   new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.Half, FlexCalcMethod.Half))),
					recordReGet.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
					vacationClass,
					recordReGet.getCalculationRangeOfOneDay().getTimeVacationAdditionRemainingTime().get(),
					statutoryDivision,siftCode,
					recordReGet.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLate(),  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
					recordReGet.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLeaveEarly(),  //日別実績の計算区分.遅刻早退の自動計算設定.早退
					recordReGet.getPersonalInfo().getWorkingSystem(),
					recordReGet.getWorkDeformedLaborAdditionSet(),
					recordReGet.getWorkFlexAdditionSet(),
					recordReGet.getWorkRegularAdditionSet(),
					recordReGet.getHolidayAddtionSet().get(),
					recordReGet.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getUpLimitORtSet(),flexPreAppTime,
					recordReGet.getDailyUnit(),recordReGet.getWorkTimezoneCommonSet(),conditionItem,
					predetermineTimeSetByPersonInfo,coreTimeSetting);
		}

		val overTimeWork = new AttendanceTime(0);
		return new OverTimeOfDaily(overTimeFrameTimeSheet,
								   overTimeFrame,
								   excessOverTimeWorkMidNightTime,
								   irregularTime,
								   flexTime,
								   overTimeWork);
		
	}
	/**
	 * 所定外深夜時間の計算
	 * @param oneDay
	 * @return　所定外深夜時間
	 */
	private static ExcessOverTimeWorkMidNightTime calcExcessMidNightTime(OverTimeSheet overTimeSheet,AutoCalOvertimeSetting autoCalcSet,AttendanceTime beforeApplicationTime,CalAttrOfDailyPerformance calAttr) {
		
		AttendanceTime calcTime = overTimeSheet.calcMidNightTime(autoCalcSet);
		//事前申請制御
		if(calAttr.getOvertimeSetting().getNormalMidOtTime().getUpLimitORtSet()==TimeLimitUpperLimitSetting.LIMITNUMBERAPPLICATION&&calcTime.greaterThanOrEqualTo(beforeApplicationTime.valueAsMinutes())) {
			return new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(beforeApplicationTime, calcTime));
		}
		return new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation.sameTime(calcTime));
	}
	
	/**
	 * 残業時間超過のチェック&エラーゲット 
	 * @param attendanceItemDictionary 
	 */
	public List<EmployeeDailyPerError> checkOverTimeExcess(String employeeId,
														   GeneralDate targetDate,
														   AttendanceItemDictionaryForCalc attendanceItemDictionary,
														   ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		for(OverTimeFrameTime frameTime:this.getOverTimeWorkFrameTime()) {
			if(frameTime.isOverLimitDivergenceTime()) {
				//残業時間
				attendanceItemDictionary.findId("残業時間"+frameTime.getOverWorkFrameNo().v()).ifPresent( itemId -> 
						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
				);
//				//振替時間
//				attendanceItemDictionary.findId("振替残業時間"+frameTime.getOverWorkFrameNo().v()).ifPresent( itemId -> 
//						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
//				);
			}
		}
		return returnErrorList;
	}
	
	/**
	 * 事前残業申請超過のチェック&エラーゲット 
	 */
	public List<EmployeeDailyPerError> checkPreOverTimeExcess(String employeeId,
														   GeneralDate targetDate,
														   AttendanceItemDictionaryForCalc attendanceItemDictionary,
														   ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		for(OverTimeFrameTime frameTime:this.getOverTimeWorkFrameTime()) {
			if(frameTime.isPreOverLimitDivergenceTime()) {
				//残業時間
				attendanceItemDictionary.findId("残業時間"+frameTime.getOverWorkFrameNo().v()).ifPresent( itemId -> 
						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
				);
//				//振替時間
//				attendanceItemDictionary.findId("振替残業時間"+frameTime.getOverWorkFrameNo().v()).ifPresent( itemId -> 
//						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
//				);
			}
		}
		return returnErrorList;
	}
	
	/**
	 *　フレ超過のチェック＆エラーゲット 
	 * @return
	 */
	public List<EmployeeDailyPerError> checkFlexTimeExcess(String employeeId,
														   GeneralDate targetDate,
														   String searchWord,
														   AttendanceItemDictionaryForCalc attendanceItemDictionary,
														   ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if(this.getFlexTime().isOverLimitDivergenceTime()) {
			val itemId = attendanceItemDictionary.findId(searchWord);
			if(itemId.isPresent())
				returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId.get()));
		}
		return returnErrorList;
	}
	
	/**
	 *　事前フレ申請超過のチェック＆エラーゲット 
	 * @return
	 */
	public List<EmployeeDailyPerError> checkPreFlexTimeExcess(String employeeId,
														   GeneralDate targetDate,
														   String searchWord,
														   AttendanceItemDictionaryForCalc attendanceItemDictionary,
														   ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if(this.getFlexTime().isPreOverLimitDivergenceTime()) {
			val itemId = attendanceItemDictionary.findId(searchWord);
			if(itemId.isPresent())
				returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId.get()));
		}
		return returnErrorList;
	}

	/**
	 *　実績深夜時間のチェック＆エラーゲット 
	 * @return
	 */
	public List<EmployeeDailyPerError> checkNightTimeExcess(String employeeId,
														   GeneralDate targetDate,
														   String searchWord,
														   AttendanceItemDictionaryForCalc attendanceItemDictionary,
														   ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if(this.getExcessOverTimeWorkMidNightTime().isPresent()
				&& this.getExcessOverTimeWorkMidNightTime().get().isOverLimitDivergenceTime()) {
			val itemId = attendanceItemDictionary.findId(searchWord);
			if(itemId.isPresent())
				returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId.get()));
		}
		return returnErrorList;
	}
	
	
	//大塚モード計算
	/**
	 * 
	 * @param actualWorkTime 実働就業時間
	 * @param dailyUnit 
	 * @param predetermineTimeSetForCalc 
	 * @param finally1 
	 * @param optional 
	 * @param unUseBreakTime　休憩未取得時間
	 * @param predetermineTime 1日の所定時間
	 * @param ootsukaFixedCalcSet　大塚固定計算設定
	 */
	public void calcOotsukaOverTime(AttendanceTime actualWorkTime, AttendanceTime unUseBreakTime,
									AttendanceTime annualAddTime,AttendanceTime predTime,
									Optional<FixedWorkCalcSetting> ootsukaFixedCalcSet,
									AutoCalOvertimeSetting autoCalcSet, DailyUnit dailyUnit, 
									Optional<FixRestTimezoneSet> restTimeSheet, 
									Finally<WithinWorkTimeSheet> withinWorkTimeSheet, PredetermineTimeSetForCalc predetermineTimeSetForCalc) {
		if(ootsukaFixedCalcSet != null && ootsukaFixedCalcSet.isPresent() ) {
			//休憩未取得時間から残業時間計算
			calcOverTimeFromUnuseTime(actualWorkTime, unUseBreakTime, ootsukaFixedCalcSet.get().getOverTimeCalcNoBreak(),dailyUnit,predTime);
			//所定時間を超過した残業時間を計算
			calcOverTimeFromOverPredTime(actualWorkTime, unUseBreakTime, annualAddTime, predetermineTimeSetForCalc.getAdditionSet().getPredTime().getOneDay(), ootsukaFixedCalcSet.get().getExceededPredAddVacationCalc(), 
										 autoCalcSet,restTimeSheet,withinWorkTimeSheet);
		}
	}

	/**
	 *  休憩未取得時間から残業時間計算 
	 * @param predTime  
	 * @param dailyUnit  
	 */
	private void calcOverTimeFromUnuseTime(AttendanceTime actualWorkTime, AttendanceTime unUseBreakTime,
										   OverTimeCalcNoBreak ootsukaFixedCalcSet, DailyUnit dailyUnit, AttendanceTime predTime
										   ) {
		//就業時間として計算か判定
		if(ootsukaFixedCalcSet == null
			|| ootsukaFixedCalcSet.getCalcMethod() == null
			|| ootsukaFixedCalcSet.getCalcMethod().isCalcAsWorking()
			|| ootsukaFixedCalcSet.getInLawOT() == null
			|| ootsukaFixedCalcSet.getNotInLawOT() == null)
			return;
		//法定労働時間を取得
		val statutoryTime = dailyUnit.getDailyTime();
		//既に計算されてきた残業枠Noの一覧
		val frameNoList = this.overTimeWorkFrameTime.stream().map(tc -> tc.getOverWorkFrameNo()).collect(Collectors.toList());
		//実働就業<=法定労働(法定内)
		if(actualWorkTime.lessThanOrEqualTo(statutoryTime)) {
			if(unUseBreakTime.greaterThan(0)) {
				if(frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()))) {
					this.overTimeWorkFrameTime.forEach(tc -> {if(tc.getOverWorkFrameNo().v().equals(ootsukaFixedCalcSet.getInLawOT().v())) 
						 									     tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(tc.getOverTimeWork().getTime().addMinutes(unUseBreakTime.valueAsMinutes()), tc.getOverTimeWork().getCalcTime()));
															 });
				}
				else {					
					this.overTimeWorkFrameTime.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()), 
							 TimeDivergenceWithCalculation.createTimeWithCalculation(unUseBreakTime,unUseBreakTime), 
							 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), 
						     new AttendanceTime(0), 
						     new AttendanceTime(0)));
				}
			}
		}
		//実働就業>法定労働(法定外)
		else {
			//法内
			final int calcUnbreakTime = unUseBreakTime.minusMinutes(actualWorkTime.valueAsMinutes() - statutoryTime.valueAsMinutes()).valueAsMinutes();
			if(frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()))) {
				this.overTimeWorkFrameTime.forEach(tc -> {if(tc.getOverWorkFrameNo().v().equals(ootsukaFixedCalcSet.getInLawOT().v())) 
					 									     tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(tc.getOverTimeWork().getTime().addMinutes(calcUnbreakTime), tc.getOverTimeWork().getCalcTime()));
														 });
			}
			else {
				this.overTimeWorkFrameTime.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()), 
																	 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(calcUnbreakTime)),
																	 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), 
																     new AttendanceTime(0), 
																     new AttendanceTime(0)));
			}
			frameNoList.add(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()));
			//法外
			final AttendanceTime excessOverTime = actualWorkTime.minusMinutes(statutoryTime.valueAsMinutes()).valueAsMinutes() > unUseBreakTime.v() ? unUseBreakTime : actualWorkTime.minusMinutes(statutoryTime.valueAsMinutes());
			if(frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getNotInLawOT().v()))) {
				this.overTimeWorkFrameTime.forEach(tc -> {if(tc.getOverWorkFrameNo().v().equals(ootsukaFixedCalcSet.getNotInLawOT().v())) 
															tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(tc.getOverTimeWork().getTime().addMinutes(excessOverTime.valueAsMinutes()), tc.getOverTimeWork().getCalcTime()));
														 });
			}
			else {
				this.overTimeWorkFrameTime.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getNotInLawOT().v()), 
																	 TimeDivergenceWithCalculation.sameTime( unUseBreakTime.minusMinutes(excessOverTime.valueAsMinutes()).valueAsMinutes() < 0 ? new AttendanceTime(0) : excessOverTime),
																	 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), 
																     new AttendanceTime(0), 
																     new AttendanceTime(0)));
			}
		}
	}
	
	/**
	 * 所定時間を超過した残業時間を計算
	 * @param restTimeSheet 
	 * @param withinWorkTimeSheet 
	 * @param actualWorkTime　実働時間
	 * @param unUseBreakTime　未使用休憩時間
	 * @param annualAddTime　年休加算時間
	 * @param oneDayPredTime　1日の所定時間
	 * @param ootsukaFixedCalcSet　大塚固定計算設定
	 * @param autoCalcSet　残業時間の自動計算設定
	 */
	private void calcOverTimeFromOverPredTime(AttendanceTime actualWorkTime, AttendanceTime unUseBreakTime,
											  AttendanceTime annualAddTime, AttendanceTime oneDayPredTime,
											  ExceededPredAddVacationCalc ootsukaFixedCalcSet,
											  AutoCalOvertimeSetting autoCalcSet, Optional<FixRestTimezoneSet> restTimeSheet, 
											  Finally<WithinWorkTimeSheet> withinWorkTimeSheet){
		//AttendanceTime breakTimeInWithinTimeSheet = getBreakTimeInWithin(withinWorkTimeSheet,restTimeSheet);
		
		final AttendanceTime totalWorkTime = new AttendanceTime(actualWorkTime.valueAsMinutes()
				  						+ annualAddTime.valueAsMinutes()
				  						- unUseBreakTime.valueAsMinutes());
		
		final AttendanceTime withinOverTime = totalWorkTime.greaterThan(oneDayPredTime.valueAsMinutes())
										?totalWorkTime.minusMinutes(oneDayPredTime.valueAsMinutes())
										:new AttendanceTime(0);
		
		//就業時間として計算か判定
		if(ootsukaFixedCalcSet == null
				|| ootsukaFixedCalcSet.getCalcMethod() == null
				|| ootsukaFixedCalcSet.getCalcMethod().isCalcAsWorking()
				|| ootsukaFixedCalcSet.getOtFrameNo() == null)
				return;
		
		val frameNoList = this.overTimeWorkFrameTime.stream().map(tc -> tc.getOverWorkFrameNo()).collect(Collectors.toList());
										
		//一旦、普通を見るようにする
		//打刻から計算する　
		if(autoCalcSet.decisionCalcAtr(StatutoryAtr.Statutory, false)) {
			if(withinOverTime.greaterThan(0)) {
				if(frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()))) {
					this.overTimeWorkFrameTime.forEach(tc ->{if(tc.getOverWorkFrameNo().equals(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v())))
															tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(tc.getOverTimeWork().getTime().addMinutes(withinOverTime.valueAsMinutes()),
																																	   tc.getOverTimeWork().getCalcTime().addMinutes(withinOverTime.valueAsMinutes())));
												    });
				}
				else {
					this.overTimeWorkFrameTime.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()), 
																		 TimeDivergenceWithCalculation.sameTime(withinOverTime),
																		 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), 
																		 new AttendanceTime(0), 
																		 new AttendanceTime(0)));
				}				
			}
		}
		//上記条件以外
		else {
			if(frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()))) {
				this.overTimeWorkFrameTime.forEach(tc ->{if(tc.getOverWorkFrameNo().equals(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v())))
															tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(tc.getOverTimeWork().getTime(),
																																	   tc.getOverTimeWork().getCalcTime().addMinutes(withinOverTime.valueAsMinutes())));
												   });
			}
			else {
				this.overTimeWorkFrameTime.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()), 
						 					   						 TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0), withinOverTime),
						 					   						 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), 
						 					   						 new AttendanceTime(0), 
						 					   						 new AttendanceTime(0)));
			}

		}
		
	}
	
	
	/**
	 * 就内時間帯に含まれている休憩時間の計算
	 * @param withinWorkTimeSheet　就業時間帯
	 * @param restTimeSheetSet　就業時間帯マスタの休憩時間帯
	 * @return　取得した休憩時間
	 */
	@SuppressWarnings("unused")
	private AttendanceTime getBreakTimeInWithin(Finally<WithinWorkTimeSheet> withinWorkTimeSheet,
			Optional<FixRestTimezoneSet> restTimeSheetSet) {
		if(!restTimeSheetSet.isPresent()
		  ||withinWorkTimeSheet == null
		  ||!withinWorkTimeSheet.isPresent())
			return new AttendanceTime(0);
		AttendanceTime restTime = new AttendanceTime(0);
		for(DeductionTime restTimeSheet :restTimeSheetSet.get().getLstTimezone()) {
			restTime = restTime.addMinutes(withinWorkTimeSheet.get().getDupRestTime(restTimeSheet).valueAsMinutes());
		}
		return restTime;
	}



	/**
	 * 乖離時間のみ再計算
	 * @return
	 */
	public OverTimeOfDaily calcDiverGenceTime() {
		List<OverTimeFrameTime> OverTimeFrameList = new ArrayList<>();
		for(OverTimeFrameTime overTimeFrameTime:this.overTimeWorkFrameTime) {
			overTimeFrameTime.calcDiverGenceTime();
			OverTimeFrameList.add(overTimeFrameTime);
		}
		FlexTime flexTime = this.flexTime!=null?this.flexTime.calcDiverGenceTime():this.flexTime;
		Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeMidNight = this.excessOverTimeWorkMidNightTime.isPresent()?Finally.of(this.excessOverTimeWorkMidNightTime.get().calcDiverGenceTime())
																																:this.excessOverTimeWorkMidNightTime;
		return new OverTimeOfDaily(this.overTimeWorkFrameTimeSheet,OverTimeFrameList,excessOverTimeMidNight,this.irregularWithinPrescribedOverTimeWork,flexTime,this.overTimeWorkSpentAtWork);
	}
	
	//PCログインログオフから計算した計算時間を入れる(大塚モードのみ)
	public void setPCLogOnValue(Map<OverTimeFrameNo, OverTimeFrameTime> map) {
		Map<OverTimeFrameNo,OverTimeFrameTime> changeList = convertOverMap(this.getOverTimeWorkFrameTime());

		for(int frameNo = 1 ; frameNo<=10 ; frameNo++) {
			
			//値更新
			if(changeList.containsKey(new OverTimeFrameNo(frameNo))) {
				val getframe = changeList.get(new OverTimeFrameNo(frameNo)); 
				if(map.containsKey(new OverTimeFrameNo(frameNo))) {
					//残業時間の置き換え
					if(getframe.getOverTimeWork()!=null && map.get(new OverTimeFrameNo(frameNo)).getOverTimeWork()!=null) {
						getframe.getOverTimeWork().replaceTimeAndCalcDiv(map.get(new OverTimeFrameNo(frameNo)).getOverTimeWork().getCalcTime());
					}else {
						getframe.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),new AttendanceTime(0)));
					}
					//振替時間の置き換え
					if(getframe.getTransferTime()!=null&&map.get(new OverTimeFrameNo(frameNo)).getTransferTime()!=null) {
						getframe.getTransferTime().replaceTimeAndCalcDiv(map.get(new OverTimeFrameNo(frameNo)).getTransferTime().getCalcTime());
					}else {
						getframe.setTransferTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),new AttendanceTime(0)));
					}
				}
				else {
					//残業時間の置き換え
					getframe.getOverTimeWork().replaceTimeAndCalcDiv(new AttendanceTime(0));
					//振替時間の置き換え
					getframe.getTransferTime().replaceTimeAndCalcDiv(new AttendanceTime(0));
				}
				changeList.remove(new OverTimeFrameNo(frameNo));
				changeList.put(new OverTimeFrameNo(frameNo), getframe);
			}
			//リストへ追加
			else {
				if(map.containsKey(new OverTimeFrameNo(frameNo))) {
					changeList.put(new OverTimeFrameNo(frameNo),
							   	   new OverTimeFrameTime(new OverTimeFrameNo(frameNo),
							   			   				 TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),map.get(new OverTimeFrameNo(frameNo)).getOverTimeWork().getCalcTime()),
							   			   				 TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),map.get(new OverTimeFrameNo(frameNo)).getTransferTime().getCalcTime()),
							   			   				 new AttendanceTime(0),
							   			   				 new AttendanceTime(0)));
				}
			}
		}
		this.overTimeWorkFrameTime = new ArrayList<>(changeList.values());
	}
	
	private Map<OverTimeFrameNo,OverTimeFrameTime> convertOverMap(List<OverTimeFrameTime> overTimeWorkFrameTime) {
		Map<OverTimeFrameNo,OverTimeFrameTime> map = new HashMap<>();
		for(OverTimeFrameTime ot : overTimeWorkFrameTime) {
			map.put(ot.getOverWorkFrameNo(), ot);
		}
		return map;
	}
	
	public void transWithinOverTimeForOOtsukaSpecialHoliday(List<OverTimeOfTimeZoneSet> overTimeSheetByWorkTimeMaster,AttendanceTime withinOverTime) {
		AttendanceTime copyWithinOverTime = withinOverTime;
		List<OverTimeOfTimeZoneSet> sortedOverTimeZoneSet = overTimeSheetByWorkTimeMaster;
		if(overTimeSheetByWorkTimeMaster.size() > 0) {
			boolean nextLoopFlag = true;
			while(nextLoopFlag) {
				for(int index = 0 ; index <= sortedOverTimeZoneSet.size() ; index++) {
					if(index == sortedOverTimeZoneSet.size() - 1)
					{
						nextLoopFlag = false;
						break;
					}
					//精算順序の比較
					if(sortedOverTimeZoneSet.get(index).getSettlementOrder().greaterThan(sortedOverTimeZoneSet.get(index + 1).getSettlementOrder())) {
						OverTimeOfTimeZoneSet pary = sortedOverTimeZoneSet.get(index);
						sortedOverTimeZoneSet.set(index, sortedOverTimeZoneSet.get(index + 1));
						sortedOverTimeZoneSet.set(index + 1, pary);
						break;
					}
					//枠の比較(精算順序同じケースの整頓)
					else if(sortedOverTimeZoneSet.get(index).getSettlementOrder().equals(sortedOverTimeZoneSet.get(index + 1).getSettlementOrder())) {
						if(sortedOverTimeZoneSet.get(index).getTimezone().getStart().greaterThan(sortedOverTimeZoneSet.get(index + 1).getTimezone().getStart()) ) {
							OverTimeOfTimeZoneSet pary = sortedOverTimeZoneSet.get(index);
							sortedOverTimeZoneSet.set(index, sortedOverTimeZoneSet.get(index + 1));
							sortedOverTimeZoneSet.set(index + 1, pary);
							break;
						}
					}
		
				}
			}
		}
		
		
		for(OverTimeOfTimeZoneSet set : sortedOverTimeZoneSet) {
			//全残業枠の残業時間＋振出時間の合計
			Optional<AttendanceTime> transAndOverTime = overTimeWorkFrameTime.stream().filter(tc -> tc.getOverWorkFrameNo().compareTo(set.getOtFrameNo().v()) == 0)
										  											  .map(ts -> ts.getOverTimeWork().getTime().addMinutes(ts.getTransferTime().getTime().valueAsMinutes()))
										  											  .findFirst();
			AttendanceTime transTime = new AttendanceTime(0) ;
			if(transAndOverTime.isPresent() && copyWithinOverTime.greaterThan(transAndOverTime.get())) {
				transTime = transAndOverTime.get();
			}
			else {
				transTime = copyWithinOverTime;
			}
			
			final int toTime = transTime.valueAsMinutes();
			//減算
			overTimeWorkFrameTime.forEach(tc ->{
				if(tc.getOverWorkFrameNo().compareTo(set.getOtFrameNo().v()) == 0) {
					tc.minusTimeResultGreaterEqualZero(new AttendanceTime(toTime));
				}
			});
			//加算
			Optional<OverTimeFrameNo> forcsWithin = overTimeWorkFrameTime.stream().filter(tc -> tc.getOverWorkFrameNo().compareTo(set.getLegalOTframeNo().v()) == 0)
																				  .map(ts -> ts.getOverWorkFrameNo())
																				  .findFirst();
			//既存枠がある
			if(forcsWithin.isPresent()) {
				overTimeWorkFrameTime.forEach(ts ->{
					if(ts.getOverWorkFrameNo().compareTo(forcsWithin.get()) == 0) {
						ts.add(new AttendanceTime(toTime));
					}
				});
			}
			//既存枠がない
			else {
				this.overTimeWorkFrameTime.add(new OverTimeFrameTime(new OverTimeFrameNo(set.getLegalOTframeNo().v()), 
																	 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(toTime)), 
																	 TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), 
																	 new AttendanceTime(0), 
																	 new AttendanceTime(0)));
			}
			
			copyWithinOverTime = copyWithinOverTime.minusMinutes(toTime);

			if(copyWithinOverTime.lessThan(0))
				break;
		}
		
	}
}
