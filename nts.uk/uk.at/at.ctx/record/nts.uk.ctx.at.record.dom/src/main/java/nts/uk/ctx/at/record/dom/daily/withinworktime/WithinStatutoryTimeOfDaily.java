package nts.uk.ctx.at.record.dom.daily.withinworktime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
//import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
//import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
//import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
//import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
//import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
//import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeGoOutTimes;
//import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AttendanceItemDictionaryForCalc;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.FlexWithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManageReGetClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
//import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethod;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.PremiumHolidayCalcMethod;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.WorkTimeHolidayCalcMethod;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
//import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfFlexWork;
//import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfIrregularWork;
//import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfRegularWork;
//import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.CalculationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
//import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.context.AppContexts;

/**
 * 日別実績の所定内時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class WithinStatutoryTimeOfDaily {
	//就業時間
	@Setter
	private AttendanceTime workTime;
	//実働就業時間
	@Setter
	private AttendanceTime actualWorkTime = new AttendanceTime(0);
	//所定内割増時間
	private AttendanceTime withinPrescribedPremiumTime = new AttendanceTime(0);
	//所定内深夜時間
	@Setter
	private WithinStatutoryMidNightTime withinStatutoryMidNightTime = new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)));
	//休暇加算時間
	private AttendanceTime vacationAddTime = new AttendanceTime(0);  
	
	/**
	 * Constructor
	 * @param workTime 就業時間
	 * @param actualTime 
	 */
	private WithinStatutoryTimeOfDaily(AttendanceTime workTime,AttendanceTime actualTime, AttendanceTime premiumTime, WithinStatutoryMidNightTime midNightTime) {
		this.workTime = workTime;
		this.actualWorkTime = actualTime;
		this.withinPrescribedPremiumTime = premiumTime;
		this.withinStatutoryMidNightTime = midNightTime;
	}
	
	/**
	 * 全メンバを算出するために計算指示を出す
	 * @param calcMethod 
	 * @param autoCalcAtr 
	 * @param flexCalcMethod 
	 * @param flexLimitSetting 
	 * @param workTimeDailyAtr 
	 * @param workTimeCode 
	 * @param leaveEarlyTime 
	 * @param lateTime 
	 * @return
	 */
	public static WithinStatutoryTimeOfDaily calcStatutoryTime(ManageReGetClass recordReget,	
			   												   VacationClass vacationClass,
			   												   WorkType workType,
			   												   CalcMethodOfNoWorkingDay calcMethod, 
			   												   Optional<SettingOfFlexWork> flexCalcMethod, 
			   												   Optional<WorkTimeDailyAtr> workTimeDailyAtr, 
			   												   Optional<WorkTimeCode> workTimeCode,
			   												   AttendanceTime preFlexTime,
			   												   WorkingConditionItem conditionItem,
			   												   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo) {
		AttendanceTime workTime = new AttendanceTime(0);
		AttendanceTime actualTime = new AttendanceTime(0);
		AttendanceTime withinpremiumTime = new AttendanceTime(0);
		//ここに係船できる状態でやってきているかチェックをする
		//if(!recordReget.getCalculatable()) return new WithinStatutoryTimeOfDaily(workTime, actualTime, new AttendanceTime(0), new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))));
		
		if(recordReget.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().isPresent() && recordReget.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc() != null) {
			//所定内割増時間の計算
			val predTime = recordReget.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc().getpredetermineTime(workType.getDailyWork());
			withinpremiumTime = predTime.greaterThan(recordReget.getDailyUnit().getDailyTime().valueAsMinutes())
										?new AttendanceTime(recordReget.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get().getWithinWorkTimeFrame().stream()
																										  .filter(tc -> tc.getPremiumTimeSheetInPredetermined().isPresent())
																										  .map(tc -> tc.getPremiumTimeSheetInPredetermined().get().getTimeSheet().lengthAsMinutes())
																										  .collect(Collectors.summingInt(tc -> tc)))
																										  
										:new AttendanceTime(0);
		}
		//就業時間の計算

			workTime = calcWithinStatutoryTime(recordReget.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get(),vacationClass,workType,
					  									  recordReget.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLate(),
					  									  recordReget.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLeaveEarly(),
														  recordReget.getPersonalInfo().getWorkingSystem(),
														  recordReget.getWorkDeformedLaborAdditionSet(),
														  recordReget.getWorkFlexAdditionSet(),recordReget.getWorkRegularAdditionSet(),
														  recordReget.getHolidayAddtionSet().get(),
														  recordReget.getHolidayCalcMethodSet(),
														  calcMethod,
														  flexCalcMethod,
														  workTimeDailyAtr,
														  workTimeCode,preFlexTime,
														  recordReget.getCoreTimeSetting(),
														  recordReget.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
														  recordReget.getCalculationRangeOfOneDay().getTimeVacationAdditionRemainingTime(),
														  recordReget.getDailyUnit(),
														  recordReget.getWorkTimezoneCommonSet(),
														  conditionItem,
														  predetermineTimeSetByPersonInfo,
														  Optional.empty());
			

		//実働時間の計算
			actualTime =  calcActualWorkTime(recordReget.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get(),vacationClass,workType,
					  							  recordReget.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLate(),
					  							  recordReget.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLeaveEarly(),
					  							  recordReget.getPersonalInfo().getWorkingSystem(),
					  							  recordReget.getWorkDeformedLaborAdditionSet(),
					  							  recordReget.getWorkFlexAdditionSet(),
//					  							  new WorkFlexAdditionSet(recordReget.getWorkFlexAdditionSet().getCompanyId(),
//					  									  				  new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,recordReget.getWorkFlexAdditionSet().getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//					  									  										   new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, recordReget.getWorkFlexAdditionSet().getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//					  									  				  ),
					  							  recordReget.getWorkRegularAdditionSet(),
//					  							  new WorkRegularAdditionSet(recordReget.getWorkRegularAdditionSet().getCompanyId(),
//					  									  					new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,recordReget.getWorkRegularAdditionSet().getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//					  									  											 new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, recordReget.getWorkRegularAdditionSet().getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//					  									  					),
					  							  recordReget.getHolidayAddtionSet().get(),
					  							  recordReget.getHolidayCalcMethodSet(),
					  							  calcMethod,
					  							  flexCalcMethod,
					  							  workTimeDailyAtr,
					  							  workTimeCode,preFlexTime,
					  							  recordReget.getCoreTimeSetting(),
					  							recordReget.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
					  							recordReget.getCalculationRangeOfOneDay().getTimeVacationAdditionRemainingTime(),
					  							recordReget.getDailyUnit(),
					  							recordReget.getWorkTimezoneCommonSet(),
					  							conditionItem,
					  							predetermineTimeSetByPersonInfo,
					  							Optional.of(new DeductLeaveEarly(0, 1)));
			actualTime = actualTime.minusMinutes(withinpremiumTime.valueAsMinutes());
			
		//所定内深夜時間の計算
		WithinStatutoryMidNightTime midNightTime = WithinStatutoryMidNightTime.calcPredetermineMidNightTime(recordReget.getCalculationRangeOfOneDay());

		 
		return new WithinStatutoryTimeOfDaily(workTime,actualTime,withinpremiumTime,midNightTime);
	}
	
	
	/**
	 * 日別実績の法定内時間の計算
	 * @param dailyUnit 
	 * @param withinpremiumTime 
	 */
	public static AttendanceTime calcWithinStatutoryTime(WithinWorkTimeSheet withinTimeSheet,
			   												   VacationClass vacationClass,
			   												   WorkType workType,
			   												   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			   												   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
			   												   WorkingSystem workingSystem,
			   												   WorkDeformedLaborAdditionSet illegularAddSetting,
			   												   WorkFlexAdditionSet flexAddSetting,
			   												   WorkRegularAdditionSet regularAddSetting,
			   												   HolidayAddtionSet holidayAddtionSet,
			   												   HolidayCalcMethodSet holidayCalcMethodSet,
			   												   CalcMethodOfNoWorkingDay calcMethod, 
			   												   Optional<SettingOfFlexWork> flexCalcMethod,
			   												   Optional<WorkTimeDailyAtr> workTimeDailyAtr,//就業時間帯からとってきた勤務区分
			   												   Optional<WorkTimeCode> workTimeCode,
			   												   AttendanceTime preFlexTime,Optional<CoreTimeSetting> coreTimeSetting,
			   												   PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			   												   Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime, DailyUnit dailyUnit,
			   												   Optional<WorkTimezoneCommonSet> commonSetting,
			   												   WorkingConditionItem conditionItem,
			   												Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			   												Optional<DeductLeaveEarly> deductLeaveEarly
			   												   ) {
		if(conditionItem.getLaborSystem().isFlexTimeWork() 
//		if(true
			&& (!workTimeDailyAtr.isPresent() || workTimeDailyAtr.get().isFlex())) {
			FlexWithinWorkTimeSheet changedFlexTimeSheet = (FlexWithinWorkTimeSheet)withinTimeSheet;
			DeductLeaveEarly leaveLateset = new DeductLeaveEarly(1,1);
			if(deductLeaveEarly.isPresent()) {
				leaveLateset = deductLeaveEarly.get();
			}
			else {
				if(flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
					leaveLateset = flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly();
				}
			}
			Optional<WorkTimezoneCommonSet> leaveLatesetForWorkTime = commonSetting.isPresent() && commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime() && coreTimeSetting.isPresent() && coreTimeSetting.get().isUseTimeSheet()
					  												  ?Optional.of(commonSetting.get().reverceTimeZoneLateEarlySet())
					  												  :commonSetting;
			
			return changedFlexTimeSheet.calcWorkTime(PremiumAtr.RegularWork,
															 flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(),
						  									 vacationClass,
						  									 timeVacationAdditionRemainingTime.get(),
						  									 StatutoryDivision.Nomal,workType,
						  									 predetermineTimeSetForCalc,
						  									 workTimeCode,
						  									 late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
						  									 leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
						  									 workingSystem,
						  									 illegularAddSetting,
						  									 flexAddSetting,
						  									 regularAddSetting,
						  									 holidayAddtionSet,
						  									 holidayCalcMethodSet,
						  									 calcMethod,
						  									 AutoCalAtrOvertime.CALCULATEMBOSS,
						  									 flexCalcMethod.get(),
						  									 preFlexTime,
						  									coreTimeSetting,
						  									dailyUnit,
						  									leaveLatesetForWorkTime,
						  									TimeLimitUpperLimitSetting.NOUPPERLIMIT,
						  									conditionItem,
						  									predetermineTimeSetByPersonInfo,
						  									leaveLateset
					   );
		}
		else {
			DeductLeaveEarly leaveLateset = new DeductLeaveEarly(1,1);
			if(deductLeaveEarly.isPresent()) {
				leaveLateset = deductLeaveEarly.get();
			}
			else {
				if(regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
					leaveLateset = regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly();
				}
			}
			Optional<WorkTimezoneCommonSet> leaveLatesetForWorkTime = commonSetting.isPresent() && commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime()
																	 ?Optional.of(commonSetting.get().reverceTimeZoneLateEarlySet())
																	 :commonSetting;
			return withinTimeSheet.calcWorkTime(PremiumAtr.RegularWork,
																				  regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(),
						  														  vacationClass,
						  														  timeVacationAdditionRemainingTime.get(),
						  														  StatutoryDivision.Nomal,workType,
						  														  predetermineTimeSetForCalc,
						  														  workTimeCode,
						  														  late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
						  														  leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
						  														  workingSystem,
						  														  illegularAddSetting,
						  														  flexAddSetting,
						  														  regularAddSetting,
						  														  holidayAddtionSet,
						  														  holidayCalcMethodSet,
						  														  dailyUnit,
						  														  leaveLatesetForWorkTime,
						  														  conditionItem,
						  														  predetermineTimeSetByPersonInfo,coreTimeSetting,
						  														  HolidayAdditionAtr.HolidayAddition.convertFromCalcByActualTimeToHolidayAdditionAtr(regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation()),
						  									  					  leaveLateset
												).getWorkTime();
		}
	}
	
	/**
	 * 受け取った引数で日別実績の法定内時間を作成する
	 * @author ken_takasu
	 * @param workTime
	 * @param workTimeIncludeVacationTime
	 * @param withinPrescribedPremiumTime
	 * @param withinStatutoryMidNightTime
	 * @param vacationAddTime
	 * @return
	 */
	public static WithinStatutoryTimeOfDaily createWithinStatutoryTimeOfDaily(AttendanceTime workTime,
																	   AttendanceTime actualWorkTime,
																	   AttendanceTime withinPrescribedPremiumTime,
																	   WithinStatutoryMidNightTime withinStatutoryMidNightTime,
																	   AttendanceTime vacationAddTime) {
		WithinStatutoryTimeOfDaily withinStatutoryTimeOfDaily = new WithinStatutoryTimeOfDaily(workTime,actualWorkTime,withinPrescribedPremiumTime,withinStatutoryMidNightTime);
		withinStatutoryTimeOfDaily.actualWorkTime = actualWorkTime;
		withinStatutoryTimeOfDaily.withinPrescribedPremiumTime = withinPrescribedPremiumTime;
		withinStatutoryTimeOfDaily.withinStatutoryMidNightTime = withinStatutoryMidNightTime;
		withinStatutoryTimeOfDaily.vacationAddTime = vacationAddTime;
		return withinStatutoryTimeOfDaily;
	}
	
	public List<EmployeeDailyPerError> checkWithinMidNightExcess(String employeeId,
			                                               		 GeneralDate targetDate,
																 String searchWord,
																 AttendanceItemDictionaryForCalc attendanceItemDictionary,
																 ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorItem = new ArrayList<>();
		if(this.getWithinStatutoryMidNightTime().isOverLimitDivergenceTime()) {
			val itemId = attendanceItemDictionary.findId(searchWord);
			if(itemId.isPresent())
				returnErrorItem.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId.get()));
					
		}
		return returnErrorItem;
	}
	
	/**
	 * 就業時間から休憩未使用時間を減算(大塚モード専用処理)
	 * @param unUseBreakTime 休憩未取得時間
	 */
	public void workTimeMinusUnUseBreakTimeForOotsuka(AttendanceTime unUseBreakTime) {
		this.workTime = this.workTime.minusMinutes(unUseBreakTime.valueAsMinutes());
		this.actualWorkTime = this.actualWorkTime.minusMinutes(unUseBreakTime.valueAsMinutes());
	}
	
	/**
	 * 乖離時間のみ再計算
	 * @return
	 */
	public WithinStatutoryTimeOfDaily calcDiverGenceTime() {
		return new WithinStatutoryTimeOfDaily(this.workTime,
											  this.actualWorkTime,
											  this.withinPrescribedPremiumTime,
											  this.withinStatutoryMidNightTime!=null?this.withinStatutoryMidNightTime.calcDiverGenceTime():this.withinStatutoryMidNightTime);
	}
	
	/**
	 * 深夜時間の上限時間調整処理
	 * @param upperTime 上限時間
	 */
	public void controlMidTimeUpper(AttendanceTime upperTime) {
		this.withinStatutoryMidNightTime.controlUpperTime(upperTime);
	}
	
	
	/**
	 * 実働就業時間の計算
	 * @param dailyUnit 
	 * @param leaveEarlyTime 
	 * @param lateTime 
	 * @param withinpremiumTime 
	 */
	public static AttendanceTime calcActualWorkTime(WithinWorkTimeSheet withinTimeSheet,
			   												   VacationClass vacationClass,
			   												   WorkType workType,
			   												   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			   												   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
			   												   WorkingSystem workingSystem,
			   												   WorkDeformedLaborAdditionSet illegularAddSetting,
			   												   WorkFlexAdditionSet flexAddSetting,
			   												   WorkRegularAdditionSet regularAddSetting,
			   												   HolidayAddtionSet holidayAddtionSet,
			   												   HolidayCalcMethodSet holidayCalcMethodSet,
			   												   CalcMethodOfNoWorkingDay calcMethod, 
			   												   Optional<SettingOfFlexWork> flexCalcMethod,
			   												   Optional<WorkTimeDailyAtr> workTimeDailyAtr,//就業時間帯からとってきた勤務区分
			   												   Optional<WorkTimeCode> workTimeCode,
			   												   AttendanceTime preFlexTime,Optional<CoreTimeSetting> coreTimeSetting,
			   												   PredetermineTimeSetForCalc predetermineTimeSetForCalc,
			   												   Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime, DailyUnit dailyUnit,
			   												   Optional<WorkTimezoneCommonSet> commonSetting,
			   												   WorkingConditionItem conditionItem,
			   												Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			   												Optional<DeductLeaveEarly> deductLeaveEarly
			   												   ) {
		if(conditionItem.getLaborSystem().isFlexTimeWork() 
//		if(true
			&& (!workTimeDailyAtr.isPresent() || workTimeDailyAtr.get().isFlex())) {
			FlexWithinWorkTimeSheet changedFlexTimeSheet = (FlexWithinWorkTimeSheet)withinTimeSheet;
			DeductLeaveEarly leaveLateset = new DeductLeaveEarly(1,1);
			if(deductLeaveEarly.isPresent()) {
				leaveLateset = deductLeaveEarly.get();
			}
			else {
				if(flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
					leaveLateset = flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly();
				}
			}
			Optional<WorkTimezoneCommonSet> leaveLatesetForWorkTime = commonSetting.isPresent() && commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime() && coreTimeSetting.isPresent() && coreTimeSetting.get().isUseTimeSheet()
																	  ?Optional.of(commonSetting.get().reverceTimeZoneLateEarlySet())
																	  :commonSetting;
			
			return changedFlexTimeSheet.calcActualWorkTime(PremiumAtr.RegularWork,
															 flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(),
						  									 vacationClass,
						  									 timeVacationAdditionRemainingTime.get(),
						  									 StatutoryDivision.Nomal,workType,
						  									 predetermineTimeSetForCalc,
						  									 workTimeCode,
						  									 late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
						  									 leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
						  									 workingSystem,
						  									 illegularAddSetting,
						  									 flexAddSetting,
						  									 regularAddSetting,
						  									 holidayAddtionSet,
						  									 holidayCalcMethodSet,
						  									 calcMethod,
						  									 AutoCalAtrOvertime.CALCULATEMBOSS,
						  									 flexCalcMethod.get(),
						  									 preFlexTime,
						  									coreTimeSetting,
						  									dailyUnit,
						  									leaveLatesetForWorkTime,
						  									TimeLimitUpperLimitSetting.NOUPPERLIMIT,
						  									conditionItem,
						  									predetermineTimeSetByPersonInfo,
						  									leaveLateset
					   );
		}
		else {
			DeductLeaveEarly leaveLateset = new DeductLeaveEarly(1,1);
			if(deductLeaveEarly.isPresent()) {
				leaveLateset = deductLeaveEarly.get();
			}
			else {
				if(flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
					leaveLateset = regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly();
				}
			}
			Optional<WorkTimezoneCommonSet> leaveLatesetForWorkTime = commonSetting.isPresent() && commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime()
																	?Optional.of(commonSetting.get().reverceTimeZoneLateEarlySet())
																	:commonSetting;
			return withinTimeSheet.calcWorkTime(PremiumAtr.RegularWork,
																				  regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(),
						  														  vacationClass,
						  														  timeVacationAdditionRemainingTime.get(),
						  														  StatutoryDivision.Nomal,workType,
						  														  predetermineTimeSetForCalc,
						  														  workTimeCode,
						  														  late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
						  														  leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
						  														  workingSystem,
													  							  illegularAddSetting, 
																				  new WorkFlexAdditionSet(flexAddSetting.getCompanyId(),
																						  new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
							  									  										   new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
							  									  				  ),
//																	   regularAddSetting,
																				  new WorkRegularAdditionSet(regularAddSetting.getCompanyId(),
																						   new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,regularAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
						  									  											 new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
						  									  					  ), 
						  														  holidayAddtionSet,
						  														  holidayCalcMethodSet,
						  														  dailyUnit,
						  														  leaveLatesetForWorkTime,
						  														  conditionItem,
						  														  predetermineTimeSetByPersonInfo,coreTimeSetting,
						  														  //HolidayAdditionAtr.HolidayAddition.convertFromCalcByActualTimeToHolidayAdditionAtr(regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation()),
						  														  //休暇加算するか(就業時間計算時)はここを見るようにしている
						  														  HolidayAdditionAtr.HolidayNotAddition,
						  														  leaveLateset
						  														  
												).getWorkTime();
		}
	}
	
	public static WithinStatutoryTimeOfDaily defaultValue(){
		return new WithinStatutoryTimeOfDaily(AttendanceTime.ZERO, AttendanceTime.ZERO, AttendanceTime.ZERO, 
				new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.defaultValue()));
	}
	
	/**
	 * 所定内休憩未取得時間を計算
	 * @param unUseBreakTime 休憩未取得時間
	 * @param withinBreakTime 就業時間帯に設定されている所定内の休憩時間
	 * @param attendanceTime 
	 * @return 休憩未取得時間
	 */
	public AttendanceTime calcUnUseWithinBreakTime(AttendanceTime unUseBreakTime, AttendanceTime predTime, AttendanceTime withinBreakTime) {
		//所定内時間
		AttendanceTime withinPredTime = predTime.addMinutes(withinBreakTime.valueAsMinutes()).minusMinutes(this.getActualWorkTime().valueAsMinutes());
		if(withinPredTime.greaterThan(unUseBreakTime.valueAsMinutes())) {
			return unUseBreakTime;
		}
		else {
			return withinPredTime;
		}
	}
}