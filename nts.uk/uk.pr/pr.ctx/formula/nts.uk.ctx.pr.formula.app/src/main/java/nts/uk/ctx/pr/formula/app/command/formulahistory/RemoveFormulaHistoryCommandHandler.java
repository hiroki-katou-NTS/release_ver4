package nts.uk.ctx.pr.formula.app.command.formulahistory;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryDomainService;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author Nam-PT K screen activity 33 - del
 *
 */
@Stateless
public class RemoveFormulaHistoryCommandHandler extends CommandHandler<RemoveFormulaHistoryCommand> {

	@Inject
	private FormulaHistoryDomainService formulaHistoryDomainService;

	@Override
	protected void handle(CommandHandlerContext<RemoveFormulaHistoryCommand> context) {

		RemoveFormulaHistoryCommand command = context.getCommand();
		String companyCode = AppContexts.user().companyCode();

		formulaHistoryDomainService.remove(command.getDifficultyAtr(), companyCode, command.getFormulaCode(),
				command.getHistoryId(), command.getStartDate());
	}

}
