/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.wagetable.WtHead;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.service.WtHeadService;
import nts.uk.ctx.pr.core.dom.wagetable.history.service.WtHistoryService;

/**
 * The Class WtInitCommandHandler.
 */
@Stateless
public class WtInitCommandHandler extends CommandHandlerWithResult<WtInitCommand, WtHistory> {

	/** The wage table head repo. */
	@Inject
	private WtHeadRepository headRepo;

	/** The wage table history repo. */
	@Inject
	private WtHistoryRepository historyRepo;

	/** The history service. */
	@Inject
	private WtHistoryService historyService;

	/** The head service. */
	@Inject
	private WtHeadService headService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected WtHistory handle(CommandHandlerContext<WtInitCommand> context) {
		// Get command.
		WtInitCommand command = context.getCommand();

		// Transfer data
		WtHead header = command.getWageTableHeadDto().toDomain();

		// Validate
		header.validate();
		headService.validateRequiredItem(header);
		headService.checkDuplicateCode(header);

		// Create new history
		WtHistory history = WtHistory.initFromHead(header, new YearMonth(command.getStartMonth()));

		// Validate
		history.validate();
		historyService.validateRequiredItem(history);
		historyService.validateDateRange(history);

		// Insert into db.
		this.headRepo.add(header);
		this.historyRepo.addHistory(history);

		// Return.
		return history;
	}
}
