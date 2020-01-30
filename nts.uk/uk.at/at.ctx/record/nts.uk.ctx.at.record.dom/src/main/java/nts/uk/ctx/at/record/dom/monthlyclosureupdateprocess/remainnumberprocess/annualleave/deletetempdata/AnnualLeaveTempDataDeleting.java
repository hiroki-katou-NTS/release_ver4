package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.remainnumberprocess.annualleave.deletetempdata;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author HungTT - <<Work>> 年休暫定データ削除
 *
 */

@Stateless
public class AnnualLeaveTempDataDeleting {
	
	// Fix bug 109524
	@Inject
	private InterimRemainRepository tmpAnnualLeaveRepo;

	/**
	 * 年休暫定データ削除
	 * 
	 * @param employeeId
	 * @param period
	 */
	public void deleteTempAnnualLeaveData(String employeeId, DatePeriod period) {
		tmpAnnualLeaveRepo.removeByPeriod(employeeId, period);
	}
}
