package nts.uk.ctx.at.record.dom.actualworkinghours;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.temporarytime.TemporaryTimeOfDaily;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDaily;
//import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeSheet;
import nts.uk.ctx.at.record.dom.calculationattribute.BonusPayAutoCalcSet;
//import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculationMinusExist;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeGoOutTimes;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDayForCalc;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.latetime.IntervalExemptionTime;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AbsenceOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.OverSalaryOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.SubstituteHolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.TimeDigestOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.YearlyReservedOfDaily;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AttendanceItemDictionaryForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CheckExcessAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionAtr;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.FlexWithinWorkTimeSheet;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.ManageReGetClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSpanForDailyCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
//import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.valueobject.CalcFlexTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.valueobject.WorkHour;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeFrame;
//import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTime;
//import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTime;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethod;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfEachPremiumHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.FlexCalcMethodOfHalfWork;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaiseSalaryTimeOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.stamp.GoOutReason;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.SystemFixedErrorAlarm;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.dom.PremiumAtr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.DeductLeaveEarly;
import nts.uk.ctx.at.shared.dom.scherec.addsettingofworktime.HolidayCalcMethodSet;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalFlexOvertimeSetting;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
//import nts.uk.ctx.at.shared.dom.ot.autocalsetting.TimeLimitUpperLimitSetting;
//import nts.uk.ctx.at.shared.dom.statutory.worktime.sharedNew.DailyUnit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayAdditionAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
//import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
//import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
//import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
//import nts.uk.ctx.at.shared.dom.workrule.statutoryworktime.DailyCalculationPersonalInformation;
//import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.EmTimeZoneSet;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
//import nts.uk.ctx.at.shared.dom.worktime.common.HolidayCalculation;
//import nts.uk.ctx.at.shared.dom.worktime.common.LateEarlyAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
//import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixRestTimezoneSet;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
//import nts.uk.ctx.at.shared.dom.worktime.flexset.CoreTimeSetting;
//import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexCalcSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 
 * @author nampt
 * 総労働時間
 *
 */
@Getter
public class TotalWorkingTime {
	
	//総労働時間
	private AttendanceTime totalTime;
	
	//総計算時間
	private AttendanceTime totalCalcTime;
	
	//実働時間
	private AttendanceTime actualTime;
	
	//日別実績の所定内時間
	private WithinStatutoryTimeOfDaily withinStatutoryTimeOfDaily;
	
	//日別実績の所定外時間
	private ExcessOfStatutoryTimeOfDaily excessOfStatutoryTimeOfDaily;
	
	//日別実績の遅刻時間
	private List<LateTimeOfDaily> lateTimeOfDaily = Collections.emptyList();
	
	//日別実績の早退時間
	private List<LeaveEarlyTimeOfDaily> leaveEarlyTimeOfDaily = Collections.emptyList(); 
	
	//日別実績の休憩時間
	private BreakTimeOfDaily breakTimeOfDaily;
	
	//日別実績の外出時間	
	private List<OutingTimeOfDaily> outingTimeOfDailyPerformance;
		
	//加給時間
	private RaiseSalaryTimeOfDailyPerfor raiseSalaryTimeOfDailyPerfor;
	
	//勤務回数
	private WorkTimes workTimes;
	
	/*日別実績の臨時時間*/
	private TemporaryTimeOfDaily temporaryTime;
	
	/*短時間勤務時間*/
	private ShortWorkTimeOfDaily shotrTimeOfDaily;
	
	/*日別実績の休暇時間*/
	private HolidayOfDaily holidayOfDaily;
	
	/*休暇加算時間*/
	@Setter
	private AttendanceTime vacationAddTime = new AttendanceTime(0);
	
	/**
	 * Construtor
	 * @param totalTime
	 * @param totalCalcTime
	 * @param actualTime
	 * @param withinStatutoryTimeOfDaily
	 * @param excessOfStatutoryTimeOfDaily
	 * @param lateTimeOfDaily
	 * @param leaveEarlyTimeOfDaily
	 * @param breakTimeOfDaily
	 * @param outingTimeOfDailyPerformance
	 * @param raiseSalaryTimeOfDailyPerfor
	 * @param workTimes
	 * @param temporaryTime
	 */
	public TotalWorkingTime(AttendanceTime totalTime, AttendanceTime totalCalcTime, AttendanceTime actualTime,
			WithinStatutoryTimeOfDaily withinStatutoryTimeOfDaily,
			ExcessOfStatutoryTimeOfDaily excessOfStatutoryTimeOfDaily, List<LateTimeOfDaily> lateTimeOfDaily,
			List<LeaveEarlyTimeOfDaily> leaveEarlyTimeOfDaily, BreakTimeOfDaily breakTimeOfDaily,
			List<OutingTimeOfDaily> outingTimeOfDailyPerformance,
			RaiseSalaryTimeOfDailyPerfor raiseSalaryTimeOfDailyPerfor, WorkTimes workTimes,
			TemporaryTimeOfDaily temporaryTime, ShortWorkTimeOfDaily shotrTime,HolidayOfDaily holidayOfDaily) {
		super();
		this.totalTime = totalTime;
		this.totalCalcTime = totalCalcTime;
		this.actualTime = actualTime;
		this.withinStatutoryTimeOfDaily = withinStatutoryTimeOfDaily;
		this.excessOfStatutoryTimeOfDaily = excessOfStatutoryTimeOfDaily;
		this.lateTimeOfDaily = lateTimeOfDaily;
		this.leaveEarlyTimeOfDaily = leaveEarlyTimeOfDaily;
		this.breakTimeOfDaily = breakTimeOfDaily;
		this.outingTimeOfDailyPerformance = outingTimeOfDailyPerformance;
		this.raiseSalaryTimeOfDailyPerfor = raiseSalaryTimeOfDailyPerfor;
		this.workTimes = workTimes;
		this.temporaryTime = temporaryTime;
		this.shotrTimeOfDaily = shotrTime;
		this.holidayOfDaily = holidayOfDaily;
	}
	
	
	public static TotalWorkingTime createAllZEROInstance() {
		List<HolidayWorkFrameTime> lstHolidayWorkFrameTime = new ArrayList<>();
		for(int i = 1; i<=10; i++) {
			TimeDivergenceWithCalculation holidayWorkTime = TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0), new AttendanceTime(0));			
			HolidayWorkFrameTime frameTime = new HolidayWorkFrameTime(new HolidayWorkFrameNo(i),
					Finally.of(holidayWorkTime), Finally.of(holidayWorkTime), Finally.of(new AttendanceTime(0)));
			lstHolidayWorkFrameTime.add(frameTime);
		}
		return new TotalWorkingTime(new AttendanceTime(0),
									new AttendanceTime(0),
									new AttendanceTime(0),
									WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(new AttendanceTime(0), 
																								new AttendanceTime(0), 
																								new AttendanceTime(0), 
																								new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))), 
																								new AttendanceTime(0)),
									new ExcessOfStatutoryTimeOfDaily(new ExcessOfStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)), new AttendanceTime(0)),
																	 Optional.of(new OverTimeOfDaily(Collections.emptyList(), 
																			 						 Collections.emptyList(), 
																			 						 Finally.of(new ExcessOverTimeWorkMidNightTime(TimeDivergenceWithCalculation.createTimeWithCalculation(new AttendanceTime(0), new AttendanceTime(0)))),
																			 						 new AttendanceTime(0),
																			 						 new nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime(TimeDivergenceWithCalculationMinusExist.createTimeWithCalculation(new AttendanceTimeOfExistMinus(0), new AttendanceTimeOfExistMinus(0)),new AttendanceTime(0)), 
																			 						 new AttendanceTime(0))),
																	 Optional.of(new HolidayWorkTimeOfDaily(Collections.emptyList(), 
																			 								lstHolidayWorkFrameTime, 
																			 								Finally.of(new HolidayMidnightWork(Collections.emptyList())), 
																			 								new AttendanceTime(0)))),
									Arrays.asList(new LateTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)), 
																	  TimeWithCalculation.sameTime(new AttendanceTime(0)), 
																	  new WorkNo(0), 
																	  TimevacationUseTimeOfDaily.defaultValue(), 
																	  IntervalExemptionTime.defaultValue())),
									Arrays.asList(new LeaveEarlyTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)), 
											  								TimeWithCalculation.sameTime(new AttendanceTime(0)), 
											  								new WorkNo(0), 
											  								TimevacationUseTimeOfDaily.defaultValue(), 
											  								IntervalExemptionTime.defaultValue())),
									new BreakTimeOfDaily(DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0))),
											DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0))),
																  new BreakTimeGoOutTimes(0),
																  new AttendanceTime(0),
																  Collections.emptyList()),
									Collections.emptyList(),
									new RaiseSalaryTimeOfDailyPerfor(new ArrayList<>(),new ArrayList<>()),
									new WorkTimes(0),
									new TemporaryTimeOfDaily(Collections.emptyList()),
									new ShortWorkTimeOfDaily(new WorkTimes(0),
															 DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0))),
															 DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0)), TimeWithCalculation.sameTime(new AttendanceTime(0))),
															 ChildCareAttribute.CARE),
									new HolidayOfDaily(new AbsenceOfDaily(new AttendanceTime(0)),
													   new TimeDigestOfDaily(new AttendanceTime(0),new AttendanceTime(0)),
													   new YearlyReservedOfDaily(new AttendanceTime(0)),
													   new SubstituteHolidayOfDaily(new AttendanceTime(0), new AttendanceTime(0)),
													   new OverSalaryOfDaily(new AttendanceTime(0), new AttendanceTime(0)),
													   new SpecialHolidayOfDaily(new AttendanceTime(0), new AttendanceTime(0)),
													   new AnnualOfDaily(new AttendanceTime(0), new AttendanceTime(0))));
	}
	
	/**
	 * 日別実績の総労働時間
	 * @param recordClass 実績
	 * @param vacationClass 休暇クラス
	 * @param workType 勤務種類
	 * @param workTimeDailyAtr 勤務形態区分
	 * @param flexCalcMethod フレックス勤務の設定
	 * @param bonusPayAutoCalcSet 加給自動計算設定
	 * @param eachCompanyTimeSet 会社別代休時間設定
	 * @param conditionItem 労働条件項目
	 * @param predetermineTimeSetByPersonInfo 計算用所定時間設定
	 * @param leaveLateSet 遅刻早退を控除する
	 * @param recordWorkTimeCode 就業時間帯コード
	 * @return 総労働時間
	 */
	public static TotalWorkingTime calcAllDailyRecord(
			ManageReGetClass recordClass,
			VacationClass vacationClass,
			WorkType workType,
			Optional<WorkTimeDailyAtr> workTimeDailyAtr,
			Optional<SettingOfFlexWork> flexCalcMethod,
			BonusPayAutoCalcSet bonusPayAutoCalcSet,
			List<CompensatoryOccurrenceSetting> eachCompanyTimeSet,
			WorkingConditionItem conditionItem,
			Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
			DeductLeaveEarly leaveLateSet,
			Optional<WorkTimeCode> recordWorkTimeCode) {
		
		/*日別実績の所定内時間(就業時間)*/
		val withinStatutoryTimeOfDaily = WithinStatutoryTimeOfDaily.calcStatutoryTime(
				recordClass,
				vacationClass,
				workType,
				CalcMethodOfNoWorkingDayForCalc.isCalculateFlexTime,
				flexCalcMethod,
				workTimeDailyAtr,
				recordWorkTimeCode,
				conditionItem,
				predetermineTimeSetByPersonInfo);
		
		//日別実績の所定外時間
		ExcessOfStatutoryTimeOfDaily excesstime = ExcessOfStatutoryTimeOfDaily.calculationExcessTime(
				recordClass,
				CalcMethodOfNoWorkingDayForCalc.isCalculateFlexTime,
				workType,
				flexCalcMethod,
				vacationClass,
				StatutoryDivision.Nomal,
				recordWorkTimeCode,
				workTimeDailyAtr,
				eachCompanyTimeSet,
				conditionItem,
				predetermineTimeSetByPersonInfo,recordClass.getCoreTimeSetting());
		
		//日別実績の休憩時間
		val breakTime = BreakTimeOfDaily.calcTotalBreakTime(recordClass.getCalculationRangeOfOneDay(),recordClass.getBreakCount(),recordClass.getCalculatable(),PremiumAtr.RegularWork,recordClass.getHolidayCalcMethodSet(),recordClass.getWorkTimezoneCommonSet());
		
		//日別実績の外出時間
		val outingList = OutingTimeOfDaily.calcList(recordClass);
		
		//日別実績の短時間勤務
		val shotrTime = ShortWorkTimeOfDaily.calcShortWorkTime(recordClass,PremiumAtr.RegularWork,recordClass.getHolidayCalcMethodSet(),recordClass.getWorkTimezoneCommonSet());
		
		//加給時間
		val raiseTime = RaiseSalaryTimeOfDailyPerfor.calcBonusPayTime(recordClass.getCalculationRangeOfOneDay(), recordClass.getIntegrationOfDaily().getCalAttr().getRasingSalarySetting(), bonusPayAutoCalcSet, recordClass.getIntegrationOfDaily().getCalAttr());
		
		//勤務回数
		val workCount = new WorkTimes(workCounter(recordClass.getCalculationRangeOfOneDay()));
		
		/*日別実績の臨時時間*/
		val tempTime = new TemporaryTimeOfDaily(Collections.emptyList());

		//日別実績の遅刻時間
		List<LateTimeOfDaily> lateTime = LateTimeOfDaily.calcList(
				recordClass,
				vacationClass,
				workType,
				conditionItem,
				predetermineTimeSetByPersonInfo,
				leaveLateSet,
				recordWorkTimeCode);
		
		//日別実績の早退時間
		List<LeaveEarlyTimeOfDaily> leaveEarlyTime = LeaveEarlyTimeOfDaily.calcList(
				recordClass,
				vacationClass,
				workType,
				conditionItem,
				predetermineTimeSetByPersonInfo,
				leaveLateSet,
				recordWorkTimeCode);
		
		//日別実績の休暇
		val vacationOfDaily = VacationClass.calcUseRestTime(
				workType,
				recordWorkTimeCode,
				conditionItem,
				outingList,
				lateTime,
				leaveEarlyTime,
				recordClass,
				predetermineTimeSetByPersonInfo);
		
		//総労働時間		
		int flexTime = workTimeDailyAtr.isPresent()&&workTimeDailyAtr.get().isFlex() ? excesstime.getOverTimeWork().get().getFlexTime().getFlexTime().getTime().valueAsMinutes():0;
		flexTime = (flexTime<0)?0:flexTime;
		val totalWorkTime = new AttendanceTime(withinStatutoryTimeOfDaily.getWorkTime().valueAsMinutes()
											+ withinStatutoryTimeOfDaily.getWithinPrescribedPremiumTime().valueAsMinutes() 
											+ excesstime.calcOverTime().valueAsMinutes()
											+ excesstime.calcWorkHolidayTime().valueAsMinutes()
											+ tempTime.totalTemporaryFrameTime()
											+ flexTime);
		
		//総計算時間
		val totalCalcTime = new AttendanceTime(withinStatutoryTimeOfDaily.getActualWorkTime().valueAsMinutes()
											+ withinStatutoryTimeOfDaily.getWithinPrescribedPremiumTime().valueAsMinutes() 
											+ excesstime.calcOverTime().valueAsMinutes()
											+ excesstime.calcWorkHolidayTime().valueAsMinutes()
											+ tempTime.totalTemporaryFrameTime()
											+ flexTime);
		
		//実働時間
		val actualTime = new AttendanceTime(withinStatutoryTimeOfDaily.getActualWorkTime().valueAsMinutes()
											+ withinStatutoryTimeOfDaily.getWithinPrescribedPremiumTime().valueAsMinutes() 
											+ excesstime.calcOverTime().valueAsMinutes()
											+ excesstime.calcWorkHolidayTime().valueAsMinutes()
											+ tempTime.totalTemporaryFrameTime()
											+ flexTime
											/*変形基準内残業の時間もここにタス*/);
		//実働時間の計算
		boolean isOOtsukaIWMode = decisionIWOOtsukaMode(workType,recordWorkTimeCode,recordClass);
		//大塚専用IW専用処理
		if(isOOtsukaIWMode) {
			if(recordClass.getPredSetForOOtsuka().isPresent()) {
				withinStatutoryTimeOfDaily.setActualWorkTime(recordClass.getPredSetForOOtsuka().get().getAdditionSet().getPredTime().getOneDay());
			}
		}
		TotalWorkingTime returnTotalWorkingTimereturn = new TotalWorkingTime(
				totalWorkTime,
				totalCalcTime,
				actualTime,
				withinStatutoryTimeOfDaily,
				excesstime,
				lateTime,
				leaveEarlyTime,
				breakTime,
				outingList,
				raiseTime,
				workCount,
				tempTime,
				shotrTime,
				vacationOfDaily);
		
		//休暇加算時間の計算
		returnTotalWorkingTimereturn.setVacationAddTime(
				calcTotalHolidayAddTime(
						recordClass,
						vacationClass,
						workType,
						conditionItem,
						predetermineTimeSetByPersonInfo,
						recordWorkTimeCode,
						lateTime,
						leaveEarlyTime,
						outingList));
		
		return returnTotalWorkingTimereturn;
	}
	
	private static int workCounter(CalculationRangeOfOneDay oneDay) {
		int workCount = 0;
		if(oneDay != null && oneDay.getAttendanceLeavingWork() != null) {
			workCount = oneDay.getAttendanceLeavingWork().getTimeLeavingWorks().stream()
																   .filter(tc -> 
																   		tc.getAttendanceStamp() != null
																   	&&  tc.getAttendanceStamp().isPresent()
																   	&&  tc.getAttendanceStamp().get().getStamp() != null
																   	&&  tc.getAttendanceStamp().get().getStamp().isPresent()
																   	&&  tc.getAttendanceStamp().get().getStamp().get().getTimeWithDay() != null
																   	&&  tc.getLeaveStamp() != null
																   	&&  tc.getLeaveStamp().isPresent()
																   	&&  tc.getLeaveStamp().get().getStamp() != null
																   	&&  tc.getLeaveStamp().get().getStamp().isPresent()
																   	&&  tc.getLeaveStamp().get().getStamp().get().getTimeWithDay() != null
																   	&&  tc.getTimespan() != null
																   	&&  tc.getTimespan().lengthAsMinutes() > 0)
																   .collect(Collectors.toList()).size();
		}
		//↓に臨時を入れる
		//リンジ
		
		return workCount;
	}

	public Optional<LeaveEarlyTimeOfDaily> getLeaveEarlyTimeNo(int no){
		return leaveEarlyTimeOfDaily.stream().filter(c -> c.getWorkNo().v() == no).findFirst();
	}

	/**
	 * エラーチェック(乖離以外)への分岐 
	 * @param attendanceItemConverter 
	 * @return 社員のエラーチェック一覧
	 */
	public List<EmployeeDailyPerError> checkOverTimeExcess(String employeeId,
														   GeneralDate targetDate,
														   SystemFixedErrorAlarm fixedErrorAlarmCode,
														   CheckExcessAtr checkAtr) {
		List<EmployeeDailyPerError> returnErrorItem = new ArrayList<>();
		AttendanceItemDictionaryForCalc attendanceItemDictionary = AttendanceItemDictionaryForCalc.setDictionaryValue(); 
		switch(checkAtr) {
			//残業超過
			case OVER_TIME_EXCESS:
				if(this.getExcessOfStatutoryTimeOfDaily() != null) 
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkOverTimeExcess(employeeId, targetDate,attendanceItemDictionary,new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//休出超過
			case REST_TIME_EXCESS:
				if(this.getExcessOfStatutoryTimeOfDaily() != null)
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkHolidayWorkTimeExcess(employeeId, targetDate, attendanceItemDictionary,new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//フレ超過
			case FLEX_OVER_TIME:
				if(this.getExcessOfStatutoryTimeOfDaily() != null) 
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkFlexTimeExcess(employeeId, targetDate,"フレックス時間", attendanceItemDictionary,new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//深夜超過
			case MIDNIGHT_EXCESS:
				if(this.getWithinStatutoryTimeOfDaily() != null)
					returnErrorItem.addAll(this.getWithinStatutoryTimeOfDaily().checkWithinMidNightExcess(employeeId, targetDate,"内深夜時間",attendanceItemDictionary,new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				if(this.getExcessOfStatutoryTimeOfDaily() != null)
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkMidNightExcess(employeeId, targetDate,"外深夜時間", attendanceItemDictionary,new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//事前残業申請超過
			case PRE_OVERTIME_APP_EXCESS:
				if(this.getExcessOfStatutoryTimeOfDaily() != null) 
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkPreOverTimeExcess(employeeId, targetDate, attendanceItemDictionary,new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//事前休出申請超過
			case PRE_HOLIDAYWORK_APP_EXCESS:
				if(this.getExcessOfStatutoryTimeOfDaily() != null)
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkPreHolidayWorkTimeExcess(employeeId, targetDate,attendanceItemDictionary, new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//事前フレ申請超過
			case PRE_FLEX_APP_EXCESS:
				if(this.getExcessOfStatutoryTimeOfDaily() != null) 
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkPreFlexTimeExcess(employeeId, targetDate,"フレックス時間",attendanceItemDictionary, new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//事前深夜
			case PRE_MIDNIGHT_EXCESS:
				if(this.getExcessOfStatutoryTimeOfDaily() != null) 
					returnErrorItem.addAll(this.getExcessOfStatutoryTimeOfDaily().checkPreMidNightExcess(employeeId, targetDate,"外深夜時間",attendanceItemDictionary, new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value)));
				break;
			//遅刻
			case LATE:
				if(!this.getLateTimeOfDaily().isEmpty())
					returnErrorItem.addAll(this.getLateTimeOfDaily().stream().map(tc -> tc.checkError(employeeId, targetDate,"遅刻時間", attendanceItemDictionary, new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value))).flatMap(tc -> tc.stream()).collect(Collectors.toList()));					
				break;
			//早退
			case LEAVE_EARLY:
				if(!this.getLeaveEarlyTimeOfDaily().isEmpty())
					returnErrorItem.addAll(this.getLeaveEarlyTimeOfDaily().stream().map(tc -> tc.checkError(employeeId, targetDate,"早退時間", attendanceItemDictionary, new ErrorAlarmWorkRecordCode(fixedErrorAlarmCode.value))).flatMap(tc -> tc.stream()).collect(Collectors.toList()));;
				break;		   
			default :
				throw new RuntimeException("unknown error item Atr in DailyCalc:"+checkAtr);
		}
		return returnErrorItem;
	}
	
	public Optional<LateTimeOfDaily> getLateTimeNo(int no){
		return this.lateTimeOfDaily.stream().filter(l -> l.getWorkNo().v() == no).findFirst();
	}

	public Optional<OutingTimeOfDaily> getOutingTimeByReason(GoOutReason reason){
		return getOutingTimeByReason(reason.value);
	}

	public Optional<OutingTimeOfDaily> getOutingTimeByReason(int reason){
		return this.outingTimeOfDailyPerformance.stream().filter(o -> o.getReason().value == reason).findFirst();
	}

	public TotalWorkingTime calcDiverGenceTime() {
		TotalWorkingTime result = new TotalWorkingTime(this.totalTime,
									this.totalCalcTime,
									this.actualTime,
									this.withinStatutoryTimeOfDaily!=null?this.withinStatutoryTimeOfDaily.calcDiverGenceTime():this.withinStatutoryTimeOfDaily,
									this.excessOfStatutoryTimeOfDaily!=null?this.excessOfStatutoryTimeOfDaily.calcDiverGenceTime():this.excessOfStatutoryTimeOfDaily,
									this.lateTimeOfDaily,
									this.leaveEarlyTimeOfDaily,
									this.breakTimeOfDaily,
									this.outingTimeOfDailyPerformance,
									this.raiseSalaryTimeOfDailyPerfor, this.workTimes, this.temporaryTime, this.shotrTimeOfDaily, this.holidayOfDaily); 
		result.setVacationAddTime(this.vacationAddTime);
		return result;
	}


	/**
	 * 控除合計時間の計算(手修正不就労用)
	 * @return
	 */
	public AttendanceTime calcTotalDedTime(CalculationRangeOfOneDay oneDay,PremiumAtr premiumAtr,HolidayCalcMethodSet holidayCalcMethodSet,Optional<WorkTimezoneCommonSet> commonSetting) {
		AttendanceTime totalTime = new AttendanceTime(0);
		if(oneDay == null) return totalTime;
		//休憩時間
		totalTime = BreakTimeOfDaily.calculationDedBreakTime(DeductionAtr.Deduction, oneDay,premiumAtr,holidayCalcMethodSet,commonSetting).getTotalTime().getCalcTime();
		//外出
		//短時間
		
		return totalTime;
	}
	
	
	public void calcTotalWorkingTimeForReCalc() {
		this.totalTime = recalcTotalWorkingTime();
	}
	/**
	 * 手修正の再計算時に使用する総労働時間の計算
	 * @return
	 */
	public AttendanceTime recalcTotalWorkingTime() {
		int withinTime = calcWithinTime();
		int overTime = calcOverTime();
		int holidayTime = calcHolidayTime();
		int rinzi = 0;
		return new AttendanceTime(withinTime + overTime + holidayTime + rinzi);
	}

	public int calcHolidayTime() {
		int totalHolidayTimeTime = 0;
		int totaltransTime = 0;
		if(this.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()) {
			totalHolidayTimeTime = this.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayWorkFrameTime()
																									.stream()
																									.filter(tc -> tc.getHolidayWorkTime().isPresent())
																									.map(tc -> tc.getHolidayWorkTime().get().getTime().valueAsMinutes())
																									.collect(Collectors.summingInt(tc -> tc));
			totaltransTime = this.getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().get().getHolidayWorkFrameTime()
																							  .stream()
																							  .filter(tc -> tc.getTransferTime().isPresent())
																							  .map(tc -> tc.getTransferTime().get().getTime().valueAsMinutes())
																							  .collect(Collectors.summingInt(tc -> tc));
		}
		return totalHolidayTimeTime + totaltransTime;
	}
	
	/**
	 * 手修正再計算用
	 * 残業時間＋フレ＋振替時間を求める
	 * @return
	 */
	public int calcOverTime() {
		int removeFlexTime = 0;
		int flexTime = 0;
		if(this.excessOfStatutoryTimeOfDaily.getOverTimeWork().isPresent()) {
			removeFlexTime = calcOverTimeRemoveFlex();
			flexTime = this.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getFlexTime().getTime().valueAsMinutes();
			flexTime = flexTime > 0 ? flexTime : 0;
		}
		return removeFlexTime + flexTime;
	}
	
	public int calcOverTimeRemoveFlex() {
		int totalOverTime = 0;
		int totalTransTime = 0;
		if(this.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()) {
			totalOverTime = this.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTime()
				  				.stream()
				  				.filter(tc -> tc != null 
				  						&& tc.getOverTimeWork() != null
				  						&& tc.getOverTimeWork().getTime() != null)
				  				.map(tc -> tc.getOverTimeWork().getTime().valueAsMinutes())
				  				.collect(Collectors.summingInt(tc -> tc));
		
			totalTransTime = this.getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getOverTimeWorkFrameTime()
				   				 .stream()
				   				 .filter(tc -> tc != null 
				   				 	&& tc.getTransferTime() != null
				   				 	&& tc.getTransferTime().getTime() != null)
				   				 .map(tc -> tc.getTransferTime().getTime().valueAsMinutes())
				   				 .collect(Collectors.summingInt(tc -> tc));
		}
		return totalOverTime + totalTransTime;
	}

	/**
	 * 手修正後の再計算(実働時間)
	 * @return
	 */
	public AttendanceTime recalcActualTime() {
		//実働時間
		//return recalcTotalWorkingTime();
		//return this.getWithinStatutoryTimeOfDaily().getActualWorkTime();
		return this.getActualTime();
						 //+変形基準内残業を足して返す;
	}
	/**
	 * 手修正、再計算用
	 * 所定内時間+所定内割増時間を求める
	 * @return
	 */
	private int calcWithinTime() {
		if(this.getWithinStatutoryTimeOfDaily() != null) {
			int workTime = this.getWithinStatutoryTimeOfDaily().getWorkTime() != null
							?this.getWithinStatutoryTimeOfDaily().getWorkTime().valueAsMinutes()
							:0;
			int premiumTime = this.getWithinStatutoryTimeOfDaily().getWithinPrescribedPremiumTime() != null
							? this.getWithinStatutoryTimeOfDaily().getWithinPrescribedPremiumTime().valueAsMinutes()
							:0;
			return workTime + premiumTime;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * 深夜時間の上限時間調整処理(加給と深夜へ指示を出す)
	 * @param upperTime 上限時間
	 */
	public void controlUpperTime(AttendanceTime upperTime) {
		//深夜の上限制御
		this.controlUpperTimeForMidTime(upperTime);
		//加給の上限制御
		this.raiseSalaryTimeOfDailyPerfor.controlUpperTimeForSalaryTime(upperTime);
	}
	
	/**
	 * 深夜時間の上限時間制御指示
	 * @param upperTime 上限時間
	 */
	private void controlUpperTimeForMidTime(AttendanceTime upperTime) {
		if(this.withinStatutoryTimeOfDaily != null)
			this.withinStatutoryTimeOfDaily.controlMidTimeUpper(upperTime);
		if(this.excessOfStatutoryTimeOfDaily != null)
			this.excessOfStatutoryTimeOfDaily.controlMidTimeUpper(upperTime);
	}

	public void setWithinWorkTime(AttendanceTime predetermineTime) {
		if(this.withinStatutoryTimeOfDaily != null)
			this.withinStatutoryTimeOfDaily.setWorkTime(predetermineTime);
	}
		
	
	/**
	 * 大塚モードの計算（遅刻早退）
	 * @param fixRestTimeZoneSet 
	 * @param fixWoSetting 
	 * @param attendanceLeave 
	 * @param workType 
	 * @param attendanceTime 
	 * @return
	 */
	public TotalWorkingTime reCalcLateLeave(Optional<WorkTimezoneCommonSet> workTimeZone, Optional<FixRestTimezoneSet> fixRestTimeZoneSet, List<EmTimeZoneSet> fixWoSetting, Optional<TimeLeavingOfDailyPerformance> attendanceLeave, AttendanceTime actualPredTime, WorkType workType) {
		if(workType.getDailyWork().decisionNeedPredTime() != AttendanceHolidayAttr.FULL_TIME) {
			offSetRestTime(workTimeZone,fixRestTimeZoneSet,fixWoSetting,attendanceLeave);	
		}
		else {
			offSetUnUseBreakTime(workTimeZone, fixRestTimeZoneSet, fixWoSetting, attendanceLeave, actualPredTime);	
		}
		return this;
	}
	
	/**
	 * 大塚モード(欠勤控除)時の休暇加算時間との相殺処理
	 */
	private void offSetRestTime(Optional<WorkTimezoneCommonSet> workTimeZone, Optional<FixRestTimezoneSet> fixRestTimeZoneSet, List<EmTimeZoneSet> fixWoSetting, Optional<TimeLeavingOfDailyPerformance> attendanceLeave) {		
		//休暇時に計算する設定かどうか判断
		if(!workTimeZone.isPresent()
		|| workTimeZone.get().getHolidayCalculation().getIsCalculate().isNotUse()) {
			return ;
		}

		AttendanceTime unBreakTime = new AttendanceTime(0);
		//休憩未取得を計算するためのチェック
		if(fixRestTimeZoneSet.isPresent()
		&& attendanceLeave != null
		&& attendanceLeave.isPresent()) {
			//休憩未取得時間の計算
			unBreakTime = this.getBreakTimeOfDaily().calcUnUseBrekeTime(fixRestTimeZoneSet.get(), fixWoSetting, attendanceLeave.get());
		}

		
		
		
		//遅刻早退の合計時間を取得
		TimeWithCalculation lateLeaveTotalTime = this.calcLateLeaveTotalTime();
		//欠勤控除時間の計算
		TimeWithCalculation absenteeismDeductionTime = this.calcAbsenteeismDeductionTime(lateLeaveTotalTime,unBreakTime);
		if(!this.lateTimeOfDaily.isEmpty()) {
			//勤務NOの昇順でソート
			this.lateTimeOfDaily.sort((c1, c2) -> c1.getWorkNo().compareTo(c2.getWorkNo()));
			//欠勤控除時間を遅刻時間とする
			this.lateTimeOfDaily.get(0).rePlaceLateTime(absenteeismDeductionTime);
		}
		
		if(!this.leaveEarlyTimeOfDaily.isEmpty()) {
			//勤務NOの昇順でソート
			this.leaveEarlyTimeOfDaily.sort((c1, c2) -> c1.getWorkNo().compareTo(c2.getWorkNo()));
			//早退時間をクリア
			this.leaveEarlyTimeOfDaily.get(0).rePlaceLeaveEarlyTime(TimeWithCalculation.sameTime(new AttendanceTime(0)));
		}
	}
	
	/**
	 * 大塚モード(欠勤控除)時の休憩未取得時間との相殺処理
	 */
	private void offSetUnUseBreakTime(Optional<WorkTimezoneCommonSet> workTimeZone, Optional<FixRestTimezoneSet> fixRestTimeZoneSet, List<EmTimeZoneSet> fixWoSetting, Optional<TimeLeavingOfDailyPerformance> attendanceLeave,
									AttendanceTime actualPredTime) {
		AttendanceTime unBreakTime = new AttendanceTime(0);
		//休憩未取得を計算するためのチェック
		if(fixRestTimeZoneSet.isPresent()
		&& attendanceLeave != null
		&& attendanceLeave.isPresent()) {
			//休憩未取得時間の計算
			unBreakTime = this.getBreakTimeOfDaily().calcUnUseBrekeTime(fixRestTimeZoneSet.get(), fixWoSetting, attendanceLeave.get());
		}
		
		int withinBreakTime = 0;
		//就業時間帯に設定されている休憩のループ
		if(fixRestTimeZoneSet.isPresent()) {
			for(DeductionTime breakTImeSheet : fixRestTimeZoneSet.get().getLstTimezone()) {
				//就業時間帯に設定されている勤務時間帯のstream
				withinBreakTime += fixWoSetting.stream().filter(tc -> tc.getTimezone().isOverlap(breakTImeSheet))
													  .map(tt -> tt.getTimezone().getDuplicatedWith(breakTImeSheet.timeSpan()).get().lengthAsMinutes())
													  .collect(Collectors.summingInt(ts -> ts));	
			}			
		}
		//所定内休憩未取得時間
		final val withinUnUseBreakTime = this.getWithinStatutoryTimeOfDaily().calcUnUseWithinBreakTime(unBreakTime, actualPredTime, new AttendanceTime(withinBreakTime));
		if(withinUnUseBreakTime.greaterThan(0)) {
			final AttendanceTime restWithinUnUseBreakTime = minusLateTime(withinUnUseBreakTime);
			if(restWithinUnUseBreakTime.greaterThan(0)) {
				minusEarlyLeaveTime(restWithinUnUseBreakTime);
			}
		}
		
	}
	

	/**
	 * 大塚モード(欠勤控除)時の休憩未取得時間との相殺処理(遅刻時間Ver)
	 * @return 残遅早相殺可能時間
	 */
	private AttendanceTime minusLateTime(AttendanceTime withinUnUseBreakTime) {
		AttendanceTime restOffSetTime = withinUnUseBreakTime;
		if(!this.lateTimeOfDaily.isEmpty()) {
			//勤務NOの昇順でソート
			this.lateTimeOfDaily.sort((c1, c2) -> c1.getWorkNo().compareTo(c2.getWorkNo()));
			for(LateTimeOfDaily ltOfDaily:this.lateTimeOfDaily) {
				val lateTime = ltOfDaily.getLateTime();
				//残相殺可能時間が０になるパターン
				if(lateTime.getTime().greaterThan(restOffSetTime.valueAsMinutes())) {
					ltOfDaily.rePlaceLateTime(TimeWithCalculation.createTimeWithCalculation(lateTime.getTime().minusMinutes(restOffSetTime.valueAsMinutes()),
																							lateTime.getCalcTime()));
					restOffSetTime = new AttendanceTime(0);
					break;
				}
				//残相殺可能時間が０より大きいパターン
				else {
					restOffSetTime = restOffSetTime.minusMinutes(lateTime.getTime().valueAsMinutes());
					ltOfDaily.rePlaceLateTime(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0),
																							lateTime.getCalcTime()));
				}
			}
		}
		return restOffSetTime;
	}

	/**
	 * 大塚モード(欠勤控除)時の休憩未取得時間との相殺処理(早退時間Ver)
	 */
	private void minusEarlyLeaveTime(AttendanceTime restWithinUnUseBreakTime) {
		AttendanceTime restOffSetTime = restWithinUnUseBreakTime;
		if(!this.leaveEarlyTimeOfDaily.isEmpty()) {
			//勤務NOの昇順でソート
			this.leaveEarlyTimeOfDaily.sort((c1, c2) -> c1.getWorkNo().compareTo(c2.getWorkNo()));
			for(LeaveEarlyTimeOfDaily leOfDaily:this.leaveEarlyTimeOfDaily) {
				val leaveEarlyTime = leOfDaily.getLeaveEarlyTime();
				//残相殺可能時間が０になるパターン
				if(leaveEarlyTime.getTime().greaterThan(restOffSetTime.valueAsMinutes())) {
					leOfDaily.rePlaceLeaveEarlyTime(TimeWithCalculation.createTimeWithCalculation(leaveEarlyTime.getTime().minusMinutes(restOffSetTime.valueAsMinutes()),
																								  leaveEarlyTime.getCalcTime()));
					restOffSetTime = new AttendanceTime(0);
					break;
				}
				//残相殺可能時間が０より大きいパターン
				else {
					restOffSetTime = restOffSetTime.minusMinutes(leaveEarlyTime.getTime().valueAsMinutes());
					leOfDaily.rePlaceLeaveEarlyTime(TimeWithCalculation.createTimeWithCalculation(new AttendanceTime(0),
																								  leaveEarlyTime.getCalcTime()));
				}
			}
		}
	}

	/**
	 * 遅刻早退の合計時間を取得
	 * 大塚専用処理なので遅刻早退クラスに実装しない
	 * @return
	 */
	public TimeWithCalculation calcLateLeaveTotalTime() {
		
		AttendanceTime time = new AttendanceTime(0);
		AttendanceTime calcTime = new AttendanceTime(0);
		//日別実績の遅刻時間の取得（遅刻早退合計時間への追加）
		if(!this.lateTimeOfDaily.isEmpty()) {
			List<LateTimeOfDaily> list = this.lateTimeOfDaily.stream().filter(l -> l.getWorkNo().equals(new WorkNo(1))).collect(Collectors.toList());
			if(!list.isEmpty()) {
				LateTimeOfDaily lateTimeOfDaily = list.get(0);
				time = time.addMinutes(lateTimeOfDaily.getLateTime().getTime().valueAsMinutes());
				calcTime = calcTime.addMinutes(lateTimeOfDaily.getLateTime().getCalcTime().valueAsMinutes());
			}
		}
		//日別実績の早退時間の取得（遅刻早退合計時間への追加）
		if(!this.leaveEarlyTimeOfDaily.isEmpty()) {
			List<LeaveEarlyTimeOfDaily> list = this.leaveEarlyTimeOfDaily.stream().filter(l -> l.getWorkNo().equals(new WorkNo(1))).collect(Collectors.toList());
			if(!list.isEmpty()) {
				LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily = list.get(0);
				time = time.addMinutes(leaveEarlyTimeOfDaily.getLeaveEarlyTime().getTime().valueAsMinutes());
				calcTime = calcTime.addMinutes(leaveEarlyTimeOfDaily.getLeaveEarlyTime().getCalcTime().valueAsMinutes());
			}
		}		
		return TimeWithCalculation.createTimeWithCalculation(time, calcTime);
	}
	
	/**
	 * 欠勤控除時間の計算
	 * @return
	 */
	public TimeWithCalculation calcAbsenteeismDeductionTime(TimeWithCalculation lateLeaveTotalTime,AttendanceTime unBreakTime) {

		//時間、計算時間から休暇加算時間を減算
		AttendanceTime time = lateLeaveTotalTime.getTime().minusMinutes(this.vacationAddTime.valueAsMinutes()).minusMinutes(unBreakTime.valueAsMinutes());
		AttendanceTime calcTime = lateLeaveTotalTime.getCalcTime().minusMinutes(this.vacationAddTime.valueAsMinutes()).minusMinutes(unBreakTime.valueAsMinutes());
		
		//0:00以下なら0：00に補正
		if(time.valueAsMinutes()<0) {
			time = new AttendanceTime(0);
		}
		if(calcTime.valueAsMinutes()<0) {
			calcTime = new AttendanceTime(0);
		}
		
		return TimeWithCalculation.createTimeWithCalculation(time, calcTime);
	}
	
	/**
	 * 合計休暇加算時間の計算
	 * @return
	 */
	public static AttendanceTime calcTotalHolidayAddTime(ManageReGetClass recordClass,
												  VacationClass vacationClass,
												  WorkType workType,
												  WorkingConditionItem conditionItem,
												  Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo,
												  Optional<WorkTimeCode> workTimeCode,
												  List<LateTimeOfDaily> lateTime,
												  List<LeaveEarlyTimeOfDaily> leaveEarlyTime,
												  List<OutingTimeOfDaily> outingList
												  ) {
		
		//日単位の休暇加算時間の計算
//		val vacationAddTime =  vacationClass.calcVacationAddTime(nts.uk.ctx.at.shared.dom.PremiumAtr.RegularWork,
//																workType,
//																recordClass.getPersonalInfo().getWorkingSystem(),
//																workTimeCode,
//																conditionItem,
//																recordClass.getHolidayAddtionSet(),
//																recordClass.getHolidayCalcMethodSet(),
//																recordClass.getCalculatable()?Optional.of(recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc()):Optional.empty(),
//																predetermineTimeSetByPersonInfo);
		int dailyvacationAddTime = 0;
		if(recordClass.getWorkTimeSetting().isPresent()&&recordClass.getWorkTimeSetting().get().getWorkTimeDivision().getWorkTimeDailyAtr().isFlex()) {
			//フレックスの場合
			FlexWithinWorkTimeSheet changedFlexTimeSheet = (FlexWithinWorkTimeSheet)recordClass.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get();
			CalcFlexTime flexTime = changedFlexTimeSheet.calcFlexTime(recordClass.getHolidayCalcMethodSet(), 
																	  recordClass.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getCalAtr(),
																	  workType,
																	  new SettingOfFlexWork(new FlexCalcMethodOfHalfWork(new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.Half, FlexCalcMethod.Half),
																			   				new FlexCalcMethodOfEachPremiumHalfWork(FlexCalcMethod.Half, FlexCalcMethod.Half))),
																	  recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
																	  vacationClass,
																	  recordClass.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get().getTimeVacationAdditionRemainingTime().get(),
																	  StatutoryDivision.Nomal,
																	  workTimeCode,
																	  recordClass.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting(),
																	  recordClass.getAddSetting(),
																	  recordClass.getHolidayAddtionSet().get(),
																	  recordClass.getDailyUnit(),
																	  recordClass.getWorkTimezoneCommonSet(),
																	  recordClass.getIntegrationOfDaily().getCalAttr().getFlexExcessTime().getFlexOtTime().getUpLimitORtSet(),
																	  conditionItem,
																	  predetermineTimeSetByPersonInfo,
																	  recordClass.getCoreTimeSetting()
																	  );
			dailyvacationAddTime = flexTime.getVacationAddTime().valueAsMinutes();
		}else {
			
			//フレックス以外の場合
			WorkHour workHour = recordClass.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get().calcWorkTime(PremiumAtr.RegularWork,
																														 vacationClass,
																														 recordClass.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get().getTimeVacationAdditionRemainingTime().get(),
																														 workType,
																														 recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
																														 workTimeCode,
																														 recordClass.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting(),
																														 recordClass.getAddSetting(),
																														 recordClass.getHolidayAddtionSet().get(),
																														 recordClass.getHolidayCalcMethodSet(),
																														 recordClass.getDailyUnit(),
																														 recordClass.getWorkTimezoneCommonSet(),
																														 conditionItem,
																														 predetermineTimeSetByPersonInfo,
																														 recordClass.getCoreTimeSetting()
																														 ,HolidayAdditionAtr.HolidayAddition.convertFromCalcByActualTimeToHolidayAdditionAtr(recordClass.getAddSetting().getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getCalculateActualOperation()),
																														  NotUseAtr.USE
																														 );
			dailyvacationAddTime = workHour.getVacationAddTime().valueAsMinutes();
		}
			
		//遅刻休暇加算時間の計算
		int lateVacationAddTime = 0;
		for(LateTimeOfDaily lateTimeOfDaily:lateTime) {
			lateVacationAddTime = lateVacationAddTime + lateTimeOfDaily.calcVacationAddTime(recordClass.getHolidayAddtionSet());
		}
		//早退休暇加算時間の計算
		int leaveVacationAddTime = 0;
		for(LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily:leaveEarlyTime) {
			leaveVacationAddTime = leaveVacationAddTime + leaveEarlyTimeOfDaily.calcVacationAddTime(recordClass.getHolidayAddtionSet());
		}
		//外出休暇加算時間の計算
		int outingVacationAddTime = 0;
		for(OutingTimeOfDaily outingTimeOfDaily:outingList) {
			outingVacationAddTime = outingVacationAddTime + outingTimeOfDaily.calcVacationAddTime(recordClass.getHolidayAddtionSet());
		}
		AttendanceTime result = new AttendanceTime(dailyvacationAddTime
												   +lateVacationAddTime
												   +leaveVacationAddTime
												   +outingVacationAddTime);		
		return result;
	}


	public TotalWorkingTime SpecialHolidayCalculationForOotsuka(ManageReGetClass recordClass, VacationClass vacationClass, WorkType workType, Optional<WorkTimeDailyAtr> workTimeDailyAtr, Optional<SettingOfFlexWork> flexCalcMethod, BonusPayAutoCalcSet bonusPayAutoCalcSet, List<CompensatoryOccurrenceSetting> eachCompanyTimeSet, WorkingConditionItem conditionItem, Optional<PredetermineTimeSetForCalc> predetermineTimeSetByPersonInfo, DeductLeaveEarly leaveLateSet) {
		switch(recordClass.getPersonalInfo().getWorkingSystem()) {
			case FLEX_TIME_WORK:
				AttendanceTimeOfExistMinus flexTime = this.excessOfStatutoryTimeOfDaily.getOverTimeWork().get().getFlexTime().getFlexTime().getTime();
				if(flexTime.lessThan(0))
					flexTime = AttendanceTimeOfExistMinus.ZERO;
				AttendanceTimeOfExistMinus calcFlexTime = this.excessOfStatutoryTimeOfDaily.getOverTimeWork().get().getFlexTime().getFlexTime().getCalcTime();
				if(calcFlexTime.lessThan(0))
					calcFlexTime = AttendanceTimeOfExistMinus.ZERO;
				//set flexTime And calcFlexTime

//				AttendanceTime flexPreAppTime = new AttendanceTime(0);
////				ChildCareAttribute careAtr = ChildCareAttribute.CHILD_CARE;
//				if(recordClass.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().isPresent()
//					&& recordClass.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily() != null
//					&& recordClass.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime() != null){
//						if(recordClass.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily() != null) {
//							if(recordClass.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().isPresent()
//								&& recordClass.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime() != null) {
//									//事前フレックス
//									flexPreAppTime = recordClass.getIntegrationOfDaily().getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getOverTimeWork().get().getFlexTime().getBeforeApplicationTime();
//							}
//						}
//				}
//				
//				Optional<WorkTimeCode> workTimeCode = Optional.empty();
//				//日別実績の所定外時間
//				if(recordClass.getCalculatable() && recordClass.getCalculationRangeOfOneDay().getWorkInformationOfDaily().getRecordInfo().getWorkTimeCode() != null) {
//					workTimeCode = recordClass.getCalculationRangeOfOneDay().getWorkInformationOfDaily().getRecordInfo().getWorkTimeCode().v() == null
//																				?Optional.empty()
//																				:Optional.of(new WorkTimeCode(recordClass.getCalculationRangeOfOneDay().getWorkInformationOfDaily().getRecordInfo().getWorkTimeCode().v().toString()));
//				}
//				
//				WorkingConditionItem regularWorkCondition = conditionItem;//.changeWorkingSystemToRegular();
//
			
//				AttendanceTime actualWorkTime = WithinStatutoryTimeOfDaily.calcActualWorkTime(recordClass.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get(), 
//																   vacationClass, 
//																   workType, 
//																   recordClass.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLate(), 
//																   recordClass.getIntegrationOfDaily().getCalAttr().getLeaveEarlySetting().isLeaveEarly(), 
//																   recordClass.getPersonalInfo().getWorkingSystem(),
//																   recordClass.getWorkDeformedLaborAdditionSet(),
////																   recordClass.getWorkFlexAdditionSet(),
////																   recordClass.getWorkRegularAdditionSet(),
//										  							  new WorkFlexAdditionSet(recordClass.getWorkFlexAdditionSet().getCompanyId(),
//				  									  				  new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,recordClass.getWorkFlexAdditionSet().getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//				  									  										   new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, recordClass.getWorkFlexAdditionSet().getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//				  									  				  ),
//
//										  							  new WorkRegularAdditionSet(recordClass.getWorkRegularAdditionSet().getCompanyId(),
//				  									  					new HolidayCalcMethodSet(new PremiumHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME,recordClass.getWorkRegularAdditionSet().getVacationCalcMethodSet().getPremiumCalcMethodOfHoliday().getAdvanceSet()),
//				  									  											 new WorkTimeHolidayCalcMethod(CalcurationByActualTimeAtr.CALCULATION_BY_ACTUAL_TIME, recordClass.getWorkRegularAdditionSet().getVacationCalcMethodSet().getWorkTimeCalcMethodOfHoliday().getAdvancedSet()))
//				  									  					),
//																   recordClass.getHolidayAddtionSet().get(),
//																   recordClass.getHolidayCalcMethodSet(),
//																   CalcMethodOfNoWorkingDay.isCalculateFlexTime, 
//																   flexCalcMethod, 
//																   workTimeDailyAtr, 
//																   workTimeCode, 
//																   flexPreAppTime, 
//																   recordClass.getCoreTimeSetting(),
//																   recordClass.getCalculationRangeOfOneDay().getPredetermineTimeSetForCalc(),
//																   recordClass.getCalculationRangeOfOneDay().getTimeVacationAdditionRemainingTime(),
//																   recordClass.getDailyUnit(),
//																   recordClass.getWorkTimezoneCommonSet(),
//																   regularWorkCondition, 
//																   predetermineTimeSetByPersonInfo, 
//																   Optional.of(new DeductLeaveEarly(0, 1)));
				AttendanceTime actualWorkTime = new AttendanceTime(0);
				if(!recordClass.getPredSetForOOtsuka().isPresent())
					break;
				for(TimezoneUse timeZone : recordClass.getPredSetForOOtsuka().get().getTimeSheets()) {
					if(timeZone.isUsed())
					{
						for(WithinWorkTimeFrame copyItem: recordClass.getCalculationRangeOfOneDay().getWithinWorkingTimeSheet().get().getWithinWorkTimeFrame()) {
							TimeWithDayAttr start = copyItem.getTimeSheet().getStart();
							TimeWithDayAttr end = copyItem.getTimeSheet().getEnd();
							if(timeZone.getDuplicatedWith(copyItem.getTimeSheet().getTimeSpan()).isPresent()) {
								if(copyItem.getTimeSheet().getStart().lessThan(timeZone.getStart())) {
									start = timeZone.getStart();
								}
								if(copyItem.getTimeSheet().getEnd().greaterThan(timeZone.getEnd())) {
									end = timeZone.getEnd();
								}
							}
							else {
								end = start;
							}
							copyItem.replaceTimeSheet(new TimeSpanForDailyCalc(start, end));
							copyItem.replaceOwnDedTimeSheet();
							actualWorkTime = new AttendanceTime(actualWorkTime.v() + copyItem.calcTotalTime().valueAsMinutes());
						}
					}
					
				}
		
				
				this.withinStatutoryTimeOfDaily.setActualWorkTime(actualWorkTime);
				if(this.excessOfStatutoryTimeOfDaily.getOverTimeWork().isPresent()) {
					int ootsukaFlex = (flexTime.valueAsMinutes() - actualWorkTime.valueAsMinutes()) < 0 ? 0 : flexTime.valueAsMinutes() - actualWorkTime.valueAsMinutes();
					this.excessOfStatutoryTimeOfDaily.getOverTimeWork().get().getFlexTime().setOnlyFlexTime(new AttendanceTimeOfExistMinus(ootsukaFlex));
				}
				
				break;
			case REGULAR_WORK:
				//法定内残業とする時間を計算する
				AttendanceTime workActualTime = this.withinStatutoryTimeOfDaily.getActualWorkTime();
				AttendanceTime workTime = this.withinStatutoryTimeOfDaily.getWorkTime();
				/*workTime - workActualTime < 0*/
				AttendanceTime difference = workTime.minusMinutes(workActualTime.valueAsMinutes());
				if(workTime.lessThan(workActualTime))
					workTime = new AttendanceTime(0);
				this.withinStatutoryTimeOfDaily.setWorkTime(difference);
			
				
				AttendanceTime excessRow = this.totalCalcTime.minusMinutes(recordClass.getDailyUnit().getDailyTime().valueAsMinutes());
				//全残業枠時間＋
				if(this.excessOfStatutoryTimeOfDaily.getOverTimeWork().isPresent()) {
					final AttendanceTime withinRow = new AttendanceTime(this.excessOfStatutoryTimeOfDaily.getOverTimeWork().get().calcTotalFrameTime()
											   					  + this.excessOfStatutoryTimeOfDaily.getOverTimeWork().get().calcTransTotalFrameTime()
											   					  - excessRow.valueAsMinutes());
					//法定内残業とする時間を計算する
					this.excessOfStatutoryTimeOfDaily.getOverTimeWork().ifPresent(tc ->{
						tc.transWithinOverTimeForOOtsukaSpecialHoliday(recordClass.getOverTimeSheetSetting(),withinRow);
					});
				}
				break;
			default:
				break;
		}
		return this;
	}
	
	//大塚専用IW判定処理
	private static boolean decisionIWOOtsukaMode(WorkType workType, Optional<WorkTimeCode> workTimeCode,
			ManageReGetClass recordReget) {
		if(!workTimeCode.isPresent()) return false;
		if(!(workTimeCode.get().v().equals("100") || workTimeCode.get().v().equals("101")))
			return false;
		if(workType.getWorkTypeCode().v().equals("100")) 
			return false;
		
		switch(workType.getDailyWork().decisionNeedPredTime()) {
		case AFTERNOON:
			if(workType.getDailyWork().getAfternoon().isHolidayWork()) {
				return false;
			}
			//出勤or振出であろうという推測
			else {
				return true;
			}
		case FULL_TIME:
			if(workType.getDailyWork().getOneDay().isHolidayWork()) {
				return false;
			}
			//出勤or振出であろうという推測
			else {
				return true;
			}
		case MORNING:
			if(workType.getDailyWork().getMorning().isHolidayWork()) {
				return false;
			}
			//出勤or振出であろうという推測
			else {
				return true;
			}
		case HOLIDAY:
			return false;
		default:
			throw new RuntimeException("unkwon pred need workType in IW Decision");
		}
	}
	
}
