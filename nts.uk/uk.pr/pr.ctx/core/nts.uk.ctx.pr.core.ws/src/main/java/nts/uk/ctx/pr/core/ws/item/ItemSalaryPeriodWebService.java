package nts.uk.ctx.pr.core.ws.item;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod.AddItemSalaryPeriodCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod.AddItemSalaryPeriodCommandHandler;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod.DeleteItemSalaryPeriodCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod.DeleteItemSalaryPeriodCommandHandler;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod.UpdateItemSalaryPeriodCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalaryperiod.UpdateItemSalaryPeriodCommandHandler;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalaryperiod.ItemSalaryPeriodDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemsalaryperiod.ItemSalaryPeriodFinder;

@Path("pr/core/itemsalaryperiod")
@Produces("application/json")
public class ItemSalaryPeriodWebService extends WebService {
	@Inject
	ItemSalaryPeriodFinder itemSalaryPeriodFinder;
	@Inject
	AddItemSalaryPeriodCommandHandler addHandler;
	@Inject
	UpdateItemSalaryPeriodCommandHandler updateHandler;
	@Inject
	DeleteItemSalaryPeriodCommandHandler deleteHandler;

	@POST
	@Path("find/{itemCode}")
	public ItemSalaryPeriodDto findItemSalaryPeriod(@PathParam("itemCode") String itemCode) {
		return this.itemSalaryPeriodFinder.find(itemCode);
	}

	@POST
	@Path("add")
	public void addItemSalaryPeriod(AddItemSalaryPeriodCommand command) {
		this.addHandler.handle(command);

	}

	@POST
	@Path("delete")
	public void deleteItemSalaryPeriod(DeleteItemSalaryPeriodCommand command) {
		this.deleteHandler.handle(command);

	}

	@POST
	@Path("update")
	public void updateItemSalaryPeriod(UpdateItemSalaryPeriodCommand command) {
		this.updateHandler.handle(command);

	}
}
