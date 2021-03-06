package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.worktime.attendanceleave.AttendanceLeaveGateTimeOfMonthly;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の入退門時間 */
public class AttendanceLeaveGateTimeOfMonthlyDto implements ItemConst, AttendanceItemDataGate {

	/** 出勤前時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = ATTENDANCE, layout = LAYOUT_A)
	private int timeBeforeAttendance;

	/** 滞在時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = STAYING, layout = LAYOUT_B)
	private int stayingTime;

	/** 退勤後時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = LEAVE, layout = LAYOUT_C)
	private int timeAfterLeaveWork;

	/** 不就労時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = UNEMPLOYED, layout = LAYOUT_D)
	private int unemployedTime;
	
	public static AttendanceLeaveGateTimeOfMonthlyDto from(AttendanceLeaveGateTimeOfMonthly domain) {
		AttendanceLeaveGateTimeOfMonthlyDto dto = new AttendanceLeaveGateTimeOfMonthlyDto();
		if(domain != null) {
			dto.setStayingTime(domain.getStayingTime() == null ? 0 : domain.getStayingTime().valueAsMinutes());
			dto.setTimeAfterLeaveWork(domain.getTimeAfterLeaveWork() == null ? 0 : domain.getTimeAfterLeaveWork().valueAsMinutes());
			dto.setTimeBeforeAttendance(domain.getTimeBeforeAttendance() == null ? 0 : domain.getTimeBeforeAttendance().valueAsMinutes());
			dto.setUnemployedTime(domain.getUnemployedTime() == null ? 0 : domain.getUnemployedTime().valueAsMinutes());
		}
		return dto;
	}
	public AttendanceLeaveGateTimeOfMonthly toDomain() {
		return AttendanceLeaveGateTimeOfMonthly.of(
										toAttendanceTimeMonth(timeBeforeAttendance), 
										toAttendanceTimeMonth(timeAfterLeaveWork), 
										toAttendanceTimeMonth(stayingTime), 
										toAttendanceTimeMonth(unemployedTime));
	}
	
	private AttendanceTimeMonth toAttendanceTimeMonth(Integer time) {
		return new AttendanceTimeMonth(time);
	}
	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case ATTENDANCE:
			return Optional.of(ItemValue.builder().value(timeBeforeAttendance).valueType(ValueType.TIME));
		case STAYING:
			return Optional.of(ItemValue.builder().value(stayingTime).valueType(ValueType.TIME));
		case LEAVE:
			return Optional.of(ItemValue.builder().value(timeAfterLeaveWork).valueType(ValueType.TIME));
		case UNEMPLOYED:
			return Optional.of(ItemValue.builder().value(unemployedTime).valueType(ValueType.TIME));
		default:
			return Optional.empty();
		}
	}
	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case ATTENDANCE:
		case STAYING:
		case LEAVE:
		case UNEMPLOYED:
			return PropType.VALUE;
		default:
			return PropType.OBJECT;
		}
	}
	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case ATTENDANCE:
			timeBeforeAttendance = value.valueOrDefault(0); break;
		case STAYING:
			stayingTime = value.valueOrDefault(0); break;
		case LEAVE:
			timeAfterLeaveWork = value.valueOrDefault(0); break;
		case UNEMPLOYED:
			unemployedTime = value.valueOrDefault(0); break;
		default:
		}
	}


}

