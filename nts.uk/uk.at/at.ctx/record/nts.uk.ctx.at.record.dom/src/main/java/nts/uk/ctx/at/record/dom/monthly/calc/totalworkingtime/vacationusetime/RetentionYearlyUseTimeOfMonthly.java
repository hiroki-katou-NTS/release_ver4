package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries.RetentionYearlyUseTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 月別実績の積立年休使用時間
 * @author shuichi_ishida
 */
public class RetentionYearlyUseTimeOfMonthly {
	
	/** 使用時間 */
	@Getter
	private AttendanceTimeMonth useTime;
	/** 時系列ワーク */
	@Getter
	private Map<GeneralDate, RetentionYearlyUseTimeOfTimeSeries> timeSeriesWorks;

	/**
	 * コンストラクタ
	 */
	public RetentionYearlyUseTimeOfMonthly(){
		
		this.useTime = new AttendanceTimeMonth(0);
		this.timeSeriesWorks = new HashMap<>();
	}

	/**
	 * ファクトリー
	 * @param useTime 使用時間
	 * @return 月別実績の積立年休使用時間
	 */
	public static RetentionYearlyUseTimeOfMonthly of(
			AttendanceTimeMonth useTime){
		
		val domain = new RetentionYearlyUseTimeOfMonthly();
		domain.useTime = useTime;
		return domain;
	}
	
	/**
	 * 積立年休使用時間を確認する
	 * @param datePeriod 期間
	 * @param attendanceTimeOfDailys 日別実績の勤怠時間リスト
	 */
	public void confirm(DatePeriod datePeriod,
			Map<GeneralDate, AttendanceTimeOfDailyPerformance> attendanceTimeOfDailys){

		for (val attendanceTimeOfDaily : attendanceTimeOfDailys.values()) {
			val ymd = attendanceTimeOfDaily.getYmd();
			
			// 期間外はスキップする
			if (!datePeriod.contains(ymd)) continue;
			
			// 「日別実績の積立年休」を取得する
			val actualWorkingTimeOfDaily = attendanceTimeOfDaily.getActualWorkingTimeOfDaily();
			val totalWorkingTime = actualWorkingTimeOfDaily.getTotalWorkingTime();
			//*****（未）　ここから先のドメインがまだない
			//VacationOfDaily vacationOfDaily = totalWorkingTime.getVacation();
			//RetentionYearlyOfDaily retentionYearlyOfDaily = vacationOfDaily.getRetentionYearly();
			
			// 取得した使用時間を「月別実績の積立年休使用時間」に入れる
			//*****（未）　「日別実績の積立年休」クラスをnewして、値を入れて、それをset？
			val retentionYearlyUseTimeOfTimeSeries = RetentionYearlyUseTimeOfTimeSeries.of(ymd);
			this.timeSeriesWorks.putIfAbsent(ymd, retentionYearlyUseTimeOfTimeSeries);
		}
	}
	
	/**
	 * 積立年休使用時間を集計する
	 * @param datePeriod 期間
	 */
	public void aggregate(DatePeriod datePeriod){
		
		this.useTime = new AttendanceTimeMonth(0);
		
		for (val timeSeriesWork : this.timeSeriesWorks.values()){
			if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
			//RetentionYearlyOfDaily retentionYearly = timeSeriesWork.getRetentionYearly();
			//this.useTime.addMinutes(retentionYearly.getUseTime().valueAsMinutes());
		}
	}
}
