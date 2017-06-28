package nts.uk.ctx.at.shared.ws.bonuspay;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.app.command.BPTimeItemSettingAddCommand;
import nts.uk.ctx.at.shared.app.command.BPTimeItemSettingAddCommandHandler;
import nts.uk.ctx.at.shared.app.command.BPTimeItemSettingUpdateCommand;
import nts.uk.ctx.at.shared.app.command.BPTimeItemSettingUpdateCommandHandler;
import nts.uk.ctx.at.shared.app.find.bonuspay.BPTimeItemSettingDto;
import nts.uk.ctx.at.shared.app.find.bonuspay.BPTimeItemSettingFinder;

@Path("at/share/bpTimeItemSetting")
@Produces("application/json")
public class BPTimeItemSettingWebService extends WebService {
	@Inject
	private BPTimeItemSettingAddCommandHandler bpTimeItemSettingAddCommandHandler;
	@Inject
	private BPTimeItemSettingUpdateCommandHandler bpTimeItemSettingUpdateCommandHandler;
	@Inject
	private BPTimeItemSettingFinder bpTimeItemSettingFinder;

	@POST
	@Path("getListSetting")
	List<BPTimeItemSettingDto> getListSetting() {
		return bpTimeItemSettingFinder.getListSetting();

	}

	@POST
	@Path("getListSpecialSetting")
	List<BPTimeItemSettingDto> getListSpecialSetting() {
		return bpTimeItemSettingFinder.getListSpecialSetting();
	}

	@POST
	@Path("addListSetting")
	void addListSetting(List<BPTimeItemSettingAddCommand> lstSetting) {
		bpTimeItemSettingAddCommandHandler.handle(lstSetting);
	}

	@POST
	@Path("updateListSetting")
	void updateListSetting(List<BPTimeItemSettingUpdateCommand> lstSetting) {
		bpTimeItemSettingUpdateCommandHandler.handle(lstSetting);
	}

}
