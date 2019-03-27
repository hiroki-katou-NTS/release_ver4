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
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;

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
	
	@Override
	public StayingTimeDto clone() {
		return new StayingTimeDto(stayingTime, beforeWoringTime, afterLeaveTime, beforePCLogOnTime, afterPCLogOffTime);
	}

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
							toAttendanceTimeMinus(afterPCLogOffTime), 
							toAttendanceTimeMinus(beforePCLogOnTime),
							toAttendanceTimeMinus(beforeWoringTime), 
							toAttendanceTime(stayingTime),
							toAttendanceTimeMinus(afterLeaveTime));
	}
	
	public static StayingTimeOfDaily defaultDomain() {
		return new StayingTimeOfDaily(AttendanceTime.ZERO, 
										AttendanceTime.ZERO,
										AttendanceTime.ZERO, 
										AttendanceTime.ZERO,
										AttendanceTime.ZERO);
	}

	private AttendanceTime toAttendanceTime(Integer time) {
		return time == null ? AttendanceTime.ZERO : new AttendanceTime(time);
	}

	private AttendanceTimeOfExistMinus toAttendanceTimeMinus(Integer time) {
		return time == null ? new AttendanceTimeOfExistMinus(0) : new AttendanceTimeOfExistMinus(time);
	}
	
	private static Integer getAttendanceTime(AttendanceTime time) {
		return time == null ? 0 : time.valueAsMinutes();
	}
	
	private static Integer getAttendanceTime(AttendanceTimeOfExistMinus time) {
		return time == null ? 0 : time.valueAsMinutes();
	}
}
