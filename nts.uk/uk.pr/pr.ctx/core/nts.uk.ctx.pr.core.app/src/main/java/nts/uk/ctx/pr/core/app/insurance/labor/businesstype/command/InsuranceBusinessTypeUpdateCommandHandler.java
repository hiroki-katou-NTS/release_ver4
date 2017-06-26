/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.labor.businesstype.command;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessType;
import nts.uk.ctx.pr.core.dom.insurance.labor.businesstype.InsuranceBusinessTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * The Class InsuranceBusinessTypeUpdateCommandHandler.
 */
@Stateless
public class InsuranceBusinessTypeUpdateCommandHandler
	extends CommandHandler<InsuranceBusinessTypeUpdateCommand> {

	/** The insurance business type repository. */
	@Inject
	private InsuranceBusinessTypeRepository repository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<InsuranceBusinessTypeUpdateCommand> context) {

		// get login user
		LoginUserContext loginUserContext = AppContexts.user();

		// get command
		InsuranceBusinessTypeUpdateCommand command = context.getCommand();

		// company code by user
		String companyCode = loginUserContext.companyCode();

		// update data
		List<InsuranceBusinessType> data = command.toDomain(companyCode);
		this.repository.update(data);
	}

}
