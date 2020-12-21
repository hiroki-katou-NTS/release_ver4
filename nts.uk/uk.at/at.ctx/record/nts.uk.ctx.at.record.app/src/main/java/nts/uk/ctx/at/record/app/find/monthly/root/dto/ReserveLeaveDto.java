package nts.uk.ctx.at.record.app.find.monthly.root.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.common.DayUsedNumberDto;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.ItemConst;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.anno.AttendanceItemLayout;
import nts.uk.ctx.at.record.app.find.monthly.root.common.RsvLeaveRemainingNumberInfoDto;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.ReserveLeave;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.ReserveLeaveRemainingInfo;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.reserveleave.ReserveLeaveUsedNumber;

@Data
/** 積立年休 */
@NoArgsConstructor
@AllArgsConstructor
public class ReserveLeaveDto implements ItemConst {

	/** 使用数 */
	@AttendanceItemLayout(jpPropertyName = USAGE, layout = LAYOUT_A)
	private DayUsedNumberDto usedNumber;

	/** 残数 */
	@AttendanceItemLayout(jpPropertyName = REMAIN, layout = LAYOUT_B)
	private RsvLeaveRemainingNumberInfoDto remainingNumber;

	public static ReserveLeaveDto from(ReserveLeave domain) {

		return domain == null ? null : new ReserveLeaveDto(
						DayUsedNumberDto.from(domain.getUsedNumber()),
						RsvLeaveRemainingNumberInfoDto.from(domain.getRemainingNumberInfo()));

	}

	public ReserveLeave toDomain() {

		return ReserveLeave.of(usedNumber == null ? new ReserveLeaveUsedNumber() : usedNumber.toDomain(),
							remainingNumber == null ? new ReserveLeaveRemainingInfo() : remainingNumber.toReserveDomain());
	}

//	public static ReserveLeaveDto from(ReserveLeave domain) {
//		return domain == null ? null : new ReserveLeaveDto(
//						DayUsedNumberDto.from(domain.getUsedNumber()),
//						RsvLeaveRemainingNumberDto.from(domain.getRemainingNumber()),
//						RsvLeaveRemainingNumberDto.from(domain.getRemainingNumberBeforeGrant()),
//						RsvLeaveRemainingNumberDto.from(domain.getRemainingNumberAfterGrant().orElse(null)),
//						0);
//	}

//	public RealReserveLeave toRealDomain() {
//		return RealReserveLeave.of(
//				usedNumber == null ? new ReserveLeaveUsedNumber() : usedNumber.toDomain(),
//				remainingNumber == null ? new ReserveLeaveRemainingNumber() : remainingNumber.toReserveDomain(),
//				remainingNumberBeforeGrant == null ? new ReserveLeaveRemainingNumber() : remainingNumberBeforeGrant.toReserveDomain(),
//				Optional.ofNullable(remainingNumberAfterGrant == null ? null : remainingNumberAfterGrant.toReserveDomain()));
//	}
}
