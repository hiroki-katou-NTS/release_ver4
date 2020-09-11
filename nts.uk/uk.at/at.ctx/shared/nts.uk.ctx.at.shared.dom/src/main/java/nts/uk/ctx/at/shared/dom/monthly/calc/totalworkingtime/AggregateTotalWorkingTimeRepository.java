package nts.uk.ctx.at.shared.dom.monthly.calc.totalworkingtime;

import nts.uk.ctx.at.shared.dom.monthly.AttendanceTimeOfMonthlyKey;

/**
 * リポジトリ：集計総労働時間
 * @author shuichu_ishida
 */
public interface AggregateTotalWorkingTimeRepository {

	/**
	 * 更新
	 * @param attendanceTimeOfMonthlyKey キー値：月別実績の勤怠時間
	 * @param aggregateTotalWorkingTime 集計総労働時間
	 */
	void update(AttendanceTimeOfMonthlyKey attendanceTimeOfMonthlyKey,
			AggregateTotalWorkingTime aggregateTotalWorkingTime);
}
