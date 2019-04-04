package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime;
import nts.uk.ctx.at.shared.dom.attendance.util.ItemConst;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.attendance.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;

/** 乖離時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivergenceTimeDto implements ItemConst {

	/** 乖離時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = DIVERGENCE)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer divergenceTime;

	/** 控除時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = DEDUCTION)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer deductionTime;

	/** 乖離理由コード: 乖離理由コード */
	@AttendanceItemLayout(layout = LAYOUT_C, jpPropertyName = DIVERGENCE + REASON + CODE)
	@AttendanceItemValue
	private String divergenceReasonCode;

	/** 乖離理由: 乖離理由 */
	@AttendanceItemLayout(layout = LAYOUT_D, jpPropertyName = DIVERGENCE + REASON)
	@AttendanceItemValue(type = ValueType.TEXT)
	private String divergenceReason;

	/** 控除後乖離時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_E, jpPropertyName = DEDUCTION + AFTER)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer divergenceTimeAfterDeduction;

	/** 乖離時間NO: 乖離時間NO */
	private Integer no;
	
	@Override
	public DivergenceTimeDto clone(){
		return new DivergenceTimeDto(divergenceTime, 
									deductionTime,
									divergenceReasonCode, 
									divergenceReason, 
									divergenceTimeAfterDeduction, 
									no);
	}
	
	public static DivergenceTimeDto fromDivergenceTime(DivergenceTime domain){
		return domain == null ? null : new DivergenceTimeDto(
				getAttendanceTime(domain.getDivTime()), 
				getAttendanceTime(domain.getDeductionTime()),
				!domain.getDivResonCode().isPresent() ? null : domain.getDivResonCode().get().v(), 
				!domain.getDivReason().isPresent() ? null : domain.getDivReason().get().v(), 
				getAttendanceTime(domain.getDivTimeAfterDeduction()), 
				domain.getDivTimeId());
	}

	private static Integer getAttendanceTime(AttendanceTimeOfExistMinus domain) {
		return domain == null ? 0 : domain.valueAsMinutes();
	}
}
