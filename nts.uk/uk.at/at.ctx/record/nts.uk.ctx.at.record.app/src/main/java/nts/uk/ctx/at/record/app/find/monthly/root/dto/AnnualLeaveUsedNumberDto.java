package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.common.TimeUsedNumberDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave.AnnualLeaveUsedNumber;

@Data
/** 年休使用数 */
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveUsedNumberDto implements ItemConst, AttendanceItemDataGate {

	/** 使用日数 */
	@AttendanceItemLayout(jpPropertyName = DAYS, layout = LAYOUT_A)
	private AnnualLeaveUsedDaysDto usedDays;

	/** 使用時間 */
	@AttendanceItemLayout(jpPropertyName = TIME, layout = LAYOUT_B)
	private TimeUsedNumberDto usedTime;

	public static AnnualLeaveUsedNumberDto from(AnnualLeaveUsedNumber domain) {
		return domain == null ? null : new AnnualLeaveUsedNumberDto(
												AnnualLeaveUsedDaysDto.from(domain.getUsedDays().orElse(null)),
												TimeUsedNumberDto.from(domain.getUsedTime().orElse(null)));
	}
	
	public AnnualLeaveUsedNumber toDomain() {
		return AnnualLeaveUsedNumber.of(
								Optional.ofNullable(usedDays == null ? null : usedDays.toDomain()), 
								Optional.ofNullable(usedTime == null ? null : usedTime.toDomain()));
	}
	
	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case DAYS:
		case TIME:
			return PropType.OBJECT;
		default:
			return PropType.OBJECT;
		}
	}
	
	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		switch (path) {
		case DAYS:
			return new AnnualLeaveUsedDaysDto();
		case TIME:
			return new TimeUsedNumberDto();
		default:
			break;
		}
		return null;
	}
	
	@Override
	public Optional<AttendanceItemDataGate> get(String path) {
		switch (path) {
		case DAYS:
			return Optional.ofNullable(usedDays);
		case TIME:
			return Optional.ofNullable(usedTime);
		default:
			break;
		}
		return Optional.empty();
	}

	@Override
	public void set(String path, AttendanceItemDataGate value) {
		switch (path) {
		case DAYS:
			this.usedDays = (AnnualLeaveUsedDaysDto) value;
			break;
		case TIME:
			this.usedTime = (TimeUsedNumberDto) value;
			break;
		default:
			break;
		}
	}
}
