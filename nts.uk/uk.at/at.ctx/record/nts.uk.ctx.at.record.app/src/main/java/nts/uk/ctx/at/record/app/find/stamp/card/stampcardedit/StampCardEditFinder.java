package nts.uk.ctx.at.record.app.find.stamp.card.stampcardedit;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.stamp.card.stamcardedit.StampCardEditing;
import nts.uk.ctx.at.record.dom.stamp.card.stamcardedit.StampCardEditingRepo;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class StampCardEditFinder {
	
	@Inject
	private StampCardEditingRepo stampCardEditRepo;
	
	public StampCardEditDto findDto() {
		String cId = AppContexts.user().companyId();
		Optional<StampCardEditing> stampCardEditingOpt = Optional.ofNullable(stampCardEditRepo.get(cId));
		
		if (!stampCardEditingOpt.isPresent()) {
			throw new RuntimeException("No data StampCardEditing");
		}
		
		return StampCardEditDto.createFromDomain(stampCardEditingOpt.get());
	}

}
