/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.command;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository;
import nts.uk.ctx.pr.core.dom.insurance.labor.service.LaborInsuranceOfficeService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class LaborInsuranceOfficeUpdateCommandHandler.
 */
@Stateless
public class LaborInsuranceOfficeUpdateCommandHandler
	extends CommandHandler<LaborInsuranceOfficeUpdateCommand> {

	/** The repository. */
	@Inject
	private LaborInsuranceOfficeRepository repository;

	/** The service. */
	@Inject
	private LaborInsuranceOfficeService service;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<LaborInsuranceOfficeUpdateCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		LaborInsuranceOfficeUpdateCommand command = context.getCommand();

		// to Domain
		LaborInsuranceOffice office = command.toDomain(companyCode);

		// validate
		office.validate();
		service.validateRequiredItem(office);

		// call repository
		this.repository.update(office);
	}

}
