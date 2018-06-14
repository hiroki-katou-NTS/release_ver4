package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.TimevacationUseTimeOfDaily;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * 日別実績の時間休暇使用時間
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValicationUseDto implements ItemConst {

	/** 時間年休使用時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = ANNUNAL_LEAVE)
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer timeAnnualLeaveUseTime;

	/** 超過有休使用時間 === 60H超休使用時間 */
	@AttendanceItemLayout(layout =LAYOUT_B, jpPropertyName = EXCESS)
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer excessHolidayUseTime;

	/** 特別休暇使用時間 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = SPECIAL)
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer timeSpecialHolidayUseTime;

	/** 時間代休使用時間 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = COMPENSATORY)
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer timeCompensatoryLeaveUseTime;
	
	public TimevacationUseTimeOfDaily toDomain(){
		return new TimevacationUseTimeOfDaily(
						timeAnnualLeaveUseTime == null ? null : new AttendanceTime(timeAnnualLeaveUseTime), 
						timeCompensatoryLeaveUseTime == null ? null : new AttendanceTime(timeCompensatoryLeaveUseTime), 
						excessHolidayUseTime == null ? null : new AttendanceTime(excessHolidayUseTime), 
						timeSpecialHolidayUseTime == null ? null : new AttendanceTime(timeSpecialHolidayUseTime));
	}
}
