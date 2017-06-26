package nts.uk.ctx.pr.formula.ws.formulahistory;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.pr.formula.app.command.formulahistory.AddFormulaHistoryCommand;
import nts.uk.ctx.pr.formula.app.command.formulahistory.AddFormulaHistoryCommandHandler;
import nts.uk.ctx.pr.formula.app.command.formulahistory.RemoveFormulaHistoryCommand;
import nts.uk.ctx.pr.formula.app.command.formulahistory.RemoveFormulaHistoryCommandHandler;
import nts.uk.ctx.pr.formula.app.command.formulahistory.UpdateFormulaHistoryCommand;
import nts.uk.ctx.pr.formula.app.command.formulahistory.UpdateFormulaHistoryCommandHandler;

@Path("pr/formula/formulaHistory")
@Produces("application/json")
public class FormulaHistoryWebService extends WebService {
	
	@Inject
	private RemoveFormulaHistoryCommandHandler removeFormulaHistoryCommandHandler;
	@Inject
	private AddFormulaHistoryCommandHandler addFormulaHistoryCommandHandler;
	@Inject
	private UpdateFormulaHistoryCommandHandler updateFormulaHistoryCommandHandler;
	
	@POST
	@Path("removeHistory")
	public void removeFormulaMaster (RemoveFormulaHistoryCommand command){
		this.removeFormulaHistoryCommandHandler.handle(command);
	}
	
	@POST
	@Path("addHistory")
	public void addFormulaMaster (AddFormulaHistoryCommand command){
		this.addFormulaHistoryCommandHandler.handle(command);
	}
	
	@POST
	@Path("updateHistory")
	public void updateFormulaMaster (UpdateFormulaHistoryCommand command){
		this.updateFormulaHistoryCommandHandler.handle(command);
	}
}
