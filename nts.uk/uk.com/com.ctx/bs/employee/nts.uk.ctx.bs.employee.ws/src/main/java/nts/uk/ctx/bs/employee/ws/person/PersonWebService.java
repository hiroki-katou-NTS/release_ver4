package nts.uk.ctx.bs.employee.ws.person;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.bs.employee.app.find.person.info.PersonDto;
import nts.uk.ctx.bs.employee.app.find.person.info.PersonFinder;

@Path("bs/employee/person")
@Produces(MediaType.APPLICATION_JSON)
public class PersonWebService extends WebService {
	@Inject
	private PersonFinder personFinder;

	@POST
	@Path("findByEmployeeId/{employeeId}")
	public PersonDto findByEmployeeId(@PathParam("employeeId") String employeeId) {
		return personFinder.getPersonByEmpId(employeeId);
	}
}