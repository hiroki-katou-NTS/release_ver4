package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.deviationtime.DivergenceTime;

/** 乖離時間 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DivergenceTimeDto implements ItemConst, AttendanceItemDataGate {

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
	private int no;
	
	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch (path) {
		case DIVERGENCE:
			return Optional.of(ItemValue.builder().value(divergenceTime).valueType(ValueType.TIME));
		case DEDUCTION:
			return Optional.of(ItemValue.builder().value(deductionTime).valueType(ValueType.TIME));
		case (DIVERGENCE + REASON + CODE):
			return Optional.of(ItemValue.builder().value(divergenceReasonCode).valueType(ValueType.CODE));
		case (DIVERGENCE + REASON):
			return Optional.of(ItemValue.builder().value(divergenceReason).valueType(ValueType.TEXT));
		case (DEDUCTION + AFTER):
			return Optional.of(ItemValue.builder().value(divergenceTimeAfterDeduction).valueType(ValueType.TIME));
		default:
			return Optional.empty();
		}
	}
	
	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case DIVERGENCE:
		case DEDUCTION:
		case (DIVERGENCE + REASON + CODE):
		case (DIVERGENCE + REASON):
		case (DEDUCTION + AFTER):
			return PropType.VALUE;
		default:
			return PropType.OBJECT;
		}
	}

	@Override
	public void set(String path, ItemValue value) {
		switch (path) {
		case DIVERGENCE:
			this.divergenceTime = value.valueOrDefault(null);
			break;
		case DEDUCTION:
			this.deductionTime = value.valueOrDefault(null);
			break;
		case (DIVERGENCE + REASON + CODE):
			this.divergenceReasonCode = value.valueOrDefault(null);
			break;
		case (DIVERGENCE + REASON):
			this.divergenceReason = value.valueOrDefault(null);
			break;
		case (DEDUCTION + AFTER):
			this.divergenceTimeAfterDeduction = value.valueOrDefault(null);
			break;
		default:
			break;
		}
	}

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
