package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.overtimehours.clearovertime;

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
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.createremain.subtransfer.SubsTransferProcessMode;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.bonuspay.BonusPayAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.calcategory.CalAttrOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeDivergenceWithCalculationMinusExist;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.erroralarm.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.overtimehours.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.paytime.BonusPayTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workingstyle.flex.SettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.AttendanceItemDictionaryForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.FlexWithinWorkTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManageReGetClass;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.OutsideWorkTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.declare.DeclareCalcRange;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.declare.DeclareTimezoneResult;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.CalculationRangeOfOneDay;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.deductiontime.DeductionAtr;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.outsideworktime.OverTimeFrameTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.outsideworktime.OverTimeFrameTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.outsideworktime.OverTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.timezone.withinworkinghours.WithinWorkTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.declare.DeclareFrameSet;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.week.DailyUnit;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.common.OverTimeOfTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimezoneOfFixedRestTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.ExceededPredAddVacationCalc;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.OverTimeCalcNoBreak;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * ???????????????????????????
 * @author keisuke_hoshina
 */
@Getter
public class OverTimeOfDaily {
	// ??????????????????
	private List<OverTimeFrameTimeSheet> overTimeWorkFrameTimeSheet;
	// ???????????????
	@Setter
	private List<OverTimeFrameTime> overTimeWorkFrameTime;
	// ????????????????????? (?????????????????????)
	@Setter
	private Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime;
	// ??????????????????
	private AttendanceTime overTimeWorkSpentAtWork = new AttendanceTime(0);
	// ?????????????????????
	private AttendanceTime irregularWithinPrescribedOverTimeWork = new AttendanceTime(0);
	// ?????????????????????
	@Setter
	private FlexTime flexTime = new FlexTime(
			TimeDivergenceWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)), new AttendanceTime(0));

	public OverTimeOfDaily(List<OverTimeFrameTimeSheet> frameTimeSheetList, List<OverTimeFrameTime> frameTimeList,
			Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime) {
		this.overTimeWorkFrameTimeSheet = new ArrayList<>(frameTimeSheetList);
		this.overTimeWorkFrameTime = new ArrayList<>(frameTimeList);
		this.excessOverTimeWorkMidNightTime = excessOverTimeWorkMidNightTime;
	}

	public OverTimeOfDaily(List<OverTimeFrameTimeSheet> frameTimeSheetList, List<OverTimeFrameTime> frameTimeList,
			Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTime, AttendanceTime irregularTime,
			FlexTime flexTime, AttendanceTime overTimeWork) {
		this.overTimeWorkFrameTimeSheet = new ArrayList<>(frameTimeSheetList);
		this.overTimeWorkFrameTime = new ArrayList<>(frameTimeList);
		this.excessOverTimeWorkMidNightTime = excessOverTimeWorkMidNightTime;
		this.irregularWithinPrescribedOverTimeWork = irregularTime;
		this.flexTime = flexTime;
		this.overTimeWorkSpentAtWork = overTimeWork;
	}

	public static OverTimeOfDaily createEmpty() {
		return new OverTimeOfDaily(new ArrayList<>(), new ArrayList<>(), Finally.empty());
	}
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @param startTime
	 * @param workNo
	 * @param attendanceTime
	 * @return
	 */
	public static boolean startDicision(TimeWithDayAttr startTime, int workNo, TimeWithDayAttr attendanceTime) {
		if (workNo == 0) {
			return (startTime.v() < attendanceTime.v());
		} else {
			return (startTime.v() >= attendanceTime.v());
		}
	}

	/**
	 * ???????????????????????????????????????????????????
	 * @return ?????????????????????
	 */
	public List<BonusPayTime> calcBonusPay(AutoCalRaisingSalarySetting bonusPayAutoCalcSet, BonusPayAtr bonusPayAtr,
			CalAttrOfDailyAttd calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(OverTimeFrameTimeSheetWork frameTimeSheet : overTimeWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
//		}
		return bonusPayList;
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 * @return ?????????????????????
	 */
	public List<BonusPayTime> calcSpecifiedBonusPay(AutoCalRaisingSalarySetting bonusPayAutoCalcSet,
			BonusPayAtr bonusPayAtr, CalAttrOfDailyAttd calcAtrOfDaily) {
		List<BonusPayTime> bonusPayList = new ArrayList<>();
//		for(OverTimeFrameTimeSheetWork frameTimeSheet : overTimeWorkFrameTimeSheet) {
//			bonusPayList.addAll(frameTimeSheet.calcSpacifiedBonusPay(ActualWorkTimeSheetAtr.OverTimeWork,bonusPayAutoCalcSet, calcAtrOfDaily));
//		}
		return bonusPayList;
	}

	/**
	 * ???????????????????????????????????????????????????
	 * @return ???????????????????????????????????????
	 */
	public ExcessOverTimeWorkMidNightTime calcMidNightTimeIncludeOverTimeWork(AutoCalOvertimeSetting autoCalcSet) {
		int totalTime = 0;
//		for(OverTimeFrameTimeSheetWork frameTime : overTimeWorkFrameTimeSheet) {
//			/*????????????????????????????????????????????????*/
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
		return new ExcessOverTimeWorkMidNightTime(
				TimeDivergenceWithCalculation.sameTime(new AttendanceTime(totalTime)));
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @return ????????????
	 */
	public AttendanceTime calcTotalFrameTime() {
		int sumOverTime = this.overTimeWorkFrameTime.stream()
				.collect(Collectors.summingInt(x -> x.getOverTimeWork().getTime().v()));
		return  new AttendanceTime(sumOverTime);
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @return ??????????????????
	 */
	public AttendanceTime calcTransTotalFrameTime() {
		int sumOverTranferTime = this.overTimeWorkFrameTime.stream()
				.collect(Collectors.summingInt(x -> x.getTransferTime().getTime().v()));
		return  new AttendanceTime(sumOverTranferTime);
	}

	//?????????????????????????????????????????????
	public AttendanceTime calcTotalAppTime() {
		int sumApp = this.overTimeWorkFrameTime.stream()
				.collect(Collectors.summingInt(x -> x.getBeforeApplicationTime().v()));
		return new AttendanceTime(sumApp);
	}
	
	public OverTimeOfDaily createFromJavaType(List<OverTimeFrameTime> frameTimeList,
			ExcessOverTimeWorkMidNightTime midNightTime, AttendanceTime irregularTime, FlexTime flexTime,
			AttendanceTime overTimeWork) {
		return new OverTimeOfDaily(Collections.emptyList(), frameTimeList, Finally.of(midNightTime), irregularTime,
				flexTime, overTimeWork);
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * ????????????????????????????????????????????????
	 * @param recordReGet ??????
	 * @param settingOfFlex ??????????????????????????????
	 * @param flexPreAppTime ????????????
	 * @param beforeApplicationTime ??????????????????
	 * @param declareResult ???????????????????????????
	 * @return ???????????????????????????
	 */
	public static OverTimeOfDaily calculationTime(
			ManageReGetClass recordReGet,
			Optional<SettingOfFlexWork> settingOfFlex,
			AttendanceTime flexPreAppTime,
			AttendanceTime beforeApplicationTime,
			DeclareTimezoneResult declareResult) {
		
		// ????????????
		if (!recordReGet.getWorkType().isPresent()) return OverTimeOfDaily.createEmpty();
		WorkType workType = recordReGet.getWorkType().get();
		// ????????????????????????
		Optional<WorkTimeCode> siftCode = Optional.empty();
		if (recordReGet.getIntegrationOfWorkTime().isPresent()){
			siftCode = Optional.of(recordReGet.getIntegrationOfWorkTime().get().getCode());
		}
		
		val overTimeSheet = recordReGet.getCalculationRangeOfOneDay().getOutsideWorkTimeSheet().get().getOverTimeWorkSheet().get();
		//???????????????????????????
		val overTimeFrameTimeSheet = overTimeSheet.changeOverTimeFrameTimeSheet(
				recordReGet.getPersonDailySetting().getRequire(), workType.getCompanyId(),
				recordReGet.getIntegrationOfDaily().getCalAttr().getOvertimeSetting(), workType,
				siftCode.map(x -> x.v()), recordReGet.getIntegrationOfDaily(), recordReGet.getStatutoryFrameNoList(),
				true, recordReGet.getCompanyCommonSetting().getOvertimeFrameList(),
				recordReGet.getIntegrationOfWorkTime().map(i -> i.getCommonSetting().getGoOutSet()));
		// ?????????????????????
		val overTimeFrame = overTimeSheet.collectOverTimeWorkTime(
				recordReGet.getPersonDailySetting().getRequire(), workType.getCompanyId(),
				recordReGet.getIntegrationOfDaily().getCalAttr().getOvertimeSetting(), workType,
				siftCode.map(x -> x.v()), recordReGet.getIntegrationOfDaily(), recordReGet.getStatutoryFrameNoList(),
				declareResult, true, recordReGet.getCompanyCommonSetting().getOvertimeFrameList(),
				recordReGet.getIntegrationOfWorkTime().map(i -> i.getCommonSetting().getGoOutSet()));
		// ???????????????????????????
		val excessOverTimeWorkMidNightTime = Finally.of(calcExcessMidNightTime(recordReGet, overTimeSheet,
				recordReGet.getIntegrationOfDaily().getCalAttr().getOvertimeSetting(), beforeApplicationTime,
				recordReGet.getIntegrationOfDaily().getCalAttr(), declareResult, settingOfFlex));
		// ????????????????????????????????????
		val irregularTime = overTimeSheet.calcIrregularTime(recordReGet.getIntegrationOfWorkTime().map(i -> i.getCommonSetting().getGoOutSet()));
		// ?????????????????????
		FlexTime flexTime = new FlexTime(
				TimeDivergenceWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)),
				new AttendanceTime(0));
		// ??????????????????????????????
		if (recordReGet.getWorkTimeSetting().isPresent()
				&& recordReGet.getWorkTimeSetting().get().getWorkTimeDivision().getWorkTimeDailyAtr().isFlex()
				&& recordReGet.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet() != null) {

			val changeVariant = ((FlexWithinWorkTimeSheet) recordReGet.getCalculationRangeOfOneDay()
					.getWithinWorkingTimeSheet().get());
			// ??????????????????????????????
			flexTime = changeVariant.createWithinWorkTimeSheetAsFlex(recordReGet.getPersonDailySetting(), 
					recordReGet.getIntegrationOfDaily(), recordReGet.getIntegrationOfWorkTime(),
					recordReGet.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getCalAtr(),
					workType, settingOfFlex.get(), recordReGet.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
					recordReGet.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting(),
					recordReGet.getAddSetting(), recordReGet.getHolidayAddtionSet().get(),
					recordReGet.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getUpLimitORtSet(),
					flexPreAppTime, recordReGet.getDailyUnit(), recordReGet.getWorkTimezoneCommonSet(),
					NotUseAtr.NOT_USE, Optional.of(DeductionAtr.Appropriate));
		}

		val overTimeWork = new AttendanceTime(0);
		return new OverTimeOfDaily(overTimeFrameTimeSheet, overTimeFrame, excessOverTimeWorkMidNightTime, irregularTime,
				flexTime, overTimeWork);
	}

	/**
	 * ??????????????????????????????
	 * ???????????????????????????????????????????????????????????????????????????
	 * @param recordReGet ??????
	 * @param overTimeSheet ???????????????
	 * @param autoCalcSet ?????????????????????????????????
	 * @param beforeApplicationTime ??????????????????
	 * @param calAttr ???????????????????????????
	 * @param declareResult ???????????????????????????
	 * @param flexCalcMethod ??????????????????????????????
	 * @return ???????????????????????????
	 */
	private static ExcessOverTimeWorkMidNightTime calcExcessMidNightTime(
			ManageReGetClass recordReGet,
			OverTimeSheet overTimeSheet,
			AutoCalOvertimeSetting autoCalcSet,
			AttendanceTime beforeApplicationTime,
			CalAttrOfDailyAttd calAttr,
			DeclareTimezoneResult declareResult,
			Optional<SettingOfFlexWork> flexCalcMethod) {

		AttendanceTime flexWithoutTime = new AttendanceTime(0);
		
		// ??????????????????
		WorkingConditionItem conditionItem = recordReGet.getPersonDailySetting().getPersonInfo();
		// ?????????????????????
		if (!recordReGet.getWorkType().isPresent()){
			return new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation.defaultValue());
		}
		WorkType workType = recordReGet.getWorkType().get();
		// ?????????????????????
		if (recordReGet.getWorkTimeSetting().isPresent()) {
			if (recordReGet.getWorkTimeSetting().get().getWorkTimeDivision().isFlexWorkDay(conditionItem)) {
				// ??????????????????????????????
				Optional<WithinWorkTimeSheet> withinWorkTimeSheetOpt = Optional.empty();
				if (recordReGet.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().isPresent()) {
					withinWorkTimeSheetOpt = Optional
							.of(recordReGet.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get());
				}
				if (withinWorkTimeSheetOpt.isPresent()){
					flexWithoutTime = ((FlexWithinWorkTimeSheet)withinWorkTimeSheetOpt.get()).calcWithoutMidnightTime(
							recordReGet.getPersonDailySetting(),
							recordReGet.getIntegrationOfDaily(),
							recordReGet.getIntegrationOfWorkTime(),
							recordReGet.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getCalAtr(),
							workType,
							flexCalcMethod.get(),
							recordReGet.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
							recordReGet.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting(),
							recordReGet.getAddSetting(),
							recordReGet.getHolidayAddtionSet().get(),
							recordReGet.getDailyUnit(),
							recordReGet.getWorkTimezoneCommonSet(),
							recordReGet.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getUpLimitORtSet(),
							NotUseAtr.NOT_USE);
				}
			}
		}
		// ???????????????????????????
		TimeDivergenceWithCalculation midnightTime = overTimeSheet.calcMidNightTime(autoCalcSet);
		// ????????????????????????????????????????????????????????????????????????.????????????????????????="????????????????????????"????????????????????????????????????
		if (autoCalcSet.getNormalMidOtTime().getCalAtr().isCalculateEmbossing()){
			midnightTime = midnightTime.addMinutes(flexWithoutTime, flexWithoutTime);
		}
		else{
			midnightTime = midnightTime.addMinutes(AttendanceTime.ZERO, flexWithoutTime);
		}
		// ??????????????????
		if (calAttr.getOvertimeSetting().getNormalMidOtTime()
				.getUpLimitORtSet() == TimeLimitUpperLimitSetting.LIMITNUMBERAPPLICATION
				&& midnightTime.getTime().greaterThanOrEqualTo(beforeApplicationTime.valueAsMinutes())) {
			return new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation
					.createTimeWithCalculation(beforeApplicationTime, midnightTime.getCalcTime()));
		}
		if (declareResult.getCalcRangeOfOneDay().isPresent()) {
			// ?????????????????????????????????
			AttendanceTime declareTime = OverTimeOfDaily.calcDeclareOvertimeMidnightTime(overTimeSheet, autoCalcSet,
					declareResult);
			if (declareTime.valueAsMinutes() > 0) {
				midnightTime.replaceTimeWithCalc(declareTime);
				// ??????????????????????????? ??? true
				if (declareResult.getDeclareCalcRange().isPresent()) {
					declareResult.getDeclareCalcRange().get().getEditState().setOvertimeMn(true);
				}
			}
		}
		return new ExcessOverTimeWorkMidNightTime(midnightTime);
	}

	/**
	 * ?????????????????????????????????
	 * 
	 * @param overTimeSheet ???????????????
	 * @param autoCalcSet   ?????????????????????????????????
	 * @param declareResult ???????????????????????????
	 * @return ????????????????????????
	 */
	private static AttendanceTime calcDeclareOvertimeMidnightTime(OverTimeSheet overTimeSheet,
			AutoCalOvertimeSetting autoCalcSet, DeclareTimezoneResult declareResult) {

		AttendanceTime result = new AttendanceTime(0);
		if (!declareResult.getCalcRangeOfOneDay().isPresent())
			return result;
		CalculationRangeOfOneDay declareCalcRange = declareResult.getCalcRangeOfOneDay().get();
		if (!declareResult.getDeclareCalcRange().isPresent())
			return result;
		DeclareCalcRange calcRange = declareResult.getDeclareCalcRange().get();
		// ????????????????????????
		if (calcRange.getDeclareSet().getFrameSet() == DeclareFrameSet.WORKTIME_SET) {
			// ??????????????????????????????????????????????????????
			OutsideWorkTimeSheet declareOutsideWork = declareCalcRange.getOutsideWorkTimeSheet().get();
			if (declareOutsideWork.getOverTimeWorkSheet().isPresent()) {
				OverTimeSheet declareSheet = declareOutsideWork.getOverTimeWorkSheet().get();
				result = declareSheet.calcMidNightTime(autoCalcSet).getCalcTime();
			}
			// ?????????????????????????????????
			return result;
		}
		// ?????????????????????????????????
		{
			// ?????????????????????
			if (calcRange.getWorkTypeOpt().isPresent()) {
				// ???????????????????????????
				if (!calcRange.getWorkTypeOpt().get().isHolidayWork()) {
					// ???????????????????????????????????????????????????
					result = new AttendanceTime(calcRange.getCalcTime().getEarlyOvertimeMn().valueAsMinutes()
							+ calcRange.getCalcTime().getOvertimeMn().valueAsMinutes());
				}
			}
		}
		// ?????????????????????????????????
		return result;
	}

	/**
	 * ?????????????????????????????????&??????????????????
	 * 
	 * @param attendanceItemDictionary
	 */
	public List<EmployeeDailyPerError> checkOverTimeExcess(String employeeId, GeneralDate targetDate,
			AttendanceItemDictionaryForCalc attendanceItemDictionary, ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		for (OverTimeFrameTime frameTime : this.getOverTimeWorkFrameTime()) {
			if (frameTime.isOverLimitDivergenceTime()) {
				// ????????????
				attendanceItemDictionary.findId("????????????" + frameTime.getOverWorkFrameNo().v()).ifPresent(
						itemId -> returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(),
								employeeId, targetDate, errorCode, itemId)));
//				//????????????
//				attendanceItemDictionary.findId("??????????????????"+frameTime.getOverWorkFrameNo().v()).ifPresent( itemId -> 
//						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
//				);
			}
		}
		return returnErrorList;
	}

	/**
	 * ???????????????????????????????????????&??????????????????
	 */
	public List<EmployeeDailyPerError> checkPreOverTimeExcess(String employeeId, GeneralDate targetDate,
			AttendanceItemDictionaryForCalc attendanceItemDictionary, ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		for (OverTimeFrameTime frameTime : this.getOverTimeWorkFrameTime()) {
			if (frameTime.isPreOverLimitDivergenceTime()) {
				// ????????????
				attendanceItemDictionary.findId("????????????" + frameTime.getOverWorkFrameNo().v()).ifPresent(
						itemId -> returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(),
								employeeId, targetDate, errorCode, itemId)));
//				//????????????
//				attendanceItemDictionary.findId("??????????????????"+frameTime.getOverWorkFrameNo().v()).ifPresent( itemId -> 
//						returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId))
//				);
			}
		}
		return returnErrorList;
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public List<EmployeeDailyPerError> checkFlexTimeExcess(String employeeId, GeneralDate targetDate, String searchWord,
			AttendanceItemDictionaryForCalc attendanceItemDictionary, ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if (this.getFlexTime().isOverLimitDivergenceTime()) {
			val itemId = attendanceItemDictionary.findId(searchWord);
			if (itemId.isPresent())
				returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate,
						errorCode, itemId.get()));
		}
		return returnErrorList;
	}

	/**
	 * ????????????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public List<EmployeeDailyPerError> checkPreFlexTimeExcess(String employeeId, GeneralDate targetDate,
			String searchWord, AttendanceItemDictionaryForCalc attendanceItemDictionary,
			ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if (this.getFlexTime().isPreOverLimitDivergenceTime()) {
			val itemId = attendanceItemDictionary.findId(searchWord);
			if (itemId.isPresent())
				returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate,
						errorCode, itemId.get()));
		}
		return returnErrorList;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 * 
	 * @return
	 */
	public List<EmployeeDailyPerError> checkNightTimeExcess(String employeeId, GeneralDate targetDate,
			String searchWord, AttendanceItemDictionaryForCalc attendanceItemDictionary,
			ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if (this.getExcessOverTimeWorkMidNightTime().isPresent()
				&& this.getExcessOverTimeWorkMidNightTime().get().isOverLimitDivergenceTime()) {
			val itemId = attendanceItemDictionary.findId(searchWord);
			if (itemId.isPresent())
				returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate,
						errorCode, itemId.get()));
		}
		return returnErrorList;
	}

	// ?????????????????????
	/**
	 * 
	 * @param actualWorkTime             ??????????????????
	 * @param dailyUnit
	 * @param predetermineTimeSetForCalc
	 * @param finally1
	 * @param optional
	 * @param unUseBreakTime             ?????????????????????
	 * @param predetermineTime           1??????????????????
	 * @param ootsukaFixedCalcSet        ????????????????????????
	 */
	public void calcOotsukaOverTime(AttendanceTime actualWorkTime, AttendanceTime unUseBreakTime,
			AttendanceTime annualAddTime, AttendanceTime predTime, Optional<FixedWorkCalcSetting> ootsukaFixedCalcSet,
			AutoCalOvertimeSetting autoCalcSet, DailyUnit dailyUnit, Optional<TimezoneOfFixedRestTimeSet> restTimeSheet,
			Finally<WithinWorkTimeSheet> withinWorkTimeSheet, PredetermineTimeSetForCalc predetermineTimeSetForCalc) {
		if (ootsukaFixedCalcSet != null && ootsukaFixedCalcSet.isPresent()) {
			// ?????????????????????????????????????????????
			calcOverTimeFromUnuseTime(actualWorkTime, unUseBreakTime,
					ootsukaFixedCalcSet.get().getOverTimeCalcNoBreak(), dailyUnit, predTime);
			// ????????????????????????????????????????????????
			calcOverTimeFromOverPredTime(actualWorkTime, unUseBreakTime, annualAddTime,
					predetermineTimeSetForCalc.getAdditionSet().getPredTime().getOneDay(),
					ootsukaFixedCalcSet.get().getExceededPredAddVacationCalc(), autoCalcSet, restTimeSheet,
					withinWorkTimeSheet);
		}
	}

	/**
	 * ?????????????????????????????????????????????
	 * 
	 * @param predTime
	 * @param dailyUnit
	 */
	private void calcOverTimeFromUnuseTime(AttendanceTime actualWorkTime, AttendanceTime unUseBreakTime,
			OverTimeCalcNoBreak ootsukaFixedCalcSet, DailyUnit dailyUnit, AttendanceTime predTime) {
		// ????????????????????????????????????
		if (ootsukaFixedCalcSet == null || ootsukaFixedCalcSet.getCalcMethod() == null
				|| ootsukaFixedCalcSet.getCalcMethod().isCalcAsWorking() || ootsukaFixedCalcSet.getInLawOT() == null
				|| ootsukaFixedCalcSet.getNotInLawOT() == null)
			return;
		// ???????????????????????????
		val statutoryTime = dailyUnit.getDailyTime();
		// ????????????????????????????????????No?????????
		val frameNoList = this.overTimeWorkFrameTime.stream().map(tc -> tc.getOverWorkFrameNo())
				.collect(Collectors.toList());
		// ????????????<=????????????(?????????)
		if (actualWorkTime.lessThanOrEqualTo(statutoryTime.v())) {
			if (unUseBreakTime.greaterThan(0)) {
				if (frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()))) {
					this.overTimeWorkFrameTime.forEach(tc -> {
						if (tc.getOverWorkFrameNo().v().equals(ootsukaFixedCalcSet.getInLawOT().v()))
							tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(
									tc.getOverTimeWork().getTime().addMinutes(unUseBreakTime.valueAsMinutes()),
									tc.getOverTimeWork().getCalcTime()));
					});
				} else {
					this.overTimeWorkFrameTime
							.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()),
									TimeDivergenceWithCalculation.createTimeWithCalculation(unUseBreakTime,
											unUseBreakTime),
									TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
									new AttendanceTime(0), new AttendanceTime(0)));
				}
			}
		}
		// ????????????>????????????(?????????)
		else {
			// ??????
			final int calcUnbreakTime = unUseBreakTime
					.minusMinutes(actualWorkTime.valueAsMinutes() - statutoryTime.valueAsMinutes()).valueAsMinutes();
			if (frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()))) {
				this.overTimeWorkFrameTime.forEach(tc -> {
					if (tc.getOverWorkFrameNo().v().equals(ootsukaFixedCalcSet.getInLawOT().v()))
						tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(
								tc.getOverTimeWork().getTime().addMinutes(calcUnbreakTime),
								tc.getOverTimeWork().getCalcTime()));
				});
			} else {
				this.overTimeWorkFrameTime
						.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()),
								TimeDivergenceWithCalculation.sameTime(new AttendanceTime(calcUnbreakTime)),
								TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), new AttendanceTime(0),
								new AttendanceTime(0)));
			}
			frameNoList.add(new OverTimeFrameNo(ootsukaFixedCalcSet.getInLawOT().v()));
			// ??????
			final AttendanceTime excessOverTime = actualWorkTime.minusMinutes(statutoryTime.valueAsMinutes())
					.valueAsMinutes() > unUseBreakTime.v() ? unUseBreakTime
							: actualWorkTime.minusMinutes(statutoryTime.valueAsMinutes());
			if (frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getNotInLawOT().v()))) {
				this.overTimeWorkFrameTime.forEach(tc -> {
					if (tc.getOverWorkFrameNo().v().equals(ootsukaFixedCalcSet.getNotInLawOT().v()))
						tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(
								tc.getOverTimeWork().getTime().addMinutes(excessOverTime.valueAsMinutes()),
								tc.getOverTimeWork().getCalcTime()));
				});
			} else {
				this.overTimeWorkFrameTime.add(new OverTimeFrameTime(
						new OverTimeFrameNo(ootsukaFixedCalcSet.getNotInLawOT().v()),
						TimeDivergenceWithCalculation.sameTime(
								unUseBreakTime.minusMinutes(excessOverTime.valueAsMinutes()).valueAsMinutes() < 0
										? new AttendanceTime(0)
										: excessOverTime),
						TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), new AttendanceTime(0),
						new AttendanceTime(0)));
			}
		}
	}

	/**
	 * ????????????????????????????????????????????????
	 * 
	 * @param restTimeSheet
	 * @param withinWorkTimeSheet
	 * @param actualWorkTime      ????????????
	 * @param unUseBreakTime      ?????????????????????
	 * @param annualAddTime       ??????????????????
	 * @param oneDayPredTime      1??????????????????
	 * @param ootsukaFixedCalcSet ????????????????????????
	 * @param autoCalcSet         ?????????????????????????????????
	 */
	private void calcOverTimeFromOverPredTime(AttendanceTime actualWorkTime, AttendanceTime unUseBreakTime,
			AttendanceTime annualAddTime, AttendanceTime oneDayPredTime,
			ExceededPredAddVacationCalc ootsukaFixedCalcSet, AutoCalOvertimeSetting autoCalcSet,
			Optional<TimezoneOfFixedRestTimeSet> restTimeSheet, Finally<WithinWorkTimeSheet> withinWorkTimeSheet) {
		// AttendanceTime breakTimeInWithinTimeSheet =
		// getBreakTimeInWithin(withinWorkTimeSheet,restTimeSheet);

		final AttendanceTime totalWorkTime = new AttendanceTime(
				actualWorkTime.valueAsMinutes() + annualAddTime.valueAsMinutes() - unUseBreakTime.valueAsMinutes());

		final AttendanceTime withinOverTime = totalWorkTime.greaterThan(oneDayPredTime.valueAsMinutes())
				? totalWorkTime.minusMinutes(oneDayPredTime.valueAsMinutes())
				: new AttendanceTime(0);

		// ????????????????????????????????????
		if (ootsukaFixedCalcSet == null || ootsukaFixedCalcSet.getCalcMethod() == null
				|| ootsukaFixedCalcSet.getCalcMethod().isCalcAsWorking() || ootsukaFixedCalcSet.getOtFrameNo() == null)
			return;

		val frameNoList = this.overTimeWorkFrameTime.stream().map(tc -> tc.getOverWorkFrameNo())
				.collect(Collectors.toList());

		// ???????????????????????????????????????
		// ????????????????????????
		if (autoCalcSet.decisionCalcAtr(StatutoryAtr.Statutory, false)) {
			if (withinOverTime.greaterThan(0)) {
				if (frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()))) {
					this.overTimeWorkFrameTime.forEach(tc -> {
						if (tc.getOverWorkFrameNo().equals(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v())))
							tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(
									tc.getOverTimeWork().getTime().addMinutes(withinOverTime.valueAsMinutes()),
									tc.getOverTimeWork().getCalcTime().addMinutes(withinOverTime.valueAsMinutes())));
					});
				} else {
					this.overTimeWorkFrameTime
							.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()),
									TimeDivergenceWithCalculation.sameTime(withinOverTime),
									TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
									new AttendanceTime(0), new AttendanceTime(0)));
				}
			}
		}
		// ??????????????????
		else {
			if (frameNoList.contains(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()))) {
				this.overTimeWorkFrameTime.forEach(tc -> {
					if (tc.getOverWorkFrameNo().equals(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v())))
						tc.setOverTimeWork(TimeDivergenceWithCalculation.createTimeWithCalculation(
								tc.getOverTimeWork().getTime(),
								tc.getOverTimeWork().getCalcTime().addMinutes(withinOverTime.valueAsMinutes())));
				});
			} else {
				this.overTimeWorkFrameTime
						.add(new OverTimeFrameTime(new OverTimeFrameNo(ootsukaFixedCalcSet.getOtFrameNo().v()),
								TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),
										withinOverTime),
								TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), new AttendanceTime(0),
								new AttendanceTime(0)));
			}

		}

	}

	/**
	 * ?????????????????????????????????????????????????????????
	 * 
	 * @param withinWorkTimeSheet ???????????????
	 * @param restTimeSheetSet    ??????????????????????????????????????????
	 * @return ????????????????????????
	 */
	@SuppressWarnings("unused")
	private AttendanceTime getBreakTimeInWithin(Finally<WithinWorkTimeSheet> withinWorkTimeSheet,
			Optional<TimezoneOfFixedRestTimeSet> restTimeSheetSet) {
		if (!restTimeSheetSet.isPresent() || withinWorkTimeSheet == null || !withinWorkTimeSheet.isPresent())
			return new AttendanceTime(0);
		AttendanceTime restTime = new AttendanceTime(0);
		for (DeductionTime restTimeSheet : restTimeSheetSet.get().getTimezones()) {
			restTime = restTime.addMinutes(withinWorkTimeSheet.get().getDupRestTime(restTimeSheet).valueAsMinutes());
		}
		return restTime;
	}

	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	public OverTimeOfDaily calcDiverGenceTime() {
		List<OverTimeFrameTime> OverTimeFrameList = new ArrayList<>();
		for (OverTimeFrameTime overTimeFrameTime : this.overTimeWorkFrameTime) {
			overTimeFrameTime.calcDiverGenceTime();
			OverTimeFrameList.add(overTimeFrameTime);
		}
		FlexTime flexTime = this.flexTime != null ? this.flexTime.calcDiverGenceTime() : this.flexTime;
		Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeMidNight = this.excessOverTimeWorkMidNightTime.isPresent()
				? Finally.of(this.excessOverTimeWorkMidNightTime.get().calcDiverGenceTime())
				: this.excessOverTimeWorkMidNightTime;
		return new OverTimeOfDaily(this.overTimeWorkFrameTimeSheet, OverTimeFrameList, excessOverTimeMidNight,
				this.irregularWithinPrescribedOverTimeWork, flexTime, this.overTimeWorkSpentAtWork);
	}

	/**
	 * ??????????????????????????????0?????????
	 * 
	 * @param overtimeFrameTimeList ????????????????????????
	 */
	public static void divergenceMinusValueToZero(List<OverTimeFrameTime> overtimeFrameTimeList) {

		// ????????????????????????
		if (AppContexts.optionLicense().customize().ootsuka() == false)
			return;

		// ??????????????????????????????0?????????
		for (val overtimeFrameTime : overtimeFrameTimeList) {
			overtimeFrameTime.getOverTimeWork().divergenceMinusValueToZero();
			overtimeFrameTime.getTransferTime().divergenceMinusValueToZero();
		}
	}

	// PC??????????????????????????????????????????????????????????????????(?????????????????????)
	public void setPCLogOnValue(Map<OverTimeFrameNo, OverTimeFrameTime> map) {
		Map<OverTimeFrameNo, OverTimeFrameTime> changeList = convertOverMap(this.getOverTimeWorkFrameTime());

		for (int frameNo = 1; frameNo <= 10; frameNo++) {

			// ?????????
			if (changeList.containsKey(new OverTimeFrameNo(frameNo))) {
				val getframe = changeList.get(new OverTimeFrameNo(frameNo));
				if (map.containsKey(new OverTimeFrameNo(frameNo))) {
					// ???????????????????????????
					if (getframe.getOverTimeWork() != null
							&& map.get(new OverTimeFrameNo(frameNo)).getOverTimeWork() != null) {
						getframe.getOverTimeWork().replaceTimeAndCalcDiv(
								map.get(new OverTimeFrameNo(frameNo)).getOverTimeWork().getCalcTime());
					} else {
						getframe.setOverTimeWork(TimeDivergenceWithCalculation
								.createTimeWithCalculation(new AttendanceTime(0), new AttendanceTime(0)));
					}
					// ???????????????????????????
					if (getframe.getTransferTime() != null
							&& map.get(new OverTimeFrameNo(frameNo)).getTransferTime() != null) {
						getframe.getTransferTime().replaceTimeAndCalcDiv(
								map.get(new OverTimeFrameNo(frameNo)).getTransferTime().getCalcTime());
					} else {
						getframe.setTransferTime(TimeDivergenceWithCalculation
								.createTimeWithCalculation(new AttendanceTime(0), new AttendanceTime(0)));
					}
				} else {
					// ???????????????????????????
					getframe.getOverTimeWork().replaceTimeAndCalcDiv(new AttendanceTime(0));
					// ???????????????????????????
					getframe.getTransferTime().replaceTimeAndCalcDiv(new AttendanceTime(0));
				}
				changeList.remove(new OverTimeFrameNo(frameNo));
				changeList.put(new OverTimeFrameNo(frameNo), getframe);
			}
			// ??????????????????
			else {
				if (map.containsKey(new OverTimeFrameNo(frameNo))) {
					changeList.put(new OverTimeFrameNo(frameNo),
							new OverTimeFrameTime(new OverTimeFrameNo(frameNo),
									TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),
											map.get(new OverTimeFrameNo(frameNo)).getOverTimeWork().getCalcTime()),
									TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0),
											map.get(new OverTimeFrameNo(frameNo)).getTransferTime().getCalcTime()),
									new AttendanceTime(0), new AttendanceTime(0)));
				}
			}
		}
		this.overTimeWorkFrameTime = new ArrayList<>(changeList.values());
	}

	private Map<OverTimeFrameNo, OverTimeFrameTime> convertOverMap(List<OverTimeFrameTime> overTimeWorkFrameTime) {
		Map<OverTimeFrameNo, OverTimeFrameTime> map = new HashMap<>();
		for (OverTimeFrameTime ot : overTimeWorkFrameTime) {
			map.put(ot.getOverWorkFrameNo(), ot);
		}
		return map;
	}

	public void transWithinOverTimeForOOtsukaSpecialHoliday(List<OverTimeOfTimeZoneSet> overTimeSheetByWorkTimeMaster,
			AttendanceTime withinOverTime) {
		AttendanceTime copyWithinOverTime = withinOverTime;
		List<OverTimeOfTimeZoneSet> sortedOverTimeZoneSet = overTimeSheetByWorkTimeMaster;
		if (overTimeSheetByWorkTimeMaster.size() > 0) {
			boolean nextLoopFlag = true;
			while (nextLoopFlag) {
				for (int index = 0; index <= sortedOverTimeZoneSet.size(); index++) {
					if (index == sortedOverTimeZoneSet.size() - 1) {
						nextLoopFlag = false;
						break;
					}
					// ?????????????????????
					if (sortedOverTimeZoneSet.get(index).getSettlementOrder()
							.greaterThan(sortedOverTimeZoneSet.get(index + 1).getSettlementOrder())) {
						OverTimeOfTimeZoneSet pary = sortedOverTimeZoneSet.get(index);
						sortedOverTimeZoneSet.set(index, sortedOverTimeZoneSet.get(index + 1));
						sortedOverTimeZoneSet.set(index + 1, pary);
						break;
					}
					// ????????????(????????????????????????????????????)
					else if (sortedOverTimeZoneSet.get(index).getSettlementOrder()
							.equals(sortedOverTimeZoneSet.get(index + 1).getSettlementOrder())) {
						if (sortedOverTimeZoneSet.get(index).getTimezone().getStart()
								.greaterThan(sortedOverTimeZoneSet.get(index + 1).getTimezone().getStart())) {
							OverTimeOfTimeZoneSet pary = sortedOverTimeZoneSet.get(index);
							sortedOverTimeZoneSet.set(index, sortedOverTimeZoneSet.get(index + 1));
							sortedOverTimeZoneSet.set(index + 1, pary);
							break;
						}
					}

				}
			}
		}

		for (OverTimeOfTimeZoneSet set : sortedOverTimeZoneSet) {
			// ???????????????????????????????????????????????????
			Optional<AttendanceTime> transAndOverTime = overTimeWorkFrameTime.stream()
					.filter(tc -> tc.getOverWorkFrameNo().compareTo(set.getOtFrameNo().v()) == 0).map(ts -> ts
							.getOverTimeWork().getTime().addMinutes(ts.getTransferTime().getTime().valueAsMinutes()))
					.findFirst();
			AttendanceTime transTime = new AttendanceTime(0);
			if (transAndOverTime.isPresent() && copyWithinOverTime.greaterThan(transAndOverTime.get())) {
				transTime = transAndOverTime.get();
			} else {
				transTime = copyWithinOverTime;
			}

			final int toTime = transTime.valueAsMinutes();
			// ??????
			overTimeWorkFrameTime.forEach(tc -> {
				if (tc.getOverWorkFrameNo().compareTo(set.getOtFrameNo().v()) == 0) {
					tc.minusTimeResultGreaterEqualZero(new AttendanceTime(toTime));
				}
			});
			// ??????
			Optional<OverTimeFrameNo> forcsWithin = overTimeWorkFrameTime.stream()
					.filter(tc -> tc.getOverWorkFrameNo().compareTo(set.getLegalOTframeNo().v()) == 0)
					.map(ts -> ts.getOverWorkFrameNo()).findFirst();
			// ??????????????????
			if (forcsWithin.isPresent()) {
				overTimeWorkFrameTime.forEach(ts -> {
					if (ts.getOverWorkFrameNo().compareTo(forcsWithin.get()) == 0) {
						ts.add(new AttendanceTime(toTime));
					}
				});
			}
			// ??????????????????
			else {
				this.overTimeWorkFrameTime.add(new OverTimeFrameTime(new OverTimeFrameNo(set.getLegalOTframeNo().v()),
						TimeDivergenceWithCalculation.sameTime(new AttendanceTime(toTime)),
						TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), new AttendanceTime(0),
						new AttendanceTime(0)));
			}

			copyWithinOverTime = copyWithinOverTime.minusMinutes(toTime);

			if (copyWithinOverTime.lessThan(0))
				break;
		}

	}

	public void mergeOverTimeList(List<OverTimeFrameTime> frameTimeList) {
		for (OverTimeFrameTime frameTime : frameTimeList) {
			if (this.overTimeWorkFrameTime.stream()
					.filter(tc -> tc.getOverWorkFrameNo().equals(frameTime.getOverWorkFrameNo())).findFirst()
					.isPresent()) {
				this.overTimeWorkFrameTime.stream()
						.filter(tc -> tc.getOverWorkFrameNo().equals(frameTime.getOverWorkFrameNo())).findFirst()
						.ifPresent(ts -> {
							ts = ts.addOverTime(frameTime.getOverTimeWork().getTime(),
									frameTime.getOverTimeWork().getCalcTime());
							ts = ts.addTransoverTime(frameTime.getTransferTime().getTime(),
									frameTime.getTransferTime().getCalcTime());
						});
			} else {
				this.overTimeWorkFrameTime.add(frameTime);
			}
		}
	}

	// ???????????????????????????????????????????????????
	public static OverTimeOfDaily createDefaultBeforeApp(List<Integer> lstNo) {
		List<OverTimeFrameTime> workFrameTime = lstNo.stream().map(x -> {
			return new OverTimeFrameTime(new OverTimeFrameNo(x), TimeDivergenceWithCalculation.emptyTime(),
					TimeDivergenceWithCalculation.emptyTime(), new AttendanceTime(0), new AttendanceTime(0));
		}).collect(Collectors.toList());
		return new OverTimeOfDaily(new ArrayList<>(), workFrameTime, Finally.empty());
	}

	@Override
	public OverTimeOfDaily clone() {
		// ??????????????????
		List<OverTimeFrameTimeSheet> overTimeWorkFrameTimeSheetClone = this.overTimeWorkFrameTimeSheet.stream()
				.map(x -> x.clone()).collect(Collectors.toList());
		// ???????????????
		List<OverTimeFrameTime> overTimeWorkFrameTimeClone = this.overTimeWorkFrameTime.stream().map(x -> x.clone())
				.collect(Collectors.toList());
		// ????????????????????? (?????????????????????)
		Finally<ExcessOverTimeWorkMidNightTime> excessOverTimeWorkMidNightTimeClone = excessOverTimeWorkMidNightTime
				.isPresent() ? Finally.of(excessOverTimeWorkMidNightTime.get().clone()) : Finally.empty();
		// ??????????????????
		AttendanceTime overTimeWorkClone = new AttendanceTime(overTimeWorkSpentAtWork.v());
		// ?????????????????????
		AttendanceTime irregularTimeClone = new AttendanceTime(irregularWithinPrescribedOverTimeWork.v());
		// ?????????????????????
		FlexTime flexTimeClone = flexTime.clone();

		return new OverTimeOfDaily(overTimeWorkFrameTimeSheetClone, overTimeWorkFrameTimeClone,
				excessOverTimeWorkMidNightTimeClone, irregularTimeClone, flexTimeClone, overTimeWorkClone);
	}

	//????????????????????????????????????????????????????????????
	public boolean tranferOvertimeCompenCall(SubsTransferProcessMode processMode) {
		AttendanceTime sumOverTime = calcTotalFrameTime();
		AttendanceTime sumOverTranferTime = calcTransTotalFrameTime();
		AttendanceTime sumApp = calcTotalAppTime();
		if ((sumOverTime.valueAsMinutes() + sumOverTranferTime.valueAsMinutes()) <= 0
				&& processMode == SubsTransferProcessMode.DAILY && sumApp.valueAsMinutes() > 0) {
			return true;
		}
		return false;
	}
}
