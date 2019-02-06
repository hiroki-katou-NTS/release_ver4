package nts.uk.shr.infra.time;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;

@Produces("application/json")
@Path("/server/time")
public class ServerTimeUtilityWebService extends WebService {

	@POST
	@Path("/today")
	public JavaTypeResult<GeneralDate> today() {
		return new JavaTypeResult<GeneralDate>(GeneralDate.today());
	}
	
	@POST
	@Path("/now")
	public JavaTypeResult<GeneralDateTime> now() {
		return new JavaTypeResult<GeneralDateTime>(GeneralDateTime.now());
	}
}
