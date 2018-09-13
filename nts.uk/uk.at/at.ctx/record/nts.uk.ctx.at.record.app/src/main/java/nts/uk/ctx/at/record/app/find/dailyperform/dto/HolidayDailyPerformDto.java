package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.AbsenceOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.HolidayOfDaily;
import nts.uk.ctx.at.record.dom.daily.vacationusetime.YearlyReservedOfDaily;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 日別実績の休暇 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayDailyPerformDto implements ItemConst {

	/** 年休: 日別実績の年休 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = ANNUNAL_LEAVE)
	private HolidayUseTimeDto annualLeave;

	/** 特別休暇: 日別実績の特別休暇 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = SPECIAL)
	private HolidayUseTimeDto specialHoliday;

	/** 超過有休: 日別実績の超過有休 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = EXCESS)
	private HolidayUseTimeDto excessSalaries;

	/** 代休: 日別実績の代休 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = COMPENSATORY)
	private HolidayUseTimeDto compensatoryLeave;

	/** 積立年休: 日別実績の積立年休 */
	@AttendanceItemLayout(layout = LAYOUT_E, jpPropertyName = RETENTION)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer retentionYearly;

	/** 時間消化休暇: 日別実績の時間消化休暇 */
	@AttendanceItemLayout(layout = LAYOUT_F, jpPropertyName = TIME_DIGESTION)
	private TimeDigestionVacationDailyPerformDto timeDigestionVacation;

	/** 欠勤: 日別実績の欠勤 */
	@AttendanceItemLayout(layout = LAYOUT_G, jpPropertyName = ABSENCE)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer absence;
	
	@Override
	public HolidayDailyPerformDto clone() {
		return new HolidayDailyPerformDto(annualLeave == null ? null : annualLeave.clone(), 
											specialHoliday == null ? null : specialHoliday.clone(), 
											excessSalaries == null ? null : excessSalaries.clone(), 
											compensatoryLeave == null ? null : compensatoryLeave.clone(), 
											retentionYearly, 
											timeDigestionVacation == null ? null : timeDigestionVacation.clone(), 
											absence);
	}
	
	public static HolidayDailyPerformDto from(HolidayOfDaily domain) {
		return domain == null ? null : 
			new HolidayDailyPerformDto(HolidayUseTimeDto.from(domain.getAnnual()), 
					HolidayUseTimeDto.from(domain.getSpecialHoliday()), 
					HolidayUseTimeDto.from(domain.getOverSalary()), 
					HolidayUseTimeDto.from(domain.getSubstitute()), 
					domain.getYearlyReserved() == null ? null : fromTime(domain.getYearlyReserved().getUseTime()), 
					TimeDigestionVacationDailyPerformDto.from(domain.getTimeDigest()), 
					domain.getAbsence() == null ? null : fromTime(domain.getAbsence().getUseTime()));
	}

	public HolidayOfDaily toDomain() {
		return new HolidayOfDaily(absence == null ? null : new AbsenceOfDaily(new AttendanceTime(absence)),
				timeDigestionVacation == null ? null : timeDigestionVacation.toDomain(),
				retentionYearly == null ? null : new YearlyReservedOfDaily(new AttendanceTime(retentionYearly)),
				compensatoryLeave == null ? null : compensatoryLeave.toSubstituteHoliday(),
				excessSalaries == null ? null : excessSalaries.toOverSalary(), 
				specialHoliday == null ? null : specialHoliday.toSpecialHoliday(),
				annualLeave == null ? null : annualLeave.toAnnualOfDaily());
	}
	
	private static Integer fromTime(AttendanceTime time) {
		return time == null ? null : time.valueAsMinutes();
	}
}
