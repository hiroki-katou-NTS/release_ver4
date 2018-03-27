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
public class DivergenceReasonSelectAddCommandHandler extends CommandHandler<DivergenceReasonSelectAddCommand> {

	/** The div reason select repo. */
	@Inject
	DivergenceReasonSelectRepository divReasonSelectRepo;

	@Override
	protected void handle(CommandHandlerContext<DivergenceReasonSelectAddCommand> context) {
		// get command
		DivergenceReasonSelectAddCommand command = context.getCommand();

		// Convert to Domain
		DivergenceReasonSelect domain = new DivergenceReasonSelect(command);

		// add
		this.divReasonSelectRepo.add(command.getDivergenceTimeNo(), domain);

	}

}
