package nts.uk.ctx.at.record.app.command.divergence.time.reason;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelect;
import nts.uk.ctx.at.record.dom.divergence.time.reason.DivergenceReasonSelectRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceReasonSelectSaveCommandHandler.
 */
@Stateless
public class DivergenceReasonSelectSaveCommandHandler extends CommandHandler<DivergenceReasonSelectSaveCommand> {

	/** The div reason select repo. */
	@Inject
	DivergenceReasonSelectRepository divReasonSelectRepo;

	@Override
	protected void handle(CommandHandlerContext<DivergenceReasonSelectSaveCommand> context) {
		// get command

		DivergenceReasonSelectSaveCommand command = context.getCommand();

		// convert to domain
		DivergenceReasonSelect domain = new DivergenceReasonSelect(command);

		Optional<DivergenceReasonSelect> domainFound = this.divReasonSelectRepo.findReasonInfo(
				command.getDivergenceTimeNo(), AppContexts.user().companyId(),
				command.getDivergenceReasonCode().toString());
		if (domainFound.isPresent()) {
			// Save
			this.divReasonSelectRepo.update(command.getDivergenceTimeNo(), domain);
		} else
			// add
			this.divReasonSelectRepo.add(command.getDivergenceTimeNo(), domain);

	}

}
