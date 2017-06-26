package nts.uk.ctx.pr.core.ws.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalarybd.ItemSalaryBDDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemsalarybd.ItemSalaryBDFinder;

@Path("pr/core/itemsalarybd")
@Produces("application/json")
public class ItemSalaryBDWebService extends WebService {

	@Inject
	ItemSalaryBDFinder itemSalaryBDFinder;

	@POST
	@Path("find/{itemCode}")
	public List<ItemSalaryBDDto> findAllItemSalaryDB(@PathParam("itemCode") String itemCode) {
		return itemSalaryBDFinder.findAll(itemCode);
	}

}
