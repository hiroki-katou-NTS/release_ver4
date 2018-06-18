package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.shorttimework.ShortWorkTimeOfDaily;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;

/** 日別実績の短時間勤務時間 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortWorkTimeDto implements ItemConst {

	/** 合計控除時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = DEDUCTION, needCheckIDWithMethod = DEFAULT_CHECK_ENUM_METHOD)
	private TotalDeductionTimeDto totalDeductionTime;

	/** 合計時間 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = TOTAL, needCheckIDWithMethod = DEFAULT_CHECK_ENUM_METHOD)
	private TotalDeductionTimeDto totalTime;

	/** 勤務回数 */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = COUNT, needCheckIDWithMethod = DEFAULT_CHECK_ENUM_METHOD)
	@AttendanceItemValue(type = ValueType.COUNT)
	private Integer times;

	/** 育児介護区分 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = CHILD_CARE_ATTR)
	@AttendanceItemValue(type = ValueType.ATTR)
	private int attr;
	
	public String enumText(){
		switch (this.attr) {
		case 0:
			return E_CHILD_CARE;

		case 1:
			return E_CARE;
		default:
			return EMPTY_STRING;
		}
	}
	
	public static ShortWorkTimeDto toDto(ShortWorkTimeOfDaily domain){
		return domain == null ? null : new ShortWorkTimeDto(
											TotalDeductionTimeDto.getDeductionTime(domain.getTotalDeductionTime()), 
											TotalDeductionTimeDto.getDeductionTime(domain.getTotalTime()), 
											domain.getWorkTimes() == null ? null : domain.getWorkTimes().v(), 
											domain.getChildCareAttribute().value);
	}
	
}
