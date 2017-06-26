package nts.uk.ctx.pr.core.ws.item;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd.AddItemDeductBDCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd.AddItemDeductBDCommandHandler;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd.DeleteItemDeductBDCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd.DeleteItemDeductBDCommandHandler;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd.UpdateItemDeductBDCommand;
import nts.uk.ctx.pr.core.app.command.itemmaster.itemdeductbd.UpdateItemDeductBDCommandHandler;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.itemdeductbd.ItemDeductBDDto;
import nts.uk.ctx.pr.core.app.find.itemmaster.itemdeductbd.ItemDeductBDFinder;

@Path("pr/core/itemdeductbd")
@Produces("application/json")
public class ItemDeductBDWebService extends WebService {
	@Inject
	ItemDeductBDFinder itemDeductBDFinder;
	@Inject
	AddItemDeductBDCommandHandler addHandler;
	@Inject
	DeleteItemDeductBDCommandHandler deleteHandler;
	@Inject
	UpdateItemDeductBDCommandHandler updateHandler;

	@POST
	@Path("find/{itemCode}")
	public List<ItemDeductBDDto> findAllItemDeductDB(@PathParam("itemCode") String itemCode) {
		return this.itemDeductBDFinder.findAll(itemCode);
	}

	@POST
	@Path("add")
	public void addItemDeductDB(AddItemDeductBDCommand command) {
		this.addHandler.handle(command);
	}

	@POST
	@Path("delete")
	public void deleteItemDeductDB(DeleteItemDeductBDCommand command) {
		this.deleteHandler.handle(command);
	}

	@POST
	@Path("update")
	public void updateItemDeductDB(UpdateItemDeductBDCommand command) {
		this.updateHandler.handle(command);
	}

}
