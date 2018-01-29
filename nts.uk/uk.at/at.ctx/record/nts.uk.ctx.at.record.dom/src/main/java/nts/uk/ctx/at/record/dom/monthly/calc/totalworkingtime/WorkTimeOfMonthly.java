package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.hdwkandcompleave.HolidayWorkTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.overtime.OverTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries.WorkTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.employment.statutory.worktime.employment.WorkingSystem;
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
	private AttendanceTimeMonth withinPrescribedPremiumTime;
	
	/** 時系列ワーク */
	private List<WorkTimeOfTimeSeries> timeSeriesWorks;
	
	/**
	 * コンストラクタ
	 */
	public WorkTimeOfMonthly(){
		
		this.workTime = new AttendanceTimeMonth(0);
		this.withinPrescribedPremiumTime = new AttendanceTimeMonth(0);
		this.timeSeriesWorks = new ArrayList<>();
	}

	/**
	 * ファクトリー
	 * @param workTime 就業時間
	 * @param withinPrescribedPremiumTime 所定内割増時間
	 * @return 月別実績の就業時間
	 */
	public static WorkTimeOfMonthly of(
			AttendanceTimeMonth workTime,
			AttendanceTimeMonth withinPrescribedPremiumTime){
		
		val domain = new WorkTimeOfMonthly();
		domain.workTime = workTime;
		domain.withinPrescribedPremiumTime = withinPrescribedPremiumTime;
		return domain;
	}
	
	/**
	 * 就業時間を確認する
	 * @param datePeriod 期間
	 * @param attendanceTimeOfDailys リスト：日別実績の勤怠時間
	 */
	public void confirm(DatePeriod datePeriod,
			List<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys){
		
		for (val attendanceTimeOfDaily : attendanceTimeOfDailys) {
			
			// 期間外はスキップする
			if (!datePeriod.contains(attendanceTimeOfDaily.getYmd())) continue;
			
			// ドメインモデル「日別実績の所定内時間」を取得する
			val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
			val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
			val withinPrescribedTimeOfDaily = totalWorkingTime.getWithinStatutoryTimeOfDaily();
	
			// 取得した就業時間・所定内割増時間を確認する
			val workTime = new AttendanceTime(withinPrescribedTimeOfDaily.getWorkTime().v());
			val withinPrescribedPremiumTime =
					new AttendanceTime(withinPrescribedTimeOfDaily.getWithinPrescribedPremiumTime().v());
			
			// ドメインモデル「日別実績の残業時間」を取得する
			val illegalTimeOfDaily = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
			if (illegalTimeOfDaily.getOverTimeWork().isPresent()){
				val overTimeOfDaily = illegalTimeOfDaily.getOverTimeWork().get();
				
				// 変形法定内残業を就業時間に加算
				workTime.addMinutes(overTimeOfDaily.getIrregularWithinPrescribedOverTimeWork().valueAsMinutes());
			}
	
			// 時系列ワークに追加
			this.timeSeriesWorks.add(WorkTimeOfTimeSeries.of(
					attendanceTimeOfDaily.getYmd(),
					WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
							workTime,
							withinPrescribedTimeOfDaily.getWorkTimeIncludeVacationTime(),
							withinPrescribedPremiumTime,
							withinPrescribedTimeOfDaily.getWithinStatutoryMidNightTime(),
							withinPrescribedTimeOfDaily.getVacationAddTime())
					));
		}
	}
	
	/**
	 * 時系列合計法定内時間を取得する
	 * @param datePeriod 期間
	 * @return 時系列合計法定内時間
	 */
	public AttendanceTimeMonth getTimeSeriesTotalLegalTime(DatePeriod datePeriod){
		
		AttendanceTimeMonth returnTime = new AttendanceTimeMonth(0);
		for (val timeSeriesWork : this.timeSeriesWorks){
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

		// 就業時間を集計する
		this.workTime = new AttendanceTimeMonth(0);
		this.withinPrescribedPremiumTime = new AttendanceTimeMonth(0);
		for (val timeSeriesWork : this.timeSeriesWorks){
			if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
			val legalTime = timeSeriesWork.getLegalTime();
			this.workTime = this.workTime.addMinutes(legalTime.getWorkTime().v());
			this.withinPrescribedPremiumTime = this.withinPrescribedPremiumTime.addMinutes(
					legalTime.getWithinPrescribedPremiumTime().v());
		}
		
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
}
