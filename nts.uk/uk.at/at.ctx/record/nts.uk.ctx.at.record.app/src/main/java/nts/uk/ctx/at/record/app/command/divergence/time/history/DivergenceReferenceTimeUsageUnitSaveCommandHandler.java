package nts.uk.ctx.at.record.app.command.divergence.time.history;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeUsageUnit;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceReferenceTimeUsageUnitRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceReferenceTimeUsageUnitSaveCommandHandler.
 */
@Stateless
public class DivergenceReferenceTimeUsageUnitSaveCommandHandler
		extends CommandHandler<DivergenceReferenceTimeUsageUnitCommand> {

	/** The repository. */
	@Inject
	private DivergenceReferenceTimeUsageUnitRepository divergenceRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<DivergenceReferenceTimeUsageUnitCommand> context) {

		/* Get company ID */
		String companyId = AppContexts.user().companyId();

		// get command
		DivergenceReferenceTimeUsageUnitCommand command = context.getCommand();

		// find unit setting
		Optional<DivergenceReferenceTimeUsageUnit> opt = this.divergenceRepo.findByCompanyId(companyId);

		// update
		if (opt.isPresent()) {
			DivergenceReferenceTimeUsageUnit domain = opt.get();
			domain.setWorkTypeUseSet(command.getWorkTypeUseSet());
			this.divergenceRepo.update(domain);
		}
	}

}
