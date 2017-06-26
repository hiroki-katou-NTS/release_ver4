/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOffice;
import nts.uk.ctx.pr.core.dom.insurance.labor.LaborInsuranceOfficeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class LaborInsuranceOfficeDeleteCommandHandler.
 */
@Stateless
public class LaborInsuranceOfficeDeleteCommandHandler
	extends CommandHandler<LaborInsuranceOfficeDeleteCommand> {

	/** The labor insurance office repository. */
	@Inject
	private LaborInsuranceOfficeRepository repository;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<LaborInsuranceOfficeDeleteCommand> context) {

		// get user login info
		LoginUserContext loginUserContext = AppContexts.user();

		// get companyCode by user login
		String companyCode = loginUserContext.companyCode();

		// get command
		LaborInsuranceOfficeDeleteCommand command = context.getCommand();

		// call Repository remove
		Optional<LaborInsuranceOffice> data = this.repository.findById(companyCode,
			command.getLaborInsuranceOfficeDeleteDto().getCode());

		// check exist
		if (!data.isPresent()) {
			throw new BusinessException("ER010");
		}

		// call repository
		this.repository.remove(companyCode, command.getLaborInsuranceOfficeDeleteDto().getCode());
	}

}
