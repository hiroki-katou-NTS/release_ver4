package nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.empinfo.grantremainingdata;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveGrantDayNumber;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveRemainingDayNumber;
import nts.uk.ctx.at.record.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.daynumber.ReserveLeaveUsedNumber;

@Getter
public class ReserveLeaveNumberInfo {
	
	/**
	 * 付与日数
	 */
	private ReserveLeaveGrantDayNumber grantNumber;
	
	/**
	 * 使用数
	 */
	private ReserveLeaveUsedNumber usedNumber;
	
	/**
	 * 残日数
	 */
	private ReserveLeaveRemainingDayNumber remainingNumber;

}
