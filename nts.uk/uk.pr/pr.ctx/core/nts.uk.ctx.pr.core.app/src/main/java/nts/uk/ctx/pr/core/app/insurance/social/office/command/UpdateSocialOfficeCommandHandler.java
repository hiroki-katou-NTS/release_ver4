/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.office.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.OfficeCode;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.social.SocialInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.service.SocialInsuranceOfficeService;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class UpdateSocialOfficeCommandHandler.
 */
@Stateless
public class UpdateSocialOfficeCommandHandler extends CommandHandler<UpdateSocialOfficeCommand> {

	/** The social insurance office service. */
	@Inject
	private SocialInsuranceOfficeService socialInsuranceOfficeService;

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
	protected void handle(CommandHandlerContext<UpdateSocialOfficeCommand> context) {

		UpdateSocialOfficeCommand command = context.getCommand();

		// Get the current company code.
		String companyCode = AppContexts.user().companyCode();

		// Convert Dto to Domain
		SocialInsuranceOffice socialInsuranceOffice = command.toDomain(companyCode);
		socialInsuranceOffice.validate();
		// validate
		socialInsuranceOfficeService.validateRequiredItem(socialInsuranceOffice);

		Optional<SocialInsuranceOffice> findOffice = socialInsuranceOfficeRepository
				.findByOfficeCode(companyCode, new OfficeCode(command.getCode()));

		if (findOffice.isPresent()) {
			socialInsuranceOfficeRepository.update(socialInsuranceOffice);
		} else {
			throw new BusinessException("ER010");
		}

		return;
	}

}
