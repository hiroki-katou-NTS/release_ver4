/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find.HealthInsAvgearnsModel;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find.HealthInsuranceAvgearnDto;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.find.HealthInsuranceAvgearnValueDto;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.HealthInsuranceAvgearnValue;
import nts.uk.ctx.pr.core.dom.insurance.social.healthavgearn.service.HealthInsuranceAvgearnService;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRate;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRateRepository;
import nts.uk.ctx.pr.core.dom.insurance.social.healthrate.HealthInsuranceRounding;

/**
 * The Class UpdateHealthInsuranceAvgearnCommandHandler.
 */
@Stateless
public class RecalculateHealthInsuAvgearnCommandHandler extends
		CommandHandlerWithResult<RecalculateHealthInsuAvgearnCommand, HealthInsAvgearnsModel> {

	/** The health insurance rate repository. */
	@Inject
	private HealthInsuranceRateRepository healthInsuranceRateRepository;

	/** The health insurance avgearn repository. */
	@Inject
	private HealthInsuranceAvgearnRepository healthInsuranceAvgearnRepository;

	/** The pension avgearn service. */
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
	protected HealthInsAvgearnsModel handle(
			CommandHandlerContext<RecalculateHealthInsuAvgearnCommand> context) {
		// Get command.
		RecalculateHealthInsuAvgearnCommand command = context.getCommand();

		Optional<HealthInsuranceRate> optHealthInsuranceRate = healthInsuranceRateRepository
				.findById(command.getHistoryId());

		if (!optHealthInsuranceRate.isPresent()) {
			throw new BusinessException(new RawErrorMessage("Rate isn't exist."));
		}

		HealthInsuranceRate healthInsuranceRate = optHealthInsuranceRate.get();

		Set<HealthInsuranceRounding> roundingMethods = healthInsuranceRate.getRoundingMethods();

		// Map to domain
		List<HealthInsuranceAvgearn> listDomain = healthInsuranceAvgearnRepository
				.findById(command.getHistoryId());

		// Map to list Dto.
		List<HealthInsuranceAvgearnDto> listDto = listDomain.stream().map(domain -> {
			HealthInsuranceAvgearnDto dto = new HealthInsuranceAvgearnDto();
			dto = dto.fromDomain(domain);

			// Re-calculate
			// Company item
			HealthInsuranceAvgearnValue companyAvg = healthInsuranceAvgearnService
					.calculateAvgearnValue(roundingMethods, BigDecimal.valueOf(domain.getAvgEarn()),
							healthInsuranceRate.getRateItems(), false);
			dto.setCompanyAvg(new HealthInsuranceAvgearnValueDto(companyAvg.getHealthBasicMny().v(),
					companyAvg.getHealthGeneralMny().v(), companyAvg.getHealthNursingMny().v(),
					companyAvg.getHealthSpecificMny().v()));

			// Personal item
			HealthInsuranceAvgearnValue personalAvg = healthInsuranceAvgearnService
					.calculateAvgearnValue(roundingMethods, BigDecimal.valueOf(domain.getAvgEarn()),
							healthInsuranceRate.getRateItems(), true);
			dto.setPersonalAvg(new HealthInsuranceAvgearnValueDto(
					personalAvg.getHealthBasicMny().v(), personalAvg.getHealthGeneralMny().v(),
					personalAvg.getHealthNursingMny().v(), personalAvg.getHealthSpecificMny().v()));

			return dto;
		}).collect(Collectors.toList());

		HealthInsAvgearnsModel healthInsAvgearnsModel = HealthInsAvgearnsModel.builder()
				.listHealthInsuranceAvgearnDto(listDto).historyId(command.getHistoryId()).build();

		return healthInsAvgearnsModel;

	}
}