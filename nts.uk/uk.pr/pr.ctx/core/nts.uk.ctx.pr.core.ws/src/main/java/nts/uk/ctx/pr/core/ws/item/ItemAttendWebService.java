package nts.uk.ctx.pr.core.ws.item;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemattend.ItemAttendDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemattend.ItemAttendFinder;

@Path("pr/core/itemattend")
@Produces("application/json")
public class ItemAttendWebService extends WebService {
	@Inject
	ItemAttendFinder itemAttendFinder;

	@POST
	@Path("find/{itemCode}")
	public ItemAttendDto findItemAttend(@PathParam("itemCode") String itemCode) {
		return itemAttendFinder.find(itemCode);

	}
}
