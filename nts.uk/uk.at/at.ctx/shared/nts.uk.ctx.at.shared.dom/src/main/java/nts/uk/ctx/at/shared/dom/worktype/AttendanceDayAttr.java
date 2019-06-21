package nts.uk.ctx.at.shared.dom.worktype;

import lombok.AllArgsConstructor;

/**
 * 出勤日区分
 * @author shuichi_ishida
 */
@AllArgsConstructor
public enum AttendanceDayAttr {
	/** １日休日系 */
	HOLIDAY(0),
	/** 半日出勤系 */
	HALF_TIME(1),
	/** １日出勤系 */
	FULL_TIME(2),
	/** 休出 */
	HOLIDAY_WORK(3);

	public final int value;
}
