package nts.uk.ctx.at.record.dom.actualworkinghours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.medical.MedicalCareTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workingtime.StayingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workschedule.WorkScheduleTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workschedule.WorkScheduleTimeOfDaily;
import nts.uk.ctx.at.record.dom.adapter.personnelcostsetting.PersonnelCostSettingImport;
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CheckExcessAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManagePerCompanySet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManageReGetClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.ctx.at.record.dom.workrule.specific.CalculateOfTotalConstraintTime;
import nts.uk.ctx.at.record.dom.workrule.specific.UpperLimitTotalWorkingHour;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.calculation.holiday.kmk013_splitdomain.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktime.common.GoLeavingWorkAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkTimeNightShift;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 
 * @author nampt
 * 日別実績の勤怠時間 - root
 *
 */
@Getter
public class AttendanceTimeOfDailyPerformance extends AggregateRoot {

	//社員ID
	private String employeeId;
	
	//年月日
	private GeneralDate ymd;
	
	//勤務予定時間 - 日別実績の勤務予定時間
	private WorkScheduleTimeOfDaily workScheduleTimeOfDaily;
	
	//実働時間/実績時間  - 日別実績の勤務実績時間
	private ActualWorkingTimeOfDaily actualWorkingTimeOfDaily;
	
	//滞在時間 - 日別実績の滞在時間 change tyle
	private StayingTimeOfDaily stayingTime;
	
	//不就労時間 - 勤怠時間
	private AttendanceTimeOfExistMinus unEmployedTime;
	
	//予実差異時間 - 勤怠時間
	private AttendanceTimeOfExistMinus budgetTimeVariance;
	
	//医療時間 - 日別実績の医療時間
	private MedicalCareTimeOfDaily medicalCareTime;
	
	
	public AttendanceTimeOfDailyPerformance (String employeeId,
											 GeneralDate ymd,
											 WorkScheduleTimeOfDaily schedule,
											 ActualWorkingTimeOfDaily actual,
											 StayingTimeOfDaily stay,
											 AttendanceTimeOfExistMinus budget,
											 AttendanceTimeOfExistMinus unEmploy) {
		this.employeeId = employeeId;
		this.ymd = ymd;
		this.workScheduleTimeOfDaily = schedule;
		this.actualWorkingTimeOfDaily = actual;
		this.stayingTime = stay;
		this.budgetTimeVariance = budget;
		this.unEmployedTime = unEmploy;
	}
	
	public AttendanceTimeOfDailyPerformance(String employeeId, GeneralDate ymd,
			WorkScheduleTimeOfDaily workScheduleTimeOfDaily, ActualWorkingTimeOfDaily actualWorkingTimeOfDaily,
			StayingTimeOfDaily stayingTime, AttendanceTimeOfExistMinus unEmployedTime, AttendanceTimeOfExistMinus budgetTimeVariance,
			MedicalCareTimeOfDaily medicalCareTime) {
		super();
		this.employeeId = employeeId;
		this.ymd = ymd;
		this.workScheduleTimeOfDaily = workScheduleTimeOfDaily;
		this.actualWorkingTimeOfDaily = actualWorkingTimeOfDaily;
		this.stayingTime = stayingTime;
		this.unEmployedTime = unEmployedTime;
		this.budgetTimeVariance = budgetTimeVariance;
		this.medicalCareTime = medicalCareTime;
	}
	
	
	public AttendanceTimeOfDailyPerformance inssertActualWorkingTimeOfDaily(ActualWorkingTimeOfDaily time) {
		return new AttendanceTimeOfDailyPerformance(this.employeeId, this.ymd, this.workScheduleTimeOfDaily, time, this.stayingTime, this.budgetTimeVariance, this.unEmployedTime); 
	}
	
	/**
	 * 時間・回数・乖離系(計算で求める全ての値)が全て０
	 * @return
	 */
	public static AttendanceTimeOfDailyPerformance allZeroValue(String empId, GeneralDate ymd) {
		return new AttendanceTimeOfDailyPerformance(empId, 
													ymd, 
													WorkScheduleTimeOfDaily.defaultValue(), 
													ActualWorkingTimeOfDaily.defaultValue(), 
													StayingTimeOfDaily.defaultValue(), 
													new AttendanceTimeOfExistMinus(0), 
													new AttendanceTimeOfExistMinus(0), 
													MedicalCareTimeOfDaily.defaultValue());
	}
	
	/**
	 * 日別実績の勤怠時間の計算
	 * @param oneDay 1日の範囲クラス
	 * @param schePreTimeSet 
	 * @param ootsukaFixCalsSet 
	 * @param workTimeDailyAtr2 
	 * @param scheduleReGetClass 
	 * @param schePred 
	 * @param converter 
	 * @param companyCommonSetting 
	 * @param personalSetting 
	 * @param recordWorkTimeCode 
	 * @param integrationOfDaily2 
	 * @return 日別実績(Work)クラス
	 */
	public static IntegrationOfDaily calcTimeResult(
			VacationClass vacation, WorkType workType,
			Optional<SettingOfFlexWork> flexCalcMethod, BonusPayAutoCalcSet bonusPayAutoCalcSet,
			List<CompensatoryOccurrenceSetting> eachCompanyTimeSet,
			List<DivergenceTime> divergenceTimeList,
			CalculateOfTotalConstraintTime calculateOfTotalConstraintTime, ManageReGetClass scheduleReGetClass,
			ManageReGetClass recordReGetClass,WorkingConditionItem conditionItem,
			Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,DeductLeaveEarly leaveLateSet,DeductLeaveEarly scheleaveLateSet, Optional<PredetermineTimeSetForCalc> schePred,
			DailyRecordToAttendanceItemConverter converter, ManagePerCompanySet companyCommonSetting, List<PersonnelCostSettingImport> personalSetting, Optional<WorkTimeCode> recordWorkTimeCode) {

		Optional<AttendanceTimeOfDailyPerformance> calcResult = Optional.empty();
		
		IntegrationOfDaily result = converter.setData(recordReGetClass.getIntegrationOfDaily()).toDomain();;

		/* 日別実績(Work)の退避 */
		IntegrationOfDaily copyIntegrationOfDaily = converter.setData(recordReGetClass.getIntegrationOfDaily()).toDomain();
		
		// 乖離時間計算用 勤怠項目ID紐づけDto作成
		DailyRecordToAttendanceItemConverter forCalcDivergenceDto = converter.setData(copyIntegrationOfDaily);
		
		//連続勤務の時は予定は計算を行い、実績は計算不要なため
		//2019.3.11時点
		if(recordReGetClass.getWorkType().isPresent() && recordReGetClass.getWorkType().get().getDailyWork().isContinueWork()) {
			calcResult = Optional.of(calcTimeResultForContinusWork(recordReGetClass, 
																   workType, 
																   vacation, 
																   flexCalcMethod, 
																   bonusPayAutoCalcSet, 
																   eachCompanyTimeSet, 
																   scheduleReGetClass,
																   conditionItem, 
																   predetermineTimeSetByPersonInfo, 
																   scheleaveLateSet, 
																   schePred,
																   recordWorkTimeCode));
			// // 編集状態を取得（日別実績の編集状態が持つ勤怠項目IDのみのList作成）
			List<Integer> attendanceItemIdList = recordReGetClass.getIntegrationOfDaily().getEditState().stream()
					.filter(editState -> editState.getEmployeeId()
							.equals(recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getEmployeeId())
							&& editState.getYmd().equals(recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getYmd()))
					.map(editState -> editState.getAttendanceItemId()).distinct().collect(Collectors.toList());

			copyIntegrationOfDaily.setAttendanceTimeOfDailyPerformance(calcResult);
			result = copyIntegrationOfDaily;
			
			List<ItemValue> itemValueList = Collections.emptyList();
			if (!attendanceItemIdList.isEmpty()) {
				DailyRecordToAttendanceItemConverter beforDailyRecordDto = forCalcDivergenceDto.setData(recordReGetClass.getIntegrationOfDaily());
				itemValueList = beforDailyRecordDto.convert(attendanceItemIdList);
				DailyRecordToAttendanceItemConverter afterDailyRecordDto = forCalcDivergenceDto
						.setData(copyIntegrationOfDaily);
				afterDailyRecordDto.merge(itemValueList);

				// 手修正された項目の値を計算前に戻す
				result = afterDailyRecordDto.toDomain();
			}
		}
		else {
			calcResult = Optional.of(collectCalculationResult(vacation,
															  workType,
															  flexCalcMethod,
															  bonusPayAutoCalcSet,
															  eachCompanyTimeSet,
															  forCalcDivergenceDto,
															  divergenceTimeList,
															  calculateOfTotalConstraintTime,
															  scheduleReGetClass,
															  recordReGetClass,
															  conditionItem,
															  predetermineTimeSetByPersonInfo,
															  leaveLateSet,
															  scheleaveLateSet,
															  schePred,
															  recordWorkTimeCode));
			// // 編集状態を取得（日別実績の編集状態が持つ勤怠項目IDのみのList作成）
			List<Integer> attendanceItemIdList = recordReGetClass.getIntegrationOfDaily().getEditState().stream()
					.filter(editState -> editState.getEmployeeId()
							.equals(recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getEmployeeId())
							&& editState.getYmd().equals(recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getYmd()))
					.map(editState -> editState.getAttendanceItemId()).distinct().collect(Collectors.toList());

			copyIntegrationOfDaily.setAttendanceTimeOfDailyPerformance(calcResult);
			
			List<ItemValue> itemValueList = Collections.emptyList();
			if (!attendanceItemIdList.isEmpty()) {
				DailyRecordToAttendanceItemConverter beforDailyRecordDto = forCalcDivergenceDto.setData(recordReGetClass.getIntegrationOfDaily());
				itemValueList = beforDailyRecordDto.convert(attendanceItemIdList);
				DailyRecordToAttendanceItemConverter afterDailyRecordDto = forCalcDivergenceDto
						.setData(copyIntegrationOfDaily);
				afterDailyRecordDto.merge(itemValueList);

				// 手修正された項目の値を計算前に戻す
				copyIntegrationOfDaily = afterDailyRecordDto.toDomain();
			}

			// 手修正後の再計算
			result = reCalc(copyIntegrationOfDaily,
					recordReGetClass.getCalculationRangeOfOneDay(), recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getEmployeeId(), companyCommonSetting , forCalcDivergenceDto,
					attendanceItemIdList, recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getYmd(), PremiumAtr.RegularWork, recordReGetClass.getHolidayCalcMethodSet(),
					recordReGetClass.getWorkTimezoneCommonSet(), recordReGetClass,personalSetting);

			if (!attendanceItemIdList.isEmpty()) {

				// 手修正された項目の値を計算値に戻す(手修正再計算の後Ver)
				DailyRecordToAttendanceItemConverter afterReCalcDto = forCalcDivergenceDto.setData(result);
				afterReCalcDto.merge(itemValueList);
				result = afterReCalcDto.toDomain();
			}

		}
		return result;	
	}
	
	/**
	 * 手修正後の再計算
	 * 
	 * @param calcResultIntegrationOfDaily
	 * @param calculationRangeOfOneDay
	 * @param companyId
	 * @param companyCommonSetting
	 * @param overTotalTime
	 *            手修正前の残業時間の合計
	 * @param attendanceItemIdList
	 * @param recordReGetClass
	 * @param personalSetting 
	 * @param holidayWorkTotalTime
	 *            手修正前の休出時間の合計
	 * @return
	 */
	private static IntegrationOfDaily reCalc(IntegrationOfDaily calcResultIntegrationOfDaily,
			CalculationRangeOfOneDay calculationRangeOfOneDay, String companyId,
			ManagePerCompanySet companyCommonSetting, DailyRecordToAttendanceItemConverter converter,
			List<Integer> attendanceItemIdList, GeneralDate targetDate, PremiumAtr premiumAtr,
			HolidayCalcMethodSet holidayCalcMethodSet, Optional<WorkTimezoneCommonSet> commonSetting,
			ManageReGetClass recordReGetClass, List<PersonnelCostSettingImport> personalSetting) {
		// 乖離時間(AggregateRoot)取得
		List<DivergenceTime> divergenceTimeList = companyCommonSetting.getDivergenceTime();
		if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {

			AttendanceTimeOfDailyPerformance attendanceTimeOfDailyPerformance = calcResultIntegrationOfDaily
					.getAttendanceTimeOfDailyPerformance().get();

			ActualWorkingTimeOfDaily actualWorkingTimeOfDaily = attendanceTimeOfDailyPerformance
					.getActualWorkingTimeOfDaily();

			calcResultIntegrationOfDaily
					.setAttendanceTimeOfDailyPerformance(Optional.of(attendanceTimeOfDailyPerformance
							.inssertActualWorkingTimeOfDaily(actualWorkingTimeOfDaily.inssertTotalWorkingTime(
									actualWorkingTimeOfDaily.getTotalWorkingTime().calcDiverGenceTime()))));

		}

		// //深夜時間
		if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
					.getTotalWorkingTime() != null) {
				;
				calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
						.getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().reCalcMidNightTime();
			}
		}

		// 総労働時間
		if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
					.getTotalWorkingTime() != null) {
				calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
						.getTotalWorkingTime().calcTotalWorkingTimeForReCalc();
			}
		}

		// 予実差異時間
		AttendanceTimeOfExistMinus scheActDiffTime = new AttendanceTimeOfExistMinus(0);
		// 総労働時間が編集している項目リストに含まれていなければ再計算
		if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			AttendanceTimeOfExistMinus scheTime = new AttendanceTimeOfExistMinus(
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getWorkScheduleTimeOfDaily().getWorkScheduleTime().getTotal().valueAsMinutes());
			scheActDiffTime = calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
					.getBudgetTimeVariance();
			if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
					.getActualWorkingTimeOfDaily() != null
					&& calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getActualWorkingTimeOfDaily().getTotalWorkingTime() != null
					&& !attendanceItemIdList.contains(new Integer(559))) {
				AttendanceTimeOfExistMinus totalWorkTime = new AttendanceTimeOfExistMinus(
						calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
								.getActualWorkingTimeOfDaily().getTotalWorkingTime().getTotalTime().valueAsMinutes());
				scheActDiffTime = totalWorkTime.minusMinutes(scheTime.valueAsMinutes());
			}
		}
		// 不就労時間
		AttendanceTimeOfExistMinus alreadlyDedBindTime = new AttendanceTimeOfExistMinus(0);
		// 総労働時間が編集している項目リストに含まれていなければ再計算
		if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {
			alreadlyDedBindTime = calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
					.getUnEmployedTime();
			if (calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
					.getTotalWorkingTime() != null
					&& calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getActualWorkingTimeOfDaily() != null
					&& !attendanceItemIdList.contains(new Integer(559))) {
				// ↓で総控除時間を引く
				alreadlyDedBindTime = new AttendanceTimeOfExistMinus(
						calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getStayingTime()
								.getStayingTime()
								.minusMinutes(calcResultIntegrationOfDaily
										.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily()
										.getTotalWorkingTime().calcTotalDedTime(calculationRangeOfOneDay, premiumAtr,
												holidayCalcMethodSet, commonSetting)
										.valueAsMinutes())
								.valueAsMinutes());
				alreadlyDedBindTime = alreadlyDedBindTime.minusMinutes(calcResultIntegrationOfDaily
						.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime()
						.recalcActualTime().valueAsMinutes());
			}
		}

		// 乖離時間計算用 勤怠項目ID紐づけDto作成
		DailyRecordToAttendanceItemConverter forCalcDivergenceDto = converter.setData(calcResultIntegrationOfDaily);

		if (calcResultIntegrationOfDaily != null
				&& calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().isPresent()) {

			// 割増時間の計算
			PremiumTimeOfDailyPerformance premiumTimeOfDailyPerformance = ActualWorkingTimeOfDaily
					.createPremiumTimeOfDailyPerformance(
							personalSetting,
							Optional.of(forCalcDivergenceDto));

			val reCalcDivergence = ActualWorkingTimeOfDaily.createDivergenceTimeOfDaily(forCalcDivergenceDto,
					divergenceTimeList, calcResultIntegrationOfDaily.getCalAttr(),
					recordReGetClass.getFixRestTimeSetting(),
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getActualWorkingTimeOfDaily().getTotalWorkingTime(),
							recordReGetClass.getWorkTimeSetting(),
							recordReGetClass.getWorkType());

			val reCreateActual = ActualWorkingTimeOfDaily.of(
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getActualWorkingTimeOfDaily().getConstraintDifferenceTime(),
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getActualWorkingTimeOfDaily().getConstraintTime(),
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getActualWorkingTimeOfDaily().getTimeDifferenceWorkingHours(),
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getActualWorkingTimeOfDaily().getTotalWorkingTime(),
					reCalcDivergence,
					// calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getPremiumTimeOfDailyPerformance());
					premiumTimeOfDailyPerformance);

			val reCreateAttendanceTime = new AttendanceTimeOfDailyPerformance(
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getEmployeeId(),
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getYmd(),
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
							.getWorkScheduleTimeOfDaily(),
					reCreateActual,
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getStayingTime(),
					alreadlyDedBindTime, scheActDiffTime,
					calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get().getMedicalCareTime());
			calcResultIntegrationOfDaily.setAttendanceTimeOfDailyPerformance(Optional.of(reCreateAttendanceTime));
		}
		// 総労働の上限設定
		Optional<UpperLimitTotalWorkingHour> upperControl = companyCommonSetting.getUpperControl();
		upperControl.ifPresent(tc -> {
			tc.controlUpperLimit(calcResultIntegrationOfDaily.getAttendanceTimeOfDailyPerformance().get()
					.getActualWorkingTimeOfDaily().getTotalWorkingTime());
		});

		return calcResultIntegrationOfDaily;
	}
	
	/**
	 * 
	 * @param recordReGetClass 実績のデータ管理クラス
	 * @param workType 実績側の勤務種類
	 * @param scheduleReGetClass 予定のデータ管理クラス
	 * @param recordWorkTimeCode 
	 * @return　計算結果
	 */
	public static AttendanceTimeOfDailyPerformance calcTimeResultForContinusWork(ManageReGetClass recordReGetClass, WorkType workType, 
			VacationClass vacation, Optional<SettingOfFlexWork> flexCalcMethod, BonusPayAutoCalcSet bonusPayAutoCalcSet, 
			List<CompensatoryOccurrenceSetting> eachCompanyTimeSet, ManageReGetClass scheduleReGetClass, WorkingConditionItem conditionItem, 
			Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo, DeductLeaveEarly scheleaveLateSet, 
			Optional<PredetermineTimeSetForCalc> schePred, Optional<WorkTimeCode> recordWorkTimeCode){
		
		val workScheduleTime = calcWorkSheduleTime(recordReGetClass.getCalculationRangeOfOneDay(), workType, 
												   vacation, 
												   flexCalcMethod, bonusPayAutoCalcSet, 
												   eachCompanyTimeSet, scheduleReGetClass,conditionItem,
												   predetermineTimeSetByPersonInfo,scheleaveLateSet,schePred,recordWorkTimeCode);
		
		return new AttendanceTimeOfDailyPerformance(recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getEmployeeId(),
													recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getYmd(),
													workScheduleTime,
													ActualWorkingTimeOfDaily.defaultValue(),
													StayingTimeOfDaily.defaultValue(),
													new AttendanceTimeOfExistMinus(0),
													new AttendanceTimeOfExistMinus(0),
													MedicalCareTimeOfDaily.defaultValue());
	}
	
	/**
	 * 時間の計算結果をまとめて扱う
	 * @param schePred 
	 * @param recordWorkTimeCode 
	 * @param schePreTimeSet 
	 * @param breakTimeCount 
	 * @param ootsukaFixCalsSet 
	 * @param integrationOfDaily 
	 * @param flexSetting 
	 * @param 1日の範囲クラス
	 * @return 計算結果
	 */
	private static AttendanceTimeOfDailyPerformance collectCalculationResult(
				VacationClass vacation, WorkType workType,
				Optional<SettingOfFlexWork> flexCalcMethod,BonusPayAutoCalcSet bonusPayAutoCalcSet,
				List<CompensatoryOccurrenceSetting> eachCompanyTimeSet,
				DailyRecordToAttendanceItemConverter forCalcDivergenceDto, List<DivergenceTime> divergenceTimeList,
				CalculateOfTotalConstraintTime calculateOfTotalConstraintTime, ManageReGetClass scheduleReGetClass,
				ManageReGetClass recordReGetClass,WorkingConditionItem conditionItem,
				Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,DeductLeaveEarly leaveLateSet,DeductLeaveEarly scheleaveLateSet, Optional<PredetermineTimeSetForCalc> schePred, Optional<WorkTimeCode> recordWorkTimeCode) {
		
		/*日別実績の勤務予定時間の計算*/
		val workScheduleTime = calcWorkSheduleTime(recordReGetClass.getCalculationRangeOfOneDay(), workType, 
													vacation, 
												   flexCalcMethod, bonusPayAutoCalcSet, 
												    eachCompanyTimeSet, scheduleReGetClass,conditionItem,
												    predetermineTimeSetByPersonInfo,scheleaveLateSet,schePred,
												    recordWorkTimeCode);
		
			/*日別実績の実績時間の計算*/
		Optional<WorkTimeDailyAtr> workDailyAtr = recordReGetClass.getWorkTimeSetting() != null && recordReGetClass.getWorkTimeSetting().isPresent()?
													Optional.of(recordReGetClass.getWorkTimeSetting().get().getWorkTimeDivision().getWorkTimeDailyAtr()):
													Optional.empty();
		ActualWorkingTimeOfDaily actualWorkingTimeOfDaily = ActualWorkingTimeOfDaily.calcRecordTime(recordReGetClass,
			    vacation,
			    workType,
			    workDailyAtr,
			    flexCalcMethod,
				bonusPayAutoCalcSet,
				eachCompanyTimeSet,
				forCalcDivergenceDto,
				divergenceTimeList,
				conditionItem,
				predetermineTimeSetByPersonInfo,
				leaveLateSet,
				workScheduleTime, recordWorkTimeCode);
	
	

		/*滞在時間の計算*/
		StayingTimeOfDaily stayingTime = new StayingTimeOfDaily(recordReGetClass.getIntegrationOfDaily().getPcLogOnInfo().isPresent()?recordReGetClass.getIntegrationOfDaily().getPcLogOnInfo().get().calcPCLogOnCalc(recordReGetClass.getIntegrationOfDaily().getAttendanceLeave(),GoLeavingWorkAtr.LEAVING_WORK):new AttendanceTimeOfExistMinus(0),
																recordReGetClass.getIntegrationOfDaily().getPcLogOnInfo().isPresent()?recordReGetClass.getIntegrationOfDaily().getPcLogOnInfo().get().calcPCLogOnCalc(recordReGetClass.getIntegrationOfDaily().getAttendanceLeave(),GoLeavingWorkAtr.GO_WORK):new AttendanceTimeOfExistMinus(0),
																recordReGetClass.getIntegrationOfDaily().getAttendanceLeavingGate().isPresent()?recordReGetClass.getIntegrationOfDaily().getAttendanceLeavingGate().get().calcBeforeAttendanceTime(recordReGetClass.getIntegrationOfDaily().getAttendanceLeave(),GoLeavingWorkAtr.GO_WORK):new AttendanceTimeOfExistMinus(0),
																StayingTimeOfDaily.calcStayingTimeOfDaily(recordReGetClass.getIntegrationOfDaily().getAttendanceLeavingGate(),recordReGetClass.getIntegrationOfDaily().getPcLogOnInfo(),recordReGetClass.getIntegrationOfDaily().getAttendanceLeave(),calculateOfTotalConstraintTime),
																recordReGetClass.getIntegrationOfDaily().getAttendanceLeavingGate().isPresent()?recordReGetClass.getIntegrationOfDaily().getAttendanceLeavingGate().get().calcBeforeAttendanceTime(recordReGetClass.getIntegrationOfDaily().getAttendanceLeave(),GoLeavingWorkAtr.LEAVING_WORK):new AttendanceTimeOfExistMinus(0));
			
		/*不就労時間*/
		val deductedBindTime = stayingTime.getStayingTime().minusMinutes(actualWorkingTimeOfDaily.getTotalWorkingTime().calcTotalDedTime(recordReGetClass.getCalculationRangeOfOneDay(),PremiumAtr.RegularWork,recordReGetClass.getHolidayCalcMethodSet(),recordReGetClass.getWorkTimezoneCommonSet()).valueAsMinutes());
		val unEmployedTime = deductedBindTime.minusMinutes(actualWorkingTimeOfDaily.getTotalWorkingTime().getActualTime().valueAsMinutes());
		/*予定差異時間の計算*/
		val budgetTimeVariance = new AttendanceTimeOfExistMinus(actualWorkingTimeOfDaily.getTotalWorkingTime().getTotalTime().minusMinutes(workScheduleTime.getWorkScheduleTime().getTotal().valueAsMinutes()).valueAsMinutes());
		/*医療時間*/
		val medicalCareTime = new MedicalCareTimeOfDaily(WorkTimeNightShift.DAY_SHIFT,
														 new AttendanceTime(0),
														 new AttendanceTime(0),
														 new AttendanceTime(0));

		return new AttendanceTimeOfDailyPerformance(recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getEmployeeId(),
													recordReGetClass.getIntegrationOfDaily().getAffiliationInfor().getYmd(),
													workScheduleTime,
													actualWorkingTimeOfDaily,
													stayingTime,
													new AttendanceTimeOfExistMinus(unEmployedTime.valueAsMinutes()),
													budgetTimeVariance,
													medicalCareTime);
		
	}
	
	/**
	 * 計画所定の算出
	 * @param recordOneDay　実績の1日の範囲クラス
	 * @param scheduleOneDay　予定の1日の範囲クラス
	 * @param schePreTime　労働条件の個人勤務区分別の就時コードから取得した所定時間クラス
	 * @param workType
	 * @param scheWorkType 
	 * @param overTimeAutoCalcSet 
	 * @param holidayAutoCalcSetting 
	 * @param personalCondition 
	 * @param vacationClass 
	 * @param late 
	 * @param leaveEarly 
	 * @param workingSystem 
	 * @param illegularAddSetting 
	 * @param flexAddSetting 
	 * @param regularAddSetting 
	 * @param holidayAddtionSet 
	 * @param overTimeAutoCalcAtr 
	 * @param scheWorkTimeDailyAtr 
	 * @param flexCalcMethod 
	 * @param holidayCalcMethodSet 
	 * @param raisingAutoCalcSet 
	 * @param bonusPayAutoCalcSet 
	 * @param calcAtrOfDaily 
	 * @param eachWorkTimeSet 
	 * @param eachCompanyTimeSet 
	 * @param schePred 
	 * @param recordWorkTimeCode 
	 * @param breakTimeCount 
	 * @param integrationOfDaily 
	 * @param flexAutoCalSet 
	 * @param coreTimeSetting 
	 * @param statutoryFrameNoList 
	 * @return
	 */
	private static WorkScheduleTimeOfDaily calcWorkSheduleTime(CalculationRangeOfOneDay recordOneDay, 
															   WorkType workType, 
															   VacationClass vacationClass, 
															   Optional<SettingOfFlexWork> flexCalcMethod,
															   BonusPayAutoCalcSet bonusPayAutoCalcSet, 
															   List<CompensatoryOccurrenceSetting> eachCompanyTimeSet,
															   ManageReGetClass scheRegetManage
															   ,WorkingConditionItem conditionItem,
															   Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
															   DeductLeaveEarly leaveLateSet, Optional<PredetermineTimeSetForCalc> schePred, Optional<WorkTimeCode> recordWorkTimeCode) {
		//勤務予定時間を計算
		//val schedulePredWorkTime = (scheduleOneDay.getWorkInformastionOfDaily().getRecordInfo().getWorkTimeCode() == null)?new AttendanceTime(0):recordOneDay.getPredetermineTimeSetForCalc().getpredetermineTime(workType.getDailyWork());
		AttendanceTime scheTotalTime = new AttendanceTime(0);
		AttendanceTime scheExcessTotalTime = new AttendanceTime(0);
		AttendanceTime scheWithinTotalTime = new AttendanceTime(0);
		//実績所定労働時間の計算
		val actualPredWorkTime = ( recordOneDay == null
									|| !recordWorkTimeCode.isPresent()
									|| workType.getDailyWork().isHolidayWork()
									|| recordOneDay.getPredetermineTimeSetForCalc() == null)
									?new AttendanceTime(0)
//									:recordOneDay.getPredetermineTimeSetForCalc().getPredetermineTimeByAttendanceAtr(workType.getDailyWork().decisionNeedPredTime());
									:recordOneDay.getPredetermineTimeSetForCalc().getpredetermineTime(workType.getDailyWork());	
		//予定勤務種類が設定されてなかったら、実績の所定労働のみ埋めて返す
		if(!scheRegetManage.getWorkType().isPresent()) return new WorkScheduleTimeOfDaily(new WorkScheduleTime(scheTotalTime,scheExcessTotalTime,scheWithinTotalTime),new AttendanceTime(0),actualPredWorkTime);
		
		Optional<WorkTimeDailyAtr> workDailyAtr = (scheRegetManage.getWorkTimeSetting() != null && scheRegetManage.getWorkTimeSetting().isPresent()) ? Optional.of(scheRegetManage.getWorkTimeSetting().get().getWorkTimeDivision().getWorkTimeDailyAtr()) : Optional.empty();
		TotalWorkingTime totalWorkingTime = TotalWorkingTime.createAllZEROInstance();
		Optional<PredetermineTimeSetForCalc> schePreTimeSet = Optional.empty();
		AttendanceTime shedulePreWorkTime = new AttendanceTime(0);
		if(scheRegetManage.getCalculatable()) {
			totalWorkingTime = TotalWorkingTime.calcAllDailyRecord(scheRegetManage,
																   vacationClass, 
																   scheRegetManage.getWorkType().get(), 
																   workDailyAtr, //就業時間帯依存
																   flexCalcMethod, //詳細が決まってなさそう(2018.6.21)
																   bonusPayAutoCalcSet, //会社共通
																   eachCompanyTimeSet, //会社共通 
																   conditionItem,
																   predetermineTimeSetByPersonInfo,
																   leaveLateSet,
																   recordWorkTimeCode
																   );
			scheTotalTime = totalWorkingTime.getTotalTime();
			if(totalWorkingTime.getWithinStatutoryTimeOfDaily() != null)
				scheWithinTotalTime = totalWorkingTime.getWithinStatutoryTimeOfDaily().getWorkTime();
			int overWorkTime = totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()?totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().calcTotalFrameTime():0;
			overWorkTime += totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()?totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().calcTransTotalFrameTime():0;
			int holidayWorkTime = totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()?totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().calcTotalFrameTime():0;
			holidayWorkTime += totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()?totalWorkingTime.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().calcTransTotalFrameTime():0;
			scheExcessTotalTime = new AttendanceTime(overWorkTime + holidayWorkTime);
			//計画所定時間の計算
			schePreTimeSet = Optional.of(scheRegetManage.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc());
			shedulePreWorkTime = (schePreTimeSet.isPresent() && !scheRegetManage.getWorkType().get().getDailyWork().isHolidayWork())
//									  ? schePreTimeSet.get().getPredetermineTimeByAttendanceAtr(scheRegetManage.getWorkType().get().getDailyWork().decisionNeedPredTime())
					                  ? schePreTimeSet.get().getpredetermineTime(scheRegetManage.getWorkType().get().getDailyWork())
									  :new AttendanceTime(0);
		}
		shedulePreWorkTime = schePred.isPresent()?schePred.get().getpredetermineTime(scheRegetManage.getWorkType().get().getDailyWork()):shedulePreWorkTime;
		return new WorkScheduleTimeOfDaily(new WorkScheduleTime(scheTotalTime,scheExcessTotalTime,scheWithinTotalTime),shedulePreWorkTime,actualPredWorkTime);
	}

	/**
	 * エラーチェックの指示メソッド 
	 * @param attendanceItemConverter 
	 * @return 社員のエラーチェック一覧
	 */
	public List<EmployeeDailyPerError> getErrorList(String employeeId,GeneralDate targetDate,
			   										SystemFixedErrorAlarm fixedErrorAlarmCode, CheckExcessAtr checkAtr) {
		List<EmployeeDailyPerError> returnErrorItem = new ArrayList<>();
		if(this.getActualWorkingTimeOfDaily() != null) {
			return getActualWorkingTimeOfDaily().requestCheckError(employeeId, targetDate, fixedErrorAlarmCode, checkAtr);
		}
		return returnErrorItem;
	}

	
}
