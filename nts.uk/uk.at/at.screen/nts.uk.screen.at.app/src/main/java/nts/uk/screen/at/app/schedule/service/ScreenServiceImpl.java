package nts.uk.screen.at.app.schedule.service;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.pub.workrule.closure.PresentClosingPeriodExport;
import nts.uk.ctx.at.shared.pub.workrule.closure.ShClosurePub;

@Stateless
public class ScreenServiceImpl implements ScreenService{

	@Inject
	private ShClosurePub shClosurePub;
	
	@Override
	public Optional<GeneralDate> getProcessingYM(String cId, int closureId) {
		Optional<PresentClosingPeriodExport> e = shClosurePub.find(cId, closureId);
		if(e.isPresent()) {
			return Optional.of(e.get().getClosureEndDate());
		}
		return Optional.empty();
	}

}
