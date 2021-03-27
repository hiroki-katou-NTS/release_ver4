package nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.daynumber;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveUsedNumber;

@Getter
@Setter
public class AnnualLeaveUsedNumber extends LeaveUsedNumber{

//	/**
//	 * 日数
//	 */
//	private AnnualLeaveUsedDayNumber days;
//
//	/**
//	 * 時間
//	 */
//	private Optional<AnnualLeaveUsedTime> minutes;
//
//	/**
//	 * 積み崩し日数
//	 */
//	private Optional<AnnualLeaveUsedDayNumber> stowageDays;

	public AnnualLeaveUsedNumber() {
		super();
	}

	public AnnualLeaveUsedNumber(double days, Integer minutes, Double stowageDays) {
		this.days = new AnnualLeaveUsedDayNumber(days);
		this.minutes = minutes != null ? Optional.of(new AnnualLeaveUsedTime(minutes)) : Optional.empty();
		this.stowageDays = stowageDays != null ? Optional.of(new AnnualLeaveUsedDayNumber(stowageDays))
				: Optional.empty();
	}

	public static AnnualLeaveUsedNumber createFromJavaType(double days, Integer minutes, Double stowageDays) {
		return new AnnualLeaveUsedNumber(days, minutes, stowageDays);
	}
}
