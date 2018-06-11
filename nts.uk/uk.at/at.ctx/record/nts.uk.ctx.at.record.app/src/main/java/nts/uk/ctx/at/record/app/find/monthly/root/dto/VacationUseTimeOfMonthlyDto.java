package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.AnnualLeaveUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.CompensatoryLeaveUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.RetentionYearlyUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.SpecialHolidayUseTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.vacationusetime.VacationUseTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の休暇使用時間 */
public class VacationUseTimeOfMonthlyDto {

	/** 年休 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "年休", layout = "A")
	private int annualLeave;

	/** 積立年休 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "積立年休", layout = "B")
	private int retentionYearly;

	/** 特別休暇 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "特別休暇", layout = "C")
	private int specialHoliday;

	/** 代休 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "代休", layout = "D")
	private int compensatoryLeave;

	public VacationUseTimeOfMonthly toDomain() {
		return VacationUseTimeOfMonthly.of(
							AnnualLeaveUseTimeOfMonthly.of(new AttendanceTimeMonth(annualLeave)),
							RetentionYearlyUseTimeOfMonthly.of(new AttendanceTimeMonth(retentionYearly)),
							SpecialHolidayUseTimeOfMonthly.of(new AttendanceTimeMonth(specialHoliday)),
							CompensatoryLeaveUseTimeOfMonthly.of(new AttendanceTimeMonth(compensatoryLeave)));
	}
	
	public static VacationUseTimeOfMonthlyDto from(VacationUseTimeOfMonthly domain) {
		VacationUseTimeOfMonthlyDto dto = new VacationUseTimeOfMonthlyDto();
		if(domain != null) {
			dto.setAnnualLeave(domain.getAnnualLeave() == null ? 0 : domain.getAnnualLeave().getUseTime().valueAsMinutes());
			dto.setCompensatoryLeave(domain.getCompensatoryLeave() == null ? 0 : domain.getCompensatoryLeave().getUseTime().valueAsMinutes());
			dto.setRetentionYearly(domain.getRetentionYearly() == null ? 0 : domain.getRetentionYearly().getUseTime().valueAsMinutes());
			dto.setSpecialHoliday(domain.getSpecialHoliday() == null ? 0 : domain.getSpecialHoliday().getUseTime().valueAsMinutes());
		}
		return dto;
	}
}
