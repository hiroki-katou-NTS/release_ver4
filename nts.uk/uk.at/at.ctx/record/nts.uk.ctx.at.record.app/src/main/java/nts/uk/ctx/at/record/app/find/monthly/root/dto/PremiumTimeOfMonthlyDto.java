package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.app.util.attendanceitem.ConvertHelper;
import nts.uk.ctx.at.shared.dom.attendance.util.item.AttendanceItemDataGate;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.verticaltotal.worktime.premiumtime.PremiumTimeOfMonthly;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 月別実績の割増時間 */
public class PremiumTimeOfMonthlyDto implements ItemConst, AttendanceItemDataGate {

	/** 割増時間: 集計割増時間 */
	@AttendanceItemLayout(jpPropertyName = PREMIUM, layout = LAYOUT_A, listMaxLength = 10, indexField = DEFAULT_INDEX_FIELD_NAME)
	private List<AggregatePremiumTimeDto> premiumTimes;

	public static PremiumTimeOfMonthlyDto from(PremiumTimeOfMonthly domain) {
		PremiumTimeOfMonthlyDto dto = new PremiumTimeOfMonthlyDto();
		if(domain != null) {
			dto.setPremiumTimes(ConvertHelper.mapTo(domain.getPremiumTime(), c -> AggregatePremiumTimeDto.from(c.getValue())));
		}
		return dto;
	}
	
	public PremiumTimeOfMonthly toDomain(){
		return PremiumTimeOfMonthly.of(ConvertHelper.mapTo(premiumTimes, c -> c.toDomain()));
	}

	@Override
	public AttendanceItemDataGate newInstanceOf(String path) {
		if(PREMIUM.equals(path)) {
			return new AggregatePremiumTimeDto();
		}
		return AttendanceItemDataGate.super.newInstanceOf(path);
	}

	@Override
	public int size(String path) {
		if(PREMIUM.equals(path)) {
			return 10;
		}
		return AttendanceItemDataGate.super.size(path);
	}

	@Override
	public PropType typeOf(String path) {
		switch (path) {
		case PREMIUM:
			return PropType.IDX_LIST;
		default:
			return PropType.OBJECT;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AttendanceItemDataGate> List<T> gets(String path) {
		if(PREMIUM.equals(path)) {
			return (List<T>) premiumTimes;
		}
		return AttendanceItemDataGate.super.gets(path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AttendanceItemDataGate> void set(String path, List<T> value) {
		if(PREMIUM.equals(path)) {
			premiumTimes = (List<AggregatePremiumTimeDto>) value;
		}
	}
}
