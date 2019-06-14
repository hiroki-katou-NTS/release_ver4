/**
 * 
 */
package nts.uk.ctx.sys.assist.ws.deletiondata;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.app.command.JavaTypeResult;
import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting.AddManualSetDelHandler;
import nts.uk.ctx.sys.assist.app.command.deletedata.manualsetting.ManualSetDelCommand;
import nts.uk.ctx.sys.assist.app.find.deletedata.ManualSetDelDto;
import nts.uk.ctx.sys.assist.app.find.deletedata.ManualSetDelFinder;
import nts.uk.ctx.sys.assist.app.find.deletedata.SystemDateDto;

/**
 * @author hiep.th
 *
 */
@Path("ctx/sys/assist/app")
@Produces("application/json")
public class ManualSetDelWebService extends WebService {

	@Inject
	private AddManualSetDelHandler addMalSet;

	@Inject
	private ManualSetDelFinder manualSetDelFinder;

	@POST
	@Path("addManualSetDel")
	public JavaTypeResult<String> add(ManualSetDelCommand mal) {
		return new JavaTypeResult<String>(this.addMalSet.handle(mal));
	}

	@POST
	@Path("findManualSetDel/{delId}")
	public ManualSetDelDto findManualSetDel(@PathParam("delId") String delId) {
		return manualSetDelFinder.findManualSetDelById(delId);
	}
	
	@POST
	@Path("getSystemDate")
	public SystemDateDto getSystemDate() {
		SystemDateDto result = new SystemDateDto(GeneralDate.today());
		return result;
	}
}
