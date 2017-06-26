/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.rule.employment.unitprice.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPrice;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistoryRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceHistoryService;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.service.UnitPriceService;

/**
 * The Class UpdateUnitPriceHistoryCommandHandler.
 */
@Stateless
public class UpdateUnitPriceHistoryCommandHandler extends CommandHandler<UpdateUnitPriceHistoryCommand> {

	/** The unit price history repo. */
	@Inject
	private UnitPriceHistoryRepository unitPriceHistoryRepo;

	/** The unit price repo. */
	@Inject
	private UnitPriceRepository unitPriceRepo;

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
	protected void handle(CommandHandlerContext<UpdateUnitPriceHistoryCommand> context) {
		// Get command.
		UpdateUnitPriceHistoryCommand command = context.getCommand();

		// Update history.
		Optional<UnitPriceHistory> optUnitPriceHistory = this.unitPriceHistoryRepo.findHistoryByUuid(command.getId());
		UnitPriceHistory history = optUnitPriceHistory.get();
		history.setBudget(command.getBudget());
		history.setFixPaySettingType(command.getFixPaySettingType());
		history.setFixPayAtr(command.getFixPayAtr());
		history.setFixPayAtrDaily(command.getFixPayAtrDaily());
		history.setFixPayAtrDayMonth(command.getFixPayAtrDayMonth());
		history.setFixPayAtrHourly(command.getFixPayAtrHourly());
		history.setFixPayAtrMonthly(command.getFixPayAtrMonthly());
		history.setMemo(command.getMemo());

		// Update unit price.
		Optional<UnitPrice> optUnitPrice = this.unitPriceRepo.findByCode(history.getCompanyCode(), history.getUnitPriceCode());
		UnitPrice unitPrice = optUnitPrice.get();
		unitPrice.setName(command.getName());

		// Validate
		history.validate();
		unitPrice.validate();
		this.unitPriceService.validateRequiredItem(unitPrice);
		this.unitPriceHistoryService.validateRequiredItem(history);

		// Update to db.
		this.unitPriceRepo.update(unitPrice);
		this.unitPriceHistoryRepo.updateHistory(history);
	}
}
