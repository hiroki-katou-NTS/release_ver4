package nts.uk.ctx.pr.core.ws.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd.AddItemSalaryBDCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd.AddItemSalaryBDCommandHandler;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd.DeleteItemSalaryBDCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd.DeleteItemSalaryBDCommandHandler;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd.UpdateItemSalaryBDCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemsalarybd.UpdateItemSalaryBDCommandHandler;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemsalarybd.ItemSalaryBDDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemsalarybd.ItemSalaryBDFinder;

@Path("pr/core/itemsalarybd")
@Produces("application/json")
public class ItemSalaryBDWebService extends WebService {

	@Inject
	ItemSalaryBDFinder itemSalaryBDFinder;
	@Inject
	AddItemSalaryBDCommandHandler addHandler;
	@Inject
	DeleteItemSalaryBDCommandHandler deleteHandler;
	@Inject
	UpdateItemSalaryBDCommandHandler updateHandler;

	@POST
	@Path("find/{itemCode}")
	public List<ItemSalaryBDDto> findAllItemSalaryDB(@PathParam("itemCode") String itemCode) {
		return itemSalaryBDFinder.findAll(itemCode);
	}

	@POST
	@Path("add")
	public void addItemSalaryDB(AddItemSalaryBDCommand command) {
		this.addHandler.handle(command);
	}
	@POST
	@Path("delete")
	public void deleteItemSalaryDB(DeleteItemSalaryBDCommand command) {
		this.deleteHandler.handle(command);
	}
	@POST
	@Path("update")
	public void updateItemSalaryDB(UpdateItemSalaryBDCommand command) {
		this.updateHandler.handle(command);
	}
}
