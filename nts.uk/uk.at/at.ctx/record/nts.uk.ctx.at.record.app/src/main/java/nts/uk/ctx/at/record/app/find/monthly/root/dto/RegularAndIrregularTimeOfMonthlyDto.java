package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.IrregularWorkingTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.calc.actualworkingtime.RegularAndIrregularTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の通常変形時間 */
public class RegularAndIrregularTimeOfMonthlyDto implements ItemConst {

	/** 週割増合計時間 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = WEEKLY_PREMIUM + TOTAL, layout = LAYOUT_A)
	private int weeklyTotalPremiumTime;

	/** 月割増合計時間 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = MONTHLY_PREMIUM + TOTAL, layout = LAYOUT_B)
	private int monthlyTotalPremiumTime;

	/** 変形労働時間 */
	@AttendanceItemLayout(jpPropertyName = IRREGULAR + LABOR, layout = LAYOUT_C)
	private IrregularWorkingTimeOfMonthlyDto irregularWorkingTime;

	public RegularAndIrregularTimeOfMonthly toDomain() {
		return RegularAndIrregularTimeOfMonthly.of(
				new AttendanceTimeMonth(weeklyTotalPremiumTime), 
				new AttendanceTimeMonth(monthlyTotalPremiumTime),
				irregularWorkingTime == null ? new IrregularWorkingTimeOfMonthly() : irregularWorkingTime.toDomain());
	}
	
	public static RegularAndIrregularTimeOfMonthlyDto from(RegularAndIrregularTimeOfMonthly domain) {
		RegularAndIrregularTimeOfMonthlyDto dto = new RegularAndIrregularTimeOfMonthlyDto();
		if(domain != null) {
			dto.setIrregularWorkingTime(IrregularWorkingTimeOfMonthlyDto.from(domain.getIrregularWorkingTime()));
			dto.setMonthlyTotalPremiumTime(domain.getMonthlyTotalPremiumTime() == null 
					? 0 : domain.getMonthlyTotalPremiumTime().valueAsMinutes());
			dto.setWeeklyTotalPremiumTime(domain.getWeeklyTotalPremiumTime() == null 
					? 0 : domain.getWeeklyTotalPremiumTime().valueAsMinutes());
		}
		return dto;
	}
}
