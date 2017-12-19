package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.List;

import lombok.Data;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;

/** 日別実績の加給時間 */
@Data
public class RaisingSalaryTimeDailyPerformDto {

	/** 特定日加給時間 : 加給時間 */
	@AttendanceItemLayout(layout = "A", isList = true, jpPropertyName = "特定加給時間")
	private List<RaisingSalaryTimeDto> specificDayOfRaisingSalaryTime;

	/** 加給時間 : 加給時間 */
	@AttendanceItemLayout(layout = "B", isList = true, jpPropertyName = "加給時間", needCheckIDWithIndex = true)
	private List<RaisingSalaryTimeDto> raisingSalaryTime;
}
