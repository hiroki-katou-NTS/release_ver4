package nts.uk.ctx.pr.formula.app.command.formulahistory;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author nampt
 *
 */
@Stateless
public class RemoveFormulaHistoryCommandHandler extends CommandHandler<RemoveFormulaHistoryCommand>{


	@Inject
	private FormulaHistoryRepository repository;
	
	/**
	 * @ CCD = login company code
	 * @ FORMULA CD = [K _ LBL _ 002]
	 * History ID of the history selected with @HIST_ID = [A_LST_001]
	 */
	@Override
	protected void handle(CommandHandlerContext<RemoveFormulaHistoryCommand> context) {
		RemoveFormulaHistoryCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();
		
		FormulaHistory formulaHistory = new FormulaHistory(
				companyCode,
				new FormulaCode(command.getFormulaCode()),
				command.getHistoryId(),
				new YearMonth(command.getStartDate()),
				new YearMonth(command.getEndDate()));
		
		repository.remove(formulaHistory);		
	}

}
