package nts.uk.ctx.bs.person.ws.person.setting.init;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import command.person.setting.init.CopyInitValueSetCommand;
import command.person.setting.init.CopyInitValueSetCommandHandler;
import command.person.setting.init.DeleteInitValueSettingCommand;
import command.person.setting.init.DeleteInitValueSettingHandler;
import command.person.setting.init.InsertInitValueSettingCommand;
import command.person.setting.init.InsertInitValueSettingHandler;
import find.person.setting.init.PerInfoInitValueSettingDto;
import find.person.setting.init.PerInfoInitValueSettingFinder;
import nts.arc.layer.ws.WebService;

@Path("ctx/bs/person/info/setting/init")
@Produces("application/json")
public class PerInfoInitValueSettingWebservice extends WebService {
	@Inject
	private PerInfoInitValueSettingFinder finder;

	@Inject
	private InsertInitValueSettingHandler add;

	@Inject
	private CopyInitValueSetCommandHandler copyInitValue;

	@Inject
	private DeleteInitValueSettingHandler delete;

	@POST
	@Path("findAll")
	public List<PerInfoInitValueSettingDto> getAllInitValueSetting() {
		return this.finder.getAllInitValueSetting();
	}

	@POST
	@Path("add")
	public void add(InsertInitValueSettingCommand command) {
		this.add.handle(command);
	}

	@POST
	@Path("delete")
	public void delete(DeleteInitValueSettingCommand command) {
		this.delete.handle(command);
	}

	// sonnlb

	@POST
	@Path("findAllHasChild")
	public List<PerInfoInitValueSettingDto> getAllInitValueSettingHasChild() {
		return this.finder.getAllInitValueSettingHasChild();
	}

	// sonnlb

	// hoatt
	@POST
	@Path("copyInitValue")
	public void copyInitValueCtg(CopyInitValueSetCommand command) {
		this.copyInitValue.handle(command);
	}
}
