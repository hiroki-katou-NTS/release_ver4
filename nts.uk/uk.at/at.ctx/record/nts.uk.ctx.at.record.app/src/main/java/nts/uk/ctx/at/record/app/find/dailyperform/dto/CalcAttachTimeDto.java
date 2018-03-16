package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculationMinusExist;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/** 計算付き時間 and 計算付き時間(マイナス有り) */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalcAttachTimeDto {

	/** 計算時間 */
	@AttendanceItemLayout(layout = "B", jpPropertyName="計算時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer calcTime;

	/** 時間 */
	@AttendanceItemLayout(layout = "A", jpPropertyName="時間")
	@AttendanceItemValue(type = ValueType.INTEGER)
	private Integer time;
	
	public static CalcAttachTimeDto toTimeWithCal(TimeWithCalculation time){
		return time == null ? null : new CalcAttachTimeDto(
											time.getCalcTime() == null ? null : time.getCalcTime().valueAsMinutes(), 
											time.getTime() == null ? null : time.getTime().valueAsMinutes());
	}
	
	public static CalcAttachTimeDto toTimeWithCal(TimeWithCalculationMinusExist time) {
		return time == null ? null : new CalcAttachTimeDto(
											time.getCalcTime() == null ? null : time.getCalcTime().valueAsMinutes(), 
											time.getTime() == null ? null : time.getTime().valueAsMinutes());
	}
	
	public TimeWithCalculation createTimeWithCalc() {
		return TimeWithCalculation.createTimeWithCalculation(
										time == null ? null : new AttendanceTime(time), 
										calcTime == null ? null : new AttendanceTime(calcTime));
	}
}
