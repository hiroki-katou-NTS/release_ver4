package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.midnighttime.IllegalMidnightTime;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 法定外深夜時間 */
public class IllegalMidnightTimeDto {

	/** 時間 */
	@AttendanceItemLayout(jpPropertyName="時間", layout="A")
	private TimeMonthWithCalculationDto time;
	
	/** 事前時間 */
	@AttendanceItemLayout(jpPropertyName="事前時間", layout="B")
	@AttendanceItemValue(type=ValueType.INTEGER)
	private int beforeTime;
	
	public static IllegalMidnightTimeDto from(IllegalMidnightTime domain) {
		IllegalMidnightTimeDto dto = new IllegalMidnightTimeDto();
		if(domain != null) {
			dto.setBeforeTime(domain.getBeforeTime() == null ? 0 : domain.getBeforeTime().valueAsMinutes());
			dto.setTime(TimeMonthWithCalculationDto.from(domain.getTime()));
		}
		return dto;
	}

	public IllegalMidnightTime toDomain(){
		return IllegalMidnightTime.of(time == null ? new TimeMonthWithCalculation() : time.toDomain(), new AttendanceTimeMonth(beforeTime));
	}
}
