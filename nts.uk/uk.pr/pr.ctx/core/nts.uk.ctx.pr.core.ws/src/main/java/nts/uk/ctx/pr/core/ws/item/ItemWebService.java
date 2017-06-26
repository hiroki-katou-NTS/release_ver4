package nts.uk.ctx.pr.core.ws.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.item.ItemDto;
import nts.uk.ctx.pr.core.app.find.item.ItemFinder;


@Path("pr/proto/item")
@Produces("application/json")
public class ItemWebService extends WebService {

	@Inject
	private ItemFinder itemFinder;

	@POST
	@Path("findall/bycategory/{categoryAtr}")
	public List<ItemDto> getItemsByCategory(@PathParam("categoryAtr") int categoryAtr){
		return itemFinder.getAllItems(categoryAtr);
	}

	@POST
	@Path("find/{categoryAtr}/{itemCode}")
	public ItemDto getItemsByCategory(@PathParam("categoryAtr") int categoryAtr, @PathParam("itemCode") String itemCode){
		return itemFinder.getItem(categoryAtr, itemCode).get();
	}
	
}
