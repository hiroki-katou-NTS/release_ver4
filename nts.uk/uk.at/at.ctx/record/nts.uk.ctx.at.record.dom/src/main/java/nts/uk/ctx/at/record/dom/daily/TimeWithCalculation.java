package nts.uk.ctx.at.record.dom.daily;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * 計算付き時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class TimeWithCalculation {
	@Setter
	private AttendanceTime time;
	private AttendanceTime calcTime;
	
	private TimeWithCalculation(AttendanceTime time,AttendanceTime calcTime) {
		this.time = time;
		this.calcTime = calcTime;
	}
	
	/**
	 * 時間、計算時間が同じ計算付き時間帯を作成する
	 * @return
	 */
	public static TimeWithCalculation sameTime(AttendanceTime time) {
		return new TimeWithCalculation(time,time);
	}
	
	
	/**
	 * 指定された時間で計算付き時間を作成する
	 * @return
	 */
	public static TimeWithCalculation createTimeWithCalculation(AttendanceTime time,AttendanceTime calcTime) {
		return new TimeWithCalculation(time,calcTime);
		
	}
	
	
}
