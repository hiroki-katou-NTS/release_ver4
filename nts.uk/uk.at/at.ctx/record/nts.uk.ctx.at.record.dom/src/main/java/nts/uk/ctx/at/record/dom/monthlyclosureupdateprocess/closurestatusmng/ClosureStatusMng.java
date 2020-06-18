package nts.uk.ctx.at.record.dom.monthlyclosureupdateprocess.closurestatusmng;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagementRepository;

/**
 *
 * @author HungTT - <<Work>> 締め状態管理
 *
 */

@Stateless
public class ClosureStatusMng {

	@Inject
	private ClosureStatusManagementRepository closureSttRepo;

	public void closureStatusManage(AggrPeriodEachActualClosure period, String empId) {
		ClosureStatusManagement statusMng = new ClosureStatusManagement(
				period.getClosureMonth().yearMonth(),
				empId,
				period.getClosureMonth().closureId(),
				period.getClosureMonth().closureDate(),
				period.getPeriod());
		closureSttRepo.add(statusMng);
	}
}
