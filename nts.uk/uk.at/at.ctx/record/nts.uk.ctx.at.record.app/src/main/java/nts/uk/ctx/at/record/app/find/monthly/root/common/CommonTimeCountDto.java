package nts.uk.ctx.at.record.app.find.monthly.root.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.TimeMonthWithCalculationDto;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimesMonth;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.lateleaveearly.Late;
import nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.lateleaveearly.LeaveEarly;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 早退 + 遅刻 */
public class CommonTimeCountDto {

	@AttendanceItemLayout(jpPropertyName = "回数", layout = "A")
	@AttendanceItemValue(type = ValueType.INTEGER)
	/** 回数: 勤怠月間回数 */
	private Integer times;

	@AttendanceItemLayout(jpPropertyName = "時間", layout = "B")
	/** 時間: 計算付き月間時間 */
	private TimeMonthWithCalculationDto time;
	
	public static CommonTimeCountDto from(LeaveEarly domain) {
		CommonTimeCountDto dto = new CommonTimeCountDto();
		if(domain != null) {
			dto.setTime(TimeMonthWithCalculationDto.from(domain.getTime()));
			dto.setTimes(domain.getTimes() == null ? null : domain.getTimes().v());
		}
		return dto;
	}
	
	public static CommonTimeCountDto from(Late domain) {
		CommonTimeCountDto dto = new CommonTimeCountDto();
		if(domain != null) {
			dto.setTime(TimeMonthWithCalculationDto.from(domain.getTime()));
			dto.setTimes(domain.getTimes() == null ? null : domain.getTimes().v());
		}
		return dto;
	}
	
	public Late toLate() {
		return Late.of(times == null ? null : new AttendanceTimesMonth(times),
						time == null ? null : time.toDomain());
	}
	
	public LeaveEarly toLeaveEarly() {
		return LeaveEarly.of(times == null ? null : new AttendanceTimesMonth(times),
							time == null ? null : time.toDomain());
	}
}
