/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthrate.command;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.uk.ctx.pr.core.dom.insurance.CalculateMethod;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service.HealthInsuranceAvgearnService;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.service.HealthInsuranceRateService;

/**
 * The Class UpdateHealthInsuranceCommandHandler.
 */
@Stateless
public class UpdateHealthInsuRateCommandHandler
		extends CommandHandler<UpdateHealthInsuRateCommand> {

	/** The health insurance rate service. */
	@Inject
	private HealthInsuranceRateService healthInsuranceRateService;

	/** The health insurance rate repository. */
	@Inject
	private HealthInsuranceRateRepository healthInsuranceRateRepository;
	
	@Inject 
	private HealthInsuranceAvgearnService healthInsuranceAvgearnService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected void handle(CommandHandlerContext<UpdateHealthInsuRateCommand> context) {
		// Get command.
		UpdateHealthInsuRateCommand command = context.getCommand();

		// Get the history.
		Optional<HealthInsuranceRate> optHealthInsuranceRate = this.healthInsuranceRateRepository
				.findById(command.getHistoryId());

		// if is found
		if (optHealthInsuranceRate.isPresent()) {
			// Transfer data
			HealthInsuranceRate healthInsuranceRate = optHealthInsuranceRate.get();
			healthInsuranceRate.setAutoCalculate(command.getAutoCalculate());
			healthInsuranceRate.setMaxAmount(command.getMaxAmount());
			healthInsuranceRate.setRateItems(command.getRateItems());
			healthInsuranceRate.setRoundingMethods(command.getRoundingMethods());

			// Validate
			healthInsuranceRate.validate();
			this.healthInsuranceRateService.validateRequiredItem(healthInsuranceRate);

			this.healthInsuranceRateRepository.update(healthInsuranceRate);
			
			// if Autocalculate update avg earn
			if (healthInsuranceRate.getAutoCalculate().equals(CalculateMethod.Auto)) {
				healthInsuranceAvgearnService.updateHealthInsuranceRateAvgEarn(healthInsuranceRate);
			}
		}
		// if is not found.
		else {
			throw new BusinessException("ER010");
		}
	}

}
