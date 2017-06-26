package nts.uk.ctx.basic.ws.organization.department;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.organization.department.AddDepartmentCommand;
import nts.uk.ctx.basic.app.command.organization.department.AddDepartmentCommandHandler;
import nts.uk.ctx.basic.app.command.organization.department.UpdateStartDateandEndDateHistoryCommand;
import nts.uk.ctx.basic.app.command.organization.department.RemoveDepartmentCommand;
import nts.uk.ctx.basic.app.command.organization.department.RemoveDepartmentCommandHandler;
import nts.uk.ctx.basic.app.command.organization.department.RemoveHistoryCommandHandler;
import nts.uk.ctx.basic.app.command.organization.department.UpdateDepartmentCommand;
import nts.uk.ctx.basic.app.command.organization.department.UpdateDepartmentCommandHandler;
import nts.uk.ctx.basic.app.command.organization.department.UpdateEndDateByHistoryIdCommandHandler;
import nts.uk.ctx.basic.app.command.organization.department.UpdateStartDateAndEndDateByHistoryIdCommandHandler;
import nts.uk.ctx.basic.app.find.organization.department.DepartmentDto;
import nts.uk.ctx.basic.app.find.organization.department.DepartmentFinder;
import nts.uk.ctx.basic.app.find.organization.department.DepartmentHistoryDto;
import nts.uk.ctx.basic.app.find.organization.department.DepartmentMemoDto;
import nts.uk.ctx.basic.app.find.organization.department.DepartmentQueryResult;
import nts.uk.shr.com.context.AppContexts;

@Path("basic/organization")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentWebService extends WebService {

	@Inject
	private DepartmentFinder departmentFinder;

	@Inject
	private AddDepartmentCommandHandler addDepartmentCommandHandler;

	@Inject
	private UpdateDepartmentCommandHandler updateDepartmentCommandHandler;

	@Inject
	private RemoveDepartmentCommandHandler removeDepartmentCommandHandler;

	@Inject
	private RemoveHistoryCommandHandler removeHistoryCommandHandler;

	@Inject
	private UpdateEndDateByHistoryIdCommandHandler updateHistoryCommandHandler;

	@Inject
	private UpdateStartDateAndEndDateByHistoryIdCommandHandler updateStartAndEnddate;

	@POST
	@Path("getAllhistoryDepartment")
	public List<DepartmentHistoryDto> getAllhistory() {
		String ccd = AppContexts.user().companyCode();
		List<DepartmentHistoryDto> list =  departmentFinder.getAllHistory(ccd);
		return list;
	}

	@POST
	@Path("getalldepartment")
	public DepartmentQueryResult init() {
		String ccd = AppContexts.user().companyCode();
		DepartmentQueryResult i = departmentFinder.handle();
		System.out.println(i);
		return departmentFinder.handle();
	}

	@POST
	@Path("getalldepbyhistid/{historyId}")
	public List<DepartmentDto> getListDepByHistId(@PathParam("historyId") String historyId) {
		String ccd = AppContexts.user().companyCode();
		List<DepartmentDto> list = departmentFinder.findAllByHistory(ccd, historyId);
		return list;
	}

	@POST
	@Path("getmemobyhistid/{historyId}")
	public DepartmentMemoDto getMemoByHistId(@PathParam("historyId") String historyId) {
		String ccd = AppContexts.user().companyCode();
		return departmentFinder.findMemo(ccd, historyId);
	}

	@Path("adddepartment")
	@POST
	public void add(List<AddDepartmentCommand> command) {
		this.addDepartmentCommandHandler.handle(command);
	}

	@Path("updatedepartment")
	@POST
	public void update(List<UpdateDepartmentCommand> command) {
		this.updateDepartmentCommandHandler.handle(command);
	}

	@Path("updateenddate")
	@POST
	public void updateEndDate(List<UpdateDepartmentCommand> command) {
		this.updateDepartmentCommandHandler.handle(command);
	}

	@Path("updateenddatebyhistoryid")
	@POST
	public void updateEndDateByHistId(String command) {
		this.updateHistoryCommandHandler.handle(command);
	}

	@Path("deletehistory")
	@POST
	public void deleteHistory(String command) {
		this.removeHistoryCommandHandler.handle(command);
	}

	@Path("updatestartdateandenddate")
	@POST
	public void updateStartDateandEndDateByHistId(UpdateStartDateandEndDateHistoryCommand command) {
		this.updateStartAndEnddate.handle(command);
	}

	@Path("deletedep")
	@POST
	public void deleteDepartment(RemoveDepartmentCommand command) {
		this.removeDepartmentCommandHandler.handle(command);
	}

}
