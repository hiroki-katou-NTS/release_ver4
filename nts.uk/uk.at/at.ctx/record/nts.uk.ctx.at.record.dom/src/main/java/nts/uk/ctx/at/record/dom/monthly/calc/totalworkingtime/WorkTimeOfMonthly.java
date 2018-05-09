package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.hdwkandcompleave.HolidayWorkTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.overtime.OverTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries.WorkTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の就業時間
 * @author shuichi_ishida
 */
@Getter
public class WorkTimeOfMonthly {

	/** 就業時間 */
	@Setter
	private AttendanceTimeMonth workTime;
	/** 所定内割増時間 */
	@Setter
	private AttendanceTimeMonth withinPrescribedPremiumTime;
	/** 実働就業時間 */
	@Setter
	private AttendanceTimeMonth actualWorkTime;
	
	/** 時系列ワーク */
	private Map<GeneralDate, WorkTimeOfTimeSeries> timeSeriesWorks;
	
	/**
	 * コンストラクタ
	 */
	public WorkTimeOfMonthly(){
		
		this.workTime = new AttendanceTimeMonth(0);
		this.withinPrescribedPremiumTime = new AttendanceTimeMonth(0);
		this.actualWorkTime = new AttendanceTimeMonth(0);
		this.timeSeriesWorks = new HashMap<>();
	}

	/**
	 * ファクトリー
	 * @param workTime 就業時間
	 * @param withinPrescribedPremiumTime 所定内割増時間
	 * @param actualWorkTime 実働就業時間
	 * @return 月別実績の就業時間
	 */
	public static WorkTimeOfMonthly of(
			AttendanceTimeMonth workTime,
			AttendanceTimeMonth withinPrescribedPremiumTime,
			AttendanceTimeMonth actualWorkTime){
		
		val domain = new WorkTimeOfMonthly();
		domain.workTime = workTime;
		domain.withinPrescribedPremiumTime = withinPrescribedPremiumTime;
		domain.actualWorkTime = actualWorkTime;
		return domain;
	}

	/**
	 * 複写
	 * @param workTime 就業時間
	 * @param withinPrescribedPremiumTime 所定内割増時間
	 * @param actualWorkTime 実働就業時間
	 * @param timeSeriesWorks 時系列ワーク
	 * @return 月別実績の就業時間
	 */
	public static WorkTimeOfMonthly copyFrom(
			AttendanceTimeMonth workTime,
			AttendanceTimeMonth withinPrescribedPremiumTime,
			AttendanceTimeMonth actualWorkTime,
			Map<GeneralDate, WorkTimeOfTimeSeries> timeSeriesWorks){
		
		val domain = new WorkTimeOfMonthly();
		domain.workTime = new AttendanceTimeMonth(workTime.valueAsMinutes());
		domain.withinPrescribedPremiumTime = new AttendanceTimeMonth(withinPrescribedPremiumTime.valueAsMinutes());
		domain.actualWorkTime = new AttendanceTimeMonth(actualWorkTime.valueAsMinutes());
		domain.timeSeriesWorks = timeSeriesWorks;
		return domain;
	}
	
	/**
	 * 就業時間を確認する
	 * @param datePeriod 期間
	 * @param attendanceTimeOfDailyMap 日別実績の勤怠時間リスト
	 */
	public void confirm(DatePeriod datePeriod,
			Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyMap){
		
		for (val attendanceTimeOfDaily : attendanceTimeOfDailyMap.values()) {
			val ymd = attendanceTimeOfDaily.getYmd();
			
			// 期間外はスキップする
			if (!datePeriod.contains(ymd)) continue;
			
			// ドメインモデル「日別実績の所定内時間」を取得する
			val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
			val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
			WithinStatutoryTimeOfDaily withinPrescribedTimeOfDaily = totalWorkingTime.getWithinStatutoryTimeOfDaily();
			if (withinPrescribedTimeOfDaily == null){
				withinPrescribedTimeOfDaily = WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
						new AttendanceTime(0),
						new AttendanceTime(0),
						new AttendanceTime(0),
						new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))),
						new AttendanceTime(0));
			}
	
			// 取得した就業時間・所定内割増時間を確認する
			AttendanceTime workTime = new AttendanceTime(withinPrescribedTimeOfDaily.getWorkTime().v());
			val withinPrescribedPremiumTime = withinPrescribedTimeOfDaily.getWithinPrescribedPremiumTime();
			
			// ドメインモデル「日別実績の残業時間」を取得する
			val illegalTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
			if (illegalTimeOfDaily.getOverTimeWork().isPresent()){
				val overTimeOfDaily = illegalTimeOfDaily.getOverTimeWork().get();
				
				// 変形法定内残業を就業時間に加算
				workTime = workTime.addMinutes(overTimeOfDaily.getIrregularWithinPrescribedOverTimeWork().v());
			}
			
			// 「日別実績の総労働時間．休暇加算時間」を取得する
			val vacationAddTime = totalWorkingTime.getVacationAddTime();
	
			// 時系列ワークに追加
			val workTimeOfTimeSeries = WorkTimeOfTimeSeries.of(ymd,
					WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
							workTime,
							withinPrescribedTimeOfDaily.getActualWorkTime(),
							withinPrescribedPremiumTime,
							withinPrescribedTimeOfDaily.getWithinStatutoryMidNightTime(),
							withinPrescribedTimeOfDaily.getVacationAddTime()),
					vacationAddTime
					);
			this.timeSeriesWorks.putIfAbsent(ymd, workTimeOfTimeSeries);
		}
	}
	
	/**
	 * 時系列合計法定内時間を取得する
	 * @param datePeriod 期間
	 * @return 時系列合計法定内時間
	 */
	public AttendanceTimeMonth getTimeSeriesTotalLegalTime(DatePeriod datePeriod){
		
		AttendanceTimeMonth returnTime = new AttendanceTimeMonth(0);
		for (val timeSeriesWork : this.timeSeriesWorks.values()){
			if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
			returnTime = returnTime.addMinutes(timeSeriesWork.getLegalTime().getWorkTime().v());
		}
		return returnTime;
	}
	
	/**
	 * 就業時間を集計する
	 * @param datePeriod 期間
	 * @param workingSystem 労働制
	 * @param actualWorkingTime 実働時間
	 * @param flexTime フレックス時間
	 * @param overTime 残業時間
	 * @param holidayWorkTime 休出時間
	 */
	public void aggregate(
			DatePeriod datePeriod,
			WorkingSystem workingSystem,
			RegularAndIrregularTimeOfMonthly actualWorkingTime,
			FlexTimeOfMonthly flexTime,
			OverTimeOfMonthly overTime,
			HolidayWorkTimeOfMonthly holidayWorkTime){

		// 就業時間の合計処理
		this.totalizeWorkTime(datePeriod);
		
		// 就業時間に法定内残業時間を加算する
		this.workTime = this.workTime.addMinutes(overTime.getLegalOverTime(datePeriod).v());
		
		// 就業時間に法定内休出時間を加算する
		this.workTime = this.workTime.addMinutes(holidayWorkTime.getLegalHolidayWorkTime(datePeriod).v());
		
		// 就業時間に「加算した休暇使用時間」を加算する
		this.workTime = this.workTime.addMinutes(
				actualWorkingTime.getAddedVacationUseTime().getAddTimePerMonth().v());
		this.workTime = this.workTime.addMinutes(
				flexTime.getAddedVacationUseTime().getAddTimePerMonth().v());
		
		// 就業時間から週割増合計時間・月割増合計時間を引く
		this.workTime = this.workTime.minusMinutes(actualWorkingTime.getWeeklyTotalPremiumTime().v());
		this.workTime = this.workTime.minusMinutes(actualWorkingTime.getMonthlyTotalPremiumTime().v());
	}
	
	/**
	 * 就業時間の合計処理
	 * @param datePeriod 期間
	 */
	public void totalizeWorkTime(DatePeriod datePeriod){
		
		this.workTime = new AttendanceTimeMonth(0);
		this.withinPrescribedPremiumTime = new AttendanceTimeMonth(0);
		this.actualWorkTime = new AttendanceTimeMonth(0);
		for (val timeSeriesWork : this.timeSeriesWorks.values()){
			if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
			val legalTime = timeSeriesWork.getLegalTime();
			this.workTime = this.workTime.addMinutes(legalTime.getWorkTime().v());
			this.withinPrescribedPremiumTime = this.withinPrescribedPremiumTime.addMinutes(
					legalTime.getWithinPrescribedPremiumTime().v());
			this.actualWorkTime = this.actualWorkTime.addMinutes(
					legalTime.getActualWorkTime().v());
		}
	}
	
	/**
	 * 総労働対象時間の取得
	 * @return 総労働対象時間
	 */
	public AttendanceTimeMonth getTotalWorkingTargetTime(){
		
		return new AttendanceTimeMonth(this.workTime.v() + this.withinPrescribedPremiumTime.v());
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(WorkTimeOfMonthly target){
		
		this.workTime = this.workTime.addMinutes(target.workTime.v());
		this.withinPrescribedPremiumTime = this.withinPrescribedPremiumTime.addMinutes(
				target.withinPrescribedPremiumTime.v());
		this.actualWorkTime = this.actualWorkTime.addMinutes(target.actualWorkTime.v());
	}
}
