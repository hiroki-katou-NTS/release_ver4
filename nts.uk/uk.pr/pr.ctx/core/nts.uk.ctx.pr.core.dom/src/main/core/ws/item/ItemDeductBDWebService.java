package nts.uk.ctx.pr.core.ws.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemdeductbd.ItemDeductBDDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemdeductbd.ItemDeductBDFinder;

@Path("pr/core/itemdeductbd")
@Produces("application/json")
public class ItemDeductBDWebService extends WebService {
	@Inject
	ItemDeductBDFinder itemDeductBDFinder;

	@POST
	@Path("find/{itemCode}")
	public List<ItemDeductBDDto> findAllItemDeductDB(@PathParam("itemCode") String itemCode) {

		return this.itemDeductBDFinder.findAll(itemCode);
	}

}
