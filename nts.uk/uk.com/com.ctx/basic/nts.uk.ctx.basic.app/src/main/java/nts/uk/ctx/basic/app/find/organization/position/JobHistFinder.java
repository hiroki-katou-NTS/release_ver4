package nts.uk.ctx.basic.app.find.organization.position;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.organization.position.PositionRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JobHistFinder {

	@Inject
	private PositionRepository repository;
	
	public List<JobHistDto> init() {
		List<JobHistDto> listHist = this.repository
				.getAllHistory(AppContexts.user().companyCode()).stream()
				.map(c -> JobHistDto.fromDomain(c)).collect(Collectors.toList());
		return listHist;
	}

}
