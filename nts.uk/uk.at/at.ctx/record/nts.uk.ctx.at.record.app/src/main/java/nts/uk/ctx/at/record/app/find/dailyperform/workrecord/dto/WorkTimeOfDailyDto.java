package nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendanceitem.util.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendanceitem.util.annotation.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendanceitem.util.item.ValueType;

/** 日別実績の作業時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkTimeOfDailyDto {
	
	/** 作業枠NO: 作業枠NO */
	private int workFrameNo;
	
	/** 時間帯: 作業実績の時間帯 */
	@AttendanceItemLayout(layout="A", jpPropertyName="")
	private ActualWorkTimeSheetDto timeSheet;
	
	/** 時間: 作業実績の時間 */
	@AttendanceItemLayout(layout="B", jpPropertyName="")
	@AttendanceItemValue(type=ValueType.INTEGER)
	private Integer workTime;
}
