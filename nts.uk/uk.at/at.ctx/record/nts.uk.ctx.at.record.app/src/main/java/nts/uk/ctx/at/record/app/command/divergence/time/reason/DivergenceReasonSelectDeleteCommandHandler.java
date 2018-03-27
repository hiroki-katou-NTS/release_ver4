package nts.uk.ctx.at.record.app.command.divergence.time.reason;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelect;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelectRepository;

/**
 * The Class DivergenceReasonSelectAddCommandHandler.
 */
@Stateless
public class DivergenceReasonSelectDeleteCommandHandler extends CommandHandler<DivergenceReasonSelectDeleteCommand> {

	/** The div reason select repo. */
	@Inject
	DivergenceReasonSelectRepository divReasonSelectRepo;

	@Override
	protected void handle(CommandHandlerContext<DivergenceReasonSelectDeleteCommand> context) {
		// get command
		DivergenceReasonSelectDeleteCommand command = context.getCommand();

		// Convert to domain
		DivergenceReasonSelect domain = new DivergenceReasonSelect(command);

		// Delete
		this.divReasonSelectRepo.delete(command.getDivergenceTimeNo(), domain);

	}

}
