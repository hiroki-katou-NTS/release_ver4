package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.Data;

/** 休出枠時間帯 */
@Data
public class HolidayWorkFrameTimeSheetDto {

	/** 時間帯: 計算用時間帯 */
//	@AttendanceItemLayout(layout = "A")
	private TimeSpanForCalcDto timeSheet;

	/** 休出枠NO: 休出枠NO */
//	@AttendanceItemLayout(layout = "B")
//	@AttendanceItemValue(itemId = -1, type = ValueType.INTEGER)
	private Integer holidayWorkFrameNo;
}
