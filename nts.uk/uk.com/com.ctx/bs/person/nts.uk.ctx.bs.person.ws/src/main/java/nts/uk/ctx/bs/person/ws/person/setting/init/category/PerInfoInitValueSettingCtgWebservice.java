package nts.uk.ctx.bs.person.ws.person.setting.init.category;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import find.person.setting.init.PerInfoInitValueSettingFinder;
import find.person.setting.init.PerInitValueSettingDto;
import find.person.setting.init.category.PerInfoInitValueSettingCtgDto;
import find.person.setting.init.category.PerInfoInitValueSettingCtgFinder;
import nts.arc.layer.ws.WebService;

@Path("ctx/bs/person/info/setting/init/ctg")
@Produces("application/json")
public class PerInfoInitValueSettingCtgWebservice extends WebService {

	@Inject
	private PerInfoInitValueSettingCtgFinder cgtFinder;
	
	@Inject
	private PerInfoInitValueSettingFinder finder;

	@POST
	@Path("find/{settingId}")
	public PerInitValueSettingDto getAllInitValueSetting(@PathParam("settingId") String settingId) {
		return this.finder.getAllInitValueSetting(settingId);
	}

	// sonnlb

	@POST
	@Path("findAllBySetId/{settingId}")
	public List<PerInfoInitValueSettingCtgDto> getAllCategoryBySetId(@PathParam("settingId") String settingId) {
		return this.cgtFinder.getAllCategoryBySetId(settingId);
	}

	// sonnlb

}
