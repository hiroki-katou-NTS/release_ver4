package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemLayout;
import nts.uk.ctx.at.shared.app.util.attendanceitem.annotation.AttendanceItemValue;
import nts.uk.ctx.at.shared.app.util.attendanceitem.type.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 日別実績の所定内時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithinStatutoryTimeDailyPerformDto {

	/** 就業時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "就業時間")
	@AttendanceItemValue(itemId = 532, type = ValueType.INTEGER)
	private Integer workTime;

	/** 実働就業時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "就業時間（休暇加算時間含む）")
	@AttendanceItemValue(itemId = 533, type = ValueType.INTEGER)
	private Integer workTimeIncludeVacationTime;

	/** 所定内割増時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "所定内割増時間")
	@AttendanceItemValue(itemId = 558, type = ValueType.INTEGER)
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
		return domain == null ? null
				: new WithinStatutoryTimeDailyPerformDto(domain.getWorkTime().valueAsMinutes(),
						domain.getWorkTimeIncludeVacationTime().valueAsMinutes(),
						domain.getWithinPrescribedPremiumTime().valueAsMinutes(),
						new CalcAttachTimeDto(
								domain.getWithinStatutoryMidNightTime().getTime().getCalcTime().valueAsMinutes(),
								domain.getWithinStatutoryMidNightTime().getTime().getTime().valueAsMinutes()),
						domain.getVacationAddTime().valueAsMinutes());
	}

	public WithinStatutoryTimeOfDaily toDomain() {
		return WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(new AttendanceTime(workTime),
				new AttendanceTime(workTimeIncludeVacationTime), new AttendanceTime(withinPrescribedPremiumTime),
				new WithinStatutoryMidNightTime(TimeWithCalculation.createTimeWithCalculation(
						new AttendanceTime(withinStatutoryMidNightTime.getTime()),
						new AttendanceTime(withinStatutoryMidNightTime.getCalcTime()))),
				new AttendanceTime(vacationAddTime));
	}
}
