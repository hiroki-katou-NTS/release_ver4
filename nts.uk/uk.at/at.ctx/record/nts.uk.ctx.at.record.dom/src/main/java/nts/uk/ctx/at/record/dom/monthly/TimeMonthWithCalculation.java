package nts.uk.ctx.at.record.dom.monthly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

/**
 * 計算付き月間時間
 * @author shuichi_ishida
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeMonthWithCalculation {

	/** 時間 */
	private AttendanceTimeMonth time;
	/** 計算期間 */
	private AttendanceTimeMonth calcTime;

	/**
	 * 時間と計算時間を同じ時間で作成
	 * @param minutes 時間（分）
	 * @return 計算付き月間時間
	 */
	public static TimeMonthWithCalculation ofSameTime(int minutes){
		return new TimeMonthWithCalculation(
				new AttendanceTimeMonth(minutes),
				new AttendanceTimeMonth(minutes)
			);
	}

	/**
	 * 時間と計算時間に同じ時間を加算する
	 * @param minutes 加算する時間（分）
	 * @return 計算付き月間時間
	 */
	public TimeMonthWithCalculation addSameTime(int minutes){
		return new TimeMonthWithCalculation(
				this.time.addMinutes(minutes),
				this.calcTime.addMinutes(minutes)
			);
	}
	
	/**
	 * 分を加算する
	 * @param time 時間（分）
	 * @param calcTime 計算時間（分）
	 * @return 計算付き月間時間
	 */
	public TimeMonthWithCalculation addMinutes(int time, int calcTime){
		return new TimeMonthWithCalculation(
				this.time.addMinutes(time),
				this.calcTime.addMinutes(calcTime)
			);
	}
}
