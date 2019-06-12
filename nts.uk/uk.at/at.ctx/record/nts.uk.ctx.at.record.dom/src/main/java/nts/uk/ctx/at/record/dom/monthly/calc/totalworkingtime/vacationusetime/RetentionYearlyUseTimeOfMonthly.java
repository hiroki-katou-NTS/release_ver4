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
@Getter
public class RetentionYearlyUseTimeOfMonthly implements Cloneable {
	
	/** 使用時間 */
	private AttendanceTimeMonth useTime;
	/** 時系列ワーク */
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

	@Override
	public RetentionYearlyUseTimeOfMonthly clone() {
		RetentionYearlyUseTimeOfMonthly cloned = new RetentionYearlyUseTimeOfMonthly();
		try {
			cloned.useTime = new AttendanceTimeMonth(this.useTime.v());
			// ※　Shallow Copy.
			cloned.timeSeriesWorks = this.timeSeriesWorks;
		}
		catch (Exception e){
			throw new RuntimeException("RetentionYearlyUseTimeOfMonthly clone error.");
		}
		return cloned;
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
			if (totalWorkingTime.getHolidayOfDaily() == null) return;
			val holidayOfDaily = totalWorkingTime.getHolidayOfDaily();
			if (holidayOfDaily.getYearlyReserved() == null) return;
			val yearlyReserved = holidayOfDaily.getYearlyReserved();
			
			// 取得した使用時間を「月別実績の積立年休使用時間」に入れる
			val retentionYearlyUseTime = RetentionYearlyUseTimeOfTimeSeries.of(ymd, yearlyReserved);
			this.timeSeriesWorks.putIfAbsent(ymd, retentionYearlyUseTime);
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
			this.addMinuteToUseTime(timeSeriesWork.getRetentionYearlyUseTime().getUseTime().v());
		}
	}
	
	/**
	 * 積立年休使用時間を求める
	 * @param datePeriod 期間
	 * @return 積立年休使用時間
	 */
	public AttendanceTimeMonth getTotalUseTime(DatePeriod datePeriod){
		
		AttendanceTimeMonth returnTime = new AttendanceTimeMonth(0);
		
		for (val timeSeriesWork : this.timeSeriesWorks.values()){
			if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
			returnTime = returnTime.addMinutes(timeSeriesWork.getRetentionYearlyUseTime().getUseTime().v());
		}
		return returnTime;
	}
	
	/**
	 * 使用時間に分を加算する
	 * @param minutes 分
	 */
	public void addMinuteToUseTime(int minutes){
		this.useTime = this.useTime.addMinutes(minutes);
	}
	
	/**
	 * 積立年休使用時間を取得する　（日指定）
	 * @param ymd 対象日
	 * @return 積立年休使用時間
	 */
	public AttendanceTimeMonth getUseTimeDaily(GeneralDate ymd){
		int result = 0;
		if (this.timeSeriesWorks.containsKey(ymd)) {
			result = this.timeSeriesWorks.get(ymd).getRetentionYearlyUseTime().getUseTime().v();
		}
		return new AttendanceTimeMonth(result);
	}
}
