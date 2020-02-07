package nts.uk.ctx.at.request.dom.application.overtime.time36;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeYear;
import nts.arc.time.calendar.period.YearMonthPeriod;
/**
 * 36協定上限各月平均時間
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class Time36AgreeUpperLimitPerMonth {
	
	/*
	 * 期間
	 */
	private YearMonthPeriod period;
	
	/*
	 * 平均時間
	 */
	private AttendanceTimeMonth averageTime;
	
	/*
	 * 合計時間
	 */
	private AttendanceTimeYear totalTime;
	
	public Time36AgreeUpperLimitPerMonth(Integer periodStart, Integer periodEnd, Integer averageTime, Integer totalTime){
		this.period = new YearMonthPeriod(new YearMonth(periodStart), new YearMonth(periodEnd)); 
		this.averageTime = new AttendanceTimeMonth(averageTime);
		this.totalTime = new AttendanceTimeYear(totalTime);
	} 
	
	/*
	 * 期間月数
	 */
	public Integer getPeriodNumberOfMonths(){
		return 0;
	}
	
	/*
	 * 平均時間計算
	 */
	public AttendanceTimeMonth calculationAverageTime(){
		return null;
	} 
}
