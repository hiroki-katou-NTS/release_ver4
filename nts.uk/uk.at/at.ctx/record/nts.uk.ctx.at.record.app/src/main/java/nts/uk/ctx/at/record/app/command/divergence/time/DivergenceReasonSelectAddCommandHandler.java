package nts.uk.ctx.at.record.app.command.divergence.time;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonSelect;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceReasonSelectRepository;

/**
 * The Class DivergenceReasonSelectAddCommandHandler.
 */
@Stateless
public class DivergenceReasonSelectAddCommandHandler extends CommandHandler<DivergenceReasonSelectAddCommand> {

	/** The div reason select repo. */
	@Inject
	DivergenceReasonSelectRepository divReasonSelectRepo;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<DivergenceReasonSelectAddCommand> context) {
		// get command

		DivergenceReasonSelectAddCommand command = context.getCommand();

		// Convert to Domain

		DivergenceReasonSelect domain = new DivergenceReasonSelect(command);

		// add

		this.divReasonSelectRepo.add(command.getDivergenceTimeNo(),domain);

	}

}
