/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.salarydetail.aggregate.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.report.app.salarydetail.aggregate.command.dto.SalaryAggregateItemSaveDto;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItem;
import nts.uk.ctx.pr.report.dom.salarydetail.aggregate.SalaryAggregateItemRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class SalaryAggregateItemSaveCommandHandler.
 */
@Stateless
public class SalaryAggregateItemSaveCommandHandler extends CommandHandler<SalaryAggregateItemSaveCommand> {

	/** The salary aggregate item repository. */
	@Inject
	private SalaryAggregateItemRepository salaryAggregateItemRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<SalaryAggregateItemSaveCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		SalaryAggregateItemSaveDto salaryAggregateItemSaveDto = context.getCommand()
			.getSalaryAggregateItemSaveDto();

		SalaryAggregateItem domain = salaryAggregateItemSaveDto.toDomain(companyCode);
		domain.validate();

		this.salaryAggregateItemRepository.update(domain);
	}

}
