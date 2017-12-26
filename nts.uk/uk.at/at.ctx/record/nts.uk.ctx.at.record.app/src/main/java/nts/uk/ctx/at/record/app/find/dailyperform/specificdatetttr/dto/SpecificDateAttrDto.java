package nts.uk.ctx.at.record.app.find.dailyperform.specificdatetttr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemValue;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ValueType;

/** 特定日区分 */
@Data
@AllArgsConstructor
public class SpecificDateAttrDto {

	@AttendanceItemLayout(layout = "A", jpPropertyName = "特定日区分")
	@AttendanceItemValue(type = ValueType.STRING, itemId = { 416, 417, 418, 419, 420, 421, 422, 423, 424, 425 })
	private boolean specificDate;

	private Integer itemNo;
}
