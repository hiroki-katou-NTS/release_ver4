package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.WorkTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の就業時間 */
public class WorkingTimeOfMonthlyDto {

	/** 就業時間 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "就業時間", layout = "A")
	private int workTime;

	/** 所定内割増時間 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "所定内割増時間", layout = "B")
	private int withinPrescribedPremiumTime;
	
	/** 実働就業時間 */
	@AttendanceItemValue(type = ValueType.INTEGER)
	@AttendanceItemLayout(jpPropertyName = "実働就業時間", layout = "C")
	private int actualWorkTime;
	
	public static WorkingTimeOfMonthlyDto from(WorkTimeOfMonthly domain) {
		WorkingTimeOfMonthlyDto dto = new WorkingTimeOfMonthlyDto();
		if(domain != null) {
			dto.setWithinPrescribedPremiumTime(domain.getWithinPrescribedPremiumTime() == null 
					? 0 : domain.getWithinPrescribedPremiumTime().valueAsMinutes());
			dto.setWorkTime(domain.getWorkTime() == null ? 0 : domain.getWorkTime().valueAsMinutes());
			dto.setActualWorkTime(domain.getActualWorkTime() == null ? 0 : domain.getActualWorkTime().valueAsMinutes());
		}
		return dto;
	}
	
	public WorkTimeOfMonthly toDomain() {
		return WorkTimeOfMonthly.of(new AttendanceTimeMonth(workTime),
									new AttendanceTimeMonth(withinPrescribedPremiumTime),
									new AttendanceTimeMonth(actualWorkTime));
	}
}
