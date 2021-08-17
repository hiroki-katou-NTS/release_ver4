package nts.uk.ctx.exio.ws.input;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.exio.app.input.command.setting.RemoveExternalImportSettingCommand;
import nts.uk.ctx.exio.app.input.command.setting.RemoveExternalImportSettingCommandHandler;
import nts.uk.ctx.exio.app.input.find.setting.ExternalImportSettingDto;
import nts.uk.ctx.exio.app.input.find.setting.ExternalImportSettingFinder;
import nts.uk.ctx.exio.app.input.find.setting.ExternalImportSettingListItemDto;
import nts.uk.ctx.exio.app.input.find.setting.FindExternalImportSettingLayoutQuery;

@Path("exio/input/setting")
@Produces("application/json")
public class ExternalImportSettingWebService extends WebService {
	
	@Inject
	private GetExternalImportSetting finder;
	
	@Inject
	private RemoveExternalImportSettingCommandHandler removeCmd;
	
	@POST
	@Path("find-all")
	public List<ExternalImportSettingListItemDto> findAll() {
		List<ExternalImportSettingListItemDto> result = finder.findAll();
		return result;
	}
	
	@POST
	@Path("find/{settingCode}")
	public ExternalImportSettingDto find(@PathParam("settingCode") String settingCode) {
		ExternalImportSettingDto result = finder.find(settingCode);
		return result;
	}
	
	@POST
	@Path("remove")
	public void remove(RemoveExternalImportSettingCommand command) {
		removeCmd.handle(command);
	}
}
