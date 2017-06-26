package nts.uk.ctx.pr.formula.ws.systemvariable;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.formula.app.find.formula.SystemVariableDto;
import nts.uk.ctx.pr.formula.app.find.formula.SystemVariableFinder;

@Path("pr/formula/systemvariable")
@Produces("application/json")
public class SystemVariableWebService extends WebService {
	@Inject
	private SystemVariableFinder systemVariableFinder;
	
	@POST
	@Path("getAll")
	public List<SystemVariableDto> getAllSystemVariable() {
		return this.systemVariableFinder.init();
	}
}
