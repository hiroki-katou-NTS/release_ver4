/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.office.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DeleteSocialOfficeCommandHandler.
 */
@Stateless
public class DeleteSocialOfficeCommandHandler extends CommandHandler<DeleteSocialOfficeCommand> {

	/** The social insurance office repository. */
	@Inject
	private SocialInsuranceOfficeRepository socialInsuranceOfficeRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<DeleteSocialOfficeCommand> command) {
		// Get the current company code.
		String companyCode = AppContexts.user().companyCode();
		String officeCode = command.getCommand().getInsuranceOfficeCode();
		socialInsuranceOfficeRepository.remove(companyCode, officeCode);
		return;
	}
}
