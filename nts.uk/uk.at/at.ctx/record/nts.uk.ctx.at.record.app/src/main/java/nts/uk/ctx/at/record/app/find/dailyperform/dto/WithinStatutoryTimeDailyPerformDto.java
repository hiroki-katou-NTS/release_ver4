package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 日別実績の所定内時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithinStatutoryTimeDailyPerformDto {

	/** 就業時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "就業時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer workTime;

	/** 実働就業時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "就業時間（休暇加算時間含む）")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer workTimeIncludeVacationTime;

	/** 所定内割増時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "所定内割増時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer withinPrescribedPremiumTime;

	/** 所定内深夜時間: 所定内深夜時間 */
	@AttendanceItemLayout(layout = "D", jpPropertyName = "所定内深夜時間")
	private CalcAttachTimeDto withinStatutoryMidNightTime;

	/** 休暇加算時間: 勤怠時間 */
	// TODO: Check id
	// 日別実績の勤怠時間．実績時間．総労働時間．所定内時間．休暇加算時間 年休加算時間 576
	// 日別実績の勤怠時間．実績時間．総労働時間．所定内時間．休暇加算時間 特別休暇加算時間 577
	// 日別実績の勤怠時間．実績時間．総労働時間．所定内時間．休暇加算時間 積立年休加算時間 578
	@AttendanceItemLayout(layout = "E", jpPropertyName = "休暇加算時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer vacationAddTime;

	public static WithinStatutoryTimeDailyPerformDto fromWithinStatutoryTimeDailyPerform(
			WithinStatutoryTimeOfDaily domain) {
		return domain == null ? null: new WithinStatutoryTimeDailyPerformDto(
						getAttendanceTime(domain.getWorkTime()),
						getAttendanceTime(domain.getWorkTimeIncludeVacationTime()),
						getAttendanceTime(domain.getWithinPrescribedPremiumTime()),
						getWithStatutory(domain.getWithinStatutoryMidNightTime()),
						getAttendanceTime(domain.getVacationAddTime()));
	}

	private static CalcAttachTimeDto getWithStatutory(WithinStatutoryMidNightTime domain) {
		return domain == null || domain.getTime() == null ? null : CalcAttachTimeDto.toTimeWithCal(domain.getTime());
	}

	public WithinStatutoryTimeOfDaily toDomain() {
		return WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
				toAttendanceTime(workTime),
				toAttendanceTime(workTimeIncludeVacationTime), 
				toAttendanceTime(withinPrescribedPremiumTime),
				withinStatutoryMidNightTime == null ? null 
						: new WithinStatutoryMidNightTime(withinStatutoryMidNightTime.createTimeWithCalc()),
				toAttendanceTime(vacationAddTime));
	}
	
	private static Integer getAttendanceTime(AttendanceTime domain) {
		return domain == null ? null : domain.valueAsMinutes();
	}
	
	private AttendanceTime toAttendanceTime(Integer time) {
		return time == null ? null : new AttendanceTime(time);
	}
}
