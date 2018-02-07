package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.ConstraintTime;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTimeOfDaily;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTime;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 日別実績の勤務実績時間 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualWorkTimeDailyPerformDto {

	/** 割増時間: 日別実績の割増時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName = "割増時間", listMaxLength = 10, indexField = "premiumTimeNo")
	private List<PremiumTimeDto> premiumTimes;

	/** 拘束差異時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName = "拘束差異時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer constraintDifferenceTime;

	/** 拘束時間: 総拘束時間 */
	@AttendanceItemLayout(layout = "C", jpPropertyName = "拘束時間")
	private ConstraintTimeDto constraintTime;

	/** 時差勤務時間: 勤怠時間 */
	@AttendanceItemLayout(layout = "D", jpPropertyName = "時差勤務時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer timeDifferenceWorkingHours;

	/** 総労働時間: 日別実績の総労働時間 */
	@AttendanceItemLayout(layout = "E", jpPropertyName = "総労働時間")
	private TotalWorkingTimeDto totalWorkingTime;

	/** 乖離時間: 日別実績の乖離時間 */
	@AttendanceItemLayout(layout = "F", jpPropertyName = "乖離時間", listMaxLength = 5, indexField = "divergenceTimeNo")
	private List<DivergenceTimeDto> divergenceTime;

	public static ActualWorkTimeDailyPerformDto toActualWorkTime(ActualWorkingTimeOfDaily domain) {
		return domain == null ? null : new ActualWorkTimeDailyPerformDto(getPremiumTime(domain.getPremiumTimeOfDailyPerformance()),
						getAttendanceTime(domain.getConstraintDifferenceTime()),
						getConstraintTime(domain.getConstraintTime()),
						getAttendanceTime(domain.getTimeDifferenceWorkingHours()),
						TotalWorkingTimeDto.fromTotalWorkingTime(domain.getTotalWorkingTime()),
						getDivTime(domain.getDivTime()));
	}

	private static List<DivergenceTimeDto> getDivTime(DivergenceTimeOfDaily domain) {
		return domain == null || domain.getDivergenceTime() == null ? new ArrayList<>()
				: ConvertHelper.mapTo(domain.getDivergenceTime(), d -> DivergenceTimeDto.fromDivergenceTime(d));
	}

	private static Integer getAttendanceTime(AttendanceTime domain) {
		return domain == null ? null : domain.valueAsMinutes();
	}

	private static ConstraintTimeDto getConstraintTime(ConstraintTime domain) {
		return domain == null ? null : new ConstraintTimeDto(
						domain.getTotalConstraintTime() == null ? null
								: domain.getTotalConstraintTime().valueAsMinutes(),
						domain.getLateNightConstraintTime() == null ? null
								: domain.getLateNightConstraintTime().valueAsMinutes());
	}

	private static List<PremiumTimeDto> getPremiumTime(PremiumTimeOfDailyPerformance domain) {
		return domain == null || domain.getPremiumTimes() == null ? new ArrayList<>()
				: ConvertHelper.mapTo(domain.getPremiumTimes(),
						c -> new PremiumTimeDto(
								c.getPremitumTime() == null ? null : c.getPremitumTime().valueAsMinutes(),
								c.getPremiumTimeNo()));
	}

	public ActualWorkingTimeOfDaily toDomain() {
		return ActualWorkingTimeOfDaily.of(
					totalWorkingTime == null ? null : totalWorkingTime.toDomain(), 
					constraintTime == null ? null : constraintTime.getLateNightConstraintTime(),
					constraintTime == null ? null : constraintTime.getTotalConstraintTime(),
					constraintDifferenceTime == null ? 0 : constraintDifferenceTime, timeDifferenceWorkingHours,
				new DivergenceTimeOfDaily(divergenceTime == null ? new ArrayList<>()
						: ConvertHelper.mapTo(divergenceTime,
								c -> new DivergenceTime(toAttendanceTime(c.getDivergenceTimeAfterDeduction()),
										toAttendanceTime(c.getDeductionTime()), toAttendanceTime(c.getDivergenceTime()),
										c.getDivergenceTimeNo(),
										c.getDivergenceReason() == null ? null : new DivergenceReasonContent(c.getDivergenceReason()),
										c.getDivergenceReasonCode() == null ? null : new DiverdenceReasonCode(c.getDivergenceReasonCode())))),
				new PremiumTimeOfDailyPerformance(premiumTimes == null ? new ArrayList<>() : ConvertHelper.mapTo(premiumTimes,
						c -> new PremiumTime(c.getPremiumTimeNo(), toAttendanceTime(c.getPremitumTime())))));
	}

	private AttendanceTime toAttendanceTime(Integer value) {
		return value == null ? null : new AttendanceTime(value);
	}
}
