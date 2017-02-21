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
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.service.SocialInsuranceOfficeService;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class RegisterSocialOfficeCommandHandler.
 */
@Stateless
public class RegisterSocialOfficeCommandHandler extends CommandHandler<RegisterSocialOfficeCommand> {

	/** The insurance social service. */
	@Inject
	SocialInsuranceOfficeService insuranceSocialService;

	/** The social insurance office repository. */
	@Inject
	SocialInsuranceOfficeRepository socialInsuranceOfficeRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<RegisterSocialOfficeCommand> context) {
		RegisterSocialOfficeCommand command = context.getCommand();
		// Get the current company code.
		CompanyCode companyCode = new CompanyCode(AppContexts.user().companyCode());

		// convert to domain
		SocialInsuranceOffice socialInsuranceOffice = command.toDomain(companyCode);

		// Validate
		insuranceSocialService.validateRequiredItem(socialInsuranceOffice);
		insuranceSocialService.checkDuplicateCode(socialInsuranceOffice);

		socialInsuranceOfficeRepository.add(socialInsuranceOffice);
		return;
	}
}
