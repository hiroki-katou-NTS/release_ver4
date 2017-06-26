package nts.uk.ctx.pr.core.ws.item;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemdeduct.ItemDeductDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemdeduct.ItemDeductFinder;

@Path("pr/core/itemdeduct")
@Produces("application/json")
public class ItemDeductWebService extends WebService {
	@Inject
	ItemDeductFinder itemDeductFinder;

	@POST
	@Path("find/{itemCode}")
	public ItemDeductDto findItemDeduct(@PathParam("itemCode") String itemCode) {
		return itemDeductFinder.find(itemCode);

	}
}
