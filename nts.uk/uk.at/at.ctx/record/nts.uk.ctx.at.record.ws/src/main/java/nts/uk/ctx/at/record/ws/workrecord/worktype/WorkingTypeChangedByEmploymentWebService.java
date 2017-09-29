/**
 * 
 */
package nts.uk.ctx.at.record.ws.workrecord.worktype;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.at.record.app.command.workrecord.worktype.WorkTypeEmploymentCommand;
import nts.uk.ctx.at.record.app.command.workrecord.worktype.WorkTypeEmploymentCommandHandler;
import nts.uk.ctx.at.record.app.find.workrecord.worktype.WorkTypeGroupDto;
import nts.uk.ctx.at.record.dom.workrecord.workingtype.WorkingTypeChangedByEmployment;
import nts.uk.ctx.at.record.dom.workrecord.workingtype.WorkingTypeChangedByEmploymentRepoInterface;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author danpv
 *
 */
@Path("at/record/workrecord/worktype/")
@Produces("application/json")
public class WorkingTypeChangedByEmploymentWebService {

	@Inject
	private WorkingTypeChangedByEmploymentRepoInterface worktypeRepo;

	@Inject
	private WorkTypeEmploymentCommandHandler handler;
	
	@GET
	@Path("get/{empCode}")
	public List<WorkTypeGroupDto> findWorkTypeGroups(@PathParam("empCode") String empCode) {
		String companyId = AppContexts.user().companyId();
		WorkingTypeChangedByEmployment workTypeChanged = worktypeRepo
				.getWorkingTypeChangedByEmployment(new CompanyId(companyId), new EmploymentCode(empCode));
		List<WorkTypeGroupDto> groupDtos = new ArrayList<>();
		workTypeChanged.getChangeableWorkTypeGroups().forEach(group -> 
			groupDtos.add(new WorkTypeGroupDto(group.getNo(), group.getName().v(), group.getWorkTypeList()))
		);
		return groupDtos;
	}
	
	@POST
	@Path("register") 
	public void registerWorkType(WorkTypeEmploymentCommand command) {
		handler.handle(command);
	}

}
