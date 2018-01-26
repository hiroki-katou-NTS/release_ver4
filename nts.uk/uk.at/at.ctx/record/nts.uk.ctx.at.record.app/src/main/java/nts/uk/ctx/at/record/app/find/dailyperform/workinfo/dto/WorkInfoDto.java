package nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;

/** 勤務情報 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkInfoDto {

	/** 勤務種類コード */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "勤務種類コード")
	@AttendanceItemValue
	private String workTypeCode;

	/** 就業時間帯コード */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "就業時間帯コード")
	@AttendanceItemValue
	private String workTimeCode;
}
