package nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.bonuspaytime.BonusPayTime;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.daily.midnight.MidNightTimeSheet;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ActualWorkTimeSheetAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.BonusPayAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CommonFixedWorkTimezoneSet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ConditionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.FlexWithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateLeaveEarlyManagementTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManageReGetClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetOfDeductionItem;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationAddTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.valueobject.WorkHour;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.calculation.holiday.HolidayAddtionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkDeformedLaborAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkFlexAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.WorkRegularAdditionSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.PremiumCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.WorkTimeCalcMethodDetailOfHoliday;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.ENUM.CalcurationByActualTimeAtr;
import nts.uk.ctx.at.shared.dom.common.TimeOfDay;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.worktime.common.AmPmAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.LateEarlyAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneLateEarlySet;
import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 就業時間内時間帯
 * @author keisuke_hoshina
 *
 */
//@AllArgsConstructor
@Getter
public class WithinWorkTimeSheet implements LateLeaveEarlyManagementTimeSheet{

	//就業時間内時間枠
	private final List<WithinWorkTimeFrame> withinWorkTimeFrame;
	//短時間時間帯
	private List<TimeSheetOfDeductionItem> shortTimeSheet;
	//早退判断時刻
	private List<LeaveEarlyDecisionClock> leaveEarlyDecisionClock = new ArrayList<>();
	//遅刻判断時刻
	private List<LateDecisionClock> lateDecisionClock = new ArrayList<>();
	
	
	public WithinWorkTimeSheet(List<WithinWorkTimeFrame> withinWorkTimeFrame,List<TimeSheetOfDeductionItem> shortTimeSheets,Optional<LateDecisionClock> lateDecisionClock,Optional<LeaveEarlyDecisionClock> leaveEarlyDecisionClock) {
		this.withinWorkTimeFrame = withinWorkTimeFrame;
		this.shortTimeSheet = shortTimeSheets;
		if(lateDecisionClock != null
			&& lateDecisionClock.isPresent())
			this.lateDecisionClock.add(lateDecisionClock.get());
		if(leaveEarlyDecisionClock != null
			&& leaveEarlyDecisionClock.isPresent())
			this.leaveEarlyDecisionClock.add(leaveEarlyDecisionClock.get());
	}
	
	
	/**
	 * 就業時間内時間帯
	 * 
	 * @param workType 勤務種類クラス
	 * @param predetermineTimeSet 所定時間設定クラス
	 * @param fixedWorkSetting 固定勤務設定クラス
	 * @param workTimeCommonSet 就業時間帯の共通設定
	 * @param deductionTimeSheet 控除時間帯
	 * @param bonuspaySetting 加給設定
	 * @param personalCondition 
	 * @param vacationClass 
	 * @param late 
	 * @param late 
	 * @param leaveEarly 
	 * @param leaveEarly 
	 * @param workingSystem 
	 * @param workingSystem 
	 * @param illegularAddSetting 
	 * @param illegularAddSetting 
	 * @param flexAddSetting 
	 * @param flexAddSetting 
	 * @param regularAddSetting 
	 * @param regularAddSetting 
	 * @param holidayAddtionSet 
	 * @param holidayAddtionSet 
	 * @param calcMethod 
	 * @param calcMethod 
	 * @param autoCalcAtr 
	 * @param autoCalcAtr 
	 * @param flexCalcMethod 
	 * @param flexCalcMethod 
	 * @param workTimeDailyAtr 
	 * @param workTimeDailyAtr 
	 * @param workTimeCode 
	 * @param workTimeCode 
	 * @param preFlexTime 
	 * @param preFlexTime 
	 * @param timeVacationAdditionRemainingTime 
	 * @param predetermineTimeForSet 
	 * @param timeVacationAdditionRemainingTime 
	 * @return 就業時間内時間帯
	 */
	public static WithinWorkTimeSheet createAsFixed(TimeLeavingWork timeLeavingWork,
													WorkType workType,
													PredetermineTimeSetForCalc predetermineTimeSetForCalc,
													CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
													WorkTimezoneCommonSet workTimeCommonSet,
													DeductionTimeSheet deductionTimeSheet,
													Optional<BonusPaySetting> bonuspaySetting,
													MidNightTimeSheet midNightTimeSheet,
													int workNo,
													Optional<CoreTimeSetting> coreTimeSetting,
													HolidayCalcMethodSet holidayCalcMethodSet,
													WorkTimezoneLateEarlySet workTimezoneLateEarlySet,
													DailyUnit dailyUnit,List<TimeSheetOfDeductionItem> breakTimeList, 
													VacationClass vacationClass, boolean late, boolean leaveEarly, 
													WorkingSystem workingSystem, 
													WorkDeformedLaborAdditionSet illegularAddSetting, 
													WorkFlexAdditionSet flexAddSetting, 
													WorkRegularAdditionSet regularAddSetting, 
													HolidayAddtionSet holidayAddtionSet, 
													CalcMethodOfNoWorkingDay calcMethod, 
													Optional<SettingOfFlexWork> flexCalcMethod, 
													Optional<WorkTimeDailyAtr> workTimeDailyAtr, 
													Optional<WorkTimeCode> workTimeCode, 
													AttendanceTime preFlexTime, 
													Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime,
													Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
													Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
													Optional<DeductLeaveEarly> deductLeaveEarly,
													Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets) {
		
		List<WithinWorkTimeFrame> timeFrames = new ArrayList<>();
		List<TimeSheetOfDeductionItem> shortTimeSheets = new ArrayList<>();
		//遅刻判断時刻
		Optional<LateDecisionClock> lateDesClock = Optional.empty();
		//遅刻判断時刻
		Optional<LeaveEarlyDecisionClock> leaveEarlyDesClock = Optional.empty();
	
		if(workType.isWeekDayAttendance()) {
			//遅刻判断時刻を求める
			lateDesClock = LateDecisionClock.create(
					workNo,
					predetermineTimeSetForCalc,
					deductionTimeSheet,
					workTimeCommonSet.getLateEarlySet().getOtherEmTimezoneLateEarlySet(LateEarlyAtr.LATE).getGraceTimeSet(),
					timeLeavingWork,
					coreTimeSetting,workType,breakTimeList);
			//早退判断時刻を求める
			leaveEarlyDesClock = LeaveEarlyDecisionClock.create(
					workNo,
					predetermineTimeSetForCalc,
					deductionTimeSheet,
					workTimeCommonSet.getLateEarlySet().getOtherEmTimezoneLateEarlySet(LateEarlyAtr.EARLY).getGraceTimeSet(),
					timeLeavingWork,
					coreTimeSetting,workType,breakTimeList);
				
			//就業時間内枠の作成
			timeFrames = isWeekDayProcess(
					timeLeavingWork,
					workType,
					predetermineTimeSetForCalc,
					 lstHalfDayWorkTimezone,
					 workTimeCommonSet,
					 deductionTimeSheet,
					 bonuspaySetting,
					 midNightTimeSheet,
					 workNo,
					lateDesClock,
					leaveEarlyDesClock,
					 holidayCalcMethodSet,
					 workTimezoneLateEarlySet,
					coreTimeSetting,
					dailyUnit,breakTimeList, 
					 vacationClass, 
					 late, 
					 leaveEarly, 
					 workingSystem, 
					 illegularAddSetting, 
					 flexAddSetting, 
					 regularAddSetting, 
					 holidayAddtionSet, 
					 calcMethod, 
					 flexCalcMethod, 
					 workTimeDailyAtr, 
					 workTimeCode, 
					 preFlexTime, 
					 timeVacationAdditionRemainingTime,
					 commonSetting,
					 conditionItem,
					 predetermineTimeSetByPersonInfo,
					 deductLeaveEarly, specificDateAttrSheets
					 );
			//短時間時間帯の取得
			shortTimeSheets = toHaveShortTime(timeFrames,deductionTimeSheet);
		}
		return new WithinWorkTimeSheet(timeFrames,shortTimeSheets,lateDesClock,leaveEarlyDesClock);
	}

	
	
	
	/**
	 * 就業時間内時間帯の作成
	 * @param personalCondition 
	 * @param vacationClass 
	 * @param late 
	 * @param leaveEarly 
	 * @param workingSystem 
	 * @param illegularAddSetting 
	 * @param flexAddSetting 
	 * @param regularAddSetting 
	 * @param holidayAddtionSet 
	 * @param calcMethod 
	 * @param autoCalcAtr 
	 * @param flexCalcMethod 
	 * @param workTimeDailyAtr 
	 * @param workTimeCode 
	 * @param preFlexTime 
	 * @param timeVacationAdditionRemainingTime 
	 * @param workType　勤務種類クラス
	 * @param predetermineTimeSet 所定時間設定クラス
	 * @param fixedWorkSetting  固定勤務設定クラス
	 * @return 就業時間内時間帯クラス
	 */
	private static List<WithinWorkTimeFrame> isWeekDayProcess(
			TimeLeavingWork timeLeavingWork,
			WorkType workType,
			PredetermineTimeSetForCalc predetermineTimeForSet,
			CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
			WorkTimezoneCommonSet workTimeCommonSet,
			DeductionTimeSheet deductionTimeSheet,
			Optional<BonusPaySetting> bonuspaySetting,
			MidNightTimeSheet midNightTimeSheet,
			int workNo,
			Optional<LateDecisionClock> lateDesClock,
			Optional<LeaveEarlyDecisionClock> leaveEarlyDesClock,
			HolidayCalcMethodSet holidayCalcMethodSet,
			WorkTimezoneLateEarlySet workTimezoneLateEarlySet,
			Optional<CoreTimeSetting> coreTimeSetting,
			DailyUnit dailyUnit,List<TimeSheetOfDeductionItem> breakTimeList, 
			VacationClass vacationClass, 
			boolean late, 
			boolean leaveEarly, 
			WorkingSystem workingSystem, 
			WorkDeformedLaborAdditionSet illegularAddSetting, 
			WorkFlexAdditionSet flexAddSetting, 
			WorkRegularAdditionSet regularAddSetting, 
			HolidayAddtionSet holidayAddtionSet, 
			CalcMethodOfNoWorkingDay calcMethod, 
			Optional<SettingOfFlexWork> flexCalcMethod, 
			Optional<WorkTimeDailyAtr> workTimeDailyAtr, 
			Optional<WorkTimeCode> workTimeCode, 
			AttendanceTime preFlexTime, 
			Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime,
			Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
			Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			Optional<DeductLeaveEarly> deductLeaveEarly,
			Optional<SpecificDateAttrOfDailyPerfor> specificDateAttrSheets
			) {
		
		val timeFrames = new ArrayList<WithinWorkTimeFrame>();
		//出退勤時刻と↑の重複時間帯と重複部分取得
		List<WithinWorkTimeFrame> withinWorkTimeFrame = duplicatedByStamp(timeLeavingWork,lstHalfDayWorkTimezone,workType);
		
		for(WithinWorkTimeFrame duplicateTimeSheet :withinWorkTimeFrame) {
			//就業時間内時間枠の作成
			timeFrames.add(WithinWorkTimeFrame.createWithinWorkTimeFrame(duplicateTimeSheet,
																		 deductionTimeSheet,
																		 bonuspaySetting,
																		 midNightTimeSheet,
																		 lateDesClock,
																		 leaveEarlyDesClock,
																		 timeLeavingWork,
																		 holidayCalcMethodSet,
																		 workNo,
																		 workTimezoneLateEarlySet,
																		 predetermineTimeForSet.getTimeSheets(workType.getDailyWork().decisionNeedPredTime(),workNo),
																		 coreTimeSetting,breakTimeList,workType,predetermineTimeForSet,commonSetting, specificDateAttrSheets,
																		 duplicateTimeSheet.getWorkingHoursTimeNo().v().intValue() == 1,
																		 duplicateTimeSheet.getWorkingHoursTimeNo().v().intValue() == withinWorkTimeFrame.size()));
		}
		/*所定内割増時間の時間帯作成*/
		return predetermineWithinPremiumTime(dailyUnit.getDailyTime(),predetermineTimeForSet.getAdditionSet().getPredTime().getPredetermineWorkTime(),timeFrames, 
				vacationClass, 
				workType, 
				late, 
				leaveEarly, 
				workingSystem, 
				illegularAddSetting, 
				flexAddSetting, 
				regularAddSetting, 
				holidayAddtionSet, 
				holidayCalcMethodSet, 
				calcMethod, 
				flexCalcMethod, 
				workTimeDailyAtr, 
				workTimeCode, 
				preFlexTime, 
				coreTimeSetting, 
				predetermineTimeForSet, 
				timeVacationAdditionRemainingTime,
				lateDesClock, 
				leaveEarlyDesClock,
				dailyUnit,commonSetting,
				conditionItem,
				predetermineTimeSetByPersonInfo,
				deductLeaveEarly,
				deductionTimeSheet,
				NotUseAtr.NOT_USE
				);
	}
	
	
	/**
	 * 自身が持つ短時間勤務時間帯(控除)を収集
	 * @return　短時間勤務時間帯
	 */
	public static List<TimeSheetOfDeductionItem> toHaveShortTime(List<WithinWorkTimeFrame> afterWithinPremiumCreate,DeductionTimeSheet dedTimeSheet) {
		List<TimeSheetOfDeductionItem> returnList = new ArrayList<>();
		List<TimeSheetOfDeductionItem> allInFrameShortTimeSheets = new ArrayList<>();
		for(WithinWorkTimeFrame frame : afterWithinPremiumCreate) {
			allInFrameShortTimeSheets.addAll(frame.collectShortTimeSheetInFrame());
		}
		//マスタ側の控除時間帯ループ
		val loopList = dedTimeSheet.getForRecordTimeZoneList().stream().filter(tc -> tc.getDeductionAtr().isChildCare()).collect(Collectors.toList());
		for(TimeSheetOfDeductionItem masterDedItem : loopList) {
			List<TimeSheetOfDeductionItem> notDupShort = Arrays.asList(masterDedItem);
			for(TimeSheetOfDeductionItem dedItem:allInFrameShortTimeSheets) {
				//ループ中短時間のどこにも所属していない時間帯
				List<TimeSpanForCalc> timeReplace = notDupShort.stream().map(tc -> tc.getTimeSheet().getTimeSpan().getNotDuplicationWith(dedItem.getTimeSheet().getTimeSpan())).flatMap(List::stream).collect(Collectors.toList());
				timeReplace = timeReplace.stream().filter(ts -> ts.getSpan().lengthAsMinutes() > 0).collect(Collectors.toList());
				notDupShort = timeReplace.stream().map(ts -> dedItem.replaceTimeSpan(Optional.of(ts))).collect(Collectors.toList());
			}
			if(!notDupShort.isEmpty()) {
				returnList.addAll(notDupShort);
			}
		}		
		return returnList;
	}


	/**
	 * 所定内割増時間の分割
	 * @param timeFrames 
	 * @param statutoryTime　法定労働時間
	 * @param predTime　所定時間
	 */
	private static List<WithinWorkTimeFrame> predetermineWithinPremiumTime(TimeOfDay statutoryTime, int predTime, List<WithinWorkTimeFrame> timeFrames,
			 VacationClass vacationClass, 
			 WorkType workType, 
			 boolean late, 
			 boolean leaveEarly, 
			 WorkingSystem workingSystem, 
			 WorkDeformedLaborAdditionSet illegularAddSetting, 
			 WorkFlexAdditionSet flexAddSetting, 
			 WorkRegularAdditionSet regularAddSetting, 
			 HolidayAddtionSet holidayAddtionSet, 
			 HolidayCalcMethodSet holidayCalcMethodSet, 
			 CalcMethodOfNoWorkingDay calcMethod, 
			 Optional<SettingOfFlexWork> flexCalcMethod, 
			 Optional<WorkTimeDailyAtr> workTimeDailyAtr, 
			 Optional<WorkTimeCode> workTimeCode, 
			 AttendanceTime preFlexTime, 
			 Optional<CoreTimeSetting> coreTimeSetting, 
			 PredetermineTimeSetForCalc predetermineTimeSetForCalc, 
			 Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime,
			 Optional<LateDecisionClock> lateDesClock,
			 Optional<LeaveEarlyDecisionClock> leaveEarlyDesClock,
			 DailyUnit dailyUnit,Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
			 Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<DeductLeaveEarly> deductLeaveEarly,
			 DeductionTimeSheet dedTimeSheet,NotUseAtr lateEarlyMinusAtr) {
		AttendanceTime restPredeterminePremiumTime = calcPredeterminePremiumTime(predTime, statutoryTime,
				  new WithinWorkTimeSheet(timeFrames, toHaveShortTime(timeFrames,dedTimeSheet),lateDesClock, leaveEarlyDesClock),
				  vacationClass,
				  workType,
				  late, 
				  leaveEarly, 
				  workingSystem, 
				  illegularAddSetting, 
				  flexAddSetting, 
				  regularAddSetting, 
				  holidayAddtionSet, 
				  holidayCalcMethodSet, 
				  calcMethod, 
				  flexCalcMethod, 
				  workTimeDailyAtr, 
				  workTimeCode, 
				  preFlexTime, 
				  coreTimeSetting, 
				  predetermineTimeSetForCalc, 
				  timeVacationAdditionRemainingTime,
				  dailyUnit,commonSetting,conditionItem,
				  predetermineTimeSetByPersonInfo,
				  deductLeaveEarly,
				  lateEarlyMinusAtr);
		List<WithinWorkTimeFrame> copyWithinWorkTime = timeFrames.stream().sorted((first,second) -> second.getTimeSheet().getStart().compareTo(first.getTimeSheet().getStart())).collect(Collectors.toList());
		for(WithinWorkTimeFrame workTimeFrame : copyWithinWorkTime) {
			if(restPredeterminePremiumTime.greaterThan(0)) {
				AttendanceTime redeterminePremiumTime = (restPredeterminePremiumTime.greaterThan(new AttendanceTime(workTimeFrame.getTimeSheet().getTimeSpan().lengthAsMinutes())))
														?new AttendanceTime(workTimeFrame.getTimeSheet().timeSpan().lengthAsMinutes())
														:restPredeterminePremiumTime;
														
				Optional<TimeSpanForCalc> timeSpan = workTimeFrame.createTimeSpan(workTimeFrame.getCalcrange(),new TimeWithDayAttr(redeterminePremiumTime.valueAsMinutes()));
				if(!timeSpan.isPresent())
					continue;
				workTimeFrame.setPremiumTimeSheetInPredetermined(Optional.of(new WithinPremiumTimeSheetForCalc(new TimeSpanForCalc(timeSpan.get().getEnd(),workTimeFrame.getTimeSheet().getEnd()))));
				
				//ここ注意　minusMinutes(XXXX) XXXは作成した所定内割増時間分？それともredeterminePremiumTime？
				restPredeterminePremiumTime = restPredeterminePremiumTime.minusMinutes(timeSpan.get().lengthAsMinutes());
				
			}
			else {
				break;
			}
		}
		return copyWithinWorkTime;
	}

	/**
	 * 所定内割増時間の計算
	 * @param predetermineTimeSet 計算用所定時 
	 * @param statutoryTime 法定労働時間
	 */
	public static AttendanceTime calcPredeterminePremiumTime(int predTime,TimeOfDay statutoryTime,
			 WithinWorkTimeSheet withinTimeSheet, 
			 VacationClass vacationClass, 
			 WorkType workType, 
			 boolean late, 
			 boolean leaveEarly, 
			 WorkingSystem workingSystem, 
			 WorkDeformedLaborAdditionSet illegularAddSetting, 
			 WorkFlexAdditionSet flexAddSetting, 
			 WorkRegularAdditionSet regularAddSetting, 
			 HolidayAddtionSet holidayAddtionSet, 
			 HolidayCalcMethodSet holidayCalcMethodSet, 
			 CalcMethodOfNoWorkingDay calcMethod, 
			 Optional<SettingOfFlexWork> flexCalcMethod, 
			 Optional<WorkTimeDailyAtr> workTimeDailyAtr, 
			 Optional<WorkTimeCode> workTimeCode, 
			 AttendanceTime preFlexTime, 
			 Optional<CoreTimeSetting> coreTimeSetting, 
			 PredetermineTimeSetForCalc predetermineTimeSetForCalc, 
			 Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime,
			 DailyUnit dailyUnit,Optional<WorkTimezoneCommonSet> commonSetting,WorkingConditionItem conditionItem,
			 Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			 Optional<DeductLeaveEarly> deductLeaveEarly,
			 NotUseAtr lateEarlyMinusAtr) {
		if( statutoryTime.greaterThan(0)
			&& predTime > statutoryTime.valueAsMinutes()) {
			return decisionPredeterminePremiumTime(statutoryTime, 
												  withinTimeSheet,
												  vacationClass,
												  workType,
												  late, 
												  leaveEarly, 
												  workingSystem, 
												  illegularAddSetting, 
												  flexAddSetting, 
												  regularAddSetting, 
												  holidayAddtionSet, 
												  holidayCalcMethodSet, 
												  calcMethod, 
												  flexCalcMethod, 
												  workTimeDailyAtr, 
												  workTimeCode, 
												  preFlexTime, 
												  coreTimeSetting, 
												  predetermineTimeSetForCalc, 
												  timeVacationAdditionRemainingTime,
												  dailyUnit,commonSetting,
												  conditionItem,
												  predetermineTimeSetByPersonInfo,
												  deductLeaveEarly,
												  lateEarlyMinusAtr
												  );
		}
		else {
			return new AttendanceTime(0);
		}
	}
	
	/**
	 * 所定内割増時間を計算
	 * @param predetermineTime　計算用所定時間
	 * @param statutoryTime 法定労働時間
	 * @param withinTimeSheet 
	 * @param personalCondition 
	 * @param vacationClass 
	 * @param workType 
	 * @param late 
	 * @param leaveEarly 
	 * @param workingSystem 
	 * @param illegularAddSetting 
	 * @param flexAddSetting 
	 * @param regularAddSetting 
	 * @param holidayAddtionSet 
	 * @param holidayCalcMethodSet 
	 * @param calcMethod 
	 * @param autoCalcAtr 
	 * @param flexCalcMethod 
	 * @param workTimeDailyAtr 
	 * @param workTimeCode 
	 * @param preFlexTime 
	 * @param coreTimeSetting 
	 * @param predetermineTimeSetForCalc 
	 * @param timeVacationAdditionRemainingTime 
	 * @return 所定内割増時間
	 */
	public static AttendanceTime decisionPredeterminePremiumTime(TimeOfDay statutoryTime, 
																 WithinWorkTimeSheet withinTimeSheet, 
																 VacationClass vacationClass, 
																 WorkType workType, 
																 boolean late, 
																 boolean leaveEarly, 
																 WorkingSystem workingSystem, 
																 WorkDeformedLaborAdditionSet illegularAddSetting, 
																 WorkFlexAdditionSet flexAddSetting, 
																 WorkRegularAdditionSet regularAddSetting, 
																 HolidayAddtionSet holidayAddtionSet, 
																 HolidayCalcMethodSet holidayCalcMethodSet, 
																 CalcMethodOfNoWorkingDay calcMethod, 
																 Optional<SettingOfFlexWork> flexCalcMethod, 
																 Optional<WorkTimeDailyAtr> workTimeDailyAtr, 
																 Optional<WorkTimeCode> workTimeCode, 
																 AttendanceTime preFlexTime, 
																 Optional<CoreTimeSetting> coreTimeSetting, 
																 PredetermineTimeSetForCalc predetermineTimeSetForCalc, 
																 Finally<TimevacationUseTimeOfDaily> timeVacationAdditionRemainingTime,
																 DailyUnit dailyUnit,Optional<WorkTimezoneCommonSet> commonSetting,
																 WorkingConditionItem conditionItem,
																 Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
																 Optional<DeductLeaveEarly> deductLeaveEarly,
																 NotUseAtr lateEarlyMinusAtr) {
		//就業時間計算
		AttendanceTime workTime = WithinStatutoryTimeOfDaily.calcWithinStatutoryTime(withinTimeSheet,
														   vacationClass,
														   workType,
														   late, 
														   leaveEarly, 
														   workingSystem, 
														   illegularAddSetting, 
														   flexAddSetting, 
														   regularAddSetting, 
														   holidayAddtionSet, 
														   holidayCalcMethodSet, 
														   calcMethod, 
														   flexCalcMethod, 
														   workTimeDailyAtr, 
														   workTimeCode, 
														   preFlexTime, 
														   coreTimeSetting, 
														   predetermineTimeSetForCalc, 
														   timeVacationAdditionRemainingTime,
														   dailyUnit,commonSetting,
														   conditionItem,
														   predetermineTimeSetByPersonInfo,
														   deductLeaveEarly,
														   lateEarlyMinusAtr
														   );
		return workTime.minusMinutes(statutoryTime.valueAsMinutes());
	}
	
	/***
	 * 出勤、退勤時刻との重複部分を調べる
	 * @param workingHourSet 就業時間枠の時間帯
	 * @param timeLeavingWork　出退勤
	 * @return　時間枠の時間帯と出退勤の重複時間
	 */
	private static List<WithinWorkTimeFrame> duplicatedByStamp(
			TimeLeavingWork timeLeavingWork,CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,WorkType workType) {
		List<WithinWorkTimeFrame> returnList = new ArrayList<>();
		Optional<TimeSpanForCalc> duplicatedRange = Optional.empty(); 
		val attendanceHolidayAttr = workType.getAttendanceHolidayAttr();
		val emTimeZoneSet = getWorkingHourSetByAmPmClass(lstHalfDayWorkTimezone, attendanceHolidayAttr);
		
		for(EmTimeZoneSet timeZone:emTimeZoneSet) {
			duplicatedRange = timeZone.getTimezone().getDuplicatedWith(timeLeavingWork.getTimespan());
			if(duplicatedRange.isPresent()) {
				returnList.add(new WithinWorkTimeFrame(timeZone.getEmploymentTimeFrameNo(),
														new TimeZoneRounding(duplicatedRange.get().getStart(),duplicatedRange.get().getEnd(),timeZone.getTimezone().getRounding()),
														new TimeSpanForCalc(timeZone.getTimezone().getStart(), timeZone.getTimezone().getEnd()),
														new ArrayList<>(),
														new ArrayList<>(),
														new ArrayList<>(),
														Optional.empty(),
														new ArrayList<>(),
														Optional.empty(),
														Optional.empty()));
			}else {
				returnList.add(new WithinWorkTimeFrame(timeZone.getEmploymentTimeFrameNo(),
													   new TimeZoneRounding(timeZone.getTimezone().getStart(),timeZone.getTimezone().getStart(),timeZone.getTimezone().getRounding()),
													   new TimeSpanForCalc(timeZone.getTimezone().getStart(), timeZone.getTimezone().getEnd()),
													   new ArrayList<>(),
													   new ArrayList<>(),
													   new ArrayList<>(),
													   Optional.empty(),
													   new ArrayList<>(),
													   Optional.empty(),
													   Optional.empty()));
			}
		}
		return returnList;
	}

	/**
	 * 平日出勤の出勤時間帯を取得
	 * @param fixedWorkSetting 固定勤務設定クラス
	 * @param attendanceHolidayAttr 出勤休日区分
	 * @return 出勤時間帯
	 */
	private static List<EmTimeZoneSet> getWorkingHourSetByAmPmClass(
			CommonFixedWorkTimezoneSet lstHalfDayWorkTimezone,
			AttendanceHolidayAttr attendanceHolidayAttr) {
		
		switch (attendanceHolidayAttr) {
		case FULL_TIME:
		case HOLIDAY:
			return lstHalfDayWorkTimezone.getFixedWorkTimezoneMap().get(AmPmAtr.ONE_DAY).getLstWorkingTimezone();
		case MORNING:
			return lstHalfDayWorkTimezone.getFixedWorkTimezoneMap().get(AmPmAtr.AM).getLstWorkingTimezone();
		case AFTERNOON:
			return lstHalfDayWorkTimezone.getFixedWorkTimezoneMap().get(AmPmAtr.PM).getLstWorkingTimezone();
		default:
			throw new RuntimeException("unknown attendanceHolidayAttr" + attendanceHolidayAttr);
		}
	}
	
//	/**
//	 * 引数のNoと一致する遅刻判断時刻を取得する
//	 * @param workNo
//	 * @return　遅刻判断時刻
//	 */
//	public LateDecisionClock getlateDecisionClock(int workNo) {
//		List<LateDecisionClock> clockList = this.lateDecisionClock.stream().filter(tc -> tc.getWorkNo()==workNo).collect(Collectors.toList());
//		if(clockList.size()>1) {
//			throw new RuntimeException("Exist duplicate workNo : " + workNo);
//		}
//		return clockList.get(0);
//	}
	
//	/**
//	 * 引数のNoと一致する早退判断時刻を取得する
//	 * @param workNo
//	 * @return　早退判断時刻
//	 */
//	public LeaveEarlyDecisionClock getleaveEarlyDecisionClock(int workNo) {
//		List<LeaveEarlyDecisionClock> clockList = this.leaveEarlyDecisionClock.stream().filter(tc -> tc.getWorkNo()==workNo).collect(Collectors.toList());
//		if(clockList.size()>1) {
//			throw new RuntimeException("Exist duplicate workNo : " + workNo);
//		}
//		return clockList.get(0);
//	}
//	
	/**
	 * コアタイムのセット
	 * @param coreTimeSetting コアタイム時間設定
	 */
	public WithinWorkTimeSheet createWithinFlexTimeSheet(CoreTimeSetting coreTimeSetting,DeductionTimeSheet dedTimeSheet) {
		List<TimeSpanForCalc> duplicateCoreTimeList = new ArrayList<>();
		for(WithinWorkTimeFrame workTimeFrame : this.withinWorkTimeFrame) {
			Optional<TimeSpanForCalc> duplicateSpan = workTimeFrame.getCalcrange().getDuplicatedWith(new TimeSpanForCalc(coreTimeSetting.getCoreTimeSheet().getStartTime(),
																									 					 coreTimeSetting.getCoreTimeSheet().getEndTime())); 
			if(duplicateSpan.isPresent()) {
				duplicateCoreTimeList.add(duplicateSpan.get());
			}
		}
		TimeWithDayAttr startTime = new TimeWithDayAttr(0);
		TimeWithDayAttr endTime = new TimeWithDayAttr(0);
		if(!duplicateCoreTimeList.isEmpty()) {
			startTime = duplicateCoreTimeList.stream().sorted((first,second)-> first.getStart().compareTo(second.getStart())).collect(Collectors.toList()).get(0).getStart();
			endTime = duplicateCoreTimeList.stream().sorted((first,second)-> first.getStart().compareTo(second.getStart())).collect(Collectors.toList()).get(duplicateCoreTimeList.size() - 1).getEnd();
			/*フレックス時間帯に入れる*/
			return new FlexWithinWorkTimeSheet(this.withinWorkTimeFrame,toHaveShortTime(this.withinWorkTimeFrame,dedTimeSheet),Optional.of(new TimeSpanForCalc(startTime, endTime)));
		}
		else {
			return new FlexWithinWorkTimeSheet(this.withinWorkTimeFrame,toHaveShortTime(this.withinWorkTimeFrame,dedTimeSheet),Optional.empty());
		}
	}
	
	
	/**
	 * 就業時間(法定内用)の計算
	 * @param calcActualTime 実働のみで計算する
	 * @param dedTimeSheet　控除時間帯
	 * @return 就業時間の計算結果
	 */
	public AttendanceTime calcWorkTimeForStatutory(PremiumAtr premiumAtr,
			   CalcurationByActualTimeAtr calcActualTime,
			   TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
			   VacationClass vacationClass,
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
			   HolidayCalcMethodSet holidayCalcMethodSet, DailyUnit dailyUnit, Optional<WorkTimezoneCommonSet> commonSetting,
			   WorkingConditionItem conditionItem,ManageReGetClass recordReget,Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<CoreTimeSetting> coreTimeSetting
			   ,DeductLeaveEarly deductLeaveEarly,NotUseAtr lateEarlyMinusAtr) {
		return calcWorkTime(
					premiumAtr,
					calcActualTime,
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
				    regularAddSetting,
				    holidayAddtionSet,
				    holidayCalcMethodSet,
				    dailyUnit,commonSetting,
				    conditionItem,
				    predetermineTimeSetByPersonInfo,coreTimeSetting
				    ,HolidayAdditionAtr.HolidayAddition.convertFromCalcByActualTimeToHolidayAdditionAtr(calcActualTime),
				    deductLeaveEarly,
				    lateEarlyMinusAtr
				).getWorkTime();
	}
	
	
	/**
	 * 就業時間の計算(控除時間差し引いた後)
	 * @param dailyUnit 
	 * @param leaveEarlyTime 
	 * @param lateTime 
	 * @param withinpremiumTime 
	 * @return 就業時間
	 */
	public WorkHour calcWorkTime(PremiumAtr premiumAtr,
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
									   HolidayCalcMethodSet holidayCalcMethodSet, DailyUnit dailyUnit, Optional<WorkTimezoneCommonSet> commonSetting,
									   WorkingConditionItem conditionItem,Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,Optional<CoreTimeSetting> coreTimeSetting
									   ,HolidayAdditionAtr holidayAddition,DeductLeaveEarly deductLeaveEarly,NotUseAtr lateEarlyMinusAtr
									   ) {
		
		//就業時間計算
		AttendanceTime workTime = calcWorkTimeBeforeDeductPremium(holidayAddition,
																  timevacationUseTimeOfDaily,
																  workingSystem,
																  regularAddSetting,
																  illegularAddSetting, 
																  flexAddSetting,
																  holidayAddtionSet,
																  late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
																  leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
																  holidayCalcMethodSet,
																  premiumAtr,commonSetting,coreTimeSetting,
																  calcActualTime,
																  vacationClass,
																  statutoryDivision,
																  workType,
																  predetermineTimeSet,
																  siftCode,
																  dailyUnit,
																  conditionItem,
																  predetermineTimeSetByPersonInfo,
																  lateEarlyMinusAtr
																  );
		//フレの時は上限値制御をしたくない。
		//フレの時は法定労働時間が0として設定されてきているため↓のif文でフレをスキップセル
		if(dailyUnit.getDailyTime().greaterThan(0)) {
			AttendanceTime withinpremiumTime =  predetermineTimeSet != null 
												&& predetermineTimeSet.getpredetermineTime(workType.getDailyWork()).greaterThan(dailyUnit.getDailyTime().valueAsMinutes())
					?new AttendanceTime(this.withinWorkTimeFrame.stream()
																					  .filter(tc -> tc.getPremiumTimeSheetInPredetermined().isPresent())
																					  .map(tc -> tc.getPremiumTimeSheetInPredetermined().get().getTimeSheet().lengthAsMinutes())
																					  .collect(Collectors.summingInt(tc -> tc)))
																					  
					:new AttendanceTime(0);
			//就業時間に含まれてしまっている所定内割増時間を差し引く
			workTime = workTime.minusMinutes(withinpremiumTime.valueAsMinutes());
		}
		
		int holidayAddTime = 0;
		if(holidayAddition.isHolidayAddition()) {
			//休暇加算時間を計算
			VacationAddTime vacationAddTime = vacationClass.calcVacationAddTime(premiumAtr,
																				workType,
																				workingSystem,
																				siftCode, 
																				conditionItem,
																				Optional.of(holidayAddtionSet),
																				holidayCalcMethodSet,
																				predetermineTimeSet == null ? Optional.empty():Optional.of(predetermineTimeSet),
																				predetermineTimeSetByPersonInfo
																				);
			holidayAddTime = vacationAddTime.calcTotaladdVacationAddTime();
			//休暇加算時間を加算
			workTime = workTime.addMinutes(holidayAddTime);
		}
		
		//コア無しフレックスで遅刻した場合の時間補正
		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()&&coreTimeSetting.isPresent()&&!coreTimeSetting.get().isUseTimeSheet()) {
			//遅刻時間を就業時間から控除しない場合
			if(!holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().isDeductLateLeaveEarly(commonSetting)) {
				TimeWithCalculation calcedLateTime = calcNoCoreCalcLateTimeForWorkTime(workTime,
																			DeductionAtr.Appropriate,
																			late,
																			holidayCalcMethodSet,
																			coreTimeSetting,
																			commonSetting
																			);
				//コア無しフレックス遅刻時間　＞　0 の場合
				if(calcedLateTime.getCalcTime().greaterThan(0)) {
					workTime = coreTimeSetting.get().getMinWorkTime();
				}
			}
		}
//		if(!deductLeaveEarly.isDeduct()) {
//			val calcLateLeaveTime = calcLateLeaveEarlyinWithinWorkTime();
//			workTime = workTime.minusMinutes(calcLateLeaveTime.getCalcTime().valueAsMinutes());
//		}
		
			
		return new WorkHour(workTime, new AttendanceTime(holidayAddTime));
	}
	

	
	/**
	 * 就業時間内時間枠の全枠分の就業時間を算出する
	 * (所定内割増時間を差し引く前)
	 * @return 就業時間
	 */
	public AttendanceTime calcWorkTimeBeforeDeductPremium(HolidayAdditionAtr holidayAdditionAtr,
														  TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
														  WorkingSystem workingSystem,
														  WorkRegularAdditionSet addSettingOfRegularWork,
														  WorkDeformedLaborAdditionSet addSettingOfIrregularWork, 
														  WorkFlexAdditionSet addSettingOfFlexWork,
														  HolidayAddtionSet holidayAddtionSet,
														  boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
														  boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
														  HolidayCalcMethodSet holidayCalcMethodSet,
														  PremiumAtr premiumAtr,Optional<WorkTimezoneCommonSet> commonSetting,Optional<CoreTimeSetting> coreTimeSetting,
														  
														  CalcurationByActualTimeAtr calcActualTime,
														   VacationClass vacationClass,
														   StatutoryDivision statutoryDivision,
														   WorkType workType,
														   PredetermineTimeSetForCalc predetermineTimeSet,
														   Optional<WorkTimeCode> siftCode,DailyUnit dailyUnit,WorkingConditionItem conditionItem,
														   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
														   NotUseAtr lateEarlyMinusAtr
														  ) {
		boolean decisionDeductChild = decisionDeductChild(premiumAtr,holidayCalcMethodSet,commonSetting);
		AttendanceTime workTime = new AttendanceTime(0);
		for(WithinWorkTimeFrame copyItem: withinWorkTimeFrame) {
			//workTime.addMinutes(copyItem.calcActualWorkTimeAndWorkTime(holidayAdditionAtr,dedTimeSheet).v());
			workTime = new AttendanceTime(workTime.v()+copyItem.calcActualWorkTimeAndWorkTime(holidayAdditionAtr,
																							  timevacationUseTimeOfDaily,
																							  workingSystem,
																							  addSettingOfRegularWork,
																							  addSettingOfIrregularWork, 
																							  addSettingOfFlexWork,
																							  holidayAddtionSet,
																							  late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
																							  leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
																							  holidayCalcMethodSet,
																							  premiumAtr,commonSetting,
																							  lateEarlyMinusAtr
																							  ).v());
			if(decisionDeductChild) {
				//遅刻が持つ育児or介護時間
				if(copyItem.getLateTimeSheet().isPresent()&&copyItem.getLateTimeSheet().get().getDecitionTimeSheet(DeductionAtr.Deduction).isPresent()) {
					workTime = workTime.addMinutes(copyItem.getLateTimeSheet().get().getDecitionTimeSheet(DeductionAtr.Deduction).get().calcDedTimeByAtr(DeductionAtr.Deduction,ConditionAtr.Child).valueAsMinutes());
					workTime = workTime.addMinutes(copyItem.getLateTimeSheet().get().getDecitionTimeSheet(DeductionAtr.Deduction).get().calcDedTimeByAtr(DeductionAtr.Deduction,ConditionAtr.Care).valueAsMinutes());
				}
				//早退が持つ育児or介護時間
				if(copyItem.getLeaveEarlyTimeSheet().isPresent()&&copyItem.getLeaveEarlyTimeSheet().get().getDecitionTimeSheet(DeductionAtr.Deduction).isPresent()) {
					workTime = workTime.addMinutes(copyItem.getLeaveEarlyTimeSheet().get().getDecitionTimeSheet(DeductionAtr.Deduction).get().calcDedTimeByAtr(DeductionAtr.Deduction,ConditionAtr.Child).valueAsMinutes());
					workTime = workTime.addMinutes(copyItem.getLeaveEarlyTimeSheet().get().getDecitionTimeSheet(DeductionAtr.Deduction).get().calcDedTimeByAtr(DeductionAtr.Deduction,ConditionAtr.Care).valueAsMinutes());
				}
			}		
		}
		return workTime;
	}
	
	/**
	 * 遅刻早退が保持する育児時間を控除するか判定
	 * @param premiumAtr
	 * @param holidayCalcMethodSet
	 * @param commonSetting
	 * @return
	 */
	private boolean decisionDeductChild(PremiumAtr premiumAtr,HolidayCalcMethodSet holidayCalcMethodSet,Optional<WorkTimezoneCommonSet> commonSetting) {
		boolean decisionDeductChild = false;
		if(premiumAtr.isRegularWork()) {			
			Optional<WorkTimeCalcMethodDetailOfHoliday> advancedSet = holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet();	
				if(advancedSet.isPresent()
					&& advancedSet.get().getNotDeductLateLeaveEarly().isEnableSetPerWorkHour()
					&&commonSetting.isPresent()) {
					if(advancedSet.isPresent()&&advancedSet.get().getCalculateIncludCareTime()==NotUseAtr.USE
							&&commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime()) {
						decisionDeductChild = true;
					}
				}else {
					if(advancedSet.isPresent()&&advancedSet.get().getCalculateIncludCareTime()==NotUseAtr.USE
							&&advancedSet.get().getNotDeductLateLeaveEarly().isDeduct()) {
						decisionDeductChild = true;
					}
				}
		}else {
			Optional<PremiumCalcMethodDetailOfHoliday> advanceSet = holidayCalcMethodSet.getPremiumCalcMethodOfHoliday().getAdvanceSet();
				if(advanceSet.isPresent()){
					if(advanceSet.get().getNotDeductLateLeaveEarly().isEnableSetPerWorkHour()&&commonSetting.isPresent()) {
						if(advanceSet.get().getCalculateIncludCareTime()==NotUseAtr.USE
								&&commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime()) {
							decisionDeductChild = true;
						}
					}else {
						if(advanceSet.get().getCalculateIncludCareTime()==NotUseAtr.USE&&
								advanceSet.get().getNotDeductLateLeaveEarly().isDeduct()) {
							decisionDeductChild = true;
						}
					}
				}
		}
		return decisionDeductChild;
	}
	
	/**コア無しフレックス遅刻時間の計算
	 * 
	 * @return
	 */
	public TimeWithCalculation calcNoCoreCalcLateTimeForWorkTime(AttendanceTime workTime,
													  DeductionAtr deductionAtr,
													   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
													   HolidayCalcMethodSet holidayCalcMethodSet,
													   Optional<CoreTimeSetting> coreTimeSetting,
													   Optional<WorkTimezoneCommonSet> commonSetting
													  ){		
		//遅刻時間の計算
		AttendanceTime lateTime = calcLateTimeForWorkTime(workTime,deductionAtr,
											   holidayCalcMethodSet,
											   coreTimeSetting,
											   commonSetting
											   );
		//時間休暇との相殺処理(いずれ実装が必要)
		
		//遅刻早退の自動計算設定．遅刻をチェック
		if(late) {
			return TimeWithCalculation.sameTime(lateTime);
		}
		return TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0), lateTime);
	}
	
	/**
	 * 遅刻時間の計算
	 * フレックスのコア無しの場合専用の遅刻時間の計算処理(就業時間計算用)
	 * @param workTime
	 * @param deductionAtr
	 * @param coreTimeSetting
	 * @param holidayCalcMethodSet
	 * @param commonSetting
	 * @return
	 */
	public AttendanceTime calcLateTimeForWorkTime(AttendanceTime workTime,DeductionAtr deductionAtr,
			   HolidayCalcMethodSet holidayCalcMethodSet,
			   Optional<CoreTimeSetting> coreTimeSetting,
			   Optional<WorkTimezoneCommonSet> commonSetting
			) {
		//パラメータ「控除区分」＝”控除”　かつ　控除しない
		if(deductionAtr.isDeduction()&&holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()&&!holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().isDeductLateLeaveEarly(commonSetting)) {
			return new AttendanceTime(0);
		}
		//遅刻時間の計算   (最低勤務時間　－　パラメータで受け取った就業時間)
		AttendanceTime result = coreTimeSetting.get().getMinWorkTime().minusMinutes(workTime.valueAsMinutes());
		//計算結果がマイナスの場合は0
		if(result.valueAsMinutes()<0) {
			return new AttendanceTime(0);
		}
		return result;
	}
	
	
	
	/**コア無しフレックス遅刻時間の計算
	 * 
	 * @return
	 */
	public TimeWithCalculation calcNoCoreCalcLateTime(DeductionAtr deductionAtr,
													  
													   PremiumAtr premiumAtr, 
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
													   Optional<CoreTimeSetting> coreTimeSetting,
													   DailyUnit dailyUnit,
													   Optional<WorkTimezoneCommonSet> commonSetting,
													   WorkingConditionItem conditionItem,
													   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
		   												List<LateTimeOfDaily> lateTimes, 
		   												List<LeaveEarlyTimeOfDaily> leaveEarlyTime,
		   												Optional<DeductLeaveEarly> deductLeaveEarly,
		   												NotUseAtr lateEarlyMinusAtr
													  ){
		
		//遅刻時間の計算
		AttendanceTime lateTime = calcLateTime(deductionAtr,
											   premiumAtr,
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
											   coreTimeSetting,
											   dailyUnit,
											   commonSetting,
											   conditionItem,
											   predetermineTimeSetByPersonInfo,
											   lateTimes,
											   leaveEarlyTime,
											   lateEarlyMinusAtr
											   );
		//時間休暇との相殺処理(いずれ実装が必要)
		//丸め
//		if(commonSetting.isPresent()) {
//			val setting = commonSetting.get().getLateEarlySet().getOtherEmTimezoneLateEarlySet(LateEarlyAtr.LATE).getRoundingSetByDedAtr(deductionAtr.isDeduction());
//			lateTime = new AttendanceTime(setting.round(lateTime.valueAsMinutes()));
//		}
//		
		//遅刻早退の自動計算設定．遅刻をチェック
		if(late) {
			return TimeWithCalculation.sameTime(lateTime);
		}
		return TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0), lateTime);
	}
	
	/**
	 * 遅刻時間の計算
	 * フレックスのコア無しの場合専用の遅刻時間の計算処理
	 * @param workTime
	 * @param deductionAtr
	 * @param coreTimeSetting
	 * @param holidayCalcMethodSet
	 * @param commonSetting
	 * @return
	 */
	public AttendanceTime calcLateTime(DeductionAtr deductionAtr,
			   PremiumAtr premiumAtr, 
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
			   Optional<CoreTimeSetting> coreTimeSetting,
			   DailyUnit dailyUnit,
			   Optional<WorkTimezoneCommonSet> commonSetting,
			   WorkingConditionItem conditionItem,
			   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			   List<LateTimeOfDaily> lateTime, 
			   List<LeaveEarlyTimeOfDaily> leaveEarlyTime,
			   NotUseAtr lateEarlyMinusAtr
			) {
		//パラメータ「控除区分」＝”控除”　かつ　控除しない
		if(deductionAtr.isDeduction()&&holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()&&!holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().isDeductLateLeaveEarly(commonSetting)) {
			return new AttendanceTime(0);
		}
		
		DeductLeaveEarly leaveLateset = new DeductLeaveEarly(1,1);
		if(flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()) {
			leaveLateset = flexAddSetting.getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly();
		}
		//コア無し計算遅刻時間
		AttendanceTime noCore = clacNoCoreWorkTime(premiumAtr,
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
				   								   coreTimeSetting,
				   								   dailyUnit,
				   								   commonSetting,
				   								   conditionItem,
				   								   predetermineTimeSetByPersonInfo,
												   lateTime, 
												   leaveEarlyTime,
												   leaveLateset,
												   lateEarlyMinusAtr);
		//遅刻時間の計算   (最低勤務時間　－　パラメータで受け取った就業時間)
		AttendanceTime result = coreTimeSetting.get().getMinWorkTime().minusMinutes(noCore.valueAsMinutes());
		//計算結果がマイナスの場合は0
		if(result.valueAsMinutes()<0) {
			return new AttendanceTime(0);
		}
		return result;
	}
	
	/**
	 * コアタイム無し遅刻時間計算用の就業時間の計算
	 * 遅刻時間の計算時にのみ利用する（就業時間計算時には利用しない）
	 * @return
	 */
	public AttendanceTime clacNoCoreWorkTime(PremiumAtr premiumAtr, 
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
			   								 Optional<CoreTimeSetting> coreTimeSetting,
			   								 DailyUnit dailyUnit,
			   								 Optional<WorkTimezoneCommonSet> commonSetting,
			   								 WorkingConditionItem conditionItem,
			   								 Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
											 List<LateTimeOfDaily> lateTime, 
											 List<LeaveEarlyTimeOfDaily> leaveEarlyTime,
											 DeductLeaveEarly deductLeaveEarly,
											 NotUseAtr lateEarlyMinusAtr
			   								 ) {
		
		//遅刻、早退の控除設定を「控除する」に変更する
		HolidayCalcMethodSet changeHolidayCalcMethodSet = new HolidayCalcMethodSet(holidayCalcMethodSet.getPremiumCalcMethodOfHoliday(),holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().changeDeduct());
		//就業時間帯の遅刻早退を控除するかを見る場合、就業時間帯の遅刻、早退の控除設定を「控除する」に変更する
		Optional<WorkTimezoneCommonSet> changeCommonSetting = commonSetting;
		if(holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().isPresent()&&
		   holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet().get().getNotDeductLateLeaveEarly().isEnableSetPerWorkHour()&&
		   commonSetting.isPresent()) {
			 changeCommonSetting = Optional.of(commonSetting.get().changeWorkTimezoneLateEarlySet());
		}
		
		//就業時間（法定内用）の計算
		AttendanceTime result = calcWorkTime(premiumAtr,
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
											 changeHolidayCalcMethodSet,
											 dailyUnit,
											 changeCommonSetting,
											 conditionItem,
											 predetermineTimeSetByPersonInfo,
											 coreTimeSetting
											 ,HolidayAdditionAtr.HolidayAddition.convertFromCalcByActualTimeToHolidayAdditionAtr(calcActualTime),
											 deductLeaveEarly,
											 lateEarlyMinusAtr
				).getWorkTime();
		
		return result;
	}
	
	
//	/**
//	 * 遅刻早退時間の計算
//	 * @return
//	 */
//	public TimeWithCalculation calcLateLeaveEarlyinWithinWorkTime() {
//		TimeWithCalculation totalValue = TimeWithCalculation.sameTime(new AttendanceTime(0));
//		for(WithinWorkTimeFrame workTimeFrame : withinWorkTimeFrame) {
//			if(workTimeFrame.getLateTimeSheet().isPresent()) {
//				TimeWithCalculation timeValue = workTimeFrame.getLateTimeSheet().get().calcDedctionTime(true, NotUseAtr.USE);
//				totalValue = totalValue.addMinutes(timeValue.getTime(), timeValue.getCalcTime());
//			}
//			if(workTimeFrame.getLeaveEarlyTimeSheet().isPresent()) {
//				TimeWithCalculation timeValue = workTimeFrame.getLeaveEarlyTimeSheet().get().calcDedctionTime(true, NotUseAtr.USE);
//				totalValue = totalValue.addMinutes(timeValue.getTime(), timeValue.getCalcTime());
//			}
//		}
//		return totalValue;
//	}
	
//	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
//	
//	//就業時間内時間帯クラスを作成　　（流動勤務）
//	public WithinWorkTimeSheet createAsFluidWork(
//			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
//			WorkType workType,
//			WorkInfoOfDailyPerformance workInformationOfDaily,
//			FluidWorkSetting fluidWorkSetting,
//			DeductionTimeSheet deductionTimeSheet) {
//		//開始時刻を取得
//		TimeWithDayAttr startClock = getStartClock();
//		//所定時間帯、残業開始を補正
//		cllectPredetermineTimeAndOverWorkTimeStart();
//		//残業開始となる経過時間を取得
//		AttendanceTime elapsedTime = fluidWorkSetting.getWeekdayWorkTime().getWorkTimeSheet().getMatchWorkNoOverTimeWorkSheet(1).getFluidWorkTimeSetting().getElapsedTime();
//		//経過時間から終了時刻を計算
//		TimeWithDayAttr endClock = startClock.backByMinutes(elapsedTime.valueAsMinutes());
//		//就業時間帯の作成（一時的に作成）
//		TimeSpanForCalc workTimeSheet = new TimeSpanForCalc(startClock,endClock);
//		//控除時間帯を取得 (控除時間帯分ループ）
//		for(TimeSheetOfDeductionItem timeSheetOfDeductionItem : deductionTimeSheet.getForDeductionTimeZoneList()) {
//			//就業時間帯に重複する控除時間を計算
//			TimeSpanForCalc duplicateTime = workTimeSheet.getDuplicatedWith(timeSheetOfDeductionItem.getTimeSheet().getSpan()).orElse(null);
//			//就業時間帯と控除時間帯が重複しているかチェック
//			if(duplicateTime!=null) {
//				//控除項目の時間帯に法定内区分をセット
//				timeSheetOfDeductionItem = new TimeSheetOfDeductionItem(
//						timeSheetOfDeductionItem.getTimeSheet().getSpan(),
//						timeSheetOfDeductionItem.getGoOutReason(),
//						timeSheetOfDeductionItem.getBreakAtr(),
//						timeSheetOfDeductionItem.getDeductionAtr(),
//						WithinStatutoryAtr.WithinStatutory);
//				//控除時間分、終了時刻をズラす
//				endClock.backByMinutes(duplicateTime.lengthAsMinutes());
//				//休暇加算するかチェックしてズラす
//				
//			}		
//		}
//		//就業時間内時間帯クラスを作成
//		
//		
//		
//	}
//	
//	/**
//	 * 開始時刻を取得　　（流動勤務（平日・就内））
//	 * @return
//	 */
//	public TimeWithDayAttr getStartClock() {
//		
//	}
//	
//	
//	//所定時間帯、残業開始を補正
//	public void cllectPredetermineTimeAndOverWorkTimeStart(
//			PredetermineTimeSetForCalc predetermineTimeSetForCalc,
//			WorkType workType,
//			WorkInfoOfDailyPerformance workInformationOfDaily) {
//		//所定時間帯を取得
//		predetermineTimeSetForCalc.correctPredetermineTimeSheet(workType.getDailyWork());
//		//予定所定時間が変更された場合に所定時間を変更するかチェック
//		//勤務予定と勤務実績の勤怠情報を比較
//		//勤務種類が休日出勤でないかチェック
//		if(
//				!workInformationOfDaily.isMatchWorkInfomation()||
//				workType.getDailyWork().isHolidayWork()
//				) {
//			return;
//		}
//		//就業時間帯の所定時間と予定時間を比較
//			
//		//計算用所定時間設定を所定終了ずらす時間分ズラす
//		
//		//流動勤務時間帯設定の残業時間帯を所定終了ずらす時間分ズラす
//		
//	}
//	
//	

	
//	/**
//	 * 渡した時間帯(List)を1つの時間帯に結合する
//	 * @param list
//	 * @return
//	 */
//	public TimeSpanForCalc bondTimeSpan(List<TimeSpanForCalc> list) {
//		TimeWithDayAttr start = list.stream().map(ts -> ts.getStart()).min(Comparator.naturalOrder()).get();
//		TimeWithDayAttr end =  list.stream().map(ts -> ts.getEnd()).max(Comparator.naturalOrder()).get();
//		TimeSpanForCalc bondTimeSpan = new TimeSpanForCalc(start, end);
//		return bondTimeSpan;
//	}


//	/**
//	 * 指定された計算区分を基に計算付き時間帯を作成する
//	 * @return
//	 */
//	public TimeWithCalculation calcClacificationjudge(boolean clacification , int calcTime) {
//		if(clacification) {
//			return TimeWithCalculation.sameTime(new AttendanceTime(calcTime));
//		}else {
//			return TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0),new AttendanceTime(calcTime));
//		}
//	}
	
//	//遅刻早退時間帯（List）を1つの遅刻早退時間帯に結合する
//	public LateLeaveEarlyTimeSheet connect(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		 return new LeaveEarlyTimeSheet(createMinStartMaxEndSpanForTimeSheet(timeSheetList),
//		 								createMinStartMaxEndSpanForCalcRange(timeSheetList),
//		 								extract(List<LeaveEarlyTimeSheet> timeSheetList),
//		 								Optional.empty(),
//		 								Optional.empty(),
//		 								Optional.empty());
//	}
//	//遅刻早退時間帯（List）の時間帯（丸め付）を1つの時間帯（丸め付）にする
//	public TimeSpanWithRounding createMinStartMaxEndSpanForTimeSheet(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		TimeWithDayAttr start = timeSheetList.stream().map(ts -> ts.getTimeSheet().getStart()).min(Comparator.naturalOrder()).get();
//		TimeWithDayAttr end =  timeSheetList.stream().map(ts -> ts.getTimeSheet().getEnd()).max(Comparator.naturalOrder()).get();
//		return new TimeSpanWithRounding(start, end, rounding);//丸めはどうする？
//	}
//	//遅刻早退時間帯（List）の計算範囲を1つの計算範囲にする
//	public TimeSpanForCalc createMinStartMaxEndSpanForCalcRange(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		TimeWithDayAttr start = timeSheetList.stream().map(ts -> ts.getCalcrange().getStart()).min(Comparator.naturalOrder()).get();
//		TimeWithDayAttr end =  timeSheetList.stream().map(ts -> ts.getCalcrange().getEnd()).max(Comparator.naturalOrder()).get();
//		return new TimeSpanForCalc(start,end);
//	}
//	//遅刻早退時間帯（List）の控除項目の時間帯（List）を1つの控除項目の時間帯（List）にする
//	public List<TimeSheetOfDeductionItem> extract(List<LateLeaveEarlyTimeSheet> timeSheetList){
//		return timeSheetList.stream().map(tc -> tc.getDeductionTimeSheets()).collect(Collectors.toList());
//	}
	
	
//	/**
//	 * 遅刻時間の休暇時間相殺
//	 * @return
//	 */
//	public DeductionOffSetTime calcDeductionOffSetTime(
//			TimevacationUseTimeOfDaily TimeVacationAdditionRemainingTime,//時間休暇使用残時間を取得する
//			LateTimeSheet lateTimeSheet,
//			DeductionAtr deductionAtr) {
//		TimeSpanForCalc calcRange;
//		//計算範囲の取得
//		if(deductionAtr.isDeduction()) {//パラメータが控除の場合
//			calcRange = lateTimeSheet.getForDeducationTimeSheet().get().getTimeSheet().getSpan();
//		}else {//パラメータが計上の場合
//			calcRange = lateTimeSheet.getForRecordTimeSheet().get().getTimeSheet().getSpan();
//		}
//		//遅刻時間を求める
//		int lateRemainingTime = calcRange.lengthAsMinutes();
//		//時間休暇相殺を利用して相殺した各時間を求める
//		DeductionOffSetTime deductionOffSetTime = createDeductionOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime);
//		
//		return 	deductionOffSetTime;
//	}
	
	
//	/**
//	 * 時間休暇相殺を利用して相殺した各時間を求める  （一時的に作成）
//	 * @return
//	 */
//	public DeductionOffSetTime createDeductionOffSetTime(int lateRemainingTime,TimevacationUseTimeOfDaily TimeVacationAdditionRemainingTime) {
//		
//		AttendanceTime timeAnnualLeaveUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getTimeAnnualLeaveUseTime());
//		lateRemainingTime -= timeAnnualLeaveUseTime.valueAsMinutes();
//
//		AttendanceTime timeCompensatoryLeaveUseTime = new AttendanceTime(0);
//		AttendanceTime sixtyHourExcessHolidayUseTime = new AttendanceTime(0);
//		AttendanceTime timeSpecialHolidayUseTime = new AttendanceTime(0);
//		
//		if(lateRemainingTime > 0) {
//			timeCompensatoryLeaveUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getTimeCompensatoryLeaveUseTime());
//			lateRemainingTime -= timeCompensatoryLeaveUseTime.valueAsMinutes();
//		}
//		
//		if(lateRemainingTime > 0) {
//			sixtyHourExcessHolidayUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getSixtyHourExcessHolidayUseTime());
//			lateRemainingTime -= sixtyHourExcessHolidayUseTime.valueAsMinutes();
//		}
//		
//		if(lateRemainingTime > 0) {
//			timeSpecialHolidayUseTime = calcOffSetTime(lateRemainingTime,TimeVacationAdditionRemainingTime.getTimeSpecialHolidayUseTime());
//			lateRemainingTime -= timeSpecialHolidayUseTime.valueAsMinutes();
//		}
//				
//		return new DeductionOffSetTime(
//				timeAnnualLeaveUseTime,
//				timeCompensatoryLeaveUseTime,
//				sixtyHourExcessHolidayUseTime,
//				timeSpecialHolidayUseTime);
//	}

	
	/**
	 * 
	 * @param lateRemainingTime 遅刻残数
	 * @param timeVacationUseTime　時間休暇使用時間
	 * @return
	 */
	public AttendanceTime calcOffSetTime(int lateRemainingTime,AttendanceTime timeVacationUseTime) {
		int offSetTime;
		//相殺する時間を計算（比較）する
		if(timeVacationUseTime.lessThanOrEqualTo(lateRemainingTime)) {
			offSetTime = timeVacationUseTime.valueAsMinutes();
		}else {
			offSetTime = lateRemainingTime;
		}
		return new AttendanceTime(offSetTime);
	}

		
	//＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
	
	/**
	 * 就業時間内時間帯に入っている加給時間の計算
	 */
	public List<BonusPayTime> calcBonusPayTimeInWithinWorkTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(WithinWorkTimeFrame timeFrame : withinWorkTimeFrame) {
			bonusPayList.addAll(timeFrame.calcBonusPay(ActualWorkTimeSheetAtr.WithinWorkTime,raisingAutoCalcSet,bonusPayAutoCalcSet,calcAtrOfDaily,bonusPayAtr));
		}
		//同じNo同士はここで加算し、Listのサイズを減らす
		return sumBonusPayTime(bonusPayList);
	}
	/**
	 * 就業時間内時間帯に入っている特定加給時間の計算
	 */
	public List<BonusPayTime> calcSpecifiedBonusPayTimeInWithinWorkTime(AutoCalRaisingSalarySetting raisingAutoCalcSet,BonusPayAutoCalcSet bonusPayAutoCalcSet,BonusPayAtr bonusPayAtr,CalAttrOfDailyPerformance calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
		for(WithinWorkTimeFrame timeFrame : withinWorkTimeFrame) {
			bonusPayList.addAll(timeFrame.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.WithinWorkTime,raisingAutoCalcSet,bonusPayAutoCalcSet, calcAtrOfDaily,bonusPayAtr));
		}
		//同じNo同士はここで加算し、Listのサイズを減らす
		return sumBonusPayTime(bonusPayList);
	}
	
	/**
	 * 同じ加給時間Ｎｏを持つものを１つにまとめる
	 * @param bonusPayTime　加給時間
	 * @return　Noでユニークにした加給時間List
	 */
	private List<BonusPayTime> sumBonusPayTime(List<BonusPayTime> bonusPayTime){
		List<BonusPayTime> returnList = new ArrayList<>();
		List<BonusPayTime> refineList = new ArrayList<>();
		for(int bonusPayNo = 1 ; bonusPayNo <= 10 ; bonusPayNo++) {
			refineList = getByBonusPayNo(bonusPayTime, bonusPayNo);
			if(refineList.size()>0) {
				returnList.add(new BonusPayTime(bonusPayNo,
												new AttendanceTime(refineList.stream().map(tc -> tc.getBonusPayTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))),
												TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(refineList.stream().map(tc -> tc.getWithinBonusPay().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))),
																							  new AttendanceTime(refineList.stream().map(tc -> tc.getWithinBonusPay().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc)))),
												TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(refineList.stream().map(tc -> tc.getExcessBonusPayTime().getTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))),
																							  new AttendanceTime(refineList.stream().map(tc -> tc.getExcessBonusPayTime().getCalcTime().valueAsMinutes()).collect(Collectors.summingInt(tc -> tc))))
												));
			}
		}
		return returnList;
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
	 * 法定内深夜時間の計算
	 * @return　法定内深夜時間
	 */
	public AttendanceTime calcMidNightTime() {
		int totalMidNightTime = 0;
//		int totalDedTime = 0;
		totalMidNightTime = withinWorkTimeFrame.stream()
											   .filter(tc -> tc.getMidNightTimeSheet().isPresent())
											   .map(ts -> ts.getMidNightTimeSheet().get().calcTotalTime(DeductionAtr.Deduction).v())
											   .collect(Collectors.summingInt(tc -> tc));
		
//		for(WithinWorkTimeFrame frametime : withinWorkTimeFrame) {
//			val a = frametime.getDedTimeSheetByAtr(DeductionAtr.Deduction, ConditionAtr.BREAK);
//			if(frametime.getMidNightTimeSheet().isPresent()) {
//				totalDedTime += a.stream().filter(tc -> tc.getCalcrange().getDuplicatedWith(frametime.getMidNightTimeSheet().get().getCalcrange()).isPresent())
//										 .map(tc -> tc.getCalcrange().getDuplicatedWith(frametime.getMidNightTimeSheet().get().getCalcrange()).get().lengthAsMinutes())
//										 .collect(Collectors.summingInt(tc->tc));
//			}
//		}
//		
//		totalMidNightTime -= totalDedTime;
		return new AttendanceTime(totalMidNightTime);
	}
	
	/**
	 * 全枠の中に入っている控除時間(控除区分に従って)を合計する
	 * @return 控除合計時間
	 */
	public AttendanceTime calculationAllFrameDeductionTime(DeductionAtr dedAtr,ConditionAtr atr,PremiumAtr premiumAtr,HolidayCalcMethodSet holidayCalcMethodSet,Optional<WorkTimezoneCommonSet> commonSetting) {
		AttendanceTime totalTime = new AttendanceTime(0);
		for(WithinWorkTimeFrame frameTime : this.withinWorkTimeFrame) {
			val addTime = frameTime.forcs(atr,dedAtr).valueAsMinutes();
			int forLateAddTime = 0;
			int forLeaveAddTime = 0;
			//遅刻が保持する控除時間の合計取得
			if(decisionGetLateLeaveHaveChild(premiumAtr,holidayCalcMethodSet,commonSetting,atr)) {
				if(frameTime.getLateTimeSheet().isPresent()) {
					if(frameTime.getLateTimeSheet().get().getDecitionTimeSheet(dedAtr).isPresent()) {
						forLateAddTime = frameTime.getLateTimeSheet().get().getDecitionTimeSheet(dedAtr).get().forcs(atr, dedAtr).valueAsMinutes();
					}
				}
				//早退が保持する控除時間の合計取得
				if(frameTime.getLeaveEarlyTimeSheet().isPresent()) {
					if(frameTime.getLeaveEarlyTimeSheet().get().getDecitionTimeSheet(dedAtr).isPresent()) {
						forLeaveAddTime = frameTime.getLeaveEarlyTimeSheet().get().getDecitionTimeSheet(dedAtr).get().forcs(atr, dedAtr).valueAsMinutes();
					}
				}
			}
			totalTime = totalTime.addMinutes(addTime+forLateAddTime+forLeaveAddTime);
		}
		if(dedAtr.isAppropriate() && (atr.isCare() || atr.isChild()))
				totalTime = totalTime.addMinutes(this.shortTimeSheet.stream().map(tc -> tc.calcTotalTime(dedAtr).valueAsMinutes()).collect(Collectors.summingInt(ts -> ts)));
		
		return totalTime;
	}
	
	/**
	 * 遅刻早退が保持する育児時間を取得するか判断
	 * 遅刻早退が就業時間から控除されない場合は就業時間が育児時間を保持しているので遅刻早退が保持する育児を取得する必要がない
	 * @param premiumAtr
	 * @param holidayCalcMethodSet
	 * @param commonSetting
	 * @param atr 
	 * @return
	 */
	private boolean decisionGetLateLeaveHaveChild(PremiumAtr premiumAtr,HolidayCalcMethodSet holidayCalcMethodSet,Optional<WorkTimezoneCommonSet> commonSetting, ConditionAtr atr) {
		boolean decisionGetChild = false;
		if(!(atr.isCare() || atr.isChild()))
				return false;
		if(premiumAtr.isRegularWork()) {			
			Optional<WorkTimeCalcMethodDetailOfHoliday> advancedSet = holidayCalcMethodSet.getWorkTimeCalcMethodOfHoliday().getAdvancedSet();	
				if(advancedSet.isPresent() &&
					advancedSet.get().getNotDeductLateLeaveEarly().isEnableSetPerWorkHour()&&commonSetting.isPresent()) {
					if(advancedSet.isPresent()&&commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime()) {
						decisionGetChild = true;
					}
				}else {
					if(advancedSet.isPresent()&&advancedSet.get().getNotDeductLateLeaveEarly().isDeduct()) {
						decisionGetChild = true;
					}
				}
		}else {
			Optional<PremiumCalcMethodDetailOfHoliday> advanceSet = holidayCalcMethodSet.getPremiumCalcMethodOfHoliday().getAdvanceSet();
			
				if(advanceSet.isPresent()&&advanceSet.get().getNotDeductLateLeaveEarly().isEnableSetPerWorkHour()&&commonSetting.isPresent()) {
					if(commonSetting.get().getLateEarlySet().getCommonSet().isDelFromEmTime()) {
						decisionGetChild = true;
					}
				}else {
					if(advanceSet.isPresent()&&advanceSet.get().getNotDeductLateLeaveEarly().isDeduct()) {
						decisionGetChild = true;
					}
				}
		}
		return decisionGetChild;
	}
	
	
	/**
	 * 休憩未取得用の処理(大塚要件)
	 * 就業時間内時間帯に含まれている休憩時間を計算する
	 * @param restTimeSheet　就業時間帯マスタの休憩時間帯
	 * @return　取得した休憩時間
	 */
	public AttendanceTime getDupRestTime(DeductionTime restTimeSheet) {
		List<TimeSpanForCalc> notDupSpanList = Arrays.asList(new TimeSpanForCalc(restTimeSheet.getStart(), restTimeSheet.getEnd()));
		for(WithinWorkTimeFrame frame : this.getWithinWorkTimeFrame()) {
			notDupSpanList = notDupSpanList.stream()
										   .filter(tc -> tc.getDuplicatedWith(frame.getTimeSheet().getTimeSpan()).isPresent())
										   .map(tc -> tc.getDuplicatedWith(frame.getTimeSheet().getTimeSpan()).get())
										   .collect(Collectors.toList());
		}
		return new AttendanceTime(notDupSpanList.stream().map(ts -> ts.getSpan().lengthAsMinutes()).collect(Collectors.summingInt(tc -> tc)));
	}
	/**
	 * 大塚モード使用時専用の遅刻、早退削除処理
	 */
	public void cleanLateLeaveEarlyTimeForOOtsuka() {
		this.withinWorkTimeFrame.forEach(tc -> tc.cleanLateLeaveEarlyTimeForOOtsuka());
		cleanLateTime();
		cleanLeaveEarly();
	}
	
	private void cleanLateTime() {
		this.lateDecisionClock = Collections.emptyList();
	}
	
	private void cleanLeaveEarly() {
		this.leaveEarlyDecisionClock = Collections.emptyList();
	}

	/**
	 * 大塚所定内割増→残業への変換後に所定内割増リセット
	 */
	public void resetPremiumTimeSheet() {
		this.withinWorkTimeFrame.forEach(tc -> {
			tc.setPremiumTimeSheetInPredetermined(Optional.empty());
		});
	}
}
