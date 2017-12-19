package nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto;

import lombok.Data;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemValue;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ValueType;

/** 予定時間帯 */
@Data
public class ScheduleTimeZoneDto {

	/** 勤務NO */
	private Integer workNo;

	/** 出勤 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "出勤")
	@AttendanceItemValue(itemId = { 3, 5 }, type = ValueType.INTEGER)
	private Integer working;

	/** 退勤 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "退勤")
	@AttendanceItemValue(itemId = { 4, 6 }, type = ValueType.INTEGER)
	private Integer leave;
}
