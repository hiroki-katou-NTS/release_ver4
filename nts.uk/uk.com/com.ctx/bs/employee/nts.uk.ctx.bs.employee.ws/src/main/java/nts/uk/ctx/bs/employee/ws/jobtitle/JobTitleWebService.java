package nts.uk.ctx.bs.employee.ws.jobtitle;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.ws.company.organization.jobtitle.Kcp003Dto;
import nts.uk.ctx.bs.employee.app.command.jobtitle.history.SaveJobTitleHistoryCommand;
import nts.uk.ctx.bs.employee.app.command.jobtitle.history.SaveJobTitleHistoryCommandHandler;
import nts.uk.ctx.bs.employee.app.find.jobtitle.JobTitleFinder;
import nts.uk.ctx.bs.employee.app.find.jobtitle.dto.JobTitleFindDto;
import nts.uk.ctx.bs.employee.app.find.jobtitle.dto.JobTitleItemDto;

/**
 * The Class JobTitleWebService.
 */
@Path("bs/employee/jobtitle")
@Produces(MediaType.APPLICATION_JSON)
public class JobTitleWebService extends WebService {

	/** The job title finder. */
	@Inject
	private JobTitleFinder jobTitleFinder;
	
	/** The save job title history command handler. */
	@Inject
	private SaveJobTitleHistoryCommandHandler saveJobTitleHistoryCommandHandler;
	
	@Path("findAll")
	@POST
	public List<JobTitleItemDto> findAll(Kcp003Dto dto) {
		return this.jobTitleFinder.findAll(dto.getBaseDate());
	}
	
	/**
	 * Find by job id.
	 *
	 * @param findObj the find obj
	 * @return the list
	 */
	@Path("history/findByJobId")
	@POST
	public JobTitleFindDto findByJobId(JobTitleFindDto findObj) {
		return this.jobTitleFinder.findJobHistoryByJobId(findObj.getJobTitleId());
	}	
	
	/**
	 * Save history.
	 *
	 * @param command the command
	 */
	@Path("history/save")
	@POST
	public void saveHistory(SaveJobTitleHistoryCommand command) {
		this.saveJobTitleHistoryCommandHandler.handle(command);
	}
}
