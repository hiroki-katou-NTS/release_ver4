package nts.uk.ctx.bs.employee.ws.regpersoninfo.init.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.app.find.init.item.InitValueSetItemFinder;
import nts.uk.ctx.bs.employee.app.find.init.item.SettingItemDto;

@Path("regpersoninfo/init/item")
@Produces(MediaType.APPLICATION_JSON)
public class InitValueSetItemWebService extends WebService {

	@Inject
	private InitValueSetItemFinder finder;

	@POST
	@Path("findInit/{settingId}/{categoryCd}/{baseDate}")
	public List<SettingItemDto> getAllInitItem(@PathParam("settingId") String settingId,
			@PathParam("categoryCd") String categoryCd, @PathParam("baseDate") String baseDate) {
		return this.finder.getAllInitItem(settingId, categoryCd, GeneralDate.fromString(baseDate, "yyyyMMdd"));
	}

}
