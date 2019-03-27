package nts.uk.ctx.at.record.app.command.monthlyclosureupdate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.monthlyclosureupdatelog.MonthlyClosureUpdateLogRepository;

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class UpdateStatus {
	@Inject
	private MonthlyClosureUpdateLogRepository monthlyClosureUpdateRepo;
	
	public void updateStatusForCompany(String companyId) {
	// update status = 2 cho all nhan vien trong cong ty co thoi gian xu ly
			// > 4h ma chua done
			this.monthlyClosureUpdateRepo.updateStatusForCompany(companyId);
	}
}
