package nts.uk.ctx.at.schedule.app.command.budget.external;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.i18n.custom.IInternationalization;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.schedule.dom.budget.external.ExternalBudget;
import nts.uk.ctx.at.schedule.dom.budget.external.ExternalBudgetRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
@Transactional
public class InsertExternalBudgetCommandHandler extends CommandHandler<InsertExternalBudgetCommand> {

	@Inject
	private ExternalBudgetRepository budgetRepo;
	@Inject
	private IInternationalization internationalization;

	@Override
	protected void handle(CommandHandlerContext<InsertExternalBudgetCommand> context) {
		// get command
		InsertExternalBudgetCommand command = context.getCommand();
		// convert to server
		ExternalBudget exBudget = command.toDomain();
		// Check exist
		Optional<ExternalBudget> optional = this.budgetRepo.find(AppContexts.user().companyId(),
				command.getExternalBudgetCode());
		if (optional.isPresent()) {
			throw new BusinessException("Msg_3");
		} else {
			// insert process
			budgetRepo.insert(exBudget);
		}
	}

}
