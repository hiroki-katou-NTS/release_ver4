package nts.uk.ctx.at.record.app.find.divergencetime;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.divergencetime.DivergenceTimeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DivergenceItemNameFinder {
	@Inject
	private DivergenceTimeRepository divTimeRepo;
	String companyId = AppContexts.user().companyId();
	public List<DivergenceItemNameDto> getItemSelected(int divTimeId){
		List<DivergenceItemNameDto> lstItemSelected = this.divTimeRepo.getItemSelected(companyId, Integer.valueOf(divTimeId))
										.stream()
										.map(c->DivergenceItemNameDto.fromDomain(c))
										.collect(Collectors.toList());
		return lstItemSelected;
	}
}
