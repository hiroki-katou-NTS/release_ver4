package nts.uk.ctx.basic.app.find.organization.position;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.organization.position.PositionRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JobRefAuthFinder {
	@Inject
	private PositionRepository positionRepo;

	/**
	 * get all authorization
	 * 
	 * @return
	 */
	public List<JobRefAuthDto> getAllRefAuth(String historyId, String jobCode) {
		List<JobRefAuthDto> listRefAuth = this.positionRepo
				.getAllAuth(AppContexts.user().companyCode(), historyId, jobCode, "KT").stream()
				.map(c -> JobRefAuthDto.fromDomain(c)).collect(Collectors.toList());
		return listRefAuth;
	}
}
