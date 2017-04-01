/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.command;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearnRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class UpdatePensionAvgearnCommandHandler.
 */
@Stateless
public class UpdatePensionAvgearnCommandHandler extends CommandHandler<UpdatePensionAvgearnCommand> {

	/** The pension avgearn repository. */
	@Inject
	private PensionAvgearnRepository pensionAvgearnRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<UpdatePensionAvgearnCommand> context) {
		// Get command.
		UpdatePensionAvgearnCommand command = context.getCommand();

		// Get the current company code.
		CompanyCode companyCode = new CompanyCode(AppContexts.user().companyCode());

		// Map to domain
		List<PensionAvgearn> updatedPensionAvgearns = command.getListPensionAvgearn().stream().map(item -> {
			return new PensionAvgearn(item);
		}).collect(Collectors.toList());

		// Update
		pensionAvgearnRepository.update(updatedPensionAvgearns, companyCode.v(), command.getOfficeCode());
	}
}
