/**
 * 
 */
package nts.uk.ctx.sys.assist.ws.manualsetting;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.uk.ctx.sys.assist.app.command.manualsetting.AddManualSettingHandler;
import nts.uk.ctx.sys.assist.app.command.manualsetting.ManualSettingCommand;
import nts.uk.ctx.sys.assist.app.find.manualsetting.ManualSettingDto;
import nts.uk.ctx.sys.assist.app.find.manualsetting.ManualSettingFinder;

/**
 * @author nam.lh
 *
 */
@Path("ctx/sys/assist/app")
@Produces("application/json")
public class ManualSettingWebService {

	@Inject
	private AddManualSettingHandler addMalSet;

	@Inject
	private ManualSettingFinder manualSettingFinder;

	@POST
	@Path("addMalSet")
	public void add(ManualSettingCommand mal) {
		this.addMalSet.handle(mal);
	}

	@POST
	@Path("findAll")
	public List<ManualSettingDto> getAll() {
		return manualSettingFinder.getAll();
	}
}
