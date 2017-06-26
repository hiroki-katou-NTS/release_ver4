/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.rule.employment.unitprice.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistoryRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceHistoryService;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceService;

/**
 * The Class CreateUnitPriceHistoryCommandHandler.
 */
@Stateless
public class CreateUnitPriceHistoryCommandHandler
		extends CommandHandlerWithResult<CreateUnitPriceHistoryCommand, UnitPriceHistory> {

	/** The unit price history repository. */
	@Inject
	private UnitPriceHistoryRepository unitPriceHistoryRepository;

	/** The unit price repository. */
	@Inject
	private UnitPriceRepository unitPriceRepository;

	/** The unit price history service. */
	@Inject
	private UnitPriceHistoryService unitPriceHistoryService;

	/** The unit price service. */
	@Inject
	private UnitPriceService unitPriceService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected UnitPriceHistory handle(CommandHandlerContext<CreateUnitPriceHistoryCommand> context) {
		// Get command.
		CreateUnitPriceHistoryCommand command = context.getCommand();
		command.setId(IdentifierUtil.randomUniqueId());

		// Transfer data
		UnitPrice unitPrice = new UnitPrice(command);
		UnitPriceHistory unitPriceHistory = new UnitPriceHistory(command);

		// Validate
		unitPriceHistory.validate();
		unitPrice.validate();
		this.unitPriceHistoryService.validateRequiredItem(unitPriceHistory);
		this.unitPriceService.validateRequiredItem(unitPrice);
		this.unitPriceService.checkDuplicateCode(unitPrice);
		this.unitPriceHistoryService.validateDateRange(unitPriceHistory);

		// Persit.
		this.unitPriceRepository.add(unitPrice);
		this.unitPriceHistoryRepository.addHistory(unitPriceHistory);

		// Ret.
		return unitPriceHistory;
	}

}
