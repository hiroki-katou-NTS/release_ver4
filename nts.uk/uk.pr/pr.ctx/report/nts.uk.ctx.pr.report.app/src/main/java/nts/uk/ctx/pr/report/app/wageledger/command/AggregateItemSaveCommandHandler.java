/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.report.app.wageledger.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItem;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLAggregateItemRepository;
import nts.uk.ctx.pr.report.dom.wageledger.aggregate.WLItemSubject;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class AggregateItemSaveCommandHandler.
 */
@Stateless
public class AggregateItemSaveCommandHandler extends CommandHandler<AggregateItemSaveCommand> {

	/** The repository. */
	@Inject
	private WLAggregateItemRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<AggregateItemSaveCommand> context) {
		val companyCode = AppContexts.user().companyCode();
		val command = context.getCommand();
		
		// Validate item selection
		if (CollectionUtil.isEmpty(command.getSubItems())) {
			throw new BusinessException("ER007");
		}

		// Convert command to Domain.
		WLItemSubject subject = command.getSubject().toDomain(companyCode);
		subject.validate();
		// In case update.
		if (!command.isCreateMode()) {
			// Find aggregate item.
			WLAggregateItem aggregateItem = this.repository.findByCode(subject);
			if (aggregateItem == null) {
				throw new IllegalStateException("Aggegate item is not exist!");
			}

			// Convert to domain.
			aggregateItem = command.toDomain(companyCode);
			// Update.
			this.repository.update(aggregateItem);
			return;
		}

		// In case create.
		// Check duplicate code.
		if (this.repository.isExist(subject)) {
			throw new BusinessException(new RawErrorMessage("入力したコードは既に存在しています。\r\nコードを確認してください。"));
		}

		// Convert to domain.
		val aggregateItem = command.toDomain(companyCode);
		this.repository.create(aggregateItem);
		return;
	}

}
