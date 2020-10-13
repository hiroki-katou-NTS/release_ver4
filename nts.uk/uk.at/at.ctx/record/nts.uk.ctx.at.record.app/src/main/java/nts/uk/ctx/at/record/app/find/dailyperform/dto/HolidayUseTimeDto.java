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
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.vacationusetime.AnnualOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.vacationusetime.OverSalaryOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.vacationusetime.SpecialHolidayOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.vacationusetime.SubstituteHolidayOfDaily;

/** 日別実績の特別休暇 / 日別実績の年休 / 日別実績の超過有休 / 日別実績の代休 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayUseTimeDto implements ItemConst, AttendanceItemDataGate {

	/** 時間消化休暇使用時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = TIME_DIGESTION)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer timeDigestionVacationUseTime;

	/** 使用時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = USAGE)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer useTime;
	
	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case TIME_DIGESTION:
			return Optional.of(ItemValue.builder().value(timeDigestionVacationUseTime).valueType(ValueType.TIME));
		case USAGE:
			return Optional.of(ItemValue.builder().value(useTime).valueType(ValueType.TIME));
		default:
			break;
		}
		return AttendanceItemDataGate.super.valueOf(path);
	}
	
	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case TIME_DIGESTION:
		case USAGE:
			return PropType.VALUE;
		default:
			return PropType.OBJECT;
		}
	}

	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case TIME_DIGESTION:
			timeDigestionVacationUseTime = value.valueOrDefault(null);
			break;
		case USAGE:
			useTime = value.valueOrDefault(null);
			break;
		default:
			break;
		}
	}
	
	@Override
	public HolidayUseTimeDto clone() {
		return new HolidayUseTimeDto(timeDigestionVacationUseTime, useTime);
	}

	public SpecialHolidayOfDaily toSpecialHoliday() {
		return new SpecialHolidayOfDaily(toUseAttendanceTime(),
				toDigestionTIme());
	}
	
	public SubstituteHolidayOfDaily toSubstituteHoliday() {
		return new SubstituteHolidayOfDaily(toUseAttendanceTime(),
				toDigestionTIme());
	}
	
	public OverSalaryOfDaily toOverSalary() {
		return new OverSalaryOfDaily(toUseAttendanceTime(),
				toDigestionTIme());
	}
	
	public AnnualOfDaily toAnnualOfDaily() {
		return new AnnualOfDaily(toUseAttendanceTime(),
				toDigestionTIme());
	}
	
	public static HolidayUseTimeDto from(SpecialHolidayOfDaily domain) {
		return domain == null ? null : new HolidayUseTimeDto(fromTime(domain.getDigestionUseTime()),
				fromTime(domain.getUseTime()));
	}
	
	public static HolidayUseTimeDto from(SubstituteHolidayOfDaily domain) {
		return domain == null ? null : new HolidayUseTimeDto(fromTime(domain.getDigestionUseTime()),
				fromTime(domain.getUseTime()));
	}
	
	public static HolidayUseTimeDto from(OverSalaryOfDaily domain) {
		return domain == null ? null : new HolidayUseTimeDto(fromTime(domain.getDigestionUseTime()),
				fromTime(domain.getUseTime()));
	}
	
	public static HolidayUseTimeDto from(AnnualOfDaily domain) {
		return domain == null ? null : new HolidayUseTimeDto(fromTime(domain.getDigestionUseTime()),
				fromTime(domain.getUseTime()));
	}
	
	private static Integer fromTime(AttendanceTime time) {
		return time == null ? 0 : time.valueAsMinutes();
	}

	private AttendanceTime toDigestionTIme() {
		return timeDigestionVacationUseTime == null ? AttendanceTime.ZERO : new AttendanceTime(timeDigestionVacationUseTime);
	}

	private AttendanceTime toUseAttendanceTime() {
		return useTime == null ? AttendanceTime.ZERO : new AttendanceTime(useTime);
	}
}
