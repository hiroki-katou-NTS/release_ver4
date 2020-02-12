package nts.uk.ctx.at.record.ac.workrule.closure;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.workrule.closure.ClosureAdapter;
import nts.uk.ctx.at.record.dom.adapter.workrule.closure.PresentClosingPeriodImport;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;

@Stateless
public class ClosureAcFinder implements ClosureAdapter {
	
	@Inject 
	private ShClosurePub shClosurePub;

	@Override
	public Optional<PresentClosingPeriodImport> findByClosureId(String cId, int closureId) {
		Optional<PresentClosingPeriodExport> data = shClosurePub.find(cId, closureId);
		if(data.isPresent()) {
			return Optional.of(convertToExport(data.get()));
		}
		return Optional.empty();
	}
	
	private PresentClosingPeriodImport convertToExport(PresentClosingPeriodExport ex) {
		return new PresentClosingPeriodImport(ex.getProcessingYm(), ex.getClosureStartDate(), ex.getClosureEndDate());
	}

}
