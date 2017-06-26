package nts.uk.ctx.pr.core.ws.item;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductperiod.AddItemDeductPeriodCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductperiod.AddItemDeductPeriodCommandHandler;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductperiod.UpdateItemDeductPeriodCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductperiod.UpdateItemDeductPeriodCommandHandler;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemdeductperiod.ItemDeductPeriodDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemdeductperiod.ItemDeductPeriodFinder;

@Path("pr/core/itemdeductperiod")
@Produces("application/json")
public class ItemDeductPeriodWebService extends WebService {
	@Inject
	private ItemDeductPeriodFinder itemDeductPeriodFinder;
	@Inject
	AddItemDeductPeriodCommandHandler addHandler;
	@Inject
	UpdateItemDeductPeriodCommandHandler updateHandler;

	@POST
	@Path("find/{itemCode}")
	public ItemDeductPeriodDto findItemDeductPeriod(@PathParam("itemCode") String itemCode) {
		return this.itemDeductPeriodFinder.find(itemCode);

	}

	@POST
	@Path("add")
	public void addItemDeductPeriod(AddItemDeductPeriodCommand command) {
		this.addHandler.handle(command);

	}

	@POST
	@Path("update")
	public void updateItemDeductPeriod(UpdateItemDeductPeriodCommand command) {
		this.updateHandler.handle(command);

	}

}
