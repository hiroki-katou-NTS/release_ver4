package nts.uk.ctx.bs.employee.ws.regpersoninfo.init.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.init.item.InitValueSettingItemDto;
import nts.uk.ctx.bs.employee.dom.regpersoninfo.init.item.PerInfoInitValueSetItemFinder;

@Path("regpersoninfo/init/item")
@Produces(MediaType.APPLICATION_JSON)
public class PerInfoInitValueSetItemWebService extends WebService {

	@Inject
	private PerInfoInitValueSetItemFinder finder;

	@POST
	@Path("findInit/{settingId}/{categoryCd}")
	public List<InitValueSettingItemDto> getAllInitItem(@PathParam("settingId") String settingId,
			@PathParam("categoryCd") String categoryCd) {
		return this.finder.getAllInitItem(settingId, categoryCd);
	}

}
