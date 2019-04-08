package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.actualworkinghours.ActualWorkingTimeOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.ConstraintTime;
import nts.uk.ctx.at.record.dom.actualworkinghours.TotalWorkingTime;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTimeOfDaily;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTime;
import nts.uk.ctx.at.record.dom.premiumtime.PremiumTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;

/** 日別実績の勤務実績時間 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualWorkTimeDailyPerformDto implements ItemConst {

	/** 割増時間: 日別実績の割増時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = PREMIUM, 
			listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<PremiumTimeDto> premiumTimes;

	/** 拘束差異時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = RESTRAINT + DIFF)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer constraintDifferenceTime;

	/** 拘束時間: 総拘束時間 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = RESTRAINT)
	private ConstraintTimeDto constraintTime;

	/** 時差勤務時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = TIME_DIFF + WORKING_TIME)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer timeDifferenceWorkingHours;

	/** 総労働時間: 日別実績の総労働時間 */
	@AttendanceItemLayout(layout = LAYOUT_E, jpPropertyName = TOTAL_LABOR)
	private TotalWorkingTimeDto totalWorkingTime;

	/** 乖離時間: 日別実績の乖離時間 */
	@AttendanceItemLayout(layout = LAYOUT_F, jpPropertyName = DIVERGENCE, listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<DivergenceTimeDto> divergenceTime;

	@Override
	public ActualWorkTimeDailyPerformDto clone() {
		return new ActualWorkTimeDailyPerformDto(
						premiumTimes == null ? null : premiumTimes.stream().map(t -> t.clone()).collect(Collectors.toList()),
						constraintDifferenceTime,
						constraintTime == null ? null : constraintTime.clone(),
						timeDifferenceWorkingHours,
						totalWorkingTime == null ? null : totalWorkingTime.clone(),
						divergenceTime == null ? null : divergenceTime.stream().map(t -> t.clone()).collect(Collectors.toList()));
	}

	public static ActualWorkTimeDailyPerformDto toActualWorkTime(ActualWorkingTimeOfDaily domain) {
		return domain == null ? null : new ActualWorkTimeDailyPerformDto(
						getPremiumTime(domain.getPremiumTimeOfDailyPerformance()),
						getAttendanceTime(domain.getConstraintDifferenceTime()),
						getConstraintTime(domain.getConstraintTime()),
						getAttendanceTime(domain.getTimeDifferenceWorkingHours()),
						TotalWorkingTimeDto.fromTotalWorkingTime(domain.getTotalWorkingTime()),
						getDivTime(domain.getDivTime()));
	}

	private static List<DivergenceTimeDto> getDivTime(DivergenceTimeOfDaily domain) {
		return domain == null ? new ArrayList<>()
				: ConvertHelper.mapTo(domain.getDivergenceTime(), d -> DivergenceTimeDto.fromDivergenceTime(d));
	}

	private static Integer getAttendanceTime(AttendanceTime domain) {
		return domain == null ? 0 : domain.valueAsMinutes();
	}

	private static ConstraintTimeDto getConstraintTime(ConstraintTime domain) {
		return domain == null ? null : new ConstraintTimeDto(
						domain.getTotalConstraintTime() == null ? 0
								: domain.getTotalConstraintTime().valueAsMinutes(),
						domain.getLateNightConstraintTime() == null ? 0
								: domain.getLateNightConstraintTime().valueAsMinutes());
	}

	private static List<PremiumTimeDto> getPremiumTime(PremiumTimeOfDailyPerformance domain) {
		return domain == null ? new ArrayList<>() : ConvertHelper.mapTo(domain.getPremiumTimes(),
						c -> new PremiumTimeDto(
								c.getPremitumTime() == null ? 0 : c.getPremitumTime().valueAsMinutes(),
								c.getPremiumTimeNo()));
	}

	public ActualWorkingTimeOfDaily toDomain() {
		return ActualWorkingTimeOfDaily.of(
					totalWorkingTime == null ? TotalWorkingTime.createAllZEROInstance() : totalWorkingTime.toDomain(), 
					constraintTime == null || constraintTime.getLateNightConstraintTime() == null ? 0 : constraintTime.getLateNightConstraintTime(),
					constraintTime == null || constraintTime.getTotalConstraintTime() == null ? 0 : constraintTime.getTotalConstraintTime(),
					constraintDifferenceTime == null ? 0 : constraintDifferenceTime, 
					timeDifferenceWorkingHours == null ? 0 : timeDifferenceWorkingHours,
				new DivergenceTimeOfDaily(ConvertHelper.mapTo(divergenceTime,
								c -> new DivergenceTime(toAttendanceTimeWithMinus(c.getDivergenceTimeAfterDeduction()),
										toAttendanceTimeWithMinus(c.getDeductionTime()), toAttendanceTimeWithMinus(c.getDivergenceTime()),
										c.getNo(),
										c.getDivergenceReason() == null ? null : new DivergenceReasonContent(c.getDivergenceReason()),
										c.getDivergenceReasonCode() == null ? null : new DiverdenceReasonCode(c.getDivergenceReasonCode())))),
				new PremiumTimeOfDailyPerformance(ConvertHelper.mapTo(premiumTimes,
										c -> new PremiumTime(c.getNo(), toAttendanceTime(c.getPremitumTime())))));
	}

	private AttendanceTime toAttendanceTime(Integer value) {
		return value == null ? AttendanceTime.ZERO : new AttendanceTime(value);
	}

	private AttendanceTimeOfExistMinus toAttendanceTimeWithMinus(Integer value) {
		return value == null ? AttendanceTimeOfExistMinus.ZERO : new AttendanceTimeOfExistMinus(value);
	}
}
