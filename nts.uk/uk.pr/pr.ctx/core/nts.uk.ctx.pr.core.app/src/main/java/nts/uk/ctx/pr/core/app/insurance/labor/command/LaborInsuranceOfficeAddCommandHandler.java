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

/**
 * The Class LaborInsuranceOfficeAddCommandHandler.
 */
@Stateless
public class LaborInsuranceOfficeAddCommandHandler extends CommandHandler<LaborInsuranceOfficeAddCommand> {

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
	protected void handle(CommandHandlerContext<LaborInsuranceOfficeAddCommand> context) {
		// user login
		String companyCode = AppContexts.user().companyCode();

		// get command
		LaborInsuranceOfficeAddCommand command = context.getCommand();

		// toDomain
		LaborInsuranceOffice office = command.toDomain(companyCode);

		// validate domain
		office.validate();
		this.service.validateRequiredItem(office);
		this.service.checkDuplicateCode(office);
		
		// add
		this.repository.add(office);
	}

}
