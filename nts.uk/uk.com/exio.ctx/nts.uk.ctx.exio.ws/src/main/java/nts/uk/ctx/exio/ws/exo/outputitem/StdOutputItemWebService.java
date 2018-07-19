package nts.uk.ctx.exio.ws.exo.outputitem;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.exio.app.command.exo.outputitem.AddStdOutItemCommandHandler;
import nts.uk.ctx.exio.app.command.exo.outputitem.RemoveStdOutItemCommandHandler;
import nts.uk.ctx.exio.app.command.exo.outputitem.StdOutItemCommand;
import nts.uk.ctx.exio.app.command.exo.outputitem.UpdateStdOutItemCommandHandler;
import nts.uk.ctx.exio.app.find.exo.item.StdOutItemDto;
import nts.uk.ctx.exio.app.find.exo.item.StdOutputItemFinder;

@Path("exio/exo/outputitem")
@Produces("application/json")
public class StdOutputItemWebService extends WebService {
	@Inject
	private StdOutputItemFinder stdOutputItemFinder;

	@Inject
	private AddStdOutItemCommandHandler addStdOutItemCommandHandler;

	@Inject
	private UpdateStdOutItemCommandHandler updateStdOutItemCommandHandler;

	@Inject
	private RemoveStdOutItemCommandHandler removeStdOutItemCommandHandler;

	@POST
	@Path("getOutItems")
	public List<StdOutItemDto> getOutItem(String condSetCd) {
		return stdOutputItemFinder.getOutItems(condSetCd);
	}

	@POST
	@Path("add")
	public void addOutputItem(StdOutItemCommand comand) {
		this.addStdOutItemCommandHandler.handle(comand);
	}

	@POST
	@Path("update")
	public void updateOutputItem(StdOutItemCommand comand) {
		this.updateStdOutItemCommandHandler.handle(comand);
	}

	@POST
	@Path("remove")
	public void removeOutputItem(StdOutItemCommand comand) {
		this.removeStdOutItemCommandHandler.handle(comand);
	}
}
