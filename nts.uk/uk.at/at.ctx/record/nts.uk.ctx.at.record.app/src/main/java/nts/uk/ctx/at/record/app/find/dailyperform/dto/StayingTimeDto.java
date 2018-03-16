package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workingtime.StayingTimeOfDaily;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 日別実績の滞在時間 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StayingTimeDto {

	/** 滞在時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "滞在時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer stayingTime;

	/** 出勤前時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "出勤前時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer beforeWoringTime;

	/** 退勤後時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "退勤後時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer afterLeaveTime;

	/** PCログオン前時間 */
	@AttendanceItemLayout(layout = "D", jpPropertyName = "PCログオン前時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer beforePCLogOnTime;

	/** PCログオフ後時間 */
	@AttendanceItemLayout(layout = "E", jpPropertyName = "PCログオフ後時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer afterPCLogOffTime;

	public static StayingTimeDto fromStayingTime(StayingTimeOfDaily domain) {
		return domain == null ? null : new StayingTimeDto(
												getAttendanceTime(domain.getStayingTime()),
												getAttendanceTime(domain.getBeforeWoringTime()), 
												getAttendanceTime(domain.getAfterLeaveTime()),
												getAttendanceTime(domain.getBeforePCLogOnTime()), 
												getAttendanceTime(domain.getAfterPCLogOffTime()));
	}

	public StayingTimeOfDaily toDomain() {
		return new StayingTimeOfDaily(
							toAttendanceTime(afterPCLogOffTime), 
							toAttendanceTime(beforePCLogOnTime),
							toAttendanceTime(beforeWoringTime), 
							toAttendanceTime(stayingTime),
							toAttendanceTime(afterLeaveTime));
	}

	private AttendanceTime toAttendanceTime(Integer time) {
		return time == null ? null : new AttendanceTime(time);
	}
	
	private static Integer getAttendanceTime(AttendanceTime time) {
		return time == null ? null : time.valueAsMinutes();
	}
}
