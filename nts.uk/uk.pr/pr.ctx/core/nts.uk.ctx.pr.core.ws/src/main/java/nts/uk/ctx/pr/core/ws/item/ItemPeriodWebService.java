package nts.uk.ctx.pr.core.ws.item;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemperiod.ItemPeriodDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemperiod.ItemPeriodFinder;

@Path("pr/core/itemperiod")
@Produces("application/json")
public class ItemPeriodWebService extends WebService {

	@Inject
	private ItemPeriodFinder itemPeriodFinder;

	@POST
	@Path("find/{categoryAtr}/{itemCode}")
	public ItemPeriodDto getItemsByCategory(
			@PathParam("categoryAtr") int categoryAtr,
			@PathParam("itemCode") String itemCode) {
		return itemPeriodFinder.find(categoryAtr, itemCode);
	}

}
