package nts.uk.ctx.basic.app.find.organization.position;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.basic.dom.organization.position.JobTitle;
import nts.uk.ctx.basic.dom.organization.position.PositionRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JobTitleFinder {

	@Inject
	private PositionRepository repository;
		
	public List<JobTitleDto> findAllPosition(String historyId) {
		String companyCode = AppContexts.user().companyCode();
		return repository.findAllJobTitle(companyCode, historyId)
				.stream().map(e->{return convertToDto(e);}).collect(Collectors.toList());
	}


	private JobTitleDto convertToDto(JobTitle jobTitle) {
		JobTitleDto positionDto = new JobTitleDto();
		positionDto.setCompanyCode(jobTitle.getCompanyCode());
		positionDto.setHistoryId(jobTitle.getHistoryId());
		positionDto.setJobCode(jobTitle.getJobCode().v());
		positionDto.setJobName(jobTitle.getJobName().v());
		positionDto.setPresenceCheckScopeSet(jobTitle.getPresenceCheckScopeSet().value);
		positionDto.setMemo(jobTitle.getMemo().v());
		positionDto.setJobOutCode(jobTitle.getJobOutCode().v());
		positionDto.setHiterarchyOrderCode(jobTitle.getHiterarchyOrderCode().v());
		
		return positionDto;
	}

	
}
