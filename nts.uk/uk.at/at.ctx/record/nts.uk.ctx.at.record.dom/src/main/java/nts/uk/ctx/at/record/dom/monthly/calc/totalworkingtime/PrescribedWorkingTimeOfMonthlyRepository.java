package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime;

import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthlyKey;

/**
 * 月別実績の所定労働時間
 * @author shuichu_ishida
 */
public interface PrescribedWorkingTimeOfMonthlyRepository {

	/**
	 * 追加
	 * @param attendanceTimeOfMonthlyKey キー値：月別実績の勤怠時間
	 * @param prescribedWorkingTimeOfMonthly 月別実績の所定労働時間
	 */
	void insert(AttendanceTimeOfMonthlyKey attendanceTimeOfMonthlyKey,
			PrescribedWorkingTimeOfMonthly prescribedWorkingTimeOfMonthly);

	/**
	 * 更新
	 * @param attendanceTimeOfMonthlyKey キー値：月別実績の勤怠時間
	 * @param prescribedWorkingTimeOfMonthly 月別実績の所定労働時間
	 */
	void update(AttendanceTimeOfMonthlyKey attendanceTimeOfMonthlyKey,
			PrescribedWorkingTimeOfMonthly prescribedWorkingTimeOfMonthly);
}
