/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemRepository;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject;
import nts.uk.ctx.pr.report.dom.wageledger.outputsetting.WLOutputSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class AggregateItemRemoveCommandHandler.
 */
@Stateless
public class AggregateItemRemoveCommandHandler extends CommandHandler<AggregateItemRemoveCommand> {

	/** The repository. */
	@Inject
	private WLAggregateItemRepository repository;

	/** The output setting repo. */
	@Inject
	private WLOutputSettingRepository outputSettingRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<AggregateItemRemoveCommand> context) {
		String companyCode = AppContexts.user().companyCode();
		WLItemSubject itemSubject = context.getCommand().getSubject().toDomain(companyCode);

		// Remove aggregate item.
		this.repository.remove(itemSubject);

		// Remove aggregate item used by output setting.
		this.outputSettingRepo.removeAggregateItemUsed(itemSubject);
	}

}
