/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.app.command.ot.autocalsetting.wkpjob;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.wkpjob.WkpJobAutoCalSettingRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class RemoveWkpJobAutoCalSetCommandHandler.
 */
@Stateless
@Transactional
public class RemoveWkpJobAutoCalSetCommandHandler extends CommandHandler<RemoveWkpJobAutoCalSetCommand> {

	/** The wkp job auto cal setting repository. */
	@Inject
	private WkpJobAutoCalSettingRepository wkpJobAutoCalSettingRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	protected void handle(CommandHandlerContext<RemoveWkpJobAutoCalSetCommand> context) {
		String companyId = AppContexts.user().companyId();
		RemoveWkpJobAutoCalSetCommand command = context.getCommand();
		wkpJobAutoCalSettingRepository.delete(companyId, command.getWkpId(), command.getJobId());
	}
}
