package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.common.days.MonthlyDays;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnualLeaveAttdRateDays;

@Data
/** 年休出勤率日数 */
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveAttdRateDaysDto implements ItemConst, AttendanceItemDataGate {

	/** 労働日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = LABOR, layout = LAYOUT_A)
	private double workingDays;
	
	/** 所定日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = WITHIN_STATUTORY, layout = LAYOUT_B)
	private double prescribedDays;
	
	/** 控除日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = DEDUCTION, layout = LAYOUT_C)
	private double deductedDays;
	
	public static AnnualLeaveAttdRateDaysDto from(AnnualLeaveAttdRateDays domain) {
		return domain == null ? null : new AnnualLeaveAttdRateDaysDto(
									domain.getWorkingDays().v(), 
									domain.getPrescribedDays().v(),
									domain.getDeductedDays().v());
	}
	
	public AnnualLeaveAttdRateDays toAttdRateDaysDomain() {
		return AnnualLeaveAttdRateDays.of(new MonthlyDays(workingDays), 
											new MonthlyDays(prescribedDays), 
											new MonthlyDays(deductedDays));
	}

	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case LABOR:
			return Optional.of(ItemValue.builder().value(workingDays).valueType(ValueType.DAYS));
		case WITHIN_STATUTORY:
			return Optional.of(ItemValue.builder().value(prescribedDays).valueType(ValueType.DAYS));
		case DEDUCTION:
			return Optional.of(ItemValue.builder().value(deductedDays).valueType(ValueType.DAYS));
		default:
			break;
		}
		return AttendanceItemDataGate.super.valueOf(path);
	}

	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case LABOR:
		case WITHIN_STATUTORY:
		case DEDUCTION:
			return PropType.VALUE;
		default:
			break;
		}
		return AttendanceItemDataGate.super.typeOf(path);
	}

	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case LABOR:
			workingDays = value.valueOrDefault(0d); break;
		case WITHIN_STATUTORY:
			(prescribedDays) = value.valueOrDefault(0d); break;
		case DEDUCTION:
			(deductedDays) = value.valueOrDefault(0d); break;
		default:
			break;
		}
	}

	
}
