package nts.uk.ctx.at.record.dom.daily;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.AutoCalOverTimeAttr;
import nts.uk.ctx.at.record.dom.daily.calcset.CalcMethodOfNoWorkingDay;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayMidnightWork;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkTimeOfDaily;
import nts.uk.ctx.at.record.dom.daily.overtimework.FlexTime;
import nts.uk.ctx.at.record.dom.daily.overtimework.OverTimeOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.DeductionTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.VacationClass;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.withinstatutory.WithinWorkTimeSheet;
import nts.uk.ctx.at.record.dom.raborstandardact.flex.SettingOfFlexWork;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
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
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.StaturoryAtrOfHolidayWork;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.waytowork.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneOtherSubHolTimeSet;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 日別実績の所定外時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class ExcessOfStatutoryTimeOfDaily {
	@Setter
	private ExcessOfStatutoryMidNightTime excessOfStatutoryMidNightTime;
	private Optional<OverTimeOfDaily> overTimeWork;
	private Optional<HolidayWorkTimeOfDaily> workHolidayTime;
	
	
	/**
	 * Constructor
	 * @param excessOfStatutoryMidNightTime
	 * @param overTimeWork
	 * @param workHolidayTime
	 */
	public ExcessOfStatutoryTimeOfDaily(
			ExcessOfStatutoryMidNightTime excessOfStatutoryMidNightTime,
			Optional<OverTimeOfDaily> overTimeWork,
			Optional<HolidayWorkTimeOfDaily> workHolidayTime) {
		super();
		this.excessOfStatutoryMidNightTime = excessOfStatutoryMidNightTime;
		this.overTimeWork = overTimeWork;
		this.workHolidayTime = workHolidayTime;
	}
	
	/**
	 * 各時間の計算を指示するクラス
	 * @return
	 */
	public static ExcessOfStatutoryTimeOfDaily calculationExcessTime(CalculationRangeOfOneDay oneDay,AutoCalOvertimeSetting overTimeAutoCalcSet,AutoCalSetting holidayAutoCalcSetting,
																	 CalcMethodOfNoWorkingDay calcMethod,HolidayCalcMethodSet holidayCalcMethodSet,AutoCalOverTimeAttr autoCalcAtr,WorkType workType,
																	 Optional<SettingOfFlexWork> flexCalcMethod,PredetermineTimeSetForCalc predetermineTimeSet,
			   														 VacationClass vacationClass,TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
			   														 StatutoryDivision statutoryDivision,Optional<WorkTimeCode> siftCode,
			   														 Optional<PersonalLaborCondition> personalCondition, LateTimeSheet lateTimeSheet,LeaveEarlyTimeSheet leaveEarlyTimeSheet,LateTimeOfDaily lateTimeOfDaily,
			   														 LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
			   														 boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
			   														 WorkingSystem workingSystem,AddSettingOfIrregularWork addSettingOfIrregularWork,AddSettingOfFlexWork addSettingOfFlexWork,AddSettingOfRegularWork addSettingOfRegularWork,
			   														 VacationAddTimeSet vacationAddTimeSet,
			   														 WorkTimeDailyAtr workTimeDailyAtr,
			   														 List<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
			   														 List<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		//残業時間
		val overTime = calculationOverTime(oneDay,overTimeAutoCalcSet,calcMethod,holidayCalcMethodSet,autoCalcAtr,workType,flexCalcMethod,
										   predetermineTimeSet,vacationClass,timevacationUseTimeOfDaily,
				   						   statutoryDivision,siftCode,
				   						   personalCondition, lateTimeSheet,leaveEarlyTimeSheet,lateTimeOfDaily,
				   						   leaveEarlyTimeOfDaily,late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
				   						   leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
				   						   workingSystem,addSettingOfIrregularWork,addSettingOfFlexWork,addSettingOfRegularWork,
				   						   vacationAddTimeSet,workTimeDailyAtr,
										   eachWorkTimeSet.stream().filter(tc -> tc.getOriginAtr().isOverTime()).findFirst(),
										   eachCompanyTimeSet.stream().filter(tc -> tc.getOccurrenceType().isOverTime()).findFirst());
		//休出時間
		val workHolidayTime = calculationHolidayTime(oneDay,holidayAutoCalcSetting,workType,
													 eachWorkTimeSet.stream().filter(tc -> !tc.getOriginAtr().isOverTime()).findFirst(),
													 eachCompanyTimeSet.stream().filter(tc -> !tc.getOccurrenceType().isOverTime()).findFirst());
		//所定外深夜
		val excessOfStatutoryMidNightTime = ExcessOfStatutoryMidNightTime.calcExcessTime(overTime,workHolidayTime);//.calcExcessMidNightTime(oneDay);//new ExcessOfStatutoryMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0));
		
		return new ExcessOfStatutoryTimeOfDaily(excessOfStatutoryMidNightTime, Optional.of(overTime), Optional.of(workHolidayTime));
	}
	


	/**
	 * 残業時間の計算
	 * @param oneDay 
	 */
	private static OverTimeOfDaily calculationOverTime(CalculationRangeOfOneDay oneDay,AutoCalOvertimeSetting overTimeAutoCalcSet,
													   CalcMethodOfNoWorkingDay calcMethod,HolidayCalcMethodSet holidayCalcMethodSet,AutoCalOverTimeAttr autoCalcAtr,WorkType workType,
													   Optional<SettingOfFlexWork> flexCalcMethod,PredetermineTimeSetForCalc predetermineTimeSet,
													   VacationClass vacationClass,TimevacationUseTimeOfDaily timevacationUseTimeOfDaily,
													   StatutoryDivision statutoryDivision,Optional<WorkTimeCode> siftCode,
													   Optional<PersonalLaborCondition> personalCondition, LateTimeSheet lateTimeSheet,LeaveEarlyTimeSheet leaveEarlyTimeSheet,LateTimeOfDaily lateTimeOfDaily,
													   LeaveEarlyTimeOfDaily leaveEarlyTimeOfDaily,boolean late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
													   boolean leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
													   WorkingSystem workingSystem,AddSettingOfIrregularWork addSettingOfIrregularWork,AddSettingOfFlexWork addSettingOfFlexWork,AddSettingOfRegularWork addSettingOfRegularWork,
													   VacationAddTimeSet vacationAddTimeSet,WorkTimeDailyAtr workTimeDailyAtr,
													   Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
													   Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		if(oneDay.getOutsideWorkTimeSheet().isPresent()) {
			if(oneDay.getOutsideWorkTimeSheet().get().getOverTimeWorkSheet().isPresent()) {
				return OverTimeOfDaily.calculationTime(oneDay.getOutsideWorkTimeSheet().get().getOverTimeWorkSheet().get(), overTimeAutoCalcSet,
													   oneDay.getWithinWorkingTimeSheet().get(),calcMethod,holidayCalcMethodSet,autoCalcAtr,workType,flexCalcMethod,predetermineTimeSet,
													   oneDay.getTemporaryDeductionTimeSheet(),vacationClass,timevacationUseTimeOfDaily,
													   statutoryDivision,siftCode,
													   personalCondition, lateTimeSheet,leaveEarlyTimeSheet,lateTimeOfDaily,
													   leaveEarlyTimeOfDaily,late,  //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
													   leaveEarly,  //日別実績の計算区分.遅刻早退の自動計算設定.早退
													   workingSystem,addSettingOfIrregularWork,addSettingOfFlexWork,addSettingOfRegularWork,
													   vacationAddTimeSet,workTimeDailyAtr,
													   eachWorkTimeSet,
													   eachCompanyTimeSet);
			}
		}
		List<OverTimeFrameTime> calcOverTimeWorkTimeList = new ArrayList<>();
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(1), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(2), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(3), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(4), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(5), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(6), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(7), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(8), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(9), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		calcOverTimeWorkTimeList.add(new OverTimeFrameTime(new OverTimeFrameNo(10), TimeWithCalculation.sameTime(new AttendanceTime(0)),TimeWithCalculation.sameTime(new AttendanceTime(0)),new AttendanceTime(0),new AttendanceTime(0)));
		return new OverTimeOfDaily(Collections.emptyList(),
								   calcOverTimeWorkTimeList,
								   Finally.of(new ExcessOverTimeWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)))),
								   new AttendanceTime(0),
								   new FlexTime(TimeWithCalculationMinusExist.sameTime(new AttendanceTimeOfExistMinus(0)), new AttendanceTime(0)),
								   new AttendanceTime(0));
	}
	
	/**
	 * 休出時間の計算
	 * @return
	 */
	private static HolidayWorkTimeOfDaily calculationHolidayTime(CalculationRangeOfOneDay oneDay,AutoCalSetting holidayAutoCalcSetting,
			 													 WorkType workType,
			 													Optional<WorkTimezoneOtherSubHolTimeSet> eachWorkTimeSet,
			 													 Optional<CompensatoryOccurrenceSetting> eachCompanyTimeSet) {
		if(oneDay.getOutsideWorkTimeSheet().isPresent()) {
			if(oneDay.getOutsideWorkTimeSheet().get().getHolidayWorkTimeSheet().isPresent()) {
				return HolidayWorkTimeOfDaily.calculationTime(oneDay.getOutsideWorkTimeSheet().get().getHolidayWorkTimeSheet().get(), 
															  holidayAutoCalcSetting,
															  workType,
															  eachWorkTimeSet,
															  eachCompanyTimeSet);
			}
		}
		List<HolidayWorkFrameTime> calcHolidayTimeWorkTimeList = new ArrayList<>();
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(1),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(2),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(3),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(4),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(5),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(6),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(7),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(8),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(9),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		calcHolidayTimeWorkTimeList.add(new HolidayWorkFrameTime(new HolidayWorkFrameNo(10),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),Finally.of(new AttendanceTime(0))));
		
		List<HolidayWorkMidNightTime> addList = new ArrayList<>(); 
		addList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)), StaturoryAtrOfHolidayWork.WithinPrescribedHolidayWork));
		addList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)), StaturoryAtrOfHolidayWork.PublicHolidayWork));
		addList.add(new HolidayWorkMidNightTime(TimeWithCalculation.sameTime(new AttendanceTime(0)), StaturoryAtrOfHolidayWork.ExcessOfStatutoryHolidayWork));
		return new HolidayWorkTimeOfDaily(Collections.emptyList(),
										  calcHolidayTimeWorkTimeList,
				   						  Finally.of(new HolidayMidnightWork(addList)),
				   						  new AttendanceTime(0));
	}
}
