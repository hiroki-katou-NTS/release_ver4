package nts.uk.ctx.pr.core.ws.item;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalary.ItemSalaryDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemsalary.ItemSalaryFinder;

@Path("pr/core/itemsalary")
@Produces("application/json")
public class ItemSalaryWebService extends WebService {
	@Inject
	ItemSalaryFinder itemSalaryFinder;

	@POST
	@Path("find/{itemCode}")
	public ItemSalaryDto findItemSalary(@PathParam("itemCode") String itemCode) {
		return itemSalaryFinder.find(itemCode);
	}
}
