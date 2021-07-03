package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.calc.flex.FlexCarryforwardTime;

@Data
/** フレックス繰越時間 */
@NoArgsConstructor
@AllArgsConstructor
public class FlexCarryforwardTimeDto implements ItemConst, AttendanceItemDataGate {

	/** フレックス繰越勤務時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = WORKING_TIME, layout = LAYOUT_A)
	private int flexCarryforwardWorkTime;

	/** フレックス繰越時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = TIME, layout = LAYOUT_B)
	private int flexCarryforwardTime;

	/** フレックス繰越不足時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = SHORTAGE, layout = LAYOUT_C)
	private int flexCarryforwardShortageTime;
	
	/** フレックス繰越不可時間 */
	private int flexNotCarryforwardTime;

	public FlexCarryforwardTime toDomain() {
		return FlexCarryforwardTime.of(
						new AttendanceTimeMonthWithMinus(flexCarryforwardTime),
						new AttendanceTimeMonth(flexCarryforwardWorkTime),
						new AttendanceTimeMonth(flexCarryforwardShortageTime),
						new AttendanceTimeMonth(flexNotCarryforwardTime));
	}
	
	public static FlexCarryforwardTimeDto from(FlexCarryforwardTime domain) {
		FlexCarryforwardTimeDto dto = new FlexCarryforwardTimeDto();
		if(domain != null) {
			dto.setFlexCarryforwardShortageTime(from(domain.getFlexCarryforwardShortageTime()));
			dto.setFlexCarryforwardTime(from(domain.getFlexCarryforwardTime()));
			dto.setFlexCarryforwardWorkTime(from(domain.getFlexCarryforwardWorkTime()));
			dto.setFlexNotCarryforwardTime(from(domain.getFlexNotCarryforwardTime()));
		}
		return dto;
	}

	private static Integer from(AttendanceTimeMonth domain) {
		return domain == null ? 0 : domain.valueAsMinutes();
	}

	private static Integer from(AttendanceTimeMonthWithMinus domain) {
		return domain == null ? 0 : domain.valueAsMinutes();
	}
	
	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case WORKING_TIME:
			return Optional.of(ItemValue.builder().value(flexCarryforwardWorkTime).valueType(ValueType.TIME));
		case TIME:
			return Optional.of(ItemValue.builder().value(flexCarryforwardTime).valueType(ValueType.TIME));
		case SHORTAGE:
			return Optional.of(ItemValue.builder().value(flexCarryforwardShortageTime).valueType(ValueType.TIME));
		case NO + SHORTAGE:
			return Optional.of(ItemValue.builder().value(flexNotCarryforwardTime).valueType(ValueType.TIME));
		default:
			return Optional.empty();
		}
	}

	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case WORKING_TIME:
		case TIME:
		case SHORTAGE:
		case NO + SHORTAGE:
			return PropType.VALUE;
		default:
			return PropType.OBJECT;
		}
	}

	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case WORKING_TIME:
			flexCarryforwardWorkTime = value.valueOrDefault(0);
			break;
		case TIME:
			flexCarryforwardTime = value.valueOrDefault(0);
			break;
		case SHORTAGE:
			flexCarryforwardShortageTime = value.valueOrDefault(0);
			break;
		case NO + SHORTAGE:
			flexNotCarryforwardTime = value.valueOrDefault(0);
			break;
		default:
		}
	}
}
