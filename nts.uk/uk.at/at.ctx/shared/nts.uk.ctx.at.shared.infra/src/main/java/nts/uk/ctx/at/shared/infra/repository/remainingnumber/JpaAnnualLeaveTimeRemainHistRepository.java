package nts.uk.ctx.at.shared.infra.repository.remainingnumber;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveTimeRemainHistRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveTimeRemainingHistory;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.annlea.KrcdtAnnLeaTimeRemainHist;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
public class JpaAnnualLeaveTimeRemainHistRepository extends JpaRepository
		implements AnnualLeaveTimeRemainHistRepository {

	@Override
	public void add(AnnualLeaveTimeRemainingHistory domain) {
		this.commandProxy().insert(KrcdtAnnLeaTimeRemainHist.fromDomain(domain));
	}

}
