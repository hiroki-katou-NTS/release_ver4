package nts.uk.ctx.basic.ws.organization.employment;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.basic.app.command.organization.employment.CreateEmploymentCommand;
import nts.uk.ctx.basic.app.command.organization.employment.CreateEmploymentCommandHandler;
import nts.uk.ctx.basic.app.command.organization.employment.DeleteEmploymentCommand;
import nts.uk.ctx.basic.app.command.organization.employment.DeleteEmploymentCommandHandler;
import nts.uk.ctx.basic.app.command.organization.employment.UpdateEmploymentCommand;
import nts.uk.ctx.basic.app.command.organization.employment.UpdateEmploymentCommandHandler;
import nts.uk.ctx.basic.app.find.organization.employment.EmploymentDto;
import nts.uk.ctx.basic.app.find.organization.employment.EmploymentFinder;

@Path("basic/organization/employment")
@Produces("application/json")
public class EmploymentWebservice extends WebService {
	@Inject
	private CreateEmploymentCommandHandler createEmployment;
	@Inject
	private UpdateEmploymentCommandHandler updateEmployment;
	@Inject
	private DeleteEmploymentCommandHandler deleteEmployment;
	@Inject
	private EmploymentFinder finder;
	
	@POST
	@Path("findallemployments")
	public List<EmploymentDto> getAllEmployments(){
		return this.finder.getAllEmployment();
	}
	@POST
	@Path("createemployment")
	public void createEmployment(CreateEmploymentCommand command){
		this.createEmployment.handle(command);
	}
	@POST
	@Path("updateemployment")
	public void updateEmployment(UpdateEmploymentCommand command){
		this.updateEmployment.handle(command);
	}
	@POST
	@Path("deleteemployment")
	public void deleteEmployment(DeleteEmploymentCommand command){
		this.deleteEmployment.handle(command);
	}
	@POST
	@Path("findemploymentbycode/{employmentCode}")
	public void getEmploymentByCode(@PathParam("employmentCode") String employmentCode){
		this.finder.getEmployment(employmentCode);
	}
}
