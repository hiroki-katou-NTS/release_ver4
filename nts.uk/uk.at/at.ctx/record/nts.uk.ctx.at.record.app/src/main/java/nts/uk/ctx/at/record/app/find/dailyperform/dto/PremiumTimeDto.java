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
	private Integer no;

	@Override
	public PremiumTimeDto clone() {
		return new PremiumTimeDto(premitumTime, premiumAmount, no);
	}

	@Override
	public Optional<ItemValue> valueOf(String path) {
		if (PREMIUM.equals(path)) {
			return Optional.of(ItemValue.builder().value(premitumTime).valueType(ValueType.TIME));
		}
		return AttendanceItemDataGate.super.valueOf(path);
	}

	@Override
	public void set(String path, ItemValue value) {
		if (PREMIUM.equals(path)) {
			this.premitumTime = value.valueOrDefault(null);
		}
	}
	
	@Override
	public PropType typeOf(String path) {
		if (PREMIUM.equals(path)) {
			return PropType.VALUE;
		}
		return PropType.OBJECT;
	}
	
	
	
	public PremiumTime toDomain() {
		return new PremiumTime(this.no, new AttendanceTime(this.premitumTime), new AttendanceAmountDaily(this.premiumAmount));
	}
	
	public static PremiumTimeDto valueOf(PremiumTime domain) {
		return new PremiumTimeDto(
				domain.getPremitumTime().valueAsMinutes(),
				domain.getPremiumAmount().v(),
				domain.getPremiumTimeNo());
	}
}
