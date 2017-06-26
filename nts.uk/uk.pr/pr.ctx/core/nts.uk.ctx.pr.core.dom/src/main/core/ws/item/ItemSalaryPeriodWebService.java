package nts.uk.ctx.pr.core.ws.item;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalaryperiod.ItemSalaryPeriodDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemsalaryperiod.ItemSalaryPeriodFinder;

@Path("pr/core/itemsalaryperiod")
@Produces("application/json")
public class ItemSalaryPeriodWebService extends WebService {
	@Inject
	ItemSalaryPeriodFinder itemSalaryPeriodFinder;


	@POST
	@Path("find/{itemCode}")
	public ItemSalaryPeriodDto findItemSalaryPeriod(@PathParam("itemCode") String itemCode) {
		return this.itemSalaryPeriodFinder.find(itemCode);
	}

}
