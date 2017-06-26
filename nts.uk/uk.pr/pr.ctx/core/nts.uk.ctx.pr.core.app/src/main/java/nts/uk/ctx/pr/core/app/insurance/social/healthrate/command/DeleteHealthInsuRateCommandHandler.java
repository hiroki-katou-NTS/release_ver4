/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.command;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateRepository;

/**
 * The Class DeleteHealthInsuranceCommandHandler.
 */
@Stateless
public class DeleteHealthInsuRateCommandHandler extends CommandHandler<DeleteHealthInsuRateCommand> {

	/** The health insurance rate repository. */
	@Inject
	private HealthInsuranceRateRepository healthInsuranceRateRepository;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<DeleteHealthInsuRateCommand> command) {
		String historyId = command.getCommand().getHistoryId();
		
		healthInsuranceRateRepository.remove(historyId);
		return;
	}
}