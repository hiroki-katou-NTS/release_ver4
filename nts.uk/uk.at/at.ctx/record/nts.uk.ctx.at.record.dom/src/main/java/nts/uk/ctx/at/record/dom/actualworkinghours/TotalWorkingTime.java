package nts.uk.ctx.at.record.dom.actualworkinghours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.temporarytime.TemporaryTimeOfDaily;
import nts.uk.ctx.at.record.dom.bonuspay.autocalc.BonusPayAutoCalcSet;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDaily;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.AutoCalOverTimeAttr;
import nts.uk.ctx.at.record.dom.daily.DeductionTotalTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.ExcessOfStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LateTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.LeaveEarlyTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeGoOutTimes;
import nts.uk.ctx.at.record.dom.daily.breaktimegoout.BreakTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.daily.latetime.IntervalExemptionTime;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.record.dom.raisesalarytime.RaiseSalaryTimeOfDailyPerfor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.shorttimework.enums.ChildCareAttribute;
import nts.uk.ctx.at.record.dom.stamp.GoOutReason;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfIrregularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.AddSettingOfRegularWork;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.StatutoryDivision;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.CompensatoryOccurrenceSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.VacationAddTimeSet;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.RaisingSalaryCalcAtr;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

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
	
	//日別実績の残業時間
	//private OverTimeOfDaily overTimeWorkOfDaily;
	
	//日別実績の休出時間
	//private HolidayWorkTimeOfDaily holidayWorkTimeOfDaily;
	
	//日別実績の法定内時間
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
	private List<OutingTimeOfDaily> outingTimeOfDailyPerformance = Collections.emptyList(); 
		
	//加給時間
	private RaiseSalaryTimeOfDailyPerfor raiseSalaryTimeOfDailyPerfor;
	
	//勤務回数
	private WorkTimes workTimes;
	
	/*日別実績の臨時時間*/
	private TemporaryTimeOfDaily temporaryTime;
	
	/*短時間勤務時間*/
	private ShortWorkTimeOfDaily shotrTimeOfDaily;
	
	
	/**
	 * 
	 * @param withinStatutory
	 * @param breakTime
	 * @return
	 */
	private TotalWorkingTime(WithinStatutoryTimeOfDaily withinStatutory,BreakTimeOfDaily breakTime) {
		this.withinStatutoryTimeOfDaily = withinStatutory;
		this.breakTimeOfDaily = breakTime;
	}
	
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
			TemporaryTimeOfDaily temporaryTime, ShortWorkTimeOfDaily shotrTime) {
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
	}
	
	/**
	 * 日別実績の総労働時間の計算
	 * @return 
	 */
	public static TotalWorkingTime calcAllDailyRecord(CalculationRangeOfOneDay oneDay,AutoCalOvertimeSetting overTimeAutoCalcSet,AutoCalSetting holidayAutoCalcSetting,
			   Optional<PersonalLaborCondition> personalCondition,
			   VacationClass vacationClass,
			   WorkType workType,
			   LateTimeSheet lateTimeSheet,
			   LeaveEarlyTimeSheet leaveEarlyTimeSheet,
			   LateTimeOfDaily lateTimeOfDaily,
			   LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,
			   boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
			   WorkingSystem workingSystem,
			   AddSettingOfIrregularWork addSettingOfIrregularWork,
			   AddSettingOfFlexWork addSettingOfFlexWork,
			   AddSettingOfRegularWork addSettingOfRegularWork,
			   VacationAddTimeSet vacationAddTimeSet,
			   AutoCalOverTimeAttr overTimeAutoCalcAtr,
			   WorkTimeDailyAtr workTimeDailyAtr,
			   Optional<SettingOfFlexWork> flexCalcMethod,
			   HolidayCalcMethodSet holidayCalcMethodSet,
			   RaisingSalaryCalcAtr raisingAutoCalcSet,
			   BonusPayAutoCalcSet bonusPayAutoCalcSet,
			   CalAttrOfDailyPerformance calcAtrOfDaily,
			   List<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
			   List<CompensatoryOccurrenceSetting> eachCompanyTimeSet
			   ) {
		

		/*日別実績の法定内時間(就業時間)*/
		val withinStatutoryTimeOfDaily = WithinStatutoryTimeOfDaily.calcStatutoryTime(oneDay,
				   																      personalCondition,
				   																      vacationClass,
				   																      workType,
				   																      lateTimeSheet,
				   																      leaveEarlyTimeSheet,
				   																      lateTimeOfDaily,
				   																      leaveEarlyTimeOfDaily,
				   																      late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
				   																      leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
				   																      workingSystem,
				   																      addSettingOfIrregularWork,
				   																      addSettingOfFlexWork,
				   																      addSettingOfRegularWork,
				   																      vacationAddTimeSet,
				   																      AutoCalAtrOvertime.CALCULATEMBOSS
				   																      );
		Optional<WorkTimeCode> workTimeCode = Optional.empty();
		//日別実績の所定外時間
		if(oneDay.getWorkInformationOfDaily().getRecordWorkInformation().getWorkTimeCode() != null) {
			workTimeCode = oneDay.getWorkInformationOfDaily().getRecordWorkInformation().getWorkTimeCode().v() == null
																		?Optional.empty()
																		:Optional.of(new WorkTimeCode(oneDay.getWorkInformationOfDaily().getRecordWorkInformation().getWorkTimeCode().v().toString()));
		}
		
		ExcessOfStatutoryTimeOfDaily excesstime =ExcessOfStatutoryTimeOfDaily.calculationExcessTime(oneDay, overTimeAutoCalcSet,holidayAutoCalcSetting,
																									CalcMethodOfNoWorkingDay.isCalculateFlexTime,
																									holidayCalcMethodSet,
																									overTimeAutoCalcAtr,
																									workType,flexCalcMethod,oneDay.getPredetermineTimeSetForCalc()
																									,vacationClass,oneDay.getTimeVacationAdditionRemainingTime().get(),
																									StatutoryDivision.Nomal,
																									workTimeCode,
																									personalCondition, lateTimeSheet,leaveEarlyTimeSheet,lateTimeOfDaily,
																									leaveEarlyTimeOfDaily,late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
																									leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
																									workingSystem,addSettingOfIrregularWork,addSettingOfFlexWork,addSettingOfRegularWork,
																									vacationAddTimeSet,workTimeDailyAtr,
																									eachWorkTimeSet,
																									eachCompanyTimeSet);
		int overWorkTime = excesstime.getOverTimeWork().isPresent()?excesstime.getOverTimeWork().get().calcTotalFrameTime():0;
		overWorkTime += excesstime.getOverTimeWork().isPresent()?excesstime.getOverTimeWork().get().calcTransTotalFrameTime():0;
		int holidayWorkTime = excesstime.getWorkHolidayTime().isPresent()?excesstime.getWorkHolidayTime().get().calcTotalFrameTime():0;
		holidayWorkTime += excesstime.getWorkHolidayTime().isPresent()?excesstime.getWorkHolidayTime().get().calcTransTotalFrameTime():0;
		//2018.02.14　一時的対応 byホシナ ↓
		//日別実績の遅刻時間
		List<LateTimeOfDaily> lateTime = new ArrayList<>();
		lateTime.add(new LateTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)),
										 TimeWithCalculation.sameTime(new AttendanceTime(0)),
										 new WorkNo(1),
										 new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
										 new IntervalExemptionTime(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0))
										 ));
		lateTime.add(new LateTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)),
										TimeWithCalculation.sameTime(new AttendanceTime(0)),
										new WorkNo(2),
										new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
										new IntervalExemptionTime(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0))
				 						));
		//日別実績の早退時間
		List<LeaveEarlyTimeOfDaily> earlyTime = new ArrayList<>();
		earlyTime.add(new LeaveEarlyTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)),
									    TimeWithCalculation.sameTime(new AttendanceTime(0)),
									    new WorkNo(1),
									    new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
									    new IntervalExemptionTime(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0))
										));
		earlyTime.add(new LeaveEarlyTimeOfDaily(TimeWithCalculation.sameTime(new AttendanceTime(0)),
										  TimeWithCalculation.sameTime(new AttendanceTime(0)),
										  new WorkNo(2),
										  new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
										  new IntervalExemptionTime(new AttendanceTime(0), new AttendanceTime(0), new AttendanceTime(0))
					));
		//2018.02.14　一時的対応 byホシナ ↑
		//日別実績の休憩時間
		val breakTime = BreakTimeOfDaily.calcTotalBreakTime(oneDay);

		//日別実績の外出時間
		val outingList = new ArrayList<OutingTimeOfDaily>();
		val outingTime = OutingTimeOfDaily.calcOutingTime(oneDay);
		outingList.add(outingTime);
		
		val shotrTime = new ShortWorkTimeOfDaily(new WorkTimes(1),
											 DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)),
													 			   TimeWithCalculation.sameTime(new AttendanceTime(0)),
													 			   TimeWithCalculation.sameTime(new AttendanceTime(0))),
											 DeductionTotalTime.of(TimeWithCalculation.sameTime(new AttendanceTime(0)),
										 			   			   TimeWithCalculation.sameTime(new AttendanceTime(0)),
										 			   			   TimeWithCalculation.sameTime(new AttendanceTime(0))),
											 ChildCareAttribute.CARE
											);
		//加給時間
		val raiseTime = RaiseSalaryTimeOfDailyPerfor.calcBonusPayTime(oneDay, raisingAutoCalcSet, bonusPayAutoCalcSet, calcAtrOfDaily);
		//勤務回数
		val workTimes = new WorkTimes(1);
		/*日別実績の臨時時間*/
		val tempTime = new TemporaryTimeOfDaily(Collections.emptyList());
		
		//総労働時間
		val totalWorkTime = new AttendanceTime(withinStatutoryTimeOfDaily.getWorkTime().valueAsMinutes()
						  					   + withinStatutoryTimeOfDaily.getWithinPrescribedPremiumTime().valueAsMinutes() 
						  					   + overWorkTime
						  					   + holidayWorkTime
						  					   + tempTime.totalTemporaryFrameTime());
		
		//総計算時間
		val totalCalcTime = new AttendanceTime(0);
		//実働時間
		val actualTime = new AttendanceTime(0);
		

		
		return new TotalWorkingTime(totalWorkTime,
									totalCalcTime,
									actualTime,
									withinStatutoryTimeOfDaily,
									excesstime,
									Collections.emptyList(),
									Collections.emptyList(),
									breakTime,
									outingList,
									raiseTime,
									workTimes,
									tempTime,
									shotrTime);
	}

	public Optional<LateTimeOfDaily> getLateTimeNo(int no){
		return this.lateTimeOfDaily.stream().filter(l -> l.getWorkNo().v() == no).findFirst();
	}

	public Optional<LeaveEarlyTimeOfDaily> getLeaveEarlyTimeNo(int no){
		return this.leaveEarlyTimeOfDaily.stream().filter(l -> l.getWorkNo().v() == no).findFirst();
	}

	public Optional<OutingTimeOfDaily> getOutingTimeByReason(GoOutReason reason){
		return getOutingTimeByReason(reason.value);
	}

	public Optional<OutingTimeOfDaily> getOutingTimeByReason(int reason){
		return this.outingTimeOfDailyPerformance.stream().filter(o -> o.getReason().value == reason).findFirst();
	}
}
