package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.TimeDigestOfDaily;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 日別実績の時間消化休暇 */
@Data
@AllArgsConstructor
public class TimeDigestionVacationDailyPerformDto {

	/** 不足時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "不足時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer shortageTime;

	/** 使用時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "使用時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer useTime;
	
	public static TimeDigestionVacationDailyPerformDto from(TimeDigestOfDaily domain) {
		return domain == null ? null: new TimeDigestionVacationDailyPerformDto(
				domain.getLeakageTime() == null ? null : domain.getLeakageTime().valueAsMinutes(), 
				domain.getUseTime() == null ? null : domain.getUseTime().valueAsMinutes());
	}
	
	public TimeDigestOfDaily toDomain() {
		return new TimeDigestOfDaily(useTime == null ? null : new AttendanceTime(useTime),
									shortageTime == null ? null : new AttendanceTime(shortageTime));
	}
}
