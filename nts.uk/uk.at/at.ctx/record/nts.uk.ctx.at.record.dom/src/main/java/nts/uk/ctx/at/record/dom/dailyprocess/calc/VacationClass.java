package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Value;
import lombok.val;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AbsenceOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.OverSalaryOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SubstituteHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.TimeDigestOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.YearlyReservedOfDaily;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.LeaveSetAdded;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktype.VacationCategory;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * 休暇クラス
 * @author keisuke_hoshina
 *
 */
@Value
public class VacationClass {
	private HolidayOfDaily holidayOfDaily;
	
	/**
	 * 休暇使用時間の計算
	 * @return
	 */
	public static HolidayOfDaily calcUseRestTime(WorkType workType,
			 							  Optional<WorkTimeCode> siftCode,
			 							   WorkingConditionItem conditionItem,
			 							  List<OutingTimeOfDaily> goOutTimeOfDaily,
			 							  List<LateTimeOfDaily> lateTimeOfDaily,
			 							  List<LeaveEarlyTimeOfDaily> leaveEarlyTimeOfDaily,
			 							  ManageReGetClass recordReGet,
			 							  Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo) {
		
		Optional<PredetermineTimeSetForCalc> predSetting = recordReGet.getCalculatable()
																?Optional.of(recordReGet.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc())
																:Optional.empty();
		//***欠勤***//
		AttendanceTime absenceUseTime = vacationTimeOfcalcDaily(workType,VacationCategory.Absence,
																predSetting,
																predetermineTimeSetByPersonInfo,
																siftCode,
				  											    conditionItem,recordReGet.getHolidayAddtionSet());
		val absenceOfDaily = new AbsenceOfDaily(absenceUseTime);
		//***欠勤***//
		
		//***時間消化休暇***//
		AttendanceTime timeDigest = new AttendanceTime(0); 
		val timeDigestOfDaily = new TimeDigestOfDaily(timeDigest, new AttendanceTime(0));
		//***時間消化休暇***//
		
		//***積立年休使用時間***//
		AttendanceTime yearlyReservedTime = vacationTimeOfcalcDaily(workType,VacationCategory.YearlyReserved,
																	predSetting,predetermineTimeSetByPersonInfo,siftCode,
				  											  		conditionItem,recordReGet.getHolidayAddtionSet());
		val yearlyReservedOfDaily = new YearlyReservedOfDaily(yearlyReservedTime);
		//***積立年休使用時間***//
		
		//***代休***//
		AttendanceTime substituUseTime = vacationTimeOfcalcDaily(workType,VacationCategory.SubstituteHoliday,
																 predSetting,predetermineTimeSetByPersonInfo,siftCode,
																 conditionItem,recordReGet.getHolidayAddtionSet());
		int sumSubTime = goOutTimeOfDaily.stream()
										 .map(tc -> tc.getTimeVacationUseOfDaily().getTimeCompensatoryLeaveUseTime().valueAsMinutes())
										 .collect(Collectors.summingInt(tc -> tc))
						+ lateTimeOfDaily.stream()
										 .map(tc -> tc.getTimePaidUseTime().getTimeCompensatoryLeaveUseTime().valueAsMinutes())
										 .collect(Collectors.summingInt(tc -> tc))
						+ leaveEarlyTimeOfDaily.stream()
						 					   .map(tc -> tc.getTimePaidUseTime().getTimeCompensatoryLeaveUseTime().valueAsMinutes())
						 					   .collect(Collectors.summingInt(tc -> tc));
		substituUseTime = substituUseTime.addMinutes(sumSubTime);
		
		val substituteOfDaily = new SubstituteHolidayOfDaily(substituUseTime,new AttendanceTime(0));
		//***代休***//
		
		//***超過有休***//
		AttendanceTime overSalaryTime = vacationTimeOfcalcDaily(workType,VacationCategory.TimeDigestVacation,
																predSetting,predetermineTimeSetByPersonInfo,siftCode,
			  	 												 conditionItem,recordReGet.getHolidayAddtionSet());
		int sumOverTime = goOutTimeOfDaily.stream()
				 						  .map(tc -> tc.getTimeVacationUseOfDaily().getSixtyHourExcessHolidayUseTime().valueAsMinutes())
				 						  .collect(Collectors.summingInt(tc -> tc))
				 		  + lateTimeOfDaily.stream()
				 		  				  .map(tc -> tc.getTimePaidUseTime().getSixtyHourExcessHolidayUseTime().valueAsMinutes())
				 		  				  .collect(Collectors.summingInt(tc -> tc))
						  + leaveEarlyTimeOfDaily.stream()
							 			  .map(tc -> tc.getTimePaidUseTime().getSixtyHourExcessHolidayUseTime().valueAsMinutes())
							 			  .collect(Collectors.summingInt(tc -> tc));
		overSalaryTime = overSalaryTime.addMinutes(sumOverTime);
		
		val overSalaryOfDaily = new OverSalaryOfDaily(overSalaryTime, new AttendanceTime(0));
		//***超過有休***//
		
		//***特別休暇***//
		AttendanceTime specHolidayTime = vacationTimeOfcalcDaily(workType,VacationCategory.SpecialHoliday,
																 predSetting,predetermineTimeSetByPersonInfo,siftCode,
				 											  	 conditionItem,recordReGet.getHolidayAddtionSet());
		int sumSpecTime = goOutTimeOfDaily.stream()
				 						  .map(tc -> tc.getTimeVacationUseOfDaily().getTimeSpecialHolidayUseTime().valueAsMinutes())
				 						  .collect(Collectors.summingInt(tc -> tc))
				 		  + lateTimeOfDaily.stream()
				 		  				  .map(tc -> tc.getTimePaidUseTime().getTimeSpecialHolidayUseTime().valueAsMinutes())
										  .collect(Collectors.summingInt(tc -> tc))
						  + leaveEarlyTimeOfDaily.stream()
							 			  .map(tc -> tc.getTimePaidUseTime().getTimeSpecialHolidayUseTime().valueAsMinutes())
							 			  .collect(Collectors.summingInt(tc -> tc));
		specHolidayTime = specHolidayTime.addMinutes(sumSpecTime);
		
		val specHolidayOfDaily = new SpecialHolidayOfDaily(specHolidayTime, new AttendanceTime(0));
		//***特別休暇***//
		
		//***年休***//
		AttendanceTime annualUseTime = vacationTimeOfcalcDaily(workType,VacationCategory.AnnualHoliday,
															   predSetting,predetermineTimeSetByPersonInfo,siftCode,
				 											   conditionItem,recordReGet.getHolidayAddtionSet());
		int sumAnnTime = goOutTimeOfDaily.stream()
				 						 .map(tc -> tc.getTimeVacationUseOfDaily().getTimeAnnualLeaveUseTime().valueAsMinutes())
				 						 .collect(Collectors.summingInt(tc -> tc))
				 		 + lateTimeOfDaily.stream()
										 .map(tc -> tc.getTimePaidUseTime().getTimeAnnualLeaveUseTime().valueAsMinutes())
										 .collect(Collectors.summingInt(tc -> tc))
						 + leaveEarlyTimeOfDaily.stream()
						 					   .map(tc -> tc.getTimePaidUseTime().getTimeAnnualLeaveUseTime().valueAsMinutes())
						 					   .collect(Collectors.summingInt(tc -> tc));
		annualUseTime = annualUseTime.addMinutes(sumAnnTime);

		val annualOfDaily = new AnnualOfDaily(annualUseTime, new AttendanceTime(0));
		//***年休***//

		return new HolidayOfDaily(absenceOfDaily,
								  timeDigestOfDaily,
								  yearlyReservedOfDaily,
								  substituteOfDaily,
								  overSalaryOfDaily,
								  specHolidayOfDaily,
								  annualOfDaily
								  );
	}
	
	
	/**
	 * 日数単位の休暇時間計算
	 * @return　1日の時間内訳時間
	 */
	public static AttendanceTime vacationTimeOfcalcDaily(WorkType workType,
														 VacationCategory vacationCategory,
														 Optional<PredetermineTimeSetForCalc> predetermineTimeSet,
														 Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
														 Optional<WorkTimeCode> siftCode,
														 WorkingConditionItem conditionItem,
			 											 Optional<HolidayAddtionSet> holidayAdditionSet
			 											 ) {
		//使用する区分を持っている休暇種類であるかを判定しつつ、区分を持ってる休暇は使用するしないも見る
		if(!holidayAdditionSet.isPresent()) //|| holidayAdditionSet.get().getAdditionVacationSet().dicisionExistAndNotUse(vacationCategory))
			return new AttendanceTime(0);
		BreakDownTimeDay breakDownTimeDay = getVacationAddSet(predetermineTimeSet, siftCode, holidayAdditionSet.get(),conditionItem,predetermineTimeSetByPersonInfo);
		switch(workType.getDailyWork().decisionMatchWorkType(vacationCategory.convertWorkTypeClassification())) {
			//1日出勤系
			case FULL_TIME:
				return breakDownTimeDay.getOneDay();
			//午前出勤系
			case MORNING:
				return breakDownTimeDay.getMorning();
			//午後出勤系
			case AFTERNOON:
				return breakDownTimeDay.getAfternoon();
			//1日休暇
			case HOLIDAY:
				return new AttendanceTime(0);
			//例外
			default:
				throw new RuntimeException("unknown WorkType");
		}
	}
	
	/**
	 * 休暇加算設定の取得
	 * @param holidayAddtionSet. 実績の就業時間帯を参照する(休暇加算時間設定クラスのメンバ)
	 * @return
	 */
	private static BreakDownTimeDay getVacationAddSet(Optional<PredetermineTimeSetForCalc> predetermineTimeSet,Optional<WorkTimeCode> siftCode
											 ,HolidayAddtionSet holidayAddtionSet,WorkingConditionItem conditionItem,
											  Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo) {
		//参照する
		if(holidayAddtionSet.getReferActualWorkHours().equals(NotUseAtr.USE)) {
			if(siftCode.isPresent()) {
				return predetermineTimeSet.isPresent()?predetermineTimeSet.get().getAdditionSet().getAddTime()
													  :new BreakDownTimeDay(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0));
			}
			else {
				//会社一律の休暇加算
				if(holidayAddtionSet.getWorkRecord().isPresent() ) {
					val test = holidayAddtionSet.getWorkRecord().get().getAdditionTimeCompany();
					return (test.isPresent())
							?new BreakDownTimeDay(test.get().getOneDay(), test.get().getMorning(), test.get().getAfternoon())
							:new BreakDownTimeDay(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0));
				}
				//社員一律
				else {
					return getAddVacationTimeFromEmpInfo(holidayAddtionSet, conditionItem, predetermineTimeSetByPersonInfo);
				}
			}
		}
		//社員設定から取得
		else {
			return getAddVacationTimeFromEmpInfo(holidayAddtionSet, conditionItem, predetermineTimeSetByPersonInfo);
		}
	}

	/**
	 * 日単位の休暇加算時間の計算
	 * @author ken_takasu
	 * 
	 * @param statutoryDivision  割増区分（"通常"、"割増")
	 * @param workingSystem　　　　　労働制
	 * @param addSettingOfRegularWork　　
	 * @param vacationAddTimeSet
	 * @param workType
	 * @param predetermineTimeSet
	 * @param siftCode
	 * @param personalCondition
	 * @param addSettingOfIrregularWork
	 * @param addSettingOfFlexWork
	 * @return
	 */
	public VacationAddTime calcVacationAddTime(nts.uk.ctx.at.shared.dom.PremiumAtr premiumAtr,
											   WorkType workType,
											   WorkingSystem workingSystem,
											   Optional<WorkTimeCode> siftCode,
											   WorkingConditionItem conditionItem,
											   Optional<HolidayAddtionSet> holidayAdditionSet,
											   HolidayCalcMethodSet holidayCalcMethodSet,
											   Optional<PredetermineTimeSetForCalc> predTimeSettingForCalc,
											   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo
											   ) {
		VacationAddTime vacationAddTime;
		if (holidayAdditionSet.isPresent() && holidayCalcMethodSet.getCalcurationByActualTimeAtr(premiumAtr)==CalcurationByActualTimeAtr.CALCULATION_OTHER_THAN_ACTUAL_TIME) {// 実働時間以外も含めて計算する 場合
			// 加算時間の設定を取得
			BreakDownTimeDay breakdownTimeDay = getVacationAddSet(predTimeSettingForCalc, siftCode,holidayAdditionSet.get(),conditionItem,predetermineTimeSetByPersonInfo);
			// 休暇加算時間を加算するかどうか判断
			vacationAddTime = judgeVacationAddTime(breakdownTimeDay, workingSystem, premiumAtr,
												   holidayAdditionSet.get(), workType,holidayCalcMethodSet);
		} else {// 実働時間のみで計算する 場合
				// 休暇加算時間を全て 0 で返す
			vacationAddTime = new VacationAddTime(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0));
		}
		return vacationAddTime;
	}
	
	/**
	 * 休暇加算時間の計算
	 * @author ken_takasu
	 * @return
	 */
	public VacationAddTime judgeVacationAddTime(BreakDownTimeDay breakdownTimeDay, 
												WorkingSystem workingSystem,
												nts.uk.ctx.at.shared.dom.PremiumAtr premiumAtr, 
												HolidayAddtionSet holidayAddtionSet, 
												WorkType workType,
												HolidayCalcMethodSet holidayCalcMethodSet) {
		VacationAddTime vacationAddTime = new VacationAddTime(new AttendanceTime(0), new AttendanceTime(0),
				new AttendanceTime(0));
		// 加算する休暇設定を取得
		LeaveSetAdded leaveSetAdded = getAddVacationSet(premiumAtr, holidayAddtionSet, holidayCalcMethodSet);
		// 勤務区分をチェックする
		if (workType.isOneDay()) {
			val addTimes = checkVacationToAdd(leaveSetAdded, workType.getDailyWork().getOneDay(),breakdownTimeDay.getOneDay());
			vacationAddTime = vacationAddTime.addVacationAddTime(addTimes);
		} else {
			vacationAddTime = vacationAddTime.addVacationAddTime(checkVacationToAdd(leaveSetAdded, workType.getDailyWork().getMorning(),
					breakdownTimeDay.getMorning()));
			vacationAddTime = vacationAddTime.addVacationAddTime(checkVacationToAdd(leaveSetAdded,
					workType.getDailyWork().getAfternoon(), breakdownTimeDay.getAfternoon()));
		}
		return vacationAddTime;
	}

	/***
	 * 加算する休暇の種類を取得 
	 * @author ken_takasu
	 * @return
	 */
	public LeaveSetAdded getAddVacationSet(nts.uk.ctx.at.shared.dom.PremiumAtr premiumAtr,
											HolidayAddtionSet holidayAddtionSet,
											HolidayCalcMethodSet holidayCalcMethodSet) {
		LeaveSetAdded leaveSetAdded = new LeaveSetAdded(NotUseAtr.NOT_USE, NotUseAtr.NOT_USE, NotUseAtr.NOT_USE);// 下のif文に入らない場合は全てしないを返す
		// 休暇加算設定の取得
		if (holidayCalcMethodSet.getNotUseAtr(premiumAtr)==NotUseAtr.USE) {// 加算する場合
			leaveSetAdded = holidayAddtionSet.getAdditionVacationSet();
		}
		return leaveSetAdded;
	}

	/**
	 * 休暇加算時間を加算するかチェックする
	 * @author ken_takasu
	 * @return
	 */
	private VacationAddTime checkVacationToAdd(LeaveSetAdded leaveSetAdded,
											   WorkTypeClassification workTypeClassification, 
											   AttendanceTime attendanceTime) {
		VacationAddTime vacationAddTime = new VacationAddTime(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0));
		if (workTypeClassification.isAnnualLeave()) {// 年休の場合
			if (leaveSetAdded.getAnnualHoliday()==NotUseAtr.USE) {
				vacationAddTime = new VacationAddTime(attendanceTime, new AttendanceTime(0), new AttendanceTime(0));
			}
		} else if (workTypeClassification.isYearlyReserved()) {// 積立年休の場合
			if (leaveSetAdded.getYearlyReserved()==NotUseAtr.USE) {
				vacationAddTime = new VacationAddTime(new AttendanceTime(0), attendanceTime, new AttendanceTime(0));
			}
		} else if (workTypeClassification.isSpecialHoliday()) {// 特別休暇の場合
			if (leaveSetAdded.getSpecialHoliday()==NotUseAtr.USE) {
				vacationAddTime = new VacationAddTime(new AttendanceTime(0), new AttendanceTime(0), attendanceTime);
			}
		}
		return vacationAddTime;
	}
	
	/**
	 * 社員設定から休暇加算時間取得
	 * @param holidayAdditionSet 休暇加算時間設定
	 * @param workConditionItem 労働条件項目ドメイン
	 * @param predTimeForCalc　所定時間設定(計算用)
	 * @return 1日の時間内訳
	 */
	public static BreakDownTimeDay getAddVacationTimeFromEmpInfo(HolidayAddtionSet holidayAdditionSet,WorkingConditionItem workConditionItem,Optional<PredetermineTimeSetForCalc> predTimeForCalc) {
		BreakDownTimeDay addTime = new BreakDownTimeDay(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0));
		
		if(holidayAdditionSet == null || !holidayAdditionSet.getEmployeeInformation().isPresent()) return addTime;
		
		//労働条件項目側から時間を取得
		if(holidayAdditionSet.getEmployeeInformation().get().getTimeReferenceDestination().isWorkHourDurWeekDay()) {
			addTime = workConditionItem.getHolidayAddTimeSet().isPresent()
									? new BreakDownTimeDay(workConditionItem.getHolidayAddTimeSet().get().getOneDay(),
														   workConditionItem.getHolidayAddTimeSet().get().getMorning(),
														   workConditionItem.getHolidayAddTimeSet().get().getAfternoon())
									: addTime;
		}
		//個人情報(平日)側にある就業時間帯の設定から取得
		else {
			if(predTimeForCalc != null) {
				addTime = predTimeForCalc.isPresent()?predTimeForCalc.get().getAdditionSet().getAddTime()
													 :new BreakDownTimeDay(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0));
			}
		}
		return addTime;
	}

//	/**
//	 * 休暇加算設定の取得
//	 * @author ken_takasu
//	 * @param workingSystem
//	 * @return
//	 */
//	private CalculationByActualTimeAtr getCalculationByActualTimeAtr(WorkingSystem workingSystem,
//																	 StatutoryDivision statutoryDivision,
//																	 AddSettingOfRegularWork addSettingOfRegularWork,
//																	 AddSettingOfIrregularWork addSettingOfIrregularWork, 
//																	 AddSettingOfFlexWork addSettingOfFlexWork) {
//		switch (workingSystem) {
//		case REGULAR_WORK:
//			return addSettingOfRegularWork.getCalculationByActualTimeAtr(statutoryDivision);
//
//		case FLEX_TIME_WORK:
//			return addSettingOfFlexWork.getCalculationByActualTimeAtr(statutoryDivision);
//
//		case VARIABLE_WORKING_TIME_WORK:
//			return addSettingOfIrregularWork.getCalculationByActualTimeAtr(statutoryDivision);
//
//		case EXCLUDED_WORKING_CALCULATE:
//			throw new RuntimeException("不正な労働制です");
//		default:
//			throw new RuntimeException("不正な労働制です");
//		}
//	}


}
