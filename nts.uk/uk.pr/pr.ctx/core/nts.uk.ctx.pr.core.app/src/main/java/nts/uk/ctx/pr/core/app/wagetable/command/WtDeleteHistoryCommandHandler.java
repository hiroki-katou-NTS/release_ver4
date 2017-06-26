/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryRepository;

/**
 * The Class WageTableHistoryDeleteCommandHandler.
 */
@Stateless
@Transactional
public class WtDeleteHistoryCommandHandler extends CommandHandler<WtDeleteHistoryCommand> {

	/** The wage table history repo. */
	@Inject
	private WtHistoryRepository wageTableHistoryRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<WtDeleteHistoryCommand> context) {
		// Get command
		WtDeleteHistoryCommand command = context.getCommand();

		// Delete history.
		this.wageTableHistoryRepo.deleteHistory(command.getHistoryId());
	}

}
