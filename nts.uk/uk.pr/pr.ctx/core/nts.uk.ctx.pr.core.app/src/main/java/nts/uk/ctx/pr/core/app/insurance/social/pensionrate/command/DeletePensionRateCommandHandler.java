/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionrate.command;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository;

/**
 * The Class DeletePensionCommandHandler.
 */
@Stateless
public class DeletePensionRateCommandHandler extends CommandHandler<DeletePensionRateCommand> {

	/** The pension rate repository. */
	@Inject
	private PensionRateRepository pensionRateRepository;

	/* (non-Javadoc)
	 * @see nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command.CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<DeletePensionRateCommand> command) {
		String historyId = command.getCommand().getHistoryId();
		
		pensionRateRepository.remove(historyId);
		return;
	}
}