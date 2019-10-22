/**
 * 
 */
package nts.uk.ctx.hr.develop.ws.databeforereflecting;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.uk.ctx.hr.develop.app.databeforereflecting.command.DataBeforeReflectCommand;
import nts.uk.ctx.hr.develop.app.databeforereflecting.command.PreCheckCommandHandler;
import nts.uk.ctx.hr.develop.app.databeforereflecting.command.RegisterNewEmpCommandHandler;
import nts.uk.ctx.hr.develop.app.databeforereflecting.find.CheckStatusRegistration;
import nts.uk.ctx.hr.develop.app.databeforereflecting.find.DataBeforeReflectDto;
import nts.uk.ctx.hr.develop.app.databeforereflecting.find.DatabeforereflectingFinder;

@Path("databeforereflecting")
@Produces(MediaType.APPLICATION_JSON)
public class DataBeforeReflectingPerInfoWS {

	@Inject
	private DatabeforereflectingFinder finder;
	
	@Inject
	private CheckStatusRegistration checkStatusRegis;
	
	@Inject
	private RegisterNewEmpCommandHandler addCommand;
	
	@Inject
	private PreCheckCommandHandler preCheck;

	@POST
	@Path("/getData")
	public List<DataBeforeReflectDto> getData() {
		List<DataBeforeReflectDto> result = finder.getDataBeforeReflect();
		return result;
	}
	
	@POST
	@Path("/checkStatusRegistration/{sid}")
	public void checkStatusRegistration(@PathParam("sid") String sid) {
		 this.checkStatusRegis.CheckStatusRegistration(sid);
	}
	
	@POST
	@Path("/register/preCheck")
	public void preCheck(DataBeforeReflectCommand command) {
		 this.preCheck.handle(command);
	}
	
	@POST
	@Path("/add")
	public void add(DataBeforeReflectCommand command) {
		this.addCommand.handle(command);
	}

	@POST
	@Path("/update")
	public void update() {
		
	}

	@POST
	@Path("/remove")
	public void remove() {
		
	}
}
