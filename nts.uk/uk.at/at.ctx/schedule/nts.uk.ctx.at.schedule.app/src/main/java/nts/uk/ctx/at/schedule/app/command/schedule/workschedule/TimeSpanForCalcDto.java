package nts.uk.ctx.at.schedule.app.command.schedule.workschedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSpanForCalcDto {

	/** 開始時刻 */
//	@AttendanceItemLayout(layout = "A")
//	@AttendanceItemValue(itemId = -1, type = ValueType.TIME_WITH_DAY)
	private Integer start;

	/** 終了時刻 */
//	@AttendanceItemLayout(layout = "B")
//	@AttendanceItemValue(itemId = -1, type = ValueType.TIME_WITH_DAY)
	private Integer end;
	
	@Override
	public TimeSpanForCalcDto clone() {
		return new TimeSpanForCalcDto(start, end);
	}
}
