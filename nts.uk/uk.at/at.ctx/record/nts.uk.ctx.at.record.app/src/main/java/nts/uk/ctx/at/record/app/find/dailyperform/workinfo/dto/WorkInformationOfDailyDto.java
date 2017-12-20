package nts.uk.ctx.at.record.app.find.dailyperform.workinfo.dto;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemRoot;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ConvertibleAttendanceItem;

/** 日別実績の勤務情報 */
@Data
@AttendanceItemRoot(rootName = "日別実績の勤務情報")
public class WorkInformationOfDailyDto implements ConvertibleAttendanceItem {

	/** 勤務実績の勤務情報: 勤務情報 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "勤務実績の勤務情報")
	private WorkInfoDto actualWorkInfo;

	/** 勤務予定の勤務情報: 勤務情報 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "勤務予定の勤務情報")
	private WorkInfoDto planWorkInfo;

	/** 勤務予定時間帯: 予定時間帯 */
	@AttendanceItemLayout(layout = "C", isList = true, jpPropertyName = "勤務予定時間帯")
	private List<ScheduleTimeZoneDto> scheduleTimeZone;

}
