package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.ortherpackage.classfunction.TimevacationUseTimeOfDaily;

/**
 * 日別実績の時間休暇使用時間
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValicationUseDto implements ItemConst, AttendanceItemDataGate {

	/** 時間年休使用時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = ANNUNAL_LEAVE)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer timeAnnualLeaveUseTime;

	/** 超過有休使用時間 === 60H超休使用時間 */
	@AttendanceItemLayout(layout =LAYOUT_B, jpPropertyName = EXCESS)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer excessHolidayUseTime;

	/** 特別休暇使用時間 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = SPECIAL)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer timeSpecialHolidayUseTime;

	/** 時間代休使用時間 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = COMPENSATORY)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer timeCompensatoryLeaveUseTime;
	
	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case ANNUNAL_LEAVE:
			return Optional.of(ItemValue.builder().value(timeAnnualLeaveUseTime).valueType(ValueType.TIME));
		case EXCESS:
			return Optional.of(ItemValue.builder().value(excessHolidayUseTime).valueType(ValueType.TIME));
		case SPECIAL:
			return Optional.of(ItemValue.builder().value(timeSpecialHolidayUseTime).valueType(ValueType.TIME));
		case COMPENSATORY:
			return Optional.of(ItemValue.builder().value(timeCompensatoryLeaveUseTime).valueType(ValueType.TIME));
		default:
			return Optional.empty();
		}
	}
	
	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case ANNUNAL_LEAVE:
		case EXCESS:
		case SPECIAL:
		case COMPENSATORY:
			return PropType.VALUE;
		default:
			return PropType.OBJECT;
		}
	}

	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case ANNUNAL_LEAVE:
			this.timeAnnualLeaveUseTime = value.valueOrDefault(null);
			break;
		case EXCESS:
			this.excessHolidayUseTime = value.valueOrDefault(null);
			break;
		case SPECIAL:
			this.timeSpecialHolidayUseTime = value.valueOrDefault(null);
			break;
		case COMPENSATORY:
			this.timeCompensatoryLeaveUseTime = value.valueOrDefault(null);
			break;
		default:
			break;
		}
	}
	
	public TimevacationUseTimeOfDaily toDomain(){
		return new TimevacationUseTimeOfDaily(
						timeAnnualLeaveUseTime == null ? AttendanceTime.ZERO : new AttendanceTime(timeAnnualLeaveUseTime), 
						timeCompensatoryLeaveUseTime == null ? AttendanceTime.ZERO : new AttendanceTime(timeCompensatoryLeaveUseTime), 
						excessHolidayUseTime == null ? AttendanceTime.ZERO : new AttendanceTime(excessHolidayUseTime), 
						timeSpecialHolidayUseTime == null ? AttendanceTime.ZERO : new AttendanceTime(timeSpecialHolidayUseTime));
	}
	
	public static TimevacationUseTimeOfDaily createEmpty(){
		return new TimevacationUseTimeOfDaily(AttendanceTime.ZERO, AttendanceTime.ZERO, 
											AttendanceTime.ZERO, AttendanceTime.ZERO);
	}
	
	@Override
	public ValicationUseDto clone(){
		return new ValicationUseDto(timeAnnualLeaveUseTime, excessHolidayUseTime, 
									timeSpecialHolidayUseTime, timeCompensatoryLeaveUseTime);
	}
}
