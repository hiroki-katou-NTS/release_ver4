package nts.uk.ctx.at.record.app.find.divergencetime;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DivergenceItemSetFinder {
	@Inject
	private DivergenceTimeRepository divTimeRepo;
	//user contexts
	String companyId = AppContexts.user().companyId();
	
	public List<DivergenceItemSetDto> getAllDivReasonByCode(String divTimeId){
		List<DivergenceItemSetDto> lst = this.divTimeRepo.getallItembyCode(companyId,Integer.valueOf(divTimeId))
				.stream()
				.map(c->DivergenceItemSetDto.fromDomain(c))
				.collect(Collectors.toList());
		return lst;
	}
}
