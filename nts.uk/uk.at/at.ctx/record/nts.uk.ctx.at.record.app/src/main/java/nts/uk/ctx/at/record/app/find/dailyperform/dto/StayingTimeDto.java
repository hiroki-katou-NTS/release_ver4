package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workingtime.StayingTimeOfDaily;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 日別実績の滞在時間 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StayingTimeDto implements ItemConst {

	/** 滞在時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = STAYING)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer stayingTime;

	/** 出勤前時間 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = ATTENDANCE)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer beforeWoringTime;

	/** 退勤後時間 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = LEAVE)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer afterLeaveTime;

	/** PCログオン前時間 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = LOGON)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer beforePCLogOnTime;

	/** PCログオフ後時間 */
	@AttendanceItemLayout(layout = LAYOUT_E, jpPropertyName = LOGOFF)
	@AttendanceItemValue(type = ValueType.TIME)
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
		return time == null ? new AttendanceTime(0) : new AttendanceTime(time);
	}
	
	private static Integer getAttendanceTime(AttendanceTime time) {
		return time == null ? 0 : time.valueAsMinutes();
	}
}
