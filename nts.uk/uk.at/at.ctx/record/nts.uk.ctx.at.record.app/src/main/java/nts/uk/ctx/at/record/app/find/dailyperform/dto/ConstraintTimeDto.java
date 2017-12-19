package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.Data;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemValue;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ValueType;

/** 総拘束時間 */
@Data
public class ConstraintTimeDto {

	/** 総拘束時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "総拘束時間")
	@AttendanceItemValue(itemId = 749, type = ValueType.INTEGER)
	private Integer totalConstraintTime;

	/** 深夜拘束時間 : 勤怠時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "深夜拘束時間")
	@AttendanceItemValue(itemId = 748, type = ValueType.INTEGER)
	private Integer lateNightConstraintTime;
}
