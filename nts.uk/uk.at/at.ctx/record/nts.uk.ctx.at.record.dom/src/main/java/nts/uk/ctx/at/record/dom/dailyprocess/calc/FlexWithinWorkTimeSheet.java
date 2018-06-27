package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.LateDecisionClock;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.LeaveEarlyDecisionClock;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeFrame;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.PremiumHolidayCalcMethod;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.WorkTimeHolidayCalcMethod;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.CalculationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
//import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktype.VacationCategory;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * フレックス就業時間内時間帯
 * @author keisuke_hoshina
 *
 */
@Getter
public class FlexWithinWorkTimeSheet extends WithinWorkTimeSheet{
	/*コアタイム*/
	private Optional<TimeSpanForCalc> coreTimeSheet;

	public FlexWithinWorkTimeSheet(List<WithinWorkTimeFrame> withinWorkTimeFrame, Optional<TimeSpanForCalc> coreTimeSheet) {
		super(withinWorkTimeFrame,Optional.of(new LateDecisionClock(new TimeWithDayAttr(0), 1)),Optional.of(new LeaveEarlyDecisionClock(new TimeWithDayAttr(0), 1)));
		this.coreTimeSheet = coreTimeSheet;
	}	
	
	
//	/**
//	 * 休日控除時間の計算
//	 */
//	public AttendanceTime calcHolidayDeductionTime(WorkType workType) {
//		int useTime = VacationClass.vacationTimeOfcalcDaily(workType,VacationCategory.Holiday).valueAsMinutes();
//		useTime += VacationClass.vacationTimeOfcalcDaily(workType,VacationCategory.SubstituteHoliday).valueAsMinutes();
//		return new AttendanceTime(useTime);
//	}
	
	/**
	 * 代休使用時間の計算
	 * @return
	 */
	public AttendanceTime calcSubstituteHoliday(WorkType workType,
			 									Optional<PredetermineTimeSetForCalc> predetermineTimeSet,
			 									Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			 									Optional<WorkTimeCode> siftCode,
			 									WorkingConditionItem  conditionItem,
			 									Optional<HolidayAddtionSet> holidayAddtionSet) {
		if(workType.getDailyWork().getOneDay().isSubstituteHoliday()
			|| workType.getDailyWork().getMorning().isSubstituteHoliday()
			|| workType.getDailyWork().getAfternoon().isSubstituteHoliday())
		{
			return VacationClass.vacationTimeOfcalcDaily(workType, 
													 VacationCategory.SubstituteHoliday,
													 predetermineTimeSet,
													 predetermineTimeSetByPersonInfo,
													 siftCode,
													 conditionItem,
													 holidayAddtionSet);
		}
		else {
			return new AttendanceTime(0);
		}
	}
	
	/**
	 * フレックス時間を計算する
	 * @param preAppTime 
	 * @return
	 */
	public FlexTime createWithinWorkTimeSheetAsFlex(CalcMethodOfNoWorkingDay calcMethod,HolidayCalcMethodSet holidayCalcMethodSet,AutoCalAtrOvertime autoCalcAtr,WorkType workType,SettingOfFlexWork flexCalcMethod,PredetermineTimeSetForCalc predetermineTimeSet,
			   										VacationClass vacationClass,TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
			   										StatutoryDivision statutoryDivision,Optional<WorkTimeCode> siftCode,
//			   										LateTimeSheet lateTimeSheet,LeaveEarlyTimeSheet leaveEarlyTimeSheet,LateTimeOfDaily lateTimeOfDaily,
//			   										LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,
			   										boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			   										boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
			   										WorkingSystem workingSystem,WorkDeformedLaborAdditionSet illegularAddSetting,
													 WorkFlexAdditionSet flexAddSetting,
													 WorkRegularAdditionSet regularAddSetting,
													 HolidayAddtionSet holidayAddtionSet,TimeLimitUpperLimitSetting flexLimitSetting, AttendanceTime preAppTime,
													 DailyUnit dailyUnit,Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
													 Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo) {
		
		FlexTime flexTime = new FlexTime(TimeDivergenceWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)),new AttendanceTime(0));
		
		//1日休日 or 1日休出
		if(workType.getDailyWork().isOneDayHoliday() || workType.getDailyWork().isHolidayWork())
			return flexTime;
		//フレックス計算しない　and 非勤務日
		if(!calcMethod.isCalclateFlexTime() && workType.getDailyWork().getAttendanceHolidayAttr().isHoliday()) 
				return flexTime; 
		/*フレックス時間の計算*/
		AttendanceTimeOfExistMinus calcflexTime = calcFlexTime(holidayCalcMethodSet,autoCalcAtr,workType,flexCalcMethod,predetermineTimeSet,
														  vacationClass,timevacationUseTimeOfDaily,statutoryDivision,siftCode,
//				   										  lateTimeSheet, leaveEarlyTimeSheet, lateTimeOfDaily,
//				   										  leaveEarlyTimeOfDaily,
				   										  late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
				   										  leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
				   										  workingSystem, illegularAddSetting, flexAddSetting, regularAddSetting,
				   										  holidayAddtionSet,dailyUnit,commonSetting,
				   										  flexLimitSetting, conditionItem,predetermineTimeSetByPersonInfo);
		/*事前申請を上限とする制御*/
		AttendanceTimeOfExistMinus afterLimitFlexTime = decisionLimit(flexLimitSetting,calcflexTime,preAppTime);
		
		return new FlexTime(TimeDivergenceWithCalculationMinusExist.sameTime(afterLimitFlexTime),new AttendanceTime(0));
	}
	
	/**
	 * 事前申請上限制所処理
	 * @param flexLimitSetting フレックス超過時間の自動計算設定.自動計算設定.時間外の上限設定
	 * @param flexTime 
	 * @param preAppTime 
	 * @return
	 */
	private AttendanceTimeOfExistMinus decisionLimit(TimeLimitUpperLimitSetting flexLimitSetting, AttendanceTimeOfExistMinus flexTime, AttendanceTime preAppTime) {
		switch(flexLimitSetting) {
			//事前申請を上限にする
			case LIMITNUMBERAPPLICATION:
				//指示時間を上限にする ←はEAで修正されたら適切な処理入れる
			case INDICATEDYIMEUPPERLIMIT:	
				//上限制御をやりつつ、値を返す
				return (preAppTime.greaterThan(flexTime.v()))
						?flexTime
						:new AttendanceTimeOfExistMinus(preAppTime.v());
			
			//上限なし	
			case NOUPPERLIMIT:
				return flexTime;
		default:
			throw new RuntimeException("unknown flex Limit setting:" + flexLimitSetting);
		}
	}




	/**
	 * フレックス時間の計算
	 */
	public AttendanceTimeOfExistMinus calcFlexTime(HolidayCalcMethodSet holidayCalcMethodSet,AutoCalAtrOvertime autoCalcAtr,WorkType workType,SettingOfFlexWork flexCalcMethod,PredetermineTimeSetForCalc predetermineTimeSet,
												   VacationClass vacationClass,TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
												   StatutoryDivision statutoryDivision,Optional<WorkTimeCode> siftCode,
												   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
												   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
												   WorkingSystem workingSystem,WorkDeformedLaborAdditionSet illegularAddSetting,
													 WorkFlexAdditionSet flexAddSetting,
													 WorkRegularAdditionSet regularAddSetting,
													 HolidayAddtionSet holidayAddtionSet,DailyUnit dailyUnit,Optional<WorkTimezoneCommonSet> commonSetting,
													 TimeLimitUpperLimitSetting flexUpper,//こいつは残さないとだめ,
													 WorkingConditionItem conditionItem,Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo
													 ) {
		/*法定労働時間の算出*/
		StatutoryWorkingTime houtei = calcStatutoryTime(workType,flexCalcMethod,predetermineTimeSet==null?Optional.empty():Optional.of(predetermineTimeSet), siftCode,conditionItem,Optional.of(holidayAddtionSet),predetermineTimeSetByPersonInfo);
		/*実働時間の算出*/
		AttendanceTimeOfExistMinus zitudou = new AttendanceTimeOfExistMinus(super.calcWorkTime(PremiumAtr.RegularWork, 
																							   flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getCalculateActualOperation(),
																							   //flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(),
																							   //CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,
																							   vacationClass, 
																							   timevacationUseTimeOfDaily,
																							   statutoryDivision,
																							   workType,
																							   predetermineTimeSet,
																							   siftCode,
																							   late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
																							   leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
																							   workingSystem,
																							   illegularAddSetting,
																	  						   flexAddSetting,
//																	  						   new WorkFlexAdditionSet(flexAddSetting.getCompanyId(),
//																	  							  	  				  new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//																	  									  										   new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//																	  									  				  ),
																	  						   regularAddSetting,
//																	  						   new WorkRegularAdditionSet(regularAddSetting.getCompanyId(),
//																	  									  					new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//																	  									  											 new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//																	  									  					),
																							   holidayAddtionSet,
																							   holidayCalcMethodSet,
																							   dailyUnit,commonSetting,
																							   conditionItem,
																							   predetermineTimeSetByPersonInfo
																							   ).valueAsMinutes());
		/**/
		
		/*実働時間の算出(割増時間含む)*/
		AttendanceTimeOfExistMinus zitudouIncludePremium = new AttendanceTimeOfExistMinus(super.calcWorkTime(PremiumAtr.Premium,
																											 flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getCalculateActualOperation(),
																											 //CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME,
																											 vacationClass, 
																											 timevacationUseTimeOfDaily,
																											 statutoryDivision,
																											 workType,
																											 predetermineTimeSet,
																											 siftCode,
																											 late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
																											 leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
																											 workingSystem,
																											 illegularAddSetting,
																					  						 flexAddSetting,
//																					  						   new WorkFlexAdditionSet(flexAddSetting.getCompanyId(),
//																					  								   					new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME,flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//																					  									  										   new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME, flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//																					  									  				  ),
																					  						 regularAddSetting,
//																					  						   new WorkRegularAdditionSet(regularAddSetting.getCompanyId(),
//																					  									  				new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME,flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//																					  									  											 new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME, flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//																					  									  				 ),

																											 holidayAddtionSet,
																											 holidayCalcMethodSet,
																											 dailyUnit,commonSetting,
																											 conditionItem,
																											 predetermineTimeSetByPersonInfo
																											 ).valueAsMinutes());
		
		AttendanceTimeOfExistMinus flexTime = new AttendanceTimeOfExistMinus(0);
		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation()!=CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME
				&& holidayCalcMethodSet.getPremiumCalcMethodOfHoliday().getCalculateActualOperation()==CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME) {
			/*フレックス時間算出*/
			
			flexTime = new AttendanceTimeOfExistMinus(zitudou.valueAsMinutes()).minusMinutes(houtei.getForActualWorkTime().v());
			
			if(flexTime.lessThan(0)) {
				val flexTimeIncludePremium = new AttendanceTimeOfExistMinus(zitudouIncludePremium.valueAsMinutes()).minusMinutes(houtei.getForWorkTimeIncludePremium().v());
				if(flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()
					&&flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getIncludeVacationSet().getPredeterminedDeficiencyOfFlex().isPresent()
					&&!flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getIncludeVacationSet().getPredeterminedDeficiencyOfFlex().get().isCalc())
					flexTime = (flexTime.greaterThan(0))?new AttendanceTimeOfExistMinus(0):flexTime;
					
				/*不足しているフレックス時間*/
				//zitudouIncludとzitudouを入れ替える
				flexTime = flexTimeIncludePremium.minusMinutes(flexTime.valueAsMinutes());
				//AttendanceTimeOfExistMinus husokuZiKasanZikan = zitudouIncludePremium.minusMinutes(zitudou.valueAsMinutes());
			}
		}
		else if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation()!=CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME
				&& holidayCalcMethodSet.getPremiumCalcMethodOfHoliday().getCalculateActualOperation()!=CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME) {
			
			flexTime = new AttendanceTimeOfExistMinus(zitudou.valueAsMinutes()).minusMinutes(houtei.getForActualWorkTime().v());
		}
		else if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation()==CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME
				&& holidayCalcMethodSet.getPremiumCalcMethodOfHoliday().getCalculateActualOperation()==CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME) {
			/*不足しているフレックス時間*/
			//flexTime = new AttendanceTimeOfExistMinus(houtei.getForWorkTimeIncludePremium().valueAsMinutes() - predetermineTimeSet.getAdditionSet().getPredTime().getPredetermineWorkTime());
			
			flexTime = new AttendanceTimeOfExistMinus(zitudouIncludePremium.v()).minusMinutes(houtei.getForWorkTimeIncludePremium().valueAsMinutes());
		}
		else {
			throw new RuntimeException("A combination that can not be selected is selected");
		}
		
		if((!autoCalcAtr.isCalculateEmbossing()) && flexTime.greaterThan(0)) {
			flexTime = new AttendanceTimeOfExistMinus(0);
		}
		
		return flexTime;
	}
	
	/**
	 * 法定労働時間から控除(フレックス用)
	 * @return
	 */
	public StatutoryWorkingTime calcStatutoryTime(WorkType workType,SettingOfFlexWork flexCalcMethod,Optional<PredetermineTimeSetForCalc> predetermineTimeSet,
												  Optional<WorkTimeCode> siftCode, WorkingConditionItem conditionItem,Optional<HolidayAddtionSet> holidayAddtionSet,
												  Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo) {
		val predetermineTime = predetermineTimeSet.isPresent()?predetermineTimeSet.get().getAdditionSet().getPredTime().getOneDay():new AttendanceTime(0);
		StatutoryDeductionForFlex deductionTime = calcdeductTime(workType,flexCalcMethod,predetermineTimeSet,predetermineTimeSetByPersonInfo, siftCode,conditionItem,holidayAddtionSet);
		return new StatutoryWorkingTime( new AttendanceTime(predetermineTime.v() - deductionTime.getForActualWork().valueAsMinutes()) 
										,new AttendanceTime(predetermineTime.v() - deductionTime.getForPremium().valueAsMinutes()));
	}
	/**
	 * 控除する時間の計算
	 */
	public StatutoryDeductionForFlex calcdeductTime(WorkType workType,SettingOfFlexWork flexCalcMethod,
													Optional<PredetermineTimeSetForCalc> predetermineTimeSet,
													Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
													Optional<WorkTimeCode> siftCode,
													WorkingConditionItem conditionItem,
													Optional<HolidayAddtionSet> holidayAddtionSet){
		/*休日控除時間の計算*/
//		AttendanceTime forHolidayTime = calcHolidayDeductionTime(workType);
		/*代休控除時間の計算*/
		AttendanceTime forCompensatoryLeaveTime = calcSubstituteHoliday(workType,predetermineTimeSet,predetermineTimeSetByPersonInfo, siftCode,conditionItem,holidayAddtionSet);
		
		DeductionTime deductionTime = new DeductionTime(forCompensatoryLeaveTime,forCompensatoryLeaveTime);//(forHolidayTime,forCompensatoryLeaveTime);
		//DeductionTime deductionTime = new DeductionTime(new AttendanceTime(0),new AttendanceTime(0));//(forHolidayTime,forCompensatoryLeaveTime);
		//休日控除が算出できるようになったら、各代休控除時間に加算するように変える
		return new StatutoryDeductionForFlex(deductionTime.getForCompensatoryHolidayTime(),deductionTime.getForCompensatoryHolidayTime());
//		return new StatutoryDeductionForFlex(deductionTime.forLackCalcPredetermineDeduction(flexCalcMethod.getFlexCalcMethod())
//											,deductionTime.forPremiumCalcPredetermineDeduction(flexCalcMethod.getFlexCalcMethod()));
	}
	
	public AttendanceTime calcWorkTime(PremiumAtr premiumAtr, 
									   CalcurationByActualTimeAtr calcActualTime,
									   VacationClass vacationClass,
									   TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
									   StatutoryDivision statutoryDivision,
									   WorkType workType,
									   PredetermineTimeSetForCalc predetermineTimeSet,
									   Optional<WorkTimeCode> siftCode,
									   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
									   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
									   WorkingSystem workingSystem,
									   WorkDeformedLaborAdditionSet illegularAddSetting,
									   WorkFlexAdditionSet flexAddSetting,
									   WorkRegularAdditionSet regularAddSetting,
									   HolidayAddtionSet holidayAddtionSet,
									   HolidayCalcMethodSet holidayCalcMethodSet,
									   CalcMethodOfNoWorkingDay calcMethod, 
									   AutoCalAtrOvertime flexAutoCalcAtr, //フレの上限時間設定
									   SettingOfFlexWork flexCalcMethod,
									   AttendanceTime preFlexTime,Optional<CoreTimeSetting> coreTimeSetting,
									   DailyUnit dailyUnit,Optional<WorkTimezoneCommonSet> commonSetting,
									   TimeLimitUpperLimitSetting flexUpper,//こいつは残さないといけない
									   WorkingConditionItem conditionItem,
									   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo
			   ) {
		AttendanceTime withinTime = super.calcWorkTime(premiumAtr,
													   calcActualTime,
													   vacationClass,
													   timevacationUseTimeOfDaily,
													   statutoryDivision, 
													   workType, 
													   predetermineTimeSet, 
													   siftCode, 
													   late, 
													   leaveEarly, 
													   workingSystem, 
													   illegularAddSetting, 
													   flexAddSetting, 
													   regularAddSetting, 
													   holidayAddtionSet, 
													   holidayCalcMethodSet,
													   dailyUnit,commonSetting,
													   conditionItem,
													   predetermineTimeSetByPersonInfo
													   );
		FlexTime flexTime = this.createWithinWorkTimeSheetAsFlex(calcMethod, 
																 holidayCalcMethodSet, 
																 flexAutoCalcAtr, 
																 workType, 
																 flexCalcMethod, 
																 predetermineTimeSet, 
																 vacationClass, 
																 timevacationUseTimeOfDaily, 
																 statutoryDivision, 
																 siftCode, 
																 late, 
																 leaveEarly, 
																 workingSystem, 
																 illegularAddSetting, 
//									  							  flexAddSetting,
									  							  new WorkFlexAdditionSet(flexAddSetting.getCompanyId(),
					  									  								  new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(),flexAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
					  									  										  				   flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday())
									  									  				  ),
//									  							  regularAddSetting,
									  							  new WorkRegularAdditionSet(regularAddSetting.getCompanyId(),
									  									  					new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation(),regularAddSetting.getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
									  									  											 regularAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday())
									  									  					), 
																 holidayAddtionSet, 
																 flexUpper,
																 preFlexTime,
																 dailyUnit,
																 commonSetting,
																 conditionItem,
																 predetermineTimeSetByPersonInfo
																 );
		AttendanceTime result = new AttendanceTime(0);
		if(flexTime.getFlexTime().getTime().greaterThan(0)) {
			result = withinTime.minusMinutes(flexTime.getFlexTime().getTime().valueAsMinutes());
		}
		else {
			result = withinTime;
		}
		
		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()&&coreTimeSetting.isPresent()&&!coreTimeSetting.get().isUseTimeSheet()) {
			//遅刻時間を就業時間から控除しない場合かつ最低勤務時間よりも就業時間が小さい場合の処理
			if(!holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().isDeductLateLeaveEarly(commonSetting)) {
				if(result.lessThan(coreTimeSetting.get().getMinWorkTime())) {
					result = coreTimeSetting.get().getMinWorkTime();
				}
			}
		}
		return result;
	}
}
