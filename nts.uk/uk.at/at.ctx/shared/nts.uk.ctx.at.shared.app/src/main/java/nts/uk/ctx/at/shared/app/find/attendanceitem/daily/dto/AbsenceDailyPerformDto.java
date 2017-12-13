package nts.uk.ctx.at.shared.app.find.attendanceitem.daily.dto;

import lombok.Data;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemValue;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ValueType;

/** 日別実績の欠勤 */
@Data
public class AbsenceDailyPerformDto {

	/** 使用時間:　勤怠時間*/
	@AttendanceItemLayout(layout="A")
	@AttendanceItemValue(itemId=-1, type=ValueType.INTEGER)
	private int annualLeaveUseTime;
}
