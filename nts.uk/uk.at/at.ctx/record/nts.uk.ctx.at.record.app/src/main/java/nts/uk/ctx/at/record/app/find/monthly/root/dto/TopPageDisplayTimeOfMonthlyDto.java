package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate.PropType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonthWithMinus;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.worktime.toppage.TopPageDisplayOfMonthly;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績のトップページ表示用時間 */
public class TopPageDisplayTimeOfMonthlyDto implements ItemConst, AttendanceItemDataGate  {

	/** 残業合計時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = OVERTIME, layout = LAYOUT_A)
	private int overtime;

	/** 休日出勤合計時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = HOLIDAY_WORK, layout = LAYOUT_B)
	private int holidayWorkTime;

	/** フレックス合計時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.TIME)
	@AttendanceItemLayout(jpPropertyName = FLEX, layout = LAYOUT_C)
	private int flexTime;
	
	public static TopPageDisplayTimeOfMonthlyDto from(TopPageDisplayOfMonthly domain) {
		TopPageDisplayTimeOfMonthlyDto dto = new TopPageDisplayTimeOfMonthlyDto();
		if(domain != null) {
			dto.setFlexTime(domain.getFlex().valueAsMinutes());
			dto.setHolidayWorkTime(domain.getHolidayWork().valueAsMinutes());
			dto.setOvertime(domain.getOvertime().valueAsMinutes());
		}
		return dto;
	}
	public TopPageDisplayOfMonthly toDomain() {
		return TopPageDisplayOfMonthly.of(
				new AttendanceTimeMonth(overtime),
				new AttendanceTimeMonth(holidayWorkTime), 
				new AttendanceTimeMonthWithMinus(flexTime));
	}
	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case OVERTIME:
			return Optional.of(ItemValue.builder().value(overtime).valueType(ValueType.TIME));
		case HOLIDAY_WORK:
			return Optional.of(ItemValue.builder().value(holidayWorkTime).valueType(ValueType.TIME));
		case FLEX:
			return Optional.of(ItemValue.builder().value(flexTime).valueType(ValueType.TIME));
		default:
			return Optional.empty();
		}
	}
	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case OVERTIME:
		case HOLIDAY_WORK:
		case FLEX:
			return PropType.VALUE;
		default:
			return PropType.OBJECT;
		}
	}
	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case OVERTIME:
			overtime = value.valueOrDefault(0); break;
		case HOLIDAY_WORK:
			holidayWorkTime = value.valueOrDefault(0); break;
		case FLEX:
			flexTime = value.valueOrDefault(0); break;
		default:
		}

	}
}

