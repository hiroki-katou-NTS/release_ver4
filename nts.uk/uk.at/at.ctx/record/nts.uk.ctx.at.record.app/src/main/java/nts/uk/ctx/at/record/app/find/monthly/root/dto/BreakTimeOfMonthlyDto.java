package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.monthly.verticaltotal.worktime.breaktime.BreakTimeOfMonthly;
import nts.uk.ctx.at.shared.dom.common.times.AttendanceTimesMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の休憩時間 */
public class BreakTimeOfMonthlyDto implements ItemConst {

	/** 休憩時間: 勤怠月間時間 */
	@AttendanceItemLayout(jpPropertyName = TIME, layout = LAYOUT_A)
	@AttendanceItemValue(type = ValueType.TIME)
	private int breakTime;

	/** 所定内休憩時間: 勤怠月間時間 */
	@AttendanceItemLayout(jpPropertyName = WITHIN_STATUTORY, layout = LAYOUT_B)
	@AttendanceItemValue(type = ValueType.TIME)
	private int withinBreakTime;

	/** 所定外休憩時間: 勤怠月間時間 */
	@AttendanceItemLayout(jpPropertyName = EXCESS_STATUTORY, layout = LAYOUT_C)
	@AttendanceItemValue(type = ValueType.TIME)
	private int excessBreakTime;

	/** 所定内休憩時間: 勤怠月間時間 */
	@AttendanceItemLayout(jpPropertyName = COUNT, layout = LAYOUT_D)
	@AttendanceItemValue(type = ValueType.COUNT)
	private int breakTimes;

	/** 所定内控除時間: 勤怠月間時間 */
	@AttendanceItemLayout(jpPropertyName = WITHIN_STATUTORY + DEDUCTION, layout = LAYOUT_E)
	@AttendanceItemValue(type = ValueType.TIME)
	private int withinDeductionTime;

	/** 所定外控除時間: 勤怠月間時間 */
	@AttendanceItemLayout(jpPropertyName = EXCESS_STATUTORY + DEDUCTION, layout = LAYOUT_F)
	@AttendanceItemValue(type = ValueType.TIME)
	private int excessDeductionTime;

	public static BreakTimeOfMonthlyDto from(BreakTimeOfMonthly domain) {
		return domain == null ? null : new BreakTimeOfMonthlyDto(
				domain.getBreakTime() == null ? 0 : domain.getBreakTime().valueAsMinutes(),
				domain.getWithinTime() == null ? 0 : domain.getWithinTime().valueAsMinutes(), 
				domain.getExcessTime() == null ? 0 : domain.getExcessTime().valueAsMinutes(),
				domain.getBreakTimes() == null ? 0 : domain.getBreakTimes().v(),
				domain.getWithinDeductionTime() == null ? 0 : domain.getWithinDeductionTime().valueAsMinutes(),
				domain.getExcessDeductionTime() == null ? 0 : domain.getExcessDeductionTime().valueAsMinutes());
	}
	
	public BreakTimeOfMonthly toDomain() {
		return BreakTimeOfMonthly.of(new AttendanceTimesMonth(breakTimes),
				new AttendanceTimeMonth(breakTime),
				new AttendanceTimeMonth(withinBreakTime),
				new AttendanceTimeMonth(withinDeductionTime),
				new AttendanceTimeMonth(excessBreakTime), 
				new AttendanceTimeMonth(excessDeductionTime));
	}
}
