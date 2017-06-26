/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.wagetable.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.wagetable.WtHead;
import nts.uk.ctx.pr.core.dom.wagetable.WtHeadRepository;
import nts.uk.ctx.pr.core.dom.wagetable.WtName;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistory;
import nts.uk.ctx.pr.core.dom.wagetable.history.WtHistoryRepository;
import nts.uk.ctx.pr.core.dom.wagetable.history.service.WtHeadService;
import nts.uk.ctx.pr.core.dom.wagetable.history.service.WtHistoryService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.primitive.Memo;

/**
 * The Class WtUpdateCommandHandler.
 */
@Stateless
public class WtUpdateCommandHandler extends CommandHandler<WtUpdateCommand> {

	/** The wt head repo. */
	@Inject
	private WtHeadRepository wtHeadRepo;

	/** The wt history repo. */
	@Inject
	private WtHistoryRepository wtHistoryRepo;

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
	protected void handle(CommandHandlerContext<WtUpdateCommand> context) {
		// Get infos
		WtUpdateCommand command = context.getCommand();

		// Get the company code
		String companyCode = AppContexts.user().companyCode();

		// Get the header info.
		Optional<WtHead> optHeader = this.wtHeadRepo.findByCode(companyCode, command.getCode());

		// Check exist
		if (!optHeader.isPresent()) {
			throw new BusinessException("ER026");
		}

		// Update new data
		WtHead header = optHeader.get();
		header.setName(new WtName(command.getName()));
		header.setMemo(new Memo(command.getMemo()));

		// Validate
		header.validate();
		headService.validateRequiredItem(header);

		Optional<WtHistory> optHistory = this.wtHistoryRepo
				.findHistoryByUuid(command.getWtHistoryDto().getHistoryId());

		// Check exist
		if (!optHistory.isPresent()) {
			throw new BusinessException("ER026");
		}

		// Create history from cmd.
		WtHistory history = command.getWtHistoryDto().toDomain(header.getCode().v());

		// Validate
		history.validate();
		historyService.validateRequiredItem(history);

		// Update into db.
		this.wtHeadRepo.update(header);
		this.wtHistoryRepo.updateHistory(history);
	}

}
