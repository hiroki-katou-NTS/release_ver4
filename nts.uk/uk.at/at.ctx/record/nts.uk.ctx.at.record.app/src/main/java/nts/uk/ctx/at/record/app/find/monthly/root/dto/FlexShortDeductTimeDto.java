package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.calc.flex.FlexShortDeductTime;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** フレックス不足控除時間 */
public class FlexShortDeductTimeDto {

	/** 欠勤控除時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "欠勤控除時間", layout = "A")
	private int absenceDeductTime;

	/** 控除前のフレックス不足時間: 勤怠月間時間 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "控除前のフレックス不足時間", layout = "B")
	private int flexShortTimeBeforeDeduct;

	/** 年休控除日数: 勤怠月間日数 */
	@AttendanceItemValue(type = ValueType.DOUBLE)
	@AttendanceItemLayout(jpPropertyName = "年休控除日数", layout = "C")
	private double annualLeaveDeductDays;

	public FlexShortDeductTime toDomain() {
		return FlexShortDeductTime.of(
						new AttendanceDaysMonth(annualLeaveDeductDays),
						new AttendanceTimeMonth(absenceDeductTime),
						new AttendanceTimeMonth(flexShortTimeBeforeDeduct));
	}
	
	public static FlexShortDeductTimeDto from(FlexShortDeductTime domain) {
		FlexShortDeductTimeDto dto = new FlexShortDeductTimeDto();
		if(domain != null) {
			dto.setAbsenceDeductTime(domain.getAbsenceDeductTime() == null ? 0 : domain.getAbsenceDeductTime().valueAsMinutes());
			dto.setAnnualLeaveDeductDays(domain.getAnnualLeaveDeductDays() == null ? 0 : domain.getAnnualLeaveDeductDays().v());
			dto.setFlexShortTimeBeforeDeduct(domain.getFlexShortTimeBeforeDeduct() == null ? 0 : domain.getFlexShortTimeBeforeDeduct().valueAsMinutes());
		}
		return dto;
	}
}
