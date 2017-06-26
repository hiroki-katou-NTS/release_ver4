/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.command;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

import nts.arc.error.BusinessException;
import nts.arc.error.RawErrorMessage;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.uk.ctx.pr.core.app.insurance.social.healthavgearn.command.RecalculateHealthInsuAvgearnCommand;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find.ListPensionAvgearnModel;
import nts.uk.ctx.pr.core.app.insurance.social.pensionavgearn.find.PensionAvgearnDto;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.PensionAvgearn;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionavgearn.service.PensionAvgearnService;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRate;
import nts.uk.ctx.pr.core.dom.insurance.social.pensionrate.PensionRateRepository;

/**
 * The Class UpdateHealthInsuranceAvgearnCommandHandler.
 */
@Stateless
public class RecalculatePensionAvgearnCommandHandler extends
		CommandHandlerWithResult<RecalculateHealthInsuAvgearnCommand, ListPensionAvgearnModel> {

	/** The health insurance rate repository. */
	@Inject
	private PensionRateRepository pensionRateRepository;

	/** The pension avgearn service. */
	@Inject
	private PensionAvgearnService pensionAvgearnService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.arc.layer.app.command.CommandHandler#handle(nts.arc.layer.app.command
	 * .CommandHandlerContext)
	 */
	@Override
	@Transactional
	protected ListPensionAvgearnModel handle(
			CommandHandlerContext<RecalculateHealthInsuAvgearnCommand> context) {
		// Get command.
		RecalculateHealthInsuAvgearnCommand command = context.getCommand();

		Optional<PensionRate> optPensionRate = pensionRateRepository
				.findById(command.getHistoryId());

		if (!optPensionRate.isPresent()) {
			throw new BusinessException(new RawErrorMessage("Rate isn't exist."));
		}

		PensionRate pensionRate = optPensionRate.get();

		// Map to domain
		List<PensionAvgearn> listDomain = pensionAvgearnService
				.calculateListPensionAvgearn(pensionRate);

		// Map to list Dto.
		List<PensionAvgearnDto> listPensionAvgearnDto = listDomain.stream().map(domain -> {
			return PensionAvgearnDto.builder().build().fromDomain(domain);
		}).collect(Collectors.toList());

		ListPensionAvgearnModel healthInsAvgearnsModel = ListPensionAvgearnModel.builder()
				.listPensionAvgearnDto(listPensionAvgearnDto).historyId(command.getHistoryId())
				.build();

		return healthInsAvgearnsModel;
	}
}