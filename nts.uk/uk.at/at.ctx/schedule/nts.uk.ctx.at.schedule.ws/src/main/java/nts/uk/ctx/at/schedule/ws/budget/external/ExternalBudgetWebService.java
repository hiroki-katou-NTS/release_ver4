package nts.uk.ctx.at.schedule.ws.budget.external;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.schedule.app.command.budget.external.DeleteExternalBudgetCommand;
import nts.uk.ctx.at.schedule.app.command.budget.external.DeleteExternalBudgetCommandHandler;
import nts.uk.ctx.at.schedule.app.command.budget.external.InsertExternalBudgetCommand;
import nts.uk.ctx.at.schedule.app.command.budget.external.InsertExternalBudgetCommandHandler;
import nts.uk.ctx.at.schedule.app.command.budget.external.UpdateExternalBudgetCommand;
import nts.uk.ctx.at.schedule.app.command.budget.external.UpdateExternalBudgetCommandHandler;
import nts.uk.ctx.at.schedule.app.find.budget.external.ExternalBudgetDto;
import nts.uk.ctx.at.schedule.app.find.budget.external.ExternalBudgetFinder;

@Path("at/schedule/budget/external")
@Produces("application/json")
public class ExternalBudgetWebService extends WebService {
	@Inject
	private ExternalBudgetFinder find;

	@Inject
	private InsertExternalBudgetCommandHandler insert;

	@Inject
	private UpdateExternalBudgetCommandHandler update;

	@Inject
	private DeleteExternalBudgetCommandHandler delete;

	@POST
	@Path("findallexternalbudget")
	public List<ExternalBudgetDto> getAllExternalBudget() {
		return this.find.findAll();
	}
	
	@POST
	@Path("insertexternalbudget")
	public void insertbudget(InsertExternalBudgetCommand command) {
		this.insert.handle(command);
	}

	@POST
	@Path("updateexternalbudget")
	public void updateData(UpdateExternalBudgetCommand command) {
		this.update.handle(command);
	}

	@POST
	@Path("deleteexternalbudget")
	public void deleteData(DeleteExternalBudgetCommand command) {
		this.delete.handle(command);
	}

}
