package nts.uk.ctx.at.record.app.find.monthly.root.common;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveUsedDayNumber;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ValueType;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.ReserveLeaveUsedNumber;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeaveRemainDay;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.specialholiday.SpecialLeaveUseDays;

@Data
@NoArgsConstructor
@AllArgsConstructor
/** 積立年休使用数 */
public class DayUsedNumberDto implements ItemConst {

	/** 使用日数 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = DAYS, layout = LAYOUT_A)
	private double usedDays;

	/** 使用日数付与前 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = GRANT + BEFORE, layout = LAYOUT_B)
	private double usedDaysBeforeGrant;

	/** 使用日数付与後 */
	@AttendanceItemValue(type = ValueType.DAYS)
	@AttendanceItemLayout(jpPropertyName = GRANT + AFTER, layout = LAYOUT_C)
	private Double usedDaysAfterGrant;

	public static DayUsedNumberDto from(ReserveLeaveUsedNumber domain){
		return domain == null ? null : new DayUsedNumberDto(
				domain.getUsedDays().v(), 
				domain.getUsedDaysBeforeGrant().v(),
				domain.getUsedDaysAfterGrant().map(c -> c.v()).orElse(null));
	}

	public ReserveLeaveUsedNumber toDomain(){
		return ReserveLeaveUsedNumber.of(
				new ReserveLeaveUsedDayNumber(usedDays), 
				new ReserveLeaveUsedDayNumber(usedDaysBeforeGrant),
				Optional.ofNullable(usedDaysAfterGrant == null ? null : new ReserveLeaveUsedDayNumber(usedDaysAfterGrant)));
	}

//	public static DayUsedNumberDto from(SpecialLeaveUseDays domain){
////		return domain == null ? null : new DayUsedNumberDto(domain.getUseDays().v(), domain.getBeforeUseGrantDays().v(),
////				domain.getAfterUseGrantDays().isPresent() ? domain.getAfterUseGrantDays().get().v() : null);
//		return domain == null ? null : new DayUsedNumberDto(domain.getUseDays().v(), domain.getUseDays().v(),
//				domain.getAfterUseGrantDays().isPresent() ? domain.getAfterUseGrantDays().get().v() : null);
//
//	}

//	public SpecialLeaveUseDays toSpecial(){
////		return new SpecialLeaveUseDays(new SpecialLeaveRemainDay(usedDays), new SpecialLeaveRemainDay(usedDaysBeforeGrant),
////				Optional.ofNullable(usedDaysAfterGrant == null ? null : new SpecialLeaveRemainDay(usedDaysAfterGrant)));
//		return null;
//	}
}
