package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.ExcessOverTimeWorkMidNightTime;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 法定外残業深夜時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcessOverTimeWorkMidNightTimeDto {

	/** 時間: 計算付き時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "時間")
	private CalcAttachTimeDto time;

	public static ExcessOverTimeWorkMidNightTimeDto fromOverTimeWorkDailyPerform(
			ExcessOverTimeWorkMidNightTime domain) {
		return domain == null ? null
				: new ExcessOverTimeWorkMidNightTimeDto(new CalcAttachTimeDto(
						domain.getTime().getCalcTime().valueAsMinutes(), domain.getTime().getTime().valueAsMinutes()));
	}

	public ExcessOverTimeWorkMidNightTime toDomain() {
		return new ExcessOverTimeWorkMidNightTime(TimeWithCalculation
				.createTimeWithCalculation(new AttendanceTime(time.getTime()), new AttendanceTime(time.getCalcTime())));
	}
}
