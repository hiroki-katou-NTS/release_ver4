package nts.uk.ctx.at.record.app.find.dailyperform.workrecord.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.dailyperform.common.WithActualTimeStampDto;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;

/** 作業実績の時間帯 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualWorkTimeSheetDto {

	/**　開始: 勤怠打刻(実打刻付き) */
	@AttendanceItemLayout(layout="A", jpPropertyName="")
	private WithActualTimeStampDto start;
	
	/**　終了: 勤怠打刻(実打刻付き) */
	@AttendanceItemLayout(layout="B", jpPropertyName="")
	private WithActualTimeStampDto end;
}
