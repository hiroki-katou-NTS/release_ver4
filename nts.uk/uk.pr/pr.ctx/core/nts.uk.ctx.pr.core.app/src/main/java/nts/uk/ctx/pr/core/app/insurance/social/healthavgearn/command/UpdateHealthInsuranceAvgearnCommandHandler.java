/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class UpdateHealthInsuranceAvgearnCommandHandler.
 */
@Stateless
public class UpdateHealthInsuranceAvgearnCommandHandler
		extends CommandHandler<UpdateHealthInsuranceAvgearnCommand> {

	/** The health insurance avgearn repository. */
	@Inject
	private HealthInsuranceAvgearnRepository healthInsuranceAvgearnRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<UpdateHealthInsuranceAvgearnCommand> context) {
		// Get command.
		UpdateHealthInsuranceAvgearnCommand command = context.getCommand();

		// Map to domain
		List<HealthInsuranceAvgearn> healthInsuranceAvgearns = command
				.getListHealthInsuranceAvgearnDto().stream()
				.map(item -> item.toDomain(command.getHistoryId()))
				.collect(Collectors.toList());

		// Update
		healthInsuranceAvgearnRepository.update(healthInsuranceAvgearns,
				AppContexts.user().companyCode(), command.getOfficeCode());

	}
}