package nts.uk.ctx.at.request.ac.closure;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.common.adapter.closure.PresentClosingPeriodImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.RqClosureAdapter;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;
@Stateless
public class RqClosureAdapterImpl implements RqClosureAdapter{

	@Inject
	private ShClosurePub shClosurePub;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<PresentClosingPeriodImport> getClosureById(String cId, int closureId) {
		// TODO Auto-generated method stub
		Optional<PresentClosingPeriodImport> closure = shClosurePub.find(cId, closureId)
				.map(c-> new PresentClosingPeriodImport(c.getProcessingYm(),
						c.getClosureStartDate(),
						c.getClosureEndDate()));
		return closure;
	}

}
