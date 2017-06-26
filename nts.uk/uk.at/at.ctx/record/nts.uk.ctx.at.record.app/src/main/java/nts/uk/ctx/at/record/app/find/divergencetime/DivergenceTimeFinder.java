package nts.uk.ctx.at.record.app.find.divergencetime;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DivergenceTimeFinder {

	@Inject
	private DivergenceTimeRepository divTimeRepo;
	
	public List<DivergenceTimeDto> getAllDivTime(){
		String companyId = AppContexts.user().companyId();
		List<DivergenceTimeDto> lst = this.divTimeRepo.getAllDivTime(companyId).stream()
				.map(c->DivergenceTimeDto.fromDomain(c))
				.collect(Collectors.toList());
		return lst;
	}
}
