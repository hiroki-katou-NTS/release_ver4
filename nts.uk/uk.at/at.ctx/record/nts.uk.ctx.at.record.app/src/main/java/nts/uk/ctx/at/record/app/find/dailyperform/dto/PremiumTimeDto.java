package nts.uk.ctx.at.record.app.find.dailyperform.dto;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.common.amount.AttendanceAmountDaily;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.premiumtime.PremiumTime;

/** 割増時間 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PremiumTimeDto implements ItemConst, AttendanceItemDataGate {

	/** 割増時間: 勤怠時間 */
	@AttendanceItemLayout(layout = LAYOUT_A, jpPropertyName = PREMIUM)
	@AttendanceItemValue(type = ValueType.TIME)
	private Integer premitumTime;
	
	/** 割増金額: 勤怠日別金額 */
	@AttendanceItemLayout(layout = LAYOUT_B, jpPropertyName = PREMIUM + AMOUNT)
	@AttendanceItemValue(type = ValueType.AMOUNT_NUM)
	private Integer premiumAmount;

	/** 割増時間NO: 割増時間NO */
	private int no;

	@Override
	public PremiumTimeDto clone() {
		return new PremiumTimeDto(premitumTime, premiumAmount, no);
	}

	@Override
	public Optional<ItemValue> valueOf(String path) {
		switch(path) {
		case PREMIUM:
			return Optional.of(ItemValue.builder().value(premitumTime).valueType(ValueType.TIME));
		case (PREMIUM + AMOUNT):
			return Optional.of(ItemValue.builder().value(premiumAmount).valueType(ValueType.TIME));
		default:
			return AttendanceItemDataGate.super.valueOf(path);
		}
	}

	@Override
	public void set(String path, ItemValue value) {
		switch(path) {
		case PREMIUM:
			this.premitumTime = value.valueOrDefault(0);
			break;
		case (PREMIUM + AMOUNT):
			this.premiumAmount = value.valueOrDefault(0);
			break;
		default:
			break;
		}
	}
	
	@Override
	public PropType typeOf(String path) {
		switch(path) {
		case PREMIUM:
		case (PREMIUM + AMOUNT):
			return PropType.VALUE;
		default:
			return PropType.OBJECT;
		}
	}
	
	public PremiumTime toDomain() {
		return new PremiumTime(
				this.no,
				this.premitumTime == null ? AttendanceTime.ZERO : new AttendanceTime(this.premitumTime),
				this.premiumAmount == null ? AttendanceAmountDaily.ZERO : new AttendanceAmountDaily(this.premiumAmount));
	}
	
	public static PremiumTimeDto valueOf(PremiumTime domain) {
		return new PremiumTimeDto(
				domain.getPremitumTime().valueAsMinutes(),
				domain.getPremiumAmount().v(),
				domain.getPremiumTimeNo());
	}
}
